package prgm.pdfwebformsutil.drivers.service.posizionewealthservices.recuperaposizionewealth;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.drivers.PdfBaseDriver;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsutil.drivers.dao.posizionewealthservices.recuperaposizionewealth.PosizioneWealthModel;
import prgm.pdfwebformsutil.drivers.dao.posizionewealthservices.recuperaposizionewealth.RecuperaPosizioneWealthDaoAccess;
import prgm.pdfwebformsutil.drivers.service.AbstractCachedService;
import prgm.pdfwebformsutil.drivers.util.Util;

public class RecuperaPosizioneWealthService<T extends PdfBaseDriver>
		extends AbstractCachedService<T, PosizioneWealthModel, PosizioneWealthModel> {
	public RecuperaPosizioneWealthService(T pdfDriver, boolean enableCache) {
		super(pdfDriver, enableCache);
	}

	public RecuperaPosizioneWealthService(T pdfDriver) {
		super(pdfDriver);
	}

	public PosizioneWealthModel read(ClientSessionContext csc, PdfDataModel pdfData, StringType codiceCliente)
			throws Exception {

		PosizioneWealthModel recuperaPosizioneWealthModel = new PosizioneWealthModel();
		recuperaPosizioneWealthModel.setCodiceCliente(codiceCliente);

		return read(csc, pdfData, recuperaPosizioneWealthModel);
	}

	@Override
	protected PosizioneWealthModel readData(ClientSessionContext csc, PdfDataModel pdfData, PosizioneWealthModel key,
			boolean async) throws Exception, DAOException {
		if (key == null || key.getCodiceCliente().equals("")) {
			return null;
		}

		key.setCodiceCliente(new StringType(Util.lZeroPad(key.getCodiceCliente().toString(), 11)));

		RecuperaPosizioneWealthDaoAccess daoAccess = new RecuperaPosizioneWealthDaoAccess(csc, key, pdfData, this);
		if (async) {
			daoAccess.executeAsync();
			return null;
		} else {
			daoAccess.executeSync();
			return key;
		}
	}

	@Override
	protected String getCacheKey(PdfDataModel pdfData, PosizioneWealthModel key) {
		return Util.lZeroPad(key.getCodiceCliente().toString(), 11);
	}

	@Override
	public void onExecutionSuccess(Object sender, PdfDataModel pdfData, Object o) {
		PosizioneWealthModel obj = ((PosizioneWealthModel) o);
		setHolderValueAndReleaseLock(pdfData, getCacheKey(pdfData, obj), obj);
	}

	@Override
	public void onExecutionError(Object sender, PdfDataModel pdfData, Object o, Throwable e) {
		PosizioneWealthModel obj = ((PosizioneWealthModel) o);
		setHolderValueAndReleaseLock(pdfData, getCacheKey(pdfData, obj), null);
	}
}
