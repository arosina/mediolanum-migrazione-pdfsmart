package prgm.cedacri.adeguatezza.internal;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.*;

public class InputDefaultProfiloModel extends CommandDataModel {
	private static final long serialVersionUID = 0;

	private IntegerType eta = new IntegerType();
	private StringType titStud = new StringType();

	public IntegerType getEta() {
		return eta;
	}
	public void setEta(IntegerType eta) {
		this.eta = eta;
	}
	public StringType getTitStud() {
		return titStud;
	}
	public void setTitStud(StringType titStud) {
		this.titStud = titStud;
	}
}
