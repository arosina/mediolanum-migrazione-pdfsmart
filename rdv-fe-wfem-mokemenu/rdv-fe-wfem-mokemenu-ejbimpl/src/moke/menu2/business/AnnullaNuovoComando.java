package moke.menu2.business;

import moke.menu2.model.ComandoModel;
import moke.menu2.model.ElencoComandiModel;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.StringType;

/*******************************************************************************/
/*******************************************************************************/
public class AnnullaNuovoComando extends BusinessCommand {

	/*******************************************************************************/
	/*******************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ElencoComandiModel model = (ElencoComandiModel)dataModel;
			setForwardDisplay(new Integer(0));
        	model.setComando(new ComandoModel());
        	model.getComando().setProgetto(new StringType(model.getProgetto().toString()));
			return model;
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/*******************************************************************************/
	/*******************************************************************************/
	public Class getInputViewClass() {
		return ElencoComandiModel.class;
	}

}
