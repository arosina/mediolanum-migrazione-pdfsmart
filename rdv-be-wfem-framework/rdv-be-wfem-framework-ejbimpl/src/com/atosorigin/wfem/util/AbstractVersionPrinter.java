package com.atosorigin.wfem.util;

import com.atosorigin.wfem.loggers.LogPrinter;

public abstract class AbstractVersionPrinter {
	
	public AbstractVersionPrinter(){
		LogPrinter.println("***************************************************************************************");
		LogPrinter.println("** Module: ["+getModuleName()+"] - Version: ["+getModuleVersion()+"]");
		LogPrinter.println("***************************************************************************************");
	}
	public AbstractVersionPrinter(String moduleName){
		LogPrinter.println("***************************************************************************************");
		LogPrinter.println("** Module: ["+getModuleName()+"] - Version: ["+getModuleVersion()+"]");
		LogPrinter.println("***************************************************************************************");
	}
	public final String getModuleName(){
		return this.getClass().getPackage().getName().toUpperCase();
	}
	public abstract String getModuleVersion();
}
