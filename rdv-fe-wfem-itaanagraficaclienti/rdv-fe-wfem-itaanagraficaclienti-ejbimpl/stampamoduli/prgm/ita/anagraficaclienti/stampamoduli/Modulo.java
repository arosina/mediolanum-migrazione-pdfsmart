package prgm.ita.anagraficaclienti.stampamoduli;

import prgm.ita.anagraficaclienti.facade.AnagraficaClientiFacade;
import prgm.ita.anagraficaclienti.facade.VerificheAnagrafica;
import prgm.ita.anagraficaclienti.model.ClienteKeyModel;
import prgm.ita.anagraficaclienti.model.ClienteModel;
import prgm.ita.anagraficaclienti.popup.facade.PopupFacade;
import prgm.ita.anagraficaclienti.popup.model.PopupClientiModel;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.PrintCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.StringType;

/***********************************************************************************************/
/***********************************************************************************************/
public class Modulo extends PrintCommand {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		
		try{
		
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			ModuloModel modulo = (ModuloModel)dataModel;
			
			AnagraficaClientiFacade facade = (AnagraficaClientiFacade)ROF.getFacade(csc,AnagraficaClientiFacade.class);		

			if (modulo.getNomeModulo().equals(Costanti.NOME_MODULO_MGM) || modulo.getNomeModulo().equals(Costanti.NOME_MODULO_VOM)){
				
				//Recupera le informazioni dell'agente 
				modulo.setAgenteCollegato(facade.leggiAgente(csc, new StringType(csc.getUserCode())));
				//modulo.setCodAgente(new StringType());
				modulo.setCodAgente(new StringType(csc.getUserCode()));
				//Recupera le informazioni del supervisore
				caricaSpvAgente(csc,modulo);
				modulo.setSupervisoreAgenteCollegato(facade.leggiAgente(csc, new StringType(modulo.getCodAgenteSpv().toString())));
				
				//Recupera le informazioni dell'utente
				PopupFacade popupFacade = (PopupFacade)ROF.getFacade(csc,PopupFacade.class);		
				PopupClientiModel popupClienti = new PopupClientiModel();
				if (modulo.getCodMediolanum().isNull()){
					popupClienti.getParams().setCodMediolanum(modulo.getCodPotenziale());
					popupClienti.getParams().setCodMediolanumIsCodPotenziale(new BooleanType(true));
					popupClienti = popupFacade.getElencoClientiPotenziali(csc,popupClienti);
				} else {
					popupClienti.getParams().setCodMediolanum(modulo.getCodMediolanum());
					popupClienti = popupFacade.getElencoClienti(csc,popupClienti);
				}
				if (popupClienti.getElenco().size() > 0)
					modulo.setCliente((ClienteModel)popupClienti.getElenco().get(0));
				//Controllo che il cliente non sia un FB...altrimenti visualizzo il pdf con il messaggio di errore...				
				if(VerificheAnagrafica.verificaSeClienteIsAgente(csc, modulo.getCodMediolanum()))
					modulo.setNomeModulo(new StringType("ClienteIsAgente"));
				
			} else {
			
				ClienteModel cliente = facade.leggiCliente(csc,modulo);
				modulo.setCliente(cliente);
		
			}

			return modulo;
			
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nel Modulo: "+e;
			CommandException ce = new CommandException(errorMsg);
			LOG.error(ce);
			throw ce;
		}
	}

	/********************************************************************************************************/
	/********************************************************************************************************/
	private void caricaSpvAgente(ClientSessionContext csc, ModuloModel model) throws Exception{

		DAOObject dao = null;
		try{
			
			boolean result = true;
			
			String country = csc.getCountryCode();
			String xmlName = country.substring(0,1).toUpperCase()+country.substring(1).toLowerCase();
			xmlName += "AnagraficaClienti.AnagraficaClienti";

			dao = new DAOObject(csc,xmlName);
			dao.executeCallableAccess("loadSupervisoreAgente",model);
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" - Eccezione DAO nel caricare il supervisore dell'agente "+daoe;
			Exception e = new Exception(errorMsg);
			throw e;
		}catch(Exception e){
			String errorMsg = getClass()+" - Eccezione nel nel caricare il supervisore dell'agente: "+e;
			e = new Exception(errorMsg);
			throw e;
		}finally{
			if(dao != null) dao.closeConnection();
		}
	}	
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return ModuloModel.class;
	}
	
}
