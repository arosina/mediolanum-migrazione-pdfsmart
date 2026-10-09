package prgm.pdfwebformspolizzeutil;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.drivers.PdfBaseDriverUtil;
import prgm.pdfwebforms.drivers.io.PageEventInputData;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsutil.drivers.autocompletion.nazione.NazioneAutocompleteDao;
import prgm.pdfwebformsutil.drivers.autocompletion.toponimo.ToponimoAutocompleteDao;
import prgm.pdfwebformsutil.drivers.util.Util;

public class Utils {
	
	private Utils() {
		throw new IllegalStateException("Utility class");
	}

	public static String DAO_FILE_NAME = "PdfWebFormsPolizzeUtil.Beneficiari";

	public static StringType getFieldStringValue(PdfDataModel pdfDataModel, String fieldName) {
		return (StringType) pdfDataModel.read(fieldName);
	}

	public static String getFieldName(String baseFieldName, String suffisso) {
		return baseFieldName + suffisso;
	}

	public static Map<String, String> getMapFieldNameDbValue(String[] fieldPdfNames, String[] fieldDbNames) {
		HashMap<String, String> retval = new HashMap<String, String>();
		for (int i = 0; i < fieldPdfNames.length; i++) {
			retval.put(fieldPdfNames[i], fieldDbNames[i]);
		}
		return retval;
	}

	public static String[] getFieldNames(String[] baseFieldNames, String suffisso) {
		String[] retval = new String[baseFieldNames.length];
		for (int i = 0; i < baseFieldNames.length; i++) {
			retval[i] = baseFieldNames[i] + suffisso;
		}
		return retval;
	}

	public static boolean checkFormatoDescrizioneRelazione(StringType descrizioneRelazione) {
		boolean retval = true;
		if (!Pattern.matches("[a-zA-Z0-9אטילעש',.;\\s]+", descrizioneRelazione.toString())) {
			retval = false;
			return retval;
		}

		return retval;
	}

	public static boolean checkLunghezzaDescrizioneRelazione(StringType descrizioneRelazione) {
		boolean retval = true;
		if (descrizioneRelazione.toString().length() > 50) {
			retval = false;
			return retval;
		}

		return retval;
	}

	public static boolean isNumeric(String codiceFiscalePartitaIva)  {
		return Pattern.matches("^[0-9]+", codiceFiscalePartitaIva);
	}
	
	
	protected static void checkDatiRecapito(PdfDataModel pdfDataModel, String suffisso, String errorMessage) throws Exception {

		StringType prefissoTelefonoBeneficiario = (StringType) pdfDataModel.read(Constants.PREFISSO_TEL_BASE_FIELD_NAME_PDF + suffisso);
		StringType telefonoBeneficiario = (StringType) pdfDataModel.read(Constants.NUMERO_TEL_BASE_FIELD_NAME_PDF + suffisso);
		StringType emailBeneficiario = (StringType) pdfDataModel.read(Constants.EMAIL_BASE_FIELD_NAME_PDF + suffisso);

		if (prefissoTelefonoBeneficiario.isNull()) {
			prefissoTelefonoBeneficiario.addTypeError(errorMessage);
		}

		if (telefonoBeneficiario.isNull()) {
			telefonoBeneficiario.addTypeError(errorMessage);
		}

		if (!emailBeneficiario.isNull() && !Util.checkEmail(emailBeneficiario.toString())) {
			emailBeneficiario.addTypeError("Inserire un indirizzo e-mail valido");
		}
	}
	
	protected static void checkDatiIndirizzo(ClientSessionContext csc, PdfDataModel pdfDataModel, String suffisso)  {

		StringType toponimoIndirizzoResidenzaBeneficiario = (StringType) pdfDataModel.read(Constants.TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF + suffisso);
		StringType indirizzoResidenzaBeneficiario = (StringType) pdfDataModel.read(Constants.INDIRIZZO_BASE_FIELD_NAME_PDF + suffisso);
		StringType capComuneResidenzaBeneficiario = (StringType) pdfDataModel.read(Constants.CAP_COMUNE_BASE_FIELD_NAME_PDF + suffisso);
		StringType comuneResidenzaBeneficiario = (StringType) pdfDataModel.read(Constants.COMUNE_BASE_FIELD_NAME_PDF + suffisso);
		StringType provinciaComuneResidenzaBeneficiario = (StringType) pdfDataModel.read(Constants.PROVINCIA_COMUNE_BASE_FIELD_NAME_PDF + suffisso);
		StringType nazioneComuneResidenzaBeneficiario = (StringType) pdfDataModel.read(Constants.NAZIONE_COMUNE_BASE_FIELD_NAME_PDF + suffisso);

		if (indirizzoResidenzaBeneficiario.isNull()) {
			indirizzoResidenzaBeneficiario.addTypeError(Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO);
		} 

		if (comuneResidenzaBeneficiario.isNull()) {
			comuneResidenzaBeneficiario.addTypeError(Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO);
		}

		if (nazioneComuneResidenzaBeneficiario.isNull()) {
			nazioneComuneResidenzaBeneficiario.addTypeError(Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO);
		} else {
			if (!nazioneComuneResidenzaBeneficiario.isNull()
					&& !NazioneAutocompleteDao.isNazioneExists(csc, nazioneComuneResidenzaBeneficiario)) {
				nazioneComuneResidenzaBeneficiario.addTypeError("Nazione errata.");
			}
			
			if (capComuneResidenzaBeneficiario.isNull() 
					&& nazioneComuneResidenzaBeneficiario.equals(prgm.pdfwebformsutil.drivers.autocompletion.comune.ComuneAutocomplete.NAZIONEITALIA)) {
				capComuneResidenzaBeneficiario.addTypeError(Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO);
			} else if (!capComuneResidenzaBeneficiario.isNull() 
					&& !nazioneComuneResidenzaBeneficiario.equals(prgm.pdfwebformsutil.drivers.autocompletion.comune.ComuneAutocomplete.NAZIONEITALIA)) {
				capComuneResidenzaBeneficiario.addTypeError(Constants.MSG_ERRORE_VALORE_NON_AMMESSO);
			}

			if (provinciaComuneResidenzaBeneficiario.isNull() 
					&& nazioneComuneResidenzaBeneficiario.equals(prgm.pdfwebformsutil.drivers.autocompletion.comune.ComuneAutocomplete.NAZIONEITALIA)) {
				provinciaComuneResidenzaBeneficiario.addTypeError(Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO);
			} else if (!provinciaComuneResidenzaBeneficiario.isNull() 
					&& !nazioneComuneResidenzaBeneficiario.equals(prgm.pdfwebformsutil.drivers.autocompletion.comune.ComuneAutocomplete.NAZIONEITALIA)) {
				provinciaComuneResidenzaBeneficiario.addTypeError(Constants.MSG_ERRORE_VALORE_NON_AMMESSO);
			}

			if (toponimoIndirizzoResidenzaBeneficiario.isNull() 
					&& nazioneComuneResidenzaBeneficiario.equals(prgm.pdfwebformsutil.drivers.autocompletion.comune.ComuneAutocomplete.NAZIONEITALIA)) {
				toponimoIndirizzoResidenzaBeneficiario.addTypeError(Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO);
			} else if (!toponimoIndirizzoResidenzaBeneficiario.isNull() 
					&& !nazioneComuneResidenzaBeneficiario.equals(prgm.pdfwebformsutil.drivers.autocompletion.comune.ComuneAutocomplete.NAZIONEITALIA)) {
				toponimoIndirizzoResidenzaBeneficiario.addTypeError(Constants.MSG_ERRORE_VALORE_NON_AMMESSO);

			}

		}

		boolean doCheckIndirizzoResidenzaNazioneItalia = !nazioneComuneResidenzaBeneficiario.isNull() 
				&& !comuneResidenzaBeneficiario.isNull() 
				&& !provinciaComuneResidenzaBeneficiario.isNull()
				&& !capComuneResidenzaBeneficiario.isNull() 
				&& nazioneComuneResidenzaBeneficiario.equals(prgm.pdfwebformsutil.drivers.autocompletion.comune.ComuneAutocomplete.NAZIONEITALIA);

		if (doCheckIndirizzoResidenzaNazioneItalia) {
			PdfBaseDriverUtil pdfBaseDriverUtil = new PdfBaseDriverUtil();
			pdfBaseDriverUtil.ctrl_comune(csc,pdfDataModel, suffisso);

			if (!toponimoIndirizzoResidenzaBeneficiario.isNull() 
					&& !ToponimoAutocompleteDao.isToponimoExists(csc, toponimoIndirizzoResidenzaBeneficiario)) {
				toponimoIndirizzoResidenzaBeneficiario.addTypeError("Toponimo errato.");
			} 
		}

	}
	
	public static StringType decodeDatoAnagrafico(ClientSessionContext csc, String tipoDecod, StringType valore) throws Exception {
		try {
			StringType codice = new StringType();
			DAOObject dao = new DAOObject(csc, "PdfWebFormsPolizzeUtil.Beneficiari");
	    	MapCommandDataModel mapIn = new MapCommandDataModel();
			mapIn.addProperty("tipo", new StringType(tipoDecod));
			mapIn.addProperty("descrizione", valore);
			
			DAOQueryResultModel result = dao.executeQueryAccess("getCodiceFromValore", mapIn);
			if (result.getSingleResult() != null) {
				codice = (StringType)result.getSingleResult();
			}
			
			if (codice.isNull()) {
				throw new Exception ("Impossibile decodificare il tipo dato "+tipoDecod+" valore "+valore+"");
			}
			
			return codice;
			
		} catch (DAOException daoE) {
			throw new Exception("Si e' verificato un errore durante la decodifica del tipo dato "+tipoDecod+" [valore "+valore+"]: "+daoE.getMessage());
		}
	}
	
	public static StringType getCodiceAgente(PdfDataModel pdfData) {
		StringType codiceAgente;

		if (!pdfData.getCodAgeImpersonato().isNull()) { //FORZATURA per Specialista Protezione
			codiceAgente = pdfData.getCodAgeImpersonato();
		} else {
			codiceAgente = (StringType)pdfData.read("codiceAgente");
		}
		
		return codiceAgente;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void recuperaDatiLegaleRapprProcuratore(ClientSessionContext csc, PageEventInputData input) throws Exception {

		PdfDataModel pdfData = input.getPdfData();

		String[] eventArgs = input.getEventArgs().split(",");
		String prefissoBeneficiario = eventArgs[0];
		String indiceBeneficiario = eventArgs[1];

		String suffissoBeneficiario = prefissoBeneficiario + indiceBeneficiario;

		pdfData.write("cognomeLegaleRapprProcuratore"+suffissoBeneficiario, new StringType());
		pdfData.write("nomeLegaleRapprProcuratore"+suffissoBeneficiario,  new StringType());
		pdfData.write("codiceFiscaleLegaleRapprProcuratore"+suffissoBeneficiario,  new StringType());
		
		StringType codiceCliente = (StringType) pdfData.read("codiceCliente" + suffissoBeneficiario);
		if (codiceCliente.isNull()) {
			return;
		}
		
		boolean isPersonaFisica = ((StringType) pdfData.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF + suffissoBeneficiario)).equals("true");
		
		String nomeQuery = isPersonaFisica?"getDatiProcuratore":"getDatiLegaleRappresentante";
		try {
			DAOObject dao = new DAOObject(csc, DAO_FILE_NAME);
			MapCommandDataModel map = new MapCommandDataModel();
			map.addProperty("ndgCliente", pdfData.read(Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario));
			DAOQueryResultModel qres = dao.executeQueryAccess(nomeQuery, map);
			if (qres.getResult().size() > 0) {
				MapCommandDataModel legalereRappOProcuratore = (MapCommandDataModel)qres.getResult().get(0);
				pdfData.write("cognomeLegaleRapprProcuratore"+suffissoBeneficiario, legalereRappOProcuratore.readProperty("cognome"));
				pdfData.write("nomeLegaleRapprProcuratore"+suffissoBeneficiario, legalereRappOProcuratore.readProperty("nome"));
				pdfData.write("codiceFiscaleLegaleRapprProcuratore"+suffissoBeneficiario, legalereRappOProcuratore.readProperty("codiceFiscale"));
			}

		} catch (DAOException de) {
		}
	
	}

}
