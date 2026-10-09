package com.atosorigin.wfem.types;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class TypeWarning  extends AbstractTypeError {
    
	/****************************************************************/
	/****************************************************************/
	public TypeWarning( String key ) {
	
		this(key, null, null, null, null);
	}
	
	/****************************************************************/
	/****************************************************************/
	public TypeWarning( String key, Object value0 ) {
	
		this(key, value0, null, null, null);
	}
	
	/****************************************************************/
	/****************************************************************/
	public TypeWarning( String key, Object value0, Object value1 ) {
	
		this(key, value0, value1, null, null);
	}
	
	/****************************************************************/
	/****************************************************************/
	public TypeWarning( String key, Object value0, Object value1, Object value2 ) {
	
		this(key, value0, value1, value2, null);
	}
	
	/****************************************************************/
	/****************************************************************/
	public TypeWarning(String key, Object value0, Object value1,
	                       Object value2, Object value3) {
		super();
	    this.key = key;
	    values[0] = value0;
	    values[1] = value1;
	    values[2] = value2;
	    values[3] = value3;
	
	}
}
