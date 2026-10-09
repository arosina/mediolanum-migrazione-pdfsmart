package com.atosorigin.wfem.webservices;

import java.io.Serializable;

import com.atosorigin.wfem.command.ClientSessionContext;

/***********************************************************************************************/
/***********************************************************************************************/
public class ClientSessionContextBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String caller = "";		
	private String callid = "";		
	private String country = "";	// countryCode
	private String channel = "";	// channelCode
	private String language = "";	// langCode
	private String user = "";		// userCode
	private String clientip = ""; 	// clientIp	
    private String cluser = ""; 	// currentLinkedUserCode
    private String userType = ClientSessionContext.USER_TYPE_RETE;	// User type (RETE, SEDE, ASSISTENTEFB)
    
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public String getCaller() {
		return caller;
	}
	public void setCaller(String caller) {
		this.caller = caller;
	}
	public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getClientip() {
		return clientip;
	}
	public void setClientip(String clientip) {
		this.clientip = clientip;
	}
	public String getCluser() {
		return cluser;
	}
	public void setCluser(String cluser) {
		this.cluser = cluser;
	}
	public String getCallid() {
		return callid;
	}
	public void setCallid(String callid) {
		this.callid = callid;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}

}
