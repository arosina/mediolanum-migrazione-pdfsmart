package com.atosorigin.wfem.dao;

import java.util.*;

import com.atosorigin.wfem.dao.exceptions.*;
import com.atosorigin.wfem.loggers.*;
/**
 * @author Ricotti
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 */
public class DAOCallableResultBinder {
	
	transient private static DAOLogger LOG = DAOLogger.getInstance();
	
	private static final Map resultBindings = new HashMap();
	
	static{
		resultBindings.put(new Integer(1),DAOException.class);
		resultBindings.put(new Integer(2),DAOException.class);
		resultBindings.put(new Integer(3),DAOException.class);
		resultBindings.put(new Integer(4),DAOException.class);
		resultBindings.put(new Integer(5),PrimaryKeyViolation.class);
		resultBindings.put(new Integer(6),NoRowsAffected.class);
		resultBindings.put(new Integer(8),ForeignKeyViolation.class);
		resultBindings.put(new Integer(9),ConcurrencyViolation.class);
	}
	
	/*********************************************************************************************/
	/*********************************************************************************************/
	public static DAOException mapStatus(int status) {
		try{
			
			Class exClass = (Class)resultBindings.get(new Integer(status));
			if(exClass == null){
				LOG.debug("DAOCallableResultBinder.mapStatus: Result status ["+status+"] is not mapped. Returnin null");
				return null;
			}
			LOG.debug("DAOCallableResultBinder.mapStatus: Mapping callable result status ["+status+"] into class ["+exClass+"]");
			DAOException ex = (DAOException)exClass.newInstance();
			return ex;
			
		}catch(Exception e){
			String errorMsg = "DAOCallableResultBinder.mapResult: Exception ["+e+"] mapping result status ["+status+"]";
			DAOException daoe = new DAOException(errorMsg);
			LOG.error(daoe);
			return daoe;
		}
	}
		
}
