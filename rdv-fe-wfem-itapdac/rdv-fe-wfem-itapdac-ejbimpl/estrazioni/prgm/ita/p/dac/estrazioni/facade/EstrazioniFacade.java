package prgm.ita.p.dac.estrazioni.facade;

import java.rmi.RemoteException;

import prgm.ita.p.dac.estrazioni.model.EstrazioniModel;
import prgm.ita.p.dac.estrazioni.model.PritInviatiInSedeModel;

import com.atosorigin.wfem.backend.Facade;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;

public interface EstrazioniFacade extends Facade {
	public EstrazioniModel fillCodDesc (ClientSessionContext clientSessionContext, CommandDataModel model) throws RemoteException;
	public PritInviatiInSedeModel popolaCombo (ClientSessionContext clientSessionContext, PritInviatiInSedeModel model) throws RemoteException;
	public PritInviatiInSedeModel eseguiReportSintesi (ClientSessionContext clientSessionContext, PritInviatiInSedeModel model) throws RemoteException;
	public PritInviatiInSedeModel eseguiReportDettaglio (ClientSessionContext clientSessionContext, PritInviatiInSedeModel model) throws RemoteException;
}
