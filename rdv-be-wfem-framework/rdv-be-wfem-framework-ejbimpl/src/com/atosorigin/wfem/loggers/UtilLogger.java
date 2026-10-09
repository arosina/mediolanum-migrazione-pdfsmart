package com.atosorigin.wfem.loggers;

import com.atosorigin.wfem.controller.Configuration;

/****************************************************************/
/****************************************************************/
public class UtilLogger extends AbstractLogger{
	
	private static UtilLogger singleton = null;

	/****************************************************************/
	/****************************************************************/
	private UtilLogger() {
		logger = "UTILITY     ";
		try{
		    getLevelMethod = Configuration.class.getMethod("getUtilTraceLevel",null);
		}catch(Exception e){
			getLevelMethod = null;
		}
	}
	
	/****************************************************************/
	/****************************************************************/
	public static UtilLogger getInstance() {
		if(singleton == null) {
	        synchronized (UtilLogger.class) {
		        if(singleton == null){
					singleton = new UtilLogger();
		        }
	        }
		}
		return singleton;
	}
}
