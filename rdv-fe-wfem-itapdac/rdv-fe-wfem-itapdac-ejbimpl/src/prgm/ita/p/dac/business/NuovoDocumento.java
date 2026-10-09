package prgm.ita.p.dac.business;

import prgm.ita.p.dac.facade.DacFacade;
import prgm.ita.p.dac.facade.FacadeLoader;
import prgm.ita.p.dac.model.DacModel;
import prgm.ita.p.dac.util.DacTools;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public class NuovoDocumento extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			DacModel dac = (DacModel)dataModel;
			
			String message = dac.getDocumento().getMessageString();
			boolean visualizzaAlert = dac.getDocumento().isVisualizzaAlert();
			
			// Salvo il documento corrente se l'utente l'ha modificato senza salvarlo
			dac = DacTools.salvaDocumentoModificato(userSessionContext, dac);
			if(dac.getDocumento().hasCommandErrors()){
				setForwardDisplay(new Integer(0));
				return dac;					
			}

			DacFacade facade = (DacFacade)FacadeLoader.getFacade(csc, DacFacade.class);
			dac = facade.nuovoDocumento(csc, dac);

			dac.getDocumento().setVisible(true);
			
			if(message.length() > 0)
				dac.getDocumento().addCommandMessage(message);

			dac.getDocumento().setVisualizzaAlert(visualizzaAlert);
			
			setForwardDisplay(new Integer(0));
			return dac;
		}catch(Exception e){
			throw new CommandException(e.toString());
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return DacModel.class;
	}

}
