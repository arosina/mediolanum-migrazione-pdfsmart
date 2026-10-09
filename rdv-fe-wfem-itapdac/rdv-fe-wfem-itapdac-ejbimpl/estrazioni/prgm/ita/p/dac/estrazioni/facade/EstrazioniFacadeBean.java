package prgm.ita.p.dac.estrazioni.facade;

import java.rmi.RemoteException;

import javax.ejb.EJBException;

import prgm.ita.p.dac.estrazioni.model.EstrazioniModel;
import prgm.ita.p.dac.estrazioni.model.PritInviatiInSedeModel;

import com.atosorigin.wfem.backend.FacadeObject;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.util.Tools;

public class EstrazioniFacadeBean extends FacadeObject implements EstrazioniFacade{
	private static final String DAO_XML_NAME = "ItaPDac.Estrazioni";
	
	public EstrazioniModel fillCodDesc(ClientSessionContext clientSessionContext, CommandDataModel model)throws RemoteException {
		 DAOObject dao = new DAOObject(clientSessionContext, DAO_XML_NAME);
		 EstrazioniModel mioModel=(EstrazioniModel)model;
		 dao.fillCodDesc(mioModel);
		 return mioModel;
	}
	
	public PritInviatiInSedeModel popolaCombo(ClientSessionContext clientSessionContext, PritInviatiInSedeModel model)throws RemoteException {
		 DAOObject dao = new DAOObject(clientSessionContext, DAO_XML_NAME);
		 PritInviatiInSedeModel mioModel=(PritInviatiInSedeModel)model;
		 dao.fillCodDesc(mioModel);
		 return mioModel;
	}
	
	public PritInviatiInSedeModel eseguiReportSintesi(ClientSessionContext csc, PritInviatiInSedeModel model)throws RemoteException {
		try{			
            model.setIsEsegui(new BooleanType(true));
           
			DAOObject dao = new DAOObject(csc,DAO_XML_NAME);
			DAOQueryResultModel qRes = dao.executeQueryAccess("outputReportSintesi",model);
			
    		if(qRes.getResult().size() == 0) {
    	      	model.setIsEsegui(new BooleanType(false));
    	      	return model;
    		}
			model.setOutputReportSintesi(qRes.getResult());
			return model;
			
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO nella query: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nella query: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}		
	}
	
	public PritInviatiInSedeModel eseguiReportDettaglio(ClientSessionContext csc, PritInviatiInSedeModel model)throws RemoteException {
		try{
			
            model.setIsEsegui(new BooleanType(true));
			
			DAOObject dao = new DAOObject(csc,DAO_XML_NAME);
			DAOQueryResultModel qRes = dao.executeQueryAccess("outputReportDettaglio",model);
			model.setOutputReportDettaglio(qRes.getResult());
			return model;
			
			
		}catch(DAOException daoe){
			String errorMsg = getClass()+" Eccezione DAO nella query: "+daoe;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}catch(Exception e){
			String errorMsg = getClass()+" Eccezione nella query: "+e;
			EJBException ejbe = new EJBException(errorMsg);
			LOG.error(ejbe);
			throw ejbe;
		}		
	}
}
