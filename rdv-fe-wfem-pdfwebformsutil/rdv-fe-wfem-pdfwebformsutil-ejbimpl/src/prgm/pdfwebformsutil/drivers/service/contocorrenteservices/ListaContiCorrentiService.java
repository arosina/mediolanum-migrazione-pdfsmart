package prgm.pdfwebformsutil.drivers.service.contocorrenteservices;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.dataentryutil.ContoAutocompleteInput;
import prgm.pdfwebforms.dataentryutil.ContoAutocompleteModel;
import prgm.pdfwebforms.drivers.PdfBaseDriver;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsutil.drivers.service.AbstractCachedService;
import prgm.pdfwebformsutil.drivers.util.Util;

public class ListaContiCorrentiService<T extends PdfBaseDriver>
		extends AbstractCachedService<T, ContoAutocompleteInput, ListaContiCorrentiModel> {

	public ListaContiCorrentiService(T pdfDriver, boolean enableCache) {
		super(pdfDriver, enableCache);
	}

	public ListaContiCorrentiService(T pdfDriver) {
		super(pdfDriver);
	}

	@Override
	protected ListaContiCorrentiModel readData(ClientSessionContext csc, PdfDataModel pdfData,
			ContoAutocompleteInput key, boolean async) throws Exception, DAOException {
		try {

			StringType codAgente = new StringType();
			if (key.isUseCodAgente()) {
				codAgente = (StringType) pdfData.read("codiceAgente");
			}
			StringType ndgCliente = (StringType) pdfData.read(key.getNdgFieldName());
			if (key.isUseCodAgente()) {
				if ((codAgente == null || codAgente.isNull()) || (ndgCliente == null || ndgCliente.isNull())) {
					return null;
				}
			} else {
				if ((ndgCliente == null || ndgCliente.isNull())) {
					return null;
				}
			}

			DAOObject dao = new DAOObject(csc, "PdfWebForms.PdfDataentryUtil");
			ContoAutocompleteModel inputModel = new ContoAutocompleteModel();
			inputModel.setTipoConto(new StringType(key.getTipoConto()));
			inputModel.setRuoliAmmessi(new StringType(key.getRuoliAmmessi()));
			inputModel.setDivisaConto(new StringType(key.getDivisaConto()));
			inputModel.setCodAgente(codAgente);
			inputModel.setNdgCliente(ndgCliente);

			DAOQueryResultModel qRes = dao.executeQueryAccess("contoAutocomplete", inputModel);
			ListaContiCorrentiModel res = new ListaContiCorrentiModel();
			res.setInput(key);
			res.setItems(qRes.getResult());

			for (int i = 0; i < res.getItems().size(); i++) {
				ContoAutocompleteModel conto = (ContoAutocompleteModel) qRes.getResult().get(i);
				conto.setNominativiConto(dao.executeQueryAccess("nominativiContoAutocomplete", conto).getResult());
			}

			onExecutionSuccess(this, pdfData, res);
			return res;
		} catch (Exception e) {
			onExecutionError(this, pdfData, key, e);
			throw e;
		}
	}

	@Override
	protected String getCacheKey(PdfDataModel pdfData, ContoAutocompleteInput key) {
		if (key != null) {
			StringType codAgente = new StringType();
			if (key.isUseCodAgente()) {
				codAgente = (StringType) pdfData.read("codiceAgente");
			}
			StringType codCliente = (StringType) pdfData.read(key.getNdgFieldName());
			return String.format("%s;%s;%s;%s;%s", Util.lZeroPad(codCliente.toString(), 11),
					Util.lZeroPad(codAgente.toString(), 10), key.getTipoConto(), key.getRuoliAmmessi(),
					key.getDivisaConto());
		}
		return null;
	}

	@Override
	public void onExecutionSuccess(Object sender, PdfDataModel pdfData, Object o) {
		ListaContiCorrentiModel obj = ((ListaContiCorrentiModel) o);
		String key = getCacheKey(pdfData, obj.getInput());
		setHolderValueAndReleaseLock(pdfData, key, obj);
	}

	@Override
	public void onExecutionError(Object sender, PdfDataModel pdfData, Object o, Throwable e) {
		ContoAutocompleteInput obj = ((ContoAutocompleteInput) o);
		String key = getCacheKey(pdfData, obj);
		setHolderValueAndReleaseLock(pdfData, key, null);
	}
}
