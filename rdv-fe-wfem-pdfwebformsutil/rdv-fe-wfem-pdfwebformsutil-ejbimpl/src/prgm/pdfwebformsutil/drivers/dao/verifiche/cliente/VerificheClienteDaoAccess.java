package prgm.pdfwebformsutil.drivers.dao.verifiche.cliente;

import java.util.List;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebformsutil.drivers.dao.AbstractDaoAccess;
import prgm.pdfwebformsutil.drivers.util.Util;

public class VerificheClienteDaoAccess extends AbstractDaoAccess {
	private static final String NDG_CLIENTE = "ndgCliente";
	public static final String DAO_FILE_NAME = "PdfWebFormsUtil.VerificheCliente";
	private static final String ACCESS_NAME_IS_CLI_COINT_PURO = "isAlmenoUnClienteCointestatarioPuro";
	private static final String ACCESS_NAME_IS_AGENTE = "isCodiceFiscaleAgente";

	public VerificheClienteDaoAccess(ClientSessionContext csc) {
		super(csc, DAO_FILE_NAME);
	}

	public VerificheClienteDaoAccess(ClientSessionContext csc, DAOObject daoObj) {
		super(csc, daoObj);
	}

	public boolean isAlmenoUnClienteCointestatarioPuro(ClientSessionContext csc, List<String> listaCodiciClienti)
			throws Exception {
		StringBuilder elencoCodici = new StringBuilder();
		for (String codCliente : listaCodiciClienti) {
			if (elencoCodici.length() > 0) {
				elencoCodici.append(',');
			}
			elencoCodici.append(String.format("'%s'", Util.lZeroPad(codCliente, 11)));
		}

		VerificheClienteModel model = new VerificheClienteModel();
		model.setInputElencoCodiciCliente(new StringType(elencoCodici.toString()));
		return isAlmenoUnClienteCointestatarioPuro(csc, model);
	}

	public boolean isAlmenoUnClienteCointestatarioPuro(ClientSessionContext csc, VerificheClienteModel dataModel)
			throws Exception {
		boolean result = false;

		if (dataModel == null) {
			return result;
		}

		MapCommandDataModel inputModel = new MapCommandDataModel();
		inputModel.addProperty("inputElencoCodiciCliente", dataModel.getInputElencoCodiciCliente());
		inputModel.addProperty("inputCodiceCliente", dataModel.getInputCodiceCliente());

		try {
			DAOQueryResultModel qrResult = getDaoObj().executeQueryAccess(ACCESS_NAME_IS_CLI_COINT_PURO, dataModel);

			if (qrResult != null) {
				result = ((BooleanType) qrResult.getSingleResult()).booleanValue();
			}
		} catch (DAOException daoe) {
			String errorMsg = "PdfDriver - Eccezione DAO nel verificare se i sottoscrittori sono puri: " + daoe;
			throw new Exception(errorMsg);
		} catch (Exception e) {
			String errorMsg = "PdfDriver - Eccezione nel verificare se i sottoscrittori sono puri: " + e;
			throw new Exception(errorMsg);
		}
		return result;
	}

	public boolean isAgente(ClientSessionContext csc, VerificheClienteModel dataModel) throws Exception {
		boolean result = false;

		if (dataModel == null) {
			return result;
		}

		MapCommandDataModel inputModel = new MapCommandDataModel();
		inputModel.addProperty("inputCodiceFiscale", dataModel.getInputCodiceFiscale());

		try {
			DAOQueryResultModel qrResult = getDaoObj().executeQueryAccess(ACCESS_NAME_IS_AGENTE, dataModel);

			if (qrResult != null) {
				result = ((BooleanType) qrResult.getSingleResult()).booleanValue();
			}
		} catch (DAOException daoe) {
			String errorMsg = "PdfDriver - Eccezione DAO nel verificare se il codice fiscale è di un agente: " + daoe;
			throw new Exception(errorMsg);
		} catch (Exception e) {
			String errorMsg = "PdfDriver - Eccezione nel verificare se il codice fiscale è di un agente: " + e;
			throw new Exception(errorMsg);
		}
		return result;
	}
	
	
	public boolean isResidenzaFisicaEstera(StringType ndgCliente, StringType codiceProspect) throws Exception {
		try {
			MapCommandDataModel inputModel = new MapCommandDataModel();
			String query = null;
			if (codiceProspect!= null && !codiceProspect.isNull()) {//cliente prospect
				query = "isResidenzaFisicaEsteraProspect";
				inputModel.addProperty("codProspect",  codiceProspect);
			} else {
				query = "isResidenzaFisicaEstera";
				inputModel.addProperty(VerificheClienteDaoAccess.NDG_CLIENTE,  new StringType(Tools.fillSx(ndgCliente.toString(), '0', 11)));
			}
			DAOQueryResultModel result = getDaoObj().executeQueryAccess(query, inputModel);
			return ((BooleanType) result.getSingleResult()).booleanValue();		
		} catch (DAOException de) {
			throw new Exception("Errore durante la verifica della residenza fisica del cliente "+ndgCliente+".");
		}
	}
	
	public StringType residenzaFiscalePredominante(StringType ndgCliente) throws Exception {
		try {
			MapCommandDataModel inputModel = new MapCommandDataModel();
			inputModel.addProperty(VerificheClienteDaoAccess.NDG_CLIENTE, new StringType(Tools.fillSx(ndgCliente.toString(), '0', 11)));
			DAOQueryResultModel result = getDaoObj().executeQueryAccess("residenzaFiscalePredominante", inputModel);
			StringType singleResult = (StringType)(result.getSingleResult());
			return singleResult == null ? new StringType("") : singleResult;	
		} catch (DAOException de) {
			throw new Exception("Errore durante la verifica della residenza fiscale predominante del cliente "+ndgCliente+".");
		}
	}
		
	public boolean isResidenzaInPaeseARischio(StringType ndgCliente, StringType codiceProspect) throws Exception {
		try {
			MapCommandDataModel inputModel = new MapCommandDataModel();
			String query = null;
			
			if (codiceProspect != null && !codiceProspect.isNull()) {
				// cliente prospect
				query = "gradoRischioResidenzaFisicaProspect";
				inputModel.addProperty("codProspect", codiceProspect);
			} else {
				query = "gradoRischioResidenzaFisica";
				inputModel.addProperty(VerificheClienteDaoAccess.NDG_CLIENTE, new StringType(Tools.fillSx(ndgCliente.toString(), '0', 11)));
			}
			DAOQueryResultModel result = getDaoObj().executeQueryAccess(query, inputModel);
			if (result.getSingleResult() != null && !result.getSingleResult().isNull())
				return ((IntegerType) result.getSingleResult()).intValue() == 1;
			else {
				return false; // caso residenza italiana o residenza in paese non a rischio
			}
		} catch (DAOException de) {
			throw new Exception(
					"Errore durante la verifica della residenza fisica del cliente " + ndgCliente + ".");
		}
		
	}	
}
