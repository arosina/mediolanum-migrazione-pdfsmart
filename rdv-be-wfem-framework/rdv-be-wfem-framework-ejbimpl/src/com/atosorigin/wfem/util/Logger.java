package com.atosorigin.wfem.util;

import com.atosorigin.wfem.controller.Configuration;
import com.atosorigin.wfem.loggers.AbstractLogger;

/****************************************************************/
/****************************************************************/
public class Logger extends AbstractLogger{
	
	private static Logger singleton = null;
	
	/****************************************************************/
	/****************************************************************/
	private Logger() {
		logger = "APPLICATION ";
		try{
		    getLevelMethod = Configuration.class.getMethod("getTraceLevel",null);
		}catch(Exception e){
			getLevelMethod = null;
		}
	}
	
	/****************************************************************/
	/****************************************************************/
	public static Logger getInstance() {
		if(singleton == null) {
	        synchronized (Logger.class) {
		        if(singleton == null){
					singleton = new Logger();
		        }
	        }
		}
		return singleton;
	}
}
