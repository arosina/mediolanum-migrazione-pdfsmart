package com.atosorigin.wfem.wlt;

import java.util.List;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandError;
import com.atosorigin.wfem.command.CommandMessage;
import com.atosorigin.wfem.command.CommandWarning;
import com.atosorigin.wfem.layout.Template;

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public class MsgErrRenderer{
	
	private PageRenderer pageRenderer;
	
	private static final String errStyle = "background-color:red;color:white;";
	private static final String warStyle = "background-color:khaki;";
	private static final String msgStyle = "background-color:lightcyan;";
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public MsgErrRenderer(PageRenderer pageRenderer){
		this.pageRenderer = pageRenderer;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String html() {
		
		if(pageRenderer.getTemplate().getModality() == Template.PRINT_MODALITY)
			return "";
		
		Template t = pageRenderer.getTemplate();
		StringBuffer res = new StringBuffer();
		
		CommandDataModel model = t.getPageDataModel();
		if(model == null)
			return res.toString();
		
		if(!model.hasCommandErrors() && !model.hasCommandMessages() && !model.hasCommandWarnings())
			return res.toString();
		
		res.append("<table width='100%' cellpadding='0' cellspacing='0' class='text' id='messagesAndErrors' name='messagesAndErrors'>\n");
		if(model.hasCommandErrors()){
			List errori = model.getCommandErrors();
			for(int i=0;i<errori.size();i++){
				CommandError ce = (CommandError)errori.get(i);
				res.append("<tr><td align='center' style='"+errStyle+"'>"+t.getProperty(ce)+"</td></tr>\n");
			}
		}
		if(model.hasCommandWarnings()){
			List warnings = model.getCommandWarnings();
			for(int i=0;i<warnings.size();i++){
				CommandWarning cw = (CommandWarning)warnings.get(i);
				res.append("<tr><td align='center' style='"+warStyle+"'>"+t.getProperty(cw)+"</td></tr>\n");
			}
		}
		if(model.hasCommandMessages()){
			List messages = model.getCommandMessages();
			for(int i=0;i<messages.size();i++){
				CommandMessage cm = (CommandMessage)messages.get(i);
				res.append("<tr><td align='center' style='"+msgStyle+"'>"+t.getProperty(cm)+"</td></tr>\n");
			}
		}
		res.append("</table>\n");
		return res.toString();
		
	}

}
