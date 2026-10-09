package prgm.pdfwebforms.drivers.io;

import prgm.pdfwebforms.drivers.AbstractEventOutputData;


/***********************************************************************************************/
/***********************************************************************************************/
public class LoadPdfOutputData extends AbstractEventOutputData{
	
	private String 	errorMessage = null;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	
}
