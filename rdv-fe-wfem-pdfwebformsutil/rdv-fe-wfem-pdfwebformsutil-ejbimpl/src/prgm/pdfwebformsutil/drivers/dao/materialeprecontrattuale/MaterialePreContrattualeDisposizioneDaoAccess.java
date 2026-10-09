package prgm.pdfwebformsutil.drivers.dao.materialeprecontrattuale;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOOSBResultModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.util.XmlServiceCallData;

import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsutil.drivers.dao.AbstractCommandDaoAccess;
import prgm.pdfwebformsutil.drivers.materialeprecontrattuale.MaterialePreContrattualeModel;
import prgm.pdfwebformsutil.drivers.threads.ExecutorClient;

public class MaterialePreContrattualeDisposizioneDaoAccess extends AbstractCommandDaoAccess<MaterialePreContrattualeModel> {
	public static final String DAO_FILE_NAME = "PdfWebFormsUtil.MaterialeContrattuale";
	public static final String ACCESS_NAME = "getMaterialePrecontrattualeDisposizione";
	private static final String SERVICE_NAME = "/rdv-be-osb-ItaCarrelloWS/Proxy/PS_CarrelloWS";

	public MaterialePreContrattualeDisposizioneDaoAccess(ClientSessionContext csc, MaterialePreContrattualeModel dataModel) {
		super(csc, DAO_FILE_NAME, dataModel);
	}

	public MaterialePreContrattualeDisposizioneDaoAccess(ClientSessionContext csc, DAOObject daoObj, MaterialePreContrattualeModel dataModel) {
		super(csc, daoObj, dataModel);
	}

	public MaterialePreContrattualeDisposizioneDaoAccess(ClientSessionContext csc, MaterialePreContrattualeModel dataModel, PdfDataModel pdfData,
			ExecutorClient callbackableObject) {
		super(csc, DAO_FILE_NAME, dataModel, pdfData, callbackableObject);
	}

	public MaterialePreContrattualeDisposizioneDaoAccess(ClientSessionContext csc, DAOObject daoObj, MaterialePreContrattualeModel dataModel,
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
					+ SERVICE_NAME + " [" + getDataModel().getResultCode().toString() + "]" + " [" + getDataModel().getResultMessage().toString() + "]");
		}		
	}
}
