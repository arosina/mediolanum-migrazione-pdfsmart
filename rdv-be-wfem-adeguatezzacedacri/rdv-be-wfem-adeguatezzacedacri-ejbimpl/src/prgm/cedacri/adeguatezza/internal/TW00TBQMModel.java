package prgm.cedacri.adeguatezza.internal;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.*;

public class TW00TBQMModel extends CommandDataModel {
	private static final long serialVersionUID = 0;
	private IntegerType codisti = new IntegerType();
	private StringType  ndgdoss = new StringType();
	private StringType  ndgtemp = new StringType();
	private StringType  codappl = new StringType();      
	private IntegerType dfinval = new IntegerType();
	private TimestampType datsche = new TimestampType();
	private IntegerType numdoma = new IntegerType();
	private	StringType  selmult = new StringType();
	
	public StringType getCodappl() {
		return codappl;
	}
	public void setCodappl(StringType codappl) {
		this.codappl = codappl;
	}
	public IntegerType getCodisti() {
		return codisti;
	}
	public void setCodisti(IntegerType codisti) {
		this.codisti = codisti;
	}
	public TimestampType getDatsche() {
		return datsche;
	}
	public void setDatsche(TimestampType datsche) {
		this.datsche = datsche;
	}
	public IntegerType getDfinval() {
		return dfinval;
	}
	public void setDfinval(IntegerType dfinval) {
		this.dfinval = dfinval;
	}
	public StringType getNdgdoss() {
		return ndgdoss;
	}
	public void setNdgdoss(StringType ndgdoss) {
		this.ndgdoss = ndgdoss;
	}
	public StringType getNdgtemp() {
		return ndgtemp;
	}
	public void setNdgtemp(StringType ndgtemp) {
		this.ndgtemp = ndgtemp;
	}
	public IntegerType getNumdoma() {
		return numdoma;
	}
	public void setNumdoma(IntegerType numdoma) {
		this.numdoma = numdoma;
	}
	public StringType getSelmult() {
		return selmult;
	}
	public void setSelmult(StringType selmult) {
		this.selmult = selmult;
	} 
}
