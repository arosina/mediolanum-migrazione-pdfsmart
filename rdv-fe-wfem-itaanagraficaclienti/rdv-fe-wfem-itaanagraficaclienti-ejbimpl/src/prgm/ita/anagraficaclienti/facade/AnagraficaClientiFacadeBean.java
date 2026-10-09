package prgm.ita.anagraficaclienti.facade;

import java.util.Calendar;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.atosorigin.wfem.backend.FacadeObject;
import com.atosorigin.wfem.coddesc.CodDescDataList;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQASResultModel;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.dbjavaclasses.DBJavaClassLoader;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.QASCallData;
import com.atosorigin.wfem.util.Tools;
import com.atosorigin.wfem.util.VersionPrinter;

import prgm.ita.anagraficaclienti.agenticlienti.RicercaGlobaleClientiModel;
import prgm.ita.anagraficaclienti.cogestione.CogestioneDataManager;
import prgm.ita.anagraficaclienti.model.AgenteModel;
import prgm.ita.anagraficaclienti.model.ClienteKeyModel;
import prgm.ita.anagraficaclienti.model.ClienteModel;
import prgm.ita.anagraficaclienti.model.DocumentoModel;
import prgm.ita.anagraficaclienti.model.FotografiaModel;
import prgm.ita.anagraficaclienti.model.IndirizzoModel;
import prgm.ita.anagraficaclienti.model.ParametriApplicazioneAnagrafica;
import prgm.ita.anagraficaclienti.model.TelefonoModel;
import prgm.ita.anagraficaclienti.model.VariazioneKeyModel;

/***********************************************************************************************/
/***********************************************************************************************/
@Stateless(name = "AnagraficaClientiFacade", mappedName = "AnagraficaClientiFacade")
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class AnagraficaClientiFacadeBean extends FacadeObject implements AnagraficaClientiFacade{
	
	{VersionPrinter.getInstance().print("ItaAnagraficaClienti",this);}
	
	private static final String thisClassName = "AnagraficaClientiFacadeBean";
    private static com.atosorigin.wfem.util.Logger LOG = com.atosorigin.wfem.util.Logger.getInstance();	

    private static final String S_CODATECO = "codAteco";
    
	/***********************************************************************************************/
	/***********************************************************************************************/
	public RicercaGlobaleClientiModel ricercaGlobaleClienti(ClientSessionContext csc, 
														  RicercaGlobaleClientiModel ricercaAgentiClientiModel) throws EJBException {
		
		DAOObject dao = null;
		try{
			 
			dao = new DAOObject(csc,getNomeDAORicerche(csc));
			dao.openConnection();
			
			if(ricercaAgentiClientiModel.getParams().getCodRete().isNull())
				ricercaAgentiClientiModel.getParams().setCodRete(new StringType(csc.getChannelCode().toUpperCase()));
			
			StringType codMediolanum = ricercaAgentiClientiModel.getParams().getCodMediolanum();
			if(!codMediolanum.isNull())
				ricercaAgentiClientiModel.getParams().setCodMediolanum(new StringType(Tools.fillSx(codMediolanum.toString(),'0',11)));
			
			DAOQueryResultModel queryResult = dao.executeQueryAccess("ricercaGlobaleClienti",ricercaAgentiClientiModel);
			ricercaAgentiClientiModel.setElenco(queryResult.getResult());
			
			ricercaAgentiClientiModel.getParams().setCodMediolanum(codMediolanum);
			
			return ricercaAgentiClientiModel;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+ "Eccezione DAO nel leggere l'elenco globale dei clienti: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nel leggere l'elenco globale dei clienti: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}finally{
			if(dao != null) dao.closeConnection();
		}
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public RicercaGlobaleClientiModel elencoAgentiCliente(ClientSessionContext csc, 
														   RicercaGlobaleClientiModel ricercaGlobaleClientiModel) throws EJBException {
		
		DAOObject dao = null;
		try{
			 
			dao = new DAOObject(csc,getNomeDAORicerche(csc));
			dao.openConnection();
			
			IntegerType idxClienteSelezionato = ricercaGlobaleClientiModel.getIdxClienteSelezionato();
			ClienteModel clienteSelezionato = (ClienteModel)ricercaGlobaleClientiModel.getElenco().get(idxClienteSelezionato.intValue());
			
			DAOQueryResultModel queryResult = dao.executeQueryAccess("elencoAgentiCliente",clienteSelezionato);
			ricercaGlobaleClientiModel.setElencoAgentiCliente(queryResult.getResult());
			
			return ricercaGlobaleClientiModel;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+ "Eccezione DAO nel leggere l'elenco degli agenti del cliente: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nel leggere l'elenco degli agenti del cliente: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}finally{
			if(dao != null) dao.closeConnection();
		}
		
	}

	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public ClienteModel nuovoCliente(ClientSessionContext csc, ParametriApplicazioneAnagrafica params) throws EJBException{
		
		DAOObject dao = null;
		try{
			
			dao = new DAOObject(csc,getNomeDAO(csc));
			dao.openConnection();						

			ClienteModel model = new ClienteModel();
			model.setIsDatoreDiLavoro(params.getIsDatoreDiLavoro());
			model.setStato(new StringType(StatiAnagraficaCliente.CLIENTE_POTENZIALE));
			model.setStatoProposta(new StringType(StatiPropostaAnagrafica.BOZZA));
			model.setCodRete(new StringType(csc.getChannelCode()));			
			model.setCodAgente(new StringType(csc.getCurrentLinkedUserCode()));
			
			// Per ora i datori di lavoro sono solo di persone giuridiche
			if(model.getIsDatoreDiLavoro().booleanValue()){
				model.setNaturaGiuridica(new StringType(Costanti.NATURA_GIURIDICA_DATORE_DI_LAVORO));
				model.setTipoPersona(new StringType(Costanti.TIPO_PERSONA_GIURIDICA));
				model.setSesso(new StringType(Costanti.SESSO_SOCIETA));
				model.getInfoPersonali().setCodSottogruppoAttivita(new StringType(Costanti.CODICE_SOTTOGRUPPO_ATTIVITA_DATORI_DI_LAVORO));
				model.getInfoPersonali().setCodGruppoAttivita(new StringType(Costanti.CODICE_GRUPPO_ATTIVITA_DATORI_DI_LAVORO));
			}

			LoaderAnagrafica.loadAgente(csc,dao,model);			
			
			model.getInfoPersonali().addCodDescField("codOrigine","ORIGINI"+params.getCantiere());
			if(!params.getCantiere().isNull())
				model.getInfoPersonali().setCodOrigine(params.getCantiere());

			// In M4U la persona è fisica e la professione è preimpostata a Studente
			if(model.getInfoPersonali().getCodOrigine().equals(Costanti.CODICE_ORIGINE_MFORYOU)){
				model.setTipoPersona(new StringType(Costanti.TIPO_PERSONA_FISICA));
				model.getInfoPersonali().setCodProfessione(new StringType(Costanti.CODICE_PROFESSIONE_STUDENTE));
			}

			CodDescDataList listSettoriEconomici = InfoLoader.leggiCodDescListSettoriEconomici(csc, model, true);
			model.getInfoPersonali().addCodDescField("codSettoreEconomico",listSettoriEconomici);

			CodDescDataList listCodiciSae = InfoLoader.leggiCodDescListSae(csc, model, false);
			model.getInfoPersonali().addCodDescField("codSottogruppoAttivita",listCodiciSae);

			CodDescDataList listCodiciAteco = InfoLoader.leggiCodDescListAteco(csc, model, false);
			model.getInfoPersonali().addCodDescField(S_CODATECO,listCodiciAteco);

			// Imposto il flag che indica se il cliente è segnalato. Verrà ripulito all'invio in sede
			model.getDatiApplicativi().setFlagClienteSegnalato(params.getFlagClienteSegnalato());
			
			// Nei censimenti preimpostiamo la prima residenza fiscale a ITALIA
			model.getResidenza().setCodNazioneResidenzaFiscale1(new StringType(Costanti.COD_UIC_NAZIONE_ITALIA));
			
			dao.fillCodDesc(model);
			return model;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nel inizializzare una nuova anagrafica: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO nel inizializzare una nuova anagrafica: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}finally{
			if(dao != null) dao.closeConnection();
		}
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public ClienteModel nuovaDitta(ClientSessionContext csc, ParametriApplicazioneAnagrafica params) throws EJBException{
		
		DAOObject dao = null;
		try{
			
			dao = new DAOObject(csc,getNomeDAO(csc));
			dao.openConnection();						

			ClienteModel model = new ClienteModel();
			model.getDatiApplicativi().setCensimentoDitta(true);
			model.setTipoPersona(new StringType(Costanti.TIPO_PERSONA_FISICA));
			model.setStato(new StringType(StatiAnagraficaCliente.CLIENTE_POTENZIALE));
			model.setStatoProposta(new StringType(StatiPropostaAnagrafica.BOZZA));
			model.setCodRete(new StringType(csc.getChannelCode()));			
			model.setCodAgente(new StringType(csc.getCurrentLinkedUserCode()));
			
			LoaderAnagrafica.loadAgente(csc,dao,model);			

			model.getInfoPersonali().addCodDescField("codOrigine","ORIGINIDITTA"+params.getCantiere());
			if(!params.getCantiere().isNull())
				model.getInfoPersonali().setCodOrigine(params.getCantiere());
			
			CodDescDataList listSettoriEconomici = InfoLoader.leggiCodDescListSettoriEconomici(csc, model, true);
			model.getInfoPersonali().addCodDescField("codSettoreEconomico",listSettoriEconomici);
			
			CodDescDataList listCodiciSae = InfoLoader.leggiCodDescListSae(csc, model, true);
			model.getInfoPersonali().addCodDescField("codSottogruppoAttivita",listCodiciSae);
			
			// Nei censimenti preimpostiamo la prima residenza fiscale a ITALIA
			model.getResidenza().setCodNazioneResidenzaFiscale1(new StringType(Costanti.COD_UIC_NAZIONE_ITALIA));
			
			dao.fillCodDesc(model);
			return model;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nel inizializzare una nuova anagrafica: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO nel inizializzare una nuova anagrafica: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}finally{
			if(dao != null) dao.closeConnection();
		}
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public ClienteModel leggiClienteLight(ClientSessionContext csc, ClienteKeyModel clienteKey) throws EJBException{
		try{
			clienteKey = (ClienteKeyModel)Tools.cloneObject(clienteKey);
			return innerLeggiCliente(csc,clienteKey,true);
		}catch(Exception e){
			throw new EJBException(e.toString());
		}
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public ClienteModel leggiCliente(ClientSessionContext csc, ClienteKeyModel clienteKey) throws EJBException{
		try{
			clienteKey = (ClienteKeyModel)Tools.cloneObject(clienteKey);
			return innerLeggiCliente(csc,clienteKey,false);			
		}catch(Exception e){
			throw new EJBException(e.toString());
		}
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public ClienteModel innerLeggiCliente(ClientSessionContext csc, ClienteKeyModel clienteKey, boolean isLight) throws Exception{
		
		DAOObject dao = null;
		try{
			
			CogestioneDataManager.initCodAgenteTitolare(csc, clienteKey);
			
			dao = new DAOObject(csc,getNomeDAO(csc));
			dao.openConnection();						

			ClienteModel model = trovaCliente(dao,clienteKey);
			if(model == null){ // Non trovato
				model = new ClienteModel();
				model.setStato(new StringType(StatiPropostaAnagrafica.NON_TROVATA));
				model.setStatoProposta(new StringType(StatiPropostaAnagrafica.INVIATA_IN_SEDE));
				if(!csc.getCurrentLinkedUserCode().equals(clienteKey.getCodAgente().toString()))
					model.addCommandError("Attenzione ! Il cliente non &egrave; in cogestione");
				else
					model.addCommandError("Attenzione ! Il cliente non &egrave; stato trovato");
				return model;
			}
			
			model.setCallingAppl(new StringType(clienteKey.getCallingAppl().toString()));
			LoaderAnagrafica.loadCliente(csc,dao,model,true,true,isLight);
			impostaCancellabile(csc,model);
			
			CodDescDataList listSettoriEconomici = InfoLoader.leggiCodDescListSettoriEconomici(csc, model, false);
			model.getInfoPersonali().addCodDescField("codSettoreEconomico",listSettoriEconomici);

			CodDescDataList listCodiciSae = InfoLoader.leggiCodDescListSae(csc, model, model.getIsDitta().booleanValue());
			model.getInfoPersonali().addCodDescField("codSottogruppoAttivita",listCodiciSae);

			if(!model.getIsDitta().booleanValue()){
				CodDescDataList listCodiciAteco = InfoLoader.leggiCodDescListAteco(csc, model, false);
				model.getInfoPersonali().addCodDescField(S_CODATECO,listCodiciAteco);
			}
			
			String coddescOrigini = "ORIGINI";
			if(model.getIsDitta().booleanValue())
				coddescOrigini = "ORIGINIDITTA";
			model.getInfoPersonali().addCodDescField("codOrigine",coddescOrigini);
			
			if(model.getInfoPersonali().getCodOrigine().equals(Costanti.CODICE_ORIGINE_MEMBER_GET_MEMBER))
				model.getInfoPersonali().addCodDescField("codOrigine",coddescOrigini+Costanti.CODICE_ORIGINE_MEMBER_GET_MEMBER);
			if(model.getInfoPersonali().getCodOrigine().equals(Costanti.CODICE_ORIGINE_MFORYOU))
				model.getInfoPersonali().addCodDescField("codOrigine",coddescOrigini+Costanti.CODICE_ORIGINE_MFORYOU);
			if(model.getInfoPersonali().getCodOrigine().equals(Costanti.CODICE_ORIGINE_IMF))
				model.getInfoPersonali().addCodDescField("codOrigine",coddescOrigini+Costanti.CODICE_ORIGINE_IMF);
				
			dao.fillCodDesc(model);
			
			if(model.getIsDitta().booleanValue()){
				if(model.getCodAgente().isNull() || model.getCodFiscale().isNull()){
					model.setClienteTitolare(null);
				}else{
					ClienteKeyModel titolareKey = new ClienteKeyModel();
					titolareKey.setCodAgente(model.getCodAgente());
					titolareKey.setCodFiscale(model.getCodFiscale());
					ClienteModel titolare = leggiClienteTitolare(csc,titolareKey);
					if(titolare.getStato().equals(StatiPropostaAnagrafica.NON_TROVATA))
						model.setClienteTitolare(null);
					else
						model.setClienteTitolare(titolare);
				}
			}

			// Carico il segnalatore in caso di ogirine 
			loadSegnalatoreOrigine(csc, dao, model);
			
			// Con FATCA: Se l'anagrafica in variazione ha già impostato la motivazione pep ma non corrisponde a nessun valore nella tendina
			// ne lasciamo inalterata la valorizzazione e disabilitiamo la tendina (in pagina, se "motivazionePepNonCongruente = true" allora creiamo una tendina "finta")
			if(model.getIsEffettivo().booleanValue()){
				if(!model.getResidenza().getFlagPep().isNull() && !model.getResidenza().getFlagPep().equals("N")){
					BooleanType existMotivazionePep = (BooleanType)dao.executeQueryAccess("existMotivazionePep", model.getResidenza()).getSingleResult();
					if(existMotivazionePep != null && !existMotivazionePep.booleanValue()) {
						model.getResidenza().getCodDescFields().remove("motivazionePep");
						model.getDatiApplicativi().setMotivazionePepNonCongruente(true);
					}
				}
			}
			
			recuperaDifferenzialiCliente(csc,model);

			model.getInfoPersonali().initCombinazioneProvenienzaPatrimonioChecks();
			
			// Valorizzo i campi in input al processo
			model.setCodAgeImpersonato(clienteKey.getCodAgeImpersonato());
			model.setModalitaDiSottoscrizione(clienteKey.getModalitaDiSottoscrizione());
			return model;
			
		}catch(Exception e){
			String errorMsg = thisClassName+" Eccezione nel leggere l'anagrafica: "+e;
			e = new Exception(errorMsg);
			LOG.error(e);
			throw e;
		}catch(DAOException daoe){
			String errorMsg = thisClassName+" Eccezione DAO nel leggere l'anagrafica: "+daoe;
			Exception e = new Exception(errorMsg);
			LOG.error(e);
			throw e;
		}finally{
			if(dao != null) dao.closeConnection();
		}
	}

	/*****************************************************************************************************/
	/*****************************************************************************************************/
	private void loadSegnalatoreOrigineDittaMGM(DAOObject dao, ClienteModel model) throws DAOException, AnagraficaClientiException{
		try {
			ClienteKeyModel segnalatoreKey = new ClienteKeyModel();
			segnalatoreKey.setCodAgente(model.getCodAgente());
			// Vado a prendere il supervisore perchè il segnalatore potrebbe essere dell'SPV
			dao.executeCallableAccess("loadSupervisoreAgente",segnalatoreKey);
			String codCliente = model.getInfoPersonali().getCodOrigineECodCliente().toString();
			if(codCliente.length() > 4){
				codCliente = codCliente.substring(4);
				if(codCliente.length() == 11) // Cliente effettivo
					segnalatoreKey.setCodMediolanum(new StringType(codCliente));
				else
					segnalatoreKey.setCodPotenziale(new StringType(codCliente));
				ClienteModel segnalatore = trovaCliente(dao,segnalatoreKey);
				if(segnalatore != null)
					model.getInfoPersonali().setSegnalatore(segnalatore);
			}
		}catch(Exception e) {
			throw new AnagraficaClientiException(e.toString());
		}
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	private void loadSegnalatoreOrigineMGM(DAOObject dao, ClienteModel model) throws DAOException, Exception{
		try {
			ClienteKeyModel segnalatoreKey = new ClienteKeyModel();
			String codOrigineECliente = model.getInfoPersonali().getCodOrigineECodCliente().toString();
			if(codOrigineECliente.length() > 4){
				codOrigineECliente = codOrigineECliente.substring(4);
				if(codOrigineECliente.length() == 11) // Cliente effettivo
					segnalatoreKey.setCodMediolanum(new StringType(codOrigineECliente));
				else
					segnalatoreKey.setCodPotenziale(new StringType(codOrigineECliente));
				ClienteModel segnalatore = trovaClienteSenzaAgente(dao,segnalatoreKey);
				if(segnalatore != null)
					model.getInfoPersonali().setSegnalatore(segnalatore);
			}
		}catch(Exception e) {
			throw new AnagraficaClientiException(e.toString());
		}
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	private void loadSegnalatoreOrigineIMF(DAOObject dao, ClienteModel model) throws DAOException, Exception{
		try {
			ClienteKeyModel segnalatoreKey = new ClienteKeyModel();
			String codCliente = model.getInfoPersonali().getCodOrigineECodCliente().toString();
			if(codCliente.length() > 4){
				codCliente = codCliente.substring(4);
				if(codCliente.length() == 11) // Cliente effettivo
					segnalatoreKey.setCodMediolanum(new StringType(codCliente));
				else
					segnalatoreKey.setCodPotenziale(new StringType(codCliente));
				ClienteModel segnalatore = trovaClienteSenzaAgente(dao,segnalatoreKey);
				if(segnalatore != null)
					model.getInfoPersonali().setSegnalatore(segnalatore);
			}
		}catch(Exception e) {
			throw new AnagraficaClientiException(e.toString());
		}
	}

	/*****************************************************************************************************/
	/*****************************************************************************************************/
	private void loadSegnalatoreOrigineGBL(ClientSessionContext csc, ClienteModel model){
		String codAgente = model.getInfoPersonali().getCodOrigineECodCliente().toString();
		if(codAgente.length() > 4)
			codAgente = codAgente.substring(4);
		AgenteModel age = leggiAltroAgente(csc,new StringType(codAgente));
		ClienteModel agenteSegnalatore = new ClienteModel();
		agenteSegnalatore.setCodMediolanum(age.getCodAgente());
		agenteSegnalatore.setCodAgente(age.getCodAgente());
		agenteSegnalatore.setCognome(age.getCognomeAgente());
		agenteSegnalatore.setNome(age.getNomeAgente());
		model.getInfoPersonali().setSegnalatore(agenteSegnalatore);
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	private void loadSegnalatoreOrigine(ClientSessionContext csc, DAOObject dao, ClienteModel model) throws DAOException{
		
		try {
			
			// La gestione delle campagne (MGM, M4U...) prevede che nel campo GENERAT_REFERRAL ci sia concatenato 
			// il codice campagna + "-" + codice cliente (Inforete / Mediolanum)
			if((model.getIsDitta().booleanValue() && model.getInfoPersonali().getCodOrigine().equals(Costanti.CODICE_ORIGINE_MEMBER_GET_MEMBER)) || 
				model.getInfoPersonali().getCodOrigine().equals(Costanti.CODICE_ORIGINE_MFORYOU)){
				loadSegnalatoreOrigineDittaMGM(dao, model);
			}
			
			// Nel campo GENERAL_REFERRAL si concatena 
			// il codice campagna + "-" + codice cliente (Inforete / Mediolanum oppure codicePotenziale)
			if(!model.getIsDitta().booleanValue() && model.getInfoPersonali().getCodOrigine().equals(Costanti.CODICE_ORIGINE_MEMBER_GET_MEMBER)){
				loadSegnalatoreOrigineMGM(dao, model);
			}
			
			// La gestione Iniziativa IMF prevede che nel campo GENERAT_REFERRAL ci sia concatenato 
			// il codice campagna + "-" + codice cliente (Inforete / Mediolanum oppure codicePotenziale)
			if(model.getInfoPersonali().getCodOrigine().equals(Costanti.CODICE_ORIGINE_IMF)){
				loadSegnalatoreOrigineIMF(dao, model);
			}
			
			// La gestione dell'origine GBS (Global specialist) prevede che nel campo GENERAT_REFERRAL ci sia concatenato 
			// il codice origine GBS + "-" + codice agente global specialist
			if(model.getInfoPersonali().getCodOrigine().equals(Costanti.CODICE_ORIGINE_GLOBAL_SPECIALIST)){
				loadSegnalatoreOrigineGBL(csc, model);
			}
		
		}catch(Exception e){/* do nothing */ }
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public ClienteModel leggiClienteTitolare(ClientSessionContext csc, ClienteKeyModel dittaKey) throws EJBException{
		
		DAOObject dao = null;
		try{
			
			dittaKey = (ClienteKeyModel)Tools.cloneObject(dittaKey);
			CogestioneDataManager.initCodAgenteTitolare(csc, dittaKey);
			
			dao = new DAOObject(csc,getNomeDAO(csc));
			dao.openConnection();						

			ClienteModel model = trovaClienteTitolare(dao,dittaKey);
			if(model != null){

				LoaderAnagrafica.loadCliente(csc,dao,model,true,false,false);
				dao.fillCodDesc(model);
				return model;
				
			}else{
				
				model = new ClienteModel();
				model.setStato(new StringType(StatiPropostaAnagrafica.NON_TROVATA));
				model.setStatoProposta(new StringType(StatiPropostaAnagrafica.INVIATA_IN_SEDE));
				model.addCommandError("titolareNonTrovato");
				
			}
			return model;
			
		}catch(Exception e){
			String errorMsg = thisClassName+" Eccezione nel leggere l'anagrafica del titolare: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(DAOException daoe){
			String errorMsg = thisClassName+" Eccezione DAO nel leggere l'anagrafica del titolare: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}finally{
			if(dao != null) dao.closeConnection();
		}
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public ClienteModel salvaBozzaCliente(ClientSessionContext csc, 
									  	   ClienteModel model) throws EJBException{
		try{
			
			model.resetCommandErrors(); model.resetCommandWarnings(); model.resetCommandMessages();
			Tools.resetTypesWarningAndErrorsAndMessages(model);

			eseguiControlliSalvataggio(csc,model);
			
			if(Tools.containsTypeErrors(model)){
				model.addCommandError("errori");
				return model;
			}
			
			impostaEnteRilasciante(csc,model);
			impostaCodiceFiscaleForzato(csc,model);
			
			WriterAnagraficaManager writerManager = (WriterAnagraficaManager)ROF.getManager(csc,WriterAnagraficaManager.class);
			model = writerManager.salva(csc,model);
			if(model.getConcurrencyViolationFoundedWidth() != null)
				return model;

			if(Tools.containsTypeWarnings(model))
				model.addCommandError("errori");
				
			model.addCommandMessage("salvato");
			return model;
			
		}catch(Exception e){
			String errorMsg = thisClassName+" Eccezione nel salvare l'anagrafica: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
	  	}
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public ClienteKeyModel cancellaCliente(ClientSessionContext csc, ClienteKeyModel model) throws EJBException{
		try{
			
			model.resetCommandErrors(); model.resetCommandMessages();
			Tools.resetTypesWarningAndErrorsAndMessages(model);
			
			VariazioneKeyModel varModel = new VariazioneKeyModel();
			Tools.copyCommandDataModel(model,varModel);
			varModel.setProgressivo(new IntegerType(Costanti.PROGRESSIVO_INIZIALE_SEDE));
			
			WriterAnagraficaManager writerManager = (WriterAnagraficaManager)ROF.getManager(csc,WriterAnagraficaManager.class);
			model = writerManager.cancella(csc,varModel,MappaturaTabelle.CLI_PROSPECT_RETE);

			model.addCommandMessage("propostaCancellata");
			return model;
			
		}catch(Exception e){
			String errorMsg = thisClassName+" Eccezione nel cancellare l'anagrafica: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
	  	}
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public ClienteModel cancellaVariazione(ClientSessionContext csc, ClienteModel model) throws EJBException{
		try{
			
			model.resetCommandErrors(); model.resetCommandMessages();
			Tools.resetTypesWarningAndErrorsAndMessages(model);
			
			VariazioneKeyModel chiave = new VariazioneKeyModel();
			Tools.copyCommandDataModel(model,chiave);
			
			WriterAnagraficaManager writerManager = (WriterAnagraficaManager)ROF.getManager(csc,WriterAnagraficaManager.class);
			writerManager.cancella(csc,chiave,MappaturaTabelle.CLI_ELETTR);
			
			ListType variazioni = model.getVariazioni().getElencoVariazioni();
			for(int i=0;i<variazioni.size();i++){
				ClienteModel variazione = (ClienteModel)variazioni.get(i);
				if(variazione.getProgressivo().intValue() == chiave.getProgressivo().intValue()){
					variazioni.getElements().remove(i);
					break;
				}
			}

			model.addCommandMessage("variazioneCancellata");
			return model;
			
		}catch(Exception e){
			String errorMsg = thisClassName+" Eccezione nel cancellare l'anagrafica: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
	  	}
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public ClienteModel confermaPropostaCliente(ClientSessionContext csc, ClienteModel model) throws EJBException{

		try{
			
			model.resetCommandErrors(); model.resetCommandMessages();
			Tools.resetTypesWarningAndErrorsAndMessages(model);
	
			eseguiControlliSalvataggio(csc,model);
			eseguiControlliInvio(csc,model);
			
			if(Tools.containsTypeErrors(model)){
				model.addCommandError("errori");
				return model;
			}
			
			impostaEnteRilasciante(csc,model);
			impostaCodiceFiscaleForzato(csc,model);
			
			WriterAnagraficaManager writerManager = (WriterAnagraficaManager)ROF.getManager(csc,WriterAnagraficaManager.class);

			if(Tools.containsTypeWarnings(model)){
				model = writerManager.salva(csc,model);
				if(model.getConcurrencyViolationFoundedWidth() != null)
					return model;
				model.addCommandMessage("salvato");
				model.addCommandError("errori");					
			}else{
				model = writerManager.conferma(csc,model);
				if(model.getConcurrencyViolationFoundedWidth() != null)
					return model;
				model.addCommandMessage("confermataProposta");
			}
			
			return model;
			
		}catch(Exception e){
			String errorMsg = thisClassName+" Eccezione nel confermare l'anagrafica: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
	  	}
	}

	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public ClienteModel controllaClienteConChiave(ClientSessionContext csc, 
									     		  ClienteKeyModel clienteKey) throws EJBException{
		
		ClienteModel model = leggiCliente(csc,clienteKey);
		if(model.getStato().equals(StatiPropostaAnagrafica.NON_TROVATA))
			return model;
		
		return controllaCliente(csc,model);
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public ClienteModel controllaCliente(ClientSessionContext csc, 
								   		 ClienteModel model) throws EJBException{
		try{
			
			model.resetCommandErrors(); model.resetCommandMessages();
			Tools.resetTypesWarningAndErrorsAndMessages(model);
	
			eseguiControlliSalvataggio(csc,model);
			eseguiControlliInvio(csc,model);
			
			if(Tools.containsTypeWarningOrErrors(model))
				model.addCommandError("errori");
			return model;
			
		}catch(Exception e){
			String errorMsg = thisClassName+" Eccezione nel controllare l'anagrafica: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
	  	}		
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public ClienteModel inviaInSedeCliente(ClientSessionContext csc, 
									  		ClienteModel model) throws EJBException{
		
		try{
			DocumentoModel documento = model.getDocumento();

			if(!model.isSaltaControlliAllInvioInSede()){
				
				model.resetCommandErrors(); model.resetCommandMessages();
				Tools.resetTypesWarningAndErrorsAndMessages(model);
		
				eseguiControlliSalvataggio(csc,model);
				eseguiControlliInvio(csc,model);
				
				if(Tools.containsTypeWarningOrErrors(model)){
					model.addCommandError("errori");
					return model;
				}
			}
			
			impostaEnteRilasciante(csc,model);
			impostaCodiceFiscaleForzato(csc,model);

			WriterAnagraficaManager writerManager = (WriterAnagraficaManager)ROF.getManager(csc,WriterAnagraficaManager.class);
			
			if(model.getIsPotenziale().booleanValue() && 
			  !model.getStatoConfermato().equals(Costanti.VALORE_FLAG_STATO_CONFERMATO)){
				
				model = writerManager.inviaInSede(csc,model);
				if(model.getConcurrencyViolationFoundedWidth() != null)
					return model;
				model.addCommandMessage("propostaInviataInSede");
				if(controllaDataRilascioGiornoFestivo(csc, documento))
					model.getDatiApplicativi().appendMessaggioCentrale("<br><li>La data rilascio risulta essere un giorno festivo, si prega di inviare in sede copia cartacea del documento</li>");					
				
			}else{
				
				model = writerManager.inviaInSede(csc,model);
				if(model.getConcurrencyViolationFoundedWidth() != null)
					return model;
				model.addCommandMessage("variazioneInviataInSede");
				
			}
			
			return model;
			
		}catch(Exception e){
			String errorMsg = thisClassName+" Eccezione nel inviare l'anagrafica: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
	  	}
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public IndirizzoModel nuovoIndirizzo(ClientSessionContext csc) throws EJBException{
		
		DAOObject dao = null;
		try{
			
			dao = new DAOObject(csc,getNomeDAO(csc));
			dao.openConnection();						

			IndirizzoModel model = new IndirizzoModel();

			dao.fillCodDesc(model);
			return model;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nel inizializzare un nuovo indirizzo: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO nel inizializzare un nuovo indirizzo: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}finally{
			if(dao != null) dao.closeConnection();
		}
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public TelefonoModel nuovoTelefono(ClientSessionContext csc) throws EJBException{
		
		DAOObject dao = null;
		try{
			
			dao = new DAOObject(csc,getNomeDAO(csc));
			dao.openConnection();						

			TelefonoModel model = new TelefonoModel();

			dao.fillCodDesc(model);
			return model;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nel inizializzare un nuovo telefono: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO nel inizializzare un nuovo telefono: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}finally{
			if(dao != null) dao.closeConnection();
		}
	}
	
	/********************************************************************************************************/
	/********************************************************************************************************/
	public AgenteModel leggiAgente(ClientSessionContext csc, StringType codAgente) throws EJBException{

		DAOObject dao = null;
		try{
			
			dao = new DAOObject(csc,getNomeDAO(csc));
			dao.openConnection();
			
			String filledCodAgente = Tools.fillSx(codAgente.toString(),'0',10);
			AgenteModel agente = new AgenteModel();
			agente.setCodAgente(new StringType(filledCodAgente));		

			boolean esisteAgente = true;
			try{							
				DAOQueryResultModel qRes = dao.executeQueryAccess("loadAgente",agente);
				if(qRes.getResult().size() == 0)
					esisteAgente = false;
			}catch(DAOException daoe){
				esisteAgente = false;
			}
			
			if(!esisteAgente){
				agente = new AgenteModel();
				agente.setCicloVitaAgente(new StringType(CicliVitaAgente.NON_TROVATO));
				agente.setCodAgente(new StringType(filledCodAgente));
				agente.setCodRete(new StringType(csc.getChannelCode()));				
				agente.setServerReplica(new StringType(Costanti.SERVER_REPLICA_NULLO));				
			}else{
				
				// Imposto il server replica
				agente.setServerReplica(new StringType());				
				try{							
					dao.executeQueryAccess("loadServerReplicaAgente",agente);
				}catch(DAOException daoe){
					String errorMsg = thisClassName+" Eccezione DAO nel recuperare il server replica per l'agente ["+codAgente+"]: "+daoe;
					EJBException ejbe = new EJBException(errorMsg);
					LOG.error(ejbe);
					throw ejbe;
				}
				
			}
			return agente;
			
		}catch(DAOException daoe){
			String errorMsg = thisClassName+" Eccezione DAO nel leggere l'agente ["+codAgente+"]: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = thisClassName+" Eccezione nel leggere l'agente ["+codAgente+"]: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			throw ejbe;
		}finally{
			if(dao != null) dao.closeConnection();
		}
	}
	
	/********************************************************************************************************/
	/********************************************************************************************************/
	private AgenteModel leggiAltroAgente(ClientSessionContext csc, StringType codAgente) throws EJBException{

		DAOObject dao = null;
		try{
			
			dao = new DAOObject(csc,getNomeDAO(csc));
			dao.openConnection();
			
			String filledCodAgente = Tools.fillSx(codAgente.toString(),'0',10);
			AgenteModel agente = new AgenteModel();
			agente.setCodAgente(new StringType(filledCodAgente));		

			boolean esisteAgente = true;
			try{							
				DAOQueryResultModel qRes = dao.executeQueryAccess("loadAltroAgente",agente);
				if(qRes.getResult().size() == 0)
					esisteAgente = false;
			}catch(DAOException daoe){
				esisteAgente = false;
			}
			
			if(!esisteAgente){
				agente = new AgenteModel();
				agente.setCicloVitaAgente(new StringType(CicliVitaAgente.NON_TROVATO));
				agente.setCodAgente(new StringType(filledCodAgente));
				agente.setCodRete(new StringType(csc.getChannelCode()));				
				agente.setServerReplica(new StringType(Costanti.SERVER_REPLICA_NULLO));				
			}
			return agente;
			
		}catch(DAOException daoe){
			String errorMsg = thisClassName+" Eccezione DAO nel leggere l'agente ["+codAgente+"]: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = thisClassName+" Eccezione nel leggere l'agente ["+codAgente+"]: "+e;
			EJBException ebje = new EJBException(errorMsg);
			throw ebje;
		}finally{
			if(dao != null) dao.closeConnection();
		}
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public ClienteModel leggiVariazione(ClientSessionContext csc, 
									  	 VariazioneKeyModel variazioneKey) throws EJBException{
		
		DAOObject dao = null;
		try{
			
			dao = new DAOObject(csc,getNomeDAO(csc));
			dao.openConnection();						

			ClienteModel clienteOriginale = trovaCliente(dao,variazioneKey);
			if(clienteOriginale != null)
				LoaderAnagrafica.loadCliente(csc,dao,clienteOriginale,false,false,false);				

			ClienteModel model = LoaderAnagrafica.loadVariazione(csc,dao,variazioneKey,clienteOriginale);			
			
			CodDescDataList listSettoriEconomici = InfoLoader.leggiCodDescListSettoriEconomici(csc, model, false);
			model.getInfoPersonali().addCodDescField("codSettoreEconomico",listSettoriEconomici);

			CodDescDataList listCodiciSae = InfoLoader.leggiCodDescListSae(csc, model, model.getIsDitta().booleanValue());
			model.getInfoPersonali().addCodDescField("codSottogruppoAttivita",listCodiciSae);

			if(!model.getIsDitta().booleanValue()){
				CodDescDataList listCodiciAteco = InfoLoader.leggiCodDescListAteco(csc, model, false);
				model.getInfoPersonali().addCodDescField(S_CODATECO,listCodiciAteco);
			}

			// Con FATCA: Se l'anagrafica in variazione ha già impostato la motivazione pep ma non corrisponde a nessun valore nella tendina
			// ne lasciamo inalterata la valorizzazione e disabilitiamo la tendina (in pagina, se "motivazionePepNonCongruente = true" allora creiamo una tendina "finta")
			if(!model.getResidenza().getFlagPep().isNull() && !model.getResidenza().getFlagPep().equals("N")){
				BooleanType existMotivazionePep = (BooleanType)dao.executeQueryAccess("existMotivazionePep", model.getResidenza()).getSingleResult();
				if(existMotivazionePep != null && !existMotivazionePep.booleanValue()) {
					model.getResidenza().getCodDescFields().remove("motivazionePep");
					model.getDatiApplicativi().setMotivazionePepNonCongruente(true);
				}
			}
			
			dao.fillCodDesc(model);
			return model;
				
		}catch(Exception e){
			String errorMsg = thisClassName+" Eccezione nel leggere la variazione n° ["+variazioneKey.getProgressivo()+"]: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(DAOException daoe){
			String errorMsg = thisClassName+" Eccezione DAO nel leggere la variazione n° ["+variazioneKey.getProgressivo()+"]: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}finally{
			if(dao != null) dao.closeConnection();
		}
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public ClienteModel calcolaCodiceFiscale(ClientSessionContext csc, ClienteModel model) throws EJBException{
		try{
			model.getCodFiscale().resetTypeMessages();
			VerificheAnagrafica.verificaComune(csc,model.getComuneNascita(),model);
			String codFiscale = CalcoloCodiceFiscale.calcolaCodiceFiscale(csc,model);
			model.setCodFiscale(new StringType(codFiscale));
			if(codFiscale.equals(""))
				model.getCodFiscale().addTypeMessage("msg.codFiscaleNonCalcolabile");
			return model;
		}catch(Exception e){
			throw new EJBException(e.toString());
		}
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public ClienteModel leggiFotografieCliente(ClientSessionContext csc, ClienteModel cliente) throws EJBException {
		DAOObject dao = null;
		try{
			
			dao = new DAOObject(csc,getNomeDAO(csc));
			dao.openConnection();						

			DAOQueryResultModel qRes = dao.executeQueryAccess("elencoFotografieCliente",cliente);
			cliente.setElencoFotografie(qRes.getResult());
			
			for(int i=0;i<cliente.getElencoFotografie().size();i++){
				FotografiaModel f = (FotografiaModel)cliente.getElencoFotografie().get(i);
				if(f.getIdRepository().isNull())
					continue;
				try{
					DAOQASResultModel qasRes = dao.executeQASAccess("loadStatoFotografiaCliente",f);
					if(qasRes.getQasCallData().getStatus() != QASCallData.STATUS_OK){
						f.setStatoRepository(new StringType("N.P."));
					}else{
						try{
							qRes = dao.executeQueryAccess("loadDescrStatoFotografiaCliente",f);
							if(qRes.getResult().size() == 0)
								f.setStatoRepository(new StringType());
						}catch(Throwable t){
							f.setStatoRepository(new StringType());
						}
						if(f.getStatoRepository().isNull())
							f.setStatoRepository(new StringType("Elaborata dalla sede"));
					}
				}catch(Throwable t){
					LOG.error(t);
					f.setStatoRepository(new StringType("N.P."));
				}

			}
			return cliente;
				
		}catch(Exception e){
			String errorMsg = thisClassName+" Eccezione nel leggere la foto per ["+cliente.getCodFiscale()+"]: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(DAOException daoe){
			String errorMsg = thisClassName+" Eccezione DAO nel leggere la foto per ["+cliente.getCodFiscale()+"]: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}finally{
			if(dao != null) dao.closeConnection();
		}
	}

	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public ClienteModel salvaFotografiaCliente(ClientSessionContext csc, ClienteModel cliente) throws EJBException {
		try{
			cliente.resetCommandMessages();
			try{							
				DAOObject dao = new DAOObject(csc,getNomeDAO(csc));
				dao.executeQueryAccess("loadServerReplicaAgente",cliente);
			}catch(DAOException daoe){
				String errorMsg = thisClassName+" Eccezione DAO nel recuperare il server replica per l'agente ["+cliente.getCodAgente()+"]: "+daoe;
				EJBException ejbe = new EJBException(errorMsg);
				LOG.error(ejbe);
				throw ejbe;
			}
			WriterAnagraficaManager writerManager = (WriterAnagraficaManager)ROF.getManager(csc,WriterAnagraficaManager.class);
			cliente = writerManager.salvaFotografia(csc,cliente);
			return cliente;
		}catch(Exception e){
			String errorMsg = thisClassName+" Eccezione in scriviFotografiaCliente: "+e;
			throw new EJBException(errorMsg);
		}
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public static ClienteModel trovaCliente(ClientSessionContext csc, ClienteKeyModel clienteKey) throws DAOException, Exception{
		DAOObject dao = null;
		try{
			
			dao = new DAOObject(csc,getNomeDAO(csc));
			dao.openConnection();
			return trovaCliente(dao,clienteKey);
			
		}catch(Exception e){
			String errorMsg = thisClassName+" Eccezione nel leggere l'anagrafica: "+e;
			e = new Exception(errorMsg);
			LOG.error(e);
			throw e;
		}catch(DAOException daoe){
			String errorMsg = thisClassName+" Eccezione DAO nel leggere l'anagrafica: "+daoe;
			Exception e = new Exception(errorMsg);
			LOG.error(e);
			throw e;
		}finally{
			if(dao != null) dao.closeConnection();
		}
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	private static ClienteModel trovaCliente(DAOObject dao, ClienteKeyModel clienteKey) throws DAOException, Exception{
		
		DAOQueryResultModel res;
		ClienteModel model = new ClienteModel();
		String codAgente = clienteKey.getCodAgente().toString();
		if(!codAgente.equals(""))
			codAgente = Tools.fillSx(codAgente,'0',10);
		model.setCodAgente(new StringType(codAgente));
		// Agente spv
		String codAgenteSpv = clienteKey.getCodAgenteSpv().toString();
		if(!codAgenteSpv.equals(""))
			codAgenteSpv = Tools.fillSx(codAgenteSpv,'0',10);
		model.setCodAgenteSpv(new StringType(codAgenteSpv));

		// La chiave è di un effettivo
		if(!clienteKey.getCodMediolanum().isNull()){

			model.setCodMediolanum(clienteKey.getCodMediolanum());
			
			model.getDatiApplicativi().setNomeTabella(new StringType(MappaturaTabelle.CLI));
			res = dao.executeQueryAccess("trovaCliente",model);
			if(res.getResult().size() > 0)
				return model;
			model.getDatiApplicativi().setNomeTabella(new StringType(MappaturaTabelle.CLI_COINT));
			res = dao.executeQueryAccess("trovaCliente",model);
			if(res.getResult().size() > 0)
				return model;
			
			return null;
		}
		
		// La chiave può essere di un potenziale
		model.setCodPotenziale(clienteKey.getCodPotenziale());
			
		model.getDatiApplicativi().setNomeTabella(new StringType(MappaturaTabelle.CLI_PROSPECT_RETE));
		res = dao.executeQueryAccess("trovaCliente",model);
		if(res.getResult().size() > 0)
			return model;
		model.getDatiApplicativi().setNomeTabella(new StringType(MappaturaTabelle.CLI_PROSPECT));
		res = dao.executeQueryAccess("trovaCliente",model);
		if(res.getResult().size() > 0)
			return model;
		model.getDatiApplicativi().setNomeTabella(new StringType(MappaturaTabelle.CLI));
		res = dao.executeQueryAccess("trovaCliente",model);
		if(res.getResult().size() > 0)
			return model;
		model.getDatiApplicativi().setNomeTabella(new StringType(MappaturaTabelle.CLI_COINT));
		res = dao.executeQueryAccess("trovaCliente",model);
		if(res.getResult().size() > 0)
			return model;
		model.getDatiApplicativi().setNomeTabella(new StringType(MappaturaTabelle.CLI_ELETTR));
		res = dao.executeQueryAccess("trovaCliente",model);
		if(res.getResult().size() > 0)
			return model;

		// Ultima spiaggia....solo se ho codice fiscale/partita iva
		// La chiave può essere di uno che è diventato effettivo ma il potzle non viene trovato
		// Provo con codice fiscale e partita iva
		if(clienteKey.getCodFiscale().isNull() && 
		   clienteKey.getPartitaIva().isNull())
			return null;
		
		model.setCodPotenziale(new StringType());
		model.setCodFiscale(clienteKey.getCodFiscale());
		model.setPartitaIva(clienteKey.getPartitaIva());

		model.getDatiApplicativi().setNomeTabella(new StringType(MappaturaTabelle.CLI));
		res = dao.executeQueryAccess("trovaCliente",model);
		if(res.getResult().size() > 0)
			return model;
		model.getDatiApplicativi().setNomeTabella(new StringType(MappaturaTabelle.CLI_COINT));
		res = dao.executeQueryAccess("trovaCliente",model);
		if(res.getResult().size() > 0)
			return model;

		return null;
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	private static ClienteModel trovaClienteSenzaAgente(DAOObject dao, ClienteKeyModel clienteKey) throws DAOException, Exception{
		
		DAOQueryResultModel res;
		ClienteModel model = new ClienteModel();
		String codAgente = clienteKey.getCodAgente().toString();
		if(!codAgente.equals(""))
			codAgente = Tools.fillSx(codAgente,'0',10);
		model.setCodAgente(new StringType(codAgente));
		// Agente spv
		String codAgenteSpv = clienteKey.getCodAgenteSpv().toString();
		if(!codAgenteSpv.equals(""))
			codAgenteSpv = Tools.fillSx(codAgenteSpv,'0',10);
		model.setCodAgenteSpv(new StringType(codAgenteSpv));

		// La chiave è di un effettivo
		if(!clienteKey.getCodMediolanum().isNull()){

			model.setCodMediolanum(clienteKey.getCodMediolanum());
			
			model.getDatiApplicativi().setNomeTabella(new StringType(MappaturaTabelle.CLI));
			res = dao.executeQueryAccess("trovaClienteSenzaAgente",model);
			if(res.getResult().size() > 0)
				return model;
			model.getDatiApplicativi().setNomeTabella(new StringType(MappaturaTabelle.CLI_COINT));
			res = dao.executeQueryAccess("trovaClienteSenzaAgente",model);
			if(res.getResult().size() > 0)
				return model;
			
			return null;
		}
		
		// La chiave può essere di un potenziale
		model.setCodPotenziale(clienteKey.getCodPotenziale());
			
		model.getDatiApplicativi().setNomeTabella(new StringType(MappaturaTabelle.CLI_PROSPECT_RETE));
		res = dao.executeQueryAccess("trovaClienteSenzaAgente",model);
		if(res.getResult().size() > 0)
			return model;
		model.getDatiApplicativi().setNomeTabella(new StringType(MappaturaTabelle.CLI_PROSPECT));
		res = dao.executeQueryAccess("trovaClienteSenzaAgente",model);
		if(res.getResult().size() > 0)
			return model;
		model.getDatiApplicativi().setNomeTabella(new StringType(MappaturaTabelle.CLI));
		res = dao.executeQueryAccess("trovaClienteSenzaAgente",model);
		if(res.getResult().size() > 0)
			return model;
		model.getDatiApplicativi().setNomeTabella(new StringType(MappaturaTabelle.CLI_COINT));
		res = dao.executeQueryAccess("trovaClienteSenzaAgente",model);
		if(res.getResult().size() > 0)
			return model;
		model.getDatiApplicativi().setNomeTabella(new StringType(MappaturaTabelle.CLI_ELETTR));
		res = dao.executeQueryAccess("trovaClienteSenzaAgente",model);
		if(res.getResult().size() > 0)
			return model;

		// Ultima spiaggia....solo se ho codice fiscale/partita iva
		// La chiave può essere di uno che è diventato effettivo ma il potzle non viene trovato
		// Provo con codice fiscale e partita iva
		if(clienteKey.getCodFiscale().isNull() && 
		   clienteKey.getPartitaIva().isNull())
			return null;
		
		model.setCodPotenziale(new StringType());
		model.setCodFiscale(clienteKey.getCodFiscale());
		model.setPartitaIva(clienteKey.getPartitaIva());

		model.getDatiApplicativi().setNomeTabella(new StringType(MappaturaTabelle.CLI));
		res = dao.executeQueryAccess("trovaClienteSenzaAgente",model);
		if(res.getResult().size() > 0)
			return model;
		model.getDatiApplicativi().setNomeTabella(new StringType(MappaturaTabelle.CLI_COINT));
		res = dao.executeQueryAccess("trovaClienteSenzaAgente",model);
		if(res.getResult().size() > 0)
			return model;

		return null;
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	private ClienteModel trovaClienteTitolare(DAOObject dao,  ClienteKeyModel dittaKey) throws DAOException, Exception{
		
		String codFiscaleError = "err.codFiscaleMultiplo";
		
		DAOQueryResultModel res;
		ClienteModel model = new ClienteModel();
		model.setCodAgente(dittaKey.getCodAgente());
		model.setCodPotenziale(dittaKey.getCodPotenziale());
		model.setCodMediolanum(dittaKey.getCodMediolanum());
		model.setCodFiscale(dittaKey.getCodFiscale());

		model.getDatiApplicativi().setNomeTabella(new StringType(MappaturaTabelle.CLI));
		res = dao.executeQueryAccess("trovaClienteTitolare",model);
		if(res.getResult().size() == 1)
			return model;
		if(res.getResult().size() > 1){
			model.getCodFiscale().addTypeError(codFiscaleError);
			return model;
		}
		model.getDatiApplicativi().setNomeTabella(new StringType(MappaturaTabelle.CLI_COINT));
		res = dao.executeQueryAccess("trovaClienteTitolare",model);
		if(res.getResult().size() == 1)
			return model;
		if(res.getResult().size() > 1){
			model.getCodFiscale().addTypeError(codFiscaleError);
			return model;
		}
		model.getDatiApplicativi().setNomeTabella(new StringType(MappaturaTabelle.CLI_ELETTR));
		res = dao.executeQueryAccess("trovaClienteTitolare",model);
		if(res.getResult().size() == 1)
			return model;
		if(res.getResult().size() > 1){
			model.getCodFiscale().addTypeError(codFiscaleError);
			return model;
		}

		return null;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void eseguiControlliSalvataggio(ClientSessionContext csc, ClienteModel model) throws Exception{
		
		// Controllo generali
		Controlli controlli = (Controlli)DBJavaClassLoader.loadDbJavaObject(csc,ControlliAnagraficaSalvataggio.class);
		controlli.eseguiControllo(csc,model);
		
		// Controllo codice fiscale per le persone fisiche e le ditte senza titolare
		if(model.getIsPersonaFisica().booleanValue() || 
			(model.getIsDitta().booleanValue() && model.getClienteTitolare() == null)){
			controlli = (Controlli)DBJavaClassLoader.loadDbJavaObject(csc,ControlloCodiceFiscale.class);
			controlli.eseguiControllo(csc,model);
		}
		// Controllo partita IVA per le ditte e le persone giuridiche
		if(model.getIsDatoreDiLavoro().booleanValue() || model.getIsDitta().booleanValue()){
			controlli = (Controlli)DBJavaClassLoader.loadDbJavaObject(csc,ControlloPartitaIva.class);		
			controlli.eseguiControllo(csc,model);
		}
		return;
		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void eseguiControlliInvio(ClientSessionContext csc, ClienteModel model) throws Exception{
		
		Controlli controlli = (Controlli)DBJavaClassLoader.loadDbJavaObject(csc,ControlliAnagraficaInvio.class);
		controlli.eseguiControllo(csc,model);
		return;
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void impostaCancellabile(ClientSessionContext csc, ClienteModel model){
		
	    DAOObject dao = null;
	    try {
	
	      dao = new DAOObject(csc, getNomeDAOVerifiche(csc));
	      dao.openConnection();
	      
	      if(model.getIsPotenziale().booleanValue() && model.getStatoProposta().equals(StatiPropostaAnagrafica.BOZZA)){
	    	  try{
				DAOQueryResultModel qRes = dao.executeQueryAccess("verificaSeAnagraficaUtilizzataInMHD",model);
				IntegerType count = (IntegerType)qRes.getSingleResult();
				if(count.intValue() == 0)
					model.setIsCancellabile(new BooleanType(true));				
	    	  }catch(DAOException daoe){
				model.setIsCancellabile(new BooleanType(true));						
	    	  }
	      }
	      
	    }catch (DAOException daoe) {
	    	LOG.warning(getClass() + "Eccezione DAO nel verificare se l'anagrafica è utilizzata in MHD: "+daoe);
	    }catch (Exception e) {
	    	LOG.warning(getClass() + "Eccezione nel verificare se l'anagrafica è utilizzata in MHD: "+e);
	    }finally {
	      if (dao != null)
	        dao.closeConnection();
	    }
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void impostaCodiceFiscaleForzato(ClientSessionContext csc, ClienteModel model) throws Exception{
		model.getDatiApplicativi().setCodiceFiscaleForzato(new StringType());
		String codFiscaleCalcolato = CalcoloCodiceFiscale.calcolaCodiceFiscale(csc,model);
		if(!model.getCodFiscale().isNull() && !model.getCodFiscale().equals(codFiscaleCalcolato))
			model.getDatiApplicativi().setCodiceFiscaleForzato(new StringType(Costanti.FLAG_CODICE_FISCALE_FORZATO));
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void impostaEnteRilasciante(ClientSessionContext csc, ClienteModel model) throws Exception{
		
	    DAOObject dao = null;
	    try {
	
	      dao = new DAOObject(csc, getNomeDAO(csc));
	      dao.openConnection();
	      
	      DAOQueryResultModel qres = dao.executeQueryAccess("getEnteRilasciante",model);
	      StringType codEnte = (StringType)qres.getSingleResult();
	      if(codEnte != null && !codEnte.isNull())
	    	  model.getDocumento().setCodEnteRilasciante(codEnte);
	      else
	    	  model.getDocumento().setCodEnteRilasciante(new StringType());
			
	    }catch (DAOException daoe) {
	    	String errorMsg = getClass() + "Eccezione DAO nell'impostare l'ente rilasciante: "+daoe;
	    	Exception e = new Exception(errorMsg);
	    	LOG.error(e);
	    	throw e;
	    }catch (Exception e) {
	    	String errorMsg = getClass() + "Eccezione nell'impostare l'ente rilasciante: "+e;
	    	e = new Exception(errorMsg);
	    	LOG.error(e);
	    	throw e;
	    }finally {
	      if (dao != null)
	        dao.closeConnection();
	    }
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static String getNomeDAO(ClientSessionContext csc){
		String country = csc.getCountryCode();
		String xmlName = country.substring(0,1).toUpperCase()+country.substring(1).toLowerCase();
		xmlName += "AnagraficaClienti.AnagraficaClienti";
	  	return xmlName;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String getNomeDAOVerifiche(ClientSessionContext csc){
		String country = csc.getCountryCode();
		String xmlName = country.substring(0,1).toUpperCase()+country.substring(1).toLowerCase();
		xmlName += "AnagraficaClienti.Verifiche";
	  	return xmlName;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String getNomeDAORicerche(ClientSessionContext csc){
		String country = csc.getCountryCode();
		String xmlName = country.substring(0,1).toUpperCase()+country.substring(1).toLowerCase();
		xmlName += "AnagraficaClienti.Ricerche";
	  	return xmlName;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/		
	private boolean controllaDataRilascioGiornoFestivo(ClientSessionContext csc,DocumentoModel documento) {
		
		DAOObject dao = null;
		try {
			dao = new DAOObject(csc,getNomeDAO(csc));
			dao.openConnection();
						
			DAOQueryResultModel qRes = dao.executeQueryAccess("verificaDataRilascioFestivo", documento);
			BooleanType isGiornoFestivita =(BooleanType)qRes.getSingleResult();
			
			BooleanType isGiornoDomenica = new BooleanType();
			
			Calendar dataRilascioCalendar = Calendar.getInstance();
			dataRilascioCalendar.setTime(documento.getDataRilascio().dateValue());

			int dayofweek = dataRilascioCalendar.get(Calendar.DAY_OF_WEEK);
		    if (dayofweek == Calendar.SUNDAY)
		    	isGiornoDomenica = new BooleanType(true);
			
		    if(isGiornoFestivita.booleanValue() || isGiornoDomenica.booleanValue())
		    	return true;
		    else
		    	return false;
			
		}catch(Exception e){
			LOG.error(e);
			return false;
		}catch(DAOException daoe){
			LOG.error(daoe);
			return false;
		}finally{
			if(dao != null) dao.closeConnection();
		}
		
	}
	/***********************************************************************************************/
	/***********************************************************************************************/		
	private void recuperaDifferenzialiCliente(ClientSessionContext csc,ClienteModel model){
		DAOObject dao = null;
		try {
			dao = new DAOObject(csc,"ItaAnagraficaClienti.AnagraficaClientiOracle");
			DAOQueryResultModel qRes = dao.executeQueryAccess("recuperaDifferenziali", model);
			model.setDifferenziali((ListType)qRes.getResult());
		}catch(Exception e){
			LOG.error(e);
		}catch(DAOException daoe){
			LOG.error(daoe);
		}
	}
	
}
