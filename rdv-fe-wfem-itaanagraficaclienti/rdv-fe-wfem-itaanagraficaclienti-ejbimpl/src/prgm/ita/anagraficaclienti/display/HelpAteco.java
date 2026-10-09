package prgm.ita.anagraficaclienti.display;

import prgm.ita.anagraficaclienti.facade.InfoLoader;
import prgm.ita.anagraficaclienti.model.ClienteModel;

import com.atosorigin.wfem.coddesc.CodDescDataList;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;

public class HelpAteco extends DisplayCommand {

	public CommandDataModel execute(UserSessionContext userSessionContext,	CommandDataModel dataModel) throws CommandException {
		
		ClientSessionContext csc = userSessionContext.getClientSessionContext();
		
		ClienteModel cliente = (ClienteModel)dataModel;
		ClienteModel clienteXAteco = new ClienteModel();
		
		clienteXAteco.getInfoPersonali().setCodSottogruppoAttivita(cliente.getInfoPersonali().getCodSottogruppoAttivita());

		CodDescDataList listCodiciAteco = InfoLoader.leggiCodDescListAteco(csc, clienteXAteco, true);

		clienteXAteco.getInfoPersonali().addCodDescField("codAteco",listCodiciAteco);

		return clienteXAteco;
	}

	public Class getInputViewClass() {
		return ClienteModel.class;
	}

	public boolean isNoSubmitCommand() {
		return true;
	}

}
