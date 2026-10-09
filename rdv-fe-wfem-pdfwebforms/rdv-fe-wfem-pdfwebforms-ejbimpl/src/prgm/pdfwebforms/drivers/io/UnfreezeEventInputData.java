package prgm.pdfwebforms.drivers.io;

import prgm.pdfwebforms.drivers.AbstractEventInputData;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class UnfreezeEventInputData extends AbstractEventInputData{
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public UnfreezeEventInputData(PdfModel pdf){
		super(pdf, pdf.mainPdfData());
	}
	
	public String getCompilationMode() {
		return getPdf().getPdfCompilationMode().toString();
	}

}
