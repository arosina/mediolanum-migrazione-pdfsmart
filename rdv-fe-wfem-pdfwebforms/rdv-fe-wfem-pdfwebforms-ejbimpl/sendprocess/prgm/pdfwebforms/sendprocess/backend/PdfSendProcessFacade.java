package prgm.pdfwebforms.sendprocess.backend;

import java.rmi.RemoteException;

import prgm.pdfwebforms.model.PdfModel;

import com.atosorigin.wfem.backend.Facade;
import com.atosorigin.wfem.command.ClientSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public interface PdfSendProcessFacade extends Facade{	
	public PdfModel sendPdfInstance(ClientSessionContext csc, PdfModel pdf, byte[] pdfContent) throws RemoteException;
}
