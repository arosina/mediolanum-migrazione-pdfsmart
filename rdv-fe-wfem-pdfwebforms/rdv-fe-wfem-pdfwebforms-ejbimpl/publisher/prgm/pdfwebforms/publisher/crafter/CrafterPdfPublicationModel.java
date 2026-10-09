package prgm.pdfwebforms.publisher.crafter;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

/*******************************************************************/
/*******************************************************************/
public class CrafterPdfPublicationModel extends CommandDataModel {
	
	private IntegerType 			pdfPublicationId = new IntegerType();	
	private DateType				pdfPubStartDate = new DateType();
	private IntegerType 			pdfAcroformVersion = new IntegerType(1);
	private StringType 				pdfEdition = new StringType();
	private StringType 				pdfMomVersion = new StringType();
	private IntegerType				pdfNumPages = new IntegerType();
	private StringType 				pdfFileName = new StringType();
	private StringType 				pdfDriverVersion = new StringType();
	
	public IntegerType getPdfPublicationId() {
		return pdfPublicationId;
	}
	public void setPdfPublicationId(IntegerType pdfPublicationId) {
		this.pdfPublicationId = pdfPublicationId;
	}
	public DateType getPdfPubStartDate() {
		return pdfPubStartDate;
	}
	public void setPdfPubStartDate(DateType pdfPubStartDate) {
		this.pdfPubStartDate = pdfPubStartDate;
	}
	public IntegerType getPdfAcroformVersion() {
		return pdfAcroformVersion;
	}
	public void setPdfAcroformVersion(IntegerType pdfAcroformVersion) {
		this.pdfAcroformVersion = pdfAcroformVersion;
	}
	public StringType getPdfEdition() {
		return pdfEdition;
	}
	public void setPdfEdition(StringType pdfEdition) {
		this.pdfEdition = pdfEdition;
	}
	public StringType getPdfMomVersion() {
		return pdfMomVersion;
	}
	public void setPdfMomVersion(StringType pdfMomVersion) {
		this.pdfMomVersion = pdfMomVersion;
	}
	public IntegerType getPdfNumPages() {
		return pdfNumPages;
	}
	public void setPdfNumPages(IntegerType pdfNumPages) {
		this.pdfNumPages = pdfNumPages;
	}
	public StringType getPdfFileName() {
		return pdfFileName;
	}
	public void setPdfFileName(StringType pdfFileName) {
		this.pdfFileName = pdfFileName;
	}
	public StringType getPdfDriverVersion() {
		return pdfDriverVersion;
	}
	public void setPdfDriverVersion(StringType pdfDriverVersion) {
		this.pdfDriverVersion = pdfDriverVersion;
	}
}
