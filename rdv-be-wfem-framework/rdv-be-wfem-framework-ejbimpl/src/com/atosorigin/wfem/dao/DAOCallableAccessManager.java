package com.atosorigin.wfem.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.controller.Configuration;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.dao.exceptions.SQLExceptionBinder;
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
class DAOCallableAccessManager extends DAOAccessManager {

	private Configuration configuration = Configuration.getInstance();
	private static final String thisClassName = DAOCallableAccessManager.class.getName();
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	protected DAOCallableResultModel doAccess(ClientSessionContext csc, Connection dbConnection,
				 				    		  DAOCallableAccessInfo dbCallableAccessInfo,
				 				    		  CommandDataModel inputOutputDataModel) throws DAOException {
								  
		DAOCallableResultModel result = executeCallableAccess(csc,dbConnection,
										   					  inputOutputDataModel,
										   					  dbCallableAccessInfo);
		return result;
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	private DAOCallableResultModel executeCallableAccess(ClientSessionContext csc,
														 Connection dbConnection,
									  					 CommandDataModel dataModel,
							   		  		 			 DAOCallableAccessInfo dbCallableAccessInfo) throws DAOException{
	
		String storedName = dbCallableAccessInfo.getSqlString();
		LOG.debug(thisClassName + ".executeCallableAccess: Executing callable access ["+storedName+"]");
		storedName = getRealStoredName(storedName,dataModel);
	
		CallableStatement stmt = null;
		ResultSet rs = null;
		 
		try {
	
			String query = "{? = call "+ storedName;
			
			DAOAccessParameters inputParameters = dbCallableAccessInfo.getInputParameters();
			DAOAccessParameters outputParameters = dbCallableAccessInfo.getOutputParameters();
			
			if(inputParameters != null){
				for(int i=0;i<inputParameters.getParametersCount();i++){
					query += " ?";
					if(i < (inputParameters.getParametersCount()-1))
					   query += ",";
				}
		    }
			query += " }";
			   
			stmt = dbConnection.prepareCall(query);
	
			StringBuffer middleTierInputParameters = setPsParameters(csc,stmt,dataModel,inputParameters,dbCallableAccessInfo,2,false);
			
			CommandDataModel outModel = null;
			String outModelClassName = dbCallableAccessInfo.getOutputDataModelClassName();
			if(outModelClassName != null && !outModelClassName.equals("")){
				Class outModelClass = Class.forName(outModelClassName);
				outModel = (CommandDataModel)outModelClass.newInstance();		
			}else{
				outModel = dataModel;
			}
	
			// Resutl status	
			stmt.registerOutParameter(1,Types.INTEGER);
			
			if(outModel != null)
				setOutParameters(stmt,outModel,outputParameters,dbCallableAccessInfo.isNamedParameters());
			
			stmt.execute();
	
			Vector errors = new Vector();
			int status = -1;
			for(int i=0;i<configuration.getNumOfCallableExceptionIgnored();i++){
				try{
					status = stmt.getInt(1);
					break;
				}catch(SQLException sqle){
					errors.add(sqle);
				}
			}
			
			LOG.debug(thisClassName + ".executeCallableAccess: Callable access ["+storedName+"] executed. Return status is ["+status+"]");
			if(status < 0){
				String errorMsg = traceExceptions(errors,storedName,status);
				DAOException daoe = new DAOException(errorMsg);
				LOG.error(daoe);
				throw daoe;
			}
			
			DAOException daoe = DAOCallableResultBinder.mapStatus(status);
			String errorMsg = traceExceptions(errors,storedName,status);
			LOG.warning(errorMsg);
			if(daoe != null){
				if(daoe.getClass().equals(DAOException.class)){
					daoe.setDescr(errorMsg);
				}
				throw daoe;
			}
	
			if(outModel != null){
				DAOResultSet daoRs = new DAOResultSet(stmt);
				loadRow(daoRs,outModel,outputParameters,
						DAOCallableAccessInfo.getBooleanTrueValue(),
						DAOCallableAccessInfo.getBooleanTrueValue(),
					    2);
			}
	
			DAOCallableResultModel result = new DAOCallableResultModel();
			result.setOutputCommandDataModel(outModel);
			result.setResult(status);
			result.setMiddleTierInputParameters(middleTierInputParameters);
			return result;
				
		}catch(SQLException sqle){
			
			DAOException daoSqle = SQLExceptionBinder.mapException(sqle);
			String errorMsg = thisClassName + ".executeCallableAccess: SQL exception ["+daoSqle+"] executing callable access: ["+storedName+"] ";
			daoSqle.setDescr(errorMsg);
			LOG.error(daoSqle);
			throw daoSqle;
			
		}catch(Exception e){
			
			String errorMsg = thisClassName + ".executeCallableAccess: Exception ["+e+"] executing callable access: ["+storedName+"] ";
			DAOException daoe = new DAOException(errorMsg);
			LOG.error(daoe);
			throw daoe;
			
		}finally{
			
			try{
				if(rs != null){rs.close();rs = null;}
				if(stmt != null){stmt.close();stmt = null;}
			}catch(Exception e){
				String errorMsg = thisClassName+".executeCallableAccess: Exception ["+e+"] in closing ResultSet and/or Statement";
				DAOException daoe = new DAOException(errorMsg);
				LOG.error(daoe);
				throw daoe;
			}
			
		}
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	private void setOutParameters(CallableStatement ps,
							      CommandDataModel model,
							      DAOAccessParameters parameters,
							      boolean namedParameters) throws Exception {
	
		if(parameters == null || model == null)
			return;
			 
	    try {
	
		    int offset = 2;
	        for (int i = 0; i < parameters.getParametersCount(); i++) {
	
		        DAOAccessParameter parameter = parameters.getParameter(i);
		        if(parameter == null || 
			       parameter.getPropertyName() == null ||
			       parameter.getPropertyName().equals(""))
		        	continue;
		        	
		        String propName = parameter.getPropertyName();
	            Class parType = Tools.getPropertyType(model,propName);
	            if(parType == null){
					LOG.debug(thisClassName + ".setOutParameters: Property type for ["+propName+"] is null");
	            }
	            
				Integer sqlType = null;
	            if (parType.equals(StringType.class)) {
	
		            sqlType = new Integer(java.sql.Types.CHAR);
	
	            } else if (parType.equals(BooleanType.class)) {
	
		            sqlType = new Integer(java.sql.Types.CHAR);
		                
	            } else if (parType.equals(IntegerType.class)) {
	
		            sqlType = new Integer(java.sql.Types.DOUBLE);
	
	            } else if (parType.equals(DoubleType.class)) {
	
		            sqlType = new Integer(java.sql.Types.DOUBLE);
	
	            } else if (parType.equals(DateType.class)) {
	
		            sqlType = new Integer(java.sql.Types.DATE);
	
	            } else if (parType.equals(TimestampType.class)) {
	
		            sqlType = new Integer(java.sql.Types.TIMESTAMP);
	
	            } else if (parType.equals(ByteArrayType.class)) {
	            	
		            sqlType = new Integer(java.sql.Types.BINARY);
	
	            } else if (parType.equals(FileType.class)) {
	            	
	            	String fileTypeDataPart = parameter.getFileTypeDataPart();
	            	if(fileTypeDataPart == null){
			            sqlType = new Integer(java.sql.Types.BINARY);
	            	}else if(fileTypeDataPart.equals(DAOAccessParameter.FILETYPE_IMAGE_DATA)){
			            sqlType = new Integer(java.sql.Types.BINARY);
	            	}else if(fileTypeDataPart.equals(DAOAccessParameter.FILETYPE_CONTENTTYPE_DATA)){
			            sqlType = new Integer(java.sql.Types.CHAR);
	            	}else if(fileTypeDataPart.equals(DAOAccessParameter.FILETYPE_IMAGE_IS_VIRUS_EXAMINED)){
			            sqlType = new Integer(java.sql.Types.CHAR);
	            	}else if(fileTypeDataPart.equals(DAOAccessParameter.FILETYPE_FILENAME_DATA)){
			            sqlType = new Integer(java.sql.Types.CHAR);
	            	}
	
	            } else {
	
	             	String errorMsg = thisClassName + ".setOutParameters: ATTENTION !!!!! THE TYPE " +
	             	                  parType + " IS NOT MANAGED !!!!!";
	                Exception e = new Exception(errorMsg);
	                LOG.error(e);
	                throw e;
	
	            }
	
	            if(sqlType != null){
	            	LOG.debug(thisClassName + ".setOutParameters: Setting callable parameter number " + (i + offset) + " to type " + sqlType);
	            	if(namedParameters)
	            		ps.registerOutParameter(propName, sqlType.intValue());
	            	else
	            		ps.registerOutParameter(i + offset, sqlType.intValue());
	            }
	        }
	        
	    } catch (Exception e) {
		    
	        String errorMsg = thisClassName + ".setParameters: Exception ["+e+"] in setting callable parameter for model ["+model.getClass()+"]";
	        e = new Exception(errorMsg);
	        LOG.error(e);
	        throw e;
	       
	    }
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	private String traceExceptions(Vector errors,String storedName, int status){
		String errorMsg = thisClassName + ".executeCallableAccess: Result status ["+status+"] executing callable access: ["+storedName+"] ";
		for(int i=0;i<errors.size();i++){
			errorMsg += "--> Exception number ["+(i+1)+"]: "+((SQLException)errors.get(i)).toString()+"\n";
		}
		return errorMsg;
	}
}
