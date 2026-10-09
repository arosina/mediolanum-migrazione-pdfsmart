package prgm.ita.p.dac.sede.facade;

import javax.ejb.EJBException;

import prgm.ita.p.dac.facade.Costanti;
import prgm.ita.p.dac.facade.DacFacade;
import prgm.ita.p.dac.facade.DacLoader;
import prgm.ita.p.dac.facade.FacadeLoader;
import prgm.ita.p.dac.manager.DacManager;
import prgm.ita.p.dac.model.DacKeyModel;
import prgm.ita.p.dac.model.DacModel;
import prgm.ita.p.dac.model.DocumentoModel;
import prgm.ita.p.dac.model.ErroreDocumentoModel;
import prgm.ita.p.dac.model.ParamsModel;
import prgm.ita.p.dac.sede.model.SbloccaDacModel;
import prgm.ita.p.dac.util.DacTools;

import com.atosorigin.wfem.backend.FacadeObject;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandError;
import com.atosorigin.wfem.command.CommandMessage;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

/***********************************************************************************************/
/***********************************************************************************************/
public class DacSedeFacadeBean extends FacadeObject implements DacSedeFacade{

	private static final String DAO_XML_NAME = "ItaPDac.Dac";
	private static final String DAO_XML_NAME_SEDE = "ItaPDac.DacSede";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacModel verificaSeEsisteDacAttiva(ClientSessionContext csc, DacModel dac) throws EJBException{
		try{
			if (!dac.getIdDac().isNull()) {
				DAOObject dao = new DAOObject(csc,DAO_XML_NAME);			
				
				DacLoader.loadDocumentiDac(csc, dao, dac);
				DacLoader.loadErroriDocumentiDac(csc, dao, dac);				
			}

			if ((dac.isSmistatore() && dac.getBox().isNull()) || (!dac.isSmistatore() && dac.getUffDestinatario().isNull())) {
				dac.setBox(new IntegerType());
				dac.setUffDestinatario(new IntegerType());
				dac.setCassettaBox(new IntegerType());
				return dac;
			}
			
			DAOObject daoSede = new DAOObject(csc,DAO_XML_NAME_SEDE);
			DAOQueryResultModel qRes = null;
			
			if (dac.isSmistatore()) {
				//Recupero l'ufficio associato al box selezionato
				qRes = daoSede.executeQueryAccess("recuperaUffDestinatario",dac);
				IntegerType uffDestinatario = (IntegerType)qRes.getSingleResult();
				if (uffDestinatario == null)
					throw new EJBException("Nessun ufficio associato al box " + dac.getBox().toString());
				else
					dac.setUffDestinatario(uffDestinatario);
			}
			
			//Verifico se esiste una dac in bozza generata dallo user corrente e destinata all'ufficio recuperato sopra 
			qRes = daoSede.executeQueryAccess("verificaSeEsisteDacAttiva",dac);
			BooleanType esisteDac = (BooleanType)qRes.getSingleResult();
			if (esisteDac.booleanValue()) {
				if (dac.isSmistatore())
					dac.setMsg("E' già presente una DAC per il box selezionato");
				else
					dac.setMsg("E' già presente una DAC per l'ufficio destinatario selezionato");
			}

			if (dac.isCtrlCassette()) {
				DAOObject dao = new DAOObject(csc,DAO_XML_NAME);

				qRes = dao.executeQueryAccess("recuperaCassettaBox",dac);
				IntegerType codCassetta = (IntegerType)qRes.getSingleResult();
				if (codCassetta == null) {
					dac.setCassettaBox(new IntegerType());
					
					String errStr = "Nessuna cassetta definita per il box selezionato. Impossibile inserire documenti";
					if (dac.isSmistatore())
						dac.getBox().addTypeError(errStr);
					else
						dac.getUffDestinatario().addTypeError(errStr);
				} else {
					dac.setCassettaBox(codCassetta);
				}
			}
			
			return dac;			
		}catch (DAOException daoe) {
			String errorMsg = getClass()+" Eccezione DAO nel ricercare l'esistenza della DAC: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nel ricercare l'esistenza della DAC: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}				
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacModel nuovaDacResi(ClientSessionContext csc,	ParamsModel dacParams) throws EJBException {
		try{
			
			DacModel dac = nuovaDac(csc, dacParams);
			
			dac.setReso(new BooleanType(true));
			return dac;
			
		} catch(Exception e){
			String errorMsg = getClass()+" Eccezione in nuovaDacResi: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacModel nuovaDac(ClientSessionContext csc,	ParamsModel dacParams) throws EJBException {
		try{
			
			DacModel dac = new DacModel();
			dacParams.addParam(ParamsModel.dopoSpunta);
			DacTools.loadUfficioUtente(csc, dacParams);
			dac.copyParams(dacParams);
			
			dac.setTipoDac(new IntegerType(Costanti.TIPO_DAC_SEDE));
			dac.setUbicazione(new IntegerType(dac.getUfficio()));
			dac.setUffMittente(new IntegerType(dac.getUfficio()));			
			
			return dac;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in nuovaDac: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}				
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private ErroreDocumentoModel scriviErroreDocumento (ClientSessionContext csc, StringType idDac, DocumentoModel doc, int codErrore, StringType parametroUno, boolean inSpedizione) throws DAOException {
		if (parametroUno==null)
			parametroUno = new StringType();
		
		ErroreDocumentoModel errore = new ErroreDocumentoModel();
		DAOObject daoDac = new DAOObject(csc,DAO_XML_NAME);
		
		errore.setIdDac(idDac);
		errore.setBarcode(doc.getBarcode());
		errore.setCodErrore(new IntegerType(codErrore));
		errore.setDataIns(Tools.now());
		errore.setInSpedizione(new BooleanType(inSpedizione));
		errore.setParametroUno(parametroUno);
		
		daoDac.executeTableInsertAccess("erroreDocumento", errore);
		return errore;		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public DocumentoModel inserisciDocInDac(ClientSessionContext csc, DocumentoModel docDaInserire) throws EJBException{

		DAOObject daoDac = new DAOObject(csc,DAO_XML_NAME);
		DAOObject daoSede = new DAOObject(csc,DAO_XML_NAME_SEDE);
		DAOQueryResultModel qRes = null;

		DocumentoModel docLetto = new DocumentoModel();
		docLetto.setBarcode(docDaInserire.getBarcode());
		docLetto.copyParams(docDaInserire);

		try{
			// Verifico l'esistenza del documento
			qRes = daoSede.executeQueryAccess("loadDocumento", docLetto);
			if (qRes.getResult().size()==0) {
				ErroreDocumentoModel errore = scriviErroreDocumento(csc, docDaInserire.getIdDac(), docLetto, Costanti.ERR_DOC_NON_TROVATO, null, true);
				docLetto.setErroreInLettura(errore);
				
				return docLetto;
			} 

			//Dati del documento letto
			boolean isLavorato = docLetto.getStato().equals(Costanti.STATO_LAVORATA);
			boolean isFuoriDac = docLetto.getIdDac().equals(Costanti.ID_DAC_X_DOC_FUORI_DAC);
			boolean isNonPervenuto = docLetto.getNonPervenuto().booleanValue();
			boolean isInAltroUfficio = !docLetto.getUbicazione().equals(docDaInserire.getUfficio());
			boolean isInViaggio = docLetto.getUbicazione().equals(Costanti.UBICAZIONE_IN_VIAGGIO);
			boolean isDaSpunta = false;
			boolean isInDacDaChiudere = docLetto.getIsInBusta().booleanValue();
			
			DacModel dac = new DacModel();
			if (!isFuoriDac) {
				//Recupero i dati della DAC corrente
				dac.setIdDac(docLetto.getIdDac());
				daoDac.executeTableLoadAccess("dac",dac);
				
				if(dac.getTipoDac().intValue() != Costanti.TIPO_DAC_SEDE)
					isDaSpunta = true;
			}
			
			//Il documento "Non pervenuto" è sempre sparabile
			if (isNonPervenuto && isFuoriDac) {
				//Dichiarato in ricezione "Non pervenuto" e la DAC contenente il documento è stata chiusa
				//Inserisco il documento nella dac
				DacManager manager = (DacManager)ROF.getManager(csc,DacManager.class);
				DocumentoModel docInserito = manager.inserisciDocInDac(csc,docDaInserire.getIdDac(),docLetto.getIdDocumento(), false, false);
				return docInserito;
			}
			

			// Verifico se il documento è stato lavorato
			if (!isLavorato) {
				ErroreDocumentoModel errore = scriviErroreDocumento(csc, docDaInserire.getIdDac(), docLetto, Costanti.ERR_DOC_NON_LAVORATO, null, true);
				docLetto.setErroreInLettura(errore);
				
				return docLetto;
			}
			
			// Verifico se il documento si trova in altro ufficio
			if (isInAltroUfficio && (isFuoriDac || isDaSpunta)) {
				qRes = daoSede.executeQueryAccess("recuperaDescrUfficio", docLetto);
				ErroreDocumentoModel errore = scriviErroreDocumento(csc, docDaInserire.getIdDac(), docLetto, Costanti.ERR_DOC_NON_DISPONIBILE_IN_UFF_CORR, (StringType)qRes.getSingleResult(), true);
				docLetto.setErroreInLettura(errore);
				
				return docLetto;
			}

			// Verifico se il documento si trova in altra DAC			
			if (!isFuoriDac && (isInAltroUfficio || isInViaggio)) {
				ErroreDocumentoModel errore = scriviErroreDocumento(csc, docDaInserire.getIdDac(), docLetto, Costanti.ERR_DOC_IN_ALTRA_DAC, docLetto.getIdDac(), true);
				docLetto.setErroreInLettura(errore);
				
				return docLetto;
			}
			
			// Verifico se il documento si trova in una DAC ancora da chiudere
			if (!isInAltroUfficio && isFuoriDac && isInDacDaChiudere) {
				ErroreDocumentoModel errore = scriviErroreDocumento(csc, docDaInserire.getIdDac(), docLetto, Costanti.ERR_DOC_IN_DAC_NON_CHIUSA, null, true);
				docLetto.setErroreInLettura(errore);
				
				return docLetto;
			}

			//Inserisco il documento nella dac
			DacManager manager = (DacManager)ROF.getManager(csc,DacManager.class);
			DocumentoModel docInserito = manager.inserisciDocInDac(csc,docDaInserire.getIdDac(),docLetto.getIdDocumento(), false, false);

			//Warning
			if(docLetto.isCtrlCassette() && !docLetto.getCodCassetta().equals(docDaInserire.getCodCassetta())) {
				qRes = daoSede.executeQueryAccess("recuperaDescrCassettaDocumento", docLetto);
				ErroreDocumentoModel errore = scriviErroreDocumento(csc, docDaInserire.getIdDac(), docLetto, Costanti.ERR_BOX_CASSETTA_NON_COMPATIBILI, (StringType)qRes.getSingleResult(), true);
				docInserito.setErroreInLettura(errore);
				docInserito.setWarningInLettura(true);
			}

			return docInserito;
			
		}catch (DAOException daoe) {
			String errorMsg = getClass()+" Eccezione DAO nell'inserire il documento nella DAC: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nell'inserire il documento nella DAC: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}				
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public DocumentoModel rimuoviDocDaDac(ClientSessionContext csc, DocumentoModel doc) throws EJBException{
		try{
			//Rimuovo il documento dalla dac
			DacManager manager = (DacManager)ROF.getManager(csc,DacManager.class);
			manager.rimuoviDocDaDac(csc, doc.getIdDac(), doc.getIdDocumento());
	
			return doc;			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nel rimuovere il documento dalla DAC: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}				
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public DocumentoModel spinzaDocDaDac(ClientSessionContext csc, DocumentoModel doc) throws EJBException{

		try{
			DacManager manager = (DacManager)ROF.getManager(csc,DacManager.class);
			doc = manager.spinzaDocInSpedizione(csc, doc);

			return doc;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nello spinzare il documento: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}		
	}

	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacModel spedisciDac(ClientSessionContext csc, DacModel dac) throws EJBException{
		
		try{
			
			DacLoader.loadDocumentiDac(csc,null,dac);
			DacLoader.loadErroriDocumentiDac(csc,null,dac);

			ControlliDacSede controlli = (ControlliDacSede)new ControlliDacSedeImpl();
			if(!controlli.controllaDacInSpedizione(csc, dac))
				return dac;
				
			DacManager manager = (DacManager)ROF.getManager(csc,DacManager.class);
			dac = manager.spedisciDac(csc, dac);
			
			dac.setRefreshable(true);
			dac.addCommandMessage("DAC spedita correttamente");
			return dac;
			
		} catch(Exception e){
			String errorMsg = getClass()+" Eccezione nello spedire la DAC: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}				
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacModel gestisciDac(ClientSessionContext csc, DacKeyModel dacKey) throws EJBException{
		try{
			DacFacade facade = (DacFacade)FacadeLoader.getFacade(csc, DacFacade.class);
			DacModel dac = facade.leggiDac(csc, dacKey);

			DacManager manager = (DacManager)ROF.getManager(csc,DacManager.class);
			dac = manager.gestisciDac(csc, dac);
			
			dac.setRefreshable(true);
			return dac;
		} catch(Exception e){
			String errorMsg = getClass()+" Eccezione nel gestire la DAC: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}				
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public DocumentoModel aggiornaDatiDocInRicezione(ClientSessionContext csc, DocumentoModel docDaAggiornare) throws EJBException{
		try{
			DacManager manager = (DacManager)ROF.getManager(csc,DacManager.class);
			return manager.aggiornaDatiDocInRicezione(csc, docDaAggiornare);
		} catch(Exception e){
			String errorMsg = getClass()+" Eccezione nell'aggiornare il documento in ricezione: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}				
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacModel	aggiungiDocInRicezione(ClientSessionContext csc, DacModel dac)	 throws EJBException {
		DAOObject dao = new DAOObject(csc,DAO_XML_NAME);
		DAOObject daoSede = new DAOObject(csc,DAO_XML_NAME_SEDE);
		DAOQueryResultModel qRes = null;

		DocumentoModel docLetto = new DocumentoModel();
		docLetto.setBarcode(dac.getDocumento().getBarcode());
		docLetto.copyParams(dac);

		try{
			ErroreDocumentoModel errore = null;
			
			// Verifico l'esistenza del documento
			qRes = daoSede.executeQueryAccess("loadDocumento", docLetto);
			if (qRes.getResult().size()==0) {
				errore = scriviErroreDocumento(csc, dac.getIdDac(), docLetto, Costanti.ERR_DOC_NON_TROVATO, null, false);
				docLetto.setErroreInLettura(errore);
			} 
			
			//Verifico se il documento è già stato ricevuto nell'ufficio corrente
			if (docLetto.getUbicazione().equals(dac.getUfficio())) {
				errore = scriviErroreDocumento(csc, dac.getIdDac(), docLetto, Costanti.ERR_DOC_GIA_RICEVUTO, null, false);
				docLetto.setErroreInLettura(errore);
			}

			if (errore != null) {
				//Ricarico i documenti
				DacLoader.loadDocumentiDac(csc,dao,dac);
				DacLoader.loadErroriDocumentiDac(csc,dao,dac);

				qRes = daoSede.executeQueryAccess("recuperaDescrErrore", errore);
				dac.addCommandError(((StringType)qRes.getSingleResult()).toString());
				return dac;
			}
			
			//Controllo errore smistamento
			boolean dichiaratoNonPrevenuto = docLetto.getNonPervenuto().booleanValue() && docLetto.getIdDac().equals(Costanti.ID_DAC_X_DOC_FUORI_DAC);
			boolean rimastoDalMittente = docLetto.getUbicazione().equals(dac.getUffMittente());	//TRUE se è stato messo un documento in una busta senza inserirlo in una DAC
			boolean aggiungible = rimastoDalMittente || dichiaratoNonPrevenuto;
			if (!aggiungible) {
				docLetto.setIsErroreSmistamento(new BooleanType(true));
				dac.addCommandError(new CommandError("Il documento con barcode " + docLetto.getBarcode().toString() + " risulta essere in un altro ufficio"));
			}
			
			//Inserisco il documento nella DAC
			DacManager manager = (DacManager)ROF.getManager(csc,DacManager.class);
			manager.inserisciDocInDac(csc,dac.getIdDac(),docLetto.getIdDocumento(), true, docLetto.getIsErroreSmistamento().booleanValue());
			
			if (!docLetto.getIsErroreSmistamento().booleanValue()) {
				//Segno il documento come ricevuto
				docLetto.setIdDac(new StringType(Costanti.ID_DAC_X_DOC_FUORI_DAC));
				docLetto.setUbicazione(dac.getUfficio());
				docLetto.setNonPervenuto(new BooleanType(false));
				docLetto.setIsInBusta(new BooleanType(true));
				
				aggiornaDatiDocInRicezione(csc, docLetto);
			}

			//Ricarico i documenti
			DacLoader.loadDocumentiDac(csc,dao,dac);
			DacLoader.loadErroriDocumentiDac(csc,dao,dac);
			return dac;
			
		}catch (DAOException daoe) {
			docLetto.addCommandError(new CommandError("Errore non previsto. Documento non aggiunto"));
			return dac;
		}catch(Exception e){
			docLetto.addCommandError(new CommandError("Errore non previsto. Documento non aggiunto"));
			return dac;
		}				
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void riceviDocInErroreSmistamento (ClientSessionContext csc, StringType idDac, StringType idDoc, IntegerType ufficioDiRicezione, boolean inFaseRicezione, boolean aggiuntoInRicezione) throws EJBException {
		
		try {
			DacManager manager = (DacManager)ROF.getManager(csc,DacManager.class);

			manager.rimuoviDocDaDac(csc, idDac, idDoc);
			manager.inserisciDocInDac(csc, idDac, idDoc, aggiuntoInRicezione, false);

			//Segno il documento come ricevuto
			DocumentoModel doc = new DocumentoModel();

			if (inFaseRicezione)
				doc.setIdDac(new StringType(Costanti.ID_DAC_X_DOC_FUORI_DAC));
			else
				doc.setIdDac(idDac);
			
			doc.setIdDocumento(idDoc);
			doc.setUbicazione(ufficioDiRicezione);
			doc.setNonPervenuto(new BooleanType(false));
			doc.setIsInBusta(new BooleanType(true));
			
			aggiornaDatiDocInRicezione(csc, doc);		
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nel verificare il blocco sul documento: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}				

	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacModel verificaSeDocumentoInErrSmistamento(ClientSessionContext csc, DacModel dac) throws EJBException{
		try{
			DAOObject daoSede = new DAOObject(csc,DAO_XML_NAME_SEDE);
			
			DAOQueryResultModel qRes = daoSede.executeQueryAccess("verificaSeDocumentoInErrSmistamento",dac);
			if (qRes.getResult().size()==0) {
				dac.addCommandError(new CommandError("Il documento con barcode " + dac.getDocumento().getBarcode().toString() + " risulta bloccato in un altro ufficio"));
			} else {
				DocumentoModel doc = (DocumentoModel)qRes.getResult().get(0);
				
				riceviDocInErroreSmistamento (csc, dac.getIdDac(), doc.getIdDocumento(), dac.getUfficio(), true, true);				
			}

			//Ricarico i documenti
			DAOObject dao = new DAOObject(csc,DAO_XML_NAME);
			DacLoader.loadDocumentiDac(csc,dao,dac);
			
			return dac;			
		}catch (DAOException daoe) {
			String errorMsg = getClass()+" Eccezione DAO nel verificare il blocco sul documento: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nel verificare il blocco sul documento: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}				
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public DacModel chiudiDac(ClientSessionContext csc, DacModel dac) throws EJBException{
		try{
			
			DacLoader.loadDocumentiDac(csc,null,dac);
			
			ControlliDacSede controlli = (ControlliDacSede)new ControlliDacSedeImpl();
			if(!controlli.controllaDacInChiusura(csc, dac))
				return dac;
			
			DacManager manager = (DacManager)ROF.getManager(csc,DacManager.class);
			dac = manager.chiudiDac(csc, dac);
			
			dac.setRefreshable(true);
			dac.addCommandMessage("DAC chiusa correttamente");
			
			return dac;
		} catch(Exception e){
			String errorMsg = getClass()+" Eccezione nel chiudere la DAC: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}				
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public SbloccaDacModel loadDacAttive(ClientSessionContext csc, SbloccaDacModel model) throws EJBException {

		try{
			
			DAOObject daoSede = new DAOObject(csc,DAO_XML_NAME_SEDE);
			
			//Carico i dati del documento
			DocumentoModel doc = new DocumentoModel();
			doc.setBarcode(model.getBarcode());
			daoSede.executeQueryAccess("loadDocumento", doc);
			model.setDocumento(doc);
			
			//Carico le DAC che contengono il documento			
			DAOQueryResultModel qRes = daoSede.executeQueryAccess("loadDacBloccate", model);
			model.setElencoDac(qRes.getResult());
			
			return model;

		}catch (DAOException daoe) {
			String errorMsg = getClass()+" Eccezione DAO nel recuperare le DAC bloccate: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		} catch(Exception e){
			String errorMsg = getClass()+" Eccezione nel recuperare le DAC bloccate: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public SbloccaDacModel riceviDocForzato(ClientSessionContext csc, SbloccaDacModel model) throws EJBException {
		
		try{
			model.resetCommandMessages();
			
			DacManager manager = (DacManager)ROF.getManager(csc,DacManager.class);
			
			//Trovo la Dac selezionata nella lista
			DacModel dac = null;
			for (int i=0; i<model.getElencoDac().size(); i++) {
				DacModel curDac = (DacModel)model.getElencoDac().get(i);
				
				if (curDac.getIdDac().equals(model.getIdDacSelezionata()))
					dac = curDac;
			}

			//Elimino il documento da tutte le altre DAC
			DocumentoModel doc = null;
			for (int i=0; i<model.getElencoDac().size(); i++) {
				DacModel curDac = (DacModel)model.getElencoDac().get(i);
				
				if (!curDac.getIdDac().equals(dac.getIdDac())) {
					doc = new DocumentoModel();
					
					doc.setIdDac(curDac.getIdDac());
					doc.setIdDocumento(model.getDocumento().getIdDocumento());
					
					manager.rimuoviDocDaDac(csc, doc.getIdDac(), doc.getIdDocumento());
					
					//Spinzo il documento da un eventuale plico
					manager.spinzaDocInSpedizione(csc, doc);
				}
			}
			
			//Segno il documento come ricevuto nella DAC selezionata
			if (dac.getStato().equals(Costanti.STATO_INCORSO)) {
				doc = new DocumentoModel();
				
				doc.setIdDac(dac.getIdDac());
				doc.setIdDocumento(model.getDocumento().getIdDocumento());

				//Spinzo il documento da un eventuale plico
				manager.spinzaDocInSpedizione(csc, doc);

				riceviDocInErroreSmistamento (csc, dac.getIdDac(), model.getDocumento().getIdDocumento(), dac.getUffMittente(), false, false);
			} else {
				doc = new DocumentoModel();
				
				doc.setIdDac(new StringType());
				doc.setIdDocumento(model.getDocumento().getIdDocumento());

				//Spinzo il documento da un eventuale plico
				manager.spinzaDocInSpedizione(csc, doc);

				riceviDocInErroreSmistamento (csc, dac.getIdDac(), model.getDocumento().getIdDocumento(), dac.getUffDestinatario(), true, false);
			}
			
			//Ricarcio le DAC bloccate
			loadDacAttive(csc, model);
			
			model.addCommandMessage(new CommandMessage("SbloccaDac.ricezioneOk", model.getBarcode(), dac.getIdDac()));
			return model;

		} catch(Exception e){
			String errorMsg = getClass()+" Eccezione nel risolvere il blocco DAC: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}
		
	}

}

