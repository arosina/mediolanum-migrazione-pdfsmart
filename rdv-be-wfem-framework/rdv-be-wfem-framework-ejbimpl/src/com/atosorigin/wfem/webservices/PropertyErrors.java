package com.atosorigin.wfem.webservices;

import java.io.Serializable;

/***********************************************************************************************/
/***********************************************************************************************/
public class PropertyErrors implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String				propName = "";
	private PropertyError[] 	propWarnings = new PropertyError[0];
	private PropertyError[] 	propErrors = new PropertyError[0];
	
	public String getPropName() {
		return propName;
	}
	public void setPropName(String propName) {
		this.propName = propName;
	}
	public PropertyError[] getPropWarnings() {
		return propWarnings;
	}
	public void setPropWarnings(PropertyError[] propWarnings) {
		this.propWarnings = propWarnings;
	}
	public PropertyError[] getPropErrors() {
		return propErrors;
	}
	public void setPropErrors(PropertyError[] propErrors) {
		this.propErrors = propErrors;
	}
	
}
