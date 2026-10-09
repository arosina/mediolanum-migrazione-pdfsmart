package prgm.ita.anagraficaclienti.stampamoduli;

import prgm.ita.anagraficaclienti.facade.VerificheAnagrafica;
import prgm.ita.anagraficaclienti.stampamoduli.Costanti;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.MenuCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.types.BooleanType;


/*
 * Funzione che permette di vedere gli agenti che un cliente ha e ha avuto
 */
/***********************************************************************************************/
/***********************************************************************************************/
public class StampaModuloPromozionale extends DisplayCommand implements MenuCommand{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext,
									CommandDataModel dataModel) throws CommandException {
		
		try{
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			StampaModuloPromozionaleModel stampaModuloPromozionaleModel = (StampaModuloPromozionaleModel)dataModel;
			
			stampaModuloPromozionaleModel.setNomeModulo(Costanti.NOME_MODULO_MGM);
			stampaModuloPromozionaleModel.setFlagCodicePromoValido(new BooleanType(false));
			if (!stampaModuloPromozionaleModel.getCodicePromo().isNull())
				if (VerificheAnagrafica.verificaCodPromo(csc, stampaModuloPromozionaleModel.getCodicePromo()))
					stampaModuloPromozionaleModel.setFlagCodicePromoValido(new BooleanType(true));
				
			
			return stampaModuloPromozionaleModel;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nella verifica del codice promozionale: "+e;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return StampaModuloPromozionaleModel.class;
	}
}
