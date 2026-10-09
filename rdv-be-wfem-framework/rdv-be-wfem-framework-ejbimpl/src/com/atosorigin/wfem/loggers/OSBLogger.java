package com.atosorigin.wfem.loggers;

import com.atosorigin.wfem.controller.Configuration;

/****************************************************************/
/****************************************************************/
public class OSBLogger extends AbstractLogger{
	
	private static OSBLogger singleton = null;

	/****************************************************************/
	/****************************************************************/
	private OSBLogger() {
		logger = "WS          ";
		try{
		    getLevelMethod = Configuration.class.getMethod("getOSBTraceLevel",null);
		}catch(Exception e){
			getLevelMethod = null;
		}
	}
	
	/****************************************************************/
	/****************************************************************/
	public static OSBLogger getInstance() {
		if(singleton == null) {
	        synchronized (OSBLogger.class) {
		        if(singleton == null){
					singleton = new OSBLogger();
		        }
	        }
		}
		return singleton;
	}
}
