package prgm.ita.p.dac.sede.creazionedac;

import prgm.ita.p.dac.facade.DacFacade;
import prgm.ita.p.dac.facade.FacadeLoader;
import prgm.ita.p.dac.model.DacModel;
import prgm.ita.p.dac.model.DocumentoModel;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;

public class CreazioneDacResiSede extends DisplayCommand {

	public CommandDataModel execute(UserSessionContext userSessionContext,
			CommandDataModel dataModel) throws CommandException {

		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			DacModel dac = (DacModel)dataModel;
			
			dac.addCodDescField("ufficio", "Uffici");
			dac.addCodDescField("uffDestinatario", "UfficiDestinatariDacResi");
			
			DacFacade facade = (DacFacade)FacadeLoader.getFacade(csc, DacFacade.class);
			dac = (DacModel)facade.fillCodDesc(csc,dac,false);
			dac.setDocumento((DocumentoModel)facade.fillCodDesc(csc,dac.getDocumento(),true));
			
			dac.initReadonlySede();
			dac.getDocumento().initTitolo(dac);
			
			return dac;
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
		
	}

	public Class getInputViewClass() {
		return DacModel.class;
	}

}
