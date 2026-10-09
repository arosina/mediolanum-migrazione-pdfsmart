package prgm.pdfwebformspolizzeutil.popup;

import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.dataentryutil.AbstractAutocompleteModel;
import prgm.pdfwebformspolizzeutil.model.BeneficiarioModel;

public class RicercaBeneficiariModel extends AbstractAutocompleteModel {

	
	private ListType	elencoBeneficiari = new ListType(BeneficiarioModel.class);
	private boolean 	isPrimaAttivazione = true;



	
	//Parametri di ricerca	
	private StringType 	codiceCliente = new StringType();
	private StringType 	codiceFiscale = new StringType();
	private StringType 	nome = new StringType();
	private StringType 	cognome = new StringType();
	private StringType 	ragioneSociale = new StringType();
	private StringType 	tipoRicerca = new StringType(); //PF, PG o TIT
	private StringType 	codiceAgente = new StringType(); 

	
	//Parametri di ricerca di appoggio (usati per aggirare il problema di freeze su Chrome)
	private StringType 	codiceClienteAppoggio = new StringType();
	private StringType 	codiceFiscaleAppoggio = new StringType();
	private StringType 	nomeAppoggio = new StringType();
	private StringType 	cognomeAppoggio = new StringType();
	private StringType 	ragioneSocialeAppoggio = new StringType();
	
	private StringType codAgeImpersonato = new StringType();
    private StringType codRuoloImpersonato =new StringType();

	
	
	public ListType getElencoBeneficiari() {
		return elencoBeneficiari;
	}
	public void setElencoBeneficiari(ListType elencoBeneficiari) {
		this.elencoBeneficiari = elencoBeneficiari;
	}
	public boolean isPrimaAttivazione() {
		return isPrimaAttivazione;
	}
	public void setPrimaAttivazione(boolean isPrimaAttivazione) {
		this.isPrimaAttivazione = isPrimaAttivazione;
	}
	public StringType getCodiceCliente() {
		return codiceCliente;
	}
	public void setCodiceCliente(StringType codiceCliente) {
		this.codiceCliente = codiceCliente;
	}
	public StringType getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(StringType codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public StringType getNome() {
		return nome;
	}
	public void setNome(StringType nome) {
		this.nome = nome;
	}
	public StringType getCognome() {
		return cognome;
	}
	public void setCognome(StringType cognome) {
		this.cognome = cognome;
	}
	public StringType getRagioneSociale() {
		return ragioneSociale;
	}
	public void setRagioneSociale(StringType ragioneSociale) {
		this.ragioneSociale = ragioneSociale;
	}
	public StringType getTipoRicerca() {
		return tipoRicerca;
	}
	public void setTipoRicerca(StringType tipoRicerca) {
		this.tipoRicerca = tipoRicerca;
	}
	public StringType getCodiceClienteAppoggio() {
		return codiceClienteAppoggio;
	}
	public void setCodiceClienteAppoggio(StringType codiceClienteAppoggio) {
		this.codiceClienteAppoggio = codiceClienteAppoggio;
	}
	public StringType getCodiceFiscaleAppoggio() {
		return codiceFiscaleAppoggio;
	}
	public void setCodiceFiscaleAppoggio(StringType codiceFiscaleAppoggio) {
		this.codiceFiscaleAppoggio = codiceFiscaleAppoggio;
	}
	public StringType getNomeAppoggio() {
		return nomeAppoggio;
	}
	public void setNomeAppoggio(StringType nomeAppoggio) {
		this.nomeAppoggio = nomeAppoggio;
	}
	public StringType getCognomeAppoggio() {
		return cognomeAppoggio;
	}
	public void setCognomeAppoggio(StringType cognomeAppoggio) {
		this.cognomeAppoggio = cognomeAppoggio;
	}
	public StringType getRagioneSocialeAppoggio() {
		return ragioneSocialeAppoggio;
	}
	public void setRagioneSocialeAppoggio(StringType ragioneSocialeAppoggio) {
		this.ragioneSocialeAppoggio = ragioneSocialeAppoggio;
	}
	public StringType getCodiceAgente() {
		return codiceAgente;
	}
	public void setCodiceAgente(StringType codiceAgente) {
		this.codiceAgente = codiceAgente;
	}
	public StringType getCodAgeImpersonato() {
		return codAgeImpersonato;
	}
	public StringType getCodRuoloImpersonato() {
		return codRuoloImpersonato;
	}
	public void setCodAgeImpersonato(StringType codAgeImpersonato) {
		this.codAgeImpersonato = codAgeImpersonato;
	}
	public void setCodRuoloImpersonato(StringType codRuoloImpersonato) {
		this.codRuoloImpersonato = codRuoloImpersonato;
	}
	

	
}
