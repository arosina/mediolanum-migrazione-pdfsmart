package prgm.pdfwebforms.dataentryutil;

import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

/*******************************************************************/
/*******************************************************************/
public class ToponimoAutocompleteModel extends AbstractAutocompleteModel {

	private StringType  descrToponimo = new StringType();
	private StringType  codToponimo = new StringType();

	/***********************************************************************************************/
	/***********************************************************************************************/
	public String autocompleteLabel(){
		return Tools.capitalize(getDescrToponimo().toString());
	}

	public StringType getDescrToponimo() {
		return descrToponimo;
	}

	public void setDescrToponimo(StringType descrToponimo) {
		this.descrToponimo = descrToponimo;
	}

	public StringType getCodToponimo() {
		return codToponimo;
	}

	public void setCodToponimo(StringType codToponimo) {
		this.codToponimo = codToponimo;
	}

}
