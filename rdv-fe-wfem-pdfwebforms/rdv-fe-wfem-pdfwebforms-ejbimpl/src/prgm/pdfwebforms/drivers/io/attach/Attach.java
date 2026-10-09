package prgm.pdfwebforms.drivers.io.attach;

import java.io.Serializable;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class Attach implements Serializable{
	
	public static String DEFAULT_FILE_TYPES = "jpg,jpeg,pdf";
	public static String DEFAULT_MIME_TYPES = "image/jpeg,image/jpg,application/pdf";
	public static long DEFAULT_MAXDIM = (1024 * 4000);
	
	private String 	descr = "";
	private String 	descrSuffix = "";
	private String 	fileTypes = DEFAULT_FILE_TYPES;
	private boolean mandatory = false;
	private	long	maxdim = DEFAULT_MAXDIM;
	private byte[]	implicitContent = null;
	private String 	mimeTypes = DEFAULT_MIME_TYPES;
	private boolean keepOriginal = false;

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Attach(){
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public Attach(String descr){
		this.descr = descr;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String getMaxdimMeasure(){
		String measure = "KB";
		long kb = maxdim / 1024;
		if(kb >= 1000){
			measure = "MB";
			kb = maxdim / (1024*1000);
			if(kb >= 1000){
				measure = "GB";
				kb = maxdim / (1024*1000000);					
			}
		}
		measure = ""+kb+measure;
		return measure;
	}	

	public String getDescr() {
		return descr;
	}

	public void setDescr(String descr) {
		this.descr = descr;
	}

	public long getMaxdim() {
		return maxdim;
	}

	public void setMaxdim(long maxdim) {
		this.maxdim = maxdim;
	}

	public boolean isMandatory() {
		return mandatory;
	}

	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}

	public String getFileTypes() {
		return fileTypes;
	}

	public void setFileTypes(String fileTypes) {
		this.fileTypes = fileTypes;
	}

	public byte[] getImplicitContent() {
		return implicitContent;
	}

	public void setImplicitContent(byte[] implicitContent) {
		this.implicitContent = implicitContent;
	}

	public boolean isKeepOriginal() {
		return keepOriginal;
	}

	public void setKeepOriginal(boolean keepOriginal) {
		this.keepOriginal = keepOriginal;
	}
	public String getMimeTypes() {
		return mimeTypes;
	}
	public void setMimeTypes(String mimeTypes) {
		this.mimeTypes = mimeTypes;
	}
	public String getDescrSuffix() {
		return descrSuffix;
	}
	public void setDescrSuffix(String descrSuffix) {
		this.descrSuffix = descrSuffix;
	}

}
