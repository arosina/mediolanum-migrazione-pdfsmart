package prgm.pdfwebformsutil.drivers.dao.medsecurityservices.nuovasessionetecnica;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOOSBResultModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.util.XmlServiceCallData;

import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsutil.drivers.dao.AbstractCommandDaoAccess;
import prgm.pdfwebformsutil.drivers.threads.ExecutorClient;
import prgm.pdfwebformsutil.drivers.util.Util;

public class NuovaSessioneTecnicaDaoAccess extends AbstractCommandDaoAccess<NuovaSessioneTecnicaModel> {
	private static final String DAO_FILE_NAME = "PdfWebFormsUtil.MedSecurityServices";
	private static final String ACCESS_NAME = "NuovaSessioneTecnica";
	private static final String SERVICE_NAME = "/med-be-osb-security-services-prj/proxy/med-be-token-mngr";

	public NuovaSessioneTecnicaDaoAccess(ClientSessionContext csc, NuovaSessioneTecnicaModel dataModel) {
		super(csc, DAO_FILE_NAME, dataModel);
	}

	public NuovaSessioneTecnicaDaoAccess(ClientSessionContext csc, DAOObject daoObj,
			NuovaSessioneTecnicaModel dataModel) {
		super(csc, daoObj, dataModel);
	}

	public NuovaSessioneTecnicaDaoAccess(ClientSessionContext csc, NuovaSessioneTecnicaModel dataModel,
			PdfDataModel pdfData, ExecutorClient callbackableObject) {
		super(csc, DAO_FILE_NAME, dataModel, pdfData, callbackableObject);
	}

	public NuovaSessioneTecnicaDaoAccess(ClientSessionContext csc, DAOObject daoObj,
			NuovaSessioneTecnicaModel dataModel, PdfDataModel pdfData, ExecutorClient callbackableObject) {
		super(csc, daoObj, dataModel, pdfData, callbackableObject);
	}

	@Override
	protected void execute() throws DAOException {		 
		for(int numRetray = 0; numRetray < 3; numRetray++) {
			DAOOSBResultModel wsRes = getDaoObj().executeOSBAccess(ACCESS_NAME, getDataModel());
			if (wsRes.getWsCallData().getStatus() == XmlServiceCallData.STATUS_SERVICE_DISABLED) {
				throw new DAOException("Si sono verificati problemi tecnici: Servizio " + SERVICE_NAME + " disabilitato");
			} else if (wsRes.getWsCallData().getStatus() != XmlServiceCallData.STATUS_OK) {
				throw new DAOException("Si sono verificati problemi tecnici: Errore di comunicazione con il sistema remoto "
						+ SERVICE_NAME);
			} else if (getDataModel().getResultCode().intValue() != 0) {
				throw new DAOException(Util.concat("Si sono verificati problemi tecnici: Errore di elaborazione del sistema remoto "
						, SERVICE_NAME , " [" , getDataModel().getResultCode().toString() , "]"));
			}
			if (!getDataModel().getWebCookie().isNull()) {
				break;
			}			
		}	
		if (getDataModel().getWebCookie().isNull()) {
			throw new DAOException("Si sono verificati problemi tecnici: Errore sessione tecnica non valida "
					+ SERVICE_NAME);			
		}
	}
}
