package com.atosorigin.wfem.tierlog;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOAccessInfo;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class MiddleTierAccessLogger extends AbstractAccessLogger {
	
	private static MiddleTierAccessLogger singleton = null;
	
	/****************************************************************/
	/****************************************************************/
	private MiddleTierAccessLogger(){
		try{
			initLogger("Middle");
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/********************************************************************/
	/********************************************************************/
	public static MiddleTierAccessLogger getInstance() {
		if(singleton == null) {
	        synchronized (MiddleTierAccessLogger.class) {
				if(singleton == null) {
					singleton = new MiddleTierAccessLogger();
				}
	        }
		}
		return singleton;
	}
	
	/****************************************************************/
	/****************************************************************/
	public static TierAccessLoggerInfo initTier(ClientSessionContext csc, String accessType, DAOAccessInfo access){
		String userCode = "unknown";
		String daoObjectName = "unknown";
		String daoAccessName = "unknown";
		
		if(csc != null && csc.getUserCode() != null)
			userCode = csc.getUserCode().toString();
		
		if(access != null && access.getDaoObjectName() != null)
			daoObjectName = access.getDaoObjectName();
		
		if(access != null && access.getDaoAccessName() != null)
			daoAccessName = access.getDaoAccessName();

		String crossTierTraceId = csc.getCrossTierTraceId();
		TierAccessLoggerInfo ali = new TierAccessLoggerInfo();
		ali.setTier(TierAccessLoggerInfo.MIDDLE_TIER);
		ali.setAccessId(crossTierTraceId); // null if client command is not traced
		ali.setAccessUser(userCode);
		ali.setAccessType(accessType);
		ali.setAccessName(daoObjectName+"@"+daoAccessName);
		ali.setClientIp(csc.getClientIp());
		ali.setInputParameters("");
		ali.start();
		return ali;
	}

	/****************************************************************/
	/****************************************************************/
	public static TierAccessLoggerInfo initTier(ClientSessionContext csc, String accessType, String accessName){
		String userCode = "unknown";
		
		if(csc != null && csc.getUserCode() != null)
			userCode = csc.getUserCode().toString();
		
		String crossTierTraceId = csc.getCrossTierTraceId();
		TierAccessLoggerInfo ali = new TierAccessLoggerInfo();
		ali.setTier(TierAccessLoggerInfo.MIDDLE_TIER);
		ali.setAccessId(crossTierTraceId); // null if client command is not traced
		ali.setAccessUser(userCode);
		ali.setAccessType(accessType);
		ali.setAccessName(accessName);
		ali.setClientIp(csc.getClientIp());
		ali.setInputParameters("");
		ali.start();
		return ali;
	}
}
