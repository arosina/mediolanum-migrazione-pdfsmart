package prgm.pdfwebforms.drivers;

import com.atosorigin.wfem.command.ClientSessionContext;

import prgm.pdfwebforms.model.PdfDataModel;
import prgm.pdfwebforms.model.PdfModel;

public abstract class PdfBaseMomInstanceEnrichment {

	private ClientSessionContext csc;
	private PdfModel pdf;

	public abstract void doWork(PdfDataModel pdfData);

	public ClientSessionContext getCsc() {
		return csc;
	}

	public void setCsc(ClientSessionContext csc) {
		this.csc = csc;
	}

	public PdfModel getPdf() {
		return pdf;
	}

	public void setPdf(PdfModel pdf) {
		this.pdf = pdf;
	}
	
}
