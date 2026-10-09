package prgm.pdfwebforms.drivers.io.reportadeguatezza;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class DispositivaReportAdeguatezzaModel extends CommandDataModel {
	
	public static class Operazioni{
		public static String SOTTOSCRIZIONE_INIZIALE 	= "2";
		public static String SOTTOSCRIZIONE_AGGIUNTIVA 	= "3";
		public static String DISINVESTIMENTO_PARZIALE 	= "10";
		public static String DISINVESTIMENTO_TOTALE 	= "11";
		public static String SWITCH 					= "9";
		public static String CONVERSIONE 				= "5";
	}
	
	private boolean doProdottoDefaultTranslation = true;
	private boolean doDerogaDefaultTranslation = true;
	private boolean addToReport = true;

	private StringType 	descr = new StringType();
	private StringType 	prodotto = new StringType();
	private StringType 	contratto = new StringType();						
	private StringType 	operazione = new StringType();				
	private DoubleType 	controvalore = new DoubleType();				
	private DoubleType 	variazione = new DoubleType();				
	private StringType 	deroga = new StringType();

	private StringType 	contrattoRimborsoSwitch = new StringType();						
	private StringType 	operazioneRimborsoSwitch = new StringType();				
	
	// Se l'elenco dei soggetti viene lasciato vuoto il motore utilizza i soggetti coinvolti nel pdf
	private ListType 	soggetti = new ListType(SoggettoReportAdeguatezzaModel.class);
	private ListType	prodotti = new ListType(ProdottoReportAdeguatezzaModel.class);

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void addSoggetto(SoggettoReportAdeguatezzaModel sogg){
		getSoggetti().add(sogg);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void addProdotto(ProdottoReportAdeguatezzaModel p){
		getProdotti().add(p);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isSwitch(){
		return !getContrattoRimborsoSwitch().isNull();
	}
	
	public StringType getDescr() {
		return descr;
	}

	public void setDescr(StringType descr) {
		this.descr = descr;
	}

	public StringType getProdotto() {
		return prodotto;
	}

	public void setProdotto(StringType prodotto) {
		this.prodotto = prodotto;
	}

	public StringType getContratto() {
		return contratto;
	}

	public void setContratto(StringType contratto) {
		this.contratto = contratto;
	}

	public StringType getOperazione() {
		return operazione;
	}

	public void setOperazione(StringType operazione) {
		this.operazione = operazione;
	}

	public DoubleType getControvalore() {
		return controvalore;
	}

	public void setControvalore(DoubleType controvalore) {
		this.controvalore = controvalore;
	}

	public DoubleType getVariazione() {
		return variazione;
	}

	public void setVariazione(DoubleType variazione) {
		this.variazione = variazione;
	}

	public StringType getDeroga() {
		return deroga;
	}

	public void setDeroga(StringType deroga) {
		this.deroga = deroga;
	}

	public ListType getSoggetti() {
		return soggetti;
	}

	public void setSoggetti(ListType soggetti) {
		this.soggetti = soggetti;
	}

	public ListType getProdotti() {
		return prodotti;
	}

	public void setProdotti(ListType prodotti) {
		this.prodotti = prodotti;
	}

	public boolean isDoProdottoDefaultTranslation() {
		return doProdottoDefaultTranslation;
	}

	public void setDoProdottoDefaultTranslation(boolean doProdottoDefaultTranslation) {
		this.doProdottoDefaultTranslation = doProdottoDefaultTranslation;
	}

	public boolean isAddToReport() {
		return addToReport;
	}

	public void setAddToReport(boolean addToReport) {
		this.addToReport = addToReport;
	}

	public boolean isDoDerogaDefaultTranslation() {
		return doDerogaDefaultTranslation;
	}

	public void setDoDerogaDefaultTranslation(boolean doDerogaDefaultTranslation) {
		this.doDerogaDefaultTranslation = doDerogaDefaultTranslation;
	}

	public StringType getContrattoRimborsoSwitch() {
		return contrattoRimborsoSwitch;
	}

	public void setContrattoRimborsoSwitch(StringType contrattoRimborsoSwitch) {
		this.contrattoRimborsoSwitch = contrattoRimborsoSwitch;
	}

	public StringType getOperazioneRimborsoSwitch() {
		return operazioneRimborsoSwitch;
	}

	public void setOperazioneRimborsoSwitch(StringType operazioneRimborsoSwitch) {
		this.operazioneRimborsoSwitch = operazioneRimborsoSwitch;
	}

}
