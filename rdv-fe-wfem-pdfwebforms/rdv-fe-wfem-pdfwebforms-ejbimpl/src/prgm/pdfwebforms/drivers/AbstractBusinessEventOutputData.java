package prgm.pdfwebforms.drivers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import prgm.pdfwebforms.core.PdfCodedMessage;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public abstract class AbstractBusinessEventOutputData extends AbstractEventOutputData{
	
	private ArrayList<String>  	errors = new ArrayList<String>();
	private ArrayList<String>  	warnings = new ArrayList<String>();
	private Map<String, String> errorsCodes = new HashMap<String, String>();
	private Map<String, String> warningsCodes = new HashMap<String, String>();

	/**************************************************************************************************
	 * Aggiunge un errore bloccante. Viene mostrato in popup all'avanti
	**************************************************************************************************/
	public void addError(String error){
		if(error.startsWith(PdfCodedMessage.MESSAGE_CODE_PREFIX)) {
			String code = error.substring(PdfCodedMessage.MESSAGE_CODE_PREFIX.length(), error.indexOf("_"));
			error = error.substring(error.indexOf("_")+1);
			errorsCodes.put(error, code);
		}
		if(!getErrors().contains(error))
			getErrors().add(error);
	}
	
	/**************************************************************************************************
	 * Aggiunge un elenco di errori bloccanti. Viene mostrato in popup all'avanti
	**************************************************************************************************/
	public void addErrors(ArrayList<String> errors){
		for(String error : errors)
			addError(error);
	}

	/**************************************************************************************************
	 * Aggiunge un errore non bloccante. Viene mostrato in popup all'avanti
	**************************************************************************************************/
	public void addWarning(String warning){
		if(warning.startsWith(PdfCodedMessage.MESSAGE_CODE_PREFIX)) {
			String code = warning.substring(PdfCodedMessage.MESSAGE_CODE_PREFIX.length(), warning.indexOf("_"));
			warning = warning.substring(warning.indexOf("_")+1);
			warningsCodes.put(warning, code);
		}
		if(!getWarnings().contains(warning))
			getWarnings().add(warning);
	}
	
	/**************************************************************************************************
	 * Aggiunge un elenco di errori non bloccanti. Viene mostrato in popup all'avanti
	**************************************************************************************************/
	public void addWarnings(ArrayList<String> warnings){
		for(String warning : warnings)
			addWarning(warning);
	}

	public ArrayList<String> getErrors() {
		return errors;
	}
	public void setErrors(ArrayList<String> errors) {
		this.errors = errors;
	}

	public ArrayList<String> getWarnings() {
		return warnings;
	}

	public void setWarnings(ArrayList<String> warnings) {
		this.warnings = warnings;
	}

	public Map<String, String> getErrorsCodes() {
		return errorsCodes;
	}

	public Map<String, String> getWarningsCodes() {
		return warningsCodes;
	}

}
