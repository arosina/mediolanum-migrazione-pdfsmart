package prgm.ita.anagraficaclienti.facade;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.atosorigin.wfem.backend.ManagerObject;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOCallableResultModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.dao.exceptions.NoRowsAffected;
import com.atosorigin.wfem.pdf.AdobeFormCompiler;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;
import com.atosorigin.wfem.util.Tools;

import prgm.ita.anagraficaclienti.cogestione.CogestioneDataManager;
import prgm.ita.anagraficaclienti.flussofatca.AbstractNavigatore;
import prgm.ita.anagraficaclienti.model.ClienteModel;
import prgm.ita.anagraficaclienti.model.ContatoreModel;
import prgm.ita.anagraficaclienti.model.DatiPrivacyModel;
import prgm.ita.anagraficaclienti.model.DatiStampa;
import prgm.ita.anagraficaclienti.model.DatiVariazioneModel;
import prgm.ita.anagraficaclienti.model.IndirizzoModel;
import prgm.ita.anagraficaclienti.model.InfoPritModel;
import prgm.ita.anagraficaclienti.model.PdfVariazioneModel;
import prgm.ita.anagraficaclienti.model.TelefonoModel;
import prgm.ita.anagraficaclienti.model.VariazioneKeyModel;
import prgm.ita.p.dac.service.Cliente;
import prgm.ita.p.dac.service.Contratto;
import prgm.ita.p.dac.service.DacServiceCaller;

/********************************************************************************************************/
/********************************************************************************************************/
@Stateless(name = "WriterAnagraficaManager", mappedName = "WriterAnagraficaManager")
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class WriterAnagraficaManagerBean extends ManagerObject implements WriterAnagraficaManager{

	private static final String DAO_XML_ORACLE = "ItaAnagraficaClienti.AnagraficaClientiOracle";
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void salvaOracle(ClientSessionContext csc, ClienteModel model) throws EJBException {

		try{
			
			DAOObject dao = new DAOObject(csc,DAO_XML_ORACLE);
			scriviClienteOracle(dao, model, MappaturaTabelleOracle.CLI_PROSPECT_RETE);
			
			CogestioneDataManager.scriviCogestione(csc, model);
			
			try {
				dao.executeTableUpdateAccess("tracciaAzioneSalvataggio", model);
			}catch(NoRowsAffected nra) {
				dao.executeTableInsertAccess("tracciaAzioneSalvataggio", model);
			}
			
		}catch(DAOException daoe){
			LOG.error(daoe);
			throw new EJBException(daoe.toString());
		}catch(Exception e){
			LOG.error(e);
			throw new EJBException(e.toString());
		}
	}

	/*****************************************************************************************************/
	/*****************************************************************************************************/
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void inviaInSedeOracle(ClientSessionContext csc, ClienteModel model) throws EJBException {

		try{
			
			DAOObject dao = new DAOObject(csc,DAO_XML_ORACLE);
			
			if(model.getIsPotenziale().booleanValue())
				scriviClienteOracle(dao, model, MappaturaTabelleOracle.CLI_PROSPECT_RETE);
			
			scriviClienteOracle(dao, model, MappaturaTabelleOracle.CLI_ELETTR);
			
			if(model.getIsPotenziale().booleanValue()){
				model.getDatiApplicativi().setNomeTabellaOracle(new StringType(MappaturaTabelleOracle.CLI_PROSPECT_RETE));
				dao.executeTableUpdateAccess("impostaDatiInvioInSede",model);
			}
			
			model.getDatiApplicativi().setNomeTabellaOracle(new StringType(MappaturaTabelleOracle.CLI_ELETTR));
			dao.executeTableUpdateAccess("impostaDatiInvioInSede",model);
		
			CogestioneDataManager.scriviCogestione(csc, model);
			
		}catch(DAOException daoe){
			LOG.error(daoe);
			throw new EJBException(daoe.toString());
		}catch(Exception e){
			LOG.error(e);
			throw new EJBException(e.toString());
		}
	}

	/*****************************************************************************************************/
	/*****************************************************************************************************/
	private void scriviClienteOracle(DAOObject dao, ClienteModel model, String nomeTabellaCli) throws Exception {

		try{
			
						
			// Cliente
			model.getDatiApplicativi().setNomeTabellaOracle(new StringType(nomeTabellaCli));
			try{dao.executeTableUpdateAccess("propostaCliente",model);}catch(NoRowsAffected nra){dao.executeTableInsertAccess("propostaCliente",model);}

			// Indirizzi
			// Residenza
			IndirizzoModel indirizzoResidenza = model.getResidenza().getIndirizzo();
			indirizzoResidenza.getDatiApplicativi().setNomeTabellaOracle(new StringType(MappaturaTabelleOracle.getNomeTabellaIndirizzi(nomeTabellaCli)));
			try{dao.executeTableUpdateAccess("propostaIndirizzo",indirizzoResidenza);}catch(NoRowsAffected nra){dao.executeTableInsertAccess("propostaIndirizzo",indirizzoResidenza);}		

			// Domicilio
			IndirizzoModel indirizzoDomicilio = model.getDomicilio().getIndirizzo();
			indirizzoDomicilio.getDatiApplicativi().setNomeTabellaOracle(new StringType(MappaturaTabelleOracle.getNomeTabellaIndirizzi(nomeTabellaCli)));
			try{dao.executeTableUpdateAccess("propostaIndirizzo",indirizzoDomicilio);}catch(NoRowsAffected nra){dao.executeTableInsertAccess("propostaIndirizzo",indirizzoDomicilio);}
			
			// Recapiti
			// Telefono residenza (abitazione o sede legale)
			TelefonoModel telefono = model.getRecapiti().getTelefonoResidenza();
			telefono.getDatiApplicativi().setNomeTabellaOracle(new StringType(MappaturaTabelleOracle.getNomeTabellaTelefoni(nomeTabellaCli)));
			try{dao.executeTableUpdateAccess("propostaTelefono",telefono);}catch(NoRowsAffected nra){dao.executeTableInsertAccess("propostaTelefono",telefono);}		
			// Telefono domicilio (Solo in censimento)
			if(model.getIsPotenziale().booleanValue()){
				telefono = model.getRecapiti().getTelefonoDomicilio();
				telefono.getDatiApplicativi().setNomeTabellaOracle(new StringType(MappaturaTabelleOracle.getNomeTabellaTelefoni(nomeTabellaCli)));
				try{dao.executeTableUpdateAccess("propostaTelefono",telefono);}catch(NoRowsAffected nra){dao.executeTableInsertAccess("propostaTelefono",telefono);}		
			}
			// Fax
			telefono = model.getRecapiti().getTelefonoFax();
			telefono.getDatiApplicativi().setNomeTabellaOracle(new StringType(MappaturaTabelleOracle.getNomeTabellaTelefoni(nomeTabellaCli)));
			try{dao.executeTableUpdateAccess("propostaTelefono",telefono);}catch(NoRowsAffected nra){dao.executeTableInsertAccess("propostaTelefono",telefono);}
			// Cellulare
			telefono = model.getRecapiti().getTelefonoCellulare();
			telefono.getDatiApplicativi().setNomeTabellaOracle(new StringType(MappaturaTabelleOracle.getNomeTabellaTelefoni(nomeTabellaCli)));
			try{dao.executeTableUpdateAccess("propostaTelefono",telefono);}catch(NoRowsAffected nra){dao.executeTableInsertAccess("propostaTelefono",telefono);}					
			
			return;
			
		}catch(DAOException daoe){
			LOG.error(daoe);
			throw new Exception(daoe.toString());
		}catch(Exception e){
			LOG.error(e);
			throw new Exception(e.toString());
		}
	}

	/*****************************************************************************************************/
	/*****************************************************************************************************/
	@Override
	@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
	public void cancellaOracle(ClientSessionContext csc, ClienteModel cliente) throws EJBException {

		try{
			
			DAOObject dao = new DAOObject(csc,DAO_XML_ORACLE);
			
			try{
				cliente.getDatiApplicativi().setNomeTabellaOracle(new StringType(MappaturaTabelleOracle.TELCLI_PROSPECT_RETE));
				dao.executeTableDeleteChildsAccess("propostaTelefono",cliente);
			}catch(NoRowsAffected nra){}
			
			try{
				cliente.getDatiApplicativi().setNomeTabellaOracle(new StringType(MappaturaTabelleOracle.INDCLI_PROSPECT_RETE));
				dao.executeTableDeleteChildsAccess("propostaIndirizzo",cliente);
			}catch(NoRowsAffected nra){}
			
			try{
				cliente.getDatiApplicativi().setNomeTabellaOracle(new StringType(MappaturaTabelleOracle.CLI_PROSPECT_RETE));
				dao.executeTableDeleteAccess("propostaCliente",cliente);			
			}catch(NoRowsAffected nra){}
			
			CogestioneDataManager.cancellaCogestione(csc, cliente);
			
			cliente.setDataOraUltimaModifica(Tools.now());
			dao.executeTableInsertAccess("tracciaAzioneCancellazione", cliente);
			
		}catch(DAOException daoe){
			LOG.error(daoe);
			throw new EJBException(daoe.toString());
		}catch(Exception e){
			LOG.error(e);
			throw new EJBException(e.toString());
		}
		
	}

	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public ClienteModel salva(ClientSessionContext csc, ClienteModel model) throws EJBException {

		model.setStatoProposta(new StringType(StatiPropostaAnagrafica.BOZZA));		
		model.setStatoConfermato(new StringType("S"));
		
		// Cogestione: switch tra un Fb e l'altro
		CogestioneDataManager.impostaCogestioneInScrittura(csc, model);
		if(model.getConcurrencyViolationFoundedWidth() != null)
			return model;
		
		// Splitto la ragione sociale
		LoaderAnagrafica.splittaRagioneSociale(model);
		
		scriviCliente(csc,model,MappaturaTabelle.CLI_PROSPECT_RETE);
		
		/*************** SCRITTURA SU ORACLE ****************************/
		model.setEsitoScritturaSuOracle("");
		model.setMsgErrScritturaSuOracle("");
		try{
			WriterAnagraficaManager wam = (WriterAnagraficaManager)ROF.getManager(csc, WriterAnagraficaManager.class);
			wam.salvaOracle(csc, model);
			model.setEsitoScritturaSuOracle("OK");
		}catch(Throwable t){
			model.setEsitoScritturaSuOracle("KO");
			model.setMsgErrScritturaSuOracle(t.toString());
		}
		/****************************************************************/
		
		// Ripristino la ragione sociale
		LoaderAnagrafica.componiRagioneSociale(model);
		
		return model;
		
	}

	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public ClienteModel salvaFotografia(ClientSessionContext csc, ClienteModel model) throws EJBException {
		
		DAOObject dao = null;
		try{
			
			dao = new DAOObject(csc,getNomeDAO(csc));
			dao.openConnection();
			
			ContatoreModel contatore = new ContatoreModel();
			contatore.setNomeRisorsa(new StringType(Costanti.NOME_RISORSA_FOTO_CLIENTE));
			String filledUserCode = Tools.fillSx(csc.getUserCode().toUpperCase(),'0',10);
			contatore.setUtente(new StringType(filledUserCode));
			contatore.setIncremento(new IntegerType());
			DAOCallableResultModel callRes = dao.executeCallableAccess("getContatore",contatore);
			if(callRes.getResult() != 0)
				throw new EJBException("Errore ["+callRes.getResult()+"] nel prendere il contatore per la fotografia");
			model.getFotografia().setIdInforete(contatore.getProgressivo());
			model.getFotografia().setDataUpload(Tools.now());
			dao.executeTableInsertAccess("fotografia",model);				
			return model;
			
		}catch(DAOException daoe){
			String errorMsg = " Eccezione DAO nel salvare la foto per ["+model.getCodFiscale()+"] "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = " Eccezione DAO nel salvare la foto per ["+model.getCodFiscale()+"] "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}finally{
			if(dao != null) dao.closeConnection();
		}
			
	}

	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public ClienteModel conferma(ClientSessionContext csc, ClienteModel model) throws EJBException {

		model.setStatoProposta(new StringType(StatiPropostaAnagrafica.INVIATA_IN_SEDE));
		model.setStatoConfermato(new StringType(Costanti.VALORE_FLAG_STATO_CONFERMATO));
		
		// Cogestione: switch tra un Fb e l'altro
		CogestioneDataManager.impostaCogestioneInScrittura(csc, model);
		if(model.getConcurrencyViolationFoundedWidth() != null)
			return model;
			
		// Splitto la ragione sociale
		LoaderAnagrafica.splittaRagioneSociale(model);
		
		scriviCliente(csc,model,MappaturaTabelle.CLI_PROSPECT_RETE);
		
		/*************** SCRITTURA SU ORACLE ****************************/
		model.setEsitoScritturaSuOracle("");
		model.setMsgErrScritturaSuOracle("");
		try{
			WriterAnagraficaManager wam = (WriterAnagraficaManager)ROF.getManager(csc, WriterAnagraficaManager.class);
			wam.salvaOracle(csc, model);
			model.setEsitoScritturaSuOracle("OK");
		}catch(Throwable t){
			model.setEsitoScritturaSuOracle("KO");
			model.setMsgErrScritturaSuOracle(t.toString());
		}
		/****************************************************************/
		
		// Ripristino la ragione sociale
		LoaderAnagrafica.componiRagioneSociale(model);
		
		return model;
		
	}

	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public ClienteModel inviaInSede(ClientSessionContext csc, ClienteModel model) throws EJBException {

		model.setStatoProposta(new StringType(StatiPropostaAnagrafica.INVIATA_IN_SEDE));
		model.setStatoConfermato(new StringType("S"));
		model.getDatiApplicativi().setFlagClienteSegnalato(new StringType()); // Ripulisco il flag che mi faceva capire se il cliente è un segnalato M4U
		
		// Cogestione: switch tra un Fb e l'altro
		CogestioneDataManager.impostaCogestioneInScrittura(csc, model);
		if(model.getConcurrencyViolationFoundedWidth() != null)
			return model;
			
		// Splitto la ragione sociale
		LoaderAnagrafica.splittaRagioneSociale(model);
		
		if(model.getIsPotenziale().booleanValue())
			scriviCliente(csc,model,MappaturaTabelle.CLI_PROSPECT_RETE);
			
		scriviCliente(csc,model,MappaturaTabelle.CLI_ELETTR);
		
		// La dataVariazioneFirmaDigitale valorizzata indica che il ciclo di vita dell'anagrafica è terminato, cosa
		// sempre vera per le variaioni in FD
		model.setDataVariazioneFirmaDigitale(new TimestampType());
		if(model.isDigitale() && model.getIsEffettivo().booleanValue()){
			model.setDataVariazioneFirmaDigitale(Tools.now());
			model.setIsDaInviareAllaFabbrica(new BooleanType(false)); // Le variazioni in FD non devono essere passate a CSC
		}
			
		try{
			DAOObject dao = new DAOObject(csc,getNomeDAO(csc));
			
			if(model.getIsPotenziale().booleanValue()){
				model.getDatiApplicativi().setNomeTabella(new StringType(MappaturaTabelle.CLI_PROSPECT_RETE));
				dao.executeTableUpdateAccess("impostaDatiInvioInSede",model);
			}
			
			model.getDatiApplicativi().setNomeTabella(new StringType(MappaturaTabelle.CLI_ELETTR));
			dao.executeTableUpdateAccess("impostaDatiInvioInSede",model);
			
			if(!model.getCodDisposizione().isNull()) { // La nostra firma digitale utilizza il contratto elettronico
				
				DatiVariazioneModel dati = new DatiVariazioneModel();
				dati.setCodiceVariazione(model.getCodDisposizione());
				// Copernico
				if ( model.getDynamicData().readProperty("validatore") != null && model.getDynamicData().readProperty("validatore").equals("1")) {
					
					// aggiorna stato = 13 (accettato) su CEPE_CONTRATTI_ELETTRONICI
					dati.setStatoVariazione(new StringType("13"));
					dati.setDataValidazione(Tools.now());
					
					dao.executeTableUpdateAccess("aggiornaContrattiElettronici",dati);
					
					// aggiorna pdf						
					PdfVariazioneModel pdfmodel = new PdfVariazioneModel();
					pdfmodel.setCodiceVariazione(model.getCodDisposizione());
					DAOObject daoCopernico = new DAOObject(csc, "ItaAnagraficaClienti.Copernico");
					daoCopernico.executeTableLoadAccess("pdfVariazione", pdfmodel);	
					InputStream pdfis = pdfmodel.getPdfFile().getInputStream();						
					DatiStampa ds = new DatiStampa();
					ds.addProperty("codiceVariazione",new StringType(model.getCodMediolanum()+"-"+model.getProgressivo()));
					pdfmodel.setDatiStampa(ds);
					ByteArrayOutputStream pdfos = AdobeFormCompiler.fillAdobeForm(pdfis, pdfmodel);
					pdfis.close();
					pdfmodel.getPdfFile().setFileContent(pdfos.toByteArray());
					daoCopernico.executeTableUpdateAccess("pdfVariazione", pdfmodel);					
				} else {
					// aggiorna stato = 3 (inviato in sede) su CEPE_CONTRATTI_ELETTRONICI
					dati.setStatoVariazione(new StringType("03"));
					dao.executeTableUpdateAccess("aggiornaContrattiElettronici",dati);
				}

			}
		}catch(DAOException daoe){
			LOG.error(daoe);
			throw new EJBException(daoe.toString());
		}catch(Exception e){
			LOG.error(e);
			throw new EJBException(e.toString());			
		}

		/*************** SCRITTURA SU ORACLE ****************************/
		model.setEsitoScritturaSuOracle("");
		model.setMsgErrScritturaSuOracle("");
		try{
			WriterAnagraficaManager wam = (WriterAnagraficaManager)ROF.getManager(csc, WriterAnagraficaManager.class);
			wam.inviaInSedeOracle(csc, model);
			model.setEsitoScritturaSuOracle("OK");
		}catch(Throwable t){
			model.setEsitoScritturaSuOracle("KO");
			model.setMsgErrScritturaSuOracle(t.toString());
		}
		/****************************************************************/
		
		/****************************** PRIT *******************************************/
		if(!model.isDigitale() && !model.isSaltaPritAllInvioInSede()){
			try{
				if(model.getIsPotenziale().booleanValue())
					inserisciPritCensimento(csc,model);
				else
					inserisciPritVariazione(csc,model);
			}catch(Exception e){			
				String errorMsg = " Eccezione nel inserire il prit per il cliente ";
				   errorMsg += "codAgente=["+model.getCodAgente()+"] ";
				   errorMsg += "codMediolanum=["+model.getCodMediolanum()+"] ";
				   errorMsg += "codPotenziale=["+model.getCodPotenziale()+"] ";
				   errorMsg += "codFiscale=["+model.getCodFiscale()+"] ";
				   errorMsg += "codPartitaIva=["+model.getPartitaIva()+"]: "+e;
				EJBException ejbe = new EJBException(errorMsg);
				LOG.error(ejbe);
				throw ejbe;
			}
		}
		/*******************************************************************************/
		
		// Ripristino la ragione sociale
		LoaderAnagrafica.componiRagioneSociale(model);
		
		return model;
		
	}

	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public VariazioneKeyModel cancella(ClientSessionContext csc, VariazioneKeyModel chiave, String nomeTabella) throws EJBException {

		DAOObject dao = null;
		try{
			
			dao = new DAOObject(csc,getNomeDAO(csc));
			dao.openConnection();
			
			ClienteModel cliente = new ClienteModel();
			Tools.copyCommandDataModel(chiave,cliente);

			cliente.getDatiApplicativi().setNomeTabella(new StringType(nomeTabella));
			dao.executeTableLoadAccess("propostaCliente",cliente);
			if(!cliente.getStatoProposta().equals(StatiPropostaAnagrafica.BOZZA))
				return chiave;
			
			try{
				cliente.getDatiApplicativi().setNomeTabella(new StringType(MappaturaTabelle.getNomeTabellaTelefoni(nomeTabella)));
				dao.executeTableDeleteChildsAccess("propostaTelefono",cliente);
			}catch(NoRowsAffected nra){}
			
			try{
				cliente.getDatiApplicativi().setNomeTabella(new StringType(MappaturaTabelle.getNomeTabellaIndirizzi(nomeTabella)));
				dao.executeTableDeleteChildsAccess("propostaIndirizzo",cliente);
			}catch(NoRowsAffected nra){}
			
			try{
				cliente.getDatiApplicativi().setNomeTabella(new StringType(nomeTabella));
				dao.executeTableDeleteAccess("propostaCliente",cliente);			
			}catch(NoRowsAffected nra){}
			
			/*************** SCRITTURA SU ORACLE ****************************/
			chiave.setEsitoScritturaSuOracle("");
			chiave.setMsgErrScritturaSuOracle("");
			try{
				WriterAnagraficaManager wam = (WriterAnagraficaManager)ROF.getManager(csc, WriterAnagraficaManager.class);
				wam.cancellaOracle(csc, cliente);
				chiave.setEsitoScritturaSuOracle("OK");
			}catch(Throwable t){
				chiave.setEsitoScritturaSuOracle("KO");
				chiave.setMsgErrScritturaSuOracle(t.toString());
			}
			/****************************************************************/
			
			return chiave;
			
		}catch(DAOException daoe){
			String errorMsg = " Eccezione DAO nel salvare il cliente ";
				   errorMsg += "codAgente=["+chiave.getCodAgente()+"] ";
				   errorMsg += "codMediolanum=["+chiave.getCodMediolanum()+"] ";
				   errorMsg += "codPotenziale=["+chiave.getCodPotenziale()+"] ";
				   errorMsg += "codFiscale=["+chiave.getCodFiscale()+"] ";
				   errorMsg += "codPartitaIva=["+chiave.getPartitaIva()+"]: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = " Eccezione nel salvare il cliente ";
				   errorMsg += "codAgente=["+chiave.getCodAgente()+"] ";
				   errorMsg += "codPotenziale=["+chiave.getCodPotenziale()+"] ";
				   errorMsg += "codFiscale=["+chiave.getCodFiscale()+"] ";
				   errorMsg += "codPartitaIva=["+chiave.getPartitaIva()+"]: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}finally{
			if(dao != null) dao.closeConnection();
		}
	}

	/*****************************************************************************************************/
	/*****************************************************************************************************/
	private void scriviCliente(ClientSessionContext csc, ClienteModel model, String nomeTabella) throws EJBException{
		
		DAOObject dao = null;
		try{
			
			dao = new DAOObject(csc,getNomeDAO(csc));
			dao.openConnection();
			
			model.getDatiApplicativi().setNomeTabella(new StringType(nomeTabella));

			int progr = Costanti.PROGRESSIVO_INIZIALE_SEDE;
			if (model.getIsEffettivo().booleanValue()) {
				progr = ultimoProgressivoVariazioni(csc, model) + 1;
				if (progr < Costanti.PROGRESSIVO_INIZIALE_SEDE) {
					progr = Costanti.PROGRESSIVO_INIZIALE_SEDE;
				}
			}
			model.setProgressivo(new IntegerType(progr));

			if(model.getIsPotenziale().booleanValue())
				model.setDataInserimento(Tools.now());
			model.setDataVariazione(Tools.now());

			impostaDatiInScrittura(model);
			
			if(model.getCodPotenziale().isNull()){				
				
				StringType codPotenziale = getContatore(csc);
				model.setCodPotenziale(codPotenziale);
				model.setCodInforete(model.getCodPotenziale());
				dao.executeTableInsertAccess("propostaCliente",model);
				
			}else{
				
				try{
					dao.executeTableUpdateAccess("propostaCliente",model);
				}catch(NoRowsAffected nra){
					dao.executeTableInsertAccess("propostaCliente",model);				
				}
				
			}
			
			scriviResidenza(dao,model);
			scriviDomicilio(dao,model);
			scriviRecapiti(dao,model);
			
			model.getDatiApplicativi().setRefreshable(true);
			return;
			
		}catch(DAOException daoe){
			String errorMsg = " Eccezione DAO nel salvare il cliente ";
				   errorMsg += "codAgente=["+model.getCodAgente()+"] ";
				   errorMsg += "codMediolanum=["+model.getCodMediolanum()+"] ";
				   errorMsg += "codPotenziale=["+model.getCodPotenziale()+"] ";
				   errorMsg += "codFiscale=["+model.getCodFiscale()+"] ";
				   errorMsg += "codPartitaIva=["+model.getPartitaIva()+"]: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = " Eccezione nel salvare il cliente ";
				   errorMsg += "codAgente=["+model.getCodAgente()+"] ";
				   errorMsg += "codMediolanum=["+model.getCodMediolanum()+"] ";
				   errorMsg += "codPotenziale=["+model.getCodPotenziale()+"] ";
				   errorMsg += "codFiscale=["+model.getCodFiscale()+"] ";
				   errorMsg += "codPartitaIva=["+model.getPartitaIva()+"]: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}finally{
			if(dao != null) dao.closeConnection();
		}
	}

	/********************************************************************************************************/
	/********************************************************************************************************/
	private void impostaDatiInScrittura(ClienteModel model) {
		
		// Natura giuridica
		if(model.getIsDatoreDiLavoro().booleanValue()){
			model.setNaturaGiuridica(new StringType(Costanti.NATURA_GIURIDICA_DATORE_DI_LAVORO));
			model.setTipoPersona(new StringType(Costanti.TIPO_PERSONA_GIURIDICA));
			model.setSesso(new StringType(Costanti.SESSO_SOCIETA));
			model.getInfoPersonali().setCodSottogruppoAttivita(new StringType(Costanti.CODICE_SOTTOGRUPPO_ATTIVITA_DATORI_DI_LAVORO));
			model.getInfoPersonali().setCodGruppoAttivita(new StringType(Costanti.CODICE_GRUPPO_ATTIVITA_DATORI_DI_LAVORO));
		}else if(model.getIsDitta().booleanValue()) {
			model.setNaturaGiuridica(new StringType("S"+model.getSesso().toString().toUpperCase()+model.getSesso().toString().toUpperCase()));
		}else if(model.getIsPersonaFisica().booleanValue()) {
			model.setNaturaGiuridica(new StringType("F"+model.getSesso().toString().toUpperCase()+model.getSesso().toString().toUpperCase()));
		}
		
		// Comune di nascita estero
		if(!model.getComuneNascita().getCodNazione().equalsIgnoreCase(Costanti.COD_NAZIONE_ITALIA)){
			model.getComuneNascita().setComune(model.getComuneNascita().getComuneEstero());
			model.getComuneNascita().setCodComune(new StringType());
			model.getComuneNascita().setProvincia(new StringType("EE"));
		}
		
		// Se una delle 2 cittadinanze è USA allora la seconda residenza fiscale deve essere USA
		if(model.getCittadinanza().equals(Costanti.COD_NAZIONE_US_UIC) || model.getSecondaCittadinanza().equals(Costanti.COD_NAZIONE_US_UIC))
			model.getResidenza().setCodNazioneResidenzaFiscale2(new StringType(Costanti.COD_NAZIONE_US_UIC));
		
		//////////////////////////////////////////////////////////////////////////////////////
		// Per i datori di lavoro imposto a TBN il campo "Segnalatore"
		// E' una porcheria che non centra niente con il segnalatore ma ho dovuto farla!!
		if(model.getIsDatoreDiLavoro().booleanValue())
			model.getInfoPersonali().setCodOrigineECodCliente(new StringType("TBN"));
		//////////////////////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////////////
				
		//////////////////////////////////////////////////////////////////////////////////////
		// Altra porcheria!
		// Il campo CLI_C_LUOGO_PROF contiene il codice UIC della nazione se != Italia
		// altrimenti il codice provincia
		model.getInfoPersonali().setNazOProvSvolgProf(new StringType());
		if(!model.getInfoPersonali().getNazioneSvolgimentoProfessione().isNull()){
			if(model.getInfoPersonali().getNazioneSvolgimentoProfessione().equals(Costanti.COD_UIC_NAZIONE_ITALIA))
				model.getInfoPersonali().setNazOProvSvolgProf(new StringType(model.getInfoPersonali().getProvinciaSvolgimentoProfessione().toString()));
			else
				model.getInfoPersonali().setNazOProvSvolgProf(new StringType(model.getInfoPersonali().getNazioneSvolgimentoProfessione().toString()));
		}
		//////////////////////////////////////////////////////////////////////////////////////
		//////////////////////////////////////////////////////////////////////////////////////
		
		// -CaricaPubblicaRicoperta <-> HaCarichePubbliche
		if(!model.getAdempimentiNormativi().getHaCarichePubbliche().isNull() && !model.getAdempimentiNormativi().getHaCarichePubbliche().equals("S"))
			model.getAdempimentiNormativi().setCaricaPubblicaRicoperta(new StringType(Costanti.CODICE_CARICA_PUBBLICA_RICOPERTA_NO));
		
		// -TipologiaLegameAffariDiversoDaAttivitaPrincipale <-> HaLegamiAffariDiversiDaAttivitaPrincipale				
		if(!model.getAdempimentiNormativi().getHaLegamiAffariDiversiDaAttivitaPrincipale().isNull() && !model.getAdempimentiNormativi().getHaLegamiAffariDiversiDaAttivitaPrincipale().equals("S"))
			model.getAdempimentiNormativi().setTipologiaLegameAffariDiversoDaAttivitaPrincipale(new StringType(Costanti.CODICE_TIPOLOGIA_LEGAME_AFFARI_DIVERSO_ATTIVITA_PRINCIPALE_NO));
			
	}
	
	/********************************************************************************************************/
	/********************************************************************************************************/
	private void scriviResidenza(DAOObject dao, ClienteModel model) throws DAOException{

		// Scrivo l'indirizzo di residenza sulla tabella relativa a quella che sto gestendo
		StringType nomeTabella = new StringType(MappaturaTabelle.getNomeTabellaIndirizzi(model.getDatiApplicativi().getNomeTabella().toString()));

		IndirizzoModel indirizzoResidenza = model.getResidenza().getIndirizzo();
		indirizzoResidenza.getDatiApplicativi().setNomeTabella(nomeTabella);
		inizializzaIndirizzo(model,indirizzoResidenza,1);
		
		// #119648: il campo comunqe viene tolto dal front-end e quindi lo valorizziamo con quanto letto solo se la località non è stata modificata
		// altrimenti lo annulliamo in quanto non deducibile dalla località inserita
		// In censimento sarà sempre "null"
		if(model.getIsPotenziale().booleanValue()){
			indirizzoResidenza.setDescrizioneComune(new StringType());
		}else{
			ClienteModel clienteCaricato = model.getDatiApplicativi().getClienteCaricato();
			if(clienteCaricato != null && !clienteCaricato.getResidenza().getIndirizzo().getComune().toString().equalsIgnoreCase(indirizzoResidenza.getComune().toString()))
				indirizzoResidenza.setDescrizioneComune(new StringType());
		}
		
		try{
			dao.executeTableUpdateAccess("propostaIndirizzo",indirizzoResidenza);
		}catch(NoRowsAffected nra){
			dao.executeTableInsertAccess("propostaIndirizzo",indirizzoResidenza);
		}		

	}

	/********************************************************************************************************/
	/********************************************************************************************************/
	private void scriviDomicilio(DAOObject dao, ClienteModel model) throws DAOException{

		// Scrivo l'indirizzo di domicilio sulla tabella relativa a quella che sto gestendo
		StringType nomeTabella = new StringType(MappaturaTabelle.getNomeTabellaIndirizzi(model.getDatiApplicativi().getNomeTabella().toString()));

		IndirizzoModel indirizzoDomicilio = model.getDomicilio().getIndirizzo();
		indirizzoDomicilio.getDatiApplicativi().setNomeTabella(nomeTabella);
		inizializzaIndirizzo(model,indirizzoDomicilio,2);

		// #119648: il campo comunqe viene tolto dal front-end e quindi lo valorizziamo con quanto letto solo se la località non è stata modificata
		// altrimenti lo annulliamo in quanto non deducibile dalla località inserita
		// In censimento sarà sempre "null"
		if(model.getIsPotenziale().booleanValue()){
			indirizzoDomicilio.setDescrizioneComune(new StringType());
		}else{
			ClienteModel clienteCaricato = model.getDatiApplicativi().getClienteCaricato();
			if(clienteCaricato != null && !clienteCaricato.getDomicilio().getIndirizzo().getComune().toString().equalsIgnoreCase(indirizzoDomicilio.getComune().toString()))
				indirizzoDomicilio.setDescrizioneComune(new StringType());
		}
		
		try{
			dao.executeTableUpdateAccess("propostaIndirizzo",indirizzoDomicilio);
		}catch(NoRowsAffected nra){
			dao.executeTableInsertAccess("propostaIndirizzo",indirizzoDomicilio);
		}		

	}

	/********************************************************************************************************/
	/********************************************************************************************************/
	private void scriviRecapiti(DAOObject dao, ClienteModel model) throws DAOException{

		// Scrivo il telefono sulla tabella relativa a quella che sto gestendo
		StringType nomeTabella = new StringType(MappaturaTabelle.getNomeTabellaTelefoni(model.getDatiApplicativi().getNomeTabella().toString()));

		// Telefono residenza (abitazione o sede legale)
		TelefonoModel telefono = model.getRecapiti().getTelefonoResidenza();
		telefono.getDatiApplicativi().setNomeTabella(nomeTabella);
		inizializzaTelefono(model,telefono,1);
		try{
			dao.executeTableUpdateAccess("propostaTelefono",telefono);
		}catch(NoRowsAffected nra){
			dao.executeTableInsertAccess("propostaTelefono",telefono);
		}		

		// Telefono domicilio (Solo in censimento)
		if(model.getIsPotenziale().booleanValue()){
			telefono = model.getRecapiti().getTelefonoDomicilio();
			telefono.getDatiApplicativi().setNomeTabella(nomeTabella);
			inizializzaTelefono(model,telefono,2);
			try{
				dao.executeTableUpdateAccess("propostaTelefono",telefono);
			}catch(NoRowsAffected nra){
				dao.executeTableInsertAccess("propostaTelefono",telefono);
			}		
		}

		// Fax
		telefono = model.getRecapiti().getTelefonoFax();
		telefono.getDatiApplicativi().setNomeTabella(nomeTabella);
		inizializzaTelefono(model,telefono,3);
		try{
			dao.executeTableUpdateAccess("propostaTelefono",telefono);
		}catch(NoRowsAffected nra){
			dao.executeTableInsertAccess("propostaTelefono",telefono);
		}
		
		// Cellulare
		telefono = model.getRecapiti().getTelefonoCellulare();
		telefono.getDatiApplicativi().setNomeTabella(nomeTabella);
		inizializzaTelefono(model,telefono,4);
		try{
			dao.executeTableUpdateAccess("propostaTelefono",telefono);
		}catch(NoRowsAffected nra){
			dao.executeTableInsertAccess("propostaTelefono",telefono);
		}		
	}

	/********************************************************************************************************/
	/********************************************************************************************************/
	private StringType getContatore(ClientSessionContext csc) throws Exception{

		DAOObject dao = null;
		try{
			
			dao = new DAOObject(csc,getNomeDAO(csc));			
			dao.openConnection();
			
			String filledUserCode = Tools.fillSx(csc.getUserCode().toUpperCase(),'0',10);

			ContatoreModel model = new ContatoreModel();
			model.setNomeRisorsa(new StringType(Costanti.NOME_RISORSA));
			model.setUtente(new StringType(filledUserCode));
			model.setIncremento(new IntegerType());
			DAOCallableResultModel callRes = dao.executeCallableAccess("getContatore",model);
			if(callRes.getResult() != 0)
				throw new Exception("Errore ["+callRes.getResult()+"] nel prendere il contatore per l'anagrafica");
			return model.getProgressivo();
			
		}catch(DAOException daoe){
			String errorMsg = "Eccezione DAO nel prendere il contatore per l'anagrafica: "+daoe;
			Exception e = new Exception(errorMsg);
			throw e;
		}catch(Exception e){
			String errorMsg = "Eccezione nel prendere il contatore per l'anagrafica: "+e;
			e = new Exception(errorMsg);
			throw e;
		}finally{
			if(dao != null) dao.closeConnection();
		}
	}	
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean inizializzaIndirizzo(ClienteModel model, IndirizzoModel indirizzo,int propressivo){ 
												
		boolean res = false;

		// Se impostato lasciamo il progressivo valorizzato in lettura
		if(indirizzo.getProgressivo().isNull()){
			indirizzo.setProgressivo(new IntegerType(propressivo));
			res = true;
		}

		indirizzo.setCodAgente(model.getCodAgente());
		indirizzo.setCodPotenziale(model.getCodPotenziale());
		indirizzo.setCodRete(model.getCodRete());
		indirizzo.setStato(model.getStato());
		indirizzo.setStatoProposta(model.getStatoProposta());
		indirizzo.setServerReplica(model.getServerReplica());
		indirizzo.setDataVariazione(Tools.now());		
		indirizzo.setProgressivoProposta(model.getProgressivo());
		
		if(!indirizzo.getCodNazione().equalsIgnoreCase(Costanti.COD_NAZIONE_ITALIA)){
			indirizzo.setComune(indirizzo.getComuneEstero());
			indirizzo.setCodComune(new StringType());
			indirizzo.setCap(new StringType());
			indirizzo.setProvincia(new StringType("EE"));
			indirizzo.setToponimoIndirizzo(new StringType());
		}
		
		// Per favorire la replica degli indirizzi dei clienti presale
		indirizzo.setStatoConfermato(model.getStatoConfermato());
		
		return res;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean inizializzaTelefono(ClienteModel model, TelefonoModel telefono, int propressivo){ 
											   
		boolean res = false;
		
		if(telefono.getProgressivo().isNull()){
			telefono.setProgressivo(new IntegerType(propressivo));
			res = true;
		}

		telefono.setCodAgente(model.getCodAgente());
		telefono.setCodPotenziale(model.getCodPotenziale());
		telefono.setCodRete(model.getCodRete());
		telefono.setStato(model.getStato());
		telefono.setStatoProposta(model.getStatoProposta());
		telefono.setServerReplica(model.getServerReplica());
		telefono.setDataVariazione(Tools.now());			
		telefono.setProgressivoProposta(model.getProgressivo());
		
		String t = "";
		if(!telefono.getReperibileOraDa().isNull())
			t = Costanti.TIMESTAMP_PRE+telefono.getReperibileOraDa().toString()+Costanti.TIMESTAMP_SUF;
		telefono.setReperibileOraDaDB(new TimestampType(t));

		t = "";
		if(!telefono.getReperibileOraA().isNull())
			t = Costanti.TIMESTAMP_PRE+telefono.getReperibileOraA().toString()+Costanti.TIMESTAMP_SUF;
		telefono.setReperibileOraADB(new TimestampType(t));
		
		// Per favorire la replica degli indirizzi dei clienti presale
		telefono.setStatoConfermato(model.getStatoConfermato());
		
		return res;
	}
	
	/***********************************************************************************************/
	private static final String errorePrit = "<li style='color:red;'>Si &egrave verificato un errore nell'inserimento della/e righe di Prit. Provare ad inserirle tramite l'applicazione Prit Promotore</li>"; 
	private static final String errorePritCensimento = "<li style='color:red;'>Si &egrave verificato un errore nell'inserimento della riga di Prit relativa al censimento. Provare ad inserirla tramite l'applicazione Prit Promotore</li>";
	private static final String errorePritVariazione = "<li style='color:red;'>Si &egrave verificato un errore nell'inserimento della riga di Prit relativa alla variazione. Provare ad inserirla tramite l'applicazione Prit Promotore</li>";
	private static final String remindPritPrivacy = "<li>Allegare la scheda privacy</li>";
	private static final String errorePritPrivacy = "<li style='color:red;'>Si &egrave verificato un errore nell'inserimento della riga di Prit relativa alla scheda privacy. Provare ad inserirla tramite l'applicazione Prit Promotore</li>";
	private static final String remindPritCCIAA = "<li>Allegare l'Iscrizione alla Camera di Commercio</li>";
	private static final String errorePritCCIAA = "<li style='color:red;'>Si &egrave verificato un errore nell'inserimento della riga di Prit relativa all'Iscrizione alla Camera di Commercio. Provare ad inserirla tramite l'applicazione Prit Promotore</li>";
	private static final String remindPritCoupon = "<li>Allegare il Coupon &rsquo;Presenta un amico&rsquo;</li>";
	private static final String errorePritCoupon = "<li style='color:red;'>Si &egrave verificato un errore nell'inserimento della riga di Prit relativa al Coupon &rsquo;Presenta un amico&rsquo;. Provare ad inserirla tramite l'applicazione Prit Promotore</li>";
	
	private static final String remindPritW9Fatca = "<li>Allegare il modulo W-9</li>";
	private static final String errorePritW9Fatca = "<li style='color:red;'>Si &egrave verificato un errore nell'inserimento della riga di Prit relativa al modulo W-9. Provare ad inserirla tramite l'applicazione Prit Promotore</li>";
	private static final String remindPritAllegatiFatca = "<li>Allegare gli allegati previsti</li>";
	
	/***********************************************************************************************/
	private void inserisciPritCensimento(ClientSessionContext csc, ClienteModel model){
		
		if(model.getIsDatoreDiLavoro().booleanValue())
			return;
		
		InfoPritModel infoPritModel = null;
		
		String codAgente = Tools.fillSx(model.getAgente().getCodAgente().toString(),'0',10);
		Contratto contratto = new Contratto();
		contratto.setCodInforete(model.getCodPotenziale().toString());
		contratto.setNumeroContratto(model.getNumeroOrdine().toString());

		Cliente cliente = new Cliente();
		cliente.setCognome(model.getCognome().toString());
		cliente.setNome(model.getNome().toString());
		
		try{

			try{
				// ********* RIGA CONTRATTO ***********//
				infoPritModel = InfoLoader.getInfoPrit(csc,new StringType(Costanti.CODICE_PRODOTTO),new StringType(Costanti.CHIAVE_PRIT_CENSIMENTO));
				contratto.setCodProdotto(Integer.parseInt(infoPritModel.getPritCodProdotto().toString()));
				contratto.setCodOperazione(Integer.parseInt(infoPritModel.getPritCodOperazione().toString()));
				DacServiceCaller.inserisciDocumento(csc, codAgente, 
													Costanti.NOME_RISORSA,contratto,cliente,null);
			}catch(Throwable tr){
				model.getDatiApplicativi().appendMessaggioCentrale(errorePritCensimento);
			}
			
			// ********* RIGA PRIVACY ***********//
			rigaPritPrivacy(csc,model,cliente,contratto);
			
			// ********* RIGHE FATCA ***********//
			righePritFatca(csc,model,cliente,contratto);
			
			// ********* RIGA CCIAA PER DITTE ***********//
			if(model.getIsDitta().booleanValue() && !model.getInfoDitta().getNumeroIscrizioneREA().isNull()){
				
				try{
					infoPritModel = InfoLoader.getInfoPrit(csc, new StringType(Costanti.CODICE_PRODOTTO), new StringType(Costanti.CHIAVE_PRIT_CCIAA));
					contratto.setCodProdotto(Integer.parseInt(infoPritModel.getPritCodProdotto().toString()));
					contratto.setCodOperazione(Integer.parseInt(infoPritModel.getPritCodOperazione().toString()));
					DacServiceCaller.inserisciDocumento(csc, codAgente, 
							   							Costanti.NOME_RISORSA, contratto,cliente,null);
					model.getDatiApplicativi().appendMessaggioCentrale(remindPritCCIAA);
				}catch(Throwable tr){
					model.getDatiApplicativi().appendMessaggioCentrale(errorePritCCIAA);
				}
				
			}
			
			// ********* RIGA COUPON MGM SE PRESENTATO MEMBER GET MEMBER ***********//
			if(model.getInfoPersonali().getCodOrigine().equals(Costanti.CODICE_ORIGINE_MEMBER_GET_MEMBER)){
				try{					
					if (model.getIsDitta().booleanValue()){
						infoPritModel = InfoLoader.getInfoPrit(csc, new StringType(Costanti.CODICE_PRODOTTO), new StringType(Costanti.CHIAVE_PRIT_COUPON_PRESENTA_UN_AMICO));
					}else{
						infoPritModel = InfoLoader.getInfoPrit(csc, new StringType(Costanti.CODICE_PRODOTTO), new StringType(Costanti.CHIAVE_PRIT_COUPON_PROMOZIONE_MGM));
					}
					contratto.setCodProdotto(Integer.parseInt(infoPritModel.getPritCodProdotto().toString()));
					contratto.setCodOperazione(Integer.parseInt(infoPritModel.getPritCodOperazione().toString()));
					DacServiceCaller.inserisciDocumento(csc, codAgente, 
							   							Costanti.NOME_RISORSA,contratto,cliente,null);
					model.getDatiApplicativi().appendMessaggioCentrale(remindPritCoupon);
					
				}catch(Throwable tr){
					model.getDatiApplicativi().appendMessaggioCentrale(errorePritCoupon);
				}
			}
			
			// ********* RIGA COUPON MGM SE PRESENTATO IMF ***********//
			if(model.getInfoPersonali().getCodOrigine().equals(Costanti.CODICE_ORIGINE_IMF)){
				try{					
					infoPritModel = InfoLoader.getInfoPrit(csc, new StringType(Costanti.CODICE_PRODOTTO), new StringType(Costanti.CHIAVE_PRIT_COUPON_PROMOZIONE_MGM));
					contratto.setCodProdotto(Integer.parseInt(infoPritModel.getPritCodProdotto().toString()));
					contratto.setCodOperazione(Integer.parseInt(infoPritModel.getPritCodOperazione().toString()));
					DacServiceCaller.inserisciDocumento(csc, codAgente, 
							   							Costanti.NOME_RISORSA,contratto,cliente,null);
					model.getDatiApplicativi().appendMessaggioCentrale(remindPritCoupon);
				}catch(Throwable tr){
					model.getDatiApplicativi().appendMessaggioCentrale(errorePritCoupon);
				}
			}
			
		}catch(Throwable tr){
			model.getDatiApplicativi().appendMessaggioCentrale(errorePrit);
		}

	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void inserisciPritVariazione(ClientSessionContext csc, ClienteModel model){

		if(model.getIsDatoreDiLavoro().booleanValue())
			return;
		
		InfoPritModel infoPritModel = null;
		
		String codAgente = Tools.fillSx(model.getAgente().getCodAgente().toString(),'0',10);
		Contratto contratto = new Contratto();
		contratto.setCodInforete(model.getCodMediolanum().toString() + "-" + model.getProgressivo().toString());
		contratto.setNumeroContratto(model.getNumeroOrdine().toString());

		Cliente cliente = new Cliente();
		cliente.setCognome(model.getCognome().toString());
		cliente.setNome(model.getNome().toString());
		cliente.setCodMediolanum(model.getCodMediolanum().toString());
		try{
			
			try{
				// ********* RIGA CONTRATTO ***********//
				infoPritModel = InfoLoader.getInfoPrit(csc, new StringType(Costanti.CODICE_PRODOTTO), new StringType(Costanti.CHIAVE_PRIT_VARIAZIONE));
				contratto.setCodProdotto(Integer.parseInt(infoPritModel.getPritCodProdotto().toString()));
				contratto.setCodOperazione(Integer.parseInt(infoPritModel.getPritCodOperazione().toString()));
				DacServiceCaller.inserisciDocumento(csc, codAgente, 
													Costanti.NOME_RISORSA,contratto,cliente,null);
			}catch(Throwable tr){
				model.getDatiApplicativi().appendMessaggioCentrale(errorePritVariazione);			
			}
			
			// ********* RIGA PRIVACY SE NECESSARIA ***********//
			DatiPrivacyModel privacyOriginale = model.getDatiApplicativi().getClienteOriginale().getDatiPrivacy();
			if(!model.getDatiPrivacy().getFlagCarte().equals(privacyOriginale.getFlagCarte())             ||
			   !model.getDatiPrivacy().getFlagLiberatoria().equals(privacyOriginale.getFlagLiberatoria()) ||
			   !model.getDatiPrivacy().getFlagExtraUE().equals(privacyOriginale.getFlagExtraUE()) ||
			   !model.getDatiPrivacy().getFlagProfilazione().equals(privacyOriginale.getFlagProfilazione())){
				rigaPritPrivacy(csc,model,cliente,contratto);
			}

			// ********* RIGHE FATCA ***********//
			righePritFatca(csc,model,cliente,contratto);
			
		}catch(Throwable tr){
			model.getDatiApplicativi().appendMessaggioCentrale(errorePrit);			
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void rigaPritPrivacy(ClientSessionContext csc, ClienteModel model, Cliente clientePrit, Contratto contrattoPrit){
		try{
			String codAgente = Tools.fillSx(model.getAgente().getCodAgente().toString(),'0',10);
			InfoPritModel infoPritModel = InfoLoader.getInfoPrit(csc, new StringType(Costanti.CODICE_PRODOTTO), new StringType(Costanti.CHIAVE_PRIT_PRIVACY));
			contrattoPrit.setCodProdotto(Integer.parseInt(infoPritModel.getPritCodProdotto().toString()));
			contrattoPrit.setCodOperazione(Integer.parseInt(infoPritModel.getPritCodOperazione().toString()));
			DacServiceCaller.inserisciDocumento(csc, codAgente, 
												Costanti.NOME_RISORSA,contrattoPrit,clientePrit,null);
			model.getDatiApplicativi().appendMessaggioCentrale(remindPritPrivacy);
		}catch(Throwable tr){
			model.getDatiApplicativi().appendMessaggioCentrale(errorePritPrivacy);
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void righePritFatca(ClientSessionContext csc, ClienteModel model, Cliente clientePrit, Contratto contrattoPrit){
		
		if(model.getModuloFatca().equals(Costanti.MODULO_W9_FATCA)){
			try{
				String codAgente = Tools.fillSx(model.getAgente().getCodAgente().toString(),'0',10);
				InfoPritModel infoPritModel = InfoLoader.getInfoPrit(csc, new StringType(Costanti.CODICE_PRODOTTO), new StringType(Costanti.CHIAVE_PRIT_W9_FATCA));
				contrattoPrit.setCodProdotto(Integer.parseInt(infoPritModel.getPritCodProdotto().toString()));
				contrattoPrit.setCodOperazione(Integer.parseInt(infoPritModel.getPritCodOperazione().toString()));
				for(int i=0;i<4;i++){
					DacServiceCaller.inserisciDocumento(csc, codAgente, 
														Costanti.NOME_RISORSA,contrattoPrit,clientePrit,null);
				}
				model.getDatiApplicativi().appendMessaggioCentrale(remindPritW9Fatca);
			}catch(Throwable tr){
				model.getDatiApplicativi().appendMessaggioCentrale(errorePritW9Fatca);
			}
		}

		if(model.getDatiFatca().getAlertMessage().equals(AbstractNavigatore.FATCA_ALERT_ALLEGATI))
			model.getDatiApplicativi().appendMessaggioCentrale(remindPritAllegatiFatca);

	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private String getNomeDAO(ClientSessionContext csc){
		String country = csc.getCountryCode();
		String xmlName = country.substring(0,1).toUpperCase()+country.substring(1).toLowerCase();
		xmlName += "AnagraficaClienti.AnagraficaClienti";
	  	return xmlName;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/		
	public int ultimoProgressivoVariazioni(ClientSessionContext csc, ClienteModel cliente) throws DAOException {

		DAOObject dao = new DAOObject(csc, "ItaAnagraficaClienti.Copernico");
		DAOQueryResultModel qRes= dao.executeQueryAccess("ultimoProgressivoVariazioni", cliente);
		int result = ((IntegerType)qRes.getSingleResult()).intValue();
		
		return result;
	}

}
