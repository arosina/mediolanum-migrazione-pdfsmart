package com.atosorigin.wfem.dao;

import java.io.Serializable;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public abstract class DAOAccessInfo implements Serializable{

	private static String booleanTrueValue  = "S";
	private static String booleanFalseValue = "N";
	
	private String daoObjectName;
	private String daoAccessName;
	private String dataSourceReferenceName;
	
	private String sqlString;
	private String inputDataModelClassName;
	private String outputDataModelClassName;
		
	private String concurrencyProperty;
	
	private DAOAccessParameters inputParameters;
	private DAOAccessParameters outputParameters;

	/**************************************************************************************************/
	/**************************************************************************************************/
	public DAOAccessInfo() {
		super();
	}

	public static String getBooleanFalseValue() {
		return booleanFalseValue;
	}

	public static String getBooleanTrueValue() {
		return booleanTrueValue;
	}

	public String getConcurrencyProperty() {
		return concurrencyProperty;
	}

	public String getDaoAccessName() {
		return daoAccessName;
	}

	public String getInputDataModelClassName() {
		return inputDataModelClassName;
	}

	public DAOAccessParameters getInputParameters() {
		return inputParameters;
	}

	public String getOutputDataModelClassName() {
		return outputDataModelClassName;
	}

	public DAOAccessParameters getOutputParameters() {
		return outputParameters;
	}

	public String getSqlString() {
		return sqlString;
	}

	public void setConcurrencyProperty(String newConcurrencyProperty) {
		concurrencyProperty = newConcurrencyProperty;
	}

	public void setDaoAccessName(String newDaoAccessName) {
		daoAccessName = newDaoAccessName;
	}

	public void setInputDataModelClassName(String newInputDataModelClassName) {
		inputDataModelClassName = newInputDataModelClassName;
	}

	public void setInputParameters(DAOAccessParameters newInputParameters) {
		inputParameters = newInputParameters;
	}

	public void setOutputDataModelClassName(String newOutputDataModelClassName) {
		outputDataModelClassName = newOutputDataModelClassName;
	}

	public void setOutputParameters(DAOAccessParameters newOutputParameters) {
		outputParameters = newOutputParameters;
	}

	public void setSqlString(String newSqlString) {
		sqlString = newSqlString;
	}

	public String getDataSourceReferenceName() {
		return dataSourceReferenceName;
	}

	public void setDataSourceReferenceName(String dataSourceReferenceName) {
		this.dataSourceReferenceName = dataSourceReferenceName;
	}

	public String getDaoObjectName() {
		return daoObjectName;
	}

	public void setDaoObjectName(String daoObjectName) {
		this.daoObjectName = daoObjectName;
	}

}
