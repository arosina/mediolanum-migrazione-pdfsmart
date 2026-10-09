package prgm.ita.p.dac.business;

import prgm.ita.p.dac.model.DacModel;
import prgm.ita.p.dac.model.DocumentoModel;
import prgm.ita.p.dac.util.DacTools;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public class ChiudiDocumento extends BusinessCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		try{
			DacModel dac = (DacModel)dataModel;
			
			// Salvo il documento corrente se l'utente l'ha modificato senza salvarlo
			dac = DacTools.salvaDocumentoModificato(userSessionContext, dac);
			if(dac.getDocumento().hasCommandErrors()){
				setForwardDisplay(new Integer(0));
				return dac;					
			}
			boolean visualizzaAlert = dac.getDocumento().isVisualizzaAlert();
			dac.setDocumento(new DocumentoModel());
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
