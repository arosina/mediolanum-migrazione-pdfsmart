package prgm.ita.anagraficaclienti.allegafoto;

import prgm.ita.anagraficaclienti.facade.AnagraficaClientiFacade;
import prgm.ita.anagraficaclienti.model.ClienteModel;
import prgm.ita.anagraficaclienti.popup.model.PopupClientiModel;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.UserSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public class DettaglioClientePerFoto extends DisplayCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext,
									CommandDataModel dataModel) throws CommandException {
		
		try{
		
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			AnagraficaClientiFacade facade = (AnagraficaClientiFacade)ROF.getFacade(csc,AnagraficaClientiFacade.class);

			PopupClientiModel popupClientiModel = (PopupClientiModel)dataModel;
			ClienteModel clienteSelezionato = popupClientiModel.getClienteSelezionato();
			ClienteModel cliente = new ClienteModel();
			cliente.setCodAgente(clienteSelezionato.getCodAgente());
			cliente.setCodFiscale(clienteSelezionato.getCodFiscale());
			cliente = facade.leggiFotografieCliente(csc,cliente);
			popupClientiModel.setClienteSelezionato(cliente);
			return popupClientiModel;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in DettaglioClientePerFoto: "+e;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PopupClientiModel.class;
	}

}
