package prgm.pdfwebforms.publisher.model;

import com.atosorigin.wfem.types.ByteArrayType;

/*******************************************************************/
/*******************************************************************/
public class PdfPageAnagModel extends PdfPageAnagKeyModel {
	
	private ByteArrayType	pdfPageImg = new ByteArrayType();

	public ByteArrayType getPdfPageImg() {
		return pdfPageImg;
	}

	public void setPdfPageImg(ByteArrayType pdfPageImg) {
		this.pdfPageImg = pdfPageImg;
	}
	
}
