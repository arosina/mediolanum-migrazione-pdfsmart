package prgm.cedacri.adeguatezza.internal;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.*;

public class TW00TBQKModel extends CommandDataModel {
	private static final long serialVersionUID = 0;
	private IntegerType codisti = new IntegerType();
	private StringType  ndgdoss = new StringType();
	private StringType  ndgtemp = new StringType();
	private	StringType  codappl = new StringType();
	private	IntegerType dfinval = new IntegerType();
	private	TimestampType datsche = new TimestampType();
	private	IntegerType filoper = new IntegerType();
	private	IntegerType release = new IntegerType();
	private	StringType  selrisp = new StringType();
	private	StringType  canvend = new StringType();
	
	public StringType getCanvend() {
		return canvend;
	}
	public void setCanvend(StringType canvend) {
		this.canvend = canvend;
	}
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
	public IntegerType getFiloper() {
		return filoper;
	}
	public void setFiloper(IntegerType filoper) {
		this.filoper = filoper;
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
	public IntegerType getRelease() {
		return release;
	}
	public void setRelease(IntegerType release) {
		this.release = release;
	}
	public StringType getSelrisp() {
		return selrisp;
	}
	public void setSelrisp(StringType selrisp) {
		this.selrisp = selrisp;
	}	
}
