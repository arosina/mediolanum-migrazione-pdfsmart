package prgm.pdfwebforms.business;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.GenericCommandResponseModel;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ByteArrayType;

import prgm.pdfwebforms.core.PdfNasUtil;
import prgm.pdfwebforms.model.PdfPageIdxModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class PageImage extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfPageIdxModel pdfPageIdx = (PdfPageIdxModel)dataModel;
			
			GenericCommandResponseModel resp = new GenericCommandResponseModel();
			DAOObject dao = new DAOObject(csc,"PdfWebForms.PdfWebForms");
			String daoAccess = pdfPageIdx.getPdfOnWork().booleanValue() ? "loadPdfPageImageOnWork" : "loadPdfPageImage";
			ByteArrayType img = (ByteArrayType)dao.executeQueryAccess(daoAccess,pdfPageIdx).getSingleResult();
			if(img == null || img.isNull())
				img = pdfPageIdx.getPdfOnWork().booleanValue() ? PdfNasUtil.PDF_PAGE_WORK.loadPdfPageWorkContent(csc, pdfPageIdx) : PdfNasUtil.PDF_PAGE.loadPdfPageContent(csc, pdfPageIdx);
			
			if(img != null && !img.isNull()){
				byte[] content = img.byteArrayValue(); 
				resp.setContentType("image/jpeg");
				resp.setContent(content);
				resp.setContentLength(content.length);
			}
			setGenericCommandResponse(resp);
			return null;
			
		}catch(DAOException daoe){
			throw new CommandException(daoe.toString());
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfPageIdxModel.class;
	}

}
