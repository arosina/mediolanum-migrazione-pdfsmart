package prgm.pdfwebforms.drivers.io;

import prgm.pdfwebforms.model.PdfDataModel;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class ProvideCrossFBCustomersDataRequest{
	
	private PdfDataModel 		pdfData;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public ProvideCrossFBCustomersDataRequest(PdfDataModel pdfData){
		this.pdfData = pdfData;
	}

	public PdfDataModel getPdfData() {
		return pdfData;
	}
	
}
