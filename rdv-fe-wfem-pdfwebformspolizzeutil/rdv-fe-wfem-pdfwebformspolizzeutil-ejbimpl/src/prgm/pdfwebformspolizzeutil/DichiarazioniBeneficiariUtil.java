package prgm.pdfwebformspolizzeutil;

import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsutil.drivers.util.CodiceFiscaleUtils;

public class DichiarazioniBeneficiariUtil {
	private DichiarazioniBeneficiariUtil() {
		throw new IllegalStateException("Utility class");
	}

	private static final String FALSE = "false";

	public static void checkDichiarazioneBeneficiarioNominativoNonCensito(PdfDataModel pdfDataModel, String suffissoBeneficiario) throws Exception {

		String suffissoDichiarazione = Constants.LEGALE_RAPPR_PROCURATORE + suffissoBeneficiario;
		
		StringType cognome = (StringType) pdfDataModel.read(Constants.COGNOME_BASE_FIELD_NAME_PDF + suffissoDichiarazione);
		StringType nome = (StringType) pdfDataModel.read(Constants.NOME_BASE_FIELD_NAME_PDF + suffissoDichiarazione);
		StringType codiceFiscale = (StringType) pdfDataModel.read(Constants.CODICE_FISCALE_BASE_FIELD_NAME_PDF + suffissoDichiarazione);

		if (cognome == null) {
			//Sul modulo non ci sono le dichiarazioni
			return;
		}
		
		//Il legale rappresentante è obbligatorio per i beneficiari persone giudiche
		//Il procuratore è facoltativo per i beneficiari persone fisiche
		//Ma se viene indicato il cognome oppure il nome oppure il codice fiscale
		//gli altri dati diventano obbligatori
		
		
		boolean isPersonaFisica = true;
		if (((StringType) pdfDataModel.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF + suffissoBeneficiario)).equals(FALSE)) {
			isPersonaFisica = false;
		}

		if (isPersonaFisica && nome.isNull() && cognome.isNull() && codiceFiscale.isNull()) {
			return;
		}

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
}
