package com.atosorigin.wfem.command;

import java.io.ByteArrayOutputStream;


/**************************************************************************************************/
/**************************************************************************************************/
public abstract class BusinessCommand extends com.atosorigin.wfem.controller.Command {
	
	private Object nextCommandObject = null;
	private boolean executeOnForward = true;
	private boolean transactional = true;
	private String sourcePropertyName;
	private String targetPropertyName;
	private String propertiesNamePrefix = "";
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public BusinessCommand() {
		super();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public Class getInMemoryPdfCommand(){
		return null;
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setInMemoryPdfCommand(ByteArrayOutputStream pdf){
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public CommandDataModel getInMemoryPdfCommandInputModel(){
		return null;
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public CommandDataModel getInMemoryPdfCommandOutputModel(){
		return null;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public java.lang.String getNextCommand() {
	
		if(nextCommandObject instanceof Class){
			return ((Class)nextCommandObject).getName();
		}else if(nextCommandObject instanceof String){
			return (String)nextCommandObject;
		}else if(nextCommandObject instanceof Integer){
			return ((Integer)nextCommandObject).toString();
		}else{
			return null;
		}
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public java.lang.Object getNextCommandObject() {
		return nextCommandObject;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public boolean isExecuteOnForward() {
		return executeOnForward;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public boolean isTransactional() {
		return transactional;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setForwardDisplay(java.lang.Integer numForward) {
		nextCommandObject = numForward;
		executeOnForward = true;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setGenericCommandResponse(GenericCommandResponseModel genericResponseModel) {
		nextCommandObject = genericResponseModel;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setForwardDisplay(java.lang.Integer numForward, boolean execute) {
		nextCommandObject = numForward;
		executeOnForward = execute;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setNextCommandClass(java.lang.Class newNextCommandClass) {
		
		nextCommandObject = newNextCommandClass;
	
		sourcePropertyName = null;
		targetPropertyName = null;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setNextCommandClass(java.lang.Class newNextCommandClass,String sourcePropertyName, String targetPropertyName) {
		
		nextCommandObject = newNextCommandClass;
		this.sourcePropertyName = sourcePropertyName;
		this.targetPropertyName = targetPropertyName;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setNextDisplayPage(java.lang.String newNextDisplayPage) {
		nextCommandObject = newNextDisplayPage;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setTransactional(boolean newTransactional) {
		transactional = newTransactional;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getSourcePropertyName() {
		return sourcePropertyName;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getTargetPropertyName() {
		return targetPropertyName;
	}


	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getPropertiesNamePrefix() {
		return propertiesNamePrefix;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setPropertiesNamePrefix(String propertiesNamePrefix) {
		this.propertiesNamePrefix = propertiesNamePrefix;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setNextCommandObject(Object nextCommandObject) {
		this.nextCommandObject = nextCommandObject;
	}
}
