package prgm.ita.p.dac.sede.ricezionedac;

import prgm.ita.p.dac.facade.FacadeLoader;
import prgm.ita.p.dac.model.DacModel;
import prgm.ita.p.dac.ricerche.sede.DacDaGestire;
import prgm.ita.p.dac.ricerche.sede.DacDaGestireModel;
import prgm.ita.p.dac.sede.facade.DacSedeFacade;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public class ChiudiDac extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			DacModel dac = (DacModel)dataModel;
			
			DacSedeFacade facade = (DacSedeFacade)FacadeLoader.getFacade(csc, DacSedeFacade.class); 
			dac = facade.chiudiDac(csc, dac);

			if(!dac.hasCommandErrors()){
				setNextCommandClass(DacDaGestire.class);
				DacDaGestireModel result = new DacDaGestireModel();
				result.copyParams(dac);
				return result;
			}
			setNextCommandClass(RicezioneDacSede.class);
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
