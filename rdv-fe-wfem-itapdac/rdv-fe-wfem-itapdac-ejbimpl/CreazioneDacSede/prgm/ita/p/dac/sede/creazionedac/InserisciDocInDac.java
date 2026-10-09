package prgm.ita.p.dac.sede.creazionedac;

import prgm.ita.p.dac.facade.DacFacade;
import prgm.ita.p.dac.facade.FacadeLoader;
import prgm.ita.p.dac.model.DocumentoModel;
import prgm.ita.p.dac.sede.facade.DacSedeFacade;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public class InserisciDocInDac extends DisplayCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			DocumentoModel docDaInserire = (DocumentoModel)dataModel;
			
			DacSedeFacade facadeSede = (DacSedeFacade)FacadeLoader.getFacade(csc, DacSedeFacade.class); 
			DocumentoModel doc = facadeSede.inserisciDocInDac(csc, docDaInserire);
			
			DacFacade facade = (DacFacade)FacadeLoader.getFacade(csc, DacFacade.class); 
			doc = (DocumentoModel)facade.fillCodDesc(csc,doc,true);
			
			return doc;
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return DocumentoModel.class;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isNoSubmitCommand() {
		return true;
	}

}
