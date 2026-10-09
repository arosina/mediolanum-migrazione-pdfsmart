package com.atosorigin.wfem.loggers;

import com.atosorigin.wfem.controller.Configuration;

/****************************************************************/
/****************************************************************/
public class DAOLogger extends AbstractLogger{
	
	private static DAOLogger singleton = null;
	
	/****************************************************************/
	/****************************************************************/
	private DAOLogger() {
		logger = "DAO         ";
		try{
		    getLevelMethod = Configuration.class.getMethod("getDaoTraceLevel",null);
		}catch(Exception e){
			getLevelMethod = null;
		}
	}
	
	/****************************************************************/
	/****************************************************************/
	public static DAOLogger getInstance() {
		if(singleton == null) {
	        synchronized (DAOLogger.class) {
		        if(singleton == null){
					singleton = new DAOLogger();
		        }
	        }
		}
		return singleton;
	}
}
