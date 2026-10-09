package prgm.ita.p.dac.sede.creazionedac;

import prgm.ita.p.dac.facade.FacadeLoader;
import prgm.ita.p.dac.model.DocumentoModel;
import prgm.ita.p.dac.sede.facade.DacSedeFacade;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;

public class RimuoviDocDaDac extends DisplayCommand {

	public boolean isNoSubmitCommand() {
		return true;
	}

	public CommandDataModel execute(UserSessionContext userSessionContext,
			CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			DocumentoModel docDaRimuovere = (DocumentoModel)dataModel;
			
			DacSedeFacade facade = (DacSedeFacade)FacadeLoader.getFacade(csc, DacSedeFacade.class); 
			docDaRimuovere = facade.rimuoviDocDaDac(csc, docDaRimuovere);
			
			return docDaRimuovere;
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	public Class getInputViewClass() {
		return DocumentoModel.class;
	}

}
