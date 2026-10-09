package prgm.pdfwebformsutil.drivers.service.clienteservices;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.exceptions.DAOException;

import prgm.pdfwebforms.drivers.PdfBaseDriver;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfPersonModel;
import prgm.pdfwebformsutil.drivers.service.AbstractCachedService;
import prgm.pdfwebformsutil.drivers.util.Util;

public class ClienteAnyService<T extends PdfBaseDriver> extends AbstractCachedService<T, PdfPersonModel, PdfPersonModel> {
	public ClienteAnyService(T pdfDriver, boolean enableCache) {
		super(pdfDriver, enableCache);
	}

	public ClienteAnyService(T pdfDriver) {
		super(pdfDriver);
	}

	@Override
	protected PdfPersonModel readData(ClientSessionContext csc, PdfDataModel pdfData, PdfPersonModel key, boolean async)
			throws Exception, DAOException {
		try {
			PdfPersonModel personModel = getPdfDriver().readAnyCliente(csc, getCacheKey(pdfData, key));

			onExecutionSuccess(this, pdfData, personModel);
			return personModel;		
		} catch (Exception e) {
			onExecutionError(this, pdfData, key, e);
			throw e;
		}
	}

	@Override
	protected String getCacheKey(PdfDataModel pdfData, PdfPersonModel key) {
		if (!key.getNdg().isNull()) {
			return Util.lZeroPad(key.getNdg().toString(), 11);
		} else {
			return key.getIdCensimento().toString();
		}
	}

	@Override
	public void onExecutionSuccess(Object sender, PdfDataModel pdfData, Object o) {
		PdfPersonModel obj = ((PdfPersonModel) o);
		setHolderValueAndReleaseLock(pdfData, getCacheKey(pdfData, obj), obj);
	}

	@Override
	public void onExecutionError(Object sender, PdfDataModel pdfData, Object o, Throwable e) {
		PdfPersonModel obj = ((PdfPersonModel) o);
		setHolderValueAndReleaseLock(pdfData, getCacheKey(pdfData, obj), null);
	}
}
