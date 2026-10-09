package com.atosorigin.wfem.types;

import com.atosorigin.wfem.controller.FieldFormatException;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public final class BooleanType extends AbstractType	implements java.io.Serializable {

	/****************************************************************/
	/****************************************************************/
	public BooleanType() {
		this(new Boolean(false));
	}

	/****************************************************************/
	/****************************************************************/
	public BooleanType(Boolean parameterValue) {
		super(parameterValue);
	}

	/****************************************************************/
	/****************************************************************/
	public BooleanType(String str) throws FieldFormatException {
		super(str);
	}

	/****************************************************************/
	/****************************************************************/
	public BooleanType(boolean parameterValue) {
		this(new Boolean(parameterValue));
	}

	/****************************************************************/
	/****************************************************************/
	public boolean booleanValue() {
		return ((Boolean) getValue()).booleanValue();
	}

	/****************************************************************/
	/****************************************************************/
	public void setBooleanValue(boolean value) {
		setValue(new Boolean(value));
	}
	
	/****************************************************************/
	/****************************************************************/
	public int compareTo(AbstractType o) {

		return compareTo(new BooleanType(o.toString()));
	}

	/****************************************************************/
	/****************************************************************/
	public int compareTo(BooleanType o) {

		boolean oBval = o.booleanValue();
		boolean thisBval = this.booleanValue();
		if (thisBval && !oBval)
			return 1;
		if (!thisBval && oBval)
			return -1;
		return 0;
	}

	/****************************************************************/
	/****************************************************************/
	public boolean equals(boolean b) {

		return booleanValue() == b;
	}

	/****************************************************************/
	/****************************************************************/
	public boolean equals(AbstractType o) {
		int check = checkNullCompareToNull(o);
		if (check == 0)
			return true;
		else if (check == 1 || check == -1)
			return false;
		else {
			if (o instanceof BooleanType) {
				return equals((Boolean) o.getValue());
			}
			return equals(new BooleanType(o.toString()));
		}
	}

	/****************************************************************/
	/****************************************************************/
	public boolean equals(Boolean b) {

		return equals(b.booleanValue());
	}

	/****************************************************************/
	/****************************************************************/
	public void setStringValue(java.lang.String newValue)
		throws com.atosorigin.wfem.controller.FieldFormatException {
		if (newValue == null)
			newValue = "false";
		else if (newValue.equalsIgnoreCase("on")) {
			newValue = "true";
		}
		setValue(new Boolean(newValue));
	}

}
