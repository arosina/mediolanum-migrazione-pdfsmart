package com.atosorigin.wfem.loggers;

import com.atosorigin.wfem.controller.Configuration;

/****************************************************************/
/****************************************************************/
public class PdfLogger extends AbstractLogger{
	
	private static PdfLogger singleton = null;

	/****************************************************************/
	/****************************************************************/
	private PdfLogger() {
		logger = "PDF         ";
		try{
		    getLevelMethod = Configuration.class.getMethod("getPdfTraceLevel",null);
		}catch(Exception e){
			getLevelMethod = null;
		}
	}
	
	/****************************************************************/
	/****************************************************************/
	public static PdfLogger getInstance() {
		if(singleton == null) {
	        synchronized (PdfLogger.class) {
		        if(singleton == null){
					singleton = new PdfLogger();
		        }
	        }
		}
		return singleton;
	}
}
