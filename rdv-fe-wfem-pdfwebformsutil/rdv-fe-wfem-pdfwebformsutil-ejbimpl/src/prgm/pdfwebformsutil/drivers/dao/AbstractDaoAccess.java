package prgm.pdfwebformsutil.drivers.dao;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;

/**************************************************************************************************
 * @author: Santoro Luca
**************************************************************************************************/

public abstract class AbstractDaoAccess {
	private DAOObject daoObj = null;
	private ClientSessionContext csc = null; 
	
	public AbstractDaoAccess(ClientSessionContext csc, String daoFileName)	{
		this(csc, new DAOObject(csc, daoFileName));
	}
	
	public AbstractDaoAccess(ClientSessionContext csc, DAOObject daoObj)	{
		this.csc = csc;
		this.daoObj = daoObj;		
	}

	public DAOObject getDaoObj() {
		return daoObj;
	}

	public void setDaoObj(DAOObject daoObj) {
		this.daoObj = daoObj;
	}

	public ClientSessionContext getCsc() {
		return csc;
	}

	public void setCsc(ClientSessionContext csc) {
		this.csc = csc;
	}
}
