package prgm.pdfwebforms.stream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.GenericCommandResponseModel;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.DAOTableResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

import prgm.pdfwebforms.core.PdfEngine;
import prgm.pdfwebforms.core.PdfNasUtil;
import prgm.pdfwebforms.core.PdfPredefinedFields;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.publisher.model.PdfAnagKeyModel;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfOpen extends BusinessCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext,
									CommandDataModel dataModel) throws CommandException {
		
		try{
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			DAOObject dao = new DAOObject(csc,"PdfWebForms.PdfAsStream");

			PdfAnagKeyModel pdfKey = (PdfAnagKeyModel)dataModel;

			int numRes = 1;
			ListType elenco = new ListType();
			if(!pdfKey.getPdfCode().isNull() && pdfKey.getPdfId().isNull()){
				DAOQueryResultModel qRes = dao.executeQueryAccess("loadPdfIdFromCode",pdfKey);
				if(qRes.getResult().size() == 0)
					qRes = dao.executeQueryAccess("loadPdfIdFromMomCode",pdfKey);
				elenco = qRes.getResult();
				numRes = elenco.size();
				if(numRes == 0){
					String message = "Codice modulo ["+pdfKey.getPdfCode()+"] non trovato";
					GenericCommandResponseModel resp = new GenericCommandResponseModel();
					resp.setContentType("text/html");
					resp.setContent(message.getBytes());
					resp.setContentLength(message.length());
					setGenericCommandResponse(resp);
					return null;
				}else if(numRes == 1){
					PdfAnagModel pdfAnag = (PdfAnagModel)elenco.get(0);
					pdfKey.setPdfId(pdfAnag.getPdfId());
				}else{
					StringBuffer html = new StringBuffer();
					html.append("<html>");
					html.append(	"<script>");
					html.append(	"function openPdf(obj){");
					html.append(		"window.open('call.wfem?wfemCmd=prgm.pdfwebforms.stream.PdfOpen.execute&pdfId='+obj.getAttribute('pdfId'));");
					html.append(	"}");
					html.append(	"</script>");
					html.append(	"<table>");
					for(int i=0;i<elenco.size();i++){
						PdfAnagModel pdfAnag = (PdfAnagModel)elenco.get(i);
						html.append(	"<tr><td style='font-family: Segoe UI;font-size: 8pt;color: #666666;'>");
						html.append(		"<span pdfId='"+pdfAnag.getPdfId()+"' style='cursor:pointer;' onclick='openPdf(this);' "+
												  "onmouseover='this.style.textDecoration=\"underline\";' "+
												  "onmouseout='this.style.textDecoration=\"none\";'>"+
												  pdfAnag.getPdfDescr()+
											"</span>");
						html.append(	"</td></tr>");
					}
					html.append(	"</table>");
					html.append("</html>");
					GenericCommandResponseModel resp = new GenericCommandResponseModel();
					resp.setContentType("text/html");
					resp.setContent(html.toString().getBytes());
					resp.setContentLength(html.length());
					setGenericCommandResponse(resp);
					return null;
				}
			}
			
			dao.executeQueryAccess("loadCurrentPubblicationId",pdfKey);
			
			PdfInstanceModel pdfInstance = new PdfInstanceModel();
			pdfInstance.setPdfAnag(new PdfAnagModel(pdfKey));
			boolean fileExist = true;
			String title = null;
			String fileName = null;
			byte[] fileContent = null;
			if(!pdfKey.getPdfPublicationId().isNull() && pdfKey.getPdfPublicationId().intValue() != PdfAnagKeyModel.NO_PUBLICATION){
				DAOQueryResultModel qRes = dao.executeQueryAccess("blobPdfPubblicato",pdfInstance);
				if(qRes.getResult().size() != 1){
					fileExist = false;
				}else{
					title = pdfInstance.getPdfAnag().getTitle();
					fileName = pdfInstance.getPdfAnag().getTitleAsFileName();
					fileContent = pdfInstance.getPdfContent().byteArrayValue();
					if(fileContent == null)
						fileContent = PdfNasUtil.PDF_PUBLICATION.loadPdfPublicationContent(csc, pdfInstance.getPdfAnag().getPdfId(), pdfInstance.getPdfAnag().getPdfPublicationId());
					fileContent = PdfEngine.compilePdfForDownload(fileContent, pdfInstance.getPdfAnag());
					fileContent = putBarcodeField(dao, fileContent, pdfKey);
				}
			}else{
				DAOTableResultModel tRes = dao.executeTableLoadAccess("blobPdfCatalogo",pdfInstance.getPdfAnag());
				if(tRes.getResult().intValue() != 1){
					fileExist = false;
				}else{
					fileName = pdfInstance.getPdfAnag().getPdfContent().getFileName();
					fileContent = pdfInstance.getPdfAnag().getPdfContent().getFileContent();
				}
			}
			
			if(!fileExist){
				String message = "ID modulo ["+pdfKey.getPdfId()+"] non trovato";
				GenericCommandResponseModel resp = new GenericCommandResponseModel();
				resp.setContentType("text/html");
				resp.setContent(message.getBytes());
				resp.setContentLength(message.length());
				setGenericCommandResponse(resp);
				return null;
			}
			
			GenericCommandResponseModel resp = new GenericCommandResponseModel();
			
			boolean pdfAsFacsimile = false;
			if(pdfKey.getAsFacsimile() != null){
				if(pdfKey.getAsFacsimile().booleanValue())
					pdfAsFacsimile = true;
			}else{
				BooleanType isPdfAsFacsimile = (BooleanType)dao.executeQueryAccess("isPdfAsFacsimile",pdfInstance.getPdfAnag()).getSingleResult();
				if(isPdfAsFacsimile != null && isPdfAsFacsimile.booleanValue())
					pdfAsFacsimile = true;
			}
			if(pdfAsFacsimile)
				fileContent = PdfEngine.generateAsFacsimile(fileContent);
			
			if(title != null)
				fileContent = PdfEngine.putTitle(title, fileContent, pdfAsFacsimile ? true: false);

			resp.setContentType("application/pdf");
			resp.setContent(fileContent);
			resp.setContentLength(fileContent.length);
			resp.setSuggestedFileName(fileName);
			setGenericCommandResponse(resp);
			return pdfKey;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO nell'aprire l'allegato: "+daoe;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nell'aprire l'allegato: "+e;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfAnagKeyModel.class;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	static byte[] putBarcodeField(DAOObject dao, byte[] fileContent, PdfAnagKeyModel pdfKey) throws DocumentException{
		if(pdfKey.getGeneraBarcode().isNull())
			return fileContent;

		StringType barcodeSeq = null;
		try {
			String accessName = "get_"+pdfKey.getGeneraBarcode().toString().toUpperCase()+"_BarcodeValue";
			barcodeSeq = (StringType)dao.executeQueryAccess(accessName, pdfKey).getSingleResult();
		}catch(DAOException daoe) {
			return fileContent;
		}
		if(barcodeSeq == null || barcodeSeq.isNull())
			return fileContent;
		
		try {
			InputStream pdf = new ByteArrayInputStream(fileContent);
			ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
		    PdfReader reader = new PdfReader(pdf);
		    PdfStamper stamp = new PdfStamper(reader, pdfOut);
		    AcroFields acroForm = stamp.getAcroFields();
		    if(acroForm.getField(PdfPredefinedFields.BARCODE) != null) {
		    	String barcode = ""+pdfKey.getPdfMomCode()+pdfKey.getPdfMomVersion()+"C"+Tools.fillSx(barcodeSeq.toString(),'0',10);
		    	PdfEngine.createBarcode(new PdfModel(), stamp, acroForm, PdfPredefinedFields.BARCODE, new StringType(barcode));
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
