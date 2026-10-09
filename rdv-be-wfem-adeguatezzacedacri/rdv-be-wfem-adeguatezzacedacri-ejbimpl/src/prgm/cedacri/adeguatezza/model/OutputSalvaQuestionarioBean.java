package prgm.cedacri.adeguatezza.model;
/*
 * Output Servizio SalvaQuestionario
 */
public class OutputSalvaQuestionarioBean implements java.io.Serializable
{
	public static final long serialVersionUID = 0;
	/*
	 * Sezione dedicata al codice e descrizione di ritorno della funzione
	 * Esito   : codice di errore, 000 se con successo
	 * DescErr : Motivo dell'errore
	 * numElem : numero della domanda coinvolta nell'errore
	 */
	private String descErr = new String();
	private String esito   = new String();
	private int numElem = 0;
	private int dfinval = 0;
	
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
	public int getNumElem() {
		return numElem;
	}
	public void setNumElem(int numElem) {
		this.numElem = numElem;
	}	
	public int getDfinval() {
		return dfinval;
	}
	public void setDfinval(int dfinval) {
		this.dfinval = dfinval;
	}	
}
