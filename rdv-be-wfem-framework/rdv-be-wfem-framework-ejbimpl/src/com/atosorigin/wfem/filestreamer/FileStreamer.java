package com.atosorigin.wfem.filestreamer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import com.atosorigin.wfem.controller.Configuration;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class FileStreamer {
	
	private HttpMethod method = null;	
	private File tempFile = null;
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public boolean remoteFileExist(String fileServerName, String fileName) throws Exception{
		FileStreamerInfo info = getRemoteFileStream(fileServerName, fileName, true);
		return info.isFileExist();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public FileStreamerInfo getRemoteFileStream(String fileServerName, String fileName) throws Exception{
		return getRemoteFileStream(fileServerName, fileName, false);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private FileStreamerInfo getRemoteFileStream(String fileServerName, String fileName, 
												 boolean onlyCheck) throws Exception{
		
		fileName = fileName.replaceAll("\\\\","/");
		
		int streamBlockSize = Configuration.getInstance().getFileServerStreamBlockSize(fileServerName);
		
		String remoteServer = Configuration.getInstance().getFileServerUrl(fileServerName);
		if(remoteServer == null){
			String errorMsg = "File server ["+fileServerName+"] not configured in wfem.properties";
			Exception e = new Exception(errorMsg);
			throw e;
		}
				
		FileStreamerInfo info = new FileStreamerInfo();
		info.setInMemory(true);
		
		try{
			
			HttpClient httpCli = new HttpClient();
			if(remoteServer.endsWith("/"))
				remoteServer = remoteServer.substring(0,(remoteServer.length()-1));
			String url = remoteServer+"/wfemfilestreamer/call.wfemFileStreamer";
			
	        Vector params = new Vector();
	        NameValuePair par = null;
	        par = new NameValuePair("filename",fileName); params.add(par);
	        par = new NameValuePair("onlycheck",""+onlyCheck); params.add(par);
	        par = new NameValuePair("streamBlockSize",""+streamBlockSize); params.add(par);
	
	        NameValuePair[] np = new NameValuePair[]{};
	        np = (NameValuePair[])params.toArray(np);
			method = new PostMethod(url);			
	        method.setQueryString(np);
	        int res;
	        try{
	        	res = httpCli.executeMethod(method);
	        }catch(Exception e){
	        	info.setHttpResult(-1);
	        	info.setErrorMessage(e.toString());
				return info;
	        }

			info.setHttpResult(HttpServletResponse.SC_OK);
			
			if(res == HttpServletResponse.SC_NOT_FOUND){
				info.setFileExist(false);
				return info;
			}
			
			info.setFileExist(true);
			if(res == HttpServletResponse.SC_NO_CONTENT && onlyCheck)
				return info;
			
			if(res != HttpServletResponse.SC_OK){
				info.setHttpResult(res);
				return info;
			}
			
			boolean isDirectory = false; 
			Header isDirHeader = method.getResponseHeader("is-directory");
			if(isDirHeader != null && method.getResponseHeader("is-directory").getValue().equalsIgnoreCase("true"))
				isDirectory = true;
			if(isDirectory){
				byte[] xmlResp = method.getResponseBody();
				info.setDirectory(true);
				info.setContent(xmlResp);
				info.setFileLength(xmlResp.length);
			}else{
				info.setFileLength(Integer.parseInt(method.getResponseHeader("content-length").getValue()));
				if(info.getFileLength() <= Configuration.getInstance().getFileServerInMemoryFileSize(fileServerName)){
					info.setContent(method.getResponseBody());
					return info;
				}
				InputStream is = method.getResponseBodyAsStream();
				if(info.getFileLength() <= Configuration.getInstance().getFileServerOnDiskFileSize(fileServerName))
					is = localFileInputStreamFromResponseInputStream(fileServerName,is,fileName,streamBlockSize);
				info.setInputStream(is);
				info.setInMemory(false);
			}
			return info;
			
		}catch(Exception e){
        	info.setHttpResult(-1);
        	info.setErrorMessage(e.toString());
			return info;
		}finally{
			if(method != null && (info.isInMemory() || this.tempFile != null)){
				try{ method.releaseConnection(); }catch(Exception e){e.printStackTrace();}
				method = null;
			}
		}
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void close(){
		if(method != null){
			try{ method.releaseConnection(); }catch(Exception e){e.printStackTrace();}
			method = null;
		}
		
		if(this.tempFile != null){
			try{
				this.tempFile.delete();
			}catch(Exception e){
				e.printStackTrace();
			}
			this.tempFile = null;
		}
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private InputStream localFileInputStreamFromResponseInputStream(String fileServerName, InputStream is, 
																	String fileName, int streamBlockSize) throws IOException{
		this.tempFile = File.createTempFile("FILESTREAMER",".wfem");
		this.tempFile.deleteOnExit();
		FileOutputStream out = null;
	    try{
	    	out = new FileOutputStream(this.tempFile);
		    byte[] buf = new byte[streamBlockSize];
		    int charsRead;
		    while ((charsRead = is.read(buf)) != -1) {
		    	out.write(buf, 0, charsRead);
		    }
	    }finally{
	    	try{is.close();}catch(Exception e){}
	    	if(out != null){
	    		try{out.flush(); out.close();}catch(Exception e){}
	    	}
	    }		
	    InputStream fis = new FileInputStream(this.tempFile);
	    return fis;
	}
	
}
