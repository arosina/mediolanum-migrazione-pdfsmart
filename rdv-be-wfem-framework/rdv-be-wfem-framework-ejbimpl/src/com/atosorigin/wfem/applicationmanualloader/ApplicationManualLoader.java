package com.atosorigin.wfem.applicationmanualloader;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.MapCommandDataModel;
import com.atosorigin.wfem.controller.Configuration;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ByteArrayType;
import com.atosorigin.wfem.types.FileType;
import com.atosorigin.wfem.types.StringType;

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public class ApplicationManualLoader {
	
	private static final String XML_NAME = "wfem.ApplicationManualLoader.ApplicationManualLoader";
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static String getApplicationManualHtmlLink(ClientSessionContext csc, String documentId){
		return getApplicationManualHtmlLink(csc,documentId,null,false);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static String getApplicationManualHtmlLink(ClientSessionContext csc, String documentId, String style, boolean withImage){
		try{
			MapCommandDataModel model = loadApplicationManualInfo(csc,documentId);
			if(model == null)
				return "";
			
			StringType label = (StringType)model.readProperty("documentLabel");
			if(label == null || label.isNull())
				label = new StringType("Manuale operativo");
			if(style == null)
				style = "font-size:8pt;font-weight:bold;text-decoration:underline;";
			StringBuffer result = new StringBuffer("<table cellspacing='0' cellpadding='0'><tr>");
			result.append("<td onclick='openApplicationManual(\""+documentId+"\",this);' class='text' nowrap='nowrap' style='"+style+"cursor:pointer;'>"+label+"</td>");
			if(withImage)
				result.append("<td>&nbsp;</td><td style='width:16px;'><img src='"+Configuration.getInstance().getWfemlayoutWebApp()+"/images/pdf.gif'></td>");
		    result.append("</tr></table>");
			return result.toString();
		}catch(Exception e){
			return "";
		}
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public static FileType loadApplicationManual(ClientSessionContext csc, String documentId) throws Exception{
		try{
			MapCommandDataModel model = loadApplicationManualInfo(csc,documentId);
			if(model == null)
				return new FileType();
			
			DAOObject dao = new DAOObject(csc,XML_NAME);
			dao.executeTableLoadAccess("loadManualContent",model);
			
			StringType contentType = (StringType)model.readProperty("documentContentType");
			StringType fileName = (StringType)model.readProperty("documentFileName");
			ByteArrayType fileContent = (ByteArrayType)model.readProperty("documentImage");
			
			FileType result = new FileType(fileContent.byteArrayValue(),contentType.toString(),fileName.toString());
			return result;
		}catch(DAOException daoe){
			Exception e = new Exception(daoe.toString());
			throw e;
		}catch(Exception e){
			throw e;
		}		
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private static MapCommandDataModel loadApplicationManualInfo(ClientSessionContext csc, String documentId) throws Exception{
		try{
			String environment = "ONLINE";
			if(Configuration.getInstance().isOfflineEnvironment())
				environment = "OFFLINE";
			MapCommandDataModel model = new MapCommandDataModel();
			model.addProperty("documentId", new StringType(documentId));
			model.addProperty("environment", new StringType(environment));
			model.addProperty("documentLabel", new StringType());
			model.addProperty("documentContentType", new StringType());
			model.addProperty("documentFileName", new StringType());
			model.addProperty("documentImage", new ByteArrayType());
			DAOObject dao = new DAOObject(csc,XML_NAME);
			DAOQueryResultModel qRes = dao.executeQueryAccess("loadManualKey",model);
			if(qRes.getResult().size() <= 0 || model.readProperty("documentFileName").isNull())
				return null;
			return model;
		}catch(DAOException daoe){
			Exception e = new Exception(daoe.toString());
			throw e;
		}catch(Exception e){
			throw e;
		}		
	}

}
