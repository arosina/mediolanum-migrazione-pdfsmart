package prgm.cedacri.adeguatezza.log;

import javax.ejb.Remote;

import com.atosorigin.wfem.backend.Manager;
import com.atosorigin.wfem.command.ClientSessionContext;

@Remote
public interface UserAccessLogManager extends Manager {
	public void traceAccess( ClientSessionContext csCtx, UserAccessLog userAccessLog );
}
