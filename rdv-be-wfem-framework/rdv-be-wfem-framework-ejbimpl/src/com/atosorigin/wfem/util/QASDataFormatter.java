package com.atosorigin.wfem.util;

import java.math.BigDecimal;
import java.text.NumberFormat;

import org.dom4j.Element;
import org.dom4j.Node;

import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class QASDataFormatter extends XmlServiceDataFormatter {

	/********************************************************************************/
	/********************************************************************************/
	protected String manageOutputFormat(Element el, AbstractType propValue) {
		String value = propValue.toString();
		if(propValue instanceof StringType)
			value = manageOutStringFormat(el,(StringType)propValue);
		else if(propValue instanceof DateType)
			value = manageOutDateFormat(el,(DateType)propValue);
		else if(propValue instanceof TimestampType)
			value = manageOutTimestampFormat(el,(TimestampType)propValue);
		else if(propValue instanceof DoubleType)
			value = manageOutDoubleFormat(el,(DoubleType)propValue);
		else if(propValue instanceof BooleanType){
			value = manageOutBooleanFormat(el,(BooleanType)propValue);
		}
		value = manageValueFormat(el,value);
		return value;
	}

	/********************************************************************************/
	/********************************************************************************/
	private String manageOutStringFormat(Node currentNode, StringType value){
		return Tools.stringToXMLString(value.toString());
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private String manageOutDateFormat(Node currentNode, DateType value){
		try{
			String format = currentNode.valueOf(TAG_format);
			if(format == null || format.length() == 0)
				return value.toString();
			
			int idxYY = -1;
			int idxYYYY = format.indexOf("YYYY");
			if(idxYYYY < 0)
				idxYY = format.indexOf("YY");
			String year = value.getAA();
			String month = value.getMM();
			String day = value.getGG();
			String result = format.toString();
			result = result.replaceAll("DD",day);
			result = result.replaceAll("MM",month);
			if(idxYY >= 0){
				result = result.replaceAll("YY",year.substring(2));				
			}else{
				result = result.replaceAll("YYYY",year);				
			}
			return result;
		}catch(Exception e){
			return "";
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private String manageOutTimestampFormat(Node currentNode, TimestampType value){
		return manageOutDateFormat(currentNode, value.toDateType());
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private String manageOutDoubleFormat(Node currentNode, DoubleType value){
		if(value.toString().length() > 0)
			return value.bigValue().setScale(2, BigDecimal.ROUND_HALF_UP).toString();
		else
			return value.toString();
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private String manageOutBooleanFormat(Node currentNode, BooleanType value){
		return value.booleanValue() ? "S" : "N";
	}
	
	/********************************************************************************/
	/********************************************************************************/
	/********************************************************************************/
	/********************************************************************************/
	
	/********************************************************************************/
	/********************************************************************************/
	protected String manageInputFormat(Node templateNode, String propValue, Class propClass) {
		if(propClass.equals(StringType.class)){
			propValue = manageInStringFormat(templateNode,propValue);							
		}else if(propClass.equals(DateType.class))
			propValue = manageInDateFormat(templateNode,propValue);
		else if(propClass.equals(TimestampType.class))
			propValue = manageInTimestampFormat(templateNode,propValue);
		else if(propClass.equals(DoubleType.class))
			propValue = manageInDoubleFormat(templateNode,propValue);
		else if(propClass.equals(BooleanType.class)){
			propValue = manageInBooleanFormat(templateNode,propValue);
		}
		propValue = manageValueFormat(templateNode,propValue);
		return propValue;
	}

	/********************************************************************************/
	/********************************************************************************/
	private String manageInStringFormat(Node templateNode, String value){
		// Nothing to do. The XML parser convert special chars in reading tags
		return value;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private String manageInDateFormat(Node templateNode, String value){
		try{
			String format = templateNode.valueOf(TAG_format);
			if(format == null || format.length() == 0)
				return value;

			int idxYY = -1;
			int idxYYYY = format.indexOf("YYYY");
			if(idxYYYY < 0)
				idxYY = format.indexOf("YY");
			int idxMM = format.indexOf("MM");
			int idxDD = format.indexOf("DD");
			
			String day = value.substring(idxDD,idxDD+2);
			String month = value.substring(idxMM,idxMM+2);
			String year = "";
			if(idxYY >= 0)
				year = "20"+value.substring(idxYY,idxYY+2);
			else
				year = value.substring(idxYYYY,idxYYYY+4);				
			return day+"-"+month+"-"+year;
		}catch(Exception e){
			return "";
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private String manageInTimestampFormat(Node templateNode, String value){
		return manageInDateFormat(templateNode, value);
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private String manageInDoubleFormat(Node templateNode, String value){
		if(value.length() > 0)
			return NumberFormat.getInstance(java.util.Locale.ITALY).format(new BigDecimal(value).doubleValue());
		else
			return value;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private String manageInBooleanFormat(Node templateNode, String value){
		if(value.equalsIgnoreCase("S"))
			return "true";
		else
			return "false";
	}
}
