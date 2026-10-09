package com.atosorigin.wfem.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Namespace;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class XMLServiceInnerCallData implements Serializable{

	private String 	url="";
	private String 	serviceName=null;
	private String 	operationName="";
	private String  daoAccessName=null;
	private boolean	compactEmpty = false;
	private String  dataFormatterClassName=null;
	protected transient Class dataFormatterClass=null;
	private String resultCharsetName = null;	
	private transient List nameSpaces = new ArrayList();
	private transient StringBuffer middleTierInputParameters = new StringBuffer();
	private boolean	markNillable = false;
	private boolean keepAttrs = false;
	private String contentType = null;	
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	protected void addNameSpace(Namespace namespace){
		if(!this.nameSpaces.contains(namespace))
			this.nameSpaces.add(namespace);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	protected String nameSpaces(){
		StringBuffer res = new StringBuffer();
		for(int i=0;i<this.nameSpaces.size();i++){
			Namespace ns = (Namespace)nameSpaces.get(i);
			String uri = ns.getURI();
			if(uri != null && uri.length() > 0){
				String prefix = ns.getPrefix();
				if(prefix != null && prefix.length() > 0)
					res.append(" xmlns:"+ns.getPrefix()+"=\""+uri+"\" ");
				else
					res.append(" xmlns=\""+uri+"\" ");
			}
		}
		return res.toString();
	}
	
	public boolean isCompactEmpty() {
		return compactEmpty;
	}
	public void setCompactEmpty(boolean compactEmpty) {
		this.compactEmpty = compactEmpty;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getDataFormatterClassName() {
		return dataFormatterClassName;
	}
	public void setDataFormatterClassName(String dataFormatterClassName) {
		this.dataFormatterClassName = dataFormatterClassName;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public List getNameSpaces() {
		return nameSpaces;
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

	public String getResultCharsetName() {
		return resultCharsetName;
	}

	public void setResultCharsetName(String resultCharsetName) {
		this.resultCharsetName = resultCharsetName;
	}	

	public StringBuffer getMiddleTierInputParameters() {
		return middleTierInputParameters;
	}

	public void setMiddleTierInputParameters(StringBuffer middleTierInputParameters) {
		this.middleTierInputParameters = middleTierInputParameters;
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
