package com.atosorigin.wfem.command;

import java.io.InputStream;

/***********************************************************************************************/
/***********************************************************************************************/
public abstract class DownloadStreamCommand extends BusinessCommand {
	public abstract void downloadTerminated();	
	public abstract InputStream getInputStream();
	public abstract String getFileName();	
	public abstract int    getFileLength();	
}
