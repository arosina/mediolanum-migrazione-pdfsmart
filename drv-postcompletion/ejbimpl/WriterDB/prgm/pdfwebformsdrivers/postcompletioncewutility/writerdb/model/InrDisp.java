package prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.model;

import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebformsdrivers.postcompletioncewutility.writerdb.input.InrDispInput;

/*********************************************************************************/
/*********************************************************************************/
public class InrDisp extends InrDispInput {

    private StringType  tipoDisposizione		= new StringType();
    private StringType  socProdotto 			= new StringType();
    private StringType  tipoProdotto 			= new StringType();
    
	public StringType getTipoDisposizione() {
		return tipoDisposizione;
	}
	public void setTipoDisposizione(StringType tipoDisposizione) {
		this.tipoDisposizione = tipoDisposizione;
	}
	public StringType getSocProdotto() {
		return socProdotto;
	}
	public void setSocProdotto(StringType socProdotto) {
		this.socProdotto = socProdotto;
	}
	public StringType getTipoProdotto() {
		return tipoProdotto;
	}
	public void setTipoProdotto(StringType tipoProdotto) {
		this.tipoProdotto = tipoProdotto;
	}

    
}
