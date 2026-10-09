package com.atosorigin.wfem.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Vector;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.dao.exceptions.ConcurrencyViolation;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.dao.exceptions.NoRowsAffected;
import com.atosorigin.wfem.dao.exceptions.SQLExceptionBinder;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

/*************************************************************************************************/
/*************************************************************************************************/
class DAOTableAccessManager extends DAOAccessManager {
	
	private static final String thisClassName = DAOTableAccessManager.class.getName();
	private static final String REPLICATION_SERVER_COLUMN_NAME = "SERVER_C_REPLICA";
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	private String createDeleteString(String tableName,
									  DAOAccessParameters keyParameters) throws Exception {
		try{
			
			String deleteString = "delete from " + tableName;
			deleteString += createKeyCondition(keyParameters);
			return deleteString;
			
		}catch(Exception e){
			String errorMsg = thisClassName+".createDeleteString: Exception ["+e+"] creating delete string for table ["+tableName+"]";
			e = new Exception(errorMsg);
			LOG.error(e);
			throw e;
		}
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	private String createDeleteChildsString(String tableName,
									         DAOAccessParameters foreignKeyParameters) throws Exception {
		try{
			
			String deleteString = "delete from " + tableName;
			deleteString += createKeyCondition(foreignKeyParameters);
			return deleteString;
			
		}catch(Exception e){
			String errorMsg = thisClassName+".createDeleteChildsString: Exception ["+e+"] creating delete childs string for table ["+tableName+"]";
			e = new Exception(errorMsg);
			LOG.error(e);
			throw e;
		}
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	private String createInsertString(ClientSessionContext csc, String tableName,
									  DAOAccessParameters parameters, boolean replicable) throws Exception {
	
		try{
			
			String insertString = "insert into "+ tableName + "(";
	
			for(int i=0;i<parameters.getParametersCount();i++){
				DAOAccessParameter parameter = parameters.getParameter(i);			
				insertString += parameter.getDbColumnName();
				if(i < (parameters.getParametersCount()-1))
					insertString += ",";
			}

			if(replicable)
				insertString += ","+REPLICATION_SERVER_COLUMN_NAME;
			
			insertString += ") values (";
			
			for(int i=0;i<parameters.getParametersCount();i++){
				insertString += "?";	
				if(i < (parameters.getParametersCount()-1))
					insertString += ",";
			}
			
			if(replicable){
				if(csc.getReplicationServer() == null)
					insertString += ",NULL";
				else
					insertString += ",'"+csc.getReplicationServer()+"'";
			}

			insertString += ")";
			
			return insertString;
			
		}catch(Exception e){
			String errorMsg = thisClassName+".createInsertString: Exception ["+e+"] creating insert string for table ["+tableName+"]";
			e = new Exception(errorMsg);
			LOG.error(e);
			throw e;
		}
		
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	private String createLoadString(String tableName,
									DAOAccessParameters parameters,
									DAOAccessParameters keyParameters) throws Exception {
										
		try{
			
			String loadString = "select ";
			
			for(int i=0;i<parameters.getParametersCount();i++){
				DAOAccessParameter parameter = parameters.getParameter(i);
				loadString += parameter.getDbColumnName();
				if(i < (parameters.getParametersCount()-1))
					loadString += ",";
			}
			
			loadString += " from " + tableName;
			loadString += createKeyCondition(keyParameters);
			
			return loadString;
				
		}catch(Exception e){
			String errorMsg = thisClassName+".createLoadString: Exception ["+e+"] creating load string for table ["+tableName+"]";
			e = new Exception(errorMsg);
			LOG.error(e);
			throw e;
		}
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	private String createLoadAllString(String tableName,
									   DAOAccessParameters parameters,
									   DAOAccessParameters keyParameters,
									   CommandDataModel dataModel) throws Exception {
										
		try{
			
			String loadString = "select ";
			
			for(int i=0;i<parameters.getParametersCount();i++){
				DAOAccessParameter parameter = parameters.getParameter(i);
				loadString += parameter.getDbColumnName();
				if(i < (parameters.getParametersCount()-1))
					loadString += ",";
			}
			
			loadString += " from " + tableName;
			loadString += createAllKeyCondition(keyParameters,dataModel);
			
			return loadString;
				
		}catch(Exception e){
			String errorMsg = thisClassName+".createLoadString: Exception ["+e+"] creating load string for table ["+tableName+"]";
			e = new Exception(errorMsg);
			LOG.error(e);
			throw e;
		}
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	private String createLoadChildsString(String tableName,
										   DAOAccessParameters parameters,
										   DAOAccessParameters foreignKeyParameters) throws Exception {
										
		try{
			
			String loadString = "select ";
			
			for(int i=0;i<parameters.getParametersCount();i++){
				DAOAccessParameter parameter = parameters.getParameter(i);
				loadString += parameter.getDbColumnName();
				if(i < (parameters.getParametersCount()-1))
					loadString += ",";
			}
			
			loadString += " from " + tableName;
			loadString += createKeyCondition(foreignKeyParameters);
			
			return loadString;
				
		}catch(Exception e){
			String errorMsg = thisClassName+".createLoadChildsString: Exception ["+e+"] creating load childs string for table ["+tableName+"]";
			e = new Exception(errorMsg);
			LOG.error(e);
			throw e;
		}
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	private String createKeyCondition(DAOAccessParameters keyParameters) {
		
		String result = " where ";
		
		for(int i=0;i<keyParameters.getParametersCount();i++){
			DAOAccessParameter key = keyParameters.getParameter(i);
			result += key.getDbColumnName() + " = ?";
			if(i < (keyParameters.getParametersCount()-1))
				result += " and ";
		}
		return result;
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	private String createAllKeyCondition(DAOAccessParameters keyParameters, CommandDataModel model) {
		
		String result = " where ";
		
		if(model == null)
		    return result + " 1 = 1";
		
		boolean keyFounded = false;
		for(int i=0;i<keyParameters.getParametersCount();i++){
			DAOAccessParameter key = keyParameters.getParameter(i);
			AbstractType par = null;
			try{
			    par = (AbstractType)Tools.getPropertyValue(model,key.getPropertyName());
			}catch(Exception e){
			    e = new Exception(thisClassName+".createKeyCondition: Exception ["+e+"] retrieving value of key property ["+key.getPropertyName()+"]");
			    LOG.error(e);
			    par = null;
			}
			if(par == null || par.isNull()){
			    keyParameters.disableParameter(i);
			    continue;
			}
			
			keyFounded = true;
			result += key.getDbColumnName() + " = ? and ";
		}
		
		if(keyFounded)
		    result = result.substring(0,result.length()-5);
		else
		    result += "1 = 1";
		
		return result;
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	private String createUpdateString(String tableName,
									  DAOAccessParameters parameters,
									  DAOAccessParameters keyParameters,
		                              CommandDataModel dataModel) throws Exception {
	
		try{
				
		    String updateString = "update " + tableName + " set ";
	
		    for (int i = 0; i < parameters.getParametersCount(); i++) {
			    DAOAccessParameter parameter = parameters.getParameter(i);
		        updateString += parameter.getDbColumnName() + " = ?";
				if(i < (parameters.getParametersCount()-1))
					updateString += ",";
		    }
	
			updateString += createKeyCondition(keyParameters);
			
		    return updateString;
		    
		}catch(Exception e){
			String errorMsg = thisClassName+".createUpdateString: Exception ["+e+"] creating update string for table ["+tableName+"]";
			e = new Exception(errorMsg);
			LOG.error(e);
			throw e;
		}
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	protected DAOTableResultModel doAccess(ClientSessionContext csc, Connection dbConnection,
				 				 		   DAOTableAccessInfo dbTableAccessInfo,
				 				 		   CommandDataModel dataModel,
										   Class outputDataModelClass,							   						
				 				 		   int tableAccessMode) throws DAOException {
							   
		if(tableAccessMode != DAOTableAccessInfo.CREATE_ACCESS_MODE &&
		   tableAccessMode != DAOTableAccessInfo.LOAD_ACCESS_MODE &&
		   tableAccessMode != DAOTableAccessInfo.UPDATE_ACCESS_MODE &&
		   tableAccessMode != DAOTableAccessInfo.DELETE_ACCESS_MODE &&
		   tableAccessMode != DAOTableAccessInfo.LOAD_ALL_ACCESS_MODE &&
		   tableAccessMode != DAOTableAccessInfo.LOAD_CHILDS_ACCESS_MODE &&
		   tableAccessMode != DAOTableAccessInfo.DELETE_ALL_ACCESS_MODE &&
		   tableAccessMode != DAOTableAccessInfo.DELETE_CHILDS_ACCESS_MODE){
			String errorMsg = thisClassName + ".doAccess: Access mode ["+tableAccessMode+"] of referenced table access ["+dbTableAccessInfo.getDaoAccessName()+"] is not correct";
			DAOException daoe = new DAOException(errorMsg);
			LOG.error(daoe);
			throw daoe;
	    }
	
		DAOTableResultModel result = executeTableAccess(csc,dbConnection,dataModel,outputDataModelClass,
														dbTableAccessInfo,tableAccessMode);
		return result;
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	private DAOTableResultModel executeTableAccess(ClientSessionContext csc, Connection dbConnection,
								    				CommandDataModel dataModel,
													Class outputDataModelClass,							   						
													DAOTableAccessInfo dbTableAccessInfo,
							   						int tableAccessMode) throws DAOException{
	
		DAOTableResultModel result = new DAOTableResultModel();
		String tableName = dbTableAccessInfo.getSqlString();		
		LOG.debug(thisClassName + ".executeTableAccess: Executing table access ["+dbTableAccessInfo.getDaoAccessName()+"] and mode code ["+tableAccessMode+"]");
		tableName = getRealTableName(tableName,dataModel);
	
		String accessMode = "Invalid";
		int updatedRows = 0;
		PreparedStatement ps = null;
		ResultSet rs = null;
		 
		try {
			
	        DAOResultSet daoRs = null;
			DAOAccessParameters parameters = dbTableAccessInfo.getInputParameters();
			DAOAccessParameters keyParameters = parameters.getPrimaryKeys();
			DAOAccessParameters foreignKeyParameters = parameters.getForeignKeys();
			String sqlString = null;		
			switch(tableAccessMode){
				
				case DAOTableAccessInfo.CREATE_ACCESS_MODE:
					accessMode = "Create";
					
					boolean skipIdentity = true;
					
                    String sequenceName = dbTableAccessInfo.getSequenceName();
					if(sequenceName != null){
						if(dbTableAccessInfo.getIdentityProperty() != null && dbTableAccessInfo.getSequenceName() != null){
	                        LOG.debug(csc,"Table "+tableName+" has identity on sequence ["+sequenceName+"]. Get new counter");
	                        DAOAccessParameter identityElement = parameters.getParameter(dbTableAccessInfo.getIdentityProperty());
	                        if(!csc.isTestEnabled()){
	                        	selectIdentitySequenceProperty(csc,dbConnection,sequenceName,identityElement,dataModel);
	                        }else{
								IntegerType value =  new IntegerType(new BigDecimal((double)Calendar.getInstance().getTimeInMillis()));
								String identityPropertyName = identityElement.getPropertyName();
								Tools.setPropertyValue(dataModel,identityPropertyName,value);
	                        }
	                        skipIdentity = false;
						}
					}
			
					DAOAccessParameters createParameters = getWritableParameters(parameters,dataModel,dbTableAccessInfo,skipIdentity);
					sqlString = createInsertString(csc,tableName,createParameters,dbTableAccessInfo.isReplicable());

					if(!csc.isTestEnabled()){
						ps = dbConnection.prepareStatement(sqlString);
						result.setMiddleTierInputParameters(setPsParameters(csc,ps,dataModel,createParameters,dbTableAccessInfo,1,false));
					}
					
					LOG.debug("Executing table insert: ["+sqlString+"]");
					if(!csc.isTestEnabled())
						updatedRows = ps.executeUpdate();
					else
						updatedRows = 1;
	
					if(dbTableAccessInfo.getIdentityProperty() != null && sequenceName == null){
						LOG.debug("Table "+tableName+" has identity. Set corresponding model property");
						DAOAccessParameter identityElement = parameters.getParameter(dbTableAccessInfo.getIdentityProperty());
						if(!csc.isTestEnabled()){
							selectIdentityProperty(dbConnection,tableName,identityElement,dataModel);
						}else{
							IntegerType value =  new IntegerType(new BigDecimal((double)Calendar.getInstance().getTimeInMillis()));
							String identityPropertyName = identityElement.getPropertyName();
							Tools.setPropertyValue(dataModel,identityPropertyName,value);
						}
					}
					if(dbTableAccessInfo.getConcurrencyProperty() != null){
						LOG.debug("Table "+tableName+" has concurrency. Set corresponding model property");
						DAOAccessParameter concurrencyPar = parameters.getParameter(dbTableAccessInfo.getConcurrencyProperty());
						if(!csc.isTestEnabled())
							selectConcurrencyProperty(csc,dbConnection,dbTableAccessInfo,keyParameters,concurrencyPar,dataModel);
					}							
					LOG.debug("Table insert executed. ["+updatedRows+"] rows inserted");
					break;
					
				case DAOTableAccessInfo.LOAD_ACCESS_MODE:
					accessMode = "Load";
					
					if(dbTableAccessInfo.getConcurrencyProperty() != null){
						LOG.debug("Table "+tableName+" has concurrency. Verifing if property value is not null");
						DAOAccessParameter concurrencyPar = parameters.getParameter(dbTableAccessInfo.getConcurrencyProperty());
						StringType concurrencyVal = (StringType)Tools.getPropertyValue(dataModel,concurrencyPar.getPropertyName());
						if(concurrencyVal != null && !concurrencyVal.isNull()){
							LOG.debug("Concurrency property value is ["+concurrencyVal+"]");
							StringType savConcurrencyVal = new StringType(concurrencyVal.toString());
							selectConcurrencyProperty(csc,dbConnection,dbTableAccessInfo,keyParameters,concurrencyPar,dataModel);
							concurrencyVal = (StringType)Tools.getPropertyValue(dataModel,concurrencyPar.getPropertyName());
							if(!savConcurrencyVal.equals(concurrencyVal)){
								String errorMsg = "ConcurrencyViolation on table ["+tableName+"] - field ["+dbTableAccessInfo.getConcurrencyProperty()+"]";
								LOG.debug(errorMsg);
								throw new ConcurrencyViolation(errorMsg);
							}else{
								LOG.debug("Concurrency property value is unchanged");
							}
						}
					}							

					sqlString = createLoadString(tableName,parameters,keyParameters);
					ps = dbConnection.prepareStatement(sqlString);
					result.setMiddleTierInputParameters(setPsParameters(csc,ps,dataModel,keyParameters,dbTableAccessInfo,1,false));
					LOG.debug("Executing table load: ["+sqlString+"]");
			        rs = ps.executeQuery();
			        if(!rs.next()){
						result.setResult(new IntegerType(0));
						return result;
			        }

			        updatedRows = 1;
			        daoRs = new DAOResultSet(rs);
			        loadRow(daoRs,dataModel,parameters,DAOTableAccessInfo.getBooleanTrueValue(),DAOTableAccessInfo.getBooleanFalseValue(),1);
							
			        while(rs.next())
				        updatedRows++;

					LOG.debug("Table load executed. ["+updatedRows+"] rows loaded");
					break;
					
				case DAOTableAccessInfo.LOAD_ALL_ACCESS_MODE:
					accessMode = "LoadAll";
				
					keyParameters = (DAOAccessParameters)Tools.cloneObject(keyParameters);
					
					sqlString = createLoadAllString(tableName,parameters,keyParameters,dataModel);
					if(dbTableAccessInfo.getOrderBy() != null && !dbTableAccessInfo.getOrderBy().equals(""))
						sqlString += " order by "+dbTableAccessInfo.getOrderBy();
					
					ps = dbConnection.prepareStatement(sqlString);
					
					result.setMiddleTierInputParameters(setPsParameters(csc,ps,dataModel,keyParameters,dbTableAccessInfo,1,false));
					
					LOG.debug("Executing table load all: ["+sqlString+"]");
			        rs = ps.executeQuery();

					Vector all = new Vector();
							
			        while(rs.next()){
						Object output = createRow(rs,outputDataModelClass.getName(),
						        				  parameters,DAOTableAccessInfo.getBooleanTrueValue(),DAOTableAccessInfo.getBooleanTrueValue());
						all.addElement(output);
				        updatedRows++;
			        }

					result.setAll(new ListType(outputDataModelClass,all));	
					LOG.debug("Table load all executed. ["+updatedRows+"] rows loaded");
					result.setResult(new IntegerType(updatedRows));
					return result;
					
				case DAOTableAccessInfo.UPDATE_ACCESS_MODE:
					accessMode = "Update";
					
					DAOAccessParameters updateParameters = getUpdatableParameters(parameters,dataModel,dbTableAccessInfo);
					sqlString = createUpdateString(tableName,updateParameters,keyParameters,dataModel);
					
					if(!csc.isTestEnabled() && dbTableAccessInfo.getConcurrencyProperty() != null){
						DAOAccessParameter concurrencyPar = parameters.getParameter(dbTableAccessInfo.getConcurrencyProperty());
	            		AbstractType concurrencyVal = (AbstractType)Tools.getPropertyValue(dataModel,concurrencyPar.getPropertyName());
	            		String sConcurrencyVal = "";
	            		if(concurrencyVal != null && !concurrencyVal.isNull()){
	            			sConcurrencyVal = concurrencyVal.toString();
	            		}
	            		if(sConcurrencyVal.startsWith("0x"))
							sqlString += " and "+concurrencyPar.getDbColumnName()+" = "+concurrencyVal.toString();
	            		else
							sqlString += " and "+concurrencyPar.getDbColumnName()+" = 0x"+concurrencyVal.toString();
					}
					
					if(!csc.isTestEnabled()){
						ps = dbConnection.prepareStatement(sqlString);
						setPsParameters(csc,ps,dataModel,updateParameters,dbTableAccessInfo,1,false);				
						result.setMiddleTierInputParameters(setPsParameters(csc,ps,dataModel,keyParameters,dbTableAccessInfo,updateParameters.getParametersCount()+1,false));
					}
					
					LOG.debug("Executing table update: ["+sqlString+"]");
					if(!csc.isTestEnabled())
						updatedRows = ps.executeUpdate();
					else
						updatedRows = 1;
					
					if(updatedRows <= 0) {
						if(dbTableAccessInfo.getConcurrencyProperty() != null){
							String errorMsg = "ConcurrencyViolation on table ["+tableName+"] - field ["+dbTableAccessInfo.getConcurrencyProperty()+"]";
							LOG.debug(errorMsg);
							throw new ConcurrencyViolation(errorMsg);
						}
					}
					
					if(!csc.isTestEnabled() && dbTableAccessInfo.getConcurrencyProperty() != null){
						LOG.debug("Table "+tableName+" has concurrency. Set corresponding model property");
						DAOAccessParameter concurrencyPar = parameters.getParameter(dbTableAccessInfo.getConcurrencyProperty());
						selectConcurrencyProperty(csc,dbConnection,dbTableAccessInfo,keyParameters,concurrencyPar,dataModel);
					}
					
					LOG.debug("Table update executed. ["+updatedRows+"] rows updated");
					break;
					
				case DAOTableAccessInfo.DELETE_ACCESS_MODE:
					accessMode = "Delete";
					
					sqlString = createDeleteString(tableName,keyParameters);
						
					if(!csc.isTestEnabled() && dbTableAccessInfo.getConcurrencyProperty() != null){
						DAOAccessParameter concurrencyPar = parameters.getParameter(dbTableAccessInfo.getConcurrencyProperty());
	            		AbstractType concurrencyVal = (AbstractType)Tools.getPropertyValue(dataModel,concurrencyPar.getPropertyName());
	            		String sConcurrencyVal = concurrencyVal.toString();
	            		if(sConcurrencyVal.startsWith("0x"))
							sqlString += " and "+concurrencyPar.getDbColumnName()+" = "+concurrencyVal.toString();
	            		else
							sqlString += " and "+concurrencyPar.getDbColumnName()+" = 0x"+concurrencyVal.toString();
					}

					if(!csc.isTestEnabled()){ 
						ps = dbConnection.prepareStatement(sqlString);
						result.setMiddleTierInputParameters(setPsParameters(csc,ps,dataModel,keyParameters,dbTableAccessInfo,1,false));
					}
					
					LOG.debug("Executing table delete: ["+sqlString+"]");
					if(!csc.isTestEnabled()) 
						updatedRows = ps.executeUpdate();
					else
						updatedRows = 1;
					
					if(updatedRows <= 0) {
						if(dbTableAccessInfo.getConcurrencyProperty() != null){
							String errorMsg = "ConcurrencyViolation on table ["+tableName+"] - field ["+dbTableAccessInfo.getConcurrencyProperty()+"]";
							LOG.debug(errorMsg);
							throw new ConcurrencyViolation(errorMsg);
						}
					}				
					
					LOG.debug("Table delete executed. ["+updatedRows+"] rows deleted");
					break;

				case DAOTableAccessInfo.DELETE_ALL_ACCESS_MODE:
					accessMode = "DeleteAll";
					sqlString = "delete from " + tableName;
					if(!csc.isTestEnabled()) 
						ps = dbConnection.prepareStatement(sqlString);
					LOG.debug("Executing table delete all: ["+sqlString+"]");
					if(!csc.isTestEnabled()) 
						updatedRows = ps.executeUpdate();
					else
						updatedRows = 1;
					LOG.debug("Table delete all executed. ["+updatedRows+"] rows deleted");
					break;
					
				case DAOTableAccessInfo.LOAD_CHILDS_ACCESS_MODE:
					accessMode = "LoadChilds";
					
					sqlString = createLoadChildsString(tableName,parameters,foreignKeyParameters);
					if(dbTableAccessInfo.getOrderBy() != null && !dbTableAccessInfo.getOrderBy().equals(""))
						sqlString += " order by "+dbTableAccessInfo.getOrderBy();
					
					ps = dbConnection.prepareStatement(sqlString);
					
					result.setMiddleTierInputParameters(setPsParameters(csc,ps,dataModel,foreignKeyParameters,dbTableAccessInfo,1,true));
					
					LOG.debug("Executing table load childs: ["+sqlString+"]");
			        rs = ps.executeQuery();

					Vector childs = new Vector();
							
			        while(rs.next()){
						Object output = createRow(rs,outputDataModelClass.getName(),
						        				  parameters,DAOTableAccessInfo.getBooleanTrueValue(),DAOTableAccessInfo.getBooleanTrueValue());
						childs.addElement(output);
				        updatedRows++;
			        }

					result.setChilds(new ListType(outputDataModelClass,childs));	
					LOG.debug("Table load childs executed. ["+updatedRows+"] rows loaded");
					result.setResult(new IntegerType(updatedRows));
					return result;
					
				case DAOTableAccessInfo.DELETE_CHILDS_ACCESS_MODE:
					accessMode = "DeleteChilds";
					
					sqlString = createDeleteChildsString(tableName,foreignKeyParameters);

					if(!csc.isTestEnabled()){
						ps = dbConnection.prepareStatement(sqlString);
						result.setMiddleTierInputParameters(setPsParameters(csc,ps,dataModel,foreignKeyParameters,dbTableAccessInfo,1,true));
					}

					LOG.debug("Executing table delete childs: ["+sqlString+"]");
					if(!csc.isTestEnabled())
						updatedRows = ps.executeUpdate();
					else
						updatedRows = 1;
					LOG.debug("Table delete childs executed. ["+updatedRows+"] rows deleted");
					break;
					
			}
			if(updatedRows <= 0)
				throw new NoRowsAffected("No rows affected executing table access ["+dbTableAccessInfo.getDaoAccessName()+"] in ["+accessMode+"] mode");
		
			result.setResult(new IntegerType(updatedRows));
			return result;
				
		}catch(SQLException sqle){
	
			DAOException daoSqle = SQLExceptionBinder.mapException(sqle);
			String errorMsg = thisClassName + ".executeTableAccess: SQL exception ["+sqle+"] executing table access: ["+dbTableAccessInfo.getDaoAccessName()+"] in ["+accessMode+"] mode";
			daoSqle.setDescr(errorMsg);
			LOG.error(daoSqle);
			throw daoSqle;
			
		}catch(Exception e){
			
			String errorMsg = thisClassName + ".executeTableAccess: Exception ["+e+"] executing table access: ["+dbTableAccessInfo.getDaoAccessName()+"] in ["+accessMode+"] mode";
			DAOException daoe = new DAOException(errorMsg);
			LOG.error(daoe);
			throw daoe;
			
		}finally{
			
			try{
				if(rs != null){rs.close();rs = null;}
				if(ps != null){ps.close();ps = null;}
			}catch(Exception e){
				String errorMsg = thisClassName+".executeTableAccess: Exception ["+e+"] in closing ResultSet and/or Statement";
				DAOException daoe = new DAOException(errorMsg);
				LOG.error(daoe);
				throw daoe;
			}
			
		}
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	private DAOAccessParameters getWritableParameters(DAOAccessParameters parameters,
													  CommandDataModel dataModel,
													  DAOTableAccessInfo tableAccessInfo,
													  boolean skipIdentity) throws Exception{
	
	    String identityProperty = tableAccessInfo.getIdentityProperty();
		String concurrencyProperty = tableAccessInfo.getConcurrencyProperty();
		
		DAOAccessParameters createParameters = new DAOAccessParameters();
		
		for(int i=0;i<parameters.getParametersCount();i++){
	
			DAOAccessParameter parameter = parameters.getParameter(i);
			
			String propName = parameter.getPropertyName();
	
			// Skip identity field
			if(propName.equals(identityProperty) && skipIdentity)
				continue;
	
			// Skip concurrency field
			if(propName.equals(concurrencyProperty))
				continue;
	
			// Skip readonly field
			if(parameter.isReadonly())
				continue;
	
			// Get the property value
			AbstractType value;
			try{
			
				value = (AbstractType)Tools.getPropertyValue(dataModel,propName);
			
			}catch(ClassCastException cce){
				String errorMsg = thisClassName+".getWritableParameters: ClassCastException ["+cce+"] getting value for property ["+propName+"] on model ["+dataModel.getClass()+"]";
				Exception ne = new Exception(errorMsg);
				LOG.error(ne);
				throw ne;
			}catch(Exception e){
				String errorMsg = thisClassName+".getWritableParameters: Exception ["+e+"] getting value for property ["+propName+"] on model ["+dataModel.getClass()+"]";
				Exception ne = new Exception(errorMsg);
				LOG.error(ne);
				throw ne;
			}		
			
			// Skip readonly field only if property value in model is null or empty
			if(parameter.isReadonlyIfNull() && (value == null || value.isNull()))
				continue;

			// Add parameter to parameters list
			createParameters.addParameter(parameter);
		}
		
		return createParameters;
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	private DAOAccessParameters getUpdatableParameters(DAOAccessParameters parameters,
													    CommandDataModel dataModel,
													    DAOTableAccessInfo tableAccessInfo) throws Exception{
	
	    String identityProperty = tableAccessInfo.getIdentityProperty();
		String concurrencyProperty = tableAccessInfo.getConcurrencyProperty();
		
		DAOAccessParameters createParameters = new DAOAccessParameters();
		
		for(int i=0;i<parameters.getParametersCount();i++){
	
			DAOAccessParameter parameter = parameters.getParameter(i);
			
			String propName = parameter.getPropertyName();
	
			// Skip identity field
			if(propName.equals(identityProperty))
				continue;
	
			// Skip concurrency field
			if(propName.equals(concurrencyProperty))
				continue;
	
			// Skip readonly field
			if(parameter.isReadonly())
				continue;
	
			// Skip primaryKey field
			if(parameter.isPrimaryKey())
				continue;
	
			// Get the property value
			AbstractType value;
			try{
			
				value = (AbstractType)Tools.getPropertyValue(dataModel,propName);
			
			}catch(ClassCastException cce){
				String errorMsg = thisClassName+".getUpdatableParameters: ClassCastException ["+cce+"] getting value for property ["+propName+"] on model ["+dataModel.getClass()+"]";
				Exception ne = new Exception(errorMsg);
				LOG.error(ne);
				throw ne;
			}catch(Exception e){
				String errorMsg = thisClassName+".getUpdatableParameters: Exception ["+e+"] getting value for property ["+propName+"] on model ["+dataModel.getClass()+"]";
				Exception ne = new Exception(errorMsg);
				LOG.error(ne);
				throw ne;
			}
			
			// Skip readonly field only if property value in model is null or empty
			if(parameter.isReadonlyIfNull() && (value == null || value.isNull()))
				continue;
			
			// Add parameter to parameters list
			createParameters.addParameter(parameter);
		}
		
		return createParameters;
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	private void selectIdentityProperty(Connection dbConnection,
									    String tableName,
		                                DAOAccessParameter identityElement,
		                                CommandDataModel dataModel) throws Exception, SQLException {
			                               
	    String thisMethod = ".selectIdentityProperty: ";
		String nullIdentityMsg = "Identity DB value is NULL";
	
		String identityPropertyName = identityElement.getPropertyName();
	    String identityColumnName   = identityElement.getDbColumnName();
		Class identityPropertyType = null;
	    try{
			identityPropertyType = Tools.getPropertyType(dataModel,identityPropertyName);
	    }catch(Exception e){
	        String errorMsg = thisClassName + thisMethod + "Exception ["+e+"] in getting identity field class type of property ["+identityPropertyName+"]";
	        e = new Exception(errorMsg);
	        LOG.error(e);
	        throw e;
	    }
		
		if(identityPropertyType == null ||
		   !identityPropertyType.equals(IntegerType.class)){
			String errorMsg = getClass()+thisMethod+"ATTENTION!!! Identity property ["+identityPropertyName+"] in model ["+
							  dataModel.getClass()+"] must be an IntegerType";
			Exception e = new Exception(errorMsg);
			LOG.error(e);
			throw e;		
		}
		
	    ResultSet rs = null;
	    Statement stmt = null;
	
	    try {
	
	        stmt = dbConnection.createStatement();
	
	        String queryString = "select @@identity";
	
	        LOG.debug("Executing query: " + queryString);
	        rs = stmt.executeQuery(queryString);
	        rs.next();
	
			LOG.debug("Managing identity property ["+identityPropertyName+"] and identity column ["+identityColumnName+"]");
			
			try {
	
				BigDecimal ideVal =  rs.getBigDecimal(1);
				if(rs.wasNull()){
					SQLException e = new SQLException(nullIdentityMsg);
					LOG.error(e);
					throw e;
				}
				
				LOG.debug("Setting identity property ["+identityPropertyName+"] for identity column ["+identityColumnName+"] to value ["+ideVal+"]");
				IntegerType value =  new IntegerType(ideVal);
				Tools.setPropertyValue(dataModel,identityPropertyName,value);
				
			}catch(Exception e){
		        String errorMsg = thisClassName + thisMethod + "Exception ["+e+"] in setting identity property ["+identityPropertyName+"] into model ["+dataModel.getClass()+"]";
		        e = new Exception(errorMsg);
				LOG.error(e);
				throw e;
			}
	
	    } catch (SQLException sqle) {
	
	        String errorMsg = thisClassName + thisMethod + "SQL Exception ["+sqle+"] in selecting identity from table ["+tableName+"]";
	        sqle = new SQLException(errorMsg);
	        LOG.error(sqle);
	        throw sqle;
	
	    } catch (Exception e) {
	
	        String errorMsg = thisClassName + thisMethod + "Exception ["+e+"] in selecting identity from table ["+tableName+"]";
	        e = new Exception(errorMsg);
	        LOG.error(e);
	        throw e;
	        
	    } finally {
	
	        try {
	            if (rs != null)
	                rs.close();
	            if (stmt != null)
	                stmt.close();
	        } catch (Exception e) {
	            String errorMsg = getClass() + thisMethod + "Exception ["+e+"] in closing ResultSet and/or Statement";
	            e = new Exception(errorMsg);
	            LOG.error(e);
	            throw e;
	        }
	
	    }
	
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	private void selectIdentitySequenceProperty(ClientSessionContext csc,
												Connection dbConnection,
												String sequenceName,
												DAOAccessParameter identityElement,
												CommandDataModel dataModel) throws Exception, SQLException {
			                               
	    String thisMethod = ".selectIdentitySequenceProperty: ";
		String nullIdentityMsg = "Identity DB value is NULL";
	
		String identityPropertyName = identityElement.getPropertyName();
	    String identityColumnName   = identityElement.getDbColumnName();
		Class identityPropertyType = null;
	    try{
			identityPropertyType = Tools.getPropertyType(dataModel,identityPropertyName);
	    }catch(Exception e){
	        String errorMsg = thisClassName + thisMethod + "Exception ["+e+"] in getting identity field class type of property ["+identityPropertyName+"]";
	        e = new Exception(errorMsg);
	        LOG.error(e);
	        throw e;
	    }
		
		if(identityPropertyType == null ||
		   (!identityPropertyType.equals(IntegerType.class) && !identityPropertyType.equals(StringType.class))){
			String errorMsg = getClass()+thisMethod+"ATTENTION!!! Identity property ["+identityPropertyName+"] in model ["+
							  dataModel.getClass()+"] must be an IntegerType or a StringType";
			Exception e = new Exception(errorMsg);
			LOG.error(e);
			throw e;		
		}
		
	    ResultSet rs = null;
	    Statement stmt = null;
	
	    try {
	
	        stmt = dbConnection.createStatement();
	
	        String queryString = "select "+sequenceName+".nextval from dual";
	
	        LOG.debug(csc,"Executing query: " + queryString);
	        rs = stmt.executeQuery(queryString);
	        rs.next();
	
			LOG.debug(csc,"Managing identity property ["+identityPropertyName+"] and identity column ["+identityColumnName+"]");
			
			try {
	
				BigDecimal ideVal =  rs.getBigDecimal(1);
				if(rs.wasNull()){
					SQLException e = new SQLException(nullIdentityMsg);
					LOG.error(e);
					throw e;
				}
				
				LOG.debug(csc,"Setting identity property ["+identityPropertyName+"] for identity column ["+identityColumnName+"] to value ["+ideVal+"]");
				AbstractType value =  null;
				if(identityPropertyType.equals(IntegerType.class))
					value =  new IntegerType(ideVal);
				else
					value =  new StringType(""+ideVal);
				Tools.setPropertyValue(dataModel,identityPropertyName,value);
				
			}catch(Exception e){
		        String errorMsg = thisClassName + thisMethod + "Exception ["+e+"] in setting identity property ["+identityPropertyName+"] into model ["+dataModel.getClass()+"]";
		        e = new Exception(errorMsg);
				LOG.error(e);
				throw e;
			}
	
	    } catch (SQLException sqle) {
	
	        String errorMsg = thisClassName + thisMethod + "SQL Exception ["+sqle+"] in selecting identity from sequence ["+sequenceName+"]";
	        sqle = new SQLException(errorMsg);
	        LOG.error(sqle);
	        throw sqle;
	
	    } catch (Exception e) {
	
	        String errorMsg = thisClassName + thisMethod + "Exception ["+e+"] in selecting identity from sequence ["+sequenceName+"]";
	        e = new Exception(errorMsg);
	        LOG.error(e);
	        throw e;
	        
	    } finally {
	
	        try {
	            if (rs != null)
	                rs.close();
	            if (stmt != null)
	                stmt.close();
	        } catch (Exception e) {
	            String errorMsg = getClass() + thisMethod + "Exception ["+e+"] in closing ResultSet and/or Statement";
	            e = new Exception(errorMsg);
	            LOG.error(e);
	            throw e;
	        }
	
	    }
	
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	private void selectConcurrencyProperty( ClientSessionContext csc, 
											Connection dbConnection,
									     	DAOTableAccessInfo dbTableAccessInfo,
									     	DAOAccessParameters keyParameters,
		                                 	DAOAccessParameter concurrencyElement,
		                                 	CommandDataModel dataModel) throws Exception, SQLException {
			                               
	    String thisMethod = ".selectConcurrencyProperty: ";
		String nullConcurrencyMsg = "Concurrency DB value is NULL";
	
		String concurrencyPropertyName = concurrencyElement.getPropertyName();
	    String concurrencyColumnName   = concurrencyElement.getDbColumnName();
		Class concurrencyPropertyType = null;
	    try{
			concurrencyPropertyType = Tools.getPropertyType(dataModel,concurrencyPropertyName);
	    }catch(Exception e){
	        String errorMsg = thisClassName + thisMethod + "Exception ["+e+"] in getting concurrency field class type of property ["+concurrencyPropertyName+"]";
	        e = new Exception(errorMsg);
	        LOG.error(e);
	        throw e;
	    }
		
		if(concurrencyPropertyType == null ||
		   !concurrencyPropertyType.equals(StringType.class)){
			String errorMsg = getClass()+thisMethod+"ATTENTION!!! Concurrency property ["+concurrencyPropertyName+"] in model ["+
							  dataModel.getClass()+"] must be a StringType";
			Exception e = new Exception(errorMsg);
			LOG.error(e);
			throw e;		
		}
		
	    PreparedStatement ps = null;
	    ResultSet rs = null;
    	String tableName = dbTableAccessInfo.getSqlString();
		tableName = getRealTableName(tableName,dataModel);
	
	    try {
	    	
	
	        String queryString = "select "+concurrencyColumnName+" from "+tableName;
	        queryString += createKeyCondition(keyParameters);
	        
	        ps = dbConnection.prepareStatement(queryString);
	
			setPsParameters(csc,ps,dataModel,keyParameters,dbTableAccessInfo,1,false);
	
	        LOG.debug("Executing query: " + queryString);
	        rs = ps.executeQuery();
	        rs.next();
	
			LOG.debug("Managing concurrency property ["+concurrencyPropertyName+"] and concurrency column ["+concurrencyColumnName+"]");
			
			try {
	
				String conVal =  rs.getString(1);
				if(rs.wasNull()){
					SQLException e = new SQLException(nullConcurrencyMsg);
					LOG.error(e);
					throw e;
				}
				
				LOG.debug("Setting concurrency property ["+concurrencyPropertyName+"] for concurrency column ["+concurrencyColumnName+"] to value ["+conVal+"]");
				StringType value =  new StringType(conVal);
				Tools.setPropertyValue(dataModel,concurrencyPropertyName,value);
				
			}catch(Exception e){
		        String errorMsg = thisClassName + thisMethod + "Exception ["+e+"] in setting concurrency property ["+concurrencyPropertyName+"] into model ["+dataModel.getClass()+"]";
		        e = new Exception(errorMsg);
				LOG.error(e);
				throw e;
			}
	
	    } catch (SQLException sqle) {
	
	        String errorMsg = thisClassName + thisMethod + "SQL Exception ["+sqle+"] in selecting concurrency from table ["+tableName+"]";
	        sqle = new SQLException(errorMsg);
	        LOG.error(sqle);
	        throw sqle;
	
	    } catch (Exception e) {
	
	        String errorMsg = thisClassName + thisMethod + "Exception ["+e+"] in selecting concurrency from table ["+tableName+"]";
	        e = new Exception(errorMsg);
	        LOG.error(e);
	        throw e;
	        
	    } finally {
	
	        try {
	            if (rs != null)
	                rs.close();
	            if (ps != null)
	                ps.close();
	        } catch (Exception e) {
	            String errorMsg = getClass() + thisMethod + "Exception ["+e+"] in closing ResultSet and/or Statement";
	            e = new Exception(errorMsg);
	            LOG.error(e);
	            throw e;
	        }
	
	    }
	
	}
}
