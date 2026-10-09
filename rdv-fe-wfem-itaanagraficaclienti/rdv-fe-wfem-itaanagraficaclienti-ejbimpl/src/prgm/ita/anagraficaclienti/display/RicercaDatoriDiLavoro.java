package prgm.ita.anagraficaclienti.display;

import prgm.ita.anagraficaclienti.popup.facade.PopupFacade;
import prgm.ita.anagraficaclienti.popup.model.PopupClientiModel;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.StringType;

/*
 * runtimeModality=null/simple  se è simple non sarà possibile aggiungere altri indirizzi/telefoni
 * 								in variazione
 */
/***********************************************************************************************/
/***********************************************************************************************/
public class RicercaDatoriDiLavoro extends DisplayCommand implements MenuCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext,
									CommandDataModel dataModel) throws CommandException {
		
		try{
		
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PopupClientiModel popupClientiModel = (PopupClientiModel)dataModel;
		
			PopupFacade popupFacade = (PopupFacade)ROF.getFacade(csc,PopupFacade.class);
			popupClientiModel.getParams().setTipoElementi(new StringType("datoridilavoro"));
			popupClientiModel = popupFacade.getElencoClientiAgente(csc,popupClientiModel);
			return popupClientiModel;
			
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
		return PopupClientiModel.class;
	}

}
