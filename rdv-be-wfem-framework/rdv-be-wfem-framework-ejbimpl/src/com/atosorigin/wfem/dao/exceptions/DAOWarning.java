package com.atosorigin.wfem.dao.exceptions;

import java.sql.SQLException;

/**
 * Insert the type's description here.
 * Creation date: (06/11/2002 12.26.49)
 * @author: Administrator
 */
public class DAOWarning extends DAOException {
	
	/*************************************************************************/
	/*************************************************************************/
	public DAOWarning(){
		super();
	}
	
	/*************************************************************************/
	/*************************************************************************/
	public DAOWarning(SQLException sqle){
		super(sqle);
	}
	
	/*************************************************************************/
	/*************************************************************************/
	public DAOWarning(String descr) {
		super(descr);
	}
}