package com.atosorigin.wfem.layout;

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public abstract class FieldRenderer {
	public abstract String getHtml() throws Exception;
	public abstract String getPdf() throws Exception;
	public abstract String getCalc() throws Exception;
}
