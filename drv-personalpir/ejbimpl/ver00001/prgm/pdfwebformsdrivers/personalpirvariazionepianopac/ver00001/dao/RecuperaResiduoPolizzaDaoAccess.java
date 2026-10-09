package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.dao;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOOSBResultModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.util.XmlServiceCallData;

import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.model.ResiduoPolizzaModel;
import prgm.pdfwebformsutil.drivers.dao.AbstractCommandDaoAccess;
import prgm.pdfwebformsutil.drivers.threads.ExecutorClient;

public class RecuperaResiduoPolizzaDaoAccess extends AbstractCommandDaoAccess<ResiduoPolizzaModel> {
	private static final String DAO_FILE_NAME = "PdfWebFormsDrivers.PersonalPirVariazionePianoPac.ver00001.PersonalPirVariazionePianoPac";
	public static final String ACCESS_NAME = "recuperaResiduoPolizza";
	private static final String SERVICE_NAME = "/service/soggetto/posizioneinformativa/PianiAccumuloService/v1";

	public RecuperaResiduoPolizzaDaoAccess(ClientSessionContext csc, ResiduoPolizzaModel dataModel) {
		super(csc, DAO_FILE_NAME, dataModel);
	}

	public RecuperaResiduoPolizzaDaoAccess(ClientSessionContext csc, DAOObject daoObj, ResiduoPolizzaModel dataModel) {
		super(csc, daoObj, dataModel);
	}

	public RecuperaResiduoPolizzaDaoAccess(ClientSessionContext csc, ResiduoPolizzaModel dataModel, PdfDataModel pdfData,
			ExecutorClient callbackableObject) {
		super(csc, DAO_FILE_NAME, dataModel, pdfData, callbackableObject);
	}

	public RecuperaResiduoPolizzaDaoAccess(ClientSessionContext csc, DAOObject daoObj, ResiduoPolizzaModel dataModel,
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
					+ SERVICE_NAME + " [" + getDataModel().getResultCode().toString() + "]");
		}				
	}
}
