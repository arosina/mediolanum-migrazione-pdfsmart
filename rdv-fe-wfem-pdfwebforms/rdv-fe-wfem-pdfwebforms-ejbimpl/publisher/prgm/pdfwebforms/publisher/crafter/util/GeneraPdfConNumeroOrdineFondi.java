package prgm.pdfwebforms.publisher.crafter.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Iterator;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.GenericCommandResponseModel;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.StringType;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfFormField;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import prgm.pdfwebforms.core.PdfEngine;
import prgm.pdfwebforms.core.PdfPredefinedFields;
import prgm.pdfwebforms.drivers.PdfBaseDriverUtil;
import prgm.pdfwebforms.stream.PdfOpen;

/***********************************************************************************************/
/***********************************************************************************************/
public class GeneraPdfConNumeroOrdineFondi extends PdfOpen{
	
	private static final long serialVersionUID = 1L;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public CommandDataModel execute(UserSessionContext userSessionContext,
									CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			GeneraPdfConNumeroOrdineFondiInputModel pdfKey = (GeneraPdfConNumeroOrdineFondiInputModel)dataModel;

			StringType numeroOrdineFondi = null;
			String errorMessage = null;
			if(pdfKey.getPdfId().isNull()){
				errorMessage = "Id del pdf non specificato";
			}else if(pdfKey.getTipoMandato().isNull()){
				errorMessage = "Tipo mandato non specificato";
			}else{
				PdfBaseDriverUtil driverUtil = new PdfBaseDriverUtil();
				numeroOrdineFondi = driverUtil.getNumeroOrdineFondi(csc, pdfKey.getNdg(), pdfKey.getTipoMandato().toString());
				if(numeroOrdineFondi == null || numeroOrdineFondi.isNull())
					errorMessage = "Si è verificato un errore nel recuperare il numero d'ordine";
			}
			
			if(errorMessage != null){
				GenericCommandResponseModel resp = new GenericCommandResponseModel();
				resp.setContentType("text/html");
				resp.setContent(errorMessage.getBytes());
				resp.setContentLength(errorMessage.length());
				setGenericCommandResponse(resp);
				return null;
			}
						
			pdfKey.setAsFacsimile(new BooleanType(false));
			pdfKey = (GeneraPdfConNumeroOrdineFondiInputModel)super.execute(userSessionContext, pdfKey);
			if(pdfKey != null){
				Object cmdResult = getNextCommandObject();
				if(cmdResult instanceof GenericCommandResponseModel){
					GenericCommandResponseModel gcrm = (GenericCommandResponseModel)cmdResult;
					byte[] fileContent = gcrm.getContent();
					fileContent = enableAllFields(fileContent, numeroOrdineFondi.toString());
					GenericCommandResponseModel resp = new GenericCommandResponseModel();
					resp.setContentType("application/pdf");
					resp.setContent(fileContent);
					resp.setContentLength(fileContent.length);
					resp.setSuggestedFileName(gcrm.getSuggestedFileName());
					setGenericCommandResponse(resp);
				}
			}
			return null;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nel generare il pdf con il numero ordine dei fondi: "+e;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public Class getInputViewClass() {
		return GeneraPdfConNumeroOrdineFondiInputModel.class;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static byte[] enableAllFields(byte[] fileContent, String numeroProposta) throws DocumentException{
		try {
			InputStream pdf = new ByteArrayInputStream(fileContent);
			ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
		    PdfReader reader = new PdfReader(pdf);
		    PdfStamper stamp = new PdfStamper(reader, pdfOut);
	
		    AcroFields acroForm = stamp.getAcroFields();
	    	Iterator<String> fields = acroForm.getFields().keySet().iterator();
	    	while(fields.hasNext()){
	    		
	    		String pdfFieldName = fields.next();
	    		int fieldType = acroForm.getFieldType(pdfFieldName);
	    		
	    		if( fieldType == AcroFields.FIELD_TYPE_SIGNATURE)
	    			continue;
	    		
	    		boolean isCopiaPer = pdfFieldName.equals(PdfPredefinedFields.COPIA_PER) || pdfFieldName.matches(PdfPredefinedFields.COPIA_PER+"(\\d+)");
	    		if( pdfFieldName.equals(PdfPredefinedFields.NUMERO_PROPOSTA) ||
	    			pdfFieldName.equals(PdfPredefinedFields.BARCODE) ||
	    			isCopiaPer){
	    			acroForm.setFieldProperty(pdfFieldName, "setfflags", PdfFormField.FF_READ_ONLY, null); 
	    		}else{
	    			acroForm.setFieldProperty(pdfFieldName, "clrfflags", PdfFormField.FF_READ_ONLY, null);
	    		}
	    		if(pdfFieldName.equals(PdfPredefinedFields.NUMERO_PROPOSTA))
	    			acroForm.setField(pdfFieldName, numeroProposta);
	    		else if(!isCopiaPer)
	    			PdfEngine.setAcroformField(acroForm,pdfFieldName, "");
	    	}
			stamp.setFormFlattening(false);
		    stamp.close();
		    reader.close();
		    pdfOut.close();
			pdf.close();
			return pdfOut.toByteArray();
		}catch(Exception e) {
			throw new DocumentException(e.toString());
		}
	}
	
}
