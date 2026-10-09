package com.atosorigin.wfem.layout;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.atosorigin.wfem.charts.ChartParameters;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandError;
import com.atosorigin.wfem.command.CommandMessage;
import com.atosorigin.wfem.command.CommandWarning;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.controller.Configuration;
import com.atosorigin.wfem.controller.ResourceSupplier;
import com.atosorigin.wfem.loggers.LayoutLogger;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.util.Tools;

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public class Template extends TemplatePropertiesReader implements java.io.Serializable{
	
	private boolean wlt = false;
	public static final String WLT_PAGE_FACTORY_CLASS_NAME = "com.atosorigin.wfem.wlt.PageRenderer";

	public static final int FEATURE_ALL = Integer.MAX_VALUE;
	public static final int FEATURE_AJAX = 2;
	public static final int FEATURE_HIDDEN_SUBMIT = 4;
	public static final int FEATURE_FIELDS = 8;
	public static final int FEATURE_GRIDS = 16;
	public static final int FEATURE_UPLOAD = 32;
	public static final int FEATURE_KEEPALIVE = 64;
	
	public static final int INSERT_MODALITY  = 1;
	public static final int UPDATE_MODALITY  = 2;
	public static final int READ_MODALITY    = 3;
	public static final int PRINT_MODALITY   = 4;

	public static final int NO_LABEL    = 0;
	public static final int LEFT_LABEL  = 1;
	public static final int UP_LABEL    = 2;
	public static final int RIGHT_LABEL = 3;
	public static final int DOWN_LABEL  = 4;	

	public static final int CENTER_ANCHOR 		= 1;
	public static final int TOP_LEFT_ANCHOR 	 	= 2;
	public static final int TOP_RIGHT_ANCHOR 	= 3;
	public static final int BOTTOM_LEFT_ANCHOR 	= 4;
	public static final int BOTTOM_RIGHT_ANCHOR	= 5;
	public static final int TOP_CENTER_ANCHOR 	= 6;
	public static final int RIGHT_CENTER_ANCHOR	= 7;
	public static final int LEFT_CENTER_ANCHOR 	= 8;
	public static final int BOTTOM_CENTER_ANCHOR	= 9;

	public static final String CONTROLLER_APPL_CODE = "WFEM";
	public static final String CONTROLLER_CALL = "call.wfem";
	public static final String CONTROLLER_CMD  = "wfemCmd";
	public static final String CONTROLLER_CALL_CMD = CONTROLLER_CALL+"?"+CONTROLLER_CMD;

	transient private static final String DEFAULT_APPL_CODE = "DEFAULT";
	transient private static LayoutLogger LOG = LayoutLogger.getInstance();

	private int includedFeatures = FEATURE_ALL & ~FEATURE_HIDDEN_SUBMIT;
	
	private LayoutFactory layoutFactory;
	
	private String  originalWebApp;
	private String  webApp;
	private String  langCode = "IT";
	private boolean templateCacheEnabled = true;
	private HttpServletRequest request = null;
	private CommandDataModel pageDataModel = null;
	private String labelWidth = null;
	private String fieldWidth = null;
	private Integer browserInstance = null;
	private int doubleScale = -1;

	private WLTPage wltff = null;
	private FieldsFactory ff = null;
	
	private boolean inPrintCommand = false;
	private boolean inGrid = false;
	private boolean noPadding = false;
	
	private transient HttpSession mainSession;
	private Map variables = new HashMap();
	private UserSessionContext userSessionContext = null;
	
	static class Patterns{
		private static final String paramPattern="\\s*=\\s*(['\"])(.*?)\\1";
		static final Pattern modality 			= Pattern.compile("modality"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern maxlength 			= Pattern.compile("maxlength"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern labelwidth 		= Pattern.compile("labelwidth"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern fieldwidth 		= Pattern.compile("fieldwidth"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern labelalign 		= Pattern.compile("labelalign"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern fieldalign 		= Pattern.compile("fieldalign"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern uppercase 			= Pattern.compile("uppercase"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern labelposition 		= Pattern.compile("labelposition"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern labelstyle 		= Pattern.compile("labelstyle"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern anchorposition 	= Pattern.compile("anchorposition"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern border 			= Pattern.compile("border"+paramPattern,Pattern.CASE_INSENSITIVE);	
		static final Pattern cols 				= Pattern.compile("cols"+paramPattern,Pattern.CASE_INSENSITIVE);	
		static final Pattern colswidths 		= Pattern.compile("colswidths"+paramPattern,Pattern.CASE_INSENSITIVE);	
		static final Pattern onexecute 			= Pattern.compile("onexecute"+paramPattern,Pattern.CASE_INSENSITIVE);	
		static final Pattern enabled 			= Pattern.compile("enabled"+paramPattern,Pattern.CASE_INSENSITIVE);	
		static final Pattern text 				= Pattern.compile("text"+paramPattern,Pattern.CASE_INSENSITIVE);	
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public Template(){
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public Template(String langCode, LayoutFactory layoutFactory, boolean templateCacheEnabled, 
					 HttpServletRequest request) {
		
		super();
	
		this.langCode = langCode;
		this.layoutFactory = layoutFactory;
		this.templateCacheEnabled = templateCacheEnabled;
		this.request = request;
		this.pageDataModel = null;
		
		initWebApp(request.getContextPath());
		
		setApplCode(DEFAULT_APPL_CODE);
	}		
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public Template(String langCode, LayoutFactory layoutFactory, boolean templateCacheEnabled, 
					 HttpServletRequest request, CommandDataModel pageDataModel) {
		
		super();
	
		this.langCode = langCode;
		this.layoutFactory = layoutFactory;
		this.templateCacheEnabled = templateCacheEnabled;
		this.request = request;
		this.pageDataModel = pageDataModel;

		initWebApp(request.getContextPath());
		
		setApplCode(DEFAULT_APPL_CODE);
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public Template(String langCode, LayoutFactory layoutFactory, boolean templateCacheEnabled, 
					 HttpServletRequest request, CommandDataModel pageDataModel, String webApp) {
		
		super();
	
		this.langCode = langCode;
		this.layoutFactory = layoutFactory;
		this.templateCacheEnabled = templateCacheEnabled;
		this.request = request;
		this.pageDataModel = pageDataModel;

		initWebApp(webApp);

		setApplCode(DEFAULT_APPL_CODE);
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private void initWebApp(String webApp){
		if(webApp != null)
			originalWebApp = webApp.toString();
		if(webApp.startsWith(Configuration.WFEM_LAYOUT_WEBAPP)){
			if(Configuration.getInstance().isImageServerForWfemlayoutEnabled())
				this.webApp = Configuration.getInstance().getImageServerUrl()+webApp;
			else
				this.webApp = webApp;
		}else{
			if(Configuration.getInstance().isImageServerForWebApplEnabled(webApp))
				this.webApp = Configuration.getInstance().getImageServerUrl()+webApp;
			else
				this.webApp = webApp;
		}
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void initFieldsFactory(HttpSession mainSession){
		
		this.mainSession = mainSession;
		
		try {
			Class defFieldsFactoryClass = Class.forName(Configuration.DEFAULT_FIELDS_FACTORY_CLASS_NAME);
			ff = (FieldsFactory)defFieldsFactoryClass.newInstance() ;
			ff.init(getWebApp(),"",getPageDataModel(),this);
			if(getPageDataModel() != null)
				ff.setModality(getPageDataModel().getModality());
		}catch(Exception e) {
			LOG.error(e);
		}
		
		try {
			Class wltFieldsFactoryClass = Class.forName(WLT_PAGE_FACTORY_CLASS_NAME);
			wltff = (WLTPage)wltFieldsFactoryClass.newInstance();
			wltff.init(this);
		}catch(ClassNotFoundException cnfe) {
			wltff = null;
		}catch(Exception e) {
			LOG.error(e);
			wltff = null;
		}
	}

	/***********************************************************************************************
	 * @deprecated
	 ***********************************************************************************************/
	public void setWebApp(String webApp) {
		try{
			String imageUrl = Configuration.getInstance().getImageServerUrl();
			if(imageUrl != null && imageUrl.length() > 0 && webApp.startsWith(imageUrl))
				webApp = webApp.substring(imageUrl.length());
		}catch(Exception e){}
		
		initWebApp(webApp);
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setIncludedFeatures(int features){
		includedFeatures = features;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public boolean isFeatureIncluded(int feature){
		if((includedFeatures & feature) != 0) 
			return true;
		return false;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getMessagesAndErrors(){
		return getMessagesAndErrors(null,null,null);
	}
	
	private static final String defaultErrStyle = "background-color:red;color:white;";
	private static final String defaultWarStyle = "background-color:khaki;";
	private static final String defaultMsgStyle = "background-color:lightcyan;";
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getMessagesAndErrors(String errStyle, String warStyle, String msgStyle){

		if(isWlt())
			return wltff.messagesAndErrors();
		
		if(errStyle == null || errStyle.length() == 0) errStyle = defaultErrStyle;
		if(warStyle == null || warStyle.length() == 0) warStyle = defaultWarStyle;
		if(msgStyle == null || msgStyle.length() == 0) msgStyle = defaultMsgStyle;
		
		StringBuffer res = new StringBuffer();
		CommandDataModel model = getPageDataModel();
		if(model == null)
			return res.toString();
		
		if(!model.hasCommandErrors() && !model.hasCommandMessages() && !model.hasCommandWarnings())
			return res.toString();
		
		res.append("<table width='100%' cellpadding='0' cellspacing='0' class='text' id='messagesAndErrors' name='messagesAndErrors'>\n");
		if(model.hasCommandErrors()){
			List errori = model.getCommandErrors();
			for(int i=0;i<errori.size();i++){
				CommandError ce = (CommandError)errori.get(i);
				res.append("<tr><td align='center' style='"+errStyle+"'>"+getProperty(ce)+"</td></tr>\n");
			}
		}
		if(model.hasCommandWarnings()){
			List warnings = model.getCommandWarnings();
			for(int i=0;i<warnings.size();i++){
				CommandWarning cw = (CommandWarning)warnings.get(i);
				res.append("<tr><td align='center' style='"+warStyle+"'>"+getProperty(cw)+"</td></tr>\n");
			}
		}
		if(model.hasCommandMessages()){
			List messages = model.getCommandMessages();
			for(int i=0;i<messages.size();i++){
				CommandMessage cm = (CommandMessage)messages.get(i);
				res.append("<tr><td align='center' style='"+msgStyle+"'>"+getProperty(cm)+"</td></tr>\n");
			}
		}
		res.append("</table>\n");
		return res.toString();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getHeader() {
		
		if(isWlt())
			return wltff.header();
		
		try {
			Class resourceSupplierClass = Class.forName(Configuration.DEFAULT_RESOURCE_SUPPLIER_CLASS_NAME);
			ResourceSupplier supplier = (ResourceSupplier)resourceSupplierClass.newInstance();
			supplier.init(langCode, templateCacheEnabled, request); 
			return supplier.getHeader(this);
		}
		catch(Exception e) {
			LOG.error(e);
			return "";
		}
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getFooter() {
		
		if(isWlt())
			return wltff.footer();
		
		try {
			Class resourceSupplierClass = Class.forName(Configuration.DEFAULT_RESOURCE_SUPPLIER_CLASS_NAME);
			ResourceSupplier supplier = (ResourceSupplier)resourceSupplierClass.newInstance();
			supplier.init(langCode, templateCacheEnabled, request); 
			return supplier.getFooter(this);
		}
		catch(Exception e) {
			LOG.error(e);
			return "";
		}
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getWfemLayoutWebApp(){
		
		if(isWlt())
			return Configuration.getInstance().getWfemlayoutWebApp()+"/wlt";
		
		return Configuration.getInstance().getWfemlayoutWebApp();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setApplCode(String applCode) {
		
		String applFile = "";
	
		if(langCode != null)
			applFile = "/" + applCode + "_" + langCode + ".properties";
		else
			applFile = "/" + applCode + ".properties";
	
		String upApplFile = applFile.toUpperCase();
			
		properties = (Properties)request.getSession().getAttribute(upApplFile);
		if(properties == null){
	
			try {
	
				properties = new Properties();
					
				if(layoutFactory == null){
					InputStream inputStream = this.getClass().getResourceAsStream(applFile);
					properties.load(inputStream);
					inputStream.close();
	
				}else{
					properties = layoutFactory.getTranslation(langCode,applCode);
				}
					
				if(templateCacheEnabled)
					request.getSession().setAttribute(upApplFile, properties);
					
				String msg = "";
				if(langCode != null)
					msg = "Set Application Code: Properties for language "+langCode+" and application "+applCode+" loaded";
				else
					msg = "Set Application Code: Properties for application "+applCode+" and no language loaded";
				LOG.info(msg);
				
			} catch ( Exception e ) {
				String errorMsg = "";
				if(langCode != null)
					errorMsg = "Set Application Code: Error loading properties for language "+langCode+" and application "+applCode;
				else
					errorMsg = "Set Application Code: Error loading properties for application "+applCode+" and no language";
				LOG.warning(errorMsg);
	
				// Save an empty properties object in session so
				// I don't try to load it every time
				properties = new Properties();
				request.getSession().setAttribute(upApplFile, properties);
			}
		}
		
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void loadPropertiesFromWebContext( ServletContext servletContext, String resourceName ) {
		
		String propertyFile = "";
	
		if ( !resourceName.startsWith("/") ) {
			resourceName = "/"+resourceName;
		}
		
		if (langCode != null )
			propertyFile = resourceName + "_" + langCode + ".properties";
		else
			propertyFile = resourceName + ".properties";
	
		properties = (Properties)request.getSession().getAttribute(propertyFile);
		if(properties == null){
	
			try {
	
				properties = new Properties();
					
				if(layoutFactory == null){
					InputStream inputStream = servletContext.getResourceAsStream(propertyFile);
					properties.load(inputStream);
					inputStream.close();
	
				}else{
					properties = layoutFactory.getTranslation(langCode,propertyFile);
				}
					
				if(templateCacheEnabled)
					request.getSession().setAttribute(propertyFile, properties);
					
				String msg = "";
				if(langCode != null)
					msg = "Load Properties From Web Context: Properties for language "+langCode+" and propertyFile "+propertyFile+" loaded";
				else
					msg = "Load Properties From Web Context: Properties for propertyFile "+propertyFile+" and no language loaded";
				LOG.info(msg);
				
			} catch ( Exception e ) {
				String errorMsg = "";
				if(langCode != null)
					errorMsg = "Load Properties From Web Context: Error loading properties for language "+langCode+" and propertyFile "+propertyFile;
				else
					errorMsg = "Load Properties From Web Context: Error loading properties for propertyFile "+propertyFile+" and no language";
				LOG.warning(errorMsg);
	
				// Save an empty properties object in session so
				// I don't try to load it every time
				properties = new Properties();
				request.getSession().setAttribute(propertyFile, properties);
			}
		}
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public CommandDataModel getPageDataModel() {
		return pageDataModel;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public CommandDataModel getCurrentDataModel() throws Exception{
		return (CommandDataModel)Tools.getPropertyValue(pageDataModel,getPrefix());
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public FieldsFactory getFieldsFactory(){
		return getFieldsFactory("");
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public FieldsFactory getFieldsFactory(String pageName){
		try {
			Class defFieldsFactoryClass = Class.forName(Configuration.DEFAULT_FIELDS_FACTORY_CLASS_NAME);
			ff = (FieldsFactory)defFieldsFactoryClass.newInstance() ;
			ff.init(getWebApp(),pageName,getPageDataModel(),this);
			return ff;
		}
		catch(Exception e) {
			LOG.error(e);
			return null;
		}
	}

	/* ************************************************************************************************
	 * SOLO GETTER
	 * ************************************************************************************************/

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getWebApp() {
		return webApp;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getOriginalWebApp() {
		return originalWebApp;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getLangCode() {
		return langCode;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public HttpServletRequest getRequest() {
		return request;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public boolean isTemplateCacheEnabled() {
		return templateCacheEnabled;
	}

	
	
	
	
	/* ************************************************************************************************
	 * MEMBRI DI TEMPLATE
	 * ************************************************************************************************/
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getFieldWidth() {
		return fieldWidth;
	}
	public void setFieldWidth(String fieldWidth) {
		this.fieldWidth = fieldWidth;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getLabelWidth() {
		return labelWidth;
	}
	public void setLabelWidth(String labelWidth) {
		this.labelWidth = labelWidth;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public boolean isInGrid() {
		return inGrid;
	}
	public void setInGrid(boolean inGrid) {
		this.inGrid = inGrid;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public boolean isNoPadding() {
		return noPadding;
	}
	public void setNoPadding(boolean noPadding) {
		this.noPadding = noPadding;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public boolean isInPrintCommand() {
		return inPrintCommand;
	}
	public void setInPrintCommand(boolean inPrintCommand) {
		this.inPrintCommand = inPrintCommand;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public Integer getBrowserInstance() {
		return browserInstance;
	}
	public void setBrowserInstance(Integer browserInstance) {
		this.browserInstance = browserInstance;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public int getDoubleScale() {
		return doubleScale;
	}
	public void setDoubleScale(int doubleScale) {
		this.doubleScale = doubleScale;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public boolean isWlt() {
		if(wltff == null)
			return false;
		return wlt;
	}
	public void setWlt(boolean wlt) {
		if(wlt)
			setIncludedFeatures(FEATURE_ALL & ~FEATURE_AJAX & ~FEATURE_UPLOAD);
		this.wlt = wlt;
	}


	
	
	
	/* ************************************************************************************************
	 * MEMBRI DI FIELDSFACTORY
	 * ************************************************************************************************/
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public int getModality() {
		return ff.getModality();
	}
	public void setModality(int modality) {
		ff.setModality(modality);
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setUpperCase(boolean upperCase) {
		ff.setUpperCase(upperCase);
	}
	public boolean getUpperCase() {
		return ff.getUpperCase();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setLabelPosition(int labelPosition) {
	    ff.setLabelPosition((double)labelPosition);
	}
	public int getLabelPosition() {
	    return (int)ff.getLabelPosition();
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setLabelAlign(String labelAlign) {
	    ff.setLabelAlign(labelAlign);
	}
	public String getLabelAlign() {
	    return ff.getLabelAlign();
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
    public void setLabelStyle(String labelStyle) {
        ff.setLabelStyle(labelStyle);
    }
    public String getLabelStyle() {
        return ff.getLabelStyle();
    }

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setPrefix(String prefix) {
		ff.setPrefix(prefix);
	}
	public void addPrefix(String prefix) {
		ff.addPrefix(prefix);
	}
	public String getPrefix() {
		return ff.getPrefix();
	}
	public void savePrefix(){
		ff.savePrefix();
	}
	public void restorePrefix(){
		ff.restorePrefix();
	}

    /**************************************************************************************************/
	/**************************************************************************************************/
	public void setBorder(String border) {
		ff.setBorder(border);
	}
	public String getBorder() {
		return ff.getBorder();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setPageName(String pageName) {
		ff.setPageName(pageName);
	}
	public String getPageName() {
		return ff.getPageName();
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getLabelCodePrefix() {
		return ff.getLabelCodePrefix();
	}
	public void setLabelCodePrefix(String labelCodePrefix) {
		ff.setLabelCodePrefix(labelCodePrefix);
	}

	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setFieldAlign(String fieldAlign) {
		ff.setFieldAlign(fieldAlign);
	}
	public String getFieldAlign() {
		return ff.getFieldAlign();
	}
    

	
	/* ************************************************************************************************
	 * CREAZIONE COMPONENTI HTML
	 * ************************************************************************************************/
	
    /**************************************************************************************************/
	/**************************************************************************************************/
	public String component(String componentType, String componentName){
		return component(componentType,componentName,"");
	}
	
    /**************************************************************************************************/
	/**************************************************************************************************/
	public String component(String componentType, String componentName, String parameters){
		if(isWlt())
			return wltff.component(componentType,componentName,parameters);
		return "";
	}
	
    
	/* ************************************************************************************************
	 * CREAZIONE CAMPI
	 * ************************************************************************************************/
	
    /**************************************************************************************************/
	/**************************************************************************************************/
	public String hidden(String propertyName){
		
		if(isWlt())
			return wltff.hidden(propertyName);
		
		return ff.hiddenField(propertyName);
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String inlineAnchor(String propertyName) {

		try{
			AbstractType propValue = (AbstractType)Tools.getPropertyValue(getPageDataModel(),propertyName);
			if(isWlt())
				return wltff.inlineAnchor(propertyName,propValue);
			return ff.getInlineProblemsHelperAnchor(propertyName,propValue,TOP_RIGHT_ANCHOR);
		}catch(Exception e){
			LOG.error(e);
			return "";
		}
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String inlineMsgAnchor(String propertyName) {

		try{
			AbstractType propValue = (AbstractType)Tools.getPropertyValue(getPageDataModel(),propertyName);
			if(isWlt())
				return wltff.inlineMsgAnchor(propertyName,propValue);
			return "";
		}catch(Exception e){
			LOG.error(e);
			return "";
		}
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String field(String propertyName) {
		return field(propertyName,"");
	}
			
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String field(String propertyName, String parameters) {
		
		if(isWlt())
			return wltff.field(propertyName,parameters);

		Matcher mat=null;
		String res = "";
		
		try{
			if(parameters == null)
				parameters = "";
			parameters += " ";

			int localModality = ff.getModality();
			mat = Patterns.modality.matcher(parameters);
			if(mat.find()){
				String exp = mat.group(2);
				if(exp.equalsIgnoreCase("insert"))
					localModality = INSERT_MODALITY;
				else if(exp.equalsIgnoreCase("update"))
					localModality = UPDATE_MODALITY;
				else if(exp.equalsIgnoreCase("read"))
					localModality = READ_MODALITY;
				parameters = parameters.substring(0, mat.start()) + parameters.substring(mat.end());
			}
	
			long maxLength = 0;
			mat = Patterns.maxlength.matcher(parameters);
			if(mat.find()){
				String exp = mat.group(2);
				maxLength = Long.parseLong(exp);
				parameters = parameters.substring(0, mat.start()) + parameters.substring(mat.end());
			}
	
			String locLabelWidth = getLabelWidth();
			mat = Patterns.labelwidth.matcher(parameters);
			if(mat.find()){
				String exp = mat.group(2);
				locLabelWidth = exp;
				parameters = parameters.substring(0, mat.start()) + parameters.substring(mat.end());
			}
	
			String locFieldWidth = getFieldWidth();
			mat = Patterns.fieldwidth.matcher(parameters);
			if(mat.find()){
				String exp = mat.group(2);
				locFieldWidth = exp;
				parameters = parameters.substring(0, mat.start()) + parameters.substring(mat.end());
			}
	
			String locLabelAlign = ff.getLabelAlign();
			mat = Patterns.labelalign.matcher(parameters);
			if(mat.find()){
				String exp = mat.group(2);
				locLabelAlign = exp;
				parameters = parameters.substring(0, mat.start()) + parameters.substring(mat.end());
			}
	
			String locFieldAlign = ff.getFieldAlign();
			mat = Patterns.fieldalign.matcher(parameters);
			if(mat.find()){
				String exp = mat.group(2);
				locFieldAlign = exp;
				parameters = parameters.substring(0, mat.start()) + parameters.substring(mat.end());
			}
	
			boolean locUpperCase = ff.getUpperCase();
			mat = Patterns.uppercase.matcher(parameters);
			if(mat.find()){
				String exp = mat.group(2);
				if(exp.equalsIgnoreCase("false"))
				    locUpperCase = false;
				else
				    locUpperCase = true;
				parameters = parameters.substring(0, mat.start()) + parameters.substring(mat.end());
			}
	
			int locLabelPosition = (int)ff.getLabelPosition();
			mat = Patterns.labelposition.matcher(parameters);
			if(mat.find()){
				String exp = mat.group(2);
				if(exp.equalsIgnoreCase("nolabel"))
				    locLabelPosition = NO_LABEL;
				else if(exp.equalsIgnoreCase("left"))
				    locLabelPosition = LEFT_LABEL;
				else if(exp.equalsIgnoreCase("up"))
				    locLabelPosition = UP_LABEL;
				else if(exp.equalsIgnoreCase("right"))
				    locLabelPosition = RIGHT_LABEL;
				else if(exp.equalsIgnoreCase("down"))
				    locLabelPosition = DOWN_LABEL;
				parameters = parameters.substring(0, mat.start()) + parameters.substring(mat.end());
			}
	
			String locLabelStyle = ff.getLabelStyle();
			mat = Patterns.labelstyle.matcher(parameters);
			if(mat.find()){
				String exp = mat.group(2);
				locLabelStyle = exp;
				parameters = parameters.substring(0, mat.start()) + parameters.substring(mat.end());
			}
	
			int locAnchorPosition = ff.getAnchorPosition();
			mat = Patterns.anchorposition.matcher(parameters);
			if(mat.find()){
				String exp = mat.group(2);
				if(exp.equalsIgnoreCase("center"))
				    locAnchorPosition = CENTER_ANCHOR;
				else if(exp.equalsIgnoreCase("topleft"))
				    locAnchorPosition = TOP_LEFT_ANCHOR;
				else if(exp.equalsIgnoreCase("topright"))
				    locAnchorPosition = TOP_RIGHT_ANCHOR;
				else if(exp.equalsIgnoreCase("bottomleft"))
				    locAnchorPosition = BOTTOM_LEFT_ANCHOR;
				else if(exp.equalsIgnoreCase("bottomright"))
				    locAnchorPosition = BOTTOM_RIGHT_ANCHOR;
				else if(exp.equalsIgnoreCase("topcenter"))
				    locAnchorPosition = TOP_CENTER_ANCHOR;
				else if(exp.equalsIgnoreCase("rightcenter"))
				    locAnchorPosition = RIGHT_CENTER_ANCHOR;
				else if(exp.equalsIgnoreCase("leftcenter"))
				    locAnchorPosition = LEFT_CENTER_ANCHOR;
				else if(exp.equalsIgnoreCase("bottomcenter"))
				    locAnchorPosition = BOTTOM_CENTER_ANCHOR;
				parameters = parameters.substring(0, mat.start()) + parameters.substring(mat.end());
			}
	
			String locBorder = ff.getBorder();
			mat = Patterns.border.matcher(parameters);
			if(mat.find()){
				String exp = mat.group(2);
				locBorder = exp;
				parameters = parameters.substring(0, mat.start()) + parameters.substring(mat.end());
			}
			
			if(getDoubleScale() >= 0 && parameters.indexOf(" doublescale=") < 0)
				parameters += " doublescale='"+getDoubleScale()+"' ";

			String ffLabelAlign = ff.getLabelAlign();
			String ffFieldAlign = ff.getFieldAlign();
			boolean ffUpperCase = ff.getUpperCase();
			int ffLabelPosition = (int)ff.getLabelPosition();
			String ffLabelStyle = ff.getLabelStyle();
			int ffAnchorPosition  = ff.getAnchorPosition();
			String ffBorder = ff.getBorder();
	
			ff.setLabelAlign(locLabelAlign);
			ff.setFieldAlign(locFieldAlign);
			ff.setUpperCase(locUpperCase);
			ff.setLabelPosition((double)locLabelPosition);
			ff.setLabelStyle(locLabelStyle);
			ff.setAnchorPosition(locAnchorPosition);
			ff.setBorder(locBorder);
	
		    res = ff.field(propertyName,localModality,maxLength,parameters,locLabelWidth,locFieldWidth);
		    
			ff.setLabelAlign(ffLabelAlign);
			ff.setFieldAlign(ffFieldAlign);
			ff.setUpperCase(ffUpperCase);
			ff.setLabelPosition((double)ffLabelPosition);
			ff.setLabelStyle(ffLabelStyle);
			ff.setAnchorPosition(ffAnchorPosition);
			ff.setBorder(ffBorder);
			
		}catch(Exception e){
			res = "<table><tr><td>"+e.toString()+"</td></tr></table>";
		}finally{
			return res;
		}
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String grid(String propertyName) {
		return grid(propertyName,"");
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String grid(String propertyName, String parameters) {		
		
		if(isWlt())
			return wltff.grid(propertyName,parameters);
		
		return ff.gridField(propertyName,parameters);
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String action(String actionName){
		String methodName = "";
		methodName += actionName.substring(0,1).toUpperCase();
		methodName += actionName.substring(1);
		return action(actionName, "onexecute='do"+methodName+"();'");
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String action(String actionName, String parameters){

		if(isWlt())
			return wltff.action(actionName,parameters);

		Matcher mat=null;
		String res = "";

		try{
			if(parameters == null)
				parameters = "";
			parameters += " ";

			String onExecute = "";
			mat = Patterns.onexecute.matcher(parameters);
			if(mat.find()){
				String exp = mat.group(2);
				onExecute = exp;
				parameters = parameters.substring(0, mat.start()) + parameters.substring(mat.end());
			}else{
				onExecute += "do"+actionName.substring(0,1).toUpperCase()+actionName.substring(1);
			}

			boolean enabled = true;
			mat = Patterns.enabled.matcher(parameters);
			if(mat.find()){
				String exp = mat.group(2);
				if(exp.equalsIgnoreCase("false"))
				    enabled = false;
				else
				    enabled = true;
				parameters = parameters.substring(0, mat.start()) + parameters.substring(mat.end());
			}

			String text = null;
			if(getLabelCodePrefix() != null && !getLabelCodePrefix().equals(""))
				text = getProperty(getLabelCodePrefix()+actionName);
			else
				text = getProperty(ff.getPageName()+actionName);

			mat = Patterns.text.matcher(parameters);
			if(mat.find()){
				String exp = mat.group(2);
				text = exp;
				parameters = parameters.substring(0, mat.start()) + parameters.substring(mat.end());
			}

			res = ff.action(actionName,onExecute,parameters,enabled,text);

		}catch(Exception e){
			res = "<table><tr><td>"+e.toString()+"</td></tr></table>";
		}finally{
			return res;
		}
		
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String chart(String chartType, String parameters) {
		ChartParameters chartParameters = new ChartParameters(chartType,parameters);
		return chart(chartParameters);
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String chart(ChartParameters chartParameters) {
		String chartID = chartParameters.getID();
		mainSession.setAttribute(chartID,chartParameters);
		
		StringBuffer result = new StringBuffer(); 
		result.append("<table><tr><td align='center'>");
		result.append("<img src=\"call.wfem?wfemCmd=getChart&chartID="+chartID+"\">");
		result.append("</td></tr></table>");
		return result.toString();
	}

	
	
	
	
	
	
	
	
	
	/* ************************************************************************************************
	 * DA DISMETTERE
	 * ************************************************************************************************/
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String anchor(String anchorName, String propertyName) {
		if(isWlt())
			return "<script>alert('template.anchor is deprecated');</script>";
		return ff.getProblemsHelperAnchor(anchorName,propertyName);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String anchor(String anchorName, String propertyName, int anchorPosition) {
		if(isWlt())
			return "<script>alert('template.anchor is deprecated');</script>";
		return ff.getProblemsHelperAnchor(anchorName,propertyName,anchorPosition);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String table(String propertyName) {
		if(isWlt())
			return "<script>alert('template.table is deprecated');</script>";
		return table( propertyName,"");
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String table(String propertyName, String parameters) {
		if(isWlt())
			return "<script>alert('template.table is deprecated');</script>";
		
		Matcher mat=null;
		String res = "";
		
		try{
			if(parameters == null)
				parameters = "";
			parameters += " ";

			String[] cols = null;
			mat = Patterns.cols.matcher(parameters);
			if(mat.find()){
				String exp = mat.group(2);
				Vector vtmp = new Vector();
				StringTokenizer st = new StringTokenizer(exp,",");
				while(st.hasMoreTokens()){
					vtmp.add(st.nextToken());
				}
				cols = (String[])vtmp.toArray(new String[0]);
				parameters = parameters.substring(0, mat.start()) + parameters.substring(mat.end());
			}
			
			String[] colsWidths = null;
			mat = Patterns.colswidths.matcher(parameters);
			if(mat.find()){
				String exp = mat.group(2);
				Vector vtmp = new Vector();
				StringTokenizer st = new StringTokenizer(exp,",");
				while(st.hasMoreTokens()){
					vtmp.add(st.nextToken());
				}
				colsWidths = (String[])vtmp.toArray(new String[0]);
				parameters = parameters.substring(0, mat.start()) + parameters.substring(mat.end());
			}
			
			res = ff.tableField(propertyName,cols,colsWidths,parameters);
			
		}catch(Exception e){
			res = "<table><tr><td>"+e.toString()+"</td></tr></table>";
		}finally{
			return res;
		}
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getProblemsHelperAnchor(String anchorName, String propName) {
		if(isWlt())
			return "<script>alert('template.getProblemsHelperAnchor is deprecated');</script>";
		return ff.getProblemsHelperAnchor(anchorName,propName);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getProblemsHelperAnchor(String propName, AbstractType propValue, int anchorPosition) {
		if(isWlt())
			return "<script>alert('template.getProblemsHelperAnchor is deprecated');</script>";
		return ff.getProblemsHelperAnchor(propName,propValue,anchorPosition);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getProblemsHelperAnchor(String anchorName, String propName, int anchorPosition) {
		if(isWlt())
			return "<script>alert('template.getProblemsHelperAnchor is deprecated');</script>";
		return ff.getProblemsHelperAnchor(anchorName,propName,anchorPosition);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getProblemsHelperAnchor(String anchorName, String propName, 
													 AbstractType propValue, int anchorPosition) {
		if(isWlt())
			return "<script>alert('template.getProblemsHelperAnchor is deprecated');</script>";
		return ff.getProblemsHelperAnchor(anchorName,propName,propValue,anchorPosition);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setAnchorPosition(int anchorPosition) {
		ff.setAnchorPosition(anchorPosition);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setJSCombo(boolean jsCombo) {
	    ff.setJSCombo(jsCombo);
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public boolean getJSCombo() {
	    return ff.getJSCombo();
	}

    /**************************************************************************************************/
	/**************************************************************************************************/
    public String getComboElementsString(String propertyName){
        return ff.getComboElementsString(propertyName);
    }

	public Map getVariables() {
		return variables;
	}

	public void setVariables(Map variables) {
		this.variables = variables;
	}
	
	public UserSessionContext getUserSessionContext() {
		return userSessionContext;
	}

	public void setUserSessionContext(UserSessionContext userSessionContext) {
		this.userSessionContext = userSessionContext;
	}

}
