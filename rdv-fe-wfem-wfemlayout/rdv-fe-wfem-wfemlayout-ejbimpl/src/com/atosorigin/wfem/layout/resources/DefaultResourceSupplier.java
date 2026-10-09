package com.atosorigin.wfem.layout.resources;

import java.io.BufferedInputStream;
import java.io.File;
import java.net.URL;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.controller.Configuration;
import com.atosorigin.wfem.controller.RequestManager;
import com.atosorigin.wfem.controller.ResourceSupplier;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.loggers.AbstractLogger;
import com.atosorigin.wfem.loggers.LayoutLogger;

/**************************************************************************************************/
/**************************************************************************************************/
public class DefaultResourceSupplier implements ResourceSupplier{

	private static AbstractLogger LOG = LayoutLogger.getInstance();

	private static final String cssFileName 		= "style.css";
	private static final String blankPageFileName   = "blankPage.html";

	private static final String imagesRootName =  "images/";
	private static final String scriptsRootName = "scripts/";

	private Template template;
	private HttpServlet servlet;
	private HttpServletRequest request;
	
	static class Patterns{
		static final Pattern property	 			= Pattern.compile("<%=\\s*property\\s*:\\s*(.*?)\\s*%>",Pattern.CASE_INSENSITIVE);
		static final Pattern getSkippableFields	 	= Pattern.compile("<%=\\s*getSkippableFields\\s*%>",Pattern.CASE_INSENSITIVE);
		static final Pattern getTraceLevel	 		= Pattern.compile("<%=\\s*getTraceLevel\\s*%>",Pattern.CASE_INSENSITIVE);
		static final Pattern wfemLayoutWebapp		= Pattern.compile("<%=\\s*wfemLayoutWebapp\\s*%>",Pattern.CASE_INSENSITIVE);
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	/**************            Dynamic resource management     ****************************************/
	/**************************************************************************************************/
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void init(String langCode, boolean templateCacheEnabled,	HttpServletRequest request) {
		this.request = request;
		setTemplate(new Template(langCode, null, templateCacheEnabled, request));
		getTemplate().setApplCode(Template.CONTROLLER_APPL_CODE);
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getHeader(Template pageTemplate) {
		StringBuffer result = new StringBuffer();
		Template wt = getTemplate();
		
		result.append("<!-- WFEM HEADER START -->\n");
		result.append("<!-- HOSTNAME APP: ["+Configuration.getInstance().getHostnameApp()+"] -->\n");
		
		result.append("<link rel='stylesheet' type='text/css' href='"+Configuration.getInstance().getWfemlayoutWebApp()+"/style.css'>\n");

		result.append("<script>\n");
		result.append("var __isWlt=false;\n");
		result.append("function __getBrowserInstance(){return "+pageTemplate.getBrowserInstance()+";}\n");
		if(Configuration.getInstance().isImageServerForWfemlayoutEnabled())
			result.append("var __jsWfemLayoutResourceServerUrl='"+Configuration.getInstance().getImageServerUrl()+"';\n");
		else
			result.append("var __jsWfemLayoutResourceServerUrl='';\n");
		
		if(Configuration.getInstance().isImageServerForWebApplEnabled(pageTemplate.getOriginalWebApp()))
			result.append("var __jsResourceServerUrl='"+Configuration.getInstance().getImageServerUrl()+"';\n");
		else
			result.append("var __jsResourceServerUrl='';\n");
		result.append("var __jsWebApp='"+pageTemplate.getOriginalWebApp()+"';\n");
		result.append("</script>\n");
		
		
		if(pageTemplate.isFeatureIncluded(Template.FEATURE_UPLOAD)){
			result.append("<script>\n");
			result.append("var virusWarningFileTypeMsg =  \""+wt.getProperty("FileType.virusWarning")+"\";\n");
			result.append("var filenameErrorLoadFileTypeMsg = \""+wt.getProperty("FileType.filenameError")+"\";\n");
			result.append("var sizeErrorLoadFileTypeMsg = \""+wt.getProperty("FileType.sizeError")+"\";\n");
			result.append("var typeErrorLoadFileTypeMsg = \""+wt.getProperty("FileType.typeError")+"\";\n");
			result.append("var nameErrorLoadFileTypeMsg = \""+wt.getProperty("FileType.nameError")+"\";\n");
			result.append("var contentTypeErrorLoadFileTypeMsg = \""+wt.getProperty("FileType.contentTypeError")+"\";\n");
			result.append("var htmlDownaloadButtonForFileType = \"<input type='button' value='"+wt.getProperty("FileType.attach")+"' class='action' style='width:100%;'>\";\n");
			long uploadMaxSize = -1;
			CommandDataModel model = pageTemplate.getPageDataModel();
			if(model != null)
				uploadMaxSize = model.getUploadMaxSize();
			if(uploadMaxSize <= 0)
				uploadMaxSize = 750*1024;
			result.append("var uploadMaxSize = "+uploadMaxSize+";\n");
			result.append("</script>\n");
		}
		
		if(pageTemplate.isFeatureIncluded(Template.FEATURE_FIELDS)){
			result.append("<script>\n");
			result.append("var calToday = \""+wt.getProperty("calendar.today")+"\";\n");
			result.append("var calMonth=new Array('"+wt.getProperty("calendar.month0")+"',"+
												 "'"+wt.getProperty("calendar.month1")+"',"+
												 "'"+wt.getProperty("calendar.month2")+"',"+
												 "'"+wt.getProperty("calendar.month3")+"',"+
												 "'"+wt.getProperty("calendar.month4")+"',"+
												 "'"+wt.getProperty("calendar.month5")+"',"+
												 "'"+wt.getProperty("calendar.month6")+"',"+
												 "'"+wt.getProperty("calendar.month7")+"',"+
												 "'"+wt.getProperty("calendar.month8")+"',"+
												 "'"+wt.getProperty("calendar.month9")+"',"+
												 "'"+wt.getProperty("calendar.month10")+"',"+
												 "'"+wt.getProperty("calendar.month11")+"');\n");
			result.append("var calDay=new Array( '"+wt.getProperty("calendar.day0")+"',"+
												"'"+wt.getProperty("calendar.day1")+"',"+
												"'"+wt.getProperty("calendar.day2")+"',"+
												"'"+wt.getProperty("calendar.day3")+"',"+
												"'"+wt.getProperty("calendar.day4")+"',"+
												"'"+wt.getProperty("calendar.day5")+"',"+
												"'"+wt.getProperty("calendar.day6")+"');\n");
			result.append("</script>\n");
			result.append("<script src='"+Configuration.getInstance().getWfemlayoutWebApp()+"/scripts/AnchorPosition.js'></script>\n");
			result.append("<script src='"+Configuration.getInstance().getWfemlayoutWebApp()+"/scripts/PopupWindow.js'></script>\n");
			result.append("<script src='"+Configuration.getInstance().getWfemlayoutWebApp()+"/scripts/date.js'></script>\n");
			result.append("<script src='"+Configuration.getInstance().getWfemlayoutWebApp()+"/scripts/CalendarPopup.js'></script>\n");
			result.append("<script>document.write(CalendarPopup_getStyles());</script>\n");
		}
		
		result.append("<script src='"+Configuration.getInstance().getWfemlayoutWebApp()+"/scripts/script.js'></script>\n");
		
		if(pageTemplate.isFeatureIncluded(Template.FEATURE_AJAX)){
			result.append("<script src='"+Configuration.getInstance().getWfemlayoutWebApp()+"/scripts/prototype.js'></script>\n");
			result.append("<script src='"+Configuration.getInstance().getWfemlayoutWebApp()+"/scripts/wfemAjax.js'></script>\n");
		}
		
		if(pageTemplate.isFeatureIncluded(Template.FEATURE_FIELDS)){
			result.append("<script src='"+Configuration.getInstance().getWfemlayoutWebApp()+"/scripts/numberFormat.js'></script>\n");
			result.append("<script src='"+Configuration.getInstance().getWfemlayoutWebApp()+"/scripts/fieldFormat.js'></script>\n");
		}
		
		result.append("<script src='"+Configuration.getInstance().getWfemlayoutWebApp()+"/scripts/JSComboBox.js'></script>\n");
		result.append("<script src='"+Configuration.getInstance().getWfemlayoutWebApp()+"/scripts/JSTableList.js'></script>\n");
		result.append("<script src='"+Configuration.getInstance().getWfemlayoutWebApp()+"/scripts/JSTabbedPane.js'></script>\n");
		
		if(pageTemplate.isFeatureIncluded(Template.FEATURE_GRIDS))
			result.append("<script src='"+Configuration.getInstance().getWfemlayoutWebApp()+"/scripts/JSGridList.js'></script>\n");
		
		if(pageTemplate.isFeatureIncluded(Template.FEATURE_UPLOAD))
			result.append("<script src='"+Configuration.getInstance().getWfemlayoutWebApp()+"/scripts/FileType.js'></script>\n");
		
		if(pageTemplate.isFeatureIncluded(Template.FEATURE_HIDDEN_SUBMIT))
			result.append("<script src='"+Configuration.getInstance().getWfemlayoutWebApp()+"/scripts/HiddenSubmit.js'></script>\n");

		result.append("<link id='__gatewayUrlGeneratorObject' rel='stylesheet' type='text/css' href='/prgm/mokeCssToHaveGatewayUrl.css'></link>\n");
		
		result.append("<!-- WFEM HEADER END -->\n");
		return result.toString();
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getFooter(Template pageTemplate) {

		StringBuffer result = new StringBuffer();
		Template wt = getTemplate();
		
		result.append("<!-- WFEM FOOTER START -->\n");
		
		if(pageTemplate.isFeatureIncluded(Template.FEATURE_FIELDS)){
			result.append("<div id='calendarDiv' style='position:absolute;visibility:hidden;background-color:#ECF0F2;'></div>\n");
			result.append("<script>wfemFooterInitCalendar();</script>\n");
		}

		result.append("<iframe id='DivViewPort' src='"+Configuration.getInstance().getWfemlayoutWebApp()+"/blankPage.html' scrolling='no' frameborder='0' style='position:absolute; top:0px; left:0px; display:none;'></iframe>\n");

		result.append("<div id='divwaitOpacityCover' class='divwaitOpacityCoverStyle' style='display:none;'></div>\n");
		result.append("<div name='divwait' id='divwait' style='display:none;position:absolute;top:0;left:0;z-index:1001;'></div>\n");
		result.append("<script>wfemFooterInitWait('"+wt.getProperty("waitString")+"');</script>\n");

		if(pageTemplate.isFeatureIncluded(Template.FEATURE_FIELDS)){
			String skippableFields;
			try{
				skippableFields = pageTemplate.getPageDataModel().getSkippableFields().toString();
			}catch (NullPointerException e){skippableFields = "";}
			
			result.append("<div name='divErrHelper' id='divErrHelper' style='display:none;background:white;position:absolute;z-index:999;'></div>\n");
			result.append("<div name='divWarHelper' id='divWarHelper' style='display:none;background:white;position:absolute;z-index:999;'></div>\n");
			result.append("<div name='divMsgHelper' id='divMsgHelper' style='display:none;background:white;position:absolute;z-index:999;'></div>\n");
			result.append("<script>wfemFooterInitFields(\""+wt.getProperty("helper.errors.title")+"\","+
													   "\""+skippableFields+"\","+
													   "\""+wt.getProperty("helper.warnings.title")+"\","+
													   "\""+wt.getProperty("helper.warning.modify")+"\","+
													   "\""+wt.getProperty("helper.warning.ignore")+"\","+
													   "\""+wt.getProperty("helper.messages.title")+"\");</script>\n");
		}

		result.append("<script>wfemFooterEnd(\""+wt.getProperty("status.normal")+"\");</script>\n");
		
		if(pageTemplate.isFeatureIncluded(Template.FEATURE_HIDDEN_SUBMIT))
			result.append("<div id='wfemHiddenSubmitIframe' name='wfemHiddenSubmitIframe' style='display:none;'></div>\n");
		
		if(pageTemplate.getPageDataModel() != null && pageTemplate.getPageDataModel().getModality() != Template.READ_MODALITY)
			result.append("<script>showHelpersAnchor();</script>\n");
		
		result.append("<!-- WFEM FOOTER END -->\n");
		return result.toString();
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	/**************            Static resource management     *****************************************/
	/**************************************************************************************************/
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void init(String langCode, HttpServlet servlet, RequestManager requestManager) {
		this.servlet = servlet;
		this.request = requestManager.getRequest();
		setTemplate(new Template(langCode, null, true, request));
		getTemplate().setApplCode(Template.CONTROLLER_APPL_CODE);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public boolean checkHeader(HttpServletRequest request, HttpServletResponse response, long date) throws Exception{

		long ifModifiedSince = -1;
		try{
			ifModifiedSince = request.getDateHeader("If-Modified-Since");
		}catch(Exception e){
			ifModifiedSince = -1;
		}
		
		if(ifModifiedSince != -1) {						
            if(date <= ifModifiedSince + 1000) {
				response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
				return false;
            }
		}
		return true;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getScript(String scriptFileName) {
		return getResourceAsString(scriptsRootName + scriptFileName);
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public long getScriptDate(String scriptFileName) {
		return getResourceFileDate(scriptsRootName + scriptFileName);
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getCss() {
		return getResourceAsString(cssFileName);
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public long getCssDate() {
		return getResourceFileDate(cssFileName);
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public byte[] getImage(String imgFileName) {
		return getResourceFile(imagesRootName + imgFileName);
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public long getImageDate(String imgFileName) {
		return getResourceFileDate(imagesRootName + imgFileName);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getBlankPage() {
		return getResourceAsString(blankPageFileName);
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public long getBlankPageDate() {
		return getResourceFileDate(blankPageFileName);
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getResource(String fileName) {
		String out = getResourceAsString(fileName);
		out = parseProperty(out);
		out = parseTraceLevel(out);
		return out;
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public long getResourceDate(String fileName) {
		return getResourceFileDate(fileName);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String parseProperty(String in) {
		Matcher mat = Patterns.property.matcher(in);
		while(mat.find()){
			String token = mat.group();
			String propertyKey = mat.group(1);
			String propertyValue = getTemplate().getProperty(propertyKey);
			in = in.replaceAll(token,propertyValue);
		}
		mat = Patterns.wfemLayoutWebapp.matcher(in);
		while(mat.find()){
			String token = mat.group();
			in = in.replaceAll(token,Configuration.getInstance().getWfemlayoutWebApp());
		}
		return in;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private String parseTraceLevel(String in) {
		String traceLevel = ""+Configuration.getInstance().getLayoutTraceLevel();

		Matcher mat = Patterns.getTraceLevel.matcher(in);
		if(mat.find())
			in = in.substring(0,mat.start())+traceLevel+in.substring(mat.end());
		return in;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private long getResourceFileDate(String fileName) {

		try {
			
			Long lastModifiedFile = (Long)request.getSession().getAttribute("lastModified:"+fileName);
			if(lastModifiedFile != null){
				return lastModifiedFile.longValue();
			}

			ServletContext context = servlet.getServletContext().getContext(Configuration.WFEM_LAYOUT_WEBAPP);
			URL url = context.getResource("/"+fileName);
			File file = new File(url.getFile());
			// In Jaguar la lastModified torna 0 !!!! (Forse perchè il file è in un jar)
			long lastModified = file.lastModified();
			if(lastModified == 0){
				lastModified = Calendar.getInstance().getTime().getTime();
				request.getSession().setAttribute("lastModified:"+fileName,new Long(lastModified));
			}
			return lastModified;
			
		} catch (Exception e) {
			String errorMsg = this.getClass().getName() + ": Error loading " + fileName;
			LOG.warning(errorMsg);
			return -1;
		}
	}


	/**************************************************************************************************/
	/**************************************************************************************************/
	private byte[] getResourceFile(String fileName) {

		try {
			byte[] data;

			ServletContext context = servlet.getServletContext().getContext(Configuration.WFEM_LAYOUT_WEBAPP);
			BufferedInputStream in = new BufferedInputStream(context.getResourceAsStream("/"+fileName));

			data = new byte[in.available()];
			in.read(data, 0, data.length);	
			in.close();

			return data;

		} catch (Exception e) {
			String errorMsg = this.getClass().getName() + ": Error loading " + fileName;
			LOG.warning(errorMsg);
			return null;
		}
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getResourceAsString(String fileName) {

		byte[] data = getResourceFile(fileName);
		if (data == null) 
			return "";
		
		return new String(data);
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public Template getTemplate() {
		return template;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setTemplate(Template template) {
		this.template = template;
	}

}
