package com.atosorigin.wfem.command;


import java.io.Serializable;

/****************************************************************/
/****************************************************************/
public class CommandError implements Serializable, Comparable {

    private String key = null;
    private Object values[] = { null, null, null, null };

	/****************************************************************/
	/****************************************************************/
	public CommandError( String key ) {
	
		this(key, null, null, null, null);
	}
	
	/****************************************************************/
	/****************************************************************/
	public CommandError( String key, Object value0 ) {
	
		this(key, value0, null, null, null);
	}
	
	/****************************************************************/
	/****************************************************************/
	public CommandError( String key, Object value0, Object value1 ) {
	
		this(key, value0, value1, null, null);
	}
	
	/****************************************************************/
	/****************************************************************/
	public CommandError( String key, Object value0, Object value1, Object value2 ) {
	
		this(key, value0, value1, value2, null);
	}
	
	/****************************************************************/
	/****************************************************************/
	public CommandError(String key, Object value0, Object value1,
	                       Object value2, Object value3) {
		super();
	    this.key = key;
	    values[0] = value0;
	    values[1] = value1;
	    values[2] = value2;
	    values[3] = value3;
	
	}
	
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
	
	/****************************************************************/
	/****************************************************************/
	public void setValuesListAsString(String strValues, String separator){
	    
	    if(values == null)
	    	return;
	    
	    String temp = strValues;
	    String[] valuesArr = new String[this.values.length];
	    int index = 0; int count = 0;
	    while((index = temp.indexOf(separator)) != -1) {
	      String value = temp.substring(0,index);
	      valuesArr[count++] = value;
	      temp = temp.substring(index+separator.length());
	    }
	    if(!"".equals(temp)){
	      valuesArr[count++] = temp;
	    }
	    for ( int i = 0; i < count && i < this.values.length; i++ ) {
	      this.values[i] = (Object) valuesArr[i];
	    }
	}
	
}
