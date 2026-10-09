package com.atosorigin.wfem.controller;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.atosorigin.wfem.layout.Template;

/**************************************************************************************************/
/**************************************************************************************************/
public interface ResourceSupplier {

	public void init(String langCode, boolean templateCacheEnabled,	HttpServletRequest request);
	public String getHeader(Template pageTemplate);
	public String getFooter(Template pageTemplate);
	
	public void init(String langCode, HttpServlet servlet,  RequestManager requestManager);
	public String getScript(String scriptFileName);
	public long getScriptDate(String scriptFileName);
	public String getCss();
	public long getCssDate();
	public byte[] getImage(String imgFileName);
	public long getImageDate(String imgFileName);
	public String getBlankPage();
	public long getBlankPageDate();
	public String getResource(String fileName);
	public long getResourceDate(String fileName);
	public boolean checkHeader(HttpServletRequest request, HttpServletResponse response, long date) throws Exception;
	
}
