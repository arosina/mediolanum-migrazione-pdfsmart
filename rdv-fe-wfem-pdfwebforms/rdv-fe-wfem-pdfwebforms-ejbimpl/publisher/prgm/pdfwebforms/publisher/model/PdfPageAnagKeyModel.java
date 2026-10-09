package prgm.pdfwebforms.publisher.model;

import com.atosorigin.wfem.types.IntegerType;

/*******************************************************************/
/*******************************************************************/
public class PdfPageAnagKeyModel extends PdfAnagKeyModel {
	
	private IntegerType pdfPageNum = new IntegerType();
	
	public IntegerType getPdfPageNum() {
		return pdfPageNum;
	}
	public void setPdfPageNum(IntegerType pdfPageNum) {
		this.pdfPageNum = pdfPageNum;
	}

}
