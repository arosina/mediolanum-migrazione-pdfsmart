package prgm.pdfwebforms.copernicoprocess.backend;

import java.rmi.RemoteException;

import com.atosorigin.wfem.backend.Facade;
import com.atosorigin.wfem.command.ClientSessionContext;

import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public interface PdfCopernicoProcessFacade extends Facade{	
	public PdfModel sendToCliPdfInstance(ClientSessionContext csc, PdfModel pdf, byte[] pdfContent) throws RemoteException;
	public PdfModel acceptPdfInstance(ClientSessionContext csc, PdfModel pdf, byte[] pdfContent) throws RemoteException;
}
