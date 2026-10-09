package com.atosorigin.wfem.dao;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.util.QASCallData;
import com.atosorigin.wfem.util.QASCaller;
import com.atosorigin.wfem.util.XMLServiceInnerCallData;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
class DAOQASAccessManager extends DAOAccessManager {
		
	private static final String thisClassName = DAOQASAccessManager.class.getName();
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	protected DAOQASResultModel doAccess(ClientSessionContext csc, DAOQASAccessInfo dbQASAccessInfo,
				  				 		 CommandDataModel inputDataModel, boolean onlyPrepare) throws DAOException {
					
		try{
			DAOQASResultModel result = new DAOQASResultModel();
			CommandDataModel outputModel = null;
			String outputModelClassName = dbQASAccessInfo.getOutputDataModelClassName();
			if(outputModelClassName == null || outputModelClassName.length() == 0){
				outputModel = inputDataModel;
			}else{
				Class outputModelClass = Class.forName(dbQASAccessInfo.getOutputDataModelClassName());
				outputModel = (CommandDataModel)outputModelClass.newInstance();
			}
			
			XMLServiceInnerCallData inCallData = new XMLServiceInnerCallData();
			inCallData.setServiceName(dbQASAccessInfo.getServiceName());
			inCallData.setDaoAccessName(dbQASAccessInfo.getDaoObjectName()+"."+dbQASAccessInfo.getDaoAccessName());
			inCallData.setUrl(dbQASAccessInfo.getUrl());
			inCallData.setCompactEmpty(dbQASAccessInfo.isCompactEmpty());
			inCallData.setDataFormatterClassName(dbQASAccessInfo.getDataFormatter());
			inCallData.setResultCharsetName(dbQASAccessInfo.getResultCharsetName());
			inCallData.setMarkNillable(dbQASAccessInfo.isCompactEmpty());
			inCallData.setKeepAttrs(dbQASAccessInfo.isKeepAttrs());
			inCallData.setContentType(dbQASAccessInfo.getContentType());
			QASCallData qasCallData = null;
				
			try{
				qasCallData = QASCaller.prepare(inCallData,inputDataModel,dbQASAccessInfo.getXmlInputTemplate());
				result.setMiddleTierInputParameters(inCallData.getMiddleTierInputParameters());
				LOG.debug(thisClassName + ".doAccess: Sending XML ["+qasCallData.getXmlSend()+"]");
			}catch(Exception e){
				DAOException daoe = new DAOException(thisClassName+".doAccess: exception creating XML to send: ["+e.toString()+"]");
				LOG.error(daoe);
				throw daoe;			
			}
			
			if(onlyPrepare){
				result.setQasCallData(qasCallData);
				return result;
			}
				
			QASCaller.sendReceive(csc,qasCallData,outputModel,dbQASAccessInfo.getXmlOutputTemplate());
			LOG.debug(thisClassName + ".doAccess: Received XML ["+qasCallData.getXmlReceive()+"]");
				
			result.setQasCallData(qasCallData);
			result.setResult(outputModel);
			return result;
			
		}catch(Exception e){
			DAOException daoe = new DAOException(thisClassName+".doAccess: exception calling QAS: ["+e.toString()+"]");
			LOG.error(daoe);
			throw daoe;			
		}
	}

}
