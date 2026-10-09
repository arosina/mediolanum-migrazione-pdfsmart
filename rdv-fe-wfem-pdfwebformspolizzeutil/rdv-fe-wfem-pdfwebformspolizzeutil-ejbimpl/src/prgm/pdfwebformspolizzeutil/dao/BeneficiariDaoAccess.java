package prgm.pdfwebformspolizzeutil.dao;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebformspolizzeutil.model.BeneficiarioModel;
import prgm.pdfwebformsutil.drivers.dao.AbstractSearchDaoAccess;

public class BeneficiariDaoAccess
		extends AbstractSearchDaoAccess<BeneficiarioModel> {
	public static final String DAO_FILE_NAME = "PdfWebFormsPolizzeUtil.Beneficiari";
	private static final String ACCESS_NAME_RICERCA_BENEFICIARI = "ricercaBeneficiari";
	private static final String ACCESS_NAME_LOAD_BENEFICIARIO = "ricercaBeneficiario";
	private static final String ACCESS_NAME_CLIENTE_IS_SOCIETA_FIDUCIARIA = "clienteIsSocietaFiduciaria";
	private String PARAMETRI_NON_VALIDI = "Parametri di input non validi per la ricerca";

	public BeneficiariDaoAccess(ClientSessionContext csc) {
		super(csc, DAO_FILE_NAME);
	}

	public BeneficiariDaoAccess(ClientSessionContext csc, DAOObject daoObj) {
		super(csc, daoObj);
	}

	@Override
	public ListType findList(BeneficiarioModel dataModel) throws DAOException {
		if (dataModel.getCodiceCliente() == null && dataModel.getCodiceFiscale().isNull() && dataModel.getCognome().isNull() && dataModel.getCodiceAgente().isNull()) {
			throw new DAOException(PARAMETRI_NON_VALIDI);
		}

		DAOQueryResultModel qRes = getDaoObj().executeQueryAccess(ACCESS_NAME_RICERCA_BENEFICIARI, dataModel);
		return qRes.getResult();
	}
	
	
	public BeneficiarioModel findBeneficiario(BeneficiarioModel dataModel) throws DAOException {
		if (dataModel.getCodiceCliente() == null || dataModel.getCodiceCliente().isNull()) {
			throw new DAOException(PARAMETRI_NON_VALIDI);
		}
		
		DAOQueryResultModel qRes = getDaoObj().executeQueryAccess(ACCESS_NAME_LOAD_BENEFICIARIO, dataModel);
		if (qRes.getResult().size() > 1) {
			dataModel.getCodiceCliente().addTypeError("Corrispondenza non univoca per codice cliente " + dataModel.getCodiceCliente());
			return dataModel;
		}
		return (BeneficiarioModel) qRes.getResult().get(0);
	}
	
	
	public boolean isClienteGiaCensito(BeneficiarioModel dataModel) throws DAOException {
		if (dataModel.getCodiceFiscale() == null || dataModel.getCodiceFiscale().isNull()) {
			throw new DAOException(PARAMETRI_NON_VALIDI);
		}
		
		DAOQueryResultModel qRes = getDaoObj().executeQueryAccess(ACCESS_NAME_RICERCA_BENEFICIARI, dataModel);
		return qRes.getResult().size() != 0;
	}
	
	
	public boolean clienteIsSocietaFiduciaria(StringType codiceCliente) throws DAOException {
		if (codiceCliente == null || codiceCliente.isNull()) {
			throw new DAOException(PARAMETRI_NON_VALIDI);
		}
		
		MapCommandDataModel input = new MapCommandDataModel();
		input.addProperty("codiceCliente", codiceCliente);

		DAOQueryResultModel qRes = getDaoObj().executeQueryAccess(ACCESS_NAME_CLIENTE_IS_SOCIETA_FIDUCIARIA, input);
		return ((BooleanType)qRes.getSingleResult()).booleanValue();
	}
	 	
}
