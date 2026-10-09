package prgm.pdfwebforms.mom;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.DAOOSBResultModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.nasstorage.NasStorage;
import com.atosorigin.wfem.nasstorage.SaveFileInfo;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.OSBCallData;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.backend.PdfInstanceFacadeBean;
import prgm.pdfwebforms.core.PdfEngine;
import prgm.pdfwebforms.core.PdfPredefinedFields;
import prgm.pdfwebforms.core.PdfWebFormsException;
import prgm.pdfwebforms.core.PdfXmlUtils;
import prgm.pdfwebforms.dataloader.DataLoader;
import prgm.pdfwebforms.drivers.PdfDriverCaller;
import prgm.pdfwebforms.drivers.io.prit.ProvidePritDataResponse;
import prgm.pdfwebforms.drivers.io.squadra.ElementoSquadra;
import prgm.pdfwebforms.drivers.io.srvdispositiva.DispoAggiuntiva;
import prgm.pdfwebforms.drivers.io.srvdispositiva.DispoAggiuntiva.PritRetrieveInfo;
import prgm.pdfwebforms.drivers.io.srvdispositiva.ProvideSrvDispositivaDataResponse;
import prgm.pdfwebforms.drivers.io.srvdispositiva.VincoloDispositiva;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfInstanceAttachModel;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.model.PdfPersonInstanceModel;
import prgm.pdfwebforms.model.PdfPersonModel;
import prgm.pdfwebforms.pritmom.PritMomInfo;
import prgm.pdfwebforms.pritmom.PritMomInfoLoader;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class SrvDispositivaCaller {

	private static final String TIPO_PROCESSO_DI_SEDE_MOM1 = "MOM";
	private static final String TIPO_PROCESSO_DI_SEDE_MOM2 = "MOM2";
	private static final String TIPO_PROCESSO_DI_SEDE_MOMO = "MOMO";

	private static final String DAO_ACCESS_LOAD_CODICI_SURROGATI_PRIT = "loadCodiciSurrogatiPrit";
	private static final String AZIONE_SRV_PROCESSA = "processa";
	private static final String AZIONE_SRV_RISOTTOMETTI = "risottometti";
	private static final String S_OPERZ_SUBSTR = "] operz=[";
	private static final String S_NON_RISULTANO_CONFIGURATI = "] non risultano configurati";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static SrvDispositivaCallModel callSrvDispositivaFromCompilation(ClientSessionContext csc, PdfInstanceModel pdfInstance, PdfModel pdf, byte[] pdfContent) throws Exception{
		return callSrvDispositiva(csc, pdfInstance, pdf, pdf.mainPdfData(), pdf.isProcessoNonVirtuoso(), pdfContent, AZIONE_SRV_PROCESSA, true);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String callSrvDispositivaFromOperatoreMOM(ClientSessionContext csc, PdfInstanceModel pdfInstance, PdfModel pdf, byte[] pdfContent) throws Exception{
		String azione = AZIONE_SRV_RISOTTOMETTI;
		if(pdfInstance.getCodDispositivaBMED().isNull())
			azione = AZIONE_SRV_PROCESSA;
		
		PdfDataModel pdfData = pdf.getPdfData();
		pdfInstance.setPdfXmlData(new StringType(PdfXmlUtils.xmlFromModel(pdfData, pdf, true)));
		boolean isProcessoNonVirtuoso = false;
		if(pdf.isInInserimentoMOM())
			isProcessoNonVirtuoso = true;
		
		String callSrvErrorMessage = callSrvDispositiva(csc, pdfInstance, pdf, pdf.mainPdfData(), isProcessoNonVirtuoso, pdfContent, azione, false).getCallSrvErrorMessage();
		
		// Aggiorno il codice dispositiva su DB, se è la prima volta che chiamo senza errori
		if(callSrvErrorMessage == null && azione.equals(AZIONE_SRV_PROCESSA)){ 
			try{
				new DAOObject(csc, PdfInstanceFacadeBean.DAO_MOM_XML_NAME).executeTableUpdateAccess("aggiornaCodDispositivaBMED", pdfInstance);			
			}catch(DAOException daoe){
				throw new Exception(daoe.toString());
			}
		}
		return callSrvErrorMessage;
	}
	
	/***********************************************************************************************/
	/* Richiamato dal WS "confermaModificaDocumentoMOM", non più usato
	/***********************************************************************************************/
	@Deprecated
	public static String callSrvDispositivaFromMOMValidation(ClientSessionContext csc, PdfInstanceModel pdfInstance, PdfModel pdf, byte[] pdfContent) throws Exception{
		return callSrvDispositiva(csc, pdfInstance, pdf, pdf.mainPdfData(), false, pdfContent, AZIONE_SRV_RISOTTOMETTI, false).getCallSrvErrorMessage();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static SrvDispositivaCallModel injectError(SrvDispositivaCallModel callModel, String callSrvErrorMessage) {
		callModel.setCallSrvErrorMessage(callSrvErrorMessage);
		return callModel;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static SrvDispositivaCallModel callSrvDispositiva(ClientSessionContext csc, PdfInstanceModel pdfInstance, PdfModel pdf, PdfDataModel pdfMainData, 
										  	 				  boolean isProcessononVirtuoso, byte[] pdfContent, String azione, boolean saveFile) throws Exception{
		
		SrvDispositivaCallModel callModel = new SrvDispositivaCallModel();

		if(pdfInstance.getPdfAnag().getPdfCodProdottoPrit().isNull() || 
		   pdfInstance.getPdfAnag().getPdfCodOperazionePrit().isNull()){
			return injectError(callModel,"Per questo modulo risulta configurata la chiamata al servizio \"dispositiva\" ma non risultano configurati i codici prodotto/operazione PRIT. Non è possibile proseguire.");
		}
		
		SaveFileInfo saveFileInfo = new SaveFileInfo();
		if(saveFile){
			if(pdfInstance.getPdfCompilationMode().equals(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_COPERNICO) ||
			   pdfInstance.getPdfCompilationMode().equals(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_FIRMA_DIGITALE)) {
				try{
					byte[] pdfContentForMom = extractPdfsForMOM(pdf, pdfInstance, pdfContent);
					saveFileInfo = NasStorage.saveFile(csc, "CLIENTIPROMOTORI", "Pdf_Compilabili", "PDF"+pdfInstance.getPdfInstanceId()+".pdf", new ByteArrayInputStream(pdfContentForMom));
				}catch(Exception e){
					return injectError(callModel,e.toString());
				}
			}
		}
		
		ProvideSrvDispositivaDataResponse driverSrvData = null;
		try {
			driverSrvData = PdfDriverCaller.callProvideSrvDispositivaData(csc, pdf);
		}catch(Exception e) {
			return injectError(callModel,"callProvideSrvDispositivaData: "+e.toString());
		}
		if(driverSrvData == null)
			driverSrvData = new ProvideSrvDispositivaDataResponse();
		
		DAOObject dao = new DAOObject(csc, PdfInstanceFacadeBean.DAO_MOM_XML_NAME);
		try{
			
			callModel.initCallModelFromCSS(csc);
			callModel.initCallModel(pdfInstance, pdf, saveFileInfo, azione, isProcessononVirtuoso);
			
			// Con pratiche digitali, solo se digitale, viene introdotta la gestione delle "eccezioni" di codice prodotto/operazione prit 
			// per riconoscere alcune specifiche operazioni
			boolean hasEccezione = false;
			if(!pdf.isOperatoreMOM() &&
			   !pdf.getPdfCompilationMode().equals(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_LIBERA) &&
			   !pdf.getPdfCompilationMode().equals(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_CHIMICA))	
				hasEccezione = loadEccezioneMOM(dao, pdf, pdfInstance, callModel);			

			// Valorizzazione dati generici del pdf da passare al servizio
			pdfInstance.instanceFromProcessData(pdf);
			
			// Creazione della dispositiva principale
			SrvDispositivaElemModel mainDispo = createMainDispo(csc, callModel, pdf, pdfInstance, hasEccezione);
			if( mainDispo.getCodiceSurrogatoProdottoServizio().isNull() || mainDispo.getCodiceTipoOperazioneProdottoServizio().isNull())
				return injectError(callModel,"Servizio Dispositiva: i codici surrogati "+(hasEccezione?"eccezione ":"")+"PRIT per prod=["+mainDispo.getCodProdottoPrit()+S_OPERZ_SUBSTR+mainDispo.getCodOperazionePrit()+S_NON_RISULTANO_CONFIGURATI);
			
			// Aggiungo clienti ed agenti alla dispositiva principale. Se in anagrafica il tipo processo è MOM2 il pdf è "ante" mop per cui si deve usare il comportamento ante MOP
			if(pdf.isTipoProcessoSedeMOM2() && !pdf.mainPdfAnag().getTipoProcessoSedeInPdfAnag().equals(TIPO_PROCESSO_DI_SEDE_MOM2)) {
				addClientiMom2(csc, mainDispo, pdf, pdf.mainPdfData());
				addClientiBeneficiariCensitiMom2(mainDispo, driverSrvData);
			}else {
				addMainClienti(pdfInstance, mainDispo, driverSrvData);
			}
			addAgenti(mainDispo, pdfMainData, driverSrvData);
			callModel.getElencoDispositive().add(mainDispo);

			// Creazione delle ulteriori dispositive,solo per processo di sede MOM2, correlate ai pdf e i vincoli escludendo la principale (mainDispo, i=1)
			if(pdf.isTipoProcessoSedeMOM2()) {
				for(int i=1;i<pdf.getPdfAnags().size();i++){
					
					PdfAnagModel pdfDettAnag = pdf.getPdfAnags().get(i);
					PdfDataModel pdfDettData = (PdfDataModel)pdf.getPdfData().getPdfs().get(i);	
					
					// Se non prevista l'integrazione con MOM non invio la relativa dispositiva
					if(pdfDettAnag.getCallSrvDispositivaBMED().isNull())
						continue;
					
					SrvDispositivaElemModel otherDispo = createOtherMom2Dispo(csc, callModel, pdf, pdfDettAnag, pdfDettData, pdfInstance, "-"+(i+1));
					if( otherDispo.getCodiceSurrogatoProdottoServizio().isNull() || otherDispo.getCodiceTipoOperazioneProdottoServizio().isNull())
						return injectError(callModel,"Servizio Dispositiva: i codici surrogati PRIT per prod=["+otherDispo.getCodProdottoPrit()+S_OPERZ_SUBSTR+otherDispo.getCodOperazionePrit()+S_NON_RISULTANO_CONFIGURATI);
	
					addClientiMom2(csc, otherDispo, pdf, pdfDettData);
					addAgenti(otherDispo, pdfDettData, new ProvideSrvDispositivaDataResponse());				
					callModel.getElencoDispositive().add(otherDispo);
				}
			}
			
			// Salvo il numero di dispo aggiunte automaticamente dal motore per poi poter gestire i vincoli generati dal driver
			int numDispoAuto = callModel.getElencoDispositive().size(); 
			
			// Aggiungo gli allegati, se presenti
			addAllegatiMainDispo(mainDispo, pdf);
			
			// Clono tante dispositive quante ne vengono specificate dal driver e le inizializzo con i dati specificati dal driver
			for(int i=0; i<driverSrvData.getDispoAggiuntive().size();i++) {
				
				DispoAggiuntiva driverAggDispo = driverSrvData.getDispoAggiuntive().get(i);
				
				if(pdf.isTipoProcessoSedeMOM2() && findDispositivaAggiuntivaCensimentoBeneficiari(driverSrvData) == driverAggDispo) // Se MOM2 e la dispositiva è quella del censimento benficiari non la passiamo a MOM
					continue;
				
				SrvDispositivaElemModel aggDispo = createAggDispo(csc, driverAggDispo, mainDispo, pdfInstance, "-"+(callModel.getElencoDispositive().size()+1));
				
				if(aggDispo.getCodProdottoPrit().isNull() || aggDispo.getCodOperazionePrit().isNull())
					return injectError(callModel,"Servizio Dispositiva: i codici PRIT della dispo aggiuntiva per pdfCode=["+driverAggDispo.getPritRetrieveInfo().getPdfCode()+"] chiave=["+driverAggDispo.getPritRetrieveInfo().getChiave()+S_NON_RISULTANO_CONFIGURATI);
				if(aggDispo.getCodiceSurrogatoProdottoServizio().isNull() || aggDispo.getCodiceTipoOperazioneProdottoServizio().isNull())
					return injectError(callModel,"Servizio Dispositiva: i codici surrogati PRIT della dispo aggiuntiva per prod=["+aggDispo.getCodProdottoPrit()+S_OPERZ_SUBSTR+aggDispo.getCodOperazionePrit()+S_NON_RISULTANO_CONFIGURATI);
				
				addAgenti(aggDispo, pdfMainData, driverSrvData);				
				callModel.getElencoDispositive().add(aggDispo);
			}
			
			// Gestisco gli eventuali vincoli specificati dal driver
			for(int i=0; i<driverSrvData.getVincoliDispositive().size();i++) {

				if(pdf.isTipoProcessoSedeMOM2()) {
					DispoAggiuntiva driverAggDispo = driverSrvData.getDispoAggiuntive().get(i);
					if(findDispositivaAggiuntivaCensimentoBeneficiari(driverSrvData) == driverAggDispo) // Se MOM2 e la dispositiva è quella del censimento benficiari non la passiamo a MOM
						continue;
				}

				VincoloDispositiva driverVincolo = driverSrvData.getVincoliDispositive().get(i);
				
				int dispoIndex = driverVincolo.getIndiceDispositivaVincolante() == VincoloDispositiva.INDICE_DISPOSITIVA_PRINCIPALE ? 0 : numDispoAuto+driverVincolo.getIndiceDispositivaVincolante();
				SrvDispositivaElemModel dispoVincolante = (SrvDispositivaElemModel)callModel.getElencoDispositive().get(dispoIndex);
				
				dispoIndex = driverVincolo.getIndiceDispositivaVincolata() == VincoloDispositiva.INDICE_DISPOSITIVA_PRINCIPALE ? 0 : numDispoAuto+driverVincolo.getIndiceDispositivaVincolata();
				SrvDispositivaElemModel dispoVincolata = (SrvDispositivaElemModel)callModel.getElencoDispositive().get(dispoIndex);
				
				SrvVincoloDispositivaElemModel vincolo = new SrvVincoloDispositivaElemModel();
				vincolo.setCodiceOperazioneDispositivaSistemaOrigineVincolante(dispoVincolante.getCodiceOperazioneDispositivaSistemaOrigine());
				vincolo.setCodiceOperazioneDispositivaSistemaOrigineVincolata(dispoVincolata.getCodiceOperazioneDispositivaSistemaOrigine());
				callModel.getElencoVincoliDispositive().add(vincolo);
			}
			
		}catch(Exception e){
			return injectError(callModel,e.toString());
		}catch(DAOException daoe){
			return injectError(callModel,daoe.toString());
		}
		
		try{
			// Inizializzo globalData nelle istanze di dispositiva per effettuare la chiamata al servizio con i dati ripetuti in ogni elemento
			for(int i=0;i<callModel.getElencoDispositive().size();i++) {
				SrvDispositivaElemModel dispo = (SrvDispositivaElemModel)callModel.getElencoDispositive().get(i);
				dispo.initGlobalData(callModel);
			}
			// Richiamo il servizio
			DAOOSBResultModel wsRes = dao.executeOSBAccess("callSrvDispositiva", callModel);
			// Gestione dell'esito
			if(wsRes.getWsCallData().getStatus() == OSBCallData.STATUS_SERVICE_DISABLED){
				return injectError(callModel,"Servizio dispositiva disabilitato.");
			}else if(wsRes.getWsCallData().getStatus() != OSBCallData.STATUS_OK){	
				return injectError(callModel,"Errore di comunicazione con il servizio dispositiva: "+wsRes.getWsCallData().getMessage());
			}else if(!callModel.getEsito().equalsIgnoreCase("ok")){
				return injectError(callModel,"Errore ritornato dal servizio dispositiva");
			}else if(callModel.getElencoDispositiveResult().size() != callModel.getElencoDispositive().size()){
				return injectError(callModel,"Errore dal servizio dispositiva: il numero di elementi ritornati ["+callModel.getElencoDispositiveResult().size()+"] non è coerente con il numero di dispositive in input ["+callModel.getElencoDispositive().size()+"]");
			}else if(callModel.getCodDispositivaBMED().isNull()){
				return injectError(callModel,"Errore dal servizio dispositiva: codice dispositiva BMED non ritornato");
			}
		}catch(DAOException daoe){
			return injectError(callModel,"Eccezione nel richiamo al servizio dispositiva.");
		}finally {
			// Ripulisco globalData
			for(int i=0;i<callModel.getElencoDispositive().size();i++) {
				SrvDispositivaElemModel dispo = (SrvDispositivaElemModel)callModel.getElencoDispositive().get(i);
				dispo.initGlobalData(null);
			}
		}
		
		String errMsg = legaDispoResult(callModel);
		if(errMsg != null)
			return injectError(callModel,errMsg);
		
		pdfInstance.setCodDispositivaBMED(callModel.getCodDispositivaBMED()); 
		pdfInstance.setIdFileNAS(new StringType(saveFileInfo.getIdFile()));
		return callModel;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static DispoAggiuntiva findDispositivaAggiuntivaCensimentoBeneficiari(ProvideSrvDispositivaDataResponse driverSrvData) {
		for(int i=0; i<driverSrvData.getDispoAggiuntive().size();i++) {			
			DispoAggiuntiva driverAggDispo = driverSrvData.getDispoAggiuntive().get(i);
			if(driverAggDispo.getPritRetrieveInfo().getChiave().equals("CENSIMENTO_ANAGRAFICO_BENEFICIARI") &&
			   driverAggDispo.getTipoDispositiva().equals("CENSIMENTO_ANAGRAFICO"))
				return driverAggDispo;
		}
		return null;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static String legaDispoResult(SrvDispositivaCallModel callModel) {

		int founded = 0;
		for(int i=0;i<callModel.getElencoDispositive().size();i++) {
			SrvDispositivaElemModel aggDispo = (SrvDispositivaElemModel)callModel.getElencoDispositive().get(i);
			for(int j=0;j<callModel.getElencoDispositiveResult().size();j++) {
				SrvDispositivaResultElemModel resDispo = (SrvDispositivaResultElemModel)callModel.getElencoDispositiveResult().get(j);
				if(resDispo.getCodiceOperazioneDispositiva().isNull())
					return "Errore dal servizio dispositiva: uno o più elementi ritornati non riportano il codice dispositiva BMED";
				if(resDispo.getCodiceOperazioneDispositivaSistemaOrigine().equals(aggDispo.getCodiceOperazioneDispositivaSistemaOrigine())) {
					aggDispo.setDispositivaResult(resDispo);
					founded++;
					break;
				}
			}
			
		}	
		if(founded != callModel.getElencoDispositive().size()) 
			return "Errore dal servizio dispositiva: uno o più elementi ritornati non combaciano con le chiavi delle dispositive";		
		return null;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static boolean loadEccezioneMOM(DAOObject dao, PdfModel pdf, PdfInstanceModel pdfInstance, SrvDispositivaCallModel callModel) throws DAOException{
		
		String daoAccess = "loadEccezionePritMom";
		boolean hasEccezione = false;
					
		if(pdfInstance.getPutOnSignedProcessBatchQueue().isNull() || pdfInstance.getPutOnSignedProcessBatchQueue().equals("S")){
			String chiaveEccezioneMOM = "EXC_MOM";
			callModel.setChiaveEccezioniMom(new StringType(chiaveEccezioneMOM));
			callModel.setCodProd(pdfInstance.getCodProd());
			DAOQueryResultModel qRes = dao.executeQueryAccess(daoAccess, callModel); 
			if(qRes.getResult().size() == 0 && !pdfInstance.getChiavePritMomInfo().isNull()){
				callModel.setChiaveEccezioniMom(new StringType(chiaveEccezioneMOM+"#"+pdfInstance.getChiavePritMomInfo()));
				qRes = dao.executeQueryAccess(daoAccess, callModel); 
			}					
			if(qRes.getResult().size() > 0)
				hasEccezione = true;
		}
		
		if(!hasEccezione && pdfInstance.getPutOnPostCompletionProcessBatchQueue().equals("S")){
			String chiaveEccezioneMOM = "EXC_MOM_POSTCOMPL";
			callModel.setCodProd(pdfInstance.getCodProd());
			callModel.setChiaveEccezioniMom(new StringType(chiaveEccezioneMOM));
			DAOQueryResultModel qRes = dao.executeQueryAccess(daoAccess, callModel); 
			if(qRes.getResult().size() == 0 && !pdfInstance.getChiavePritMomInfo().isNull()){
				callModel.setChiaveEccezioniMom(new StringType(chiaveEccezioneMOM+"#"+pdfInstance.getChiavePritMomInfo()));
				qRes = dao.executeQueryAccess(daoAccess, callModel); 
			}					
			if(qRes.getResult().size() > 0)
				hasEccezione = true;
		}
		return hasEccezione;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static SrvDispositivaElemModel createMainDispo(ClientSessionContext csc, SrvDispositivaCallModel callModel,
														   PdfModel pdf, PdfInstanceModel pdfInstance, boolean hasEccezione) throws Exception, DAOException {
		
		PdfAnagModel pdfMainAnag = pdf.mainPdfAnag();
		PdfDataModel pdfMainData = pdf.mainPdfData();
		
		SrvDispositivaElemModel dispo = new SrvDispositivaElemModel();
		
		dispo.setCodiceProcessoOperativo(new StringType(TIPO_PROCESSO_DI_SEDE_MOM1));
		if(pdf.isTipoProcessoSedeMOM2() || pdfMainAnag.getTipoProcessoSedeInPdfAnag().equals(TIPO_PROCESSO_DI_SEDE_MOM2))
			dispo.setCodiceProcessoOperativo(new StringType(TIPO_PROCESSO_DI_SEDE_MOM2));
		
		if(pdf.isTipoProcessoSedeMOM2()){
			dispo.setCodProdottoPrit(new StringType(pdfMainAnag.getPdfCodProdottoPrit().toString()));
			dispo.setCodOperazionePrit(new StringType(pdfMainAnag.getPdfCodOperazionePrit().toString()));
			ProvidePritDataResponse pritData = PdfDriverCaller.callProvideMom2PritData(csc, pdf, pdfMainData);
			if(pritData != null) {
				PritMomInfo pritInfo = PritMomInfoLoader.loadPritMomInfo(csc, pdfMainAnag, pritData.getChiavePritRigaConfigurata());
				if(pritInfo != null){
					dispo.setCodProdottoPrit(new StringType(Integer.toString(pritInfo.getCodProdotto())));
					dispo.setCodOperazionePrit(new StringType(Integer.toString(pritInfo.getCodOperazione())));
				}
			}
		}else{
			if(hasEccezione) { // Se esiste l'eccezione MOM allora i codici sono stati letti su "callModel" nella "loadEccezioneMOM"
				dispo.setCodProdottoPrit(new StringType(callModel.getCodProdottoPrit().toString()));
				dispo.setCodOperazionePrit(new StringType(callModel.getCodOperazionePrit().toString()));
			}else{
				dispo.setCodProdottoPrit(new StringType(pdfInstance.getPdfAnag().getPdfCodProdottoPrit().toString()));
				dispo.setCodOperazionePrit(new StringType(pdfInstance.getPdfAnag().getPdfCodOperazionePrit().toString()));
			}
		}
		dispo.setDescrizioneNote(new StringType(pdfMainAnag.getPdfCode().toString()+" "+pdfMainAnag.getPdfDescr().toString()));
		dispo.setCodiceOperazioneDispositivaSistemaOrigine(pdfInstance.getPdfInstanceId());

		// Carico i codici surrogati
		new DAOObject(csc, PdfInstanceFacadeBean.DAO_MOM_XML_NAME).executeQueryAccess(DAO_ACCESS_LOAD_CODICI_SURROGATI_PRIT, dispo); 
		if(dispo.getCodiceSurrogatoProdottoServizio().isNull() || dispo.getCodiceTipoOperazioneProdottoServizio().isNull())
			return dispo;

		dispo.setContrNRapportoRiferimento(pdfInstance.getNumeroContratto());
		dispo.setCodiceBarcode(pdfInstance.getPdfBarcode());

		dispo.setNumeroProposta(pdfInstance.getNumeroProposta());
		dispo.setCodicePropostaCorrelata(pdfInstance.getNumeroPropostaCorrelata());
		dispo.setValoreDispositiva(pdfInstance.getImporto());
		if(!dispo.getValoreDispositiva().isNull())
			dispo.setCodiceTipoValoreDispositiva(new StringType("IMP"));
		
		AbstractType codiceScopoRapporto = pdfMainData.read(PdfPredefinedFields.SCOPO_RAPPORTO);
		if(codiceScopoRapporto != null && !codiceScopoRapporto.isNull())
			dispo.setCodiceScopoRapporto(new StringType(codiceScopoRapporto.toString()));
		
		AbstractType codTipoDocumMom = pdf.mainPdfData().readProperty(PdfPredefinedFields.PRAT_DIG_MOM_COD_TIPO_DOCUM);
		dispo.setCodTipoDocum(new StringType(codTipoDocumMom == null?"":codTipoDocumMom.toString()));
		
		return dispo;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void addAllegatiMainDispo(SrvDispositivaElemModel mainDispo, PdfModel pdf) {
		mainDispo.setAllegati(null);
		if(pdf.getPdfInstanceAttachments().size() == 0) {
			return;
		}
		mainDispo.setAllegati(new ListType(AllegatoMOMModel.class));
		for(int i=0; i<pdf.getPdfInstanceAttachments().size();i++) {
			PdfInstanceAttachModel allPdf = (PdfInstanceAttachModel)pdf.getPdfInstanceAttachments().get(i);
			AllegatoMOMModel allMOM = new AllegatoMOMModel();
			allMOM.setIdFileNasAllegato(allPdf.getIdFileNasAllegato());
			String descrAll = allPdf.getDescrizioneAllegato().toString();
			if(descrAll.length() > 250)
				descrAll = descrAll.substring(0, 250);
			allMOM.setDescrizioneAllegato(new StringType(descrAll));
			allMOM.setDenominazioneFileAllegato(allPdf.getDenominazioneFileAllegato());
			allMOM.setProgressivoAllegatoSrvMom(new IntegerType(i+2));
			allMOM.setCodTipoDocum(mainDispo.getCodTipoDocum());
			mainDispo.getAllegati().add(allMOM);
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static SrvDispositivaElemModel createOtherMom2Dispo(ClientSessionContext csc, SrvDispositivaCallModel callModel, 
														    	PdfModel pdf, PdfAnagModel pdfAnag, PdfDataModel pdfData,
														    	PdfInstanceModel pdfInstance, String aggDispoIdentifier) throws Exception, DAOException {		

		SrvDispositivaElemModel dispo = new SrvDispositivaElemModel();
		
		dispo.setCodiceProcessoOperativo(new StringType(TIPO_PROCESSO_DI_SEDE_MOM2));
		if(pdfAnag.getTipoProcessoSede().equals(TIPO_PROCESSO_DI_SEDE_MOMO))
			dispo.setCodiceProcessoOperativo(new StringType(TIPO_PROCESSO_DI_SEDE_MOM1)); // I MOMO verso pratiche digitali sonon MOM1 (valore MOM)
		
		dispo.setPdfIndex(pdfData.getPdfIndex().intValue());
		dispo.setCodProdottoPrit(new StringType(pdfAnag.getPdfCodProdottoPrit().toString()));
		dispo.setCodOperazionePrit(new StringType(pdfAnag.getPdfCodOperazionePrit().toString()));
		ProvidePritDataResponse pritData = PdfDriverCaller.callProvideMom2PritData(csc, pdf, pdfData);
		if(pritData != null) {
			PritMomInfo pritInfo = PritMomInfoLoader.loadPritMomInfo(csc, pdfAnag, pritData.getChiavePritRigaConfigurata());
			if(pritInfo != null){
				dispo.setCodProdottoPrit(new StringType(Integer.toString(pritInfo.getCodProdotto())));
				dispo.setCodOperazionePrit(new StringType(Integer.toString(pritInfo.getCodOperazione())));
			}
		}
		dispo.setDescrizioneNote(new StringType(pdfAnag.getPdfCode().toString()+" "+pdfAnag.getPdfDescr().toString()));
		dispo.setCodiceOperazioneDispositivaSistemaOrigine(new StringType(pdfInstance.getPdfInstanceId()+aggDispoIdentifier));

		// Carico i codici surrogati
		new DAOObject(csc, PdfInstanceFacadeBean.DAO_MOM_XML_NAME).executeQueryAccess(DAO_ACCESS_LOAD_CODICI_SURROGATI_PRIT, dispo); 
		if(dispo.getCodiceSurrogatoProdottoServizio().isNull() || dispo.getCodiceTipoOperazioneProdottoServizio().isNull())
			return dispo;

		dispo.setContrNRapportoRiferimento(pdfInstance.getNumeroContratto());
		dispo.setCodiceBarcode(pdfData.getPdfBarcode());

		dispo.setNumeroProposta(pdfInstance.getNumeroProposta());
		dispo.setCodicePropostaCorrelata(pdfInstance.getNumeroPropostaCorrelata());
		dispo.setValoreDispositiva(pdfInstance.getImporto());
		if(!dispo.getValoreDispositiva().isNull())
			dispo.setCodiceTipoValoreDispositiva(new StringType("IMP"));
		AbstractType codiceScopoRapporto = pdfData.read(PdfPredefinedFields.SCOPO_RAPPORTO);
		if(codiceScopoRapporto != null && !codiceScopoRapporto.isNull())
			dispo.setCodiceScopoRapporto(new StringType(codiceScopoRapporto.toString()));

		AbstractType codTipoDocumMom = pdf.mainPdfData().readProperty(PdfPredefinedFields.PRAT_DIG_MOM_COD_TIPO_DOCUM);
		dispo.setCodTipoDocum(new StringType(codTipoDocumMom == null?"":codTipoDocumMom.toString()));

		// Vincolo (la main vincolata dall'other?)
		SrvVincoloDispositivaElemModel vincolo = new SrvVincoloDispositivaElemModel();
		vincolo.setCodiceOperazioneDispositivaSistemaOrigineVincolante(dispo.getCodiceOperazioneDispositivaSistemaOrigine());
		vincolo.setCodiceOperazioneDispositivaSistemaOrigineVincolata(pdfInstance.getPdfInstanceId());
		vincolo.setCodiceTipologiaVincoloDispositiva(new StringType("M"));
		callModel.getElencoVincoliDispositive().add(vincolo);
		
		return dispo;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static SrvDispositivaElemModel createAggDispo(ClientSessionContext csc, DispoAggiuntiva driverAggDispo, SrvDispositivaElemModel mainDispo,
														  PdfInstanceModel pdfInstance, String aggDispoIdentifier) throws DAOException {
		
		SrvDispositivaElemModel dispo = new SrvDispositivaElemModel();
		
		dispo.setCodiceProcessoOperativo(mainDispo.getCodiceProcessoOperativo());
		dispo.setMainPdfInstanceId(pdfInstance.getPdfInstanceId());
		dispo.setTipoDispositiva(new StringType(driverAggDispo.getTipoDispositiva()));
		
		loadAggDispoPritCodes(csc, dispo, driverAggDispo);
		if(dispo.getCodProdottoPrit().isNull() || dispo.getCodOperazionePrit().isNull())
			return dispo;
		
		dispo.setDescrizioneNote(new StringType(driverAggDispo.getDescrizione()));
		dispo.setCodiceOperazioneDispositivaSistemaOrigine(new StringType(pdfInstance.getPdfInstanceId()+aggDispoIdentifier));

		// Carico i codici surrogati
		new DAOObject(csc, PdfInstanceFacadeBean.DAO_MOM_XML_NAME).executeQueryAccess(DAO_ACCESS_LOAD_CODICI_SURROGATI_PRIT, dispo); 
		if(dispo.getCodiceSurrogatoProdottoServizio().isNull() || dispo.getCodiceTipoOperazioneProdottoServizio().isNull())
			return dispo;

		dispo.setContrNRapportoRiferimento(new StringType(driverAggDispo.getNumeroContratto()));
		dispo.setCodiceBarcode(new StringType(driverAggDispo.getBarcode()));

		dispo.setNumeroProposta(new StringType(driverAggDispo.getNumeroProposta()));
		dispo.setCodicePropostaCorrelata(new StringType(driverAggDispo.getCodicePropostaCorrelata()));
		dispo.setValoreDispositiva(new DoubleType(driverAggDispo.getValoreDispositiva()));
		if(!dispo.getValoreDispositiva().isNull())
			dispo.setCodiceTipoValoreDispositiva(new StringType("IMP"));
		dispo.setCodTipoDocum(mainDispo.getCodTipoDocum());
		
		for(int i=0;i<driverAggDispo.getSoggetti().size();i++){
			SoggettoDispositivaCallModel soggetto = driverAggDispo.getSoggetti().get(i);
			soggetto.setProgressivoSoggettoDispositiva(new StringType(""+(i+1)));
			dispo.getSoggetti().add(soggetto);
		}
		
		
		return dispo;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private static void loadAggDispoPritCodes(ClientSessionContext csc, SrvDispositivaElemModel dispo, DispoAggiuntiva driverAggDispo) throws DAOException{
		
		PritRetrieveInfo pritRetrieveInfo = driverAggDispo.getPritRetrieveInfo();
		String chiave = pritRetrieveInfo.getChiave();
		String pdfCode = pritRetrieveInfo.getPdfCode(); 
		if(chiave == null || chiave.length() == 0 || pdfCode == null || pdfCode.length() == 0)
			return;
		
		ListType infosPrit = DAOObject.executeDynaQueryAccess(csc, "CEPE", 
										"select PRIT_C_PRODOTTO, PRIT_C_OPERAZIONE "+
										"from PDF_INFO_PRIT "+
										"where PDF_CODE='"+pdfCode+"' "+
										"and CHIAVE='"+chiave+"'", null, MapCommandDataModel.class).getResult();
		if(infosPrit == null || infosPrit.size() == 0)
			return;
		
		MapCommandDataModel infoPrit = (MapCommandDataModel)infosPrit.get(0);
		IntegerType codProd = (IntegerType)infoPrit.readProperty("pritCProdotto");
		IntegerType codOpe = (IntegerType)infoPrit.readProperty("pritCOperazione");
		if(codProd == null || codOpe == null || codProd.intValue() == 0 || codOpe.intValue() == 0)
			return;
		
		dispo.setCodProdottoPrit(new StringType(codProd.toString()));
		dispo.setCodOperazionePrit(new StringType(codOpe.toString()));
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void addClientiMom2(ClientSessionContext csc, SrvDispositivaElemModel srvDispo, 
									   PdfModel pdf, PdfDataModel pdfData) throws Exception {
		
		List<ElementoSquadra> squadra = PdfDriverCaller.callProvideSquadraData(csc, pdf, pdfData, new String[] {ElementoSquadra.Ruoli.SOTTOSCRITTORE,
																												ElementoSquadra.Ruoli.COSOTTOSCRITTORE,
																												ElementoSquadra.Ruoli.LEGALERAPPRESENTANTE,
																												ElementoSquadra.Ruoli.ASSICURATO,
																												ElementoSquadra.Ruoli.TERZOPAGATORE,
																												ElementoSquadra.Ruoli.BENEFICIARIO,
																												ElementoSquadra.Ruoli.TITOLARE_EFFETTIVO_BENEFICIARIO});
		for(ElementoSquadra elSq : squadra) {
			boolean isProspect = false;
			StringType codCli = (StringType)pdfData.read(elSq.getNomeCampoNdg());
			if(codCli == null || codCli.isNull()) {
				codCli = (StringType)pdfData.read(elSq.getNomeCampoIdCensimento());
				isProspect = true;
			}
			if(codCli == null || codCli.isNull())
				continue;
			
			if(!isProspect)
				codCli = new StringType(Tools.fillSx(codCli.toString(), '0', 11));
			
			SoggettoDispositivaCallModel cliente = new SoggettoDispositivaCallModel();
			cliente.setCodiceRuoloSoggetto(new StringType(elSq.getRuoli()));
			cliente.setCodiceTipoSoggetto(isProspect ? new StringType("3") : new StringType("2"));
			cliente.setCodiceSoggettoOriginale(codCli);
			if(isProspect){
				cliente.setCodiceSoggettoEsterno(codCli);
				PdfPersonModel p = pdfData.getPerson(codCli.toString());
				if(p != null) {
					cliente.setDenominazioneNomeSoggettoEsterno((StringType)p.readProperty("nome"));
					cliente.setDenominazioneCognomeSoggettoEsterno((StringType)p.readProperty("cognome"));
				}
			}
			addClienteMom2(cliente, srvDispo);
		}		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void addClientiBeneficiariCensitiMom2(SrvDispositivaElemModel mainDispo, ProvideSrvDispositivaDataResponse driverSrvData) throws Exception {
		// Il driver per i beneficiari specifica un elenco di "soggetti cliente" aggiuntivi, con ruolo 28, che con MOM2 dobbiamo aggiungere ai clienti della mainDispo
		if(!driverSrvData.getClientiAggiuntiviDispositiva().isEmpty() && findDispositivaAggiuntivaCensimentoBeneficiari(driverSrvData) != null) {
			for(SoggettoDispositivaCallModel sogg : driverSrvData.getClientiAggiuntiviDispositiva()) {
				if(!sogg.getCodiceRuoloSoggetto().equals(ElementoSquadra.Ruoli.BENEFICIARIO))
					continue;
				sogg.setProgressivoSoggettoDispositiva(new StringType(""+(mainDispo.getSoggetti().size()+1)));
				addClienteMom2(sogg, mainDispo);		
			}
		}
	}	

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void addClienteMom2(SoggettoDispositivaCallModel cliente, SrvDispositivaElemModel srvDispo) {
		for(int i=0; i < srvDispo.getSoggetti().size(); i++) {
			SoggettoDispositivaCallModel cliel = (SoggettoDispositivaCallModel)srvDispo.getSoggetti().get(i);
			// /Se il soggetto di un certo tipo appare già con lo stesso ruolo non lo aggiungiamo all'elenco dei soggetti da passare al servizio
			if( cliel.getCodiceSoggettoOriginale().equals(cliente.getCodiceSoggettoOriginale()) && 
				cliel.getCodiceRuoloSoggetto().equals(cliente.getCodiceRuoloSoggetto()) &&
				cliel.getCodiceTipoSoggetto().equals(cliente.getCodiceTipoSoggetto()))
				return;
		}
		cliente.setProgressivoSoggettoDispositiva(new StringType(""+(srvDispo.getSoggetti().size()+1)));
		srvDispo.getSoggetti().add(cliente);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void addMainClienti(PdfInstanceModel pdfInstance, SrvDispositivaElemModel mainDispo, ProvideSrvDispositivaDataResponse driverSrvData) throws Exception {		
		// Caricamento soggetti "CLIENTE" alla dispositiva principale
		for(int i=0;i<DataLoader.MAX_NUM_CLIENTI;i++){
			
			PdfPersonInstanceModel pdfCli = (PdfPersonInstanceModel)Tools.getPropertyValue(pdfInstance, "cli"+(i+1));
			if(pdfCli == null)
				break;
			if(pdfCli.getNdg().isNull() && pdfCli.getIdCensimento().isNull())
				continue;
			
			SoggettoDispositivaCallModel cliente = new SoggettoDispositivaCallModel();
			if(i==0)
				cliente.setCodiceRuoloSoggetto(new StringType("6"));
			else
				cliente.setCodiceRuoloSoggetto(new StringType("13"));
			cliente.setProgressivoSoggettoDispositiva(new StringType(""+(i+1)));
			cliente.setCodiceTipoSoggetto(pdfCli.getNdg().isNull() ? new StringType("3") : new StringType("2"));
			cliente.setCodiceSoggettoOriginale(pdfCli.getNdg().isNull() ? pdfCli.getIdCensimento() : pdfCli.getNdg());
			if(pdfCli.getNdg().isNull()){
				cliente.setCodiceSoggettoEsterno(new StringType(pdfCli.getIdCensimento().toString()));
				cliente.setDenominazioneNomeSoggettoEsterno(new StringType(pdfCli.getNome().toString()));
				cliente.setDenominazioneCognomeSoggettoEsterno(new StringType(pdfCli.getCognome().toString()));
			}
			mainDispo.getSoggetti().add(cliente);
		}
			
		if(!driverSrvData.getClientiAggiuntiviDispositiva().isEmpty()) {	// Il driver specifica un elenco di "soggetti cliente" aggiuntivi
			// Aggiungo i "sogetti cliente" specificati dal driver (ad es. la TBN imposta anche i beneficiari)
			for(SoggettoDispositivaCallModel sogg : driverSrvData.getClientiAggiuntiviDispositiva()) {
				sogg.setProgressivoSoggettoDispositiva(new StringType(""+(mainDispo.getSoggetti().size()+1)));
				mainDispo.getSoggetti().add(sogg);				
			}
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void addAgenti(SrvDispositivaElemModel dispo, PdfDataModel pdfMainData, ProvideSrvDispositivaDataResponse driverSrvData) {
		// Caricamento soggetti "AGENTE"
		if(driverSrvData.getAgentiDispositiva().isEmpty()){ // Il driver non specifica alcun elenco di "soggetti agente", imposto l'agente principale
			SoggettoDispositivaCallModel agente = new SoggettoDispositivaCallModel();
			agente.setCodiceRuoloSoggetto(new StringType("1"));
			agente.setProgressivoSoggettoDispositiva(new StringType(""+(dispo.getSoggetti().size()+1)));
			agente.setCodiceTipoSoggetto(new StringType("1"));
			String codAge = pdfMainData.getAgente().getCodAgente().toString(); // Imposto l'agente inputato nel pdf
			if(codAge.length() > 0)
				codAge = Tools.fillSx(codAge, '0', 10);
			agente.setCodiceSoggettoOriginale(new StringType(codAge));
			dispo.getSoggetti().add(agente);
		}else{
			// Imposto i "sogetti agente" specificati dal driver (ad es. la raccomandazione non virtuosa lo fa per la gestione degli specialisti protezione)
			for(SoggettoDispositivaCallModel sogg : driverSrvData.getAgentiDispositiva()) {
				sogg.setProgressivoSoggettoDispositiva(new StringType(""+(dispo.getSoggetti().size()+1)));
				dispo.getSoggetti().add(sogg);				
			}
		}
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private static byte[] extractPdfsForMOM(PdfModel pdf, PdfInstanceModel pdfInstance, byte[] pdfContent) throws PdfWebFormsException {

		if(pdfInstance.getPdfsForMOM().isNull())
			return pdfContent;

		ArrayList<Integer> pagesToExtract = new ArrayList<Integer>(); 
		ArrayList<String> pdfsForMom = new ArrayList<String>(Arrays.asList(pdfInstance.getPdfsForMOM().toString().split(",")));
		for(int pdfsForMomIndex=0;pdfsForMomIndex<pdfsForMom.size();pdfsForMomIndex++){
			int globalPageIndex = 1;
			int pdfIndexToExtract = Integer.parseInt(pdfsForMom.get(pdfsForMomIndex)) - 1;
			for(int pdfIndex=0;pdfIndex<pdf.getPdfAnags().size();pdfIndex++) {
				PdfAnagModel pdfAnag = pdf.getPdfAnags().get(pdfIndex);
				for(int pdfPages=0;pdfPages<pdfAnag.getPdfNumPages().intValue();pdfPages++) {
					if(pdfIndex == pdfIndexToExtract)
						pagesToExtract.add(globalPageIndex);
					globalPageIndex++;
				}
			}
		}		
		return PdfEngine.extractPdfPages(pdfContent, pagesToExtract);		
	}

}
