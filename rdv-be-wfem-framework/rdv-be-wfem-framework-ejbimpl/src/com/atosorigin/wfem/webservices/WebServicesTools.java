package com.atosorigin.wfem.webservices;

import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.controller.FieldFormatException;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.SimpleTypesMap;
import com.atosorigin.wfem.types.TypeError;
import com.atosorigin.wfem.util.ObjectsPropertiesCache;
import com.atosorigin.wfem.util.Tools;

/***********************************************************************************************/
/***********************************************************************************************/
public class WebServicesTools {

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void initModelProp(CommandDataModel model, String propName, String propValue){
		AbstractType prop = null;
		try{
			prop = (AbstractType)Tools.getPropertyValue(model,propName);
			if(prop != null){
				prop.setStringValue(propValue);
			}else{
				Class propClass = Tools.getPropertyType(model,propName);
				if(propClass != null && AbstractType.class.isAssignableFrom(propClass)){
					prop =AbstractType.newInstance(propClass, propValue);
					Tools.setPropertyValue(model,propName,prop);
				}
			}
		}catch(FieldFormatException ffe){
			if(prop != null)
				prop.addTypeError(new TypeError(prop.getClass().getName().substring(prop.getClass().getName().lastIndexOf(".")+1)+"FormatException"));
		}catch(Exception e){
			if(prop != null)
				prop.addTypeError(new TypeError(prop.getClass().getName().substring(prop.getClass().getName().lastIndexOf(".")+1)+"Exception"));
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void initSkippableFields(CommandDataModel model, String skippableFields) throws Exception{
		if(skippableFields == null || skippableFields.length() == 0)
			return;
		model.setSkippableFields(skippableFields);
		String[] propNames = skippableFields.split("\\,");
		for(int i=0;i<propNames.length;i++){
			AbstractType t = (AbstractType)Tools.getPropertyValue(model, propNames[i]);
			if(t != null)
				t.setSkippable(true);
		}
	}
	
    /********************************************************************************/
    /********************************************************************************/
    public static String xmlFromBean(Object bean) throws Exception{
        String xmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        xmlString += "<bean>";
        xmlString += 	transformBeanToXmlNode(bean);
        xmlString += "</bean>";
        return xmlString;
    }

    /********************************************************************************/
    /********************************************************************************/
    private static String transformBeanToXmlNode(Object bean) throws Exception{
            
        StringBuffer xmlString = new StringBuffer();
        
        try{
            
            PropertyDescriptor[] props = ObjectsPropertiesCache.getInstance().getProperties(bean);
            if(props == null)
                return xmlString.toString();
                    
            for(int i=0;i<props.length;i++){
                String propName = props[i].getName();
                Object propValue = Tools.getPropertyValue(bean,propName);
                if(propValue == null)
                        continue;
                
                if(isSimpleProperty(propValue.getClass()) || propValue.getClass().isEnum()){
                	String pVal = propValue.toString();
                	if(pVal.isEmpty())
                		continue;
                    if(propValue instanceof String)
                        pVal = Tools.stringToXMLString(propValue.toString());
                   	xmlString.append("<"+propName+">"+pVal+"</"+propName+">");
                    continue;
                }
                
                if(propValue.getClass().isArray()){
                    Object[] list = (Object[])propValue;
                    if(list.length == 0)
                        continue;
                    for(int j=0;j<list.length;j++){
                        Object listElement = list[j];
                    	String pVal = transformBeanToXmlNode(listElement);
                    	if(pVal.isEmpty())
                    		continue;                        	
                        xmlString.append("<"+propName+">"+pVal+"</"+propName+">");
                    }
                    continue;
                }

                try{
                	String pVal = transformBeanToXmlNode(propValue);
                	if(pVal.isEmpty())
                		continue;                        	
                    xmlString.append("<"+propName+">"+pVal+"</"+propName+">");
                	continue;
                }catch(Throwable t){}
                
            }
            return xmlString.toString();
                
        }catch(Exception e){
            String errMsg = "Tools.transformBeanToXmlNode: Exception in transforming into xml bean ["+bean+"]: "+e;
            e = new Exception(errMsg);
            e.printStackTrace();
            throw e;
        }
    }
    
    /********************************************************************************/
    /********************************************************************************/
    private static int classCode(Class propType){
        int classCode = SimpleTypesMap.getClassCode(propType);
    	if(classCode < 0 && propType.isPrimitive()){
    		if(propType.equals(int.class))
    			classCode = SimpleTypesMap.INTEGER;
    		else if(propType.equals(long.class))
    			classCode = SimpleTypesMap.LONG;
    		else if(propType.equals(double.class))
    			classCode = SimpleTypesMap.DOUBLE;
    		else if(propType.equals(boolean.class))
    			classCode = SimpleTypesMap.BOOLEAN;
    		else
    			classCode = -1;
    	}
    	return classCode;
    }
    
    /********************************************************************************/
    /********************************************************************************/
    public static boolean isSimpleProperty(Class propType){
        return classCode(propType) > 0;
    }

	/********************************************************************************/
	/********************************************************************************/
	public static Map<String, String> notNullModelPropertiseMap(Object bean) throws Exception{
		Map<String, String> result = new HashMap<String, String>();
		recurseNotNullModelPropertiseMap(bean,"",result);
		return result;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private static void recurseNotNullModelPropertiseMap(Object bean, String ident, Map<String, String> proplist) throws Exception{
		
		PropertyDescriptor[] props = ObjectsPropertiesCache.getInstance().getProperties(bean);
		if(props == null)
			return;
			
		for(int i=0;i<props.length;i++){
            String propName = props[i].getName();
            Object propValue = Tools.getPropertyValue(bean,propName);
            if(propValue == null)
                    continue;
            
            if(isSimpleProperty(propValue.getClass()) || propValue.getClass().isEnum()){
				String lab = ident+"_"+propName;
				if(ident.equals(""))
					lab = propName;
            	String pVal = propValue.toString();
            	if(pVal.isEmpty())
            		continue;
            	proplist.put(lab,propValue.toString());
                continue;
            }
            
            if(propValue.getClass().isArray()){
                Object[] list = (Object[])propValue;
                if(list.length == 0)
                    continue;
                for(int j=0;j<list.length;j++){
                    Object listElement = list[j];
                    recurseNotNullModelPropertiseMap(listElement,ident+"_"+propName+j,proplist);
                }
                continue;
            }

            try{
				String lab = ident+"_"+propName;
				if(ident.equals(""))
					lab = propName;
				recurseNotNullModelPropertiseMap(propValue,lab,proplist);
            	continue;
            }catch(Throwable t){
            	t.printStackTrace();
            }
		}
		return;
			
	}
	
}
