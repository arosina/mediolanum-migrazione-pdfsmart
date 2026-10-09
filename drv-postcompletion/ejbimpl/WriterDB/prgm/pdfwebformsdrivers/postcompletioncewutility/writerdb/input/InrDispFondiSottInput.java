package prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.StringType;

public class InrDispFondiSottInput extends CommandDataModel {

	private StringType  codDisposizione			= new StringType();
	private StringType  codAgente				= new StringType();
	private StringType  codRete					= new StringType();
	private StringType  serverReplica			= new StringType();
	private StringType  tipoVersamento			= new StringType();
	private StringType  codComparto				= new StringType();
	private StringType  descrComparto			= new StringType();
	private StringType  codServizio				= new StringType();
	private DoubleType  importo					= new DoubleType();
	private StringType  cadenzaLoi				= new StringType();
	private DoubleType  importoLoi				= new DoubleType();
	private DoubleType  percentualeConsolida	= new DoubleType();
	private StringType  ccConsolidamento		= new StringType();
	private StringType  codFondoCons	 		= new StringType();
	private StringType  numeroMandatoCons		= new StringType();
	private StringType  codDispMezzoPG			= new StringType();
	private DoubleType  importoAlimentazione	= new DoubleType();
	private DoubleType  importoRata				= new DoubleType();
	private DoubleType  numeroRate				= new DoubleType();
	private StringType  flagISTAT				= new StringType();
	private StringType  flagRaddoppio 			= new StringType();
	private StringType  flagDistribuzione		= new StringType();

	private StringType  flagServizio			= new StringType();
	private DoubleType  numeroQuote				= new DoubleType();
	private StringType  tipoRimborso			= new StringType();

	private StringType  tipoCertificato			= new StringType();
	private StringType  certificatoPresso		= new StringType();
	private StringType  emissioneCertABI		= new StringType();
	private StringType  emissioneCertCAB		= new StringType();
	private StringType  emissioneCertIndirizzo	= new StringType();

	private StringType  tipoSottoscrizione		= new StringType();
	private StringType  revocaRID				= new StringType();


	public StringType getCadenzaLoi() {
		return cadenzaLoi;
	}

	public void setCadenzaLoi(StringType cadenzaLoi) {
		this.cadenzaLoi = cadenzaLoi;
	}

	public StringType getCodAgente() {
		return codAgente;
	}

	public void setCodAgente(StringType codAgente) {
		this.codAgente = codAgente;
	}

	public StringType getCodComparto() {
		return codComparto;
	}

	public void setCodComparto(StringType codComparto) {
		this.codComparto = codComparto;
	}

	public StringType getCodDisposizione() {
		return codDisposizione;
	}

	public void setCodDisposizione(StringType codDisposizione) {
		this.codDisposizione = codDisposizione;
	}

	public StringType getCodRete() {
		return codRete;
	}

	public void setCodRete(StringType codRete) {
		this.codRete = codRete;
	}

	public StringType getCodServizio() {
		return codServizio;
	}

	public void setCodServizio(StringType codServizio) {
		this.codServizio = codServizio;
	}

	public DoubleType getImporto() {
		return importo;
	}

	public void setImporto(DoubleType importo) {
		this.importo = importo;
	}

	public StringType getServerReplica() {
		return serverReplica;
	}

	public void setServerReplica(StringType serverReplica) {
		this.serverReplica = serverReplica;
	}

	public StringType getTipoVersamento() {
		return tipoVersamento;
	}

	public void setTipoVersamento(StringType tipoVersamento) {
		this.tipoVersamento = tipoVersamento;
	}

	public DoubleType getImportoLoi() {
		return importoLoi;
	}

	public void setImportoLoi(DoubleType importoLoi) {
		this.importoLoi = importoLoi;
	}

	public StringType getCcConsolidamento() {
		return ccConsolidamento;
	}

	public void setCcConsolidamento(StringType ccConsolidamento) {
		this.ccConsolidamento = ccConsolidamento;
	}

	public StringType getCodDispMezzoPG() {
		return codDispMezzoPG;
	}

	public void setCodDispMezzoPG(StringType codDispMezzoPG) {
		this.codDispMezzoPG = codDispMezzoPG;
	}

	public DoubleType getImportoAlimentazione() {
		return importoAlimentazione;
	}

	public void setImportoAlimentazione(DoubleType importoAlimentazione) {
		this.importoAlimentazione = importoAlimentazione;
	}

	public DoubleType getPercentualeConsolida() {
		return percentualeConsolida;
	}

	public void setPercentualeConsolida(DoubleType percentualeConsolida) {
		this.percentualeConsolida = percentualeConsolida;
	}

	public DoubleType getImportoRata() {
		return importoRata;
	}

	public void setImportoRata(DoubleType importoRata) {
		this.importoRata = importoRata;
	}

	public DoubleType getNumeroRate() {
		return numeroRate;
	}

	public void setNumeroRate(DoubleType numeroRate) {
		this.numeroRate = numeroRate;
	}

	public StringType getFlagISTAT() {
		return flagISTAT;
	}

	public void setFlagISTAT(StringType flagISTAT) {
		this.flagISTAT = flagISTAT;
	}

	public StringType getFlagRaddoppio() {
		return flagRaddoppio;
	}

	public void setFlagRaddoppio(StringType flagRaddoppio) {
		this.flagRaddoppio = flagRaddoppio;
	}

	public StringType getCodFondoCons() {
		return codFondoCons;
	}

	public void setCodFondoCons(StringType codFondoCons) {
		this.codFondoCons = codFondoCons;
	}

	public StringType getNumeroMandatoCons() {
		return numeroMandatoCons;
	}

	public void setNumeroMandatoCons(StringType numeroMandatoCons) {
		this.numeroMandatoCons = numeroMandatoCons;
	}

	public StringType getFlagDistribuzione() {
		return flagDistribuzione;
	}

	public void setFlagDistribuzione(StringType flagDistribuzione) {
		this.flagDistribuzione = flagDistribuzione;
	}

	public StringType getDescrComparto() {
		return descrComparto;
	}

	public void setDescrComparto(StringType descrComparto) {
		this.descrComparto = descrComparto;
	}

	public StringType getEmissioneCertABI() {
		return emissioneCertABI;
	}

	public StringType getCertificatoPresso() {
		return certificatoPresso;
	}

	public void setCertificatoPresso(StringType certificatoPresso) {
		this.certificatoPresso = certificatoPresso;
	}

	public void setEmissioneCertABI(StringType emissioneCertABI) {
		this.emissioneCertABI = emissioneCertABI;
	}

	public StringType getEmissioneCertCAB() {
		return emissioneCertCAB;
	}

	public void setEmissioneCertCAB(StringType emissioneCertCAB) {
		this.emissioneCertCAB = emissioneCertCAB;
	}

	public StringType getEmissioneCertIndirizzo() {
		return emissioneCertIndirizzo;
	}

	public void setEmissioneCertIndirizzo(StringType emissioneCertIndirizzo) {
		this.emissioneCertIndirizzo = emissioneCertIndirizzo;
	}

	public StringType getTipoCertificato() {
		return tipoCertificato;
	}

	public void setTipoCertificato(StringType tipoCertificato) {
		this.tipoCertificato = tipoCertificato;
	}

	public StringType getTipoSottoscrizione() {
		return tipoSottoscrizione;
	}

	public void setTipoSottoscrizione(StringType tipoSottoscrizione) {
		this.tipoSottoscrizione = tipoSottoscrizione;
	}

	public StringType getFlagServizio() {
		return flagServizio;
	}

	public void setFlagServizio(StringType flagServizio) {
		this.flagServizio = flagServizio;
	}

	public DoubleType getNumeroQuote() {
		return numeroQuote;
	}

	public void setNumeroQuote(DoubleType numeroQuote) {
		this.numeroQuote = numeroQuote;
	}

	public StringType getTipoRimborso() {
		return tipoRimborso;
	}

	public void setTipoRimborso(StringType tipoRimborso) {
		this.tipoRimborso = tipoRimborso;
	}

	public StringType getRevocaRID() {
		return revocaRID;
	}

	public void setRevocaRID(StringType revocaRID) {
		this.revocaRID = revocaRID;
	}



}
