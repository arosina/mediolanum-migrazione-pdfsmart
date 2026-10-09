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
public class CalcolaCodiceFiscale extends BusinessCommand {

	/*******************************************************************/
	/*******************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel)	throws CommandException {
		
		try {
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			ClienteModel model = (ClienteModel)dataModel;
			
			AnagraficaClientiFacade facade = (AnagraficaClientiFacade)ROF.getFacade(csc,AnagraficaClientiFacade.class);
			model = facade.calcolaCodiceFiscale(csc,model);
			setForwardDisplay(new Integer(0));
			return model;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nel calcolare il codice fiscale cliente: "+e;
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
