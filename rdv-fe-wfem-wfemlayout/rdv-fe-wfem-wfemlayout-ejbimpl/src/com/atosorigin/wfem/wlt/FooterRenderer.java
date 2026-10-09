package com.atosorigin.wfem.wlt;

import com.atosorigin.wfem.controller.Configuration;
import com.atosorigin.wfem.layout.Template;

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public class FooterRenderer{
	
	private PageRenderer pageRenderer;
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public FooterRenderer(PageRenderer pageRenderer){
		this.pageRenderer = pageRenderer;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String html() {
		
		if(pageRenderer.getTemplate().getModality() == Template.PRINT_MODALITY)
			return "";
		
		StringBuffer result = new StringBuffer();
		Template t = pageRenderer.getTemplate();
		Template wt = pageRenderer.getFfTemplate();
		
		result.append("<!-- WLT FOOTER START -->\n");
		
		if(t.isFeatureIncluded(Template.FEATURE_KEEPALIVE) && Configuration.getInstance().getKeepaliveTimerMinutes() > 0)
			result.append("<iframe id='__keepaliveIframe' src='call.wfem?wfemCmd=keepalive&BrowserInstance="+t.getBrowserInstance()+"' style='display:none;'></iframe>\n");
		
		result.append("<iframe id='utilIFrame' name='utilIFrame' src='"+t.getWfemLayoutWebApp()+"/blankPage.html' scrolling='no' frameborder='0' style='position:absolute; top:0px; left:0px; display:none;'></iframe>\n");
		result.append("<div id='divwaitOpacityCover' class='divwaitOpacityCoverStyle' style='display:none;'></div>\n");
		result.append("<div name='divwait' id='divwait' class='divwaitStyle' style='display:none;'></div>\n");
		result.append("<script>__pageIntf.footerInitWait('"+wt.getProperty("waitString")+"');</script>\n");

		if(t.isFeatureIncluded(Template.FEATURE_FIELDS)){
			String skippableFields;
			try{
				skippableFields = t.getPageDataModel().getSkippableFields().toString();
			}catch (NullPointerException e){skippableFields = "";}
			
			result.append("<div name='divErrHelper' id='divErrHelper' style='display:none;background:white;position:absolute;z-index:999;'></div>\n");
			result.append("<div name='divWarHelper' id='divWarHelper' style='display:none;background:white;position:absolute;z-index:999;'></div>\n");
			result.append("<div name='divMsgHelper' id='divMsgHelper' style='display:none;background:white;position:absolute;z-index:999;'></div>\n");
			result.append("<script>__pageIntf.footerInitFields(\""+wt.getProperty("helper.errors.title")+"\","+
														    "\""+skippableFields+"\","+
														    "\""+wt.getProperty("helper.warnings.title")+"\","+
														    "\""+wt.getProperty("helper.warning.modify")+"\","+
														    "\""+wt.getProperty("helper.warning.ignore")+"\","+
														    "\""+wt.getProperty("helper.messages.title")+"\");</script>\n");
		}
		
		result.append("<div id='wltPopupContainer' style='display:none;width:150;height:150;margin:0;'>");
		result.append(	"<iframe id='wltPopupContainerIFrame' name='wltPopupContainerIFrame' src='"+t.getWfemLayoutWebApp()+"/blankPage.html' frameborder='0' width='99%' height='98%'></iframe>");
		result.append("</div>");
		
		result.append("<!-- WLT FOOTER END -->\n");
		return result.toString();
		
	}

}
