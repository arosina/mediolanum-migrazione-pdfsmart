package prgm.ita.p.dac.ricerche.sede;

import prgm.ita.p.dac.business.ApriDac;
import prgm.ita.p.dac.display.AbstractRicercaDac;
import prgm.ita.p.dac.model.AbstractRicercaDacModel;
import prgm.ita.p.dac.model.DacModel;
import prgm.ita.p.dac.model.ParamsModel;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ListType;

/***********************************************************************************************/
/***********************************************************************************************/
public class EseguiRicercaDac extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			AbstractRicercaDacModel ricercaModel = (AbstractRicercaDacModel)dataModel;
			ricercaModel = (AbstractRicercaDacModel)AbstractRicercaDac.search(userSessionContext,ricercaModel);
			if(!ricercaModel.getFnc().isNull() && ricercaModel.getElencoDac().size() == 1 && ricercaModel.getTabNum().intValue() == 0){
				setNextCommandClass(ApriDac.class);
				DacModel dac = (DacModel)ricercaModel.getElencoDac().get(0);
				dac.copyParams(ricercaModel);
				dac.addParam(ParamsModel.showBack);
				ricercaModel.setElencoDac(new ListType(DacModel.class));
				ricercaModel.getElencoDac().add(dac);
				return dac;
			}else{
				ricercaModel.setDoSearch(new BooleanType(false));
				setForwardDisplay(new Integer(0),true);
				return ricercaModel;
			}
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return CommandDataModel.class;
	}

}
