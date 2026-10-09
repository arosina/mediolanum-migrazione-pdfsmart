package com.atosorigin.wfem.controller;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletOutputStream;

/************************************************************
 *  @author: Ricotti Corrado
 *************************************************************/
public class RedirectServletOutputStream extends ServletOutputStream {

	private ByteArrayOutputStream outputStream;
	private PrintWriter  writer;
	private RedirectServletResponse responseWrapper;
	    
    public RedirectServletOutputStream(RedirectServletResponse responseWrapper) throws UnsupportedEncodingException {
	    this.responseWrapper    = responseWrapper;
        this.outputStream       = new ByteArrayOutputStream();
        this.writer             = new PrintWriter(new BufferedWriter(new OutputStreamWriter(this, responseWrapper.getCharacterEncoding())));
    }
    public void close() throws IOException {
    	responseWrapper.forwardToResponse(outputStream);
        outputStream.close();
    }
    public void flush() throws IOException {
    	responseWrapper.forwardToResponse(outputStream);
        outputStream.flush();
    }
    public OutputStream getOutputStream() throws java.io.IOException {
        return outputStream;
    }
    public PrintWriter getWriter() throws java.io.IOException {
        return writer;
    }
    public void write(int b) throws java.io.IOException {
        outputStream.write(b);
    }
}
