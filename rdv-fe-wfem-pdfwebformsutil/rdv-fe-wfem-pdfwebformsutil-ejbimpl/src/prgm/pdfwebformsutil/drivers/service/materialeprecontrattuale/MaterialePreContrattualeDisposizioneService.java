package prgm.pdfwebformsutil.drivers.service.materialeprecontrattuale;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.exceptions.DAOException;

import prgm.pdfwebforms.drivers.PdfBaseDriver;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsutil.drivers.dao.materialeprecontrattuale.MaterialePreContrattualeDisposizioneDaoAccess;
import prgm.pdfwebformsutil.drivers.materialeprecontrattuale.MaterialePreContrattualeModel;
import prgm.pdfwebformsutil.drivers.service.AbstractCachedService;

public class MaterialePreContrattualeDisposizioneService <T extends PdfBaseDriver> extends AbstractCachedService<T, MaterialePreContrattualeModel, MaterialePreContrattualeModel>{
	
	public MaterialePreContrattualeDisposizioneService(T pdfDriver, boolean enableCache) {
		super(pdfDriver, enableCache);
	}

	public MaterialePreContrattualeDisposizioneService(T pdfDriver) {
		super(pdfDriver);
	}

	
	@Override
	protected MaterialePreContrattualeModel readData(ClientSessionContext csc, PdfDataModel pdfData, MaterialePreContrattualeModel materialeContrattualeModel, boolean async)
			throws Exception, DAOException {
		if (materialeContrattualeModel == null) {
			return null;
		}
		
		MaterialePreContrattualeDisposizioneDaoAccess daoAccess = new MaterialePreContrattualeDisposizioneDaoAccess(csc, materialeContrattualeModel, pdfData, this);
		
		if (async) {
			daoAccess.executeAsync();
			return null;
		} else {
			daoAccess.executeSync();
			return materialeContrattualeModel;
		}
	}
	
	@Override
	protected String getCacheKey(PdfDataModel pdfData, MaterialePreContrattualeModel key) {
		return key.calcolaHashIsins();
	}	
}
