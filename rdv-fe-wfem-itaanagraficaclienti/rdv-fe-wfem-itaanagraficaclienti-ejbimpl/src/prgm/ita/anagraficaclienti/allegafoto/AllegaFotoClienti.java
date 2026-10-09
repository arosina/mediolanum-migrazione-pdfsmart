package prgm.ita.anagraficaclienti.allegafoto;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.StringType;

import prgm.ita.anagraficaclienti.model.ClienteModel;
import prgm.ita.anagraficaclienti.popup.facade.PopupFacade;
import prgm.ita.anagraficaclienti.popup.model.PopupClientiModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class AllegaFotoClienti extends DisplayCommand implements MenuCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext,
									CommandDataModel dataModel) throws CommandException {
		
		try{
		
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PopupClientiModel popupClientiModel = (PopupClientiModel)dataModel;
			
			popupClientiModel.getParams().setTipoRicerca(new StringType("secondaricensiti"));
			popupClientiModel.getParams().setTipoElementi(new StringType("concodicefiscale"));
			popupClientiModel.getParams().setTipoInclusioneCogestiti(new StringType("T"));
			popupClientiModel.getParams().setRuoloCogestione(new StringType("BC"));
			popupClientiModel.setClienteSelezionato(new ClienteModel());
		
			PopupFacade popupFacade = (PopupFacade)ROF.getFacade(csc,PopupFacade.class);		
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
