package prgm.pdfwebformsutil.drivers.autocompletion.toponimo;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Logger;

import prgm.pdfwebformsutil.drivers.dao.AbstractSearchDaoAccess;

public class ToponimoAutocompleteDao extends AbstractSearchDaoAccess<ToponimoAutocompleteModel> {
	public static final String DAO_FILE_NAME = "PdfWebFormsUtil.Toponimo";
	private static final String ACCESS_NAME = "toponimoAccess";

	public ToponimoAutocompleteDao(ClientSessionContext csc) {
		super(csc, DAO_FILE_NAME);
	}

	public ToponimoAutocompleteDao(ClientSessionContext csc, DAOObject daoObj) {
		super(csc, daoObj);
	}

	@Override
	public ListType findList(ToponimoAutocompleteModel dataModel) throws DAOException {
		return getDaoObj().executeQueryAccess(ACCESS_NAME, dataModel).getResult();
	}

	@Override
	public ToponimoAutocompleteModel findFirst(ToponimoAutocompleteModel dataModel) throws DAOException {
		ListType items = findList(dataModel);
		return (items != null && items.size() > 0) ? (ToponimoAutocompleteModel) items.get(0) : null;
	}

	public static boolean isToponimoExists(ClientSessionContext csc, StringType toponimo) {
		if (toponimo == null || toponimo.isNull()) {
			return false;
		}

		ToponimoAutocompleteModel toponimoModel = new ToponimoAutocompleteModel();
		toponimoModel.setDescrizioneToponimo(toponimo);
		ToponimoAutocompleteDao dao = new ToponimoAutocompleteDao(csc);
		try {
			return dao.findList(toponimoModel).size() > 0;
		} catch (DAOException e) {
			Logger.getInstance().error(e);
		}
		return false;
	}
}
