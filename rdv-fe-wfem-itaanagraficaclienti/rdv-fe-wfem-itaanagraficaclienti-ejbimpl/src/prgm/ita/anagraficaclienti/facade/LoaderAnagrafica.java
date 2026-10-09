package prgm.ita.anagraficaclienti.facade;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.cedacri.adeguatezza.model.OutputGetQuestionarioModel;
import prgm.ita.anagraficaclienti.cogestione.CogestioneDataManager;
import prgm.ita.anagraficaclienti.model.ClienteModel;
import prgm.ita.anagraficaclienti.model.ComuneModel;
import prgm.ita.anagraficaclienti.model.IndirizzoModel;
import prgm.ita.anagraficaclienti.model.TelefonoModel;
import prgm.ita.anagraficaclienti.model.UniversitaModel;
import prgm.ita.anagraficaclienti.model.VariazioneKeyModel;
import prgm.ita.anagraficaclienti.pcp.ProfiloPcpClienteLoader;
import prgm.ita.anagraficaclienti.pcp.ProfiloPcpClienteModel;
import prgm.ita.anagraficaclienti.pcp.ProfiloPcpToCedacriMapper;

/***********************************************************************************************/
/***********************************************************************************************/
public class LoaderAnagrafica {

    private static com.atosorigin.wfem.util.Logger LOG = com.atosorigin.wfem.util.Logger.getInstance();	
    
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void loadCliente(ClientSessionContext csc, DAOObject dao, ClienteModel model, 
								   boolean aggiornatoAllUltimaVersione, boolean leggiVariazioni,
								   boolean isLight) throws DAOException, Exception{

		if(isLight){
			//  Sonar - isLight non è eliminabile
		}
		
		dao.executeQueryAccess("loadDatiCliente",model);				
		
		if(model.getIsPotenziale().booleanValue())
			dao.executeQueryAccess("loadDatiAggiuntiviCensimento",model);
		
		if(model.getIsEffettivo().booleanValue())
			dao.executeQueryAccess("loadDatiAggiuntiviEffettivo",model);
		
		loadDatiCliente(csc,dao,model);
		
		if(model.getIsPersonaGiuridica().booleanValue()){
			dao.executeQueryAccess("loadInfoSocietariePGiuridica",model);
			dao.executeQueryAccess("loadLegaleRappresentantePGiuridica",model);
		}

		ClienteModel clienteOriginale = (ClienteModel)Tools.cloneObject(model);
		
		if(model.getIsEffettivo().booleanValue() && aggiornatoAllUltimaVersione){
			
			DAOQueryResultModel qRes = dao.executeQueryAccess("getProgressivoUltimaVariazione",model);
			IntegerType progressivo = (IntegerType)qRes.getSingleResult();
			if(progressivo != null && !progressivo.isNull()){
				StringType savNomeTabella = model.getDatiApplicativi().getNomeTabella();
				
				model.initialize();
				
				model.setProgressivo(progressivo);
				model.getDatiApplicativi().setNomeTabella(new StringType(MappaturaTabelle.CLI_ELETTR));
				
				dao.executeTableLoadAccess("propostaCliente",model);
				loadDatiCliente(csc,dao,model);				
				
				// Se il domicilio (indirizzo e telefono) in variazione non era stato salvato lo imposto a quello originale
				if(!model.getDomicilio().getIndirizzo().isExistOnDB())
					model.setDomicilio(clienteOriginale.getDomicilio());
				if(!model.getRecapiti().getTelefonoDomicilio().isExistOnDB())
					model.getRecapiti().setTelefonoDomicilio(clienteOriginale.getRecapiti().getTelefonoDomicilio());
				
				model.setCodMediolanum(clienteOriginale.getCodMediolanum());
				model.setProgressivo(clienteOriginale.getProgressivo());
				model.setStatoProposta(clienteOriginale.getStatoProposta());				
				model.getDatiApplicativi().setNomeTabella(savNomeTabella);
				
				//Il codice cluste è sempre prso dal cliente originale
				model.setCodCluster(clienteOriginale.getCodCluster());
			}
		}
		
		if(model.getIsEffettivo().booleanValue() && leggiVariazioni)
			loadVariazioni(dao,model);
		
		impostaDatiInLettura(model);
		impostaDatiInLettura(clienteOriginale);

		// Aggiorno le informazioni sulle nazioni fiscali (non sono salvate con il cliente)
		dao.executeQueryAccess("loadInfoCodiceFiscale2",model);
		dao.executeQueryAccess("loadInfoCodiceFiscale3",model);			
		
		model.getDatiApplicativi().setClienteOriginale(clienteOriginale);
		
		ClienteModel clienteCaricato = (ClienteModel)Tools.cloneObject(model);
		model.getDatiApplicativi().setClienteCaricato(clienteCaricato);
		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static ClienteModel loadVariazione(ClientSessionContext csc, DAOObject dao, VariazioneKeyModel variazioneKey,
											  ClienteModel clienteOriginale) throws DAOException, Exception{

		ClienteModel model = new ClienteModel();
		Tools.copyCommandDataModel(variazioneKey,model);
		model.setProgressivo(variazioneKey.getProgressivo());
		model.getDatiApplicativi().setNomeTabella(new StringType(MappaturaTabelle.CLI_ELETTR));
		model.getDatiApplicativi().setVariazione(true);

		dao.executeTableLoadAccess("propostaCliente",model);
		loadDatiCliente(csc,dao,model);
		
		impostaDatiInLettura(model);
		
		model.getDatiApplicativi().setClienteOriginale(clienteOriginale);
		return model;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void loadDatiCliente(ClientSessionContext csc, DAOObject dao, ClienteModel model) throws DAOException, Exception{

		loadComune(dao,model.getComuneNascita());
		loadAgente(csc,dao,model);
		loadUniversita(dao,model.getInfoPersonali().getUniversita());
		loadResidenza(dao,model);
		loadDomicilio(dao,model);
		loadRecapiti(dao,model);
		
		// Carico il profilo cliente
		ProfiloPcpClienteModel profiloPcp = ProfiloPcpClienteLoader.loadProfiloPcp(csc, model, "S");
		model.setProfiloPcp(profiloPcp);
		
		// Mantengo comunque anche le vecchie info cedacri impostate con le nuove pcp
		OutputGetQuestionarioModel cedOutModel = ProfiloPcpToCedacriMapper.fromProfiloPcpToProfiloCedacri(profiloPcp);
		model.setVersioneQuestionario(cedOutModel.getRelease());
		model.setCodProfiloDiInvestimento(cedOutModel.getProfilo());
		model.setCodClusterCedacri(cedOutModel.getCluster());
		model.setDescrClusterCedacri(cedOutModel.getDesCluster());
		model.setDFinValCedacri(cedOutModel.getDfinval());
		model.setDtCompQuestCedacri(cedOutModel.getDatComp());
		model.setSeValid(cedOutModel.getSeValid());
		model.setEspfina(cedOutModel.getEspfina());

		// *******************************************************************************************
		// Imposto per compatibilità con le altre applicazioni (MHD) i campi che nel tempo 
		// hanno cambiato modello contenitore
		model.getInfoPersonali().setEmail(new StringType(model.getRecapiti().getEmail().toString()));
		model.getResidenza().setTelefono(model.getRecapiti().getTelefonoResidenza());
		model.getDomicilio().setTelefono(model.getRecapiti().getTelefonoDomicilio());
		// *******************************************************************************************
				
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void impostaDatiInLettura(ClienteModel model) {
		
		// Tipo persona
		if(model.getIsMaschio().booleanValue() || model.getIsFemmina().booleanValue())
			model.setTipoPersona(new StringType(Costanti.TIPO_PERSONA_FISICA));
		else
			model.setTipoPersona(new StringType(Costanti.TIPO_PERSONA_GIURIDICA));

		// Natura giuridica
		if(model.getNaturaGiuridica().equals(Costanti.NATURA_GIURIDICA_DITTA_MASCHIO)){
			model.setTipoPersona(new StringType(Costanti.TIPO_PERSONA_FISICA));
			model.setSesso(new StringType(Costanti.SESSO_MASCHIO));
		}else if(model.getNaturaGiuridica().equals(Costanti.NATURA_GIURIDICA_DITTA_FEMMINA)){
			model.setTipoPersona(new StringType(Costanti.TIPO_PERSONA_FISICA));
			model.setSesso(new StringType(Costanti.SESSO_FEMMINA));
		}else if(model.getNaturaGiuridica().equals(Costanti.NATURA_GIURIDICA_PERSONA_MASCHIO)){
			model.setTipoPersona(new StringType(Costanti.TIPO_PERSONA_FISICA));
			model.setSesso(new StringType(Costanti.SESSO_MASCHIO));			
		}else if(model.getNaturaGiuridica().equals(Costanti.NATURA_GIURIDICA_PERSONA_FEMMINA)){
			model.setTipoPersona(new StringType(Costanti.TIPO_PERSONA_FISICA));
			model.setSesso(new StringType(Costanti.SESSO_FEMMINA));			
		}
				
		//////////////////////////////////////////////////////////////////////////////////////
		// Porcherie imposta da cedacri !
		// Il campo CLI_C_LUOGO_PROF contiene il codice UIC della nazione se != Italia
		// altrimenti il codice provincia
		model.getInfoPersonali().getNazioneSvolgimentoProfessione().setStringValue("");
		model.getInfoPersonali().getProvinciaSvolgimentoProfessione().setStringValue("");
		if(!model.getInfoPersonali().getNazOProvSvolgProf().isNull()){
			if(model.getInfoPersonali().getNazOProvSvolgProf().toString().length() > 2){ 	// Non italia
				model.getInfoPersonali().setNazioneSvolgimentoProfessione(new StringType(model.getInfoPersonali().getNazOProvSvolgProf().toString()));
			}else{																			// Italia, il campo continene la provincia
				model.getInfoPersonali().setNazioneSvolgimentoProfessione(new StringType(Costanti.COD_UIC_NAZIONE_ITALIA));
				model.getInfoPersonali().setProvinciaSvolgimentoProfessione(new StringType(model.getInfoPersonali().getNazOProvSvolgProf().toString()));
			}
		}
		//////////////////////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////////////

		// Impostazioni valori di default in caso di campi null su DB
		if(model.getCittadinanza().isNull() && !model.getCallingAppl().toString().equals(Costanti.CALLING_APPL_MHD))
			model.getCittadinanza().setStringValue(Costanti.COD_UIC_NAZIONE_ITALIA);
		if(model.getInfoPersonali().getNazioneSvolgimentoProfessione().isNull())
			model.getInfoPersonali().getNazioneSvolgimentoProfessione().setStringValue(Costanti.COD_UIC_NAZIONE_ITALIA);
		
		// Per le società e i datori di lavoro il campo cognome (che è la ragione sociale) è splittato su seconda intestazione
		componiRagioneSociale(model);
		
		if(model.getRecapiti().getTelefonoCellulare().getPrefissoInternazionale().isNull())
			model.getRecapiti().getTelefonoCellulare().setPrefissoInternazionale(new StringType(Costanti.PREFIX_INTERNAZIONALE_ITALIA));
		
		if(model.getRecapiti().getTelefonoFax().getPrefissoInternazionale().isNull())
			model.getRecapiti().getTelefonoFax().setPrefissoInternazionale(new StringType(Costanti.PREFIX_INTERNAZIONALE_ITALIA));
		
		// Impostazione dei campi non persistenti e derivati
		//
		// -DomicilioDiversoDaResidenza
		model.setDomicilioDiversoDaResidenza(new StringType("S"));
		if( model.getDomicilio().getIndirizzo().getPresso().isNull() &&
			model.getDomicilio().getIndirizzo().isEmpty())
			model.setDomicilioDiversoDaResidenza(new StringType("N"));
		
		// -HaCarichePubbliche <-> CaricaPubblicaRicoperta
		if(!model.getAdempimentiNormativi().getCaricaPubblicaRicoperta().isNull()) {
			if(model.getAdempimentiNormativi().getCaricaPubblicaRicoperta().equals(Costanti.CODICE_CARICA_PUBBLICA_RICOPERTA_NO)) {
				model.getAdempimentiNormativi().setHaCarichePubbliche(new StringType("N"));
				model.getAdempimentiNormativi().setCaricaPubblicaRicoperta(new StringType());
			}else {
				model.getAdempimentiNormativi().setHaCarichePubbliche(new StringType("S"));
			}
		}
		
		// -HaLegamiAffariDiversiDaAttivitaPrincipale <-> TipologiaLegameAffariDiversoDaAttivitaPrincipale
		if(!model.getAdempimentiNormativi().getTipologiaLegameAffariDiversoDaAttivitaPrincipale().isNull()) {
			if(model.getAdempimentiNormativi().getTipologiaLegameAffariDiversoDaAttivitaPrincipale().equals(Costanti.CODICE_TIPOLOGIA_LEGAME_AFFARI_DIVERSO_ATTIVITA_PRINCIPALE_NO)) {
				model.getAdempimentiNormativi().setHaLegamiAffariDiversiDaAttivitaPrincipale(new StringType("N"));
				model.getAdempimentiNormativi().setTipologiaLegameAffariDiversoDaAttivitaPrincipale(new StringType());
			}else{
				model.getAdempimentiNormativi().setHaLegamiAffariDiversiDaAttivitaPrincipale(new StringType("S"));
			}
		}
		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void loadResidenza(DAOObject dao, ClienteModel model) throws Exception{

		try{

			model.getDatiApplicativi().setTipoElemento(new StringType(Costanti.COD_INDIRIZZO_RESIDENZA));
			DAOQueryResultModel result = dao.executeQueryAccess("loadIndirizzi",model);
			if(result.getResult().size() > 0){
				IndirizzoModel indResidenza = (IndirizzoModel)result.getResult().get(0);
				indResidenza.setExistOnDB(true);
				loadComune(dao,indResidenza);
				indResidenza.getCodDescFields().remove("tipoIndirizzo");
				model.getResidenza().setIndirizzo(indResidenza);
			}
			
		}catch(DAOException daoe){
			String errorMsg = "Eccezione DAO nel leggere la residenza dell'anagrafica ["+model.getCodPotenziale()+"]: "+daoe;
			Exception e = new Exception(errorMsg);
			LOG.error(e);
			throw e;
		}catch(Exception e){
			String errorMsg = "Eccezione nel leggere la residenza dell'anagrafica ["+model.getCodPotenziale()+"]: "+e;
			e = new Exception(errorMsg);
			LOG.error(e);
			throw e;
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void loadDomicilio(DAOObject dao, ClienteModel model) throws Exception{

		try{

			model.getDatiApplicativi().setTipoElemento(new StringType(Costanti.COD_INDIRIZZO_DOMICILIO));
			DAOQueryResultModel result = dao.executeQueryAccess("loadIndirizzi",model);
			if(result.getResult().size() > 0){
				IndirizzoModel indDomicilio = (IndirizzoModel)result.getResult().get(0);
				indDomicilio.setExistOnDB(true);
				loadComune(dao,indDomicilio);
				indDomicilio.getCodDescFields().remove("tipoIndirizzo");
				model.getDomicilio().setIndirizzo(indDomicilio);
			}

		}catch(DAOException daoe){
			String errorMsg = "Eccezione DAO nel leggere il domicilio dell'anagrafica ["+model.getCodPotenziale()+"]: "+daoe;
			Exception e = new Exception(errorMsg);
			LOG.error(e);
			throw e;
		}catch(Exception e){
			String errorMsg = "Eccezione nel leggere il domicilio dell'anagrafica ["+model.getCodPotenziale()+"]: "+e;
			e = new Exception(errorMsg);
			LOG.error(e);
			throw e;
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void loadRecapiti(DAOObject dao, ClienteModel model) throws Exception{

		try{

			model.getDatiApplicativi().setTipoElemento(new StringType(Costanti.COD_TELEFONO_RESIDENZA));
			DAOQueryResultModel result = dao.executeQueryAccess("loadTelefoni",model);
			if(result.getResult().size() > 0){
				TelefonoModel telefono = (TelefonoModel)result.getResult().get(0);
				telefono.setExistOnDB(true);
				inizializzaTelefono(model,telefono);
				telefono.getCodDescFields().remove("tipoTelefono");
				model.getRecapiti().setTelefonoResidenza(telefono);
			}
			
			model.getDatiApplicativi().setTipoElemento(new StringType(Costanti.COD_TELEFONO_DOMICILIO));
			result = dao.executeQueryAccess("loadTelefoni",model);
			if(result.getResult().size() > 0){
				TelefonoModel telefono = (TelefonoModel)result.getResult().get(0);
				telefono.setExistOnDB(true);
				inizializzaTelefono(model,telefono);
				telefono.getCodDescFields().remove("tipoTelefono");
				model.getRecapiti().setTelefonoDomicilio(telefono);
			}
			
			model.getDatiApplicativi().setTipoElemento(new StringType(Costanti.COD_TELEFONO_FAX));
			result = dao.executeQueryAccess("loadTelefoni",model);
			if(result.getResult().size() > 0){
				TelefonoModel telefono = (TelefonoModel)result.getResult().get(0);
				telefono.setExistOnDB(true);
				inizializzaTelefono(model,telefono);
				telefono.getCodDescFields().remove("tipoTelefono");
				model.getRecapiti().setTelefonoFax(telefono);
			}
			
			model.getDatiApplicativi().setTipoElemento(new StringType(Costanti.COD_TELEFONO_CELLULARE));
			result = dao.executeQueryAccess("loadTelefoni",model);
			if(result.getResult().size() > 0){
				TelefonoModel telefono = (TelefonoModel)result.getResult().get(0);
				telefono.setExistOnDB(true);
				inizializzaTelefono(model,telefono);
				telefono.getCodDescFields().remove("tipoTelefono");
				model.getRecapiti().setTelefonoCellulare(telefono);
			}
			
		}catch(DAOException daoe){
			String errorMsg = "Eccezione DAO nel leggere i recapiti dell'anagrafica ["+model.getCodPotenziale()+"]: "+daoe;
			Exception e = new Exception(errorMsg);
			LOG.error(e);
			throw e;
		}catch(Exception e){
			String errorMsg = "Eccezione nel leggere i recapiti dell'anagrafica ["+model.getCodPotenziale()+"]: "+e;
			e = new Exception(errorMsg);
			LOG.error(e);
			throw e;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void loadVariazioni(DAOObject dao, ClienteModel model){

		try{

			DAOQueryResultModel result = dao.executeQueryAccess("loadVariazioni",model);
			model.getVariazioni().setElencoVariazioni(result.getResult());
			for(int i=0;i<model.getVariazioni().getElencoVariazioni().size();i++){
				ClienteModel variazione = (ClienteModel)model.getVariazioni().getElencoVariazioni().get(i);
				variazione.setAgente(model.getAgente());
				variazione.getInfoPersonali().setCodAgente(model.getCodAgente());
			}
			
			
		}catch(DAOException daoe){
			String errorMsg = "Eccezione DAO nel leggere le variazioni dell'anagrafica ["+model.getCodPotenziale()+"]: "+daoe;
			Exception e = new Exception(errorMsg);
			LOG.error(e);
			return;
		}catch(Exception e){
			String errorMsg = "Eccezione nel leggere le variazioni dell'anagrafica ["+model.getCodPotenziale()+"]: "+e;
			e = new Exception(errorMsg);
			LOG.error(e);
			return;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void loadComune(DAOObject dao, ComuneModel model) throws Exception{
		
		if(model.getCodNazione().isNull())
			model.getCodNazione().setStringValue(Costanti.COD_NAZIONE_ITALIA);

		if(!model.getCodNazione().equals(Costanti.COD_NAZIONE_ITALIA)){
			model.setComuneEstero(model.getComune());
			model.setComune(new StringType());
			model.setCap(new StringType());
			model.setProvincia(new StringType("EE"));
			return;
		}
		
		if(model.getCodComune().isNull() &&
		   model.getComune().isNull()    &&
		   model.getCap().isNull()       &&
		   model.getProvincia().isNull())
			return;
		
		StringType savCodComune = model.getCodComune();
		StringType savComune = model.getComune();
		StringType savCap = model.getCap();
		StringType savProvincia = model.getProvincia();
		
		try{

			String accessName = "loadComune";
			if(model.getIsIscrittoAlCatasto().booleanValue())
				accessName = "loadComuneIscrittoAlCatasto";
			DAOQueryResultModel qRes = dao.executeQueryAccess(accessName,model);
			if(qRes.getResult().size() == 0){
				model.setCodComune(savCodComune);
				model.setComune(savComune);
				model.setCap(savCap);
				model.setProvincia(savProvincia);				
			}
			
		}catch(DAOException daoe){
			String errorMsg = "Eccezione DAO nel leggere il comune ["+model.getCodComune()+"]: "+daoe;
			Exception e = new Exception(errorMsg);
			LOG.error(e);
			throw e;
		}catch(Exception e){
			String errorMsg = "Eccezione nel leggere il comune ["+model.getCodComune()+"]: "+e;
			e = new Exception(errorMsg);
			LOG.error(e);
			throw e;
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void loadUniversita(DAOObject dao, UniversitaModel model) throws Exception{
		if(model.getCodUniversita().isNull())
			return;
		try{
			dao.executeQueryAccess("loadUniversita",model);
		}catch(DAOException daoe){
			LOG.error(daoe);
			throw new Exception(daoe.toString());
		}catch(Exception e){
			LOG.error(e);
			throw e;
		}		
	}
	
	/********************************************************************************************************/
	/********************************************************************************************************/
	public static void loadAgente(ClientSessionContext csc, DAOObject dao, ClienteModel model) throws Exception{

		try{
			boolean esisteAgente = true;
			try{
				boolean isFreezed = model.getDatiApplicativi().isVariazione() || (model.getIsPotenziale().booleanValue() && model.getIsPropostaInviataInSede().booleanValue());
				if(isFreezed && !model.getCodAgenteUltimaModifica().isNull())
					model.getAgente().setCodAgente(model.getCodAgenteUltimaModifica());
				else
					model.getAgente().setCodAgente(new StringType(Tools.fillSx(csc.getCurrentLinkedUserCode(),'0',10)));
				DAOQueryResultModel qRes = dao.executeQueryAccess("loadAgente",model.getAgente());
				if(qRes.getResult().size() == 0)
					esisteAgente = false;
				else
					model.setServerReplica(model.getAgente().getServerReplica());				
			}catch(DAOException daoe){
				esisteAgente = false;
			}
			
			if(!esisteAgente)
				model.getAgente().setCicloVitaAgente(new StringType(CicliVitaAgente.NON_TROVATO));
			else if(model.getIsPotenziale().booleanValue())
				CogestioneDataManager.initCogestioneData(csc, model);
			
			// Imposto il server replica
			model.getAgente().setServerReplica(new StringType());				
			try{							
				dao.executeQueryAccess("loadServerReplicaAgente",model.getAgente());
			}catch(DAOException daoe){
				String errorMsg = "Eccezione DAO nel recuperare il server replica per l'agente ["+model.getAgente()+"]: "+daoe;
				Exception e = new Exception(errorMsg);
				LOG.error(e);
				throw e;
			}

			model.setServerReplica(model.getAgente().getServerReplica());				
			model.getAgente().setAreaAgente(new StringType(Tools.unFillSx(model.getAgente().getAreaAgente().toString(),'0')));
			
			model.getInfoPersonali().setCodAgente(model.getCodAgente());			
			
		}catch(DAOException daoe){
			String errorMsg = " Eccezione DAO nel leggere l'agente ["+model.getCodAgente()+"]: "+daoe;
			throw new Exception(errorMsg);
		}catch(Exception e){
			String errorMsg = " Eccezione nel leggere l'agente ["+model.getCodAgente()+"]: "+e;
			e = new Exception(errorMsg);
			throw e;
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void inizializzaTelefono(ClienteModel model, TelefonoModel telefono){

		String t = telefono.getReperibileOraDaDB().toHourString();
		if(!t.equals(""))
			t = t.substring(0,5);
		telefono.setReperibileOraDa(new StringType(t));
		
		t = telefono.getReperibileOraADB().toHourString();
		if(!t.equals(""))
			t = t.substring(0,5);
		telefono.setReperibileOraA(new StringType(t));
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void splittaRagioneSociale(ClienteModel model) {
		// Per i datori di lavoro il campo cognome (che è la ragione sociale) è splittato su seconda intestazione
		if(model.getIsDatoreDiLavoro().booleanValue() || model.getIsPersonaGiuridica().booleanValue()){
			if(model.getCognome().toString().length() > 40){
				String ragSociale = model.getCognome().toString();
				String par1 = ragSociale.substring(0,40);
				String par2 = ragSociale.substring(40);
				model.setCognome(new StringType(par1));
				model.setSecondaIntestazione(new StringType(par2));
			}else{
				model.setSecondaIntestazione(new StringType());
			}
		}
	}	

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void componiRagioneSociale(ClienteModel model) {
		// Per i datori di lavoro ripristino il cognome se spezzato
		if(model.getIsPersonaGiuridica().booleanValue() || model.getIsDatoreDiLavoro().booleanValue()){
			model.setCognome(new StringType(model.getCognome().toString()+model.getSecondaIntestazione().toString()));
			model.setSecondaIntestazione(new StringType());
		}
	}	
}
