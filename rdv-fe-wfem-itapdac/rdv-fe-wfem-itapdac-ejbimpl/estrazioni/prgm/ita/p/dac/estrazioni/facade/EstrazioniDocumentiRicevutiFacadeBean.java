package prgm.ita.p.dac.estrazioni.facade;

import java.rmi.RemoteException;

import prgm.ita.p.dac.estrazioni.model.DocumentiRicevutiModel;
import prgm.ita.p.dac.estrazioni.model.EstrazioniModel;

import com.atosorigin.wfem.backend.FacadeObject;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;

public class EstrazioniDocumentiRicevutiFacadeBean extends FacadeObject implements EstrazioniDocumentiRicevutiFacade{
	
	private static final String DAO_XML_NAME = "ItaPDac.Estrazioni";
	public DocumentiRicevutiModel fillCodDesc(ClientSessionContext clientSessionContext, CommandDataModel model)	throws RemoteException {
		 DAOObject dao = new DAOObject(clientSessionContext, DAO_XML_NAME);
		 DocumentiRicevutiModel mioModel=(DocumentiRicevutiModel)model;
		 dao.fillCodDesc(mioModel);
		 return mioModel;
	}

}
