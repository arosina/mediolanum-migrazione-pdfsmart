package com.atosorigin.wfem.util;

import java.util.Iterator;

import org.dom4j.Element;
import org.dom4j.Node;

import com.atosorigin.wfem.command.CommandDataModel;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class OSBCaller extends XMLServiceCaller {
	
	/********************************************************************************/
	/********************************************************************************/
	public static OSBCallData prepare(String url, CommandDataModel inputModel, String xmlInputTemplate) throws Exception{
		XMLServiceInnerCallData inCallData = new XMLServiceInnerCallData();
		inCallData.setUrl(url);
		return prepare(inCallData,inputModel,xmlInputTemplate);
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static OSBCallData prepare(XMLServiceInnerCallData inCalldata, CommandDataModel inputModel, String xmlInputTemplate) throws Exception{
		OSBCallData outData = new OSBCallData();
		prepareData(outData, inCalldata, inputModel, xmlInputTemplate);
		return outData;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static void findErrorInReceivedXml(OSBCallData callData, Element root) throws Exception{
		if(root == null || callData.getXmlReceive() == null || callData.getXmlReceive().length() == 0)
			return;
		try{
			findBody(callData, root);
		}catch(Exception e){
			String errMsg = "XMLServiceCaller.findErrorInReceivedXml: Exception in finding errors in xml response: "+e;
			e = new Exception(errMsg);
			LOG(callData).error(e);
			throw e;
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private static void findBody(OSBCallData callData, Element root) throws Exception{
		Iterator it = root.content().iterator();
		while(it.hasNext()){
			Node currentNode = (Node)it.next();
			if(currentNode.getNodeType() != Node.ELEMENT_NODE)
				continue;
			if(currentNode.getName().equalsIgnoreCase("body"))
				findFault(callData, (Element)currentNode);
		}
		return;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private static void findFault(OSBCallData callData, Element body) throws Exception{
		Iterator it = body.content().iterator();
		while(it.hasNext()){
			Node currentNode = (Node)it.next();
			if(currentNode.getNodeType() != Node.ELEMENT_NODE)
				continue;
			if(currentNode.getName().equalsIgnoreCase("fault"))
				initFault(callData, (Element)currentNode);
		}
		return;
	}	
	
	/********************************************************************************/
	/********************************************************************************/
	private static void initFault(OSBCallData callData, Element fault) throws Exception{
		Iterator it = fault.content().iterator();
		while(it.hasNext()){
			Node currentNode = (Node)it.next();
			if(currentNode.getNodeType() != Node.ELEMENT_NODE)
				continue;
			
			callData.setStatus(OSBCallData.STATUS_HTTP_POST_KO);
			
			if(currentNode.getName().equalsIgnoreCase("faultcode")){
				callData.setSeverity(currentNode.getText());
				callData.setValuesFound(callData.getValuesFound()+1);
			}
			
			if(currentNode.getName().equalsIgnoreCase("faultstring")){
				callData.setMessage(currentNode.getText());
				callData.setValuesFound(callData.getValuesFound()+1);
			}
		}
		return;
	}
	
}
