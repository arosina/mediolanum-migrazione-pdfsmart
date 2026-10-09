package prgm.pdfwebforms.drivers.io;

import prgm.pdfwebforms.drivers.AbstractBusinessEventOutputData;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class AfterSavedPdfOutputData extends AbstractBusinessEventOutputData{
	
	private String 	errorMessage = null;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}


}
