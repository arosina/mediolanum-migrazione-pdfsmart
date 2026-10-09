package prgm.pdfwebformsutil.drivers.service.contocorrenteservices;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.dataentryutil.ContoAutocompleteInput;
import prgm.pdfwebforms.dataentryutil.ContoAutocompleteModel;
import prgm.pdfwebforms.drivers.PdfBaseDriver;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsutil.drivers.service.AbstractCachedService;

public class ContoCorrenteService<T extends PdfBaseDriver>
		extends AbstractCachedService<T, ContoAutocompleteInput, ContoAutocompleteModel> {

	private static final String IBAN_CONTO = "ibanConto";

	public ContoCorrenteService(T pdfDriver, boolean enableCache) {
		super(pdfDriver, enableCache);
	}

	public ContoCorrenteService(T pdfDriver) {
		super(pdfDriver);
	}

	@Override
	protected ContoAutocompleteModel readData(ClientSessionContext csc, PdfDataModel pdfData,
			ContoAutocompleteInput key, boolean async) throws Exception, DAOException {
		try {
			ContoAutocompleteModel contoModel = pdfData.getPdfDriver().readConto(csc, pdfData, key);
			if (contoModel == null) {
				contoModel = new ContoAutocompleteModel();
				contoModel.setNotFound(true);
			}
			if (contoModel.isNotFound()) {				
				StringType ibanConto = (StringType) pdfData.read(key.getFieldName());
				contoModel.addProperty(IBAN_CONTO, ibanConto);
			}
			onExecutionSuccess(this, pdfData, contoModel);
			return contoModel;
		} catch (Exception e) {
			onExecutionError(this, pdfData, key, e);
			throw e;
		}
	}

	@Override
	protected String getCacheKey(PdfDataModel pdfData, ContoAutocompleteInput key) {
		StringType result = (StringType) pdfData.read(key.getFieldName());
		if (result != null) {
			return result.toString();
		} else {
			return null;
		}
	}

	@Override
	public void onExecutionSuccess(Object sender, PdfDataModel pdfData, Object o) {
		ContoAutocompleteModel obj = ((ContoAutocompleteModel) o);
		setHolderValueAndReleaseLock(pdfData, obj.readProperty(IBAN_CONTO).toString(), obj);
	}

	@Override
	public void onExecutionError(Object sender, PdfDataModel pdfData, Object o, Throwable e) {
		ContoAutocompleteInput obj = ((ContoAutocompleteInput) o);
		setHolderValueAndReleaseLock(pdfData, getCacheKey(pdfData, obj), null);
	}
}
