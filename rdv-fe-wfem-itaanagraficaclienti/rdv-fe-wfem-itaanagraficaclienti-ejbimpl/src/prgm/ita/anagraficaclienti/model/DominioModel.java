package prgm.ita.anagraficaclienti.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class DominioModel extends CommandDataModel {

	private StringType cod = new StringType();
	private StringType descr = new StringType();
	private StringType validita = new StringType();
	private StringType shortDescr = new StringType();
	
	public StringType getCod() {
		return cod;
	}

	public StringType getDescr() {
		return descr;
	}

	public void setCod(StringType cod) {
		this.cod = cod;
	}

	public void setDescr(StringType descr) {
		this.descr = descr;
	}

	public StringType getValidita() {
		return validita;
	}

	public void setValidita(StringType validita) {
		this.validita = validita;
	}

	public StringType getShortDescr() {
		return shortDescr;
	}

	public void setShortDescr(StringType shortDescr) {
		this.shortDescr = shortDescr;
	}

}
