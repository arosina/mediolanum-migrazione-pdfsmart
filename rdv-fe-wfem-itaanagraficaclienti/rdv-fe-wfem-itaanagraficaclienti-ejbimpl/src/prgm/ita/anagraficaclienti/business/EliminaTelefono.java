package prgm.ita.anagraficaclienti.business;

import prgm.ita.anagraficaclienti.model.ClienteModel;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public class EliminaTelefono extends AbstractElencoAttributiBusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try {
			
			ClienteModel model = (ClienteModel)dataModel;
			
			eliminaAttributo(model.getTelefoni());

			setForwardDisplay(new Integer(0),false);
			return model;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nell' eliminare un telefono: "+e;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return ClienteModel.class;
	}

}
