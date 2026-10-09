package prgm.pdfwebforms.drivers.io;

import prgm.pdfwebforms.drivers.AbstractEventInputData;
import prgm.pdfwebforms.model.PdfModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class CompilationModeSelectionEventInputData extends AbstractEventInputData{
	
	public static String MODALITA_SOTTOSCRIZIONE_CARTA_LIBERA 	= "CARTA_LIBERA";
	public static String MODALITA_SOTTOSCRIZIONE_CARTA_CHIMICA 	= "CARTA_CHIMICA";
	public static String MODALITA_SOTTOSCRIZIONE_FIRMA_DIGITALE = "FIRMA_DIGITALE";
	public static String MODALITA_SOTTOSCRIZIONE_COPERNICO = "COPERNICO";

	private String selectedCompilationMode;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public CompilationModeSelectionEventInputData(PdfModel pdf, String selectedCompilationMode){
		super(pdf, pdf.mainPdfData());
		this.selectedCompilationMode = selectedCompilationMode;
	}
	
	public String getSelectedCompilationMode() {
		return selectedCompilationMode;
	}
	
	/**
	 * @deprecated usare getSelectedCompilationMode  
	 */
	@Deprecated
	public String getSelectedComilationMode() {
		return selectedCompilationMode;
	}

}
