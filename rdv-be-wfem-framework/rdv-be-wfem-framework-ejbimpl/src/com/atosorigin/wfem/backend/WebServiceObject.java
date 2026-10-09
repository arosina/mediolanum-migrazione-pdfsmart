package com.atosorigin.wfem.backend;

import javax.annotation.Resource;
import javax.ejb.SessionContext;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.tierlog.ClientTierAccessLogger;
import com.atosorigin.wfem.tierlog.TierAccessLoggerInfo;
import com.atosorigin.wfem.webservices.ClientSessionContextBean;
import com.atosorigin.wfem.webservices.MethodInputBean;

/***********************************************************************************************
 * @author Ricotti Corrado
 ***********************************************************************************************/
public class WebServiceObject extends AbstractBackendObject{
	
    @Resource
    private SessionContext sessionContext;

	/***********************************************************************************************/
	/***********************************************************************************************/
    protected ClientSessionContext initClientSessionContext(MethodInputBean bean){
    	return initClientSessionContext(bean, false);
    }
    
	/***********************************************************************************************/
	/***********************************************************************************************/
    protected ClientSessionContext initClientSessionContext(MethodInputBean bean, boolean loadUserData){
    	ClientSessionContextBean cscBean = bean.getMethodHeader().getClientSessionContext();
    	ClientSessionContext csc = new ClientSessionContext();
    	if(!cscBean.getCountry().isEmpty()){ 	csc.setCountryCode(cscBean.getCountry().toUpperCase()); }else{ csc.setCountryCode("ITA"); }
    	if(!cscBean.getChannel().isEmpty()){ 	csc.setChannelCode(cscBean.getChannel().toUpperCase()); }else{ csc.setChannelCode("P"); }
    	if(!cscBean.getLanguage().isEmpty()){ 	csc.setLangCode(cscBean.getLanguage().toUpperCase()); }else{ csc.setLangCode("IT"); }
    	if(!cscBean.getUserType().isEmpty()){ 	csc.setUserType(cscBean.getUserType().toUpperCase()); }
    	csc.setUserCode(cscBean.getUser());
    	csc.setClientIp(cscBean.getClientip());
    	csc.setCurrentLinkedUserCode(cscBean.getCluser());
    	
    	if(loadUserData && csc.getUserCode() != null && csc.getUserCode().length() > 0){
    		String[] userData = UserContextDataLoader.loadLoginNameAndRolesForUser(csc.getUserCode());
    		csc.setLoginName(userData[0]);
    		csc.setUserType(userData[1]);
    		csc.setUserCode(userData[2]);
    	}
    	
        if(getSessionContext() != null && (csc.getUserCode() == null || csc.getUserCode().length() == 0))
            csc.setUserCode(getSessionContext().getCallerPrincipal().getName());
        
    	return csc;
    }
    
	/***********************************************************************************************/
	/***********************************************************************************************/
    protected TierAccessLoggerInfo initTier(ClientSessionContext csc, MethodInputBean input){
    	TierAccessLoggerInfo result = ClientTierAccessLogger.initTier(csc,input);
    	return result;
    }
    
    public SessionContext getSessionContext() {
            return sessionContext;
    }

    public void setSessionContext(SessionContext sessionContext) {
            this.sessionContext = sessionContext;
    }

}
