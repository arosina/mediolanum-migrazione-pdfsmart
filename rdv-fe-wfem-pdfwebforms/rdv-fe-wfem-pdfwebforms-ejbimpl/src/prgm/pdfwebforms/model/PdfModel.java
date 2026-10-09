package prgm.pdfwebforms.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.AbstractTypePropertyDescriptor;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;
import com.atosorigin.wfem.util.Tools;
import com.itextpdf.text.pdf.AcroFields;

import prgm.pdfwebforms.aml.model.AmlModel;
import prgm.pdfwebforms.aml.model.CoraModel;
import prgm.pdfwebforms.basket.Basket;
import prgm.pdfwebforms.basket.BasketElement;
import prgm.pdfwebforms.core.PdfConfig;
import prgm.pdfwebforms.core.PdfContextIntf;
import prgm.pdfwebforms.core.PdfFieldInfos;
import prgm.pdfwebforms.core.PdfPredefinedFields;
import prgm.pdfwebforms.idd.FlagControlloTargetMarketBackupBean;
import prgm.pdfwebforms.idd.IddCallModel;
import prgm.pdfwebforms.idd.IddStatiQuestionario;
import prgm.pdfwebforms.mifid.MifidCallModel;
import prgm.pdfwebforms.mifid.MifidManlevaDataModel;
import prgm.pdfwebforms.mom.CostantiMOM;
import prgm.pdfwebforms.mom.MomEventDataModel;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;
import prgm.pdfwebforms.reportadeguatezza.LegameDispositivaOrdineModel;
import prgm.pdfwebforms.reportadeguatezza.OrdineModel;
import prgm.pdfwebforms.reportadeguatezza.RecuperaReportAdeguatezzaCallModel;
import prgm.pdfwebforms.reportadeguatezza.ReportAdeguatezzaCallModel;
import prgm.pdfwebforms.reportadeguatezza.ReportAdeguatezzaCaller;
import prgm.pdfwebforms.sostituzioni.PreferenzeClienteSostituzioniModel;
import prgm.pdfwebforms.sostituzioni.SostituzioniCallModel;
import prgm.pdfwebforms.validation.PdfValidationEventDataModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfModel extends PdfContextIntf{

	public static double DEFAULT_SCALE_FACTOR = 1.55;
	public static int DEFAULT_FONT_SIZE = 12;

	private IntegerType modelHashCode = new IntegerType();
	private IntegerType dataHashCode = new IntegerType();
	
	private Class		firstDisplayClass = null;
	private boolean 	testMode = false;
	private boolean 	pdfOnWork = false;
	private boolean 	firstDriverNotFound = false;
	private boolean 	someDriverNotFount = false;
	private String 		initialErrorMsg = null;
	private String 		endErrorTraceMsg = null;
	private String 		pdfTitle = null;
	private int			errorOnSomePerson = PdfPersonModel.NO_ERROR;
	private boolean		errorOnSign = false;
	private boolean		wayoutEnabled = false;
	private BooleanType flagWayout = new BooleanType();
	private String 		nacUrlDomain = "";
	private String[]	erroriAdeguatezza = null;
	private boolean		adeguatezzaOnPreview = true;
	private Integer		maxRecuperaReportAdeguatezzaRetryCount = null;
	private Boolean 	putOnSignedProcessBatchQueue = null;
	private Boolean 	putOnPostCompletionProcessBatchQueue = null;
	private boolean		primoClienteIsCointestatarioPuro = false;
	
	private String		skipWarningCommandName = null;
	private BooleanType skipCommandWarnings = new BooleanType();
	
	private DoubleType			scaleFactor = new DoubleType(DEFAULT_SCALE_FACTOR);
	private IntegerType 		scrollYValue = new IntegerType();
	private IntegerType 		scrollXValue = new IntegerType();

	private StringType  		pdfCompilationMode = new StringType();
	private PdfDataModel 		pdfData = new PdfDataModel();
	
	private StringType			mainCodAgente;
	private PdfPersonModel		personaCorrente;

	private String	 			pdfGeneratedBarcodes = "";
	private String	 			titoloFineOperazione = null;
	private String	 			messaggioFineOperazione = null;
	private String	 			messaggioPdfAlert = null;
	private String	 			messaggioPdfAmlAlert = null;
	private StringType			codiciOperazionePritPerMultioperazione = new StringType();
	private ListType			listaCodiciOperazionePritPerMultioperazione = null;
	
	private ArrayList<PdfPersonModel> fullProcessPersons = new ArrayList<PdfPersonModel>();
	private ArrayList<PdfAnagModel>	  pdfAnags = null;
	
	private PdfPersonModel		emptyAnagraficaSoggetto = null;
	private byte[]				pdfTestContent = null;
	
	private MapCommandDataModel	callbackData = null;
	private PdfDataModel 		pdfDataBeforeImage = null;
	private ArrayList<String> 	saveDateSott = null;
	private ArrayList<String> 	saveOreSott = null;
	
	private StringType			profiloUtente = new StringType();
	private StringType			eventName = new StringType();
	private StringType          eventArgs = new StringType();
	private BooleanType			reloadPageOnEvent = new BooleanType();
	private String[]			globalPageDriverJsScript = null;
	private Map<String, String> fieldsJsScript = null;
	
	private StringType			pdfNoteFBCopernico = new StringType();
	private StringType			isFirmaADistanza = new StringType();

	// Mifid call data
	private BooleanType			isMifidSkipped = new BooleanType(false);
	private MifidCallModel		mifidCallModel = null;
	private MifidManlevaDataModel		mifidManlevaData = null;
	
	// Idd call data
	private IddCallModel		iddCallModel = null;
	private transient FlagControlloTargetMarketBackupBean flagControlloTargetMarketBackup = null; // RFC #292576: per non chiamare ogni volta il servizio pilota

	// AML data
	private String				codiceModuloAmlHidden = null;
	private transient AmlModel	amlModel = null;
	private transient CoraModel	coraModel = null;

	// Sostituzioni data
	private PreferenzeClienteSostituzioniModel 	preferenzeClienteSostituzioni = new PreferenzeClienteSostituzioniModel();
	private SostituzioniCallModel				sostituzioniCallModel = null;

	// Report adeguatezza call data
	private ReportAdeguatezzaCallModel			reportAdeguatezzaCallModel = null;
	private RecuperaReportAdeguatezzaCallModel 	recuperaReportAdeguatezzaCallModel = null;
	private boolean								firstSignPage = true;
	private boolean								reportAdeguatezzaClicked = false;
	private boolean								raccomandazioneIddClicked = false;
	private String								dataUltimaChiamataAdeguatezza = null;

	// Attachments
	private IntegerType attachIdx = new IntegerType();
	private ListType	pdfAttachments = null;
	private ListType 	pdfInstanceAttachments = new ListType(PdfInstanceAttachModel.class);
	private ListType 	pdfInstanceOriginalAttachments = new ListType(PdfInstanceAttachModel.class);
	
	// Autocomplete support fields
	private StringType  noElementsIndicator = new StringType();
	private StringType  autocompleteFieldName = new StringType();
	private StringType  autocompleteTerm = new StringType();
	
	// Technical data
	private Map<String, PdfPersonModel> personsCache = new Hashtable<String, PdfPersonModel>();
	private String xmlSrvSend = null;
	private String xmlSrvReceived = null;
	private TimestampType 	dataOraUltimaModifica = new TimestampType();
	private StringType 		codUtenteUltimaModifica = new StringType();
	private boolean			concurrencyViolationFounded = false;
	private BooleanType		materialePrecontrattualeClicked = new BooleanType();
	private BooleanType		materialePrecontrattualeAccessorioClicked = new BooleanType();
	private BooleanType		presaVisioneMaterialePrecontrattuale = new BooleanType();
	private List<String>	preloadedPersonFieldsEditableIfNull = null;
	private String			lastSpeakOnPageLoad = ""; //Accessibilità
	
	// Technical message codes data
	private Map<String, String> errorsCodes = new HashMap<String, String>();
	private Map<String, String> warningsCodes = new HashMap<String, String>();
	
	// MOM integration data
	private boolean				pdfOnMomValidation = false;
	private boolean				processoNonVirtuoso = false;
	private PritMomInfoModel	pritMomInfoModel = null;
	private MomEventDataModel	momEventData = new MomEventDataModel();
	private StringType 			tipoProcessoSede = new StringType(); // a livello globale: MOM2 se tutti i moduli sono MOM2
	private transient Map<String, Properties> errorsPropsMap = null; // Property files stateful frontend cache
	private transient Map<String, Properties> warningsPropsMap = null; // Property files stateful frontend cache
	private transient Map<String, Properties> typeErrorsPropsMap = null; // Property files stateful frontend cache
	
	// Clone data
	private StringType  originalPdfInstanceId = new StringType();

	// Dati di transito per chiamata al servizio "dispositiva"
	private String codiceGuidDocumento = null;

	// Dati per pilota copernico smart
	private String prodottiPilotaCopernicoSmart = null;
	private String clientiInPilotaCopernicoSmart = null;
	
	// Configurazione disabilitazione modalità di sottoscrizione e messaggio copernico
	private String confDisabilitazioneFirmaDigitale = null;
	private String confDisabilitazioneCopernico = null;
	private String messaggioCopernico = null;

	// Validation integration data
	private boolean						pdfOnValidation = false;
	private PdfValidationEventDataModel	pdfValidationEventData = new PdfValidationEventDataModel();

	//Transient basket input data ************************************************
	private transient Basket basket = null;

	// Doppia spunta
	private List<PdfModel> pdfOriginali = null;

	// Legale rappresentante
	private boolean  gestioneLegaleRappresentanteAttivaGiaCaricata = false;
	private boolean  gestioneLegaleRappresentanteAttiva = false;

	/***********************************************************************************************/
	/***********************************************************************************************/
	public StringType getCodAgeFiltro() {
		if(isOperatoreMOM())
			return new StringType("0000000000");
		return getPdfData().getCodAgeImpersonato().isNull() ? getPdfData().getAgente().getCodAgente() : getPdfData().getCodAgeImpersonato();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfModel(){
		setUploadMaxSize(4000*1024);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfAnagModel mainPdfAnag() {
		return getPdfAnags().get(0);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfAnagModel getPdfAnag() {
		return getPdfAnags().get(getPdfData().getPdfIndex().intValue());
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isFirmaDigitaleAccessibile(){
		if(!isFirmaDigitaleVisibile())
			return false;	
		if(isQuestionarioIddProvvisorioOSospesoInBasket())
			return false;
		if(isInBasket() && getBasket().isSoloFirmaOlografa())
			return false;
		if(getMifidCallModel() != null && getMifidCallModel().isSoloFirmaOlografa())
			return false;
		if(existOtherSottoscrittoriPG())
			return false;
		return true;
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isFirmaDigitaleVisibile(){
		if(isModalitaDiSottoscrizioneDisabilitata(confDisabilitazioneFirmaDigitale))
			return false;
		if(getMainCodAgente() == null)
			return false;
		if(getUserSessionContext().getClientSessionContext().isAssistenteFB())
			return false;
		if(getIsSede().booleanValue())
			return false;
		if(Tools.fillSx(getMainCodAgente().toString(),'0',10).equals("0000000108"))
			return false;
		return true;
	}	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isCopernicoAccessibile(){
		if(!isCopernicoVisibile())
			return false;	
		if(isQuestionarioIddProvvisorioOSospesoInBasket())
			return false;
		if(isInBasket() && getBasket().isSoloFirmaOlografa())
			return false;
		if(getMifidCallModel() != null && getMifidCallModel().isSoloFirmaOlografa())
			return false;
		if(isSottoscrittorePG() || existOtherSottoscrittoriPG())
			return false;
		return true;
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isCopernicoVisibile(){
		if(isModalitaDiSottoscrizioneDisabilitata(confDisabilitazioneCopernico))
			return false;
		if(getMainCodAgente() == null)
			return false;
		if(getUserSessionContext().getClientSessionContext().isAssistenteFB())
			return false;
		if(getIsSede().booleanValue())
			return false;
		if(isPrimoClienteAgente(mainPdfData()))
			return false;
		if(isPrimoClienteIsCointestatarioPuro())
			return false;
		// Pilota copernico smart.
		if(prodottoPilotaCopernicoSmart() && !clienteInPilotaCopernicoSmart())
			return false;
		return true;
	}

	/***********************************************************************************************/
	/* Inizializzazione configurazione disabilitazione modalità di sottoscrizione
	 * 
	   Esempi di configurazione:
	 
	  	SEZIONE: PROCESSO_DI_VENDITA
		PARAMETRO: PERIODO_DISABILITAZIONE_FIRMA_DIGITALE
		VALORE: 01-01-2024 31-01-2024=FI02,FI03,LNBS04,fondiirlandesiiniziali
		
		SEZIONE: PROCESSO_DI_VENDITA
		PARAMETRO: PERIODO_DISABILITAZIONE_COPERNICO
		VALORE: 01-01-2024 02-01-2024=*		
	
	*/
	/***********************************************************************************************/
	private static final String SEZIONE_CONF_PROCESSO_DI_VENDITA = "PROCESSO_DI_VENDITA";
	public void initModalitaDiSottoscrizioneDisabilitate(ClientSessionContext csc) throws Exception{
		if(confDisabilitazioneFirmaDigitale == null)
			confDisabilitazioneFirmaDigitale = PdfConfig.getParamAsString(csc, SEZIONE_CONF_PROCESSO_DI_VENDITA, "PERIODO_DISABILITAZIONE_FIRMA_DIGITALE").toString();
		if(confDisabilitazioneCopernico == null)
			confDisabilitazioneCopernico = PdfConfig.getParamAsString(csc, SEZIONE_CONF_PROCESSO_DI_VENDITA, "PERIODO_DISABILITAZIONE_COPERNICO").toString();
	}

	/***********************************************************************************************/
	/* Inizializzazione configurazione messaggio copernico
	 * 
	   Esempi di configurazione:
	 
	  	SEZIONE: PROCESSO_DI_VENDITA
		PARAMETRO: PERIODO_MESSAGGIO_COPERNICO
		VALORE: 01-01-2024 31-01-2024=messaggio
	
	*/
	/***********************************************************************************************/
	private static final String PARAM_PERIODO_MESSAGGIO_COPERNICO = "PERIODO_MESSAGGIO_COPERNICO";
	public void initMessaggioCopernico(ClientSessionContext csc){
		if(messaggioCopernico != null)
			return;		
		try {
			messaggioCopernico = "";
			String periodoAsString = PdfConfig.getParamAsString(csc, SEZIONE_CONF_PROCESSO_DI_VENDITA, PARAM_PERIODO_MESSAGGIO_COPERNICO).toString();
			if(!periodoAsString.isEmpty()) {
				String[] periodo = periodoAsString.split("\\s+");
				DateType dataInizio = new DateType(periodo[0]);
				DateType dataFine = new DateType(periodo[1]);
				DateType oggi = Tools.today();
				if(oggi.compareTo(dataInizio) >= 0 && oggi.compareTo(dataFine) <= 0) 
					messaggioCopernico = PdfConfig.getExtendedValue(csc, SEZIONE_CONF_PROCESSO_DI_VENDITA, PARAM_PERIODO_MESSAGGIO_COPERNICO).toString();
			}
		}catch(Exception e) {
			messaggioCopernico = "";
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean isModalitaDiSottoscrizioneDisabilitata(String config) {
		if(config == null || config.isEmpty())
			return false;
		try {
			String[] vals = config.split("=");
			String periodoAsString = vals[0];
			String[] periodo = periodoAsString.split("\\s+");
			DateType dataInizio = new DateType(periodo[0]);
			DateType dataFine = new DateType(periodo[1]);
			DateType oggi = Tools.today();
			if(oggi.compareTo(dataInizio) < 0 || oggi.compareTo(dataFine) > 0)
				return false;
			
			String codiciAsString = vals[1];
			String[] codici = codiciAsString.split(",");	
			ArrayList<String> codiciAsArray = new ArrayList<String>(Arrays.asList(codici)); 
			if(isInBasket()) {
				for(BasketElement be : getBasket().getBasketElements()) {
					PdfModel bepdf = be.getDispoPdf();
					if(isModalitaDiSottoscrizioneModuloDisabilitata(codiciAsArray, bepdf))
						return true;
				}
			}else {
				return isModalitaDiSottoscrizioneModuloDisabilitata(codiciAsArray, this);
			}
			return false;
		}catch(Exception e) {
			return false;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean isModalitaDiSottoscrizioneModuloDisabilitata(ArrayList<String> codiciAsArray, PdfModel pdf) {
		if(pdf.getPdfAnags() == null)
			return false;
		
		if(codiciAsArray.contains("*"))
			return true;
		
		for(PdfAnagModel pdfAnag : pdf.getPdfAnags()) {
			if(!pdfAnag.getPdfMomCode().isNull() && codiciAsArray.contains(pdfAnag.getPdfMomCode().toString()))
				return true;
			if(!pdfAnag.getPdfDriverName().isNull() && codiciAsArray.contains(pdfAnag.getPdfDriverName().toString()))
				return true;
			if(!pdfAnag.getPdfCodLineaBusiness().isNull() && codiciAsArray.contains("LNBS"+pdfAnag.getPdfCodLineaBusiness().toString()))
				return true;
		}
		return false;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isMultiPdf(){
		return getPdfData().getPdfs().size() > 0 ? true : false;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfDataModel mainPdfData(){
		return getPdfData().getPdfs().size() > 0 ? (PdfDataModel)getPdfData().getPdfs().get(0) : getPdfData();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public int getNumPages() {
		return getPdfData().getPdfInfos().getPageWidths().length;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public float getPageHeight(int pagNum) {
		return getPdfData().getPdfInfos().getPageHeights()[pagNum-1];
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public float getPageWidth(int pagNum) {
		return getPdfData().getPdfInfos().getPageWidths()[pagNum-1];
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public float getMaxPageWidth() {
		try{
			float w = 0;
			for(int i=0;i<getNumPages();i++){
				if(getPdfData().getPdfInfos().getPageWidths()[i] > w)
					w = getPdfData().getPdfInfos().getPageWidths()[i];
			}
			return w;
		}catch(Throwable t){
			return 0;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean pageHasFields(int pagNum) {
		for(PdfFieldInfos fi : getPdfData().getPdfInfos().getFieldInfos()){
			if(fi.page == pagNum)
				return true;
		}
		return false;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public List<PdfFieldInfos> getPersonSignFields(PdfPersonModel person){
		List<PdfFieldInfos> result = new ArrayList<PdfFieldInfos>();
		for(int i=1;i<=getNumPages();i++)
			result.addAll(getPersonSignFieldsInPage(person, i, -1));
		return result;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public List<PdfFieldInfos> getPersonSignFieldsInPage(PdfPersonModel person, int pageNum, int cliIndex){
		
		List<PdfFieldInfos> result = new ArrayList<PdfFieldInfos>();
		for(PdfFieldInfos fi : getPdfData().getPdfInfos().getFieldInfos()){
			if(fi.page != pageNum)
				continue;
			if(fi.fieldType != AcroFields.FIELD_TYPE_SIGNATURE)
				continue;
			Matcher mat = null;
			if(person.isAgente())
				mat = PdfPredefinedFields.AGENTE_FIRMA_N_PATTERN.matcher(fi.htmlFieldName);
			else
				mat = PdfPredefinedFields.CLIENTE_FIRMA_N_DI_M_PATTERN.matcher(fi.htmlFieldName);
			if(!mat.matches())
				continue;
			
			String propertyName = null;
			if(person.isAgente()){
				propertyName = "firma"+mat.group(1)+"Agente";
			}else{
				if(cliIndex < 0){	// On current person
					if(!person.getIndexInFieldName().contains(Integer.parseInt(mat.group(2))))
						continue;
				}else{				// On other persons
					if(Integer.parseInt(mat.group(2)) != cliIndex)
						continue;
				}
				propertyName = "firma"+mat.group(1)+"Cliente"+mat.group(2);
			}
			AbstractType field = getPdfData().readProperty(propertyName);
			if(field == null)
				continue;
			
			result.add(fi);
		}		
		return result;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public List<PdfFieldInfos> getPersonClauseFields(PdfPersonModel person){
		List<PdfFieldInfos> result = new ArrayList<PdfFieldInfos>();
		for(int i=1;i<=getNumPages();i++)
			result.addAll(getPersonClauseFieldsInPage(person, i));
		return result;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public List<PdfFieldInfos> getPersonClauseFieldsInPage(PdfPersonModel person, int pageNum){
		
		List<PdfFieldInfos> result = new ArrayList<PdfFieldInfos>();
		for(PdfFieldInfos fi : getPdfData().getPdfInfos().getFieldInfos()){
			if(fi.page != pageNum)
				continue;
			if(fi.fieldType == AcroFields.FIELD_TYPE_SIGNATURE)
				continue;
			Matcher mat = null;
			if(person.isAgente())
				mat = PdfPredefinedFields.AGENTE_CLAUSOLA_N_PATTERN.matcher(fi.htmlFieldName);
			else
				mat = PdfPredefinedFields.CLIENTE_CLAUSOLA_N_DI_M_PATTERN.matcher(fi.htmlFieldName);
			if(!mat.matches())
				continue;
			
			String propertyName = null;
			if(person.isAgente()){
				propertyName = "clausola"+mat.group(1)+"Agente";
			}else{
				if(!person.getIndexInFieldName().contains(Integer.parseInt(mat.group(2))))
					continue;
				propertyName = "clausola"+mat.group(1)+"Cliente"+mat.group(2);
			}
			AbstractType field = getPdfData().readProperty(propertyName);
			if(field == null)
				continue;
			
			result.add(fi);
		}		
		return result;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void clearEndProcessFields() throws Exception{
		setDataUltimaChiamataAdeguatezza(null);
		restoreDataSottoscrizione();
		clearSignFieldsOnPdfData(getPdfData());
		getPdfData().setPdfBarcode(new StringType());
		if(isMultiPdf()){
			for(int i=0;i<getPdfData().getPdfs().size();i++){
				PdfDataModel pd = (PdfDataModel)getPdfData().getPdfs().get(i);
				clearSignFieldsOnPdfData(pd);
				pd.setPdfBarcode(new StringType());
			}			
		}
		ReportAdeguatezzaCaller.clearDerogaSostituzione(this);
		setMessaggioFineOperazione(null);
		resetCommandErrors();
		resetCommandMessages();
		resetCommandWarnings();
		return;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void saveAndSetDataSottoscrizione(){	
		ArrayList<String> locSaveDateSott = new ArrayList<String>();
		ArrayList<String> locSaveOreSott = new ArrayList<String>();
		if(isMultiPdf()){
			for(int i=0;i<getPdfData().getPdfs().size();i++){
				PdfDataModel pdfDataElement = (PdfDataModel)getPdfData().getPdfs().get(i);
				setDataOraSottoscrizioneValue(locSaveDateSott, locSaveOreSott, pdfDataElement);
			}
		}else{			
			setDataOraSottoscrizioneValue(locSaveDateSott, locSaveOreSott, getPdfData());
		}
		setSaveDateSott(locSaveDateSott);		
		setSaveOreSott(locSaveOreSott);		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void setDataOraSottoscrizioneValue(ArrayList<String> saveDateSottList, ArrayList<String> saveOreSottList, PdfDataModel pdfData) {
		AbstractType dataSottoscrizione = pdfData.readProperty(PdfPredefinedFields.DATA_SOTTOSCRIZIONE);
		if(dataSottoscrizione != null){
			saveDateSottList.add(dataSottoscrizione.toString());
			dataSottoscrizione.setStringValue(Tools.today().toString());
		}else{
			saveDateSottList.add("");
		}	
		
		AbstractType oraSottoscrizione = pdfData.readProperty(PdfPredefinedFields.ORA_SOTTOSCRIZIONE);
		if(oraSottoscrizione != null){
			saveOreSottList.add(oraSottoscrizione.toString());
			TimestampType now = Tools.now();
			oraSottoscrizione.setStringValue(now.getHH()+":"+now.getMI());
		}else{
			saveOreSottList.add("");
		}		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void restoreDataSottoscrizione(){
		ArrayList<String> locSaveDateSott = getSaveDateSott();
		ArrayList<String> locSaveOreSott = getSaveOreSott();
		if(locSaveDateSott == null || locSaveOreSott == null || locSaveDateSott.isEmpty() || locSaveOreSott.isEmpty())
			return;
		if(isMultiPdf()){
			for(int i=0;i<getPdfData().getPdfs().size();i++){
				PdfDataModel pdfDataElement = (PdfDataModel)getPdfData().getPdfs().get(i);
				restoreDataOraSottoscrizioneValue(locSaveDateSott, locSaveOreSott, pdfDataElement, i);
			}
		}else{
			restoreDataOraSottoscrizioneValue(locSaveDateSott, locSaveOreSott, getPdfData(), 0);
		}
		setSaveDateSott(null);
		setSaveOreSott(null);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void restoreDataOraSottoscrizioneValue(ArrayList<String> saveDateSottList, ArrayList<String> saveOreSottList, PdfDataModel pdfData, int index) {
		AbstractType dataSottoscrizione = pdfData.readProperty(PdfPredefinedFields.DATA_SOTTOSCRIZIONE);
		if(dataSottoscrizione != null){
			dataSottoscrizione.setStringValue(saveDateSottList.get(index));
		}

		AbstractType oraSottoscrizione = pdfData.readProperty(PdfPredefinedFields.ORA_SOTTOSCRIZIONE);
		if(oraSottoscrizione != null){
			oraSottoscrizione.setStringValue(saveOreSottList.get(index));
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void clearSignFieldsOnPdfData(PdfDataModel pdfData) throws Exception{
		AbstractTypePropertyDescriptor[] propsArray = pdfData.getMappedPropertyDescriptors();
		for(int i=0;i<propsArray.length;i++){
			if(PdfPredefinedFields.AGENTE_FIRMA_N_PATTERN.matcher(propsArray[i].getName()).matches() ||
			   PdfPredefinedFields.AGENTE_NOME_FIRMA_N_PATTERN.matcher(propsArray[i].getName()).matches() ||
			   PdfPredefinedFields.CLIENTE_FIRMA_N_DI_M_PATTERN.matcher(propsArray[i].getName()).matches() ||
			   PdfPredefinedFields.CLIENTE_NOME_FIRMA_N_DI_M_PATTERN.matcher(propsArray[i].getName()).matches()){
				
				propsArray[i].getValue().setStringValue("");
				
			}else if(PdfPredefinedFields.AGENTE_CLAUSOLA_N_PATTERN.matcher(propsArray[i].getName()).matches() ||
					 PdfPredefinedFields.CLIENTE_CLAUSOLA_N_DI_M_PATTERN.matcher(propsArray[i].getName()).matches()){
						 
				propsArray[i].getValue().setStringValue("false");
				
			}

		}	
		return;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfPersonModel firstFilledPerson(){
		for(int i=0;i<getFullProcessPersons().size();i++){
			PdfPersonModel person = getFullProcessPersons().get(i);
			if(!person.isEmty())
				return person;
		}
		return null;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean pdfIsInCartaChimica(){
		AbstractType numeroCartaChimica = mainPdfData().readProperty(PdfPredefinedFields.NUMERO_CARTA_CHIMICA);
		if(numeroCartaChimica == null || numeroCartaChimica.isNull())
			return false;
		else
			return true;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfPersonModel findClienteInPdfData(PdfPersonModel inputCli){
		for(int j=0;j<getPdfData().getClienti().size();j++){
			PdfPersonModel c = getPdfData().getClienti().get(j);
			if(c.isEqual(inputCli))
				return c;
		}
		return null;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean hasProcessoControlliCompleti(){
		if(getPdfData().getIsVolatile().booleanValue())
			return true;
		if(getPdfAnags() == null || getPdfAnags().size() == 0)
			return false;
		PdfAnagModel anag = mainPdfAnag();
		if(!anag.getPdfDriverName().isNull())
			return true;
		if(anag.getIsControlliCompleti().booleanValue())
			return true;
		return false;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean hasDataSottoscrizioneOggi(){
		if(getPdfAnags() == null)
			return false;
		if(isMultiPdf()){
			for(int i=0;i<getPdfAnags().size();i++){
				PdfAnagModel anag = getPdfAnags().get(i);
				if(anag == null)
					continue;
				if(anag.getHasDataSottoscrizioneOggi().booleanValue())
					return true;
			}
			return false;
		}else{
			return getPdfAnag().getHasDataSottoscrizioneOggi().booleanValue();	
		}
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void initCompilationModes(PdfDataModel pdfData){
		
		PdfAnagModel pdfMainAnag = mainPdfAnag();
		PdfDataModel pdfMainData = mainPdfData();
		
		String compilationModes = "";
		if(!pdfMainData.getInputCompilationModes().isNull()){
			compilationModes = pdfMainData.getInputCompilationModes().toString();
		}else{
			if(pdfMainAnag.getPdfIsCartaLiberaEnabled().booleanValue())
				compilationModes += PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_LIBERA+",";
			if(pdfMainAnag.getPdfIsCartaChimicaEnabled().booleanValue())
				compilationModes += PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_CHIMICA+",";
			if(pdfMainAnag.getPdfIsFirmaDigitaleEnabled().booleanValue())
				compilationModes += PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_FIRMA_DIGITALE+",";
			if(pdfMainAnag.getPdfIsCopernicoEnabled().booleanValue())
				compilationModes += PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_COPERNICO+",";
			if(pdfMainAnag.getPdfIsStampaEnabled().booleanValue())
				compilationModes += PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_STAMPA+",";
			if(compilationModes.length() > 0)
				compilationModes = compilationModes.substring(0,compilationModes.length()-1);
			else
				compilationModes = PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_NESSUNA;
		}
		
		if(compilationModes.length() > 0){
			if(isMultiPdf()){
				for(int i=0;i<getPdfData().getPdfs().size();i++){
					PdfDataModel pd = (PdfDataModel)getPdfData().getPdfs().get(i);
					pd.setCompilationModes(new StringType(compilationModes));
				}			
			}else{
				pdfData.setCompilationModes(new StringType(compilationModes));
			}
		}

	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public StringType globalPdfCompilationModes(){
		if(isMultiPdf()){
			String valid = "";
			String[] all = new String[]{
					PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_COPERNICO,
					PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_FIRMA_DIGITALE,
					PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_CHIMICA,
					PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_LIBERA,
					PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_STAMPA};
			for(int i=0;i<all.length;i++){
				boolean isValid = true;
				for(int j=0;j<getPdfData().getPdfs().size();j++){
					PdfDataModel pdfDataElement = (PdfDataModel)getPdfData().getPdfs().get(j);
					if(pdfDataElement.getCompilationModes().toString().indexOf(all[i]) < 0){
						isValid = false;
						break;
					}
				}
				if(isValid && valid.indexOf(all[i]) < 0)
					valid += all[i]+",";
			}
			if(valid.length() > 0)
				valid = valid.substring(0, valid.length()-1);
			return new StringType(valid);
		}else{
			return getPdfData().getCompilationModes();
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String getIdQuestionarioIdd(){
		if(  getIddCallModel() != null &&
			 getIddCallModel().getInput() != null && 
			!getIddCallModel().getIdQuestionarioIdd().isNull())
			return getIddCallModel().getIdQuestionarioIdd().toString();
		else
			return ""; 
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String getIdRaccomandazioneIdd(){
		return getPdfData().getIdRaccomandazioneIdd().toString(); 
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isQuestionarioIddProvvisorioOSospesoInBasket(){
		if(!isInBasket())
			return isQuestionarioIddProvvisorioOSospeso();
		for(BasketElement be : getBasket().getBasketElements()) {
			PdfModel bepdf = be.getDispoPdf();
			if(bepdf.isQuestionarioIddProvvisorioOSospeso())
				return true;
		}
		return false;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isQuestionarioIddProvvisorioOSospeso(){
		return  getIddCallModel() != null && 
				getIddCallModel().getInput() != null &&
			   !getIddCallModel().getIdQuestionarioIdd().isNull() &&
				
			   (getIddCallModel().getCodStatoQuestionarioIdd().equals(IddStatiQuestionario.PROVVISORIO) ||
				getIddCallModel().getCodStatoQuestionarioIdd().equals(IddStatiQuestionario.SOSPESO) );
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String getIdReportAdeguatezza(){
		if(  getReportAdeguatezzaCallModel() != null &&
			!getReportAdeguatezzaCallModel().getIdReportAdeguatezza().isNull())
			return getReportAdeguatezzaCallModel().getIdReportAdeguatezza().toString();
		else
			return getPdfData().getIdReportAdeguatezza().toString(); 
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public String getIdSostituzione(){
		if(  getSostituzioniCallModel() != null &&
			!getSostituzioniCallModel().getIdSostituzione().isNull())
			return getSostituzioniCallModel().getIdSostituzione().toString();
		else
			return getPdfData().getIdSostituzione().toString(); 
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean reportAdeguatezzaPassatoDalChiamate(){
		return !getPdfData().getNumOrdineReportAdeguatezza().isNull();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String getStatoRecueroReportAdeguatezza(){
		if(getIdReportAdeguatezza().length() == 0 || getRecuperaReportAdeguatezzaCallModel() == null)
			return "NONE";
		else
			return getRecuperaReportAdeguatezzaCallModel().getStatoReportAdeguatezza();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public String writeLinksReportAdeguatezza(){
		if(getIdReportAdeguatezza().length() == 0 || getRecuperaReportAdeguatezzaCallModel() == null)
			return "[]";
		else
			return getRecuperaReportAdeguatezzaCallModel().writeLinksReportAdeguatezza();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void initMaxRecuperaReportAdeguatezzaRetryCount(ClientSessionContext csc){
		if(getMaxRecuperaReportAdeguatezzaRetryCount() != null)
			return;
		setMaxRecuperaReportAdeguatezzaRetryCount(10);
		try{
			IntegerType maxRecuperaReportAdeguatezzaRetryCount = PdfConfig.getParamAsInt(csc, "REPORT_ADEGUATEZZA", "MAX_RECUPERO_RETRY_COUNT");
			if(maxRecuperaReportAdeguatezzaRetryCount != null && !maxRecuperaReportAdeguatezzaRetryCount.isNull())
				setMaxRecuperaReportAdeguatezzaRetryCount(new Integer(maxRecuperaReportAdeguatezzaRetryCount.toString()));
		}catch(Throwable t){
			setMaxRecuperaReportAdeguatezzaRetryCount(10);
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public int getNumDeclaredPdfSignFields(){
		int res = 0;
		if(isMultiPdf()){
			for(int i=0;i<getPdfData().getPdfs().size();i++){
				PdfDataModel pd = (PdfDataModel)getPdfData().getPdfs().get(i);
				res += pd.getPdfInfos().numDeclaredSignFields();
			}			
		}else{
			res = getPdfData().getPdfInfos().numDeclaredSignFields();
		}
		return res;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public int getNumPdfSignFields(){
		int res = 0;
		if(isMultiPdf()){
			for(int i=0;i<getPdfData().getPdfs().size();i++){
				PdfDataModel pd = (PdfDataModel)getPdfData().getPdfs().get(i);
				res += pd.getPdfInfos().numSignFields(pd);
			}			
		}else{
			res = getPdfData().getPdfInfos().numSignFields(getPdfData());
		}
		return res;
	}
	
	
	/***********************************************************************************************/
	// Allinea le proprietà di processo che potrebbero essere impostate dai driver e che vanno aggiornate
	// su tutte le istanze di "pdfData" degli eventuali altri pdf 
	/***********************************************************************************************/
	public void alignDynamicProcessProperties(){
		PdfDataModel procData = getPdfData();
		if(procData.getPdfs().size() > 0){
			for(int i=0;i<procData.getPdfs().size();i++){
				PdfDataModel pdfDataElement = (PdfDataModel)procData.getPdfs().get(i);
				pdfDataElement.setIsSwitch(new BooleanType(procData.getIsSwitch().booleanValue()));
			}
		}
	}

	/***********************************************************************************************/
	/* riconoscimento operatore/fasi lavorazione MOM
	/***********************************************************************************************/
	public boolean isOperatoreMOM() {
		return getPdfData().isOperatoreMOM();
	}
	public boolean isInValidazioneMOM() {
		if(!isOperatoreMOM())
			return false;
		return !isInInserimentoMOM();
	}
	public boolean isInInserimentoMOM() {
		if(!isOperatoreMOM())
			return false;
		if(getPdfData().getExternalEntityName().equals(CostantiMOM.MOM_EXTERNAL_IMPORTED_KEY_ENTITY_NAME))
			return false;
		if(getPdfData().getExternalEntityName().equals(CostantiMOM.MOM_EXTERNAL_DOPPIASPUNTA_VALIDAZIONEMOM_KEY_ENTITY_NAME))
			return false;
		if(getPdfData().getExternalEntityName().equals(CostantiMOM.MOM_EXTERNAL_DOPPIASPUNTA_INSERTMENTOMOM_KEY_ENTITY_NAME))
			return true;
		return getPdfData().getPdfEnvironment().equals(CostantiMOM.MOM_ENVIRONMENT);
	}
	public boolean isInDoppiaSpuntaMOM() {
		return getPdfData().isOperatoreMOM() && 
			   getPdfData().getPdfEnvironment().equals(CostantiMOM.MOM_ENVIRONMENT) &&
			   getPdfData().getExternalEntityName().toString().startsWith(CostantiMOM.MOM_EXTERNAL_DOPPIASPUNTA_KEY_ENTITY_NAME_PREFIX);
	}

	/***********************************************************************************************/
	/* riconoscimento attore cliente
	/***********************************************************************************************/
	public boolean isInAccettazioneCopernico() {
		return getPdfData().isProcessoAccettazioneCopernico();
	}

	/***********************************************************************************************/
	/* rm62711-MIFID 2 in prod. Pilota sempre attivo
	/***********************************************************************************************/
	@Deprecated
	public Boolean getIsPilotaMifid2() {
		return true;
	}

	/***********************************************************************************************/
	// Inizializzazione dati pilota copernico smart
	/***********************************************************************************************/
	public void initPilotaCopernicoSmart(ClientSessionContext csc) throws Exception{
		if(prodottiPilotaCopernicoSmart == null)
			prodottiPilotaCopernicoSmart = loadConfPilotaCopernicoSmart(csc, "PRODOTTI_SOLO_SMART");
		
		if(clientiInPilotaCopernicoSmart == null)
			clientiInPilotaCopernicoSmart = loadConfPilotaCopernicoSmart(csc, "CLIENTI_IN_PILOTA");
	}
	private boolean prodottoPilotaCopernicoSmart(){
		if(prodottiPilotaCopernicoSmart == null)
			return true;
		if(prodottiPilotaCopernicoSmart.length() == 0)
			return true;
		return prodottiPilotaCopernicoSmart.indexOf(mainPdfAnag().getPdfMomCode().toString()) >= 0;
	}
	private boolean clienteInPilotaCopernicoSmart(){
		if(clientiInPilotaCopernicoSmart == null)
			return true;
		if(clientiInPilotaCopernicoSmart.length() == 0)
			return true;
		StringType ndg = (StringType)mainPdfData().read("ndgCliente1");
		if(ndg == null || ndg.isNull())
			return false;
		String filledndg = Tools.fillSx(ndg.toString(), '0', 11);
		return clientiInPilotaCopernicoSmart.indexOf(filledndg) >= 0;
	}
	public boolean isProdottoCopernicoSmart(){
		return true;
	}
		
	/***********************************************************************************************/
	/* Per gestione pilota copernico smart nei driver
	/***********************************************************************************************/
	public Boolean getIsPilotaCopernicoSmart() {
		return isProdottoCopernicoSmart();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isPrimoClienteAgente(PdfDataModel pdfData){
		PdfPersonModel firstPerson = pdfData.getClienti().get(0);
		return firstPerson != null && !firstPerson.isEmty() && firstPerson.isClienteAgente();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isSottoscrittorePG(){
		PdfPersonModel firstPerson = mainPdfData().getPerson(1);
		return firstPerson != null && !firstPerson.isEmty() && firstPerson.isPersonaGiuridica();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean existOtherSottoscrittoriPG(){
		for(int i=1;i<getPdfData().getClienti().size();i++) {
			PdfPersonModel p = getPdfData().getClienti().get(i);
			if(p != null && !p.isEmty() && p.isPersonaGiuridica()) 
				return true;
		}
		return false;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private String loadConfPilotaCopernicoSmart(ClientSessionContext csc, String codice){
		try{
			String v = "";
			ListType els = DAOObject.executeDynaQueryAccess(csc, "CEPE", 
													"select DOMINIO_X_DESCR from INR_CE_DOMINIO where "
													+ 	  "DOMINIO_C_TABELLA = 'PILOTA_COPERNICO_SMART' "
													+ "and DOMINIO_C_CODICE like '"+codice+"%' "
													+ "and DOMINIO_F_VALIDITA = 'S'",
													null, MapCommandDataModel.class).getResult();
			if(els != null && els.size() > 0){
				for(int i=0;i<els.size();i++){
					MapCommandDataModel m = (MapCommandDataModel)els.get(i);
					StringType p = (StringType)m.readProperty("dominioXDescr");
					if(p != null && !p.isNull())
						v += p.toString()+",";
				}
			}
			if(v.endsWith(","))
				v = v.substring(0,v.length()-1);
			return v;
		}catch(Throwable t){
			return null;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isProtectionSpecialistInHub(){
		return getPdfData().getPdfEnvironment().equals("HUB_SPECIALISTI_PROTEZIONE") || getPdfData().getCodRuoloImpersonato().equals(CodRuoliImpersonati.FPS);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public ListType recuperaOrdiniRda() {		
		if (getIdReportAdeguatezza().equals("") || getIdReportAdeguatezza().length() == 0) {
			return null;
		}
		
		ListType result = new ListType(LegameDispositivaOrdineModel.class);
		
		if (mainPdfData().getNumOrdineReportAdeguatezza().isNull()) {
			ReportAdeguatezzaCallModel rdaCallModel = getReportAdeguatezzaCallModel();
			
			for(int i = 0; i < rdaCallModel.getOrdini().size(); i++) {
				OrdineModel ordineModel = (OrdineModel) rdaCallModel.getOrdini().get(i);
				
				if (ordineModel.getPadre().isNull()) {
					LegameDispositivaOrdineModel legameOrdineModel = new LegameDispositivaOrdineModel();
					legameOrdineModel.setIdReport(new StringType(getIdReportAdeguatezza()));
					legameOrdineModel.setProgr(ordineModel.getProgr());
					
					result.add(legameOrdineModel);
				}
			}				
		}
		else {
			// Da 5D						
			String[] listaProgr = mainPdfData().getNumOrdineReportAdeguatezza().toString().split(",");
			
			for(String progr : listaProgr) {
				LegameDispositivaOrdineModel ordineModel = new LegameDispositivaOrdineModel();
				ordineModel.setIdReport(new StringType(getIdReportAdeguatezza()));
				ordineModel.setProgr(new StringType(progr.trim()));
				
				result.add(ordineModel);
			}
		}	
		
		return result;
	}	
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void alignCommonToAllPdfDataProperties() {
		alignCommonToAllPdfDataPropertiesOnPdf(this, this);
		if(isInBasket()) {
			for(BasketElement be : getBasket().getBasketElements()) {
				if(be.getDispoPdf() != null && be.getDispoPdf() != this)
					alignCommonToAllPdfDataPropertiesOnPdf(this, be.getDispoPdf());
			}
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void alignCommonToAllPdfDataPropertiesOnPdf(PdfModel pdfFrom, PdfModel pdfTo) {
		alignCommonToAllPdfDataPropertyOnPdf(pdfFrom, pdfTo, PdfPredefinedFields.LUOGO);
		alignCommonToAllPdfDataPropertyOnPdf(pdfFrom, pdfTo, PdfPredefinedFields.TIPO_DISTANZA_COLLOCAMENTO);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void alignCommonToAllPdfDataPropertyOnPdf(PdfModel pdfFrom, PdfModel pdfTo, String propName) {
		AbstractType from = pdfFrom.getPdfData().read(propName);
		if(from==null || from.isNull())
			return;
		
		if(pdfTo.isMultiPdf()) {
			for(int i=0;i<pdfTo.getPdfData().getPdfs().size();i++) {
				PdfDataModel pdfDataElement = (PdfDataModel)pdfTo.getPdfData().getPdfs().get(i);
				AbstractType to = pdfDataElement.read(propName);
				if(to != null && to != from)
					to.setStringValue(from.toString());
			}
		}
		AbstractType to = pdfTo.getPdfData().read(propName);
		if(to != null && to != from)
			to.setStringValue(from.toString());
		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isInBasket() {
		return getBasket() != null;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isInBasketSemplt() {
		return !getPdfData().getIdCarrello().isNull() && !getPdfData().getPdfEnvironment().equals("CARRELLO");
	}	

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isInAccettazioneCopernicoMobile() {
		return isInAccettazioneCopernico() && (getPdfData().getSistemaClient().equals("L") || getPdfData().getSistemaClient().equals("D"));
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isTipoProcessoSedeMOM2() {
		return tipoProcessoSede.equals("MOM2");
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean allImplicitAttach() {
		if(getPdfAttachments() == null)
			return true;
        for(int i=0;i<getPdfAttachments().size(); i++){
        	PdfAttachModel pdfAttach = (PdfAttachModel)getPdfAttachments().get(i);
        	if(!pdfAttach.isImplicitAttach())
        		return false;
        }
        return true;
	}
	
	/***********************************************************************************************/
	private static final String CALL_SRV_DISPOSITIVA_IN_DIGITALE = "D";
	private static final String CALL_SRV_DISPOSITIVA_SEMPRE = "S";
	/***********************************************************************************************/
	public String callSrvDispositivaBMEDConfiguration(){
		if(!isMultiPdf() || !isTipoProcessoSedeMOM2()) {
			if(mainPdfData().getIsSwitch().booleanValue() && getPdfAnags() != null && getPdfAnags().size() > 1)
				return getPdfAnags().get(1).getCallSrvDispositivaBMED().toString().toUpperCase();
			else
				return mainPdfAnag().getCallSrvDispositivaBMED().toString().toUpperCase();
		}
		StringBuilder values = new StringBuilder();
		for(PdfAnagModel anag : getPdfAnags()) {
			StringType conf = anag.getCallSrvDispositivaBMED();
			if(conf.isNull()) // Se non configurato non partecipa alla definizione dell'info "chiamo si/no"
				continue;
			values.append(conf.toString());
		}
		// Nelle configurazioni "vince" il meno potente ma se nessuno prevede la chiamata non chiamiamo
		if(values.length() == 0)
			return "";
		return values.indexOf(CALL_SRV_DISPOSITIVA_IN_DIGITALE) >= 0 ? CALL_SRV_DISPOSITIVA_IN_DIGITALE : CALL_SRV_DISPOSITIVA_SEMPRE;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isVenditaComeBmed() {
		return getPdfAnags() != null && mainPdfAnag().isVenditaComeBmed();
	}

	public DoubleType getScaleFactor() {
		return scaleFactor;
	}

	public void setScaleFactor(DoubleType scaleFactor) {
		this.scaleFactor = scaleFactor;
	}

	public boolean isTestMode() {
		return testMode;
	}

	public void setTestMode(boolean testMode) {
		this.testMode = testMode;
	}

	public boolean isPdfOnWork() {
		return pdfOnWork;
	}

	public void setPdfOnWork(boolean pdfOnWork) {
		this.pdfOnWork = pdfOnWork;
	}

	public PdfDataModel getPdfData() {
		return pdfData;
	}

	public void setPdfData(PdfDataModel pdfData) {
		this.pdfData = pdfData;
	}

	public PdfPersonModel getPersonaCorrente() {
		return personaCorrente;
	}

	public void setPersonaCorrente(PdfPersonModel personaCorrente) {
		this.personaCorrente = personaCorrente;
	}

	public IntegerType getScrollYValue() {
		return scrollYValue;
	}

	public void setScrollYValue(IntegerType scrollYValue) {
		this.scrollYValue = scrollYValue;
	}

	public IntegerType getScrollXValue() {
		return scrollXValue;
	}

	public void setScrollXValue(IntegerType scrollXValue) {
		this.scrollXValue = scrollXValue;
	}

	public String getMessaggioFineOperazione() {
		return messaggioFineOperazione;
	}

	public void setMessaggioFineOperazione(String messaggioFineOperazione) {
		this.messaggioFineOperazione = messaggioFineOperazione;
	}

	public Class getFirstDisplayClass() {
		return firstDisplayClass;
	}

	public void setFirstDisplayClass(Class firstDisplayClass) {
		this.firstDisplayClass = firstDisplayClass;
	}

	public StringType getMainCodAgente() {
		return mainCodAgente;
	}

	public void setMainCodAgente(StringType mainCodAgente) {
		this.mainCodAgente = mainCodAgente;
	}

	public ArrayList<PdfPersonModel> getFullProcessPersons() {
		return fullProcessPersons;
	}

	public void setFullProcessPersons(ArrayList<PdfPersonModel> fullProcessPersons) {
		this.fullProcessPersons = fullProcessPersons;
	}

	public StringType getPdfCompilationMode() {
		return pdfCompilationMode;
	}

	public void setPdfCompilationMode(StringType pdfCompilationMode) {
		this.pdfCompilationMode = pdfCompilationMode;
	}

	public String getInitialErrorMsg() {
		return initialErrorMsg;
	}

	public void setInitialErrorMsg(String initialErrorMsg) {
		this.initialErrorMsg = initialErrorMsg;
	}

	public byte[] getPdfTestContent() {
		return pdfTestContent;
	}

	public void setPdfTestContent(byte[] pdfTestContent) {
		this.pdfTestContent = pdfTestContent;
	}

	public PdfPersonModel getEmptyAnagraficaSoggetto() {
		return emptyAnagraficaSoggetto;
	}

	public void setEmptyAnagraficaSoggetto(PdfPersonModel emptyAnagraficaSoggetto) {
		this.emptyAnagraficaSoggetto = emptyAnagraficaSoggetto;
	}

	public MapCommandDataModel getCallbackData() {
		return callbackData;
	}

	public void setCallbackData(MapCommandDataModel callbackData) {
		this.callbackData = callbackData;
	}

	public PdfDataModel getPdfDataBeforeImage() {
		return pdfDataBeforeImage;
	}

	public void setPdfDataBeforeImage(PdfDataModel pdfDataBeforeImage) {
		this.pdfDataBeforeImage = pdfDataBeforeImage;
	}

	public ArrayList<PdfAnagModel> getPdfAnags() {
		return pdfAnags;
	}

	public void setPdfAnags(ArrayList<PdfAnagModel> pdfAnags) {
		this.pdfAnags = pdfAnags;
	}

	public ArrayList<String> getSaveDateSott() {
		return saveDateSott;
	}

	public void setSaveDateSott(ArrayList<String> saveDateSott) {
		this.saveDateSott = saveDateSott;
	}

	public String getPdfTitle() {
		return pdfTitle;
	}

	public void setPdfTitle(String pdfTitle) {
		this.pdfTitle = pdfTitle;
	}

	public BooleanType getSkipCommandWarnings() {
		return skipCommandWarnings;
	}

	public void setSkipCommandWarnings(BooleanType skipCommandWarnings) {
		this.skipCommandWarnings = skipCommandWarnings;
	}

	public StringType getEventName() {
		return eventName;
	}

	public void setEventName(StringType eventName) {
		this.eventName = eventName;
	}

	public StringType getEventArgs() {
		return eventArgs;
	}

	public void setEventArgs(StringType eventArgs) {
		this.eventArgs = eventArgs;
	}

	public String[] getGlobalPageDriverJsScript() {
		return globalPageDriverJsScript;
	}

	public void setGlobalPageDriverJsScript(String[] globalPageDriverJsScript) {
		this.globalPageDriverJsScript = globalPageDriverJsScript;
	}

	public BooleanType getReloadPageOnEvent() {
		return reloadPageOnEvent;
	}

	public void setReloadPageOnEvent(BooleanType reloadPageOnEvent) {
		this.reloadPageOnEvent = reloadPageOnEvent;
	}

	public StringType getProfiloUtente() {
		return profiloUtente;
	}

	public void setProfiloUtente(StringType profiloUtente) {
		this.profiloUtente = profiloUtente;
	}

	public String getPdfGeneratedBarcodes() {
		return pdfGeneratedBarcodes;
	}

	public void setPdfGeneratedBarcodes(String pdfGeneratedBarcodes) {
		this.pdfGeneratedBarcodes = pdfGeneratedBarcodes;
	}

	public boolean isErrorOnSign() {
		return errorOnSign;
	}

	public void setErrorOnSign(boolean errorOnSign) {
		this.errorOnSign = errorOnSign;
	}

	public BooleanType getFlagWayout() {
		return flagWayout;
	}

	public void setFlagWayout(BooleanType flagWayout) {
		this.flagWayout = flagWayout;
	}

	public boolean isWayoutEnabled() {
		return wayoutEnabled;
	}

	public void setWayoutEnabled(boolean wayoutEnabled) {
		this.wayoutEnabled = wayoutEnabled;
	}

	public String getMessaggioPdfAlert() {
		return messaggioPdfAlert;
	}

	public void setMessaggioPdfAlert(String messaggioPdfAlert) {
		this.messaggioPdfAlert = messaggioPdfAlert;
	}

	public ListType getListaCodiciOperazionePritPerMultioperazione() {
		return listaCodiciOperazionePritPerMultioperazione;
	}

	public void setListaCodiciOperazionePritPerMultioperazione(
			ListType listaCodiciOperazionePritPerMultioperazione) {
		this.listaCodiciOperazionePritPerMultioperazione = listaCodiciOperazionePritPerMultioperazione;
	}

	public StringType getCodiciOperazionePritPerMultioperazione() {
		return codiciOperazionePritPerMultioperazione;
	}

	public void setCodiciOperazionePritPerMultioperazione(
			StringType codiciOperazionePritPerMultioperazione) {
		this.codiciOperazionePritPerMultioperazione = codiciOperazionePritPerMultioperazione;
	}
	
	public String getNacUrlDomain() {
		return nacUrlDomain;
	}

	public void setNacUrlDomain(String nacUrlDomain) {
		this.nacUrlDomain = nacUrlDomain;
	}
	
	public StringType getPdfNoteFBCopernico() {
		return pdfNoteFBCopernico;
	}

	public void setPdfNoteFBCopernico(StringType pdfNoteFBCopernico) {
		this.pdfNoteFBCopernico = pdfNoteFBCopernico;
	}

	public String[] getErroriAdeguatezza() {
		return erroriAdeguatezza;
	}

	public void setErroriAdeguatezza(String[] erroriAdeguatezza) {
		this.erroriAdeguatezza = erroriAdeguatezza;
	}

	public boolean isAdeguatezzaOnPreview() {
		return adeguatezzaOnPreview;
	}

	public void setAdeguatezzaOnPreview(boolean adeguatezzaOnPreview) {
		this.adeguatezzaOnPreview = adeguatezzaOnPreview;
	}

	public Map<String, String> getFieldsJsScript() {
		return fieldsJsScript;
	}

	public void setFieldsJsScripts(Map<String, String> fieldsJsScript) {
		this.fieldsJsScript = fieldsJsScript;
	}

	public StringType getAutocompleteFieldName() {
		return autocompleteFieldName;
	}

	public void setAutocompleteFieldName(StringType autocompleteFieldName) {
		this.autocompleteFieldName = autocompleteFieldName;
	}

	public StringType getAutocompleteTerm() {
		return autocompleteTerm;
	}

	public void setAutocompleteTerm(StringType autocompleteTerm) {
		this.autocompleteTerm = autocompleteTerm;
	}

	public MifidCallModel getMifidCallModel() {
		return mifidCallModel;
	}

	public void setMifidCallModel(MifidCallModel mifidCallModel) {
		this.mifidCallModel = mifidCallModel;
	}

	public StringType getNoElementsIndicator() {
		return noElementsIndicator;
	}

	public void setNoElementsIndicator(StringType noElementsIndicator) {
		this.noElementsIndicator = noElementsIndicator;
	}

	public ReportAdeguatezzaCallModel getReportAdeguatezzaCallModel() {
		return reportAdeguatezzaCallModel;
	}

	public void setReportAdeguatezzaCallModel(ReportAdeguatezzaCallModel reportAdeguatezzaCallModel) {
		this.reportAdeguatezzaCallModel = reportAdeguatezzaCallModel;
	}

	public RecuperaReportAdeguatezzaCallModel getRecuperaReportAdeguatezzaCallModel() {
		return recuperaReportAdeguatezzaCallModel;
	}

	public void setRecuperaReportAdeguatezzaCallModel(RecuperaReportAdeguatezzaCallModel recuperaReportAdeguatezzaCallModel) {
		this.recuperaReportAdeguatezzaCallModel = recuperaReportAdeguatezzaCallModel;
	}

	public boolean isFirstSignPage() {
		return firstSignPage;
	}

	public void setFirstSignPage(boolean firstSignPage) {
		this.firstSignPage = firstSignPage;
	}

	public boolean isReportAdeguatezzaClicked() {
		return reportAdeguatezzaClicked;
	}

	public void setReportAdeguatezzaClicked(boolean reportAdeguatezzaClicked) {
		this.reportAdeguatezzaClicked = reportAdeguatezzaClicked;
	}

	public String getXmlSrvSend() {
		return xmlSrvSend;
	}

	public void setXmlSrvSend(String xmlSrvSend) {
		this.xmlSrvSend = xmlSrvSend;
	}

	public String getXmlSrvReceived() {
		return xmlSrvReceived;
	}

	public void setXmlSrvReceived(String xmlSrvReceived) {
		this.xmlSrvReceived = xmlSrvReceived;
	}

	public Boolean getPutOnSignedProcessBatchQueue() {
		return putOnSignedProcessBatchQueue;
	}

	public void setPutOnSignedProcessBatchQueue(Boolean putOnSignedProcessBatchQueue) {
		this.putOnSignedProcessBatchQueue = putOnSignedProcessBatchQueue;
	}

	public Boolean getPutOnPostCompletionProcessBatchQueue() {
		return putOnPostCompletionProcessBatchQueue;
	}

	public void setPutOnPostCompletionProcessBatchQueue(Boolean putOnPostCompletionProcessBatchQueue) {
		this.putOnPostCompletionProcessBatchQueue = putOnPostCompletionProcessBatchQueue;
	}

	public Integer getMaxRecuperaReportAdeguatezzaRetryCount() {
		return maxRecuperaReportAdeguatezzaRetryCount;
	}

	public void setMaxRecuperaReportAdeguatezzaRetryCount(Integer maxRecuperaReportAdeguatezzaRetryCount) {
		this.maxRecuperaReportAdeguatezzaRetryCount = maxRecuperaReportAdeguatezzaRetryCount;
	}

	public String getSkipWarningCommandName() {
		return skipWarningCommandName;
	}

	public void setSkipWarningCommandName(String skipWarningCommandName) {
		this.skipWarningCommandName = skipWarningCommandName;
	}

	/**
	 * @deprecated vecchia integrazione con MOM. Con MOP rimpiazzato da {@link #isInValidazioneMOM()}
	 */
	@Deprecated
	public boolean isPdfOnMomValidation() {
		return pdfOnMomValidation;
	}

	public void setPdfOnMomValidation(boolean pdfOnMomValidation) {
		this.pdfOnMomValidation = pdfOnMomValidation;
	}

	public boolean isProcessoNonVirtuoso() {
		return processoNonVirtuoso;
	}

	public void setProcessoNonVirtuoso(boolean processoNonVirtuoso) {
		this.processoNonVirtuoso = processoNonVirtuoso;
	}

	public StringType getOriginalPdfInstanceId() {
		return originalPdfInstanceId;
	}

	public void setOriginalPdfInstanceId(StringType originalPdfInstanceId) {
		this.originalPdfInstanceId = originalPdfInstanceId;
	}
	
	public ListType getPdfAttachments() {
		return pdfAttachments;
	}

	public void setPdfAttachments(ListType pdfAttachments) {
		this.pdfAttachments = pdfAttachments;
	}

	public IntegerType getAttachIdx() {
		return attachIdx;
	}

	public void setAttachIdx(IntegerType attachIdx) {
		this.attachIdx = attachIdx;
	}

	public String getCodiceGuidDocumento() {
		return codiceGuidDocumento;
	}

	public void setCodiceGuidDocumento(String codiceGuidDocumento) {
		this.codiceGuidDocumento = codiceGuidDocumento;
	}

	public IddCallModel getIddCallModel() {
		return iddCallModel;
	}

	public void setIddCallModel(IddCallModel iddCallModel) {
		this.iddCallModel = iddCallModel;
	}

	public String getDataUltimaChiamataAdeguatezza() {
		return dataUltimaChiamataAdeguatezza;
	}

	public void setDataUltimaChiamataAdeguatezza(String dataUltimaChiamataAdeguatezza) {
		this.dataUltimaChiamataAdeguatezza = dataUltimaChiamataAdeguatezza;
	}

	public boolean isRaccomandazioneIddClicked() {
		return raccomandazioneIddClicked;
	}

	public void setRaccomandazioneIddClicked(boolean raccomandazioneIddClicked) {
		this.raccomandazioneIddClicked = raccomandazioneIddClicked;
	}

	public PritMomInfoModel getPritMomInfoModel() {
		return pritMomInfoModel;
	}

	public void setPritMomInfoModel(PritMomInfoModel pritMomInfoModel) {
		this.pritMomInfoModel = pritMomInfoModel;
	}

	public MomEventDataModel getMomEventData() {
		return momEventData;
	}

	public void setMomEventData(MomEventDataModel momEventData) {
		this.momEventData = momEventData;
	}

	public int getErrorOnSomePerson() {
		return errorOnSomePerson;
	}

	public void setErrorOnSomePerson(int errorOnSomePerson) {
		this.errorOnSomePerson = errorOnSomePerson;
	}

	public Map<String, PdfPersonModel> getPersonsCache() {
		return personsCache;
	}

	public void setPersonsCache(Map<String, PdfPersonModel> personsCache) {
		this.personsCache = personsCache;
	}

	public String getEndErrorTraceMsg() {
		return endErrorTraceMsg;
	}

	public void setEndErrorTraceMsg(String endErrorTraceMsg) {
		this.endErrorTraceMsg = endErrorTraceMsg;
	}

	public boolean isSomeDriverNotFount() {
		return someDriverNotFount;
	}

	public void setSomeDriverNotFount(boolean someDriverNotFount) {
		this.someDriverNotFount = someDriverNotFount;
	}

	public boolean isPdfOnValidation() {
		return pdfOnValidation;
	}

	public void setPdfOnValidation(boolean pdfOnValidation) {
		this.pdfOnValidation = pdfOnValidation;
	}

	public PdfValidationEventDataModel getPdfValidationEventData() {
		return pdfValidationEventData;
	}

	public void setPdfValidationEventData(PdfValidationEventDataModel validationEventData) {
		this.pdfValidationEventData = validationEventData;
	}

	public BooleanType getIsMifidSkipped() {
		return isMifidSkipped;
	}

	public void setIsMifidSkipped(BooleanType isMifidSkipped) {
		this.isMifidSkipped = isMifidSkipped;
	}

	public ArrayList<String> getSaveOreSott() {
		return saveOreSott;
	}

	public void setSaveOreSott(ArrayList<String> saveOreSott) {
		this.saveOreSott = saveOreSott;
	}

	public boolean isFirstDriverNotFound() {
		return firstDriverNotFound;
	}

	public void setFirstDriverNotFound(boolean firstDriverNotFound) {
		this.firstDriverNotFound = firstDriverNotFound;
	}

	public Basket getBasket() {
		return basket;
	}

	public void setBasket(Basket basket) {
		this.basket = basket;
	}

	public SostituzioniCallModel getSostituzioniCallModel() {
		return sostituzioniCallModel;
	}

	public void setSostituzioniCallModel(SostituzioniCallModel sostituzioniCallModel) {
		this.sostituzioniCallModel = sostituzioniCallModel;
	}

	public PreferenzeClienteSostituzioniModel getPreferenzeClienteSostituzioni() {
		return preferenzeClienteSostituzioni;
	}

	public void setPreferenzeClienteSostituzioni(PreferenzeClienteSostituzioniModel preferenzeClienteSostituzioni) {
		this.preferenzeClienteSostituzioni = preferenzeClienteSostituzioni;
	}
	public boolean isPrimoClienteIsCointestatarioPuro() {
		return primoClienteIsCointestatarioPuro;
	}

	public void setPrimoClienteIsCointestatarioPuro(boolean primoClienteIsCointestatarioPuro) {
		this.primoClienteIsCointestatarioPuro = primoClienteIsCointestatarioPuro;
	}

	public IntegerType getModelHashCode() {
		return modelHashCode;
	}

	public void setModelHashCode(IntegerType modelHashCode) {
		this.modelHashCode = modelHashCode;
	}

	public IntegerType getDataHashCode() {
		return dataHashCode;
	}

	public void setDataHashCode(IntegerType dataHashCode) {
		this.dataHashCode = dataHashCode;
	}

	public TimestampType getDataOraUltimaModifica() {
		return dataOraUltimaModifica;
	}

	public void setDataOraUltimaModifica(TimestampType dataOraUltimaModifica) {
		this.dataOraUltimaModifica = dataOraUltimaModifica;
	}

	public StringType getCodUtenteUltimaModifica() {
		return codUtenteUltimaModifica;
	}

	public void setCodUtenteUltimaModifica(StringType codUtenteUltimaModifica) {
		this.codUtenteUltimaModifica = codUtenteUltimaModifica;
	}

	public boolean isConcurrencyViolationFounded() {
		return concurrencyViolationFounded;
	}

	public void setConcurrencyViolationFounded(boolean concurrencyViolationFounded) {
		this.concurrencyViolationFounded = concurrencyViolationFounded;
	}

	public ListType getPdfInstanceAttachments() {
		return pdfInstanceAttachments;
	}

	public void setPdfInstanceAttachments(ListType pdfInstanceAttachments) {
		this.pdfInstanceAttachments = pdfInstanceAttachments;
	}
	
	public String getMessaggioPdfAmlAlert() {
		return messaggioPdfAmlAlert;
	}

	public void setMessaggioPdfAmlAlert(String messaggioPdfAmlAlert) {
		this.messaggioPdfAmlAlert = messaggioPdfAmlAlert;
	}

	public Map<String, Properties> getErrorsPropsMap() {
		return errorsPropsMap;
	}

	public void setErrorsPropsMap(Map<String, Properties> errorsPropsMap) {
		this.errorsPropsMap = errorsPropsMap;
	}

	public Map<String, Properties> getWarningsPropsMap() {
		return warningsPropsMap;
	}

	public void setWarningsPropsMap(Map<String, Properties> warningsPropsMap) {
		this.warningsPropsMap = warningsPropsMap;
	}

	public Map<String, Properties> getTypeErrorsPropsMap() {
		return typeErrorsPropsMap;
	}

	public void setTypeErrorsPropsMap(Map<String, Properties> typeErrorsPropsMap) {
		this.typeErrorsPropsMap = typeErrorsPropsMap;
	}

	public StringType getTipoProcessoSede() {
		return tipoProcessoSede;
	}

	public void setTipoProcessoSede(StringType tipoProcessoSede) {
		this.tipoProcessoSede = tipoProcessoSede;
	}

	public StringType getIsFirmaADistanza() {
		return isFirmaADistanza;
	}

	public void setIsFirmaADistanza(StringType isFirmaADistanza) {
		this.isFirmaADistanza = isFirmaADistanza;
	}
	
	public BooleanType getMaterialePrecontrattualeClicked() {
		return materialePrecontrattualeClicked;
	}

	public void setMaterialePrecontrattualeClicked(BooleanType materialePrecontrattualeClicked) {
		this.materialePrecontrattualeClicked = materialePrecontrattualeClicked;
	}

	public BooleanType getPresaVisioneMaterialePrecontrattuale() {
		return presaVisioneMaterialePrecontrattuale;
	}

	public void setPresaVisioneMaterialePrecontrattuale(BooleanType presaVisioneMaterialePrecontrattuale) {
		this.presaVisioneMaterialePrecontrattuale = presaVisioneMaterialePrecontrattuale;
	}

	public List<PdfModel> getPdfOriginali() {
		return pdfOriginali;
	}

	public void setPdfOriginali(List<PdfModel> pdfOriginali) {
		this.pdfOriginali = pdfOriginali;
	}

	public ListType getPdfInstanceOriginalAttachments() {
		return pdfInstanceOriginalAttachments;
	}

	public void setPdfInstanceOriginalAttachments(ListType pdfInstanceOriginalAttachments) {
		this.pdfInstanceOriginalAttachments = pdfInstanceOriginalAttachments;
	}

	public Map<String, String> getErrorsCodes() {
		return errorsCodes;
	}

	public void setErrorsCodes(Map<String, String> errorsCodes) {
		this.errorsCodes = errorsCodes;
	}
	
	public Map<String, String> getWarningsCodes() {
		return warningsCodes;
	}

	public void setWarningsCodes(Map<String, String> warningsCodes) {
		this.warningsCodes = warningsCodes;
	}

	public List<String> getPreloadedPersonFieldsEditableIfNull() {
		return preloadedPersonFieldsEditableIfNull;
	}

	public void setPreloadedPersonFieldsEditableIfNull(List<String> preloadedPersonFieldsEditableIfNull) {
		this.preloadedPersonFieldsEditableIfNull = preloadedPersonFieldsEditableIfNull;
	}

	public AmlModel getAmlModel() {
		return amlModel;
	}

	public void setAmlModel(AmlModel amlModel) {
		this.amlModel = amlModel;
	}

	public CoraModel getCoraModel() {
		return coraModel;
	}

	public void setCoraModel(CoraModel coraModel) {
		this.coraModel = coraModel;
	}

	public String getCodiceModuloAmlHidden() {
		return codiceModuloAmlHidden;
	}

	public void setCodiceModuloAmlHidden(String codiceModuloAmlHidden) {
		this.codiceModuloAmlHidden = codiceModuloAmlHidden;
	}

	public String getMessaggioCopernico() {
		return messaggioCopernico;
	}

	public String getLastSpeakOnPageLoad() {
		return lastSpeakOnPageLoad;
	}

	public void setLastSpeakOnPageLoad(String lastSpeakOnPageLoad) {
		this.lastSpeakOnPageLoad = lastSpeakOnPageLoad;
	}

	public MifidManlevaDataModel getMifidManlevaData() {
		return mifidManlevaData;
	}

	public void setMifidManlevaData(MifidManlevaDataModel mifidManlevaData) {
		this.mifidManlevaData = mifidManlevaData;
	}

	public BooleanType getMaterialePrecontrattualeAccessorioClicked() {
		return materialePrecontrattualeAccessorioClicked;
	}

	public void setMaterialePrecontrattualeAccessorioClicked(BooleanType materialePrecontrattualeAccessorioClicked) {
		this.materialePrecontrattualeAccessorioClicked = materialePrecontrattualeAccessorioClicked;
	}
	
	public String getTitoloFineOperazione() {
		return titoloFineOperazione;
	}

	public void setTitoloFineOperazione(String titoloFineOperazione) {
		this.titoloFineOperazione = titoloFineOperazione;
	}

	public boolean isGestioneLegaleRappresentanteAttivaGiaCaricata() {
		return gestioneLegaleRappresentanteAttivaGiaCaricata;
	}

	public void setGestioneLegaleRappresentanteAttivaGiaCaricata(boolean gestioneLegaleRappresentanteAttivaGiaCaricata) {
		this.gestioneLegaleRappresentanteAttivaGiaCaricata = gestioneLegaleRappresentanteAttivaGiaCaricata;
	}

	public boolean isGestioneLegaleRappresentanteAttiva() {
		return gestioneLegaleRappresentanteAttiva;
	}

	public void setGestioneLegaleRappresentanteAttiva(boolean gestioneLegaleRappresentanteAttiva) {
		this.gestioneLegaleRappresentanteAttiva = gestioneLegaleRappresentanteAttiva;
	}

	public FlagControlloTargetMarketBackupBean getFlagControlloTargetMarketBackup() {
		return flagControlloTargetMarketBackup;
	}

	public void setFlagControlloTargetMarketBackup(FlagControlloTargetMarketBackupBean flagControlloTargetMarketBackup) {
		this.flagControlloTargetMarketBackup = flagControlloTargetMarketBackup;
	}

}
