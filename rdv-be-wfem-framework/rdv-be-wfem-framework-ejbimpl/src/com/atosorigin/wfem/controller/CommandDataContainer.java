package com.atosorigin.wfem.controller;

import com.atosorigin.wfem.command.UserSessionContext;

/**************************************************************************************************/
/**************************************************************************************************/
public class CommandDataContainer {
	private UserSessionContext userContext = null;
	private Command command = null;
	private Object model = null;
	
	public UserSessionContext getUserContext() {
		return userContext;
	}
	public void setUserContext(UserSessionContext userContext) {
		this.userContext = userContext;
	}
	public Command getCommand() {
		return command;
	}
	public void setCommand(Command command) {
		this.command = command;
	}
	public Object getModel() {
		return model;
	}
	public void setModel(Object model) {
		this.model = model;
	}
}
