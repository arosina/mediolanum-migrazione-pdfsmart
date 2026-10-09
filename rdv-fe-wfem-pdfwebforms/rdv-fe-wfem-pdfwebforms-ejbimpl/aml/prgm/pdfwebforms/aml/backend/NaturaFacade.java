package prgm.pdfwebforms.aml.backend;

import java.rmi.RemoteException;

import com.atosorigin.wfem.backend.Facade;
import com.atosorigin.wfem.command.ClientSessionContext;

import prgm.pdfwebforms.aml.model.NaturaModel;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public interface NaturaFacade extends Facade{	

	public void init(ClientSessionContext csc, PdfModel pdf, NaturaModel natura) throws RemoteException;
	public NaturaModel scopoRapportoDispositiva(ClientSessionContext csc, PdfModel pdf) throws RemoteException;		
}
