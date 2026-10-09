package prgm.ita.p.dac.sede.business;

import prgm.ita.p.dac.facade.FacadeLoader;
import prgm.ita.p.dac.sede.display.SbloccaDac;
import prgm.ita.p.dac.sede.facade.DacSedeFacade;
import prgm.ita.p.dac.sede.model.SbloccaDacModel;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

public class LoadDacAttive extends BusinessCommand {

	public CommandDataModel execute(UserSessionContext userSessionContext,
			CommandDataModel dataModel) throws CommandException {

		SbloccaDacModel model = (SbloccaDacModel) dataModel;
		
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();

			DacSedeFacade facade = (DacSedeFacade)FacadeLoader.getFacade(csc, DacSedeFacade.class);
			model = facade.loadDacAttive(csc, model);

			setNextCommandClass(SbloccaDac.class);
			return model;
			
		}catch(Exception e){
			throw new CommandException(e.toString());
		}	

	}

	public Class getInputViewClass() {
		return SbloccaDacModel.class;
	}

}
