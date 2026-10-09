package prgm.pdfwebformsutil.drivers.service.strategieinvestimento;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.drivers.PdfBaseDriver;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsutil.drivers.dao.strategieinvestimento.StrategiePerClienteDaoAccess;
import prgm.pdfwebformsutil.drivers.dao.strategieinvestimento.StrategiePerClienteModel;
import prgm.pdfwebformsutil.drivers.service.AbstractCachedService;
import prgm.pdfwebformsutil.drivers.util.Util;

public class StrategiePerClienteService<T extends PdfBaseDriver>
		extends AbstractCachedService<T, StrategiePerClienteModel, StrategiePerClienteModel> {
	
	public StrategiePerClienteService(T pdfDriver, boolean enableCache) {
		super(pdfDriver, enableCache);
	}
	
	public StrategiePerClienteService(T pdfDriver) {
		super(pdfDriver);
	}

	public StrategiePerClienteModel read(ClientSessionContext csc, PdfDataModel pdfData, StringType codiceCliente, boolean async)
			throws Exception {

		StrategiePerClienteModel strategiePerClienteModel = new StrategiePerClienteModel();
		strategiePerClienteModel.setCodCliente(codiceCliente);
		
		return read(csc, pdfData, strategiePerClienteModel, async);
	}

	@Override
	protected StrategiePerClienteModel readData(ClientSessionContext csc, PdfDataModel pdfData, StrategiePerClienteModel key,
			boolean async) throws Exception, DAOException {
		if (key == null || key.getCodCliente().equals("")) {
			return null;
		}

		key.setCodCliente(new StringType(Util.lZeroPad(key.getCodCliente().toString(), 11)));
		
		StrategiePerClienteDaoAccess daoAccess = new StrategiePerClienteDaoAccess(csc, key, pdfData, this);
		if (async) {
			daoAccess.executeAsync();
			return null;
		} else {
			daoAccess.executeSync();
			return key;
		}
	}

	@Override
	protected String getCacheKey(PdfDataModel pdfData, StrategiePerClienteModel key) {
		return Util.lZeroPad(key.getCodCliente().toString(), 11);
	}	
}