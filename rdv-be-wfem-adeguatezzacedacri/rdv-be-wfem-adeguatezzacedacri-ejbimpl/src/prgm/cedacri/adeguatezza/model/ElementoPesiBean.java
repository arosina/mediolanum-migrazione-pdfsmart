package prgm.cedacri.adeguatezza.model;
/*
 * Elemento della lista che contiene i pesi dati alle risposte
 */
public class ElementoPesiBean  implements java.io.Serializable
{
	public static final long serialVersionUID = 0;
	private int    numDoma = 0;
	private int    numRisp = 0;
	private String dominio = new String();
	private int    pesoRis = 0;
	
	public String getDominio() {
		return dominio;
	}
	public void setDominio(String dominio) {
		this.dominio = dominio;
	}
	public int getNumDoma() {
		return numDoma;
	}
	public void setNumDoma(int numDoma) {
		this.numDoma = numDoma;
	}
	public int getNumRisp() {
		return numRisp;
	}
	public void setNumRisp(int numRisp) {
		this.numRisp = numRisp;
	}
	public int getPesoRis() {
		return pesoRis;
	}
	public void setPesoRis(int pesoRis) {
		this.pesoRis = pesoRis;
	}
	
}
