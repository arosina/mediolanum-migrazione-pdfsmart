package com.atosorigin.wfem.loggers;

import com.atosorigin.wfem.controller.Configuration;

/****************************************************************/
/****************************************************************/
public class RefresherLogger extends AbstractLogger{
	
	private static RefresherLogger singleton = null;
	
	/****************************************************************/
	/****************************************************************/
	private RefresherLogger(){
		logger = "REFRESHER   ";
		try{
		    getLevelMethod = Configuration.class.getMethod("getRefresherTraceLevel",null);
		}catch(Exception e){
			getLevelMethod = null;
		}
	}
	
	/****************************************************************/
	/****************************************************************/
	public static RefresherLogger getInstance() {
		if(singleton == null) {
	        synchronized (RefresherLogger.class) {
		        if(singleton == null){
					singleton = new RefresherLogger();
		        }
	        }
		}
		return singleton;
	}
}
