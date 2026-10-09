package com.atosorigin.wfem.dao;

import org.dom4j.Element;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class DAOOSBAccessInfo extends DAOAccessInfo{

	private String url = "";
	private String serviceName=null;
	private String operationName="";
	private String xmlInputTemplate;
	private String xmlOutputTemplate;
	private boolean compactEmpty = false;
	private String dataFormatter = null;
	private String resultCharsetName = null;
	private boolean markNillable = false;
	private boolean keepAttrs = false;
	private String contentType = null;
	
	/********************************************************************************
	/********************************************************************************/
	public DAOOSBAccessInfo() {
		super();
	}

	public String getXmlInputTemplate() {
		return xmlInputTemplate;
	}

	public void setXmlInputTemplate(Element xmlInputTemplate) {
		this.xmlInputTemplate = xmlInputTemplate.getText();
	}

	public String getXmlOutputTemplate() {
		return xmlOutputTemplate;
	}

	public void setXmlOutputTemplate(Element xmlOutputTemplate) {
		this.xmlOutputTemplate = xmlOutputTemplate.getText();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public boolean isCompactEmpty() {
		return compactEmpty;
	}

	public void setCompactEmpty(boolean compactEmpty) {
		this.compactEmpty = compactEmpty;
	}

	public String getDataFormatter() {
		return dataFormatter;
	}

	public void setDataFormatter(String dataFormatter) {
		this.dataFormatter = dataFormatter;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getResultCharsetName() {
		return resultCharsetName;
	}

	public void setResultCharsetName(String resultCharsetName) {
		this.resultCharsetName = resultCharsetName;
	}

	public boolean isMarkNillable() {
		return markNillable;
	}

	public void setMarkNillable(boolean markNillable) {
		this.markNillable = markNillable;
	}

	public boolean isKeepAttrs() {
		return keepAttrs;
	}

	public void setKeepAttrs(boolean keepAttrs) {
		this.keepAttrs = keepAttrs;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
