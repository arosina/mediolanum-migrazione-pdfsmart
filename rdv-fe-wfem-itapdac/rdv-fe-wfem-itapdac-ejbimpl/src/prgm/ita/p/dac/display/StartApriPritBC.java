package prgm.ita.p.dac.display;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;

import prgm.ita.p.dac.model.DacKeyModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class StartApriPritBC extends DisplayCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		return dataModel;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return DacKeyModel.class;
	}
	
	/*******************************************************************/
	/*******************************************************************/
	@Override
	public boolean isNoSubmitCommand() {
		return true;
	}
	
}
