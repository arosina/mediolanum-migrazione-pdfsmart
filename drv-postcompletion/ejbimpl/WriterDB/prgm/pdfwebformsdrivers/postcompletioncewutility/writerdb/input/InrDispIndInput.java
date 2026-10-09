package prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.StringType;

/*********************************************************************************/
/*********************************************************************************/
public class InrDispIndInput extends CommandDataModel{

	private StringType codDispInd			= new StringType();
	private StringType codRete				= new StringType(); 
	private StringType codAgente			= new StringType();
	private StringType codDisposizione		= new StringType();
	private StringType tipoStatoDisposizione= new StringType();
	private StringType codNazione			= new StringType();
	private StringType codProvincia			= new StringType();
	private StringType codComune			= new StringType();
	private StringType codCap				= new StringType();
	private StringType nominativo			= new StringType();
	private StringType presso				= new StringType();
	private StringType indirizzo			= new StringType();
	private DateType   dataVariazione		= new DateType();
	private StringType prefisso				= new StringType();
	private StringType telefono				= new StringType();
	private StringType codIndirizzo			= new StringType();
	private StringType serverReplica		= new StringType();
	private StringType toponimoIndirizzo	= new StringType();
	private StringType numeroCivico			= new StringType();
	private StringType comune			= new StringType();

	public StringType getCodAgente() {
		return codAgente;
	}

	public StringType getCodCap() {
		return codCap;
	}

	public StringType getCodComune() {
		return codComune;
	}

	public StringType getCodDispInd() {
		return codDispInd;
	}

	public StringType getCodDisposizione() {
		return codDisposizione;
	}

	public StringType getCodIndirizzo() {
		return codIndirizzo;
	}

	public StringType getCodNazione() {
		return codNazione;
	}

	public StringType getCodProvincia() {
		return codProvincia;
	}

	public StringType getCodRete() {
		return codRete;
	}

	public DateType getDataVariazione() {
		return dataVariazione;
	}

	public StringType getIndirizzo() {
		return indirizzo;
	}

	public StringType getNominativo() {
		return nominativo;
	}

	public StringType getPrefisso() {
		return prefisso;
	}

	public StringType getPresso() {
		return presso;
	}

	public StringType getServerReplica() {
		return serverReplica;
	}

	public StringType getTelefono() {
		return telefono;
	}

	public StringType getTipoStatoDisposizione() {
		return tipoStatoDisposizione;
	}

	public void setCodAgente(StringType codAgente) {
		this.codAgente = codAgente;
	}

	public void setCodCap(StringType codCap) {
		this.codCap = codCap;
	}

	public void setCodComune(StringType codComune) {
		this.codComune = codComune;
	}

	public void setCodDispInd(StringType codDispInd) {
		this.codDispInd = codDispInd;
	}

	public void setCodDisposizione(StringType codDisposizione) {
		this.codDisposizione = codDisposizione;
	}

	public void setCodIndirizzo(StringType codIndirizzo) {
		this.codIndirizzo = codIndirizzo;
	}

	public void setCodNazione(StringType codNazione) {
		this.codNazione = codNazione;
	}

	public void setCodProvincia(StringType codProvincia) {
		this.codProvincia = codProvincia;
	}

	public void setCodRete(StringType codRete) {
		this.codRete = codRete;
	}

	public void setDataVariazione(DateType dataVariazione) {
		this.dataVariazione = dataVariazione;
	}

	public void setIndirizzo(StringType indirizzo) {
		this.indirizzo = indirizzo;
	}

	public void setNominativo(StringType nominativo) {
		this.nominativo = nominativo;
	}

	public void setPrefisso(StringType prefisso) {
		this.prefisso = prefisso;
	}

	public void setPresso(StringType presso) {
		this.presso = presso;
	}

	public void setServerReplica(StringType serverReplica) {
		this.serverReplica = serverReplica;
	}

	public void setTelefono(StringType telefono) {
		this.telefono = telefono;
	}

	public void setTipoStatoDisposizione(StringType tipoStatoDisposizione) {
		this.tipoStatoDisposizione = tipoStatoDisposizione;
	}

	public StringType getNumeroCivico() {
		return numeroCivico;
	}

	public void setNumeroCivico(StringType numeroCivico) {
		this.numeroCivico = numeroCivico;
	}

	public StringType getToponimoIndirizzo() {
		return toponimoIndirizzo;
	}

	public void setToponimoIndirizzo(StringType toponimoIndirizzo) {
		this.toponimoIndirizzo = toponimoIndirizzo;
	}

	public StringType getComune() {
		return comune;
	}

	public void setComune(StringType comune) {
		this.comune = comune;
	}

}
