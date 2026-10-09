package prgm.pdfwebformsutil.drivers.dao.fatcaservices.fatcainformation;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOOSBResultModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.util.XmlServiceCallData;

import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsutil.drivers.dao.AbstractCommandDaoAccess;
import prgm.pdfwebformsutil.drivers.threads.ExecutorClient;
import prgm.pdfwebformsutil.drivers.util.Util;

public class FatcaInformationDaoAccess extends AbstractCommandDaoAccess<FatcaInformationModel> {
	private static final String DAO_FILE_NAME = "PdfWebFormsUtil.FATCAServices";
	private static final String ACCESS_NAME = "getFatcaInformation";
	private static final String SERVICE_NAME = "/arch-be-osb-fatca-prj/PS/ps_fatca";

	public FatcaInformationDaoAccess(ClientSessionContext csc, FatcaInformationModel dataModel) {
		super(csc, DAO_FILE_NAME, dataModel);
	}

	public FatcaInformationDaoAccess(ClientSessionContext csc, DAOObject daoObj, FatcaInformationModel dataModel) {
		super(csc, daoObj, dataModel);
	}

	public FatcaInformationDaoAccess(ClientSessionContext csc, FatcaInformationModel dataModel, PdfDataModel pdfData,
			ExecutorClient callbackableObject) {
		super(csc, DAO_FILE_NAME, dataModel, pdfData, callbackableObject);
	}

	public FatcaInformationDaoAccess(ClientSessionContext csc, DAOObject daoObj, FatcaInformationModel dataModel,
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
