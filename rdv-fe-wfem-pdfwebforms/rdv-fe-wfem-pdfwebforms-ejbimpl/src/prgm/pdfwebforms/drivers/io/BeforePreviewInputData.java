package prgm.pdfwebforms.drivers.io;

import prgm.pdfwebforms.drivers.AbstractEventInputData;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class BeforePreviewInputData extends AbstractEventInputData{
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public BeforePreviewInputData(PdfModel pdf){
		super(pdf, pdf.mainPdfData());
	}

}
