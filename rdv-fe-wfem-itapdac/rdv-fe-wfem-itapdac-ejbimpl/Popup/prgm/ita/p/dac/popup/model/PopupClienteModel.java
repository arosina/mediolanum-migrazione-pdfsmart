package prgm.ita.p.dac.popup.model;

import prgm.ita.p.dac.model.AgenteModel;
import prgm.ita.p.dac.model.ClienteModel;

import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.StringType;

/********************************************************************************/
/********************************************************************************/
public class PopupClienteModel extends ClienteModel {
	
	private StringType 	codCluster = new StringType();
	private StringType 	codFiscale = new StringType();
	private StringType 	partitaIva = new StringType();
	private DateType 	dataNascita = new DateType();
	
	private AgenteModel agente = new AgenteModel();

	/***********************************************************************************************/
	/***********************************************************************************************/
	public PopupClienteModel(){
		addCodDescField("codCluster","CLUSTER");
	}

	public StringType getCodCluster() {
		return codCluster;
	}

	public void setCodCluster(StringType codCluster) {
		this.codCluster = codCluster;
	}

	public StringType getCodFiscale() {
		return codFiscale;
	}

	public void setCodFiscale(StringType codFiscale) {
		this.codFiscale = codFiscale;
	}

	public StringType getPartitaIva() {
		return partitaIva;
	}

	public void setPartitaIva(StringType partitaIva) {
		this.partitaIva = partitaIva;
	}

	public AgenteModel getAgente() {
		return agente;
	}

	public void setAgente(AgenteModel agente) {
		this.agente = agente;
	}

	public DateType getDataNascita() {
		return dataNascita;
	}

	public void setDataNascita(DateType dataNascita) {
		this.dataNascita = dataNascita;
	}
	
}
