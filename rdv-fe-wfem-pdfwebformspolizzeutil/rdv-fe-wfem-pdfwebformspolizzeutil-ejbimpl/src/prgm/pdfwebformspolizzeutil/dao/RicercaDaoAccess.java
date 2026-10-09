package prgm.pdfwebformspolizzeutil.dao;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ListType;

import prgm.pdfwebformspolizzeutil.popup.RicercaPopupModel;
import prgm.pdfwebformsutil.drivers.dao.AbstractSearchDaoAccess;

public class RicercaDaoAccess
		extends AbstractSearchDaoAccess<RicercaPopupModel> {
	public static final String DAO_FILE_NAME = "PdfWebFormsPolizzeUtil.RicercaPopup";
	public static final String ACCESS_NAME_RICERCA_POPUP = "ricercaPopup";	
	public static String PARAMETRI_NON_VALIDI = "Parametri di input non validi per la ricerca";

	public RicercaDaoAccess(ClientSessionContext csc) {
		super(csc, DAO_FILE_NAME);
	}

	public RicercaDaoAccess(ClientSessionContext csc, DAOObject daoObj) {
		super(csc, daoObj);
	}

	@Override
	public ListType findList(RicercaPopupModel dataModel) throws DAOException {
		if (dataModel.getCodiceCliente().isNull() && dataModel.getCodiceFiscale().isNull() && dataModel.getCognome().isNull() && dataModel.getCodiceAgente().isNull()) {
			throw new DAOException(PARAMETRI_NON_VALIDI);
		}

		DAOQueryResultModel qRes = getDaoObj().executeQueryAccess(ACCESS_NAME_RICERCA_POPUP, dataModel);
		return qRes.getResult();
	}		

}
