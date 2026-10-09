package prgm.ita.anagraficaclienti.business;

import prgm.ita.anagraficaclienti.facade.AnagraficaClientiFacade;
import prgm.ita.anagraficaclienti.model.ClienteModel;
import prgm.ita.anagraficaclienti.model.IndirizzoModel;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public class NuovoIndirizzo extends AbstractElencoAttributiBusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try {
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			ClienteModel model = (ClienteModel)dataModel;
			
			AnagraficaClientiFacade facade = (AnagraficaClientiFacade)ROF.getFacade(csc,AnagraficaClientiFacade.class);
			IndirizzoModel indirizzo = facade.nuovoIndirizzo(csc);
			nuovoAttributo(model.getIndirizzi(),indirizzo);
			
			setForwardDisplay(new Integer(0),false);
			return model;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nell'inizializzare un nuovo indirizzo: "+e;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return ClienteModel.class;
	}

}
