package prgm.ita.anagraficaclienti.business;

import prgm.ita.anagraficaclienti.facade.AnagraficaClientiFacade;
import prgm.ita.anagraficaclienti.model.ClienteModel;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

/*******************************************************************/
/*******************************************************************/
public class CancellaVariazioneCliente extends BusinessCommand {

	/*******************************************************************/
	/*******************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel)	throws CommandException {
		
		ClienteModel model = (ClienteModel)dataModel;
		try {
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();

			AnagraficaClientiFacade facade = (AnagraficaClientiFacade)ROF.getFacade(csc,AnagraficaClientiFacade.class);
			model = facade.cancellaVariazione(csc, model);
		
			model.getDatiApplicativi().setRefreshable(true);
			
			setForwardDisplay(new Integer(0),true);
			return model;			
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nel cancellare la variazione anagrafica prog=["+model.getProgressivo()+"] codPotenziale=["+model.getCodPotenziale()+"] codFiscale=["+model.getCodFiscale()+"] partitaIva=["+model.getPartitaIva()+"]: "+e;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}
	}

	/*******************************************************************/
	/*******************************************************************/
	public Class getInputViewClass() {
		return ClienteModel.class;
	}

}
