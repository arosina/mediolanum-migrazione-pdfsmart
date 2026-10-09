package prgm.ita.anagraficaclienti.business;

import com.atosorigin.wfem.coddesc.CodDescDataList;
import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.StringType;

import prgm.ita.anagraficaclienti.facade.InfoLoader;
import prgm.ita.anagraficaclienti.model.ClienteModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class AggiornaElencoSettoriEconomici extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext,
									CommandDataModel dataModel) throws CommandException {
		
		ClientSessionContext csc = userSessionContext.getClientSessionContext();
		ClienteModel cliente = (ClienteModel)dataModel;
		cliente.getInfoPersonali().setCodSettoreEconomico(new StringType());
		
		CodDescDataList listSettoriEconomici = InfoLoader.leggiCodDescListSettoriEconomici(csc, cliente, false);
		cliente.getInfoPersonali().addCodDescField("codSettoreEconomico",listSettoriEconomici);

		setForwardDisplay(0, false);
		return cliente;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return ClienteModel.class;
	}

}
