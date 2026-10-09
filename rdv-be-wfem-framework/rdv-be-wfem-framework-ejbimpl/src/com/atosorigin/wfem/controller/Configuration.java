package com.atosorigin.wfem.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import com.atosorigin.wfem.loggers.LogPrinter;
import com.atosorigin.wfem.util.RefreshEventListener;
import com.atosorigin.wfem.util.RefreshNotifier;
import com.atosorigin.wfem.util.Tools;

/********************************************************************/
/********************************************************************/
public class Configuration implements RefreshEventListener{
	
	private static Configuration singleton = null;

	public static final String WFEM_LAYOUT_WEBAPP = "/wfemlayout";
	public static final String DEFAULT_RESOURCE_SUPPLIER_CLASS_NAME = "com.atosorigin.wfem.layout.resources.DefaultResourceSupplier";
	public static final String DEFAULT_FIELDS_FACTORY_CLASS_NAME = "com.atosorigin.wfem.layout.field.DefaultFieldsFactory";

	private static final int REFRESH_TIME = 10;
	
	public static final String CONFIGURATION_FILES_ROOT             = "/appsproperties/wfem";
	
	private static final String CONFIGURATION_FILE                   = CONFIGURATION_FILES_ROOT+"/wfem.properties";
	private static final String PACKAGE_WEBREDIR_CONFIGURATION_FILE  = CONFIGURATION_FILES_ROOT+"/wfemUrlRedirection.properties";
	private static final String WEBREDIR_CONFIGURATION_FILE          = CONFIGURATION_FILES_ROOT+"/wfemWebAppRedirection.properties";
	private static final String SHARED_BEANS_CONFIGURATION_FILE      = CONFIGURATION_FILES_ROOT+"/wfemSharedBeans.properties";
	private static final String WFEM_IMAGE_SERVER_WEBAPP_ENABLED     = CONFIGURATION_FILES_ROOT+"/wfemImageServerWebAppEnabled.properties";
	private static final String CSRF_CONFIGURATION_FILE              = CONFIGURATION_FILES_ROOT+"/wfemCsrfWatchList.properties";

	private static final String PARM_TRACE_BROWSERINSTANCES_NOTFOUND = "TRACE_BROWSERINSTANCES_NOTFOUND";
	
	private static final String PARM_TRUSTED_SITE = "TRUSTED_SITE";
	
	private static final String PARM_LOG_FILENAME            = "LOG_FILENAME";
	private static final String PARM_TRACE_LEVEL             = "TRACE_LEVEL";
	private static final String PARM_CONTROLLER_TRACE_LEVEL  = "CONTROLLER_TRACE_LEVEL";
	private static final String PARM_DB_TRACE_LEVEL          = "DB_TRACE_LEVEL";
	private static final String PARM_UTIL_TRACE_LEVEL        = "UTIL_TRACE_LEVEL";
	private static final String PARM_QAS_TRACE_LEVEL         = "QAS_TRACE_LEVEL";
	private static final String PARM_OSB_TRACE_LEVEL         = "OSB_TRACE_LEVEL";
	private static final String PARM_ROF_TRACE_LEVEL         = "ROF_TRACE_LEVEL";
	private static final String PARM_DAO_TRACE_LEVEL         = "DAO_TRACE_LEVEL";
	private static final String PARM_LAYOUT_TRACE_LEVEL      = "LAYOUT_TRACE_LEVEL";
	private static final String PARM_HTMLTOPDF_TRACE_LEVEL   = "HTMLTOPDF_TRACE_LEVEL";
	private static final String PARM_PDF_TRACE_LEVEL   		 = "PDF_TRACE_LEVEL";
	private static final String PARM_REFRESHER_TRACE_LEVEL   = "REFRESHER_TRACE_LEVEL";
	private static final String PARM_MAIL_TRACE_LEVEL        = "MAIL_TRACE_LEVEL";
	private static final String PARM_SUBSESSIONS_INVALIDATOR_TRACE_LEVEL  = "SUBSESSIONS_INVALIDATOR_TRACE_LEVEL";
	private static final String PARM_DYNATRACE_SRC           = "DYNATRACE_SRC";
	
	private static final String PARM_TRACED_USERCODE         = "TRACED_USERCODE";
	
	private static final String PARM_QAS_SIMULATION_ENABLED  = "QAS_SIMULATION_ENABLED";
	private static final String PARM_OSB_SIMULATION_ENABLED  = "OSB_SIMULATION_ENABLED";
	
	private static final String PARM_ENVIRONMENT        		   	   = "ENVIRONMENT";
	private static final String PARM_DATABASE_USER            		   = "DATABASE_USER";
	private static final String PARM_DATABASE_PASSWORD        		   = "DATABASE_PASSWORD";
	private static final String PARM_NUM_OF_CALLABLE_EXCEPTION_IGNORED = "NUM_OF_CALLABLE_EXCEPTION_IGNORED";
	private static final String PARM_DATASOURCES_NAMES        		   = "DATASOURCES_NAMES";	
	private static final String PARM_SYBASE_DATASOURCES_NAMES          = "SYBASE_DATASOURCES_NAMES";	

	private static final String PARM_PDF_PROVIDER_URL      = "PDF_PROVIDER_URL";

	private static final String PARAM_EXTERNAL_SYSTEM_WEBAPP  = "EXTERNAL_SYSTEM_WEBAPP";

	private static final String PARM_EJB_PROVIDER_URL = "EJB_PROVIDER_URL";
	private static final String PARM_EJB_USER         = "EJB_USER";
	private static final String PARM_EJB_PASSWORD     = "EJB_PASSWORD";

	private static final String PARM_EJB_CONTEXT_FACTORY           = "EJB_CONTEXT_FACTORY";
	
	private static final String PARM_COMMAND_STACK_SIZE = "COMMAND_STACK_SIZE";

	private static final String PARM_OBJECTS_CACHE_ENABLED = "OBJECTS_CACHE_ENABLED";
	private static final String PARM_REMOTE_OBJECTS_CACHE_ENABLED = "REMOTE_OBJECTS_CACHE_ENABLED";
	private static final String PARM_DAO_CACHE_ENABLED = "DAO_CACHE_ENABLED";
	
	private static final String PARM_JDBC_DRIVER = "JDBC_DRIVER";

	private static final String PARM_DBCONNECTION_TRACE_DIR = "DBCONNECTION_TRACE_DIR";
	private static final String PARM_DBCONNECTION_TRACE_ENABLED = "DBCONNECTION_TRACE_ENABLED";
	private static final String PARM_TIER_TRACE_DIR = "TIER_TRACE_DIR";

	private static final String PARM_ENVIRONMENT_TYPE = "ENVIRONMENT_TYPE";
	private static final String OFFLINE_ENVIRONMENT = "offline";
	private static final String ONLINE_ENVIRONMENT = "online";
	private String	environmentType = ONLINE_ENVIRONMENT;
		
	private static final String PARM_SERIALIZED_CACHE_DIR = "SERIALIZED_CACHE_DIR";
	private static final String PARM_DB_JAVA_CLASSES_DIR = "DB_JAVA_CLASSES_DIR";
	private static final String PARM_TEST_JAVA_CLASSES_DIR = "TEST_JAVA_CLASSES_DIR";
	
	private static final String PARM_MODEL_TRACE_ENABLED     = "MODEL_TRACE_ENABLED";
	private static final String PARM_MODEL_TRACE_DIR         = "MODEL_TRACE_DIR";

	private static final String PARM_BO_URL         		= "BO_URL";
	private static final String PARM_QAS_URL         		= "QAS_URL";
	private static final String PARM_OSB_URL         		= "OSB_URL";
	private static final String PARM_FILENET_URL       		= "FILENET_URL";

	private static final String PARM_IMAGE_SERVER_URL  						= "IMAGE_SERVER_URL";
	private static final String PARM_IMAGE_SERVER_FOR_WFEMLAYOUT_ENABLED  	= "IMAGE_SERVER_FOR_WFEMLAYOUT_ENABLED";

	private static final String PARM_ALLOW_COPY_ON_DISABLED_INPUT_FIELDS = "ALLOW_COPY_ON_DISABLED_INPUT_FIELDS";	
	
	private static final String PARM_JAGUAR_SERVLET_CONTEXT_CLASS = "JAGUAR_SERVLET_CONTEXT_CLASS";
	private static final String PARM_TOMCAT_SERVLET_CONTEXT_CLASS = "TOMCAT_SERVLET_CONTEXT_CLASS";

	private static final String PARM_HOSTNAME_APP = "HOSTNAME_APP";
	private static final String PARM_KEEPALIVE_TIMER_MINUTES = "KEEPALIVE_TIMER_MINUTES"; 
	private static final String PARM_ACCEPT_CTX_ONLY_ON_HEADER = "ACCEPT_CTX_ONLY_ON_HEADER"; 

	private static String DEFAULT_CROSS_SITE_SCRIPTING_REGEX = "[<>'\\\"&;\\\\/\\\\()]";
	private static final String PARM_CROSS_SITE_SCRIPTING_REGEX = "CROSS_SITE_SCRIPTING_REGEX"; 
	
	private static final String PARM_NAS_SOURCE = "NAS_SOURCE";
	
	private String	jaguarServletContextClass = null;
	private String	tomcatServletContextClass = null;

	private boolean traceBrowserInstancesNotFoundActive = false;
	
	private String	REFRESHABLE_CACHE = "refreshable";
	private String	SERIALIZED_CACHE = "serialized";
	private static final String PARM_DAO_OBJECTS_CACHE_TYPE 			= "DAO_OBJECTS_CACHE_TYPE";
	private static final String PARM_DAO_CODDESC_CACHE_TYPE 			= "DAO_CODDESC_CACHE_TYPE";
	private static final String PARM_OBJECTS_CACHE_SCAN_TIME 		= "OBJECTS_CACHE_SCAN_TIME";
	private static final String PARM_OBJECTS_LIFE_TIME 				= "OBJECTS_LIFE_TIME";
	private static final String PARM_DAO_OBJECTS_CACHE_SCAN_TIME 	= "DAO_OBJECTS_CACHE_SCAN_TIME";
	private static final String PARM_DAO_OBJECTS_LIFE_TIME 			= "DAO_OBJECTS_LIFE_TIME";
	private static final String PARM_DAO_CODDESC_CACHE_SCAN_TIME 	= "DAO_CODDESC_CACHE_SCAN_TIME";
	private static final String PARM_DAO_CODDESC_DEFAULT_LIFE_TIME 	= "DAO_CODDESC_DEFAULT_LIFE_TIME";
	private String daoObjectsCacheType		= REFRESHABLE_CACHE;
	private String daoCodDescCacheType	 	= REFRESHABLE_CACHE;
	private int objectsCacheScanTime 		= 30;
	private int objectsLifeTime      		= 600;
	private int daoObjectsCacheScanTime 	= 10;
	private int daoObjectsLifeTime      	= 30;
	private int daoCodDescCacheScanTime 	= 10;
	private int daoCodDescDefaultLifeTime  = 600;

	private boolean trustedSite = false;
	
	private String logFilename 		 = null;
	private int traceLevel           = 0;
	private int controllerTraceLevel = 0;
	private int dbTraceLevel         = 0;
	private int utilTraceLevel       = 0;
	private int qasTraceLevel        = 0;
	private int osbTraceLevel         = 0;
	private int rofTraceLevel        = 0;
	private int daoTraceLevel        = 0;
	private int layoutTraceLevel     = 0;
	private int htmlToPdfTraceLevel	 = 0;
	private int pdfTraceLevel	 	 = 0;
	private int refresherTraceLevel  = 0;
	private int mailTraceLevel       = 0;
	private int subsessionsInvalidatorTraceLevel = 0;
	private String tracedUserCode     = "";
	
	private String  environment = "";
	private boolean qasSimulationEnabled = false;
	private boolean osbSimulationEnabled = false;
	private String  ejbContextFactory = null;
	private String externalSystemWebapp = null;
	
	private String dbUser        = null;
	private String dbPassword    = null;
	private int numOfCallableExceptionIgnored = 100;

	private String pdfProviderUrl    = null;
	private String ejbProviderUrl    = null;
	private String ejbUser           = null;
	private String ejbPassword       = null;

	private int commandStackSize = 5;
		
	private boolean objectsCacheEnabled = true;
	private boolean remoteObjectsCacheEnabled = true;
	private boolean daoCacheEnabled = true;

	private String jdbcDriver = null;

	private String   dbConnectionTraceDir       = null;
	private boolean  dbConnectionTraceEnabled = false;

	private String   serializedCacheDir = null;
	private String   dbJavaClassesDir = null;
	private String   testJavaClassesDir = null;
	
	private boolean  modelTraceEnabled = false;
	private String   modelTraceDir = null;
	private String   tierTraceDir = null;

	private String   boUrl = "";
	private String   qasUrl = "https://ofxme/qarclet.dll";
	private String   osbUrl = "";
	private String   filenetUrl = "";
	
	private String   imageServerUrl = "";
	private boolean  imageServerForWfemlayoutEnabled = true;
	private Properties  imageServerWfemWebAppEnabledMap = new Properties();
	
	private Properties configurationProps = new Properties();
	private Properties webRedirection = new Properties();
	private Properties urlWebRedirection = new Properties();
	private String[]   sortedUrlWebRedirectionPropertyNames = null;
	private Properties sharedBeansProps = new Properties();
	private String dataSourcesNames = null;
	private ArrayList<String> dataSourcesNamesAsArray = null;
	private String sybaseDataSourcesNames = null;
	private ArrayList<String> sybaseDataSourcesNamesAsArray = null;

	private boolean allowCopyOnDisabledInputField = false;
	private String hostnameApp = "";
	private int keepaliveTimerMinutes = 15;
	private boolean acceptCtxOnlyOnHeader = false;
	
	private String dynatraceSrc  = "";
	
	private String   nasSource = "WFEM";
	private String   crossSiteScriptingRegex = DEFAULT_CROSS_SITE_SCRIPTING_REGEX;
	private Properties crossSiteRequestForgeryWatchList = new Properties();
	
	/********************************************************************/
	/********************************************************************/
	private Configuration(boolean onInit) {
		super();
	
		if(loadConfiguration(onInit))		
			RefreshNotifier.addListener(this,REFRESH_TIME);
	}
	
	/********************************************************************/
	/********************************************************************/
	public synchronized void refresh(){
		try{
			loadRefreshableConfiguration(loadConfigurationFile(),false);
		}catch(Exception e){
			String errorMsg = "Wfem Exception in refreshing trace configuration : "+e;
			Exception ne = new Exception(errorMsg);
			ne.printStackTrace();
		}
	}
	
	/********************************************************************/
	/********************************************************************/
	public String getWfemlayoutWebApp() {
		if(isOfflineEnvironment())
			return WFEM_LAYOUT_WEBAPP;
		if(!isImageServerForWfemlayoutEnabled())
			return WFEM_LAYOUT_WEBAPP;
		return imageServerUrl+WFEM_LAYOUT_WEBAPP;
	}
	
	/********************************************************************/
	/********************************************************************/
	public String getFileServerUrl(String serverName) {
		return configurationProps.getProperty(serverName+"_FILESERVER_URL");
	}
	
	/********************************************************************/
	private static final int DEF_FILESERVER_BLOCK_SIZE = (16*1024);
	/********************************************************************/
	public int getFileServerStreamBlockSize(String serverName) {
		try{
			String s = configurationProps.getProperty(serverName+"_FILESERVER_STREAMBLOCKSIZE");
			if(s != null)
				return Integer.parseInt(s);
			return DEF_FILESERVER_BLOCK_SIZE;
		}catch(Exception e){
			return DEF_FILESERVER_BLOCK_SIZE;
		}
	}
	
	/********************************************************************/
	private static final long DEF_IN_MEMORY_SIZE = (16*1024); // 16kb
	/********************************************************************/
	public long getFileServerInMemoryFileSize(String serverName) {
		try{
			String s = configurationProps.getProperty(serverName+"_FILESERVER_INMEMORYFILESIZE");
			if(s != null)
				return Long.parseLong(s);
			return DEF_IN_MEMORY_SIZE;
		}catch(Exception e){
			return DEF_IN_MEMORY_SIZE;
		}
	}
	
	/********************************************************************/
	private static final long DEF_ON_DISK_SIZE = (100*(1024*1000)); // 100mb
	/********************************************************************/
	public long getFileServerOnDiskFileSize(String serverName) {
		try{
			String s = configurationProps.getProperty(serverName+"_FILESERVER_ONDISKFILESIZE");
			if(s != null)
				return Long.parseLong(s);
			return DEF_ON_DISK_SIZE;
		}catch(Exception e){
			return DEF_ON_DISK_SIZE;
		}
	}
	
	/********************************************************************/
	/********************************************************************/
	public String getJdbcUrl(String dataSourceName) {
		return configurationProps.getProperty(dataSourceName);
	}
	
	/********************************************************************/
	/********************************************************************/
	public String getJdbcDbUser(String dataSourceName) {
		return configurationProps.getProperty(dataSourceName+"_USER");
	}
	
	/********************************************************************/
	/********************************************************************/
	public String getJdbcDbPassword(String dataSourceName) {
		return configurationProps.getProperty(dataSourceName+"_PASSWORD");
	}

	/********************************************************************/
	/********************************************************************/
	public String getJdbcDbDriver(String dataSourceName) {
		return configurationProps.getProperty(dataSourceName+"_DRIVER");
	}
	
	/********************************************************************/
	/********************************************************************/
	public int getControllerTraceLevel() {
		return controllerTraceLevel;
	}
	
	/********************************************************************/
	/********************************************************************/
	public int getSubsessionsInvalidatorTraceLevel() {
		return subsessionsInvalidatorTraceLevel;
	}
	
	/********************************************************************/
	/********************************************************************/
	public java.lang.String getDataSourcesNames() {
		return dataSourcesNames;
	}

	/********************************************************************/
	/********************************************************************/
	public ArrayList<String> getDataSourcesNamesAsArray() {
		return dataSourcesNamesAsArray;
	}
	
	/********************************************************************/
	/********************************************************************/
	public java.lang.String getSybaseDataSourcesNames() {
		return sybaseDataSourcesNames;
	}

	/********************************************************************/
	/********************************************************************/
	public ArrayList<String> getSybaseDataSourcesNamesAsArray() {
		return sybaseDataSourcesNamesAsArray;
	}
	
	/********************************************************************/
	/********************************************************************/
	public java.lang.String getDbPassword() {
		return dbPassword;
	}
	
	/********************************************************************/
	/********************************************************************/
	public int getDbTraceLevel() {
		return dbTraceLevel;
	}
	
	/********************************************************************/
	/********************************************************************/
	public java.lang.String getDbUser() {
		return dbUser;
	}
	
	/********************************************************************/
	/********************************************************************/
	public java.lang.String getEjbContextFactory() {
		return ejbContextFactory;
	}
	
	/********************************************************************/
	/********************************************************************/
	public java.lang.String getExternalSystemWebapp() {
		return externalSystemWebapp;
	}
	
	/********************************************************************/
	/********************************************************************/
	public java.lang.String getEjbPassword() {
		return ejbPassword;
	}
	
	/********************************************************************/
	/********************************************************************/
	public java.lang.String getEjbProviderUrl() {
		return ejbProviderUrl;
	}
	
	/********************************************************************/
	/********************************************************************/
	public java.lang.String getEjbUser() {
		return ejbUser;
	}
	
	/********************************************************************/
	/********************************************************************/
	public static Configuration initInstance() {
		if(singleton == null) {
	        synchronized (Configuration.class) {
				if(singleton == null) {
					singleton = new Configuration(true);
				}
	        }
		}
		return singleton;
	}
	
	/********************************************************************/
	/********************************************************************/
	public static Configuration getInstance() {
		if(singleton == null) {
	        synchronized (Configuration.class) {
				if(singleton == null) {
					singleton = new Configuration(false);
				}
	        }
		}
		return singleton;
	}
	
	/********************************************************************/
	/********************************************************************/
	public int getNumOfCallableExceptionIgnored() {
		return numOfCallableExceptionIgnored;
	}
	
	/********************************************************************/
	/********************************************************************/
	public java.lang.String getPdfProviderUrl() {
		return pdfProviderUrl;
	}
	
	/********************************************************************/
	/********************************************************************/
	public int getTraceLevel() {
		return traceLevel;
	}
	
	/********************************************************************/
	/********************************************************************/
	public int getUtilTraceLevel() {
		return utilTraceLevel;
	}
	
	/********************************************************************/
	/********************************************************************/
	public int getQASTraceLevel() {
		return qasTraceLevel;
	}
	
	/********************************************************************/
	/********************************************************************/
	public int getOSBTraceLevel() {
		return osbTraceLevel;
	}
	
	/********************************************************************/
	/********************************************************************/
	public boolean isImageServerForWebApplEnabled(String webApp) {
		return imageServerWfemWebAppEnabledMap.getProperty(webApp) != null ? true : false;
	}

	/********************************************************************/
	/********************************************************************/
	private void loadRefreshableConfiguration(Properties properties, boolean forcePrint) {
		
		String param = "";
		int tmpValue = 0;
		boolean tmpBoolValue = false;
		String s = "Wfem configuration: ";
		
		try	{
	
			s += "Parameter ";
			
			// Controller trace level
			tmpValue = 0;
			param = readParameter(properties,PARM_CONTROLLER_TRACE_LEVEL);
		    if(param != null) {
		    	try {
		    		tmpValue = new Integer(param).intValue();
		    	}catch(Exception e){
				    LogPrinter.println(s+"Controller trace parameter ("+PARM_CONTROLLER_TRACE_LEVEL+") must be a number between 0 and 3");
		    	}
		    }
		    if(tmpValue != controllerTraceLevel){
		    	controllerTraceLevel = tmpValue;
			    LogPrinter.println(s+PARM_CONTROLLER_TRACE_LEVEL+": Controller trace setted to value ["+controllerTraceLevel+"]");
		    }else if(forcePrint){
			    LogPrinter.println(s+PARM_CONTROLLER_TRACE_LEVEL+": Controller trace setted to value ["+controllerTraceLevel+"]");
		    }
		    
			// Application trace level
		    tmpValue = 0;
			param = readParameter(properties,PARM_TRACE_LEVEL);
		    if(param != null) {
		    	try {
		    		tmpValue = new Integer(param).intValue();
		    	}catch(Exception e){
				    LogPrinter.println(s+"Applications trace parameter ("+PARM_TRACE_LEVEL+") must be a number between 0 and 3");
		    	}
		    }	       
		    if(tmpValue != traceLevel){
		    	traceLevel = tmpValue;
			    LogPrinter.println(s+PARM_TRACE_LEVEL+": Applications trace setted to value ["+traceLevel+"]");
		    }else if(forcePrint){
			    LogPrinter.println(s+PARM_TRACE_LEVEL+": Applications trace setted to value ["+traceLevel+"]");
		    }
	
			// DB trace level
			tmpValue = 0;
			param = readParameter(properties,PARM_DB_TRACE_LEVEL);
		    if(param != null) {
		    	try {
		    		tmpValue = new Integer(param).intValue();
		    	}catch(Exception e){
				    LogPrinter.println(s+"DB trace parameter ("+PARM_DB_TRACE_LEVEL+") must be a number between 0 and 3");
		    	}
		    }
		    if(tmpValue != dbTraceLevel){
		    	dbTraceLevel = tmpValue;
			    LogPrinter.println(s+PARM_DB_TRACE_LEVEL+": DB trace setted to value ["+dbTraceLevel+"]");
		    }else if(forcePrint){
			    LogPrinter.println(s+PARM_DB_TRACE_LEVEL+": DB trace setted to value ["+dbTraceLevel+"]");
		    }
		    
			// DAO trace level
			tmpValue = 0;
			param = readParameter(properties,PARM_DAO_TRACE_LEVEL);
		    if(param != null) {
		    	try {
		    		tmpValue = new Integer(param).intValue();
		    	}catch(Exception e){
				    LogPrinter.println(s+"DAO trace parameter ("+PARM_DAO_TRACE_LEVEL+") must be a number between 0 and 3");
		    	}
		    }
		    if(tmpValue != daoTraceLevel){
		    	daoTraceLevel = tmpValue;
			    LogPrinter.println(s+PARM_DAO_TRACE_LEVEL+": DAO trace setted to value ["+daoTraceLevel+"]");
		    }else if(forcePrint){
			    LogPrinter.println(s+PARM_DAO_TRACE_LEVEL+": DAO trace setted to value ["+daoTraceLevel+"]");
		    }
		    
			// Layout trace level
			tmpValue = 0;
			param = readParameter(properties,PARM_LAYOUT_TRACE_LEVEL);
		    if(param != null) {
		    	try {
		    		tmpValue = new Integer(param).intValue();
		    	}catch(Exception e){
				    LogPrinter.println(s+"Layout trace parameter ("+PARM_LAYOUT_TRACE_LEVEL+") must be a number between 0 and 3");
		    	}
		    }
		    if(tmpValue != layoutTraceLevel){
		    	layoutTraceLevel = tmpValue;
			    LogPrinter.println(s+PARM_LAYOUT_TRACE_LEVEL+": Layout trace setted to value ["+layoutTraceLevel+"]");
		    }else if(forcePrint){
			    LogPrinter.println(s+PARM_LAYOUT_TRACE_LEVEL+": Layout trace setted to value ["+layoutTraceLevel+"]");
		    }
		    
			// HtmlToPdf trace level
			tmpValue = 0;
			param = readParameter(properties,PARM_HTMLTOPDF_TRACE_LEVEL);
		    if(param != null) {
		    	try {
		    		tmpValue = new Integer(param).intValue();
		    	}catch(Exception e){
				    LogPrinter.println(s+"HtmlToPdf trace parameter ("+PARM_HTMLTOPDF_TRACE_LEVEL+") must be a number between 0 and 3");
		    	}
		    }
		    if(tmpValue != htmlToPdfTraceLevel){
		    	htmlToPdfTraceLevel = tmpValue;
			    LogPrinter.println(s+PARM_HTMLTOPDF_TRACE_LEVEL+": HtmlToPdf trace setted to value ["+htmlToPdfTraceLevel+"]");
		    }else if(forcePrint){
			    LogPrinter.println(s+PARM_HTMLTOPDF_TRACE_LEVEL+": HtmlToPdf trace setted to value ["+htmlToPdfTraceLevel+"]");
		    }
		    
			// Pdf trace level
			tmpValue = 0;
			param = readParameter(properties,PARM_PDF_TRACE_LEVEL);
		    if(param != null) {
		    	try {
		    		tmpValue = new Integer(param).intValue();
		    	}catch(Exception e){
				    LogPrinter.println(s+"Pdf trace parameter ("+PARM_PDF_TRACE_LEVEL+") must be a number between 0 and 3");
		    	}
		    }
		    if(tmpValue != pdfTraceLevel){
		    	pdfTraceLevel = tmpValue;
			    LogPrinter.println(s+PARM_PDF_TRACE_LEVEL+": Pdf trace setted to value ["+pdfTraceLevel+"]");
		    }else if(forcePrint){
			    LogPrinter.println(s+PARM_PDF_TRACE_LEVEL+": Pdf trace setted to value ["+pdfTraceLevel+"]");
		    }
		    
			// Refresher trace level
			tmpValue = 0;
			param = readParameter(properties,PARM_REFRESHER_TRACE_LEVEL);
		    if(param != null) {
		    	try {
		    		tmpValue = new Integer(param).intValue();
		    	}catch(Exception e){
				    LogPrinter.println(s+"Refresher trace parameter ("+PARM_REFRESHER_TRACE_LEVEL+") must be a number between 0 and 3");
		    	}
		    }
		    if(tmpValue != refresherTraceLevel){
		    	refresherTraceLevel = tmpValue;
			    LogPrinter.println(s+PARM_REFRESHER_TRACE_LEVEL+": Refresher trace setted to value ["+refresherTraceLevel+"]");
		    }else if(forcePrint){
			    LogPrinter.println(s+PARM_REFRESHER_TRACE_LEVEL+": Refresher trace setted to value ["+refresherTraceLevel+"]");
		    }
		    
			// Util trace level
			tmpValue = 0;
			param = readParameter(properties,PARM_UTIL_TRACE_LEVEL);
		    if(param != null) {
		    	try {
		    		tmpValue = new Integer(param).intValue();
		    	}catch(Exception e){
				    LogPrinter.println(s+"Util trace Parameter ("+PARM_UTIL_TRACE_LEVEL+") must be a number between 0 and 3");
		    	}
		    }
		    if(tmpValue != utilTraceLevel){
		    	utilTraceLevel = tmpValue;
			    LogPrinter.println(s+PARM_UTIL_TRACE_LEVEL+": Util trace setted to value ["+utilTraceLevel+"]");
		    }else if(forcePrint){
			    LogPrinter.println(s+PARM_UTIL_TRACE_LEVEL+": Util trace setted to value ["+utilTraceLevel+"]");
		    }
		    
			// Qas trace level
			tmpValue = 0;
			param = readParameter(properties,PARM_QAS_TRACE_LEVEL);
		    if(param != null) {
		    	try {
		    		tmpValue = new Integer(param).intValue();
		    	}catch(Exception e){
				    LogPrinter.println(s+"Qas trace Parameter ("+PARM_QAS_TRACE_LEVEL+") must be a number between 0 and 3");
		    	}
		    }
		    if(tmpValue != qasTraceLevel){
		    	qasTraceLevel = tmpValue;
			    LogPrinter.println(s+PARM_QAS_TRACE_LEVEL+": Qas trace setted to value ["+qasTraceLevel+"]");
		    }else if(forcePrint){
			    LogPrinter.println(s+PARM_QAS_TRACE_LEVEL+": Qas trace setted to value ["+qasTraceLevel+"]");
		    }
		    
			// OSB trace level
			tmpValue = 0;
			param = readParameter(properties,PARM_OSB_TRACE_LEVEL);
		    if(param != null) {
		    	try {
		    		tmpValue = new Integer(param).intValue();
		    	}catch(Exception e){
				    LogPrinter.println(s+"Web Services trace Parameter ("+PARM_OSB_TRACE_LEVEL+") must be a number between 0 and 3");
		    	}
		    }
		    if(tmpValue != osbTraceLevel){
		    	osbTraceLevel = tmpValue;
			    LogPrinter.println(s+PARM_OSB_TRACE_LEVEL+": OSB trace setted to value ["+osbTraceLevel+"]");
		    }else if(forcePrint){
			    LogPrinter.println(s+PARM_OSB_TRACE_LEVEL+": OSB Services trace setted to value ["+osbTraceLevel+"]");
		    }

		    // ROF trace level
			tmpValue = 0;
			param = readParameter(properties,PARM_ROF_TRACE_LEVEL);
		    if(param != null) {
		    	try {
		    		tmpValue = new Integer(param).intValue();
		    	}catch(Exception e){
				    LogPrinter.println(s+"ROF trace Parameter ("+PARM_ROF_TRACE_LEVEL+") must be a number between 0 and 3");
		    	}
		    }
		    if(tmpValue != rofTraceLevel){
		    	rofTraceLevel = tmpValue;
			    LogPrinter.println(s+PARM_ROF_TRACE_LEVEL+": ROF trace setted to value ["+rofTraceLevel+"]");
		    }else if(forcePrint){
			    LogPrinter.println(s+PARM_ROF_TRACE_LEVEL+": ROF trace setted to value ["+rofTraceLevel+"]");
		    }
		    
		    // DB Connection trace enabled
			boolean tmpBTrace = dbConnectionTraceEnabled;
			param = readParameter(properties,PARM_DBCONNECTION_TRACE_ENABLED);
			if(param != null){
				if(param.equalsIgnoreCase("true"))
					tmpBTrace = true;
				else
					tmpBTrace = false;
			}
		    if(tmpBTrace != dbConnectionTraceEnabled){
		    	dbConnectionTraceEnabled = tmpBTrace;
			    LogPrinter.println(s+PARM_DBCONNECTION_TRACE_ENABLED+": DB Connections trace setted to value ["+dbConnectionTraceEnabled+"]");
		    }else if(forcePrint){
			    LogPrinter.println(s+PARM_DBCONNECTION_TRACE_ENABLED+": DB Connections trace setted to value ["+dbConnectionTraceEnabled+"]");
		    }
		    
			// Mail trace level
			tmpValue = 0;
			param = readParameter(properties,PARM_MAIL_TRACE_LEVEL);
		    if(param != null) {
		    	try {
		    		tmpValue = new Integer(param).intValue();
		    	}catch(Exception e){
				    LogPrinter.println(s+"Mail trace parameter ("+PARM_MAIL_TRACE_LEVEL+") must be a number between 0 and 3");
		    	}
		    }
		    if(tmpValue != mailTraceLevel){
		    	mailTraceLevel = tmpValue;
			    LogPrinter.println(s+PARM_MAIL_TRACE_LEVEL+": Mail trace setted to value ["+mailTraceLevel+"]");
		    }else if(forcePrint){
			    LogPrinter.println(s+PARM_MAIL_TRACE_LEVEL+": Mail trace setted to value ["+mailTraceLevel+"]");
		    }
		    
			// Subsession invalidator trace level
			tmpValue = 0;
			param = readParameter(properties,PARM_SUBSESSIONS_INVALIDATOR_TRACE_LEVEL);
		    if(param != null) {
		    	try {
		    		tmpValue = new Integer(param).intValue();
		    	}catch(Exception e){
				    LogPrinter.println(s+"Subsessions invalidator trace parameter ("+PARM_SUBSESSIONS_INVALIDATOR_TRACE_LEVEL+") must be a number between 0 and 3");
		    	}
		    }
		    if(tmpValue != subsessionsInvalidatorTraceLevel){
		    	subsessionsInvalidatorTraceLevel = tmpValue;
			    LogPrinter.println(s+PARM_SUBSESSIONS_INVALIDATOR_TRACE_LEVEL+": Subsessions invalidator trace setted to value ["+subsessionsInvalidatorTraceLevel+"]");
		    }else if(forcePrint){
			    LogPrinter.println(s+PARM_SUBSESSIONS_INVALIDATOR_TRACE_LEVEL+": Subsessions invalidator trace setted to value ["+subsessionsInvalidatorTraceLevel+"]");
		    }

		    // QAS Service call simulation
		    tmpBoolValue = qasSimulationEnabled;
			param = readParameter(properties,PARM_QAS_SIMULATION_ENABLED);
			if(param != null){
			    tmpBoolValue = false;
				if(param.equalsIgnoreCase("true"))
					tmpBoolValue = true;
			}
			if(tmpBoolValue != qasSimulationEnabled){
				qasSimulationEnabled = tmpBoolValue;
				LogPrinter.println(s+PARM_QAS_SIMULATION_ENABLED+": QAS service caller simulation setted to: ["+qasSimulationEnabled+"]");
			}else if(forcePrint){
				LogPrinter.println(s+PARM_QAS_SIMULATION_ENABLED+": QAS service caller simulation setted to: ["+qasSimulationEnabled+"]");				
			}
		    
		    // OSB Service call simulation
		    tmpBoolValue = osbSimulationEnabled;
			param = readParameter(properties,PARM_OSB_SIMULATION_ENABLED);
			if(param != null){
			    tmpBoolValue = false;
				if(param.equalsIgnoreCase("true"))
					tmpBoolValue = true;
			}
			if(tmpBoolValue != osbSimulationEnabled){
				osbSimulationEnabled = tmpBoolValue;
				LogPrinter.println(s+PARM_OSB_SIMULATION_ENABLED+": OSB caller simulation setted to: ["+osbSimulationEnabled+"]");
			}else if(forcePrint){
				LogPrinter.println(s+PARM_OSB_SIMULATION_ENABLED+": OSB caller simulation setted to: ["+osbSimulationEnabled+"]");				
			}
			
		    // Traced user
			param = readParameter(properties,PARM_TRACED_USERCODE);
			if(param == null)
			    param = "";
			if(!param.equals(tracedUserCode)){
				tracedUserCode = param;
				LogPrinter.println(s+PARM_TRACED_USERCODE+": Traced Usercode setted to: ["+tracedUserCode+"]");
			}else if(forcePrint){
				LogPrinter.println(s+PARM_TRACED_USERCODE+": Traced Usercode setted to: ["+tracedUserCode+"]");
			}
		    
		    // Model trace enabled
			if(modelTraceDir != null){
			    tmpBoolValue = modelTraceEnabled;
				param = readParameter(properties,PARM_MODEL_TRACE_ENABLED);
				if(param != null){
				    tmpBoolValue = false;
					if(param.equalsIgnoreCase("true"))
						tmpBoolValue = true;
				}
				if(tmpBoolValue != modelTraceEnabled){
					modelTraceEnabled = tmpBoolValue;
					LogPrinter.println(s+PARM_MODEL_TRACE_ENABLED+": Model trace setted to: ["+modelTraceEnabled+"]");
				}else if(forcePrint){
					LogPrinter.println(s+PARM_MODEL_TRACE_ENABLED+": Model trace setted to: ["+modelTraceEnabled+"]");				
				}
			}else{
				modelTraceEnabled = false;
			}
			
			// KeepAlive minutes
			tmpValue = 15;
			param = readParameter(properties,PARM_KEEPALIVE_TIMER_MINUTES);
		    if(param != null) {
		    	try {
		    		tmpValue = new Integer(param).intValue();
		    	}catch(Exception e){
				    System.out.println(s+"Keepalive param ("+PARM_KEEPALIVE_TIMER_MINUTES+") must be an integer number");
		    	}
		    }
		    if(tmpValue != keepaliveTimerMinutes){
		    	keepaliveTimerMinutes = tmpValue;
			    System.out.println(s+PARM_KEEPALIVE_TIMER_MINUTES+": keepalive timer setted to : ["+keepaliveTimerMinutes+"] minutes");
		    }else if(forcePrint){
			    System.out.println(s+PARM_KEEPALIVE_TIMER_MINUTES+": keepalive timer setted to : ["+keepaliveTimerMinutes+"] minutes");
		    }
			
			// Accept context only from header (AS is protected)
		    tmpBoolValue = acceptCtxOnlyOnHeader;
			param = readParameter(properties,PARM_ACCEPT_CTX_ONLY_ON_HEADER);
			if(param != null){
			    tmpBoolValue = false;
				if(param.equalsIgnoreCase("true"))
					tmpBoolValue = true;
			}
			if(tmpBoolValue != acceptCtxOnlyOnHeader){
				acceptCtxOnlyOnHeader = tmpBoolValue;
				System.out.println(s+PARM_ACCEPT_CTX_ONLY_ON_HEADER+": accept context only on header flag setted to: ["+acceptCtxOnlyOnHeader+"]");
			}else if(forcePrint){
				System.out.println(s+PARM_ACCEPT_CTX_ONLY_ON_HEADER+": accept context only on header flag setted to: ["+acceptCtxOnlyOnHeader+"]");				
			}
		    
		    // DynatraceSrc
			param = readParameter(properties,PARM_DYNATRACE_SRC);
			if(param == null)
				param = "";
			if(!dynatraceSrc.equals(param)){
				dynatraceSrc = param;
				System.out.println(s+PARM_DYNATRACE_SRC+": Dynatrace Src setted to: ["+dynatraceSrc+"]");
			}else if(forcePrint){
				System.out.println(s+PARM_DYNATRACE_SRC+": Dynatrace Src setted to: ["+dynatraceSrc+"]");
			}
			
			// NAS source
			param = readParameter(properties,PARM_NAS_SOURCE);
			if(param == null)
				param = "WFEM";
			if(!nasSource.equals(param)){
				nasSource = param;
				System.out.println(s+PARM_NAS_SOURCE+": NAS source setted to : ["+nasSource+"]");
			}else if(forcePrint){
				System.out.println(s+PARM_NAS_SOURCE+": NAS source setted to : ["+nasSource+"]");
			}

			// Cross scripting elimination regular expression
			param = readParameter(properties,PARM_CROSS_SITE_SCRIPTING_REGEX);
			if(param == null)
				param = DEFAULT_CROSS_SITE_SCRIPTING_REGEX;
			if(!crossSiteScriptingRegex.equals(param)){
				crossSiteScriptingRegex = param;
				System.out.println(s+PARM_CROSS_SITE_SCRIPTING_REGEX+": Cross Site Scripting regular expression sanitizer setted to : ["+crossSiteScriptingRegex+"]");
			}else if(forcePrint){
				System.out.println(s+PARM_CROSS_SITE_SCRIPTING_REGEX+": Cross Site Scripting regular expression sanitizer setted to : ["+crossSiteScriptingRegex+"]");
			}

			// Image Server URL (Only online)
			if(isOnlineEnvironment()){
				
			    // Image server for WfemLayout enabled
				tmpBoolValue = imageServerForWfemlayoutEnabled;
				param = readParameter(properties,PARM_IMAGE_SERVER_FOR_WFEMLAYOUT_ENABLED);
				if(param != null){
					if(param.equalsIgnoreCase("false"))
						tmpBoolValue = false;
					else
						tmpBoolValue = true;
				}
			    if(tmpBoolValue != imageServerForWfemlayoutEnabled){
			    	imageServerForWfemlayoutEnabled = tmpBoolValue;
				    LogPrinter.println(s+PARM_IMAGE_SERVER_FOR_WFEMLAYOUT_ENABLED+": image server management for WfemLayout setted to value ["+imageServerForWfemlayoutEnabled+"]");
			    }else if(forcePrint){
				    LogPrinter.println(s+PARM_IMAGE_SERVER_FOR_WFEMLAYOUT_ENABLED+": image server management for WfemLayout setted to value ["+imageServerForWfemlayoutEnabled+"]");
			    }

			    // Image server URL
				param = readParameter(properties,PARM_IMAGE_SERVER_URL);
				if(param == null)
					param = "";
				if(!imageServerUrl.equals(param)){
					imageServerUrl = param;
					LogPrinter.println(s+PARM_IMAGE_SERVER_URL+": Image Server url url setted to: ["+imageServerUrl+"]");
				}else if(forcePrint){
					LogPrinter.println(s+PARM_IMAGE_SERVER_URL+": Image Server url setted to: ["+imageServerUrl+"]");
				}
				
			}else{
				imageServerForWfemlayoutEnabled = false;
			}

		}catch(Exception e){
			
			String errorMsg = "Wfem Exception in loading configuration file: "+e;
			Exception ne = new Exception(errorMsg);
			ne.printStackTrace();
			
		}
	}
	
	/********************************************************************/
	/********************************************************************/
	private Properties loadConfigurationFile() throws Exception{
		try{
			InputStream inputStream = this.getClass().getResourceAsStream(CONFIGURATION_FILE);
			if(inputStream == null){
				String errorMsg = "Wfem configuration file ["+CONFIGURATION_FILE+"] not found";
				System.err.println(errorMsg);
				return null;
			}
			configurationProps.load(inputStream);
			inputStream.close();
			return configurationProps;
		}catch(IOException ioe){
			String errorMsg = "Wfem configuration file ["+CONFIGURATION_FILE+"] not found";
			Exception e = new Exception(errorMsg);
			e.printStackTrace();
			return null;
		}
	}
	
	/********************************************************************/
	/********************************************************************/
	public boolean loadConfiguration() {
		return loadConfiguration(false);
	}
	
	/********************************************************************/
	/********************************************************************/
	public boolean loadConfiguration(boolean onInit) {
	
		String param = "";
		String s = "Wfem global configuration: ";
		
		try	{
	
		    // Web Applications enabled for Image Server
			InputStream inputStream = this.getClass().getResourceAsStream(WFEM_IMAGE_SERVER_WEBAPP_ENABLED);
			if(inputStream != null){
				imageServerWfemWebAppEnabledMap.load(inputStream);
				inputStream.close();
			}
			
			Properties properties = loadConfigurationFile();
			if(properties == null)
				return false;
			
			// Log file
			logFilename = readParameter(properties,PARM_LOG_FILENAME);
			if(logFilename != null && logFilename.isEmpty())
				logFilename = null;
			if(logFilename != null && onInit){
				File f = new File(logFilename);
				if(f.exists()){
					String backFilename = logFilename.toString();
					try{
						String ext = "";
						int idx = backFilename.lastIndexOf(".");
						if(idx >= 0){
							ext = backFilename.substring(idx);
							backFilename = backFilename.substring(0,idx);
						}
						String now = ""+Tools.now();
						File backFile = new File(backFilename+" "+now.replaceAll("\\:","-")+ext);
						if(!f.renameTo(backFile))
							System.out.println("Wfem logfile rotation: unable to rename ["+f.getName()+"] to ["+backFile.getName()+"]");
					}catch(Throwable t){
						t.printStackTrace();
					}
				}
			}
			
			if(onInit)
				LogPrinter.initLogFilename(logFilename);

			LogPrinter.println(s+"Loading global configuration from file "+CONFIGURATION_FILE);

			s += "Parameter ";
			
		    // Environment	
			environment = readParameter(properties,PARM_ENVIRONMENT);
			if(environment != null){
				LogPrinter.println(s+PARM_ENVIRONMENT+": ENVIRONMENT gobal configuration is setted to ["+environment+"]");
			}else{
				environment = "";
				LogPrinter.println(s+PARM_ENVIRONMENT+": ATTENTION! ENVIRONMENT gobal configuration is not defined");
			}

			// Trace browser instances
			traceBrowserInstancesNotFoundActive = false;
			param = readParameter(properties,PARM_TRACE_BROWSERINSTANCES_NOTFOUND);
			if(param != null && param.equalsIgnoreCase("true"))
				traceBrowserInstancesNotFoundActive = true;
			LogPrinter.println(s+PARM_TRACE_BROWSERINSTANCES_NOTFOUND+": Parameter setted to: ["+traceBrowserInstancesNotFoundActive+"]");

			// Trusted site
			trustedSite = false;
			param = readParameter(properties,PARM_TRUSTED_SITE);
			if(param != null && param.equalsIgnoreCase("true"))
				trustedSite = true;
			LogPrinter.println(s+PARM_TRUSTED_SITE+": Parameter setted to: ["+trustedSite+"]");
		    
			// Database User
			dbUser = readParameter(properties,PARM_DATABASE_USER);
			if(dbUser == null){
			    LogPrinter.println(s+PARM_DATABASE_USER+": Database User is not definded");
			}else{
			    LogPrinter.println(s+PARM_DATABASE_USER+": Database User is setted to: ["+dbUser+"]");
			}
			
			// Database User Password
			if(dbUser != null){
				dbPassword = readParameter(properties,PARM_DATABASE_PASSWORD);
				if(dbPassword == null)
					dbPassword = "";
			    LogPrinter.println(s+PARM_DATABASE_PASSWORD+": Database Password is setted to: ["+dbPassword+"]");
			}else{
			    LogPrinter.println(s+PARM_DATABASE_PASSWORD+": Database Password is not defined since Database User is not defined");
			}
	
		    // Datasources	
			dataSourcesNames = readParameter(properties,PARM_DATASOURCES_NAMES);
			if(dataSourcesNames != null){
				dataSourcesNamesAsArray = new ArrayList<String>(Arrays.asList(dataSourcesNames.toUpperCase().split("[,;]")));
				LogPrinter.println(s+PARM_DATASOURCES_NAMES+": Defined preloaded Datasources names are ["+dataSourcesNames+"]");
			}else{
				LogPrinter.println(s+PARM_DATASOURCES_NAMES+": No preloaded Datasource name defined");
			}
			
		    // Sybase Datasources	
			sybaseDataSourcesNames = readParameter(properties,PARM_SYBASE_DATASOURCES_NAMES);
			if(sybaseDataSourcesNames != null && sybaseDataSourcesNames.length() > 0){
				sybaseDataSourcesNamesAsArray = new ArrayList<String>(Arrays.asList(sybaseDataSourcesNames.toUpperCase().split("[,;]")));
				LogPrinter.println(s+PARM_SYBASE_DATASOURCES_NAMES+": Sybase Datasources names are ["+sybaseDataSourcesNames+"]");
			}

			// Number of ignored exception in callable statement	
			param = readParameter(properties,PARM_NUM_OF_CALLABLE_EXCEPTION_IGNORED);
		    if(param != null) {
		    	try {
		    		numOfCallableExceptionIgnored = new Integer(param).intValue();
		    	}catch(Exception e){
					numOfCallableExceptionIgnored = 100;
		    	}
		    }
		    LogPrinter.println(s+PARM_NUM_OF_CALLABLE_EXCEPTION_IGNORED+": Number of ignored exception in callable statement setted to value ["+numOfCallableExceptionIgnored+"]");
		    
			// Pdf provider URL
			pdfProviderUrl = readParameter(properties,PARM_PDF_PROVIDER_URL);
			if(pdfProviderUrl == null){
			    LogPrinter.println(s+PARM_PDF_PROVIDER_URL+": PDF Provider URL is not defined");
			}else{
			    LogPrinter.println(s+PARM_PDF_PROVIDER_URL+": PDF Provider URL is setted to: ["+pdfProviderUrl+"]");
			}	
			
			// Ejb provider URL
			ejbProviderUrl = readParameter(properties,PARM_EJB_PROVIDER_URL);
			if(ejbProviderUrl == null){
			    LogPrinter.println(s+PARM_EJB_PROVIDER_URL+": EJB Provider URL is not defined");
			}else{
			    LogPrinter.println(s+PARM_EJB_PROVIDER_URL+": EJB Provider URL is setted to: ["+ejbProviderUrl+"]");
			}	
			
			// Ejb provider user
			if(ejbProviderUrl != null){
				ejbUser = readParameter(properties,PARM_EJB_USER);
				if(ejbUser == null){
				    LogPrinter.println(s+PARM_EJB_USER+": EJB User is not defined");
				}else{
				    LogPrinter.println(s+PARM_EJB_USER+": EJB User is setted to: ["+ejbUser+"]");
				}
			}else{
			    LogPrinter.println(s+PARM_EJB_USER+": EJB User is not defined since EJB Provider is not defined");
			}
			
			// Ejb provider password
			if(ejbUser != null){
				ejbPassword = readParameter(properties,PARM_EJB_PASSWORD);
				if(ejbPassword == null)
					ejbPassword = "";
			    LogPrinter.println(s+PARM_EJB_PASSWORD+": EJB Password is setted to: ["+ejbPassword+"]");
			}else{
			    LogPrinter.println(s+PARM_EJB_PASSWORD+": EJB Password is not defined since EJB User is not defined");
			}
			
			// Ejb Context Factory
			ejbContextFactory = properties.getProperty(PARM_EJB_CONTEXT_FACTORY);
			if(ejbContextFactory != null){
				ejbContextFactory = ejbContextFactory.trim();
				if(ejbContextFactory.equals(""))
					ejbContextFactory = null;
			}
			if(ejbContextFactory == null)
				LogPrinter.println(s+PARM_EJB_CONTEXT_FACTORY+": EJB context factory is setted to default (No parameters)");
			else
				LogPrinter.println(s+PARM_EJB_CONTEXT_FACTORY+": EJB context factory is setted to: ["+ejbContextFactory+"]");
									
			// Command stack size
			param = readParameter(properties,PARM_COMMAND_STACK_SIZE);
		    if(param != null) {
		    	try {
		    		commandStackSize = new Integer(param).intValue();
		    		commandStackSize++; // One stack is reserved for menù
		    	}catch(Exception e){
					commandStackSize = 5;
		    	}
		    }
		    LogPrinter.println(s+PARM_COMMAND_STACK_SIZE+": Command stack size setted to value ["+commandStackSize+"]");
		    
		    // Use cache in ObjectPropertiesCache
			objectsCacheEnabled = true;
			param = readParameter(properties,PARM_OBJECTS_CACHE_ENABLED);
			if(param != null){
				if(param.equalsIgnoreCase("false"))
					objectsCacheEnabled = false;
			}
		    LogPrinter.println(s+PARM_OBJECTS_CACHE_ENABLED+": Flag to enable objects cache in ObjectPropertiesCache is setted to: ["+objectsCacheEnabled+"]");
		    
		    // Use cache in RemoteObjectFactory
			remoteObjectsCacheEnabled = true;
			param = readParameter(properties,PARM_REMOTE_OBJECTS_CACHE_ENABLED);
			if(param != null){
				if(param.equalsIgnoreCase("false"))
					remoteObjectsCacheEnabled = false;
			}
		    LogPrinter.println(s+PARM_REMOTE_OBJECTS_CACHE_ENABLED+": Flag to enable remote objects cache in RemoteObjectFactory is setted to: ["+remoteObjectsCacheEnabled+"]");
		    
		    // Use cache in DAO
			daoCacheEnabled = true;
			param = readParameter(properties,PARM_DAO_CACHE_ENABLED);
			if(param != null){
				if(param.equalsIgnoreCase("false"))
					daoCacheEnabled = false;
			}
		    LogPrinter.println(s+PARM_DAO_CACHE_ENABLED+": Flag to enable DAO cache is setted to: ["+daoCacheEnabled+"]");

		    // JDBC driver
			param = readParameter(properties,PARM_JDBC_DRIVER);
			if(param != null && !param.equals("")){
				jdbcDriver = param;
			    LogPrinter.println(s+PARM_JDBC_DRIVER+": JDBC Driver is setted to: ["+jdbcDriver+"]");
			}else{
			    LogPrinter.println(s+PARM_JDBC_DRIVER+": JDBC Driver is not setted. Datasources must be defined");
			}
	
		    // DB Connection trace dir
			dbConnectionTraceDir = readParameter(properties,PARM_DBCONNECTION_TRACE_DIR);
			if(dbConnectionTraceDir == null)
				dbConnectionTraceDir = System.getProperty("user.dir");
			boolean dirExist = true;
			File f = new File(dbConnectionTraceDir);
			if(!f.exists()){
				dirExist = false;
				f.mkdir();
			}
			if(!dbConnectionTraceDir.endsWith("/"))
				dbConnectionTraceDir += "/";		    
			if(dirExist)
			    LogPrinter.println(s+PARM_DBCONNECTION_TRACE_DIR+": DB Connection trace dir is setted to: ["+dbConnectionTraceDir+"]");
			else
			    LogPrinter.println(s+PARM_DBCONNECTION_TRACE_DIR+": DB Connection trace dir is setted to: ["+dbConnectionTraceDir+"]. Directory was created on server");
		    
		    // DB java classes dir
			dbJavaClassesDir = readParameter(properties,PARM_DB_JAVA_CLASSES_DIR);
			if(dbJavaClassesDir == null)
				dbJavaClassesDir = System.getProperty("user.dir");
			dirExist = true;
			f = new File(dbJavaClassesDir);
			if(!f.exists()){
				dirExist = false;
				f.mkdir();
			}
			if(!dbJavaClassesDir.endsWith("/"))
				dbJavaClassesDir += "/";		    
			if(dirExist)
			    LogPrinter.println(s+PARM_DB_JAVA_CLASSES_DIR+": DB java classes dir is setted to: ["+dbJavaClassesDir+"]");
			else
			    LogPrinter.println(s+PARM_DB_JAVA_CLASSES_DIR+": DB java classes dir is setted to: ["+dbJavaClassesDir+"]. Directory was created on server");

		    // TEST java classes dir
			testJavaClassesDir = readParameter(properties,PARM_TEST_JAVA_CLASSES_DIR);
			if(testJavaClassesDir == null)
				testJavaClassesDir = System.getProperty("user.dir");
			dirExist = true;
			f = new File(testJavaClassesDir);
			if(!f.exists()){
				dirExist = false;
				f.mkdir();
			}
			if(!testJavaClassesDir.endsWith("/"))
				testJavaClassesDir += "/";		    
			if(dirExist)
			    LogPrinter.println(s+PARM_TEST_JAVA_CLASSES_DIR+": TEST java classes dir is setted to: ["+testJavaClassesDir+"]");
			else
			    LogPrinter.println(s+PARM_TEST_JAVA_CLASSES_DIR+": TEST java classes dir is setted to: ["+testJavaClassesDir+"]. Directory was created on server");

		    // Model trace dir
			modelTraceDir = readParameter(properties,PARM_MODEL_TRACE_DIR);
			if(modelTraceDir != null){
				dirExist = true;
				f = new File(modelTraceDir);
				if(!f.exists()){
					dirExist = false;
					f.mkdir();
				}
				if(!modelTraceDir.endsWith("/"))
					modelTraceDir += "/";		    
				if(dirExist)
				    LogPrinter.println(s+PARM_MODEL_TRACE_DIR+": Model trace dir is setted to: ["+modelTraceDir+"]");
				else
				    LogPrinter.println(s+PARM_MODEL_TRACE_DIR+": Model trace dir is setted to: ["+modelTraceDir+"]. Directory was created on server");
			}else{
			    LogPrinter.println(s+PARM_MODEL_TRACE_DIR+": Model trace dir is not setted. Model tracing is not activable");
			}

		    // Tier trace dir
			tierTraceDir = readParameter(properties,PARM_TIER_TRACE_DIR);
			if(tierTraceDir != null){
				dirExist = true;
				f = new File(tierTraceDir);
				if(!f.exists()){
					dirExist = false;
					f.mkdir();
				}
				if(!tierTraceDir.endsWith("/"))
					tierTraceDir += "/";		    
				if(dirExist)
				    LogPrinter.println(s+PARM_TIER_TRACE_DIR+": Tier trace dir is setted to: ["+tierTraceDir+"]");
				else
				    LogPrinter.println(s+PARM_TIER_TRACE_DIR+": Tier trace dir is setted to: ["+tierTraceDir+"]. Directory was created on server");
			}else{
			    LogPrinter.println(s+PARM_TIER_TRACE_DIR+": Tier trace dir is not setted. Tier tracing is not available");
			}

			// Environment type
			environmentType = readParameter(properties,PARM_ENVIRONMENT_TYPE);
			if(environmentType == null || environmentType.equals("")){
				environmentType = ONLINE_ENVIRONMENT;
			}
			LogPrinter.println(s+PARM_ENVIRONMENT_TYPE+": Environment type is setted to ["+environmentType+"]");
			
		    // Jaguar servlet context class
			jaguarServletContextClass = readParameter(properties,PARM_JAGUAR_SERVLET_CONTEXT_CLASS);
			LogPrinter.println(s+PARM_JAGUAR_SERVLET_CONTEXT_CLASS+": Jaguar servlet context class is ["+jaguarServletContextClass+"]");
	
		    // Tomcat servlet context class
			tomcatServletContextClass = readParameter(properties,PARM_TOMCAT_SERVLET_CONTEXT_CLASS);
			LogPrinter.println(s+PARM_TOMCAT_SERVLET_CONTEXT_CLASS+": Tomcat servlet context class is ["+tomcatServletContextClass+"]");

			// External system WebApp
			externalSystemWebapp = readParameter(properties,PARAM_EXTERNAL_SYSTEM_WEBAPP);
			if(externalSystemWebapp == null){
				externalSystemWebapp = "wfemExternalSystem";
			}	
		    LogPrinter.println(s+PARAM_EXTERNAL_SYSTEM_WEBAPP+": External system webapp is setted to: ["+externalSystemWebapp+"]");

		    // Copy on input fields enabled
		    allowCopyOnDisabledInputField = false;
			param = readParameter(properties,PARM_ALLOW_COPY_ON_DISABLED_INPUT_FIELDS);
			if(param != null){
				if(param.equalsIgnoreCase("true"))
					allowCopyOnDisabledInputField = true;
			}
		    LogPrinter.println(s+PARM_ALLOW_COPY_ON_DISABLED_INPUT_FIELDS+": Flag to enable copy on disabled input field is setted to: ["+allowCopyOnDisabledInputField+"]");
		    
		    // Business Objects URL
			param = readParameter(properties,PARM_BO_URL);
			if(param != null)
				boUrl = param;
			LogPrinter.println(s+PARM_BO_URL+": Business Object url setted to: ["+boUrl+"]");

		    // QAS URL
			param = readParameter(properties,PARM_QAS_URL);
			if(param != null)
				qasUrl = param;
			LogPrinter.println(s+PARM_QAS_URL+": QAS url setted to: ["+qasUrl+"]");
			
		    // OSB URL
			param = readParameter(properties,PARM_OSB_URL);
			if(param != null)
				osbUrl = param;
			LogPrinter.println(s+PARM_OSB_URL+": OSB url setted to: ["+osbUrl+"]");

		    // FILENET URL
			param = readParameter(properties,PARM_FILENET_URL);
			if(param != null)
				filenetUrl = param;
			LogPrinter.println(s+PARM_FILENET_URL+": Filenet url setted to: ["+filenetUrl+"]");
			
			// Hostname for Application
			param = readParameter(properties,PARM_HOSTNAME_APP);
			if(param != null)
				hostnameApp = param;
			LogPrinter.println(s+PARM_HOSTNAME_APP+": Hostname from application setted to : ["+hostnameApp+"]");
			
			loadRefreshableCachesConfiguration(s,properties);
			
			loadRefreshableConfiguration(properties,true);
			loadWebRedirectionConfiguration();
			loadSharedBeansConfiguration();
			loadCrossSiteRequestForgeryWatchList();
			
			return true;
			
		}catch(Exception e){
			
			String errorMsg = "Wfem Exception in loading configuration file: "+e;
			Exception ne = new Exception(errorMsg);
			ne.printStackTrace();
			return false;
			
		}
		
	}
	
	/********************************************************************/
	/********************************************************************/
	private void loadRefreshableCachesConfiguration(String s,Properties properties) {

		String param = "";
		boolean dirExist = true;
		
	    // DAO objects cache type
		daoObjectsCacheType = readParameter(properties,PARM_DAO_OBJECTS_CACHE_TYPE);
		if(daoObjectsCacheType == null || daoObjectsCacheType.equals("")){
			daoObjectsCacheType = REFRESHABLE_CACHE;
		}
		LogPrinter.println(s+PARM_DAO_OBJECTS_CACHE_TYPE+": DAO cache type is setted to ["+daoObjectsCacheType+"]");

	    // DAO CODDESC cache type
		daoCodDescCacheType = readParameter(properties,PARM_DAO_CODDESC_CACHE_TYPE);
		if(daoCodDescCacheType == null || daoCodDescCacheType.equals("")){
			daoCodDescCacheType = REFRESHABLE_CACHE;
		}
		LogPrinter.println(s+PARM_DAO_CODDESC_CACHE_TYPE+": DAO CODDESC cache type is setted to ["+daoCodDescCacheType+"]");

	    // Serialized Cache dir (only if DAO or CODDESC is serialized
		if(isDaoCodDescCacheSerialized() || isDaoObjectsCacheSerialized()){
			serializedCacheDir = readParameter(properties,PARM_SERIALIZED_CACHE_DIR);
			if(serializedCacheDir == null)
				serializedCacheDir = System.getProperty("user.dir")+"/WfemSerializedCaches";
			File f = new File(serializedCacheDir);
			if(!f.exists()){
				dirExist = false;
				f.mkdir();
			}
			if(!serializedCacheDir.endsWith("/"))
				serializedCacheDir += "/";		    
			if(dirExist){
				File[] sottoDirs = f.listFiles();
				for(int i=0;i<sottoDirs.length;i++){
					if(sottoDirs[i].isDirectory()){
						File[] dirFiles = sottoDirs[i].listFiles();
						for(int j=0;j<dirFiles.length;j++)
							dirFiles[j].delete();
						sottoDirs[i].delete();
					}else
						sottoDirs[i].delete();
				}
			    LogPrinter.println(s+PARM_SERIALIZED_CACHE_DIR+": Serialized caches dir is setted to: ["+serializedCacheDir+"]. Directory was cleared on server");
			}else
			    LogPrinter.println(s+PARM_SERIALIZED_CACHE_DIR+": Serialized caches dir is setted to: ["+serializedCacheDir+"]. Directory was created on server");
		}
		
		// OBJECTS_CACHE_SCAN_TIME
		param = readParameter(properties,PARM_OBJECTS_CACHE_SCAN_TIME);
		if(param != null){
			try{
				objectsCacheScanTime = Integer.parseInt(param);
			}catch(Exception e){}
		}
	    LogPrinter.println(s+PARM_OBJECTS_CACHE_SCAN_TIME+": setted to ["+objectsCacheScanTime+"] seconds");
		
		// OBJECTS_LIFE_TIME
		param = readParameter(properties,PARM_OBJECTS_LIFE_TIME);
		if(param != null){
			try{
				objectsLifeTime = Integer.parseInt(param);
			}catch(Exception e){}
		}
	    LogPrinter.println(s+PARM_OBJECTS_LIFE_TIME+": setted to ["+objectsLifeTime+"] seconds");

		// DAO_OBJECTS_CACHE_SCAN_TIME
		param = readParameter(properties,PARM_DAO_OBJECTS_CACHE_SCAN_TIME);
		if(param != null){
			try{
				daoObjectsCacheScanTime = Integer.parseInt(param);
			}catch(Exception e){}
		}
	    LogPrinter.println(s+PARM_DAO_OBJECTS_CACHE_SCAN_TIME+": setted to ["+daoObjectsCacheScanTime+"] seconds");

		// DAO_OBJECTS_LIFE_TIME
		param = readParameter(properties,PARM_DAO_OBJECTS_LIFE_TIME);
		if(param != null){
			try{
				daoObjectsLifeTime = Integer.parseInt(param);
			}catch(Exception e){}
		}
	    LogPrinter.println(s+PARM_DAO_OBJECTS_LIFE_TIME+": setted to ["+daoObjectsLifeTime+"] seconds");

		// DAO_CODDESC_CACHE_SCAN_TIME
		param = readParameter(properties,PARM_DAO_CODDESC_CACHE_SCAN_TIME);
		if(param != null){
			try{
				daoCodDescCacheScanTime = Integer.parseInt(param);
			}catch(Exception e){}
		}
	    LogPrinter.println(s+PARM_DAO_CODDESC_CACHE_SCAN_TIME+": setted to ["+daoCodDescCacheScanTime+"] seconds");

		// DAO_CODDESC_CACHE_SCAN_TIME
		param = readParameter(properties,PARM_DAO_CODDESC_DEFAULT_LIFE_TIME);
		if(param != null){
			try{
				daoCodDescDefaultLifeTime = Integer.parseInt(param);
			}catch(Exception e){}
		}
	    LogPrinter.println(s+PARM_DAO_CODDESC_DEFAULT_LIFE_TIME+": setted to ["+daoCodDescDefaultLifeTime+"] seconds");
	}
	
	/********************************************************************/
	/********************************************************************/
	private void loadCrossSiteRequestForgeryWatchList() {
		
		try{
	
			LogPrinter.println("Wfem Cross Site Request Forgery watch list configuration: loading configuration file ["+CSRF_CONFIGURATION_FILE+"]");
			InputStream inputStream = this.getClass().getResourceAsStream(CSRF_CONFIGURATION_FILE);
			if(inputStream == null){
				LogPrinter.println("Wfem Cross Site Request Forgery watch list configuration file ["+CSRF_CONFIGURATION_FILE+"] not found");
			}else{
				crossSiteRequestForgeryWatchList.load(inputStream);
				inputStream.close();
				LogPrinter.println("Wfem Cross Site Request Forgery watch list loaded");
			}
			
		}catch(Exception e){
			// do nothing
		}
		
	}
	
	/********************************************************************/
	/********************************************************************/
	private void loadSharedBeansConfiguration() {
		
		try{
	
			LogPrinter.println("Wfem local shared beans configuration: loading configuration file ["+SHARED_BEANS_CONFIGURATION_FILE+"]");
			InputStream inputStream = this.getClass().getResourceAsStream(SHARED_BEANS_CONFIGURATION_FILE);
			if(inputStream == null){
				LogPrinter.println("Wfem local shared beans configuration file ["+SHARED_BEANS_CONFIGURATION_FILE+"] not found");
			}else{
				sharedBeansProps.load(inputStream);
				inputStream.close();
				LogPrinter.println("Wfem local shared beans configuration: shared beans: ["+sharedBeansProps+"]");
			}
			
		}catch(Exception e){			
		}
		
	}
	
	/********************************************************************/
	/********************************************************************/
	private void loadWebRedirectionConfiguration() {
		
		try{
	
			LogPrinter.println("Wfem Package web redirection configuration: loading configuration file ["+PACKAGE_WEBREDIR_CONFIGURATION_FILE+"]");
			InputStream inputStream = this.getClass().getResourceAsStream(PACKAGE_WEBREDIR_CONFIGURATION_FILE);
			if(inputStream == null){
				LogPrinter.println("Wfem Package web redirection configuration file ["+PACKAGE_WEBREDIR_CONFIGURATION_FILE+"] not found");
			}else{
				urlWebRedirection.load(inputStream);
				inputStream.close();
				this.sortedUrlWebRedirectionPropertyNames = Tools.sortPropertiesKeys(urlWebRedirection);
				LogPrinter.println("Wfem Package web redirection configuration: package web redirection loaded: ["+urlWebRedirection+"]");
			}
			
		}catch(Exception e){
			
			String errorMsg = "Wfem Exception in loading package web redirection configuration file: ["+e+"]";
			Exception ne = new Exception(errorMsg);
			ne.printStackTrace();
			
		}
		
		try{
	
			LogPrinter.println("Wfem WebApp redirection configuration: loading configuration file ["+WEBREDIR_CONFIGURATION_FILE+"]");
			InputStream inputStream = this.getClass().getResourceAsStream(WEBREDIR_CONFIGURATION_FILE);
			if(inputStream == null){
				LogPrinter.println("Wfem WebApp redirection configuration file ["+WEBREDIR_CONFIGURATION_FILE+"] not found");
			}else{
				webRedirection.load(inputStream);
				inputStream.close();
				LogPrinter.println("Wfem WebApp redirection configuration: redirection loaded: ["+webRedirection+"]");
			}
			
		}catch(Exception e){
			
			String errorMsg = "Wfem Exception in loading WebApp redirection configuration file: ["+e+"]";
			Exception ne = new Exception(errorMsg);
			ne.printStackTrace();
			
		}
	}
	
	/********************************************************************/
	/********************************************************************/
	private String readParameter(Properties p, String parName) {
	
		String par = p.getProperty(parName);
		if(par != null){
			par = par.trim();
			if(par.equals(""))
				par = null;
		}
		return par;
	}

	/********************************************************************/
	/********************************************************************/
	public boolean isDaoObjectsCacheSerialized(){
		return daoObjectsCacheType.equalsIgnoreCase(SERIALIZED_CACHE);
	}

	/********************************************************************/
	/********************************************************************/
	public boolean isDaoCodDescCacheSerialized(){
		return daoCodDescCacheType.equalsIgnoreCase(SERIALIZED_CACHE);
	}
	
	/********************************************************************/
	/********************************************************************/
	public int getCommandStackSize() {
		return commandStackSize;
	}

	/********************************************************************/
	/********************************************************************/
	public int getRofTraceLevel() {
		return rofTraceLevel;
	}

	/********************************************************************/
	/********************************************************************/
	public int getDaoTraceLevel() {
		return daoTraceLevel;
	}

	/********************************************************************/
	/********************************************************************/
	public int getMailTraceLevel() {
		return mailTraceLevel;
	}

	/********************************************************************/
	/********************************************************************/
	public int getLayoutTraceLevel() {
		return layoutTraceLevel;
	}

	/********************************************************************/
	/********************************************************************/
	public int getHtmlToPdfTraceLevel() {
		return htmlToPdfTraceLevel;
	}

	/********************************************************************/
	/********************************************************************/
	public int getPdfTraceLevel() {
		return pdfTraceLevel;
	}

	/********************************************************************/
	/********************************************************************/
	public int getRefresherTraceLevel() {
		return refresherTraceLevel;
	}

	/********************************************************************/
	/********************************************************************/
	public Properties getWebRedirection() {
		return webRedirection;
	}

	/********************************************************************/
	/********************************************************************/
	public Properties getUrlWebRedirection() {
		return urlWebRedirection;
	}
	
	/********************************************************************/
	/********************************************************************/
	public Properties getCrossSiteRequestForgeryWatchList() {
		return crossSiteRequestForgeryWatchList;
	}
	
	/********************************************************************/
	/********************************************************************/
	public String[] getSortedUrlWebRedirectionPropertyNames(){
		return this.sortedUrlWebRedirectionPropertyNames;
	}
	
	/********************************************************************/
	/********************************************************************/
	public boolean isRemoteObjectsCacheEnabled() {
		return remoteObjectsCacheEnabled;
	}

	/********************************************************************/
	/********************************************************************/
	public boolean isObjectsCacheEnabled() {
		return objectsCacheEnabled;
	}

	/********************************************************************/
	/********************************************************************/
	public boolean isDaoCacheEnabled() {
		return daoCacheEnabled;
	}

	/********************************************************************/
	/********************************************************************/
	public boolean isRemoteObjectLocal(String remoteObjectClassName) {
		String res = configurationProps.getProperty(remoteObjectClassName);
		if(res == null)
			return false;
		return true;
	}
	
	/********************************************************************/
	/********************************************************************/
	public boolean isSharedBeanLocalObject(String remoteObjectClassName) {
		String res = sharedBeansProps.getProperty(remoteObjectClassName);
		if(res == null)
			return false;
		return true;
	}

	/********************************************************************/
	/********************************************************************/
	public String getJdbcDriver() {
		return jdbcDriver;
	}

	/********************************************************************/
	/********************************************************************/
	public String getDbConnectionTraceDir() {
		return dbConnectionTraceDir;
	}

	/********************************************************************/
	/********************************************************************/
	public String getDbJavaClassesDir() {
		return dbJavaClassesDir;
	}

	/********************************************************************/
	/********************************************************************/
	public String getSerializedCacheDir() {
		return serializedCacheDir;
	}

	/********************************************************************/
	/********************************************************************/
	public boolean isDbConnectionTraceEnabled() {
		return dbConnectionTraceEnabled;
	}

	/********************************************************************/
	/********************************************************************/
	public boolean isOnlineEnvironment() {
		return environmentType.equalsIgnoreCase(ONLINE_ENVIRONMENT);
	}

	/********************************************************************/
	/********************************************************************/
	public boolean isOfflineEnvironment() {
		return environmentType.equalsIgnoreCase(OFFLINE_ENVIRONMENT);
	}

	/********************************************************************/
	/********************************************************************/
	public String getJaguarServletContextClass() {
		return jaguarServletContextClass;
	}

	/********************************************************************/
	/********************************************************************/
	public String getTomcatServletContextClass() {
		return tomcatServletContextClass;
	}

	/********************************************************************/
	/********************************************************************/
	public int getDaoCodDescCacheScanTime() {
		return daoCodDescCacheScanTime;
	}

	/********************************************************************/
	/********************************************************************/
	public int getDaoObjectsCacheScanTime() {
		return daoObjectsCacheScanTime;
	}

	/********************************************************************/
	/********************************************************************/
	public int getDaoObjectsLifeTime() {
		return daoObjectsLifeTime;
	}

	/********************************************************************/
	/********************************************************************/
	public int getObjectsCacheScanTime() {
		return objectsCacheScanTime;
	}

	/********************************************************************/
	/********************************************************************/
	public int getObjectsLifeTime() {
		return objectsLifeTime;
	}

	/********************************************************************/
	/********************************************************************/
	public int getDaoCodDescDefaultLifeTime() {
		return daoCodDescDefaultLifeTime;
	}

	/********************************************************************/
	/********************************************************************/
	public boolean isQasSimulationEnabled() {
		return qasSimulationEnabled;
	}

	/********************************************************************/
	/********************************************************************/
	public boolean isOSBSimulationEnabled() {
		return osbSimulationEnabled;
	}
	
	/********************************************************************/
	/********************************************************************/
	public String getTracedUserCode() {
		return tracedUserCode;
	}

	/********************************************************************/
	/********************************************************************/
	public String getTestJavaClassesDir() {
		return testJavaClassesDir;
	}

	/********************************************************************/
	/********************************************************************/
	public boolean isModelTraceEnabled() {
		return modelTraceEnabled;
	}

	/********************************************************************/
	/********************************************************************/
	public String getModelTraceDir() {
		return modelTraceDir;
	}
	
	/********************************************************************/
	/********************************************************************/
	public String getTierTraceDir() {
		return tierTraceDir;
	}
	
	/********************************************************************/
	/********************************************************************/
	public String getBoUrl() {
		return boUrl;
	}
	
	/********************************************************************/
	/********************************************************************/
	public String getQasUrl() {
		return qasUrl;
	}
	
	/********************************************************************/
	/********************************************************************/
	public String getOsbUrl() {
		return osbUrl;
	}
	
	/********************************************************************/
	/********************************************************************/
	public String getFilenetUrl() {
		return filenetUrl;
	}
	
	/********************************************************************/
	/********************************************************************/
	public String getImageServerUrl() {
		return imageServerUrl;
	}

	/********************************************************************/
	/********************************************************************/
	public boolean isImageServerForWfemlayoutEnabled() {
		return imageServerForWfemlayoutEnabled;
	}

	/********************************************************************/
	/********************************************************************/
	public boolean isTrustedSite() {
		return trustedSite;
	}

	/********************************************************************/
	/********************************************************************/
	public boolean isTraceBrowserInstancesNotFoundActive() {
		return traceBrowserInstancesNotFoundActive;
	}

	/********************************************************************/
	/********************************************************************/
	public boolean isAllowCopyOnDisabledInputField() {
		return allowCopyOnDisabledInputField;
	}
	
	/********************************************************************/
	/********************************************************************/
	public String getHostnameApp() {
		return hostnameApp;
	}

	/********************************************************************/
	/********************************************************************/
	public String getLogFilename() {
		return logFilename;
	}

	/********************************************************************/
	/********************************************************************/
	public int getKeepaliveTimerMinutes() {
		return keepaliveTimerMinutes;
	}

	/********************************************************************/
	/********************************************************************/
	public int getKeepAliveTimerInMilliseconds(){
		return 1000 * 60 * getKeepaliveTimerMinutes();
	}
	
	/********************************************************************/
	/********************************************************************/
	public boolean isAcceptCtxOnlyOnHeader() {
		return acceptCtxOnlyOnHeader;
	}

	/********************************************************************/
	/********************************************************************/
	public String getDynatraceSrc() {
		return dynatraceSrc;
	}

	/********************************************************************/
	/********************************************************************/
	public String getNasSource() {
		return nasSource;
	}

	/********************************************************************/
	/********************************************************************/
	public String getNasShareRoot(String shareName) {
		return configurationProps.getProperty("NAS_"+shareName+"_ROOT");
	}

	/********************************************************************/
	/********************************************************************/
	public String getCrossSiteScriptingRegex() {
		return crossSiteScriptingRegex;
	}

	/********************************************************************/
	/********************************************************************/
	public String getEnvironment() {
		return environment;
	}
	
	/********************************************************************/
	/********************************************************************/
	public String getOsbNameUrl(String osbName) {
		return configurationProps.getProperty(osbName+"_URL");
	}
	
}