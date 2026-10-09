package prgm.cedacri.adeguatezza.internal;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

public class RisposteVerificaModel extends CommandDataModel {
	private static final long serialVersionUID = 0;
	private IntegerType numDoma = new IntegerType();
	private StringType  risMult = new StringType();
	private StringType  risposto = new StringType();
	public IntegerType getNumDoma() {
		return numDoma;
	}
	public void setNumDoma(IntegerType numDoma) {
		this.numDoma = numDoma;
	}
	public StringType getRisMult() {
		return risMult;
	}
	public void setRisMult(StringType risMult) {
		this.risMult = risMult;
	}
	public StringType getRisposto() {
		return risposto;
	}
	public void setRisposto(StringType risposto) {
		this.risposto = risposto;
	}
}
