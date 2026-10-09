package prgm.pdfwebforms.drivers;

import com.atosorigin.wfem.command.ClientSessionContext;

import prgm.pdfwebforms.drivers.io.AfterSavedPdfInputData;
import prgm.pdfwebforms.drivers.io.AfterSavedPdfOutputData;
import prgm.pdfwebforms.drivers.io.BeforePreviewInputData;
import prgm.pdfwebforms.drivers.io.BeforePreviewOutputData;
import prgm.pdfwebforms.drivers.io.CompilationModeSelectionEventInputData;
import prgm.pdfwebforms.drivers.io.CompilationModeSelectionEventOutputData;
import prgm.pdfwebforms.drivers.io.FinalEventInputData;
import prgm.pdfwebforms.drivers.io.FinalEventOutputData;
import prgm.pdfwebforms.drivers.io.FreezeEventInputData;
import prgm.pdfwebforms.drivers.io.FreezeEventOutputData;
import prgm.pdfwebforms.drivers.io.InitPdfInputData;
import prgm.pdfwebforms.drivers.io.InitPdfOutputData;
import prgm.pdfwebforms.drivers.io.LoadPdfInputData;
import prgm.pdfwebforms.drivers.io.LoadPdfOutputData;
import prgm.pdfwebforms.drivers.io.MomEventInputData;
import prgm.pdfwebforms.drivers.io.MomEventOutputData;
import prgm.pdfwebforms.drivers.io.PdfVerifiedInputData;
import prgm.pdfwebforms.drivers.io.PdfVerifiedOutputData;
import prgm.pdfwebforms.drivers.io.ProvideAgevolazioneDipendentiDataRequest;
import prgm.pdfwebforms.drivers.io.ProvideAgevolazioneDipendentiDataResponse;
import prgm.pdfwebforms.drivers.io.ProvideCompilationModesRequest;
import prgm.pdfwebforms.drivers.io.ProvideCompilationModesResponse;
import prgm.pdfwebforms.drivers.io.ProvideCrossFBCustomersDataRequest;
import prgm.pdfwebforms.drivers.io.ProvideCrossFBCustomersDataResponse;
import prgm.pdfwebforms.drivers.io.SendToCliEventInputData;
import prgm.pdfwebforms.drivers.io.SendToCliEventOutputData;
import prgm.pdfwebforms.drivers.io.SubmitPdfInputData;
import prgm.pdfwebforms.drivers.io.SubmitPdfOutputData;
import prgm.pdfwebforms.drivers.io.UnfreezeEventInputData;
import prgm.pdfwebforms.drivers.io.UnfreezeEventOutputData;
import prgm.pdfwebforms.drivers.io.VerifyCopernicoPdfInputData;
import prgm.pdfwebforms.drivers.io.VerifyCopernicoPdfOutputData;
import prgm.pdfwebforms.drivers.io.VerifyPdfInputData;
import prgm.pdfwebforms.drivers.io.VerifyPdfOutputData;
import prgm.pdfwebforms.drivers.io.attach.ProvideAttachmentsDataRequest;
import prgm.pdfwebforms.drivers.io.attach.ProvideAttachmentsDataResponse;
import prgm.pdfwebforms.drivers.io.attach.VerifyAttachmentInputData;
import prgm.pdfwebforms.drivers.io.attach.VerifyAttachmentOutputData;
import prgm.pdfwebforms.drivers.io.basket.ProvideBasketDataRequest;
import prgm.pdfwebforms.drivers.io.basket.ProvideBasketDataResponse;
import prgm.pdfwebforms.drivers.io.idd.ProvideIddDataRequest;
import prgm.pdfwebforms.drivers.io.idd.ProvideIddDataResponse;
import prgm.pdfwebforms.drivers.io.mifid.ProvideMifidDataRequest;
import prgm.pdfwebforms.drivers.io.mifid.ProvideMifidDataResponse;
import prgm.pdfwebforms.drivers.io.prit.ProvidePritDataRequest;
import prgm.pdfwebforms.drivers.io.prit.ProvidePritDataResponse;
import prgm.pdfwebforms.drivers.io.reportadeguatezza.ProvideReportAdeguatezzaDataRequest;
import prgm.pdfwebforms.drivers.io.reportadeguatezza.ProvideReportAdeguatezzaDataResponse;
import prgm.pdfwebforms.drivers.io.sostituzioni.ProvideSostituzioniDataRequest;
import prgm.pdfwebforms.drivers.io.sostituzioni.ProvideSostituzioniDataResponse;
import prgm.pdfwebforms.drivers.io.squadra.ProvideSquadraDataRequest;
import prgm.pdfwebforms.drivers.io.squadra.ProvideSquadraDataResponse;
import prgm.pdfwebforms.drivers.io.srvdispositiva.ProvideSrvDispositivaDataRequest;
import prgm.pdfwebforms.drivers.io.srvdispositiva.ProvideSrvDispositivaDataResponse;
import prgm.pdfwebforms.drivers.io.validation.ValidationEventInputData;
import prgm.pdfwebforms.drivers.io.validation.ValidationEventOutputData;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class PdfBaseDriver extends PdfBaseDriverUtil implements PdfDriverIntf{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static class ModalitaDiSottoscrizione{
		public static int TUTTE = Integer.MAX_VALUE;
		public static int CARTA_LIBERA = 1;
		public static int CARTA_CHIMICA = 2;
		public static int FIRMA_DIGITALE = 4;
		public static int STAMPA = 8;
		public static int COPERNICO = 16;
	}
	
	/**************************************************************************************************
	 * Sovrascrivere per utilizzare anche su MOM l'impostazione della non editabilità dei campi fatta in java
	**************************************************************************************************/
	public boolean keepJavaUneditableFieldsOnMom(){
		return false;
	}
	
	/**************************************************************************************************
	 * Richiamato solo la prima volta che viene caricato il pdf
	**************************************************************************************************/
	public InitPdfOutputData onInitPdf(ClientSessionContext csc, InitPdfInputData input) throws Exception{
		return null;
	}

	/**************************************************************************************************
	 * Richiamato ogni volta che il pdf viene caricato, compresa la prima volta subito dopo l'init
	**************************************************************************************************/
	public LoadPdfOutputData onLoadPdf(ClientSessionContext csc, LoadPdfInputData input) throws Exception{
		return null;
	}

	/**************************************************************************************************
	 * Richiamato per rinfrescare i dati
	**************************************************************************************************/
	public SubmitPdfOutputData onSubmitPdf(ClientSessionContext csc, SubmitPdfInputData input) throws Exception{
		return null;
	}
	
	/**************************************************************************************************
	 * Richiamato dopo aver salvato il pdf, sia con tasto "Salva" sia all'avanti
	 * L'eventuale messaggi d'errore ritornato sarà bloccante per la prosecuzione ma la bozza 
	 * risulterà comunque salvata
	**************************************************************************************************/
	public AfterSavedPdfOutputData onAfterSavedPdf(ClientSessionContext csc, AfterSavedPdfInputData input) throws Exception{
		return null;
	}

	/**************************************************************************************************
	 * Richiamato per effettuare i controlli
	**************************************************************************************************/
	public VerifyPdfOutputData onVerifyPdf(ClientSessionContext csc, VerifyPdfInputData input) throws Exception{
		return null;
	}
	
	/**************************************************************************************************
	 * Richiamato quando controlli ok
	**************************************************************************************************/
	public PdfVerifiedOutputData onPdfVerified(ClientSessionContext csc, PdfVerifiedInputData input) throws Exception{
		return null;
	}

	/**************************************************************************************************
	 * Richiamato sul driver del primo pdf prima della visualizzazione della preview
	**************************************************************************************************/
	public BeforePreviewOutputData onPdfPreview(ClientSessionContext csc, BeforePreviewInputData input) throws Exception{
		return null;
	}

	/**************************************************************************************************
	 * Richiamato sul driver del primo pdf alla selezione della modalita' di compilazione
	**************************************************************************************************/
	public CompilationModeSelectionEventOutputData onCompilationModeSelection(ClientSessionContext csc, CompilationModeSelectionEventInputData input) throws Exception{
		return null;
	}

	/**************************************************************************************************
	 * Richiamato sul driver del primo pdf al completamento del processo, prima che venga poi reso 
	 * persistente il pdf (se non volatile). Il pdf in questo momento non è ancora stato generato ed è
	 * quindi possibile arricchirne i campi per la stampa finale
	**************************************************************************************************/
	public FinalEventOutputData onPdfCompleted(ClientSessionContext csc, FinalEventInputData input) throws Exception{
		return null;
	}

	/**************************************************************************************************
	 * Richiamato per effettuare i controlli all'accettazione copernico Smart
	**************************************************************************************************/
	public VerifyCopernicoPdfOutputData onVerifyCopernicoPdf(ClientSessionContext csc, VerifyCopernicoPdfInputData input) throws Exception{
		return null;
	}
	
	/**************************************************************************************************
	 * Copernico: richiamato sul driver del primo pdf all'invio al cliente prima della scrittura finale su DB
	 * Il pdf in input è quello definitivo
	**************************************************************************************************/
	public SendToCliEventOutputData onSendToCli(ClientSessionContext csc, SendToCliEventInputData input) throws Exception{
		return null;
	}

	/**************************************************************************************************
	 * Richiamato sul driver del primo pdf prima della scrittura finale su DB
	 * Il pdf in input è quello definitivo
	**************************************************************************************************/
	public FreezeEventOutputData onPdfFreeze(ClientSessionContext csc, FreezeEventInputData input) throws Exception{
		return null;
	}

	/**************************************************************************************************
	 * Richiamato sul driver del primo pdf in caso di errori post-freeze
	**************************************************************************************************/
	public UnfreezeEventOutputData onPdfUnfreeze(ClientSessionContext csc, UnfreezeEventInputData input) throws Exception{
		return null;
	}

	/**************************************************************************************************
	 * Richiamato sul driver del primo pdf ad ogni evento pilotato da MOM
	**************************************************************************************************/
	public MomEventOutputData onMomEvent(ClientSessionContext csc, MomEventInputData input) throws Exception{
		return null;
	}
	
	/**************************************************************************************************
	 * Richiamato sul driver del primo pdf ad ogni evento pilotato dall'ambiente di validazione di sede
	**************************************************************************************************/
	public ValidationEventOutputData onValidationEvent(ClientSessionContext csc, ValidationEventInputData input) throws Exception{
		return null;
	}

	/**************************************************************************************************
	 * MOM1: Richiamato sul driver del primo pdf al completamento del processo cartaceo per l'inserimento
	 * di eventuali righe di prit aggiuntive.
	 * E' possibile specificare in output se mantenere o meno l'inserimento delle righe configurate
	 * 
	 * MOM2: Richiamato su ogni driver per l'inserimento di eventuali righe di prit aggiuntive e/o
	 * di codifiche particolari.
	 * includiRigheConfigurate non viene utilizzato e per sovrascrivere prod/ope si usa chiavePritRigaConfigurata 
	**************************************************************************************************/
	public ProvidePritDataResponse providePritData(ClientSessionContext csc, ProvidePritDataRequest input) throws Exception {
		return null;
	}	
	
	/**************************************************************************************************
	 * Richiamato sul driver del primo pdf per il controllo Mifid
	**************************************************************************************************/
	public ProvideMifidDataResponse provideMifidData(ClientSessionContext csc, ProvideMifidDataRequest input) throws Exception {
		return null;
	}
	
	/**************************************************************************************************
	 * Richiamato sul driver del primo pdf per il controllo Idd
	**************************************************************************************************/
	public ProvideIddDataResponse provideIddData(ClientSessionContext csc, ProvideIddDataRequest input) throws Exception {
		return null;
	}
	
	/**************************************************************************************************
	 * Richiamato sul driver del primo pdf per il controllo Sostituzioni
	**************************************************************************************************/
	public ProvideSostituzioniDataResponse provideSostituzioniData(ClientSessionContext csc, ProvideSostituzioniDataRequest input) throws Exception {
		return null;
	}
	
	/**************************************************************************************************
	 * Richiamato sul driver del primo pdf dal batch dello stock dati
	**************************************************************************************************/
	public ProvideSostituzioniDataResponse provideStockData(ClientSessionContext csc, ProvideSostituzioniDataRequest input) throws Exception {
		return null;
	}
	
	/**************************************************************************************************
	 * Richiamato sul driver del primo pdf per avere i dati da passare alla generazione del report adeguatezza
	**************************************************************************************************/
	public ProvideReportAdeguatezzaDataResponse provideReportAdeguatezzaData(ClientSessionContext csc, ProvideReportAdeguatezzaDataRequest input) throws Exception {
		return null;
	}

	/**************************************************************************************************
	 * Richiamato sul driver del primo pdf per avere gli allegati al/ai moduli
	**************************************************************************************************/
	public ProvideAttachmentsDataResponse provideAttachmentsData(ClientSessionContext csc,	ProvideAttachmentsDataRequest input) throws Exception {
		return null;
	}	

	/**************************************************************************************************
	 * Richiamato sul driver del primo pdf per verificare gli allegati al/ai moduli
	**************************************************************************************************/
	public VerifyAttachmentOutputData onVerifyAttachment(ClientSessionContext csc,	VerifyAttachmentInputData input) throws Exception {
		return null;
	}	

	/**************************************************************************************************
	 * Richiamato sul driver del primo pdf per ottenere le modalità di sottoscrizione valide.
	 * Le modalita' di sottoscrizione espresse con questo metodo annullano completamente
	 * le modalita' presenti nella configurazione del modulo e quelle eventualmente passate in input.
	 * @deprecated (Non più utilizzabile. Usare i metodi standard in verify/verified)
	**************************************************************************************************/
	@Deprecated
	public ProvideCompilationModesResponse provideCompilationModes(ClientSessionContext csc, ProvideCompilationModesRequest input) throws Exception {
		return null;
	}

	/**************************************************************************************************
	 * Richiamato per avere i dati dell'agevolazione dipendenti
	 * Il metodo non può sollevare eccezioni
	**************************************************************************************************/
	public ProvideAgevolazioneDipendentiDataResponse provideAgevolazioneDipendentiData(ClientSessionContext csc, ProvideAgevolazioneDipendentiDataRequest input) {
		return null;
	}	

	/**************************************************************************************************
	 * Richiamato sul driver del primo pdf per ottenere i dati aggiuntivi da includere nella chiamata
	 * al "servizio dispositiva".
	**************************************************************************************************/
	public ProvideSrvDispositivaDataResponse provideSrvDispositivaData(ClientSessionContext csc, ProvideSrvDispositivaDataRequest input) throws Exception{
		return null;
	}
	
	/**************************************************************************************************
	 * Richiamato sul driver del primo pdf per ottenere le info relative alle caratteristiche del basket
	 * quando la dispositiva viene sottoscritta "a basket"
	**************************************************************************************************/
	public ProvideBasketDataResponse provideBasketData(ClientSessionContext csc, ProvideBasketDataRequest input) throws Exception {
		return null;
	}
	
	/**************************************************************************************************
	 * Richiamato su ogni driver della dispositiva per ottenere le info relative alla squadra (clienti/ruoli)
	**************************************************************************************************/
	public ProvideSquadraDataResponse provideSquadraData(ClientSessionContext csc, ProvideSquadraDataRequest input) throws Exception {
		return null;
	}
	
	/**************************************************************************************************
	 * Richiamato per avere le informazioni sui clienti che possono essere di tutti gli FB
	**************************************************************************************************/
	public ProvideCrossFBCustomersDataResponse provideCrossFBCustomersData(ClientSessionContext csc, ProvideCrossFBCustomersDataRequest input) throws Exception {
		return null;
	}

}
