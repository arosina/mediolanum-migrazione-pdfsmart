package prgm.pdfwebformsutil.drivers.dao;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ListType;

public abstract class AbstractSearchDaoAccess<T extends CommandDataModel> extends AbstractDaoAccess {

	public AbstractSearchDaoAccess(ClientSessionContext csc, String daoFileName) {
		super(csc, daoFileName);
	}
	
	public AbstractSearchDaoAccess(ClientSessionContext csc, DAOObject daoObj)	{
		super(csc, daoObj);
	}
	
	public ListType findList(T dataModel) throws DAOException {
		return null;
	}
	
	public T findFirst(T dataModel) throws DAOException {
		return null;
	}	
}
