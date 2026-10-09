package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001;

import java.math.BigDecimal;
import java.util.ArrayList;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.core.PdfCodedMessage;
import prgm.pdfwebforms.core.PdfPredefinedFields;
import prgm.pdfwebforms.dataentryutil.ContoAutocompleteInput;
import prgm.pdfwebforms.dataentryutil.ContoAutocompleteModel;
import prgm.pdfwebforms.drivers.PdfBaseDriver;
import prgm.pdfwebforms.drivers.io.BeforePreviewInputData;
import prgm.pdfwebforms.drivers.io.BeforePreviewOutputData;
import prgm.pdfwebforms.drivers.io.FinalEventInputData;
import prgm.pdfwebforms.drivers.io.FinalEventOutputData;
import prgm.pdfwebforms.drivers.io.FreezeEventInputData;
import prgm.pdfwebforms.drivers.io.FreezeEventOutputData;
import prgm.pdfwebforms.drivers.io.InitPdfInputData;
import prgm.pdfwebforms.drivers.io.InitPdfOutputData;
import prgm.pdfwebforms.drivers.io.LoadPdfInputData;
import prgm.pdfwebforms.drivers.io.LoadPdfOutputData;
import prgm.pdfwebforms.drivers.io.PdfVerifiedInputData;
import prgm.pdfwebforms.drivers.io.PdfVerifiedOutputData;
import prgm.pdfwebforms.drivers.io.SubmitPdfInputData;
import prgm.pdfwebforms.drivers.io.SubmitPdfOutputData;
import prgm.pdfwebforms.drivers.io.VerifyCopernicoPdfInputData;
import prgm.pdfwebforms.drivers.io.VerifyCopernicoPdfOutputData;
import prgm.pdfwebforms.drivers.io.VerifyPdfInputData;
import prgm.pdfwebforms.drivers.io.VerifyPdfOutputData;
import prgm.pdfwebforms.drivers.io.basket.ProvideBasketDataRequest;
import prgm.pdfwebforms.drivers.io.basket.ProvideBasketDataResponse;
import prgm.pdfwebforms.drivers.io.mifid.CompartoMifidModel;
import prgm.pdfwebforms.drivers.io.mifid.OperazioneMifidModel;
import prgm.pdfwebforms.drivers.io.mifid.ProvideMifidDataRequest;
import prgm.pdfwebforms.drivers.io.mifid.ProvideMifidDataResponse;
import prgm.pdfwebforms.drivers.io.prit.ProvidePritDataRequest;
import prgm.pdfwebforms.drivers.io.prit.ProvidePritDataResponse;
import prgm.pdfwebforms.drivers.io.prit.RigaPrit;
import prgm.pdfwebforms.drivers.io.reportadeguatezza.DispositivaReportAdeguatezzaModel;
import prgm.pdfwebforms.drivers.io.reportadeguatezza.ProdottoReportAdeguatezzaModel;
import prgm.pdfwebforms.drivers.io.reportadeguatezza.ProvideReportAdeguatezzaDataRequest;
import prgm.pdfwebforms.drivers.io.reportadeguatezza.ProvideReportAdeguatezzaDataResponse;
import prgm.pdfwebforms.drivers.io.squadra.ElementoSquadra;
import prgm.pdfwebforms.drivers.io.squadra.ProvideSquadraDataRequest;
import prgm.pdfwebforms.drivers.io.squadra.ProvideSquadraDataResponse;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.model.PdfPersonModel;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.PdfDataHelper.FieldValues;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.autocompletion.NumeroPolizzaAutoCompleteModel;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.model.ResiduoPolizzaModel;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.service.ResiduoPolizzaService;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.util.Costanti;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.util.Utility;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.validators.CollocazioneFondoValidator;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.validators.PdfCopernicoValidator;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.validators.PdfPolizzaPicValidator;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.validators.PdfValidator;
import prgm.pdfwebformsutil.drivers.PdfEventEnum;
import prgm.pdfwebformsutil.drivers.operativitaresidentiestero.ControlloOperativitaResidentiEstero;
import prgm.pdfwebformsutil.drivers.operativitaresidentiestero.model.ControlliUSPersonModel;

public class PdfDriver extends PdfBaseDriver {
	public static final String FIELD_NDG_ALTROCLIENTESDDBMED_VARIAZIONE = "ndgALTROCLIENTESDDBMEDVariazione";
	public static final String PREFIX_FIELD_LINEA_FONDO_PREMIO = "lineaFondoPremio";
	public static final String PREFIX_FIELD_CODICE_FONDO_PREMIO = "codiceFondoPremio";
	public static final String PREFIX_FIELD_SOCIETA_FONDO_PREMIO = "societaFondoPremio";
	public static final String PREFIX_FIELD_DESCRIZIONE_FONDO_PREMIO = "descrizioneFondoPremio";
	public static final String FIELD_TOTALE_IMPORTO = "totaleImporto";
	public static final String FIELD_TOTALE_PERCENTUALE = "totalePercentuale";
	public static final String FIELD_IS_VARIAZIONE_CONTO_SDD = "isVariazioneContoSDD";
	public static final String FIELD_IS_VARIAZIONE_DATA_PREMIO_RIATTIVAZIONE_SDD = "isVariazioneDataPremioRiattivazioneSDD";
	public static final String FIELD_IS_VARIAZIONE_FREQUENZA_SDD = "isVariazioneFrequenzaSDD";
	public static final String FIELD_FREQUENZA_VARIAZIONE_SDD = "frequenzaVariazioneSDD";
	public static final String FIELD_IS_VARIAZIONE_FRAZIONAMENTO_RIATTIVAZIONE_SDD = "isVariazioneFrazionamentoRiattivazioneSDD";
	public static final String FIELD_FREQUENZA_RIATTIVAZIONE_SDD = "frequenzaRiattivazioneSDD";
	public static final String FIELD_IS_VARIAZIONE_DISPOSIZIONE_SDD = "isVariazioneDisposizioneSDD";
	public static final String FIELD_IS_VARIAZIONE_IMPORTO_SDD = "isVariazioneImportoSDD";
	public static final String FIELD_IMPORTO_VARIAZIONE_SDD = "importoVariazioneSDD";
	public static final String FIELD_TIPO_SOSPENSIONEREVOCARIATTIVAZIONESDD = "tipoSOSPENSIONEREVOCARIATTIVAZIONESDD";
	public static final String FIELD_IS_VARIAZIONE_IMPORTO_RIATTIVAZIONE_SDD = "isVariazioneImportoRiattivazioneSDD";
	public static final String FIELD_IMPORTO_RIATTIVAZIONE_SDD = "importoRiattivazioneSDD";
	public static final String TIPOLOGIA_RIATTIVAZIONE = "RIATTIVAZIONE";
	public static final String PREFIX_FIELD_IMPORTO_FONDO_PREMIO = "importoFondoPremio";
	public static final String PREFIX_FIELD_PERCENTUALE_FONDO_PREMIO = "percentualeFondoPremio";
	public static final String PREFIX_FIELD_ISIN_FONDO_PREMIO = "isinFondoPremio";
	private boolean doInsertPritDelegato = false;
	private ResiduoPolizzaService residuoPolizzaService = null;
	
	public static final String ID_CENSIMENTO_CLIENTE1 = "idCensimentoCliente1";
	public static final String NDG_CLIENTE1 = "ndgCliente1";	
	public static final String DAO_FILE_NAME = "PdfWebFormsDrivers.PersonalPirVariazionePianoPac.ver00001.PersonalPirVariazionePianoPac";
	private static final String PROPERTY_NDG_CLIENTE = "ndgCliente";
	private ControlloOperativitaResidentiEstero<PdfDriver> opEstero;
		
	public PdfDriver() {
		residuoPolizzaService = new ResiduoPolizzaService(this, true);
		opEstero = new ControlloOperativitaResidentiEstero<PdfDriver>(this, true); 
	}
	
		
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public InitPdfOutputData onInitPdf(ClientSessionContext csc, InitPdfInputData input) throws Exception {

		InitPdfOutputData result = new InitPdfOutputData();
		
		ArrayList<String> uneditableFields = new ArrayList<>();
				
		uneditableFields.add("cognomeNomeALTROCLIENTESDDBMEDVariazione");

		result.setUneditableFields(uneditableFields);
		return result;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public LoadPdfOutputData onLoadPdf(ClientSessionContext csc, LoadPdfInputData input) throws Exception {
		PdfDataModel pdfData = input.getPdfData();
		PdfDataHelper dataHelper = new PdfDataHelper(pdfData);

		//Serve a distingure tra nuovo documento e bozza, nel primo caso non deve lanciare i controlli
		if((dataHelper.getIsVariazioneRipartizione()!= null) && 
		   (!dataHelper.getIsVariazioneRipartizione().isNull())) {
			CollocazioneFondoValidator collocazioneFondoValidator = new CollocazioneFondoValidator(this, PdfEventEnum.PAGE_ONLOAD);
			collocazioneFondoValidator.doValidate(csc, pdfData, null);
		}
		ctrlOperativitaResidentiEstero(csc, pdfData);
		
		// rfc #257299 - Se esiste la forma contrattuale ma è vuota è il nuovo modulo ma la bozza era il vecchio
		if( pdfData.fieldExist(PdfDataHelper.FieldNames.FORMACONTRATTUALE) && 
		   !dataHelper.getNdgCliente1().isNull() &&
		   !dataHelper.getNumeroPolizza().isNull() &&
			dataHelper.getFormaContrattuale().isNull()){
			NumeroPolizzaAutoCompleteModel polizza = Utility.recuperaPolizza(csc, pdfData, getPdf());
			dataHelper.setFormaContrattuale(polizza.getFormaContrattuale());
		}

		return null;
	}	
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public VerifyPdfOutputData onVerifyPdf(ClientSessionContext csc, VerifyPdfInputData input) throws Exception {
		
		VerifyPdfOutputData result = new VerifyPdfOutputData();
		PdfDataModel pdfData = input.getPdfData();
		PdfDataHelper dataHelper = new PdfDataHelper(pdfData);
		
		// rfc #257299 - aggiunta Sezione 2 al modulo. Se esiste il nuovo campo "formaContrattuale" e vale PIC allora gestiamo solo questa sezione.
		if(pdfData.fieldExist(PdfDataHelper.FieldNames.FORMACONTRATTUALE) && dataHelper.getFormaContrattuale().equals("PIC")) {
			PdfPolizzaPicValidator validator = new PdfPolizzaPicValidator(this, PdfEventEnum.VERIFY_PDF);
			validator.validate(csc, pdfData, result);
		}else {
			PdfValidator validator = new PdfValidator(this, PdfEventEnum.VERIFY_PDF);
			validator.validate(csc, pdfData, result);
		}
		
		ctrlOperativitaResidentiEstero(csc, pdfData);
		return result;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public SubmitPdfOutputData onSubmitPdf(ClientSessionContext csc, SubmitPdfInputData input) throws Exception {

		SubmitPdfOutputData result = new SubmitPdfOutputData();
		PdfDataModel pdfData = input.getPdfData();
		
		PdfDataHelper dataHelper = new PdfDataHelper(pdfData);
		
		if (!dataHelper.getNumeroPolizza().isNull()) {
	
			BigDecimal importoTotaleInvestito = BigDecimal.valueOf(0);
			BigDecimal percentualeTotaleInvestita = BigDecimal.valueOf(0);
			double importoTotale = 0;
			int idxUltimoComparto = -1; 
	
			importoTotale = Utility.getImportoRataPianoPac(csc, input.getPdfData());
	
			for (int i=0;;i++){
				StringType isinFondo = (StringType)pdfData.read(PdfDriver.PREFIX_FIELD_ISIN_FONDO_PREMIO+i);
				DoubleType percentualeFondo = (DoubleType)pdfData.read(PdfDriver.PREFIX_FIELD_PERCENTUALE_FONDO_PREMIO+i);
				DoubleType importoFondo = (DoubleType)pdfData.read(PdfDriver.PREFIX_FIELD_IMPORTO_FONDO_PREMIO+i);
	
				if (isinFondo == null)
					break;
	
				if(isinFondo.isNull() && percentualeFondo.isNull() && importoFondo.isNull())
					continue;
	
				idxUltimoComparto = i;
				importoTotaleInvestito = importoTotaleInvestito.add((importoFondo.isNull()?BigDecimal.valueOf(0):importoFondo.bigValue()));
				percentualeTotaleInvestita = percentualeTotaleInvestita.add((percentualeFondo.isNull()?BigDecimal.valueOf(0):percentualeFondo.bigValue()));
			}
	
			if (importoTotaleInvestito.doubleValue() == importoTotale && importoTotale > 0) {
				if (percentualeTotaleInvestita.doubleValue() < 100) {
					DoubleType percentualeFondo = (DoubleType)pdfData.read(PdfDriver.PREFIX_FIELD_PERCENTUALE_FONDO_PREMIO+idxUltimoComparto);
					BigDecimal percentualeArrotondata = BigDecimal.valueOf(100 - (percentualeTotaleInvestita.doubleValue() - percentualeFondo.doubleValue())).setScale(2, BigDecimal.ROUND_HALF_UP);
					pdfData.write(PdfDriver.PREFIX_FIELD_PERCENTUALE_FONDO_PREMIO+idxUltimoComparto, new DoubleType(percentualeArrotondata.doubleValue()));
					
				} else if (percentualeTotaleInvestita.doubleValue() > 100) {
					DoubleType percentualeFondo = (DoubleType)pdfData.read(PdfDriver.PREFIX_FIELD_PERCENTUALE_FONDO_PREMIO+idxUltimoComparto);
					BigDecimal deltaPercentuale = BigDecimal.valueOf(percentualeTotaleInvestita.doubleValue() - 100).setScale(2, BigDecimal.ROUND_HALF_UP);
					pdfData.write(PdfDriver.PREFIX_FIELD_PERCENTUALE_FONDO_PREMIO+idxUltimoComparto, new DoubleType(percentualeFondo.doubleValue() - deltaPercentuale.doubleValue()));
				}
			}
	
			if (percentualeTotaleInvestita.doubleValue() == 100) {
				if (importoTotaleInvestito.doubleValue() < importoTotale){
					DoubleType importoFondo = (DoubleType)pdfData.read(PdfDriver.PREFIX_FIELD_IMPORTO_FONDO_PREMIO+idxUltimoComparto);
					BigDecimal importoArrotondato = BigDecimal.valueOf(importoTotale - (importoTotaleInvestito.doubleValue() - importoFondo.doubleValue())).setScale(2, BigDecimal.ROUND_HALF_UP);
					pdfData.write(PdfDriver.PREFIX_FIELD_IMPORTO_FONDO_PREMIO+idxUltimoComparto, new DoubleType(importoArrotondato));
				} else if (importoTotaleInvestito.doubleValue() > importoTotale){
					DoubleType importoFondo = (DoubleType)pdfData.read(PdfDriver.PREFIX_FIELD_IMPORTO_FONDO_PREMIO+idxUltimoComparto);
					BigDecimal deltaImporto = BigDecimal.valueOf(importoTotaleInvestito.doubleValue() - importoTotale).setScale(2, BigDecimal.ROUND_HALF_UP);
					pdfData.write(PdfDriver.PREFIX_FIELD_IMPORTO_FONDO_PREMIO+idxUltimoComparto, new DoubleType(importoFondo.doubleValue() - deltaImporto.doubleValue()));
				}
			}
		}
		return result;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public PdfVerifiedOutputData onPdfVerified(ClientSessionContext csc, PdfVerifiedInputData input) throws Exception {

		PdfVerifiedOutputData result = new PdfVerifiedOutputData();
		PdfDataModel pdfData = input.getPdfData();
		PdfDataHelper dataHelper = new PdfDataHelper(pdfData);

		//Stampa sempre disabilitata
		result.setInvalidCompilationModes(result.getInvalidCompilationModes() | ModalitaDiSottoscrizione.STAMPA);

		//fisso 0 per la milestone
		pdfData.addProperty(PdfPredefinedFields.IMPORTO, new DoubleType(0));

		PdfPersonModel cliente = readCliente(csc, pdfData, 1);
		if(cliente != null){
			BooleanType isPersonaFisica = (BooleanType)cliente.readProperty("isPersonaFisica");
			if(isPersonaFisica != null) {
				if(isPersonaFisica.booleanValue()){
					int eta = Utility.getEtaCliente(cliente);
					if(eta > 0 && eta < 18){
						result.setInvalidCompilationModes(result.getInvalidCompilationModes() | ModalitaDiSottoscrizione.FIRMA_DIGITALE | ModalitaDiSottoscrizione.COPERNICO);						
						PdfCodedMessage.addWarning(result, Costanti.W_ASSICURANDO_MINORENNE);
					}
				}else{
					result.setInvalidCompilationModes(result.getInvalidCompilationModes() | ModalitaDiSottoscrizione.FIRMA_DIGITALE | ModalitaDiSottoscrizione.COPERNICO);					
					PdfCodedMessage.addWarning(result, Costanti.W_CONTRAENTE_PERSONA_GIURIDICA);
				}
			}
		}

		if(dataHelper.getTipoSOSPENSIONEREVOCARIATTIVAZIONESDD().equals("SOSPENSIONE") || dataHelper.getTipoSOSPENSIONEREVOCARIATTIVAZIONESDD().equals("REVOCA")) {
			if(Utility.isSddAttivoBancaEsterna(csc, dataHelper)) {				
				PdfCodedMessage.addWarning(result, Costanti.W_RICHIESTA_SOSPENSIONE);
			}
		}
	
		if(dataHelper.getTipoIntestazioneContoSDDBMEDVariazione().equals("INTESTATARIO") &&
				dataHelper.getTipoContoINTESTATARIOSDDBMEDVariazione().equals("CCESTERNA") &&
			    !dataHelper.getIbanContoCorrenteCCINTESTATARIOSDDEsternaVariazione().isNull()) {
			result.setInvalidCompilationModes(result.getInvalidCompilationModes() | ModalitaDiSottoscrizione.FIRMA_DIGITALE | ModalitaDiSottoscrizione.COPERNICO);
		}

		if (dataHelper.getTipoIntestazioneContoSDDBMEDVariazione().equals("ALTROCLIENTE")){
			result.setInvalidCompilationModes(result.getInvalidCompilationModes() | ModalitaDiSottoscrizione.FIRMA_DIGITALE | ModalitaDiSottoscrizione.COPERNICO);
			PdfCodedMessage.addWarning(result, Costanti.W_CONTRAENTE_NON_INTESTATARIO_O_COINTESTATARIO);
		}
		
		if (dataHelper.getTipoIntestazioneContoSDDBMEDVariazione().equals(FieldValues.TIPOINTESTAZIONECONTOSDDBMEDVARIAZIONE_INTESTATARIO) && 
				dataHelper.getTipoContoINTESTATARIOSDDBMEDVariazione().equals(FieldValues.TIPOCONTOINTESTATARIOSDDBMEDVARIAZIONE_CCINAPERTURA)){
			result.setInvalidCompilationModes(result.getInvalidCompilationModes() | ModalitaDiSottoscrizione.FIRMA_DIGITALE | ModalitaDiSottoscrizione.COPERNICO);
		}

		doInsertPritDelegato = false;
		if (dataHelper.getTipoIntestazioneContoSDDBMEDVariazione().equals("INTESTATARIO") && dataHelper.getTipoContoINTESTATARIOSDDBMEDVariazione().equals("CC")){
			StringType iban = dataHelper.getIbanContoCorrenteCCINTESTATARIOSDDBMEDVariazione();
			if(!iban.isNull()){
				ContoAutocompleteInput inputConto = new ContoAutocompleteInput();
				inputConto.setNdgFieldName(PdfDriver.NDG_CLIENTE1);
				inputConto.setTipoConto(ContoAutocompleteInput.TIPO_CONTO_CONTO_CORRENTE);
				inputConto.setFieldName("ibanContoCorrenteCCINTESTATARIOSDDBMEDVariazione");
				ContoAutocompleteModel conto = super.readConto(csc, pdfData, inputConto);
				if(conto != null){
					StringType ruolo = (StringType)conto.readProperty("codiceRuoloConto");
					if(ruolo != null && ruolo.equals("D")){
						PdfCodedMessage.addWarning(result, Costanti.W_INTESTATARIO_UNICAMENTE_DELEGATO);
						result.setInvalidCompilationModes(result.getInvalidCompilationModes() | ModalitaDiSottoscrizione.FIRMA_DIGITALE | ModalitaDiSottoscrizione.COPERNICO);
						doInsertPritDelegato = true;
					}
				}
			}
		}
		
		if((dataHelper.getIsVariazioneRipartizione()!= null) && 
				   (!dataHelper.getIsVariazioneRipartizione().isNull())) {
					CollocazioneFondoValidator collocazioneFondoValidator = new CollocazioneFondoValidator(this, PdfEventEnum.PDF_VERIFIED);
					collocazioneFondoValidator.doValidate(csc, pdfData, null);
				}
		//CONTROLLI MOP 2 SOLO PER TEST
		controlliMOPWave(csc, result, pdfData, dataHelper);
		// FINE CONTROLLI MOP 2
		return result;
	}
		
	private void controlliMOPWave(ClientSessionContext csc, PdfVerifiedOutputData result, PdfDataModel pdfData, PdfDataHelper dataHelper) throws Exception {
		if (Utility.isClienteMinorenne(csc, pdfData)) {
			result.setInvalidCompilationModes(result.getInvalidCompilationModes() | ModalitaDiSottoscrizione.FIRMA_DIGITALE | ModalitaDiSottoscrizione.COPERNICO);
			PdfCodedMessage.addWarning(result, Costanti.W_CONTRAENTE_MINORENNE);
		}
		
		if (dataHelper.getTipoRelazioneContraenteTerzoPagatore().equals(Costanti.TIPO_RELAZIONE_ALTRO)
				&& !dataHelper.getDescrizioneTipoRelazioneContraenteTerzoPagatore().isNull()) {			
			PdfCodedMessage.addWarning(result, Costanti.W_RELAZIONE_TERZO_PAGATORE_ALTRO);
		}
	}
	
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public FreezeEventOutputData onPdfFreeze(ClientSessionContext csc, FreezeEventInputData input) throws Exception {
		FreezeEventOutputData result = new FreezeEventOutputData();
		
		if(getPdf().isTipoProcessoSedeMOM2()) // Per MOM2 non accodiamo sul postcompl
			result.setPutOnPostCompletionProcessBatchQueue(false);
		
		clearCache();
		return result;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public BeforePreviewOutputData onPdfPreview(ClientSessionContext csc, BeforePreviewInputData input) throws Exception {
		BeforePreviewOutputData result = new BeforePreviewOutputData();
		result.setFacSimileOnPreview(true);
		return result;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public FinalEventOutputData onPdfCompleted(ClientSessionContext csc, FinalEventInputData input) throws Exception {

		FinalEventOutputData result = new FinalEventOutputData();

		/* settaggio code a cui inviare la dispositiva */
		result.setPutOnPostCompletionProcessBatchQueue(true);

		return result;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public ProvidePritDataResponse providePritData(ClientSessionContext csc, ProvidePritDataRequest input) throws Exception {

		ProvidePritDataResponse result = new ProvidePritDataResponse();
		PdfDataModel pdfData = input.getPdfData();
		PdfDataHelper dataHelper = new PdfDataHelper(pdfData);

		if(doInsertPritDelegato){
			RigaPrit rigaPrit = new RigaPrit();
			rigaPrit.setChiavePrit("VRPAC_PERSONALPIR_MANLEVA_DELEGATO");
			rigaPrit.setNumeroContratto(dataHelper.getNumeroContratto().toString());
			result.addRigaDiPrit(rigaPrit);
		}

		return result;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public ProvideReportAdeguatezzaDataResponse provideReportAdeguatezzaData(ClientSessionContext csc, ProvideReportAdeguatezzaDataRequest input)
	throws Exception {

		PdfDataModel pdfData = input.getPdfData();
		PdfDataHelper dataHelper = new PdfDataHelper(pdfData);

		if(!dataHelper.getIsVariazioneRipartizione().equals("SI"))
			return null;

		ProvideReportAdeguatezzaDataResponse result = new ProvideReportAdeguatezzaDataResponse();
		DispositivaReportAdeguatezzaModel d = result.getDispositiva();
		d.setProdotto(new StringType(Costanti.TARIFFA_PAC));
		d.setContratto(dataHelper.getNumeroPolizza());
		d.setOperazione(new StringType(Costanti.REPORTADEGUATEZZA_OPERAZIONE_VARIAZIONE));
		d.setDescr(new StringType(Utility.recuperaDescrizioneProdottoReportAdeguatezza(csc)));

		DoubleType importoResiduo = readResiduoPolizza(csc, dataHelper.getCodProdottoPolizza(), dataHelper.getNumeroContratto()).getResiduo();
		for (int i=0; ; i++){
			StringType isinFondo = (StringType)pdfData.readProperty(PdfDriver.PREFIX_FIELD_ISIN_FONDO_PREMIO+i);
			if (isinFondo == null)
				break;
			if (isinFondo.isNull())
				continue;
			ProdottoReportAdeguatezzaModel p = new ProdottoReportAdeguatezzaModel();
			p.setDoProdottoDefaultTranslation(false);
			p.setDescr((StringType)pdfData.readProperty(PdfDriver.PREFIX_FIELD_DESCRIZIONE_FONDO_PREMIO+i));
			DoubleType perc = (DoubleType)pdfData.readProperty(PdfDriver.PREFIX_FIELD_PERCENTUALE_FONDO_PREMIO+i);
			double importoFondo = importoResiduo.doubleValue()*perc.doubleValue()/100;
			p.setVariazione(new DoubleType(importoFondo));
			p.setProdotto(new StringType(Utility.recuperaAnagraficaProdottoFondoReportAdeguatezza(csc, isinFondo.toString(), Costanti.TARIFFA_PAC)));
			d.addProdotto(p);
		}

		return result;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public ProvideMifidDataResponse provideMifidData(ClientSessionContext csc, ProvideMifidDataRequest input) throws Exception {

		PdfDataModel pdfData = input.getPdfData();
		PdfDataHelper dataHelper = new PdfDataHelper(input.getPdfData());

		if(!dataHelper.getIsVariazioneRipartizione().equals("SI"))
			return null;

		ProvideMifidDataResponse output = new ProvideMifidDataResponse();
		output.addCliente(input.getPerson(1));
		if(!dataHelper.getIbanContoCorrenteCCINTESTATARIOSDDBMEDVariazione().isNull()) {
			output.setContoDiAddebito(dataHelper.getIbanContoCorrenteCCINTESTATARIOSDDBMEDVariazione());
		}else if(!dataHelper.getIbanContoCorrenteALTROCLIENTESDDBMEDVariazione().isNull()) {
			output.setContoDiAddebito(dataHelper.getIbanContoCorrenteALTROCLIENTESDDBMEDVariazione());
		}

		OperazioneMifidModel acquisto = new OperazioneMifidModel();

		DoubleType importoResiduo = readResiduoPolizza(csc, dataHelper.getCodProdottoPolizza(), dataHelper.getNumeroContratto()).getResiduo();
		for (int i=0; ; i++){
			StringType isinFondo = (StringType)pdfData.readProperty(PdfDriver.PREFIX_FIELD_ISIN_FONDO_PREMIO+i);
			if (isinFondo == null)
				break;
			if (isinFondo.isNull())
				continue;	
			CompartoMifidModel comparto = new CompartoMifidModel();
			StringType codiceFondo = (StringType)pdfData.readProperty(PdfDriver.PREFIX_FIELD_CODICE_FONDO_PREMIO+i);
			comparto.setCodiceFondo(codiceFondo);
			comparto.setCodiceTipoProdotto(new StringType("PV"));
			comparto.setDivisa(new StringType("EUR"));
			comparto.setTariffa(new StringType(Costanti.TARIFFA_PAC));
			DoubleType perc = (DoubleType)pdfData.readProperty(PdfDriver.PREFIX_FIELD_PERCENTUALE_FONDO_PREMIO+i);
			double importoFondo = importoResiduo.doubleValue()*perc.doubleValue()/100;
			comparto.setImporto(new DoubleType(importoFondo));
			acquisto.addComparto(comparto);
		}

		output.addAcquisto(acquisto);

		return output;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public ProvideBasketDataResponse provideBasketData(ClientSessionContext csc, ProvideBasketDataRequest input) throws Exception {

		ProvideBasketDataResponse result = new ProvideBasketDataResponse();

		PdfDataModel pdfData = input.getPdfData();

		result.setTipoOperazione("");
		result.setTipoControlloSaldo(ProvideBasketDataResponse.TIPO_CONTROLLO_SALDO_NESSUNO);
		result.setNumeroContoControlloSaldo(new StringType());
		result.setImportoControlloSaldo(new DoubleType(((DoubleType)pdfData.read(PdfPredefinedFields.IMPORTO)).doubleValue()));

		return result;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	@Override
	public VerifyCopernicoPdfOutputData onVerifyCopernicoPdf(ClientSessionContext csc, VerifyCopernicoPdfInputData input) throws Exception {

		VerifyCopernicoPdfOutputData result = new VerifyCopernicoPdfOutputData();
		PdfDataModel pdfData = input.getPdfData();
		PdfDataHelper dataHelper = new PdfDataHelper(pdfData);

		// rfc #257299 - aggiunta Sezione 2 al modulo. Se esiste il nuovo campo "formaContrattuale" e vale PIC allora non ci sono controlli.
		if(pdfData.fieldExist(PdfDataHelper.FieldNames.FORMACONTRATTUALE) && dataHelper.getFormaContrattuale().equals("PIC")) 				
			return result;
		
		PdfCopernicoValidator validator = new PdfCopernicoValidator(this, PdfEventEnum.VERIFY_COPERNICO_PDF);
		validator.validate(csc, pdfData, result);

		result.addWarnings(validator.getWarnings());
		result.addErrors(validator.getErrors());

		return result;
	}
	
	@Override 
	public ProvideSquadraDataResponse provideSquadraData(ClientSessionContext csc, ProvideSquadraDataRequest input) throws Exception {

		ProvideSquadraDataResponse res = new ProvideSquadraDataResponse();
		
		ElementoSquadra elSq = new ElementoSquadra();		
		elSq.setNomeCampoNdg(PdfDriver.NDG_CLIENTE1);
		elSq.setNomeCampoIdCensimento(PdfDriver.ID_CENSIMENTO_CLIENTE1);
		elSq.setNomiCampoErrore(PdfDriver.NDG_CLIENTE1);		
		elSq.setRuoli(ElementoSquadra.Ruoli.SOTTOSCRITTORE );	
	
		res.getSquadra().add(elSq);
		
		elSq = new ElementoSquadra();
		elSq.setNomeCampoNdg(PdfDriver.FIELD_NDG_ALTROCLIENTESDDBMED_VARIAZIONE);		
		elSq.setNomiCampoErrore(PdfDriver.FIELD_NDG_ALTROCLIENTESDDBMED_VARIAZIONE);
		elSq.setRuoli(ElementoSquadra.Ruoli.TERZOPAGATORE);
		res.getSquadra().add(elSq);		     
		
		return res;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isOperatoreMOM() {
		return getPdf().isOperatoreMOM();
	}
	
	@Override
	public PdfModel getPdf() {
		return super.getPdf();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public ResiduoPolizzaService getResiduoPolizzaService() {
		return residuoPolizzaService;
	}

	
	/***********************************************************************************************/
	/**
	 * @throws Exception
	 *********************************************************************************************/
	public ResiduoPolizzaModel readResiduoPolizza(ClientSessionContext csc, StringType codProdotto, StringType numeroContratto,
			boolean async) throws Exception {
		return residuoPolizzaService.read(csc, codProdotto, numeroContratto, async);
	}

	/***********************************************************************************************/
	/**
	 * @throws Exception
	 *********************************************************************************************/
	public ResiduoPolizzaModel readResiduoPolizza(ClientSessionContext csc, StringType codProdotto, StringType numeroContratto)
			throws Exception {
		return residuoPolizzaService.read(csc, codProdotto, numeroContratto, false);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void clearCache() {
		residuoPolizzaService.clearCache();
		opEstero.clearCache();
	}
	
	public void ctrlOperativitaResidentiEstero(ClientSessionContext csc, PdfDataModel pdfData) throws Exception {		
		try {
						
			for (int i = 1;; i++) {		
				String ndgClienteFieldName = String.format("%s%d",PdfDriver.PROPERTY_NDG_CLIENTE,i);
				
				StringType ndgCliente = (StringType) pdfData.read(ndgClienteFieldName);			
				
				if (ndgCliente == null || ndgCliente.isNull() || i > 1) {
					break;
				}
				ctrlNdgClienteOperativaEstero(csc, pdfData, ndgClienteFieldName);			
			}
		}
		catch (DAOException e) {
			throw new Exception(e);
		}	
	}
	
	public boolean isLegaleRapp(PdfDataModel pdfData, String ndgFieldName) {
		BooleanType isLegaleRapp = (BooleanType)pdfData.read(String.format("isCliente%sLegaleRappresentante", ndgFieldName.substring(ndgFieldName.length()-1)));
		if (isLegaleRapp == null) {
			return false;
		}
		return isLegaleRapp.booleanValue();
	}
	

	public void ctrlNdgClienteOperativaEstero(ClientSessionContext csc, PdfDataModel pdfData, String ndgFieldName)
			throws Exception, DAOException {
		boolean isLegaleRapp = isLegaleRapp(pdfData, ndgFieldName);
		if (!isLegaleRapp) {
						
			int indexNdgCliente = extractIndex(ndgFieldName);
			ControlliUSPersonModel ctrlOpEsteroModel = new ControlliUSPersonModel();
			ctrlOpEsteroModel.setFamigliaProdotto(new StringType(ControlloOperativitaResidentiEstero.FAMIGLIA_PRODOTTO_POLIZZE_MEDVITA_PIR));
			ctrlOpEsteroModel.setTipoOperazione(new StringType(ControlloOperativitaResidentiEstero.TIPO_OPERAZIONE_VARIAZIONE_PIANO_PAC));
			
		    opEstero.validate(csc, pdfData, indexNdgCliente, ctrlOpEsteroModel);
		}
	}
	
	
	
	public int extractIndex(String fieldName) {
	    // Rimuove il prefisso "ndgCliente" e converte il resto in un numero intero
	    try {
	        return Integer.parseInt(fieldName.replace(PROPERTY_NDG_CLIENTE, ""));
	    } catch (NumberFormatException e) {
	        return -1; 
	    }
	}
}
