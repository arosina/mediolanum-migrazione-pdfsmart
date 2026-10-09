package prgm.ita.p.dac.model;

import com.atosorigin.wfem.command.CommandError;
import com.atosorigin.wfem.command.CommandMessage;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;

/***********************************************************************************************/
/***********************************************************************************************/
public class BaseModel extends ParamsModel {

	private boolean		  refreshable = false;
	private StringType    userMOM = new StringType();
	private StringType    esitoMOM = new StringType();
	private StringType    descrEsitoMOM = new StringType();
	
	private AgenteModel   agenteRiferimento = new AgenteModel();
	
	private StringType	  timestamp = new StringType();
	private StringType	  codUtenteIns = new StringType();
	private StringType	  codUtenteUpd = new StringType();
	private TimestampType dataOraIns = new TimestampType();
	private TimestampType dataOraUpd = new TimestampType();
	private TimestampType dataOraCambioStato = new TimestampType();
	private TimestampType dataOraEmissione = new TimestampType();

	private IntegerType   stato = new IntegerType();
	private IntegerType   esito = new IntegerType();
	private IntegerType   azione = new IntegerType();
	
	private IntegerType   uffMittente = new IntegerType();
	private IntegerType   uffDestinatario = new IntegerType();
	private IntegerType   ubicazione = new IntegerType();
	
	private StringType	  flagReplica = new StringType();
	
	private StringType	  noteAutore = new StringType();
	private StringType	  noteOperatore = new StringType();
	
	// Calcolati join DB
	private StringType	  descrStato = new StringType();
	private StringType	  descrEsito = new StringType();
	private StringType	  descrUfficio = new StringType();
	private StringType	  descrUbicazione = new StringType();
	private StringType	  descrUffMittente = new StringType();
	private StringType	  descrUffDestinatario = new StringType();
	private StringType	  descrUffLavorazione = new StringType();
	private StringType	  descrUffSpunta = new StringType();

	/***********************************************************************************************/
	/***********************************************************************************************/
	public BaseModel(){
		addCodDescField("ufficio","Uffici");
		addCodDescField("uffMittente","Uffici");
		addCodDescField("uffDestinatario","Uffici");
		addCodDescField("ubicazione","Uffici");
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String getMessageString(){
		if(!hasCommandMessages())
			return "";
		CommandMessage m = (CommandMessage)getCommandMessages().get(0);
		return m.toString();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String getErrorString(){
		if(!hasCommandErrors())
			return "";
		CommandError e = (CommandError)getCommandErrors().get(0);
		return e.toString();		
	}
	
	
	public StringType getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(StringType timestamp) {
		this.timestamp = timestamp;
	}
	public IntegerType getStato() {
		return stato;
	}
	public void setStato(IntegerType stato) {
		this.stato = stato;
	}
	public IntegerType getUbicazione() {
		return ubicazione;
	}
	public void setUbicazione(IntegerType ubicazione) {
		this.ubicazione = ubicazione;
	}
	public IntegerType getAzione() {
		return azione;
	}
	public void setAzione(IntegerType azione) {
		this.azione = azione;
	}
	public IntegerType getUffMittente() {
		return uffMittente;
	}
	public void setUffMittente(IntegerType uffMittente) {
		this.uffMittente = uffMittente;
	}
	public IntegerType getUffDestinatario() {
		return uffDestinatario;
	}
	public void setUffDestinatario(IntegerType uffDestinatario) {
		this.uffDestinatario = uffDestinatario;
	}
	public StringType getCodUtenteIns() {
		return codUtenteIns;
	}
	public void setCodUtenteIns(StringType codUtenteIns) {
		this.codUtenteIns = codUtenteIns;
	}
	public StringType getCodUtenteUpd() {
		return codUtenteUpd;
	}
	public void setCodUtenteUpd(StringType codUtenteUpd) {
		this.codUtenteUpd = codUtenteUpd;
	}
	public TimestampType getDataOraIns() {
		return dataOraIns;
	}
	public void setDataOraIns(TimestampType dataOraIns) {
		this.dataOraIns = dataOraIns;
	}
	public TimestampType getDataOraUpd() {
		return dataOraUpd;
	}
	public void setDataOraUpd(TimestampType dataOraUpd) {
		this.dataOraUpd = dataOraUpd;
	}
	public AgenteModel getAgenteRiferimento() {
		return agenteRiferimento;
	}
	public void setAgenteRiferimento(AgenteModel agenteRiferimento) {
		this.agenteRiferimento = agenteRiferimento;
	}
	public StringType getFlagReplica() {
		return flagReplica;
	}
	public void setFlagReplica(StringType flagReplica) {
		this.flagReplica = flagReplica;
	}
	public TimestampType getDataOraCambioStato() {
		return dataOraCambioStato;
	}
	public void setDataOraCambioStato(TimestampType dataOraCambioStato) {
		this.dataOraCambioStato = dataOraCambioStato;
	}
	public TimestampType getDataOraEmissione() {
		return dataOraEmissione;
	}
	public void setDataOraEmissione(TimestampType dataOraEmissione) {
		this.dataOraEmissione = dataOraEmissione;
	}
	public IntegerType getEsito() {
		return esito;
	}
	public void setEsito(IntegerType esito) {
		this.esito = esito;
	}
	public StringType getNoteAutore() {
		return noteAutore;
	}
	public void setNoteAutore(StringType noteAutore) {
		this.noteAutore = noteAutore;
	}
	public StringType getNoteOperatore() {
		return noteOperatore;
	}
	public void setNoteOperatore(StringType noteOperatore) {
		this.noteOperatore = noteOperatore;
	}
	public StringType getDescrStato() {
		return descrStato;
	}

	public void setDescrStato(StringType descrStato) {
		this.descrStato = descrStato;
	}

	public StringType getDescrEsito() {
		return descrEsito;
	}

	public void setDescrEsito(StringType descrEsito) {
		this.descrEsito = descrEsito;
	}

	public StringType getDescrUffDestinatario() {
		return descrUffDestinatario;
	}

	public void setDescrUffDestinatario(StringType descrUffDestinatario) {
		this.descrUffDestinatario = descrUffDestinatario;
	}

	public StringType getDescrUffMittente() {
		return descrUffMittente;
	}

	public void setDescrUffMittente(StringType descrUffMittente) {
		this.descrUffMittente = descrUffMittente;
	}

	public boolean isRefreshable() {
		return refreshable;
	}

	public void setRefreshable(boolean refreshable) {
		this.refreshable = refreshable;
	}

	public StringType getDescrUfficio() {
		return descrUfficio;
	}

	public void setDescrUfficio(StringType descrUfficio) {
		this.descrUfficio = descrUfficio;
	}

	public StringType getDescrUbicazione() {
		return descrUbicazione;
	}

	public void setDescrUbicazione(StringType descrUbicazione) {
		this.descrUbicazione = descrUbicazione;
	}

	public StringType getDescrUffLavorazione() {
		return descrUffLavorazione;
	}

	public void setDescrUffLavorazione(StringType descrUffLavorazione) {
		this.descrUffLavorazione = descrUffLavorazione;
	}

	public StringType getDescrUffSpunta() {
		return descrUffSpunta;
	}

	public void setDescrUffSpunta(StringType descrUffSpunta) {
		this.descrUffSpunta = descrUffSpunta;
	}

	public StringType getEsitoMOM() {
		return esitoMOM;
	}

	public void setEsitoMOM(StringType esitoMOM) {
		this.esitoMOM = esitoMOM;
	}

	public StringType getDescrEsitoMOM() {
		return descrEsitoMOM;
	}

	public void setDescrEsitoMOM(StringType descrEsitoMOM) {
		this.descrEsitoMOM = descrEsitoMOM;
	}

	public StringType getUserMOM() {
		return userMOM;
	}

	public void setUserMOM(StringType userMOM) {
		this.userMOM = userMOM;
	}

}
