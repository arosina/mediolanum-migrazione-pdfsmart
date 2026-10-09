package prgm.ita.p.dac.popup.display;

import prgm.ita.p.dac.facade.FacadeLoader;
import prgm.ita.p.dac.popup.facade.PopupFacade;
import prgm.ita.p.dac.popup.model.PopupContrattiModel;
import prgm.ita.p.dac.util.DacTools;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public class PopupContratti extends DisplayCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PopupContrattiModel model = (PopupContrattiModel)dataModel;
			if(model.isPrimaVolta()){
				model.setAgenteCollegato(DacTools.loadAgenteCollegato(csc,model.getUfficio()));
				if(model.getNumeroContratto().isNull())
					return model;
			}
			PopupFacade facade = (PopupFacade)FacadeLoader.getFacade(csc,PopupFacade.class);
			model = facade.cercaContratti(csc,model);
			model.setPrimaVolta(false);
			return model;
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PopupContrattiModel.class;
	}

}
