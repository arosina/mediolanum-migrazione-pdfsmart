package com.atosorigin.wfem.controller;

import java.util.Hashtable;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.UserSessionContext;

public abstract class Command implements java.io.Serializable{
	protected static transient com.atosorigin.wfem.util.Logger LOG = com.atosorigin.wfem.util.Logger.getInstance();
	protected static transient com.atosorigin.wfem.util.RemoteObjectFactory ROF = com.atosorigin.wfem.util.RemoteObjectFactory.getInstance();
	private Hashtable persistentModels = null;

	// To put in garbage !!!!
	private javax.servlet.http.HttpServletRequest request = null;
	
	/******************************************************************************************************/
	/******************************************************************************************************/
	public abstract CommandDataModel execute(UserSessionContext userSessionContext,
		                                       CommandDataModel dataModel) throws CommandException;
	
	/******************************************************************************************************/
	/******************************************************************************************************/
	public String getCommandName() {
	
		String commandClass = this.getClass().getName();
	
		int lastPoint = commandClass.lastIndexOf(".");
	
		if ( lastPoint != -1 ) {
			return commandClass.substring( lastPoint+1 );
		}
		return commandClass;
	}
	
	/******************************************************************************************************/
	/******************************************************************************************************/
	public abstract Class getInputViewClass();
	
	/******************************************************************************************************/
	/******************************************************************************************************/
	public Class getOutputViewClass(){
		return null;
	}
	
	/******************************************************************************************************/
	/******************************************************************************************************/
	public CommandDataModel getPersistentDataModel(Class dataModelClass) {
		if(persistentModels == null)
			return null;
		return (CommandDataModel)persistentModels.get(dataModelClass.getName());
	}
	
	/******************************************************************************************************/
	/******************************************************************************************************/
	public void setPersistentDataModel(CommandDataModel dataModel) {
		if(persistentModels == null)
			persistentModels = new Hashtable();
		persistentModels.put(dataModel.getClass().getName(),dataModel);
		return;
	}
	
	/******************************************************************************************************/
	/******************************************************************************************************/
	public void unsetPersistentDataModel(Class dataModelClass) {
		if(persistentModels == null)
			return;
		persistentModels.remove(dataModelClass.getName());
		return;
	}
	
	/******************************************************************************************************/
	/******************************************************************************************************/
	public void setPersistentModels(Hashtable persistentModels) {
		this.persistentModels = persistentModels;
	}

	/******************************************************************************************************/
	/******************************************************************************************************/
	public Hashtable getPersistentModels() {
		return persistentModels;
	}
	
	/******************************************************************************************************
	 @deprecated
	******************************************************************************************************/
	protected javax.servlet.http.HttpServletRequest getRequest() {
		return request;
	}
	
	/******************************************************************************************************
	 @deprecated
	******************************************************************************************************/
	protected void toGarbageSetRequest(javax.servlet.http.HttpServletRequest request) {
		this.request = request;
	}
}
