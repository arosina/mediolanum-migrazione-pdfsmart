package com.atosorigin.wfem.util;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;

import com.atosorigin.wfem.backend.Facade;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.RemoteCommandException;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.controller.Configuration;
import com.atosorigin.wfem.controller.ControllerServlet;
import com.atosorigin.wfem.loggers.AbstractLogger;
import com.atosorigin.wfem.types.AbstractType;

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public abstract class AbstractRemoteObjectFactory  {

	protected String thisClassName = AbstractRemoteObjectFactory.class.getName();
	
	protected boolean local = false;
	protected AbstractLogger LOG;
	protected static Hashtable<String, String> env = null;
	
	/***********************************************************************************************************/
	/***********************************************************************************************************/
	public CommandDataModel callRemoteCommand(UserSessionContext usc, String remoteServer, 
							 	     		  Class commandClass, CommandDataModel inputModel) throws CommandException{
		String remoteWebapp = Configuration.getInstance().getExternalSystemWebapp();
		return callRemoteCommand(usc, remoteServer, remoteWebapp, commandClass, inputModel);
	}
	
	/***********************************************************************************************************/
	/***********************************************************************************************************/
	public CommandDataModel callRemoteCommand(UserSessionContext usc, String remoteServer, String remoteWebapp,
							 	     		  Class commandClass, CommandDataModel inputModel) throws CommandException{
		try{
			
			HttpClient httpCli = new HttpClient();
			if(remoteServer.endsWith("/"))
				remoteServer = remoteServer.substring(0,(remoteServer.length()-1));
			if(remoteWebapp.endsWith("/"))
				remoteWebapp = remoteWebapp.substring(0,(remoteWebapp.length()-1));
			if(remoteWebapp.startsWith("/"))
				remoteWebapp = remoteWebapp.substring(1);
			String url = remoteServer+"/"+remoteWebapp+"/call.wfem";
			
			HttpMethod method = new PostMethod(url);			
	        Vector params = new Vector();
	        NameValuePair par = null;
	        par = new NameValuePair("wfemCmd",commandClass.getName()+".execute"); params.add(par);
	        par = new NameValuePair(ControllerServlet.BHV_GET_XML_MODEL,"true"); params.add(par);
	        ArrayList fieldNames = Tools.getAbstractTypeFieldNames(inputModel);
	        for(int i=0;i<fieldNames.size();i++){
	        	String fieldName = (String)fieldNames.get(i);
	        	AbstractType field = (AbstractType)Tools.getPropertyValue(inputModel,fieldName);
		        par = new NameValuePair(fieldName,field.toString()); params.add(par);
	        }

	        NameValuePair[] np = new NameValuePair[]{};
	        np = (NameValuePair[])params.toArray(np);
	        method.setQueryString(np);
			httpCli.executeMethod(method);
			String xml = method.getResponseBodyAsString();
			method.releaseConnection();
			
			ByteArrayInputStream is = new ByteArrayInputStream(xml.getBytes());
		    CommandDataModel model = Tools.modelFromXml(is,false);
		    if(model instanceof RemoteCommandException){
		    	String errMsg = ((RemoteCommandException)model).getException().toString();
		    	throw new CommandException(errMsg);
		    }
			return model;
			
		}catch(Exception e){
			CommandException ce = new CommandException(e);
			throw ce;
		}
	}
	
	/***********************************************************************************************************/
	/***********************************************************************************************************/
	public RemoteCommandResponseStream callRemoteCommandAsStream(UserSessionContext usc, String remoteServer, 
										 	     		  		 Class commandClass, CommandDataModel inputModel) throws CommandException{
		String remoteWebapp = Configuration.getInstance().getExternalSystemWebapp();
		return callRemoteCommandAsStream(usc, remoteServer, remoteWebapp, commandClass, inputModel);
	}
	
	/***********************************************************************************************************/
	/***********************************************************************************************************/
	public RemoteCommandResponseStream callRemoteCommandAsStream(UserSessionContext usc, String remoteServer, String remoteWebapp,
										 	     		  		 Class commandClass, CommandDataModel inputModel) throws CommandException{
		try{
			
			HttpClient httpCli = new HttpClient();
			if(remoteServer.endsWith("/"))
				remoteServer = remoteServer.substring(0,(remoteServer.length()-1));
			if(remoteWebapp.endsWith("/"))
				remoteWebapp = remoteWebapp.substring(0,(remoteWebapp.length()-1));
			if(remoteWebapp.startsWith("/"))
				remoteWebapp = remoteWebapp.substring(1);
			String url = remoteServer+"/"+remoteWebapp+"/call.wfem";
			
			HttpMethod method = new PostMethod(url);			
	        Vector params = new Vector();
	        NameValuePair par = null;
	        par = new NameValuePair("wfemCmd",commandClass.getName()+".execute"); params.add(par);
	        par = new NameValuePair("externalSystemCmd","true"); params.add(par);
	        ArrayList fieldNames = Tools.getAbstractTypeFieldNames(inputModel);
	        for(int i=0;i<fieldNames.size();i++){
	        	String fieldName = (String)fieldNames.get(i);
	        	AbstractType field = (AbstractType)Tools.getPropertyValue(inputModel,fieldName);
		        par = new NameValuePair(fieldName,field.toString()); params.add(par);
	        }

	        NameValuePair[] np = new NameValuePair[]{};
	        np = (NameValuePair[])params.toArray(np);
	        method.setQueryString(np);
			int res = httpCli.executeMethod(method);
			InputStream is = null;
			if(res == HttpServletResponse.SC_OK)
				is = method.getResponseBodyAsStream();

			RemoteCommandResponseStream remoteCommandResponseStream = new RemoteCommandResponseStream();
			Header contentLength = method.getResponseHeader("content-length");
			Header fileName = method.getResponseHeader("file-name");			
			remoteCommandResponseStream.setContentLength(Integer.parseInt(contentLength.getValue()));
			remoteCommandResponseStream.setFileName(fileName.getValue());
			remoteCommandResponseStream.setHttpMethod(method);
			remoteCommandResponseStream.setInputStream(is);
			return remoteCommandResponseStream;
			
		}catch(Exception e){
			CommandException ce = new CommandException(e);
			throw ce;
		}
	}
	
	/***********************************************************************************************************/
	/***********************************************************************************************************/
	protected Object getLocalObject(Class objectClass) throws Exception {
		String interfaceClassName = objectClass.getName();
		String beanClassName = interfaceClassName+"Bean";
		Object localObject = null;
		try{
			
			LOG.debug("Creating local object ["+beanClassName+"]");
			objectClass = Class.forName(beanClassName);
			localObject = objectClass.newInstance();
			LOG.debug("Local object created: ["+localObject+"]");
			
			return localObject;
			
		}catch(ClassNotFoundException cnfe){
			Exception ne = new Exception(thisClassName+ ".getLocalObject: ClassNotFoundException exception loading class ["+beanClassName+"]: "+cnfe);
			LOG.error(ne);
			throw ne;
		}
	}
	
	/***********************************************************************************************************/
	/***********************************************************************************************************/
	public Facade getFacade(ClientSessionContext ctx, Class facadeInterfaceClass) throws Exception {
	
		String thisMethod = ".getFacade: ";
		String JNDIName = "";
		
		try {
			
			if(local)
				return (Facade)getLocalObject(facadeInterfaceClass);
		
			
    		String className = facadeInterfaceClass.getName();
    		String prefix = facadeInterfaceClass.getSimpleName();
    		InitialContext initialContext = null;
    		if(env == null)
    			initialContext = new InitialContext();
    		else
    			initialContext = new InitialContext(env);
    		Facade ejbRemoteInterface = (Facade)initialContext.lookup(prefix+"#"+className);
			LOG.debug("Facade component created: ["+ejbRemoteInterface+"]");
			return ejbRemoteInterface;
			
		}catch(Exception e) {
			Exception ne = new Exception(thisClassName+ thisMethod+"Exception in loading Facade. JNDI Name ["+JNDIName+"]: "+e);
			LOG.error(ne);
			throw e;
		}
	}
	
	/***********************************************************************************************************/
	/***********************************************************************************************************/
	protected void initProvider() throws Exception {
	
		String ejbProviderUrl = Configuration.getInstance().getEjbProviderUrl();
		String ejbContextFactory = Configuration.getInstance().getEjbContextFactory();

		if(ejbProviderUrl != null && ejbProviderUrl.equalsIgnoreCase("local")){
			LOG.info("RemoteObjectFactory: ejbProviderUrl=local -> objects are local");
			local = true;
			return;
		}
		LOG.info("RemoteObjectFactory: objects are remote");
		
		if(ejbContextFactory == null){ // EJB context factory is setted to default (No parameters)
			LOG.info("RemoteObjectFactory: initial context for Ejb setted to default (empty)");
			return;
		}
		
		String ejbUser = Configuration.getInstance().getEjbUser();
		String ejbPassword = Configuration.getInstance().getEjbPassword();
		
		env = new Hashtable<String, String>();
        env.put(Context.INITIAL_CONTEXT_FACTORY, ejbContextFactory);
        env.put(Context.PROVIDER_URL, ejbProviderUrl);
        if(ejbUser != null)
        	env.put(Context.SECURITY_PRINCIPAL, ejbUser);
        if(ejbPassword != null)
        	env.put(Context.SECURITY_CREDENTIALS, ejbPassword);
        
		LOG.info("RemoteObjectFactory: initial context for EJB Provider URL ["+ejbProviderUrl+"] EJB user ["+ejbUser+"] EJB password ["+ejbPassword+"]");
		
	}

}
