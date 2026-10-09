package prgm.pdfwebformspolizzeutil.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

public class StrategiaInvestimentoOutputModel extends CommandDataModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private StringType 	codProdotto 	= new StringType();
	private StringType 	codContratto 	= new StringType();
	private StringType 	codServizio 	= new StringType();
	private StringType 	contrNPoliz 	= new StringType();
	private StringType 	tipoServz 		= new StringType();
	private IntegerType numRatePiano 	= new IntegerType();
	private IntegerType numRateVersate 	= new IntegerType();
	private IntegerType numRateResid 	= new IntegerType();
	private DoubleType 	impoRataPiano 	= new DoubleType();
	private DoubleType 	impTotPiano 	= new DoubleType();
	private DoubleType 	impPianoVersato = new DoubleType();
	private DoubleType 	impPianoResid 	= new DoubleType();    
    private ListType 	dettaglio = new ListType(StrategiaInvestimentoDettaglioModel.class);
    
	public StringType getCodProdotto() {
		return codProdotto;
	}
	public StringType getCodContratto() {
		return codContratto;
	}
	public StringType getCodServizio() {
		return codServizio;
	}
	public StringType getContrNPoliz() {
		return contrNPoliz;
	}
	public StringType getTipoServz() {
		return tipoServz;
	}
	public IntegerType getNumRatePiano() {
		return numRatePiano;
	}
	public IntegerType getNumRateVersate() {
		return numRateVersate;
	}
	public IntegerType getNumRateResid() {
		return numRateResid;
	}
	public DoubleType getImpoRataPiano() {
		return impoRataPiano;
	}
	public DoubleType getImpTotPiano() {
		return impTotPiano;
	}
	public DoubleType getImpPianoVersato() {
		return impPianoVersato;
	}
	public DoubleType getImpPianoResid() {
		return impPianoResid;
	}
	
	public void setCodProdotto(StringType codProdotto) {
		this.codProdotto = codProdotto;
	}
	public void setCodContratto(StringType codContratto) {
		this.codContratto = codContratto;
	}
	public void setCodServizio(StringType codServizio) {
		this.codServizio = codServizio;
	}
	public void setContrNPoliz(StringType contrNPoliz) {
		this.contrNPoliz = contrNPoliz;
	}
	public void setTipoServz(StringType tipoServz) {
		this.tipoServz = tipoServz;
	}
	public void setNumRatePiano(IntegerType numRatePiano) {
		this.numRatePiano = numRatePiano;
	}
	public void setNumRateVersate(IntegerType numRateVersate) {
		this.numRateVersate = numRateVersate;
	}
	public void setNumRateResid(IntegerType numRateResid) {
		this.numRateResid = numRateResid;
	}
	public void setImpoRataPiano(DoubleType impoRataPiano) {
		this.impoRataPiano = impoRataPiano;
	}
	public void setImpTotPiano(DoubleType impTotPiano) {
		this.impTotPiano = impTotPiano;
	}
	public void setImpPianoVersato(DoubleType impPianoVersato) {
		this.impPianoVersato = impPianoVersato;
	}
	public void setImpPianoResid(DoubleType impPianoResid) {
		this.impPianoResid = impPianoResid;
	}
	public ListType getDettaglio() {
		return dettaglio;
	}
	public void setDettaglio(ListType dettaglio) {
		this.dettaglio = dettaglio;
	}
	
			
}
