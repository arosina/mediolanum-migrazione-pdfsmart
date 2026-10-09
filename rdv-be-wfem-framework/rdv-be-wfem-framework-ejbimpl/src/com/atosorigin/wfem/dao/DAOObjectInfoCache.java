package com.atosorigin.wfem.dao;

import java.io.InputStream;
import java.util.List;
import java.util.Vector;

import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.atosorigin.wfem.controller.Configuration;
import com.atosorigin.wfem.loggers.DAOLogger;
import com.atosorigin.wfem.util.RefreshNotifier;
import com.atosorigin.wfem.util.RefreshableCache;
import com.atosorigin.wfem.util.RefreshableCacheContainer;
import com.atosorigin.wfem.util.RefreshableCacheIntf;
import com.atosorigin.wfem.util.RefreshableSerializedCache;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
class DAOObjectInfoCache implements RefreshableCacheContainer{
	
	private static DAOObjectInfoCache singleton = null;
	private static DAOLogger LOG = DAOLogger.getInstance();

    private RefreshableCacheIntf cachedDaoObjects = null;

	/********************************************************************************
	/********************************************************************************/
	protected DAOObjectInfoCache() {
		super();
		
		if(Configuration.getInstance().isDaoObjectsCacheSerialized())
			cachedDaoObjects = new RefreshableSerializedCache();
		else
			cachedDaoObjects = new RefreshableCache();
		
		if(Configuration.getInstance().isDaoCacheEnabled()){
			int scanTime = Configuration.getInstance().getDaoObjectsCacheScanTime();
	        RefreshNotifier.addCache(this,cachedDaoObjects,scanTime);
		}
	}
	
	/********************************************************************************
	/********************************************************************************/
	protected static DAOObjectInfoCache getInstance() {
	    if (singleton == null) {
	        synchronized (DAOObjectInfoCache.class) {
	            if (singleton == null) {
	                singleton = new DAOObjectInfoCache();
	            }
	        }
	    }
		return singleton;
	}
	
	/********************************************************************************
	/********************************************************************************/
	protected DAOObjectInfo getDaoObjectInfo(String daoObjectName){
	
		DAOObjectInfo daoObjectInfo = (DAOObjectInfo)cachedDaoObjects.get(daoObjectName);
		if(daoObjectInfo == null){
			try{
				daoObjectInfo = loadDaoObjectInfo(daoObjectName);
			}catch(Exception e){
				String errorMsg = getClass() + ".getDaoObjectInfo: Referenced DAO Object ["+daoObjectName+"] not found";
				e = new Exception(errorMsg);
				LOG.error(e);
				return null;
			}
			if(Configuration.getInstance().isDaoCacheEnabled())
				cachedDaoObjects.put(daoObjectName,daoObjectInfo);
		}else{
	 	  LOG.debug(getClass()+ ".getDaoObjectInfo: DAO Object ["+daoObjectName+"] founded in cache");      
		}
		return daoObjectInfo;
	}
	
	/********************************************************************************
	/********************************************************************************/
	private DAOObjectInfo loadDaoObjectInfo(String daoObjectName) throws Exception{
	
	    DAOAccessInfo daoAccessInfo = null;
	    DAOCodDescLoadInfo daoCodDescLoadInfo = null;
	    
	    DAOObjectInfo daoObjectInfo = new DAOObjectInfo();
	    daoObjectInfo.setDaoObjectName(daoObjectName);
	    
	    try {
		    
	      String pathName = "/DAORoot/" + daoObjectName.replace('.', '/') + ".xml";
	      
	 	  LOG.debug(getClass()+ ".loadDaoObjectInfo: Loading DAO Object ["+daoObjectName+"] from XML file ["+pathName+"]");      
	      InputStream inputStream = getClass().getResourceAsStream(pathName);
	      SAXReader myreader = new SAXReader();
	      org.dom4j.Document xmldoc = myreader.read(new org.xml.sax.InputSource(inputStream));
	      
	      Node root = xmldoc.getRootElement();
	      String globalOsbName =  root.valueOf("@osbName");
		  daoObjectInfo.setDataSourceReferenceName(root.valueOf("@connectionCacheReference"));
		  
	      List xmlCodDescs = root.selectNodes("CodDescriptions/CodDesc");
	      for (int i=0;i<xmlCodDescs.size();i++){
		      
		    Node xmlCodDesc = (Node)xmlCodDescs.get(i);
		    daoCodDescLoadInfo = loadDAOCodDescLoadInfo(xmlCodDesc);
	
		    daoObjectInfo.addDaoCodDescLoadInfo(daoCodDescLoadInfo); 
	      }
		  
	      List xmlAccesses = root.selectNodes("Access");
	      for (int i=0;i<xmlAccesses.size();i++){
		      
		    Node xmlAccess = (Node)xmlAccesses.get(i);
		    String accessType = xmlAccess.valueOf("@type").trim();
		    
	        if(accessType.equals("DAOTableAccess"))
	 		    daoAccessInfo = loadDAOTableAccessInfo(xmlAccess);
	        else if(accessType.equals("DAOQueryAccess"))
	 		    daoAccessInfo = loadDAOQueryAccessInfo(xmlAccess);
	        else if(accessType.equals("DAOCallableAccess"))
	 		    daoAccessInfo = loadDAOCallableAccessInfo(xmlAccess);
	        else if(accessType.equals("DAOQASAccess"))
	 		    daoAccessInfo = loadDAOQASAccessInfo(xmlAccess);
	        else if(accessType.equals("DAOOSBAccess"))
	 		    daoAccessInfo = loadDAOOSBAccessInfo(xmlAccess, globalOsbName);
	 		else{
				String errorMsg = getClass() + ".loadDaoObjectInfo: Access type in object: ["+daoObjectName+"] is not correct";
				ClassCastException cce = new ClassCastException(errorMsg);
				LOG.error(cce);
				throw cce;
	 		}
	          
	        daoAccessInfo.setDaoObjectName(daoObjectInfo.getDaoObjectName());
		    daoObjectInfo.addDaoAccessInfo(daoAccessInfo);
	      }
	
	      inputStream.close();
	    }
	    catch (Exception e) {
			String errorMsg = getClass() + ".loadDaoObjectInfo: Exception ["+e+"] loading info for object: ["+daoObjectName+"]";
			e = new Exception(errorMsg);
			LOG.error(e);
			throw e;
	    }
	
	    return daoObjectInfo;
	}
	  
	/********************************************************************************
	/********************************************************************************/
	private static DAOQASAccessInfo loadDAOQASAccessInfo(Node xmlDaoAccess) throws Exception{
		
		  String xmlVal = null;
		  
	      DAOQASAccessInfo daoQASAccessInfo = new DAOQASAccessInfo();
	      daoQASAccessInfo.setDaoAccessName(xmlDaoAccess.valueOf("@name"));
		  xmlVal = xmlDaoAccess.valueOf("@serviceName");
		  if(xmlVal != null && !xmlVal.equals(""))
			  daoQASAccessInfo.setServiceName(xmlVal);
		  xmlVal = xmlDaoAccess.valueOf("@url");
		  if(xmlVal != null && !xmlVal.equals(""))
			  daoQASAccessInfo.setUrl(xmlVal);
		  else
			  daoQASAccessInfo.setUrl(Configuration.getInstance().getQasUrl());
		  xmlVal = xmlDaoAccess.valueOf("@compactEmpty");
		  if(xmlVal != null && xmlVal.equalsIgnoreCase("true"))
			  daoQASAccessInfo.setCompactEmpty(true);
		  xmlVal = xmlDaoAccess.valueOf("@dataFormatter");
		  if(xmlVal != null && !xmlVal.equals(""))
			  daoQASAccessInfo.setDataFormatter(xmlVal);
		  xmlVal = xmlDaoAccess.valueOf("@resultCharsetName");
		  if(xmlVal != null && !xmlVal.equals(""))
			  daoQASAccessInfo.setResultCharsetName(xmlVal);
		  xmlVal = xmlDaoAccess.valueOf("@markNillable");
		  if(xmlVal != null && xmlVal.equalsIgnoreCase("true"))
			  daoQASAccessInfo.setMarkNillable(true);		  
		  xmlVal = xmlDaoAccess.valueOf("@keepAttrs");
		  if(xmlVal != null && xmlVal.equalsIgnoreCase("true"))
			  daoQASAccessInfo.setKeepAttrs(true);		  
		  xmlVal = xmlDaoAccess.valueOf("@contentType");
		  if(xmlVal != null && !xmlVal.equals(""))
			  daoQASAccessInfo.setContentType(xmlVal);		  
	      daoQASAccessInfo.setOutputDataModelClassName(xmlDaoAccess.valueOf("@OutputCommandDataModel"));
       	  Element xmlInput = xmlDaoAccess.selectSingleNode("Input").getParent().element("Input");
          daoQASAccessInfo.setXmlInputTemplate(xmlInput);  	       	   
       	  Element xmlOutput = xmlDaoAccess.selectSingleNode("Output").getParent().element("Output");
          daoQASAccessInfo.setXmlOutputTemplate(xmlOutput);  	       	   

	      return daoQASAccessInfo;
	
	}
	
	/********************************************************************************
	/********************************************************************************/
	private static DAOOSBAccessInfo loadDAOOSBAccessInfo(Node xmlDaoAccess, String globalOsbName) throws Exception{
		
		  String xmlVal = null;
		  
	      DAOOSBAccessInfo daoOSBAccessInfo = new DAOOSBAccessInfo();
	      daoOSBAccessInfo.setDaoAccessName(xmlDaoAccess.valueOf("@name"));
		  xmlVal = xmlDaoAccess.valueOf("@serviceName");
		  if(xmlVal != null && !xmlVal.equals(""))
			  daoOSBAccessInfo.setServiceName(xmlVal);
		  xmlVal = xmlDaoAccess.valueOf("@operationName");
		  if(xmlVal != null && !xmlVal.equals(""))
			  daoOSBAccessInfo.setOperationName(xmlVal);
		  xmlVal = xmlDaoAccess.valueOf("@url");
		  if(xmlVal != null && !xmlVal.equals("")){
			  daoOSBAccessInfo.setUrl(xmlVal);
		  }else{
			  xmlVal = xmlDaoAccess.valueOf("@osbName");
			  if(xmlVal != null && !xmlVal.equals(""))
				  daoOSBAccessInfo.setUrl(Configuration.getInstance().getOsbNameUrl(xmlVal));
			  else if(globalOsbName != null && !globalOsbName.equals(""))
				  daoOSBAccessInfo.setUrl(Configuration.getInstance().getOsbNameUrl(globalOsbName));
			  else
				  daoOSBAccessInfo.setUrl(Configuration.getInstance().getOsbUrl());
		  }
		  xmlVal = xmlDaoAccess.valueOf("@compactEmpty");
		  if(xmlVal != null && xmlVal.equalsIgnoreCase("true"))
			  daoOSBAccessInfo.setCompactEmpty(true);
		  xmlVal = xmlDaoAccess.valueOf("@dataFormatter");
		  if(xmlVal != null && !xmlVal.equals(""))
			  daoOSBAccessInfo.setDataFormatter(xmlVal);
		  xmlVal = xmlDaoAccess.valueOf("@resultCharsetName");
		  if(xmlVal != null && !xmlVal.equals(""))
			  daoOSBAccessInfo.setResultCharsetName(xmlVal);
		  xmlVal = xmlDaoAccess.valueOf("@markNillable");		  
		  if(xmlVal != null && xmlVal.equalsIgnoreCase("true"))
			  daoOSBAccessInfo.setMarkNillable(true);
		  xmlVal = xmlDaoAccess.valueOf("@keepAttrs");
		  if(xmlVal != null && xmlVal.equalsIgnoreCase("true"))
			  daoOSBAccessInfo.setKeepAttrs(true);
		  xmlVal = xmlDaoAccess.valueOf("@contentType");
		  if(xmlVal != null && !xmlVal.equals(""))
			  daoOSBAccessInfo.setContentType(xmlVal);		 
		  daoOSBAccessInfo.setOutputDataModelClassName(xmlDaoAccess.valueOf("@OutputCommandDataModel"));
       	  Element xmlInput = xmlDaoAccess.selectSingleNode("Input").getParent().element("Input");
       	  daoOSBAccessInfo.setXmlInputTemplate(xmlInput);  	       	   
       	  Element xmlOutput = xmlDaoAccess.selectSingleNode("Output").getParent().element("Output");
       	  daoOSBAccessInfo.setXmlOutputTemplate(xmlOutput);  	       	   

	      return daoOSBAccessInfo;
	
	}
	
	/********************************************************************************
	/********************************************************************************/
	private DAOCallableAccessInfo loadDAOCallableAccessInfo(Node xmlDaoAccess) throws Exception{
	
		  String xmlVal = null;
	
	      DAOCallableAccessInfo daoCallableAccessInfo = new DAOCallableAccessInfo();
	      
	      daoCallableAccessInfo.setDaoAccessName(xmlDaoAccess.valueOf("@name"));
	      daoCallableAccessInfo.setSqlString(xmlDaoAccess.valueOf("@store"));
	      daoCallableAccessInfo.setOutputDataModelClassName(xmlDaoAccess.valueOf("@OutputCommandDataModel"));
		  xmlVal = xmlDaoAccess.valueOf("@concurrencyProperty");
		  if(xmlVal != null && !xmlVal.equals(""))
		     daoCallableAccessInfo.setConcurrencyProperty(xmlVal);
		  xmlVal = xmlDaoAccess.valueOf("@connectionCacheReference");
		  if(xmlVal != null && !xmlVal.equals(""))
		     daoCallableAccessInfo.setDataSourceReferenceName(xmlVal);
		  xmlVal = xmlDaoAccess.valueOf("@namedParameters");
		  if(xmlVal != null && xmlVal.equalsIgnoreCase("true"))
			  daoCallableAccessInfo.setNamedParameters(true);
		  
		  // Input parameters
	      DAOAccessParameters parameters = new DAOAccessParameters();      
	      List xmlParameters = xmlDaoAccess.selectNodes("Input/Parameter");
	      for(int i=0;i < xmlParameters.size(); i++) {
	
	        DAOAccessParameter parameter = new DAOAccessParameter();
	        Node xmlParameter = (Node)xmlParameters.get(i);
	        parameter.setPropertyName(xmlParameter.valueOf("@propertyName"));
	        if(parameter.getPropertyName().equals(daoCallableAccessInfo.getConcurrencyProperty()))
	      	  parameter.setConcurrencyField(true);
	      	  
			xmlVal = xmlParameter.valueOf("@propertyValue");
			if(xmlVal != null && !xmlVal.equals(""))
			  parameter.setPropertyValue(xmlVal);
	        
			xmlVal = xmlParameter.valueOf("@propertyType");
			if(xmlVal != null && !xmlVal.equals(""))
			  parameter.setPropertyType("com.atosorigin.wfem.types."+xmlVal);

	        parameters.addParameter(parameter);
	        
	      }
		  daoCallableAccessInfo.setInputParameters(parameters);
		  
		  // Output parameters
	      xmlParameters = xmlDaoAccess.selectNodes("Output/Parameter");
	      parameters = new DAOAccessParameters();
	      for(int i=0;i < xmlParameters.size(); i++) {
	
	        DAOAccessParameter parameter = new DAOAccessParameter();
	        Node xmlParameter = (Node)xmlParameters.get(i);
	        
	        parameter.setPropertyName(xmlParameter.valueOf("@propertyName"));
		    xmlVal = xmlParameter.valueOf("@fileTypeDataPart");
			if(xmlVal != null && !xmlVal.equals(""))   
			   parameter.setFileTypeDataPart(xmlVal);
	
	        parameters.addParameter(parameter);
	
	      }
		  daoCallableAccessInfo.setOutputParameters(parameters);
		  
	      return daoCallableAccessInfo;
	}
	
	/********************************************************************************
	/********************************************************************************/
	private DAOCodDescLoadInfo loadDAOCodDescLoadInfo(Node xmlCodDesc) throws Exception{
	
		String xmlVal = null;
		
	    DAOCodDescLoadInfo daoCodDescLoadInfo = new DAOCodDescLoadInfo();
		xmlVal = xmlCodDesc.valueOf("@connectionCacheReference");
		if(xmlVal != null && !xmlVal.equals(""))
			daoCodDescLoadInfo.setDataSourceReferenceName(xmlVal);
	    daoCodDescLoadInfo.setCodDescName(xmlCodDesc.valueOf("@name"));
	    daoCodDescLoadInfo.setTableName(xmlCodDesc.valueOf("@tableName"));
	    daoCodDescLoadInfo.setCodColumnName(xmlCodDesc.valueOf("@codColumnName"));
	    
		xmlVal = xmlCodDesc.valueOf("@codAliasColumnName");
		if(xmlVal != null && !xmlVal.equals(""))
	    	daoCodDescLoadInfo.setCodAliasColumnName(xmlVal);
		xmlVal = xmlCodDesc.valueOf("@descrAliasColumnName");
		if(xmlVal != null && !xmlVal.equals(""))
	    	daoCodDescLoadInfo.setDescrAliasColumnName(xmlVal);
		xmlVal = xmlCodDesc.valueOf("@orderBy");
		if(xmlVal != null && !xmlVal.equals(""))
	    	daoCodDescLoadInfo.setOrderBy(xmlVal);
		xmlVal = xmlCodDesc.valueOf("@distinct");
		if(xmlVal != null && xmlVal.equalsIgnoreCase("true"))
	    	daoCodDescLoadInfo.setDistinct(true);
		xmlVal = xmlCodDesc.valueOf("@shortDescrColumnName");
		if(xmlVal != null && !xmlVal.equals(""))
	    	daoCodDescLoadInfo.setShortDescrColumnName(xmlVal);
		xmlVal = xmlCodDesc.valueOf("@descrColumnName");
		if(xmlVal != null && !xmlVal.equals(""))
	    	daoCodDescLoadInfo.setDescrColumnName(xmlVal);
		xmlVal = xmlCodDesc.valueOf("@whereCondition");
		if(xmlVal != null && !xmlVal.equals(""))
			daoCodDescLoadInfo.setWhereCondition(xmlVal);
		xmlVal = xmlCodDesc.valueOf("@validityWhereCondition");
		if(xmlVal != null && !xmlVal.equals(""))
			daoCodDescLoadInfo.setValidityWhereCondition(xmlVal);
	    xmlVal = xmlCodDesc.valueOf("@refreshTime");
	    if(xmlVal != null && !xmlVal.equals(""))
	        daoCodDescLoadInfo.setRefreshTime(Integer.parseInt(xmlVal));
	    else
	    	daoCodDescLoadInfo.setRefreshTime(Configuration.getInstance().getDaoCodDescDefaultLifeTime());
			
		Vector whereConditionFields = daoCodDescLoadInfo.getWhereConditionFields();
	    List xmlWhereFields = xmlCodDesc.selectNodes("WhereFields/Parameter");
	    for (int i=0;i<xmlWhereFields.size();i++){     
	      Node xmlValField = (Node)xmlWhereFields.get(i);
		  xmlVal = xmlValField.valueOf("@propertyName");
		  if(xmlVal != null && !xmlVal.equals(""))
		  	whereConditionFields.add(xmlVal);
	    }
			
		Vector validityFields = daoCodDescLoadInfo.getValidityWhereConditionFields();
	    List xmlValidityFields = xmlCodDesc.selectNodes("ValidityFields/Parameter");
	    for (int i=0;i<xmlValidityFields.size();i++){  
	      Node xmlValField = (Node)xmlValidityFields.get(i);
		  xmlVal = xmlValField.valueOf("@propertyName");
		  if(xmlVal != null && !xmlVal.equals(""))
		  	validityFields.add(xmlVal);
	    }
	    
	    if(whereConditionFields.size() > 0 ||
	       validityFields.size() > 0)
	        daoCodDescLoadInfo.setRefreshTime(0);
			
		return daoCodDescLoadInfo;      
	}
	
	/********************************************************************************
	/********************************************************************************/
	private static DAOQueryAccessInfo loadDAOQueryAccessInfo(Node xmlDaoAccess) throws Exception{
		  
	       String xmlVal = null;
	
	       DAOQueryAccessInfo daoQueryAccessInfo = new DAOQueryAccessInfo();
	       
	       daoQueryAccessInfo.setDaoAccessName(xmlDaoAccess.valueOf("@name"));
	       xmlVal = xmlDaoAccess.valueOf("@queryType");
	       if(xmlVal != null && !xmlVal.equals(""))
		       daoQueryAccessInfo.setQueryType(Integer.parseInt(xmlVal));
	       
	       if(daoQueryAccessInfo.getQueryType() == DAOQueryAccessInfo.PARSED_QUERY){
  	       	   Element xmlQuery = xmlDaoAccess.selectSingleNode("Query").getParent().element("Query");
	           daoQueryAccessInfo.setSqlNodeString(xmlQuery);  	       	   
	       }else{
	           String sqlQuery = "";
		       sqlQuery = xmlDaoAccess.valueOf("@query");
		       if(sqlQuery == null || sqlQuery.equals("")){
	  	          	Node xmlQuery = xmlDaoAccess.selectSingleNode("Query");
	  	          	if(xmlQuery != null)
		  	          	sqlQuery = xmlQuery.getText();
		       }
	           daoQueryAccessInfo.setSqlString(sqlQuery);
	       }
           
	       daoQueryAccessInfo.setOutputDataModelClassName(xmlDaoAccess.valueOf("@OutputCommandDataModel"));
	       xmlVal = xmlDaoAccess.valueOf("@maxResultRows");
	       if(xmlVal != null && !xmlVal.equals(""))
		       daoQueryAccessInfo.setMaxResultRows(Integer.parseInt(xmlVal));
		   xmlVal = xmlDaoAccess.valueOf("@concurrencyProperty");
		   if(xmlVal != null && !xmlVal.equals(""))
		       daoQueryAccessInfo.setConcurrencyProperty(xmlVal);
		   xmlVal = xmlDaoAccess.valueOf("@connectionCacheReference");
		   if(xmlVal != null && !xmlVal.equals(""))
		       daoQueryAccessInfo.setDataSourceReferenceName(xmlVal);
	       xmlVal = xmlDaoAccess.valueOf("@useCallable");
	       if(xmlVal != null && !xmlVal.equals(""))
	    	   daoQueryAccessInfo.setUseCallable(Boolean.valueOf(xmlVal).booleanValue());
		   xmlVal = xmlDaoAccess.valueOf("@storedToCallAfterQuery");
		   if(xmlVal != null && !xmlVal.equals(""))
		       daoQueryAccessInfo.setStoredToCallAfterQuery(xmlVal);
		   xmlVal = xmlDaoAccess.valueOf("@tablesToDeleteAfterQuery");
		   if(xmlVal != null && !xmlVal.equals(""))
		       daoQueryAccessInfo.setTablesToDeleteAfterQuery(xmlVal);

		   // Input parameters
	       DAOAccessParameters parameters = new DAOAccessParameters();
	       List xmlParameters = xmlDaoAccess.selectNodes("Input/Parameter");
	       for(int i=0;i<xmlParameters.size();i++){
	
	         DAOAccessParameter parameter = new DAOAccessParameter();
	         Node xmlParameter = (Node)xmlParameters.get(i);
	         
	         parameter.setPropertyName(xmlParameter.valueOf("@propertyName"));
	         if(parameter.getPropertyName().equals(daoQueryAccessInfo.getConcurrencyProperty()))
	      	    parameter.setConcurrencyField(true);
	         
			 xmlVal = xmlParameter.valueOf("@propertyValue");
			 if(xmlVal != null && !xmlVal.equals(""))
			   parameter.setPropertyValue(xmlVal);
	        
			 xmlVal = xmlParameter.valueOf("@propertyType");
			 if(xmlVal != null && !xmlVal.equals(""))
			   parameter.setPropertyType("com.atosorigin.wfem.types."+xmlVal);

	         parameters.addParameter(parameter);         
	       }
		   daoQueryAccessInfo.setInputParameters(parameters);
		   
		   // Output parameters
	       parameters = new DAOAccessParameters();
	       xmlParameters = xmlDaoAccess.selectNodes("Output/Parameter");
	       for(int i=0;i<xmlParameters.size();i++){
	
	         DAOAccessParameter parameter = new DAOAccessParameter();
	         Node xmlParameter = (Node)xmlParameters.get(i);
	         
	         parameter.setPropertyName(xmlParameter.valueOf("@propertyName"));
	         if(parameter.getPropertyName().equals(daoQueryAccessInfo.getConcurrencyProperty()))
	      	    parameter.setConcurrencyField(true);
	         xmlVal = xmlParameter.valueOf( "@notNullValue");
		     if(xmlVal != null && !xmlVal.equals(""))
	      	 	parameter.setNotNullValue(xmlVal);
		     xmlVal = xmlParameter.valueOf("@fileTypeDataPart");
			 if(xmlVal != null && !xmlVal.equals(""))   
			   parameter.setFileTypeDataPart(xmlVal);
	
			 xmlVal = xmlParameter.valueOf("@propertyType");
			 if(xmlVal != null && !xmlVal.equals(""))
			   parameter.setPropertyType("com.atosorigin.wfem.types."+xmlVal);
			 
	         parameters.addParameter(parameter);
	       }
		   daoQueryAccessInfo.setOutputParameters(parameters);
	
	       return daoQueryAccessInfo;
	
	}
	
	/********************************************************************************
	/********************************************************************************/
	private static DAOTableAccessInfo loadDAOTableAccessInfo(Node xmlDaoAccess) throws Exception{
	
	    String xmlVal = null;
	
	    DAOTableAccessInfo daoTableAccessInfo = new DAOTableAccessInfo();
	    daoTableAccessInfo.setDaoAccessName(xmlDaoAccess.valueOf("@name"));
	    daoTableAccessInfo.setSqlString(xmlDaoAccess.valueOf("@tableName"));

	    xmlVal = xmlDaoAccess.valueOf("@replicable");
		if(xmlVal != null && !xmlVal.equals(""))
	    	daoTableAccessInfo.setReplicable(Boolean.valueOf(xmlVal).booleanValue());

	    xmlVal = xmlDaoAccess.valueOf("@identityProperty");
		if(xmlVal != null && !xmlVal.equals(""))
	    	daoTableAccessInfo.setIdentityProperty(xmlVal);
	    xmlVal = xmlDaoAccess.valueOf("@sequenceName");
		if(xmlVal != null && !xmlVal.equals(""))
	    	daoTableAccessInfo.setSequenceName(xmlVal);
	    xmlVal = xmlDaoAccess.valueOf("@concurrencyProperty");
		if(xmlVal != null && !xmlVal.equals(""))
		    daoTableAccessInfo.setConcurrencyProperty(xmlVal);
	    xmlVal = xmlDaoAccess.valueOf("@connectionCacheReference");
	    if(xmlVal != null && !xmlVal.equals(""))
	        daoTableAccessInfo.setDataSourceReferenceName(xmlVal);
	    xmlVal = xmlDaoAccess.valueOf("@orderBy");
	    if(xmlVal != null && !xmlVal.equals(""))
	        daoTableAccessInfo.setOrderBy(xmlVal);
	    
	    DAOAccessParameters parameters = new DAOAccessParameters();
	    List xmlParameters = xmlDaoAccess.selectNodes("Bindings/Parameter");
	    for (int i=0;i<xmlParameters.size();i++){
	
	      DAOAccessParameter parameter = new DAOAccessParameter();
	      Node xmlParameter = (Node)xmlParameters.get(i);
	      
	      parameter.setPropertyName(xmlParameter.valueOf("@propertyName"));
	      if(parameter.getPropertyName().equals(daoTableAccessInfo.getConcurrencyProperty()))
	      	parameter.setConcurrencyField(true);
	      parameter.setDbColumnName(xmlParameter.valueOf("@dbColumnName"));
	      xmlVal = xmlParameter.valueOf("@notNullValue");
		  if(xmlVal != null && !xmlVal.equals(""))   
		      parameter.setNotNullValue(xmlVal);
		  xmlVal = xmlParameter.valueOf("@fieldType");
		  if(xmlVal != null && !xmlVal.equals(""))
		      parameter.setFieldType(Integer.parseInt(xmlVal));
		  xmlVal = xmlParameter.valueOf("@primaryKey");
		  if(xmlVal != null && !xmlVal.equals(""))
		      parameter.setPrimaryKey(Boolean.valueOf(xmlVal).booleanValue());

		  xmlVal = xmlParameter.valueOf("@fileTypeDataPart");
		  if(xmlVal != null && !xmlVal.equals(""))   
		      parameter.setFileTypeDataPart(xmlVal);

		  xmlVal = xmlParameter.valueOf("@foreignKey");
		  if(xmlVal != null && !xmlVal.equals(""))
		      parameter.setForeignKey(xmlVal);
		      
	      xmlVal = xmlParameter.valueOf("@propertyValue");
		  if(xmlVal != null && !xmlVal.equals(""))
		     parameter.setPropertyValue(xmlVal);
	        
		  xmlVal = xmlParameter.valueOf("@propertyType");
		  if(xmlVal != null && !xmlVal.equals(""))
			 parameter.setPropertyType("com.atosorigin.wfem.types."+xmlVal);

	      parameters.addParameter(parameter);
	    }
	    daoTableAccessInfo.setInputParameters(parameters);
	
		return daoTableAccessInfo;
	}
}
