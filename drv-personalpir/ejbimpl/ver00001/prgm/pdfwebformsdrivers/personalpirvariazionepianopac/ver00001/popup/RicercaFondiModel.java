package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.popup;

import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.dataentryutil.AbstractAutocompleteModel;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.model.FondoModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class RicercaFondiModel extends AbstractAutocompleteModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4493698971609796675L;
	private StringType	tipoRicerca = new StringType(); //PIC,DC,BC,IIS
	private StringType 	tipoSottoscrizione = new StringType();
	
	private StringType	tipologiaDoubleChance = new StringType();
	private StringType	codiceProdotto   = new StringType();
	private StringType  flagTrasformatoPic   = new StringType();
	private StringType	fondiSelezionati   = new StringType();
	private StringType	numeroContratto   = new StringType();
	private StringType	codProdottoPolizza   = new StringType();

	//Parametri di ricerca	
	private StringType	tipologia = new StringType();
	private StringType 	societa = new StringType();
	private StringType 	denominazione = new StringType();
	private StringType  inPortafoglio = new StringType();
	
	//Parametri di ricerca di appoggio (usati per aggirare il problema di freeze su Chrome)
	private StringType	tipologiaAppoggio = new StringType();
	private StringType 	societaAppoggio = new StringType();
	private StringType 	denominazioneAppoggio = new StringType();
	private StringType  inPortafoglioAppoggio = new StringType();
	
	private ListType	elencoFondi = new ListType(FondoModel.class);
	private boolean 	isPrimaAttivazione = true;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public StringType getTipologiaRicercaOICR(){
		if (this.tipoRicerca.equals("picpac")){
			if (this.tipoSottoscrizione.equals("PIC")
					|| this.tipoSottoscrizione.equals("PICDC")){
				return new StringType("pic");
			} else if (this.tipoSottoscrizione.equals("PAC")) {
				return new StringType("pac");			
			}
		}
		return new StringType(this.tipoRicerca.toString());
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public StringType getTipoRicerca() {
		return tipoRicerca;
	}
	public void setTipoRicerca(StringType tipoRicerca) {
		this.tipoRicerca = tipoRicerca;
	}
	public StringType getTipologia() {
		return tipologia;
	}
	public void setTipologia(StringType tipologia) {
		this.tipologia = tipologia;
	}
	public StringType getSocieta() {
		return societa;
	}
	public void setSocieta(StringType societa) {
		this.societa = societa;
	}
	public StringType getDenominazione() {
		return denominazione;
	}
	public void setDenominazione(StringType denominazione) {
		this.denominazione = denominazione;
	}
	public ListType getElencoFondi() {
		return elencoFondi;
	}
	public void setElencoFondi(ListType elencoFondi) {
		this.elencoFondi = elencoFondi;
	}
	public boolean isPrimaAttivazione() {
		return isPrimaAttivazione;
	}
	public void setPrimaAttivazione(boolean isPrimaAttivazione) {
		this.isPrimaAttivazione = isPrimaAttivazione;
	}
	public StringType getInPortafoglio() {
		return inPortafoglio;
	}
	public void setInPortafoglio(StringType inPortafoglio) {
		this.inPortafoglio = inPortafoglio;
	}
	public StringType getCodiceProdotto() {
		return codiceProdotto;
	}
	public void setCodiceProdotto(StringType codiceProdotto) {
		this.codiceProdotto = codiceProdotto;
	}
	public StringType getFondiSelezionati() {
		return fondiSelezionati;
	}
	public void setFondiSelezionati(StringType fondiSelezionati) {
		this.fondiSelezionati = fondiSelezionati;
	}
	public StringType getTipologiaDoubleChance() {
		return tipologiaDoubleChance;
	}
	public void setTipologiaDoubleChance(StringType tipologiaDoubleChance) {
		this.tipologiaDoubleChance = tipologiaDoubleChance;
	}
	public StringType getTipologiaAppoggio() {
		return tipologiaAppoggio;
	}
	public void setTipologiaAppoggio(StringType tipologiaAppoggio) {
		this.tipologiaAppoggio = tipologiaAppoggio;
	}
	public StringType getSocietaAppoggio() {
		return societaAppoggio;
	}
	public void setSocietaAppoggio(StringType societaAppoggio) {
		this.societaAppoggio = societaAppoggio;
	}
	public StringType getDenominazioneAppoggio() {
		return denominazioneAppoggio;
	}
	public void setDenominazioneAppoggio(StringType denominazioneAppoggio) {
		this.denominazioneAppoggio = denominazioneAppoggio;
	}
	public StringType getInPortafoglioAppoggio() {
		return inPortafoglioAppoggio;
	}
	public void setInPortafoglioAppoggio(StringType inPortafoglioAppoggio) {
		this.inPortafoglioAppoggio = inPortafoglioAppoggio;
	}
	public StringType getNumeroContratto() {
		return numeroContratto;
	}
	public void setNumeroContratto(StringType numeroContratto) {
		this.numeroContratto = numeroContratto;
	}
	public StringType getCodProdottoPolizza() {
		return codProdottoPolizza;
	}
	public void setCodProdottoPolizza(StringType codProdottoPolizza) {
		this.codProdottoPolizza = codProdottoPolizza;
	}
	public StringType getFlagTrasformatoPic() {
		return flagTrasformatoPic;
	}
	public void setFlagTrasformatoPic(StringType flagTrasformatoPic) {
		this.flagTrasformatoPic = flagTrasformatoPic;
	}
	public StringType getTipoSottoscrizione() {
		return tipoSottoscrizione;
	}
	public void setTipoSottoscrizione(StringType tipoSottoscrizione) {
		this.tipoSottoscrizione = tipoSottoscrizione;
	}
}
