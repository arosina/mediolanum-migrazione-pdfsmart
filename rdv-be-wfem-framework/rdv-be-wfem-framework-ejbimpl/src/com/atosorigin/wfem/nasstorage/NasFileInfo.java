package com.atosorigin.wfem.nasstorage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;


/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class NasFileInfo {
	
	private File fileReference;

	private String fileName = "";
	private String fileExt = "";
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public boolean fileExists(){
		if(this.fileReference == null)
			return false;
		return this.fileReference.exists();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getRealFilePath(){
		if(fileReference == null)
			return null;
		return fileReference.getPath();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public byte[] getFileContent(){
		if(!fileExists())
			return null;
		InputStream fileInputStream = null;
		ByteArrayOutputStream fileOutputStream = null;
		try{
			fileInputStream = new FileInputStream(this.fileReference);
			fileOutputStream = new ByteArrayOutputStream();
	        byte[] buf = new byte[(16*1024)];
	        int charsRead;
	        while ((charsRead = fileInputStream.read(buf)) != -1) {
	        	fileOutputStream.write(buf, 0, charsRead);
	        	fileOutputStream.flush();
	        }
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}finally{
	        try{ if(fileInputStream != null){fileInputStream.close();} }catch(Exception e){}
	        try{ if(fileOutputStream != null){fileOutputStream.close();} }catch(Exception e){}
		}
		if(fileOutputStream == null)
			return null;
		else
			return fileOutputStream.toByteArray();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public InputStream getInputStream(){
		if(!fileExists())
			return null;
		try{
			return new FileInputStream(this.fileReference);
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	protected void setFileReference(File fileReference) {
		this.fileReference = fileReference;
	}
	
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileExt() {
		return fileExt;
	}
	public void setFileExt(String fileExt) {
		this.fileExt = fileExt;
	}
}
