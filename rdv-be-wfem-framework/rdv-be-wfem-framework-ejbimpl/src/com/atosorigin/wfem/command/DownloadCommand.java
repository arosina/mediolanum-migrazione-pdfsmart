package com.atosorigin.wfem.command;

import java.io.File;

/***********************************************************************************************/
/***********************************************************************************************/
public abstract class DownloadCommand extends BusinessCommand {
	public abstract void downloadTerminated();	
	public abstract File getFile();
}
