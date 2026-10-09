package prgm.pdfwebforms.drivers.io.sostituzioni;

import prgm.pdfwebforms.drivers.AbstractEventInputData;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.model.PdfPersonModel;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class ProvideSostituzioniDataRequest extends AbstractEventInputData{
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public ProvideSostituzioniDataRequest(PdfModel pdf){
		super(pdf, pdf.mainPdfData());
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfPersonModel getPerson(int personIdx){
		try{
			return getPdfData().getClienti().get(personIdx-1);
		}catch(Throwable t){
			return null;
		}
	}
}
