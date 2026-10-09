package com.atosorigin.wfem.loggers;

import com.atosorigin.wfem.controller.Configuration;

/****************************************************************/
/****************************************************************/
public class RofLogger extends AbstractLogger{
	
	private static RofLogger singleton = null;
	
	/****************************************************************/
	/****************************************************************/
	private RofLogger(){
		logger = "ROF         ";
		try{
		    getLevelMethod = Configuration.class.getMethod("getRofTraceLevel",null);
		}catch(Exception e){
			getLevelMethod = null;
		}
	}
	
	/****************************************************************/
	/****************************************************************/
	public static RofLogger getInstance() {
		if(singleton == null) {
	        synchronized (RofLogger.class) {
		        if(singleton == null){
					singleton = new RofLogger();
		        }
	        }
		}
		return singleton;
	}
}
