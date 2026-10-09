package prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/*********************************************************************************/
/*********************************************************************************/
public class InrDispVitaCliInput extends CommandDataModel{
	
    private StringType codDisposizione			= new StringType();
    private StringType codAgente				= new StringType();
    private StringType codRete					= new StringType();
    private StringType codRuoloCliProd			= new StringType();
    private StringType tipoStatoDisposizione	= new StringType();
    private StringType codClienteMediolanum		= new StringType();
    private StringType codPotenziale			= new StringType();
    private StringType serverReplica			= new StringType();
    private StringType lavoratoreDipendente		= new StringType();
    private StringType abilitazioneInternet     = new StringType();
    private StringType titoloStudio			    = new StringType();
    private StringType professione			    = new StringType(); 
    private StringType primaOccupazione		    = new StringType();
    private StringType tipoRelazioneContraenteAssicurando			    = new StringType(); 
    private StringType descrizioneTipoRelazioneContraenteAssicurando		    = new StringType();


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

	public StringType getLavoratoreDipendente() {
		return lavoratoreDipendente;
	}

	public void setLavoratoreDipendente(StringType lavoratoreDipendente) {
		this.lavoratoreDipendente = lavoratoreDipendente;
	}

	public StringType getAbilitazioneInternet() {
		return abilitazioneInternet;
	}

	public void setAbilitazioneInternet(StringType abilitazioneInternet) {
		this.abilitazioneInternet = abilitazioneInternet;
	}

	public StringType getTitoloStudio() {
		return titoloStudio;
	}

	public void setTitoloStudio(StringType titoloStudio) {
		this.titoloStudio = titoloStudio;
	}

	public StringType getProfessione() {
		return professione;
	}

	public void setProfessione(StringType professione) {
		this.professione = professione;
	}

	public StringType getPrimaOccupazione() {
		return primaOccupazione;
	}

	public void setPrimaOccupazione(StringType primaOccupazione) {
		this.primaOccupazione = primaOccupazione;
	}

	public StringType getTipoRelazioneContraenteAssicurando() {
		return tipoRelazioneContraenteAssicurando;
	}

	public void setTipoRelazioneContraenteAssicurando(StringType tipoRelazioneContraenteAssicurando) {
		this.tipoRelazioneContraenteAssicurando = tipoRelazioneContraenteAssicurando;
	}

	public StringType getDescrizioneTipoRelazioneContraenteAssicurando() {
		return descrizioneTipoRelazioneContraenteAssicurando;
	}

	public void setDescrizioneTipoRelazioneContraenteAssicurando(StringType descrizioneTipoRelazioneContraenteAssicurando) {
		this.descrizioneTipoRelazioneContraenteAssicurando = descrizioneTipoRelazioneContraenteAssicurando;
	}

	
	
	
}