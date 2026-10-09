package prgm.pdfwebforms.model;

import java.util.ArrayList;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ByteArrayType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.core.PdfContextIntf;
import prgm.pdfwebforms.core.PdfPredefinedFields;
import prgm.pdfwebforms.core.PdfXmlUtils;
import prgm.pdfwebforms.mom.CostantiMOM;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfInstanceModel extends PdfContextIntf {

	public static String STATO_BOZZA 								= "01";
	public static String STATO_COMPLETATO_E_INVIATO_AL_CLIENTE 		= "02";
	public static String STATO_COMPLETATO 							= "03";
	public static String STATO_RIFIUTATO_DAL_CLIENTE				= "04";
	
	//Where condition di esclusione dei pdf che utilizzano una agevolazione numero rosso
	public static String CONDIZIONE_STATO_PDF_COMPLETATO_RETE = 
				   	  		"(PDF_INSTANCE.STATO = '03' or (PDF_INSTANCE.STATO = '02' and convert(date, getDate()) <= convert(date, dateadd(dd, 3 , PDF_INSTANCE.COMPLETION_TIME))))";
		
	public static String MODALITA_SOTTOSCRIZIONE_TUTTE = "TUTTE";
	public static String MODALITA_SOTTOSCRIZIONE_NESSUNA = "NESSUNA";
	public static String MODALITA_SOTTOSCRIZIONE_CARTA_LIBERA 	= "CARTA_LIBERA";
	public static String MODALITA_SOTTOSCRIZIONE_CARTA_CHIMICA 	= "CARTA_CHIMICA";
	public static String MODALITA_SOTTOSCRIZIONE_FIRMA_DIGITALE = "FIRMA_DIGITALE";
	public static String MODALITA_SOTTOSCRIZIONE_COPERNICO = "COPERNICO";
	public static String MODALITA_SOTTOSCRIZIONE_STAMPA = "STAMPA";
	public static String MODALITA_SOTTOSCRIZIONE_CARTA_DIGITALE = "CARTA_DIGITALE"; // Modalità privata. E' firma digitale senza firme impostata solo alla fine

	public static String TIPO_COPERNICO_SMART = "S";
	
	private StringType  			pdfInstanceId = new StringType();
	private IntegerType  			pdfInstanceDettNum = new IntegerType();
	private StringType  			pdfDescr = new StringType();
	private BooleanType				isMultiPdf = new BooleanType();
	private StringType  			originalPdfInstanceId = new StringType();
	
	private StringType 				pdfCreationUser = new StringType();
	private TimestampType			pdfCreationTime = new TimestampType();
	private StringType 				pdfLastModUser = new StringType();
	private TimestampType			pdfLastModTime = new TimestampType();
	private StringType 				pdfCompletionUser = new StringType();
	private TimestampType			pdfCompletionTime = new TimestampType();

	private StringType 				pdfCreationUserType = new StringType();
	
	private StringType 				pdfStatus = new StringType(STATO_BOZZA);
	private StringType 				pdfCompilationMode = new StringType();	
	private StringType 				pdfBarcode = new StringType();
	private StringType  			pdfEnvironment = new StringType();
	
	private StringType 				codAgente = new StringType();
	private StringType 				nominativoAgente = new StringType();
	private StringType 				codAgeImpersonato = new StringType();
	private StringType 				codAgeSupportante = new StringType();
	private StringType 				codRuoloImpersonato = new StringType();
	
	private	PdfPersonInstanceModel	cli1 = new PdfPersonInstanceModel();
	private	PdfPersonInstanceModel	cli2 = new PdfPersonInstanceModel();
	private	PdfPersonInstanceModel	cli3 = new PdfPersonInstanceModel();
	private	PdfPersonInstanceModel	cli4 = new PdfPersonInstanceModel();
	private	PdfPersonInstanceModel	cli5 = new PdfPersonInstanceModel();
	private	PdfPersonInstanceModel	cli6 = new PdfPersonInstanceModel();
	private	PdfPersonInstanceModel	cli7 = new PdfPersonInstanceModel();
	private	PdfPersonInstanceModel	cli8 = new PdfPersonInstanceModel();
	private	PdfPersonInstanceModel	cli9 = new PdfPersonInstanceModel();
	
	private StringType 				pdfXmlData = new StringType();
	private ByteArrayType			pdfContent = null;
	
	private PdfAnagModel			pdfAnag = new PdfAnagModel();
	private StringType 				pdfCompilationModeDescr = new StringType();
	private BooleanType 			flagWayout = new BooleanType();
	
	private StringType				pdfTipoCopernico = new StringType(); // "L" = Light, "S" = Smart
	private StringType				pdfNoteFBCopernico = new StringType();

	private BooleanType 			isSwitch = new BooleanType();
	private IntegerType 			idCarrello = new IntegerType();
	private IntegerType 			idDispCarrello = new IntegerType();
	private StringType  			codProd = new StringType();
	
	private StringType  			externalEntityAppl = new StringType();
	private StringType  			externalEntityName = new StringType();
	private StringType  			externalEntityKey = new StringType();
	
	private StringType  			callback = new StringType();
	
	private StringType  			idAgevolazione = new StringType();
	private StringType  			codiceAgevolazione = new StringType();
	private DoubleType  			importo = new DoubleType();
	private StringType  			iban = new StringType();
	private StringType  			numeroContratto = new StringType();
	private StringType  			numeroProposta = new StringType();	
	private StringType  			numeroPropostaCorrelata = new StringType();		// Utile solo a MOM
	private StringType  			tipoSupportoMaterialeContrattuale = new StringType();
	private StringType  			idReportAdeguatezza = new StringType();
	private StringType  			idSostituzione = new StringType();	
	private StringType  			numOrdineReportAdeguatezza = new StringType();
	private StringType  			idQuestionarioIdd = new StringType();
	private StringType  			idRaccomandazioneIdd = new StringType();
	private StringType  			codDispositivaBMED = new StringType();
	private StringType  			idFileNAS = new StringType();
	private StringType				putOnSignedProcessBatchQueue = new StringType();
	private StringType 				putOnPostCompletionProcessBatchQueue = new StringType();
	private StringType 				putOnDeadEndProcessBatchQueue = new StringType();
	private StringType  			pdfsForMOM = new StringType();
	private StringType				chiavePritMomInfo = new StringType();
	private StringType  			sistemaClient = new StringType();
	private StringType				isFirmaADistanza = new StringType();
	private StringType  			isMop = new StringType(); // Istanza integrata con MOM con il modello MOP
	
	// Basket
	private StringType 				inBasket = new StringType();
	private IntegerType 			ordineCompilazioneBasket = new IntegerType();
	
	// PAPF	
	private BooleanType				inProcessPdfInstanceContentAsBinary = new BooleanType();

	/***********************************************************************************************/
	/* Main process data from main pdfData 														   */
	/***********************************************************************************************/
	public void instanceFromProcessData(PdfModel pdf) throws Exception{
		PdfDataModel mainPdfData = pdf.mainPdfData();
		setImporto(mainPdfData.read(PdfPredefinedFields.IMPORTO)==null ? new DoubleType():new DoubleType(mainPdfData.read(PdfPredefinedFields.IMPORTO).toString()));
		setIban(mainPdfData.read(PdfPredefinedFields.IBAN)==null ? new StringType():new StringType(mainPdfData.read(PdfPredefinedFields.IBAN).toString()));
		setNumeroContratto(mainPdfData.read(PdfPredefinedFields.NUMERO_CONTRATTO)==null ? new StringType():new StringType(mainPdfData.read(PdfPredefinedFields.NUMERO_CONTRATTO).toString()));
		setNumeroProposta(mainPdfData.read(PdfPredefinedFields.NUMERO_PROPOSTA)==null ? new StringType():new StringType(mainPdfData.read(PdfPredefinedFields.NUMERO_PROPOSTA).toString()));
		setNumeroPropostaCorrelata(mainPdfData.read(PdfPredefinedFields.NUMERO_PROPOSTA_CORRELATA)==null ? new StringType():new StringType(mainPdfData.read(PdfPredefinedFields.NUMERO_PROPOSTA_CORRELATA).toString()));
		setTipoSupportoMaterialeContrattuale(mainPdfData.read(PdfPredefinedFields.TIPO_SUPPORTO_MATERIALE_CONTRATTUALE)==null ? new StringType():new StringType(mainPdfData.read(PdfPredefinedFields.TIPO_SUPPORTO_MATERIALE_CONTRATTUALE).toString()));
		setCodAgeSupportante((mainPdfData.read(PdfPredefinedFields.COD_FPS_SUPPORTANTE)==null || mainPdfData.read(PdfPredefinedFields.COD_FPS_SUPPORTANTE).isNull()) ? new StringType():new StringType(Tools.fillSx(mainPdfData.read(PdfPredefinedFields.COD_FPS_SUPPORTANTE).toString(),'0',10)));
	}
	
	/***********************************************************************************************/
	private static final int MAX_NUM_CLIENTI_SQUADRA_DB = 9;
	/***********************************************************************************************/
	public void instanceFromData(PdfModel pdf) throws Exception{
		PdfDataModel pdfData = pdf.getPdfData();
		
		setIsSwitch(pdfData.getIsSwitch());
		setIdCarrello(pdfData.getIdCarrello());
		setIdDispCarrello(pdf.mainPdfData().getIdDispCarrello());
		setIdReportAdeguatezza(pdf.mainPdfData().getIdReportAdeguatezza());
		setIdSostituzione(pdf.mainPdfData().getIdSostituzione());
		setNumOrdineReportAdeguatezza(pdf.mainPdfData().getNumOrdineReportAdeguatezza());
		setInBasket(pdf.isInBasket()?new StringType("S"):new StringType());
		setOrdineCompilazioneBasket(pdfData.getOrdineCompilazioneBasket());
		setPdfBarcode(new StringType(pdf.mainPdfData().getPdfBarcode().toString()));		
		setCodDispositivaBMED(pdf.mainPdfData().getCodDispositivaBMED());
		
		instanceCodProdFromPdfModules(pdf);
		
		setPdfInstanceId(pdfData.getPdfInstanceId());
		setOriginalPdfInstanceId(pdf.getOriginalPdfInstanceId());
		setPdfEnvironment(pdfData.getPdfEnvironment());
		setExternalEntityAppl(pdfData.getExternalEntityAppl());
		setExternalEntityName(pdfData.getExternalEntityName());
		setExternalEntityKey(pdfData.getExternalEntityKey());
		
		// Gestione agevolazione
		initAgevolazioneInInstance(pdf);
		
		// Main process data from main pdfData 														   */
		instanceFromProcessData(pdf);

		setPdfAnag(pdf.mainPdfAnag());
		
		setPdfDescr(pdf.mainPdfAnag().getPdfDescr());
		if(!pdf.getPdfData().getPdfTitle().isNull())
			setPdfDescr(pdf.getPdfData().getPdfTitle());
		
		pdf.alignDynamicProcessProperties();		
		
		setPdfXmlData(new StringType(PdfXmlUtils.xmlFromModel(pdfData, pdf, false)));
		
		setCodAgente(pdf.getMainCodAgente()==null?new StringType():pdf.getMainCodAgente());
		if(!pdfData.getCodAgeImpersonato().isNull())
			setCodAgeImpersonato(new StringType(Tools.fillSx(pdfData.getCodAgeImpersonato().toString(),'0',10)));
		else if(pdf.getIsRete().booleanValue())
			setCodAgeImpersonato(getCodAgente());
		setCodRuoloImpersonato(pdfData.getCodRuoloImpersonato());

		for(int i=0;i<MAX_NUM_CLIENTI_SQUADRA_DB;i++)
			Tools.setPropertyValue(this,"cli"+(i+1),new PdfPersonInstanceModel());
		
		ArrayList<PdfPersonModel> cliToSave = clientiInstanceFromData(pdf);
		for(int i=0;i<cliToSave.size();i++){
			if(i >= MAX_NUM_CLIENTI_SQUADRA_DB)
				break;
			PdfPersonModel person = (PdfPersonModel)cliToSave.get(i);
			PdfPersonInstanceModel pi = (PdfPersonInstanceModel)Tools.getPropertyValue(this,"cli"+(i+1));
			pi.instanceFromData(person);
		}
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void instanceCodProdFromPdfModules(PdfModel pdf) throws Exception{
		StringBuilder prodCProd = new StringBuilder();
		for(int i=0;i<pdf.getPdfAnags().size();i++){
			PdfAnagModel pdfDettAnag = (PdfAnagModel)pdf.getPdfAnags().get(i);
			if(pdfDettAnag.getPdfMomCode().isNull())
				continue;
			prodCProd.append(pdfDettAnag.getPdfMomCode()+",");
		}
		if(prodCProd.length() > 0)			
			prodCProd = prodCProd.deleteCharAt(prodCProd.length()-1);
		setCodProd(new StringType(prodCProd.toString()));
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void initAgevolazioneInInstance(PdfModel pdf) throws Exception{
		// Id agevolazione
		setIdAgevolazione(new StringType());
		PdfDataModel agevolazionePdfData = pdf.mainPdfData();
		if(agevolazionePdfData.getNumAgevolazione().isNull()){  // se non passata in input 
			agevolazionePdfData = pdf.getPdfData();
			if(pdf.isMultiPdf()){
				agevolazionePdfData = (PdfDataModel)pdf.getPdfData().getPdfs().get(0);
				if(pdf.getPdfData().getIsSwitch().booleanValue() && pdf.getPdfData().getPdfs().size() > 1)
					agevolazionePdfData = (PdfDataModel)pdf.getPdfData().getPdfs().get(1);
			}
			String idAgev = "";
			if(agevolazionePdfData.read(PdfPredefinedFields.ID_AGEVOLAZIONE) != null)
				idAgev = agevolazionePdfData.read(PdfPredefinedFields.ID_AGEVOLAZIONE).toString();
			setIdAgevolazione(new StringType(idAgev));
		}else{													// se passata in input la imposto
			setIdAgevolazione(new StringType(agevolazionePdfData.getNumAgevolazione().toString()));
		}
		
		// Codice agevolazione
		setCodiceAgevolazione(new StringType());
		agevolazionePdfData = pdf.mainPdfData();
		if(agevolazionePdfData.getCodAgevolazione().isNull()){  // se non passato in input 
			agevolazionePdfData = pdf.getPdfData();
			if(pdf.isMultiPdf()){
				agevolazionePdfData = (PdfDataModel)pdf.getPdfData().getPdfs().get(0);
				if(pdf.getPdfData().getIsSwitch().booleanValue() && pdf.getPdfData().getPdfs().size() > 1)
					agevolazionePdfData = (PdfDataModel)pdf.getPdfData().getPdfs().get(1);
			}
			String codAgev = "";
			if(agevolazionePdfData.read(PdfPredefinedFields.CODICE_AGEVOLAZIONE) != null)
				codAgev = agevolazionePdfData.read(PdfPredefinedFields.CODICE_AGEVOLAZIONE).toString();
			setCodiceAgevolazione(new StringType(codAgev));
		}else{													// se passato in input lo imposto
			setCodiceAgevolazione(new StringType(agevolazionePdfData.getCodAgevolazione().toString()));
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static ArrayList<PdfPersonModel> clientiInstanceFromData(PdfModel pdf) throws Exception{
		
		ArrayList<PdfPersonModel> clienti = new ArrayList<PdfPersonModel>();
		if(pdf.isMultiPdf()){
			
			for(int i=0;i<pdf.getPdfData().getPdfs().size();i++){ // for each pdf
				PdfDataModel pdfDataElement = (PdfDataModel)pdf.getPdfData().getPdfs().get(i);
				for(PdfPersonModel pdfCli : pdfDataElement.getClienti()){ // for each person of pdf
					if(pdfCli.isEmty())
						continue;
					boolean found = false;
					for(int j=0;j<clienti.size();j++){ // for each person managed
						PdfPersonModel managedCli = (PdfPersonModel)clienti.get(j);
						if(managedCli.isEqual(pdfCli)){
							found = true;
							break;
						}
					}
					if(!found){
						PdfPersonModel cli = (PdfPersonModel)Tools.cloneObject(pdfCli); 
						clienti.add(cli);
					}
					
				}
			}
		}else{
			
			for(PdfPersonModel pdfCli : pdf.getPdfData().getClienti()){
				if(pdfCli.isEmty())
					continue;
				boolean found = false;
				for(int j=0;j<clienti.size();j++){ // for each person managed
					PdfPersonModel managedCli = (PdfPersonModel)clienti.get(j);
					if(managedCli.isEqual(pdfCli)){
						found = true;
						break;
					}
				}
				if(!found){
					PdfPersonModel cli = (PdfPersonModel)Tools.cloneObject(pdfCli); 
					clienti.add(cli);
				}
			}
			
		}
		return clienti;
	}
		
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void dataFromInstance(PdfDataModel pdfData) throws Exception{
		pdfData.setPdfId(new StringType(getPdfAnag().getPdfId().toString()));
		pdfData.setPdfCode(new StringType(getPdfAnag().getPdfCode().toString()));
		pdfData.setPdfMomCode(new StringType(getPdfAnag().getPdfMomCode().toString()));
		pdfData.setPdfEnvironment(new StringType(getPdfEnvironment().toString()));
		pdfData.setPdfPublicationId(new IntegerType(getPdfAnag().getPdfPublicationId().intValue()));
		pdfData.setPdfInstanceId(new StringType(getPdfInstanceId().toString()));
		pdfData.setIsSwitch(new BooleanType(getIsSwitch().booleanValue()));
		pdfData.setPdfStatus(getPdfStatus().toString());
		
		pdfData.setExternalEntityAppl(new StringType(getExternalEntityAppl().toString()));
		pdfData.setExternalEntityName(new StringType(getExternalEntityName().toString()));
		pdfData.setExternalEntityKey(new StringType(getExternalEntityKey().toString()));
		
		for(int i=0;i<pdfData.getPdfs().size();i++) {
			PdfDataModel el = (PdfDataModel)pdfData.getPdfs().get(i);
			el.setPdfInstanceId(new StringType(getPdfInstanceId().toString()));
		}
	}
	
	/***********************************************************************************************/
	/* riconoscimento fasi lavorazione MOM
	/***********************************************************************************************/
	public boolean byOperatoreMom(){ // Creata dall'operatore mom sia per carta chimica che per doppia spunta
		return  getPdfEnvironment().equals(CostantiMOM.MOM_ENVIRONMENT) &&
				getPdfCreationUserType().equals(ClientSessionContext.USER_TYPE_SEDE) &&
				!getExternalEntityName().equals(CostantiMOM.MOM_EXTERNAL_IMPORTED_KEY_ENTITY_NAME);
	}
	public boolean isCloneDoppiaSpuntaMOM() {
		return getPdfEnvironment().equals(CostantiMOM.MOM_ENVIRONMENT) &&
			   getExternalEntityName().toString().startsWith(CostantiMOM.MOM_EXTERNAL_DOPPIASPUNTA_KEY_ENTITY_NAME_PREFIX);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isBasketSemplt() {
		return !getIdCarrello().isNull() && !getPdfEnvironment().equals("CARRELLO");
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
	public StringType getPdfCompletionUser() {
		return pdfCompletionUser;
	}
	public void setPdfCompletionUser(StringType pdfCompletionUser) {
		this.pdfCompletionUser = pdfCompletionUser;
	}
	public TimestampType getPdfCompletionTime() {
		return pdfCompletionTime;
	}
	public void setPdfCompletionTime(TimestampType pdfCompletionTime) {
		this.pdfCompletionTime = pdfCompletionTime;
	}
	public StringType getPdfStatus() {
		return pdfStatus;
	}
	public void setPdfStatus(StringType pdfStatus) {
		this.pdfStatus = pdfStatus;
	}
	public StringType getPdfXmlData() {
		return pdfXmlData;
	}
	public void setPdfXmlData(StringType pdfXmlData) {
		this.pdfXmlData = pdfXmlData;
	}
	public ByteArrayType getPdfContent() {
		return pdfContent;
	}
	public void setPdfContent(ByteArrayType pdfContent) {
		this.pdfContent = pdfContent;
	}
	public PdfAnagModel getPdfAnag() {
		return pdfAnag;
	}
	public void setPdfAnag(PdfAnagModel pdfAnag) {
		this.pdfAnag = pdfAnag;
	}

	public StringType getPdfBarcode() {
		return pdfBarcode;
	}

	public void setPdfBarcode(StringType pdfBarcode) {
		this.pdfBarcode = pdfBarcode;
	}

	public StringType getCodAgente() {
		return codAgente;
	}

	public void setCodAgente(StringType codAgente) {
		this.codAgente = codAgente;
	}

	public PdfPersonInstanceModel getCli1() {
		return cli1;
	}

	public void setCli1(PdfPersonInstanceModel cli1) {
		this.cli1 = cli1;
	}

	public PdfPersonInstanceModel getCli2() {
		return cli2;
	}

	public void setCli2(PdfPersonInstanceModel cli2) {
		this.cli2 = cli2;
	}

	public PdfPersonInstanceModel getCli3() {
		return cli3;
	}

	public void setCli3(PdfPersonInstanceModel cli3) {
		this.cli3 = cli3;
	}

	public StringType getPdfCompilationMode() {
		return pdfCompilationMode;
	}

	public void setPdfCompilationMode(StringType pdfCompilationMode) {
		this.pdfCompilationMode = pdfCompilationMode;
	}

	public StringType getPdfInstanceId() {
		return pdfInstanceId;
	}

	public void setPdfInstanceId(StringType pdfInstanceId) {
		this.pdfInstanceId = pdfInstanceId;
	}

	public StringType getPdfEnvironment() {
		return pdfEnvironment;
	}

	public void setPdfEnvironment(StringType pdfEnvironment) {
		this.pdfEnvironment = pdfEnvironment;
	}

	public PdfPersonInstanceModel getCli4() {
		return cli4;
	}

	public void setCli4(PdfPersonInstanceModel cli4) {
		this.cli4 = cli4;
	}

	public PdfPersonInstanceModel getCli5() {
		return cli5;
	}

	public void setCli5(PdfPersonInstanceModel cli5) {
		this.cli5 = cli5;
	}

	public PdfPersonInstanceModel getCli6() {
		return cli6;
	}

	public void setCli6(PdfPersonInstanceModel cli6) {
		this.cli6 = cli6;
	}

	public PdfPersonInstanceModel getCli7() {
		return cli7;
	}

	public void setCli7(PdfPersonInstanceModel cli7) {
		this.cli7 = cli7;
	}

	public PdfPersonInstanceModel getCli8() {
		return cli8;
	}

	public void setCli8(PdfPersonInstanceModel cli8) {
		this.cli8 = cli8;
	}

	public PdfPersonInstanceModel getCli9() {
		return cli9;
	}

	public void setCli9(PdfPersonInstanceModel cli9) {
		this.cli9 = cli9;
	}

	public StringType getPdfCompilationModeDescr() {
		return pdfCompilationModeDescr;
	}

	public void setPdfCompilationModeDescr(StringType pdfCompilationModeDescr) {
		this.pdfCompilationModeDescr = pdfCompilationModeDescr;
	}


	public StringType getNominativoAgente() {
		return nominativoAgente;
	}


	public void setNominativoAgente(StringType nominativoAgente) {
		this.nominativoAgente = nominativoAgente;
	}


	public BooleanType getFlagWayout() {
		return flagWayout;
	}


	public void setFlagWayout(BooleanType flagWayout) {
		this.flagWayout = flagWayout;
	}


	public StringType getPdfNoteFBCopernico() {
		return pdfNoteFBCopernico;
	}


	public void setPdfNoteFBCopernico(StringType pdfNoteFBCopernico) {
		this.pdfNoteFBCopernico = pdfNoteFBCopernico;
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

	public StringType getPdfDescr() {
		return pdfDescr;
	}


	public void setPdfDescr(StringType pdfDescr) {
		this.pdfDescr = pdfDescr;
	}


	public StringType getIdAgevolazione() {
		return idAgevolazione;
	}


	public void setIdAgevolazione(StringType idAgevolazione) {
		this.idAgevolazione = idAgevolazione;
	}


	public StringType getCodiceAgevolazione() {
		return codiceAgevolazione;
	}


	public void setCodiceAgevolazione(StringType codiceAgevolazione) {
		this.codiceAgevolazione = codiceAgevolazione;
	}


	public DoubleType getImporto() {
		return importo;
	}


	public void setImporto(DoubleType importo) {
		this.importo = importo;
	}


	public StringType getNumeroContratto() {
		return numeroContratto;
	}


	public void setNumeroContratto(StringType numeroContratto) {
		this.numeroContratto = numeroContratto;
	}


	public StringType getNumeroProposta() {
		return numeroProposta;
	}


	public void setNumeroProposta(StringType numeroProposta) {
		this.numeroProposta = numeroProposta;
	}
	
	public StringType getCallback() {
		return callback;
	}


	public void setCallback(StringType callback) {
		this.callback = callback;
	}

	public StringType getPdfCreationUserType() {
		return pdfCreationUserType;
	}

	public void setPdfCreationUserType(StringType pdfCreationUserType) {
		this.pdfCreationUserType = pdfCreationUserType;
	}

	public StringType getIdReportAdeguatezza() {
		return idReportAdeguatezza;
	}


	public void setIdReportAdeguatezza(StringType idReportAdeguatezza) {
		this.idReportAdeguatezza = idReportAdeguatezza;
	}


	public StringType getTipoSupportoMaterialeContrattuale() {
		return tipoSupportoMaterialeContrattuale;
	}


	public void setTipoSupportoMaterialeContrattuale(StringType tipoSupportoMaterialeContrattuale) {
		this.tipoSupportoMaterialeContrattuale = tipoSupportoMaterialeContrattuale;
	}


	public IntegerType getPdfInstanceDettNum() {
		return pdfInstanceDettNum;
	}


	public void setPdfInstanceDettNum(IntegerType pdfInstanceDettNum) {
		this.pdfInstanceDettNum = pdfInstanceDettNum;
	}


	public BooleanType getIsMultiPdf() {
		return isMultiPdf;
	}


	public void setIsMultiPdf(BooleanType isMultiPdf) {
		this.isMultiPdf = isMultiPdf;
	}


	public StringType getPutOnSignedProcessBatchQueue() {
		return putOnSignedProcessBatchQueue;
	}


	public void setPutOnSignedProcessBatchQueue(StringType putOnSignedProcessBatchQueue) {
		this.putOnSignedProcessBatchQueue = putOnSignedProcessBatchQueue;
	}


	public StringType getPutOnPostCompletionProcessBatchQueue() {
		return putOnPostCompletionProcessBatchQueue;
	}


	public void setPutOnPostCompletionProcessBatchQueue(StringType putOnPostCompletionProcessBatchQueue) {
		this.putOnPostCompletionProcessBatchQueue = putOnPostCompletionProcessBatchQueue;
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


	public StringType getCodDispositivaBMED() {
		return codDispositivaBMED;
	}


	public void setCodDispositivaBMED(StringType codDispositivaBMED) {
		this.codDispositivaBMED = codDispositivaBMED;
	}


	public StringType getIdFileNAS() {
		return idFileNAS;
	}


	public void setIdFileNAS(StringType idFileNAS) {
		this.idFileNAS = idFileNAS;
	}


	public StringType getOriginalPdfInstanceId() {
		return originalPdfInstanceId;
	}


	public void setOriginalPdfInstanceId(StringType originalPdfInstanceId) {
		this.originalPdfInstanceId = originalPdfInstanceId;
	}


	public StringType getPdfsForMOM() {
		return pdfsForMOM;
	}


	public void setPdfsForMOM(StringType pdfsForMOM) {
		this.pdfsForMOM = pdfsForMOM;
	}
	
	public StringType getCodProd() {
		return codProd;
	}


	public void setCodProd(StringType codProd) {
		this.codProd = codProd;
	}


	public StringType getIdQuestionarioIdd() {
		return idQuestionarioIdd;
	}


	public void setIdQuestionarioIdd(StringType idQuestionarioIdd) {
		this.idQuestionarioIdd = idQuestionarioIdd;
	}


	public StringType getIdRaccomandazioneIdd() {
		return idRaccomandazioneIdd;
	}


	public void setIdRaccomandazioneIdd(StringType idRaccomandazioneIdd) {
		this.idRaccomandazioneIdd = idRaccomandazioneIdd;
	}


	public StringType getPdfTipoCopernico() {
		return pdfTipoCopernico;
	}


	public void setPdfTipoCopernico(StringType pdfTipoCopernico) {
		this.pdfTipoCopernico = pdfTipoCopernico;
	}


	public StringType getIban() {
		return iban;
	}


	public void setIban(StringType iban) {
		this.iban = iban;
	}

	public StringType getNumOrdineReportAdeguatezza() {
		return numOrdineReportAdeguatezza;
	}

	public void setNumOrdineReportAdeguatezza(StringType numOrdineReportAdeguatezza) {
		this.numOrdineReportAdeguatezza = numOrdineReportAdeguatezza;
	}

	public StringType getNumeroPropostaCorrelata() {
		return numeroPropostaCorrelata;
	}

	public void setNumeroPropostaCorrelata(StringType numeroPropostaCorrelata) {
		this.numeroPropostaCorrelata = numeroPropostaCorrelata;
	}

	public StringType getChiavePritMomInfo() {
		return chiavePritMomInfo;
	}

	public void setChiavePritMomInfo(StringType chiavePritMomInfo) {
		this.chiavePritMomInfo = chiavePritMomInfo;
	}

	public StringType getCodAgeImpersonato() {
		return codAgeImpersonato;
	}

	public void setCodAgeImpersonato(StringType codAgeImpersonato) {
		this.codAgeImpersonato = codAgeImpersonato;
	}

	public StringType getInBasket() {
		return inBasket;
	}

	public void setInBasket(StringType inBasket) {
		this.inBasket = inBasket;
	}

	public IntegerType getOrdineCompilazioneBasket() {
		return ordineCompilazioneBasket;
	}

	public void setOrdineCompilazioneBasket(IntegerType ordineCompilazioneBasket) {
		this.ordineCompilazioneBasket = ordineCompilazioneBasket;
	}

	public StringType getIdSostituzione() {
		return idSostituzione;
	}

	public void setIdSostituzione(StringType idSostituzione) {
		this.idSostituzione = idSostituzione;
	}

	public StringType getCodAgeSupportante() {
		return codAgeSupportante;
	}

	public void setCodAgeSupportante(StringType codAgeSupportante) {
		this.codAgeSupportante = codAgeSupportante;
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

	public BooleanType getInProcessPdfInstanceContentAsBinary() {
		return inProcessPdfInstanceContentAsBinary;
	}

	public void setInProcessPdfInstanceContentAsBinary(BooleanType inProcessPdfInstanceContentAsBinary) {
		this.inProcessPdfInstanceContentAsBinary = inProcessPdfInstanceContentAsBinary;
	}

	public StringType getIsFirmaADistanza() {
		return isFirmaADistanza;
	}

	public void setIsFirmaADistanza(StringType isFirmaADistanza) {
		this.isFirmaADistanza = isFirmaADistanza;
	}

	public StringType getIsMop() {
		return isMop;
	}

	public void setIsMop(StringType isMop) {
		this.isMop = isMop;
	}

	public StringType getPutOnDeadEndProcessBatchQueue() {
		return putOnDeadEndProcessBatchQueue;
	}

	public void setPutOnDeadEndProcessBatchQueue(StringType putOnDeadEndProcessBatchQueue) {
		this.putOnDeadEndProcessBatchQueue = putOnDeadEndProcessBatchQueue;
	}

}
