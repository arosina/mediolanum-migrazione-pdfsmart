package prgm.ita.p.dac.ricerche.sede;

import prgm.ita.p.dac.display.AbstractRicercaDac;
import prgm.ita.p.dac.facade.Costanti;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class RicercaDac extends AbstractRicercaDac implements MenuCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		RicercaDacModel ricercaModel = (RicercaDacModel)dataModel;
		
		// In spunta preimposto lo stato a "Spedita" (ossia "Da lavorare")
		if(ricercaModel.getFnc().equals(Costanti.FNC_SPUNTA)){
			ricercaModel.getParametri().addCodDescField("uffLavorazione","UfficiSpuntaRicercaDac");
			if(ricercaModel.isPrimaAttivazione() && ricercaModel.getParametri().getStato().isNull())
				ricercaModel.getParametri().setStato(new IntegerType(Costanti.STATO_SPEDITA));
		}else if(ricercaModel.getFnc().isNull()){
			ricercaModel.setFnc(new StringType(Costanti.FNC_RICERCA));
		}
		
		super.execute(userSessionContext, ricercaModel);
		return ricercaModel;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return RicercaDacModel.class;
	}
}
