package com.atosorigin.wfem.dao;

import org.dom4j.Element;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class DAOQueryAccessInfo extends DAOAccessInfo{

	public static final int NORMAL_QUERY = 0;
	public static final int PARSED_QUERY = 1;
	public static final int STORED_QUERY = 2;

	private int queryType = NORMAL_QUERY;
	private int maxResultRows = 0;
	private boolean dynamicQuery = false;
	private boolean useCallable = false;
	private String storedToCallAfterQuery;
	private String tablesToDeleteAfterQuery;
	
	private Element sqlNodeString;
	
	/********************************************************************************
	/********************************************************************************/
	public DAOQueryAccessInfo() {
		super();
	}
	
	/********************************************************************************
	/********************************************************************************/
	public java.lang.String getSqlString() {
		String sqlQuery = super.getSqlString();
      	if(sqlQuery != null){
			sqlQuery = sqlQuery.replace('\n',' ');
			sqlQuery = sqlQuery.replace('\r',' ');
			sqlQuery = sqlQuery.replace('\t',' ');
      	}
		return sqlQuery;
	}

	/********************************************************************************
	/********************************************************************************/
	public int getMaxResultRows() {
		return maxResultRows;
	}

	/********************************************************************************
	/********************************************************************************/
	public int getQueryType() {
		return queryType;
	}

	/********************************************************************************
	/********************************************************************************/
	public void setMaxResultRows(int newMaxResultRows) {
		maxResultRows = newMaxResultRows;
	}

	/********************************************************************************
	/********************************************************************************/
	public void setQueryType(int newQueryType) {
		queryType = newQueryType;
	}

	/********************************************************************************
	/********************************************************************************/
	public void setSqlNodeString(Element newSqlNodeString) {
		sqlNodeString = newSqlNodeString;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public Element getSqlNodeString() {
		return sqlNodeString;
	}

	/********************************************************************************
	/********************************************************************************/
	public boolean isDynamicQuery() {
		return dynamicQuery;
	}

	/********************************************************************************
	/********************************************************************************/
	public void setDynamicQuery(boolean dynamicQuery) {
		this.dynamicQuery = dynamicQuery;
	}

	/********************************************************************************
	/********************************************************************************/
	public boolean isUseCallable() {
		return useCallable;
	}

	/********************************************************************************
	/********************************************************************************/
	public void setUseCallable(boolean useCallable) {
		this.useCallable = useCallable;
	}

	/********************************************************************************
	/********************************************************************************/
	public String getStoredToCallAfterQuery() {
		return storedToCallAfterQuery;
	}

	/********************************************************************************
	/********************************************************************************/
	public void setStoredToCallAfterQuery(String storedToCallAfterQuery) {
		this.storedToCallAfterQuery = storedToCallAfterQuery;
	}

	/********************************************************************************
	/********************************************************************************/
	public String getTablesToDeleteAfterQuery() {
		return tablesToDeleteAfterQuery;
	}

	/********************************************************************************
	/********************************************************************************/
	public void setTablesToDeleteAfterQuery(String tablesToDeleteAfterQuery) {
		this.tablesToDeleteAfterQuery = tablesToDeleteAfterQuery;
	}

}
