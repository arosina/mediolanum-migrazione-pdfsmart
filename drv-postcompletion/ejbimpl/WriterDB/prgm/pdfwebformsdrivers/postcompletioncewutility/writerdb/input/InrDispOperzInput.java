package prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input;

import com.atosorigin.wfem.command.*;
import com.atosorigin.wfem.types.*;
import com.atosorigin.wfem.util.Tools;

/*********************************************************************************/
/*********************************************************************************/
public class InrDispOperzInput extends CommandDataModel{

	private StringType 	codOperazione			= new StringType();
	private StringType 	codDisposizione			= new StringType();
	private StringType 	codAgente				= new StringType();
	private StringType 	codRete					= new StringType();
	private StringType 	codContratto			= new StringType();
	private StringType 	serverReplica			= new StringType();
	private StringType 	tipoOperazione			= new StringType();
	private StringType 	tipoStatoDisposizione	= new StringType();
	private StringType 	codPeriodoVersato	= new StringType();
	private StringType 	giorniScadenza		= new StringType();
	private StringType 	codPartzPg			= new StringType();
	private StringType 	durataVersamento	= new StringType();
	private StringType 	codUniDur			= new StringType();
	private IntegerType	eta					= new IntegerType();
	private StringType	codTipoQuotaCons	= new StringType();
	private StringType	codPercCons			= new StringType();
	private StringType	codProdCons			= new StringType();
	private StringType	flagIstat			= new StringType();
	private StringType	flagDistr			= new StringType();
	private DateType 	dataInizio			= new DateType();
	private DateType 	dataFine			= new DateType();
	private StringType	flagCambio			= new StringType();
	private StringType  flagAdesioneRevoca	= new StringType();


	public StringType getCodAgente() {
		return codAgente;
	}

	public StringType getCodContratto() {
		return codContratto;
	}

	public StringType getCodDisposizione() {
		return codDisposizione;
	}

	public StringType getCodOperazione() {
		return codOperazione;
	}

	public StringType getCodPartzPg() {
		return codPartzPg;
	}

	public StringType getCodPercCons() {
		return codPercCons;
	}

	public StringType getCodPeriodoVersato() {
		return codPeriodoVersato;
	}

	public StringType getCodProdCons() {
		return codProdCons;
	}

	public StringType getCodRete() {
		return codRete;
	}

	public StringType getCodTipoQuotaCons() {
		return codTipoQuotaCons;
	}

	public StringType getCodUniDur() {
		return codUniDur;
	}

	public DateType getDataFine() {
		return dataFine;
	}

	public DateType getDataInizio() {
		return dataInizio;
	}

	public StringType getDurataVersamento() {
		return durataVersamento;
	}

	public IntegerType getEta() {
		return eta;
	}

	public StringType getFlagDistr() {
		return flagDistr;
	}

	public StringType getFlagIstat() {
		return flagIstat;
	}

	public StringType getGiorniScadenza() {
		return giorniScadenza;
	}

	public StringType getServerReplica() {
		return serverReplica;
	}

	public StringType getTipoOperazione() {
		return tipoOperazione;
	}

	public StringType getTipoStatoDisposizione() {
		return tipoStatoDisposizione;
	}

	public void setCodAgente(StringType codAgente) {
		this.codAgente = codAgente;
	}

	public void setCodContratto(StringType codContratto) {
		this.codContratto = codContratto;
	}

	public void setCodDisposizione(StringType codDisposizione) {
		this.codDisposizione = codDisposizione;
	}

	public void setCodOperazione(StringType codOperazione) {
		this.codOperazione = codOperazione;
	}

	public void setCodPartzPg(StringType codPartzPg) {
		this.codPartzPg = codPartzPg;
	}

	public void setCodPercCons(StringType codPercCons) {
		this.codPercCons = codPercCons;
	}

	public void setCodPeriodoVersato(StringType codPeriodoVersato) {
		this.codPeriodoVersato = codPeriodoVersato;
	}

	public void setCodProdCons(StringType codProdCons) {
		this.codProdCons = codProdCons;
	}

	public void setCodRete(StringType codRete) {
		this.codRete = codRete;
	}

	public void setCodTipoQuotaCons(StringType codTipoQuotaCons) {
		this.codTipoQuotaCons = codTipoQuotaCons;
	}

	public void setCodUniDur(StringType codUniDur) {
		this.codUniDur = codUniDur;
	}

	public void setDataFine(DateType dataFine) {
		this.dataFine = dataFine;
	}

	public void setDataInizio(DateType dataInizio) {
		this.dataInizio = dataInizio;
	}

	public void setDurataVersamento(StringType durataVersamento) {
		this.durataVersamento = durataVersamento;
	}

	public void setEta(IntegerType eta) {
		this.eta = eta;
	}

	public void setFlagDistr(StringType flagDistr) {
		this.flagDistr = flagDistr;
	}

	public void setFlagIstat(StringType flagIstat) {
		this.flagIstat = flagIstat;
	}

	public void setGiorniScadenza(StringType giorniScadenza) {
		this.giorniScadenza = giorniScadenza;
	}

	public void setServerReplica(StringType serverReplica) {
		this.serverReplica = serverReplica;
	}

	public void setTipoOperazione(StringType tipoOperazione) {
		this.tipoOperazione = tipoOperazione;
	}

	public void setTipoStatoDisposizione(StringType tipoStatoDisposizione) {
		this.tipoStatoDisposizione = tipoStatoDisposizione;
	}

	public StringType getFlagCambio() {
		return flagCambio;
	}

	public void setFlagCambio(StringType flagCambio) {
		this.flagCambio = flagCambio;
	}
	

	public StringType getFlagAdesioneRevoca() {
		return flagAdesioneRevoca;
	}
	

	public void setFlagAdesioneRevoca(StringType flagAdesioneRevoca) {
		this.flagAdesioneRevoca = flagAdesioneRevoca;
	}

}
