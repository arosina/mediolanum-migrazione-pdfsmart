package prgm.ita.p.dac.popup.display;

import prgm.ita.p.dac.facade.FacadeLoader;
import prgm.ita.p.dac.popup.facade.PopupFacade;
import prgm.ita.p.dac.popup.model.PopupAgentiModel;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public class PopupAgenti extends DisplayCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PopupAgentiModel model = (PopupAgentiModel)dataModel;
			if(model.isPrimaVolta())
				return model;
			PopupFacade facade = (PopupFacade)FacadeLoader.getFacade(csc,PopupFacade.class);
			model = facade.cercaAgenti(csc,model);
			model.setPrimaVolta(false);
			return model;
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PopupAgentiModel.class;
	}

}
