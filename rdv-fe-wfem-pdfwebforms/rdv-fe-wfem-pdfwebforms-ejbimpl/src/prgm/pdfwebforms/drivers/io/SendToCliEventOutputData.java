package prgm.pdfwebforms.drivers.io;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class SendToCliEventOutputData{

	private boolean 	skipLegameConRda = false;				// Permette di non creare il legame tra il pdf e l'RDA
	private Boolean 	putOnDeadEndProcessBatchQueue = null;	// Per pilotare l'inserimento nella coda batch "DeadEnd"
	private String 		errorMessage = null;
	
	public boolean isSkipLegameConRda() {
		return skipLegameConRda;
	}
	public void setSkipLegameConRda(boolean skipLegameConRda) {
		this.skipLegameConRda = skipLegameConRda;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public Boolean getPutOnDeadEndProcessBatchQueue() {
		return putOnDeadEndProcessBatchQueue;
	}
	public void setPutOnDeadEndProcessBatchQueue(Boolean putOnDeadEndProcessBatchQueue) {
		this.putOnDeadEndProcessBatchQueue = putOnDeadEndProcessBatchQueue;
	}
}
