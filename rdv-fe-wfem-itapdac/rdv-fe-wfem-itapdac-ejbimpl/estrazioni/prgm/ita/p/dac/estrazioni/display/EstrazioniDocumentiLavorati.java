package prgm.ita.p.dac.estrazioni.display;

import java.rmi.RemoteException;

import javax.ejb.EJBException;

import prgm.ita.p.dac.estrazioni.facade.EstrazioniFacade;
import prgm.ita.p.dac.estrazioni.model.EstrazioniModel;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.util.FacadeLoader;

public class EstrazioniDocumentiLavorati extends DisplayCommand{

	public CommandDataModel execute(UserSessionContext userSessionContext,
			CommandDataModel dataModel) throws CommandException {
		ClientSessionContext csc = userSessionContext.getClientSessionContext();
		EstrazioniModel model=(EstrazioniModel)dataModel;

		try {
			EstrazioniFacade facade=(EstrazioniFacade)FacadeLoader.getFacade(csc,EstrazioniFacade.class);
			
			model=facade.fillCodDesc(csc, model);
		
		} catch (RemoteException e) {
			LOG.error(e);
            EJBException ejbEx = new EJBException(" Eccezione " + e.toString());
            throw ejbEx;
		} catch (Exception e) {
			LOG.error(e);
            EJBException ejbEx = new EJBException(" Eccezione " + e.toString());
            throw ejbEx;
		}
		
		return model;
	}

	public Class getInputViewClass() {
		return EstrazioniModel.class;
	}

	
}
