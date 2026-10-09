package prgm.ita.p.dac.ricerche.sede;

import prgm.ita.p.dac.display.AbstractRicercaDoc;
import prgm.ita.p.dac.facade.Costanti;
import prgm.ita.p.dac.model.AbstractRicercaDocModel;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class RicercaDoc extends AbstractRicercaDoc implements MenuCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		AbstractRicercaDocModel ricercaModel = (AbstractRicercaDocModel)dataModel;
		ricercaModel.setFnc(new StringType(Costanti.FNC_RICERCA));
		super.execute(userSessionContext, ricercaModel);
		return ricercaModel;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return RicercaDocModel.class;
	}
}
