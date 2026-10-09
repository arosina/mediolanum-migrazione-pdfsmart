package prgm.ita.anagraficaclienti.questionari.business;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.types.BooleanType;

import prgm.ita.anagraficaclienti.display.ErroriOnStartUp;
import prgm.ita.anagraficaclienti.facade.StatiPropostaAnagrafica;
import prgm.ita.anagraficaclienti.model.ClienteKeyModel;
import prgm.ita.anagraficaclienti.questionari.display.Questionario;
import prgm.ita.anagraficaclienti.questionari.facade.QuestionariFacade;
import prgm.ita.anagraficaclienti.questionari.model.QuestionarioModel;

/*******************************************************************/
/*******************************************************************/
public class NuovoQuestionario extends BusinessCommand implements MenuCommand{
	
	/*******************************************************************/
	/*******************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try {
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			ClienteKeyModel clienteKey = (ClienteKeyModel)dataModel;
			BooleanType showBack = clienteKey.getShowBack();
			
			QuestionariFacade facade = (QuestionariFacade)ROF.getFacade(csc,QuestionariFacade.class);
			QuestionarioModel questionario = facade.getQuestionario(csc,clienteKey);
			if(questionario.getCliente().getStato().equals(StatiPropostaAnagrafica.NON_TROVATA)) {
				setNextCommandClass(ErroriOnStartUp.class);
				return questionario.getCliente();
			}
			
			questionario.setModality(Template.READ_MODALITY);
			questionario.setShowBack(showBack);
			setNextCommandClass(Questionario.class);
			return questionario;
			
		}catch(Exception e){
			throw new CommandException(e.toString());
		}		
	}

	/*******************************************************************/
	/*******************************************************************/
	public Class getInputViewClass() {
		return ClienteKeyModel.class;
	}

}
