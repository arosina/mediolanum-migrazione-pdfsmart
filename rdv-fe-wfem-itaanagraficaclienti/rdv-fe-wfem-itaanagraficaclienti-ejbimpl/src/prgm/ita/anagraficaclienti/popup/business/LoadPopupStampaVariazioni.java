package prgm.ita.anagraficaclienti.popup.business;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.util.Tools;

import prgm.ita.anagraficaclienti.business.StampaVariazione;
import prgm.ita.anagraficaclienti.model.ClienteKeyModel;
import prgm.ita.anagraficaclienti.model.ClienteModel;
import prgm.ita.anagraficaclienti.model.VariazioneKeyModel;
import prgm.ita.anagraficaclienti.popup.display.PopupStampaVariazioni;

/***********************************************************************************************/
/***********************************************************************************************/
public class LoadPopupStampaVariazioni extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext,
									CommandDataModel dataModel) throws CommandException {
		
		try{
		
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			ClienteKeyModel cliKey = (ClienteKeyModel)dataModel;
			ClienteModel cli = new ClienteModel();
			Tools.copyCommandDataModel(cliKey,cli);
			DAOObject dao = new DAOObject(csc,"ItaAnagraficaClienti.AnagraficaClienti");
			DAOQueryResultModel result = dao.executeQueryAccess("loadVariazioni",cli);
			if(result.getResult().size() == 1){
				ClienteModel var = (ClienteModel)result.getResult().get(0);
				if(var.isDigitale()){
					cli.getDatiApplicativi().setMessaggioCentrale("L'unica variazione e' in firma digitale. La stampa non e' disponibile");
				}else{
					VariazioneKeyModel varKey = new VariazioneKeyModel();
					Tools.copyCommandDataModel(var,varKey);
					StampaVariazione sv = new StampaVariazione();
					var = (ClienteModel)sv.execute(userSessionContext,varKey);
					setNextCommandObject(sv.getNextCommandObject());
					return var;
				}
			}
			cli.getVariazioni().setElencoVariazioni(result.getResult());
			setNextCommandClass(PopupStampaVariazioni.class);
			return cli;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO nell'elenco variazioni: "+daoe;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nell'elenco variazioni: "+e;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return ClienteKeyModel.class;
	}

}
