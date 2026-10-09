package prgm.pdfwebforms.carrello;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/******************************************************************************/
/******************************************************************************/
public class SottoscrittoreCarrelloModel  extends CommandDataModel {

	StringType codiceCliente = new StringType();
	StringType tipoVariazione = new StringType();

	public StringType getCodiceCliente() {
		return codiceCliente;
	}

	public void setCodiceCliente(StringType codiceCliente) {
		this.codiceCliente = codiceCliente;
	}

	public StringType getTipoVariazione() {
		return tipoVariazione;
	}

	public void setTipoVariazione(StringType tipoVariazione) {
		this.tipoVariazione = tipoVariazione;
	}	
}
