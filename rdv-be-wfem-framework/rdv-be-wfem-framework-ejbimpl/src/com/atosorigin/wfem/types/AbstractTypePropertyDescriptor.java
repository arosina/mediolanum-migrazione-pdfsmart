package com.atosorigin.wfem.types;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;

import com.atosorigin.wfem.command.MapCommandDataModel;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class AbstractTypePropertyDescriptor {

	private String name;
	private AbstractType value;
	private Method readMethod;
	private Method writeMethod;
	private PropertyDescriptor propertyDescriptor;
	
	public AbstractTypePropertyDescriptor(String fieldName, AbstractType fieldValue,int index){
		this.propertyDescriptor = null;
		this.name = fieldName;
		this.value = fieldValue;
		try{
			this.readMethod = MapCommandDataModel.class.getMethod("readFf"+index,null);
			this.writeMethod = MapCommandDataModel.class.getMethod("writeFf"+index,new Class[]{AbstractType.class});
		}catch(Exception e){e.printStackTrace();};
	}

	public AbstractTypePropertyDescriptor(PropertyDescriptor propertyDescriptor){
		this.propertyDescriptor = propertyDescriptor;
	}
	
	public PropertyDescriptor getPropertyDescriptor() {
		return propertyDescriptor;
	}

	public Method getReadMethod() {
		if(propertyDescriptor != null)
			return propertyDescriptor.getReadMethod();
		return readMethod;
	}
	
	public Method getWriteMethod() {
		if(propertyDescriptor != null)
			return propertyDescriptor.getWriteMethod();
		return writeMethod;
	}
	
	public String getName() {
		if(propertyDescriptor != null)
			return propertyDescriptor.getName();
		return name;
	}

	public Class getPropertyType() {
		if(propertyDescriptor != null)
			return propertyDescriptor.getPropertyType();
		return value.getClass();
	}
	
	public AbstractType getValue() throws Exception{
		if(propertyDescriptor != null)
			return (AbstractType)propertyDescriptor.getPropertyType().newInstance();
		return value;
	}

}
