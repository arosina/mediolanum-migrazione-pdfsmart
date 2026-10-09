package prgm.pdfwebformsutil.drivers.autocompletion.comune;

import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.pdfwebforms.dataentryutil.AbstractAutocompleteModel;

public class ComuneNascitaAutocompleteModel  extends AbstractAutocompleteModel {

	private StringType  nazioneComune = new StringType();
	private StringType  provinciaComune = new StringType();
	private StringType  comune = new StringType();

	/***********************************************************************************************/
	/***********************************************************************************************/
	public String autocompleteLabel(){
		return Tools.capitalize(getComune().toString())+" ("+getProvinciaComune()+")";
	}

	public StringType getProvinciaComune() {
		return provinciaComune;
	}

	public void setProvinciaComune(StringType provinciaComune) {
		this.provinciaComune = provinciaComune;
	}


	public StringType getComune() {
		return comune;
	}

	public void setComune(StringType comune) {
		this.comune = comune;
	}

	public StringType getNazioneComune() {
		return nazioneComune;
	}

	public void setNazioneComune(StringType nazioneComune) {
		this.nazioneComune = nazioneComune;
	}

}

