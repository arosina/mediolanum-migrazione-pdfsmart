package prgm.ita.anagraficaclienti.display;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.StringType;

import prgm.ita.anagraficaclienti.cogestione.CogestioneDataManager;
import prgm.ita.anagraficaclienti.cogestione.CogestioneDataModel;
import prgm.ita.anagraficaclienti.popup.facade.PopupFacade;
import prgm.ita.anagraficaclienti.popup.model.PopupClientiModel;
import prgm.ita.anagraficaclienti.popup.model.PopupClientiParamsModel;

/*
 * runtimeModality=null/simple  se è simple non sarà possibile aggiungere altri indirizzi/telefoni
 * 								in variazione
 */
/***********************************************************************************************/
/***********************************************************************************************/
public class RicercaClienti extends DisplayCommand implements MenuCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext,
									CommandDataModel dataModel) throws CommandException {
		
		try{
		
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PopupClientiModel popupClientiModel = (PopupClientiModel)dataModel;
			PopupClientiParamsModel params = popupClientiModel.getParams();

			if(popupClientiModel.getIsPrimaVolta().booleanValue()) {
				CogestioneDataModel cogestioneData = popupClientiModel.getClienteSelezionato().getCogestioneData();
				CogestioneDataManager.initContrattoCogestione(csc, cogestioneData);
				if(cogestioneData.getIsUtenteCogestore().booleanValue()) {
					params.setTipoInclusioneCogestiti(new StringType("T"));
					params.setRuoloCogestione(new StringType("BC"));
					params.setTipoOrdinamentoCogestiti(new StringType("C"));
				}else if(cogestioneData.getIsUtenteTitolare().booleanValue()) {
					params.setTipoInclusioneCogestiti(new StringType("T"));
					params.setRuoloCogestione(new StringType("BC"));
					params.setTipoOrdinamentoCogestiti(new StringType("P"));
				}
			}
			
			PopupFacade popupFacade = (PopupFacade)ROF.getFacade(csc,PopupFacade.class);		
			popupClientiModel = popupFacade.getElencoClientiAgente(csc,popupClientiModel);
			return popupClientiModel;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO nell'elenco clienti: "+daoe;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
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
