package prgm.ita.p.dac.display;

import prgm.ita.p.dac.facade.DacFacade;
import prgm.ita.p.dac.facade.FacadeLoader;
import prgm.ita.p.dac.model.FirmeModel;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public class Firme extends DisplayCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			FirmeModel model = (FirmeModel)dataModel;
			DacFacade facade = (DacFacade)FacadeLoader.getFacade(csc, DacFacade.class);
			model = facade.loadFirmeCliente(csc, model); 
			return model;
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return FirmeModel.class;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isNoSubmitCommand() {
		return true;
	}
	
}
