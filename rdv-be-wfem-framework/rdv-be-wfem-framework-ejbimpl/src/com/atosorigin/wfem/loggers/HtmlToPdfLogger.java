package com.atosorigin.wfem.loggers;

import com.atosorigin.wfem.controller.Configuration;

/****************************************************************/
/****************************************************************/
public class HtmlToPdfLogger extends AbstractLogger{
	
	private static HtmlToPdfLogger singleton = null;

	/****************************************************************/
	/****************************************************************/
	private HtmlToPdfLogger(boolean loadConfiguration) {
		
		logger = "HTMLTOPDF   ";
		if(loadConfiguration){
			try{
			    getLevelMethod = Configuration.class.getMethod("getHtmlToPdfTraceLevel",null);
			}catch(Exception e){
				getLevelMethod = null;
			}
		}else{
			defaultTraceLevel = 3;
		}
	}
	
	/****************************************************************/
	/****************************************************************/
	public static HtmlToPdfLogger getInstance(boolean loadConfiguration) {
		if(singleton == null) {
	        synchronized (HtmlToPdfLogger.class) {
		        if(singleton == null){
					singleton = new HtmlToPdfLogger(loadConfiguration);
		        }
	        }
		}
		return singleton;
	}
}
