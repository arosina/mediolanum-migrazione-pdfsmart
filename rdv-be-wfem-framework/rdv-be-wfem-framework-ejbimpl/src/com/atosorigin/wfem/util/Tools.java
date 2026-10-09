package com.atosorigin.wfem.util;

import java.beans.PropertyDescriptor;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.text.html.parser.ParserDelegator;

import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.atosorigin.wfem.coddesc.CodDescData;
import com.atosorigin.wfem.coddesc.CodDescDataList;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.ListCommandDataModel;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.AbstractTypePropertyDescriptor;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;
import com.atosorigin.wfem.types.TypeError;
import com.atosorigin.wfem.types.TypeWarning;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class Tools implements java.io.Serializable{

	transient private static final String thisClassName = Tools.class.getName();

	transient private static com.atosorigin.wfem.loggers.UtilLogger LOG = com.atosorigin.wfem.loggers.UtilLogger.getInstance();

	transient private static Pattern patternJsp10 = Pattern.compile("\\<\\%\\=([A-Za-z\\_0-9]+)\\%\\>");
	transient private static Pattern patternJsp20 = Pattern.compile("\\$\\{([A-Za-z\\_0-9]+)\\}");
	
	/********************************************************************************/
	/********************************************************************************/
	private static class HtmlParser extends javax.swing.text.html.HTMLEditorKit.ParserCallback{
		private InputStreamReader isr;
		private String result = "";
		private boolean keepHtmlBlank;
		private ParserDelegator parserDelegator = new ParserDelegator();
		
		public HtmlParser(String htmlString, boolean keepHtmlBlank){
		    ByteArrayInputStream inpHtml = new ByteArrayInputStream(htmlString.getBytes());
			isr = new InputStreamReader(inpHtml);
			this.keepHtmlBlank=keepHtmlBlank;
			try{
				parserDelegator.parse(isr,this,true);
			}catch(Exception e){
				e = new Exception("Tools.xmlParsing: Exception in parsing xml string: "+e.toString());
				e.printStackTrace();
			}
		}
		public void handleText(char[] data, int pos){
			if(!keepHtmlBlank){
				for(int i=0;i<data.length;i++)
					if(data[i] == 160) data[i] = 32;
			}
			result += String.valueOf(data);
		}
		public String getResult(){
			return result;
		}			
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public Tools() {
		super();
	}
	
	private static final char sp = 0xffff & ' '; 
	private static final char zero = 0xffff & '0'; 
	private static final char nove = 0xffff & '9';
	private static final char ami = 0xffff & 'a'; 
	private static final char zetami = 0xffff & 'z';
	private static final char ama = 0xffff & 'A'; 
	private static final char zetama = 0xffff & 'Z';
	/********************************************************************************/
	/********************************************************************************/
	public static boolean isAlphaNumString(String str){
		char[] ca = str.toCharArray();
		char c;
	    for (int i=0;i<ca.length;i++){
	        c = ca[i];
            int ci = 0xffff & c;
            if( ci == sp ||
               (ci >= zero && ci <= nove) ||
               (ci >= ami && ci <= zetami) ||
               (ci >= ama && ci <= zetama)){
            	continue;
            }else{
            	return false;
            }
	    }
    	return true;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static ArrayList changedPropertiesOnModel(CommandDataModel sourceModel, 
													   CommandDataModel targetModel ) throws Exception {
	
		String logPrefix = "Tools.changedPropertiesOnModel() - ";
		LOG.debug(logPrefix+"(START) ");
		logPrefix = logPrefix+"(END) ";
	
		String sourceModelClassName = sourceModel.getClass().getName();
		String targetModelClassName = targetModel.getClass().getName();
	
		LOG.debug("Source model class (" + sourceModelClassName + ")" ); 
		LOG.debug("Target model class (" + targetModelClassName + ")" ); 
	
		if ( sourceModelClassName != targetModelClassName ) {
			LOG.debug(logPrefix+"Error different class for models. Returning null");
			return null;
		}
		
		ArrayList resultList = new ArrayList();
	    AbstractTypePropertyDescriptor prop = null;
	    String propName = null;
	    Class  propType = null;
	    Method propGetter = null;
		
	    try {
	    	
	        AbstractTypePropertyDescriptor[] props = ObjectsPropertiesCache.getInstance().getAbstractTypesPropertyDescriptors(sourceModel);
	        if ( props == null ) {
				LOG.debug(logPrefix+"No properties for model ["+sourceModel+"]");
	            return null;
			}
		
	        for ( int i = 0; i < props.length; i++ ) {
	
		        prop       = props[i];
		        propName   = prop.getName();
		        propType   = prop.getPropertyType();
	            propGetter = prop.getReadMethod();
	
		        if ( propType == null || propGetter == null ) {
			        continue;
		        }
		        
		        // If property is a CommandDataModel, skip
	            CommandDataModel innerDataModel = isCommandDataModelProperty( sourceModel, propGetter );
	            if ( innerDataModel != null ) {
		            LOG.debug(logPrefix+"Property is of CommandDataModel type, skip it");
	                continue;
	            }
	
	            // Is a ListType ?
	            ListType list = isListTypeProperty( sourceModel, propGetter );
	            if ( list != null ) {
	
		            LOG.debug(logPrefix+"Property is of ListType type, skip it");
				    Class modelType = list.getModelType();
				    if ( modelType == null ) {
			            LOG.debug(logPrefix+"ModelType of ListType is null. List in not managed");
				    	continue;
				    }
	            }
	            
	            // Types different from AbstractType must be skipped
	            Class superClass = propType.getSuperclass();
	            if ( superClass == null || !superClass.getName().equals(AbstractType.class.getName())) {
		            LOG.debug(logPrefix+"Property is different from AbstractType, skip it");
	            	continue;
	            }
	            
	            try {
												         
					AbstractType sourceValue = (AbstractType)propGetter.invoke(sourceModel,null);
					AbstractType targetValue = (AbstractType)propGetter.invoke(targetModel,null);
					if ( !sourceValue.toString().equals( targetValue.toString() ) ) {
						LOG.debug(logPrefix+"Property [" + propName + "] is changed from (" + sourceValue.toString() + ") to (" + targetValue.toString() + "). Add property to result list");
						resultList.add( propName );
					}
					
	            } catch (Exception e) {
	                String errorMsg = "Exception getting property ["+propName+"] "+"for data model ["+sourceModel+"]: "+e;
			        Exception ne = new Exception(errorMsg);
			        throw ne;
	            }
	        }
	        LOG.debug(logPrefix+"returning ["+resultList+"]");
	        return resultList;
	
	    } catch (Exception e) {
	        String errorMsg = "Exception in loading properties for data model [" + sourceModel + "]: " + e;
	        Exception ne = new Exception(errorMsg);
	        throw ne;
	    }
	}
	
	
	/********************************************************************************/
	/********************************************************************************/
	public static void copyObject(Object src, Object dst) throws Exception {
		copyObject(src,dst,null);
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static void copyObject(Object src, Object dst, String srcSuffix) throws Exception {
		
		String logPrefix = "Tools.copyObject() - ";
		LOG.debug(logPrefix+"(START) ");
		logPrefix = logPrefix+"(END) ";
		
		LOG.debug("Coping object "+src+" into object "+dst);
		
		try{

			if(src instanceof CommandDataModel && dst instanceof CommandDataModel) {
				Map srcCodDescFieldsMap = ((CommandDataModel)src).getCodDescFields();
				if(srcCodDescFieldsMap != null){
					for (Iterator iter = srcCodDescFieldsMap.keySet().iterator(); iter.hasNext();) {
						String key =  (String)iter.next();
						((CommandDataModel)dst).addCodDescField(key, (String)srcCodDescFieldsMap.get(key));
						((CommandDataModel)dst).addCodDescDataList((String)srcCodDescFieldsMap.get(key), ((CommandDataModel)src).getCodDescDataList(key));
					}
				}
			}
			
			AbstractTypePropertyDescriptor[] propsDst = ObjectsPropertiesCache.getInstance().getAbstractTypesPropertyDescriptors(dst);
			if(propsDst == null)
				return;
				
			String propDstName = null;
			Class  propDstClass = null;
			Class  propSrcClass = null;
	
			AbstractTypePropertyDescriptor propSrc = null;
			Method setter = null;
			Method getter = null;
			Object value  = null;
			Object[] param = new Object[1];
			
			for(int i=0;i<propsDst.length;i++){
				
				propDstName = propsDst[i].getName();
				propDstClass = propsDst[i].getPropertyType();
	
				String propSrcName = propDstName;
				if(srcSuffix != null)
					propSrcName = propSrcName + srcSuffix;
					
				propSrc = ObjectsPropertiesCache.getInstance().getAbstractTypePropertyDescriptor(src,propSrcName);
				if(propSrc == null)
					continue;
						
				propSrcClass = propSrc.getPropertyType();
				if(propSrcClass == null)
					continue;

				if(!propSrcClass.equals(propDstClass))
					continue;
	
				LOG.debug("Getting source property "+propSrcName);	
				getter = propSrc.getReadMethod();
				if(getter == null){
					LOG.debug("Getter method for "+propSrcName+" is null");
					continue;
				}
				value = getter.invoke(src,null);					
				LOG.debug("Source property value is: "+value);
				
				LOG.debug("Setting destination property "+propDstName);	
				setter = propsDst[i].getWriteMethod();
				if(setter == null){
					LOG.debug("Setter method for "+propDstName+" is null");
					continue;
				}
				param[0] = value;
				setter.invoke(dst, param);
				LOG.debug("Property "+propDstName+" copied");	
				
			}
			LOG.debug(logPrefix);
			return;
			
		}catch(Exception e){
			String errorMsg = thisClassName+".copyObject: Exception in coping object "+src+" into object "+dst+": "+e;
			Exception ne = new Exception(errorMsg);
			throw ne;
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static String fillDx(String str,char filler,int outLen) {
		if(str == null)
			return null;
			
		String result = str;
		int len = outLen - str.length();
		for(int i=0;i<len;i++)
			result += filler;
		return result;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static String fillSx(String str,char filler,int outLen) {
		if(str == null)
			return null;
			
		String result = "";
		int len = outLen - str.length();
		for(int i=0;i<len;i++)
			result += filler;
		return result + str;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static String unFillSx(String str,char filler) {
		if(str == null)
			return null;
			
		char[] tmp = str.toCharArray();
		int i=0;
		for(;i<tmp.length;i++){
			if(tmp[i] != filler)
				break;
		}
		return str.substring(i);
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static String unFillDx(String str,char filler) {
		if(str == null)
			return null;
			
		char[] tmp = str.toCharArray();
		int i=tmp.length-1;
		for(;i>=0;i--){
			if(tmp[i] != filler)
				break;
		}
		return str.substring(0,i+1);
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static String getClassName(Object obj) {
	
	    String className = obj.getClass().getName();
	
	    int lastPoint = className.lastIndexOf(".");
	    if (lastPoint != -1) {
	        className = className.substring(lastPoint + 1);
	    }
	    return className;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static DoubleType getColumnSum(String propertyName, ListType list) {
	
		String logPrefix = "Tools.getColumnSum() - ";
		LOG.debug(logPrefix+"(START) ");
		logPrefix = logPrefix+"(END) ";
		
	    DoubleType dValue = new DoubleType();
	
	    try {
	
	        Iterator it = list.getIterator();
	        while (it.hasNext()) {
	            CommandDataModel model = (CommandDataModel) it.next();
	            AbstractType value = (AbstractType) getPropertyValue(model, propertyName);
	
	            if (value instanceof IntegerType) {
	
	                dValue = dValue.add(new DoubleType(((IntegerType) value).intValue()));
	
	            } else
	                if (value instanceof DoubleType) {
	
	                    dValue = dValue.add((DoubleType) value);
	
	                } else {
	
	                    String errorMsg = propertyName + " is not of IntegerType or DoubleType";
	                    Exception ne = new Exception(errorMsg);
	                    LOG.error(ne);
	                }
	        }
	        LOG.debug(logPrefix+"returning ["+dValue+"]");
	        return dValue;
	
	    } catch (Exception e) {
	        String errorMsg = "Exception in getting the sum of " + propertyName + " property in list: " + e;
	        Exception ne = new Exception(errorMsg);
	        LOG.error(ne);
	        return new DoubleType(dValue);
	    }
	}
	
	/*******************************************************************************
	/*******************************************************************************/
	public static Object getPropertyValue(Object src, String fullPropertyName) throws Exception {
		
		String logPrefix = "Tools.getPropertyValue() - ";
		LOG.debug(logPrefix+"(START) ");
		logPrefix = logPrefix+"(END) ";

		Object result = null;
		String nestedIndicator = com.atosorigin.wfem.controller.Constants.NESTED_INDICATOR;
		
		try {
			
			int nestIdx = fullPropertyName.indexOf( nestedIndicator );
			
			if(nestIdx == -1) {
				result = getSimplePropertyValue(src, fullPropertyName);
				LOG.debug(logPrefix+"returning ["+result+"]");
				return result;
			}
	
			LOG.debug("Getting value of property ["+fullPropertyName+"] in object ["+src+"]");
		
			String propertyName = fullPropertyName.substring(0,nestIdx);
			fullPropertyName = fullPropertyName.substring(nestIdx+1);
			
			Class propertyClass = null;
			String index = getIndexInName(propertyName);
			int listIdx = -1;
			if(index != null){
				LOG.debug("Index in property name is ["+index+"]");
				listIdx = Integer.parseInt(index);
				String listPropertyName = propertyName.substring(0,propertyName.length()-index.length());
				propertyClass = getSimplePropertyType(src,listPropertyName);
				if(ListType.class.equals(propertyClass))
					propertyName = listPropertyName;
				else
					propertyClass = null;
			}			
			if(propertyClass == null)		
				propertyClass = getSimplePropertyType(src,propertyName);				
			if(propertyClass == null){
				LOG.debug(logPrefix+"property class is null");
				return null;
			}
						
			Object propertyObj = getSimplePropertyValue(src,propertyName);
			if(propertyObj == null){
				propertyObj = propertyClass.newInstance();
				setSimplePropertyValue(src,propertyName,propertyObj);
			}
			
			if(propertyObj instanceof ListType){
	
				LOG.debug("Property ["+propertyName+"] of object ["+src+"] is a ListType ["+propertyObj+"]");
				ListType list = (ListType)propertyObj;
	            CommandDataModel listElement = (CommandDataModel)list.get(listIdx);;
	            if(listElement == null){
					LOG.debug(logPrefix+"Element ad index ["+listIdx+"] of list ["+list+"] is null");
	            	return null;
	            }	            
				LOG.debug("Recall getPropertyValue on list element ["+listElement+"] and propertyName ["+fullPropertyName+"]");
				result = getPropertyValue(listElement,fullPropertyName);
				LOG.debug(logPrefix+"returning ["+result+"]");
				return result;	
	        }
			LOG.debug("Recall getPropertyValue on object ["+propertyObj+"] and propertyName ["+fullPropertyName+"]");
			result = getPropertyValue(propertyObj,fullPropertyName);
			LOG.debug(logPrefix+"returning ["+result+"]");
			return result;
			
		}catch(Exception e){
			String errorMsg = thisClassName+".getPropertyValue: Exception in getting property value for object ["+src+"] property ["+fullPropertyName+"]: "+e;
			Exception ne = new Exception(errorMsg);
			throw ne;
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private static Object getSimplePropertyValue(Object src, String propertyName) throws Exception {
		
		LOG.debug("  Getting simple property value ["+propertyName+"] of object ["+src+"]");
		
		try{

			String index = getIndexInName(propertyName);
			if(index != null){
				LOG.debug("Index in property name is ["+index+"]");
				int listIdx = Integer.parseInt(index);
				String listPropertyName = propertyName.substring(0,propertyName.length()-index.length());
				Object listObj = getSimplePropertyValue(src,listPropertyName);
				if(listObj != null && listObj instanceof ListType){
					ListType list = (ListType)listObj;
		            CommandDataModel listElement = (CommandDataModel)list.get(listIdx);
		            return listElement;
				}
				LOG.debug("Property has a number in name but is not a ListType");
			}			
			
			AbstractTypePropertyDescriptor prop = ObjectsPropertiesCache.getInstance().getAbstractTypePropertyDescriptor(src,propertyName);
			if(prop == null){
				LOG.debug("   Simple property ["+propertyName+"] does not exist");
				return null;
			}
			
			Method getter = prop.getReadMethod();
			if(getter == null){
				LOG.debug("    Simple property getter method for ["+prop.getName()+"] is null");
				return null;
			}
			
			Object value = getter.invoke(src,null);					
			LOG.debug("  Simple property ["+propertyName+"] of object ["+src+"] value is: ["+value+"]");
			return value;
			
		}catch(Exception e){
			String errorMsg = thisClassName+".getSimplePropertyValue: Exception in getting simple property value for object ["+src+"] property ["+propertyName+"]: "+e;
			Exception ne = new Exception(errorMsg);
			throw ne;
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static Object initObject(Object o) throws Exception {
	
		String logPrefix = "Tools.initObject() - ";
		LOG.debug(logPrefix+"(START) ");
		logPrefix = logPrefix+"(END) ";
		
	    LOG.debug("Initializing object " + o);
	
	    try {
	
	        Iterator props = Arrays.asList(ObjectsPropertiesCache.getInstance().getAbstractTypesPropertyDescriptors(o)).iterator();
	        if(props == null)
	        	return o;
	
	        AbstractTypePropertyDescriptor prop;
	        Class propClass;
	        String propName;
	        Method getter;
	        Method setter;
	        Object[] param = new Object[1];
	
	        while (props.hasNext()) {
	
	            prop = (AbstractTypePropertyDescriptor) props.next();
	
	            propName = prop.getName();
	            LOG.debug("Property " + propName + ": initializing...");
	            propClass = prop.getPropertyType();
	
	            getter = prop.getReadMethod();
	
	            if (getter == null)
	                continue;
	
	            String getterName = getter.getName();
	            if(!getterName.startsWith("get"))
	            	continue;
	            	
	            Object value = getter.invoke(o, new Object[0]);
	            
	            try {
	                if (value == null && (setter = prop.getWriteMethod()) != null) {
	                    value = propClass.newInstance();
	                    param[0] = value;
	                    setter.invoke(o, param);
	                    LOG.debug("Property " + propName + " initialized!");
	                }
	
	            }catch(InstantiationException iex) {
	            	LOG.debug("Property " + propName + " not initializable!");
	            }
	        }	
	        LOG.debug(logPrefix);
	        return o;
	
	    } catch (Exception e) {
	        String errorMsg = thisClassName+".initObject: Exception in initializing object "+o+ ": "+e;
	        Exception ne = new Exception(errorMsg);
	        throw ne;
	    }
	
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static CommandDataModel isCommandDataModelProperty(CommandDataModel model, Method propGetter) throws Exception {
	
	    Object value = propGetter.invoke(model, new Object[0]);
	    return (value != null && value instanceof CommandDataModel) ? (CommandDataModel) value : null;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static ListCommandDataModel isListCommandDataModelProperty(CommandDataModel model, Method propGetter) throws Exception {
	
	    Object value = propGetter.invoke(model, new Object[0]);
	    return (value != null && value instanceof ListCommandDataModel) ? (ListCommandDataModel) value : null;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static ListType isListTypeProperty(CommandDataModel model, Method propGetter) throws Exception {
	
	    Object value = propGetter.invoke(model, new Object[0]);
	    return (value != null && value instanceof ListType) ? (ListType) value : null;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static boolean isModelChanged( CommandDataModel model, boolean checkNested ) throws Exception {

		String logPrefix = "Tools.isModelChanged(checkNested=["+checkNested+"]) - ";
		LOG.debug(logPrefix+"(START)");
		logPrefix = logPrefix+"(END) ";
		
		// If model is null, return false
		if( model == null ) {
	        LOG.debug(logPrefix+"input model==null. return [false]");
			return false;
		}
		
		if(checkNested){
			if(model.isChanged())
				return true;
			ArrayList models = getInnerCommandDataModelList(model);
			for(int i=0;i<models.size();i++){
				if(((CommandDataModel)models.get(i)).isChanged()){
					LOG.debug(logPrefix+"returning [true]");
					return true;
				}
			}
			LOG.debug(logPrefix+"returning [false]");
			return false;
		}else{
			LOG.debug(logPrefix+"returning ["+model.isChanged()+"]");
			return model.isChanged();
		}	
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static boolean checkCodDescFieldValidity(CommandDataModel model, String propertyName) {
		
		String logPrefix = "Tools.checkCodDescFieldValidity - ";
		LOG.debug(logPrefix+"(START) on property ["+propertyName+"]");
		logPrefix = logPrefix+"(END) ";
		
		int idx = propertyName.lastIndexOf("_");
		if(idx >= 0){
			String modelPropName = propertyName.substring(0,idx);
			propertyName = propertyName.substring(idx+1);
			try{
				model = (CommandDataModel)Tools.getPropertyValue(model,modelPropName);
			}catch(Exception e){
				String errmsg = logPrefix+": Exception ["+e+"] in checking cod/description field ["+propertyName+"] in model ["+model+"]";
				e = new Exception(errmsg);
				LOG.error(e);
				return false;
			}
		}
		
		// Field is not registered as CodDescField
	    String codDescReferenceName = model.getCodDescReference(propertyName);
	    if(codDescReferenceName == null){
	    	LOG.debug(logPrefix+"Reference name is null. Returning [true]");
	    	return true;
	    }
	
		CodDescDataList dataList = model.getCodDescDataList(propertyName);
		if(dataList == null){
	    	LOG.debug(logPrefix+"DataList is null. Returning [true]");
			return true;
		}
	
		try{
				
		    AbstractType propertyValue = (AbstractType)Tools.getPropertyValue(model, propertyName);
		    if(propertyValue == null){
		    	LOG.debug(logPrefix+"Property value is null. Returning [true]");
		    	return true;
		    }
		    
		    if(!propertyValue.isVisible()){
		    	LOG.debug(logPrefix+"Property value is not displayable. Returning [true]");
		    	return true;
		    }
		    	
		    CodDescData data = dataList.getCodDesc(propertyValue.toString());
		    if(data != null && !data.isValid()){
		    	LOG.debug(logPrefix+"Property ["+propertyName+"] is not valid. Returning [false]");
		    	propertyValue.addTypeError(new TypeError("CodDescValidityException"));
		        propertyValue.setValid(false);
				model.setValid(false);
	        	return false;
		    }
	    	LOG.debug(logPrefix+"Property ["+propertyName+"] is valid. Returning [true]");
			return true;
		    
		}catch(Exception e){
			String errmsg = logPrefix+": Exception ["+e+"] in checking cod/description field ["+propertyName+"] in model ["+model+"]";
			e = new Exception(errmsg);
			LOG.error(e);
			return false;
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static boolean checkCodDescFieldsValidity(CommandDataModel model) {

		String logPrefix = "Tools.checkCodDescFieldsValidity - ";
		LOG.debug(logPrefix+"(START)");
		logPrefix = logPrefix+"(END) ";
		
		// If model is null, return false
		if( model == null ) {
	        LOG.debug(logPrefix+"input model==null. return [false]");
			return false;
		}
		
		boolean res = true;
		
		Map codDescFields = model.getCodDescFields();
		Iterator keys = codDescFields.keySet().iterator();
		while(keys.hasNext()){
			String fieldName = (String)keys.next();
			if(!checkCodDescFieldValidity(model,fieldName))
				res = false;
		}
		
		try{
			ArrayList models = getInnerCommandDataModelList(model);
			for(int i=0;i<models.size();i++){
				CommandDataModel curModel = (CommandDataModel)models.get(i);
				codDescFields = curModel.getCodDescFields();
				keys = codDescFields.keySet().iterator();
				while(keys.hasNext()){
					String fieldName = (String)keys.next();
					if(!checkCodDescFieldValidity(curModel,fieldName))
						res = false;
				}
			}
			LOG.debug(logPrefix+"returning [true]");
			return res;
		}catch(Exception e){
	        String errorMsg = thisClassName+".checkCodDescFieldsValidity: Exception in checking model "+model+ ": "+e;
	        Exception ne = new Exception(errorMsg);
	        LOG.error(ne);
	        return false;
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static boolean checkModelValidity(CommandDataModel model) {

		String logPrefix = "Tools.checkModelValidity - ";
		LOG.debug(logPrefix+"(START)");
		logPrefix = logPrefix+"(END) ";
		
		// If model is null, return false
		if( model == null ) {
	        LOG.debug(logPrefix+"input model==null. return [false]");
			return false;
		}
		
		ArrayList fields = getAbstractTypeFields(model);
		for(int i=0;i<fields.size();i++){
			AbstractType field = (AbstractType)fields.get(i);
			if(!field.isValid()){
				LOG.debug(logPrefix+"Field at index ["+i+"] on model ["+model+"] is not valid. Returning [false]");
				return false;
			}
		}
			
		try{
			ArrayList models = getInnerCommandDataModelList(model);
			for(int i=0;i<models.size();i++){
				CommandDataModel curModel = (CommandDataModel)models.get(i);
				fields = getAbstractTypeFields(curModel);
				for(int j=0;j<fields.size();j++){
					AbstractType field = (AbstractType)fields.get(j);
					if(!field.isValid()){
						LOG.debug(logPrefix+"Field at index ["+j+"] on model ["+curModel+"] is not valid. Returning [false]");
						return false;
					}
				}
			}
			LOG.debug(logPrefix+"returning [true]");
			return true;
		}catch(Exception e){
	        String errorMsg = thisClassName+".checkModelValidity: Exception in checking model "+model+ ": "+e;
	        Exception ne = new Exception(errorMsg);
	        LOG.error(ne);
	        return false;
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static TimestampType now() {
		Timestamp now = new Timestamp(Calendar.getInstance().getTime().getTime());
		return new TimestampType(now);
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static  String replaceParam( String sourceString, String oldToken, String newToken ) {
		
		String result = new String(sourceString);
	
		int index = sourceString.indexOf( oldToken );
		if ( index != -1 ) {
			result  = sourceString.substring( 0, index );
			result += newToken;
			result += sourceString.substring( index + oldToken.length() );
		}
		return result;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static String replaceParamAll( String sourceString, String oldToken, String newToken ) {
	
		String result = new String(sourceString);
		while ( true ) {
			String prevResult = result;
			result = replaceParam( prevResult, oldToken, newToken );
			if ( result.equals( prevResult ) ) {
				break;
			}
		}
		return result;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static void setPropertyValue(Object src, String fullPropertyName, 
									      Object value) throws Exception {
									      	
		String logPrefix = "Tools.setPropertyValue() - ";
		LOG.debug(logPrefix+"(START) ");
		logPrefix = logPrefix+"(END) ";

		String nestedIndicator = com.atosorigin.wfem.controller.Constants.NESTED_INDICATOR;
		
		try {
			
			int nestIdx = fullPropertyName.indexOf( nestedIndicator );
			
			if(nestIdx == -1) {
				setSimplePropertyValue(src, fullPropertyName, value);
				LOG.debug(logPrefix);
				return;
			}
	
			LOG.debug("Setting property ["+fullPropertyName+"] of object ["+src+"] to value ["+value+"]");
		
			String propertyName = fullPropertyName.substring(0,nestIdx);
			fullPropertyName = fullPropertyName.substring(nestIdx+1);
			
			Class propertyClass = null;
			String index = getIndexInName(propertyName);
			int listIdx = -1;
			if(index != null){
				LOG.debug("Index in property name is ["+index+"]");
				listIdx = Integer.parseInt(index);
				String listPropertyName = propertyName.substring(0,propertyName.length()-index.length());
				propertyClass = getSimplePropertyType(src,listPropertyName);
				if(ListType.class.equals(propertyClass))
					propertyName = listPropertyName;
				else
					propertyClass = null;
			}			
			if(propertyClass == null)		
				propertyClass = getSimplePropertyType(src,propertyName);				
			if(propertyClass == null){
				LOG.debug(logPrefix+"property class is null");
				return;
			}
												
			Object propertyObj = getSimplePropertyValue( src, propertyName);
			if(propertyObj == null){
				propertyObj = propertyClass.newInstance();
				setSimplePropertyValue(src,propertyName,propertyObj);
			}
			
	        if(propertyObj instanceof ListType) {
	
				LOG.debug("Property ["+propertyName+"] of object ["+src+"] is a ListType ["+propertyObj+"]");
				ListType list = (ListType)propertyObj;
	            Class modelType = list.getModelType();
	            if (modelType == null) {
	                LOG.debug(logPrefix+"ModelType of ListType ["+propertyObj+"] is null. List in not managed");
	                return;
	            }
	            
	            CommandDataModel listElement = null;
	            int listSize = list.size();
	            if(listIdx >= listSize){
	            	for(int i=listSize;i<=listIdx;i++){
		                listElement = (CommandDataModel)modelType.newInstance();
			            list.add(listElement);
	            	}
	            }else{
		            listElement = (CommandDataModel)list.get(listIdx);
	            }
	
				LOG.debug("Recall setPropertyValue on list element ["+listElement+"], propertyName ["+fullPropertyName+"] and value ["+value+"]");
				setPropertyValue(listElement,fullPropertyName,value);
				
	            list.set(listElement, listIdx);
				LOG.debug(logPrefix);
	            return;
	        }
	        
			LOG.debug("Recall setPropertyValue on object ["+propertyObj+"], propertyName ["+fullPropertyName+"] and value ["+value+"]");
			setPropertyValue(propertyObj,fullPropertyName,value);
			LOG.debug(logPrefix);
			return;
			
		}catch(Exception e){
			String errorMsg = thisClassName+".setPropertyValue: Exception in setting property value for object ["+src+"] property ["+fullPropertyName+"]: "+e;
			Exception ne = new Exception(errorMsg);
			throw ne;
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private static void setSimplePropertyValue(Object src, String propertyName, 
											     Object value) throws Exception {
		
		LOG.debug("  Setting simple property ["+propertyName+"] of object ["+src+"] to value ["+value+"]");
		try{
			
			AbstractTypePropertyDescriptor prop = ObjectsPropertiesCache.getInstance().getAbstractTypePropertyDescriptor(src,propertyName);
			if(prop == null){
				if(src instanceof MapCommandDataModel){
					((MapCommandDataModel)src).addProperty(propertyName,new StringType());
					prop = ObjectsPropertiesCache.getInstance().getAbstractTypePropertyDescriptor(src,propertyName);
					if(prop == null){
						LOG.debug("    Simple property ["+propertyName+"] does not exist");
						return;
					}
				}else{
					LOG.debug("    Simple property ["+propertyName+"] does not exist");
					return;
				}
			}
			
			Method setter = prop.getWriteMethod();
			if(setter == null){
				LOG.debug("    Simple property setter method for ["+prop.getName()+"] is null");
				return;
			}

			Object[] param = new Object[1];
			param[0] = value;
			setter.invoke(src,param);
			
			LOG.debug("  Simple property ["+propertyName+"] of object ["+src+"] setted to value ["+value+"]");
			return;
			
		}catch(Exception e){
			String errorMsg = thisClassName+".setSimplePropertyValue: Exception in setting simple property value for object ["+src+"] property ["+propertyName+"]: "+e;
			Exception ne = new Exception(errorMsg);
			throw ne;
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static String getIndexInName(String name){
		
		char[] cname = name.toCharArray();
		int firstNum = cname.length;
		for(int i=cname.length-1;i>=0;i--){
			if(cname[i] < '0' || cname[i] > '9')
				break;
			firstNum--;
		}
		
		if(firstNum < 0 || firstNum == cname.length){
			return null;
		}else{
			String res = name.substring(firstNum);
			try{ 
				Integer.parseInt(res); 
			}catch(NumberFormatException nfe){ 
				return null;
			}
			return res;
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static DateType today() {
		
		Calendar cNow = Calendar.getInstance();
		Date dNow = new Date(cNow.getTime().getTime());
		DateType now = new DateType(dNow);
		return now;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static void copyCommandDataModel(CommandDataModel src, CommandDataModel dst) throws Exception {
		
		String logPrefix = "Tools.copyCommandDataModel() - ";
		LOG.debug(logPrefix+"(START) ");
		logPrefix = logPrefix+"(END) ";

		LOG.debug("Coping CommandDataModel "+src+" into CommandDataModel "+dst);
		
		try{

			Map srcCodDescFieldsMap = src.getCodDescFields();
			if(srcCodDescFieldsMap != null){
				for (Iterator iter = srcCodDescFieldsMap.keySet().iterator(); iter.hasNext();) {
					String key =  (String)iter.next();
					dst.addCodDescField(key, (String)srcCodDescFieldsMap.get(key));
					dst.addCodDescDataList((String)srcCodDescFieldsMap.get(key), src.getCodDescDataList(key));
				}
			}
			
			AbstractTypePropertyDescriptor[] propsDst = ObjectsPropertiesCache.getInstance().getAbstractTypesPropertyDescriptors(dst);
			if(propsDst == null)
				return;
			for(int i=0;i<propsDst.length;i++){
				
				String propName = propsDst[i].getName();
	
				Object propSrcValue = getPropertyValue(src,propName);
				if(propSrcValue == null){
					LOG.debug("Source property ["+propName+"] is null. Skip it");
					continue;
				}
	
				Class propDstType = getPropertyType(dst,propName);
				Class propSrcType = getPropertyType(src,propName);
				if(propDstType == null || propSrcType == null){
					LOG.debug("Source or destination property ["+propName+"] type is null. Skip it");
					continue;
				}
				
				Object propDstValue = null;
	            Class propDstSuperType = propDstType.getSuperclass();
		        if(propDstSuperType == null || !propDstSuperType.getName().equals(AbstractType.class.getName())){
					LOG.debug("Destination property ["+propName+"] is not an AbstractType. Try to create new instance");
					try{
						propDstValue = propDstType.newInstance();
					}catch(Exception ie){
						LOG.debug("Destination property ["+propName+"] is not instantiable. Skip it");
						continue;
					}
		        }else{
					LOG.debug("Destination property ["+propName+"] is an AbstractType. Try to create new instance");
					try{
						propDstValue = AbstractType.newInstance(propDstType, propSrcValue.toString());
					}catch(Exception e){
						LOG.debug("Destination property ["+propName+"] is not instantiable. Skip it");
						continue;
					}
		        }
				
				if(propSrcValue instanceof CommandDataModel &&
				   propDstValue instanceof CommandDataModel){
					LOG.debug("Properties "+propName+" are ComandDataModel");
					copyCommandDataModel((CommandDataModel)propSrcValue,(CommandDataModel)propDstValue);
					setPropertyValue(dst,propName,propDstValue);
					continue;
				}
				
				if(propSrcValue instanceof ListType &&
				   propDstValue instanceof ListType){
	
					LOG.debug("Properties "+propName+" are ListType");
					ListType srcList = (ListType)propSrcValue;
					ListType dstList = (ListType)getPropertyValue(dst,propName);
					if(dstList == null){
						LOG.debug("Destination ListType property "+propName+" is null. Create new instance");
						dstList = new ListType(srcList.getModelType());
					}
					
	                if(srcList.getModelType() != null && 
	                   srcList.getModelType().equals(dstList.getModelType())){
						LOG.debug("Copy Source and Destination ListType property "+propName);
	                
						dstList.clear();

						Iterator it = srcList.getIterator();
						while(it.hasNext()){
							CommandDataModel srcModel = (CommandDataModel)it.next();
			                CommandDataModel dstModel = (CommandDataModel)srcModel.getClass().newInstance();
							copyCommandDataModel(srcModel,dstModel);
							dstList.add(dstModel);
						}
						dstList.setRowsPerPage(srcList.getRowsPerPage());
						dstList.setMaxRowsExceeded(srcList.isMaxRowsExceeded());
						dstList.setCurrentPage(srcList.getCurrentPage());
						
						setPropertyValue(dst,propName,dstList);
	                }else{
						LOG.debug("Source and Destination ListType property "+propName+" has different model type");
                    }
					continue;
				}
	
	            Class propSrcSuperType = propSrcType.getSuperclass();
		        if(propSrcSuperType == null || !propSrcSuperType.getName().equals(AbstractType.class.getName())){
					LOG.debug("Source property "+propName+" is not an AbstractType. Skip it");
	                continue;
		        }
	
		        if(!propDstValue.getClass().equals(propSrcValue.getClass())){ 
					LOG.debug("Source and destination properties "+propName+" are of different types. Skip it");
					continue;
		        }
	
				LOG.debug("Setting destination property "+propName+" with source value "+propSrcValue);
				setPropertyValue(dst,propName,propDstValue);
			}
			LOG.debug(logPrefix);
			
		}catch(Exception e){
			String errorMsg = thisClassName+".copyCommandDataModel: Exception in coping CommandDataModel "+src+" into "+dst+": "+e;
			Exception ne = new Exception(errorMsg);
			throw ne;
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static ArrayList getAbstractTypeFields(CommandDataModel model){
	
		String logPrefix = "Tools.getAbstractTypeFields() - ";
		LOG.debug(logPrefix+"(START) ");
		logPrefix = logPrefix+"(END) ";

		ArrayList result = new ArrayList();
		String propName = null;
		
		try{
			
			AbstractTypePropertyDescriptor[] props = ObjectsPropertiesCache.getInstance().getAbstractTypesPropertyDescriptors(model);
			if(props == null)
				return result;
				
			for(int i=0;i<props.length;i++){
				
				propName = props[i].getName();
				Class  propType = getPropertyType(model,propName);
				if(propType == null)
					continue;
	            Class  propSuperType = propType.getSuperclass();
		        if(propSuperType != null && propSuperType.getName().equals(AbstractType.class.getName())){
		        	AbstractType value = (AbstractType)getPropertyValue(model,propName);
		        	if(value != null)
						result.add(value);
		        }
			}
			LOG.debug(logPrefix+"returning ["+result+"]");
			
		}catch(Exception e){
			String errorMsg = thisClassName+".getAbstractTypeFields: Exception getting field ["+propName+"] on model ["+model.getClass()+"]: "+e;
			Exception ne = new Exception(errorMsg);
			LOG.error(ne);
		}
		return result;
		
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static ArrayList getAbstractTypeFieldNames(CommandDataModel model){
	
		String logPrefix = "Tools.getAbstractTypeFieldNames() - ";
		LOG.debug(logPrefix+"(START) ");
		logPrefix = logPrefix+"(END) ";

		ArrayList result = new ArrayList();
		String propName = null;
		
		try{
			
			AbstractTypePropertyDescriptor[] props = ObjectsPropertiesCache.getInstance().getAbstractTypesPropertyDescriptors(model);
			if(props == null)
				return result;
				
			for(int i=0;i<props.length;i++){
				
				propName = props[i].getName();
				Class  propType = getPropertyType(model,propName);
				if(propType == null)
					continue;
	            Class  propSuperType = propType.getSuperclass();
		        if(propSuperType != null && propSuperType.getName().equals(AbstractType.class.getName())){
					result.add(propName);
		        }
			}
			LOG.debug(logPrefix+"returning ["+result+"]");
			
		}catch(Exception e){
			String errorMsg = thisClassName+".getAbstractTypeFieldNames: Exception getting a field name on model ["+model.getClass()+"]: "+e;
			Exception ne = new Exception(errorMsg);
			LOG.error(ne);
		}
		return result;
		
	}

	
	/********************************************************************************/
	/********************************************************************************/
	public static void resetChangedAndValid(CommandDataModel model) throws Exception {
		
		String logPrefix = "Tools.resetChangedAndValid() - ";
		LOG.debug(logPrefix+"(START) ");
		logPrefix = logPrefix+"(END) ";

		model.setChanged(false);		
		ArrayList fields = getAbstractTypeFields(model);
		for(int i=0;i<fields.size();i++){
			AbstractType field = (AbstractType)fields.get(i);
			field.setChanged(false);
			field.setValid(true);
		}
		
		ArrayList models = getInnerCommandDataModelList(model);
		for(int i=0;i<models.size();i++){
			CommandDataModel inModel = (CommandDataModel)models.get(i);
			inModel.setChanged(false);
			fields = getAbstractTypeFields(inModel);
			for(int j=0;j<fields.size();j++){
				AbstractType field = (AbstractType)fields.get(j);
				field.setChanged(false);
				field.setValid(true);
			}
		}
		
		LOG.debug(logPrefix);			
	}
		
	/********************************************************************************/
	/********************************************************************************/
	public static void resetChanged(CommandDataModel model) throws Exception {
		
		String logPrefix = "Tools.resetChanged() - ";
		LOG.debug(logPrefix+"(START) ");
		logPrefix = logPrefix+"(END) ";
		
		model.setChanged(false);		
		ArrayList fields = getAbstractTypeFields(model);
		for(int i=0;i<fields.size();i++){
			AbstractType field = (AbstractType)fields.get(i);
			field.setChanged(false);
		}
		
		ArrayList models = getInnerCommandDataModelList(model);
		for(int i=0;i<models.size();i++){
			CommandDataModel inModel = (CommandDataModel)models.get(i);
			inModel.setChanged(false);
			fields = getAbstractTypeFields(inModel);
			for(int j=0;j<fields.size();j++){
				AbstractType field = (AbstractType)fields.get(j);
				field.setChanged(false);
			}
		}
		
		LOG.debug(logPrefix);			
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static void resetSkippable(CommandDataModel model) throws Exception {
		
		String logPrefix = "Tools.resetSkippable() - ";
		LOG.debug(logPrefix+"(START) ");
		logPrefix = logPrefix+"(END) ";
		
		ArrayList fields = getAbstractTypeFields(model);
		for(int i=0;i<fields.size();i++){
			AbstractType field = (AbstractType)fields.get(i);
			field.setSkippable(false);
		}
		
		ArrayList models = getInnerCommandDataModelList(model);
		for(int i=0;i<models.size();i++){
			CommandDataModel inModel = (CommandDataModel)models.get(i);
			fields = getAbstractTypeFields(inModel);
			for(int j=0;j<fields.size();j++){
				AbstractType field = (AbstractType)fields.get(j);
				field.setSkippable(false);
			}
		}
		
		LOG.debug(logPrefix);			
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static void setChanged(CommandDataModel model) throws Exception {
		if(recurseSetChanged(model))
			model.setChanged(true);
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private static boolean recurseSetChanged(CommandDataModel model) throws Exception {

		boolean res = false;		
		AbstractTypePropertyDescriptor[] props = ObjectsPropertiesCache.getInstance().getAbstractTypesPropertyDescriptors(model);
		if(props == null)
			return false;
			
		for(int i=0;i<props.length;i++){
			
			String propName = props[i].getName();
			Object o = Tools.getPropertyValue(model,propName);
			if(o == null)
				continue;
			if(o instanceof CommandDataModel){
				if(recurseSetChanged((CommandDataModel)o)){
					model.setChanged(true);
					res = true;
				}
			}else if(o instanceof ListType){
				ListType list = (ListType)o;
				for(int j=0;j<list.size();j++){
					CommandDataModel listElement = list.get(j);
					if(recurseSetChanged(listElement)){
						model.setChanged(true);
						res = true;
					}
				}
			}else if(o instanceof AbstractType){
				if(((AbstractType)o).isChanged()){
					model.setChanged(true);
					res = true;
				}
			}
		}
		return res;
	}
		
	/********************************************************************************/
	/********************************************************************************/
	public static void resetValid(CommandDataModel model) throws Exception {
		
		String logPrefix = "Tools.resetValid() - ";
		LOG.debug(logPrefix+"(START) ");
		logPrefix = logPrefix+"(END) ";
		
		ArrayList fields = getAbstractTypeFields(model);
		for(int i=0;i<fields.size();i++){
			AbstractType field = (AbstractType)fields.get(i);
			field.setValid(true);
		}
		
		ArrayList models = getInnerCommandDataModelList(model);
		for(int i=0;i<models.size();i++){
			CommandDataModel inModel = (CommandDataModel)models.get(i);
			fields = getAbstractTypeFields(inModel);
			for(int j=0;j<fields.size();j++){
				AbstractType field = (AbstractType)fields.get(j);
				field.setValid(true);
			}
		}
		
		LOG.debug(logPrefix);		
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static ArrayList getInnerCommandDataModelList(CommandDataModel model) throws Exception {
	
		String logPrefix = "Tools.getInnerCommandDataModelList() - ";
		LOG.debug(logPrefix+"(START) ");
		logPrefix = logPrefix+"(END) ";
		
		ArrayList models = new ArrayList();
		loadInnerCommandDataModelList(model,models);
		LOG.debug(logPrefix+"returning ["+models+"]");
		return models;
		
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private static void loadInnerCommandDataModelList(CommandDataModel model, ArrayList resultList) throws Exception {
	
		try {
	
			AbstractTypePropertyDescriptor[] props = ObjectsPropertiesCache.getInstance().getAbstractTypesPropertyDescriptors(model);
			if(props == null)
				return;
			for(int i=0;i<props.length;i++){
				
				String propName = props[i].getName();
				Object o = Tools.getPropertyValue(model,propName);
				if(o == null)
					continue;
				if(o instanceof CommandDataModel){
					loadInnerCommandDataModelList((CommandDataModel)o,resultList);
					resultList.add(o);
				}else if(o instanceof ListType){
					ListType list = (ListType)o;
					for(int j=0;j<list.size();j++){
						CommandDataModel listElement = list.get(j);
						loadInnerCommandDataModelList(listElement,resultList);
						resultList.add(listElement);
					}
				}
			}
			return;
			
		}catch(Exception e){
			String errorMsg = thisClassName+".getInnerCommandDataModelList: Exception in getting inner CommandDataModel for model ["+model.getClass()+"]: "+e;
			Exception ne = new Exception(errorMsg);
			throw ne;
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static Class getPropertyType(Object src, String fullPropertyName) throws Exception {
	
		String logPrefix = "Tools.getPropertyType() - ";
		LOG.debug(logPrefix+"(START) ");
		logPrefix = logPrefix+"(END) ";

		Class result = null;
		String nestedIndicator = com.atosorigin.wfem.controller.Constants.NESTED_INDICATOR;
		
		try {
			
			int nestIdx = fullPropertyName.indexOf( nestedIndicator );
			
			if(nestIdx == -1) {
				result = getSimplePropertyType(src, fullPropertyName);
				LOG.debug(logPrefix+"returning ["+result+"]");
				return result;
			}
			
			LOG.debug("Getting type of property ["+fullPropertyName+"] for class ["+src+"]");
	
			String propertyName = fullPropertyName.substring(0,nestIdx);
			fullPropertyName = fullPropertyName.substring(nestIdx+1);
			
			Class propertyClass = null;
			String index = getIndexInName(propertyName);
			int listIdx = -1;
			if(index != null){
				LOG.debug("Index in property name is ["+index+"]");
				listIdx = Integer.parseInt(index);
				String listPropertyName = propertyName.substring(0,propertyName.length()-index.length());
				propertyClass = getSimplePropertyType(src,listPropertyName);
				if(ListType.class.equals(propertyClass))
					propertyName = listPropertyName;
				else
					propertyClass = null;
			}			
			if(propertyClass == null)		
				propertyClass = getSimplePropertyType(src,propertyName);				
			if(propertyClass == null){
				LOG.debug(logPrefix+"property class is null");
				return null;
			}
						
			Object propertyObj = getSimplePropertyValue(src,propertyName);
			if(propertyObj == null){
				Class parentClass = getSimplePropertyType(src,propertyName);
				propertyObj = parentClass.newInstance();
				setSimplePropertyValue(src,propertyName,propertyObj);
			}
							
			if(propertyObj instanceof ListType){
				
				LOG.debug("Property ["+propertyName+"] of object ["+src+"] is a ListType");				
				ListType list = (ListType)propertyObj;
	            Class modelType = list.getModelType();
	            if (modelType == null) {
	                LOG.debug(logPrefix+"ModelType of ListType ["+propertyObj+"] is null. List in not managed");
	                return null;
	            }
	            
	            CommandDataModel listElement = null;
	            if(list.size() > listIdx)
	            	listElement = (CommandDataModel)list.get(listIdx);
	            if(listElement == null)
	            	listElement = (CommandDataModel)modelType.newInstance();

				LOG.debug("Recall getPropertyType on list element class ["+listElement.getClass()+"] and propertyName ["+fullPropertyName+"]");
				result = getPropertyType(listElement,fullPropertyName);
				LOG.debug(logPrefix+"returning ["+result+"]");
				return result;				
				
			}
			LOG.debug("Recall getPropertyType on object ["+propertyObj+"] and propertyName ["+fullPropertyName+"]");
			result = getPropertyType(propertyObj,fullPropertyName);
			LOG.debug(logPrefix+"returning ["+result+"]");
			return result;
			
		}catch(Exception e){
			String errorMsg = thisClassName+".getPropertyType: Exception in getting property type for object ["+src+"] property ["+fullPropertyName+"]: "+e;
			Exception ne = new Exception(errorMsg);
			throw ne;
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private static Class getSimplePropertyType(Object src, String propertyName) throws Exception {
		
		LOG.debug("  Getting simple property type for ["+src+"] property ["+propertyName+"]");
		
		try{
			
			String index = getIndexInName(propertyName);
			if(index != null){
				LOG.debug("Index in property name is ["+index+"]");
				String listPropertyName = propertyName.substring(0,propertyName.length()-index.length());
				Object listObj = getSimplePropertyValue(src,listPropertyName);
				if(listObj != null && listObj instanceof ListType){
					ListType list = (ListType)listObj;
		            return list.getModelType();
				}
				LOG.debug("Property has a number in name but is not a ListType");
			}			
			
			AbstractTypePropertyDescriptor prop = ObjectsPropertiesCache.getInstance().getAbstractTypePropertyDescriptor(src,propertyName);
			if(prop == null){
				LOG.debug("    Simple property ["+propertyName+"] does not exist");
				return null;
			}
			return prop.getPropertyType();
			
		}catch(Exception e){
			String errorMsg = thisClassName+".getSimplePropertyType: Exception in getting simple property type for object ["+src+"] property ["+propertyName+"]: "+e;
			Exception ne = new Exception(errorMsg);
			throw ne;
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static void resetTypesErrors(CommandDataModel model) throws Exception {
		
		String logPrefix = "Tools.resetTypesErrors() - ";
		LOG.debug(logPrefix+"(START) ");
		logPrefix = logPrefix+"(END) ";
		
		ArrayList fields = getAbstractTypeFields(model);
		for(int i=0;i<fields.size();i++){
			AbstractType field = (AbstractType)fields.get(i);
			field.resetTypeErrors();
		}
		
		ArrayList models = getInnerCommandDataModelList(model);
		for(int i=0;i<models.size();i++){
			CommandDataModel inModel = (CommandDataModel)models.get(i);
			fields = getAbstractTypeFields(inModel);
			for(int j=0;j<fields.size();j++){
				AbstractType field = (AbstractType)fields.get(j);
				field.resetTypeErrors();
			}
		}
		
		LOG.debug(logPrefix);			
	}

	/********************************************************************************/
	/********************************************************************************/
	public static void resetTypesWarnings(CommandDataModel model) throws Exception {
		
		String logPrefix = "Tools.resetTypesWarnings() - ";
		LOG.debug(logPrefix+"(START) ");
		logPrefix = logPrefix+"(END) ";
		
		ArrayList fields = getAbstractTypeFields(model);
		for(int i=0;i<fields.size();i++){
			AbstractType field = (AbstractType)fields.get(i);
			field.resetTypeWarnings();
		}
		
		ArrayList models = getInnerCommandDataModelList(model);
		for(int i=0;i<models.size();i++){
			CommandDataModel inModel = (CommandDataModel)models.get(i);
			fields = getAbstractTypeFields(inModel);
			for(int j=0;j<fields.size();j++){
				AbstractType field = (AbstractType)fields.get(j);
				field.resetTypeWarnings();
			}
		}
		
		LOG.debug(logPrefix);			
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static void resetTypesMessages(CommandDataModel model) throws Exception {
		
		String logPrefix = "Tools.resetTypesWarnings() - ";
		LOG.debug(logPrefix+"(START) ");
		logPrefix = logPrefix+"(END) ";
		
		ArrayList fields = getAbstractTypeFields(model);
		for(int i=0;i<fields.size();i++){
			AbstractType field = (AbstractType)fields.get(i);
			field.resetTypeMessages();
		}
		
		ArrayList models = getInnerCommandDataModelList(model);
		for(int i=0;i<models.size();i++){
			CommandDataModel inModel = (CommandDataModel)models.get(i);
			fields = getAbstractTypeFields(inModel);
			for(int j=0;j<fields.size();j++){
				AbstractType field = (AbstractType)fields.get(j);
				field.resetTypeMessages();
			}
		}
		
		LOG.debug(logPrefix);			
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static void resetTypesWarningAndErrors(CommandDataModel model) throws Exception {
		
		String logPrefix = "Tools.resetTypesWarningsAndErrors() - ";
		LOG.debug(logPrefix+"(START) ");
		logPrefix = logPrefix+"(END) ";
		
		ArrayList fields = getAbstractTypeFields(model);
		for(int i=0;i<fields.size();i++){
			AbstractType field = (AbstractType)fields.get(i);
			field.resetTypeErrors();
			field.resetTypeWarnings();
		}
		
		ArrayList models = getInnerCommandDataModelList(model);
		for(int i=0;i<models.size();i++){
			CommandDataModel inModel = (CommandDataModel)models.get(i);
			fields = getAbstractTypeFields(inModel);
			for(int j=0;j<fields.size();j++){
				AbstractType field = (AbstractType)fields.get(j);
				field.resetTypeErrors();
				field.resetTypeWarnings();
			}
		}
		
		LOG.debug(logPrefix);			
	}

	/********************************************************************************/
	/********************************************************************************/
	public static void resetTypesWarningAndErrorsAndMessages(CommandDataModel model) throws Exception {
		
		String logPrefix = "Tools.resetTypesWarningsAndErrors() - ";
		LOG.debug(logPrefix+"(START) ");
		logPrefix = logPrefix+"(END) ";
		
		ArrayList fields = getAbstractTypeFields(model);
		for(int i=0;i<fields.size();i++){
			AbstractType field = (AbstractType)fields.get(i);
			field.resetTypeErrors();
			field.resetTypeWarnings();
			field.resetTypeMessages();
		}
		
		ArrayList models = getInnerCommandDataModelList(model);
		for(int i=0;i<models.size();i++){
			CommandDataModel inModel = (CommandDataModel)models.get(i);
			fields = getAbstractTypeFields(inModel);
			for(int j=0;j<fields.size();j++){
				AbstractType field = (AbstractType)fields.get(j);
				field.resetTypeErrors();
				field.resetTypeWarnings();
				field.resetTypeMessages();
			}
		}
		
		LOG.debug(logPrefix);			
	}

	/********************************************************************************/
	/********************************************************************************/
	public static boolean containsTypeErrors(CommandDataModel model) throws Exception {
		
		String logPrefix = "Tools.containsTypeErrors() - ";
		LOG.debug(logPrefix+"(START) ");
		logPrefix = logPrefix+"(END) ";
		
		ArrayList fields = getAbstractTypeFields(model);
		for(int i=0;i<fields.size();i++){
			AbstractType field = (AbstractType)fields.get(i);
			if(field.hasTypeErrors())
				return true;
		}
		
		ArrayList models = getInnerCommandDataModelList(model);
		for(int i=0;i<models.size();i++){
			CommandDataModel inModel = (CommandDataModel)models.get(i);
			fields = getAbstractTypeFields(inModel);
			for(int j=0;j<fields.size();j++){
				AbstractType field = (AbstractType)fields.get(j);
				if(field.hasTypeErrors())
					return true;
			}
		}
		
		LOG.debug(logPrefix);			
		return false;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static boolean containsTypeWarnings(CommandDataModel model) throws Exception {
		
		String logPrefix = "Tools.containsTypeWarnings() - ";
		LOG.debug(logPrefix+"(START) ");
		logPrefix = logPrefix+"(END) ";
		
		ArrayList fields = getAbstractTypeFields(model);
		for(int i=0;i<fields.size();i++){
			AbstractType field = (AbstractType)fields.get(i);
			if(field.hasTypeWarnings())
				return true;
		}
		
		ArrayList models = getInnerCommandDataModelList(model);
		for(int i=0;i<models.size();i++){
			CommandDataModel inModel = (CommandDataModel)models.get(i);
			fields = getAbstractTypeFields(inModel);
			for(int j=0;j<fields.size();j++){
				AbstractType field = (AbstractType)fields.get(j);
				if(field.hasTypeWarnings())
					return true;
			}
		}
		
		LOG.debug(logPrefix);			
		return false;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static boolean containsTypeWarningOrErrors(CommandDataModel model) throws Exception {
		
		String logPrefix = "Tools.containsTypeWarningOrErrors() - ";
		LOG.debug(logPrefix+"(START) ");
		logPrefix = logPrefix+"(END) ";
		
		ArrayList fields = getAbstractTypeFields(model);
		for(int i=0;i<fields.size();i++){
			AbstractType field = (AbstractType)fields.get(i);
			if(field.hasTypeErrors() || field.hasTypeWarnings())
				return true;
		}
		
		ArrayList models = getInnerCommandDataModelList(model);
		for(int i=0;i<models.size();i++){
			CommandDataModel inModel = (CommandDataModel)models.get(i);
			fields = getAbstractTypeFields(inModel);
			for(int j=0;j<fields.size();j++){
				AbstractType field = (AbstractType)fields.get(j);
				if(field.hasTypeErrors() || field.hasTypeWarnings())
					return true;
			}
		}
		
		LOG.debug(logPrefix);			
		return false;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static CommandDataModel modelFromXml(Class modelClass, InputStream xmlInputStream) throws Exception{
		
		CommandDataModel model = (CommandDataModel)modelClass.newInstance();
		
		SAXReader myreader = new SAXReader();
		org.dom4j.Document xmldoc = myreader.read(new org.xml.sax.InputSource(xmlInputStream));
	      
		Element root = xmldoc.getRootElement();
		transformXmlNodeToModel(model,root,true);

		xmlInputStream.close();
		return model;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static CommandDataModel modelFromXml(InputStream xmlInputStream) throws Exception{
		return modelFromXml(xmlInputStream,true);
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static CommandDataModel modelFromXml(InputStream xmlInputStream, boolean decodeXml) throws Exception{
		
		SAXReader myreader = new SAXReader();
		org.dom4j.Document xmldoc = myreader.read(new org.xml.sax.InputSource(xmlInputStream));
	      
		Element root = xmldoc.getRootElement();
		String modelClassName = root.valueOf("@class");
		if(modelClassName == null || modelClassName.equals("")){
			throw new Exception(thisClassName+".modelFromXml: class attribute not specified in model xml root structure");
		}
		
		Class modelClass = Class.forName(modelClassName);
		CommandDataModel model = (CommandDataModel)modelClass.newInstance();
		transformXmlNodeToModel(model,root,decodeXml);

		xmlInputStream.close();
		return model;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private static void transformXmlNodeToModel(CommandDataModel model, Element node, boolean decodeXml) throws Exception{
		
		Iterator it = node.content().iterator();
		Node currentNode;
		String propName  = "";
		
		try{
			
			while (it.hasNext()) {
				currentNode = (Node) it.next();
				if(currentNode.getNodeType() == Node.ELEMENT_NODE){
					
					propName  = currentNode.getName();
					String propType  = currentNode.valueOf("@type");
					String propValue = currentNode.getText();
					if(propType == null || propType.equals("")){
						try{
							Class propClass = getPropertyType(model,propName);
							if(model instanceof MapCommandDataModel && propClass == null){
								String dpropType = currentNode.valueOf("@dtype");
								if(dpropType != null && !dpropType.equals("")){
									try{
										((MapCommandDataModel)model).addProperty(propName,AbstractType.newInstance(Class.forName("com.atosorigin.wfem.types."+dpropType),propValue));
									}catch(Exception e){
										LOG.warning("Tools.transformXmlNodeToModel: Problems in setting property ["+propName+"]");
									}
									continue;
								}
							}
							
							if(propClass == null)
								continue;
							
							if(decodeXml && propClass.equals(StringType.class))
								propValue = xmlToString(propValue);

							AbstractType modelValue = (AbstractType)Tools.getPropertyValue(model,propName);
							if(modelValue != null)
								modelValue.setStringValue(propValue);
							else
								modelValue = AbstractType.newInstance(propClass,propValue);
							setPropertyValue(model,propName,modelValue);
						}catch(Exception e){
							LOG.warning("Tools.transformXmlNodeToModel: Problems in setting property ["+propName+"]");
						}
						continue;
					}
	
					if(propType.equalsIgnoreCase("model")){
						String modelClassName = currentNode.valueOf("@class");
						try{
							CommandDataModel innerModel = null;
							try{
								innerModel = (CommandDataModel)getPropertyValue(model,propName);
							}catch(Exception e){}
							if(innerModel == null){
								Class modelClass = Class.forName(modelClassName);
								innerModel = (CommandDataModel)modelClass.newInstance();
							}
							transformXmlNodeToModel(innerModel,(Element)currentNode,decodeXml); 					
							setPropertyValue(model,propName,innerModel);
						}catch(Exception e){
							LOG.warning("Tools.transformXmlNodeToModel: Problems in setting model ["+modelClassName+"] of property ["+propName+"]");
						}
						continue;
					}
	
					if(propType.equalsIgnoreCase("list")){
						String modelClassName = currentNode.valueOf("@class");
						boolean isMaxRowsExceeded = (currentNode.valueOf("@maxRowsExceeded")!=null && currentNode.valueOf("@maxRowsExceeded").equals("true"));
						Class modelClass = Class.forName(modelClassName);
						ListType list = new ListType(modelClass);
						list.setMaxRowsExceeded(isMaxRowsExceeded);
						List elements = currentNode.selectNodes(propName+"Element");
						for (int i=0;i<elements.size();i++){		      
						  Node elNode = (Node)elements.get(i);
						  CommandDataModel innerModel = null;
						  try{
							  innerModel = (CommandDataModel)modelClass.newInstance();
						  }catch(Exception e){
							  LOG.warning("Tools.transformXmlNodeToModel: Problems in setting model ["+modelClassName+"] of list property ["+propName+"]");
							  break;
						  }
						  transformXmlNodeToModel(innerModel,(Element)elNode,decodeXml);
						  list.add(innerModel); 										
						}
						setPropertyValue(model,propName,list);
						continue;
					}
				}
			}
					
		}catch(Exception e){
			String errMsg = "Tools.transformXmlNodeToModel: Exception in transforming into CommandDataModel xml string at node ["+propName+"]: "+e;
			e = new Exception(errMsg);
			throw e;
		}
	}

	/********************************************************************************/
	/********************************************************************************/
	public static String xmlFromModel(CommandDataModel model) throws Exception{
		return xmlFromModel(model,true,true);
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static String xmlFromModel(CommandDataModel model, 
									   boolean encodeXml, boolean insertVersion) throws Exception{

		String xmlString = "";
		if(insertVersion)
			xmlString += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		xmlString += "<model class=\""+model.getClass().getName()+"\">\n";
		xmlString += transformModelToXmlNode(model," ",encodeXml);
		xmlString += "</model>\n";
		return xmlString;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private static String transformModelToXmlNode(CommandDataModel model, String ident,
													boolean encodeXml) throws Exception{
		
		String xmlString = "";
		String newIdent = ident + " ";
		
		try{
			AbstractTypePropertyDescriptor[] props = ObjectsPropertiesCache.getInstance().getAbstractTypesPropertyDescriptors(model);
			if(props == null)
				return xmlString;
				
			for(int i=0;i<props.length;i++){
				String propName = props[i].getName();
				Object propValue = getPropertyValue(model,propName);
				if(propValue == null)
					continue;
				
				if(propValue instanceof AbstractType){
					if(((AbstractType)propValue).isNull())
						continue;
					String dtype ="";
					if(model instanceof MapCommandDataModel)
						dtype = " dtype=\""+propValue.getClass().getSimpleName()+"\"";
					if(propValue instanceof StringType){
						String value = propValue.toString();
						if(encodeXml)
							xmlString += newIdent+"<"+propName+dtype+">"+stringToXMLString(value)+"</"+propName+">\n";
						else
							xmlString += newIdent+"<"+propName+dtype+"><![CDATA["+value+"]]></"+propName+">\n";
					}else
						xmlString += newIdent+"<"+propName+dtype+">"+propValue.toString()+"</"+propName+">\n";
					continue;
				}
				
				if(propValue instanceof CommandDataModel){
					xmlString += newIdent+"<"+propName+" type=\"model\" class=\""+propValue.getClass().getName()+"\">\n";
					xmlString += transformModelToXmlNode((CommandDataModel)propValue,newIdent,encodeXml);
					xmlString += newIdent+"</"+propName+">\n";
					continue;
				}

				if(propValue instanceof ListType){
					List list = ((ListType)propValue).getElements();
					if(list.size() == 0)
						continue;
						
					xmlString += newIdent+"<"+propName+" type=\"list\" class=\""+((ListType)propValue).getModelType().getName()+"\"";
					xmlString += (((ListType)propValue).isMaxRowsExceeded()? " maxRowsExceeded=\"true\" ": " ") + ">\n";
					for(int j=0;j<list.size();j++){
						xmlString += newIdent+"<"+propName+"Element>\n";
						CommandDataModel listElement = (CommandDataModel)list.get(j);
						xmlString += transformModelToXmlNode(listElement,newIdent,encodeXml);
						xmlString += newIdent+"</"+propName+"Element>\n";
					}
					xmlString += newIdent+"</"+propName+">\n";
					continue;
				}
			}
			return xmlString;
			
		}catch(Exception e){
			String errMsg = "Tools.transformModelToXmlNode: Exception in transforming into xml CommandDataModel ["+model+"]: "+e;
			e = new Exception(errMsg);
			throw e;
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static String stringToHTMLString(String string) {
		
	    StringBuffer sb = new StringBuffer(string.length());

	    // true if last char was blank
	    boolean lastWasBlankChar = false;
	    int len = string.length();
	    char c;
	
	    for (int i = 0; i < len; i++){
	        c = string.charAt(i);
	        if (c == ' ') {
	            // blank gets extra work,
	            // this solves the problem you get if you replace all
	            // blanks with &nbsp;, if you do that you loss 
	            // word breaking
	            if (lastWasBlankChar) {
	                lastWasBlankChar = false;
	                sb.append("&nbsp;");
	            }else{
	                lastWasBlankChar = true;
	                sb.append(' ');
	            }
	        }else{
	            lastWasBlankChar = false;
	            
	            // HTML Special Chars
	            if (c == '"'){
	                sb.append("&quot;");
	            }else if (c == '&'){
	                sb.append("&amp;");
	            }else if (c == '<'){
	                sb.append("&lt;");
	            }else if (c == '>'){
	                sb.append("&gt;");
	            }else if (c == '\n'){
	                // Handle Newline
                    sb.append("&#");
                    sb.append(new Integer(10).toString());
                    sb.append(';');
	            }else if (c == '\r'){
	            	;
	            }else {
	                int ci = 0xffff & c;
	                if (ci < 160 ){
	                    // nothing special only 7 Bit
	                    sb.append(c);
	                }else {
	                    // Not 7 Bit use the unicode system
	                    sb.append("&#");
	                    sb.append(new Integer(ci).toString());
	                    sb.append(';');
	                }
	            }
	         }
	    }
	    return sb.toString();
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static String stringToXMLString(String string) {
		
	    StringBuffer sb = new StringBuffer(string.length());

	    int len = string.length();
	    char c;
	    for (int i = 0; i < len; i++){
	        c = string.charAt(i);
            // XML Special Chars
            if (c == '"'){
                sb.append("&quot;");
            }else if (c == '&'){
                sb.append("&amp;");
            }else if (c == '<'){
                sb.append("&lt;");
            }else if (c == '>'){
                sb.append("&gt;");
            }else if (c == '\''){
                sb.append("&apos;");
            }else if (c == '\n'){  // Handle Newline
                sb.append("&#");
                sb.append(new Integer(10).toString());
                sb.append(';');
            }else if (c == '\r'){
            	;
            }else {
                int ci = 0xffff & c;
                if(ci == 160){ // &nbsp;
                    sb.append(" ");
                }else if(ci < 160 ){ // nothing special only 7 Bit
                    sb.append(c);
                }else{  // Not 7 Bit use the unicode system
                    sb.append("&#");
                    sb.append(new Integer(ci).toString());
                    sb.append(';');
                }
            }
	    }
	    return sb.toString();
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static String convertSpecialChars(String string) {
		
	    StringBuffer sb = new StringBuffer(string.length());
	    int len = string.length();
	    char c;	
	    for (int i = 0; i < len; i++){
	        c = string.charAt(i);
            int ci = 0xffff & c;
            if (ci < 160 ){
                // nothing special only 7 Bit
                sb.append(c);
            }else {
                // Not 7 Bit use the unicode system
                sb.append("&#");
                sb.append(new Integer(ci).toString());
                sb.append(';');
            }
	    }
	    return sb.toString();
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static String htmlToString(String htmlString) {
		HtmlParser htmlParser = new HtmlParser(htmlString,true);
		return htmlParser.getResult();
	}

	/********************************************************************************/
	/********************************************************************************/
	public static String xmlToString(String xmlString) {
		xmlString = xmlString.replaceAll("\\s","&nbsp;");
		xmlString = xmlString.replaceAll("\\<","&lt;");
		HtmlParser htmlParser = new HtmlParser(xmlString,false);
		return htmlParser.getResult();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static Object cloneObject(Object obj) throws CloneNotSupportedException {
	    
		try{
			ByteArrayOutputStream bout = new ByteArrayOutputStream();
			ObjectOutputStream out = new ObjectOutputStream(bout);
			out.writeObject(obj);
			out.close();
					
			ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bout.toByteArray()));
			Object result = in.readObject();	
			in.close();
			bout.close();
			return result;
		}catch(Exception e){
			e.printStackTrace();
			throw new CloneNotSupportedException();
		}
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void zipFile(String outZipFileName, String fileName) throws Exception{
		String[] fileNames = new String[]{fileName};
		zipFiles(outZipFileName,fileNames);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void zipFiles(String outZipFileName, String[] fileNames) throws Exception{
		int BUFFER = 32*1024;
		
        BufferedInputStream origin = null;
        FileOutputStream dest = new FileOutputStream(outZipFileName);
        ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
        out.setMethod(ZipOutputStream.DEFLATED);

        byte data[] = new byte[BUFFER];
        for (int i=0; i<fileNames.length; i++) {
           File file = new File(fileNames[i]);
           FileInputStream fi = new FileInputStream(file);
           origin = new BufferedInputStream(fi, BUFFER);
           ZipEntry entry = new ZipEntry(file.getName());
           out.putNextEntry(entry);
           int count;
           while((count = origin.read(data, 0, BUFFER)) != -1) {
              out.write(data, 0, count);
           }
           out.closeEntry();
           origin.close();
        }
        out.close();
	}

    /**************************************************************************************************/
    /**************************************************************************************************/
    public static byte[] encodeBase64(byte input[]){
        return Base64.encodeBase64(input);
    }

    /**************************************************************************************************/
    /**************************************************************************************************/
    public static byte[] encodeBase64Chunked(byte input[]){
        return Base64.encodeBase64Chunked(input);
    }

    /**************************************************************************************************/
    /**************************************************************************************************/
    public static byte[] decodeBase64(byte input[]){
        return Base64.decodeBase64(input);    	
    }
    
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static byte[] zipArray(byte[] input) throws Exception{
	    ByteArrayInputStream in = new ByteArrayInputStream(input);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPOutputStream zipper = new GZIPOutputStream(out);
		byte[] buf = new byte[1024];
        int len;
        while((len = in.read(buf)) > 0)
        	zipper.write(buf, 0, len);
        zipper.close();
        out.close();
        in.close();
        return out.toByteArray();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static byte[] unzipArray(byte[] input) throws Exception{
	    ByteArrayInputStream in = new ByteArrayInputStream(input);
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
		GZIPInputStream unzipper = new GZIPInputStream(in);
		byte[] buf = new byte[1024];
        int len;
        while((len = unzipper.read(buf)) > 0)
        	out.write(buf, 0, len);
        unzipper.close();
        in.close();
        out.close();
        return out.toByteArray();
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static boolean xor(boolean operando1, boolean operando2, boolean operando3){
		/* Galli Luca
		 * Questa e' una XOR a 3 la formula e' di questo tipo !(A*B*C) * (A+B+C)
		 */ 
		return !(operando1 && operando2 && operando3) && (operando1 || operando2 || operando3);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static boolean xor(boolean operando1, boolean operando2){
		return xor(operando1,operando2,false);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String loadTextResourceAsString(String resourcePath, Object refClassLoaderObject) throws Exception{
		InputStream is = refClassLoaderObject.getClass().getResourceAsStream(resourcePath);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuffer resStr = new StringBuffer();
	    char[] buf = new char[16*1024];
	    int charsRead;
	    while((charsRead = reader.read(buf)) != -1)
			resStr.append(buf,0,charsRead);
		return resStr.toString();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String textByTemplateJsp10(String txtTemplate, CommandDataModel dataSource) throws Exception{
		return textByTemplateJspAll(txtTemplate,dataSource,patternJsp10);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String textByTemplateJsp20(String txtTemplate, CommandDataModel dataSource) throws Exception{
		return textByTemplateJspAll(txtTemplate,dataSource,patternJsp20);
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	private static String textByTemplateJspAll(String txtTemplate, CommandDataModel dataSource,
											   Pattern pattern) throws Exception{
		
		Matcher mat = pattern.matcher(txtTemplate);
		while(mat.find()) {                                                                                                                                                                                                                                
			String token = mat.group();
			if(pattern == patternJsp20){
				token = token.replaceAll("\\$","\\\\\\$");
				token = token.replaceAll("\\{","\\\\\\{");
				token = token.replaceAll("\\}","\\\\\\}");				
			}
			String propName = mat.group(1);
			String propValue = Tools.getPropertyValue(dataSource,propName).toString();
			txtTemplate = txtTemplate.replaceAll( token, propValue );
		}                                                                                                                                                                                                                                                  
		return txtTemplate;
	}
	

	/********************************************************************************/
	/********************************************************************************/
	public static CommandDataModel modelFromXmlV2(Class modelClass, InputStream xmlInputStream) throws Exception{
		
		CommandDataModel model = (CommandDataModel)modelClass.newInstance();
		
		SAXReader myreader = new SAXReader();
		org.dom4j.Document xmldoc = myreader.read(new org.xml.sax.InputSource(xmlInputStream));
	      
		Element root = xmldoc.getRootElement();
		transformXmlNodeToModelV2(model,root);

		xmlInputStream.close();
		return model;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static CommandDataModel modelFromXmlV2(InputStream xmlInputStream) throws Exception{
		
		SAXReader myreader = new SAXReader();
		org.dom4j.Document xmldoc = myreader.read(new org.xml.sax.InputSource(xmlInputStream));
	      
		Element root = xmldoc.getRootElement();
		String modelClassName = root.valueOf("@class");
		if(modelClassName == null || modelClassName.equals("")){
			throw new Exception(thisClassName+".modelFromXmlV2: class attribute not specified in model xml root structure");
		}
		
		Class modelClass = Class.forName(modelClassName);
		CommandDataModel model = (CommandDataModel)modelClass.newInstance();
		transformXmlNodeToModelV2(model,root);

		xmlInputStream.close();
		return model;
	}

	/********************************************************************************/
	/********************************************************************************/
	private static void transformXmlNodeToModelV2(CommandDataModel model, Element node) throws Exception{
		
		Iterator it = node.content().iterator();
		Node currentNode;
		String propName  = "";
		
		try{
			
			while (it.hasNext()) {
				currentNode = (Node) it.next();
				if(currentNode.getNodeType() == Node.ELEMENT_NODE){
					
					propName = currentNode.getName();
					String propValue = currentNode.getText();
					Class propClass = getPropertyType(model,propName);
					
					if(model instanceof MapCommandDataModel && propClass == null){
						String dpropType = currentNode.valueOf("@dtype");
						if(dpropType != null && !dpropType.equals("")){
							try{
								((MapCommandDataModel)model).addProperty(propName,AbstractType.newInstance(Class.forName("com.atosorigin.wfem.types."+dpropType),propValue));
							}catch(Exception e){
								LOG.warning(null,"Tools.transformXmlNodeToModelV2: Problems in setting MapCommandDataModel property ["+propName+"] of type ["+dpropType+"]");
							}
							continue;
						}
					}

					if(propClass == null)
						continue;
					
					if(CommandDataModel.class.isAssignableFrom(propClass)){
						try{
							CommandDataModel innerModel = (CommandDataModel)propClass.newInstance();
							transformXmlNodeToModelV2(innerModel,(Element)currentNode); 					
							setPropertyValue(model,propName,innerModel);
						}catch(Exception e){
							LOG.warning(null,"Tools.transformXmlNodeToModelV2: Problems in setting CommandDataModel property ["+propName+"] of type ["+propClass+"]");
						}
					}else if(ListType.class.isAssignableFrom(propClass)){
						ListType list = (ListType)getPropertyValue(model,propName);
						if(list == null || list.getModelType() == null){
							try{
								String modelClassName = currentNode.valueOf("@class");
								Class modelClass = Class.forName(modelClassName);
								list = new ListType(modelClass);
								setPropertyValue(model,propName,list);
							}catch(Exception e){
								LOG.warning(null,"Tools.transformXmlNodeToModelV2: Problems in setting ListType property ["+propName+"] of models ["+list.getModelType()+"]");
								break;
							}
						}
						CommandDataModel innerModel = null;
						try{
							innerModel = (CommandDataModel)list.getModelType().newInstance();
						}catch(Exception e){
							LOG.warning(null,"Tools.transformXmlNodeToModelV2: Problems in setting ListType property ["+propName+"] of models ["+list.getModelType()+"]");
							break;
						}
						transformXmlNodeToModelV2(innerModel,(Element)currentNode);
						list.add(innerModel); 										
					}else if(AbstractType.class.isAssignableFrom(propClass)){
						try{
							if(propClass.equals(StringType.class))
								propValue = htmlToString(propValue);
							AbstractType prop = AbstractType.newInstance(propClass,propValue);
							setPropertyValue(model,propName,prop);
						}catch(Exception e){
							LOG.warning(null,"Tools.transformXmlNodeToModelV2: Problems in setting AbstractType property ["+propName+"]");
						}
					}
					
				}
			}
					
		}catch(Exception e){
			String errMsg = "Tools.transformXmlNodeToModelV2: Exception in transforming into CommandDataModel xml string at node ["+propName+"]: "+e;
			e = new Exception(errMsg);
			throw e;
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static String xmlFromModelV2(CommandDataModel model) throws Exception{
		return xmlFromModelV2(model,true,true,true);
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static String xmlFromModelV2(CommandDataModel model, boolean encodeXml,
	    							  boolean insertVersion, boolean insertClassNames) throws Exception{

		String xmlString = "";
		if(insertVersion)
			xmlString += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		if(insertClassNames)
			xmlString += "<model class=\""+model.getClass().getName()+"\" ver=\"2\">\n";
		else
			xmlString += "<model ver=\"2\">\n";
		xmlString += transformModelToXmlNodeV2(model,"",encodeXml,insertClassNames);
		xmlString += "</model>\n";
		return xmlString;
	}

	/********************************************************************************/
	/********************************************************************************/
	private static String transformModelToXmlNodeV2(CommandDataModel model, String ident,
	    										  boolean encodeXml, boolean insertClassNames) throws Exception{
		
		String xmlString = "";
		String newIdent = ident.toString()+" ";
		
		try{
			AbstractTypePropertyDescriptor[] props = ObjectsPropertiesCache.getInstance().getAbstractTypesPropertyDescriptors(model);
			if(props == null)
				return xmlString;
				
			for(int i=0;i<props.length;i++){
				String propName = props[i].getName();
				Object propValue = Tools.getPropertyValue(model,propName);
				if(propValue == null || propValue.toString().equals(""))
					continue;
				
				if(propValue instanceof AbstractType){
					String dtype ="";
					if(model instanceof MapCommandDataModel)
						dtype = " dtype=\""+propValue.getClass().getSimpleName()+"\"";
					if(propValue instanceof StringType){
						String encodedValue = propValue.toString();
						if(encodeXml)
							encodedValue = stringToHTMLString(encodedValue);
						xmlString += newIdent+"<"+propName+dtype+"><![CDATA["+encodedValue+"]]></"+propName+">\n";
					}else{
						xmlString += newIdent+"<"+propName+dtype+">"+propValue.toString()+"</"+propName+">\n";
					}
					continue;
				}
				
				if(propValue instanceof CommandDataModel){
					if(insertClassNames)
						xmlString += newIdent+"<"+propName+" class=\""+propValue.getClass().getName()+"\">\n";
					else
						xmlString += newIdent+"<"+propName+">\n";
					xmlString += transformModelToXmlNodeV2((CommandDataModel)propValue,newIdent,encodeXml,insertClassNames);
					xmlString += newIdent+"</"+propName+">\n";
					continue;
				}

				if(propValue instanceof ListType){
					List list = ((ListType)propValue).getElements();
					if(list.size() == 0)
						continue;
						
					for(int j=0;j<list.size();j++){
						CommandDataModel listElement = (CommandDataModel)list.get(j);
						if(j == 0 && insertClassNames)
							xmlString += newIdent+"<"+propName+" class=\""+listElement.getClass().getName()+"\">\n";
						else
							xmlString += newIdent+"<"+propName+">\n";
						xmlString += transformModelToXmlNodeV2(listElement,newIdent,encodeXml,insertClassNames);
						xmlString += newIdent+"</"+propName+">\n";
					}
					continue;
				}
			}
			return xmlString;
			
		}catch(Exception e){
			String errMsg = "Tools.transformModelToXmlNodeV2: Exception in transforming into xml CommandDataModel ["+model+"]: "+e;
			e = new Exception(errMsg);
			throw e;
		}
	}
	
	/********************************************************************/
	/********************************************************************/
	public static String[] sortPropertiesKeys(Properties p){		
		ArrayList keys = new ArrayList(p.keySet());
		Collections.sort(keys,	new java.util.Comparator(){
												public int compare(Object o1, Object o2) {
													return o2.toString().length() - o1.toString().length();
												}
											});
		String[] result = new String[keys.size()];
		for(int i=0;i<result.length;i++)
			result[i] = (String)keys.get(i);
		return result;
	}
	
	/********************************************************************/
	/********************************************************************/
	public static String capitalize(String s){
		StringBuilder sb = new StringBuilder();
		String[] tokens = s.toLowerCase().split("\\s");
		for(int i = 0; i < tokens.length; i++){
			if(tokens[i].length() > 1){
			    char capLetter = Character.toUpperCase(tokens[i].charAt(0));
			    sb.append(capLetter + tokens[i].substring(1, tokens[i].length()));
			}else{
				sb.append(tokens[i]);
			}
			if(i < (tokens.length -1))
				sb.append(" ");
		}
		return sb.toString();
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static Map modelErrorMap(CommandDataModel model) throws Exception{
		Map result = new HashMap();
		recurseModelErrorMap(model,"",result);
		return result;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private static void recurseModelErrorMap(CommandDataModel model, String ident, Map errlist) throws Exception{
		
		try{
			PropertyDescriptor[] props = ObjectsPropertiesCache.getInstance().getProperties(model);
			if(props == null)
				return;
				
			for(int i=0;i<props.length;i++){
				String propName = props[i].getName();
				Object propValue = Tools.getPropertyValue(model,propName);
				if(propValue == null)
					continue;
				
				if(propValue instanceof AbstractType){
					
					String lab = ident+"_"+propName;
					if(ident.equals(""))
						lab = propName;
					if(((AbstractType)propValue).hasTypeErrors()){
						List l = ((AbstractType)propValue).getTypeErrors();
						for(int k=0;k<l.size();k++){
							TypeError e = (TypeError)l.get(k);
							if(errlist.get(lab) == null)
								errlist.put(lab,new ArrayList());
							ArrayList al = (ArrayList)errlist.get(lab);
							al.add(e);
						}
					}
					if(((AbstractType)propValue).hasTypeWarnings()){
						List l = ((AbstractType)propValue).getTypeWarnings();
						for(int k=0;k<l.size();k++){
							TypeWarning e = (TypeWarning)l.get(k);
							if(errlist.get(lab) == null)
								errlist.put(lab,new ArrayList());
							ArrayList al = (ArrayList)errlist.get(lab);
							al.add(e);
						}
					}
					continue;
				}
				
				if(propValue instanceof CommandDataModel){
					String lab = ident+"_"+propName;
					if(ident.equals(""))
						lab = propName;
					recurseModelErrorMap((CommandDataModel)propValue,lab,errlist);
					continue;
				}

				if(propValue instanceof ListType){
					List list = ((ListType)propValue).getElements();
					if(list.size() == 0)
						continue;
					
					String lab = ident+"_"+propName;
					if(ident.equals(""))
						lab = propName;
					
					for(int j=0;j<list.size();j++){
						CommandDataModel listElement = (CommandDataModel)list.get(j);
						recurseModelErrorMap(listElement,lab+j,errlist);
					}
					continue;
				}
			}
			return;
			
		}catch(Exception e){
			e = new Exception(e.toString());
			throw e;
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static boolean checkCodDescFieldsRange(CommandDataModel model) {

		// If model is null, return false
		if( model == null )
			return false;
		
		boolean res = true;
		
		Map codDescFields = model.getCodDescFields();
		Iterator keys = codDescFields.keySet().iterator();
		while(keys.hasNext()){
			String fieldName = (String)keys.next();
			if(!checkCodDescFieldsRange(model,fieldName))
				res = false;
		}
		
		try{
			ArrayList models = Tools.getInnerCommandDataModelList(model);
			for(int i=0;i<models.size();i++){
				CommandDataModel curModel = (CommandDataModel)models.get(i);
				codDescFields = curModel.getCodDescFields();
				keys = codDescFields.keySet().iterator();
				while(keys.hasNext()){
					String fieldName = (String)keys.next();
					if(!checkCodDescFieldsRange(curModel,fieldName))
						res = false;
				}
			}
			return res;
		}catch(Exception e){
	        return true;
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static boolean checkCodDescFieldsRange(CommandDataModel model, String propertyName) {
		
		int idx = propertyName.lastIndexOf("_");
		if(idx >= 0){
			String modelPropName = propertyName.substring(0,idx);
			propertyName = propertyName.substring(idx+1);
			try{
				model = (CommandDataModel)Tools.getPropertyValue(model,modelPropName);
			}catch(Exception e){
				return true;
			}
		}
		
		// Field is not registered as CodDescField
	    String codDescReferenceName = model.getCodDescReference(propertyName);
	    if(codDescReferenceName == null)
	    	return true;
	    
	
		CodDescDataList dataList = model.getCodDescDataList(propertyName);
		if(dataList == null)
			return true;
	
		try{
				
		    AbstractType propertyValue = (AbstractType)Tools.getPropertyValue(model, propertyName);
		    if(propertyValue == null)
		    	return true;
		    
		    if(propertyValue.isNull())
		    	return true;
		    
		    if(!propertyValue.isVisible())
		    	return true;
		    	
		    CodDescData data = dataList.getCodDesc(propertyValue.toString());
		    if(data == null){
		    	propertyValue.addTypeError(new TypeError("CodDescRangeException"));
	        	return false;
		    }
			return true;
		    
		}catch(Exception e){
			return true;
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static String xmlFromModelV3(CommandDataModel model) throws Exception{
		return xmlFromModelV3(model,true,true);
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static String xmlFromModelV3(CommandDataModel model, boolean insertVersion, boolean insertClassNames) throws Exception{

		String xmlString = "";
		if(insertVersion)
			xmlString += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n";
		if(insertClassNames)
			xmlString += "<model class=\""+model.getClass().getName()+"\" ver=\"3\">\n";
		else
			xmlString += "<model ver=\"3\">\n";
		xmlString += transformModelToXmlNodeV3(model,"",insertClassNames);
		xmlString += "</model>\n";
		return xmlString;
	}

	/********************************************************************************/
	/********************************************************************************/
	private static String transformModelToXmlNodeV3(CommandDataModel model, String ident,
	    										  	boolean insertClassNames) throws Exception{
		
		String xmlString = "";
		String newIdent = ident.toString()+" ";
		
		try{
			AbstractTypePropertyDescriptor[] props = ObjectsPropertiesCache.getInstance().getAbstractTypesPropertyDescriptors(model);
			if(props == null)
				return xmlString;
				
			for(int i=0;i<props.length;i++){
				String propName = props[i].getName();
				Object propValue = Tools.getPropertyValue(model,propName);
				if(propValue == null || propValue.toString().equals(""))
					continue;
				
				if(propValue instanceof AbstractType){
					String dtype ="";
					if(model instanceof MapCommandDataModel)
						dtype = " dtype=\""+propValue.getClass().getSimpleName()+"\"";
					if(propValue instanceof StringType){
						String encodedValue = stringToXMLString(propValue.toString());
						xmlString += newIdent+"<"+propName+dtype+">"+encodedValue+"</"+propName+">\n";
					}else{
						xmlString += newIdent+"<"+propName+dtype+">"+propValue.toString()+"</"+propName+">\n";
					}
					continue;
				}
				
				if(propValue instanceof CommandDataModel){
					if(insertClassNames)
						xmlString += newIdent+"<"+propName+" class=\""+propValue.getClass().getName()+"\">\n";
					else
						xmlString += newIdent+"<"+propName+">\n";
					xmlString += transformModelToXmlNodeV3((CommandDataModel)propValue,newIdent,insertClassNames);
					xmlString += newIdent+"</"+propName+">\n";
					continue;
				}

				if(propValue instanceof ListType){
					List list = ((ListType)propValue).getElements();
					if(list.size() == 0)
						continue;
						
					for(int j=0;j<list.size();j++){
						CommandDataModel listElement = (CommandDataModel)list.get(j);
						if(j == 0 && insertClassNames)
							xmlString += newIdent+"<"+propName+" class=\""+listElement.getClass().getName()+"\">\n";
						else
							xmlString += newIdent+"<"+propName+">\n";
						xmlString += transformModelToXmlNodeV3(listElement,newIdent,insertClassNames);
						xmlString += newIdent+"</"+propName+">\n";
					}
					continue;
				}
			}
			return xmlString;
			
		}catch(Exception e){
			String errMsg = "Tools.transformModelToXmlNodeV3: Exception in transforming into xml CommandDataModel ["+model+"]: "+e;
			e = new Exception(errMsg);
			throw e;
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static CommandDataModel modelFromXmlV3(Class modelClass, InputStream xmlInputStream) throws Exception{
		
		CommandDataModel model = (CommandDataModel)modelClass.newInstance();
		
		SAXReader myreader = new SAXReader();
		org.dom4j.Document xmldoc = myreader.read(new org.xml.sax.InputSource(xmlInputStream));
	      
		Element root = xmldoc.getRootElement();
		transformXmlNodeToModelV3(model,root);

		xmlInputStream.close();
		return model;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static CommandDataModel modelFromXmlV3(InputStream xmlInputStream) throws Exception{
		
		SAXReader myreader = new SAXReader();
		org.dom4j.Document xmldoc = myreader.read(new org.xml.sax.InputSource(xmlInputStream));
	      
		Element root = xmldoc.getRootElement();
		String modelClassName = root.valueOf("@class");
		if(modelClassName == null || modelClassName.equals("")){
			throw new Exception(thisClassName+".modelFromXmlV3: class attribute not specified in model xml root structure");
		}
		
		Class modelClass = Class.forName(modelClassName);
		CommandDataModel model = (CommandDataModel)modelClass.newInstance();
		transformXmlNodeToModelV3(model,root);

		xmlInputStream.close();
		return model;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private static void transformXmlNodeToModelV3(CommandDataModel model, Element node) throws Exception{
		
		Iterator it = node.content().iterator();
		Node currentNode;
		String propName  = "";
		
		try{
			
			while (it.hasNext()) {
				currentNode = (Node) it.next();
				if(currentNode.getNodeType() == Node.ELEMENT_NODE){
					
					propName = currentNode.getName();
					String propValue = currentNode.getText();
					Class propClass = getPropertyType(model,propName);
					
					if(model instanceof MapCommandDataModel && propClass == null){
						String dpropType = currentNode.valueOf("@dtype");
						if(dpropType != null && !dpropType.equals("")){
							try{
								((MapCommandDataModel)model).addProperty(propName,AbstractType.newInstance(Class.forName("com.atosorigin.wfem.types."+dpropType),propValue));
							}catch(Exception e){
								LOG.warning(null,"Tools.transformXmlNodeToModelV3: Problems in setting MapCommandDataModel property ["+propName+"] of type ["+dpropType+"]");
							}
							continue;
						}
					}
					
					if(propClass == null)
						continue;
					
					if(CommandDataModel.class.isAssignableFrom(propClass)){
						try{
							CommandDataModel innerModel = (CommandDataModel)propClass.newInstance();
							transformXmlNodeToModelV3(innerModel,(Element)currentNode); 					
							setPropertyValue(model,propName,innerModel);
						}catch(Exception e){
							LOG.warning(null,"Tools.transformXmlNodeToModelV3: Problems in setting CommandDataModel property ["+propName+"] of type ["+propClass+"]");
						}
					}else if(ListType.class.isAssignableFrom(propClass)){
						ListType list = (ListType)getPropertyValue(model,propName);
						if(list == null || list.getModelType() == null){
							try{
								String modelClassName = currentNode.valueOf("@class");
								Class modelClass = Class.forName(modelClassName);
								list = new ListType(modelClass);
								setPropertyValue(model,propName,list);
							}catch(Exception e){
								LOG.warning(null,"Tools.transformXmlNodeToModelV3: Problems in setting ListType property ["+propName+"] of models ["+list.getModelType()+"]");
								break;
							}
						}
						CommandDataModel innerModel = null;
						try{
							innerModel = (CommandDataModel)list.getModelType().newInstance();
						}catch(Exception e){
							LOG.warning(null,"Tools.transformXmlNodeToModelV3: Problems in setting ListType property ["+propName+"] of models ["+list.getModelType()+"]");
							break;
						}
						transformXmlNodeToModelV3(innerModel,(Element)currentNode);
						list.add(innerModel); 										
					}else if(AbstractType.class.isAssignableFrom(propClass)){
						try{
							AbstractType prop = AbstractType.newInstance(propClass,propValue);
							setPropertyValue(model,propName,prop);
						}catch(Exception e){
							LOG.warning(null,"Tools.transformXmlNodeToModelV3: Problems in setting AbstractType property ["+propName+"]");
						}
					}
					
				}
			}
					
		}catch(Exception e){
			String errMsg = "Tools.transformXmlNodeToModelV3: Exception in transforming into CommandDataModel xml string at node ["+propName+"]: "+e;
			e = new Exception(errMsg);
			throw e;
		}
	}
	
}
