package prgm.cedacri.adeguatezza.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.*;
/*
 * Input Servizio CalcoloProfilo
 */
public class InputCalcoloProfiloModel extends CommandDataModel implements java.io.Serializable
{
	private static final long serialVersionUID = 0;
	/*
	 * Input
	 * ndgDoss  : ndg del cliente
	 * ndgTemp : ndg temporaneo del cliente
	 * canVend  : canale di vendita
	 * risposte : risposte al questionario (classe ElementoQuestionarioRisposteModel)
	 * eta      : eta del cliente per il calcolo del profilo di default
	 * titStud  : titolo di studio del cliente per il calcolo del profilo di default
	 * country  : sigla paese 3 caratteri usata per log
	 * username : sigla utente 10 caratteri usata per log
	 * pg       : persona giuridica
	 */
	private StringType  ndgDoss  = new StringType();
	private StringType  ndgTemp  = new StringType();
	private StringType  canVend  = new StringType();
	private ListType    risposte = new ListType();
	private IntegerType eta      = new IntegerType();
	private StringType  titStud  = new StringType();
	private StringType  country  = new StringType();
	private StringType  username = new StringType();
	private StringType  pg       = new StringType();
	
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
	public ListType getRisposte() {
		return risposte;
	}
	public void setRisposte(ListType risposte) {
		this.risposte = risposte;
	}
	public IntegerType getEta() {
		return eta;
	}
	public void setEta(IntegerType eta) {
		this.eta = eta;
	}
	public StringType getTitStud() {
		return titStud;
	}
	public void setTitStud(StringType titStud) {
		this.titStud = titStud;
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

