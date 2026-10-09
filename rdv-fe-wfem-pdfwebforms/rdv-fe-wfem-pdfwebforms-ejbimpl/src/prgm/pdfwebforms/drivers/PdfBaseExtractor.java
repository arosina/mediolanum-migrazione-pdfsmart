package prgm.pdfwebforms.drivers;

import prgm.pdfwebforms.model.PdfDataModel;

import com.atosorigin.wfem.command.ClientSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfBaseExtractor{

	private ClientSessionContext csc = null;
	private PdfExtractionInfo pdfExtractionInfo = null;
	private PdfDataModel pdfData = null;

	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfBaseExtractor(ClientSessionContext csc, PdfExtractionInfo pdfExtractionInfo, PdfDataModel pdfData){
		this.csc = csc;
		this.pdfExtractionInfo = pdfExtractionInfo;
		this.pdfData = pdfData;
	}
	
	/**************************************************************************************************
	 * Verifica delle precondizioni di estrazione, richiamato ad ogni esecuzione sulle entità ancora
	 * da estrarre. Quando torna un messaggio non vuoto l'estrazione non verrà fatta e tale stringa
	 * verrà utilizzata per il log del processo. 
	 * Nella verifica è anche possibile impostare dati non valorizzati durante la compilazione del modulo 
	**************************************************************************************************/
	public String checkExtractPrecondition(){
		return null;
	}
	
	protected ClientSessionContext getCsc() {
		return csc;
	}

	protected PdfDataModel getPdfData() {
		return pdfData;
	}

	public PdfExtractionInfo getPdfExtractionInfo() {
		return pdfExtractionInfo;
	}

}
