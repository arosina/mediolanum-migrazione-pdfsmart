package prgm.cedacri.adeguatezza.model;
/*
 * Elemento della lista che compone le risposte del questionario 
 */
public class ElementoQuestionarioRisposteBean implements java.io.Serializable
{
	public static final long serialVersionUID = 0;
	/*
	 * Elemento delle Risposte del Questionario
	 * numElem : numero domanda nella sezione
	 * numSele : numero risposta della domanda
	 */
	private int numElem = 0;
	private int numSele = 0;
	
	/*
	 * Metodi Set e Get
	 */
	public int getNumElem() {
		return numElem;
	}
	public void setNumElem(int numElem) {
		this.numElem = numElem;
	}
	public int getNumSele() {
		return numSele;
	}
	public void setNumSele(int numSele) {
		this.numSele = numSele;
	}
}
