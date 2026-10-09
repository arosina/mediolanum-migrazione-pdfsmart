package prgm.cedacri.adeguatezza.internal;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;

public class IntegerModel extends CommandDataModel {
	private static final long serialVersionUID = 0;
	private IntegerType  valore = new IntegerType();
	public IntegerType getValore() {
		return valore;
	}
	public void setValore(IntegerType valore) {
		this.valore = valore;
	}
}
