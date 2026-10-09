package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.autocompletion;

import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.dataentryutil.AbstractAutocompleteModel;
import prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.util.Utility;

/***********************************************************************************************/
/***********************************************************************************************/
public class NumeroPolizzaAutoCompleteModel extends AbstractAutocompleteModel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -9220662625131202715L;
	
	private BooleanType	findOnlyPAC					= new BooleanType();
	private StringType  codProdotto 				= new StringType();
	private StringType  descrizioneProdotto 		= new StringType();
	private StringType  codProdottoPolizza	 		= new StringType();
	private StringType  numeroContratto 	 		= new StringType();
	private StringType  numeroPolizza		 		= new StringType();
	private StringType  codiceAgente         		= new StringType();
	private StringType  ndgCliente  		 		= new StringType();
	private StringType  ruolo           			= new StringType();		
	private StringType  nomeCliente   	     		= new StringType();
	private StringType  cognomeCliente   	 		= new StringType();
	private DateType 	dataEmissione		 		= new DateType();
	private StringType  formaContrattuale  	 		= new StringType();
	private StringType  flagTrasformatoPic   		= new StringType();
	private DoubleType  controvalorePolizza  		= new DoubleType();
	private StringType  scudoFiscale     			= new StringType();
	
	public StringType getScudoFiscale() {
		return scudoFiscale;
	}
	public void setScudoFiscale(StringType scudoFiscale) {
		this.scudoFiscale = scudoFiscale;
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String autocompletionLabel(){
		return getDescrizioneProdotto().toString() + " " + getFormaContrattuale().toString() + " " + "Nr. " + getNumeroPolizza().toString() + " Controvalore: " + Utility.formattaImporto(getControvalorePolizza().doubleValue()) + "&euro;";
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String autocompletionValue(){
		return getNumeroPolizza().toString();
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String propertyToString(String propName){
		return readProperty(propName) == null ? "" : readProperty(propName).toString();
	}
	public StringType getCodProdotto() {
		return codProdotto;
	}
	public void setCodProdotto(StringType codProdotto) {
		this.codProdotto = codProdotto;
	}
	public StringType getDescrizioneProdotto() {
		return descrizioneProdotto;
	}
	public void setDescrizioneProdotto(StringType descrizioneProdotto) {
		this.descrizioneProdotto = descrizioneProdotto;
	}
	public StringType getNumeroContratto() {
		return numeroContratto;
	}
	public void setNumeroContratto(StringType numeroContratto) {
		this.numeroContratto = numeroContratto;
	}
	public StringType getNumeroPolizza() {
		return numeroPolizza;
	}
	public void setNumeroPolizza(StringType numeroPolizza) {
		this.numeroPolizza = numeroPolizza;
	}
	public StringType getRuolo() {
		return ruolo;
	}
	public void setRuolo(StringType ruolo) {
		this.ruolo = ruolo;
	}
	public StringType getNomeCliente() {
		return nomeCliente;
	}
	public void setNomeCliente(StringType nomeCliente) {
		this.nomeCliente = nomeCliente;
	}
	public StringType getCognomeCliente() {
		return cognomeCliente;
	}
	public void setCognomeCliente(StringType cognomeCliente) {
		this.cognomeCliente = cognomeCliente;
	}
	public DateType getDataEmissione() {
		return dataEmissione;
	}
	public void setDataEmissione(DateType dataEmissione) {
		this.dataEmissione = dataEmissione;
	}

	public StringType getCodiceAgente() {
		return codiceAgente;
	}

	public void setCodiceAgente(StringType codiceAgente) {
		this.codiceAgente = codiceAgente;
	}

	public StringType getNdgCliente() {
		return ndgCliente;
	}

	public void setNdgCliente(StringType ndgCliente) {
		this.ndgCliente = ndgCliente;
	}

	public StringType getCodProdottoPolizza() {
		return codProdottoPolizza;
	}

	public void setCodProdottoPolizza(StringType codProdottoPolizza) {
		this.codProdottoPolizza = codProdottoPolizza;
	}

	public StringType getFormaContrattuale() {
		return formaContrattuale;
	}

	public void setFormaContrattuale(StringType formaContrattuale) {
		this.formaContrattuale = formaContrattuale;
	}

	public StringType getFlagTrasformatoPic() {
		return flagTrasformatoPic;
	}

	public void setFlagTrasformatoPic(StringType flagTrasformatoPic) {
		this.flagTrasformatoPic = flagTrasformatoPic;
	}
	public DoubleType getControvalorePolizza() {
		return controvalorePolizza;
	}

	public void setControvalorePolizza(DoubleType controvalorePolizza) {
		this.controvalorePolizza = controvalorePolizza;
	}
	public BooleanType getFindOnlyPAC() {
		return findOnlyPAC;
	}
	public void setFindOnlyPAC(BooleanType findOnlyPAC) {
		this.findOnlyPAC = findOnlyPAC;
	}
}
