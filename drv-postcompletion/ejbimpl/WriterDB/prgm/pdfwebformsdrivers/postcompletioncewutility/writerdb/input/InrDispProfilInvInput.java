package prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.StringType;

/*********************************************************************************/
/*********************************************************************************/
public class InrDispProfilInvInput extends CommandDataModel{
	
	private StringType 	codProfiloInvestimento	= new StringType();
	private StringType 	codDisposizione			= new StringType();
	private StringType 	codRete					= new StringType();
	private StringType 	codAgente				= new StringType();
	private StringType 	codTipoProfilo			= new StringType();
	private DoubleType	prcIntrapr				= new DoubleType();
	private DoubleType prcDina					= new DoubleType();
	private DoubleType prcProtz				= new DoubleType();
	private DoubleType prcCresc				= new DoubleType();
	private DoubleType prcModer				= new DoubleType();
	private DoubleType prcLiqui				= new DoubleType();
	private DoubleType prcFondo7				= new DoubleType();
	private StringType 	serverReplica			= new StringType();
	private StringType 	tipoStatoDisposizione	= new StringType();

	private StringType 	flagIntrapr				= new StringType();
	private StringType 	flagDina				= new StringType();
	private StringType 	flagProtz				= new StringType();
	private StringType 	flagCresc				= new StringType();

	
	public StringType getCodAgente() {
		return codAgente;
	}

	public StringType getCodDisposizione() {
		return codDisposizione;
	}

	public StringType getCodProfiloInvestimento() {
		return codProfiloInvestimento;
	}

	public StringType getCodRete() {
		return codRete;
	}

	public StringType getCodTipoProfilo() {
		return codTipoProfilo;
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

	public void setCodDisposizione(StringType codDisposizione) {
		this.codDisposizione = codDisposizione;
	}

	public void setCodProfiloInvestimento(StringType codProfiloInvestimento) {
		this.codProfiloInvestimento = codProfiloInvestimento;
	}

	public void setCodRete(StringType codRete) {
		this.codRete = codRete;
	}

	public void setCodTipoProfilo(StringType codTipoProfilo) {
		this.codTipoProfilo = codTipoProfilo;
	}

	public void setServerReplica(StringType serverReplica) {
		this.serverReplica = serverReplica;
	}

	public void setTipoStatoDisposizione(StringType tipoStatoDisposizione) {
		this.tipoStatoDisposizione = tipoStatoDisposizione;
	}

	public DoubleType getPrcCresc() {
		return prcCresc;
	}

	public DoubleType getPrcDina() {
		return prcDina;
	}

	public DoubleType getPrcFondo7() {
		return prcFondo7;
	}

	public DoubleType getPrcIntrapr() {
		return prcIntrapr;
	}

	public DoubleType getPrcLiqui() {
		return prcLiqui;
	}

	public DoubleType getPrcModer() {
		return prcModer;
	}

	public DoubleType getPrcProtz() {
		return prcProtz;
	}

	public void setPrcCresc(DoubleType prcCresc) {
		this.prcCresc = prcCresc;
	}

	public void setPrcDina(DoubleType prcDina) {
		this.prcDina = prcDina;
	}

	public void setPrcFondo7(DoubleType prcFondo7) {
		this.prcFondo7 = prcFondo7;
	}

	public void setPrcIntrapr(DoubleType prcIntrapr) {
		this.prcIntrapr = prcIntrapr;
	}

	public void setPrcLiqui(DoubleType prcLiqui) {
		this.prcLiqui = prcLiqui;
	}

	public void setPrcModer(DoubleType prcModer) {
		this.prcModer = prcModer;
	}

	public void setPrcProtz(DoubleType prcProtz) {
		this.prcProtz = prcProtz;
	}

	public StringType getFlagIntrapr() {
		return flagIntrapr;
	}

	public void setFlagIntrapr(StringType flagIntrapr) {
		this.flagIntrapr = flagIntrapr;
	}

	public StringType getFlagDina() {
		return flagDina;
	}

	public void setFlagDina(StringType flagDina) {
		this.flagDina = flagDina;
	}

	public StringType getFlagProtz() {
		return flagProtz;
	}

	public void setFlagProtz(StringType flagProtz) {
		this.flagProtz = flagProtz;
	}

	public StringType getFlagCresc() {
		return flagCresc;
	}

	public void setFlagCresc(StringType flagCresc) {
		this.flagCresc = flagCresc;
	}

}
