package prgm.ita.anagraficaclienti.questionari.display;

import prgm.ita.anagraficaclienti.questionari.model.PatrimonioModel;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;

/*******************************************************************/
/*******************************************************************/
public class Patrimonio extends DisplayCommand {
	
	/*******************************************************************/
	/*******************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		PatrimonioModel model = (PatrimonioModel)dataModel;
		return model;
	}

	/*******************************************************************/
	/*******************************************************************/
	public Class getInputViewClass() {
		return PatrimonioModel.class;
	}
}
