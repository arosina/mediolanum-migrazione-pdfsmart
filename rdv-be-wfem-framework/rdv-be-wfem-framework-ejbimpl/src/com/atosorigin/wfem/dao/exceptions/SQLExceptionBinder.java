package com.atosorigin.wfem.dao.exceptions;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;

import com.atosorigin.wfem.loggers.DAOLogger;
import com.atosorigin.wfem.util.Tools;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class SQLExceptionBinder implements java.io.Serializable{

	/*********************************************************************************************/
	/*********************************************************************************************/
	static class ExBind{
		private Class   exClass;
		private Integer val1;
		private Integer val2;
		public ExBind(Class exClass,int val){
			this.exClass = exClass;
			this.val1 = new Integer(val);
		}
		public ExBind(Class exClass,int val1,int val2){
			this.exClass = exClass;
			this.val1 = new Integer(val1);
			this.val2 = new Integer(val2);
		}
		public Class getExClass() {
			return exClass;
		}
		public Integer getVal1() {
			return val1;
		}
		public Integer getVal2() {
			return val2;
		}

	}
	
	transient private static DAOLogger LOG = DAOLogger.getInstance();
	
    private static ArrayList sybaseExBindings = new ArrayList();
	private static ArrayList oracleExBindings = new ArrayList();

    static {
    	
        sybaseExBindings.add(new ExBind(PrimaryKeyViolation.class,2601));
        sybaseExBindings.add(new ExBind(PrimaryKeyViolation.class,2615));
        sybaseExBindings.add(new ExBind(PrimaryKeyViolation.class,2625));
        
        sybaseExBindings.add(new ExBind(ForeignKeyViolation.class,546));
        sybaseExBindings.add(new ExBind(ForeignKeyViolation.class,547));
        sybaseExBindings.add(new ExBind(ForeignKeyViolation.class,20000,29999));
        
    	oracleExBindings.add(new ExBind(PrimaryKeyViolation.class,1));
    	oracleExBindings.add(new ExBind(PrimaryKeyViolation.class,1062));

    	oracleExBindings.add(new ExBind(ForeignKeyViolation.class,546));
    	oracleExBindings.add(new ExBind(ForeignKeyViolation.class,2292));
    	oracleExBindings.add(new ExBind(ForeignKeyViolation.class,20000,29999));
    }
    
	/*********************************************************************************************/
	/*********************************************************************************************/
	public static DAOException mapException(SQLException sqle) {
		try{
			
			Class exClass = getExClass(sqle);
			DAOException result = (DAOException)exClass.newInstance();
			result.setSqlException(sqle);
			return result;
			
		}catch(Exception e){
			String errorMsg = "SQLExceptionBinder.mapException: Exception ["+e+"] mapping SQLException ["+sqle.toString()+"]";
			DAOException daoe = new DAOException(errorMsg);
			LOG.error(daoe);
			return daoe;
		}
	}
	
	/*********************************************************************************************/
	/*********************************************************************************************/
	private static Class getExClass(SQLException sqle){
		String message = sqle.getMessage();
		int sqlCode = sqle.getErrorCode();
		ArrayList exBindings = sybaseExBindings;
		if(message != null && message.startsWith("ORA-"+Tools.fillSx(""+sqlCode, '0',5)+":"))
			exBindings = oracleExBindings;
		ExBind exBind = null;
		Iterator it = exBindings.iterator();
		while(it.hasNext()){
			exBind = (ExBind)it.next();
			if(exBind.getVal2() == null){
				if(sqlCode == exBind.getVal1().intValue()){
					return exBind.getExClass();
				}
			}else{
				if(sqlCode >= exBind.getVal1().intValue() &&
				   sqlCode <= exBind.getVal2().intValue()){
					return exBind.getExClass();
				}
			}
		}
		return DAOException.class;
	}
}
