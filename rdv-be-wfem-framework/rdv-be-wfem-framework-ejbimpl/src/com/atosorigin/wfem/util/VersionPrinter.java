package com.atosorigin.wfem.util;

import java.util.Vector;

import com.atosorigin.wfem.loggers.LogPrinter;

public class VersionPrinter {

	private static Vector printedModules = new Vector();
	private static VersionPrinter singleton = null;
	
	/****************************************************************/
	/****************************************************************/
	public static VersionPrinter getInstance(){
		if(singleton == null) {
	        synchronized (VersionPrinter.class) {
		        if(singleton == null){
					singleton = new VersionPrinter();
		        }
	        }
		}
		return singleton;		
	}
	
	/****************************************************************/
	/****************************************************************/
	public void print(String moduleName, Object refClassLoaderObject){
		moduleName = moduleName.toUpperCase();
		if(printedModules.contains(moduleName))
			return;
		printedModules.add(moduleName);
		String moduleVersion = "";
		try{
			moduleVersion = Tools.loadTextResourceAsString("/prgm/moduleVersion/"+moduleName+".version",refClassLoaderObject);
		}catch(Exception e){
			moduleVersion = "Version file for module ["+moduleName+"] not found";
		}
		LogPrinter.println("***************************************************************************************");
		LogPrinter.println("** Module: ["+moduleName+"] - Version: ["+moduleVersion+"]");
		LogPrinter.println("***************************************************************************************");
	}
	
}
