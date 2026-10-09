package prgm.ita.p.dac.sede.creazionedac;

import prgm.ita.p.dac.facade.DacFacade;
import prgm.ita.p.dac.facade.FacadeLoader;
import prgm.ita.p.dac.model.DacKeyModel;
import prgm.ita.p.dac.model.DacModel;
import prgm.ita.p.dac.model.ParamsModel;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public class ApriDacInCreazione extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			
			DacKeyModel dacKey = (DacKeyModel)dataModel;
			dacKey.addParam(ParamsModel.inSpedizione);
			
			DacModel dac = null;
			DacFacade facade = (DacFacade)FacadeLoader.getFacade(csc, DacFacade.class);
			dac = facade.leggiDac(csc,dacKey);
			if (dac.getReso().booleanValue())
				setNextCommandClass(CreazioneDacResiSede.class);
			else
				setNextCommandClass(CreazioneDacSede.class);
			return dac;
			
		}catch(Exception e){
			throw new CommandException(e.toString());
		}	

	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return DacKeyModel.class;
	}

}
