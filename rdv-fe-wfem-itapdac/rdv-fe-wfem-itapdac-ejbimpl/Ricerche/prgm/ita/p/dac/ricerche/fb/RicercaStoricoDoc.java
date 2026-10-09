package prgm.ita.p.dac.ricerche.fb;

import prgm.ita.p.dac.display.AbstractRicercaDoc;
import prgm.ita.p.dac.facade.Costanti;
import prgm.ita.p.dac.model.AbstractRicercaDocModel;
import prgm.ita.p.dac.model.ParamsModel;
import prgm.ita.p.dac.ricerche.sede.RicercaStoricoDocModel;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

/***********************************************************************************************/
/***********************************************************************************************/
public class RicercaStoricoDoc extends AbstractRicercaDoc implements MenuCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		AbstractRicercaDocModel ricercaModel = (AbstractRicercaDocModel)dataModel;
		ricercaModel.setFnc(new StringType(Costanti.FNC_RICERCA));
		ricercaModel.addParam(ParamsModel.storicizzato);
		ricercaModel.setUfficio(new IntegerType(Costanti.UFFICIO_RETE));
		ricercaModel.setTipoRicerca(new StringType("storica"));
		String codAgente = Tools.fillSx(userSessionContext.getClientSessionContext().getCurrentLinkedUserCode(),'0',10);
		ricercaModel.getParametri().setCodiceAgente(new StringType(codAgente));
		super.execute(userSessionContext, ricercaModel);
		return ricercaModel;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return RicercaStoricoDocModel.class;
	}
}
