package prgm.pdfwebforms.publisher.business;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.GenericCommandResponseModel;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;

import prgm.pdfwebforms.core.PdfNasUtil;
import prgm.pdfwebforms.publisher.backend.PdfAnagFacadeBean;
import prgm.pdfwebforms.publisher.common.Utility;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class CreatePdfDataHelperPdfPubblicato extends BusinessCommand{
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfAnagModel pdfAnag = (PdfAnagModel)dataModel;
			
			DAOObject dao = new DAOObject(csc,PdfAnagFacadeBean.DAO_XML_NAME);
			dao.executeTableLoadAccess("pdfAnag",pdfAnag);
			dao.executeTableLoadAccess("pdfPublication",pdfAnag);
			PdfNasUtil.PDF_PUBLICATION.loadPdfPublicationContent(csc, pdfAnag);
		
			String source = Utility.generatePdfDataHelperSource(pdfAnag, pdfAnag.getPdfContent().getFileContent());

			GenericCommandResponseModel resp = new GenericCommandResponseModel();
			byte[] content = source.getBytes(); 
			resp.setContentType("text/plain");
			resp.setContent(content);
			resp.setContentLength(content.length);
			setGenericCommandResponse(resp);
			pdfAnag = null;
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

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfAnagModel.class;
	}

}
