package prgm.pdfwebforms.aml.backend;

import java.rmi.RemoteException;

import com.atosorigin.wfem.backend.Facade;
import com.atosorigin.wfem.command.ClientSessionContext;

import prgm.pdfwebforms.aml.model.RelazioniModel;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public interface RelazioniFacade extends Facade{	

	public void init(ClientSessionContext csc, PdfModel pdf,  RelazioniModel relazioni) throws RemoteException;
	public RelazioniModel relazioniDispositiva(ClientSessionContext csc, PdfModel pdf) throws RemoteException;	
}
