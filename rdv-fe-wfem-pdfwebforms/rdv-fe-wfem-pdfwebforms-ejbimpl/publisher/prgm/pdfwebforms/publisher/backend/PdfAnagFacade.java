package prgm.pdfwebforms.publisher.backend;

import java.rmi.RemoteException;

import com.atosorigin.wfem.backend.Facade;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;

import prgm.pdfwebforms.publisher.model.PdfAnagKeyModel;
import prgm.pdfwebforms.publisher.model.PdfConfigurationModel;
import prgm.pdfwebforms.publisher.model.PdfPublisherModel;

/***********************************************************************************************/
/***********************************************************************************************/
public interface PdfAnagFacade extends Facade{	
	public CommandDataModel 		fillCodDesc(ClientSessionContext csc, CommandDataModel model, boolean includeInnerModels) throws RemoteException;
	public PdfPublisherModel 		searchPdf(ClientSessionContext csc, PdfPublisherModel pdfPubliherModel) throws RemoteException;
	public PdfConfigurationModel	newPdfConf(ClientSessionContext csc) throws RemoteException;
	public PdfConfigurationModel	openPdfConf(ClientSessionContext csc, PdfAnagKeyModel pdfAnagKey) throws RemoteException;
	public PdfConfigurationModel	openPdfConf(ClientSessionContext csc, PdfAnagKeyModel pdfAnagKey, boolean light) throws RemoteException;
	public PdfConfigurationModel	savePdfConf(ClientSessionContext csc, PdfConfigurationModel confModel) throws RemoteException;
	public PdfConfigurationModel	publishPdfConf(ClientSessionContext csc, PdfConfigurationModel confModel) throws RemoteException;
	public PdfConfigurationModel 	editPdfPublication(ClientSessionContext csc, PdfConfigurationModel confModel) throws RemoteException;
	public PdfConfigurationModel 	cancelChangesPdfConf(ClientSessionContext csc, PdfConfigurationModel confModel) throws RemoteException;
	public PdfConfigurationModel	updatePdfAnag(ClientSessionContext csc, PdfConfigurationModel confModel) throws RemoteException;
	public PdfAnagKeyModel 			deletePdf(ClientSessionContext csc, PdfAnagKeyModel pdfAnagKey) throws RemoteException;
	public PdfConfigurationModel	deletePdfPublication(ClientSessionContext csc, PdfConfigurationModel confModel) throws RemoteException;
	public PdfConfigurationModel	archivePdfPublication(ClientSessionContext csc, PdfConfigurationModel confModel) throws RemoteException;
	public PdfConfigurationModel	restorePdfPublication(ClientSessionContext csc, PdfConfigurationModel confModel) throws RemoteException;
	public PdfConfigurationModel	savePageImage(ClientSessionContext csc, PdfConfigurationModel confModel) throws RemoteException;
}
