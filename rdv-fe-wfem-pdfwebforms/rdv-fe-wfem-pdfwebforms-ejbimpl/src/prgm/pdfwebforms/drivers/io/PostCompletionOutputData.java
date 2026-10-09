package prgm.pdfwebforms.drivers.io;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class PostCompletionOutputData{
	
	public static String OK 		= "OK";			// Goto next step (if exist)
	public static String WARNING	= "WARNING";	// Goto next step but something goes wrong
	public static String RETRY 		= "RETRY";		// Retry current step in next process execution
	public static String STOP		= "STOP";		// Stop steps execution
	public static String ERROR		= "ERROR";		// Stop steps execution for error
	
	private String 	stepDescription = "";
	private String 	resultCode = OK;
	private String	resultMessage = "";
	
	public String getResultCode() {
		return resultCode;
	}
	public void setResultCode(String resultActionCode) {
		this.resultCode = resultActionCode;
	}
	public String getResultMessage() {
		return resultMessage;
	}
	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}
	public String getStepDescription() {
		return stepDescription;
	}
	public void setStepDescription(String stepDescription) {
		this.stepDescription = stepDescription;
	}
	
}
