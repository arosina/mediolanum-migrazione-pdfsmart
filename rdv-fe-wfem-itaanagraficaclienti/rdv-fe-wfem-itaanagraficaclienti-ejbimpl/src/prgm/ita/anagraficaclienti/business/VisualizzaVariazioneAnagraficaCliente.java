package prgm.ita.anagraficaclienti.business;

import prgm.ita.anagraficaclienti.display.AnagraficaCliente;
import prgm.ita.anagraficaclienti.facade.AnagraficaClientiFacade;
import prgm.ita.anagraficaclienti.model.ClienteModel;
import prgm.ita.anagraficaclienti.model.VariazioneKeyModel;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

/*******************************************************************/
/*******************************************************************/
public class VisualizzaVariazioneAnagraficaCliente extends BusinessCommand {

	/*******************************************************************/
	/*******************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel)	throws CommandException {
		
		VariazioneKeyModel variazioneKey = (VariazioneKeyModel)dataModel;
		try{
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();

			AnagraficaClientiFacade facade = (AnagraficaClientiFacade)ROF.getFacade(csc,AnagraficaClientiFacade.class);
			ClienteModel model = facade.leggiVariazione(csc, variazioneKey);
			model.getDatiApplicativi().setMessaggioCentrale("###");
			model.setModality(model.READ_MODALITY);
			
			setNextCommandClass(AnagraficaCliente.class);
			return model;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nel popup variazione n° ["+variazioneKey.getProgressivo()+"] ";
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
