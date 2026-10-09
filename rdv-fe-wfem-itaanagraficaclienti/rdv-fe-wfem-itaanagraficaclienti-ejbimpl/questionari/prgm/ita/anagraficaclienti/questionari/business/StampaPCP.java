package prgm.ita.anagraficaclienti.questionari.business;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

import prgm.ita.anagraficaclienti.model.ClienteKeyModel;
import prgm.ita.anagraficaclienti.questionari.facade.QuestionariFacade;
import prgm.ita.anagraficaclienti.questionari.model.QuestionarioModel;
import prgm.ita.anagraficaclienti.questionari.print.PersonaFisicaPCP;

/*******************************************************************/
/*******************************************************************/
public class StampaPCP extends BusinessCommand {

	/*******************************************************************/
	/*******************************************************************/
	@Override
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
 		ClienteKeyModel chiave = (ClienteKeyModel)dataModel;
		
		try {
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			QuestionariFacade facade = (QuestionariFacade)ROF.getFacade(csc,QuestionariFacade.class);
			QuestionarioModel questionario = facade.getQuestionario(csc,chiave);
			setNextCommandClass(PersonaFisicaPCP.class);
			return questionario;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nella stampa anagrafica codPotenziale=: "+e;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}
	}

	/*******************************************************************/
	/*******************************************************************/
	@Override
	public Class getInputViewClass() {
		return ClienteKeyModel.class;
	}

}
