package prgm.pdfwebforms.drivers;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfExtractionInfo {
	
	private String  pdfInstanceId = null;
	private String  target = null;
	private String  extractionType = null;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PdfExtractionInfo(String  pdfInstanceId, String  target, String  extractionType){
		this.pdfInstanceId = pdfInstanceId;
		this.target = target;
		this.extractionType = extractionType;
	}

	public String getPdfInstanceId() {
		return pdfInstanceId;
	}

	public String getTarget() {
		return target;
	}

	public String getExtractionType() {
		return extractionType;
	}
	
}
