package com.atosorigin.wfem.dao.exceptions;

import java.sql.SQLException;

/**
 * Insert the type's description here.
 * Creation date: (06/11/2002 12.26.49)
 * @author: Administrator
 */
public class PrimaryKeyViolation extends DAOWarning {
	
	/*************************************************************************/
	/*************************************************************************/
	public PrimaryKeyViolation(){
		super();
	}
	
	/*************************************************************************/
	/*************************************************************************/
	public PrimaryKeyViolation(SQLException sqle){
		super(sqle);
	}
	
	/*************************************************************************/
	/*************************************************************************/
	public PrimaryKeyViolation(String descr) {
		super(descr);
	}

}
