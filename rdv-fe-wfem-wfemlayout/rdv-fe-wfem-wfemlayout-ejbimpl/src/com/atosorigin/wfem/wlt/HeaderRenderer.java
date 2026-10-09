package com.atosorigin.wfem.wlt;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.controller.Configuration;
import com.atosorigin.wfem.layout.Template;

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public class HeaderRenderer{
	
	private PageRenderer pageRenderer;
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public HeaderRenderer(PageRenderer pageRenderer){
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
		String webapp = t.getWfemLayoutWebApp();
		
		result.append("<!-- WLT HEADER START -->\n");
		result.append("<!-- HOSTNAME APP: ["+Configuration.getInstance().getHostnameApp()+"] -->\n");

		String dynatraceSrc = Configuration.getInstance().getDynatraceSrc();
		if(dynatraceSrc != null && dynatraceSrc.length() > 0)
			result.append("<script type=\"text/javascript\" src=\""+dynatraceSrc+"\"></script>\n");
		
		String jqueryVersion = (String)pageRenderer.getTemplate().getVariables().get("jQueryVersion");
		
		// ***** CSS *****
		if(jqueryVersion != null)
			result.append("<link rel='stylesheet' type='text/css' href='"+webapp+"/private/jquery"+jqueryVersion+"/css/jquery-ui.css'/>\n");
		else
			result.append("<link rel='stylesheet' type='text/css' href='"+webapp+"/private/jquery/css/jquery-ui-1.8.2.azzurro.css'/>\n");
		
		result.append("<link rel='stylesheet' type='text/css' href='"+webapp+"/css/page.css'/>\n");
		result.append("<link rel='stylesheet' type='text/css' href='"+webapp+"/css/tab.css'/>\n");
		result.append("<link rel='stylesheet' type='text/css' href='"+webapp+"/css/field.css'/>\n");
		result.append("<link rel='stylesheet' type='text/css' href='"+webapp+"/css/grid.css'/>\n");
		
		result.append("<script>\n");
		result.append("var __isWlt=true;\n");
		result.append("function __getBrowserInstance(){return "+t.getBrowserInstance()+";}\n");
		if(Configuration.getInstance().isImageServerForWfemlayoutEnabled())
			result.append("var __jsWfemLayoutResourceServerUrl='"+Configuration.getInstance().getImageServerUrl()+"';\n");
		else
			result.append("var __jsWfemLayoutResourceServerUrl='';\n");
		if(Configuration.getInstance().isImageServerForWebApplEnabled(t.getOriginalWebApp()))
			result.append("var __jsResourceServerUrl='"+Configuration.getInstance().getImageServerUrl()+"';\n");
		else
			result.append("var __jsResourceServerUrl='';\n");
		result.append("var __jsWebApp='"+t.getOriginalWebApp()+"';\n");
		
		if(t.isFeatureIncluded(Template.FEATURE_UPLOAD)){
			result.append("var virusWarningFileTypeMsg =  \""+wt.getProperty("FileType.virusWarning")+"\";\n");
			result.append("var filenameErrorLoadFileTypeMsg = \""+wt.getProperty("FileType.filenameError")+"\";\n");
			result.append("var sizeErrorLoadFileTypeMsg = \""+wt.getProperty("FileType.sizeError")+"\";\n");
			result.append("var typeErrorLoadFileTypeMsg = \""+wt.getProperty("FileType.typeError")+"\";\n");
			result.append("var nameErrorLoadFileTypeMsg = \""+wt.getProperty("FileType.nameError")+"\";\n");
			result.append("var contentTypeErrorLoadFileTypeMsg = \""+wt.getProperty("FileType.contentTypeError")+"\";\n");
			result.append("var htmlDownaloadButtonForFileType = \"<input type='button' value='"+wt.getProperty("FileType.attach")+"' class='action' style='width:100%;'>\";\n");
			long uploadMaxSize = -1;
			CommandDataModel model = t.getPageDataModel();
			if(model != null)
				uploadMaxSize = model.getUploadMaxSize();
			if(uploadMaxSize <= 0)
				uploadMaxSize = 750*1024;
			result.append("var uploadMaxSize = "+uploadMaxSize+";\n");
		}
		result.append("</script>\n");

		result.append("<script src='"+webapp+"/scripts/Deprecated.js'></script>\n");
		result.append("<script src='"+webapp+"/scripts/Tools.js'></script>\n");
		result.append("<script src='"+webapp+"/scripts/HorzTab.js'></script>\n");
		result.append("<script src='"+webapp+"/scripts/VertTab.js'></script>\n");

		// jQuery+UI JS
		if(jqueryVersion != null){
			result.append("<script type='text/javascript' src='"+webapp+"/private/jquery"+jqueryVersion+"/jquery.min.js'></script>\n");
			result.append("<script type='text/javascript' src='"+webapp+"/private/jquery"+jqueryVersion+"/jquery-ui.min.js'></script>\n");
			result.append("<script type='text/javascript' src='"+webapp+"/private/jquery"+jqueryVersion+"/i18n/jquery.ui.datepicker-"+pageRenderer.getTemplate().getLangCode().toLowerCase()+".js'></script>\n");
		}else{
			result.append("<script type='text/javascript' src='"+webapp+"/private/jquery/jquery-1.4.2.min.js'></script>\n");
			result.append("<script type='text/javascript' src='"+webapp+"/private/jquery/jquery-ui-1.8.2.azzurro.min.js'></script>\n");
			result.append("<script type='text/javascript' src='"+webapp+"/private/jquery/i18n/jquery.ui.datepicker-"+pageRenderer.getTemplate().getLangCode().toLowerCase()+".js'></script>\n");
		}
		// *********
		
		result.append("<script src='"+webapp+"/private/Page.js'></script>\n");
		
		result.append("<script src='"+webapp+"/private/inputmask/JavaScriptUtil.js'></script>\n");
		result.append("<script src='"+webapp+"/private/inputmask/Parsers.js'></script>\n");
		result.append("<script src='"+webapp+"/private/inputmask/InputMask.js'></script>\n");
		
		result.append("<script src='"+webapp+"/private/Field.js'></script>\n");
		
		if(t.isFeatureIncluded(Template.FEATURE_GRIDS))
			result.append("<script src='"+webapp+"/private/Grid.js'></script>\n");
		
		if(t.isFeatureIncluded(Template.FEATURE_UPLOAD))
			result.append("<script src='"+webapp+"/private/File.js'></script>\n");
		
		result.append("<script src='"+webapp+"/private/HiddenSubmit.js'></script>\n");

		// Javascript intefrace objects
		result.append("<script>\n");
		result.append("var __pageIntf = new PageIntf('"+pageRenderer.getTemplate().getLangCode().toLowerCase()+"');\n");
		result.append("var __fieldIntf = new FieldIntf();\n");
		if(t.isFeatureIncluded(Template.FEATURE_GRIDS))
			result.append("var __gridIntf = new GridIntf();\n");
		if(t.isFeatureIncluded(Template.FEATURE_UPLOAD))
			result.append("var __fileIntf = new FileIntf();\n");
		result.append("var __hiddenSubmitIntf = new HiddenSubmitIntf();\n");
		
		if(t.isFeatureIncluded(Template.FEATURE_KEEPALIVE) && Configuration.getInstance().getKeepaliveTimerMinutes() > 0){
			// Keepalive minutes
			long keepAliveTimer = Configuration.getInstance().getKeepAliveTimerInMilliseconds();
			result.append("$( document ).ready(function() { setInterval( function() { try{var tm=new Date().getTime(); document.getElementById('__keepaliveIframe').src='call.wfem?wfemCmd=keepalive&BrowserInstance="+t.getBrowserInstance()+"&timenow='+tm;}catch(e){} }, "+keepAliveTimer+"); });\n");
		}
		
		UserSessionContext userSessionContext = t.getUserSessionContext();
		if(userSessionContext != null && userSessionContext.getUserRoles() != null && userSessionContext.getUserRoles().length() > 0){
			String[] rolesArr = userSessionContext.getUserRoles().split("\\|");
			String roles = "";
			boolean doInlineManualTrackingCall = false;
			for(int i=0;i<rolesArr.length;i++){
				if("monitoraggio_inlinemanual_s".equalsIgnoreCase(rolesArr[i]))
					doInlineManualTrackingCall=true;
				roles += "\""+rolesArr[i]+"\",";
			}
			if(roles.length() > 0)
				roles = roles.substring(0, roles.length()-1);
			if(doInlineManualTrackingCall){
				result.append("window.inlineManualTracking = {\n"+
																"uid: \""+userSessionContext.getUserCode()+"\",\n"+
																"roles: ["+roles+"]\n"+
																"};\n");
				result.append("try{\n");
				result.append("!function(){var e=document.createElement('script'),t=document.getElementsByTagName('script')[0];e.async=1,e.src='https://inlinemanual.com/embed/player.0576262734f53371791efd2647001813.js',e.charset='UTF-8',t.parentNode.insertBefore(e,t)}();\n");
				result.append("}catch(e){}");
			}
			
		}		
		result.append("</script>\n");
		
		result.append("<link id='__gatewayUrlGeneratorObject' rel='stylesheet' href='/prgm/mokeCssToHaveGatewayUrl.css' type='text/css'/>\n");
		
		result.append("<!-- WLT HEADER END -->\n");
		return result.toString();
		
	}

}
