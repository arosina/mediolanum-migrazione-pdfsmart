package prgm.ita.anagraficaclienti.agenticlienti;

import prgm.ita.anagraficaclienti.facade.AnagraficaClientiFacade;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public class ElencoAgentiCliente extends DisplayCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext,
									CommandDataModel dataModel) throws CommandException {
		try{
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			RicercaGlobaleClientiModel ricercaGlobaleClientiModel = (RicercaGlobaleClientiModel)dataModel;
			
			RicercaGlobaleClientiModel stackedRicercaGlobaleClientiModel = ricercaGlobaleClientiModel;
			AnagraficaClientiFacade facade = (AnagraficaClientiFacade)ROF.getFacade(csc,AnagraficaClientiFacade.class);		
			ricercaGlobaleClientiModel = facade.elencoAgentiCliente(csc,ricercaGlobaleClientiModel);
			stackedRicercaGlobaleClientiModel.setElencoAgentiCliente(ricercaGlobaleClientiModel.getElencoAgentiCliente());
			return ricercaGlobaleClientiModel;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nell'elenco agenti del cliente: "+e;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isNoSubmitCommand() {
		return true;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return RicercaGlobaleClientiModel.class;
	}

}
