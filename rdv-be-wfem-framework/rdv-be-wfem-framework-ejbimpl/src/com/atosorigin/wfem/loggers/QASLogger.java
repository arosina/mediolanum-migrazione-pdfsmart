package com.atosorigin.wfem.loggers;

import com.atosorigin.wfem.controller.Configuration;

/****************************************************************/
/****************************************************************/
public class QASLogger extends AbstractLogger{
	
	private static QASLogger singleton = null;

	/****************************************************************/
	/****************************************************************/
	private QASLogger() {
		logger = "QAS         ";
		try{
		    getLevelMethod = Configuration.class.getMethod("getQASTraceLevel",null);
		}catch(Exception e){
			getLevelMethod = null;
		}
	}
	
	/****************************************************************/
	/****************************************************************/
	public static QASLogger getInstance() {
		if(singleton == null) {
	        synchronized (QASLogger.class) {
		        if(singleton == null){
					singleton = new QASLogger();
		        }
	        }
		}
		return singleton;
	}
}
