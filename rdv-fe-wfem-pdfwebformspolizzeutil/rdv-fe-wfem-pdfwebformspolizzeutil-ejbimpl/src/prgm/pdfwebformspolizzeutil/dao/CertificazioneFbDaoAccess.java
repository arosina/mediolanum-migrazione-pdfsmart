package prgm.pdfwebformspolizzeutil.dao;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebformspolizzeutil.model.CertificazioneFbModel;
import prgm.pdfwebformsutil.drivers.dao.AbstractDaoAccess;

public class CertificazioneFbDaoAccess extends AbstractDaoAccess {

	public static final String DAO_FILE_NAME = "PdfWebFormsPolizzeUtil.CertificazioneFb";
	private static final String ACCESS_NAME_RECUPERA_ESITO_CERT = "recuperaEsitoCertificazione";

	public CertificazioneFbDaoAccess(ClientSessionContext csc) {
		super(csc, DAO_FILE_NAME);
	}

	public CertificazioneFbDaoAccess(ClientSessionContext csc, DAOObject daoObj) {
		super(csc, daoObj);
	}

	public CertificazioneFbModel recuperaEsitoCertificazione(StringType codFb, StringType certificazione) throws Exception {
						
		if(codFb == null || certificazione==null) {
			return null;
		}
		
		CertificazioneFbModel certificazioneFbModel = new CertificazioneFbModel();
		certificazioneFbModel.setCodAgente(codFb);
		certificazioneFbModel.setCertificazione(certificazione);
		
		DAOQueryResultModel result;
		try {
			result = getDaoObj().executeQueryAccess(ACCESS_NAME_RECUPERA_ESITO_CERT, certificazioneFbModel);
			return result.getResult().size()>0 ? (CertificazioneFbModel)result.getResult().get(0) : null;
		} catch (DAOException daoe) {
			String errorMsg = "PdfWebFormsPolizzeUtil.CertificazioneFb - Eccezione DAO nel recuperaEsitoCertificazione: "+daoe;
			throw new Exception(errorMsg);		
		}	
		
	}

}
