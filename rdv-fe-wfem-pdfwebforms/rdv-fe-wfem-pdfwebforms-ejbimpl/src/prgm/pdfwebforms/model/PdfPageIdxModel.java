package prgm.pdfwebforms.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfPageIdxModel extends CommandDataModel {

	private BooleanType pdfOnWork = new BooleanType();

	private StringType  pdfId = new StringType();
	private StringType  pdfCode = new StringType();
	private IntegerType pdfPublicationId = new IntegerType();
	private IntegerType pageImgIdx = new IntegerType();

	public IntegerType getPageImgIdx() {
		return pageImgIdx;
	}

	public void setPageImgIdx(IntegerType pageImgIdx) {
		this.pageImgIdx = pageImgIdx;
	}

	public BooleanType getPdfOnWork() {
		return pdfOnWork;
	}

	public void setPdfOnWork(BooleanType pdfOnWork) {
		this.pdfOnWork = pdfOnWork;
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

	public StringType getPdfId() {
		return pdfId;
	}

	public void setPdfId(StringType pdfId) {
		this.pdfId = pdfId;
	}
	
}
