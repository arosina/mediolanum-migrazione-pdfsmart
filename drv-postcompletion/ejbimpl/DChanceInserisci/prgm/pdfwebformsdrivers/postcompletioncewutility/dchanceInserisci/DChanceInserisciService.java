package prgm.pdfwebformsdrivers.postcompletioncewutility.dchanceInserisci;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.DAOTableResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.dao.exceptions.NoRowsAffected;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.drivers.io.PostCompletionOutputData;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebformsdrivers.postcompletioncewutility.dchanceInserisci.model.ConvenzioneDCModel;
import prgm.pdfwebformsdrivers.postcompletioncewutility.dchanceInserisci.model.DChanceEsitoModel;
import prgm.pdfwebformsdrivers.postcompletioncewutility.dchanceInserisci.model.InOutDChanceInserisciModel;
import prgm.pdfwebformsdrivers.postcompletioncewutility.internal.ServiceCaller;
import prgm.pdfwebformsdrivers.postcompletioncewutility.model.PdfInstancePostCompletionModel;
import prgm.pdfwebformsdrivers.postcompletioncewutility.model.ServiceResponse;
import prgm.pdfwebformsdrivers.postcompletioncewutility.utils.PostCompletionCewUtils;

public class DChanceInserisciService {

	private static final String XML_NAME = "PdfWebFormDriver.PostCompletionCewUtility.DChanceInserisci.DChanceInserisci";
	
	private DChanceInserisciService() {
		throw new IllegalStateException("Utility class");
	}
	  
	public static ServiceResponse chiamaDChanceInserisci(ClientSessionContext csc, PdfModel pdfModel, InOutDChanceInserisciModel inputServizio) throws Exception, DAOException {
		DChanceEsitoModel esitoElaborazione = new DChanceEsitoModel();
		esitoElaborazione.setPdfInstanceId(inputServizio.getIdRiferimento());
		esitoElaborazione.setDataRichiestaApertura(Tools.now());
		
		PdfInstancePostCompletionModel pdfInstanceModel = PostCompletionCewUtils.getDatiPdf(csc, pdfModel.getPdfData().getPdfInstanceId());
		inputServizio.setIdVendente(pdfInstanceModel.getCodAgente());
		inputServizio.setCodiceAgente(!pdfInstanceModel.getCodAgenteImpersonato().isNull() ? pdfInstanceModel.getCodAgenteImpersonato() : pdfInstanceModel.getCodAgente());
		
		String err = ServiceCaller.callQASservice(csc, XML_NAME, "CSCDChanceInserisci", inputServizio);
		if(null!=err) {
			return ServiceResponse.esitoERROR(err);	
		}
		if(null==inputServizio.getEsitoRichiestaApertura()|| !inputServizio.getEsitoRichiestaApertura().equals("OK") ||
		   null==inputServizio.getIdCSCRichiestaApertura() || inputServizio.getIdCSCRichiestaApertura().isNull()) {
			return ServiceResponse.esitoFATAL("Errore nei dati ricevuti dal servizio CSCDChanceInserisci");
		}
		esitoElaborazione.setIdCSCRichiestaApertura(inputServizio.getIdCSCRichiestaApertura());
		try {
			writeEsito(csc, esitoElaborazione);
		} catch (DAOException e) {
			return ServiceResponse.esitoWARNING("Errore salvataggio dati in tabella PDF_POSTCOMPLETION_DATA"+e.getMessage());
		}
		
		String msg = aggiornaStatoPraticaMOM(csc, esitoElaborazione);
		return new ServiceResponse(ServiceResponse.OK, new StringType(msg));
	}
	
	/***********************************************************************************************/
	private static final String ERROR_CODE = "errorCode";
	/***********************************************************************************************/
	public static String aggiornaStatoPraticaMOM(ClientSessionContext csc, DChanceEsitoModel esitoElaborazione){
		try{
			
			
			DAOObject dao = new DAOObject(csc, XML_NAME);
			DAOTableResultModel tRes = dao.executeTableLoadAccess("loadDatiPdfInstance", esitoElaborazione);
			if(tRes.getResult().intValue() == 0 || esitoElaborazione.getPdfCodiceDispositivaBMED().isNull())
				return "";
			
			MapCommandDataModel m = new MapCommandDataModel();
			m.addProperty("codDispositivaBMED", esitoElaborazione.getPdfCodiceDispositivaBMED());
			m.addProperty("pdfInstanceId", esitoElaborazione.getPdfInstanceId());
			if(esitoElaborazione.getErrorCode().equals(new StringType(PostCompletionOutputData.ERROR))) {
				m.addProperty("codStatoOrigEven", new StringType("P41721"));
			}else {
				m.addProperty("codStatoOrigEven", new StringType("P41719"));
			}
			
			m.addProperty(ERROR_CODE, new StringType());
			m.addProperty("errorMessage", new StringType());
			m.addProperty("seqEvento", new StringType());
			dao.executeOSBAccess("aggiornaStatoPraticaMOM", m);
			if(!m.readProperty(ERROR_CODE).equals("0"))
				return "Errore nell'aggiornare MOM ["+m.readProperty(ERROR_CODE)+"-"+m.readProperty("errorMessage")+"]";
			return "Id evento MOM ["+m.readProperty("seqEvento")+"]";
		}catch(DAOException daoe){
			return "Eccezione nell'aggiornare MOM";
		}
	}

	public static StringType getConvenzione(ClientSessionContext csc, PdfModel pdfModel, ConvenzioneDCModel modelC) throws CommandException {
		StringType convenzione = getConvenzione(csc, modelC);
		convenzione = ConvenzioneDerogaDCManager.getConvenzioneDerogaDC(csc, pdfModel, convenzione);
		return convenzione;
	}
	
	public static StringType getConvenzione(ClientSessionContext csc, ConvenzioneDCModel modelC) throws CommandException {
		StringType convenzione = new StringType();
		try {
			DAOObject dao = new DAOObject(csc,XML_NAME);
			DAOQueryResultModel qasRes = dao.executeQueryAccess("getConvenzioneDoubleChance", modelC);
		    convenzione = (StringType)qasRes.getSingleResult();
		} catch (DAOException e) {			
			throw new CommandException(e.toString());
		} 
		return convenzione;		
		
	}
	
	public static ServiceResponse verificaCreazioneContoTecnico(ClientSessionContext csc,StringType pdfInstanceId){
		DAOObject dao = new DAOObject(csc,XML_NAME);
		MapCommandDataModel inQuery = new MapCommandDataModel();
		inQuery.addProperty("pdfInstanceId", pdfInstanceId);
		DAOQueryResultModel clusterResult;
		try {
			clusterResult = dao.executeQueryAccess("getEsitoCreazioneConto", inQuery);
			StringType esito = (StringType)clusterResult.getSingleResult();
			if(null==esito || esito.isNull()) {
				return ServiceResponse.esitoWARNING("In attesa di creazione conto tecnico");
			} else if ("OK".equalsIgnoreCase(esito.toString().trim())) {
				return ServiceResponse.esitoOK();
			} else {
				return ServiceResponse.esitoFATAL("Errore esito creazione conto tecnico :"+esito);
			}
		} catch (DAOException e) {
			return ServiceResponse.esitoERROR("Errore nella verifica apertura conto tecnico : "+e.getMessage());
		}
	}
	
	
	private static void writeEsito(ClientSessionContext csc, DChanceEsitoModel esito) throws DAOException {
		DAOObject dao = new DAOObject(csc,XML_NAME);
		try{
			dao.executeTableUpdateAccess("esitoElaborazione",esito);
		}catch(NoRowsAffected nra){
			dao.executeTableInsertAccess("esitoElaborazione",esito);
		}			
	}
	
}
