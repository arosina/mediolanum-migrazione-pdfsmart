package prgm.pdfwebforms.sendprocess.display;

import prgm.pdfwebforms.core.ProcessUtils;
import prgm.pdfwebforms.model.PdfModel;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;

/*******************************************************************/
/*******************************************************************/
public class PdfEndSendProcess extends DisplayCommand{

	/*******************************************************************/
	/*******************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		if(!(dataModel instanceof PdfModel))
			return dataModel;
		
        ClientSessionContext csc = userSessionContext.getClientSessionContext();
        PdfModel pdf = (PdfModel)dataModel;
    	// Carico la url del NAC per il refresh finale
		ProcessUtils.loadNacUrl(csc, pdf);
        return pdf;
	}

	/*******************************************************************/
	/*******************************************************************/
	public Class<CommandDataModel> getInputViewClass() {
		return CommandDataModel.class;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isStepCommand() {
		return true;
	}
}
