package prgm.pdfwebformsutil.drivers.service.materialeprecontrattuale;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.exceptions.DAOException;

import prgm.pdfwebforms.drivers.PdfBaseDriver;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsutil.drivers.dao.materialeprecontrattuale.MaterialePreContrattualeCarrelloDaoAccess;
import prgm.pdfwebformsutil.drivers.materialeprecontrattuale.MaterialePreContrattualeModel;
import prgm.pdfwebformsutil.drivers.service.AbstractCachedService;
import prgm.pdfwebformsutil.drivers.util.Util;

public class MaterialePreContrattualeCarrelloService <T extends PdfBaseDriver> extends AbstractCachedService<T, MaterialePreContrattualeModel, MaterialePreContrattualeModel>{
	
	public MaterialePreContrattualeCarrelloService(T pdfDriver, boolean enableCache) {
		super(pdfDriver, enableCache);
	}

	public MaterialePreContrattualeCarrelloService(T pdfDriver) {
		super(pdfDriver);
	}

	
	@Override
	protected MaterialePreContrattualeModel readData(ClientSessionContext csc, PdfDataModel pdfData, MaterialePreContrattualeModel materialeContrattualeModel, boolean async)
			throws Exception, DAOException {
		if (materialeContrattualeModel == null) {
			return null;
		}
		
		MaterialePreContrattualeCarrelloDaoAccess daoAccess = new MaterialePreContrattualeCarrelloDaoAccess(csc, materialeContrattualeModel, pdfData, this);
		
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
		return Util.concat(Util.lZeroPad(key.getCodiceFb().toString(), 10), Util.lZeroPad(key.getIdProposta().toString(), 10), Util.lZeroPad(key.getIdOrdine().toString(), 10));
	}	
}
