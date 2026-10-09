package com.businessobjects.dsws;

public class DSWSException extends Exception {
	private static final long serialVersionUID = 1L;

	public String getCauseMessage() {
		return getMessage();
	}
}
