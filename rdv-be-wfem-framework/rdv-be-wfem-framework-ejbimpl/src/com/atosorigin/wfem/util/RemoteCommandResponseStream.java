package com.atosorigin.wfem.util;

import java.io.InputStream;

import org.apache.commons.httpclient.HttpMethod;

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public class RemoteCommandResponseStream {
	
	private int contentLength;
	private String fileName;
	private InputStream inputStream;
	private HttpMethod httpMethod;
	
	public void closeResponse(){
		httpMethod.releaseConnection();
	}
	public void setHttpMethod(HttpMethod httpMethod) {
		this.httpMethod = httpMethod;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	public int getContentLength() {
		return contentLength;
	}
	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
}
