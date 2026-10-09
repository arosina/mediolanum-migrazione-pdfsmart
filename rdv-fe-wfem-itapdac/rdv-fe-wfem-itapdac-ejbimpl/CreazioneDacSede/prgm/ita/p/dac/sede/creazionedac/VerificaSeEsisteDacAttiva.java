package prgm.ita.p.dac.sede.creazionedac;

import prgm.ita.p.dac.business.SalvaDac;
import prgm.ita.p.dac.facade.FacadeLoader;
import prgm.ita.p.dac.model.DacModel;
import prgm.ita.p.dac.sede.facade.DacSedeFacade;
import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.util.Tools;

public class VerificaSeEsisteDacAttiva extends BusinessCommand {

	public CommandDataModel execute(UserSessionContext userSessionContext,
			CommandDataModel dataModel) throws CommandException {

		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			DacModel dac = (DacModel)dataModel;
			
			DacSedeFacade facade = (DacSedeFacade)FacadeLoader.getFacade(csc, DacSedeFacade.class); 
			dac = facade.verificaSeEsisteDacAttiva(csc, dac);
			
			if (!dac.getIdDac().isNull() && !Tools.containsTypeErrors(dac))
				setNextCommandClass(SalvaDac.class);
			else
				setForwardDisplay(new Integer(0));
			
			return dac;
		}catch(Exception e){
			throw new CommandException(e.toString());
		}

	}

	public Class getInputViewClass() {
		return DacModel.class;
	}

}
