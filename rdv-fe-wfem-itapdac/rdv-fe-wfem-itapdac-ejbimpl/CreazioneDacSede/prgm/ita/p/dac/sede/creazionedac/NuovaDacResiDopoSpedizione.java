package prgm.ita.p.dac.sede.creazionedac;

import prgm.ita.p.dac.model.DacModel;
import prgm.ita.p.dac.model.ParamsModel;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.IntegerType;

/***********************************************************************************************/
/***********************************************************************************************/
public class NuovaDacResiDopoSpedizione extends BusinessCommand implements MenuCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			DacModel dac = (DacModel)dataModel;
			IntegerType box = dac.getBox();
			IntegerType uff = dac.getUffDestinatario();
			dac = (DacModel)new NuovaDacResi().execute(userSessionContext,(ParamsModel)dac);
			dac.setBox(box);
			dac.setUffDestinatario(uff);
			dac = (DacModel)new VerificaSeEsisteDacAttiva().execute(userSessionContext,dac);
			setNextCommandClass(CreazioneDacResiSede.class);
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
