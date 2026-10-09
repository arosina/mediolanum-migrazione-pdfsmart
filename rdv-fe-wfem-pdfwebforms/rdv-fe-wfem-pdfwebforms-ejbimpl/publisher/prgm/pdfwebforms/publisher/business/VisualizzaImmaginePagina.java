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
import prgm.pdfwebforms.publisher.model.PdfPageAnagModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class VisualizzaImmaginePagina extends BusinessCommand{
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfPageAnagModel model = (PdfPageAnagModel)dataModel;
			
			DAOObject dao = new DAOObject(csc,PdfAnagFacadeBean.DAO_XML_NAME);
			dao.executeTableLoadAccess("pdfPage_WORK",model);
			PdfNasUtil.PDF_PAGE_WORK.loadPdfPageWorkContent(csc, model);
		
			GenericCommandResponseModel resp = new GenericCommandResponseModel();
			byte[] content = model.getPdfPageImg().byteArrayValue();
			resp.setContentType("image/jpeg");
			resp.setContent(content);
			resp.setContentLength(content.length);
			setGenericCommandResponse(resp);
			model = null;
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
		return PdfPageAnagModel.class;
	}

}
