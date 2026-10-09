package prgm.pdfwebforms.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.AbstractTypePropertyDescriptor;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.core.PdfPredefinedFields;
import prgm.pdfwebforms.signprocess.common.PdfPersonSignProcessInfo;

/*****************************************************************************************************/
/*****************************************************************************************************/
public class PdfPersonModel extends MapCommandDataModel {

	public static int NO_ERROR = 0;
	public static int CLI_NOT_EXIST = 1;
	public static int PROSPECT_NOT_EXIST = 2;
	public static int PROSPECT_IS_DRAFT = 3;
	
	public static String COD_RUOLO_VERIFICA_MIFID_INTESTATARIO 		= "INT";
	public static String COD_RUOLO_VERIFICA_MIFID_COINTESTATARIO 	= "CNT";
	public static String COD_RUOLO_VERIFICA_MIFID_ORDINANTE 		= "ORD";
	public static String COD_RUOLO_VERIFICA_MIFID_DELEGATO 			= "DTO";
	public static String COD_RUOLO_VERIFICA_MIFID_DELEGANTE 		= "DTE";
	
	private static final String S_DATA_NASCITA = "dataNascita";

	private boolean		agente = false;
	private boolean		notFound = false;
	private boolean		bozza = false;
	private boolean		clienteAgente = false;
	private boolean		hasBancaDiretta = true;

	private PdfPersonSignProcessInfo[]	pdfPersonsSignProcessInfos = null;	// Usato nel processo di firma
	
	private ArrayList<Integer>	indexInFieldName = new ArrayList<Integer>();
	private boolean				hasSignInSomePdf = false;
	
	private StringType  codAgente = new StringType();
	private StringType  pdfEnvironment = new StringType();
	private StringType  codAgeImpersonato = new StringType();
	
	private StringType  idCensimento = new StringType();
	private StringType  ndg = new StringType();
	
	private StringType 	codPotenziale = new StringType();
	private IntegerType progressivoUltimaVariazione = new IntegerType();
	private StringType  codRuoloVerificaMifid = new StringType(COD_RUOLO_VERIFICA_MIFID_ORDINANTE);
	private BooleanType	acceptDraftCustomer = new BooleanType();

	private PdfPersonSignDataModel signData = null;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isEmty(){
		return getNdg().isNull() && getIdCensimento().isNull() ? true : false;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isEqual(PdfPersonModel other){
		if((!isAgente() && other.isAgente()) || (isAgente() && !other.isAgente()))
			return false;
		return (!getNdg().isNull() && !other.getNdg().isNull() && getNdg().equals(other.getNdg())) || 
		       (!getIdCensimento().isNull() && !other.getIdCensimento().isNull() && getIdCensimento().equals(other.getIdCensimento()));	
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public BooleanType getIsEffettivo(){
		return !getNdg().isNull() ? new BooleanType(true) : new BooleanType(false);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public StringType getSubjCod(){
		return getNdg().isNull() ? new StringType(readCodiceFiscale()) : getNdg();
	}		
	/***********************************************************************************************/
	/***********************************************************************************************/
	public StringType getSubjType(){
		return getNdg().isNull() ? new StringType("P") : new StringType("F");
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public StringType getSubjCodOTP(){
		return getNdg().isNull() || !isHasBancaDiretta() ? new StringType(readCodiceFiscale()) : getNdg();
	}		
	/***********************************************************************************************/
	/***********************************************************************************************/
	public StringType getSubjTypeOTP(){
		return getNdg().isNull() || !isHasBancaDiretta() ? new StringType("P") : new StringType("F");
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isPersonaGiuridica() {
		return propertyToString("isPersonaGiuridica").equals("true");
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public StringType getCodicePersona() {
		return getNdg().isNull() ? getIdCensimento() : getNdg();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void fillPdfDataFromAgente(PdfModel pdf, PdfDataModel pdfData, boolean keyFieldPreloaded){
		
		try{
			AbstractTypePropertyDescriptor[] dinamycProps = getMappedPropertyDescriptors();
			for(int i=0;i<dinamycProps.length;i++){
				String propName = dinamycProps[i].getName()+"Agente";
				if(pdf.getPdfData().getPdfInfos().findFieldInfoByHtmlName(propName) == null)
					continue;
				AbstractType val = dinamycProps[i].getValue();
				if(val != null){
					AbstractType pdfDataPropValue = pdfData.readProperty(propName);
					if(pdfDataPropValue != null)
						pdfData.addProperty(propName,val);
					
					if(keyFieldPreloaded){
						if(!pdfData.getPdfInitialInputDataArray().contains(propName))
							pdfData.getPdfInitialInputDataArray().add(propName);
					}
				}
			}
			
			pdfData.addProperty(PdfPredefinedFields.AGENTE_CODICE, new StringType(Tools.unFillSx(getCodAgente().toString(),'0')));
			pdfData.addProperty(PdfPredefinedFields.AGENTE_NDG, new StringType(Tools.unFillSx(getNdg().toString(),'0')));
			
			if(keyFieldPreloaded){
				if(!pdfData.getPdfInitialInputDataArray().contains(PdfPredefinedFields.AGENTE_CODICE))
					pdfData.getPdfInitialInputDataArray().add(PdfPredefinedFields.AGENTE_CODICE);
				if(!pdfData.getPdfInitialInputDataArray().contains(PdfPredefinedFields.AGENTE_NDG))
					pdfData.getPdfInitialInputDataArray().add(PdfPredefinedFields.AGENTE_NDG);
			}
			
			if(pdf.getIsRete().booleanValue() || pdf.getIsAssistenteFB().booleanValue())
				fillPdfDataForProtectionSpecialist(pdf, pdfData);
			
		}catch(Exception e){}
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void fillPdfDataForProtectionSpecialist(PdfModel pdf, PdfDataModel pdfData){

		PdfPersonModel agenteImpersonato = pdf.getPdfData().getAgenteImpersonato();
		if(agenteImpersonato == null)
			agenteImpersonato = new PdfPersonModel();
		
		boolean existConsulenteFinanziario = pdfData.read(PdfPredefinedFields.PROTECTION_SPECIALIST_CODICE_CONSULENTE_FINANZIARIO) != null;
		boolean existProtectionSpecialist = pdfData.read(PdfPredefinedFields.PROTECTION_SPECIALIST_CODICE_SPECIALIST) != null;
		
		// Init data
		if(existConsulenteFinanziario) {
			pdfData.addProperty(PdfPredefinedFields.PROTECTION_SPECIALIST_COGNOME_NOME_CONSULENTE_FINANZIARIO, new StringType());
			pdfData.addProperty(PdfPredefinedFields.PROTECTION_SPECIALIST_CODICE_CONSULENTE_FINANZIARIO, new StringType());
			pdfData.addProperty(PdfPredefinedFields.PROTECTION_SPECIALIST_CODICE_AREA_CONSULENTE_FINANZIARIO, new StringType());
		}
		
		if(existProtectionSpecialist) {
			pdfData.addProperty(PdfPredefinedFields.PROTECTION_SPECIALIST_COGNOME_NOME_SPECIALIST, new StringType());
			pdfData.addProperty(PdfPredefinedFields.PROTECTION_SPECIALIST_CODICE_SPECIALIST, new StringType());
			pdfData.addProperty(PdfPredefinedFields.PROTECTION_SPECIALIST_CODICE_AREA_SPECIALIST, new StringType());
		}
		
		if(pdf.isProtectionSpecialistInHub()) {

			if(existConsulenteFinanziario) {
				pdfData.addProperty(PdfPredefinedFields.PROTECTION_SPECIALIST_COGNOME_NOME_CONSULENTE_FINANZIARIO, new StringType(agenteImpersonato.readCognomeNome()));
				pdfData.addProperty(PdfPredefinedFields.PROTECTION_SPECIALIST_CODICE_CONSULENTE_FINANZIARIO, new StringType(agenteImpersonato.getCodAgente().toString()));
				pdfData.addProperty(PdfPredefinedFields.PROTECTION_SPECIALIST_CODICE_AREA_CONSULENTE_FINANZIARIO, agenteImpersonato.readProperty("codiceArea"));
			}
			
			if(existProtectionSpecialist) {
				pdfData.addProperty(PdfPredefinedFields.PROTECTION_SPECIALIST_COGNOME_NOME_SPECIALIST, new StringType(readCognomeNome()));
				pdfData.addProperty(PdfPredefinedFields.PROTECTION_SPECIALIST_CODICE_SPECIALIST, new StringType(getCodAgente().toString()));
				pdfData.addProperty(PdfPredefinedFields.PROTECTION_SPECIALIST_CODICE_AREA_SPECIALIST, readProperty("codiceArea"));
			}
			
		}else if(pdf.getIsProtectionSpecialistEsterno().booleanValue()) {
		
			if(existProtectionSpecialist) {
				pdfData.addProperty(PdfPredefinedFields.PROTECTION_SPECIALIST_COGNOME_NOME_SPECIALIST, new StringType(readCognomeNome()));
				pdfData.addProperty(PdfPredefinedFields.PROTECTION_SPECIALIST_CODICE_SPECIALIST, new StringType(getCodAgente().toString()));
				pdfData.addProperty(PdfPredefinedFields.PROTECTION_SPECIALIST_CODICE_AREA_SPECIALIST, readProperty("codiceArea"));
			}
			
		}else {

			if(existConsulenteFinanziario) {
				pdfData.addProperty(PdfPredefinedFields.PROTECTION_SPECIALIST_COGNOME_NOME_CONSULENTE_FINANZIARIO, new StringType(readCognomeNome()));
				pdfData.addProperty(PdfPredefinedFields.PROTECTION_SPECIALIST_CODICE_CONSULENTE_FINANZIARIO, new StringType(getCodAgente().toString()));
				pdfData.addProperty(PdfPredefinedFields.PROTECTION_SPECIALIST_CODICE_AREA_CONSULENTE_FINANZIARIO, readProperty("codiceArea"));
			}
			
		}
		
	}
	
	/***********************************************************************************************/
	private static final String SUFF_CLIENTE = "Cliente";
	/***********************************************************************************************/
	public void fillPdfDataFromCliente(PdfModel pdf, PdfDataModel pdfData, int index, boolean onInit){
		
		try{

			boolean keyFieldPreloaded = false; 
			if(pdfData.getPdfInitialInputDataArray().contains(PdfPredefinedFields.CLIENTE_NDG_PREFIX+index) || 
			   pdfData.getPdfInitialInputDataArray().contains(PdfPredefinedFields.CLIENTE_ID_CENSIMENTO_PREFIX+index))
				keyFieldPreloaded = true;
			
			AbstractTypePropertyDescriptor[] dinamycProps = getMappedPropertyDescriptors();
			for(int i=0;i<dinamycProps.length;i++){
				String propName = dinamycProps[i].getName()+SUFF_CLIENTE+index;
				if(pdf.getPdfData().getPdfInfos().findFieldInfoByHtmlName(propName) == null)
					continue;
				AbstractType val = dinamycProps[i].getValue();
				if(val != null){

					AbstractType pdfDataPropValue = pdfData.readProperty(propName);

					// I campi modificabili, solo se vuoti, li lascio editabili e con il valore del dataentry
					boolean keepPreloadedValue = keyFieldPreloaded;
					if(pdfDataPropValue != null && pdfDataPropValue.isNull() &&
					   pdf.getPreloadedPersonFieldsEditableIfNull() != null && 
					   pdf.getPreloadedPersonFieldsEditableIfNull().contains(dinamycProps[i].getName()))
						keepPreloadedValue = false;
					
					if(onInit || keepPreloadedValue){
						if(pdfDataPropValue != null)
							pdfData.addProperty(propName,val);
					}else if(pdf.isInValidazioneMOM()){
						if(pdfDataPropValue == null || pdfDataPropValue.isNull())
							pdfData.addProperty(propName,val);
					}else if(pdf.isOperatoreMOM()){ // altrimenti in inserimento/doppia spunta
						pdfData.addProperty(propName,val);
					}else{
						if(pdfDataPropValue == null || pdfDataPropValue.isNull())
							pdfData.addProperty(propName,val);
					}
					
					pdfData.getPdfInitialInputDataArray().remove(propName);
					if(keepPreloadedValue)
						pdfData.getPdfInitialInputDataArray().add(propName);
				}
			}
			
			pdfData.addProperty(PdfPredefinedFields.CLIENTE_NDG_PREFIX+index, new StringType(Tools.unFillSx(getNdg().toString(),'0')));
			if(getNdg().hasTypeErrors())
				pdfData.read(PdfPredefinedFields.CLIENTE_NDG_PREFIX+index).setTypeErrors(getNdg().getTypeErrors());
			pdfData.addProperty(PdfPredefinedFields.CLIENTE_ID_CENSIMENTO_PREFIX+index, new StringType(getIdCensimento().toString()));
			
			pdfData.getPdfInitialInputDataArray().remove(PdfPredefinedFields.CLIENTE_NDG_PREFIX+index);
			pdfData.getPdfInitialInputDataArray().remove(PdfPredefinedFields.CLIENTE_ID_CENSIMENTO_PREFIX+index);
			if(keyFieldPreloaded){
				pdfData.getPdfInitialInputDataArray().add(PdfPredefinedFields.CLIENTE_NDG_PREFIX+index);
				pdfData.getPdfInitialInputDataArray().add(PdfPredefinedFields.CLIENTE_ID_CENSIMENTO_PREFIX+index);
			}
			
		}catch(Exception e){}

	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void fillPdfDataFromEmptyCliente(PdfModel pdf, PdfDataModel pdfData, int index, boolean onInit){

		try{

			boolean keyFieldPreloaded = false;
			AbstractTypePropertyDescriptor[] dinamycProps = getMappedPropertyDescriptors();

			for(int i=0;i<dinamycProps.length;i++){
				String propName = dinamycProps[i].getName()+SUFF_CLIENTE+index;
				if(propName.equals(PdfPredefinedFields.CLIENTE_ID_CENSIMENTO_PREFIX+index) || 
				   propName.equals(PdfPredefinedFields.CLIENTE_NDG_PREFIX+index)) {
					AbstractType val = pdfData.readProperty(propName);
					if(val != null && !val.isNull()){
						if(pdfData.getPdfInitialInputDataArray().contains(propName))
							keyFieldPreloaded = true;
						break;
					}
				}
			}
			
			for(int i=0;i<dinamycProps.length;i++){
				String propName = dinamycProps[i].getName()+SUFF_CLIENTE+index;
				if(pdf.getPdfData().getPdfInfos().findFieldInfoByHtmlName(propName) == null)
					continue;
				AbstractType val = dinamycProps[i].getValue();
				if(val != null){
					AbstractType pdfDataPropValue = pdfData.readProperty(propName);
					if(onInit){
						if(pdfDataPropValue != null)
							pdfData.addProperty(propName,val);
					}else if(pdf.isInValidazioneMOM()){
						if(pdfDataPropValue == null || pdfDataPropValue.isNull())
							pdfData.addProperty(propName,val);
					}else if(pdf.isOperatoreMOM()){ // altrimenti in inserimento/doppia spunta
						pdfData.addProperty(propName,val);
					}else{
						if(pdfDataPropValue != null)
							pdfData.addProperty(propName,val);
					}
				}
				pdfData.getPdfInitialInputDataArray().remove(propName);
				if(keyFieldPreloaded)
					pdfData.getPdfInitialInputDataArray().add(propName);
			}
			
			pdfData.getPdfInitialInputDataArray().remove(PdfPredefinedFields.CLIENTE_NDG_PREFIX+index);
			pdfData.getPdfInitialInputDataArray().remove(PdfPredefinedFields.CLIENTE_ID_CENSIMENTO_PREFIX+index);
			if(keyFieldPreloaded){
				pdfData.getPdfInitialInputDataArray().add(PdfPredefinedFields.CLIENTE_NDG_PREFIX+index);
				pdfData.getPdfInitialInputDataArray().add(PdfPredefinedFields.CLIENTE_ID_CENSIMENTO_PREFIX+index);
			}
			
		}catch(Exception e){}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String readCognomeNome(){
		return readProperty("cognomeNome") == null ? "" : Tools.capitalize(readProperty("cognomeNome").toString());
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String readNomeCognome(){
		return readProperty("nomeCognome") == null ? "" : Tools.capitalize(readProperty("nomeCognome").toString());
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String readCodiceFiscale(){
		return readProperty("codiceFiscale") == null ? "" : readProperty("codiceFiscale").toString();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public String propertyToString(String propName){
		return readProperty(propName) == null ? "" : readProperty(propName).toString();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public int etaPersona() {
		try {
			String dataNascitaAsString = propertyToString(S_DATA_NASCITA);
			if(dataNascitaAsString.isEmpty())
				return -1;
			Date dataNascita = new DateType(dataNascitaAsString).dateValue();
			int age = 0;
			Calendar birthdate = Calendar.getInstance();
			birthdate.setTime(dataNascita);
			Calendar now = Calendar.getInstance();
			age = now.get(Calendar.YEAR) - birthdate.get(Calendar.YEAR);
			birthdate.add(Calendar.YEAR, age);		
			if(now.before(birthdate))
				age--;
			return age;		
		}catch(Throwable t) {
			return -1;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public String createJsonData(String fieldName){
		StringBuffer jsonObj = new StringBuffer();
		
		if(isAgente()){
			jsonObj.append("\"codice\":\""+Tools.unFillSx(getCodAgente().toString(),'0')+"\",");
			jsonObj.append("\"numeroCellulare\":\""+propertyToString("numeroCellulare")+"\",");
			jsonObj.append("\"numeroTelefono\":\""+propertyToString("numeroTelefono")+"\",");
			jsonObj.append("\"codiceArea\":\""+propertyToString("codiceArea")+"\",");
			jsonObj.append("\"codiceAgenzia\":\""+propertyToString("codiceAgenzia")+"\",");
			jsonObj.append("\"comuneAgenzia\":\""+propertyToString("comuneAgenzia")+"\",");
			jsonObj.append("\"provinciaAgenzia\":\""+propertyToString("provinciaAgenzia")+"\",");
			jsonObj.append("\"dataIscrizioneRui\":\""+propertyToString("dataIscrizioneRui")+"\",");
			jsonObj.append("\"numeroIscrizioneRui\":\""+propertyToString("numeroIscrizioneRui")+"\",");
		}
		
		jsonObj.append("\"ndg\":\""+Tools.unFillSx(getNdg().toString(),'0')+"\",");
		jsonObj.append("\"idCensimento\":\""+getIdCensimento()+"\",");
		
		jsonObj.append("\"cognome\":\""+propertyToString("cognome")+"\",");
		jsonObj.append("\"nome\":\""+propertyToString("nome")+"\",");
		jsonObj.append("\"secondaIntestazione\":\""+propertyToString("secondaIntestazione")+"\",");
		jsonObj.append("\"codiceFiscale\":\""+propertyToString("codiceFiscale")+"\",");
		jsonObj.append("\"partitaIva\":\""+propertyToString("partitaIva")+"\",");
		jsonObj.append("\"codiceFiscalePartitaIva\":\""+propertyToString("codiceFiscalePartitaIva")+"\",");
		jsonObj.append("\"sesso\":\""+propertyToString("sesso")+"\",");
		jsonObj.append("\"cognomeNome\":\""+propertyToString("cognomeNome")+"\",");
		jsonObj.append("\"nomeCognome\":\""+propertyToString("nomeCognome")+"\",");
		jsonObj.append("\"isPrimafila\":\""+propertyToString("isPrimafila")+"\",");
		jsonObj.append("\"isGiaCliente\":\""+propertyToString("isGiaCliente")+"\",");
		jsonObj.append("\"isCointestatario\":\""+propertyToString("isCointestatario")+"\",");
		
		jsonObj.append("\"dataNascita\":\""+propertyToString(S_DATA_NASCITA)+"\",");
		jsonObj.append("\"luogoNascita\":\""+propertyToString("luogoNascita")+"\",");
		jsonObj.append("\"codiceNazioneNascita\":\""+propertyToString("codiceNazioneNascita")+"\",");
		jsonObj.append("\"nazioneNascita\":\""+propertyToString("nazioneNascita")+"\",");
		jsonObj.append("\"provinciaNascita\":\""+propertyToString("provinciaNascita")+"\",");
	
		
		jsonObj.append("\"isPersonaFisica\":\""+propertyToString("isPersonaFisica")+"\",");
		jsonObj.append("\"isPersonaGiuridica\":\""+propertyToString("isPersonaGiuridica")+"\",");
		jsonObj.append("\"isDittaIndividuale\":\""+propertyToString("isDittaIndividuale")+"\",");
		jsonObj.append("\"isLiberoProfessionista\":\""+propertyToString("isLiberoProfessionista")+"\",");
		jsonObj.append("\"naturaGiuridica\":\""+propertyToString("naturaGiuridica")+"\",");

		jsonObj.append("\"codiceStatoCivile\":\""+propertyToString("codiceStatoCivile")+"\",");
		jsonObj.append("\"statoCivile\":\""+propertyToString("statoCivile")+"\",");
		
		jsonObj.append("\"codiceNazioneCittadinanza\":\""+propertyToString("codiceNazioneCittadinanza")+"\",");
		jsonObj.append("\"nazioneCittadinanza\":\""+propertyToString("nazioneCittadinanza")+"\",");
		
		jsonObj.append("\"email\":\""+propertyToString("email")+"\",");
		
		jsonObj.append("\"codiceTipoDocumento\":\""+propertyToString("codiceTipoDocumento")+"\",");
		jsonObj.append("\"tipoDocumento\":\""+propertyToString("tipoDocumento")+"\",");
		jsonObj.append("\"numeroDocumento\":\""+propertyToString("numeroDocumento")+"\",");
		jsonObj.append("\"codiceEnteRilascianteDocumento\":\""+propertyToString("codiceEnteRilascianteDocumento")+"\",");
		jsonObj.append("\"enteRilascianteDocumento\":\""+propertyToString("enteRilascianteDocumento")+"\",");
		jsonObj.append("\"dataEmissioneDocumento\":\""+propertyToString("dataEmissioneDocumento")+"\",");
		jsonObj.append("\"luogoEmissioneDocumento\":\""+propertyToString("luogoEmissioneDocumento")+"\",");
		jsonObj.append("\"provinciaEmissioneDocumento\":\""+propertyToString("provinciaEmissioneDocumento")+"\",");
		jsonObj.append("\"dataScadenzaDocumento\":\""+propertyToString("dataScadenzaDocumento")+"\",");
		
		jsonObj.append("\"codiceToponimoResidenza\":\""+propertyToString("codiceToponimoResidenza")+"\",");
		jsonObj.append("\"toponimoResidenza\":\""+propertyToString("toponimoResidenza")+"\",");
		jsonObj.append("\"indirizzoResidenza\":\""+propertyToString("indirizzoResidenza")+"\",");
		jsonObj.append("\"toponimoIndirizzoResidenza\":\""+propertyToString("toponimoIndirizzoResidenza")+"\",");
		jsonObj.append("\"numeroCivicoResidenza\":\""+propertyToString("numeroCivicoResidenza")+"\",");
		jsonObj.append("\"toponimoIndirizzoNumeroResidenza\":\""+propertyToString("toponimoIndirizzoNumeroResidenza")+"\",");
		jsonObj.append("\"capResidenza\":\""+propertyToString("capResidenza")+"\",");
		jsonObj.append("\"luogoResidenza\":\""+propertyToString("luogoResidenza")+"\",");
		jsonObj.append("\"comuneResidenza\":\""+propertyToString("comuneResidenza")+"\",");
		jsonObj.append("\"provinciaResidenza\":\""+propertyToString("provinciaResidenza")+"\",");
		jsonObj.append("\"codiceNazioneResidenza\":\""+propertyToString("codiceNazioneResidenza")+"\",");
		jsonObj.append("\"nazioneResidenza\":\""+propertyToString("nazioneResidenza")+"\",");
		
		jsonObj.append("\"prefissoTelefonoAbitazione\":\""+propertyToString("prefissoTelefonoAbitazione")+"\",");
		jsonObj.append("\"numeroTelefonoAbitazione\":\""+propertyToString("numeroTelefonoAbitazione")+"\",");
		jsonObj.append("\"telefonoAbitazione\":\""+propertyToString("telefonoAbitazione")+"\",");

		jsonObj.append("\"prefissoTelefonoCellulare\":\""+propertyToString("prefissoTelefonoCellulare")+"\",");
		jsonObj.append("\"numeroTelefonoCellulare\":\""+propertyToString("numeroTelefonoCellulare")+"\",");
		jsonObj.append("\"telefonoCellulare\":\""+propertyToString("telefonoCellulare")+"\",");

		jsonObj.append("\"motivazionePep\":\""+propertyToString("motivazionePep")+"\",");
		
		String value = "";
		if(isAgente() && fieldName.equals("codice"))
			value = Tools.unFillSx(getCodAgente().toString(),'0');
		else if(fieldName.equals("ndg"))
			value = Tools.unFillSx(getNdg().toString(),'0');
		else if(fieldName.equals("idCensimento"))
			value = getIdCensimento().toString();
		else
			value = propertyToString(fieldName);
		jsonObj.append("\"value\":\""+value+"\",");

		String label = "";
		String cod = getNdg().isNull() ? getIdCensimento().toString() : getNdg().toString();
		if(isAgente())
			cod = Tools.fillSx(getCodAgente().toString(),'0',10);
		if(isNotFound()){
			label = cod + " - Non trovato"; 
		}else{
			String natoil = "";
			String dataNascita = propertyToString(S_DATA_NASCITA);
			if(dataNascita.length() > 0)
				natoil = ": "+("F".equals(propertyToString("sesso")) ? "nata il " : "nato il ")+""+dataNascita;
			label = "<span style='white-space:nowrap;'>"+cod + " - " + Tools.capitalize(propertyToString("cognome")+" "+propertyToString("nome"))+natoil+"</span>";
		}
		jsonObj.append("\"label\":\""+label+"\"");
		
		return jsonObj.toString();
	}
	
	public StringType getCodAgente() {
		return codAgente;
	}

	public void setCodAgente(StringType codAgente) {
		this.codAgente = codAgente;
	}

	public StringType getCodPotenziale() {
		return codPotenziale;
	}

	public void setCodPotenziale(StringType codPotenziale) {
		this.codPotenziale = codPotenziale;
	}

	public IntegerType getProgressivoUltimaVariazione() {
		return progressivoUltimaVariazione;
	}

	public void setProgressivoUltimaVariazione(
			IntegerType progressivoUltimaVariazione) {
		this.progressivoUltimaVariazione = progressivoUltimaVariazione;
	}


	public boolean isAgente() {
		return agente;
	}

	public void setAgente(boolean agente) {
		this.agente = agente;
	}

	public PdfPersonSignDataModel getSignData() {
		return signData;
	}

	public void setSignData(PdfPersonSignDataModel signData) {
		this.signData = signData;
	}

	public StringType getIdCensimento() {
		return idCensimento;
	}

	public void setIdCensimento(StringType idCensimento) {
		this.idCensimento = idCensimento;
	}

	public StringType getNdg() {
		return ndg;
	}

	public void setNdg(StringType ndg) {
		this.ndg = ndg;
	}

	public boolean isNotFound() {
		return notFound;
	}

	public void setNotFound(boolean notFound) {
		this.notFound = notFound;
	}

	public ArrayList<Integer> getIndexInFieldName() {
		return indexInFieldName;
	}

	public void setIndexInFieldName(ArrayList<Integer> indexInFieldName) {
		this.indexInFieldName = indexInFieldName;
	}

	public PdfPersonSignProcessInfo[] getPdfPersonsSignProcessInfos() {
		return pdfPersonsSignProcessInfos;
	}

	public void setPdfPersonsSignProcessInfos(
			PdfPersonSignProcessInfo[] pdfPersonsSignProcessInfos) {
		this.pdfPersonsSignProcessInfos = pdfPersonsSignProcessInfos;
	}

	public boolean isHasSignInSomePdf() {
		return hasSignInSomePdf;
	}

	public void setHasSignInSomePdf(boolean hasSignInSomePdf) {
		this.hasSignInSomePdf = hasSignInSomePdf;
	}

	public boolean isBozza() {
		return bozza;
	}

	public void setBozza(boolean bozza) {
		this.bozza = bozza;
	}

	public boolean isClienteAgente() {
		return clienteAgente;
	}

	public void setClienteAgente(boolean clienteAgente) {
		this.clienteAgente = clienteAgente;
	}
	public StringType getCodRuoloVerificaMifid() {
		return codRuoloVerificaMifid;
	}
	public void setCodRuoloVerificaMifid(StringType codRuoloVerificaMifid) {
		this.codRuoloVerificaMifid = codRuoloVerificaMifid;
	}
	public StringType getPdfEnvironment() {
		return pdfEnvironment;
	}
	public void setPdfEnvironment(StringType pdfEnvironment) {
		this.pdfEnvironment = pdfEnvironment;
	}
	public StringType getCodAgeImpersonato() {
		return codAgeImpersonato;
	}
	public void setCodAgeImpersonato(StringType codAgeImpersonato) {
		this.codAgeImpersonato = codAgeImpersonato;
	}

	public boolean isHasBancaDiretta() {
		return hasBancaDiretta;
	}

	public void setHasBancaDiretta(boolean hasBancaDiretta) {
		this.hasBancaDiretta = hasBancaDiretta;
	}

	public BooleanType getAcceptDraftCustomer() {
		return acceptDraftCustomer;
	}

	public void setAcceptDraftCustomer(BooleanType acceptDraftCustomer) {
		this.acceptDraftCustomer = acceptDraftCustomer;
	}
}
