package com.atosorigin.wfem.dao;


/*************************************************************************************************/
/*************************************************************************************************/
public class DAOCallableAccessInfo extends DAOAccessInfo{
	
	private boolean namedParameters;
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	public DAOCallableAccessInfo() {
		super();
	}

	public boolean isNamedParameters() {
		return namedParameters;
	}

	public void setNamedParameters(boolean namedParameters) {
		this.namedParameters = namedParameters;
	}
}
