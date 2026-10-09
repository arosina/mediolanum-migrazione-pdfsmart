package prgm.pdfwebformsdrivers.personalpirvariazionepianopac.ver00001.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

public class ResiduoPolizzaModel extends CommandDataModel {

	private static final long serialVersionUID = 1L;

	private StringType numeroContratto = new StringType();
	private StringType codProdotto = new StringType();
	private IntegerType numVersamentiPrevisti = new IntegerType();
	private IntegerType numRateSostenute = new IntegerType();
	private IntegerType  numRateMancanti = new IntegerType();
	private DoubleType importoRata = new DoubleType();
	private DoubleType  totalePiano = new DoubleType();
	private DoubleType  versato	= new DoubleType();
	private DoubleType  residuo	= new DoubleType();
	private IntegerType frazionamento = new IntegerType();
	private StringType dataInizioPiano = new StringType();
	private ListType listaFondi = new ListType(ResiduoPolizzaFondiModel.class);
	private IntegerType progressivoMifid = new IntegerType();
	private IntegerType resultCode = new IntegerType();

	public StringType getNumeroContratto() {
		return numeroContratto;
	}
	public void setNumeroContratto(StringType numeroContratto) {
		this.numeroContratto = numeroContratto;
	}
	public StringType getCodProdotto() {
		return codProdotto;
	}
	public void setCodProdotto(StringType codProdotto) {
		this.codProdotto = codProdotto;
	}
	public IntegerType getNumVersamentiPrevisti() {
		return numVersamentiPrevisti;
	}
	public void setNumVersamentiPrevisti(IntegerType numVersamentiPrevisti) {
		this.numVersamentiPrevisti = numVersamentiPrevisti;
	}
	public IntegerType getNumRateSostenute() {
		return numRateSostenute;
	}
	public void setNumRateSostenute(IntegerType numRateSostenute) {
		this.numRateSostenute = numRateSostenute;
	}
	public IntegerType getNumRateMancanti() {
		return numRateMancanti;
	}
	public void setNumRateMancanti(IntegerType numRateMancanti) {
		this.numRateMancanti = numRateMancanti;
	}
	public DoubleType getImportoRata() {
		return importoRata;
	}
	public void setImportoRata(DoubleType importoRata) {
		this.importoRata = importoRata;
	}
	public DoubleType getTotalePiano() {
		return totalePiano;
	}
	public void setTotalePiano(DoubleType totalePiano) {
		this.totalePiano = totalePiano;
	}
	public DoubleType getVersato() {
		return versato;
	}
	public void setVersato(DoubleType versato) {
		this.versato = versato;
	}
	public DoubleType getResiduo() {
		return residuo;
	}
	public void setResiduo(DoubleType residuo) {
		this.residuo = residuo;
	}
	//il servizio di ACN coincide con il numero rate annue 
	public IntegerType getFrazionamento() {		
		return frazionamento;
	}
	public void setFrazionamento(IntegerType frazionamento) {
		this.frazionamento = frazionamento;
	}
	public StringType getDataInizioPiano() {
		return dataInizioPiano;
	}
	public void setDataInizioPiano(StringType dataInizioPiano) {
		this.dataInizioPiano = dataInizioPiano;
	}
	public ListType getListaFondi() {
		return listaFondi;
	}
	public void setListaFondi(ListType listaFondi) {
		this.listaFondi = listaFondi;
	}
	public IntegerType getProgressivoMifid() {
		return progressivoMifid;
	}
	public void setProgressivoMifid(IntegerType progressivoMifid) {
		this.progressivoMifid = progressivoMifid;
	}
	public IntegerType getResultCode() {
		return resultCode;
	}
	public void setResultCode(IntegerType resultCode) {
		this.resultCode = resultCode;
	}

}
