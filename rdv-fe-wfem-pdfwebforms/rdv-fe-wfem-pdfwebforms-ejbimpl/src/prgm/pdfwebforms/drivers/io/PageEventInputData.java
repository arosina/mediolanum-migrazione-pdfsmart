package prgm.pdfwebforms.drivers.io;

import prgm.pdfwebforms.drivers.AbstractEventInputData;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class PageEventInputData extends AbstractEventInputData{
	
	private String eventArgs = null;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PageEventInputData(PdfModel pdf){
		super(pdf);
		if(!pdf.getEventArgs().isNull())
			eventArgs = pdf.getEventArgs().toString();
	}

	public String getEventArgs() {
		return eventArgs;
	}

}
