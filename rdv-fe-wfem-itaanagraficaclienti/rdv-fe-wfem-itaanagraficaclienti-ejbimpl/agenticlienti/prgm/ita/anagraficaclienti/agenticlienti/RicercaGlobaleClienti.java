package prgm.ita.anagraficaclienti.agenticlienti;

import prgm.ita.anagraficaclienti.facade.AnagraficaClientiFacade;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;

/*
 * Funzione che permette di vedere gli agenti che un cliente ha e ha avuto
 */
/***********************************************************************************************/
/***********************************************************************************************/
public class RicercaGlobaleClienti extends DisplayCommand implements MenuCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		
		try{
		
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			RicercaGlobaleClientiModel ricercaGlobaleClientiModel = (RicercaGlobaleClientiModel)dataModel;
		
			if(ricercaGlobaleClientiModel.isPrimaAttivazione()){
				return ricercaGlobaleClientiModel;
			}
							
			AnagraficaClientiFacade facade = (AnagraficaClientiFacade)ROF.getFacade(csc,AnagraficaClientiFacade.class);
			ricercaGlobaleClientiModel = facade.ricercaGlobaleClienti(csc,ricercaGlobaleClientiModel);
			return ricercaGlobaleClientiModel;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nell'elenco globale dei clienti: "+e;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return RicercaGlobaleClientiModel.class;
	}

}
