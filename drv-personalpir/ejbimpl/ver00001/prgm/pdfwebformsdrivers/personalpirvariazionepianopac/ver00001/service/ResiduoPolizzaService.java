package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.service;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.PdfDriver;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.dao.RecuperaResiduoPolizzaDaoAccess;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.model.ResiduoPolizzaModel;
import prgm.pdfwebformsutil.drivers.service.AbstractCachedService;

public class ResiduoPolizzaService extends AbstractCachedService<PdfDriver, ResiduoPolizzaModel, ResiduoPolizzaModel> {
	public ResiduoPolizzaService(PdfDriver pdfDriver, boolean enableCache) {
		super(pdfDriver, enableCache);
	}

	public ResiduoPolizzaService(PdfDriver pdfDriver) {
		super(pdfDriver);
	}

	public ResiduoPolizzaModel read(ClientSessionContext csc, StringType codProdotto, StringType numeroContratto, boolean async)
			throws Exception {
		if (numeroContratto.isNull()) {
			return null;
		}

		ResiduoPolizzaModel residuoModel = new ResiduoPolizzaModel();
		residuoModel.setCodProdotto(codProdotto);
		residuoModel.setNumeroContratto(numeroContratto);
		return read(csc, null, residuoModel, async);
	}

	@Override
	protected ResiduoPolizzaModel readData(ClientSessionContext csc, PdfDataModel pdfData, ResiduoPolizzaModel key, boolean async)
			throws Exception, DAOException {
		if (key == null) {
			return null;
		}

		RecuperaResiduoPolizzaDaoAccess daoAccess = new RecuperaResiduoPolizzaDaoAccess(csc, key, pdfData, this);
		if (async) {
			daoAccess.executeAsync();
			return null;
		} else {
			daoAccess.executeSync();
			return key;
		}
	}

	@Override
	protected String getCacheKey(PdfDataModel pdfData, ResiduoPolizzaModel key) {
		return key.getNumeroContratto().toString();
	}	
}
