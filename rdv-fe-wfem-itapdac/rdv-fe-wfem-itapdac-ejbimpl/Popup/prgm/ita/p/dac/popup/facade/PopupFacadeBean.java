package prgm.ita.p.dac.popup.facade;

import javax.ejb.EJBException;

import prgm.ita.p.dac.popup.model.PopupAgentiModel;
import prgm.ita.p.dac.popup.model.PopupClientiModel;
import prgm.ita.p.dac.popup.model.PopupContrattiModel;
import prgm.ita.p.dac.popup.model.PopupContrattoModel;

import com.atosorigin.wfem.backend.FacadeObject;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.controller.Configuration;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

/***********************************************************************************************/
/***********************************************************************************************/
public class PopupFacadeBean extends FacadeObject implements PopupFacade{

	private static final String DAO_XML_NAME = "ItaPDac.Popup";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PopupContrattiModel cercaContratti(ClientSessionContext csc, PopupContrattiModel popupContrattiModel) throws EJBException{
		try{
			
			DAOObject dao = new DAOObject(csc,DAO_XML_NAME);
			popupContrattiModel.setElencoContratti(dao.executeQueryAccess("ricercaContratti",popupContrattiModel).getResult());
			arricchisciContratti(dao,popupContrattiModel.getElencoContratti());
			return popupContrattiModel;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO in cercaContratti: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in cercaContratti: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PopupClientiModel cercaClienti(ClientSessionContext csc, PopupClientiModel popupClientiModel) throws EJBException{
		try{
			DAOObject dao = new DAOObject(csc,DAO_XML_NAME);
			StringType savCodAge = popupClientiModel.getCodAgente();
			if(Configuration.getInstance().isOfflineEnvironment() ||					// Sull'offline comunque usiamo la stored su INR
			   popupClientiModel.getTipoRicerca().intValue() == PopupClientiModel.RICERCA_MIEI_CLIENTI){
				
				popupClientiModel.setCodAgente(new StringType(Tools.fillSx(csc.getCurrentLinkedUserCode(),'0',10)));
				ListType elenco = dao.executeQueryAccess("ricercaClientiAgente",popupClientiModel).getResult();
				if(elenco.size() > 50){
					int numEl = elenco.size();
					elenco.removeRecalc(numEl-1);
					elenco.setMaxRowsExceeded(true);
				}
				popupClientiModel.setElencoClienti(elenco);
				dao.fillCodDesc(popupClientiModel);
				
			}else{
				
				if(!savCodAge.isNull())
					popupClientiModel.setCodAgente(new StringType(Tools.fillSx(savCodAge.toString(),'0',10)));
				StringType savCodMediolanum = popupClientiModel.getCodMediolanum();
				if(!savCodMediolanum.isNull())
					popupClientiModel.setCodMediolanum(new StringType(Tools.fillSx(savCodMediolanum.toString(),'0',11)));
				popupClientiModel.setElencoClienti(dao.executeQueryAccess("ricercaClienti",popupClientiModel).getResult());
				popupClientiModel.setCodMediolanum(savCodMediolanum);
				
			}
			
			popupClientiModel.setElencoContrattiCliente(new ListType(PopupContrattoModel.class));
			popupClientiModel.setCodAgente(savCodAge);
			return popupClientiModel;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO in cercaClienti: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in cercaClienti: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PopupClientiModel cercaContrattiCliente(ClientSessionContext csc, PopupClientiModel popupClientiModel) throws EJBException{
		try{
			
			DAOObject dao = new DAOObject(csc,DAO_XML_NAME);
			dao.executeQueryAccess("loadDatiCliente",popupClientiModel.getClienteSelezionato());
			popupClientiModel.setElencoContrattiCliente(dao.executeQueryAccess("elencoContrattiCliente",popupClientiModel).getResult());
			arricchisciContratti(dao,popupClientiModel.getElencoContrattiCliente());
			return popupClientiModel;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO in cercaContrattiCliente: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in cercaContrattiCliente: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public PopupAgentiModel cercaAgenti(ClientSessionContext csc, PopupAgentiModel popupAgentiModel) throws EJBException{
		try{
			
			DAOObject dao = new DAOObject(csc,DAO_XML_NAME);
			popupAgentiModel.setElencoAgenti(dao.executeQueryAccess("ricercaAgenti",popupAgentiModel).getResult());
			return popupAgentiModel;
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO in cercaAgenti: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione in cercaAgenti: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private void arricchisciContratti(DAOObject dao, ListType contratti) throws DAOException{
		for(int i=0;i<contratti.size();i++){
			PopupContrattoModel contr = (PopupContrattoModel)contratti.get(i);
			dao.executeQueryAccess("loadArricchimentoContratto"+contr.getTipoProdotto(),contr);
		}
	}
	
}

