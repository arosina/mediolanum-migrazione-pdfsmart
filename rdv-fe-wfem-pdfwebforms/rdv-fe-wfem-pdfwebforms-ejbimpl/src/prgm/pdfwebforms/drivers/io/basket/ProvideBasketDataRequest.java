package prgm.pdfwebforms.drivers.io.basket;

import prgm.pdfwebforms.drivers.AbstractEventInputData;
import prgm.pdfwebforms.model.PdfModel;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class ProvideBasketDataRequest extends AbstractEventInputData{
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public ProvideBasketDataRequest(PdfModel pdf){
		super(pdf, pdf.mainPdfData());
	}

}
