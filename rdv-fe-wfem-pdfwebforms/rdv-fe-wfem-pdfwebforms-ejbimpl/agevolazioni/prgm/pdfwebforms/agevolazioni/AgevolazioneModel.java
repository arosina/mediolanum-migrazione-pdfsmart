package prgm.pdfwebforms.agevolazioni;

import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.dataentryutil.AbstractAutocompleteModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class AgevolazioneModel extends AbstractAutocompleteModel{

	public static String DICITURA_DEROGA_SOSTITUZIONE = "Esenzione 100%";
	public static String PERCENTUALE_DEROGA_SOSTITUZIONE = "100";
	public static String PERCENTUALE_DEROGA_DIPENDENTI = "100";
	
	// Tabella CEPE_CRLEVE_MOV_DEROG_GEST
	private IntegerType idAgevolazione = new IntegerType();							// ID_DEROGA
	private StringType	codProdottoDispositiva = new StringType(); 					// PROD_C_PROD
	private StringType	codProdottoDispositivaPartenza = new StringType(); 			// PROD_C_PROD_PART
	private StringType  codAgente = new StringType();								// COD_AGENTE
	private StringType  codCliente = new StringType();								// COD_CLIENTE
	private StringType  tipologiaAgevolazione = new StringType();					// TIPO_DEROGA
	private StringType  descrTipologiaAgevolazione = new StringType();				// Derivata da CEPE_CRLEVE_DOMINIO_DEROGA=TIPO_DEROGA
	private StringType  codiceAgevolazione = new StringType();						// CODICE_CONV
	private StringType  modalitaVersamentoAgevolazione = new StringType();			// MODAL_VERS
	private StringType  percentualeAgevolazione = new StringType();					// PCT_ESENZ
	private DoubleType  importoAgevolazione = new DoubleType();						// IMPORTO_INVEST
	private BooleanType isSwitch = new BooleanType();								// IS_SWITCH
	private StringType  numeroContrattoPartenza = new StringType();					// MANDATO_PART
	private StringType  numeroContrattoDestinazione = new StringType();				// MANDATO_ARR
	private StringType  applicazioneDeroga = new StringType();						// APPL_DEROGA
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String getDescrizioneAgevolazione(){
		String desc = "";
		desc += getDescrTipologiaAgevolazione().isNull() ? getTipologiaAgevolazione().toString() : getDescrTipologiaAgevolazione().toString();
		if(!getPercentualeAgevolazione().isNull())
			desc += " - "+getPercentualeAgevolazione()+"%";
		if(!getModalitaVersamentoAgevolazione().isNull())
			desc += " - "+getModalitaVersamentoAgevolazione();
		if(!getApplicazioneDeroga().isNull() && "pac".equalsIgnoreCase(getModalitaVersamentoAgevolazione().toString()))
			desc += " - "+getApplicazioneDeroga();
		if(!getNumeroContrattoDestinazione().isNull())
			desc += " - contratto: "+getNumeroContrattoDestinazione();
		if(!getImportoAgevolazione().isNull())
			desc += " - importo: "+getImportoAgevolazione();
		if(!getIdAgevolazione().isNull())
			desc += " - id :"+getIdAgevolazione(); 
		return desc;
	}
	
	public IntegerType getIdAgevolazione() {
		return idAgevolazione;
	}
	public void setIdAgevolazione(IntegerType idAgevolazione) {
		this.idAgevolazione = idAgevolazione;
	}
	public StringType getTipologiaAgevolazione() {
		return tipologiaAgevolazione;
	}
	public void setTipologiaAgevolazione(StringType tipologiaAgevolazione) {
		this.tipologiaAgevolazione = tipologiaAgevolazione;
	}
	public StringType getCodiceAgevolazione() {
		return codiceAgevolazione;
	}
	public void setCodiceAgevolazione(StringType codiceAgevolazione) {
		this.codiceAgevolazione = codiceAgevolazione;
	}
	public StringType getModalitaVersamentoAgevolazione() {
		return modalitaVersamentoAgevolazione;
	}
	public void setModalitaVersamentoAgevolazione(
			StringType modalitaVersamentoAgevolazione) {
		this.modalitaVersamentoAgevolazione = modalitaVersamentoAgevolazione;
	}
	public StringType getPercentualeAgevolazione() {
		return percentualeAgevolazione;
	}
	public void setPercentualeAgevolazione(StringType percentualeAgevolazione) {
		this.percentualeAgevolazione = percentualeAgevolazione;
	}
	public DoubleType getImportoAgevolazione() {
		return importoAgevolazione;
	}
	public void setImportoAgevolazione(DoubleType importoAgevolazione) {
		this.importoAgevolazione = importoAgevolazione;
	}

	public StringType getCodAgente() {
		return codAgente;
	}

	public void setCodAgente(StringType codAgente) {
		this.codAgente = codAgente;
	}

	public StringType getCodCliente() {
		return codCliente;
	}

	public void setCodCliente(StringType codCliente) {
		this.codCliente = codCliente;
	}

	public StringType getCodProdottoDispositiva() {
		return codProdottoDispositiva;
	}

	public void setCodProdottoDispositiva(StringType codProdottoDispositiva) {
		this.codProdottoDispositiva = codProdottoDispositiva;
	}

	public BooleanType getIsSwitch() {
		return isSwitch;
	}

	public void setIsSwitch(BooleanType isSwitch) {
		this.isSwitch = isSwitch;
	}

	public StringType getNumeroContrattoPartenza() {
		return numeroContrattoPartenza;
	}

	public void setNumeroContrattoPartenza(StringType numeroContrattoPartenza) {
		this.numeroContrattoPartenza = numeroContrattoPartenza;
	}

	public StringType getNumeroContrattoDestinazione() {
		return numeroContrattoDestinazione;
	}

	public void setNumeroContrattoDestinazione(
			StringType numeroContrattoDestinazione) {
		this.numeroContrattoDestinazione = numeroContrattoDestinazione;
	}

	public StringType getApplicazioneDeroga() {
		return applicazioneDeroga;
	}

	public void setApplicazioneDeroga(StringType applicazioneDeroga) {
		this.applicazioneDeroga = applicazioneDeroga;
	}

	public StringType getDescrTipologiaAgevolazione() {
		return descrTipologiaAgevolazione;
	}

	public void setDescrTipologiaAgevolazione(StringType descrTipologiaAgevolazione) {
		this.descrTipologiaAgevolazione = descrTipologiaAgevolazione;
	}

	public StringType getCodProdottoDispositivaPartenza() {
		return codProdottoDispositivaPartenza;
	}

	public void setCodProdottoDispositivaPartenza(
			StringType codProdottoDispositivaPartenza) {
		this.codProdottoDispositivaPartenza = codProdottoDispositivaPartenza;
	}

}
