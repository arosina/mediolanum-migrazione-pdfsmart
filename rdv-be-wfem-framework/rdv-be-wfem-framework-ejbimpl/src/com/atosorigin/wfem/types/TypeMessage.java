package com.atosorigin.wfem.types;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class TypeMessage  extends AbstractTypeError {

	/****************************************************************/
	/****************************************************************/
	public TypeMessage( String key ) {
	
		this(key, null, null, null, null);
	}
	
	/****************************************************************/
	/****************************************************************/
	public TypeMessage( String key, Object value0 ) {
	
		this(key, value0, null, null, null);
	}
	
	/****************************************************************/
	/****************************************************************/
	public TypeMessage( String key, Object value0, Object value1 ) {
	
		this(key, value0, value1, null, null);
	}
	
	/****************************************************************/
	/****************************************************************/
	public TypeMessage( String key, Object value0, Object value1, Object value2 ) {
	
		this(key, value0, value1, value2, null);
	}
	
	/****************************************************************/
	/****************************************************************/
	public TypeMessage(String key, Object value0, Object value1,
	                       Object value2, Object value3) {
		super();
	    this.key = key;
	    values[0] = value0;
	    values[1] = value1;
	    values[2] = value2;
	    values[3] = value3;
	
	}
}
