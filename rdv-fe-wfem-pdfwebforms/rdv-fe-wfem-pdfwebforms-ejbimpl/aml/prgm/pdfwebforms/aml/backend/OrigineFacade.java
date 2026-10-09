package prgm.pdfwebforms.aml.backend;

import java.rmi.RemoteException;

import com.atosorigin.wfem.backend.Facade;
import com.atosorigin.wfem.command.ClientSessionContext;

import prgm.pdfwebforms.aml.model.OrigineModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.model.PdfPersonModel;

/***********************************************************************************************/
/***********************************************************************************************/
public interface OrigineFacade extends Facade{	

	public void init(ClientSessionContext csc, PdfModel pdf,  OrigineModel origine) throws RemoteException;
	public OrigineModel importiOrigineDispositiva(ClientSessionContext csc, PdfModel pdf) throws RemoteException;
	public boolean isPersonaConsiderataFisica(ClientSessionContext csc, PdfPersonModel cli) throws RemoteException;

}
