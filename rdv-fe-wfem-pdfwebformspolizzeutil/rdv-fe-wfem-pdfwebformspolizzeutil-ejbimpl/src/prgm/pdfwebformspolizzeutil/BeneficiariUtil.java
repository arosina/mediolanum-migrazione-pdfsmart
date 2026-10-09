package prgm.pdfwebformspolizzeutil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.drivers.AbstractEventInputData;
import prgm.pdfwebforms.drivers.PdfBaseDriver;
import prgm.pdfwebforms.drivers.io.PageEventInputData;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.InrWriterService;
import prgm.pdfwebformspolizzeutil.dao.BeneficiariDaoAccess;
import prgm.pdfwebformspolizzeutil.model.BeneficiarioModel;
import prgm.pdfwebformspolizzeutil.pagedriver.Beneficiari;
import prgm.pdfwebformsutil.drivers.util.CodiceFiscaleUtils;
import prgm.pdfwebformsutil.drivers.util.Util;

public class BeneficiariUtil {

	private static final String FALSE = "false";
	
	protected static String[] FIELDSNAME_AS_STRING_PERSONA_FISICA = { 	
			Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF,
			Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF,
			Constants.NOME_BASE_FIELD_NAME_PDF,
			Constants.COGNOME_BASE_FIELD_NAME_PDF,
			Constants.CODICE_FISCALE_BASE_FIELD_NAME_PDF,
			Constants.COMUNE_NASCITA_BASE_FIELD_NAME_PDF,
			Constants.PROVINCIA_COMUNE_NASCITA_BASE_FIELD_NAME_PDF,
			Constants.NAZIONE_COMUNE_NASCITA_BASE_FIELD_NAME_PDF,
			Constants.SESSO_BASE_FIELD_NAME_PDF,
			Constants.TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF,
			Constants.INDIRIZZO_BASE_FIELD_NAME_PDF,
			Constants.NUMERO_CIVICO_INDIRIZZO_BASE_FIELD_NAME_PDF,
			Constants.CAP_COMUNE_BASE_FIELD_NAME_PDF,
			Constants.COMUNE_BASE_FIELD_NAME_PDF,
			Constants.PROVINCIA_COMUNE_BASE_FIELD_NAME_PDF,
			Constants.NAZIONE_COMUNE_BASE_FIELD_NAME_PDF,
			Constants.PREFISSO_INTERNAZIONALE_TEL_BASE_FIELD_NAME_PDF,
			Constants.PREFISSO_TEL_BASE_FIELD_NAME_PDF,
			Constants.NUMERO_TEL_BASE_FIELD_NAME_PDF,
			Constants.TIPO_RECAPITO_TEL_BASE_FIELD_NAME_PDF,
			Constants.EMAIL_BASE_FIELD_NAME_PDF,
			Constants.TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF,
			Constants.DESCR_TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF,
			Constants.TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF,
			Constants.DESCR_TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF,
			Constants.IS_PEP_BASE_FIELD_NAME_PDF,
			Constants.INDICE_MOTIVAZIONE_PEP_BASE_FIELD_NAME_PDF
	};
	
	protected static String[] FIELDSNAME_AS_STRING_PERSONA_GIURIDICA = { 	
			Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF,
			Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF,
			Constants.TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF,
			Constants.INDIRIZZO_BASE_FIELD_NAME_PDF,
			Constants.NUMERO_CIVICO_INDIRIZZO_BASE_FIELD_NAME_PDF,
			Constants.CAP_COMUNE_BASE_FIELD_NAME_PDF,
			Constants.COMUNE_BASE_FIELD_NAME_PDF,
			Constants.PROVINCIA_COMUNE_BASE_FIELD_NAME_PDF,
			Constants.NAZIONE_COMUNE_BASE_FIELD_NAME_PDF,
			Constants.PREFISSO_INTERNAZIONALE_TEL_BASE_FIELD_NAME_PDF,
			Constants.PREFISSO_TEL_BASE_FIELD_NAME_PDF,
			Constants.NUMERO_TEL_BASE_FIELD_NAME_PDF,
			Constants.TIPO_RECAPITO_TEL_BASE_FIELD_NAME_PDF,
			Constants.EMAIL_BASE_FIELD_NAME_PDF,
			Constants.RAGIONE_SOCIALE_BASE_FIELD_NAME_PDF,
			Constants.CODICE_FISCALE_PIVA_BASE_FIELD_NAME_PDF,
			Constants.NUMERO_ISCRIZIONE_CCIAA_BASE_FIELD_NAME_PDF,
			Constants.PROVINCIA_ISCRIZIONE_CCIAA_BASE_FIELD_NAME_PDF,
	};
	
	protected static String[] FIELDSNAME_AS_STRING_TITOLARE = { 	
			Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF,
			Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF,
			Constants.NOME_BASE_FIELD_NAME_PDF,
			Constants.COGNOME_BASE_FIELD_NAME_PDF,
			Constants.CODICE_FISCALE_BASE_FIELD_NAME_PDF,
			Constants.COMUNE_NASCITA_BASE_FIELD_NAME_PDF,
			Constants.PROVINCIA_COMUNE_NASCITA_BASE_FIELD_NAME_PDF,
			Constants.NAZIONE_COMUNE_NASCITA_BASE_FIELD_NAME_PDF,
			Constants.SESSO_BASE_FIELD_NAME_PDF,
			Constants.TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF,
			Constants.INDIRIZZO_BASE_FIELD_NAME_PDF,
			Constants.NUMERO_CIVICO_INDIRIZZO_BASE_FIELD_NAME_PDF,
			Constants.CAP_COMUNE_BASE_FIELD_NAME_PDF,
			Constants.COMUNE_BASE_FIELD_NAME_PDF,
			Constants.PROVINCIA_COMUNE_BASE_FIELD_NAME_PDF,
			Constants.NAZIONE_COMUNE_BASE_FIELD_NAME_PDF,
			Constants.PREFISSO_INTERNAZIONALE_TEL_BASE_FIELD_NAME_PDF,
			Constants.PREFISSO_TEL_BASE_FIELD_NAME_PDF,
			Constants.NUMERO_TEL_BASE_FIELD_NAME_PDF,
			Constants.TIPO_RECAPITO_TEL_BASE_FIELD_NAME_PDF,
			Constants.EMAIL_BASE_FIELD_NAME_PDF,
			Constants.TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF,
			Constants.DESCR_TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF,
			Constants.TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF,
			Constants.DESCR_TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF,
			Constants.IS_PEP_BASE_FIELD_NAME_PDF,
	};


	private BeneficiariUtil() {
		throw new IllegalStateException("Utility class");
	}

	private static void checkCoerenzaBeneficiarioConSottoscrittori(boolean assicurando,
			StringType codiceFiscalePIVABeneficiario, StringType codiceFiscaleContraente,
			StringType codiceFiscaleAssicurando) {

		if (assicurando && !codiceFiscalePIVABeneficiario.isNull() && !codiceFiscaleAssicurando.isNull()
				&& codiceFiscalePIVABeneficiario.equals(codiceFiscaleAssicurando)) {
			codiceFiscalePIVABeneficiario.addTypeError(
					"Il codice fiscale inserito corrisponde a quello dell'Assicurando: l'Assicurando non può essere inserito tra i beneficiari.");

		}

		if (!codiceFiscalePIVABeneficiario.isNull() && !codiceFiscaleContraente.isNull()
				&& (codiceFiscaleAssicurando == null || codiceFiscaleAssicurando.isNull())
				&& codiceFiscalePIVABeneficiario.equals(codiceFiscaleContraente)) {
			codiceFiscalePIVABeneficiario.addTypeError(
					"Il codice fiscale inserito corrisponde a quello del Contraente/Assicurando: il Contraente/Assicurando non può essere inserito tra i beneficiari.");

		}

	}

	private static void checkBeneficiariRipetuti(Set<StringType> beneficiari,
			StringType codiceFiscalePIVABeneficiario) {

		if (!codiceFiscalePIVABeneficiario.isNull() && beneficiari.contains(codiceFiscalePIVABeneficiario)) {
			codiceFiscalePIVABeneficiario
					.addTypeError("E' presente un altro beneficiario con lo stesso codice fiscale/partita IVA.");
		}

		beneficiari.add(codiceFiscalePIVABeneficiario);

	}

	private static void checkClienteGiaCensito(ClientSessionContext csc, boolean isPersonaFisica,
			StringType codiceFiscalePIVA, StringType codiceAgente) throws DAOException {

		if (codiceFiscalePIVA != null && !codiceFiscalePIVA.isNull()) {
			BeneficiariDaoAccess daoAccess = new BeneficiariDaoAccess(csc);
			BeneficiarioModel beneficiarioModel = new BeneficiarioModel();
			beneficiarioModel.setCodiceFiscale(codiceFiscalePIVA);
			if (isPersonaFisica) {
				beneficiarioModel.setTipoRicerca(new StringType("PF"));
			} else {
				beneficiarioModel.setTipoRicerca(new StringType("PG"));
			}

			beneficiarioModel.setCodiceAgente(codiceAgente);

			boolean isClienteGiaCensito = daoAccess.isClienteGiaCensito(beneficiarioModel);

			if (isClienteGiaCensito) {
				codiceFiscalePIVA.addTypeError("Il cliente risulta già censito.");
			}
		}

	}

	private static void checkDatiRecapito(PdfDataModel pdfDataModel, String suffissoBeneficiario) throws Exception {

		String errorMessage = Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO;
		
		StringType tipoTelefonoBeneficiario = (StringType) pdfDataModel.read(Constants.TIPO_RECAPITO_TEL_BASE_FIELD_NAME_PDF + suffissoBeneficiario);

		if (tipoTelefonoBeneficiario.isNull()) {
			tipoTelefonoBeneficiario.addTypeError(errorMessage);
		}

		Utils.checkDatiRecapito(pdfDataModel, suffissoBeneficiario, errorMessage);
		
		StringType prefissoTelefonoBeneficiario = (StringType) pdfDataModel.read(Constants.PREFISSO_TEL_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
		StringType telefonoBeneficiario = (StringType) pdfDataModel.read(Constants.NUMERO_TEL_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
		
		if (tipoTelefonoBeneficiario.equals("F")) {
			if (prefissoTelefonoBeneficiario.toString().length() > 5) {
				prefissoTelefonoBeneficiario.addTypeError("Il campo può contenere al massimo 5 caratteri.");
			}
			if (telefonoBeneficiario.toString().length() > 9) {
				telefonoBeneficiario.addTypeError("Il campo può contenere al massimo 9 caratteri.");
			}
		} else if (tipoTelefonoBeneficiario.equals("C")) {
			if (prefissoTelefonoBeneficiario.toString().length() > 3) {
				prefissoTelefonoBeneficiario.addTypeError("Il campo può contenere al massimo 3 caratteri.");
			}
			if (telefonoBeneficiario.toString().length() > 12) {
				telefonoBeneficiario.addTypeError("Il campo può contenere al massimo 12 caratteri.");
			}
		}
	}

	private static void checkDatiIndirizzoNoCliente(ClientSessionContext csc, PdfDataModel pdfDataModel, String suffissoBeneficiario) {
		Utils.checkDatiIndirizzo(csc, pdfDataModel, suffissoBeneficiario);
	}

	private static void checkDatiIndirizzoGiaCliente(PdfDataModel pdfDataModel, String suffissoBeneficiario) {

		String errorMessage = Constants.MSG_ERRORE_CENSIMENTO_ANAGRAFICA;

		StringType toponimoIndirizzoResidenzaBeneficiario = (StringType) pdfDataModel.read(Constants.TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
		StringType indirizzoResidenzaBeneficiario = (StringType) pdfDataModel.read(Constants.INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
		StringType capComuneResidenzaBeneficiario = (StringType) pdfDataModel.read(Constants.CAP_COMUNE_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
		StringType comuneResidenzaBeneficiario = (StringType) pdfDataModel.read(Constants.COMUNE_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
		StringType provinciaComuneResidenzaBeneficiario = (StringType) pdfDataModel.read(Constants.PROVINCIA_COMUNE_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
		StringType nazioneComuneResidenzaBeneficiario = (StringType) pdfDataModel.read(Constants.NAZIONE_COMUNE_BASE_FIELD_NAME_PDF + suffissoBeneficiario);

		if (indirizzoResidenzaBeneficiario.isNull()) {
			indirizzoResidenzaBeneficiario.addTypeError(errorMessage);
		}

		if (comuneResidenzaBeneficiario.isNull()) {
			comuneResidenzaBeneficiario.addTypeError(errorMessage);
		}

		if (!nazioneComuneResidenzaBeneficiario.isNull()) {

			if (capComuneResidenzaBeneficiario.isNull() 
					&& nazioneComuneResidenzaBeneficiario.equals(prgm.pdfwebformsutil.drivers.autocompletion.comune.ComuneAutocomplete.NAZIONEITALIA)) {
				capComuneResidenzaBeneficiario.addTypeError(errorMessage);
			}

			if (provinciaComuneResidenzaBeneficiario.isNull() 
					&& nazioneComuneResidenzaBeneficiario.equals(prgm.pdfwebformsutil.drivers.autocompletion.comune.ComuneAutocomplete.NAZIONEITALIA)) {
				provinciaComuneResidenzaBeneficiario.addTypeError(errorMessage);
			}

			if (toponimoIndirizzoResidenzaBeneficiario.isNull() 
					&& nazioneComuneResidenzaBeneficiario.equals(prgm.pdfwebformsutil.drivers.autocompletion.comune.ComuneAutocomplete.NAZIONEITALIA)) {
				toponimoIndirizzoResidenzaBeneficiario.addTypeError(errorMessage);
			}

		}

	}

	private static void checkDatiAnagraficiPGNoCliente(PdfDataModel pdfDataModel, String suffissoBeneficiario)
			throws Exception {

		String errorMessage = Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO;

		StringType ragioneSociale = (StringType) pdfDataModel
				.read(Constants.RAGIONE_SOCIALE_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
		StringType codiceFiscalePIVABeneficiario = (StringType) pdfDataModel
				.read(Constants.CODICE_FISCALE_PIVA_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
		StringType numeroIscrizioneCCIAA = (StringType) pdfDataModel
				.read(Constants.NUMERO_ISCRIZIONE_CCIAA_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
		DateType dataIscrizioneCCIAA = (DateType) pdfDataModel
				.read(Constants.DATA_ISCRIZIONE_CCIAA_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
		StringType provinciaIscrizioneCCIAA = (StringType) pdfDataModel
				.read(Constants.PROVINCIA_ISCRIZIONE_CCIAA_BASE_FIELD_NAME_PDF + suffissoBeneficiario);

		if (ragioneSociale.isNull()) {
			ragioneSociale.addTypeError(errorMessage);
		}

		if (codiceFiscalePIVABeneficiario.isNull()) {
			codiceFiscalePIVABeneficiario.addTypeError(errorMessage);
		} else {
			boolean isNumeric = Utils.isNumeric(codiceFiscalePIVABeneficiario.toString());
			if (isNumeric && !Util.checkPartitaIva(codiceFiscalePIVABeneficiario.toString())) {
				codiceFiscalePIVABeneficiario.addTypeError(Constants.MSG_ERRORE_FORMATO_NON_CORRETTO);
			} else if (!isNumeric) {
				CodiceFiscaleUtils.checkCodiceFiscale(codiceFiscalePIVABeneficiario);
			}
		}

		if (!numeroIscrizioneCCIAA.isNull()) {
			if (dataIscrizioneCCIAA.isNull()) {
				dataIscrizioneCCIAA.addTypeError(Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO);
			}
			if (provinciaIscrizioneCCIAA.isNull()) {
				provinciaIscrizioneCCIAA.addTypeError(Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO);
			}
		} else if (numeroIscrizioneCCIAA.isNull()
				&& (!dataIscrizioneCCIAA.isNull() || !provinciaIscrizioneCCIAA.isNull())) {
			numeroIscrizioneCCIAA.addTypeError(Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO);

			if (dataIscrizioneCCIAA.isNull()) {
				dataIscrizioneCCIAA.addTypeError(Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO);
			}
			if (provinciaIscrizioneCCIAA.isNull()) {
				provinciaIscrizioneCCIAA.addTypeError(Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO);
			}
		}

		if (!dataIscrizioneCCIAA.isNull() 
				&& dataIscrizioneCCIAA.compareTo(Tools.today()) > 0) {
			dataIscrizioneCCIAA.addTypeError(Constants.MSG_ERRORE_DATA_ISCRIZIONE_CCIAA);
		}
	}

	private static void checkDatiAnagraficiPGGiaCliente(PdfDataModel pdfDataModel, String suffissoBeneficiario)
			throws Exception {

		String errorMessage = Constants.MSG_ERRORE_CENSIMENTO_ANAGRAFICA;

		StringType ragioneSociale = (StringType) pdfDataModel
				.read(Constants.RAGIONE_SOCIALE_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
		StringType codiceFiscalePIVABeneficiario = (StringType) pdfDataModel
				.read(Constants.CODICE_FISCALE_PIVA_BASE_FIELD_NAME_PDF + suffissoBeneficiario);

		if (ragioneSociale.isNull()) {
			ragioneSociale.addTypeError(errorMessage);
		}

		if (codiceFiscalePIVABeneficiario.isNull()) {
			codiceFiscalePIVABeneficiario.addTypeError(errorMessage);
		}
	}

	private static void checkDatiAnagraficiPFNoCliente(ClientSessionContext csc, PdfDataModel pdfDataModel,
			String suffisso) throws Exception {

		String errorMessage = Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO;
		StringType cognomeBeneficiario = (StringType) pdfDataModel.read(Constants.COGNOME_BASE_FIELD_NAME_PDF + suffisso);
		StringType nomeBeneficiario = (StringType) pdfDataModel.read(Constants.NOME_BASE_FIELD_NAME_PDF + suffisso);
		StringType sessoBeneficiario = (StringType) pdfDataModel.read(Constants.SESSO_BASE_FIELD_NAME_PDF + suffisso);
		StringType codiceFiscaleBeneficiario = (StringType) pdfDataModel.read(Constants.CODICE_FISCALE_BASE_FIELD_NAME_PDF + suffisso);
		DateType dataNascitaBeneficiario = (DateType) pdfDataModel.read(Constants.DATA_NASCITA_BASE_FIELD_NAME_PDF + suffisso);
		StringType comuneNascitaBeneficiario = (StringType) pdfDataModel.read(Constants.COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffisso);
		StringType provinciaComuneNascitaBeneficiario = (StringType) pdfDataModel.read(Constants.PROVINCIA_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffisso);
		StringType nazioneComuneNascitaBeneficiario = (StringType) pdfDataModel.read(Constants.NAZIONE_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffisso);

		if (nomeBeneficiario.isNull()) {
			nomeBeneficiario.addTypeError(errorMessage);
		}

		if (cognomeBeneficiario.isNull()) {
			cognomeBeneficiario.addTypeError(errorMessage);
		}

		if (sessoBeneficiario.isNull()) {
			sessoBeneficiario.addTypeError(errorMessage);
		}

		if (dataNascitaBeneficiario.isNull()) {
			dataNascitaBeneficiario.addTypeError(errorMessage);
		} else {
			if (dataNascitaBeneficiario.compareTo(Tools.today()) >= 0) {
				dataNascitaBeneficiario.addTypeError(Constants.MSG_ERRORE_DATA_NASCITA);
			}
		}

		if (comuneNascitaBeneficiario.isNull()) {
			comuneNascitaBeneficiario.addTypeError(errorMessage);
		}

		if (codiceFiscaleBeneficiario.isNull()) {
			codiceFiscaleBeneficiario.addTypeError(errorMessage);
		} else {
			if (CodiceFiscaleUtils.checkCodiceFiscale(codiceFiscaleBeneficiario)) {

				boolean doVerificaCodiceFiscale = !cognomeBeneficiario.isNull() && !nomeBeneficiario.isNull()
						&& !sessoBeneficiario.isNull() && !dataNascitaBeneficiario.isNull()
						&& !comuneNascitaBeneficiario.isNull() && !provinciaComuneNascitaBeneficiario.isNull()
						&& !nazioneComuneNascitaBeneficiario.isNull()
						&& nazioneComuneNascitaBeneficiario.equalsIgnoreCase(
								prgm.pdfwebformsutil.drivers.autocompletion.comune.ComuneAutocomplete.NAZIONEITALIA);

				if (doVerificaCodiceFiscale) {
					CodiceFiscaleUtils.checkCoerenzaDatiCodiceFiscale(csc, cognomeBeneficiario, nomeBeneficiario,
							sessoBeneficiario, dataNascitaBeneficiario, comuneNascitaBeneficiario,
							provinciaComuneNascitaBeneficiario, codiceFiscaleBeneficiario);
				}

			}
		}

		if (nazioneComuneNascitaBeneficiario.isNull()) {
			nazioneComuneNascitaBeneficiario.addTypeError(Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO);
		} else {
			if (provinciaComuneNascitaBeneficiario.isNull() && nazioneComuneNascitaBeneficiario.equals(prgm.pdfwebformsutil.drivers.autocompletion.comune.ComuneAutocomplete.NAZIONEITALIA)) {
				provinciaComuneNascitaBeneficiario.addTypeError(Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO);
			} else if (!provinciaComuneNascitaBeneficiario.isNull() && !nazioneComuneNascitaBeneficiario.equals(prgm.pdfwebformsutil.drivers.autocompletion.comune.ComuneAutocomplete.NAZIONEITALIA)) {
				provinciaComuneNascitaBeneficiario.addTypeError(Constants.MSG_ERRORE_VALORE_NON_AMMESSO);
			}
		}

	}

	private static void checkDatiAnagraficiPFGiaCliente(PdfDataModel pdfDataModel,
			String suffisso) throws Exception {

		String errorMessage = Constants.MSG_ERRORE_CENSIMENTO_ANAGRAFICA;
		StringType cognomeBeneficiario = (StringType) pdfDataModel
				.read(Constants.COGNOME_BASE_FIELD_NAME_PDF + suffisso);
		StringType nomeBeneficiario = (StringType) pdfDataModel.read(Constants.NOME_BASE_FIELD_NAME_PDF + suffisso);
		StringType sessoBeneficiario = (StringType) pdfDataModel.read(Constants.SESSO_BASE_FIELD_NAME_PDF + suffisso);
		StringType codiceFiscaleBeneficiario = (StringType) pdfDataModel
				.read(Constants.CODICE_FISCALE_BASE_FIELD_NAME_PDF + suffisso);
		DateType dataNascitaBeneficiario = (DateType) pdfDataModel
				.read(Constants.DATA_NASCITA_BASE_FIELD_NAME_PDF + suffisso);
		StringType comuneNascitaBeneficiario = (StringType) pdfDataModel
				.read(Constants.COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffisso);
		StringType provinciaComuneNascitaBeneficiario = (StringType) pdfDataModel
				.read(Constants.PROVINCIA_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffisso);
		StringType nazioneComuneNascitaBeneficiario = (StringType) pdfDataModel
				.read(Constants.NAZIONE_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffisso);

		if (nomeBeneficiario.isNull()) {
			nomeBeneficiario.addTypeError(errorMessage);
		}

		if (cognomeBeneficiario.isNull()) {
			cognomeBeneficiario.addTypeError(errorMessage);
		}

		if (sessoBeneficiario.isNull()) {
			sessoBeneficiario.addTypeError(errorMessage);
		}

		if (dataNascitaBeneficiario.isNull()) {
			dataNascitaBeneficiario.addTypeError(errorMessage);
		} else {
			if (dataNascitaBeneficiario.compareTo(Tools.today()) >= 0) {
				dataNascitaBeneficiario.addTypeError(Constants.MSG_ERRORE_DATA_NASCITA);
			}
		}

		if (comuneNascitaBeneficiario.isNull()) {
			comuneNascitaBeneficiario.addTypeError(errorMessage);
		}

		if (codiceFiscaleBeneficiario.isNull()) {
			codiceFiscaleBeneficiario.addTypeError(errorMessage);
		}

		if (!nazioneComuneNascitaBeneficiario.isNull() && provinciaComuneNascitaBeneficiario.isNull() && nazioneComuneNascitaBeneficiario
					.equals(prgm.pdfwebformsutil.drivers.autocompletion.comune.ComuneAutocomplete.NAZIONEITALIA)) {
				provinciaComuneNascitaBeneficiario.addTypeError(errorMessage);
		}

	}

	public static void checkRelazione(StringType tipoRelazione, StringType descrizioneRelazione) {

		if (tipoRelazione.isNull()) {
			tipoRelazione.addTypeError(Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO);
		} else if (tipoRelazione.equalsIgnoreCase(Constants.TIPO_RELAZIONE_ALTRO)) {
			if (descrizioneRelazione.isNull()) {
				descrizioneRelazione.addTypeError(Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO);
			} else {
				if (!Utils.checkFormatoDescrizioneRelazione(descrizioneRelazione)) {
					descrizioneRelazione.addTypeError(Constants.MSG_ERRORE_FORMATO_NON_CORRETTO);
				}
				if (!Utils.checkLunghezzaDescrizioneRelazione(descrizioneRelazione)) {
					descrizioneRelazione.addTypeError(Constants.MSG_ERRORE_LUNGHEZZA_DESCRIZIONE_RELAZIONE_ERRATA);
				}
			}
		} else if (!tipoRelazione.equalsIgnoreCase(Constants.TIPO_RELAZIONE_ALTRO) && !descrizioneRelazione.isNull()) {
			descrizioneRelazione.addTypeError(Constants.MSG_ERRORE_VALORE_NON_AMMESSO);
		}
	}

	public static void checkBeneficiarioNominativo(ClientSessionContext csc, PdfDataModel pdfDataModel,
			boolean assicurando, int posizioneAssicurando, String prefissoBeneficiario, int indiceBeneficiario,
			Set<StringType> beneficiari, String tipoCasoBeneficiario) throws Exception, DAOException {

		StringType codiceFiscalePartitaIvaCliente1 = (StringType) pdfDataModel.read("codiceFiscalePartitaIvaCliente1");
		StringType codiceFiscalePartitaIvaAssicurando = (StringType) pdfDataModel.read("codiceFiscalePartitaIvaCliente" + posizioneAssicurando);
		StringType codiceAgente = Utils.getCodiceAgente(pdfDataModel);

		String suffissoBeneficiario = prefissoBeneficiario + indiceBeneficiario;

		StringType isGiaCliente = (StringType) pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario);

		if (!isGiaCliente.isNull()) {

			StringType codiceCliente = (StringType) pdfDataModel.read(Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
			if (pdfDataModel.isOperatoreMOM() && codiceCliente.isNull()) {
				codiceCliente.addTypeError(Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO);
			}
			
			checkPercentuale(pdfDataModel, isGiaCliente, suffissoBeneficiario);

			boolean isPersonaFisica = true;
			if (((StringType) pdfDataModel.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF + suffissoBeneficiario)).equals(FALSE)) {
				isPersonaFisica = false;
			}

			StringType invioComunicazione = (StringType) pdfDataModel.read(Constants.INVIO_COMUNICAZIONE_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
			// se presente è obbligatorio (presente solo per beneficiari vita)
			if (invioComunicazione != null && invioComunicazione.isNull()) {
				invioComunicazione.addTypeError(Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO);
			}

			DichiarazioniBeneficiariUtil.checkDichiarazioneBeneficiarioNominativoNonCensito(pdfDataModel, suffissoBeneficiario);
			
			if (isPersonaFisica) {// persona fisica

				StringType codiceFiscaleBeneficiario = (StringType) pdfDataModel.read(Constants.CODICE_FISCALE_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
				StringType tipoRelazioneContraenteBeneficiario = (StringType) pdfDataModel.read(Constants.TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
				StringType descrizioneTipoRelazioneContraenteBeneficiario = (StringType) pdfDataModel.read(Constants.DESCR_TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario);

				if (isGiaCliente.equals("S")) {
					BeneficiariUtil.checkDatiAnagraficiPFGiaCliente(pdfDataModel, suffissoBeneficiario);
					BeneficiariUtil.checkDatiIndirizzoGiaCliente(pdfDataModel, suffissoBeneficiario);
					BeneficiariUtil.checkDatiRecapitoGiaCliente(pdfDataModel, suffissoBeneficiario);
				} else if (isGiaCliente.equals("N")) {
					BeneficiariUtil.checkDatiAnagraficiPFNoCliente(csc, pdfDataModel, suffissoBeneficiario);
					if (!codiceFiscaleBeneficiario.hasTypeErrors()) {
						BeneficiariUtil.checkClienteGiaCensito(csc, isPersonaFisica, codiceFiscaleBeneficiario,	codiceAgente);
					}
					BeneficiariUtil.checkDatiIndirizzoNoCliente(csc, pdfDataModel, suffissoBeneficiario);
					BeneficiariUtil.checkDatiRecapito(pdfDataModel, suffissoBeneficiario);
				}

				/* controllo beneficiari ripetuti */
				BeneficiariUtil.checkBeneficiariRipetuti(beneficiari, codiceFiscaleBeneficiario);

				/* controllo beneficiari / sottoscrittori */
				if (tipoCasoBeneficiario.equals(Constants.TIPO_CASO_BENEFICARIO_DECESSO)) {
					BeneficiariUtil.checkCoerenzaBeneficiarioConSottoscrittori(assicurando, codiceFiscaleBeneficiario,
							codiceFiscalePartitaIvaCliente1, codiceFiscalePartitaIvaAssicurando);
				}

				/* controllo relazioni */
				BeneficiariUtil.checkRelazione(tipoRelazioneContraenteBeneficiario,
						descrizioneTipoRelazioneContraenteBeneficiario);

				if (assicurando) {
					StringType tipoRelazioneAssicurandoBeneficiario = (StringType) pdfDataModel
							.read(Constants.TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
					StringType descrizioneTipoRelazioneAssicurandoBeneficiario = (StringType) pdfDataModel.read(
							Constants.DESCR_TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + suffissoBeneficiario);

					if (tipoRelazioneAssicurandoBeneficiario != null
							&& !codiceFiscalePartitaIvaAssicurando.isNull() 
							&& !codiceFiscalePartitaIvaCliente1.isNull()
							&& !codiceFiscalePartitaIvaAssicurando.toString().equalsIgnoreCase(codiceFiscalePartitaIvaCliente1.toString())) {
						BeneficiariUtil.checkRelazione(tipoRelazioneAssicurandoBeneficiario,
								descrizioneTipoRelazioneAssicurandoBeneficiario);
					}
				}
				
				/* controllo pep */
				StringType isPep = (StringType) pdfDataModel.read(Constants.IS_PEP_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
				if (isPep != null && isPep.isNull())
					isPep.addTypeError(Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO);

			} else {// persona giuridica

				StringType codiceFiscaleBeneficiario = (StringType) pdfDataModel.read(Constants.CODICE_FISCALE_PIVA_BASE_FIELD_NAME_PDF + suffissoBeneficiario);

				/* controllo beneficiari ripetuti */
				BeneficiariUtil.checkBeneficiariRipetuti(beneficiari, codiceFiscaleBeneficiario);

				/* controllo beneficiari / sottoscrittori */
				BeneficiariUtil.checkCoerenzaBeneficiarioConSottoscrittori(assicurando, codiceFiscaleBeneficiario, codiceFiscalePartitaIvaCliente1, codiceFiscalePartitaIvaAssicurando);

				if (isGiaCliente.equals("S")) {
					BeneficiariUtil.checkDatiAnagraficiPGGiaCliente(pdfDataModel, suffissoBeneficiario);
					BeneficiariUtil.checkDatiIndirizzoGiaCliente(pdfDataModel, suffissoBeneficiario);
				} else if (isGiaCliente.equals("N")) {
					BeneficiariUtil.checkDatiAnagraficiPGNoCliente(pdfDataModel, suffissoBeneficiario);
					if (!codiceFiscaleBeneficiario.hasTypeErrors()) {
						BeneficiariUtil.checkClienteGiaCensito(csc, isPersonaFisica, codiceFiscaleBeneficiario,
								codiceAgente);
					}
					BeneficiariUtil.checkDatiIndirizzoNoCliente(csc, pdfDataModel, suffissoBeneficiario);
					BeneficiariUtil.checkDatiRecapito(pdfDataModel, suffissoBeneficiario);
				}

				int numeroTitolariCompilati = 0;
				HashSet<StringType> titolari = new HashSet<StringType>();

				// ciclo sui titolari
				for (int j = 1;; j++) {
					String suffissoTitolare = Constants.TITOLARE + j + prefissoBeneficiario + indiceBeneficiario;

					if (pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare) == null) {
						break;
					}

					StringType isGiaClienteTitolare = (StringType) pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare);
					if (!isGiaClienteTitolare.isNull()) {
						StringType codiceFiscaleTitolare = (StringType) pdfDataModel.read(Constants.CODICE_FISCALE_BASE_FIELD_NAME_PDF + suffissoTitolare);

						numeroTitolariCompilati++;

						/* controllo titolari ripetuti */
						if (!titolari.add(codiceFiscaleTitolare)) {
							codiceFiscaleTitolare
									.addTypeError("E' presente un altro titolare con lo stesso codice fiscale.");
						}

						if (isGiaClienteTitolare.equals("S")) {
							BeneficiariUtil.checkDatiAnagraficiPFGiaCliente(pdfDataModel, suffissoTitolare);
							BeneficiariUtil.checkDatiIndirizzoGiaCliente(pdfDataModel, suffissoTitolare);					
						} else if (isGiaClienteTitolare.equals("N")) {
							BeneficiariUtil.checkDatiAnagraficiPFNoCliente(csc, pdfDataModel, suffissoTitolare);
							if (!codiceFiscaleTitolare.hasTypeErrors()) {
								BeneficiariUtil.checkClienteGiaCensito(csc, true, codiceFiscaleTitolare, codiceAgente);
							}
							BeneficiariUtil.checkDatiIndirizzoNoCliente(csc, pdfDataModel, suffissoTitolare);
							BeneficiariUtil.checkDatiRecapito(pdfDataModel, suffissoTitolare);
						}

						/* controllo relazioni */
						StringType tipoRelazioneContraenteBeneficiario = (StringType) pdfDataModel
								.read(Constants.TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + suffissoTitolare);
						StringType descrizioneTipoRelazioneContraenteBeneficiario = (StringType) pdfDataModel
								.read(Constants.DESCR_TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + suffissoTitolare);

						BeneficiariUtil.checkRelazione(tipoRelazioneContraenteBeneficiario,
								descrizioneTipoRelazioneContraenteBeneficiario);

						if (assicurando) {
							StringType tipoRelazioneAssicurandoBeneficiario = (StringType) pdfDataModel
									.read(Constants.TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + suffissoTitolare);
							StringType descrizioneTipoRelazioneAssicurandoBeneficiario = (StringType) pdfDataModel.read(
									Constants.DESCR_TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + suffissoTitolare);
							if (tipoRelazioneAssicurandoBeneficiario != null
									&& !codiceFiscalePartitaIvaAssicurando.isNull()
									&& !codiceFiscalePartitaIvaCliente1.isNull() 
									&& !codiceFiscalePartitaIvaAssicurando.toString().equalsIgnoreCase(codiceFiscalePartitaIvaCliente1.toString())) {
								BeneficiariUtil.checkRelazione(tipoRelazioneAssicurandoBeneficiario,
										descrizioneTipoRelazioneAssicurandoBeneficiario);
							}
						}

						/* controllo pep */
						StringType isPep = (StringType) pdfDataModel.read(Constants.IS_PEP_BASE_FIELD_NAME_PDF + suffissoTitolare);
						if (isPep != null && isPep.isNull())
							isPep.addTypeError(Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO);
					}
				}

				StringType cognomeTitolare1Beneficiario = (StringType) pdfDataModel
						.read(Constants.COGNOME_BASE_FIELD_NAME_PDF + Constants.TITOLARE + "1" + prefissoBeneficiario
								+ indiceBeneficiario);

				if (numeroTitolariCompilati == 0 && isGiaCliente.equals("N")) {
					cognomeTitolare1Beneficiario.addTypeError("E' necessario inserire almeno un titolare effettivo.");
				} else if (numeroTitolariCompilati == 0 && isGiaCliente.equals("S")) {
					cognomeTitolare1Beneficiario.addTypeError(Constants.MSG_ERRORE_CENSIMENTO_ANAGRAFICA);
				}
			}
		}

	}

	private static void checkPercentuale(PdfDataModel pdfDataModel, StringType isGiaCliente, String suffissoBeneficiario) {
		
		DoubleType percentualeBeneficiario = (DoubleType) pdfDataModel.read(Constants.PERCENTUALE_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
		// se presente è obbligatorio (non c'è sui moduli di recupero dei beneficiari)
		if (percentualeBeneficiario == null ) 
			return;
		
		StringType codiceCliente = (StringType) pdfDataModel.read(Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF+suffissoBeneficiario);
		if (isGiaCliente.equals("S") && codiceCliente.isNull())
			return;
		
		if (percentualeBeneficiario.isNull()) {
			percentualeBeneficiario.addTypeError(Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO);
		} else if (percentualeBeneficiario.doubleValue() <= 0 
				|| percentualeBeneficiario.doubleValue() > 100) {
			percentualeBeneficiario.addTypeError(Constants.MSG_ERRORE_PERCENTUALE_NON_AMMESSA);
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void compattaBeneficiariCasoVita(PdfDataModel pdfDataModel, boolean assicurando) {

		String prefissoBeneficiario = Constants.PREFISSO_BENEFICIARIO_VITA;

		StringType tipoBeneficiario = (StringType) pdfDataModel
				.read(Constants.TIPO_BENEFICIARIO_BASE_FIELD_NAME_PDF + prefissoBeneficiario);

		if (!tipoBeneficiario.isNull() && tipoBeneficiario.equals(Constants.TIPO_BENEFICIARIO_ALTRO)) {
			BeneficiariUtil.compattaBeneficiari(pdfDataModel, assicurando, prefissoBeneficiario);
		}
	}

	public static void compattaBeneficiariCasoDecesso(PdfDataModel pdfDataModel, boolean assicurando) {

		String prefissoBeneficiario = Constants.PREFISSO_BENEFICIARIO_DECESSO;

		StringType tipoBeneficiario = (StringType) pdfDataModel
				.read(Constants.TIPO_BENEFICIARIO_BASE_FIELD_NAME_PDF + prefissoBeneficiario);

		if (!tipoBeneficiario.isNull() && tipoBeneficiario.equals(Constants.TIPO_BENEFICIARIO_ALTRO)) {
			BeneficiariUtil.compattaBeneficiari(pdfDataModel, assicurando, prefissoBeneficiario);
		}
	}

	private static void compattaBeneficiari(PdfDataModel pdfDataModel, boolean assicurando,
			String prefissoBeneficiario) {

		ArrayList<MapCommandDataModel> beneficiariPFSelezionati = new ArrayList<MapCommandDataModel>();
		ArrayList<MapCommandDataModel> beneficiariPGSelezionati = new ArrayList<MapCommandDataModel>();

		for (int i = 1;; i++) {
			String suffissoBeneficiario = prefissoBeneficiario + i;
			if (pdfDataModel.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF + suffissoBeneficiario) == null) {
				break;
			}

			boolean isPersonaFisica = true;
			if (((StringType) pdfDataModel.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF + suffissoBeneficiario))
					.equals(FALSE)) {
				isPersonaFisica = false;
			}

			if (isBeneficiarioCompilato(pdfDataModel, assicurando, isPersonaFisica, prefissoBeneficiario, i)) {
				MapCommandDataModel beneficiarioModel = beneficiarioToModel(pdfDataModel, assicurando, isPersonaFisica,
						prefissoBeneficiario, i);
				if (isPersonaFisica) {
					beneficiariPFSelezionati.add(beneficiarioModel);
				} else {
					beneficiariPGSelezionati.add(beneficiarioModel);
				}
			}
		}

		// compatto i beneficiari PF
		int firstBeneficiarioPG = 1;
		int idx = 0;
		for (int i = 1;; i++) {
			String suffissoBeneficiario = prefissoBeneficiario + i;
			if (pdfDataModel.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF + suffissoBeneficiario) == null) {
				break;
			}

			boolean isPersonaFisica = true;
			if (((StringType) pdfDataModel.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF + suffissoBeneficiario))
					.equals(FALSE)) {
				isPersonaFisica = false;
			}

			if (!isPersonaFisica) {
				firstBeneficiarioPG = i;
				break;
			}

			if (idx < beneficiariPFSelezionati.size()) {
				MapCommandDataModel beneficiarioModel = beneficiariPFSelezionati.get(idx);
				beneficiarioToPdf(beneficiarioModel, pdfDataModel, assicurando, true, prefissoBeneficiario, i);
			} else {
				clearBeneficiario(pdfDataModel, assicurando, true, prefissoBeneficiario, i);
			}
			idx++;
		}

		// compatto i beneficiari PG
		idx = 0;
		for (int j = firstBeneficiarioPG;; j++) {
			String suffissoBeneficiario = prefissoBeneficiario + j;
			if (pdfDataModel.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF + suffissoBeneficiario) == null) {
				break;
			}

			if (idx < beneficiariPGSelezionati.size()) {
				MapCommandDataModel beneficiarioModel = beneficiariPGSelezionati.get(idx);
				beneficiarioToPdf(beneficiarioModel, pdfDataModel, assicurando, false, prefissoBeneficiario, j);
			} else {
				clearBeneficiario(pdfDataModel, assicurando, false, prefissoBeneficiario, j);
			}

			// per ogni beneficiario PG devo compattare i titolari

			compattaTitolari(pdfDataModel, assicurando, prefissoBeneficiario, j);

			idx++;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void compattaTitolari(PdfDataModel pdfDataModel, boolean assicurando, String prefissoBeneficiario,
			int indiceBeneficiario) {

		ArrayList<MapCommandDataModel> titolariSelezionati = new ArrayList<MapCommandDataModel>();

		for (int i = 1;; i++) {
			String suffissoTitolare = Constants.TITOLARE + i + prefissoBeneficiario + indiceBeneficiario;
			if (pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare) == null) {
				break;
			}

			if (isTitolareCompilato(pdfDataModel, assicurando, prefissoBeneficiario, indiceBeneficiario, i)) {
				MapCommandDataModel titolareModel = titolareToModel(pdfDataModel, assicurando, prefissoBeneficiario,
						indiceBeneficiario, i);
				titolariSelezionati.add(titolareModel);
			}
		}

		// compatto i titolari
		int idxTit = 0;
		for (int k = 1;; k++) {
			String suffissoTitolare = Constants.TITOLARE + k + prefissoBeneficiario + indiceBeneficiario;
			if (pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare) == null) {
				break;
			}

			if (idxTit < titolariSelezionati.size()) {
				MapCommandDataModel titolareModel = titolariSelezionati.get(idxTit);
				titolareToPdf(titolareModel, pdfDataModel, assicurando, prefissoBeneficiario, indiceBeneficiario, k);
			} else {
				clearTitolare(pdfDataModel, assicurando, prefissoBeneficiario, indiceBeneficiario, k);
			}
			idxTit++;
		}

	}

	private static boolean isBeneficiarioCompilato(PdfDataModel pdfDataModel, boolean assicurando,
			boolean isPersonaFisica, String prefissoBeneficiario, int indiceBeneficiario) {

		String suffissoBeneficiario = prefissoBeneficiario + indiceBeneficiario;

		AbstractType percentuale = pdfDataModel.read(Constants.PERCENTUALE_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
		AbstractType tipoRelazioneAssicurando = pdfDataModel.read(Constants.TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
		AbstractType descrTipoRelazioneAssicurando = pdfDataModel.read(Constants.DESCR_TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
		AbstractType isPep = pdfDataModel.read(Constants.IS_PEP_BASE_FIELD_NAME_PDF+ suffissoBeneficiario);
			
		if (isPersonaFisica && (!pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario)
				.isNull() || !pdfDataModel.read(Constants.COGNOME_BASE_FIELD_NAME_PDF + suffissoBeneficiario).isNull()
				|| !pdfDataModel.read(Constants.NOME_BASE_FIELD_NAME_PDF + suffissoBeneficiario).isNull()
				|| !pdfDataModel.read(Constants.CODICE_FISCALE_BASE_FIELD_NAME_PDF + suffissoBeneficiario).isNull()
				|| !pdfDataModel.read(Constants.SESSO_BASE_FIELD_NAME_PDF + suffissoBeneficiario).isNull()
				|| !pdfDataModel.read(Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario).isNull()
				|| (percentuale != null && !percentuale.isNull())
				|| !pdfDataModel.read(Constants.DATA_NASCITA_BASE_FIELD_NAME_PDF + suffissoBeneficiario).isNull()
				|| !pdfDataModel.read(Constants.COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffissoBeneficiario).isNull()
				|| !pdfDataModel.read(Constants.PROVINCIA_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffissoBeneficiario)
						.isNull()
				|| !pdfDataModel.read(Constants.NAZIONE_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffissoBeneficiario)
						.isNull()
				|| !pdfDataModel.read(Constants.TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoBeneficiario).isNull()
				|| !pdfDataModel.read(Constants.INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoBeneficiario).isNull()
				|| !pdfDataModel.read(Constants.NUMERO_CIVICO_INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoBeneficiario)
						.isNull()
				|| !pdfDataModel.read(Constants.CAP_COMUNE_BASE_FIELD_NAME_PDF + suffissoBeneficiario).isNull()
				|| !pdfDataModel.read(Constants.COMUNE_BASE_FIELD_NAME_PDF + suffissoBeneficiario).isNull()
				|| !pdfDataModel.read(Constants.PROVINCIA_COMUNE_BASE_FIELD_NAME_PDF + suffissoBeneficiario).isNull()
				|| !pdfDataModel.read(Constants.NAZIONE_COMUNE_BASE_FIELD_NAME_PDF + suffissoBeneficiario).isNull()
				|| !pdfDataModel.read(Constants.TIPO_RECAPITO_TEL_BASE_FIELD_NAME_PDF + suffissoBeneficiario).isNull()
				|| !pdfDataModel.read(Constants.PREFISSO_INTERNAZIONALE_TEL_BASE_FIELD_NAME_PDF + suffissoBeneficiario)
						.isNull()
				|| !pdfDataModel.read(Constants.PREFISSO_TEL_BASE_FIELD_NAME_PDF + suffissoBeneficiario).isNull()
				|| !pdfDataModel.read(Constants.NUMERO_TEL_BASE_FIELD_NAME_PDF + suffissoBeneficiario).isNull()
				|| !pdfDataModel.read(Constants.EMAIL_BASE_FIELD_NAME_PDF + suffissoBeneficiario).isNull()
				|| !pdfDataModel.read(Constants.TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario)
						.isNull()
				|| !pdfDataModel
						.read(Constants.DESCR_TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario)
						.isNull()
				|| (assicurando && tipoRelazioneAssicurando != null && !tipoRelazioneAssicurando.isNull())
				|| (assicurando && descrTipoRelazioneAssicurando!= null && !descrTipoRelazioneAssicurando.isNull()))
				|| (isPep != null && !isPep.isNull())){
			return true;

		} else if (!isPersonaFisica) {
			if (!pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario).isNull()
					|| !pdfDataModel.read(Constants.RAGIONE_SOCIALE_BASE_FIELD_NAME_PDF + suffissoBeneficiario).isNull()
					|| !pdfDataModel.read(Constants.CODICE_FISCALE_PIVA_BASE_FIELD_NAME_PDF + suffissoBeneficiario)
							.isNull()
					|| !pdfDataModel.read(Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario).isNull()
					|| (percentuale != null && !percentuale.isNull())
					|| !pdfDataModel.read(Constants.NUMERO_ISCRIZIONE_CCIAA_BASE_FIELD_NAME_PDF + suffissoBeneficiario)
							.isNull()
					|| !pdfDataModel.read(Constants.DATA_ISCRIZIONE_CCIAA_BASE_FIELD_NAME_PDF + suffissoBeneficiario)
							.isNull()
					|| !pdfDataModel
							.read(Constants.PROVINCIA_ISCRIZIONE_CCIAA_BASE_FIELD_NAME_PDF + suffissoBeneficiario)
							.isNull()
					|| !pdfDataModel.read(Constants.TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoBeneficiario)
							.isNull()
					|| !pdfDataModel.read(Constants.INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoBeneficiario).isNull()
					|| !pdfDataModel.read(Constants.NUMERO_CIVICO_INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoBeneficiario)
							.isNull()
					|| !pdfDataModel.read(Constants.CAP_COMUNE_BASE_FIELD_NAME_PDF + suffissoBeneficiario).isNull()
					|| !pdfDataModel.read(Constants.COMUNE_BASE_FIELD_NAME_PDF + suffissoBeneficiario).isNull()
					|| !pdfDataModel.read(Constants.PROVINCIA_COMUNE_BASE_FIELD_NAME_PDF + suffissoBeneficiario)
							.isNull()
					|| !pdfDataModel.read(Constants.NAZIONE_COMUNE_BASE_FIELD_NAME_PDF + suffissoBeneficiario).isNull()
					|| !pdfDataModel.read(Constants.TIPO_RECAPITO_TEL_BASE_FIELD_NAME_PDF + suffissoBeneficiario)
							.isNull()
					|| !pdfDataModel
							.read(Constants.PREFISSO_INTERNAZIONALE_TEL_BASE_FIELD_NAME_PDF + suffissoBeneficiario)
							.isNull()
					|| !pdfDataModel.read(Constants.PREFISSO_TEL_BASE_FIELD_NAME_PDF + suffissoBeneficiario).isNull()
					|| !pdfDataModel.read(Constants.NUMERO_TEL_BASE_FIELD_NAME_PDF + suffissoBeneficiario).isNull()
					|| !pdfDataModel.read(Constants.EMAIL_BASE_FIELD_NAME_PDF + suffissoBeneficiario).isNull()) {
				return true;
			} else {
				// controllo se valorizzato almeno un titolare
				// ciclo sui titolari
				for (int j = 1;; j++) {
					String suffissoTitolare = Constants.TITOLARE + j + prefissoBeneficiario + indiceBeneficiario;

					if (pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare) == null) {
						break;
					}

					if (isTitolareCompilato(pdfDataModel, assicurando, prefissoBeneficiario, indiceBeneficiario, j)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	private static boolean isTitolareCompilato(PdfDataModel pdfDataModel, boolean assicurando,
			String prefissoBeneficiario, int indiceBeneficiario, int indiceTitolare) {

		String suffissoTitolare = Constants.TITOLARE + indiceTitolare + prefissoBeneficiario + indiceBeneficiario;

		AbstractType tipoRelazioneAssicurando = pdfDataModel.read(Constants.TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + suffissoTitolare);
		AbstractType descrTipoRelazioneAssicurando = pdfDataModel.read(Constants.DESCR_TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + suffissoTitolare);
		AbstractType isPep = pdfDataModel.read(Constants.IS_PEP_BASE_FIELD_NAME_PDF+ suffissoTitolare);
		
		if (!pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare).isNull()
				|| !pdfDataModel.read(Constants.COGNOME_BASE_FIELD_NAME_PDF + suffissoTitolare).isNull()
				|| !pdfDataModel.read(Constants.NOME_BASE_FIELD_NAME_PDF + suffissoTitolare).isNull()
				|| !pdfDataModel.read(Constants.CODICE_FISCALE_BASE_FIELD_NAME_PDF + suffissoTitolare).isNull()
				|| !pdfDataModel.read(Constants.SESSO_BASE_FIELD_NAME_PDF + suffissoTitolare).isNull()
				|| !pdfDataModel.read(Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare).isNull()
				|| !pdfDataModel.read(Constants.DATA_NASCITA_BASE_FIELD_NAME_PDF + suffissoTitolare).isNull()
				|| !pdfDataModel.read(Constants.COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffissoTitolare).isNull()
				|| !pdfDataModel.read(Constants.PROVINCIA_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffissoTitolare)
						.isNull()
				|| !pdfDataModel.read(Constants.NAZIONE_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffissoTitolare).isNull()
				|| !pdfDataModel.read(Constants.TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoTitolare).isNull()
				|| !pdfDataModel.read(Constants.INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoTitolare).isNull()
				|| !pdfDataModel.read(Constants.NUMERO_CIVICO_INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoTitolare).isNull()
				|| !pdfDataModel.read(Constants.CAP_COMUNE_BASE_FIELD_NAME_PDF + suffissoTitolare).isNull()
				|| !pdfDataModel.read(Constants.COMUNE_BASE_FIELD_NAME_PDF + suffissoTitolare).isNull()
				|| !pdfDataModel.read(Constants.PROVINCIA_COMUNE_BASE_FIELD_NAME_PDF + suffissoTitolare).isNull()
				|| !pdfDataModel.read(Constants.NAZIONE_COMUNE_BASE_FIELD_NAME_PDF + suffissoTitolare).isNull()
				|| !pdfDataModel.read(Constants.TIPO_RECAPITO_TEL_BASE_FIELD_NAME_PDF + suffissoTitolare).isNull()
				|| !pdfDataModel.read(Constants.PREFISSO_INTERNAZIONALE_TEL_BASE_FIELD_NAME_PDF + suffissoTitolare)
						.isNull()
				|| !pdfDataModel.read(Constants.PREFISSO_TEL_BASE_FIELD_NAME_PDF + suffissoTitolare).isNull()
				|| !pdfDataModel.read(Constants.NUMERO_TEL_BASE_FIELD_NAME_PDF + suffissoTitolare).isNull()
				|| !pdfDataModel.read(Constants.EMAIL_BASE_FIELD_NAME_PDF + suffissoTitolare).isNull()
				|| !pdfDataModel.read(Constants.TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + suffissoTitolare)
						.isNull()
				|| !pdfDataModel.read(Constants.DESCR_TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + suffissoTitolare)
						.isNull()
				|| (assicurando && tipoRelazioneAssicurando != null && !tipoRelazioneAssicurando.isNull())
				|| (assicurando && descrTipoRelazioneAssicurando!= null && !descrTipoRelazioneAssicurando.isNull())
				|| (isPep != null && !isPep.isNull())) {
			return true;
		}

		return false;

	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static MapCommandDataModel beneficiarioToModel(PdfDataModel pdfDataModel, boolean assicurando,
			boolean isPersonaFisica, String prefissoBeneficiario, int indiceBeneficiario) {
		MapCommandDataModel beneficiarioModel = new MapCommandDataModel();

		String suffissoBeneficiario = prefissoBeneficiario + indiceBeneficiario;

		beneficiarioModel.addProperty(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + prefissoBeneficiario,
				pdfDataModel.readProperty(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario));
		beneficiarioModel.addProperty(Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF + prefissoBeneficiario,
				pdfDataModel.readProperty(Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario));

		// Indirizzo
		beneficiarioModel.addProperty(Constants.COD_TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF + prefissoBeneficiario,
				pdfDataModel.readProperty(Constants.COD_TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoBeneficiario));
		beneficiarioModel.addProperty(Constants.TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF + prefissoBeneficiario,
				pdfDataModel.readProperty(Constants.TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoBeneficiario));
		beneficiarioModel.addProperty(Constants.INDIRIZZO_BASE_FIELD_NAME_PDF + prefissoBeneficiario,
				pdfDataModel.readProperty(Constants.INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoBeneficiario));
		beneficiarioModel.addProperty(Constants.NUMERO_CIVICO_INDIRIZZO_BASE_FIELD_NAME_PDF + prefissoBeneficiario,
				pdfDataModel
						.readProperty(Constants.NUMERO_CIVICO_INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoBeneficiario));
		beneficiarioModel.addProperty(Constants.CAP_COMUNE_BASE_FIELD_NAME_PDF + prefissoBeneficiario,
				pdfDataModel.readProperty(Constants.CAP_COMUNE_BASE_FIELD_NAME_PDF + suffissoBeneficiario));
		beneficiarioModel.addProperty(Constants.COMUNE_BASE_FIELD_NAME_PDF + prefissoBeneficiario,
				pdfDataModel.readProperty(Constants.COMUNE_BASE_FIELD_NAME_PDF + suffissoBeneficiario));
		beneficiarioModel.addProperty(Constants.PROVINCIA_COMUNE_BASE_FIELD_NAME_PDF + prefissoBeneficiario,
				pdfDataModel.readProperty(Constants.PROVINCIA_COMUNE_BASE_FIELD_NAME_PDF + suffissoBeneficiario));
		beneficiarioModel.addProperty(Constants.NAZIONE_COMUNE_BASE_FIELD_NAME_PDF + prefissoBeneficiario,
				pdfDataModel.readProperty(Constants.NAZIONE_COMUNE_BASE_FIELD_NAME_PDF + suffissoBeneficiario));

		// recapiti
		beneficiarioModel.addProperty(Constants.PREFISSO_INTERNAZIONALE_TEL_BASE_FIELD_NAME_PDF + prefissoBeneficiario,
				pdfDataModel.readProperty(
						Constants.PREFISSO_INTERNAZIONALE_TEL_BASE_FIELD_NAME_PDF + suffissoBeneficiario));
		beneficiarioModel.addProperty(Constants.PREFISSO_TEL_BASE_FIELD_NAME_PDF + prefissoBeneficiario,
				pdfDataModel.readProperty(Constants.PREFISSO_TEL_BASE_FIELD_NAME_PDF + suffissoBeneficiario));
		beneficiarioModel.addProperty(Constants.NUMERO_TEL_BASE_FIELD_NAME_PDF + prefissoBeneficiario,
				pdfDataModel.readProperty(Constants.NUMERO_TEL_BASE_FIELD_NAME_PDF + suffissoBeneficiario));
		beneficiarioModel.addProperty(Constants.TIPO_RECAPITO_TEL_BASE_FIELD_NAME_PDF + prefissoBeneficiario,
				pdfDataModel.readProperty(Constants.TIPO_RECAPITO_TEL_BASE_FIELD_NAME_PDF + suffissoBeneficiario));
		beneficiarioModel.addProperty(Constants.EMAIL_BASE_FIELD_NAME_PDF + prefissoBeneficiario,
				pdfDataModel.readProperty(Constants.EMAIL_BASE_FIELD_NAME_PDF + suffissoBeneficiario));

		// percentuale
		AbstractType percentuale = pdfDataModel.readProperty(Constants.PERCENTUALE_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
		if (percentuale != null) {
			beneficiarioModel.addProperty(Constants.PERCENTUALE_BASE_FIELD_NAME_PDF + prefissoBeneficiario,
					pdfDataModel.readProperty(Constants.PERCENTUALE_BASE_FIELD_NAME_PDF + suffissoBeneficiario));
		}

		if (isPersonaFisica) {
			// dati anagrafici
			beneficiarioModel.addProperty(Constants.NOME_BASE_FIELD_NAME_PDF + prefissoBeneficiario,
					pdfDataModel.readProperty(Constants.NOME_BASE_FIELD_NAME_PDF + suffissoBeneficiario));
			beneficiarioModel.addProperty(Constants.COGNOME_BASE_FIELD_NAME_PDF + prefissoBeneficiario,
					pdfDataModel.readProperty(Constants.COGNOME_BASE_FIELD_NAME_PDF + suffissoBeneficiario));
			beneficiarioModel.addProperty(Constants.CODICE_FISCALE_BASE_FIELD_NAME_PDF + prefissoBeneficiario,
					pdfDataModel.readProperty(Constants.CODICE_FISCALE_BASE_FIELD_NAME_PDF + suffissoBeneficiario));
			beneficiarioModel.addProperty(Constants.COMUNE_NASCITA_BASE_FIELD_NAME_PDF + prefissoBeneficiario,
					pdfDataModel.readProperty(Constants.COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffissoBeneficiario));
			beneficiarioModel.addProperty(Constants.DATA_NASCITA_BASE_FIELD_NAME_PDF + prefissoBeneficiario,
					pdfDataModel.readProperty(Constants.DATA_NASCITA_BASE_FIELD_NAME_PDF + suffissoBeneficiario));
			beneficiarioModel.addProperty(Constants.PROVINCIA_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + prefissoBeneficiario,
					pdfDataModel.readProperty(
							Constants.PROVINCIA_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffissoBeneficiario));
			beneficiarioModel.addProperty(Constants.NAZIONE_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + prefissoBeneficiario,
					pdfDataModel
							.readProperty(Constants.NAZIONE_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffissoBeneficiario));
			beneficiarioModel.addProperty(Constants.SESSO_BASE_FIELD_NAME_PDF + prefissoBeneficiario,
					pdfDataModel.readProperty(Constants.SESSO_BASE_FIELD_NAME_PDF + suffissoBeneficiario));

			// relazioni

			beneficiarioModel.addProperty(
					Constants.TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + prefissoBeneficiario,
					pdfDataModel.readProperty(
							Constants.TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario));
			beneficiarioModel.addProperty(
					Constants.DESCR_TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + prefissoBeneficiario,
					pdfDataModel.readProperty(
							Constants.DESCR_TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario));
			if (assicurando) {
				StringType tipoRelazioneAssicurando = (StringType)pdfDataModel.readProperty(Constants.TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
				if (tipoRelazioneAssicurando != null) {
					beneficiarioModel.addProperty(
							Constants.TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + prefissoBeneficiario,
							tipoRelazioneAssicurando);
					beneficiarioModel.addProperty(
							Constants.DESCR_TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + prefissoBeneficiario,
							pdfDataModel.readProperty(
									Constants.DESCR_TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + suffissoBeneficiario));
				}
			}

			StringType isPep = (StringType)pdfDataModel.readProperty(Constants.IS_PEP_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
			if (isPep == null) {
				// pep; qui mi basta gestire la motivazione perchè sono nel caso beneficiario
				// nominativo
				beneficiarioModel.addProperty(Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP + prefissoBeneficiario,
												pdfDataModel.readProperty(Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoBeneficiario));
			} else {
				//Sono nella gestione con il flag 
				beneficiarioModel.addProperty(Constants.IS_PEP_BASE_FIELD_NAME_PDF + prefissoBeneficiario, isPep);
				StringType indiceMotivazionePep = (StringType)pdfDataModel.readProperty(Constants.INDICE_MOTIVAZIONE_PEP_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
				beneficiarioModel.addProperty(Constants.INDICE_MOTIVAZIONE_PEP_BASE_FIELD_NAME_PDF + prefissoBeneficiario, indiceMotivazionePep);
				//la motivazione la compatto a parte perchè con il flag non c'è la correlazione tra indice beneficiario e indice motivazione pep
			}

		} else {
			// dati anagrafici
			beneficiarioModel.addProperty(Constants.RAGIONE_SOCIALE_BASE_FIELD_NAME_PDF + prefissoBeneficiario,
					pdfDataModel.readProperty(Constants.RAGIONE_SOCIALE_BASE_FIELD_NAME_PDF + suffissoBeneficiario));
			beneficiarioModel.addProperty(Constants.CODICE_FISCALE_PIVA_BASE_FIELD_NAME_PDF + prefissoBeneficiario,
					pdfDataModel
							.readProperty(Constants.CODICE_FISCALE_PIVA_BASE_FIELD_NAME_PDF + suffissoBeneficiario));
			beneficiarioModel.addProperty(Constants.NUMERO_ISCRIZIONE_CCIAA_BASE_FIELD_NAME_PDF + prefissoBeneficiario,
					pdfDataModel.readProperty(
							Constants.NUMERO_ISCRIZIONE_CCIAA_BASE_FIELD_NAME_PDF + suffissoBeneficiario));
			beneficiarioModel.addProperty(Constants.DATA_ISCRIZIONE_CCIAA_BASE_FIELD_NAME_PDF + prefissoBeneficiario,
					pdfDataModel
							.readProperty(Constants.DATA_ISCRIZIONE_CCIAA_BASE_FIELD_NAME_PDF + suffissoBeneficiario));
			beneficiarioModel.addProperty(
					Constants.PROVINCIA_ISCRIZIONE_CCIAA_BASE_FIELD_NAME_PDF + prefissoBeneficiario,
					pdfDataModel.readProperty(
							Constants.PROVINCIA_ISCRIZIONE_CCIAA_BASE_FIELD_NAME_PDF + suffissoBeneficiario));

			// ciclo sui titolari
			for (int j = 1;; j++) {
				String suffissoTitolare = Constants.TITOLARE + j + prefissoBeneficiario + indiceBeneficiario;

				if (pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare) == null) {
					break;
				}

				beneficiarioModel.addProperty(
						Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j + prefissoBeneficiario,
						pdfDataModel.readProperty(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare));
				beneficiarioModel.addProperty(
						Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j + prefissoBeneficiario,
						pdfDataModel.readProperty(Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare));

				// dati anagrafici
				beneficiarioModel.addProperty(
						Constants.NOME_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j + prefissoBeneficiario,
						pdfDataModel.readProperty(Constants.NOME_BASE_FIELD_NAME_PDF + suffissoTitolare));
				beneficiarioModel.addProperty(
						Constants.COGNOME_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j + prefissoBeneficiario,
						pdfDataModel.readProperty(Constants.COGNOME_BASE_FIELD_NAME_PDF + suffissoTitolare));
				beneficiarioModel.addProperty(
						Constants.CODICE_FISCALE_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j + prefissoBeneficiario,
						pdfDataModel.readProperty(Constants.CODICE_FISCALE_BASE_FIELD_NAME_PDF + suffissoTitolare));
				beneficiarioModel.addProperty(
						Constants.COMUNE_NASCITA_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j + prefissoBeneficiario,
						pdfDataModel.readProperty(Constants.COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffissoTitolare));
				beneficiarioModel.addProperty(
						Constants.DATA_NASCITA_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j + prefissoBeneficiario,
						pdfDataModel.readProperty(Constants.DATA_NASCITA_BASE_FIELD_NAME_PDF + suffissoTitolare));
				beneficiarioModel.addProperty(
						Constants.PROVINCIA_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j
								+ prefissoBeneficiario,
						pdfDataModel.readProperty(
								Constants.PROVINCIA_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffissoTitolare));
				beneficiarioModel.addProperty(
						Constants.NAZIONE_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j
								+ prefissoBeneficiario,
						pdfDataModel
								.readProperty(Constants.NAZIONE_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffissoTitolare));
				beneficiarioModel.addProperty(
						Constants.SESSO_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j + prefissoBeneficiario,
						pdfDataModel.readProperty(Constants.SESSO_BASE_FIELD_NAME_PDF + suffissoTitolare));

				// Indirizzo
				beneficiarioModel.addProperty(
						Constants.COD_TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j
								+ prefissoBeneficiario,
						pdfDataModel.readProperty(Constants.COD_TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoTitolare));
				beneficiarioModel.addProperty(
						Constants.TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j
								+ prefissoBeneficiario,
						pdfDataModel.readProperty(Constants.TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoTitolare));
				beneficiarioModel.addProperty(
						Constants.INDIRIZZO_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j + prefissoBeneficiario,
						pdfDataModel.readProperty(Constants.INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoTitolare));
				beneficiarioModel.addProperty(
						Constants.NUMERO_CIVICO_INDIRIZZO_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j
								+ prefissoBeneficiario,
						pdfDataModel.readProperty(
								Constants.NUMERO_CIVICO_INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoTitolare));
				beneficiarioModel.addProperty(
						Constants.CAP_COMUNE_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j + prefissoBeneficiario,
						pdfDataModel.readProperty(Constants.CAP_COMUNE_BASE_FIELD_NAME_PDF + suffissoTitolare));
				beneficiarioModel.addProperty(
						Constants.COMUNE_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j + prefissoBeneficiario,
						pdfDataModel.readProperty(Constants.COMUNE_BASE_FIELD_NAME_PDF + suffissoTitolare));
				beneficiarioModel.addProperty(
						Constants.PROVINCIA_COMUNE_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j + prefissoBeneficiario,
						pdfDataModel.readProperty(Constants.PROVINCIA_COMUNE_BASE_FIELD_NAME_PDF + suffissoTitolare));
				beneficiarioModel.addProperty(
						Constants.NAZIONE_COMUNE_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j + prefissoBeneficiario,
						pdfDataModel.readProperty(Constants.NAZIONE_COMUNE_BASE_FIELD_NAME_PDF + suffissoTitolare));

				// recapiti
				beneficiarioModel.addProperty(
						Constants.PREFISSO_INTERNAZIONALE_TEL_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j
								+ prefissoBeneficiario,
						pdfDataModel.readProperty(
								Constants.PREFISSO_INTERNAZIONALE_TEL_BASE_FIELD_NAME_PDF + suffissoTitolare));
				beneficiarioModel.addProperty(
						Constants.PREFISSO_TEL_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j + prefissoBeneficiario,
						pdfDataModel.readProperty(Constants.PREFISSO_TEL_BASE_FIELD_NAME_PDF + suffissoTitolare));
				beneficiarioModel.addProperty(
						Constants.NUMERO_TEL_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j + prefissoBeneficiario,
						pdfDataModel.readProperty(Constants.NUMERO_TEL_BASE_FIELD_NAME_PDF + suffissoTitolare));
				beneficiarioModel.addProperty(
						Constants.TIPO_RECAPITO_TEL_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j + prefissoBeneficiario,
						pdfDataModel.readProperty(Constants.TIPO_RECAPITO_TEL_BASE_FIELD_NAME_PDF + suffissoTitolare));
				beneficiarioModel.addProperty(
						Constants.EMAIL_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j + prefissoBeneficiario,
						pdfDataModel.readProperty(Constants.EMAIL_BASE_FIELD_NAME_PDF + suffissoTitolare));

				// relazioni

				beneficiarioModel.addProperty(
						Constants.TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j
								+ prefissoBeneficiario,
						pdfDataModel.readProperty(
								Constants.TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + suffissoTitolare));
				beneficiarioModel.addProperty(
						Constants.DESCR_TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j
								+ prefissoBeneficiario,
						pdfDataModel.readProperty(
								Constants.DESCR_TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + suffissoTitolare));
				if (assicurando) {
					StringType tipoRelazioneAssicurando = (StringType)pdfDataModel.readProperty(Constants.TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + suffissoTitolare);
					if (tipoRelazioneAssicurando != null) {
						beneficiarioModel.addProperty(
								Constants.TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j + prefissoBeneficiario, tipoRelazioneAssicurando);
						beneficiarioModel.addProperty(
								Constants.DESCR_TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j + prefissoBeneficiario,
								pdfDataModel.readProperty(Constants.DESCR_TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + suffissoTitolare));
					}
				}

				StringType isPep = (StringType)pdfDataModel.readProperty(Constants.IS_PEP_BASE_FIELD_NAME_PDF + suffissoTitolare);
				if (isPep != null) {
					beneficiarioModel.addProperty(Constants.IS_PEP_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j + prefissoBeneficiario, isPep);
				}
				// pep; qui mi basta gestire la motivazione perchè sono nel caso beneficiario
				// nominativo
				beneficiarioModel.addProperty(
						Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP + Constants.TITOLARE + j
								+ prefissoBeneficiario,
						pdfDataModel.readProperty(
								Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoTitolare));
				
				StringType cognomePepTitolare = (StringType)pdfDataModel.readProperty(Constants.COGNOME_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoTitolare);
				if (cognomePepTitolare != null) {
					beneficiarioModel.addProperty(Constants.COGNOME_BASE_FIELD_NAME_PDF + Constants.PEP + Constants.TITOLARE + j + prefissoBeneficiario, cognomePepTitolare);
				}
				StringType nomePepTitolare = (StringType)pdfDataModel.readProperty(Constants.NOME_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoTitolare);
				if (nomePepTitolare != null) {
					beneficiarioModel.addProperty(Constants.NOME_BASE_FIELD_NAME_PDF + Constants.PEP + Constants.TITOLARE + j + prefissoBeneficiario, nomePepTitolare);
				}
			}
		}
		return beneficiarioModel;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void beneficiarioToPdf(MapCommandDataModel beneficiarioModel, PdfDataModel pdfDataModel,
			boolean assicurando, boolean isPersonaFisica, String prefissoBeneficiario, int indiceBeneficiario) {

		String suffissoBeneficiario = prefissoBeneficiario + indiceBeneficiario;

		pdfDataModel.addProperty(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
				beneficiarioModel.readProperty(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + prefissoBeneficiario));
		pdfDataModel.addProperty(Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
				beneficiarioModel.readProperty(Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF + prefissoBeneficiario));

		// Indirizzo
		pdfDataModel.addProperty(Constants.COD_TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
				beneficiarioModel
						.readProperty(Constants.COD_TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF + prefissoBeneficiario));
		pdfDataModel.addProperty(Constants.TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
				beneficiarioModel
						.readProperty(Constants.TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF + prefissoBeneficiario));
		pdfDataModel.addProperty(Constants.INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
				beneficiarioModel.readProperty(Constants.INDIRIZZO_BASE_FIELD_NAME_PDF + prefissoBeneficiario));
		pdfDataModel.addProperty(Constants.NUMERO_CIVICO_INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
				beneficiarioModel
						.readProperty(Constants.NUMERO_CIVICO_INDIRIZZO_BASE_FIELD_NAME_PDF + prefissoBeneficiario));
		pdfDataModel.addProperty(Constants.CAP_COMUNE_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
				beneficiarioModel.readProperty(Constants.CAP_COMUNE_BASE_FIELD_NAME_PDF + prefissoBeneficiario));
		pdfDataModel.addProperty(Constants.COMUNE_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
				beneficiarioModel.readProperty(Constants.COMUNE_BASE_FIELD_NAME_PDF + prefissoBeneficiario));
		pdfDataModel.addProperty(Constants.PROVINCIA_COMUNE_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
				beneficiarioModel.readProperty(Constants.PROVINCIA_COMUNE_BASE_FIELD_NAME_PDF + prefissoBeneficiario));
		pdfDataModel.addProperty(Constants.NAZIONE_COMUNE_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
				beneficiarioModel.readProperty(Constants.NAZIONE_COMUNE_BASE_FIELD_NAME_PDF + prefissoBeneficiario));

		// recapiti
		pdfDataModel.addProperty(Constants.PREFISSO_INTERNAZIONALE_TEL_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
				beneficiarioModel.readProperty(
						Constants.PREFISSO_INTERNAZIONALE_TEL_BASE_FIELD_NAME_PDF + prefissoBeneficiario));
		pdfDataModel.addProperty(Constants.PREFISSO_TEL_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
				beneficiarioModel.readProperty(Constants.PREFISSO_TEL_BASE_FIELD_NAME_PDF + prefissoBeneficiario));
		pdfDataModel.addProperty(Constants.NUMERO_TEL_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
				beneficiarioModel.readProperty(Constants.NUMERO_TEL_BASE_FIELD_NAME_PDF + prefissoBeneficiario));
		pdfDataModel.addProperty(Constants.TIPO_RECAPITO_TEL_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
				beneficiarioModel.readProperty(Constants.TIPO_RECAPITO_TEL_BASE_FIELD_NAME_PDF + prefissoBeneficiario));
		pdfDataModel.addProperty(Constants.EMAIL_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
				beneficiarioModel.readProperty(Constants.EMAIL_BASE_FIELD_NAME_PDF + prefissoBeneficiario));

		// percentuale
		AbstractType percentuale = beneficiarioModel.readProperty(Constants.PERCENTUALE_BASE_FIELD_NAME_PDF + prefissoBeneficiario);
		if (percentuale != null) {
			pdfDataModel.addProperty(Constants.PERCENTUALE_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
					beneficiarioModel.readProperty(Constants.PERCENTUALE_BASE_FIELD_NAME_PDF + prefissoBeneficiario));
		}

		if (isPersonaFisica) {
			// dati anagrafici
			pdfDataModel.addProperty(Constants.NOME_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
					beneficiarioModel.readProperty(Constants.NOME_BASE_FIELD_NAME_PDF + prefissoBeneficiario));
			pdfDataModel.addProperty(Constants.COGNOME_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
					beneficiarioModel.readProperty(Constants.COGNOME_BASE_FIELD_NAME_PDF + prefissoBeneficiario));
			pdfDataModel.addProperty(Constants.CODICE_FISCALE_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
					beneficiarioModel
							.readProperty(Constants.CODICE_FISCALE_BASE_FIELD_NAME_PDF + prefissoBeneficiario));
			pdfDataModel.addProperty(Constants.COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
					beneficiarioModel
							.readProperty(Constants.COMUNE_NASCITA_BASE_FIELD_NAME_PDF + prefissoBeneficiario));
			pdfDataModel.addProperty(Constants.DATA_NASCITA_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
					beneficiarioModel.readProperty(Constants.DATA_NASCITA_BASE_FIELD_NAME_PDF + prefissoBeneficiario));
			pdfDataModel.addProperty(Constants.PROVINCIA_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
					beneficiarioModel.readProperty(
							Constants.PROVINCIA_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + prefissoBeneficiario));
			pdfDataModel.addProperty(Constants.NAZIONE_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
					beneficiarioModel
							.readProperty(Constants.NAZIONE_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + prefissoBeneficiario));
			pdfDataModel.addProperty(Constants.SESSO_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
					beneficiarioModel.readProperty(Constants.SESSO_BASE_FIELD_NAME_PDF + prefissoBeneficiario));

			// relazioni

			pdfDataModel.addProperty(Constants.TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
					beneficiarioModel.readProperty(
							Constants.TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + prefissoBeneficiario));
			pdfDataModel.addProperty(
					Constants.DESCR_TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
					beneficiarioModel.readProperty(
							Constants.DESCR_TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + prefissoBeneficiario));
			if (assicurando) {
				StringType tipoRelazioneAssicurando = (StringType)beneficiarioModel.readProperty(Constants.TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + prefissoBeneficiario);
				if (tipoRelazioneAssicurando != null) {
					pdfDataModel.addProperty(
							Constants.TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
							tipoRelazioneAssicurando);
					pdfDataModel.addProperty(
							Constants.DESCR_TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
							beneficiarioModel.readProperty(Constants.DESCR_TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + prefissoBeneficiario));
				}
			}

			StringType isPep = (StringType)beneficiarioModel.readProperty(Constants.IS_PEP_BASE_FIELD_NAME_PDF + prefissoBeneficiario);
			if (isPep == null) {
				// pep; qui mi basta gestire la motivazione perchè sono nel caso beneficiario
				// nominativo
				pdfDataModel.addProperty(Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoBeneficiario,
						beneficiarioModel.readProperty(
								Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP + prefissoBeneficiario));
			} else {
				//Sono nella gestione con il flag 
				pdfDataModel.addProperty(
						Constants.IS_PEP_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
						isPep);
				StringType indiceMotivazionePep = (StringType)beneficiarioModel.readProperty(Constants.INDICE_MOTIVAZIONE_PEP_BASE_FIELD_NAME_PDF + prefissoBeneficiario);
				if (indiceMotivazionePep != null) {
					pdfDataModel.addProperty(Constants.INDICE_MOTIVAZIONE_PEP_BASE_FIELD_NAME_PDF + suffissoBeneficiario, indiceMotivazionePep);
				}
				//la motivazione la compatto a parte perchè con il flag non c'è la correlazione tra indice beneficiario e indice motivazione pep
			}

		} else {
			// dati anagrafici
			pdfDataModel.addProperty(Constants.RAGIONE_SOCIALE_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
					beneficiarioModel
							.readProperty(Constants.RAGIONE_SOCIALE_BASE_FIELD_NAME_PDF + prefissoBeneficiario));
			pdfDataModel.addProperty(Constants.CODICE_FISCALE_PIVA_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
					beneficiarioModel
							.readProperty(Constants.CODICE_FISCALE_PIVA_BASE_FIELD_NAME_PDF + prefissoBeneficiario));
			pdfDataModel.addProperty(Constants.NUMERO_ISCRIZIONE_CCIAA_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
					beneficiarioModel.readProperty(
							Constants.NUMERO_ISCRIZIONE_CCIAA_BASE_FIELD_NAME_PDF + prefissoBeneficiario));
			pdfDataModel.addProperty(Constants.DATA_ISCRIZIONE_CCIAA_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
					beneficiarioModel
							.readProperty(Constants.DATA_ISCRIZIONE_CCIAA_BASE_FIELD_NAME_PDF + prefissoBeneficiario));
			pdfDataModel.addProperty(Constants.PROVINCIA_ISCRIZIONE_CCIAA_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
					beneficiarioModel.readProperty(
							Constants.PROVINCIA_ISCRIZIONE_CCIAA_BASE_FIELD_NAME_PDF + prefissoBeneficiario));

			// ciclo sui titolari
			for (int j = 1;; j++) {
				String suffissoTitolare = Constants.TITOLARE + j + prefissoBeneficiario + indiceBeneficiario;

				if (pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare) == null) {
					break;
				}

				pdfDataModel.addProperty(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare,
						beneficiarioModel.readProperty(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + Constants.TITOLARE
								+ j + prefissoBeneficiario));
				pdfDataModel.addProperty(Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare,
						beneficiarioModel.readProperty(Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF + Constants.TITOLARE
								+ j + prefissoBeneficiario));

				// dati anagrafici
				pdfDataModel.addProperty(Constants.NOME_BASE_FIELD_NAME_PDF + suffissoTitolare,
						beneficiarioModel.readProperty(
								Constants.NOME_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j + prefissoBeneficiario));
				pdfDataModel.addProperty(Constants.COGNOME_BASE_FIELD_NAME_PDF + suffissoTitolare,
						beneficiarioModel.readProperty(
								Constants.COGNOME_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j + prefissoBeneficiario));
				pdfDataModel.addProperty(Constants.CODICE_FISCALE_BASE_FIELD_NAME_PDF + suffissoTitolare,
						beneficiarioModel.readProperty(Constants.CODICE_FISCALE_BASE_FIELD_NAME_PDF + Constants.TITOLARE
								+ j + prefissoBeneficiario));
				pdfDataModel.addProperty(Constants.COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffissoTitolare,
						beneficiarioModel.readProperty(Constants.COMUNE_NASCITA_BASE_FIELD_NAME_PDF + Constants.TITOLARE
								+ j + prefissoBeneficiario));
				pdfDataModel.addProperty(Constants.DATA_NASCITA_BASE_FIELD_NAME_PDF + suffissoTitolare,
						beneficiarioModel.readProperty(Constants.DATA_NASCITA_BASE_FIELD_NAME_PDF + Constants.TITOLARE
								+ j + prefissoBeneficiario));
				pdfDataModel.addProperty(Constants.PROVINCIA_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffissoTitolare,
						beneficiarioModel.readProperty(Constants.PROVINCIA_COMUNE_NASCITA_BASE_FIELD_NAME_PDF
								+ Constants.TITOLARE + j + prefissoBeneficiario));
				pdfDataModel.addProperty(Constants.NAZIONE_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffissoTitolare,
						beneficiarioModel.readProperty(Constants.NAZIONE_COMUNE_NASCITA_BASE_FIELD_NAME_PDF
								+ Constants.TITOLARE + j + prefissoBeneficiario));
				pdfDataModel.addProperty(Constants.SESSO_BASE_FIELD_NAME_PDF + suffissoTitolare,
						beneficiarioModel.readProperty(
								Constants.SESSO_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j + prefissoBeneficiario));

				// Indirizzo
				pdfDataModel.addProperty(Constants.COD_TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoTitolare,
						beneficiarioModel.readProperty(Constants.COD_TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF
								+ Constants.TITOLARE + j + prefissoBeneficiario));
				pdfDataModel.addProperty(Constants.TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoTitolare,
						beneficiarioModel.readProperty(Constants.TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF
								+ Constants.TITOLARE + j + prefissoBeneficiario));
				pdfDataModel.addProperty(Constants.INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoTitolare,
						beneficiarioModel.readProperty(Constants.INDIRIZZO_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j
								+ prefissoBeneficiario));
				pdfDataModel.addProperty(Constants.NUMERO_CIVICO_INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoTitolare,
						beneficiarioModel.readProperty(Constants.NUMERO_CIVICO_INDIRIZZO_BASE_FIELD_NAME_PDF
								+ Constants.TITOLARE + j + prefissoBeneficiario));
				pdfDataModel.addProperty(Constants.CAP_COMUNE_BASE_FIELD_NAME_PDF + suffissoTitolare,
						beneficiarioModel.readProperty(Constants.CAP_COMUNE_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j
								+ prefissoBeneficiario));
				pdfDataModel.addProperty(Constants.COMUNE_BASE_FIELD_NAME_PDF + suffissoTitolare,
						beneficiarioModel.readProperty(
								Constants.COMUNE_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j + prefissoBeneficiario));
				pdfDataModel.addProperty(Constants.PROVINCIA_COMUNE_BASE_FIELD_NAME_PDF + suffissoTitolare,
						beneficiarioModel.readProperty(Constants.PROVINCIA_COMUNE_BASE_FIELD_NAME_PDF
								+ Constants.TITOLARE + j + prefissoBeneficiario));
				pdfDataModel.addProperty(Constants.NAZIONE_COMUNE_BASE_FIELD_NAME_PDF + suffissoTitolare,
						beneficiarioModel.readProperty(Constants.NAZIONE_COMUNE_BASE_FIELD_NAME_PDF + Constants.TITOLARE
								+ j + prefissoBeneficiario));

				// recapiti
				pdfDataModel.addProperty(Constants.PREFISSO_INTERNAZIONALE_TEL_BASE_FIELD_NAME_PDF + suffissoTitolare,
						beneficiarioModel.readProperty(Constants.PREFISSO_INTERNAZIONALE_TEL_BASE_FIELD_NAME_PDF
								+ Constants.TITOLARE + j + prefissoBeneficiario));
				pdfDataModel.addProperty(Constants.PREFISSO_TEL_BASE_FIELD_NAME_PDF + suffissoTitolare,
						beneficiarioModel.readProperty(Constants.PREFISSO_TEL_BASE_FIELD_NAME_PDF + Constants.TITOLARE
								+ j + prefissoBeneficiario));
				pdfDataModel.addProperty(Constants.NUMERO_TEL_BASE_FIELD_NAME_PDF + suffissoTitolare,
						beneficiarioModel.readProperty(Constants.NUMERO_TEL_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j
								+ prefissoBeneficiario));
				pdfDataModel.addProperty(Constants.TIPO_RECAPITO_TEL_BASE_FIELD_NAME_PDF + suffissoTitolare,
						beneficiarioModel.readProperty(Constants.TIPO_RECAPITO_TEL_BASE_FIELD_NAME_PDF
								+ Constants.TITOLARE + j + prefissoBeneficiario));
				pdfDataModel.addProperty(Constants.EMAIL_BASE_FIELD_NAME_PDF + suffissoTitolare,
						beneficiarioModel.readProperty(
								Constants.EMAIL_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j + prefissoBeneficiario));

				// relazioni

				pdfDataModel.addProperty(Constants.TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + suffissoTitolare,
						beneficiarioModel.readProperty(Constants.TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF
								+ Constants.TITOLARE + j + prefissoBeneficiario));
				pdfDataModel.addProperty(
						Constants.DESCR_TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + suffissoTitolare,
						beneficiarioModel.readProperty(Constants.DESCR_TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF
								+ Constants.TITOLARE + j + prefissoBeneficiario));
				if (assicurando) {
					StringType tipoRelazioneAssicurando = (StringType)beneficiarioModel.readProperty(Constants.TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j + prefissoBeneficiario);
					if (tipoRelazioneAssicurando != null) {
						pdfDataModel.addProperty(
								Constants.TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + suffissoTitolare,
								tipoRelazioneAssicurando);
						pdfDataModel.addProperty(
								Constants.DESCR_TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + suffissoTitolare,
								beneficiarioModel.readProperty(Constants.DESCR_TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF+ Constants.TITOLARE + j + prefissoBeneficiario));
					}
				}
				
				StringType isPep = (StringType)beneficiarioModel.readProperty(Constants.IS_PEP_BASE_FIELD_NAME_PDF + Constants.TITOLARE + j + prefissoBeneficiario);
				if (isPep != null) {
					pdfDataModel.addProperty(
							Constants.IS_PEP_BASE_FIELD_NAME_PDF + suffissoTitolare,
							isPep);
				}

				// pep; qui mi basta gestire la motivazione perchè sono nel caso beneficiario
				// nominativo
				pdfDataModel.addProperty(Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoTitolare,
						beneficiarioModel.readProperty(Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP
								+ Constants.TITOLARE + j + prefissoBeneficiario));

			}
		}

	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void clearBeneficiario(PdfDataModel pdfDataModel, boolean assicurando, boolean isPersonaFisica,
			String prefissoBeneficiario, int indiceBeneficiario) {

		String suffissoBeneficiario = prefissoBeneficiario + indiceBeneficiario;

		pdfDataModel.addProperty(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario, new StringType());
		pdfDataModel.addProperty(Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario, new StringType());

		// Indirizzo
		pdfDataModel.addProperty(Constants.COD_TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
				new StringType());
		pdfDataModel.addProperty(Constants.TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
				new StringType());
		pdfDataModel.addProperty(Constants.INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoBeneficiario, new StringType());
		pdfDataModel.addProperty(Constants.NUMERO_CIVICO_INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
				new StringType());
		pdfDataModel.addProperty(Constants.CAP_COMUNE_BASE_FIELD_NAME_PDF + suffissoBeneficiario, new StringType());
		pdfDataModel.addProperty(Constants.COMUNE_BASE_FIELD_NAME_PDF + suffissoBeneficiario, new StringType());
		pdfDataModel.addProperty(Constants.PROVINCIA_COMUNE_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
				new StringType());
		pdfDataModel.addProperty(Constants.NAZIONE_COMUNE_BASE_FIELD_NAME_PDF + suffissoBeneficiario, new StringType());

		// recapiti
		pdfDataModel.addProperty(Constants.PREFISSO_INTERNAZIONALE_TEL_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
				new StringType());
		pdfDataModel.addProperty(Constants.PREFISSO_TEL_BASE_FIELD_NAME_PDF + suffissoBeneficiario, new StringType());
		pdfDataModel.addProperty(Constants.NUMERO_TEL_BASE_FIELD_NAME_PDF + suffissoBeneficiario, new StringType());
		pdfDataModel.addProperty(Constants.TIPO_RECAPITO_TEL_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
				new StringType());
		pdfDataModel.addProperty(Constants.EMAIL_BASE_FIELD_NAME_PDF + suffissoBeneficiario, new StringType());

		// percentuale
		//Se non c'è non devo crearla
		DoubleType percentuale = (DoubleType)pdfDataModel.readProperty(Constants.PERCENTUALE_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
		if (percentuale != null)
			pdfDataModel.addProperty(Constants.PERCENTUALE_BASE_FIELD_NAME_PDF + suffissoBeneficiario, new DoubleType());
		
		if (isPersonaFisica) {
			// dati anagrafici
			pdfDataModel.addProperty(Constants.NOME_BASE_FIELD_NAME_PDF + suffissoBeneficiario, new StringType());
			pdfDataModel.addProperty(Constants.COGNOME_BASE_FIELD_NAME_PDF + suffissoBeneficiario, new StringType());
			pdfDataModel.addProperty(Constants.CODICE_FISCALE_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
					new StringType());
			pdfDataModel.addProperty(Constants.COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
					new StringType());
			pdfDataModel.addProperty(Constants.DATA_NASCITA_BASE_FIELD_NAME_PDF + suffissoBeneficiario, new DateType());
			pdfDataModel.addProperty(Constants.PROVINCIA_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
					new StringType());
			pdfDataModel.addProperty(Constants.NAZIONE_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
					new StringType());
			pdfDataModel.addProperty(Constants.SESSO_BASE_FIELD_NAME_PDF + suffissoBeneficiario, new StringType());

			// relazioni

			pdfDataModel.addProperty(Constants.TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
					new StringType());
			pdfDataModel.addProperty(
					Constants.DESCR_TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
					new StringType());
			if (assicurando) {
				//Se non c'è non devo crearlo
				StringType tipoRelazioneAssicurando = (StringType)pdfDataModel.readProperty(Constants.TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
				if (tipoRelazioneAssicurando != null) {
					pdfDataModel.addProperty(Constants.TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + suffissoBeneficiario, new StringType());
					pdfDataModel.addProperty(Constants.DESCR_TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + suffissoBeneficiario, new StringType());
				}
			}
			
			//Se non c'è non devo crearlo
			StringType isPep = (StringType)pdfDataModel.readProperty(Constants.IS_PEP_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
			if (isPep == null) {
				// pep; qui mi basta gestire la motivazione perchè sono nel caso beneficiario
				// nominativo
				pdfDataModel.addProperty(Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoBeneficiario, new StringType());
			} else {
				pdfDataModel.addProperty(Constants.IS_PEP_BASE_FIELD_NAME_PDF + suffissoBeneficiario, new StringType());
				pdfDataModel.addProperty(Constants.INDICE_MOTIVAZIONE_PEP_BASE_FIELD_NAME_PDF + suffissoBeneficiario, new StringType());
				//la motivazione la compatto a parte perchè con il flag non c'è la correlazione tra indice beneficiario e indice motivazione pep
			}

		} else {
			// dati anagrafici
			pdfDataModel.addProperty(Constants.RAGIONE_SOCIALE_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
					new StringType());
			pdfDataModel.addProperty(Constants.CODICE_FISCALE_PIVA_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
					new StringType());
			pdfDataModel.addProperty(Constants.NUMERO_ISCRIZIONE_CCIAA_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
					new StringType());
			pdfDataModel.addProperty(Constants.DATA_ISCRIZIONE_CCIAA_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
					new DateType());
			pdfDataModel.addProperty(Constants.PROVINCIA_ISCRIZIONE_CCIAA_BASE_FIELD_NAME_PDF + suffissoBeneficiario,
					new StringType());

			// ciclo sui titolari
			for (int j = 1;; j++) {
				String suffissoTitolare = Constants.TITOLARE + j + prefissoBeneficiario + indiceBeneficiario;

				if (pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare) == null) {
					break;
				}

				pdfDataModel.addProperty(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare,
						new StringType());
				pdfDataModel.addProperty(Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare,
						new StringType());

				// dati anagrafici
				pdfDataModel.addProperty(Constants.NOME_BASE_FIELD_NAME_PDF + suffissoTitolare, new StringType());
				pdfDataModel.addProperty(Constants.COGNOME_BASE_FIELD_NAME_PDF + suffissoTitolare, new StringType());
				pdfDataModel.addProperty(Constants.CODICE_FISCALE_BASE_FIELD_NAME_PDF + suffissoTitolare,
						new StringType());
				pdfDataModel.addProperty(Constants.COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffissoTitolare,
						new StringType());
				pdfDataModel.addProperty(Constants.DATA_NASCITA_BASE_FIELD_NAME_PDF + suffissoTitolare, new DateType());
				pdfDataModel.addProperty(Constants.PROVINCIA_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffissoTitolare,
						new StringType());
				pdfDataModel.addProperty(Constants.NAZIONE_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffissoTitolare,
						new StringType());
				pdfDataModel.addProperty(Constants.SESSO_BASE_FIELD_NAME_PDF + suffissoTitolare, new StringType());

				// Indirizzo
				pdfDataModel.addProperty(Constants.COD_TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoTitolare,
						new StringType());
				pdfDataModel.addProperty(Constants.TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoTitolare,
						new StringType());
				pdfDataModel.addProperty(Constants.INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoTitolare, new StringType());
				pdfDataModel.addProperty(Constants.NUMERO_CIVICO_INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoTitolare,
						new StringType());
				pdfDataModel.addProperty(Constants.CAP_COMUNE_BASE_FIELD_NAME_PDF + suffissoTitolare, new StringType());
				pdfDataModel.addProperty(Constants.COMUNE_BASE_FIELD_NAME_PDF + suffissoTitolare, new StringType());
				pdfDataModel.addProperty(Constants.PROVINCIA_COMUNE_BASE_FIELD_NAME_PDF + suffissoTitolare,
						new StringType());
				pdfDataModel.addProperty(Constants.NAZIONE_COMUNE_BASE_FIELD_NAME_PDF + suffissoTitolare,
						new StringType());

				// recapiti
				pdfDataModel.addProperty(Constants.PREFISSO_INTERNAZIONALE_TEL_BASE_FIELD_NAME_PDF + suffissoTitolare,
						new StringType());
				pdfDataModel.addProperty(Constants.PREFISSO_TEL_BASE_FIELD_NAME_PDF + suffissoTitolare,
						new StringType());
				pdfDataModel.addProperty(Constants.NUMERO_TEL_BASE_FIELD_NAME_PDF + suffissoTitolare, new StringType());
				pdfDataModel.addProperty(Constants.TIPO_RECAPITO_TEL_BASE_FIELD_NAME_PDF + suffissoTitolare,
						new StringType());
				pdfDataModel.addProperty(Constants.EMAIL_BASE_FIELD_NAME_PDF + suffissoTitolare, new StringType());

				// relazioni

				pdfDataModel.addProperty(Constants.TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + suffissoTitolare,
						new StringType());
				pdfDataModel.addProperty(
						Constants.DESCR_TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + suffissoTitolare,
						new StringType());
				if (assicurando) {
					//Se non c'è non devo crearla
					StringType tipoRelazioneAssicurando = (StringType)pdfDataModel.readProperty(Constants.TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + suffissoTitolare);
					if (tipoRelazioneAssicurando != null) {
						pdfDataModel.addProperty(Constants.TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + suffissoTitolare, new StringType());
						pdfDataModel.addProperty(Constants.DESCR_TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + suffissoTitolare, new StringType());
					}
				}

				//Se non c'è non devo crearlo
				StringType isPep = (StringType)pdfDataModel.readProperty(Constants.IS_PEP_BASE_FIELD_NAME_PDF + suffissoTitolare);
				if (isPep != null) {
					pdfDataModel.addProperty(Constants.IS_PEP_BASE_FIELD_NAME_PDF + suffissoTitolare, new StringType());
				}
				
				// pep; qui mi basta gestire la motivazione perchè sono nel caso beneficiario
				// nominativo
				pdfDataModel.addProperty(Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoTitolare,
						new StringType());

			}
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static MapCommandDataModel titolareToModel(PdfDataModel pdfDataModel, boolean assicurando,
			String prefissoBeneficiario, int indiceBeneficiario, int indiceTitolare) {
		MapCommandDataModel titolareModel = new MapCommandDataModel();

		String suffissoTitolare = Constants.TITOLARE + indiceTitolare + prefissoBeneficiario + indiceBeneficiario;

		titolareModel.addProperty(
				Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario,
				pdfDataModel.readProperty(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare));
		titolareModel.addProperty(
				Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario,
				pdfDataModel.readProperty(Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare));

		// dati anagrafici
		titolareModel.addProperty(Constants.NOME_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario,
				pdfDataModel.readProperty(Constants.NOME_BASE_FIELD_NAME_PDF + suffissoTitolare));
		titolareModel.addProperty(Constants.COGNOME_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario,
				pdfDataModel.readProperty(Constants.COGNOME_BASE_FIELD_NAME_PDF + suffissoTitolare));
		titolareModel.addProperty(
				Constants.CODICE_FISCALE_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario,
				pdfDataModel.readProperty(Constants.CODICE_FISCALE_BASE_FIELD_NAME_PDF + suffissoTitolare));
		titolareModel.addProperty(
				Constants.COMUNE_NASCITA_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario,
				pdfDataModel.readProperty(Constants.COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffissoTitolare));
		titolareModel.addProperty(
				Constants.DATA_NASCITA_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario,
				pdfDataModel.readProperty(Constants.DATA_NASCITA_BASE_FIELD_NAME_PDF + suffissoTitolare));
		titolareModel.addProperty(
				Constants.PROVINCIA_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario,
				pdfDataModel.readProperty(Constants.PROVINCIA_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffissoTitolare));
		titolareModel.addProperty(
				Constants.NAZIONE_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario,
				pdfDataModel.readProperty(Constants.NAZIONE_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffissoTitolare));
		titolareModel.addProperty(Constants.SESSO_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario,
				pdfDataModel.readProperty(Constants.SESSO_BASE_FIELD_NAME_PDF + suffissoTitolare));

		// Indirizzo
		titolareModel.addProperty(
				Constants.COD_TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario,
				pdfDataModel.readProperty(Constants.COD_TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoTitolare));
		titolareModel.addProperty(
				Constants.TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario,
				pdfDataModel.readProperty(Constants.TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoTitolare));
		titolareModel.addProperty(Constants.INDIRIZZO_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario,
				pdfDataModel.readProperty(Constants.INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoTitolare));
		titolareModel.addProperty(
				Constants.NUMERO_CIVICO_INDIRIZZO_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario,
				pdfDataModel.readProperty(Constants.NUMERO_CIVICO_INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoTitolare));
		titolareModel.addProperty(Constants.CAP_COMUNE_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario,
				pdfDataModel.readProperty(Constants.CAP_COMUNE_BASE_FIELD_NAME_PDF + suffissoTitolare));
		titolareModel.addProperty(Constants.COMUNE_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario,
				pdfDataModel.readProperty(Constants.COMUNE_BASE_FIELD_NAME_PDF + suffissoTitolare));
		titolareModel.addProperty(
				Constants.PROVINCIA_COMUNE_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario,
				pdfDataModel.readProperty(Constants.PROVINCIA_COMUNE_BASE_FIELD_NAME_PDF + suffissoTitolare));
		titolareModel.addProperty(
				Constants.NAZIONE_COMUNE_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario,
				pdfDataModel.readProperty(Constants.NAZIONE_COMUNE_BASE_FIELD_NAME_PDF + suffissoTitolare));

		// recapiti
		titolareModel.addProperty(
				Constants.PREFISSO_INTERNAZIONALE_TEL_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario,
				pdfDataModel
						.readProperty(Constants.PREFISSO_INTERNAZIONALE_TEL_BASE_FIELD_NAME_PDF + suffissoTitolare));
		titolareModel.addProperty(
				Constants.PREFISSO_TEL_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario,
				pdfDataModel.readProperty(Constants.PREFISSO_TEL_BASE_FIELD_NAME_PDF + suffissoTitolare));
		titolareModel.addProperty(Constants.NUMERO_TEL_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario,
				pdfDataModel.readProperty(Constants.NUMERO_TEL_BASE_FIELD_NAME_PDF + suffissoTitolare));
		titolareModel.addProperty(
				Constants.TIPO_RECAPITO_TEL_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario,
				pdfDataModel.readProperty(Constants.TIPO_RECAPITO_TEL_BASE_FIELD_NAME_PDF + suffissoTitolare));
		titolareModel.addProperty(Constants.EMAIL_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario,
				pdfDataModel.readProperty(Constants.EMAIL_BASE_FIELD_NAME_PDF + suffissoTitolare));

		// relazioni

		titolareModel.addProperty(
				Constants.TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario,
				pdfDataModel.readProperty(Constants.TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + suffissoTitolare));
		titolareModel.addProperty(
				Constants.DESCR_TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + Constants.TITOLARE
						+ prefissoBeneficiario,
				pdfDataModel.readProperty(
						Constants.DESCR_TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + suffissoTitolare));

		if (assicurando) {
			StringType tipoRelazioneAssicurando = (StringType)pdfDataModel.readProperty(Constants.TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + suffissoTitolare);
			if (tipoRelazioneAssicurando != null) {
				titolareModel.addProperty(
						Constants.TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario,
						tipoRelazioneAssicurando);
				titolareModel.addProperty(
						Constants.DESCR_TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + Constants.TITOLARE	+ prefissoBeneficiario,
						pdfDataModel.readProperty(Constants.DESCR_TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + suffissoTitolare));
			}
		}
		
		StringType isPep = (StringType)pdfDataModel.readProperty(Constants.IS_PEP_BASE_FIELD_NAME_PDF + suffissoTitolare);
		if (isPep != null) {
			titolareModel.addProperty(
					Constants.IS_PEP_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario,
					isPep);
		}

		// pep; qui mi basta gestire la motivazione perchè sono nel caso beneficiario
		// nominativo
		titolareModel.addProperty(
				Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP + Constants.TITOLARE + prefissoBeneficiario,
				pdfDataModel
						.readProperty(Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoTitolare));

		return titolareModel;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void titolareToPdf(MapCommandDataModel titolareModel, PdfDataModel pdfDataModel, boolean assicurando,
			String prefissoBeneficiario, int indiceBeneficiario, int indiceTitolare) {

		String suffissoTitolare = Constants.TITOLARE + indiceTitolare + prefissoBeneficiario + indiceBeneficiario;

		pdfDataModel.addProperty(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare,
				titolareModel.readProperty(
						Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario));
		pdfDataModel.addProperty(Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare,
				titolareModel.readProperty(
						Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario));

		// dati anagrafici
		pdfDataModel.addProperty(Constants.NOME_BASE_FIELD_NAME_PDF + suffissoTitolare, titolareModel
				.readProperty(Constants.NOME_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario));
		pdfDataModel.addProperty(Constants.COGNOME_BASE_FIELD_NAME_PDF + suffissoTitolare, titolareModel
				.readProperty(Constants.COGNOME_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario));
		pdfDataModel.addProperty(Constants.CODICE_FISCALE_BASE_FIELD_NAME_PDF + suffissoTitolare,
				titolareModel.readProperty(
						Constants.CODICE_FISCALE_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario));
		pdfDataModel.addProperty(Constants.COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffissoTitolare,
				titolareModel.readProperty(
						Constants.COMUNE_NASCITA_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario));
		pdfDataModel.addProperty(Constants.DATA_NASCITA_BASE_FIELD_NAME_PDF + suffissoTitolare, titolareModel
				.readProperty(Constants.DATA_NASCITA_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario));
		pdfDataModel.addProperty(Constants.PROVINCIA_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffissoTitolare,
				titolareModel.readProperty(Constants.PROVINCIA_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + Constants.TITOLARE
						+ prefissoBeneficiario));
		pdfDataModel.addProperty(Constants.NAZIONE_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffissoTitolare,
				titolareModel.readProperty(Constants.NAZIONE_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + Constants.TITOLARE
						+ prefissoBeneficiario));
		pdfDataModel.addProperty(Constants.SESSO_BASE_FIELD_NAME_PDF + suffissoTitolare, titolareModel
				.readProperty(Constants.SESSO_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario));

		// Indirizzo
		pdfDataModel.addProperty(Constants.COD_TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoTitolare,
				titolareModel.readProperty(
						Constants.COD_TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario));
		pdfDataModel.addProperty(Constants.TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoTitolare,
				titolareModel.readProperty(
						Constants.TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario));
		pdfDataModel.addProperty(Constants.INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoTitolare, titolareModel
				.readProperty(Constants.INDIRIZZO_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario));
		pdfDataModel.addProperty(Constants.NUMERO_CIVICO_INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoTitolare,
				titolareModel.readProperty(Constants.NUMERO_CIVICO_INDIRIZZO_BASE_FIELD_NAME_PDF + Constants.TITOLARE
						+ prefissoBeneficiario));
		pdfDataModel.addProperty(Constants.CAP_COMUNE_BASE_FIELD_NAME_PDF + suffissoTitolare, titolareModel
				.readProperty(Constants.CAP_COMUNE_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario));
		pdfDataModel.addProperty(Constants.COMUNE_BASE_FIELD_NAME_PDF + suffissoTitolare, titolareModel
				.readProperty(Constants.COMUNE_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario));
		pdfDataModel.addProperty(Constants.PROVINCIA_COMUNE_BASE_FIELD_NAME_PDF + suffissoTitolare,
				titolareModel.readProperty(
						Constants.PROVINCIA_COMUNE_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario));
		pdfDataModel.addProperty(Constants.NAZIONE_COMUNE_BASE_FIELD_NAME_PDF + suffissoTitolare,
				titolareModel.readProperty(
						Constants.NAZIONE_COMUNE_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario));

		// recapiti
		pdfDataModel.addProperty(Constants.PREFISSO_INTERNAZIONALE_TEL_BASE_FIELD_NAME_PDF + suffissoTitolare,
				titolareModel.readProperty(Constants.PREFISSO_INTERNAZIONALE_TEL_BASE_FIELD_NAME_PDF
						+ Constants.TITOLARE + prefissoBeneficiario));
		pdfDataModel.addProperty(Constants.PREFISSO_TEL_BASE_FIELD_NAME_PDF + suffissoTitolare, titolareModel
				.readProperty(Constants.PREFISSO_TEL_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario));
		pdfDataModel.addProperty(Constants.NUMERO_TEL_BASE_FIELD_NAME_PDF + suffissoTitolare, titolareModel
				.readProperty(Constants.NUMERO_TEL_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario));
		pdfDataModel.addProperty(Constants.TIPO_RECAPITO_TEL_BASE_FIELD_NAME_PDF + suffissoTitolare,
				titolareModel.readProperty(
						Constants.TIPO_RECAPITO_TEL_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario));
		pdfDataModel.addProperty(Constants.EMAIL_BASE_FIELD_NAME_PDF + suffissoTitolare, titolareModel
				.readProperty(Constants.EMAIL_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario));

		// relazioni

		pdfDataModel.addProperty(Constants.TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + suffissoTitolare,
				titolareModel.readProperty(Constants.TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + Constants.TITOLARE
						+ prefissoBeneficiario));
		pdfDataModel.addProperty(Constants.DESCR_TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + suffissoTitolare,
				titolareModel.readProperty(Constants.DESCR_TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF
						+ Constants.TITOLARE + prefissoBeneficiario));
		if (assicurando) {
			StringType tipoRelazioneAssicurando = (StringType)titolareModel.readProperty(Constants.TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario);
			if (tipoRelazioneAssicurando != null) {
				pdfDataModel.addProperty(
						Constants.TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + suffissoTitolare,
						tipoRelazioneAssicurando);
				pdfDataModel.addProperty(
						Constants.DESCR_TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + suffissoTitolare,
						titolareModel.readProperty(Constants.DESCR_TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario));
			}
		}
		
		StringType isPep = (StringType)titolareModel.readProperty(Constants.IS_PEP_BASE_FIELD_NAME_PDF + Constants.TITOLARE + prefissoBeneficiario);
		if (isPep != null) {
			pdfDataModel.addProperty(
					Constants.IS_PEP_BASE_FIELD_NAME_PDF + suffissoTitolare,
					isPep);
		}

		// pep; qui mi basta gestire la motivazione perchè sono nel caso beneficiario
		// nominativo
		pdfDataModel.addProperty(Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoTitolare,
				titolareModel.readProperty(Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP
						+ Constants.TITOLARE + prefissoBeneficiario));

	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void clearTitolare(PdfDataModel pdfDataModel, boolean assicurando, String prefissoBeneficiario,
			int indiceBeneficiario, int indiceTitolare) {

		String suffissoTitolare = Constants.TITOLARE + indiceTitolare + prefissoBeneficiario + indiceBeneficiario;

		pdfDataModel.addProperty(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare, new StringType());
		pdfDataModel.addProperty(Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare, new StringType());

		// dati anagrafici
		pdfDataModel.addProperty(Constants.NOME_BASE_FIELD_NAME_PDF + suffissoTitolare, new StringType());
		pdfDataModel.addProperty(Constants.COGNOME_BASE_FIELD_NAME_PDF + suffissoTitolare, new StringType());
		pdfDataModel.addProperty(Constants.CODICE_FISCALE_BASE_FIELD_NAME_PDF + suffissoTitolare, new StringType());
		pdfDataModel.addProperty(Constants.COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffissoTitolare, new StringType());
		pdfDataModel.addProperty(Constants.DATA_NASCITA_BASE_FIELD_NAME_PDF + suffissoTitolare, new DateType());
		pdfDataModel.addProperty(Constants.PROVINCIA_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffissoTitolare,
				new StringType());
		pdfDataModel.addProperty(Constants.NAZIONE_COMUNE_NASCITA_BASE_FIELD_NAME_PDF + suffissoTitolare,
				new StringType());
		pdfDataModel.addProperty(Constants.SESSO_BASE_FIELD_NAME_PDF + suffissoTitolare, new StringType());

		// Indirizzo
		pdfDataModel.addProperty(Constants.COD_TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoTitolare, new StringType());
		pdfDataModel.addProperty(Constants.TOPONIMO_INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoTitolare, new StringType());
		pdfDataModel.addProperty(Constants.INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoTitolare, new StringType());
		pdfDataModel.addProperty(Constants.NUMERO_CIVICO_INDIRIZZO_BASE_FIELD_NAME_PDF + suffissoTitolare,
				new StringType());
		pdfDataModel.addProperty(Constants.CAP_COMUNE_BASE_FIELD_NAME_PDF + suffissoTitolare, new StringType());
		pdfDataModel.addProperty(Constants.COMUNE_BASE_FIELD_NAME_PDF + suffissoTitolare, new StringType());
		pdfDataModel.addProperty(Constants.PROVINCIA_COMUNE_BASE_FIELD_NAME_PDF + suffissoTitolare, new StringType());
		pdfDataModel.addProperty(Constants.NAZIONE_COMUNE_BASE_FIELD_NAME_PDF + suffissoTitolare, new StringType());

		// recapiti
		pdfDataModel.addProperty(Constants.PREFISSO_INTERNAZIONALE_TEL_BASE_FIELD_NAME_PDF + suffissoTitolare,
				new StringType());
		pdfDataModel.addProperty(Constants.PREFISSO_TEL_BASE_FIELD_NAME_PDF + suffissoTitolare, new StringType());
		pdfDataModel.addProperty(Constants.NUMERO_TEL_BASE_FIELD_NAME_PDF + suffissoTitolare, new StringType());
		pdfDataModel.addProperty(Constants.TIPO_RECAPITO_TEL_BASE_FIELD_NAME_PDF + suffissoTitolare, new StringType());
		pdfDataModel.addProperty(Constants.EMAIL_BASE_FIELD_NAME_PDF + suffissoTitolare, new StringType());

		// relazioni

		pdfDataModel.addProperty(Constants.TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + suffissoTitolare,
				new StringType());
		pdfDataModel.addProperty(Constants.DESCR_TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + suffissoTitolare,
				new StringType());
		if (assicurando) {
			//Se non c'è non devo crearla
			StringType tipoRelazioneAssicurando = (StringType)pdfDataModel.readProperty(Constants.TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + suffissoTitolare);
			if (tipoRelazioneAssicurando != null) {			
				pdfDataModel.addProperty(Constants.TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + suffissoTitolare, new StringType());
				pdfDataModel.addProperty(Constants.DESCR_TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + suffissoTitolare, new StringType());
			}
		}

		//Se non c'è non devo crearla
		StringType isPep = (StringType)pdfDataModel.readProperty(Constants.IS_PEP_BASE_FIELD_NAME_PDF + suffissoTitolare);
		if (isPep != null) {
			pdfDataModel.addProperty(Constants.IS_PEP_BASE_FIELD_NAME_PDF + suffissoTitolare, new StringType());
		}
		
		// pep; qui mi basta gestire la motivazione perchè sono nel caso beneficiario
		// nominativo
		pdfDataModel.addProperty(Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoTitolare,
				new StringType());

	}

	public static ArrayList<String> getWarningsRelazioni(PdfDataModel pdfDataModel, String prefissoBeneficiario) {
		ArrayList<String> retval = new ArrayList<String>();

		// uso 2 variabili nel caso si vogliano differenziare i messaggi
		boolean relazioneAltroContraenteBeneficiarioPF = false;
		boolean relazioneAltroContraenteBeneficiarioPG = false;

		for (int i = 1;; i++) {
			String suffissoBeneficiario = prefissoBeneficiario + i;
			if (pdfDataModel.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF + suffissoBeneficiario) == null) {
				break;
			}

			boolean isPersonaFisica = true;
			if (((StringType) pdfDataModel.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF + suffissoBeneficiario))
					.equals(FALSE)) {
				isPersonaFisica = false;
			}

			if (isPersonaFisica) {// persona fisica
				StringType tipoRelazioneContraenteBeneficiario = (StringType) pdfDataModel
						.read(Constants.TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario);

				StringType isGiaCliente = (StringType) pdfDataModel
						.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario);

				if (!isGiaCliente.isNull() && isGiaCliente.equals("N") && !tipoRelazioneContraenteBeneficiario.isNull()
						&& tipoRelazioneContraenteBeneficiario.equals(Constants.TIPO_RELAZIONE_ALTRO)) {
					relazioneAltroContraenteBeneficiarioPF = true;
				}

			} else {
				// ciclo sui titolari
				for (int j = 1;; j++) {
					String suffissoTitolare = Constants.TITOLARE + j + prefissoBeneficiario + i;

					if (pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare) == null) {
						break;
					}

					StringType tipoRelazioneContraenteBeneficiario = (StringType) pdfDataModel
							.read(Constants.TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + suffissoTitolare);

					StringType isGiaClienteTitolare = (StringType) pdfDataModel
							.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare);

					if (!isGiaClienteTitolare.isNull() && isGiaClienteTitolare.equals("N")
							&& !tipoRelazioneContraenteBeneficiario.isNull()
							&& tipoRelazioneContraenteBeneficiario.equals(Constants.TIPO_RELAZIONE_ALTRO)) {
						relazioneAltroContraenteBeneficiarioPG = true;
					}

				}

			}

		}

		if (relazioneAltroContraenteBeneficiarioPF || relazioneAltroContraenteBeneficiarioPG) {
			retval.add(Constants.MSG_WARNING_RELAZIONE_ALTRO);
		}

		return retval;

	}
	
	/*
	* 	Nuovo Metodo per evolutiva su MOP che implementa al suo interno i vecchi controlli del metodo getWarningsRelazioni
	* 	e aggiunge i  nuovi
	* 
	*/
	public static ArrayList<String> getWarningsRelazioniAltro(PdfDataModel pdfDataModel, String prefissoBeneficiario) {
		ArrayList<String> retval = new ArrayList<String>();

		boolean relazioneAltroContraenteNoCensitoModuloAV = false;
		boolean relazioneAltroContraenteModuloAV = false;
		boolean relazioneAltroAssicurandoNoCensitoModuloAV = false;
		boolean relazioneAltroAssicurandoModuloAV = false;

		int i=0;
		boolean whileEseguibile = true; 
		while (whileEseguibile) {
			i++;
			whileEseguibile = i < 99;

			String suffissoBeneficiario = prefissoBeneficiario + i;
			if (pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario) == null) {
				break;
			}

			if (pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario).isNull()) {
				continue;
			}

			boolean isPersonaFisica = ((StringType) pdfDataModel.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF + suffissoBeneficiario)).equals("true");

			if (isPersonaFisica) { // persona fisica
				StringType isGiaCliente = (StringType) pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario);

				StringType tipoRelazioneContraenteBeneficiario = (StringType) pdfDataModel.read(Constants.TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
				if (tipoRelazioneContraenteBeneficiario.equals(Constants.TIPO_RELAZIONE_ALTRO)) {
					if (isGiaCliente.equals("N")) {
						relazioneAltroContraenteNoCensitoModuloAV = true;
					} else {
						relazioneAltroContraenteModuloAV = true;
					}
				}

				StringType tipoRelazioneAssicurandoBeneficiario = (StringType) pdfDataModel.read(Constants.TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
				if (tipoRelazioneAssicurandoBeneficiario != null && tipoRelazioneAssicurandoBeneficiario.equals(Constants.TIPO_RELAZIONE_ALTRO)) {
					if (isGiaCliente.equals("N")) {
						relazioneAltroAssicurandoNoCensitoModuloAV = true;
					} else {
						relazioneAltroAssicurandoModuloAV = true;
					}
				}
				
			} else { // Persona giuridica ciclo sui titolari
				int j=0;
				boolean whileTitolariEseguibile = true; 
				while (whileTitolariEseguibile) {
					j++;
					whileTitolariEseguibile = j < 9;

					String suffissoTitolare = Constants.TITOLARE + j + prefissoBeneficiario + i;

					if (pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare) == null) {
						break;
					}

					if (pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare).isNull()) {
						continue;
					}
					
					StringType tipoRelazioneContraenteBeneficiario = (StringType) pdfDataModel.read(Constants.TIPO_RELAZIONE_CONTRAENTE_BASE_FIELD_NAME_PDF + suffissoTitolare);
					StringType isGiaClienteTitolare = (StringType) pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare);

					if (tipoRelazioneContraenteBeneficiario.equals(Constants.TIPO_RELAZIONE_ALTRO)) {
						if (isGiaClienteTitolare.equals("N")) {
							relazioneAltroContraenteNoCensitoModuloAV = true;
						} else {
							relazioneAltroContraenteModuloAV = true;
						}
					}
					
					StringType tipoRelazioneAssicurandoBeneficiario = (StringType) pdfDataModel.read(Constants.TIPO_RELAZIONE_ASSICURANDO_BASE_FIELD_NAME_PDF + suffissoTitolare);
					if (tipoRelazioneAssicurandoBeneficiario != null && tipoRelazioneAssicurandoBeneficiario.equals(Constants.TIPO_RELAZIONE_ALTRO)) {
						if (isGiaClienteTitolare.equals("N")) {
							relazioneAltroAssicurandoNoCensitoModuloAV = true;
						} else {
							relazioneAltroAssicurandoModuloAV = true;
						}
					}
				}
			}
		}

		if (relazioneAltroContraenteNoCensitoModuloAV) 
			retval.add(Constants.MSG_WARNING_RELAZIONE_ALTRO_NO_CENSITO_MODULO_AV);

		if (relazioneAltroContraenteModuloAV) 
			retval.add(Constants.MSG_WARNING_RELAZIONE_ALTRO_MODULO_AV);			
		
		if (relazioneAltroAssicurandoNoCensitoModuloAV)
			retval.add(Constants.MSG_WARNING_RELAZIONE_ASSICURANDO_ALTRO_NO_CENSITO_MODULO_AV);
		
		if (relazioneAltroAssicurandoModuloAV)
			retval.add(Constants.MSG_WARNING_RELAZIONE_ASSICURANDO_ALTRO_MODULO_AV);			
		
		return retval;
	}
	
	public static void buildCodiciProspectAndOtherFields(ClientSessionContext csc, PdfDataModel pdfDataModel,
			String prefissoBeneficiario) throws Exception {
		
		StringType codiceAgente = Utils.getCodiceAgente(pdfDataModel);
		
		
		for (int i = 1;; i++) {
			String suffissoBeneficiario = prefissoBeneficiario + i;
			if (pdfDataModel.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF + suffissoBeneficiario) == null) {
				break;
			}


			StringType isGiaCliente = (StringType) pdfDataModel
					.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario);

			if (!isGiaCliente.isNull()) {
				boolean isPersonaFisica = true;
				if (((StringType) pdfDataModel.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF + suffissoBeneficiario))
						.equals(FALSE)) {
					isPersonaFisica = false;
				}
				
				if (isPersonaFisica) {// persona fisica

					if (isGiaCliente.equals("N")) {
						pdfDataModel.addProperty(Constants.CODICE_PROSPECT_BASE_FIELD_NAME_PDF + suffissoBeneficiario, getCodiceProspect(csc,codiceAgente));
					} else {
						pdfDataModel.addProperty(Constants.CODICE_PROSPECT_BASE_FIELD_NAME_PDF + suffissoBeneficiario, new StringType());
					} 
				} else {
					if (isGiaCliente.equals("N")) {
						pdfDataModel.addProperty(Constants.CODICE_PROSPECT_BASE_FIELD_NAME_PDF + suffissoBeneficiario, getCodiceProspect(csc,codiceAgente));
					} else {
						pdfDataModel.addProperty(Constants.CODICE_PROSPECT_BASE_FIELD_NAME_PDF + suffissoBeneficiario, new StringType());
					}
					
					// ciclo sui titolari
					for (int j = 1;; j++) {
						String suffissoTitolare = Constants.TITOLARE + j + prefissoBeneficiario + i;

						if (pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare) == null) {
							break;
						}

						StringType isGiaClienteTitolare = (StringType) pdfDataModel
								.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare);
						if (!isGiaClienteTitolare.isNull() && isGiaClienteTitolare.equals("N")) {
							pdfDataModel.addProperty(Constants.CODICE_PROSPECT_BASE_FIELD_NAME_PDF + suffissoTitolare, getCodiceProspect(csc,codiceAgente));
						} else {
							pdfDataModel.addProperty(Constants.CODICE_PROSPECT_BASE_FIELD_NAME_PDF + suffissoTitolare, new StringType());
						}
					}
					
				}
				
			} else {
				pdfDataModel.addProperty(Constants.CODICE_PROSPECT_BASE_FIELD_NAME_PDF + suffissoBeneficiario, new StringType());
			} 

		}
	

	}
	
	private static StringType getCodiceProspect(ClientSessionContext csc,  StringType codiceAgente) throws Exception {
		return InrWriterService.getContatoreTabella(csc, codiceAgente.toString(), "ANAGRAFICA_CLIENTI");
	}
	
	protected static void checkDatiRecapitoGiaCliente(PdfDataModel pdfDataModel, String suffissoBeneficiario) {

		String errorMessage = Constants.MSG_ERRORE_CENSIMENTO_ANAGRAFICA;

		StringType prefissoTelefonoBeneficiario = (StringType) pdfDataModel.read(Constants.PREFISSO_TEL_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
		StringType telefonoBeneficiario = (StringType) pdfDataModel.read(Constants.NUMERO_TEL_BASE_FIELD_NAME_PDF + suffissoBeneficiario);

		if (prefissoTelefonoBeneficiario.isNull()) {
			prefissoTelefonoBeneficiario.addTypeError(errorMessage);
		}

		if (telefonoBeneficiario.isNull()) {
			telefonoBeneficiario.addTypeError(errorMessage);
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void setFiduciariaCliente(ClientSessionContext csc, AbstractEventInputData input, int indCodiceCliente) throws DAOException , Exception {
		
		PdfDataModel pdfData = input.getPdfData();
		StringType codiceCliente = (StringType) pdfData.read(Constants.NDG_CLIENTE_BASE_FIELD_NAME_PDF + indCodiceCliente) ;

		AbstractType personaFisicaField = pdfData.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF+ "Cliente" + indCodiceCliente);
		boolean isPersonaFisica = true;
		if (personaFisicaField instanceof StringType)
			isPersonaFisica = !((StringType)personaFisicaField).equals(FALSE);
		else if (personaFisicaField instanceof BooleanType)
			isPersonaFisica = ((BooleanType)personaFisicaField).booleanValue();

		if (codiceCliente.isNull() || isPersonaFisica) {
			pdfData.write(Constants.IS_SOCIETA_FIDUCIARIA_CLIENTE_BASE_FIELD_NAME_PDF+indCodiceCliente, new BooleanType(false));
			return;
		}

		BeneficiariDaoAccess daoAccess = new BeneficiariDaoAccess(csc);
		boolean isSocietaFiduciariaCliente = daoAccess.clienteIsSocietaFiduciaria(codiceCliente);
		pdfData.write(Constants.IS_SOCIETA_FIDUCIARIA_CLIENTE_BASE_FIELD_NAME_PDF+indCodiceCliente, new BooleanType(isSocietaFiduciariaCliente));
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static int getIndPrimoBeneficiarioGiuridico(AbstractEventInputData input, String prefissoBeneficiario) {
		
		for (int i = 1;; i++) {

			String suffissoBeneficiario = prefissoBeneficiario + i;

			if (input.getPdfData().read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF + suffissoBeneficiario) == null) {
				break;
			}

			StringType isPersonaFisica = (StringType) input.getPdfData().read("isPersonaFisica" + suffissoBeneficiario);
			if (isPersonaFisica.equals(FALSE)) {
				return i;
			}			
		}
		
		return -1;
	}

	public static void gestioneForzaturaBeneficiarioVitaContraenteGiuridico(ClientSessionContext csc, AbstractEventInputData input, int indCodiceContraente, int indBeneficiarioGiuridico) throws DAOException , Exception {
		String prefissoBeneficiario = Constants.PREFISSO_BENEFICIARIO_VITA;
		BeneficiariUtil.gestioneForzaturaBeneficiarioContraenteGiuridico(csc, input, indCodiceContraente, indBeneficiarioGiuridico, prefissoBeneficiario);
	}
	
	public static void gestioneForzaturaBeneficiarioDecessoContraenteGiuridico(ClientSessionContext csc, AbstractEventInputData input, int indCodiceContraente, int indBeneficiarioGiuridico) throws DAOException , Exception {
		String prefissoBeneficiario = Constants.PREFISSO_BENEFICIARIO_DECESSO;
		BeneficiariUtil.gestioneForzaturaBeneficiarioContraenteGiuridico(csc, input, indCodiceContraente, indBeneficiarioGiuridico, prefissoBeneficiario);
	}
	
	private static void gestioneForzaturaBeneficiarioContraenteGiuridico(ClientSessionContext csc, AbstractEventInputData input, int indCodiceContraente, int indBeneficiarioGiuridico, String prefissoBeneficiario) throws DAOException , Exception {
		
		//Cerco l'indice del primo contraente giuridico
		if (indBeneficiarioGiuridico == -1)
			indBeneficiarioGiuridico = getIndPrimoBeneficiarioGiuridico(input, prefissoBeneficiario);
		
		PdfDataModel pdfData = input.getPdfData();		

		StringType codiceContraente = (StringType) pdfData.read(Constants.NDG_CLIENTE_BASE_FIELD_NAME_PDF + indCodiceContraente);
		
		AbstractType personaFisicaField = pdfData.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF+ "Cliente" + indCodiceContraente);
		boolean isPersonaFisica = true;
		if (personaFisicaField instanceof StringType)
			isPersonaFisica = !((StringType)personaFisicaField).equals(FALSE);
		else if (personaFisicaField instanceof BooleanType)
			isPersonaFisica = ((BooleanType)personaFisicaField).booleanValue();

		setFiduciariaCliente(csc, input, indCodiceContraente);
		boolean isSocietaFiduciaria = ((BooleanType) pdfData.read(Constants.IS_SOCIETA_FIDUCIARIA_CLIENTE_BASE_FIELD_NAME_PDF + indCodiceContraente)).booleanValue();

		boolean isBeneficiarioGiuridicoDaForzare = true;
		if ( pdfData.read("isBeneficiarioGiuridicoDaForzare") != null) {
			isBeneficiarioGiuridicoDaForzare =	((BooleanType) pdfData.read("isBeneficiarioGiuridicoDaForzare")).booleanValue();
		}

		if (codiceContraente.isNull() || isPersonaFisica || isSocietaFiduciaria || !isBeneficiarioGiuridicoDaForzare) {
			return;
		}
		
		String suffissoBeneficiario = prefissoBeneficiario + indBeneficiarioGiuridico;
		//Pulisco i beneficiari e le motivazioni pep diversi dal beneficiario giuridico
		clearBeneficiariEPep(pdfData, indBeneficiarioGiuridico, prefissoBeneficiario);
		
		input.getPdfData().write(Constants.PERCENTUALE_BASE_FIELD_NAME_PDF+suffissoBeneficiario, new DoubleType(100));
		
		StringType codiceClienteBeneficiarioGiuridico = (StringType)pdfData.read(Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF+suffissoBeneficiario);
		if (!codiceClienteBeneficiarioGiuridico.equals(codiceContraente)) {
			input.getPdfData().write(Constants.TIPO_BENEFICIARIO_BASE_FIELD_NAME_PDF + prefissoBeneficiario, new StringType(Constants.TIPO_BENEFICIARIO_ALTRO));
			input.getPdfData().write(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario, new StringType("S"));
			input.getPdfData().write(Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario, new StringType(codiceContraente.toString()));
			input.getPdf().setEventArgs(new StringType(prefissoBeneficiario+","+indBeneficiarioGiuridico+",PG"));
	
			PageEventInputData inputBeneficiari = new PageEventInputData(input.getPdf());
			Beneficiari.onChangeCodiceClienteSezioneBeneficiario(csc, inputBeneficiari);
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void clearBeneficiariEPep(PdfDataModel pdfData, int indBeneficiarioGiuridico, String prefissoBeneficiario) {
		
		for (int indiceBeneficiario = 1;; indiceBeneficiario++) {
			
			if (indiceBeneficiario == indBeneficiarioGiuridico)
				continue;
			
			String suffissoBeneficiario = prefissoBeneficiario + indiceBeneficiario;
			if (pdfData.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF + suffissoBeneficiario) == null) {
				break;
			}

			boolean isPersonaFisica = ((StringType) pdfData.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF + suffissoBeneficiario)).equals("true");
			
			BeneficiariUtil.clearBeneficiario(pdfData, true, isPersonaFisica, prefissoBeneficiario, indiceBeneficiario);
			PepUtil.clearPep(pdfData, isPersonaFisica, prefissoBeneficiario, indiceBeneficiario);
		}
	}

	public static void checkAggiuntiviBeneficiariNominativi(ClientSessionContext csc, PdfModel pdf, PdfBaseDriver pdfBasedriver, boolean assicurando, int posizioneAssicurando,
			String prefissoBeneficiario)throws Exception {
		
		PdfDataModel pdfDataModel = pdf.getPdfData();
		
		StringType tipoBeneficiario = (StringType) pdfDataModel.read(Constants.TIPO_BENEFICIARIO_BASE_FIELD_NAME_PDF + prefissoBeneficiario);

		if (!tipoBeneficiario.equals(Constants.TIPO_BENEFICIARIO_ALTRO)) 
			return;
		
		int i=0;
		boolean whileEseguibile = true; 
		while (whileEseguibile) {
			i++;
			whileEseguibile = i < 99;

			String suffissoBeneficiario = prefissoBeneficiario + i;
			if (pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario) == null) {
				break;
			}

			if (pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario).isNull()) {
				continue;
			}

			boolean isPersonaFisica = ((StringType) pdfDataModel.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF + suffissoBeneficiario)).equals("true");

			if (isPersonaFisica) { // persona fisica
				
				pdfBasedriver.ctrl_isClienteDeceduto(csc, pdfDataModel, Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
			
			} else { // Persona giuridica ciclo sui titolari
				int j=0;
				boolean whileTitolariEseguibile = true; 
				while (whileTitolariEseguibile) {
					j++;
					whileTitolariEseguibile = j < 9;

					String suffissoTitolare = Constants.TITOLARE + j + prefissoBeneficiario + i;

					if (pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare) == null) {
						break;
					}

					if (pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare).isNull()) {
						continue;
					}
					
					pdfBasedriver.ctrl_isClienteDeceduto(csc, pdfDataModel, Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare);
				}
			}
		}
	}	
}
