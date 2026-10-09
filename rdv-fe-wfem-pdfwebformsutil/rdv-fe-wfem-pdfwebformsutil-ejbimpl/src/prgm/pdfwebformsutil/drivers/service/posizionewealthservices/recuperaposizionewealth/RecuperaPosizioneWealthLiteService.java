package prgm.pdfwebformsutil.drivers.service.posizionewealthservices.recuperaposizionewealth;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.drivers.PdfBaseDriver;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsutil.drivers.dao.posizionewealthservices.recuperaposizionewealth.PosizioneWealthLiteModel;
import prgm.pdfwebformsutil.drivers.dao.posizionewealthservices.recuperaposizionewealth.RecuperaPosizioneWealthLiteDaoAccess;
import prgm.pdfwebformsutil.drivers.service.AbstractCachedService;
import prgm.pdfwebformsutil.drivers.util.Util;

public class RecuperaPosizioneWealthLiteService<T extends PdfBaseDriver>
		extends AbstractCachedService<T, PosizioneWealthLiteModel, PosizioneWealthLiteModel> {
	public RecuperaPosizioneWealthLiteService(T pdfDriver, boolean enableCache) {
		super(pdfDriver, enableCache);
	}

	public RecuperaPosizioneWealthLiteService(T pdfDriver) {
		super(pdfDriver);
	}

	public PosizioneWealthLiteModel read(ClientSessionContext csc, PdfDataModel pdfData, StringType codiceCliente)
			throws Exception {

		PosizioneWealthLiteModel recuperaPosizioneWealthLiteModel = new PosizioneWealthLiteModel();
		recuperaPosizioneWealthLiteModel.setCodiceCliente(codiceCliente);

		return read(csc, pdfData, recuperaPosizioneWealthLiteModel);
	}

	@Override
	protected PosizioneWealthLiteModel readData(ClientSessionContext csc, PdfDataModel pdfData, PosizioneWealthLiteModel key,
			boolean async) throws Exception, DAOException {
		if (key == null || key.getCodiceCliente().equals("")) {
			return null;
		}

		key.setCodiceCliente(new StringType(Util.lZeroPad(key.getCodiceCliente().toString(), 11)));

		RecuperaPosizioneWealthLiteDaoAccess daoAccess = new RecuperaPosizioneWealthLiteDaoAccess(csc, key, pdfData, this);
		if (async) {
			daoAccess.executeAsync();
			return null;
		} else {
			daoAccess.executeSync();
			return key;
		}
	}

	@Override
	protected String getCacheKey(PdfDataModel pdfData, PosizioneWealthLiteModel key) {
		return Util.lZeroPad(key.getCodiceCliente().toString(), 11);
	}

	@Override
	public void onExecutionSuccess(Object sender, PdfDataModel pdfData, Object o) {
		PosizioneWealthLiteModel obj = ((PosizioneWealthLiteModel) o);
		setHolderValueAndReleaseLock(pdfData, getCacheKey(pdfData, obj), obj);
	}

	@Override
	public void onExecutionError(Object sender, PdfDataModel pdfData, Object o, Throwable e) {
		PosizioneWealthLiteModel obj = ((PosizioneWealthLiteModel) o);
		setHolderValueAndReleaseLock(pdfData, getCacheKey(pdfData, obj), null);
	}
}
