package prgm.ita.anagraficaclienti.business;

import prgm.ita.anagraficaclienti.facade.AnagraficaClientiFacade;
import prgm.ita.anagraficaclienti.facade.Costanti;
import prgm.ita.anagraficaclienti.model.ClienteKeyModel;
import prgm.ita.anagraficaclienti.model.ClienteModel;
import prgm.ita.anagraficaclienti.print.Ditta;
import prgm.ita.anagraficaclienti.print.PersonaFisica;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

/*******************************************************************/
/*******************************************************************/
public class StampaCensimento extends BusinessCommand {

	/*******************************************************************/
	/*******************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {

		ClienteKeyModel chiave = (ClienteKeyModel)dataModel;
		try {
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();

			AnagraficaClientiFacade facade = (AnagraficaClientiFacade)ROF.getFacade(csc,AnagraficaClientiFacade.class);
			ClienteModel model = facade.leggiCliente(csc, chiave);
			model.getDatiApplicativi().setTipoStampa(Costanti.STAMPA_CENSIMENTO);
			
			if(model.getIsDitta().booleanValue())
				setNextCommandClass(Ditta.class);
			else
				setNextCommandClass(PersonaFisica.class);
			return model;
			
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nella stampa anagrafica codPotenziale=["+chiave.getCodPotenziale()+"] codFiscale=["+chiave.getCodFiscale()+"] partitaIva=["+chiave.getPartitaIva()+"]: "+e;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}
	}

	/*******************************************************************/
	/*******************************************************************/
	public Class getInputViewClass() {
		return ClienteKeyModel.class;
	}

}
