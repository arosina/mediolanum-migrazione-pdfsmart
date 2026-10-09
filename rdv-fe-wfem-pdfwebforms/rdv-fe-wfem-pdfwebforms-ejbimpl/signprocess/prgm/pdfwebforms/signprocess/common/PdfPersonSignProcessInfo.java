package prgm.pdfwebforms.signprocess.common;

import java.io.Serializable;
import java.util.ArrayList;

/*****************************************************************************************************/
/*****************************************************************************************************/
public class PdfPersonSignProcessInfo implements Serializable{
	
	private boolean 			hasSignOnPdf = false;
	private ArrayList<Integer>	indexInFieldName = new ArrayList<Integer>();
	private ArrayList<String>	signedFieldNames = new ArrayList<String>();
	
	public ArrayList<Integer> getIndexInFieldName() {
		return indexInFieldName;
	}
	public void setIndexInFieldName(ArrayList<Integer> indexInFieldName) {
		this.indexInFieldName = indexInFieldName;
	}
	public ArrayList<String> getSignedFieldNames() {
		return signedFieldNames;
	}
	public void setSignedFieldNames(ArrayList<String> signedFieldNames) {
		this.signedFieldNames = signedFieldNames;
	}
	public boolean isHasSignOnPdf() {
		return hasSignOnPdf;
	}
	public void setHasSignOnPdf(boolean hasSignOnPdf) {
		this.hasSignOnPdf = hasSignOnPdf;
	}
}
