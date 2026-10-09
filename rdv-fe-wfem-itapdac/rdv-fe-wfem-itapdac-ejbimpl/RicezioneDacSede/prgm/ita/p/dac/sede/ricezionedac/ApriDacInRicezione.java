package prgm.ita.p.dac.sede.ricezionedac;

import prgm.ita.p.dac.facade.Costanti;
import prgm.ita.p.dac.facade.DacFacade;
import prgm.ita.p.dac.facade.FacadeLoader;
import prgm.ita.p.dac.model.DacKeyModel;
import prgm.ita.p.dac.model.DacModel;
import prgm.ita.p.dac.model.ParamsModel;
import prgm.ita.p.dac.sede.facade.DacSedeFacade;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public class ApriDacInRicezione extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			
			DacKeyModel dacKey = (DacKeyModel)dataModel;
			dacKey.addParam(ParamsModel.inRicezione);

			DacModel dac = null;
			if (dacKey.getStato().equals(Costanti.STATO_SPEDITA)) {
				DacSedeFacade facade = (DacSedeFacade)FacadeLoader.getFacade(csc, DacSedeFacade.class);
				dac = facade.gestisciDac(csc,dacKey);
			} else {
				DacFacade facade = (DacFacade)FacadeLoader.getFacade(csc, DacFacade.class);
				dac = facade.leggiDac(csc,dacKey);
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
		return DacKeyModel.class;
	}

}
