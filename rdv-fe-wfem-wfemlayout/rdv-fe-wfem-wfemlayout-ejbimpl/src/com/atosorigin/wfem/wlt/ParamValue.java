package com.atosorigin.wfem.wlt;

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public class ParamValue {
	private String value;
	private String apice;
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public ParamValue(String value, String apice){
		this.value = value;
		if(this.value == null)
			this.value = "";
		this.apice = apice;
		if(this.apice == null)
			this.apice = "";
	}
	
	public String asString(){
		return this.value;
	}
	
	public int asInt(){
		return Integer.parseInt(this.value);
	}

	public boolean asBoolean(){
		return Boolean.parseBoolean(this.value);
	}
	
	public String apice() {
		return this.apice;
	}

}
