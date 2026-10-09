package prgm.pdfwebformsutil.drivers.service.mail;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.exceptions.DAOException;

import prgm.pdfwebforms.drivers.PdfBaseDriver;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsutil.drivers.dao.mail.InvioMailGenericaConAttachmentDaoAccess;
import prgm.pdfwebformsutil.drivers.dao.mail.MailModel;
import prgm.pdfwebformsutil.drivers.service.AbstractPdfDriverService;
import prgm.pdfwebformsutil.drivers.threads.ExecutorClient;

public class MailService<T extends PdfBaseDriver> extends AbstractPdfDriverService<T> {
	public MailService(T pdfDriver) {
		super(pdfDriver);
	}
	
	public void inviaMailGenericaConAttachment(ClientSessionContext csc, PdfDataModel pdfData, MailModel model) throws Exception { 
		inviaMailGenericaConAttachment(csc, pdfData, model, false, this);	
	}
	
	public void inviaMailGenericaConAttachment(ClientSessionContext csc, PdfDataModel pdfData, MailModel model, boolean async) throws Exception { 
		inviaMailGenericaConAttachment(csc, pdfData, model, async, this);	
	}
	
	public void inviaMailGenericaConAttachment(ClientSessionContext csc, PdfDataModel pdfData, MailModel model, boolean async, ExecutorClient callbackableObject) throws Exception {
		InvioMailGenericaConAttachmentDaoAccess daoAccess = new InvioMailGenericaConAttachmentDaoAccess(csc, model, pdfData, callbackableObject);
		try {
			if (async) {
				daoAccess.executeAsync();				
			} else {
				daoAccess.executeSync();				
			}
		} catch (DAOException e) {
			throw new Exception(e);
		}
	}
}
