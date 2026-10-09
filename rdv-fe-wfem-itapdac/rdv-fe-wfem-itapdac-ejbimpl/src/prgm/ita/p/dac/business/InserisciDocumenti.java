package prgm.ita.p.dac.business;

import prgm.ita.p.dac.model.DacModel;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public class InserisciDocumenti extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			DacModel dac = (DacModel)dataModel;
			dac.getDocumento().resetCommandMessages();
			dac.setInClonazione(false);
			setNextCommandClass(NuovoDocumento.class);
			return dac;
			
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return DacModel.class;
	}

}
