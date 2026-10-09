package prgm.ita.anagraficaclienti.popup.display;

import prgm.ita.anagraficaclienti.popup.facade.PopupFacade;
import prgm.ita.anagraficaclienti.popup.model.PopupUniversitaModel;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.util.FacadeLoader;

/***********************************************************************************************/
/***********************************************************************************************/
public class PopupUniversita extends DisplayCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel)	throws CommandException {
		try{
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PopupUniversitaModel popupUniversitaModel = (PopupUniversitaModel)dataModel;
			
			PopupFacade popupFacade = (PopupFacade)FacadeLoader.getFacade(csc,PopupFacade.class);
			popupUniversitaModel = popupFacade.getElencoUniversita(csc,popupUniversitaModel);
			return popupUniversitaModel;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nell'elenco Universita: "+e;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PopupUniversitaModel.class;
	}

}
