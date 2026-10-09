package prgm.ita.anagraficaclienti.popup.facade;

import java.util.List;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.atosorigin.wfem.backend.FacadeObject;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

import prgm.ita.anagraficaclienti.facade.AnagraficaClientiFacadeBean;
import prgm.ita.anagraficaclienti.facade.StatiPropostaAnagrafica;
import prgm.ita.anagraficaclienti.model.ClienteModel;
import prgm.ita.anagraficaclienti.model.IndirizziModel;
import prgm.ita.anagraficaclienti.popup.model.PopupAgentiModel;
import prgm.ita.anagraficaclienti.popup.model.PopupClientiModel;
import prgm.ita.anagraficaclienti.popup.model.PopupComuniModel;
import prgm.ita.anagraficaclienti.popup.model.PopupIndirizziClienteModel;
import prgm.ita.anagraficaclienti.popup.model.PopupUniversitaModel;

/***********************************************************************************************/
/***********************************************************************************************/
@Stateless(name = "PopupFacade", mappedName = "PopupFacade")
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class PopupFacadeBean extends FacadeObject implements PopupFacade{
	
	private static final String INSERISCI_FILTRO_RICERCA = "Inserire un filtro di ricerca: ";
	private static final String ECCEZIONE_LETTURA_CLIENTI = "Eccezione nel leggere l'elenco dei clienti: ";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PopupAgentiModel  getElencoAgenti(ClientSessionContext csc, PopupAgentiModel popupAgentiModel) throws EJBException{

		DAOObject dao = null;
		try{
			
			if(popupAgentiModel.getIsPrimaVolta().booleanValue()){
				if(popupAgentiModel.getParams().getCodAgente().isNull() &&
				   popupAgentiModel.getParams().getCognomeAgente().isNull())
				return popupAgentiModel;
			}
			 
			popupAgentiModel.setIsPrimaVolta(new BooleanType());
			
			dao = new DAOObject(csc,getNomeDAORicerche(csc));
			dao.openConnection();
			
			if(!popupAgentiModel.getParams().getCodAgente().isNull()){
				String filledCodAgente = popupAgentiModel.getParams().getCodAgente().toString();
				filledCodAgente = Tools.fillSx(filledCodAgente,'0',10);
				popupAgentiModel.getParams().setCodAgente(new StringType(filledCodAgente));
			}
			
			DAOQueryResultModel queryResult = dao.executeQueryAccess("elencoAgenti"+popupAgentiModel.getParams().getTipoRicerca(),popupAgentiModel);
			popupAgentiModel.setElenco(queryResult.getResult());
						
			if(!popupAgentiModel.getParams().getCodAgente().isNull()){
				String unfilledCodAgente = popupAgentiModel.getParams().getCodAgente().toString();
				unfilledCodAgente = Tools.unFillSx(unfilledCodAgente,'0');
				popupAgentiModel.getParams().setCodAgente(new StringType(unfilledCodAgente));
			}
			
			dao.fillCodDesc(popupAgentiModel);
			return popupAgentiModel;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+ "Eccezione DAO nel leggere l'elenco degli agenti: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nel leggere l'elenco degli agenti: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}finally{
			if(dao != null) dao.closeConnection();
		}		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PopupClientiModel getElencoClienti(ClientSessionContext csc, PopupClientiModel popupClientiModel) throws EJBException {
		
		DAOObject dao = null;
		try{
			
			if(popupClientiModel.getIsPrimaVolta().booleanValue()
					&& popupClientiModel.getParams().getCodAgente().isNull()
					&& popupClientiModel.getParams().getCognome().isNull()
					&& popupClientiModel.getParams().getCodMediolanum().isNull()) {
				return popupClientiModel;
			}
			 
			popupClientiModel.setIsPrimaVolta(new BooleanType());
			
			if(popupClientiModel.getIsCampiObbligatori().booleanValue()) {				
				popupClientiModel.resetCommandErrors();
				popupClientiModel.resetCommandMessages();
				popupClientiModel.resetCommandWarnings();
				Tools.resetTypesWarningAndErrors(popupClientiModel);
				if (popupClientiModel.getParams().getCognome().isNull() &&
					popupClientiModel.getParams().getNome().isNull() &&
					popupClientiModel.getParams().getCodMediolanum().isNull()){
						popupClientiModel.getParams().getCognome().addTypeError(INSERISCI_FILTRO_RICERCA);
						return popupClientiModel;
				}
			}

			dao = new DAOObject(csc,getNomeDAORicerche(csc));
			dao.openConnection();
			
			fillCodAgente(csc,popupClientiModel);
			fillCodCliente(csc,popupClientiModel);
			
			DAOQueryResultModel queryResult = dao.executeQueryAccess("elencoClienti",popupClientiModel);
			popupClientiModel.setElenco(queryResult.getResult());
						
			unfillCodAgente(csc,popupClientiModel);
			unfillCodCliente(csc,popupClientiModel);
			
			dao.fillCodDesc(popupClientiModel);
			return popupClientiModel;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+ ECCEZIONE_LETTURA_CLIENTI + daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+ ECCEZIONE_LETTURA_CLIENTI + e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}finally{
			if(dao != null) dao.closeConnection();
		}
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public PopupClientiModel getElencoClientiSecondari(ClientSessionContext csc, PopupClientiModel popupClientiModel) throws EJBException {
		
		DAOObject dao = null;
		try{
			
			if(popupClientiModel.getIsPrimaVolta().booleanValue()
					&& popupClientiModel.getParams().getCodAgente().isNull()
					&& popupClientiModel.getParams().getCognome().isNull()
					&& popupClientiModel.getParams().getCodMediolanum().isNull()) {
				return popupClientiModel;
			}
			 
			popupClientiModel.setIsPrimaVolta(new BooleanType());
			
			if(popupClientiModel.getIsCampiObbligatori().booleanValue()) {				
				popupClientiModel.resetCommandErrors();
				popupClientiModel.resetCommandMessages();
				popupClientiModel.resetCommandWarnings();
				Tools.resetTypesWarningAndErrors(popupClientiModel);
				if (popupClientiModel.getParams().getCognome().isNull() &&
					popupClientiModel.getParams().getNome().isNull() &&
					popupClientiModel.getParams().getCodMediolanum().isNull()){
						popupClientiModel.getParams().getCognome().addTypeError(INSERISCI_FILTRO_RICERCA);
						return popupClientiModel;
				}
			}

			dao = new DAOObject(csc,getNomeDAORicerche(csc));
			dao.openConnection();
			
			fillCodAgente(csc,popupClientiModel);
			fillCodCliente(csc,popupClientiModel);
			
			DAOQueryResultModel queryResult = dao.executeQueryAccess("elencoClientiPotenziali",popupClientiModel);
			DAOQueryResultModel queryResult2 = dao.executeQueryAccess("elencoClienti",popupClientiModel);
			
			List<ClienteModel> listaClienti = queryResult.getResult().getElements();
			listaClienti.addAll(queryResult2.getResult().getElements());
			popupClientiModel.setElenco(new ListType(listaClienti));
			
			unfillCodAgente(csc,popupClientiModel);
			unfillCodCliente(csc,popupClientiModel);
			
			dao.fillCodDesc(popupClientiModel);
			return popupClientiModel;
			
		}catch(DAOException daoe){
			String errorMsg = getClass() + ECCEZIONE_LETTURA_CLIENTI + daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass() + ECCEZIONE_LETTURA_CLIENTI + e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}finally{
			if(dao != null) dao.closeConnection();
		}
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public PopupClientiModel getElencoClientiPotenziali(ClientSessionContext csc, PopupClientiModel popupClientiModel) throws EJBException {
		
		DAOObject dao = null;
		try{
			
			if(popupClientiModel.getIsPrimaVolta().booleanValue()){
				if(popupClientiModel.getParams().getCodAgente().isNull() &&
				   popupClientiModel.getParams().getCognome().isNull() &&
				   popupClientiModel.getParams().getCodMediolanum().isNull())
				return popupClientiModel;
			}
			 
			popupClientiModel.setIsPrimaVolta(new BooleanType());
			
			if(popupClientiModel.getIsCampiObbligatori().booleanValue()) {				
				popupClientiModel.resetCommandErrors();
				popupClientiModel.resetCommandMessages();
				popupClientiModel.resetCommandWarnings();
				Tools.resetTypesWarningAndErrors(popupClientiModel);
				if (popupClientiModel.getParams().getCognome().isNull() &&
					popupClientiModel.getParams().getNome().isNull() &&
					popupClientiModel.getParams().getCodMediolanum().isNull()){
						popupClientiModel.getParams().getCognome().addTypeError(INSERISCI_FILTRO_RICERCA);
						return popupClientiModel;
				}
			}
			
			dao = new DAOObject(csc,getNomeDAORicerche(csc));
			dao.openConnection();
			
			fillCodAgente(csc,popupClientiModel);
			fillCodCliente(csc,popupClientiModel);
			
			DAOQueryResultModel queryResult = dao.executeQueryAccess("elencoClientiPotenziali",popupClientiModel);
			popupClientiModel.setElenco(queryResult.getResult());
						
			unfillCodAgente(csc,popupClientiModel);
			unfillCodCliente(csc,popupClientiModel);
			
			dao.fillCodDesc(popupClientiModel);
			return popupClientiModel;
			
		}catch(DAOException daoe){
			String errorMsg = getClass() + ECCEZIONE_LETTURA_CLIENTI + daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass() + ECCEZIONE_LETTURA_CLIENTI + e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}finally{
			if(dao != null) dao.closeConnection();
		}
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public PopupClientiModel getElencoMandatiM4U(ClientSessionContext csc, PopupClientiModel popupClientiModel) throws EJBException {
		
		DAOObject dao = null;
		try{
			
			if(popupClientiModel.getIsPrimaVolta().booleanValue())
				return popupClientiModel;
			 
			if(popupClientiModel.getParams().getCodAgente().isNull())
				popupClientiModel.getParams().setCodAgente(new StringType(csc.getCurrentLinkedUserCode()));
			
			popupClientiModel.setIsPrimaVolta(new BooleanType());
			
			dao = new DAOObject(csc,getNomeDAORicerche(csc));
			dao.openConnection();

			StringType savCodAge = popupClientiModel.getParams().getCodAgente();
			if(!popupClientiModel.getParams().getCodAgente().isNull()){
				String filledCodAgente = popupClientiModel.getParams().getCodAgente().toString();
				filledCodAgente = Tools.fillSx(filledCodAgente,'0',10);
				popupClientiModel.getParams().setCodAgente(new StringType(filledCodAgente));
			}
			
			DAOQueryResultModel queryResult = dao.executeQueryAccess("elencoMandatiM4U",popupClientiModel);
			popupClientiModel.setElenco(queryResult.getResult());
			popupClientiModel.getParams().setCodAgente(savCodAge);
			dao.fillCodDesc(popupClientiModel);
			return popupClientiModel;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+ "Eccezione DAO nel leggere l'elenco dei mandati M4U: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nel leggere l'elenco dei mandati M4U: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}finally{
			if(dao != null) dao.closeConnection();
		}
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public PopupClientiModel getElencoClientiAgente(ClientSessionContext csc, PopupClientiModel popupClientiModel) throws EJBException {
		
		DAOObject dao = null;
		try{

			// ************************************************************************************************ //
			// Non servirebbe più ma la store lo prevede come parametro anche se poi non lo usa piu
			if(popupClientiModel.getParams().getCodRete().isNull())
				popupClientiModel.getParams().setCodRete(new StringType(csc.getChannelCode().toUpperCase()));
			// ************************************************************************************************ //
			
			if(popupClientiModel.getParams().getCodAgente().isNull())
				popupClientiModel.getParams().setCodAgente(new StringType(csc.getCurrentLinkedUserCode()));
			
			fillCodAgente(csc,popupClientiModel);
			fillCodCliente(csc,popupClientiModel);

			popupClientiModel.getParams().getAgente().setCodAgente(popupClientiModel.getParams().getCodAgente());			
			popupClientiModel.getParams().setMaxRows(new IntegerType(popupClientiModel.getParams().getMaxRows().intValue()+1));
			
			if(popupClientiModel.getIsPrimaVolta().booleanValue()){
				if(popupClientiModel.getParams().getTipoInclusioneAgenti().equals("spv")){
					dao = new DAOObject(csc,getNomeDAOAnagrafica(csc));
					dao.executeCallableAccess("loadSupervisoreAgente",popupClientiModel.getParams());
				}
				
				unfillCodAgente(csc,popupClientiModel);
				unfillCodCliente(csc,popupClientiModel);
				
				popupClientiModel.getParams().setCodAgenteInp(new StringType(popupClientiModel.getParams().getCodAgente().toString()));
				
				if(popupClientiModel.getParams().getCognome().isNull() &&
				   popupClientiModel.getParams().getCodMediolanum().isNull()) {
					
					dao = new DAOObject(csc,getNomeDAORicerche(csc));
					dao.fillCodDesc(popupClientiModel.getParams());
					popupClientiModel.getParams().setAgenteDiretto(popupClientiModel.getParams().getCodAgenteRoot());
					return popupClientiModel;
				}
			}
							
			if (!popupClientiModel.getParams().getCodAgenteRoot().isNull() && 
					(popupClientiModel.getParams().getAgenteDiretto().isNull() || popupClientiModel.getParams().getAgenteDiretto().equals(""))) {
				
				popupClientiModel.getParams().getAgenteDiretto().addTypeError("campoObbligatorio");
				popupClientiModel.setIsPrimaVolta(new BooleanType(true));
				return popupClientiModel;
			}

			popupClientiModel.setIsPrimaVolta(new BooleanType());
			
			fillCodAgente(csc,popupClientiModel);
			fillCodCliente(csc,popupClientiModel);
			
			dao = new DAOObject(csc,getNomeDAORicerche(csc));
			dao.openConnection();
			
			DAOQueryResultModel queryResult = dao.executeQueryAccess("elencoClientiAgente",popupClientiModel);
			ListType elenco = queryResult.getResult();
			if(popupClientiModel.getParams().getMaxRows().intValue() > 0 &&
			   elenco.size() > (popupClientiModel.getParams().getMaxRows().intValue()-1)){
				int numEl = elenco.size();
				elenco.removeRecalc(numEl-1);
				elenco.setMaxRowsExceeded(true);
			}
			popupClientiModel.setElenco(queryResult.getResult());
			popupClientiModel.getParams().setMaxRows(new IntegerType(popupClientiModel.getParams().getMaxRows().intValue()-1));
						
			unfillCodAgente(csc,popupClientiModel);
			unfillCodCliente(csc,popupClientiModel);
			
			dao.fillCodDesc(popupClientiModel);
			impostaCancellabile(csc,popupClientiModel.getElenco());
			return popupClientiModel;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+ "Eccezione DAO nel leggere l'elenco dei clienti di un agente: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nel leggere l'elenco dei clienti di un agente: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}finally{
			if(dao != null) dao.closeConnection();
		}
		
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public PopupComuniModel getElencoComuni(ClientSessionContext csc, PopupComuniModel popupComuniModel) throws EJBException {

	    DAOObject dao = null;
	    try {
	
	      dao = new DAOObject(csc, getNomeDAORicerche(csc));
	      dao.openConnection();
	      
	      if(popupComuniModel.getIsPrimaVolta().booleanValue()){
			if(popupComuniModel.getCap().isNull() &&
			   popupComuniModel.getComune().isNull() &&
			   popupComuniModel.getProvincia().isNull())
			return popupComuniModel;
	      }
					
	      popupComuniModel.setIsPrimaVolta(new BooleanType());
	      
	      DAOQueryResultModel queryResult = null;
	      if(popupComuniModel.getIsRicercaIscrittiAlCatasto().booleanValue())
		      queryResult = dao.executeQueryAccess("elencoComuniIscrittiAlCatasto", popupComuniModel);
	      else
	    	  queryResult = dao.executeQueryAccess("elencoComuni", popupComuniModel);
	      popupComuniModel.setElenco(queryResult.getResult());
	      return popupComuniModel;
	
	    }catch (DAOException daoe) {
	      String errorMsg = getClass() + "Eccezione DAO nel leggere l'elenco dei Comuni: " + daoe;
	      EJBException ejbe = new EJBException(errorMsg);
	      LOG.error(ejbe);
	      throw ejbe;
	    }catch (Exception e) {
	      String errorMsg = getClass() + " Eccezione nel leggere l'elenco dei Comuni: " + e;
	      EJBException ejbe = new EJBException(errorMsg);
	      LOG.error(ejbe);
	      throw ejbe;
	    }finally {
	      if (dao != null)
	        dao.closeConnection();
	    }
	
  	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public PopupUniversitaModel getElencoUniversita(ClientSessionContext csc, PopupUniversitaModel popupUniversitaModel) throws EJBException {

	    DAOObject dao = null;
	    try {
	
		    dao = new DAOObject(csc, getNomeDAORicerche(csc));
		    dao.openConnection();
		      
		    if(popupUniversitaModel.getIsPrimaVolta().booleanValue()){
		    	dao.fillCodDesc(popupUniversitaModel);
		    	return popupUniversitaModel;
		    }
						
		    popupUniversitaModel.setIsPrimaVolta(new BooleanType());
		      
		    DAOQueryResultModel queryResult = dao.executeQueryAccess("elencoUniversita", popupUniversitaModel);
		    popupUniversitaModel.setElenco(queryResult.getResult());
		    return popupUniversitaModel;
	
	    }catch (DAOException daoe) {
		    String errorMsg = getClass() + "Eccezione DAO nel leggere l'elenco delle universita: " + daoe;
		    EJBException ejbe = new EJBException(errorMsg);
		    LOG.error(ejbe);
		    throw ejbe;
	    }catch (Exception e) {
			String errorMsg = getClass() + " Eccezione nel leggere l'elenco delle universita: " + e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
	    }finally {
	    	if(dao != null)
	    		dao.closeConnection();
	    }
	
  	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public PopupIndirizziClienteModel getElencoIndirizziCliente(ClientSessionContext csc, PopupIndirizziClienteModel popupIndirizziClienteModel) throws EJBException {

	    DAOObject dao = null;
	    try {
	
	    	popupIndirizziClienteModel.getIndirizzi().setElencoAttributi(new ListType(IndirizziModel.class));
	    	ClienteModel cliente = AnagraficaClientiFacadeBean.trovaCliente(csc,popupIndirizziClienteModel.getCliente());
	    	if(cliente != null){
	  	      	dao = new DAOObject(csc, getNomeDAOAnagrafica(csc));
	  	      	dao.openConnection();
	  	      	cliente.getDatiApplicativi().setTipoElemento(new StringType("ALL"));
	  	      	DAOQueryResultModel qRes = dao.executeQueryAccess("loadIndirizzi",cliente);
	  	      	popupIndirizziClienteModel.getIndirizzi().setElencoAttributi(qRes.getResult());
	  	      	dao.fillCodDesc(popupIndirizziClienteModel.getIndirizzi());
	    	}
		    return popupIndirizziClienteModel;
	
	    }catch (DAOException daoe) {
		    String errorMsg = getClass() + "Eccezione DAO nel leggere l'elenco degli indirizzi cliente: " + daoe;
		    EJBException ejbe = new EJBException(errorMsg);
		    LOG.error(ejbe);
		    throw ejbe;
	    }catch (Exception e) {
			String errorMsg = getClass() + " Eccezione nel leggere l'elenco degli indirizzi cliente: " + e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
	    }finally {
	    	if(dao != null)
	    		dao.closeConnection();
	    }
	
  	}


	/***********************************************************************************************/
	/***********************************************************************************************/
	private void impostaCancellabile(ClientSessionContext csc, ListType clienti){
		
	    DAOObject dao = null;
	    try {
	
	      dao = new DAOObject(csc, getNomeDAOVerifiche(csc));
	      dao.openConnection();
	      
			for(int i=0;i<clienti.size();i++){
				ClienteModel cliente = (ClienteModel)clienti.get(i);
				if(cliente.getIsPotenziale().booleanValue()){
					if(cliente.getStatoProposta().equals(StatiPropostaAnagrafica.BOZZA)){
						try{
							DAOQueryResultModel qRes = dao.executeQueryAccess("verificaSeAnagraficaUtilizzataInMHD",cliente);
							IntegerType count = (IntegerType)qRes.getSingleResult();
							if(count.intValue() == 0)
								cliente.setIsCancellabile(new BooleanType(true));				
						}catch(DAOException daoe){
							cliente.setIsCancellabile(new BooleanType(true));						
						}
					}
				}
			}
			
	    }catch (DAOException daoe) {
	    	LOG.warning(getClass() + "Eccezione DAO nel verificare se l'anagrafica è utilizzata in MHD: "+daoe);
	    }catch (Exception e) {
	    	LOG.warning(getClass() + "Eccezione nel verificare se l'anagrafica è utilizzata in MHD: "+e);
	    }finally {
	      if (dao != null)
	        dao.closeConnection();
	    }
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void fillCodAgente(ClientSessionContext csc, PopupClientiModel model){
		if(model.getParams().getCodAgente().isNull())
			return;
		String filledCodAgente = model.getParams().getCodAgente().toString();
		filledCodAgente = Tools.fillSx(filledCodAgente,'0',10);
		model.getParams().setCodAgente(new StringType(filledCodAgente));		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void unfillCodAgente(ClientSessionContext csc, PopupClientiModel model){
		if(model.getParams().getCodAgente().isNull())
			return;
		String unfilledCodAgente = model.getParams().getCodAgente().toString();
		unfilledCodAgente = Tools.unFillSx(unfilledCodAgente,'0');
		model.getParams().setCodAgente(new StringType(unfilledCodAgente));
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void fillCodCliente(ClientSessionContext csc, PopupClientiModel model){
		if(model.getParams().getCodMediolanum().isNull())
			return;
		String filledCodMediolanum = model.getParams().getCodMediolanum().toString();
		if(model.getParams().getCodMediolanumIsCodPotenziale().booleanValue())
			filledCodMediolanum = Tools.fillSx(filledCodMediolanum,'0',16);
		else
			filledCodMediolanum = Tools.fillSx(filledCodMediolanum,'0',11);
		model.getParams().setCodMediolanum(new StringType(filledCodMediolanum));		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void unfillCodCliente(ClientSessionContext csc, PopupClientiModel model){
		if(model.getParams().getCodMediolanum().isNull())
			return;
		String unfilledCodMediolanum = model.getParams().getCodMediolanum().toString();
		unfilledCodMediolanum = Tools.unFillSx(unfilledCodMediolanum,'0');
		model.getParams().setCodMediolanum(new StringType(unfilledCodMediolanum));
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String getNomeDAOAnagrafica(ClientSessionContext csc){
		String country = csc.getCountryCode();
		String xmlName = country.substring(0,1).toUpperCase()+country.substring(1).toLowerCase();
		xmlName += "AnagraficaClienti.AnagraficaClienti";
	  	return xmlName;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String getNomeDAORicerche(ClientSessionContext csc){
		String country = csc.getCountryCode();
		String xmlName = country.substring(0,1).toUpperCase()+country.substring(1).toLowerCase();
		xmlName += "AnagraficaClienti.Ricerche";
	  	return xmlName;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String getNomeDAOVerifiche(ClientSessionContext csc){
		String country = csc.getCountryCode();
		String xmlName = country.substring(0,1).toUpperCase()+country.substring(1).toLowerCase();
		xmlName += "AnagraficaClienti.Verifiche";
	  	return xmlName;
	}
	
	/** ******************************************************************************************** */
	/** ******************************************************************************************** */
	public PopupClientiModel getElencoClientiAgentePrestitiPreapprovati(ClientSessionContext csc, PopupClientiModel popupClientiModel)
			throws EJBException {

		DAOObject dao = null;
		try{

			// ************************************************************************************************ //
			// Non servirebbe più ma la store lo prevede come parametro anche se poi non lo usa piu
			if(popupClientiModel.getParams().getCodRete().isNull())
				popupClientiModel.getParams().setCodRete(new StringType(csc.getChannelCode().toUpperCase()));
			// ************************************************************************************************ //
			
			if(popupClientiModel.getParams().getCodAgente().isNull())
				popupClientiModel.getParams().setCodAgente(new StringType(csc.getCurrentLinkedUserCode()));
			
			fillCodAgente(csc,popupClientiModel);
			fillCodCliente(csc,popupClientiModel);

			popupClientiModel.getParams().getAgente().setCodAgente(popupClientiModel.getParams().getCodAgente());			
			popupClientiModel.getParams().setMaxRows(new IntegerType(popupClientiModel.getParams().getMaxRows().intValue()+1));
			
			if(popupClientiModel.getIsPrimaVolta().booleanValue()){
				if(popupClientiModel.getParams().getTipoInclusioneAgenti().equals("spv")){
					dao = new DAOObject(csc,getNomeDAOAnagrafica(csc));
					dao.executeCallableAccess("loadSupervisoreAgente",popupClientiModel.getParams());
				}
				
				unfillCodAgente(csc,popupClientiModel);
				unfillCodCliente(csc,popupClientiModel);
				
				popupClientiModel.getParams().setCodAgenteInp(new StringType(popupClientiModel.getParams().getCodAgente().toString()));
				
				if(popupClientiModel.getParams().getCognome().isNull() &&
				   popupClientiModel.getParams().getCodMediolanum().isNull()) {
					
					dao = new DAOObject(csc,getNomeDAORicerche(csc));
					dao.fillCodDesc(popupClientiModel.getParams());
					popupClientiModel.getParams().setAgenteDiretto(popupClientiModel.getParams().getCodAgenteRoot());
					return popupClientiModel;
				}
			}
							
			if (!popupClientiModel.getParams().getCodAgenteRoot().isNull() && 
					(popupClientiModel.getParams().getAgenteDiretto().isNull() || popupClientiModel.getParams().getAgenteDiretto().equals(""))) {
				
				popupClientiModel.getParams().getAgenteDiretto().addTypeError("campoObbligatorio");
				popupClientiModel.setIsPrimaVolta(new BooleanType(true));
				return popupClientiModel;
			}

			popupClientiModel.setIsPrimaVolta(new BooleanType());
			
			fillCodAgente(csc,popupClientiModel);
			fillCodCliente(csc,popupClientiModel);
			
			dao = new DAOObject(csc,getNomeDAORicerche(csc));
			dao.openConnection();
			
			DAOQueryResultModel queryResult = dao.executeQueryAccess("elencoClientiAgentePreapprovati", popupClientiModel);
			ListType elenco = queryResult.getResult();
						
			if(popupClientiModel.getParams().getMaxRows().intValue() > 0 &&
			   elenco.size() > (popupClientiModel.getParams().getMaxRows().intValue()-1)){
				int numEl = elenco.size();
				elenco.removeRecalc(numEl-1);
				elenco.setMaxRowsExceeded(true);
			}
			popupClientiModel.setElenco(queryResult.getResult());
			popupClientiModel.getParams().setMaxRows(new IntegerType(popupClientiModel.getParams().getMaxRows().intValue()-1));
						
			unfillCodAgente(csc,popupClientiModel);
			unfillCodCliente(csc,popupClientiModel);
			
			dao.fillCodDesc(popupClientiModel);
			impostaCancellabile(csc,popupClientiModel.getElenco());
			return popupClientiModel;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+ "Eccezione DAO nel leggere l'elenco dei clienti di un agente: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nel leggere l'elenco dei clienti di un agente: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}finally{
			if(dao != null) dao.closeConnection();
		}
	}
}