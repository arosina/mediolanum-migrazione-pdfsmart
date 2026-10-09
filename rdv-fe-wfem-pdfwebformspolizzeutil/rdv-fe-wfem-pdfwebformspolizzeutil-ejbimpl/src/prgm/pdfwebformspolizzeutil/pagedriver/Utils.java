package prgm.pdfwebformspolizzeutil.pagedriver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.core.PdfActionInfos;
import prgm.pdfwebforms.drivers.io.PageLoadInputData;
import prgm.pdfwebformspolizzeutil.Constants;

public class Utils {
	
	private Utils() {
		throw new IllegalStateException("Utility class");
	}

	public static void buildJsScriptsSezioneBeneficiariCasoDecesso(PageLoadInputData input, Map<String, String> fieldsJsScripts,
			boolean assicurando, int posizioneAssicurando) {
		buildJsScriptsSezioneBeneficiari(input, fieldsJsScripts,
				 assicurando, posizioneAssicurando, "Beneficiario");
	}
	
	
	public static void buildJsScriptsSezioneBeneficiariCasoVita(PageLoadInputData input, Map<String, String> fieldsJsScripts,
			boolean assicurando, int posizioneAssicurando) {
		buildJsScriptsSezioneBeneficiari(input, fieldsJsScripts,
				 assicurando, posizioneAssicurando, "BeneficiarioVita");
	}
	
	
	public static void buildJsScriptsSezioneBeneficiari(PageLoadInputData input, Map<String, String> fieldsJsScripts,
			boolean assicurando, int posizioneAssicurando, String prefissoBeneficiario) {
		if (fieldsJsScripts == null) {
			fieldsJsScripts = new HashMap<String, String>();
		}

		StringType codiceAgente = prgm.pdfwebformspolizzeutil.Utils.getCodiceAgente(input.getPdfData());

		for (int i = 1;; i++) {

			String suffissoBeneficiario = prefissoBeneficiario + i;

			if (input.getPdfData().read("isPersonaFisica" + suffissoBeneficiario) == null) {
				break;
			}

			StringType isPersonaFisicaBeneficiarioTemp = (StringType) input.getPdfData()
					.read("isPersonaFisica" + suffissoBeneficiario);
			boolean isPersonaFisicaBeneficiario = true;
			if (isPersonaFisicaBeneficiarioTemp.equals("false")) {
				isPersonaFisicaBeneficiario = false;
			}
			

			if (isPersonaFisicaBeneficiario) {
				fieldsJsScripts.put("comuneNascita" + suffissoBeneficiario, 
						"bindComuneNascitaBeneficiarioAutocomplete({comuneFieldName: 'comune', " + 
						"fieldName:            'comuneNascita" + suffissoBeneficiario + "', "+
						"prefissoBeneficiario: '"+prefissoBeneficiario+"', "+
						"indiceBeneficiario:   '"+i+"', "+
						"});");

				fieldsJsScripts.put("nazioneComuneNascita" + suffissoBeneficiario,
						"bindNazioneAutocomplete('nazioneComuneNascita" + suffissoBeneficiario + "'," + "[]);");
				
				
				fieldsJsScripts.put("codiceCliente"+ suffissoBeneficiario, 
						"bindRicercaBeneficiarioAutocomplete({beneficiarioFieldName: 'codiceCliente', "+
						"fieldName: 		'codiceCliente"+suffissoBeneficiario+"', "+
						"codAgente: 		'"+codiceAgente+"', "+
						"assicurando: 		'"+assicurando+"', "+
						"posizioneAssicurando: 		'"+posizioneAssicurando+"', "+
						"tipoRicerca: 	'PF', "+
						"prefissoBeneficiario: 		'"+prefissoBeneficiario+"', "+
						"indiceBeneficiario: 		'"+i+"', "+
						"minLenght: 		'3'});");

		
				fieldsJsScripts.put("cognome"+ suffissoBeneficiario, 
						"bindRicercaBeneficiarioAutocomplete({beneficiarioFieldName: 'cognome', "+
						"fieldName: 		'cognome"+suffissoBeneficiario+"', "+
						"codAgente: 		'"+codiceAgente+"', "+
						"assicurando: 		'"+assicurando+"', "+
						"posizioneAssicurando: 		'"+posizioneAssicurando+"', "+
						"tipoRicerca: 	'PF', "+
						"prefissoBeneficiario: 		'"+prefissoBeneficiario+"', "+
						"indiceBeneficiario: 		'"+i+"', "+
						"minLenght: 		'3'});");
				
				fieldsJsScripts.put("codiceFiscale"+ suffissoBeneficiario, 
						"bindRicercaBeneficiarioAutocomplete({beneficiarioFieldName: 'codiceFiscale', "+
						"fieldName: 		'codiceFiscale"+suffissoBeneficiario+"', "+
						"codAgente: 		'"+codiceAgente+"', "+
						"assicurando: 		'"+assicurando+"', "+
						"posizioneAssicurando: 		'"+posizioneAssicurando+"', "+
						"tipoRicerca: 	'PF', "+
						"prefissoBeneficiario: 		'"+prefissoBeneficiario+"', "+
						"indiceBeneficiario: 		'"+i+"', "+
						"minLenght: 		'3'});");
	
				
				initAction("apriPopupRicercaAction" + suffissoBeneficiario, "<img id='apriPopupRicercaAction"
						+ suffissoBeneficiario
						+ "' src='/PdfWebFormsPolizzeUtil/images/search.gif' onclick='pdfPageDriver.openPopupRicercaBeneficiari(false,\""
						+ prefissoBeneficiario + "\"," + i + ",\"PF\"," + codiceAgente
						+ ");' style='cursor:pointer;'></img>", input);
				
				
				

			} else {
				
				fieldsJsScripts.put("codiceCliente"+ suffissoBeneficiario, 
						"bindRicercaBeneficiarioAutocomplete({beneficiarioFieldName: 'codiceCliente', "+
						"fieldName: 		'codiceCliente"+suffissoBeneficiario+"', "+
						"codAgente: 		'"+codiceAgente+"', "+
						"assicurando: 		'"+assicurando+"', "+
						"posizioneAssicurando: 		'"+posizioneAssicurando+"', "+
						"tipoRicerca: 	'PG', "+
						"prefissoBeneficiario: 		'"+prefissoBeneficiario+"', "+
						"indiceBeneficiario: 		'"+i+"', "+
						"minLenght: 		'3'});");


				fieldsJsScripts.put("ragioneSociale"+ suffissoBeneficiario, 
						"bindRicercaBeneficiarioAutocomplete({beneficiarioFieldName: 'ragioneSociale', "+
						"fieldName: 		'ragioneSociale"+suffissoBeneficiario+"', "+
						"codAgente: 		'"+codiceAgente+"', "+
						"assicurando: 		'"+assicurando+"', "+
						"posizioneAssicurando: 		'"+posizioneAssicurando+"', "+
						"tipoRicerca: 	'PG', "+
						"prefissoBeneficiario: 		'"+prefissoBeneficiario+"', "+
						"indiceBeneficiario: 		'"+i+"', "+
						"minLenght: 		'3'});");
				
				
				fieldsJsScripts.put("codiceFiscalePartitaIva"+ suffissoBeneficiario, 
						"bindRicercaBeneficiarioAutocomplete({beneficiarioFieldName: 'codiceFiscale', "+
						"fieldName: 		'codiceFiscalePartitaIva"+suffissoBeneficiario+"', "+
						"codAgente: 		'"+codiceAgente+"', "+
						"assicurando: 		'"+assicurando+"', "+
						"posizioneAssicurando: 		'"+posizioneAssicurando+"', "+
						"tipoRicerca: 	'PG', "+
						"prefissoBeneficiario: 		'"+prefissoBeneficiario+"', "+
						"indiceBeneficiario: 		'"+i+"', "+
						"minLenght: 		'3'});");
	
				
				initAction("apriPopupRicercaAction" + suffissoBeneficiario, "<img id='apriPopupRicercaAction"
						+ suffissoBeneficiario
						+ "' src='/PdfWebFormsPolizzeUtil/images/search.gif' onclick='pdfPageDriver.openPopupRicercaBeneficiari(false,\""
						+ prefissoBeneficiario + "\"," + i + ",\"PG\"," + codiceAgente
						+ ");' style='cursor:pointer;'></img>", input);

				int j=0;
				boolean eseguiWhile = true; 
				while (eseguiWhile) {
					//I titolari max dovrebbero essere 2. Serve solo per bypassare le segnalazioni sonar
					j++;
					eseguiWhile = j<9;

					String suffissoTitolare = Constants.TITOLARE + j + prefissoBeneficiario + i;
					if (input.getPdfData().read(Constants.IS_GIA_CLIENTE_BASE_FIELD_NAME_PDF + suffissoTitolare) == null) {
						break;
					}

					fieldsJsScripts.put("comuneNascita" + suffissoTitolare, 
							"bindComuneNascitaBeneficiarioAutocomplete({comuneFieldName: 'comune', " + 
							"fieldName:            'comuneNascita" + suffissoTitolare + "', "+
							"prefissoBeneficiario: '"+prefissoBeneficiario+"', "+
							"indiceBeneficiario:   '"+i+"', "+
							"indiceTitolare:       '"+j+"'});");

					fieldsJsScripts.put("nazioneComuneNascita" + suffissoTitolare,
							"bindNazioneAutocomplete('nazioneComuneNascita" + suffissoTitolare + "'," + "[]);");

					
					fieldsJsScripts.put("codiceCliente"+ suffissoTitolare, 
							"bindRicercaBeneficiarioAutocomplete({beneficiarioFieldName: 'codiceCliente', "+
							"fieldName: 		'codiceCliente"+suffissoTitolare+"', "+
							"codAgente: 		'"+codiceAgente+"', "+
							"assicurando: 		'"+assicurando+"', "+
							"posizioneAssicurando: 		'"+posizioneAssicurando+"', "+
							"tipoRicerca: 	'TIT', "+
							"prefissoBeneficiario: 		'"+prefissoBeneficiario+"', "+
							"indiceBeneficiario: 		'"+i+"', "+
							"indiceTitolare: 		'"+j+"', "+
							"minLenght: 		'3'});");

			
					fieldsJsScripts.put("cognome"+ suffissoTitolare, 
							"bindRicercaBeneficiarioAutocomplete({beneficiarioFieldName: 'cognome', "+
							"fieldName: 		'cognome"+suffissoTitolare+"', "+
							"codAgente: 		'"+codiceAgente+"', "+
							"assicurando: 		'"+assicurando+"', "+
							"posizioneAssicurando: 		'"+posizioneAssicurando+"', "+
							"tipoRicerca: 	'TIT', "+
							"prefissoBeneficiario: 		'"+prefissoBeneficiario+"', "+
							"indiceBeneficiario: 		'"+i+"', "+
							"indiceTitolare: 		'"+j+"', "+
							"minLenght: 		'3'});");
					
					
					fieldsJsScripts.put("codiceFiscale"+ suffissoTitolare, 
							"bindRicercaBeneficiarioAutocomplete({beneficiarioFieldName: 'codiceFiscale', "+
							"fieldName: 		'codiceFiscale"+suffissoTitolare+"', "+
							"codAgente: 		'"+codiceAgente+"', "+
							"assicurando: 		'"+assicurando+"', "+
							"posizioneAssicurando: 		'"+posizioneAssicurando+"', "+
							"tipoRicerca: 	'TIT', "+
							"prefissoBeneficiario: 		'"+prefissoBeneficiario+"', "+
							"indiceBeneficiario: 		'"+i+"', "+
							"indiceTitolare: 		'"+j+"', "+
							"minLenght: 		'3'});");
					
					initAction("apriPopupRicercaAction" + suffissoTitolare, "<img id='apriPopupRicercaAction"
							+ suffissoTitolare
							+ "' src='/PdfWebFormsPolizzeUtil/images/search.gif' onclick='pdfPageDriver.openPopupRicercaTitolari(false,\""
							+ prefissoBeneficiario + "\"," + i + "," + j + "," + codiceAgente
							+ ");' style='cursor:pointer;'></img>", input);
					
					
					
					fieldsJsScripts.put("comune" + suffissoTitolare,
							"pdfPageDriver.bindComuneAutocomplete({" + "comuneFieldName: 'comune', " + "fieldName: 'comune"
									+ suffissoTitolare + "', "
									+ "driverCmd: 'prgm.pdfwebformsutil.drivers.autocompletion.comune.ComuneAutocomplete'});");

					fieldsJsScripts.put("capComune" + suffissoTitolare,
							"pdfPageDriver.bindComuneAutocomplete({" + "comuneFieldName: 'capComune', "
									+ "fieldName: 'capComune" + suffissoTitolare + "', "
									+ "driverCmd: 'prgm.pdfwebformsutil.drivers.autocompletion.comune.ComuneAutocomplete'});");

					fieldsJsScripts.put("nazioneComune" + suffissoTitolare,
							"bindNazioneAutocomplete('nazioneComune" + suffissoTitolare + "'," + "[]);");

					fieldsJsScripts.put("toponimoIndirizzo" + suffissoTitolare,
							"bindToponimoAutocomplete('toponimoIndirizzo" + suffissoTitolare + "',[]);");
				}

			}

			fieldsJsScripts.put("comune" + suffissoBeneficiario,
					"pdfPageDriver.bindComuneAutocomplete({" + "comuneFieldName: 'comune', " + "fieldName: 'comune"
							+ suffissoBeneficiario + "', "
							+ "driverCmd: 'prgm.pdfwebformsutil.drivers.autocompletion.comune.ComuneAutocomplete'});");

			fieldsJsScripts.put("capComune" + suffissoBeneficiario,
					"pdfPageDriver.bindComuneAutocomplete({" + "comuneFieldName: 'capComune', "
							+ "fieldName: 'capComune" + suffissoBeneficiario + "', "
							+ "driverCmd: 'prgm.pdfwebformsutil.drivers.autocompletion.comune.ComuneAutocomplete'});");

			fieldsJsScripts.put("nazioneComune" + suffissoBeneficiario,
					"bindNazioneAutocomplete('nazioneComune" + suffissoBeneficiario + "'," + "[]);");

			fieldsJsScripts.put("toponimoIndirizzo" + suffissoBeneficiario,
					"bindToponimoAutocomplete('toponimoIndirizzo" + suffissoBeneficiario + "',[]);");

		}

	}
	
	public static void buildJsScriptsRicercaPopup(PageLoadInputData input,
			String prefisso, String eventObj, String titolo, String i, String escludiXxx) {
				
		StringType codiceAgente = prgm.pdfwebformspolizzeutil.Utils.getCodiceAgente(input.getPdfData());
		
		String index = "\"\"";
		if(!i.equalsIgnoreCase("")) {
			index = i;
		}
		initAction("apriPopupRicercaAction"+ i , "<img id='apriPopupRicercaAction"+ i					
				+ "' src='/PdfWebFormsPolizzeUtil/images/search.gif' onclick='pdfPageDriver.openRicercaPopup("+ codiceAgente
				+","+ index +",\""+ prefisso +"\","+ eventObj +",\""+ titolo +"\",\""+ escludiXxx+"\");' style='cursor:pointer;'></img>", input);
		
	}
	
	public static void initAction(String actionName, String html, PageLoadInputData input) {
		ArrayList<PdfActionInfos> actions = input.getPdfData().getPdfInfos().findActions(actionName);
		for (PdfActionInfos action : actions)
			action.html = html;
	}

	public static void buildDrawHeader(StringBuilder result, boolean assicurando) {

		if (result == null) {
			result = new StringBuilder();
		}

		result.append("<script src=\"/PdfWebFormsUtil/autocompletion/NazioneAutocomplete.js\"></script>");
		result.append("<script src=\"/PdfWebFormsUtil/autocompletion/ToponimoAutocomplete.js\"></script>");
		result.append("<script src=\"/PdfWebFormsUtil/autocompletion/ComuneNascitaAutocomplete.js\"></script>");


		result.append("<script src=\"/PdfWebFormsPolizzeUtil/javascript/PdfPageDriverPolizzeUtil.js\"></script>");
		result.append("<script src=\"/PdfWebFormsPolizzeUtil/javascript/PdfPageDriverBeneficiariUtil.js\"></script>");
		result.append("<script src=\"/PdfWebFormsPolizzeUtil/javascript/PdfPageDriverReferenteTerzoUtil.js\"></script>");
		result.append("<script src=\"/PdfWebFormsPolizzeUtil/javascript/PdfPageDriverMotivazionePepUtil.js\"></script>");
		result.append("<script src=\"/PdfWebFormsPolizzeUtil/javascript/PdfPageDriverDichiarazioniBeneficiariUtil.js\"></script>");

		result.append("<script src=\"/PdfWebFormsPolizzeUtil/javascript/Popup.js\"></script>");
		result.append("<script src=\"/PdfWebFormsPolizzeUtil/javascript/PopupTitolari.js\"></script>");

		//nuovo codice
		result.append("<script src=\"/PdfWebFormsPolizzeUtil/javascript/RicercaPopup.js\"></script>");
		
		result.append("<script src=\"/PdfWebFormsPolizzeUtil/javascript/PdfTooltip.js\"></script>");

		result.append("<script src=\"/PdfWebFormsPolizzeUtil/sezioni/Beneficiari.js\"></script>");
		result.append("<script src=\"/PdfWebFormsPolizzeUtil/sezioni/ReferenteTerzo.js\"></script>");
		result.append("<script src=\"/PdfWebFormsPolizzeUtil/sezioni/MotivazionePep.js\"></script>");
		result.append("<script src=\"/PdfWebFormsPolizzeUtil/sezioni/DichiarazioniBeneficiari.js\"></script>");
		
		result.append("<script src=\"/PdfWebFormsPolizzeUtil/autocompletion/RicercaBeneficiarioAutocomplete.js\"></script>");
		result.append("<script src=\"/PdfWebFormsPolizzeUtil/autocompletion/ComuneNascitaBeneficiarioAutocomplete.js\"></script>");


		if (assicurando) {
			result.append("<script src=\"/PdfWebFormsPolizzeUtil/sezioni/Assicurando.js\"></script>");
		}
	}

	public static void buildJsScriptsSezioneReferenteTerzo(
			Map<String, String> fieldsJsScripts) {
		if (fieldsJsScripts == null) {
			fieldsJsScripts = new HashMap<String, String>();
		}

		fieldsJsScripts.put("toponimoIndirizzoReferenteTerzo",
				"bindToponimoAutocomplete('toponimoIndirizzoReferenteTerzo',[]);");

		fieldsJsScripts.put("capComuneReferenteTerzo",
				"pdfPageDriver.bindComuneAutocomplete({" + "comuneFieldName: 'capComune', "
						+ "fieldName: 'capComuneReferenteTerzo', "
						+ "driverCmd: 'prgm.pdfwebformsutil.drivers.autocompletion.comune.ComuneAutocomplete'});");

		fieldsJsScripts.put("comuneReferenteTerzo",
				"pdfPageDriver.bindComuneAutocomplete({" + "comuneFieldName: 'comune', "
						+ "fieldName: 'comuneReferenteTerzo', "
						+ "driverCmd: 'prgm.pdfwebformsutil.drivers.autocompletion.comune.ComuneAutocomplete'});");

		fieldsJsScripts.put("nazioneComuneReferenteTerzo",
				"bindNazioneAutocomplete('nazioneComuneReferenteTerzo'," + "[]);");

	}

	public static void buildDrawHeaderTerzoPagatore(StringBuilder result) {

		if (result == null) {
			result = new StringBuilder();
		}

		result.append("<script src=\"/PdfWebFormsPolizzeUtil/javascript/PdfPageDriverPolizzeUtil.js\"></script>");
		result.append("<script src=\"/PdfWebFormsPolizzeUtil/javascript/PdfPageDriverBeneficiariUtil.js\"></script>");
		result.append("<script src=\"/PdfWebFormsPolizzeUtil/sezioni/TerzoPagatore.js\"></script>");
	}
}
