package com.atosorigin.wfem.command;

import java.util.List;

/*******************************************************************/
/*******************************************************************/
public class PrintFdfCommandModelContainer extends CommandDataModel {
	
	private String suggestedFileName = null;
	
	private java.util.List fdfCommandList = new java.util.ArrayList();
	private java.util.List fdfCommandTitleList = new java.util.ArrayList();
	private java.util.List fdfCommandModelList = new java.util.ArrayList();

	/*******************************************************************/
	/*******************************************************************/
	public void addPrintFdfCommand(String title, Class printFdfCommandClass, 
									CommandDataModel printFdfCommandModel){
		addPrintFdfObject(title,printFdfCommandClass,printFdfCommandModel);
	}
	
	/*******************************************************************/
	/*******************************************************************/
	public void addPrintFdfCommand(String title, String printPdfAsClassName, 
									CommandDataModel printFdfCommandModel){
		addPrintFdfObject(title,printPdfAsClassName,printFdfCommandModel);
	}
	
	/*******************************************************************/
	/*******************************************************************/
	private void addPrintFdfObject(String title, Object printObject, 
									CommandDataModel printFdfCommandModel){
		fdfCommandTitleList.add(title);
		fdfCommandList.add(printObject);
		fdfCommandModelList.add(printFdfCommandModel);
	}
	
	/*******************************************************************/
	/*******************************************************************/
	public java.util.List getFdfCommandList() {
		return fdfCommandList;
	}

	/*******************************************************************/
	/*******************************************************************/
	public java.util.List getFdfCommandModelList() {
		return fdfCommandModelList;
	}

	/*******************************************************************/
	/*******************************************************************/
	public java.util.List getFdfCommandTitleList() {
		return fdfCommandTitleList;
	}
	
	/*******************************************************************/
	/*******************************************************************/
	public void loadPrintContainer(PrintFdfCommandModelContainer src){
		List cmds = src.getFdfCommandList();
		List cmdModels = src.getFdfCommandModelList();
		List titles = src.getFdfCommandTitleList();
		for(int i=0;i<cmds.size();i++)
			addPrintFdfObject((String)titles.get(i),cmds.get(i),(CommandDataModel)cmdModels.get(i));
	}

	/*******************************************************************/
	/*******************************************************************/
	public String getSuggestedFileName() {
		return suggestedFileName;
	}

	/*******************************************************************/
	/*******************************************************************/
	public void setSuggestedFileName(String suggestedFileName) {
		this.suggestedFileName = suggestedFileName;
	}

}
