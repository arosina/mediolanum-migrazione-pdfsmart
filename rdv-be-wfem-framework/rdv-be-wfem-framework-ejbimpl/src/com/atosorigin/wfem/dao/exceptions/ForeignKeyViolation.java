package com.atosorigin.wfem.dao.exceptions;

import java.sql.SQLException;

/**
 * Insert the type's description here.
 * Creation date: (06/11/2002 12.26.49)
 * @author: Administrator
 */
public class ForeignKeyViolation extends DAOWarning {
	
	/*************************************************************************/
	/*************************************************************************/
	public ForeignKeyViolation(){
		super();
	}
	
	/*************************************************************************/
	/*************************************************************************/
	public ForeignKeyViolation(SQLException sqle){
		super(sqle);
	}
	
	/*************************************************************************/
	/*************************************************************************/
	public ForeignKeyViolation(String descr) {
		super(descr);
	}

}
