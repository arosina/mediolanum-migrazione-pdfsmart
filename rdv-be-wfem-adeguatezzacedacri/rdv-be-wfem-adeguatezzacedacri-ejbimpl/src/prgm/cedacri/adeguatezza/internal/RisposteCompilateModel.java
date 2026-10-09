package prgm.cedacri.adeguatezza.internal;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.*;

public class RisposteCompilateModel extends CommandDataModel {
	private static final long serialVersionUID = 0;
	private IntegerType numDoma = new IntegerType();
	private StringType  selRisp = new StringType();
	public IntegerType getNumDoma() {
		return numDoma;
	}
	public void setNumDoma(IntegerType numDoma) {
		this.numDoma = numDoma;
	}
	public StringType getSelRisp() {
		return selRisp;
	}
	public void setSelRisp(StringType selRisp) {
		this.selRisp = selRisp;
	}
}
