package com.atosorigin.wfem.dbjavaclasses;

import java.util.StringTokenizer;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.DAOTableResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.dao.exceptions.NoRowsAffected;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.ByteArrayType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;
import com.atosorigin.wfem.util.Tools;

/*****************************************************************************************************/
/*****************************************************************************************************/
public class DbJavaClassManager {

	private static final String XML_NAME = "wfem.DbJavaClasses.DbJavaClasses";
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public ListType getDbJavaProjectList(ClientSessionContext csc) throws Exception{
		
		DAOObject dao = null;
		try{
			
			dao = new DAOObject(csc,XML_NAME);
			dao.openConnection();	
			DAOQueryResultModel qRes = dao.executeQueryAccess("dbJavaProjectList",null);
			return qRes.getResult();
			
		}catch(DAOException daoe){
			throw new Exception(daoe.toString());
		}catch(Exception e){
			throw e;
		}finally{
			if(dao != null) dao.closeConnection();
		}		
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public DbJavaProjectModel readDbJavaProject(ClientSessionContext csc, StringType projectName) throws Exception{
		
		DbJavaProjectModel javaProjectModel = new DbJavaProjectModel();
		javaProjectModel.setProjectName(projectName);

		DAOObject dao = null;
		try{
			
			dao = new DAOObject(csc,XML_NAME);
			dao.openConnection();	
			dao.executeTableLoadAccess("dbJavaProject",javaProjectModel);
			DAOQueryResultModel qRes = dao.executeQueryAccess("dbJavaProjectClassList",javaProjectModel);
			javaProjectModel.setClasses(qRes.getResult());
			return javaProjectModel;
			
		}catch(DAOException daoe){
			throw new Exception(daoe.toString());
		}catch(Exception e){
			throw e;
		}finally{
			if(dao != null) dao.closeConnection();
		}		
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public DbJavaProjectModel writeDbJavaProject(ClientSessionContext csc, 
												 DbJavaProjectModel javaProjectModel) throws Exception{
		
		try{
			Tools.resetTypesErrors(javaProjectModel);
		}catch(Exception e){}
		javaProjectModel.resetCommandErrors();
		javaProjectModel.resetCommandMessages();

		if(javaProjectModel.isNewProject()){
			if(javaProjectModel.getProjectName().isNull()){
				javaProjectModel.getProjectName().addTypeError("projectNameMandatory");
				return javaProjectModel;
			}
		}
		
		DAOObject dao = null;
		try{
			
			dao = new DAOObject(csc,XML_NAME);
			dao.openConnection();

			if(javaProjectModel.isNewProject()){
				DAOQueryResultModel qRes = dao.executeQueryAccess("dbJavaProjectExist",javaProjectModel);
				IntegerType count = (IntegerType)qRes.getSingleResult();
				if(count.intValue() > 0){
					javaProjectModel.getProjectName().addTypeError("projectExist");
					return javaProjectModel;
				}
			}
			
			try{
				dao.executeTableUpdateAccess("dbJavaProject",javaProjectModel);
			}catch(NoRowsAffected nra){
				dao.executeTableInsertAccess("dbJavaProject",javaProjectModel);				
			}
			
			javaProjectModel.setNewProject(false);
			return javaProjectModel;
			
		}catch(DAOException daoe){
			throw new Exception(daoe.toString());
		}catch(Exception e){
			throw e;
		}finally{
			if(dao != null) dao.closeConnection();
		}		
	}

	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public void deleteDbJavaProjects(ClientSessionContext csc, StringType projectsToDelete) throws Exception{
		
		DAOObject dao = null;
		try{
			
			dao = new DAOObject(csc,XML_NAME);
			dao.openConnection();

			DbJavaProjectModel prj = new DbJavaProjectModel();
			StringTokenizer st = new StringTokenizer(projectsToDelete.toString(),",");
			while(st.hasMoreTokens()){
				String prjName = st.nextToken();
				prj = readDbJavaProject(csc,new StringType(prjName));
				ListType classes = prj.getClasses();
				for(int i=0;i<classes.size();i++){
					DbJavaClassModel cls = (DbJavaClassModel)classes.get(i);
					deleteDbJavaClasses(csc,cls.getNameOfClass());
				}
				dao.executeTableDeleteAccess("dbJavaProject",prj);
			}
			return;
			
		}catch(DAOException daoe){
			throw new Exception(daoe.toString());
		}catch(Exception e){
			throw e;
		}finally{
			if(dao != null) dao.closeConnection();
		}		
	}

	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public DbJavaClassModel readDbJavaClass(ClientSessionContext csc, 
											StringType className) throws Exception{
		
		DbJavaClassModel javaClassModel = new DbJavaClassModel();
		javaClassModel.setNameOfClass(className);

		DAOObject dao = null;
		try{
			
			dao = new DAOObject(csc,XML_NAME);
			dao.openConnection();	
			DAOTableResultModel tres = dao.executeTableLoadAccess("dbJavaClass",javaClassModel);
			if(tres.getResult().intValue() <= 0)
				throw new Exception("Class ["+className+"] not found");
			tres = dao.executeTableLoadAccess("dbJavaProject",javaClassModel);
			if(tres.getResult().intValue() <= 0)
				throw new Exception("Project for class ["+className+"] not found");
			dao.fillCodDesc(javaClassModel);
			return javaClassModel;
			
		}catch(NoRowsAffected nra){
			throw new Exception("Class ["+className+"] not found");
		}catch(DAOException daoe){
			throw new Exception(daoe.toString());
		}catch(Exception e){
			throw e;
		}finally{
			if(dao != null) dao.closeConnection();
		}		
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public DbJavaClassModel readDbJavaClassBackup(ClientSessionContext csc, 
												  StringType className, StringType backupTimeString) throws Exception{
		
		DbJavaClassModel javaClassModel = new DbJavaClassModel();
		javaClassModel.setNameOfClass(className);
		javaClassModel.setBackupTimeString(backupTimeString);

		DAOObject dao = null;
		try{
			
			dao = new DAOObject(csc,XML_NAME);
			dao.openConnection();	
			DAOTableResultModel tres = dao.executeTableLoadAccess("dbJavaClassBytecode",javaClassModel);
			BooleanType enabled = javaClassModel.getEnabled();
			tres = dao.executeTableLoadAccess("dbJavaClassBackup",javaClassModel);
			if(tres.getResult().intValue() <= 0)
				throw new Exception("Class ["+className+"] not found");
			tres = dao.executeTableLoadAccess("dbJavaProject",javaClassModel);
			if(tres.getResult().intValue() <= 0)
				throw new Exception("Project for class ["+className+"] not found");
			javaClassModel.setEnabled(enabled);
			dao.fillCodDesc(javaClassModel);
			return javaClassModel;
			
		}catch(NoRowsAffected nra){
			throw new Exception("Class ["+className+"] not found");
		}catch(DAOException daoe){
			throw new Exception(daoe.toString());
		}catch(Exception e){
			throw e;
		}finally{
			if(dao != null) dao.closeConnection();
		}		
	}
	
	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public DbJavaClassModel writeDbJavaClass(ClientSessionContext csc, 
											 DbJavaClassModel javaClassModel) throws Exception{
		
		try{
			Tools.resetTypesErrors(javaClassModel);
		}catch(Exception e){}
		javaClassModel.resetCommandErrors();
		javaClassModel.resetCommandMessages();
		
		javaClassModel.setBytecode(new ByteArrayType());
		javaClassModel.setCompilationErrors(false);
		javaClassModel.setCompilerOut(new StringType());			
		
		if(javaClassModel.isNewClass()){
			if(javaClassModel.getNameOfClass().isNull()){
				javaClassModel.setCompilationErrors(true);
				javaClassModel.setCompilerOut(new StringType("Class name is mandatory"));			
		        return javaClassModel;
			}
		}
		
		if(javaClassModel.getSource().isNull()){
			javaClassModel.setCompilationErrors(true);
			javaClassModel.setCompilerOut(new StringType("Source code is mandatory"));			
	        return javaClassModel;
		}

		DAOObject dao = null;
		try{
			
			dao = new DAOObject(csc,XML_NAME);
			dao.openConnection();
			
			if(javaClassModel.isNewClass()){
				DAOQueryResultModel qRes = dao.executeQueryAccess("dbJavaClassExist",javaClassModel);
				IntegerType count = (IntegerType)qRes.getSingleResult();
				if(count.intValue() > 0){
					javaClassModel.setCompilationErrors(true);
					javaClassModel.setCompilerOut(new StringType("Class exist"));			
			        return javaClassModel;
				}
			}

			DbJavaClassCompiler compiler = new DbJavaClassCompiler();
			javaClassModel = compiler.compile(javaClassModel);
			if(javaClassModel.isCompilationErrors())
		        return javaClassModel;				
			
			try{
				javaClassModel.setBackupTime(new TimestampType());
				if(javaClassModel.isNewClass()){
					dao.executeTableInsertAccess("dbJavaClass",javaClassModel);
				}else{
					DbJavaClassModel oldJavaClassModel = new DbJavaClassModel();
					oldJavaClassModel.setProjectName(javaClassModel.getProjectName());
					oldJavaClassModel.setNameOfClass(javaClassModel.getNameOfClass());
					dao.executeTableLoadAccess("dbJavaClass",oldJavaClassModel);
					oldJavaClassModel.setBackupTime(Tools.now());
					oldJavaClassModel.setBackupTimeString(new StringType(oldJavaClassModel.getBackupTime().toString()));
					dao.executeTableInsertAccess("dbJavaClassBackup",oldJavaClassModel);

					dao.executeTableUpdateAccess("dbJavaClass",javaClassModel);
				}
			}catch(DAOException daoe){
				throw new Exception(daoe.toString());				
			}catch(Exception e){
				throw e;
			}
			
			javaClassModel.setNewClass(false);
			dao.fillCodDesc(javaClassModel);
			return javaClassModel;
			
		}catch(DAOException daoe){
			javaClassModel.setCompilationErrors(true);
			javaClassModel.setCompilerOut(new StringType(daoe.toString()));			
	        return javaClassModel;
		}catch(Exception e){
			javaClassModel.setCompilationErrors(true);
			javaClassModel.setCompilerOut(new StringType(e.toString()));			
	        return javaClassModel;
		}finally{
			if(dao != null) dao.closeConnection();
		}		
	}

	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public void deleteDbJavaClasses(ClientSessionContext csc, StringType classesToDelete) throws Exception{
		
		DAOObject dao = null;
		try{
			
			dao = new DAOObject(csc,XML_NAME);
			dao.openConnection();

			DbJavaClassModel cls = new DbJavaClassModel();
			StringTokenizer st = new StringTokenizer(classesToDelete.toString(),",");
			while(st.hasMoreTokens()){
				String nameOfClass = st.nextToken();
				cls.setNameOfClass(new StringType(nameOfClass));
				dao.executeTableLoadAccess("dbJavaClass",cls);
				dao.executeTableDeleteAccess("dbJavaClass",cls);
				try{
					dao.executeTableDeleteChildsAccess("dbJavaClassBackup",cls);
				}catch(NoRowsAffected nra){}
			}
			return;
			
		}catch(DAOException daoe){
			throw new Exception(daoe.toString());
		}catch(Exception e){
			throw e;
		}finally{
			if(dao != null) dao.closeConnection();
		}		
	}

	/*****************************************************************************************************/
	/*****************************************************************************************************/
	public void enableDbJavaClasses(ClientSessionContext csc, StringType classesToEnable, BooleanType enable) throws Exception{
		
		DAOObject dao = null;
		try{
			
			dao = new DAOObject(csc,XML_NAME);
			dao.openConnection();

			DbJavaClassModel cls = new DbJavaClassModel();
			StringTokenizer st = new StringTokenizer(classesToEnable.toString(),",");
			while(st.hasMoreTokens()){
				String nameOfClass = st.nextToken();
				cls.setNameOfClass(new StringType(nameOfClass));
				dao.executeTableLoadAccess("dbJavaClass",cls);
				cls.setEnabled(enable);
				dao.executeTableUpdateAccess("dbJavaClass",cls);
			}
			return;
			
		}catch(DAOException daoe){
			throw new Exception(daoe.toString());
		}catch(Exception e){
			throw e;
		}finally{
			if(dao != null) dao.closeConnection();
		}		
	}

}
