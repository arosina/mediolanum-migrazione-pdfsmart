package com.atosorigin.wfem.command;

import java.io.Serializable;
import java.util.Vector;

public class CommandException extends java.rmi.RemoteException implements Serializable {
	private Vector messages = new Vector();
	private Vector errors = new Vector();
/**
 * Insert the method's description here.
 * Creation date: (22/04/2002 16.30.41)
 */
public CommandException() {
	super();
}
public CommandException(Exception e) {
	super();
	String msg = e.getMessage();
	if(msg == null || msg.equals(""))
		setMessage(e.toString());
	else
		setMessage(msg);
}
/**
 * Insert the method's description here.
 * Creation date: (22/04/2002 16.37.36)
 */
public CommandException(String msg) {
	super(msg);
	setMessage(msg);
}
/**
 * Insert the method's description here.
 * Creation date: (22/04/2002 16.56.50)
 */
public CommandException(Vector msg) {
	setMessage(msg);
}
/**
 * Insert the method's description here.
 * Creation date: (23/09/2002 15.06.40)
 */
public void addError( CommandError commandError ) {

	errors.addElement( commandError );
}
/**
 * Insert the method's description here.
 * Creation date: (23/09/2002 15.07.15)
 */
public void addErrorList(java.util.Vector errorList) {

	for ( int i = 0; i < errorList.size(); i++ ) {
		errors.addElement(errorList.elementAt(i));
	}
}
/**
 * Insert the method's description here.
 * Creation date: (23/09/2002 15.07.33)
 */
public java.util.Vector getErrors() {
	return errors;
}
/**
 * Insert the method's description here.
 * Creation date: (23/04/2002 15.39.41)
 */
public java.lang.String getMessage() {
	String result = "";
	for(int i=0;i<messages.size();i++){
		result += (String)messages.elementAt(i) + "\n";
	}
	return result;
}
/**
 * Insert the method's description here.
 * Creation date: (22/04/2002 15.22.09)
 * @return java.lang.String
 */
public java.lang.String getMessage(int index) {
	if(index < 0 ||
	   index >= messages.size())
		return null;
		
	return (String)messages.elementAt(index);
}
/**
 * Insert the method's description here.
 * Creation date: (23/09/2002 15.07.47)
 */
public boolean isError() {
	return (this.errors.size() > 0);
}
/**
 * Insert the method's description here.
 * Creation date: (23/09/2002 15.08.01)
 */
public void resetErrors() {
	errors.clear();
}
/**
 * Insert the method's description here.
 * Creation date: (22/04/2002 15.22.09)
 * @param newMessage java.lang.String
 */
public void setMessage(java.lang.String newMessage) {
	messages = new Vector();
	messages.addElement(newMessage);
}
/**
 * Insert the method's description here.
 * Creation date: (22/04/2002 16.57.27)
 */
public void setMessage(Vector newMessages) {
	messages = newMessages;
}
}
