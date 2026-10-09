package prgm.pdfwebformsutil.drivers.dao;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.util.Logger;

import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsutil.drivers.threads.AsyncExecutor;
import prgm.pdfwebformsutil.drivers.threads.ExecutorClient;

public abstract class AbstractCommandDaoAccess<T extends CommandDataModel> extends AbstractDaoAccess
		implements Runnable {
	private T dataModel;
	private ExecutorClient callbackableObject = null;
	private PdfDataModel pdfData = null;

	public T getDataModel() {
		return dataModel;
	}

	public void setDataModel(T dataModel) {
		this.dataModel = dataModel;
	}

	public AbstractCommandDaoAccess(ClientSessionContext csc, String daoFileName, T dataModel, PdfDataModel pdfData,
			ExecutorClient callbackableObject) {
		super(csc, daoFileName);
		this.dataModel = dataModel;
		this.pdfData = pdfData;
		this.callbackableObject = callbackableObject;
	}

	public AbstractCommandDaoAccess(ClientSessionContext csc, DAOObject daoObj, T dataModel, PdfDataModel pdfData,
			ExecutorClient callbackableObject) {
		super(csc, daoObj);
		this.dataModel = dataModel;
		this.pdfData = pdfData;
		this.callbackableObject = callbackableObject;
	}

	public AbstractCommandDaoAccess(ClientSessionContext csc, String daoFileName, T dataModel, PdfDataModel pdfData) {
		this(csc, daoFileName, dataModel, pdfData, null);
	}

	public AbstractCommandDaoAccess(ClientSessionContext csc, DAOObject daoObj, T dataModel, PdfDataModel pdfData) {
		this(csc, daoObj, dataModel, pdfData, null);
	}

	public AbstractCommandDaoAccess(ClientSessionContext csc, String daoFileName, T dataModel) {
		this(csc, daoFileName, dataModel, null, null);
	}

	public AbstractCommandDaoAccess(ClientSessionContext csc, DAOObject daoObj, T dataModel) {
		this(csc, daoObj, dataModel, null, null);
	}

	protected abstract void execute() throws DAOException;

	public void executeSync() throws DAOException {
		try {
			execute();
			executionSuccess(this, getDataModel());
		} catch (DAOException e) {
			Logger.getInstance().error(e);
			executionError(this, getDataModel(), e);
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
			executionSuccess(this, getDataModel());
		} catch (Throwable e) {
			Logger.getInstance().error(e);
			executionError(this, getDataModel(), e);
		}
	}

	protected void executionSuccess(Object sender, Object o) {
		if (callbackableObject != null) {
			callbackableObject.onExecutionSuccess(sender, pdfData, o);
		}
	}

	protected void executionError(Object sender, Object o, Throwable e) {
		if (callbackableObject != null) {
			callbackableObject.onExecutionError(sender, pdfData, o, e);
		}
	}

	public PdfDataModel getPdfData() {
		return pdfData;
	}
}
