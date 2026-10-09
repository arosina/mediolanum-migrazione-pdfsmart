package prgm.pdfwebforms.publisher.business;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;

import prgm.pdfwebforms.core.PdfNasUtil;
import prgm.pdfwebforms.publisher.backend.PdfAnagFacadeBean;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;
import prgm.pdfwebforms.publisher.model.PdfConfigurationModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class RestoreFilePdf extends BusinessCommand{
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			
            ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfConfigurationModel model = (PdfConfigurationModel)dataModel;
			PdfAnagModel pdf = model.getPdfAnag();
			DAOObject dao = new DAOObject(csc,PdfAnagFacadeBean.DAO_XML_NAME);
			dao.executeTableLoadAccess("pdfFile_WORK",pdf);
			PdfNasUtil.PDF_PUBLICATION_WORK.loadPdfPublicationWorkContent(csc, pdf);
			setForwardDisplay(new Integer(0));
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
		return PdfConfigurationModel.class;
	}

}
