package prgm.ita.p.dac.estrazioni.business;

import prgm.ita.p.dac.estrazioni.display.EstrazioniPritInviatiInSede;
import prgm.ita.p.dac.estrazioni.facade.EstrazioniFacade;
import prgm.ita.p.dac.estrazioni.model.PritInviatiInSedeModel;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.FacadeLoader;

public class EstrazioniPritInviatiInSedeStart extends BusinessCommand implements MenuCommand {
	
	public CommandDataModel execute(UserSessionContext userSessionContext,
			CommandDataModel dataModel) throws CommandException {

		try {
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PritInviatiInSedeModel model = (PritInviatiInSedeModel)dataModel;
			DAOObject dao = new DAOObject(csc,"ItaPDac.Estrazioni");
            EstrazioniFacade facade = (EstrazioniFacade)FacadeLoader.getFacade(csc,EstrazioniFacade.class);
            
            dao.fillCodDesc(model);
            
            model.setTipoEstrazione(new StringType("sintesi"));
            
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
