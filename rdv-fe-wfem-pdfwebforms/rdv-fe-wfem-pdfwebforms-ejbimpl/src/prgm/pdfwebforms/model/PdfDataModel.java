package prgm.pdfwebforms.model;

import java.util.ArrayList;
import java.util.Arrays;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.AbstractTypePropertyDescriptor;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.core.PdfInfos;
import prgm.pdfwebforms.core.PdfPredefinedFields;
import prgm.pdfwebforms.drivers.PdfBaseDriver;
import prgm.pdfwebforms.drivers.PdfBasePageDriver;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfDataModel extends MapCommandDataModel {
	
	public static String ENVIRONMENT_CATALOGO_MODULI = "CATALOGO_MODULI";
	public static String ENVIRONMENT_CATALOGO_OPERAZIONI = "CATALOGO_OPERAZIONI";

	public static String PRIIPS_SUPPORTO_CARTACEO = "CARTACEO";
	public static String PRIIPS_SUPPORTO_NON_CARTACEO = "NONCARTACEO";
	
	private IntegerType pdfIndex = new IntegerType(0);
	
	//Transient input data *******************************************************
	private StringType  	gobackLabel = new StringType();
	private StringType  	gobackUrl = new StringType();
	private BooleanType		gobackUrlOnTop = new BooleanType();
	private StringType  	gobackEndLabel = new StringType();
	private StringType  	gobackEndUrl = new StringType();
	private BooleanType		gobackEndUrlOnTop = new BooleanType();
	private BooleanType		skipDataentry = new BooleanType();
	private BooleanType		isVolatile = new BooleanType();
	private BooleanType		isOnSameStack = new BooleanType();
	private StringType 		doMultipleCopiesOnPrintPdf = new StringType();
	private StringType 		facSimileLabelOnPrintPdf = new StringType();
	private BooleanType		replaceExternalPdfInstance = new BooleanType();
	private StringType  	callerSelectedCompilationMode = new StringType();
	private BooleanType		readonlyDataentry = new BooleanType();
	private StringType  	processDriverReference = new StringType();
	
	//Input data *******************************************************
	// On process 
	private StringType  	pdfId = new StringType();
	private StringType  	pdfInstanceId = new StringType();
	private StringType  	pdfEnvironment = new StringType();
	private StringType  	pdfTitle = new StringType();
	private BooleanType 	isSwitch = new BooleanType();
	private IntegerType 	idCarrello = new IntegerType();
	private StringType 		numAgevolazione = new StringType();
	private StringType 		codAgevolazione = new StringType();
	private StringType  	externalEntityAppl = new StringType();
	private StringType  	externalEntityName = new StringType();
	private StringType  	externalEntityKey = new StringType();
	private StringType  	idReportAdeguatezza = new StringType();
	private StringType  	idSostituzione = new StringType();	
	private StringType  	numOrdineReportAdeguatezza = new StringType();
	private StringType  	idRaccomandazioneIdd = new StringType();
	private StringType  	priipsTipoSupportoMaterialeContrattuale = new StringType();
	private StringType  	priipsOrizzonteTemporale = new StringType();
	private StringType  	priipsTolleranzaVolatilita = new StringType();
	private BooleanType 	facSimileOnPreview = new BooleanType();
	private BooleanType  	hideSaveButton = new BooleanType();
	private StringType  	inviaInSedeButtonLabel = new StringType();
	private StringType  	firmaDigitaleButtonLabel = new StringType();
	private StringType  	copernicoButtonLabel = new StringType();
	private StringType  	codAgeImpersonato = new StringType();
	private PdfPersonModel  agenteImpersonato = null;					// Impostato solo sul pdfData principale
	private StringType  	codRuoloImpersonato = new StringType();		//Impostato solo sul pdfData principale

	//Input data *******************************************************
	// On single pdf 
	private StringType  	pdfCode = new StringType();
	private StringType  	pdfMomCode = new StringType();
	private IntegerType 	idDispCarrello = new IntegerType();
	private IntegerType 	idDispModuloCarrello = new IntegerType(); // Modulo carrello semplificato (semplt)

	// Firma del legale rappresentante
	private IntegerType		indiceLegaleRappresentante = new IntegerType();
	private BooleanType  	esistonoFirmeDelLegaleRappresentante = new BooleanType();
	private StringType  	codiceLegaleRappresentante = new StringType();
	
	// On basket process 
	private IntegerType		ordineCompilazioneBasket = new IntegerType();
	private StringType  	idAdeguatezzaMifidPadre = new StringType();
	private StringType  	flagManlevaMifidKOESG = new StringType();
	// On process (modifiable via driver)
	private StringType  	compilationModes = new StringType();
	// On single pdf (modifiable via driver)
	private BooleanType		signAll = new BooleanType();
	private StringType  	fieldsToRemove = new StringType();
	private StringType  	editableFields = new StringType();
	private StringType  	hidedFields = new StringType();
	private StringType  	uneditableFields = new StringType();
	private StringType  	extraMandatoryFields = new StringType();
	private StringType  	visiblePages = new StringType();
	private ExternalLinkOnSignDataModel externalLinkOnSignData = null;
	// *****************************************************************
	private ListType		pdfs = new ListType(PdfDataModel.class);
	// *****************************************************************
	
	// Technical data
	private StringType  	inputCompilationModes = new StringType();
	private StringType  	pdfInitialInputData = new StringType();

	private IntegerType 	pdfPublicationId = new IntegerType();
	private StringType  	pdfBarcode = new StringType();
	private IntegerType		pdfNumPages = new IntegerType();
	private BooleanType		isInitCalled = null;
	private boolean			jsInitToCall = false;
	private boolean 		publicationIdChanged = false;
	
	private StringType  				momVersion = new StringType();
	private IntegerType 				acroformVersion = new IntegerType();
	private StringType 					pdfApplReferenceCode = null;
	private transient PdfBaseDriver		pdfDriver = null;
	private transient PdfBasePageDriver	pdfPageDriver = null;
	private boolean						onlyPrint = false;
	private ArrayList<String> 			pdfInitialInputDataArray = new ArrayList<String>();
	private PdfPersonModel 				agente = new PdfPersonModel();
	private ArrayList<PdfPersonModel>  	clienti = new ArrayList<PdfPersonModel>();
	private PdfInfos 					pdfInfos;
	private IntegerType					pdfNumCopie = new IntegerType();
	private String  					pdfTestoCopia1 = "";
	private String  					pdfTestoCopia2 = "";
	private String  					pdfTestoCopia3 = "";
	private String  					pdfTestoCopia4 = "";
	private CommandDataModel			inputDataModel = null;
	private MapCommandDataModel			callbackData = null;
	private StringType					profiloUtente = new StringType();
	private String 						pdfStatus = new String(PdfInstanceModel.STATO_BOZZA);

	private boolean 					initialInputArrayIsOn = false;
	private Agevolazione				agevolazioneBackup = null;

	// MOM data
	private boolean			operatoreMOM = false;
	private StringType  	codDispositivaBMED = new StringType();  // Chiave in input per MOM e per tutti i pdf integrati con il processo dispositivo
	private StringType		idPraticaMOM = new StringType();		// Chiave in input per MOM. Concorre all'impostazione delle "externalKey" e poi non serve più
	private BooleanType		readonly = new BooleanType();			// Imposta a readonly la pagina di dataentry per MOM
	private StringType		confrontaDoppiaSpunta = new StringType();// Per doppia spunta. S=Solo confronto. V=Confronto con visualizzazione differenze
	private StringType  	coraFb = new StringType();
	
	// NMOL data
	private StringType  	sistemaClient = new StringType();
	private boolean			processoAccettazioneCopernico = false;

	// Clone data
	private String 			originalPdfInstanceId = "";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfDataModel(){
		addCodDescField("priipsOrizzonteTemporale",   "PRIIPS_ORIZZONTE_TEMPORALE");
		addCodDescField("priipsTolleranzaVolatilita", "PRIIPS_TOLLERANZA_VOLATILITA");
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void initSinglePdf(PdfModel pdf) throws Exception{
		setInputCompilationModes(new StringType(getCompilationModes().toString()));
		setCompilationModes(new StringType());
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void initMultiplePdf(PdfModel pdf) throws Exception{
		for(int i=0;i<getPdfs().size();i++){
			PdfDataModel pdfDataElement = (PdfDataModel)getPdfs().get(i);
			pdfDataElement.setInputCompilationModes(new StringType(getCompilationModes().toString()));
			pdfDataElement.setPdfEnvironment(new StringType(getPdfEnvironment().toString()));
			pdfDataElement.setSignAll(new BooleanType(getSignAll().booleanValue()));
			pdfDataElement.setPdfIndex(new IntegerType(i));
			pdfDataElement.setPdfInstanceId(new StringType());
			
			pdfDataElement.setIdCarrello(new IntegerType(getIdCarrello().toString()));
			pdfDataElement.setIsSwitch(new BooleanType(getIsSwitch().booleanValue()));
			
			pdfDataElement.setNumAgevolazione(new StringType(getNumAgevolazione().toString()));
			pdfDataElement.setCodAgevolazione(new StringType(getCodAgevolazione().toString()));
			pdfDataElement.setIdReportAdeguatezza(new StringType(getIdReportAdeguatezza().toString()));
			pdfDataElement.setIdSostituzione(new StringType(getIdSostituzione().toString()));
			pdfDataElement.setNumOrdineReportAdeguatezza(new StringType(getNumOrdineReportAdeguatezza().toString()));
			pdfDataElement.setIdRaccomandazioneIdd(new StringType(getIdRaccomandazioneIdd().toString()));

			pdfDataElement.setCodAgeImpersonato(new StringType(getCodAgeImpersonato().toString()));
			pdfDataElement.setIdPraticaMOM(new StringType(getIdPraticaMOM().toString()));

			pdfDataElement.setIdPCA(new StringType(getIdPCA().toString()));
			pdfDataElement.setIdQLTM(new StringType(getIdQLTM().toString()));
		}
		setPdfCode(new StringType());
		setPdfMomCode(new StringType());
		setInputCompilationModes(new StringType(getCompilationModes().toString()));
		setCompilationModes(new StringType());
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void initDataFromPdfs(int pdfIndex) throws Exception{
		
		if(getPdfs().size() == 0)
			return;
		
		PdfDataModel pdfsData = (PdfDataModel)getPdfs().get(pdfIndex);
		
		setPriipsTipoSupportoMaterialeContrattuale(pdfsData.getPriipsTipoSupportoMaterialeContrattuale());
		setPriipsOrizzonteTemporale(pdfsData.getPriipsOrizzonteTemporale());
		setPriipsTolleranzaVolatilita(pdfsData.getPriipsTolleranzaVolatilita());
		
		setInputDataModel(pdfsData.getInputDataModel());
		setVisiblePages(pdfsData.getVisiblePages());
		setPdfDriver(pdfsData.getPdfDriver());
		setPdfPageDriver(pdfsData.getPdfPageDriver());
		
		setPdfId(pdfsData.getPdfId());
		setPdfCode(pdfsData.getPdfCode());
		setPdfMomCode(pdfsData.getPdfMomCode());
		setIdDispCarrello(pdfsData.getIdDispCarrello());
		setIdDispModuloCarrello(pdfsData.getIdDispModuloCarrello());
		setPdfBarcode(pdfsData.getPdfBarcode());
		setPdfIndex(pdfsData.getPdfIndex());
		setPdfInitialInputDataArray(pdfsData.getPdfInitialInputDataArray());
		setPdfPublicationId(pdfsData.getPdfPublicationId());
		setAgente(pdfsData.getAgente());
		setClienti(pdfsData.getClienti());
		setPdfInfos(pdfsData.getPdfInfos());
		
		setPdfNumCopie(pdfsData.getPdfNumCopie());
		setPdfTestoCopia1(pdfsData.getPdfTestoCopia1());
		setPdfTestoCopia2(pdfsData.getPdfTestoCopia2());
		setPdfTestoCopia3(pdfsData.getPdfTestoCopia3());
		setPdfTestoCopia4(pdfsData.getPdfTestoCopia4());

		setSignAll(pdfsData.getSignAll());
		setAcroformVersion(pdfsData.getAcroformVersion());
		setFieldsToRemove(pdfsData.getFieldsToRemove());
		setEditableFields(pdfsData.getEditableFields());
		setUneditableFields(pdfsData.getUneditableFields());
		setHidedFields(pdfsData.getHidedFields());
		setExtraMandatoryFields(pdfsData.getExtraMandatoryFields());
		
		setIndiceLegaleRappresentante(pdfsData.getIndiceLegaleRappresentante());
		setEsistonoFirmeDelLegaleRappresentante(pdfsData.getEsistonoFirmeDelLegaleRappresentante());
		
		setIsInitCalled(pdfsData.getIsInitCalled());
		
		setExternalLinkOnSignData(pdfsData.getExternalLinkOnSignData());
		setCoraFb(pdfsData.getCoraFb());

		clearMappedProperties();
		AbstractTypePropertyDescriptor[] pds = pdfsData.getMappedPropertyDescriptors();
		for(int k=0; k < pds.length; k++){
			AbstractTypePropertyDescriptor pd = pds[k];
			addProperty(pd.getName(), pd.getValue());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void initPdfsFromData() throws Exception{
		if(getPdfs().size() == 0)
			return;
		
		PdfDataModel pdfsData = (PdfDataModel)getPdfs().get(getPdfIndex().intValue());
		
		pdfsData.setPriipsTipoSupportoMaterialeContrattuale(getPriipsTipoSupportoMaterialeContrattuale());
		pdfsData.setPriipsOrizzonteTemporale(getPriipsOrizzonteTemporale());
		pdfsData.setPriipsTolleranzaVolatilita(getPriipsTolleranzaVolatilita());
		
		pdfsData.setVisiblePages(getVisiblePages());
		pdfsData.setPdfDriver(getPdfDriver());
		pdfsData.setPdfPageDriver(getPdfPageDriver());
		
		pdfsData.setPdfPublicationId(getPdfPublicationId());
		pdfsData.setPdfInitialInputDataArray(getPdfInitialInputDataArray());
		pdfsData.setAgente(getAgente());
		pdfsData.setClienti(getClienti());
		pdfsData.setPdfInfos(getPdfInfos());
		
		pdfsData.setEditableFields(getEditableFields());
		pdfsData.setUneditableFields(getUneditableFields());
		pdfsData.setHidedFields(getHidedFields());
		
		pdfsData.setIsInitCalled(getIsInitCalled());
		
		pdfsData.setExternalLinkOnSignData(getExternalLinkOnSignData());
		pdfsData.setCoraFb(getCoraFb());
		
		pdfsData.clearMappedProperties();
		AbstractTypePropertyDescriptor[] pds = getMappedPropertyDescriptors();
		for(int k=0; k < pds.length; k++){
			AbstractTypePropertyDescriptor pd = pds[k];
			pdfsData.addProperty(pd.getName(), pd.getValue());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void initPdfAnag(PdfAnagModel pdfAnag) throws Exception{
		setPdfMomCode(new StringType(pdfAnag.getPdfMomCode().toString()));
		setPdfNumPages(new IntegerType(pdfAnag.getPdfNumPages().intValue()));
		setPdfNumCopie(pdfAnag.getPdfNumCopie());
		setPdfTestoCopia1(pdfAnag.getPdfTestoCopia1().toString());
		setPdfTestoCopia2(pdfAnag.getPdfTestoCopia2().toString());
		setPdfTestoCopia3(pdfAnag.getPdfTestoCopia3().toString());
		setPdfTestoCopia4(pdfAnag.getPdfTestoCopia4().toString());
		return;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void initPdfInitialInputDataStringFromArray(){
		setPdfInitialInputData(new StringType());
		StringBuffer initialInputData = new StringBuffer();
		for(String s : getPdfInitialInputDataArray())
			initialInputData.append(s+",");
		if(initialInputData.length() > 0)
			setPdfInitialInputData(new StringType(initialInputData.substring(0, initialInputData.length()-1)));
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void initPdfInitialInputDataArrayFromString(){
		getPdfInitialInputDataArray().clear();
		if(getPdfInitialInputData().isNull())
			return;
		String[] pdfInitialInputDataArray = getPdfInitialInputData().toString().split("\\,");
		for(int i=0;i<pdfInitialInputDataArray.length;i++)
			getPdfInitialInputDataArray().add(pdfInitialInputDataArray[i]);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public AbstractType read(String fieldName){
		return readProperty(fieldName);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String readAsString(String fieldName){
		return readProperty(fieldName) == null ? "" : readProperty(fieldName).toString();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void write(String fieldName, AbstractType fieldValue){
		addProperty(fieldName, fieldValue);
		if(initialInputArrayIsOn && !getPdfInitialInputDataArray().contains(fieldName))
			getPdfInitialInputDataArray().add(fieldName);
		return;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void copyFrom(String fieldName, PdfDataModel otherPdfData){
		AbstractType p = otherPdfData.read(fieldName);
		if(p != null)
			addProperty(fieldName, AbstractType.newInstance(p.getClass(), p.toString()));
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void clearPdfInitialInputDataInitialization(){
		getPdfInitialInputDataArray().clear();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void startPdfInitialInputDataInitialization(){
		initialInputArrayIsOn = true;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void stopPdfInitialInputDataInitialization(){
		initialInputArrayIsOn = false;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void backupAgevolazione(){
		addProperty("isDerogaSostituzione", new BooleanType(true));
		agevolazioneBackup = new Agevolazione(	new StringType(getNumAgevolazione().toString()), new StringType(getCodAgevolazione().toString()),
												read(PdfPredefinedFields.TIPO_AGEVOLAZIONE), 
												read(PdfPredefinedFields.ID_AGEVOLAZIONE), 
												read(PdfPredefinedFields.CODICE_AGEVOLAZIONE), 
												read(PdfPredefinedFields.DESCR_AGEVOLAZIONE),
												read(PdfPredefinedFields.PERCENTUALE_AGEVOLAZIONE),
												read(PdfPredefinedFields.TIPOLOGIA_AGEVOLAZIONE),
												read(PdfPredefinedFields.MOD_VERSAMENTO_AGEVOLAZIONE),
												read(PdfPredefinedFields.IMPORTO_AGEVOLAZIONE));
		setNumAgevolazione(new StringType());
		setCodAgevolazione(new StringType());
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void restoreAgevolazione(){
		if(agevolazioneBackup == null)
			return;
		
		setNumAgevolazione(new StringType(agevolazioneBackup.getNumAgevolazione().toString()));
		setCodAgevolazione(new StringType(agevolazioneBackup.getCodAgevolazione().toString()));
	
		addProperty("isDerogaSostituzione", null);
		addProperty(PdfPredefinedFields.TIPO_AGEVOLAZIONE, 				agevolazioneBackup.getTipo());
		addProperty(PdfPredefinedFields.ID_AGEVOLAZIONE, 				agevolazioneBackup.getId());
		addProperty(PdfPredefinedFields.CODICE_AGEVOLAZIONE,			agevolazioneBackup.getCodice());
		addProperty(PdfPredefinedFields.DESCR_AGEVOLAZIONE, 			agevolazioneBackup.getDescr());
		addProperty(PdfPredefinedFields.PERCENTUALE_AGEVOLAZIONE, 		agevolazioneBackup.getPerc());
		addProperty(PdfPredefinedFields.TIPOLOGIA_AGEVOLAZIONE, 		agevolazioneBackup.getTipologia());
		addProperty(PdfPredefinedFields.MOD_VERSAMENTO_AGEVOLAZIONE, 	agevolazioneBackup.getModalitaVersamento());
		addProperty(PdfPredefinedFields.IMPORTO_AGEVOLAZIONE, 			agevolazioneBackup.getImporto());
		agevolazioneBackup = null;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfPersonModel getPerson(int personIdx){
		try{
			return getClienti().get(personIdx-1);
		}catch(Throwable t){
			return null;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfPersonModel getPerson(String codCli){
		for(PdfPersonModel p : getClienti()) {
			if(p.getNdg().equals(codCli) || p.getIdCensimento().equals(codCli))
				return p;
		}
		return null;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isFieldHided(String fieldName) {
   		if(getHidedFields().isNull())
   			return false;
		ArrayList<String> hidedFieldsAsArray = new ArrayList<String>(Arrays.asList(getHidedFields().toString().split("\\,")));
		return hidedFieldsAsArray.contains(fieldName);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean fieldExist(String fieldName) {
		return getPdfInfos() != null && getPdfInfos().findFieldInfoByPdfName(fieldName) != null;
	}
	
	/***********************************************************************************************/
	// RFC #292576: Id questionario light Target Market. Passato dal 5D o generato dai driver
	// Li maschero con gli accessor ma sono nei dati dinamici
	// Il campo "idQLTM" è anche in acroform
	/***********************************************************************************************/
	public StringType getIdPCA() {
		return new StringType(readAsString(PdfPredefinedFields.ID_PCA));
	}
	public void setIdPCA(StringType idPCA) {
		this.addProperty(PdfPredefinedFields.ID_PCA, idPCA);
	}
	public StringType getIdQLTM() {
		return new StringType(readAsString(PdfPredefinedFields.ID_QLTM));
	}
	public void setIdQLTM(StringType idQLTM) {
		this.addProperty(PdfPredefinedFields.ID_QLTM, idQLTM);
	}
	
	public StringType getPdfCode() {
		return pdfCode;
	}

	public void setPdfCode(StringType pdfCode) {
		this.pdfCode = pdfCode;
	}

	public StringType getPdfInitialInputData() {
		return pdfInitialInputData;
	}

	public void setPdfInitialInputData(StringType pdfInitialInputData) {
		this.pdfInitialInputData = pdfInitialInputData;
	}

	public IntegerType getPdfPublicationId() {
		return pdfPublicationId;
	}

	public void setPdfPublicationId(IntegerType pdfPublicationId) {
		this.pdfPublicationId = pdfPublicationId;
	}

	public StringType getPdfBarcode() {
		return pdfBarcode;
	}

	public void setPdfBarcode(StringType pdfBarcode) {
		this.pdfBarcode = pdfBarcode;
	}

	public StringType getCompilationModes() {
		return compilationModes;
	}

	public void setCompilationModes(StringType compilationModes) {
		this.compilationModes = compilationModes;
	}

	public StringType getPdfInstanceId() {
		return pdfInstanceId;
	}

	public void setPdfInstanceId(StringType pdfInstanceId) {
		this.pdfInstanceId = pdfInstanceId;
	}

	public StringType getPdfId() {
		return pdfId;
	}

	public void setPdfId(StringType pdfId) {
		this.pdfId = pdfId;
	}

	public StringType getPdfEnvironment() {
		return pdfEnvironment;
	}

	public void setPdfEnvironment(StringType pdfEnvironment) {
		this.pdfEnvironment = pdfEnvironment;
	}

	public StringType getGobackLabel() {
		return gobackLabel;
	}

	public void setGobackLabel(StringType gobackLabel) {
		this.gobackLabel = gobackLabel;
	}

	public StringType getGobackEndLabel() {
		return gobackEndLabel;
	}

	public void setGobackEndLabel(StringType gobackEndLabel) {
		this.gobackEndLabel = gobackEndLabel;
	}

	public StringType getGobackEndUrl() {
		return gobackEndUrl;
	}

	public void setGobackEndUrl(StringType gobackEndUrl) {
		this.gobackEndUrl = gobackEndUrl;
	}

	public StringType getGobackUrl() {
		return gobackUrl;
	}

	public void setGobackUrl(StringType gobackUrl) {
		this.gobackUrl = gobackUrl;
	}

	public ListType getPdfs() {
		return pdfs;
	}

	public void setPdfs(ListType pdfs) {
		this.pdfs = pdfs;
	}

	public IntegerType getPdfIndex() {
		return pdfIndex;
	}

	public void setPdfIndex(IntegerType pdfIndex) {
		this.pdfIndex = pdfIndex;
	}

	public ArrayList<String> getPdfInitialInputDataArray() {
		return pdfInitialInputDataArray;
	}

	public void setPdfInitialInputDataArray(
			ArrayList<String> pdfInitialInputDataArray) {
		this.pdfInitialInputDataArray = pdfInitialInputDataArray;
	}

	public ArrayList<PdfPersonModel> getClienti() {
		return clienti;
	}

	public void setClienti(ArrayList<PdfPersonModel> clienti) {
		this.clienti = clienti;
	}

	public PdfPersonModel getAgente() {
		return agente;
	}

	public void setAgente(PdfPersonModel agente) {
		this.agente = agente;
	}

	public PdfInfos getPdfInfos() {
		return pdfInfos;
	}

	public void setPdfInfos(PdfInfos pdfInfos) {
		this.pdfInfos = pdfInfos;
	}

	public BooleanType getSkipDataentry() {
		return skipDataentry;
	}

	public void setSkipDataentry(BooleanType skipDataentry) {
		this.skipDataentry = skipDataentry;
	}

	public BooleanType getIsVolatile() {
		return isVolatile;
	}

	public void setIsVolatile(BooleanType isVolatile) {
		this.isVolatile = isVolatile;
	}

	public boolean isOnlyPrint() {
		return onlyPrint;
	}

	public void setOnlyPrint(boolean onlyPrint) {
		this.onlyPrint = onlyPrint;
	}

	public IntegerType getPdfNumPages() {
		return pdfNumPages;
	}

	public void setPdfNumPages(IntegerType pdfNumPages) {
		this.pdfNumPages = pdfNumPages;
	}

	public MapCommandDataModel getCallbackData() {
		return callbackData;
	}

	public void setCallbackData(MapCommandDataModel callbackData) {
		this.callbackData = callbackData;
	}

	public StringType getFieldsToRemove() {
		return fieldsToRemove;
	}

	public void setFieldsToRemove(StringType fieldsToRemove) {
		this.fieldsToRemove = fieldsToRemove;
	}

	public BooleanType getSignAll() {
		return signAll;
	}

	public void setSignAll(BooleanType signAll) {
		this.signAll = signAll;
	}

	public StringType getEditableFields() {
		return editableFields;
	}

	public void setEditableFields(StringType editableFields) {
		this.editableFields = editableFields;
	}

	public StringType getUneditableFields() {
		return uneditableFields;
	}

	public void setUneditableFields(StringType uneditableFields) {
		this.uneditableFields = uneditableFields;
	}

	public PdfBaseDriver getPdfDriver() {
		return pdfDriver;
	}

	public void setPdfDriver(PdfBaseDriver pdfDriver) {
		this.pdfDriver = pdfDriver;
	}

	public IntegerType getAcroformVersion() {
		return acroformVersion;
	}

	public void setAcroformVersion(IntegerType acroformVersion) {
		this.acroformVersion = acroformVersion;
	}

	public BooleanType getIsInitCalled() {
		return isInitCalled;
	}

	public void setIsInitCalled(BooleanType isInitCalled) {
		this.isInitCalled = isInitCalled;
	}

	public StringType getPdfApplReferenceCode() {
		return pdfApplReferenceCode;
	}

	public void setPdfApplReferenceCode(StringType pdfApplReferenceCode) {
		this.pdfApplReferenceCode = pdfApplReferenceCode;
	}

	public StringType getInputCompilationModes() {
		return inputCompilationModes;
	}

	public void setInputCompilationModes(StringType inputCompilationModes) {
		this.inputCompilationModes = inputCompilationModes;
	}

	public CommandDataModel getInputDataModel() {
		return inputDataModel;
	}

	public void setInputDataModel(CommandDataModel inputDataModel) {
		this.inputDataModel = inputDataModel;
	}

	public BooleanType getIsOnSameStack() {
		return isOnSameStack;
	}

	public void setIsOnSameStack(BooleanType isOnSameStack) {
		this.isOnSameStack = isOnSameStack;
	}

	public IntegerType getPdfNumCopie() {
		return pdfNumCopie;
	}

	public void setPdfNumCopie(IntegerType pdfNumCopie) {
		this.pdfNumCopie = pdfNumCopie;
	}

	public String getPdfTestoCopia1() {
		return pdfTestoCopia1;
	}

	public void setPdfTestoCopia1(String pdfTestoCopia1) {
		this.pdfTestoCopia1 = pdfTestoCopia1;
	}

	public String getPdfTestoCopia2() {
		return pdfTestoCopia2;
	}

	public void setPdfTestoCopia2(String pdfTestoCopia2) {
		this.pdfTestoCopia2 = pdfTestoCopia2;
	}

	public String getPdfTestoCopia3() {
		return pdfTestoCopia3;
	}

	public void setPdfTestoCopia3(String pdfTestoCopia3) {
		this.pdfTestoCopia3 = pdfTestoCopia3;
	}

	public String getPdfTestoCopia4() {
		return pdfTestoCopia4;
	}

	public void setPdfTestoCopia4(String pdfTestoCopia4) {
		this.pdfTestoCopia4 = pdfTestoCopia4;
	}

	public StringType getPdfTitle() {
		return pdfTitle;
	}

	public void setPdfTitle(StringType pdfTitle) {
		this.pdfTitle = pdfTitle;
	}

	public PdfBasePageDriver getPdfPageDriver() {
		return pdfPageDriver;
	}

	public void setPdfPageDriver(PdfBasePageDriver pdfPageDriver) {
		this.pdfPageDriver = pdfPageDriver;
	}

	public StringType getProfiloUtente() {
		return profiloUtente;
	}

	public void setProfiloUtente(StringType profiloUtente) {
		this.profiloUtente = profiloUtente;
	}

	public BooleanType getIsSwitch() {
		return isSwitch;
	}

	public void setIsSwitch(BooleanType isSwitch) {
		this.isSwitch = isSwitch;
	}

	public IntegerType getIdCarrello() {
		return idCarrello;
	}

	public void setIdCarrello(IntegerType idCarrello) {
		this.idCarrello = idCarrello;
	}

	public IntegerType getIdDispCarrello() {
		return idDispCarrello;
	}

	public void setIdDispCarrello(IntegerType idDispCarrello) {
		this.idDispCarrello = idDispCarrello;
	}

	public StringType getExtraMandatoryFields() {
		return extraMandatoryFields;
	}

	public void setExtraMandatoryFields(StringType extraMandatoryFields) {
		this.extraMandatoryFields = extraMandatoryFields;
	}

	public StringType getExternalEntityAppl() {
		return externalEntityAppl;
	}

	public void setExternalEntityAppl(StringType externalEntityAppl) {
		this.externalEntityAppl = externalEntityAppl;
	}

	public StringType getExternalEntityName() {
		return externalEntityName;
	}

	public void setExternalEntityName(StringType externalEntityName) {
		this.externalEntityName = externalEntityName;
	}

	public StringType getExternalEntityKey() {
		return externalEntityKey;
	}

	public void setExternalEntityKey(StringType externalEntityKey) {
		this.externalEntityKey = externalEntityKey;
	}

	public StringType getPdfMomCode() {
		return pdfMomCode;
	}

	public void setPdfMomCode(StringType pdfMomCode) {
		this.pdfMomCode = pdfMomCode;
	}

	public StringType getPriipsTipoSupportoMaterialeContrattuale() {
		return priipsTipoSupportoMaterialeContrattuale;
	}

	public void setPriipsTipoSupportoMaterialeContrattuale(StringType priipsTipoSupportoMaterialeContrattuale) {
		this.priipsTipoSupportoMaterialeContrattuale = priipsTipoSupportoMaterialeContrattuale;
	}

	public StringType getIdReportAdeguatezza() {
		return idReportAdeguatezza;
	}

	public void setIdReportAdeguatezza(StringType idReportAdeguatezza) {
		this.idReportAdeguatezza = idReportAdeguatezza;
	}

	public String getPdfStatus() {
		return pdfStatus;
	}

	public void setPdfStatus(String pdfStatus) {
		this.pdfStatus = pdfStatus;
	}

	public BooleanType getFacSimileOnPreview() {
		return facSimileOnPreview;
	}

	public void setFacSimileOnPreview(BooleanType facSimileOnPreview) {
		this.facSimileOnPreview = facSimileOnPreview;
	}

	public StringType getIdPraticaMOM() {
		return idPraticaMOM;
	}

	public void setIdPraticaMOM(StringType idPraticaMOM) {
		this.idPraticaMOM = idPraticaMOM;
	}

	public StringType getCodDispositivaBMED() {
		return codDispositivaBMED;
	}

	public void setCodDispositivaBMED(StringType codDispositivaBMED) {
		this.codDispositivaBMED = codDispositivaBMED;
	}

	public String getOriginalPdfInstanceId() {
		return originalPdfInstanceId;
	}

	public void setOriginalPdfInstanceId(String originalPdfInstanceId) {
		this.originalPdfInstanceId = originalPdfInstanceId;
	}

	public boolean isJsInitToCall() {
		return jsInitToCall;
	}

	public void setJsInitToCall(boolean jsInitToCall) {
		this.jsInitToCall = jsInitToCall;
	}

	public StringType getNumAgevolazione() {
		return numAgevolazione;
	}

	public void setNumAgevolazione(StringType numAgevolazione) {
		this.numAgevolazione = numAgevolazione;
	}

	public BooleanType getGobackEndUrlOnTop() {
		return gobackEndUrlOnTop;
	}

	public void setGobackEndUrlOnTop(BooleanType gobackEndUrlOnTop) {
		this.gobackEndUrlOnTop = gobackEndUrlOnTop;
	}

	public StringType getIdRaccomandazioneIdd() {
		return idRaccomandazioneIdd;
	}

	public void setIdRaccomandazioneIdd(StringType idRaccomandazioneIdd) {
		this.idRaccomandazioneIdd = idRaccomandazioneIdd;
	}

	public BooleanType getHideSaveButton() {
		return hideSaveButton;
	}

	public void setHideSaveButton(BooleanType hideSaveButton) {
		this.hideSaveButton = hideSaveButton;
	}

	public StringType getVisiblePages() {
		return visiblePages;
	}

	public void setVisiblePages(StringType visiblePages) {
		this.visiblePages = visiblePages;
	}

	public StringType getMomVersion() {
		return momVersion;
	}

	public void setMomVersion(StringType momVersion) {
		this.momVersion = momVersion;
	}

	public boolean isOperatoreMOM() {
		return operatoreMOM;
	}

	public void setOperatoreMOM(boolean operatoreMOM) {
		this.operatoreMOM = operatoreMOM;
	}

	public boolean isProcessoAccettazioneCopernico() {
		return processoAccettazioneCopernico;
	}

	public void setProcessoAccettazioneCopernico(boolean processoAccettazioneCopernico) {
		this.processoAccettazioneCopernico = processoAccettazioneCopernico;
	}

	public StringType getCodAgevolazione() {
		return codAgevolazione;
	}

	public void setCodAgevolazione(StringType codAgevolazione) {
		this.codAgevolazione = codAgevolazione;
	}

	public StringType getNumOrdineReportAdeguatezza() {
		return numOrdineReportAdeguatezza;
	}

	public void setNumOrdineReportAdeguatezza(StringType numOrdineReportAdeguatezza) {
		this.numOrdineReportAdeguatezza = numOrdineReportAdeguatezza;
	}
	
	public StringType getInviaInSedeButtonLabel() {
		return inviaInSedeButtonLabel;
	}

	public void setInviaInSedeButtonLabel(StringType inviaInSedeButtonLabel) {
		this.inviaInSedeButtonLabel = inviaInSedeButtonLabel;
	}

	public StringType getFirmaDigitaleButtonLabel() {
		return firmaDigitaleButtonLabel;
	}

	public void setFirmaDigitaleButtonLabel(StringType firmaDigitaleButtonLabel) {
		this.firmaDigitaleButtonLabel = firmaDigitaleButtonLabel;
	}

	public StringType getCopernicoButtonLabel() {
		return copernicoButtonLabel;
	}

	public void setCopernicoButtonLabel(StringType copernicoButtonLabel) {
		this.copernicoButtonLabel = copernicoButtonLabel;
	}

	public StringType getCodAgeImpersonato() {
		return codAgeImpersonato;
	}

	public void setCodAgeImpersonato(StringType codAgeImpersonato) {
		this.codAgeImpersonato = codAgeImpersonato;
	}

	public PdfPersonModel getAgenteImpersonato() {
		return agenteImpersonato;
	}

	public void setAgenteImpersonato(PdfPersonModel agenteImpersonato) {
		this.agenteImpersonato = agenteImpersonato;
	}

	public StringType getPriipsOrizzonteTemporale() {
		return priipsOrizzonteTemporale;
	}

	public void setPriipsOrizzonteTemporale(StringType priipsOrizzonteTemporale) {
		this.priipsOrizzonteTemporale = priipsOrizzonteTemporale;
	}

	public StringType getPriipsTolleranzaVolatilita() {
		return priipsTolleranzaVolatilita;
	}

	public void setPriipsTolleranzaVolatilita(StringType priipsTolleranzaVolatilita) {
		this.priipsTolleranzaVolatilita = priipsTolleranzaVolatilita;
	}

	public ExternalLinkOnSignDataModel getExternalLinkOnSignData() {
		return externalLinkOnSignData;
	}

	public void setExternalLinkOnSignData(ExternalLinkOnSignDataModel externalLinkOnSignData) {
		this.externalLinkOnSignData = externalLinkOnSignData;
	}

	public IntegerType getOrdineCompilazioneBasket() {
		return ordineCompilazioneBasket;
	}

	public void setOrdineCompilazioneBasket(IntegerType ordineCompilazioneBasket) {
		this.ordineCompilazioneBasket = ordineCompilazioneBasket;
	}

	public StringType getDoMultipleCopiesOnPrintPdf() {
		return doMultipleCopiesOnPrintPdf;
	}

	public void setDoMultipleCopiesOnPrintPdf(StringType doMultipleCopiesOnPrintPdf) {
		this.doMultipleCopiesOnPrintPdf = doMultipleCopiesOnPrintPdf;
	}

	public StringType getFacSimileLabelOnPrintPdf() {
		return facSimileLabelOnPrintPdf;
	}

	public void setFacSimileLabelOnPrintPdf(StringType facSimileLabelOnPrintPdf) {
		this.facSimileLabelOnPrintPdf = facSimileLabelOnPrintPdf;
	}

	public StringType getIdSostituzione() {
		return idSostituzione;
	}

	public void setIdSostituzione(StringType idSostituzione) {
		this.idSostituzione = idSostituzione;
	}

	public StringType getSistemaClient() {
		return sistemaClient;
	}

	public void setSistemaClient(StringType sistemaClient) {
		this.sistemaClient = sistemaClient;
	}

	public StringType getCodRuoloImpersonato() {
		return codRuoloImpersonato;
	}

	public void setCodRuoloImpersonato(StringType codRuoloImpersonato) {
		this.codRuoloImpersonato = codRuoloImpersonato;
	}

	public BooleanType getReadonly() {
		return readonly;
	}

	public void setReadonly(BooleanType readonly) {
		this.readonly = readonly;
	}

	public BooleanType getReplaceExternalPdfInstance() {
		return replaceExternalPdfInstance;
	}

	public void setReplaceExternalPdfInstance(BooleanType replaceExternalPdfInstance) {
		this.replaceExternalPdfInstance = replaceExternalPdfInstance;
	}

	public StringType getConfrontaDoppiaSpunta() {
		return confrontaDoppiaSpunta;
	}

	public void setConfrontaDoppiaSpunta(StringType confrontaDoppiaSpunta) {
		this.confrontaDoppiaSpunta = confrontaDoppiaSpunta;
	}

	public boolean isPublicationIdChanged() {
		return publicationIdChanged;
	}

	public void setPublicationIdChanged(boolean publicationIdChanged) {
		this.publicationIdChanged = publicationIdChanged;
	}

	public StringType getHidedFields() {
		return hidedFields;
	}

	public void setHidedFields(StringType hidedFields) {
		this.hidedFields = hidedFields;
	}

	public StringType getCoraFb() {
		return coraFb;
	}

	public void setCoraFb(StringType coraFb) {
		this.coraFb = coraFb;
	}

	public IntegerType getIdDispModuloCarrello() {
		return idDispModuloCarrello;
	}

	public void setIdDispModuloCarrello(IntegerType idDispModuloCarrello) {
		this.idDispModuloCarrello = idDispModuloCarrello;
	}
	
	public StringType getCallerSelectedCompilationMode() {
		return callerSelectedCompilationMode;
	}

	public void setCallerSelectedCompilationMode(StringType callerSelectedCompilationMode) {
		this.callerSelectedCompilationMode = callerSelectedCompilationMode;
	}

	public StringType getIdAdeguatezzaMifidPadre() {
		return idAdeguatezzaMifidPadre;
	}

	public void setIdAdeguatezzaMifidPadre(StringType idAdeguatezzaMifidPadre) {
		this.idAdeguatezzaMifidPadre = idAdeguatezzaMifidPadre;
	}

	public StringType getFlagManlevaMifidKOESG() {
		return flagManlevaMifidKOESG;
	}

	public void setFlagManlevaMifidKOESG(StringType flagManlevaMifidKOESG) {
		this.flagManlevaMifidKOESG = flagManlevaMifidKOESG;
	}

	public BooleanType getReadonlyDataentry() {
		return readonlyDataentry;
	}

	public void setReadonlyDataentry(BooleanType readonlyDataentry) {
		this.readonlyDataentry = readonlyDataentry;
	}

	public BooleanType getGobackUrlOnTop() {
		return gobackUrlOnTop;
	}

	public void setGobackUrlOnTop(BooleanType gobackUrlOnTop) {
		this.gobackUrlOnTop = gobackUrlOnTop;
	}

	public StringType getProcessDriverReference() {
		return processDriverReference;
	}

	public void setProcessDriverReference(StringType processDriverReference) {
		this.processDriverReference = processDriverReference;
	}

	public IntegerType getIndiceLegaleRappresentante() {
		return indiceLegaleRappresentante;
	}

	public void setIndiceLegaleRappresentante(IntegerType indiceLegaleRappresentante) {
		this.indiceLegaleRappresentante = indiceLegaleRappresentante;
	}

	public BooleanType getEsistonoFirmeDelLegaleRappresentante() {
		return esistonoFirmeDelLegaleRappresentante;
	}

	public void setEsistonoFirmeDelLegaleRappresentante(BooleanType esistonoFirmeDelLegaleRappresentante) {
		this.esistonoFirmeDelLegaleRappresentante = esistonoFirmeDelLegaleRappresentante;
	}

	public StringType getCodiceLegaleRappresentante() {
		return codiceLegaleRappresentante;
	}

	public void setCodiceLegaleRappresentante(StringType codiceLegaleRappresentante) {
		this.codiceLegaleRappresentante = codiceLegaleRappresentante;
	}
}
