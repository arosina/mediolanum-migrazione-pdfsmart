package prgm.pdfwebforms.drivers.io.mifid;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.StringType;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class CompartoMifidModel extends CommandDataModel{

	private StringType      codiceTipoProdotto = new StringType();
    private StringType      codiceTitolo = new StringType();
	private StringType      indicativoDiEmissione = new StringType();
    private StringType      tariffa = new StringType();
    private StringType      codiceFondo = new StringType();
    private StringType      profilo = new StringType();
    private StringType      categoria = new StringType();
    private StringType      sottocategoria = new StringType();
    private StringType      convenzione = new StringType();
    private StringType      codiceOPV = new StringType();
    private DoubleType      importo = new DoubleType();
    private StringType      divisa = new StringType();
    private DateType        dataScadenzaContratto = new DateType();
    private StringType      fcontr = new StringType(); // Forma contrattuale (Pic / Pac)
    private DoubleType      versamentoIniziale = new DoubleType();
    private DoubleType      derogaCommissionale = new DoubleType();
    private StringType      strategia = new StringType();
    private StringType      codiceFondoPartenzaStrategia = new StringType();
    private DoubleType      importoAggiuntivoPAC = new DoubleType();
    private DoubleType      importoRataPAC = new DoubleType();
    private StringType      frequenzaRataPAC = new StringType();
    private DoubleType      durataPianoPAC = new DoubleType();
    
    /**************************************************/
    /**************************************************/
    public StringType getDurataPianoPACAsStringType() {
    	if (getDurataPianoPAC().isNull())
    		return new StringType();
    	
    	return new StringType(getDurataPianoPAC().bigValue().toString());
    }
    
	public StringType getCodiceTipoProdotto() {
		return codiceTipoProdotto;
	}
	public void setCodiceTipoProdotto(StringType codiceTipoProdotto) {
		this.codiceTipoProdotto = codiceTipoProdotto;
	}
	public StringType getCodiceTitolo() {
		return codiceTitolo;
	}
	public void setCodiceTitolo(StringType codiceTitolo) {
		this.codiceTitolo = codiceTitolo;
	}
	public StringType getIndicativoDiEmissione() {
		return indicativoDiEmissione;
	}
	public void setIndicativoDiEmissione(StringType indicativoDiEmissione) {
		this.indicativoDiEmissione = indicativoDiEmissione;
	}
	public StringType getTariffa() {
		return tariffa;
	}
	public void setTariffa(StringType tariffa) {
		this.tariffa = tariffa;
	}
	public StringType getCodiceFondo() {
		return codiceFondo;
	}
	public void setCodiceFondo(StringType codiceFondo) {
		this.codiceFondo = codiceFondo;
	}
	public StringType getProfilo() {
		return profilo;
	}
	public void setProfilo(StringType profilo) {
		this.profilo = profilo;
	}
	public StringType getCategoria() {
		return categoria;
	}
	public void setCategoria(StringType categoria) {
		this.categoria = categoria;
	}
	public StringType getSottocategoria() {
		return sottocategoria;
	}
	public void setSottocategoria(StringType sottocategoria) {
		this.sottocategoria = sottocategoria;
	}
	public StringType getConvenzione() {
		return convenzione;
	}
	public void setConvenzione(StringType convenzione) {
		this.convenzione = convenzione;
	}
	public StringType getCodiceOPV() {
		return codiceOPV;
	}
	public void setCodiceOPV(StringType codiceOPV) {
		this.codiceOPV = codiceOPV;
	}
	public DoubleType getImporto() {
		return importo;
	}
	public void setImporto(DoubleType importo) {
		this.importo = importo;
	}
	public StringType getDivisa() {
		return divisa;
	}
	public void setDivisa(StringType divisa) {
		this.divisa = divisa;
	}
	public DateType getDataScadenzaContratto() {
		return dataScadenzaContratto;
	}
	public void setDataScadenzaContratto(DateType dataScadenzaContratto) {
		this.dataScadenzaContratto = dataScadenzaContratto;
	}
	public StringType getFcontr() {
		return fcontr;
	}
	public void setFcontr(StringType fcontr) {
		this.fcontr = fcontr;
	}
	public DoubleType getDerogaCommissionale() {
		return derogaCommissionale;
	}
	public void setDerogaCommissionale(DoubleType derogaCommissionale) {
		this.derogaCommissionale = derogaCommissionale;
	}
	public StringType getStrategia() {
		return strategia;
	}
	public void setStrategia(StringType strategia) {
		this.strategia = strategia;
	}
	public StringType getCodiceFondoPartenzaStrategia() {
		return codiceFondoPartenzaStrategia;
	}
	public void setCodiceFondoPartenzaStrategia(StringType codiceFondoPartenzaStrategia) {
		this.codiceFondoPartenzaStrategia = codiceFondoPartenzaStrategia;
	}
	public DoubleType getImportoAggiuntivoPAC() {
		return importoAggiuntivoPAC;
	}
	public void setImportoAggiuntivoPAC(DoubleType importoAggiuntivoPAC) {
		this.importoAggiuntivoPAC = importoAggiuntivoPAC;
	}
	public DoubleType getImportoRataPAC() {
		return importoRataPAC;
	}
	public void setImportoRataPAC(DoubleType importoRataPAC) {
		this.importoRataPAC = importoRataPAC;
	}
	public StringType getFrequenzaRataPAC() {
		return frequenzaRataPAC;
	}
	public void setFrequenzaRataPAC(StringType frequenzaRataPAC) {
		this.frequenzaRataPAC = frequenzaRataPAC;
	}
	public DoubleType getDurataPianoPAC() {
		return durataPianoPAC;
	}
	public void setDurataPianoPAC(DoubleType durataPianoPAC) {
		this.durataPianoPAC = durataPianoPAC;
	}
	public DoubleType getVersamentoIniziale() {
		return versamentoIniziale;
	}
	public void setVersamentoIniziale(DoubleType versamentoIniziale) {
		this.versamentoIniziale = versamentoIniziale;
	}
 
}
