package prgm.pdfwebformsdrivers.postcompletioncewutility.internal;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.dao.DAOOSBResultModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQASResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.util.OSBCallData;
import com.atosorigin.wfem.util.QASCallData;

public class ServiceCaller {

	public static String callQASservice(ClientSessionContext csc, String xmlName, String serviceName,
			CommandDataModel inputDataModel) {
		try {
			DAOQASResultModel qasRes = new DAOObject(csc, xmlName).executeQASAccess(serviceName, inputDataModel);
			QASCallData cd = qasRes.getQasCallData();
			if (cd.getStatus() == OSBCallData.STATUS_SERVICE_DISABLED) {
				return "Servizio " + serviceName + " disabilitato";
			} else if (cd.getStatus() != OSBCallData.STATUS_OK) {
				return "Errore di comunicazione con il servizio " + serviceName + ": " + cd.getMessage();
			} else {
				return null;
			}
		} catch (DAOException daoe) {
			return "Eccezione nel richiamo al servizio " + serviceName + ": " + daoe.toString();
		}
	}
	
	public static String callOSBservice(ClientSessionContext csc, String xmlName, String serviceName,
			CommandDataModel inputDataModel) {
		try {
			
			DAOOSBResultModel osbRes = new DAOObject(csc, xmlName).executeOSBAccess(serviceName, inputDataModel);
			OSBCallData cd = osbRes.getWsCallData();
			if (cd.getStatus() == OSBCallData.STATUS_SERVICE_DISABLED) {
				return "Servizio " + serviceName + " disabilitato";
			} else if (cd.getStatus() != OSBCallData.STATUS_OK) {
				return "Errore di comunicazione con il servizio " + serviceName + ": " + cd.getMessage();
			} else {
				return null;
			}
		} catch (DAOException daoe) {
			return "Eccezione nel richiamo al servizio " + serviceName + ": " + daoe.toString();
		}
	}
}
