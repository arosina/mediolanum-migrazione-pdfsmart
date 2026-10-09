package com.atosorigin.wfem.webservices;

import java.io.Serializable;

/***********************************************************************************************/
/***********************************************************************************************/
public class MethodHeaderBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private ClientSessionContextBean clientSessionContext = new ClientSessionContextBean();
	private String skippableFields = "";

	public ClientSessionContextBean getClientSessionContext() {
		return clientSessionContext;
	}

	public void setClientSessionContext(
			ClientSessionContextBean clientSessionContext) {
		this.clientSessionContext = clientSessionContext;
	}

	public String getSkippableFields() {
		return skippableFields;
	}

	public void setSkippableFields(String skippableFields) {
		this.skippableFields = skippableFields;
	}	
}
