package prgm.pdfwebformsutil.drivers.dao;

import java.util.ArrayList;
import java.util.List;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ListType;

public abstract class AbstractKeySearchDaoAccess<K, T extends CommandDataModel> extends AbstractDaoAccess {

	public AbstractKeySearchDaoAccess(ClientSessionContext csc, String daoFileName) {
		super(csc, daoFileName);
	}
	
	public AbstractKeySearchDaoAccess(ClientSessionContext csc, DAOObject daoObj)	{
		super(csc, daoObj);
	}
	
	@SuppressWarnings("unchecked")
	public final List<T> findListItems(K key) throws DAOException {
		List<T> result = new ArrayList<T>();
		ListType lt = findList(key);
		if (lt != null) {
			for (int i = 0; i < lt.size(); i++) {
				result.add((T)lt.get(i));
			}
		}
		return result;
	}
	
	public ListType findList(K key) throws DAOException {
		return null;
	}
	
	public T findFirst(K key) throws DAOException {
		return null;
	}	
}
