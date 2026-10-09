package prgm.ita.anagraficaclienti.business;

import prgm.ita.anagraficaclienti.display.AnagraficaCliente;
import prgm.ita.anagraficaclienti.model.ClienteModel;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.layout.Template;

/*******************************************************************/
/*******************************************************************/
public class PulisciTitolare extends BusinessCommand {

	/*******************************************************************/
	/*******************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel)	throws CommandException {
		
		ClienteModel cliente = (ClienteModel)dataModel;
		try {
			
			cliente.getDatiApplicativi().setSceltaTipologiaCensimentoDittaEffettuata(true);
			cliente.setClienteTitolare(null);
			
			cliente.setModality(Template.UPDATE_MODALITY);
			setNextCommandClass(AnagraficaCliente.class);
			return cliente;
			
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nel pulire il titolare: "+e;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}
	}

	/*******************************************************************/
	/*******************************************************************/
	public Class getInputViewClass() {
		return ClienteModel.class;
	}

}
