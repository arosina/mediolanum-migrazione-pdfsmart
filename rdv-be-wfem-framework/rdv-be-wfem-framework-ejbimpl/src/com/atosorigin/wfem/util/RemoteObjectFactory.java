package com.atosorigin.wfem.util;

import com.atosorigin.wfem.loggers.RofLogger;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class RemoteObjectFactory extends AbstractRemoteObjectFactory{

	protected String thisClassName = RemoteObjectFactory.class.getName();
	private static RemoteObjectFactory singleton = null;
	
	/***********************************************************************************************************/
	/***********************************************************************************************************/
	private RemoteObjectFactory() throws Exception 
	{
		super();
		
		LOG = RofLogger.getInstance();
		
		initProvider();
	}
	
	/***********************************************************************************************************/
	/***********************************************************************************************************/
	public static RemoteObjectFactory getInstance() {
	    if (singleton == null) {
	        synchronized (RemoteObjectFactory.class) {
	            if (singleton == null) {
		            try{
	                	singleton = new RemoteObjectFactory();
					} catch(Exception e) {
						e = new Exception("Exception creating RemoteObjectFactory instance: "+e);
						e.printStackTrace();
						return null;
					}
	            }
	        }
	    }
	    return singleton;
	}
	
}
