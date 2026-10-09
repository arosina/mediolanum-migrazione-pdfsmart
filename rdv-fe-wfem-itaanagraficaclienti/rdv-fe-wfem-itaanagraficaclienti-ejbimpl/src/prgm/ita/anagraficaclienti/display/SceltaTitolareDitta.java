package prgm.ita.anagraficaclienti.display;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.exceptions.DAOException;

import prgm.ita.anagraficaclienti.cogestione.CogestioneDataManager;
import prgm.ita.anagraficaclienti.cogestione.CogestioneDataModel;
import prgm.ita.anagraficaclienti.model.ClienteModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class SceltaTitolareDitta extends DisplayCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClienteModel model = (ClienteModel)dataModel;
			CogestioneDataModel cogestioneData = model.getCogestioneData();
			CogestioneDataManager.initContrattoCogestione(userSessionContext.getClientSessionContext(), cogestioneData);
			return model;
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO in SceltaTitolareDitta: "+daoe;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return ClienteModel.class;
	}
}
