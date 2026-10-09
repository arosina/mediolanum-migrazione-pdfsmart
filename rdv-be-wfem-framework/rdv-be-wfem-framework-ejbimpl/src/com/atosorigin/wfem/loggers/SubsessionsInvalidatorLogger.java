package com.atosorigin.wfem.loggers;

import com.atosorigin.wfem.controller.Configuration;

/****************************************************************/
/****************************************************************/
public class SubsessionsInvalidatorLogger extends AbstractLogger{
	
	private static SubsessionsInvalidatorLogger singleton = null;
	
	/****************************************************************/
	/****************************************************************/
	private SubsessionsInvalidatorLogger() {
		logger = "SUBSESSINV  ";
		try{
		    getLevelMethod = Configuration.class.getMethod("getSubsessionsInvalidatorTraceLevel",null);
		}catch(Exception e){
			getLevelMethod = null;
		}
	}
	
	/****************************************************************/
	/****************************************************************/
	public static SubsessionsInvalidatorLogger getInstance() {
		if(singleton == null) {
	        synchronized (SubsessionsInvalidatorLogger.class) {
		        if(singleton == null){
					singleton = new SubsessionsInvalidatorLogger();
		        }
	        }
		}
		return singleton;
	}
}
