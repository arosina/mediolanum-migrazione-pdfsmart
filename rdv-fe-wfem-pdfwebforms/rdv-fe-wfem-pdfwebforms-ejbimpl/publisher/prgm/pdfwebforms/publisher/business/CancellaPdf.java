package prgm.pdfwebforms.publisher.business;

import prgm.pdfwebforms.publisher.backend.PdfAnagFacade;
import prgm.pdfwebforms.publisher.model.PdfPublisherModel;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.util.FacadeLoader;

/***********************************************************************************************/
/***********************************************************************************************/
public class CancellaPdf extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfPublisherModel model = (PdfPublisherModel)dataModel;
			PdfAnagFacade facade = (PdfAnagFacade)FacadeLoader.getFacade(csc, PdfAnagFacade.class); 
			facade.deletePdf(csc,model.getPdfDaGestire());
			setForwardDisplay(new Integer(0));
			return model;			
		
		}catch(Exception e){
			CommandException ce = new CommandException(e.toString());
			LOG.error(ce);
			throw ce;
		}
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfPublisherModel.class;
	}

}
