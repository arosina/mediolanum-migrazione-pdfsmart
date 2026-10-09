package prgm.ita.anagraficaclienti.stampamoduli;

import prgm.ita.anagraficaclienti.facade.VerificheAnagrafica;
import prgm.ita.anagraficaclienti.model.ClienteModel;
import prgm.ita.anagraficaclienti.popup.facade.PopupFacade;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.StringType;

/*
 * Funzione che permette di vedere gli agenti che un cliente ha e ha avuto
 */
/***********************************************************************************************/
/***********************************************************************************************/
public class StampaModuli extends DisplayCommand implements MenuCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext,
									CommandDataModel dataModel) throws CommandException {
		
		try{
		
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			StampaModuliModel stampaModuliModel = (StampaModuliModel)dataModel;
			
			PopupFacade popupFacade = (PopupFacade)ROF.getFacade(csc,PopupFacade.class);		

			if (stampaModuliModel.getNomeModulo().equals(Costanti.NOME_MODULO_MGM) || stampaModuliModel.getNomeModulo().equals(Costanti.NOME_MODULO_VOM)){
				stampaModuliModel.getPopupClientiModel().getParams().setTipoRicerca(new StringType("effettiviSenzaCodiceBloccoK"));
				stampaModuliModel.setPopupClientiModel(popupFacade.getElencoClienti(csc,stampaModuliModel.getPopupClientiModel()));
			} else {
				stampaModuliModel.setPopupClientiModel(popupFacade.getElencoClientiAgente(csc,stampaModuliModel.getPopupClientiModel()));
			}
			return stampaModuliModel;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nell'elenco clienti: "+e;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return StampaModuliModel.class;
	}
}
