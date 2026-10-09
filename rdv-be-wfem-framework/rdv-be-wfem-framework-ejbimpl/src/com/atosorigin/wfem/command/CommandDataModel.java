package com.atosorigin.wfem.command;

import java.io.Serializable;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

import com.atosorigin.wfem.coddesc.CodDescData;
import com.atosorigin.wfem.coddesc.CodDescDataList;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.util.Tools;

/***********************************************************************************************
 * @author Ricotti Corrado
 ***********************************************************************************************/
public abstract class CommandDataModel implements Serializable {

	public static final int INSERT_MODALITY = 1;
	public static final int UPDATE_MODALITY = 2;
	public static final int READ_MODALITY   = 3;
	public static final int PRINT_MODALITY  = 4;

	transient protected com.atosorigin.wfem.util.Logger LOG = com.atosorigin.wfem.util.Logger.getInstance();
	transient private UserSessionContext userSessionContext;

	private java.util.Map nestedModels = new java.util.HashMap();
	
	private java.util.List commandErrors   = new java.util.Vector();
	private java.util.List commandMessages = new java.util.Vector();
	private java.util.List commandWarnings = new java.util.Vector();

	private int modality = INSERT_MODALITY;

	private String skippableFields = new String();
	private String skippableXssValidationFields = new String();
	
	private String copyFields = new String();
	
	private boolean changed = false;
	private boolean persistent = false;
	private boolean valid = true;
	private long uploadMaxSize = -1;
	
	private HashMap codDescFields    = new HashMap();
	private HashMap codDescDataLists = new HashMap();

	private String codDescEmptyValue = "------";

	transient private Template template;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel() {}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isSkippable(String propName) {
		StringTokenizer st = new StringTokenizer(skippableFields.toString(),",");
		while(st.hasMoreTokens()){
			String tok = st.nextToken();
			if(tok.equals(propName))
				return true;
		}
		return false;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isSkipXssValidation(String propName) {
		StringTokenizer st = new StringTokenizer(skippableXssValidationFields,",");
		while(st.hasMoreTokens()){
			String tok = st.nextToken();
			if(tok.equals(propName))
				return true;
		}
		return false;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void addCommandErrors(java.util.List errorList) {
	
		for ( int i = 0; i < errorList.size(); i++ ) {
			commandErrors.add(errorList.get(i));
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void addCommandWarnings(java.util.List warningList) {
	
		for ( int i = 0; i < warningList.size(); i++ ) {
			commandWarnings.add(warningList.get(i));
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void addCommandMessages(java.util.List messageList) {
	
		for ( int i = 0; i < messageList.size(); i++ ) {
			commandMessages.add(messageList.get(i));
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void addCodDescDataList(String codDescReferenceName, 
		                           CodDescDataList dataList){
	    codDescDataLists.put(codDescReferenceName,dataList);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void addCodDescField(String propertyName,  String codDescReferenceName){
	    codDescFields.put(propertyName,codDescReferenceName);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void addCodDescField(String propertyName,  CodDescDataList dataList){
		addCodDescField(propertyName,propertyName+"List");
		addCodDescDataList(propertyName+"List",dataList);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void addCommandError( String error ) {
		CommandError ce = new CommandError(error);
		addCommandError(ce);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void addCommandError( CommandError commandError ) {
		if(commandErrors.contains(commandError))
			return;
		commandErrors.add( commandError );
	}

	/***********************************************************************************************
	 * @deprecated
	 ***********************************************************************************************/
	@Deprecated
	public void addError( CommandError commandError ) {
		if(commandErrors.contains(commandError))
			return;	
		commandErrors.add( commandError );
	}
	
	/***********************************************************************************************
	 * @deprecated
	 ***********************************************************************************************/
	@Deprecated
	public void addErrorList(java.util.Vector errorList) {
	
		for ( int i = 0; i < errorList.size(); i++ ) {
			commandErrors.add(errorList.elementAt(i));
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void addCommandMessage( String message ) {
		CommandMessage cm = new CommandMessage(message);
		addCommandMessage(cm);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void addCommandMessage( CommandMessage commandMessage ) {	
		if(commandMessages.contains(commandMessage))
			return;	
		commandMessages.add( commandMessage );
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void addCommandWarning( String warning ) {
		CommandWarning cw = new CommandWarning(warning);
		addCommandWarning(cw);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void addCommandWarning( CommandWarning commandWarning ) {
		if(commandWarnings.contains(commandWarning))
			return;	
		commandWarnings.add( commandWarning );
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void beforeRequestLoading() {
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean checkCodDescFieldValidity(String propertyName) {
		
		boolean result = true;
	
		// Field is not registered ad CodDescField
	    String codDescReferenceName = (String)codDescFields.get(propertyName);
	    if(codDescReferenceName == null)
	    	return result;
	
	    // CodDEsc structure info is not loaded into model	
		if(codDescDataLists.isEmpty())
			return result;
	
		try{
				
		    AbstractType propertyValue = (AbstractType)Tools.getPropertyValue(this, propertyName);
		    if(propertyValue == null)
		    	return result;
		    	
		    CodDescDataList dataList = (CodDescDataList)codDescDataLists.get(codDescReferenceName);
		    CodDescData data = dataList.getCodDesc(propertyValue.toString());
		    if(data != null && !data.isValid()){
		        propertyValue.setValid(false);
	        	addError(new CommandError("CodDescValidity"+codDescReferenceName));
	        	result=false;
		    }
		    
		}catch(Exception e){
			String errmsg = getClass()+".checkCodDescFieldValidity: Exception ["+e+"] in checking cod/description field ["+propertyName+"] in model ["+this+"]";
			addError(new CommandError(errmsg));
			result = false;
		}
		return result;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void clearAllCodDescDataLists(){
	    codDescDataLists.clear();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public CodDescDataList getCodDescDataList(String propertyName){
		  
	    String tableName = (String)codDescFields.get(propertyName);
	    if(tableName == null)
	      return null;
	
	    CodDescDataList dataList = (CodDescDataList)codDescDataLists.get(tableName);        
	    return dataList;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public String getCodDescReference(String propertyName){		  
	    return (String)codDescFields.get(propertyName);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public java.lang.String getCodDescEmptyValue() {
		return codDescEmptyValue;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public HashMap getCodDescFields() {
	    return codDescFields;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public String getDescValue(String propertyName) {
	
	    String daoCodDescName = (String)codDescFields.get(propertyName);
	    if(daoCodDescName == null){
	      doLogWarning(getClass()+".getDescValue(): No binding defined for property ["+propertyName+"]");
	      return "";
	    }
	    
	    try{
	
	        com.atosorigin.wfem.types.AbstractType propertyValue = (com.atosorigin.wfem.types.AbstractType)com.atosorigin.wfem.util.Tools.getPropertyValue(this, propertyName);
	
	        CodDescDataList dataList = (CodDescDataList)codDescDataLists.get(daoCodDescName);
	        if(dataList == null){
		        doLogWarning(getClass()+".getDescValue(): Table ["+daoCodDescName+"] not loaded for property ["+propertyName+"]");
			    return "";
	        }
	        
	        String propertyCode = propertyValue.toString();
	        for(int i=0;i<dataList.getCodDescCount();i++){
	
	          CodDescData data = dataList.getCodDesc(i);
	          String code = data.getCod();
	          String desc = data.getDescr();
	
	          if(code.equals(propertyCode))
		         return desc;
	          
	        }
		    return "";
	
	      }
	      catch (Exception e) {
	        String errorMsg = "CommandDataModel "+this.getClass()+": Exception in getting cod/desc value for property " +
	        				  propertyName + " from Cod/Description reference " + daoCodDescName + ": " + e;
	        Exception ne = new Exception(errorMsg);
	        doLogError(ne);
	        return "";
	      }
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public String getShortDescValue(String propertyName) {
	
	    String daoCodDescName = (String)codDescFields.get(propertyName);
	    if(daoCodDescName == null){
	      doLogWarning(getClass()+".getShortDescValue(): No binding defined for property ["+propertyName+"]");
	      return getCodDescEmptyValue();
	    }
	    
	    try{
	
	        com.atosorigin.wfem.types.AbstractType propertyValue = (com.atosorigin.wfem.types.AbstractType)com.atosorigin.wfem.util.Tools.getPropertyValue(this, propertyName);
	
	        CodDescDataList dataList = (CodDescDataList)codDescDataLists.get(daoCodDescName);
	        if(dataList == null){
		        doLogWarning(getClass()+".getShortDescValue(): Table ["+daoCodDescName+"] not loaded for property ["+propertyName+"]");
			    return getCodDescEmptyValue();
	        }
	        String propertyCode = propertyValue.toString();
	
	        for(int i=0;i<dataList.getCodDescCount();i++){
	
	          CodDescData data = dataList.getCodDesc(i);
	          String code = data.getCod();
	          String desc = data.getShortDescr();
	
	          if(code.equals(propertyCode))
		         return desc;
	          
	        }
	
		    return getCodDescEmptyValue();
	
	      }
	      catch (Exception e) {
	        String errorMsg = "CommandDataModel "+this.getClass()+": Exception in getting cod/desc value for property " +
	            propertyName + " from Cod/Description reference " + daoCodDescName + ": " + e;
	        Exception ne = new Exception(errorMsg);
	        doLogError(ne);
	        return getCodDescEmptyValue();
	      }
	}

	/***********************************************************************************************
	 * @deprecated
	 ***********************************************************************************************/
	@Deprecated
	public java.util.Vector getErrors() {
		return (Vector)commandErrors;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public java.util.List getCommandErrors() {
		return commandErrors;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public java.util.List getCommandMessages() {
		return commandMessages;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public String getModelName() {
	
		String modelClass = this.getClass().getName();
	
		int lastPoint = modelClass.lastIndexOf(".");
	
		if ( lastPoint != -1 ) {
			return modelClass.substring( lastPoint+1 );
		}
		return modelClass;
	}

	/***********************************************************************************************
	 * @deprecated
	 ***********************************************************************************************/
	@Deprecated
	public java.util.Map getNestedModels() {
		return nestedModels;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getPropertyType(String propertyName) throws Exception {
		return Tools.getPropertyType(this,propertyName);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public Object getPropertyValue(String propertyName) throws Exception {
		return Tools.getPropertyValue(this,propertyName);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public java.util.List getCommandWarnings() {
		return commandWarnings;
	}

	/***********************************************************************************************
	 * @deprecated
	 ***********************************************************************************************/
	@Deprecated
	public boolean hasErrors() {
		return (this.commandErrors.size() > 0);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean hasCommandErrors() {
		return (this.commandErrors.size() > 0);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean hasCommandMessages() {
		return (this.commandMessages.size() > 0);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean hasCommandWarnings() {
		return (this.commandWarnings.size() > 0);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	protected void initObject() {
		try{
			com.atosorigin.wfem.util.Tools.initObject(this);
		}catch(Exception e){
			String errorMsg = getClass().getName()+".initObject: Exception in initialize object: "+e;
			Exception ne = new Exception(errorMsg);
			doLogError(ne);
		}
	}

	/***********************************************************************************************
	 * @deprecated
	 ***********************************************************************************************/
	@Deprecated
	public boolean isError() {
		return (this.commandErrors.size() > 0);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isPersistent() {
		return persistent;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public String makeCombo(String propertyName) {
	
	    String comboStringIni = "<SELECT name="+propertyName+">";
	    String comboStringEnd = "</SELECT>";
	    
	    String daoCodDescName = (String)codDescFields.get(propertyName);
	    if(daoCodDescName == null){
	      doLogWarning(getClass()+".makeCombo(): No binding defined for property ["+propertyName+"]");
	      return comboStringIni + comboStringEnd;
	    }
	
	    try{
	
	        CodDescDataList dataList = getCodDescDataList(propertyName);
	        if(dataList == null){
		      doLogWarning(getClass()+".makeCombo(): Table ["+daoCodDescName+"] not loaded for property ["+propertyName+"]");
		      return comboStringIni + comboStringEnd;
		    }
	        
	        com.atosorigin.wfem.types.AbstractType propertyValue = (com.atosorigin.wfem.types.AbstractType)com.atosorigin.wfem.util.Tools.getPropertyValue(this, propertyName);
	        String propertyCode = propertyValue.toString();
	        String options = "";
	
	        if(propertyValue == null || propertyValue.isNull())
	          options += "<OPTION SELECTED value=\"\">" + getCodDescEmptyValue();
	        else
	          options += "<OPTION value=\"\">" + getCodDescEmptyValue();
	
	        for(int i=0;i<dataList.getCodDescCount();i++){
	
	          CodDescData data = dataList.getCodDesc(i);
	          String code = data.getCod();
	          String desc = data.getDescr();
	
	          if(code.equals(propertyCode)){
	                  options += "<OPTION SELECTED value =" + code + ">";
	          }else{
	                  options += "<OPTION value =" + code + ">";
	          }
	          if(!data.isValid())
	          	options += "* ";
	          options += desc;
	        }
	
	        return comboStringIni + options + comboStringEnd;
	
	      }
	      catch (Exception e) {
	        String errorMsg = "CommandDataModel "+this.getClass()+": Exception in generating web combo for property " +
	                          propertyName + ": " + e;
	        Exception ne = new Exception(errorMsg);
	        doLogError(ne);
	        return comboStringIni + comboStringEnd;
	      }
	}

	/***********************************************************************************************
	 * @deprecated
	 ***********************************************************************************************/
	@Deprecated
	public void resetErrors() {
		commandErrors.clear();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void resetCommandErrors() {
		commandErrors.clear();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void resetCommandMessages() {
		commandMessages.clear();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void resetCommandWarnings() {
		commandWarnings.clear();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void setCodDescEmptyValue(java.lang.String newCodDescEmptyValue) {
		codDescEmptyValue = newCodDescEmptyValue;
	}
	
	/***********************************************************************************************
	 * @deprecated
	 ***********************************************************************************************/
	@Deprecated
	public void setNestedModels(java.util.Map newNestedModels) {
		nestedModels = newNestedModels;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void setPersistent(boolean newPersistent) {
		persistent = newPersistent;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isChanged() {
		return changed;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void doLogWarning(String msg){
		if(LOG == null)
			LOG = com.atosorigin.wfem.util.Logger.getInstance();
		LOG.warning(msg);	
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private void doLogError(Exception e){
		if(LOG == null)
			LOG = com.atosorigin.wfem.util.Logger.getInstance();
		LOG.error(e);	
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public UserSessionContext getUserSessionContext() {
		return userSessionContext;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void setUserSessionContext(UserSessionContext userSessionContext) {
		this.userSessionContext = userSessionContext;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public String getSkippableFields() {
		return skippableFields;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void setSkippableFields(String skippableFields) {
		this.skippableFields = skippableFields;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	/***********************************************************************************************/
	public String getCopyFields() {
		return copyFields;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void setCopyFields(String copyFields) {
		this.copyFields = copyFields;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean isValid() {
		return valid;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void setValid(boolean valid) {
		this.valid = valid;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public int getModality() {
		return modality;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void setModality(int modality) {
		this.modality = modality;
	}

	public long getUploadMaxSize() {
		return uploadMaxSize;
	}

	public void setUploadMaxSize(long uploadMaxSize) {
		this.uploadMaxSize = uploadMaxSize;
	}

	public Template getTemplate() {
		return template;
	}

	public void setTemplate(Template template) {
		this.template = template;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public String retriveSkippableXssValidationFields() {
		return skippableXssValidationFields;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void assignSkippableXssValidationFields(String skippableXssValidationFields) {
		this.skippableXssValidationFields = skippableXssValidationFields;
	}

}
