package prgm.pdfwebforms.stream;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DownloadStreamCommand;
import com.atosorigin.wfem.command.GenericCommandResponseModel;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.DAOTableResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ListType;

import prgm.pdfwebforms.core.PdfEngine;
import prgm.pdfwebforms.core.PdfNasUtil;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.publisher.model.PdfAnagKeyModel;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfDownload extends DownloadStreamCommand{

	private byte[] fileContent = null;
	private String fileName = null;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext,
									CommandDataModel dataModel) throws CommandException {
		
		try{
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			DAOObject dao = new DAOObject(csc,"PdfWebForms.PdfAsStream");

			PdfAnagKeyModel pdfKey = (PdfAnagKeyModel)dataModel;
			
			if(!pdfKey.getPdfCode().isNull() && pdfKey.getPdfId().isNull()){
				int numRes = -1;
				ListType elenco = new ListType();
				DAOQueryResultModel qRes = dao.executeQueryAccess("loadPdfIdFromCode",pdfKey);
				if(qRes.getResult().size() == 0)
					qRes = dao.executeQueryAccess("loadPdfIdFromMomCode",pdfKey);
				elenco = qRes.getResult();
				numRes = elenco.size();
				if(numRes == 1){
					PdfAnagModel pdfAnag = (PdfAnagModel)elenco.get(0);
					pdfKey.setPdfId(pdfAnag.getPdfId());
				}else{
					GenericCommandResponseModel resp = new GenericCommandResponseModel();
					fileName = pdfKey.getPdfCode().toString();
					setGenericCommandResponse(resp);
					return null;
				}
			}
			
			dao.executeQueryAccess("loadCurrentPubblicationId",pdfKey);
			
			PdfInstanceModel pdfInstance = new PdfInstanceModel();
			pdfInstance.setPdfAnag(new PdfAnagModel(pdfKey));
			boolean fileExist = true;
			String title = null;
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
					fileContent = PdfOpen.putBarcodeField(dao, fileContent, pdfKey);
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
				GenericCommandResponseModel resp = new GenericCommandResponseModel();
				fileName = pdfKey.getPdfId().toString();
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
			
			resp.setContent(fileContent);
			setGenericCommandResponse(resp);
			return null;
			
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
	public String getFileName() {
		return fileName;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public int getFileLength() {
		return fileContent.length;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public InputStream getInputStream() {
		return fileContent == null ? null : new ByteArrayInputStream(fileContent);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void downloadTerminated() {
		
	}
}
