package com.atosorigin.wfem.loggers;

import com.atosorigin.wfem.controller.Configuration;

/****************************************************************/
/****************************************************************/
public class BkRofLogger extends AbstractLogger{
	
	private static BkRofLogger singleton = null;
	
	/****************************************************************/
	/****************************************************************/
	private BkRofLogger(){
		logger = "BKROF       ";
		try{
		    getLevelMethod = Configuration.class.getMethod("getRofTraceLevel",null);
		}catch(Exception e){
			getLevelMethod = null;
		}
	}
	
	/****************************************************************/
	/****************************************************************/
	public static BkRofLogger getInstance() {
		if(singleton == null) {
	        synchronized (BkRofLogger.class) {
		        if(singleton == null){
					singleton = new BkRofLogger();
		        }
	        }
		}
		return singleton;
	}
}
