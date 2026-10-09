package prgm.pdfwebformsutil.drivers.service;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.util.Logger;

import prgm.pdfwebforms.drivers.PdfBaseDriver;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebformsutil.drivers.threads.AsyncExecutor;
import prgm.pdfwebformsutil.drivers.threads.ExecutorClient;

public abstract class AbstractCommandService<T extends PdfBaseDriver> extends AbstractPdfDriverService<T>
		implements Runnable {
	private ExecutorClient callbackableObject = null;
	private ClientSessionContext csc;
	private PdfModel pdfModel = null;

	public AbstractCommandService(ClientSessionContext csc, T pdfDriver, PdfModel pdfModel) {
		this(csc, pdfDriver, pdfModel, null);
	}

	public AbstractCommandService(ClientSessionContext csc, T pdfDriver, PdfModel pdfModel,
			ExecutorClient callbackableObject) {
		super(pdfDriver);
		this.csc = csc;
		this.pdfModel = pdfModel;
		this.callbackableObject = callbackableObject;
	}

	protected abstract void execute() throws Exception;

	public void executeSync() throws Exception {
		try {
			execute();
			executionSuccess(this, getPdf());
		} catch (Exception e) {
			Logger.getInstance().error(e);
			executionError(this, getPdf(), e);
			throw e;
		}
	}

	public void executeAsync() {
		AsyncExecutor executor = new AsyncExecutor();
		executor.execute(this);
	}

	public void run() {
		try {
			execute();
			executionSuccess(this, getPdf());
		} catch (Throwable e) {
			Logger.getInstance().error(e);
			executionError(this, getPdf(), e);
		}
	}

	protected void executionSuccess(Object sender, Object o) {
		if (callbackableObject != null) {
			callbackableObject.onExecutionSuccess(sender, pdfModel.mainPdfData(), o);
		}
	}

	protected void executionError(Object sender, Object o, Throwable e) {
		if (callbackableObject != null) {
			callbackableObject.onExecutionError(sender, pdfModel.mainPdfData(), o, e);
		}
	}

	public ClientSessionContext getCsc() {
		return csc;
	}

	public void setCsc(ClientSessionContext csc) {
		this.csc = csc;
	}

	public PdfModel getPdf() {
		return pdfModel;
	}
}
