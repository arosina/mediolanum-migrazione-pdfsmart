package com.atosorigin.wfem.loggers;

import com.atosorigin.wfem.controller.Configuration;

/****************************************************************/
/****************************************************************/
public class LayoutLogger extends AbstractLogger{
	
	private static LayoutLogger singleton = null;

	/****************************************************************/
	/****************************************************************/
	private LayoutLogger() {
		logger = "LAYOUT      ";
		try{
		    getLevelMethod = Configuration.class.getMethod("getLayoutTraceLevel",null);
		}catch(Exception e){
			getLevelMethod = null;
		}
	}
	
	/****************************************************************/
	/****************************************************************/
	public static LayoutLogger getInstance() {
		if(singleton == null) {
	        synchronized (LayoutLogger.class) {
		        if(singleton == null){
					singleton = new LayoutLogger();
		        }
	        }
		}
		return singleton;
	}
}
