package com.atosorigin.wfem.controller;

import java.util.HashMap;
import java.util.Map;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class ManagedCommands{
		
	public static final Map hashedCommands = new HashMap();
	
	public static final String showExternalPageCmd   			= "showExternalPage"; 			// Show external urls
	public static final String showLoginCmd          			= "showLogin";					// Show the login page (if login is managed)
	public static final String executeLoginCmd       			= "executeLogin";				// Execute the autentication on login manager
	public static final String executeChangePwdCmd   			= "executeChangePwd";			// Execute the change password on login manager
	public static final String selectCmd             			= "select";					// Select an element from a list an pass to the command
	public static final String selectOnNewThreadCmd  			= "selectOnNewThread";			// Select an element from a list an pass to the command on a new stack
	public static final String selectOnPopupCmd      			= "selectOnPopup";				// Select an element from a list an pass to the command on popup
	public static final String nextPageCmd           			= "nextPage";					// Manage list paginaiton
	public static final String previousPageCmd       			= "previousPage";				// Manage list paginaiton
	public static final String firstPageCmd          			= "firstPage";					// Manage list paginaiton
	public static final String lastPageCmd           			= "lastPage";					// Manage list paginaiton
	public static final String gotoPageCmd           			= "gotoPage";					// Manage list paginaiton
	public static final String executeProcessCmd     			= "executeProcess";				// Execute a new process
	public static final String executeProcessOnNewStackCmd     	= "executeProcessOnNewStack";	// Execute a new process on a new stack
	public static final String closeProcessStackCmd    			= "closeProcessStack";				// Close a process stack
	public static final String executeCmd            			= "execute";					// Execute a command
	public static final String executeOnNewThreadCmd 			= "executeOnNewThread";		// Execute a command on a new stack
	public static final String executeOnPopupCmd     			= "executeOnPopup";			// Execute a command
	public static final String executeCurrentCmd     			= "executeCurrentDisplay";		// Execute the current display command
	public static final String executeLastCmd        			= "executeLastDisplay";		// Execute the last display command
	public static final String showCurrentCmd        			= "showCurrentDisplay";		// Show the current display command wihtout executing it
	public static final String showLastCmd           			= "showLastDisplay";			// Show the last display command without executing it
	public static final String showPageCmd           			= "showPage";					// Show the specified page
	public static final String showPdfCmd            			= "showPdf";					// Show the specified pdf (or more than one)
	public static final String getScriptCmd          			= "getScript";					// Supplies script file by ResourceSupplier
	public static final String getCssCmd             			= "getCss";					// Supplies css file by ResourceSupplier
	public static final String getImageCmd           			= "getImage";					// Supplies img file by ResourceSupplier
	public static final String getChartCmd           			= "getChart";					// Return a chart image
	public static final String getBlankPageCmd       			= "getBlankPage";				// Supplies a blank HTML page to avoid HTTPS secure content problems
	public static final String getWebResourceCmd     			= "getWebResource";			// Supplies a Web resource
	public static final String closeThreadCmd 	     			= "closeThread";				// Close a thread stack
	public static final String invalidateSession     			= "invalidateSession";			// Invalidate session
	public static final String openGridPdfCmd        			= "openGridPdf";				// Show grid in pdf
	public static final String openGridCalcCmd       			= "openGridCalc";				// Show grid in calc
	public static final String loadFileTypeCmd  	 			= "loadFileType";				// Load the FileType property
	public static final String clearFileTypeCmd 	 			= "clearFileType";				// Clear the FileType property
	public static final String showFileTypeCmd  	 			= "showFileType";				// Show the FileType property
	public static final String uploadPdfCmd 		 			= "uploadPdf";					// Upload pdf
    public static final String showErrorsPageCmd 	 			= "showErrorsPage";			// Show a page with all fields in error
    public static final String keepAliveCmd 	 	 			=  "keepalive";				// To keep alive user session
    public static final String loadApplicationManual 			=  "loadApplicationManual";	// Load the document from DB
    public static final String executeBoReport 		 			=  "executeBoReport";			// Execute a Business Objects report
    public static final String streamRemoteFile 	 			=  "streamRemoteFile";			// Stream a remote file
    public static final String pingCmd 	 	 					=  "ping";						// To ping servlet

	public static final int iselectCmd              		= 1;
	public static final int iselectOnNewThreadCmd   		= 2;
	public static final int iselectOnPopupCmd       		= 3;
	public static final int inextPageCmd            		= 4;
	public static final int ipreviousPageCmd        		= 5;
	public static final int ifirstPageCmd           		= 6;
	public static final int ilastPageCmd            		= 7;
	public static final int igotoPageCmd            		= 8;
	public static final int iexecuteProcessCmd      		= 9;
	public static final int iexecuteCmd             		= 10;
	public static final int iexecuteOnNewThreadCmd  		= 11;
	public static final int iexecuteOnPopupCmd      		= 12;
	public static final int iexecuteCurrentCmd      		= 13;
	public static final int iexecuteLastCmd         		= 14;
	public static final int ishowCurrentCmd         		= 15;
	public static final int ishowLastCmd            		= 16;
	public static final int ishowPageCmd            		= 17;
	public static final int ishowPdfCmd             		= 18;
	public static final int icloseThreadCmd         		= 19;
	public static final int iinvalidateSession      		= 20;
	public static final int iopenGridPdfCmd         		= 21;
	public static final int iopenGridCalcCmd        		= 22;
	public static final int iloadFileTypeCmd    			= 23;
	public static final int iclearFileTypeCmd 				= 24;
	public static final int ishowFileTypeCmd  				= 25;
	public static final int ishowExternalPageCmd    		= 26;
	public static final int iUploadPdfCmd    				= 27;
	public static final int iloadApplicationManual 			= 28;
	public static final int iexecuteBoReport 				= 29;
	public static final int istreamRemoteFile				= 30;
	public static final int iexecuteProcessOnNewStackCmd	= 31;
	public static final int icloseProcessStackCmd			= 32;
	
	public static final int ishowLoginCmd           = 1001;
	public static final int iexecuteLoginCmd        = 1002;
	public static final int iexecuteChangePwdCmd    = 1003;
	public static final int igetCssCmd              = 1004;
	public static final int igetScriptCmd           = 1005;
	public static final int igetImageCmd            = 1006;
	public static final int igetChartCmd            = 1007;
	public static final int igetBlankPageCmd        = 1008;
	public static final int iShowErrorsPageCmd      = 1009;
	public static final int igetWebResourceCmd      = 1010;
	public static final int ikeepAliveCmd      		= 1011;
	public static final int ipingCmd      			= 1012;

	static{
		hashedCommands.put(showLoginCmd            ,new Integer(ishowLoginCmd           ));
		hashedCommands.put(executeLoginCmd         ,new Integer(iexecuteLoginCmd        ));
		hashedCommands.put(executeChangePwdCmd     ,new Integer(iexecuteChangePwdCmd    ));
		hashedCommands.put(getCssCmd               ,new Integer(igetCssCmd              ));
		hashedCommands.put(getScriptCmd            ,new Integer(igetScriptCmd           ));
		hashedCommands.put(getImageCmd             ,new Integer(igetImageCmd            ));
		hashedCommands.put(getChartCmd             ,new Integer(igetChartCmd            ));
		hashedCommands.put(getBlankPageCmd         ,new Integer(igetBlankPageCmd        ));
		hashedCommands.put(showErrorsPageCmd       ,new Integer(iShowErrorsPageCmd      ));
		hashedCommands.put(getWebResourceCmd       ,new Integer(igetWebResourceCmd      ));
		hashedCommands.put(keepAliveCmd       	   ,new Integer(ikeepAliveCmd      		));
		hashedCommands.put(pingCmd       	   	   ,new Integer(ipingCmd      			));
		
		hashedCommands.put(selectCmd               			,new Integer(iselectCmd              ));
		hashedCommands.put(selectOnNewThreadCmd    			,new Integer(iselectOnNewThreadCmd   ));
		hashedCommands.put(selectOnPopupCmd        			,new Integer(iselectOnPopupCmd       ));
		hashedCommands.put(nextPageCmd             			,new Integer(inextPageCmd            ));
		hashedCommands.put(previousPageCmd         			,new Integer(ipreviousPageCmd        ));
		hashedCommands.put(firstPageCmd            			,new Integer(ifirstPageCmd           ));
		hashedCommands.put(lastPageCmd             			,new Integer(ilastPageCmd            ));
		hashedCommands.put(gotoPageCmd             			,new Integer(igotoPageCmd            ));
		hashedCommands.put(executeProcessCmd       			,new Integer(iexecuteProcessCmd      ));
		hashedCommands.put(executeProcessOnNewStackCmd      ,new Integer(iexecuteProcessOnNewStackCmd    ));
		hashedCommands.put(closeProcessStackCmd       		,new Integer(icloseProcessStackCmd    ));
		hashedCommands.put(executeCmd              			,new Integer(iexecuteCmd             ));
		hashedCommands.put(executeOnNewThreadCmd   			,new Integer(iexecuteOnNewThreadCmd  ));
		hashedCommands.put(executeOnPopupCmd       			,new Integer(iexecuteOnPopupCmd      ));
		hashedCommands.put(executeCurrentCmd       			,new Integer(iexecuteCurrentCmd      ));
		hashedCommands.put(executeLastCmd          			,new Integer(iexecuteLastCmd         ));
		hashedCommands.put(showCurrentCmd          			,new Integer(ishowCurrentCmd         ));
		hashedCommands.put(showLastCmd             			,new Integer(ishowLastCmd            ));
		hashedCommands.put(showPageCmd             			,new Integer(ishowPageCmd            ));
		hashedCommands.put(showPdfCmd              			,new Integer(ishowPdfCmd             ));
		hashedCommands.put(closeThreadCmd 	   	   			,new Integer(icloseThreadCmd        ));
		hashedCommands.put(invalidateSession       			,new Integer(iinvalidateSession      ));
		hashedCommands.put(openGridPdfCmd          			,new Integer(iopenGridPdfCmd         ));
		hashedCommands.put(openGridCalcCmd         			,new Integer(iopenGridCalcCmd        ));
		hashedCommands.put(loadFileTypeCmd  	   			,new Integer(iloadFileTypeCmd    	));
		hashedCommands.put(clearFileTypeCmd 	   			,new Integer(iclearFileTypeCmd 		));
		hashedCommands.put(showFileTypeCmd  	   			,new Integer(ishowFileTypeCmd  		));
		hashedCommands.put(showExternalPageCmd     			,new Integer(ishowExternalPageCmd    ));
		hashedCommands.put(uploadPdfCmd     	   			,new Integer(iUploadPdfCmd	    	));
		hashedCommands.put(loadApplicationManual   			,new Integer(iloadApplicationManual	));
		hashedCommands.put(executeBoReport   	   			,new Integer(iexecuteBoReport		));
		hashedCommands.put(streamRemoteFile	   	   			,new Integer(istreamRemoteFile		));
	}
	
	public static boolean isLoginfreeCommand(int commandCode){
		return (commandCode >= ishowLoginCmd ? true : false);
	}
	
	public static int getCommandCode(String commandName){
		Integer commandCode = (Integer)hashedCommands.get(commandName);
		return (commandCode == null ?  -1: commandCode.intValue());
	}
}
