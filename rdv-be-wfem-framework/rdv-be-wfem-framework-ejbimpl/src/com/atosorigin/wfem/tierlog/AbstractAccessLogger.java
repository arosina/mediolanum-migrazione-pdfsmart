package com.atosorigin.wfem.tierlog;

import java.io.FileWriter;
import java.io.PrintWriter;

import com.atosorigin.wfem.controller.Configuration;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public abstract class AbstractAccessLogger {

	private PrintWriter printer;
	
	/****************************************************************/
	/****************************************************************/
	public static String formatParameters(String value){
		String res = value.replaceAll("(\\|*)(\\;*)(\n*)(\r*)","");
		return res.length() > 50 ? res.substring(0,50)+"..." : res;
			
	}
	
	/****************************************************************/
	/****************************************************************/
	protected void initLogger(String fileSuffix){
		try{
			printer = null;
			String tierTraceDir = Configuration.getInstance().getTierTraceDir();
			if(tierTraceDir != null){
				String fileName = tierTraceDir+fileSuffix+"TierAccessLog.csv";
				printer = new PrintWriter(new FileWriter(fileName,true),true); 
			}
		}catch(Exception e){
			printer = null;
			e.printStackTrace();
		}
	}
	
	/****************************************************************/
	/****************************************************************/
	public void writeAccessLog(TierAccessLoggerInfo ali){
		if(printer == null)
			return;
		
		if(ali.getAccessId() == null)
			return;
		
		try{
			printer.println(ali.traceString());
		}catch(Throwable t){
			t.printStackTrace();
		}
	}
}
