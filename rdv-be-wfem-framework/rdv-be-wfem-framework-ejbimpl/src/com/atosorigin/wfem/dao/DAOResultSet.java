package com.atosorigin.wfem.dao;

import java.io.InputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.AbstractTypePropertyDescriptor;
import com.atosorigin.wfem.types.ByteArrayType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
class DAOResultSet implements Serializable {
	
	protected transient Connection dbConnection;
	protected transient String funcName;
	protected transient DAOQueryAccessInfo dbQueryAccessInfo;
	protected transient CommandDataModel dataModel;
	
	private ResultSet resultSet;
	private CallableStatement callableStatement;

	/********************************************************************************/
	/********************************************************************************/
	protected DAOResultSet(ResultSet resultSet){
		this.resultSet = resultSet;
	}

	/********************************************************************************/
	/********************************************************************************/
	protected DAOResultSet(CallableStatement callableStatement){
		this.callableStatement = callableStatement;
	}

	/********************************************************************************/
	/********************************************************************************/
	protected String getString(int idx) throws SQLException{
		return (resultSet == null ? callableStatement.getString(idx) : resultSet.getString(idx));
	}

	/********************************************************************************/
	/********************************************************************************/
	protected BigDecimal getBigDecimal(int idx) throws SQLException{
		return (resultSet == null ? callableStatement.getBigDecimal(idx) : resultSet.getBigDecimal(idx));
	}

	/********************************************************************************/
	/********************************************************************************/
	protected Date getDate(int idx) throws SQLException{
		return (resultSet == null ? callableStatement.getDate(idx) : resultSet.getDate(idx));
	}

	/********************************************************************************/
	/********************************************************************************/
	protected Timestamp getTimestamp(int idx) throws SQLException{
		return (resultSet == null ? callableStatement.getTimestamp(idx) : resultSet.getTimestamp(idx));
	}

	/********************************************************************************/
	/********************************************************************************/
	protected byte[] getBytes(int idx) throws SQLException{
		return (resultSet == null ? callableStatement.getBytes(idx) : resultSet.getBytes(idx));
	}

	/********************************************************************************/
	/********************************************************************************/
	protected InputStream getInputStream(int idx) throws SQLException{
		if(resultSet == null)
			throw new SQLException("getInputStream non valid for callable");
		return resultSet.getBinaryStream(idx);
	}

	/********************************************************************************/
	/********************************************************************************/
	protected boolean wasNull() throws SQLException{
		return (resultSet == null ? callableStatement.wasNull() : resultSet.wasNull());
	}

	/********************************************************************************/
	/********************************************************************************/
	protected String getColumnLabel(int idx) throws SQLException{
		String collab = resultSet == null ? callableStatement.getMetaData().getColumnLabel(idx) : 
											resultSet.getMetaData().getColumnLabel(idx);
		if(collab.length() == 0)
			collab = getColumnName(idx);
		return collab;
	}

	/********************************************************************************/
	/********************************************************************************/
	protected String getColumnName(int idx) throws SQLException{
		String javaColName = "";
		String colname = resultSet == null ? callableStatement.getMetaData().getColumnName(idx) : 
											 resultSet.getMetaData().getColumnName(idx);
		if(colname == null || colname.length() == 0)
			return "column"+idx;
		
		colname = colname.toLowerCase();
		if(colname.startsWith("_"))
			colname = colname.substring(1);
		for(;;){
			int punt = colname.indexOf('_');
			if(punt <= 0)
				break;
			javaColName += colname.substring(0,1).toUpperCase()+colname.substring(1,punt);
			colname = colname.substring(punt+1);
		}
		if(colname.length() >= 1)
			javaColName += colname.substring(0,1).toUpperCase()+colname.substring(1);
		if(javaColName.length() >= 1)
			javaColName = javaColName.substring(0,1).toLowerCase()+javaColName.substring(1);
		return javaColName;
	}

	/********************************************************************************/
	/********************************************************************************/
	protected int getColumnCount() throws SQLException{
		return (resultSet == null ? callableStatement.getMetaData().getColumnCount() : 
									resultSet.getMetaData().getColumnCount());
	}

	/********************************************************************************/
	/********************************************************************************/
	protected Class getColumnType(int idx) throws SQLException{
		int type = (resultSet == null ? callableStatement.getMetaData().getColumnType(idx) : 
										resultSet.getMetaData().getColumnType(idx));
		switch(type){
		
			case Types.CHAR:
			case Types.VARCHAR:
			case Types.LONGVARCHAR:
				return StringType.class;
				
			case Types.BIGINT:
			case Types.INTEGER:
			case Types.TINYINT:
			case Types.SMALLINT:				
				return IntegerType.class;
				
			case Types.DECIMAL:
			case Types.DOUBLE:
			case Types.FLOAT:
			case Types.NUMERIC:
				return DoubleType.class;
				
			case Types.DATE:
			case Types.TIMESTAMP:
				return DateType.class;
				
			case Types.BINARY:
			case Types.BLOB:
			case Types.CLOB:
			case Types.LONGVARBINARY:
			case Types.VARBINARY:
				return ByteArrayType.class;
				
			default:
				throw new SQLException(getClass()+".getColumnType: sql type ["+type+"] is not managed");
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	AbstractTypePropertyDescriptor[] getColumnsAsProperties() throws Exception{
		ResultSetMetaData md = (resultSet == null ? callableStatement.getMetaData() : resultSet.getMetaData());
		int colCount = md.getColumnCount();
		AbstractTypePropertyDescriptor[] result = new AbstractTypePropertyDescriptor[colCount];
		for(int i=0;i<colCount;i++){
			String colName = getColumnName(i+1);
			AbstractType colValue = (AbstractType)getColumnType(i+1).newInstance();
    		AbstractTypePropertyDescriptor p = new AbstractTypePropertyDescriptor(colName,colValue,i);
    		result[i] = p;			
		}
    	return result;
	}

	/********************************************************************************/
	/********************************************************************************/
	protected ResultSet getResultSet() {
		return resultSet;
	}	
	
}
