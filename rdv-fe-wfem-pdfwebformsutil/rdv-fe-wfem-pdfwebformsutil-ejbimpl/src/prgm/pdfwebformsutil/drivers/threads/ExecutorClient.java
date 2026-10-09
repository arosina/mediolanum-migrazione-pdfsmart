package prgm.pdfwebformsutil.drivers.threads;

import prgm.pdfwebforms.model.PdfDataModel;

public interface ExecutorClient {
	public void onExecutionSuccess(Object sender, PdfDataModel pdfData, Object o);

	public void onExecutionError(Object sender, PdfDataModel pdfData, Object o, Throwable e);
}
