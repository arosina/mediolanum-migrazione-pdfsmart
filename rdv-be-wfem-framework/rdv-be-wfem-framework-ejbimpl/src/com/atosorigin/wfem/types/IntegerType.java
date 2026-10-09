package com.atosorigin.wfem.types;

import java.math.BigDecimal;

import com.atosorigin.wfem.controller.FieldFormatException;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public final class IntegerType extends AbstractType implements java.io.Serializable{

	public static final int ROUNDING_MODE = BigDecimal.ROUND_HALF_UP;
	public static final int SCALE = 0;

	/****************************************************************/
	/****************************************************************/
	public IntegerType() {
	}
	
	/****************************************************************/
	/****************************************************************/
	public IntegerType(int parameterValue) {
		this(new BigDecimal((double)parameterValue));
	}
	
	/****************************************************************/
	/****************************************************************/
	public IntegerType(IntegerType parameterValue) {
	    this(parameterValue.isNull() ? null : (BigDecimal) parameterValue.getValue());
	}

	/****************************************************************/
	/****************************************************************/
	public IntegerType(java.lang.Integer parameterValue) {
	    this(parameterValue == null ? null : new BigDecimal(parameterValue.intValue()));
	}

	/****************************************************************/
	/****************************************************************/
	public IntegerType(String str) throws FieldFormatException {
	    super(str);
	}

	/****************************************************************/
	/****************************************************************/
	public IntegerType(BigDecimal parameterValue) {
	    setValue(parameterValue);
	}
	
	/****************************************************************/
	/****************************************************************/
	public IntegerType add(IntegerType num) {
	    BigDecimal thisValue = isNull() ? new BigDecimal(0) : (BigDecimal) getValue();
	    BigDecimal numValue = num.isNull() ? new BigDecimal(0) : (BigDecimal) num.getValue();
	
	    return new IntegerType((thisValue).add(numValue));
	}
	
	/****************************************************************/
	/****************************************************************/
	public int compareTo(AbstractType o) {
		
	    return compareTo(new IntegerType(o.toString()));
	}
	
	/****************************************************************/
	/****************************************************************/
	public int compareTo(IntegerType o) {
	
	    return checkNullCompareToNull(o) != Integer.MAX_VALUE
	        ? checkNullCompareToNull(o)
	        : ((BigDecimal) getValue()).compareTo((BigDecimal)o.getValue());
	}
	
	/****************************************************************/
	/****************************************************************/
	public IntegerType divide(IntegerType num) {
	    BigDecimal thisValue = isNull() ? new BigDecimal(0) : (BigDecimal) getValue();
	    BigDecimal numValue = num.isNull() ? new BigDecimal(0) : (BigDecimal) num.getValue();
	
	    return new IntegerType((thisValue).divide(numValue, ROUNDING_MODE));
	}
	
	/****************************************************************/
	/****************************************************************/
	public int intValue() {
		if(isNull())
			return 0;
			
		return ((BigDecimal)getValue()).intValue();
	}

	/****************************************************************/
	/****************************************************************/
	public boolean equals(int i) {
	
		return intValue() == i;
	}
	
	/****************************************************************/
	/****************************************************************/
	public boolean equals(AbstractType o) {
		// in questo modo 0 è uguale a isNull
		// altrementi bisognerebbe verificare prima isNull di entrambi
	    if (o instanceof IntegerType) {
	        return equals(((IntegerType) o).intValue());
	    }
	    return equals(new IntegerType(o.toString()));
	}
	
	/****************************************************************/
	/****************************************************************/
	public boolean equals(Integer i) {
	
		return equals(i.intValue());
	}

	/****************************************************************/
	/****************************************************************/
	public boolean equals(BigDecimal i) {
	
	    return equals(i.intValue());
	}
	
	/****************************************************************/
	/****************************************************************/
	public IntegerType multiply(IntegerType num) {
	    BigDecimal thisValue = isNull() ? new BigDecimal(0) : (BigDecimal) getValue();
	    BigDecimal numValue = num.isNull() ? new BigDecimal(0) : (BigDecimal) num.getValue();
	
	    return new IntegerType((thisValue).multiply(numValue));
	}
	
	
	/****************************************************************/
	/****************************************************************/
	public void setStringValue(java.lang.String newValue) throws FieldFormatException {
	    try {
	        if (newValue == null || newValue.equals("")) {
	            setValue(null);
	        } else {
	            setValue(new BigDecimal(newValue));
	        }
	    } catch (Exception e) {
	        throw new FieldFormatException(e.getMessage());
	    }
	}
	
	/****************************************************************/
	/****************************************************************/
	protected void setValue(BigDecimal newValue) {
	    super.setValue((newValue != null) ? newValue.setScale(SCALE, ROUNDING_MODE) : null);
	}

	/****************************************************************/
	/****************************************************************/
	public IntegerType subtract(IntegerType num) {
	    BigDecimal thisValue = isNull() ? new BigDecimal(0) : (BigDecimal) getValue();
	    BigDecimal numValue = num.isNull() ? new BigDecimal(0) : (BigDecimal) num.getValue();
	
	    return new IntegerType((thisValue).subtract(numValue));
	}
	
	/****************************************************************/
	/****************************************************************/
	public java.lang.String toString() {
	
	    if (isNull())
	        return "";
	
		return getValue().toString();
	}

	/****************************************************************/
	/****************************************************************/
	public BigDecimal bigValue(){
		return (BigDecimal)getValue();
	}
	
	/****************************************************************/
	/****************************************************************/
	public void setBigValue(BigDecimal value){
	    setValue(value);
	}
}
