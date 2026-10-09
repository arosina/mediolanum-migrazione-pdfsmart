package com.atosorigin.wfem.dao;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.util.OSBCallData;
import com.atosorigin.wfem.util.OSBCaller;
import com.atosorigin.wfem.util.XMLServiceInnerCallData;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
class DAOOSBAccessManager extends DAOAccessManager {
		
	private static final String thisClassName = DAOOSBAccessManager.class.getName();
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	protected DAOOSBResultModel doAccess(ClientSessionContext csc, DAOOSBAccessInfo dbWSAccessInfo,
				  				 		 CommandDataModel inputDataModel, boolean onlyPrepare) throws DAOException {
					
		try{
			DAOOSBResultModel result = new DAOOSBResultModel();
			CommandDataModel outputModel = null;
			String outputModelClassName = dbWSAccessInfo.getOutputDataModelClassName();
			if(outputModelClassName == null || outputModelClassName.length() == 0){
				outputModel = inputDataModel;
			}else{
				Class outputModelClass = Class.forName(dbWSAccessInfo.getOutputDataModelClassName());
				outputModel = (CommandDataModel)outputModelClass.newInstance();
			}
			
			XMLServiceInnerCallData inCallData = new XMLServiceInnerCallData();
			inCallData.setServiceName(dbWSAccessInfo.getServiceName());
			inCallData.setOperationName(dbWSAccessInfo.getOperationName());
			inCallData.setDaoAccessName(dbWSAccessInfo.getDaoObjectName()+"."+dbWSAccessInfo.getDaoAccessName());
			inCallData.setUrl(dbWSAccessInfo.getUrl());
			inCallData.setCompactEmpty(dbWSAccessInfo.isCompactEmpty());
			inCallData.setDataFormatterClassName(dbWSAccessInfo.getDataFormatter());
			inCallData.setResultCharsetName(dbWSAccessInfo.getResultCharsetName());
			inCallData.setMarkNillable(dbWSAccessInfo.isMarkNillable());
			inCallData.setKeepAttrs(dbWSAccessInfo.isKeepAttrs());
			inCallData.setContentType(dbWSAccessInfo.getContentType());
			OSBCallData wsCallData = null;
				
			try{
				wsCallData = OSBCaller.prepare(inCallData,inputDataModel,dbWSAccessInfo.getXmlInputTemplate());
				result.setMiddleTierInputParameters(inCallData.getMiddleTierInputParameters());
				LOG.debug(thisClassName + ".doAccess: Sending XML ["+wsCallData.getXmlSend()+"]");
			}catch(Exception e){
				DAOException daoe = new DAOException(thisClassName+".doAccess: exception creating XML to send: ["+e.toString()+"]");
				LOG.error(daoe);
				throw daoe;			
			}
			
			if(onlyPrepare){
				result.setWsCallData(wsCallData);
				return result;
			}
				
			OSBCaller.sendReceive(csc,wsCallData,outputModel,dbWSAccessInfo.getXmlOutputTemplate());
			LOG.debug(thisClassName + ".doAccess: Received XML ["+wsCallData.getXmlReceive()+"]");
				
			result.setWsCallData(wsCallData);
			result.setResult(outputModel);
			return result;
			
		}catch(Exception e){
			DAOException daoe = new DAOException(thisClassName+".doAccess: exception calling OSB: ["+e.toString()+"]");
			LOG.error(daoe);
			throw daoe;			
		}
	}

}
