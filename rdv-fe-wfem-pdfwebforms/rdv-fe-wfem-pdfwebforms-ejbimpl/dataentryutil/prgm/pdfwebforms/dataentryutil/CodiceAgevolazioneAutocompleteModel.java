package prgm.pdfwebforms.dataentryutil;

import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.agevolazioni.AgevolazioneModel;

/*******************************************************************/
/*******************************************************************/
public class CodiceAgevolazioneAutocompleteModel extends AgevolazioneModel {

	private BooleanType isOperatoreMom = new BooleanType();
	private StringType  pdfMomCode = new StringType();
	private StringType	codDescrAgevolazioneDipendenti = new StringType();
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String autocompletionLabel(){
		return "<span style='white-space:nowrap;'>"+getDescrizioneAgevolazione()+"</span>";
	}

	public StringType getPdfMomCode() {
		return pdfMomCode;
	}

	public void setPdfMomCode(StringType pdfMomCode) {
		this.pdfMomCode = pdfMomCode;
	}

	public StringType getCodDescrAgevolazioneDipendenti() {
		return codDescrAgevolazioneDipendenti;
	}

	public void setCodDescrAgevolazioneDipendenti(StringType codDescrAgevolazioneDipendenti) {
		this.codDescrAgevolazioneDipendenti = codDescrAgevolazioneDipendenti;
	}

	public BooleanType getIsOperatoreMom() {
		return isOperatoreMom;
	}

	public void setIsOperatoreMom(BooleanType isOperatoreMom) {
		this.isOperatoreMom = isOperatoreMom;
	}

}
