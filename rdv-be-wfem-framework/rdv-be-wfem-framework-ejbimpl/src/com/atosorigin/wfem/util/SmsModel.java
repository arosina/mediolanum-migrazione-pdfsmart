package com.atosorigin.wfem.util;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.StringType;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class SmsModel extends CommandDataModel {

	private static final long serialVersionUID = 1L;

	//Input
	private StringType  number = new StringType();
	private StringType  message = new StringType();
	private StringType  environment = new StringType();
	
	// Output
	private StringType  smsMtCode = new StringType();

	public StringType getNumber() {
		return number;
	}

	public void setNumber(StringType number) {
		this.number = number;
	}

	public StringType getMessage() {
		return message;
	}

	public void setMessage(StringType message) {
		this.message = message;
	}

	public StringType getEnvironment() {
		return environment;
	}

	public void setEnvironment(StringType environment) {
		this.environment = environment;
	}

	public StringType getSmsMtCode() {
		return smsMtCode;
	}

	public void setSmsMtCode(StringType smsMtCode) {
		this.smsMtCode = smsMtCode;
	}
 
}
