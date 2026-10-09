package prgm.cedacri.adeguatezza.model;
/*
 * Input Servizio Lettura Ultimo Questionario 
 */
public class InputGetQuestionarioBean implements java.io.Serializable
{
	private static final long serialVersionUID = 0;
	/*
	 * Input
	 * ndgDoss : ndg del cliente
	 * ndgTemp : ndg temporaneo del cliente
	 * canVend : canale di vendita
	 * userKey : chiave univoca di questionario (facoltativo)
	 * country  : sigla paese 3 caratteri usata per log
	 * username : sigla utente 10 caratteri usata per log
	 * pg : persona giuridica
	 */
	private String ndgDoss  = new String();
	private String ndgTemp  = new String();
	private String canVend  = new String();
	private String userKey  = new String(); 
	private String country  = new String();
	private String username = new String();
	private String pg = new String();

	/*
	 * Metodi Set e Get
	 */
	public String getNdgDoss() {
		return ndgDoss;
	}
	public void setNdgDoss(String ndgDoss) {
		this.ndgDoss = ndgDoss;
	}
	public String getCanVend() {
		return canVend;
	}
	public void setCanVend(String canVend) {
		this.canVend = canVend;
	}
	public String getUserKey() {
		return userKey;
	}
	public void setUserKey(String userKey) {
		this.userKey = userKey;
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
	public String getPG() {
		return pg;
	}
	public void setPG(String pg) {
		this.pg = pg;
	}
}
