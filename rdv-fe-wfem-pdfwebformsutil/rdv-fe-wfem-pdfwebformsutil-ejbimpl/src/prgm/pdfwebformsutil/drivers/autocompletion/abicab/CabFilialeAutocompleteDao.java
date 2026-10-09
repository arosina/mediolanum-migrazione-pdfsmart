package prgm.pdfwebformsutil.drivers.autocompletion.abicab;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Logger;

import prgm.pdfwebformsutil.drivers.dao.AbstractSearchDaoAccess;

public class CabFilialeAutocompleteDao extends AbstractSearchDaoAccess<CabFilialeAutocompleteModel> {
	public static final String DAO_FILE_NAME = "PdfWebFormsUtil.AbiCab";
	private static final String ACCESS_NAME = "cabFilialeAccess";

	public CabFilialeAutocompleteDao(ClientSessionContext csc) {
		super(csc, DAO_FILE_NAME);
	}

	public CabFilialeAutocompleteDao(ClientSessionContext csc, DAOObject daoObj) {
		super(csc, daoObj);
	}

	@Override
	public ListType findList(CabFilialeAutocompleteModel dataModel) throws DAOException {
		ListType result = new ListType();
		if (!dataModel.getInputAbi().isNull()) {
			result = getDaoObj().executeQueryAccess(ACCESS_NAME, dataModel).getResult();
		}
		return result;
	}

	@Override
	public CabFilialeAutocompleteModel findFirst(CabFilialeAutocompleteModel dataModel) throws DAOException {
		ListType items = findList(dataModel);
		return (items != null && items.size() > 0) ? (CabFilialeAutocompleteModel) items.get(0) : null;
	}

	public static boolean isFilialeExists(ClientSessionContext csc, StringType abi, StringType cab) {
		if (abi == null || abi.isNull() || cab == null || cab.isNull()) {
			return false;
		}

		CabFilialeAutocompleteModel cabModel = new CabFilialeAutocompleteModel();
		cabModel.setInputAbi(abi);
		cabModel.setInputCab(cab);
		CabFilialeAutocompleteDao dao = new CabFilialeAutocompleteDao(csc);
		try {
			return dao.findList(cabModel).size() > 0;
		} catch (DAOException e) {
			Logger.getInstance().error(e);
		}
		return false;
	}
}
