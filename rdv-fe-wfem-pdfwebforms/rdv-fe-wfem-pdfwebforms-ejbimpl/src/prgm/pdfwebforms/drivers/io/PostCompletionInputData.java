package prgm.pdfwebforms.drivers.io;

import prgm.pdfwebforms.model.PdfInstanceModel;
import prgm.pdfwebforms.model.PdfModel;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class PostCompletionInputData{

	private PdfModel			pdf;
	private PdfInstanceModel 	pdfInstance;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PostCompletionInputData(PdfModel pdf, PdfInstanceModel pdfInstance){
		this.pdf = pdf;
		this.pdfInstance = pdfInstance;
	}
	
	public PdfModel getPdf() {
		return pdf;
	}

	public PdfInstanceModel getPdfInstance() {
		return pdfInstance;
	}
}
