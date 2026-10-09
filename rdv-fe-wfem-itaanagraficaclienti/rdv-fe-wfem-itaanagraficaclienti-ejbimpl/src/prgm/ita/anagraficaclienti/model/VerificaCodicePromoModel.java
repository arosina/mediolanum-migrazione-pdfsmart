package prgm.ita.anagraficaclienti.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.BooleanType;

/***********************************************************************************************/
/***********************************************************************************************/
public class VerificaCodicePromoModel extends CommandDataModel {

	private StringType codicePromo = new StringType();
	private BooleanType flagCodiceValido = new BooleanType();

	
	
	public StringType getCodicePromo() {
		return codicePromo;
	}
	public void setCodicePromo(StringType codicePromo) {
		this.codicePromo = codicePromo;
	}
	public BooleanType getFlagCodiceValido() {
		return flagCodiceValido;
	}
	public void setFlagCodiceValido(BooleanType flagCodiceValido) {
		this.flagCodiceValido = flagCodiceValido;
	}
}
