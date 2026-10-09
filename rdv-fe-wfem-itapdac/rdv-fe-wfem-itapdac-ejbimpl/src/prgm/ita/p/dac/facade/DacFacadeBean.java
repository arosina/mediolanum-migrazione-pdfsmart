package prgm.ita.p.dac.facade;

import javax.ejb.EJBException;

import com.atosorigin.wfem.backend.FacadeObject;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.controller.Configuration;
import com.atosorigin.wfem.dao.DAOOSBResultModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQASResultModel;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.DAOTableResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.OSBCallData;
import com.atosorigin.wfem.util.QASCallData;
import com.atosorigin.wfem.util.Tools;

import prgm.ita.p.dac.manager.DacManager;
import prgm.ita.p.dac.model.AbstractRicercaDacModel;
import prgm.ita.p.dac.model.AbstractRicercaDocModel;
import prgm.ita.p.dac.model.AgenteBCModel;
import prgm.ita.p.dac.model.AgenteModel;
import prgm.ita.p.dac.model.ContoCorrenteModel;
import prgm.ita.p.dac.model.DacKeyModel;
import prgm.ita.p.dac.model.DacModel;
import prgm.ita.p.dac.model.DacTestataModel;
import prgm.ita.p.dac.model.DocumentoKeyModel;
import prgm.ita.p.dac.model.DocumentoModel;
import prgm.ita.p.dac.model.ErroreDocumentoModel;
import prgm.ita.p.dac.model.FirmaModel;
import prgm.ita.p.dac.model.FirmeModel;
import prgm.ita.p.dac.model.MezzoPagamentoModel;
import prgm.ita.p.dac.model.ParamsModel;
import prgm.ita.p.dac.util.DacTools;
import prgm.ita.p.dac.util.DocumentoAssegnoTools;
import prgm.ita.p.dac.util.MOMCallIndicator;

/***********************************************************************************************/
/***********************************************************************************************/
public class DacFacadeBean extends FacadeObject implements DacFacade{

	private static final String DAO_DAC_XML_NAME = "ItaPDac.Dac";
	private static final String DAO_RICERCHE_XML_NAME = "ItaPDac.Ricerche";
	private static final String DAO_FIRME_XML_NAME = "ItaPDac.Firme";

	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacModel caricaDacAttiva(ClientSessionContext csc, ParamsModel params) throws EJBException {
		
		try{
			
			DacModel dac = new DacModel();
			
			DacTools.loadUfficioUtente(csc, params);
			dac.copyParams(params);
			if(params.isAutoSpuntataParams()) // Il parametro 'autoSpuntata' Arriva da menù
				dac.setIsAutoSpuntata(new BooleanType(true));
			
			if(dac.getTipoDac().isNull())
				throw new EJBException("Attenzione ! Tipo DAC non valorizzato");
			
			DAOObject dao = new DAOObject(csc,DAO_DAC_XML_NAME);

			loadInfoBC(dao, dac);

			// Imposto l'agente di riferimento
			if(params.getUfficio().equals(Costanti.UFFICIO_RETE)){
				AgenteModel agenteRiferimento = dac.getAgenteRiferimento();
				agenteRiferimento.setCodAgente(new StringType(Tools.fillSx(csc.getCurrentLinkedUserCode(),'0',10)));
				dao.executeQueryAccess("loadAgente",agenteRiferimento);
				if(DacTools.alwaysCanMakeForOther(csc))
					agenteRiferimento.setCanMakeForOtherFb(new BooleanType(true));
				dac.setAgenteRiferimento(agenteRiferimento);
			}
			
			DAOObject daoAss = new DAOObject(csc,DAO_DAC_XML_NAME);
			if(csc.isAssistenteFB()){
				ClientSessionContext cscAss = (ClientSessionContext)Tools.cloneObject(csc); 
				cscAss.setUserCode(csc.getCurrentLinkedUserCode().toString());
				daoAss = new DAOObject(cscAss,DAO_DAC_XML_NAME);
			}
			DAOQueryResultModel qRes = null;
			if(Configuration.getInstance().isOfflineEnvironment())
				qRes = daoAss.executeQueryAccess("esisteDacCorrenteOffline",dac);
			else
				qRes = daoAss.executeQueryAccess("esisteDacCorrenteOnline",dac);
			StringType idDac = (StringType)qRes.getSingleResult();
			if(idDac == null || idDac.isNull()){
				dac.setUffMittente(new IntegerType(dac.getUfficio()));
				dac.setUffDestinatario(new IntegerType(Costanti.UFFICIO_CODING_SPUNTA));
				DacTools.impostaVersioneDac(csc,dac);
				DacManager manager = (DacManager)ROF.getManager(csc,DacManager.class);
				dac = manager.salvaDac(csc,dac);				
			}else{
				dac.setIdDac(idDac);
				DacLoader.reloadDac(csc,dao,params,dac);
			}
			dac.setFnc(new StringType(Costanti.FNC_GESTIONE));
			return dac;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO in caricaDacAttiva: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in caricaDacAttiva: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void loadInfoBC(DAOObject dao, DacModel dac) throws DAOException {
		dac.setElencoBC(dao.executeQueryAccess("elencoBC", dac).getResult());
		for(int i=0;i<dac.getElencoBC().size();i++) {
			AgenteBCModel bc = (AgenteBCModel)dac.getElencoBC().get(i);
			dao.executeQueryAccess("loadDatiDacAgenteBC", bc);
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacModel inviaDac(ClientSessionContext csc, DacModel dac) throws EJBException {
		try{
			
			ControlliDac controlli = (ControlliDac)new ControlliDacImpl();
			if(!controlli.controllaDac(csc, dac)){
				dac.addCommandError("Attenzione! Operazione non effettuata");
				return dac;
			}
			
			DacManager manager = (DacManager)ROF.getManager(csc,DacManager.class);
			dac = manager.inviaDac(csc,dac);
			if(dac.isShowAlert()){
				String msg = dac.getMsg();
				dac = caricaDacAttiva(csc,dac);
				dac.setShowAlert(true);
				dac.setMsg(msg);
			}else{
				dac = leggiDac(csc,dac);
			}
			dac.setRefreshable(true);
			return dac;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in inviaDac: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;			
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public StringType leggiIdUltimaDac(ClientSessionContext csc, ParamsModel params) throws EJBException {
		try{
			
			DacKeyModel dacKey = new DacKeyModel();
			dacKey.copyParams(params);
			
			if(dacKey.getTipoDac().isNull())
				throw new EJBException("Attenzione ! Tipo DAC non valorizzato");
			
			if(MOMCallIndicator.callMOM){
				if(csc.isAssistenteFB())
					dacKey.setUserMOM(new StringType(Tools.fillSx(csc.getCurrentLinkedUserCode(),'0',10)));
				else
					dacKey.setUserMOM(new StringType(Tools.fillSx(csc.getUserCode(),'0',10)));
				DAOOSBResultModel osbRes = new DAOObject(csc,Costanti.DAO_XML_NAME_MOM).executeOSBAccess("getIdUltimoPritMOM",dacKey);
				if(osbRes.getWsCallData().getStatus() == OSBCallData.STATUS_OK){
					if(!dacKey.getIdDac().isNull())
						return dacKey.getIdDac();
				}	
			}
			
			DAOObject daoAss = new DAOObject(csc,DAO_DAC_XML_NAME);
			if(csc.isAssistenteFB()){
				ClientSessionContext cscAss = (ClientSessionContext)Tools.cloneObject(csc); 
				cscAss.setUserCode(csc.getCurrentLinkedUserCode().toString());
				daoAss = new DAOObject(cscAss,DAO_DAC_XML_NAME);
			}
			DAOQueryResultModel qRes = daoAss.executeQueryAccess("ultimaDacInviata",dacKey);
			StringType ultimaKey = (StringType)qRes.getSingleResult();
			if(ultimaKey == null || ultimaKey.isNull())
				return new StringType();
			return ultimaKey;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO in leggiUltimaDac: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in leggiUltimaDac: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;			
		}
		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacModel leggiDacLight(ClientSessionContext csc, DacKeyModel dacKey) throws EJBException {
		
		DAOObject dao = null;
		try{
			
			// Gestione localizzazione DAC
			DacModel dac = new DacModel();
			dac.setIdDac(dacKey.getIdDac());
			dao = DacLoader.loadDacEverywhere(csc,dacKey,dac,true);
			dao.fillCodDesc(dac,false);
			return dac;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in leggiDacLight: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}finally{
			if(dao != null) dao.closeConnection();
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacModel leggiDac(ClientSessionContext csc, DacKeyModel dacKey) throws EJBException {
		
		DAOObject dao = null;
		try{
			
			DacModel dac = new DacModel();
			dac.setIdDac(dacKey.getIdDac());
			dao = DacLoader.loadDacEverywhere(csc,dacKey,dac,false);
			dao.fillCodDesc(dac,false);
			return dac;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in leggiDac: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;			
		}finally{
			if(dao != null) dao.closeConnection();
		}
		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacModel leggiDacCompleta(ClientSessionContext csc, DacKeyModel dacKey) throws EJBException {
		
		DAOObject dao = null;
		try{
			
			DacModel dac = new DacModel();
			dac.setIdDac(dacKey.getIdDac());
			dac.setForceLoadPlichiBusta(true);
			dao = DacLoader.loadDacEverywhere(csc,dacKey,dac,false);
			dac.setForceLoadPlichiBusta(false);
			if(dac.getIsPritMOM().booleanValue())
				return dac;
			
			// Carico i dati di ciascun dettaglio
			ListType documenti = dac.getDocumenti();
			for(int i=0;i<documenti.size();i++){
				DocumentoModel doc = (DocumentoModel)documenti.get(i);
				// Salvo il codice aggregatore in quanto potrebbe essere stato letto dalla busta o dal documento
				String savCodAggr = doc.getCodAggregatore().toString();
				dao.executeTableLoadAccess("documento",doc); // Leggo tutti i dati del documento
				doc.setCodAggregatore(new StringType(savCodAggr));
				doc.setUfficio(dac.getUfficio());
				if(!dac.getUfficio().equals(Costanti.UFFICIO_RETE))
					dao.executeTableLoadAccess("documentoSede",doc);
				if(doc.isDocumentoAssegno())
					dao.executeTableLoadAccess("mezzoDiPagamento",doc.getDatiAssegno());
				else
					DacLoader.loadMezziPgDocumento(csc, dao, doc);
			}
			return dac;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO in leggiDacCompleta: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in leggiDacCompleta: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;			
		}finally{
			if(dao != null) dao.closeConnection();
		}
		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacModel salvaDac(ClientSessionContext csc, DacModel dac) throws EJBException {
		try{
			
			ControlliDac controlli = (ControlliDac)new ControlliDacImpl();
			if(!controlli.controllaDac(csc,dac)){
				dac.addCommandError("Attenzione! Operazione non effettuata");
				return dac;
			}
			
			DacManager manager = (DacManager)ROF.getManager(csc,DacManager.class);
			dac = manager.salvaDac(csc,dac);
			dac.setRefreshable(true);
			return dac;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in salvaDac: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;			
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacModel lavoraDac(ClientSessionContext csc, DacModel dac) throws EJBException {
		try{

			boolean refreshable = false;
			if( dac.getStato().equals(Costanti.STATO_SPEDITA) ||
			   (dac.getStato().equals(Costanti.STATO_LAVORATA) && dac.getEsito().equals(Costanti.ESITO_DAC_TUTTI_MANCANTI))){
				DacTools.loadUfficioUtente(csc,dac);
				DacManager manager = (DacManager)ROF.getManager(csc,DacManager.class);
				dac = manager.lavoraDac(csc,dac);
				refreshable = true;
			}
			
			dac = leggiDac(csc, dac);
			dac.setRefreshable(refreshable);
			return dac;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in lavoraDac: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;			
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacModel spuntaDac(ClientSessionContext csc, DacModel dac) throws EJBException {
		try{
			
			ControlliDac controlli = (ControlliDac)new ControlliDacImpl();
			if(!controlli.controllaDac(csc, dac)){
				dac.addCommandError("Attenzione! Operazione non effettuata");
				return dac;
			}
			
			DacManager manager = (DacManager)ROF.getManager(csc,DacManager.class);
			dac = manager.spuntaDac(csc,dac);
			dac.setRefreshable(true);
			return dac;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in spuntaDac: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;			
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacModel autorizzaErroreDocumento(ClientSessionContext csc, DacModel dac) throws EJBException {
		try{
			
			ErroreDocumentoModel errore = dac.getErroreDocumento();
			if(errore.getProgressivo().isNull())
				throw new EJBException("autorizzaErroreDocumento: Progressivo non velorizzato");
			
			errore.setDataAutorizzazione(Tools.now());

			DAOObject dao = new DAOObject(csc,DAO_DAC_XML_NAME);
			dao.executeTableUpdateAccess("autorizzaErroreDocumento",errore);
			
			DacLoader.loadErroriDocumentiDac(csc,dao,dac);
			return dac;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO in autorizzaErroreDocumento: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in autorizzaErroreDocumento: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;			
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacModel nuovoDocumento(ClientSessionContext csc, DacModel dac) throws EJBException {
		try{
			DocumentoModel doc = new DocumentoModel();
			doc.copyParams(dac);
			if(!dac.getAgenteRiferimento().getCodAgente().isNull())
				doc.setAgente((AgenteModel)Tools.cloneObject(dac.getAgenteRiferimento()));
			if(doc.getFnc().equals(Costanti.FNC_SPUNTA))
				doc.setEsito(new IntegerType(Costanti.ESITO_DOC_AGGIUNTO));
			dac.setDocumento(doc);
			impostaOperazioneReportAdeguatezza(csc, doc);
			impostaOperazioneRaccomandazioneIdd(csc, doc);
			return dac;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in nuovoDocumento: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public DocumentoModel leggiDocumento(ClientSessionContext csc, DocumentoKeyModel docKey) throws EJBException {

		DAOObject dao = null;
		try{
			
			// Gestione localizzazione DOC
			docKey.removeParam(ParamsModel.storicizzato);
			DocumentoModel doc = new DocumentoModel();
			doc.setIdDocumento(docKey.getIdDocumento());
			doc.copyParams(docKey);
			
			String connAttuale = "CEPE";
			dao = new DAOObject(csc,DAO_DAC_XML_NAME);
			dao.openConnection();
			DAOTableResultModel tRes = dao.executeTableLoadAccess("documento",doc);
			if(tRes.getResult().intValue() == 0){
				if(Configuration.getInstance().isOfflineEnvironment())
					throw new EJBException("Documento con ID=["+docKey.getIdDocumento()+"] non trovato");

				connAttuale = "IQ_PRIT";
				dao.closeConnection();
				dao.openConnection(connAttuale);
				tRes = dao.executeTableLoadAccess("documento",doc);
				if(tRes.getResult().intValue() == 0)
					throw new EJBException("Documento con ID=["+docKey.getIdDocumento()+"] non trovato");
				doc.addParam(ParamsModel.storicizzato);
			}
			
			if(!doc.getUfficio().equals(Costanti.UFFICIO_RETE)){
				DAOObject daoAgente = new DAOObject(csc,DAO_DAC_XML_NAME);
				daoAgente.executeQueryAccess("loadAgente",doc.getAgente());

				doc.getAgente().setMsgAgenteNonIscrittoAlboOAM("");
				if(doc.isFaseDiSpunta() && !doc.getAgente().getIsIscrittoOAM().booleanValue())
					doc.getAgente().setMsgAgenteNonIscrittoAlboOAM("ATTENZIONE: RIGA PRIT DA SCARTARE<br>METTERE IN CARTELLINA PER TRASMISSIONE IN MEDIOLANUM");
				
				if(!doc.isStoricizzato())
					dao.executeTableLoadAccess("tipoControlloFirmeDocumento",doc);
				dao.executeTableLoadAccess("esitoFirmaClienteDocumento",doc);
				dao.executeTableLoadAccess("esitoFirmaAgenteDocumento",doc);
				dao.executeTableLoadAccess("documentoSede",doc);
			}

			// Il documento assegno non ha mezzi di pagamento
			if(!doc.isDocumentoAssegno())
				DacLoader.loadMezziPgDocumento(csc, dao, doc);

			// Leggo il numero di conti corrente del cliente
			doc.getCliente().setNumContiCorrenti(new IntegerType());
			if(doc.isFaseDiSpunta() && !doc.getCliente().getCodMediolanum().isNull()){
				if(doc.isServizioFirmePerContoCorrente())
					dao.executeQueryAccess("loadNumContiCorrentiCliente",doc.getCliente());
				else
					doc.getCliente().setNumContiCorrenti(new IntegerType(1));
			}
			
			// Leggo il numero di conti corrente dell'agente
			doc.getAgente().setNumContiCorrenti(new IntegerType());
			if(doc.isFaseDiSpunta() && !doc.getAgente().getCodMediolanum().isNull()){
				if(doc.isServizioFirmePerContoCorrente())
					dao.executeQueryAccess("loadNumContiCorrentiCliente",doc.getAgente());
				else
					doc.getAgente().setNumContiCorrenti(new IntegerType(1));
			}
				

			// Rinfresco i dati per il controllo firme agenti
			if(doc.isFaseDiSpunta() || doc.isAutoSpuntataParams())
				dao.executeQueryAccess("loadDatiDocumentoPerControlloFirmeAgentiInSpunta",doc);
			
			// Se il documento è assegno carico i dati dal documento padre del mezzo di pagamento cui fa riferimento
			if(doc.isDocumentoAssegno())
				DocumentoAssegnoTools.impostaDatiDocumentoPadreDocAssegno(dao,doc);

			// Carico la storia documento
			if( Configuration.getInstance().isOnlineEnvironment() && 
			   !doc.isFaseDiSpunta() && 
			   !doc.getStato().equals(Costanti.STATO_INCORSO)){

				if(!doc.getUfficio().equals(Costanti.UFFICIO_RETE) && !doc.getCodAggregatore().isNull())
					doc.setPlicoDocumento(DacLoader.loadPlicoDocumento(csc, dao, doc, false));

				doc.setStoriaDocumento(dao.executeQueryAccess("loadStoriaDocumento",doc).getResult());
				if (connAttuale.equals("CEPE")) {
					connAttuale = "IQ_PRIT";
					dao.closeConnection();
					dao.openConnection(connAttuale);
					ListType storiaCepe = doc.getStoriaDocumento(); 
					ListType storiaIq = dao.executeQueryAccess("loadStoriaDocumento",doc).getResult();
					ListType storiaDoc = new ListType(DacTestataModel.class);
					
					//Inizializzo la storia del doc con le testate presenti su IQ e non su CEPE
					for (int i=0; i<storiaIq.size(); i++) {
						DacTestataModel testataIq = (DacTestataModel)storiaIq.get(i);
						
						boolean testataPresente = false;
						for (int k=0; k<storiaCepe.size(); k++) {
							DacTestataModel testataCepe = (DacTestataModel)storiaCepe.get(k);
							
							if (testataIq.getIdDac().equals(testataCepe.getIdDac())) {
								testataPresente = true;
								break;
							}
						}
						
						if (!testataPresente)
							storiaDoc.add(testataIq);
					}
					
					//Appendo le testate presenti su CEPE
					for (int k=0; k<storiaCepe.size(); k++) {
						DacTestataModel testataCepe = (DacTestataModel)storiaCepe.get(k);
						
						storiaDoc.add(testataCepe);
					}
					
					doc.setStoriaDocumento(storiaDoc);
				}
			}
			
			impostaOperazioneReportAdeguatezza(csc, doc);
			impostaOperazioneRaccomandazioneIdd(csc, doc);
			return doc;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO in leggiDocumento: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in leggiDocumento: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}finally{
			if(dao != null) dao.closeConnection();
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void impostaOperazioneReportAdeguatezza(ClientSessionContext csc, DocumentoModel doc){
		if(doc.getOperazioneReportAdeguatezza() != null)
			return;
		
		// Leggo il prodotto-operazione relativo al report di adeguatezza
		try{
	        StringType operReportAdeguatezza = (StringType)DAOObject.executeDynaQueryAccess(csc,"CEPE", 
														    				"select DOMINIO_C_CODICE "+
																			"from   INR_CE_DOMINIO "+
																			"where  DOMINIO_C_TABELLA = 'PRIT_OPERAZIONE_REPORT_ADEGUATEZZA'", 
																			null, StringType.class).getSingleResult();
			if(operReportAdeguatezza != null)
				doc.setOperazioneReportAdeguatezza(operReportAdeguatezza.toString());
		}catch(DAOException daoe){
			doc.setOperazioneReportAdeguatezza("");
		}
		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void impostaOperazioneRaccomandazioneIdd(ClientSessionContext csc, DocumentoModel doc){
		if(doc.getOperazioneRaccomandazioneIdd() != null)
			return;
		
		// Leggo il prodotto-operazione relativo alla raccomandazioen IDD
		try{
	        StringType operRaccomandazioneIdd = (StringType)DAOObject.executeDynaQueryAccess(csc,"CEPE", 
														    				"select DOMINIO_C_CODICE "+
																			"from   INR_CE_DOMINIO "+
																			"where  DOMINIO_C_TABELLA = 'PRIT_OPERAZIONE_RACCOMANDAZIONE_IDD'", 
																			null, StringType.class).getSingleResult();
			if(operRaccomandazioneIdd != null)
				doc.setOperazioneRaccomandazioneIdd(operRaccomandazioneIdd.toString());
		}catch(DAOException daoe){
			doc.setOperazioneRaccomandazioneIdd("");
		}
		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacModel salvaDocumento(ClientSessionContext csc, DacModel dac) throws EJBException {
		try{
			
			DocumentoModel doc = dac.getDocumento();
			
			IntegerType esitoCorrente = doc.getEsito().isNull() ? new IntegerType() : new IntegerType(doc.getEsito().intValue());
			
			ControlliDac controlli = (ControlliDac)new ControlliDacImpl();
			if(!controlli.controllaDocumento(csc,doc)){
				doc.addCommandError("Attenzione! Operazione non effettuata");
				return dac;
			}
			
			if(dac.isFaseDiSpunta()){
				if(!controlli.controllaPlicoInDac(csc, dac))
					return dac;					
			}
			
			// Se la Dac nasce spuntata l'esito documento è "Accettato" (1)
			if(dac.getIsAutoSpuntata().booleanValue())
				doc.setEsito(new IntegerType(Costanti.ESITO_DOC_ACCETTATO));
			
			DAOObject dao = new DAOObject(csc,DAO_DAC_XML_NAME);
			
			// Aggiorno i dati dell'agente
			dao.executeQueryAccess("loadAgente",doc.getAgente());
			
			// Aggiorno l'elenco dei conti correnti del cliente (Il cliente potrebbe essere stato cambiato dall'utente)
			doc.getCliente().setNumContiCorrenti(new IntegerType());
			if(dac.isFaseDiSpunta() && !doc.getCliente().getCodMediolanum().isNull()){
				if(doc.isServizioFirmePerContoCorrente())
					dao.executeQueryAccess("loadNumContiCorrentiCliente",doc.getCliente());
				else
					doc.getCliente().setNumContiCorrenti(new IntegerType(1));
			}
				
			
			// Aggiorno l'elenco dei conti correnti dell'agente (L'agente potrebbe essere stato cambiato dall'utente)
			// e rinfresco i dati per il controllo firme agenti
			doc.getAgente().setNumContiCorrenti(new IntegerType());
			if(doc.isFaseDiSpunta() && !doc.getAgente().getCodMediolanum().isNull()){
				if(doc.isServizioFirmePerContoCorrente())
					dao.executeQueryAccess("loadNumContiCorrentiCliente",doc.getAgente());
				else
					doc.getAgente().setNumContiCorrenti(new IntegerType(1));
			}
			
			DacManager manager = (DacManager)ROF.getManager(csc,DacManager.class);
			if(doc.getIdDocumento().isNull()){
				doc = manager.creaDocumento(csc, dac.getIdDac(), dac.getAgenteRiferimento(), doc, dac.getUbicazione().intValue());
			}else{
				doc.setEsitoCorrente(esitoCorrente);
				doc = manager.salvaDocumento(csc, doc);
				doc.setEsitoCorrente(new IntegerType());
				// Ricarico i mezzi di pagamento del documento
				DacLoader.loadMezziPgDocumento(csc, dao, doc);
			}

			// Rinfresco i dati per il controllo firme agenti
			if(doc.isFaseDiSpunta() || doc.isAutoSpuntataParams())
				dao.executeQueryAccess("loadDatiDocumentoPerControlloFirmeAgentiInSpunta",doc);
			
			dac.setDocumento(doc);
			
			// Ricarico i documenti della dac
			DacLoader.loadDocumentiDac(csc,dao,dac);
			return dac;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO in salvaDocumento: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in salvaDocumento: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacModel cancellaDocumento(ClientSessionContext csc, DacModel dac) throws EJBException {
		try{
			
			DacManager manager = (DacManager)ROF.getManager(csc,DacManager.class);
			manager.cancellaDocumento(csc,dac.getIdDac(),dac.getDocumento());
			
			// Ricarico i documenti
			DacLoader.loadDocumentiDac(csc,null,dac);
			return dac;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in cancellaDocumento: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacModel esitaDocumento(ClientSessionContext csc, DacModel dac) throws EJBException {
		try{
			
			DocumentoModel doc = dac.getDocumento();
			
			IntegerType esitoCorrente = doc.getEsito().isNull() ? new IntegerType() : new IntegerType(doc.getEsito().intValue());
			doc.setEsito(new IntegerType(doc.getEsitoNew().intValue()));
			
			ControlliDac controlli = (ControlliDac)new ControlliDacImpl();
			if(!controlli.controllaDocumento(csc,doc)){
				doc.addCommandError("Attenzione! Operazione non effettuata");
				doc.setEsito(esitoCorrente);
				return dac;
			}
			
			if(!controlli.controllaPlicoInDac(csc, dac)){
				doc.setEsito(esitoCorrente);
				return dac;					
			}
			
			DAOObject dao = new DAOObject(csc,DAO_DAC_XML_NAME);
			// Gestione stato "Modificato" confrontando la copia dei dati
			if(doc.getEsito().equals(Costanti.ESITO_DOC_ACCETTATO)){
				DocumentoModel docRete = new DocumentoModel();
				docRete.setIdDocumento(doc.getIdDocumento());
				docRete.copyParams(doc);
				DAOTableResultModel tRes = dao.executeTableLoadAccess("documentoCpyRete",docRete);
				if(tRes.getResult().intValue() == 1){
					if(docRete.isDocumentoAssegno())
						dao.executeTableLoadAccess("mezzoDiPagamentoCpyRete",docRete.getDatiAssegno());
					else
						docRete.setMezziPagamento(dao.executeTableLoadChildsAccess("mezzoDiPagamentoCpyRete", docRete, MezzoPagamentoModel.class).getChilds());
					if(!docRete.isEqual(doc))
						doc.setEsito(new IntegerType(Costanti.ESITO_DOC_MODIFICATO));
				}
			}
			
			// Aggiorno i dati dell'agente
			dao.executeQueryAccess("loadAgente",doc.getAgente());
			
			// Aggiorno l'elenco dei conti correnti del cliente
			doc.getCliente().setNumContiCorrenti(new IntegerType());
			if(!doc.getCliente().getCodMediolanum().isNull()){
				if(doc.isServizioFirmePerContoCorrente())
					dao.executeQueryAccess("loadNumContiCorrentiCliente",doc.getCliente());
				else
					doc.getCliente().setNumContiCorrenti(new IntegerType(1));
			}
				
			
			// Aggiorno l'elenco dei conti correnti dell'agente
			doc.getAgente().setNumContiCorrenti(new IntegerType());
			if(doc.isFaseDiSpunta() && !doc.getAgente().getCodMediolanum().isNull()){
				if(doc.isServizioFirmePerContoCorrente())
					dao.executeQueryAccess("loadNumContiCorrentiCliente",doc.getAgente());
				else
					doc.getAgente().setNumContiCorrenti(new IntegerType(1));
			}

			DacManager manager = (DacManager)ROF.getManager(csc,DacManager.class);
			doc.setEsitoCorrente(esitoCorrente);
			doc = manager.salvaDocumento(csc, doc);
			doc.setEsitoCorrente(new IntegerType());
			
			// Rinfresco i dati per il controllo firme agenti
			if(doc.isFaseDiSpunta() || doc.isAutoSpuntataParams())
				dao.executeQueryAccess("loadDatiDocumentoPerControlloFirmeAgentiInSpunta",doc);
			
			// Ricarico i mezzi di pagamento del documento
			DacLoader.loadMezziPgDocumento(csc, dao, doc);
			
			dac.setDocumento(doc);
			
			// Ricarico i documenti della dac
			DacLoader.loadDocumentiDac(csc,dao,dac);
			return dac;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO in esitaDocumento: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in esitaDocumento: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacModel esitaFirmaCliente(ClientSessionContext csc, DacModel dac) throws EJBException {
		try{
			
			// Imposto il nuovo esito
			DocumentoModel doc = dac.getDocumento();
			doc.setEsitoFirmaCliente(new StringType(doc.getEsitoFirmaClienteNew().toString()));

			DacManager manager = (DacManager)ROF.getManager(csc,DacManager.class);
			doc = manager.esitaFirmaCliente(csc, doc);

			// Ricarico i documenti della dac
			DacLoader.loadDocumentiDac(csc,new DAOObject(csc,DAO_DAC_XML_NAME),dac);
			return dac;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in esitaFirmaCliente: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacModel esitaFirmaAgente(ClientSessionContext csc, DacModel dac) throws EJBException {
		try{
			
			// Imposto il nuovo esito
			DocumentoModel doc = dac.getDocumento();
			doc.setEsitoFirmaAgente(new StringType(doc.getEsitoFirmaAgenteNew().toString()));

			DacManager manager = (DacManager)ROF.getManager(csc,DacManager.class);
			doc = manager.esitaFirmaAgente(csc, doc);

			// Ricarico i documenti della dac
			DacLoader.loadDocumentiDac(csc,new DAOObject(csc,DAO_DAC_XML_NAME),dac);
			return dac;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in esitaFirmaAgente: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacModel pinzaDocumenti(ClientSessionContext csc, DacModel dac) throws EJBException {
		try{
			
			DocumentoModel unDocSelezionato = null; 
			ListType documenti = dac.getDocumenti();
			for(int i=0;i<documenti.size();i++){
				DocumentoModel doc = (DocumentoModel)documenti.get(i);
				if(doc.getIsSelected().booleanValue()){
					unDocSelezionato = doc;
					break;
				}
			}
			if(unDocSelezionato == null)
				return dac;
				
			if(dac.isFaseDiSpunta()){
				ControlliDac controlli = (ControlliDac)new ControlliDacImpl();
				if(!controlli.controllaPlicoInPinzatura(csc, dac, unDocSelezionato))
					return dac;					
			}
			
			DacManager manager = (DacManager)ROF.getManager(csc,DacManager.class);
			dac = manager.pinzaDocumenti(csc, dac);
			DacLoader.reloadDac(csc,null,dac,dac);
			if(!dac.getDocumento().getIdDocumento().isNull()){
				DAOObject dao = new DAOObject(csc,DAO_DAC_XML_NAME);
				dao.executeTableLoadAccess("documento",dac.getDocumento());
			}
			return dac;
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO in pinzaDocumenti: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in pinzaDocumenti: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacModel spinzaDocumenti(ClientSessionContext csc, DacModel dac) throws EJBException {
		try{
			DacManager manager = (DacManager)ROF.getManager(csc,DacManager.class);
			dac = manager.spinzaDocumenti(csc, dac);
			DacLoader.reloadDac(csc,null,dac,dac);
			if(!dac.getDocumento().getIdDocumento().isNull()){
				DAOObject dao = new DAOObject(csc,DAO_DAC_XML_NAME);
				dao.executeTableLoadAccess("documento",dac.getDocumento());
			}
			return dac;
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO in spinzaDocumenti: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in spinzaDocumenti: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel fillCodDesc(ClientSessionContext csc, CommandDataModel model, boolean includeInnerModels) throws EJBException {
		try{
			
			DAOObject dao = new DAOObject(csc,DAO_DAC_XML_NAME);
			dao.fillCodDesc(model,includeInnerModels);
			return model;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in fillCodDesc: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public AbstractRicercaDacModel ricercaDac(ClientSessionContext csc, AbstractRicercaDacModel ricercaModel) throws EJBException{

		if(ricercaModel.getDaoAccessName().isNull())
			throw new EJBException("Specificare il nome dell'accesso DAO");
		
		ricercaModel.getParametri().setIsOfflineEnvironment(new BooleanType(Configuration.getInstance().isOfflineEnvironment()));
		ricercaModel.setElencoDac(new ListType(DacModel.class));
		
		DAOObject dao = null;
		try{
			
			dao = new DAOObject(csc,DAO_RICERCHE_XML_NAME);
			if(csc.isAssistenteFB()){
				ClientSessionContext cscAss = (ClientSessionContext)Tools.cloneObject(csc); 
				cscAss.setUserCode(csc.getCurrentLinkedUserCode().toString());
				dao = new DAOObject(cscAss,DAO_RICERCHE_XML_NAME);
			}
			
			if(!ricercaModel.getParametri().getIdDac().isNull()){ // Ricerca per chiave

				// Cerco su ASE se la ricerca non è storica
				if(ricercaModel.getDaoAccessName().equals("ricercaStoricoDacSede") ||
				   ricercaModel.getDaoAccessName().equals("ricercaStoricoDacFb")){
					ricercaModel.setTipoRicerca(new StringType("storica"));
					ricercaModel.addParam(ParamsModel.storicizzato);
					dao.openConnection("IQ_PRIT");
					ricercaModel.setElencoDac(dao.executeQueryAccess(ricercaModel.getDaoAccessName().toString(),ricercaModel).getResult());
				}else{				
					String savTipoRicerca = ricercaModel.getTipoRicerca().toString();
					ricercaModel.setTipoRicerca(new StringType());
					ricercaModel.removeParam(ParamsModel.storicizzato);
					dao.openConnection();
					ricercaModel.setElencoDac(dao.executeQueryAccess(ricercaModel.getDaoAccessName().toString(),ricercaModel).getResult());
					// Se non trovato vado su IQ ma solo se non siamo in spunta
					if( Configuration.getInstance().isOnlineEnvironment() && 
					    ricercaModel.getElencoDac().size() == 0 &&
					   !ricercaModel.getFnc().equals(Costanti.FNC_SPUNTA) && 
					    ricercaModel.getDaoAccessName().equals("ricercaDacSede")){
						ricercaModel.setTipoRicerca(new StringType("storica"));
						ricercaModel.addParam(ParamsModel.storicizzato);
						dao.closeConnection();
						dao.openConnection("IQ_PRIT");
						ricercaModel.setElencoDac(dao.executeQueryAccess(ricercaModel.getDaoAccessName().toString(),ricercaModel).getResult());
					}
					ricercaModel.setTipoRicerca(new StringType(savTipoRicerca));
				}
				return ricercaModel;
				
			}
			
			StringType savUtenteMitt = ricercaModel.getParametri().getCodUtenteMittente();
			if(!savUtenteMitt.isNull())
				ricercaModel.getParametri().setCodUtenteMittente(new StringType(Tools.fillSx(savUtenteMitt.toString(),'0',10)));
			StringType savUtenteLav = ricercaModel.getParametri().getCodUtenteLavorazione();
			if(!savUtenteLav.isNull())
				ricercaModel.getParametri().setCodUtenteLavorazione(new StringType(Tools.fillSx(savUtenteLav.toString(),'0',10)));
			StringType savUtenteSpunta = ricercaModel.getParametri().getCodUtenteSpunta();
			if(!savUtenteSpunta.isNull())
				ricercaModel.getParametri().setCodUtenteSpunta(new StringType(Tools.fillSx(savUtenteSpunta.toString(),'0',10)));
			StringType savCodMediolanum = ricercaModel.getParametri().getCodMediolanum();
			if(!savCodMediolanum.isNull())
				ricercaModel.getParametri().setCodMediolanum(new StringType(Tools.fillSx(savCodMediolanum.toString(),'0',11)));
			StringType savCodiceAgente = ricercaModel.getParametri().getCodiceAgente();
			if(!savCodiceAgente.isNull())
				ricercaModel.getParametri().setCodiceAgente(new StringType(Tools.fillSx(savCodiceAgente.toString(),'0',10)));
			
			// Ricerca per parametri
			if(Configuration.getInstance().isOnlineEnvironment() && !ricercaModel.getTipoRicerca().isNull()){ // Se ricerca storica
				ricercaModel.addParam(ParamsModel.storicizzato);
				dao.openConnection("IQ_PRIT");
			}else{
				ricercaModel.removeParam(ParamsModel.storicizzato);
				dao.openConnection();
			}
			ricercaModel.setElencoDac(dao.executeQueryAccess(ricercaModel.getDaoAccessName().toString(),ricercaModel).getResult());
			
			ricercaModel.getParametri().setCodUtenteMittente(savUtenteMitt);
			ricercaModel.getParametri().setCodUtenteLavorazione(savUtenteLav);
			ricercaModel.getParametri().setCodUtenteSpunta(savUtenteSpunta);
			ricercaModel.getParametri().setCodMediolanum(savCodMediolanum);
			ricercaModel.getParametri().setCodiceAgente(savCodiceAgente);
			return ricercaModel;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO in ricercaDac: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in ricercaDac: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}finally{
			if(dao != null) dao.closeConnection();
		}		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public AbstractRicercaDocModel ricercaDoc(ClientSessionContext csc, AbstractRicercaDocModel ricercaModel) throws EJBException{

		if(ricercaModel.getDaoAccessName().isNull())
			throw new EJBException("Specificare il nome dell'accesso DAO");
		
		ricercaModel.getParametri().setIsOfflineEnvironment(new BooleanType(Configuration.getInstance().isOfflineEnvironment()));
		ricercaModel.setDocumenti(new ListType(DocumentoModel.class));
		
		DAOObject dao = null;
		try{
			
			dao = new DAOObject(csc,DAO_RICERCHE_XML_NAME);
			
			if(!ricercaModel.getParametri().getBarcode().isNull()){ // Ricerca per chiave
				
				// Cerco su ASE se la ricerca non è storica
				if(ricercaModel.getDaoAccessName().equals("ricercaStoricoDoc")){
					ricercaModel.setTipoRicerca(new StringType("storica"));
					ricercaModel.addParam(ParamsModel.storicizzato);
					dao.openConnection("IQ_PRIT");
					ricercaModel.setDocumenti(dao.executeQueryAccess(ricercaModel.getDaoAccessName().toString(),ricercaModel).getResult());
				}else{
					String savTipoRicerca = ricercaModel.getTipoRicerca().toString();
					ricercaModel.setTipoRicerca(new StringType(""));
					ricercaModel.removeParam(ParamsModel.storicizzato);
					dao.openConnection();
					ricercaModel.setDocumenti(dao.executeQueryAccess(ricercaModel.getDaoAccessName().toString(),ricercaModel).getResult());
					// Se non trovato vado su IQ
					if(Configuration.getInstance().isOnlineEnvironment() &&
					   ricercaModel.getDocumenti().size() == 0 &&
					   ricercaModel.getDaoAccessName().equals("ricercaDoc")){
						ricercaModel.setTipoRicerca(new StringType("storica"));
						ricercaModel.addParam(ParamsModel.storicizzato);
						dao.closeConnection();
						dao.openConnection("IQ_PRIT");
						ricercaModel.setDocumenti(dao.executeQueryAccess(ricercaModel.getDaoAccessName().toString(),ricercaModel).getResult());
					}
					ricercaModel.setTipoRicerca(new StringType(savTipoRicerca));
				}
				return ricercaModel;
				
			}
			
			StringType savCodMediolanum = ricercaModel.getParametri().getCodMediolanum();
			if(!savCodMediolanum.isNull())
				ricercaModel.getParametri().setCodMediolanum(new StringType(Tools.fillSx(savCodMediolanum.toString(),'0',11)));
			StringType savCodiceAgente = ricercaModel.getParametri().getCodiceAgente();
			if(!savCodiceAgente.isNull())
				ricercaModel.getParametri().setCodiceAgente(new StringType(Tools.fillSx(savCodiceAgente.toString(),'0',10)));
			
			// Ricerca per parametri
			if(Configuration.getInstance().isOnlineEnvironment() && !ricercaModel.getTipoRicerca().isNull()){ // Se ricerca storica
				ricercaModel.addParam(ParamsModel.storicizzato);
				dao.openConnection("IQ_PRIT");
			}else{
				ricercaModel.removeParam(ParamsModel.storicizzato);
				dao.openConnection();
			}
			ricercaModel.setDocumenti(dao.executeQueryAccess(ricercaModel.getDaoAccessName().toString(),ricercaModel).getResult());
			
			ricercaModel.getParametri().setCodiceAgente(savCodiceAgente);
			ricercaModel.getParametri().setCodMediolanum(savCodMediolanum);
			return ricercaModel;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO in ricercaDoc: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in ricercaDoc: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}finally{
			if(dao != null) dao.closeConnection();
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public FirmeModel loadFirmeCliente(ClientSessionContext csc, FirmeModel firmeCliente) throws EJBException{
		if(firmeCliente.isServizioFirmePerContoCorrente())
			loadFirmeClientePerConto(csc, firmeCliente);
		else
			loadFirmeClientePerCodice(csc, firmeCliente);
		return firmeCliente;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void loadFirmeClientePerCodice(ClientSessionContext csc, FirmeModel firmeCliente) throws EJBException{
		try{

			StringType srvCode = null;
			StringType srvMessage = null;
			
			StringType numeroContoSelezionato = firmeCliente.getContoSelezionato().getNumeroConto();

			ContoCorrenteModel conto = new ContoCorrenteModel();
			conto.setNumeroConto(new StringType("0000000000"));
			conto.setCodMediolanum(firmeCliente.getCodMediolanum());
			firmeCliente.getElencoConti().clear();
			firmeCliente.getElencoConti().add(conto);
			
			if(numeroContoSelezionato.isNull()){
				
				if(!firmeCliente.isFaseDiSpunta() || firmeCliente.getIsReadonly().booleanValue())
					return;
				
			}
			
			try{
				DAOObject daoFirme = new DAOObject(csc,DAO_FIRME_XML_NAME);
				DAOQASResultModel qasRes = daoFirme.executeQASAccess("firmeCliente",conto);
				if(qasRes.getQasCallData().getStatus() != QASCallData.STATUS_OK){
					srvCode = new StringType(""+qasRes.getQasCallData().getStatus());
					srvMessage = new StringType(qasRes.getQasCallData().getMessage());
					conto.getNumeroConto().addTypeError("errore");
				}else{
					srvCode = conto.getSrvCode();
					srvMessage = conto.getSrvMessage();
				}
				
			}catch(DAOException daoe){
				LOG.error(daoe);
				conto.getNumeroConto().addTypeError("errore");
			}catch(Exception e){
				LOG.error(e);
				conto.getNumeroConto().addTypeError("errore");
			}
			
			firmeCliente.setContoSelezionato(conto);
			
			if(firmeCliente.getContoSelezionato().getNumeroConto().hasTypeErrors()){
				firmeCliente.getContoSelezionato().getNumeroConto().addTypeError("errore");
				firmeCliente.getContoSelezionato().setSrvCode(srvCode);
				firmeCliente.getContoSelezionato().setSrvMessage(srvMessage);
			}else{
				ListType firmeSortate = new ListType(FirmaModel.class);
				ListType firme = firmeCliente.getContoSelezionato().getFirmeDelConto();
				for(int i=0;i<firme.size();i++){
					FirmaModel f = (FirmaModel)firme.get(i);
					if(!f.getUrlFirma().isNull())
						firmeSortate.add(f);
				}
				for(int i=0;i<firme.size();i++){
					FirmaModel f = (FirmaModel)firme.get(i);
					if(f.getUrlFirma().isNull())
						firmeSortate.add(f);
				}
				firmeCliente.getContoSelezionato().setFirmeDelConto(firmeSortate);
			}
		
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in loadFirmeCliente: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}	
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void loadFirmeClientePerConto(ClientSessionContext csc, FirmeModel firmeCliente) throws EJBException{
		try{

			StringType srvCode = null;
			StringType srvMessage = null;
			
			StringType numeroContoSelezionato = firmeCliente.getContoSelezionato().getNumeroConto();
			
			firmeCliente.setContoSelezionato(new ContoCorrenteModel());
			
			DAOObject daoFirme = new DAOObject(csc,DAO_FIRME_XML_NAME);
			firmeCliente.setElencoConti(daoFirme.executeQueryAccess("loadContiCorrentiCliente",firmeCliente).getResult());

			// Se il cliente non ha conti non chiamo quarc
			if(firmeCliente.getElencoConti().size() == 0)
				return;
				
			// Chiamo qarc con un conto alla volta. Appena ho un risultato valido --> ok, finito
			// Se ho selezionato un conto preciso, chiamo qarc solo per quello
			ListType elencoConti = firmeCliente.getElencoConti();
			if(!numeroContoSelezionato.isNull()){
				
				for(int i=0;i<elencoConti.size();i++){
					ContoCorrenteModel conto = (ContoCorrenteModel)elencoConti.get(i);
					conto.setFirmeDelConto(new ListType(FirmaModel.class));
					if(!numeroContoSelezionato.equals(conto.getNumeroConto()))
						continue;
					
					try{
						DAOQASResultModel qasRes = daoFirme.executeQASAccess("firmeContoCorrente",conto);
						if(qasRes.getQasCallData().getStatus() != QASCallData.STATUS_OK){
							srvCode = new StringType(""+qasRes.getQasCallData().getStatus());
							srvMessage = new StringType(qasRes.getQasCallData().getMessage());
							firmeCliente.getContoSelezionato().getNumeroConto().addTypeError("errore");
						}else{
							srvCode = conto.getSrvCode();
							srvMessage = conto.getSrvMessage();
						}
						firmeCliente.setContoSelezionato(conto);
					}catch(DAOException daoe){
						LOG.error(daoe);
						firmeCliente.getContoSelezionato().getNumeroConto().addTypeError("errore");
					}catch(Exception e){
						LOG.error(e);
						firmeCliente.getContoSelezionato().getNumeroConto().addTypeError("errore");
					}
					break;
				}
				
			}else{
				
				if(!firmeCliente.isFaseDiSpunta() || firmeCliente.getIsReadonly().booleanValue())
					return;
				
				boolean errore = false;
				for(int i=0;i<elencoConti.size();i++){
					ContoCorrenteModel conto = (ContoCorrenteModel)elencoConti.get(i);
					conto.setFirmeDelConto(new ListType(FirmaModel.class));
					try{
						errore = false;
						DAOQASResultModel qasRes = daoFirme.executeQASAccess("firmeContoCorrente",conto);
						if(qasRes.getQasCallData().getStatus() != QASCallData.STATUS_OK){
							srvCode = new StringType(""+qasRes.getQasCallData().getStatus());
							srvMessage = new StringType(qasRes.getQasCallData().getMessage());
							errore = true;
						}else{
							srvCode = conto.getSrvCode();
							srvMessage = conto.getSrvMessage();
						}
					}catch(DAOException daoe){
						LOG.error(daoe);
						errore = true;
						continue;
					}catch(Exception e){
						LOG.error(e);
						errore = true;
						continue;
					}
					if(conto.getFirmeDelConto().size() > 0){
						firmeCliente.setContoSelezionato(conto);
						errore = false;
						break;
					}
				}
				if(errore)
					firmeCliente.getContoSelezionato().getNumeroConto().addTypeError("errore");
			}
			
			if(firmeCliente.getContoSelezionato().getNumeroConto().hasTypeErrors() ||
			   firmeCliente.getContoSelezionato().getFirmeDelConto().size() == 0){
				firmeCliente.getContoSelezionato().getNumeroConto().addTypeError("errore");
				firmeCliente.getContoSelezionato().setSrvCode(srvCode);
				firmeCliente.getContoSelezionato().setSrvMessage(srvMessage);
			}else{
				ListType firmeSortate = new ListType(FirmaModel.class);
				ListType firme = firmeCliente.getContoSelezionato().getFirmeDelConto();
				for(int i=0;i<firme.size();i++){
					FirmaModel f = (FirmaModel)firme.get(i);
					if(!f.getUrlFirma().isNull())
						firmeSortate.add(f);
				}
				for(int i=0;i<firme.size();i++){
					FirmaModel f = (FirmaModel)firme.get(i);
					if(f.getUrlFirma().isNull())
						firmeSortate.add(f);
				}
				firmeCliente.getContoSelezionato().setFirmeDelConto(firmeSortate);
			}
			return;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO in loadFirmeCliente: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in loadFirmeCliente: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}				
	}
}

