package prgm.pdfwebforms.display;

import prgm.pdfwebforms.backend.PdfInstanceFacade;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.util.FacadeLoader;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfInstanceContentAsPage extends DisplayCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfInstanceModel pdfInstanceKey = (PdfInstanceModel)dataModel;
			
			PdfDataModel pdfData = new PdfDataModel();
			pdfData.setPdfInstanceId(pdfInstanceKey.getPdfInstanceId());
			
			PdfInstanceFacade facade = (PdfInstanceFacade)FacadeLoader.getFacade(csc, PdfInstanceFacade.class);
			PdfModel pdf = facade.readPdf(csc, pdfData);
			
			return pdf;
			
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PdfInstanceModel.class;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isNoSubmitCommand() {
		return true;
	}


}
