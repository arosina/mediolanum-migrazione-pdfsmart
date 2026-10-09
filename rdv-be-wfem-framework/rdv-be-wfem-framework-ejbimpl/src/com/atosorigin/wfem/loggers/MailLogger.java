package com.atosorigin.wfem.loggers;

import com.atosorigin.wfem.controller.Configuration;

/****************************************************************/
/****************************************************************/
public class MailLogger extends AbstractLogger{
	
	private static MailLogger singleton = null;
	
	/****************************************************************/
	/****************************************************************/
	private MailLogger() {
		logger = "MAIL        ";
		try{
		    getLevelMethod = Configuration.class.getMethod("getMailTraceLevel",null);
		}catch(Exception e){
			getLevelMethod = null;
		}
	}
	
	/****************************************************************/
	/****************************************************************/
	public static MailLogger getInstance() {
		if(singleton == null) {
	        synchronized (MailLogger.class) {
		        if(singleton == null){
					singleton = new MailLogger();
		        }
	        }
		}
		return singleton;
	}
}
