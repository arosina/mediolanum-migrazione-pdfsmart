package com.atosorigin.wfem.util;

import javax.naming.InitialContext;

import com.atosorigin.wfem.backend.Manager;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.loggers.BkRofLogger;

/**
 */
public class BkRemoteObjectFactory extends AbstractRemoteObjectFactory{

	protected String thisClassName = BkRemoteObjectFactory.class.getName();
	private static BkRemoteObjectFactory singleton = null;
	
	/***********************************************************************************************************/
	/***********************************************************************************************************/
	private BkRemoteObjectFactory() throws Exception 
	{
		super();
		
		LOG = BkRofLogger.getInstance();
		
		initProvider();
	}
	
	/***********************************************************************************************************/
	/***********************************************************************************************************/
	public static BkRemoteObjectFactory getInstance() {
	    if (singleton == null) {
	        synchronized (BkRemoteObjectFactory.class) {
	            if (singleton == null) {
		            try{
	                	singleton = new BkRemoteObjectFactory();
					} catch(Exception e) {
						e = new Exception("Exception creating BkRemoteObjectFactory instance: "+e);
						e.printStackTrace();
						return null;
					}
	            }
	        }
	    }
	    return singleton;
	}
	
	/***********************************************************************************************************/
	/***********************************************************************************************************/
	public Manager getManager(ClientSessionContext ctx, Class managerInterfaceClass) throws Exception {
	
		String thisMethod = ".getManager: ";
		String JNDIName = "";
		try {
			
			if(local)
				return (Manager)getLocalObject(managerInterfaceClass);
			
    		String className = managerInterfaceClass.getName();
    		String prefix = managerInterfaceClass.getSimpleName();
    		InitialContext initialContext = null;
    		if(env == null)
    			initialContext = new InitialContext();
    		else
    			initialContext = new InitialContext(env);
    		Manager ejbRemoteInterface = (Manager)initialContext.lookup(prefix+"#"+className);
			LOG.debug("Manager component created: ["+ejbRemoteInterface+"]");
			return ejbRemoteInterface;
			
		}catch(Exception e) {
			Exception ne = new Exception(thisClassName+ thisMethod+"Exception in loading Manager. JNDI Name ["+JNDIName+"]: "+e);
			LOG.error(ne);
			throw e;
		}
	}
	
}
