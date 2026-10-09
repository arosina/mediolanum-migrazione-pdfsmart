package prgm.ita.anagraficaclienti.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class DatiApplicativiModel extends CommandDataModel {
	
	private int tipoStampa;
	private boolean refreshable;
	private boolean censimentoDitta;
	
	// Serve per gestire, nei censimenti ditte, la richiesta all'utente se
	// il cliente titolare è censito oppure no
	private boolean sceltaTipologiaCensimentoDittaEffettuata = false;

	private StringType  	nomeTabCorrente = new StringType("residenza");
	private StringType  	nomeFunzione = new StringType();
	private StringType  	nomeTabella = new StringType();
	private StringType  	nomeTabellaOracle = new StringType();
	private StringType  	tipoElemento = new StringType();
	private StringType  	flagClienteSegnalato = new StringType();

	private StringType  	codiceFiscaleForzato = new StringType();
	
	private ClienteModel	clienteOriginale = null;	// Dati del cliente così come letto dai dati consolidati
	private ClienteModel	clienteCaricato = null;		// Dati del cliente attualmente presenti a db, comprese le eventuali variazioni
	
	private boolean			variazione = false;
	
	private String 			messaggioCentrale = "";
	private String 			erroreCentrale = "";
	
	private boolean			motivazionePepNonCongruente = false;
	private boolean 		showAlertAddendumAVR = false;
	private boolean 		showCorniceCentraleBlu = false;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void appendMessaggioCentrale(String msg){
		messaggioCentrale = messaggioCentrale.concat(msg);
	}
	
	public StringType getNomeFunzione() {
		return nomeFunzione;
	}

	public void setNomeFunzione(StringType nomeFunzione) {
		this.nomeFunzione = nomeFunzione;
	}

	public StringType getNomeTabCorrente() {
		return nomeTabCorrente;
	}

	public void setNomeTabCorrente(StringType nomeTabCorrente) {
		this.nomeTabCorrente = nomeTabCorrente;
	}

	public StringType getNomeTabella() {
		return nomeTabella;
	}

	public void setNomeTabella(StringType nomeTabella) {
		this.nomeTabella = nomeTabella;
	}

	public StringType getTipoElemento() {
		return tipoElemento;
	}

	public void setTipoElemento(StringType tipoElemento) {
		this.tipoElemento = tipoElemento;
	}

	public StringType getCodiceFiscaleForzato() {
		return codiceFiscaleForzato;
	}

	public void setCodiceFiscaleForzato(StringType codiceFiscaleForzato) {
		this.codiceFiscaleForzato = codiceFiscaleForzato;
	}

	public int getTipoStampa() {
		return tipoStampa;
	}

	public void setTipoStampa(int tipoStampa) {
		this.tipoStampa = tipoStampa;
	}

	public boolean isRefreshable() {
		return refreshable;
	}

	public void setRefreshable(boolean refreshable) {
		this.refreshable = refreshable;
	}

	public ClienteModel getClienteOriginale() {
		return clienteOriginale;
	}

	public void setClienteOriginale(ClienteModel clienteOriginale) {
		this.clienteOriginale = clienteOriginale;
	}

	public boolean isVariazione() {
		return variazione;
	}

	public void setVariazione(boolean variazione) {
		this.variazione = variazione;
	}

	public boolean isCensimentoDitta() {
		return censimentoDitta;
	}

	public void setCensimentoDitta(boolean censimentoDitta) {
		this.censimentoDitta = censimentoDitta;
	}

	public boolean isSceltaTipologiaCensimentoDittaEffettuata() {
		return sceltaTipologiaCensimentoDittaEffettuata;
	}

	public void setSceltaTipologiaCensimentoDittaEffettuata(
			boolean sceltaTipologiaCensimentoDittaEffettuata) {
		this.sceltaTipologiaCensimentoDittaEffettuata = sceltaTipologiaCensimentoDittaEffettuata;
	}

	public String getMessaggioCentrale() {
		return messaggioCentrale;
	}

	public void setMessaggioCentrale(String messaggioCentrale) {
		this.messaggioCentrale = messaggioCentrale;
	}

	public StringType getFlagClienteSegnalato() {
		return flagClienteSegnalato;
	}

	public void setFlagClienteSegnalato(StringType flagClienteSegnalato) {
		this.flagClienteSegnalato = flagClienteSegnalato;
	}

	public ClienteModel getClienteCaricato() {
		return clienteCaricato;
	}

	public void setClienteCaricato(ClienteModel clienteCaricato) {
		this.clienteCaricato = clienteCaricato;
	}

	public boolean isMotivazionePepNonCongruente() {
		return motivazionePepNonCongruente;
	}

	public void setMotivazionePepNonCongruente(boolean motivazionePepNonCongruente) {
		this.motivazionePepNonCongruente = motivazionePepNonCongruente;
	}

	public String getErroreCentrale() {
		return erroreCentrale;
	}

	public void setErroreCentrale(String erroreCentrale) {
		this.erroreCentrale = erroreCentrale;
	}

	public StringType getNomeTabellaOracle() {
		return nomeTabellaOracle;
	}

	public void setNomeTabellaOracle(StringType nomeTabellaOracle) {
		this.nomeTabellaOracle = nomeTabellaOracle;
	}

	public boolean isShowAlertAddendumAVR() {
		return showAlertAddendumAVR;
	}

	public void setShowAlertAddendumAVR(boolean showAlertAddendumAVR) {
		this.showAlertAddendumAVR = showAlertAddendumAVR;
	}

	public boolean isShowCorniceCentraleBlu() {
		return showCorniceCentraleBlu;
	}

	public void setShowCorniceCentraleBlu(boolean showCorniceCentraleBlu) {
		this.showCorniceCentraleBlu = showCorniceCentraleBlu;
	}

}
