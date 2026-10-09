package prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/*********************************************************************************/
/*********************************************************************************/
public class InrDispContrInput extends CommandDataModel{ 

	private StringType codContratto			= new StringType();
	private StringType codDisposizione		= new StringType();
	private StringType codAgente			= new StringType();
	private StringType codRete				= new StringType();
	private StringType tipoStatoDisposizione= new StringType();
	private StringType serverReplica		= new StringType();
	private StringType codProd				= new StringType();
	private StringType tipoContratto		= new StringType();
	private StringType codClasse			= new StringType();
	private StringType codTipoQuota			= new StringType();

	public StringType getCodAgente() {
		return codAgente;
	}

	public StringType getCodClasse() {
		return codClasse;
	}

	public StringType getCodContratto() {
		return codContratto;
	}

	public StringType getCodDisposizione() {
		return codDisposizione;
	}

	public StringType getCodProd() {
		return codProd;
	}

	public StringType getCodRete() {
		return codRete;
	}

	public StringType getCodTipoQuota() {
		return codTipoQuota;
	}

	public StringType getServerReplica() {
		return serverReplica;
	}

	public StringType getTipoContratto() {
		return tipoContratto;
	}

	public StringType getTipoStatoDisposizione() {
		return tipoStatoDisposizione;
	}

	public void setCodAgente(StringType codAgente) {
		this.codAgente = codAgente;
	}

	public void setCodClasse(StringType codClasse) {
		this.codClasse = codClasse;
	}

	public void setCodContratto(StringType codContratto) {
		this.codContratto = codContratto;
	}

	public void setCodDisposizione(StringType codDisposizione) {
		this.codDisposizione = codDisposizione;
	}

	public void setCodProd(StringType codProd) {
		this.codProd = codProd;
	}

	public void setCodRete(StringType codRete) {
		this.codRete = codRete;
	}

	public void setCodTipoQuota(StringType codTipoQuota) {
		this.codTipoQuota = codTipoQuota;
	}

	public void setServerReplica(StringType serverReplica) {
		this.serverReplica = serverReplica;
	}

	public void setTipoContratto(StringType tipoContratto) {
		this.tipoContratto = tipoContratto;
	}

	public void setTipoStatoDisposizione(StringType tipoStatoDisposizione) {
		this.tipoStatoDisposizione = tipoStatoDisposizione;
	}

}
