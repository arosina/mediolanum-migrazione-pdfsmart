package prgm.pdfwebformsutil.drivers.dao.qarcservices.qarcenhancement;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

public class VariabileSessioneQarcModel extends CommandDataModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private StringType variabile = new StringType();
	private StringType valore = new StringType();

	public VariabileSessioneQarcModel() {
	}

	public VariabileSessioneQarcModel(StringType variabile, StringType valore) {
		this.variabile = variabile;
		this.valore = valore;
	}

	public StringType getVariabile() {
		return variabile;
	}

	public void setVariabile(StringType variabile) {
		this.variabile = variabile;
	}

	public StringType getValore() {
		return valore;
	}

	public void setValore(StringType valore) {
		this.valore = valore;
	}
}
