package com.atosorigin.wfem.util;

import org.dom4j.Element;
import org.dom4j.Node;

import com.atosorigin.wfem.types.AbstractType;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public abstract class XmlServiceDataFormatter {
	
	protected static final String TAG_format = "@format";
	
	private static final String TAG_cdata = "@cdata";
	private static final String TAG_notnullvalue = "@notNullValue";
	private static final String TAG_fillsx = "@fillsx";
	private static final String TAG_filldx = "@filldx";
	private static final String TAG_unfillsx = "@unfillsx";
	private static final String TAG_unfilldx = "@unfilldx";
	private static final String TAG_toString = "@toString";
	
	protected abstract String manageOutputFormat(Element el, AbstractType propValue);
	protected abstract String manageInputFormat(Node templateNode, String propValue, Class propClass);

	/********************************************************************************/
	/********************************************************************************/
	static boolean isToString(Node currentNode){
		String val = currentNode.valueOf(TAG_toString);
		if(val != null && val.equalsIgnoreCase("true"))
			return true;
		return false;
	}
		
	/********************************************************************************/
	/********************************************************************************/
	static boolean isCData(Node currentNode){
		String val = currentNode.valueOf(TAG_cdata);
		if(val != null && val.equalsIgnoreCase("true"))
			return true;
		return false;
	}
		
	/********************************************************************************/
	/********************************************************************************/
	static String manageValueFormat(Node currentNode, String value){
		try{
			String notNullValue = currentNode.valueOf(TAG_notnullvalue);
			if(notNullValue != null && notNullValue.length() > 0){
				if(value.length() == 0)
					value = notNullValue;
			}
			String fill = currentNode.valueOf(TAG_fillsx);
			if(fill != null && fill.length() > 0){
				String[] val = fill.split(",");
				if(val.length == 2)
					value = Tools.fillSx(value,val[0].charAt(0),Integer.parseInt(val[1]));
			}
			fill = currentNode.valueOf(TAG_filldx);
			if(fill != null && fill.length() > 0){
				String[] val = fill.split(",");
				if(val.length == 2)
					value = Tools.fillDx(value,val[0].charAt(0),Integer.parseInt(val[1]));
			}
			String unfill = currentNode.valueOf(TAG_unfillsx);
			if(unfill != null && unfill.length() > 0){
				value = Tools.unFillSx(value,unfill.charAt(0));
			}
			unfill = currentNode.valueOf(TAG_unfilldx);
			if(unfill != null && unfill.length() > 0){
				value = Tools.unFillDx(value,unfill.charAt(0));
			}
			return value;
		}catch(Exception e){
			return "";
		}
	}
	
	
}
