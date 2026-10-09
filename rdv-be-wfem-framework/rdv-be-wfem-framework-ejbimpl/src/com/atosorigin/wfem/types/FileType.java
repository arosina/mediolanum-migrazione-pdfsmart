package com.atosorigin.wfem.types;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.commons.fileupload.FileItem;

import com.atosorigin.wfem.controller.Configuration;
import com.atosorigin.wfem.controller.FieldFormatException;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class FileType extends AbstractType implements java.io.Serializable {

	public  static final int CONTENT_TYPE_MEMORY_SPACE = 251;
	public  static final int FILE_NAME_MEMORY_SPACE = 251;
	private static final int HEADER_LENGTH = CONTENT_TYPE_MEMORY_SPACE+FILE_NAME_MEMORY_SPACE; 
	
	private transient FileItem item = null; 	// To write on DB
	private transient File file = null;			// To read from DB
	
    private String  fileTypes="";
    private String  fileName=null;
	private String  contentType="";
	private boolean virusExamined;
	private boolean inMemory = true;
	
	/****************************************************************/
	/****************************************************************/
	public FileType() {
	}

	/****************************************************************/
	/****************************************************************/
	public FileType(String fileTypes){
		this.fileTypes = fileTypes;
	}
	
	/****************************************************************/
	/****************************************************************/
	public FileType(byte[] fileTypeAndContent) {
		setByteArrayValue(fileTypeAndContent);
	}
	
	/****************************************************************/
	/****************************************************************/
	public FileType(byte[] fileContent, String contentType, String fileName) {
		setFileContent(fileContent);
		setContentType(contentType);
		setFileName(fileName);
	}
	
	/****************************************************************/
	/****************************************************************/
	public byte[] byteArrayValue(){
		if(isNull())
			return null;
		
		byte[] byteArrayValue = new byte[getFileContent().length+HEADER_LENGTH];
		
		byte[] bContentType = new byte[CONTENT_TYPE_MEMORY_SPACE]; 
		byte[] bFileName = new byte[FILE_NAME_MEMORY_SPACE]; 
		byte[] bFileContent = getFileContent();
		
		System.arraycopy(getContentType().getBytes(),0,bContentType,0,getContentType().getBytes().length);
		System.arraycopy(getFileName().getBytes(),0,bFileName,0,getFileName().getBytes().length);
		
		System.arraycopy(bContentType,0,byteArrayValue,0,CONTENT_TYPE_MEMORY_SPACE);
		System.arraycopy(bFileName,0,byteArrayValue,CONTENT_TYPE_MEMORY_SPACE,FILE_NAME_MEMORY_SPACE);
		System.arraycopy(bFileContent,0,byteArrayValue,HEADER_LENGTH,bFileContent.length);
		return byteArrayValue;
	}
	
	/****************************************************************/
	/****************************************************************/
	public void setByteArrayValue(byte[] fileTypeAndContent){
		if(fileTypeAndContent == null){
			clear();
			return;
		}
		byte[] bContentType = new byte[CONTENT_TYPE_MEMORY_SPACE]; 
		byte[] bFileName = new byte[FILE_NAME_MEMORY_SPACE]; 
		byte[] bFileContent = new byte[fileTypeAndContent.length-HEADER_LENGTH];
		
		StringBuffer ct = new StringBuffer();
		System.arraycopy(fileTypeAndContent,0,bContentType,0,CONTENT_TYPE_MEMORY_SPACE);
		for(int i=0;i<CONTENT_TYPE_MEMORY_SPACE;i++){
			char c = (char)bContentType[i];
			if(c == 0)
				break;
			ct.append(c);
		}
		
		StringBuffer fn = new StringBuffer();
		System.arraycopy(fileTypeAndContent,CONTENT_TYPE_MEMORY_SPACE,bFileName,0,FILE_NAME_MEMORY_SPACE);
		for(int i=0;i<FILE_NAME_MEMORY_SPACE;i++){
			char c = (char)bFileName[i];
			if(c == 0)
				break;
			fn.append(c);
		}
		
		System.arraycopy(fileTypeAndContent,HEADER_LENGTH,bFileContent,0,fileTypeAndContent.length-HEADER_LENGTH);
		
		contentType = ct.toString();
		fileName = fn.toString();
		setFileContent(bFileContent);		
	}
	
	/****************************************************************/
	/****************************************************************/
	public void setFileName(String fileName) {
		if(fileName == null){
			this.fileName = null;
		}else{
			fileName = fileName.replaceAll("\\\\","\\/");
			if(fileName.lastIndexOf("/") >= 0)
				fileName = fileName.substring(fileName.lastIndexOf("/")+1);
			this.fileName = fileName;
		}
	}

	/****************************************************************/
	/****************************************************************/
	public long getLength(){
		if(this.item != null)
			return this.item.getSize();
		if(this.file != null)
			return this.file.length();
		if((byte[])getValue() != null)
			return ((byte[])getValue()).length;
		return 0;
	}
	
	/****************************************************************/
	/****************************************************************/
	public InputStream getInputStream(){
		try{
			if(this.item != null)
				return this.item.getInputStream();
			if(this.file != null)
				return new FileInputStream(this.file);
			if((byte[])getValue() != null)
				return new ByteArrayInputStream((byte[])getValue());
			return null;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	/****************************************************************/
	/****************************************************************/
	public boolean isNull(){
		if(!super.isNull())
			return false;
		
		if(this.item != null)
			return false;
		
		if(this.file != null)
			return false;
		return true;
	}

	/****************************************************************/
	/****************************************************************/
	public int compareTo(AbstractType o) {
		return 0;
	}

	/****************************************************************/
	/****************************************************************/
	public boolean equals(AbstractType o) {
		return false;
	}

	/****************************************************************/
	/****************************************************************/
	public String toString() {
		return getFileName();
	}
	
	/****************************************************************/
	/****************************************************************/
	public void setStringValue(String newValue) throws FieldFormatException{
		throw new FieldFormatException("Impossible to set a String into a ByteArray");
	}

	/****************************************************************/
	/****************************************************************/
	public byte[] getFileContent() {
		if((byte[])getValue() != null)
			return (byte[])getValue();
		
		if(this.item != null)
			return this.item.get();
		
		if(this.file != null){
			try{
				InputStream is = new FileInputStream(this.file);
                byte[] buf = new byte[Configuration.getInstance().getFileServerStreamBlockSize("")];
                int charsRead;
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                while ((charsRead = is.read(buf)) != -1) {
                    out.write(buf, 0, charsRead);
                    out.flush(); 
                }
                byte[] content = out.toByteArray();
                setFileContent(content);
                return content;
			}catch(Exception e){e.printStackTrace();}
		}
		return null;
	}

	/****************************************************************/
	/****************************************************************/
	public void setFileContent(byte[] fileContent) {
		setValue(fileContent);
	}

	/****************************************************************/
	/****************************************************************/
	public String getContentType() {
		return contentType;
	}

	/****************************************************************/
	/****************************************************************/
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	/****************************************************************/
	/****************************************************************/
	public String getFileName() {
		if(!isNull() && fileName == null)
			return "---";
		if(isNull() && fileName == null)
			return "";
		return fileName;
	}

	/****************************************************************/
	/****************************************************************/
	public void clear() {
		setFileContent(null);
		setFileName("");
		setContentType("");
		
		if(this.item != null)
			this.item.delete();
		this.item = null;
		
		if(this.file != null)
			this.file.delete();
		this.file = null;
	}

	/****************************************************************/
	/****************************************************************/
	public String getFileTypes() {
		return fileTypes;
	}

	/****************************************************************/
	/****************************************************************/
	public void setFileTypes(String fileTypes) {
		this.fileTypes = fileTypes;
	}

	/****************************************************************/
	/****************************************************************/
	public boolean isVirusExamined() {
		return virusExamined;
	}

	/****************************************************************/
	/****************************************************************/
	public void setVirusExamined(boolean virusExamined) {
		this.virusExamined = virusExamined;
	}

	/****************************************************************/
	/****************************************************************/
	public void setItem(FileItem item) {
		this.item = item;
		
		if(this.file != null)
			this.file.delete();
		this.file = null;
	}

	/****************************************************************/
	/****************************************************************/
	public void setFile(File file) {
		this.file = file;
		
		if(this.item != null)
			this.item.delete();
		this.item = null;
	}

	/****************************************************************/
	/****************************************************************/
	public File getFile(){
		try{
			InputStream is = getInputStream();
			File file = new File(System.getProperty("java.io.tmpdir")+System.getProperty("file.separator")+getFileName());
		    FileOutputStream out = new FileOutputStream(file);
		    byte[] buf = new byte[Configuration.getInstance().getFileServerStreamBlockSize("")];
		    int charsRead;
		    while ((charsRead = is.read(buf)) != -1) {
		        out.write(buf, 0, charsRead);
		        out.flush(); 
		    }
		    out.close();
		    is.close();
		    return file;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	/****************************************************************/
	/****************************************************************/
	public boolean isInMemory() {
		return inMemory;
	}

	/****************************************************************/
	/****************************************************************/
	public void setInMemory(boolean inMemory) {
		this.inMemory = inMemory;
	}
}
