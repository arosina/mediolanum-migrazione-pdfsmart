package prgm.pdfwebformspolizzeutil;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsutil.drivers.util.CodiceFiscaleUtils;

public class ReferenteTerzoUtil {
	private ReferenteTerzoUtil() {
		throw new IllegalStateException("Utility class");
	}

	public static void checkReferenteTerzo(ClientSessionContext csc, PdfDataModel pdfDataModel, boolean assicurando,
			int posizioneAssicurando) throws Exception, DAOException {

		StringType sceltaReferenteTerzo = (StringType) pdfDataModel.read("sceltaReferenteTerzo");

		if (sceltaReferenteTerzo.equals("S")) {

			StringType codiceFiscaleReferenteTerzo = (StringType) pdfDataModel
					.read(Constants.CODICE_FISCALE_BASE_FIELD_NAME_PDF + Constants.REFERENTE_TERZO);
			StringType codiceFiscalePartitaIvaCliente1 = (StringType) pdfDataModel
					.read("codiceFiscalePartitaIvaCliente1");
			StringType codiceFiscalePartitaIvaAssicurando = (StringType) pdfDataModel
					.read("codiceFiscalePartitaIvaCliente" + posizioneAssicurando);

			checkDatiAnagraficiReferenteTerzo(pdfDataModel);
			if (!codiceFiscaleReferenteTerzo.isNull()) {
				checkCoerenzaReferenteTerzoConBeneficiari(pdfDataModel, codiceFiscaleReferenteTerzo);
				checkCoerenzaReferenteTerzoConSottoscrittori(assicurando, codiceFiscaleReferenteTerzo,
						codiceFiscalePartitaIvaCliente1, codiceFiscalePartitaIvaAssicurando);
			}
			Utils.checkDatiIndirizzo(csc, pdfDataModel, Constants.REFERENTE_TERZO);
			Utils.checkDatiRecapito(pdfDataModel, Constants.REFERENTE_TERZO, Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO);

		}

	}

	private static void checkDatiAnagraficiReferenteTerzo(PdfDataModel pdfDataModel) throws Exception {

		StringType cognome = (StringType) pdfDataModel.read(Constants.COGNOME_BASE_FIELD_NAME_PDF + Constants.REFERENTE_TERZO);
		StringType nome = (StringType) pdfDataModel.read(Constants.NOME_BASE_FIELD_NAME_PDF + Constants.REFERENTE_TERZO);
		StringType codiceFiscale = (StringType) pdfDataModel
				.read(Constants.CODICE_FISCALE_BASE_FIELD_NAME_PDF + Constants.REFERENTE_TERZO);

		if (nome.isNull()) {
			nome.addTypeError(Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO);
		} 

		if (cognome.isNull()) {
			cognome.addTypeError(Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO);
		} 

		if (codiceFiscale.isNull()) {
			codiceFiscale.addTypeError(Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO);
		} else {

			CodiceFiscaleUtils.checkCodiceFiscale(codiceFiscale);

		}

	}

	private static void checkCoerenzaReferenteTerzoConSottoscrittori(boolean assicurando,
			StringType codiceFiscaleReferenteTerzo, StringType codiceFiscaleContraente,
			StringType codiceFiscaleAssicurando) {

		if (assicurando && !codiceFiscaleReferenteTerzo.isNull() && !codiceFiscaleAssicurando.isNull()
				&& codiceFiscaleReferenteTerzo.equals(codiceFiscaleAssicurando)) {
			codiceFiscaleReferenteTerzo.addTypeError(Constants.MSG_ERRORE_COERENZA_REFERENTE_TERZO);
		}

		if (!codiceFiscaleReferenteTerzo.isNull() && !codiceFiscaleContraente.isNull()
				&& (codiceFiscaleAssicurando == null || codiceFiscaleAssicurando.isNull())
				&& codiceFiscaleReferenteTerzo.equals(codiceFiscaleContraente)) {
			codiceFiscaleReferenteTerzo.addTypeError(Constants.MSG_ERRORE_COERENZA_REFERENTE_TERZO);
		}
	}

	private static void checkCoerenzaReferenteTerzoConBeneficiari(PdfDataModel pdfDataModel,
			StringType codiceFiscaleReferenteTerzo) {

		for (int i = 1;; i++) {
			String suffissoBeneficiario = Constants.PREFISSO_BENEFICIARIO_DECESSO + i;
			if (pdfDataModel.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF + suffissoBeneficiario) == null) {
				break;
			}

			boolean isPersonaFisica = true;
			if (((StringType) pdfDataModel.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF + suffissoBeneficiario))
					.equals("false")) {
				isPersonaFisica = false;
			}

			StringType isGiaCliente = (StringType) pdfDataModel
					.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario);

			if (!isGiaCliente.isNull()) {
				if (isPersonaFisica) {// persona fisica

					StringType codiceFiscaleBeneficiario = (StringType) pdfDataModel
							.read(Constants.CODICE_FISCALE_BASE_FIELD_NAME_PDF + suffissoBeneficiario);

					if (!codiceFiscaleBeneficiario.isNull()
							&& codiceFiscaleReferenteTerzo.equals(codiceFiscaleBeneficiario)) {
						codiceFiscaleReferenteTerzo.addTypeError(Constants.MSG_ERRORE_COERENZA_REFERENTE_TERZO);
					}

				} else {

					StringType codiceFiscaleBeneficiario = (StringType) pdfDataModel
							.read(Constants.CODICE_FISCALE_PIVA_BASE_FIELD_NAME_PDF + suffissoBeneficiario);

					if (!codiceFiscaleBeneficiario.isNull()
							&& codiceFiscaleReferenteTerzo.equals(codiceFiscaleBeneficiario)) {
						codiceFiscaleReferenteTerzo.addTypeError(Constants.MSG_ERRORE_COERENZA_REFERENTE_TERZO);
					}

				}
			}

		}

	}

}
