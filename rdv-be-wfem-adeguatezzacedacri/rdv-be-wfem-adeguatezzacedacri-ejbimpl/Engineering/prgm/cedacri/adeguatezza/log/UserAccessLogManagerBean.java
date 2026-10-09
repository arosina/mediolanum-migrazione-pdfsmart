package prgm.cedacri.adeguatezza.log;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.atosorigin.wfem.backend.ManagerObject;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.util.Tools;

@Stateless(name = "UserAccessLogManager", mappedName = "UserAccessLogManager")
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class UserAccessLogManagerBean extends ManagerObject implements UserAccessLogManager{

  public void traceAccess( ClientSessionContext csc, UserAccessLog userAccessLog ) throws EJBException {
		
	  try {
		  if ( userAccessLog.getAccessTime().isNull() )
			  userAccessLog.setAccessTime(Tools.now());

		  DAOObject dao = new DAOObject(csc,"Cedacri/useraccesslogs/UserAccessLog");
		  dao.executeTableInsertAccess("userAccessLogRecord",userAccessLog);
	      
	  }catch (DAOException daoe) {
		  daoe.printStackTrace();
	  }catch (Exception e){
		  e.printStackTrace();
	  }
  }

}