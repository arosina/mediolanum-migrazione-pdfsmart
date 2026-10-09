package com.atosorigin.wfem.controller;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.htmltopdf.DocumentController;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class RedirectServletResponse extends javax.servlet.http.HttpServletResponseWrapper {

	private boolean htmlTrace;
	private String  htmlTraceDir;
	private String  pageName = "";
	private CommandDataModel pageModel;
	private boolean doForward = true;
	private String fileNameToDownload;
	private String suggestedFileName;
	
	private int                          responseType;
	private ServletContext               appContext;
	private RequestManager 				 requestManager;
    private RedirectServletOutputStream  redirectedStream;

	public static final int PDF_RESPONSE_TYPE = 1;
	public static final int HTML_RESPONSE_TYPE = 2;
	public static final int CALC_RESPONSE_TYPE = 3;
    
	private ByteArrayOutputStream pdfStream = null;
	private boolean forwardedToResponse = false;

	/**************************************************************************************************/
	/**************************************************************************************************/
	public RedirectServletResponse(int responseType,ServletContext appContext,
								   RequestManager requestManager, 
								   boolean htmlTrace, String htmlTraceDir, String pageName, String fileNameToDownload,
								   CommandDataModel pageModel, boolean doForward, String suggestedFileName) throws IOException{
	
		super(requestManager.getResponse());

		this.doForward = doForward;
		this.pageModel = pageModel;
		this.htmlTrace = htmlTrace;
		this.htmlTraceDir = htmlTraceDir;
		this.pageName = pageName;
		this.responseType = responseType;
		this.appContext = appContext;	
		this.requestManager = requestManager;
		this.redirectedStream = new RedirectServletOutputStream(this);
		this.fileNameToDownload = fileNameToDownload;
		this.suggestedFileName = suggestedFileName;
		
	}

	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void flushBuffer() throws IOException {
		this.redirectedStream.flush();
		this.redirectedStream.close();
		super.flushBuffer();
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void forwardToResponse(ByteArrayOutputStream htmlStream) throws IOException{
		   if(isForwardedToResponse())
			   return;
		   
		   String contentType = "text/html";
		   
		   String elId = (String)requestManager.getAttribute(ControllerServlet.HIDDEN_SUBMIT_ELEMENT_ID);
		   if(elId != null && elId.length() > 0){
			   HiddenServletResponse hiddenServletResponse = new HiddenServletResponse();
			   ByteArrayOutputStream parsedHtmlStream = hiddenServletResponse.getInnerHtmlStream(requestManager,htmlStream,elId,getCharacterEncoding());
			   if(parsedHtmlStream != null){
				   htmlStream = parsedHtmlStream;
				   if(requestManager.getResponse().containsHeader("WHSReplaceAsBody"))
					   contentType = "text/html";
				   else
					   contentType = "text/xml";
			   }else{
				   contentType = "text/html";				   
			   }
			   hiddenServletResponse=null;
		   }
		   
	       saveHtml(pageName,htmlStream);
	       
		   if(responseType == PDF_RESPONSE_TYPE){
			   
				try{
				    DocumentController doc = new DocumentController(htmlStream,appContext,requestManager,pageModel);
				    pdfStream = (ByteArrayOutputStream)doc.generatePdfDocument();
				}catch(Exception e){
					e.printStackTrace();
					throw new IOException(e.toString());
				}
				contentType =  "application/pdf";
				
		   }else if(responseType == CALC_RESPONSE_TYPE){
			   
				try{
				    DocumentController doc = new DocumentController(htmlStream,appContext,requestManager,pageModel);
				    pdfStream = (ByteArrayOutputStream)doc.generateCalcDocument();
				}catch(Exception e){
					e.printStackTrace();
					throw new IOException(e.toString());
				}
				contentType = "application/vnd.ms-excel";
				
		    }else if(responseType == HTML_RESPONSE_TYPE){
		    	
		    	fileNameToDownload = null; // No download of html response even if comes from calling
		    	pdfStream = htmlStream;
		    	
		    }
		   
			setContentType(contentType);			   		
		
		   	if(this.doForward){
			    setContentLength(pdfStream.size());
			    
			    if(fileNameToDownload != null && fileNameToDownload.length() > 0){
					setHeader("Content-Disposition","attachment; filename=\""+fileNameToDownload+"\"");
					setHeader("content-length",""+pdfStream.size());
					setHeader("file-name",fileNameToDownload);
			    }else if(suggestedFileName != null && suggestedFileName.length() > 0){
					setHeader("Content-Disposition","inline; filename=\""+suggestedFileName+"\"");
					setHeader("file-name",suggestedFileName);
			    }
			    
			    pdfStream.writeTo(getResponse().getOutputStream());
			    getResponse().getOutputStream().flush();
		   	}
		    forwardedToResponse = true;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public ServletOutputStream getOutputStream() throws java.io.IOException {
		return redirectedStream;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public PrintWriter getWriter() throws IOException {
		return redirectedStream.getWriter();
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private void saveHtml(String pageName, ByteArrayOutputStream baos) {
		if(!htmlTrace)
			return;
		try{
	
			String fileName = pageName;
			int idx = fileName.lastIndexOf(".");
			if(idx >= 0)
				fileName = fileName.substring(0,idx);
			fileName = fileName.replace('/','.');
			idx = fileName.indexOf(".");
			if(idx == 0)
				fileName = fileName.substring(1);
			if(!htmlTraceDir.endsWith("/"))
				htmlTraceDir += "/";
			fileName = htmlTraceDir + fileName + ".html";
			
			FileOutputStream fw = new FileOutputStream(fileName);
			fw.write(baos.toByteArray());
			fw.flush();
			fw.close();
	
		}catch(Exception e){
			com.atosorigin.wfem.loggers.ControllerLogger.getInstance().warning("Unable to trace HTML for page "+pageName);
		}
	}


	/**************************************************************************************************/
	/**************************************************************************************************/
	public boolean isForwardedToResponse() {
		return forwardedToResponse;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public ByteArrayOutputStream getPdfStream() {
		return pdfStream;
	}
}
