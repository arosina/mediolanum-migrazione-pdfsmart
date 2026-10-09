package prgm.pdfwebforms.publisher.crafter.util;

import com.atosorigin.wfem.types.StringType;

import prgm.pdfwebforms.publisher.model.PdfAnagKeyModel;

/*******************************************************************/
/*******************************************************************/
public class GeneraPdfConNumeroOrdineFondiInputModel extends PdfAnagKeyModel {
	
	private StringType  ndg = new StringType();
	private StringType  tipoMandato = new StringType();

	public StringType getNdg() {
		return ndg;
	}

	public void setNdg(StringType ndg) {
		this.ndg = ndg;
	}

	public StringType getTipoMandato() {
		return tipoMandato;
	}

	public void setTipoMandato(StringType tipoMandato) {
		this.tipoMandato = tipoMandato;
	}
}
