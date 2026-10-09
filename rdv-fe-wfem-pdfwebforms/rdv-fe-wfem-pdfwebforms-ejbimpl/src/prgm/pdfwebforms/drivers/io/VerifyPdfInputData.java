package prgm.pdfwebforms.drivers.io;

import prgm.pdfwebforms.drivers.AbstractEventInputData;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class VerifyPdfInputData extends AbstractEventInputData{
	
	private PdfDataModel pdfDataBeforeImage;

	/***********************************************************************************************/
	/***********************************************************************************************/
	public VerifyPdfInputData(PdfModel pdf, PdfDataModel pdfDataBeforeImage){
		super(pdf);
		this.pdfDataBeforeImage = pdfDataBeforeImage; 
	}
	
	public PdfDataModel getPdfDataBeforeImage() {
		return pdfDataBeforeImage;
	}

}
