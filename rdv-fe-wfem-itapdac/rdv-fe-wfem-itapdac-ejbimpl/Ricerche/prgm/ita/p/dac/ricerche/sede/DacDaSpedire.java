package prgm.ita.p.dac.ricerche.sede;

import prgm.ita.p.dac.display.AbstractRicercaDac;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class DacDaSpedire extends AbstractRicercaDac implements MenuCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {

		DacDaSpedireModel ricercaModel = (DacDaSpedireModel)dataModel;
		
		ricercaModel = (DacDaSpedireModel)super.execute(userSessionContext, ricercaModel);
		if(ricercaModel.isPrimaAttivazione()){
			ricercaModel.setPrimaAttivazione(false);
			ricercaModel.setDaoAccessName(new StringType("dacDaSpedire"));
			ricercaModel.setDoSearch(new BooleanType(true));
			ricercaModel = (DacDaSpedireModel)super.execute(userSessionContext, ricercaModel);
			ricercaModel.setDoSearch(new BooleanType(false));
		}else{
			ricercaModel = (DacDaSpedireModel)super.execute(userSessionContext, ricercaModel);			
		}
		return ricercaModel;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return DacDaSpedireModel.class;
	}
}
