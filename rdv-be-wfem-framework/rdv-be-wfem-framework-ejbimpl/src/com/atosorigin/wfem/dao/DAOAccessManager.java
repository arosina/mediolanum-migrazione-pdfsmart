package com.atosorigin.wfem.dao;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.controller.Configuration;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.loggers.DAOLogger;
import com.atosorigin.wfem.tierlog.AbstractAccessLogger;
import com.atosorigin.wfem.tierlog.MiddleTierAccessLogger;
import com.atosorigin.wfem.tierlog.TierAccessLoggerInfo;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.AbstractTypePropertyDescriptor;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ByteArrayType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.FileType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;
import com.atosorigin.wfem.util.Tools;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
class DAOAccessManager {

	private static DAOAccessManager singleton = null;

	private DAOTableAccessManager    tableAccessManager;
	private DAOQueryAccessManager    queryAccessManager;
	private DAOCallableAccessManager callableAccessManager;
	private DAOQASAccessManager 	 qasAccessManager;
	private DAOOSBAccessManager 	 	 osbAccessManager;
	
	private static final String thisClassName = DAOAccessManager.class.getName();
	protected static DAOLogger LOG = DAOLogger.getInstance();

	/*************************************************************************************************/
	/*************************************************************************************************/
	protected DAOAccessManager() {
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	protected DAOQASResultModel doQASAccess(ClientSessionContext csc, 
										   DAOQASAccessInfo daoQASAccessInfo,
										   CommandDataModel inputDataModel, boolean onlyPrepare) throws DAOException {
		TierAccessLoggerInfo tli = MiddleTierAccessLogger.initTier(csc,"QAS",daoQASAccessInfo);
		DAOQASResultModel res = getQASAccessManager().doAccess(csc,daoQASAccessInfo,inputDataModel,onlyPrepare);
		tli.stop(res.getMiddleTierInputParameters());
		return res;
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	protected DAOOSBResultModel doOSBAccess(ClientSessionContext csc, 
										   DAOOSBAccessInfo daoOSBAccessInfo,
										   CommandDataModel inputDataModel, boolean onlyPrepare) throws DAOException {
		TierAccessLoggerInfo tli = MiddleTierAccessLogger.initTier(csc,"WS",daoOSBAccessInfo);
		DAOOSBResultModel res = getOSBAccessManager().doAccess(csc,daoOSBAccessInfo,inputDataModel,onlyPrepare);
		tli.stop(res.getMiddleTierInputParameters());
		return res;
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	protected DAOCallableResultModel doCallableAccess(ClientSessionContext csc, Connection dbConnection,
												   	  DAOCallableAccessInfo daoCallableAccessInfo,
												      CommandDataModel inputOutputDataModel) throws DAOException {
		TierAccessLoggerInfo tli = MiddleTierAccessLogger.initTier(csc,"CALLABLE",daoCallableAccessInfo);
		DAOCallableResultModel res = getCallableAccessManager().doAccess(csc,dbConnection,daoCallableAccessInfo,inputOutputDataModel);
		tli.stop(res.getMiddleTierInputParameters());
		return res;
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	protected DAOQueryResultModel doQueryAccess(ClientSessionContext csc, Connection dbConnection,
												DAOObject dao,
							                 	DAOQueryAccessInfo daoQueryAccessInfo,
						                     	CommandDataModel inputParametersDataModel) throws DAOException {
		dao.tierAccessLoggerInfo = null;
		String accessType = "QUERY";
		if(dao.fetchableQuery)
			accessType = "FETCHABLEQUERY";
		TierAccessLoggerInfo tli = MiddleTierAccessLogger.initTier(csc,accessType,daoQueryAccessInfo);
		DAOQueryResultModel res = getQueryAccessManager().doAccess(csc,dbConnection,dao,daoQueryAccessInfo,inputParametersDataModel);
		if(dao.fetchableQuery)
			dao.tierAccessLoggerInfo = tli;
		else
			tli.stop(res.getMiddleTierInputParameters());
		return res;
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	protected CommandDataModel fetchQuery(DAOQueryResultModel result) throws DAOException {
		CommandDataModel res = getQueryAccessManager().fetchQuery(result);
		if(res == null && result.dao.tierAccessLoggerInfo != null)
			result.dao.tierAccessLoggerInfo.stop(result.getMiddleTierInputParameters());
		return res;
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	protected DAOTableResultModel doTableAccess(ClientSessionContext csc, Connection dbConnection,
						     				    DAOTableAccessInfo daoTableAccessInfo,
						     				    CommandDataModel dataModel,
						     				    Class outputDataModelClass,
						     				    int tableAccessMode) throws DAOException {
		TierAccessLoggerInfo tli = MiddleTierAccessLogger.initTier(csc,"TABLE",daoTableAccessInfo);
		DAOTableResultModel res = getTableAccessManager().doAccess(csc,dbConnection,daoTableAccessInfo,dataModel,
																   outputDataModelClass,tableAccessMode);
		tli.stop(res.getMiddleTierInputParameters());
		return res;
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	private DAOQASAccessManager getQASAccessManager() {
		return qasAccessManager;
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	private DAOOSBAccessManager getOSBAccessManager() {
		return osbAccessManager;
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	private DAOCallableAccessManager getCallableAccessManager() {
		return callableAccessManager;
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	protected static DAOAccessManager getInstance() {
	    if (singleton == null) {
	        synchronized (DAOAccessManager.class) {
	            if (singleton == null) {
	                singleton = new DAOAccessManager();
					singleton.setTableAccessManager(new DAOTableAccessManager());
					singleton.setQueryAccessManager(new DAOQueryAccessManager());
					singleton.setCallableAccessManager(new DAOCallableAccessManager());
					singleton.setQASAccessManager(new DAOQASAccessManager());
					singleton.setOSBAccessManager(new DAOOSBAccessManager());
	            }
	        }
	    }
		return singleton;
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	private DAOQueryAccessManager getQueryAccessManager() {
		return queryAccessManager;
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	private DAOTableAccessManager getTableAccessManager() {
		return tableAccessManager;
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	protected Object createRow(ResultSet rs,
							    String outputModelClassName,
							    DAOAccessParameters parameters,
							    String booleanTrueValue,
							    String booleanFalseValue) throws Exception {
	
		String pkg = "com.atosorigin.wfem.types.";
		if(outputModelClassName.endsWith("StringType"))
			outputModelClassName = pkg+"StringType";
		else if(outputModelClassName.endsWith("BooleanType"))
			outputModelClassName = pkg+"BooleanType";
		else if(outputModelClassName.endsWith("IntegerType"))
			outputModelClassName = pkg+"IntegerType";
		else if(outputModelClassName.endsWith("DoubleType"))
			outputModelClassName = pkg+"DoubleType";
		else if(outputModelClassName.endsWith("DateType"))
			outputModelClassName = pkg+"DateType";
		else if(outputModelClassName.endsWith("TimestampType"))
			outputModelClassName = pkg+"TimestampType";
		else if(outputModelClassName.endsWith("ByteArrayType"))
			outputModelClassName = pkg+"ByteArrayType";
		else if(outputModelClassName.endsWith("FileType"))
			outputModelClassName = pkg+"FileType";
		
		
		Class modelClass;
		try{
			modelClass = Class.forName(outputModelClassName);
		}catch(ClassNotFoundException cnfe){
	        String errorMsg = thisClassName + ".createRow: ClassNotFoundException ["+cnfe+"] in loading class ["+outputModelClassName+"]";
	        Exception e = new Exception(errorMsg);
	        LOG.error(e);
	        throw e;
		}
		
		try {
			
			Object obj = modelClass.newInstance();
			if(obj instanceof CommandDataModel){
				CommandDataModel model = (CommandDataModel)obj;
				DAOResultSet daoRs = new DAOResultSet(rs);
				loadRow(daoRs,model,parameters,booleanTrueValue,booleanFalseValue,1);
				return model;		
				
			}else if(obj instanceof AbstractType){
				
				Object dbValue = null;
				AbstractType result = (AbstractType)obj;
				
				if(result instanceof StringType){
					
					dbValue = rs.getString(1);
					if(!rs.wasNull())
						dbValue = ((String)dbValue).trim();
					else
						dbValue = null;				
						
					result = new StringType((String)dbValue);
					
				}else if(result instanceof BooleanType){
	
					dbValue = new Boolean("false");
					String booleanDB = rs.getString(1);
					if(rs.wasNull())
						booleanDB = null;
						
					if(booleanDB != null &&
					   booleanDB.equalsIgnoreCase(booleanTrueValue))
						dbValue =  new Boolean("true");
	
					result = new BooleanType((Boolean)dbValue);
					
				}else if(result instanceof IntegerType){
					
					dbValue =  rs.getBigDecimal(1);
					if(rs.wasNull())
						dbValue = null;					
					result = new IntegerType((BigDecimal)dbValue);
					
				}else if(result instanceof DoubleType){
	
					dbValue = rs.getBigDecimal(1);
					if(rs.wasNull())
						dbValue = null;
					result = new DoubleType((BigDecimal)dbValue);
					
				}else if(result instanceof DateType){
					
					dbValue = rs.getDate(1);
					if(rs.wasNull())
						dbValue = null;
					result = new DateType((java.util.Date)dbValue);
					
				}else if(result instanceof TimestampType){
					
					dbValue = rs.getTimestamp(1);
					if(rs.wasNull())
						dbValue = null;	
					result = new TimestampType((java.sql.Timestamp)dbValue);
					
				}else if(result instanceof ByteArrayType){
					
					dbValue = rs.getBytes(1);
					if(rs.wasNull())
						dbValue = null;	
					result = new ByteArrayType((byte[])dbValue);
					
				}else{
					
					String errorMsg = thisClassName+".createRow for single result: ATTENTION !!!!! THE TYPE "+result.getClass().getName()+" IS NOT MANAGED !!!!!";
					Exception e = new Exception(errorMsg);
					LOG.error(e);
					throw e;
					
				}				
				LOG.debug(thisClassName+".createRow: Returning query single value ["+result+"]");
				return result;
				
			}else{
		        String errorMsg = thisClassName + ".createRow: Class of type ["+modelClass+"] are not managed";
		        Exception e = new Exception(errorMsg);
		        LOG.error(e);
		        throw e;
			}
			
		}catch(Exception e){
	        String errorMsg = thisClassName + ".createRow: Exception ["+e+"] creating row instance for model ["+outputModelClassName+"]";
	        e = new Exception(errorMsg);
			LOG.error(e);
			throw e;
		}	
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	private boolean checkChanged(CommandDataModel model, AbstractType wt, Object value){
		if(wt.isNull() && value == null)
			return false;
		
		if(wt.isNull() && value != null){
			model.setChanged(true);
			return false;
		}
		
		if(!wt.isNull() && value == null){
			model.setChanged(true);
			return false;
		}
		return true;
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	protected void loadRow(DAOResultSet rs,
						     CommandDataModel model,
						     DAOAccessParameters parameters,
						     String booleanTrueValue,
						     String booleanFalseValue,
						     int offset) throws Exception {
	
		try {
			
			// If dynamic model load properties into model as db columns
			if(model instanceof MapCommandDataModel){
				MapCommandDataModel mapCommandDataModel = (MapCommandDataModel)model;
				AbstractTypePropertyDescriptor[] props = rs.getColumnsAsProperties();
				for(int i=0;i<props.length;i++){
					if(parameters == null || parameters.getParametersCount() == 0){
						mapCommandDataModel.addProperty(props[i].getName(),props[i].getValue());
					}else{
						if(i < parameters.getParametersCount()){
							AbstractType mapval = props[i].getValue();
							DAOAccessParameter mappar = parameters.getParameter(i);
							if(mappar.getPropertyType() != null && mappar.getPropertyType().length() > 0)
								mapval = AbstractType.newInstance(Class.forName(mappar.getPropertyType()), mapval.toString());
							mapCommandDataModel.addProperty(mappar.getPropertyName(),mapval);
						}
					}
				}
			}
			
			if(parameters == null || parameters.getParametersCount() == 0)
					return;			 
	
			model.setChanged(false);
			
			for(int i=0;i<parameters.getParametersCount();i++){
	
				DAOAccessParameter parameter = parameters.getParameter(i);
		        if(parameter == null || 
			       parameter.getPropertyName() == null ||
			       parameter.getPropertyName().equals(""))
		        	continue;
									
				String propertyName = parameter.getPropertyName();
				Class  propertyType = Tools.getPropertyType(model,propertyName);
				if(propertyType == null){
					LOG.debug(thisClassName + ".loadRow: Property type for ["+propertyName+"] is null");
					continue;
				}
				
				AbstractType curValue = (AbstractType)Tools.getPropertyValue(model,propertyName);
	
				LOG.debug(thisClassName + ".loadRow: Managing property ["+propertyName+"]");
			
				if(propertyType.equals(StringType.class)){
					
					String value = rs.getString(i+offset);
					if(!rs.wasNull()){
						
						// To manage columns with not null value but with null input value
						String notNullValue = parameter.getNotNullValue();
						if(notNullValue != null && 
						   ((String)value).equals(notNullValue))
							value = null;
						else
							value = ((String)value).trim();
						
					}else{
						value = null;
					}
					
					LOG.debug(thisClassName+".loadRow: Setting StringType property ["+propertyName+"] to value ["+value+"]");
					if(curValue != null){
						StringType wt = (StringType)curValue;
						if(checkChanged(model,wt,value) && !wt.toString().equals(value))
							model.setChanged(true);
						wt.setStringValue(value);
					}else{
						model.setChanged(true);
						Tools.setPropertyValue(model,propertyName,new StringType(value));
					}
						
				}else if(propertyType.equals(BooleanType.class)){
	
					boolean value = false;
					String booleanDB = rs.getString(i+offset);
					if(rs.wasNull())
						booleanDB = null;
						
					if(booleanDB != null &&
					   booleanDB.equalsIgnoreCase(booleanTrueValue))
						value = true;
	
					LOG.debug(thisClassName+".loadRow: Setting BooleanType property ["+propertyName+"] to value ["+value+"]");
					if(curValue != null){
						BooleanType wt = (BooleanType)curValue;
						if(checkChanged(model,wt,booleanDB) && wt.booleanValue() != value)
							model.setChanged(true);
						wt.setBooleanValue(value);
					}else{
						model.setChanged(true);
						Tools.setPropertyValue(model,propertyName,new BooleanType(value));
					}
					
				}else if(propertyType.equals(IntegerType.class)){
					
					BigDecimal value = rs.getBigDecimal(i+offset);
					if(!rs.wasNull()){
						
						// To manage columns with not null value but with null input value
						String notNullValue = parameter.getNotNullValue();
						if(notNullValue != null &&  
						   ((BigDecimal)value).equals(new IntegerType(notNullValue).bigValue()))
							value = null;
							
					}else{
						value = null;
					}
					
					LOG.debug(thisClassName+".loadRow: Setting IntegerType property ["+propertyName+"] to value ["+value+"]");
					if(curValue != null){
						IntegerType wt = (IntegerType)curValue;
						if(checkChanged(model,wt,value) && !wt.bigValue().equals(value))
							model.setChanged(true);
						wt.setBigValue(value);
					}else{
						model.setChanged(true);
						Tools.setPropertyValue(model,propertyName,new IntegerType(value));
					}
					
				}else if(propertyType.equals(DoubleType.class)){
	
					BigDecimal value = rs.getBigDecimal(i+offset);
					if(!rs.wasNull()){
						
						// To manage columns with not null value but with null input value
						String notNullValue = parameter.getNotNullValue();
						if(notNullValue != null &&  
						   ((BigDecimal)value).equals(new DoubleType(notNullValue).bigValue()))
							value = null;
							
					}else{
						value = null;
					}
					
					LOG.debug(thisClassName+".loadRow: Setting DoubleType property ["+propertyName+"] to value ["+value+"]");
					if(curValue != null){
						DoubleType wt = (DoubleType)curValue;
						if(checkChanged(model,wt,value) && !wt.bigValue().equals(value))
							model.setChanged(true);
						wt.setBigValue(value);
					}else{
						model.setChanged(true);
						Tools.setPropertyValue(model,propertyName,new DoubleType(value));
					}
					
				}else if(propertyType.equals(DateType.class)){
					
					java.util.Date value = rs.getDate(i+offset);
					if(!rs.wasNull()){
	
						DateType tmpdate = new DateType((java.util.Date)value);
						String gg = tmpdate.getGG();
						String mm = tmpdate.getMM();
						String aa = tmpdate.getAA();
						String tmpdatestr = aa + "-" + mm + "-" + gg;
						// To manage columns with not null value but with null input value
						String notNullValue = parameter.getNotNullValue();
						if(notNullValue != null && 
						   tmpdatestr.equals(notNullValue))
							value = null;
					}else{
						value = null;
					}
					
					LOG.debug(thisClassName+".loadRow: Setting DateType property ["+propertyName+"] to value ["+value+"]");
					if(curValue != null){
						DateType wt = (DateType)curValue;
						if(checkChanged(model,wt,value) && !wt.dateValue().equals(value))
							model.setChanged(true);
						wt.setDateValue(value);
					}else{
						model.setChanged(true);
						Tools.setPropertyValue(model,propertyName,new DateType(value));
					}
					
				}else if(propertyType.equals(TimestampType.class)){
					
					java.sql.Timestamp value = rs.getTimestamp(i+offset);
					if(rs.wasNull())
						value = null;
					
					LOG.debug(thisClassName+".loadRow: Setting TimestampType property ["+propertyName+"] to value ["+value+"]");
					if(curValue != null){
						TimestampType wt = (TimestampType)curValue;
						if(checkChanged(model,wt,value) && !wt.timestampValue().equals(value))
							model.setChanged(true);
						wt.setTimestampValue(value);
					}else{
						model.setChanged(true);
						Tools.setPropertyValue(model,propertyName,new TimestampType(value));
					}
					
				}else if(propertyType.equals(ByteArrayType.class)){
					
					byte[] value = rs.getBytes(i+offset);
					if(rs.wasNull())
						value = null;
					
					LOG.debug(thisClassName+".loadRow: Setting ByteArrayType property ["+propertyName+"] to value ["+value+"]");
					if(curValue != null)
						((ByteArrayType)curValue).setByteArrayValue(value);
					else
						Tools.setPropertyValue(model,propertyName,new ByteArrayType(value));
					
				}else if(propertyType.equals(FileType.class)){

	            	String fileTypeDataPart = parameter.getFileTypeDataPart();
	            	if(fileTypeDataPart == null){
						byte[] value = rs.getBytes(i+offset);
						if(rs.wasNull())
							value = null;
						LOG.debug(thisClassName+".loadRow: Setting FileType property ["+propertyName+"] to value ["+value+"]");
						if(curValue != null)
							((FileType)curValue).setByteArrayValue(value);
						else
							Tools.setPropertyValue(model,propertyName,new FileType(value));
	            	}else if(fileTypeDataPart.equals(DAOAccessParameter.FILETYPE_IMAGE_DATA)){
	            		if(curValue != null && !((FileType)curValue).isInMemory()){
							InputStream is = rs.getInputStream(i+offset);
							if(rs.wasNull()){
								LOG.debug(thisClassName+".loadRow: Setting FileType property ["+propertyName+"] to value [null]");
								((FileType)curValue).setFile(null);
							}else{
			                	File tmpf = File.createTempFile("DAO",".wfem");
			                	tmpf.deleteOnExit();
			    	            OutputStream out = new FileOutputStream(tmpf);
			    	            try{
			    	                byte[] buf = new byte[Configuration.getInstance().getFileServerStreamBlockSize("")];
			    	                int charsRead;
			    	                while ((charsRead = is.read(buf)) != -1) {
			    	                    out.write(buf, 0, charsRead);
			    	                    out.flush(); 
			    	                }
			    	            }finally{
			    	                is.close(); if(out != null){out.close();}
			    	            }
								LOG.debug(thisClassName+".loadRow: Setting FileType property ["+propertyName+"] to file ["+tmpf+"]");
								((FileType)curValue).setFile(tmpf);
							}
	            		}else{
							byte[] fileContent = rs.getBytes(i+offset);
							if(rs.wasNull())
								fileContent = null;
							LOG.debug(thisClassName+".loadRow: Setting FileType property ["+propertyName+"] to value ["+fileContent+"]");
							if(curValue != null)
								((FileType)curValue).setFileContent(fileContent);
							else
								Tools.setPropertyValue(model,propertyName,new FileType(fileContent,null,null));
	            		}
	            	}else if(fileTypeDataPart.equals(DAOAccessParameter.FILETYPE_CONTENTTYPE_DATA)){
						String contentType = rs.getString(i+offset);
						if(rs.wasNull())
							contentType = null;
						LOG.debug(thisClassName+".loadRow: Setting FileType property contentType for ["+propertyName+"] to value ["+contentType+"]");
						if(curValue != null)
							((FileType)curValue).setContentType(contentType);
						else
							Tools.setPropertyValue(model,propertyName,new FileType(null,contentType,null));
	            	}else if(fileTypeDataPart.equals(DAOAccessParameter.FILETYPE_IMAGE_IS_VIRUS_EXAMINED)){
						boolean value = false;
						String booleanDB = rs.getString(i+offset);
						if(rs.wasNull())
							booleanDB = null;
						if(booleanDB != null &&
						   booleanDB.equalsIgnoreCase(booleanTrueValue))
							value = true;
		
						LOG.debug(thisClassName+".loadRow: Setting FileType property virusExamined for ["+propertyName+"] to value ["+value+"]");
						if(curValue != null)
							((FileType)curValue).setVirusExamined(value);
						else
							Tools.setPropertyValue(model,propertyName,new BooleanType(value));
	            	}else if(fileTypeDataPart.equals(DAOAccessParameter.FILETYPE_FILENAME_DATA)){
						String fileName = rs.getString(i+offset);
						if(rs.wasNull())
							fileName = null;
						LOG.debug(thisClassName+".loadRow: Setting FileType property fileName for ["+propertyName+"] to value ["+fileName+"]");
						if(curValue != null)
							((FileType)curValue).setFileName(fileName);
						else
							Tools.setPropertyValue(model,propertyName,new FileType(null,null,fileName));
	            	}
					
				}else{
					
					String errorMsg = thisClassName+".loadRow: ATTENTION !!!!! THE TYPE "+propertyType.getName()+" IS NOT MANAGED !!!!!";
					Exception e = new Exception(errorMsg);
					LOG.error(e);
					throw e;
					
				}
			}
			return;
			
		}catch(Exception e){
	        String errorMsg = thisClassName + ".loadRow: Exception ["+e+"] loading row into model ["+model.getClass()+"]";
	        e = new Exception(errorMsg);
			LOG.error(e);
			throw e;
		}	
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	private void setQASAccessManager(DAOQASAccessManager newQASAccessManager) {
		qasAccessManager = newQASAccessManager;
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	private void setOSBAccessManager(DAOOSBAccessManager newOSBAccessManager) {
		osbAccessManager = newOSBAccessManager;
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	private void setCallableAccessManager(DAOCallableAccessManager newCallableAccessManager) {
		callableAccessManager = newCallableAccessManager;
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	protected StringBuffer setPsParameters(ClientSessionContext csc,
								   		   PreparedStatement ps,
								   		   CommandDataModel model,
								   		   DAOAccessParameters parameters,
								   		   DAOAccessInfo accessInfo,
								   		   int offset, boolean forChilds) throws Exception {
	
		StringBuffer middleTierParameters = new StringBuffer();
		
		if(parameters == null || model == null)
			return middleTierParameters;
			 
        String propName = null;
        String propClass = null;
        Class propType = null;
        AbstractType par = null;
	    try {
	
		    String booleanTrueValue = DAOAccessInfo.getBooleanTrueValue(); 
		    String booleanFalseValue = DAOAccessInfo.getBooleanFalseValue();
		    
	        for (int i = 0; i < parameters.getParametersCount(); i++) {
	
		        DAOAccessParameter parameter = parameters.getParameter(i);
		        if(parameter.isDisabled())
		            continue;

		        propName = parameter.getPropertyName();
	        	
		        String pVal = DAOAccessParameters.processCscVariables(csc, propName, "#");
	        	if(pVal != null){
	        		parameter.setPropertyType("com.atosorigin.wfem.types.StringType");
	        		parameter.setPropertyValue(pVal);
	        	}
	        	
	            if(forChilds && parameter.getForeignKey() != null)
	                propName = parameter.getForeignKey();
	                
		        propClass = parameter.getPropertyType();
		        if(propClass != null && !propClass.equals("")){
		        	
		        	String propValue = parameter.getPropertyValue();
			        propType = Class.forName(propClass);
	            	par = AbstractType.newInstance(propType,propValue);
	            	
		        }else{		        
		        	
			        boolean startPercent = false;
			        boolean endPercent = false;
			        if(propName.startsWith("%")){
			        	startPercent = true;
			        	propName = propName.substring(1);
			        }
			        if(propName.endsWith("%")){
			        	endPercent = true;
			        	propName = propName.substring(0,propName.length()-1);
			        }
					
			        propType = Tools.getPropertyType(model,propName);
			       	if(propType == null){
		             	String errorMsg = thisClassName + ".setPsParameters: error in getting type for property ["+propName+"] in model ["+model+"]";
		                Exception e = new Exception(errorMsg);
		                LOG.error(e);
		                throw e;
			       	}
			       	
		            par = (AbstractType)Tools.getPropertyValue(model,propName);
		            if(par != null && (startPercent || endPercent)){
		            	String parValue = par.toString();
			            if(startPercent)
			            	parValue = "%" + parValue;
			            if(endPercent)
		            		parValue = parValue + "%";
	
		            	par = AbstractType.newInstance(propType,parValue);
		            }
		            
		        }
	
	            if (propType.equals(StringType.class)) {
	
		            if(par == null || par.isNull()){
						// To manage columns with not null value but with null input value
						String notNullValue = parameter.getNotNullValue();
						if(notNullValue != null)
			                ps.setString(i + offset, notNullValue);
						else
				            ps.setNull(i + offset, Types.CHAR);		
		            }else{
		                String value = ((StringType) par).toString();
		                ps.setString(i + offset, value);
		            }
	
	            } else if (propType.equals(BooleanType.class)) {
	
		            if(par == null || par.isNull()){
			            ps.setNull(i + offset, Types.CHAR);
		            }else{
			            if (((BooleanType) par).booleanValue())
			            	ps.setString(i + offset, booleanTrueValue);
			            else
			            	ps.setString(i + offset, booleanFalseValue);
		            }
		                
	            } else if (propType.equals(IntegerType.class)) {
	
		            if(par == null || par.isNull()){
						// To manage columns with not null value but with null input value
						String notNullValue = parameter.getNotNullValue();
						if(notNullValue != null)
			                ps.setBigDecimal(i + offset, new IntegerType(notNullValue).bigValue());
						else
				            ps.setNull(i + offset, Types.INTEGER);
		            }else{
		                BigDecimal value = ((IntegerType) par).bigValue();
		                ps.setBigDecimal(i + offset, value);
		            }
	
	            } else if (propType.equals(DoubleType.class)) {
	
		            if(par == null || par.isNull()){
						// To manage columns with not null value but with null input value
						String notNullValue = parameter.getNotNullValue();
						if(notNullValue != null)
			                ps.setBigDecimal(i + offset, new DoubleType(notNullValue).bigValue());
						else
				            ps.setNull(i + offset, (accessInfo instanceof DAOCallableAccessInfo ? Types.DOUBLE : Types.DECIMAL));
		            }else{
		                BigDecimal value = ((DoubleType) par).bigValue();
		                ps.setBigDecimal(i + offset, value);
		            }
	
	            } else if (propType.equals(DateType.class)) {
	
		            if(par == null || par.isNull()){
						// To manage columns with not null value but with null input value
						String notNullValue = parameter.getNotNullValue();
						if(notNullValue != null){
							DateType dt = new DateType(notNullValue);
			                ps.setDate(i + offset, new java.sql.Date(dt.dateValue().getTime()));
						}else
				            ps.setNull(i + offset, Types.DATE);
		            }else{
		                java.util.Date value = ((DateType) par).dateValue();
		                ps.setDate(i + offset, new java.sql.Date(((java.util.Date) value).getTime()));
		            }
	
	            } else if (propType.equals(TimestampType.class)) {
	
		            if(par == null || par.isNull()){
						// To manage columns with not null value but with null input value
						String notNullValue = parameter.getNotNullValue();
						if(notNullValue != null){
							TimestampType tt = new TimestampType(notNullValue);
			                ps.setTimestamp(i + offset, tt.timestampValue());
						}else
				            ps.setNull(i + offset, Types.TIMESTAMP);
		            }else{
		                java.sql.Timestamp value = ((TimestampType) par).timestampValue();
		                ps.setTimestamp(i + offset, value);
		            }
	
	            } else if (propType.equals(ByteArrayType.class)) {
	            	
		            if(par == null || par.isNull()){
			            ps.setNull(i + offset, Types.BINARY);
		            }else{
		                byte[] value = ((ByteArrayType) par).byteArrayValue();
		                ps.setBytes(i + offset, value);
		            }
	
	            } else if (propType.equals(FileType.class)) {
	            	
		            if(par == null || par.isNull()){
			            ps.setNull(i + offset, Types.BINARY);
		            }else{
		            	String fileTypeDataPart = parameter.getFileTypeDataPart();
		            	if(fileTypeDataPart == null){
			                byte[] value = ((FileType) par).byteArrayValue();
			                ps.setBytes(i + offset, value);		            				            		
		            	}else if(fileTypeDataPart.equals(DAOAccessParameter.FILETYPE_IMAGE_DATA)){
		            		FileType ft = (FileType)par;
	            			if(ft.isInMemory())
				                ps.setBytes(i + offset, ft.getFileContent());
	            			else
	            				ps.setBinaryStream(i + offset, ft.getInputStream(), (int)ft.getLength());
		            	}else if(fileTypeDataPart.equals(DAOAccessParameter.FILETYPE_CONTENTTYPE_DATA)){
			                String contentType = ((FileType) par).getContentType();
			                ps.setString(i + offset, contentType);
		            	}else if(fileTypeDataPart.equals(DAOAccessParameter.FILETYPE_IMAGE_IS_VIRUS_EXAMINED)){
				            if (((FileType)par).isVirusExamined())
				            	ps.setString(i + offset, booleanTrueValue);
				            else
				            	ps.setString(i + offset, booleanFalseValue);
		            	}else if(fileTypeDataPart.equals(DAOAccessParameter.FILETYPE_FILENAME_DATA)){
			                String fileName = ((FileType) par).getFileName();
			                ps.setString(i + offset, fileName);		            		
		            	}
		            }
	
	            } else {
	
	             	String errorMsg = thisClassName + ".setPsParameters: ATTENTION !!!!! THE TYPE " + 
	             	                  par.getClass().getName() + " IS NOT MANAGED !!!!!";
	                Exception e = new Exception(errorMsg);
	                LOG.error(e);
	                throw e;
	
	            }
	
	            LOG.debug(thisClassName + ".setPsParameters: Setting parameter number " + (i + offset) + ", property ["+propName+"] to value [" + par +"]");
	            if(par != null)
	            	middleTierParameters.append("|"+propName+"="+AbstractAccessLogger.formatParameters(par.toString())+"|");
	        }
	        
	        return middleTierParameters;
	        
	    } catch (Exception e) {
		    
	        String errorMsg = thisClassName + ".setPsParameters: Exception ["+e+"] in setting parameter into model ["+model.getClass()+"]";
	        e = new Exception(errorMsg);
	        LOG.error(e);
	        throw e;
	       
	    }
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	private void setQueryAccessManager(DAOQueryAccessManager newQueryAccessManager) {
		queryAccessManager = newQueryAccessManager;
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	private void setTableAccessManager(DAOTableAccessManager newTableAccessManager) {
		tableAccessManager = newTableAccessManager;
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	protected static String getRealStoredName(String storedName,CommandDataModel dataModel) {
		return getRealName(storedName,dataModel);
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	protected static String getRealTableName(String tableName,CommandDataModel dataModel) {
		return getRealName(tableName,dataModel);
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	private static String getRealName(String name,CommandDataModel dataModel) {
		int idx1 = name.indexOf('%');
		if(idx1 < 0)
			return name;
		int idx2 = name.lastIndexOf('%');
		if(idx2 < 0 || idx1 == idx2)
			return name;

		String part1 = name.substring(0,idx1);
		String part2 = name.substring(idx2+1);
		String propertyName = name.substring(idx1+1,idx2);
		try {
			AbstractType propertyValue = (AbstractType)Tools.getPropertyValue(dataModel,propertyName);
			String result = part1 + propertyValue.toString() + part2;
			LOG.debug("Parsed name is: ["+result+"]");
			return result;	
		}catch (Exception e) {
			LOG.warning("Problems in parsing name ["+name+"] with model ["+dataModel+"]");
			return name;
		}		
	}
	
}
