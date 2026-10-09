package prgm.pdfwebformsutil.drivers.autocompletion.abicab;

import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.dataentryutil.AbstractAutocompleteModel;
import prgm.pdfwebformsutil.drivers.util.Util;

/***********************************************************************************************/
/***********************************************************************************************/
public class CabFilialeAutocompleteModel extends AbstractAutocompleteModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4770255233731114806L;
	private StringType inputAbi = new StringType();
    private StringType inputCab = new StringType();
    private StringType inputFiliale = new StringType();    
    private StringType inputDenominazione = new StringType();    
    
	/***********************************************************************************************/    
	/***********************************************************************************************/
	public String autocompletionLabel(){		
		String cab =  propertyToString("cab");
		String filiale = Util.filtraCaratteriSpeciali(propertyToString("filiale"));
		
		return String.format("%s %s", cab, filiale);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String descrizioneFiliale(){
		String cab =  propertyToString("cab");
		String filiale = Util.filtraCaratteriSpeciali(propertyToString("filiale"));
		
		return String.format("%s %s", cab, filiale);		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public String propertyToString(String propName){
		return readProperty(propName) == null ? "" : readProperty(propName).toString();
	}

	public StringType getInputAbi() {
		return inputAbi;
	}
	
	public void setInputAbi(StringType inputAbi) {
		this.inputAbi = inputAbi;
	}
	
	public StringType getInputCab() {
		return inputCab;
	}		
	
	public void setInputCab(StringType inputCab) {
		this.inputCab = inputCab;
	}

	public StringType getInputDenominazione() {
		return inputDenominazione;
	}

	public void setInputDenominazione(StringType inputDenominazione) {
		this.inputDenominazione = inputDenominazione;
	}

	public StringType getInputFiliale() {
		return inputFiliale;
	}

	public void setInputFiliale(StringType inputFiliale) {
		this.inputFiliale = inputFiliale;
	}	
}
