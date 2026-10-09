package com.atosorigin.wfem.command;

import com.atosorigin.wfem.types.StringType;

/********************************************************************************
/********************************************************************************/
public class RemoteCommandException extends CommandDataModel {
	
	private StringType exception = new StringType();

	public StringType getException() {
		return exception;
	}

	public void setException(StringType exception) {
		this.exception = exception;
	}
	
}
