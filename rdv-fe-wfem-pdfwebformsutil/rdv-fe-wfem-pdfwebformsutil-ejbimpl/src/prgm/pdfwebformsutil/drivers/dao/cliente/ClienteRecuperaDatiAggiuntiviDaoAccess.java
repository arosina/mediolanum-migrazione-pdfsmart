package prgm.pdfwebformsutil.drivers.dao.cliente;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;

import prgm.pdfwebforms.model.PdfPersonModel;
import prgm.pdfwebformsutil.drivers.dao.AbstractDaoAccess;


public class ClienteRecuperaDatiAggiuntiviDaoAccess  extends AbstractDaoAccess {
	public static final String DAO_FILE_NAME = "PdfWebFormsUtil.Cliente";

	public ClienteRecuperaDatiAggiuntiviDaoAccess(ClientSessionContext csc) {
		super(csc, DAO_FILE_NAME);
	}

	public ClienteRecuperaDatiAggiuntiviDaoAccess(ClientSessionContext csc, DAOObject daoObj) {
		super(csc, daoObj);
	}
	
	// bozza dati aggiuntivi
	public void recuperaDatiAggiuntiviCliente(PdfPersonModel personModel) throws DAOException {
		getDaoObj().executeQueryAccess("recuperaDatiAggiuntiviCliente", personModel);
	}
	
}
