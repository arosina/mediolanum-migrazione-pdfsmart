package prgm.pdfwebforms.mifid;

import java.util.ArrayList;
import java.util.Arrays;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOOSBResultModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.OSBCallData;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.basket.Basket;
import prgm.pdfwebforms.basket.BasketElement;
import prgm.pdfwebforms.core.PdfConfig;
import prgm.pdfwebforms.core.PdfPredefinedFields;
import prgm.pdfwebforms.drivers.PdfDriverCaller;
import prgm.pdfwebforms.drivers.io.mifid.CompartoMifidModel;
import prgm.pdfwebforms.drivers.io.mifid.OperazioneMifidModel;
import prgm.pdfwebforms.drivers.io.mifid.ProvideMifidDataResponse;
import prgm.pdfwebforms.idd.IddCaller;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.model.PdfPersonModel;
import prgm.pdfwebforms.sostituzioni.SostituzioniCaller;


/***********************************************************************************************/
/***********************************************************************************************/
public class MifidCaller {

	public static String WARNING_INDICATOR = "#WARNING#";

	private static final String EVENT_NAME_PREVIEW = "PREVIEW";
	private static final String EVENT_NAME_SEND = "SEND";
	private static final String EVENT_NAME_SIGN = "SIGN";
	private static final String EVENT_NAME_COPERNICO = "COPERNICO";
	private static final String EVENT_NAME_ACCETTAZIONE_COPERNICO = "ACCETTAZIONE_COPERNICO";
	private static final String EVENT_NAME_MOM = "MOM";
	
	private static final String COD_APP_CHIAMANTE_MOM = "MOM";
	private static final String COD_APP_CHIAMANTE_SEDE = "PDFSEDE";
	private static final String COD_APP_CHIAMANTE_PDFPREVIEW = "PDFPREVIEW";
	private static final String COD_APP_CHIAMANTE_PDFSEND = "PDFSEND";
	private static final String COD_APP_CHIAMANTE_PDFSIGN = "PDFSIGN";
	
	private static final String ZERO = "0";
	private static final String OK = "OK";
	private static final String WARNING = "WARNING";
	private static final String CHIOCCIOLA = "@";
	
	private static final String COD_TIPO_OPERAZIONE_SWITCH = "SWITCH";
	private static final String COD_TIPO_OPERAZIONE_CONVERSIONE = "CONV";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String[] callOnPreview(ClientSessionContext csc, PdfModel pdf) throws Exception{
		pdf.setIsMifidSkipped(new BooleanType(false));
		pdf.setAdeguatezzaOnPreview(true);
		String errOrWar = callMifid(csc, pdf, (csc.isSede()?COD_APP_CHIAMANTE_SEDE:COD_APP_CHIAMANTE_PDFPREVIEW), 
									null, pdf.isInBasket(), Basket.isLastDispoPdf(pdf), EVENT_NAME_PREVIEW);
		
		String iddErrMsg = IddCaller.callIdd(csc, pdf, true);
		if(iddErrMsg == null)
			iddErrMsg = IddCaller.verificaIddInMifid(pdf);
		
		String sostituzioniErrMsg = null;
		if((errOrWar == null || errOrWar.startsWith(WARNING_INDICATOR)) && iddErrMsg == null) // Sostituzioni: solo se mifid e idd ok
			sostituzioniErrMsg = SostituzioniCaller.callVerificaSostituzioni(csc, pdf, true);

		String saldoBasketMsg = null;
		if(pdf.isInBasket() && Basket.isLastDispoPdf(pdf)) {
			boolean callSaldo = (errOrWar == null || errOrWar.startsWith(WARNING_INDICATOR)) &&
								(iddErrMsg == null || iddErrMsg.startsWith(WARNING_INDICATOR)) &&
								(sostituzioniErrMsg == null || sostituzioniErrMsg.startsWith(WARNING_INDICATOR));
			if(callSaldo)
				saldoBasketMsg = Basket.verificaSaldi(csc, pdf);	
		}
		
		if(errOrWar == null && iddErrMsg == null && sostituzioniErrMsg == null && saldoBasketMsg == null)
			return null;
				
		return new String[]{errOrWar, iddErrMsg, sostituzioniErrMsg, saldoBasketMsg};
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String[] callOnSend(ClientSessionContext csc, PdfModel pdf) throws Exception{
		pdf.setAdeguatezzaOnPreview(false);
		String errOrWar = callMifid(csc, pdf, (csc.isSede()?COD_APP_CHIAMANTE_SEDE:COD_APP_CHIAMANTE_PDFSEND), 
									null, pdf.isInBasket(), Basket.isFirstDispoPdf(pdf), EVENT_NAME_SEND);
		if(errOrWar != null && errOrWar.startsWith(WARNING_INDICATOR))
			errOrWar = null;
		
		String iddErrMsg = IddCaller.callIdd(csc, pdf, true);
		if(iddErrMsg == null)
			iddErrMsg = IddCaller.verificaIddInMifid(pdf);
		
		String sostituzioniErrMsg = null;
		if((errOrWar == null || errOrWar.startsWith(WARNING_INDICATOR)) && iddErrMsg == null) // Sostituzioni: solo se mifid e idd ok
			sostituzioniErrMsg = SostituzioniCaller.callVerificaSostituzioni(csc, pdf, false);

		if(errOrWar == null && iddErrMsg == null && sostituzioniErrMsg == null)
			return null;

		return new String[]{errOrWar, iddErrMsg, sostituzioniErrMsg, null};
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String[] callOnSign(ClientSessionContext csc, PdfModel pdf) throws Exception{
		pdf.setAdeguatezzaOnPreview(false);
		String errOrWar = callMifid(csc, pdf, (csc.isSede()?COD_APP_CHIAMANTE_SEDE:COD_APP_CHIAMANTE_PDFSIGN), 
									null, pdf.isInBasket(), Basket.isFirstDispoPdf(pdf), EVENT_NAME_SIGN);
		if(!pdf.isOperatoreMOM()){ // All'operatore MOM visualizziamo anche i warning, all'FB no, li togliamo perchè già superato nello step "preview"
			if(errOrWar != null && errOrWar.startsWith(WARNING_INDICATOR))
				errOrWar = null;
		}

		String iddErrMsg = IddCaller.callIdd(csc, pdf, true);
		if(iddErrMsg == null)
			iddErrMsg = IddCaller.verificaIddInMifid(pdf);
		
		String sostituzioniErrMsg = null;
		if((errOrWar == null || errOrWar.startsWith(WARNING_INDICATOR)) && iddErrMsg == null) // Sostituzioni: solo se mifid e idd ok
			sostituzioniErrMsg = SostituzioniCaller.callVerificaSostituzioni(csc, pdf, false);

		if(errOrWar == null && iddErrMsg == null && sostituzioniErrMsg == null)
			return null;
		
		return new String[]{errOrWar, iddErrMsg, sostituzioniErrMsg, null};
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String[] callOnCopernico(ClientSessionContext csc, PdfModel pdf, ProvideMifidDataResponse mifidData) throws Exception{
		pdf.setAdeguatezzaOnPreview(false);
		String errOrWar = callMifid(csc, pdf, COD_APP_CHIAMANTE_PDFSIGN, mifidData, pdf.isInBasket(), Basket.isFirstDispoPdf(pdf), EVENT_NAME_COPERNICO);
		if(errOrWar != null && errOrWar.startsWith(WARNING_INDICATOR))
			errOrWar = null;
		
		String iddErrMsg = IddCaller.callIdd(csc, pdf, true);
		if(iddErrMsg == null)
			iddErrMsg = IddCaller.verificaIddInMifid(pdf);
		
		String sostituzioniErrMsg = null;
		if((errOrWar == null || errOrWar.startsWith(WARNING_INDICATOR)) && iddErrMsg == null) // Sostituzioni: solo se mifid e idd ok
			sostituzioniErrMsg = SostituzioniCaller.callVerificaSostituzioni(csc, pdf, false);

		if(errOrWar == null && iddErrMsg == null && sostituzioniErrMsg == null)
			return null;
		
		return new String[]{errOrWar, iddErrMsg, sostituzioniErrMsg, null};
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String[] callOnAccettazioneCopernico(ClientSessionContext csc, PdfModel pdf, ProvideMifidDataResponse mifidData) throws Exception{		
		pdf.setAdeguatezzaOnPreview(false);
		String errOrWar = callMifid(csc, pdf, COD_APP_CHIAMANTE_PDFSIGN, mifidData, false, true, EVENT_NAME_ACCETTAZIONE_COPERNICO);		
		
		String iddErrMsg = IddCaller.callIdd(csc, pdf, true);
		if(iddErrMsg == null)
			iddErrMsg = IddCaller.verificaIddInAccettazioneCopernico(pdf);
		
		if(errOrWar == null && iddErrMsg == null)
			return null;
		
		return new String[]{errOrWar, iddErrMsg, null, null};
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String[] callOnAccettazioneBasketCopernico(ClientSessionContext csc, PdfModel pdf) throws Exception{		
		pdf.setAdeguatezzaOnPreview(false);
		String errOrWar = callMifid(csc, pdf, COD_APP_CHIAMANTE_PDFSIGN, null, true, true, EVENT_NAME_ACCETTAZIONE_COPERNICO);		
		
		String iddErrMsg = null;
		for(BasketElement be : pdf.getBasket().getBasketElements()) {
			iddErrMsg = IddCaller.callIdd(csc, be.getDispoPdf(), true);
			if(iddErrMsg == null)
				iddErrMsg = IddCaller.verificaIddInAccettazioneCopernico(be.getDispoPdf());
			if(iddErrMsg != null)
				break;
		}

		String saldoBasketMsg = null;
		boolean callSaldo = (errOrWar == null || errOrWar.startsWith(WARNING_INDICATOR)) &&
							(iddErrMsg == null || iddErrMsg.startsWith(WARNING_INDICATOR));
		if(callSaldo)
			saldoBasketMsg = Basket.verificaSaldi(csc, pdf);	
		
		if(errOrWar == null && iddErrMsg == null && saldoBasketMsg == null)
			return null;
		
		return new String[]{errOrWar, iddErrMsg, null, saldoBasketMsg};
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String callOnMom(ClientSessionContext csc, PdfModel pdf) throws Exception{
		pdf.setAdeguatezzaOnPreview(false);
		String errOrWar = callMifid(csc, pdf, COD_APP_CHIAMANTE_MOM, null, false, true, EVENT_NAME_MOM);
		if(errOrWar == null) {
			if(pdf.getMifidCallModel() == null) 	// se tutto ok ma il modello non è impostato significa che le regole del driver non prevedono la mifid
				return "MIFID-NOT-CALLED";			// e a MOM daremo OK con un messaggio (vedi PdfWebFormsWS)
			return null;
		}
		
		return errOrWar;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static String callMifid(ClientSessionContext csc, PdfModel pdf, String applicazioneChiamante, ProvideMifidDataResponse mifidData,
									boolean inBasket, boolean isCurrentPdfInBasket, String eventName) throws Exception{
		
		if(inBasket && !isCurrentPdfInBasket)
			return null;

		pdf.setMifidCallModel(null);
		
		MifidCallModel callModel = new MifidCallModel();
		
		// Imposto i vari flag per manlevaMifid  e l'RDA dinamico (rfc #260880)
		impostaMifidManlevaData(pdf, callModel, inBasket, eventName);
		
		callModel.setApplicazioneChiamante(new StringType(applicazioneChiamante));
		if(inBasket)
			callModel.setCodDispoSorgente(new StringType(pdf.getPdfData().getIdCarrello().toString()));
		else
			callModel.setCodDispoSorgente(pdf.getPdfData().getPdfInstanceId());
		if (applicazioneChiamante.equals(COD_APP_CHIAMANTE_MOM)) {
			//La verifica MiFID da MOM non traccia nella tabella DETT_CHIAM_MOTR_NRMTVO
			callModel.setCodDispoSorgente(new StringType());
		}

		if(!inBasket) {
			if(mifidData == null)
				mifidData = PdfDriverCaller.callProvideMifidData(csc, pdf);
			if(mifidData == null)
				return null;

			ListType acquisti = mifidData.getAcquisti();
			ListType disinvestimenti = mifidData.getDisinvestimenti();
			if(acquisti.size() == 0 && disinvestimenti.size() > 0)
				return null;	//In caso di disinvestimento puro non chiamo la MiFID

			callModel.setInputBasket(new InputBasketMifidModel());
			callModel.getInputBasket().setFlagAdeguatezza(mifidData.getFlagAdeguatezza());	//Viene passato il flagAdeguatezza della singola dispositiva
			initDispoCallModel(csc, pdf, callModel, mifidData);
		}else {
			if(!initBasketCallModel(csc, callModel, pdf)) // Se nessun pdf ha mifid non chiamo
				return null;
		}
		
		if(pdf.isTestMode())
			return null;
		
		try{

			if(inBasket)
				impostaFlagCheckRiprofilatura(csc, pdf, callModel); // rfc #207357: Carrello GoaldBased

			if(!pdf.isAdeguatezzaOnPreview())
				pdf.setDataUltimaChiamataAdeguatezza(Tools.today().toString());
			
			// RFC #292576: PCA e Questionario Light
			// Se da 5D sono passati in input sono su tutti in mainPdfData del basket
			// Se da catalogo sono in mainPdfData, il PCA nella chiamata IDD, il quesitonario light impostato dal driver o dai controlli MOM
			callModel.setIdPCA(pdf.mainPdfData().getIdPCA());
			callModel.setIdQLTM(pdf.mainPdfData().getIdQLTM());
			
			DAOOSBResultModel wsRes = new DAOObject(csc, "PdfWebForms.PdfWebFormsMifid").executeOSBAccess("callMiFidBasket", callModel);
			if(wsRes.getWsCallData().getStatus() == OSBCallData.STATUS_SERVICE_DISABLED){
				return "Servizio di verifica MIFID disabilitato";
			}else if(wsRes.getWsCallData().getStatus() != OSBCallData.STATUS_OK){	
				return "Errore di comunicazione con il servizio MIFID: "+wsRes.getWsCallData().getMessage();
			}else if(callModel.getErr().isNull()){
				return "Errore nei dati ricevuti dal servizio MIFID";
			}else{
				impostaIdAdeguatezzaManleva(pdf, callModel, inBasket, eventName);
				setMifidCallModelForAllPdf(pdf, callModel);
				String concentrazioneFiaResMsg = ControlliConcentrazioneFia.eseguiControlli(csc, callModel);
				if(concentrazioneFiaResMsg != null) {
					callModel.setErr(new IntegerType(ZERO));
					return concentrazioneFiaResMsg;
				}
				return componiBasketMifidMsg(callModel, inBasket, eventName);
			}
				
				
		}catch(DAOException daoe){
			return "Eccezione nel richiamo al servizio MIFID: "+daoe.toString();
		}
	}	

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void impostaMifidManlevaData(PdfModel pdf, MifidCallModel callModel, boolean inBasket, String eventName) throws Exception{
		
		MifidManlevaDataModel mifidManlevaData = new MifidManlevaDataModel();
		if(eventName.equals(EVENT_NAME_PREVIEW)) {
			if(inBasket) { // Uso i parametri passati in input
				mifidManlevaData.setIdAdeguatezzaPadre(pdf.getPdfData().getIdAdeguatezzaMifidPadre());
				mifidManlevaData.setFlagManlevaKOESG(pdf.getPdfData().getFlagManlevaMifidKOESG());
				callModel.setFlagRDA(new StringType("N"));
			}else { // Valorizzerò il resto, IdAdeguatezzaPadre e FlagManlevaKOESG, a valle della chiamata mifid in preview, in "impostaIdAdeguatezzaPadrePreview"
				callModel.setFlagRDA(new StringType("D"));
			}
			callModel.setMifidManlevaData(mifidManlevaData);
			pdf.setMifidManlevaData((MifidManlevaDataModel)Tools.cloneObject(mifidManlevaData));
			return;
		}
		
		if(pdf.getMifidManlevaData() != null)
			mifidManlevaData = pdf.getMifidManlevaData();
		callModel.setMifidManlevaData(mifidManlevaData);
		
		callModel.setFlagRDA(new StringType("N"));
		if(!inBasket && (eventName.equals(EVENT_NAME_SEND) || eventName.equals(EVENT_NAME_SIGN) || eventName.equals(EVENT_NAME_COPERNICO)))
			callModel.setFlagRDA(new StringType("S"));		
	}

	/***********************************************************************************************/
	private static final String ESITO_MANLEVA_KOESG = "19";
	/***********************************************************************************************/
	private static void impostaIdAdeguatezzaManleva(PdfModel pdf, MifidCallModel callModel, boolean inBasket, String eventName) throws Exception{
		// Impostiamo i dati di manleva solo se non siamo in un basket, siamo in preview, ossia la prima chiamata, e lo stato mifid è KOESG
		if(!inBasket && eventName.equals(EVENT_NAME_PREVIEW) && callModel.getStato().equals(ESITO_MANLEVA_KOESG)) {
			MifidManlevaDataModel mifidManlevaData = callModel.getMifidManlevaData();
			mifidManlevaData.setIdAdeguatezzaPadre(callModel.getIdEsito());
			mifidManlevaData.setFlagManlevaKOESG(new StringType("S"));
			pdf.setMifidManlevaData((MifidManlevaDataModel)Tools.cloneObject(mifidManlevaData));
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void impostaFlagCheckRiprofilatura(ClientSessionContext csc, PdfModel pdf, MifidCallModel callModel) throws DAOException, Exception {
		StringType tipiPropostaGB = PdfConfig.getParamAsString(csc, "MIFID", "TIPI_PROPOSTA_GOAL_BASED");
		if(tipiPropostaGB.isNull())
			tipiPropostaGB = new StringType("MAPC,MAPW");			
		ArrayList<String> tipiPropostaGBArray = new ArrayList<>(Arrays.asList(tipiPropostaGB.toString().split(",")));
		
		StringType tipoCarrello = (StringType)DAOObject.executeDynaQueryAccess(csc, "DBAZ_SOGG", 
											"select cod_tipo_prop from sogg_infosimu.anag_carl where cod_carl="+pdf.getPdfData().getIdCarrello(), 
											null, StringType.class).getSingleResult();
		if(tipoCarrello != null && tipiPropostaGBArray.contains(tipoCarrello.toString())) // rfc #207357: Carrello GoaldBased
			callModel.setFlagCheckRiprofilatura(new StringType("N"));
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static boolean initBasketCallModel(ClientSessionContext csc, MifidCallModel callModel, PdfModel currentPdf) throws Exception{
		
		// RFC 241617: nei basket non chiamiamo la mifid se sono tutti rimborsi altrimenti si
		// Creo quindi un elenco di risposte alla provide con tanti elementi quante sono le dispositive nel basket
		// Se esiste almeno un elenco di acquisti > 0 allora la chiamata la facciamo, altrimenti no
		boolean doCall = false;
		ArrayList<ProvideMifidDataResponse> provideMifidDataResponses = new ArrayList<ProvideMifidDataResponse>();
		Basket basket = currentPdf.getBasket();
		for(BasketElement be : basket.getBasketElements()) {
			ProvideMifidDataResponse mifidData = PdfDriverCaller.callProvideMifidData(csc, be.getDispoPdf());
			provideMifidDataResponses.add(mifidData);
			if(mifidData != null && mifidData.getAcquisti().size() > 0)
				doCall = true;
		}
		if(!doCall)
			return false;
		
		callModel.setInputBasket(new InputBasketMifidModel());
		callModel.getInputBasket().setFlagAdeguatezza(new StringType("N"));
		for(int i=0;i<basket.getBasketElements().size();i++) {
			ProvideMifidDataResponse mifidData = provideMifidDataResponses.get(i);
			if(mifidData == null)
				continue;

			if(mifidData.getFlagAdeguatezza().equals("S"))
				callModel.getInputBasket().setFlagAdeguatezza(new StringType("S"));	//Se è presente una dispositiva con "S" prevale
			initDispoCallModel(csc, basket.getBasketElements().get(i).getDispoPdf(), callModel, mifidData);
		}
		return true;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void initDispoCallModel(ClientSessionContext csc, PdfModel dispoPdf, MifidCallModel callModel, ProvideMifidDataResponse mifidData) throws Exception{
		
		callModel.setUserId(new StringType(csc.getUserCode()));
		
		ListType clienti = mifidData.getClienti();
		ListType acquisti = mifidData.getAcquisti();
		ListType disinvestimenti = mifidData.getDisinvestimenti();

		PdfDataModel mainPdfData = dispoPdf.mainPdfData();
		PdfDataModel pdfData = null;
		boolean inSwitch = dispoPdf.getPdfData().getIsSwitch().booleanValue();
		if (inSwitch) {
			pdfData = (PdfDataModel)dispoPdf.getPdfData().getPdfs().get(1);
		} else {
			pdfData = mainPdfData;
		}
		
		boolean addOnlyFirstCliToDisinvestimenti = false;
		if(acquisti.size() > 0 && disinvestimenti.size() > 0)
			addOnlyFirstCliToDisinvestimenti = true;
		
		//Gestione centralizzata di switch e conversioni
		String codTipoOperazione = "";
		if (inSwitch) {
			codTipoOperazione = COD_TIPO_OPERAZIONE_SWITCH;
		} else {
			//Verifico se il driver compare nell'elenco dei driver di tipo conversione
			String driverName = dispoPdf.mainPdfAnag().getPdfDriverName().toString();
			StringType paramValue = PdfConfig.getParamAsString(csc, "MIFID", "DRIVER_NAME_CONV", false);
			String[] arrDriverNameConv = paramValue.toString().split(",");
			for (int i=0; i<arrDriverNameConv.length; i++) {
				String driverNameConv = arrDriverNameConv[i];
				if (driverNameConv.equals(driverName)) {
					codTipoOperazione = COD_TIPO_OPERAZIONE_CONVERSIONE;
					break;
				}
			}
		}

		StringType percentualeAgevolazione = new StringType();
		if (pdfData.readProperty(PdfPredefinedFields.PERCENTUALE_AGEVOLAZIONE) != null) {
			AbstractType abstractPercentualeAgevolazione = (AbstractType)pdfData.readProperty(PdfPredefinedFields.PERCENTUALE_AGEVOLAZIONE);
			//Con la rfc 185853 siamo andati in produzione settando erroneamente percentualeAgevolazione come DoubleType anzichè StringType
			//E' stato corretto nel metodo drawHiddenFields della classe PdfHtmlFieldsDrawer
			//Viene gestito anche qui perchè nella chiamata MiFID da MOM e in accettazione copernico non si passa dal FE
			if (abstractPercentualeAgevolazione instanceof DoubleType) {
				percentualeAgevolazione = new StringType(abstractPercentualeAgevolazione.toString());
			} else {
				percentualeAgevolazione = (StringType)abstractPercentualeAgevolazione;
			}
		}

		for(int i=0;i<acquisti.size();i++) {
			OperazioneMifidModel odriv = (OperazioneMifidModel)acquisti.get(i);
			OperazioneBasketMifidModel obasket = new OperazioneBasketMifidModel();
			Tools.copyCommandDataModel(odriv, obasket);
			
			//Dobbiamo risettare i comparti in quanto la copyCommandDataModel sfrutta il toString del DoubleType che limita a 3 decimali
			//Il tag durataPianoPAC necessita di 5 decimali
			obasket.setComparti(odriv.getComparti());
			
			//I driver che non vogliono passare il numero contratto anche se presente nell'acroform passano null
			if (odriv.getNumeroContratto() != null && odriv.getNumeroContratto().isNull()) {
				//Il driver non ha specificato il dato nel provideMifidData
				StringType numeroContratto = (StringType)pdfData.readProperty(PdfPredefinedFields.NUMERO_CONTRATTO);
				if (numeroContratto != null) {
					//Esiste nel pdfData
					obasket.setNumeroContratto(numeroContratto);
				}
			}
			
			if (odriv.getCodTipoOperazione() == null) {
				obasket.setCodTipoOperazione(new StringType(codTipoOperazione));
			} else {
				codTipoOperazione = odriv.getCodTipoOperazione().toString();
			}
			if (codTipoOperazione.equals(COD_TIPO_OPERAZIONE_SWITCH) || codTipoOperazione.equals(COD_TIPO_OPERAZIONE_CONVERSIONE))
				obasket.setFlagSwitch(new StringType("S"));
			else
				obasket.setFlagSwitch(new StringType("N"));
			
			for (int j=0; j<obasket.getComparti().size(); j++) {
				CompartoMifidModel comp = (CompartoMifidModel)obasket.getComparti().get(j);
				if (comp.getDerogaCommissionale().isNull())
					comp.setDerogaCommissionale(new DoubleType(percentualeAgevolazione.toString()));
			}
			
			obasket.setContoDiAddebito(new StringType(mifidData.getContoDiAddebito().toString()));
			obasket.setClienti(clienti);
			callModel.getInputBasket().getAcquisti().add(obasket);
		}
		
		for(int i=0;i<disinvestimenti.size();i++) {
			OperazioneMifidModel odriv = (OperazioneMifidModel)disinvestimenti.get(i);
			OperazioneBasketMifidModel obasket = new OperazioneBasketMifidModel();
			Tools.copyCommandDataModel(odriv, obasket);
			
			//I driver che non vogliono passare il numero contratto anche se presente nell'acroform passano null
			if (odriv.getNumeroContratto() != null && odriv.getNumeroContratto().isNull()) {
				//Il driver non ha specificato il dato nel provideMifidData
				StringType numeroContratto = (StringType)mainPdfData.readProperty(PdfPredefinedFields.NUMERO_CONTRATTO);
				if (numeroContratto != null) {
					//Esiste nel pdfData
					obasket.setNumeroContratto(numeroContratto);
				}
			}

			if (odriv.getCodTipoOperazione() == null) {
				obasket.setCodTipoOperazione(new StringType(codTipoOperazione));
			} else {
				codTipoOperazione = odriv.getCodTipoOperazione().toString();
			}
			if (codTipoOperazione.equals(COD_TIPO_OPERAZIONE_SWITCH) || codTipoOperazione.equals(COD_TIPO_OPERAZIONE_CONVERSIONE))
				obasket.setFlagSwitch(new StringType("S"));
			else
				obasket.setFlagSwitch(new StringType("N"));
			
			for (int j=0; j<obasket.getComparti().size(); j++) {
				CompartoMifidModel comp = (CompartoMifidModel)obasket.getComparti().get(j);
				if (comp.getDerogaCommissionale().isNull())
					comp.setDerogaCommissionale(new DoubleType(percentualeAgevolazione.toString()));
			}
						
			if(addOnlyFirstCliToDisinvestimenti) {
				if(clienti.size() > 0)
					obasket.getClienti().add(clienti.get(0));
			}else {
				obasket.setClienti(clienti);
			}
			callModel.getInputBasket().getDisinvestimenti().add(obasket);
		}
		
		ControlliConcentrazioneFia.addElementiDispo(mifidData, callModel);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void setMifidCallModelForAllPdf(PdfModel pdf, MifidCallModel callModel) {
		if(!pdf.isInBasket()) {
			pdf.setMifidCallModel(callModel);
		}else {
			Basket basket = pdf.getBasket();
			for(BasketElement be : basket.getBasketElements()) {
				be.getDispoPdf().setMifidCallModel(callModel);
				be.getDispoPdf().setMifidManlevaData(pdf.getMifidManlevaData());
			}
		}
	}

	/***********************************************************************************************/
	private static final String HTMLHR = "<hr>";
	private static final String KOESG_SUFFIX =  "<br><br>Con esclusivo riferimento alla presente proposta, il cliente esprime la volontà di adattare le proprie preferenze di sostenibilità. "+
												"La proposta risulterà coerente con le nuove preferenze temporanee, ma non allineata a quelle espresse nel questionario di profilatura. "+
												"In caso di successive modifiche della proposta sarà necessario riverificare l'adeguatezza e, se previsto, richiedere nuovamente l'adattamento delle preferenze.<br><br>";
	/***********************************************************************************************/
	private static String componiBasketMifidMsg(MifidCallModel callModel, boolean inBasket, String eventName) {
		
		if(!callModel.getErr().toString().equals(ZERO)) {
			if (!callModel.getDescrizioneEsito().isNull())
				return callModel.getDescrizioneEsito().toString();	//Il NMDA torna la descrizione nel tag <descrizioneEsito>
			else if (!callModel.getDescrizioneErrore().isNull())
				return callModel.getDescrizioneErrore().toString();	//Il vecchio motore torna la descrizione nel tag <descrErr>
			else
				return "Errore generico nella chiamata al servizio MIFID";
		}
		
		// Costruzione mesaggio MiFid
		StringBuilder messaggioMifid = new StringBuilder();
		for(int i=0;i<callModel.getEsitiBasket().size();i++) {
			EsitoBasketMifidModel eb = (EsitoBasketMifidModel)callModel.getEsitiBasket().get(i);
			String nominativoCliente = searchNominativoClienteMifid(eb, callModel);	
			String nominativoClienteHtml = "<b>"+nominativoCliente+"</b><br>";			
			if(!eb.getEsito().equals(OK) && !eb.getEsito().equals(WARNING)){
				messaggioMifid.append(nominativoClienteHtml+eb.getDescrizioneEsito().toString()+CHIOCCIOLA);
			}else if (eb.getProfili().size()>0) {
				//Cerco i KO nei cicli secondari
				ProfiloClienteMifidModel profilo = (ProfiloClienteMifidModel)eb.getProfili().get(0);
				StringBuilder messaggioMifidSec = new StringBuilder();
				for(int j=0;j<profilo.getEsitiSecondari().size();j++) {
					EsitoBasketMifidModel ebs = (EsitoBasketMifidModel)profilo.getEsitiSecondari().get(j);
					if(!ebs.getEsito().equals(OK) && !ebs.getEsito().equals(WARNING)) {
						messaggioMifidSec.append(ebs.getDescrizioneEsito().toString()+HTMLHR);
					}
				}				
				if(messaggioMifidSec.length() > 0)
					messaggioMifid.append(nominativoClienteHtml+messaggioMifidSec.substring(0, messaggioMifidSec.length()-HTMLHR.length())+CHIOCCIOLA);
			}
		}		
		if(messaggioMifid.length() > 0)
			messaggioMifid.deleteCharAt(messaggioMifid.length()-1);
		
		// Costruzione clienti con profilo provvisorio
		ArrayList<String> clientiConProfiloProvvisorio = new ArrayList<String>();
		for(int i=0;i<callModel.getEsitiBasket().size();i++) {
			EsitoBasketMifidModel eb = (EsitoBasketMifidModel)callModel.getEsitiBasket().get(i);
			if(eb.getProfili().size()>0) {
				//Vedo se il profilo è provvisorio
				ProfiloClienteMifidModel profilo = (ProfiloClienteMifidModel)eb.getProfili().get(0);
				if(profilo.getFlagProfiloValido().equals("S") && profilo.getStatoProfilo().equals("P")) {
					String nominativoCliente = searchNominativoClienteMifid(eb, callModel);	
					String clienteConProfiloProvvisorio = nominativoCliente.indexOf("-")>=0?nominativoCliente.substring(nominativoCliente.indexOf("-")+1) : nominativoCliente; //  rfc 260880: Vogliono vedere solo il nominativo e non l'ndg(!?)
					clienteConProfiloProvvisorio = clienteConProfiloProvvisorio.toUpperCase();	
					if(!clientiConProfiloProvvisorio.contains(clienteConProfiloProvvisorio))
						clientiConProfiloProvvisorio.add(clienteConProfiloProvvisorio);
				}
			}
		}

		// Warning. Li gestiamo solo se siamo in preview e non siamo in un basket
		String warnEsg = "";
		String warnProfilo = "";
		if(!inBasket && eventName.equals(EVENT_NAME_PREVIEW)) {
			
			if(callModel.getStato().equals(ESITO_MANLEVA_KOESG))
				warnEsg = messaggioMifid.toString();

			if(!clientiConProfiloProvvisorio.isEmpty()) {
				callModel.setSoloFirmaOlografa(true);
				if(clientiConProfiloProvvisorio.size() > 1) {
					String clientiConProfiloProvisorioAsString = clientiConProfiloProvisorioAsString(clientiConProfiloProvvisorio);
					warnProfilo = "Non è possibile procedere con Firma Digitale/Copernico per i clienti "+clientiConProfiloProvisorioAsString+" in quanto il PCP relativo a questa proposta risulta in stato provvisorio.";
				}else {
					warnProfilo = "Non è possibile procedere con Firma Digitale/Copernico per il cliente "+clientiConProfiloProvvisorio.get(0)+" in quanto il PCP relativo a questa proposta risulta in stato provvisorio.";
				}
			}
		}

		// KO per ESG -> Messagio sempre non bloccante. Lasciamo solo il warning eventualmente impostato in preview
		if(callModel.getStato().equals(ESITO_MANLEVA_KOESG))
			messaggioMifid = new StringBuilder();
		
		if(messaggioMifid.length() > 0) // MiFid KO. Bloccante
			return messaggioMifid.toString();
		else if(!warnEsg.isEmpty())		// MiFid KO per ESG ed eventuale profilo provvisorio. Non bloccante
			return WARNING_INDICATOR+warnEsg+KOESG_SUFFIX+warnProfilo;
		else if(!warnProfilo.isEmpty())	// MiFid OK con profilo provvisorio. Non bloccante
			return WARNING_INDICATOR+warnProfilo;
		return null;					// MiFid OK
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static String clientiConProfiloProvisorioAsString(ArrayList<String> clientiConProfiloProvvisorio) {
		StringBuilder res = new StringBuilder();
		for(int i=0;i<clientiConProfiloProvvisorio.size()-2;i++)
			res.append(clientiConProfiloProvvisorio.get(i)+", ");
		res.append(clientiConProfiloProvvisorio.get(clientiConProfiloProvvisorio.size()-2)+" e "+clientiConProfiloProvvisorio.get(clientiConProfiloProvvisorio.size()-1));
		return res.toString();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	static String searchNominativoClienteMifid(EsitoBasketMifidModel eb, MifidCallModel callModel) {
		try {
			for(int i=0;i<callModel.getInputBasket().getAcquisti().size();i++) {
				OperazioneBasketMifidModel o = (OperazioneBasketMifidModel)callModel.getInputBasket().getAcquisti().get(i);
				for(int j=0;j<o.getClienti().size();j++) {
					PdfPersonModel p = (PdfPersonModel)o.getClienti().get(j);
					if(!eb.getNdg().isNull() && p.getNdg().equals(eb.getNdg()))
						return eb.getNdg()+" - "+p.readNomeCognome();
					if(!eb.getCodPotenziale().isNull() && p.getIdCensimento().equals(eb.getCodPotenziale()))
						return eb.getCodPotenziale()+" - "+p.readNomeCognome();
				}
			}
			for(int i=0;i<callModel.getInputBasket().getDisinvestimenti().size();i++) {
				OperazioneBasketMifidModel o = (OperazioneBasketMifidModel)callModel.getInputBasket().getAcquisti().get(i);
				for(int j=0;j<o.getClienti().size();j++) {
					PdfPersonModel p = (PdfPersonModel)o.getClienti().get(j);
					if(!eb.getNdg().isNull() && p.getNdg().equals(eb.getNdg()))
						return eb.getNdg()+" - "+p.readNomeCognome();
					if(!eb.getCodPotenziale().isNull() && p.getIdCensimento().equals(eb.getCodPotenziale()))
						return eb.getCodPotenziale()+" - "+p.readNomeCognome();
				}
			}
		}catch(Exception e){
			// Do nothing and return only ndg/potzle
		}
		return eb.getNdg().isNull() ? eb.getCodPotenziale().toString() : eb.getNdg().toString();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static ArrayList<String> formattaErroriAdeguatezzaMifid(String erroriAdeguatezzaMifid) {
		ArrayList<String> elencoMsgAdgMifid = new ArrayList<String>();
		if(erroriAdeguatezzaMifid == null)
			return elencoMsgAdgMifid;
		
		if(erroriAdeguatezzaMifid.startsWith(MifidCaller.WARNING_INDICATOR))
			erroriAdeguatezzaMifid = erroriAdeguatezzaMifid.substring(MifidCaller.WARNING_INDICATOR.length());
		
		String[] clienti = erroriAdeguatezzaMifid.split(CHIOCCIOLA);
		for(int cliIdx=0; cliIdx<clienti.length; cliIdx++) {
			StringBuilder sb = new StringBuilder();
			String cliente = clienti[cliIdx];
			String[] gruppi = cliente.split("#");
			for(int gruppoIdx=0; gruppoIdx<gruppi.length; gruppoIdx++) {
				String gruppo = gruppi[gruppoIdx];
				String[] righe = gruppo.split("\\|");
				if (righe.length == 1) {
					String riga = righe[0];
					sb.append(riga+"<br>");
				} else {
					for (int k=0; k<righe.length; k++) {
						String riga = righe[k];
						if (k==0)
							sb.append(riga+"<br>");
						else
							sb.append("&nbsp;&nbsp;&nbsp;&nbsp;"+riga+"<br>");
					}
				}
			}
			elencoMsgAdgMifid.add(sb.toString());
		}

		return elencoMsgAdgMifid;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String formattaErroriAdeguatezzaMifidPerMOM(String erroriAdeguatezzaMifid) {
		return erroriAdeguatezzaMifid.replaceAll("\\n","").replaceAll("\\r","");
	}

}
