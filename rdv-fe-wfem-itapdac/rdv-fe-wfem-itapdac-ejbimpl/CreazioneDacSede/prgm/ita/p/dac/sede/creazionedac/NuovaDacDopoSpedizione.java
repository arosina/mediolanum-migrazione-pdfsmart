package prgm.ita.p.dac.sede.creazionedac;

import prgm.ita.p.dac.model.DacModel;
import prgm.ita.p.dac.model.ParamsModel;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public class NuovaDacDopoSpedizione extends BusinessCommand implements MenuCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			DacModel dac = (DacModel)dataModel;
			dac = (DacModel)new NuovaDac().execute(userSessionContext,(ParamsModel)dac);
			setNextCommandClass(CreazioneDacSede.class);
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
