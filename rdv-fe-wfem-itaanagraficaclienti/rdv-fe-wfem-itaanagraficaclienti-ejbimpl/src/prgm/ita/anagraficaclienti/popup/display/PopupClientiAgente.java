package prgm.ita.anagraficaclienti.popup.display;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.util.FacadeLoader;

import prgm.ita.anagraficaclienti.cogestione.CogestioneDataManager;
import prgm.ita.anagraficaclienti.cogestione.CogestioneDataModel;
import prgm.ita.anagraficaclienti.popup.facade.PopupFacade;
import prgm.ita.anagraficaclienti.popup.model.PopupClientiModel;
import prgm.ita.anagraficaclienti.popup.model.PopupClientiParamsModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class PopupClientiAgente extends DisplayCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext,
									CommandDataModel dataModel) throws CommandException {
		
		try{
		
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PopupClientiModel popupClientiModel = (PopupClientiModel)dataModel;
			PopupClientiParamsModel params = popupClientiModel.getParams();

			// rfc 234474: controllo per evitare di cercare clienti di altri FB
			if(!csc.isSede() && !params.getCodAgente().isNull() && !params.getCodAgente().equals(csc.getCurrentLinkedUserCode()) &&
				params.getRuoloCogestione().isNull() && params.getTipoInclusioneCogestiti().isNull())
				throw new CommandException("Accesso con FB non autorizzato");
			
			if(popupClientiModel.getIsPrimaVolta().booleanValue() && !params.getTipoInclusioneCogestiti().isNull()) {
				CogestioneDataModel cogestioneData = popupClientiModel.getClienteSelezionato().getCogestioneData();
				CogestioneDataManager.initContrattoCogestione(csc, cogestioneData);
			}
			
			PopupFacade popupFacade = (PopupFacade)FacadeLoader.getFacade(csc,PopupFacade.class);		
			popupClientiModel = popupFacade.getElencoClientiAgente(csc,popupClientiModel);
			popupClientiModel.setModality(Template.INSERT_MODALITY);
			return popupClientiModel;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO nell'elenco clienti di un agente: "+daoe;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
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
