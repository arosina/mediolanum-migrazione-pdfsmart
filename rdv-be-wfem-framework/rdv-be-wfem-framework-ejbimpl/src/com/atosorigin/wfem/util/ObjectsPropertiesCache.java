package com.atosorigin.wfem.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;

import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.controller.Configuration;
import com.atosorigin.wfem.types.AbstractTypePropertyDescriptor;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class ObjectsPropertiesCache implements RefreshableCacheContainer{

	transient private static final String thisClassName = ObjectsPropertiesCache.class.getName();
	transient private static ObjectsPropertiesCache singleton = null;
	transient private static com.atosorigin.wfem.loggers.UtilLogger LOG = com.atosorigin.wfem.loggers.UtilLogger.getInstance();
	transient private RefreshableCache cache = new RefreshableCache();

	private boolean useCache = true;
	
	/********************************************************************************/
	/********************************************************************************/
	private ObjectsPropertiesCache() {
		super();
		
		useCache = Configuration.getInstance().isObjectsCacheEnabled();
		if(useCache){
			int scanTime = Configuration.getInstance().getObjectsCacheScanTime();
	        RefreshNotifier.addCache(this,cache,scanTime);
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public void clearCache() {
		if(cache != null)
			cache.clear();
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static ObjectsPropertiesCache getInstance() {
	    if (singleton == null) {
	        synchronized (ObjectsPropertiesCache.class) {
	            if (singleton == null) {
	                singleton = new ObjectsPropertiesCache();
	            }
	        }
	    }
	    return singleton;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public AbstractTypePropertyDescriptor[] getAbstractTypesPropertyDescriptors(Object o) throws Exception {
	
		ObjectProperties objProps = (ObjectProperties)cache.get(o.getClass().getName());
		if(objProps == null){
			objProps = loadObject(o);
		}
		
		AbstractTypePropertyDescriptor[] props = objProps.getAbstractTypesPropertyDescriptors();
		if(o instanceof MapCommandDataModel)
			props = ((MapCommandDataModel)o).addMappedPropertyDescriptors(objProps.getAbstractTypesPropertyDescriptors());
		return props;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public AbstractTypePropertyDescriptor getAbstractTypePropertyDescriptor(Object o, 
																			String propertyName) throws Exception {
		
		ObjectProperties objProps = (ObjectProperties)cache.get(o.getClass().getName());
		if(objProps == null){
			objProps = loadObject(o);
		}
	
		AbstractTypePropertyDescriptor prop = objProps.getAbstractTypePropertyDescriptor(propertyName);
		if(o instanceof MapCommandDataModel)
			prop = ((MapCommandDataModel)o).getMappedPropertyDescriptor(propertyName,prop);
		
		return prop;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public PropertyDescriptor[] getProperties(Object o) throws Exception {
	
		ObjectProperties objProps = (ObjectProperties)cache.get(o.getClass().getName());
		if(objProps == null){
			objProps = loadObject(o);
		}
		return objProps.getProperties();
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public PropertyDescriptor getProperty(Object o, String propertyName) throws Exception {
		
		ObjectProperties objProps = (ObjectProperties)cache.get(o.getClass().getName());
		if(objProps == null){
			objProps = loadObject(o);
		}
	
		PropertyDescriptor prop = objProps.getProperty(propertyName);
		return prop;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private ObjectProperties loadObject(Object o) throws Exception {
		
		try{
			
			BeanInfo bi = Introspector.getBeanInfo(o.getClass(),Object.class);
			PropertyDescriptor[] props = bi.getPropertyDescriptors();
			ObjectProperties objProps = new ObjectProperties(props);
			
			if(useCache)
				cache.put(o.getClass().getName(),objProps);
			
			return objProps;
			
		}catch(Exception e){
			String errorMsg = thisClassName+".loadObject(Object obj): Exception in caching object class "+o.getClass()+": "+e;
			Exception ne = new Exception(errorMsg);
			LOG.error(ne);
			throw ne;
		}
	}
}
