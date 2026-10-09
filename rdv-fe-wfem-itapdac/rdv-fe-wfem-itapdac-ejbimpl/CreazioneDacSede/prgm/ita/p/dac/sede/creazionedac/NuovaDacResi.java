package prgm.ita.p.dac.sede.creazionedac;

import prgm.ita.p.dac.facade.FacadeLoader;
import prgm.ita.p.dac.model.DacModel;
import prgm.ita.p.dac.model.ParamsModel;
import prgm.ita.p.dac.sede.facade.DacSedeFacade;
import prgm.ita.p.dac.util.DacTools;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public class NuovaDacResi extends BusinessCommand implements MenuCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			
			ParamsModel params = (ParamsModel)dataModel;
			params.addParam(ParamsModel.inSpedizione);
			
			DacSedeFacade facade = (DacSedeFacade)FacadeLoader.getFacade(csc, DacSedeFacade.class); 
			DacModel dac = facade.nuovaDacResi(csc, params);
			
			// Imposto la versione della DAC
			DacTools.impostaVersioneDac(csc,dac);
			
			setNextCommandClass(CreazioneDacResiSede.class);
			return dac;
			
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return ParamsModel.class;
	}

}
