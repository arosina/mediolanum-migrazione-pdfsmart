package prgm.pdfwebforms.drivers.io;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class CompilationModeSelectionEventOutputData{
	
	private String 	errorMessage = null;
	private String 	warningMessage = null;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getWarningMessage() {
		return warningMessage;
	}

	public void setWarningMessage(String warningMessage) {
		this.warningMessage = warningMessage;
	}

}
