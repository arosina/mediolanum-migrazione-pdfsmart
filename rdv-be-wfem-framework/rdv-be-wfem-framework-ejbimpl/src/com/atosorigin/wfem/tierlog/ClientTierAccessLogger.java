package com.atosorigin.wfem.tierlog;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Map;
import java.util.Properties;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.controller.Configuration;
import com.atosorigin.wfem.controller.RequestManager;
import com.atosorigin.wfem.util.RefreshEventListener;
import com.atosorigin.wfem.util.RefreshNotifier;
import com.atosorigin.wfem.util.Tools;
import com.atosorigin.wfem.webservices.ClientSessionContextBean;
import com.atosorigin.wfem.webservices.MethodInputBean;
import com.atosorigin.wfem.webservices.WebServicesTools;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class ClientTierAccessLogger extends AbstractAccessLogger implements RefreshEventListener {
	
	private static ClientTierAccessLogger singleton = null;
	
	private static final String CONFIGURATION_FILE_EXCLUDED = Configuration.CONFIGURATION_FILES_ROOT+"/wfemTierAccessLoggerConfExcluded.properties";
	private static final String CONFIGURATION_FILE = Configuration.CONFIGURATION_FILES_ROOT+"/wfemTierAccessLoggerConf.properties";
	private static final int REFRESH_TIME = 30;
	private String[] configApplExcluded = null; 
	private String[] configAppl = null; 
	
	/****************************************************************/
	/****************************************************************/
	private ClientTierAccessLogger(){
		try{
			initLogger("Client");
			loadConfigAppl();
			RefreshNotifier.addListener(this,REFRESH_TIME);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/********************************************************************/
	/********************************************************************/
	public static ClientTierAccessLogger getInstance() {
		if(singleton == null) {
	        synchronized (ClientTierAccessLogger.class) {
				if(singleton == null) {
					singleton = new ClientTierAccessLogger();
				}
	        }
		}
		return singleton;
	}
	
	/****************************************************************/
	/****************************************************************/
	public static TierAccessLoggerInfo initTier(ClientSessionContext csc, MethodInputBean input){
		
		String userCode = "unknown";
		
		String caller = "unknown";
		String callid = ""+Thread.currentThread().getId()+Calendar.getInstance().getTimeInMillis();
		if(input != null){
			ClientSessionContextBean cscbean = input.getMethodHeader().getClientSessionContext();
			if(cscbean != null){
				if(!cscbean.getCaller().isEmpty()){
					caller = cscbean.getCaller(); 
					if(!cscbean.getCallid().isEmpty())
						callid = cscbean.getCallid();
				}
			}
		}
		
		String className = "unknown";
		String methodName = "unknown";
		try{
			StackTraceElement ste = Thread.currentThread().getStackTrace()[3]; 
			className = ste.getClassName();
			methodName = ste.getMethodName();
		}catch(Throwable t){}
		
		if(csc != null && csc.getUserCode() != null)
			userCode = csc.getUserCode().toString();
		
		csc.setCrossTierTraceId(null);

		TierAccessLoggerInfo ali = new TierAccessLoggerInfo();
		ali.setTier(TierAccessLoggerInfo.CLIENT_TIER);
		ali.setAccessUser(userCode);
		ali.setAccessType("WS");
		ali.setAccessName(className+"."+methodName);
		ali.setClientIp(csc.getClientIp());
		ali.setInputParameters("");
		ali.start();		
		if(ClientTierAccessLogger.getInstance().isTraceActiveOnApp(className)){
	    	if(input != null){
		    	try{
		    		Map<String, String> notNullModelPropertiseMap = WebServicesTools.notNullModelPropertiseMap(input);
		    		StringBuffer sb = new StringBuffer();
		    		for(String propName : notNullModelPropertiseMap.keySet()){
		    			sb.append("|"+propName+"="+AbstractAccessLogger.formatParameters(notNullModelPropertiseMap.get(propName))+"|");
		    		}
		    		ali.setInputParameters(sb.toString());
		    	}catch(Throwable t){
		    		ali.setInputParameters(t.toString());
		    	}
	    	}
			String id = caller+"-"+callid;
			csc.setCrossTierTraceId(id); // To tell to middle tier that trace is on for the access name, null otherwise
			ali.setAccessId(id);
		}
		return ali;
	}

	/****************************************************************/
	/****************************************************************/
	public static TierAccessLoggerInfo initTier(RequestManager rm, ClientSessionContext csc, String accessName, boolean fromClientRequest){
		
		String userCode = "unknown";
		
		if(csc != null && csc.getUserCode() != null)
			userCode = csc.getUserCode().toString();
		
		csc.setCrossTierTraceId(null);

		TierAccessLoggerInfo ali = new TierAccessLoggerInfo();
		ali.setTier(TierAccessLoggerInfo.CLIENT_TIER);
		ali.setAccessUser(userCode);
		if(fromClientRequest)
			ali.setAccessType("REQUEST");
		else
			ali.setAccessType("FORWARD");
		ali.setAccessName(accessName.toString());
		ali.setClientIp(csc.getClientIp());
		ali.setInputParameters("");
		ali.start();		
		if(ClientTierAccessLogger.getInstance().isTraceActiveOnApp(accessName)){
			String id = rm.getSession().getId()+
						Thread.currentThread().getId()+
						Calendar.getInstance().getTimeInMillis();		
			csc.setCrossTierTraceId(id); // To tell to middle tier that trace is on for the access name, null otherwise
			ali.setAccessId(id);
		}
		return ali;
	}

	/****************************************************************/
	/****************************************************************/
	public boolean isTraceActiveOnApp(String accessName){
		try{
			
			if(configAppl == null)
				return false;
			
			if(configApplExcluded != null){
		        for(int i=0;i<configApplExcluded.length;i++){
			    	if(accessName.startsWith(configApplExcluded[i]))
			    		return false;
		        }
			}
			
	        for(int i=0;i<configAppl.length;i++){
		    	if(accessName.startsWith(configAppl[i]))
		    		return true;
	        }
	        return false;
	        
		}catch(Throwable t){
			return false;
		}
	}
	
	/****************************************************************/
	/****************************************************************/
	public void refresh() {
		loadConfigAppl();
	}
	
	/****************************************************************/
	/****************************************************************/
	private void loadConfigAppl(){
		try{
			
			// Included packages
			Properties p = new Properties();
			InputStream inputStream = this.getClass().getResourceAsStream(CONFIGURATION_FILE);
			if(inputStream == null){
				configAppl = null;
			}else{
				p.load(inputStream);
				inputStream.close();
				configAppl = Tools.sortPropertiesKeys(p);
			}
			
			// Excluded packages
			p = new Properties();
			inputStream = this.getClass().getResourceAsStream(CONFIGURATION_FILE_EXCLUDED);
			if(inputStream == null){
				configApplExcluded = null;
			}else{
				p.load(inputStream);
				inputStream.close();
				configApplExcluded = Tools.sortPropertiesKeys(p);
			}
			return;
			
		}catch(Throwable t){
			t.printStackTrace();
			return;
		}
	}
	
}