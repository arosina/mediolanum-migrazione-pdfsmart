package prgm.ita.anagraficaclienti.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.*;

/***********************************************************************************************/
/***********************************************************************************************/
public class VariazioniModel extends CommandDataModel {

	private ListType elencoVariazioni = new ListType(ClienteModel.class);
	
	public ListType getElencoVariazioni() {
		return elencoVariazioni;
	}

	public void setElencoVariazioni(ListType elencoVariazioni) {
		this.elencoVariazioni = elencoVariazioni;
	}

}
