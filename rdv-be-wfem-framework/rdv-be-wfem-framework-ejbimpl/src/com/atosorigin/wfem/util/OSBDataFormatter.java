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
public class OSBDataFormatter extends XmlServiceDataFormatter {

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
		if(value.isNull())
			return "";
		return value.getAA()+"-"+value.getMM()+"-"+value.getGG();
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private String manageOutTimestampFormat(Node currentNode, TimestampType value){
		if(value.isNull())
			return "";
		String ms = value.getMS();
		if(ms.length() > 0)
			ms = "."+ms;
		return value.getAA()+"-"+value.getMM()+"-"+value.getGG()+"T"+value.getHH()+":"+value.getMI()+":"+value.getSS()+ms;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private String manageOutDoubleFormat(Node currentNode, DoubleType value){
		if(value.isNull())
			return "";
		return value.bigValue().setScale(2, BigDecimal.ROUND_HALF_UP).toString();
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private String manageOutBooleanFormat(Node currentNode, BooleanType value){
		return value.booleanValue() ? "true" : "false";
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
	private String manageInStringFormat(Node currentNode, String value){
		// Nothing to do. The XML parser convert special chars in reading tags
		return value;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private String manageInDateFormat(Node currentNode, String value){
		if(value.length() == 0)
			return "";
		try{
			String day = value.substring(8);
			String month = value.substring(5,7);
			String year = value.substring(0,4);
			return day+"-"+month+"-"+year;
		}catch(Exception e){
			return "";
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private String manageInTimestampFormat(Node templateNode, String value){
		try{
			if(value.length() == 0)
				return "";
			
			String format = templateNode.valueOf(TAG_format);
			if(format == null || format.length() == 0)
				format = "YYYY-MM-DDTHH:MI:SS";

			int idxYY = -1;
			int idxYYYY = format.indexOf("YYYY");
			if(idxYYYY < 0)
				idxYY = format.indexOf("YY");
			int idxMM = format.indexOf("MM");
			int idxDD = format.indexOf("DD");
			int idxHH = format.indexOf("HH");
			int idxMI = format.indexOf("MI");
			int idxSS = format.indexOf("SS");

			String day = value.substring(idxDD,idxDD+2);
			String month = value.substring(idxMM,idxMM+2);
			String year = "";
			if(idxYY >= 0)
				year = "20"+value.substring(idxYY,idxYY+2);
			else
				year = value.substring(idxYYYY,idxYYYY+4);				
			String hh = value.substring(idxHH,idxHH+2);
			String mi = value.substring(idxMI,idxMI+2);
			String ss = value.substring(idxSS,idxSS+2);
			
			String milliseconds = "";
			int milliIdx = value.lastIndexOf(".");
			if(milliIdx >= 0){
				milliseconds = value.substring(milliIdx+1);
				try{
					Long.parseLong(milliseconds);
					milliseconds = "."+Tools.fillDx(milliseconds,'0',9);
				}catch(Exception e){
					milliseconds = "";					
				}
			}
			return day+"-"+month+"-"+year+" "+hh+":"+mi+":"+ss+milliseconds;
			
		}catch(Exception e){
			return "";
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private String manageInDoubleFormat(Node templateNode, String value){
		if(value.length() == 0)
			return "";
		return NumberFormat.getInstance(java.util.Locale.ITALY).format(new BigDecimal(value).doubleValue());
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private String manageInBooleanFormat(Node templateNode, String value){
		return value;
	}
}
