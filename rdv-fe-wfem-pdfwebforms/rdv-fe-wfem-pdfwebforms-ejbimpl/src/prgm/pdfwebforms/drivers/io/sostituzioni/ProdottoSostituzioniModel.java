package prgm.pdfwebforms.drivers.io.sostituzioni;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class ProdottoSostituzioniModel extends CommandDataModel{

	private boolean doProdottoDefaultTranslation = true; // Attiva o meno la transcodifica automatica da prodotto a codice surrogato per il campo prodotto
	private boolean doProdottoPadreDefaultTranslation = true; // Attiva o meno la transcodifica automatica da prodotto a codice surrogato per il campo prodottoPadre
	
	// I campi "prodotto" e "prodottoPadre" corrispondono ai codiciSurrogati in input al servizio di verifica sostituzioni.
	private StringType  prodotto = new StringType(); // Quando "doProdottoDefaultTranslation = true" viene transcodificato in codice surrogato altrimenti va impostato direttamente con lo specifico codice surrogato.
	private StringType  prodottoPadre = new StringType();// Quando "doProdottoPadreDefaultTranslation = true" viene transcodificato in codice surrogato altrimenti va impostato direttamente con lo specifico codice surrogato.
	
	private DoubleType  controvalore = new DoubleType();
	private IntegerType progressivo = new IntegerType();
	private IntegerType progressivoPadre = new IntegerType();
	private StringType  formaContrattuale = new StringType();
	
	public boolean isDoProdottoDefaultTranslation() {
		return doProdottoDefaultTranslation;
	}
	public void setDoProdottoDefaultTranslation(boolean doProdottoDefaultTranslation) {
		this.doProdottoDefaultTranslation = doProdottoDefaultTranslation;
	}
	public boolean isDoProdottoPadreDefaultTranslation() {
		return doProdottoPadreDefaultTranslation;
	}
	public void setDoProdottoPadreDefaultTranslation(boolean doProdottoPadreDefaultTranslation) {
		this.doProdottoPadreDefaultTranslation = doProdottoPadreDefaultTranslation;
	}
	public StringType getProdotto() {
		return prodotto;
	}
	public void setProdotto(StringType prodotto) {
		this.prodotto = prodotto;
	}
	public StringType getProdottoPadre() {
		return prodottoPadre;
	}
	public void setProdottoPadre(StringType prodottoPadre) {
		this.prodottoPadre = prodottoPadre;
	}
	public DoubleType getControvalore() {
		return controvalore;
	}
	public void setControvalore(DoubleType controvalore) {
		this.controvalore = controvalore;
	}
	public IntegerType getProgressivo() {
		return progressivo;
	}
	public void setProgressivo(IntegerType progressivo) {
		this.progressivo = progressivo;
	}
	public IntegerType getProgressivoPadre() {
		return progressivoPadre;
	}
	public void setProgressivoPadre(IntegerType progressivoPadre) {
		this.progressivoPadre = progressivoPadre;
	}
	public StringType getFormaContrattuale() {
		return formaContrattuale;
	}
	public void setFormaContrattuale(StringType formaContrattuale) {
		this.formaContrattuale = formaContrattuale;
	}


}
