package prgm.cedacri.adeguatezza.model;
/*
 * Elemento della lista che compone i Punteggi
 */
public class ElementoPunteggiBean  implements java.io.Serializable
{
	public static final long serialVersionUID = 0;
	/*
	 * Elemento del Punteggio
	 */
	private String dominio = new String();
	private int    valDomi = 0;
	
	/*
	 * Metodi Set e Get
	 */
	public String getDominio() {
		return dominio;
	}
	public void setDominio(String dominio) {
		this.dominio = dominio;
	}
	public int getValDomi() {
		return valDomi;
	}
	public void setValDomi(int valDomi) {
		this.valDomi = valDomi;
	}
}
