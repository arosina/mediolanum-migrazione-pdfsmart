package com.atosorigin.wfem.nasstorage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.controller.Configuration;
import com.atosorigin.wfem.dao.DAOOSBResultModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.tierlog.MiddleTierAccessLogger;
import com.atosorigin.wfem.tierlog.TierAccessLoggerInfo;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.OSBCallData;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class NasStorage {

	/********************************************************************************/
	/********************************************************************************/
	@Deprecated
	public static void saveSimpleFile(String shareName, String filePath, String fileName, 
								  		InputStream fileInputStream) throws Exception{
		saveSimpleFile(new ClientSessionContext(), shareName, filePath, fileName, fileInputStream);
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static void saveSimpleFile(ClientSessionContext csc, String shareName, String filePath, String fileName, 
								  		InputStream fileInputStream) throws Exception{
		
		String shareRootPath = Configuration.getInstance().getNasShareRoot(shareName);
		if(shareRootPath == null)
			throw new Exception("NasStorage.saveSimpleFile: root path configuration for share ["+shareName+"] not found");
		
		String fileFullPath = "/"+shareRootPath+"/"+filePath;
		fileFullPath = fileFullPath.replaceAll("\\/\\/", "/");
		if(fileFullPath.endsWith("/") && !fileFullPath.equals("/"))
			fileFullPath = fileFullPath.substring(0, fileFullPath.length()-1);

		TierAccessLoggerInfo tli = MiddleTierAccessLogger.initTier(csc,"NAS","WRITESIMPLE");
		writeFileToNAS(fileFullPath, fileName, "", fileInputStream);
		tli.stop(new StringBuffer(fileFullPath+"||"+fileName));
	}

	/********************************************************************************/
	/********************************************************************************/
	@Deprecated
	public static File readSimpleFile(String shareName, String filePath, String fileName) throws Exception{
		return readSimpleFile(new ClientSessionContext(), shareName, filePath, fileName);
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static File readSimpleFile(ClientSessionContext csc, String shareName, String filePath, String fileName) throws Exception{
		
		String shareRootPath = Configuration.getInstance().getNasShareRoot(shareName);
		if(shareRootPath == null)
			throw new Exception("NasStorage.readSimpleFile: root path configuration for share ["+shareName+"] not found");
		
		String fileFullPath = "/"+shareRootPath+"/"+filePath;
		fileFullPath = fileFullPath.replaceAll("\\/\\/", "/");
		if(fileFullPath.endsWith("/") && !fileFullPath.equals("/"))
			fileFullPath = fileFullPath.substring(0, fileFullPath.length()-1);

		TierAccessLoggerInfo tli = MiddleTierAccessLogger.initTier(csc,"NAS","READSIMPLE");
		File res = new File(fileFullPath+"/"+fileName);
		tli.stop(new StringBuffer(fileFullPath+"||"+fileName));
		return res;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static SaveFileInfo saveFile(ClientSessionContext csc, String shareName, String filePath, String fileName, 
								  		 InputStream fileInputStream) throws Exception{

		String shareRootPath = Configuration.getInstance().getNasShareRoot(shareName);
		if(shareRootPath == null)
			throw new Exception("NasStorage.saveFile: root path configuration for share ["+shareName+"] not found");

		UUID uuidFile = UUID.randomUUID();

		String fileExt = "";
		int idx = fileName.lastIndexOf(".");
		if(idx >= 0){
			fileExt = fileName.substring(idx+1);
			fileName = fileName.substring(0, idx);
		}
		if(fileName.length() > 0)
			fileName = fileName+"_"+uuidFile.toString();
		else
			fileName = uuidFile.toString();
		
		String fileFullPath = "/"+shareRootPath+"/"+filePath;
		fileFullPath = fileFullPath.replaceAll("\\/\\/", "/");
		if(fileFullPath.endsWith("/") && !fileFullPath.equals("/"))
			fileFullPath = fileFullPath.substring(0, fileFullPath.length()-1);
		
		TierAccessLoggerInfo tli = MiddleTierAccessLogger.initTier(csc,"NAS","WRITE");
		writeFileToNAS(fileFullPath, fileName, fileExt, fileInputStream);
		tli.stop(new StringBuffer(fileFullPath+"||"+fileName+"||"+fileExt));
		
		NasSrvModel srvModel = new NasSrvModel();
	    srvModel.setDestinationPath(new StringType(fileFullPath));
	    srvModel.setNameApp(new StringType("WfemAppl"));
	    srvModel.setNameFile(new StringType(fileName));
	    srvModel.setSource(new StringType(Configuration.getInstance().getNasSource()));
	    srvModel.setType(new StringType(fileExt));
	    srvModel.setUserId(new StringType(csc.getUserCode()));
		
		try{
			DAOOSBResultModel wsRes = new DAOObject(csc, "wfem.NasStorage.NasStorage").executeOSBAccess("saveFile", srvModel);
			if(wsRes.getWsCallData().getStatus() != OSBCallData.STATUS_OK){
				throw new Exception("NasStorage.saveFile: comunication error calling insertMedia service "+wsRes.getWsCallData().getMessage());
			}else{
				if(srvModel.getIdFile().isNull()){
					throw new Exception("NasStorage.saveFile: no result calling insertMedia service");
				}
			}
		}catch(DAOException daoe){
			throw new Exception("NasStorage.saveFile: DAO exception calling insertMedia service");
		}
		
		SaveFileInfo result = new SaveFileInfo();
		result.setIdFile(srvModel.getIdFile().toString());
		result.setFileName(fileName+(fileExt.length()>0?"."+fileExt:""));
		result.setFilePath(fileFullPath);
		return result;
	}

	/********************************************************************************/
	/********************************************************************************/
	public static NasFileInfo readFile(ClientSessionContext csc, String idFile) throws Exception{

		if(idFile == null || idFile.length() == 0)
			throw new Exception("NasStorage.readFile: idFile in input is null or empty");
		
		NasSrvModel srvModel = new NasSrvModel();
	    srvModel.setIdFile(new StringType(idFile));
		
		try{
			DAOOSBResultModel wsRes = new DAOObject(csc, "wfem.NasStorage.NasStorage").executeOSBAccess("readFile", srvModel);
			if(wsRes.getWsCallData().getStatus() != OSBCallData.STATUS_OK){
				throw new Exception("NasStorage.readFile: comunication error calling listMedia service "+wsRes.getWsCallData().getMessage());
			}else{
				if(srvModel.getCheckIdFile().isNull()){
					throw new Exception("NasStorage.readFile: no result calling listMedia service for idFile=["+idFile+"]");
				}else if(!srvModel.getCheckIdFile().equals(idFile)){
					throw new Exception("NasStorage.readFile: wrong result calling listMedia service for idFile=["+idFile+"]");
				}
			}
		}catch(DAOException daoe){
			throw new Exception("NasStorage.readFile: DAO exception calling listMedia service");
		}
		
		String filePath = srvModel.getDestinationPath().toString();
		String fileName = srvModel.getNameFile().toString();
		String fileExt = srvModel.getType().toString();

		TierAccessLoggerInfo tli = MiddleTierAccessLogger.initTier(csc,"NAS","READ");
		File fileReference = readFileFromNAS(filePath, fileName, fileExt);
		tli.stop(new StringBuffer(filePath+"||"+fileName+"||"+fileExt));
		
		int idx = fileName.lastIndexOf("_");
		if(idx >= 0)
			fileName = fileName.substring(0, idx);
		else
			fileName = "";

		NasFileInfo result = new NasFileInfo();
		result.setFileName(fileName);
		result.setFileExt(fileExt);
		result.setFileReference(fileReference);
		return result;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static boolean existFile(ClientSessionContext csc, String idFile) throws Exception{

		if(idFile == null || idFile.length() == 0)
			throw new Exception("NasStorage.existFile: idFile in input is null or empty");
		
		NasSrvModel srvModel = new NasSrvModel();
	    srvModel.setIdFile(new StringType(idFile));
		
		try{
			DAOOSBResultModel wsRes = new DAOObject(csc, "wfem.NasStorage.NasStorage").executeOSBAccess("readFile", srvModel);
			if(wsRes.getWsCallData().getStatus() != OSBCallData.STATUS_OK){
				throw new Exception("NasStorage: comunication error calling listMedia service "+wsRes.getWsCallData().getMessage());
			}else{
				if(srvModel.getCheckIdFile().isNull()){
					throw new Exception("NasStorage.fileExist: no result calling listMedia service for idFile=["+idFile+"]");
				}else if(!srvModel.getCheckIdFile().equals(idFile)){
					throw new Exception("NasStorage.fileExist: wrong result calling listMedia service for idFile=["+idFile+"]");
				}
			}
		}catch(DAOException daoe){
			throw new Exception("NasStorage.fileExist: DAO exception calling listMedia service");
		}
		
		String filePath = srvModel.getDestinationPath().toString();
		String fileName = srvModel.getNameFile().toString();
		String fileExt = srvModel.getType().toString();

		TierAccessLoggerInfo tli = MiddleTierAccessLogger.initTier(csc,"NAS","EXIST");
		File fileReference = readFileFromNAS(filePath, fileName, fileExt);
		tli.stop(new StringBuffer(filePath+"||"+fileName+"||"+fileExt));
		
		return fileReference.exists();
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public static boolean deleteFile(ClientSessionContext csc, String idFile) throws Exception{

		if(idFile == null || idFile.length() == 0)
			throw new Exception("NasStorage.deleteFile: idFile in input is null or empty");

		NasSrvModel srvModel = new NasSrvModel();
	    srvModel.setIdFile(new StringType(idFile));
		
		try{
			DAOOSBResultModel wsRes = new DAOObject(csc, "wfem.NasStorage.NasStorage").executeOSBAccess("deleteFile", srvModel);
			if(wsRes.getWsCallData().getStatus() != OSBCallData.STATUS_OK){
				throw new Exception("NasStorage.deleteFile: comunication error calling deleteFileNAS service "+wsRes.getWsCallData().getMessage());
			}else{
				return srvModel.getDeleteFileNASResponse().booleanValue();
			}
		}catch(DAOException daoe){
			throw new Exception("NasStorage.deleteFile: DAO exception calling deleteFileNAS service");
		}
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private static void writeFileToNAS(String fileFullPath, String fileName, String ext, InputStream fileInputStream) throws IOException{
		String fullFileName = fileFullPath+"/"+fileName;
		if(ext.length() > 0)
			fullFileName += "."+ext;
		
		OutputStream fileOutputStream = new FileOutputStream(fullFileName);
	    try{
		    byte[] buf = new byte[(16*1024)];
		    int charsRead;
		    while((charsRead = fileInputStream.read(buf)) != -1){
		    	fileOutputStream.write(buf, 0, charsRead);
		    }
	    }finally{
	    	try{ fileInputStream.close(); }catch(Exception e){}
    		try{ fileOutputStream.flush(); }catch(Exception e){}
    		try{ fileOutputStream.close(); }catch(Exception e){}
	    }				
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private static File readFileFromNAS(String filePath, String fileName, String ext) throws IOException{
		if(filePath.startsWith("/") && filePath.length() > 1)
			filePath = filePath.substring(1);
		if(filePath.endsWith("/"))
			filePath = filePath.substring(0, filePath.length()-1);
		
		String fullFileName = "/"+filePath+"/"+fileName;
		if(ext.length() > 0)
			fullFileName += "."+ext;
		
		return new File(fullFileName);
	}
}
