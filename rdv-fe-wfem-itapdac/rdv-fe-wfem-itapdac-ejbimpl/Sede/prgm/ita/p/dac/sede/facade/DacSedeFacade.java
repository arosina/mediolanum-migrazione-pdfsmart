package prgm.ita.p.dac.sede.facade;

import java.rmi.RemoteException;

import prgm.ita.p.dac.model.DacKeyModel;
import prgm.ita.p.dac.model.DacModel;
import prgm.ita.p.dac.model.DocumentoModel;
import prgm.ita.p.dac.model.ParamsModel;
import prgm.ita.p.dac.sede.model.SbloccaDacModel;

import com.atosorigin.wfem.backend.Facade;
import com.atosorigin.wfem.command.ClientSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public interface DacSedeFacade extends Facade {
	//Creazione
	public DacModel 			nuovaDac(ClientSessionContext csc, ParamsModel params) throws RemoteException;
	public DacModel 			nuovaDacResi(ClientSessionContext csc, ParamsModel params) throws RemoteException;	
	public DacModel 			spedisciDac(ClientSessionContext csc, DacModel dac) throws RemoteException;
	public DacModel 			verificaSeEsisteDacAttiva(ClientSessionContext csc, DacModel dac) throws RemoteException;
	public DocumentoModel		inserisciDocInDac(ClientSessionContext csc, DocumentoModel docDaInserire) throws RemoteException;
	public DocumentoModel		rimuoviDocDaDac(ClientSessionContext csc, DocumentoModel doc) throws RemoteException;
	public DocumentoModel		spinzaDocDaDac(ClientSessionContext csc, DocumentoModel doc) throws RemoteException;
	
	//Ricezione
	public DacModel 			gestisciDac(ClientSessionContext csc, DacKeyModel dacKey) throws RemoteException;
	public DocumentoModel		aggiornaDatiDocInRicezione(ClientSessionContext csc, DocumentoModel docDaAggiornare) throws RemoteException;
	public DacModel				aggiungiDocInRicezione(ClientSessionContext csc, DacModel dac) throws RemoteException;
	public DacModel 			verificaSeDocumentoInErrSmistamento(ClientSessionContext csc, DacModel dac) throws RemoteException;
	public DacModel 			chiudiDac(ClientSessionContext csc, DacModel dac) throws RemoteException;
	
	//Tools
	public SbloccaDacModel		loadDacAttive(ClientSessionContext csc, SbloccaDacModel sbloccaDac) throws RemoteException;
	public SbloccaDacModel		riceviDocForzato(ClientSessionContext csc, SbloccaDacModel sbloccaDac) throws RemoteException;
}
