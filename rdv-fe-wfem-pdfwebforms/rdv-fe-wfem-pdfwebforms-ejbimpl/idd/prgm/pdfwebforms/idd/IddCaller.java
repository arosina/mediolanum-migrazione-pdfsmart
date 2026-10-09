package prgm.pdfwebforms.idd;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOOSBResultModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;
import com.atosorigin.wfem.util.XmlServiceCallData;

import prgm.pdfwebforms.drivers.PdfDriverCaller;
import prgm.pdfwebforms.drivers.io.idd.ProvideIddDataResponse;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.model.PdfPersonModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class IddCaller {

	private static final String DAO_XML_IDD = "PdfWebForms.PdfWebFormsIdd";	
	private static final String OK = "OK";
	private static final String NON_POSSIBILE_PROCEDERE_SUFFIX = "pertanto non è possibile procedere.";
	private static final String RACCOMANDAZIONE_NON_VALIDA = "Non è possibile procedere perchè la raccomandazione non risulta valida.";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String callIddOnLoad(ClientSessionContext csc, PdfModel pdf) throws Exception{
		String res = callIdd(csc, pdf, false, true);
		if(pdf.isOperatoreMOM()) // Per l'operatore MOM la chiamata la facciamo comunque ma eventuali errori non devono essere mostrati nell'onLoad
			return null;
		return res;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String callIdd(ClientSessionContext csc, PdfModel pdf) throws Exception{
		return callIdd(csc, pdf, false, false);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String callIdd(ClientSessionContext csc, PdfModel pdf, boolean forceCall) throws Exception{
		return callIdd(csc, pdf, forceCall, false);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static String callIdd(ClientSessionContext csc, PdfModel pdf, boolean forceCall, boolean onload) throws Exception{
		
		if( pdf.isTestMode())
			return null;

		ProvideIddDataResponse iddData = PdfDriverCaller.callProvideIddData(csc, pdf, onload);
		if(iddData == null)
			return null;

		// RFC #292576: la verifica IDD va fatta solo per gli iniziali
		if(iddData.getTipoDispositiva() != ProvideIddDataResponse.TIPO_DISPOSITIVA_INIZIALE)
			return null;
		// RFC #292576: la verifica IDD del ramo III dipende da pic/pac, non la si può fare nell'onLoad
		if(onload && iddData.getTipoVerfica() == ProvideIddDataResponse.VERIFICA_IDD_RAMO_III)
			return null;

		DateType oggi = Tools.today();
		
		try{
			
			if(iddData.getTipoVerfica() == ProvideIddDataResponse.VERIFICA_IDD_RAMO_PROTEZIONE){
				
				String errmsg = null;
				
				if(pdf.getIdRaccomandazioneIdd().length() == 0){
					errmsg = "Non è possibile procedere perchè non risulta valorizzato l'identificativo della raccomandazione IDD.";
					return errmsg;
				}
				
				if(pdf.mainPdfData().getClienti().isEmpty()){
					errmsg = "Non è possibile procedere perchè non risulta valorizzato alcun cliente, da prevalorizzare in caso di IDD ramo protezione.";
					return errmsg;
				}
				
				PdfPersonModel mainCli = pdf.mainPdfData().getClienti().get(0);
				if(mainCli == null || mainCli.isEmty()){
					errmsg = "Non è possibile procedere perchè non risulta valorizzato il cliente, da prevalorizzare in caso di IDD ramo protezione.";
					return errmsg;
				}
				
				boolean isCliEffettivo = mainCli.getNdg().isNull() ? false : true;
				if(!forceCall && pdf.getIddCallModel() != null){ // only one call for cli
					String curCodCli = isCliEffettivo ? mainCli.getNdg().toString() : mainCli.getIdCensimento().toString();
					if(pdf.getIddCallModel().getCodiceCliente().equals(curCodCli))
						return null;
				}
				 
				IddCallModel callModel = pdf.getIddCallModel();
				if(callModel == null)
					callModel = new IddCallModel();
				callModel.setInput(iddData);
				callModel.setUtente(new StringType(csc.getUserCode()));
				callModel.setDataRiferimento(new StringType(oggi.getAA()+"-"+oggi.getMM()+"-"+oggi.getGG()));
				callModel.setIdRaccomandazioneIdd(new StringType(pdf.getIdRaccomandazioneIdd()));
				callModel.setCodiceCliente(isCliEffettivo ? mainCli.getNdg() : mainCli.getIdCensimento());
				callModel.setTipoCliente(isCliEffettivo ? new StringType("2") : new StringType("3"));
				callModel.initRuoloAgente(pdf.getPdfData().getCodRuoloImpersonato());
				callModel.setProdottoInInput(iddData.getProdotto());
				
				pdf.setDataUltimaChiamataAdeguatezza(oggi.toString());
				
				String accessName = "verificaRaccomandazioneIdd";
				if(pdf.isInBasketSemplt())
					accessName = "verificaRaccomandazioneProtezioneIdd";
				DAOOSBResultModel wsRes = new DAOObject(csc, DAO_XML_IDD).executeOSBAccess(accessName, callModel);
				if(wsRes.getWsCallData().getStatus() == XmlServiceCallData.STATUS_SERVICE_DISABLED){
					return "Servizio di verifica raccomandazione IDD disabilitato";
				}else if(wsRes.getWsCallData().getStatus() != XmlServiceCallData.STATUS_OK){	
					return "Errore di comunicazione con il servizio di verifica raccomandazione IDD: "+wsRes.getWsCallData().getMessage();
				}else if(callModel.getIdRaccomandazioneIdd().isNull()){	
					return "La raccomandazione IDD numero ["+pdf.getIdRaccomandazioneIdd()+"] non esiste";
				}else if(!callModel.getIdRaccomandazioneIdd().equalsIgnoreCase(pdf.getIdRaccomandazioneIdd())){	
					return "Il servizio di verifica raccomandazione IDD ha restituito un ID raccomandazione ["+callModel.getIdRaccomandazioneIdd()+"] differente rispetto a quello richiesto ["+pdf.getIdRaccomandazioneIdd()+"]";
				}else{
					if(pdf.isInBasketSemplt()) {
						if(!callModel.getEsitoTecnico().equalsIgnoreCase(OK))
							return "Errore tecnico dal servizio di verifica raccomandazione IDD";
					}else{
						if(callModel.getFlagEsitoAdeguatezzaRaccomandazione().isNull()){	
							return "Il servizio di verifica raccomandazione IDD non ha restituito l'esito adeguatezza";
						}else if(callModel.getCodStatoQuestionarioIdd().isNull()){	
							return "Il servizio di verifica raccomandazione IDD non ha restituito lo stato del questionario";
						}
					}
					pdf.setIddCallModel(callModel);
					return null; // Idd OK
				}

			}else if(iddData.getTipoVerfica() == ProvideIddDataResponse.VERIFICA_IDD_RAMO_III || 
					 iddData.getTipoVerfica() == ProvideIddDataResponse.VERIFICA_IDD_RAMO_PREVIDENZA){
			
				PdfDataModel pdfData = onload ? pdf.getPdfData() : pdf.mainPdfData();
				
				if(pdfData.getClienti().isEmpty())
					return null;
				
				PdfPersonModel mainCli = pdfData.getClienti().get(0);
				if(mainCli == null || mainCli.isEmty())
					return null;
				
				DAOObject dao = new DAOObject(csc, DAO_XML_IDD);
				
				boolean isCliEffettivo = mainCli.getNdg().isNull() ? false : true;
				IddCallModel callModel = new IddCallModel();
				callModel.setUtente(new StringType(csc.getUserCode()));
				callModel.setInput(iddData);				
				callModel.setDataRiferimento(new StringType(oggi.getAA()+"-"+oggi.getMM()+"-"+oggi.getGG()));
				callModel.setCodiceCliente(isCliEffettivo ? mainCli.getNdg() : mainCli.getIdCensimento());
				callModel.setTipoCliente(isCliEffettivo ? new StringType("2") : new StringType("3"));
				callModel.setIdQuestionarioIddInInput(new StringType());
				
				// Calcolo il prodotto in input che per il ramo III è funzione anche di pic/pac
				// Se non valorizzato non facciamo la call
				String err = initProdottoInInput(dao, pdf.getIddCallModel(), callModel);
				if(err != null)
					return err;

				// Se la chiamata non è forzata e le condizioni non sono cambiate non la rifaccio
				if(!forceCall && pdf.getIddCallModel() != null){
					if(condizioniInvariate(pdf.getIddCallModel(), callModel)) 
						return null;
					// Se la chiamata non è forzata e invece le condizioni sono cambiate facciamo la call ripulendo la raccomandazione 
					// eventualmente già creata
					pdf.getIddCallModel().setIdRaccomandazioneIdd(new StringType());
				}
								
				if(pdf.isInAccettazioneCopernico() && pdf.getIddCallModel() != null) {	// In accettazione copernico deve essere passato l'id questionario utilizzato all'invio lato rete
					callModel.setIdQuestionarioIddInInput(new StringType(pdf.getIddCallModel().getIdQuestionarioIdd().toString()));
				}else if(!pdf.isInAccettazioneCopernico() && 
						  iddData.getTipoVerfica() == ProvideIddDataResponse.VERIFICA_IDD_RAMO_III &&
						  pdf.reportAdeguatezzaPassatoDalChiamate() && pdf.getIdReportAdeguatezza().length() > 0){	// Altrimenti, per il ramo III, se l'rda è passata in input dobbiamo verificare l'id questionario presente nell'RDA e non il corrente
					callModel.setIdReportAdeguatezza(new StringType(pdf.getIdReportAdeguatezza()));
					dao.executeQueryAccess("loadIdRaccomandazioneIddFromRda", callModel);
					if(callModel.getIdRaccomandazioneIdd().isNull())
						return "Nessuna raccomandazione trovata per l'RDA ["+callModel.getIdReportAdeguatezza()+"]";
					dao.executeOSBAccess("loadIdQuestionarioFromRaccomandazione", callModel);
					if(callModel.getIdQuestionarioIddInInput().isNull())
						return "Nessun questionario trovato per la raccomandazione ["+callModel.getIdRaccomandazioneIdd()+"] e RDA ["+callModel.getIdReportAdeguatezza()+"]";
				}
				
				pdf.setDataUltimaChiamataAdeguatezza(oggi.toString());
				
				String osbAccess = "verificaQuestionarioIdd";
				if(!callModel.getIdQuestionarioIddInInput().isNull())
					osbAccess = "verificaQuestionarioIddConIdQuestionario";
				DAOOSBResultModel wsRes = dao.executeOSBAccess(osbAccess, callModel);
				if(wsRes.getWsCallData().getStatus() == XmlServiceCallData.STATUS_SERVICE_DISABLED){
					return "Servizio di verifica questionario IDD disabilitato";
				}else if(wsRes.getWsCallData().getStatus() != XmlServiceCallData.STATUS_OK){	
					return "Errore di comunicazione con il servizio di verifica questionario IDD: "+wsRes.getWsCallData().getMessage();
				}else if(callModel.getFlagEsitoAdeguatezzaQuestionario().isNull()){	
					return "Il servizio di verifica questionario IDD ha restituito l'esito adeguatezza";
				}else{
					if(callModel.getIdQuestionarioIdd().isNull()){
						callModel.setCodStatoQuestionarioIdd(new StringType(IddStatiQuestionario.INESISTENTE));
						callModel.setDescrStatoQuestionarioIdd(new StringType("assente"));
					}
					callModel.setDescrStatoQuestionarioIdd(new StringType(callModel.getDescrStatoQuestionarioIdd().toString().toLowerCase()));
					
					pdf.setIddCallModel(callModel);
					
					// RFC #292576: salvo l'id del pca se non siamo da 5D, dove è lui che lo passa in input, e se siamo nel pilota
					if(!pdf.isInBasket()) {
						pdf.mainPdfData().setIdPCA(new StringType());
						if(callModel.getFlagControlloTMSottoscrizione().equals("S") || callModel.getFlagControlloTMSottoscrizione().equals("N"))
							pdf.mainPdfData().setIdPCA(new StringType(callModel.getIdQuestionarioIdd().toString())); 
					}
					return null; // Idd OK
				}
				
			}else{
				return null;
			}
			
		}catch(DAOException daoe){
			return "Eccezione nel richiamo al servizio IDD: "+daoe.toString();
		}
	}	

	/***********************************************************************************************/
	// RFC #292576: per il ramo III gestione prodotto funzione di pic/pac e pilota Target Market
	/***********************************************************************************************/
	private static String initProdottoInInput(DAOObject dao, IddCallModel currCallModel, IddCallModel newCallModel) throws DAOException{
		ProvideIddDataResponse iddData = newCallModel.getInput();
		newCallModel.setProdottoInInput(iddData.getProdotto());
		newCallModel.setTariffa(iddData.getTariffa());
		
		if(iddData.getTipoVerfica() == ProvideIddDataResponse.VERIFICA_IDD_RAMO_III) {
			// Tariffa non valorizzata dal driver -> esco lasciando impostato il prodotto standard
			if(newCallModel.getTariffa().isNull())
				return null;
			
			// Cliente e tariffa non modificati, torno il prodotto e il pilota già impostato se call già fatta
			if(currCallModel != null) {
				if( newCallModel.getCodiceCliente().equals(currCallModel.getCodiceCliente()) && 
					newCallModel.getTariffa().equals(currCallModel.getTariffa())) {
					newCallModel.setProdottoInInput(currCallModel.getProdottoInInput());
					newCallModel.setFlagControlloTMSottoscrizione(currCallModel.getFlagControlloTMSottoscrizione());
					return null;
				}
			}
			
			// Chiamo getPolizze per avere flagControlloTMSottoscrizione e impostare opportunamente il prodotto in input
			DAOOSBResultModel wsRes = dao.executeOSBAccess("readFlagControlloTargetMarket", newCallModel);
			if(wsRes.getWsCallData().getStatus() != XmlServiceCallData.STATUS_OK)
				return "Errore di comunicazione con il servizio getPolizze: "+wsRes.getWsCallData().getMessage();
	
			// Cliente abilitato TM
			// Nel caso in cui il driver non imposti ancora il prodotto Target Market usiamo quello standard
			if(newCallModel.getFlagControlloTMSottoscrizione().equals("S") || newCallModel.getFlagControlloTMSottoscrizione().equals("N"))
				newCallModel.setProdottoInInput(iddData.getProdottoTM().isNull() ? iddData.getProdotto() : iddData.getProdottoTM()); 
		}
		return null;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static boolean condizioniInvariate(IddCallModel currCallModel, IddCallModel newCallModel) {
		return newCallModel.getCodiceCliente().equals(currCallModel.getCodiceCliente()) && 
			   newCallModel.getProdottoInInput().equals(currCallModel.getProdottoInInput());
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static int readTipoVerificaIdd(PdfModel pdf){
		IddCallModel idd = pdf.getIddCallModel();
		if(idd == null)
			return -1;
		
		ProvideIddDataResponse iddData = idd.getInput();
		if(iddData == null)
			return -1;
		
		return iddData.getTipoVerfica();
	}
	
	/***********************************************************************************************/
	private static final String CONSENTITO_PROCEDERE_MA = "E' consentito procedere, ma non sarà possibile completare il processo di sottoscrizione del prodotto.";
	/***********************************************************************************************/
	public static String verificaIddInApertura(PdfModel pdf){
		IddCallModel idd = pdf.getIddCallModel();
		if(idd == null || pdf.isOperatoreMOM())
			return null;
		
		int tipoVerificaIdd = readTipoVerificaIdd(pdf);
		if(tipoVerificaIdd == ProvideIddDataResponse.VERIFICA_IDD_RAMO_PROTEZIONE){
			if(!idd.getEsitoTecnico().isNull()) { // Valorizzato solo con i nuovi servizi IDD
				if(!idd.getEsitoValutazione().equalsIgnoreCase(OK))
					return RACCOMANDAZIONE_NON_VALIDA;
			}else {
				if(!idd.getFlagEsitoAdeguatezzaRaccomandazione().equalsIgnoreCase("true")){
					if( idd.getCodStatoQuestionarioIdd().equals(IddStatiQuestionario.ANNULLATO) ||
						idd.getCodStatoQuestionarioIdd().equals(IddStatiQuestionario.INCOMPLETO)){
						return "Non è possibile procedere perchè il prospetto di consulenza assicurativa protezione non risulta valido.";				
					}else{					
						return "Non è possibile procedere perchè la raccomandazione è scaduta.<br>E' necessario generarla nuovamente da Protection Easy Tool.";
					}
				}
			}
		}else if(tipoVerificaIdd == ProvideIddDataResponse.VERIFICA_IDD_RAMO_III){
			if( !idd.getCodStatoQuestionarioIdd().equals(IddStatiQuestionario.VALIDO) && 
				!idd.getCodStatoQuestionarioIdd().equals(IddStatiQuestionario.PROVVISORIO) &&
				!idd.getCodStatoQuestionarioIdd().equals(IddStatiQuestionario.SOSPESO)){
				return  "Il prospetto di consulenza assicurativa investimenti assicurativi risulta "+idd.getDescrStatoQuestionarioIdd()+". "+
						CONSENTITO_PROCEDERE_MA;
			}else if(!idd.getFlagEsitoAdeguatezzaQuestionario().equalsIgnoreCase("true")){
				return  "Il prospetto di consulenza assicurativa investimenti assicurativi risulta non compatibile con il prodotto selezionato. "+
						CONSENTITO_PROCEDERE_MA;
			}
		}else if(tipoVerificaIdd == ProvideIddDataResponse.VERIFICA_IDD_RAMO_PREVIDENZA){
			if( !idd.getCodStatoQuestionarioIdd().equals(IddStatiQuestionario.VALIDO) && 
				!idd.getCodStatoQuestionarioIdd().equals(IddStatiQuestionario.PROVVISORIO) &&
				!idd.getCodStatoQuestionarioIdd().equals(IddStatiQuestionario.SOSPESO)){
				return  "Il prospetto di consulenza assicurativa previdenza risulta "+idd.getDescrStatoQuestionarioIdd()+". "+
						CONSENTITO_PROCEDERE_MA;
			}else if(!idd.getFlagEsitoAdeguatezzaQuestionario().equalsIgnoreCase("true")){
				return  "Il prospetto di consulenza assicurativa previdenza risulta non compatibile con il prodotto selezionato. "+
						CONSENTITO_PROCEDERE_MA;
			}
		}
		return null;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String verificaIddInMifid(PdfModel pdf){
		IddCallModel idd = pdf.getIddCallModel();
		if(idd == null)
			return null;
		
		int tipoVerificaIdd = readTipoVerificaIdd(pdf);
		if(tipoVerificaIdd == ProvideIddDataResponse.VERIFICA_IDD_RAMO_PROTEZIONE){
			if(!idd.getEsitoTecnico().isNull()) { // Valorizzato solo con i nuovi servizi IDD
				if(!idd.getEsitoValutazione().equalsIgnoreCase(OK))
					return RACCOMANDAZIONE_NON_VALIDA;				
			}else {
				if(!idd.getFlagEsitoAdeguatezzaRaccomandazione().equalsIgnoreCase("true")){
					if( idd.getCodStatoQuestionarioIdd().equals(IddStatiQuestionario.ANNULLATO) ||
						idd.getCodStatoQuestionarioIdd().equals(IddStatiQuestionario.INCOMPLETO)){
						return "Non è possibile procedere perchè il prospetto di consulenza assicurativa protezione non risulta valido.";				
					}else{					
						return "Non è possibile procedere perchè la raccomandazione è scaduta.<br>E' necessario generarla nuovamente da Protection Easy Tool.";
					}
				}
			}
		}else if(tipoVerificaIdd == ProvideIddDataResponse.VERIFICA_IDD_RAMO_III){
			if( !idd.getCodStatoQuestionarioIdd().equals(IddStatiQuestionario.VALIDO) && 
				!idd.getCodStatoQuestionarioIdd().equals(IddStatiQuestionario.PROVVISORIO) &&
				!idd.getCodStatoQuestionarioIdd().equals(IddStatiQuestionario.SOSPESO)){
				return  "Il prospetto di consulenza assicurativa investimenti assicurativi risulta "+idd.getDescrStatoQuestionarioIdd()+", "+
						NON_POSSIBILE_PROCEDERE_SUFFIX;
			}else if(!idd.getFlagEsitoAdeguatezzaQuestionario().equalsIgnoreCase("true")){
				return  "Il prospetto di consulenza assicurativa investimenti assicurativi risulta non compatibile con il prodotto selezionato, "+
						NON_POSSIBILE_PROCEDERE_SUFFIX;
			}
		}else if(tipoVerificaIdd == ProvideIddDataResponse.VERIFICA_IDD_RAMO_PREVIDENZA){
			if( !idd.getCodStatoQuestionarioIdd().equals(IddStatiQuestionario.VALIDO) && 
				!idd.getCodStatoQuestionarioIdd().equals(IddStatiQuestionario.PROVVISORIO) &&
				!idd.getCodStatoQuestionarioIdd().equals(IddStatiQuestionario.SOSPESO)){
				return  "Il prospetto di consulenza assicurativa previdenza risulta "+idd.getDescrStatoQuestionarioIdd()+", "+
						NON_POSSIBILE_PROCEDERE_SUFFIX;
			}else if(!idd.getFlagEsitoAdeguatezzaQuestionario().equalsIgnoreCase("true")){
				return  "Il prospetto di consulenza assicurativa previdenza risulta non compatibile con il prodotto selezionato, "+
						NON_POSSIBILE_PROCEDERE_SUFFIX;
			}
		}
		return null;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String verificaIddInAccettazioneCopernico(PdfModel pdf){
		IddCallModel idd = pdf.getIddCallModel();
		if(idd == null)
			return null;
		
		int tipoVerificaIdd = readTipoVerificaIdd(pdf);
		if(tipoVerificaIdd == ProvideIddDataResponse.VERIFICA_IDD_RAMO_PROTEZIONE){
			if(!idd.getEsitoTecnico().isNull()) { // Valorizzato solo con i nuovi servizi IDD
				if(!idd.getEsitoValutazione().equalsIgnoreCase(OK))
					return RACCOMANDAZIONE_NON_VALIDA;				
			}else {
				if(!idd.getFlagEsitoAdeguatezzaRaccomandazione().equalsIgnoreCase("true")){
					if( idd.getCodStatoQuestionarioIdd().equals(IddStatiQuestionario.ANNULLATO) ||
						idd.getCodStatoQuestionarioIdd().equals(IddStatiQuestionario.INCOMPLETO) ||
						idd.getCodStatoQuestionarioIdd().equals(IddStatiQuestionario.DA_RICOMPILARE)){
						return "Non è possibile procedere perché il prospetto di consulenza assicurativa protezione non risulta valido.";				
					}else{					
						return "Non è possibile procedere con la sottoscrizione di questo prodotto perché la raccomandazione personalizzata è scaduta";
					}
				}
			}
		}else if(tipoVerificaIdd == ProvideIddDataResponse.VERIFICA_IDD_RAMO_III){
				return  null;
		}else if(tipoVerificaIdd == ProvideIddDataResponse.VERIFICA_IDD_RAMO_PREVIDENZA){
			if( idd.getCodStatoQuestionarioIdd().equals(IddStatiQuestionario.ANNULLATO) ||
				idd.getCodStatoQuestionarioIdd().equals(IddStatiQuestionario.INCOMPLETO) ||
				idd.getCodStatoQuestionarioIdd().equals(IddStatiQuestionario.DA_RICOMPILARE)){
				return "Non è possibile procedere perché il prospetto di consulenza assicurativa previdenza non risulta valido.";				
			}
		}
		return null;
	}	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String callGeneraRaccomandazioneIdd(ClientSessionContext csc, PdfModel pdf){
		
		if( pdf.isTestMode() || 
			pdf.getIsSede().booleanValue())
			return null;

		// Se l'id della raccomandazione è stata passata in input non la devo mai generare
		if(pdf.getIdRaccomandazioneIdd().length() > 0)
			return null;
		
		// Se iddCallModel è null significa che il driver non è integrato con IDD
		IddCallModel callModel = pdf.getIddCallModel();
		if(callModel == null)
			return null;

		// Se nel primo pdf il cliente principale è stato svuotato dopo aver generato la raccomandazione ripuliamo il "callModel" 
		// per reinizializzare l'intero processo IDD (non dovrebbe mai accadere perchè nei pdf IDD il cliente è sempre obbligatorio)
		PdfPersonModel mainCli = pdf.mainPdfData().getClienti().get(0);
		if(mainCli == null || mainCli.isEmty()) {
			pdf.setIddCallModel(null);
			return null;
		}

		// Se nella sessione abbiamo già chiamato la generazione non la rigeneriamo 
		if(!callModel.getIdRaccomandazioneIdd().isNull())
			return null;
		
		try{
			
			callModel.setUtente(new StringType(csc.getUserCode()));
			callModel.initRuoloAgente(pdf.getPdfData().getCodRuoloImpersonato());
			DAOOSBResultModel wsRes = new DAOObject(csc, DAO_XML_IDD).executeOSBAccess("generaRaccomandazioneIdd", callModel);
			if(wsRes.getWsCallData().getStatus() == XmlServiceCallData.STATUS_SERVICE_DISABLED){
				return "Servizio di generazione raccomandazione IDD disabilitato";
			}else if(wsRes.getWsCallData().getStatus() != XmlServiceCallData.STATUS_OK){	
				return "Errore di comunicazione con il servizio di generazione raccomandazione IDD: "+wsRes.getWsCallData().getMessage();
			}else{
				if(callModel.getIdRaccomandazioneIdd().isNull())
					return "Il servizio di generazione raccomandazione IDD non ha restituito un ID questionario valido";
				return null; // Raccomandazione generata
			}
		}catch(DAOException daoe){
			return "Eccezione nel richiamo al servizio di generazione raccomandazione IDD: "+daoe.toString();
		}
	}	
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String callCollegaRaccomandazioneIddPassataInInput(ClientSessionContext csc, PdfModel pdf) throws Exception{
		
		if( pdf.isTestMode() || 
			pdf.getIsSede().booleanValue() ||
			pdf.getIdRaccomandazioneIdd().length() == 0 ||
			pdf.getPdfData().getPdfInstanceId().isNull())
			return null;

		ProvideIddDataResponse iddData = PdfDriverCaller.callProvideIddData(csc, pdf, false);
		if(iddData == null || iddData.getTipoVerfica() == ProvideIddDataResponse.VERIFICA_IDD_RAMO_III) // Nel ramo III la raccomandazione è associata all'RDA per cui non facciamo il collegamento
			return null;
		
		return callCollegaRaccomandazioneIddSrv(csc, pdf, pdf.getIdRaccomandazioneIdd());
	}	
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String callCollegaRaccomandazioneIddGenerata(ClientSessionContext csc, PdfModel pdf) throws Exception{
		
		if( pdf.isTestMode() || 
			pdf.getIsSede().booleanValue() ||
			pdf.getIddCallModel() == null ||
			pdf.getIddCallModel().getIdRaccomandazioneIdd().isNull() ||
			pdf.getPdfData().getPdfInstanceId().isNull())
			return null;

		ProvideIddDataResponse iddData = PdfDriverCaller.callProvideIddData(csc, pdf, false);
		if(iddData == null || iddData.getTipoVerfica() == ProvideIddDataResponse.VERIFICA_IDD_RAMO_III) // Nel ramo III la raccomandazione è associata all'RDA per cui non facciamo il collegamento
			return null;
		
		return callCollegaRaccomandazioneIddSrv(csc, pdf, pdf.getIddCallModel().getIdRaccomandazioneIdd().toString());
	}	
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static String callCollegaRaccomandazioneIddSrv(ClientSessionContext csc, PdfModel pdf, String idRaccomandazioneIdd) throws Exception{
		
		try{
			// Nei basket semplificati come quelli protezione, non dobbiamo mai collegare la raccomandazione alla dispositiva
			if(pdf.isInBasketSemplt())
				return null;
			
			IddCallModel callModel = new IddCallModel();
			callModel.setUtente(new StringType(csc.getUserCode()));
			callModel.setPdfInstanceId(pdf.getPdfData().getPdfInstanceId());
			callModel.setIdRaccomandazioneIdd(new StringType(idRaccomandazioneIdd));
			DAOOSBResultModel wsRes = new DAOObject(csc, DAO_XML_IDD).executeOSBAccess("collegaRaccomandazioneIdd", callModel);
			if(wsRes.getWsCallData().getStatus() == XmlServiceCallData.STATUS_SERVICE_DISABLED){
				return "Servizio di modifica raccomandazione IDD disabilitato";
			}else if(wsRes.getWsCallData().getStatus() != XmlServiceCallData.STATUS_OK){	
				return "Errore di comunicazione con il servizio di modifica raccomandazione IDD: "+wsRes.getWsCallData().getMessage();
			}else{
				if(callModel.getIdRaccomandazioneIddCtrl().isNull())
					return "Il servizio di modifica raccomandazione IDD non ha restituito alcun ID raccomandazione";
				if(!callModel.getIdRaccomandazioneIddCtrl().equals(callModel.getIdRaccomandazioneIdd()))
					return "Il servizio di modifica raccomandazione IDD non ha restituito un ID raccomandazione valido";
				return null; // Raccomandazione collegata
			}
		}catch(DAOException daoe){
			return "Eccezione nel richiamo al servizio di modifica raccomandazione IDD: "+daoe.toString();
		}
	}	

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String callRecuperaIdEcmRaccomandazioneIdd(ClientSessionContext csc, PdfModel pdf) throws Exception{
		
		try{
			
			IddCallModel callModel = new IddCallModel();
			callModel.setUtente(new StringType(csc.getUserCode()));
			if(pdf.getIdRaccomandazioneIdd().length() > 0)
				callModel.setIdRaccomandazioneIdd(new StringType(pdf.getIdRaccomandazioneIdd()));
			else if(pdf.getIddCallModel() != null)
				callModel.setIdRaccomandazioneIdd(pdf.getIddCallModel().getIdRaccomandazioneIdd());
			DAOOSBResultModel wsRes = new DAOObject(csc, DAO_XML_IDD).executeOSBAccess("recuperaIdEcmRaccomandazioneIdd", callModel);
			if(wsRes.getWsCallData().getStatus() == XmlServiceCallData.STATUS_SERVICE_DISABLED){
				throw new Exception("Servizio di recupero dell'ID Ecm della raccomandazione IDD disabilitato");
			}else if(wsRes.getWsCallData().getStatus() != XmlServiceCallData.STATUS_OK){	
				throw new Exception("Errore di comunicazione con il servizio di recupero dell'ID Ecm della raccomandazione IDD: "+wsRes.getWsCallData().getMessage());
			}else{
				if(callModel.getIdEcmRaccomandazioneIdd().isNull())
					throw new Exception("Il servizio di recupero dell'ID Ecm della raccomandazione IDD ["+pdf.getIdRaccomandazioneIdd()+"] non ha restituito alcun valore");
				return callModel.getIdEcmRaccomandazioneIdd().toString(); 
			}
		}catch(DAOException daoe){
			throw new Exception("Eccezione DAO nel richiamo al servizio di recupero dell'ID Ecm della raccomandazione IDD");
		}
	}	

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String formattaErroriAdeguatezzaIddPerMOM(String erroriAdeguatezzaIdd) {
		return erroriAdeguatezzaIdd;
	}
}
