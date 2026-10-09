package com.atosorigin.wfem.filestreamer;

import java.io.InputStream;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class FileStreamerInfo {
	private boolean inMemory;
	private int 	httpResult;
	private String 	errorMessage;
	
	private boolean directory = false;
	private boolean fileExist = true;
	private InputStream inputStream = null;
	private byte[] content = null;
	private long fileLength;
	
	public boolean isFileExist() {
		return fileExist;
	}
	public void setFileExist(boolean fileExist) {
		this.fileExist = fileExist;
	}
	public long getFileLength() {
		return fileLength;
	}
	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}
	public InputStream getInputStream() {
		return inputStream;
	}
	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	public int getHttpResult() {
		return httpResult;
	}
	public void setHttpResult(int httpResult) {
		this.httpResult = httpResult;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public boolean isInMemory() {
		return inMemory;
	}
	public void setInMemory(boolean inMemory) {
		this.inMemory = inMemory;
	}
	public byte[] getContent() {
		return content;
	}
	public void setContent(byte[] content) {
		this.content = content;
	}
	public boolean isDirectory() {
		return directory;
	}
	public void setDirectory(boolean directory) {
		this.directory = directory;
	}
	
}
