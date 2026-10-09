package prgm.ita.anagraficaclienti.popup.display;

import prgm.ita.anagraficaclienti.popup.facade.PopupFacade;
import prgm.ita.anagraficaclienti.popup.model.PopupAgentiModel;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.FacadeLoader;

/***********************************************************************************************/
/***********************************************************************************************/
public class PopupGlobalSpecialist extends DisplayCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext,
									CommandDataModel dataModel) throws CommandException {
		
		try{
		
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PopupAgentiModel popupAgentiModel = (PopupAgentiModel)dataModel;
			popupAgentiModel.getParams().setTipoRicerca(new StringType("GlobalSpecialist"));
			
			PopupFacade popupFacade = (PopupFacade)FacadeLoader.getFacade(csc,PopupFacade.class);		
			popupAgentiModel = popupFacade.getElencoAgenti(csc,popupAgentiModel);
			popupAgentiModel.setModality(Template.INSERT_MODALITY);
			return popupAgentiModel;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nell'elenco agenti: "+e;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PopupAgentiModel.class;
	}

}
