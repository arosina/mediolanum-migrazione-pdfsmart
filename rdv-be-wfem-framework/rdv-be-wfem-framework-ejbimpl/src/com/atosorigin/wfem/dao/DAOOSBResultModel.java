package com.atosorigin.wfem.dao;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.util.OSBCallData;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class DAOOSBResultModel  extends DAOBaseResultModel{
	private OSBCallData wsCallData = null; 
	private CommandDataModel result = null;
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	public DAOOSBResultModel() {
		super();
	}

	public CommandDataModel getResult() {
		return result;
	}

	public void setResult(CommandDataModel result) {
		this.result = result;
	}

	public OSBCallData getWsCallData() {
		return wsCallData;
	}

	public void setWsCallData(OSBCallData wsCallData) {
		this.wsCallData = wsCallData;
	}

}
