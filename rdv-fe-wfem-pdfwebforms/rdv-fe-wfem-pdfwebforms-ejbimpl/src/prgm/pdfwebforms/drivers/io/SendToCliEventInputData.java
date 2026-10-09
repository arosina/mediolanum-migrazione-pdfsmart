package prgm.pdfwebforms.drivers.io;

import prgm.pdfwebforms.drivers.AbstractEventInputData;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class SendToCliEventInputData extends AbstractEventInputData{
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public SendToCliEventInputData(PdfModel pdf){
		super(pdf, pdf.mainPdfData());
	}
	
}
