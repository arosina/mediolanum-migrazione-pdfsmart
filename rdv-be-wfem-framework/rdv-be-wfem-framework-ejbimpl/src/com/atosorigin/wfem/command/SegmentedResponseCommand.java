package com.atosorigin.wfem.command;

/***********************************************************************************************/
/***********************************************************************************************/
public abstract class SegmentedResponseCommand extends BusinessCommand {
	public abstract boolean resourceExist();	
	public abstract byte[] getResponseSegment() throws CommandException;
	public abstract String getContentType();	
	public abstract String getFileName();	
	public abstract int    getResponseLength();
	public abstract void   responseTerminated();	
	
}
