package prgm.pdfwebforms.drivers.io.reportadeguatezza;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class ProdottoReportAdeguatezzaModel extends CommandDataModel {

	private boolean doProdottoDefaultTranslation = true;
	
	private StringType 	descr = new StringType();
	private StringType 	prodotto = new StringType();
	private DoubleType 	controvalore = new DoubleType();				
	private DoubleType 	variazione = new DoubleType();
	private StringType 	operazione = new StringType();				
	private ListType	prodotti = new ListType(ProdottoReportAdeguatezzaModel.class);
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void addProdotto(ProdottoReportAdeguatezzaModel p){
		getProdotti().add(p);
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
	public boolean isDoProdottoDefaultTranslation() {
		return doProdottoDefaultTranslation;
	}
	public void setDoProdottoDefaultTranslation(boolean doProdottoDefaultTranslation) {
		this.doProdottoDefaultTranslation = doProdottoDefaultTranslation;
	}
	public StringType getOperazione() {
		return operazione;
	}
	public void setOperazione(StringType operazione) {
		this.operazione = operazione;
	}
	public ListType getProdotti() {
		return prodotti;
	}
	public void setProdotti(ListType prodotti) {
		this.prodotti = prodotti;
	}
}
