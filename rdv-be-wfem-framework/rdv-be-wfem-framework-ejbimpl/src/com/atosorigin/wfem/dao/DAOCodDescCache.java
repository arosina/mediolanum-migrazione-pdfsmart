package com.atosorigin.wfem.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Vector;

import com.atosorigin.wfem.coddesc.CodDescData;
import com.atosorigin.wfem.coddesc.CodDescDataList;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.controller.Configuration;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.loggers.DAOLogger;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;
import com.atosorigin.wfem.util.RefreshNotifier;
import com.atosorigin.wfem.util.RefreshableCache;
import com.atosorigin.wfem.util.RefreshableCacheContainer;
import com.atosorigin.wfem.util.RefreshableCacheIntf;
import com.atosorigin.wfem.util.RefreshableSerializedCache;
import com.atosorigin.wfem.util.Tools;

/********************************************************************************
/********************************************************************************/
class DAOCodDescCache implements RefreshableCacheContainer{

    private static DAOCodDescCache singleton;
	private static DAOLogger LOG = DAOLogger.getInstance();
    private RefreshableCacheIntf cachedCodDescLists = null;

	/********************************************************************************
	/********************************************************************************/
	private DAOCodDescCache() {
		super();
		
		if(Configuration.getInstance().isDaoCodDescCacheSerialized())
			cachedCodDescLists = new RefreshableSerializedCache();
		else
			cachedCodDescLists = new RefreshableCache();
		
		if(Configuration.getInstance().isDaoCacheEnabled()){
			int scanTime = Configuration.getInstance().getDaoCodDescCacheScanTime();
	        RefreshNotifier.addCache(this,cachedCodDescLists,scanTime);
		}
	}
	
	/********************************************************************************
	/********************************************************************************/
	protected CodDescDataList getCodDescDataList(Connection currentConnection,
												  ClientSessionContext clientContext,
												  DAOObjectInfo daoObjectInfo,
											 	  DAOCodDescLoadInfo daoCodDescLoadInfo,
											 	  CommandDataModel model) {
												 	 	
    	Connection dbConnection = null;
    	String identifier = ""; 
    	if(clientContext.getCountryCode() != null)
    		identifier += "_"+clientContext.getCountryCode();
    	if(clientContext.getChannelCode() != null)
    		identifier += "_"+clientContext.getChannelCode();
	    String codDescName = daoObjectInfo.getDaoObjectName() + "." + daoCodDescLoadInfo.getCodDescName() + identifier;
	    CodDescDataList dataList = (CodDescDataList)cachedCodDescLists.get(codDescName);
	    if(dataList == null){
	    	
	    	try{
				String dataSourceRefName = daoCodDescLoadInfo.getDataSourceReferenceName();
				if(dataSourceRefName != null){
					currentConnection = null;
			    	dbConnection = DAOConnectionManager.getInstance().getConnection(clientContext,dataSourceRefName);
				}else{	    		
			    	if(currentConnection == null){
						dataSourceRefName = daoObjectInfo.getDataSourceReferenceName();
				    	dbConnection = DAOConnectionManager.getInstance().getConnection(clientContext,dataSourceRefName);
			    	}else{
			    		dbConnection = currentConnection;
			    	}
				}
			    	
				dataList = loadCodDescDataList(dbConnection,daoCodDescLoadInfo,model);
				if(dataList == null){
					String errorMsg = getClass() + ".getCodDescDataList: Referenced DaoCodDesc ["+codDescName+"] not found";
					java.sql.SQLException sqle = new java.sql.SQLException(errorMsg);
					LOG.error(sqle);
					return new CodDescDataList();
				}
				
				if(Configuration.getInstance().isDaoCacheEnabled()){
					LOG.debug(getClass()+".getCodDescDataList: caching data list for DaoCodDesc ["+codDescName+"]");
					cachedCodDescLists.put(codDescName,dataList);
				}
				
	    	}catch(DAOException daoe){
				String errorMsg = getClass() + ".getCodDescDataList: DAO Exception ["+daoe+"] in getting data list for DaoCodDesc ["+codDescName+"]";
				daoe.setDescr(errorMsg);
				LOG.error(daoe);
				return new CodDescDataList();
	    	}catch(Exception e){
				String errorMsg = getClass() + ".getCodDescDataList: Exception ["+e+"] in getting data list for DaoCodDesc ["+codDescName+"]";
				e = new Exception(errorMsg);
				LOG.error(e);
				return new CodDescDataList();
	    	}finally{
	    		if(currentConnection == null)
	    			DAOConnectionManager.getInstance().closeConnection(dbConnection);
	    	}
	    }else{
			LOG.debug(getClass()+".getCodDescDataList: data list for DaoCodDesc ["+codDescName+"] founded in cache");
		}
	    return dataList;
	}
	
	/********************************************************************************
	/********************************************************************************/
	protected static DAOCodDescCache getInstance() {
	    if (singleton == null) {
	        synchronized (DAOCodDescCache.class) {
	            if (singleton == null) {
	                singleton = new DAOCodDescCache();
	            }
	        }
	    }
		return singleton;
	}
	
	/********************************************************************************
	/********************************************************************************/
	private CodDescDataList loadCodDescDataList(Connection dbConnection, 
											 	 DAOCodDescLoadInfo daoCodDescLoadInfo,
											 	 CommandDataModel model){
												 	 	
	    PreparedStatement ps = null;
	    ResultSet rs = null;
	    
	    try{
			  CodDescDataList dataList = new CodDescDataList();
			  
			  dataList.setRefreshTime(daoCodDescLoadInfo.getRefreshTime());
			  LOG.debug(getClass()+".loadCodDescDataList: data list refresh time for DaoCodDesc ["+daoCodDescLoadInfo.getCodDescName()+"] setted to ["+daoCodDescLoadInfo.getRefreshTime()+"] seconds");
		      
			  String tableName      = daoCodDescLoadInfo.getTableName();
			  String dbCod          = daoCodDescLoadInfo.getCodColumnName();
			  String dbAliasCod     = daoCodDescLoadInfo.getCodAliasColumnName();
			  String dbShortDesc    = daoCodDescLoadInfo.getShortDescrColumnName();
			  String dbDesc         = daoCodDescLoadInfo.getDescrColumnName();
			  String dbAliasDesc    = daoCodDescLoadInfo.getDescrAliasColumnName();
			  String whereCondition = daoCodDescLoadInfo.getWhereCondition();
			  String validityWhere  = daoCodDescLoadInfo.getValidityWhereCondition();
			  String orderBy		= daoCodDescLoadInfo.getOrderBy();
	
		      String selectString = "select ";
		      
		      if(daoCodDescLoadInfo.isDistinct())
		    	  selectString += "distinct ";
		      
		      if(dbAliasCod == null){
		    	  if(daoCodDescLoadInfo.isDistinct())
		    		  selectString += "("+dbCod+")";
		    	  else
		    		  selectString += dbCod;
		      }else{
		    	  selectString += "("+dbCod+") "+dbAliasCod;
		      }
		      
		      if(dbShortDesc != null)
			      selectString += ","+dbShortDesc;
		      
		      if(dbDesc != null){
		    	  if(dbAliasDesc == null)
		    		  selectString += ","+dbDesc;
		    	  else
		    		  selectString += ",("+dbDesc+") "+dbAliasDesc;
		      }
		      
		      selectString += " from " + tableName;
		      
		      String queryString = selectString;
			  if(whereCondition != null && !whereCondition.equals(""))
			      queryString += " where " + whereCondition;
			  
			  if(orderBy != null)
			      queryString += " order by " + orderBy;
			  
			  Vector whereConditionFields = daoCodDescLoadInfo.getWhereConditionFields();			  
			  LOG.debug(getClass()+".loadCodDescDataList: executing DaoCodDesc query ["+queryString+"]");
		      ps = dbConnection.prepareStatement(queryString);
		  	  setPsParameters(ps,model,whereConditionFields);
		      rs = ps.executeQuery();
	
	          while (rs.next()) {
	          	
		          CodDescData data = new CodDescData();
		          
		          
		          int k = -1;
	        	  if(dbAliasCod == null)
		        	  dbCod = (k = dbCod.lastIndexOf(".")) != -1 ? dbCod.substring(k+1) : dbCod; /** managing table alias if occurs (STARA) **/
	        	  
	        	  Object oCod = null;
	        	  if(dbAliasCod == null)
	        		  oCod = rs.getObject(dbCod);
	        	  else
	        		  oCod = rs.getObject(dbAliasCod);
	        	  
		          data.setCod(oCod.toString().trim());
		          
		          Object oShortDesc = null;
		          if(dbShortDesc != null){
		        	  dbShortDesc = (k = dbShortDesc.lastIndexOf(".")) != -1 ? dbShortDesc.substring(k+1) : dbShortDesc; /** managing table alias if occurs (STARA) **/
			          oShortDesc = rs.getObject(dbShortDesc);
			          if(!rs.wasNull())
				          data.setShortDescr(oShortDesc.toString().trim());
				      else
				          data.setShortDescr("");
		          }
		          
		          if(dbDesc != null){
		        	  if(dbAliasDesc == null)
		        		  dbDesc = (k = dbDesc.lastIndexOf(".")) != -1 ? dbDesc.substring(k+1) : dbDesc; /** managing table alias if occurs (STARA) **/
		        		  
		        	  Object oDesc = null;
		        	  if(dbAliasDesc == null)
		        		  oDesc = rs.getObject(dbDesc);
		        	  else
		        		  oDesc = rs.getObject(dbAliasDesc);
		        	  
			          if(!rs.wasNull())
				          data.setDescr(oDesc.toString().trim());
				      else
				          data.setDescr("");
		          }
	
		          dataList.addCodDescData(data);
				  LOG.debug(getClass()+".loadCodDescDataList: inserted element ["+data.getCod()+"] ["+data.getDescr()+"] ["+data.getShortDescr()+"]");
		          
	          }
	          if(rs != null){ rs.close(); rs = null; }
	          if(ps != null){ ps.close(); ps = null; }
	
			  if(validityWhere != null && !validityWhere.equals("")){
				  
				  for(int j=0;j<dataList.getCodDescCount();j++){
					  CodDescData data = dataList.getCodDesc(j);
					  data.setValid(false);
				  }
				  
			      queryString = selectString + " where " + validityWhere;
	
				  Vector validityWhereConditionFields = daoCodDescLoadInfo.getValidityWhereConditionFields();				  	  
				  LOG.debug(getClass()+".loadCodDescDataList: executing validity DaoCodDesc query ["+queryString+"]");
			  	  ps = dbConnection.prepareStatement(queryString);
			  	  setPsParameters(ps,model,validityWhereConditionFields);
				  rs = ps.executeQuery();
	
		          while (rs.next()) {
			          Object oCod = rs.getObject(dbCod);
			          
			          CodDescData data = dataList.getCodDesc(oCod.toString().trim());
			          if(data != null){
					     data.setValid(true);
			          }
		          }
		          if(rs != null){ rs.close(); rs = null; }
	              if(ps != null){ ps.close(); ps = null; }
	
			  }
	           
		      return dataList;
		      
	    }catch(Exception e){
			String errorMsg = getClass() + ".loadCodDescDataList: Exception ["+e+"] loading code/descritpion values for ["+daoCodDescLoadInfo.getCodDescName()+"]";
			e = new Exception(errorMsg);
			LOG.error(e);
			return null;
	    }finally{
		    try{
		        if(rs != null){ rs.close(); rs = null; }
	            if(ps != null){ ps.close(); ps = null; }
		    }catch(SQLException sqle){
				String errorMsg = getClass() + ".loadCodDescDataList: Exception ["+sqle+"] in closing resultSet/statement for ["+daoCodDescLoadInfo.getCodDescName()+"]";
				sqle = new SQLException(errorMsg);
				LOG.error(sqle);
				return null;
		    }
	    }
	
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	private void setPsParameters(PreparedStatement ps,
							      CommandDataModel model,
							      Vector properties) throws Exception {
	
	    try {
			int offset = 1;
				
	        for (int i = 0; i < properties.size(); i++) {
	
		        String propName = (String)properties.get(i);
		        
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
				
		        Class propType = Tools.getPropertyType(model,propName);
		       	if(propType == null){
	             	String errorMsg = getClass() + ".setPsParameters: error in getting type for property ["+propName+"] in model ["+model+"]";
	                Exception e = new Exception(errorMsg);
	                LOG.error(e);
	                throw e;
		       	}
		       	
	            AbstractType par = (AbstractType)Tools.getPropertyValue(model,propName);
	            if(par != null && (startPercent || endPercent)){
	            	String parValue = par.toString();
		            if(startPercent)
		            	parValue = "%" + parValue;
		            if(endPercent)
	            		parValue = parValue + "%";

	            	par = AbstractType.newInstance(propType,parValue);
	            }
	
	            if (propType.equals(StringType.class)) {
	
		            if(par == null || par.isNull()){
			            ps.setNull(i + offset, Types.CHAR);
		            }else{
		                String value = ((StringType) par).toString();
		                ps.setString(i + offset, value);
		            }
	
	            } else if (propType.equals(IntegerType.class)) {
	
		            if(par == null || par.isNull()){
			            ps.setNull(i + offset, Types.INTEGER);
		            }else{
		                int value = ((IntegerType) par).intValue();
		                ps.setInt(i + offset, value);
		            }
	
	            } else if (propType.equals(DoubleType.class)) {
	
		            if(par == null || par.isNull()){
			            ps.setNull(i + offset, Types.DECIMAL);
		            }else{
		                double value = ((DoubleType) par).doubleValue();
		                ps.setDouble(i + offset, value);
		            }
	
	            } else if (propType.equals(DateType.class)) {
	
		            if(par == null || par.isNull()){
			            ps.setNull(i + offset, Types.DATE);
		            }else{
		                java.util.Date value = ((DateType) par).dateValue();
		                ps.setDate(i + offset, new java.sql.Date(((java.util.Date) value).getTime()));
		            }
	
	            } else if (propType.equals(TimestampType.class)) {
	
		            if(par == null || par.isNull()){
			            ps.setNull(i + offset, Types.TIMESTAMP);
		            }else{
		                java.sql.Timestamp value = ((TimestampType) par).timestampValue();
		                ps.setTimestamp(i + offset, value);
		            }
	
	            } else {
	
	             	String errorMsg = getClass() + ".setPsParameters: ATTENTION !!!!! THE TYPE " + 
	             	                  par.getClass().getName() + " IS NOT MANAGED !!!!!";
	                Exception e = new Exception(errorMsg);
	                LOG.error(e);
	                throw e;
	
	            }
	
	            LOG.debug(getClass() + ".setPsParameters: Setting parameter number " + (i + offset) + " to value [" + par +"]");
	        }
	        
	    } catch (Exception e) {
		    
	        String errorMsg = getClass() + ".setPsParameters: Exception ["+e+"] in setting parameter into model ["+model.getClass()+"]";
	        e = new Exception(errorMsg);
	        LOG.error(e);
	        throw e;
	       
	    }
	}

}
