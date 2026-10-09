package prgm.ita.p.dac.business;

import prgm.ita.p.dac.facade.DacFacade;
import prgm.ita.p.dac.facade.FacadeLoader;
import prgm.ita.p.dac.model.DacModel;
import prgm.ita.p.dac.ricerche.fb.RicercaDacModel;
import prgm.ita.p.dac.ricerche.sede.RicercaDac;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public class SpuntaDac extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			DacModel dac = (DacModel)dataModel;
			DacFacade facade = (DacFacade)FacadeLoader.getFacade(csc, DacFacade.class);
			dac = facade.spuntaDac(csc,dac);
			if(!dac.hasCommandErrors()){
				setNextCommandClass(RicercaDac.class);
				RicercaDacModel result = new RicercaDacModel();
				result.copyParams(dac);
				return result;
			}
			setForwardDisplay(new Integer(0));
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
