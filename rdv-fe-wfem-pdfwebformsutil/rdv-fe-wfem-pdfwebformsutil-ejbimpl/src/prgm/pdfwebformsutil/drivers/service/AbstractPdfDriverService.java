package prgm.pdfwebformsutil.drivers.service;

import java.util.concurrent.Semaphore;

import prgm.pdfwebforms.drivers.PdfBaseDriver;
import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebformsutil.drivers.threads.ExecutorClient;

public abstract class AbstractPdfDriverService<T extends PdfBaseDriver> implements ExecutorClient {
	private T pdfDriver;
	private Semaphore semaphore = new Semaphore(1);

	public T getPdfDriver() {
		return pdfDriver;
	}

	public void setPdfDriver(T pdfDriver) {
		this.pdfDriver = pdfDriver;
	}

	public Semaphore getSemaphore() {
		return semaphore;
	}

	public AbstractPdfDriverService(T pdfDriver) {
		this.pdfDriver = pdfDriver;
	}

	public void onExecutionSuccess(Object sender, PdfDataModel pdfData, Object o) {
		getSemaphore().release();
	}

	public void onExecutionError(Object sender, PdfDataModel pdfData, Object o, Throwable e) {
		getSemaphore().release();
	}
}
