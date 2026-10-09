package prgm.pdfwebforms.drivers.io;

import prgm.pdfwebforms.drivers.AbstractEventInputData;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class FreezeEventInputData extends AbstractEventInputData{
	
	byte[] pdfContent;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public FreezeEventInputData(PdfModel pdf, byte[] pdfContent){
		super(pdf, pdf.mainPdfData());
		this.pdfContent = pdfContent;
	}
	
	public String getCompilationMode() {
		return getPdf().getPdfCompilationMode().toString();
	}

	public byte[] getPdfContent() {
		return pdfContent;
	}
	
}
