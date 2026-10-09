package com.atosorigin.wfem.dbjavaclasses;

import java.util.HashMap;
import java.util.Map;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.DAOTableResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.dao.exceptions.NoRowsAffected;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;

/*****************************************************************************************************/
/*****************************************************************************************************/
public class DBJavaClassLoader extends ClassLoader {

	private static final String XML_NAME = "wfem.DbJavaClasses.DbJavaClasses";
	private static Map loadedClasses = new HashMap();
	private ClassLoader parent;
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	class ClassStruct{
		TimestampType compilationTime;
		Class		  generatedClass;
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	protected DBJavaClassLoader(){
		this.parent = Thread.currentThread().getContextClassLoader();
	}

	/*****************************************************************************************************/
	/*****************************************************************************************************/
    public static synchronized Object loadDbJavaObject(ClientSessionContext csc, String className) 
    														throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    	DBJavaClassLoader classLoader = new DBJavaClassLoader();
    	Class c = classLoader.createDbJavaClass(csc,className);
    	return c.newInstance();
    }
    
	/*****************************************************************************************************/
	/*****************************************************************************************************/
    public static synchronized Object loadDbJavaObject(ClientSessionContext csc, Class type) 
    														throws ClassNotFoundException, InstantiationException, IllegalAccessException {
    	DBJavaClassLoader classLoader = new DBJavaClassLoader();
    	Class c = classLoader.createDbJavaClass(csc,type.getName());
    	return c.newInstance();
    }

    /*****************************************************************************************************/
	/*****************************************************************************************************/
    public static synchronized Class loadDbJavaClass(ClientSessionContext csc, String className) throws ClassNotFoundException {
    	DBJavaClassLoader classLoader = new DBJavaClassLoader();
    	return classLoader.createDbJavaClass(csc,className);
    }
    
    /*****************************************************************************************************/
	/*****************************************************************************************************/
    public static synchronized Class loadDbJavaClass(ClientSessionContext csc, Class type) throws ClassNotFoundException {
    	DBJavaClassLoader classLoader = new DBJavaClassLoader();
    	return classLoader.createDbJavaClass(csc,type.getName());
    }

    /*****************************************************************************************************/
	/*****************************************************************************************************/
    protected synchronized Class loadClass(String className, boolean resolveIt) throws ClassNotFoundException {
    	return parent.loadClass(className);
    }

	/*****************************************************************************************************/
	/*****************************************************************************************************/
    private Class createDbJavaClass(ClientSessionContext csc, String className) throws ClassNotFoundException {

    	DbJavaClassModel javaClassModel = new DbJavaClassModel();
		javaClassModel.setNameOfClass(new StringType(className));

		DAOObject dao = null;
		try{
			
			dao = new DAOObject(csc,XML_NAME);
			dao.openConnection();	

			// Check if class was recompiled
			DAOQueryResultModel qres = dao.executeQueryAccess("dbJavaClassCompilationTime",javaClassModel);
			if(qres.getResult().size() == 0)
				return parent.loadClass(className);
			if(!javaClassModel.getEnabled().booleanValue())
				return parent.loadClass(className);
			TimestampType compTime = javaClassModel.getCompilationTime();
			if(compTime == null)
				compTime = new TimestampType();
			ClassStruct cs = (ClassStruct)loadedClasses.get(className);
			if(cs != null && cs.compilationTime.toString().equals(compTime.toString()))
				return cs.generatedClass;
			
			DAOTableResultModel tres = dao.executeTableLoadAccess("dbJavaClassBytecode",javaClassModel);
			if(tres.getResult().intValue() <= 0)
				throw new ClassNotFoundException();
			
			if(javaClassModel.getBytecode() == null || 
			   javaClassModel.getBytecode().isNull())
				throw new ClassNotFoundException();

			byte[] bytecode = javaClassModel.getBytecode().byteArrayValue();
			Class createdClass = createClass(className,bytecode);
			
			cs = new ClassStruct();
			cs.compilationTime = compTime;
			cs.generatedClass = createdClass;
			loadedClasses.put(className,cs);
			return createdClass; 
			
		}catch(ClassNotFoundException cnfe){
			return parent.loadClass(className);
		}catch(NoRowsAffected nra){
			return parent.loadClass(className);
		}catch(DAOException daoe){
			daoe.printStackTrace();
			return parent.loadClass(className);
		}catch(Exception e){
			e.printStackTrace();
			return parent.loadClass(className);
		}finally{
			if(dao != null) dao.closeConnection();
		}		
    }
    
	/*****************************************************************************************************/
	/*****************************************************************************************************/
    Class createClass(String className, byte[] bytecode) throws ClassFormatError {
		Class result = defineClass(className, bytecode, 0, bytecode.length);
	    if (result == null)
	        throw new ClassFormatError();    	
        resolveClass(result);
        return result;
    }

}
