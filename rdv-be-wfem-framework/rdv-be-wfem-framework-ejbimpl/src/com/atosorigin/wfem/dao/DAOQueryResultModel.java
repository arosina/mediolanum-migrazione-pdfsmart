package com.atosorigin.wfem.dao;

import java.sql.Connection;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.ListType;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class DAOQueryResultModel extends DAOBaseResultModel{
	private AbstractType singleResult;
	private ListType result = new ListType();
	private boolean maxRowsExceeded = false;
	private int maxRows = 0;
	
	protected transient Connection dbConnection;
	protected transient String funcName;
	protected transient String query;
	protected transient DAOQueryAccessInfo dbQueryAccessInfo;
	protected transient CommandDataModel dataModel;
	protected transient DAOObject dao;
	protected transient DAOAccessParameters outputParameters;
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	public DAOQueryResultModel() {
		super();
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	public com.atosorigin.wfem.types.ListType getResult() {
		return result;
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	public boolean isMaxRowsExceeded() {
		return maxRowsExceeded;
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	protected void setMaxRowsExceeded(boolean newMaxRowsExceeded) {
		maxRowsExceeded = newMaxRowsExceeded;
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	protected void setResult(com.atosorigin.wfem.types.ListType newResult) {
		result = newResult;
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	public AbstractType getSingleResult() {
		return singleResult;
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	public void setSingleResult(AbstractType singleResult) {
		this.singleResult = singleResult;
	}

	
	/*************************************************************************************************/
	/*************************************************************************************************/
	public int getMaxRows() {
		return maxRows;
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	protected void setMaxRows(int maxRows) {
		this.maxRows = maxRows;
	}

}
