package prgm.ita.p.dac.estrazioni.facade;

import java.rmi.RemoteException;

import com.atosorigin.wfem.backend.Facade;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;

import prgm.ita.p.dac.estrazioni.model.DocumentiRicevutiModel;

public interface EstrazioniDocumentiRicevutiFacade extends Facade{
	public DocumentiRicevutiModel fillCodDesc (ClientSessionContext clientSessionContext, CommandDataModel model) throws RemoteException;
}
