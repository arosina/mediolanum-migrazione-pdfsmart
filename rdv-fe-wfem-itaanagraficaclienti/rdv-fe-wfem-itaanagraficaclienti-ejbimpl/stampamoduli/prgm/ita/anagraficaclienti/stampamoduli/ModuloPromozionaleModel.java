package prgm.ita.anagraficaclienti.stampamoduli;

import com.atosorigin.wfem.types.StringType;

import prgm.ita.anagraficaclienti.model.AgenteModel;
import prgm.ita.anagraficaclienti.model.ClienteKeyModel;
import prgm.ita.anagraficaclienti.model.ClienteModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class ModuloPromozionaleModel extends ClienteKeyModel {
	
	private StringType   nomeModulo = new StringType();
	private StringType   formatoPagina = new StringType("A4");
	private ClienteModel cliente = new ClienteModel();
	private AgenteModel  agenteCollegato = new AgenteModel();
	//Ticket 1007052
	private StringType 		flagCodicePromo 		= new StringType();
	private	StringType		codicePromo				= new StringType();
	//Ticket 1007052

	public StringType getNomeModulo() {
		return nomeModulo;
	}

	public void setNomeModulo(StringType nomeModulo) {
		this.nomeModulo = nomeModulo;
	}

	public ClienteModel getCliente() {
		return cliente;
	}

	public void setCliente(ClienteModel cliente) {
		this.cliente = cliente;
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
	//Ticket 1007052
	public StringType getFlagCodicePromo() {
		return flagCodicePromo;
	}

	public void setFlagCodicePromo(StringType flagCodicePromo) {
		this.flagCodicePromo = flagCodicePromo;
	}

	public StringType getCodicePromo() {
		return codicePromo;
	}

	public void setCodicePromo(StringType codicePromo) {
		this.codicePromo = codicePromo;
	}
	//Ticket 1007052
}
