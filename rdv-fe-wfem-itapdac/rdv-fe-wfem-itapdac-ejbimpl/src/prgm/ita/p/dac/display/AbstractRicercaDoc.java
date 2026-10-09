package prgm.ita.p.dac.display;

import prgm.ita.p.dac.facade.DacFacade;
import prgm.ita.p.dac.facade.FacadeLoader;
import prgm.ita.p.dac.model.AbstractRicercaDocModel;
import prgm.ita.p.dac.model.DocumentoModel;
import prgm.ita.p.dac.util.DacTools;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.BooleanType;

/***********************************************************************************************/
/***********************************************************************************************/
public abstract class AbstractRicercaDoc extends DisplayCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			AbstractRicercaDocModel ricercaModel = (AbstractRicercaDocModel)dataModel;
			
			DacFacade facade = (DacFacade)FacadeLoader.getFacade(csc,DacFacade.class);
			if(ricercaModel.isPrimaAttivazione()){
				DacTools.loadUfficioUtente(csc, ricercaModel);
				ricercaModel = (AbstractRicercaDocModel)facade.fillCodDesc(csc,ricercaModel,true);
				return ricercaModel;
			}

			if(ricercaModel.getDoSearch().booleanValue()){
				ricercaModel = facade.ricercaDoc(csc,ricercaModel);
				ricercaModel.setDocumento(new DocumentoModel());
				ricercaModel.setDoSearch(new BooleanType(false));
			}
			return ricercaModel;
			
		}catch(Exception e){
			String errorMsg = "prgm.ita.p.dac.display.AbstractRicercaDoc: Eccezione nella ricerca Documenti: "+e;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}
	}
	
}
