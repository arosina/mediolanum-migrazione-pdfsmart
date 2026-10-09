package prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

public class InrDispInput extends CommandDataModel {

	// CAMPO TECNICO
	private StringType  codProdotto				= new StringType();
	private BooleanType onLine 					 = new BooleanType();	
	private BooleanType inSwitch = new BooleanType();
	
	// DB
    private StringType  codDisposizione 		= new StringType();
    private StringType  codAgente				= new StringType();
    private StringType  codRete 				= new StringType();

    private StringType  tipoStatoDisposizione 	= new StringType();
    private StringType  codDivisa 				= new StringType();
    private StringType  codAgenteSplit 			= new StringType();
    private DoubleType  dispAgenteSplit 		= new DoubleType();

    private StringType  codComune 				= new StringType();
    private DateType    dataRegistrazione 		= new DateType();
    private DateType    dataModulo 				= new DateType();
    private IntegerType numeroRichiesteAllegato = new IntegerType();
    private IntegerType copieStampabili 		= new IntegerType();
    private DateType    dataSottoscrizione 		= new DateType();
    private StringType  firma 					= new StringType();
    private StringType  ordine 					= new StringType();
    private DateType    dataVariazione 			= new DateType();
    private DateType    dataTrasmissione 		= new DateType();
    private DateType	dataValidazione 		= new DateType();
    private DateType    dataElaborazione 		= new DateType();
    private DateType    dataRich 				= new DateType();
    private DoubleType  totMov 					= new DoubleType();
    private StringType  serverReplica 			= new StringType();
    private DateType    dataElaborazioneSede 	= new DateType();
    private StringType  codAgenteMittente 		= new StringType();
    private StringType  versioneDisposizione 	= new StringType();
    private StringType  provenienzaDisposizione = new StringType();
    private StringType	 linea;
    private StringType  numeroContoCorrente = new StringType();
    private StringType  comune = new StringType();
    private StringType  codiceSwitch = new StringType();
     	
    
    
	public StringType getCodProdotto() {
		return codProdotto;
	}

	public void setCodProdotto(StringType codProdotto) {
		this.codProdotto = codProdotto;
	}
	
	public BooleanType getOnLine() {
		return onLine;
	}

	public void setOnLine(BooleanType onLine) {
		this.onLine = onLine;
	}

	public BooleanType getInSwitch() {
		return inSwitch;
	}

	public void setInSwitch(BooleanType inSwitch) {
		this.inSwitch = inSwitch;
	}

	public StringType getCodAgente() {
		return codAgente;
	}

	public StringType getCodAgenteMittente() {
		return codAgenteMittente;
	}

	public StringType getCodAgenteSplit() {
		return codAgenteSplit;
	}

	public StringType getCodComune() {
		return codComune;
	}

	public StringType getCodDisposizione() {
		return codDisposizione;
	}

	public StringType getCodDivisa() {
		return codDivisa;
	}

	public StringType getCodRete() {
		return codRete;
	}

	public IntegerType getCopieStampabili() {
		return copieStampabili;
	}

	public DateType getDataElaborazione() {
		return dataElaborazione;
	}

	public DateType getDataElaborazioneSede() {
		return dataElaborazioneSede;
	}

	public DateType getDataModulo() {
		return dataModulo;
	}

	public DateType getDataRegistrazione() {
		return dataRegistrazione;
	}

	public DateType getDataRich() {
		return dataRich;
	}

	public DateType getDataSottoscrizione() {
		return dataSottoscrizione;
	}

	public DateType getDataTrasmissione() {
		return dataTrasmissione;
	}

	public DateType getDataVariazione() {
		return dataVariazione;
	}

	public DoubleType getDispAgenteSplit() {
		return dispAgenteSplit;
	}

	public StringType getFirma() {
		return firma;
	}

	public IntegerType getNumeroRichiesteAllegato() {
		return numeroRichiesteAllegato;
	}

	public StringType getOrdine() {
		return ordine;
	}

	public StringType getServerReplica() {
		return serverReplica;
	}


	public StringType getTipoStatoDisposizione() {
		return tipoStatoDisposizione;
	}

	public DoubleType getTotMov() {
		return totMov;
	}

	public StringType getVersioneDisposizione() {
		return versioneDisposizione;
	}

	public void setCodAgente(StringType codAgente) {
		this.codAgente = codAgente;
	}

	public void setCodAgenteMittente(StringType codAgenteMittente) {
		this.codAgenteMittente = codAgenteMittente;
	}

	public void setCodAgenteSplit(StringType codAgenteSplit) {
		this.codAgenteSplit = codAgenteSplit;
	}

	public void setCodComune(StringType codComune) {
		this.codComune = codComune;
	}

	public void setCodDisposizione(StringType codDisposizione) {
		this.codDisposizione = codDisposizione;
	}

	public void setCodDivisa(StringType codDivisa) {
		this.codDivisa = codDivisa;
	}

	public void setCodRete(StringType codRete) {
		this.codRete = codRete;
	}

	public void setCopieStampabili(IntegerType copieStampabili) {
		this.copieStampabili = copieStampabili;
	}

	public void setDataElaborazione(DateType dataElaborazione) {
		this.dataElaborazione = dataElaborazione;
	}

	public void setDataElaborazioneSede(DateType dataElaborazioneSede) {
		this.dataElaborazioneSede = dataElaborazioneSede;
	}

	public void setDataModulo(DateType dataModulo) {
		this.dataModulo = dataModulo;
	}

	public void setDataRegistrazione(DateType dataRegistrazione) {
		this.dataRegistrazione = dataRegistrazione;
	}

	public void setDataRich(DateType dataRich) {
		this.dataRich = dataRich;
	}

	public void setDataSottoscrizione(DateType dataSottoscrizione) {
		this.dataSottoscrizione = dataSottoscrizione;
	}

	public void setDataTrasmissione(DateType dataTrasmissione) {
		this.dataTrasmissione = dataTrasmissione;
	}

	public void setDataVariazione(DateType dataVariazione) {
		this.dataVariazione = dataVariazione;
	}

	public void setDispAgenteSplit(DoubleType dispAgenteSplit) {
		this.dispAgenteSplit = dispAgenteSplit;
	}

	public void setFirma(StringType firma) {
		this.firma = firma;
	}

	public void setNumeroRichiesteAllegato(IntegerType numeroRichiesteAllegato) {
		this.numeroRichiesteAllegato = numeroRichiesteAllegato;
	}

	public void setOrdine(StringType ordine) {
		this.ordine = ordine;
	}

	public void setServerReplica(StringType serverReplica) {
		this.serverReplica = serverReplica;
	}

	public void setTipoStatoDisposizione(StringType tipoStatoDisposizione) {
		this.tipoStatoDisposizione = tipoStatoDisposizione;
	}

	public void setTotMov(DoubleType totMov) {
		this.totMov = totMov;
	}

	public void setVersioneDisposizione(StringType versioneDisposizione) {
		this.versioneDisposizione = versioneDisposizione;
	}


	public StringType getProvenienzaDisposizione() {
		return provenienzaDisposizione;
	}

	public void setProvenienzaDisposizione(StringType provenienzaDisposizione) {
		this.provenienzaDisposizione = provenienzaDisposizione;
	}

	public StringType getLinea() {
		return linea;
	}

	public void setLinea(StringType linea) {
		this.linea = linea;
	}

	public StringType getNumeroContoCorrente() {
		return numeroContoCorrente;
	}

	public void setNumeroContoCorrente(StringType numeroContoCorrente) {
		this.numeroContoCorrente = numeroContoCorrente;
	}

	public DateType getDataValidazione() {
		return dataValidazione;
	}

	public void setDataValidazione(DateType dataValidazione) {
		this.dataValidazione = dataValidazione;
	}

	public StringType getComune() {
		return comune;
	}

	public void setComune(StringType comune) {
		this.comune = comune;
	}

	public StringType getCodiceSwitch() {
		return codiceSwitch;
	}

	public void setCodiceSwitch(StringType codiceSwitch) {
		this.codiceSwitch = codiceSwitch;
	}
	
}
