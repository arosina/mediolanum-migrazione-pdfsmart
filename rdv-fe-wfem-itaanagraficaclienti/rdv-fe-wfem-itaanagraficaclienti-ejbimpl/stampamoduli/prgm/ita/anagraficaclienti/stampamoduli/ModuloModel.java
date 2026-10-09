package prgm.ita.anagraficaclienti.stampamoduli;

import prgm.ita.anagraficaclienti.model.AgenteModel;
import prgm.ita.anagraficaclienti.model.ClienteKeyModel;
import prgm.ita.anagraficaclienti.model.ClienteModel;

import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class ModuloModel extends ClienteKeyModel {
	
	private StringType   nomeModulo = new StringType();
	private StringType   formatoPagina = new StringType("A4");
	private StringType   orientamentoPagina = new StringType("horizontal"); 
	private ClienteModel cliente = new ClienteModel();
	private AgenteModel  agenteCollegato = new AgenteModel();
	private AgenteModel  supervisoreAgenteCollegato = new AgenteModel();

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
	public StringType getOrientamentoPagina() {
		return orientamentoPagina;
	}

	public void setOrientamentoPagina(StringType orientamentoPagina) {
		this.orientamentoPagina = orientamentoPagina;
	}

	public AgenteModel getSupervisoreAgenteCollegato() {
		return supervisoreAgenteCollegato;
	}

	public void setSupervisoreAgenteCollegato(AgenteModel supervisoreAgenteCollegato) {
		this.supervisoreAgenteCollegato = supervisoreAgenteCollegato;
	}


}
