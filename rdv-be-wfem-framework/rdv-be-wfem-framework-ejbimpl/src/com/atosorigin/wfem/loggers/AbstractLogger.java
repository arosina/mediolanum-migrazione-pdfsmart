package com.atosorigin.wfem.loggers;

import java.lang.reflect.Method;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.controller.Configuration;

/****************************************************************/
/****************************************************************/
public abstract class AbstractLogger {

	protected String logger = "";
	protected Method getLevelMethod;
	protected int defaultTraceLevel = 0;
	
	private static final String DEBUG   = "DEBUG  ";
	private static final String ERROR   = "ERROR  ";
	private static final String INFO    = "INFO   ";
	private static final String WARNING = "WARNING";
	private static final String NOTHING = "       ";
		
	/****************************************************************/
	/****************************************************************/
	 private void callPrint(ClientSessionContext csc, String type, Object o) {
	 	String msg = new String(logger);
	 	String tracedUsercode = Configuration.getInstance().getTracedUserCode();
	 	if(csc != null){
		 	if(tracedUsercode.length() > 0 && !tracedUsercode.equals(csc.getUserCode())) // If traced usercode defined but is not current: do nothing
		 		return;

	 		msg += " ["+csc.getUserCode()+"] ";
	 	}else{
	 		if(tracedUsercode.length() > 0)
	 			return;
	 	}
	 	msg += type +": ";
	 	if(o != null)
		 	msg += o.toString();
	 	LogPrinter.println(msg);
	 }
	 
	/****************************************************************/
	/****************************************************************/
	public void debug(ClientSessionContext csc, Object o) {
		if(getTraceLevel() >= 3)
			callPrint(csc,DEBUG,o);
	}
	/****************************************************************/
	/****************************************************************/
	public void debug(Object o) {
		debug(null, o);
	}
	
	/****************************************************************/
	/****************************************************************/
	public void error(ClientSessionContext csc, Exception e) {
		callPrint(csc,ERROR,e);
		e.printStackTrace();
	}
	/****************************************************************/
	/****************************************************************/
	public void error(Exception e) {
		error(null,e);
	}
	
	/****************************************************************/
	/****************************************************************/
	public void error(ClientSessionContext csc, Throwable e) {
		callPrint(csc,ERROR,e);
		e.printStackTrace();
	}
	/****************************************************************/
	/****************************************************************/
	public void error(Throwable e) {
		error(null,e);
	}
	
	/****************************************************************/
	/****************************************************************/
	public void info(ClientSessionContext csc, Object o) {
		if(getTraceLevel() >= 2)
			callPrint(csc,INFO,o);
	}
	/****************************************************************/
	/****************************************************************/
	public void info(Object o) {
		info(null,o);
	}
	
	/****************************************************************/
	/****************************************************************/
	public void print(ClientSessionContext csc, Object o) {
		callPrint(csc,NOTHING,o);
	}
	/****************************************************************/
	/****************************************************************/
	public void print(Object o) {
		print(null,o);
	}

	/****************************************************************/
	/****************************************************************/
	public void warning(ClientSessionContext csc, Object o) {
		if(getTraceLevel() >= 1)
			callPrint(csc,WARNING,o);
	}
	/****************************************************************/
	/****************************************************************/
	public void warning(Object o) {
		warning(null,o);
	}

	/****************************************************************/
	/****************************************************************/
	private int getTraceLevel(){
		
		if(getLevelMethod == null)
			return defaultTraceLevel;
			
		try{
		    Integer traceLevel = (Integer)getLevelMethod.invoke(Configuration.getInstance(),null);
		    return traceLevel.intValue();
		}catch(Exception e){
			error(e);
			return 0;
		}
	}
}
