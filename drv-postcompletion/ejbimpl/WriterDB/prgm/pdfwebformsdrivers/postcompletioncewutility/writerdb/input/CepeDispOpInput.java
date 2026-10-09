package prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;

public class CepeDispOpInput extends CommandDataModel{

	private StringType  	codDisposizione					= new StringType();
	private StringType  	codAgente						= new StringType();
	private StringType  	codRete							= new StringType();
	private StringType  	tipoStatoDisposizione			= new StringType();
	private StringType  	serverReplica					= new StringType();
	private StringType 		codProd							= new StringType();
	
	//Dati offerta 
	private IntegerType  	idOp							= new IntegerType();
	private TimestampType 	dataInserimento					= new TimestampType();
	private StringType  	sceltaDossier					= new StringType();
	private StringType  	numDossier						= new StringType();
	private StringType  	numSottorubrica					= new StringType();
	private StringType 		codMediolanumIntSottorubrica	= new StringType();
	private StringType  	numeroConto						= new StringType();
	private DoubleType  	importoRichiesto				= new DoubleType();
	private DoubleType  	importoPrenotato				= new DoubleType();
	private DoubleType  	numTitoli						= new DoubleType();
	private DoubleType  	controValore					= new DoubleType();
	
	public StringType getCodDisposizione() {
		return codDisposizione;
	}
	public void setCodDisposizione(StringType codDisposizione) {
		this.codDisposizione = codDisposizione;
	}
	public StringType getCodAgente() {
		return codAgente;
	}
	public void setCodAgente(StringType codAgente) {
		this.codAgente = codAgente;
	}
	public StringType getCodRete() {
		return codRete;
	}
	public void setCodRete(StringType codRete) {
		this.codRete = codRete;
	}
	public StringType getTipoStatoDisposizione() {
		return tipoStatoDisposizione;
	}
	public void setTipoStatoDisposizione(StringType tipoStatoDisposizione) {
		this.tipoStatoDisposizione = tipoStatoDisposizione;
	}
	public StringType getServerReplica() {
		return serverReplica;
	}
	public void setServerReplica(StringType serverReplica) {
		this.serverReplica = serverReplica;
	}
	public IntegerType getIdOp() {
		return idOp;
	}
	public void setIdOp(IntegerType idOp) {
		this.idOp = idOp;
	}
	public TimestampType getDataInserimento() {
		return dataInserimento;
	}
	public void setDataInserimento(TimestampType dataInserimento) {
		this.dataInserimento = dataInserimento;
	}
	public StringType getSceltaDossier() {
		return sceltaDossier;
	}
	public void setSceltaDossier(StringType sceltaDossier) {
		this.sceltaDossier = sceltaDossier;
	}
	public StringType getNumDossier() {
		return numDossier;
	}
	public void setNumDossier(StringType numDossier) {
		this.numDossier = numDossier;
	}
	public StringType getNumSottorubrica() {
		return numSottorubrica;
	}
	public void setNumSottorubrica(StringType numSottorubrica) {
		this.numSottorubrica = numSottorubrica;
	}
	public StringType getCodMediolanumIntSottorubrica() {
		return codMediolanumIntSottorubrica;
	}
	public void setCodMediolanumIntSottorubrica(StringType codMediolanumIntSottorubrica) {
		this.codMediolanumIntSottorubrica = codMediolanumIntSottorubrica;
	}
	public StringType getNumeroConto() {
		return numeroConto;
	}
	public void setNumeroConto(StringType numeroConto) {
		this.numeroConto = numeroConto;
	}
	public DoubleType getImportoRichiesto() {
		return importoRichiesto;
	}
	public void setImportoRichiesto(DoubleType importoRichiesto) {
		this.importoRichiesto = importoRichiesto;
	}
	public DoubleType getImportoPrenotato() {
		return importoPrenotato;
	}
	public void setImportoPrenotato(DoubleType importoPrenotato) {
		this.importoPrenotato = importoPrenotato;
	}
	public DoubleType getNumTitoli() {
		return numTitoli;
	}
	public void setNumTitoli(DoubleType numTitoli) {
		this.numTitoli = numTitoli;
	}
	public DoubleType getControValore() {
		return controValore;
	}
	public void setControValore(DoubleType controValore) {
		this.controValore = controValore;
	}
	public StringType getCodProd() {
		return codProd;
	}
	public void setCodProd(StringType codProd) {
		this.codProd = codProd;
	}
}
