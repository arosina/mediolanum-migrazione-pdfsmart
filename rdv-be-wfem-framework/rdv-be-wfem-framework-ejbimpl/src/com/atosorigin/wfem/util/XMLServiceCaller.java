package com.atosorigin.wfem.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.controller.Configuration;
import com.atosorigin.wfem.loggers.AbstractLogger;
import com.atosorigin.wfem.tierlog.AbstractAccessLogger;
import com.atosorigin.wfem.tierlog.MiddleTierAccessLogger;
import com.atosorigin.wfem.tierlog.TierAccessLoggerInfo;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.xmlservicelogger.XmlServiceLoggerManager;


/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public abstract class XMLServiceCaller {
		
	protected static String QARC_CALL_SUFFIX = "QARCMSGSRQV1";
	protected static String QARC_RESP_SUFFIX = "QARCMSGSRSV1";
		
	private static final String TAG_asXml = "@asXml";
	private static final String TAG_propertyName = "@propertyName";
	private static final String TAG_propertyType = "@propertyType";
	private static final String TAG_attrs = "@attrs";
	private static final String TAG_attrs_value_indicator = "#TAG_VALUE"; // Per caricare liste di stringhe: nel template specificare propertyName="%listType%" attrs="%stringName%@#TAG_VALUE"
	
	private static final String TAG_appInfo = "@appInfo";
	private static final String TAG_dataFormatter = "@dataFormatter";
			
	private static final String TAG_nillable_true = "xsi:nil=\"true\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"";	
	private static final String TAG_append = "@append";
	private static final String TAG_listContainer = "listContainer";
	private static final String TAG_markNillable = "markNillable";
	
	private static final List<String> reservedAttrNameList = Arrays.asList(TAG_append.substring(1), TAG_asXml.substring(1), TAG_propertyName.substring(1), TAG_propertyType.substring(1), 
			TAG_attrs.substring(1), TAG_appInfo.substring(1), TAG_dataFormatter.substring(1), TAG_listContainer, TAG_markNillable);
			
	private static final String cdataStart = "<![CDATA[";
	private static final String cdataEnd = "]]>";
	
	
	/********************************************************************************/
	/********************************************************************************/
	static AbstractLogger LOG(){
		return com.atosorigin.wfem.loggers.QASLogger.getInstance();
	}
	
	/********************************************************************************/
	/********************************************************************************/
	static AbstractLogger LOG(XmlServiceCallData callData){
		if(callData.getServiceType() == XmlServiceCallData.OSB_SERVICE_TYPE)
			return com.atosorigin.wfem.loggers.OSBLogger.getInstance();
		else
			return com.atosorigin.wfem.loggers.QASLogger.getInstance();
	}
	
	/********************************************************************************/
	/********************************************************************************/
	static void findErrorInReceivedXml(XmlServiceCallData callData, Element xmlRoot) throws Exception{
		callData.setValuesFound(0);
		if(callData.getServiceType() == XmlServiceCallData.OSB_SERVICE_TYPE)
			OSBCaller.findErrorInReceivedXml((OSBCallData)callData, xmlRoot);
		else
			QASCaller.findErrorInReceivedXml((QASCallData)callData, xmlRoot);
	}
	
	/********************************************************************************/
	/********************************************************************************/
	protected static void prepareData(XmlServiceCallData outData, XMLServiceInnerCallData inCalldata, 
									CommandDataModel inputModel, String xmlInputTemplate) throws Exception{
	
		// Set data formatter
		Class formatterClass = null;
		if( inCalldata.getDataFormatterClassName() != null && 
		   (inCalldata.dataFormatterClass == null || !inCalldata.dataFormatterClass.getName().equals(inCalldata.getDataFormatterClassName()))){
			try{
				formatterClass = Class.forName(inCalldata.getDataFormatterClassName());
			}catch(Exception e){
				LOG(outData).error(e);
				formatterClass = null;
			}
		}
		inCalldata.dataFormatterClass=formatterClass;
		// Copy formatter onto the output data
		outData.dataFormatterClassName=inCalldata.getDataFormatterClassName();
		outData.dataFormatterClass=inCalldata.dataFormatterClass;
		outData.resultCharsetName=inCalldata.getResultCharsetName();
		
		outData.setUrl(inCalldata.getUrl());
		
		String xmlSend = null;
		if(inputModel != null)
			xmlSend = xmlFromModel(inputModel,xmlInputTemplate,inCalldata,outData);
		else
			xmlSend = xmlInputTemplate;
		outData.setXmlSend(xmlSend);
		
		String serviceName = inCalldata.getServiceName();
		if(serviceName == null || serviceName.length() == 0)
			serviceName = findServiceName(outData);
		if(serviceName == null)
			throw new Exception("XMLCaller.prepare: Bad input XML. No service name found");
		
		outData.setServiceName(serviceName);
		outData.setOperationName(inCalldata.getOperationName());
		outData.setDaoAccessName(inCalldata.getDaoAccessName());
		outData.setContentType(inCalldata.getContentType());
		return;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static void sendReceive(XmlServiceCallData callData, CommandDataModel outputModel, 
								   String xmlOutputTemplate) throws Exception{
		sendReceive(new ClientSessionContext(), callData, outputModel, xmlOutputTemplate);
	}
	
	private static final String CONFIGURATION_FILE_EXCLUDED = Configuration.CONFIGURATION_FILES_ROOT+"/wfemXmlServicesSimulationConfExcluded.properties";
	private static final String CONFIGURATION_FILE = Configuration.CONFIGURATION_FILES_ROOT+"/wfemXmlServicesSimulationConf.properties";
	
	/****************************************************************/
	/****************************************************************/
	private static boolean isServiceSimulated(String daoAccessName, String serviceName){
		try{
			
			// Included services
			Properties srv = new Properties();
			InputStream inputStream = srv.getClass().getResourceAsStream(CONFIGURATION_FILE);
			if(inputStream == null){
				srv = null;
			}else{
				srv.load(inputStream);
				inputStream.close();
			}
			
			// Excluded services
			Properties srvExcluded = new Properties();
			inputStream = srvExcluded.getClass().getResourceAsStream(CONFIGURATION_FILE_EXCLUDED);
			if(inputStream != null){
				srvExcluded.load(inputStream);
				inputStream.close();
			}
			
			if((daoAccessName != null && srvExcluded.get(daoAccessName) != null) || srvExcluded.get(serviceName) != null)
				return false;
			
			if(srv == null || srv.isEmpty())
				return true;
			
			if((daoAccessName != null && srv.get(daoAccessName) != null) || srv.get(serviceName) != null)
				return true;
			
			return false;
			
		}catch(Throwable t){
			t.printStackTrace();
			return true;
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static void sendReceive(ClientSessionContext csc, XmlServiceCallData callData, 
								   CommandDataModel outputModel, String xmlOutputTemplate) throws Exception{
		
		String simulationFileName = null;
		String operation = callData.getOperationName();
		if(operation.length() > 0)
			operation = "@" + operation;
		if(callData.getServiceType() == XmlServiceCallData.OSB_SERVICE_TYPE){
			if(Configuration.getInstance().isOSBSimulationEnabled())
				simulationFileName = "/osbSimulation/"+callData.getServiceName().replaceAll("/","@")+operation+".xml";
		}else{
			if(Configuration.getInstance().isQasSimulationEnabled())
				simulationFileName = "/qasSimulation/"+callData.getServiceName()+".xml";
		}
		
		if(simulationFileName != null && isServiceSimulated(callData.getDaoAccessName(), callData.getServiceName()+operation)){
			String simulationXml = "";
			try{
				simulationXml = Tools.loadTextResourceAsString(simulationFileName, callData);
			}catch(Exception e){
				throw new Exception("Simulation file for service "+callData.getServiceName()+" not found");
			}
			
			callData.setStatus(XmlServiceCallData.STATUS_OK);
			callData.setXmlReceive(simulationXml);
			
			loadModelFromXml(callData,outputModel,xmlOutputTemplate);
			
		    callData.setStartTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
		    callData.setEndTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
			return;
		}
		
		XmlServiceLoggerManager xmlServiceLogger = null;
		try{
			xmlServiceLogger = (XmlServiceLoggerManager)BkRemoteObjectFactory.getInstance().getManager(csc,XmlServiceLoggerManager.class);
			if(xmlServiceLogger.isServiceDisabled(csc,callData.getServiceName()+operation)){
				callData.setStatus(XmlServiceCallData.STATUS_SERVICE_DISABLED);
				return;
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		String exception = null;
		try{
			
			// Set data formatter
			if( callData.dataFormatterClassName != null && 
			   (callData.dataFormatterClass == null || !callData.dataFormatterClass.getName().equals(callData.dataFormatterClassName))){
				try{
					Class formatterClass = Class.forName(callData.dataFormatterClassName);
					callData.dataFormatterClass = formatterClass;
				}catch(Exception e){
					LOG(callData).error(e);
					callData.dataFormatterClass = null;
				}
			}

			if(!inSendReceive(csc, callData, callData.getServiceName()+operation)){
				findErrorInReceivedXml(callData,getReceivedXmlRoot(callData));
				callData.setForcedTrace(true);
				try{
					if(xmlServiceLogger != null) 
						xmlServiceLogger.log(csc,callData);
				}catch(Exception e){
					LOG(callData).error(e);
				}
				return;
			}
			loadModelFromXml(callData,outputModel,xmlOutputTemplate);
			
		}catch(Exception e){
			LOG(callData).error(e);
			exception = e.toString();
			callData.setForcedTrace(true);
			callData.setMessage(exception);
		}
		
		try{
			if(xmlServiceLogger != null) 
				xmlServiceLogger.log(csc,callData);
		}catch(Exception e){
			LOG(callData).error(e);
		}
		
		if(exception != null)
			throw new Exception(exception);
		
		return;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static String xmlFromModel(CommandDataModel model, String xmlTemplate, 
									  boolean compactEmptyTags, Class dataFormatterClass) throws Exception{
		XMLServiceInnerCallData inCallData = new XMLServiceInnerCallData();
		inCallData.setCompactEmpty(compactEmptyTags);
		inCallData.dataFormatterClass = dataFormatterClass;
		
		QASCallData callData = new QASCallData();
		callData.dataFormatterClass = dataFormatterClass;
		return xmlFromModel(model,xmlTemplate,inCallData,callData);
	}

	/********************************************************************************/
	/********************************************************************************/
	private static String nodeTagStart(Element el, XMLServiceInnerCallData inCallData) {
		if (inCallData.isKeepAttrs()) {
			StringBuffer attrList = new StringBuffer();		
					
			Iterator itNode = el.nodeIterator();
			while(itNode.hasNext()){
				Node currentNode = (Node) itNode.next();
				if (currentNode.getNodeType() == Node.NAMESPACE_NODE) {
					if (attrList.length() > 0) {
						attrList.append(" ");
					}
					attrList.append(currentNode.asXML());
				}
			}
			
			Iterator itAttr = el.attributeIterator();
			while(itAttr.hasNext()){
				Attribute currentAttribute = (Attribute) itAttr.next();
				if (!reservedAttrNameList.contains(currentAttribute.getName())) {
					if (attrList.length() > 0) {
						attrList.append(" ");
					}				
					attrList.append(currentAttribute.asXML());				
				}
				else if (currentAttribute.getName().equals(TAG_append.substring(1))) {
					if (attrList.length() > 0) {
						attrList.append(" ");
					}
					attrList.append(currentAttribute.getStringValue());
				}
			}
			
			return el.getQualifiedName()+(attrList.length() > 0 ? " "+attrList.toString():"");
		}
		else {
			String append = el.valueOf(TAG_append);
			return el.getQualifiedName()+(append != null && append.length() > 0 ? " "+append:"");			
		}
	}	
		
	/********************************************************************************/
	/********************************************************************************/
	private static String nodeTag(Node node){
		if(node instanceof Element){
			Element el =  (Element)node;
			return el.getQualifiedName();
		}else
			return node.getName();
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private static String xmlFromModel(CommandDataModel model, String xmlTemplate, 
									   XMLServiceInnerCallData inCallData, XmlServiceCallData callData) throws Exception{
		try{
			ByteArrayInputStream isXmlTemplate = new ByteArrayInputStream(xmlTemplate.getBytes());
			SAXReader myreader = new SAXReader();
			org.dom4j.Document xmldoc = myreader.read(new org.xml.sax.InputSource(isXmlTemplate));
			Element root = xmldoc.getRootElement();
			StringBuffer result = new StringBuffer();
			inCallData.getNameSpaces().clear();
			result.append(transformModelToXmlNode(model,root,inCallData,callData).toString());
			return "<"+nodeTagStart(root, inCallData)+inCallData.nameSpaces()+">"+result.toString()+"</"+nodeTag(root)+">";			
		}catch(Exception e){
			String errMsg = "XMLServiceCaller.xmlFromModel: Exception in generating service xml request: "+e;
			e = new Exception(errMsg);
			LOG(callData).error(e);
			throw e;
		}
	}

	/********************************************************************************/
	/********************************************************************************/
	private static StringBuffer transformModelToXmlNode(CommandDataModel model, Element node, 
														XMLServiceInnerCallData inCallData, XmlServiceCallData callData) throws Exception{
		if (!inCallData.isKeepAttrs()) {
			inCallData.addNameSpace(node.getNamespace());
		}
		
		StringBuffer result = new StringBuffer();
		Iterator it = node.content().iterator();
		try{
			
			while(it.hasNext()){
				
				Node currentNode = (Node) it.next();
				switch(currentNode.getNodeType()){
				
					case Node.TEXT_NODE:
						result.append(currentNode.getText());
						break;
						
					case Node.ELEMENT_NODE:
						Element currentElement = (Element)currentNode;
						String propName = currentElement.valueOf(TAG_propertyName);
						
						String nodeAttr = "";
						
						if(propName == null || propName.length() == 0){
							if(model != null)
								nodeAttr = manageModelToXmlNodeAttr(model,currentElement,inCallData,callData);
							result.append("<"+nodeTagStart(currentElement, inCallData)+nodeAttr+">");
							if(XmlServiceDataFormatter.isCData(currentElement))
								result.append(cdataStart);
							result.append(transformModelToXmlNode((CommandDataModel)model,currentElement,inCallData,callData));
							if(XmlServiceDataFormatter.isCData(currentElement))
								result.append(cdataEnd);
							result.append("</"+nodeTag(currentElement)+">");
							continue;						
						}
						
						if(model == null)
							continue;
						
						Object propValue = Tools.getPropertyValue(model,propName);
						if(propValue == null) 
							continue;						
						
						if(propValue instanceof AbstractType){
							nodeAttr = manageModelToXmlNodeAttr(model,currentElement,inCallData,callData);
							String value = null;
							if(inCallData.dataFormatterClass != null)
								value = callDataFormatter(inCallData.dataFormatterClass,currentElement,((AbstractType)propValue).toString());
							if(value == null){ // No custom formatter								
								if((XmlServiceDataFormatter.isCData(currentElement) && propValue instanceof StringType) ||
								   (XmlServiceDataFormatter.isToString(currentElement))){
									value = propValue.toString();
								}else{
									if(callData.getServiceType() == XmlServiceCallData.OSB_SERVICE_TYPE)
										value = new OSBDataFormatter().manageOutputFormat(currentElement,(AbstractType)propValue);
									else
										value = new QASDataFormatter().manageOutputFormat(currentElement,(AbstractType)propValue);
								}
							}
							
							if (value.length() == 0 && inCallData.isMarkNillable() && isEnabledAttribute(currentElement, TAG_markNillable, true)) {
								// se sull'access mark nillable = true, value.length() == 0 e la gestione del nillable sul campo non è disabilitata aggiungo l'attributo xsi:nil="true"
								nodeAttr += " " + TAG_nillable_true;
							}
							
							String appInfo = currentElement.valueOf(TAG_appInfo);
							if(callData != null && appInfo != null && appInfo.equalsIgnoreCase("true"))
								callData.setInAppinfo(value);

							if(inCallData.isCompactEmpty() && value.length() == 0){
								result.append("<"+nodeTagStart(currentElement, inCallData)+nodeAttr+" />");
								continue;
							}
							result.append("<"+nodeTagStart(currentElement, inCallData)+nodeAttr+">");
							if(XmlServiceDataFormatter.isCData(currentElement))
								result.append(cdataStart);
							result.append(value);
							if(XmlServiceDataFormatter.isCData(currentElement))
								result.append(cdataEnd);
							result.append("</"+nodeTag(currentElement)+">");
							if(value != null)
								inCallData.getMiddleTierInputParameters().append("|"+propName+"="+AbstractAccessLogger.formatParameters(value)+"|");
							continue;
						}
						
						if(propValue instanceof CommandDataModel){
							nodeAttr = manageModelToXmlNodeAttr((CommandDataModel)propValue,currentElement,inCallData,callData);
							result.append("<"+nodeTagStart(currentElement, inCallData)+nodeAttr+">");
							result.append(transformModelToXmlNode((CommandDataModel)propValue,currentElement,inCallData,callData));
							result.append("</"+nodeTag(currentElement)+">");
							continue;
						}
						
						if(propValue instanceof ListType){
							List list = ((ListType)propValue).getElements();
							if(list.size() == 0) {
								if (inCallData.isMarkNillable() && isEnabledAttribute(currentElement, TAG_markNillable, true)) {
									// se sull'access mark nillable = true, list.size() == 0 e la gestione del nillable sul campo non è disabilitata aggiungo l'attributo xsi:nil="true"
									nodeAttr += " " + TAG_nillable_true;
									if(inCallData.isCompactEmpty())
										result.append("<"+nodeTagStart(currentElement, inCallData)+nodeAttr+" />");
									else {
										result.append("<"+nodeTagStart(currentElement, inCallData)+nodeAttr+">");									
										result.append("</"+nodeTag(currentElement)+">");
									}									
								}
								continue;
							}
							 
							if (isEnabledAttribute(currentElement, TAG_listContainer, false)) {
								nodeAttr = manageModelToXmlNodeAttr(model,currentElement,inCallData,callData);
								result.append("<"+nodeTagStart(currentElement, inCallData)+nodeAttr+">");
								result.append(transformModelToXmlNode(model,currentElement,inCallData,callData));
								result.append("</"+nodeTag(currentElement)+">");								
							}
							else {
								for(int j=0;j<list.size();j++){
									CommandDataModel listElement = (CommandDataModel)list.get(j);
									nodeAttr = manageModelToXmlNodeAttr(listElement,currentElement,inCallData,callData);
									result.append("<"+nodeTagStart(currentElement, inCallData)+nodeAttr+">");
									result.append(transformModelToXmlNode(listElement,currentElement,inCallData,callData));
									result.append("</"+nodeTag(currentElement)+">");
								}
							}
							continue;
						}
						break;
						
					default:
						break;
				}
			}
			return result;
			
		}catch(Exception e){
			String errMsg = "XMLServiceCaller.transformModelToXmlNode: Exception in generating service xml request: "+e;
			e = new Exception(errMsg);
			LOG(callData).error(e);
			throw e;
		}
	}
	
	private static boolean isEnabledAttribute(Element el, String attrName, boolean defaultValue) {
		boolean result = defaultValue;
		
		Attribute attr = el.attribute(attrName); 
		if (attr != null) {			
			result = attr.getValue().equalsIgnoreCase("true"); 
		}		
		return result;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private static String manageModelToXmlNodeAttr(CommandDataModel model, Element templateNode, 
												   XMLServiceInnerCallData inCallData, XmlServiceCallData callData){
		String nodeAttr = "";
		try{
			String strAttrs = templateNode.valueOf(TAG_attrs); 
			if(strAttrs == null || strAttrs.length() == 0)
				return nodeAttr;
			
			String[] attrs = strAttrs.split("\\,");
			for(int i=0;i<attrs.length;i++){
				
				String[] propAndAttr = attrs[i].split("\\@");
				if(propAndAttr.length != 2)
					continue;
				
				String propName = propAndAttr[0];
				String attr = propAndAttr[1];
				
				Object propValue = Tools.getPropertyValue(model,propName);
				if(propValue == null)
					continue;
			
				String value = null;
				if(inCallData.dataFormatterClass != null)
					value = callDataFormatter(inCallData.dataFormatterClass,templateNode,((AbstractType)propValue).toString());
				if(value == null){ // No custom formatter
					if((XmlServiceDataFormatter.isToString(templateNode))){
						value = propValue.toString();
					}else{
						if(callData.getServiceType() == XmlServiceCallData.OSB_SERVICE_TYPE)
							value = new OSBDataFormatter().manageOutputFormat(templateNode,(AbstractType)propValue);
						else
							value = new QASDataFormatter().manageOutputFormat(templateNode,(AbstractType)propValue);
					}
				}
				if(value != null)
					nodeAttr += " "+attr+"=\""+value+"\"";
			}
			
		}catch(Exception e){ 
			e.printStackTrace();
			nodeAttr = ""; 
		}
		return nodeAttr;
	}

	/********************************************************************************
	/********************************************************************************/
	private static boolean inSendReceive(ClientSessionContext csc, XmlServiceCallData callData, String wsTraceName) throws Exception{
		boolean result = false;
		String wsType = "QASCALL";
		String url = callData.getUrl();
		if(url == null)
			throw new Exception("URL is not defined");
		if(callData.getServiceType() == XmlServiceCallData.OSB_SERVICE_TYPE){
			if(!url.endsWith("/"))
				url += "/";
			url += callData.getServiceName();
			wsType = "OSBCALL";
		}
		TierAccessLoggerInfo tli = MiddleTierAccessLogger.initTier(csc,wsType,wsTraceName);
		try{
		    callData.setStartTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
			PostMethod post = new PostMethod(url);
		    post.setRequestBody(callData.getXmlSend());
		    post.setRequestContentLength(callData.getXmlSend().length());
		    if(callData.getServiceType() == XmlServiceCallData.OSB_SERVICE_TYPE){
			    post.setRequestHeader("Content-Type", callData.getContentType() == null ? "text/xml" : callData.getContentType());
				post.setRequestHeader("SOAPAction","");
		    }else
		    	post.setRequestHeader("Content-type", callData.getContentType() == null ? "application/x-ofx" : callData.getContentType());
		    HttpClient httpclient = new HttpClient();
		    LOG(callData).debug("XMLServiceCaller: sending XML ["+callData.getXmlSend()+"]");
		    httpclient.executeMethod(post);
		    callData.setEndTime(new Timestamp(Calendar.getInstance().getTime().getTime()));
		    callData.setHttpStatus(post.getStatusCode());
		    if(post.getStatusCode() == HttpStatus.SC_OK){
			    callData.setStatus(XmlServiceCallData.STATUS_OK);
			    callData.setXmlReceive(post.getResponseBodyAsString());
			    LOG(callData).debug("XMLServiceCaller: received XML ["+callData.getXmlReceive()+"]");
			    result = true;
		    }else{
			    callData.setXmlReceive(post.getResponseBodyAsString());
			    callData.setStatus(XmlServiceCallData.STATUS_HTTP_POST_KO);
			    LOG(callData).debug("XMLServiceCaller: Http error ["+post.getStatusCode()+"]");
		    }
		    if( (callData.resultCharsetName == null || callData.resultCharsetName.length() == 0) && 
			    (post.getResponseCharSet() != null && post.getResponseCharSet().length() > 0))
			    callData.resultCharsetName = post.getResponseCharSet();
		    post.releaseConnection();
			tli.stop(new StringBuffer(url+"||statusCode="+callData.getHttpStatus()));
		    return result;
		}catch(Exception e){
			String errMsg = "XMLServiceCaller.inSendReceive: Exception in service request/callData: "+e.toString();
			e = new Exception(errMsg);
			LOG(callData).error(e);
		    callData.setStatus(XmlServiceCallData.STATUS_EXCEPTION_ON_CALL);
			callData.setMessage(errMsg);
			tli.exception();
			tli.stop(new StringBuffer(url+"||statusCode="+callData.getHttpStatus()));
			return false;
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static void loadModelFromXml(CommandDataModel model, String xmlTemplate, String xmlData, Class dataFormatterClass) throws Exception{
		XmlServiceCallData callData = new QASCallData();
		callData.setXmlReceive(xmlData);
		callData.dataFormatterClass = dataFormatterClass;
		loadModelFromXml(callData,model,xmlTemplate);
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private static Element getReceivedXmlRoot(XmlServiceCallData callData) throws Exception{
		String xmlData = callData.getXmlReceive();
		if(xmlData == null || xmlData.length() == 0)
			return null;
		SAXReader saxReader = new SAXReader();
		
		ByteArrayInputStream isXmlData = null;
		if(callData.resultCharsetName == null || callData.resultCharsetName.length() == 0)
			isXmlData = new ByteArrayInputStream(xmlData.getBytes());
		else
			isXmlData = new ByteArrayInputStream(xmlData.getBytes(callData.resultCharsetName));
		org.dom4j.Document xmlDataDoc = saxReader.read(new org.xml.sax.InputSource(isXmlData));
		return xmlDataDoc.getRootElement();
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private static void loadModelFromXml(XmlServiceCallData callData, CommandDataModel outputModel, String xmlTemplate) throws Exception{
		
		try{
			
			Map pathMap = new Hashtable();
			if(xmlTemplate != null){
				SAXReader saxReader = new SAXReader();
				ByteArrayInputStream isXmlTemplate = new ByteArrayInputStream(xmlTemplate.getBytes());		
				org.dom4j.Document xmlTemplateDoc = saxReader.read(new org.xml.sax.InputSource(isXmlTemplate));
				Element xmlTemplateRoot = xmlTemplateDoc.getRootElement();
				loadPathMap(callData,pathMap,xmlTemplateRoot,xmlTemplateRoot.getName());
			}
			
			Element xmlRoot = getReceivedXmlRoot(callData);
			if(xmlRoot == null)
				return;
			
			findErrorInReceivedXml(callData,xmlRoot);
			
			Map emptyMapModelMap = new HashMap();
			transformXmlNodeToModel(callData,outputModel,pathMap,xmlRoot,xmlRoot.getName(),emptyMapModelMap);
			return;
			
		}catch(Exception e){
			String errMsg = "XMLServiceCaller.loadModelFromXml: Exception in generating model from service xml response: "+e;
			e = new Exception(errMsg);
			LOG(callData).error(e);
			throw e;
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private static void loadPathMap(XmlServiceCallData callData, Map map, Element node, String nodePath) throws Exception{
		try{
			if(node.getNodeType() == Node.ELEMENT_NODE){
				String propName = node.valueOf(TAG_propertyName);
				String attrs = node.valueOf(TAG_attrs);
				if((propName != null && propName.length() > 0) || (attrs != null && attrs.length() > 0))
					map.put(nodePath,node); 		//	map.put(node.getPath(),node);
			}
			Iterator it = node.content().iterator();
			while(it.hasNext()){
				Node currentNode = (Node) it.next();
				if(currentNode.getNodeType() != Node.ELEMENT_NODE)
					continue;
				loadPathMap(callData, map,(Element)currentNode,nodePath+"/"+currentNode.getName());
			}
		}catch(Exception e){
			String errMsg = "XMLServiceCaller.loadPathMap: Exception loading paths map: "+e;
			e = new Exception(errMsg);
			LOG(callData).error(e);
			throw e;
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private static void transformXmlNodeToModel(XmlServiceCallData callData, CommandDataModel model, 
												Map pathMap, Element node, String nodePath, Map emptyMapModelMap) throws Exception{
		
		if(model == null)
			return;
		
		Iterator it = node.content().iterator();
		try{
			
			while(it.hasNext()){
				
				Node currentNode = (Node)it.next();
				if(currentNode.getNodeType() != Node.ELEMENT_NODE)
					continue;

				String curPath = nodePath+"/"+currentNode.getName();
				
				String propertyName = null;
				Node templateNode = (Node)pathMap.get(curPath);		// Node templateNode = (Node)pathMap.get(currentNode.getPath());
				if(templateNode != null){
					propertyName = templateNode.valueOf(TAG_propertyName);
					if(propertyName == null || propertyName.length() == 0)
						manageXmlToModelNodeAttr(callData,model,templateNode,currentNode);
				}
				
				if(propertyName == null || propertyName.length() == 0){
					transformXmlNodeToModel(callData,model,pathMap,(Element)currentNode,curPath,emptyMapModelMap);
					continue;
				}
				
				if(model instanceof MapCommandDataModel){
					AbstractType mapPropertyValue = new StringType(); // Default type for MapCommandDataModel
					String mapPropertyType = templateNode.valueOf(TAG_propertyType);
					if(mapPropertyType != null && mapPropertyType.length() > 0)
						mapPropertyValue = AbstractType.newInstance(Class.forName("com.atosorigin.wfem.types."+mapPropertyType),"");
					((MapCommandDataModel)model).addProperty(propertyName,mapPropertyValue);
				}
				
				Class propClass = Tools.getPropertyType(model, propertyName);
				if(propClass == null)
					throw new Exception("XMLServiceCaller.transformXmlNodeToModel: Property type for propertyName ["+propertyName+"] is null.");
				
				if(AbstractType.class.isAssignableFrom(propClass)){
					manageXmlToModelNodeAttr(callData,model,templateNode,currentNode);
					try{
						String propValue = null;
						if(callData.dataFormatterClass != null)
							propValue = callDataFormatter(callData.dataFormatterClass, templateNode, currentNode.getText());
						
						if(propValue == null){ // No custom format
							
							propValue = currentNode.getText();
							
							String asXml = "false";
							if(templateNode != null)
								asXml = templateNode.valueOf(TAG_asXml);
							
							if(asXml.equalsIgnoreCase("true") && propClass.equals(StringType.class)){ // Node as xml value. Valid only on StringType properties
								
								propValue = currentNode.asXML();
								try{ // try to load child
									Node n = (Node)((Element)currentNode).content().iterator().next();
									propValue = n.asXML();
								}catch(Exception e){}
								
							}

							if(callData.getServiceType() == XmlServiceCallData.OSB_SERVICE_TYPE)
								propValue = new OSBDataFormatter().manageInputFormat(templateNode, propValue, propClass);
							else
								propValue = new QASDataFormatter().manageInputFormat(templateNode, propValue, propClass);
								
						}

						String appInfo = templateNode.valueOf(TAG_appInfo);
						if(appInfo != null && appInfo.equalsIgnoreCase("true"))
							callData.setOutAppinfo(propValue);
						
						AbstractType modelValue = (AbstractType)Tools.getPropertyValue(model,propertyName);
						if(modelValue != null)
							modelValue.setStringValue(propValue);
						else
							modelValue = AbstractType.newInstance(propClass,propValue);
						Tools.setPropertyValue(model,propertyName,modelValue);
					}catch(Exception e){
						LOG(callData).warning("XMLServiceCaller.transformXmlNodeToModel: Problems in setting property ["+propertyName+"]");
					}
					continue;
				}
				
				if(CommandDataModel.class.isAssignableFrom(propClass)){
					try{
						CommandDataModel innerModel = (CommandDataModel)propClass.newInstance();
						
						if(innerModel instanceof MapCommandDataModel) {
							if(emptyMapModelMap.get(templateNode) != null)
								innerModel = (MapCommandDataModel)Tools.cloneObject(emptyMapModelMap.get(templateNode));
							else
								fillNullMappedProps((MapCommandDataModel)innerModel,pathMap,curPath,templateNode,emptyMapModelMap);
						}
						
						manageXmlToModelNodeAttr(callData,innerModel,templateNode,currentNode);
						transformXmlNodeToModel(callData,innerModel,pathMap,(Element)currentNode,curPath,emptyMapModelMap); 					
						
						Tools.setPropertyValue(model,propertyName,innerModel);
					}catch(Exception e){
						LOG(callData).warning("XMLServiceCaller.transformXmlNodeToModel: Problems in setting model ["+propClass+"] of property ["+propertyName+"]");
					}
					continue;
				}
				if(ListType.class.isAssignableFrom(propClass)){
					Class modelClass = null;
					try{
						ListType list = (ListType)Tools.getPropertyValue(model,propertyName);
						modelClass = list.getModelType();
						CommandDataModel element = (CommandDataModel)modelClass.newInstance();
						
						if(element instanceof MapCommandDataModel) {
							if(emptyMapModelMap.get(templateNode) != null)
								element = (MapCommandDataModel)Tools.cloneObject(emptyMapModelMap.get(templateNode));
							else
								fillNullMappedProps((MapCommandDataModel)element,pathMap,curPath,templateNode,emptyMapModelMap);
						}
						
						manageXmlToModelNodeAttr(callData,element,templateNode,currentNode);
						transformXmlNodeToModel(callData,element,pathMap,(Element)currentNode,curPath,emptyMapModelMap);

						list.add(element);
					}catch(Exception e){
						LOG(callData).warning("XMLServiceCaller.transformXmlNodeToModel: Problems in setting model ["+modelClass+"] of list property ["+propertyName+"]");
					}
					continue;
				}
			}
			return;
			
		}catch(Exception e){
			String errMsg = "XMLServiceCaller.transformXmlNodeToModel: Exception in generating model from service xml response: "+e;
			e = new Exception(errMsg);
			LOG(callData).error(e);
			throw e;
		}
	}

	/********************************************************************************/
	/********************************************************************************/
	private static void fillNullMappedProps(MapCommandDataModel mapModel, Map pathMap, String nodePath, Node templateNode, Map emptyMapModelMap){
		try{
			
		    java.util.Iterator kit = pathMap.keySet().iterator();
		    while (kit.hasNext()) {
		        String mapkey = (String) kit.next();
		        if(mapkey.length() > nodePath.length() && mapkey.startsWith(nodePath)){
		        	Node n = (Node)pathMap.get(mapkey);
					String mapPropertyName = n.valueOf(TAG_propertyName);
					if(mapPropertyName == null || mapPropertyName.length() == 0)
						continue;
					String mapPropertyType = n.valueOf(TAG_propertyType);
					if(mapPropertyType != null && mapPropertyType.length() > 0){
						mapModel.addProperty(mapPropertyName, AbstractType.newInstance(Class.forName("com.atosorigin.wfem.types."+mapPropertyType),""));
					}
		        	
		        }
		    }
		    emptyMapModelMap.put(templateNode, Tools.cloneObject(mapModel));
		}catch(Throwable t){
			t.printStackTrace();
		}
	}

	/********************************************************************************/
	/********************************************************************************/
	private static void manageXmlToModelNodeAttr(XmlServiceCallData callData, CommandDataModel model,  
												 Node templateNode, Node currentNode){
		try{
			String strAttrs = templateNode.valueOf(TAG_attrs); 
			if(strAttrs == null || strAttrs.length() == 0)
				return;
			
			String[] attrs = strAttrs.split("\\,");
			for(int i=0;i<attrs.length;i++){
				
				String[] propAndAttr = attrs[i].split("\\@");
				if(propAndAttr.length != 2 && propAndAttr.length != 3)
					continue;
				
				String propName = propAndAttr[0];
				String attr = propAndAttr[1];
				String mapPropertyType = null;
				if(propAndAttr.length == 3)
					mapPropertyType = propAndAttr[2];
				
				String propValue = "";
				if(attr.equals(TAG_attrs_value_indicator))
					propValue = currentNode.getText();
				else
					propValue = currentNode.valueOf("@"+attr);
				
				if(model instanceof MapCommandDataModel){
					AbstractType mapPropertyValue = new StringType(); // Default type for MapCommandDataModel
					if(mapPropertyType != null && mapPropertyType.length() > 0)
						mapPropertyValue = AbstractType.newInstance(Class.forName("com.atosorigin.wfem.types."+mapPropertyType),"");
					((MapCommandDataModel)model).addProperty(propName,mapPropertyValue);
				}
				
				Class propClass = Tools.getPropertyType(model,propName);
				if(propClass == null){
					LOG(callData).warning("XMLServiceCaller.manageXmlToModelNodeAttr: Property type for attrs=propertyName ["+propName+"] is null.");
					continue;
				}
				
				if(AbstractType.class.isAssignableFrom(propClass)){
					if(callData.getServiceType() == XmlServiceCallData.OSB_SERVICE_TYPE)
						propValue = new OSBDataFormatter().manageInputFormat(templateNode, propValue, propClass);
					else
						propValue = new QASDataFormatter().manageInputFormat(templateNode, propValue, propClass);
					AbstractType modelValue = (AbstractType)Tools.getPropertyValue(model,propName);
					if(modelValue != null)
						modelValue.setStringValue(propValue);
					else
						modelValue = AbstractType.newInstance(propClass,propValue);
					Tools.setPropertyValue(model,propName,modelValue);
				}								
			}
		}catch(Exception e){
			e.printStackTrace();			
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private static String callDataFormatter(Class dataFormatterClass, 
											Node currentNode, String value){
		String methodName = currentNode.valueOf(TAG_dataFormatter);
		if(methodName == null || methodName.length() == 0)
			return null;
		try{
			Method m = dataFormatterClass.getDeclaredMethod(methodName,new Class[]{String.class});
			return (String)m.invoke(null, new Object[]{value});
		}catch(Exception e){
			LOG().warning("XMLServiceCaller.callDataFormatter: Problems in formatting ["+value+"] with dataFormatter ["+dataFormatterClass.getName()+"] on method ["+methodName+"]");
			return value;
		}
	}
	
	/*****************************************************************************************/
	/*****************************************************************************************/
	private static String findServiceName(XmlServiceCallData callData){
		try{
			if(callData.getServiceType() == XmlServiceCallData.OSB_SERVICE_TYPE){
				String url = callData.getUrl().toString();
				String serviceName = url.substring(url.lastIndexOf("/")+1);
				String wsUrl = url.substring(0,url.lastIndexOf("/"));
				callData.setUrl(wsUrl);
				return serviceName;
			}else{
				Document xmldoc = null;
				String xml = callData.getXmlSend();
				SAXReader myreader = new SAXReader();
				try{
					ByteArrayInputStream isXmlTemplate = new ByteArrayInputStream(xml.getBytes());
					xmldoc = myreader.read(new org.xml.sax.InputSource(isXmlTemplate));
				}catch(DocumentException de){
					ByteArrayInputStream isXmlTemplate = new ByteArrayInputStream(xml.getBytes("UTF-8"));
					xmldoc = myreader.read(new org.xml.sax.InputSource(isXmlTemplate));
				}
				Element root = xmldoc.getRootElement();
				Iterator it = root.content().iterator();
				while(it.hasNext()){
					Node currentNode = (Node) it.next();
					if(currentNode.getNodeType() != Node.ELEMENT_NODE)
						continue;
					String serviceName = currentNode.getName();
					if(serviceName.length() > QARC_CALL_SUFFIX.length())
						serviceName = serviceName.substring(0,serviceName.length()-QARC_CALL_SUFFIX.length());
					return serviceName;
				}			
				return null;
			}
		}catch(Exception e){
			return null;
		}
	}
	

}
