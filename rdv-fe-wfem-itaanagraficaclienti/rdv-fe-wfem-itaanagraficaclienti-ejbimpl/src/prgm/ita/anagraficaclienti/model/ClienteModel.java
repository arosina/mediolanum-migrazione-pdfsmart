package prgm.ita.anagraficaclienti.model;

import java.util.Calendar;
import java.util.Date;

import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;
import com.atosorigin.wfem.util.Tools;

import prgm.ita.anagraficaclienti.cogestione.CogestioneDataModel;
import prgm.ita.anagraficaclienti.facade.Costanti;
import prgm.ita.anagraficaclienti.facade.MappaturaTabelle;
import prgm.ita.anagraficaclienti.facade.StatiAnagraficaCliente;
import prgm.ita.anagraficaclienti.facade.StatiPropostaAnagrafica;
import prgm.ita.anagraficaclienti.pcp.ProfiloPcpClienteModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class ClienteModel extends ClienteKeyModel{
	
	private static final long serialVersionUID = 1L;
	
	private static final String S_NOME = "nome";
	private static final String S_COGNOME  = "cognome";
	private static final String S_SESSO = "sesso";
	private static final String S_DATANASCITA = "dataNascita";
	private static final String S_CODNAZIONE = "codNazione";
	private static final String S_COMUNE = "comune";
	private static final String S_COMUNENASCITA = "comuneNascita";
	private static final String S_RESIDENZA_INDIRIZZO = "residenza_indirizzo";
	private static final String S_INFOPERSONALI = "infoPersonali";
	private static final String S_CODSTATOCIVILE = "codStatoCivile";
	private static final String S_CAP = "cap";	
	private static final String S_TOPONIMOINDIRIZZO = "toponimoIndirizzo";
	private static final String S_DESCRIZIONEINDIRIZZO = "descrizioneIndirizzo";
	private static final String S_NUMEROCIVICO = "numeroCivico";
	private static final String S_CODPROFESSIONE = "codProfessione";
	private static final String S_CODSETTOREECONOMICO = "codSettoreEconomico";
	private static final String S_HAFIGLI = "haFigli";
	private static final String S_CODTITOLODISTUDIO = "codTitoloStudio";
	private static final String S_NUMEROFAMILIARI = "numeroFamiliari";
	
	private String			concurrencyViolationFoundedWidth = null;
	private TimestampType 	dataOraUltimaModifica = new TimestampType();
	private StringType 		dataOraUltimaModificaAsString = new StringType();
	private StringType 		codAgenteUltimaModifica	= new StringType();

	private CogestioneDataModel	cogestioneData = new CogestioneDataModel();
	private BooleanType			isClienteInCogestione = new BooleanType();
	
	private boolean			onStampaBozzaAction = false;
	private BooleanType 	isDaInviareAllaFabbrica = new BooleanType(true);
	
	private StringType 		numeroOrdine	= new StringType();	// Numero carta chimica / barcode / prekit
	
	private BooleanType isDipendente = new BooleanType();
	private BooleanType isCancellabile = new BooleanType();
	
	private DatiApplicativiModel datiApplicativi = new DatiApplicativiModel();	

	private StringType 		codRete			= new StringType();
	private StringType  	stato 			= new StringType();
	private StringType  	statoProposta	= new StringType();
	private IntegerType 	progressivo 	= new IntegerType();
	private StringType  	serverReplica	= new StringType();    
	private TimestampType 	dataInserimento = new TimestampType();
	private TimestampType 	dataVariazione  = new TimestampType();
	private StringType 		numeroCelluarePrimario = new StringType();
	
	private StringType  	codCluster 			= new StringType();	
	private BooleanType  	isTopBusiness		= new BooleanType();
	
	private ProfiloPcpClienteModel	profiloPcp = new ProfiloPcpClienteModel();
	
	private IntegerType 	versioneQuestionario		= new IntegerType();
	private StringType  	codProfiloDiInvestimento	= new StringType();	
	private StringType  	codClusterCedacri 			= new StringType();	
	private StringType  	descrClusterCedacri 		= new StringType();	
	private StringType   	dtCompQuestCedacri  		= new StringType();     // data compilazione questionario
	private IntegerType   	dFinValCedacri  			= new IntegerType();
	private StringType 		seValid      				= new StringType();
	private StringType 		espfina	   					= new StringType();		// Output da servizio "getProfilo" CEDACRI

	private StringType  	codNazl				= new StringType();	
	private StringType  	cittadinanza		= new StringType(Costanti.COD_UIC_NAZIONE_ITALIA);	
	private StringType  	secondaCittadinanza	= new StringType();	
	
	private StringType  	statoConfermato = new StringType();
	private IntegerType 	numVariazioni = new IntegerType();

	private StringType  	tipoPersona 			= new StringType(Costanti.TIPO_PERSONA_FISICA);
	private StringType 		tipoDitta				= new StringType();
	private StringType  	naturaGiuridica			= new StringType(); // Campo DB calcolato
	private StringType  	secondoNome 			= new StringType(); 
	private StringType  	secondaIntestazione		= new StringType();
	private StringType  	sesso 					= new StringType();
	
	private DateType    	dataNascita 			= new DateType();
    private ComuneModel 	comuneNascita			= new ComuneModel();
   
	private AgenteModel					agente 				= new AgenteModel();
	private ResidenzaModel				residenza 			= new ResidenzaModel();
	private DomicilioModel				domicilio 			= new DomicilioModel();
	private InfoPersonaliModel 			infoPersonali 		= new InfoPersonaliModel();
	private DocumentoModel 	 			documento 			= new DocumentoModel();
	private InfoDittaModel 				infoDitta 			= new InfoDittaModel();
	private DatiPrivacyModel		 	datiPrivacy 		= new DatiPrivacyModel();
	private IndirizziModel				indirizzi 			= new IndirizziModel();
	private TelefoniModel				telefoni 			= new TelefoniModel();
	private DatiMarketingModel			datiMarketing 		= new DatiMarketingModel();
	private DatiSedeModel				datiSede			= new DatiSedeModel();
	private RecapitiModel				recapiti 			= new RecapitiModel();
	private AdempimentiNormativiModel 	adempimentiNormativi= new AdempimentiNormativiModel();
	private VariazioniModel				variazioni 			= new VariazioniModel();
	private ClienteModel				clienteTitolare		= null;

	private FotografiaModel			fotografia			= new FotografiaModel();
	private ListType				elencoFotografie	= new ListType(FotografiaModel.class);
	
	private DatiStampa				datiStampa = new DatiStampa();
	
	private boolean 	almenoUnaProvinciaScaduta = false;
	private BooleanType titolareContoDepositoAttivo = new BooleanType();
	private StringType 	domicilioDiversoDaResidenza = new StringType();
	
	private DateType scadenzaDocTaxUS = new DateType();

	private StringType		moduloFatca = new StringType();
	
	private TimestampType 	dataVariazioneFirmaDigitale  = new TimestampType();	// Utilizzato per escludere le anagrafiche inviate a CSC (forse)
	
	// FATCA ************************************************************
	private boolean 		saltaPritAllInvioInSede = false;
	private boolean		 	saltaControlliAllInvioInSede = false;
	private BooleanType 	isFlussoFatcaTerminato = new BooleanType();
	private DatiFatcaModel 	datiFatca = new DatiFatcaModel();
	private String			firstFatcaPopupName = null;
	// ******************************************************************
	
	// Modelli Servizio Cliente
	private ListType differenziali = new ListType(DifferenzialeClienteModel.class);
	
	private InfoSocietarieModel infoSocietarie = new InfoSocietarieModel();
		
	/***********************************************************************************************/
	/***********************************************************************************************/
	public ClienteModel(){

		addCodDescField("codCluster","CLUSTER");
		addCodDescField("cittadinanza","NAZIONIUIC");
		addCodDescField("secondaCittadinanza","NAZIONIUIC");

	    // I comuni di nascita sono solo quelli iscritti al catasto
	    getComuneNascita().setIsIscrittoAlCatasto(new BooleanType(true));
	    
		setUploadMaxSize(1024*750);
	}	

	// Campi deprecati
	/**
	 * @deprecated
	 */
	@Deprecated
	public ComuneModel getComuneNascitaItalia() {
		return getComuneNascita();
	}
	/**
	 * @deprecated
	 */
	@Deprecated
	public StringType getCodNazioneNascita() {
		return getComuneNascita().getCodNazione();
	}
	/**
	 * @deprecated
	 */
	@Deprecated
	public StringType getComuneNascitaEstero() {
		return getComuneNascita().getComuneEstero();
	}
	/********************************************************************/

	/***********************************************************************************************/
	/***********************************************************************************************/	
	public StringType getCodiceClienteFatca(){
		if(getIsPotenziale().booleanValue())
			return new StringType("99999999999");
		else
			return getCodMediolanum();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/	
	public StringType getTipoSoggettoFatca(){
		if(getIsPotenziale().booleanValue())
			return new StringType("P");
		else
			return new StringType("C");
	}
	
	
	/***********************************************************************************************/
	/***********************************************************************************************/	
	public StringType getDenominazioneDitta() {
		if (!getIsDitta().booleanValue())
			return new StringType();
		
		if(!getSecondaIntestazione().isNull())
			return new StringType(getSecondaIntestazione().toString());
		else
			return new StringType((getCognome().toString() + " " + getNomeCompleto().toString()));
	}

	/***********************************************************************************************/
	/***********************************************************************************************/	
	public StringType getNomeCompleto() {
		if (getSecondoNome().isNull())
			return new StringType(getNome().toString());
		else
			return new StringType((getNome().toString() + " " + getSecondoNome().toString()));
	}

	/***********************************************************************************************/
	/***********************************************************************************************/	
	public StringType getLuogoSvolgimentoAttivita() {
		if(getInfoPersonali().getProvinciaSvolgimentoProfessione().isNull())
			return new StringType(getInfoPersonali().getDescValue("nazioneSvolgimentoProfessione"));
		else
			return new StringType(getInfoPersonali().getDescValue("nazioneSvolgimentoProfessione")+" - "+getInfoPersonali().getDescValue("provinciaSvolgimentoProfessione"));
	}

	/***********************************************************************************************/
	/***********************************************************************************************/	
	public DateType getDataScadenzaProfiloCedacri(){		
		try{
			String  d = getDFinValCedacri().toString();
			return new DateType(d.substring(6)+"-"+d.substring(4,6)+"-"+d.substring(0,4));
		}catch(Exception e){
			return new DateType();
		}
	}
	/***********************************************************************************************/
	/***********************************************************************************************/	
	public DateType getDataCompilazioneQuesitonarioCedacri(){		
		try{
			String  d = getDtCompQuestCedacri().toString();
			return new DateType(d.substring(6)+"-"+d.substring(4,6)+"-"+d.substring(0,4));
		}catch(Exception e){
			return new DateType();
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void initialize(){
		
	    comuneNascita	= new ComuneModel();
	    comuneNascita.setIsIscrittoAlCatasto(new BooleanType(true));
	    
		agente 				= new AgenteModel();
		residenza 			= new ResidenzaModel();
		domicilio 			= new DomicilioModel();
		infoPersonali 		= new InfoPersonaliModel();
		documento 			= new DocumentoModel();
		datiPrivacy 		= new DatiPrivacyModel();
		indirizzi 			= new IndirizziModel();
		telefoni 			= new TelefoniModel();
		datiMarketing 		= new DatiMarketingModel();
		datiSede			= new DatiSedeModel();
		variazioni 			= new VariazioniModel();		
		datiStampa 			= new DatiStampa();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String fch(String propName){
		return fch("",propName);
	}
	
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String fch(String modelName, String propName){
		if(!getDatiApplicativi().isVariazione()){
			String background = "background-image:url('/ItaAnagraficaClienti/images/pem.gif');background-repeat:repeat;";
			if(getCallingAppl().toString().equals(Costanti.CALLING_APPL_MHD)){
				
				if((modelName.equals("") && propName.equals(S_NOME)) ||
				   (modelName.equals("") && propName.equals(S_COGNOME)) ||
				   (modelName.equals("") && propName.equals(S_SESSO)) ||
				   (modelName.equals("") && propName.equals(S_DATANASCITA)) ||
				   (modelName.equals(S_COMUNENASCITA) && propName.equals(S_CODNAZIONE)) ||
				   (modelName.equals(S_COMUNENASCITA) && propName.equals(S_COMUNE)) ||
				   (modelName.equals(S_RESIDENZA_INDIRIZZO) && propName.equals(S_CODNAZIONE)) ||
				   (modelName.equals(S_RESIDENZA_INDIRIZZO) && propName.equals(S_CAP)) ||
				   (modelName.equals(S_RESIDENZA_INDIRIZZO) && propName.equals(S_COMUNE)) ||
				   (modelName.equals(S_INFOPERSONALI) && propName.equals(S_CODSTATOCIVILE)))
					return background;
				
			}else if(getCallingAppl().toString().equals(Costanti.CALLING_APPL_TOOLPREVIDENZA)){
				
				if((modelName.equals("") && propName.equals(S_NOME)) ||
				   (modelName.equals("") && propName.equals(S_COGNOME)) ||
				   (modelName.equals("") && propName.equals(S_SESSO)) ||
				   (modelName.equals("") && propName.equals(S_DATANASCITA)) ||
				   (modelName.equals(S_RESIDENZA_INDIRIZZO) && propName.equals(S_CODNAZIONE)) ||
				   (modelName.equals(S_RESIDENZA_INDIRIZZO) && propName.equals(S_CAP)) ||
				   (modelName.equals(S_RESIDENZA_INDIRIZZO) && propName.equals(S_COMUNE)) ||
				   (modelName.equals(S_RESIDENZA_INDIRIZZO) && propName.equals(S_TOPONIMOINDIRIZZO)) ||
				   (modelName.equals(S_RESIDENZA_INDIRIZZO) && propName.equals(S_DESCRIZIONEINDIRIZZO)) ||
				   (modelName.equals(S_RESIDENZA_INDIRIZZO) && propName.equals(S_NUMEROCIVICO)) ||
				   (modelName.equals(S_INFOPERSONALI) && propName.equals(S_CODPROFESSIONE)) ||
				   (modelName.equals(S_INFOPERSONALI) && propName.equals(S_CODSETTOREECONOMICO)) ||
				   (modelName.equals(S_INFOPERSONALI) && propName.equals(S_CODSTATOCIVILE)) ||
				   (modelName.equals(S_INFOPERSONALI) && propName.equals(S_HAFIGLI)) ||
				   (modelName.equals(S_INFOPERSONALI) && propName.equals(S_CODTITOLODISTUDIO)))
					return background;
				
			}else if(getCallingAppl().toString().equals(Costanti.CALLING_APPL_TOOLPROTEZIONE)){
				
				if((modelName.equals("") && propName.equals(S_NOME)) ||
				   (modelName.equals("") && propName.equals(S_COGNOME)) ||
				   (modelName.equals("") && propName.equals(S_SESSO)) ||
				   (modelName.equals("") && propName.equals(S_DATANASCITA)) ||
				   (modelName.equals(S_RESIDENZA_INDIRIZZO) && propName.equals(S_CODNAZIONE)) ||
				   (modelName.equals(S_RESIDENZA_INDIRIZZO) && propName.equals(S_CAP)) ||
				   (modelName.equals(S_RESIDENZA_INDIRIZZO) && propName.equals(S_COMUNE)) ||
				   (modelName.equals(S_RESIDENZA_INDIRIZZO) && propName.equals(S_TOPONIMOINDIRIZZO)) ||
				   (modelName.equals(S_RESIDENZA_INDIRIZZO) && propName.equals(S_DESCRIZIONEINDIRIZZO)) ||
				   (modelName.equals(S_RESIDENZA_INDIRIZZO) && propName.equals(S_NUMEROCIVICO)) ||
				   (modelName.equals(S_INFOPERSONALI) && propName.equals(S_CODSTATOCIVILE)) ||
				   (modelName.equals(S_INFOPERSONALI) && propName.equals(S_NUMEROFAMILIARI)))
					return background;
				
			}else if(getCallingAppl().toString().equals(Costanti.CALLING_APPL_TOOLDOSSIERTITOLI)){
				{
				if((modelName.equals("") && propName.equals(S_NOME)) ||
				   (modelName.equals("") && propName.equals(S_COGNOME)) ||
				   (modelName.equals("") && propName.equals(S_SESSO)))
					return background;
				}
			}
			
		}else{
			
			if(propChanged(modelName, propName, false))
				return "background-image:url('/ItaAnagraficaClienti/images/chsf.gif');background-repeat:repeat;";
			
		}
		
		return "";
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public boolean somePropChanged(String[] propNames){
		for(int i=0;i<propNames.length;i++){
			if(propChanged("", propNames[i], true))
				return true;
		}
		return false;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean propChanged(String modelName, String propName, boolean onClienteCaricato) {
		try{
			
			AbstractSectionModel model = this;
			if(!modelName.equals(""))
				model = (AbstractSectionModel)Tools.getPropertyValue(model,modelName);
			AbstractSectionModel otherModel = onClienteCaricato ? getDatiApplicativi().getClienteCaricato() : getDatiApplicativi().getClienteOriginale();
			if(otherModel == null)
				return false;
			if(!modelName.equals(""))
				otherModel = (AbstractSectionModel)Tools.getPropertyValue(otherModel,modelName);

			AbstractType prop = (AbstractType)Tools.getPropertyValue(model,propName);
			AbstractType originalProp = (AbstractType)Tools.getPropertyValue(otherModel,propName);
			
			return !prop.toString().equals(originalProp.toString());
			
		}catch(Exception e){
			return false;
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public BooleanType getIsPropostaInviataInSede(){
		if(getStatoProposta().equals(StatiPropostaAnagrafica.INVIATA_IN_SEDE))
			return new BooleanType(true);
		else
			return new BooleanType(false);
	}	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public BooleanType getIsProspect(){
		if(getDatiApplicativi().getNomeTabella().equals(MappaturaTabelle.CLI_PROSPECT))
			return new BooleanType(true);
		else
			return new BooleanType(false);
	}	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public BooleanType getIsBozza(){
		if(getStatoProposta().equals(StatiPropostaAnagrafica.BOZZA))
			return new BooleanType(true);
		else
			return new BooleanType(false);
	}	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public BooleanType getIsPotenziale(){
		if(getCodMediolanum().isNull())
			return new BooleanType(true);
		else
			return new BooleanType(false);
	}	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public BooleanType getIsEffettivo(){
		if(!getCodMediolanum().isNull())
			return new BooleanType(true);
		else
			return new BooleanType(false);
	}	


	/***********************************************************************************************/
	/***********************************************************************************************/
	public BooleanType getIsPersonaGiuridica(){
		if(sesso.toString().equalsIgnoreCase(Costanti.SESSO_SOCIETA))
			return new BooleanType(true);
		else
			return new BooleanType(false);
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public BooleanType getIsPersonaFisica(){
		if(getIsDitta().booleanValue())
			return new BooleanType();
		boolean isGiuridica = getIsPersonaGiuridica().booleanValue();
		return new BooleanType(!isGiuridica);
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public BooleanType getIsDitta(){
		if(getDatiApplicativi().isCensimentoDitta())
			return new BooleanType(true);
		if(naturaGiuridica.equals(Costanti.NATURA_GIURIDICA_DITTA_FEMMINA) ||
		   naturaGiuridica.equals(Costanti.NATURA_GIURIDICA_DITTA_MASCHIO))
			return new BooleanType(true);
		else
			return new BooleanType(false);
	}
	

	/***********************************************************************************************/
	/***********************************************************************************************/
	public BooleanType getIsMaschio(){
		if(sesso.toString().equalsIgnoreCase(Costanti.SESSO_MASCHIO))
			return new BooleanType(true);
		else
			return new BooleanType(false);
	}	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public BooleanType getIsFemmina(){
		if(sesso.toString().equalsIgnoreCase(Costanti.SESSO_FEMMINA))
			return new BooleanType(true);
		else
			return new BooleanType(false);
	}	


	/***********************************************************************************************/
	/***********************************************************************************************/
	public BooleanType getIsEffettivoRiassegnato(){
		if(stato.equals(StatiAnagraficaCliente.CLIENTE_EFFETTIVO_RIASSEGNATO))
			return new BooleanType(true);
		else
			return new BooleanType(false);
	}	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public BooleanType getIsEffettivoPersonale(){
		if(stato.equals(StatiAnagraficaCliente.CLIENTE_EFFETTIVO_PERSONALE))
			return new BooleanType(true);
		else
			return new BooleanType(false);
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public BooleanType getIsAcquisito(){
		if(stato.equals(StatiAnagraficaCliente.CLIENTE_ACQUISITO))
			return new BooleanType(true);
		else
			return new BooleanType(false);
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public BooleanType getIsCointestatarioNonAssegnato(){
		if(stato.equals(StatiAnagraficaCliente.CLIENTE_COINTESTATARIO_NON_ASSEGNATO))
			return new BooleanType(true);
		else
			return new BooleanType(false);
	}
	/***********************************************************************************************/
	/***********************************************************************************************/	
	public BooleanType getIsAssegnatoAdAltroAgente(){
		if(stato.equals(StatiAnagraficaCliente.CLIENTE_ASSEGNATO_AD_ALTRO_AGENTE))
			return new BooleanType(true);
		else
			return new BooleanType(false);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public IntegerType getEta(){
		
		if(getDataNascita().isNull())
			return new IntegerType();
		
		Date dn = getDataNascita().dateValue();

	    int age = 0;
	    Calendar birthdate = Calendar.getInstance();
	    birthdate.setTime(dn);

	    Calendar now = Calendar.getInstance();
	    age = now.get(Calendar.YEAR) - birthdate.get(Calendar.YEAR);
	    birthdate.add(Calendar.YEAR, age);
	    if (now.before(birthdate))
	      age--;
	    return new IntegerType(age);
		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public StringType getStatoProposta() {
		if(statoProposta.equals("4"))
			return new StringType(StatiPropostaAnagrafica.INVIATA_IN_SEDE);
		return statoProposta;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public StringType getDescrCluster() {
		return new StringType(getDescValue("codCluster"));
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public TelefonoModel getTelefonoFaxCasa(){
		return getTelefonoTipo(Costanti.TIPO_TELEFONO_FAX_CASA);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public TelefonoModel getTelefonoFaxUfficio(){
		return getTelefonoTipo(Costanti.TIPO_TELEFONO_FAX_UFFICIO);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public TelefonoModel getTelefonoCellulare(){
		return getTelefonoTipo(Costanti.TIPO_TELEFONO_CELLULARE);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public TelefonoModel getTelefonoUfficio(){
		return getTelefonoTipo(Costanti.TIPO_TELEFONO_UFFICIO);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private TelefonoModel getTelefonoTipo(String tipoTel){
		ListType tels = getTelefoni().getElencoAttributi();
		for(int i=0;i<tels.size();i++){
			TelefonoModel telefono = (TelefonoModel)tels.get(i);
			if(telefono.getTipoTelefono().equals(tipoTel))
				return telefono;
		}
		return new TelefonoModel();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void initDatiDittaFromTitolare(ClienteModel titolare){
		setSesso(titolare.getSesso());
		setNome(titolare.getNome());
		setCognome(titolare.getCognome());
		setCodFiscale(titolare.getCodFiscale());
		setDataNascita(titolare.getDataNascita());
		setComuneNascita(titolare.getComuneNascita());
		setDocumento(titolare.getDocumento());
		setNaturaGiuridica(new StringType("S"+getSesso().toString().toUpperCase()+getSesso().toString().toUpperCase()));
		getInfoPersonali().setCodSettoreEconomico(titolare.getInfoPersonali().getCodSettoreEconomico());
		getInfoPersonali().setCodProfessionePrecedente(titolare.getInfoPersonali().getCodProfessionePrecedente());
		getInfoPersonali().setCodProfessione(titolare.getInfoPersonali().getCodProfessione());
	}

	/********************************************************************************/
	/********************************************************************************/
	public boolean documentoChanged() {
		return somePropChanged(new String[]{
				"documento_tipoDocumento",
				"documento_numeroDocumento",
				"documento_luogoRilascio_comune",
				"documento_dataRilascio",
				"documento_dataScadenza"
		});
	}

	public BooleanType getIsCancellabile() {
		return isCancellabile;
	}

	public void setIsCancellabile(BooleanType isCancellabile) {
		this.isCancellabile = isCancellabile;
	}

	public DateType getDataNascita() {
		return dataNascita;
	}

	public StringType getSecondoNome() {
		return secondoNome;
	}

	public StringType getSesso() {
		return sesso;
	}

	public StringType getStato() {
		return stato;
	}

	public void setDataNascita(DateType dataNascita) {
		this.dataNascita = dataNascita;
	}

	public void setSecondoNome(StringType secondoNome) {
		this.secondoNome = secondoNome;
	}

	public void setSesso(StringType sesso) {
		this.sesso = sesso;
	}

	public void setStato(StringType stato) {
		this.stato = stato;
	}

	public DatiMarketingModel getDatiMarketing() {
		return datiMarketing;
	}

	public DatiPrivacyModel getDatiPrivacy() {
		return datiPrivacy;
	}

	public IndirizziModel getIndirizzi() {
		return indirizzi;
	}

	public TelefoniModel getTelefoni() {
		return telefoni;
	}

	public VariazioniModel getVariazioni() {
		return variazioni;
	}

	public void setDatiMarketing(DatiMarketingModel datiMarketing) {
		this.datiMarketing = datiMarketing;
	}

	public void setDatiPrivacy(DatiPrivacyModel datiPrivacy) {
		this.datiPrivacy = datiPrivacy;
	}

	public void setIndirizzi(IndirizziModel indirizzi) {
		this.indirizzi = indirizzi;
	}

	public void setTelefoni(TelefoniModel telefoni) {
		this.telefoni = telefoni;
	}

	public void setVariazioni(VariazioniModel variazioni) {
		this.variazioni = variazioni;
	}

	public DomicilioModel getDomicilio() {
		return domicilio;
	}

	public ResidenzaModel getResidenza() {
		return residenza;
	}

	public void setDomicilio(DomicilioModel domicilio) {
		this.domicilio = domicilio;
	}

	public void setResidenza(ResidenzaModel residenza) {
		this.residenza = residenza;
	}

	public DatiApplicativiModel getDatiApplicativi() {
		return datiApplicativi;
	}

	public void setDatiApplicativi(DatiApplicativiModel datiApplicativi) {
		this.datiApplicativi = datiApplicativi;
	}

	public AgenteModel getAgente() {
		return agente;
	}

	public void setAgente(AgenteModel agente) {
		this.agente = agente;
	}

	public StringType getCodRete() {
		return codRete;
	}

	public void setCodRete(StringType codRete) {
		this.codRete = codRete;
	}

	public DocumentoModel getDocumento() {
		return documento;
	}

	public void setDocumento(DocumentoModel documento) {
		this.documento = documento;
	}

	public void setStatoProposta(StringType statoProposta) {
		this.statoProposta = statoProposta;
	}

	public IntegerType getProgressivo() {
		return progressivo;
	}

	public void setProgressivo(IntegerType progressivo) {
		this.progressivo = progressivo;
	}

	public TimestampType getDataInserimento() {
		return dataInserimento;
	}

	public void setDataInserimento(TimestampType dataInserimento) {
		this.dataInserimento = dataInserimento;
	}

	public StringType getServerReplica() {
		return serverReplica;
	}

	public void setServerReplica(StringType serverReplica) {
		this.serverReplica = serverReplica;
	}

	public TimestampType getDataVariazione() {
		return dataVariazione;
	}

	public void setDataVariazione(TimestampType dataVariazione) {
		this.dataVariazione = dataVariazione;
	}

	public DatiSedeModel getDatiSede() {
		return datiSede;
	}

	public void setDatiSede(DatiSedeModel datiSede) {
		this.datiSede = datiSede;
	}

	public StringType getCodCluster() {
		return codCluster;
	}

	public void setCodCluster(StringType codCluster) {
		this.codCluster = codCluster;
	}

	public StringType getStatoConfermato() {
		return statoConfermato;
	}

	public void setStatoConfermato(StringType statoConfermato) {
		this.statoConfermato = statoConfermato;
	}

	public DatiStampa getDatiStampa() {
		return datiStampa;
	}

	public void setDatiStampa(DatiStampa datiStampa) {
		this.datiStampa = datiStampa;
	}

	public IntegerType getNumVariazioni() {
		return numVariazioni;
	}

	public void setNumVariazioni(IntegerType numVariazioni) {
		this.numVariazioni = numVariazioni;
	}

	public InfoPersonaliModel getInfoPersonali() {
		return infoPersonali;
	}

	public void setInfoPersonali(InfoPersonaliModel infoPersonali) {
		this.infoPersonali = infoPersonali;
	}

	public StringType getCodNazl() {
		return codNazl;
	}

	public void setCodNazl(StringType codNazl) {
		this.codNazl = codNazl;
	}

	public boolean isAlmenoUnaProvinciaScaduta() {
		return almenoUnaProvinciaScaduta;
	}

	public void setAlmenoUnaProvinciaScaduta(boolean almenoUnaProvinciaScaduta) {
		this.almenoUnaProvinciaScaduta = almenoUnaProvinciaScaduta;
	}

	public BooleanType getTitolareContoDepositoAttivo() {
		return titolareContoDepositoAttivo;
	}

	public void setTitolareContoDepositoAttivo(BooleanType titolareContoDepositoAttivo) {
		this.titolareContoDepositoAttivo = titolareContoDepositoAttivo;
	}

	public BooleanType getIsTopBusiness() {
		return isTopBusiness;
	}

	public void setIsTopBusiness(BooleanType isTopBusiness) {
		this.isTopBusiness = isTopBusiness;
	}

	public ComuneModel getComuneNascita() {
		return comuneNascita;
	}

	public void setComuneNascita(ComuneModel comuneNascita) {
		this.comuneNascita = comuneNascita;
	}

	public StringType getTipoPersona() {
		return tipoPersona;
	}

	public void setTipoPersona(StringType tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	public ClienteModel getClienteTitolare() {
		return clienteTitolare;
	}

	public void setClienteTitolare(ClienteModel clienteTitolare) {
		this.clienteTitolare = clienteTitolare;
	}

	public StringType getNaturaGiuridica() {
		return naturaGiuridica;
	}

	public void setNaturaGiuridica(StringType naturaGiuridica) {
		this.naturaGiuridica = naturaGiuridica;
	}

	public StringType getSecondaIntestazione() {
		return secondaIntestazione;
	}

	public void setSecondaIntestazione(StringType secondaIntestazione) {
		this.secondaIntestazione = secondaIntestazione;
	}

	public InfoDittaModel getInfoDitta() {
		return infoDitta;
	}

	public void setInfoDitta(InfoDittaModel infoDitta) {
		this.infoDitta = infoDitta;
	}

	public StringType getCodProfiloDiInvestimento() {
		return codProfiloDiInvestimento;
	}

	public void setCodProfiloDiInvestimento(StringType codProfiloDiInvestimento) {
		this.codProfiloDiInvestimento = codProfiloDiInvestimento;
	}

	public RecapitiModel getRecapiti() {
		return recapiti;
	}

	public void setRecapiti(RecapitiModel recapiti) {
		this.recapiti = recapiti;
	}

	public FotografiaModel getFotografia() {
		return fotografia;
	}

	public void setFotografia(FotografiaModel fotografia) {
		this.fotografia = fotografia;
	}

	public ListType getElencoFotografie() {
		return elencoFotografie;
	}

	public void setElencoFotografie(ListType elencoFotografie) {
		this.elencoFotografie = elencoFotografie;
	}

	public StringType getNumeroOrdine() {
		return numeroOrdine;
	}

	public void setNumeroOrdine(StringType numeroOrdine) {
		this.numeroOrdine = numeroOrdine;
	}

	public StringType getCittadinanza() {
		return cittadinanza;
	}

	public void setCittadinanza(StringType cittadinanza) {
		this.cittadinanza = cittadinanza;
	}

	public StringType getDescrClusterCedacri() {
		return descrClusterCedacri;
	}

	public void setDescrClusterCedacri(StringType descrClusterCedacri) {
		this.descrClusterCedacri = descrClusterCedacri;
	}

	public StringType getCodClusterCedacri() {
		return codClusterCedacri;
	}

	public void setCodClusterCedacri(StringType codClusterCedacri) {
		this.codClusterCedacri = codClusterCedacri;
	}


	public IntegerType getDFinValCedacri() {
		return dFinValCedacri;
	}


	public void setDFinValCedacri(IntegerType finValCedacri) {
		dFinValCedacri = finValCedacri;
	}

	public StringType getDtCompQuestCedacri() {
		return dtCompQuestCedacri;
	}

	public void setDtCompQuestCedacri(StringType dtCompQuestCedacri) {
		this.dtCompQuestCedacri = dtCompQuestCedacri;
	}

	public IntegerType getVersioneQuestionario() {
		return versioneQuestionario;
	}

	public void setVersioneQuestionario(IntegerType versioneQuestionario) {
		this.versioneQuestionario = versioneQuestionario;
	}

	public StringType getSeValid() {
		return seValid;
	}

	public void setSeValid(StringType seValid) {
		this.seValid = seValid;
	}

	public DateType getScadenzaDocTaxUS() {
		return scadenzaDocTaxUS;
	}

	public void setScadenzaDocTaxUS(DateType scadenzaDocTaxUS) {
		this.scadenzaDocTaxUS = scadenzaDocTaxUS;
	}

	public StringType getNumeroCelluarePrimario() {
		return numeroCelluarePrimario;
	}

	public void setNumeroCelluarePrimario(StringType numeroCelluarePrimario) {
		this.numeroCelluarePrimario = numeroCelluarePrimario;
	}

	public StringType getSecondaCittadinanza() {
		return secondaCittadinanza;
	}

	public void setSecondaCittadinanza(StringType secondaCittadinanza) {
		this.secondaCittadinanza = secondaCittadinanza;
	}

	public StringType getTipoDitta() {
		return tipoDitta;
	}

	public void setTipoDitta(StringType tipoDitta) {
		this.tipoDitta = tipoDitta;
	}

	public DatiFatcaModel getDatiFatca() {
		return datiFatca;
	}

	public void setDatiFatca(DatiFatcaModel datiFatca) {
		this.datiFatca = datiFatca;
	}

	public BooleanType getIsFlussoFatcaTerminato() {
		return isFlussoFatcaTerminato;
	}

	public void setIsFlussoFatcaTerminato(BooleanType isFlussoFatcaTerminato) {
		this.isFlussoFatcaTerminato = isFlussoFatcaTerminato;
	}

	public String getFirstFatcaPopupName() {
		return firstFatcaPopupName;
	}

	public void setFirstFatcaPopupName(String firstFatcaPopupName) {
		this.firstFatcaPopupName = firstFatcaPopupName;
	}

	public boolean isSaltaPritAllInvioInSede() {
		return saltaPritAllInvioInSede;
	}

	public void setSaltaPritAllInvioInSede(boolean saltaPritAllInvioInSede) {
		this.saltaPritAllInvioInSede = saltaPritAllInvioInSede;
	}

	public boolean isSaltaControlliAllInvioInSede() {
		return saltaControlliAllInvioInSede;
	}

	public void setSaltaControlliAllInvioInSede(boolean saltaControlliAllInvioInSede) {
		this.saltaControlliAllInvioInSede = saltaControlliAllInvioInSede;
	}

	public StringType getModuloFatca() {
		return moduloFatca;
	}

	public void setModuloFatca(StringType moduloFatca) {
		this.moduloFatca = moduloFatca;
	}

	public StringType getEspfina() {
		return espfina;
	}

	public void setEspfina(StringType espfina) {
		this.espfina = espfina;
	}

	public TimestampType getDataVariazioneFirmaDigitale() {
		return dataVariazioneFirmaDigitale;
	}

	public void setDataVariazioneFirmaDigitale(
			TimestampType dataVariazioneFirmaDigitale) {
		this.dataVariazioneFirmaDigitale = dataVariazioneFirmaDigitale;
	}

	public BooleanType getIsDaInviareAllaFabbrica() {
		return isDaInviareAllaFabbrica;
	}

	public void setIsDaInviareAllaFabbrica(BooleanType isDaInviareAllaFabbrica) {
		this.isDaInviareAllaFabbrica = isDaInviareAllaFabbrica;
	}

	public ListType getDifferenziali() {
		return differenziali;
	}

	public void setDifferenziali(ListType differenziali) {
		this.differenziali = differenziali;
	}

	public boolean isOnStampaBozzaAction() {
		return onStampaBozzaAction;
	}

	public void setOnStampaBozzaAction(boolean onStampaBozzaAction) {
		this.onStampaBozzaAction = onStampaBozzaAction;
	}

	public InfoSocietarieModel getInfoSocietarie() {
		return infoSocietarie;
	}

	public void setInfoSocietarie(InfoSocietarieModel infoSocietarie) {
		this.infoSocietarie = infoSocietarie;
	}

	public BooleanType getIsDipendente() {
		return isDipendente;
	}

	public void setIsDipendente(BooleanType isDipendente) {
		this.isDipendente = isDipendente;
	}

	public StringType getDomicilioDiversoDaResidenza() {
		return domicilioDiversoDaResidenza;
	}

	public void setDomicilioDiversoDaResidenza(StringType domicilioDiversoDaResidenza) {
		this.domicilioDiversoDaResidenza = domicilioDiversoDaResidenza;
	}

	public AdempimentiNormativiModel getAdempimentiNormativi() {
		return adempimentiNormativi;
	}

	public void setAdempimentiNormativi(AdempimentiNormativiModel adempimentiNormativi) {
		this.adempimentiNormativi = adempimentiNormativi;
	}

	public BooleanType getIsClienteInCogestione() {
		return isClienteInCogestione;
	}

	public void setIsClienteInCogestione(BooleanType isClienteInCogestione) {
		this.isClienteInCogestione = isClienteInCogestione;
	}

	public CogestioneDataModel getCogestioneData() {
		return cogestioneData;
	}

	public void setCogestioneData(CogestioneDataModel cogestioneData) {
		this.cogestioneData = cogestioneData;
	}

	public TimestampType getDataOraUltimaModifica() {
		return dataOraUltimaModifica;
	}

	public void setDataOraUltimaModifica(TimestampType dataOraUltimaModifica) {
		this.dataOraUltimaModifica = dataOraUltimaModifica;
	}

	public StringType getCodAgenteUltimaModifica() {
		return codAgenteUltimaModifica;
	}

	public void setCodAgenteUltimaModifica(StringType codAgenteUltimaModifica) {
		this.codAgenteUltimaModifica = codAgenteUltimaModifica;
	}

	public String getConcurrencyViolationFoundedWidth() {
		return concurrencyViolationFoundedWidth;
	}

	public void setConcurrencyViolationFoundedWidth(String concurrencyViolationFoundedWidth) {
		this.concurrencyViolationFoundedWidth = concurrencyViolationFoundedWidth;
	}

	public StringType getDataOraUltimaModificaAsString() {
		return dataOraUltimaModificaAsString;
	}

	public void setDataOraUltimaModificaAsString(StringType dataOraUltimaModificaAsString) {
		this.dataOraUltimaModificaAsString = dataOraUltimaModificaAsString;
	}

	public ProfiloPcpClienteModel getProfiloPcp() {
		return profiloPcp;
	}

	public void setProfiloPcp(ProfiloPcpClienteModel profiloPcp) {
		this.profiloPcp = profiloPcp;
	}

}
