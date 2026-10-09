package prgm.pdfwebformsutil.drivers.autocompletion.toponimo;

import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.dataentryutil.AbstractAutocompleteModel;

/*******************************************************************/
/*******************************************************************/
public class ToponimoAutocompleteModel extends AbstractAutocompleteModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2947587984186008866L;
	private StringType  codiceToponimo = new StringType();
	private StringType  descrizioneToponimo = new StringType();

	public StringType getDescrizioneToponimo() {
		return descrizioneToponimo;
	}
	public void setDescrizioneToponimo(StringType descrizioneToponimo) {
		this.descrizioneToponimo = descrizioneToponimo;
	}
	public StringType getCodiceToponimo() {
		return codiceToponimo;
	}
	public void setCodiceToponimo(StringType codiceToponimo) {
		this.codiceToponimo = codiceToponimo;
	}
	
}
