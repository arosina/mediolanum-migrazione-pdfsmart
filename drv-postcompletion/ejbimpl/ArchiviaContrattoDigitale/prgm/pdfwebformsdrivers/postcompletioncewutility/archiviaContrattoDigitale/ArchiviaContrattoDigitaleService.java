package prgm.pdfwebformsdrivers.postcompletioncewutility.archiviaContrattoDigitale;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.dao.exceptions.NoRowsAffected;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.QASCaller;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebformsdrivers.postcompletioncewutility.archiviaContrattoDigitale.model.InOutArchiviaContrattoDigitaleModel;
import prgm.pdfwebformsdrivers.postcompletioncewutility.internal.ServiceCaller;
import prgm.pdfwebformsdrivers.postcompletioncewutility.model.ServiceResponse;
import prgm.pdfwebformsdrivers.postcompletioncewutility.model.ServiziEsitoModel;

public class ArchiviaContrattoDigitaleService {

	private static String XML_NAME = "PdfWebFormDriver.PostCompletionCewUtility.ArchiviaContrattoDigitale.ArchiviaContrattoDigitale";
	
	public static ServiceResponse chiamaArchiviaContrattoDigitale(ClientSessionContext csc,  InOutArchiviaContrattoDigitaleModel inputServizio) {
		// RFC #141104 - Archiviazione pdf su filenet PAPF. L'archiviazione su filenet e l'inivio dell'sms vengono sempre gestiti dall'icw "signed"
		return ServiceResponse.esitoOK();
	}
	
	private static void writeEsito(ClientSessionContext csc, ServiziEsitoModel esito) throws DAOException {
		DAOObject dao = new DAOObject(csc,XML_NAME);
		try{
			dao.executeTableUpdateAccess("esitoElaborazioneArchivia",esito);
		}catch(NoRowsAffected nra){
			dao.executeTableInsertAccess("esitoElaborazioneArchivia",esito);
		}			
	}
	
	//refs 56744 - GPM
	public static ServiceResponse chiamaArchiviaContrattoDigitaleSenzaSms(ClientSessionContext csc,  InOutArchiviaContrattoDigitaleModel inputServizio) {
		
		ServiziEsitoModel esito = new ServiziEsitoModel();
		esito.setPdfInstanceId(inputServizio.getPdfInstanceId());
		esito.setDataElabArchivia(Tools.now());
		
		String err = ServiceCaller.callOSBservice(csc, XML_NAME, "scriviContratto", inputServizio);
		if(null != err) {
			return ServiceResponse.esitoERROR(err);	
		}
		
		StringType xmlResponse = inputServizio.getXmlResponse();
		if(xmlResponse.toString().indexOf("<ReturnCode") >= 0){				
			String path = "/prgm/pdfwebformsdrivers/postcompletioncewutility/archiviaContrattoDigitale/XmlServizioArchiviaFile.xml";
			try {					
				String template = Tools.loadTextResourceAsString(path, new ArchiviaContrattoDigitaleService());//defect 38000
				QASCaller.loadModelFromXml(inputServizio, template, inputServizio.getXmlResponse().toString(),null);
				if(inputServizio.getFilenetGuid().isNull()){
					return ServiceResponse.esitoERROR("Istanza di PDF n. ["+inputServizio.getPdfInstanceId()+"] - Errore di servizio in 'addObject'. ReturnCode ["+inputServizio.getReturnCode()+"]");
				}
			} catch (Exception e) {
				//return gestioneErrore(fase, dispo, e.toString());
				return ServiceResponse.esitoERROR("Errore di sistema nel recuperare l'xml di ritorno " + e.toString());
			}
		} else {
			//return gestioneErrore(fase, dispo, "Errore di sistema nel firmare il contratto." );
			return ServiceResponse.esitoERROR("Errore di sistema nel firmare il contratto.");
		}
			
		//Gestione esito firma
		if (!inputServizio.getReturnCode().equals("0")) {
			return ServiceResponse.esitoWARNING("Errore di sistema nel firmare il contratto: "+inputServizio.getReturnCode());
		}
		
		esito.setInfoArchivia(new StringType("FilenetGuid : " + inputServizio.getFilenetGuid()));
		
		try {
			writeEsito(csc, esito);
		} catch (DAOException e) {
			return ServiceResponse.esitoWARNING("Errore salvataggio dati in tabella PDF_POSTCOMP_DATA"+e.getMessage());
		}

		return ServiceResponse.esitoOK();
	}
	
}
