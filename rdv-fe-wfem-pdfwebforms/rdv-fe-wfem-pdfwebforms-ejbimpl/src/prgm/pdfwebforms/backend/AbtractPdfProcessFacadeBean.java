package prgm.pdfwebforms.backend;

import java.io.ByteArrayInputStream;
import java.util.List;

import com.atosorigin.wfem.backend.FacadeObject;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOTableResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.dao.exceptions.NoRowsAffected;
import com.atosorigin.wfem.nasstorage.NasStorage;
import com.atosorigin.wfem.nasstorage.SaveFileInfo;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ByteArrayType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.basket.Basket;
import prgm.pdfwebforms.core.PdfConfig;
import prgm.pdfwebforms.core.PdfNasUtil;
import prgm.pdfwebforms.core.PdfPredefinedFields;
import prgm.pdfwebforms.core.PdfXmlUtils;
import prgm.pdfwebforms.drivers.PdfDriverCaller;
import prgm.pdfwebforms.drivers.PdfModuliAggiuntiviManager;
import prgm.pdfwebforms.drivers.io.FreezeEventOutputData;
import prgm.pdfwebforms.drivers.io.SendToCliEventOutputData;
import prgm.pdfwebforms.drivers.io.idd.ProvideIddDataResponse;
import prgm.pdfwebforms.idd.IddCaller;
import prgm.pdfwebforms.model.PdfAttachModel;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfInstanceAttachModel;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.model.PritMomInfoModel;
import prgm.pdfwebforms.model.PritMomInfoProdottoAttivatoModel;
import prgm.pdfwebforms.mom.SrvDispositivaCallModel;
import prgm.pdfwebforms.mom.SrvDispositivaCaller;
import prgm.pdfwebforms.mom.SrvDispositivaElemModel;
import prgm.pdfwebforms.pca.PcaUtility;
import prgm.pdfwebforms.pritmom.PritMomInfo;
import prgm.pdfwebforms.pritmom.PritMomInfoLoader;
import prgm.pdfwebforms.pritmom.PritMomInfoProdottoAttivato;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;
import prgm.pdfwebforms.questionariolight.QuestionarioLightUtility;
import prgm.pdfwebforms.reportadeguatezza.ReportAdeguatezzaCaller;

/***********************************************************************************************/
/***********************************************************************************************/
public abstract class AbtractPdfProcessFacadeBean extends FacadeObject{

	/***********************************************************************************************/
	/***********************************************************************************************/
	protected void freezePdfInstance(ClientSessionContext csc, PdfModel pdf, PdfInstanceModel pdfInstance, String accessName, String stato, byte[] pdfContent) throws Exception{

		pdf.resetCommandErrors();
		
		String idReportAdeguatezza = pdf.getIdReportAdeguatezza();
		String idSostituzione = pdf.getIdSostituzione();
		String numOrdineReportAdeguatezza = pdf.mainPdfData().getNumOrdineReportAdeguatezza().toString();
		String idQuestionarioIdd = pdf.getIdQuestionarioIdd();
		String idRaccomandazioneIdd = pdf.getIdRaccomandazioneIdd();  // Id raccomandazione viene salvato solo per ramo protezione, passato in input, e previdenza
		if(idRaccomandazioneIdd.length() == 0 && pdf.getIddCallModel() != null && !pdf.getIddCallModel().getIdRaccomandazioneIdd().isNull()) {
			ProvideIddDataResponse iddData = PdfDriverCaller.callProvideIddData(csc, pdf, false);
			if(iddData != null && iddData.getTipoVerfica() == ProvideIddDataResponse.VERIFICA_IDD_RAMO_PREVIDENZA)
				idRaccomandazioneIdd = pdf.getIddCallModel().getIdRaccomandazioneIdd().toString();
		}
		String codDispositivaBMEDFromDriver = null;
			
		// Arricchisco l'istanza con tutti i dati già presenti su db
		DAOObject dao = new DAOObject(csc,PdfInstanceFacadeBean.DAO_XML_NAME);
		try{
			DAOTableResultModel tRes = dao.executeTableLoadAccess("pdfInstance", pdfInstance);
			if(tRes.getResult().intValue() == 0){
				pdf.addCommandError("#NOBACK#La bozza del contratto non è più disponibile. Ti invitiamo a procedere con una nuova compilazione.");
				return;
			}
			if(pdf.isInAccettazioneCopernico()){
				if(!PdfUtil.testConcorrenzaCopernicoIsOk(csc, pdf)) {
					pdf.addCommandError("#NOBACK#"+pdf.getInitialErrorMsg());
					return;
				}
			}else{
				if(!PdfUtil.testConcorrenzaBozzaIsOk(csc, pdf)) {
					pdf.addCommandError("#NOBACK#"+pdf.getInitialErrorMsg());
					return;
				}
				if(!pdfInstance.getIdAgevolazione().isNull()){
					BooleanType agevGiaUsata = (BooleanType)dao.executeQueryAccess("isCodiceAgevolazioneGiaUtilizzato", pdfInstance).getSingleResult();
					BooleanType agevBruciata = null;
					if(pdfInstance.getIdCarrello().isNull()) // CR26 - La bruciatura non va verificata nel carrello perchè il 5d la fa prima delle sottoscrizioni
						agevBruciata = (BooleanType)dao.executeQueryAccess("isCodiceAgevolazioneBruciato", pdfInstance).getSingleResult();	
					if((agevGiaUsata != null && agevGiaUsata.booleanValue()) || (agevBruciata != null && agevBruciata.booleanValue())){
						pdf.addCommandError("#NOBACK#Attenzione, la deroga selezionata è gia stata utilizzata e non è più disponibile, non è possibile procedere.");
						return;
					}
				}
			}
		}catch(DAOException daoe){
			LOG.error(daoe);
			throw new Exception(daoe.toString());
		}

		if(!pdf.isInAccettazioneCopernico()){
			// Per le chiamate alla adeguatezza che prevedono la gestione dello "scavallamento mezzanotte" controllo che 
			// non si stia terminando il giorno successivo
			if(pdf.getDataUltimaChiamataAdeguatezza() != null){
				if(!Tools.today().toString().equals(pdf.getDataUltimaChiamataAdeguatezza().toString())){
					pdf.addCommandError("La data corrente non coincide con la data di esecuzione dei controlli di adeguatezza della richiesta, pertanto non è possibile procedere.");
					return;
				}
			}
		}
		
		pdfInstance.setPdfAnag((PdfAnagModel)Tools.cloneObject(pdf.mainPdfAnag()));
		pdfInstance.setPdfsForMOM(new StringType());
		pdfInstance.setChiavePritMomInfo(new StringType());
		
		//MOP: Imposto il tipo processo di sede (null=MOM, MOM2, MOMO)
		impostaTipoProcessoSede(csc, pdf);
		
		// Chiamo la "freeze" sul driver del primo pdf. Salvo l'informazion di "freeze eseguita" così da poter fare la "unfreeze" in caso di errori
		// Se siamo in copernico smart chiamo la "sendTocli" e la "freeze" verrà richiamata in fase di accettazione
		boolean freezeDone = false;
		boolean skipLegameConRda = false;
		Boolean onFreezePutOnSignedProcessBatchQueue = null;
		Boolean onFreezePutOnPostCompletionProcessBatchQueue = null;
		if(stato.equals(PdfInstanceModel.STATO_COMPLETATO_E_INVIATO_AL_CLIENTE)){
			
			SendToCliEventOutputData eventOutput = PdfDriverCaller.callSendToCli(csc, pdf, pdfContent);
			if(eventOutput != null){
				if(eventOutput.getErrorMessage() != null && eventOutput.getErrorMessage().length() > 0){
					pdf.addCommandError(eventOutput.getErrorMessage());
					return;
				}
				skipLegameConRda = eventOutput.isSkipLegameConRda();
				if(eventOutput.getPutOnDeadEndProcessBatchQueue() != null)
					pdfInstance.setPutOnDeadEndProcessBatchQueue(new StringType(eventOutput.getPutOnDeadEndProcessBatchQueue().booleanValue() ? "S" : "N"));
			}
			
		}else{
			
			FreezeEventOutputData eventOutput = PdfDriverCaller.callPdfFreeze(csc, pdf, pdfContent);
			copiaBeneficiariProspectSuModuloAml(csc, pdf); // Nelle polizze i beneficiari prospect sono solo codici generati in freeze per cui per MOM i campi vanno messi nel pdfData del moduo AML
			if(eventOutput != null){
				if(eventOutput.getErrorMessage() != null && eventOutput.getErrorMessage().length() > 0){
					pdf.addCommandError(eventOutput.getErrorMessage());
					return;
				}
				if(eventOutput.getPritMomInfo() != null){
					pdfInstance.setChiavePritMomInfo(new StringType(eventOutput.getPritMomInfo().getChiave()));
					PritMomInfo pritMomInfo = PritMomInfoLoader.loadPritMomInfo(csc, pdf.mainPdfAnag(), eventOutput.getPritMomInfo().getChiave());
					if(pritMomInfo != null){
						pdfInstance.getPdfAnag().setPdfCodProdottoPrit(new IntegerType(pritMomInfo.getCodProdotto()));
						pdfInstance.getPdfAnag().setPdfCodOperazionePrit(new IntegerType(pritMomInfo.getCodOperazione()));
					}else if(eventOutput.getPritMomInfo().getCodProdotto() != 0 && eventOutput.getPritMomInfo().getCodOperazione() != 0){
						pdfInstance.getPdfAnag().setPdfCodProdottoPrit(new IntegerType(eventOutput.getPritMomInfo().getCodProdotto()));
						pdfInstance.getPdfAnag().setPdfCodOperazionePrit(new IntegerType(eventOutput.getPritMomInfo().getCodOperazione()));
					}
				}
				if(eventOutput.getPdfsForMOM() != null && eventOutput.getPdfsForMOM().length() > 0){
					pdfInstance.setPdfsForMOM(new StringType(eventOutput.getPdfsForMOM()));
				}
				if(eventOutput.getPutOnSignedProcessBatchQueue() != null) {
					pdf.setPutOnSignedProcessBatchQueue(eventOutput.getPutOnSignedProcessBatchQueue());
					onFreezePutOnSignedProcessBatchQueue = eventOutput.getPutOnSignedProcessBatchQueue();
				}
				if(eventOutput.getPutOnPostCompletionProcessBatchQueue() != null) {
					pdf.setPutOnPostCompletionProcessBatchQueue(eventOutput.getPutOnPostCompletionProcessBatchQueue());
					onFreezePutOnPostCompletionProcessBatchQueue = eventOutput.getPutOnPostCompletionProcessBatchQueue();
				}

				skipLegameConRda = eventOutput.isSkipLegameConRda();
				if(eventOutput.getCodDispositivaBMEDFromDriver() != null)
					codDispositivaBMEDFromDriver = eventOutput.getCodDispositivaBMEDFromDriver();
			}
			freezeDone = true;
			
			// Imposto il modello master per i dati Prit/MOM
			PritMomInfo eventPritMomInfo = eventOutput == null ? null : eventOutput.getPritMomInfo();
			PritMomInfoModel pritMomInfoModel = initPritMomInfoModel(csc, eventPritMomInfo, pdfInstance);
			pdf.setPritMomInfoModel(pritMomInfoModel);
			
		}
		
		// Inizializzo i dati e richiamo il servizio dispositiva, se da richamare
		SrvDispositivaCallModel srvDispositivaCallModel = null;
		try{
			
			boolean callSrvDispositivaBMEDIsDisabled = false;
			String callSrvDispositivaBMEDConfiguration = pdf.callSrvDispositivaBMEDConfiguration();
			boolean callSrvDispositivaBMED = callSrvDispositivaBMEDConfiguration.equals("S") || callSrvDispositivaBMEDConfiguration.equals("D"); 
			if(callSrvDispositivaBMED){
				if(!pdf.isTipoProcessoSedeMOM2() && !pdf.mainPdfAnag().getTipoProcessoSedeInPdfAnag().equals("MOM2"))
					callSrvDispositivaBMEDIsDisabled = PdfConfig.getParamAsBool(csc, "PRATICHE_DIGITALI", "DISABILITA_CALL_SRV_DISPOSITIVA_PROC_MOM").booleanValue();
				if(callSrvDispositivaBMEDIsDisabled){
					callSrvDispositivaBMED = false;
				}else{
					if(stato.equals(PdfInstanceModel.STATO_COMPLETATO_E_INVIATO_AL_CLIENTE)){	// per ora non chiamo se siamo in invio al cliente copernico
						callSrvDispositivaBMED = false;
					}else if(callSrvDispositivaBMEDConfiguration.equalsIgnoreCase("D")){ // non chiamo se compilazione non in digitale/copernico
						if( !pdf.getPdfCompilationMode().equals(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_FIRMA_DIGITALE) &&
							!pdf.getPdfCompilationMode().equals(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_COPERNICO))
							callSrvDispositivaBMED = false;
					}
				}
			}

			// Salvo gli attach, se siamo lato rete
			if(!pdf.isInAccettazioneCopernico()) {
				String errMsg = saveAttachmentsOnNas(csc, pdf, pdfInstance, callSrvDispositivaBMEDIsDisabled);
				if(errMsg != null) {
					doUnfreeze(freezeDone, csc, pdf);
					pdf.addCommandError("Errore di sistema nel salvataggio degli allegati per MOM: "+errMsg);
					return;
				}
			}
			
			TimestampType now = Tools.now();
			PdfDataModel pdfData = pdf.getPdfData();
			
			pdfInstance.setPdfXmlData(new StringType(PdfXmlUtils.xmlFromModel(pdfData, pdf, true)));
			
			pdfInstance.setPdfStatus(new StringType(stato));
			pdfInstance.setPdfCompletionTime(now);
			pdfInstance.setPdfCompletionUser(new StringType(csc.getUserCode()));
			pdfInstance.setPdfCompilationMode(new StringType(pdf.getPdfCompilationMode().toString()));				
			pdfInstance.setPdfBarcode(new StringType(pdf.mainPdfData().getPdfBarcode().toString()));
			pdfInstance.setIdReportAdeguatezza(new StringType(idReportAdeguatezza));
			pdfInstance.setIdSostituzione(new StringType(idSostituzione));
			pdfInstance.setNumOrdineReportAdeguatezza(new StringType(numOrdineReportAdeguatezza));
			pdfInstance.setIdQuestionarioIdd(new StringType(idQuestionarioIdd));
			pdfInstance.setIdRaccomandazioneIdd(new StringType(idRaccomandazioneIdd));
			pdfInstance.setPdfContent(new ByteArrayType(pdfContent));
			pdfInstance.setIsMultiPdf(new BooleanType(pdf.isMultiPdf()));
			pdfInstance.setSistemaClient(pdf.getPdfData().getSistemaClient());
			pdfInstance.setPdfTipoCopernico(new StringType(PdfInstanceModel.TIPO_COPERNICO_SMART));

			pdfInstance.initAgevolazioneInInstance(pdf);

			pdfInstance.setFlagWayout(pdf.getFlagWayout());
			pdfInstance.setPdfNoteFBCopernico(new StringType(pdf.getPdfNoteFBCopernico().toString()));
			pdfInstance.instanceCodProdFromPdfModules(pdf);
			
			// Firma a distanza o in presenza
			if(pdf.getPdfCompilationMode().equals(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_FIRMA_DIGITALE) ||
			   pdf.getPdfCompilationMode().equals(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_COPERNICO)){
				PdfModel firstPdf = Basket.getFirstDispoPdf(pdf);
				pdfInstance.setIsFirmaADistanza(new StringType(firstPdf.getIsFirmaADistanza().toString()));
			}
			
			// Integrazione MOM modello MOP
			pdfInstance.setIsMop(new StringType());
			if(pdf.isTipoProcessoSedeMOM2())
				pdfInstance.setIsMop(new StringType("S"));
			
			// Batch queue flags (in accettazione copernico sono già stati scritti nella fase dispositiva)
			if(!pdf.isInAccettazioneCopernico()){			
				// OnSignedProcessBatchQueue
				if(pdf.getPutOnSignedProcessBatchQueue() != null)
					pdfInstance.setPutOnSignedProcessBatchQueue(new StringType(pdf.getPutOnSignedProcessBatchQueue().booleanValue() ? "S" : "N"));
				else if(pdf.getPdfCompilationMode().equals(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_LIBERA) ||
						pdf.getPdfCompilationMode().equals(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_CHIMICA))
					pdfInstance.setPutOnSignedProcessBatchQueue(new StringType("N"));
				
				// OnPostCompletionProcessBatchQueue
				if(pdf.getPutOnPostCompletionProcessBatchQueue() != null)
					pdfInstance.setPutOnPostCompletionProcessBatchQueue(new StringType(pdf.getPutOnPostCompletionProcessBatchQueue().booleanValue() ? "S" : "N"));
			}else{
				dao.executeQueryAccess("loadBatchQueueFlags", pdfInstance);
				// Se impostati in feeeze rimpiazzo i flag delle code
				if(onFreezePutOnSignedProcessBatchQueue != null)
					pdfInstance.setPutOnSignedProcessBatchQueue(new StringType(onFreezePutOnSignedProcessBatchQueue ? "S" : "N"));
				if(onFreezePutOnPostCompletionProcessBatchQueue != null)
					pdfInstance.setPutOnPostCompletionProcessBatchQueue(new StringType(onFreezePutOnPostCompletionProcessBatchQueue ? "S" : "N"));
			}

			// Integrazione servizio "dispositiva" 
			if(callSrvDispositivaBMED){
				srvDispositivaCallModel = SrvDispositivaCaller.callSrvDispositivaFromCompilation(csc, pdfInstance, pdf, pdfContent);
				String errorMsg = srvDispositivaCallModel.getCallSrvErrorMessage();
				if(errorMsg != null && errorMsg.length() > 0){
					doUnfreeze(freezeDone, csc, pdf);
					pdf.addCommandError(errorMsg);
					return;
				}
				// Se tutto ok per i pdf integrati con il servizio dispositiva le mail e gli sms vengono mandati da ICWPdfWebFormsSignedProcess
				if(!pdfInstance.getCodDispositivaBMED().isNull() &&
					(!pdf.getPdfCompilationMode().equals(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_LIBERA) &&
					 !pdf.getPdfCompilationMode().equals(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_CHIMICA)))
					pdfInstance.setPutOnSignedProcessBatchQueue(new StringType("S"));				
			}

		}catch(Exception | DAOException e){
			doUnfreeze(freezeDone, csc, pdf);
			LOG.error(e);
			throw new Exception(e.toString());
		}

		// Persistenza pdf su NAS
		byte[] savePdfContent = pdfInstance.getPdfContent().byteArrayValue();
		try {
			boolean blobSuDB = PdfNasUtil.blobPdfInstanceSuDB(csc);
			if(!blobSuDB){
				StringType pilotaNAS = PdfConfig.getParamAsString(csc, "PILOTA_SCRITTURA_NAS", "CODICI_AGENTE", true);
				String userPilota = pdf.getMainCodAgente() != null ? pdf.getMainCodAgente().toString() : csc.getUserCode(); // La "if" sul !=null è per sicurezza ma non dovrebbe mai accadere
				if(pilotaNAS.isNull() || pilotaNAS.toString().indexOf(Tools.fillSx(userPilota,'0',10)) >= 0){
					PdfNasUtil.PDF_INSTANCE.writePdfInstanceContent(csc, pdfInstance.getPdfInstanceId(), savePdfContent);
					pdfInstance.getPdfContent().setByteArrayValue(null);
				}
			}
		}catch(Exception e){
			doUnfreeze(freezeDone, csc, pdf);
			LOG.error(e);
			throw e;
		}
			
		// Effettuo la persistenza in transazione
		boolean committed = false;
		try{

			// Imposto adeguatamente l'agente impersonato, vuoto se corrisponde all'agente vendente
			if(pdfInstance.getCodAgeImpersonato().equals(pdfInstance.getCodAgente()))
				pdfInstance.setCodAgeImpersonato(new StringType());
			
			// Se il driver specifica una chiaveK lo salviamo (non lo deve mai fare ma per PCP siamo stati obbligati)
			if(codDispositivaBMEDFromDriver != null)
				pdfInstance.setCodDispositivaBMED(new StringType(codDispositivaBMEDFromDriver));
			
			dao.beginTransaction();
			dao.executeTableUpdateAccess(accessName,pdfInstance);
			pdfInstance.getPdfContent().setByteArrayValue(savePdfContent);
			
			deleteChilds(dao,"pdfInstanceDett",pdfInstance);
			if(pdf.isMultiPdf()){
				for(int i=0;i<pdf.getPdfAnags().size();i++){
					PdfInstanceModel pdfInstanceDett = instanceDettFromAnag(pdfInstance, pdf, i, srvDispositivaCallModel);
					dao.executeTableInsertAccess("pdfInstanceDett", pdfInstanceDett);
				}
			}
			
			// Salvo i dati callSrvDispositivaBMED su DB se vi sono dispositive aggiuntive (indice dall'1 in poi)
			deleteChilds(dao,"pdfSrvDispositivaBmedData",pdfInstance);
			if(srvDispositivaCallModel != null && srvDispositivaCallModel.getElencoDispositive().size() > 1) {
				for(int i=1;i<srvDispositivaCallModel.getElencoDispositive().size();i++) {
					SrvDispositivaElemModel dispoAggiuntiva = (SrvDispositivaElemModel)srvDispositivaCallModel.getElencoDispositive().get(i);
					if(!dispoAggiuntiva.getMainPdfInstanceId().isNull() && !dispoAggiuntiva.getTipoDispositiva().isNull())
						dao.executeTableInsertAccess("pdfSrvDispositivaBmedData", dispoAggiuntiva);
				}
			}

			// Se siamo lato rete, abbiamo una agevolazione, abbiamo un RDA e non siamo nel 5D "bruciamo" l'agevolazione (il 5d le brucia in autonomia)
			bruciaAgevolazione(dao, pdf, pdfInstance);
			// Successivamente bruciamo le eventuali agevolazioni presenti nei moduli successivi al primo. In questo caso lo facciamo sempre
			bruciaAgevolazioniAggiuntive(dao, pdf, pdfInstance);
			
			dao.commitTransaction();
			committed = true;
			
		}catch(Exception | DAOException e){
			LOG.error(e);
			throw new Exception(e.toString());
		}finally{
			if(!committed) {
				dao.rollbackTransaction();
				doUnfreeze(freezeDone, csc, pdf);
			}
		}
				
		if(committed){
			if(!skipLegameConRda){ // Legame pdf-rda, quando necessario
				try{
					// Da specifiche ricevute (mail Bosco 9 novembre 2017 18:56) la scrittura del legame non deve essere bloccante
					// Non gestisco il messaggio ritornato dal metodo ne le eventuali eccezioni
					ReportAdeguatezzaCaller.callScriviLegameDispositivaOrdine(csc, pdf, pdfInstance);
				}catch(Exception e){
					LOG.error(e);
				}
			}
			try{
				 // Legame pdf-raccomandazione, quando generata
				IddCaller.callCollegaRaccomandazioneIddGenerata(csc, pdf);
			}catch(Exception e){
				LOG.error(e);
			}				
			try{
				 // Legame questionario light - istanza per i post vendita polizze
				QuestionarioLightUtility.callCollegaQuestionarioPostvendita(csc, pdf);
			}catch(DAOException | Exception e){
				LOG.error(e);
			}
			try{
				 // Legame PCA con Report adeguatezza
				PcaUtility.callCollegaPcaAReportAdeguatezza(csc, pdf, idReportAdeguatezza);
			}catch(DAOException | Exception e){
				LOG.error(e);
			}
		}
		
	}

		
	/***********************************************************************************************/
	/*
	 * 	Per stabilire se uno switch è MOM2 vengono usate le chiavi configurate e predefinite 
	 *  per ciascun prodotto dello switch.
	 *  Per gli irlandesi aggiuntivi, avendo un unico modulo, usiamo di default il BESTBRANDS 
	 *  
		-- Switch Rimborsi
		'SWITCH_RMB_FONDIITALIA'	FI13
		'SWITCH_RMB_BESTBRANDS'		FE15
		'SWITCH_RMB_CHALLENGE'		FE15 -- Non usato qui, modulo unico si fa riferimento al BestBrands
		
		-- Switch Iniziali
		'SWITCH_INI_FONDIITALIA'	FI02
		'SWITCH_INI_BESTBRANDS'		FE02
		'SWITCH_INI_CHALLENGE' 		FE03-- Non usato qui, si fa comunque riferimento al BestBrands
		
		-- Switch Aggiuntivi
		'SWITCH_AGG_FONDIITALIA'	FI07
		'SWITCH_AGG_BESTBRANDS'		FE23
		'SWITCH_AGG_CHALLENGE'		FE23 	-- Non usato qui, modulo unico si fa riferimento al BestBrands

	 */
	/***********************************************************************************************/
	private static final String TIPO_PROCESSO_DI_SEDE_MOM2 = "MOM2";
	private static final String TIPO_PROCESSO_DI_SEDE_MOMO = "MOMO";
	private void impostaTipoProcessoSede(ClientSessionContext csc, PdfModel pdf) throws Exception{
		try {
			
			boolean allMom2 = true;
			pdf.setTipoProcessoSede(new StringType());
			
			List<String> elencoModuliMOMO = PdfConfig.getParamAsStringArray(csc, "MOP", "MODULI_MOMO", "");

			for(int i=0;i<pdf.getPdfAnags().size();i++) {
				
				PdfAnagModel pdfAnag = pdf.getPdfAnags().get(i); 
				pdfAnag.setTipoProcessoSede(new StringType());
				
				// Se modulo configurato internamente come MOMO non lo considero per la determinazione del tipo processo di sede
				if( (!pdfAnag.getPdfMomCode().isNull() && elencoModuliMOMO.contains(pdfAnag.getPdfMomCode().toString())) || 
					(!pdfAnag.getPdfDriverName().isNull() && elencoModuliMOMO.contains(pdfAnag.getPdfDriverName().toString()))){
					pdfAnag.setTipoProcessoSede(new StringType(TIPO_PROCESSO_DI_SEDE_MOMO));
					continue;
				}
				
				int codProdottoPrit = pdfAnag.getPdfCodProdottoPrit().intValue();
				int codOperazionePrit  = pdfAnag.getPdfCodOperazionePrit().intValue();
				if(pdf.getPdfData().getIsSwitch().booleanValue()){
					String chiave = null;
					if(pdfAnag.getPdfDriverName().equals("fondiitaliarimborso"))
						chiave = "SWITCH_RMB_FONDIITALIA";
					else if(pdfAnag.getPdfDriverName().equals("fondiirlandesirimborso"))
						chiave = "SWITCH_RMB_BESTBRANDS";
					else if(pdfAnag.getPdfDriverName().equals("fondiitaliainiziale"))
						chiave = "SWITCH_INI_FONDIITALIA";
					else if(pdfAnag.getPdfDriverName().equals("fondiirlandesiiniziale"))
						chiave = "SWITCH_INI_BESTBRANDS";
					else if(pdfAnag.getPdfDriverName().equals("fondiitaliaaggiuntivo"))
						chiave = "SWITCH_AGG_FONDIITALIA";
					else if(pdfAnag.getPdfDriverName().equals("fondiirlandesiaggiuntivo"))
						chiave = "SWITCH_AGG_BESTBRANDS";
					if(chiave != null) {
						PritMomInfo pritMomInfo = PritMomInfoLoader.loadPritMomInfo(csc, pdfAnag, chiave);
						if(pritMomInfo != null){
							codProdottoPrit = pritMomInfo.getCodProdotto();
							codOperazionePrit = pritMomInfo.getCodOperazione();
						}else{ // Manca la riga configurativa di uno dei 2 -> MOM1
							return;
						}
					}
				}

				MapCommandDataModel codiciPrit = new MapCommandDataModel();
				codiciPrit.addProperty("codProdottoPrit", new StringType(Integer.toString(codProdottoPrit)));
				codiciPrit.addProperty("codOperazionePrit", new StringType(Integer.toString(codOperazionePrit)));
				
				StringType tipoProcessoSedeMOM = (StringType)new DAOObject(csc, PdfInstanceFacadeBean.DAO_XML_NAME).executeQueryAccess("loadTipoProcessoSedeMOM", codiciPrit).getSingleResult();
				if(tipoProcessoSedeMOM == null)
					tipoProcessoSedeMOM = new StringType();
				if(tipoProcessoSedeMOM.equals(TIPO_PROCESSO_DI_SEDE_MOM2))
					pdfAnag.setTipoProcessoSede(new StringType(TIPO_PROCESSO_DI_SEDE_MOM2));
				else if(tipoProcessoSedeMOM.equals(TIPO_PROCESSO_DI_SEDE_MOMO))
					pdfAnag.setTipoProcessoSede(new StringType(TIPO_PROCESSO_DI_SEDE_MOMO));
				else
					allMom2 = false;
			}
			
			if(allMom2)
				pdf.setTipoProcessoSede(new StringType(TIPO_PROCESSO_DI_SEDE_MOM2));

		}catch(DAOException daoe) {
			throw new Exception(daoe.toString());
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private PritMomInfoModel initPritMomInfoModel(ClientSessionContext csc, PritMomInfo eventPritMomInfo, PdfInstanceModel pdfInstance) throws Exception{
		boolean saveData = false;
		PritMomInfoModel pritMomInfoModel = new PritMomInfoModel();
		
		if(!pdfInstance.getPdfAnag().getPdfCodProdottoPrit().isNull()){
			pritMomInfoModel.setCodProdottoPrit(pdfInstance.getPdfAnag().getPdfCodProdottoPrit());
			saveData = true;
		}
		if(!pdfInstance.getPdfAnag().getPdfCodOperazionePrit().isNull()){
			pritMomInfoModel.setCodOperazionePrit(pdfInstance.getPdfAnag().getPdfCodOperazionePrit());
			saveData = true;
		}
		
		if(eventPritMomInfo != null && eventPritMomInfo.getProdottiAttivati() != null && eventPritMomInfo.getProdottiAttivati().size() > 0){
			pritMomInfoModel.setProdottiAttivati(new ListType(PritMomInfoProdottoAttivatoModel.class));
			for(int i=0;i<eventPritMomInfo.getProdottiAttivati().size();i++){
				PritMomInfoProdottoAttivato p = eventPritMomInfo.getProdottiAttivati().get(i);
				PritMomInfoProdottoAttivatoModel pm = new PritMomInfoProdottoAttivatoModel();
				if(i == 0)
					pm.setIsMaster(new BooleanType(true));
				pm.setCodSurrProdServ(new StringType(p.getCodSurrProdServ()));
				pm.setCodTipoOperSuProd(new StringType(p.getCodTipoOperSuProd()));
				pritMomInfoModel.getProdottiAttivati().add(pm);
			}
			saveData = true;
		}
		
		/* Per ora non valorizziamo il codice macro richiesto da MOM (commentato anche l'accesso DAO)
		// Carico il codice macro tipo dispositiva
		if(!pdfInstance.getPdfAnag().getPdfCodProdottoPrit().isNull() && !pdfInstance.getPdfAnag().getPdfCodOperazionePrit().isNull()){
			try{
				StringType codiceMacroTipoDisp = (StringType)new DAOObject(csc,PdfInstanceFacadeBean.DAO_MOM_XML_NAME).executeQueryAccess("loadCodiceMacroTipoDisp", pdfInstance).getSingleResult();
				pritMomInfoModel.setCodiceMacroTipoDisp(codiceMacroTipoDisp);
			}catch(DAOException daoe){
				LOG.error(daoe);
				throw new Exception(daoe.toString());
			}
		}
		*/
		
		if(saveData)
			return pritMomInfoModel;
		else
			return null;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private PdfInstanceModel instanceDettFromAnag(PdfInstanceModel pdfInstance, PdfModel pdf, int pdfIndex, SrvDispositivaCallModel srvDispositivaCallModel){
		PdfDataModel pdfData = (PdfDataModel)pdf.getPdfData().getPdfs().get(pdfIndex);
		PdfAnagModel pdfAnag = pdf.getPdfAnags().get(pdfIndex);
		
		PdfInstanceModel pdfinstanceDett = new PdfInstanceModel();
		pdfinstanceDett.setPdfInstanceId(pdfInstance.getPdfInstanceId());
		pdfinstanceDett.setPdfInstanceDettNum(new IntegerType(pdfIndex));
		pdfinstanceDett.setPdfAnag(pdfAnag);
		pdfinstanceDett.setPdfBarcode(pdfData.getPdfBarcode());
		if(srvDispositivaCallModel != null && pdf.isTipoProcessoSedeMOM2())
			pdfinstanceDett.setCodDispositivaBMED(srvDispositivaCallModel.getCodDispositivaBMED(pdfIndex));
		return pdfinstanceDett;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void deleteChilds(DAOObject dao, String accessName, CommandDataModel model) throws DAOException{
		try{ dao.executeTableDeleteChildsAccess(accessName, model); }catch(NoRowsAffected nra){/*do nothing*/}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void bruciaAgevolazione(DAOObject dao, PdfModel pdf, PdfInstanceModel pdfInstance) throws DAOException{
		// Se siamo lato rete, abbiamo una agevolazione, abbiamo un RDA e non siamo nel 5D "bruciamo" l'agevolazione (il 5d le brucia in autonomia)
		if(!pdf.isInAccettazioneCopernico() &&
		   !pdfInstance.getIdAgevolazione().isNull()){
			IntegerType idAttivita = (IntegerType)dao.executeQueryAccess("loadIdAttivitaFromIdAgevolazione", pdfInstance).getSingleResult();
			if(idAttivita != null && !idAttivita.isNull()) {
				try{
					MapCommandDataModel m = new MapCommandDataModel();
					m.addProperty("idAttivita", idAttivita);
					dao.executeTableUpdateAccess("bruciaAgevolazione",m);
				}catch(NoRowsAffected nra){/* do nothing */}
			}
		}		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void bruciaAgevolazioniAggiuntive(DAOObject dao, PdfModel pdf, PdfInstanceModel pdfInstance) throws DAOException{
		
		PdfDataModel pdfData = pdf.getPdfData();
		if(pdfData.getPdfs().size() < 2)
			return;
		
		// Se siamo lato rete e ci sono più moduli cerco le eventuali agevolazioni a partire dal secondo, diverse dalla principale nella pdfInstance (ad es lo switch)
		if(pdf.isInAccettazioneCopernico())
			return;
		for(int i=1;i<pdfData.getPdfs().size();i++) {
			PdfDataModel pdfDataElement = (PdfDataModel)pdfData.getPdfs().get(i);
			StringType idAgevolazione = (StringType)pdfDataElement.read(PdfPredefinedFields.ID_AGEVOLAZIONE);
			if(idAgevolazione != null && !idAgevolazione.isNull() && !idAgevolazione.equals(pdfInstance.getIdAgevolazione())) {
				MapCommandDataModel m = new MapCommandDataModel();
				m.addProperty(PdfPredefinedFields.ID_AGEVOLAZIONE, idAgevolazione);
				IntegerType idAttivita = (IntegerType)dao.executeQueryAccess("loadIdAttivitaFromIdAgevolazione", m).getSingleResult();
				if(idAttivita != null && !idAttivita.isNull()) {
					try{
						m.addProperty("idAttivita", idAttivita);
						dao.executeTableUpdateAccess("bruciaAgevolazione",m);
					}catch(NoRowsAffected nra){/* do nothing */}
				}
				
			}
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private String saveAttachmentsOnNas(ClientSessionContext csc, PdfModel pdf, PdfInstanceModel pdfInstance, boolean callSrvDispositivaBMEDIsDisabled) {

		SaveFileInfo saveFileInfo = null;

		boolean hasAttach = pdf.getPdfAttachments() != null && pdf.getPdfAttachments().size() > 0;
		boolean isIntegratoMOM = pdf.mainPdfAnag().getCallSrvDispositivaBMED().equals("S") || pdf.mainPdfAnag().getCallSrvDispositivaBMED().equals("D");
		boolean isDigitale = pdf.getPdfCompilationMode().equals(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_FIRMA_DIGITALE) || pdf.getPdfCompilationMode().equals(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_COPERNICO);

		clearAttachmentsFromNas(csc, pdf);
		
		// Salvo gli attach sulla nas per MOM, se il servizoi pratiche digitali non è disabilitato
		if(!callSrvDispositivaBMEDIsDisabled && hasAttach && isIntegratoMOM && isDigitale) {
			
			for(int i=0;i<pdf.getPdfAttachments().size();i++) {
				PdfAttachModel attach = (PdfAttachModel)pdf.getPdfAttachments().get(i);
				if(attach.getDriverAttachRef() != null && (attach.getDriverAttachRef().getImplicitContent() != null || !attach.getFile().isNull())) {				
					try{
						byte[] attachContent = attach.getDriverAttachRef().getImplicitContent() != null ? attach.getDriverAttachRef().getImplicitContent() : attach.getFile().getFileContent();
						saveFileInfo = NasStorage.saveFile(csc, "CLIENTIPROMOTORI", "Pdf_Compilabili", "PDF"+pdfInstance.getPdfInstanceId()+"_allegato"+(i+1)+".pdf", new ByteArrayInputStream(attachContent));
					}catch(Exception e){
						return e.toString();
					}
					PdfInstanceAttachModel instanceAttach = new PdfInstanceAttachModel();
					instanceAttach.setIdFileNasAllegato(new StringType(saveFileInfo.getIdFile()));
					instanceAttach.setDescrizioneAllegato(new StringType(attach.getDriverAttachRef().getDescr()));
					instanceAttach.setDenominazioneFileAllegato(new StringType(saveFileInfo.getFileName()));
					pdf.getPdfInstanceAttachments().add(instanceAttach);
				}
			}
		}

		// Salvo gli attach originali, specificati dal driver, sulla nas
		if(hasAttach) {
			for(int i=0;i<pdf.getPdfAttachments().size();i++) {
				PdfAttachModel attach = (PdfAttachModel)pdf.getPdfAttachments().get(i);
				if(attach.getDriverAttachRef() != null && !attach.getOriginalFile().isNull()) {
					String fName = attach.getOriginalFile().getFileName();
					String fExt = "";
					if(fName != null) {
						int extIdx = attach.getOriginalFile().getFileName().lastIndexOf(".");
						if(extIdx >= 0)
							fExt = fName.substring(extIdx).toLowerCase();
					}
					try{
						byte[] attachContent = attach.getOriginalFile().getFileContent();
						saveFileInfo = NasStorage.saveFile(csc, "CLIENTIPROMOTORI", "Pdf_Compilabili", "PDF"+pdfInstance.getPdfInstanceId()+"_allegatoOriginale"+(i+1)+fExt, new ByteArrayInputStream(attachContent));
					}catch(Exception e){
						return e.toString();
					}
					PdfInstanceAttachModel instanceAttach = new PdfInstanceAttachModel();
					instanceAttach.setIdFileNasAllegato(new StringType(saveFileInfo.getIdFile()));
					instanceAttach.setDescrizioneAllegato(new StringType(attach.getDriverAttachRef().getDescr()));
					instanceAttach.setDenominazioneFileAllegato(new StringType(saveFileInfo.getFileName()));
					pdf.getPdfInstanceOriginalAttachments().add(instanceAttach);
				}
			}
		}
		return null;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void clearAttachmentsFromNas(ClientSessionContext csc, PdfModel pdf) {
		for(int i=0;i<pdf.getPdfInstanceAttachments().size();i++) {
			PdfInstanceAttachModel instanceAttach = (PdfInstanceAttachModel)pdf.getPdfInstanceAttachments().get(i);
			if(!instanceAttach.getIdFileNasAllegato().isNull()) {
				try {
					NasStorage.deleteFile(csc, instanceAttach.getIdFileNasAllegato().toString());
				}catch(Exception e) {
					// Do nothing
				}
			}
		}
		for(int i=0;i<pdf.getPdfInstanceOriginalAttachments().size();i++) {
			PdfInstanceAttachModel instanceAttach = (PdfInstanceAttachModel)pdf.getPdfInstanceOriginalAttachments().get(i);
			if(!instanceAttach.getIdFileNasAllegato().isNull()) {
				try {
					NasStorage.deleteFile(csc, instanceAttach.getIdFileNasAllegato().toString());
				}catch(Exception e) {
					// Do nothing
				}
			}
		}
		pdf.getPdfInstanceAttachments().clear();
		pdf.getPdfInstanceOriginalAttachments().clear();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void copiaBeneficiariProspectSuModuloAml(ClientSessionContext csc, PdfModel pdf) {
		try {
			if(!pdf.isMultiPdf())
				return;
			PdfDataModel mainPdfData = pdf.mainPdfData();
			List<String> elencoCodiciModuloAML = PdfModuliAggiuntiviManager.elencoCodiciModuloAggiuntivi(csc);
			for(int i=1;i<pdf.getPdfData().getPdfs().size();i++) {
				PdfDataModel pdfData = (PdfDataModel)pdf.getPdfData().getPdfs().get(i);
				for(String momCodeAML : elencoCodiciModuloAML) {
					if(pdfData.getPdfMomCode().equals(momCodeAML)) {
						copiaCampiBeneficiariProspectSuModuloAml(mainPdfData, pdfData);
						return;
					}
				}				
			}
		}catch(Exception e) {
			// do nothing
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void copiaCampiBeneficiariProspectSuModuloAml(PdfDataModel mainData, PdfDataModel amlData) {
		for(int i=1;i<20;i++) {
			String fn = "codiceProspectBeneficiario"+i;
			AbstractType f = mainData.read(fn);
			if(f != null)
				amlData.addProperty(fn, f);
			
			for(int j=1;j<10;j++) {
				fn = "codiceProspectTitolare"+j+"Beneficiario"+i;
				f = mainData.read(fn);
				if(f != null)
					amlData.addProperty(fn, f);
			}
		}
	}	

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void doUnfreeze(boolean freezeDone, ClientSessionContext csc, PdfModel pdf) {
		if(!freezeDone)
			return;
		PdfDriverCaller.callPdfUnfreeze(csc, pdf);
	}
	
}
