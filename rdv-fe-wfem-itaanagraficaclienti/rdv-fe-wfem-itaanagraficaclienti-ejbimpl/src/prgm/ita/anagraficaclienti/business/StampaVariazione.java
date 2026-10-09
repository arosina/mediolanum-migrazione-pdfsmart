package prgm.ita.anagraficaclienti.business;

import prgm.ita.anagraficaclienti.facade.AnagraficaClientiFacade;
import prgm.ita.anagraficaclienti.facade.Costanti;
import prgm.ita.anagraficaclienti.model.ClienteModel;
import prgm.ita.anagraficaclienti.model.VariazioneKeyModel;
import prgm.ita.anagraficaclienti.print.Ditta;
import prgm.ita.anagraficaclienti.print.PersonaFisica;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

/*******************************************************************/
/*******************************************************************/
public class StampaVariazione extends BusinessCommand {

	/*******************************************************************/
	/*******************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {

		VariazioneKeyModel variazioneKey = (VariazioneKeyModel)dataModel;
		try{
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();

			AnagraficaClientiFacade facade = (AnagraficaClientiFacade)ROF.getFacade(csc,AnagraficaClientiFacade.class);
			ClienteModel model = facade.leggiVariazione(csc, variazioneKey);
			
			model.getDatiApplicativi().setTipoStampa(Costanti.STAMPA_VARIAZIONE);	
				
			if(model.getIsDitta().booleanValue())
				setNextCommandClass(Ditta.class);
			else
				setNextCommandClass(PersonaFisica.class);
			return model;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nella stampa variazione n° ["+variazioneKey.getProgressivo()+"] ";
				   errorMsg += "del cliente ["+variazioneKey.getCodPotenziale()+"] ";
				   errorMsg += "codFiscale=["+variazioneKey.getCodFiscale()+"] ";
				   errorMsg += "partitaIva=["+variazioneKey.getPartitaIva()+"]: "+e;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}
	}

	/*******************************************************************/
	/*******************************************************************/
	public Class getInputViewClass() {
		return VariazioneKeyModel.class;
	}

}
