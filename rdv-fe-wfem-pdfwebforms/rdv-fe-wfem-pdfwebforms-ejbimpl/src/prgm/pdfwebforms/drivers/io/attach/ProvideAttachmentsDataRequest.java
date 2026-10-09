package prgm.pdfwebforms.drivers.io.attach;

import prgm.pdfwebforms.drivers.AbstractEventInputData;
import prgm.pdfwebforms.model.PdfModel;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class ProvideAttachmentsDataRequest extends AbstractEventInputData{
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public ProvideAttachmentsDataRequest(PdfModel pdf){
		super(pdf, pdf.mainPdfData());
	}
	
}
