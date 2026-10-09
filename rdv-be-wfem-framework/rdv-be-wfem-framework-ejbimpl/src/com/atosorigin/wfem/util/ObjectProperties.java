package com.atosorigin.wfem.util;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.Hashtable;

import com.atosorigin.wfem.controller.Configuration;
import com.atosorigin.wfem.types.AbstractTypePropertyDescriptor;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
class ObjectProperties extends RefreshableCacheElement implements Serializable{

	private Hashtable propsIndex = null;
	private AbstractTypePropertyDescriptor[] wfemProps = null;
		
	/********************************************************************************/
	/********************************************************************************/
	protected ObjectProperties(AbstractTypePropertyDescriptor[] props) {
		super();
		
		propsIndex = new Hashtable();
		wfemProps = props;
	
		String propName = null;
		for(int i=0;i<props.length;i++){
			propName = props[i].getName();
			propsIndex.put(propName,new Integer(i));
		}
	}

	/********************************************************************************/
	/********************************************************************************/
	protected ObjectProperties(PropertyDescriptor[] props) {
		super();
		
		propsIndex = new Hashtable();
		wfemProps = new AbstractTypePropertyDescriptor[props.length];
		for(int i=0;i<props.length;i++)
			wfemProps[i] = new AbstractTypePropertyDescriptor(props[i]);
		
		String propName = null;
		for(int i=0;i<props.length;i++){
			propName = props[i].getName();
			propsIndex.put(propName,new Integer(i));
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	protected AbstractTypePropertyDescriptor[] getAbstractTypesPropertyDescriptors() {
		return wfemProps;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	protected AbstractTypePropertyDescriptor getAbstractTypePropertyDescriptor(String propertyName) {
		Integer idx = (Integer)propsIndex.get(propertyName);
		if(idx == null)
			return null;
		else
			return wfemProps[idx.intValue()];
	}

	/********************************************************************************/
	/********************************************************************************/
	protected PropertyDescriptor[] getProperties() {
		PropertyDescriptor[] pd = new PropertyDescriptor[wfemProps.length];
		for(int i=0;i<wfemProps.length;i++)
			pd[i] = wfemProps[i].getPropertyDescriptor();
		return pd;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	protected PropertyDescriptor getProperty(String propertyName) {
		Integer idx = (Integer)propsIndex.get(propertyName);
		if(idx == null)
			return null;
		else
			return wfemProps[idx.intValue()].getPropertyDescriptor();
	}

	/********************************************************************************/
	/********************************************************************************/
	public int getRefreshTime() {
		return Configuration.getInstance().getObjectsLifeTime();
	}

}
