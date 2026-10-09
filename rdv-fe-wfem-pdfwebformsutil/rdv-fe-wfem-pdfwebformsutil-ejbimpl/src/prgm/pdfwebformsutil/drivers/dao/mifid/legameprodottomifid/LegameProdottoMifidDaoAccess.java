package prgm.pdfwebformsutil.drivers.dao.mifid.legameprodottomifid;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebformsutil.drivers.dao.AbstractSearchDaoAccess;

public class LegameProdottoMifidDaoAccess extends AbstractSearchDaoAccess<LegameProdottoMifidModel> {
	public static final String DAO_FILE_NAME = "PdfWebFormsUtil.Mifid";
	private static final String ACCESS_NAME = "legameProdottoMifid";

	public LegameProdottoMifidDaoAccess(ClientSessionContext csc) {
		super(csc, DAO_FILE_NAME);
	}

	public LegameProdottoMifidDaoAccess(ClientSessionContext csc, DAOObject daoObj) {
		super(csc, daoObj);
	}

	@Override
	public ListType findList(LegameProdottoMifidModel dataModel) throws DAOException {
		return getDaoObj().executeQueryAccess(ACCESS_NAME, dataModel).getResult();
	}

	@Override
	public LegameProdottoMifidModel findFirst(LegameProdottoMifidModel dataModel) throws DAOException {
		ListType items = findList(dataModel);
		return (items != null && items.size() > 0) ? (LegameProdottoMifidModel) items.get(0) : null;
	}

	public StringType readCodSurrogatoProdotto(ClientSessionContext csc, LegameProdottoMifidModel dataModel)
			throws Exception {
		if (dataModel == null) {
			return null;
		}

		StringType result = null;

		try {
			LegameProdottoMifidModel resultModel = findFirst(dataModel);
			if (resultModel != null) {
				result = resultModel.getCodSurrogatoProdotto();
			} else {
				result = null;
			}
		} catch (DAOException e) {
			throw new Exception(e);
		}

		return result;
	}
}
