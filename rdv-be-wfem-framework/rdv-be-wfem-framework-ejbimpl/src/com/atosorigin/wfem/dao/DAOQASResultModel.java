package com.atosorigin.wfem.dao;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.util.QASCallData;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class DAOQASResultModel  extends DAOBaseResultModel{
	private QASCallData qasCallData = null; 
	private CommandDataModel result = null;
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	public DAOQASResultModel() {
		super();
	}

	public CommandDataModel getResult() {
		return result;
	}

	public void setResult(CommandDataModel result) {
		this.result = result;
	}

	public QASCallData getQasCallData() {
		return qasCallData;
	}

	public void setQasCallData(QASCallData qasCallData) {
		this.qasCallData = qasCallData;
	}
}
