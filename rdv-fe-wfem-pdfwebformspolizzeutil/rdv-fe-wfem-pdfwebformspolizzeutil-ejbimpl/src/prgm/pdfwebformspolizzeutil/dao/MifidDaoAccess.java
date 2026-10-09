package prgm.pdfwebformspolizzeutil.dao;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebformsutil.drivers.dao.AbstractDaoAccess;

public class MifidDaoAccess extends AbstractDaoAccess {

	public static final String DAO_FILE_NAME = "PdfWebFormsPolizzeUtil.Mifid";
	private static final String ACCESS_NAME_RECUPERA_TARIFFA = "recuperaDescrizioneTariffa";

	public MifidDaoAccess(ClientSessionContext csc) {
		super(csc, DAO_FILE_NAME);
	}

	public MifidDaoAccess(ClientSessionContext csc, DAOObject daoObj) {
		super(csc, daoObj);
	}

	// #108838
	public StringType recuperaDescrTariffaReportAdeguatezza(String tariffa) throws Exception {
		MapCommandDataModel input = new MapCommandDataModel();
		input.addProperty("tariffa", new StringType(tariffa));

		DAOQueryResultModel result;
		try {
			result = getDaoObj().executeQueryAccess(ACCESS_NAME_RECUPERA_TARIFFA, input);
			return ((StringType) result.getSingleResult());
		} catch (DAOException daoe) {
			String errorMsg = "PdfWebFormsPolizzeUtil.recuperaDescrTariffaReportAdeguatezza - Eccezione DAO nel recuperare la descrizione della tariffa: "+daoe;
			throw new Exception(errorMsg);		
		}
		
		

	}

}
