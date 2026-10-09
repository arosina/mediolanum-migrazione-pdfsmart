package com.atosorigin.wfem.bo;

import java.io.Serializable;
import java.net.URL;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.controller.Configuration;
import com.businessobjects.dsws.Connection;
import com.businessobjects.dsws.bicatalog.BICatalog;
import com.businessobjects.dsws.reportengine.ReportEngine;
import com.businessobjects.dsws.session.EnterpriseCredential;
import com.businessobjects.dsws.session.Session;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class BoSession implements Serializable, Runnable{
	
	private boolean connected = false;
	private Session session = null;
	private Connection connection = null;
	private ReportEngine reportEngine = null;
	private BICatalog catalog = null;
	private boolean sessionTimerStarted = false;
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void run() {
		
		// Sleep for seconds ti serve report images
		try{
			Thread.currentThread().sleep(60*1000);
		}catch(Exception e){}
		
		try{session.logout();}catch(Exception e){e.printStackTrace();}
        connected = false;
		sessionTimerStarted = false;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void start(){
		
		sessionTimerStarted = true;
		
		Thread t = new Thread(this);
		t.setDaemon(true);
		t.start();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void login(ClientSessionContext csc) throws Exception{
		
		String boUrl = Configuration.getInstance().getBoUrl();
		connection = new Connection(new URL(boUrl));
        session = new Session(connection);
        
        EnterpriseCredential credential = new EnterpriseCredential();
        String userCode = csc.getCurrentLinkedUserCode();
        String pwd = "MKS";
        if(userCode.equals("0000000113")) 
        	userCode = "1000000000";
        if(csc.getChannelCode().equals("T")) 
        	pwd = "PTM";
        else if(csc.getChannelCode().equals("M")) 
        	pwd = "MUT";
        credential.setLogin(userCode);
        credential.setPassword(pwd);
        
        connected = true;
        session.login(credential);
        
        String[] engURLS = session.getAssociatedServicesURL("ReportEngine");
        if(engURLS.length == 0 )
        	throw new Exception("BO Engine not found");

        reportEngine = ReportEngine.getInstance(session,engURLS[0]);

        // Search for document
        String[] catURLS = session.getAssociatedServicesURL("BICatalog");
        if(catURLS.length == 0 )
        	throw new Exception("BO Catalog not found");
        catalog = BICatalog.getInstance(session,catURLS[0]);
        
        return;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void logout(){
		if(sessionTimerStarted)
			return;
		if(connected){
			try{session.logout();}catch(Exception e){e.printStackTrace();}
	        connected = false;
		}
	}
	
	public boolean isConnected() {
		return connected;
	}
	
	public Session getSession() {
		return session;
	}
	public void setSession(Session session) {
		this.session = session;
	}
	public Connection getConnection() {
		return connection;
	}
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	public ReportEngine getReportEngine() {
		return reportEngine;
	}
	public void setReportEngine(ReportEngine reportEngine) {
		this.reportEngine = reportEngine;
	}
	public BICatalog getCatalog() {
		return catalog;
	}
	public void setCatalog(BICatalog catalog) {
		this.catalog = catalog;
	}

}
