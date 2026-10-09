package com.atosorigin.wfem.command;

import java.util.HashMap;
import java.util.Map;

/********************************************************************************/
/********************************************************************************/
public class UserSessionContext implements java.io.Serializable {

	private String countryCode;
	private String channelCode;
	private String userCode;
	private String loginName;
	private String delegatedUserCode;
	private String langCode;
	private String startUrl;
	private String clientIp;
	private String currentLinkedUserCode;
	private String loginUserId;
	private String organizationCode;
	private String customHomepageDir;
	private String userType = ClientSessionContext.USER_TYPE_RETE;
	private String userRoles = "";
	
	private boolean changed = false;
	private boolean createdByFactory = true;

	private ClientSessionContext clientSessionContext = new ClientSessionContext();
	private Map extendedInfo = new ExtendedInfo();

	/********************************************************************************/
	/********************************************************************************/
	protected class ExtendedInfo extends HashMap {
		private static final long serialVersionUID = 9147203349269339403L;
		
		public ExtendedInfo() {
		}

		public ExtendedInfo(Map wrapped) {
			super(wrapped);
		}
		
		public Object put(Object key, Object value) {
			Object returnValue = super.put(key, value);
			UserSessionContext.this.setChanged(true);
			return returnValue;
		}

		public void putAll(Map arg0) {
			super.putAll(arg0);
			UserSessionContext.this.setChanged(true);
		}

		public Object remove(Object key) {
			Object returnValue = super.remove(key);
			UserSessionContext.this.setChanged(true);
			return returnValue;
		}
		
		public void clear() {
			super.clear();
			UserSessionContext.this.setChanged(true);
		}
	}
	
	/********************************************************************************/
	/* Getter methods                                 */
	/********************************************************************************/
	public java.lang.String getChannelCode() {
		return channelCode;
	}

	public java.lang.String getClientIp() {
		return clientIp;
	}

	public ClientSessionContext getClientSessionContext() {
		return clientSessionContext;
	}

	public java.lang.String getCountryCode() {
		return countryCode;
	}

	public java.lang.String getDelegatedUserCode() {
		return delegatedUserCode;
	}

	public java.lang.String getLangCode() {
		return langCode;
	}

	public java.lang.String getStartUrl() {
		return startUrl;
	}

	public java.lang.String getUserCode() {
		return userCode;
	}

	public String getCurrentLinkedUserCode() {
		return currentLinkedUserCode;
	}

	public String getOrganizationCode() {
		return organizationCode;
	}

	public Map getExtendedInfo() {
		return extendedInfo;
	}

	public String getLoginUserId() {
		return loginUserId;
	}

	public String getCustomHomepageDir() {
		return customHomepageDir;
	}

	/********************************************************************************/
	/* Setter methods - setting changed flag to true                                */
	/********************************************************************************/
	public void setExtendedInfo(Map extendedInfo) {
		this.extendedInfo = new ExtendedInfo(extendedInfo);
		this.setChanged(true);
	}

	public void setChannelCode(java.lang.String newChannelCode) {
		this.channelCode = newChannelCode;
		this.setChanged(true);
	}

	public void setClientIp(java.lang.String newClientIp) {
		this.clientIp = newClientIp;
		this.setChanged(true);
	}

	public void setClientSessionContext(ClientSessionContext newClientSessionContext) {
		this.clientSessionContext = newClientSessionContext;
		this.setChanged(true);
	}

	public void setCountryCode(java.lang.String newCountryCode) {
		this.countryCode = newCountryCode;
		this.setChanged(true);
	}

	public void setDelegatedUserCode(java.lang.String newDelegatedUserCode) {
		this.delegatedUserCode = newDelegatedUserCode;
		this.setChanged(true);
	}

	public void setLangCode(java.lang.String newLangCode) {
		this.langCode = newLangCode;
		this.setChanged(true);
	}

	public void setStartUrl(java.lang.String newStartUrl) {
		this.startUrl = newStartUrl;
		this.setChanged(true);
	}

	public void setUserCode(java.lang.String newUserCode) {
		this.userCode = newUserCode;
		this.setChanged(true);
	}

	public void setCurrentLinkedUserCode(String currentLinkedUserCode) {
		this.currentLinkedUserCode = currentLinkedUserCode;
		this.setChanged(true);
	}
	
	public void setLoginUserId(String loginUserId) {
		this.loginUserId = loginUserId;
		this.setChanged(true);
	}
	
	public void setOrganizationCode(String organizationCode) {
		this.organizationCode = organizationCode;
		this.setChanged(true);
	}

	public void setCustomHomepageDir(String customHomepageDir) {
		this.customHomepageDir = customHomepageDir;
		this.setChanged(true);
	}

	public boolean isChanged() {
		return changed;
	}

	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	public boolean isCreatedByFactory() {
		return createdByFactory;
	}

	public void setCreatedByFactory(boolean createdByFactory) {
		this.createdByFactory = createdByFactory;
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

	public String getUserRoles() {
		return userRoles;
	}

	public void setUserRoles(String userRoles) {
		this.userRoles = userRoles;
	}

}