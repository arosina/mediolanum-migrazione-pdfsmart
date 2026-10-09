package prgm.pdfwebforms.display;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;

/*******************************************************************/
/*******************************************************************/
public class PdfErrorsOnPreview extends DisplayCommand{

	/*******************************************************************/
	/*******************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		return dataModel;
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
