package prgm.pdfwebformspolizzeutil.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.StringType;

public class BeneficiarioModel extends CommandDataModel {

	private StringType 	tipoRicerca = new StringType();
	private StringType 	codiceAgente = new StringType(); 

	private StringType 	codiceCliente = new StringType();
	private StringType 	codiceFiscale = new StringType();
	private StringType 	nome = new StringType();
	private StringType 	cognome = new StringType();
	private StringType 	sesso = new StringType();
	
	private StringType 	ragioneSociale = new StringType();
	private StringType 	numeroIscrizioneCCIAA = new StringType();
	private DateType 	dataIscrizioneCCIAA = new DateType();
	private StringType 	provinciaIscrizioneCCIAA = new StringType();
	private StringType 	codiceClienteTitolare1 = new StringType();
	private StringType 	codiceClienteTitolare2 = new StringType();


	private DateType 	dataNascita = new DateType();
	private StringType 	comuneNascita = new StringType();
	private StringType 	provinciaComuneNascita = new StringType();
	private StringType 	nazioneComuneNascita = new StringType();
	
	private StringType 	codToponimoIndirizzo = new StringType();
	private StringType 	toponimoIndirizzo = new StringType();
	private StringType 	indirizzo = new StringType();
	private StringType 	numeroCivicoIndirizzo = new StringType();
	private StringType 	capComune = new StringType();
	private StringType 	comune = new StringType();
	private StringType 	provinciaComune = new StringType();
	private StringType 	nazioneComune = new StringType();
	

	private StringType 	email = new StringType();
	
	private StringType 	prefissoInternazionaleTelefono = new StringType();
	private StringType 	prefissoTelefono = new StringType();
	private StringType 	telefono = new StringType();
	private StringType 	tipoTelefono = new StringType();
	
	private StringType codAgeImpersonato = new StringType();
    private StringType codRuoloImpersonato =new StringType();


	public StringType getTipoRicerca() {
		return tipoRicerca;
	}


	public void setTipoRicerca(StringType tipoRicerca) {
		this.tipoRicerca = tipoRicerca;
	}


	public StringType getCodiceCliente() {
		return codiceCliente;
	}


	public void setCodiceCliente(StringType codiceCliente) {
		this.codiceCliente = codiceCliente;
	}


	public StringType getCodiceFiscale() {
		return codiceFiscale;
	}


	public void setCodiceFiscale(StringType codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}


	public StringType getNome() {
		return nome;
	}


	public void setNome(StringType nome) {
		this.nome = nome;
	}


	public StringType getCognome() {
		return cognome;
	}


	public void setCognome(StringType cognome) {
		this.cognome = cognome;
	}


	public StringType getSesso() {
		return sesso;
	}


	public void setSesso(StringType sesso) {
		this.sesso = sesso;
	}


	public StringType getRagioneSociale() {
		return ragioneSociale;
	}


	public void setRagioneSociale(StringType ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}


	public StringType getNumeroIscrizioneCCIAA() {
		return numeroIscrizioneCCIAA;
	}


	public void setNumeroIscrizioneCCIAA(StringType numeroIscrizioneCCIAA) {
		this.numeroIscrizioneCCIAA = numeroIscrizioneCCIAA;
	}


	public DateType getDataIscrizioneCCIAA() {
		return dataIscrizioneCCIAA;
	}


	public void setDataIscrizioneCCIAA(DateType dataIscrizioneCCIAA) {
		this.dataIscrizioneCCIAA = dataIscrizioneCCIAA;
	}


	public StringType getProvinciaIscrizioneCCIAA() {
		return provinciaIscrizioneCCIAA;
	}


	public void setProvinciaIscrizioneCCIAA(StringType provinciaIscrizioneCCIAA) {
		this.provinciaIscrizioneCCIAA = provinciaIscrizioneCCIAA;
	}


	public DateType getDataNascita() {
		return dataNascita;
	}


	public void setDataNascita(DateType dataNascita) {
		this.dataNascita = dataNascita;
	}


	public StringType getComuneNascita() {
		return comuneNascita;
	}


	public void setComuneNascita(StringType comuneNascita) {
		this.comuneNascita = comuneNascita;
	}


	public StringType getProvinciaComuneNascita() {
		return provinciaComuneNascita;
	}


	public void setProvinciaComuneNascita(StringType provinciaComuneNascita) {
		this.provinciaComuneNascita = provinciaComuneNascita;
	}


	public StringType getNazioneComuneNascita() {
		return nazioneComuneNascita;
	}


	public void setNazioneComuneNascita(StringType nazioneComuneNascita) {
		this.nazioneComuneNascita = nazioneComuneNascita;
	}


	public StringType getToponimoIndirizzo() {
		return toponimoIndirizzo;
	}


	public void setToponimoIndirizzo(StringType toponimoIndirizzo) {
		this.toponimoIndirizzo = toponimoIndirizzo;
	}


	public StringType getIndirizzo() {
		return indirizzo;
	}


	public void setIndirizzo(StringType indirizzo) {
		this.indirizzo = indirizzo;
	}


	public StringType getNumeroCivicoIndirizzo() {
		return numeroCivicoIndirizzo;
	}


	public void setNumeroCivicoIndirizzo(StringType numeroCivicoIndirizzo) {
		this.numeroCivicoIndirizzo = numeroCivicoIndirizzo;
	}


	public StringType getCapComune() {
		return capComune;
	}


	public void setCapComune(StringType capComune) {
		this.capComune = capComune;
	}


	public StringType getComune() {
		return comune;
	}


	public void setComune(StringType comune) {
		this.comune = comune;
	}


	public StringType getProvinciaComune() {
		return provinciaComune;
	}


	public void setProvinciaComune(StringType provinciaComune) {
		this.provinciaComune = provinciaComune;
	}


	public StringType getNazioneComune() {
		return nazioneComune;
	}


	public void setNazioneComune(StringType nazioneComune) {
		this.nazioneComune = nazioneComune;
	}


	public StringType getEmail() {
		return email;
	}


	public void setEmail(StringType email) {
		this.email = email;
	}


	public StringType getCodiceAgente() {
		return codiceAgente;
	}


	public void setCodiceAgente(StringType codiceAgente) {
		this.codiceAgente = codiceAgente;
	}


	public StringType getPrefissoInternazionaleTelefono() {
		return prefissoInternazionaleTelefono;
	}


	public void setPrefissoInternazionaleTelefono(StringType prefissoInternazionaleTelefono) {
		this.prefissoInternazionaleTelefono = prefissoInternazionaleTelefono;
	}


	public StringType getPrefissoTelefono() {
		return prefissoTelefono;
	}


	public void setPrefissoTelefono(StringType prefissoTelefono) {
		this.prefissoTelefono = prefissoTelefono;
	}


	public StringType getTelefono() {
		return telefono;
	}


	public void setTelefono(StringType telefono) {
		this.telefono = telefono;
	}


	public StringType getTipoTelefono() {
		return tipoTelefono;
	}


	public void setTipoTelefono(StringType tipoTelefono) {
		this.tipoTelefono = tipoTelefono;
	}


	public StringType getCodiceClienteTitolare1() {
		return codiceClienteTitolare1;
	}


	public void setCodiceClienteTitolare1(StringType codiceClienteTitolare1) {
		this.codiceClienteTitolare1 = codiceClienteTitolare1;
	}


	public StringType getCodiceClienteTitolare2() {
		return codiceClienteTitolare2;
	}


	public void setCodiceClienteTitolare2(StringType codiceClienteTitolare2) {
		this.codiceClienteTitolare2 = codiceClienteTitolare2;
	}


	public StringType getCodAgeImpersonato() {
		return codAgeImpersonato;
	}


	public StringType getCodRuoloImpersonato() {
		return codRuoloImpersonato;
	}


	public void setCodAgeImpersonato(StringType codAgeImpersonato) {
		this.codAgeImpersonato = codAgeImpersonato;
	}


	public void setCodRuoloImpersonato(StringType codRuoloImpersonato) {
		this.codRuoloImpersonato = codRuoloImpersonato;
	}


	public StringType getCodToponimoIndirizzo() {
		return codToponimoIndirizzo;
	}


	public void setCodToponimoIndirizzo(StringType codToponimoIndirizzo) {
		this.codToponimoIndirizzo = codToponimoIndirizzo;
	}

}
