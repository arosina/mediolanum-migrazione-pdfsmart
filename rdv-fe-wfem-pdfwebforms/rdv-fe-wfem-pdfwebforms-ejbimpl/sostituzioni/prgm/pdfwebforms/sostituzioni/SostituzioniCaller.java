package prgm.pdfwebforms.sostituzioni;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.DAOOSBResultModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.OSBCallData;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.core.PdfConfig;
import prgm.pdfwebforms.drivers.PdfDriverCaller;
import prgm.pdfwebforms.drivers.io.sostituzioni.ClienteSostituzioniModel;
import prgm.pdfwebforms.drivers.io.sostituzioni.OperazioneSostituzioniModel;
import prgm.pdfwebforms.drivers.io.sostituzioni.ProdottoSostituzioniModel;
import prgm.pdfwebforms.drivers.io.sostituzioni.ProvideSostituzioniDataResponse;
import prgm.pdfwebforms.mifid.MifidCaller;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class SostituzioniCaller {

	private static final String DAO = "PdfWebForms.PdfWebFormsSostituzioni";
	private static final String S_ERRORE = "Errore [";
	private static final String S_SI = "S";
	private static final String S_NO = "N";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private SostituzioniCaller() {}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void loadPreferenze(ClientSessionContext csc, PdfModel pdf) throws Exception{
		if(pdf.isTestMode() || !pdf.mainPdfAnag().getHasPreferenzeInPriips().booleanValue())
			return;
		
		String osbAccess = "getAnagraficaPreferenze";
		try {			
			PreferenzeClienteSostituzioniModel pref = pdf.getPreferenzeClienteSostituzioni();
			pref.setUserId(new StringType(csc.getUserCode()));
			pref.setResultCode(new StringType());
			pref.setResultDescription(new StringType());
			pref.getElencoPreferenze().clear();
			
			DAOOSBResultModel wsRes = new DAOObject(csc, DAO).executeOSBAccess(osbAccess, pref);
			if(wsRes.getWsCallData().getStatus() == OSBCallData.STATUS_SERVICE_DISABLED){
				pref.setResultCode(new StringType("-1"));
				pref.setResultDescription(new StringType("Servizio ["+osbAccess+"] disabilitato"));
			}else if(wsRes.getWsCallData().getStatus() != OSBCallData.STATUS_OK){	
				pref.setResultCode(new StringType("-1"));
				pref.setResultDescription(new StringType("Errore di comunicazione con il servizio ["+osbAccess+"]: "+wsRes.getWsCallData().getMessage()));
			}else if(!pref.getResultCode().equals("0")){	
				pref.setResultDescription(new StringType(S_ERRORE+pref.getResultCode()+"] dal servizio ["+osbAccess+"]: "+pref.getResultDescription()));
			}else{ // Tutto ok
				if(!pdf.getPdfData().getPdfInstanceId().isNull())
					loadPreferenzeCliente(csc, pdf, pref);
				eliminaPreferenzeNonDisponibiliAlCliente(csc, pdf, pref);
			}

		}catch(DAOException daoe){
			throw new Exception("Eccezione DAO nel richiamo al servizio ["+osbAccess+"]: "+daoe.toString());
		}
		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void loadPreferenzeCliente(ClientSessionContext csc, PdfModel pdf, PreferenzeClienteSostituzioniModel pref) throws Exception{

		String osbAccess = "getPreferenzeCliente";
		try {			
			PreferenzeClienteSostituzioniModel prefCli = new PreferenzeClienteSostituzioniModel();
			prefCli.setUserId(new StringType(csc.getUserCode()));
			prefCli.setIdProposta(new StringType(pdf.getPdfData().getPdfInstanceId().toString()));
			
			DAOOSBResultModel wsRes = new DAOObject(csc, DAO).executeOSBAccess(osbAccess, prefCli);
			if(wsRes.getWsCallData().getStatus() == OSBCallData.STATUS_SERVICE_DISABLED){
				pref.setResultCode(new StringType("-1"));
				pref.setResultDescription(new StringType("Servizio ["+osbAccess+"] disabilitato"));
			}else if(wsRes.getWsCallData().getStatus() != OSBCallData.STATUS_OK){	
				pref.setResultCode(new StringType("-1"));
				pref.setResultDescription(new StringType("Errore di comunicazione con il servizio ["+osbAccess+"]: "+wsRes.getWsCallData().getMessage()));
			}else if(!prefCli.getResultCode().equals("0")){	
				pref.setResultDescription(new StringType(S_ERRORE+pref.getResultCode()+"] dal servizio ["+osbAccess+"]: "+pref.getResultDescription()));
			}else{ // Tutto ok
				initPreferenzeCliente(pref, prefCli);
			}
			
		}catch(DAOException daoe){
			throw new Exception("Eccezione DAO nel richiamo al servizio ["+osbAccess+"]: "+daoe.toString());
		}
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void initPreferenzeCliente(PreferenzeClienteSostituzioniModel pref, PreferenzeClienteSostituzioniModel prefCli){
		for(int i=0;i<pref.getElencoPreferenze().size();i++) {
			PreferenzaClienteSostituzioniModel pAnag = (PreferenzaClienteSostituzioniModel)pref.getElencoPreferenze().get(i);
			for(int j=0;j<prefCli.getElencoPreferenze().size();j++) {
				PreferenzaClienteSostituzioniModel pCli = (PreferenzaClienteSostituzioniModel)prefCli.getElencoPreferenze().get(j);
				if(pAnag.getIdPreferenza().equals(pCli.getIdPreferenza())) {
					pAnag.setIsSelezionata(new BooleanType(true));
					break;
				}
			}
		}
	}

	/***********************************************************************************************/
	private static final String FNAME_NDG_CLIENTE1 = "ndgCliente1";
	/***********************************************************************************************/
	private static void eliminaPreferenzeNonDisponibiliAlCliente(ClientSessionContext csc, PdfModel pdf, PreferenzeClienteSostituzioniModel pref) {
		BooleanType isClienteConConsulenzaRispAmm = new BooleanType(false);
		AbstractType ndgCliente1 = pdf.mainPdfData().read(FNAME_NDG_CLIENTE1);
		if(ndgCliente1 != null && !ndgCliente1.isNull()) {
			try {
				MapCommandDataModel m = new MapCommandDataModel();
				m.addProperty(FNAME_NDG_CLIENTE1, new StringType(ndgCliente1.toString()));
				isClienteConConsulenzaRispAmm = (BooleanType)new DAOObject(csc, DAO).executeQueryAccess("isClienteConConsulenzaRispAmm", m).getSingleResult();
			}catch(DAOException daoe) {
				ndgCliente1.addTypeError(daoe.toString());
				return;
			}
		}
		for(int i=pref.getElencoPreferenze().size()-1;i>=0;i--) {
			PreferenzaClienteSostituzioniModel pAnag = (PreferenzaClienteSostituzioniModel)pref.getElencoPreferenze().get(i);
			if(pAnag.getPreferenzaAmministrato().equals(S_SI) && !isClienteConConsulenzaRispAmm.booleanValue())
				pref.getElencoPreferenze().remove(i);
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void savePreferenzeCliente(ClientSessionContext csc, PdfModel pdf) throws Exception{
		if(pdf.isTestMode() || !pdf.mainPdfAnag().getHasPreferenzeInPriips().booleanValue())
			return;

		String error  = null;
		try {			
			PreferenzeClienteSostituzioniModel callPref = new PreferenzeClienteSostituzioniModel();
			callPref.setUserId(new StringType(csc.getUserCode()));
			callPref.setIdProposta(new StringType(pdf.getPdfData().getPdfInstanceId().toString()));
			for(int i=0;i<pdf.getPreferenzeClienteSostituzioni().getElencoPreferenze().size();i++) {
				PreferenzaClienteSostituzioniModel p = (PreferenzaClienteSostituzioniModel)pdf.getPreferenzeClienteSostituzioni().getElencoPreferenze().get(i);
				if(p.getIsSelezionata().booleanValue())
					callPref.getElencoPreferenze().add(p);
			}
			
			DAOOSBResultModel wsRes = new DAOObject(csc, DAO).executeOSBAccess("putPreferenzeCliente", callPref);
			if(wsRes.getWsCallData().getStatus() == OSBCallData.STATUS_SERVICE_DISABLED){
				callPref.setResultCode(new StringType("-1"));
				error = "Servizio di scrittura preferenze cliente sostituzioni disabilitato";
			}else if(wsRes.getWsCallData().getStatus() != OSBCallData.STATUS_OK){	
				callPref.setResultCode(new StringType("-1"));
				error = "Errore di comunicazione con il servizio di scrittura preferenze cliente sostituzioni: "+wsRes.getWsCallData().getMessage();
			}else if(!callPref.getResultCode().equals("0")){	
				error = S_ERRORE+callPref.getResultCode()+"] dal servizio di scrittura preferenze cliente sostituzioni: "+callPref.getResultDescription();
			}	
		}catch(DAOException daoe){
			error = "Eccezione DAO nel richiamo al servizio di scrittura preferenze cliente sostituzioni: "+daoe.toString();
		}
		if(error != null)
			throw new Exception("\n\n"+error);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String callVerificaSostituzioni(ClientSessionContext csc, PdfModel pdf, boolean inPreview) throws Exception{

		if(pdf.getIsSede().booleanValue() || pdf.isTestMode() || !pdf.getPdfData().getIdSostituzione().isNull() || !pdf.getPdfData().getIdCarrello().isNull()) // Se in simulazione o l'id sostituzione è passato in input oppure siamo nel 5d non verifico le sostituzioni
			return null;

		// Gestione pilota
		StringType pilota = PdfConfig.getParamAsString(csc, "SOSTITUZIONI", "CODICI_AGENTE_GRUPPO_PILOTA", true);
		if(!pilota.isNull() && pilota.toString().indexOf(Tools.fillSx(csc.getUserCode(),'0',10)) < 0)
			return null;
		
		pdf.setSostituzioniCallModel(null);
		
		ProvideSostituzioniDataResponse sostituzioniData = PdfDriverCaller.callProvideSostituzioniData(csc, pdf);
		if(sostituzioniData == null)
			return null;
		
		String errTrans = transcodificaProdottiInSurrogati(csc, sostituzioniData);
		if(errTrans != null)
			return errTrans;
		
		SostituzioniCallModel callModel = pdf.getSostituzioniCallModel();
		if(callModel == null)
			callModel = new SostituzioniCallModel();
		callModel.setUserId(new StringType(csc.getUserCode()));
		callModel.setOperatoreSede(new StringType(csc.isSede()?S_SI:S_NO));
		callModel.setIdProposta(pdf.getPdfData().getPdfInstanceId());
		if(pdf.getMifidCallModel() != null)
			callModel.setIdAdeguatezza(pdf.getMifidCallModel().getIdEsito());
		callModel.setInput(sostituzioniData);
		
		String result = null;
		try {			
			DAOOSBResultModel wsRes = new DAOObject(csc, DAO).executeOSBAccess("verificaSostituzione", callModel);
			if(wsRes.getWsCallData().getStatus() == OSBCallData.STATUS_SERVICE_DISABLED){
				return "Servizio di verifica sostituzioni disabilitato";
			}else if(wsRes.getWsCallData().getStatus() != OSBCallData.STATUS_OK){	
				return "Errore di comunicazione con il servizio di verifica sostituzioni: "+wsRes.getWsCallData().getMessage();
			}else if(!callModel.getResultCode().equals("0")){	
				return S_ERRORE+callModel.getResultCode()+"] dal servizio di verifica sostituzioni: "+callModel.getResultDescription();
			}else if(callModel.getIdSostituzione().isNull()){	
				return "Il servizio di verifica sostituzioni non ha restituito l'id sostituzione";
			}else if(callModel.getEsitoVerificaSostituzioneGlobale().equals("NA") || 
					 callModel.getEsitoVerificaSostituzioneGlobale().equals("OK")){	
				pdf.setSostituzioniCallModel(callModel); // Sostituzioni Non Applicabili o OK, si prosegue senza segnalazioni
				return null;
			}else{
				pdf.setSostituzioniCallModel(callModel); // Sostituzioni da verificare
				result = esitoControlloKO(callModel.getClientiResult());
			}	
		}catch(DAOException daoe){
			return "Eccezione DAO nel richiamo al servizio sostituzioni: "+daoe.toString();
		}
		boolean controlloNonBloccante = controlloNonBloccante(csc);
		if(inPreview)
			return controlloNonBloccante ? MifidCaller.WARNING_INDICATOR+result : result;
		else
			return controlloNonBloccante ? null : result;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static String transcodificaProdottiInSurrogati(ClientSessionContext csc, ProvideSostituzioniDataResponse data){
		
		try {
			// Transcodifico i codici prodotto da codice fondo/comparto etc. in codice surrogato per il servizio
			DAOObject dao = new DAOObject(csc, DAO);
			
			for(int i=0;i<data.getClienti().size();i++) {
				ClienteSostituzioniModel c = (ClienteSostituzioniModel)data.getClienti().get(i);
				for(int j=0;j<c.getOperazioni().size();j++) {
					OperazioneSostituzioniModel o = (OperazioneSostituzioniModel)c.getOperazioni().get(j);
					for(int k=0;k<o.getProdotti().size();k++) {
						ProdottoSostituzioniModel p = (ProdottoSostituzioniModel)o.getProdotti().get(k);
						if(!p.getProdotto().isNull() && p.isDoProdottoDefaultTranslation()) {
							StringType codiceSurrogato = recuperaCodiceSurrogatoProdotto(dao, p.getProdotto());
							if(codiceSurrogato == null || codiceSurrogato.isNull()){
								return "Transcodifica sostituzioni: il prodotto "+p.getProdotto()+" non è stato trovato"; 
							}
							p.setProdotto(codiceSurrogato);
						}
						if(!p.getProdottoPadre().isNull() && p.isDoProdottoPadreDefaultTranslation()) {
							StringType codiceSurrogato = recuperaCodiceSurrogatoProdotto(dao, p.getProdottoPadre());
							if(codiceSurrogato == null || codiceSurrogato.isNull()){
								return "Transcodifica sostituzioni: il prodotto padre "+p.getProdottoPadre()+" non è stato trovato"; 
							}
							p.setProdottoPadre(codiceSurrogato);
						}
					}
				}
			}
			return null;
		}catch(DAOException daoe) {
			return "Eccezione DAO nella gestione delle transcodifiche in codici surrogati per il servizio sostituzioni: "+daoe.toString();
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static StringType recuperaCodiceSurrogatoProdotto(DAOObject dao, StringType prodotto) throws DAOException{
		ProdottoSostituzioniModel p = new ProdottoSostituzioniModel();
		p.setProdotto(prodotto);
		return (StringType)dao.executeQueryAccess("recuperaCodiceSurrogatoProdotto", p).getSingleResult();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static String esitoControlloKO(ListType clientiResult) {
		for(int i=0;i<clientiResult.size();i++) {
			ClienteResultModel cli = (ClienteResultModel)clientiResult.get(i);
			if(cli.getDeltaISD().doubleValue() < -1) // il minore di -1 è stato introdotto con la rfc #170336
				return "La proposta non risulta adeguata a causa di un peggioramento della diversificazione di portafoglio";
		}
		return "La proposta non risulta adeguata a causa del rapporto benefici/costi";
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static boolean controlloNonBloccante(ClientSessionContext csc) throws Exception{
        BooleanType nonBloccante = PdfConfig.getParamAsBool(csc, "SOSTITUZIONI", "CONTROLLO_NON_BLOCCANTE");
        if(nonBloccante != null && nonBloccante.booleanValue())
        	return true;
        return false;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String formattaErroriSostituzioni(String erroriSostituzioni) {
		if(erroriSostituzioni == null)
			return null;
		boolean isWarning = erroriSostituzioni.startsWith(MifidCaller.WARNING_INDICATOR);
		if(isWarning)
			return erroriSostituzioni.substring(MifidCaller.WARNING_INDICATOR.length());
		return erroriSostituzioni;
	}
}
