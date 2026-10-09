package com.atosorigin.wfem.command;

import java.util.ArrayList;

import com.atosorigin.wfem.controller.Configuration;

/***********************************************************************************************
 * @author Ricotti Corrado
 ***********************************************************************************************/
public class ClientSessionContext implements java.io.Serializable{

	public static int RUOLO_ASSISTENTEFB = 2206;
	
	public static String USER_TYPE_RETE 		= "RETE";
	public static String USER_TYPE_SEDE 		= "SEDE";
	public static String USER_TYPE_ASSISTENTEFB	= "ASSISTENTEFB";
	public static String USER_TYPE_CLIENTE 		= "CLIENTE";
	
	private String sessionId;
	private boolean traceEnabled = false;
	private boolean testEnabled = false;
	private String crossTierTraceId;
	
	private String countryCode;
	private String channelCode;
	private String langCode;
	private String applCode;
	private String userCode;
	private String loginName;
	private String delegatedUserCode;
	private String startUrl;
	private String clientIp;	
    private String currentLinkedUserCode = "";
	private String replicationServer;	
	private String userType = USER_TYPE_RETE;

	public static final String TOMCAT_AS = "tomcat";
	public static final String JAGUAR_AS = "jaguar";
	private String	applicationServerType = JAGUAR_AS;

	/********************************************************************************
	/********************************************************************************/
	public ClientSessionContext() {
		super();
	}

	/********************************************************************************
	/********************************************************************************/
	public boolean isRete(){
		return getUserType().equals(USER_TYPE_RETE);
	}
	
	/********************************************************************************
	/********************************************************************************/
	public boolean isSede(){
		return getUserType().equals(USER_TYPE_SEDE);
	}
	
	/********************************************************************************
	/********************************************************************************/
	public boolean isAssistenteFB(){
		return getUserType().equals(USER_TYPE_ASSISTENTEFB);
	}
	
	/********************************************************************************
	/********************************************************************************/
	public boolean isCliente(){
		return getUserType().equals(USER_TYPE_CLIENTE);
	}

	/********************************************************************************
	/********************************************************************************/
	public java.lang.String getApplCode() {
		return applCode;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public java.lang.String getClientIp() {
		return clientIp;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public String getDataSourceName(String dataSourceReference) {
		return getDataSourceName(dataSourceReference,this.countryCode,this.channelCode);
	}
	
	/********************************************************************************
	/********************************************************************************/
	public String getDataSourceName(String dataSourceReference, 
									 String country, String channel) {
		
		ArrayList<String> configDatasourcesAsArray = Configuration.getInstance().getDataSourcesNamesAsArray();
		if(configDatasourcesAsArray != null && configDatasourcesAsArray.contains(dataSourceReference.toUpperCase()))
			return dataSourceReference.toUpperCase();
		
		if(country == null) 
			country = "";
		if(channel == null) 
			channel = "";
		return country.toUpperCase()+channel.toUpperCase()+dataSourceReference.toUpperCase();
	}
	
	/********************************************************************************
	/********************************************************************************/
	public java.lang.String getSessionId() {
		return sessionId;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public java.lang.String getStartUrl() {
		return startUrl;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public java.lang.String getUserCode() {
		return userCode;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public boolean isTraceEnabled() {
		return traceEnabled;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public void setApplCode(java.lang.String newApplCode) {
		applCode = newApplCode;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public void setClientIp(java.lang.String newClientIp) {
		clientIp = newClientIp;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public void setSessionId(java.lang.String newSessionId) {
		sessionId = newSessionId;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public void setStartUrl(java.lang.String newStartUrl) {
		startUrl = newStartUrl;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public void setTraceEnabled(boolean newTraceEnabled) {
		traceEnabled = newTraceEnabled;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public void setUserCode(java.lang.String newUserCode) {
		userCode = newUserCode;
	}

	/********************************************************************************
	/********************************************************************************/
	public String getChannelCode() {
		return channelCode;
	}

	/********************************************************************************
	/********************************************************************************/
	public String getCountryCode() {
		return countryCode;
	}

	/********************************************************************************
	/********************************************************************************/
	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	/********************************************************************************
	/********************************************************************************/
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	/********************************************************************************
	/********************************************************************************/
	public String getLangCode() {
		return langCode;
	}

	/********************************************************************************
	/********************************************************************************/
	public void setLangCode(String langCode) {
		this.langCode = langCode;
	}

	/********************************************************************************
	/********************************************************************************/
	public String getDelegatedUserCode() {
		return delegatedUserCode;
	}

	/********************************************************************************
	/********************************************************************************/
	public void setDelegatedUserCode(String delegatedUserCode) {
		this.delegatedUserCode = delegatedUserCode;
	}

	/********************************************************************************
	/********************************************************************************/
	public String getCurrentLinkedUserCode() {
		return currentLinkedUserCode;
	}

	/********************************************************************************
	/********************************************************************************/
	public void setCurrentLinkedUserCode(String currentLinkedUserCode) {
		this.currentLinkedUserCode = currentLinkedUserCode;
	}

	/********************************************************************/
	/********************************************************************/
	public boolean isOnlineEnvironment() {
		return Configuration.getInstance().isOnlineEnvironment();
	}

	/********************************************************************/
	/********************************************************************/
	public boolean isOfflineEnvironment() {
		return Configuration.getInstance().isOfflineEnvironment();
	}

	/********************************************************************/
	/********************************************************************/
	public String getApplicationServerType() {
		return applicationServerType;
	}

	/********************************************************************/
	/********************************************************************/
	public void setApplicationServerType(String applicationServerType) {
		this.applicationServerType = applicationServerType;
	}

	/********************************************************************/
	/********************************************************************/
	public String getReplicationServer() {
		return replicationServer;
	}

	/********************************************************************/
	/********************************************************************/
	public void setReplicationServer(String replicationServer) {
		this.replicationServer = replicationServer;
	}

	/********************************************************************/
	/********************************************************************/
	public boolean isTestEnabled() {
		return testEnabled;
	}

	/********************************************************************/
	/********************************************************************/
	public void setTestEnabled(boolean testEnabled) {
		this.testEnabled = testEnabled;
	}

	/********************************************************************/
	/********************************************************************/
	public String getCrossTierTraceId() {
		return crossTierTraceId;
	}

	/********************************************************************/
	/********************************************************************/
	public void setCrossTierTraceId(String crossTierTraceId) {
		this.crossTierTraceId = crossTierTraceId;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

}
