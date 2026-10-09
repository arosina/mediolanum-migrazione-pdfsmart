package prgm.ita.p.dac.popup.model;

import prgm.ita.p.dac.model.AgenteModel;
import prgm.ita.p.dac.model.ClienteModel;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

/********************************************************************************/
/********************************************************************************/
public class PopupContrattoModel extends CommandDataModel {
	
	private AgenteModel 	agente = new AgenteModel();
	private StringType 		tipoProdotto = new StringType();
	private StringType 		numeroContratto = new StringType();
	private StringType 		numeroPolizza = new StringType();
	private StringType 		codProdotto = new StringType();
	private StringType 		descrContratto = new StringType();
	private ClienteModel 	cliente = new ClienteModel();
	private StringType 		numeroContrattoEsteso = new StringType();
	private IntegerType 	isChiuso = new IntegerType();
	
	public AgenteModel getAgente() {
		return agente;
	}
	public void setAgente(AgenteModel agente) {
		this.agente = agente;
	}
	public StringType getTipoProdotto() {
		return tipoProdotto;
	}
	public void setTipoProdotto(StringType tipoProdotto) {
		this.tipoProdotto = tipoProdotto;
	}
	public StringType getNumeroContratto() {
		return numeroContratto;
	}
	public void setNumeroContratto(StringType numeroContratto) {
		this.numeroContratto = numeroContratto;
	}
	public ClienteModel getCliente() {
		return cliente;
	}
	public void setCliente(ClienteModel cliente) {
		this.cliente = cliente;
	}
	public StringType getNumeroContrattoEsteso() {
		return numeroContrattoEsteso;
	}
	public void setNumeroContrattoEsteso(StringType numeroContrattoEsteso) {
		this.numeroContrattoEsteso = numeroContrattoEsteso;
	}
	public StringType getCodProdotto() {
		return codProdotto;
	}
	public void setCodProdotto(StringType codProdotto) {
		this.codProdotto = codProdotto;
	}
	public StringType getNumeroPolizza() {
		return numeroPolizza;
	}
	public void setNumeroPolizza(StringType numeroPolizza) {
		this.numeroPolizza = numeroPolizza;
	}
	public StringType getDescrContratto() {
		return descrContratto;
	}
	public void setDescrContratto(StringType descrContratto) {
		this.descrContratto = descrContratto;
	}
	public IntegerType getIsChiuso() {
		return isChiuso;
	}
	public void setIsChiuso(IntegerType isChiuso) {
		this.isChiuso = isChiuso;
	}
}
