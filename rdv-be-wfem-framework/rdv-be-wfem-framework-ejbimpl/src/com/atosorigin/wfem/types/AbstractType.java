package com.atosorigin.wfem.types;

import java.util.List;
import java.util.Vector;

import com.atosorigin.wfem.controller.FieldFormatException;
import com.atosorigin.wfem.layout.FieldStyle;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public abstract class AbstractType implements java.io.Serializable, java.lang.Comparable
{
	public static final int PRINTABLE_NORMAL_TYPE = 0;
	public static final int PRINTABLE_GROUP_TYPE  = 1;
	
	private int printableType = PRINTABLE_NORMAL_TYPE; // Default printable type
	private String printableString = null;
	
	private List typeErrors;
	private List typeWarnings;
	private List typeMessages;
	
	private java.lang.Object value;
	private boolean skippable = false;
	private boolean valid = true;
	private boolean changed = false;
	private boolean visible = true;
	private boolean editable = true;
	
	private transient FieldStyle style;
	
	/****************************************************************/
	/****************************************************************/
	public AbstractType() {}
	
	/****************************************************************/
	/****************************************************************/
	public AbstractType(Object value) {
	    setValue(value);
	}
	
	/****************************************************************/
	/****************************************************************/
	public AbstractType(String str) {
	    setStringValue(str);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void addTypeError(String error) {
		addTypeError(new TypeError(error));
	}	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void addTypeError(String emitter, int progr, String error) {
		if(typeErrors == null)
			typeErrors = new Vector();
		TypeError typeError = getTypeError(emitter, progr);
		if(typeError != null)
			typeErrors.remove(typeError);
		typeError = new TypeError(emitter, progr, error);
		typeErrors.add(typeError);
	}	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void addTypeError(TypeError typeError) {
		if(typeErrors == null)
			typeErrors = new Vector();
		if(typeErrors.contains(typeError))
			return;
		typeErrors.add(typeError);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void removeTypeError(String emitter, int progr) {
		if(typeErrors == null)
			return;
		for(int i=typeErrors.size()-1;i>=0;i--){
			TypeError error = (TypeError)typeErrors.get(i);
			if((emitter+"-"+progr).equals(error.code)){
				typeErrors.remove(i);
			}
		}
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void removeTypeError(String errorCode) {
		if(typeErrors == null)
			return;
		for(int i=typeErrors.size()-1;i>=0;i--){
			TypeError error = (TypeError)typeErrors.get(i);
			if(error.toString().equals(errorCode)){
				typeErrors.remove(i);
			}
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void replaceTypeError(String emitter, int progr, TypeError newError) {
		if(typeErrors == null)
			return;
		for(int i=0;i<typeErrors.size();i++){
			TypeError error = (TypeError)typeErrors.get(i);
			if((emitter+"-"+progr).equals(error.code)){
				typeErrors.set(i,newError);
				return;
			}
		}
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void replaceTypeError(String emitter, int progr, String newKey) {
		if(typeErrors == null)
			return;
		for(int i=0;i<typeErrors.size();i++){
			TypeError error = (TypeError)typeErrors.get(i);
			if((emitter+"-"+progr).equals(error.code)){
				typeErrors.set(i,new TypeError(emitter, progr, newKey));
				return;
			}
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void addTypeWarning(String warning) {
		addTypeWarning(new TypeWarning(warning));
	}	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void addTypeWarning(TypeWarning typeWarning) {
		if(typeWarnings == null)
			typeWarnings = new Vector();
		if(typeWarnings.contains(typeWarning))
			return;
		typeWarnings.add(typeWarning);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void addTypeMessage(String message) {
		addTypeMessage(new TypeMessage(message));
	}	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void addTypeMessage(TypeMessage typeMessage) {
		if(typeMessages == null)
			typeMessages = new Vector();
		if(typeMessages.contains(typeMessage))
			return;
		typeMessages.add(typeMessage);
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public TypeError getTypeError(String emitter, int progr) {
		if(typeErrors == null)
			return null;
		for(int i=0;i<typeErrors.size();i++){
			TypeError t = (TypeError)typeErrors.get(i);
			if((emitter+"-"+progr).equals(t.code))
				return t; 
		}
		return null;
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public java.util.List getTypeErrors() {
		return typeErrors;
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public java.util.List getTypeWarnings() {
		return typeWarnings;
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public java.util.List getTypeMessages() {
		return typeMessages;
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean hasTypeErrors() {
		if(typeErrors == null)
			return false;
		return (this.typeErrors.size() > 0);
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean hasTypeWarnings() {
		if(typeWarnings == null || isSkippable())
			return false;
		return (this.typeWarnings.size() > 0);
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean hasTypeMessages() {
		if(typeMessages == null)
			return false;
		return (this.typeMessages.size() > 0);
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void resetTypeErrors() {
		typeErrors = null;
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void resetTypeWarnings() {
		typeWarnings = null;
	}
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void resetTypeMessages() {
		typeMessages = null;
	}
	/****************************************************************/
	/****************************************************************/
	public void setValid(boolean valid){
		this.valid = valid;
	}
	/****************************************************************/
	/****************************************************************/
	public void setSkippable(boolean skippable) {
		this.skippable = skippable;
	}	
	
	/****************************************************************/
	/****************************************************************/
	protected int checkNullCompareToNull(AbstractType o) {
	    if (this.isNull() && o.isNull())
	        return 0;
	
	    if (this.isNull() && !o.isNull())
	        return -1;
	
	    if (!this.isNull() && o.isNull())
	        return 1;
	
	    return Integer.MAX_VALUE;
	}
	
	/****************************************************************/
	/****************************************************************/
	public abstract int compareTo(AbstractType o);
	
	/****************************************************************/
	/****************************************************************/
	@Override
	public int compareTo(Object o){
	    return compareTo((AbstractType) o);
	}
	
	/****************************************************************/
	/****************************************************************/
	public abstract boolean equals(AbstractType o);
	
	/****************************************************************/
	/****************************************************************/
	@Override
	public boolean equals(Object o){
	
	    if (o != null)
	    	return equals((AbstractType) o);
	    return false;
	}
	
	/****************************************************************/
	/****************************************************************/
	public boolean equals(String str){
		
		return toString().equals(str);
	}
	
	/****************************************************************/
	/****************************************************************/
	public boolean equalsIgnoreCase(String str){
		
		return toString().equalsIgnoreCase(str);
	}
	
	/****************************************************************/
	/****************************************************************/
	public int getPrintableType(){
		return printableType;
	}
	
	/****************************************************************/
	/****************************************************************/
	public String getPrintableValue(){
		if(printableString == null)
			return toString();
		else
			return printableString;
	}
	
	/****************************************************************/
	/****************************************************************/
	protected java.lang.Object getValue(){
		return value;
	}
	
	/****************************************************************/
	/****************************************************************/
	@Override
	public int hashCode(){
		return toString().hashCode();
	}
	
	/****************************************************************/
	/****************************************************************/
	public boolean isNull(){
		if(value == null)
			return true;
		else
			return false;
	}
	
	/****************************************************************/
	/****************************************************************/
	public boolean isValid(){
		return valid;
	}
	
	/****************************************************************/
	/****************************************************************/
	public boolean isVisible(){
		return visible;
	}
	
	/****************************************************************/
	/****************************************************************/
	public static AbstractType newInstance(Class type, String value){
	
	    try {
	        AbstractType temp = (AbstractType) type.newInstance();
	        temp.setStringValue(value);
	        return temp;
	
	    } catch (FieldFormatException ffex) {
			throw ffex;
	    } catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
	    }
	}
	
	/****************************************************************/
	/****************************************************************/
	public void setPrintableType(int newPrintableType){
		printableType = newPrintableType;
		if(printableType == PRINTABLE_GROUP_TYPE)
			printableString = "X";
	}
	
	/****************************************************************/
	/****************************************************************/
	public void setPrintableType(int printableType, String printableString){
		this.printableType = printableType;
		this.printableString = printableString;
	}
	
	/****************************************************************/
	/****************************************************************/
	public void setPrintableValue(String newPrintableString){
		this.printableString = newPrintableString;
	}
	
	/****************************************************************/
	/****************************************************************/
	public abstract void setStringValue(java.lang.String newValue)
	    throws FieldFormatException;
	
	
	/****************************************************************/
	/****************************************************************/
	public java.lang.String getStringValue(){
		return this.toString();
	}
	
	/****************************************************************/
	/****************************************************************/
	protected void setValue(java.lang.Object newValue){
		value = newValue;
	}
	
	/****************************************************************/
	/****************************************************************/
	public void setVisible(boolean newVisible){
		visible = newVisible;
	}
	
	/****************************************************************/
	/****************************************************************/
	@Override
	public String toString(){
		if(isNull())
			return "";
			
		return getValue().toString();
	}

	/****************************************************************/
	/****************************************************************/
	public boolean isChanged() {
		return changed;
	}

	/****************************************************************/
	/****************************************************************/
	public void setChanged(boolean changed) {
		this.changed = changed;
	}

	/****************************************************************/
	/****************************************************************/
	public boolean isSkippable() {
		return skippable;
	}
	
	/****************************************************************/
	/****************************************************************/
	public void setTypeErrors(List typeErrors) {
		this.typeErrors = typeErrors;
	}

	/****************************************************************/
	/****************************************************************/
	public void setTypeMessages(List typeMessages) {
		this.typeMessages = typeMessages;
	}

	/****************************************************************/
	/****************************************************************/
	public void setTypeWarnings(List typeWarnings) {
		this.typeWarnings = typeWarnings;
	}

	/****************************************************************/
	/****************************************************************/
	@Override
	public Object clone() throws CloneNotSupportedException {
		AbstractType ret = newInstance(this.getClass(), getStringValue());
		ret.setTypeErrors(getTypeErrors());
		ret.setTypeMessages(getTypeMessages());
		ret.setTypeWarnings(getTypeWarnings());
	
		ret.setSkippable(isSkippable());
		ret.setValid(isValid());
		ret.setChanged(isChanged());
		ret.setVisible(isVisible());
		return ret;
	}

	/****************************************************************/
	/****************************************************************/
	public boolean isEditable() {
		return editable;
	}

	/****************************************************************/
	/****************************************************************/
	public void setEditable(boolean editable) {
		this.editable = editable;
	}
	
	/****************************************************************/
	/****************************************************************/
	public FieldStyle getStyle() {
		return style;
	}

	/****************************************************************/
	/****************************************************************/
	public void setStyle(FieldStyle style) {
		this.style = style;
	}

}
