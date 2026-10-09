package prgm.ita.anagraficaclienti.questionari.business;

import prgm.ita.anagraficaclienti.questionari.display.Questionario;
import prgm.ita.anagraficaclienti.questionari.facade.QuestionariFacade;
import prgm.ita.anagraficaclienti.questionari.model.QuestionarioModel;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

/*******************************************************************/
/*******************************************************************/
public class SalvaQuestionario extends BusinessCommand {

	/*******************************************************************/
	/*******************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try {
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			QuestionarioModel questionario = (QuestionarioModel)dataModel;
			
			QuestionariFacade facade = (QuestionariFacade)ROF.getFacade(csc,QuestionariFacade.class);
			questionario = facade.salvaQuestionario(csc,questionario);
			setNextCommandClass(Questionario.class);
			return questionario;
			
		}catch(Exception e){
			throw new CommandException(e.toString());
		}		
	}

	/*******************************************************************/
	/*******************************************************************/
	public Class getInputViewClass() {
		return QuestionarioModel.class;
	}

}
