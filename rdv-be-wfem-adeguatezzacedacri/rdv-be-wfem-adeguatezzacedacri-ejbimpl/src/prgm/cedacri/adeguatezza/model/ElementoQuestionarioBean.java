package prgm.cedacri.adeguatezza.model;
/*
 * Elemento della lista che compone le informazioni del questionario 
 */
public class ElementoQuestionarioBean implements java.io.Serializable
{
	public static final long serialVersionUID = 0;
	/*
	 * Elemento del Questionario
	 * tipElem : tipologia (sezione,commento,domanda,risposta)
	 * numElem : numero domanda nella sezione
	 * numSele : numero risposta della domanda
	 * testEle : testo da visualizzare
	 * selSele : se selezionato e tipo di selezione
	 */
	private String tipElem = new String();
	private String testEle = new String();
	private int    numElem = 0;
	private int    numSele = 0;
	private String selSele = new String();
	
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
	public String getSelSele() {
		return selSele;
	}
	public void setSelSele(String selSele) {
		this.selSele = selSele;
	}
	public String getTestEle() {
		return testEle;
	}
	public void setTestEle(String testEle) {
		this.testEle = testEle;
	}
	public String getTipElem() {
		return tipElem;
	}
	public void setTipElem(String tipElem) {
		this.tipElem = tipElem;
	} 
}
