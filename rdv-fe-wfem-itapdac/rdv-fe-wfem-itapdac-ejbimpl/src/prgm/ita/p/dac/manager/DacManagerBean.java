package prgm.ita.p.dac.manager;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.atosorigin.wfem.backend.ManagerObject;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.controller.Configuration;
import com.atosorigin.wfem.dao.DAOCallableResultModel;
import com.atosorigin.wfem.dao.DAOOSBResultModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOTableResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.dao.exceptions.NoRowsAffected;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;
import com.atosorigin.wfem.util.OSBCallData;
import com.atosorigin.wfem.util.Tools;

import prgm.ita.p.dac.facade.Costanti;
import prgm.ita.p.dac.facade.DacLoader;
import prgm.ita.p.dac.model.AgenteModel;
import prgm.ita.p.dac.model.ContatoreModel;
import prgm.ita.p.dac.model.DacKeyModel;
import prgm.ita.p.dac.model.DacModel;
import prgm.ita.p.dac.model.DocumentoModel;
import prgm.ita.p.dac.model.MezzoPagamentoModel;
import prgm.ita.p.dac.model.PlicoModel;
import prgm.ita.p.dac.util.DocumentoAssegnoTools;
import prgm.ita.p.dac.util.MOMCallIndicator;

@Stateless(name = "DacManager", mappedName = "DacManager")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
/***********************************************************************************************/
/***********************************************************************************************/
public class DacManagerBean extends ManagerObject implements DacManager{

	private static final String DAO_XML_NAME_DAC = "ItaPDac.Dac";
	private static final String DAO_XML_NAME_SEDE = "ItaPDac.DacSede";
	
	private static final String DAO_ACCESS_AGGIORNA_PLICO_DOCUMENTO = "aggiornaPlicoDocumento";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private DacModel creaDac(ClientSessionContext csc, DAOObject dao, DacModel dac) throws Exception {
		try{

			StringType idDac = getContatore(csc,dao,Costanti.GET_PROGRESSIVI_RISORSA_DAC);
			if (dac.getTipoDac().equals(Costanti.TIPO_DAC_SEDE))
				idDac = new StringType("D" + idDac.toString().substring(1));
			dac.setIdDac(idDac);

			dac.setUbicazione(dac.getUffMittente());
			
			dac.setStato(new IntegerType(Costanti.STATO_INCORSO));
			dac.setAzione(new IntegerType(Costanti.AZIONE_CREA));
			dac.setCodTipoSpedizione(new IntegerType(Costanti.MEZZO_SPEDIZIONE_CORRIERE));

			StringType utente = new StringType(Tools.fillSx(csc.getUserCode(),'0',10)); 
			TimestampType ora = Tools.now();
			
			dac.setDataOraCambioStato(ora);
			
			if(csc.isAssistenteFB())
				dac.setCodUtenteMittente(new StringType(csc.getCurrentLinkedUserCode()));
			else
				dac.setCodUtenteMittente(utente);
			dac.setCodUtenteIns(utente);
			dac.setCodUtenteUpd(utente);
			dac.setDataOraIns(ora);
			dac.setDataOraUpd(ora);
			
			dao.executeTableInsertAccess("dac",dac);
			if(!dac.getAgenteRiferimento().getCodAgente().isNull())
				dao.executeTableInsertAccess("dacRete",dac);
			
			if(!dac.getUfficio().equals(Costanti.UFFICIO_RETE)) {
				try{
					dao.executeTableUpdateAccess("dacSede",dac);					
				}catch(NoRowsAffected nra){
					dao.executeTableInsertAccess("dacSede",dac);					
				}
			}
			return dac;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO in creaDac: "+daoe;
			Exception e = new Exception(errorMsg);
			LOG.error(e);
			throw e;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in creaDac: "+e;
			e = new Exception(errorMsg);
			LOG.error(e);
			throw e;
		}		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacModel salvaDac(ClientSessionContext csc, DacModel dac) throws EJBException {
		DAOObject dao = null;
		try{
			
			dac.resetCommandErrors();
			
			dao = new DAOObject(csc,DAO_XML_NAME_DAC);
			
			if(dac.getIdDac().isNull())
				return creaDac(csc,dao,dac);
			
			StringType utente = new StringType(Tools.fillSx(csc.getUserCode(),'0',10)); 
			TimestampType ora = Tools.now();
			
			dac.setCodUtenteUpd(utente);
			dac.setDataOraUpd(ora);
			
			dao.executeTableUpdateAccess("updateDac",dac);
			if(!dac.getAgenteRiferimento().getCodAgente().isNull()){
				try{
					dao.executeTableUpdateAccess("updateDacRete",dac);
				}catch(NoRowsAffected nra){
					dao.executeTableInsertAccess("updateDacRete",dac);					
				}
			}
			
			if(!dac.getUfficio().equals(Costanti.UFFICIO_RETE)){
				try{
					dao.executeTableUpdateAccess("dacSede",dac);
				}catch(NoRowsAffected nra){
					dao.executeTableInsertAccess("dacSede",dac);					
				}
			}
			return dac;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO in salvaDac: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in salvaDac: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacModel inviaDac(ClientSessionContext csc, DacModel dac) throws EJBException {

		try{
			
			boolean saveDB = true;
			
			dac.resetCommandErrors();

			StringType utente = new StringType(Tools.fillSx(csc.getUserCode(),'0',10)); 
			TimestampType ora = Tools.now();
			StringType flagReplica = new StringType("S");
			if(Configuration.getInstance().isOfflineEnvironment())
				flagReplica = new StringType("D");

			DAOObject dao = new DAOObject(csc,DAO_XML_NAME_DAC);
			
			if(dac.getIsAutoSpuntata().booleanValue()){			// La Dac nasce spuntata
				dac.setUbicazione(dac.getUfficio());
				dac.setUffLavorazione(dac.getUfficio());
				dac.setCodUtenteLavorazione(utente);
				dac.setUffSpunta(dac.getUfficio());
				dac.setCodUtenteSpunta(utente);
				dac.setEsito(new IntegerType(Costanti.ESITO_DAC_TUTTI_ACCETTATI));
				dac.setStato(new IntegerType(Costanti.STATO_LAVORATA));
				dac.setAzione(new IntegerType(Costanti.AZIONE_CHIUDI));
				dac.setDataOraSpuntaDb(ora);
				if(dac.getDataRicezioneDocumenti().isNull())
					dac.setDataRicezioneDocumenti(Tools.today());
			}else{												// Dac da spuntare
				dac.setUbicazione(new IntegerType(Costanti.UFFICIO_CODING_SPUNTA));
				dac.setStato(new IntegerType(Costanti.STATO_SPEDITA));
				dac.setAzione(new IntegerType(Costanti.AZIONE_EMETTI));
			}

			dac.setDataOraEmissione(ora);
			dac.setDataOraCambioStato(ora);
			
			dac.setCodUtenteMittente(utente);
			dac.setCodUtenteUpd(utente);
			dac.setDataOraUpd(ora);
			dac.setFlagReplica(flagReplica);
			
			if(saveDB){
				dao.executeTableUpdateAccess("dac",dac);
				if(!dac.getAgenteRiferimento().getCodAgente().isNull()){
					try{
						dao.executeTableUpdateAccess("dacRete",dac);
					}catch(NoRowsAffected nra){
						dao.executeTableInsertAccess("dacRete",dac);					
					}
				}
			
				if(!dac.getUfficio().equals(Costanti.UFFICIO_RETE)){
					try{
						dao.executeTableUpdateAccess("dacSede",dac);
					}catch(NoRowsAffected nra){
						dao.executeTableInsertAccess("dacSede",dac);					
					}
				}
			}

			for(int i=0;i<dac.getDocumenti().size();i++){
				DocumentoModel doc = (DocumentoModel)dac.getDocumenti().get(i);
				dao.executeTableLoadAccess("documento",doc);
				
				// Agevolazioni
				String codInforeteEsterno = doc.getCodInforeteEsterno().toString();
				if(codInforeteEsterno.length() == 20 && codInforeteEsterno.startsWith("A"))
					dao.executeQueryAccess("loadAgevolazioneData", doc);
				
				// Sede
				if(!dac.getUfficio().equals(Costanti.UFFICIO_RETE))
					dao.executeTableLoadAccess("documentoSede",doc);					
				
				doc.getMezziPagamento().clear();
				ListType mezziPg = dao.executeTableLoadChildsAccess("mezzoDiPagamento",doc,MezzoPagamentoModel.class).getChilds();
				for(int j=0;j<mezziPg.size();j++){
					MezzoPagamentoModel mezzoPg = (MezzoPagamentoModel)mezziPg.get(j);
					doc.getMezziPagamento().add(mezzoPg);
					mezzoPg.setCodUtenteUpd(utente);
					mezzoPg.setDataOraUpd(ora);
					mezzoPg.setFlagReplica(flagReplica);
					if(saveDB)
						dao.executeTableUpdateAccess("mezzoDiPagamento",mezzoPg);
				}
				
				doc.setUbicazione(dac.getUbicazione());				
				doc.setStato(dac.getStato());
				doc.setAzione(dac.getAzione());
				
				doc.setDataOraCambioStato(ora);
				doc.setCodUtenteUpd(utente);
				doc.setDataOraUpd(ora);
				doc.setFlagReplica(dac.getFlagReplica());

				// Rimuovo evenuali aggragatori di documenti che non hanno compagni nel plico
				if(!doc.getCodAggregatore().isNull()){
					int numDocInPLico = 0;
					for(int j=0;j<dac.getDocumenti().size();j++){
						DocumentoModel otherDoc = (DocumentoModel)dac.getDocumenti().get(j);
						if(doc.getCodAggregatore().equals(otherDoc.getCodAggregatore()))
							numDocInPLico++;
					}
					if(numDocInPLico < 2){
						doc.setCodAggregatore(new StringType());
						doc.setCodAggregatoreOriginale(new StringType());
						doc.setContestoAggregatore(new StringType());
						doc.setContestoAggregatoreOriginale(new StringType());
					}
				}
				
				// Update Documento
				if(saveDB){
					dao.executeTableUpdateAccess("documento",doc);
					if(!dac.getUfficio().equals(Costanti.UFFICIO_RETE)){
						try{
							dao.executeTableUpdateAccess("documentoSede",doc);
						}catch(NoRowsAffected nra){
							dao.executeTableInsertAccess("documentoSede",doc);						
						}					
					}
					
					// Update legame testata
					dao.executeTableUpdateAccess("legameDocumento",doc);
					if(!dac.getUfficio().equals(Costanti.UFFICIO_RETE)){
						try{
							dao.executeTableUpdateAccess("legameDocumentoSede",doc);
						}catch(NoRowsAffected nra){
							dao.executeTableInsertAccess("legameDocumentoSede",doc);						
						}
					}
				}
			}
			
			// Per ora lasciamo tutto com'è aggiungiamo solo la chiamata all'inserimento su MOM
			// Se ci sono errori non fa nulla
			dac.setShowAlert(false);
			dac.setMsg("");
			
			// Imposto i dati che per MOM differiscono da quelli del prit
			try{
				dac.setIndirizzoAgenziaMOM(new StringType());
				String indirizzoAgenziaMOM = dac.getAgenteRiferimento().getIndirizzoAgenzia().toString();
				if(indirizzoAgenziaMOM.length() > 50)
					indirizzoAgenziaMOM = indirizzoAgenziaMOM.substring(0,50);
				dac.setIndirizzoAgenziaMOM(new StringType(indirizzoAgenziaMOM));
			}catch(Throwable t){}
			
			// Chiamo MOM per l'invio
			if(!MOMCallIndicator.callMOM){
				try{
					if(csc.isAssistenteFB())
						dac.setUserMOM(new StringType(Tools.fillSx(csc.getCurrentLinkedUserCode(),'0',10)));
					else
						dac.setUserMOM(new StringType(Tools.fillSx(csc.getUserCode(),'0',10)));
					new DAOObject(csc,Costanti.DAO_XML_NAME_MOM).executeOSBAccess("inviaPritMOM",dac);
				}catch(Throwable t){}
			}else{
				try{
					boolean tuttoOk = false;
					if(csc.isAssistenteFB())
						dac.setUserMOM(new StringType(Tools.fillSx(csc.getCurrentLinkedUserCode(),'0',10)));
					else
						dac.setUserMOM(new StringType(Tools.fillSx(csc.getUserCode(),'0',10)));
					DAOOSBResultModel osbRes = new DAOObject(csc,Costanti.DAO_XML_NAME_MOM).executeOSBAccess("inviaPritMOM",dac);
					if(osbRes.getWsCallData().getStatus() != OSBCallData.STATUS_OK){
						if(getSessionContext() != null) 
							getSessionContext().setRollbackOnly();
						dac.setShowAlert(true);
						dac.setMsg("Attenzione!\\nErrore di comunicazione nell'invio del Prit\\n\\n"+
													"Status: ["+osbRes.getWsCallData().getStatus()+"]\\n"+
													"Http status: ["+osbRes.getWsCallData().getHttpStatus()+"]\\n"+
													"Message: ["+osbRes.getWsCallData().getMessage().replace('"','\'').replace('\n',' ')+"]");
					}else{
						DacKeyModel dacResult = (DacKeyModel)osbRes.getResult();
						if(dacResult.getIdDac().isNull()){
							if(getSessionContext() != null) 
								getSessionContext().setRollbackOnly();
							dac.setShowAlert(true);
							dac.setMsg("Attenzione!\\nErrore di servizio nell'invio del Prit");
						}else{
							// Tutto ok. Invio a MOM effettuato
							tuttoOk = true;
						}
					}
					if(tuttoOk){
						; // per ora non cancelliamo la dac
						//eliminaDac(dao, dac); per ora non eliminiamo i prit
					}
				}catch(Throwable t){
					if(getSessionContext() != null) 
						getSessionContext().setRollbackOnly();
					dac.setShowAlert(true);
					dac.setMsg("Attenzione!\\nErrore di sistema nell'invio del Prit\\n\\n["+t.toString().substring(0,500).replace('"','\'').replace('\n',' ')+"]");
				}
			}
			return dac;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO in inviaDac: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in inviaDac: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean eliminaDac(DAOObject dao, DacModel dac){
	
		try{
			ListType documenti = dac.getDocumenti();
			for(int i=0;i<documenti.size();i++){
				DocumentoModel doc = (DocumentoModel)documenti.get(i);
				doc.setIdDac(new StringType(dac.getIdDac().toString()));
				
				// Mezzi di pagamento
				try{dao.executeTableDeleteChildsAccess("mezzoDiPagamento",doc);}catch(NoRowsAffected nra){}
		
				// Legame con la testata
				try{dao.executeTableDeleteAccess("legameDocumento",doc);}catch(NoRowsAffected nra){}
				try{dao.executeTableDeleteAccess("legameDocumentoSede",doc);}catch(NoRowsAffected nra){}
				
				// Documento
				try{dao.executeTableDeleteAccess("documento",doc);}catch(NoRowsAffected nra){}
				try{dao.executeTableDeleteAccess("documentoSede",doc);}catch(NoRowsAffected nra){}
			}
			try{dao.executeTableDeleteAccess("dac",dac);}catch(NoRowsAffected nra){}
			try{dao.executeTableDeleteAccess("dacSede",dac);}catch(NoRowsAffected nra){}
			return true;
		}catch(DAOException daoe){
			LOG.error(daoe);
			return false;
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacModel lavoraDac(ClientSessionContext csc, DacModel dac) throws EJBException{

		try{
			
			boolean dacSenzaDocumenti = false;
			if(dac.getStato().equals(Costanti.STATO_LAVORATA) && dac.getEsito().equals(Costanti.ESITO_DAC_TUTTI_MANCANTI))
				dacSenzaDocumenti = true;
			
			DAOObject dao = new DAOObject(csc,DAO_XML_NAME_DAC);
			
			StringType utente = new StringType(Tools.fillSx(csc.getUserCode(),'0',10)); 
			TimestampType ora = Tools.now();

			//Aggiorno la DAC
			dac.setStato(new IntegerType(Costanti.STATO_APERTA));
			dac.setAzione(new IntegerType(Costanti.AZIONE_APRI));
			dac.setEsito(new IntegerType());
			
			dac.setUbicazione(dac.getUfficio());
			
			dac.setUffLavorazione(dac.getUfficio());
			dac.setCodUtenteLavorazione(utente);
			dac.setDataOraLavorazione(ora);
			
			dac.setCodUtenteUpd(utente);
			dac.setDataOraUpd(ora);
			dac.setDataOraCambioStato(ora);

			dac.resetCommandErrors();
			try{
				if(dacSenzaDocumenti)
					dao.executeTableUpdateAccess("lavoraDacSenzaDocumenti",dac);
				else
					dao.executeTableUpdateAccess("lavoraDac",dac);
			}catch(NoRowsAffected nra){
				// Concorrenza sulla lavorazione della dac
				dao.executeTableLoadAccess("dac",dac);
				dac.addCommandError("Attenzione. Il Prit è stato messa in lavorazione da "+dac.getCodUtenteLavorazione());
				return dac;
			}
			try{
				dao.executeTableUpdateAccess("lavoraDacSede",dac);
			}catch(NoRowsAffected nra){
				dao.executeTableInsertAccess("lavoraDacSede",dac);				
			}

			//Aggiorno i documenti
			dac.setDocumenti(dao.executeQueryAccess("loadDocumentiDacSpunta",dac).getResult());
			for (int i=0; i<dac.getDocumenti().size(); i++) {
				DocumentoModel doc = (DocumentoModel)dac.getDocumenti().get(i);
				dao.executeTableLoadAccess("documento",doc);

				doc.setStato(dac.getStato());
				doc.setAzione(dac.getAzione());
				doc.setEsito(new IntegerType());
				doc.setUbicazione(dac.getUbicazione());
				doc.setCodUtenteUpd(utente);
				doc.setDataOraUpd(ora);
				dao.executeTableUpdateAccess("lavoraDocumento",doc);
				
				// Salvo i dati in una copia (Se non sono già stati salvati)
				DAOTableResultModel tRes = dao.executeTableLoadAccess("documentoCpyRete",doc);
				if(tRes.getResult().intValue() == 0)
					dao.executeTableInsertAccess("documentoCpyRete",doc);
				doc.setMezziPagamento(dao.executeTableLoadChildsAccess("mezzoDiPagamento", doc, MezzoPagamentoModel.class).getChilds());
				ListType mezziPg = doc.getMezziPagamento();
				for(int j=0;j<mezziPg.size();j++){
					MezzoPagamentoModel mezzo = (MezzoPagamentoModel)mezziPg.get(j);
					tRes = dao.executeTableLoadAccess("mezzoDiPagamentoCpyRete",mezzo);
					if(tRes.getResult().intValue() == 0)
						dao.executeTableInsertAccess("mezzoDiPagamentoCpyRete",mezzo);
				}
			}
			return dac;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO nel lavorare la DAC: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		} catch(Exception e){
			String errorMsg = getClass()+" Eccezione nel lavorare la DAC: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}				
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacModel spuntaDac(ClientSessionContext csc, DacModel dac) throws EJBException {

		try{
			
			dac.resetCommandErrors();

			StringType utente = new StringType(Tools.fillSx(csc.getUserCode(),'0',10)); 
			TimestampType ora = Tools.now();

			DAOObject dao = new DAOObject(csc,DAO_XML_NAME_DAC);
			
			dac.setUbicazione(dac.getUfficio());
			dac.setUffSpunta(dac.getUfficio());
			dac.setCodUtenteSpunta(utente);

			// Il vecchio prit non valorizzava l'utente che mette in lavorazione la testata
			if(dac.getCodUtenteLavorazione().isNull()){
				dac.setUffLavorazione(dac.getUfficio());
				dac.setCodUtenteLavorazione(utente);
			}
			
			ListType documenti = dao.executeTableLoadChildsAccess("documento",dac,DocumentoModel.class).getChilds();
			
			// Calcolo l'esito della Dac in funzione dei singoli stati dei suoi documenti
			int modificati = 0;
			int aggiunti = 0;
			int senzaDoc = 0;
			for(int i=0;i<documenti.size();i++){
				DocumentoModel doc = (DocumentoModel)documenti.get(i);
				int esito = doc.getEsito().intValue();
				if(esito == Costanti.ESITO_DOC_MODIFICATO)
					modificati++;
				else if(esito == Costanti.ESITO_DOC_AGGIUNTO)
					aggiunti++;
				else if(esito == Costanti.ESITO_DOC_DOCMANCANTE ||
						esito == Costanti.ESITO_DOC_DOCMANCANTESEGN)
					senzaDoc++;
			}
			if(senzaDoc > 0)
				dac.setEsito(new IntegerType(Costanti.ESITO_DAC_MANCANTI));
			else if(modificati > 0 || aggiunti > 0)
				dac.setEsito(new IntegerType(Costanti.ESITO_DAC_MODIFICATI_AGGIUNTI));
			else
				dac.setEsito(new IntegerType(Costanti.ESITO_DAC_TUTTI_ACCETTATI));
			
			dac.setStato(new IntegerType(Costanti.STATO_LAVORATA));
			dac.setAzione(new IntegerType(Costanti.AZIONE_CHIUDI));
			if(dac.getDataRicezioneDocumenti().isNull())
				dac.setDataRicezioneDocumenti(Tools.today());
			
			dac.setDataOraCambioStato(ora);
			dac.setDataOraSpuntaDb(ora);
			
			dac.setCodUtenteUpd(utente);
			dac.setDataOraUpd(ora);
						
			dao.executeTableUpdateAccess("dac",dac);
			try{
				dao.executeTableUpdateAccess("dacSede",dac);
			}catch(NoRowsAffected nra){
				dao.executeTableInsertAccess("dacSede",dac);					
			}

			for(int i=0;i<documenti.size();i++){
				DocumentoModel doc = (DocumentoModel)documenti.get(i);
				dao.executeTableLoadAccess("documento",doc);
				dao.executeTableLoadAccess("documentoSede",doc);
				
				// Aggiorno i mezzi di pagamento
				ListType mezziPg = dao.executeTableLoadChildsAccess("mezzoDiPagamento",doc,MezzoPagamentoModel.class).getChilds();
				for(int j=0;j<mezziPg.size();j++){
					MezzoPagamentoModel mezzoPg = (MezzoPagamentoModel)mezziPg.get(j);
					mezzoPg.setCodUtenteUpd(utente);
					mezzoPg.setDataOraUpd(ora);
					dao.executeTableUpdateAccess("mezzoDiPagamento",mezzoPg);
				}
				
				doc.setStato(dac.getStato());
				doc.setAzione(dac.getAzione());
				
				doc.setUbicazione(dac.getUbicazione());
				doc.setDataOraCambioStato(ora);
				doc.setCodUtenteUpd(utente);
				doc.setDataOraUpd(ora);
				
				// Update Documento
				dao.executeTableUpdateAccess("documento",doc);
				try{
					dao.executeTableUpdateAccess("documentoSede",doc);
				}catch(NoRowsAffected nra){
					dao.executeTableInsertAccess("documentoSede",doc);						
				}					
				
				// Update data/ora upd e utente upd della legame testata
				dao.executeTableUpdateAccess("updateDataOraLegameDocumento",doc);
				try{
					dao.executeTableUpdateAccess("legameDocumentoSede",doc);
				}catch(NoRowsAffected nra){
					dao.executeTableInsertAccess("legameDocumentoSede",doc);						
				}
				
			}
			return dac;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO in inviaDac: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in inviaDac: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public DocumentoModel inserisciDocInDac(ClientSessionContext csc, StringType idDac, StringType idDoc, 
											boolean inRicezione, boolean erratoSmistamento) throws EJBException {
		DAOObject dao = null;
		DAOObject daoSede = null;
		
		try{
			StringType utente = new StringType(Tools.fillSx(csc.getUserCode(),'0',10)); 
			TimestampType ora = Tools.now();

			DocumentoModel doc = new DocumentoModel();
			doc.setIdDocumento(idDoc);
			
			dao = new DAOObject(csc,DAO_XML_NAME_DAC);
			daoSede = new DAOObject(csc,DAO_XML_NAME_SEDE);
			
			dao.executeTableLoadAccess("documento", doc);
			dao.executeTableLoadAccess("documentoSede",doc);					

			if (!erratoSmistamento) {
				//In caso di documento bloccato i dati correnti non devono essere modificati
				doc.setIdDac(idDac);
			}
			doc.setCodUtenteUpd(utente);
			doc.setDataOraUpd(ora);

			//Aggiorno la dac corrente del doc
			daoSede.executeTableUpdateAccess("inserisciDocInDac",doc);

			doc.setIdDac(idDac);
			doc.setNonPervenuto(new BooleanType(false));
			doc.setAggiuntoInRicezione(new BooleanType(inRicezione));
			doc.setIsErroreSmistamento(new BooleanType(erratoSmistamento));

			doc.setFlagReplica(new StringType());
			doc.getAgenteRiferimento().setCodAgente(new StringType(csc.getCurrentLinkedUserCode()));
			doc.getAgenteRiferimento().setServerReplica(new StringType("0"));

			doc.setCodUtenteIns(utente);
			doc.setDataOraIns(ora);

			//Inserisco il doc nella dac
			dao.executeTableInsertAccess("legameDocumento",doc);
			dao.executeTableInsertAccess("legameDocumentoSede",doc);
			return doc;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO in inserisciDocInDac: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in inserisciDocInDac: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void rimuoviDocDaDac(ClientSessionContext csc, StringType idDac, StringType idDoc) throws EJBException {
		DAOObject dao = null;
		DAOObject daoSede = null;
		
		try{
			dao = new DAOObject(csc,DAO_XML_NAME_DAC);
			daoSede = new DAOObject(csc,DAO_XML_NAME_SEDE);

			StringType utente = new StringType(Tools.fillSx(csc.getUserCode(),'0',10)); 
			TimestampType ora = Tools.now();

			DocumentoModel doc = new DocumentoModel();
			doc.setIdDocumento(idDoc);
			doc.setIdDac(idDac);
			
			//Elimino il doc nella dac
			dao.executeTableDeleteAccess("legameDocumento",doc);
			try{
				dao.executeTableDeleteAccess("legameDocumentoSede",doc);
			}catch(NoRowsAffected nra){}
			
			//Aggiorno il doc come doc fuori DAC
			doc.setIdDac(new StringType(Costanti.ID_DAC_X_DOC_FUORI_DAC));
			doc.setUffDestinatario(new IntegerType());
			
			doc.setCodUtenteUpd(utente);
			doc.setDataOraUpd(ora);
			daoSede.executeTableUpdateAccess("rimuoviDocDaDac",doc);
			try{
				daoSede.executeTableUpdateAccess("rimuoviDocDaDacSede",doc);
			}catch(NoRowsAffected nra){
				daoSede.executeTableInsertAccess("rimuoviDocDaDacSede",doc);					
			}

		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO in rimuoviDocDaDac: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in rimuoviDocDaDac: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacModel spedisciDac(ClientSessionContext csc, DacModel dac) throws EJBException {
		DAOObject dao = null;
		DAOObject daoSede = null;
		
		try{
			
			dac.resetCommandErrors();

			StringType utente = new StringType(Tools.fillSx(csc.getUserCode(),'0',10)); 
			TimestampType ora = Tools.now();
			StringType flagReplica = new StringType();

			dao = new DAOObject(csc,DAO_XML_NAME_DAC);
			daoSede = new DAOObject(csc,DAO_XML_NAME_SEDE);
			
			dac.setUbicazione(new IntegerType(Costanti.UBICAZIONE_IN_VIAGGIO));
			dac.setStato(new IntegerType(Costanti.STATO_SPEDITA));
			dac.setAzione(new IntegerType(Costanti.AZIONE_EMETTI));
			
			dac.setDataOraEmissione(ora);
			dac.setDataOraCambioStato(ora);
			
			dac.setCodUtenteUpd(utente);
			dac.setDataOraUpd(ora);
			dac.setFlagReplica(flagReplica);
			
			dao.executeTableUpdateAccess("dac",dac);
			try{
				dao.executeTableUpdateAccess("dacSede",dac);
			}catch(NoRowsAffected nra){
				dao.executeTableInsertAccess("dacSede",dac);					
			}

			for(int i=0;i<dac.getDocumenti().size();i++){
				DocumentoModel doc = (DocumentoModel)dac.getDocumenti().get(i);
				
				doc.setIsInBusta(new BooleanType(true));
				doc.setNonPervenuto(new BooleanType(false));
				doc.setUffDestinatario(dac.getUffDestinatario());
				doc.setReso(dac.getReso());
				doc.setUbicazione(new IntegerType(Costanti.UBICAZIONE_IN_VIAGGIO));
				doc.setCodUtenteUpd(utente);
				doc.setDataOraUpd(ora);
				
				daoSede.executeTableUpdateAccess("spedisciDac",doc);
				try{
					daoSede.executeTableUpdateAccess("spedisciDacSede",doc);
				}catch(NoRowsAffected nra){
					daoSede.executeTableInsertAccess("spedisciDacSede",doc);					
				}
			}
			return dac;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO in spedisciDac: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in spedisciDac: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacModel gestisciDac(ClientSessionContext csc, DacModel dac) throws EJBException{

		try{
			DAOObject daoSede = new DAOObject(csc,DAO_XML_NAME_SEDE);
			
			StringType utente = new StringType(Tools.fillSx(csc.getUserCode(),'0',10)); 
			TimestampType ora = Tools.now();

			//Aggiorno la DAC
			dac.setStato(new IntegerType(Costanti.STATO_APERTA));
			dac.setAzione(new IntegerType(Costanti.AZIONE_APRI));

			dac.setUbicazione(dac.getUfficio());
			dac.setUffLavorazione(dac.getUfficio());
			dac.setCodUtenteLavorazione(utente);
			dac.setDataOraLavorazione(ora);
			
			dac.setDataOraCambioStato(ora);
			dac.setCodUtenteUpd(utente);
			dac.setDataOraUpd(ora);

			daoSede.executeTableUpdateAccess("gestisciDac",dac);
			try{
				daoSede.executeTableUpdateAccess("gestisciDacSede",dac);
			}catch(NoRowsAffected nra){
				daoSede.executeTableInsertAccess("gestisciDacSede",dac);				
			}

			//Pulisco la destinazione dei documenti della DAC
			for (int i=0; i<dac.getDocumenti().size(); i++) {
				DocumentoModel doc = (DocumentoModel)dac.getDocumenti().get(i);

				doc.setUffDestinatario(new IntegerType());
				doc.setCodUtenteUpd(utente);
				doc.setDataOraUpd(ora);
				
				daoSede.executeTableUpdateAccess("pulisciDestinatarioDoc", doc);
				try{
					daoSede.executeTableUpdateAccess("pulisciDestinatarioDocSede", doc);					
				}catch(NoRowsAffected nra){
					daoSede.executeTableInsertAccess("pulisciDestinatarioDocSede", doc);
				}
			}
			return dac;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO nel gestire la DAC: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		} catch(Exception e){
			String errorMsg = getClass()+" Eccezione nel gestire la DAC: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}				
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacModel chiudiDac(ClientSessionContext csc, DacModel dac) throws EJBException{
		DAOObject daoSede = null;

		try{
			if (!dac.isMgmPlichi()) {
				for (int i=0; i<dac.getDocumenti().size(); i++) {
					DocumentoModel doc = (DocumentoModel)dac.getDocumenti().get(i);
					if (!doc.getCodAggregatore().isNull() && doc.getIsFirstInPlico().booleanValue())
						doc.setIsSelected(new BooleanType(true));
				}
				
				spinzaDocumenti(csc, dac);				
			}

			
			daoSede = new DAOObject(csc,DAO_XML_NAME_SEDE);
			
			StringType utente = new StringType(Tools.fillSx(csc.getUserCode(),'0',10)); 
			TimestampType ora = Tools.now();

			//Aggiorno la DAC
			dac.setStato(new IntegerType(Costanti.STATO_LAVORATA));
			dac.setAzione(new IntegerType(Costanti.AZIONE_CHIUDI));
			dac.setCodUtenteLavorazione(utente);
			dac.setCodUtenteUpd(utente);
			dac.setDataOraUpd(ora);
			dac.setDataOraCambioStato(ora);
			daoSede.executeTableUpdateAccess("chiudiDac",dac);
			
			for (int i=0; i<dac.getDocumenti().size(); i++) {
				DocumentoModel doc = (DocumentoModel)dac.getDocumenti().get(i);

				if (doc.getNonPervenuto().booleanValue()) {
					//Aggiorno il flag storico
					daoSede.executeTableUpdateAccess("aggiornaLegamiInChiusuraDacSede",doc);
				}
				
				doc.setIdDac(new StringType(Costanti.ID_DAC_X_DOC_FUORI_DAC));				
				doc.setIsInBusta(new BooleanType(false));				
				doc.setDataOraRicezioneDocumento(ora);					
				doc.setCodUtenteUpd(utente);
				doc.setDataOraUpd(ora);
					
				daoSede.executeTableUpdateAccess("aggiornaDocInChiusuraDac", doc);
				try{
					daoSede.executeTableUpdateAccess("aggiornaDocInChiusuraDacSede", doc);					
				}catch(NoRowsAffected nra){
					daoSede.executeTableInsertAccess("aggiornaDocInChiusuraDacSede", doc);
				}
			}
			
			return dac;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO nel chiudere la DAC: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		} catch(Exception e){
			String errorMsg = getClass()+" Eccezione nel chiudere la DAC: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}				
	}	
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacModel pinzaDocumenti(ClientSessionContext csc, DacModel dac) throws EJBException {
		return pinzaSpinzaDocumenti(csc, dac, true);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacModel spinzaDocumenti(ClientSessionContext csc, DacModel dac) throws EJBException {
		return pinzaSpinzaDocumenti(csc, dac, false);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private DacModel pinzaSpinzaDocumenti(ClientSessionContext csc, DacModel dac, boolean pinza) throws EJBException {
		DAOObject dao = null;
		try{
			
			dao = new DAOObject(csc,DAO_XML_NAME_DAC);
			StringType codAggregatorePinzatura = null;
			ListType documenti = dac.getDocumenti();

			// In pinzatura, se è selezionato un documento di un plico, devo considerare selezionati tutti i suoi documenti
			if(pinza){
				for(int i=0;i<documenti.size();i++){
					DocumentoModel doc = (DocumentoModel)documenti.get(i);
					if(!doc.getIsSelected().booleanValue())
						continue;
					if(doc.getCodAggregatore().isNull())
						continue;
					for(int j=0;j<documenti.size();j++){
						DocumentoModel innerDoc = (DocumentoModel)documenti.get(j);
						if(doc.getCodAggregatore().equals(innerDoc.getCodAggregatore()))
							innerDoc.setIsSelected(new BooleanType(true));
					}					
				}
			}
			
			for(int i=0;i<documenti.size();i++){
				
				DocumentoModel doc = (DocumentoModel)documenti.get(i);
				if(!doc.getIsSelected().booleanValue())
					continue;
				
				doc.setIsSelected(new BooleanType());
				doc.copyParams(dac);
				
				if(pinza){
					
					if(codAggregatorePinzatura == null)
						codAggregatorePinzatura = new StringType(Costanti.CONTESTO_PLICO_DAC+"-"+doc.getIdDocumento());
					
					doc.setContestoAggregatore(new StringType(Costanti.CONTESTO_PLICO_DAC));
					doc.setCodAggregatore(codAggregatorePinzatura);
					doc.setNonPervenuto(new BooleanType());
					dao.executeTableUpdateAccess(DAO_ACCESS_AGGIORNA_PLICO_DOCUMENTO,doc);
					if(!doc.getUfficio().equals(Costanti.UFFICIO_RETE)){
						try{
							dao.executeTableUpdateAccess("aggiornaPlicoDocumentoSede",doc);							
						}catch(NoRowsAffected nra){
							dao.executeTableInsertAccess("aggiornaPlicoDocumentoSede",doc);							
						}
					}
					
				}else{
					
					PlicoModel plico = DacLoader.loadPlicoDocumento(csc,dao,doc,true);
					if(doc.getUfficio().equals(Costanti.UFFICIO_RETE)){
						spinzaDocumentiNuovoPrit(csc,dao,plico);
					}else{
						for(int j=0;j<plico.getDocumenti().size();j++){
							DocumentoModel docPlico = (DocumentoModel)plico.getDocumenti().get(j);
							
							docPlico.setContestoAggregatore(new StringType());
							docPlico.setCodAggregatore(new StringType());
							docPlico.setNonPervenuto(new BooleanType());
							dao.executeTableUpdateAccess(DAO_ACCESS_AGGIORNA_PLICO_DOCUMENTO,docPlico);
							try{
								dao.executeTableUpdateAccess("aggiornaPlicoDocumentoSede",docPlico);							
							}catch(NoRowsAffected nra){
								dao.executeTableInsertAccess("aggiornaPlicoDocumentoSede",docPlico);							
							}
						}						
					}
				}
				
			}
			return dac;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO in pinzaSpinzaDocumenti: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in pinzaSpinzaDocumenti: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void spinzaDocumentiNuovoPrit(ClientSessionContext csc, DAOObject dao, PlicoModel plico) throws DAOException{
		
		// Imposto i codici di aggregazione dei documenti (non assegni)
		for(int j=0;j<plico.getDocumenti().size();j++){
			DocumentoModel docPlico = (DocumentoModel)plico.getDocumenti().get(j);
			if(docPlico.isDocumentoAssegno())
				continue;
			DacLoader.loadMezziPgDocumento(csc, dao, docPlico);
			if(docPlico.getMezziPagamentoAssegno().size() > 0){
				if(!docPlico.getContestoAggregatoreOriginale().isNull()){
					docPlico.setContestoAggregatore(new StringType(docPlico.getContestoAggregatoreOriginale().toString()));
					docPlico.setCodAggregatore(new StringType(docPlico.getCodAggregatoreOriginale().toString()));
				}else{
					docPlico.setContestoAggregatore(new StringType(Costanti.CONTESTO_PLICO_ASS));
					docPlico.setCodAggregatore(new StringType(Costanti.CONTESTO_PLICO_ASS+"-"+docPlico.getIdDocumento()));
				}
			}else{
				docPlico.setContestoAggregatore(new StringType(docPlico.getContestoAggregatoreOriginale().toString()));
				docPlico.setCodAggregatore(new StringType(docPlico.getCodAggregatoreOriginale().toString()));
			}
		}
		
		// Reimposto i codici di aggregazione degli assegni in base ai loro padri
		for(int j=0;j<plico.getDocumenti().size();j++){
			DocumentoModel assegno = (DocumentoModel)plico.getDocumenti().get(j);
			if(!assegno.isDocumentoAssegno())
				continue;
			for(int k=0;k<plico.getDocumenti().size();k++){ // Cerco il padre
				DocumentoModel docPlico = (DocumentoModel)plico.getDocumenti().get(k);
				if(docPlico.getIdDocumento().equals(assegno.getDatiAssegno().getIdDocumento())){
					assegno.setContestoAggregatore(new StringType(docPlico.getContestoAggregatore().toString()));
					assegno.setCodAggregatore(new StringType(docPlico.getCodAggregatore().toString()));
				}
			}
		}
		
		// Aggiorno il DB per il plico corrente
		for(int j=0;j<plico.getDocumenti().size();j++){
			DocumentoModel docPlico = (DocumentoModel)plico.getDocumenti().get(j);
			docPlico.setNonPervenuto(new BooleanType());
			dao.executeTableUpdateAccess(DAO_ACCESS_AGGIORNA_PLICO_DOCUMENTO,docPlico);
		}
		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public DocumentoModel spinzaDocInSpedizione(ClientSessionContext csc, DocumentoModel doc) throws EJBException{
		DAOObject dao = null;

		try{
			dao = new DAOObject(csc, DAO_XML_NAME_DAC);
			
			doc.setContestoAggregatore(new StringType());
			doc.setCodAggregatore(new StringType());
			doc.setNonPervenuto(new BooleanType());
			dao.executeTableUpdateAccess(DAO_ACCESS_AGGIORNA_PLICO_DOCUMENTO,doc);
			try{
				dao.executeTableUpdateAccess("aggiornaPlicoDocumentoSede",doc);							
			}catch(NoRowsAffected nra){
				dao.executeTableInsertAccess("aggiornaPlicoDocumentoSede",doc);							
			}
			
			//In spedizione il documento in busta deve essere ancora spedito. Gli attributi dei plichi devono essere rimossi
			//Lo spinza viene chiamato per tutti i documenti del plico.
			//Alcuni di essi non sono nella DAC e quindi la delete relativa non va a buon fine 
			try {
				dao.executeTableUpdateAccess("aggiornaPlicoDocumentoInBusta",doc);				
			} catch (NoRowsAffected nra) {	}
			
			return doc;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO nello spinzare il documento: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nello spinzare il documento: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public DocumentoModel creaDocumento(ClientSessionContext csc, StringType idDac, 
										AgenteModel agenteRiferimento,
										DocumentoModel doc, int ubicazione) throws EJBException {
		try{
			StringType utente = new StringType(Tools.fillSx(csc.getUserCode(),'0',10)); 
			
			doc.setIdDac(idDac);
			doc.setAgenteRiferimento(agenteRiferimento);
			
			doc.setUbicazione(new IntegerType(ubicazione));
			
			doc.setStato(new IntegerType(Costanti.STATO_INCORSO));
			doc.setAzione(new IntegerType(Costanti.AZIONE_CREA));
			
			DAOObject dao = new DAOObject(csc,DAO_XML_NAME_DAC);
			StringType idDoc = getContatore(csc,dao,Costanti.GET_PROGRESSIVI_RISORSA_DOCUMENTO);
			doc.setIdDocumento(idDoc);
			
			if(doc.isDocumentoAssegno())
				DocumentoAssegnoTools.impostaDatiDocAssegno(doc);
			else
				DocumentoAssegnoTools.impostaAggregatoreDocPadreAssegno(csc,doc);
			
			TimestampType ora = Tools.now();

			doc.setDataOraCambioStato(ora);
			
			doc.setCodUtenteIns(utente);
			doc.setCodUtenteUpd(utente);
			doc.setDataOraIns(ora);
			doc.setDataOraUpd(ora);
			
			dao.executeTableInsertAccess("documento",doc);
			if(doc.isFaseDiSpunta()){
				dao.executeTableLoadAccess("tipoControlloFirmeDocumento",doc);
				dao.executeTableLoadAccess("esitoFirmaClienteDocumento",doc);
				dao.executeTableLoadAccess("esitoFirmaAgenteDocumento",doc);
			}
			
			if(!doc.getUfficio().equals(Costanti.UFFICIO_RETE))
				dao.executeTableInsertAccess("documentoSede",doc);
			
			dao.executeTableInsertAccess("legameDocumento",doc);
			if(!doc.getUfficio().equals(Costanti.UFFICIO_RETE))
				dao.executeTableInsertAccess("legameDocumentoSede",doc);
			
			if(!doc.isDocumentoAssegno())
				salvaMezziPg(csc,dao,doc);
			
			if(doc.isFaseDiSpunta()){
				String esitoFirmaClienteAutomatico = doc.getEsitoFirmaClienteAutomatico();
				if(esitoFirmaClienteAutomatico != null){
					doc.setEsitoFirmaCliente(new StringType(esitoFirmaClienteAutomatico));
					esitaFirmaCliente(csc, doc);
				}
			}
			return doc;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO in creaDocumento: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in creaDocumento: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public DocumentoModel salvaDocumento(ClientSessionContext csc, DocumentoModel doc) throws EJBException {
		try{
			
			StringType utente = new StringType(Tools.fillSx(csc.getUserCode(),'0',10)); 
			TimestampType ora = Tools.now();
			
			if(doc.isDocumentoAssegno())
				DocumentoAssegnoTools.impostaDatiDocAssegno(doc);
			else
				DocumentoAssegnoTools.impostaAggregatoreDocPadreAssegno(csc,doc);
			
			doc.setCodUtenteUpd(utente);
			doc.setDataOraCambioStato(ora);
			doc.setDataOraUpd(ora);
			
			DAOObject dao = new DAOObject(csc,DAO_XML_NAME_DAC);
			
			dao.executeTableUpdateAccess("documento",doc);
			if(doc.isFaseDiSpunta()){
				dao.executeTableLoadAccess("tipoControlloFirmeDocumento",doc);
				dao.executeTableLoadAccess("esitoFirmaClienteDocumento",doc);
				dao.executeTableLoadAccess("esitoFirmaAgenteDocumento",doc);
			}

			if(!doc.getUfficio().equals(Costanti.UFFICIO_RETE)){
				try{
					dao.executeTableUpdateAccess("documentoSede",doc);					
				}catch(NoRowsAffected nra){
					dao.executeTableInsertAccess("documentoSede",doc);										
				}
			}

			dao.executeTableUpdateAccess("updateDataOraLegameDocumento",doc);				
			if(!doc.getUfficio().equals(Costanti.UFFICIO_RETE)){
				try{
					dao.executeTableUpdateAccess("legameDocumentoSede",doc);					
				}catch(NoRowsAffected nra){
					dao.executeTableInsertAccess("legameDocumentoSede",doc);										
				}
			}
			
			if(doc.isFaseDiSpunta()){
				
				// Nel capire se è cambiato l'esito per gestire gli esiti firma 
				// devo considerare il modificato uguale all'accettato
				boolean esitoCambiato=true;
				if(doc.getEsito().equals(doc.getEsitoCorrente()) || 
				   doc.getEsito().equals(Costanti.ESITO_DOC_ACCETTATO) && doc.getEsitoCorrente().equals(Costanti.ESITO_DOC_MODIFICATO) ||
				   doc.getEsito().equals(Costanti.ESITO_DOC_MODIFICATO) && doc.getEsitoCorrente().equals(Costanti.ESITO_DOC_ACCETTATO))
					esitoCambiato=false;
				
				// Se un cliente è già stato esitato ma non è quello del documento
				// oppure l'operazione non prevede l'esitazione della firma cliente
				// oppure è cambiato l'esito del documento
				// resetto gli esiti firma cliente
				DocumentoModel tmpDoc = new DocumentoModel();
				tmpDoc.setIdDocumento(new StringType(doc.getIdDocumento().toString()));
				DAOTableResultModel tRes = dao.executeTableLoadAccess("rigaEsitoFirmaCliente",tmpDoc);
				if(tRes.getResult().intValue() > 0){
					if(!doc.getCliente().getCodMediolanum().equals(tmpDoc.getCliente().getCodMediolanum()) ||
					   !doc.isOperazioneConControlloFirmeCliente() ||
					    esitoCambiato){
						dao.executeTableDeleteAccess("rigaEsitoFirmaCliente",tmpDoc);
						doc.setEsitoFirmaCliente(new StringType());
					}
				}
				
				// Esitazione automatice delle firme cliente
				String esitoFirmaClienteAutomatico = doc.getEsitoFirmaClienteAutomatico();
				if(esitoFirmaClienteAutomatico != null){
					doc.setEsitoFirmaCliente(new StringType(esitoFirmaClienteAutomatico));
					esitaFirmaCliente(csc, doc);
				}
				
				// Se un agente è già stato esitato ma non è quello del documento
				// oppure l'operazione non prevede l'esitazione della firma agente
				// oppure è cambiato l'esito del documento
				// resetto gli esiti firma agente
				tRes = dao.executeTableLoadAccess("rigaEsitoFirmaAgente",tmpDoc);
				if(tRes.getResult().intValue() > 0){
					if(!doc.getAgente().getCodMediolanum().equals(tmpDoc.getAgente().getCodMediolanum()) ||
					   !doc.isOperazioneConControlloFirmeAgente() ||
					    esitoCambiato){
						dao.executeTableDeleteAccess("rigaEsitoFirmaAgente",tmpDoc);
						doc.setEsitoFirmaAgente(new StringType());
					}
				}
				
			}
			
			// Se è un documento assegno aggiorno il mezzo di pagamento associato e, se in spunta
			// l'esito del documento padre nel caso fosse "modificato" (gli altri non li tocco)
			if(doc.isDocumentoAssegno()){
				DocumentoAssegnoTools.allineaMezzoDiPagmentoAssociato(dao,doc);
				DocumentoAssegnoTools.impostaDatiDocumentoPadreDocAssegno(dao, doc);
			}else{
				salvaMezziPg(csc,dao,doc);
			}
			return doc;
			
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
	public DocumentoModel esitaFirmaCliente(ClientSessionContext csc, DocumentoModel doc) throws EJBException {
		try{
			
			StringType utente = new StringType(Tools.fillSx(csc.getUserCode(),'0',10)); 
			TimestampType ora = Tools.now();

			DAOObject dao = new DAOObject(csc,DAO_XML_NAME_DAC);
			
			doc.setDataOraCambioStato(ora);
			doc.setCodUtenteEsitoFirmaCliente(utente);
			doc.setCodUtenteUpd(utente);
			doc.setDataOraUpd(ora);
			doc.setDataOraEsitoFirmaCliente(ora);
			try{
				dao.executeTableUpdateAccess("rigaEsitoFirmaCliente",doc);
			}catch(NoRowsAffected nra){
				doc.setCodUtenteIns(utente);
				doc.setDataOraIns(ora);
				dao.executeTableInsertAccess("rigaEsitoFirmaCliente",doc);
			}
			return doc;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO in esitaFirmaCliente: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in esitaFirmaCliente: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}		
	}


	/***********************************************************************************************/
	/***********************************************************************************************/
	public DocumentoModel esitaFirmaAgente(ClientSessionContext csc, DocumentoModel doc) throws EJBException {
		try{
			
			StringType utente = new StringType(Tools.fillSx(csc.getUserCode(),'0',10)); 
			TimestampType ora = Tools.now();

			DAOObject dao = new DAOObject(csc,DAO_XML_NAME_DAC);
			
			doc.setDataOraCambioStato(ora);
			doc.setCodUtenteEsitoFirmaAgente(utente);
			doc.setCodUtenteUpd(utente);
			doc.setDataOraUpd(ora);
			doc.setDataOraEsitoFirmaAgente(ora);
			try{
				dao.executeTableUpdateAccess("rigaEsitoFirmaAgente",doc);
			}catch(NoRowsAffected nra){
				doc.setCodUtenteIns(utente);
				doc.setDataOraIns(ora);
				dao.executeTableInsertAccess("rigaEsitoFirmaAgente",doc);
			}
			return doc;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO in esitaFirmaAgente: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in esitaFirmaAgente: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}		
	}


	/***********************************************************************************************/
	/***********************************************************************************************/
	private void salvaMezziPg(ClientSessionContext csc, DAOObject dao, DocumentoModel doc) throws Exception, DAOException {
		
		// Calcolo il progressivo più alto sia tra i mezzi di pagamento da cancellare che tra quelli da inserire/salvare
		int maxIdMezzoPg = -1;
		ListType mezziPg = doc.getMezziPagamentoDaCancellare();
		for(int i=0;i<mezziPg.size();i++){
			MezzoPagamentoModel mezzoPg = (MezzoPagamentoModel)mezziPg.get(i);
			if(!mezzoPg.getIdMezzoPg().isNull() && mezzoPg.getIdMezzoPg().intValue() > maxIdMezzoPg)
				maxIdMezzoPg = mezzoPg.getIdMezzoPg().intValue();
		}
		mezziPg = doc.getMezziPagamento();
		for(int i=0;i<mezziPg.size();i++){
			MezzoPagamentoModel mezzoPg = (MezzoPagamentoModel)mezziPg.get(i);
			if(!mezzoPg.getIdMezzoPg().isNull() && mezzoPg.getIdMezzoPg().intValue() > maxIdMezzoPg)
				maxIdMezzoPg = mezzoPg.getIdMezzoPg().intValue();
		}
		maxIdMezzoPg++;
		
		// Vedo nei mezziPg da cancellare per cancellare i documenti assegno
		mezziPg = doc.getMezziPagamentoDaCancellare();
		for(int i=0;i<mezziPg.size();i++){
			MezzoPagamentoModel mezzoPg = (MezzoPagamentoModel)mezziPg.get(i);
			if(!mezzoPg.getCodTipoPagamento().equals(Costanti.MEZZO_PAGAMENTO_ASSEGNO) && !mezzoPg.getCodTipoPagamento().equals(Costanti.MEZZO_PAGAMENTO_ASSEGNO_ESTERO))
				continue;
			if(mezzoPg.getIdDocumentoAssegno().isNull())
				continue;
			DocumentoModel assegno = new DocumentoModel();
			assegno.copyParams(doc);
			assegno.setIdDac(new StringType(doc.getIdDac().toString()));
			assegno.setIdDocumento(new StringType(mezzoPg.getIdDocumentoAssegno().toString()));
			cancellaDocumento(csc, assegno.getIdDac(), assegno);
		}
		doc.getMezziPagamentoDaCancellare().clear();
		
		// Cancello tutti i mezziPg dal DB
		try{
			dao.executeTableDeleteChildsAccess("mezzoDiPagamento",doc);
		}catch(NoRowsAffected nra){}
		
		// Ora inserisco i mezzi pg presenti a video e per gli assegni creo/salvo il relativo documento assegno
		StringType utente = new StringType(Tools.fillSx(csc.getUserCode(),'0',10));
		mezziPg = doc.getMezziPagamento();
		for(int i=mezziPg.size()-1;i>=0;i--){
			TimestampType ora = Tools.now();
			MezzoPagamentoModel mezzoPg = (MezzoPagamentoModel)mezziPg.get(i);
			if(mezzoPg.getIdMezzoPg().isNull()){
				mezzoPg.setIdMezzoPg(new IntegerType(maxIdMezzoPg++));
				mezzoPg.setCodUtenteIns(utente);
				mezzoPg.setCodUtenteUpd(utente);
				mezzoPg.setDataOraIns(ora);
				mezzoPg.setDataOraUpd(ora);
			}else{
				mezzoPg.setCodUtenteUpd(utente);
				mezzoPg.setDataOraUpd(ora);
			}
			mezzoPg.setAgenteRiferimento((AgenteModel)Tools.cloneObject(doc.getAgenteRiferimento()));
			mezzoPg.setIdDocumento(doc.getIdDocumento());
			if(mezzoPg.getCodTipoPagamento().equals(Costanti.MEZZO_PAGAMENTO_ASSEGNO) || mezzoPg.getCodTipoPagamento().equals(Costanti.MEZZO_PAGAMENTO_ASSEGNO_ESTERO)){
				DocumentoModel docAssegno = DocumentoAssegnoTools.initDocumentoAssegnoAssociatoAMezzoPg(csc,dao,doc,mezzoPg);
				if(docAssegno != null){
					if(docAssegno.getIdDocumento().isNull()){ // Il documento-assegno non esite ancora
						creaDocumento(csc, docAssegno.getIdDac(), docAssegno.getAgenteRiferimento(), docAssegno, docAssegno.getUbicazione().intValue());
					}else{
						salvaDocumento(csc, docAssegno);
					}
					mezzoPg.setIdDocumentoAssegno(docAssegno.getIdDocumento());
				}
			}else{
				if(!mezzoPg.getIdDocumentoAssegno().isNull()){
					DocumentoModel docAssegno = DocumentoAssegnoTools.initDocumentoAssegnoAssociatoAMezzoPg(csc,dao,doc,mezzoPg);
					if(docAssegno != null){
						cancellaDocumento(csc, docAssegno.getIdDac(), docAssegno);
						mezzoPg.setIdDocumentoAssegno(new StringType());
					}
				}				
			}
			dao.executeTableInsertAccess("mezzoDiPagamento",mezzoPg);
		}
	}

	/***********************************************************************************************/
	/*
	 * Per la cancellazione basta impostare nel DocumentoModel l'id del documento da cancellare
	 */
	/***********************************************************************************************/
	public void cancellaDocumento(ClientSessionContext csc, StringType idDac, DocumentoModel doc) throws EJBException {
		try{
			doc.setIdDac(idDac);
			
			DAOObject dao = new DAOObject(csc,DAO_XML_NAME_DAC);
			
			// Mezzi di pagamento
			try{dao.executeTableDeleteChildsAccess("mezzoDiPagamento",doc);}catch(NoRowsAffected nra){}
			
			if(doc.isDocumentoAssegno()){
				// Mezzo di pagamento del padre del documento-assegno
				try{dao.executeTableDeleteAccess("mezzoDiPagamento",doc.getDatiAssegno());}catch(NoRowsAffected nra){}
				// Aggiorno i codici di aggregazione del documento padre
				DocumentoModel docPadre = DocumentoAssegnoTools.loadDocumentoPadreAssegno(dao,doc,true);
				DocumentoAssegnoTools.impostaAggregatoreDocPadreAssegno(csc,docPadre);
				try{dao.executeTableUpdateAccess(DAO_ACCESS_AGGIORNA_PLICO_DOCUMENTO,docPadre);}catch(NoRowsAffected nra){ /* do nothing */ }
				if(!doc.getUfficio().equals(Costanti.UFFICIO_RETE)){
					try{
						dao.executeTableUpdateAccess("aggiornaPlicoDocumentoSede",docPadre);							
					}catch(NoRowsAffected nra){
						dao.executeTableInsertAccess("aggiornaPlicoDocumentoSede",docPadre);							
					}
				}
			}else{
				// Cancello i documenti-assegno legati ai mezzi di pagamento
				ListType mezziPg = doc.getMezziPagamento();
				for(int i=0;i<mezziPg.size();i++){
					MezzoPagamentoModel mezzoPg = (MezzoPagamentoModel)mezziPg.get(i);
					if(!mezzoPg.getCodTipoPagamento().equals(Costanti.MEZZO_PAGAMENTO_ASSEGNO) && !mezzoPg.getCodTipoPagamento().equals(Costanti.MEZZO_PAGAMENTO_ASSEGNO_ESTERO))
						continue;
					if(mezzoPg.getIdDocumentoAssegno().isNull())
						continue;
					DocumentoModel assegno = new DocumentoModel();
					assegno.copyParams(doc);
					assegno.setIdDac(new StringType(doc.getIdDac().toString()));
					assegno.setIdDocumento(new StringType(mezzoPg.getIdDocumentoAssegno().toString()));
					cancellaDocumento(csc, assegno.getIdDac(), assegno);
				}
			}
			
			// Legame con la testata
			try{
				dao.executeTableDeleteAccess("legameDocumento",doc);
			}catch(NoRowsAffected nra){}
			if(!doc.getUfficio().equals(Costanti.UFFICIO_RETE)){
				try{
					dao.executeTableDeleteAccess("legameDocumentoSede",doc);
				}catch(NoRowsAffected nra){}
			}
			
			// Documento
			try{
				dao.executeTableDeleteAccess("documento",doc);
			}catch(NoRowsAffected nra){}
			
			if(!doc.getUfficio().equals(Costanti.UFFICIO_RETE)){
				
				try{
					dao.executeTableDeleteAccess("documentoSede",doc);
				}catch(NoRowsAffected nra){}
				
				// Firme clienti
				try{
					dao.executeTableDeleteAccess("rigaEsitoFirmaCliente",doc);
				}catch(NoRowsAffected nra){}
				
				// Firme agenti
				try{
					dao.executeTableDeleteAccess("rigaEsitoFirmaAgente",doc);
				}catch(NoRowsAffected nra){}
			}
			
			return;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO in cancellaDocumento: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in cancellaDocumento: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public DocumentoModel aggiornaDatiDocInRicezione(ClientSessionContext csc, DocumentoModel docDaAggiornare) throws EJBException{
		DAOObject dao = new DAOObject(csc,DAO_XML_NAME_SEDE);

		try{
			StringType utente = new StringType(Tools.fillSx(csc.getUserCode(),'0',10)); 
			TimestampType ora = Tools.now();

			docDaAggiornare.setUffDestinatario(new IntegerType());
			docDaAggiornare.setReso(new BooleanType(false));
			docDaAggiornare.setCodUtenteUpd(utente);
			docDaAggiornare.setDataOraUpd(ora);
			
			dao.executeTableUpdateAccess("aggiornaDatiDocInRicezione", docDaAggiornare);
			try{
				dao.executeTableUpdateAccess("aggiornaDatiDocInRicezioneSede", docDaAggiornare);
			}catch(NoRowsAffected nra){
				dao.executeTableInsertAccess("aggiornaDatiDocInRicezioneSede", docDaAggiornare);					
			}
			return docDaAggiornare;
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO nell'aggiornare il documento in ricezione: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		} catch(Exception e){
			String errorMsg = getClass()+" Eccezione nell'aggiornare il documento in ricezione: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}				
	}

	/********************************************************************************************************/
	/********************************************************************************************************/
	private StringType getContatore(ClientSessionContext csc, DAOObject dao, String nomeRisorsa) throws Exception{
		try{
			
			String filledUserCode = Tools.fillSx(csc.getUserCode().toUpperCase(),'0',10);
			if(csc.isAssistenteFB())
				filledUserCode = Tools.fillSx(csc.getCurrentLinkedUserCode().toUpperCase(),'0',10);
			ContatoreModel model = new ContatoreModel();
			model.setNomeRisorsa(new StringType(nomeRisorsa));
			model.setUtente(new StringType(filledUserCode));
			model.setIncremento(new IntegerType(1));
			DAOCallableResultModel callRes = dao.executeCallableAccess("getContatore",model);
			if(callRes.getResult() != 0)
				throw new Exception("Errore ["+callRes.getResult()+"] nel prendere il contatore per la risorsa ["+nomeRisorsa+"]");
			return model.getProgressivo();
			
		}catch(DAOException daoe){
			String errorMsg = "Eccezione DAO nel prendere il contatore per la risorsa ["+nomeRisorsa+"]: "+daoe;
			Exception e = new Exception(errorMsg);
			throw e;
		}catch(Exception e){
			String errorMsg = "Eccezione nel prendere il contatore per la risorsa ["+nomeRisorsa+"]: "+e;
			e = new Exception(errorMsg);
			throw e;
		}
	}	
	

	
}
