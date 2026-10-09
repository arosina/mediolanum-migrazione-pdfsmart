package prgm.ita.anagraficaclienti.popup.display;

import prgm.ita.anagraficaclienti.popup.facade.PopupFacade;
import prgm.ita.anagraficaclienti.popup.model.PopupClientiModel;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.util.FacadeLoader;

/***********************************************************************************************/
/***********************************************************************************************/
public class PopupClientiAgentePrestitiPreapprovati extends DisplayCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext,
									CommandDataModel dataModel) throws CommandException {
		
		try{
		
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PopupClientiModel popupClientiModel = (PopupClientiModel)dataModel;
			
			PopupFacade popupFacade = (PopupFacade)FacadeLoader.getFacade(csc,PopupFacade.class);		
			popupClientiModel = popupFacade.getElencoClientiAgentePrestitiPreapprovati(csc,popupClientiModel);
			popupClientiModel.setModality(Template.INSERT_MODALITY);
			return popupClientiModel;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nell'elenco clienti di un agente: "+e;
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
