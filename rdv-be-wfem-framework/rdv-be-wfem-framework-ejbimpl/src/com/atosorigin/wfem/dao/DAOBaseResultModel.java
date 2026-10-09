package com.atosorigin.wfem.dao;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public abstract class DAOBaseResultModel implements DAOResultModel{
	private StringBuffer middleTierInputParameters = new StringBuffer();

	public StringBuffer getMiddleTierInputParameters() {
		return middleTierInputParameters;
	}

	public void setMiddleTierInputParameters(StringBuffer middleTierInputParameters) {
		this.middleTierInputParameters = middleTierInputParameters;
	}


}
