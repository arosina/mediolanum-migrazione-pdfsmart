package prgm.ita.anagraficaclienti.business;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

import prgm.ita.anagraficaclienti.model.ClienteModel;
import prgm.ita.anagraficaclienti.print.Ditta;
import prgm.ita.anagraficaclienti.print.PersonaFisica;

/***********************************************************************************************/
/***********************************************************************************************/
public class StampaBozzaExecute extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
			
		ClienteModel model = (ClienteModel)dataModel;
		
		if(model.getIsDitta().booleanValue())
			setNextCommandClass(Ditta.class);
		else
			setNextCommandClass(PersonaFisica.class);
		return model;
			
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return ClienteModel.class;
	}

}
