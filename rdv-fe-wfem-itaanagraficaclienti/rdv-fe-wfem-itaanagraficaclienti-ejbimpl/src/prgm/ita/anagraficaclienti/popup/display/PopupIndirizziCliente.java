package prgm.ita.anagraficaclienti.popup.display;

import prgm.ita.anagraficaclienti.popup.facade.PopupFacade;
import prgm.ita.anagraficaclienti.popup.model.PopupIndirizziClienteModel;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.util.FacadeLoader;

/***********************************************************************************************/
/***********************************************************************************************/
public class PopupIndirizziCliente extends DisplayCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext,
									CommandDataModel dataModel) throws CommandException {
		
		try{
		
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PopupIndirizziClienteModel popupIndirizziClienteModel = (PopupIndirizziClienteModel)dataModel;
			
			PopupFacade popupFacade = (PopupFacade)FacadeLoader.getFacade(csc,PopupFacade.class);		
			popupIndirizziClienteModel = popupFacade.getElencoIndirizziCliente(csc,popupIndirizziClienteModel);
			return popupIndirizziClienteModel;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nell'elenco indirizzi cliente: "+e;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PopupIndirizziClienteModel.class;
	}

}
