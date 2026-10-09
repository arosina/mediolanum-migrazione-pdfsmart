package com.atosorigin.wfem.dbjavaclasses;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Javac;
import org.apache.tools.ant.types.Path;

import com.atosorigin.wfem.controller.Configuration;
import com.atosorigin.wfem.loggers.LogPrinter;
import com.atosorigin.wfem.types.ByteArrayType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

/***********************************************************************************************/
/***********************************************************************************************/
public class DbJavaClassCompiler {
	
	private static final Pattern packagePattern = Pattern.compile("\\s*package\\s+(.[^\\s]+)\\s*;{1}");
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public synchronized DbJavaClassModel compile(DbJavaClassModel javaClassModel){

		File javaFile = null;
        File classFile = null;
		
		javaClassModel.setBytecode(new ByteArrayType());
		javaClassModel.setCompilationErrors(false);
		javaClassModel.setCompilerOut(new StringType());			
		
		// If there is no ant we can't compile
		try{
			Class.forName("org.apache.tools.ant.Project");
		}catch(ClassNotFoundException cnfe){
			javaClassModel.setCompilationErrors(true);
			javaClassModel.setCompilerOut(new StringType("Ant package not found"));			
	        return javaClassModel;
		}

        byte[] bytecode = null;
		javaClassModel.setCompilationErrors(false);
		javaClassModel.setCompilerOut(new StringType(""));			
        String className = javaClassModel.getNameOfClass().toString();
		
        DbJavaClassSystemOutLogger sysOutLog = new DbJavaClassSystemOutLogger();
        DbJavaClassSystemErrLogger sysErrLog = new DbJavaClassSystemErrLogger();
        sysOutLog.startLog();
        sysErrLog.startLog();
        
		try{
			
	        String srcDirName = Configuration.getInstance().getDbJavaClassesDir();
			File srcDir = new File(srcDirName);
			
			String fileName = "";			
			int idx = className.lastIndexOf('.');
			if(idx >= 0)
				fileName = className.substring(idx+1);
			else
				fileName = className;

			String javaFileName = fileName+".java";
			javaFile = new File(srcDirName+javaFileName);

			String source = javaClassModel.getSource().toString();
			String packageName = getPackageName(source);
			
			if(!className.equals(packageName+"."+fileName)){
				javaClassModel.setCompilationErrors(true);
				javaClassModel.setCompilerOut(new StringType("Verify the package and the name of the class"));			
		        return javaClassModel;				
			}
			
			String classFileName = packageName.replace('.','/')+"/"+fileName+".class";
	        classFile = new File(srcDirName+classFileName);	
			
			String jarFiles = javaClassModel.getJarFiles().toString();
			jarFiles = jarFiles.replaceAll("\r","");
			jarFiles = jarFiles.replaceAll("\n",";");
			
			LogPrinter.println("JarFiles setted to ["+jarFiles+"]");
			try{
				jarFiles = replaceVars(jarFiles);
			}catch(Exception e){
				LogPrinter.println("WARNING: Problems in replacing vars");
			}
				        
			FileOutputStream javaFileOs = new FileOutputStream(javaFile);
			javaFileOs.write(source.getBytes());
			javaFileOs.close();

			Project project = new Project();
			DbJavaCompilerLogger logger = new DbJavaCompilerLogger();
	        logger.setOutputPrintStream(System.out);
	        logger.setErrorPrintStream(System.err);
			logger.setMessageOutputLevel(Project.MSG_VERBOSE);
	        project.addBuildListener(logger);
	        project.setBasedir(srcDirName);
	        project.init();

	        Path classPath = new Path(project);
	        StringTokenizer st = new StringTokenizer(jarFiles,";");
	        while(st.hasMoreTokens()){
	        	String jarFileName = st.nextToken();
	        	jarFileName = jarFileName.trim();
	        	classPath.setLocation(new File(jarFileName));
	        }
	
			Path srcPath = new Path(project);
			srcPath.setLocation(srcDir);

	        Javac javac = (Javac) project.createTask("javac");
	        javac.setSource("1.5");
	        javac.setTarget("1.5");
	        javac.setClasspath(classPath);
	        javac.setIncludes(javaFileName);
	        javac.setSrcdir(srcPath);
	        javac.setDestdir(srcDir);
	        javac.execute();
	        
			FileInputStream classFileIs = new FileInputStream(classFile);
			bytecode = new byte[classFileIs.available()];
	        classFileIs.read(bytecode);
	        classFileIs.close();
	        
            try{
    	        DBJavaClassLoader classLoader = new DBJavaClassLoader();
    			classLoader.createClass(className,bytecode);
            }catch(ClassFormatError e){
            	throw e;
            }
	        
            javaClassModel.setCompilationTime(Tools.now());
	        javaClassModel.setBytecode(new ByteArrayType(bytecode));
	        LogPrinter.println("Compilation ok");
			javaClassModel.setCompilerOut(new StringType(sysOutLog.stopLog()));		
			return javaClassModel;
			
		}catch(NoClassDefFoundError ncdf){
			javaClassModel.setCompilationErrors(true);
			javaClassModel.setCompilerOut(new StringType(ncdf.toString()));			
	        return javaClassModel;
		}catch(Exception e){
			javaClassModel.setCompilationErrors(true);
			javaClassModel.setCompilerOut(new StringType(e.toString()));			
	        return javaClassModel;
		}finally{
	        String errorReport = sysOutLog.stopLog()+"\n\n"+sysErrLog.stopLog();
	        if(javaClassModel.isCompilationErrors()){
	    		javaClassModel.setBytecode(new ByteArrayType());
				String outErrorReport = "Java class ["+javaClassModel.getNameOfClass()+"] has compilation errors\n\n";
				if(!javaClassModel.getCompilerOut().isNull())
					outErrorReport += javaClassModel.getCompilerOut().toString()+"\n";
				outErrorReport += "\n\n"+errorReport;			
				javaClassModel.setCompilerOut(new StringType(outErrorReport));
	        }
	        if(classFile != null) classFile.delete();
	        if(javaFile != null) javaFile.delete();
 		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private String getPackageName(String source){
		String packName = "";
		Matcher mat = packagePattern.matcher(source);
		if(mat.find())
			packName = mat.group(1);
		return packName;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private String replaceVars(String s) throws Exception{
		String result = "";
		int idx;
		for(;;){
			idx = s.indexOf('$');
			if(idx < 0)
				break;
			
			result += s.substring(0,idx);
			s = s.substring(idx+2); // Skip first '}'
			int idx2 = s.indexOf('}');
			String varName = s.substring(0,idx2);
			String varValue = System.getProperty(varName);
			varValue = replaceVars(varValue);
			s = s.substring(idx2+1);
			result += varValue;
		}
		result += s;
		return result;
	}
}
