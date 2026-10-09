package prgm.pdfwebformsutil.drivers.autocompletion.nazione;

import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.dataentryutil.AbstractAutocompleteModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class NazioneAutocompleteModel extends AbstractAutocompleteModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1852878368050080801L;
	private StringType nazione = new StringType();


	public StringType getNazione() {
		return nazione;
	}


	public void setNazione(StringType nazione) {
		this.nazione = nazione;
	}




	/***********************************************************************************************/
	/***********************************************************************************************/
	public String propertyToString(String propName) {
		return readProperty(propName) == null ? "" : readProperty(propName).toString();
	}

}
