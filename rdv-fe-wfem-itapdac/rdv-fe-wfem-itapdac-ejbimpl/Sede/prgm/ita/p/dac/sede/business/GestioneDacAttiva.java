package prgm.ita.p.dac.sede.business;

import prgm.ita.p.dac.display.Dac;
import prgm.ita.p.dac.facade.Costanti;
import prgm.ita.p.dac.facade.DacFacade;
import prgm.ita.p.dac.facade.FacadeLoader;
import prgm.ita.p.dac.model.DacModel;
import prgm.ita.p.dac.model.ParamsModel;
import prgm.ita.p.dac.util.DacTools;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.IntegerType;

/***********************************************************************************************/
/***********************************************************************************************/
public class GestioneDacAttiva extends BusinessCommand implements MenuCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			
			ParamsModel params = (ParamsModel)dataModel;
			if(params.getTipoDac().isNull())
				params.setTipoDac(new IntegerType(Costanti.TIPO_DAC_PERCONTORETE));
			
			DacFacade facade = (DacFacade)FacadeLoader.getFacade(csc, DacFacade.class); 
			DacModel dac = facade.caricaDacAttiva(csc,params);
			
			if(dac.getIsAutoSpuntata().booleanValue())
				DacTools.prossimoDocDaGestire(csc,this,dac,false,false);
			
			setNextCommandClass(Dac.class);
			return dac;
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return ParamsModel.class;
	}
}
