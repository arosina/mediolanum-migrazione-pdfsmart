package com.atosorigin.wfem.dao.exceptions;

import java.sql.SQLException;

/**
 * Insert the type's description here.
 * Creation date: (06/11/2002 12.26.49)
 * @author: Administrator
 */
public class ConcurrencyViolation extends DAOWarning {
	
	/*************************************************************************/
	/*************************************************************************/
	public ConcurrencyViolation(){
		super();
	}
	
	/*************************************************************************/
	/*************************************************************************/
	public ConcurrencyViolation(SQLException sqle){
		super(sqle);
	}
	
	/*************************************************************************/
	/*************************************************************************/
	public ConcurrencyViolation(String descr) {
		super(descr);
	}
}