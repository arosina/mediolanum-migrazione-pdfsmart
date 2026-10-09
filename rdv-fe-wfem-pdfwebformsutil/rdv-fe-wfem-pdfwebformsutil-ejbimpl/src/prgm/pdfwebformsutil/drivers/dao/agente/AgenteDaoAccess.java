package prgm.pdfwebformsutil.drivers.dao.agente;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsutil.drivers.dao.AbstractSearchDaoAccess;

public class AgenteDaoAccess extends AbstractSearchDaoAccess<AgenteModel> {
	private static final String MSG_PROBLEMI_NEL_RECUPERO_DATI = "Problemi nel recupero dei dati del codiceAgente :%s";
	private static final String DAO_FILE_NAME = "PdfWebFormsUtil.Agente";
	private static final String ACCESS_NAME = "loadDatiAgente";
	private static final String COD_RUOLO_OS = "OS"; //Operatore di sede
	private static final String COD_RUOLO_SA = "SA"; //Selfy Assistant

	public AgenteDaoAccess(ClientSessionContext csc) {
		super(csc, DAO_FILE_NAME);
	}

	public AgenteDaoAccess(ClientSessionContext csc, DAOObject daoObj) {
		super(csc, daoObj);
	}

	@Override
	public ListType findList(AgenteModel dataModel) throws DAOException {
		if (dataModel.getCodAgente() == null || dataModel.getCodAgente().isNull()) {
			return new ListType(AgenteModel.class);
		}

		DAOQueryResultModel qRes = getDaoObj().executeQueryAccess(ACCESS_NAME, dataModel);
		if (qRes == null) {
			throw new DAOException(String.format(MSG_PROBLEMI_NEL_RECUPERO_DATI, dataModel.getCodAgente().toString()));
		}
		return qRes.getResult();
	}

	@Override
	public AgenteModel findFirst(AgenteModel dataModel) throws DAOException {
		ListType items = findList(dataModel);
		return (items != null && items.size() > 0) ? (AgenteModel) items.get(0) : null;
	}

	public static boolean isAgenteAbilitatoIsvap(ClientSessionContext csc, StringType codiceAgente, PdfDataModel pdfData) throws Exception {
		if (codiceAgente == null || codiceAgente.isNull()) {
			return false;
		}
		
		StringType codRuoloImpersonato = pdfData.getCodRuoloImpersonato();
		if (codRuoloImpersonato.equals(COD_RUOLO_OS) || codRuoloImpersonato.equals(COD_RUOLO_SA)) {
			return true;
		}

		AgenteDaoAccess daoAccess = new AgenteDaoAccess(csc);

		AgenteModel inputModel = new AgenteModel();
		inputModel.setCodAgente(new StringType(Tools.fillSx(codiceAgente.toString(), '0', 10)));

		AgenteModel agenteModel = null;
		try {
			agenteModel = daoAccess.findFirst(inputModel);
		} catch (DAOException e) {
			throw new Exception(e);
		}

		if (agenteModel != null) {
			DateType oggi = Tools.today();
			DateType dataAbilIsvap = agenteModel.getDataAbilitazioneIsvap();
			return dataAbilIsvap != null && !dataAbilIsvap.isNull() && dataAbilIsvap.compareTo(oggi) <= 0;
		} else {
			throw new Exception(String.format(MSG_PROBLEMI_NEL_RECUPERO_DATI, codiceAgente));
		}
	}
}
