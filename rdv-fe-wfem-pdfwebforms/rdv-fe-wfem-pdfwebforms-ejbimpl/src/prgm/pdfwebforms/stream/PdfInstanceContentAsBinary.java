package prgm.pdfwebforms.stream;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.GenericCommandResponseModel;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ByteArrayType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;
import com.itextpdf.text.pdf.PdfCopyFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import prgm.pdfwebforms.core.PdfEngine;
import prgm.pdfwebforms.core.PdfFilenetUtil;
import prgm.pdfwebforms.core.PdfNasUtil;
import prgm.pdfwebforms.model.PdfInstanceModel;

/*******************************************************************/
/*******************************************************************/
public class PdfInstanceContentAsBinary extends BusinessCommand {

	/*******************************************************************/
	/*******************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		
		try {
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfInstanceModel pdfInstanceKey = (PdfInstanceModel)Tools.cloneObject(dataModel);
			if(pdfInstanceKey.getPdfInstanceId().isNull())
				throw new CommandException("Instance ID non specificato");

			byte[] result = null;
			String[] idPdfs = pdfInstanceKey.getPdfInstanceId().toString().split("\\,");
			if(idPdfs.length > 1){
				
				ArrayList<byte[]> allPdf = new ArrayList<byte[]>();
				for(int i=0;i<idPdfs.length;i++) {
					pdfInstanceKey.setPdfInstanceId(new StringType(idPdfs[i]));
					StringType filenetGuid = PdfFilenetUtil.readInstanceFilenetGuid(csc, pdfInstanceKey.getPdfInstanceId());
					byte[] singleResult = readPdfContent(csc, pdfInstanceKey, filenetGuid);
					if(singleResult.length > 0)
						allPdf.add(singleResult);
				}
				if(!allPdf.isEmpty())
					result = doAllPdf(allPdf);
				
			}else if(idPdfs.length == 1){
				
				StringType filenetGuid = PdfFilenetUtil.readInstanceFilenetGuid(csc, pdfInstanceKey.getPdfInstanceId());
				if(filenetGuid != null && !pdfInstanceKey.getInProcessPdfInstanceContentAsBinary().booleanValue()){
					GenericCommandResponseModel filenetResp = PdfFilenetUtil.readFilenetUrlFromGuid(csc, filenetGuid);
					if(filenetResp != null){
						setGenericCommandResponse(filenetResp);
						return null;
					}
				}
				result = readPdfContent(csc, pdfInstanceKey, filenetGuid);
				
			}else{
				throw new CommandException("Instance/Basket ID specificato in modo non corretto");
			}

			GenericCommandResponseModel resp = new GenericCommandResponseModel();
			if(result == null || result.length == 0) {
				String errResp = "<center><span style='font-family: Segoe UI;color:red;font-size:13px;'>Si &egrave; verificato un errore nel recuperare il pdf</span></center>";
				resp.setContentType("text/html");
				resp.setContentLength(errResp.length());
				resp.setContent(errResp.getBytes());
			}else{
				resp.setContentType("application/pdf");
				resp.setContentLength(result.length);
				resp.setContent(result);
				resp.setSuggestedFileName(pdfInstanceKey.getPdfAnag().getTitle()+"-"+pdfInstanceKey.getPdfInstanceId()+".pdf");
			}
			setGenericCommandResponse(resp);
			pdfInstanceKey.setPdfContent(null);
			return null;
			
		}catch(DAOException daoe){
			CommandException ce = new CommandException(daoe.toString());
			LOG.error(ce);
			throw ce;
		}catch(Exception e){
			CommandException ce = new CommandException(e.toString());
			LOG.error(ce);
			throw ce;
		}
	}

	/*******************************************************************/
	/*******************************************************************/
	public Class getInputViewClass() {
		return PdfInstanceModel.class;
	}

	/*******************************************************************/
	/*******************************************************************/
	private byte[] readPdfContent(ClientSessionContext csc, PdfInstanceModel pdfInstance, StringType filenetGuid) throws Exception, DAOException{
		if(filenetGuid != null) {
			return PdfFilenetUtil.readFilenetContentFromGuid(csc, filenetGuid);
		}else{
			new DAOObject(csc,"PdfWebForms.PdfInstance").executeQueryAccess("loadPdfInstanceContent",pdfInstance);
			if(pdfInstance.getPdfContent() == null || pdfInstance.getPdfContent().isNull())
				pdfInstance.setPdfContent(new ByteArrayType(PdfNasUtil.PDF_INSTANCE.readPdfInstanceContent(csc, pdfInstance.getPdfInstanceId())));		
			if(pdfInstance.getPdfContent() == null || pdfInstance.getPdfContent().isNull())
				return new byte[0];
			return pdfInstance.getPdfContent().byteArrayValue();
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private byte[] doAllPdf(ArrayList<byte[]> allPdf) throws Exception{
		
		ByteArrayOutputStream allPdfOut = new ByteArrayOutputStream();
		PdfCopyFields copy = new PdfCopyFields(allPdfOut);
		for(byte[] pdfByte : allPdf) {
			
			PdfReader reader = new PdfReader(pdfByte);			
			ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
		    PdfStamper stamp = new PdfStamper(reader, pdfOut);
		    stamp.setFormFlattening(true);
		    stamp.close();
		    reader.close();
		    pdfOut.close();
		    
		    reader = new PdfReader(pdfOut.toByteArray());	
			copy.addDocument(reader);
		    
		}
		copy.close();
		allPdfOut.close();		
		return PdfEngine.putTitle("Modulo", allPdfOut.toByteArray(), true);
	}	
	
}
