package com.atosorigin.wfem.webservices;

import java.io.Serializable;

/***********************************************************************************************/
/***********************************************************************************************/
public class PropertyError implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String code;
	private String descr;
	
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getDescr() {
		return descr;
	}
	public void setDescr(String descr) {
		this.descr = descr;
	}
	
}
