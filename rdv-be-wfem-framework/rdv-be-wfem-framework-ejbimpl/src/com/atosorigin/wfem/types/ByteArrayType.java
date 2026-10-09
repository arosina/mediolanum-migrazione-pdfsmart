package com.atosorigin.wfem.types;

import com.atosorigin.wfem.controller.FieldFormatException;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class ByteArrayType extends AbstractType implements java.io.Serializable {

	/****************************************************************/
	/****************************************************************/
	public ByteArrayType() {
	}

	/****************************************************************/
	/****************************************************************/
	public ByteArrayType(byte[] parameterValue) {
		super(parameterValue);
	}
	
	/****************************************************************/
	/****************************************************************/
	public byte[] byteArrayValue(){
		return (byte[])getValue();
	}
	
	/****************************************************************/
	/****************************************************************/
	public void setByteArrayValue(byte[] value){
		setValue(value);
	}

	/****************************************************************/
	/****************************************************************/
	public int compareTo(AbstractType o) {
		return 0;
	}

	/****************************************************************/
	/****************************************************************/
	public boolean equals(AbstractType o) {
		return false;
	}

	/****************************************************************/
	/****************************************************************/
	public void setStringValue(String newValue) throws FieldFormatException{
		throw new FieldFormatException("Impossible to set a String into a ByteArray");
	}

}
