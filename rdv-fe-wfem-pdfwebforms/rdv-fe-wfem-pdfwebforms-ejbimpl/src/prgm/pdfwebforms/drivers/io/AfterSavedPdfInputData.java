package prgm.pdfwebforms.drivers.io;

import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.drivers.AbstractEventInputData;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class AfterSavedPdfInputData extends AbstractEventInputData{
	
	private StringType pdfInstanceId;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public AfterSavedPdfInputData(PdfModel pdf){
		super(pdf);
		this.pdfInstanceId = pdf.getPdfData().getPdfInstanceId();
	}

	public StringType getPdfInstanceId() {
		return pdfInstanceId;
	}

}
