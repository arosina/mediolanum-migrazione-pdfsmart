package prgm.pdfwebformsutil.drivers.operativitaresidentiestero;

import java.util.ArrayList;
import java.util.List;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.drivers.PdfAcroFieldNotFoundException;
import prgm.pdfwebforms.drivers.PdfBaseDriver;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsutil.drivers.operativitaresidentiestero.model.ControlliUSPersonModel;
import prgm.pdfwebformsutil.drivers.service.operativitaresidentiestero.ControlliUSPersonService;
import prgm.pdfwebformsutil.drivers.util.Util;

/**
 * Classe per la gestione del controllo di operatività dei residenti all'estero.
 * Utilizza un driver PDF per l'accesso e la manipolazione dei dati e si avvale
 * di un servizio specifico per eseguire controlli relativi a US Person e altre regole.
 *
 * @param <T> il tipo di driver PDF che estende {@link PdfBaseDriver}
 */
public class ControlloOperativitaResidentiEstero<T extends PdfBaseDriver> {
    
    /**
     * Driver per la gestione dei campi PDF.
     */
    private T pdfDriver;
    
    /**
     * Servizio per eseguire controlli specifici su US Person.
     */
    private ControlliUSPersonService<T> controlliUsPersonService = null;
    
    /**
     * Nome base del campo identificativo cliente, usato per costruire il nome effettivo.
     */
    private static final String NDG_CLIENTE = "ndgCliente";
    
    /**
     * Nome base del campo relativo all'ID di censimento cliente.
     */
    private static final String ID_CENSIMENTO_CLIENTE = "idCensimentoCliente";
    
    /**
     * Lista dei listener registrati per ricevere notifiche al termine della validazione.
     */
    private List<ControlloOperativitaResidentiEsteroEventListener> validationEventListeners = new ArrayList<>();
    
    // Codici esito
    public static final String ESITO_OK = "OK";
    public static final String ESITO_KO = "KO";
    
    // Codici famiglia Prodotto
    public static final String FAMIGLIA_PRODOTTO_FONDI_MGF = "FO_MGF";
    public static final String FAMIGLIA_PRODOTTO_FONDI_MGF_PIR = "FO_MGF_PIR";
    public static final String FAMIGLIA_PRODOTTO_FONDI_MIF = "FO_MIF";
    public static final String FAMIGLIA_PRODOTTO_CERTIFICATES = "CERT";
    public static final String FAMIGLIA_PRODOTTO_GRANDI_PATRIMONI = "GP";
    public static final String FAMIGLIA_PRODOTTO_FONDI_DI_TERZI = "FDT";
    public static final String FAMIGLIA_PRODOTTO_POLIZZE_MEDVITA = "PO_MV";
    public static final String FAMIGLIA_PRODOTTO_POLIZZE_MEDVITA_PIR = "PO_MV_PIR";
    public static final String FAMIGLIA_PRODOTTO_POLIZZE_MEDVITA_MILL = "PO_MILL";
    public static final String FAMIGLIA_PRODOTTO_POLIZZE_PREVIDENZA = "PO_PREV";
    public static final String FAMIGLIA_PRODOTTO_POLIZZE_PROTEZIONE = "PO_PROT";
    public static final String FAMIGLIA_PRODOTTO_TITOLI = "TIT";
    
    // Codici tipo operazione
    public static final String TIPO_OPERAZIONE_SOTTOSCRIZIONE_INIZIALE = "104";
    public static final String TIPO_OPERAZIONE_VERSAMENTO_AGGIUNTIVO = "199";
    public static final String TIPO_OPERAZIONE_CONVERSIONE = "83";
    public static final String TIPO_OPERAZIONE_SWITCH = "114";
    public static final String TIPO_OPERAZIONE_SWITCH_SU_STESSO_MANDATO = "200";
    public static final String TIPO_OPERAZIONE_PIC_PROGRAMMATO_STAND_ALONE = "92";
    public static final String TIPO_OPERAZIONE_AMPLIAMENTO_PIANO_PAC = "77";
    public static final String TIPO_OPERAZIONE_VARIAZIONE_PIANO_PAC = "118";
    public static final String TIPO_OPERAZIONE_SWITCH_PROGRAMMATO_STAND_ALONE = "115";
    public static final String TIPO_OPERAZIONE_SWITCH_PROGRAMMATO_SU_STESSO_MANDATO = "201";
    public static final String TIPO_OPERAZIONE_CONVERSIONE_PROGRAMMATA = "84";
    public static final String TIPO_OPERAZIONE_BIG_CHANCE_STAND_ALONE = "79";
    public static final String TIPO_OPERAZIONE_IIS_STAND_ALONE = "90";
    public static final String TIPO_OPERAZIONE_CAMBIO_CO_SOTTOSCRITTORE = "81";
    public static final String TIPO_OPERAZIONE_APERTURACONTO_AMMINISTRATO = "202";
    public static final String TIPO_OPERAZIONE_COMPRAVENDITA = "203";    
    
    // Codici canali
    public static final String CANALE_RDV = "RDV";
    
    // Codici motivazione errore
    public static final String CODICE_MOTIVAZIONE_USPERSON                 = "1";
    public static final String CODICE_MOTIVAZIONE_RESIDENTEUS              = "2";
    public static final String CODICE_MOTIVAZIONE_RESIDENTE_ESTERO         = "3";
    public static final String CODICE_MOTIVAZIONE_CERTIFICATES             = "4";
    public static final String CODICE_MOTIVAZIONE_SGR                      = "5";
    public static final String CODICE_MOTIVAZIONE_RESIDENZA_FISCALE 	   = "6";
    public static final String CODICE_MOTIVAZIONE_SGR_CITTADINANZA_FISCALE = "7";
    
    // Messaggi di errore per i controlli di operatività estero
    public static final String ERRORE_USPERSON = "Non è possibile procedere con la sottoscrizione da parte di un cliente US Person";
    public static final String ERRORE_RESIDENZA_USA = "Non è possibile procedere con la sottoscrizione da parte di un cliente residente USA";
    public static final String ERRORE_RESIDENTE_ESTERO = "Non è possibile procedere con la sottoscrizione da parte di un cliente non Residente";
    public static final String ERRORE_RESIDENZA_FISCALE = "Non è possibile procedere con la sottoscrizione da parte di un cliente fiscalmente residente in un paese diverso da Italia";
    public static final String ERRORE_ESTERO = "Il cliente ha residenza estera, pertanto non è possibile procedere alla sottoscrizione";
    public static final String ERRORE_SGR = "Non è possibile procedere con la sottoscrizione da parte di un cliente residente in Australia, Canada, Nuova Zelanda";
    public static final String ERRORE_CERTIFICATES = "Non è possibile procedere con la sottoscrizione da parte di un cliente residente in una di queste nazioni: Stati Uniti, Canada, Giappone, Australia, Regno Unito, Russia";
    public static final String ERRORE_SGR_CITTADINANZA_FISCALE = "Non è possibile procedere con la sottoscrizione da parte di un cliente con cittadinanza di una di queste nazioni: Australia, Canada, Nuova Zelanda";
    
    
    /**
     * Interfaccia per il listener degli eventi di validazione.
     * Un componente che implementa questa interfaccia può ricevere notifiche
     * al termine del processo di validazione e in caso di errori.
     */
    public interface ControlloOperativitaResidentiEsteroEventListener {
        /**
         * Metodo invocato al termine della validazione per effettuare controlli specifici.
         *
         * @param csc il contesto della sessione client
         * @param pdfData il modello dei dati del PDF
         * @param model il modello contenente i dati dei controlli US Person
         */
        void onOperativitaResidentiEsteroValidation(ClientSessionContext csc, PdfDataModel pdfData, ControlliUSPersonModel model);
        
        /**
         * Metodo invocato in caso di errore durante la validazione.
         *
         * @param csc il contesto della sessione client
         * @param pdfData il modello dei dati del PDF
         * @param model il modello contenente i dati dei controlli US Person
         * @return true se l'errore è stato gestito dal listener, false altrimenti (in caso contrario verrà applicato il default)
         */
        boolean onOperativitaResidentiEsteroValidationError(ClientSessionContext csc, PdfDataModel pdfData, ControlliUSPersonModel model);
    }

    /**
     * Costruttore che inizializza il contesto e il driver PDF.
     *
     * @param csc il contesto della sessione client
     * @param driver il driver PDF da utilizzare
     */
    public ControlloOperativitaResidentiEstero(T driver, boolean enableCache) {        
        setPdfDriver(driver);
        // Inizializza il servizio per i controlli US Person con caching abilitato oppure disabilitato
        controlliUsPersonService = new ControlliUSPersonService<>(driver, enableCache);		
    }
    
    /**
     * Restituisce il driver PDF.
     *
     * @return il driver PDF
     */
    public T getPdfDriver() {
        return pdfDriver;
    }
        
    /**
     * Imposta il driver PDF.
     *
     * @param pdfDriver il driver PDF da impostare
     */
    public void setPdfDriver(T pdfDriver) {
        this.pdfDriver = pdfDriver;
    }
    
    /**
     * Pulisce la cache del servizio di controlli US Person.
     */
    public void clearCache() {
        controlliUsPersonService.clearCache();
    }
    
    /**
     * Aggiunge un listener per gli eventi di validazione.
     *
     * @param listener il listener da aggiungere (non nullo)
     */
    public void addValidationEventListener(ControlloOperativitaResidentiEsteroEventListener listener) {
        if (listener != null) {
            validationEventListeners.add(listener);
        }
    }

    /**
     * Rimuove un listener per gli eventi di validazione.
     *
     * @param listener il listener da rimuovere
     */
    public void removeValidationEventListener(ControlloOperativitaResidentiEsteroEventListener listener) {
        validationEventListeners.remove(listener);
    }
    
    /**
     * Esegue la validazione per un singolo cliente identificato da {@code idxCliente}.
     * Legge i campi identificativi dal modello PDF e imposta il codice cliente nel modello.
     *
     * @param csc il contesto della sessione client
     * @param pdfData il modello dei dati del PDF
     * @param idxCliente indice del cliente (per costruire il nome del campo)
     * @param model il modello contenente i dati dei controlli US Person
     * @throws PdfAcroFieldNotFoundException se il campo identificativo cliente non è definito sul PDF
     * @throws DAOException in caso di errore nell'accesso ai dati tramite il DAO
     * @throws Exception per altri errori generici
     */
    public void validate(ClientSessionContext csc, PdfDataModel pdfData, int idxCliente, ControlliUSPersonModel model)
            throws PdfAcroFieldNotFoundException, Exception, DAOException {
        // Costruisce il nome del campo identificativo cliente (es. "ndgCliente1")
        String ndgClienteFieldName = NDG_CLIENTE + idxCliente;
        // Legge il campo identificativo cliente dal PdfDataModel
        StringType ndgCliente = (StringType) pdfData.read(ndgClienteFieldName);
        
        // Costruisce il nome del campo relativo all'ID di censimento (es. "idCensimentoCliente1")
        String idCensimentoClienteFieldName = ID_CENSIMENTO_CLIENTE + idxCliente;
        // Legge il campo ID di censimento dal PdfDataModel
        StringType idCensimentoCliente = (StringType) pdfData.read(idCensimentoClienteFieldName);
       
        if (ndgCliente == null) {
            // Se il campo identificativo cliente non è definito, lancia un'eccezione specifica
            throw new PdfAcroFieldNotFoundException("Il campo " + ndgClienteFieldName + " non è definito sul PDF");
        }
        
        // Procede con la validazione se almeno uno dei campi (NDG o ID di censimento) è valorizzato        
        if (!ndgCliente.isNull() || (idCensimentoCliente != null && !idCensimentoCliente.isNull())) {
        	// Variabile per salvare il valore senza formattazione con zeri
        	String codiceClienteNonFormattato = null;
        	try {
	            // Se ndgCliente è valorizzato, si usa il suo valore come chiave, altrimenti si utilizza idCensimentoCliente.
	        	if (!ndgCliente.isNull()) {
	        		// Salva il valore non formattato
	        		codiceClienteNonFormattato = ndgCliente.toString();
	        		// Utilizza il riferimento dell'istanza ndgCliente
	                model.setCodiceCliente(ndgCliente);
	                // Il servizio richiede il valore con zeri a sx uso setStringValue perchè non bisogna cambiare il riferimento del model.codiceCliente altrimenti non si visualizza l'errore
	            	model.getCodiceCliente().setStringValue(Util.lZeroPad(model.getCodiceCliente().toString(), 11));
	            } else {
	            	// Anche se non viene formattato salvo il valore per il ripristino
	            	codiceClienteNonFormattato = idCensimentoCliente.toString();
	            	// Utilizza il riferimento dell'istanza idCensimentoCliente 
	                model.setCodiceCliente(idCensimentoCliente);
	            }
	        	
	            // Legge i dati tramite il servizio di controlli
	            ControlliUSPersonModel result = controlliUsPersonService.read(csc, pdfData, model);
  	            
	            // Riprende il riferimento al codiceCliente attuale perchè quello restituito è cachato e l'errore non verrebbe visualizzato
	            result.setCodiceCliente(model.getCodiceCliente());
	            // Se il controllo ha esito positivo, notifica i listener registrati
	            if (eseguiControllo(csc, pdfData, result)) {
	                for (ControlloOperativitaResidentiEsteroEventListener listener : validationEventListeners) {
	                    listener.onOperativitaResidentiEsteroValidation(csc, pdfData, result);
	                }
	            }
        	}
        	finally {
        		if (codiceClienteNonFormattato != null) {
		            // Ripristino il valore non formattato per non creare regressioni nel driver
		            model.getCodiceCliente().setStringValue(codiceClienteNonFormattato);
        		}
        	}
        }	       
    }
    
    /**
     * Esegue la validazione per più clienti specificati tramite un array di indici.
     *
     * @param csc il contesto della sessione client
     * @param pdfData il modello dei dati del PDF
     * @param idxCliente array contenente gli indici dei clienti
     * @param model il modello contenente i dati dei controlli US Person
     * @throws PdfAcroFieldNotFoundException se un campo richiesto non è definito sul PDF
     * @throws DAOException in caso di errore nell'accesso ai dati tramite il DAO
     * @throws Exception per altri errori generici
     */
    public void validate(ClientSessionContext csc, PdfDataModel pdfData, int[] idxCliente, ControlliUSPersonModel model)
            throws PdfAcroFieldNotFoundException, Exception, DAOException {
        // Itera sugli indici e valida ciascun cliente
        for (int idx : idxCliente) {
            validate(csc, pdfData, idx, model);
        }        
    }
    
    /**
     * Esegue i controlli di validazione in base ai risultati ottenuti dal servizio.
     * Se il risultato ha esito "KO", in base al codice motivazione viene aggiunto un errore.
     *
     * @param csc il contesto della sessione client
     * @param pdfData il modello dei dati del PDF
     * @param model il modello contenente i dati dei controlli US Person
     * @return true se il controllo ha esito positivo, false se è stato aggiunto un errore
     */
    private boolean eseguiControllo(ClientSessionContext csc, PdfDataModel pdfData, ControlliUSPersonModel model) {
        // Verifica se l'esito del controllo è negativo
        if (model.getEsito().equals(ESITO_KO)) {
            // Aggiunge errori in base al codice della motivazione
            if (model.getCodiceMotivazioneErrore().equals(CODICE_MOTIVAZIONE_USPERSON)) {
                addError(csc, pdfData, model, ERRORE_USPERSON);
                return false;	
            }
            else if (model.getCodiceMotivazioneErrore().equals(CODICE_MOTIVAZIONE_RESIDENTEUS)) {
                addError(csc, pdfData, model, ERRORE_RESIDENZA_USA);
                return false;	
            }
            else if (model.getCodiceMotivazioneErrore().equals(CODICE_MOTIVAZIONE_RESIDENTE_ESTERO)) {
                addError(csc, pdfData, model, ERRORE_RESIDENTE_ESTERO);
                return false;	
            }
            else if (model.getCodiceMotivazioneErrore().equals(CODICE_MOTIVAZIONE_RESIDENZA_FISCALE)) {
                addError(csc, pdfData, model, ERRORE_RESIDENZA_FISCALE);
                return false;	
            }    
            else if (model.getCodiceMotivazioneErrore().equals(CODICE_MOTIVAZIONE_SGR)) {
                addError(csc, pdfData, model, ERRORE_SGR);
                return false;	
            }   
            else if (model.getCodiceMotivazioneErrore().equals(CODICE_MOTIVAZIONE_CERTIFICATES)) {
                addError(csc, pdfData, model, ERRORE_CERTIFICATES);
                return false;	
            }
            else if (model.getCodiceMotivazioneErrore().equals(CODICE_MOTIVAZIONE_SGR_CITTADINANZA_FISCALE)) {
                addError(csc, pdfData, model, ERRORE_SGR_CITTADINANZA_FISCALE);
                return false;	
            } 
        }
        // Se nessun controllo ha fallito, ritorna true
        return true;
    }

    /**
     * Aggiunge un errore di validazione al modello PDF.
     * Se uno dei listener gestisce l'errore, non vengono effettuate ulteriori azioni.
     * Altrimenti, viene aggiunto un errore al campo codice cliente.
     *
     * @param csc il contesto della sessione client
     * @param pdfData il modello dei dati del PDF
     * @param model il modello contenente i dati dei controlli US Person
     * @param msgError il messaggio di errore da aggiungere
     */
    public void addError(ClientSessionContext csc, PdfDataModel pdfData, ControlliUSPersonModel model, String msgError) {
        boolean gestito = false;
        // Notifica i listener dell'errore
        if (validationEventListeners.size() > 0) {        	    	
            for (ControlloOperativitaResidentiEsteroEventListener listener : validationEventListeners) {
                if (listener.onOperativitaResidentiEsteroValidationError(csc, pdfData, model)) {
                    gestito = true;
                }
            }
        }
        // Se l'errore non è stato gestito, aggiunge il messaggio di errore al campo codice cliente
        if (!gestito && model.getCodiceCliente() != null) {
            model.getCodiceCliente().addTypeError(msgError);
        }
    }
}
