package prgm.pdfwebformsdrivers.postcompletioncewutility.riepilogoprimenuove;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.dao.exceptions.NoRowsAffected;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebformsdrivers.postcompletioncewutility.internal.ServiceCaller;
import prgm.pdfwebformsdrivers.postcompletioncewutility.model.PdfInstancePostCompletionModel;
import prgm.pdfwebformsdrivers.postcompletioncewutility.model.ServiceResponse;
import prgm.pdfwebformsdrivers.postcompletioncewutility.model.ServiziEsitoModel;
import prgm.pdfwebformsdrivers.postcompletioncewutility.utils.PostCompletionCewUtils;

public class RiepilogoPrimeNuoveService {

	private static String XML_NAME = "PdfWebFormDriver.PostCompletionCewUtility.RiepilogoPrimeNuove.RiepilogoPrimeNuoveFondiProcess";
	
	private static final String S_IDDISPOSIZIONE = "<IDDISPOSIZIONE>";
	private static final String S_ERRORE_SALVATAGGIO_DATI_POSTCOMPL = "Errore salvataggio dati in tabella PDF_POSTCOMP_DATA";
	
	/*
	 * Fondi italia iniziale compresi DC
	 * Fondi italia aggiuntivi DC
	 * Fondi Irlanda iniziale compresi DC
	 * Fondi Irlanda aggiuntivi DC
	 */
	
	private RiepilogoPrimeNuoveService() {}

	public static void arricchisciInput(ClientSessionContext csc, PdfModel pdfModel, InputRiepilogoPrimeNuoveModel inputServizio, boolean appendToDescRichiesta) throws Exception, DAOException {
		// Per i servizi 
		// FD-FOL-RiepilogoPrimeNuove
		// FD-FOL-RiepConfAgg
		// FD-FOL-RiepilogoConferma
		// FD-FOL-PropostaSwitchFondiInvestiti
		// FD-FOL-RiepConfSwitch
		// FD-FOL-ConversioneProgrammataFondi
		// FD-FOL-RimborsoProgrammatoFondi 
		
		PdfInstancePostCompletionModel pdfInstanceModel = PostCompletionCewUtils.getDatiPdf(csc, pdfModel.getPdfData().getPdfInstanceId());
		inputServizio.setIdVendente(pdfInstanceModel.getCodAgente());
		inputServizio.setIdAgente(!pdfInstanceModel.getCodAgenteImpersonato().isNull() ? pdfInstanceModel.getCodAgenteImpersonato() : pdfInstanceModel.getCodAgente());
		
		if (appendToDescRichiesta) {
			// Solo per 
			// FD-FOL-ConversioneProgrammataFondi
			// FD-FOL-RimborsoProgrammatoFondi
			// concatenare in DescRichiesta
			// \nID Vendente:=<cod fb Vendente>
			inputServizio.setDescRichiesta(new StringType(inputServizio.getDescRichiesta() + "\nID Vendente:=" + pdfInstanceModel.getCodAgente()));
		}
	}
	
	public static ServiceResponse chiamaRiepilogoPrimeNuove(ClientSessionContext csc, PdfModel pdfModel, InputRiepilogoPrimeNuoveModel inputServizio) throws Exception, DAOException {		
		arricchisciInput(csc, pdfModel, inputServizio, false);
		
		String nomeServizio = "riepilogoPrimeNuove";
		PostCompletionCewUtils.buildAdeguatezza(inputServizio);
		PostCompletionCewUtils.buildDatiRiepilogoPrimeNuove(pdfModel, inputServizio, true, false, false);		
		return chiamaServizio(csc, pdfModel, inputServizio, nomeServizio);
	}
	
	/*
	 * Fondi italia aggiuntivi no DC
	 * Fondi Irlanda aggiuntivi no DC
	 */
	public static ServiceResponse chiamaRiepConfAgg(ClientSessionContext csc, PdfModel pdfModel, InputRiepilogoPrimeNuoveModel inputServizio) throws Exception, DAOException {
		arricchisciInput(csc, pdfModel, inputServizio, false);
		
		String nomeServizio = "riepConfAgg";
		PostCompletionCewUtils.buildAdeguatezza(inputServizio);
		PostCompletionCewUtils.buildDatiRiepilogoPrimeNuove(pdfModel, inputServizio, true, false, false);
		return chiamaServizioRiepConfAgg(csc, pdfModel, inputServizio, nomeServizio);
	}
	
	/*
	 * Fondi italia rimborso
	 * Fondi irlanda rimborso
	 */
	public static ServiceResponse chiamaRiepilogoConferma(ClientSessionContext csc, PdfModel pdfModel, InputRiepilogoPrimeNuoveModel inputServizio) throws Exception, DAOException {
		arricchisciInput(csc, pdfModel, inputServizio, false);
		
		String nomeServizio = "riepilogoConferma";
		PostCompletionCewUtils.buildDatiRiepilogoPrimeNuove(pdfModel, inputServizio, false, true, false);
		return chiamaServizio(csc, pdfModel, inputServizio, nomeServizio);
	}
	
	/*
	 * Fondi italia rimborso programmato
	 * Fondi irlanda rimborso programmato
	 */
	public static ServiceResponse chiamaRimborsoProgrammatoFondi(ClientSessionContext csc, PdfModel pdfModel, InputRiepilogoPrimeNuoveModel inputServizio) throws Exception, DAOException {
		arricchisciInput(csc, pdfModel, inputServizio, true);
		
		String nomeServizio = "rimborsoProgrammatoFondi";
		PostCompletionCewUtils.buildDatiRiepilogoPrimeNuove(pdfModel, inputServizio, false, true, false); 
		return chiamaServizio(csc, pdfModel, inputServizio, nomeServizio);
	}
	
	/*
	 * Fondi Irlanda Conversioni
	 */
	public static ServiceResponse chiamaRiepConfSwitch(ClientSessionContext csc, PdfModel pdfModel, InputRiepilogoPrimeNuoveModel inputServizio) throws Exception, DAOException {
		arricchisciInput(csc, pdfModel, inputServizio, false);
		
		String nomeServizio = "riepConfSwitch";
		PostCompletionCewUtils.buildAdeguatezza(inputServizio);
		PostCompletionCewUtils.buildDatiRiepilogoPrimeNuove(pdfModel, inputServizio, true, false, false);
		return chiamaServizio(csc, pdfModel, inputServizio, nomeServizio);
	}
	
	/*
	 * Fondi Irlanda Conversioni Programmate
	 */
	public static ServiceResponse chiamaConversioneProgrammataFondi(ClientSessionContext csc, PdfModel pdfModel, InputRiepilogoPrimeNuoveModel inputServizio) throws Exception, DAOException {
		arricchisciInput(csc, pdfModel, inputServizio, true);
		
		String nomeServizio = "conversioneProgrammataFondi";
		PostCompletionCewUtils.buildAdeguatezza(inputServizio);
		PostCompletionCewUtils.buildDatiRiepilogoPrimeNuove(pdfModel, inputServizio, true, false, false);
		return chiamaServizio(csc, pdfModel, inputServizio, nomeServizio);
	}
	
	/*
	 * Fondi Italia e Irlanda Switch Disinvestimento
	 */
	public static ServiceResponse chiamaPropostaSwitchFondiDisinvestiti(ClientSessionContext csc, PdfModel pdfModel, InputRiepilogoPrimeNuoveModel inputServizio) {
		String nomeServizio = "propostaSwitchFondiDisinvestiti";
		PostCompletionCewUtils.buildAdeguatezza(inputServizio);
		PostCompletionCewUtils.buildDatiRiepilogoPrimeNuove(pdfModel, inputServizio, false, true,true);
		compilaPacBoostPerSwitch(pdfModel, inputServizio);
		return chiamaServizioSwitch(csc, pdfModel, inputServizio, nomeServizio, true);
	}
	
	private static void compilaPacBoostPerSwitch(PdfModel pdf, InputRiepilogoPrimeNuoveModel inputServizio) {
		inputServizio.setOpzioni(new StringType());
		if(pdf.getPdfData().getIsSwitch().booleanValue() && pdf.getPdfData().getPdfs().size() > 1) { // Se è uno switch
			PdfDataModel pdfData = (PdfDataModel)pdf.getPdfData().getPdfs().get(1); // Prendo i dati del secondo pdf
			AbstractType tipoPacBoostPacSottoscrizione = pdfData.read("TipoPacBoostPacSottoscrizione");
			if(tipoPacBoostPacSottoscrizione != null && tipoPacBoostPacSottoscrizione.equalsIgnoreCase("PACBOOST")) {
				inputServizio.setOpzioni(new StringType("BO"));
			}
		}
	}
	
	/*
	 * Fondi Italia e Irlanda Switch Investimento
	 */
	public static ServiceResponse chiamaPropostaSwitchFondiInvestiti(ClientSessionContext csc, PdfModel pdfModel, InputRiepilogoPrimeNuoveModel inputServizio) throws Exception, DAOException {
		arricchisciInput(csc, pdfModel, inputServizio, false);
		
		String nomeServizio = "propostaSwitchFondiInvestiti";
		PostCompletionCewUtils.buildAdeguatezza(inputServizio);
		PostCompletionCewUtils.buildDatiRiepilogoPrimeNuove(pdfModel, inputServizio, true, false, true);
		return chiamaServizioSwitch(csc, pdfModel, inputServizio, nomeServizio, false);
	}
	
	private static ServiceResponse chiamaServizio(ClientSessionContext csc, PdfModel pdfModel, InputRiepilogoPrimeNuoveModel inputServizio, String nomeServizio) {
		String err = ServiceCaller.callQASservice(csc, XML_NAME, nomeServizio, inputServizio);
		if(null!=err) return ServiceResponse.esitoERROR(err);
		if(null==inputServizio.getOutput() 
				|| null==inputServizio.getOutput().getIdDisposizione() || inputServizio.getOutput().getIdDisposizione().isNull()
				//|| null==inputServizio.getOutput().getIdIstanza() || inputServizio.getOutput().getIdIstanza().isNull() Defect 35948
				) {
			return ServiceResponse.esitoFATAL("Errore nei dati ricevuti dal servizio " + nomeServizio);
		}
		ServiziEsitoModel esito = new ServiziEsitoModel();
		esito.setPdfInstanceId(pdfModel.getPdfData().getPdfInstanceId());
		esito.setDataElabPrimeNuove(Tools.now());
		String info = S_IDDISPOSIZIONE+inputServizio.getOutput().getIdDisposizione()+"</IDDISPOSIZIONE>";
		if (null != inputServizio.getOutput().getIdIstanza() && !inputServizio.getOutput().getIdIstanza().isNull()) { //Defect 35948
			info = info + "<IDISTANZA>"+inputServizio.getOutput().getIdIstanza()+"</IDISTANZA>";
		}
		esito.setInfoPrimeNuove(new StringType(info));
		try {
			writeEsito(csc, esito, "esitoElaborazionePrimeNuove");
		} catch (DAOException e) {
			return ServiceResponse.esitoWARNING(S_ERRORE_SALVATAGGIO_DATI_POSTCOMPL+e.getMessage());
		}
		return ServiceResponse.esitoOK();
	}
	
	private static ServiceResponse chiamaServizioRiepConfAgg(ClientSessionContext csc, PdfModel pdfModel, InputRiepilogoPrimeNuoveModel inputServizio, String nomeServizio) {
		String err = ServiceCaller.callQASservice(csc, XML_NAME, nomeServizio, inputServizio);
		if(null!=err) return ServiceResponse.esitoERROR(err);
		if(null==inputServizio.getOutput() 	|| !("S".equals(inputServizio.getOutput().getEsito().stringValue()))){
			return ServiceResponse.esitoFATAL("Errore nei dati ricevuti dal servizio"+nomeServizio);
		}
		ServiziEsitoModel esito = new ServiziEsitoModel();
		esito.setPdfInstanceId(pdfModel.getPdfData().getPdfInstanceId());
		esito.setDataElabConfAgg(Tools.now());
		esito.setInfoConfAgg(new StringType(S_IDDISPOSIZIONE+inputServizio.getOutput().getIdDisposizione()+"</IDDISPOSIZIONE><IDRICHIESTA>"+inputServizio.getOutput().getIdRichiesta()+"</IDRICHIESTA>"));
		try {
			writeEsito(csc, esito, "esitoElaborazioneConfAgg");
		} catch (DAOException e) {
			return ServiceResponse.esitoWARNING(S_ERRORE_SALVATAGGIO_DATI_POSTCOMPL+e.getMessage());
		}
		return ServiceResponse.esitoOK();
	}
	
	private static ServiceResponse chiamaServizioSwitch(ClientSessionContext csc, PdfModel pdfModel, InputRiepilogoPrimeNuoveModel inputServizio, String nomeServizio, boolean isRimborso) {
		String err = ServiceCaller.callQASservice(csc, XML_NAME, nomeServizio, inputServizio);
		if(null!=err) return ServiceResponse.esitoERROR(err);
		if(null==inputServizio.getOutput()){
			return ServiceResponse.esitoFATAL("Errore nei dati ricevuti dal servizio"+nomeServizio);
		}
		ServiziEsitoModel esito = new ServiziEsitoModel();
		esito.setPdfInstanceId(pdfModel.getPdfData().getPdfInstanceId());
		String info = "" ;
		if (null != inputServizio.getOutput().getIdDisposizione() && !inputServizio.getOutput().getIdDisposizione().isNull()) { 
			info = info + S_IDDISPOSIZIONE+inputServizio.getOutput().getIdDisposizione()+"</IDDISPOSIZIONE>";
		}
		if (null != inputServizio.getOutput().getIdIstanza() && !inputServizio.getOutput().getIdIstanza().isNull()) { 
			info = info + "<IDISTANZA>"+inputServizio.getOutput().getIdIstanza()+"</IDISTANZA>";
		}
		if (isRimborso) {
			esito.setDataElabSwitchDis(Tools.now());
			esito.setInfoSwitchDis(new StringType(info));
			try {
				writeEsito(csc, esito, "esitoElaborazioneDis");
			} catch (DAOException e) {
				return ServiceResponse.esitoWARNING(S_ERRORE_SALVATAGGIO_DATI_POSTCOMPL+e.getMessage());
			}
		} else {
			esito.setDataElabSwitchInv(Tools.now());
			esito.setInfoSwitchInv(new StringType(info));
			try {
				writeEsito(csc, esito, "esitoElaborazioneInv");
			} catch (DAOException e) {
				return ServiceResponse.esitoWARNING(S_ERRORE_SALVATAGGIO_DATI_POSTCOMPL+e.getMessage());
			}
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
