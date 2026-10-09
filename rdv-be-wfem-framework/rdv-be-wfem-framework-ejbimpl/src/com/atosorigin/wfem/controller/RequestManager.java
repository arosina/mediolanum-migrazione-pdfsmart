package com.atosorigin.wfem.controller;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadBase.SizeLimitExceededException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.fileupload.servlet.ServletRequestContext;
import org.owasp.esapi.errors.IntrusionException;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.loggers.ControllerLogger;
import com.atosorigin.wfem.tierlog.AbstractAccessLogger;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.FileType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TypeError;
import com.atosorigin.wfem.util.Tools;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class RequestManager {
	
	private static ControllerLogger LOG = ControllerLogger.getInstance();
	
	protected boolean fromClientRequest = true;
	
	private HttpServletRequest  request;
	private HttpServletResponse response;
	
	private String redirWebApp;
	private Map requestParameters = new HashMap();
	private String language;
	private ArrayList<String> reservedRequestPropNames = new ArrayList<String>();
	private ArrayList<String> reservedRequestPropNamesToSanitize = new ArrayList<String>();
	private StringBuffer clientTierInputParameters = new StringBuffer();
	
	private transient HttpSession mainSession;
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public RequestManager(HttpServletRequest request, HttpServletResponse response, 
						  ArrayList<String> reservedRequestPropNames, ArrayList<String> reservedRequestPropNamesToSanitize) throws Exception{
		this.request = request;
		this.response = response;
		this.mainSession = request.getSession();
		this.reservedRequestPropNames = reservedRequestPropNames;
		this.reservedRequestPropNamesToSanitize = reservedRequestPropNamesToSanitize;
	}
		
	/**************************************************************************************************/
	/**************************************************************************************************/
	public boolean parseRequest() throws Exception, SizeLimitExceededException{
		
		boolean browserInstaceFounded = false;
		clientTierInputParameters = new StringBuffer();
		ServletRequestContext srctx = new ServletRequestContext(getRequest());
		if(ServletFileUpload.isMultipartContent(srctx)){
			
			LOG.debug("--- Start parsing MULTIPART CONTENT request ---");
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			long maxSize = 750*1024;
			if(getSession().getAttribute("uploadMaxSize") != null){
				try{
					maxSize = ((Long)getSession().getAttribute("uploadMaxSize")).longValue();
				}catch(NumberFormatException nfe){}
			}
			upload.setSizeMax(maxSize);
			List items=null;
			try{
				items = upload.parseRequest(srctx);
			}catch(SizeLimitExceededException slee){
				throw slee;
			}
			Iterator iter = items.iterator();
			while(iter.hasNext()){
			    FileItem item = (FileItem) iter.next();
			    if(item.isFormField()){
		    		String propertyName = item.getFieldName();
					String propertyValue;
		    		try{
		    			propertyValue = item.getString();
		    		}catch(ClassCastException cce){
						LOG.debug("Property ["+propertyName+"] is not a String. Continue");
		    			continue;
		    		}
		    		if(propertyValue != null){
		    			propertyValue = propertyValue.replaceAll("\\xA0"," ");
		    			propertyValue = propertyValue.trim();
		    		}
				    LOG.debug("Put parameter ["+propertyName+"] with value ["+propertyValue+"] in request as attribute");
				    setAttribute(propertyName,propertyValue);
				    if(!reservedRequestPropNames.contains(propertyName)) {
				    	clientTierInputParameters.append("|"+propertyName+"="+AbstractAccessLogger.formatParameters(propertyValue)+"|");
				    }
				    else {
					   	XSSDetector.checXSS(propertyName, propertyValue);
					}
				    if("BrowserInstance".equals(propertyName))
				    	browserInstaceFounded=true;
			    }else{
					String propertyName = item.getFieldName();
				    LOG.debug("Put file parameter ["+propertyName+"] with name ["+item.getName()+"] in request as attribute");
				    setAttribute(propertyName,item);
			    }
			}
			
		}else{
			
			LOG.debug("--- Start parsing request ---");
			Enumeration pars = getRequest().getParameterNames();
			while(pars.hasMoreElements()){
				String propertyName = (String)pars.nextElement();
				String propertyValue;
	    		try{
	    			propertyValue = getRequest().getParameter(propertyName);
	    		}catch(ClassCastException cce){
	    			continue;
	    		}
	    		if(propertyValue != null){
	    			propertyValue = propertyValue.replaceAll("\\xA0"," ");
	    			propertyValue = propertyValue.trim();
	    		}
			    LOG.debug("Put parameter ["+propertyName+"] with value ["+propertyValue+"] in request as attribute");
			    setAttribute(propertyName,propertyValue);
			    if(!reservedRequestPropNames.contains(propertyName)) {
			    	clientTierInputParameters.append("|"+propertyName+"="+AbstractAccessLogger.formatParameters(propertyValue)+"|");
			    } else {
			    	XSSDetector.checXSS(propertyName, propertyValue);
			    }
			    if("BrowserInstance".equals(propertyName))
			    	browserInstaceFounded=true;
			}
			
		}
		LOG.debug("--- End parsing request ---");
		if(Configuration.getInstance().isTraceBrowserInstancesNotFoundActive() && !browserInstaceFounded)
			return false;
		return true;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void loadCommandDataModelProperties(CommandDataModel dataModel) throws Exception{
		loadCommandDataModelProperties(null, dataModel);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void loadCommandDataModelProperties(Command command, CommandDataModel dataModel) throws Exception{
	    try {
	    	
	        boolean resetErrors = true;
	        try{
		        String s = (String)getAttribute(ControllerServlet.BHV_RESET_ERRORS);
		        if(s != null && !s.equals(""))
		        	resetErrors = Boolean.valueOf(s).booleanValue();
	        }catch(Exception e){}

	        boolean resetWarnings = true;
	        try{
		        String s = (String)getAttribute(ControllerServlet.BHV_RESET_WARNINGS);
		        if(s != null && !s.equals(""))
		        	resetWarnings = Boolean.valueOf(s).booleanValue();
	        }catch(Exception e){}
	        
	        boolean resetMessages = true;
	        try{
		        String s = (String)getAttribute(ControllerServlet.BHV_RESET_MESSAGES);
		        if(s != null && !s.equals(""))
		        	resetMessages = Boolean.valueOf(s).booleanValue();
	        }catch(Exception e){}
	        
	        boolean manageChangedFields = true;
	        try{
		        String s = (String)getAttribute(ControllerServlet.BHV_MANAGE_CHANGED_FIELDS);
		        if(s != null && !s.equals(""))
		        	manageChangedFields = Boolean.valueOf(s).booleanValue();
	        }catch(Exception e){}
	        
	        boolean loadMappedPropertiesFields = false;
	        try{
		        String s = (String)getAttribute(ControllerServlet.BHV_LOAD_MAPPED_PROPERTIES_FIELDS);
		        if(s != null && !s.equals(""))
		        	loadMappedPropertiesFields = Boolean.valueOf(s).booleanValue();
	        }catch(Exception e){}
	        
			String propertiesNamePrefix = (String)getAttribute("__wfemPropertiesNamePrefix");
			if(propertiesNamePrefix == null || propertiesNamePrefix.equals(""))
				propertiesNamePrefix = "";
				
			if(!propertiesNamePrefix.equals("")){
		        // Resetting of global field status
		        if(resetErrors)
					dataModel.resetCommandErrors();
				if(resetMessages)
					dataModel.resetCommandMessages();
				if(resetWarnings)
					dataModel.resetCommandWarnings();				
				dataModel.setValid(true);
				
				if(manageChangedFields)
					Tools.resetChangedAndValid(dataModel);
				dataModel = (CommandDataModel)Tools.getPropertyValue(dataModel,propertiesNamePrefix);
			}
			
	        // Notify the loading of the request
	        dataModel.beforeRequestLoading();
	
	        // Resetting of global field status
	        if(resetErrors)
				dataModel.resetCommandErrors();
	        if(resetMessages)
				dataModel.resetCommandMessages();
	        if(resetWarnings)
				dataModel.resetCommandWarnings();
			dataModel.setValid(true);
			
			// Reset of changed/valid flag on all the models and inner types
			if(manageChangedFields)
				Tools.resetChangedAndValid(dataModel);
			boolean someFieldChanged = false;
			
			// Get parameters from request
			String skippableFields = (String)getAttribute(ControllerServlet.SKIPPABLE_FIELDS);
			if(skippableFields == null)
				skippableFields = "";
			dataModel.setSkippableFields(skippableFields);
			
			boolean checkCodDescFields = true;
			String checkCodDescFieldsPar = (String)getAttribute(ControllerServlet.BHV_CHECK_CODDESC_FIELDS);
			if(checkCodDescFieldsPar != null && checkCodDescFieldsPar.equalsIgnoreCase("false"))
				checkCodDescFields = false;
			
			LOG.debug("--- Start loading request parameters into model ["+dataModel+"] ---");
			Iterator pars = requestParameters.keySet().iterator();			
			while(pars.hasNext()){
				
				String propertyName = (String)pars.next();
				Object propertyValue = getAttribute(propertyName);
				if(propertyValue instanceof String){
					LOG.debug("      Managing property ["+propertyName+"] with value ["+propertyValue+"]");
					if(loadCommandDataModelProperty(command,dataModel,(String)propertyValue,propertyName,checkCodDescFields,loadMappedPropertiesFields))
						someFieldChanged = true;										
				}else if(propertyValue instanceof FileItem){
					LOG.debug("      Managing file property ["+propertyName+"] with value ["+((FileItem)propertyValue).getName()+"]");
					loadCommandDataModelUploadProperty(dataModel,propertyName,(FileItem)propertyValue);
				}
			}
			
			LOG.debug("--- End loading parameters from request into model ["+dataModel+"] ---");
			if(someFieldChanged && manageChangedFields){
				LOG.debug("Setting changed flag for model ["+dataModel+"] and childs");
				Tools.setChanged(dataModel);
			}
	        
	    } catch (Exception e) {
	        String errorMsg = "Exception in loading properties for data model ["+dataModel+"]: "+e;
	        Exception ne = new Exception(errorMsg);
	        LOG.error(ne);
	        throw ne;
	    }
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private boolean loadCommandDataModelProperty(Command command, CommandDataModel dataModel, String value,
									       		 String propertyName, boolean checkCodDescField, boolean loadMappedPropertiesFields) throws Exception{
		
		if((dataModel instanceof MapCommandDataModel) && reservedRequestPropNames.contains(propertyName))
			return false;
		
		Class propertyType = Tools.getPropertyType(dataModel,propertyName);
		if(propertyType == null){			
			if(dataModel instanceof MapCommandDataModel){
				propertyType = StringType.class;
			}else{
				LOG.debug("         Type for property ["+propertyName+"] not found. Continue");
				return false;
			}
		}
		
		Class parSuperType = propertyType.getSuperclass();
        if(parSuperType == null || !(parSuperType.getName().equals(AbstractType.class.getName()))){
			LOG.debug("         Property ["+propertyName+"] is not an AbstractType. Continue");
        	return false;
        }
		
		LOG.debug("         Load property ["+propertyName+"] of type ["+propertyType+"] with value ["+value+"]");
		if(value != null)
			value = value.trim();
		
		boolean result = false;
		AbstractType prop = null;
		try{
			
			prop = (AbstractType)Tools.getPropertyValue(dataModel,propertyName);
			if(prop != null){
				try{
					prop = (AbstractType) prop.clone();
					if(value != null && !value.equals(prop.toString())){
						prop.setChanged(true);
						result = true;
					}
					if(dataModel.isSkippable(propertyName))
						prop.setSkippable(true);
					else
						prop.setSkippable(false);
					
					if (!dataModel.isSkipXssValidation(propertyName)) {
						XSSDetector.checXSS(propertyName, value);
					}					
					
					prop.setStringValue(value);
					prop.removeTypeError(prop.getClass().getName().substring(prop.getClass().getName().lastIndexOf(".")+1)+"FormatException");
					prop.setValid(true);
					
					if(prop.isChanged()){
						prop.resetTypeErrors();
						prop.resetTypeWarnings();
					}else{
						if(prop.isSkippable())
							prop.resetTypeWarnings();
					}
				}catch(FieldFormatException ffe){
					LOG.warning("         Warning !!!! Field ["+propertyName+"] as invalid format");
				 	prop.setStringValue("");
					prop.addTypeError(new TypeError(prop.getClass().getName().substring(prop.getClass().getName().lastIndexOf(".")+1)+"FormatException"));
					prop.setValid(false);
					dataModel.setValid(false);
				}
				Tools.setPropertyValue(dataModel,propertyName,prop);
			}
		}
		catch(IntrusionException ine){
			throw ine;
		}
		catch(Exception e){
			prop = null;
		}
		
		if(prop == null){
			try{
				prop = AbstractType.newInstance(propertyType, value);
			}catch(FieldFormatException ffe){
				prop = AbstractType.newInstance(propertyType,"");
				LOG.warning("         Warning !!!! Field ["+propertyName+"] as invalid format");
				prop.addTypeError(new TypeError(prop.getClass().getName().substring(prop.getClass().getName().lastIndexOf(".")+1)+"FormatException"));
				prop.setValid(false);
				dataModel.setValid(false);
			}			
			if(value != null && !value.equals(prop.toString())){
				prop.setChanged(true);
				result = true;
			}
			if(dataModel.isSkippable(propertyName))
				prop.setSkippable(true);
			Tools.setPropertyValue(dataModel,propertyName,prop);
		}
		
	    // Manage the CodDesc field
		if(checkCodDescField){
			if(!Tools.checkCodDescFieldValidity(dataModel,propertyName))
				dataModel.setValid(false);
		}
		return result;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private void loadCommandDataModelUploadProperty(CommandDataModel dataModel, String propertyName,
												    FileItem item) throws Exception{

		Class propertyType = Tools.getPropertyType(dataModel,propertyName);
		if(propertyType == null){
			LOG.debug("         Type for property ["+propertyName+"] not found. Continue");
			return;
		}
        if(!propertyType.getName().equals(FileType.class.getName())){
			LOG.debug("         Property ["+propertyName+"] is not a FileType. Continue");
        	return;
        }
		
		try{
			
	    	FileType propertyValue = (FileType)Tools.getPropertyValue(dataModel,propertyName);
	    	if(propertyValue == null)
	    		propertyValue = new FileType();
			
		    String fileName = item.getName();
		    if(fileName == null || fileName.length() == 0){
	    		propertyValue.clear();
		    	return;
		    }
	    	String contentType = item.getContentType();
	    	long sizeInBytes = item.getSize();
			LOG.debug("         Load file property ["+propertyName+"] - Filename ["+fileName+"] - ContentType ["+contentType+"] - IsInMemory ["+propertyValue.isInMemory()+"] - SizeInBytes ["+sizeInBytes+"]");
			if(propertyValue.isInMemory())
				propertyValue.setFileContent(item.get());
			else
				propertyValue.setItem(item);
			propertyValue.setFileName(fileName);
			propertyValue.setContentType(contentType);
			Tools.setPropertyValue(dataModel,propertyName,propertyValue);
			
		}catch(Exception e){
	        String errorMsg = "Exception in loading file upload property ["+propertyName+"]: "+e;
	        e = new Exception(errorMsg);
	        LOG.error(e);
		}
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public HttpSession getSession() {
		return getRequest().getSession();
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public HttpServletRequest getRequest() {
		return request;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public HttpServletResponse getResponse() {
		return response;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public Object getAttribute(String attributeName) {
		return requestParameters.get(attributeName);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setAttribute(String attributeName, Object attributeValue) {
	    if(reservedRequestPropNamesToSanitize.contains(attributeName))
	    	attributeValue = applyCrossSiteScriptingRegex(attributeValue);
		requestParameters.put(attributeName,attributeValue);
		getRequest().setAttribute(attributeName,attributeValue);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void removeAttribute(String attributeName) {
		requestParameters.remove(attributeName);
		getRequest().removeAttribute(attributeName);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getContextPath(){
		return getRequest().getContextPath();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getRemoteAddr(){
		return getRequest().getRemoteAddr();
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public Map getRequestParameters() {
		return requestParameters;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public StringBuffer getClientTierInputParameters() {
		return clientTierInputParameters;
	}
	
	/********************************************************************/
	/********************************************************************/
	private Object applyCrossSiteScriptingRegex(Object src) {
		if(src == null || !(src instanceof String))
			return src;
		if(((String)src).length() == 0)
			return src;
		return ((String)src).replaceAll(Configuration.getInstance().getCrossSiteScriptingRegex(), "");
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getRedirWebApp() {
		return redirWebApp;
	}

	public void setRedirWebApp(String redirWebApp) {
		this.redirWebApp = redirWebApp;
	}

	public HttpSession getMainSession() {
		return mainSession;
	}

}
