package prgm.ita.p.dac.facade;

import java.rmi.RemoteException;

import prgm.ita.p.dac.model.AbstractRicercaDacModel;
import prgm.ita.p.dac.model.AbstractRicercaDocModel;
import prgm.ita.p.dac.model.DacKeyModel;
import prgm.ita.p.dac.model.DacModel;
import prgm.ita.p.dac.model.DocumentoKeyModel;
import prgm.ita.p.dac.model.DocumentoModel;
import prgm.ita.p.dac.model.FirmeModel;
import prgm.ita.p.dac.model.ParamsModel;

import com.atosorigin.wfem.backend.Facade;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public interface DacFacade extends Facade {
	public CommandDataModel fillCodDesc(ClientSessionContext csc, CommandDataModel model, boolean includeInnerModels) throws RemoteException;

	public DacModel 		caricaDacAttiva(ClientSessionContext csc, ParamsModel params) throws RemoteException;
	public DacModel 		inviaDac(ClientSessionContext csc, DacModel dac) throws RemoteException;
	public StringType 		leggiIdUltimaDac(ClientSessionContext csc, ParamsModel params) throws RemoteException;

	public DacModel 		leggiDacLight(ClientSessionContext csc, DacKeyModel dacKey) throws RemoteException;
	public DacModel 		leggiDac(ClientSessionContext csc, DacKeyModel dacKey) throws RemoteException;
	public DacModel 		leggiDacCompleta(ClientSessionContext csc, DacKeyModel dacKey) throws RemoteException;
	public DacModel 		salvaDac(ClientSessionContext csc, DacModel dac) throws RemoteException;
	public DacModel 		lavoraDac(ClientSessionContext csc, DacModel dac) throws RemoteException;
	public DacModel 		spuntaDac(ClientSessionContext csc, DacModel dac) throws RemoteException;
	public DacModel 		autorizzaErroreDocumento(ClientSessionContext csc, DacModel dac) throws RemoteException;
	
	public DacModel			nuovoDocumento(ClientSessionContext csc, DacModel dac) throws RemoteException;
	public DocumentoModel	leggiDocumento(ClientSessionContext csc, DocumentoKeyModel docKey) throws RemoteException;
	public DacModel 		salvaDocumento(ClientSessionContext csc, DacModel dac) throws RemoteException;
	public DacModel 		cancellaDocumento(ClientSessionContext csc, DacModel dac) throws RemoteException;
	public DacModel 		esitaDocumento(ClientSessionContext csc, DacModel dac) throws RemoteException;
	public DacModel 		esitaFirmaCliente(ClientSessionContext csc, DacModel dac) throws RemoteException;
	public DacModel 		esitaFirmaAgente(ClientSessionContext csc, DacModel dac) throws RemoteException;

	public DacModel 		pinzaDocumenti(ClientSessionContext csc, DacModel dac) throws RemoteException;
	public DacModel 		spinzaDocumenti(ClientSessionContext csc, DacModel dac) throws RemoteException;
	
	public AbstractRicercaDacModel 	ricercaDac(ClientSessionContext csc, AbstractRicercaDacModel ricercaDac) throws RemoteException;
	public AbstractRicercaDocModel 	ricercaDoc(ClientSessionContext csc, AbstractRicercaDocModel ricercaDoc) throws RemoteException;

	public FirmeModel loadFirmeCliente(ClientSessionContext csc, FirmeModel firmeCliente) throws RemoteException;
}
