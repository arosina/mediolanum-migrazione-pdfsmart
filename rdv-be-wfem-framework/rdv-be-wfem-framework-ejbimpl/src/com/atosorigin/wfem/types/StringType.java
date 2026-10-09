package com.atosorigin.wfem.types;


/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public final class StringType extends AbstractType implements java.io.Serializable {

	/****************************************************************/
	/****************************************************************/
	public StringType() {
	}

	/****************************************************************/
	/****************************************************************/
	public StringType(String parameterValue) {
		super(parameterValue);
	}

	/****************************************************************/
	/****************************************************************/
	public int compareTo(AbstractType o) {
		return compareTo(new StringType(o.toString()));
	}

	/****************************************************************/
	/****************************************************************/
	public int compareTo(StringType o) {

		return checkNullCompareToNull(o) != Integer.MAX_VALUE
			? checkNullCompareToNull(o)
			: ((String) getValue()).compareTo((String)o.getValue());
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
			if (o instanceof StringType) {
				return equals((String) o.getValue());
			}
			return equals(new StringType(o.toString()));
		}
	}

	/****************************************************************/
	/****************************************************************/
	public boolean equals(StringType str) {
		return toString().equals(str.toString());
}

	/****************************************************************/
	/****************************************************************/
	public void setStringValue(java.lang.String newValue)
		throws com.atosorigin.wfem.controller.FieldFormatException {
		if (newValue == null || newValue.equals(""))
			setValue(null);
		else
			setValue(newValue);
	}

	/****************************************************************/
	/****************************************************************/
	public String stringValue() {
		return (String) getValue();
	}

}
