package prgm.ita.anagraficaclienti.flussofatca;

import prgm.ita.anagraficaclienti.model.ClienteModel;
import prgm.ita.anagraficaclienti.model.DatiFatcaModel;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public class GoOn extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClienteModel model = (ClienteModel)dataModel;
			DatiFatcaModel datiFatca = model.getDatiFatca();
			AbstractNavigatore navig = (AbstractNavigatore)Class.forName("prgm.ita.anagraficaclienti.flussofatca."+datiFatca.getFatcaNavigatorName()).newInstance();
			navig.doStep(model);
			setNextCommandClass(Class.forName("prgm.ita.anagraficaclienti.flussofatca."+datiFatca.getFatcaPopupName()));
			return dataModel;
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return ClienteModel.class;
	}

}
