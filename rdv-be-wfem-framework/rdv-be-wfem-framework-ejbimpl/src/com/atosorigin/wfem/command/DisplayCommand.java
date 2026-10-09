package com.atosorigin.wfem.command;

/**
 * Insert the type's description here.
 * Creation date: (24/04/2002 17.28.19)
 * @author: Administrator
 */
public abstract class DisplayCommand extends com.atosorigin.wfem.controller.Command {
	
	public static final int JSP_COMMAND = 1;
	public static final int XSL_COMMAND = 2;
	public static final int STATUS_COMMAND = 3;
	
	private int commandType = JSP_COMMAND;
	private boolean stepCommand = false;
	private boolean noSubmitCommand = false;
	private boolean fragmentCommand = false;

	public DisplayCommand() {
		super();
	}

	public String getExtension() {
		return "jsp";
	}
	
	public boolean isStepCommand() {
		return stepCommand;
	}

	public boolean isNoSubmitCommand() {
		return noSubmitCommand;
	}

	public void setNoSubmitCommand(boolean newNoSubmitCommand) {
		noSubmitCommand = newNoSubmitCommand;
	}
	
	public void setStepCommand(boolean stepCommand) {
		this.stepCommand = stepCommand;
	}

	public int getCommandType() {
		return commandType;
	}

	public void setCommandType(int commandType) {
		this.commandType = commandType;
	}

	public boolean isFragmentCommand() {
		return fragmentCommand;
	}

	public void setFragmentCommand(boolean fragmentCommand) {
		this.fragmentCommand = fragmentCommand;
	}

}
