package prgm.pdfwebforms.legalerappresentante;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOOSBResultModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;
import com.atosorigin.wfem.util.XmlServiceCallData;

import prgm.pdfwebforms.backend.PdfUtil;
import prgm.pdfwebforms.core.PdfConfig;
import prgm.pdfwebforms.core.PdfPredefinedFields;
import prgm.pdfwebforms.drivers.PdfBaseDriverUtil;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.model.PdfPersonModel;
import prgm.pdfwebforms.pritmom.PritMomInfo;
import prgm.pdfwebforms.pritmom.PritMomInfoLoader;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;

/*****************************************************************************************************/
/*****************************************************************************************************/
public class LegaleRappresentanteManager {
	
	private static final String DAO_XML =  "PdfWebForms.PdfVerifiers";
	private static final String SEZIONE_PERSONE_GIURIDICHE = "PERSONE_GIURIDICHE";
	private static final String FNAME_NDG_CLIENTE2 = "ndgCliente2";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private LegaleRappresentanteManager() {
		throw new IllegalStateException("LegaleRappresentanteUtility class");
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void initGestioneLegaleRappresentanteAttiva(ClientSessionContext csc, PdfModel pdf) throws Exception{
		if(pdf.isGestioneLegaleRappresentanteAttivaGiaCaricata())
			return;
		
		PdfAnagModel pdfAnag = pdf.mainPdfAnag();
		StringType elencoModuli = PdfConfig.getExtendedValue(csc, SEZIONE_PERSONE_GIURIDICHE, "DISPOSITIVE_GESTITE");
		if(elencoModuli.isNull()) {
			pdf.setGestioneLegaleRappresentanteAttiva(false);
			pdf.setGestioneLegaleRappresentanteAttivaGiaCaricata(true);
			return;
		}
		if(elencoModuli.equals("*")) {
			pdf.setGestioneLegaleRappresentanteAttiva(true);
			pdf.setGestioneLegaleRappresentanteAttivaGiaCaricata(true);
			return;
		}
		List<String> elencoModuliAsArr = new ArrayList<>((Arrays.asList(elencoModuli.toString().replace("\n", "").replace(" ","").split("\\,"))));
		boolean gestioneLegaleRappresentanteAttiva = elencoModuliAsArr.contains(pdfAnag.getPdfDriverName().toString()) || 
													 elencoModuliAsArr.contains(pdfAnag.getPdfMomCode().toString()) ||
													 elencoModuliAsArr.contains(pdf.getPdfData().getExternalEntityAppl().toString());
		pdf.setGestioneLegaleRappresentanteAttiva(gestioneLegaleRappresentanteAttiva);
		pdf.setGestioneLegaleRappresentanteAttivaGiaCaricata(true);
	}
	
	/***********************************************************************************************/
	/*
	 * In caso di sottoscrittore persona giuridica impostiamo il cliente2
	 */
	/***********************************************************************************************/
	public static boolean initLegaleRappresentante(PdfModel pdf) throws Exception{
		
		if(!pdf.isGestioneLegaleRappresentanteAttiva() || !pdf.isSottoscrittorePG())
			return false;

		PdfDataModel pdfData = pdf.getPdfData();
		// Se LR passato in input dal chiamante
		if(!pdfData.getCodiceLegaleRappresentante().isNull()) {
			pdfData.startPdfInitialInputDataInitialization();	
			pdfData.write(FNAME_NDG_CLIENTE2, new StringType(pdfData.getCodiceLegaleRappresentante().toString()));
			if(pdfData.fieldExist("isCliente2LegaleRappresentante"))
				pdfData.write("isCliente2LegaleRappresentante", new BooleanType(true));
			if(pdfData.fieldExist("tipoCliente2"))
				pdfData.write("tipoCliente2", new StringType("LEGALERAPP"));
			if(pdfData.fieldExist("legaleRappresentante2"))
				pdfData.write("legaleRappresentante2", new BooleanType(true));
			if(pdfData.fieldExist("isCliente2FirmatarioAssegno"))
				pdfData.write("isCliente2FirmatarioAssegno", new BooleanType(false));
			if(pdfData.fieldExist("isClienteLegaleRappresentante2"))
				pdfData.write("isClienteLegaleRappresentante2", new BooleanType(true));
			pdfData.stopPdfInitialInputDataInitialization();
			return true;
		}
		
		// Se non primo modulo e l'LR non esiste o è il cliente2
		if(pdfData.getPdfIndex().intValue() > 0 && pdfData.getIndiceLegaleRappresentante().intValue() >= 0 && pdfData.fieldExist(FNAME_NDG_CLIENTE2)) { 
			PdfPersonModel lr = findLegaleRappresentantePG(pdf.mainPdfData());
			if(lr != null) {
				pdfData.startPdfInitialInputDataInitialization();	
				pdfData.write(FNAME_NDG_CLIENTE2, new StringType(lr.getNdg().toString()));
				pdfData.stopPdfInitialInputDataInitialization();
				return true;
			}
		}
		
		return false;
	}	

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void verificaLegaleRappresentantePG(ClientSessionContext csc, PdfModel pdf) {
		if(!pdf.isGestioneLegaleRappresentanteAttiva() ||
		   !pdf.isSottoscrittorePG() || 
			pdf.getPdfData().getIndiceLegaleRappresentante().intValue() < 0 || /* Il driver dice che il LR non c'è */
		   !pdf.getPdfData().fieldExist(FNAME_NDG_CLIENTE2))
			return;
		
		StringType personIndex = new StringType();
		PdfPersonModel lr = findLegaleRappresentantePG(pdf.getPdfData(), personIndex);
		if(lr == null) {
			PdfUtil.putError(pdf.getPdfData(), new ArrayList<>(Arrays.asList(FNAME_NDG_CLIENTE2)), 
							"Il Sottoscrittore del Contratto è una persona Giuridica, è obbligatorio indicare il Legale Rappresentante. "+
							"Se il cliente non è presente nel tuo portafoglio puoi selezionarlo inserendone direttamente il codice.");
		}else {
			if(lr.isPersonaGiuridica()) {
				PdfUtil.putError(pdf.getPdfData(), new ArrayList<>(Arrays.asList(PdfPredefinedFields.CLIENTE_NDG_PREFIX+personIndex)), "Il legale rappresentante deve essere persona fisica.");
			}else {
				int eta = lr.etaPersona();
				if(eta < 18)
					PdfUtil.putError(pdf.getPdfData(), new ArrayList<>(Arrays.asList(PdfPredefinedFields.CLIENTE_NDG_PREFIX+personIndex)), "Il legale rappresentante deve essere maggiorenne.");
				else
					new PdfBaseDriverUtil().ctrl_isClienteBloccato(csc, pdf.getPdfData(), PdfPredefinedFields.CLIENTE_NDG_PREFIX+personIndex);
			}
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static PdfPersonModel findLegaleRappresentantePG(PdfDataModel pdfData){
		return findLegaleRappresentantePG(pdfData, null);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static PdfPersonModel findLegaleRappresentantePG(PdfDataModel pdfData, StringType personIndex){
		int cliIdx = 1;
		PdfPersonModel legRapp = pdfData.getPerson(++cliIdx);
		if(legRapp == null || legRapp.isEmty()) {
			if(legaleRappresentanteFieldExist(pdfData)) {
				legRapp = pdfData.getPerson(++cliIdx);
				if(legRapp == null || legRapp.isEmty())
					legRapp = pdfData.getPerson(++cliIdx);
			}
		}
		if(legRapp == null || legRapp.isEmty())
			return null;
		if(personIndex != null)
			personIndex.setStringValue(""+cliIdx);
		return legRapp;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static boolean legaleRappresentanteFieldExist(PdfDataModel pdfData) {
		return  pdfData.fieldExist("isCliente3LegaleRappresentante") || 
				pdfData.fieldExist("tipoCliente3") || pdfData.fieldExist("tipoCliente4") ||
				pdfData.fieldExist("legaleRappresentante3") || pdfData.fieldExist("legaleRappresentante4") ||
				pdfData.fieldExist("isClienteLegaleRappresentante3");
	}

	/*****************************************************************************************************/
	/*
	 * In caso di sottoscrittore persona giuridica creiamo una copia della lista dei clienti sostituendo
	 * al primo il legale rappresentante che è di default il secondo piuttosto che quello indicato dal driver
	 * Se poi il driver indica che esistono firme che il LR deve apporre lo lasciamo nell'elenco altrimenti no
	 */
	/*****************************************************************************************************/
	@SuppressWarnings("unchecked")
	public static List<PdfPersonModel> elencoClientiFirmatariPG(PdfModel pdf, PdfDataModel pdfData){

		if(!pdf.isGestioneLegaleRappresentanteAttiva())
			return pdfData.getClienti();
			
		PdfDataModel mainPdfData = pdf.mainPdfData();
		if(mainPdfData.getClienti().isEmpty())
			return pdfData.getClienti();
		
		PdfPersonModel sottoscrittore = mainPdfData.getPerson(1);
		if(sottoscrittore == null || sottoscrittore.isEmty() || !sottoscrittore.isPersonaGiuridica())
			return pdfData.getClienti();
	
		try {

			// Prendo il legale rappresentante specificato dai dati attutali oppure il primo/secondo co-sottoscrittore del modulo princpale
			// Se l'indice specificato è negativo il driver indica che l'LR sul modulo non c'è (il cliente2 è un altro soggetto)
			// Se l'indice specificato è vuoto la gestione si basa sul modulo principale
			// Se l'indice specificato è maggiore di zero la gestione si basa sul modulo corrente
			int indiceLegaleRappresentante = pdfData.getIndiceLegaleRappresentante().intValue(); 
			int legRappIdx = indiceLegaleRappresentante <= 0 ? 2 : pdfData.getIndiceLegaleRappresentante().intValue();
			PdfPersonModel legRapp = indiceLegaleRappresentante <= 0 ? mainPdfData.getPerson(legRappIdx) : pdfData.getPerson(legRappIdx);
			if(indiceLegaleRappresentante <= 0 && (legRapp == null || legRapp.isEmty())) { // Se non è stato indicato un indice specifico e il primo co-sottoscrittore non è compilato provo con il secondo co-sottoscrittore  (ndg3)
				legRappIdx++;
				legRapp = mainPdfData.getPerson(legRappIdx);
				if(legRapp == null || legRapp.isEmty()) { // Se il secondo co-sottoscrittore non è compilato provo con il terzo co-sottoscrittore (ndg4)
					legRappIdx++;
					legRapp = mainPdfData.getPerson(legRappIdx);
				}
			}
			if(legRapp.isEmty())
				return pdfData.getClienti();

			List<PdfPersonModel> clonedClienti = (List<PdfPersonModel>)Tools.cloneObject(pdfData.getClienti());
			clonedClienti.set(0, (PdfPersonModel)Tools.cloneObject(legRapp));		// Imposto come sottoscrittore il LR
			if(!pdfData.getEsistonoFirmeDelLegaleRappresentante().booleanValue() && indiceLegaleRappresentante >= 0)	// Se l'LR non deve firmare nulla ed esiste lo rimuovo
				clonedClienti.remove(legRappIdx-1);			
			return clonedClienti;
			
		}catch(Exception e) {
			// Do nothing
		}
		return pdfData.getClienti();
	}

	/***********************************************************************************************/
	// Richiamato anche da WS, motivo per cui torna ValutazionePoteriDiFirmaSrvModel
	private static final String OK = "OK";
	private static final String ERR_FO_SUFFIX = " Per consentire i dovuti controlli, è necessario procedere in modalità Stampa e Firma";
	private static final String ERR_DAO_POTERI_MSG =  "Attenzione, per problemi di sistema al momento non è possibile verificare i poteri di firma dei soggetti collegati alla società."+
													  ERR_FO_SUFFIX;
	private static final String ERR_HTTP_POTERI_MSG = "Attenzione, per problemi di comunicazione al momento non è possibile verificare i poteri di firma dei soggetti collegati alla società."+
													  ERR_FO_SUFFIX;
	private static final String ERR_POTERI_MSG = "Attenzione, al momento non è possibile verificare i poteri di firma dei soggetti collegati alla società."+
												 ERR_FO_SUFFIX;
	
	private static final Map<String, String> errori = new HashMap<>();
	static {
		errori.put("A2", "Il cliente non è autorizzato alla firma in quanto non associato alla società selezionata.");
		errori.put("A3", "Attenzione, il cliente non è autorizzato alla firma della proposta in quanto non detiene i poteri di straordinaria amministrazione.");
		errori.put("B4", "Il soggetto non è autorizzato ad operare dal canale selezionato.");
		errori.put("A4", "Attenzione, la tipologia di operazione non rientra nei poteri attribuiti al firmatario selezionato.");
		errori.put("B1", "Attenzione, l'importo dell'operazione supera il limite autorizzato per il firmatario selezionato.");
	}
	private static final Map<String, String> warning = new HashMap<>();
	static {
		warning.put("A0", "Attenzione, al momento non è possibile verificare i poteri di firma dei soggetti collegati alla società. Per consentire i dovuti controlli, è necessario procedere in modalità Stampa e Firma.");
		warning.put("B2", "Attenzione, le delibere societarie prevedono dei limiti rispetto all'operazione disposta. Per consentire i dovuti controlli, è necessario procedere in modalità Stampa e Firma.");
		warning.put("B3", "Attenzione, l'operazione è soggetta a firma congiunta da parte degli aventi poteri, pertanto, è necessario procedere in modalità Stampa e Firma.");
	}
	/***********************************************************************************************/
	public static ValutazionePoteriDiFirmaSrvModel valutaPoteriDiFirmaLegaleRappresentantePG(ClientSessionContext csc, PdfModel pdf) {

		ValutazionePoteriDiFirmaSrvModel srvModel = new ValutazionePoteriDiFirmaSrvModel();
		
		try {

			// Se non è attiva la gestione del legale rappresentante o non siamo sul primo modulo usciamo
			if(!pdf.isGestioneLegaleRappresentanteAttiva() || 
				pdf.getPdfData().getPdfIndex().intValue() > 0 ||
			   !pdf.isSottoscrittorePG()) {
				srvModel.setCallNonEffettuataMsg("Fuori ambito");
				return srvModel;
			}

			// Nel caso di piattaforma conto PG (ONBOARDINGAZIENDE) non facciamo il controllo 
			// dei poteri e lasciamo inalterate le modalità di firma passate in input dal sistema HOST
			if(pdf.getPdfData().getExternalEntityAppl().equals("ONBOARDINGAZIENDE")) {
				srvModel.setCallNonEffettuataMsg("Ambito ONBOARDINGAZIENDE");
				return srvModel;
			}
			
			// Se la valutazione dei poteri è disabilitata inibiamo FD/Copernico
			BooleanType valutazioneDisabilitata = PdfConfig.getParamAsBool(csc, SEZIONE_PERSONE_GIURIDICHE, "VALUTAZIONE_POTERI_DI_FIRMA_PG_DISABILITATO");
			if(valutazioneDisabilitata.booleanValue()) {
				srvModel.setCallNonEffettuataMsg("Valutazione disabilitata");
				setSoloFirmaOlogrfa(pdf, "Il sottoscrittore corrisponde a persona giuridica, la sottoscrizione tramite Firma Digitale o Copernico non è consentita.");
				return srvModel;
			}
			
			// Se il legale non è compilato, cosa comunque verificata nei controlli, ma anche non esiste ndgCliente2 solo FO
			PdfPersonModel lr = LegaleRappresentanteManager.findLegaleRappresentantePG(pdf.getPdfData());
			if(lr == null) { 
				srvModel.setCallNonEffettuataMsg("Nessun LR");
				if(!pdf.getPdfData().fieldExist(PdfPredefinedFields.CLIENTE_NDG_PATTERN+"2"))
					setSoloFirmaOlogrfa(pdf, "Il sottoscrittore corrisponde a persona giuridica e non è presente il Legale Rappresentante. E' possibile procedere solo in modalità Stampa e Firma.");
				return srvModel;
			}

			// L'LR non è impostato. Se esiste una PG dal terzo ndg in poi
			if(pdf.existOtherSottoscrittoriPG()) {
				srvModel.setCallNonEffettuataMsg("Più di una PG");
				setSoloFirmaOlogrfa(pdf, "Almeno uno dei co-sottoscrittori corrisponde a persona giuridica, la sottoscrizione tramite Firma Digitale e Copernico non è consentita.");
				return srvModel;
			}
			
			PdfPersonModel sottoscrittorePG = pdf.mainPdfData().getPerson(1);

			srvModel.setUserId(new StringType(csc.getUserCode()));
			srvModel.setCodiceNdgPersonaGiuridica(sottoscrittorePG.getCodicePersona());
			srvModel.setCodiceNdgFirmatario(lr.getCodicePersona());
			srvModel.setImporto(new DoubleType(pdf.mainPdfData().readAsString(PdfPredefinedFields.IMPORTO)));
			srvModel.setDataSottoscrizione(new DateType(pdf.mainPdfData().readAsString(PdfPredefinedFields.DATA_SOTTOSCRIZIONE)));
			srvModel.setPdfInstanceId(pdf.getPdfData().getPdfInstanceId());
			initCodPrit(csc, pdf, srvModel);
			
			DAOOSBResultModel wsRes = new DAOObject(csc, DAO_XML).executeOSBAccess("valutaPoteriDiFirmaLegaleRappresentante",srvModel);
			if(wsRes.getWsCallData().getStatus() != XmlServiceCallData.STATUS_OK){
				srvModel.setSystemErrorMsg("Errore di comunicazione con il servizio dei poteri di firma: "+wsRes.getWsCallData().getMessage());
				setSoloFirmaOlogrfa(pdf, ERR_HTTP_POTERI_MSG);
				return srvModel;
			}
			
			if(srvModel.getEsitoValutazioneFirmatario().equalsIgnoreCase(OK)) {
				pdf.addCommandWarning("Il sottoscrittore corrisponde a persona giuridica, la sottoscrizione tramite Copernico non è consentita.");
				return srvModel;
			}
			
			String esitoFirmatario = findEsitoFirmatario(srvModel);
			if(esitoFirmatario == null) {
				setSoloFirmaOlogrfa(pdf, ERR_POTERI_MSG);
				return srvModel;
			}
			
			if(errori.containsKey(esitoFirmatario))
				pdf.addCommandError(errori.get(esitoFirmatario));
			else if(warning.containsKey(esitoFirmatario))
				setSoloFirmaOlogrfa(pdf, warning.get(esitoFirmatario));
			
		}catch(Exception | DAOException e) {
			srvModel.setSystemErrorMsg("Eccezione nella valutazione dei poteri di firma: "+e.toString());
			setSoloFirmaOlogrfa(pdf, ERR_DAO_POTERI_MSG);
		}
		return srvModel;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static String findEsitoFirmatario(ValutazionePoteriDiFirmaSrvModel srvModel) {
		// Cerco prima gli errori, poi se non trovati i warning
		for(int i=0;i<srvModel.getErroriFirmatario().size();i++) {
			ErroreFirmatarioModel esitoFirmatario = (ErroreFirmatarioModel)srvModel.getErroriFirmatario().get(i);
			String codiceErrore = esitoFirmatario.getCodiceErrore().toString();
			if(errori.containsKey(codiceErrore))
				return codiceErrore;
		}
		for(int i=0;i<srvModel.getErroriFirmatario().size();i++) {
			ErroreFirmatarioModel esitoFirmatario = (ErroreFirmatarioModel)srvModel.getErroriFirmatario().get(i);
			String codiceErrore = esitoFirmatario.getCodiceErrore().toString();
			if(warning.containsKey(codiceErrore)) {
				return codiceErrore;
			}
		}
		return null;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void setSoloFirmaOlogrfa(PdfModel pdf, String warning) {
		PdfDataModel pdfData = pdf.getPdfData();
		if(pdf.isMultiPdf()){
			PdfDataModel pdfElement = (PdfDataModel)pdfData.getPdfs().get(pdfData.getPdfIndex().intValue());			
			pdfElement.setCompilationModes(new StringType(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_LIBERA));
		}
		pdfData.setCompilationModes(new StringType(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_LIBERA));
		pdf.addCommandWarning(warning);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void initCodPrit(ClientSessionContext csc, PdfModel pdf, ValutazionePoteriDiFirmaSrvModel srvModel) {
		PdfAnagModel pdfAnag = pdf.mainPdfAnag();
		srvModel.setCodProdottoPrit(pdfAnag.getPdfCodProdottoPrit());
		srvModel.setCodOperazionePrit(pdfAnag.getPdfCodOperazionePrit());
		// In caso di switch considero l'operazione di iniziale/aggiuntivo
		if(pdf.getPdfData().getIsSwitch().booleanValue() && pdf.isMultiPdf()){
			pdfAnag = pdf.getPdfAnags().get(1);
			String chiave = null;
			if(pdfAnag.getPdfDriverName().equals("fondiitaliainiziale"))
				chiave = "SWITCH_INI_FONDIITALIA";
			else if(pdfAnag.getPdfDriverName().equals("fondiirlandesiiniziale"))
				chiave = "SWITCH_INI_BESTBRANDS";
			else if(pdfAnag.getPdfDriverName().equals("fondiitaliaaggiuntivo"))
				chiave = "SWITCH_AGG_FONDIITALIA";
			else if(pdfAnag.getPdfDriverName().equals("fondiirlandesiaggiuntivo"))
				chiave = "SWITCH_AGG_BESTBRANDS";
			if(chiave != null) {
				PritMomInfo pritMomInfo = PritMomInfoLoader.loadPritMomInfo(csc, pdfAnag, chiave);
				if(pritMomInfo != null){
					srvModel.setCodProdottoPrit(new IntegerType(pritMomInfo.getCodProdotto()));
					srvModel.setCodOperazionePrit(new IntegerType(pritMomInfo.getCodOperazione()));
				}
			}
		}
		
	}
}
