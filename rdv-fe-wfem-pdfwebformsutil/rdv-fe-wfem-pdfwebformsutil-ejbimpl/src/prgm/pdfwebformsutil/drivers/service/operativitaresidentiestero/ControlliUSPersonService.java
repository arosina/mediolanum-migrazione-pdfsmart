package prgm.pdfwebformsutil.drivers.service.operativitaresidentiestero;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.drivers.PdfBaseDriver;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsutil.drivers.dao.medsecurityservices.nuovasessionetecnica.NuovaSessioneTecnicaDaoAccess;
import prgm.pdfwebformsutil.drivers.dao.medsecurityservices.nuovasessionetecnica.NuovaSessioneTecnicaModel;
import prgm.pdfwebformsutil.drivers.dao.operativitaresidentiestero.ControlliUSPersonDaoAccess;
import prgm.pdfwebformsutil.drivers.operativitaresidentiestero.model.ControlliUSPersonModel;
import prgm.pdfwebformsutil.drivers.service.AbstractCachedService;

public class ControlliUSPersonService<T extends PdfBaseDriver> extends AbstractCachedService<T, ControlliUSPersonModel, ControlliUSPersonModel> {
	
	public static final String WEBCOOKIE_NMOL_FB = "NMOL-FB";
	
	public ControlliUSPersonService(T pdfDriver, boolean enableCache) {
		super(pdfDriver, enableCache);
	}

	public ControlliUSPersonService(T pdfDriver) {
		super(pdfDriver);
	}
	
	@Override
	protected ControlliUSPersonModel readData(ClientSessionContext csc, PdfDataModel pdfData, ControlliUSPersonModel model,
			boolean async) throws Exception, DAOException {
		
		if (model == null) {
			return null;
		}

		NuovaSessioneTecnicaModel sessionModel = new NuovaSessioneTecnicaModel();
		sessionModel.setApplicationId(new StringType(WEBCOOKIE_NMOL_FB));
		sessionModel.setUserId(new StringType(csc.getUserCode()));

		NuovaSessioneTecnicaDaoAccess daoSession = new NuovaSessioneTecnicaDaoAccess(csc, sessionModel);
		daoSession.executeSync();

		model.setWebCookie(sessionModel.getWebCookie());
		
		ControlliUSPersonDaoAccess daoAccess = new ControlliUSPersonDaoAccess(csc, model, pdfData, this);

		if (async) {
			daoAccess.executeAsync();
			return null;
		} else {
			daoAccess.executeSync();
			return model;
		}
	}

	@Override
	protected String getCacheKey(PdfDataModel pdfData, ControlliUSPersonModel key) {
		return key.getKey();
	}

	@Override
	public void onExecutionSuccess(Object sender, PdfDataModel pdfData, Object o) {
		ControlliUSPersonModel obj = ((ControlliUSPersonModel) o);
		setHolderValueAndReleaseLock(pdfData, getCacheKey(pdfData, obj), obj);
	}

	@Override
	public void onExecutionError(Object sender, PdfDataModel pdfData, Object o, Throwable e) {
		ControlliUSPersonModel obj = ((ControlliUSPersonModel) o);
		setHolderValueAndReleaseLock(pdfData, getCacheKey(pdfData, obj), null);
	}
}
