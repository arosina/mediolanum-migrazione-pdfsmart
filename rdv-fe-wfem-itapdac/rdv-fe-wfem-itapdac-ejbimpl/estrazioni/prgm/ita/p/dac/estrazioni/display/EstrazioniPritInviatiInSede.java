package prgm.ita.p.dac.estrazioni.display;

import javax.ejb.EJBException;

import prgm.ita.p.dac.estrazioni.model.EstrazioniModel;
import prgm.ita.p.dac.estrazioni.model.PritInviatiInSedeModel;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;

public class EstrazioniPritInviatiInSede extends DisplayCommand {

	public CommandDataModel execute(UserSessionContext userSessionContext,
			CommandDataModel dataModel) throws CommandException {
		ClientSessionContext csc = userSessionContext.getClientSessionContext();
		PritInviatiInSedeModel model=(PritInviatiInSedeModel)dataModel;
		
		return model;
	}

	public Class getInputViewClass() {
		//return EstrazioniModel.class;
		return PritInviatiInSedeModel.class;
	}

	
}
