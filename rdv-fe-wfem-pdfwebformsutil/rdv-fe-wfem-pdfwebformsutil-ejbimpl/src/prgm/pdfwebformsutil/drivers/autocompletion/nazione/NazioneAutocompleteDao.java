package prgm.pdfwebformsutil.drivers.autocompletion.nazione;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Logger;

import prgm.pdfwebformsutil.drivers.dao.AbstractSearchDaoAccess;

public class NazioneAutocompleteDao extends AbstractSearchDaoAccess<NazioneAutocompleteModel> {
	public static final String DAO_FILE_NAME = "PdfWebFormsUtil.Nazione";
	private static final String ACCESS_NAME = "nazioneAccess";

	public NazioneAutocompleteDao(ClientSessionContext csc) {
		super(csc, DAO_FILE_NAME);
	}

	public NazioneAutocompleteDao(ClientSessionContext csc, DAOObject daoObj) {
		super(csc, daoObj);
	}

	@Override
	public ListType findList(NazioneAutocompleteModel dataModel) throws DAOException {
		return getDaoObj().executeQueryAccess(ACCESS_NAME, dataModel).getResult();
	}

	@Override
	public NazioneAutocompleteModel findFirst(NazioneAutocompleteModel dataModel) throws DAOException {
		ListType items = findList(dataModel);
		return (items != null && items.size() > 0) ? (NazioneAutocompleteModel) items.get(0) : null;
	}

	public static boolean isNazioneExists(ClientSessionContext csc, StringType nazione) {
		if (nazione == null || nazione.isNull()) {
			return false;
		}

		NazioneAutocompleteModel nazioneModel = new NazioneAutocompleteModel();
		nazioneModel.setNazione(nazione);
		NazioneAutocompleteDao dao = new NazioneAutocompleteDao(csc);
		try {
			return dao.findList(nazioneModel).size() > 0;
		} catch (DAOException e) {
			Logger.getInstance().error(e);
		}
		return false;
	}
}
