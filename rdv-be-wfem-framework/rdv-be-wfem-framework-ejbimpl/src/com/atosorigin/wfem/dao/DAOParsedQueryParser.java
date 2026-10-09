package com.atosorigin.wfem.dao;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.dom4j.Element;
import org.dom4j.Node;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
class DAOParsedQueryParser {

	private DAOAccessParameters accessParameters;
	private String sqlString;

	private ClientSessionContext csc;
	private String listPropertyName = "";
	private CommandDataModel dataModel;
	private DAOQueryAccessInfo queryAccessinfo;

	public static final String PAR_TAG = "p";
	public static final String PAR_LIST_TAG = "l";
	public static final String PAR_CONST_TAG = "c";

	public static final String PAR_REPEAT_TAG = "Repeat";
	public static final String IFNULL_TAG = "IfNull";
	public static final String IFNOTNULL_TAG = "IfNotNull";
	public static final String IFEQUAL_TAG = "IfEqual";
	public static final String IFNOTEQUAL_TAG = "IfNotEqual";
	public static final String IFGREATERTHAN_TAG = "IfGreaterThan";
	public static final String IFLESSTHAN_TAG = "IfLessThan";
	public static final String IFIN_TAG = "IfIn";
	public static final String IFNOTIN_TAG = "IfNotIn";
	public static final String TOSTRING_TAG = "ToString";

	private static final String STMT_PAR = " ? ";

	/********************************************************************************
	/********************************************************************************/
	private DAOParsedQueryParser(ClientSessionContext csc, DAOQueryAccessInfo queryAccessinfo, 
								 CommandDataModel dataModel) {

		setCsc(csc);
		setQueryAccessinfo(queryAccessinfo);
		setDataModel(dataModel);
		setAccessParameters(new DAOAccessParameters());
		setSqlString("");

	}

	/********************************************************************************
	/********************************************************************************/
	public static DAOParsedQueryParser parse(ClientSessionContext csc, DAOQueryAccessInfo queryAccessinfo, 
											 CommandDataModel dataModel)
		throws ParseException {

		DAOParsedQueryParser newParser = new DAOParsedQueryParser(csc, queryAccessinfo, dataModel);
		newParser.parseQuery();
		return newParser;
	}

	/********************************************************************************
	/********************************************************************************/
	private void parseQuery() throws ParseException {
		setSqlString(processBranch(getQueryAccessinfo().getSqlNodeString()));
	}

	/********************************************************************************
	/********************************************************************************/
	private String processBranch(Element node) throws ParseException {
		
		String sqlPart = "";
		Iterator it = node.content().iterator();
		Node currentNode;
		while (it.hasNext()) {
			currentNode = (Node) it.next();
			if (currentNode.getNodeType() == Node.TEXT_NODE || currentNode.getNodeType() == Node.CDATA_SECTION_NODE) {
				sqlPart += currentNode.getText();
			}else if(currentNode.getNodeType() == Node.ELEMENT_NODE){
				
				Element el = (Element)currentNode;
				if(el.getName().equalsIgnoreCase(IFNULL_TAG)){
					sqlPart += processIfNullTag(el);
				}else if(el.getName().equalsIgnoreCase(IFNOTNULL_TAG)){
					sqlPart += processIfNotNullTag(el);
				}else if(el.getName().equalsIgnoreCase(IFEQUAL_TAG)){
					sqlPart += processIfEqualTag(el);
				}else if(el.getName().equalsIgnoreCase(IFNOTEQUAL_TAG)){
					sqlPart += processIfNotEqualTag(el);
				}else if(el.getName().equalsIgnoreCase(IFGREATERTHAN_TAG)){
					sqlPart += processIfGreaterThanTag(el);
				}else if(el.getName().equalsIgnoreCase(IFLESSTHAN_TAG)){
					sqlPart += processIfLessThanTag(el);
				}else if(el.getName().equalsIgnoreCase(IFIN_TAG)){
					sqlPart += processIfInTag(el);
				}else if(el.getName().equalsIgnoreCase(IFNOTIN_TAG)){
					sqlPart += processNotInTag(el);
				}else if(el.getName().equalsIgnoreCase(PAR_REPEAT_TAG)){
					sqlPart += processRepeatParTag(el);
				}else if(el.getName().equalsIgnoreCase(PAR_CONST_TAG)){
					sqlPart += processParConstTag(el);
				}else if(el.getName().equalsIgnoreCase(PAR_TAG)){
					sqlPart += processParTag(el);
				}else if(el.getName().equalsIgnoreCase(PAR_LIST_TAG)){
					sqlPart += processListParTag(el);
				}else if(el.getName().equalsIgnoreCase(TOSTRING_TAG)){
					sqlPart += processToStringTag(el);
				}else{
					throw new ParseException("processBranch: tag ["+el.getName()+"] not supported in branch ["+node.getName()+"]",sqlPart.length());
				}
				
			}else{
				throw new ParseException("processBranch: node ["+currentNode.getName()+"] not supported in branch ["+node.getName()+"]",sqlPart.length());
			}		
		}
		
		return sqlPart;
	}
	
	/********************************************************************************
	/********************************************************************************/
	private String processParConstTag(Element node) throws ParseException {
		try{
			String propName = node.getText();
			String className = propName.substring(0,propName.lastIndexOf('.'));
			propName = propName.substring(propName.lastIndexOf('.')+1);
			Class c = Class.forName(className);
			Field f = c.getDeclaredField(propName);
			String propValue = f.get(null).toString();
			return propValue;
		}catch(Exception e){
			throw new ParseException(e.toString(),node.nodeCount()); 
		}
	}

	/********************************************************************************
	/********************************************************************************/
	private String processParTag(Element node) throws ParseException {
		DAOAccessParameter newAccessParameter = new DAOAccessParameter();
		String propName = node.getText();
		String propValue = DAOAccessParameters.processCscVariables(csc,propName,"");
		if(propValue != null){
			newAccessParameter.setPropertyType(StringType.class.getName());
			newAccessParameter.setPropertyValue(propValue);
		}
		newAccessParameter.setPropertyName(propName);
		getAccessParameters().addParameter(newAccessParameter);
		return STMT_PAR;
	}

	/********************************************************************************
	/********************************************************************************/
	private String processListParTag(Element node) throws ParseException {
		
		DAOAccessParameter newAccessParameter = new DAOAccessParameter();
		newAccessParameter.setPropertyName(getListPropertyName()+node.getText());
		getAccessParameters().addParameter(newAccessParameter);

		return STMT_PAR;
	}

	/********************************************************************************
	/********************************************************************************/
	private String processToStringTag(Element node) throws ParseException {
		
		String propName = node.getText();
		try{
			String propValue = "";
			AbstractType prop = null;
			if(getDataModel() != null)
				prop = (AbstractType)Tools.getPropertyValue(getDataModel(),propName);
			if(prop == null){
				propValue = DAOAccessParameters.processCscVariables(csc,propName,"");
				if(propValue == null)
					propValue = "";
			}else{
				propValue = Tools.getPropertyValue(getDataModel(),propName).toString();
			}
			return propValue;
		}catch(Exception e){
			return propName;
		}
	}

	/********************************************************************************
	/********************************************************************************/
	private String processRepeatParTag(Element node) throws ParseException {

		String sqlPart = "";
		String listPropertyName = node.valueOf("@propertyName");
		String rowSeparator = node.valueOf("@separator");
		String before = node.valueOf("@before");
		String after = node.valueOf("@after");
		String onEmpty = node.valueOf("@onEmpty");

		try{		

			ListType list = (ListType)Tools.getPropertyValue(getDataModel(),getListPropertyName()+listPropertyName);
			boolean emptyRepeat = true;			
			for(int i=0;i<list.size();i++){
				
				String curPropName = getListPropertyName();
				setListPropertyName(getListPropertyName()+listPropertyName+i+"_");
				
				String sqlRowPart = processBranch(node);
				boolean isEmpty = isEmpty(sqlRowPart);
				if(i < list.size()-1 && !isEmpty)
					sqlRowPart += rowSeparator;
					
				if(!isEmpty){
					sqlPart += sqlRowPart;
					emptyRepeat = false;
				}

				setListPropertyName(curPropName);				
			}
			if(emptyRepeat){
				 if(onEmpty != null && !onEmpty.equals("")){
				 	sqlPart += onEmpty;
				 }
			}else{
				 if(before != null && !before.equals("")){
				 	sqlPart = before + sqlPart;
				 }
				 if(after != null && !after.equals("")){
				 	sqlPart += after;
				 }
			}
			return sqlPart;
			
		}
		catch (Exception e) {
			throw new ParseException(
				"process ["+PAR_REPEAT_TAG+"] tag: evaluation property ["
					+ listPropertyName
					+ "] in dataModel ["
					+ getDataModel()
					+ "] throws exception ["
					+ e +"]",node.nodeCount());
		}
		
	}

	/********************************************************************************
	/********************************************************************************/
	private String processIfNullTag(Element node) throws ParseException {

		AbstractType propertyValue = getPropertyValue(node,IFNULL_TAG);
		if(propertyValue == null || propertyValue.isNull())
			return processBranch(node);
		
		return "";
	}
	
	/********************************************************************************
	/********************************************************************************/
	private String processIfNotNullTag(Element node) throws ParseException {

		AbstractType propertyValue = getPropertyValue(node,IFNOTNULL_TAG);
		if(propertyValue != null && !propertyValue.isNull())
			return processBranch(node);
		
		return "";
	}

	/********************************************************************************
	/********************************************************************************/
	private String processIfEqualTag(Element node) throws ParseException {

		AbstractType propertyValue = getPropertyValue(node,IFEQUAL_TAG);
		AbstractType propertyEQValue = getCompareToPropertyValue(node,propertyValue.getClass(),IFEQUAL_TAG);
			
		if(propertyValue != null && propertyValue.equals(propertyEQValue))
			return processBranch(node);
			
		return "";
	}
	
	/********************************************************************************
	/********************************************************************************/
	private String processIfNotEqualTag(Element node) throws ParseException {

		AbstractType propertyValue = getPropertyValue(node,IFNOTEQUAL_TAG);
		AbstractType propertyNEQValue = getCompareToPropertyValue(node,propertyValue.getClass(),IFNOTEQUAL_TAG);

		if(propertyValue != null && !propertyValue.equals(propertyNEQValue))
			return processBranch(node);
			
		return "";
	}
	
	/********************************************************************************
	/********************************************************************************/
	private String processIfGreaterThanTag(Element node) throws ParseException {

		AbstractType propertyValue = getPropertyValue(node,IFGREATERTHAN_TAG);
		AbstractType propertyGTValue = getCompareToPropertyValue(node,propertyValue.getClass(),IFGREATERTHAN_TAG);

		if(propertyValue != null && propertyValue.compareTo(propertyGTValue) > 0)
			return processBranch(node);
			
		return "";
	}
	
	/********************************************************************************
	/********************************************************************************/
	private String processIfLessThanTag(Element node) throws ParseException {

		AbstractType propertyValue = getPropertyValue(node,IFLESSTHAN_TAG);
		AbstractType propertyLTValue = getCompareToPropertyValue(node,propertyValue.getClass(),IFLESSTHAN_TAG);
		
		if(propertyValue != null && propertyValue.compareTo(propertyLTValue) < 0)
			return processBranch(node);
		
		return "";
	}
	
	/********************************************************************************
	/********************************************************************************/
	private String processIfInTag(Element node) throws ParseException {

		AbstractType propertyValue = getPropertyValue(node,IFIN_TAG);
		if(propertyValue == null)
			return "";

		String propertyInStringValue = node.valueOf("@propertyValue");
		if (propertyInStringValue == null || propertyInStringValue.equals(""))
			throw new ParseException("process "+IFIN_TAG+" tag: conditional property not found.", node.nodeCount());

		boolean founded = false;		
		StringTokenizer st = new StringTokenizer(propertyInStringValue,"|");
		while(st.hasMoreTokens()){
			String tok = st.nextToken();
			AbstractType propertyINValue = getCompareToPropertyValue(node,tok,propertyValue.getClass(),IFIN_TAG);
			if(propertyValue.equals(propertyINValue)){
				founded = true;
				break;
			}
		}

		if(founded)			
			return processBranch(node);
			
		return "";
	}
	
	/********************************************************************************
	/********************************************************************************/
	private String processNotInTag(Element node) throws ParseException {

		AbstractType propertyValue = getPropertyValue(node,IFNOTIN_TAG);
		if(propertyValue == null)
			return "";

		String propertyNotInStringValue = node.valueOf("@propertyValue");
		if (propertyNotInStringValue == null || propertyNotInStringValue.equals(""))
			throw new ParseException("process "+IFNOTIN_TAG+" tag: conditional property not found.", node.nodeCount());

		boolean founded = false;		
		StringTokenizer st = new StringTokenizer(propertyNotInStringValue,"|");
		while(st.hasMoreTokens()){
			String tok = st.nextToken();
			AbstractType propertyINValue = getCompareToPropertyValue(node,tok,propertyValue.getClass(),IFNOTIN_TAG);
			if(propertyValue.equals(propertyINValue)){
				founded = true;
				break;
			}
		}

		if(!founded)			
			return processBranch(node);
			
		return "";
	}
	
	/********************************************************************************
	/********************************************************************************/
	private AbstractType getPropertyValue(Element node, String tagName) throws ParseException {

		boolean listPropertyName = false;		
		String propertyName = node.valueOf("@propertyName");
		if(propertyName == null || propertyName.equals("")){
			listPropertyName = true;
			propertyName = node.valueOf("@listPropertyName");
		}

		if(propertyName == null || propertyName.equals(""))
			throw new ParseException("process "+tagName+" tag: property not found.", node.nodeCount());
			
		AbstractType propertyValue;
		try {
			if(listPropertyName)
				propertyValue = (AbstractType) Tools.getPropertyValue(getDataModel(),getListPropertyName()+propertyName);
			else
				propertyValue = (AbstractType) Tools.getPropertyValue(getDataModel(),propertyName);
			return propertyValue;
		}catch (Exception e) {
			throw new ParseException(
				    "process ["+tagName+"] tag: evaluation property ["
					+ propertyName
					+ "] in dataModel ["
					+ getDataModel()
					+ "] throws exception ["
					+ e +"]",node.nodeCount());
		}
	}
		
	/********************************************************************************
	/********************************************************************************/
	private AbstractType getCompareToPropertyValue(Element node, 
													Class valueClass, 
													String tagName) throws ParseException {
		
		String propertyStringValue = node.valueOf("@propertyValue");
		if (propertyStringValue == null || propertyStringValue.equals(""))
			throw new ParseException("process "+tagName+" tag: conditional property not found.", node.nodeCount());

		AbstractType propertyValue;
		try {
			propertyValue = AbstractType.newInstance(valueClass,propertyStringValue);
			return propertyValue;
		}catch (Exception e) {
			throw new ParseException(
				    "process ["+tagName+"] tag: evaluating compare property ["
					+ propertyStringValue
					+ "] in dataModel ["
					+ getDataModel()
					+ "] throws exception ["
					+ e +"]",node.nodeCount());
		}
	}
		
	/********************************************************************************
	/********************************************************************************/
	private AbstractType getCompareToPropertyValue(Element node, 
	 												String compareToValue, 
													Class valueClass, 
													String tagName) throws ParseException {
		
		AbstractType propertyValue;
		try {
			propertyValue = AbstractType.newInstance(valueClass,compareToValue);
			return propertyValue;
		}catch (Exception e) {
			throw new ParseException(
				    "process ["+tagName+"] tag: evaluating compare property ["
					+ compareToValue
					+ "] in dataModel ["
					+ getDataModel()
					+ "] throws exception ["
					+ e +"]",node.nodeCount());
		}
	}
		
	/********************************************************************************
	/********************************************************************************/
	private boolean isEmpty(String sqlStr) {
		sqlStr = sqlStr.replace('\n',' ');
		sqlStr = sqlStr.replace('\r',' ');
		sqlStr = sqlStr.replace('\t',' ');
		char[] ca = sqlStr.toCharArray();
		for(int i=0;i<ca.length;i++){
			if(ca[i] != ' ' && ca[i] != '\t' && ca[i] != '\r')
				return false;
		}
		return true;
	}
	
	/********************************************************************************
	/********************************************************************************/
	protected DAOAccessParameters getAccessParameters() {
		return accessParameters;
	}

	/********************************************************************************
	/********************************************************************************/
	private CommandDataModel getDataModel() {
		return dataModel;
	}

	/********************************************************************************
	/********************************************************************************/
	private DAOQueryAccessInfo getQueryAccessinfo() {
		return queryAccessinfo;
	}

	/********************************************************************************
	/********************************************************************************/
	protected String getSqlString() {
      	if(sqlString != null){
			sqlString = sqlString.replace('\n',' ');
			sqlString = sqlString.replace('\r',' ');
			sqlString = sqlString.replace('\t',' ');
      	}
		return sqlString;
	}

	/********************************************************************************
	/********************************************************************************/
	private void setAccessParameters(DAOAccessParameters accessParameters) {
		this.accessParameters = accessParameters;
	}

	/********************************************************************************
	/********************************************************************************/
	private void setDataModel(CommandDataModel dataModel) {
		this.dataModel = dataModel;
	}

	/********************************************************************************
	/********************************************************************************/
	private void setQueryAccessinfo(DAOQueryAccessInfo queryAccessinfo) {
		this.queryAccessinfo = queryAccessinfo;
	}

	/********************************************************************************
	/********************************************************************************/
	private void setSqlString(String sqlString) {
		this.sqlString = sqlString;
	}

	/********************************************************************************
	/********************************************************************************/
	private String getListPropertyName() {
		return listPropertyName;
	}

	/********************************************************************************
	/********************************************************************************/
	private void setListPropertyName(String listPropertyName) {
		this.listPropertyName = listPropertyName;
	}

	/********************************************************************************
	/********************************************************************************/
	public ClientSessionContext getCsc() {
		return csc;
	}

	/********************************************************************************
	/********************************************************************************/
	public void setCsc(ClientSessionContext csc) {
		this.csc = csc;
	}

}
