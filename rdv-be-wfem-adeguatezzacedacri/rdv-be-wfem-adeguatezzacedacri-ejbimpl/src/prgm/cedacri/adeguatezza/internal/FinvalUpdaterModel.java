package prgm.cedacri.adeguatezza.internal;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.*;

public class FinvalUpdaterModel extends CommandDataModel {
	private static final long serialVersionUID = 0;
	private StringType ndgDoss = new StringType();
	private IntegerType dfinval = new IntegerType();
	private IntegerType ofinval = new IntegerType();
	
	public IntegerType getDfinval() {
		return dfinval;
	}
	public void setDfinval(IntegerType dfinval) {
		this.dfinval = dfinval;
	}
	public StringType getNdgDoss() {
		return ndgDoss;
	}
	public void setNdgDoss(StringType ndgDoss) {
		this.ndgDoss = ndgDoss;
	}
	public IntegerType getOfinval() {
		return ofinval;
	}
	public void setOfinval(IntegerType ofinval) {
		this.ofinval = ofinval;
	}
	
}
