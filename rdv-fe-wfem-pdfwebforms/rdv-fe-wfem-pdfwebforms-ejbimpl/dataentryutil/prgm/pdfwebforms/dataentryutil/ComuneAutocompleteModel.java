package prgm.pdfwebforms.dataentryutil;

import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

/*******************************************************************/
/*******************************************************************/
public class ComuneAutocompleteModel extends AbstractAutocompleteModel {

	private StringType  nazioneComune = new StringType();
	private StringType  descrNazioneComune = new StringType();
	private StringType  provinciaComune = new StringType();
	private StringType  capComune = new StringType();
	private StringType  comune = new StringType();

	/***********************************************************************************************/
	/***********************************************************************************************/
	public String autocompleteLabel(){
		return Tools.capitalize(getComune().toString())+", "+getCapComune()+" ("+getProvinciaComune()+")";
	}

	public StringType getProvinciaComune() {
		return provinciaComune;
	}

	public void setProvinciaComune(StringType provinciaComune) {
		this.provinciaComune = provinciaComune;
	}

	public StringType getCapComune() {
		return capComune;
	}

	public void setCapComune(StringType capComune) {
		this.capComune = capComune;
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

	public StringType getDescrNazioneComune() {
		return descrNazioneComune;
	}

	public void setDescrNazioneComune(StringType descrNazioneComune) {
		this.descrNazioneComune = descrNazioneComune;
	}

}
