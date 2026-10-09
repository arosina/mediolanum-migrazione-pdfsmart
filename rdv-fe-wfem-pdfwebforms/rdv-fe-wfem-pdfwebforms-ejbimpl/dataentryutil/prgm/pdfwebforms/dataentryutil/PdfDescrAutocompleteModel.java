package prgm.pdfwebforms.dataentryutil;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/*******************************************************************/
/*******************************************************************/
public class PdfDescrAutocompleteModel extends CommandDataModel {

	private StringType 	areas  = new StringType();
	private StringType 	pdfCode  = new StringType();
	private StringType  pdfDescr = new StringType();
	
	/********************************************************************************/
	/********************************************************************************/
	public StringType getPdfDescrForLike(){
		if(getPdfDescr().isNull())
			return getPdfDescr();
		String res = getPdfDescr().toString().replaceAll("[\\s\\[\\]\\-\\_\\/\\(\\)\\.]","%");
		return new StringType(res.toString());
	}

	public StringType getPdfCode() {
		return pdfCode;
	}
	public void setPdfCode(StringType pdfCode) {
		this.pdfCode = pdfCode;
	}
	public StringType getPdfDescr() {
		return pdfDescr;
	}
	public void setPdfDescr(StringType pdfDescr) {
		this.pdfDescr = pdfDescr;
	}
	public StringType getAreas() {
		return areas;
	}
	public void setAreas(StringType areas) {
		this.areas = areas;
	}
}
