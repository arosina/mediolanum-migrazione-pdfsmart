package com.atosorigin.wfem.layout;

import com.atosorigin.wfem.types.AbstractType;

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public interface WLTPageIntf {

	public String header();
	public String footer();
	public String messagesAndErrors();	
	public String hidden(String propName);
	public String action(String propName,String extraPar);
	public String field(String propName,String extraPar);
	public String grid(String propName,String extraPar);
	public String inlineAnchor(String propName, AbstractType propValue);
	public String inlineMsgAnchor(String propName, AbstractType propValue);
	public String component(String componentType, String componentName, String extraPar);
	
}
