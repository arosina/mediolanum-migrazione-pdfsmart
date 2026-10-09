package prgm.pdfwebformsdrivers.postcompletioncewutility.inviaacopernico;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.dao.exceptions.NoRowsAffected;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebformsdrivers.postcompletioncewutility.internal.ServiceCaller;
import prgm.pdfwebformsdrivers.postcompletioncewutility.model.ServiceResponse;
import prgm.pdfwebformsdrivers.postcompletioncewutility.model.ServiziEsitoModel;
import prgm.pdfwebformsdrivers.postcompletioncewutility.utils.PostCompletionCewUtils;

public class InviaACopernicoService {
	
	private static String XML_NAME = "PdfWebFormDriver.PostCompletionCewUtility.InviaACopernico.ServizioCopernico";
	
	/*
	 * Fondi italia iniziale
	 * Fondi irlanda iniziale
	 */
	public static ServiceResponse chiamaInviaACopernicoNuovaPropostaFondi(ClientSessionContext csc, PdfModel pdfModel, InputServizioCopernicoModel inputServizio, byte[] pdfContent) throws Exception, DAOException {
		String nomeServizio = "inviaACopernicoNuovaPropostaFondi";
		PostCompletionCewUtils.buildAdeguatezza(inputServizio, false);
		PostCompletionCewUtils.buildDatiCopernico(csc, inputServizio, pdfModel, true, false, pdfContent, false);
		return chiamaServizio(csc, pdfModel, inputServizio, nomeServizio);
	}
	
	public static ServiceResponse chiamaInviaACopernicoSwitchPropostaFondiDis(ClientSessionContext csc, PdfModel pdfModel, InputServizioCopernicoModel inputServizio, byte[] pdfContent) throws Exception, DAOException {
		String nomeServizio = "inviaACopernicoSwitchPropostaFondiDis";
		PostCompletionCewUtils.buildAdeguatezza(inputServizio, false);
		PostCompletionCewUtils.buildDatiCopernico(csc, inputServizio, pdfModel, false, true, pdfContent, true); 
		return chiamaServizioSwitch(csc, pdfModel, inputServizio, nomeServizio, true);
	}
	
	public static ServiceResponse chiamaInviainviaACopernicoSwitchPropostaFondiInvIrlandesi(ClientSessionContext csc, PdfModel pdfModel, InputServizioCopernicoModel inputServizio, byte[] pdfContent) throws Exception, DAOException {
		String nomeServizio = "inviaACopernicoSwitchPropostaFondiInvIrlandesi";
		PostCompletionCewUtils.buildAdeguatezza(inputServizio, false);
		PostCompletionCewUtils.buildDatiCopernico(csc, inputServizio, pdfModel, true, false, pdfContent, true); 
		return chiamaServizioSwitch(csc, pdfModel, inputServizio, nomeServizio, false);
	}
	
	public static ServiceResponse chiamaInviainviaACopernicoSwitchPropostaFondiInvItaliani(ClientSessionContext csc, PdfModel pdfModel, InputServizioCopernicoModel inputServizio, byte[] pdfContent) throws Exception, DAOException {
		String nomeServizio = "inviaACopernicoSwitchPropostaFondiInvItaliani";
		PostCompletionCewUtils.buildAdeguatezza(inputServizio, false);
		PostCompletionCewUtils.buildDatiCopernico(csc, inputServizio, pdfModel, true, false, pdfContent, true); 
		return chiamaServizioSwitch(csc, pdfModel, inputServizio, nomeServizio, false);
	}
	
	public static ServiceResponse chiamaInviainviaACopernicoSwitchPropostaFondiInvAggItaliani(ClientSessionContext csc, PdfModel pdfModel, InputServizioCopernicoModel inputServizio, byte[] pdfContent) throws Exception, DAOException {
		String nomeServizio = "inviaACopernicoSwitchPropostaFondiInvAggItaliani";
		PostCompletionCewUtils.buildAdeguatezza(inputServizio, false);
		PostCompletionCewUtils.buildDatiCopernico(csc, inputServizio, pdfModel, true, false, pdfContent, true); 
		return chiamaServizioSwitch(csc, pdfModel, inputServizio, nomeServizio, false);
	}
	
	public static ServiceResponse chiamaInviainviaACopernicoSwitchPropostaFondiInvAggIrlandesi(ClientSessionContext csc, PdfModel pdfModel, InputServizioCopernicoModel inputServizio, byte[] pdfContent) throws Exception, DAOException {
		String nomeServizio = "inviaACopernicoSwitchPropostaFondiInvAggIrlandesi";
		PostCompletionCewUtils.buildAdeguatezza(inputServizio, false);
		PostCompletionCewUtils.buildDatiCopernico(csc, inputServizio, pdfModel, true, false, pdfContent, true); 
		return chiamaServizioSwitch(csc, pdfModel, inputServizio, nomeServizio, false);
	}
	
	/*
	 * Fondi italia aggiuntivo
	 * Fondi irlanda aggiuntivo
	 */
	public static ServiceResponse chiamaInviaACopernicoAggiuntivoPropostaFondi(ClientSessionContext csc, PdfModel pdfModel, InputServizioCopernicoModel inputServizio, byte[] pdfContent) throws Exception, DAOException {
		String nomeServizio = "inviaACopernicoAggiuntivoPropostaFondi";
		PostCompletionCewUtils.buildAdeguatezza(inputServizio, true);
		PostCompletionCewUtils.buildDatiCopernico(csc, inputServizio, pdfModel, true, false, pdfContent, false);
		return chiamaServizio(csc, pdfModel, inputServizio, nomeServizio);
	}
	
	/*
	 * Fondi italia rimborso
	 * Fondi irlanda rimborso
	 */
	public static ServiceResponse chiamaInviaACopernicoRimborsoPropostaFondi(ClientSessionContext csc, PdfModel pdfModel, InputServizioCopernicoModel inputServizio, byte[] pdfContent) throws Exception, DAOException {
		String nomeServizio = "inviaACopernicoRimborsoPropostaFondi";
		PostCompletionCewUtils.buildDatiCopernico(csc, inputServizio, pdfModel, false, true, pdfContent, false);
		return chiamaServizio(csc, pdfModel, inputServizio, nomeServizio);
	}
	
	public static ServiceResponse chiamaInviaACopernicoConversionePropostaFondi(ClientSessionContext csc, PdfModel pdfModel, InputServizioCopernicoModel inputServizio, byte[] pdfContent) throws Exception, DAOException {
		String nomeServizio = "inviaACopernicoConversionePropostaFondi";
		PostCompletionCewUtils.buildDatiCopernico(csc, inputServizio, pdfModel, true, true, pdfContent, false);
		return chiamaServizio(csc, pdfModel, inputServizio, nomeServizio);
	}
	
	public static ServiceResponse chiamaInviaACopernicoConversioneProgrammataPropostaFondi(ClientSessionContext csc, PdfModel pdfModel, InputServizioCopernicoModel inputServizio, byte[] pdfContent) throws Exception, DAOException {
		String nomeServizio = "inviaACopernicoConversioneProgrammataPropostaFondi";
		PostCompletionCewUtils.buildDatiCopernico(csc, inputServizio, pdfModel, true, true, pdfContent, false);
		return chiamaServizio(csc, pdfModel, inputServizio, nomeServizio);
	}
	
	/*
	 * Fondi italia rimborso programmato
	 * Fondi irlanda rimborso programmato
	 */
	public static ServiceResponse chiamaInviaACopernicoRimborsoProgrammatoPropostaFondi(ClientSessionContext csc, PdfModel pdfModel, InputServizioCopernicoModel inputServizio, byte[] pdfContent) throws Exception, DAOException {
		String nomeServizio = "inviaACopernicoRimborsoProgrammatoPropostaFondi";
		PostCompletionCewUtils.buildDatiCopernico(csc, inputServizio, pdfModel, false, true, pdfContent, false);
		return chiamaServizio(csc, pdfModel, inputServizio, nomeServizio);
	}
	
	private static ServiceResponse chiamaServizio(ClientSessionContext csc, PdfModel pdfModel, InputServizioCopernicoModel inputServizio, String nomeServizio) {
		String err = ServiceCaller.callQASservice(csc, XML_NAME, nomeServizio, inputServizio);
		
		if(null!=err) ServiceResponse.esitoERROR(err);

		if(inputServizio.getEsitoChiamata().isNull()){
			return ServiceResponse.esitoFATAL("Esito applicativo non pervenuto nella chiamata al servizio Copernico : " + nomeServizio);
		}else if(!inputServizio.getEsitoChiamata().equals("0")){
			return ServiceResponse.esitoFATAL(inputServizio.getMessaggioOutputChiamata().stringValue());
		}
		return ServiceResponse.esitoOK();
	}
	
	private static ServiceResponse chiamaServizioSwitch(ClientSessionContext csc, PdfModel pdfModel, InputServizioCopernicoModel inputServizio, String nomeServizio, boolean isRimborso) {
		String err = ServiceCaller.callQASservice(csc, XML_NAME, nomeServizio, inputServizio);
		if(null!=err) ServiceResponse.esitoERROR(err);

		if(inputServizio.getEsitoChiamata().isNull()){
			return ServiceResponse.esitoFATAL("Esito applicativo non pervenuto nella chiamata al servizio Copernico : " + nomeServizio);
		}else if(!inputServizio.getEsitoChiamata().equals("0")){
			return ServiceResponse.esitoFATAL(inputServizio.getMessaggioOutputChiamata().stringValue());
		}
		ServiziEsitoModel esito = new ServiziEsitoModel();
		esito.setPdfInstanceId(pdfModel.getPdfData().getPdfInstanceId());
		String nomeQuery = null;
		
		if (isRimborso) {
			esito.setDataElabSwitchDis(Tools.now());
			nomeQuery = "esitoElaborazioneDis";
		} else {
			esito.setDataElabSwitchInv(Tools.now());
			nomeQuery = "esitoElaborazioneInv";
		}
		
		
		try {
			writeEsito(csc, esito, nomeQuery);
		} catch (DAOException e) {
			return ServiceResponse.esitoWARNING("Errore salvataggio dati in tabella PDF_POSTCOMP_DATA"+e.getMessage());
		}
		return ServiceResponse.esitoOK();
	}
	
	private static void writeEsito(ClientSessionContext csc, ServiziEsitoModel esito, String nomeQuery) throws DAOException {
		DAOObject dao = new DAOObject(csc,XML_NAME);
		try{
			dao.executeTableUpdateAccess(nomeQuery,esito);
		}catch(NoRowsAffected nra){
			dao.executeTableInsertAccess(nomeQuery,esito);
		}			
	}
}
