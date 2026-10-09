package prgm.pdfwebforms.drivers.io.squadra;

import prgm.pdfwebforms.drivers.AbstractEventInputData;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class ProvideSquadraDataRequest extends AbstractEventInputData{
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public ProvideSquadraDataRequest(PdfModel pdf){
		super(pdf);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public ProvideSquadraDataRequest(PdfModel pdf, PdfDataModel pdfData){
		super(pdf, pdfData);
	}
}
