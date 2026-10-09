package prgm.pdfwebforms.drivers.io.validation;

import prgm.pdfwebforms.drivers.AbstractEventInputData;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.validation.PdfValidationEventDataModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class ValidationEventInputData extends AbstractEventInputData{
	
	private PdfValidationEventDataModel pdfValidationEventData = null;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public ValidationEventInputData(PdfModel pdf){
		super(pdf, pdf.mainPdfData());
		this.pdfValidationEventData = pdf.getPdfValidationEventData();
	}

	public PdfValidationEventDataModel getPdfValidationEventData() {
		return pdfValidationEventData;
	}

}
