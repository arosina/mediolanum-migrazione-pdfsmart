package prgm.pdfwebforms.publisher.display;

import prgm.pdfwebforms.publisher.backend.PdfAnagFacadeBean;
import prgm.pdfwebforms.publisher.model.PdfAnagKeyModel;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfPublicationNotes extends DisplayCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfAnagKeyModel key = (PdfAnagKeyModel)dataModel;
			
			PdfAnagModel model = new PdfAnagModel();
			model.setPdfId(key.getPdfId());
			model.setPdfPublicationId(key.getPdfPublicationId());
			new DAOObject(csc,PdfAnagFacadeBean.DAO_XML_NAME).executeQueryAccess("loadPdfPublicationNotes",model);
			return model;			
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
		return PdfAnagKeyModel.class;
	}

}
