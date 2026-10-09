package prgm.pdfwebforms.drivers.io;

import prgm.pdfwebforms.drivers.AbstractEventInputData;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class MomEventInputData extends AbstractEventInputData{
	
	private String azioneMom = null;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public MomEventInputData(PdfModel pdf){
		super(pdf, pdf.mainPdfData());
		this.azioneMom = pdf.getMomEventData().getAzioneMom().toString();
	}

	public String getAzioneMom() {
		return azioneMom;
	}

}
