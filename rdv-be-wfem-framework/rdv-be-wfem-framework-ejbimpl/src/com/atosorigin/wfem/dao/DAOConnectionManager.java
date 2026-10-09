package com.atosorigin.wfem.dao;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.naming.InitialContext;
import javax.naming.NameNotFoundException;
import javax.sql.DataSource;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.controller.Configuration;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.loggers.DAOLogger;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class DAOConnectionManager
{
	private final String thisClassName = DAOConnectionManager.class.getName();
	
	private static DAOConnectionManager singleton = null;
	
	private static DAOLogger LOG = DAOLogger.getInstance();

	private Hashtable<String,DataSource> dataSources = null;
	
	private Configuration configuration = null;
	private String jdbcDriver = null;
	
	private Hashtable<String,String> dataSourceReferenceNameForConnection = null;
	private Hashtable<String,String> dataSourceNameForConnection = null;

	/********************************************************************************
	/********************************************************************************/
	private DAOConnectionManager() {
		this.configuration = Configuration.getInstance();
		this.jdbcDriver = this.configuration.getJdbcDriver();
		this.dataSources = new Hashtable<String,DataSource>();
		this.dataSourceReferenceNameForConnection = new Hashtable<String,String>();
		this.dataSourceNameForConnection = new Hashtable<String,String>();
	}
	
	/********************************************************************************
	/********************************************************************************/
	public static DAOConnectionManager getInstance() {
	    if (singleton == null) {
	        synchronized (DAOConnectionManager.class) {
	            if (singleton == null) {
	                singleton = new DAOConnectionManager();
	            }
	        }
	    }
		return singleton;
	}
	
	/********************************************************************************
	/********************************************************************************/
	protected void closeConnection(Connection conn) {
	
		try{
			if(conn == null)
				return;
			
			String thisMethod = ".closeConnection: ";
			
			resetConnectionPar(conn);
			
			String connId = ""+conn.hashCode();
			
			try {
				if(conn != null && !conn.isClosed()){
					LOG.debug("Closing DataSource connection "+conn);
					conn.close();
				}
			}catch(Exception e){
				String errorMsg = thisClassName+thisMethod+" error in closing connection "+e;
				LOG.error(new Exception(errorMsg));
			}

			logDbRel(connId);

		}catch(Exception e){
			
			String errorMsg = thisClassName+".closeConnection: exception in closing connection "+e;
			LOG.error(new Exception(errorMsg));
			
		}
	}
	
	/********************************************************************************
	/********************************************************************************/
	protected Connection getConnection(ClientSessionContext clientContext,
										String dataSourceReferenceName) throws DAOException{
	
		try{
			
			String realDataSource = clientContext.getDataSourceName(dataSourceReferenceName);
			LOG.debug("DAOConnectionManager.getConnection: real datasource name for reference name ["+dataSourceReferenceName+"] is: ["+realDataSource+"]");
			return getRealConnection(clientContext, realDataSource, dataSourceReferenceName);
			
		}catch(Exception e){
			String errorMsg = thisClassName+".getConnection: Exception on getting connection on ClientSessionContext "+clientContext+": " + e;
			DAOException daoe = new DAOException(errorMsg);
			LOG.error(daoe);
			throw daoe;
		}
	}
	
	/********************************************************************************
	/********************************************************************************/
	protected Connection getConnection(ClientSessionContext clientContext,
										String dataSourceReferenceName,
										String country, String channel) throws DAOException{
	
		try{
			
			if(country == null)
				country = clientContext.getCountryCode();
			if(country != null)
				country = country.toUpperCase();
			if(channel == null)
				channel = clientContext.getChannelCode();
			if(channel != null)
				channel = channel.toUpperCase();
			String realDataSource = clientContext.getDataSourceName(dataSourceReferenceName,country,channel);
			LOG.debug("DAOConnectionManager.getConnection(country,channel): real datasource name for reference name ["+dataSourceReferenceName+"], country ["+country+"] and channel ["+channel+"] is: ["+realDataSource+"]");
			return getRealConnection(clientContext, realDataSource, dataSourceReferenceName);
			
		}catch(Exception e){
			String errorMsg = thisClassName+".getConnection: Exception on getting connection on ClientSessionContext "+clientContext+": " + e;
			DAOException daoe = new DAOException(errorMsg);
			LOG.error(daoe);
			throw daoe;
		}
	}
	
	/********************************************************************************
	/********************************************************************************/
	private java.sql.Connection getRealConnection(ClientSessionContext clientContext, String dataSourceName, String dataSourceReferenceName) throws SQLException{
		
		String thisMethod = ".getConnection(dataSourceName): ";
		
		try	{
			
			if(jdbcDriver != null){ // Gestione configurazione connessioni in ambiente senza datasource
				String jdbcDrv = configuration.getJdbcDbDriver(dataSourceName);
				if(jdbcDrv == null)
					jdbcDrv = jdbcDriver;
				
				LOG.debug("Loading Jdbc Driver ["+jdbcDrv+"]");
				Class.forName(jdbcDrv);

				String jdbcUrl = configuration.getJdbcUrl(dataSourceName);
				String jdbcDbUser = configuration.getJdbcDbUser(dataSourceName);
				String jdbcDbPassword = configuration.getJdbcDbPassword(dataSourceName);
				
				Connection conn = null;
				if(jdbcDbUser != null){
					LOG.debug("Getting new connection on datasource ["+dataSourceName+"], jdbcUrl ["+jdbcUrl+"] with user = ["+jdbcDbUser+"] and password = ["+jdbcDbPassword+"]");
					conn = DriverManager.getConnection(jdbcUrl, jdbcDbUser, jdbcDbPassword);
				}else{
					LOG.debug("Getting new connection on datasource ["+dataSourceName+"], jdbcUrl ["+jdbcUrl+"]");
					conn = DriverManager.getConnection(jdbcUrl);
				}
				setConnectionPar(clientContext, dataSourceName, dataSourceReferenceName, conn);
				logDbGet(conn, dataSourceName);
				LOG.debug("New connection acquired is: "+conn);
			    return conn;
			}			
	
			DataSource dataSource = dataSources.get(dataSourceName);
			if(dataSource == null){ // Datasource not in cache. Try to load now				
				try{
					initDataSource(null, dataSourceName);
				}catch(Exception e){
					String errorMsg = thisClassName+thisMethod+"Exception on getting connection on new DataSource "+dataSourceName+": " + e;
					LOG.error(new Exception(errorMsg));
					throw new SQLException(errorMsg);
				}
				dataSource = dataSources.get(dataSourceName);
			}
	
			LOG.debug("Getting new connection on DataSource "+dataSourceName);
			Connection conn = dataSource.getConnection();
			setConnectionPar(clientContext, dataSourceName, dataSourceReferenceName, conn);
			logDbGet(conn, dataSourceName);
			LOG.debug("New connection acquired is: "+conn);
		    return conn;
		    
		}catch(Exception e){
			String errorMsg = thisClassName+thisMethod+"Exception on getting connection on DataSource "+dataSourceName+": " + e;
			LOG.error(new Exception(errorMsg));
			throw new SQLException(errorMsg);
		}
	}
	
	/********************************************************************************
	/********************************************************************************/
	private void initDataSource(InitialContext context, String dataSourceName) throws Exception 
	{
		String thisMethod = ".initDataSource: ";
		
		if(context == null)
			context = new InitialContext();
			
		try{
			
			DataSource connPool = null;
			String lookName = "jdbc/Wfem/"+dataSourceName;
			try {
				connPool = (DataSource)context.lookup(lookName);
			}catch(NameNotFoundException nnfe) {
				connPool = (DataSource)context.lookup("java:comp/env/"+lookName);				
			}
			LOG.info("Connection pool initialized. Save in cache");
			dataSources.put(dataSourceName,connPool);
			
		}catch(Exception e){
			String errorMsg = thisClassName+thisMethod+"Exception on initializing DataSource " + dataSourceName + ": " + e;
			Exception ne = new Exception(errorMsg);
			LOG.error(ne);
			throw ne;
		}
		
	}
	
	/********************************************************************************
	/********************************************************************************/
	private void logDbGet(Connection conn, String dataSourceName){

		if(!configuration.isDbConnectionTraceEnabled())
			return;
			
		try{
			String connId = ""+conn.hashCode();
			this.dataSourceNameForConnection.put(connId,dataSourceName);			
			String traceDir = configuration.getDbConnectionTraceDir();
			File f = new File(traceDir+dataSourceName+"_"+connId+"_GETTED");
			f.createNewFile();
			
		}catch(Throwable t){}
	}
		
	/********************************************************************************
	/********************************************************************************/
	private void logDbRel(String connId){
		
		if(!configuration.isDbConnectionTraceEnabled())
			return;
			
		try{
			String dataSourceName = this.dataSourceNameForConnection.remove(connId);			
			String traceDir = configuration.getDbConnectionTraceDir();
			File f = new File(traceDir+dataSourceName+"_"+connId+"_GETTED");
			if(f.exists())
				f.delete();
		}catch(Throwable t){}
	}
	
	/********************************************************************************
	/********************************************************************************/
	private void setConnectionPar(ClientSessionContext clientContext,
								   String dataSourceName, String dataSourceReferenceName,
	                               Connection dbConn) {
		
		
		if(dbConn.getClass().getName().toLowerCase().indexOf("sybase") < 0){
			ArrayList<String> sybDs = Configuration.getInstance().getSybaseDataSourcesNamesAsArray();
			if(sybDs == null || !sybDs.contains(dataSourceReferenceName.toUpperCase()))
				return;
		}

		this.dataSourceReferenceNameForConnection.put(""+dbConn.hashCode(), dataSourceReferenceName);

		if(dataSourceReferenceName.toUpperCase().indexOf("IQ") >= 0)
			return;
		
		Statement st = null;	
		try{
		
	        st = dbConn.createStatement();
	        
	        st.execute("set clienthostname '"+com.atosorigin.wfem.util.Tools.now()+"'");

	        String userCode = clientContext.getUserCode();
	        if(userCode != null && !userCode.equals(""))
		        st.execute("set clientname '"+userCode+"'");
		    else
		        st.execute("set clientname 'DSREF"+dataSourceName+"'");

		    String applCode = clientContext.getApplCode();
		    if(applCode != null && !applCode.equals(""))
		        st.execute("set clientapplname '"+applCode+"'");
		    else
		        st.execute("set clientapplname 'DSREAL"+dataSourceName+"'");
	        
		}catch(SQLException sqle){
			String warningMsg = thisClassName+".setConnectionPar: Error on setting parameters in connection";
			LOG.warning(warningMsg);
		}finally{
			try{
				if(st != null){st.close();st = null;}
			}catch(SQLException sqle){
				String warningMsg = thisClassName+".setConnectionPar: Error on setting parameters in connection";
				LOG.warning(warningMsg);
			}
		}
	}
	
	/********************************************************************************
	/********************************************************************************/
	private void resetConnectionPar(Connection dbConn) {
		
		String dataSourceReferenceName = this.dataSourceReferenceNameForConnection.remove(""+dbConn.hashCode());
		if(dataSourceReferenceName == null)
			return;
		
		if(dataSourceReferenceName.toUpperCase().indexOf("IQ") >= 0)
			return;
		
		Statement st = null;	
		try{
		
	        st = dbConn.createStatement();
	        st.execute("set clientname 'unused'");
	        st.execute("set clientapplname 'unused'");
        	st.execute("sp__orphansybquit");
	        
		}catch(SQLException sqle){
			String warningMsg = thisClassName+".resetConnectionPar: SQLException resetting parameters in connection: "+sqle;
			LOG.warning(warningMsg);
		}catch(Exception e){
			String warningMsg = thisClassName+".resetConnectionPar: Exception resetting parameters in connection: "+e;
			LOG.warning(warningMsg);
		}finally{
			try{
				if(st != null){st.close();st = null;}
			}catch(SQLException sqle){
				String warningMsg = thisClassName+".resetConnectionPar: SQLException closing statement: "+sqle;
				LOG.warning(warningMsg);
			}
		}
	}

}
