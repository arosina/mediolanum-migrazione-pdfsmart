package prgm.pdfwebforms.drivers.io;

import prgm.pdfwebforms.pritmom.PritMomInfo;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class FreezeEventOutputData{
	
	private boolean skipLegameConRda = false;	// Permette di non creare il legame tra il pdf e l'RDA
	private String 	pdfsForMOM = null;			// Elenco dei pdf (numeri, a partire da 1, separati da virgola) che comporranno i pdf visto dagli operatori MOM (secretazione) 
	private PritMomInfo pritMomInfo = null;		// Da impostare per sovrascivere codProdotto e codOperazione prit sulla PDF_INSTANCE 
	private String 	errorMessage = null;
	private String 	codDispositivaBMEDFromDriver = null;

	private Boolean putOnSignedProcessBatchQueue = null;			// Per pilotare l'inserimento nella coda batch "Signed"
	private Boolean putOnPostCompletionProcessBatchQueue = null;	// Per pilotare l'inserimento nella coda batch "Post Completion"

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public String getPdfsForMOM() {
		return pdfsForMOM;
	}

	public void setPdfsForMOM(String pdfsForMOM) {
		this.pdfsForMOM = pdfsForMOM;
	}
	
	public PritMomInfo getPritMomInfo() {
		return pritMomInfo;
	}

	public void setPritMomInfo(PritMomInfo pritMomInfo) {
		this.pritMomInfo = pritMomInfo;
	}

	public boolean isSkipLegameConRda() {
		return skipLegameConRda;
	}

	public void setSkipLegameConRda(boolean skipLegameConRda) {
		this.skipLegameConRda = skipLegameConRda;
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

	public String getCodDispositivaBMEDFromDriver() {
		return codDispositivaBMEDFromDriver;
	}

	public void setCodDispositivaBMEDFromDriver(String codDispositivaBMEDFromDriver) {
		this.codDispositivaBMEDFromDriver = codDispositivaBMEDFromDriver;
	}

}
