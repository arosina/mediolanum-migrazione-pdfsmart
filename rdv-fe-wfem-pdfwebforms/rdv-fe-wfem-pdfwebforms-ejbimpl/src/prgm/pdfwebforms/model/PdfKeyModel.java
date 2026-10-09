package prgm.pdfwebforms.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfKeyModel extends CommandDataModel {
	
	private StringType  	pdfId = new StringType();
	private StringType  	pdfCode = new StringType();
	private StringType  	pdfMomCode = new StringType();
	private IntegerType 	pdfPublicationId = new IntegerType();
	private StringType  	momVersion = new StringType();
	private IntegerType 	acroformVersion = new IntegerType();
	
	public StringType getPdfId() {
		return pdfId;
	}
	public void setPdfId(StringType pdfId) {
		this.pdfId = pdfId;
	}
	public StringType getPdfCode() {
		return pdfCode;
	}
	public void setPdfCode(StringType pdfCode) {
		this.pdfCode = pdfCode;
	}
	public IntegerType getPdfPublicationId() {
		return pdfPublicationId;
	}
	public void setPdfPublicationId(IntegerType pdfPublicationId) {
		this.pdfPublicationId = pdfPublicationId;
	}
	public IntegerType getAcroformVersion() {
		return acroformVersion;
	}
	public void setAcroformVersion(IntegerType acroformVersion) {
		this.acroformVersion = acroformVersion;
	}
	public StringType getPdfMomCode() {
		return pdfMomCode;
	}
	public void setPdfMomCode(StringType pdfMomCode) {
		this.pdfMomCode = pdfMomCode;
	}
	public StringType getMomVersion() {
		return momVersion;
	}
	public void setMomVersion(StringType momVersion) {
		this.momVersion = momVersion;
	}

}
