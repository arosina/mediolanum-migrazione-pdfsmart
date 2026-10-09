package prgm.ita.anagraficaclienti.questionari.display;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;

/*******************************************************************/
/*******************************************************************/
public class AlertAfterCalcolo extends DisplayCommand {
	
	/*******************************************************************/
	/*******************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		return dataModel;
	}

	/*******************************************************************/
	/*******************************************************************/
	public Class getInputViewClass() {
		return CommandDataModel.class;
	}

	/*******************************************************************/
	/*******************************************************************/
	@Override
	public boolean isNoSubmitCommand() {
		return true;
	}
	
	
}
