package prgm.pdfwebforms.copernicoprocess.display;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;

import prgm.pdfwebforms.model.PdfModel;

/*******************************************************************/
/*******************************************************************/
public class PdfNoteFbCopernico extends DisplayCommand{

	/*******************************************************************/
	/*******************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		ClientSessionContext csc = userSessionContext.getClientSessionContext();
		PdfModel pdf = (PdfModel)dataModel;
		pdf.initMessaggioCopernico(csc);
		return pdf;
	}

	/*******************************************************************/
	/*******************************************************************/
	public Class<PdfModel> getInputViewClass() {
		return PdfModel.class;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isStepCommand() {
		return true;
	}
}
