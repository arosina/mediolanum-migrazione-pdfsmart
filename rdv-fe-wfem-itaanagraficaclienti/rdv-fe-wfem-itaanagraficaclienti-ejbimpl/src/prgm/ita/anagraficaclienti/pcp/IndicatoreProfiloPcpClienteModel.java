package prgm.ita.anagraficaclienti.pcp;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/*****************************************************************************************************/
/*****************************************************************************************************/
public class IndicatoreProfiloPcpClienteModel extends CommandDataModel {

	private StringType codiceIndicatore = new StringType();
	private StringType valoreIndicatore = new StringType();
	
	public StringType getCodiceIndicatore() {
		return codiceIndicatore;
	}
	public void setCodiceIndicatore(StringType codiceIndicatore) {
		this.codiceIndicatore = codiceIndicatore;
	}
	public StringType getValoreIndicatore() {
		return valoreIndicatore;
	}
	public void setValoreIndicatore(StringType valoreIndicatore) {
		this.valoreIndicatore = valoreIndicatore;
	}
}
