package prgm.pdfwebformsutil.drivers.service.fatcaservices.fatcainformation;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.drivers.PdfBaseDriver;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsutil.drivers.dao.fatcaservices.fatcainformation.FatcaInformationDaoAccess;
import prgm.pdfwebformsutil.drivers.dao.fatcaservices.fatcainformation.FatcaInformationModel;
import prgm.pdfwebformsutil.drivers.service.AbstractCachedService;
import prgm.pdfwebformsutil.drivers.util.Util;

public class FatcaInformationService<T extends PdfBaseDriver>
		extends AbstractCachedService<T, FatcaInformationModel, FatcaInformationModel> {
	public FatcaInformationService(T pdfDriver, boolean enableCache) {
		super(pdfDriver, enableCache);
	}

	public FatcaInformationService(T pdfDriver) {
		super(pdfDriver);
	}

	public FatcaInformationModel read(ClientSessionContext csc, PdfDataModel pdfData, StringType codiceCliente,
			StringType tipoSoggetto) throws Exception {

		FatcaInformationModel fatcaInfoModel = new FatcaInformationModel();
		fatcaInfoModel.setCodiceCliente(codiceCliente);
		fatcaInfoModel.setTipoSoggetto(tipoSoggetto);

		return read(csc, pdfData, fatcaInfoModel);
	}

	@Override
	protected FatcaInformationModel readData(ClientSessionContext csc, PdfDataModel pdfData, FatcaInformationModel key,
			boolean async) throws Exception, DAOException {
		if (key == null || key.getCodiceCliente().equals("")) {
			return null;
		}

		key.setCodiceCliente(new StringType(Util.lZeroPad(key.getCodiceCliente().toString(), 11)));

		FatcaInformationDaoAccess daoAccess = new FatcaInformationDaoAccess(csc, key, pdfData, this);
		if (async) {
			daoAccess.executeAsync();
			return null;
		} else {
			daoAccess.executeSync();
			return key;
		}
	}

	@Override
	protected String getCacheKey(PdfDataModel pdfData, FatcaInformationModel key) {
		return String.format("%s%s", key.getTipoSoggetto(), Util.lZeroPad(key.getCodiceCliente().toString(), 11));
	}

	@Override
	public void onExecutionSuccess(Object sender, PdfDataModel pdfData, Object o) {
		FatcaInformationModel obj = ((FatcaInformationModel) o);
		setHolderValueAndReleaseLock(pdfData, getCacheKey(pdfData, obj), obj);
	}

	@Override
	public void onExecutionError(Object sender, PdfDataModel pdfData, Object o, Throwable e) {
		FatcaInformationModel obj = ((FatcaInformationModel) o);
		setHolderValueAndReleaseLock(pdfData, getCacheKey(pdfData, obj), null);
	}
}
