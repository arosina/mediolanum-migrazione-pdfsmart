package prgm.cedacri.adeguatezza.model;
/*
 * Output Servizio CancellaQuestionario 
 */
public class OutputCancellaQuestionarioBean  implements java.io.Serializable
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
	 * Funzioni Get e Set
	 */
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
}
