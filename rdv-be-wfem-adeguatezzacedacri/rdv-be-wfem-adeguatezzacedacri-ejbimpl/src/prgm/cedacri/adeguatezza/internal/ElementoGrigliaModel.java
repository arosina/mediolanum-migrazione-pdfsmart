package prgm.cedacri.adeguatezza.internal;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.*;

public class ElementoGrigliaModel extends CommandDataModel {
	private static final long serialVersionUID = 0;
	private StringType  dominio = new StringType();
	private IntegerType valmaxi = new IntegerType();
	private StringType  risulta = new StringType();
	public StringType getDominio() {
		return dominio;
	}
	public void setDominio(StringType dominio) {
		this.dominio = dominio;
	}
	public StringType getRisulta() {
		return risulta;
	}
	public void setRisulta(StringType risulta) {
		this.risulta = risulta;
	}
	public IntegerType getValmaxi() {
		return valmaxi;
	}
	public void setValmaxi(IntegerType valmaxi) {
		this.valmaxi = valmaxi;
	}
}
