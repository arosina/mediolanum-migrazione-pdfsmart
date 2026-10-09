package prgm.ita.p.dac.ricerche.sede;

import prgm.ita.p.dac.facade.DacFacade;
import prgm.ita.p.dac.facade.FacadeLoader;
import prgm.ita.p.dac.model.AbstractRicercaDocModel;
import prgm.ita.p.dac.model.DocumentoKeyModel;
import prgm.ita.p.dac.model.DocumentoModel;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public class LeggiDocumento extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			AbstractRicercaDocModel ricercaModel = (AbstractRicercaDocModel)dataModel;
			
			// Imposto nel documento i parametri da menù e/o da ricerca
			ricercaModel.getDocumento().copyParams(ricercaModel);
			
			DacFacade facade = (DacFacade)FacadeLoader.getFacade(csc, DacFacade.class); 
			DocumentoModel doc = facade.leggiDocumento(csc,(DocumentoKeyModel)ricercaModel.getDocumento());
			doc = (DocumentoModel)facade.fillCodDesc(csc,doc,true);
			doc.initTitolo(null);			
			
			doc.setVisible(true);
			
			ricercaModel.setDocumento(doc);
			setForwardDisplay(new Integer(0));
			return ricercaModel;
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return CommandDataModel.class;
	}

}
