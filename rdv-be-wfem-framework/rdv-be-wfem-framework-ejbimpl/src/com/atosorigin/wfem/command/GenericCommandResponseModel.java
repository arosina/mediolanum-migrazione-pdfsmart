package com.atosorigin.wfem.command;

import java.io.Serializable;
import java.util.Vector;

/****************************************************************/
/****************************************************************/
public class GenericCommandResponseModel implements Serializable{
	
	public static final int NO_CONVERSION  = 0;
	public static final int HTML_TO_PDF    = 1;
	public static final int HTML_TO_CALC   = 2;
	
	private int conversionType = NO_CONVERSION;
	
	private String suggestedFileName = null;
	private Vector chartsParamaters;
	private String contentType = "";
	private byte[] content = null;
	private int contentLength = -1;
	
	public byte[] getContent() {
		return content;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public int getContentLength() {
		return contentLength;
	}

	public void setContentLength(int contentLength) {
		this.contentLength = contentLength;
	}

	public int getConversionType() {
		return conversionType;
	}

	public void setConversionType(int conversionType) {
		this.conversionType = conversionType;
	}

	public Vector getChartsParamaters() {
		return chartsParamaters;
	}

	public void setChartsParamaters(Vector chartsParamaters) {
		this.chartsParamaters = chartsParamaters;
	}

	public String getSuggestedFileName() {
		return suggestedFileName;
	}

	public void setSuggestedFileName(String suggestedFileName) {
		this.suggestedFileName = suggestedFileName;
	}

}
