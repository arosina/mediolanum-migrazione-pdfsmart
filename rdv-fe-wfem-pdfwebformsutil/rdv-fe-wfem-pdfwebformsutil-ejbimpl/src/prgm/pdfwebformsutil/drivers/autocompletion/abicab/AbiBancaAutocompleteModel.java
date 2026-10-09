package prgm.pdfwebformsutil.drivers.autocompletion.abicab;

import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.dataentryutil.AbstractAutocompleteModel;
import prgm.pdfwebformsutil.drivers.util.Util;

/***********************************************************************************************/
/***********************************************************************************************/
public class AbiBancaAutocompleteModel extends AbstractAutocompleteModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8073579283303860828L;
	private StringType inputAbi = new StringType();
	private StringType inputRagioneSociale = new StringType();
	private StringType inputDenominazione = new StringType();

	/***********************************************************************************************/
	/***********************************************************************************************/
	public String autocompletionLabel() {
		String abi = propertyToString("abi");
		String ragioneSociale = Util.filtraCaratteriSpeciali(propertyToString("ragioneSociale"));

		return String.format("%s %s", abi, ragioneSociale);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public String descrizioneBanca() {
		String abi = propertyToString("abi");
		String ragioneSociale = Util.filtraCaratteriSpeciali(propertyToString("ragioneSociale"));

		return String.format("%s %s", abi, ragioneSociale);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public String propertyToString(String propName) {
		return readProperty(propName) == null ? "" : readProperty(propName).toString();
	}

	public StringType getInputAbi() {
		return inputAbi;
	}

	public void setInputAbi(StringType inputAbi) {
		this.inputAbi = inputAbi;
	}

	public StringType getInputDenominazione() {
		return inputDenominazione;
	}

	public void setInputDenominazione(StringType inputDenominazione) {
		this.inputDenominazione = inputDenominazione;
	}

	public StringType getInputRagioneSociale() {
		return inputRagioneSociale;
	}

	public void setInputRagioneSociale(StringType inputRagioneSociale) {
		this.inputRagioneSociale = inputRagioneSociale;
	}
}
