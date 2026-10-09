package com.atosorigin.wfem.dao;

import com.atosorigin.wfem.command.CommandDataModel;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class DAOCallableResultModel extends DAOBaseResultModel{
	private CommandDataModel outputCommandDataModel;
	private int result = 0;
	
	/********************************************************************************/
	/********************************************************************************/
	public DAOCallableResultModel() {
		super();
	}
	
	public com.atosorigin.wfem.command.CommandDataModel getOutputCommandDataModel() {
		return outputCommandDataModel;
	}
	public int getResult() {
		return result;
	}
	protected void setOutputCommandDataModel(com.atosorigin.wfem.command.CommandDataModel newOutputCommandDataModel) {
		outputCommandDataModel = newOutputCommandDataModel;
	}
	protected void setResult(int newResult) {
		result = newResult;
	}
}
