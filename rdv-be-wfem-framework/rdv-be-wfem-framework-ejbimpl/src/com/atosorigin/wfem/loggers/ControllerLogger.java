package com.atosorigin.wfem.loggers;

import com.atosorigin.wfem.controller.Configuration;

/****************************************************************/
/****************************************************************/
public class ControllerLogger extends AbstractLogger{
	
	private static ControllerLogger singleton = null;
	
	/****************************************************************/
	/****************************************************************/
	private ControllerLogger() {
		logger = "CONTROLLER  ";
		try{
		    getLevelMethod = Configuration.class.getMethod("getControllerTraceLevel",null);
		}catch(Exception e){
			getLevelMethod = null;
		}
	}
	
	/****************************************************************/
	/****************************************************************/
	public static ControllerLogger getInstance() {
		if(singleton == null) {
	        synchronized (ControllerLogger.class) {
		        if(singleton == null){
					singleton = new ControllerLogger();
		        }
	        }
		}
		return singleton;
	}
}
