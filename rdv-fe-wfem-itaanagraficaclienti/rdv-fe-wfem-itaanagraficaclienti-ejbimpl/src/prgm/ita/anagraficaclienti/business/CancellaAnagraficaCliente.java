package prgm.ita.anagraficaclienti.business;

import prgm.ita.anagraficaclienti.facade.AnagraficaClientiFacade;
import prgm.ita.anagraficaclienti.model.ClienteKeyModel;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

/*******************************************************************/
/*******************************************************************/
public class CancellaAnagraficaCliente extends BusinessCommand {

	/*******************************************************************/
	/*******************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel)	throws CommandException {
		
		ClienteKeyModel chiave = (ClienteKeyModel)dataModel;
		try {
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();

			AnagraficaClientiFacade facade = (AnagraficaClientiFacade)ROF.getFacade(csc,AnagraficaClientiFacade.class);
			facade.cancellaCliente(csc, chiave);
			
			setForwardDisplay(new Integer(0),true);
			return null;			
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nel cancellare l'anagrafica codPotenziale=["+chiave.getCodPotenziale()+"] codFiscale=["+chiave.getCodFiscale()+"] partitaIva=["+chiave.getPartitaIva()+"]: "+e;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}
	}

	/*******************************************************************/
	/*******************************************************************/
	public Class getInputViewClass() {
		return ClienteKeyModel.class;
	}

}
