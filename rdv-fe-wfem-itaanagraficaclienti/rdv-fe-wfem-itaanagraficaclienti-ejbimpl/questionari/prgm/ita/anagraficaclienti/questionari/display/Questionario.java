package prgm.ita.anagraficaclienti.questionari.display;

import prgm.ita.anagraficaclienti.questionari.model.QuestionarioModel;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;

/*******************************************************************/
/*******************************************************************/
public class Questionario extends DisplayCommand {
	
	/*******************************************************************/
	/*******************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		QuestionarioModel model = (QuestionarioModel)dataModel;
		return model;
	}

	/*******************************************************************/
	/*******************************************************************/
	public Class getInputViewClass() {
		return QuestionarioModel.class;
	}
}
