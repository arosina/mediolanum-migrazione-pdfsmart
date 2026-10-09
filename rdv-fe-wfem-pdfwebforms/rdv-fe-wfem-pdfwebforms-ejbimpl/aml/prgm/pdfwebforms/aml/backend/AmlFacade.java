package prgm.pdfwebforms.aml.backend;

import java.rmi.RemoteException;
import java.util.List;

import com.atosorigin.wfem.backend.Facade;
import com.atosorigin.wfem.command.ClientSessionContext;

import prgm.pdfwebforms.aml.model.AmlModel;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public interface AmlFacade extends Facade{	

	public void 				createAmlAndCoraModel(ClientSessionContext csc, PdfModel pdf) throws RemoteException;
	public AmlModel				createAmlModel(ClientSessionContext csc, PdfModel pdf, List<String> elencoCodiciModuloAML) throws RemoteException;
	public boolean 				verifyAml(AmlModel amlModel) throws RemoteException;
	public void		 			addAmlHiddenModulesToBasket(ClientSessionContext csc, PdfModel pdf) throws RemoteException;
	public void		 			removeAllAmlHiddenModulesFromBasket(ClientSessionContext csc, PdfModel pdf) throws RemoteException;
	public void		 			removePdfAmlHiddenModules(ClientSessionContext csc, PdfModel pdf) throws RemoteException;
	public void 				createCoraForMom(ClientSessionContext csc, PdfModel pdf) throws RemoteException;
}
