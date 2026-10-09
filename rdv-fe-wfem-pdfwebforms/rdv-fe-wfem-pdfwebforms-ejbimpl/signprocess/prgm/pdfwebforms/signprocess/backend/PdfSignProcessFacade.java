package prgm.pdfwebforms.signprocess.backend;

import java.rmi.RemoteException;

import prgm.pdfwebforms.model.PdfModel;

import com.atosorigin.wfem.backend.Facade;
import com.atosorigin.wfem.command.ClientSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public interface PdfSignProcessFacade extends Facade{	
	public PdfModel signPdfInstance(ClientSessionContext csc, PdfModel pdf, byte[] pdfContent) throws RemoteException;
}
