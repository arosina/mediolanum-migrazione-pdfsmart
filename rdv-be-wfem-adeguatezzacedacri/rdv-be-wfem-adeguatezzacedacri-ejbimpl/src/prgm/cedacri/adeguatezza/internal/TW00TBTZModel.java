package prgm.cedacri.adeguatezza.internal;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.*;

public class TW00TBTZModel extends CommandDataModel 
{
	private static final long serialVersionUID = 0;
	private IntegerType codisti = new IntegerType();
	private IntegerType codtabe = new IntegerType();
	private StringType  chiave  = new StringType();
	private StringType  seguito = new StringType();
	private StringType  dati    = new StringType();
	
	public StringType getChiave() {
		return chiave;
	}
	public IntegerType getCodisti() {
		return codisti;
	}
	public IntegerType getCodtabe() {
		return codtabe;
	}
	public StringType getDati() {
		return dati;
	}
	public StringType getSeguito() {
		return seguito;
	}
	public void setChiave(StringType chiave) {
		this.chiave = chiave;
	}
	public void setCodisti(IntegerType codisti) {
		this.codisti = codisti;
	}
	public void setCodtabe(IntegerType codtabe) {
		this.codtabe = codtabe;
	}
	public void setDati(StringType dati) {
		this.dati = dati;
	}
	public void setSeguito(StringType seguito) {
		this.seguito = seguito;
	}

}
