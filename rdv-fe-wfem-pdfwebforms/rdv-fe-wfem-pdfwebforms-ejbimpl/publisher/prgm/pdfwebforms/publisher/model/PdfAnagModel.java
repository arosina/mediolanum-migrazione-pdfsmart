package prgm.pdfwebforms.publisher.model;

import java.util.ArrayList;

import com.atosorigin.wfem.coddesc.CodDescData;
import com.atosorigin.wfem.coddesc.CodDescDataList;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.FileType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.catalog.PdfCatalogModel;

/*******************************************************************/
/*******************************************************************/
public class PdfAnagModel extends PdfAnagKeyModel {
	
	public static String TIPO_PRIIPS_SEMPLICE 	= "S";
	public static String TIPO_PRIIPS_PREVIDENZA = "P";
	
	private BooleanType				isFromCatalogoModuli = new BooleanType(false);
	
	private StringType  			pdfOriginalCode = new StringType(); // Per l'univocità
	private IntegerType 			pdfOriginalPubId = new IntegerType(NO_PUBLICATION);
	
	// Dati MODULO (CEPE_CATM_MODULO)
	private PdfModuloModel			pdfModulo = new PdfModuloModel();

	// Dati in anagrafica generale (PDF_ANAG)
	private StringType  			pdfDescr = new StringType();
	private IntegerType				pdfCodProdottoPrit = new IntegerType();
	private IntegerType				pdfCodOperazionePrit = new IntegerType();
	private BooleanType				pdfIsInvioInSedeEnabled = new BooleanType(); // libera+chimica per catalogo
	private BooleanType				pdfIsStampaEnabled = new BooleanType();
	private BooleanType				pdfIsCartaLiberaEnabled = new BooleanType();
	private BooleanType				pdfIsCartaChimicaEnabled = new BooleanType();
	private BooleanType				pdfIsFirmaDigitaleEnabled = new BooleanType();
	private BooleanType				pdfIsCopernicoEnabled = new BooleanType();
	private BooleanType				downloadAsFacsimile = new BooleanType();
	private IntegerType				pdfNumCopie = new IntegerType(1);
	private StringType  			pdfTestoCopia1 = new StringType();
	private StringType  			pdfTestoCopia2 = new StringType();
	private StringType  			pdfTestoCopia3 = new StringType();
	private StringType  			pdfTestoCopia4 = new StringType();
	private BooleanType				signAll = new BooleanType();
	private StringType  			pdfArea = new StringType();
	private DateType				pdfStartDate = new DateType();
	private DateType				pdfEndDate = new DateType();
	private StringType 				pdfDriverName = new StringType();
	private StringType 				pdfDriverVersion = new StringType();
	private BooleanType				sendFBMailOnSign = new BooleanType();
	private BooleanType				sendCliSmsOnSign = new BooleanType();
	private StringType 				cliSmsOnSignText = new StringType();
	private StringType 				externalLinkOnSignLabel = new StringType();
	private StringType 				externalLinkOnSignUrl = new StringType();
	private StringType 				externalLinkAggOnSignLabel = new StringType();
	private StringType 				externalLinkAggOnSignUrl = new StringType();
	private BooleanType				isControlliCompleti = new BooleanType();
	private StringType				tipoPriips = new StringType(); // "S" -> semplice "P" -> previdenza. Il campo a db è PDF_ANAG.HAS_PRIIPS in quanto nasce come boooleano
	private BooleanType 			hasPreferenzeInPriips = new BooleanType();
	private BooleanType 			onStockProcessBatchQueue = new BooleanType();
	private BooleanType 			facSimileOnPreview = new BooleanType();
	private StringType 				callSrvDispositivaBMED = new StringType(); // "S"-> sempre  "D"-> solo se in digitale (fd/copernico)
	private StringType 				tipoProcessoSede = new StringType(); // "null"-> MOM (default)
	private StringType 				tipoProcessoSedeInPdfAnag = new StringType(); // MOP: per capire se il pdf è MOM2 ma versione ante MOP (Amex, Wealth, PCA etc.)
	private BooleanType				hasDataSottoscrizioneOggi = new BooleanType();
	private StringType  			noteDiConfigurazioneInInput = new StringType();
	private StringType  			noteDiConfigurazione = new StringType();
	private StringType  			noteFbCopernico = new StringType();
	private StringType 				pdfCodLineaBusiness = new StringType();
	private StringType 				pdfCodFaseCommerciale = new StringType();
	private ListType				pdfElencoRuoliUtilizzatori = new ListType(PdfRuoloUtilizzatoreModel.class);
	private	ListType				pdfApplReferences = null;
	private BooleanType				hasControlloAmlAVR = new BooleanType();
	private BooleanType				hasControlloAmlPEP = new BooleanType();
	private BooleanType				hasControlloSCAI = new BooleanType();
	private BooleanType				pdfHasLayerCollocamentoADistanza = new BooleanType();
	private StringType 				societa = new StringType();
	
	// Dati di pubblicazione
	private DateType				pdfPubStartDate = new DateType();
	private DateType				pdfDataFineAccettazionePubblPrec = new DateType();
	private StringType 				pdfEdition = new StringType();
	
	private IntegerType 			pdfAcroformVersion = new IntegerType(1);
	
	private StringType 				pdfCreationUser = new StringType();
	private TimestampType			pdfCreationTime = new TimestampType();
	private StringType 				pdfLastModUser = new StringType();
	private TimestampType			pdfLastModTime = new TimestampType();
	private StringType 				pdfPublishUser = new StringType();
	private TimestampType			pdfPublishTime = new TimestampType();

	private FileType				pdfContent = new FileType();
	private ListType				pdfPages = new ListType(PdfPageAnagModel.class);
	private IntegerType				pdfNumPages = new IntegerType();
	private IntegerType				pdfNumPubblicazioni = new IntegerType();
	private IntegerType				pdfNumArchiviazioni = new IntegerType();
	private IntegerType				pdfPubIdCorrente = new IntegerType();
	private StringType 				pdfFileName = new StringType();
	
	private FileType				pdfCompilationExample = new FileType();
	private StringType 				pdfCompilationExampleFileName = new StringType();
	private StringType 				pdfCompilationExampleFileType = new StringType();
	
	private BooleanType				isProspectEnabledOnCli1 = new BooleanType();
	private BooleanType				isProspectEnabledOnCli2 = new BooleanType();
	private BooleanType				isProspectEnabledOnCli3 = new BooleanType();
	private BooleanType				isProspectEnabledOnCli4 = new BooleanType();
	private BooleanType				isProspectEnabledOnCli5 = new BooleanType();
	private BooleanType				isProspectEnabledOnCli6 = new BooleanType();
	private BooleanType				isProspectEnabledOnCli7 = new BooleanType();
	private BooleanType				isProspectEnabledOnCli8 = new BooleanType();
	private BooleanType				isProspectEnabledOnCli9 = new BooleanType();
	
	private StringType 				pdfValidationWarningMessage = new StringType();
	private StringType 				pdfPublicationNotes = new StringType();
	private IntegerType				pdfNumSignFields = new IntegerType();

	private StringType 				filenetClasseDocCliente = new StringType();
	private StringType 				filenetCodiceDocCliente = new StringType();

	// Dati di comodo
	private BooleanType					isOnWork = new BooleanType();
	private BooleanType					fileIsChanged = new BooleanType();
	private BooleanType					generatePageImages = new BooleanType(true);
	private FileType					pageImageContent = new FileType();
	private IntegerType					pageImageNum = new IntegerType();
	private ArrayList<PdfPageAnagModel>	pagesToSave = null;
	private BooleanType					pdfHasPublicationNotes = new BooleanType();
	private BooleanType					pdfHasValidationWarningMessage = new BooleanType();
	private String					  	pritBarcode = null;
	private boolean						venditaComeBmed = false;
	private BooleanType					skipCallToAggiornaModuloMom = new BooleanType();
	
	// Dati di crafter
	private BooleanType					isTipoSoggettoPersonaFisica = new BooleanType();
	private BooleanType					isTipoSoggettoProfessional = new BooleanType();
	private BooleanType					isTipoSoggettoPersonaGiuridica = new BooleanType();
	private BooleanType					isTipoSoggettoFamilyBanker = new BooleanType();
	private BooleanType					isUsableInCrafterWayout = new BooleanType();
	private BooleanType					existInCartaChimica = new BooleanType();

	/*******************************************************************/
	/*******************************************************************/
	public PdfAnagModel(){
		getPdfContent().setFileTypes("pdf");
		getPageImageContent().setFileTypes("jpg,jpeg");
		addCodDescField("pdfArea","Aree");
		addCodDescField("pdfCodProdottoPrit","ProdottiPrit");
		addCodDescField("pdfCodOperazionePrit","OperazioniProdottoPrit");
		addCodDescField("pdfCodLineaBusiness","LineeBusiness");
		addCodDescField("pdfCodFaseCommerciale","FasiCommerciali");
		
		CodDescDataList dl = new CodDescDataList();
		CodDescData d = null;
		d = new CodDescData(); d.setCod("1"); d.setDescr(d.getCod()); dl.addCodDescData(d);
		d = new CodDescData(); d.setCod("2"); d.setDescr(d.getCod()); dl.addCodDescData(d);
		d = new CodDescData(); d.setCod("3"); d.setDescr(d.getCod()); dl.addCodDescData(d);
		d = new CodDescData(); d.setCod("4"); d.setDescr(d.getCod()); dl.addCodDescData(d);
		addCodDescField("pdfNumCopie",dl);
	}
	
	/*******************************************************************/
	/*******************************************************************/
	public PdfAnagModel(PdfAnagKeyModel key){
		this();
		setPdfId(new StringType(key.getPdfId().toString()));
		setPdfCode(new StringType(key.getPdfCode().toString()));
		setPdfPublicationId(new IntegerType(key.getPdfPublicationId().intValue()));
	}
	
	/*******************************************************************/
	/*******************************************************************/
	public boolean isPdfPassato(){
		return Tools.today().compareTo(getPdfPubStartDate()) >= 0 ? true : false;
	}
	
	/*******************************************************************/
	/*******************************************************************/
	public String getTitle(){
		String title = getPdfCode().toString();
		if(!getPdfEdition().isNull())
			title = title + " Edizione "+getPdfEdition();
		return title;
	}
	
	/*******************************************************************/
	/*******************************************************************/
	public String getTitleAsFileName(){
		String title = getTitle();
		title.replaceAll("[\\\\/:*?\"<>|]","-");
		if(!title.toUpperCase().endsWith(".PDF"))
			title += ".pdf";
		return title;
	}

	/*******************************************************************/
	/*******************************************************************/
	public String htmlApplReferences(){
		if(pdfApplReferences == null || pdfApplReferences.size() == 0){
			return "&nbsp;";
		}
		if(pdfApplReferences.size() == 1){
			PdfApplReferenceModel r = (PdfApplReferenceModel)pdfApplReferences.get(0);
			return r.getApplDescr().toString();
		}
		StringBuffer res = new StringBuffer();
		for(int i=0;i<pdfApplReferences.size();i++){
			PdfApplReferenceModel r = (PdfApplReferenceModel)pdfApplReferences.get(i);
			res.append(r.getApplDescr());
			if(i<pdfApplReferences.size()-1)
				res.append(",&nbsp;");
		}
		return res.toString();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void initFlagsFromInvioInSede(){
		// Gestione "invio in sede" abilitato come gruppo (in scrittura)
		if(getIsFromCatalogoModuli().booleanValue() || getPdfArea().equals(PdfCatalogModel.AREA_CATALOGO_OPERAZIONI)){
			setPdfIsCartaLiberaEnabled(new BooleanType(false));
			setPdfIsCartaChimicaEnabled(new BooleanType(false));
			if(getPdfIsInvioInSedeEnabled().booleanValue()){
				setPdfIsCartaLiberaEnabled(new BooleanType(true));
				setPdfIsCartaChimicaEnabled(new BooleanType(true));
			}
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void initInvioInSedeFromFlags(){
		// Gestione "invio in sede" abilitato come gruppo (in lettura)
		if(getIsFromCatalogoModuli().booleanValue() || getPdfArea().equals(PdfCatalogModel.AREA_CATALOGO_OPERAZIONI)){
			setPdfIsInvioInSedeEnabled(new BooleanType(false));
			if(getPdfIsCartaLiberaEnabled().booleanValue() &&
			   getPdfIsCartaChimicaEnabled().booleanValue()){
				setPdfIsInvioInSedeEnabled(new BooleanType(true));
			}
		}
	}

	/*******************************************************************/
	/*******************************************************************/
	public void initNoteConfigurazione(ClientSessionContext csc) {
		if(getNoteDiConfigurazioneInInput().isNull())
			return;
		String noteCorrenti = getNoteDiConfigurazione().toString();
		if(noteCorrenti.length() > 0)
			noteCorrenti = "\n\n"+noteCorrenti;
		setNoteDiConfigurazione(new StringType(csc.getUserCode()+" - "+Tools.now()+"\n"+getNoteDiConfigurazioneInInput()+noteCorrenti));
		setNoteDiConfigurazioneInInput(new StringType());
	}
	
	/*******************************************************************/
	/*******************************************************************/
	public BooleanType getHasPriips() {
		return getTipoPriips().isNull() && !getHasPreferenzeInPriips().booleanValue() ? new BooleanType(false) : new BooleanType(true);
	}

	/*******************************************************************/
	/* Per compatibilità con il pregresso rispetto all'introduzione    */
	/* del priips previdenza                                           */
	/* Questo metodo è deprecato, utilizzare "setTipoPriips"           */ 
	/*******************************************************************/
	@Deprecated
	public void setHasPriips(BooleanType hasPriips) {
		if(hasPriips.booleanValue())
			setTipoPriips(new StringType(TIPO_PRIIPS_SEMPLICE));
		else
			setTipoPriips(new StringType());
	}

	public StringType getPdfDescr() {
		return pdfDescr;
	}

	public void setPdfDescr(StringType pdfDescr) {
		this.pdfDescr = pdfDescr;
	}

	public TimestampType getPdfPublishTime() {
		return pdfPublishTime;
	}

	public void setPdfPublishTime(TimestampType pdfPublishTime) {
		this.pdfPublishTime = pdfPublishTime;
	}

	public StringType getPdfCreationUser() {
		return pdfCreationUser;
	}

	public void setPdfCreationUser(StringType pdfCreationUser) {
		this.pdfCreationUser = pdfCreationUser;
	}

	public TimestampType getPdfCreationTime() {
		return pdfCreationTime;
	}

	public void setPdfCreationTime(TimestampType pdfCreationTime) {
		this.pdfCreationTime = pdfCreationTime;
	}

	public StringType getPdfLastModUser() {
		return pdfLastModUser;
	}

	public void setPdfLastModUser(StringType pdfLastModUser) {
		this.pdfLastModUser = pdfLastModUser;
	}

	public TimestampType getPdfLastModTime() {
		return pdfLastModTime;
	}

	public void setPdfLastModTime(TimestampType pdfLastModTime) {
		this.pdfLastModTime = pdfLastModTime;
	}

	public FileType getPdfContent() {
		return pdfContent;
	}

	public void setPdfContent(FileType pdfContent) {
		this.pdfContent = pdfContent;
	}

	public ListType getPdfPages() {
		return pdfPages;
	}

	public void setPdfPages(ListType pdfPages) {
		this.pdfPages = pdfPages;
	}

	public DateType getPdfPubStartDate() {
		return pdfPubStartDate;
	}

	public void setPdfPubStartDate(DateType pdfPubStartDate) {
		this.pdfPubStartDate = pdfPubStartDate;
	}

	public IntegerType getPdfNumPages() {
		return pdfNumPages;
	}

	public void setPdfNumPages(IntegerType pdfNumPages) {
		this.pdfNumPages = pdfNumPages;
	}

	public StringType getPdfOriginalCode() {
		return pdfOriginalCode;
	}

	public void setPdfOriginalCode(StringType pdfOriginalCode) {
		this.pdfOriginalCode = pdfOriginalCode;
	}

	public StringType getPdfPublishUser() {
		return pdfPublishUser;
	}

	public void setPdfPublishUser(StringType pdfPublishUser) {
		this.pdfPublishUser = pdfPublishUser;
	}

	public StringType getPdfEdition() {
		return pdfEdition;
	}

	public void setPdfEdition(StringType pdfEdition) {
		this.pdfEdition = pdfEdition;
	}

	public IntegerType getPdfPubIdCorrente() {
		return pdfPubIdCorrente;
	}

	public void setPdfPubIdCorrente(IntegerType pdfPubIdCorrente) {
		this.pdfPubIdCorrente = pdfPubIdCorrente;
	}

	public IntegerType getPdfNumPubblicazioni() {
		return pdfNumPubblicazioni;
	}

	public void setPdfNumPubblicazioni(IntegerType pdfNumPubblicazioni) {
		this.pdfNumPubblicazioni = pdfNumPubblicazioni;
	}

	public IntegerType getPdfOriginalPubId() {
		return pdfOriginalPubId;
	}

	public void setPdfOriginalPubId(IntegerType pdfOriginalPubId) {
		this.pdfOriginalPubId = pdfOriginalPubId;
	}

	public StringType getPdfFileName() {
		return pdfFileName;
	}

	public void setPdfFileName(StringType pdfFileName) {
		this.pdfFileName = pdfFileName;
	}

	public IntegerType getPdfNumArchiviazioni() {
		return pdfNumArchiviazioni;
	}

	public void setPdfNumArchiviazioni(IntegerType pdfNumArchiviazioni) {
		this.pdfNumArchiviazioni = pdfNumArchiviazioni;
	}

	public IntegerType getPdfCodProdottoPrit() {
		return pdfCodProdottoPrit;
	}

	public void setPdfCodProdottoPrit(IntegerType pdfCodProdottoPrit) {
		this.pdfCodProdottoPrit = pdfCodProdottoPrit;
	}

	public IntegerType getPdfCodOperazionePrit() {
		return pdfCodOperazionePrit;
	}

	public void setPdfCodOperazionePrit(IntegerType pdfCodOperazionePrit) {
		this.pdfCodOperazionePrit = pdfCodOperazionePrit;
	}

	public BooleanType getIsOnWork() {
		return isOnWork;
	}

	public void setIsOnWork(BooleanType isOnWork) {
		this.isOnWork = isOnWork;
	}

	public BooleanType getPdfIsCartaLiberaEnabled() {
		return pdfIsCartaLiberaEnabled;
	}

	public void setPdfIsCartaLiberaEnabled(BooleanType pdfIsCartaLiberaEnabled) {
		this.pdfIsCartaLiberaEnabled = pdfIsCartaLiberaEnabled;
	}

	public BooleanType getPdfIsCartaChimicaEnabled() {
		return pdfIsCartaChimicaEnabled;
	}

	public void setPdfIsCartaChimicaEnabled(BooleanType pdfIsCartaChimicaEnabled) {
		this.pdfIsCartaChimicaEnabled = pdfIsCartaChimicaEnabled;
	}

	public BooleanType getPdfIsFirmaDigitaleEnabled() {
		return pdfIsFirmaDigitaleEnabled;
	}

	public void setPdfIsFirmaDigitaleEnabled(BooleanType pdfIsFirmaDigitaleEnabled) {
		this.pdfIsFirmaDigitaleEnabled = pdfIsFirmaDigitaleEnabled;
	}

	public BooleanType getPdfIsStampaEnabled() {
		return pdfIsStampaEnabled;
	}

	public void setPdfIsStampaEnabled(BooleanType pdfIsStampaEnabled) {
		this.pdfIsStampaEnabled = pdfIsStampaEnabled;
	}

	public PdfModuloModel getPdfModulo() {
		return pdfModulo;
	}

	public void setPdfModulo(PdfModuloModel pdfModulo) {
		this.pdfModulo = pdfModulo;
	}

	public BooleanType getIsFromCatalogoModuli() {
		return isFromCatalogoModuli;
	}

	public void setIsFromCatalogoModuli(BooleanType isFromCatalogoModuli) {
		this.isFromCatalogoModuli = isFromCatalogoModuli;
	}

	public BooleanType getFileIsChanged() {
		return fileIsChanged;
	}

	public void setFileIsChanged(BooleanType fileIsChanged) {
		this.fileIsChanged = fileIsChanged;
	}

	public BooleanType getGeneratePageImages() {
		return generatePageImages;
	}

	public void setGeneratePageImages(BooleanType generatePageImages) {
		this.generatePageImages = generatePageImages;
	}

	public FileType getPageImageContent() {
		return pageImageContent;
	}

	public void setPageImageContent(FileType pageImageContent) {
		this.pageImageContent = pageImageContent;
	}

	public IntegerType getPageImageNum() {
		return pageImageNum;
	}

	public void setPageImageNum(IntegerType pageImageNum) {
		this.pageImageNum = pageImageNum;
	}

	public ArrayList<PdfPageAnagModel> getPagesToSave() {
		return pagesToSave;
	}

	public void setPagesToSave(ArrayList<PdfPageAnagModel> pagesToSave) {
		this.pagesToSave = pagesToSave;
	}

	public FileType getPdfCompilationExample() {
		return pdfCompilationExample;
	}

	public void setPdfCompilationExample(FileType pdfCompilationExample) {
		this.pdfCompilationExample = pdfCompilationExample;
	}

	public StringType getPdfCompilationExampleFileName() {
		return pdfCompilationExampleFileName;
	}

	public void setPdfCompilationExampleFileName(
			StringType pdfCompilationExampleFileName) {
		this.pdfCompilationExampleFileName = pdfCompilationExampleFileName;
	}

	public StringType getPdfCompilationExampleFileType() {
		return pdfCompilationExampleFileType;
	}

	public void setPdfCompilationExampleFileType(
			StringType pdfCompilationExampleFileType) {
		this.pdfCompilationExampleFileType = pdfCompilationExampleFileType;
	}

	public IntegerType getPdfAcroformVersion() {
		return pdfAcroformVersion;
	}

	public void setPdfAcroformVersion(IntegerType pdfAcroformVersion) {
		this.pdfAcroformVersion = pdfAcroformVersion;
	}

	public BooleanType getSignAll() {
		return signAll;
	}

	public void setSignAll(BooleanType signAll) {
		this.signAll = signAll;
	}

	public StringType getPdfArea() {
		return pdfArea;
	}

	public void setPdfArea(StringType pdfArea) {
		this.pdfArea = pdfArea;
	}

	public DateType getPdfEndDate() {
		return pdfEndDate;
	}

	public void setPdfEndDate(DateType pdfEndDate) {
		this.pdfEndDate = pdfEndDate;
	}

	public DateType getPdfStartDate() {
		return pdfStartDate;
	}

	public void setPdfStartDate(DateType pdfStartDate) {
		this.pdfStartDate = pdfStartDate;
	}

	public StringType getPdfDriverName() {
		return pdfDriverName;
	}

	public void setPdfDriverName(StringType pdfDriverName) {
		this.pdfDriverName = pdfDriverName;
	}

	public StringType getPdfDriverVersion() {
		return pdfDriverVersion;
	}

	public void setPdfDriverVersion(StringType pdfDriverVersion) {
		this.pdfDriverVersion = pdfDriverVersion;
	}

	public StringType getPdfValidationWarningMessage() {
		return pdfValidationWarningMessage;
	}

	public void setPdfValidationWarningMessage(
			StringType pdfValidationWarningMessage) {
		this.pdfValidationWarningMessage = pdfValidationWarningMessage;
	}

	public BooleanType getSendFBMailOnSign() {
		return sendFBMailOnSign;
	}

	public void setSendFBMailOnSign(BooleanType sendFBMailOnSign) {
		this.sendFBMailOnSign = sendFBMailOnSign;
	}

	public BooleanType getSendCliSmsOnSign() {
		return sendCliSmsOnSign;
	}

	public void setSendCliSmsOnSign(BooleanType sendCliSmsOnSign) {
		this.sendCliSmsOnSign = sendCliSmsOnSign;
	}

	public StringType getCliSmsOnSignText() {
		return cliSmsOnSignText;
	}

	public void setCliSmsOnSignText(StringType cliSmsOnSignText) {
		this.cliSmsOnSignText = cliSmsOnSignText;
	}

	public StringType getExternalLinkOnSignLabel() {
		return externalLinkOnSignLabel;
	}

	public void setExternalLinkOnSignLabel(StringType externalLinkOnSignLabel) {
		this.externalLinkOnSignLabel = externalLinkOnSignLabel;
	}

	public StringType getExternalLinkOnSignUrl() {
		return externalLinkOnSignUrl;
	}

	public void setExternalLinkOnSignUrl(StringType externalLinkOnSignUrl) {
		this.externalLinkOnSignUrl = externalLinkOnSignUrl;
	}

	public IntegerType getPdfNumCopie() {
		return pdfNumCopie;
	}

	public void setPdfNumCopie(IntegerType pdfNumCopie) {
		this.pdfNumCopie = pdfNumCopie;
	}

	public StringType getPdfTestoCopia1() {
		return pdfTestoCopia1;
	}

	public void setPdfTestoCopia1(StringType pdfTestoCopia1) {
		this.pdfTestoCopia1 = pdfTestoCopia1;
	}

	public StringType getPdfTestoCopia2() {
		return pdfTestoCopia2;
	}

	public void setPdfTestoCopia2(StringType pdfTestoCopia2) {
		this.pdfTestoCopia2 = pdfTestoCopia2;
	}

	public StringType getPdfTestoCopia3() {
		return pdfTestoCopia3;
	}

	public void setPdfTestoCopia3(StringType pdfTestoCopia3) {
		this.pdfTestoCopia3 = pdfTestoCopia3;
	}

	public StringType getPdfTestoCopia4() {
		return pdfTestoCopia4;
	}

	public void setPdfTestoCopia4(StringType pdfTestoCopia4) {
		this.pdfTestoCopia4 = pdfTestoCopia4;
	}

	public BooleanType getDownloadAsFacsimile() {
		return downloadAsFacsimile;
	}

	public void setDownloadAsFacsimile(BooleanType downloadAsFacsimile) {
		this.downloadAsFacsimile = downloadAsFacsimile;
	}

	public BooleanType getIsProspectEnabledOnCli1() {
		return isProspectEnabledOnCli1;
	}

	public void setIsProspectEnabledOnCli1(BooleanType isProspectEnabledOnCli1) {
		this.isProspectEnabledOnCli1 = isProspectEnabledOnCli1;
	}

	public BooleanType getIsProspectEnabledOnCli2() {
		return isProspectEnabledOnCli2;
	}

	public void setIsProspectEnabledOnCli2(BooleanType isProspectEnabledOnCli2) {
		this.isProspectEnabledOnCli2 = isProspectEnabledOnCli2;
	}

	public BooleanType getIsProspectEnabledOnCli3() {
		return isProspectEnabledOnCli3;
	}

	public void setIsProspectEnabledOnCli3(BooleanType isProspectEnabledOnCli3) {
		this.isProspectEnabledOnCli3 = isProspectEnabledOnCli3;
	}

	public BooleanType getIsProspectEnabledOnCli4() {
		return isProspectEnabledOnCli4;
	}

	public void setIsProspectEnabledOnCli4(BooleanType isProspectEnabledOnCli4) {
		this.isProspectEnabledOnCli4 = isProspectEnabledOnCli4;
	}

	public BooleanType getIsProspectEnabledOnCli5() {
		return isProspectEnabledOnCli5;
	}

	public void setIsProspectEnabledOnCli5(BooleanType isProspectEnabledOnCli5) {
		this.isProspectEnabledOnCli5 = isProspectEnabledOnCli5;
	}

	public BooleanType getIsProspectEnabledOnCli6() {
		return isProspectEnabledOnCli6;
	}

	public void setIsProspectEnabledOnCli6(BooleanType isProspectEnabledOnCli6) {
		this.isProspectEnabledOnCli6 = isProspectEnabledOnCli6;
	}

	public BooleanType getIsProspectEnabledOnCli7() {
		return isProspectEnabledOnCli7;
	}

	public void setIsProspectEnabledOnCli7(BooleanType isProspectEnabledOnCli7) {
		this.isProspectEnabledOnCli7 = isProspectEnabledOnCli7;
	}

	public BooleanType getIsProspectEnabledOnCli8() {
		return isProspectEnabledOnCli8;
	}

	public void setIsProspectEnabledOnCli8(BooleanType isProspectEnabledOnCli8) {
		this.isProspectEnabledOnCli8 = isProspectEnabledOnCli8;
	}

	public BooleanType getIsProspectEnabledOnCli9() {
		return isProspectEnabledOnCli9;
	}

	public void setIsProspectEnabledOnCli9(BooleanType isProspectEnabledOnCli9) {
		this.isProspectEnabledOnCli9 = isProspectEnabledOnCli9;
	}

	public BooleanType getPdfHasPublicationNotes() {
		return pdfHasPublicationNotes;
	}

	public void setPdfHasPublicationNotes(BooleanType pdfHasPublicationNotes) {
		this.pdfHasPublicationNotes = pdfHasPublicationNotes;
	}

	public StringType getPdfPublicationNotes() {
		return pdfPublicationNotes;
	}

	public void setPdfPublicationNotes(StringType pdfPublicationNotes) {
		this.pdfPublicationNotes = pdfPublicationNotes;
	}

	public ListType getPdfApplReferences() {
		return pdfApplReferences;
	}

	public void setPdfApplReferences(ListType pdfApplReferences) {
		this.pdfApplReferences = pdfApplReferences;
	}

	public BooleanType getIsControlliCompleti() {
		return isControlliCompleti;
	}

	public void setIsControlliCompleti(BooleanType isControlliCompleti) {
		this.isControlliCompleti = isControlliCompleti;
	}

	public BooleanType getPdfIsInvioInSedeEnabled() {
		return pdfIsInvioInSedeEnabled;
	}

	public void setPdfIsInvioInSedeEnabled(BooleanType pdfIsInvioInSedeEnabled) {
		this.pdfIsInvioInSedeEnabled = pdfIsInvioInSedeEnabled;
	}

	public BooleanType getPdfIsCopernicoEnabled() {
		return pdfIsCopernicoEnabled;
	}

	public void setPdfIsCopernicoEnabled(BooleanType pdfIsCopernicoEnabled) {
		this.pdfIsCopernicoEnabled = pdfIsCopernicoEnabled;
	}

	public StringType getTipoPriips() {
		return tipoPriips;
	}

	public void setTipoPriips(StringType tipoPriips) {
		this.tipoPriips = tipoPriips;
	}

	public BooleanType getFacSimileOnPreview() {
		return facSimileOnPreview;
	}

	public void setFacSimileOnPreview(BooleanType facSimileOnPreview) {
		this.facSimileOnPreview = facSimileOnPreview;
	}

	public StringType getCallSrvDispositivaBMED() {
		return callSrvDispositivaBMED;
	}

	public void setCallSrvDispositivaBMED(StringType callSrvDispositivaBMED) {
		this.callSrvDispositivaBMED = callSrvDispositivaBMED;
	}

	public IntegerType getPdfNumSignFields() {
		return pdfNumSignFields;
	}

	public void setPdfNumSignFields(IntegerType pdfNumSignFields) {
		this.pdfNumSignFields = pdfNumSignFields;
	}

	public BooleanType getPdfHasValidationWarningMessage() {
		return pdfHasValidationWarningMessage;
	}

	public void setPdfHasValidationWarningMessage(
			BooleanType pdfHasValidationWarningMessage) {
		this.pdfHasValidationWarningMessage = pdfHasValidationWarningMessage;
	}

	public BooleanType getHasDataSottoscrizioneOggi() {
		return hasDataSottoscrizioneOggi;
	}

	public void setHasDataSottoscrizioneOggi(BooleanType hasDataSottoscrizioneOggi) {
		this.hasDataSottoscrizioneOggi = hasDataSottoscrizioneOggi;
	}

	public String getPritBarcode() {
		return pritBarcode;
	}

	public void setPritBarcode(String pritBarcode) {
		this.pritBarcode = pritBarcode;
	}

	public BooleanType getIsTipoSoggettoPersonaFisica() {
		return isTipoSoggettoPersonaFisica;
	}

	public void setIsTipoSoggettoPersonaFisica(BooleanType isTipoSoggettoPersonaFisica) {
		this.isTipoSoggettoPersonaFisica = isTipoSoggettoPersonaFisica;
	}

	public BooleanType getIsTipoSoggettoProfessional() {
		return isTipoSoggettoProfessional;
	}

	public void setIsTipoSoggettoProfessional(BooleanType isTipoSoggettoProfessional) {
		this.isTipoSoggettoProfessional = isTipoSoggettoProfessional;
	}

	public BooleanType getIsTipoSoggettoPersonaGiuridica() {
		return isTipoSoggettoPersonaGiuridica;
	}

	public void setIsTipoSoggettoPersonaGiuridica(BooleanType isTipoSoggettoPersonaGiuridica) {
		this.isTipoSoggettoPersonaGiuridica = isTipoSoggettoPersonaGiuridica;
	}

	public BooleanType getIsTipoSoggettoFamilyBanker() {
		return isTipoSoggettoFamilyBanker;
	}

	public void setIsTipoSoggettoFamilyBanker(BooleanType isTipoSoggettoFamilyBanker) {
		this.isTipoSoggettoFamilyBanker = isTipoSoggettoFamilyBanker;
	}

	public BooleanType getIsUsableInCrafterWayout() {
		return isUsableInCrafterWayout;
	}

	public void setIsUsableInCrafterWayout(BooleanType isUsableInCrafterWayout) {
		this.isUsableInCrafterWayout = isUsableInCrafterWayout;
	}

	public BooleanType getExistInCartaChimica() {
		return existInCartaChimica;
	}

	public void setExistInCartaChimica(BooleanType existInCartaChimica) {
		this.existInCartaChimica = existInCartaChimica;
	}

	public StringType getTipoProcessoSede() {
		return tipoProcessoSede;
	}

	public void setTipoProcessoSede(StringType tipoProcessoSede) {
		this.tipoProcessoSede = tipoProcessoSede;
	}
	
	public StringType getNoteDiConfigurazioneInInput() {
		return noteDiConfigurazioneInInput;
	}

	public void setNoteDiConfigurazioneInInput(StringType noteDiConfigurazioneInInput) {
		this.noteDiConfigurazioneInInput = noteDiConfigurazioneInInput;
	}

	public StringType getNoteDiConfigurazione() {
		return noteDiConfigurazione;
	}

	public void setNoteDiConfigurazione(StringType noteDiConfigurazione) {
		this.noteDiConfigurazione = noteDiConfigurazione;
	}

	public StringType getNoteFbCopernico() {
		return noteFbCopernico;
	}

	public void setNoteFbCopernico(StringType noteFbCopernico) {
		this.noteFbCopernico = noteFbCopernico;
	}

	public StringType getExternalLinkAggOnSignLabel() {
		return externalLinkAggOnSignLabel;
	}

	public void setExternalLinkAggOnSignLabel(StringType externalLinkAggOnSignLabel) {
		this.externalLinkAggOnSignLabel = externalLinkAggOnSignLabel;
	}

	public StringType getExternalLinkAggOnSignUrl() {
		return externalLinkAggOnSignUrl;
	}

	public void setExternalLinkAggOnSignUrl(StringType externalLinkAggOnSignUrl) {
		this.externalLinkAggOnSignUrl = externalLinkAggOnSignUrl;
	}

	public StringType getPdfCodLineaBusiness() {
		return pdfCodLineaBusiness;
	}

	public void setPdfCodLineaBusiness(StringType pdfCodLineaBusiness) {
		this.pdfCodLineaBusiness = pdfCodLineaBusiness;
	}

	public BooleanType getHasPreferenzeInPriips() {
		return hasPreferenzeInPriips;
	}

	public void setHasPreferenzeInPriips(BooleanType hasPreferenzeInPriips) {
		this.hasPreferenzeInPriips = hasPreferenzeInPriips;
	}

	public BooleanType getOnStockProcessBatchQueue() {
		return onStockProcessBatchQueue;
	}

	public void setOnStockProcessBatchQueue(BooleanType onStockProcessBatchQueue) {
		this.onStockProcessBatchQueue = onStockProcessBatchQueue;
	}

	public StringType getPdfCodFaseCommerciale() {
		return pdfCodFaseCommerciale;
	}

	public void setPdfCodFaseCommerciale(StringType pdfCodFaseCommerciale) {
		this.pdfCodFaseCommerciale = pdfCodFaseCommerciale;
	}

	public ListType getPdfElencoRuoliUtilizzatori() {
		return pdfElencoRuoliUtilizzatori;
	}

	public void setPdfElencoRuoliUtilizzatori(ListType pdfElencoRuoliUtilizzatori) {
		this.pdfElencoRuoliUtilizzatori = pdfElencoRuoliUtilizzatori;
	}

	public StringType getFilenetClasseDocCliente() {
		return filenetClasseDocCliente;
	}

	public void setFilenetClasseDocCliente(StringType filenetClasseDocCliente) {
		this.filenetClasseDocCliente = filenetClasseDocCliente;
	}

	public StringType getFilenetCodiceDocCliente() {
		return filenetCodiceDocCliente;
	}

	public void setFilenetCodiceDocCliente(StringType filenetCodiceDocCliente) {
		this.filenetCodiceDocCliente = filenetCodiceDocCliente;
	}

	public BooleanType getHasControlloAmlAVR() {
		return hasControlloAmlAVR;
	}

	public void setHasControlloAmlAVR(BooleanType hasControlloAmlAVR) {
		this.hasControlloAmlAVR = hasControlloAmlAVR;
	}

	public BooleanType getHasControlloAmlPEP() {
		return hasControlloAmlPEP;
	}

	public void setHasControlloAmlPEP(BooleanType hasControlloAmlPEP) {
		this.hasControlloAmlPEP = hasControlloAmlPEP;
	}

	public BooleanType getHasControlloSCAI() {
		return hasControlloSCAI;
	}

	public void setHasControlloSCAI(BooleanType hasControlloSCAI) {
		this.hasControlloSCAI = hasControlloSCAI;
	}

	public StringType getTipoProcessoSedeInPdfAnag() {
		return tipoProcessoSedeInPdfAnag;
	}

	public void setTipoProcessoSedeInPdfAnag(StringType tipoProcessoSedeInPdfAnag) {
		this.tipoProcessoSedeInPdfAnag = tipoProcessoSedeInPdfAnag;
	}

	public boolean isVenditaComeBmed() {
		return venditaComeBmed;
	}

	public void setVenditaComeBmed(boolean venditaComeBmed) {
		this.venditaComeBmed = venditaComeBmed;
	}

	public BooleanType getPdfHasLayerCollocamentoADistanza() {
		return pdfHasLayerCollocamentoADistanza;
	}

	public void setPdfHasLayerCollocamentoADistanza(BooleanType pdfHasLayerCollocamentoADistanza) {
		this.pdfHasLayerCollocamentoADistanza = pdfHasLayerCollocamentoADistanza;
	}

	public DateType getPdfDataFineAccettazionePubblPrec() {
		return pdfDataFineAccettazionePubblPrec;
	}

	public void setPdfDataFineAccettazionePubblPrec(DateType pdfDataFineAccettazionePubblPrec) {
		this.pdfDataFineAccettazionePubblPrec = pdfDataFineAccettazionePubblPrec;
	}

	public BooleanType getSkipCallToAggiornaModuloMom() {
		return skipCallToAggiornaModuloMom;
	}

	public void setSkipCallToAggiornaModuloMom(BooleanType skipCallToAggiornaModuloMom) {
		this.skipCallToAggiornaModuloMom = skipCallToAggiornaModuloMom;
	}

	public StringType getSocieta() {
		return societa;
	}

	public void setSocieta(StringType societa) {
		this.societa = societa;
	}

}
