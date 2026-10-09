package prgm.ita.anagraficaclienti.business;

import prgm.ita.anagraficaclienti.display.AnagraficaDatoreDiLavoro;
import prgm.ita.anagraficaclienti.display.SceltaTitolareDitta;
import prgm.ita.anagraficaclienti.facade.AnagraficaClientiFacade;
import prgm.ita.anagraficaclienti.model.ClienteModel;
import prgm.ita.anagraficaclienti.model.ParametriApplicazioneAnagrafica;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;

/*
 * Censimento anagrafico ditte
 */
/***********************************************************************************************/
/***********************************************************************************************/
public class NuovaAnagraficaDitta extends BusinessCommand  implements MenuCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try {
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			ParametriApplicazioneAnagrafica params = (ParametriApplicazioneAnagrafica)dataModel;
			
			AnagraficaClientiFacade facade = (AnagraficaClientiFacade)ROF.getFacade(csc,AnagraficaClientiFacade.class);
			ClienteModel model = facade.nuovaDitta(csc,params);
			
			model.setPopupMode(params.getPopupMode());
			model.setShowBack(params.getShowBack());
			model.setCallingAppl(params.getCallingAppl());
			model.setRuntimeModality(params.getRuntimeModality());
			
			if(model.getIsDatoreDiLavoro().booleanValue())
				setNextCommandClass(AnagraficaDatoreDiLavoro.class);
			else
				setNextCommandClass(SceltaTitolareDitta.class);
			return model;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nell'inizializzare un nuovo cliente: "+e;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return ParametriApplicazioneAnagrafica.class;
	}

}
