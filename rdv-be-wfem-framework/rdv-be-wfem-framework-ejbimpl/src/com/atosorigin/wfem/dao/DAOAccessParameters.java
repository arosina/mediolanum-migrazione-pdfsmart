package com.atosorigin.wfem.dao;

import java.io.Serializable;
import java.util.Vector;

import com.atosorigin.wfem.command.ClientSessionContext;

/*************************************************************************************************/
/*************************************************************************************************/
public class DAOAccessParameters implements Serializable{
	private Vector parameters = new Vector();

	/*************************************************************************************************/
	/*************************************************************************************************/
	public DAOAccessParameters() {
	}

	/********************************************************************************
	/********************************************************************************/
	public static String processCscVariables(ClientSessionContext csc, String propName, String keyPrefix){
		if(csc == null)
			return null;
		if(keyPrefix == null)
			keyPrefix = "";
		String propValue = null;
		if(propName.equals(keyPrefix+"contextCountryCode"))
			propValue = csc.getCountryCode(); 
		else if(propName.equals(keyPrefix+"contextChannelCode"))
			propValue = csc.getChannelCode(); 
		else if(propName.equals(keyPrefix+"contextLangCode"))
			propValue = csc.getLangCode(); 
		else if(propName.equals(keyPrefix+"contextUserCode"))
			propValue = csc.getUserCode(); 
		else if(propName.equals(keyPrefix+"contextDelegatedUserCode"))
			propValue = csc.getDelegatedUserCode(); 
		else if(propName.equals(keyPrefix+"contextAdvisorCode"))
			propValue = csc.getCurrentLinkedUserCode(); 
		else if(propName.equals(keyPrefix+"contextCurrentLinkedUserCode"))
			propValue = csc.getCurrentLinkedUserCode(); 
		else if(propName.equals(keyPrefix+"contextReplicationServer"))
			propValue = csc.getReplicationServer(); 
		else if(propName.equals(keyPrefix+"contextUserType"))
			propValue = csc.getUserType(); 
		return propValue;
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	public void disableParameter(int idx) {
		DAOAccessParameter parameter = (DAOAccessParameter)parameters.get(idx);
		parameter.setDisabled(true);
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	public void addParameter(DAOAccessParameter newParameter) {
		parameters.add(newParameter);
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	public DAOAccessParameter getParameter(int idx) {
		return (DAOAccessParameter)parameters.get(idx);
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	public DAOAccessParameter getParameter(String propertyName) {
		for(int i=0;i<parameters.size();i++){
			DAOAccessParameter par = (DAOAccessParameter)parameters.get(i);
			if(par.getPropertyName().equals(propertyName))
				return par;
		}
		return null;
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	public int getParametersCount() {
		return parameters.size();
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	public DAOAccessParameters getPrimaryKeys() {
		DAOAccessParameters result = new DAOAccessParameters();
		for(int i=0;i<parameters.size();i++){
			DAOAccessParameter parameter = getParameter(i);
			if(parameter.isPrimaryKey())
				result.addParameter(parameter);
		}
		return result;
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	public DAOAccessParameters getForeignKeys() {
		DAOAccessParameters result = new DAOAccessParameters();
		for(int i=0;i<parameters.size();i++){
			DAOAccessParameter parameter = getParameter(i);
			if(parameter.getForeignKey() != null)
				result.addParameter(parameter);
		}
		return result;
	}
}
