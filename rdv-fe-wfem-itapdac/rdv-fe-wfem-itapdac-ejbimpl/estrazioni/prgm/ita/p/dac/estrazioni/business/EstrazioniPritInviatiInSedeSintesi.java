package prgm.ita.p.dac.estrazioni.business;

import prgm.ita.p.dac.estrazioni.display.EstrazioniPritInviatiInSede;
import prgm.ita.p.dac.estrazioni.facade.EstrazioniFacade;
import prgm.ita.p.dac.estrazioni.model.PritInviatiInSedeModel;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.util.FacadeLoader;

public class EstrazioniPritInviatiInSedeSintesi extends BusinessCommand {
	
	public CommandDataModel execute(UserSessionContext userSessionContext,
			CommandDataModel dataModel) throws CommandException {

		try {
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PritInviatiInSedeModel model = (PritInviatiInSedeModel)dataModel;
            
            //chiamata da business a display
            setNextCommandClass(EstrazioniPritInviatiInSede.class);
            return model;
        
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nell' esecuzione del report: "+e;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;			
		}

	}

	

	public Class getInputViewClass() {
		return PritInviatiInSedeModel.class;
	}

}
