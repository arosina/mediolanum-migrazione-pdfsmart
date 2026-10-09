package prgm.pdfwebforms.drivers.io.idd;

import prgm.pdfwebforms.drivers.AbstractEventInputData;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.model.PdfPersonModel;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class ProvideIddDataRequest extends AbstractEventInputData{
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public ProvideIddDataRequest(PdfModel pdf, PdfDataModel pdfData){
		super(pdf, pdfData);
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
