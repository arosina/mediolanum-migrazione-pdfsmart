package prgm.pdfwebforms.drivers.io;

import prgm.pdfwebforms.model.PdfDataModel;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class ProvideAgevolazioneDipendentiDataRequest{
	
	private PdfDataModel 		pdfData;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public ProvideAgevolazioneDipendentiDataRequest(PdfDataModel pdfData){
		this.pdfData = pdfData;
	}

	public PdfDataModel getPdfData() {
		return pdfData;
	}
	
}
