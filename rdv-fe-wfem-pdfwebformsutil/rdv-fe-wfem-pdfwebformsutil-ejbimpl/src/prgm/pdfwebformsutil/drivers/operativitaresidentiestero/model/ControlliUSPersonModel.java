package prgm.pdfwebformsutil.drivers.operativitaresidentiestero.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebformsutil.drivers.operativitaresidentiestero.ControlloOperativitaResidentiEstero;

public class ControlliUSPersonModel extends CommandDataModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private StringType webCookie = new StringType();
	
	private StringType famigliaProdotto = new StringType();
	private StringType tipoOperazione = new StringType();
	private StringType canale = new StringType(ControlloOperativitaResidentiEstero.CANALE_RDV);
	private StringType codiceCliente = new StringType();
	private StringType sgr = new StringType();
	
	private IntegerType resultCode = new IntegerType();
	private StringType resultMessage = new StringType();		
	
	private StringType esito = new StringType();
	private StringType codiceMotivazioneErrore = new StringType();
	private StringType messaggioErrore = new StringType();
	
	public ControlliUSPersonModel() {
		
	}
	
	public ControlliUSPersonModel(String famigliaProdotto, String tipoOperazione) {
		this.famigliaProdotto = new StringType(famigliaProdotto);
		this.tipoOperazione = new StringType(tipoOperazione);
	}
	
	public ControlliUSPersonModel(String famigliaProdotto, String tipoOperazione, String sgr) {
		this(famigliaProdotto, tipoOperazione);
		this.sgr = new StringType(sgr);
	}
	
	public StringType getFamigliaProdotto() {
		return famigliaProdotto;
	}
	public StringType getTipoOperazione() {
		return tipoOperazione;
	}
	public StringType getCanale() {
		return canale;
	}
	public StringType getCodiceCliente() {
		return codiceCliente;
	}
	public StringType getSgr() {
		return sgr;
	}
	public void setFamigliaProdotto(StringType famigliaProdotto) {
		this.famigliaProdotto = famigliaProdotto;
	}
	public void setTipoOperazione(StringType tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}
	public void setCanale(StringType canale) {
		this.canale = canale;
	}
	public void setCodiceCliente(StringType codiceCliente) {
		this.codiceCliente = codiceCliente;
	}
	public void setSgr(StringType sgr) {
		this.sgr = sgr;
	}
	public IntegerType getResultCode() {
		return resultCode;
	}
	public void setResultCode(IntegerType resultCode) {
		this.resultCode = resultCode;
	}
	public StringType getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(StringType resultMessage) {
		this.resultMessage = resultMessage;
	}
	public StringType getEsito() {
		return esito;
	}
	public void setEsito(StringType esito) {
		this.esito = esito;
	}
	public StringType getCodiceMotivazioneErrore() {
		return codiceMotivazioneErrore;
	}
	public void setCodiceMotivazioneErrore(StringType codiceMotivazioneErrore) {
		this.codiceMotivazioneErrore = codiceMotivazioneErrore;
	}
	public StringType getMessaggioErrore() {
		return messaggioErrore;
	}
	public void setMessaggioErrore(StringType messaggioErrore) {
		this.messaggioErrore = messaggioErrore;
	}
	public StringType getWebCookie() {
		return webCookie;
	}
	public void setWebCookie(StringType webCookie) {
		this.webCookie = webCookie;
	}
	
	/**
     * Restituisce una chiave univoca basato sui campi:
     * famigliaProdotto, tipoOperazione, canale, codiceCliente, sgr.
     * 
     * Il metodo concatena i valori (o stringa vuota se il campo è null) separati da un delimitatore
     *  
     * @return una stringa rappresentante chiave univoca.
     */
    public String getKey() {
        // Costruisce la stringa concatenata dai campi interessati
        StringBuilder sb = new StringBuilder();
        sb.append(famigliaProdotto != null ? famigliaProdotto.toString() : "");
        sb.append("|");
        sb.append(tipoOperazione != null ? tipoOperazione.toString() : "");
        sb.append("|");
        sb.append(canale != null ? canale.toString() : "");
        sb.append("|");
        sb.append(codiceCliente != null ? codiceCliente.toString() : "");
        sb.append("|");
        sb.append(sgr != null ? sgr.toString() : "");
        return sb.toString();        
    }
}
