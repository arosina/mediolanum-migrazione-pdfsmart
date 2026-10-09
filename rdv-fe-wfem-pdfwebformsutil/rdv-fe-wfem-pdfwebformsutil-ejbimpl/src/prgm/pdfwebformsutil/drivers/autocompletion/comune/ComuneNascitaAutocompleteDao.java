package prgm.pdfwebformsutil.drivers.autocompletion.comune;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ListType;

import prgm.pdfwebformsutil.drivers.dao.AbstractSearchDaoAccess;

public class ComuneNascitaAutocompleteDao extends AbstractSearchDaoAccess<ComuneNascitaAutocompleteModel> {
	public static final String DAO_FILE_NAME = "PdfWebFormsUtil.ComuneNascita";
	private static final String ACCESS_NAME = "comuneNascitaAccess";

	public ComuneNascitaAutocompleteDao(ClientSessionContext csc) {
		super(csc, DAO_FILE_NAME);
	}

	public ComuneNascitaAutocompleteDao(ClientSessionContext csc, DAOObject daoObj) {
		super(csc, daoObj);
	}

	@Override
	public ListType findList(ComuneNascitaAutocompleteModel dataModel) throws DAOException {
		return getDaoObj().executeQueryAccess(ACCESS_NAME, dataModel).getResult();
	}

	@Override
	public ComuneNascitaAutocompleteModel findFirst(ComuneNascitaAutocompleteModel dataModel) throws DAOException {
		ListType items = findList(dataModel);
		return (items != null && items.size() > 0) ? (ComuneNascitaAutocompleteModel) items.get(0) : null;
	}

	
}
