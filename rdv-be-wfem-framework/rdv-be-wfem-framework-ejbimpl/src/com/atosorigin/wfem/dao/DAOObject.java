package com.atosorigin.wfem.dao;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.transaction.UserTransaction;

import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.atosorigin.wfem.coddesc.CodDescDataList;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.loggers.DAOLogger;
import com.atosorigin.wfem.tierlog.TierAccessLoggerInfo;
import com.atosorigin.wfem.util.Tools;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class DAOObject {
	
	private static DAOLogger LOG = DAOLogger.getInstance();

	private String daoObjectName;	
	private ClientSessionContext clientContext;
	private Connection objectConnection;
	private Connection accessConnection;
	
	private DAOObjectInfoCache   daoObjectInfoCache   = DAOObjectInfoCache.getInstance();
	private DAOConnectionManager daoConnectionManager = DAOConnectionManager.getInstance();
	private DAOAccessManager     daoAccessManager     = DAOAccessManager.getInstance();
	private DAOCodDescCache      daoCodDescChache     = DAOCodDescCache.getInstance();
	
	private String channel;
	private String country;
	
	private String applDatasourceName;
	
	private UserTransaction ut = null;
	
	protected int queryMaxResultRows = -1;

	protected boolean fetchableQuery = false;
	protected transient PreparedStatement ps;
	protected transient DAOResultSet daoRs;
	protected transient TierAccessLoggerInfo tierAccessLoggerInfo;

	/********************************************************************************
	/********************************************************************************/
	protected DAOObject() {
		super();
	}

	/********************************************************************************
	/********************************************************************************/
	public DAOObject(ClientSessionContext clientContext, String daoObjectName) {
		super();
		this.clientContext = clientContext;
		this.daoObjectName = daoObjectName;
	}

	/********************************************************************************
	/********************************************************************************/
	public Connection getConnection(){
		return objectConnection;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public DAOQASResultModel executeQASAccess(String daoQASAccessName,
									          CommandDataModel inputDataModel) throws DAOException{

		LOG.debug("Executing QAS access ["+daoQASAccessName+"]");
		
		try{
			
			DAOObjectInfo daoObjectInfo= daoObjectInfoCache.getDaoObjectInfo(daoObjectName);
			DAOAccessInfo daoAccessInfo = daoObjectInfo.getDaoAccessInfo(daoQASAccessName);
			if(!(daoAccessInfo instanceof DAOQASAccessInfo)){
				
				String errorMsg = getClass() + ".executeQASAccess: DAO access name ["+daoQASAccessName+"] is not a QAS access";
				DAOException daoe = new DAOException(errorMsg);
				LOG.error(daoe);
				throw daoe;
				
			}

			return daoAccessManager.doQASAccess(clientContext,(DAOQASAccessInfo)daoAccessInfo,inputDataModel,false);
			
		}finally{
			closeAccessConnection();
		}
	}
	
	/********************************************************************************
	/********************************************************************************/
	public DAOOSBResultModel executeOSBAccess(String daoWSAccessName,
									          CommandDataModel inputDataModel) throws DAOException{

		LOG.debug("Executing OSB access ["+daoWSAccessName+"]");
		
		try{
			
			DAOObjectInfo daoObjectInfo= daoObjectInfoCache.getDaoObjectInfo(daoObjectName);
			DAOAccessInfo daoAccessInfo = daoObjectInfo.getDaoAccessInfo(daoWSAccessName);
			if(!(daoAccessInfo instanceof DAOOSBAccessInfo)){
				
				String errorMsg = getClass() + ".executeOSBAccess: DAO access name ["+daoWSAccessName+"] is not a OSB access";
				DAOException daoe = new DAOException(errorMsg);
				LOG.error(daoe);
				throw daoe;
				
			}

			return daoAccessManager.doOSBAccess(clientContext,(DAOOSBAccessInfo)daoAccessInfo,inputDataModel,false);
			
		}finally{
			closeAccessConnection();
		}
	}
	
	/********************************************************************************
	/********************************************************************************/
	public DAOQASResultModel executeQASAccessNoSend(String daoQASAccessName,
									          		CommandDataModel inputDataModel) throws DAOException{

		LOG.debug("Executing QAS access ["+daoQASAccessName+"]");
		
		try{
			
			DAOObjectInfo daoObjectInfo= daoObjectInfoCache.getDaoObjectInfo(daoObjectName);
			DAOAccessInfo daoAccessInfo = daoObjectInfo.getDaoAccessInfo(daoQASAccessName);
			if(!(daoAccessInfo instanceof DAOQASAccessInfo)){
				
				String errorMsg = getClass() + ".executeQASAccess: DAO access name ["+daoQASAccessName+"] is not a QAS access";
				DAOException daoe = new DAOException(errorMsg);
				LOG.error(daoe);
				throw daoe;
				
			}

			return daoAccessManager.doQASAccess(clientContext,(DAOQASAccessInfo)daoAccessInfo,inputDataModel,true);
			
		}finally{
			closeAccessConnection();
		}
	}
	
	/********************************************************************************
	/********************************************************************************/
	public static DAOQueryResultModel executeDynaFetchableQueryAccess(ClientSessionContext clientContext,
															 		  String dataSourceReferenceName,
															 		  String sqlString,
															 		  CommandDataModel inputDataModel,
															 		  Class outputDataModelClass) throws DAOException{
		return innerDynaQueryAccess(clientContext, dataSourceReferenceName, sqlString, inputDataModel, outputDataModelClass, true);
	}
	
	/********************************************************************************
	/********************************************************************************/
	public static CommandDataModel fetchDynaQuery(DAOQueryResultModel result) throws DAOException{
		DAOObject dao = result.dao;
		LOG.debug("Executing fetch on query access ["+result.dbQueryAccessInfo.getDaoAccessName()+"]");
		CommandDataModel model = dao.fetchQuery(result); // Fetch on DAOObject instance 
		if(model == null)
			dao.closeConnection();
		return model;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public static void closeDynaQuery(DAOQueryResultModel result) throws DAOException{
		DAOObject dao = result.dao;
		LOG.debug("Closing fetchable query access ["+result.dbQueryAccessInfo.getDaoAccessName()+"]");
		dao.closeConnection();
		return;
	}

	/********************************************************************************
	/********************************************************************************/
	public static DAOQueryResultModel executeDynaQueryAccess(ClientSessionContext clientContext,
															 String dataSourceReferenceName,
													  		 String sqlString,
													  		 CommandDataModel inputDataModel,
													  		 Class outputDataModelClass) throws DAOException{
		return innerDynaQueryAccess(clientContext, dataSourceReferenceName, sqlString, inputDataModel, outputDataModelClass, false);
	}
	
	/********************************************************************************
	/********************************************************************************/
	private static DAOQueryResultModel innerDynaQueryAccess(ClientSessionContext clientContext,
															 String dataSourceReferenceName,
													  		 String sqlString,
													  		 CommandDataModel inputDataModel,
													  		 Class outputDataModelClass,
													  		 boolean fetchable) throws DAOException{
		
		LOG.debug("Executing on cache ["+dataSourceReferenceName+"] the dynamic query access ["+sqlString+"]");
		
		DAOObject dao = null;
		DAOQueryResultModel ret = null;

		try{
			
			dao = new DAOObject(clientContext,"DAODynaObject");
			
			DAOQueryAccessInfo daoQueryAccessInfo = new DAOQueryAccessInfo();
			daoQueryAccessInfo.setDynamicQuery(true);
			daoQueryAccessInfo.setDataSourceReferenceName(dataSourceReferenceName);
			daoQueryAccessInfo.setDaoObjectName("DynamicObject");
			daoQueryAccessInfo.setDaoAccessName("DynamicQueryAccess");
			daoQueryAccessInfo.setQueryType(DAOQueryAccessInfo.PARSED_QUERY);
			daoQueryAccessInfo.setOutputDataModelClassName(outputDataModelClass.getName());
			
			String daoXmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
			daoXmlString += "<DynaObject>\n";
			daoXmlString += "<Query>\n";
			daoXmlString += sqlString;
			daoXmlString += "</Query>\n";
			daoXmlString += "</DynaObject>\n";
			
			try{
				
				InputStream inputStream = new ByteArrayInputStream(daoXmlString.getBytes());
			    SAXReader myreader = new SAXReader();
			    org.dom4j.Document xmldoc = myreader.read(new org.xml.sax.InputSource(inputStream));

			    Node root = xmldoc.getRootElement();
  	       	    Element xmlQuery = root.selectSingleNode("Query").getParent().element("Query");
			    daoQueryAccessInfo.setSqlNodeString(xmlQuery);
			    
			}catch(Exception e){
		        String errorMsg = "DAOObject.executeDynaQueryAccess: Exception ["+e+"] creating XML tree for ["+daoXmlString+"]";
		        DAOException daoe = new DAOException(errorMsg);
				LOG.error(e);
				throw daoe;
			}
			
			dao.accessConnection = DAOConnectionManager.getInstance().getConnection(clientContext,dataSourceReferenceName);
			dao.fetchableQuery = fetchable;
			ret = DAOAccessManager.getInstance().doQueryAccess(clientContext,dao.accessConnection,
																dao,
												  				daoQueryAccessInfo,
												  				inputDataModel);
			return ret;
			
		}finally{
			if(dao != null){
				if(!fetchable || (fetchable && ret == null)){
					dao.closeConnection();
				}
			}
		}
		
	}
	
	/********************************************************************************
	/********************************************************************************/
	public DAOQueryResultModel executeQueryAccess(String daoQueryAccessName, 
									               CommandDataModel inputDataModel) throws DAOException{

		LOG.debug("Executing query access ["+daoQueryAccessName+"]");
		
		try{
			
			DAOObjectInfo daoObjectInfo= daoObjectInfoCache.getDaoObjectInfo(daoObjectName);
			DAOAccessInfo daoAccessInfo = daoObjectInfo.getDaoAccessInfo(daoQueryAccessName);
			if(!(daoAccessInfo instanceof DAOQueryAccessInfo)){
				
				String errorMsg = getClass() + ".executeQueryAccess: DAO access name ["+daoQueryAccessName+"] is not a query access";
				DAOException daoe = new DAOException(errorMsg);
				LOG.error(daoe);
				throw daoe;
				
			}

			openAccessConnection(clientContext,daoAccessInfo);

			fetchableQuery = false;
			DAOQueryResultModel qRes = daoAccessManager.doQueryAccess(clientContext,accessConnection,
																	  this,
												  					  (DAOQueryAccessInfo)daoAccessInfo,
												  					  inputDataModel);
			return qRes;
			
		}finally{
			closeAccessConnection();
		}
	}
	
	/********************************************************************************
	/********************************************************************************/
	public DAOQueryResultModel executeFetchableQueryAccess(String daoQueryAccessName, 
									               		   CommandDataModel inputDataModel) throws DAOException{

		LOG.debug("Executing query access ["+daoQueryAccessName+"]");
		
		DAOObjectInfo daoObjectInfo= daoObjectInfoCache.getDaoObjectInfo(daoObjectName);
		DAOAccessInfo daoAccessInfo = daoObjectInfo.getDaoAccessInfo(daoQueryAccessName);
		if(!(daoAccessInfo instanceof DAOQueryAccessInfo)){
			
			String errorMsg = getClass() + ".executeQueryAccess: DAO access name ["+daoQueryAccessName+"] is not a query access";
			DAOException daoe = new DAOException(errorMsg);
			LOG.error(daoe);
			throw daoe;
			
		}

		openAccessConnection(clientContext,daoAccessInfo);
					
		fetchableQuery = true;
		DAOQueryResultModel result = daoAccessManager.doQueryAccess(clientContext,accessConnection,
																	this,
																	(DAOQueryAccessInfo)daoAccessInfo,
																	inputDataModel);
		return result;
		
	}
	
	/********************************************************************************
	/********************************************************************************/
	public CommandDataModel fetchQuery(DAOQueryResultModel result) throws DAOException{

		LOG.debug("Executing fetch on query access ["+result.dbQueryAccessInfo.getDaoAccessName()+"]");
		CommandDataModel model = daoAccessManager.fetchQuery(result);
		if(model == null)
			closeAccessConnection();
		return model;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public DAOCallableResultModel executeCallableAccess(String daoCallableAccessName, 
									          	         CommandDataModel inputDataModel) throws DAOException{
	
		LOG.debug("Executing callable access ["+daoCallableAccessName+"]");
		
		try{
			
			DAOObjectInfo daoObjectInfo= daoObjectInfoCache.getDaoObjectInfo(daoObjectName);
			DAOAccessInfo daoAccessInfo = daoObjectInfo.getDaoAccessInfo(daoCallableAccessName);
			if(!(daoAccessInfo instanceof DAOCallableAccessInfo)){
				
				String errorMsg = getClass() + ".executeCallableAccess: DAO access name ["+daoCallableAccessName+"] is not a callable access";
				DAOException daoe = new DAOException(errorMsg);
				LOG.error(daoe);
				throw daoe;
				
			}
			
			openAccessConnection(clientContext,daoAccessInfo);
						
			return daoAccessManager.doCallableAccess(clientContext,accessConnection,
												      (DAOCallableAccessInfo)daoAccessInfo,
												      inputDataModel);
			
		}finally{
			closeAccessConnection();
		}
	}
	
	/********************************************************************************
	/********************************************************************************/
	public DAOTableResultModel executeTableInsertAccess(String daoTableAccessName, 
									                     CommandDataModel inputDataModel) throws DAOException{

		LOG.debug("Executing table insert access ["+daoTableAccessName+"]");
		
		return executeTableAccess(daoTableAccessName,inputDataModel,inputDataModel.getClass(),
									DAOTableAccessInfo.CREATE_ACCESS_MODE);
	}
	
	/********************************************************************************
	/********************************************************************************/
	public DAOTableResultModel executeTableLoadAccess(String daoTableAccessName, 
									                   CommandDataModel inputDataModel) throws DAOException{

		LOG.debug("Executing table load access ["+daoTableAccessName+"]");
		
		return executeTableAccess(daoTableAccessName,inputDataModel,inputDataModel.getClass(),
								   DAOTableAccessInfo.LOAD_ACCESS_MODE);
	}

	/********************************************************************************
	/********************************************************************************/
	public DAOTableResultModel executeTableLoadAllAccess(String daoTableAccessName, 
									                     CommandDataModel inputDataModel,
									                     Class outputDataModelClass) throws DAOException{

		LOG.debug("Executing table load all access ["+daoTableAccessName+"]");
		
		return executeTableAccess(daoTableAccessName,inputDataModel,outputDataModelClass,
		        				  DAOTableAccessInfo.LOAD_ALL_ACCESS_MODE);
	}

	/********************************************************************************
	/********************************************************************************/
	public DAOTableResultModel executeTableLoadChildsAccess(String daoTableAccessName, 
									                   		CommandDataModel inputDataModel,
									                   		Class outputDataModelClass) throws DAOException{

		LOG.debug("Executing table load childs access ["+daoTableAccessName+"]");
		
		return executeTableAccess(daoTableAccessName,inputDataModel,outputDataModelClass,
		        				  DAOTableAccessInfo.LOAD_CHILDS_ACCESS_MODE);
	}

	/********************************************************************************
	/********************************************************************************/
	public DAOTableResultModel executeTableDeleteAccess(String daoTableAccessName, 
									                     CommandDataModel inputDataModel) throws DAOException{

		LOG.debug("Executing table delete access ["+daoTableAccessName+"]");
		
		return executeTableAccess(daoTableAccessName,inputDataModel,inputDataModel.getClass(),
								   DAOTableAccessInfo.DELETE_ACCESS_MODE);
	}

	/********************************************************************************
	/********************************************************************************/
	public DAOTableResultModel executeTableDeleteAllAccess(String daoTableAccessName, 
														   CommandDataModel inputDataModel) throws DAOException{

		LOG.debug("Executing table delete access ["+daoTableAccessName+"]");
		Class ic = null;
		if(inputDataModel != null)
			ic = inputDataModel.getClass();
		return executeTableAccess(daoTableAccessName,inputDataModel,ic,
								   DAOTableAccessInfo.DELETE_ALL_ACCESS_MODE);
	}
	
	/********************************************************************************
	/********************************************************************************/
	public DAOTableResultModel executeTableDeleteChildsAccess(String daoTableAccessName, 
									                          CommandDataModel inputDataModel) throws DAOException{

		LOG.debug("Executing table delete childs access ["+daoTableAccessName+"]");
		
		return executeTableAccess(daoTableAccessName,inputDataModel,inputDataModel.getClass(),
		        				  DAOTableAccessInfo.DELETE_CHILDS_ACCESS_MODE);
	}

	/********************************************************************************
	/********************************************************************************/
	public DAOTableResultModel executeTableUpdateAccess(String daoTableAccessName, 
									                     CommandDataModel inputDataModel) throws DAOException{

		LOG.debug("Executing table update access ["+daoTableAccessName+"]");
		
		return executeTableAccess(daoTableAccessName,inputDataModel,inputDataModel.getClass(),
								   DAOTableAccessInfo.UPDATE_ACCESS_MODE);
	}

	/********************************************************************************
	/********************************************************************************/
	private DAOTableResultModel executeTableAccess(String daoTableAccessName, 
									          	    CommandDataModel inputDataModel,
													Class outputDataModelClass,
									          	    int mode) throws DAOException{
	
		try{
			
			DAOObjectInfo daoObjectInfo= daoObjectInfoCache.getDaoObjectInfo(daoObjectName);
			DAOAccessInfo daoAccessInfo = daoObjectInfo.getDaoAccessInfo(daoTableAccessName);
			if(!(daoAccessInfo instanceof DAOTableAccessInfo)){
				
				String errorMsg = getClass() + ".executeTableAccess: DAO access name ["+daoTableAccessName+"] is not a table access";
				DAOException daoe = new DAOException(errorMsg);
				LOG.error(daoe);
				throw daoe;
				
			}
			
			openAccessConnection(clientContext,daoAccessInfo);
						
			return daoAccessManager.doTableAccess(clientContext,accessConnection,
												   (DAOTableAccessInfo)daoAccessInfo,
												   inputDataModel,outputDataModelClass,mode);
			
		}finally{
			closeAccessConnection();
		}
	}
	
	/********************************************************************************
	/********************************************************************************/
	public void fillCodDesc(CommandDataModel dataModel){
		fillCodDesc(dataModel,true);
	}
	
	/********************************************************************************
	/********************************************************************************/
	public void fillCodDesc(CommandDataModel dataModel, boolean includeInnerModels){
		
		try{	
		
			Map visitedDataList = new HashMap();
			DAOObjectInfo daoObjectInfo = daoObjectInfoCache.getDaoObjectInfo(daoObjectName);
			
			if(!includeInnerModels){
				fillModelCodDesc(dataModel,daoObjectInfo,visitedDataList);
				return;
			}
			
			ArrayList innerModels = Tools.getInnerCommandDataModelList(dataModel);
			for(int i=0;i<innerModels.size();i++){
				CommandDataModel innerModel = (CommandDataModel)innerModels.get(i);
				fillModelCodDesc(innerModel,daoObjectInfo,visitedDataList);
			}
			fillModelCodDesc(dataModel,daoObjectInfo,visitedDataList);
			visitedDataList = null;
			
	    }catch(Exception e){
			String errorMsg = getClass() + ".fillCodDesc: Exception ["+e+"] loading code/description fields for model ["+dataModel.getClass()+"]";
			DAOException daoe = new DAOException(errorMsg);
			LOG.error(daoe);
	    }
	    		
	}
	
	/********************************************************************************
	/********************************************************************************/
	private void fillModelCodDesc(CommandDataModel dataModel, DAOObjectInfo daoObjectInfo,
									Map visitedDataList){
		
		try{
			
		    HashMap codDescFields = dataModel.getCodDescFields();
		    Set keySet = codDescFields.keySet();
		    Iterator propNames = keySet.iterator();
		    while(propNames.hasNext()){
		      String propName = (String)propNames.next();
		      String daoCodDescName = (String)codDescFields.get(propName);

			  CodDescDataList dataList = (CodDescDataList)visitedDataList.get(daoCodDescName);
			  if(dataList == null){
				  DAOCodDescLoadInfo daoCodDescLoadInfo = daoObjectInfo.getDaoCodDescLoadInfo(daoCodDescName);
				  if(daoCodDescLoadInfo == null){
				  	LOG.warning(getClass()+".fillModelCodDesc: property ["+propName+"] in model ["+dataModel+"] has defined a DaoCodDesc access ["+daoCodDescName+"] that is not defined");
				  	continue;
				  }
				  
				  dataList = daoCodDescChache.getCodDescDataList(objectConnection,clientContext,
				  												 daoObjectInfo,daoCodDescLoadInfo,
				  												 dataModel);
				  visitedDataList.put(daoCodDescName,dataList);
			  }			  													 			 
			  dataModel.addCodDescDataList(daoCodDescName,dataList);
		    }	    
			
		}catch(Exception e){
			String errorMsg = getClass() + ".fillModelCodDesc: Exception ["+e+"] loading code/description fields for model ["+dataModel.getClass()+"]";
			DAOException daoe = new DAOException(errorMsg);
			LOG.error(daoe);
		}
	}
	
	/********************************************************************************
	/********************************************************************************/
	public void openConnection() throws DAOException{
		DAOObjectInfo daoObjectInfo= daoObjectInfoCache.getDaoObjectInfo(daoObjectName);
		try{
			String dataSourceRefName = daoObjectInfo.getDataSourceReferenceName();
			openConnection(dataSourceRefName);
			applDatasourceName = null;
		}catch(Exception e){
			String errorMsg = getClass() + ".openConnection: Exception ["+e+"] opening connection for DAO object ["+daoObjectInfo.getDaoObjectName()+"]";
			DAOException daoe = new DAOException(errorMsg);
			LOG.error(daoe);
			throw daoe;
		}
	}
	
	/********************************************************************************
	/********************************************************************************/
	public void openConnection(String dataSourceRefName) throws DAOException{
		try{
			if(objectConnection == null){
				if(channel != null || country != null)
					objectConnection = daoConnectionManager.getConnection(clientContext,dataSourceRefName,country,channel);
				else
					objectConnection = daoConnectionManager.getConnection(clientContext,dataSourceRefName);
			}
			applDatasourceName = dataSourceRefName;
		}catch(Exception e){
			String errorMsg = getClass() + ".openConnection: Exception ["+e+"] opening connection on datasource ["+dataSourceRefName+"]";
			DAOException daoe = new DAOException(errorMsg);
			LOG.error(daoe);
			throw daoe;
		}
	}
	
	/********************************************************************************
	/********************************************************************************/
	public void closeConnection(){
		try{
			if(daoRs != null && daoRs.getResultSet() != null){
				daoRs.getResultSet().close();
			}
		}catch(Exception e){e.printStackTrace();}
		try{
			if(ps != null){
				ps.close();
				ps = null;
			}
		}catch(Exception e){e.printStackTrace();}
		
		if(daoRs != null){
			DAOQueryAccessManager.afterQueryCallback(daoRs.dbConnection, daoRs.dataModel, daoRs.dbQueryAccessInfo);
			daoRs = null;
		}
		
		closeAccessConnection();
		
		if(objectConnection != null){
			daoConnectionManager.closeConnection(objectConnection);
			objectConnection = null;
		}
	}

	/********************************************************************************
	/********************************************************************************/
	private void openAccessConnection(ClientSessionContext clientContext, DAOAccessInfo daoAccessInfo) throws DAOException{

		DAOObjectInfo daoObjectInfo= daoObjectInfoCache.getDaoObjectInfo(daoObjectName);
		try{		
			String objectRefName = daoObjectInfo.getDataSourceReferenceName();
			if(applDatasourceName != null)
				objectRefName = applDatasourceName;
			String accessRefName = daoAccessInfo.getDataSourceReferenceName();
				
			if(accessRefName != null && 
			   !accessRefName.equals(objectRefName)){
				if(channel != null || country != null)
					accessConnection = daoConnectionManager.getConnection(clientContext,accessRefName,country,channel);
				else
					accessConnection = daoConnectionManager.getConnection(clientContext,accessRefName);
			}else{
				if(objectConnection != null){
					accessConnection = objectConnection;
				}else{
					if(channel != null || country != null)
						accessConnection = daoConnectionManager.getConnection(clientContext,objectRefName,country,channel);
					else
						accessConnection = daoConnectionManager.getConnection(clientContext,objectRefName);
				}
			}
		}catch(Exception e){
			String errorMsg = getClass() + ".openAccessConnection: Exception ["+e+"] opening connection for DAO object ["+daoObjectInfo.getDaoObjectName()+"] "+
			                  "on access ["+daoAccessInfo.getDaoAccessName()+"]";
			DAOException daoe = new DAOException(errorMsg);
			LOG.error(daoe);
			throw daoe;
		}
	}			
	
	/********************************************************************************
	/********************************************************************************/
	private void closeAccessConnection(){		
		if(accessConnection == objectConnection)
			return;
		if(accessConnection != null){
			daoConnectionManager.closeConnection(accessConnection);
			accessConnection = null;
		}
	}

	/********************************************************************************
	/********************************************************************************/
	public void setChannel(String channel){
		this.channel = channel;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public void resetChannel(){
		this.channel = null;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public void setCountry(String country){
		this.country = country;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public void resetCountry(){
		this.country = null;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void beginTransaction() throws DAOException {
		try {
		    LOG.debug("Transaction Begin");
			openConnection();
			getConnection().setAutoCommit(false);
		} catch(Exception e) {
			String errorMsg = "Exception on beginTransaction: " + e;
			DAOException daoe = new DAOException(errorMsg);
			LOG.error(daoe);
			throw daoe;
		}
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void commitTransaction() throws DAOException {
		try {
		    LOG.debug("Transaction Commit");
			getConnection().commit();
		    closeConnection();
		} catch(Exception e) {
			String errorMsg = "Exception on commitTransaction: " + e;
			DAOException daoe = new DAOException(errorMsg);
			LOG.error(daoe);
			throw daoe;
		}
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void rollbackTransaction(){
		try {
		    LOG.debug("Transaction Rollback");
			getConnection().rollback();
		    closeConnection();
		} catch(Exception e) {
			String errorMsg = "Exception on rollbackTransaction: " + e;
			e = new Exception(errorMsg);
			LOG.error(e);
		}
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setQueryMaxResultRows(int queryMaxResultRows) {
		this.queryMaxResultRows = queryMaxResultRows;
	}

}
