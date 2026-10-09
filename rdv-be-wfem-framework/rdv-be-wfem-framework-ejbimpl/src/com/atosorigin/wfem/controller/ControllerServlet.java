package com.atosorigin.wfem.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.UserTransaction;

import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.owasp.esapi.errors.IntrusionException;

import com.atosorigin.wfem.applicationmanualloader.ApplicationManualLoader;
import com.atosorigin.wfem.backend.UserContextDataLoader;
import com.atosorigin.wfem.bo.BoInteraction;
import com.atosorigin.wfem.bo.BoParameter;
import com.atosorigin.wfem.bo.BoParameters;
import com.atosorigin.wfem.bo.BoReportInfo;
import com.atosorigin.wfem.charts.AbstractChart;
import com.atosorigin.wfem.charts.ChartParameters;
import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.CalcCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.DisplayCommand;
import com.atosorigin.wfem.command.DownloadCommand;
import com.atosorigin.wfem.command.DownloadCommandResponseModel;
import com.atosorigin.wfem.command.DownloadStreamCommand;
import com.atosorigin.wfem.command.GenericCommandResponseModel;
import com.atosorigin.wfem.command.ListCommandDataModel;
import com.atosorigin.wfem.command.PrintCommand;
import com.atosorigin.wfem.command.PrintFdfCommand;
import com.atosorigin.wfem.command.PrintFdfCommandModelContainer;
import com.atosorigin.wfem.command.RemoteCommandException;
import com.atosorigin.wfem.command.SegmentedResponseCommand;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.command.UserSessionContextFactory;
import com.atosorigin.wfem.filestreamer.FileStreamer;
import com.atosorigin.wfem.filestreamer.FileStreamerInfo;
import com.atosorigin.wfem.htmltopdf.DocumentController;
import com.atosorigin.wfem.layout.LayoutFactory;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.loggers.AbstractLogger;
import com.atosorigin.wfem.loggers.ControllerLogger;
import com.atosorigin.wfem.login.LoginFailureInfo;
import com.atosorigin.wfem.login.LoginManager;
import com.atosorigin.wfem.login.LoginManagerLdap;
import com.atosorigin.wfem.pdf.AdobeFormCompiler;
import com.atosorigin.wfem.tierlog.ClientTierAccessLogger;
import com.atosorigin.wfem.tierlog.TierAccessLoggerInfo;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.AbstractTypePropertyDescriptor;
import com.atosorigin.wfem.types.FileType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.BkRemoteObjectFactory;
import com.atosorigin.wfem.util.DbPdfWebResourceLoader;
import com.atosorigin.wfem.util.ModelToXmlResponse;
import com.atosorigin.wfem.util.ObjectsPropertiesCache;
import com.atosorigin.wfem.util.RemoteObjectFactory;
import com.atosorigin.wfem.util.Tools;
import com.atosorigin.wfem.xmlservicelogger.XmlServiceLoggerManager;
import com.businessobjects.dsws.reportengine.Image;
import com.lowagie.text.pdf.PdfCopyFields;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class ControllerServlet extends HttpServlet {
	
	//***********************************************************************************//
	//***** Configuration constants for parameters defined in servlet configuration *****//
	//***** read in initControllerParameters() method from servletConfig            *****//
	//***********************************************************************************//
	
	// General information
	private static final String PARM_COUNTRY_PARAMETER_NAME      = "COUNTRY_PARAMETER_NAME";
	private static final String PARM_LANGUAGE_PARAMETER_NAME     = "LANGUAGE_PARAMETER_NAME";
	private static final String PARM_CHANNEL_PARAMETER_NAME      = "CHANNEL_PARAMETER_NAME";
	private static final String PARM_STARTURL_PARAMETER_NAME     = "STARTURL_PARAMETER_NAME";
	private static final String PARM_CLIENTIP_PARAMETER_NAME     = "CLIENTIP_PARAMETER_NAME";
	private static final String PARM_USER_PARAMETER_NAME         = "USER_PARAMETER_NAME";
	private static final String PARM_PASSWORD_PARAMETER_NAME     = "PASSWORD_PARAMETER_NAME";
	private static final String PARM_NEW_PASSWORD_PARAMETER_NAME = "NEW_PASSWORD_PARAMETER_NAME";

    // Compatibility parameters
	private static final String PARM_SET_URL_PREFIX = "URL_PREFIX_ENABLED";
    
    // Url providers. Readed from servelt config and than,
    // if not configured, from Configuration
	private static final String PARM_PDF_PROVIDER_URL = "PDF_PROVIDER_URL";
    
	// Transaction management
	private static final String PARM_TRANSACTION_ENABLED = "WFEM_TRANSACTIONS_ENABLED";
	
	// BPM Trace directory
	private static final String PARM_TRACE_DIRECTORY = "TRACE_DIRECTORY";
	
	// Factory and manager classes
	private static final String PARM_USER_CONTEXT_FACTORY_CLASS_NAME  = "USER_CONTEXT_FACTORY_CLASS_NAME";
	private static final String PARM_LAYOUT_FACTORY_CLASS_NAME  	  = "LAYOUT_FACTORY_CLASS_NAME";
	private static final String PARM_LOGIN_MANAGER_CLASS_NAME         = "LOGIN_MANAGER_CLASS_NAME";
	private static final String PARM_FIRST_DISPLAY_COMMAND_CLASS_NAME = "FIRST_DISPLAY_COMMAND_CLASS_NAME";
	
	// Pages
	private static final String PARM_ERROR_PAGE             = "ERROR_PAGE";
	private static final String PARM_LOGIN_PAGE             = "LOGIN_PAGE";
	private static final String PARM_NO_SESSION_PAGE        = "NO_SESSION_PAGE";
	private static final String PARM_CHANGE_PWD_PAGE        = "CHANGE_PWD_PAGE";
	private static final String PARM_LOGIN_FAILED_PAGE      = "LOGIN_FAILED_PAGE";
	private static final String PARM_CHANGE_PWD_FAILED_PAGE = "CHANGE_PWD_FAILED_PAGE";

	// Html trace setting
	private static final String PARM_SET_HTML_TRACE         = "HTML_TRACE_ENABLED";
	private static final String PARM_HTML_TRACE_DIR         = "HTML_TRACE_DIR";

	// Caching parameters
	private static final String PARM_COMMANDS_CACHE_ENABLED  = "COMMANDS_CACHE_ENABLED";
	private static final String PARM_TEMPLATES_CACHE_ENABLED = "TEMPLATES_CACHE_ENABLED";
	
	//***********************************************************************************//
	//*****                          Their default values                           *****//
	//***********************************************************************************//
	
	// General information
	private String countryParameterName  = "country";
	private String languageParameterName = "language";
	private String channelParameterName  = "channel";
	private String startUrlParameterName = "starturl";
	private String clientIpParameterName = "clientip";
	private String userParameterName     = "user";
	private String loginNameParameterName= "loginname";
	private String pwdParameterName      = "password";
	private String newPwdParameterName   = "newpassword";
	private String currentLinkedUserCodeParameterName = "cluser";
	private ArrayList<String> reservedRequestPropNames = new ArrayList<String>();
	private ArrayList<String> reservedRequestPropNamesToSanitize = new ArrayList<String>();
	
    // Compatibility parameters
	private boolean urlPrefix = true;
    
    // Url providers. Readed from servelt config and than,
    // if not configured, from Configuration
	private String pdfProviderUrl = null;
	
	// Transaction management
	private boolean transactionManagementEnabled = false;

	// BPM Trace directory
	private String traceDirectory = "C:/TRACE/";

	// Factory and manager classes
	private String userContextFactoryClassName      = null;
	private String layoutFactoryClassName           = null;
	private String loginManagerClassName            = null;
	private String firstDisplayCommandClassName     = null;

	// Pages
	private String errorPage            = "error.jsp";
	private String loginPage            = "login.jsp";
	private String noSessionPage        = "noSession.jsp";
	private String changePwdPage        = "changePwd.jsp";
	private String loginFailedPage      = "loginFailed.jsp";
	private String changePwdFailedPage  = "changePwdFailed.jsp";
	
	// Html trace setting
	private boolean htmlTrace    = false;
	private String  htmlTraceDir = "/";
	
	// Layout Template caching
	private boolean commandsCacheEnabled = true;
	private boolean templatesCacheEnabled = true;
	
	//***********************************************************************************//
	//***********************************************************************************//
	//***********************************************************************************//

	// Param for display of External Page
	private String showExternalPage        		= "util/showExternalPage.jsp";
		
	// Managed behaviors
	public static final String BHV_COPY_ABSTRACT_TYPES 					= "copyAbstractTypes";
	public static final String BHV_CHECK_CODDESC_FIELDS 				= "checkCodDescFields";
	public static final String BHV_COPY_FIELDS 							= "copyFields";
	public static final String BHV_READ_REQUEST 						= "readRequest";
	public static final String BHV_RESET_ERRORS 						= "resetErrors";
	public static final String BHV_RESET_WARNINGS 						= "resetWarnings";
	public static final String BHV_RESET_MESSAGES 						= "resetMessages";
	public static final String BHV_GET_XML_MODEL 						= "getXmlModel";
	public static final String BHV_FORWARD_AS_XML_MODEL_RESPONSE		= "forwardAsXmlModelResponse";
	public static final String BHV_MANAGE_CHANGED_FIELDS				= "manageChangedFields";
	public static final String BHV_NEW_MODEL_ON_NEW_THREAD				= "newModelOnNewThread";
	private static final String BHV_CLOSE_BROWSER_INSTANCES_ON_COMMAND	= "closeBrowserInstances";
	public static final String BHV_LOAD_MAPPED_PROPERTIES_FIELDS		= "loadMappedPropertiesFields";
	
	// Fixed command parameters
	private static final String showPageParameterName = "page";
	private static final String showPdfParameterName  = "pdf";
	
	// Managed System commands
	private static final String startProcessCommand = "startProcess";
	
	// Request parameter with parametric command name
	private static final String COMMAND_PARAMETER       = "wfemCmd";
	private static final String SYSCOMMAND_PARAMETER    = "wfemSysCmd";
	private static final String TRUSTED_SITE_REQ_INDICATOR = "trusted";
	
	// Request parameter to manage select and page commands
	private static final String LIST_PROPERTY_NAME_PARAMETER = "listPropertyName";
	private static final String SELECT_INDEX_PARAMETER       = "index";
	private static final String GOTO_PAGE_PARAMETER          = "index";
	
	// Request parameter to manage get_resource commands
	private static final String FILE_NAME_PARAMETER          = "fileName";
	private static final String SUGGESTED_FILE_NAME_PARAMETER= "suggestedFileName";
	private static final String APPL_MANUAL_ID_PARAMETER     = "applicationManualId";
	private static final String FORWARD_DISPLAY_PARAMETER    = "forwardDisplay";
	
	// Request parameter to manage chart type
	public static final String CHART_ID_PARAMETER         = "chartID";
	
	// Request parameter to manage commands on  property
	private static final String PROPERTY_NAME_PARAMETER      = "propertyName";
	
	// URL Prefix
	private static final String URL_PREFIX = "/";

	// Controller session fields name
	private static final String USER_SESSION_CONTEXT = "userSessionContext";

	// Class in session to manage command stack
	private static final String WFEM_COMMAND_STACK_CONTEXT = "WfemCommandStackContext_";
	
	// Persistent session parameter name to keep persistent models in session
	private static final String PERSISTENT_MODELS = "PersistentModels_";

	// Hidden submit element id parameter name
	public static final String HIDDEN_SUBMIT_ELEMENT_ID = "wfemHiddenSubmitElementId";
	
	// Warning hidden field with skippable propertyNames 
	public static final String SKIPPABLE_FIELDS = "skippableFields";
	
	// Browser instances management session parameters
	private static final String SESS_BROWSER_INSTANCE = "SessionBrowserInstance";
	private static final String BROWSER_INSTANCE      = "BrowserInstance";
		
	// Dynamic layout request template parameter name
	private static final String TEMPLATE = "template";
	
	// Login failure request parameter name
	private static final String LOGIN_FAILURE_REQ_PARAM 	 = "loginFailure";
	private static final String LOGIN_FAILURE_REQ_PARAM_LDAP = "loginFailureInfo";

	// Change password failure request parameter name
	private static final String CHANGEPWD_FAILURE_REQ_PARAM = "changePwdFailure";
	
	// Dynamic layout management members
	private LayoutFactory   layoutFactory = null;

	// Private members
	transient private com.atosorigin.wfem.loggers.ControllerLogger LOG = null;

	private Hashtable commandCache = new Hashtable();  
	private Class resourceSupplierClass = null;
	private boolean controllerInitialized = false;

	/**************************************************************************************************/
	/**************************************************************************************************/
	static class RedirectUrlInfo implements Serializable{
		
		private AbstractLogger logger;
		
		private ServletContext servletContext;
		private String resultingUrl;
		private String contextPath;
		
		/**************************************************************************************************/
		/**************************************************************************************************/
		public RedirectUrlInfo(ControllerServlet servlet, RequestManager requestManager, String url) throws Exception{
			
			servletContext = servlet.getServletContext();
			
			if(url == null)
				return;
			
			resultingUrl = new String(url);
						
	        getLogger().debug("Manage Url redirection for url ["+url+"]");
			Properties urlWebRedirection = Configuration.getInstance().getUrlWebRedirection();
			String[] sortedUrlWebRedirectionPropertyNames = Configuration.getInstance().getSortedUrlWebRedirectionPropertyNames();
	        for(int i=0;i<sortedUrlWebRedirectionPropertyNames.length;i++){
		    	String prop = sortedUrlWebRedirectionPropertyNames[i];
		    	if(resultingUrl.startsWith(prop)){
			    	contextPath = urlWebRedirection.getProperty(prop).trim();
			    	if(prop.endsWith("/"))
			    		prop = prop.substring(0,prop.length()-1);
		    		resultingUrl = url.substring(prop.length()+1);
		    		break;
		    	}
		    }
		    if(contextPath != null){
		    	
		        if(contextPath.endsWith("*")){
		        	String webAppSuff = resultingUrl.substring(0,resultingUrl.indexOf("/"));
		        	resultingUrl = resultingUrl.substring(resultingUrl.indexOf("/")+1);
		        	contextPath = contextPath.substring(0,contextPath.length()-1)+webAppSuff;
		        }
		    	
		        if(servlet.urlPrefix)
		        	resultingUrl = URL_PREFIX + resultingUrl;
		        	
		        servletContext = servlet.getServletContext().getContext(contextPath);
			    if(servletContext == null)
				    throw new Exception("Servlet context for web application: ["+contextPath+"] is null");
				
			    requestManager.setRedirWebApp(contextPath);
			    getLogger().debug("Url ["+url+"] is redirected to ["+contextPath+"] web application context. Page url is ["+resultingUrl+"]");
		        return;
		    }
		    
	        			    
		    getLogger().debug("Url ["+url+"] is not in URL redirection. Try to look in webApp redirection");
		    resultingUrl = new String(url);
		    String applPackage = new String(url);
		    int idx = applPackage.indexOf("/");
		    if(idx > 0)
		        applPackage = applPackage.substring(0,idx);

			contextPath = Configuration.getInstance().getWebRedirection().getProperty(applPackage);
			if(contextPath != null){
				contextPath = "/" + contextPath;
		        if(servlet.urlPrefix)
		        	resultingUrl = URL_PREFIX + resultingUrl;
		        	
		        servletContext = servlet.getServletContext().getContext(contextPath);
			    if(servletContext == null)
				    throw new Exception("Servlet context for ["+contextPath+"] web application is null");
				    
			    requestManager.setRedirWebApp(contextPath);
			    getLogger().debug("Url ["+url+"] is redirected to ["+contextPath+"] web application context. Page url is ["+resultingUrl+"]");
		    	return;
			}

			contextPath = requestManager.getRequest().getContextPath();
			resultingUrl = url;
	        if(servlet.urlPrefix)
	        	resultingUrl = URL_PREFIX + resultingUrl;
		    requestManager.setRedirWebApp(contextPath);
	        getLogger().debug("Url ["+url+"] is not redirected. Web application is ["+contextPath+"] and page url is ["+resultingUrl+"]");
			return;

		}
	
		/**************************************************************************************************/
		/**************************************************************************************************/
		private AbstractLogger getLogger(){
			return logger = logger == null ? ControllerLogger.getInstance() : logger;
		}
		
		/**************************************************************************************************/
		/**************************************************************************************************/
		public String getResultingUrl() {
			return resultingUrl;
		}

		/**************************************************************************************************/
		/**************************************************************************************************/
		public ServletContext getServletContext() {
			return servletContext;
		}

		/**************************************************************************************************/
		/**************************************************************************************************/
		public String getContextPath() {
			return contextPath;
		}

	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	static class CommandStructure implements Serializable{

		boolean inStep;
		String name;
		CommandDataModel input;
		CommandDataModel output;

		public CommandStructure(){
		}

		public CommandStructure(String name,
								CommandDataModel input,
								CommandDataModel output){
			this.name = name;
			this.input = input;
			this.output = output;
		}
		public String getName(){
				return name;
		}
		public void setName(String name){
				this.name = name;
		}
		public CommandDataModel getInput(){
				return input;
		}
		public CommandDataModel getOutput(){
				return output;
		}
		public boolean isInStep(){
				return inStep;
		}
		public void setInput(CommandDataModel newInput){
				this.input = newInput;
		}
		public void setOutput(CommandDataModel newOutput){
				this.output = newOutput;
		}
		public void setInStep(boolean inStep){
				this.inStep = inStep;
		}
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	static class CommandsStack implements Serializable{

		private transient AbstractLogger logger;
		
		private Vector commands = new Vector();
		private int sleepTimeMinutes = 0;
		private boolean keepAliveActiveOnStack = false;
		
		/**************************************************************************************************/
		/**************************************************************************************************/
		private AbstractLogger getLogger(){
			return logger = logger == null ? ControllerLogger.getInstance() : logger;
		}
		
		/**************************************************************************************************/
		/**************************************************************************************************/
		public void addCommand(DisplayCommand command, CommandDataModel input, CommandDataModel output,
							   Integer browserInstance){
			
			String name = command.getClass().getName();
			getLogger().debug("Adding display command "+name+" to the commands stack object of browser instance ["+browserInstance+"]");

			if(output != null && input != null && 
			   output.getClass().getName().equals(input.getClass().getName()))
				input = output;
			
			getLogger().debug("Models in stack are: input ["+input+"] - output ["+output+"]");
			
			CommandStructure curCmd = getCurrentCommand();
			if(curCmd != null && (curCmd.getName().equals(name) || command.isFragmentCommand())){
				curCmd.setInput(input);
				curCmd.setOutput(output);
				return;
			}

			if(curCmd != null && command.isStepCommand()){
				curCmd.setInStep(true);
				curCmd.setName(name);
				curCmd.setInput(input);
				curCmd.setOutput(output);
				return;
			}
			
			if(curCmd != null && curCmd.isInStep()){
				curCmd.setInStep(false);
				curCmd.setName(name);
				curCmd.setInput(input);
				curCmd.setOutput(output);
				return;
			}
			
			int commandStackSize = Configuration.getInstance().getCommandStackSize();
			if(commands.size() >= commandStackSize)
				commands.removeElementAt(0);
			commands.add(new CommandStructure(name,input,output));
		}
		
		public CommandStructure getCurrentCommand(){
			if(commands.size() == 0)
				return null;
			return (CommandStructure)commands.get(commands.size()-1);
		}
		
		public CommandStructure getForwardCommand(int forwardDisplay){
			int numStep = commands.size();
			int numStepForward = -1;
			if(forwardDisplay < 0)
				numStepForward = forwardDisplay * -1;
			else
				numStepForward = forwardDisplay;
			if(numStep <= numStepForward)
				return getCurrentCommand();
			for(int i=numStep-1;i>=(numStep-numStepForward);i--)
				commands.removeElementAt(i);
			commands.trimToSize();
			return getCurrentCommand();
		}

		public void addSleepTimeMinutes(int minutes) {
			sleepTimeMinutes += minutes;
		}

		public void resetSleepTimeMinutes() {
			this.keepAliveActiveOnStack = true;
			this.sleepTimeMinutes = 0;
		}

		public int getSleepTimeMinutes() {
			return sleepTimeMinutes;
		}
		
		public boolean isKeepAliveIsActive() {
			return keepAliveActiveOnStack;
		}
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private void addDisplayCommandToStack(RequestManager requestManager,
							              DisplayCommand command,
							              CommandDataModel commandInputDataModel,
							              CommandDataModel commandOuputDataModel,
							              Integer browserInstance) {
	
		CommandsStack stack = (CommandsStack)requestManager.getSession().getAttribute(WFEM_COMMAND_STACK_CONTEXT+browserInstance);
		if(stack == null){
			LOG.debug("Creating new commands stack for browser instance ["+browserInstance+"]");
			stack = new CommandsStack();
			requestManager.getSession().setAttribute(WFEM_COMMAND_STACK_CONTEXT+browserInstance, stack);
		}
	
		if(command.isNoSubmitCommand())
			return;
	
		stack.addCommand(command, commandInputDataModel, commandOuputDataModel,browserInstance);
		requestManager.getSession().setAttribute(WFEM_COMMAND_STACK_CONTEXT+browserInstance,stack);	// Per sessione persistente	
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private UserTransaction beginTransaction(String commandName) throws Exception {
		try {
		    LOG.debug("Begin of transaction on command: " + commandName);
			Context ic = new InitialContext();
			UserTransaction ut = (UserTransaction)ic.lookup("java:comp/UserTransaction");
			ut.begin();
			return ut;
		} catch(Exception e) {
			String errorMsg = "Exception on begin of transaction on command " + commandName + ": " + e;
			e = new Exception(errorMsg);
			throw e;
		}
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private void commitTransaction(String commandName,
								   UserTransaction ut) throws Exception {
		try {
		    LOG.debug("Commit of transaction on command: " + commandName);
			ut.commit();
		} catch(Exception e) {
			String errorMsg = "Exception on commit of transaction on command " + commandName + ": " + e;
			e = new Exception(errorMsg);
			throw e;
		}
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private void rollbackTransaction(String commandName,
								     UserTransaction ut) throws Exception {
		try {
		    LOG.debug("Rollback of transaction on command: " + commandName);
			ut.rollback();
		} catch(Exception e) {
			String errorMsg = "Exception on rollback of transaction on command " + commandName + ": " + e;
			e = new Exception(errorMsg);
			throw e;
		}
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private CommandDataModel copyCommandOutputIntoNewInput(RequestManager requestManager,
							           			 		   Command command,
							           			   	       CommandDataModel currentInput,
							           			   	       CommandDataModel precedentOutput,
							           			   	       boolean fromPerformTask,
							           			   	       Integer browserInstance)
													       throws Exception{

		if(command.getInputViewClass() != null && command.getInputViewClass().equals(CommandDataModel.class)){
		    // I copy the request parameters in input model only if we are in
		    // the http request from performTask event (doGet / doPost)
		    if(fromPerformTask && precedentOutput != null)
					requestManager.loadCommandDataModelProperties(precedentOutput);
			return precedentOutput;
		}
		
		CommandDataModel inputCommandDataModel = null;		
		try {
			
			inputCommandDataModel = loadCommandInputDataModelObject(requestManager,command,browserInstance);
			
		}catch(Exception e){
			
			String errorMsg = "Exception in loading command input: "+e;
			e = new Exception(errorMsg);
			LOG.error(e);
			throw e;
				
		}
			
		// Load the command data model properties if there is a data model for the command
		// getting values from the output parameter of the previous business command
		if(inputCommandDataModel != null &&
		   precedentOutput != null){
			   
			try {
				
				String copyFields = (String)requestManager.getAttribute(BHV_COPY_FIELDS);
				if(copyFields == null || !copyFields.equalsIgnoreCase("false")){
					LOG.debug("Coping precedent command output data into input data");
					Tools.copyCommandDataModel(precedentOutput,inputCommandDataModel);
				}
										
			} catch( Exception e ) {
				
				String errorMsg = "Exception in coping command output into command input: "+e;
				Exception ne = new Exception(errorMsg);
				LOG.error(ne);
				throw ne;
				
			}			
		}
	
	    // I copy the request parameters in input model only if we are in
	    // the http request from performTask event (doGet / doPost)
	    if(fromPerformTask){
			if(inputCommandDataModel != null &&
			   inputCommandDataModel != precedentOutput)
				requestManager.loadCommandDataModelProperties(command,inputCommandDataModel);
	    }
			
		return inputCommandDataModel;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private CommandDataModel copyCommandOutputIntoInput(RequestManager requestManager,
							           			 		Command command,
							           			   	    CommandDataModel currentInput,
							           			   	    CommandDataModel precedentOutput,
							           			   	    boolean fromPerformTask,
							           			   	    Integer browserInstance)
													    throws Exception{

		CommandDataModel inputCommandDataModel = null;		
		if(command.getInputViewClass() != null && precedentOutput != null &&
		   (command.getInputViewClass().equals(precedentOutput.getClass()) ||
			command.getInputViewClass().equals(CommandDataModel.class))){
			   
			LOG.debug("Command has input of the same type of precedent command output");
			inputCommandDataModel = precedentOutput;
			
		}else{

			if(currentInput == null){			
				try {
					
					inputCommandDataModel = loadCommandInputDataModelObject(requestManager,command,browserInstance);
					
				}catch(Exception e){
					
					String errorMsg = "Exception in loading command input: "+e;
					e = new Exception(errorMsg);
					LOG.error(e);
					throw e;
						
				}
			}else{
				inputCommandDataModel = currentInput;
			}
			
			// Load the command data model properties if there is a data model for the command
			// getting values from the output parameter of the previous business command
			if(inputCommandDataModel != null &&
			   precedentOutput != null){
				   
				Object sourceObject = null;
				Object targetObject = null;
			
				try {
					// Get the SOURCE object
					String sourcePropertyName = (String)requestManager.getAttribute(Constants.SOURCE_PROPERTY_NAME_PARAMETER);
					if ( sourcePropertyName == null ) {
						sourceObject = precedentOutput;
						
					} else {
						LOG.debug("Found parameter ["+Constants.SOURCE_PROPERTY_NAME_PARAMETER+"] in request with value: ["+sourcePropertyName+"]");
						sourceObject = Tools.getPropertyValue( precedentOutput, sourcePropertyName);
						
						if ( sourceObject != null ) {
							
							requestManager.removeAttribute(Constants.SOURCE_PROPERTY_NAME_PARAMETER);
							LOG.debug("Class Name of source property: "+sourceObject.getClass().getName());
							
						} else {
							String errorMsg = "Source property [" + sourcePropertyName + "] not found in previous command output ["+precedentOutput+"]";
							Exception ne = new Exception(errorMsg);
							LOG.error(ne);
							throw ne;
						}
					}
	
					// Get the TARGET object
					String targetPropertyName = (String)requestManager.getAttribute(Constants.TARGET_PROPERTY_NAME_PARAMETER);
					if ( targetPropertyName == null ) {
						targetObject = inputCommandDataModel;
						
					} else {
						LOG.debug("Found parameter ["+Constants.TARGET_PROPERTY_NAME_PARAMETER+"] in request with value: ]"+targetPropertyName+"]");
						targetObject = Tools.getPropertyValue( inputCommandDataModel, targetPropertyName);
						
						if ( targetObject != null ) {
							
							requestManager.removeAttribute(Constants.TARGET_PROPERTY_NAME_PARAMETER);
							LOG.debug("Class Name of target property: "+targetObject.getClass().getName());
							
						} else {
							String errorMsg = "Target property [" + targetPropertyName + "] not found in command input ["+inputCommandDataModel+"]";
							Exception ne = new Exception(errorMsg);
							LOG.error(ne);
							throw ne;
						}
	
					}
					
					String copyFields = (String)requestManager.getAttribute(BHV_COPY_FIELDS);
					if(copyFields == null || !copyFields.equalsIgnoreCase("false")){
						LOG.debug("Coping precedent command output data into input data");
						boolean savIsPersistent = false;
						if(targetObject instanceof CommandDataModel)
							savIsPersistent = ((CommandDataModel)targetObject).isPersistent();
							
						String copyAbstractTypes = (String)requestManager.getAttribute(BHV_COPY_ABSTRACT_TYPES);
						if(copyAbstractTypes != null && copyAbstractTypes.equalsIgnoreCase("true"))
							Tools.copyCommandDataModel((CommandDataModel)sourceObject,(CommandDataModel)targetObject);
						else
							Tools.copyObject(sourceObject,targetObject);
						
						if(targetObject instanceof CommandDataModel)
							((CommandDataModel)targetObject).setPersistent(savIsPersistent);
					}
											
				} catch( Exception e ) {
					
					String errorMsg = "Exception in coping command output into command input: "+e;
					Exception ne = new Exception(errorMsg);
					LOG.error(ne);
					throw ne;
					
				}			
			}
		}
	
	    // I copy the request parameters in input model only if we are in
	    // the http request from performTask event (doGet / doPost)
	    if(fromPerformTask){
			if(inputCommandDataModel != null &&
			   inputCommandDataModel != precedentOutput)
				requestManager.loadCommandDataModelProperties(inputCommandDataModel);
	    }
			
		return inputCommandDataModel;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private UserSessionContext createInitialUserSessionContext(RequestManager requestManager,String user) {
	
		UserSessionContext userSessionContext = new UserSessionContext();
		
	    String country  = (String)requestManager.getSession().getAttribute(countryParameterName);
	    String channel  = (String)requestManager.getSession().getAttribute(channelParameterName);
	    String language = (String)requestManager.getSession().getAttribute(languageParameterName);
	    String startUrl = (String)requestManager.getSession().getAttribute(startUrlParameterName);
	    String clientIp = (String)requestManager.getSession().getAttribute(clientIpParameterName);
	    
	    // Setting initial UserSessionContext values 
	    // (except delegatedUserCode that is managed by the login process)
	    userSessionContext.setUserCode(user);	    
	    userSessionContext.setLoginUserId(user);	    
	    userSessionContext.setCountryCode(country);
	    userSessionContext.setChannelCode(channel);
	    userSessionContext.setStartUrl(startUrl);
	    userSessionContext.setClientIp(clientIp);
	    userSessionContext.setLangCode(language);
	
	    // Setting initial ClientSessionContext values 
	    userSessionContext.getClientSessionContext().setSessionId(requestManager.getSession().getId());
	    userSessionContext.getClientSessionContext().setUserCode(user);
	    userSessionContext.getClientSessionContext().setCountryCode(country);
	    userSessionContext.getClientSessionContext().setChannelCode(channel);
	    userSessionContext.getClientSessionContext().setStartUrl(startUrl);
	    userSessionContext.getClientSessionContext().setClientIp(clientIp);
	    userSessionContext.getClientSessionContext().setLangCode(language);
	
    	String servletContextClassName = getServletContext().getClass().getName();
		LOG.info("Current servlet context class name is ["+servletContextClassName+"]");
		userSessionContext.getClientSessionContext().setApplicationServerType(ClientSessionContext.JAGUAR_AS);
    	if(servletContextClassName.equals(Configuration.getInstance().getTomcatServletContextClass()))
    		userSessionContext.getClientSessionContext().setApplicationServerType(ClientSessionContext.TOMCAT_AS);

	    return userSessionContext;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private Template createLayoutTemplate(RequestManager requestManager,
							               CommandDataModel pageInputModel,
							               String webApp, Integer browserInstance) throws Exception {
	
		String language = null;
		
		UserSessionContext userSessionContext = (UserSessionContext)requestManager.getSession().getAttribute(USER_SESSION_CONTEXT);
		if(userSessionContext == null){
			language = (String)requestManager.getSession().getAttribute(languageParameterName);
		}else{
			language = userSessionContext.getLangCode();
		}
		
		if(layoutFactoryClassName != null && layoutFactory == null){
		
		    try{
			    
			    LOG.debug("Creating Layout Factory with class "+layoutFactoryClassName);
			    Class layoutFactoryClass = Class.forName(layoutFactoryClassName);
			    layoutFactory = (LayoutFactory)layoutFactoryClass.newInstance();
	
			 }catch(Exception e){
			    
			    String errorMsg = "ControllerServlet.createLayoutTemplate: Exception in creating the layout templates factory: "+e;
			    e = new Exception(errorMsg);
				throw e;	    
		    }
			 
		}
		
		if(webApp == null)
			webApp = requestManager.getContextPath();
	
		CommandDataModel pageDataModel = null;
		String propertiesNamePrefix = (String)requestManager.getAttribute("__wfemPropertiesNamePrefix");
		if(propertiesNamePrefix != null && !propertiesNamePrefix.equals("") && pageInputModel != null){
			pageDataModel = (CommandDataModel)Tools.getPropertyValue(pageInputModel,propertiesNamePrefix);			
		}else{
			pageDataModel = pageInputModel;
		}
		Template template = new Template(language, layoutFactory, templatesCacheEnabled, requestManager.getRequest(), pageDataModel, webApp);
		template.initFieldsFactory(requestManager.getSession());
		template.setBrowserInstance(browserInstance);
		template.setUserSessionContext(userSessionContext);
		return template;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private UserSessionContext createUserSessionContext(RequestManager requestManager,
														UserSessionContext userSessionContext, boolean useUserContextFacrory) throws Exception {
		
	    try{
		    // Creating environment UserSessionContext if defined
			if(userContextFactoryClassName != null && useUserContextFacrory){
			    Class userSessionContextFactoryClass = Class.forName(userContextFactoryClassName);	    
			    LOG.info("Creating User Session Context with class "+userContextFactoryClassName +
				         " in country ["+userSessionContext.getCountryCode()+"], channel ["+userSessionContext.getChannelCode()+"],"+
				         " language ["+userSessionContext.getLangCode()+"], start URL ["+userSessionContext.getStartUrl()+"],"+
				         " client IP ["+userSessionContext.getClientIp()+"] for user ["+userSessionContext.getUserCode()+"]");
			    UserSessionContextFactory userSessionContextFactory = (UserSessionContextFactory)userSessionContextFactoryClass.newInstance();
			    userSessionContext = userSessionContextFactory.createUserSessionContext(userSessionContext);
			    if(userSessionContext != null){
			    	String linkedUser = userSessionContext.getCurrentLinkedUserCode();
					LOG.debug("Setting current linked user code ["+linkedUser+"] into ClientSessionContext");
				    userSessionContext.getClientSessionContext().setCurrentLinkedUserCode(linkedUser);
				    userSessionContext.setCreatedByFactory(true);
			    }
		    }else if(!useUserContextFacrory){
			    userSessionContext.setCreatedByFactory(false);
		    }
	
		    LOG.debug("Setting User Session Context ["+userSessionContext+"] in session ["+requestManager.getSession().getId()+"]");
		    requestManager.getSession().setAttribute(USER_SESSION_CONTEXT,userSessionContext);
		    requestManager.getSession().setAttribute("LOGGED_USER","true");

		    // if not defined, set country, channel and language attributes into session from UserSessionContext
		    saveInitialUserSessionContextParameters( requestManager, userSessionContext ); 
		    
	    	String servletContextClassName = getServletContext().getClass().getName();
			LOG.info("Current servlet context class name is ["+servletContextClassName+"]");
    		userSessionContext.getClientSessionContext().setApplicationServerType(ClientSessionContext.JAGUAR_AS);
	    	if(servletContextClassName.equals(Configuration.getInstance().getTomcatServletContextClass()))
	    		userSessionContext.getClientSessionContext().setApplicationServerType(ClientSessionContext.TOMCAT_AS);
	
			return userSessionContext;
	    	
	    }catch(Exception e){
		    
		    String errorMsg = "ControllerServlet.createUserSessionContext: Exception in creating the User Session Context: "+e;
		    Exception ne = new Exception(errorMsg);
		    LOG.error(ne);
			throw ne;
			
	    }
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void saveInitialUserSessionContextParameters(RequestManager requestManager, UserSessionContext userSessionContext){

		// set country parameter
		String country = (String)requestManager.getSession().getAttribute(countryParameterName);
		if (country == null || country.equals("")){
			String uscCountry = userSessionContext.getCountryCode();
			requestManager.getSession().setAttribute(countryParameterName,uscCountry);
			LOG.debug("Setting parameter ("+countryParameterName+") with value ["+uscCountry+"] in session from User Session Context");
		}
	
		// set channel parameter
		String channel = (String)requestManager.getSession().getAttribute(channelParameterName);
		if (channel == null || channel.equals("")){
			String uscChannel = userSessionContext.getChannelCode();
			requestManager.getSession().setAttribute(channelParameterName,uscChannel);
			LOG.debug("Setting parameter ("+channelParameterName+") with value ["+uscChannel+"] in session from User Session Context");
		}
		
		// set language parameter
		String language = (String)requestManager.getSession().getAttribute(languageParameterName);
		if (language == null || language.equals("")){
			String uscLanguage = userSessionContext.getLangCode();
			requestManager.getSession().setAttribute(languageParameterName,uscLanguage);
			LOG.debug("Setting parameter ("+languageParameterName+") with value ["+uscLanguage+"] in session from User Session Context");
		}
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	@Override
	public void destroy() {
	
		if(!controllerInitialized)
			return;
			
		LOG.info("ControllerServlet destroy");
		//Configuration.getInstance().loadConfiguration();
	
		LOG.info("ControllerServlet destroy: clearing of ObjectsPropertiesCache");
		ObjectsPropertiesCache.getInstance().clearCache();
	
		LOG.info("ControllerServlet destroy: clearing of CommandCache");
		commandCache.clear();
	
		controllerInitialized = false;
		
		super.destroy();
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	@Override
	public void doGet(HttpServletRequest request,
					  HttpServletResponse response)
					  throws ServletException, IOException {
	    LOG.debug("doGet()");
	    try{
			performTask(request,response);
	    }catch(Exception e){
	    	ServletException se = new ServletException(e);
	    	LOG.error(se);
	    	throw se;
	    }
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	@Override
	public void doPost(HttpServletRequest request,
					   HttpServletResponse response)
		               throws ServletException, IOException {
	    LOG.debug("doPost()");
	    try{
			performTask(request,response);
	    }catch(Exception e){
	    	ServletException se = new ServletException(e);
	    	LOG.error(se);
	    	throw se;
	    }
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private CommandDataContainer executeBusinessCommand(RequestManager requestManager,
									    			UserSessionContext userSessionContext,
							            			BusinessCommand command,
							            			CommandDataModel precedentOutput,
							           			   	boolean fromPerformTask,
							            			Integer browserInstance,
							            			boolean newInputModel) 
									    			throws Exception {
	
		String commandClassName = command.getClass().getName();
	
		CommandDataModel inputCommandDataModel;
		if(newInputModel)
			inputCommandDataModel = copyCommandOutputIntoNewInput(requestManager,
															      command,null,precedentOutput,
															      fromPerformTask,browserInstance);
		else
			inputCommandDataModel = copyCommandOutputIntoInput(requestManager,
															   command,null,precedentOutput,
															   fromPerformTask,browserInstance);
			
		UserTransaction ut = null;
	    CommandDataContainer commandDataContainer = null;
		CommandDataModel outputCommandDataModel = null;
	    
	    try{
			// Transactions only on BusinessCommands and if are enabled 
			// for local business components
			if(transactionManagementEnabled){
			    if(command.isTransactional()) {
			    	ut = beginTransaction(commandClassName);
			    	if(ut == null){
						String errorMsg = "Error on getting new transaction";
						Exception e = new Exception(errorMsg);
						throw e;
			    	}
			    }
			}
	
		    LOG.debug("Executing business command " + commandClassName);
		    commandDataContainer = callCommandExecute(requestManager,command,userSessionContext,inputCommandDataModel);
			outputCommandDataModel = (CommandDataModel)commandDataContainer.getModel();
			command = (BusinessCommand)commandDataContainer.getCommand();
			userSessionContext = commandDataContainer.getUserContext();
		    LOG.debug("Command executed. Output is: "+outputCommandDataModel);
		    
		    // Managing source and target properties
		    String sourcePropertyName = command.getSourcePropertyName();
		    if(sourcePropertyName != null){
				LOG.debug("Found parameter ["+Constants.SOURCE_PROPERTY_NAME_PARAMETER+"] in business command ["+command+"] with value: ["+sourcePropertyName+"]");
				requestManager.setAttribute(Constants.SOURCE_PROPERTY_NAME_PARAMETER,sourcePropertyName);
		    }else{
				requestManager.removeAttribute(Constants.SOURCE_PROPERTY_NAME_PARAMETER);
		    }
		    
		    String targetPropertyName = command.getTargetPropertyName();
		    if(targetPropertyName != null){
				LOG.debug("Found parameter ["+Constants.TARGET_PROPERTY_NAME_PARAMETER+"] in business command ["+command+"] with value: ["+targetPropertyName+"]");
				requestManager.setAttribute(Constants.TARGET_PROPERTY_NAME_PARAMETER,targetPropertyName);
		    }else{
				requestManager.removeAttribute(Constants.TARGET_PROPERTY_NAME_PARAMETER);
		    }	
	
		    // Put errors in request
		    if(outputCommandDataModel != null && outputCommandDataModel.hasCommandErrors()){
			    requestManager.setAttribute(Constants.BUSINESS_COMMAND_ERROR_PARAMETER,outputCommandDataModel.getCommandErrors());
		    }
	
			if(ut != null) {
	    		commitTransaction(commandClassName,ut);
			}
		    			    
	    }catch(CommandException ce){
	
		    if(ut != null)	{
			    rollbackTransaction(commandClassName,ut);
		    }
	
			String errorMsg = "CommandException executing business command "+commandClassName+": "+ce;
			Exception e = new Exception(errorMsg);
			throw e;
	        
	    }catch(Exception e){
	
		    if(ut != null) {
			    rollbackTransaction(commandClassName,ut);
		    }
		    
			String errorMsg = "Exception executing business command "+commandClassName+": "+e;
			e = new Exception(errorMsg);
			throw e;
	        
	    }
	
	    // Put output in request
		if(outputCommandDataModel != null)
			saveCommandOutputDataModel(requestManager,userSessionContext,command,outputCommandDataModel);
	
		// Put in request the browser instance
		requestManager.setAttribute(BROWSER_INSTANCE,browserInstance.toString());
	
		if(command.getPropertiesNamePrefix() != null && !command.getPropertiesNamePrefix().equals(""))
			requestManager.setAttribute("__wfemPropertiesNamePrefix",command.getPropertiesNamePrefix());
			
		return commandDataContainer;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private void executeChangePassword(RequestManager requestManager,
		                               String user,
		                               String password,
		                               String newPassword,
		                               Integer browserInstance) throws Exception {
		
		if(user == null || password == null || newPassword == null){
			String errorMsg = "The executeChangePassword command need the "+userParameterName+", "+pwdParameterName+" and the "+newPwdParameterName+" parameters setted. Values are: ["+user+"] ["+password+"] ["+newPassword+"]";
			Exception e = new Exception(errorMsg);
			throw e;
		}
	
		if(loginManagerClassName == null){
			String errorMsg = "The executeChangePassword command need the "+PARM_LOGIN_MANAGER_CLASS_NAME+" parameter setted";
			Exception e = new Exception(errorMsg);
			throw e;
		}
	
	    UserTransaction ut = null;
	    
	    try{
		    
		    LOG.debug("Creating Login Manager with class "+loginManagerClassName);
		    Class loginManagerClass = Class.forName(loginManagerClassName);
		    LoginManager loginManager = (LoginManager)loginManagerClass.newInstance();
	
		    // Change password need transaction (if enabled)
	  		if(transactionManagementEnabled){
		  		ut = beginTransaction("executeChangePwd");
		  		if(ut == null)
		  			return;
	  		}
	
		    // Get a moke UserSessionContext to call the changePwd process
		    UserSessionContext userSessionContext = createInitialUserSessionContext(requestManager,user);
	  		String channel = (String)requestManager.getSession().getAttribute(channelParameterName);
	  		 
		    int changePwdResult=-1;
		    if(loginManager instanceof LoginManagerLdap)
		    	changePwdResult = ((LoginManagerLdap)loginManager).executeChangePwd(userSessionContext,password,newPassword);
		    else
		    	changePwdResult = loginManager.executeChangePwd(channel,user,password,newPassword);
	
		    if(changePwdResult == LoginManager.CHANGE_PWD_FAILED ||
		       changePwdResult >= LoginManager.CHANGE_PWD_MINOR_FAILURE_INDEX){
	
			    if(ut != null)
			    	rollbackTransaction("executeChangePwd",ut);
			    	
			    requestManager.setAttribute(CHANGEPWD_FAILURE_REQ_PARAM,Integer.toString(changePwdResult));
			    
				forwardPage(requestManager,changePwdFailedPage,null,browserInstance);
				return;
				
		    }else if(changePwdResult == LoginManager.CHANGE_PWD_CORRECT){
	
			    if(ut != null)
			    	commitTransaction("executeChangePwd",ut);
			    	
			    executeLogin(requestManager,user,newPassword,browserInstance); 
				return;
				
		    }else{
			    
			    if(ut != null)
			    	rollbackTransaction("executeChangePwd",ut);
			    
				String errorMsg = "The executeChangePwd command must return an int type with "+
				                  "LoginManager.CHANGE_PWD_CORRECT for success, "+
				                  "LoginManager.CHANGE_PWD_FAILED for invalid change";
				Exception e = new Exception(errorMsg);
				throw e;
				
		    }
		    
	    }catch(Exception e){
		    
		    if(ut != null)
		    	rollbackTransaction("executeChangePwd",ut);
		    	
		    String errorMsg = "ControllerServlet.executeChangePassword: Exception in changing password: "+e;
			e = new Exception(errorMsg);
			throw e;
		    
	    }
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private void executeCommand(RequestManager requestManager,
							    UserSessionContext userSessionContext,
							    String commandName,
							    CommandDataModel precedentOutput,
							    Integer currentBrowserInstance,
							    Integer newBrowserInstance,
							    boolean newInputModel,
							    boolean loadCurrentDisplayOutput)
						        throws Exception {
	
		String readReq = (String)requestManager.getAttribute(BHV_READ_REQUEST);
		if(readReq != null && readReq.equalsIgnoreCase("false")){
		    LOG.debug("read request is FALSE: values are not readed into current display output");
			loadCurrentDisplayOutput = false;
		}
		
		// Load (and cache) the command class
		Command command = loadCommand(requestManager,userSessionContext,commandName);
		if(command == null){
			return;
		}
	
		// Load the display command output data
		// precedentOutput is not null in select command (see  executeSelectCommand)
		if(precedentOutput == null && loadCurrentDisplayOutput){
			
			try{
				
				precedentOutput = loadCurrentDisplayOutputData(requestManager,currentBrowserInstance);
	
			}catch(Exception e){
				String errorMsg = "Exception in loading the output of display command: "+e;
				e = new Exception(errorMsg);
				throw e;
			}
	
			if(precedentOutput != null && !precedentOutput.isValid()){
				LOG.info("ATTENTION !!! There are errors in the format of some fields in model ["+precedentOutput+"] loading the request values. Refreshing current display command");
		        requestManager.getResponse().setHeader("WHSReplaceAsBody","true");
		        executeCurrentDisplayCommand(requestManager,userSessionContext,precedentOutput,false,currentBrowserInstance);
				return;
			}
		}
	
		try{	
			if(command instanceof DisplayCommand) {
	
				executeDisplayCommand(requestManager,
		                              userSessionContext,(DisplayCommand)command,
		                              precedentOutput,true,newBrowserInstance,newInputModel);
						
			}else if(command instanceof PrintCommand) {
	
				executePrintCommand(requestManager,
		                              userSessionContext, (PrintCommand)command,
		                              precedentOutput,true,newBrowserInstance,true);
						
			}else if(command instanceof CalcCommand) {
				
				executeCalcCommand(requestManager,
		                           userSessionContext, (CalcCommand)command,
		                           precedentOutput,true,newBrowserInstance);
						
			}else if(command instanceof PrintFdfCommand) {
	
				executePrintFdfCommand(requestManager,
		                               userSessionContext,(PrintFdfCommand)command,
		                               precedentOutput,true,newBrowserInstance,true);
						
			} else if(command instanceof BusinessCommand) {
	
				// Manage the BusinessCommand chaining
				boolean fromPerformTask = true;
				while(true){
				
					if(precedentOutput != null)
						precedentOutput.setValid(true);
					
					CommandDataContainer commandDataContainer = executeBusinessCommand(requestManager,
						                                                               userSessionContext,(BusinessCommand)command,
						                                                               precedentOutput,fromPerformTask,newBrowserInstance,
						                                                               newInputModel);
					
					CommandDataModel outputCommandDataModel = (CommandDataModel)commandDataContainer.getModel();
					command = commandDataContainer.getCommand();
					userSessionContext = commandDataContainer.getUserContext();
					
					if(command instanceof SegmentedResponseCommand){
						
						SegmentedResponseCommand segmentedCmd = (SegmentedResponseCommand)command;
						
						if(!segmentedCmd.resourceExist()){
							requestManager.getResponse().setStatus(HttpServletResponse.SC_NOT_FOUND);
							return;
						}

						int responseLength = segmentedCmd.getResponseLength();
						String contentType = segmentedCmd.getContentType();
						String fileName  = segmentedCmd.getFileName();

						if(contentType != null && contentType.length() > 0){
							requestManager.getResponse().setContentType(contentType);
							if(responseLength > 0)
								requestManager.getResponse().setContentLength(responseLength);
						}else{
							requestManager.getResponse().setHeader("Content-Disposition", "attachment; filename=\""+fileName+"\"");
							if(responseLength > 0)
								requestManager.getResponse().setHeader("content-length",""+responseLength);
							requestManager.getResponse().setHeader("file-name",fileName);
						}
				        ServletOutputStream out = null;
				        try{
				        	out = requestManager.getResponse().getOutputStream();
							for(;;){
					            byte[] buf = segmentedCmd.getResponseSegment();
					            if(buf == null)
					            	break;
				                out.write(buf, 0, buf.length);
							}
				        }catch(Exception e){
				        	LOG.warning("Segmented response may be aborted by client");
				        }finally{
				        	if(out != null){
					            try{out.flush(); out.close();}catch(Exception e){}
				        	}
				            segmentedCmd.responseTerminated();
				        }               
					    return;		
					}
					
					if(command instanceof DownloadCommand){
						DownloadCommand downCmd = (DownloadCommand)command;
						try{
							File file = downCmd.getFile();
							if(!file.exists()){
								requestManager.getResponse().setHeader("file-name",file.getName());
								requestManager.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND,"File ["+file.getName()+"] not found");
								return;
							}
							
							InputStream is = new FileInputStream(file);
							requestManager.getResponse().setHeader("Content-Disposition", "attachment; filename=\""+file.getName()+"\"");
							requestManager.getResponse().setHeader("content-length",""+file.length());
							requestManager.getResponse().setHeader("file-name",file.getName());
					        flushInputStream(is,file.length(),requestManager);
						}catch(Exception e){
							e.printStackTrace();
						}finally{
							downCmd.downloadTerminated();
						}
					    return;		
					}
					
					if(command instanceof DownloadStreamCommand){
						DownloadStreamCommand downCmd = (DownloadStreamCommand)command;
						try{
							InputStream is = downCmd.getInputStream();
							if(is == null){
								requestManager.getResponse().setHeader("file-name",downCmd.getFileName());
								requestManager.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND,"File ["+downCmd.getFileName()+"] not found");
								return;
							}
							requestManager.getResponse().setHeader("Content-Disposition", "attachment; filename=\""+downCmd.getFileName()+"\"");
							requestManager.getResponse().setHeader("content-length",""+downCmd.getFileLength());
							requestManager.getResponse().setHeader("file-name",downCmd.getFileName());
					        flushInputStream(is,downCmd.getFileLength(),requestManager);
						}catch(Exception e){
							LOG.warning("Streamed response may be aborted by client");
						}finally{
							downCmd.downloadTerminated();
						}
					    return;		
					}
					
					String elId = (String)requestManager.getAttribute(HIDDEN_SUBMIT_ELEMENT_ID);
			        if(elId != null && elId.length() > 0 && elId.equals("none")){
						LOG.debug("WfemHiddenSubmit is for 'none'. Return nothing");
						CommandsStack stack = (CommandsStack)requestManager.getSession().getAttribute(WFEM_COMMAND_STACK_CONTEXT+currentBrowserInstance);
						if(stack != null){
							CommandStructure cs = stack.getCurrentCommand();
							if(cs != null)
								cs.setOutput(outputCommandDataModel);
						}
						requestManager.getResponse().setStatus(HttpServletResponse.SC_NO_CONTENT);
						return;
			        }
					
					if(((BusinessCommand)command).getInMemoryPdfCommand() != null){
						// Load (and cache) the command class
						Command pdfCommand = loadCommand(requestManager,userSessionContext,((BusinessCommand)command).getInMemoryPdfCommand().getName());
						if(pdfCommand != null){
							
							CommandDataModel inputPdfModel = ((BusinessCommand)command).getInMemoryPdfCommandInputModel();
							if(inputPdfModel == null)
								inputPdfModel = outputCommandDataModel;
							ByteArrayOutputStream pdfStream = null;
							if(pdfCommand instanceof PrintFdfCommand)
								pdfStream = executePrintFdfCommand(requestManager,userSessionContext,(PrintFdfCommand)pdfCommand,
																   inputPdfModel,fromPerformTask,newBrowserInstance,false);
							else if(pdfCommand instanceof PrintCommand)
								pdfStream = executePrintCommand(requestManager,userSessionContext,(PrintCommand)pdfCommand,
																inputPdfModel,fromPerformTask,newBrowserInstance,false);
							
							((BusinessCommand)command).setInMemoryPdfCommand(pdfStream);
							
							CommandDataModel inMemoryPdfCommandOutputModel = ((BusinessCommand)command).getInMemoryPdfCommandOutputModel();
							if(inMemoryPdfCommandOutputModel != null)
								outputCommandDataModel = inMemoryPdfCommandOutputModel;
						}
					}
					
					if(outputCommandDataModel instanceof PrintFdfCommandModelContainer){
						executePrintFdfCommand(requestManager,
												userSessionContext,null,
												outputCommandDataModel,fromPerformTask,newBrowserInstance,true);
						return;
					}
					
					if(outputCommandDataModel != null && !outputCommandDataModel.isValid()){
						LOG.info("ATTENTION !!! There are errors in the format of some fields in model ["+precedentOutput+"] returned from business command ["+command+"]. Refreshing current display command");
				        requestManager.getResponse().setHeader("WHSReplaceAsBody","true");
						executeCurrentDisplayCommand(requestManager,userSessionContext,outputCommandDataModel,false,currentBrowserInstance);
						return;
					}
					   
					fromPerformTask = false;
					newInputModel = false;
					
					// Get the type of display to manage
					// Normal commands has a Class, page commands has a String as next command
					Object nextCommandObject = ((BusinessCommand)command).getNextCommandObject();
					String nextCommandName   = ((BusinessCommand)command).getNextCommand();
	
					// The business command must return a next command
					if(nextCommandObject == null && requestManager.getAttribute(BHV_GET_XML_MODEL) == null) {
						String errorMsg = "Business command "+commandName+" has no return command";
						Exception e = new Exception(errorMsg);
						throw e;
					}
					
					if(nextCommandObject instanceof GenericCommandResponseModel){
						
						GenericCommandResponseModel rm = (GenericCommandResponseModel)nextCommandObject;
				        String suggestedFileName = (String)requestManager.getAttribute(SUGGESTED_FILE_NAME_PARAMETER);
				        if(suggestedFileName == null || suggestedFileName.length() == 0)
				        	suggestedFileName = rm.getSuggestedFileName();
						String contentType = rm.getContentType();
						if(contentType == null || contentType.length() == 0)
							contentType = "text/html";
						
						if(rm.getConversionType() == GenericCommandResponseModel.HTML_TO_CALC)
							contentType = "application/vnd.ms-excel";
						if(rm.getConversionType() == GenericCommandResponseModel.HTML_TO_PDF)
							contentType = "application/pdf";
						
						requestManager.getResponse().setContentType(contentType);							
						LOG.debug("Content type is ["+contentType+"]");
						
						// Managing parameters for charts
						Vector chartsParameters = rm.getChartsParamaters();
						if(chartsParameters != null && chartsParameters.size() > 0){
							for(int i=0;i<chartsParameters.size();i++){
								ChartParameters chartParameters = (ChartParameters)chartsParameters.get(i);
								requestManager.getSession().setAttribute(chartParameters.getID(),chartParameters);
							}
						}
						
						if(rm instanceof DownloadCommandResponseModel){
							
							DownloadCommandResponseModel downModel = (DownloadCommandResponseModel)nextCommandObject;
							String fileName = downModel.getFileName();
							LOG.debug("Attachment is ["+fileName+"]");
							requestManager.getResponse().setHeader("Content-Disposition", "attachment; filename=\""+fileName+"\"");
							requestManager.getResponse().setHeader("file-name",fileName);
							
						}else if(suggestedFileName != null && suggestedFileName.length() > 0){
							requestManager.getResponse().setHeader("Content-Disposition","inline; filename=\""+suggestedFileName+"\"");
							requestManager.getResponse().setHeader("file-name",suggestedFileName);
						}

						if(rm.getConversionType() == GenericCommandResponseModel.HTML_TO_CALC){
							ByteArrayOutputStream responseOutput = new ByteArrayOutputStream();
							responseOutput.write(rm.getContent());
							DocumentController doc = new DocumentController(responseOutput,getServletContext(),requestManager,outputCommandDataModel);
							responseOutput = (ByteArrayOutputStream)doc.generateCalcDocument();
							rm.setContent(responseOutput.toByteArray());
						}else if(rm.getConversionType() == GenericCommandResponseModel.HTML_TO_PDF){
							ByteArrayOutputStream responseOutput = new ByteArrayOutputStream();
							responseOutput.write(rm.getContent());
							DocumentController doc = new DocumentController(responseOutput,getServletContext(),requestManager,outputCommandDataModel);
							responseOutput = (ByteArrayOutputStream)doc.generatePdfDocument();
							rm.setContent(responseOutput.toByteArray());
						}
						
						if(rm.getContent() == null){
							LOG.debug("Content is null. Return nothing");
							requestManager.getResponse().setStatus(HttpServletResponse.SC_NO_CONTENT);
							return;
						}
						
						InputStream is = new ByteArrayInputStream(rm.getContent());
				        flushInputStream(is,rm.getContent().length,requestManager);
						return;
					}
					
					// Next command is a string. Forward to the URL and terminate
					if(nextCommandObject instanceof String){
						
						LOG.debug("The next command is a String. Try to forward to page "+nextCommandName);
						forwardPage(requestManager,nextCommandName,outputCommandDataModel,newBrowserInstance);
						return;
						
					}
					
					// Next command is an integer. Forward to current or to last display command
					if(nextCommandObject instanceof Integer){
						
						boolean bExecuteCommand = ((BusinessCommand)command).isExecuteOnForward();
						if(((Integer)nextCommandObject).intValue() == 0){
							if(bExecuteCommand)
								LOG.debug("The next command is a 0 Integer. Execute the current display command");
							else
								LOG.debug("The next command is a 0 Integer. Show the current display command");
							executeCurrentDisplayCommand(requestManager,userSessionContext,outputCommandDataModel,bExecuteCommand,newBrowserInstance);
						}else{
							if(bExecuteCommand)
								LOG.debug("The next command is a -1 Integer. Execute the last display command");
							else
								LOG.debug("The next command is a -1 Integer. Show the last display command");
							executeLastDisplayCommand(requestManager,userSessionContext,outputCommandDataModel,
													  bExecuteCommand,newBrowserInstance,-1);
						}
						return;
						
					}
					
					// Load (and cache) the command class
					command = null;
					if(nextCommandName != null)
						command = loadCommand(requestManager,userSessionContext,nextCommandName);
	
					if(requestManager.getAttribute(BHV_GET_XML_MODEL) != null &&
						(command == null || !(command instanceof BusinessCommand))){
						String xmlResponse = Tools.xmlFromModel(outputCommandDataModel,false,false);
						byte[] bytes = xmlResponse.getBytes();
						doNoCacheResponse(requestManager,"text/xml",bytes,true,null);
						return;
					}
					
					if(command == null){
						return;
					}
					
					// Forward to the DisplayCommand and terminate
					if(command instanceof DisplayCommand){
						executeDisplayCommand(requestManager,
				                              userSessionContext,(DisplayCommand)command,
				                              outputCommandDataModel,fromPerformTask,newBrowserInstance,
				                              newInputModel);
						return;
					}
		 
					// Forward to the PrintCommand and terminate
					if(command instanceof PrintCommand){
						
						executePrintCommand(requestManager,
				                            userSessionContext,(PrintCommand)command,
				                            outputCommandDataModel,fromPerformTask,newBrowserInstance,true);
						return;
					}
		 
					// Forward to the CalcCommand and terminate
					if(command instanceof CalcCommand){
						
						executeCalcCommand(requestManager,
				                            userSessionContext,(CalcCommand)command,
				                            outputCommandDataModel,fromPerformTask,newBrowserInstance);
						return;
					}
					
					// Forward to the PrintFdfCommand and terminate
					if(command instanceof PrintFdfCommand){
						executePrintFdfCommand(requestManager,
				                               userSessionContext,(PrintFdfCommand)command,
				                               outputCommandDataModel,fromPerformTask,newBrowserInstance,true);
						return;
					}
		 
					if(!(command instanceof BusinessCommand)) {
						String errorMsg = "Returned command of business command is not of DisplayCommand/BusinessCommand/PrinfFdfCommand String type";
						Exception e = new Exception(errorMsg);
						throw e;
					}
					
					precedentOutput = outputCommandDataModel;
				}
	
			}
		}catch(Exception e){
			String errorMsg = "Exception managing command execution: "+e;
			e = new Exception(errorMsg);
			throw e;
		}
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private void executeCurrentDisplayCommand(RequestManager requestManager,
									          UserSessionContext userSessionContext,
											  CommandDataModel precedentOutput,
									          boolean execute,
									          Integer browserInstance)
										      throws Exception {
	
		if(execute)									      
			LOG.debug("Executing current display command");
		else
			LOG.debug("Show current display command");
	
		CommandsStack stack = getBrowserInstanceStack(requestManager,browserInstance);
		CommandStructure commandStruct = stack.getCurrentCommand();
		if(commandStruct == null){
			String errorMsg = "There is no current display command. Verify application flow";
			Exception e = new Exception(errorMsg);
			throw e;
		}
		
		String currentCommandName = commandStruct.getName();
		Command command = loadCommand(requestManager,userSessionContext,currentCommandName);
		if(command == null){
			return;
		}
				
		CommandDataModel inputCommandDataModel = commandStruct.getInput();
		if(precedentOutput != null && execute){
			inputCommandDataModel = copyCommandOutputIntoInput(requestManager,
															   command,inputCommandDataModel,precedentOutput,
															   false,browserInstance);
		}
		
		String readReq = (String)requestManager.getAttribute(BHV_READ_REQUEST);
		if(readReq != null && readReq.equalsIgnoreCase("true")){
			if(inputCommandDataModel != null)
				requestManager.loadCommandDataModelProperties(inputCommandDataModel);
		}
		
		CommandDataModel lastOutputCommandDataModel = commandStruct.getOutput();
		CommandDataModel outputCommandDataModel = null;
	
		if(execute){
			
			try {
				
			    LOG.debug("Save ListTypes pages");
			    Map pages = getPagesInListTypes(lastOutputCommandDataModel,"");

				LOG.debug("Executing last display command "+command.getClass().getName());
				CommandDataContainer commandDataContainer = callCommandExecute(requestManager,command,userSessionContext,inputCommandDataModel);
				outputCommandDataModel = (CommandDataModel)commandDataContainer.getModel();
				command = commandDataContainer.getCommand();
				userSessionContext = commandDataContainer.getUserContext();
			    LOG.debug("Command executed. Output is: "+outputCommandDataModel);
				
			    LOG.debug("Update ListTypes pages");
			    updatedPagesInListTypes(outputCommandDataModel,pages);
			    
			}catch(CommandException ce){
				
				String errorMsg = "CommandException on executeCurrentDisplayCommand of "+command.getClass().getName()+": "+ce;
				Exception e = new Exception(errorMsg);
				throw e;
				
		    }catch(Exception e){
			    
				String errorMsg = "Exception on executeCurrentDisplayCommand of "+command.getClass().getName()+": "+e;
				e = new Exception(errorMsg);
				throw e;
		        
		    }
		}else{
			if((precedentOutput != null && lastOutputCommandDataModel != null)){
				if(precedentOutput.getClass().equals(lastOutputCommandDataModel.getClass())){
					lastOutputCommandDataModel = precedentOutput;
				}else{
					String copyAbstractTypes = (String)requestManager.getAttribute(BHV_COPY_ABSTRACT_TYPES);
					if(copyAbstractTypes != null && copyAbstractTypes.equalsIgnoreCase("true"))
						Tools.copyCommandDataModel(precedentOutput,lastOutputCommandDataModel);
					else
						Tools.copyObject(precedentOutput,lastOutputCommandDataModel);
				}
			}
			outputCommandDataModel = lastOutputCommandDataModel;
		}
		
		if(precedentOutput != null && 
		   outputCommandDataModel != null &&
		   precedentOutput != outputCommandDataModel){
			outputCommandDataModel.addCommandErrors(precedentOutput.getCommandErrors());
			outputCommandDataModel.addCommandWarnings(precedentOutput.getCommandWarnings());
			outputCommandDataModel.addCommandMessages(precedentOutput.getCommandMessages());
		}
	
		// Save the new i/o of the command
		commandStruct.setInput(inputCommandDataModel);
		commandStruct.setOutput(outputCommandDataModel);	

		requestManager.getSession().setAttribute(WFEM_COMMAND_STACK_CONTEXT+browserInstance,stack); // Per sessione persistente
		
	    if(outputCommandDataModel != null)
	    	saveCommandOutputDataModel(requestManager,userSessionContext,command,outputCommandDataModel);
	    	
		// Put in request the browser instance
		requestManager.setAttribute(BROWSER_INSTANCE,browserInstance.toString());
				
	    String nextPage = getPageFromClassName(currentCommandName); 
		forwardPage(requestManager,nextPage,outputCommandDataModel,browserInstance);
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private CommandDataModel executeDisplayCommand(RequestManager requestManager,
									   			   UserSessionContext userSessionContext,
							           			   DisplayCommand command,
							           			   CommandDataModel precedentOutput,
							           			   boolean fromPerformTask,
							           			   Integer browserInstance,
							           			   boolean newInputModel) 
									   			   throws Exception {
	
		String commandClassName = command.getClass().getName();
	
		CommandDataModel inputCommandDataModel;
		if(newInputModel)
			inputCommandDataModel = copyCommandOutputIntoNewInput(requestManager,
															   	  command,null,precedentOutput,
															   	  fromPerformTask,browserInstance);
		else
			inputCommandDataModel = copyCommandOutputIntoInput(requestManager,
															   command,null,precedentOutput,
															   fromPerformTask,browserInstance);
						
		CommandDataModel outputCommandDataModel = null;
	    try{
		    
		    LOG.debug("Executing display command " + commandClassName);
		    CommandDataContainer commandDataContainer = callCommandExecute(requestManager,command,userSessionContext,inputCommandDataModel);
			outputCommandDataModel = (CommandDataModel)commandDataContainer.getModel();
			command = (DisplayCommand)commandDataContainer.getCommand();
			userSessionContext = commandDataContainer.getUserContext();
		    LOG.debug("Display command executed. Output is: "+outputCommandDataModel);
		    
	    }catch(CommandException ce){
		    
			String errorMsg = "CommandException on display command "+commandClassName+": "+ce;
			Exception e = new Exception(errorMsg);
	        throw e;
	        
	    }catch(Exception e){
		    
			String errorMsg = "Exception on display command "+commandClassName+": "+e;
			e = new Exception(errorMsg);
	        throw e;
	        
	    }
	    
		// Add the command to the display commands stack	
		addDisplayCommandToStack(requestManager,command,inputCommandDataModel,outputCommandDataModel,browserInstance);		
	
		// Save the output in request for the jsp usebean
		if(outputCommandDataModel != null)
			saveCommandOutputDataModel(requestManager,userSessionContext,command,outputCommandDataModel);
		
		// Put in request the browser instance
		requestManager.setAttribute(BROWSER_INSTANCE,browserInstance.toString());
	
		if(command.getCommandType() == DisplayCommand.XSL_COMMAND){
			
		    String nextPage = getPageFromClassName(commandClassName);	    
			
			String xmlHeader = "<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n";
			xmlHeader += "<?xml-stylesheet type='text/xsl' href='call.wfem?wfemCmd=showPage&#38;page="+nextPage+"'?>\n";
			xmlHeader += "<requestModel>\n<BrowserInstance>"+browserInstance.toString()+"</BrowserInstance>\n";
			String xmlData = Tools.xmlFromModel(outputCommandDataModel,false,false);
			String xmlResponse = xmlHeader + xmlData;
			xmlResponse += "\n</requestModel>";

			byte[] bytes = xmlResponse.getBytes();
			doNoCacheResponse(requestManager,"text/xml",bytes,true,null);
			return outputCommandDataModel;
			
		}else if(command.getCommandType() == DisplayCommand.STATUS_COMMAND){
			
			byte[] bytes = null;  // no response body
			doNoCacheResponse(requestManager,"text/html",bytes,true,null);
			return outputCommandDataModel;
			
		}
		
		// Forwarding to DisplayCommand jsp
	    String nextPage = getPageFromClassName(commandClassName,command.getExtension());	    
		forwardPage(requestManager,nextPage,outputCommandDataModel,browserInstance);
	
		return outputCommandDataModel;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private void executeLastDisplayCommand(RequestManager requestManager,
									       UserSessionContext userSessionContext,
										   CommandDataModel precedentOutput,
									       boolean execute,
									       Integer browserInstance,
									       int forwardDisplay)
										   throws Exception {
	
		if(execute)									   
			LOG.debug("Executing last display command");
		else
			LOG.debug("Show last display command");
		 
		CommandsStack stack = (CommandsStack)requestManager.getSession().getAttribute(WFEM_COMMAND_STACK_CONTEXT+browserInstance);
		if(stack == null) {
			String errorMsg = "There is no last display command. Verify application flow (Commands stack is null)";
			Exception e = new Exception(errorMsg);
			throw e;
		}
	
		CommandStructure commandStruct = stack.getForwardCommand(forwardDisplay);
		if(commandStruct == null){
			String errorMsg = "There is no last display command. Verify application flow";
			Exception e = new Exception(errorMsg);
			throw e;
		}
		
		String currentCommandName = commandStruct.getName();
		Command	command = loadCommand(requestManager,userSessionContext,currentCommandName);
		if(command == null){
			return;
		}	
		
		CommandDataModel inputCommandDataModel = commandStruct.getInput();
		if(precedentOutput != null){
			inputCommandDataModel = copyCommandOutputIntoInput(requestManager,
															   command,inputCommandDataModel,precedentOutput,
															   false,browserInstance);
		}
			
		String readReq = (String)requestManager.getAttribute(BHV_READ_REQUEST);
		if(readReq != null && readReq.equalsIgnoreCase("true")){
			if(inputCommandDataModel != null)
				requestManager.loadCommandDataModelProperties(inputCommandDataModel);
		}
		
		CommandDataModel lastOutputCommandDataModel = commandStruct.getOutput();
		CommandDataModel outputCommandDataModel = null;
	
		if(execute){
			
			try {
				
			    LOG.debug("Save ListTypes pages");
			    Map pages = getPagesInListTypes(lastOutputCommandDataModel,"");

				LOG.debug("Executing last display command "+command.getClass().getName());
				CommandDataContainer commandDataContainer = callCommandExecute(requestManager,command,userSessionContext,inputCommandDataModel);
				outputCommandDataModel = (CommandDataModel)commandDataContainer.getModel();
				command = commandDataContainer.getCommand();
				userSessionContext = commandDataContainer.getUserContext();
			    LOG.debug("Command executed. Output is: "+outputCommandDataModel);
				
			    LOG.debug("Update ListTypes");
			    updatedPagesInListTypes(outputCommandDataModel,pages);
			    
			}catch(CommandException ce){
				
				String errorMsg = "CommandException on executeLastDisplayCommand of "+command.getClass().getName()+": "+ce;
				Exception e = new Exception(errorMsg);
				throw e;
				
		    }catch(Exception e){
			    
				String errorMsg = "Exception on executeLastDisplayCommand of "+command.getClass().getName()+": "+e;
				e = new Exception(errorMsg);
				throw e;
		        
		    }
		}else{
			if((precedentOutput != null && lastOutputCommandDataModel != null)){
				if(precedentOutput.getClass().equals(lastOutputCommandDataModel.getClass())){
					lastOutputCommandDataModel = precedentOutput;
				}else{
					String copyAbstractTypes = (String)requestManager.getAttribute(BHV_COPY_ABSTRACT_TYPES);
					if(copyAbstractTypes != null && copyAbstractTypes.equalsIgnoreCase("true"))
						Tools.copyCommandDataModel(precedentOutput,lastOutputCommandDataModel);
					else
						Tools.copyObject(precedentOutput,lastOutputCommandDataModel);
				}
			}
			outputCommandDataModel = lastOutputCommandDataModel;
		}
		
		if(precedentOutput != null && 
		   outputCommandDataModel != null &&
		   precedentOutput != outputCommandDataModel){
			outputCommandDataModel.addCommandErrors(precedentOutput.getCommandErrors());
			outputCommandDataModel.addCommandWarnings(precedentOutput.getCommandWarnings());
			outputCommandDataModel.addCommandMessages(precedentOutput.getCommandMessages());
		} 
	
		// Save the new i/o of the command
		commandStruct.setInput(inputCommandDataModel);
		commandStruct.setOutput(outputCommandDataModel);	
	    
		requestManager.getSession().setAttribute(WFEM_COMMAND_STACK_CONTEXT+browserInstance,stack); // Per sessione persistente
		
	    if(outputCommandDataModel != null)
	    	saveCommandOutputDataModel(requestManager,userSessionContext,command,outputCommandDataModel);
	    	
		// Put in request the browser instance
		requestManager.setAttribute(BROWSER_INSTANCE,browserInstance.toString());
				
	    String nextPage = getPageFromClassName(currentCommandName); 
		forwardPage(requestManager,nextPage,outputCommandDataModel,browserInstance);
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private void executeLogin(RequestManager requestManager,
		                      String user,
		                      String password,
		                      Integer browserInstance) throws Exception {

		if(user == null    ||
		   password == null){
			String errorMsg = "The executeLogin command need the "+userParameterName+" and the "+pwdParameterName+" parameters setted.";
			Exception e = new Exception(errorMsg);
			throw e;
		}
	
		if(loginManagerClassName == null){
			String errorMsg = "The executeLogin command need the "+PARM_LOGIN_MANAGER_CLASS_NAME+" parameter setted";
			Exception e = new Exception(errorMsg);
			throw e;
		}
	
	    try{
		    
		    LOG.debug("Creating Login Manager with class "+loginManagerClassName);
		    Class loginManagerClass = Class.forName(loginManagerClassName);
		    LoginManager loginManager = (LoginManager)loginManagerClass.newInstance();
	
		    // Get a moke UserSessionContext to call the login process
		    UserSessionContext userSessionContext = createInitialUserSessionContext(requestManager,user);
	
		    // Get reset password parameter
		    String resetPar = (String)requestManager.getAttribute("ResetPasswordFromClient");
		    if(resetPar != null && !resetPar.equals(""))
		    	userSessionContext.getExtendedInfo().put("ResetPasswordFromClient","true");
		    else
		    	userSessionContext.getExtendedInfo().remove("ResetPasswordFromClient");
		    
		    // The login may change the delegatedUser and/or the user into the userSessionContext instance
		    int loginResult = LoginManager.LOGIN_FAILED;
			if(!Configuration.getInstance().isAcceptCtxOnlyOnHeader())
		    	loginResult = loginManager.executeLogin(userSessionContext,password);
	
		    // Put user in session
		    requestManager.getSession().setAttribute(userParameterName,userSessionContext.getUserCode());
		    
		    if(loginResult == LoginManager.LOGIN_FAILED ||
			   loginResult == LoginManager.LOGIN_NOT_ENABLED ||
			   loginResult >= LoginManager.LOGIN_MINOR_FAILURE_INDEX){
	
			    requestManager.getSession().removeAttribute(USER_SESSION_CONTEXT);
			    
				String reason = "";
			    String pageName = "";
			    
			    if(loginManager instanceof LoginManagerLdap){
					LoginFailureInfo loginFailureInfo = loginManager.getLoginFailureInfo();
					if(loginFailureInfo == null){
						loginFailureInfo = new LoginFailureInfo();
						reason = "#"+Integer.toString(loginResult);
						loginFailureInfo.setReason(reason);
					}
				    pageName = loginFailureInfo.getPage();
				    if(pageName == null || pageName.equals("")) {
				    	pageName = loginFailedPage;
				    	loginFailureInfo.setPage(pageName);
				    }
					requestManager.setAttribute(LOGIN_FAILURE_REQ_PARAM_LDAP,loginFailureInfo);
			    }else{
					LoginFailureInfo loginFailureInfo = loginManager.getLoginFailureInfo();
					if(loginFailureInfo != null){
						reason = loginFailureInfo.getReason();
						if(reason == null || reason.equals(""))
							reason = "#"+Integer.toString(loginResult);
					    pageName = loginFailureInfo.getPage();
					    if(pageName == null || pageName.equals(""))
					    	pageName = loginFailedPage;
					}else{
						reason = "#"+Integer.toString(loginResult);
				    	pageName = loginFailedPage;
					}
				    requestManager.setAttribute(LOGIN_FAILURE_REQ_PARAM,reason);
				}
				forwardPage(requestManager,pageName,null,browserInstance);				
				return;
				
		    }else if(loginResult == LoginManager.LOGIN_PWD_EXPIRED){
			    
		    	requestManager.getRequest().setAttribute(USER_SESSION_CONTEXT,userSessionContext);
			    forwardPage(requestManager,changePwdPage,null,browserInstance);
				return;
				
		    }else if(loginResult == LoginManager.LOGIN_CORRECT){
	
				if(firstDisplayCommandClassName == null){
					String errorMsg = "To manage the Login the "+PARM_FIRST_DISPLAY_COMMAND_CLASS_NAME+" parameter must be setted";
					Exception e = new Exception(errorMsg);
					throw e;
				}
				
				// Create the real user session context and put in session
				try{
					userSessionContext = createUserSessionContext(requestManager,userSessionContext,true);
				}catch(Exception e){
					String errorMsg = "Exception in creating user session context in login phase: "+e;
					e = new Exception(errorMsg);
					throw e;
				}
	
		    	// Recupero il loginName e il tipo utente
				String[] logRes = UserContextDataLoader.loadLoginNameAndRolesForUser(userSessionContext, userSessionContext.getUserCode());
				userSessionContext.setLoginName(logRes[0]);
				userSessionContext.setUserType(logRes[1]);
				userSessionContext.getClientSessionContext().setLoginName(userSessionContext.getLoginName());
				userSessionContext.getClientSessionContext().setUserType(userSessionContext.getUserType());
				
			    // Manage the first display command for the application
			    // with no input and UserSessionContext filled
			    executeCommand(requestManager,userSessionContext,firstDisplayCommandClassName,null,
				               browserInstance,browserInstance,false,true);
			    
		    }else{
			    
				String errorMsg = "The executeLogin command must return an int type with "+
				                  "LoginManager.LOGIN_CORRECT for success, "+
				                  "LoginManager.LOGIN_FAILED for invalid login, "+
				                  "LoginManager.PWD_EXPIRED to force user to change password";
				Exception e = new Exception(errorMsg);
				throw e;
				
		    }
		    
	    }catch(Exception e){
		    
		    String errorMsg = "ControllerServlet.executeLogin: Exception in executing login: "+e;
			e = new Exception(errorMsg);
			throw e;
		    
	    }
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private void executePageCommand(RequestManager requestManager,
									UserSessionContext userSessionContext,
							        int pageCmdCode,
							        Integer browserInstance)
						            throws Exception {
		
		CommandsStack stack = getBrowserInstanceStack(requestManager,browserInstance);
		CommandStructure commandStruct = stack.getCurrentCommand();
		if(commandStruct == null){
			String errorMsg = "There is no current display command. Verify application flow";
			Exception e = new Exception(errorMsg);
			throw e;
		}
	
		String currentCommandName = commandStruct.getName();
		Command	currentCommand = loadCommand(requestManager,userSessionContext,currentCommandName);
		if(currentCommand == null){
			return;
		}	
		
		CommandDataModel currentDataModel = commandStruct.getOutput();
		
		String listPropertyName = null;	
		if(currentDataModel instanceof ListCommandDataModel){
			listPropertyName = "rows";
		}else{
			listPropertyName = (String)requestManager.getAttribute(LIST_PROPERTY_NAME_PARAMETER);
			if(listPropertyName == null){
				String errorMsg = "In select command you must give the \""+LIST_PROPERTY_NAME_PARAMETER+"\" parameter in query string if your base model is not of ListCommandDataModel type";
				Exception e = new Exception(errorMsg);
				throw e;
			}
		}
	
		LOG.debug("Searching ListType property "+listPropertyName+" on last data model "+currentDataModel.getClass());
		
		ListType currentList = null;
		try{
			currentList = searchListType(listPropertyName,currentDataModel);
			if(currentList == null){
				String errorMsg = "There is no ListType property named ["+listPropertyName+"] in current data model "+currentDataModel.getClass();
				Exception e = new Exception(errorMsg);
				throw e;
			}
			
		}catch(Exception e){
			String errorMsg = "Exception in managing page command on ListType property "+listPropertyName+": "+e;
			e = new Exception(errorMsg);
			throw e;
		}
		
		try{
			if(currentList.getModelType() != null)	
				requestManager.loadCommandDataModelProperties(currentDataModel);
		}catch(Exception e){
			String errorMsg = "Exception in loading properties for list "+listPropertyName+": "+e;
			e = new Exception(errorMsg);
			throw e;
		}
		
		if(currentDataModel != null && !currentDataModel.isValid()){
			LOG.info("ATTENTION !!! There are errors in the format of some fields in model ["+currentDataModel+"] loading the request values. Refreshing current display command");
			executeCurrentDisplayCommand(requestManager,userSessionContext,currentDataModel,false,browserInstance);
		}
		
		switch(pageCmdCode){
			case ManagedCommands.inextPageCmd:
				currentList.setNextPage();
				break;
			case ManagedCommands.ipreviousPageCmd:
				currentList.setPreviousPage();
				break;
			case ManagedCommands.ifirstPageCmd:
				currentList.setFirstPage();
				break;
			case ManagedCommands.ilastPageCmd:
				currentList.setLastPage();
				break;
			case ManagedCommands.igotoPageCmd:
				String gotoPage = (String)requestManager.getAttribute(GOTO_PAGE_PARAMETER);
				currentList.setCurrentPage(Integer.parseInt(gotoPage));
				break;
		}
	
	   	saveCommandOutputDataModel(requestManager,userSessionContext,currentCommand,currentDataModel);
	    	
		// Put in request the browser instance
		requestManager.setAttribute(BROWSER_INSTANCE,browserInstance.toString());
			
		LOG.debug("Forwarding to the last (current) display command "+currentCommandName);
	    String nextPage = getPageFromClassName(currentCommandName); 
		forwardPage(requestManager,nextPage,currentDataModel,browserInstance);
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private void executeConvertListCommand(RequestManager requestManager,
							        	   UserSessionContext userSessionContext,
							        	   int convertListCmdCode,
							        	   Integer browserInstance)
						            	   throws Exception {
		
		CommandsStack stack = getBrowserInstanceStack(requestManager,browserInstance);
		CommandStructure commandStruct = stack.getCurrentCommand();
		if(commandStruct == null){
			String errorMsg = "There is no current display command. Verify application flow";
			Exception e = new Exception(errorMsg);
			throw e;
		}
	
		CommandDataModel currentDataModel = commandStruct.getOutput();
		
		String listPropertyName = null;	
		if(currentDataModel instanceof ListCommandDataModel){
			listPropertyName = "rows";
		}else{
			listPropertyName = (String)requestManager.getAttribute(LIST_PROPERTY_NAME_PARAMETER);
			if(listPropertyName == null){
				String errorMsg = "In select command you must give the \""+LIST_PROPERTY_NAME_PARAMETER+"\" parameter in query string if your base model is not of ListCommandDataModel type";
				Exception e = new Exception(errorMsg);
				throw e;
			}
		}
	
		LOG.debug("Searching ListType property "+listPropertyName+" on last data model "+currentDataModel.getClass());
		
		ListType currentList = null;
		try{
			currentList = searchListType(listPropertyName,currentDataModel);
			if(currentList == null){
				String errorMsg = "There is no ListType property named ["+listPropertyName+"] in current data model "+currentDataModel.getClass();
				Exception e = new Exception(errorMsg);
				throw e;
			}
			
		}catch(Exception e){
			String errorMsg = "Exception in managing page command on ListType property "+listPropertyName+": "+e;
			e = new Exception(errorMsg);
			throw e;
		}
		
		LOG.debug("Streaming the resulting output");
		
		String htmlString = "";
        InputStream is = null;
        DocumentController doc = null;
        ByteArrayOutputStream pdfBaos = null;
		switch(convertListCmdCode){
			case ManagedCommands.iopenGridCalcCmd:
				htmlString = currentList.getFieldRenderer().getCalc();
				pdfBaos = new ByteArrayOutputStream();
				pdfBaos.write(htmlString.getBytes(requestManager.getResponse().getCharacterEncoding()));
				doc = new DocumentController(pdfBaos,getServletContext(),requestManager,currentDataModel);
				pdfBaos = (ByteArrayOutputStream)doc.generateCalcDocument();
				requestManager.getResponse().setHeader("Content-Disposition", "attachment; filename=\"Grid.xls\"");
				requestManager.getResponse().setHeader("file-name","Grid.xls");
				requestManager.getResponse().setHeader("content-length",""+pdfBaos.size());
		        is = new ByteArrayInputStream(pdfBaos.toByteArray());
		        flushInputStream(is,pdfBaos.size(),requestManager);
		        break;
		        
			case ManagedCommands.iopenGridPdfCmd:
				htmlString = currentList.getFieldRenderer().getPdf();
				pdfBaos = new ByteArrayOutputStream();
				pdfBaos.write(htmlString.getBytes(requestManager.getResponse().getCharacterEncoding()));
				doc = new DocumentController(pdfBaos,getServletContext(),requestManager,currentDataModel);
				pdfBaos = (ByteArrayOutputStream)doc.generatePdfDocument();
				requestManager.getResponse().setHeader("Content-Disposition", "attachment; filename=\"Grid.pdf\"");
				requestManager.getResponse().setHeader("file-name","Grid.pdf");
				requestManager.getResponse().setHeader("content-length",""+pdfBaos.size());
		        is = new ByteArrayInputStream(pdfBaos.toByteArray());
		        flushInputStream(is,pdfBaos.size(),requestManager);
		        break;
		}
		return;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private ByteArrayOutputStream  executePrintCommand(RequestManager requestManager,
									   			 	   UserSessionContext userSessionContext,
									   			 	   PrintCommand command,
									   			 	   CommandDataModel precedentOutput,
									   			 	   boolean fromPerformTask,
									   			 	   Integer browserInstance,
									   			 	   boolean doForward) throws Exception {
	
		String commandClassName = command.getClass().getName();
	
		CommandDataModel inputCommandDataModel = copyCommandOutputIntoInput(requestManager,
																		    command,null,precedentOutput,
																		    fromPerformTask,browserInstance);
	
		CommandDataModel outputCommandDataModel = null;
	    try{
		    
		    LOG.debug("Executing print command " + commandClassName);
		    CommandDataContainer commandDataContainer = callCommandExecute(requestManager,command,userSessionContext,inputCommandDataModel);
			outputCommandDataModel = (CommandDataModel)commandDataContainer.getModel();
			command = (PrintCommand)commandDataContainer.getCommand();
			userSessionContext = commandDataContainer.getUserContext();
		    LOG.debug("Print command executed. Output is: "+outputCommandDataModel);
			
	    }catch(CommandException ce){
		    
			String errorMsg = "CommandException on print command "+commandClassName+": "+ce;
			Exception e = new Exception(errorMsg);
			throw e;
	        
	    }catch(Exception e){
		    
			String errorMsg = "Exception on print command "+commandClassName+": "+e;
			e = new Exception(errorMsg);
			throw e;
	        
	    }
	    
	
		// Save the output in request for the jsp usebean
		int currentModality = -1;
		if(outputCommandDataModel != null){
			currentModality = outputCommandDataModel.getModality();
		    outputCommandDataModel.setModality(CommandDataModel.PRINT_MODALITY);
			saveCommandOutputDataModel(requestManager,userSessionContext,command,outputCommandDataModel);
		}
		
		// Put in request the browser instance
		requestManager.setAttribute(BROWSER_INSTANCE,browserInstance.toString());
	
		// Forwarding to PrintCommand jsp
	    String nextPage = getPageFromClassName(commandClassName);
		
	    ByteArrayOutputStream pdfStream = null;
	    try{
	    	pdfStream = forwardHtml2PdfCalcConverter(requestManager,nextPage,outputCommandDataModel,browserInstance,
	    											 RedirectServletResponse.PDF_RESPONSE_TYPE,doForward,command.getSuggestedFileName());
	    }catch(Exception e){
		    if(currentModality != -1 && outputCommandDataModel != null)
		    	outputCommandDataModel.setModality(currentModality);
	        String errorMsg = "Exception making HTML to PDF convert on command " + commandClassName + ": " + e;
			e = new Exception(errorMsg);
			throw e;
	    }
	    if(currentModality != -1 && outputCommandDataModel != null)
	    	outputCommandDataModel.setModality(currentModality);
		return pdfStream;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private CommandDataModel executeCalcCommand(RequestManager requestManager,
									   			 UserSessionContext userSessionContext,
							           			 CalcCommand command,
							           			 CommandDataModel precedentOutput,
							           			 boolean fromPerformTask,
							           			 Integer browserInstance) throws Exception {
	
		String commandClassName = command.getClass().getName();
	
		CommandDataModel inputCommandDataModel = copyCommandOutputIntoInput(requestManager,
																		    command,null,precedentOutput,
																		    fromPerformTask,browserInstance);
	
		CommandDataModel outputCommandDataModel = null;
	    try{
		    
		    LOG.debug("Executing calc command " + commandClassName);
		    CommandDataContainer commandDataContainer = callCommandExecute(requestManager,command,userSessionContext,inputCommandDataModel);
			outputCommandDataModel = (CommandDataModel)commandDataContainer.getModel();
			command = (CalcCommand)commandDataContainer.getCommand();
			userSessionContext = commandDataContainer.getUserContext();
		    LOG.debug("Calc command executed. Output is: "+outputCommandDataModel);
			
	    }catch(CommandException ce){
		    
			String errorMsg = "CommandException on calc command "+commandClassName+": "+ce;
			Exception e = new Exception(errorMsg);
			throw e;
	        
	    }catch(Exception e){
		    
			String errorMsg = "Exception on calc command "+commandClassName+": "+e;
			e = new Exception(errorMsg);
			throw e;
	        
	    }
	    
	
		// Save the output in request for the jsp usebean
		if(outputCommandDataModel != null){
			saveCommandOutputDataModel(requestManager,userSessionContext,command,outputCommandDataModel);
		}
		
		// Put in request the browser instance
		requestManager.setAttribute(BROWSER_INSTANCE,browserInstance.toString());
	
		// Forwarding to PrintCommand jsp
	    String nextPage = getPageFromClassName(commandClassName);
		
	    try{
	        forwardHtml2PdfCalcConverter(requestManager,nextPage,outputCommandDataModel,browserInstance,
	        							 RedirectServletResponse.CALC_RESPONSE_TYPE,true,command.getSuggestedFileName());
	    }catch(Exception e){
	        String errorMsg = "Exception making HTML to CALC convert on command " + commandClassName + ": " + e;
			e = new Exception(errorMsg);
			throw e;
	    }
		return outputCommandDataModel;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private ByteArrayOutputStream executePrintFdfCommand(RequestManager requestManager,
														 UserSessionContext userSessionContext,
														 PrintFdfCommand command,
														 CommandDataModel precedentOutput,
														 boolean fromPerformTask,
														 Integer browserInstance, 
														 boolean responseToBrowser) throws Exception {

		ByteArrayOutputStream allPdfOut = new ByteArrayOutputStream();

		String suggestedFileName = null;
		String langCode = null;
		if(userSessionContext == null)
			langCode = (String)requestManager.getSession().getAttribute(languageParameterName);
		else
			langCode = userSessionContext.getLangCode();
	
		if(precedentOutput == null || 
		   (precedentOutput != null && !(precedentOutput instanceof PrintFdfCommandModelContainer))){

			suggestedFileName = command.getSuggestedFileName();
			String commandClassName = command.getClass().getName();
			CommandDataModel inputCommandDataModel = copyCommandOutputIntoInput(requestManager,
																			    command,null,precedentOutput,
																			    fromPerformTask,browserInstance);
			CommandDataModel outputCommandDataModel = null;
		    try{			    
			    LOG.debug("Executing print FDF command " + commandClassName);
			    CommandDataContainer commandDataContainer = callCommandExecute(requestManager,command,userSessionContext,inputCommandDataModel);
				outputCommandDataModel = (CommandDataModel)commandDataContainer.getModel();
				command = (PrintFdfCommand)commandDataContainer.getCommand();
				userSessionContext = commandDataContainer.getUserContext();
			    LOG.debug("Print FDF command executed. Output is: "+outputCommandDataModel);
		    }catch(CommandException ce){
				String errorMsg = "CommandException on print FDF command "+commandClassName+": "+ce;
				Exception e = new Exception(errorMsg);
		        throw e;
		    }catch(Exception e){
				String errorMsg = "Exception on print FDF command "+commandClassName+": "+e;
				e = new Exception(errorMsg);
		        throw e;
		    }
		    
			String pdfResourceName = command.getPdfResourceName();
			if(pdfResourceName == null)
				pdfResourceName = commandClassName;
			String pdfFileName = getPdfFromClassName(pdfResourceName,langCode);
			LOG.debug("PDF File name is "+pdfFileName);
			
			// Get the servlet context mapped to redirection
			RedirectUrlInfo redirUrlInfo = new RedirectUrlInfo(this,requestManager,pdfFileName);
	        ServletContext appContext = redirUrlInfo.getServletContext();
			InputStream pdfIn = DbPdfWebResourceLoader.getInputStream(redirUrlInfo.getContextPath()+redirUrlInfo.getResultingUrl());
			if(pdfIn == null)
				pdfIn = appContext.getResourceAsStream(redirUrlInfo.getResultingUrl());
			if(pdfIn == null)
				pdfIn = command.getClass().getResourceAsStream("/"+commandClassName.replaceAll("\\.", "/")+".pdf");
			
	        PdfReader reader = new PdfReader(pdfIn);
	        PdfStamper stamp = new PdfStamper(reader, allPdfOut);
	        AdobeFormCompiler.fillAdobeForm(appContext,stamp,outputCommandDataModel);
        	stamp.setFormFlattening(command.removeFormFields());
	        stamp.close();
	        pdfIn.close();
		
		}else if(precedentOutput != null && 
				 precedentOutput instanceof PrintFdfCommandModelContainer){
			
			PrintFdfCommandModelContainer fdfCommandContainer = (PrintFdfCommandModelContainer)precedentOutput;
			suggestedFileName = fdfCommandContainer.getSuggestedFileName();
			java.util.List fdfCommandList = fdfCommandContainer.getFdfCommandList();
			java.util.List fdfCommandTitleList = fdfCommandContainer.getFdfCommandTitleList();
			java.util.List fdfCommandModelList = fdfCommandContainer.getFdfCommandModelList();
			
            int pageOffset = 0;
            ArrayList master = new ArrayList();                
			PdfCopyFields copy = new PdfCopyFields(allPdfOut);
            
        	for(int i=0;i<fdfCommandList.size();i++){
        		
    	        InputStream pdfIn = null;
		        ServletContext appContext = null;
		        boolean removeFormFields = true;
        		String fdfTitle = (String)fdfCommandTitleList.get(i);
        		Object fdfCommandObject = fdfCommandList.get(i);
        		CommandDataModel fdfCommandModel = (CommandDataModel)fdfCommandModelList.get(i);

        		if(fdfCommandObject instanceof Class){ /* Manage the Command instance */
        			
	        		Class fdfCommandClass = (Class)fdfCommandObject;
	        		PrintFdfCommand fdfCommand = (PrintFdfCommand)fdfCommandClass.newInstance();
	    			String commandClassName = fdfCommandClass.getName();
					CommandDataModel outputCommandDataModel = null;
					try{			    
						LOG.debug("Executing print FDF command " + commandClassName);
						CommandDataContainer commandDataContainer = callCommandExecute(requestManager,fdfCommand,userSessionContext,fdfCommandModel);
						outputCommandDataModel = (CommandDataModel)commandDataContainer.getModel();
						fdfCommand = (PrintFdfCommand)commandDataContainer.getCommand();
						userSessionContext = commandDataContainer.getUserContext();
						LOG.debug("Print FDF command executed. Output is: "+outputCommandDataModel);
					}catch(CommandException ce){
						String errorMsg = "CommandException on print FDF command "+commandClassName+": "+ce;
						Exception e = new Exception(errorMsg);
						throw e;
					}catch(Exception e){
						String errorMsg = "Exception on print FDF command "+commandClassName+": "+e;
						e = new Exception(errorMsg);
						throw e;
					}
					String pdfResourceName = fdfCommand.getPdfResourceName();
					if(pdfResourceName == null)
						pdfResourceName = fdfCommandClass.getName();
	        		String pdfFileName = getPdfFromClassName(pdfResourceName,langCode);
					LOG.debug("PDF File name is "+pdfFileName);
					
					// Get the servlet context mapped to redirection
					RedirectUrlInfo redirUrlInfo = new RedirectUrlInfo(this,requestManager,pdfFileName);
			        appContext = redirUrlInfo.getServletContext();
					pdfIn = DbPdfWebResourceLoader.getInputStream(redirUrlInfo.getContextPath()+redirUrlInfo.getResultingUrl());
					if(pdfIn == null)
						pdfIn = appContext.getResourceAsStream(redirUrlInfo.getResultingUrl());
					if(pdfIn == null)
						pdfIn = fdfCommandContainer.getClass().getResourceAsStream("/"+commandClassName.replaceAll("\\.", "/")+".pdf");
					
					removeFormFields = fdfCommand.removeFormFields();
					
        		}else if(fdfCommandObject instanceof String){ /* Manage the pdf file instance */
        			
        			String pdfFileNameAsClass = (String)fdfCommandObject;
        			String pdfFileName = getPdfFromClassName(pdfFileNameAsClass,null);
        			
					RedirectUrlInfo redirUrlInfo = new RedirectUrlInfo(this,requestManager,pdfFileName);
			        appContext = redirUrlInfo.getServletContext();
					pdfIn = DbPdfWebResourceLoader.getInputStream(redirUrlInfo.getContextPath()+redirUrlInfo.getResultingUrl());
					if(pdfIn == null)
						pdfIn = fdfCommandContainer.getClass().getResourceAsStream("/"+pdfFileNameAsClass.replaceAll("\\.", "/")+".pdf");
					if(pdfIn == null)
						pdfIn = appContext.getResourceAsStream(redirUrlInfo.getResultingUrl());
					
        		}
    	        
    	        PdfReader reader = new PdfReader(pdfIn);
    		    ByteArrayOutputStream pdfOut = new ByteArrayOutputStream();
    	        PdfStamper stamp = new PdfStamper(reader, pdfOut);
    	        AdobeFormCompiler.fillAdobeForm(appContext,stamp,fdfCommandModel);
   	        	stamp.setFormFlattening(removeFormFields);
    	        stamp.close();
    	        pdfIn.close();

    	        reader = new PdfReader(pdfOut.toByteArray());
    	        copy.addDocument(reader);
    	        
                if(fdfCommandList.size() > 1){
                	if(fdfTitle != null && fdfTitle.length() > 0){
		                HashMap pageBookmark = new HashMap();
		                pageBookmark.put("Title",fdfTitle);
		                pageBookmark.put("Page",""+(pageOffset+1));
		                pageBookmark.put("Action", "GoTo");	                
			            master.add(pageBookmark);
                	}
                }
                
                pageOffset += reader.getNumberOfPages();
                
        	}
        	
            if (master.size() > 0){
	            copy.getWriter().setViewerPreferences(PdfWriter.PageModeUseOutlines);
                copy.setOutlines(master);
            }

            copy.close();
			
		}
		
	    allPdfOut.close();	    	
		
	    if(responseToBrowser)
	    	doNoCacheResponse(requestManager,"application/pdf",allPdfOut.toByteArray(),false,suggestedFileName);

	    return allPdfOut;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private void executeSelectCommand(RequestManager requestManager,
								      UserSessionContext userSessionContext, String commandName,
								      Integer currentBrowserInstance, Integer newBrowserInstance,
								      boolean newInputModel) throws Exception{
	
		CommandsStack stack = getBrowserInstanceStack(requestManager,currentBrowserInstance);
		CommandStructure commandStruct = stack.getCurrentCommand();
	    if (commandStruct == null) {
	        String errorMsg = "There is no current display command. Verify application flow";
	        Exception e = new Exception(errorMsg);
	        throw e;
	    }
	
	    String index = (String)requestManager.getAttribute(SELECT_INDEX_PARAMETER);
	    if (index == null) {
	        String errorMsg = "In select command you must give the \"" + SELECT_INDEX_PARAMETER + "\" parameter in query string";
	        Exception e = new Exception(errorMsg);
	        throw e;
	    }
	    LOG.debug("Select command element index=" + index + " on browser instance " + currentBrowserInstance + " for new browser instance " + newBrowserInstance);
	
	    CommandDataModel currentDataModel = commandStruct.getOutput();
	
	    String listPropertyName = null;
	    if (currentDataModel instanceof ListCommandDataModel) {
	        listPropertyName = "rows";
	    } else {
	        listPropertyName = (String)requestManager.getAttribute(LIST_PROPERTY_NAME_PARAMETER);
	        if (listPropertyName == null) {
	            String errorMsg = "In select command you must give the \"" + LIST_PROPERTY_NAME_PARAMETER + "\" parameter in query string if your base model is not of ListCommandDataModel type";
		        Exception e = new Exception(errorMsg);
		        throw e;
	        }
	    }
	
	    LOG.debug("Searching ListType property " + listPropertyName + " on last data model " + currentDataModel.getClass());
	
	    ListType currentList = null;
	    try {
	        currentList = searchListType(listPropertyName, currentDataModel);
	        if (currentList == null) {
	            String errorMsg = "There is no ListType property named [" + listPropertyName + "] in current data model " + currentDataModel.getClass();
		        Exception e = new Exception(errorMsg);
		        throw e;
	        }
	
	    }
	    catch (Exception e) {
	        String errorMsg ="Exception in managing select command on ListType property " + listPropertyName + ": " + e;
	        e = new Exception(errorMsg);
	        throw e;
	    }
	
	    CommandDataModel element = currentList.get(Integer.valueOf(index).intValue());
	
	    LOG.debug("Passing the selected element at absolute index " + Integer.valueOf(index).intValue() + " to the specified command " + commandName);
	    executeCommand(requestManager,userSessionContext,commandName,element,
	    			   newBrowserInstance,newBrowserInstance,newInputModel,true);
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private ByteArrayOutputStream forwardHtml2PdfCalcConverter(RequestManager requestManager,
															   String pageName, CommandDataModel pageInputModel,
															   Integer browserInstance, int type, boolean doForward, String suggestedFileName) throws Exception {
	
	    try {
		    
			LOG.debug("Creating template for page: " + pageName);

			// Get the servlet context mapped to redirection
			RedirectUrlInfo redirUrlInfo = new RedirectUrlInfo(this,requestManager,pageName);
			ServletContext appContext = redirUrlInfo.getServletContext();
			
			Template template = createLayoutTemplate(requestManager,pageInputModel,redirUrlInfo.getContextPath(),browserInstance);
			if (template != null) {
				LOG.debug("Put template for " + pageName + " in request");
				requestManager.setAttribute(TEMPLATE, template);
			}
	
	        String p = redirUrlInfo.getResultingUrl();	
	        LOG.debug("Forwarding to print page: " + p);
	        RequestDispatcher rd = appContext.getRequestDispatcher(p);
	        if(rd == null){
		        throw new Exception("RequestDispatcher is null");
	        }
	
	        String fileNameToDownload = (String)requestManager.getAttribute(FILE_NAME_PARAMETER);
	        String reqSuggestedFileName = (String)requestManager.getAttribute(SUGGESTED_FILE_NAME_PARAMETER);
	        if(reqSuggestedFileName != null && reqSuggestedFileName.length() > 0)
	        	suggestedFileName = reqSuggestedFileName;
			RedirectServletResponse rsr = new RedirectServletResponse(type,
																	  appContext,requestManager,
																	  htmlTrace,htmlTraceDir,pageName,fileNameToDownload,
																	  pageInputModel,doForward,suggestedFileName);
			rd.forward(requestManager.getRequest(), rsr);
	        return rsr.getPdfStream();
	
	    } catch (Exception e) {
	
	        String errorMsg = "Exception forwarding to " + pageName + " " + e;
	        e = new Exception(errorMsg);
	        throw e;
	
	    }
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private void forwardPage(RequestManager requestManager, String url,
							 CommandDataModel pageInputModel, Integer browserInstance) throws Exception {
	
		Object xmlResp = requestManager.getAttribute(BHV_FORWARD_AS_XML_MODEL_RESPONSE);
		if(xmlResp != null && "true".equals(xmlResp)){
			try{
				String xmlResponse = pageInputModel == null ? "<model></model>" : new ModelToXmlResponse().xmlFromModel(pageInputModel);
				byte[] bytes = xmlResponse.getBytes();
				doNoCacheResponse(requestManager,"text/xml",bytes,true,null);
				return;
			}catch(Throwable t){}
		}
		
	    String pageName = "";
	    String parameters = "";
	
	    // Forward to the input page
	    try {
	
		    int idx = url.indexOf("?");
		    if(idx >= 0){
			    pageName = url.substring(0,idx);
			    parameters = url.substring(idx+1);
		    }else{
			    pageName = url;
		    }
		    
		    // Put parameters in request
		    putQueryStringInRequest(requestManager,parameters);

			// Get the servlet context mapped to redirection
			RedirectUrlInfo redirUrlInfo = new RedirectUrlInfo(this,requestManager,url);
	        ServletContext appContext = redirUrlInfo.getServletContext();
		    
	        try{
		        LOG.debug("Creating template for page: " + pageName);
		        Template template = createLayoutTemplate(requestManager,pageInputModel,
		        										 redirUrlInfo.getContextPath(),browserInstance);
				if(template != null){
			        LOG.debug("Put template for " +pageName+" in request");
					requestManager.setAttribute(TEMPLATE,template);
				}
	        }catch(Exception e){
	        	String errorMsg = "Exception creating template for page ["+pageName+"]: "+e;
	        	e = new Exception(errorMsg);
	        	throw e;
	        }
	
	        url = pageName;
	        if(!parameters.equals(""))
	        	url += "?" + parameters;
	        
	        String p = redirUrlInfo.getResultingUrl();	
	        LOG.debug("Forwarding to page: " + p);
	        RequestDispatcher rd = appContext.getRequestDispatcher(p);
	        if(rd == null){
		    	String errorMsg = "RequestDispatcher is null";
				manageError(requestManager,new CommandException(errorMsg));
				return;
	        }
	
        	requestManager.getSession().removeAttribute("uploadMaxSize");
	        if(pageInputModel != null && pageInputModel.getUploadMaxSize() > 0)
	        	requestManager.getSession().setAttribute("uploadMaxSize",new Long(pageInputModel.getUploadMaxSize()));
	        	
			String elId = (String)requestManager.getAttribute(HIDDEN_SUBMIT_ELEMENT_ID);
	        if(htmlTrace || (elId != null && elId.length() > 0)){
		        String fileNameToDownload = (String)requestManager.getAttribute(FILE_NAME_PARAMETER);
				RedirectServletResponse rsr = new RedirectServletResponse(RedirectServletResponse.HTML_RESPONSE_TYPE,
																		  appContext,requestManager,
																		  htmlTrace,htmlTraceDir,pageName,fileNameToDownload,
																		  pageInputModel,true,null);
	        	rsr.setHeader("Cache-Control","no-cache, no-store, must-revalidate, max-age=0");
	        	rsr.setHeader("Pragma","no-cache");
	        	rsr.setHeader("Expires", "0");
				rd.forward(requestManager.getRequest(), rsr);
	        }else{
				doNoCacheResponse(requestManager,null,null,true,null);
		        rd.forward(requestManager.getRequest(),requestManager.getResponse());
	        }
	        return;
	        
	    } catch (Exception e) {
		    
	    	String errorMsg = "Exception forwarding to " + pageName + " "+ e;
			manageError(requestManager,new CommandException(errorMsg));
			return;
			
	    }
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private void forwardPdf(RequestManager requestManager, String inputPdfFileRef) throws Exception {

		try{
			if(inputPdfFileRef == null || inputPdfFileRef.length() == 0){
		    	String errorMsg = "No pdf name specified";
		    	doNoCacheResponse(requestManager,"text/html",errorMsg.getBytes(),true,null);
				return;
			}
				
			String[] pdfFileRefs = inputPdfFileRef.split(",");
			if(pdfFileRefs.length == 1){
				
				String pdfFileRef = pdfFileRefs[0];
				
				String fileName = pdfFileRef; 
				int idx = pdfFileRef.lastIndexOf(URL_PREFIX);
				if(idx >= 0)
					fileName = fileName.substring(idx+1);
				if(!fileName.endsWith(".pdf"))
					fileName += ".pdf";
				
		        InputStream pdfIn = DbPdfWebResourceLoader.getInputStream(pdfFileRef);
				if(pdfIn == null){
					
					String pdfUrl = DbPdfWebResourceLoader.getUrl(pdfFileRef);
					if(pdfUrl != null){
				    	String jsScript = "<script>window.location.href=\""+pdfUrl+"\";</script>";
				    	doNoCacheResponse(requestManager,"text/html",jsScript.getBytes(),true,null);
						return;
					}
					
					if(pdfFileRef.startsWith(URL_PREFIX))
						pdfFileRef = pdfFileRef.substring(1);
					String contextPath = URL_PREFIX+pdfFileRef.substring(0,pdfFileRef.indexOf(URL_PREFIX));
					ServletContext appContext = getServletContext().getContext(contextPath);
				    if(appContext == null){
				    	String errorMsg = "Servlet context for web application: ["+contextPath+"] is null";
						manageError(requestManager,new CommandException(errorMsg));
						return;
				    }
					String filePath = pdfFileRef.substring(pdfFileRef.indexOf(URL_PREFIX));
					pdfIn = appContext.getResourceAsStream(filePath);
					if(pdfIn == null){
				    	String errorMsg = pdfFileRef+" not found";
				    	doNoCacheResponse(requestManager,"text/html",errorMsg.getBytes(),true,null);
						return;
					}
				}
				
		    	ByteArrayOutputStream out = new ByteArrayOutputStream();
			    byte[] buf = new byte[16*1024];
			    int charsRead;
			    while ((charsRead = pdfIn.read(buf)) != -1) {
			    	out.write(buf, 0, charsRead);
			    }
			    pdfIn.close();
				doNoCacheResponse(requestManager,"application/pdf",out.toByteArray(),false,fileName);
				
			}else{
				
				boolean oneFound = false;
				ByteArrayOutputStream allPdfOut = new ByteArrayOutputStream();
				PdfCopyFields copy = new PdfCopyFields(allPdfOut);
				
				String fileName = "doc.pdf"; 
				for(int i=0;i<pdfFileRefs.length;i++){
					
					String pdfFileRef = pdfFileRefs[i];
					
			        InputStream pdfIn = DbPdfWebResourceLoader.getInputStream(pdfFileRef);
					if(pdfIn == null){
						if(pdfFileRef.startsWith(URL_PREFIX))
							pdfFileRef = pdfFileRef.substring(1);
						String contextPath = URL_PREFIX+pdfFileRef.substring(0,pdfFileRef.indexOf(URL_PREFIX));
						ServletContext appContext = getServletContext().getContext(contextPath);
					    if(appContext == null)
					    	continue;
					    String filePath = pdfFileRef.substring(pdfFileRef.indexOf(URL_PREFIX));
						pdfIn = appContext.getResourceAsStream(filePath);
						if(pdfIn == null)
					    	continue;
					}
					if(!oneFound){
						fileName = pdfFileRef; 
						int idx = pdfFileRef.lastIndexOf(URL_PREFIX);
						if(idx >= 0)
							fileName = fileName.substring(idx+1);
						if(!fileName.endsWith(".pdf"))
							fileName += ".pdf";
					}
					oneFound = true;
	    	        PdfReader reader = new PdfReader(pdfIn);
				    pdfIn.close();
	    	        copy.addDocument(reader);
				}
				
				if(oneFound){
					copy.close();
					allPdfOut.close();
					doNoCacheResponse(requestManager,"application/pdf",allPdfOut.toByteArray(),false,fileName);
				}else{
					allPdfOut.close();
					doNoCacheResponse(requestManager,"text/html","No file found".getBytes(),true,null);
				}
			}
			
	    } catch (Exception e) {
	    	String errorMsg = "Exception forwarding to pdf "+inputPdfFileRef+" "+e;
	    	doNoCacheResponse(requestManager,"text/html",errorMsg.getBytes(),true,null);
			return;
	    }
		
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private CommandsStack getBrowserInstanceStack(RequestManager requestManager, Integer browserInstance) {
										          		  
		CommandsStack stack = (CommandsStack)requestManager.getSession().getAttribute(WFEM_COMMAND_STACK_CONTEXT+browserInstance);
		if(stack == null){
			LOG.debug("Creating new commands stack for browser instance ["+browserInstance+"]");
			stack = new CommandsStack();
			requestManager.getSession().setAttribute(WFEM_COMMAND_STACK_CONTEXT+browserInstance, stack);
		}
	
		return stack;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getPageFromClassName(String className) {
		return getPageFromClassName(className,"jsp");
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getPageFromClassName(String className, String ext) {
	    String pageName = className;
	
	    // Remove the initial package name (Look for the first '.')
	    int idx = pageName.indexOf(".");
	    if(idx >= 0)
	    	pageName = pageName.substring(idx+1);
	
	    // Replace the '.' with the '/' character
	    pageName = pageName.replace('.','/') + "." + ext;
	    return pageName;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getPdfFromClassName(String className, String langCode) {
	    String pdfName = className;
	    
	    // Remove the initial package name (Look for the first '.')
	    int idx = pdfName.indexOf(".");
	    if(idx >= 0)
	    	pdfName = pdfName.substring(idx+1);
	
	    pdfName = pdfName.replace('.','/');
	
	    if(langCode != null)
		    pdfName += "_"+langCode+".pdf";
	    else
		    pdfName += ".pdf";
	    return pdfName;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	@Override
	public void init() {	
		
		// If the controller is just initialized -> reinit
		if(controllerInitialized)
			destroy();
				
	    // Load the global configuration   
		Configuration.initInstance();
		
	    // Get the logger   
		LOG = com.atosorigin.wfem.loggers.ControllerLogger.getInstance();
	
	    ServletConfig srltConfig = getServletConfig();    
		LOG.print("Initializing ControllerServlet into context name ["+srltConfig.getServletContext().getServletContextName()+"]");
	
		try{
			
			// Initialize the Controller parameters
			initControllerParameters();
			
			// Initialize the utility singleton classes
			initializeUtility();
	
		}catch(Exception e){
			String errorMsg = "Exception in initializing ControllerServlet: "+e;
			Exception ne = new Exception(errorMsg);
		    LOG.error(ne);
		    return;
		}
	
	    // Initialize the cache of commands
	    LOG.info("Initializing commands cache");
	    commandCache.clear();
	
	    controllerInitialized = true;
	    LOG.print("Controller servlet correctly initialized. Ready to work...");
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private void initControllerParameters() {
	
		String s = 	"Wfem controller configuration: ";
	    ServletConfig config = getServletConfig();
	
	    LOG.print(s+"Start reading parameters");
	
	    s += "Parameter ";
	    
	    //*********************************************************************************//
	    //***                          General information                              ***//
	    //*********************************************************************************//
	    if (config.getInitParameter(PARM_COUNTRY_PARAMETER_NAME) != null)
	        countryParameterName = config.getInitParameter(PARM_COUNTRY_PARAMETER_NAME);
		LOG.print(s+PARM_COUNTRY_PARAMETER_NAME+": name to keep in session the country is setted to ["+countryParameterName+"]");
	
	    if (config.getInitParameter(PARM_LANGUAGE_PARAMETER_NAME) != null)
	        languageParameterName = config.getInitParameter(PARM_LANGUAGE_PARAMETER_NAME);
	    LOG.print(s+PARM_LANGUAGE_PARAMETER_NAME+": name to keep in session the country is setted to ["+languageParameterName+"]");
	
	    if (config.getInitParameter(PARM_CHANNEL_PARAMETER_NAME) != null)
	        channelParameterName = config.getInitParameter(PARM_CHANNEL_PARAMETER_NAME);
	    LOG.print(s+PARM_CHANNEL_PARAMETER_NAME+": name to keep in session the channel is setted to ["+channelParameterName+"]");
	
	    if (config.getInitParameter(PARM_STARTURL_PARAMETER_NAME) != null)
	        startUrlParameterName = config.getInitParameter(PARM_STARTURL_PARAMETER_NAME);
	    LOG.print(s+PARM_STARTURL_PARAMETER_NAME+": name to keep in session the starting URL is setted to ["+startUrlParameterName+"]");
	
	    if (config.getInitParameter(PARM_CLIENTIP_PARAMETER_NAME) != null)
	        clientIpParameterName = config.getInitParameter(PARM_CLIENTIP_PARAMETER_NAME);
	    LOG.print(s+PARM_CLIENTIP_PARAMETER_NAME+": name to keep in session the client IP is setted to ["+clientIpParameterName+"]");
	
	    if (config.getInitParameter(PARM_USER_PARAMETER_NAME) != null)
	        userParameterName = config.getInitParameter(PARM_USER_PARAMETER_NAME);
	    LOG.print(s+PARM_USER_PARAMETER_NAME+": name to keep in session the user is setted to ["+userParameterName+"]");
	
	    if (config.getInitParameter(PARM_PASSWORD_PARAMETER_NAME) != null)
	        pwdParameterName = config.getInitParameter(PARM_PASSWORD_PARAMETER_NAME);
	    LOG.print(s+PARM_PASSWORD_PARAMETER_NAME+": name to search in request the user password is setted to ["+pwdParameterName+"]");
	
	    if (config.getInitParameter(PARM_NEW_PASSWORD_PARAMETER_NAME) != null)
	        newPwdParameterName = config.getInitParameter(PARM_NEW_PASSWORD_PARAMETER_NAME);
	    LOG.print(s+PARM_NEW_PASSWORD_PARAMETER_NAME+": name to search in request the user new password (for change password command) is setted to ["+newPwdParameterName+"]");
	    
	    reservedRequestPropNames.clear();
		reservedRequestPropNames.add(COMMAND_PARAMETER);
		reservedRequestPropNames.add(SYSCOMMAND_PARAMETER);
		reservedRequestPropNames.add(BROWSER_INSTANCE);
		reservedRequestPropNames.add(countryParameterName);
		reservedRequestPropNames.add(languageParameterName);
		reservedRequestPropNames.add(channelParameterName);
		reservedRequestPropNames.add(startUrlParameterName);
		reservedRequestPropNames.add(clientIpParameterName);
		reservedRequestPropNames.add(userParameterName);
		reservedRequestPropNames.add(pwdParameterName);
		reservedRequestPropNames.add(newPwdParameterName);
		reservedRequestPropNames.add(currentLinkedUserCodeParameterName);
		reservedRequestPropNames.add(TRUSTED_SITE_REQ_INDICATOR);
		reservedRequestPropNames.add(BHV_COPY_ABSTRACT_TYPES);
		reservedRequestPropNames.add(BHV_CHECK_CODDESC_FIELDS);
		reservedRequestPropNames.add(BHV_COPY_FIELDS);
		reservedRequestPropNames.add(BHV_READ_REQUEST);
		reservedRequestPropNames.add(BHV_RESET_ERRORS);
		reservedRequestPropNames.add(BHV_RESET_WARNINGS);
		reservedRequestPropNames.add(BHV_RESET_MESSAGES);
		reservedRequestPropNames.add(BHV_GET_XML_MODEL);
		reservedRequestPropNames.add(BHV_FORWARD_AS_XML_MODEL_RESPONSE);
		reservedRequestPropNames.add(BHV_MANAGE_CHANGED_FIELDS);
		reservedRequestPropNames.add(BHV_NEW_MODEL_ON_NEW_THREAD);
		reservedRequestPropNames.add(BHV_CLOSE_BROWSER_INSTANCES_ON_COMMAND);
		reservedRequestPropNames.add(SKIPPABLE_FIELDS);
		reservedRequestPropNames.add(HIDDEN_SUBMIT_ELEMENT_ID);
		reservedRequestPropNames.add(showPageParameterName);
		reservedRequestPropNames.add(showPdfParameterName);
				
	    reservedRequestPropNamesToSanitize.clear();
	    reservedRequestPropNamesToSanitize.add(COMMAND_PARAMETER);
	    reservedRequestPropNamesToSanitize.add(SYSCOMMAND_PARAMETER);
	    reservedRequestPropNamesToSanitize.add(BROWSER_INSTANCE);
	    reservedRequestPropNamesToSanitize.add(countryParameterName);
	    reservedRequestPropNamesToSanitize.add(languageParameterName);
	    reservedRequestPropNamesToSanitize.add(channelParameterName);
	    reservedRequestPropNamesToSanitize.add(startUrlParameterName);
	    reservedRequestPropNamesToSanitize.add(clientIpParameterName);
	    reservedRequestPropNamesToSanitize.add(userParameterName);
	    reservedRequestPropNamesToSanitize.add(pwdParameterName);
	    reservedRequestPropNamesToSanitize.add(newPwdParameterName);
	    reservedRequestPropNamesToSanitize.add(currentLinkedUserCodeParameterName);
		
	    //*********************************************************************************//
	    //*********************************************************************************//
	    //*********************************************************************************//
	
	
	    //*********************************************************************************//
	    //***                     Compatibility parameters                              ***//
	    //*********************************************************************************//
	    if (config.getInitParameter(PARM_SET_URL_PREFIX) != null) {
	        String sUrlPrefix = config.getInitParameter(PARM_SET_URL_PREFIX);
	        if(sUrlPrefix.equalsIgnoreCase("false"))
	        	urlPrefix = false;
	    }
	    LOG.print(s+PARM_SET_URL_PREFIX+": Web resource ["+URL_PREFIX+"] prefixing is setted to: ["+urlPrefix+"]");
	    
	    //*********************************************************************************//
	    //*********************************************************************************//
	    //*********************************************************************************//
	
	    
	    //*********************************************************************************//
	    //***             Url providers. Readed from servelt config and than,           ***//
	    //***                   if not configured, from Configuration                   ***//
	    //*********************************************************************************//
	    if (config.getInitParameter(PARM_PDF_PROVIDER_URL) != null){
	        pdfProviderUrl = config.getInitParameter(PARM_PDF_PROVIDER_URL);
			LOG.print(s+PARM_PDF_PROVIDER_URL+": Pdf provider url is setted to ["+pdfProviderUrl+"]");
	    }else{
	    	pdfProviderUrl = Configuration.getInstance().getPdfProviderUrl();
			LOG.print(s+PARM_PDF_PROVIDER_URL+": Pdf provider url readed from Wfem global configuration and setted to ["+pdfProviderUrl+"]");
	    }
	    //*********************************************************************************//
	    //*********************************************************************************//
	    //*********************************************************************************//
	
	     
	    //*********************************************************************************//
		//*** Transaction management ***/
	    //*********************************************************************************//
	    if (config.getInitParameter(PARM_TRANSACTION_ENABLED) != null) {
	        String sTrEnab = config.getInitParameter(PARM_TRANSACTION_ENABLED);
	        if(sTrEnab.equalsIgnoreCase("true"))
	        	transactionManagementEnabled = true;
	    }
	    LOG.print(s+PARM_TRANSACTION_ENABLED+": Wfem Transaction management on web tier is setted to: ["+transactionManagementEnabled+"]");
	    //*********************************************************************************//
	    //*********************************************************************************//
	    //*********************************************************************************//
	
	    
	    //*********************************************************************************//
		//***                             BPM Tracing                                   ***//
	    //*********************************************************************************//
	    if (config.getInitParameter(PARM_TRACE_DIRECTORY) != null) {
	        traceDirectory = config.getInitParameter(PARM_TRACE_DIRECTORY);
	    }
	    LOG.print(s+PARM_TRACE_DIRECTORY+": BPM Trace directory setted to: ["+traceDirectory+"]");
	    //*********************************************************************************//
	    //*********************************************************************************//
	    //*********************************************************************************//
	
	
	    //*********************************************************************************//
		//***                        Factory and manager classes                        ***//
	    //*********************************************************************************//
	    if (config.getInitParameter(PARM_USER_CONTEXT_FACTORY_CLASS_NAME) != null) {
	        userContextFactoryClassName = config.getInitParameter(PARM_USER_CONTEXT_FACTORY_CLASS_NAME);
		    LOG.print(s+PARM_USER_CONTEXT_FACTORY_CLASS_NAME+": UserSessionContext class factory is setted to: ["+userContextFactoryClassName+"]");
	    }
	    if(userContextFactoryClassName == null){
		    LOG.print(s+PARM_USER_CONTEXT_FACTORY_CLASS_NAME+": UserSessionContext class factory is not configured. UserSessionContext and session check is disabled");
	    }
	
	    if (config.getInitParameter(PARM_LAYOUT_FACTORY_CLASS_NAME) != null) {
	        layoutFactoryClassName = config.getInitParameter(PARM_LAYOUT_FACTORY_CLASS_NAME);
		    LOG.print(s+PARM_LAYOUT_FACTORY_CLASS_NAME+": Layout class factory is setted to: ["+layoutFactoryClassName+"]");
	    }
	    if(layoutFactoryClassName == null){
		    LOG.print(s+PARM_LAYOUT_FACTORY_CLASS_NAME+": Layout class factory is not configured. Dynamic layout generation is managed by WFEM in properties files");
	    }
	
	    if (config.getInitParameter(PARM_LOGIN_MANAGER_CLASS_NAME) != null) {
	        loginManagerClassName = config.getInitParameter(PARM_LOGIN_MANAGER_CLASS_NAME);
		    LOG.print(s+PARM_LOGIN_MANAGER_CLASS_NAME+": LoginManager class is setted to: ["+loginManagerClassName+"]");
	    }
	    if(loginManagerClassName == null){
		    LOG.print(s+PARM_LOGIN_MANAGER_CLASS_NAME+": LoginManager class is not configured. Login is not managed");
	    }
	
	    if (config.getInitParameter(PARM_FIRST_DISPLAY_COMMAND_CLASS_NAME) != null) {
	        firstDisplayCommandClassName = config.getInitParameter(PARM_FIRST_DISPLAY_COMMAND_CLASS_NAME);
		    LOG.print(s+PARM_FIRST_DISPLAY_COMMAND_CLASS_NAME+": First display command after login is setted to: ["+firstDisplayCommandClassName+"]");
	    }
	    if(firstDisplayCommandClassName == null){
		    LOG.print(s+PARM_FIRST_DISPLAY_COMMAND_CLASS_NAME+": First display command is not configured. Login, if managed, is not correctly configured.");
	    }
	
	    //*********************************************************************************//
	    //*********************************************************************************//
	    //*********************************************************************************//
	
	     
	    //*********************************************************************************//
	    //***                                Pages                                      ***// 
	    //*********************************************************************************//
	    if (config.getInitParameter(PARM_ERROR_PAGE) != null) {
	        errorPage = config.getInitParameter(PARM_ERROR_PAGE);
	    }
	    LOG.print(s+PARM_ERROR_PAGE+": Error page is setted to: ["+errorPage+"]");
	    
	    if (config.getInitParameter(PARM_LOGIN_PAGE) != null) {
	        loginPage = config.getInitParameter(PARM_LOGIN_PAGE);
	    }
	    LOG.print(s+PARM_LOGIN_PAGE+": Login page is setted to: ["+loginPage+"]");
	
	    if (config.getInitParameter(PARM_NO_SESSION_PAGE) != null) {
	        noSessionPage = config.getInitParameter(PARM_NO_SESSION_PAGE);
	    }
	    LOG.print(s+PARM_NO_SESSION_PAGE+": No session page is setted to: ["+noSessionPage+"]");
	    
	    if (config.getInitParameter(PARM_CHANGE_PWD_PAGE) != null) {
	        changePwdPage = config.getInitParameter(PARM_CHANGE_PWD_PAGE);
	    }
	    LOG.print(s+PARM_CHANGE_PWD_PAGE+": Change password page is setted to: ["+changePwdPage+"]");
	
	    if (config.getInitParameter(PARM_LOGIN_FAILED_PAGE) != null) {
	        loginFailedPage = config.getInitParameter(PARM_LOGIN_FAILED_PAGE);
	    }
	    LOG.print(s+PARM_LOGIN_FAILED_PAGE+": Login failed page is setted to: ["+loginFailedPage+"]");
	
	    if (config.getInitParameter(PARM_CHANGE_PWD_FAILED_PAGE) != null) {
	        changePwdFailedPage = config.getInitParameter(PARM_CHANGE_PWD_FAILED_PAGE);
	    }
	    LOG.print(s+PARM_CHANGE_PWD_FAILED_PAGE+": Change password failed page is setted to: ["+changePwdFailedPage+"]");
	    //*********************************************************************************//
	    //*********************************************************************************//
	    //*********************************************************************************//
	
	    
	    //*********************************************************************************//
	    //***                             HTML Trace setting                            ***//
	    //*********************************************************************************//
	    if (config.getInitParameter(PARM_SET_HTML_TRACE) != null) {
	        String sHtmlTrace = config.getInitParameter(PARM_SET_HTML_TRACE);
	        if(sHtmlTrace.equalsIgnoreCase("true"))
	        	htmlTrace = true;
	    }
	    LOG.print(s+PARM_SET_HTML_TRACE+": HTML Tracing is setted to: ["+htmlTrace+"]");
	
	    if (config.getInitParameter(PARM_HTML_TRACE_DIR) != null) {
	        htmlTraceDir = config.getInitParameter(PARM_HTML_TRACE_DIR);
	    }
	    LOG.print(s+PARM_HTML_TRACE_DIR+": HTML Trace directory is setted to: ["+htmlTraceDir+"]");
	    //*********************************************************************************//
	    //*********************************************************************************//
	    //*********************************************************************************//

	    //*********************************************************************************//
	    //***                         Caches parameters                                 ***//
	    //*********************************************************************************//
	    if (config.getInitParameter(PARM_COMMANDS_CACHE_ENABLED) != null) {
	        String sCommandsCacheEnabled = config.getInitParameter(PARM_COMMANDS_CACHE_ENABLED);
	        if(sCommandsCacheEnabled.equalsIgnoreCase("false"))
	        	commandsCacheEnabled = false;
	    }
	    LOG.print(s+PARM_COMMANDS_CACHE_ENABLED+": Commands caching is setted to: ["+commandsCacheEnabled+"]");

	    if (config.getInitParameter(PARM_TEMPLATES_CACHE_ENABLED) != null) {
	        String sTemplatesCacheEnabled = config.getInitParameter(PARM_TEMPLATES_CACHE_ENABLED);
	        if(sTemplatesCacheEnabled.equalsIgnoreCase("false"))
	        	templatesCacheEnabled = false;
	    }
	    LOG.print(s+PARM_TEMPLATES_CACHE_ENABLED+": Templates caching is setted to: ["+templatesCacheEnabled+"]");
	    //*********************************************************************************//
	    //*********************************************************************************//
	    //*********************************************************************************//
	    
	    LOG.print("Wfem configuration: Controller parameters readed");
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private void initializeUtility() {
	
		LOG.info("ControllerServlet init: initialization of RemoteObjectFactory");
		RemoteObjectFactory.getInstance();
	
		LOG.info("ControllerServlet init: initialization of ObjectsPropertiesCache");
		ObjectsPropertiesCache.getInstance();
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private Command loadCommand(RequestManager requestManager,
						 	    UserSessionContext userSessionContext,
							    String commandName) throws Exception{
	
	    Class commandClass = null;
	    
		try {	
		    // First check for class in the cache
		    commandClass = (Class) commandCache.get(commandName);
		    if (commandClass == null) {
		        // Locate the command and save it in the cache
		        commandClass = Class.forName(commandName);
		        if(commandsCacheEnabled){
			        LOG.debug("Caching command class of: " + commandName);
			        commandCache.put(commandName, commandClass);
		        }
		    }
		    
	        LOG.debug("Creating new command instance of: " + commandName);
		    Command commandObject = (Command) commandClass.newInstance();
			
			commandObject.toGarbageSetRequest(requestManager.getRequest());
			
			if(commandObject instanceof BusinessCommand){
				LOG.debug("Command is of Business type");
			}else if(commandObject instanceof DisplayCommand){
				LOG.debug("Command is of Display type");
			}else if(commandObject instanceof PrintCommand){
				LOG.debug("Command is of Print type");
			}else if(commandObject instanceof CalcCommand){
				LOG.debug("Command is of alc type");
			}else if(commandObject instanceof PrintFdfCommand){
				LOG.debug("Command is of PrintFdf type");
			}else{
				String errorMsg = "Command  " + commandName + " is of unknwown type";
				Exception e = new Exception(errorMsg);
				throw e;
			}
		    return commandObject;
		        
		}catch(Exception e){
			
			String errorMsg = "Exception loading command " + commandName + ": " + e;
			e = new Exception(errorMsg);
			throw e;
			
		}
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private CommandDataModel loadCommandInputDataModelObject(RequestManager requestManager,
							 			               		 Command command,
							 			               		 Integer browserInstance) throws Exception {
	
	    CommandDataModel commandDataModelObject = null;
	
	    Class commandDataModelClass = command.getInputViewClass();
	    if(commandDataModelClass == null) {
	        LOG.debug("Command "+command.getClass().getName()+" has no input data model");
	    	return null;
	    }
	    
	    String commandDataModelName = commandDataModelClass.getName();

		try {
			Hashtable map = (Hashtable)requestManager.getSession().getAttribute(PERSISTENT_MODELS);
			if(map != null)
				commandDataModelObject = (CommandDataModel)map.get(commandDataModelName);
			if(commandDataModelObject == null) {
		        // Create a new instance of the data model (only if the command has a data model)
		        LOG.debug("Creating new command data model instance: " + commandDataModelName);
		        commandDataModelObject = (CommandDataModel) commandDataModelClass.newInstance();
			}else{
		        LOG.debug("Input data model retrieved as persistent");
			}
	
		    return commandDataModelObject;
		    
		}catch(Exception e){
			String errorMsg = "Exception loading command data model " + commandDataModelName + ": " + e;
			e = new Exception(errorMsg);
			throw e;
	    }
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private CommandDataModel loadCurrentDisplayOutputData(RequestManager requestManager,
							                     		  Integer browserInstance) throws Exception{
						
		CommandsStack stack = getBrowserInstanceStack(requestManager,browserInstance);
		CommandStructure commandStruct = stack.getCurrentCommand();
		if(commandStruct == null)
			return null;
	
		CommandDataModel outputCommandDataModel = commandStruct.getOutput();
	
		LOG.debug("Loading the output of the current display command for browser instance "+browserInstance); 
	
		// If the output is null or in not to save in session
		// do nothing
		if(outputCommandDataModel == null){
			LOG.debug("The output is null. Do nothing");
			return null;
		}
		
		// Save the output
		LOG.debug("Loading the output of display command reading request parameters");
		requestManager.loadCommandDataModelProperties(outputCommandDataModel);
		
		return outputCommandDataModel;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private void manageError(RequestManager requestManager, CommandException e) {
	
		Object xmlResp = requestManager.getAttribute(BHV_FORWARD_AS_XML_MODEL_RESPONSE);
		if(xmlResp != null && "true".equals(xmlResp)){
			try{
				String exresp = "<exception>"+Tools.stringToXMLString(e.getMessage())+"</exception>";
				doNoCacheResponse(requestManager,"text/xml",exresp.getBytes(),true,null);
				return;
			}catch(Throwable t){}
		}
		
		if(requestManager.getResponse().isCommitted())
			return;
		
		try {
			
			if(requestManager.getAttribute(BHV_GET_XML_MODEL) != null){
				RemoteCommandException rce = new RemoteCommandException();
				rce.setException(new StringType(e.toString()));
				String xmlResponse = Tools.xmlFromModel(rce,false,false);
				byte[] bytes = xmlResponse.getBytes();
				doNoCacheResponse(requestManager,"text/xml",bytes,true,null);
				return;				
			}
			
		    Template template = null;
	        try{
		        template = createLayoutTemplate(requestManager,null,null,new Integer(0));
				if(template != null){
					requestManager.setAttribute(TEMPLATE,template);
				}
	        }catch(Exception te){
		        LOG.error(te);
	        }
	        
			String msg = "Managing error: " + e;
			LOG.error(new Exception(msg));
			requestManager.setAttribute("CommandException",e);
	        String p = errorPage;
	        if(urlPrefix)
	        	p = URL_PREFIX + errorPage;
	        RequestDispatcher rd = getServletContext().getRequestDispatcher(p);
	        LOG.debug("Forwarding to error page ["+p+"] on context ["+rd+"]");
	        requestManager.getResponse().setHeader("WHSReplaceAsBody","true");
	        requestManager.getResponse().setHeader("Cache-Control","no-cache, no-store, must-revalidate, max-age=0"); //HTTP 1.1
	        requestManager.getResponse().setHeader("Pragma","no-cache"); //HTTP 1.0
	        requestManager.getResponse().setHeader("Expires", "0"); //prevents caching at the proxy server
	        rd.forward(requestManager.getRequest(),requestManager.getResponse());
		} catch(ServletException se) {
			String errorMsg = "ServletException on managing error: " + se;
			LOG.error(new Exception(errorMsg));
		} catch (Exception ne) {
			String errorMsg = "Exception on managing error: " + ne;
			LOG.error(new Exception(errorMsg));
		}
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private void executeLoginfreeCommands(RequestManager requestManager, int commandCode, Integer browserInstance){
		
		try{
			
			if(resourceSupplierClass == null)
				resourceSupplierClass = Class.forName(Configuration.DEFAULT_RESOURCE_SUPPLIER_CLASS_NAME);
			
			switch(commandCode){
			
				case ManagedCommands.ipingCmd:{
	                requestManager.getResponse().setContentType("text/html");
	                requestManager.getResponse().setContentLength(0);
					try{
						BkRemoteObjectFactory.getInstance().getManager(new ClientSessionContext(),XmlServiceLoggerManager.class);
						requestManager.getResponse().setStatus(HttpServletResponse.SC_OK);
					}catch(Throwable t){
						requestManager.getResponse().setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
					}
					return;
				}
				case ManagedCommands.ikeepAliveCmd:{
					if(browserInstance != 0 && browserInstance != 1){
						CommandsStack stack = getBrowserInstanceStack(requestManager,browserInstance);
						if(stack != null)
							stack.resetSleepTimeMinutes();
					}
					requestManager.getResponse().setStatus(HttpServletResponse.SC_NO_CONTENT);
					return;
				}
				case ManagedCommands.iShowErrorsPageCmd:{
	                StringBuffer result = new StringBuffer("<table border=1 style='font-family: Arial;font-size:8pt;color:#1A458F;'>");
	        		CommandsStack stack = getBrowserInstanceStack(requestManager,browserInstance);
	        		CommandStructure commandStruct = stack.getCurrentCommand();
	                if(commandStruct == null)
	                {
	                    result.append("<tr><td>There is no current display command. Verify application flow</td></tr>");
	                } else
	                {
	                    CommandDataModel outputCommandDataModel = commandStruct.getOutput();
	                    if(outputCommandDataModel == null)
	                    {
	                        result.append("<tr><td>Output model is null</td></tr>");
	                    } else
	                    {
	                        result.append("<tr><td colspan='2' style='background-color:blue;color:azure;'>" + outputCommandDataModel + "</td></tr>");
	                        ArrayList fields = Tools.getAbstractTypeFieldNames(outputCommandDataModel);
	                        boolean firstTime = true;
	                        for(int i = 0; i < fields.size(); i++)
	                        {
	                            String fieldName = (String)fields.get(i);
	                            AbstractType field = (AbstractType)Tools.getPropertyValue(outputCommandDataModel, fieldName);
	                            if(field.hasTypeErrors())
	                            {
	                                if(firstTime)
	                                {
	                                    firstTime = false;
	                                    result.append("<tr><td>" + outputCommandDataModel + "</td><td>&nbsp;</td></tr>");
	                                }
	                                result.append("<tr><td>&nbsp;</td><td style='background-color:red;color:white;'>" + fieldName + "</td></tr>");
	                            }
	                            if(field.hasTypeWarnings())
	                            {
	                                if(firstTime)
	                                {
	                                    firstTime = false;
	                                    result.append("<tr><td>" + outputCommandDataModel + "</td><td>&nbsp;</td></tr>");
	                                }
	                                result.append("<tr><td>&nbsp;</td><td style='background-color:yellow;color:black;'>" + fieldName + "</td></tr>");
	                            }
	                        }

	                        ArrayList models = Tools.getInnerCommandDataModelList(outputCommandDataModel);
	                        for(int i = 0; i < models.size(); i++)
	                        {
	                            CommandDataModel model = (CommandDataModel)models.get(i);
	                            fields = Tools.getAbstractTypeFieldNames(model);
	                            firstTime = true;
	                            for(int j = 0; j < fields.size(); j++)
	                            {
	                                String fieldName = (String)fields.get(j);
	                                AbstractType field = (AbstractType)Tools.getPropertyValue(model, fieldName);
	                                if(field.hasTypeErrors())
	                                {
	                                    if(firstTime)
	                                    {
	                                        firstTime = false;
	                                        result.append("<tr><td>" + model + "</td><td>&nbsp;</td></tr>");
	                                    }
	                                    result.append("<tr><td>&nbsp;</td><td style='background-color:red;color:white;'>" + fieldName + "</td></tr>");
	                                }
	                                if(field.hasTypeWarnings())
	                                {
	                                    if(firstTime)
	                                    {
	                                        firstTime = false;
	                                        result.append("<tr><td>" + model + "</td><td>&nbsp;</td></tr>");
	                                    }
	                                    result.append("<tr><td>&nbsp;</td><td style='background-color:yellow;color:black;'>" + fieldName + "</td></tr>");
	                                }
	                            }

	                        }

	                    }
	                }
	                result.append("</table>");
	                PrintWriter out = requestManager.getResponse().getWriter();
	                requestManager.getResponse().setContentType("text/html");
	                requestManager.getResponse().setContentLength(result.length());
	                out.print(result.toString());
	                out.flush();
	                return;
				}
				
				case ManagedCommands.igetScriptCmd:{
					String fileName = (String)requestManager.getAttribute(FILE_NAME_PARAMETER);				
					if(fileName != null ){
						ResourceSupplier supplier = (ResourceSupplier)resourceSupplierClass.newInstance();
						String langCode = (String)requestManager.getSession().getAttribute(languageParameterName);					
						supplier.init(langCode,this,requestManager); 
						long scriptDate = supplier.getScriptDate(fileName);
						if(!supplier.checkHeader(requestManager.getRequest(),requestManager.getResponse(),scriptDate))
							return;
						PrintWriter out = requestManager.getResponse().getWriter();
						String script = supplier.getScript(fileName);
						requestManager.getResponse().addDateHeader("Last-Modified", scriptDate);	
						requestManager.getResponse().setContentType("text/javascript");
						requestManager.getResponse().setContentLength(script.length());
						requestManager.getResponse().setBufferSize(script.length());
						out.print(script);
						out.flush();
					}
					return;
				}
					
				case ManagedCommands.igetCssCmd:{
					ResourceSupplier supplier = (ResourceSupplier)resourceSupplierClass.newInstance();
					String langCode = (String)requestManager.getSession().getAttribute(languageParameterName);					
					supplier.init(langCode,this,requestManager); 
					long cssDate = supplier.getCssDate();
					if(!supplier.checkHeader(requestManager.getRequest(),requestManager.getResponse(),cssDate))
						return;
					PrintWriter out = requestManager.getResponse().getWriter();
					String css = supplier.getCss();
					requestManager.getResponse().addDateHeader("Last-Modified", cssDate);	
					requestManager.getResponse().setContentType("text/css");
					requestManager.getResponse().setContentLength(css.length());		
					out.print(css);
					out.flush();
					return;
				}
					
				case ManagedCommands.igetImageCmd:{
					String fileName = (String)requestManager.getAttribute(FILE_NAME_PARAMETER);
					if(fileName != null ){
						ResourceSupplier supplier = (ResourceSupplier)resourceSupplierClass.newInstance();
						String langCode = (String)requestManager.getSession().getAttribute(languageParameterName);					
						supplier.init(langCode,this,requestManager); 
						long imageDate = supplier.getImageDate(fileName);
						if(!supplier.checkHeader(requestManager.getRequest(),requestManager.getResponse(),imageDate))
							return;
						DataOutputStream out = new DataOutputStream(requestManager.getResponse().getOutputStream());
						byte[] imageData = supplier.getImage(fileName);
						requestManager.getResponse().addDateHeader("Last-Modified", imageDate);	
						requestManager.getResponse().setContentType("image/gif");
						if(imageData != null) {
							requestManager.getResponse().setContentLength(imageData.length);		
							out.write(imageData);
							out.flush();
						}else
							requestManager.getResponse().setContentLength(0);		
					}
					return;
				}
					
				case ManagedCommands.igetWebResourceCmd:{
					String fileName = (String)requestManager.getAttribute(FILE_NAME_PARAMETER);
					if(fileName != null){
						ResourceSupplier supplier = (ResourceSupplier)resourceSupplierClass.newInstance();
						String langCode = (String)requestManager.getSession().getAttribute(languageParameterName);					
						supplier.init(langCode,this,requestManager); 
						long resourceDate = supplier.getResourceDate(fileName);
						if(!supplier.checkHeader(requestManager.getRequest(),requestManager.getResponse(),resourceDate))
							return;
						PrintWriter out = requestManager.getResponse().getWriter();
						String resource = supplier.getResource(fileName);
						requestManager.getResponse().addDateHeader("Last-Modified", resourceDate);	
						requestManager.getResponse().setContentType("text/html");
						requestManager.getResponse().setContentLength(resource.length());		
						out.print(resource);
						out.flush();
					}
					return;
				}
					
				case ManagedCommands.igetBlankPageCmd:{
					ResourceSupplier supplier = (ResourceSupplier)resourceSupplierClass.newInstance();
					String langCode = (String)requestManager.getSession().getAttribute(languageParameterName);					
					supplier.init(langCode,this,requestManager); 
					long pageDate = supplier.getBlankPageDate();
					if(!supplier.checkHeader(requestManager.getRequest(),requestManager.getResponse(),pageDate))
						return;
					PrintWriter out = requestManager.getResponse().getWriter();
					String page = supplier.getBlankPage();
					requestManager.getResponse().addDateHeader("Last-Modified", pageDate);	
					requestManager.getResponse().setContentType("text/html");
					requestManager.getResponse().setContentLength(page.length());		
					out.print(page);
					out.flush();
					return;
				}
					
				case ManagedCommands.igetChartCmd:{
					try{
						String chartID = (String)requestManager.getAttribute(CHART_ID_PARAMETER);
						if(chartID != null){
							ChartParameters chartParameters = (ChartParameters)requestManager.getSession().getAttribute(chartID);
							if(chartParameters == null){
								requestManager.getResponse().setStatus(HttpServletResponse.SC_NO_CONTENT);
								return;
							}
							Class chartClass = Class.forName("com.atosorigin.wfem.charts."+chartParameters.getType()+"Chart");
							AbstractChart chart = (AbstractChart)chartClass.newInstance();
							chart.setChartParameters(chartParameters);
							chart.setLanguage(requestManager.getLanguage());
							requestManager.getSession().removeAttribute(chartID);
							ByteArrayOutputStream imageData = chart.createChartAsOutputStream();
							chart = null;
							if(imageData != null) {
								requestManager.getResponse().setContentType("image/png");
								requestManager.getResponse().setContentLength(imageData.size());		
								DataOutputStream out = new DataOutputStream(requestManager.getResponse().getOutputStream());
								out.write(imageData.toByteArray());
								out.flush();
							}else
								requestManager.getResponse().setStatus(HttpServletResponse.SC_NO_CONTENT);
						}else{
							requestManager.getResponse().setStatus(HttpServletResponse.SC_NO_CONTENT);					
						}
					}catch(Exception e){
						requestManager.getResponse().setStatus(HttpServletResponse.SC_NO_CONTENT);
					}
					return;
				}
					
				case ManagedCommands.ishowLoginCmd:{
					showLogin(requestManager);
					return;
				}
					
				case ManagedCommands.iexecuteLoginCmd:{
					// Paremeter in request for login command are:
					//		channel
					//		user
					//		password
					String user     = (String)requestManager.getAttribute(userParameterName);
					String password = (String)requestManager.getAttribute(pwdParameterName);
					// This method also create the UserSessionContext
					LOG.info("Excute Login for user ["+user+"]");
					executeLogin(requestManager,user,password,browserInstance);
					return;
				}
					
				case ManagedCommands.iexecuteChangePwdCmd:{
					// Paremeter in request for change password command are:
					//		channel
					//		user
					//		password
					//		newpassword
					String user        = (String)requestManager.getSession().getAttribute(userParameterName);
					String password    = (String)requestManager.getAttribute(pwdParameterName);
					String newPassword = (String)requestManager.getAttribute(newPwdParameterName);
					LOG.info("Excute Change Password for user ["+user+"]");
					executeChangePassword(requestManager,user,password,newPassword,browserInstance);
					return;
				}
			}
			
		}catch(Exception e){
			manageError(requestManager,new CommandException(e.toString()));
		}
		return;
		
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private String manageOAMHeader(RequestManager requestManager){
		
		String wfemHttpContext = requestManager.getRequest().getHeader("BMED_CTX_PARAMS");
		if(wfemHttpContext != null){
			wfemHttpContext = wfemHttpContext.trim();
			if(wfemHttpContext.length() == 0){
			    return "Error on Context on HTTP Header: variable exist but is empty";
			}else{
				LOG.debug("Context is setted in header variable. Value ["+wfemHttpContext+"]");
				try{
					String[] httpCtxPars = wfemHttpContext.split("\\|");
					
					String country = httpCtxPars[0].trim(); if(country.length()==0  || country.equalsIgnoreCase("NULL"))  country = "ITA";
					String channel = httpCtxPars[1].trim();	if(channel.length()==0  || channel.equalsIgnoreCase("NULL"))  channel = "P";
					String language = httpCtxPars[2].trim();if(language.length()==0 || language.equalsIgnoreCase("NULL")) language = "IT";
					String clientip = httpCtxPars[3].trim();
					String loginname = httpCtxPars[4].trim();
					String cluser = httpCtxPars[5].trim();
					
					// #151356 - Copernico mobile: clientip letto da standard header se non trovato in BMED_CTX_PARAMS
					if(clientip.length() == 0)
						clientip = requestManager.getRequest().getHeader("X-Forwarded-For");
					if(clientip == null)
						clientip = "";
					
					requestManager.setAttribute(countryParameterName, country);
					requestManager.setAttribute(channelParameterName, channel);
					requestManager.setAttribute(languageParameterName, language);
					requestManager.setAttribute(clientIpParameterName, clientip);
					requestManager.setAttribute(loginNameParameterName, loginname);
					requestManager.setAttribute(currentLinkedUserCodeParameterName, cluser);
					
					requestManager.setAttribute(TRUSTED_SITE_REQ_INDICATOR,"true");
					
				}catch(Throwable t){
				    return "Exception managing Context on HTTP Header: ["+wfemHttpContext+"]";
				}
			}
		}else if(Configuration.getInstance().isAcceptCtxOnlyOnHeader()){
		    return "Context not found";
		}
		return null;
	}	
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private void performTask(HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		LOG.debug("performTask() on session ["+request.getSession().getId()+"]");

		boolean browserInstanceFound = false;
		RequestManager requestManager = new RequestManager(request, response, reservedRequestPropNames, reservedRequestPropNamesToSanitize);

		String oamErrorMsg = manageOAMHeader(requestManager);
		if(oamErrorMsg != null){
	        manageError(requestManager,new CommandException(oamErrorMsg));
	        return;
		}
		
		try{
			browserInstanceFound = requestManager.parseRequest();
		}catch(SizeLimitExceededException slee){
			long size = slee.getPermittedSize();
			String measure = "Kb";
			long kb = (int)size/1024;
			if(kb >= 1000){
				measure = "Mb";
				kb = (int)size/(1024*1000);
				if(kb >= 1000000){
					measure = "Gb";
					kb = (int)size/(1024*1000000);					
				}
			}
			measure = ""+kb+" "+measure;
			PrintWriter out = requestManager.getResponse().getWriter();
			String page = "<script>parent.sizeErrorLoadFileType('"+measure+"');</script>";
			requestManager.getResponse().setContentType("text/html");
			requestManager.getResponse().setContentLength(page.length());		
			out.print(page);
			out.flush();
			return;
		}
		catch(IntrusionException e){
			manageError(requestManager,new CommandException(e.toString()));
			return;
		}
		
		if(!controllerInitialized){
		    String errorMsg = "Error on controller invocaton: controller servlet not properly initialized";
	        manageError(requestManager,new CommandException(errorMsg));
	        return;
		}
		
		// Manage the possibility to have parametric command
		// First figure out the command name and/or type from the request parameter
		String commandName = null;
		if(requestManager.getAttribute(SYSCOMMAND_PARAMETER) != null){
			
		    LOG.info("SysCommand is in ["+SYSCOMMAND_PARAMETER+"] parameter");
	
		    String cmd = (String)requestManager.getAttribute(SYSCOMMAND_PARAMETER);
		    LOG.info("SysCommand value is ["+cmd+"]");
		    
			if(cmd.equals(startProcessCommand)) {
			    String procCmd = (String)requestManager.getAttribute(COMMAND_PARAMETER);
				requestManager.setAttribute(COMMAND_PARAMETER,procCmd+".executeProcess");
			}else{
				String errorMsg = "Sytstem Command is not valid";
				manageError(requestManager,new CommandException(errorMsg));
				return;
			}
		}

		if(request.getServletPath() != null && request.getServletPath().startsWith("/"+BoInteraction.IMAGE_CALLBACK)){
			String docRef = request.getParameter(BoInteraction.IMAGE_DOCREF_HOLDER);
			String imageName = request.getParameter(BoInteraction.IMAGE_NAME_HOLDER);
			Image boImage = BoInteraction.getReportImage(requestManager,docRef,imageName);
			if(boImage == null){
				requestManager.getResponse().setStatus(HttpServletResponse.SC_NO_CONTENT);
				return;
			}
			response.setContentType(boImage.getMimeType());
			InputStream is = new ByteArrayInputStream(boImage.getContent());
	        flushInputStream(is,boImage.getContent().length,requestManager);
			return;
		}
		
		if(requestManager.getAttribute(COMMAND_PARAMETER) == null){
			String errorMsg = "Command not found in ["+COMMAND_PARAMETER+"] parameter";
			manageError(requestManager,new CommandException(errorMsg));
			return;
		}

		// Finding command type and command code
		String commandType = null;
		int commandCode = -1;
		
	    String cmd = (String)requestManager.getAttribute(COMMAND_PARAMETER);
	    int idx = cmd.lastIndexOf(".");
	    if(idx >= 0)
	    	commandName = cmd.substring(0,idx);
	    
	    idx = cmd.lastIndexOf(".");
	    if(idx >= 0)
	    	commandType = cmd.substring(idx+1);
	    else
	    	commandType = cmd;
	    commandCode = ManagedCommands.getCommandCode(commandType);

		// If login command remove all parameters from session
		if(commandCode == ManagedCommands.iexecuteLoginCmd){
			Vector sessionParsNames = new Vector();
			Enumeration sessionPars = requestManager.getSession().getAttributeNames();
			while(sessionPars.hasMoreElements()){
				String sessionParName = (String)sessionPars.nextElement();
				sessionParsNames.add(sessionParName);
			}
			for(int i=0;i<sessionParsNames.size();i++)
				requestManager.getSession().removeAttribute((String)sessionParsNames.get(i));
		}

		boolean isLoginFreeCommand = ManagedCommands.isLoginfreeCommand(commandCode);
		
		// Set the initial Country, Channel and Language parameters
		// if present
		if(!saveInitialRequestParameters(requestManager,isLoginFreeCommand))
			return;
	
		if(!isLoginFreeCommand && !browserInstanceFound){
			String msg = "\n************************************************************************\n"+
						 "  ATTENTION: Missing BrowserInstance in request.\n"+
						 "  Wfem command: ["+cmd+"]\n"+
						 "************************************************************************\n";
			System.err.println(msg);
		}
		
	    LOG.debug("Command value is ["+cmd+"] - Command name is: [" + commandName + "] - Command type is: [" + commandType + "] width code ["+commandCode+"]");

	    // Manage the browser instance
	    String broswerInstancePar = (String)requestManager.getAttribute(BROWSER_INSTANCE);
	    if(broswerInstancePar == null || broswerInstancePar.equals("0")) {
	    	broswerInstancePar = (String)requestManager.getSession().getAttribute(SESS_BROWSER_INSTANCE);
	    	if(broswerInstancePar == null) {
	    		broswerInstancePar = BrowserInstanceGenerator.generateUniqueBrowserInstance(requestManager).toString();
	    		requestManager.getSession().setAttribute(SESS_BROWSER_INSTANCE, broswerInstancePar);
	    	}
	    	requestManager.setAttribute(BROWSER_INSTANCE, broswerInstancePar);
	    }
	    Integer browserInstance = new Integer(broswerInstancePar);
	    if(!isLoginFreeCommand)
	    	LOG.debug("Browser Instance is: " + browserInstance);
	    
		// Manage the commands that can be executed 
		// even if the user is not logged
	    if(isLoginFreeCommand){
	    	executeLoginfreeCommands(requestManager,commandCode,browserInstance);
	    	return;
	    }
	
		// Get the user session context	
		UserSessionContext userSessionContext = (UserSessionContext)requestManager.getSession().getAttribute(USER_SESSION_CONTEXT);
		LOG.debug("PerformTask: UserSessionContext in session is setted to ["+userSessionContext+"]");
		
	    String trusted = (String)request.getAttribute(TRUSTED_SITE_REQ_INDICATOR);
	    if(trusted == null)
		    trusted = (String)requestManager.getSession().getAttribute("trustedSite");
	    if(Configuration.getInstance().isTrustedSite() && trusted != null && trusted.equals("true")){ // On trusted site no logon is required
	    	
		    String reqUser = null;
		    String reqLoginname = null;
		    
	    	String country = (String)request.getAttribute(countryParameterName);
		    if(country == null)
		    	country = (String)requestManager.getSession().getAttribute(countryParameterName);
	    	String channel = (String)request.getAttribute(channelParameterName);
		    if(channel == null)
		    	channel = (String)requestManager.getSession().getAttribute(channelParameterName);
	    	String language = (String)request.getAttribute(languageParameterName);
		    if(language == null)
		    	language = (String)requestManager.getSession().getAttribute(languageParameterName);
		    String user = (String)request.getAttribute(userParameterName);
		    if(user == null)
		    	user = (String)requestManager.getSession().getAttribute(userParameterName);
		    else
		    	reqUser = user;
		    String loginname = (String)request.getAttribute(loginNameParameterName);
		    if(loginname == null)
		    	loginname = (String)requestManager.getSession().getAttribute(loginNameParameterName);
		    else
		    	reqLoginname = loginname;
		    String cluser = (String)request.getAttribute(currentLinkedUserCodeParameterName);
		    if(cluser == null)
		    	cluser = (String)requestManager.getSession().getAttribute(currentLinkedUserCodeParameterName);
		    String clientip = (String)request.getAttribute(clientIpParameterName);
		    if(clientip == null)
		    	clientip = (String)requestManager.getSession().getAttribute(clientIpParameterName);

		    if(userSessionContext == null){
				try{
					userSessionContext = createInitialUserSessionContext(requestManager,user);
					userSessionContext = createUserSessionContext(requestManager,userSessionContext,false);
				}catch(Exception e){
					manageError(requestManager,new CommandException(e.getMessage()));
					return;
				}
			}

		    
    		String userType = userSessionContext.getUserType();
		    
		    if(reqUser != null){
		    	
		    	if(loginname == null  || !reqUser.equalsIgnoreCase(userSessionContext.getUserCode())){
		    		
		    		String[] logRes = UserContextDataLoader.loadLoginNameAndRolesForUser(userSessionContext, reqUser);
		    		loginname = logRes[0];
		    		userType = logRes[1];
		    		user = logRes[2];
		    		
		    	}
		    	
		    }else if(reqLoginname != null){
		    	
		    	if(user == null  || !reqLoginname.equalsIgnoreCase(userSessionContext.getLoginName())){
			    	
		    		String[] logRes = UserContextDataLoader.loadUserAndRolesForLoginName(userSessionContext,reqLoginname);
		    		loginname = logRes[0];
		    		userType = logRes[1];
		    		user = logRes[2];
		    		
		    	}
		    	
		    }
		    
		    requestManager.getSession().setAttribute("trustedSite",trusted);

		    requestManager.getSession().setAttribute(countryParameterName,country);
		    requestManager.getSession().setAttribute(channelParameterName,channel);
		    requestManager.getSession().setAttribute(languageParameterName,language);
		    requestManager.getSession().setAttribute(userParameterName,user);
		    requestManager.getSession().setAttribute(loginNameParameterName,loginname);
		    requestManager.getSession().setAttribute(currentLinkedUserCodeParameterName,cluser);
		    
		    userSessionContext.setCountryCode(country);
		    userSessionContext.getClientSessionContext().setCountryCode(country);
		    userSessionContext.setChannelCode(channel);
		    userSessionContext.getClientSessionContext().setChannelCode(channel);
		    userSessionContext.setLangCode(language);
		    userSessionContext.getClientSessionContext().setLangCode(language);
		    userSessionContext.setUserCode(user);
		    userSessionContext.getClientSessionContext().setUserCode(user);
		    userSessionContext.setLoginName(loginname);
		    userSessionContext.getClientSessionContext().setLoginName(loginname);
		    userSessionContext.setCurrentLinkedUserCode(cluser);
		    userSessionContext.getClientSessionContext().setCurrentLinkedUserCode(cluser);
		    userSessionContext.setClientIp(clientip);
		    userSessionContext.getClientSessionContext().setClientIp(clientip);
		    userSessionContext.setUserType(userType);
		    userSessionContext.getClientSessionContext().setUserType(userType);
		    
	    }else{
	    	
		    if(userSessionContext != null){
		    	String linkedUser = userSessionContext.getCurrentLinkedUserCode();
				LOG.debug("PerformTask: Setting current linked user code ["+linkedUser+"] into ClientSessionContext");
			    userSessionContext.getClientSessionContext().setCurrentLinkedUserCode(linkedUser);
		    }
				
			//*****************************************************************//	
			//**    Just to manage a configuration with UserSessionContext   **//
			//**    and no LoginManager                                      **//
			//*****************************************************************//	
			if(loginManagerClassName == null && userSessionContext == null){
				String user    = (String)requestManager.getSession().getAttribute(userParameterName);
				if(user == null){
					user = (String)requestManager.getAttribute(userParameterName);
					if(user != null)
						requestManager.getSession().setAttribute(userParameterName, user);
				}
				
				try{
					userSessionContext = createInitialUserSessionContext(requestManager,user);
					userSessionContext = createUserSessionContext(requestManager,userSessionContext,true);
				}catch(Exception e){
					manageError(requestManager,new CommandException(e.getMessage()));
					return;
				}
			}
			
			//****************************************************************//	
			// If the userSessionContext is not setted the user is no logged  // 
			// or the session is invalidated								  //
			//****************************************************************//	
			 if(userSessionContext == null){
				// If aggiunto da Fabrizio Fantasia per gestione sesione persistente - INIZIO
	            if(requestManager.getSession().getAttribute("LOGGED_USER") != null  && 
	               requestManager.getSession().getAttribute("LOGGED_USER") != "false"){
	                String user = (String)requestManager.getSession().getAttribute(userParameterName);
	                try{
	                    userSessionContext = createInitialUserSessionContext(requestManager,user);
	                    userSessionContext = createUserSessionContext(requestManager,userSessionContext,true);
	                    if(userSessionContext != null){
	                        String linkedUser = userSessionContext.getCurrentLinkedUserCode();
	                        LOG.debug("PerformTask: Setting current linked user code ["+linkedUser+"] into ClientSessionContext");
	                        userSessionContext.getClientSessionContext().setCurrentLinkedUserCode(linkedUser);
	                    }
	                }catch(Exception e){
	                    manageError(requestManager,new CommandException(e.getMessage()));
	                    return;
	                }
	   			// If aggiunto da Fabrizio Fantasia per gestione sesione persistente - FINE
	            }else{
	            	LOG.info("SESSION EXPIRED - HttpSession:["+requestManager.getSession().getId()+"] ClientSession:["+requestManager.getRequest().getRequestedSessionId()+"]");
	    	        requestManager.getResponse().setHeader("WHSReplaceAsBody","true");
	                forwardPage(requestManager,noSessionPage,null,browserInstance);    
	                return;
	            }
			}
			 
	    }
		
		//*****************************************************************//	
	    // reset [changed] flag of UserSessionContext before executing command
		//*****************************************************************//	
	    if ( userSessionContext != null ) {
	    	userSessionContext.setChanged(false);
	    	LOG.debug("PerformTask: resetting [changed] flag of UserSessionContext before executing command");
	    }
		    
		//*****************************************************************//	
		//**	Closing threads if specified in command			     	 **//
		//*****************************************************************//
	    String threadsPar = (String)requestManager.getAttribute(BHV_CLOSE_BROWSER_INSTANCES_ON_COMMAND);
	    if(threadsPar != null && threadsPar.length() > 0){
		    String sessBrowserInstance = (String)requestManager.getSession().getAttribute(SESS_BROWSER_INSTANCE);
	    	String[] t = threadsPar.split(",");
	    	for(int i=0;i<t.length;i++){
				if(sessBrowserInstance != null && t[i].equals(sessBrowserInstance)){
					LOG.warning("Attention !!! Attempt to close reserved Browser Instance ["+t[i]+"]");
					continue;
				}
				LOG.info("Closing Browser Instance ["+t[i]+"] on Command:["+commandName+"] Type:["+commandType+"]");
				requestManager.getSession().removeAttribute(WFEM_COMMAND_STACK_CONTEXT+t[i]);
	    	}
	    }
	    
		//*****************************************************************//	
		//**                      Executing command   				     **//
		//*****************************************************************//	
		try{
	
			String msg = "Executing Command:["+commandName+"] "+
						 "Type:["+commandType+"] "+
						 "HttpSession:["+requestManager.getSession().getId()+"] "+
						 "ClientSession:["+requestManager.getRequest().getRequestedSessionId()+"] "+
						 "Country:["+userSessionContext.getCountryCode()+"] "+
				         "Channel:["+userSessionContext.getChannelCode()+"] "+
				         "User:["+userSessionContext.getUserCode()+"] "+
				         "Linked User Code:["+userSessionContext.getCurrentLinkedUserCode()+"] "+
				         "Browser Instance ";
			
			switch(commandCode){
			
				case ManagedCommands.iexecuteBoReport:{
					String docName = (String)requestManager.getAttribute(BoInteraction.BO_DOC_NAME);
					if(docName == null){
						docName = (String)requestManager.getAttribute(BoInteraction.BO_DOC_NAME2);
						if(docName == null){
							String ret = "<html><body><script>try{parent.endBOReport();}catch(e){}\n</script><table><tr><td>You must specify a BO Report doc name in ["+BoInteraction.BO_DOC_NAME+"] parameter</td></tr></table></body></html>";
							requestManager.getResponse().setContentType("text/html");
							InputStream is = new ByteArrayInputStream(ret.getBytes());
					        flushInputStream(is,ret.length(),requestManager);
					        return;
						}
					}
					
					BoParameters boPars = new BoParameters();
					String useParNameAsIndex = (String)requestManager.getAttribute("useParNameAsIndex");
					if(useParNameAsIndex != null && useParNameAsIndex.equals("true")){
						boPars.setUseParNameAsIndex(true);
						for(int i=1;i<51;i++){
							String parValue = (String)requestManager.getAttribute(BoInteraction.BO_DOC_PARS+"P"+i);
							if(parValue == null)
								continue;
			    			parValue = parValue.trim();
							boPars.add(new BoParameter(null, parValue));
						}
					}else{
						boPars.setUseParNameAsIndex(false);
						Enumeration httpPars = requestManager.getRequest().getParameterNames();
						while(httpPars.hasMoreElements()){
							String parName = (String)httpPars.nextElement();
							if(!parName.startsWith(BoInteraction.BO_DOC_PARS))
								continue;
							String parValue;
				    		try{
				    			parValue = requestManager.getRequest().getParameter(parName);
				    		}catch(ClassCastException cce){
				    			continue;
				    		}
				    		if(parValue != null){
				    			parValue = parValue.trim();
				    			boPars.add(new BoParameter(parName.substring(BoInteraction.BO_DOC_PARS.length()), parValue));
				    		}
						}
					}

					String outType = (String)requestManager.getAttribute("type");
					if(outType == null){
						outType = (String)requestManager.getAttribute(BoInteraction.BO_DOC_TYPE);
						if( outType == null || !outType.equalsIgnoreCase(BoInteraction.PDF_OUT_TYPE))
							outType = BoInteraction.HTML_OUT_TYPE;
					}
					
					boolean showback = false;
					String showbackPar = (String)requestManager.getAttribute("showback");
					if(showbackPar != null)
						showback = Boolean.parseBoolean(showbackPar);
					
					String characterEncoding = (String)requestManager.getAttribute("encoding");
					if(characterEncoding == null)
						characterEncoding = requestManager.getResponse().getCharacterEncoding();
					else if(characterEncoding.equalsIgnoreCase("default"))
						characterEncoding = null;

					TierAccessLoggerInfo tli = ClientTierAccessLogger.initTier(requestManager,userSessionContext.getClientSessionContext(),
							   												   "wfemCmd@executeBoReport",true);
					BoReportInfo info = BoInteraction.executeReport(requestManager,userSessionContext.getClientSessionContext(),
																	docName,boPars,outType,showback,characterEncoding);
					tli.stop(requestManager.getClientTierInputParameters());
					
					byte[] docByte = null;
					if(info.result != BoReportInfo.RESULT_OK){
						String errorMsg = BoInteraction.htmlBoErrorMsg(request, info);
						docByte = errorMsg.getBytes(requestManager.getResponse().getCharacterEncoding());
						if(outType.equals(BoInteraction.PDF_OUT_TYPE)){
							ByteArrayOutputStream responseOutput = new ByteArrayOutputStream();
							responseOutput.write(docByte);
							DocumentController doc = new DocumentController(responseOutput,getServletContext(),requestManager,null);
							responseOutput = (ByteArrayOutputStream)doc.generatePdfDocument();
							docByte = responseOutput.toByteArray();
						}else{
						}
					}else{
						docByte = info.content;
					}
					
					
					if(docByte == null){
						String ret = "<html><body><script>try{parent.endBOReport();}catch(e){}\n</script><table><tr><td>Report ["+docName+"] not found</td></tr></table></body></html>";
						requestManager.getResponse().setContentType("text/html");
						InputStream is = new ByteArrayInputStream(ret.getBytes());
				        flushInputStream(is,ret.length(),requestManager);
						return;
					}

					String mimeType = info.mimeType;
					if(outType.equals(BoInteraction.PDF_OUT_TYPE)){
						mimeType = "application/pdf";
					}else if(outType.equals(BoInteraction.XLS_OUT_TYPE)){
						mimeType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
					}else if(outType.equals(BoInteraction.HTML_OUT_TYPE)){
						mimeType = "text/html";
					}
					
					requestManager.getResponse().setContentType(mimeType);
					
					InputStream is = new ByteArrayInputStream(docByte);
					
					String asDownload = (String)requestManager.getAttribute("download");
					if(asDownload != null && asDownload.equalsIgnoreCase("true")){
						String fileExt = "";
						if(mimeType.equalsIgnoreCase("application/pdf"))
							fileExt = "pdf";
						else if(mimeType.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
							fileExt = "xlsx";
						else if(mimeType.equalsIgnoreCase("text/html"))
							fileExt = "html";
						requestManager.getResponse().setHeader("Content-Disposition", "attachment; filename=\""+docName+"."+fileExt+"\"");
						requestManager.getResponse().setHeader("content-length",""+docByte.length);
						requestManager.getResponse().setHeader("file-name",docName+"."+fileExt);
					}
					
			        flushInputStream(is,docByte.length,requestManager);
					return;
				}
				
				case ManagedCommands.istreamRemoteFile:{
					
					FileStreamer fileStreamer = new FileStreamer();
					String fileServerName = (String)requestManager.getAttribute("server");
					if(fileServerName == null || fileServerName.length() == 0)
						fileServerName = "BO";
					String fileName = (String)requestManager.getAttribute("filename");
					FileStreamerInfo info = fileStreamer.getRemoteFileStream(fileServerName,fileName);
					if(info.getHttpResult() < 0){
						String errorMsg = "Exception calling FileStreamer: "+info.getErrorMessage();
						Exception e = new Exception(errorMsg);
						throw e;
					}
					if(info.getHttpResult() != HttpServletResponse.SC_OK){
						String errorMsg = "HTTP error calling FileStreamer: "+info.getHttpResult();
						Exception e = new Exception(errorMsg);
						throw e;
					}
					
					if(!info.isFileExist()){
		                StringBuffer result = new StringBuffer("<table border=1 style='font-family:Arial;font-size:10pt;color:#1A458F;'>");
	                    result.append("<tr><td>File ["+fileName+"] not found</td></tr>");
		                result.append("</table>");
		                result.append("<script>alert(\"File or Directory ["+fileName+"] not found\");</script>");
		                PrintWriter out = requestManager.getResponse().getWriter();
		                requestManager.getResponse().setContentType("text/html");
		                requestManager.getResponse().setContentLength(result.length());
		                out.print(result.toString());
		                out.flush();
						return;
					}
					
					if(info.isDirectory()){
						
						requestManager.getResponse().setContentType("text/xml");
						
					}else{
						
						String fileExt = fileName.substring(fileName.lastIndexOf('.')+1);
						if(fileExt.equalsIgnoreCase("pdf"))
							requestManager.getResponse().setContentType("application/pdf");
						else if(fileExt.equalsIgnoreCase("xls"))
							requestManager.getResponse().setContentType("application/vnd.ms-excel");
						else if(fileExt.equalsIgnoreCase("xlsx"))
							requestManager.getResponse().setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
						else if(fileExt.equalsIgnoreCase("html") || fileExt.equalsIgnoreCase("htm"))
							requestManager.getResponse().setContentType("text/html");
							
						String fname = fileName.toString();
						int idxfn = fname.lastIndexOf('\\');
						if(idxfn < 0)
							idxfn = fname.lastIndexOf('/');
						if(idxfn >= 0)
							fname = fname.substring(idxfn+1);
						String asDownload = (String)requestManager.getAttribute("download");
						if(asDownload != null && asDownload.equalsIgnoreCase("true")){
							requestManager.getResponse().setHeader("Content-Disposition", "attachment; filename=\""+fname+"\"");
							requestManager.getResponse().setHeader("content-length",""+info.getFileLength());
							requestManager.getResponse().setHeader("file-name",fname);
						}else{
							requestManager.getResponse().setHeader("Content-Disposition","inline; filename=\""+fname+"\"");
							requestManager.getResponse().setHeader("file-name",fname);
						}
						
					}
					
					if(info.isInMemory()){
						flushInputStream(new ByteArrayInputStream(info.getContent()),info.getFileLength(),requestManager);
					}else{
						try{
							flushInputStream(info.getInputStream(),info.getFileLength(),requestManager);
						}catch(Throwable t){
				        	LOG.warning("File streaming may be aborted by client");
						}finally{
							fileStreamer.close();
						}
					}
					return;
				}

				case ManagedCommands.iloadApplicationManual:{
					String documentId = (String)requestManager.getAttribute(APPL_MANUAL_ID_PARAMETER);
					if(documentId != null){
						FileType applManual = ApplicationManualLoader.loadApplicationManual(userSessionContext.getClientSessionContext(),documentId);
						requestManager.getResponse().setContentType(applManual.getContentType());
						InputStream is = new ByteArrayInputStream(applManual.byteArrayValue());
				        flushInputStream(is,applManual.byteArrayValue().length,requestManager);
					}
					return;
				}
					
				case ManagedCommands.iUploadPdfCmd:{
				     // Execute the PDF Upload process
					String propertyName = (String)request.getAttribute("propertyName");
					if(propertyName == null || propertyName.equals("")){
						String errorMsg = "You must specify a propertyName";
						Exception e = new Exception(errorMsg);
						throw e;						
					}
					CommandsStack stack = getBrowserInstanceStack(requestManager,browserInstance);
					CommandStructure commandStruct = stack.getCurrentCommand();
					if(commandStruct == null){
						String errorMsg = "There is no current display command. Verify application flow";
						Exception e = new Exception(errorMsg);
						throw e;
					}
					String uploadCommandName = (String)request.getAttribute("uploadCommand");
					if(propertyName == null || propertyName.equals("")){
						String errorMsg = "You must specify an uploadCommandName";
						Exception e = new Exception(errorMsg);
						throw e;						
					}
					Command command = loadCommand(requestManager,userSessionContext,uploadCommandName);
					if(command == null){
						String errorMsg = " ["+propertyName+"]";
						Exception e = new Exception(errorMsg);
						throw e;						
					}
					CommandDataModel outputCommandDataModel = commandStruct.getOutput();
					Class propClass = Tools.getPropertyType(outputCommandDataModel,propertyName);
					if(propClass == null){
						String errorMsg = "Exception reading class for property ["+propertyName+"]";
						Exception e = new Exception(errorMsg);
						throw e;						
					}
					
					InputStream is = request.getInputStream();
					ByteArrayOutputStream out = new ByteArrayOutputStream();
				    byte[] buf = new byte[16*1024];
				    int charsRead=0;
				    while((charsRead = is.read(buf,0,buf.length)) != -1)
						out.write(buf,0,charsRead);
				    out.flush();
				    out.close();
				    
				    FileType fileType = new FileType(out.toByteArray(),"application/pdf","");
				    Tools.setPropertyValue(outputCommandDataModel,propertyName,fileType);
				    
				    executeCommand(requestManager,userSessionContext,uploadCommandName,outputCommandDataModel,
			    			   	   browserInstance,browserInstance,false,false);
				    return;
				}
				
				case ManagedCommands.iexecuteProcessCmd:{
				    // Execute the new process clearing the stack on main instance
					Integer bi = new Integer((String)requestManager.getSession().getAttribute(SESS_BROWSER_INSTANCE));
					LOG.info(msg +"["+bi+"]");
				    executeCommand(requestManager,userSessionContext,commandName,null,bi,bi,true,false);
				    return;
				}
				
				case ManagedCommands.iexecuteCmd:{
				    // Execute the command
					LOG.info(msg +"["+browserInstance+"]");
				    executeCommand(requestManager,userSessionContext,commandName,null,
				    			   browserInstance,browserInstance,false,true);
				    
				    return;
			    }
				
				case ManagedCommands.iexecuteProcessOnNewStackCmd:{
				    // Execute the process on new stack
					Integer newBrowserInstance = getNewBrowserInstanceNumber(requestManager);
					LOG.info(msg +"["+newBrowserInstance+"]");
				    executeCommand(requestManager,userSessionContext,commandName,null,
				    			   browserInstance,newBrowserInstance,true,false);
				    return;
				}
				
				case ManagedCommands.iexecuteOnNewThreadCmd:{
				    // Execute the command on new stack
					Integer newBrowserInstance = getNewBrowserInstanceNumber(requestManager);
					LOG.info(msg +"["+newBrowserInstance+"]");
					boolean newModelOnNewThread = true;
					String newModelOnNewThreadPar = (String)requestManager.getAttribute(BHV_NEW_MODEL_ON_NEW_THREAD);
					if(newModelOnNewThreadPar != null && newModelOnNewThreadPar.equalsIgnoreCase("false"))
						newModelOnNewThread = false;
				    executeCommand(requestManager,userSessionContext,commandName,null,
				    			   browserInstance,newBrowserInstance,newModelOnNewThread,true);
				    return;
			    }
				
				case ManagedCommands.iexecuteOnPopupCmd:{
				    // Execute the command on popup
				    /************************** Da togliere **********************/
					Integer newBrowserInstance = getNewBrowserInstanceNumber(requestManager);
					LOG.info(msg +"["+newBrowserInstance+"]");
				    executeCommand(requestManager,userSessionContext,commandName,null,
				    			   browserInstance,newBrowserInstance,false,true);
				    return;
				}
				
				case ManagedCommands.iselectCmd:{
				    // Execute the select on a list
					LOG.info(msg +"["+browserInstance+"]");
				    executeSelectCommand(requestManager,userSessionContext,commandName,
				    					 browserInstance,browserInstance,false);
				    
				    return;
			    }
				
				case ManagedCommands.iselectOnNewThreadCmd:{
				    // Execute the select on a list on new stack
					Integer newBrowserInstance = getNewBrowserInstanceNumber(requestManager);
					LOG.info(msg +"["+newBrowserInstance+"]");
				    executeSelectCommand(requestManager,userSessionContext,commandName,
				    					 browserInstance,newBrowserInstance,true);
				    
				    return;
			    }
				
				case ManagedCommands.iselectOnPopupCmd:{
				    // Execute the select on a list on popup
					Integer newBrowserInstance = getNewBrowserInstanceNumber(requestManager);
					LOG.info(msg +"["+newBrowserInstance+"]");
				    executeSelectCommand(requestManager,userSessionContext,commandName,
				    					 browserInstance,newBrowserInstance,false);
				    return;
				}
				
				case ManagedCommands.icloseProcessStackCmd:
				case ManagedCommands.icloseThreadCmd:{
				    // Release the browser instance stack
			    	String closedBrowserInstance = requestManager.getRequest().getParameter(BROWSER_INSTANCE);
				    if(closedBrowserInstance == null)
					    return;
			    	String sessBrowserInstance = (String)requestManager.getSession().getAttribute(SESS_BROWSER_INSTANCE);
					if(sessBrowserInstance != null && closedBrowserInstance.equals(sessBrowserInstance)){
						LOG.warning("Attention !!! Attempt to close reserved Browser Instance ["+closedBrowserInstance+"]");
						return;
					}
					LOG.info(msg +"["+closedBrowserInstance+"]");
					requestManager.getSession().removeAttribute(WFEM_COMMAND_STACK_CONTEXT+closedBrowserInstance);
					requestManager.getResponse().setStatus(HttpServletResponse.SC_NO_CONTENT);
					LOG.info("BrowserInstance ["+closedBrowserInstance+"] has been closed.");
				    return;
			    }
				
				case ManagedCommands.inextPageCmd:{
				    // Execute the next page command
					LOG.info(msg +"["+browserInstance+"]");
				    executePageCommand(requestManager,userSessionContext,ManagedCommands.inextPageCmd,browserInstance);
				    return;
			    }
				
				case ManagedCommands.ipreviousPageCmd:{
				    // Execute the previous page command
					LOG.info(msg +"["+browserInstance+"]");
				    executePageCommand(requestManager,userSessionContext,ManagedCommands.ipreviousPageCmd,browserInstance);
				    return;
			    }
				
				case ManagedCommands.ifirstPageCmd:{
				    // Execute the first page command
					LOG.info(msg +"["+browserInstance+"]");
				    executePageCommand(requestManager,userSessionContext,ManagedCommands.ifirstPageCmd,browserInstance);
				    return;
			    }
				
				case ManagedCommands.ilastPageCmd:{
				    // Execute the last page command
					LOG.info(msg +"["+browserInstance+"]");
				    executePageCommand(requestManager,userSessionContext,ManagedCommands.ilastPageCmd,browserInstance);
				    return;
			    }
				
				case ManagedCommands.igotoPageCmd:{
				    // Execute the last page command
					LOG.info(msg +"["+browserInstance+"]");
				    executePageCommand(requestManager,userSessionContext,ManagedCommands.igotoPageCmd,browserInstance);
				    return;
			    }
				
				case ManagedCommands.iopenGridPdfCmd:
			    case ManagedCommands.iopenGridCalcCmd:{
				    // Execute the open grid command
					LOG.info(msg +"["+browserInstance+"]");
				    executeConvertListCommand(requestManager,userSessionContext,commandCode,browserInstance);
				    return;
			    }
			    
			    case ManagedCommands.iexecuteCurrentCmd:{
				    // Execute the last Display Command
					LOG.info(msg +"["+browserInstance+"]");
				    executeCurrentDisplayCommand(requestManager,userSessionContext,null,true,browserInstance);
				    return;
			    }
			    
			    case ManagedCommands.iexecuteLastCmd:{
				    // Execute the last Display Command
					LOG.info(msg +"["+browserInstance+"]");
					int fd = -1;
					String fdPar = (String)requestManager.getAttribute(FORWARD_DISPLAY_PARAMETER);
					if(fdPar != null && fdPar.length() > 0){
						try{fd = Integer.parseInt(fdPar);}catch(Exception e){fd = -1;};
					}
				    executeLastDisplayCommand(requestManager,userSessionContext,null,true,browserInstance,fd);
				    return;
			    }
			    
			    case ManagedCommands.ishowCurrentCmd:{
				    // Execute the last Display Command
					LOG.info(msg +"["+browserInstance+"]");
				    executeCurrentDisplayCommand(requestManager,userSessionContext,null,false,browserInstance);
				    return;
			    }
			    
			    case ManagedCommands.ishowLastCmd:{
				    // Execute the last Display Command
					LOG.info(msg +"["+browserInstance+"]");
					int fd = -1;
					String fdPar = (String)requestManager.getAttribute(FORWARD_DISPLAY_PARAMETER);
					if(fdPar != null && fdPar.length() > 0){
						try{fd = Integer.parseInt(fdPar);}catch(Exception e){fd = -1;};
					}
				    executeLastDisplayCommand(requestManager,userSessionContext,null,false,browserInstance,fd);
				    return;
			    }
			    
			    case ManagedCommands.ishowPageCmd:{
				    // Get the page name in request
				    String pageName = (String)requestManager.getAttribute(showPageParameterName);
				    // Remove the ' and put the & in place of ,
					pageName = pageName.replace(',','&');
					int cidx = pageName.indexOf('\'');
					if(cidx >= 0)
						pageName = pageName.substring(cidx+1);
					cidx = pageName.lastIndexOf('\'');
					if(cidx >= 0)
						pageName = pageName.substring(0,cidx);
					// Forward the page
				    forwardPage(requestManager,pageName,null,browserInstance);
				    return;
			    }
			    
			    case ManagedCommands.ishowExternalPageCmd:{
				    // Get the page name in request
				    String pageName = (String)requestManager.getAttribute(showPageParameterName);
				    // Remove the ' and put the & in place of ,
					pageName = pageName.replace(',','&');
					int cidx = pageName.indexOf('\'');
					if(cidx >= 0)
						pageName = pageName.substring(cidx+1);
					cidx = pageName.lastIndexOf('\'');
					if(cidx >= 0)
						pageName = pageName.substring(0,cidx);
					requestManager.setAttribute("url",pageName);
					// Forward to the internal page
				    forwardPage(requestManager,showExternalPage,null,browserInstance);
				    return;
			    }
			    
			    case ManagedCommands.ishowPdfCmd:{
				    // Get the pdf name in request
				    String pdfNames = (String)requestManager.getAttribute(showPdfParameterName);
				    forwardPdf(requestManager,pdfNames);
				    return;
			    }
			    
			    case ManagedCommands.iloadFileTypeCmd:{
					LOG.info(msg +"["+browserInstance+"]");
					String page = "";
					CommandDataModel dataModel = getCurrentDataModel(requestManager,browserInstance);
					if(dataModel == null){
						requestManager.getResponse().setStatus(HttpServletResponse.SC_NO_CONTENT);
						return;
					}
			    	String propertyName = (String)requestManager.getAttribute(PROPERTY_NAME_PARAMETER);
			    	requestManager.loadCommandDataModelProperties(dataModel);
			    	FileType prop = (FileType)Tools.getPropertyValue(dataModel,propertyName);
			    	String fileTypes = prop.getFileTypes();
			    	String fileName = prop.getFileName();
			    	String fileNameNoExt = fileName.toString();
			    	if(fileNameNoExt.lastIndexOf('.') > 0)
			    		fileNameNoExt = fileNameNoExt.substring(0,fileNameNoExt.lastIndexOf('.'));
			    	String fileNameNoExtWithoutAcceptableChars = fileNameNoExt.replaceAll("[_-]","");
			    	String contentType = prop.getContentType();
			    	if(fileName.length() > FileType.FILE_NAME_MEMORY_SPACE-1){
						page = "<script>parent.nameErrorLoadFileType(\""+fileName+"\");</script>";		    		
			    	}else if(!Tools.isAlphaNumString(fileNameNoExtWithoutAcceptableChars)){
						page = "<script>parent.filenameErrorLoadFileType(\""+fileName+"\");</script>";		    		
			    	}else if(contentType.length() > FileType.CONTENT_TYPE_MEMORY_SPACE-1){
							page = "<script>parent.contentTypeErrorLoadFileType(\""+fileName+"\");</script>";		    		
			    	}else{
			    		if(fileTypes != null && fileTypes.equals("*")){
				    		page = "<script>parent.updLoadFileType(\""+propertyName+"\");</script>";
			    		}else{
			    			String ext = "";
			    			int pIdx = fileName.lastIndexOf('.');
			    			if(pIdx >= 0)
			    				ext = fileName.substring(pIdx+1);
					    	if(fileTypes == null || fileTypes.equals("") || fileTypes.toLowerCase().indexOf(ext.toLowerCase()) < 0)
								page = "<script>parent.typeErrorLoadFileType(\""+fileTypes+"\");</script>";
					    	else
					    		page = "<script>parent.updLoadFileType(\""+propertyName+"\");</script>";
			    		}
			    	}
					PrintWriter out = requestManager.getResponse().getWriter();
					requestManager.getResponse().setContentType("text/html");
					requestManager.getResponse().setContentLength(page.length());		
					out.print(page);
					out.flush();
				    return;
			    }
			    
			    case ManagedCommands.iclearFileTypeCmd:{
					LOG.info(msg +"["+browserInstance+"]");
					CommandDataModel dataModel = getCurrentDataModel(requestManager,browserInstance);
					if(dataModel == null){
						requestManager.getResponse().setStatus(HttpServletResponse.SC_NO_CONTENT);
						return;
					}
			    	String propertyName = (String)requestManager.getAttribute(PROPERTY_NAME_PARAMETER);
			    	FileType propertyValue = (FileType)Tools.getPropertyValue(dataModel,propertyName);
			    	if(propertyValue != null)
			    		propertyValue.clear();
			    	else
			    		Tools.setPropertyValue(dataModel,propertyName,new FileType());
			    	String page = "<script>parent.updClearFileType('"+propertyName+"');</script>";
					PrintWriter out = requestManager.getResponse().getWriter();
					requestManager.getResponse().setContentType("text/html");
					requestManager.getResponse().setContentLength(page.length());		
					out.print(page);
					out.flush();
				    return;
			    }
			    
			    case ManagedCommands.ishowFileTypeCmd:{
					LOG.info(msg +"["+browserInstance+"]");
					CommandDataModel dataModel = getCurrentDataModel(requestManager,browserInstance);
					if(dataModel == null){
						requestManager.getResponse().setStatus(HttpServletResponse.SC_NO_CONTENT);
						return;
					}
			    	String propertyName = (String)requestManager.getAttribute(PROPERTY_NAME_PARAMETER);
			    	FileType propertyValue = (FileType)Tools.getPropertyValue(dataModel,propertyName);
			    	if(propertyValue == null || propertyValue.isNull()){
						requestManager.getResponse().setStatus(HttpServletResponse.SC_NO_CONTENT);
						return;
					}
			    	String fileName = (String)requestManager.getAttribute(FILE_NAME_PARAMETER);
			    	if(fileName != null && fileName.length() > 0){
						requestManager.getResponse().setHeader("Content-Disposition", "attachment; filename=\""+fileName+"\"");
						requestManager.getResponse().setHeader("content-length",""+propertyValue.getLength());
						requestManager.getResponse().setHeader("file-name",fileName);
			    	}
					requestManager.getResponse().setContentType(propertyValue.getContentType());
					InputStream is = propertyValue.getInputStream();
			        flushInputStream(is,propertyValue.getLength(),requestManager);
				    return;
				}
			    
			    case ManagedCommands.iinvalidateSession:{
				    // Invalidate the session
					LOG.info(msg +"["+browserInstance+"]");
				    HttpSession session = requestManager.getSession();
					LOG.info("Invalidating session ["+session.getId()+"].");
					session.invalidate();
					requestManager.getResponse().setStatus(HttpServletResponse.SC_NO_CONTENT);
				    return;
			    }
			    
			    default:{
					String errorMsg = "Command is not valid";
					manageError(requestManager,new CommandException(errorMsg));
				    return;
			    }
		    
			}
		    
		}catch(Exception e){
			manageError(requestManager,new CommandException(e.toString()));
			return;
		}finally{
			// If is changed UserSessionContext or internal map extendedInfo, set attribute "userSessionContext" into session, updating persistent data
	        if ( userSessionContext != null ) {
	        	if ( userSessionContext.isChanged() ) {
			    	LOG.debug("PerformTask: UserSessionContext was changed during command execution. Setting [userSessionContext] attribute in session");
		        	requestManager.getSession().setAttribute(USER_SESSION_CONTEXT,userSessionContext);
		        	userSessionContext.setChanged(false);
		        }
	        }
		}
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private Integer getNewBrowserInstanceNumber(RequestManager requestManager) throws CommandException{
	    Integer newBrowserInstance = BrowserInstanceGenerator.generateUniqueBrowserInstance(requestManager);
	    LOG.debug("Assigned new Browser Instance is: " + newBrowserInstance);
	    return newBrowserInstance;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private void putQueryStringInRequest(RequestManager requestManager, String queryString){
	
		if(queryString.indexOf("?") < 0)
			return;
	
		String parameters = queryString.substring(queryString.indexOf("?"));
		StringTokenizer st = new StringTokenizer(parameters,"&");
		while(st.hasMoreTokens()){
			String tok = st.nextToken();
			int idx = tok.indexOf("=");
			if(idx <= 0)
				continue;
	
			String parameterName = tok.substring(0,idx);
			String parameterValue = tok.substring(idx+1);
	
		    LOG.debug("Put parameter ["+parameterName+"] with value ["+parameterValue+"] in request as attribute");
			requestManager.setAttribute(parameterName,parameterValue);
		}
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private void saveCommandOutputDataModel(RequestManager requestManager,
							                UserSessionContext userSessionContext,
							                Command command,
							                CommandDataModel outputCommandDataModel) {
	
		String outputDataModelName = null;
		Class outputDataModelClass = command.getOutputViewClass();
		if(outputDataModelClass != null){
			outputDataModelName = outputDataModelClass.getName();
		}else{
			outputDataModelName = outputCommandDataModel.getClass().getName();
		}
		outputDataModelName = outputDataModelName.substring(outputDataModelName.lastIndexOf(".") + 1);
	
		// Decapitalize first caracter
		String requestModelName = outputDataModelName.substring(0,1).toLowerCase();
		requestModelName += outputDataModelName.substring(1);
	
		LOG.debug("Save output data model ["+outputCommandDataModel+"] in request with name "+requestModelName);
		outputCommandDataModel.setUserSessionContext(userSessionContext);
		requestManager.setAttribute(requestModelName,outputCommandDataModel);
	
		// Save object in session if it is persistent
		if(outputCommandDataModel.isPersistent()){
			LOG.debug("Model is persistent. Save output data model "+outputDataModelName+" in session");
			Hashtable map = (Hashtable)requestManager.getSession().getAttribute(PERSISTENT_MODELS);
			if(map == null)
				map = new Hashtable();
			map.put(outputCommandDataModel.getClass().getName(),outputCommandDataModel);
			requestManager.getSession().setAttribute(PERSISTENT_MODELS,map);
		}
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private boolean saveInitialRequestParameters(RequestManager requestManager, boolean isLoginFreeCommand) {
	
		String msg = "";
		
		// Catch the current country
		String country = (String)requestManager.getSession().getAttribute(countryParameterName);
		if(country == null){
			country = (String)requestManager.getAttribute(countryParameterName);
			if(country == null){
				if(!isLoginFreeCommand){
				    msg = "ATTENTION !!! Country is not defined in request . (Maybe the session is invalidated)";
				    LOG.debug(msg);
				}
			}else
				requestManager.getSession().setAttribute(countryParameterName,country);
		}
	
		// Catch the current language
		String language = (String)requestManager.getSession().getAttribute(languageParameterName);
		if(language == null){
			language = (String)requestManager.getAttribute(languageParameterName);
			if(language == null){
				if(!isLoginFreeCommand){
				    msg = "ATTENTION !!! Language is not defined in request . (Maybe the session is invalidated)";
				    LOG.debug(msg);
				}
			}else{
				requestManager.getSession().setAttribute(languageParameterName,language);
			}
		}
		requestManager.setLanguage((String)requestManager.getSession().getAttribute(languageParameterName));
	
		// Catch the current channel
		String channel = (String)requestManager.getSession().getAttribute(channelParameterName);
		if(channel == null){
			channel = (String)requestManager.getAttribute(channelParameterName);
			if(channel == null){
				if(!isLoginFreeCommand){
				    msg = "ATTENTION !!! Channel is not defined in request . (Maybe the session is invalidated)";
				    LOG.debug(msg);
				}
			}else
				requestManager.getSession().setAttribute(channelParameterName,channel);
		}
		
		// Catch the starting URL
		String startUrl = (String)requestManager.getSession().getAttribute(startUrlParameterName);
		if(startUrl == null){
			startUrl = (String)requestManager.getAttribute(startUrlParameterName);
			if(startUrl == null){
				if(!isLoginFreeCommand){
				    msg = "ATTENTION !!! Start URL is not defined in request . (Maybe the session is invalidated)";
				    LOG.debug(msg);
				}
			}else
				requestManager.getSession().setAttribute(startUrlParameterName,startUrl);
		}
		
		// Catch the Client IP
		String clientIp = (String)requestManager.getSession().getAttribute(clientIpParameterName);
		if(clientIp == null){
			clientIp = (String)requestManager.getAttribute(clientIpParameterName);
			if(clientIp == null){
				if(!isLoginFreeCommand){
				    msg = "ATTENTION !!! Client IP is not defined in request . (Maybe the session is invalidated)";
				    LOG.debug(msg);
				}
			}else
				requestManager.getSession().setAttribute(clientIpParameterName,clientIp);
		}
		
		return true;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private ListType searchListType(String propertyName, CommandDataModel dataModel) throws Exception {
	
	    int firstDot = propertyName.indexOf(com.atosorigin.wfem.controller.Constants.NESTED_INDICATOR);
	    if (firstDot == -1){
	        Object obj = Tools.getPropertyValue(dataModel, propertyName);
	        if(obj instanceof ListType)
		        return (ListType)obj;
		    else if(obj instanceof ListCommandDataModel)
		    	return ((ListCommandDataModel)obj).getRows();
		    else
		    	return null;
	    }
	
	    String newDataModelName = propertyName.substring(0, firstDot);
		CommandDataModel newDataModel = (CommandDataModel)Tools.getPropertyValue(dataModel, newDataModelName);
	
		String newPropertyName = propertyName.substring(firstDot+1);
		
	    return searchListType(newPropertyName, newDataModel);
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private void showLogin(RequestManager requestManager) throws Exception {
		forwardPage(requestManager,loginPage,null,new Integer(0));	
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private Map getPagesInListTypes(CommandDataModel model, String modelName) throws Exception{
		
		AbstractTypePropertyDescriptor[] props = ObjectsPropertiesCache.getInstance().getAbstractTypesPropertyDescriptors(model);
	    if (props == null)
	       return null;
	
		HashMap result = new HashMap();
	    for (int i=0;i<props.length;i++) {
	
	        Method propGetter = props[i].getReadMethod();
	        if(propGetter == null)
	        	continue;
	        Object obj = propGetter.invoke(model,null);
	        if(obj == null)
	        	continue;
	        if(obj instanceof ListType){
		        int rowsPerPage = ((ListType)obj).getRowsPerPage();
		        int pageNum = ((ListType)obj).getCurrentPage();
		        String val = "" + rowsPerPage + "," + pageNum;
		        result.put(modelName+props[i].getName(), val);
	        }else if(obj instanceof CommandDataModel){
		        result.putAll(getPagesInListTypes((CommandDataModel)obj,props[i].getName()+com.atosorigin.wfem.controller.Constants.NESTED_INDICATOR));
	        }
	    }
	    return result;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private void updatedPagesInListTypes(CommandDataModel model,Map savedPages) throws Exception{
		try{
			Iterator it = savedPages.keySet().iterator();
			while(it.hasNext()){
				String propName = (String)it.next();
				String val = (String)savedPages.get(propName);
				Integer rowsPerPage = new Integer(val.substring(0,val.indexOf(",")));
				Integer pageNum = new Integer(val.substring(val.indexOf(",")+1));
				ListType list = (ListType)Tools.getPropertyValue(model,propName);
				list.setRowsInPage(rowsPerPage.intValue());
				list.setCurrentPage(pageNum.intValue());
			}
		}catch(Exception e){
			LOG.info("Unable to restore pages in model ["+model+"]");
		}
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private CommandDataContainer callCommandExecute(RequestManager requestManager, Command command, 
											        UserSessionContext userContext, CommandDataModel inputDataModel) throws CommandException, Exception{
		
		BrowserInstanceGenerator.watchCsrfCommand(requestManager, command);
		
		Hashtable sessionPersistentModels = (Hashtable)requestManager.getSession().getAttribute(PERSISTENT_MODELS);
		command.setPersistentModels(sessionPersistentModels);
		
		String modelTraceDir = null;
		boolean modelTraceEnabled = Configuration.getInstance().isModelTraceEnabled();
		if(modelTraceEnabled)
			modelTraceDir = Configuration.getInstance().getModelTraceDir();
		
		CommandDataContainer commandDataContainer = new CommandDataContainer();
		if(modelTraceEnabled && inputDataModel != null)
			saveModel(command,inputDataModel,true,modelTraceDir);

		TierAccessLoggerInfo tli = ClientTierAccessLogger.initTier(requestManager,userContext.getClientSessionContext(),
																   command.getClass().getName(),requestManager.fromClientRequest);
		CommandDataModel outputModel = null;
		try{
			outputModel = command.execute(userContext,inputDataModel);
		}catch(Throwable t){
			try{tli.setAccessName(tli.getAccessName()+"#exception"); tli.stop(requestManager.getClientTierInputParameters());}catch(Exception e){}
			throw new CommandException(t.toString());
		}
		tli.stop(requestManager.getClientTierInputParameters());
		requestManager.fromClientRequest = false;
		
		if(modelTraceEnabled && outputModel != null)
			saveModel(command,outputModel,false,modelTraceDir);
		commandDataContainer.setModel(outputModel);
		commandDataContainer.setCommand(command);
		commandDataContainer.setUserContext(userContext);
		
		Hashtable commandPersistentModels = command.getPersistentModels();
		if(commandPersistentModels != null)
			requestManager.getSession().setAttribute(PERSISTENT_MODELS, commandPersistentModels);
		
		return commandDataContainer;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private void saveModel(Command command, CommandDataModel model, boolean isInput, String modelTraceDir) {
		if(modelTraceDir == null)
			return;
		try{
			String fileName = null;
			if(isInput)
				fileName = command.getClass().getName()+"InputData";
			else
				fileName = command.getClass().getName()+"OutputData";

			if(!modelTraceDir.endsWith("/"))
				modelTraceDir += "/";
			fileName = modelTraceDir + fileName + ".xml";
			
			String xml = Tools.xmlFromModelV2(model,true,true,false);
			
			FileOutputStream fw = new FileOutputStream(fileName);
			fw.write(xml.getBytes());
			fw.flush();
			fw.close();
	
		}catch(Exception e){
			com.atosorigin.wfem.loggers.ControllerLogger.getInstance().warning("Unable to trace MODEL "+model.getClass().getName());
		}
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private void flushInputStream(InputStream is, long isLength, RequestManager requestManager) throws IOException{

		ServletOutputStream out = null;
	    try{
	    	out = requestManager.getResponse().getOutputStream();
	    	if(isLength <= Integer.MAX_VALUE)
	    		requestManager.getResponse().setContentLength((int)isLength);
		    byte[] buf = new byte[16*1024];
		    int charsRead;
		    while ((charsRead = is.read(buf)) != -1) {
		    	out.write(buf, 0, charsRead);
		    }
	    }finally{
	    	try{is.close();}catch(Exception e){}
	    	if(out != null){
	            try{out.flush(); out.close();}catch(Exception e){}
	    	}
	    }				
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private CommandDataModel getCurrentDataModel(RequestManager requestManager, Integer browserInstance){
	    // Get the current datamodel
		CommandsStack stack = getBrowserInstanceStack(requestManager,browserInstance);
		if(stack == null){
			requestManager.getResponse().setStatus(HttpServletResponse.SC_NO_CONTENT);
			return null;
		}
		CommandStructure commandStruct = stack.getCurrentCommand();
		if(commandStruct == null){
			requestManager.getResponse().setStatus(HttpServletResponse.SC_NO_CONTENT);
			return null;
		}
		CommandDataModel dataModel = commandStruct.getOutput();
		if(dataModel == null){
			requestManager.getResponse().setStatus(HttpServletResponse.SC_NO_CONTENT);
			return null;
		}
		return dataModel;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private void doNoCacheResponse(RequestManager requestManager, String contentType, byte[] bytes, boolean setNoCache, String suggestedFileName) throws IOException{
		
		if(setNoCache){
		    requestManager.getResponse().setHeader("Cache-Control","no-cache, no-store, must-revalidate, max-age=0"); //HTTP 1.1
		    requestManager.getResponse().setHeader("Pragma","no-cache"); //HTTP 1.0
		    requestManager.getResponse().setHeader("Expires", "0"); //prevents caching at the proxy server
		}
		
		if(suggestedFileName != null && suggestedFileName.length() > 0){
			requestManager.getResponse().setHeader("Content-Disposition","inline; filename=\""+suggestedFileName+"\"");
			requestManager.getResponse().setHeader("file-name",suggestedFileName);
		}
		
	    if(contentType != null && contentType.length() > 0)
	    	requestManager.getResponse().setContentType(contentType);
	    
	    if(bytes != null){
	    	requestManager.getResponse().setContentLength(bytes.length);
			ServletOutputStream out = requestManager.getResponse().getOutputStream();
			out.write(bytes);
			out.flush();
			out.close();
	    }
	}
	
}
