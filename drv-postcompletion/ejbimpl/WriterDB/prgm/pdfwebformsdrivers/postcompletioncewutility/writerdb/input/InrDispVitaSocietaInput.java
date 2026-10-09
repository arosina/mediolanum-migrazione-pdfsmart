package prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/*********************************************************************************/
/*********************************************************************************/
public class InrDispVitaSocietaInput extends CommandDataModel {

    private StringType codDisposizione			= new StringType();
    private StringType codRete					= new StringType();
    private StringType codAgente				= new StringType();
    private StringType tipoSocieta				= new StringType();    
    private StringType tipoStatoDisposizione	= new StringType();
    private StringType serverReplica			= new StringType();
    private StringType codClienteMediolanum		= new StringType();
    private StringType codPotenziale			= new StringType();
    private StringType denominazioneSoc			= new StringType();
    private StringType partitaIva				= new StringType();
    private StringType toponimo					= new StringType();
    private StringType indirizzo				= new StringType();
    private StringType numeroCivico				= new StringType();
    private StringType cap						= new StringType();
    private StringType comune					= new StringType();
    private StringType localita					= new StringType();
    private StringType provincia				= new StringType();
    private StringType nazione					= new StringType();
    private StringType prefisso					= new StringType();
    private StringType telefono					= new StringType();
	
    public StringType getCap() {
		return cap;
	}
	public void setCap(StringType cap) {
		this.cap = cap;
	}
	public StringType getCodAgente() {
		return codAgente;
	}
	public void setCodAgente(StringType codAgente) {
		this.codAgente = codAgente;
	}
	public StringType getCodClienteMediolanum() {
		return codClienteMediolanum;
	}
	public void setCodClienteMediolanum(StringType codClienteMediolanum) {
		this.codClienteMediolanum = codClienteMediolanum;
	}
	public StringType getCodDisposizione() {
		return codDisposizione;
	}
	public void setCodDisposizione(StringType codDisposizione) {
		this.codDisposizione = codDisposizione;
	}
	public StringType getCodPotenziale() {
		return codPotenziale;
	}
	public void setCodPotenziale(StringType codPotenziale) {
		this.codPotenziale = codPotenziale;
	}
	public StringType getCodRete() {
		return codRete;
	}
	public void setCodRete(StringType codRete) {
		this.codRete = codRete;
	}
	public StringType getComune() {
		return comune;
	}
	public void setComune(StringType comune) {
		this.comune = comune;
	}
	public StringType getDenominazioneSoc() {
		return denominazioneSoc;
	}
	public void setDenominazioneSoc(StringType denominazioneSoc) {
		this.denominazioneSoc = denominazioneSoc;
	}
	public StringType getIndirizzo() {
		return indirizzo;
	}
	public void setIndirizzo(StringType indirizzo) {
		this.indirizzo = indirizzo;
	}
	public StringType getLocalita() {
		return localita;
	}
	public void setLocalita(StringType localita) {
		this.localita = localita;
	}
	public StringType getNazione() {
		return nazione;
	}
	public void setNazione(StringType nazione) {
		this.nazione = nazione;
	}
	public StringType getNumeroCivico() {
		return numeroCivico;
	}
	public void setNumeroCivico(StringType numeroCivico) {
		this.numeroCivico = numeroCivico;
	}
	public StringType getPartitaIva() {
		return partitaIva;
	}
	public void setPartitaIva(StringType partitaIva) {
		this.partitaIva = partitaIva;
	}
	public StringType getPrefisso() {
		return prefisso;
	}
	public void setPrefisso(StringType prefisso) {
		this.prefisso = prefisso;
	}
	public StringType getProvincia() {
		return provincia;
	}
	public void setProvincia(StringType provincia) {
		this.provincia = provincia;
	}
	public StringType getServerReplica() {
		return serverReplica;
	}
	public void setServerReplica(StringType serverReplica) {
		this.serverReplica = serverReplica;
	}
	public StringType getTelefono() {
		return telefono;
	}
	public void setTelefono(StringType telefono) {
		this.telefono = telefono;
	}
	public StringType getTipoSocieta() {
		return tipoSocieta;
	}
	public void setTipoSocieta(StringType tipoSocieta) {
		this.tipoSocieta = tipoSocieta;
	}
	public StringType getTipoStatoDisposizione() {
		return tipoStatoDisposizione;
	}
	public void setTipoStatoDisposizione(StringType tipoStatoDisposizione) {
		this.tipoStatoDisposizione = tipoStatoDisposizione;
	}
	public StringType getToponimo() {
		return toponimo;
	}
	public void setToponimo(StringType toponimo) {
		this.toponimo = toponimo;
	}
    
}
