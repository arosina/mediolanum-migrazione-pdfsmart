package prgm.pdfwebforms.stream;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

/*******************************************************************/
/*******************************************************************/
public class LeggiIlContrattoInFirma extends PdfGenerator {

	/*******************************************************************/
	/*******************************************************************/
	@Override
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		this.caller = "LEGGIILCONTRATTO";
		this.onlyCurrentBasketDispo = true;
		return innerExecute(userSessionContext, dataModel);
	}	
}
