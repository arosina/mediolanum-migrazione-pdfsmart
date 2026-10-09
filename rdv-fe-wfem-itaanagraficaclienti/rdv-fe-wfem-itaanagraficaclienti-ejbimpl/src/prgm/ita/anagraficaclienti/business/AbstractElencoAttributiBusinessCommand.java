package prgm.ita.anagraficaclienti.business;

import prgm.ita.anagraficaclienti.model.AbstractElencoAttributiModel;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.IntegerType;

/***********************************************************************************************/
/***********************************************************************************************/
public abstract class AbstractElencoAttributiBusinessCommand extends BusinessCommand {

	public abstract CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException;
	public abstract Class getInputViewClass();

	/***********************************************************************************************/
	/***********************************************************************************************/
	protected void nuovoAttributo(AbstractElencoAttributiModel elencoAttributiModel, CommandDataModel attributoModel){
		elencoAttributiModel.setIdx(new IntegerType(elencoAttributiModel.getElencoAttributi().size()));
		elencoAttributiModel.getElencoAttributi().add(attributoModel);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	protected void eliminaAttributo(AbstractElencoAttributiModel elencoAttributiModel){
		int idx = elencoAttributiModel.getIdx().intValue();
		Object attributo = elencoAttributiModel.getElencoAttributi().get(idx);
		elencoAttributiModel.getElencoAttributiCancellati().add(attributo);
		elencoAttributiModel.getElencoAttributi().getElements().remove(idx);
		if(idx >= elencoAttributiModel.getElencoAttributi().size())
			idx--;
		elencoAttributiModel.setIdx(new IntegerType(idx));
	}
}
