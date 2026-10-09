package prgm.cedacri.adeguatezza.model;
/*
 * Input servizio CancellaQuestionario
 */
public class InputCancellaQuestionarioBean  implements java.io.Serializable
{
	private static final long serialVersionUID = 0;
	/*
	 * Input
	 * ndgDoss : ndg del cliente
	 * ndgTemp : ndg temporaneo del cliente
	 * canVend : canale di vendita
	 * country  : sigla paese 3 caratteri usata per log
	 * username : sigla utente 10 caratteri usata per log
	 */
	private String ndgDoss  = new String();
	private String ndgTemp  = new String();
	private String canVend  = new String();
	private String country  = new String();
	private String username = new String();

	public String getCanVend() {
		return canVend;
	}
	public void setCanVend(String canVend) {
		this.canVend = canVend;
	}
	public String getNdgDoss() {
		return ndgDoss;
	}
	public void setNdgDoss(String ndgDoss) {
		this.ndgDoss = ndgDoss;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getNdgTemp() {
		return ndgTemp;
	}
	public void setNdgTemp(String ndgTemp) {
		this.ndgTemp = ndgTemp;
	}

}
