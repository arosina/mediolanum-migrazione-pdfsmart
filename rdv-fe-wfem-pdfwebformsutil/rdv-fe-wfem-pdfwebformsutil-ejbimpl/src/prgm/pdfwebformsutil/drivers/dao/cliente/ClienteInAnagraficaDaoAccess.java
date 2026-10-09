package prgm.pdfwebformsutil.drivers.dao.cliente;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;

import prgm.ita.anagraficaclienti.model.ClienteModel;
import prgm.pdfwebforms.model.PdfPersonModel;
import prgm.pdfwebformsutil.drivers.dao.AbstractDaoAccess;


public class ClienteInAnagraficaDaoAccess  extends AbstractDaoAccess {
	public static final String DAO_FILE_NAME = "PdfWebFormsUtil.Cliente";

	public ClienteInAnagraficaDaoAccess(ClientSessionContext csc) {
		super(csc, DAO_FILE_NAME);
	}

	public ClienteInAnagraficaDaoAccess(ClientSessionContext csc, DAOObject daoObj) {
		super(csc, daoObj);
	}
	
	// bozza base
	public ClienteModel getClienteBozza (ClienteElencoAnagraficheInputModel params) throws DAOException {
		DAOQueryResultModel qRes = getDaoObj().executeQueryAccess("cercaClientiInAnagrafica", params);
		
		if(qRes.getResult().size() > 0){
			return (ClienteModel)qRes.getResult().get(0);
		}
		return null;
	}	

	// bozza dati aggiuntivi
	public void getClienteBozzaDatiAggiuntivi(PdfPersonModel personModel) throws DAOException {
		getDaoObj().executeQueryAccess("getClientiInBozzaDatiAggiuntivi", personModel);
	}	

	// --- Riconciliato --- 
	
	// anagrafica effettiva by codiceCliente / codiceFiscale
	public MapCommandDataModel getClienteRiconciliato (MapCommandDataModel map) throws DAOException {
		DAOQueryResultModel qRes = getDaoObj().executeQueryAccess("cercaClienteRiconciliato", map);
		
		if(qRes.getResult().size() > 0){
			return (MapCommandDataModel)qRes.getResult().get(0);
		}
		return null;
	}

	
}
