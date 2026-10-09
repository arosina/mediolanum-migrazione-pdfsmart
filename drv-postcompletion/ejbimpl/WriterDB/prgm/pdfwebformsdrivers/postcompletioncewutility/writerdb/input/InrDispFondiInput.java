package prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.StringType;

/*********************************************************************************/
/*********************************************************************************/
public class InrDispFondiInput extends CommandDataModel{

	private StringType codDisposizione			= new StringType(); 
	private StringType codAgente				= new StringType();
	private StringType codRete					= new StringType();
	private StringType serverReplica			= new StringType();
	private StringType tipoStatoDisposizione	= new StringType();	
	private StringType codAgevol				= new StringType();
	private StringType flagCCAgevol				= new StringType();
	private StringType codCCAgevol				= new StringType();
	private StringType flagIntenz				= new StringType();
	private DoubleType impIntenz				= new DoubleType();
	private StringType codFondo					= new StringType();
	private StringType codRif					= new StringType();
	private StringType relativoImpresa			= new StringType();
	private StringType regimeAmministrativo		= new StringType();
	private StringType flagEmissione			= new StringType();
	private StringType numeroCertificato		= new StringType();
	private DoubleType numeroQuoteCertificato 	= new DoubleType();
	private StringType tipoCertificato			= new StringType();
	private StringType codVers					= new StringType();
	private StringType codAgenteCertifLiq		= new StringType();
	private StringType idIstanza				= new StringType();
	
	/*********************************************************************************/
	/*********************************************************************************/
		
	public StringType getCodAgente() {
		return codAgente;
	}

	public StringType getCodAgevol() {
		return codAgevol;
	}

	public StringType getCodCCAgevol() {
		return codCCAgevol;
	}

	public StringType getCodDisposizione() {
		return codDisposizione;
	}

	public StringType getCodFondo() {
		return codFondo;
	}

	public StringType getCodRete() {
		return codRete;
	}

	public StringType getCodRif() {
		return codRif;
	}

	public StringType getCodVers() {
		return codVers;
	}

	public StringType getFlagCCAgevol() {
		return flagCCAgevol;
	}

	public StringType getFlagEmissione() {
		return flagEmissione;
	}

	public StringType getFlagIntenz() {
		return flagIntenz;
	}

	public DoubleType getImpIntenz() {
		return impIntenz;
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

	public void setCodAgevol(StringType codAgevol) {
		this.codAgevol = codAgevol;
	}

	public void setCodCCAgevol(StringType codCCAgevol) {
		this.codCCAgevol = codCCAgevol;
	}

	public void setCodDisposizione(StringType codDisposizione) {
		this.codDisposizione = codDisposizione;
	}

	public void setCodFondo(StringType codFondo) {
		this.codFondo = codFondo;
	}

	public void setCodRete(StringType codRete) {
		this.codRete = codRete;
	}

	public void setCodRif(StringType codRif) {
		this.codRif = codRif;
	}

	public void setCodVers(StringType codVers) {
		this.codVers = codVers;
	}

	public void setFlagCCAgevol(StringType flagCCAgevol) {
		this.flagCCAgevol = flagCCAgevol;
	}

	public void setFlagEmissione(StringType flagEmissione) {
		this.flagEmissione = flagEmissione;
	}

	public void setFlagIntenz(StringType flagIntenz) {
		this.flagIntenz = flagIntenz;
	}

	public void setImpIntenz(DoubleType impIntenz) {
		this.impIntenz = impIntenz;
	}

	public void setServerReplica(StringType serverReplica) {
		this.serverReplica = serverReplica;
	}

	public void setTipoStatoDisposizione(StringType tipoStatoDisposizione) {
		this.tipoStatoDisposizione = tipoStatoDisposizione;
	}

	public StringType getRegimeAmministrativo() {
		return regimeAmministrativo;
	}

	public StringType getRelativoImpresa() {
		return relativoImpresa;
	}

	public void setRegimeAmministrativo(StringType regimeAmministrativo) {
		this.regimeAmministrativo = regimeAmministrativo;
	}

	public void setRelativoImpresa(StringType relativoImpresa) {
		this.relativoImpresa = relativoImpresa;
	}

	public StringType getNumeroCertificato() {
		return numeroCertificato;
	}

	public void setNumeroCertificato(StringType numeroCertificato) {
		this.numeroCertificato = numeroCertificato;
	}

	public StringType getTipoCertificato() {
		return tipoCertificato;
	}

	public void setTipoCertificato(StringType tipoCertificato) {
		this.tipoCertificato = tipoCertificato;
	}

	public void setNumeroQuoteCertificato(DoubleType numeroQuoteCertificato) {
		this.numeroQuoteCertificato = numeroQuoteCertificato;
	}

	public DoubleType getNumeroQuoteCertificato() {
		return numeroQuoteCertificato;
	}

	public StringType getCodAgenteCertifLiq() {
		return codAgenteCertifLiq;
	}

	public void setCodAgenteCertifLiq(StringType codAgenteCertifLiq) {
		this.codAgenteCertifLiq = codAgenteCertifLiq;
	}

	public StringType getIdIstanza() {
		return idIstanza;
	}

	public void setIdIstanza(StringType idIstanza) {
		this.idIstanza = idIstanza;
	}


	
}
