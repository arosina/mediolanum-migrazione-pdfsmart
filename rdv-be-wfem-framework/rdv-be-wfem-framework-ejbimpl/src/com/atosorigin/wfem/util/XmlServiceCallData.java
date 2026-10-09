package com.atosorigin.wfem.util;

import java.io.Serializable;
import java.sql.Timestamp;


/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public abstract class XmlServiceCallData implements Serializable{
	
	public static final int QAS_SERVICE_TYPE = 0;
	public static final int OSB_SERVICE_TYPE = 1;
	
	public static final int STATUS_OK = 0;
	public static final int STATUS_SERVICE_DISABLED = -2;
	public static final int STATUS_EXCEPTION_ON_CALL = -3;
	public static final int STATUS_SERVICE_TIMEOUT = -2147012894;
	
	public static final int STATUS_HTTP_POST_KO = -1;
	
	private String 			 url="";
	private String 			 serviceName="";
	private String 			 operationName="";
	private String 			 xmlSend="";
	private String 			 xmlReceive="";
	private int	   			 httpStatus;
	private int	   			 status=STATUS_HTTP_POST_KO;
	private Timestamp		 startTime;
	private Timestamp		 endTime;
	private String 			 inAppinfo="";
	private String 			 outAppinfo="";
	
	private String 			message="";
	private String 			severity="";
	private String 			source="";
	
	protected String  dataFormatterClassName=null;
	protected transient Class dataFormatterClass=null;
	protected String resultCharsetName = null;
	private boolean forcedTrace = false;
	private int	valuesFound = 0;
	private String daoAccessName=null;
	private String contentType = null;
	
	/********************************************************************************/
	/********************************************************************************/
	public abstract int getServiceType();
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getXmlReceive() {
		return xmlReceive;
	}
	public void setXmlReceive(String xmlReceive) {
		this.xmlReceive = xmlReceive;
	}
	public String getXmlSend() {
		return xmlSend;
	}
	public void setXmlSend(String xmlSend) {
		this.xmlSend = xmlSend;
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	public Timestamp getStartTime() {
		return startTime;
	}
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	public String getInAppinfo() {
		return inAppinfo;
	}
	public void setInAppinfo(String inAppinfo) {
		this.inAppinfo = inAppinfo;
	}
	public String getOutAppinfo() {
		return outAppinfo;
	}
	public void setOutAppinfo(String outAppinfo) {
		this.outAppinfo = outAppinfo;
	}
	public int getHttpStatus() {
		return httpStatus;
	}
	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}
	public boolean isForcedTrace() {
		return forcedTrace;
	}
	public void setForcedTrace(boolean forcedTrace) {
		this.forcedTrace = forcedTrace;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getSeverity() {
		return severity;
	}
	public void setSeverity(String severity) {
		this.severity = severity;
	}
	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}

	public int getValuesFound() {
		return valuesFound;
	}

	public void setValuesFound(int valuesFound) {
		this.valuesFound = valuesFound;
	}

	public String getDaoAccessName() {
		return daoAccessName;
	}

	public void setDaoAccessName(String daoAccessName) {
		this.daoAccessName = daoAccessName;
	}

	public String getOperationName() {
		return operationName;
	}

	public void setOperationName(String operationName) {
		this.operationName = operationName;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
}
