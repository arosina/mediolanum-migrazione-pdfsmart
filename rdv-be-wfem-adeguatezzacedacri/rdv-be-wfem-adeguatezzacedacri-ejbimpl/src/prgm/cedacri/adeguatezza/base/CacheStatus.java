package prgm.cedacri.adeguatezza.base;

import java.rmi.RemoteException;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.types.*;

public interface CacheStatus extends java.io.Serializable 
{
	public BooleanType initialize(ClientSessionContext csc) throws RemoteException;
	public BooleanType refresh(ClientSessionContext csc) throws RemoteException;
	public StringType getStatus(ClientSessionContext csc) throws RemoteException;
}
