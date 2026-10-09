package prgm.ita.p.dac.ricerche.fb;

import prgm.ita.p.dac.display.AbstractRicercaDac;
import prgm.ita.p.dac.facade.Costanti;
import prgm.ita.p.dac.model.ParamsModel;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.IntegerType;

/***********************************************************************************************/
/***********************************************************************************************/
public class RicercaMom extends AbstractRicercaDac implements MenuCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext,
									CommandDataModel dataModel) throws CommandException {
		
		ClientSessionContext csc = userSessionContext.getClientSessionContext();
		RicercaMomModel ricercaModel = (RicercaMomModel)dataModel;
		ricercaModel.setUfficio(new IntegerType(Costanti.UFFICIO_RETE));
		if(ricercaModel.isPrimaAttivazione()){
			ricercaModel.getParametri().setTipoDac(new IntegerType(Costanti.TIPO_DAC_STANDARD));
			if(!ricercaModel.getTipoDac().isNull())
				ricercaModel.getParametri().setTipoDac(ricercaModel.getTipoDac());
			ricercaModel.getParametri().setDataFine(new DateType());
		}
		ricercaModel.removeParam(ParamsModel.storicizzato);		
		return super.execute(userSessionContext, ricercaModel);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return RicercaMomModel.class;
	}
}
