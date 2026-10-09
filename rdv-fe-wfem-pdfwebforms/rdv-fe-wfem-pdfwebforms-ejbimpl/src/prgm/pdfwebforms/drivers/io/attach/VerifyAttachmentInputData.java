package prgm.pdfwebforms.drivers.io.attach;

import prgm.pdfwebforms.drivers.AbstractEventInputData;
import prgm.pdfwebforms.model.PdfAttachModel;
import prgm.pdfwebforms.model.PdfModel;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class VerifyAttachmentInputData extends AbstractEventInputData{
	
	private PdfAttachModel 	pdfAttach = null;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public VerifyAttachmentInputData(PdfModel pdf, PdfAttachModel pdfAttach){
		super(pdf);
		this.pdfAttach = pdfAttach;
	}

	public PdfAttachModel getPdfAttach() {
		return pdfAttach;
	}
	
}
