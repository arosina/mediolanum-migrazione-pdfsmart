package prgm.ita.anagraficaclienti.stampamoduli;

import prgm.ita.anagraficaclienti.model.AgenteModel;
import prgm.ita.anagraficaclienti.popup.model.PopupClientiModel;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class StampaModuloPromozionaleModel extends CommandDataModel {
	
	private BooleanType flagCodicePromoValido = new BooleanType();
	private StringType nomeModulo = new StringType();
	private StringType formatoPagina = new StringType("A4");
	private AgenteModel agenteCollegato = new AgenteModel();
	private	StringType codicePromo = new StringType();


	public BooleanType getFlagCodicePromoValido() {
		return flagCodicePromoValido;
	}

	public void setFlagCodicePromoValido(BooleanType flagCodicePromoValido) {
		this.flagCodicePromoValido = flagCodicePromoValido;
	}

	public StringType getNomeModulo() {
		return nomeModulo;
	}

	public void setNomeModulo(StringType nomeModulo) {
		this.nomeModulo = nomeModulo;
	}

	public StringType getFormatoPagina() {
		return formatoPagina;
	}

	public void setFormatoPagina(StringType formatoPagina) {
		this.formatoPagina = formatoPagina;
	}

	public AgenteModel getAgenteCollegato() {
		return agenteCollegato;
	}

	public void setAgenteCollegato(AgenteModel agenteCollegato) {
		this.agenteCollegato = agenteCollegato;
	}

	public StringType getCodicePromo() {
		return codicePromo;
	}

	public void setCodicePromo(StringType codicePromo) {
		this.codicePromo = codicePromo;
	}
}
