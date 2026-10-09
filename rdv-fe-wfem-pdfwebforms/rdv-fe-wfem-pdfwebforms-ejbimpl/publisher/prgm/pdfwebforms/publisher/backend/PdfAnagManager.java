package prgm.pdfwebforms.publisher.backend;

import javax.ejb.Remote;

import prgm.pdfwebforms.publisher.model.PdfAnagKeyModel;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;

import com.atosorigin.wfem.backend.Manager;
import com.atosorigin.wfem.command.ClientSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
@Remote
public interface PdfAnagManager extends Manager {
	public PdfAnagModel		savePdfConf(ClientSessionContext csc, PdfAnagModel pdf);
	public PdfAnagModel		publishPdfConf(ClientSessionContext csc, PdfAnagModel pdf);
	public void				editPdfPublication(ClientSessionContext csc, PdfAnagKeyModel pdfAnagKey);
	public void 			deletePdfOnWork(ClientSessionContext csc, PdfAnagKeyModel pdfAnagKey);
	public void 			deletePdf(ClientSessionContext csc, PdfAnagKeyModel pdfAnagKey);
	public PdfAnagModel		updatePdfAnag(ClientSessionContext csc, PdfAnagModel pdf);
	public void				deletePdfPublication(ClientSessionContext csc, PdfAnagKeyModel pdfAnagKey);
	public void				archivePdfPublication(ClientSessionContext csc, PdfAnagKeyModel pdfAnagKey);
	public void				restorePdfPublication(ClientSessionContext csc, PdfAnagKeyModel pdfAnagKey);
}
