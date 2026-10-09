package prgm.pdfwebforms.drivers.io;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class BeforePreviewOutputData{
	
	private Boolean	facSimileOnPreview = null;
	private String	inviaInSedeButtonLabel = null;
	private String	firmaDigitaleButtonLabel = null;
	private String	copernicoButtonLabel = null;
	private String 	errorMessage = null;

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Boolean getFacSimileOnPreview() {
		return facSimileOnPreview;
	}

	public void setFacSimileOnPreview(Boolean facSimileOnPreview) {
		this.facSimileOnPreview = facSimileOnPreview;
	}

	public String getInviaInSedeButtonLabel() {
		return inviaInSedeButtonLabel;
	}

	public void setInviaInSedeButtonLabel(String inviaInSedeButtonLabel) {
		this.inviaInSedeButtonLabel = inviaInSedeButtonLabel;
	}

	public String getFirmaDigitaleButtonLabel() {
		return firmaDigitaleButtonLabel;
	}

	public void setFirmaDigitaleButtonLabel(String firmaDigitaleButtonLabel) {
		this.firmaDigitaleButtonLabel = firmaDigitaleButtonLabel;
	}

	public String getCopernicoButtonLabel() {
		return copernicoButtonLabel;
	}

	public void setCopernicoButtonLabel(String copernicoButtonLabel) {
		this.copernicoButtonLabel = copernicoButtonLabel;
	}

}
