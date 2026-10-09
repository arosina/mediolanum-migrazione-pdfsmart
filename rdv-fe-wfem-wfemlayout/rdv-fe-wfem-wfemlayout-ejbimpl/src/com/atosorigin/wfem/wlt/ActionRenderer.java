package com.atosorigin.wfem.wlt;

import java.util.regex.Pattern;

import com.atosorigin.wfem.layout.Template;

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public class ActionRenderer{
	
	private PageRenderer pageRenderer;
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	static class Patterns{
		static final Pattern onexecute 			= PageRenderer.compilePattern("onexecute");	
		static final Pattern enabled 			= PageRenderer.compilePattern("enabled");	
		static final Pattern text 				= PageRenderer.compilePattern("text");
	}	
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public ActionRenderer(PageRenderer pageRenderer){
		this.pageRenderer = pageRenderer;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String html(String actionName) {
		
		if(pageRenderer.getTemplate().getModality() == Template.PRINT_MODALITY)
			return "";
		
		Template t = pageRenderer.getTemplate();
		
		String onExecute = pageRenderer.getPar(Patterns.onexecute,"do"+actionName.substring(0,1).toUpperCase()+actionName.substring(1)).asString();
		boolean enabled = pageRenderer.getPar(Patterns.enabled,"true").asBoolean();
		String actionText = t.getProperty(t.getPageName()+actionName);
		if(t.getLabelCodePrefix() != null && !t.getLabelCodePrefix().equals(""))
			actionText = t.getProperty(t.getLabelCodePrefix()+actionName);
		actionText = pageRenderer.getPar(Patterns.text,actionText).asString();
		String disabled = "";
		String className = "action";
		if(!enabled){
			className = "disabledAction";
			disabled = " disabled ";
		}

		if(onExecute.indexOf("(") < 0)
			onExecute += "()";

		StringBuffer res = new StringBuffer();
		
		res.append("<table width='100%'><tr><td align='center'>");
		res.append("<input id='"+actionName+"' name='"+actionName+"' type='button' class='"+className+"' "+disabled);
		res.append(" onmouseover='if(this.className == \"action\"){this.className=\"selectedAction\";}'");
		res.append(" onmouseout='if(this.className == \"selectedAction\"){this.className=\"action\";}'");
		res.append(" onclick='if(this.disabled || this.className == \"disabledAction\")return; if(!isRequestPending()){if(eval(\""+onExecute+"\")){startRequest();}}' ");

		res.append(pageRenderer.getPars());
			
		res.append(" value=\""+actionText+"\">");
		res.append("</td></tr></table>");
		return res.toString();
		
	}

}
