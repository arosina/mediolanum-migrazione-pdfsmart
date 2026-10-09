package com.atosorigin.wfem.bo;


/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class BoReportInfo {
	
	public static final int RESULT_OK = 0;
	public static final int RESULT_KO = -1;
	
	public int result = RESULT_OK;
	public String errorMessage;
	public byte[] content;
	public String mimeType;
	public String reportCommand;
}
