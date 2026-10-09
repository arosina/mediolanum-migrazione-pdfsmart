package prgm.pdfwebforms.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/********************************************************************************/
/********************************************************************************/
public class PdfListParamsModel extends CommandDataModel {
	
	private StringType 	areas = new StringType();
	private StringType 	pdfEnvironment = new StringType();
	private StringType 	pdfDescr = new StringType();

	/********************************************************************************/
	/********************************************************************************/
	public StringType getPdfDescrForLike(){
		if(getPdfDescr().isNull())
			return getPdfDescr();
		String res = getPdfDescr().toString().replaceAll("[\\s\\[\\]\\-\\_\\/\\(\\)\\.]","%");
		return new StringType(res.toString());
	}

	public StringType getPdfDescr() {
		return pdfDescr;
	}

	public void setPdfDescr(StringType pdfDescr) {
		this.pdfDescr = pdfDescr;
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
