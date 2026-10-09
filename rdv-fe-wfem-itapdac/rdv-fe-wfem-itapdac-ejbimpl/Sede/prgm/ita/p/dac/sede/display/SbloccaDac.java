package prgm.ita.p.dac.sede.display;

import prgm.ita.p.dac.sede.model.SbloccaDacModel;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;

public class SbloccaDac extends DisplayCommand implements MenuCommand {

	public CommandDataModel execute(UserSessionContext userSessionContext,
			CommandDataModel dataModel) throws CommandException {

		return dataModel;
	}

	public Class getInputViewClass() {
		return SbloccaDacModel.class;
	}

}
