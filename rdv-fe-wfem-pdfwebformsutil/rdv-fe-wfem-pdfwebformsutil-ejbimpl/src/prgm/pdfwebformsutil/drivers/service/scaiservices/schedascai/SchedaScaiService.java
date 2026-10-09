package prgm.pdfwebformsutil.drivers.service.scaiservices.schedascai;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.drivers.PdfBaseDriver;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsutil.drivers.dao.scaiservices.schedascai.SchedaScaiDaoAccess;
import prgm.pdfwebformsutil.drivers.dao.scaiservices.schedascai.SchedaScaiModel;
import prgm.pdfwebformsutil.drivers.service.AbstractCachedService;
import prgm.pdfwebformsutil.drivers.util.Util;

public class SchedaScaiService<T extends PdfBaseDriver> extends AbstractCachedService<T, String, SchedaScaiModel> {
	public SchedaScaiService(T pdfDriver, boolean enableCache) {
		super(pdfDriver, enableCache);
	}

	public SchedaScaiService(T pdfDriver) {
		super(pdfDriver);
	}

	@Override
	protected SchedaScaiModel readData(ClientSessionContext csc, PdfDataModel pdfData, String key, boolean async)
			throws Exception, DAOException {
		if (key == null || key.equals("")) {
			return null;
		}

		SchedaScaiModel schedaScaiModel = new SchedaScaiModel();
		schedaScaiModel.setCodiceCliente(new StringType(Util.lZeroPad(key, 11)));

		SchedaScaiDaoAccess daoAccess = new SchedaScaiDaoAccess(csc, schedaScaiModel, pdfData, this);
		if (async) {
			daoAccess.executeAsync();
			return null;
		} else {
			daoAccess.executeSync();
			return schedaScaiModel;
		}
	}

	@Override
	protected String getCacheKey(PdfDataModel pdfData, String key) {
		return Util.lZeroPad(key, 11);
	}

	@Override
	public void onExecutionSuccess(Object sender, PdfDataModel pdfData, Object o) {
		SchedaScaiModel obj = ((SchedaScaiModel) o);
		setHolderValueAndReleaseLock(pdfData, obj.getCodiceCliente().toString(), obj);
	}

	@Override
	public void onExecutionError(Object sender, PdfDataModel pdfData, Object o, Throwable e) {
		SchedaScaiModel obj = ((SchedaScaiModel) o);
		setHolderValueAndReleaseLock(pdfData, obj.getCodiceCliente().toString(), null);
	}
}
