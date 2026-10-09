package prgm.cedacri.adeguatezza.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;
/*
 * Input Servizio Lettura Ultimo Questionario 
 */
public class InputGetQuestionarioModel extends CommandDataModel implements java.io.Serializable
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
	private StringType ndgDoss  = new StringType();
	private StringType ndgTemp  = new StringType();
	private StringType canVend  = new StringType();
	private StringType userKey  = new StringType(); 
	private StringType country  = new StringType();
	private StringType username = new StringType();
	private StringType pg       = new StringType();

	/*
	 * Metodi Set e Get
	 */
	public StringType getNdgDoss() {
		return ndgDoss;
	}
	public void setNdgDoss(StringType ndgDoss) {
		this.ndgDoss = ndgDoss;
	}
	public StringType getCanVend() {
		return canVend;
	}
	public void setCanVend(StringType canVend) {
		this.canVend = canVend;
	}
	public StringType getUserKey() {
		return userKey;
	}
	public void setUserKey(StringType userKey) {
		this.userKey = userKey;
	}
	public StringType getCountry() {
		return country;
	}
	public void setCountry(StringType country) {
		this.country = country;
	}
	public StringType getUsername() {
		return username;
	}
	public void setUsername(StringType username) {
		this.username = username;
	}
	public StringType getNdgTemp() {
		return ndgTemp;
	}
	public void setNdgTemp(StringType ndgTemp) {
		this.ndgTemp = ndgTemp;
	}
	public StringType getPG() {
		return pg;
	}
	public void setPG(StringType pg) {
		this.pg = pg;
	}
}
