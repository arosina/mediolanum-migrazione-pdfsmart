package prgm.pdfwebformsutil.drivers.autocompletion.abicab;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Logger;

import prgm.pdfwebformsutil.drivers.dao.AbstractSearchDaoAccess;

public class AbiBancaAutocompleteDao extends AbstractSearchDaoAccess<AbiBancaAutocompleteModel> {
	public static final String DAO_FILE_NAME = "PdfWebFormsUtil.AbiCab";
	private static final String ACCESS_NAME = "abiBancaAccess";

	public AbiBancaAutocompleteDao(ClientSessionContext csc) {
		super(csc, DAO_FILE_NAME);
	}

	public AbiBancaAutocompleteDao(ClientSessionContext csc, DAOObject daoObj) {
		super(csc, daoObj);
	}

	@Override
	public ListType findList(AbiBancaAutocompleteModel dataModel) throws DAOException {
		return getDaoObj().executeQueryAccess(ACCESS_NAME, dataModel).getResult();
	}

	@Override
	public AbiBancaAutocompleteModel findFirst(AbiBancaAutocompleteModel dataModel) throws DAOException {
		ListType items = findList(dataModel);
		return (items != null && items.size() > 0) ? (AbiBancaAutocompleteModel) items.get(0) : null;
	}

	public static boolean isBancaExists(ClientSessionContext csc, StringType abi) {
		if (abi == null || abi.isNull()) {
			return false;
		}

		AbiBancaAutocompleteModel abiModel = new AbiBancaAutocompleteModel();
		abiModel.setInputAbi(abi);
		AbiBancaAutocompleteDao dao = new AbiBancaAutocompleteDao(csc);
		try {
			return dao.findList(abiModel).size() > 0;
		} catch (DAOException e) {
			Logger.getInstance().error(e);
		}
		return false;
	}
}
