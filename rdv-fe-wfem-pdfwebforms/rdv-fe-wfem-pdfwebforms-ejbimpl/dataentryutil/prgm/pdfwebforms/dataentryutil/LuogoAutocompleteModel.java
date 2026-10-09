package prgm.pdfwebforms.dataentryutil;

import com.atosorigin.wfem.types.StringType;

/*******************************************************************/
/*******************************************************************/
public class LuogoAutocompleteModel extends AbstractAutocompleteModel {

	private StringType  luogo = new StringType();

	public StringType getLuogo() {
		return luogo;
	}

	public void setLuogo(StringType luogo) {
		this.luogo = luogo;
	}

}
