package com.atosorigin.wfem.webservices;

import java.io.Serializable;

/***********************************************************************************************/
/***********************************************************************************************/
public abstract class MethodInputBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private MethodHeaderBean methodHeader = new MethodHeaderBean();

	public MethodHeaderBean getMethodHeader() {
		return methodHeader;
	}

	public void setMethodHeader(MethodHeaderBean methodHeader) {
		this.methodHeader = methodHeader;
	}

}
