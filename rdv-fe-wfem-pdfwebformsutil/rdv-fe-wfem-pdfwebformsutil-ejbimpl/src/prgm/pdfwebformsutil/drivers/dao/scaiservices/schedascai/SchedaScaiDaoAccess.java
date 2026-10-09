package prgm.pdfwebformsutil.drivers.dao.scaiservices.schedascai;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOOSBResultModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.util.XmlServiceCallData;

import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsutil.drivers.dao.AbstractCommandDaoAccess;
import prgm.pdfwebformsutil.drivers.threads.ExecutorClient;
import prgm.pdfwebformsutil.drivers.util.Util;

public class SchedaScaiDaoAccess extends AbstractCommandDaoAccess<SchedaScaiModel> {
	private static final String DAO_FILE_NAME = "PdfWebFormsUtil.SchedaSCAIServices";
	private static final String ACCESS_NAME = "recuperaDatiSchedaScaiCliente";
	private static final String SERVICE_NAME = "/soggetto-anagrafica-be-osb-informativedatiadv-prj/proxy/PS_Anagrafica";

	public SchedaScaiDaoAccess(ClientSessionContext csc, SchedaScaiModel dataModel) {
		super(csc, DAO_FILE_NAME, dataModel);
	}

	public SchedaScaiDaoAccess(ClientSessionContext csc, DAOObject daoObj, SchedaScaiModel dataModel) {
		super(csc, daoObj, dataModel);
	}

	public SchedaScaiDaoAccess(ClientSessionContext csc, SchedaScaiModel dataModel, PdfDataModel pdfData,
			ExecutorClient callbackableObject) {
		super(csc, DAO_FILE_NAME, dataModel, pdfData, callbackableObject);
	}

	public SchedaScaiDaoAccess(ClientSessionContext csc, DAOObject daoObj, SchedaScaiModel dataModel,
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
			throw new DAOException(Util.concat("Si sono verificati problemi tecnici: Errore di elaborazione del sistema remoto "
					, SERVICE_NAME , " [" , getDataModel().getResultCode().toString() , "]"));
		}
	}
}
