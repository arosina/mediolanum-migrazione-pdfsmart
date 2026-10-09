package prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

/*********************************************************************************/
/*********************************************************************************/
public class InrDispCliInput extends CommandDataModel {

	private StringType 	codDisposizione				= new StringType();
	private StringType 	codAgente					= new StringType();
	private StringType 	codRete						= new StringType();
	private DateType 	dataVariazione				= new DateType();
	private StringType 	serverReplica				= new StringType();
	private StringType 	tipoStatoDisposizione		= new StringType();
	private StringType 	codClienteMediolanum		= new StringType();
	private StringType 	ruoloCliProd				= new StringType();
	private StringType 	clienteBusinessClub			= new StringType();
	private IntegerType numPercettoriReddito		= new IntegerType();
	private DateType 	dataInizioProfessione		= new DateType();
	private StringType 	redditoNetto				= new StringType();
	private DateType 	dataAbitazione				= new DateType();
	private StringType 	proprietaAbitazione			= new StringType();
	private StringType 	titolarietaCC				= new StringType();
	private DateType 	dataInizioTitolCC			= new DateType();
	private StringType 	pressoBanca					= new StringType();
	private StringType 	pressoFiliale				= new StringType();
	private StringType 	conoscenzaClientePeriodo	= new StringType();
	private StringType 	valutazioneCliente			= new StringType();
	private StringType 	codPotenziale				= new StringType();
	private StringType 	codAgevolazione				= new StringType();
	private StringType 	mutuo						= new StringType();
	private StringType 	cartaCredito				= new StringType();
	
	private StringType 	descrCartaCredito			= new StringType();
	private DoubleType 	impAbtz						= new DoubleType();
	private StringType 	codGradParentela			= new StringType();
	private StringType 	codPerioRuolo				= new StringType();
	private StringType 	codValutRuolo				= new StringType();
	private StringType 	prefCellIntern				= new StringType();
	private StringType 	prefCell					= new StringType();
	private StringType 	cell						= new StringType();
	private StringType 	flagAdesBorsaMob			= new StringType();
	private StringType 	flagAbilitazioneSMS			= new StringType();
	private StringType 	flagLibrettoAssegni			= new StringType();
	private StringType 	flagCodiceSegretoDaSms		= new StringType();
	
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
	public DateType getDataVariazione() {
		return dataVariazione;
	}
	public void setDataVariazione(DateType dataVariazione) {
		this.dataVariazione = dataVariazione;
	}
	public StringType getServerReplica() {
		return serverReplica;
	}
	public void setServerReplica(StringType serverReplica) {
		this.serverReplica = serverReplica;
	}
	public StringType getTipoStatoDisposizione() {
		return tipoStatoDisposizione;
	}
	public void setTipoStatoDisposizione(StringType tipoStatoDisposizione) {
		this.tipoStatoDisposizione = tipoStatoDisposizione;
	}
	public StringType getCodClienteMediolanum() {
		return codClienteMediolanum;
	}
	public void setCodClienteMediolanum(StringType codClienteMediolanum) {
		this.codClienteMediolanum = codClienteMediolanum;
	}
	public StringType getRuoloCliProd() {
		return ruoloCliProd;
	}
	public void setRuoloCliProd(StringType ruoloCliProd) {
		this.ruoloCliProd = ruoloCliProd;
	}
	public StringType getClienteBusinessClub() {
		return clienteBusinessClub;
	}
	public void setClienteBusinessClub(StringType clienteBusinessClub) {
		this.clienteBusinessClub = clienteBusinessClub;
	}
	public IntegerType getNumPercettoriReddito() {
		return numPercettoriReddito;
	}
	public void setNumPercettoriReddito(IntegerType numPercettoriReddito) {
		this.numPercettoriReddito = numPercettoriReddito;
	}
	public DateType getDataInizioProfessione() {
		return dataInizioProfessione;
	}
	public void setDataInizioProfessione(DateType dataInizioProfessione) {
		this.dataInizioProfessione = dataInizioProfessione;
	}
	public StringType getRedditoNetto() {
		return redditoNetto;
	}
	public void setRedditoNetto(StringType redditoNetto) {
		this.redditoNetto = redditoNetto;
	}
	public DateType getDataAbitazione() {
		return dataAbitazione;
	}
	public void setDataAbitazione(DateType dataAbitazione) {
		this.dataAbitazione = dataAbitazione;
	}
	public StringType getProprietaAbitazione() {
		return proprietaAbitazione;
	}
	public void setProprietaAbitazione(StringType proprietaAbitazione) {
		this.proprietaAbitazione = proprietaAbitazione;
	}
	public StringType getTitolarietaCC() {
		return titolarietaCC;
	}
	public void setTitolarietaCC(StringType titolarietaCC) {
		this.titolarietaCC = titolarietaCC;
	}
	public DateType getDataInizioTitolCC() {
		return dataInizioTitolCC;
	}
	public void setDataInizioTitolCC(DateType dataInizioTitolCC) {
		this.dataInizioTitolCC = dataInizioTitolCC;
	}
	public StringType getPressoBanca() {
		return pressoBanca;
	}
	public void setPressoBanca(StringType pressoBanca) {
		this.pressoBanca = pressoBanca;
	}
	public StringType getPressoFiliale() {
		return pressoFiliale;
	}
	public void setPressoFiliale(StringType pressoFiliale) {
		this.pressoFiliale = pressoFiliale;
	}
	public StringType getConoscenzaClientePeriodo() {
		return conoscenzaClientePeriodo;
	}
	public void setConoscenzaClientePeriodo(StringType conoscenzaClientePeriodo) {
		this.conoscenzaClientePeriodo = conoscenzaClientePeriodo;
	}
	public StringType getValutazioneCliente() {
		return valutazioneCliente;
	}
	public void setValutazioneCliente(StringType valutazioneCliente) {
		this.valutazioneCliente = valutazioneCliente;
	}
	public StringType getCodPotenziale() {
		return codPotenziale;
	}
	public void setCodPotenziale(StringType codPotenziale) {
		this.codPotenziale = codPotenziale;
	}
	public StringType getCodAgevolazione() {
		return codAgevolazione;
	}
	public void setCodAgevolazione(StringType codAgevolazione) {
		this.codAgevolazione = codAgevolazione;
	}
	public StringType getMutuo() {
		return mutuo;
	}
	public void setMutuo(StringType mutuo) {
		this.mutuo = mutuo;
	}
	public StringType getCartaCredito() {
		return cartaCredito;
	}
	public void setCartaCredito(StringType cartaCredito) {
		this.cartaCredito = cartaCredito;
	}
	public StringType getDescrCartaCredito() {
		return descrCartaCredito;
	}
	public void setDescrCartaCredito(StringType descrCartaCredito) {
		this.descrCartaCredito = descrCartaCredito;
	}
	public DoubleType getImpAbtz() {
		return impAbtz;
	}
	public void setImpAbtz(DoubleType impAbtz) {
		this.impAbtz = impAbtz;
	}
	public StringType getCodGradParentela() {
		return codGradParentela;
	}
	public void setCodGradParentela(StringType codGradParentela) {
		this.codGradParentela = codGradParentela;
	}
	public StringType getCodPerioRuolo() {
		return codPerioRuolo;
	}
	public void setCodPerioRuolo(StringType codPerioRuolo) {
		this.codPerioRuolo = codPerioRuolo;
	}
	public StringType getCodValutRuolo() {
		return codValutRuolo;
	}
	public void setCodValutRuolo(StringType codValutRuolo) {
		this.codValutRuolo = codValutRuolo;
	}
	public StringType getPrefCellIntern() {
		return prefCellIntern;
	}
	public void setPrefCellIntern(StringType prefCellIntern) {
		this.prefCellIntern = prefCellIntern;
	}
	public StringType getPrefCell() {
		return prefCell;
	}
	public void setPrefCell(StringType prefCell) {
		this.prefCell = prefCell;
	}
	public StringType getCell() {
		return cell;
	}
	public void setCell(StringType cell) {
		this.cell = cell;
	}
	public StringType getFlagAdesBorsaMob() {
		return flagAdesBorsaMob;
	}
	public void setFlagAdesBorsaMob(StringType flagAdesBorsaMob) {
		this.flagAdesBorsaMob = flagAdesBorsaMob;
	}
	public StringType getFlagAbilitazioneSMS() {
		return flagAbilitazioneSMS;
	}
	public void setFlagAbilitazioneSMS(StringType flagAbilitazioneSMS) {
		this.flagAbilitazioneSMS = flagAbilitazioneSMS;
	}
	public StringType getFlagLibrettoAssegni() {
		return flagLibrettoAssegni;
	}
	public void setFlagLibrettoAssegni(StringType flagLibrettoAssegni) {
		this.flagLibrettoAssegni = flagLibrettoAssegni;
	}
	public StringType getFlagCodiceSegretoDaSms() {
		return flagCodiceSegretoDaSms;
	}
	public void setFlagCodiceSegretoDaSms(StringType flagCodiceSegretoDaSms) {
		this.flagCodiceSegretoDaSms = flagCodiceSegretoDaSms;
	}
}
