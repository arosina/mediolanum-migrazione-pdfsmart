package prgm.ita.p.dac.display;

import prgm.ita.p.dac.facade.DacFacade;
import prgm.ita.p.dac.facade.FacadeLoader;
import prgm.ita.p.dac.model.DacKeyModel;
import prgm.ita.p.dac.model.DacModel;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.layout.Template;

/***********************************************************************************************/
/***********************************************************************************************/
public class QuickViewDac extends DisplayCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			DacKeyModel dacKey = (DacKeyModel)dataModel;

			DacFacade facade = (DacFacade)FacadeLoader.getFacade(csc, DacFacade.class);
			DacModel dac = facade.leggiDac(csc,dacKey);

			facade.fillCodDesc(csc,dac,true);
			dac.setModality(Template.READ_MODALITY);
			return dac;
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return DacKeyModel.class;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isNoSubmitCommand() {
		return true;
	}

}
