package prgm.pdfwebforms.drivers.io.sostituzioni;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.ListType;


/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class ProvideSostituzioniDataResponse extends CommandDataModel{
	
    private ListType clienti = new ListType(ClienteSostituzioniModel.class);

	public ListType getClienti() {
		return clienti;
	}

	public void setClienti(ListType clienti) {
		this.clienti = clienti;
	}

}
