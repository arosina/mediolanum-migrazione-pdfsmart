package prgm.ita.anagraficaclienti.stampamoduli;

import prgm.ita.anagraficaclienti.facade.AnagraficaClientiFacade;
import prgm.ita.anagraficaclienti.facade.VerificheAnagrafica;
import prgm.ita.anagraficaclienti.model.AgenteModel;
import prgm.ita.anagraficaclienti.model.ClienteModel;
import prgm.ita.anagraficaclienti.popup.facade.PopupFacade;
import prgm.ita.anagraficaclienti.popup.model.PopupClientiModel;
import prgm.ita.p.dac.popup.model.PopupClienteModel;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.PrintCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TypeError;

/***********************************************************************************************/
/***********************************************************************************************/
public class ModuloPromozionale extends PrintCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		
		try{
		
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			ModuloPromozionaleModel modulo = (ModuloPromozionaleModel)dataModel;
			
			AnagraficaClientiFacade facade = (AnagraficaClientiFacade)ROF.getFacade(csc,AnagraficaClientiFacade.class);		

			//Recupera le informazioni dell'agente 
			modulo.setAgenteCollegato(facade.leggiAgente(csc, new StringType(csc.getUserCode())));
			modulo.setCodAgente(new StringType());

			return modulo;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nel Modulo: "+e;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return ModuloPromozionaleModel.class;
	}
	
}
