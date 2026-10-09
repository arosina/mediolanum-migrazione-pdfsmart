package prgm.pdfwebforms.backend;

import java.rmi.RemoteException;

import com.atosorigin.wfem.backend.Facade;
import com.atosorigin.wfem.command.ClientSessionContext;

import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public interface PdfInstanceFacade extends Facade{	
	public PdfModel 			newTestPdf(ClientSessionContext csc, PdfDataModel pdfData, boolean isOnWork) throws RemoteException;
	public PdfModel 			newPdf(ClientSessionContext csc, PdfDataModel pdfData) throws RemoteException;
	public PdfModel 			clonePdf(ClientSessionContext csc, PdfDataModel pdfData) throws RemoteException;
	public PdfModel 			readPdf(ClientSessionContext csc, PdfDataModel pdfData) throws RemoteException;
	public PdfModel 			gotoPdf(ClientSessionContext csc, PdfModel pdf, int pdfIndex, boolean callOnLoadPdfOnDriver) throws RemoteException;
	public PdfModel 			nextPdf(ClientSessionContext csc, PdfModel pdf) throws RemoteException;
	public PdfModel 			verifyPdf(ClientSessionContext csc, PdfModel pdf) throws RemoteException;
	public PdfModel 			pdfVerified(ClientSessionContext csc, PdfModel pdf) throws RemoteException;
	public PdfModel 			savePdf(ClientSessionContext csc, PdfModel pdf) throws RemoteException;
	public void		 			deletePdf(ClientSessionContext csc, PdfInstanceModel pdfInstance) throws RemoteException;

	public PdfModel 			readPdfOnMomValidation(ClientSessionContext csc, PdfDataModel pdfData) throws RemoteException;
	public PdfModel 			savePdfOnMomValidation(ClientSessionContext csc, PdfModel pdf) throws RemoteException;
	public PdfModel 			savePdfOnMomImport(ClientSessionContext csc, PdfModel pdf) throws RemoteException;
	public boolean	 			confrontaDoppiaSpuntaMom(ClientSessionContext csc, PdfModel pdf, PdfDataModel pdfData) throws RemoteException;
}
