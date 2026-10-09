package prgm.pdfwebforms.drivers;

import com.atosorigin.wfem.command.ClientSessionContext;

import prgm.pdfwebforms.drivers.io.PostCompletionInputData;
import prgm.pdfwebforms.drivers.io.PostCompletionOutputData;

/***********************************************************************************************/
/***********************************************************************************************/
public interface PdfPostCompletionIntf {

	public PostCompletionOutputData doWork(ClientSessionContext csc, PostCompletionInputData input) throws Exception;
	
}
