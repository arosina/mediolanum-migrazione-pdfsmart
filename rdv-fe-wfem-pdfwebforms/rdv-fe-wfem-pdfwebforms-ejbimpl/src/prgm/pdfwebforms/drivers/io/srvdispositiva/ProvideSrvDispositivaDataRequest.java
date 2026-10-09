package prgm.pdfwebforms.drivers.io.srvdispositiva;

import prgm.pdfwebforms.drivers.AbstractEventInputData;
import prgm.pdfwebforms.model.PdfModel;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class ProvideSrvDispositivaDataRequest extends AbstractEventInputData{
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public ProvideSrvDispositivaDataRequest(PdfModel pdf){
		super(pdf, pdf.mainPdfData());
	}
	
}
