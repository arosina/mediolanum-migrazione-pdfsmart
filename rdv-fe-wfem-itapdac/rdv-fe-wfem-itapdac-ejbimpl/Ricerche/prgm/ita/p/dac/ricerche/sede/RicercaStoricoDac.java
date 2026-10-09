package prgm.ita.p.dac.ricerche.sede;

import prgm.ita.p.dac.display.AbstractRicercaDac;
import prgm.ita.p.dac.facade.Costanti;
import prgm.ita.p.dac.model.AbstractRicercaDacModel;
import prgm.ita.p.dac.model.ParamsModel;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.StringType;


/***********************************************************************************************/
/***********************************************************************************************/
public class RicercaStoricoDac extends AbstractRicercaDac implements MenuCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext,
									CommandDataModel dataModel) throws CommandException {
		AbstractRicercaDacModel ricercaModel = (AbstractRicercaDacModel)dataModel;
		ricercaModel.setFnc(new StringType(Costanti.FNC_RICERCA));
		ricercaModel.addParam(ParamsModel.storicizzato);
		ricercaModel.setTipoRicerca(new StringType("storica"));
		super.execute(userSessionContext, ricercaModel);
		return ricercaModel;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return RicercaStoricoDacModel.class;
	}
}
