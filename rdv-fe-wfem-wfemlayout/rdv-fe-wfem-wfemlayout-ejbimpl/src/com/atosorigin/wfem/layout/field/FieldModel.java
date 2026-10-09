package com.atosorigin.wfem.layout.field;

import java.io.Serializable;

import com.atosorigin.wfem.types.AbstractType;

/**************************************************************************************************/
/**************************************************************************************************/
public class FieldModel implements Serializable {

	private String propName;
	private AbstractType propValue;
	private String fieldWidth;
	private String extraPar;
	private int modality;
	private String className;
	private boolean readonly = false;

	public FieldModel(String propName,	AbstractType propValue,	String fieldWidth, 
					   String extraPar,	int modality, String className, boolean readonly) {
		setPropName(propName);
		setPropValue(propValue);
		setFieldWidth(fieldWidth);
		setExtraPar(extraPar);
		setModality(modality);
		setClassName(className);
		setReadonly(readonly);
	}

	public String getExtraPar() {
		return extraPar;
	}

	public String getFieldWidth() {
		return fieldWidth;
	}

	public String getPropName() {
		return propName;
	}

	public AbstractType getPropValue() {
		return propValue;
	}

	public void setExtraPar(String extraPar) {
		this.extraPar = extraPar;
	}

	public void setFieldWidth(String fieldWidth) {
		this.fieldWidth = fieldWidth;
	}

	public void setPropName(String propName) {
		this.propName = propName;
	}

	public void setPropValue(AbstractType propValue) {
		this.propValue = propValue;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public int getModality() {
		return modality;
	}

	public void setModality(int modality) {
		this.modality = modality;
	}

	public boolean isReadonly() {
		return readonly;
	}

	public void setReadonly(boolean readonly) {
		this.readonly = readonly;
	}

}
