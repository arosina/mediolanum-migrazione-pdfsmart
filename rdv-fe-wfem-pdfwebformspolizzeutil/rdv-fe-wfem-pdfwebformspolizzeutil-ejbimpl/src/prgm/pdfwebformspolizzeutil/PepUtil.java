package prgm.pdfwebformspolizzeutil;

import java.util.ArrayList;

import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.model.PdfDataModel;

public class PepUtil {
	private PepUtil() {
		throw new IllegalStateException("Utility class");
	}

	public static void checkPep(PdfDataModel pdfDataModel, String prefissoBeneficiario) {

		StringType tipoBeneficiario = (StringType) pdfDataModel
				.read(Constants.TIPO_BENEFICIARIO_BASE_FIELD_NAME_PDF + prefissoBeneficiario);

		if (!tipoBeneficiario.isNull() && tipoBeneficiario.equals(Constants.TIPO_BENEFICIARIO_ALTRO)) {

			checkPepBeneficiariNominativi(pdfDataModel, prefissoBeneficiario);

		} else if (!tipoBeneficiario.isNull() && !tipoBeneficiario.equals(Constants.TIPO_BENEFICIARIO_ALTRO)) {

			checkPepBeneficiariNonNominativi(pdfDataModel, prefissoBeneficiario);
		}

	}

	private static void checkPepBeneficiariNominativi(PdfDataModel pdfDataModel, String prefissoBeneficiario) {

		for (int i = 1;; i++) {// ciclo sui beneficiari
			String suffissoBeneficiario = prefissoBeneficiario + i;

			if (pdfDataModel.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF + suffissoBeneficiario) == null) {
				break;
			}

			StringType isGiaCliente = (StringType) pdfDataModel
					.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
			boolean isPersonaFisica = true;

			if (((StringType) pdfDataModel.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF + suffissoBeneficiario))
					.equals("false")) {
				isPersonaFisica = false;
			}

			if (isPersonaFisica) {
				//Se ho il flag IS PEP verifico motivazione obbligatoria
				StringType isPep = (StringType) pdfDataModel.read(Constants.IS_PEP_BASE_FIELD_NAME_PDF+ suffissoBeneficiario);
				if (isPep != null){
					if (isPep.equals("S")) {
						StringType indiceMotivazionePep = (StringType) pdfDataModel.read(Constants.INDICE_MOTIVAZIONE_PEP_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
						StringType motivazionePep = (StringType) pdfDataModel.read(Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP + prefissoBeneficiario + indiceMotivazionePep);
						if (motivazionePep != null && motivazionePep.isNull()) {
							motivazionePep.addTypeError(Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO);
						}
					}
					continue;
				}

				StringType motivazionePep = (StringType) pdfDataModel
						.read(Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoBeneficiario);

				StringType cognomePep = (StringType) pdfDataModel
						.read(Constants.COGNOME_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoBeneficiario);

				StringType nomePep = (StringType) pdfDataModel
						.read(Constants.NOME_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoBeneficiario);

				StringType comuneNascitaPep = (StringType) pdfDataModel
						.read(Constants.COMUNE_NASCITA_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoBeneficiario);

				DateType dataNascitaPep = (DateType) pdfDataModel
						.read(Constants.DATA_NASCITA_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoBeneficiario);

				if (cognomePep != null && !cognomePep.isNull()) {
					cognomePep.addTypeError(Constants.MSG_ERRORE_VALORE_NON_AMMESSO);
				}

				if (nomePep != null && !nomePep.isNull()) {
					nomePep.addTypeError(Constants.MSG_ERRORE_VALORE_NON_AMMESSO);
				}

				if (comuneNascitaPep != null && !comuneNascitaPep.isNull()) {
					comuneNascitaPep.addTypeError(Constants.MSG_ERRORE_VALORE_NON_AMMESSO);
				}

				if (dataNascitaPep != null && !dataNascitaPep.isNull()) {
					dataNascitaPep.addTypeError(Constants.MSG_ERRORE_VALORE_NON_AMMESSO);
				}

				if (isGiaCliente.isNull() && motivazionePep != null && !motivazionePep.isNull()) {
					motivazionePep.addTypeError(Constants.MSG_ERRORE_VALORE_NON_AMMESSO);
				}
			} else {
				// ciclo sui titolari
				int j=0;
				boolean eseguiWhile = true; 
				while (eseguiWhile) {
					
					//I titolari max dovrebbero essere 2. Serve solo per bypassare le segnalazioni sonar
					j++;
					eseguiWhile = j<9;
					
					String suffissoTitolare = Constants.TITOLARE + j + prefissoBeneficiario + i;

					if (pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare) == null) {
						break;
					}

					StringType isGiaClienteTitolare = (StringType) pdfDataModel
							.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare);

					//Se ho il flag IS PEP verifico motivazione obbligatoria
					StringType isPep = (StringType) pdfDataModel.read(Constants.IS_PEP_BASE_FIELD_NAME_PDF + suffissoTitolare);

					StringType motivazionePep = (StringType) pdfDataModel.read(Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF
							+ Constants.PEP + Constants.TITOLARE + j + prefissoBeneficiario + i);

					if (isPep != null ){
						if (isPep.equals("S") && motivazionePep != null && motivazionePep.isNull()) {
							motivazionePep.addTypeError(Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO);
						}
						continue;
					}
					
					if (isGiaClienteTitolare.isNull() && motivazionePep != null && !motivazionePep.isNull()) {
						motivazionePep.addTypeError(Constants.MSG_ERRORE_VALORE_NON_AMMESSO);
					}

				}
			}

		}

	}

	private static void checkPepBeneficiariNonNominativi(PdfDataModel pdfDataModel, String prefissoBeneficiario) {
		/*
		 * La sezione pep contiene lo stesso numero di posizioni (e stessa logica PF/PG)
		 * della sezione beneficiari; per facilitare la scrittura del codice utilizziamo
		 * i campi della sezione beneficiario per scorrere le sezioni della pep
		 */
		for (int i = 1;; i++) {
			String suffissoBeneficiario = prefissoBeneficiario + i;

			if (pdfDataModel.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF + suffissoBeneficiario) == null) {
				break;
			}

			if (pdfDataModel
					.read(Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoBeneficiario) != null) {

				// nel caso di beneficiari non nominativi va soltanto controllata la coerenza
				// delle sezioni relative alle PF e non ai Titolari

				StringType motivazionePep = (StringType) pdfDataModel
						.read(Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoBeneficiario);

				StringType cognomePep = (StringType) pdfDataModel
						.read(Constants.COGNOME_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoBeneficiario);

				StringType nomePep = (StringType) pdfDataModel
						.read(Constants.NOME_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoBeneficiario);

				StringType comuneNascitaPep = (StringType) pdfDataModel
						.read(Constants.COMUNE_NASCITA_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoBeneficiario);

				DateType dataNascitaPep = (DateType) pdfDataModel
						.read(Constants.DATA_NASCITA_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoBeneficiario);

				int numeroCampiPepCompilati = 0;
				int numeroCampiPepDaCompilare = 0;

				if (motivazionePep != null) {
					numeroCampiPepDaCompilare++;
					if (!motivazionePep.isNull()) {
						numeroCampiPepCompilati++;
					}
				}

				if (cognomePep != null) {
					numeroCampiPepDaCompilare++;
					if (!cognomePep.isNull()) {
						numeroCampiPepCompilati++;
					}
				}

				if (nomePep != null) {
					numeroCampiPepDaCompilare++;
					if (!nomePep.isNull()) {
						numeroCampiPepCompilati++;
					}
				}

				if (comuneNascitaPep != null) {
					numeroCampiPepDaCompilare++;
					if (!comuneNascitaPep.isNull()) {
						numeroCampiPepCompilati++;
					}
				}

				if (dataNascitaPep != null) {
					numeroCampiPepDaCompilare++;
					if (!dataNascitaPep.isNull()) {
						numeroCampiPepCompilati++;
					}

					if (!dataNascitaPep.isNull() && dataNascitaPep.compareTo(Tools.today()) >= 0) {
						dataNascitaPep.addTypeError(Constants.MSG_ERRORE_DATA_NASCITA);
					}
				}

				if (numeroCampiPepCompilati > 0 && numeroCampiPepDaCompilare != numeroCampiPepCompilati) {
					if (motivazionePep != null && motivazionePep.isNull()) {
						motivazionePep.addTypeError(Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO);
					}
					if (cognomePep != null && cognomePep.isNull()) {
						cognomePep.addTypeError(Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO);
					}
					if (nomePep != null && nomePep.isNull()) {
						nomePep.addTypeError(Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO);
					}
					if (comuneNascitaPep != null && comuneNascitaPep.isNull()) {
						comuneNascitaPep.addTypeError(Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO);
					}
					if (dataNascitaPep != null && dataNascitaPep.isNull()) {
						dataNascitaPep.addTypeError(Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO);
					}
				}

			}

		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	
	public static void compattaPepBeneficiarioDecessoNonNominativo(PdfDataModel pdfDataModel)  {

		String prefissoBeneficiario = Constants.PREFISSO_BENEFICIARIO_DECESSO;
		
		StringType tipoBeneficiario = (StringType) pdfDataModel
				.read(Constants.TIPO_BENEFICIARIO_BASE_FIELD_NAME_PDF + prefissoBeneficiario);

		if (!tipoBeneficiario.isNull() && !tipoBeneficiario.equals(Constants.TIPO_BENEFICIARIO_ALTRO)) {
			PepUtil.compattaPepBeneficiarioNonNominativo(pdfDataModel, prefissoBeneficiario);
		}
		
	}

	private static void compattaPepBeneficiarioNonNominativo(PdfDataModel pdfDataModel, String prefissoBeneficiario) {

		ArrayList<MapCommandDataModel> pepBeneficiariPFSelezionati = new ArrayList<MapCommandDataModel>();
		ArrayList<MapCommandDataModel> pepBeneficiariPGSelezionati = new ArrayList<MapCommandDataModel>();

		for (int i = 1;; i++) {
			String suffissoBeneficiario = prefissoBeneficiario + i;
			if (pdfDataModel.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF + suffissoBeneficiario) == null) {
				break;
			}

			boolean isPersonaFisica = true;
			if (((StringType) pdfDataModel.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF + suffissoBeneficiario))
					.equals("false")) {
				isPersonaFisica = false;
			}

			if (isPepCompilata(pdfDataModel, isPersonaFisica, prefissoBeneficiario, i)) {
				MapCommandDataModel pepModel = pepToModel(pdfDataModel, isPersonaFisica, prefissoBeneficiario, i);
				if (isPersonaFisica) {
					pepBeneficiariPFSelezionati.add(pepModel);
				} else {
					pepBeneficiariPGSelezionati.add(pepModel);
				}
			}
		}

		// compatto le pep PF
		int firstBeneficiarioPG = 1;
		int idx = 0;
		for (int i = 1;; i++) {
			String suffissoBeneficiario = prefissoBeneficiario + i;
			if (pdfDataModel.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF + suffissoBeneficiario) == null) {
				break;
			}

			boolean isPersonaFisica = true;
			if (((StringType) pdfDataModel.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF + suffissoBeneficiario))
					.equals("false")) {
				isPersonaFisica = false;
			}

			if (!isPersonaFisica) {
				firstBeneficiarioPG = i;
				break;
			}

			if (idx < pepBeneficiariPFSelezionati.size()) {
				MapCommandDataModel pepModel = pepBeneficiariPFSelezionati.get(idx);
				pepToPdf(pepModel, pdfDataModel, true, prefissoBeneficiario, i);
			} else {
				clearPep(pdfDataModel, true, prefissoBeneficiario, i);
			}
			idx++;
		}

		// compatto pep beneficiari PG
		idx = 0;
		for (int j = firstBeneficiarioPG;; j++) {
			String suffissoBeneficiario = prefissoBeneficiario + j;
			if (pdfDataModel.read(Constants.IS_PERSONA_FISICA_BASE_FIELD_NAME_PDF + suffissoBeneficiario) == null) {
				break;
			}

			if (idx < pepBeneficiariPGSelezionati.size()) {
				MapCommandDataModel pepModel = pepBeneficiariPGSelezionati.get(idx);
				pepToPdf(pepModel, pdfDataModel, false, prefissoBeneficiario, j);
			} else {
				clearPep(pdfDataModel, false, prefissoBeneficiario, j);
			}

			// per ogni beneficiario PG devo compattare la pep dei titolari relativi al
			// beneficiario

			compattaPepTitolari(pdfDataModel, prefissoBeneficiario, j);

			idx++;
		}
	}

	private static boolean isPepCompilata(PdfDataModel pdfDataModel, boolean isPersonaFisica,
			String prefissoBeneficiario, int indiceBeneficiario) {

		String suffissoBeneficiario = prefissoBeneficiario + indiceBeneficiario;

		if (isPersonaFisica && (!pdfDataModel
				.read(Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoBeneficiario).isNull()
				|| !pdfDataModel.read(Constants.COGNOME_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoBeneficiario)
						.isNull()
				|| !pdfDataModel.read(Constants.NOME_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoBeneficiario)
						.isNull()
				|| !pdfDataModel.read(Constants.DATA_NASCITA_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoBeneficiario)
						.isNull()
				|| !pdfDataModel
						.read(Constants.COMUNE_NASCITA_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoBeneficiario)
						.isNull())) {
			return true;
		} else if (!isPersonaFisica) {
			// ciclo sui titolari
			for (int j = 1;; j++) {
				String suffissoTitolare = Constants.TITOLARE + j + prefissoBeneficiario + indiceBeneficiario;

				if (pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare) == null) {
					break;
				}

				if (!pdfDataModel.read(Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoTitolare)
						.isNull()) {
					return true;
				}
			}
		}

		return false;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static MapCommandDataModel pepToModel(PdfDataModel pdfDataModel, boolean isPersonaFisica,
			String prefissoBeneficiario, int indiceBeneficiario) {
		MapCommandDataModel pepModel = new MapCommandDataModel();

		String suffissoBeneficiario = prefissoBeneficiario + indiceBeneficiario;
		if (isPersonaFisica) {
			// dati anagrafici
			pepModel.addProperty(Constants.NOME_BASE_FIELD_NAME_PDF + Constants.PEP + prefissoBeneficiario, pdfDataModel
					.readProperty(Constants.NOME_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoBeneficiario));
			pepModel.addProperty(Constants.COGNOME_BASE_FIELD_NAME_PDF + Constants.PEP + prefissoBeneficiario,
					pdfDataModel.readProperty(
							Constants.COGNOME_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoBeneficiario));
			pepModel.addProperty(Constants.COMUNE_NASCITA_BASE_FIELD_NAME_PDF + Constants.PEP + prefissoBeneficiario,
					pdfDataModel.readProperty(
							Constants.COMUNE_NASCITA_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoBeneficiario));
			pepModel.addProperty(Constants.DATA_NASCITA_BASE_FIELD_NAME_PDF + Constants.PEP + prefissoBeneficiario,
					pdfDataModel.readProperty(
							Constants.DATA_NASCITA_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoBeneficiario));

			pepModel.addProperty(Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP + prefissoBeneficiario,
					pdfDataModel.readProperty(
							Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoBeneficiario));

		} else {

			// ciclo sui titolari
			for (int j = 1;; j++) {
				String suffissoTitolare = Constants.TITOLARE + j + prefissoBeneficiario + indiceBeneficiario;

				if (pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare) == null) {
					break;
				}

				pepModel.addProperty(
						Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP + Constants.TITOLARE + j
								+ prefissoBeneficiario,
						pdfDataModel.readProperty(
								Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoTitolare));

			}
		}
		return pepModel;
	}

	private static void pepToPdf(MapCommandDataModel pepModel, PdfDataModel pdfDataModel, boolean isPersonaFisica,
			String prefissoBeneficiario, int indiceBeneficiario) {

		String suffissoBeneficiario = prefissoBeneficiario + indiceBeneficiario;

		if (isPersonaFisica) {
			// dati anagrafici pep PF
			pdfDataModel.addProperty(Constants.NOME_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoBeneficiario,
					pepModel.readProperty(Constants.NOME_BASE_FIELD_NAME_PDF + Constants.PEP + prefissoBeneficiario));
			pdfDataModel.addProperty(Constants.COGNOME_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoBeneficiario,
					pepModel.readProperty(
							Constants.COGNOME_BASE_FIELD_NAME_PDF + Constants.PEP + prefissoBeneficiario));
			pdfDataModel.addProperty(
					Constants.COMUNE_NASCITA_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoBeneficiario,
					pepModel.readProperty(
							Constants.COMUNE_NASCITA_BASE_FIELD_NAME_PDF + Constants.PEP + prefissoBeneficiario));
			pdfDataModel.addProperty(Constants.DATA_NASCITA_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoBeneficiario,
					pepModel.readProperty(
							Constants.DATA_NASCITA_BASE_FIELD_NAME_PDF + Constants.PEP + prefissoBeneficiario));
			pdfDataModel.addProperty(Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoBeneficiario,
					pepModel.readProperty(
							Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP + prefissoBeneficiario));

		} else {

			// ciclo sui titolari
			for (int j = 1;; j++) {
				String suffissoTitolare = Constants.TITOLARE + j + prefissoBeneficiario + indiceBeneficiario;

				if (pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare) == null) {
					break;
				}

				pdfDataModel.addProperty(Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoTitolare,
						pepModel.readProperty(Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP
								+ Constants.TITOLARE + j + prefissoBeneficiario));

			}
		}
	}

	public static void clearPep(PdfDataModel pdfDataModel, boolean isPersonaFisica, String prefissoBeneficiario,
			int indiceBeneficiario) {

		String suffissoBeneficiario = prefissoBeneficiario + indiceBeneficiario;

		pdfDataModel.addProperty(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario, new StringType());
		pdfDataModel.addProperty(Constants.CODICE_CLIENTE_BASE_FIELD_NAME_PDF + suffissoBeneficiario, new StringType());

		if (isPersonaFisica) {
			// dati anagrafici
			pdfDataModel.addProperty(Constants.NOME_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoBeneficiario,
					new StringType());
			pdfDataModel.addProperty(Constants.COGNOME_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoBeneficiario,
					new StringType());
			pdfDataModel.addProperty(
					Constants.COMUNE_NASCITA_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoBeneficiario,
					new StringType());
			pdfDataModel.addProperty(Constants.DATA_NASCITA_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoBeneficiario,
					new DateType());

			pdfDataModel.addProperty(Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoBeneficiario,
					new StringType());

		} else {

			// ciclo sui titolari
			for (int j = 1;; j++) {
				String suffissoTitolare = Constants.TITOLARE + j + prefissoBeneficiario + indiceBeneficiario;

				if (pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare) == null) {
					break;
				}

				pdfDataModel.addProperty(Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoTitolare,
						new StringType());

				if (pdfDataModel.read(Constants.COGNOME_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoTitolare) != null)
					pdfDataModel.addProperty(Constants.COGNOME_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoTitolare, new StringType());

				if (pdfDataModel.read(Constants.NOME_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoTitolare) != null)
					pdfDataModel.addProperty(Constants.NOME_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoTitolare, new StringType());
			}
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void compattaPepTitolari(PdfDataModel pdfDataModel, String prefissoBeneficiario,
			int indiceBeneficiario) {

		ArrayList<MapCommandDataModel> pepTitolariSelezionati = new ArrayList<MapCommandDataModel>();

		for (int i = 1;; i++) {
			String suffissoTitolare = Constants.TITOLARE + i + prefissoBeneficiario + indiceBeneficiario;
			if (pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare) == null) {
				break;
			}

			if (isPepTitolareCompilata(pdfDataModel, prefissoBeneficiario, indiceBeneficiario, i)) {
				MapCommandDataModel titolareModel = pepTitolareToModel(pdfDataModel, prefissoBeneficiario,
						indiceBeneficiario, i);
				pepTitolariSelezionati.add(titolareModel);
			}
		}

		// compatto la pep dei titolari relativi a una PG
		int idxTit = 0;
		for (int k = 1;; k++) {
			String suffissoTitolare = Constants.TITOLARE + k + prefissoBeneficiario + indiceBeneficiario;
			if (pdfDataModel.read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare) == null) {
				break;
			}

			if (idxTit < pepTitolariSelezionati.size()) {
				MapCommandDataModel pepTitolareModel = pepTitolariSelezionati.get(idxTit);
				pepTitolareToPdf(pepTitolareModel, pdfDataModel, prefissoBeneficiario, indiceBeneficiario, k);
			} else {
				clearPepTitolare(pdfDataModel, prefissoBeneficiario, indiceBeneficiario, k);
			}
			idxTit++;
		}

	}

	private static boolean isPepTitolareCompilata(PdfDataModel pdfDataModel, String prefissoBeneficiario,
			int indiceBeneficiario, int indiceTitolare) {

		String suffissoTitolare = Constants.TITOLARE + indiceTitolare + prefissoBeneficiario + indiceBeneficiario;

		return (!pdfDataModel.read(Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoTitolare).isNull());
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static MapCommandDataModel pepTitolareToModel(PdfDataModel pdfDataModel, String prefissoBeneficiario,
			int indiceBeneficiario, int indiceTitolare) {
		MapCommandDataModel titolareModel = new MapCommandDataModel();

		String suffissoTitolare = Constants.TITOLARE + indiceTitolare + prefissoBeneficiario + indiceBeneficiario;

		titolareModel.addProperty(
				Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP + Constants.TITOLARE + prefissoBeneficiario,
				pdfDataModel
						.readProperty(Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoTitolare));

		return titolareModel;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void pepTitolareToPdf(MapCommandDataModel titolareModel, PdfDataModel pdfDataModel,
			String prefissoBeneficiario, int indiceBeneficiario, int indiceTitolare) {

		String suffissoTitolare = Constants.TITOLARE + indiceTitolare + prefissoBeneficiario + indiceBeneficiario;

		pdfDataModel.addProperty(Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoTitolare,
				titolareModel.readProperty(Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP
						+ Constants.TITOLARE + prefissoBeneficiario));

	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void clearPepTitolare(PdfDataModel pdfDataModel, String prefissoBeneficiario, int indiceBeneficiario,
			int indiceTitolare) {

		String suffissoTitolare = Constants.TITOLARE + indiceTitolare + prefissoBeneficiario + indiceBeneficiario;

		pdfDataModel.addProperty(Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP + suffissoTitolare,
				new StringType());

	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void checkFlagPep(PdfDataModel pdfDataModel, String suffissoBeneficiario)  {

		StringType flagPep = (StringType) pdfDataModel.read(Constants.IS_PEP_BASE_FIELD_NAME_PDF + suffissoBeneficiario);
		
		if (flagPep != null && flagPep.isNull())
			flagPep.addTypeError(Constants.MSG_ERRORE_CAMPO_OBBLIGATORIO);
		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void compattaPepBeneficiariNominativiCasoDecesso(PdfDataModel pdfDataModel)  {
		compattaPepBeneficiariNominativi(pdfDataModel, Constants.PREFISSO_BENEFICIARIO_DECESSO);
	}
	public static void compattaPepBeneficiariNominativiCasoVita(PdfDataModel pdfDataModel)  {
		compattaPepBeneficiariNominativi(pdfDataModel, Constants.PREFISSO_BENEFICIARIO_VITA);
	}
	
	private static void compattaPepBeneficiariNominativi(PdfDataModel pdfDataModel, String prefissoBeneficiario)  {

		StringType tipoBeneficiario = (StringType)pdfDataModel.read(Constants.TIPO_BENEFICIARIO_BASE_FIELD_NAME_PDF + prefissoBeneficiario);

		if (!tipoBeneficiario.equals(Constants.TIPO_BENEFICIARIO_ALTRO) || pdfDataModel.read(Constants.IS_PEP_BASE_FIELD_NAME_PDF + prefissoBeneficiario+"1") == null)
			return;
		
		for (int indiceTo = 1;; indiceTo++) {
			StringType motivazioneTo = (StringType) pdfDataModel.read(Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP + prefissoBeneficiario + indiceTo);
			if (motivazioneTo == null) {
				break;
			}

			if (!motivazioneTo.isNull())
				continue;
			
			verificaESpostaMotivazione(pdfDataModel, indiceTo, prefissoBeneficiario);
		}
	}
	
	private static void verificaESpostaMotivazione(PdfDataModel pdfDataModel, int indiceTo, String prefissoBeneficiario) {
		
		//Motivazione vuota, verifico se ce ne sono da spostare
		for (int indiceFrom = indiceTo+1;; indiceFrom++) {
			StringType motivazioneFrom = (StringType) pdfDataModel.read(Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP + prefissoBeneficiario + indiceFrom);
			if (motivazioneFrom == null) {
				break;
			}
			
			if (motivazioneFrom.isNull())
				continue;
			
			spostaCampiMovitazione(pdfDataModel, indiceFrom, indiceTo, prefissoBeneficiario);
			aggiornaRiferimentoBeneficiario(pdfDataModel, indiceFrom, indiceTo, prefissoBeneficiario);
			break;
		}
		
	}
	private static void aggiornaRiferimentoBeneficiario(PdfDataModel pdfDataModel, int indiceFrom, int indiceTo, String prefissoBeneficiario) {

		for (int indiceBenef = 1;; indiceBenef++) {
			String suffissoBeneficiario = prefissoBeneficiario + indiceBenef;
			
			StringType indiceMotivazionePepBeneficiario = (StringType)pdfDataModel.read(Constants.INDICE_MOTIVAZIONE_PEP_BASE_FIELD_NAME_PDF + suffissoBeneficiario);

			if (indiceMotivazionePepBeneficiario == null) 
				break;
			
			if (indiceMotivazionePepBeneficiario.isNull())
				continue;
			
			if (Integer.parseInt(indiceMotivazionePepBeneficiario.toString()) == indiceFrom){
				pdfDataModel.write(Constants.INDICE_MOTIVAZIONE_PEP_BASE_FIELD_NAME_PDF + suffissoBeneficiario, new StringType(""+indiceTo));
				break;
			}
		}
		
	}
	private static void spostaCampiMovitazione(PdfDataModel pdfDataModel, int indiceFrom, int indiceTo, String prefissoBeneficiario) {
		spostaCampoMotivazione(pdfDataModel, indiceFrom, indiceTo, Constants.MOTIVAZIONE_BASE_FIELD_NAME_PDF + Constants.PEP + prefissoBeneficiario);
		spostaCampoMotivazione(pdfDataModel, indiceFrom, indiceTo, Constants.COGNOME_BASE_FIELD_NAME_PDF + Constants.PEP + prefissoBeneficiario);
		spostaCampoMotivazione(pdfDataModel, indiceFrom, indiceTo, Constants.NOME_BASE_FIELD_NAME_PDF + Constants.PEP + prefissoBeneficiario);
		spostaCampoMotivazioneDateType(pdfDataModel, indiceFrom, indiceTo, Constants.DATA_NASCITA_BASE_FIELD_NAME_PDF + Constants.PEP + prefissoBeneficiario);
		spostaCampoMotivazione(pdfDataModel, indiceFrom, indiceTo, Constants.COMUNE_NASCITA_BASE_FIELD_NAME_PDF + Constants.PEP + prefissoBeneficiario);
		
	}
	private static void spostaCampoMotivazione(PdfDataModel pdfDataModel, int indiceFrom, int indiceTo, String nomeCampo) {
		StringType valoreCampo = (StringType) pdfDataModel.read(nomeCampo + indiceFrom);
		pdfDataModel.write(nomeCampo + indiceTo, valoreCampo);
		//pulisco
		pdfDataModel.write(nomeCampo + indiceFrom, new StringType());
	}
	private static void spostaCampoMotivazioneDateType(PdfDataModel pdfDataModel, int indiceFrom, int indiceTo, String nomeCampo) {
		DateType valoreCampo = (DateType) pdfDataModel.read(nomeCampo + indiceFrom);
		pdfDataModel.write(nomeCampo + indiceTo, valoreCampo);
		//pulisco
		pdfDataModel.write(nomeCampo + indiceFrom, new DateType());
	}
}
