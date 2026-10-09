package prgm.pdfwebforms.drivers.io;

import prgm.pdfwebforms.drivers.AbstractEventInputData;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class FinalEventInputData extends AbstractEventInputData{
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public FinalEventInputData(PdfModel pdf){
		super(pdf, pdf.mainPdfData());
	}
	
	public String getCompilationMode() {
		return getPdf().getPdfCompilationMode().toString();
	}
	
}
