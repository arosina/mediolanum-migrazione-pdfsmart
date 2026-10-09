package com.atosorigin.wfem.loggers;

import com.atosorigin.wfem.controller.Configuration;

/****************************************************************/
/****************************************************************/
public class DbLogger extends AbstractLogger{
	
	private static DbLogger singleton = null;

	/****************************************************************/
	/****************************************************************/
	private DbLogger() {
		logger = "DB          ";
		try{
		    getLevelMethod = Configuration.class.getMethod("getDbTraceLevel",null);
		}catch(Exception e){
			getLevelMethod = null;
		}
	}
	
	/****************************************************************/
	/****************************************************************/
	public static DbLogger getInstance() {
		if(singleton == null) {
	        synchronized (DbLogger.class) {
		        if(singleton == null){
					singleton = new DbLogger();
		        }
	        }
		}
		return singleton;
	}
}
