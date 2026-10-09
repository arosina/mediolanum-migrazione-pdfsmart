package prgm.ita.anagraficaclienti.questionari.business;

import prgm.ita.anagraficaclienti.questionari.display.Patrimonio;
import prgm.ita.anagraficaclienti.questionari.facade.QuestionariFacade;
import prgm.ita.anagraficaclienti.questionari.model.PatrimonioModel;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

/*******************************************************************/
/*******************************************************************/
public class SalvaPatrimonio extends BusinessCommand {

	/*******************************************************************/
	/*******************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try {
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PatrimonioModel patrimonio = (PatrimonioModel)dataModel;
			
			QuestionariFacade facade = (QuestionariFacade)ROF.getFacade(csc,QuestionariFacade.class);
			patrimonio = facade.salvaPatrimonio(csc,patrimonio);
			setNextCommandClass(Patrimonio.class);
			return patrimonio;
			
		}catch(Exception e){
			throw new CommandException(e.toString());
		}		
	}

	/*******************************************************************/
	/*******************************************************************/
	public Class getInputViewClass() {
		return PatrimonioModel.class;
	}

}
