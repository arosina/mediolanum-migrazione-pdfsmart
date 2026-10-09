package com.atosorigin.wfem.dao.exceptions;

import java.sql.SQLException;

/**
 * Insert the type's description here.
 * Creation date: (06/11/2002 12.26.49)
 * @author: Administrator
 */
public class NoRowsAffected extends DAOWarning {
	
	/*************************************************************************/
	/*************************************************************************/
	public NoRowsAffected(){
		super();
	}

	/*************************************************************************/
	/*************************************************************************/
	public NoRowsAffected(SQLException sqle){
		super(sqle);
	}

	/*************************************************************************/
	/*************************************************************************/
	public NoRowsAffected(String descr) {
		super(descr);
	}
}