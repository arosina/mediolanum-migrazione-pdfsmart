package com.atosorigin.wfem.loggers;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Calendar;

/****************************************************************/
/****************************************************************/
public class LogPrinter {
	
	private static LogPrinter singleton = null;
	private PrintWriter printer = null;
	
	/****************************************************************/
	/****************************************************************/
	private LogPrinter(){
		printer = null;
	}
	
	/****************************************************************/
	/****************************************************************/
	private LogPrinter(String logFilename){
		try{
			printer = null;
			if(logFilename != null)
				printer = new PrintWriter(new FileWriter(logFilename,true),true); 
		}catch(Exception e){
			printer = null;
			e.printStackTrace();
		}		
	}
	
	/****************************************************************/
	/****************************************************************/
	public static void initLogFilename(String logFilename){
		if(singleton == null) {
	        synchronized (LogPrinter.class) {
		        if(singleton == null){
					singleton = new LogPrinter(logFilename);
		        }
	        }
		}
		return;
	}
	
	/****************************************************************/
	/****************************************************************/
	private static LogPrinter getInstance() {
		if(singleton == null) {
	        synchronized (LogPrinter.class) {
		        if(singleton == null){
					singleton = new LogPrinter();
		        }
	        }
		}
		return singleton;
	}
	
	/****************************************************************/
	/****************************************************************/
	public static void println(String msg){
		try{
			msg = Calendar.getInstance().getTime()+": "+msg;
			LogPrinter lp = LogPrinter.getInstance();
			if(lp.printer == null)
				System.out.println(msg);
			else
				lp.printer.println(msg);
		}catch(Throwable t){
			t.printStackTrace();
		}
	}
	
	/****************************************************************/
	/****************************************************************/
	public static void print(String msg){
		try{
			msg = Calendar.getInstance().getTime()+": "+msg;
			LogPrinter lp = LogPrinter.getInstance();
			if(lp.printer == null)
				System.out.print(msg);
			else
				lp.printer.print(msg);
		}catch(Throwable t){
			t.printStackTrace();
		}
	}
}
