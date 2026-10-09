package prgm.ita.anagraficaclienti.popup.display;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.util.FacadeLoader;

import prgm.ita.anagraficaclienti.popup.facade.PopupFacade;
import prgm.ita.anagraficaclienti.popup.model.PopupClientiModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class PopupClienti extends DisplayCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public CommandDataModel execute(UserSessionContext userSessionContext,
									CommandDataModel dataModel) throws CommandException {
		
		try{
		
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PopupClientiModel popupClientiModel = (PopupClientiModel)dataModel;
			
			PopupFacade popupFacade = (PopupFacade)FacadeLoader.getFacade(csc,PopupFacade.class);
			if (popupClientiModel.getParams().getTipoRicerca().equals("selezionaPotenziali")) {
				popupClientiModel = popupFacade.getElencoClientiPotenziali(csc,popupClientiModel);
			} else if(popupClientiModel.getParams().getTipoRicerca().equals("secondari")) {
				popupClientiModel = popupFacade.getElencoClientiSecondari(csc, popupClientiModel);
			} else {
				popupClientiModel = popupFacade.getElencoClienti(csc,popupClientiModel);
			}
			popupClientiModel.setModality(Template.INSERT_MODALITY);
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
	@Override
	public Class getInputViewClass() {
		return PopupClientiModel.class;
	}

}
