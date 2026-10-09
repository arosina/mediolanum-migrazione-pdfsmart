package prgm.pdfwebformsutil.drivers.dao.strategieinvestimento;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOOSBResultModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.util.XmlServiceCallData;

import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsutil.drivers.dao.AbstractCommandDaoAccess;
import prgm.pdfwebformsutil.drivers.threads.ExecutorClient;

public class StrategiePerClienteDaoAccess extends AbstractCommandDaoAccess<StrategiePerClienteModel> {
	private static final String DAO_FILE_NAME = "PdfWebFormsUtil.StrategiaInvestimentoService";
	private static final String ACCESS_NAME = "strategiaInvestimento";
	private static final String SERVICE_NAME = "/Enterprise_service/soggetto/posizioneinformativa/StrategiaInvestimento/v1/inbound/PS_WS_StrategiaInvestimentoService_v1.0";

	public StrategiePerClienteDaoAccess(ClientSessionContext csc, StrategiePerClienteModel dataModel) {
		super(csc, DAO_FILE_NAME, dataModel);
	}

	public StrategiePerClienteDaoAccess(ClientSessionContext csc, DAOObject daoObj,
			StrategiePerClienteModel  dataModel) {
		super(csc, daoObj, dataModel);
	}

	public StrategiePerClienteDaoAccess(ClientSessionContext csc, StrategiePerClienteModel  dataModel,
			PdfDataModel pdfData, ExecutorClient callbackableObject) {
		super(csc, DAO_FILE_NAME, dataModel, pdfData, callbackableObject);
	}

	public StrategiePerClienteDaoAccess(ClientSessionContext csc, DAOObject daoObj,
			StrategiePerClienteModel  dataModel, PdfDataModel pdfData, ExecutorClient callbackableObject) {
		super(csc, daoObj, dataModel, pdfData, callbackableObject);
	}

	@Override
	protected void execute() throws DAOException {
		DAOOSBResultModel wsRes;		
		wsRes = getDaoObj().executeOSBAccess(ACCESS_NAME, getDataModel());
		if (wsRes.getWsCallData().getStatus() == XmlServiceCallData.STATUS_SERVICE_DISABLED) {
			throw new DAOException("Si sono verificati problemi tecnici: Servizio " + SERVICE_NAME + " disabilitato");
		} else if (wsRes.getWsCallData().getStatus() != XmlServiceCallData.STATUS_OK) {
			throw new DAOException("Si sono verificati problemi tecnici: Errore di comunicazione con il sistema remoto " + SERVICE_NAME);
		} else if (getDataModel().getResultCode().intValue() != 0) {
			throw new DAOException("Si sono verificati problemi tecnici: Errore di elaborazione del sistema remoto "
					+ SERVICE_NAME + " [" + getDataModel().getResultCode().toString() + "]");
		}
	}
}