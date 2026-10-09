package prgm.ita.p.dac.popup.display;

import prgm.ita.p.dac.facade.FacadeLoader;
import prgm.ita.p.dac.popup.facade.PopupFacade;
import prgm.ita.p.dac.popup.model.PopupClientiModel;
import prgm.ita.p.dac.util.DacTools;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public class PopupClienti extends DisplayCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PopupClientiModel model = (PopupClientiModel)dataModel;
			if(model.isPrimaVolta()){
				model.setAgenteCollegato(DacTools.loadAgenteCollegato(csc,model.getUfficio()));
				return model;
			}
			PopupFacade facade = (PopupFacade)FacadeLoader.getFacade(csc,PopupFacade.class);
			if(model.getClienteSelezionato().getCodMediolanum().isNull()){
				model = facade.cercaClienti(csc,model);
			}else{
				model = facade.cercaContrattiCliente(csc,model);
			}
			return model;
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PopupClientiModel.class;
	}

}
