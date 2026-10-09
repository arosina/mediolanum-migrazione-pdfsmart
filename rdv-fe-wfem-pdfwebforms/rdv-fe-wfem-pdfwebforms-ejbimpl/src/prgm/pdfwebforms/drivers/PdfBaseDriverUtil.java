package prgm.pdfwebforms.drivers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.DAOCallableResultModel;
import com.atosorigin.wfem.dao.DAOOSBResultModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQASResultModel;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.QASCallData;
import com.atosorigin.wfem.util.Tools;
import com.atosorigin.wfem.util.XmlServiceCallData;

import prgm.pdfwebforms.agevolazioni.AgevolazioneModel;
import prgm.pdfwebforms.core.PdfBarcodeUtils;
import prgm.pdfwebforms.core.PdfConfig;
import prgm.pdfwebforms.core.PdfPredefinedFields;
import prgm.pdfwebforms.dataentryutil.ComuneAutocompleteModel;
import prgm.pdfwebforms.dataentryutil.ContoAutocompleteInput;
import prgm.pdfwebforms.dataentryutil.ContoAutocompleteModel;
import prgm.pdfwebforms.dataentryutil.LuogoAutocompleteModel;
import prgm.pdfwebforms.dataentryutil.ToponimoAutocompleteModel;
import prgm.pdfwebforms.dataloader.DataLoader;
import prgm.pdfwebforms.drivers.iban.InternationalBankAccountNumber;
import prgm.pdfwebforms.drivers.model.ProfiloMifidModel;
import prgm.pdfwebforms.drivers.model.SaldoContoModel;
import prgm.pdfwebforms.model.CounterModel;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.model.PdfPersonModel;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class PdfBaseDriverUtil extends AbstractDriver{

	private static final String DAO_DATAENTRY_UTIL_XML_NAME = "PdfWebForms.PdfDataentryUtil";
	private static final String DAO_BASE_DRIVER_XML_NAME = "PdfWebForms.PdfBaseDriver";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void putError(PdfDataModel pdfData, ArrayList<String> fieldNames, String errmsg){
		for(String fieldName : fieldNames)
			putError(pdfData, fieldName, errmsg);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void putError(PdfDataModel pdfData, String fieldName, String errmsg){
		AbstractType field = pdfData.read(fieldName);
		if(field != null)
			field.addTypeError(errmsg);
	}
	
	/********************************************************************************************************/
	/********************************************************************************************************/
	public StringType getCounter(ClientSessionContext csc, String nomeRisorsa, String filler, int fillLength) throws Exception{

		try{

			DAOObject dao = new DAOObject(csc, DAO_BASE_DRIVER_XML_NAME);

			CounterModel contModel = new CounterModel();
			contModel.setResourceName(new StringType(nomeRisorsa));
			DAOCallableResultModel callRes = dao.executeCallableAccess("getCounter",contModel);
			if(callRes.getResult() != 0)
				throw new Exception("Errore ["+callRes.getResult()+"] nel prendere il contatore per la risorsa ["+nomeRisorsa+"]");
			StringType result = contModel.getCounter();
			if(result == null)
				throw new Exception("Contatore per la risorsa ["+nomeRisorsa+"] = null");
			if(filler != null && filler.length() > 0 && fillLength > 0 && result.toString().length() < fillLength)
				result = new StringType(Tools.fillSx(result.toString(), filler.charAt(0), fillLength));
			return result;

		}catch(DAOException daoe){
			String errorMsg = "Eccezione DAO nel prendere il contatore per la risorsa ["+nomeRisorsa+"]: "+daoe.toString();
			Exception e = new Exception(errorMsg);
			throw e;
		}catch(Exception e){
			String errorMsg = "Eccezione nel prendere il contatore per la risorsa ["+nomeRisorsa+"]: "+e.toString();
			e = new Exception(errorMsg);
			throw e;
		}
	}

	/********************************************************************************************************/
	/********************************************************************************************************/
	public StringType getNumeroOrdineFondi(ClientSessionContext csc, StringType ndg, String tipoMandato) throws Exception{

		try{
			
			if(tipoMandato == null || tipoMandato.length() == 0)
				return new StringType();

			if(ndg == null)
				ndg = new StringType();
			if(!ndg.isNull())
				ndg = new StringType(Tools.fillSx(ndg.toString(), '0', 11));
			
			MapCommandDataModel inOut = new MapCommandDataModel();
			inOut.addProperty("ndg", ndg);
			inOut.addProperty("tipoMandato", new StringType(tipoMandato));
			new DAOObject(csc, DAO_BASE_DRIVER_XML_NAME).executeQASAccess("getNumeroOrdineFondi",inOut);
			StringType numeroOrdine = (StringType)inOut.readProperty("numeroOrdine");
			if(numeroOrdine == null)
				numeroOrdine = new StringType();
			return numeroOrdine;

		}catch(DAOException daoe){
			String errorMsg = "Eccezione DAO nella chiamata al servizio numero ordine fondi per ndg ["+ndg+"] tipoMandato ["+tipoMandato+"]: "+daoe.toString();
			Exception e = new Exception(errorMsg);
			throw e;
		}catch(Exception e){
			String errorMsg = "Eccezione nella chiamata al servizio numero ordine fondi per ndg ["+ndg+"] tipoMandato ["+tipoMandato+"]: "+e.toString();
			e = new Exception(errorMsg);
			throw e;
		}
	}

	/********************************************************************************************************/
	/********************************************************************************************************/
	public StringType generateBarcode(ClientSessionContext csc, String momCode, String momVersion) throws Exception{

		if(momCode.length() == 0 || momVersion.length() == 0)
			throw new Exception("generateBarcode: Parametri non valorizzati");
		
		try{
			String canale = "B";
			if(getPdf().getPdfCompilationMode().equals(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_FIRMA_DIGITALE) ||
			   getPdf().getPdfCompilationMode().equals(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_COPERNICO))				
				canale = "D";
			String barcode = PdfBarcodeUtils.generateBarcode(csc, momCode, momVersion, canale);
			return new StringType(barcode);
		}catch(DAOException daoe){
			String errorMsg = "generateBarcode: Eccezione DAO nel generare il barcode: "+daoe.toString();
			throw new Exception(errorMsg);
		}
	}

	/********************************************************************************************************/
	/********************************************************************************************************/
	public String impostaIbanContoTecnicoPrenotato(ClientSessionContext csc, PdfModel pdf, StringType numeroOIbanContoDiRiferimento) throws Exception{

		if(pdf.isTestMode())
			return null;
		
		if( !pdf.getPdfCompilationMode().equals(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_FIRMA_DIGITALE) && 
			!pdf.getPdfCompilationMode().equals(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_COPERNICO))
			return null;

		if(numeroOIbanContoDiRiferimento.isNull())
			return "Prenotazione conto tecnico: numero conto o iban di riferimento non valorizzato";
		
		try{
			
			DAOObject dao = new DAOObject(csc, DAO_BASE_DRIVER_XML_NAME);

			StringType numeroContoDiRiferimento = numeroOIbanContoDiRiferimento.toString().length()==27 ? new StringType(numeroOIbanContoDiRiferimento.toString().substring(19)) : numeroOIbanContoDiRiferimento;
			
			MapCommandDataModel contoDiRiferimento = new MapCommandDataModel();
			contoDiRiferimento.addProperty("numeroConto", numeroContoDiRiferimento);
			
			BooleanType isContoDiRiferimentoPerContoTecnicoScudato = (BooleanType)dao.executeQueryAccess("isContoScudato", contoDiRiferimento).getSingleResult();
			if(isContoDiRiferimentoPerContoTecnicoScudato == null)
				return "Errore nel recuperare l'informazione 'Conto Scudato' per il conto numero ["+numeroOIbanContoDiRiferimento+"]";
			
			MapCommandDataModel contoTecnico = new MapCommandDataModel();
			contoTecnico.addProperty("categoria",new StringType(isContoDiRiferimentoPerContoTecnicoScudato.booleanValue() ? "0099" : "0043"));
			contoTecnico.addProperty("sottocategoria",new StringType(isContoDiRiferimentoPerContoTecnicoScudato.booleanValue() ? "0043": "0001"));
			
			DAOQASResultModel qasRes = dao.executeQASAccess("prenotaContoTecnico",contoTecnico);
			if(qasRes.getQasCallData().getStatus() != QASCallData.STATUS_OK)
				return "Errore di comunicazione col servizio di prenotazione conto tecnico: "+qasRes.getQasCallData().getMessage();
			
			StringType numeroContoTecnicoPrenotato = (StringType)contoTecnico.readProperty("numeroContoTecnicoPrenotato");
			if(numeroContoTecnicoPrenotato == null || numeroContoTecnicoPrenotato.isNull())
				return "Il servizio di prenotazione conto tecnico non ha restituito un numero conto valido";
			
			StringType cinContoTecnicoPrenotato = (StringType)contoTecnico.readProperty("cin");
			if(cinContoTecnicoPrenotato == null || cinContoTecnicoPrenotato.isNull())
				return "Il servizio di prenotazione conto tecnico non ha restituito un cin conto valido";
			cinContoTecnicoPrenotato = new StringType(Tools.fillSx(cinContoTecnicoPrenotato.toString(),'0',2));
			
			StringType ibanPaese = (StringType)contoTecnico.readProperty("ibanPaese");
			StringType ibanCin1 = (StringType)contoTecnico.readProperty("ibanCin1");
			StringType ibanCin2 = (StringType)contoTecnico.readProperty("ibanCin2");
			StringType ibanAbi = (StringType)contoTecnico.readProperty("ibanAbi");
			StringType ibanCab = (StringType)contoTecnico.readProperty("ibanCab");
			StringType ibanConto = (StringType)contoTecnico.readProperty("ibanConto");
			StringType ibanContoTecnicoPrenotato = new StringType(""+ibanPaese+ibanCin1+ibanCin2+ibanAbi+ibanCab+ibanConto);

			if(pdf.isMultiPdf()){
				for(int i=0;i<pdf.getPdfData().getPdfs().size();i++){
					PdfDataModel pdfDataElement = (PdfDataModel)pdf.getPdfData().getPdfs().get(i);
					pdfDataElement.addProperty("isContoDiRiferimentoPerContoTecnicoScudato", isContoDiRiferimentoPerContoTecnicoScudato);
					pdfDataElement.addProperty("numeroContoTecnicoPrenotato", numeroContoTecnicoPrenotato);
					pdfDataElement.addProperty("cinContoTecnicoPrenotato", cinContoTecnicoPrenotato);
					pdfDataElement.addProperty("ibanContoTecnicoPrenotato", ibanContoTecnicoPrenotato);
				}
			}else{
				pdf.getPdfData().addProperty("isContoDiRiferimentoPerContoTecnicoScudato", isContoDiRiferimentoPerContoTecnicoScudato);
				pdf.getPdfData().addProperty("numeroContoTecnicoPrenotato", numeroContoTecnicoPrenotato);
				pdf.getPdfData().addProperty("cinContoTecnicoPrenotato", cinContoTecnicoPrenotato);
				pdf.getPdfData().addProperty("ibanContoTecnicoPrenotato", ibanContoTecnicoPrenotato);
			}
			return null;
			
		}catch(DAOException daoe){
			String errorMsg = "Eccezione DAO nella chiamata al servizio di prenotazione conto tecnico: "+daoe.toString();
			Exception e = new Exception(errorMsg);
			throw e;
		}catch(Exception e){
			String errorMsg = "Eccezione nella chiamata al servizio di prenotazione conto tecnico: "+e.toString();
			e = new Exception(errorMsg);
			throw e;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean ctrl_luogo(ClientSessionContext csc, PdfDataModel pdfData, String luogoFieldName){

		StringType luogo = (StringType)pdfData.read(luogoFieldName);
		if(luogo == null || luogo.isNull())
			return true;
		
		try{
			
			LuogoAutocompleteModel input = new LuogoAutocompleteModel();
			input.setLuogo(luogo);
			DAOQueryResultModel qRes = new DAOObject(csc, DAO_DATAENTRY_UTIL_XML_NAME).executeQueryAccess("luogoAutocomplete", input);
			if(qRes.getResult().size() == 0){
				luogo.addTypeError("Il luogo specificato non esiste.");
				return false;
			}
			return true;
			
		} catch (DAOException daoe) {
			luogo.addTypeError("Problema tecnico nel recuperare i dati del luogo.");
			return false;
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean ctrl_descrToponimo(ClientSessionContext csc, PdfDataModel pdfData, String descrToponimoFieldName){

		StringType descrToponimo = (StringType)pdfData.read(descrToponimoFieldName);
		if(descrToponimo == null || descrToponimo.isNull())
			return true;
		
		try{
			
			ToponimoAutocompleteModel input = new ToponimoAutocompleteModel();
			input.setDescrToponimo(descrToponimo);
			DAOQueryResultModel qRes = new DAOObject(csc, DAO_DATAENTRY_UTIL_XML_NAME).executeQueryAccess("toponimoAutocomplete", input);
			if(qRes.getResult().size() == 0){
				descrToponimo.addTypeError("Il toponimo specificato non esiste.");
				return false;
			}
			return true;
			
		} catch (DAOException daoe) {
			descrToponimo.addTypeError("Problema tecnico nel recuperare i dati del toponimo.");
			return false;
		}
	}
	
	/***********************************************************************************************/
	private static final String DAO_ACCESS_LOAD_BLOCCO_CLIENTE = "loadBloccoCliente";
	/***********************************************************************************************/
	public boolean ctrl_isClienteBloccato(ClientSessionContext csc, PdfDataModel pdfData, String ndgFieldName){

		StringType ndg = (StringType)pdfData.read(ndgFieldName);
		if(ndg == null || ndg.isNull())
			return false;
		
		try{
			
			MapCommandDataModel input = new MapCommandDataModel();
			input.addProperty("ndg", new StringType(Tools.fillSx(ndg.toString(), '0', 11)));
			StringType bloccoCliente = (StringType)new DAOObject(csc, DAO_BASE_DRIVER_XML_NAME).executeQueryAccess(DAO_ACCESS_LOAD_BLOCCO_CLIENTE, input).getSingleResult();
			if(bloccoCliente != null) {
				if(bloccoCliente.equals("DECEDUTO")){
					ndg.addTypeError("Non è possibile procedere, il cliente risulta deceduto.");
					return true;
				}else if(bloccoCliente.equals("REVOCATO")){
					ndg.addTypeError("Non è possibile procedere, il cliente risulta revocato.");
					return true;					
				}
			}
			return false;
			
		} catch (DAOException daoe) {
			ndg.addTypeError("Problema tecnico nel verificare se il cliente è bloccato.");
			return true;
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean ctrl_isClienteDeceduto(ClientSessionContext csc, PdfDataModel pdfData, String ndgFieldName){

		StringType ndg = (StringType)pdfData.read(ndgFieldName);
		if(ndg == null || ndg.isNull())
			return false;
		
		try{
			
			MapCommandDataModel input = new MapCommandDataModel();
			input.addProperty("ndg", new StringType(Tools.fillSx(ndg.toString(), '0', 11)));
			StringType bloccoCliente = (StringType)new DAOObject(csc, DAO_BASE_DRIVER_XML_NAME).executeQueryAccess(DAO_ACCESS_LOAD_BLOCCO_CLIENTE, input).getSingleResult();
			if(bloccoCliente != null && bloccoCliente.equals("DECEDUTO")){
				ndg.addTypeError("Non è possibile procedere, il cliente risulta deceduto.");
				return true;
			}
			return false;
			
		} catch (DAOException daoe) {
			ndg.addTypeError("Problema tecnico nel verificare se il cliente è deceduto.");
			return true;
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean ctrl_isClienteRevocato(ClientSessionContext csc, PdfDataModel pdfData, String ndgFieldName){

		StringType ndg = (StringType)pdfData.read(ndgFieldName);
		if(ndg == null || ndg.isNull())
			return false;
		
		try{
			
			MapCommandDataModel input = new MapCommandDataModel();
			input.addProperty("ndg", new StringType(Tools.fillSx(ndg.toString(), '0', 11)));
			StringType bloccoCliente = (StringType)new DAOObject(csc, DAO_BASE_DRIVER_XML_NAME).executeQueryAccess(DAO_ACCESS_LOAD_BLOCCO_CLIENTE, input).getSingleResult();
			if(bloccoCliente != null && bloccoCliente.equals("REVOCATO")){
				ndg.addTypeError("Non è possibile procedere, il cliente risulta revocato.");
				return true;					
			}
			return false;
			
		} catch (DAOException daoe) {
			ndg.addTypeError("Problema tecnico nel verificare se il cliente è revocato.");
			return true;
		}
	}
	
	/***********************************************************************************************/
	public static String COLLOCAMENTO_PRODOTTI_GESTITO = "COLLOCAMENTO_PRODOTTI_GESTITO";
	/***********************************************************************************************/
	public boolean ctrl_isAgenteAbilitatoAlCollocamento(ClientSessionContext csc, PdfDataModel pdfData, String tipoCollocamento){

		StringType codiceAgente =  (StringType)pdfData.read(PdfPredefinedFields.AGENTE_CODICE);
		if(codiceAgente == null || codiceAgente.isNull())
			return true;
		
		try{
			String sezioneConfig = "CONTROLLO_AGENTE_ABILITATO_AL_COLLOCAMENTO";
			
			List<String> codiciNonAmmessi = PdfConfig.getParamAsStringArray(csc, sezioneConfig, "CODICI_AGENTE_NON_AMMESSI", "0");
			if(codiciNonAmmessi.contains(codiceAgente.toString())){
				codiceAgente.addTypeError("Codice Family Banker non ammesso.");
				return false;											
			}

			DAOObject dao = new DAOObject(csc, DAO_BASE_DRIVER_XML_NAME);
			
			//attivo e ruolo
			MapCommandDataModel fbData = new MapCommandDataModel();
			fbData.addProperty(PdfPredefinedFields.AGENTE_CODICE, codiceAgente);
			fbData.addProperty("codStato", new StringType());
			fbData.addProperty("codStatoGiuridico", new StringType());
			DAOQueryResultModel qRes = dao.executeQueryAccess("loadStatoFB", fbData);
			if(qRes.getResult().size() == 0) {
				codiceAgente.addTypeError("Family Banker non presente in anagrafica.");
				return false;				
			}
			
			StringType codStato = (StringType)fbData.readProperty("codStato");
			List<String> statiAmmessi =  PdfConfig.getParamAsStringArray(csc, sezioneConfig, "STATI_AGENTE_AMMESSI", "ATT");
			if(!statiAmmessi.contains(codStato.toString())){
				codiceAgente.addTypeError("Family Banker non attivo.");
				return false;				
			}

			if(tipoCollocamento.equals(COLLOCAMENTO_PRODOTTI_GESTITO)) {
				StringType codStatoGiuridico = (StringType)fbData.readProperty("codStatoGiuridico");
				List<String> statiGiuridiciAmmessi = PdfConfig.getParamAsStringArray(csc, sezioneConfig, "STATI_GIURIDICI_COLLOCAMENTO_PRODOTTI_GESTITO", "PRFIN,DDBMP");
				if(!statiGiuridiciAmmessi.contains(codStatoGiuridico.toString())){
					codiceAgente.addTypeError("Family Banker non abilitato al collocamento del prodotto.");
					return false;								
				}
			}
			
			//data mandato versus data sottoscrizione contratto
			DateType dataSottoscrizioneContratto = (DateType)pdfData.read(PdfPredefinedFields.DATA_SOTTOSCRIZIONE);
			if(dataSottoscrizioneContratto != null && !dataSottoscrizioneContratto.isNull()) {
				DateType dataValiditaMandatoFB = (DateType)dao.executeQueryAccess("loadDataValiditaMandatoFB", fbData).getSingleResult();
				if(dataValiditaMandatoFB == null || dataValiditaMandatoFB.isNull()){
					dataSottoscrizioneContratto.addTypeError("Data mandato non presente in anagrafica.");
					return false;
				}else if(dataValiditaMandatoFB.compareTo(dataSottoscrizioneContratto) > 0){
					dataSottoscrizioneContratto.addTypeError("La data di sottoscrizione del contratto non può essere antecedente la data di validità del mandato Family Banker.");
					return false;
				}
			}
			return true;
			
		} catch (DAOException daoe) {
			codiceAgente.addTypeError("Problema DAO nel verificare se l'agente è abilitato al collocamento");		
			return false;
		} catch (Exception e) {
			codiceAgente.addTypeError("Problema tecnico nel verificare se l'agente è abilitato al collocamento");		
			return false;
		}		
	}
	
	/***********************************************************************************************/
	/*
	 * CONTO CLIENTE
	 */
	/***********************************************************************************************/
	public ComuneAutocompleteModel readComune(ClientSessionContext csc, PdfDataModel pdfData, String comuneName) throws Exception {
		try {
			
			StringType provinciaComune = (StringType)pdfData.read("provinciaComune"+comuneName);
			StringType capComune = (StringType)pdfData.read("capComune"+comuneName);
			StringType comune = (StringType)pdfData.read("comune"+comuneName);
			if((provinciaComune == null || provinciaComune.isNull()) || (capComune == null || capComune.isNull()) || (comune == null || comune.isNull()))
				return null;
			
			ComuneAutocompleteModel input = new ComuneAutocompleteModel();
			input.setProvinciaComune(provinciaComune);
			input.setCapComune(capComune);
			input.setComune(comune);
			DAOQueryResultModel qRes = new DAOObject(csc, DAO_DATAENTRY_UTIL_XML_NAME).executeQueryAccess("comuneAutocomplete", input);
			if(qRes.getResult().size() == 0){
				ComuneAutocompleteModel res = new ComuneAutocompleteModel();
				res.setNotFound(true);
				return res;
			}
			return (ComuneAutocompleteModel)qRes.getResult().get(0);
			
		} catch (DAOException daoe) {
			throw new Exception(daoe.toString());
		}

	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean ctrl_comune(ClientSessionContext csc, PdfDataModel pdfData, String comuneName){
		return ctrl_comune(csc, pdfData, comuneName, null);
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean ctrl_comune(ClientSessionContext csc, PdfDataModel pdfData, String comuneName, ComuneAutocompleteModel comune){
		
		ArrayList<String> fieldNames = new ArrayList<String>();
		fieldNames.add("provinciaComune"+comuneName);
		fieldNames.add("capComune"+comuneName);
		fieldNames.add("comune"+comuneName);

		try{
			
			if(comune == null)
				comune = readComune(csc, pdfData, comuneName);
			if(comune == null)
				return true;
			if(comune.isNotFound()){
				putError(pdfData, fieldNames, "Il comune specificato non esiste.");
				return false;
			}
			return true;

		}catch(Exception e){
			putError(pdfData, fieldNames, "Problema tecnico nel recuperare i dati del comune.");
			return false;
		}		
	}
	
	
	/***********************************************************************************************/
	/*
	 * DATI CLIENTE DELL'FB
	 */
	/***********************************************************************************************/
	public PdfPersonModel readCliente(ClientSessionContext csc, PdfDataModel pdfData, int personIdx) throws Exception {				
		StringType codCliente = (StringType)pdfData.read(PdfPredefinedFields.CLIENTE_NDG_PREFIX+personIdx);
		if(codCliente == null || codCliente.isNull())
			codCliente = (StringType)pdfData.read(PdfPredefinedFields.CLIENTE_ID_CENSIMENTO_PREFIX+personIdx);
		if(codCliente == null || codCliente.isNull())
			return null;
		
		return readCliente(csc, pdfData, codCliente.toString());
	}	
	
	/***********************************************************************************************/
	/*
	 * DATI CLIENTE DELL'FB
	 */
	/***********************************************************************************************/
	public PdfPersonModel readCliente(ClientSessionContext csc, PdfDataModel pdfData, String codCliente) throws Exception {
		try{
			if(getPdf().isOperatoreMOM()) {
				StringType codAgente = (StringType)pdfData.read(PdfPredefinedFields.AGENTE_CODICE);
				if(codAgente == null)
					codAgente = new StringType();
				return DataLoader.loadPersonOnDriverForMom(csc, getPdf(), codCliente, codAgente.toString());
			}

			StringType codAgente = pdfData.getCodAgeImpersonato();
			if(codAgente == null || codAgente.isNull()) {
				codAgente = (StringType)pdfData.read(PdfPredefinedFields.AGENTE_CODICE);
				if(codAgente == null || codAgente.isNull())
					return null;
			}
			return DataLoader.loadPersonOnDriver(csc, getPdf(), codCliente, codAgente.toString());
			
		} catch (DAOException daoe) {
			throw new Exception(daoe.toString());
		}
	}	
	
	/***********************************************************************************************/
	/*
	 * DATI CLIENTE A PRESCINDERE DALL'FB
	 */
	/***********************************************************************************************/
	public PdfPersonModel readAnyCliente(ClientSessionContext csc, String codCliente) throws Exception {
		try{
			return DataLoader.loadAnyPersonOnDriver(csc, getPdf(), codCliente);			
		} catch (DAOException daoe) {
			throw new Exception(daoe.toString());
		}
	}	

	public static String PROFILO_MIFID_PROVVISORIO = "provvisorio";
	public static String PROFILO_MIFID_VALIDATO = "validato";

	/***********************************************************************************************/
	/*
	 * PROFILO MIFID PROVVISORIO
	 */
	/***********************************************************************************************/
	public ProfiloMifidModel readProfiloMifid(ClientSessionContext csc, PdfDataModel pdfData, int personIdx) throws Exception {
		return readProfiloMifid(csc, pdfData, personIdx, PROFILO_MIFID_PROVVISORIO);
	}
	
	/***********************************************************************************************/
	/*
	 * tipoProfilo: PdfBaseDriverUtil.PROFILO_MIFID_PROVVISORIO (default), 
	 * 				PdfBaseDriverUtil.PROFILO_MIFID_VALIDATO
	 */
	/***********************************************************************************************/
	public ProfiloMifidModel readProfiloMifid(ClientSessionContext csc, PdfDataModel pdfData, int personIdx, String tipoProfilo) throws Exception {

		if(getPdf() != null && getPdf().isOperatoreMOM()) // MOP: Per l'operatore MOM usiamo sempre e comunque il profilo validato
			tipoProfilo = PROFILO_MIFID_VALIDATO;
		
		StringType idCliente = (StringType)pdfData.read("ndgCliente"+personIdx);
		if(idCliente == null || idCliente.isNull())
			idCliente = (StringType)pdfData.read("idCensimentoCliente"+personIdx);
		else
			idCliente = new StringType(Tools.fillSx(idCliente.toString(), '0', 11));
		if(idCliente == null || idCliente.isNull())
			return null;
		
		MapCommandDataModel input = new MapCommandDataModel();
		input.addProperty("idConversazione", new StringType(UUID.randomUUID().toString()));
		input.addProperty("timeStamp", Tools.now());
		input.addProperty("userId", new StringType(csc.getUserCode()));
		input.addProperty("idCliente", idCliente);
		input.addProperty("flagProvvisorio", new StringType(tipoProfilo.equals(PROFILO_MIFID_PROVVISORIO)?"S":"N"));

		ProfiloMifidModel profilo = new ProfiloMifidModel();
		try {
			DAOOSBResultModel wsRes = new DAOObject(csc, DAO_BASE_DRIVER_XML_NAME).executeOSBAccess("readProfiloMifid", input);
			if(wsRes.getWsCallData().getStatus() == XmlServiceCallData.STATUS_OK) {
				profilo = (ProfiloMifidModel)wsRes.getResult();
				if(profilo.getProfilo().isNull())
					profilo.setNotFound(true);
			}else {
				profilo.setNotFound(true);
			}
		}catch(DAOException daoe) {
			profilo.setNotFound(true);
		}
		return profilo;			

	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean ctrl_profiloMifid(ClientSessionContext csc, PdfDataModel pdfData, int personIdx){
		return ctrl_profiloMifid(csc, pdfData, personIdx, null);
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean ctrl_profiloMifid(ClientSessionContext csc, PdfDataModel pdfData, int personIdx, ProfiloMifidModel profiloMifid){
		ArrayList<String> putErrorOn = new ArrayList<String>();
		putErrorOn.add("cognomeCliente"+personIdx);
		putErrorOn.add("nomeCliente"+personIdx);
		putErrorOn.add("ndgCliente"+personIdx);
		putErrorOn.add("codiceFiscaleCliente"+personIdx);
		putErrorOn.add("cognomeNomeCliente"+personIdx);
		putErrorOn.add("nomeCognomeCliente"+personIdx);
		putErrorOn.add("partitaIvaCliente"+personIdx);
		putErrorOn.add("codiceFiscalePartitaIvaCliente"+personIdx);
		return ctrl_profiloMifid(csc, pdfData, personIdx, profiloMifid, putErrorOn);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean ctrl_profiloMifid(ClientSessionContext csc, PdfDataModel pdfData, int personIdx, ProfiloMifidModel profiloMifid, ArrayList<String> putErrorOn){
		return ctrl_profiloMifid(csc, pdfData, personIdx, profiloMifid, putErrorOn, PROFILO_MIFID_PROVVISORIO);
	}
	
	/***********************************************************************************************/
	/*
	 * tipoProfilo: PdfBaseDriverUtil.PROFILO_MIFID_PROVVISORIO (default), 
	 * 				PdfBaseDriverUtil.PROFILO_MIFID_VALIDATO
	 */
	/***********************************************************************************************/
	public boolean ctrl_profiloMifid(ClientSessionContext csc, PdfDataModel pdfData, int personIdx, ProfiloMifidModel profiloMifid, 
									 ArrayList<String> putErrorOn, String tipoProfilo){

		try{
			
			if(profiloMifid == null)
				profiloMifid = readProfiloMifid(csc, pdfData, personIdx, tipoProfilo);
			if(profiloMifid == null)
				return true;
			
			if(profiloMifid.isNotFound() && !skipCtrlProfiloMifidPerMOM(csc)){
				putError(pdfData, putErrorOn, "Il profilo del cliente risulta scaduto o non compilato, non è possibile procedere con la richiesta.");
				return false;
			}
			
			if(!profiloMifid.getDataScadenza().isNull() && profiloMifid.getDataScadenza().compareTo(Tools.today()) <= 0) {
				putError(pdfData, putErrorOn, "Il profilo del cliente risulta scaduto o non compilato, non è possibile procedere con la richiesta.");
				return false;
			}
			return true;

		}catch(Exception e){
			putError(pdfData, putErrorOn, "Problema tecnico nel recuperare i dati del profilo Mifid.");
			return false;
		}		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean skipCtrlProfiloMifidPerMOM(ClientSessionContext csc) {
		if(getPdf() == null || !getPdf().isOperatoreMOM())
			return false;
		try {
			StringType date = PdfConfig.getParamAsString(csc, "MOP", "PERIODO_DI_FORZATURA_CONTROLLO_PCP");
			if(!date.isNull()) {
				String[] dateAsArray = date.toString().trim().split("\\s+");
				DateType dataInizio = new DateType(dateAsArray[0].trim());
				DateType dataFine = new DateType(dateAsArray[1].trim());
				DateType oggi = Tools.today();
				if(oggi.compareTo(dataInizio) >= 0 && oggi.compareTo(dataFine) <= 0)
					return true;
			}
		}catch(Exception e) {
			// do nothing
		}
		return false;
	}
	
	/***********************************************************************************************/
	/*
	 * CONTO CLIENTE
	 */
	/***********************************************************************************************/
	public ContoAutocompleteModel readConto(ClientSessionContext csc, PdfDataModel pdfData, ContoAutocompleteInput input) throws Exception {
		try {
			
			StringType codAgente = new StringType();
			if(input.isUseCodAgente() && !getPdf().isOperatoreMOM())
				codAgente = (StringType)pdfData.read(PdfPredefinedFields.AGENTE_CODICE);
			if(input.isUseCodAgente() && !pdfData.getCodAgeImpersonato().isNull())
				codAgente = pdfData.getCodAgeImpersonato();
			StringType ndgClienti = getNdgClienti(input.getNdgFieldName(), pdfData);
			StringType ibanConto = (StringType)pdfData.read(input.getFieldName());
			if(input.isUseCodAgente() && !getPdf().isOperatoreMOM()){
				if((codAgente == null || codAgente.isNull()) || (ndgClienti.isNull()) || (ibanConto == null || ibanConto.isNull()))
					return null;
			}else{
				if((ndgClienti.isNull()) || (ibanConto == null || ibanConto.isNull()))
					return null;
			}
			
			DAOObject dao = new DAOObject(csc, DAO_DATAENTRY_UTIL_XML_NAME);
			ContoAutocompleteModel inputModel = new ContoAutocompleteModel();
			inputModel.setTipoConto(new StringType(input.getTipoConto()));
			inputModel.setRuoliAmmessi(new StringType(input.getRuoliAmmessi()));
			inputModel.setDivisaConto(new StringType(input.getDivisaConto()));
			inputModel.setCodAgente(codAgente);
			inputModel.setNdgCliente(ndgClienti);
			inputModel.addProperty("ibanConto", ibanConto);
			
			// Se il tipo conto è impostato ne carico la configurazione della "where condition" prima di lanciare la query(default sono i conti correnti)
			inputModel.setWhereTipoConto(new StringType());
			if(!inputModel.getTipoConto().isNull()) 
				inputModel.setWhereTipoConto(PdfConfig.getParamAsString(csc, "WHERE_CONDITION_TIPO_CONTO", inputModel.getTipoConto().toString()));
			DAOQueryResultModel qRes = dao.executeQueryAccess("contoAutocomplete", inputModel);
			if(qRes.getResult().size() == 0){
				ContoAutocompleteModel res = new ContoAutocompleteModel();
				res.setNotFound(true);
				return res;
			}

			ContoAutocompleteModel conto = (ContoAutocompleteModel)qRes.getResult().get(0);
			conto.setNominativiConto(dao.executeQueryAccess("nominativiContoAutocomplete",conto).getResult());
			return conto;
			
		} catch (DAOException daoe) {
			throw new Exception(daoe.toString());
		}

	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private StringType getNdgClienti(String ndgFieldNames, PdfDataModel pdfData) {
		if(ndgFieldNames.isEmpty())
			return new StringType();
		StringBuilder ndgs = new StringBuilder();
		String[] ndgFieldNamesAsArray = ndgFieldNames.split(",");
		for(String ndgFieldName : ndgFieldNamesAsArray) {
			AbstractType ndg = pdfData.read(ndgFieldName);
			if(ndg != null && !ndg.isNull())
				ndgs.append("'"+Tools.fillSx(ndg.toString(), '0', 11)+"',");
		}
		if(ndgs.length() > 0)
			ndgs.deleteCharAt(ndgs.length()-1);	
		return new StringType(ndgs.toString());
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean ctrl_esistenzaConto(ClientSessionContext csc, PdfDataModel pdfData, ContoAutocompleteInput input){
		try{
			ContoAutocompleteModel conto = readConto(csc, pdfData, input);
			if(conto == null)
				return true;
			if(conto.isNotFound()){
				putError(pdfData, input.getFieldName(), "Il conto specificato non esiste o non appartiene al cliente.");
				return false;
			}
			BooleanType isContoAttivoMaBloccato = (BooleanType)conto.readProperty("isContoAttivoMaBloccato");
			if(isContoAttivoMaBloccato != null && isContoAttivoMaBloccato.booleanValue()){
				putError(pdfData, input.getFieldName(), "Attenzione! Il conto selezionato è bloccato: per proseguire è necessario selezionare un altro conto corrente.");
				return false;
			}
			return true;
		}catch(Exception e){
			putError(pdfData, input.getFieldName(), "Problema tecnico nel recuperare i dati del conto.");
			return false;
		}			
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean ctrl_formatoIban(ClientSessionContext csc, StringType iban){
		if(iban == null || iban.isNull())
			return true;
		
		if(iban.toString().length() != 27){
			iban.addTypeError("Il codice Iban è errato.");
			return false;
		}
		
		if(!InternationalBankAccountNumber.isValidIban(iban.toString().toUpperCase())){
			iban.addTypeError("Il codice Iban è errato.");
			return false;
		}
		return true;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public SaldoContoModel readSaldoConto(ClientSessionContext csc, StringType ibanONumeroConto) throws Exception {
		try {
			
			if(ibanONumeroConto.isNull())
				return null;
			
			String numeroConto = ibanONumeroConto.toString(); 
			if(numeroConto.length() > 8)
				numeroConto = numeroConto.substring(numeroConto.length()-8);
			MapCommandDataModel inOut = new MapCommandDataModel();
			inOut.addProperty("numeroConto", new StringType(numeroConto));
			new DAOObject(csc, DAO_BASE_DRIVER_XML_NAME).executeQASAccess("loadSaldoConto",inOut);
			
			DoubleType saldoContabile = fromCentsToEuro((StringType)inOut.readProperty("saldoContabile"));
			StringType segnoSaldoContabile = inOut.readProperty("segnoSaldoContabile") == null ? new StringType() : (StringType)inOut.readProperty("segnoSaldoContabile");
			
			DoubleType saldoDisponibile = fromCentsToEuro((StringType)inOut.readProperty("saldoDisponibile"));
			StringType segnoSaldoDisponibile = inOut.readProperty("segnoSaldoDisponibile") == null ? new StringType() : (StringType)inOut.readProperty("segnoSaldoDisponibile");
			
			SaldoContoModel result = new SaldoContoModel();
			if(saldoContabile.isNull() || saldoDisponibile.isNull()){
				result.setNotFound(true);
				return result;
			}
			
			if(!segnoSaldoContabile.equals("A")) 
				saldoContabile = new DoubleType(saldoContabile.doubleValue() * -1);
			if(!segnoSaldoDisponibile.equals("A")) 
				saldoDisponibile = new DoubleType(saldoDisponibile.doubleValue() * -1);
			
			result.setSaldoContabile(saldoContabile);
			result.setSaldoDisponibile(saldoDisponibile);
			return result;
			
		} catch (DAOException daoe) {
			throw new Exception(daoe.toString());
		}

	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private DoubleType fromCentsToEuro(StringType importoInCentesimi){
		if(importoInCentesimi == null || importoInCentesimi.isNull())
			return new DoubleType();
		double cent = Double.parseDouble(importoInCentesimi.toString());
		double euro = cent / 100;
		return new DoubleType(new BigDecimal(euro).setScale(2,BigDecimal.ROUND_HALF_UP));
	}
	
	/********************************************************************************************************/
	/********************************************************************************************************/
	public boolean isClienteCorrentista(ClientSessionContext csc, StringType ndg) throws Exception {
		try{
			
			if(ndg == null || ndg.isNull())
				return false;
			
			DAOObject dao = new DAOObject(csc, DAO_DATAENTRY_UTIL_XML_NAME);
			ContoAutocompleteModel inputModel = new ContoAutocompleteModel();
			inputModel.setTipoConto(new StringType("IS_CLIENTE_CORRENTISTA"));
			inputModel.setNdgCliente(ndg);

			// Se il tipo conto è impostato ne carico la configurazione della "where condition" prima di lanciare la query(default sono i conti correnti)
			inputModel.setWhereTipoConto(PdfConfig.getParamAsString(csc, "WHERE_CONDITION_TIPO_CONTO", inputModel.getTipoConto().toString()));
			DAOQueryResultModel qRes = dao.executeQueryAccess("contoAutocomplete", inputModel);
			if(qRes.getResult().size() == 0){
				return false;
			}
			return true;
		}catch(DAOException daoe){
			throw new Exception(daoe.toString());
		}			
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public class AgevolazioneCtrlData{
		public String numeroContrattoSottoscrizione;
		public String modalitaVersamentoSottoscrizione;
		public double importoSottoscrizione;
		public double importoPrimoVersamentoSottoscrizione;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public AgevolazioneModel readAgevolazione(ClientSessionContext csc, String idAgevolazione) throws Exception{
		try{
			AgevolazioneModel agevolazione = new AgevolazioneModel();
			agevolazione.setIdAgevolazione(new IntegerType(idAgevolazione));
			DAOQueryResultModel qRes = new DAOObject(csc, "PdfWebForms.PdfAgevolazioni").executeQueryAccess("loadAgevolazione", agevolazione);
			if(qRes.getResult().size() < 1)
				return null;
			return agevolazione;
		}catch(DAOException daoe){
			throw new Exception(daoe.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean ctrl_agevolazione(ClientSessionContext csc, PdfDataModel pdfData, AgevolazioneCtrlData agevolazioneCtrlData){
		
		StringType codiceAgevolazione = (StringType)pdfData.read("codiceAgevolazione");
		if(codiceAgevolazione == null)
			return true;
		
		StringType tipoAgevolazione = (StringType)pdfData.read("tipoAgevolazione");
		if(tipoAgevolazione == null)
			tipoAgevolazione = new StringType(PdfPredefinedFields.TIPO_AGEVOLAZIONE_ALTRO);

		try{
		
			if(tipoAgevolazione.equals(PdfPredefinedFields.TIPO_AGEVOLAZIONE_ALTRO)){
				
				StringType idAgevolazione = (StringType)pdfData.read("idAgevolazione");
				if(idAgevolazione == null) // Se l'agevolazione on è gestita nel pdf -> ok
					return true;
				
				if(codiceAgevolazione.isNull()){
					codiceAgevolazione.addTypeError("Campo obbligatorio");
					return false;
				}
	
				if(idAgevolazione.isNull()) // Se l'agevolazione non ha un id (tipo le agevolazioni dipendenti) -> ok
					return true;
				
				if(idAgevolazione.toString().startsWith("S")){ // Vecchie agevolazioni
					codiceAgevolazione.addTypeError("L'agevolazione selezionata non è più valida. Selezionarne o richiederne una nuova.");
					return false;
				}
				
				AgevolazioneModel agevolazione = readAgevolazione(csc, idAgevolazione.toString());
				if(agevolazione == null){
					putError(pdfData, "codiceAgevolazione", "Agevolazione ["+idAgevolazione+"] non trovata");
					return false;
				}
				
				String modalitaVersamentoSottoscrizione = agevolazioneCtrlData.modalitaVersamentoSottoscrizione;
				if(modalitaVersamentoSottoscrizione==null) 
					modalitaVersamentoSottoscrizione="";
				
				if(!agevolazione.getModalitaVersamentoAgevolazione().isNull()){
					String [] modalitaVersamentoSottoscrizioneAsArray = modalitaVersamentoSottoscrizione.toUpperCase().split(",");
					ArrayList<String> modalitaVersamentoSottoscrizioneAsList = new ArrayList<>(Arrays.asList(modalitaVersamentoSottoscrizioneAsArray));
					if(!modalitaVersamentoSottoscrizioneAsList.contains(agevolazione.getModalitaVersamentoAgevolazione().toString().toUpperCase())){
						putError(pdfData, "codiceAgevolazione", "La forma contrattuale indicata non è coerente con quanto specificato nella deroga selezionata.");
						return false;
					}
				}

				if(!agevolazione.getNumeroContrattoDestinazione().isNull()){
					String numeroContrattoSottoscrizione = agevolazioneCtrlData.numeroContrattoSottoscrizione;
					if(numeroContrattoSottoscrizione != null && numeroContrattoSottoscrizione.length() > 0){
						numeroContrattoSottoscrizione = Tools.fillSx(numeroContrattoSottoscrizione, '0', 20);
						String numeroContrattoAgevolazione = Tools.fillSx(agevolazione.getNumeroContrattoDestinazione().toString(), '0', 20);
						if(!numeroContrattoSottoscrizione.equalsIgnoreCase(numeroContrattoAgevolazione)){
							putError(pdfData, "codiceAgevolazione", "Il numero mandato indicato non è coerente con quanto specificato nella deroga selezionata.");
							return false;
						}
					}
				}

				DoubleType importoAgevolazione = agevolazione.getImportoAgevolazione();
				if(importoAgevolazione.isNull())
					return true;
				
				if(modalitaVersamentoSottoscrizione.length() == 0)
					return true;
				
				if(agevolazione.getModalitaVersamentoAgevolazione().equalsIgnoreCase("PAC")){
					if(!agevolazione.getApplicazioneDeroga().isNull()){
						String applicazioneDeroga = agevolazione.getApplicazioneDeroga().toString();
						if(applicazioneDeroga.equalsIgnoreCase("primo versamento")){
							if(importoAgevolazione.doubleValue() < agevolazioneCtrlData.importoPrimoVersamentoSottoscrizione){
								putError(pdfData, "codiceAgevolazione", "La deroga selezionata non è coerente con l'importo del versamento iniziale richiesto.");
								return false;
							}
						}else{
							if(importoAgevolazione.doubleValue() < agevolazioneCtrlData.importoSottoscrizione){
								putError(pdfData, "codiceAgevolazione", "La deroga selezionata non è coerente con il totale dell'investimento richiesto.");
								return false;
							}
						}
					}
				}else if(agevolazione.getModalitaVersamentoAgevolazione().equalsIgnoreCase("PIC")){
					if(importoAgevolazione.doubleValue() < agevolazioneCtrlData.importoSottoscrizione){
						putError(pdfData, "codiceAgevolazione", "La deroga selezionata non è coerente con il totale dell'investimento richiesto.");
						return false;
					}
				}
				
			}else if(tipoAgevolazione.equals("DIPENDENTI")){
				StringType descrizioneAgevolazioneDipendenti = (StringType)pdfData.read("descrizioneAgevolazioneDipendenti");
				if(descrizioneAgevolazioneDipendenti != null && descrizioneAgevolazioneDipendenti.isNull()){
					putError(pdfData, "descrizioneAgevolazioneDipendenti", "Campo obbligatorio");
					return false;
				}
			}
			return true;
			
		}catch(Exception e){
			putError(pdfData, "codiceAgevolazione", "Problema tecnico durante la verifica della agevolazione, non è possibile procedere con la richiesta.");
			return false;
		}

	}
}
