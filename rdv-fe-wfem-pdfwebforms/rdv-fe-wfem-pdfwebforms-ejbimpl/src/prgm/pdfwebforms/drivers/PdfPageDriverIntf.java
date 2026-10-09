package prgm.pdfwebforms.drivers;

import prgm.pdfwebforms.drivers.io.PageLoadInputData;
import prgm.pdfwebforms.drivers.io.PageLoadOutputData;
import prgm.pdfwebforms.drivers.io.PageEventInputData;
import prgm.pdfwebforms.drivers.io.PageEventOutputData;

import com.atosorigin.wfem.command.ClientSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public interface PdfPageDriverIntf {
	public PageLoadOutputData	onLoad(ClientSessionContext csc, PageLoadInputData input) throws Exception;
	public PageEventOutputData 	onEvent(ClientSessionContext csc, String eventName, PageEventInputData input) throws Exception;	
}
