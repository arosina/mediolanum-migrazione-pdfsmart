package com.atosorigin.wfem.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ByteArrayType;
import com.atosorigin.wfem.types.StringType;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class DbPdfWebResourceLoader {
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static InputStream getInputStream(String pdfWebResourceName){
		byte[] bytes = getBytes(pdfWebResourceName);
		if(bytes != null)
			return new ByteArrayInputStream(bytes);
		return null;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static byte[] getBytes(String pdfWebResourceName){
		try{
			DAOQueryResultModel qRes = DAOObject.executeDynaQueryAccess(new ClientSessionContext(),"PRGM",
														"select PDFWEBRESOURCE_BYTES from DB_PDFWEBRESOURCE where PDFWEBRESOURCE_NAME = '"+pdfWebResourceName+"'",
														null,ByteArrayType.class);
			ByteArrayType bytes = (ByteArrayType)qRes.getSingleResult();
			if(bytes != null && !bytes.isNull())
				return bytes.byteArrayValue();
			return null;
		}catch(DAOException daoe){
			daoe.printStackTrace();
			return null;
		}
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static String getUrl(String pdfWebResourceName){
		try{
			DAOQueryResultModel qRes = DAOObject.executeDynaQueryAccess(new ClientSessionContext(),"PRGM",
														"select PDFWEBRESOURCE_URL from DB_PDFWEBRESOURCE where PDFWEBRESOURCE_NAME = '"+pdfWebResourceName+"'",
														null,StringType.class);
			StringType url = (StringType)qRes.getSingleResult();
			if(url != null && !url.isNull())
				return url.toString();
			return null;
		}catch(DAOException daoe){
			daoe.printStackTrace();
			return null;
		}
	}
	
}
