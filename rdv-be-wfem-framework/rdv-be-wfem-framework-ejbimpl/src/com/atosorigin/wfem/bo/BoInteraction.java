package com.atosorigin.wfem.bo;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.controller.Configuration;
import com.atosorigin.wfem.controller.RequestManager;
import com.businessobjects.dsws.DSWSException;
import com.businessobjects.dsws.bicatalog.BICatalogObject;
import com.businessobjects.dsws.bicatalog.Document;
import com.businessobjects.dsws.bicatalog.InstanceRetrievalType;
import com.businessobjects.dsws.bicatalog.SimpleSearch;
import com.businessobjects.dsws.bicatalog.SortType;
import com.businessobjects.dsws.reportengine.Action;
import com.businessobjects.dsws.reportengine.BinaryView;
import com.businessobjects.dsws.reportengine.CallbackOption;
import com.businessobjects.dsws.reportengine.CharacterView;
import com.businessobjects.dsws.reportengine.DiscretePromptValue;
import com.businessobjects.dsws.reportengine.DocumentInformation;
import com.businessobjects.dsws.reportengine.FillPrompt;
import com.businessobjects.dsws.reportengine.FillPrompts;
import com.businessobjects.dsws.reportengine.Image;
import com.businessobjects.dsws.reportengine.ImageManagement;
import com.businessobjects.dsws.reportengine.OutputFormatType;
import com.businessobjects.dsws.reportengine.PromptInfo;
import com.businessobjects.dsws.reportengine.Refresh;
import com.businessobjects.dsws.reportengine.ReportEngine;
import com.businessobjects.dsws.reportengine.RetrieveBinaryView;
import com.businessobjects.dsws.reportengine.RetrieveData;
import com.businessobjects.dsws.reportengine.RetrieveMustFillInfo;
import com.businessobjects.dsws.reportengine.RetrievePromptsInfo;
import com.businessobjects.dsws.reportengine.RetrieveView;
import com.businessobjects.dsws.reportengine.RetrieveViewSupport;
import com.businessobjects.dsws.reportengine.View;
import com.businessobjects.dsws.reportengine.ViewModeType;
import com.businessobjects.dsws.reportengine.ViewSupport;
import com.businessobjects.dsws.reportengine.ViewType;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class BoInteraction {

	private static Pattern autoLoadDocPattern;
	private static Pattern openDocPattern;
	static{
		  autoLoadDocPattern = Pattern.compile("\\s*(window\\.location\\.replace\\()(['\"])(openDocument.htm\\?)(.*?)\\2\\)\\;",Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
		  openDocPattern = Pattern.compile("\\s*(['\"])(openDocument.htm\\?)(.*?)\\1",Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
	}
	
	public static final String BO_SESSION_INSTANCE_PARNAME = "wfemBusinessObjectSessionInstance";

	public static final String IMAGE_CALLBACK = "loadBoReportImageWfemCommand.wfem";
	
	public static final String BO_DOC_NAME = "sDoc";
	public static final String BO_DOC_NAME2 = "sDocName";
	public static final String BO_DOC_TYPE = "sType";
	public static final String BO_DOC_PARS = "lsS";
	
	public static final String IMAGE_DOCREF_HOLDER = "docRef";
	public static final String IMAGE_NAME_HOLDER = "imageName";

	public static final String PDF_OUT_TYPE = "pdf";
	public static final String HTML_OUT_TYPE = "html";
	public static final String XLS_OUT_TYPE = "xls";
	public static final String BINARY_OUT_TYPE = "bin";
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	static class Patterns{

		private static final String paramPattern="\\s*#\\s*(['\"])(.*?)\\1";
		static final Pattern showback = Pattern.compile("showback"+paramPattern,Pattern.CASE_INSENSITIVE);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static String[] getReportSupportedTypes(ClientSessionContext csc, String docName) throws Exception{
		
		BoSession boSession = new BoSession();
		try{
			
			boSession.login(csc);
			
		    String docId = retrieveDocId(boSession, docName);
		    if(docId == null)
		    	return null;
	        
		    RetrieveViewSupport rvs = new RetrieveViewSupport();
		    RetrieveData retrieveSupportedViews = new RetrieveData();
		    retrieveSupportedViews.setRetrieveViewSupport(rvs);
	
	        DocumentInformation docInfo = boSession.getReportEngine().getDocumentInformation(docId, null, null, null, retrieveSupportedViews);
			
	        Vector ret = new Vector();
			ViewSupport[] vs = docInfo.getViewSupports();
			if(vs == null)
				return (String[])ret.toArray();
			for(int i=0;i<vs.length;i++){
				if(vs[i].getOutputFormat().equals(OutputFormatType.PDF) && !ret.contains(PDF_OUT_TYPE))
					ret.add(PDF_OUT_TYPE);
				if(vs[i].getOutputFormat().equals(OutputFormatType.EXCEL) && !ret.contains(XLS_OUT_TYPE))
					ret.add(XLS_OUT_TYPE);
				if(vs[i].getOutputFormat().equals(OutputFormatType.HTML) && !ret.contains(HTML_OUT_TYPE))
					ret.add(HTML_OUT_TYPE);
				if(vs[i].getOutputFormat().equals(OutputFormatType.BINARY_CONTENT) && !ret.contains(BINARY_OUT_TYPE))
					ret.add(BINARY_OUT_TYPE);
			}
			return (String[])ret.toArray(new String[0]);
			
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}finally{
			boSession.logout();
		}
		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static BoReportInfo executeReport(RequestManager requestManager, ClientSessionContext csc,
									    	 String docName, BoParameters boPars, String outType, boolean showBack,
									    	 String charEncoding) throws Exception{
		
        BoReportInfo info = new BoReportInfo();
		BoSession boSession = new BoSession();
		try{
			
			boSession.login(csc);
			
		    String docId = retrieveDocId(boSession, docName);
	        if(docId == null){
	        	info.result = BoReportInfo.RESULT_KO;
	        	info.errorMessage = "Report ["+(docName==null?docId:docName)+"] not found";
	        	return info;
	        }
	        
	        View view = getReportView(csc, boSession.getReportEngine(), docName, docId, boPars, outType);

	        StringBuffer reportCommand = new StringBuffer();
	        reportCommand.append(BoInteraction.BO_DOC_NAME+"="+docName);
	        for(int i=0;i<boPars.size();i++){
	        	BoParameter boPar = (BoParameter)boPars.get(i);
	        	reportCommand.append("&"+BoInteraction.BO_DOC_PARS+(boPar.getParName()==null?"P"+(i+1):boPar.getParName())+"="+boPar.getParValue());
	        }
	        info.reportCommand = reportCommand.toString();
	        
	        String mimeType=null;
	        byte[] docBytes = null;
	        if(view instanceof BinaryView){
	        	
	        	docBytes = ((BinaryView)view).getContent();
        		mimeType = ((BinaryView)view).getMimeType();
        		
	        }else  if(view instanceof CharacterView){
	        	
	    		String result = ((CharacterView)view).getContent();
        		mimeType = ((CharacterView)view).getMimeType();
        		
        		Matcher mat = autoLoadDocPattern.matcher(result);
				while(mat.find()){
					String apice = mat.group(2);
					result = result.replace(mat.group(),"wfemOpenInnerBOReport("+apice+mat.group(4).replaceAll("\\\n","").replaceAll("\\\r","")+apice+",true);");
				}
				
        		mat = openDocPattern.matcher(result);
				while(mat.find()){
					String apice = "'";
					if(mat.group(1).equals("'"))
						apice = "\"";
					result = result.replace(mat.group(),mat.group(1)+"javascript:wfemOpenInnerBOReport("+apice+mat.group(3).replaceAll("\\\n","").replaceAll("\\\r","")+apice+",false);"+mat.group(1));
				}
	    		result = result.replaceAll("goback.htm","javascript:wfemBackInnerBOReport();");
	    		if(showBack){
	    			result = result+
	    					 "\n<div style='position:absolute;top:0;left:0;z-index:1000;background-color:white;'>" +
	    					 "&nbsp;<span style='color:#1A458F;text-decoration:underline;cursor:pointer;font-family:arial;' "+
	    					         "onclick='javascript:history.back();' "+
	    					         ">back</span>&nbsp;" +
	    					 "</div>\n";
	    		}
	    		
	    		String scripts = "\n\n<iframe id='wfemUtilIFrame' name='wfemUtilIFrame' src='"+Configuration.getInstance().getWfemlayoutWebApp()+"/wlt/blankPage.html' style='display:none;'></iframe>\n" +
	    						  "<script>\n"+
	    		
	    								"document.wfemReportCmd = \""+info.reportCommand+"\";\n\n"+

	    								"try{parent.endBOReport();}catch(e){}\n\n"+
	    								
	    								"function wfemOpenInnerBOReport(url,autoLoaded){\n"+
	    								"	try{ parent.openInnerBOReport(url,autoLoaded); }catch(e){}\n"+
	    								"	changeCurrentLocation('executeBoReport',url);\n"+
	    								"}\n\n"+
	    								
	    								"function wfemOpenInnerStaticFile(url){\n"+
    									"	try{parent.openInnerStaticFile(url);}catch(e){}\n"+
    									"	changeCurrentLocation('streamRemoteFile',url);\n"+
    									"}\n\n"+
    								
	    								"function changeCurrentLocation(cmd,url){\n"+
    									"	location.href='call.wfem?wfemCmd='+cmd+'&'+url;\n"+
    									"}\n\n"+
    								
	    								"function wfemOpenWindowBOReport(url,title){\n"+
	    								"	wfemOpenLocation('executeBoReport',url,title);\n" +
    									"}\n\n"+
    								
	    								"function wfemOpenWindowStaticFile(url,title){\n"+
    									"	wfemOpenLocation('streamRemoteFile',url,title);\n" +
    									"}\n\n"+
									
	    								"function wfemDonloadStaticFile(url){\n"+
										"	document.getElementById('wfemUtilIFrame').src='call.wfem?wfemCmd=streamRemoteFile&download=true&'+url;\n" +
										"}\n\n"+
								
	    								"function wfemOpenLocation(cmd,url,title){\n"+
	    								"	try{\n" +
	    								"		var woPAr1 = 'about:blank';"+
	    								"		if(!title || title == null){ woPAr1 = ''; title= ''; }\n"+
	    								"		try{url = parent.__retrieveGatewayUrl()+\"call.wfem?wfemCmd=\"+cmd+\"&\"+url;}catch(e){}\n"+
		    							"		var newWindow = window.open(woPAr1,'','titlebar=yes, scrollbars=yes, resizable=yes');\n"+
		    							"		newWindow.document.write(\"<ht\"+\"ml><he\"+\"ad><ti\"+\"tle>\"+title+\"&nbsp;</ti\"+\"tle></he\"+\"ad>\");\n"+
		    							"		newWindow.document.write(\"<bo\"+\"dy style='margin:0; padding:0;'>\");\n"+
		    							"		newWindow.document.write(\"<ifr\"+\"ame style='width=100%; height:100%' width='100%' height='100%' frameborder='0' "+
		    																		"src='\"+url+\"'></ifr\"+\"ame>\");\n"+
		    							"		newWindow.document.write(\"</bo\"+\"dy></ht\"+\"ml>\");\n"+
		    							"		newWindow.document.close();\n"+
	    								"	}catch(e){}\n"+
    									"}\n\n"+

    									"function wfemBackInnerBOReport(){\n"+
    									"	try{parent.backInnerBOReport();}catch(e){}\n"+
    									"}\n\n"+
    									
    									"document.onkeydown = function (e){\n"+
    									"	var intKey = (window.event) ? window.event.keyCode : e.which;\n"+
    									"	if(intKey == 8)\n"+
    									"		preventDefault(e);\n"+ 
    									"}\n\n"+
    									
    									"function preventDefault(event){\n"+
    									"	if (event == null && window.event){\n"+
    									"		event = window.event;\n"+
    									"	}\n"+
    									"	if (event != null){\n"+
    									"		if (event.preventDefault != null){\n"+
    									"			event.preventDefault();\n"+
    									"		} else if (event.returnValue !== null){\n"+
    									"			event.returnValue = false;\n"+
    									"		}\n"+
    									"	}\n"+
    									"	return false;\n"+
    									"}\n"+
    									
	    						  "</script>\n\n";
	    		result = scripts + result;
	    		
	    		if(charEncoding == null)
	    			docBytes = result.getBytes();
	    		else
	    			docBytes = result.getBytes(charEncoding);
	        	
	        	// Start session persistence to serve report images
				requestManager.getSession().setAttribute(BO_SESSION_INSTANCE_PARNAME,boSession);
		        boSession.start();
		        
	        }
	        
	        info.content = docBytes;
	        info.mimeType = mimeType;
	        
	        return info;
	        
		}catch(DSWSException dswse){
			dswse.printStackTrace();
			info.result = BoReportInfo.RESULT_KO;
			info.errorMessage = "DSWSException in executeReport: report ["+docName+"] - "+dswse.getCauseMessage();
			return info;
		}catch(Exception e){
			e.printStackTrace();
			info.result = BoReportInfo.RESULT_KO;
			info.errorMessage = "Exception in executeReport: report ["+docName+"] - "+e.toString();
			return info;
		}finally{
			boSession.logout();
		}
		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static String retrieveDocId(BoSession boSession, String docName) throws Exception{
		
		docName = docName.trim();
		
	    String docId = null;
	    SortType[] sort = new SortType[]{SortType.NAMEASC};
	    SimpleSearch search = new SimpleSearch();
	    search.setInName(docName);
	    search.setObjectType("documents");
	    
	    BICatalogObject[] searchResults = null;
		searchResults = boSession.getCatalog().search(search,sort,null,null,InstanceRetrievalType.ALL);
	    if(searchResults == null)
	    	return null;
	    for(int i=0;i<searchResults.length;i++){
	    	BICatalogObject catalogObj = searchResults[i];
	    	if(catalogObj instanceof Document){
	    		Document doc = (Document)catalogObj;
	    		String catDocName = doc.getName().trim();
	    		if(catDocName.equals(docName)){
		        	docId = doc.getUID();	  
		        	break;
	    		}
	    	}
	    }
	    return docId;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static View getReportView(ClientSessionContext csc, ReportEngine boReportEngine, String docName, String documentUID, 
									  BoParameters boPars, String type) throws Exception{

		  RetrieveMustFillInfo retrieveMustFillInfo = new RetrieveMustFillInfo();
          RetrievePromptsInfo retrievePromptsInfo = new RetrievePromptsInfo();
          retrieveMustFillInfo.setRetrievePromptsInfo(retrievePromptsInfo);

	      Action[] actions = new Action[1];
	      actions[0] = new Refresh();

	      DocumentInformation docInfo = boReportEngine.getDocumentInformation(documentUID, retrieveMustFillInfo, actions, null, null);
	      FillPrompts fillPrompts = initPrompts(docInfo, boPars);
	      if(fillPrompts != null) {
	    	  BoParamsVerifier.verifyParams(csc, docName, fillPrompts);
	    	  actions[0] = fillPrompts;
	      }else
	    	  actions = null;
	      
	      RetrieveView retView = null;
	      ViewSupport viewSupport = new ViewSupport();
	      
	      // Non BO mime type
	      if(!docInfo.getMimeType().equalsIgnoreCase("application/x-rpt") && 
	    	 !docInfo.getMimeType().equalsIgnoreCase("application/rep") &&
	    	 !docInfo.getMimeType().equalsIgnoreCase("application/wid")){
	    	  
        	  retView = new RetrieveBinaryView();
        	  viewSupport.setOutputFormat(OutputFormatType.BINARY_CONTENT);
        	  viewSupport.setViewType(ViewType.BINARY);
        	  viewSupport.setViewMode(ViewModeType.DOCUMENT);
	    	  
	      }else{
	    	  
	          if(HTML_OUT_TYPE.equalsIgnoreCase(type)){
	        	  retView = new RetrieveView();

	    	      ImageManagement imgMan = new ImageManagement();
	        	  CallbackOption[] callOpt = new CallbackOption[1];
	        	  imgMan.setCallbackScript(IMAGE_CALLBACK);
	        	  imgMan.setImageManagementHolder("imageName");
	        	  imgMan.setDocumentReferenceHolder("docRef");
	        	  callOpt[0] = imgMan;
	    	      retView.setCallbackOption(callOpt);
	        	  
	        	  viewSupport.setOutputFormat(OutputFormatType.HTML);
	        	  viewSupport.setViewType(ViewType.CHARACTER);
	        	  viewSupport.setViewMode(ViewModeType.REPORT);
	        	  
	          }else if(XLS_OUT_TYPE.equalsIgnoreCase(type)){
	        	  
	        	  retView = new RetrieveBinaryView();
	        	  viewSupport.setOutputFormat(OutputFormatType.EXCEL);
	        	  viewSupport.setViewType(ViewType.BINARY);
	        	  viewSupport.setViewMode(ViewModeType.DOCUMENT);
	        	  
	          }else if(PDF_OUT_TYPE.equalsIgnoreCase(type)){
	        	  
	        	  retView = new RetrieveBinaryView();
	        	  viewSupport.setOutputFormat(OutputFormatType.PDF);
	        	  viewSupport.setViewType(ViewType.BINARY);
	        	  viewSupport.setViewMode(ViewModeType.DOCUMENT);
	        	  
	          }else
	        	  throw new Exception("BO Interaction: Type ["+type+"] not supperted");
	          
	      }
          
	      retView.setViewSupport(viewSupport);

	      RetrieveData retBOData = new RetrieveData();
	      retBOData.setRetrieveView(retView);
	      
	      String docReference = docInfo.getDocumentReference();
	      docInfo = boReportEngine.getDocumentInformation(docReference, null, actions, null, retBOData);
	      return docInfo.getView();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private  static FillPrompts initPrompts(DocumentInformation docInfo, BoParameters boPars) throws Exception{
		
	      PromptInfo[] promptInfo = docInfo.getPromptInfo();
	      if(promptInfo == null || promptInfo.length == 0)
	    	  return null;
	      
	      if(boPars.isUseParNameAsIndex() && promptInfo.length > boPars.size())
	    	  throw new Exception("BO Interaction: Parameters vector must have equals or more elements then report parameters");
	      
	      FillPrompt prompt = null;
          DiscretePromptValue[] dpv = null;
          FillPrompt[] prompts = new FillPrompt[promptInfo.length];
          
          for(int i=0;i<promptInfo.length;i++){
              dpv = new DiscretePromptValue[1];
              dpv[0] = new DiscretePromptValue();
              String parValue = null;
              if(boPars.isUseParNameAsIndex()){
            	  try{
            		  parValue = ((BoParameter)boPars.get(i)).getParValue();
            	  }catch(Throwable t){
                	  throw new Exception("BO Interaction: Exception in setting report parameter on index ["+i+"]: "+t.toString());
            	  }
              }else{
           		  parValue = BoParameter.findParValueByName(promptInfo[i].getName(), boPars);
           		  if(parValue == null)
                	  throw new Exception("BO Interaction: Exception in setting report parameter ["+promptInfo[i].getName()+"]");
              }
        	  dpv[0].setValue(parValue);
              prompt = new FillPrompt();
              prompt.setID(promptInfo[i].getID());
              prompt.setValues(dpv);
              prompts[i] = prompt;
          }
          
          FillPrompts fillPrompts = new FillPrompts();
          fillPrompts.setFillPromptList(prompts);
          return fillPrompts;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public  static Image getReportImage(RequestManager requestManager,
										String docRef, String imageName) throws Exception{
		BoSession boSession = (BoSession)requestManager.getSession().getAttribute(BO_SESSION_INSTANCE_PARNAME);
		if(!boSession.isConnected())
			return null;
		Image image = boSession.getReportEngine().getImage(docRef,imageName);
		return image;
	}
	
	/***********************************************************************************************/
	private static final String END_TD_TR = "</td></tr>";
	/***********************************************************************************************/
	public static String htmlBoErrorMsg(HttpServletRequest request, BoReportInfo info) throws CommandException{
		if(info.errorMessage.indexOf(BoParamsVerifier.THROW_EXCEPTION_PREFIX_MESSAGE) >= 0) {
			String err = info.errorMessage.replace(BoParamsVerifier.THROW_EXCEPTION_PREFIX_MESSAGE, "");
			throw new CommandException(err);
		}
		return "<html><body><script>try{parent.endBOReport();}catch(e){}</script><center>"+
				"<table width='50%' style='border:solid 1px red;font-family: Arial;color:red; font-weight:bold;'>"+
			    	"<tr><td>&nbsp;</td></tr>"+
				    "<tr><td align='center' style='padding:10;'>Errore durante l'elaborazione ! "+
				    		"<span style='text-decoration:underline;cursor:pointer;' onclick='try{parent.retryBOReport();}catch(e){}\nlocation.href=\"call.wfem?"+request.getQueryString()+"\";'>Riprova</span>"+
				    END_TD_TR+
				    "<tr><td align='right'>"+
		    			"<span style='font-size:10;cursor:pointer;' onclick='try{if(document.getElementById(\"errorTable\").style.display==\"none\")document.getElementById(\"errorTable\").style.display=\"\"; else document.getElementById(\"errorTable\").style.display=\"none\";}catch(e){}'>&#9660;</span>"+
		    		END_TD_TR+
				"</table>"+
				"<br>"+
				"<table id='errorTable' width='50%' style='display:none;border:solid 1px red;font-family: Arial;color:red;'>"+
					"<tr><td align='center' style='padding:10;'>"+info.errorMessage+END_TD_TR+
			"</table>"+
		  "</center></body></html>";
	}
}
