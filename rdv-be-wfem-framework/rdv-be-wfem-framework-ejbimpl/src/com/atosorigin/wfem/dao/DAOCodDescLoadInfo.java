package com.atosorigin.wfem.dao;

import java.io.Serializable;
import java.util.Vector;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class DAOCodDescLoadInfo implements Serializable{
	private String dataSourceReferenceName;
	private String codDescName;
	private boolean distinct=false;
	private String tableName;
	private String codColumnName;
	private String codAliasColumnName;
	private String shortDescrColumnName;
	private String descrColumnName;
	private String descrAliasColumnName;
	private String whereCondition;
	private String orderBy;
	private Vector whereConditionFields = new Vector();
	private String validityWhereCondition;
	private Vector validityWhereConditionFields = new Vector();
	private int refreshTime = -1;
	
	/********************************************************************************
	/********************************************************************************/
	public DAOCodDescLoadInfo() {
		super();
	}
	
	/********************************************************************************
	/********************************************************************************/
	public java.lang.String getCodColumnName() {
		return codColumnName;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public java.lang.String getCodDescName() {
		return codDescName;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public java.lang.String getDescrColumnName() {
		return descrColumnName;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public java.lang.String getTableName() {
		return tableName;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public java.lang.String getValidityWhereCondition() {
		return validityWhereCondition;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public void setCodColumnName(java.lang.String newCodColumnName) {
		codColumnName = newCodColumnName;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public void setCodDescName(java.lang.String newCodDescName) {
		codDescName = newCodDescName;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public void setDescrColumnName(java.lang.String newDescrColumnName) {
		descrColumnName = newDescrColumnName;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public void setTableName(java.lang.String newTableName) {
		tableName = newTableName;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public void setValidityWhereCondition(java.lang.String newValidityWhereCondition) {
		validityWhereCondition = newValidityWhereCondition;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public String getShortDescrColumnName() {
		return shortDescrColumnName;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public void setShortDescrColumnName(String shortDescrColumnName) {
		this.shortDescrColumnName = shortDescrColumnName;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public void setWhereCondition(String whereCondition) {
		this.whereCondition = whereCondition;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public Vector getValidityWhereConditionFields() {
		return validityWhereConditionFields;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public Vector getWhereConditionFields() {
		return whereConditionFields;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public void setValidityWhereConditionFields(Vector validityWhereConditionFields) {
		this.validityWhereConditionFields = validityWhereConditionFields;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public void setWhereConditionFields(Vector whereConditionFields) {
		this.whereConditionFields = whereConditionFields;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public String getWhereCondition() {
		return whereCondition;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public int getRefreshTime() {
		return refreshTime;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public void setRefreshTime(int refreshTime) {
		this.refreshTime = refreshTime;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public String getDataSourceReferenceName() {
		return dataSourceReferenceName;
	}
	
	/********************************************************************************
	/********************************************************************************/
	public void setDataSourceReferenceName(String dataSourceReferenceName) {
		this.dataSourceReferenceName = dataSourceReferenceName;
	}

	/********************************************************************************
	/********************************************************************************/
	public boolean isDistinct() {
		return distinct;
	}

	/********************************************************************************
	/********************************************************************************/
	public void setDistinct(boolean distinct) {
		this.distinct = distinct;
	}

	public String getCodAliasColumnName() {
		return codAliasColumnName;
	}

	public void setCodAliasColumnName(String codAliasColumnName) {
		this.codAliasColumnName = codAliasColumnName;
	}

	public String getDescrAliasColumnName() {
		return descrAliasColumnName;
	}

	public void setDescrAliasColumnName(String descrAliasColumnName) {
		this.descrAliasColumnName = descrAliasColumnName;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

}
