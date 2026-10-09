package com.atosorigin.wfem.charts;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**************************************************************************************************/
/**************************************************************************************************/
public class ChartParameters implements Serializable{
	
	public transient static Pattern chartParametersPattern = Pattern.compile("(\\s*)?([a-zA-Z0-9\\_]+?)=('|\")(.*?)\\3(\\s*?)");
	
	String ID;
	String type="";
	Map parameters=new HashMap();
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public ChartParameters(String chartType, String parameters){
		this.ID = Integer.toString(this.hashCode());
		setType(chartType);
		parameters = " "+parameters;
		Matcher mat = chartParametersPattern.matcher(parameters);
		while(mat.find()) {
			String parName = mat.group(2);
			String parValue = mat.group(4);
			getParameters().put(parName,parValue);
		}		
	}
	
	public String getID() {
		return ID;
	}
	public Map getParameters() {
		return parameters;
	}
	public void setParameters(Map parameters) {
		this.parameters = parameters;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
