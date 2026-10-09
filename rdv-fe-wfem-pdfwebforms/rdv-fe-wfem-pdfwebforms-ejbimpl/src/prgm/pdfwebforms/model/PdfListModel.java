package prgm.pdfwebforms.model;

import prgm.pdfwebforms.publisher.model.PdfAnagModel;

import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class PdfListModel extends MapCommandDataModel {
	
	private boolean primaVolta = true;
	
	// Input params
	private StringType areas = new StringType();
	private StringType pdfEnvironment = new StringType();
	// *******************************************************
	
	private PdfListParamsModel 	params = new PdfListParamsModel(); 
	private ListType 			pdfList = new ListType(PdfAnagModel.class);

	public ListType getPdfList() {
		return pdfList;
	}

	public void setPdfList(ListType pdfList) {
		this.pdfList = pdfList;
	}

	public PdfListParamsModel getParams() {
		return params;
	}

	public void setParams(PdfListParamsModel params) {
		this.params = params;
	}

	public boolean isPrimaVolta() {
		return primaVolta;
	}

	public void setPrimaVolta(boolean primaVolta) {
		this.primaVolta = primaVolta;
	}

	public StringType getPdfEnvironment() {
		return pdfEnvironment;
	}

	public void setPdfEnvironment(StringType pdfEnvironment) {
		this.pdfEnvironment = pdfEnvironment;
	}

	public StringType getAreas() {
		return areas;
	}

	public void setAreas(StringType areas) {
		this.areas = areas;
	}

}
