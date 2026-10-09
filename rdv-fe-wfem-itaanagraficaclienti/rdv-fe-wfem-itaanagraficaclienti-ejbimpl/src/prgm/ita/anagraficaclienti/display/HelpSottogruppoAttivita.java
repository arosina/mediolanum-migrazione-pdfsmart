package prgm.ita.anagraficaclienti.display;

import prgm.ita.anagraficaclienti.model.ClienteModel;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;

public class HelpSottogruppoAttivita extends DisplayCommand {

	public CommandDataModel execute(UserSessionContext userSessionContext,	CommandDataModel dataModel) throws CommandException {
		return dataModel;
	}

	public Class getInputViewClass() {
		return ClienteModel.class;
	}

	public boolean isNoSubmitCommand() {
		return true;
	}

}
