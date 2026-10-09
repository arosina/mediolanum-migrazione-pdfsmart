package prgm.pdfwebforms.drivers.io;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class FinalEventOutputData{
	
	private String 	errorMessage = null;
	private Boolean putOnSignedProcessBatchQueue = null;			// Per pilotare l'inserimento nella coda batch "Signed"
	private Boolean putOnPostCompletionProcessBatchQueue = null;	// Per pilotare l'inserimento nella coda batch "Post Completion"

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Boolean getPutOnSignedProcessBatchQueue() {
		return putOnSignedProcessBatchQueue;
	}

	public void setPutOnSignedProcessBatchQueue(Boolean putOnSignedProcessBatchQueue) {
		this.putOnSignedProcessBatchQueue = putOnSignedProcessBatchQueue;
	}

	public Boolean getPutOnPostCompletionProcessBatchQueue() {
		return putOnPostCompletionProcessBatchQueue;
	}

	public void setPutOnPostCompletionProcessBatchQueue(Boolean putOnPostCompletionProcessBatchQueue) {
		this.putOnPostCompletionProcessBatchQueue = putOnPostCompletionProcessBatchQueue;
	}

}
