package com.atosorigin.wfem.util;

import java.util.Iterator;

import org.dom4j.Element;
import org.dom4j.Node;

import com.atosorigin.wfem.command.CommandDataModel;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class QASCaller extends XMLServiceCaller{
	
	/********************************************************************************/
	/********************************************************************************/
	public static QASCallData prepare(String url, CommandDataModel inputModel, String xmlInputTemplate) throws Exception{
		XMLServiceInnerCallData inCallData = new XMLServiceInnerCallData();
		inCallData.setUrl(url);
		return prepare(inCallData,inputModel,xmlInputTemplate);
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static QASCallData prepare(XMLServiceInnerCallData inCalldata, CommandDataModel inputModel, String xmlInputTemplate) throws Exception{
		QASCallData outData = new QASCallData();
		prepareData(outData, inCalldata, inputModel, xmlInputTemplate);
		return outData;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static void findErrorInReceivedXml(QASCallData callData, Element node) throws Exception{
		
		if(node == null || callData.getXmlReceive() == null || callData.getXmlReceive().length() == 0)
			return;
		if(callData.getValuesFound() >= 4)
			return;
		
		Iterator it = node.content().iterator();
		try{
			
			while(it.hasNext()){
				
				Node currentNode = (Node)it.next();
				if(currentNode.getNodeType() != Node.ELEMENT_NODE)
					continue;

				String path = currentNode.getPath();
				if(path.indexOf("/"+callData.getServiceName()+QARC_RESP_SUFFIX+"/") >= 0){
					
					if(path.endsWith("/STATUS/CODE")){
						try{
							int status = Integer.parseInt(currentNode.getText());
							callData.setStatus(status);
							callData.setValuesFound(callData.getValuesFound()+1);
						}catch(Exception e){
							String errMsg = "XMLServiceCaller.findErrorInReceivedXml: STATUS CODE ["+currentNode.getText()+"] is not an integer: "+e;
							e = new Exception(errMsg);
							LOG(callData).error(e);
						}
					}
					
					if(path.endsWith("/STATUS/MESSAGE")){
						callData.setMessage(currentNode.getText());
						callData.setValuesFound(callData.getValuesFound()+1);
					}
					
					if(path.endsWith("/STATUS/SEVERITY")){
						callData.setSeverity(currentNode.getText());
						callData.setValuesFound(callData.getValuesFound()+1);
					}
					
					if(path.endsWith("/STATUS/SOURCE")){
						callData.setSource(currentNode.getText());
						callData.setValuesFound(callData.getValuesFound()+1);
					}
				}
				findErrorInReceivedXml(callData,(Element)currentNode);
			}
			return;
			
		}catch(Exception e){
			String errMsg = "XMLServiceCaller.findErrorInReceivedXml: Exception in finding errors in xml response: "+e;
			e = new Exception(errMsg);
			LOG(callData).error(e);
			throw e;
		}
		
	}
		
}
