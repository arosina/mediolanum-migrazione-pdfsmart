package prgm.pdfwebformsutil.drivers.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.exceptions.DAOException;

import prgm.pdfwebforms.drivers.PdfBaseDriver;
import prgm.pdfwebforms.model.PdfDataModel;

public abstract class AbstractCachedService<T extends PdfBaseDriver, K, M> extends AbstractPdfDriverService<T> {
    private final Map<String, HolderCachedObject<K, M>> cache = new ConcurrentHashMap<>();
    private boolean enableCache;

    public boolean isEnableCache() {
        return enableCache;
    }

    public void setEnableCache(boolean enableCache) {
        this.enableCache = enableCache;
    }

    protected Map<String, HolderCachedObject<K, M>> getCache() {
        return cache;
    }

    public AbstractCachedService(T pdfDriver, boolean enableCache) {
        super(pdfDriver);
        this.enableCache = enableCache;
    }

    public AbstractCachedService(T pdfDriver) {
        this(pdfDriver, false);
    }

    public void clearCache() {
        cache.clear();
    }

    public void removeFromCache(PdfDataModel pdfData, K key) {
        String cacheKey = getCacheKey(pdfData, key);
        if (cacheKey != null && !cacheKey.isEmpty()) {
            cache.remove(cacheKey);
        }
    }

    /**
     * Metodo di lettura che tenta di recuperare i dati dalla cache.
     *
     * @param csc     Il contesto della sessione del client.
     * @param pdfData Il modello dei dati PDF.
     * @param key     La chiave per il caching.
     * @return Il valore memorizzato nella cache o caricato tramite readData.
     * @throws Exception Se si verifica un errore durante la lettura dei dati.
     */
    public M read(ClientSessionContext csc, PdfDataModel pdfData, K key) throws Exception {
        return read(csc, pdfData, key, false);
    }

    /**
     * Metodo di lettura che tenta di recuperare i dati dalla cache con la possibilità di esecuzione asincrona.
     *
     * Se il caching è abilitato, il metodo prova ad acquisire il semaforo associato al holder.
     * Se il valore non è ancora stato impostato, invoca il metodo astratto {@code readData} e delega
     * l'aggiornamento del valore e il rilascio del semaforo ai callback {@code onExecutionSuccess} o {@code onExecutionError}.
     *
     * @param csc     Il contesto della sessione del client.
     * @param pdfData Il modello dei dati PDF.
     * @param key     La chiave per il caching.
     * @param async   Flag per esecuzione asincrona.
     * @return Il valore memorizzato nella cache o caricato tramite readData.
     * @throws Exception Se si verifica un errore durante la lettura dei dati.
     */
    public M read(ClientSessionContext csc, PdfDataModel pdfData, K key, boolean async) throws Exception {
        if (key == null) {
            return null;
        }

        if (!enableCache) {
            return executeReadData(csc, pdfData, key, async);
        }

        String cacheKey = getCacheKey(pdfData, key);
        if (isCacheKeyInvalid(cacheKey)) {
            return executeReadData(csc, pdfData, key, async);
        }

        HolderCachedObject<K, M> holder = cache.get(cacheKey);
        if (isHolderReady(holder)) {
            return holder.getValue();
        }

        synchronized (getLockForKey(cacheKey)) {
            holder = cache.get(cacheKey);
            if (isHolderReady(holder)) {
                return holder.getValue();
            }

            if (holder == null) {
                holder = createAndPutHolder(cacheKey, key);
            }

            try {
                // Acquisizione del semaforo con timeout di 120 secondi
                if (holder.getSemaphore().tryAcquire(120, TimeUnit.SECONDS)) {
                    if (holder.getValue() == null) {                        
                        // il valore viene settato nelle callback
                    	// il semaforo viene rilasciato nelle calback.
                        executeReadData(csc, pdfData, key, async);
                        // Non rilasciare il semaforo qui; il rilascio avverrà nei callback onExecutionSuccess/onExecutionError.
                    } else {
                        // Se il valore è già stato impostato, rilasciamo il semaforo.
                        holder.getSemaphore().release();
                    }
                } else if (!async) {
                    throw new Exception(String.format("Errore timeout cached service %s", this.getClass().getName()));
                }
            } catch (InterruptedException e) {
                // Ripristina lo stato di interruzione del thread
                Thread.currentThread().interrupt();
                throw new Exception("Thread interrotto durante l'acquisizione del semaforo", e);
            }
        }

        return holder.getValue();
    }

    /**
     * Metodo astratto per leggere i dati. Deve essere implementato dalle sottoclassi.
     *
     * @param csc     Il contesto della sessione del client.
     * @param pdfData Il modello dei dati PDF.
     * @param key     La chiave per il caching.
     * @param async   Flag per esecuzione asincrona.
     * @return Il valore letto.
     * @throws Exception     Se si verifica un errore durante la lettura dei dati.
     * @throws DAOException Se si verifica un errore DAO durante la lettura dei dati.
     */
    protected abstract M readData(ClientSessionContext csc, PdfDataModel pdfData, K key, boolean async)
            throws Exception, DAOException;

    /**
     * Genera una chiave unica per la cache basata sui dati PDF e sulla chiave fornita.
     *
     * @param pdfData Il modello dei dati PDF.
     * @param key     La chiave per il caching.
     * @return La chiave unica per la cache.
     */
    protected String getCacheKey(PdfDataModel pdfData, K key) {
        return key.toString();
    }

    /**
     * Imposta il valore nell'oggetto cache e rilascia il semaforo.
     * Questo metodo viene chiamato dai callback onExecutionSuccess e onExecutionError.
     *
     * @param pdfData Il modello dei dati PDF.
     * @param key     La chiave della cache.
     * @param obj     Il valore da impostare.
     */
    protected void setHolderValueAndReleaseLock(PdfDataModel pdfData, String key, M obj) {
        HolderCachedObject<K, M> holder = cache.get(key);
        try {
            if (holder != null) {
                holder.setValue(obj);
            }
        } finally {
            if (holder != null) {
                holder.getSemaphore().release();
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onExecutionSuccess(Object sender, PdfDataModel pdfData, Object o) {
        String cacheKey = getCacheKey(pdfData, (K) o);
        setHolderValueAndReleaseLock(pdfData, cacheKey, (M) o);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onExecutionError(Object sender, PdfDataModel pdfData, Object o, Throwable e) {
        String cacheKey = getCacheKey(pdfData, (K) o);
        setHolderValueAndReleaseLock(pdfData, cacheKey, null);
    }

    // Metodi Helper

    /**
     * Verifica se la chiave della cache è valida (non nulla e non vuota).
     *
     * @param cacheKey La chiave della cache.
     * @return true se la chiave è nulla o vuota, altrimenti false.
     */
    private boolean isCacheKeyInvalid(String cacheKey) {
        return cacheKey == null || cacheKey.isEmpty();
    }

    /**
     * Verifica se il holder è pronto, ovvero esistente e con valore non nullo.
     *
     * @param holder L'oggetto HolderCachedObject.
     * @return true se il holder contiene un valore, altrimenti false.
     */
    private boolean isHolderReady(HolderCachedObject<K, M> holder) {
        return holder != null && holder.getValue() != null;
    }

    /**
     * Crea un nuovo HolderCachedObject e lo inserisce nella cache.
     *
     * @param cacheKey La chiave della cache.
     * @param key      La chiave generica.
     * @return Il nuovo oggetto HolderCachedObject.
     */
    private HolderCachedObject<K, M> createAndPutHolder(String cacheKey, K key) {
        HolderCachedObject<K, M> holder = new HolderCachedObject<>();
        holder.setCacheKey(cacheKey);
        holder.setKey(key);
        cache.put(cacheKey, holder);
        return holder;
    }

    /**
     * Fornisce un oggetto lock per una specifica chiave della cache.
     * Attualmente viene utilizzata l'istanza corrente, ma si può migliorare la granularità utilizzando lock specifici per chiave.
     *
     * @param cacheKey La chiave della cache.
     * @return L'oggetto lock.
     */
    private Object getLockForKey(String cacheKey) {
        return this;
    }

    /**
     * Metodo helper per eseguire readData con gestione delle DAOException.
     *
     * @param csc     Il contesto della sessione del client.
     * @param pdfData Il modello dei dati PDF.
     * @param key     La chiave per il caching.
     * @param async   Flag per esecuzione asincrona.
     * @return Il valore letto.
     * @throws Exception Se si verifica un errore durante la lettura dei dati.
     */
    private M executeReadData(ClientSessionContext csc, PdfDataModel pdfData, K key, boolean async) throws Exception {
        try {
            return readData(csc, pdfData, key, async);
        } catch (DAOException daoe) {
            throw new Exception("Errore DAO durante la lettura dei dati: " + daoe.getMessage(), daoe);
        }
    }
}
