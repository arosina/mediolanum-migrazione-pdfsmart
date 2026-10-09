package prgm.pdfwebformsutil.drivers.dao.operativitaresidentiestero;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOOSBResultModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.util.XmlServiceCallData;

import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsutil.drivers.dao.AbstractCommandDaoAccess;
import prgm.pdfwebformsutil.drivers.operativitaresidentiestero.model.ControlliUSPersonModel;
import prgm.pdfwebformsutil.drivers.threads.ExecutorClient;

public class ControlliUSPersonDaoAccess extends AbstractCommandDaoAccess<ControlliUSPersonModel> {
	public static final String DAO_FILE_NAME = "PdfWebFormsUtil.OperativitaResidentiEstero";
	public static final String ACCESS_NAME = "controlliUSPerson";
	private static final String SERVICE_NAME = "/utilitydispo/Proxy/UtilityDispoWS"; // per ora è inventato attendiamo quello reale

	public ControlliUSPersonDaoAccess(ClientSessionContext csc, ControlliUSPersonModel dataModel) {
		super(csc, DAO_FILE_NAME, dataModel);
	}

	public ControlliUSPersonDaoAccess(ClientSessionContext csc, DAOObject daoObj, ControlliUSPersonModel dataModel) {
		super(csc, daoObj, dataModel);
	}

	public ControlliUSPersonDaoAccess(ClientSessionContext csc, ControlliUSPersonModel dataModel, PdfDataModel pdfData,
			ExecutorClient callbackableObject) {
		super(csc, DAO_FILE_NAME, dataModel, pdfData, callbackableObject);
	}

	public ControlliUSPersonDaoAccess(ClientSessionContext csc, DAOObject daoObj, ControlliUSPersonModel dataModel,
			PdfDataModel pdfData, ExecutorClient callbackableObject) {
		super(csc, daoObj, dataModel, pdfData, callbackableObject);
	}

	@Override
	protected void execute() throws DAOException {
		DAOOSBResultModel wsRes;
		wsRes = getDaoObj().executeOSBAccess(ACCESS_NAME, getDataModel());
		if (wsRes.getWsCallData().getStatus() == XmlServiceCallData.STATUS_SERVICE_DISABLED) {
			throw new DAOException("Si sono verificati problemi tecnici: Servizio " + SERVICE_NAME + " disabilitato");
		} else if (wsRes.getWsCallData().getStatus() != XmlServiceCallData.STATUS_OK) {
			throw new DAOException("Si sono verificati problemi tecnici: Errore di comunicazione con il sistema remoto "
					+ SERVICE_NAME);
		} else if (getDataModel().getResultCode().intValue() != 0) {
			throw new DAOException("Si sono verificati problemi tecnici: Errore di elaborazione del sistema remoto "
					+ SERVICE_NAME + " [" + getDataModel().getResultCode().toString() + "]" + " ["
					+ getDataModel().getResultMessage().toString() + "]");
		}
	}
}
