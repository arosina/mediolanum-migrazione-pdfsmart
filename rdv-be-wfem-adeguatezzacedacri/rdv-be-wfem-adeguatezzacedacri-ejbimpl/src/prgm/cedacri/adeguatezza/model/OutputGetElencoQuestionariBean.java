package prgm.cedacri.adeguatezza.model;
/*
 * Output servizio GetElencoQuestionari
 */
public class OutputGetElencoQuestionariBean implements java.io.Serializable
{
	public static final long serialVersionUID = 0;
	/*
	 * Sezione dedicata al codice e descrizione di ritorno della funzione
	 * Esito   : codice di errore, 000 se con successo
	 * DescErr : Motivo dell'errore
	 */
	private String descErr = new String();
	private String esito   = new String();
	/*
	 * Output
	 * seCompi : flag che indica se il questionario è stato compilato
	 * storico : elenco dei questionari (classe ElementoStoricoBin)
	 */
	private String seCompi = new String();
	private ElementoStoricoBean[] storico = null;
	
	/*
	 * Metodi Set e Get
	 */
	public ElementoStoricoBean[] getStorico() {
		return storico;
	}
	public void setStorico(ElementoStoricoBean[] storico) {
		this.storico = storico;
	}
	public String getDescErr() {
		return descErr;
	}
	public void setDescErr(String descErr) {
		this.descErr = descErr;
	}
	public String getEsito() {
		return esito;
	}
	public void setEsito(String esito) {
		this.esito = esito;
	}
	public String getSeCompi() {
		return seCompi;
	}
	public void setSeCompi(String seCompi) {
		this.seCompi = seCompi;
	}
}
