package prgm.pdfwebforms.drivers.io.prit;

import prgm.pdfwebforms.drivers.AbstractEventInputData;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class ProvidePritDataRequest extends AbstractEventInputData{
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public ProvidePritDataRequest(PdfModel pdf){
		super(pdf, pdf.mainPdfData());
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public ProvidePritDataRequest(PdfModel pdf, PdfDataModel pdfData){
		super(pdf, pdfData);
	}

}
