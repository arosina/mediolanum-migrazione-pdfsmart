package prgm.ita.anagraficaclienti.popup.model;

import prgm.ita.anagraficaclienti.model.ClienteKeyModel;
import prgm.ita.anagraficaclienti.model.IndirizziModel;
import prgm.ita.anagraficaclienti.model.IndirizzoModel;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.ListType;

/***********************************************************************************************/
/***********************************************************************************************/
public class PopupIndirizziClienteModel extends CommandDataModel {

	private ClienteKeyModel cliente = new ClienteKeyModel();
	private IndirizziModel indirizzi = new IndirizziModel();
	
	public ClienteKeyModel getCliente() {
		return cliente;
	}
	public void setCliente(ClienteKeyModel cliente) {
		this.cliente = cliente;
	}
	public IndirizziModel getIndirizzi() {
		return indirizzi;
	}
	public void setIndirizzi(IndirizziModel indirizzi) {
		this.indirizzi = indirizzi;
	}
	
}
