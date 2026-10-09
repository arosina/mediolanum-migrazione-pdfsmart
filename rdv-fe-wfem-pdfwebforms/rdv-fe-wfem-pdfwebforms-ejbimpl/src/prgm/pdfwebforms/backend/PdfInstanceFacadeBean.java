package prgm.pdfwebforms.backend;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.ejb.EJBException;

import com.atosorigin.wfem.backend.FacadeObject;
import com.atosorigin.wfem.coddesc.CodDescDataList;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.controller.Constants;
import com.atosorigin.wfem.dao.DAOOSBResultModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.DAOTableResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.dao.exceptions.NoRowsAffected;
import com.atosorigin.wfem.dbjavaclasses.DBJavaClassLoader;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.AbstractTypePropertyDescriptor;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;
import com.atosorigin.wfem.util.Tools;
import com.atosorigin.wfem.util.VersionPrinter;
import com.atosorigin.wfem.util.XmlServiceCallData;
import com.itextpdf.text.pdf.AcroFields;

import prgm.pdfwebforms.agevolazioni.AgevolazioneDataLoader;
import prgm.pdfwebforms.agevolazioni.AgevolazioneModel;
import prgm.pdfwebforms.carrello.CarrelloDataLoader;
import prgm.pdfwebforms.carrello.DispositivaCarrelloModel;
import prgm.pdfwebforms.core.PdfConfig;
import prgm.pdfwebforms.core.PdfContextUtils;
import prgm.pdfwebforms.core.PdfFieldInfos;
import prgm.pdfwebforms.core.PdfNasUtil;
import prgm.pdfwebforms.core.PdfPredefinedFields;
import prgm.pdfwebforms.core.PdfWebFormsException;
import prgm.pdfwebforms.core.PdfXmlUtils;
import prgm.pdfwebforms.dataloader.DataLoader;
import prgm.pdfwebforms.display.PdfPage;
import prgm.pdfwebforms.drivers.PdfBaseDriver;
import prgm.pdfwebforms.drivers.PdfBasePageDriver;
import prgm.pdfwebforms.drivers.PdfDriverCaller;
import prgm.pdfwebforms.drivers.io.InitPdfOutputData;
import prgm.pdfwebforms.drivers.io.LoadPdfOutputData;
import prgm.pdfwebforms.drivers.io.ProvideAgevolazioneDipendentiDataResponse;
import prgm.pdfwebforms.drivers.io.idd.ProvideIddDataResponse;
import prgm.pdfwebforms.idd.IddCallModel;
import prgm.pdfwebforms.idd.IddCaller;
import prgm.pdfwebforms.legalerappresentante.LegaleRappresentanteManager;
import prgm.pdfwebforms.model.CodRuoliImpersonati;
import prgm.pdfwebforms.model.ConcurrencyModel;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfInstanceDataListModel;
import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfKeyModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.model.PdfPersonModel;
import prgm.pdfwebforms.mom.CostantiMOM;
import prgm.pdfwebforms.mom.MomEventDataModel;
import prgm.pdfwebforms.publisher.model.PdfAnagModel;
import prgm.pdfwebforms.questionariolight.QuestionarioLightUtility;
import prgm.pdfwebforms.verifiers.SchedaScaiVerifier;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfInstanceFacadeBean extends FacadeObject implements PdfInstanceFacade {
  
	{VersionPrinter.getInstance().print("PdfWebForms",this);}
	
	public static final String DAO_XML_NAME = "PdfWebForms.PdfInstance";
	public static final String DAO_MOM_XML_NAME = "PdfWebForms.PdfMom";
	private static final String DAO_VERIFIERS_XML = "PdfWebForms.PdfVerifiers";
		
	public static final String ONLY_FLAT_PRINT = "onlyFlatPrint";
	
	private static final String DAO_PDF_XML_NAME = "PdfWebForms.PdfWebForms";
	private static final String PDF_INSTANCE_ACCESSNAME = "pdfInstance";
	private static final String PDF_INSTANCE_DETT_ACCESSNAME = "pdfInstanceDett";
	private static final String PDF_MOM_INSTANCE_ACCESSNAME = "pdfMomInstance";
	private static final String DRIVER_PACKAGE_PREFIX = "prgm.pdfwebformsdrivers.";
	
	private static final String ERRMSG_CAMPO_OBBLIGATORIO = "Campo obbligatorio";
		
	private static final String S_HUB_SPECIALISTI_PROTEZIONE = "HUB_SPECIALISTI_PROTEZIONE";
		
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfModel newTestPdf(ClientSessionContext csc, PdfDataModel pdfData, boolean isOnWork) throws EJBException {
		try{		
			pdfData.setPdfInstanceId(new StringType());
			
			PdfModel pdf = new PdfModel();
			PdfContextUtils.initContext(csc, pdf);
			pdf.setTestMode(true);

			// Test if is a fb code?
			/*
			try{
				Integer.parseInt(csc.getUserCode());
				pdf.setInitialErrorMsg("Funzione abilitata solo alla sede");
				return pdf;
			}catch(Exception e){}
			*/
			
			StringType mainCodAgente = (StringType)pdfData.readProperty(PdfPredefinedFields.AGENTE_CODICE);
			if(mainCodAgente == null || mainCodAgente.isNull())
				mainCodAgente = new StringType(csc.getCurrentLinkedUserCode());
			pdfData.addProperty(PdfPredefinedFields.AGENTE_CODICE, new StringType(Tools.unFillSx(mainCodAgente.toString(),'0')));
			mainCodAgente = new StringType(Tools.fillSx(mainCodAgente.toString(),'0',10));
			pdf.setMainCodAgente(mainCodAgente);
			pdfData.initSinglePdf(pdf);
			
			new DAOObject(csc,DAO_PDF_XML_NAME).executeQueryAccess("loadCodeFromPdfId",pdfData);
			
			pdf = loadPdf(csc, pdfData, pdf, true, isOnWork, true);
			
			pdf.setTestMode(true);
			
			return pdf;
		}catch(DAOException daoe){
			LOG.error(daoe);
			throw new EJBException(daoe.toString());
		}catch(Exception e){
			LOG.error(e);
			throw new EJBException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfModel newPdf(ClientSessionContext csc, PdfDataModel pdfData) throws EJBException {
		return newPdf(csc, pdfData, false);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfModel clonePdf(ClientSessionContext csc, PdfDataModel pdfData) throws EJBException {
		try{
			PdfDataModel inputData = (PdfDataModel)Tools.cloneObject(pdfData);
			String pdfEnvironment = pdfData.getPdfEnvironment().toString();
			String originalPdfInstanceId = pdfData.getPdfInstanceId().toString();		
			String compilationModes = pdfData.getCompilationModes().toString();
			
			String originalExternalEntityAppl = pdfData.getExternalEntityAppl().toString();
			String originalExternalEntityName = pdfData.getExternalEntityName().toString();
			String originalExternalEntityKey = pdfData.getExternalEntityKey().toString();
			
			String newCodAgeImpersonato = pdfData.getCodAgeImpersonato().toString();
			String newCodRuoloImpersonato = pdfData.getCodRuoloImpersonato().toString();
			
			PdfModel pdf = mergePdfData(csc, pdfData);
			pdf.clearEndProcessFields();
			pdfData = pdf.getPdfData();
			
			// Setting input defined values
			if(pdfEnvironment.length() > 0)
				pdfData.setPdfEnvironment(new StringType(pdfEnvironment));
			if(compilationModes.length() > 0)
				pdfData.setCompilationModes(new StringType(compilationModes));
			
			pdfData.setPdfInstanceId(new StringType());
			pdfData.setPdfStatus(PdfInstanceModel.STATO_BOZZA);
			pdfData.setIsInitCalled(null);
			for(int i=0; i < pdfData.getPdfs().size(); i++){
				PdfDataModel pdfElement = (PdfDataModel)pdfData.getPdfs().get(i);
				pdfElement.setIsInitCalled(null);
			}
			
			pdfData.setOriginalPdfInstanceId(originalPdfInstanceId);			
			if(originalExternalEntityAppl.length() > 0)
				pdfData.setExternalEntityAppl(new StringType(originalExternalEntityAppl));
			if(originalExternalEntityName.length() > 0)
				pdfData.setExternalEntityName(new StringType(originalExternalEntityName));
			if(originalExternalEntityKey.length() > 0)
				pdfData.setExternalEntityKey(new StringType(originalExternalEntityKey));
			
			pdfData.setCodAgeImpersonato(new StringType(newCodAgeImpersonato));
			pdfData.setCodRuoloImpersonato(new StringType(newCodRuoloImpersonato));
			
			// Gestisco la possibilità di rimpiazzare i codici modulo originali
			managePdfCodeReplacing(inputData, pdfData);
			
			pdf = newPdf(csc, pdfData, true);
			pdf.setOriginalPdfInstanceId(new StringType(originalPdfInstanceId));			
			return pdf;
		}catch(Exception e){
			throw new EJBException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void managePdfCodeReplacing(PdfDataModel inputData, PdfDataModel pdfData) throws PdfWebFormsException{
		if(inputData.getPdfs().size() > 0){
			if(inputData.getPdfs().size() > pdfData.getPdfs().size())
				throw new PdfWebFormsException("Clone called with wrong input number of pdf for new instance");
			for(int i=0;i<inputData.getPdfs().size();i++) {
				PdfDataModel idm = (PdfDataModel)inputData.getPdfs().get(i);
				PdfDataModel dm = (PdfDataModel)pdfData.getPdfs().get(i);
				if(!idm.getPdfCode().isNull() && !idm.getPdfCode().equals("*") && !idm.getPdfCode().equals(dm.getPdfCode())) {
					dm.setPdfCode(new StringType(idm.getPdfCode().toString()));
					dm.setPdfId(new StringType());
				}
			}
		}else{
			if(!inputData.getPdfCode().isNull() && !inputData.getPdfCode().equals("*") && !inputData.getPdfCode().equals(pdfData.getPdfCode())) {
				pdfData.setPdfCode(new StringType(inputData.getPdfCode().toString()));
				pdfData.setPdfId(new StringType());
			}
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private PdfModel newPdf(ClientSessionContext csc, PdfDataModel pdfData, boolean forClone) throws EJBException {
		
		try{
			
			pdfData.setPdfInstanceId(new StringType());
			
			PdfModel pdf = new PdfModel();
			PdfContextUtils.initContext(csc, pdf);

			// External entity key check
			if(!pdfData.getExternalEntityAppl().isNull() || !pdfData.getExternalEntityName().isNull() || !pdfData.getExternalEntityKey().isNull()){
				if(pdfData.getExternalEntityAppl().isNull() || pdfData.getExternalEntityName().isNull() || pdfData.getExternalEntityKey().isNull()){
					pdf.setInitialErrorMsg("La valorizzazione della chiave dell'entità esterna non è completa");
					return pdf;
				}else{
					StringType pdfInstanceId = (StringType)DAOObject.executeDynaQueryAccess(csc, "CEPE", 
																	"select top 1 PDF_INSTANCE_ID from PDF_INSTANCE where"+
																			" EXTERNAL_ENTITY_APPL = '"+pdfData.getExternalEntityAppl()+"' and"+
																			" EXTERNAL_ENTITY_NAME = '"+pdfData.getExternalEntityName()+"' and"+
																			" EXTERNAL_ENTITY_KEY = '"+pdfData.getExternalEntityKey()+"'",
																	null, StringType.class).getSingleResult();
					if(pdfInstanceId != null && !pdfInstanceId.isNull()){
						pdf.setInitialErrorMsg("La chiave esterna specificata è già utilizzata per l'istanza di pdf ["+pdfInstanceId+"]");
						return pdf;
					}
				}
			}

			// Verifica duplicazioni carrello in generazione nuovo pdf
			if(!verificaDuplicazioniCarrello(csc, pdfData)){
				pdf.setInitialErrorMsg("Attenzione, esiste già una dispositiva associata a questa proposta di carrello");
				return pdf;
			}
			
			// Application referenced code management 
			if(pdfData.getPdfs().size() > 0){
				for(int i=0;i<pdfData.getPdfs().size();i++){
					PdfDataModel pdfDataElement = (PdfDataModel)pdfData.getPdfs().get(i);
					if(!loadPdfApplReferenceCode(csc, pdfData, pdfDataElement, i)){
						pdf.setInitialErrorMsg("Nessun codice pdf configurato per la referenza ["+pdfDataElement.getPdfApplReferenceCode()+"]");
						return pdf;
					}
				}				
			}else{
				if(!loadPdfApplReferenceCode(csc, pdfData, pdfData, -1)){
					pdf.setInitialErrorMsg("Nessun codice pdf configurato per la referenza ["+pdfData.getPdfApplReferenceCode()+"]");
					return pdf;
				}
			}
					
			if(pdfData.getCallbackData() != null){
				pdf.setCallbackData(pdfData.getCallbackData());
				pdfData.setCallbackData(null);
			}
			
			if(pdf.getIsSede().booleanValue()){
				AbstractType codAgente = pdfData.readProperty(PdfPredefinedFields.AGENTE_CODICE);
				if(codAgente != null && !codAgente.isNull()){
					for(int i=0;i<pdfData.getPdfs().size();i++){
						PdfDataModel pdfDataElement = (PdfDataModel)pdfData.getPdfs().get(i);
						pdfDataElement.addProperty(PdfPredefinedFields.AGENTE_CODICE, new StringType(codAgente.toString()));
					}
				}
			}else{
				StringType agenteCollegato = new StringType(Tools.fillSx(csc.getCurrentLinkedUserCode(),'0',10));
				pdfData.addProperty(PdfPredefinedFields.AGENTE_CODICE, new StringType(Tools.unFillSx(agenteCollegato.toString(),'0')));
				for(int i=0;i<pdfData.getPdfs().size();i++){
					PdfDataModel pdfDataElement = (PdfDataModel)pdfData.getPdfs().get(i);
					pdfDataElement.addProperty(PdfPredefinedFields.AGENTE_CODICE, new StringType(Tools.unFillSx(agenteCollegato.toString(),'0')));
				}
				if(!pdfData.getCodAgeImpersonato().isNull()){
					pdfData.setCodAgeImpersonato(new StringType(Tools.fillSx(pdfData.getCodAgeImpersonato().toString(),'0',10)));
					if(pdfData.getCodAgeImpersonato().equals(agenteCollegato))
						pdfData.setCodAgeImpersonato(new StringType());
				}
			}
			
			if(pdfData.getPdfEnvironment().isNull()){
				pdf.setInitialErrorMsg("Nessun ambiente di compilazione impostato");
				return pdf;
			}else{
				if(pdfData.getPdfEnvironment().equals(S_HUB_SPECIALISTI_PROTEZIONE))
					pdfData.setCodRuoloImpersonato(new StringType(CodRuoliImpersonati.FPS));
			}

			if(pdfData.getPdfId().isNull() && pdfData.getPdfCode().isNull() && pdfData.getPdfs().size() == 0){
				pdf.setInitialErrorMsg("Nessun codice modulo impostato");
				return pdf;
			}
			
			if(pdfData.getPdfs().size() > 0){
				pdfData.initMultiplePdf(pdf);
				for(int i=0;i<pdfData.getPdfs().size();i++){
					PdfDataModel pdfDataElement = (PdfDataModel)pdfData.getPdfs().get(i);
					if(pdfDataElement.getPdfCode().isNull()){
						pdf.setInitialErrorMsg("Uno dei codici modulo non risulta impostato");
						return pdf;					
					}
					if(!forClone)
						initInitialInputData(csc, pdfDataElement);
					if(!loadPdfKey(csc, pdfDataElement, pdf))
						return pdf;					
				}
				
				pdfData.initDataFromPdfs(0);

				if(pdfData.getPdfs().size() == 1)
					pdfData.getPdfs().clear();
				
			}else{
				pdfData.initSinglePdf(pdf);
				if(!forClone)
					initInitialInputData(csc, pdfData);
				if(!loadPdfKey(csc, pdfData, pdf))
					return pdf;					
			}
			
			pdf.setFirstDisplayClass(PdfPage.class);
			pdf = loadPdf(csc, pdfData, pdf, true, false, true);
			if(pdf.getInitialErrorMsg() != null && pdf.getInitialErrorMsg().length() > 0)
				return pdf;
			
			if(pdf.isMultiPdf()){ // Carico gli eventuali dati di inizializzazione dei pdf oltre il primo (il primo è già caricato nel pdfData comune) 
				pdfData.initPdfsFromData(); // Inizializzo il primo
				if(!pdfData.getPdfEnvironment().equals(ONLY_FLAT_PRINT)){
					for(int i=1;i<pdfData.getPdfs().size();i++){
						PdfDataModel pdfDataElement = (PdfDataModel)pdfData.getPdfs().get(i);
						DataLoader.loadCustomerPersons(csc, pdfDataElement, pdf, pdf.getPdfData().getAgente().getCodAgente(), true);
					}				
				}
			}
			
			if(pdf.getErrorOnSomePerson() == PdfPersonModel.PROSPECT_IS_DRAFT){
				pdf.setInitialErrorMsg("Per procedere con la compilazione del modulo occorre prima completare il processo di censimento anagrafico");
				return pdf;
			}
			
			// Verifica IDD in apertura
			String iddMsg = IddCaller.verificaIddInApertura(pdf);
			if(iddMsg != null){
				int tipoVerificaIdd = IddCaller.readTipoVerificaIdd(pdf);
				if(tipoVerificaIdd == ProvideIddDataResponse.VERIFICA_IDD_RAMO_PROTEZIONE){
					pdf.setInitialErrorMsg(iddMsg);
				}else if(tipoVerificaIdd == ProvideIddDataResponse.VERIFICA_IDD_RAMO_III){
					pdf.addCommandMessage(iddMsg);
				}else if(tipoVerificaIdd == ProvideIddDataResponse.VERIFICA_IDD_RAMO_PREVIDENZA){
					pdf.addCommandMessage(iddMsg);
				}
			}
			
			// MOP: se operatore MOM svuotiamo l'elenco dei campi non editabili impostato lato java
			if(pdf.isOperatoreMOM())
				clearFieldsForMOM(pdf);

			return pdf;
		
		}catch(DAOException daoe){
			LOG.error(daoe);
			throw new EJBException(daoe.toString());
		}catch(Exception e){
			LOG.error(e);
			throw new EJBException(e.toString());
		}
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean loadPdfApplReferenceCode(ClientSessionContext csc, PdfDataModel pdfData, PdfDataModel pdfDataElement, int elementIdx) throws DAOException{
		if( pdfDataElement.getPdfCode().isNull() &&
			pdfDataElement.getPdfApplReferenceCode() != null && !pdfDataElement.getPdfApplReferenceCode().isNull()){
			String pdfEnvironmentOrig = pdfDataElement.getPdfEnvironment().toString();
			String acroformVersionOrig = pdfDataElement.getAcroformVersion().toString();
			new DAOObject(csc, DAO_PDF_XML_NAME).executeQueryAccess("loadPdfKeyFromApplReference", pdfDataElement);
			if(pdfDataElement.getPdfCode().isNull())
				return false;
			if(pdfDataElement.getPdfEnvironment().isNull()) // No data in db
				pdfDataElement.setPdfEnvironment(new StringType(pdfEnvironmentOrig));
			if(pdfDataElement.getAcroformVersion().isNull()) // No data in db
				pdfDataElement.setAcroformVersion(new IntegerType(acroformVersionOrig));
			pdfDataElement.setPdfApplReferenceCode(null);
			
			if(elementIdx == 0 && !pdfDataElement.getPdfEnvironment().isNull() && pdfData.getPdfEnvironment().isNull())
				pdfData.setPdfEnvironment(new StringType(pdfDataElement.getPdfEnvironment().toString()));
		}
		return true;
	}
		
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static boolean loadPdfKey(ClientSessionContext csc, PdfDataModel pdfData, PdfModel pdf) throws Exception, DAOException{
		
		PdfKeyModel pdfKey = new PdfKeyModel();
		pdfKey.setPdfCode(pdfData.getPdfCode());
		pdfKey.setPdfId(pdfData.getPdfId());
		pdfKey.setPdfPublicationId(pdfData.getPdfPublicationId());
		pdfKey.setMomVersion(pdfData.getMomVersion());
		pdfKey.setAcroformVersion(pdfData.getAcroformVersion());
		
		DAOObject dao = new DAOObject(csc,DAO_PDF_XML_NAME);
		if(!pdfKey.getPdfId().isNull()){
			dao.executeQueryAccess("loadCodeFromPdfId",pdfKey);
		}else{
			DAOQueryResultModel qRes = dao.executeQueryAccess("loadPdfIdFromCode",pdfKey);
			if(qRes.getResult().size() == 0){
				// Try with MOM code on catalog
				qRes = dao.executeQueryAccess("loadPdfIdFromMomCodeOnCatalog",pdfKey);
				if(qRes.getResult().size() == 0){
					// Try with MOM code on general
					qRes = dao.executeQueryAccess("loadPdfIdFromMomCode",pdfKey);
					if(qRes.getResult().size() == 0){
						pdf.setInitialErrorMsg("Il codice modulo ["+pdfKey.getPdfCode()+"] risulta inesistente");
						return false;
					}
				}
			}else if(qRes.getResult().size() > 1){
				pdf.setInitialErrorMsg("Il codice modulo ["+pdfKey.getPdfCode()+"] risulta duplicato");
				return false;
			}
		}
		
		if(pdfKey.getPdfId().isNull() || pdfKey.getPdfCode().isNull()){
			if(pdf.isMultiPdf())
				pdf.setInitialErrorMsg("Uno dei codici modulo non risulta impostato");
			else
				pdf.setInitialErrorMsg("Codice modulo non impostato");
			return false;
		}
		
		dao.executeQueryAccess("initPdfCurrentPubblicationId",pdfKey);
		if(pdfKey.getPdfPublicationId().isNull()){
			if(pdfKey.getAcroformVersion().isNull())
				pdf.setInitialErrorMsg("Per il codice modulo ["+pdfKey.getPdfCode()+"] non ci sono pubblicazioni attive");
			else
				pdf.setInitialErrorMsg("Per il codice modulo ["+pdfKey.getPdfCode()+"] non ci sono pubblicazioni attive con versione form ["+pdfKey.getAcroformVersion()+"]");
			return false;
		}
		
		pdfData.setPdfCode(pdfKey.getPdfCode());
		pdfData.setPdfMomCode(pdfKey.getPdfMomCode());
		pdfData.setPdfId(pdfKey.getPdfId());
		pdfData.setPdfPublicationId(pdfKey.getPdfPublicationId());
		return true;
		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void managePublicationIdChanges(ClientSessionContext csc, PdfModel pdf, PdfDataModel pdfData) throws DAOException{
		if(pdf.isTestMode() || pdf.isOperatoreMOM() || !pdfData.getMomVersion().isNull() || !pdf.getPdfData().getPdfStatus().equals(PdfInstanceModel.STATO_BOZZA))
			return;
		PdfDataModel currentPdfPubl = new PdfDataModel();
		currentPdfPubl.setPdfId(new StringType(pdfData.getPdfId().toString()));
		if(!pdfData.getAcroformVersion().isNull() && pdfData.getAcroformVersion().intValue() > 0)
			currentPdfPubl.setAcroformVersion(new IntegerType(pdfData.getAcroformVersion().intValue()));		
		new DAOObject(csc, DAO_PDF_XML_NAME).executeQueryAccess("initPdfCurrentPubblicationId",currentPdfPubl);
		if(!currentPdfPubl.getPdfPublicationId().isNull() && currentPdfPubl.getPdfPublicationId().intValue() != pdfData.getPdfPublicationId().intValue()) {
			pdfData.setPdfPublicationId(new IntegerType(currentPdfPubl.getPdfPublicationId().intValue()));
			pdfData.setPublicationIdChanged(true);
		}
	}

	private static String NOMI_CAMPO_CHARS = "a-zA-Z0-9";
	/***********************************************************************************************/
	/***********************************************************************************************/
	private PdfModel loadPdf(ClientSessionContext csc, PdfDataModel pdfData, PdfModel pdf, 
							 boolean loadPersons, boolean isOnWork, boolean callOnLoadPdfOnDriver) throws EJBException {
		try{		
			
			if(!pdfData.getPdfStatus().equals(PdfInstanceModel.STATO_BOZZA)){
				if(!pdf.isOperatoreMOM()) //MOP
					callOnLoadPdfOnDriver=false;
			}
			
			DAOObject dao = new DAOObject(csc,DAO_XML_NAME);
			
			pdf.setPdfOnWork(isOnWork);
			pdf.setPdfData(pdfData);
			
			pdf.resetCommandErrors();
			
			// ****************************************************************
			// First time: load and init pdf/pdfs
			// ****************************************************************
			if(!pdfData.isOnlyPrint()){
				pdf.initMaxRecuperaReportAdeguatezzaRetryCount(csc);
				pdf.initPilotaCopernicoSmart(csc);
				pdf.initModalitaDiSottoscrizioneDisabilitate(csc);
			}
			
			if(pdf.getPdfAnags() == null){
				pdf.setPdfAnags(new ArrayList<PdfAnagModel>());
				if(pdf.isMultiPdf()){
					int numPdf = pdf.getPdfData().getPdfs().size();
					for(int i=0;i<numPdf;i++)
						pdf.getPdfAnags().add(null);
					CountDownLatch latch = new CountDownLatch(numPdf);
					PdfLoader[] pdfLoader = new PdfLoader[numPdf];
					for(int i=0;i<numPdf;i++){
						PdfDataModel pdfDataElemnt = (PdfDataModel)pdf.getPdfData().getPdfs().get(i);
						managePublicationIdChanges(csc, pdf, pdfDataElemnt);
						pdfLoader[i] = new PdfLoader(csc, latch, pdf.getPdfAnags(), i, pdf, pdfDataElemnt, isOnWork);  
						Thread threadPersonLoader = new Thread(pdfLoader[i]);
						threadPersonLoader.start();
					}
					latch.await();
				}else{
					managePublicationIdChanges(csc, pdf, pdfData);
					pdf.getPdfAnags().add(PdfLoader.loadAndInitPdfData(csc, pdf, pdfData, isOnWork));
				}
				pdf.getPdfData().initDataFromPdfs(0);
			}
			// ****************************************************************
			
			// Load drivers, if not done and exists and if there is dataentry
			boolean initCalled = false;
			if(pdfData.isOnlyPrint()){

				pdfData.setPdfDriver(null);
				if(!pdfData.getPdfEnvironment().equals(ONLY_FLAT_PRINT)){
					DataLoader.initPersons(csc, pdf);			
					dao.fillCodDesc(pdfData);
				}
				return pdf;
				
			}else if(pdfData.getSkipDataentry().booleanValue()){
				
				pdfData.setPdfDriver(null);
				
			}else if(pdfData.getPdfDriver() == null){
					
				// Gestione driver di processo in input
				loadProcessDriverReference(csc, pdf, pdfData);
				
				String driverName = pdf.getPdfAnag().getPdfDriverName().toString();
				if(driverName.length() == 0){
					pdfData.setPdfDriver(new PdfBaseDriver());
				}else{
					String driverVersion = pdf.getPdfAnag().getPdfDriverVersion().toString();
					if(driverVersion.length() > 0)
						driverVersion = "."+driverVersion;
					String driverClassName = driverName.toLowerCase()+driverVersion.toLowerCase();
					// Load driver
					try{
						PdfBaseDriver pdfDriver = (PdfBaseDriver)DBJavaClassLoader.loadDbJavaObject(csc, DRIVER_PACKAGE_PREFIX+driverClassName+".PdfDriver");
						if(pdfDriver != null)
							pdfData.setPdfDriver(pdfDriver);
						else
							pdfData.setPdfDriver(new PdfBaseDriver());
					}catch(Throwable t){
						pdfData.setPdfDriver(new PdfBaseDriver());
						if(!pdf.getPdfAnag().getPdfDriverName().isNull()) {
							pdf.addCommandMessage("Attenzione: il modulo prevede dei controlli che per problemi tecnici non sono attivi");
							pdf.setSomeDriverNotFount(true);
							if(pdf.getPdfData().getPdfIndex().intValue() == 0)
								pdf.setFirstDriverNotFound(true);
						}
					}
					// Load page driver
					try{
						PdfBasePageDriver pdfPageDriver = (PdfBasePageDriver)DBJavaClassLoader.loadDbJavaObject(csc, DRIVER_PACKAGE_PREFIX+driverClassName+".PdfPageDriver");
						if(pdfPageDriver != null){
							pdfPageDriver.setWebApp("/PdfWebFormsDrivers-"+driverName.toLowerCase());
							pdfPageDriver.setPdfInfos(pdfData.getPdfInfos());
							pdfPageDriver.setPdfDriver(pdfData.getPdfDriver());
							pdfData.setPdfPageDriver(pdfPageDriver);
						}
					}catch(Throwable t){}
				}
				
				if(pdfData.getPdfDriver() != null)
					pdfData.getPdfDriver().initInstance(csc, pdf);
				if(pdfData.getPdfPageDriver() != null)
					pdfData.getPdfPageDriver().initInstance(csc, pdf);
					
			}
			
			// Inizializzo linclusione nella gesitone del legale rappresentante
			LegaleRappresentanteManager.initGestioneLegaleRappresentanteAttiva(csc, pdf);
			
			boolean hasCarrello = !pdfData.getIdCarrello().isNull() && !pdfData.getIdDispCarrello().isNull();
			if(pdfData.getIsInitCalled() == null){ // First pdf initialization
				
				DispositivaCarrelloModel dispositivaCarrello = null;
				if(hasCarrello){
					try{
						dispositivaCarrello = CarrelloDataLoader.loadCarrello(csc, pdfData);
					}catch(Throwable t){
						pdf.setInitialErrorMsg(t.getMessage());
						return pdf;
					}
				}				
				
				AgevolazioneModel agevolazione = null;
				if(!pdfData.getNumAgevolazione().isNull() || !pdfData.getCodAgevolazione().isNull()){
					try{
						agevolazione = AgevolazioneDataLoader.loadAgevolazione(csc, pdfData, pdfData.getNumAgevolazione().toString(), pdfData.getCodAgevolazione().toString());
					}catch(Throwable t){
						pdf.setInitialErrorMsg(t.getMessage());
						return pdf;
					}
				}else if(pdf.reportAdeguatezzaPassatoDalChiamate()){ // Se l'RDA arriva dal chiamate siamo nel 5D e non devo chiedere le agevolazioni
					AgevolazioneDataLoader.uneditAgevolazione(pdfData);
				}
				
				if(loadPersons)
					DataLoader.initPersons(csc, pdf);			

				if(pdfData.getInputDataModel() != null)
					loadPdfDataFromInputDataModel(pdfData);
				
				if(pdf.getErrorOnSomePerson() != PdfPersonModel.PROSPECT_IS_DRAFT){
					
					String iddErrMsg = IddCaller.callIddOnLoad(csc, pdf);
					if(iddErrMsg != null && iddErrMsg.length() > 0){
						pdf.setInitialErrorMsg(iddErrMsg);
						return pdf;
					}
					
					InitPdfOutputData initPdfEventOutput = PdfDriverCaller.callInitPdf(csc, pdf, dispositivaCarrello, agevolazione);
					if(initPdfEventOutput != null){
						if(initPdfEventOutput.getErrorMessage() != null && initPdfEventOutput.getErrorMessage().length() > 0){
							pdf.setInitialErrorMsg(initPdfEventOutput.getErrorMessage());
							return pdf;
						}
						if(initPdfEventOutput.isReloadPersons())
							DataLoader.initPersons(csc, pdf);
					}
				}
				
				initCalled = true;
				
			}else{
				
				if(loadPersons)
					DataLoader.loadPersons(csc, pdf);
				
				// La verifica IDD la facciamo solo se non siamo in accettazione copernico
				if(!pdf.isInAccettazioneCopernico()){
					String iddErrMsg = IddCaller.callIddOnLoad(csc, pdf);
					if(iddErrMsg != null && iddErrMsg.length() > 0){
						pdf.setInitialErrorMsg(iddErrMsg);
						return pdf;
					}
				}
			}
			
			if(callOnLoadPdfOnDriver){
				
				// Gestisco il cambio legale rappresentante
				if(LegaleRappresentanteManager.initLegaleRappresentante(pdf))
					DataLoader.initPersons(csc, pdf);

				//  A patire dal progetto MOP le agevolazioni a due radio vengono portate nel formato con 1 solo radio
				impostaAgevolazioneDipendentiDaDueRadioAUno(csc, pdfData);
				
				LoadPdfOutputData loadPdfEventData = PdfDriverCaller.callLoadPdf(csc, pdf);
				if(loadPdfEventData != null){
					if(loadPdfEventData.getErrorMessage() != null && loadPdfEventData.getErrorMessage().length() > 0){
						pdf.setInitialErrorMsg(loadPdfEventData.getErrorMessage());
						return pdf;
					}
					if(loadPdfEventData.isReloadPersons())
						DataLoader.initPersons(csc, pdf);
				}
			}

			pdf.setPdfDataBeforeImage((PdfDataModel)Tools.cloneObject(pdfData));
			
			dao.fillCodDesc(pdfData);
			
			if(initCalled){
				pdfData.setIsInitCalled(new BooleanType(true));
				pdfData.setJsInitToCall(true);
			}
			return pdf;
			
		}catch(DAOException daoe){
			LOG.error(daoe);
			throw new EJBException(daoe.toString());
		}catch(Exception e){
			LOG.error(e);
			throw new EJBException(e.toString());
		}
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void loadProcessDriverReference(ClientSessionContext csc, PdfModel pdf, PdfDataModel pdfData) throws Exception{
		if(pdfData.getProcessDriverReference().isNull())
			return;
		
		String processDriverNameAndVersion = PdfConfig.getParamAsString(csc, "PROCESS_DRIVER_REFERENCE", pdfData.getProcessDriverReference().toString()).toString();
		if(processDriverNameAndVersion.isEmpty())
			return;
		
		String[] processDriverNameAndVersionAsArray = processDriverNameAndVersion.split("\\.");
		if(processDriverNameAndVersionAsArray.length > 0)
			pdf.getPdfAnag().setPdfDriverName(new StringType(processDriverNameAndVersionAsArray[0]));
		if(processDriverNameAndVersionAsArray.length > 1)
			pdf.getPdfAnag().setPdfDriverVersion(new StringType(processDriverNameAndVersionAsArray[1]));
	}
	
	/***********************************************************************************************/
	// A patire dal progetto MOP le agevolazioni a due radio vengono portate nel formato con 1 solo radio
	/***********************************************************************************************/
	private void impostaAgevolazioneDipendentiDaDueRadioAUno(ClientSessionContext csc, PdfDataModel pdfData) {
		
		StringType tipoAgevolazione = (StringType)pdfData.read(PdfPredefinedFields.TIPO_AGEVOLAZIONE);
		if(tipoAgevolazione == null || !tipoAgevolazione.equals(PdfPredefinedFields.TIPO_AGEVOLAZIONE_DIPENDENTI))
			return;
		
		ArrayList<PdfFieldInfos> radioValues = pdfData.getPdfInfos().getFieldInfos(PdfPredefinedFields.TIPO_AGEVOLAZIONE);
		if(radioValues.size() != 1)
			return;
		
		ProvideAgevolazioneDipendentiDataResponse agevDipData = PdfDriverCaller.callProvideAgevolazioneDipendentiData(csc, pdfData);
		if(agevDipData == null)
			return;

		String codiceAgevolazionePdf = agevDipData.getCodiceAgevolazioneDipendenti();
		String descrizioneAgevolazionePdf = agevDipData.getDescrizioneAgevolazioneDipendenti().length() > 0 ? agevDipData.getDescrizioneAgevolazioneDipendenti() : ProvideAgevolazioneDipendentiDataResponse.DEFAUT_DESCRIZIONE_AGEVOLAZIONE_DIPENDENTI;
		descrizioneAgevolazionePdf += " - "+AgevolazioneModel.PERCENTUALE_DEROGA_DIPENDENTI+"%";
		String percentualeAgevolazionePdf = AgevolazioneModel.PERCENTUALE_DEROGA_DIPENDENTI;
		String tipoAgevolazionePdf = PdfPredefinedFields.TIPO_AGEVOLAZIONE_ALTRO;

		if(pdfData.read(PdfPredefinedFields.TIPO_AGEVOLAZIONE) != null)
			pdfData.write(PdfPredefinedFields.TIPO_AGEVOLAZIONE, new StringType(tipoAgevolazionePdf));
		if(pdfData.read(PdfPredefinedFields.ID_AGEVOLAZIONE) != null)
			pdfData.write(PdfPredefinedFields.ID_AGEVOLAZIONE, new StringType());
		if(pdfData.read(PdfPredefinedFields.CODICE_AGEVOLAZIONE) != null)
			pdfData.write(PdfPredefinedFields.CODICE_AGEVOLAZIONE, new StringType(codiceAgevolazionePdf));
		if(pdfData.read(PdfPredefinedFields.DESCR_AGEVOLAZIONE) != null)
			pdfData.write(PdfPredefinedFields.DESCR_AGEVOLAZIONE, new StringType(descrizioneAgevolazionePdf));
		if(pdfData.read(PdfPredefinedFields.PERCENTUALE_AGEVOLAZIONE) != null)
			pdfData.write(PdfPredefinedFields.PERCENTUALE_AGEVOLAZIONE, new StringType(percentualeAgevolazionePdf));
		if(pdfData.read(PdfPredefinedFields.TIPOLOGIA_AGEVOLAZIONE) != null)
			pdfData.write(PdfPredefinedFields.TIPOLOGIA_AGEVOLAZIONE, new StringType());
		if(pdfData.read(PdfPredefinedFields.MOD_VERSAMENTO_AGEVOLAZIONE) != null)
			pdfData.write(PdfPredefinedFields.MOD_VERSAMENTO_AGEVOLAZIONE, new StringType());
		if(pdfData.read(PdfPredefinedFields.IMPORTO_AGEVOLAZIONE) != null)
			pdfData.write(PdfPredefinedFields.IMPORTO_AGEVOLAZIONE, new DoubleType());

	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void loadPdfDataFromInputDataModel(PdfDataModel pdfData){
		
		CommandDataModel inputDataModel = pdfData.getInputDataModel();
		for(PdfFieldInfos fi : pdfData.getPdfInfos().getFieldInfos()){
			String pdfFieldName = fi.pdfFieldName;
			try{
					
				AbstractType inputFieldFromModel = (AbstractType)Tools.getPropertyValue(inputDataModel,pdfFieldName);
				
				if(inputFieldFromModel != null && inputFieldFromModel instanceof AbstractType){
					
					Class fieldType = Class.forName("com.atosorigin.wfem.types."+fi.dataType);
					pdfData.addProperty(fi.htmlFieldName, AbstractType.newInstance(fieldType, inputFieldFromModel.toString()));
					
				}else{ // Search as "PRINTABLE_GROUP_TYPE" or "CodDesc"
					
					int idx = pdfFieldName.lastIndexOf(Constants.NESTED_INDICATOR);
					if(idx <= 0)
						continue;
						
					String pdfFieldSuffix = pdfFieldName.substring(idx); 
					String propName = pdfFieldName.substring(0,idx);
					inputFieldFromModel = (AbstractType)Tools.getPropertyValue(inputDataModel,propName);
					if(inputFieldFromModel != null && inputFieldFromModel instanceof AbstractType){
						
						if(inputFieldFromModel.getPrintableType() == AbstractType.PRINTABLE_GROUP_TYPE &&
						   (Constants.NESTED_INDICATOR+inputFieldFromModel.toString()).equals(pdfFieldSuffix)){
							
							Class fieldType = Class.forName("com.atosorigin.wfem.types."+fi.dataType);
							pdfData.addProperty(fi.htmlFieldName, AbstractType.newInstance(fieldType, inputFieldFromModel.getPrintableValue()));
							
						}else if(pdfFieldSuffix.equals("_cod")   ||
								 pdfFieldSuffix.equals("_descr") ||
								 pdfFieldSuffix.equals("_coddescr")){ // Search as "codDesc"
							
				    		CommandDataModel inModel = inputDataModel;
							idx = propName.lastIndexOf(Constants.NESTED_INDICATOR);
							if(idx >= 0){
								String inModelName = propName.substring(0,idx);
								inModel = (CommandDataModel)Tools.getPropertyValue(inputDataModel,inModelName);
								propName = propName.substring(idx+1);
							}
							String cod = inputFieldFromModel.toString();
							String descr = inModel.getDescValue(propName);
							if(pdfFieldSuffix.equals("_cod")){
								Class fieldType = Class.forName("com.atosorigin.wfem.types."+fi.dataType);
								pdfData.addProperty(fi.htmlFieldName, AbstractType.newInstance(fieldType, cod));
							}else if(pdfFieldSuffix.equals("_descr")){
								pdfData.addProperty(fi.htmlFieldName, new StringType(descr));
							}else if(pdfFieldSuffix.equals("_coddescr")){
								pdfData.addProperty(fi.htmlFieldName, new StringType(cod+" - "+descr));
							}
						}
					}
				}
			}catch(Throwable t){}
		}
		
		try{
			pdfData.setInputDataModel(null);
			if(pdfData.getPdfs().size() > 0)
				((PdfDataModel)pdfData.getPdfs().get(pdfData.getPdfIndex().intValue())).setInputDataModel(null);
		}catch(Throwable t){}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void initInitialInputData(ClientSessionContext csc, PdfDataModel pdfData){
		
		List<String> pdfInitialInputDataArray = pdfData.getPdfInitialInputDataArray();
		try{
			AbstractTypePropertyDescriptor[] initialInputDataArray = pdfData.getMappedPropertyDescriptors();
			for(int i=0;i<initialInputDataArray.length;i++){
				if(initialInputDataArray[i].getName().equals("loadMappedPropertiesFields"))
					continue;
				pdfInitialInputDataArray.add(initialInputDataArray[i].getName());
			}
			
		}catch(Exception e){}
		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfModel gotoPdf(ClientSessionContext csc, PdfModel pdf, int pdfIndex, boolean callOnLoadPdfOnDriver) throws EJBException{

		try{
			
			if(!pdf.isMultiPdf()){
				if(callOnLoadPdfOnDriver){
					LoadPdfOutputData loadPdfEventData = PdfDriverCaller.callLoadPdf(csc, pdf);
					if(loadPdfEventData != null){
						if(loadPdfEventData.isReloadPersons())
							DataLoader.initPersons(csc, pdf);
					}
				}
				return pdf;
			}
			
			pdf.getPdfData().initDataFromPdfs(pdfIndex);
			pdf = loadPdf(csc, pdf.getPdfData(), pdf, false, false, callOnLoadPdfOnDriver);
			
			pdf.setScrollXValue(new IntegerType(0));
			pdf.setScrollYValue(new IntegerType(0));
			pdf.setGlobalPageDriverJsScript(null); 
			pdf.setFieldsJsScripts(null); 
			return pdf;
			
		}catch(Exception e){
			LOG.error(e);
			throw new EJBException(e.toString());
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfModel nextPdf(ClientSessionContext csc, PdfModel pdf) throws EJBException{

		try{
			
			if(!pdf.isMultiPdf())
				return pdf;
			
			pdf.getPdfData().initDataFromPdfs(pdf.getPdfData().getPdfIndex().intValue()+1);
			pdf = loadPdf(csc, pdf.getPdfData(), pdf, true, false, true);
			
			pdf.setScrollXValue(new IntegerType(0));
			pdf.setScrollYValue(new IntegerType(0));
			pdf.setGlobalPageDriverJsScript(null); 
			pdf.setFieldsJsScripts(null); 
			return pdf;
			
		}catch(Exception e){
			LOG.error(e);
			throw new EJBException(e.toString());
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private PdfModel mergePdfData(ClientSessionContext csc, PdfDataModel pdfDataKey) throws EJBException {
		try{
			PdfDataModel inputData = (PdfDataModel)Tools.cloneObject(pdfDataKey);
			PdfModel pdf = null;
			if(pdfDataKey.isOperatoreMOM())
				pdf = readPdfOnMomValidation(csc, pdfDataKey, true, false);
			else
				pdf = readPdf(csc, pdfDataKey);
			initInputDataOnInstance(pdf, inputData);
			return pdf;
		}catch(Exception e){
			LOG.error(e);
			throw new EJBException(e.toString());
		}
	}	
		
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void initInputDataOnInstance(PdfModel pdf, PdfDataModel inputData) throws Exception{
		boolean doMainDataInit = true;
		if(pdf.isMultiPdf()){
			if(pdf.getPdfData().getPdfs().size() >= inputData.getPdfs().size()) {
				for(int i=0;i<inputData.getPdfs().size();i++){
					PdfDataModel inputPdfDataElement = (PdfDataModel)inputData.getPdfs().get(i);
					PdfDataModel pdfDataElement = (PdfDataModel)pdf.getPdfData().getPdfs().get(i);
					AbstractTypePropertyDescriptor[] pds = inputPdfDataElement.getMappedPropertyDescriptors();
					for(int k=0; k < pds.length; k++){
						AbstractTypePropertyDescriptor pd = pds[k];
						pdfDataElement.addProperty(pd.getName(), pd.getValue());
					}
					pdfDataElement.setCoraFb(new StringType(inputPdfDataElement.getCoraFb().toString()));
					pdfDataElement.setMomVersion(new StringType(inputPdfDataElement.getMomVersion().toString()));
				}			
			}else{
				doMainDataInit = false;
			}
		}
		if(doMainDataInit) {
			AbstractTypePropertyDescriptor[] pds = inputData.getMappedPropertyDescriptors();
			for(int k=0; k < pds.length; k++){
				AbstractTypePropertyDescriptor pd = pds[k];
				pdf.getPdfData().addProperty(pd.getName(), pd.getValue());
			}
			pdf.getPdfData().setCoraFb(new StringType(inputData.getCoraFb().toString()));
			pdf.getPdfData().setMomVersion(new StringType(inputData.getMomVersion().toString()));
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfModel readPdf(ClientSessionContext csc, PdfDataModel pdfDataKey) throws EJBException {
		
		try{

			DAOObject dao = new DAOObject(csc,DAO_XML_NAME);

			PdfModel pdf = new PdfModel();
			pdf.setPdfData(pdfDataKey);
			PdfContextUtils.initContext(csc, pdf);

			if(pdfDataKey.getPdfInstanceId().isNull()){
				pdf.setInitialErrorMsg("Il campo chiave [pdfInstanceId] non è specificato");
				return pdf;
			}
			
			PdfInstanceModel pdfInstance = new PdfInstanceModel();
			pdfInstance.setPdfInstanceId(new StringType(pdfDataKey.getPdfInstanceId().toString()));
			dao.executeTableLoadAccess(PDF_INSTANCE_ACCESSNAME, pdfInstance);
			if(pdfInstance.getPdfAnag().getPdfCode().isNull()){
				pdf.setInitialErrorMsg("Pdf con progressivo ["+pdfDataKey.getPdfInstanceId()+"] non trovato");
				return pdf;
			}
			
			if(!pdfInstance.getCodAgente().isNull())
				pdf.setMainCodAgente(pdfInstance.getCodAgente());
			if(pdf.getIsRete().booleanValue() && pdfInstance.getPdfStatus().equals(PdfInstanceModel.STATO_BOZZA))
				pdf.setMainCodAgente(new StringType(Tools.fillSx(csc.getCurrentLinkedUserCode(),'0',10)));
			if(pdf.getMainCodAgente() == null) // se il pdf originale non ha l'agente (ad esempio se fatto dalla sede) lo impostiamo "vuoto"
				pdf.setMainCodAgente(new StringType());
			
			String xml = pdfInstance.getPdfXmlData().toString();
			PdfInstanceDataListModel dataList = (PdfInstanceDataListModel)Tools.modelFromXml(new ByteArrayInputStream(xml.getBytes()));
			PdfDataModel pdfData = (PdfDataModel)PdfXmlUtils.modelFromXml(pdfDataKey, dataList, xml);
			if(!pdfInstance.getCodAgente().equals(pdf.getMainCodAgente())) { // Cambio agente gestore
				pdfData.addProperty(PdfPredefinedFields.AGENTE_CODICE, new StringType());
				for(int i=0;i<pdfData.getPdfs().size();i++) {
					PdfDataModel el = (PdfDataModel)pdfData.getPdfs().get(i);
					el.addProperty(PdfPredefinedFields.AGENTE_CODICE, new StringType());
				}
			}
			
			// Imposto i valori passati in input alla open
			initPdfDataKeyOnRead(pdfDataKey, pdf, pdfData, pdfInstance);
			
			// Imposto i dati letti dall'xml della istanza
			pdfInstance.dataFromInstance(pdfData);
			
			pdf = loadPdf(csc, pdfData, pdf, true, false, true);
			
			// Ripristino i dati globali all'istanza
			pdf.setPdfInstanceAttachments(dataList.getPdfInstanceAttachments());
			pdf.setPdfInstanceOriginalAttachments(dataList.getPdfInstanceOriginalAttachments());
			if(dataList.getMifidCallData() != null)
				pdf.setMifidCallModel(dataList.getMifidCallData());
			if(dataList.getMifidManlevaData() != null)
				pdf.setMifidManlevaData(dataList.getMifidManlevaData());
			if(dataList.getReportAdeguatezzaCallData() != null)
				pdf.setReportAdeguatezzaCallModel(dataList.getReportAdeguatezzaCallData());
			if(dataList.getSostituzioniCallData() != null)
				pdf.setSostituzioniCallModel(dataList.getSostituzioniCallData());
			if(dataList.getCoraModel() != null)
				pdf.setCoraModel(dataList.getCoraModel());
			if(!pdfInstance.getPdfCompilationMode().isNull())	// Se "not null" è perchè è oltre il completamento
				pdf.setPdfCompilationMode(new StringType(pdfInstance.getPdfCompilationMode().toString()));
			
			// Verifica IDD in apertura (solo se non siamo in accettazione copernico)
			if(!pdf.isInAccettazioneCopernico()){
				String iddMsg = IddCaller.verificaIddInApertura(pdf);
				if(iddMsg != null){
					int tipoVerificaIdd = IddCaller.readTipoVerificaIdd(pdf);
					if(tipoVerificaIdd == ProvideIddDataResponse.VERIFICA_IDD_RAMO_PROTEZIONE){
						pdf.setInitialErrorMsg(iddMsg);
					}else if(tipoVerificaIdd == ProvideIddDataResponse.VERIFICA_IDD_RAMO_III){
						pdf.addCommandMessage(iddMsg);
					}else if(tipoVerificaIdd == ProvideIddDataResponse.VERIFICA_IDD_RAMO_PREVIDENZA){
						pdf.addCommandMessage(iddMsg);
					}
				}
			}else{ // altrimenti reimposto anche il modello IDD originale
				if(dataList.getIddCallData() != null)
					pdf.setIddCallModel(dataList.getIddCallData());
			}
			
			pdf.setOriginalPdfInstanceId(new StringType(pdfInstance.getOriginalPdfInstanceId().toString()));
			pdf.setDataOraUltimaModifica(pdfInstance.getPdfLastModTime().isNull()?pdfInstance.getPdfCreationTime():pdfInstance.getPdfLastModTime());
			pdf.setCodUtenteUltimaModifica(pdfInstance.getPdfLastModUser().isNull()?pdfInstance.getPdfCreationUser():pdfInstance.getPdfLastModUser());
			pdfInstance.setPdfXmlData(new StringType());
			return pdf;
			
		}catch(DAOException daoe){
			LOG.error(daoe);
			throw new EJBException(daoe.toString());
		}catch(Exception e){
			LOG.error(e);
			throw new EJBException(e.toString());
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void initPdfDataKeyOnRead(PdfDataModel pdfDataKey, PdfModel pdf, PdfDataModel pdfData, PdfInstanceModel pdfInstance) {
		// Imposta i valori passati in input alla open
		pdfData.setOperatoreMOM(pdfDataKey.isOperatoreMOM());
		pdfData.setProcessoAccettazioneCopernico(pdfDataKey.isProcessoAccettazioneCopernico());
		if(!pdfDataKey.getOrdineCompilazioneBasket().isNull())
			pdfData.setOrdineCompilazioneBasket(pdfDataKey.getOrdineCompilazioneBasket());
		if(!pdfDataKey.getCompilationModes().isNull()) {
			pdfData.setInputCompilationModes(new StringType(pdfDataKey.getCompilationModes().toString()));
			for(int i=0;i<pdfData.getPdfs().size();i++) {
				PdfDataModel el = (PdfDataModel)pdfData.getPdfs().get(i);
				el.setInputCompilationModes(new StringType(pdfDataKey.getCompilationModes().toString()));
			}
		}
		if(!pdfDataKey.getFacSimileOnPreview().isNull())
			pdfData.setFacSimileOnPreview(new BooleanType(pdfDataKey.getFacSimileOnPreview().booleanValue()));
		if(!pdfDataKey.getSistemaClient().isNull())
			pdfData.setSistemaClient(new StringType(pdfDataKey.getSistemaClient().toString()));
		if(!pdfDataKey.getProcessDriverReference().isNull()) {
			pdfData.setProcessDriverReference(new StringType(pdfDataKey.getProcessDriverReference().toString()));
			for(int i=0;i<pdfData.getPdfs().size();i++) {
				PdfDataModel el = (PdfDataModel)pdfData.getPdfs().get(i);
				el.setProcessDriverReference(new StringType(pdfDataKey.getProcessDriverReference().toString()));
			}
		}
		
		pdfData.setCodRuoloImpersonato(new StringType(pdfDataKey.getCodRuoloImpersonato().toString()));
		if(pdfInstance.getPdfEnvironment().equals(S_HUB_SPECIALISTI_PROTEZIONE))
			pdfData.setCodRuoloImpersonato(new StringType("FPS"));
		if(pdfData.isProcessoAccettazioneCopernico())
			pdfData.setCodRuoloImpersonato(new StringType(pdfInstance.getCodRuoloImpersonato().toString()));
		
		StringType codAgeVendente = pdf.getMainCodAgente();
		StringType codAgeImpersonato = pdfDataKey.getCodAgeImpersonato().isNull() ? pdfInstance.getCodAgeImpersonato() : 
																					new StringType(Tools.fillSx(pdfDataKey.getCodAgeImpersonato().toString(),'0',10));
		if(codAgeVendente.equals(codAgeImpersonato))
			codAgeImpersonato = new StringType();
		pdfData.setCodAgeImpersonato(codAgeImpersonato);
		for(int i=0;i<pdfData.getPdfs().size();i++) {
			PdfDataModel el = (PdfDataModel)pdfData.getPdfs().get(i);
			el.setCodAgeImpersonato(codAgeImpersonato);
		}
		
		if(!pdfDataKey.getCodiceLegaleRappresentante().isNull())
			pdfData.setCodiceLegaleRappresentante(pdfDataKey.getCodiceLegaleRappresentante());

		if(pdfDataKey.getPdfs().size() == pdfData.getPdfs().size()) {
			for(int i=0;i<pdfData.getPdfs().size();i++) {
				PdfDataModel el = (PdfDataModel)pdfData.getPdfs().get(i);
				PdfDataModel keyel = (PdfDataModel)pdfDataKey.getPdfs().get(i);
				el.setIndiceLegaleRappresentante(keyel.getIndiceLegaleRappresentante());
				el.setEsistonoFirmeDelLegaleRappresentante(keyel.getEsistonoFirmeDelLegaleRappresentante());
			}
		}
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfModel verifyPdf(ClientSessionContext csc, PdfModel pdf) throws EJBException {
		try{
			pdf.resetCommandErrors();

			PdfDataModel pdfData = pdf.getPdfData();
			Tools.resetTypesWarningAndErrors(pdfData);
			
			pdfData.initPdfsFromData();
			
			if(pdfData.isOnlyPrint() || pdfData.getSkipDataentry().booleanValue())
				return pdf;
			
			PdfDriverCaller.callSubmitPdf(csc, pdf);
					
			String errAgev = verificaAgevolazione(csc, pdf, pdfData);
			if(errAgev != null) {
				pdf.addCommandError("Attenzione, la deroga selezionata è gia stata utilizzata e non è più disponibile, non è possibile procedere.");
				return pdf;
			}
			
			AbstractType prop = null;
	    	for(PdfFieldInfos fi : pdf.getPdfData().getPdfInfos().getFieldInfos()){
	    		if(fi.hidden || fi.readonly)
	    			continue;
	    		
	    		prop = pdfData.readProperty(fi.htmlFieldName);
	    		if(prop == null)
	    			continue;
	    		
	    		if(fi.mandatory){
					if(prop.isNull())
						prop.addTypeError(ERRMSG_CAMPO_OBBLIGATORIO);
	    		}
	    		
	    		if(!prop.isNull()) {
	    			if(!pdfData.getPriipsTipoSupportoMaterialeContrattuale().isNull() &&
	    				fi.htmlFieldName.equals(PdfPredefinedFields.TIPO_SUPPORTO_MATERIALE_CONTRATTUALE) && 
	    			   !prop.toString().equals(pdfData.getPriipsTipoSupportoMaterialeContrattuale().toString())){
						prop.addTypeError("La dichiarazione del cliente sulla modalità di consegna del materiale contrattuale non è coerente con quanto indicato dal Family Banker.");
	    			}
	    			if(pdf.mainPdfAnag().getTipoPriips().equals(PdfAnagModel.TIPO_PRIIPS_PREVIDENZA)) {
		    			if(!pdfData.getPriipsOrizzonteTemporale().isNull() &&
		    				fi.htmlFieldName.equals(PdfPredefinedFields.ORIZZONTE_TEMPORALE) && 
		    			   !prop.toString().equals(pdfData.getPriipsOrizzonteTemporale().toString())){
							prop.addTypeError("La dichiarazione del cliente sull'orizzonte temporale indicativo non è coerente con quanto indicato dal Family Banker.");
		    			}
		    			if(!pdfData.getPriipsTolleranzaVolatilita().isNull() &&
		    				fi.htmlFieldName.equals(PdfPredefinedFields.TOLLERANZA_VOLATILITA) && 
		    			   !prop.toString().equals(pdfData.getPriipsTolleranzaVolatilita().toString())){
							prop.addTypeError("La dichiarazione del cliente sulla tolleranza alla volatilità e aspettativa di rendimento non è coerente con quanto indicato dal Family Banker.");	    				
		    			}
	    			}
	    		}
	    		
	    		if(!prop.isNull() && (fi.minValue != null || fi.maxValue != null)){
	    			try{
	    				
	    				DoubleType dProp = new DoubleType(prop.toString());
	    				DoubleType dMin = new DoubleType(fi.minValue);
	    				DoubleType dMax = new DoubleType(fi.maxValue);

	    				boolean minOk = true;
		    			if(fi.minValue != null){
		    				if(dProp.doubleValue() < dMin.doubleValue())
		    					minOk = false; 
		    			}
	    				boolean maxOk = true;
		    			if(fi.maxValue != null){
		    				if(dProp.doubleValue() > dMax.doubleValue())
		    					maxOk = false;
		    			}
		    			if(!minOk || !maxOk){
		    				String errMsg = "Valore non valido: deve essere ";
		    				if(fi.minValue != null){
		    					errMsg += "superiore o uguale a "+dMin; 
			    				if(fi.maxValue != null)
			    					errMsg += " e ";
		    				}
		    				if(fi.maxValue != null){
		    					errMsg += "inferiore o uguale a "+dMax;
		    				}
		    				prop.addTypeError(errMsg);
		    			}
	    			}catch(Throwable t){}
	    		}
	    	}			
			
    		if(pdf.globalPdfCompilationModes().equals(PdfInstanceModel.MODALITA_SOTTOSCRIZIONE_CARTA_CHIMICA)){
    			prop = pdf.mainPdfData().readProperty(PdfPredefinedFields.NUMERO_CARTA_CHIMICA);
    			if(prop == null)
    				pdf.addCommandError("Nel modulo non è previsto il campo per il numero della carta chimica");
    			else if(prop.isNull())
    	    		prop.addTypeError(ERRMSG_CAMPO_OBBLIGATORIO);	
    		}
	    	
    		boolean reloadPersons = PdfDriverCaller.callVerifyPdf(csc, pdf);
			if(reloadPersons)
				DataLoader.loadPersons(csc, pdf);

	    	for(int i=1;i<=pdfData.getClienti().size();i++){
	    		AbstractType codCliente = pdfData.readProperty(PdfPredefinedFields.CLIENTE_NDG_PREFIX+i);
				if(codCliente == null || codCliente.isNull()){
					codCliente = pdfData.readProperty(PdfPredefinedFields.CLIENTE_ID_CENSIMENTO_PREFIX+i);
					if(codCliente == null || codCliente.isNull()){
						continue;
					}else if(pdf.isOperatoreMOM()){ // I prospect in MOM non sono accettati 
						try{ pdfData.readProperty(PdfPredefinedFields.CLIENTE_NDG_PREFIX+i).addTypeError(ERRMSG_CAMPO_OBBLIGATORIO); }catch(Throwable t){}
					}
				}
	    		DataLoader.verifyPerson(csc, pdf, codCliente.toString(), pdf.getPdfData().getAgente().getCodAgente().toString(), i);
	    	}
	    	
	    	// Controllo blocchi SCAI
    		new SchedaScaiVerifier().controllaSchedeSCAISquadra(csc, pdf);
    		
    		// Controlli centralizzati
    		verifyPdfForAll(csc, pdf);
    		if(pdf.isOperatoreMOM())
    			verifyPdfForMOM(csc, pdf);
    		else if(pdf.getIsRete().booleanValue())
    			verifyPdfForRete(csc, pdf);

			// Remove errors on non visible pages			
			if(!pdfData.getVisiblePages().isNull()){
				ArrayList<String> visiblePagesAsArray = new ArrayList<String>(Arrays.asList(pdfData.getVisiblePages().toString().split("\\,"))); 
				// To manage multiple field copies keep visible names
				ArrayList<String> visiblePageFieldNames = new ArrayList<String>();
				for(PdfFieldInfos fi : pdf.getPdfData().getPdfInfos().getFieldInfos()){
					if(visiblePagesAsArray.contains(""+fi.page))
						visiblePageFieldNames.add(fi.htmlFieldName);
				}
				for(PdfFieldInfos fi : pdf.getPdfData().getPdfInfos().getFieldInfos()){
					prop = pdfData.readProperty(fi.htmlFieldName);
					if(prop != null && !visiblePageFieldNames.contains(fi.htmlFieldName))
						prop.resetTypeErrors();
				}
			}

			if(pdf.hasCommandErrors())
				return pdf;
			
			if(Tools.containsTypeErrors(pdfData)){
				pdf.addCommandError("Nel modulo sono presenti errori di compilazione. Clicca sulle frecce rosse a sinistra per verificarli");
				return pdf;
			}

			pdf.setPdfDataBeforeImage((PdfDataModel)Tools.cloneObject(pdfData));
			return pdf;
			
		}catch(DAOException daoe){
			LOG.error(daoe);
			throw new EJBException(daoe.toString());
		}catch(Exception e){
			LOG.error(e);
			throw new EJBException(e.toString());
		}
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void verifyPdfForRete(ClientSessionContext csc, PdfModel pdf) throws EJBException {
		PdfDataModel pdfData = pdf.getPdfData();
		if(pdfData.getPdfInfos().findFieldInfoByPdfName(PdfPredefinedFields.TIPO_DISTANZA_COLLOCAMENTO) != null) {
			AbstractType f = pdfData.read(PdfPredefinedFields.TIPO_DISTANZA_COLLOCAMENTO);
			if(f != null && f.isNull())
				f.addTypeError(ERRMSG_CAMPO_OBBLIGATORIO);
		}
		QuestionarioLightUtility.verificaCompilazioneQuestionarioLight(csc, pdf);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void verifyPdfForMOM(ClientSessionContext csc, PdfModel pdf) throws EJBException {
		// Controllo id report adeguatezza e reccomandazione idd in inserimento e se i campi esistono sul pdf
		if(pdf.isInInserimentoMOM()){
			
			StringType idRda = new StringType();
			StringType idRaccomandazione = new StringType();
			if(pdf.getPdfData().getPdfInfos().findFieldInfoByPdfName(PdfPredefinedFields.ID_REPORT_ADEGUATEZZA) != null) {
				idRda = pdf.getPdfData().getIdReportAdeguatezza();
				if(idRda.isNull())
					pdf.addCommandWarning("RdA - "+ERRMSG_CAMPO_OBBLIGATORIO);
			}else if(pdf.getPdfData().getPdfInfos().findFieldInfoByPdfName(PdfPredefinedFields.ID_RACCOMANDAZIONE_IDD) != null) {
				idRaccomandazione = pdf.getPdfData().getIdRaccomandazioneIdd();
				if(idRaccomandazione.isNull())
					pdf.addCommandWarning("Raccomandazione - "+ERRMSG_CAMPO_OBBLIGATORIO);				
			}
			
			if(!idRda.isNull() || !idRaccomandazione.isNull()) {
				StringType ndg = (StringType)pdf.getPdfData().readProperty(PdfPredefinedFields.CLIENTE_NDG_PREFIX+"1");
				if(ndg == null)
					ndg = new StringType();
				else if(!ndg.isNull())
					ndg = new StringType(Tools.fillSx(ndg.toString(), '0', 11));
				
				MapCommandDataModel input = new MapCommandDataModel();
				input.addProperty("userId", new StringType(csc.getUserCode()));
				input.addProperty("idReportAdeguatezza", idRda);
				input.addProperty("idRaccomandazione", idRda.isNull() ? idRaccomandazione : new StringType());
				input.addProperty("ndg", ndg);
				input.addProperty("esito", new StringType());
				input.addProperty("messaggio", new StringType());
				try {
					new DAOObject(csc,DAO_MOM_XML_NAME).executeOSBAccess("verificaIdReportAdeguatezza", input);
					StringType esito = (StringType)input.readProperty("esito");
					if(!esito.equalsIgnoreCase("ok")) {
						StringType messaggio = (StringType)input.readProperty("messaggio");
						if(messaggio.isNull())
							messaggio = new StringType("Errore dal servizio di verifica dell'id RDA");
						pdf.addCommandWarning(messaggio.toString());
					}
				}catch(DAOException daoe) {
					pdf.addCommandWarning("Eccezione DAO nel verificare l'id RDA");
				}
				
				if(!idRda.isNull())
					initTargetMarketDataForInserimentoMOM(csc, pdf, idRda);
			}
			
			CodDescDataList coraDl = pdf.getPdfData().getCodDescDataList("coraFb");
			if(coraDl != null && coraDl.getCodDescCount() > 0 && pdf.getPdfData().getCoraFb().isNull())
				pdf.getPdfData().getCoraFb().addTypeError(ERRMSG_CAMPO_OBBLIGATORIO);
		}
		
		// Obbligatorietà codice agente se presente e visibile nell'acroform
		PdfFieldInfos fi = pdf.getPdfData().getPdfInfos().findFieldInfoByPdfName(PdfPredefinedFields.AGENTE_CODICE);
		if(fi != null && !fi.hidden) {
			AbstractType codiceAgente = pdf.getPdfData().read(PdfPredefinedFields.AGENTE_CODICE);
			if(codiceAgente != null && codiceAgente.isNull())
				codiceAgente.addTypeError(ERRMSG_CAMPO_OBBLIGATORIO);
		}
	}

	/***********************************************************************************************/
	// RFC #292576: per il ramo III impostiamo i dati Target Market 
	// Il PCA quando ramo III, l'id questionario light negli altri casi
	// Verranno usati poi nel successivo richiamo alla MiFid
	/***********************************************************************************************/
	private static final String DAO_XML_IDD = "PdfWebForms.PdfWebFormsIdd";	
	private static final String DAO_XML_QLTM = "PdfWebForms.PdfWebFormsQuestionarioLight";	
	private void initTargetMarketDataForInserimentoMOM(ClientSessionContext csc, PdfModel pdf, StringType idRda) {
		try {
			ProvideIddDataResponse iddData = PdfDriverCaller.callProvideIddData(csc, pdf, false);
			if(iddData == null || iddData.getTipoVerfica() != ProvideIddDataResponse.VERIFICA_IDD_RAMO_III || iddData.getTariffa().isNull())
				return;

			PdfDataModel pdfData = pdf.getPdfData();
			PdfPersonModel mainCli = pdfData.getClienti().get(0);
			if(mainCli == null || mainCli.isEmty())
				return;
			
			boolean isCliEffettivo = mainCli.getNdg().isNull() ? false : true;
			IddCallModel callModel = new IddCallModel();
			callModel.setUtente(new StringType(csc.getUserCode()));
			callModel.setCodiceCliente(isCliEffettivo ? mainCli.getNdg() : mainCli.getIdCensimento());
			callModel.setTariffa(iddData.getTariffa());
			callModel.setIdReportAdeguatezza(idRda);				
			
			// Chiamo getPolizze per avere i flagControlloTM
			DAOObject daoIdd = new DAOObject(csc, DAO_XML_IDD);
			DAOOSBResultModel wsRes = daoIdd.executeOSBAccess("readFlagControlloTargetMarket", callModel);
			if(wsRes.getWsCallData().getStatus() != XmlServiceCallData.STATUS_OK) {
				pdf.addCommandError("Errore di comunicazione con il servizio getPolizze: "+wsRes.getWsCallData().getMessage());
				return;
			}

			pdfData.setIdPCA(new StringType());
			pdfData.setIdQLTM(new StringType());
			if(iddData.getTipoDispositiva() == ProvideIddDataResponse.TIPO_DISPOSITIVA_INIZIALE){
				// Iniziali e cliente in TM
				if(callModel.getFlagControlloTMSottoscrizione().equals("S") || callModel.getFlagControlloTMSottoscrizione().equals("N")) { 
					daoIdd.executeQueryAccess("loadIdRaccomandazioneIddFromRda", callModel);
					if(!callModel.getIdRaccomandazioneIdd().isNull()) {
						wsRes = daoIdd.executeOSBAccess("loadIdQuestionarioFromRaccomandazione", callModel);
						if(wsRes.getWsCallData().getStatus() != XmlServiceCallData.STATUS_OK) {
							pdf.addCommandError("Errore di comunicazione con il servizio elencoRaccomandazioni: "+wsRes.getWsCallData().getMessage());
							return;
						}
						pdfData.setIdPCA(callModel.getIdQuestionarioIddInInput());
					}
				}	
			}else {
				// Postvendita e cliente in TM
				if(callModel.getFlagControlloTMPostvendita().equals("S") || callModel.getFlagControlloTMPostvendita().equals("N")){ 
					wsRes = new DAOObject(csc, DAO_XML_QLTM).executeOSBAccess("loadIdQuestionarioLightTargetMarketForInserimentoMOM", callModel);
					if(wsRes.getWsCallData().getStatus() != XmlServiceCallData.STATUS_OK) {
						pdf.addCommandError("Errore di comunicazione con il servizio getPolizze: "+wsRes.getWsCallData().getMessage());
						return;
					}
					pdfData.setIdQLTM(callModel.getIdQLTM());
				}				
			}

		}catch(DAOException | Exception e) {
			pdf.addCommandError("Eccezione nell'impostare i dati Target Market: "+e.toString());
		}
	}
	
	/***********************************************************************************************/
	private static final String FNAME_CODICE_AGENTE = "codiceAgente";
	private static final String FNAME_CODICE_SPLIT = "codiceSplit";
	/***********************************************************************************************/
	private void verifyPdfForAll(ClientSessionContext csc, PdfModel pdf) throws EJBException {
		PdfDataModel pdfData = pdf.getPdfData();
		if(pdfData.getPdfInfos().findFieldInfoByPdfName(FNAME_CODICE_AGENTE) != null && 
		   pdfData.getPdfInfos().findFieldInfoByPdfName(FNAME_CODICE_SPLIT) != null) {
			AbstractType codiceAgente = pdfData.read(FNAME_CODICE_AGENTE);
			AbstractType codiceSplit = pdfData.read(FNAME_CODICE_SPLIT);
			if(codiceAgente != null && codiceSplit != null && !codiceAgente.isNull() && !codiceSplit.isNull() && 
			   Tools.fillSx(codiceAgente.toString(),'0',10).equals(Tools.fillSx(codiceSplit.toString(),'0',10)))
				codiceSplit.addTypeError("Il codice agente split non può coincidere con il codice agente.");
		}
		
		verificaGestioneFiduciarie(csc, pdfData);
		LegaleRappresentanteManager.verificaLegaleRappresentantePG(csc, pdf);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfModel pdfVerified(ClientSessionContext csc, PdfModel pdf) throws EJBException {
		PdfDriverCaller.callPdfVerified(csc, pdf);
		LegaleRappresentanteManager.valutaPoteriDiFirmaLegaleRappresentantePG(csc, pdf);
		return pdf;
	}
	
	/***********************************************************************************************/
	private static final String FNAME_NDG_CLIENTE1 = "ndgCliente1";
	private static final String CLI1_SUFFIX = "Cliente1";
	private static final String[] cli1FieldNames = {"ndg"+CLI1_SUFFIX,
													"idCensimento"+CLI1_SUFFIX,
													"nome"+CLI1_SUFFIX,
													"cognome"+CLI1_SUFFIX,
													"cognomeNome"+CLI1_SUFFIX,
													"nomeCognome"+CLI1_SUFFIX,
													"codiceFiscale"+CLI1_SUFFIX,
													"partitaIva"+CLI1_SUFFIX,
													"codiceFiscalePartitaIva"+CLI1_SUFFIX };
	/***********************************************************************************************/
	private void verificaGestioneFiduciarie(ClientSessionContext csc, PdfDataModel pdfData) {
		AbstractType codiceAgente = pdfData.read(FNAME_CODICE_AGENTE);
		if(codiceAgente == null || codiceAgente.isNull())
			return;
		AbstractType ndgCliente1 = pdfData.read(FNAME_NDG_CLIENTE1);
		if(ndgCliente1 == null || ndgCliente1.isNull()) 
			return;		
		try {
			MapCommandDataModel m = new MapCommandDataModel();
			m.addProperty(FNAME_CODICE_AGENTE, new StringType(codiceAgente.toString()));
			m.addProperty(FNAME_NDG_CLIENTE1, new StringType(ndgCliente1.toString()));
			DAOObject dao = new DAOObject(csc, DAO_VERIFIERS_XML);
			BooleanType isCliFiduciaria = (BooleanType)dao.executeQueryAccess("isNdgCliente1Fiduciaria", m).getSingleResult();
			if(!isCliFiduciaria.booleanValue())
				return;
			BooleanType ageBC = (BooleanType)dao.executeQueryAccess("isAgenteBC", m).getSingleResult();
			if(!ageBC.booleanValue())
				return;
			BooleanType ageAbilitato = (BooleanType)dao.executeQueryAccess("isAgenteCertificatoPerFiduciarie", m).getSingleResult();
			if(!ageAbilitato.booleanValue())
				PdfUtil.putError(pdfData, new ArrayList<>(Arrays.asList(cli1FieldNames)), "Attenzione: l'utente non è abilitato alla gestione dei mandati fiduciari.");
		}catch(DAOException daoe) {
			ndgCliente1.addTypeError(daoe.toString());
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private String verificaAgevolazione(ClientSessionContext csc, PdfModel pdf, PdfDataModel pdfData) throws DAOException{
		if(csc.isSede() || !pdfData.getPdfStatus().equals(PdfInstanceModel.STATO_BOZZA))
			return null;
		
		AbstractType idAgev = pdfData.read(PdfPredefinedFields.ID_AGEVOLAZIONE);
		if(idAgev == null || idAgev.isNull())
			return null;
		
		MapCommandDataModel agevInput = new MapCommandDataModel();
		agevInput.addProperty("idAgevolazione", new StringType(idAgev.toString()));
		DAOObject dao = new DAOObject(csc,PdfInstanceFacadeBean.DAO_XML_NAME);
		BooleanType agevGiaUsata = (BooleanType)dao.executeQueryAccess("isCodiceAgevolazioneGiaUtilizzato", agevInput).getSingleResult();
		BooleanType agevBruciata = null;
		if(pdf.getPdfData().getIdCarrello().isNull()) // CR26 - La bruciatura non va verificata nel carrello perchè il 5d la fa prima delle sottoscrizioni
			agevBruciata = (BooleanType)dao.executeQueryAccess("isCodiceAgevolazioneBruciato", agevInput).getSingleResult();	
		if((agevGiaUsata != null && agevGiaUsata.booleanValue()) || (agevBruciata != null && agevBruciata.booleanValue()))
			return "Attenzione, la deroga selezionata è gia stata utilizzata e non è più disponibile, non è possibile procedere.";
		return null;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfModel savePdf(ClientSessionContext csc, PdfModel pdf) throws EJBException {

		try{
		
			PdfDataModel pdfData = pdf.getPdfData(); 
						
			pdf.resetCommandErrors();

			pdfData.initPdfsFromData();
			
			if(pdfData.isOnlyPrint())
				return pdf;
			
			if(pdf.isTestMode()){
				pdfData.setPdfInstanceId(new StringType("XXXXXXXXXXXXXXXXXXXX"));
				return pdf;
			}
			
			if(pdfData.getIsVolatile().booleanValue()){
				return pdf;
			}
			
			// *****************************************
			// Do persistence, if not volatile
			// *****************************************
			DAOObject dao = new DAOObject(csc,DAO_XML_NAME);
			TimestampType now = Tools.now();
			PdfInstanceModel pdfInstance = new PdfInstanceModel();
			
			boolean isNew = false;
			if(pdfData.getReplaceExternalPdfInstance().booleanValue()) {
				
				isNew = true;
				pdfData.setReplaceExternalPdfInstance(new BooleanType());
				
			}else if(pdfData.getPdfInstanceId().isNull()){
				
				if(!verificaDuplicazioniCarrello(csc, pdfData)){
					pdf.addCommandError("Esiste già una dispositiva associata a questa proposta di carrello, non è possibile procedere");
					return pdf;
				}
				
				dao.executeCallableAccess("getPdfInstanceId",pdfInstance);
				pdfData.setPdfInstanceId(new StringType(pdfInstance.getPdfInstanceId().toString()));
				
				String errMsgRaccomandazione = IddCaller.callCollegaRaccomandazioneIddPassataInInput(csc, pdf);
				if(errMsgRaccomandazione != null){
					pdfData.setPdfInstanceId(new StringType());
					pdf.addCommandError(errMsgRaccomandazione);
					return pdf;
				}
				
				isNew = true;
				
			}else{
				
	        	if(!PdfUtil.testConcorrenzaBozzaIsOk(csc, pdf))
	        		return pdf;
	        	
				pdfInstance.setPdfInstanceId(new StringType(pdfData.getPdfInstanceId().toString()));
				dao.executeTableLoadAccess(PDF_INSTANCE_ACCESSNAME,pdfInstance);
			}
			
			// Load instance data from model data
			pdfInstance.instanceFromData(pdf);
			// ----------------------------------
			
			String sempltSuffix = pdfInstance.isBasketSemplt() ? "Semplt" : ""; 
			
			if(isNew){

				pdfInstance.setPdfCreationTime(now);
				pdfInstance.setPdfCreationUser(new StringType(csc.getUserCode()));
				pdfInstance.setPdfCreationUserType(new StringType(csc.getUserType()));
				dao.executeTableInsertAccess(PDF_INSTANCE_ACCESSNAME,pdfInstance);

				// Update della riga nel carrello
				if(!pdfInstance.getIdCarrello().isNull()){
					
					String errcarl = "Il modulo non risulterà integrato con il carrello. Non è stato possibile aggiornare i dati di riferimento.";
					if(pdf.isMultiPdf()){
						for(int i=0; i<pdf.getPdfData().getPdfs().size(); i++){
							PdfDataModel pdfElement = (PdfDataModel)pdf.getPdfData().getPdfs().get(i);
							if(!pdfElement.getIdDispCarrello().isNull()){
								try{
									pdfInstance.setIdDispCarrello(pdfElement.getIdDispCarrello());
									if(pdfData.getIsSwitch().booleanValue())
										dao.executeTableUpdateAccess("aggiornaDettOrdCarlSwitch"+sempltSuffix,pdfInstance);
									else
										dao.executeTableUpdateAccess("aggiornaDettOrdCarl"+sempltSuffix,pdfInstance);
								}catch(NoRowsAffected nra){
									pdf.addCommandError(errcarl);
								}
							}
						}
					}else{
						if(!pdfData.getIdDispCarrello().isNull()){
							try{
								pdfInstance.setIdDispCarrello(pdfData.getIdDispCarrello());
								if(pdfData.getIsSwitch().booleanValue())
									dao.executeTableUpdateAccess("aggiornaDettOrdCarlSwitch"+sempltSuffix,pdfInstance);
								else
									dao.executeTableUpdateAccess("aggiornaDettOrdCarl"+sempltSuffix,pdfInstance);
							}catch(NoRowsAffected nra){
								pdf.addCommandError(errcarl);
							}
						}
					}
				}
				
			}else{
				
				pdfInstance.setPdfLastModTime(now);
				pdfInstance.setPdfLastModUser(new StringType(csc.getUserCode()));

				try{
					dao.executeTableUpdateAccess(PDF_INSTANCE_ACCESSNAME,pdfInstance);
				}catch(NoRowsAffected nra){
					pdf.addCommandError("Stato del modulo non piu' coerente col processo di compilazione");
					return pdf;
				}
				
				// Trace save action
				try{
					dao.executeTableUpdateAccess("pdfTraceSaveAction",pdfInstance);
				}catch(NoRowsAffected nra){
					dao.executeTableInsertAccess("pdfTraceSaveAction",pdfInstance);
				}
				
			}
			
			pdf.setDataOraUltimaModifica(now);
			pdf.setCodUtenteUltimaModifica(new StringType(csc.getUserCode()));
			if(!pdfInstance.getIdCarrello().isNull() && pdf.getBasket() != null) {
				ConcurrencyModel cm = new ConcurrencyModel();
				cm.setIdCarrello(pdfData.getIdCarrello());
				cm.setDataOraUltimaModifica(now);
				cm.setCodUtenteUltimaModifica(new StringType(csc.getUserCode()));
				dao.executeTableUpdateAccess("concurrencyBasketData"+sempltSuffix,cm);
				pdf.getBasket().setDataOraUltimaModifica(now);
				pdf.getBasket().setCodUtenteUltimaModifica(cm.getCodUtenteUltimaModifica());
			}

			String errorMessage = PdfDriverCaller.callAfterSavedPdf(csc, pdf);
			if(errorMessage != null && errorMessage.length() > 0)
				pdf.addCommandError(errorMessage);
			
			pdfInstance.setPdfXmlData(new StringType());
			
			return pdf;
			
		}catch(DAOException daoe){
			LOG.error(daoe);
			throw new EJBException(daoe.toString());
		}catch(Exception e){
			LOG.error(e);
			throw new EJBException(e.toString());
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void deletePdf(ClientSessionContext csc, PdfInstanceModel pdfInstance) throws EJBException{
		
		try{
			if(pdfInstance.getPdfInstanceId().isNull())
				throw new EJBException("Il campo chiave [pdfInstanceId] non è specificato");
			
			DAOObject dao = new DAOObject(csc,DAO_XML_NAME);
			dao.executeQueryAccess("loadIdCarrelloPdfInstance", pdfInstance);
			try{ dao.executeTableDeleteChildsAccess(PDF_INSTANCE_DETT_ACCESSNAME,pdfInstance); }catch(NoRowsAffected nra){/*do nothing*/}
			dao.executeTableDeleteAccess(PDF_INSTANCE_ACCESSNAME, pdfInstance);

			if(!pdfInstance.getIdCarrello().isNull()){
				String sempltSuffix = pdfInstance.isBasketSemplt() ? "Semplt" : "";
				try{
					pdfInstance.setPdfLastModTime(Tools.now());
					dao.executeTableUpdateAccess("svuotaCodDisposizioneDettOrdCarl"+sempltSuffix,pdfInstance);
				}catch(NoRowsAffected nra){}
			}
			
			try{
				boolean blobSuDB = PdfNasUtil.blobPdfInstanceSuDB(csc);
				if(!blobSuDB)
					PdfNasUtil.PDF_INSTANCE.deletePdfInstanceContent(csc, pdfInstance.getPdfInstanceId());
			}catch(Throwable t){}
			
			try{
				pdfInstance.setPdfLastModUser(new StringType(csc.getUserCode()));
				pdfInstance.setPdfLastModTime(Tools.now());
				dao.executeTableInsertAccess("pdfTraceDeleteAction",pdfInstance);
			}catch(DAOException daoe){
				// do nothing
			}
			
			return;
			
		}catch(DAOException daoe){
			LOG.error(daoe);
			throw new EJBException(daoe.toString());
		}catch(Exception e){
			LOG.error(e);
			throw new EJBException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfModel readPdfOnMomValidation(ClientSessionContext csc, PdfDataModel pdfDataKey) throws EJBException {
		return readPdfOnMomValidation(csc, pdfDataKey, true, true);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private PdfModel readPdfOnMomValidation(ClientSessionContext csc, PdfDataModel pdfDataKey, boolean loadCloneDoppiaSpunta, boolean loadMomInstance) throws EJBException {
		
		try{
			
			DAOObject dao = new DAOObject(csc,DAO_XML_NAME);
			DAOObject daoReview = new DAOObject(csc, DAO_MOM_XML_NAME);

			PdfModel pdf = new PdfModel();
			pdf.setPdfData(pdfDataKey);
			PdfContextUtils.initContext(csc, pdf);
			
			// Leggo le chiaviK dalle istanze principali a DB così da impostarle alla fine nel modello
			ArrayList<StringType> codDispositiveBMED = loadCodDispositiveBMED(csc, pdfDataKey);
			
			// principalDataList -> se esiste il clone sono i dati dell'istanza principale, la prima, altrimenti vale null
			// originalDataList  -> se non esiste il clone sono i dati dell'istanza principale, la prima, altrimenti sono i dati del clone
			
			PdfInstanceDataListModel principalDataList = null; // Se esiste un clone principalDataList != null e sono i dati principali
			if(loadCloneDoppiaSpunta) {
				String principalPdfInstanceId = pdfDataKey.getPdfInstanceId().toString();
				if(loadMomCloneDoppiaSpuntaPdfInstanceId(csc, pdfDataKey)) // Se esiste il clone tengo da parte i dati della riga principale
					principalDataList = loadPrincipalPdfInstanceData(dao, principalPdfInstanceId);
			}
			
			if(pdfDataKey.getPdfInstanceId().isNull()){
				pdf.setInitialErrorMsg("Il campo chiave [pdfInstanceId] non è specificato");
				return pdf;
			}
			
			PdfInstanceModel pdfInstance = new PdfInstanceModel();
			pdfInstance.setPdfInstanceId(new StringType(pdfDataKey.getPdfInstanceId().toString()));

			dao.executeTableLoadAccess(PDF_INSTANCE_ACCESSNAME, pdfInstance);
			if(pdfInstance.getPdfAnag().getPdfCode().isNull()){
				pdf.setInitialErrorMsg("Pdf con progressivo ["+pdfDataKey.getPdfInstanceId()+"] non trovato");
				return pdf;
			}

			// originalDataList sono i dati originali: quelli della rete se non ci sono cloni, quelli del clone se esiste
			String xml = pdfInstance.getPdfXmlData().toString();
			PdfInstanceDataListModel originalDataList = (PdfInstanceDataListModel)Tools.modelFromXml(new ByteArrayInputStream(xml.getBytes()));
			PdfInstanceDataListModel dataList = originalDataList;
			// E vedo se esitono quelli mom che tornerà dati solo se originalDataList non è riferito al clone
			if(loadMomInstance) {
				BooleanType existMomInstance = (BooleanType)daoReview.executeQueryAccess("existMomInstance", pdfInstance).getSingleResult();
				if(existMomInstance != null && existMomInstance.booleanValue()) {
					daoReview.executeTableLoadAccess(PDF_MOM_INSTANCE_ACCESSNAME, pdfInstance);
					xml = pdfInstance.getPdfXmlData().toString();
					dataList = (PdfInstanceDataListModel)Tools.modelFromXml(new ByteArrayInputStream(xml.getBytes()));
				}
			}
			
			PdfDataModel pdfData = PdfXmlUtils.modelFromXml(pdfDataKey, dataList, xml);
			
			pdfInstance.dataFromInstance(pdfData);
			
			pdfData.setOperatoreMOM(true);

			pdf = loadPdf(csc, pdfData, pdf, true, false, true);

			// Ripristino i dati globali all'istanza originale o della principale nel caso ci sia su un clone
			if(originalDataList.getMifidManlevaData() != null)
				pdf.setMifidManlevaData(originalDataList.getMifidManlevaData());
			else if(principalDataList != null && principalDataList.getMifidManlevaData() != null)
				pdf.setMifidManlevaData(principalDataList.getMifidManlevaData());
			
			// RFC #292576: e prendo i campi PCA e questionario light dai dati principali
			initTargetMarketDataForMomValidation(principalDataList, originalDataList, pdf);

			// MOP: svuotiamo l'elenco dei campi non editabili impostato lato java
			clearFieldsForMOM(pdf);
			
			pdfInstance.setPdfXmlData(new StringType());

			if(originalDataList.getReportAdeguatezzaCallData() != null) {
				pdf.getPdfData().setIdReportAdeguatezza(originalDataList.getReportAdeguatezzaCallData().getIdReportAdeguatezza());
				for(int i=0; i < pdfData.getPdfs().size(); i++){
					PdfDataModel pdfElement = (PdfDataModel)pdfData.getPdfs().get(i);
					pdfElement.setIdReportAdeguatezza(originalDataList.getReportAdeguatezzaCallData().getIdReportAdeguatezza());
				}
			}
			
			pdf.setPdfCompilationMode(new StringType(pdfInstance.getPdfCompilationMode().toString()));
			
			// MOM validation data initialization
			pdf.getPdfData().setReadonly(pdfDataKey.getReadonly());
			pdf.setOriginalPdfInstanceId(new StringType(pdfInstance.getOriginalPdfInstanceId().toString()));
			pdf.setPdfOnMomValidation(true);
			pdf.getPdfData().setOperatoreMOM(true);
			pdf.getMomEventData().setConfrontaDoppiaSpunta(pdfDataKey.getConfrontaDoppiaSpunta());
			
			// Imposto le chiaviK lette dalle righe principali
			initCodDispositiveBMEDInModel(pdf, codDispositiveBMED);

			// Solo per l'inserimento carta chimica da MOM impostiamo la raccomandazione eventualmente digitata dall'operatore.
			readIdRaccomandazioneIddInInserimentoMom(pdf, pdfInstance);
			return pdf;
			
		}catch(DAOException daoe){
			LOG.error(daoe);
			throw new EJBException(daoe.toString());
		}catch(Exception e){
			LOG.error(e);
			throw new EJBException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void initTargetMarketDataForMomValidation(PdfInstanceDataListModel principalDataList, PdfInstanceDataListModel originalDataList, PdfModel pdf) {
		// RFC #292576: e prendo i campi PCA e questionario light dai dati principali
		PdfInstanceDataListModel inst = principalDataList == null ? originalDataList : principalDataList;
		if(inst.getPdfData().size() > 0) {
			PdfDataModel pdfData = (PdfDataModel)inst.getPdfData().get(0); 
			pdf.mainPdfData().setIdPCA(pdfData.getIdPCA());
			pdf.mainPdfData().setIdQLTM(pdfData.getIdQLTM());
		}		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void readIdRaccomandazioneIddInInserimentoMom(PdfModel pdf, PdfInstanceModel pdfInstance) {
		if(!pdf.isInInserimentoMOM())
			return;
		StringType idRaccomandazioneIdd = pdfInstance.getIdRaccomandazioneIdd();
		if(pdf.getPdfData().getPdfInfos().findFieldInfoByPdfName(PdfPredefinedFields.ID_RACCOMANDAZIONE_IDD) != null)
			pdf.getPdfData().setIdRaccomandazioneIdd(idRaccomandazioneIdd);
		for(int i=0; i < pdf.getPdfData().getPdfs().size(); i++){
			PdfDataModel pdfElement = (PdfDataModel)pdf.getPdfData().getPdfs().get(i);
			if(pdfElement.getPdfInfos().findFieldInfoByPdfName(PdfPredefinedFields.ID_RACCOMANDAZIONE_IDD) != null)
				pdfElement.setIdRaccomandazioneIdd(idRaccomandazioneIdd);
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void writeIdRaccomandazioneIddInInserimentoMom(PdfModel pdf, PdfInstanceModel pdfInstance) {
		if(!pdf.isInInserimentoMOM())
			return;
		if(pdf.getPdfData().getPdfInfos().findFieldInfoByPdfName(PdfPredefinedFields.ID_RACCOMANDAZIONE_IDD) != null) {
			StringType idRaccomandazioneIdd = pdf.getPdfData().getIdRaccomandazioneIdd();
			pdfInstance.setIdRaccomandazioneIdd(idRaccomandazioneIdd);
			for(int i=0; i < pdf.getPdfData().getPdfs().size(); i++){
				PdfDataModel pdfElement = (PdfDataModel)pdf.getPdfData().getPdfs().get(i);
				if(pdfElement.getPdfInfos().findFieldInfoByPdfName(PdfPredefinedFields.ID_RACCOMANDAZIONE_IDD) != null)
					pdfElement.setIdRaccomandazioneIdd(idRaccomandazioneIdd);
			}
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private ArrayList<StringType> loadCodDispositiveBMED(ClientSessionContext csc, PdfDataModel pdfData) throws DAOException{
		ArrayList<StringType> res = new ArrayList<StringType>();
		DAOQueryResultModel qRes = new DAOObject(csc, DAO_MOM_XML_NAME).executeQueryAccess("loadCodDispositiveBMED", pdfData);
		for(int i=0;i<qRes.getResult().size();i++) {
			MapCommandDataModel m = (MapCommandDataModel)qRes.getResult().get(i);
			StringType codDisp = (StringType)m.readProperty("codDispositivaBMED");
			res.add(codDisp);
		}
		return res;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean loadMomCloneDoppiaSpuntaPdfInstanceId(ClientSessionContext csc, PdfDataModel pdfData) throws DAOException{
		StringType clonePfInstanceId = (StringType)DAOObject.executeDynaQueryAccess(csc, "CEPE", 
														"select PDF_INSTANCE_ID from PDF_INSTANCE "+
														"where ORIGINAL_PDF_INSTANCE_ID = '"+pdfData.getPdfInstanceId()+"' "+
														"and EXTERNAL_ENTITY_APPL = '"+CostantiMOM.MOM_EXTERNAL_KEY_APPL+"' "+
														"and EXTERNAL_ENTITY_NAME like '"+CostantiMOM.MOM_EXTERNAL_DOPPIASPUNTA_KEY_ENTITY_NAME_PREFIX+"%'",
														null, StringType.class).getSingleResult();
		if(clonePfInstanceId != null && !clonePfInstanceId.isNull()) {
			pdfData.setPdfInstanceId(clonePfInstanceId);
			return true;
		}
		return false;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private PdfInstanceDataListModel loadPrincipalPdfInstanceData(DAOObject dao, String principalPdfInstanceId) throws Exception, DAOException{
		PdfInstanceModel pdfInstance = new PdfInstanceModel();
		pdfInstance.setPdfInstanceId(new StringType(principalPdfInstanceId));
		dao.executeTableLoadAccess(PDF_INSTANCE_ACCESSNAME, pdfInstance);
		String xml = pdfInstance.getPdfXmlData().toString();
		return xml.isEmpty() ? null : (PdfInstanceDataListModel)Tools.modelFromXml(new ByteArrayInputStream(xml.getBytes()));
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void initCodDispositiveBMEDInModel(PdfModel pdf, ArrayList<StringType> codDispositiveBMED){
		PdfDataModel pdfData = pdf.getPdfData();
		int numModuli = pdf.isMultiPdf() ? pdfData.getPdfs().size() : 1;
		if(codDispositiveBMED.isEmpty() || codDispositiveBMED.size() != numModuli)
			return;
		
		if(pdf.isMultiPdf()) {
			for(int i=0;i<pdfData.getPdfs().size();i++) {
				PdfDataModel pdfDataElement = (PdfDataModel)pdfData.getPdfs().get(i);
				pdfDataElement.setCodDispositivaBMED(codDispositiveBMED.get(i));
			}
		}
		pdfData.setCodDispositivaBMED(codDispositiveBMED.get(0));
	}
	
	/***********************************************************************************************/
	// MOM: svuotiamo l'elenco dei campi non editabili impostato lato java e i campi non significativi
	/***********************************************************************************************/
	private void clearFieldsForMOM(PdfModel pdf) {
		PdfDataModel pdfData = pdf.getPdfData();
		PdfAnagModel pdfAnag = pdf.getPdfAnag();
		if(!keepJavaUneditableFieldsOnMom(pdfAnag))
			pdfData.setUneditableFields(new StringType());
		pdfData.setIdCarrello(new IntegerType());
		pdfData.setIdDispCarrello(new IntegerType());
		for(int i=0; i < pdfData.getPdfs().size(); i++){
			PdfDataModel pdfElement = (PdfDataModel)pdfData.getPdfs().get(i);
			PdfAnagModel pdfElementAnag = pdf.getPdfAnags().get(i);
			if(!keepJavaUneditableFieldsOnMom(pdfElementAnag))
				pdfElement.setUneditableFields(new StringType());
			pdfElement.setIdCarrello(new IntegerType());
			pdfElement.setIdDispCarrello(new IntegerType());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean keepJavaUneditableFieldsOnMom(PdfAnagModel pdfAnag) {
		try {
			String driverName = pdfAnag.getPdfDriverName().toString();
			if(driverName.isEmpty())
				return false;				
			String driverVersion = pdfAnag.getPdfDriverVersion().toString();
			if(driverVersion.length() > 0)
				driverVersion = "."+driverVersion;
			String driverPackageName = driverName.toLowerCase()+driverVersion.toLowerCase()+".";
			PdfBaseDriver drv = (PdfBaseDriver)Class.forName(DRIVER_PACKAGE_PREFIX+driverPackageName+"PdfDriver").newInstance();
			return drv.keepJavaUneditableFieldsOnMom();
		}catch(Exception e) {
			return false;
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfModel savePdfOnMomImport(ClientSessionContext csc, PdfModel pdf) throws EJBException {
		return innerSavePdfOnMomValidation(csc, pdf, true);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfModel savePdfOnMomValidation(ClientSessionContext csc, PdfModel pdf) throws EJBException {
		return innerSavePdfOnMomValidation(csc, pdf, false);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private PdfModel innerSavePdfOnMomValidation(ClientSessionContext csc, PdfModel pdf, boolean forImport) throws EJBException {
		try{
			
			PdfDataModel pdfData = pdf.getPdfData();
			TimestampType now = Tools.now();

			PdfInstanceModel pdfInstance = loadPdfInstanceOnMomValidation(csc, pdf);
			
			DAOObject dao = null;
			String daoAccessName = null;
			
			if(pdfInstance.byOperatoreMom() || forImport){	// Il pdf è stato inserito dall'operatore MOM
				
				dao = new DAOObject(csc,DAO_XML_NAME);
				if(pdfInstance.getPdfInstanceId().isNull()){
					dao.executeCallableAccess("getPdfInstanceId",pdfInstance);
					pdfData.setPdfInstanceId(new StringType(pdfInstance.getPdfInstanceId().toString()));
				}
				daoAccessName = PDF_INSTANCE_ACCESSNAME;
				
			}else{
				
				dao = new DAOObject(csc,DAO_MOM_XML_NAME);
				daoAccessName = PDF_MOM_INSTANCE_ACCESSNAME;
				
			}

			if(pdfInstance.getPdfInstanceId().isNull())
				throw new EJBException("L'istanza del pdf non risulta valorizzata. Non è possibile procedere con il salvataggio dei dati.");
			
			// Load current data into pdfs(i)
			pdfData.initPdfsFromData();
			
			// Load instance data from model data
			pdfInstance.instanceFromData(pdf);
			
			// Solo per l'inserimento carta chimica da MOM salviamo anche la raccomandazione eventualmente digitata dall'operatore.
			writeIdRaccomandazioneIddInInserimentoMom(pdf, pdfInstance);
		
			// L'xml deve contenere anche i dati finali e in particolare pritMomInfo
			pdfInstance.setPdfXmlData(new StringType(PdfXmlUtils.xmlFromModel(pdfData, pdf, true)));
			
			boolean isFreezeAction = false;
			if(pdf.getMomEventData().getAzioneMom().equals("CONFERMA")){
				isFreezeAction = true;
				pdfInstance.setPdfCompletionUser(new StringType(csc.getUserCode()));
				pdfInstance.setPdfCompletionTime(Tools.now());
			}
			
			if(pdfInstance.byOperatoreMom() && pdfInstance.isCloneDoppiaSpuntaMOM()) // Nei cloni doppia spunta non salviamo la chiaveK
				pdfInstance.setCodDispositivaBMED(new StringType());
			
			try{
				pdfInstance.setPdfLastModTime(now);
				pdfInstance.setPdfLastModUser(new StringType(csc.getUserCode()));
				dao.executeTableUpdateAccess(daoAccessName,pdfInstance);
			}catch(NoRowsAffected nra){
				if(daoAccessName.equals(PDF_MOM_INSTANCE_ACCESSNAME)){
					pdfInstance.setPdfStatus(new StringType(PdfInstanceModel.STATO_BOZZA));
					if(!isFreezeAction){
						pdfInstance.setPdfCompletionUser(new StringType());
						pdfInstance.setPdfCompletionTime(new TimestampType());
					}
				}
				pdfInstance.setPdfLastModTime(new TimestampType());
				pdfInstance.setPdfLastModUser(new StringType());
				pdfInstance.setPdfCreationTime(now);
				pdfInstance.setPdfCreationUser(new StringType(csc.getUserCode()));
				pdfInstance.setPdfCreationUserType(new StringType(csc.getUserType()));
				dao.executeTableInsertAccess(daoAccessName,pdfInstance);
			}

			
			if(pdfInstance.byOperatoreMom() || forImport) {
				if(!pdfInstance.isCloneDoppiaSpuntaMOM())
					savePdfInstanceDett(dao, pdf, pdfInstance);
				if(pdfInstance.byOperatoreMom())
					tracciaturaRdaMOM(csc, pdf);
			}
			
			pdfInstance.setPdfXmlData(new StringType());
			return pdf;
			
		}catch(DAOException daoe){
			LOG.error(daoe);
			throw new EJBException(daoe.toString());
		}catch(Exception e){
			LOG.error(e);
			throw new EJBException(e.toString());
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private PdfInstanceModel loadPdfInstanceOnMomValidation(ClientSessionContext csc, PdfModel pdf) throws Exception{
		
		PdfInstanceModel pdfInstance = new PdfInstanceModel();
		
		// Se l'istanza è nuova è necessariamente un pdf fatto dall'operatore MOM
		if(pdf.getPdfData().getPdfInstanceId().isNull()){
			pdfInstance.setPdfEnvironment(new StringType(CostantiMOM.MOM_ENVIRONMENT));
			pdfInstance.setPdfCreationUserType(new StringType(ClientSessionContext.USER_TYPE_SEDE));
			return pdfInstance;
		}

		try{
			pdfInstance.setPdfInstanceId(new StringType(pdf.getPdfData().getPdfInstanceId().toString()));
			
			DAOObject dao = new DAOObject(csc, PdfInstanceFacadeBean.DAO_XML_NAME);
			DAOTableResultModel tRes = dao.executeTableLoadAccess(PDF_INSTANCE_ACCESSNAME, pdfInstance);
			if(tRes.getResult().intValue() == 0)
				throw new Exception("Istanza ["+pdfInstance.getPdfInstanceId()+"] non trovata in validazione/inseriento MOM");			
			dao.executeQueryAccess("loadPdfInstanceCompletedData", pdfInstance);		
			
			// Se inserita dall'operatore MOM non serve leggere la tabella PDF_MOM_INSTANCE
			if( pdfInstance.byOperatoreMom())
				return pdfInstance;
			
			new DAOObject(csc, PdfInstanceFacadeBean.DAO_MOM_XML_NAME).executeTableLoadAccess(PDF_MOM_INSTANCE_ACCESSNAME, pdfInstance);		
			
			return pdfInstance;
			
		}catch(DAOException daoe){
			throw new Exception(daoe.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void savePdfInstanceDett(DAOObject dao, PdfModel pdf, PdfInstanceModel pdfInstance) throws DAOException{
		if(pdfInstance.getPdfInstanceId().isNull()) // per sicurezza
			return;
		try{ dao.executeTableDeleteChildsAccess(PDF_INSTANCE_DETT_ACCESSNAME,pdfInstance); }catch(NoRowsAffected nra){/*do nothing*/}
		if(pdf.isMultiPdf()){
			for(int i=0;i<pdf.getPdfAnags().size();i++){
				PdfDataModel pdfDettData = (PdfDataModel)pdf.getPdfData().getPdfs().get(i);
				PdfAnagModel pdfDettAnag = pdf.getPdfAnags().get(i);
				PdfInstanceModel pdfinstanceDett = new PdfInstanceModel();
				pdfinstanceDett.setPdfInstanceId(pdfInstance.getPdfInstanceId());
				pdfinstanceDett.setPdfInstanceDettNum(new IntegerType(i));
				pdfinstanceDett.setPdfAnag(pdfDettAnag);
				pdfinstanceDett.setPdfBarcode(pdfDettData.getPdfBarcode());
				pdfinstanceDett.setCodDispositivaBMED(pdfDettData.getCodDispositivaBMED());
				dao.executeTableInsertAccess(PDF_INSTANCE_DETT_ACCESSNAME, pdfinstanceDett);
			}
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void tracciaturaRdaMOM(ClientSessionContext csc, PdfModel pdf) throws DAOException{
		if(pdf.getMomEventData().getIdPratica().isNull() ||
			pdf.getPdfData().getCodDispositivaBMED().isNull() || // getExternalEntityKey -> idPraticaMOM
			pdf.getPdfData().getIdReportAdeguatezza().isNull() ||
			pdf.getPdfData().getIdReportAdeguatezza().hasTypeErrors()) 
			return;
		
		DAOObject dao = new DAOObject(csc, DAO_MOM_XML_NAME);
		MapCommandDataModel model = new MapCommandDataModel();
		model.addProperty("idPraticaMOM", pdf.getMomEventData().getIdPratica());		
		model.addProperty("codDispositivaBMED", pdf.getPdfData().getCodDispositivaBMED());
		model.addProperty("idReportAdeguatezza", pdf.getPdfData().getIdReportAdeguatezza());
		try{ 
			model.addProperty("tipoModifica", new StringType("M"));
			model.addProperty("dataOraUpd", Tools.now());
			dao.executeTableUpdateAccess("updateTracciaturaRdaMOM",model); 
		}catch(NoRowsAffected nra){
			model.addProperty("tipoModifica", new StringType("I"));
			model.addProperty("dataOraIns", Tools.now());
			dao.executeTableInsertAccess("insertTracciaturaRdaMOM",model);
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private boolean verificaDuplicazioniCarrello(ClientSessionContext csc, PdfDataModel pdfData) throws DAOException{
		
		if(pdfData.getIdCarrello().isNull())
			return true;
		
		String idDispCarrello = "";
		if(pdfData.getPdfs().size() > 0){
			for(int i=0; i<pdfData.getPdfs().size(); i++){
				PdfDataModel pdfElement = (PdfDataModel)pdfData.getPdfs().get(i);
				if(!pdfElement.getIdDispCarrello().isNull()){
					idDispCarrello = pdfElement.getIdDispCarrello().toString();
					break;
				}
			}
		}else{
			idDispCarrello = pdfData.getIdDispCarrello().toString();
		}									
		if(idDispCarrello.length() > 0){
			IntegerType c = (IntegerType)DAOObject.executeDynaQueryAccess(csc, "CEPE", 
						"select count(*) from PDF_INSTANCE where ID_CARRELLO = "+pdfData.getIdCarrello()+" and ID_DISP_CARRELLO = "+idDispCarrello, 
						null, IntegerType.class).getSingleResult();
			if(c != null && c.intValue() > 0){
				return false;
			}
		}	
		return true;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean confrontaDoppiaSpuntaMom(ClientSessionContext csc, PdfModel pdf, PdfDataModel pdfData) throws EJBException {
		
		pdf.getMomEventData().setEsitoConfrontoDoppiaSpunta(new StringType());
		 // S=semplice, seza evidenza sui campi. V=con anche evidenza sui campi
		if(!pdf.getMomEventData().getConfrontaDoppiaSpunta().equals(MomEventDataModel.CONFRONTA_DOPPIA_SPUNTA_SENZA_EVIDENZE) && 
		   !pdf.getMomEventData().getConfrontaDoppiaSpunta().equals(MomEventDataModel.CONFRONTA_DOPPIA_SPUNTA_CON_EVIDENZE))
			return false;
		
		if(pdf.getOriginalPdfInstanceId().isNull())
			return false;

		if(pdf.getPdfOriginali() == null) {
			PdfDataModel pdfDataKey = new PdfDataModel();
			pdfDataKey.setPdfInstanceId(new StringType(pdf.getOriginalPdfInstanceId().toString()));
			PdfModel prevPdf = readPdfOnMomValidation(csc, pdfDataKey, false, true);
			pdf.setPdfOriginali(new ArrayList<PdfModel>());
			pdf.getPdfOriginali().add(prevPdf);
		}

		PdfModel prevPdf = pdf.getPdfOriginali().get(0);
		
		PdfDataModel prevPdfData = prevPdf.getPdfData();
		if(prevPdf.isMultiPdf()) {
			int curPdfIndex = pdfData.getPdfIndex().intValue();
			if(prevPdfData.getPdfs().size()-1 < curPdfIndex)
				return false;
			prevPdfData = (PdfDataModel)prevPdfData.getPdfs().get(curPdfIndex);
		}
		
		boolean hasDifferences = false;
		for(PdfFieldInfos fi : pdfData.getPdfInfos().getFieldInfos()) {
			PdfFieldInfos prevFi = prevPdfData.getPdfInfos().findFieldInfoByFieldInfo(fi);
			if(prevFi == null || prevFi.fieldType == AcroFields.FIELD_TYPE_SIGNATURE || prevFi.hidden)
				continue;
			
			try {
				AbstractType prop = (AbstractType)Tools.getPropertyValue(pdfData, prevFi.htmlFieldName);
				AbstractType prevProp = (AbstractType)Tools.getPropertyValue(prevPdfData, prevFi.htmlFieldName);
				if(prop != null && prevProp != null && !prop.equals(prevProp)) {
					if(pdf.getMomEventData().getConfrontaDoppiaSpunta().equals(MomEventDataModel.CONFRONTA_DOPPIA_SPUNTA_CON_EVIDENZE)) // Verifica con apposizione errori (S solo verifica)
						prop.addTypeError("Valore precedente: "+(prevProp.isNull()?"nessuno":prevProp.toString()));
					hasDifferences = true;
				}
			}catch(Exception e) {/* do nothing*/}
		}
		AbstractType prop = pdfData.getCoraFb();
		AbstractType prevProp = prevPdfData.getCoraFb();
		if(prop != null && prevProp != null && !prop.equals(prevProp)) {
			if(pdf.getMomEventData().getConfrontaDoppiaSpunta().equals(MomEventDataModel.CONFRONTA_DOPPIA_SPUNTA_CON_EVIDENZE)) // Verifica con apposizione errori (S solo verifica)
				prop.addTypeError("Valore precedente: "+(prevProp.isNull()?"nessuno":prevProp.toString()));
			hasDifferences = true;
		}
		pdf.getMomEventData().setEsitoConfrontoDoppiaSpunta(new StringType(hasDifferences?"KO":"OK"));
		return hasDifferences;
	}
	
}
