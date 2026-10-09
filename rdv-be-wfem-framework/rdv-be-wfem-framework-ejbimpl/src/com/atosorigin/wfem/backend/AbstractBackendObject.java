package com.atosorigin.wfem.backend;

import javax.annotation.Resource;

/***********************************************************************************************
 * @author Ricotti Corrado
 ***********************************************************************************************/
public abstract class AbstractBackendObject{
	
    protected com.atosorigin.wfem.util.BkRemoteObjectFactory ROF = com.atosorigin.wfem.util.BkRemoteObjectFactory.getInstance();
    protected com.atosorigin.wfem.util.Logger LOG = com.atosorigin.wfem.util.Logger.getInstance();	
        
	@Resource
	private javax.ejb.SessionContext sessionContext;

	public javax.ejb.SessionContext getSessionContext() {
		return sessionContext;
	}

	public void setSessionContext(javax.ejb.SessionContext sessionContext) {
		this.sessionContext = sessionContext;
	}
	
}