package com.atosorigin.wfem.bo;

import java.util.Vector;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class BoParameter {
	private String parName;
	private String parValue;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public BoParameter(String parName, String parValue){
		this.parName = parName;
		this.parValue = parValue;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String findParValueByName(String parName, Vector boPars){
		for(int i=0;i<boPars.size();i++){
			BoParameter boPar = (BoParameter)boPars.get(i);
			if(boPar.getParName().equals(parName))
				return boPar.getParValue(); 
		}
		return null;
	}
	
	public String getParName() {
		return parName;
	}
	public void setParName(String parName) {
		this.parName = parName;
	}
	public String getParValue() {
		return parValue;
	}
	public void setParValue(String parValue) {
		this.parValue = parValue;
	}
}
