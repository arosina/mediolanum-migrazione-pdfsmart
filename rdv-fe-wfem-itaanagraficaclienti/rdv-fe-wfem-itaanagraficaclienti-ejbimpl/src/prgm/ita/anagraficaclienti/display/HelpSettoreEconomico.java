package prgm.ita.anagraficaclienti.display;

import com.atosorigin.wfem.coddesc.CodDescDataList;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;

import prgm.ita.anagraficaclienti.facade.InfoLoader;
import prgm.ita.anagraficaclienti.model.ClienteModel;

public class HelpSettoreEconomico extends DisplayCommand {

	public CommandDataModel execute(UserSessionContext userSessionContext,	CommandDataModel dataModel) throws CommandException {
		ClientSessionContext csc = userSessionContext.getClientSessionContext();
		ClienteModel cli = new ClienteModel();
		cli.getDatiApplicativi().setCensimentoDitta(true);
		CodDescDataList listSettoriEconomici = InfoLoader.leggiCodDescListSettoriEconomici(csc, cli, true);
		cli.getInfoPersonali().addCodDescField("codSettoreEconomico",listSettoriEconomici);
		return cli;
	}

	public Class getInputViewClass() {
		return null;
	}

	public boolean isNoSubmitCommand() {
		return true;
	}

}
