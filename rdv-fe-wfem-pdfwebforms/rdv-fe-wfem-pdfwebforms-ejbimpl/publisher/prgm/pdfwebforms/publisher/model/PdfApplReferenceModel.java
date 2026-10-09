package prgm.pdfwebforms.publisher.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/*******************************************************************/
/*******************************************************************/
public class PdfApplReferenceModel extends CommandDataModel {
	
	private StringType applReference  = new StringType();
	private StringType pdfEnvironment  = new StringType();
	private StringType pdfCode  = new StringType();
	private StringType acroformVersion  = new StringType();
	private StringType applDescr = new StringType();

	public StringType getApplReference() {
		return applReference;
	}

	public void setApplReference(StringType applReference) {
		this.applReference = applReference;
	}

	public StringType getPdfEnvironment() {
		return pdfEnvironment;
	}

	public void setPdfEnvironment(StringType pdfEnvironment) {
		this.pdfEnvironment = pdfEnvironment;
	}

	public StringType getPdfCode() {
		return pdfCode;
	}

	public void setPdfCode(StringType pdfCode) {
		this.pdfCode = pdfCode;
	}

	public StringType getAcroformVersion() {
		return acroformVersion;
	}

	public void setAcroformVersion(StringType acroformVersion) {
		this.acroformVersion = acroformVersion;
	}
	
	public StringType getApplDescr() {
		return applDescr;
	}

	public void setApplDescr(StringType applDescr) {
		this.applDescr = applDescr;
	}

}
