package prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/*********************************************************************************/
/*********************************************************************************/
public class InrDispFondiCliInput extends CommandDataModel {

	private StringType codDisposizione		= new StringType();
	private StringType codAgente			= new StringType();
	private StringType codRete				= new StringType();
	private StringType codRuoloCliProd		= new StringType();
	private StringType tipoStatoDisposizione= new StringType();
	private StringType codClienteMediolanum	= new StringType();
	private StringType codPotenziale		= new StringType();
	private StringType serverReplica		= new StringType();

	private StringType infoFinanziaria		= new StringType();
	private StringType esperienza			= new StringType();
	private StringType altriInvestimenti	= new StringType();
	private StringType obiettivo			= new StringType();
	private StringType rischio				= new StringType();
	private StringType abilitazioneInternet = new StringType();
	private StringType flagPoliticamenteEsposto 		= new StringType();
	private StringType motivazionePoliticamenteEsposto 	= new StringType();
	

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

	public StringType getCodRete() {
		return codRete;
	}

	public StringType getCodRuoloCliProd() {
		return codRuoloCliProd;
	}

	public StringType getServerReplica() {
		return serverReplica;
	}

	public StringType getTipoStatoDisposizione() {
		return tipoStatoDisposizione;
	}

	public StringType getAbilitazioneInternet() {
		return abilitazioneInternet;
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

	public void setCodRete(StringType codRete) {
		this.codRete = codRete;
	}

	public void setCodRuoloCliProd(StringType codRuoloCliProd) {
		this.codRuoloCliProd = codRuoloCliProd;
	}

	public void setServerReplica(StringType serverReplica) {
		this.serverReplica = serverReplica;
	}

	public void setTipoStatoDisposizione(StringType tipoStatoDisposizione) {
		this.tipoStatoDisposizione = tipoStatoDisposizione;
	}

	public StringType getAltriInvestimenti() {
		return altriInvestimenti;
	}

	public StringType getEsperienza() {
		return esperienza;
	}

	public StringType getInfoFinanziaria() {
		return infoFinanziaria;
	}

	public StringType getObiettivo() {
		return obiettivo;
	}

	public StringType getRischio() {
		return rischio;
	}

	public void setAltriInvestimenti(StringType altriInvestimenti) {
		this.altriInvestimenti = altriInvestimenti;
	}

	public void setEsperienza(StringType esperienza) {
		this.esperienza = esperienza;
	}

	public void setInfoFinanziaria(StringType infoFinanziaria) {
		this.infoFinanziaria = infoFinanziaria;
	}

	public void setObiettivo(StringType obiettivo) {
		this.obiettivo = obiettivo;
	}

	public void setRischio(StringType rischio) {
		this.rischio = rischio;
	}

	public void setAbilitazioneInternet(StringType abilitazioneInternet) {
		this.abilitazioneInternet = abilitazioneInternet;
	}
	public StringType getFlagPoliticamenteEsposto() {
		return flagPoliticamenteEsposto;
	}

	public void setFlagPoliticamenteEsposto(StringType flagPoliticamenteEsposto) {
		this.flagPoliticamenteEsposto = flagPoliticamenteEsposto;
	}

	public StringType getMotivazionePoliticamenteEsposto() {
		return motivazionePoliticamenteEsposto;
	}

	public void setMotivazionePoliticamenteEsposto(
			StringType motivazionePoliticamenteEsposto) {
		this.motivazionePoliticamenteEsposto = motivazionePoliticamenteEsposto;
	}

}
