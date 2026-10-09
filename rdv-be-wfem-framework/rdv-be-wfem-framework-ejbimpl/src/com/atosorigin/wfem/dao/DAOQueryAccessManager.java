package com.atosorigin.wfem.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.text.ParseException;
import java.util.Vector;

import oracle.jdbc.OracleTypes;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.dao.exceptions.SQLExceptionBinder;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.ListType;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
class DAOQueryAccessManager extends DAOAccessManager {
		
	private static final String thisClassName = DAOQueryAccessManager.class.getName();
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	protected DAOQueryResultModel doAccess(ClientSessionContext csc, Connection dbConnection,
										   DAOObject dao,			
				  				 		   DAOQueryAccessInfo dbQueryAccessInfo,
				  				 		   CommandDataModel inputParametersDataModel) throws DAOException {
								
		DAOQueryResultModel result = null;
		int queryType = dbQueryAccessInfo.getQueryType();
		switch(queryType){
			case DAOQueryAccessInfo.NORMAL_QUERY:
				result = executeNormalQuery(csc,dbConnection,
										    inputParametersDataModel,
										    dao,dbQueryAccessInfo);
				break;
			case DAOQueryAccessInfo.PARSED_QUERY:
				result = executeParsedQuery(csc,dbConnection,
											inputParametersDataModel,
											dao,dbQueryAccessInfo);
				break;
			case DAOQueryAccessInfo.STORED_QUERY:
				result = executeStoredQuery(csc,dbConnection,
											inputParametersDataModel,
											dao,dbQueryAccessInfo);
				break;
			default:
				String errorMsg = thisClassName + ".doAccess: Type ["+queryType+"] of referenced query access ["+dbQueryAccessInfo.getDaoAccessName()+"] is not correct";
				DAOException daoe = new DAOException(errorMsg);
				LOG.error(daoe);
				throw daoe;
		}

		return result;
		
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	private DAOQueryResultModel executeNormalQuery(ClientSessionContext csc, Connection dbConnection,
									               CommandDataModel dataModel,
									               DAOObject dao,
							   		               DAOQueryAccessInfo dbQueryAccessInfo) throws DAOException{
	
		String query = dbQueryAccessInfo.getSqlString();
		DAOAccessParameters inputParameters = dbQueryAccessInfo.getInputParameters();

		LOG.debug(thisClassName + ".executeNormalQuery: Executing normal query ["+query+"]");
		
	    return executeQuery(csc,dbConnection,dataModel,dao,dbQueryAccessInfo,inputParameters,query,"executeNormalQuery",dbQueryAccessInfo.isUseCallable());
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	private DAOQueryResultModel executeParsedQuery(ClientSessionContext csc, Connection dbConnection,
												   CommandDataModel dataModel,
												   DAOObject dao,
							   					   DAOQueryAccessInfo dbQueryAccessInfo) throws DAOException{

		String query = null;
		DAOAccessParameters inputParameters = null;
		try {
			
			DAOParsedQueryParser parser = DAOParsedQueryParser.parse(csc,dbQueryAccessInfo,dataModel);
			query = parser.getSqlString();
			inputParameters = parser.getAccessParameters();
			
		}catch(ParseException pe) {
			DAOException daoe = new DAOException(thisClassName+".executeParsedQuery: exception parsing query: ["+pe.getMessage()+"]");
			LOG.error(daoe);
			throw daoe;
		}

		LOG.debug(thisClassName + ".executeParsedQuery: Executing parsed query ["+query+"]");

		if(dataModel == null)
			dataModel = new DAOMokeModel();
	    return executeQuery(csc,dbConnection,dataModel,dao,dbQueryAccessInfo,inputParameters,query,"executeParsedQuery",dbQueryAccessInfo.isUseCallable());
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	private DAOQueryResultModel executeStoredQuery(ClientSessionContext csc, Connection dbConnection,
												   CommandDataModel dataModel,
												   DAOObject dao,
							   					   DAOQueryAccessInfo dbQueryAccessInfo) throws DAOException{
	
		boolean useCallable = false;
		try{
			if(dbConnection.getMetaData().getDatabaseProductName().toLowerCase().indexOf("oracle") >= 0)
				useCallable = true;
		}catch(Throwable t){
			t.printStackTrace();
		}
		
		String storedProcedureName = dbQueryAccessInfo.getSqlString();		
		storedProcedureName = getRealStoredName(storedProcedureName,dataModel);
		
		String query = useCallable ? "{ call ? := "+storedProcedureName+"(" : "exec "+storedProcedureName;
		
		DAOAccessParameters inputParameters = dbQueryAccessInfo.getInputParameters();
		if(inputParameters != null){
			for(int i=0;i<inputParameters.getParametersCount();i++){
				query += " ?";
				if(i < (inputParameters.getParametersCount()-1))
				   query += ",";
			}
	    }
		if(useCallable)
			query += ") }";
	    
		LOG.debug(thisClassName + ".executeStoredQuery: Executing stored query ["+query+"]");
		
	    return executeQuery(csc,dbConnection,dataModel,dao,dbQueryAccessInfo,inputParameters,query,"executeStoredQuery",useCallable);
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	private DAOQueryResultModel executeQuery(ClientSessionContext csc, Connection dbConnection,
										      CommandDataModel dataModel,
										      DAOObject dao,
										      DAOQueryAccessInfo dbQueryAccessInfo,
							   				  DAOAccessParameters inputParameters,
							   				  String query,
							   				  String funcName,
							   				  boolean useCallable) throws DAOException{
	
		if(!dao.fetchableQuery)
			return executeFetchedQuery(csc, dbConnection, dataModel, dao, dbQueryAccessInfo, inputParameters, query, funcName, useCallable);
		else
			return executeFetchableQuery(csc, dbConnection, dataModel, dao, dbQueryAccessInfo, inputParameters, query, funcName, useCallable);
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	private DAOQueryResultModel executeFetchedQuery(ClientSessionContext csc, Connection dbConnection,
													CommandDataModel dataModel,
													DAOObject dao,
													DAOQueryAccessInfo dbQueryAccessInfo,
													DAOAccessParameters inputParameters,
													String query,
													String funcName,
													boolean useCallable) throws DAOException{
	
		DAOQueryResultModel result = new DAOQueryResultModel();
		ResultSet rs = null;
		PreparedStatement ps = null;
		
		try{
			
			query = query.replaceAll("<!--(.+)-->","");
			LOG.debug(thisClassName + ".executeQuery: Preparing SQL statement ["+query+"]");
			int parsOffset = 1;
			if(useCallable){
				if("executeStoredQuery".equals(funcName))
					parsOffset = 2;
				ps = dbConnection.prepareCall(query);
			}else{
				ps = dbConnection.prepareStatement(query);
			}
			
			int maxRows = dao.queryMaxResultRows;
			if(maxRows <= 0)
				maxRows = dbQueryAccessInfo.getMaxResultRows();
			if(maxRows > 0){
				ps.setMaxRows(maxRows+1);
				LOG.debug(thisClassName + "."+funcName+": Max result rows setted to: "+maxRows);
			}
			dao.queryMaxResultRows = -1;
	
			StringBuffer middleTierInputParameters = setPsParameters(csc,ps,dataModel,inputParameters,dbQueryAccessInfo,parsOffset,false);
	
			int cursorIndex = 1;
			if(useCallable){
				if(inputParameters != null && !"executeStoredQuery".equals(funcName))
					cursorIndex = inputParameters.getParametersCount()+1;
				((CallableStatement)ps).registerOutParameter(cursorIndex, OracleTypes.CURSOR);
			}
			
			// Execute SQL			
			ps.execute();
			do{
				rs = useCallable ? (ResultSet)((CallableStatement)ps).getObject(cursorIndex) : ps.getResultSet();
				if(rs != null)
					result = processResultset(rs,maxRows,dbQueryAccessInfo,dataModel,funcName,dbQueryAccessInfo.getOutputParameters());
			}while(ps.getMoreResults() || ps.getUpdateCount() != -1);
			result.setMiddleTierInputParameters(middleTierInputParameters);
			return result;
			
		}catch(SQLException sqle){
			
			DAOException daoSqle = SQLExceptionBinder.mapException(sqle);
			String errorMsg = thisClassName + "."+funcName+": SQL exception ["+daoSqle+"] executing query: ["+query+"] ";
			daoSqle.setDescr(errorMsg);
			LOG.error(daoSqle);
			throw daoSqle;
			
		}catch(Exception e){
			
			String errorMsg = thisClassName + "."+funcName+": Exception ["+e+"] executing query: ["+query+"] ";
			DAOException daoe = new DAOException(errorMsg);
			LOG.error(daoe);
			throw daoe;
			
		}finally{
			
			try{
				if(rs != null){rs.close();rs = null;}
				if(ps != null){ps.close();ps = null;}
				
				afterQueryCallback(dbConnection, dataModel, dbQueryAccessInfo);
				
			}catch(Exception e){
				String errorMsg = thisClassName+".executeStoredQuery: Exception ["+e+"] in closing ResultSet and/or PreparedStatement";
				DAOException daoe = new DAOException(errorMsg);
				LOG.error(daoe);
				throw daoe;
			}
			
		}
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	private DAOQueryResultModel processResultset(ResultSet rs, int maxRows,DAOQueryAccessInfo dbQueryAccessInfo,
												 CommandDataModel dataModel, String funcName, DAOAccessParameters outputParameters) throws SQLException, Exception{
		
		Vector elements = new Vector();
		DAOQueryResultModel result = new DAOQueryResultModel();
		DAOResultSet daoRs = new DAOResultSet(rs);
		
		// Load columns
		int numCols = daoRs.getColumnCount();
		String[] colNames = new String[numCols];
		String[] colLabels = new String[numCols];
		for(int i=0;i<numCols;i++){
			colNames[i] = daoRs.getColumnName(i+1);
			colLabels[i] = daoRs.getColumnLabel(i+1);
		}
		
		// If dynamic model as output model and no parameters load them as db columns
		Class outputModelClass = null;
		if(dbQueryAccessInfo.getOutputDataModelClassName() == null ||
		   dbQueryAccessInfo.getOutputDataModelClassName().equals("")){
			if(dataModel != null)
				outputModelClass = dataModel.getClass();
		}else{
			outputModelClass = Class.forName(dbQueryAccessInfo.getOutputDataModelClassName());
		}
		
		if(outputModelClass != null && (MapCommandDataModel.class.isAssignableFrom(outputModelClass)) &&
		   (outputParameters == null || outputParameters.getParametersCount() == 0)){
			outputParameters = new DAOAccessParameters();
			for(int i=0;i<colNames.length;i++){
				DAOAccessParameter parameter = new DAOAccessParameter();
				parameter.setPropertyName(colNames[i]);
				outputParameters.addParameter(parameter);
			}
		}
		
		String[] propertyNames = null;
		if(outputParameters != null){
			int numOutPars = outputParameters.getParametersCount();
			propertyNames = new String[numOutPars];
			for(int i=0;i<numOutPars;i++){			
				DAOAccessParameter parameter = outputParameters.getParameter(i);
				propertyNames[i] = parameter.getPropertyName();
			}			
		}else{
			propertyNames = new String[numCols];
			for(int i=0;i<numCols;i++)			
				propertyNames[i] = colNames[i];
		}
		
		int count = 0;
		boolean maxRowsExceeded = false;
		
		while(rs.next()){
			
			if(maxRows > 0 && count == maxRows){
				maxRowsExceeded = true;
				break;
			}
								
			if(dbQueryAccessInfo.getOutputDataModelClassName() == null ||
			   dbQueryAccessInfo.getOutputDataModelClassName().equals("")){
	
				LOG.debug(thisClassName + "."+funcName+": query has no outputCommandDataModel: first row is loaded into inputDataModel ["+dataModel+"]");
	
				CommandDataModel cdm = null;					
				if(count == 0)
					cdm = dataModel;
				else
					cdm = (CommandDataModel)dataModel.getClass().newInstance();
				loadRow(daoRs,cdm,outputParameters,
						DAOQueryAccessInfo.getBooleanTrueValue(),
						DAOQueryAccessInfo.getBooleanTrueValue(),1);
				elements.addElement(cdm);
				count++;
				continue;
			}
			
			Object output = createRow(rs,dbQueryAccessInfo.getOutputDataModelClassName(),
									  outputParameters,
									  DAOQueryAccessInfo.getBooleanTrueValue(),
									  DAOQueryAccessInfo.getBooleanTrueValue());
											      
			if(output instanceof CommandDataModel){
				elements.addElement(output);
			}else if(output instanceof AbstractType){
				result = new DAOQueryResultModel();
				result.setSingleResult((AbstractType)output);
				return result;
			}
			count++;
		}
		
		if(dbQueryAccessInfo.getOutputDataModelClassName() == null ||
		   dbQueryAccessInfo.getOutputDataModelClassName().equals("")){
			Class contentListClass = dataModel.getClass();
			result = new DAOQueryResultModel();
			ListType resultListType = new ListType(contentListClass,elements);
			resultListType.setColumnNames(colNames);
			resultListType.setColumnLabels(colLabels);
			resultListType.setPropertyNames(propertyNames);
			result.setResult(resultListType);	
			return result;
		}
	
		Class contentListClass = Class.forName(dbQueryAccessInfo.getOutputDataModelClassName());	
		result = new DAOQueryResultModel();
		ListType resultListType = new ListType(contentListClass,elements);
		resultListType.setColumnNames(colNames);
		resultListType.setColumnLabels(colLabels);
		resultListType.setPropertyNames(propertyNames);
		result.setResult(resultListType);	
		result.setMaxRowsExceeded(maxRowsExceeded);
		result.getResult().setMaxRowsExceeded(maxRowsExceeded);
		result.setMaxRows(maxRows);
		return result;
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	private DAOQueryResultModel executeFetchableQuery(ClientSessionContext csc, Connection dbConnection,
										      		  CommandDataModel dataModel,
										      		  DAOObject dao,
										      		  DAOQueryAccessInfo dbQueryAccessInfo,
										      		  DAOAccessParameters inputParameters,
										      		  String query,
										      		  String funcName,
										      		  boolean useCallable) throws DAOException{
	
		PreparedStatement ps = null;
		
		try{
			
			query = query.replaceAll("<!--(.+)-->","");
			LOG.debug(thisClassName + ".executeQuery: Preparing SQL statement ["+query+"]");
			int parsOffset = 1;
			if(useCallable){
				if("executeStoredQuery".equals(funcName))
					parsOffset = 2;
				ps = dbConnection.prepareCall(query);
			}else{
				ps = dbConnection.prepareStatement(query);
			}
	
			int maxRows = dao.queryMaxResultRows;
			if(maxRows <= 0)
				maxRows = dbQueryAccessInfo.getMaxResultRows();
			if(maxRows > 0){
				ps.setMaxRows(maxRows+1);
				LOG.debug(thisClassName + "."+funcName+": Max result rows setted to: "+maxRows);
			}
			dao.queryMaxResultRows = -1;
	
			StringBuffer middleTierInputParameters = setPsParameters(csc,ps,dataModel,inputParameters,dbQueryAccessInfo,parsOffset,false);

			int cursorIndex = 1;
			if(useCallable){
				if(inputParameters != null && !"executeStoredQuery".equals(funcName))
					cursorIndex = inputParameters.getParametersCount()+1;
				((CallableStatement)ps).registerOutParameter(cursorIndex, OracleTypes.CURSOR);
			}
			
			DAOQueryResultModel res = startProcessResultset(dbConnection,ps,dbQueryAccessInfo,dataModel,funcName,query,dbQueryAccessInfo.getOutputParameters(),dao,useCallable,cursorIndex);
			res.setMiddleTierInputParameters(middleTierInputParameters);
			return res;
			
		}catch(SQLException sqle){
			
			DAOException daoSqle = SQLExceptionBinder.mapException(sqle);
			String errorMsg = thisClassName + "."+funcName+": SQL exception ["+daoSqle+"] executing query: ["+query+"] ";
			daoSqle.setDescr(errorMsg);
			LOG.error(daoSqle);
			throw daoSqle;
			
		}catch(Exception e){
			
			String errorMsg = thisClassName + "."+funcName+": Exception ["+e+"] executing query: ["+query+"] ";
			DAOException daoe = new DAOException(errorMsg);
			LOG.error(daoe);
			throw daoe;
			
		}
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	private DAOQueryResultModel startProcessResultset(Connection dbConnection, PreparedStatement ps, DAOQueryAccessInfo dbQueryAccessInfo,
			 										  CommandDataModel dataModel, String funcName, String query,
			 										  DAOAccessParameters outputParameters, DAOObject dao,
			 										  boolean useCallable, int cursorIndex) throws SQLException, Exception{
		
		
		// Execute SQL			
		ps.execute();
		ResultSet rs = null;
		do{
			rs = useCallable ? (ResultSet)((CallableStatement)ps).getObject(cursorIndex) : ps.getResultSet();
			if(rs != null)
				break;
		}while(ps.getMoreResults() || ps.getUpdateCount() != -1);
				
		DAOQueryResultModel result = new DAOQueryResultModel();
		DAOResultSet daoRs = new DAOResultSet(rs);
		daoRs.dbConnection = dbConnection;
		daoRs.funcName = funcName;
		daoRs.dbQueryAccessInfo = dbQueryAccessInfo;
		daoRs.dataModel = dataModel;

		result.dbConnection = dbConnection;
		result.funcName = funcName;
		result.query = query;
		result.dbQueryAccessInfo = dbQueryAccessInfo;
		result.dataModel = dataModel;

		dao.ps = ps;
		dao.daoRs = daoRs;

		result.dao = dao;
		
		// Load columns
		int numCols = daoRs.getColumnCount();
		String[] colNames = new String[numCols];
		String[] colLabels = new String[numCols];
		for(int i=0;i<numCols;i++){
			colNames[i] = daoRs.getColumnName(i+1);
			colLabels[i] = daoRs.getColumnLabel(i+1);
		}
		
		// If dynamic model as output model and no parameters load them as db columns
		Class outputModelClass = null;
		if(dbQueryAccessInfo.getOutputDataModelClassName() == null ||
		   dbQueryAccessInfo.getOutputDataModelClassName().equals("")){
			if(dataModel != null)
				outputModelClass = dataModel.getClass();
		}else{
			outputModelClass = Class.forName(dbQueryAccessInfo.getOutputDataModelClassName());
		}
		
		if(outputModelClass != null && (MapCommandDataModel.class.isAssignableFrom(outputModelClass)) &&
		   (outputParameters == null || outputParameters.getParametersCount() == 0)){
			outputParameters = new DAOAccessParameters();
			for(int i=0;i<colNames.length;i++){
				DAOAccessParameter parameter = new DAOAccessParameter();
				parameter.setPropertyName(colNames[i]);
				outputParameters.addParameter(parameter);
			}
		}
		
		String[] propertyNames = null;
		if(outputParameters != null){
			int numOutPars = outputParameters.getParametersCount();
			propertyNames = new String[numOutPars];
			for(int i=0;i<numOutPars;i++){			
				DAOAccessParameter parameter = outputParameters.getParameter(i);
				propertyNames[i] = parameter.getPropertyName();
			}			
		}else{
			propertyNames = new String[numCols];
			for(int i=0;i<numCols;i++)			
				propertyNames[i] = colNames[i];
		}
		
		if(dbQueryAccessInfo.getOutputDataModelClassName() == null ||
		   dbQueryAccessInfo.getOutputDataModelClassName().equals("")){
			Class contentListClass = dataModel.getClass();
			ListType resultListType = new ListType(contentListClass);
			resultListType.setColumnNames(colNames);
			resultListType.setColumnLabels(colLabels);
			resultListType.setPropertyNames(propertyNames);
			result.setResult(resultListType);	
		}else{
			Class contentListClass = Class.forName(dbQueryAccessInfo.getOutputDataModelClassName());	
			ListType resultListType = new ListType(contentListClass);
			resultListType.setColumnNames(colNames);
			resultListType.setColumnLabels(colLabels);
			resultListType.setPropertyNames(propertyNames);
			result.setResult(resultListType);	
		}

		result.outputParameters = outputParameters;
		
		return result;
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	public CommandDataModel fetchQuery(DAOQueryResultModel result) throws DAOException{
		
		try{
			if(!result.dao.daoRs.getResultSet().next()){
				try{
					if(result.dao.daoRs.getResultSet() != null){result.dao.daoRs.getResultSet().close();result.dao.daoRs = null;}
					if(result.dao.ps != null){result.dao.ps.close();result.dao.ps = null;}
					
					afterQueryCallback(result.dbConnection, result.dataModel, result.dbQueryAccessInfo);
					
				}catch(Exception e){
					String errorMsg = thisClassName+".executeStoredQuery: Exception ["+e+"] in closing ResultSet and/or PreparedStatement";
					DAOException daoe = new DAOException(errorMsg);
					LOG.error(daoe);
					throw daoe;
				}
				return null;
			}
			
			if(result.dbQueryAccessInfo.getOutputDataModelClassName() == null ||
			   result.dbQueryAccessInfo.getOutputDataModelClassName().equals("")){
	
				LOG.debug(thisClassName + ".fetchQuery: query has no outputCommandDataModel: first row is loaded into inputDataModel ["+result.dataModel+"]");
	
				loadRow(result.dao.daoRs,result.dataModel,result.outputParameters,
						DAOQueryAccessInfo.getBooleanTrueValue(),
						DAOQueryAccessInfo.getBooleanTrueValue(),1);
				return result.dataModel;
			}
			
			Object output = createRow(result.dao.daoRs.getResultSet(),result.dbQueryAccessInfo.getOutputDataModelClassName(),
									  result.outputParameters,
									  DAOQueryAccessInfo.getBooleanTrueValue(),
									  DAOQueryAccessInfo.getBooleanTrueValue());
			return (CommandDataModel)output;
			
		}catch(SQLException sqle){
			
			DAOException daoSqle = SQLExceptionBinder.mapException(sqle);
			String errorMsg = thisClassName + "."+result.funcName+": SQL exception ["+daoSqle+"] executing fetch for query: ["+result.query+"] ";
			daoSqle.setDescr(errorMsg);
			LOG.error(daoSqle);
			throw daoSqle;
			
		}catch(Exception e){
			
			String errorMsg = thisClassName + "."+result.funcName+": Exception ["+e+"] executing fetch for query: ["+result.query+"] ";
			DAOException daoe = new DAOException(errorMsg);
			LOG.error(daoe);
			throw daoe;
			
		}
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	protected static void afterQueryCallback(Connection dbConnection,  CommandDataModel dataModel,
										  	 DAOQueryAccessInfo dbQueryAccessInfo){
		Boolean isAutocommit = null;
		try{
		
			if(dbQueryAccessInfo.getStoredToCallAfterQuery() != null && dbQueryAccessInfo.getStoredToCallAfterQuery().length() > 0){
				
				try{
					boolean useCallable = false;
					if(dbConnection.getMetaData().getDatabaseProductName().toLowerCase().indexOf("oracle") >= 0)
						useCallable = true;
					PreparedStatement ps = null;
					try{
						String storedProcedureName = dbQueryAccessInfo.getStoredToCallAfterQuery();		
						storedProcedureName = getRealStoredName(storedProcedureName,dataModel);
						LOG.debug(thisClassName + ".afterQueryCallback: Executing query callback function ["+storedProcedureName+"]");
						if(useCallable){
							ps = dbConnection.prepareCall("{ call ? := "+storedProcedureName+"() }");
							((CallableStatement)ps).registerOutParameter(1, Types.INTEGER);
						}else{
							ps = dbConnection.prepareStatement("exec "+storedProcedureName);
						}
						ps.execute();
					}catch(Throwable dt){
						LOG.warning(thisClassName + ".afterQueryCallback: Error executing query callback stored ["+dt.getMessage()+"]");
					}finally{
						if(ps != null){ ps.close(); ps = null; }
					}
					
				}catch(Throwable t){
					LOG.warning(thisClassName + ".afterQueryCallback: error reading dbms type");
					return;
				}
				
			}				
				
			if(dbQueryAccessInfo.getTablesToDeleteAfterQuery() != null && dbQueryAccessInfo.getTablesToDeleteAfterQuery().length() > 0){
				try{
					isAutocommit = new Boolean(dbConnection.getAutoCommit());
					if(!isAutocommit)
						dbConnection.setAutoCommit(true);
					else
						isAutocommit = null;
				}catch(Throwable t){
					LOG.warning(thisClassName + ".afterQueryCallback: error reading/setting auto-commit state");
					isAutocommit = null;
				}
				
				LOG.debug(thisClassName + ".afterQueryCallback: Deleting tables ["+dbQueryAccessInfo.getTablesToDeleteAfterQuery()+"]");
				String[] tablesToDelete = dbQueryAccessInfo.getTablesToDeleteAfterQuery().split(",");
				for(int i=0;i<tablesToDelete.length;i++){
					PreparedStatement ps = null;
					try{
						ps = dbConnection.prepareStatement("delete from "+tablesToDelete[i]);
						ps.executeUpdate();
					}catch(Throwable dt){
						LOG.warning(thisClassName + ".afterQueryCallback: error deleting table "+tablesToDelete[i]+" ["+dt.getMessage()+"]");
					}finally{
						if(ps != null){ ps.close(); ps = null; }
					}
				}
			}
				
		}catch(Throwable t){
			LOG.warning(thisClassName + ".afterQueryCallback: generic error ["+t.getMessage()+"]");
			return;
		}finally{
			try{ if(isAutocommit != null){ dbConnection.setAutoCommit(isAutocommit.booleanValue()); } }catch(Throwable t){}
		}
	}
	
}
