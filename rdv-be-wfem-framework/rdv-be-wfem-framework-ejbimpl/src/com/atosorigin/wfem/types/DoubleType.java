package com.atosorigin.wfem.types;

import java.math.BigDecimal;
import java.text.NumberFormat;

import com.atosorigin.wfem.controller.FieldFormatException;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public final class DoubleType extends AbstractType implements java.io.Serializable{

	public static final int ROUNDING_MODE = BigDecimal.ROUND_HALF_UP;
	public static final int MAX_SCALE = 5;

	/****************************************************************/
	/****************************************************************/
	public DoubleType() {
	}
	
	/****************************************************************/
	/****************************************************************/
	public DoubleType(double parameterValue) {
		this(new BigDecimal(parameterValue));
	}
	
	/****************************************************************/
	/****************************************************************/
	public DoubleType(DoubleType parameterValue) {
	    this(parameterValue.isNull() ? null : (BigDecimal) parameterValue.getValue());
	}
	
	/****************************************************************/
	/****************************************************************/
	public DoubleType(java.lang.Double parameterValue) {
	    this(parameterValue == null ? null : new BigDecimal(parameterValue.doubleValue()));
	}
	
	/****************************************************************/
	/****************************************************************/
	public DoubleType(String str) throws FieldFormatException {
	    super(str);
	}
	
	/****************************************************************/
	/****************************************************************/
	public DoubleType(BigDecimal parameterValue) {
	    setValue(parameterValue);
	}
	
	/****************************************************************/
	/****************************************************************/
	public DoubleType add(DoubleType num) {
	    BigDecimal thisValue = isNull() ? new BigDecimal(0) : (BigDecimal) getValue();
	    BigDecimal numValue = num.isNull() ? new BigDecimal(0) : (BigDecimal) num.getValue();
	
	    return new DoubleType((thisValue).add(numValue));
	}
	
	/****************************************************************/
	/****************************************************************/
	public int compareTo(AbstractType o) {
		
	    return compareTo(new DoubleType(o.toString()));
	}
	
	/****************************************************************/
	/****************************************************************/
	public int compareTo(DoubleType o) {
	
	    return checkNullCompareToNull(o) != Integer.MAX_VALUE
	        ? checkNullCompareToNull(o)
	        : ((BigDecimal) getValue()).compareTo((BigDecimal)o.getValue());
	}
	
	/****************************************************************/
	/****************************************************************/
	public DoubleType divide(DoubleType num) {
	    BigDecimal thisValue = isNull() ? new BigDecimal(0) : (BigDecimal) getValue();
	    BigDecimal numValue = num.isNull() ? new BigDecimal(0) : (BigDecimal) num.getValue();
	
	    return new DoubleType((thisValue).divide(numValue, ROUNDING_MODE));
	}
	
	/****************************************************************/
	/****************************************************************/
	public double doubleValue() {
		if(isNull())
			return 0;
			
		return ((BigDecimal)getValue()).doubleValue();
	}
	
	/****************************************************************/
	/****************************************************************/
	public boolean equals(double d) {
	
		return doubleValue() == d;
	}
	
	/****************************************************************/
	/****************************************************************/
	public boolean equals(AbstractType o) {
		// in questo modo 0 è uguale a isNull
		// altrementi bisognerebbe verificare prima isNull di entrambi
	    if (o instanceof DoubleType) {
	        return equals(((DoubleType) o).doubleValue());
	    }
	    return equals(new DoubleType(o.toString()));
	}
	
	/****************************************************************/
	/****************************************************************/
	public boolean equals(Double d) {
	
	    return equals(d.doubleValue());
	}
	
	/****************************************************************/
	/****************************************************************/
	public boolean equals(BigDecimal d) {
	
	    return equals(d.doubleValue());
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
	public DoubleType multiply(DoubleType num) {
	    BigDecimal thisValue = isNull() ? new BigDecimal(0) : (BigDecimal) getValue();
	    BigDecimal numValue = num.isNull() ? new BigDecimal(0) : (BigDecimal) num.getValue();
	
	    return new DoubleType((thisValue).multiply(numValue));
	}
	
	/****************************************************************/
	/****************************************************************/
	public void setStringValue(java.lang.String newValue) throws FieldFormatException {
	    try {
	        if (newValue == null || newValue.equals("")) {
	            setValue(null);
	        } else {
	            Number num = NumberFormat.getInstance(java.util.Locale.ITALY).parse(newValue);
	            setValue(new BigDecimal(num.doubleValue()));
	        }
	    } catch (Exception e) {
	        throw new FieldFormatException(e.getMessage());
	    }
	}
	
	/****************************************************************/
	/****************************************************************/
	protected void setValue(BigDecimal newValue) {
		int scale = MAX_SCALE;
        if (newValue != null){
            scale = newValue.scale();
	        if(scale > MAX_SCALE)
	        	scale = MAX_SCALE;
        }
	    super.setValue((newValue != null) ? newValue.setScale(scale, ROUNDING_MODE) : null);
	}
	
	/****************************************************************/
	/****************************************************************/
	public DoubleType subtract(DoubleType num) {
	    BigDecimal thisValue = isNull() ? new BigDecimal(0) : (BigDecimal) getValue();
	    BigDecimal numValue = num.isNull() ? new BigDecimal(0) : (BigDecimal) num.getValue();
	
	    return new DoubleType((thisValue).subtract(numValue));
	}
	
	/****************************************************************/
	/****************************************************************/
	public java.lang.String toString() {
	
	    if (isNull())
	        return "";
	
	    return NumberFormat.getInstance(java.util.Locale.ITALY).format(((BigDecimal) getValue()).doubleValue());
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String toScaledString(int scale){
		if(isNull())
			return "";
		if(scale < 0)
			return toString();
		BigDecimal bd = bigValue().setScale(scale,BigDecimal.ROUND_HALF_UP);
		NumberFormat nf = NumberFormat.getInstance(java.util.Locale.ITALY);
		nf.setMinimumFractionDigits(scale);
		return nf.format(bd.doubleValue());
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

	/****************************************************************/
	/****************************************************************/
	public String getPrintableValue() {
		int scale = 2;
		if(!isNull()){
			String sval = toString();
			String decimals = sval.substring(sval.lastIndexOf(',')+1);
			if(!sval.equals(decimals) && decimals.length() > 2)
				scale = decimals.length();
		}
		return toScaledString(scale);
	}
	
}
