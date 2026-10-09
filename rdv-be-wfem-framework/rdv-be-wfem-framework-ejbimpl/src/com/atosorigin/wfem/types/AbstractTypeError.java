package com.atosorigin.wfem.types;

import java.io.Serializable;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public abstract class AbstractTypeError implements Serializable, Comparable {

	protected String code = null; 
    protected String key = null;
    protected Object values[] = { null, null, null, null };
    
	/****************************************************************/
	/****************************************************************/
	public String getKey() {
		return (this.key);
	}
	
	/****************************************************************/
	/****************************************************************/
	public Object[] getValues() {
		 return (this.values);
	}
	
	/****************************************************************/
	/****************************************************************/
	public String toString() {
		return getKey();
	}
	
	/****************************************************************/
	/****************************************************************/
	public boolean equals(Object o) {
		return toString().equals(o.toString());
	}
	
	/****************************************************************/
	/****************************************************************/
	public int compareTo(Object o) {
		return toString().compareTo(o.toString());
	}

}
