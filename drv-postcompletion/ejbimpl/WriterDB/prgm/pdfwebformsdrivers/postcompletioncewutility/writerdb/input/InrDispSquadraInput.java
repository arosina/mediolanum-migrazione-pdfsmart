package prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input;

import com.atosorigin.wfem.command.*;
import com.atosorigin.wfem.types.*;
import com.atosorigin.wfem.util.Tools;

/*********************************************************************************/
/*********************************************************************************/
public class InrDispSquadraInput extends CommandDataModel{
	
	private StringType codDisposizione			= new StringType();
	private StringType codRete					= new StringType();
	private StringType codClienteMediolanum		= new StringType();
	private StringType codAgente				= new StringType();
	private StringType codRuoloCliProd			= new StringType();
	private StringType tipoStatoDisposizione	= new StringType();
	private DateType 	dataInizio				= new DateType();
	private DateType 	dataFine				= new DateType();
	private DateType 	dataVariazione			= new DateType();
	private StringType codProgressivo			= new StringType();
	private StringType serverReplica			= new StringType();
	private StringType codPotenziale			= new StringType(); 

	public StringType getCodAgente() {
		return codAgente;
	}

	public StringType getCodClienteMediolanum() {
		return codClienteMediolanum;
	}

	public StringType getCodDisposizione() {
		return codDisposizione;
	}

	public StringType getCodPotenziale() {
		return codPotenziale;
	}

	public StringType getCodProgressivo() {
		return codProgressivo;
	}

	public StringType getCodRete() {
		return codRete;
	}

	public StringType getCodRuoloCliProd() {
		return codRuoloCliProd;
	}

	public DateType getDataFine() {
		return dataFine;
	}

	public DateType getDataInizio() {
		return dataInizio;
	}

	public DateType getDataVariazione() {
		return dataVariazione;
	}

	public StringType getServerReplica() {
		return serverReplica;
	}

	public StringType getTipoStatoDisposizione() {
		return tipoStatoDisposizione;
	}

	public void setCodAgente(StringType codAgente) {
		this.codAgente = codAgente;
	}

	public void setCodClienteMediolanum(StringType codClienteMediolanum) {
		this.codClienteMediolanum = codClienteMediolanum;
	}

	public void setCodDisposizione(StringType codDisposizione) {
		this.codDisposizione = codDisposizione;
	}

	public void setCodPotenziale(StringType codPotenziale) {
		this.codPotenziale = codPotenziale;
	}

	public void setCodProgressivo(StringType codProgressivo) {
		this.codProgressivo = codProgressivo;
	}

	public void setCodRete(StringType codRete) {
		this.codRete = codRete;
	}

	public void setCodRuoloCliProd(StringType codRuoloCliProd) {
		this.codRuoloCliProd = codRuoloCliProd;
	}

	public void setDataFine(DateType dataFine) {
		this.dataFine = dataFine;
	}

	public void setDataInizio(DateType dataInizio) {
		this.dataInizio = dataInizio;
	}

	public void setDataVariazione(DateType dataVariazione) {
		this.dataVariazione = dataVariazione;
	}

	public void setServerReplica(StringType serverReplica) {
		this.serverReplica = serverReplica;
	}

	public void setTipoStatoDisposizione(StringType tipoStatoDisposizione) {
		this.tipoStatoDisposizione = tipoStatoDisposizione;
	}

}
