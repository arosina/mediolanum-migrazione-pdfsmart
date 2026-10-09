package com.atosorigin.wfem.backend;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.util.Tools;

/***********************************************************************************************
 * @author Ricotti Corrado
 ***********************************************************************************************/
public class UserContextDataLoader {
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	@Deprecated
	public static String[] loadLoginNameForUser(String user, String reqUser){
		return loadLoginNameAndRolesForUser(null, reqUser);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static String[] loadLoginNameAndRolesForUser(String reqUser){
		return loadLoginNameAndRolesForUser(null, reqUser);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static String[] loadLoginNameAndRolesForUser(UserSessionContext usc, String reqUser){
		
    	// Recupero il loginName e il tipo utente solo se user non numerico, ossia se utente non FB
		String loginname = "";
		String user = reqUser;
		String userType = ClientSessionContext.USER_TYPE_RETE;
		
    	boolean loadLoginName = false;
    	try{
    		Long.parseLong(reqUser);
    	}catch(NumberFormatException nfe){
    		loadLoginName = true;
    	}
    	if(loadLoginName){
    		userType = ClientSessionContext.USER_TYPE_SEDE;
    		String dbLogniName = null;
    		String dbUserCode = null;
    		DAOObject dao = null; PreparedStatement ps = null; ResultSet rs = null;
    		try{
    			dao = new DAOObject(new ClientSessionContext(), "wfem.MainDBConnection.MainDBConnection");
    			dao.openConnection();
    			ps = dao.getConnection().prepareStatement("SELECT 	u.X_LOGIN_USER_ID "+
    													  ",		u.C_USER "+
    													  ",		(select case when count(*) > 0 then '"+ClientSessionContext.USER_TYPE_ASSISTENTEFB+"' else '"+ClientSessionContext.USER_TYPE_SEDE+"' end from PRGM_USERS_ROLES_COMPLETE r where r.C_USER = u.C_USER and r.P_ROLE="+ClientSessionContext.RUOLO_ASSISTENTEFB+") as userType "+
    													  "FROM 	PRGM_USERS u "+
    													  "WHERE 	upper(u.C_USER) = right('0000000000'+'"+reqUser.toUpperCase()+"',10) or upper(u.X_LOGIN_USER_ID) = '"+reqUser.toUpperCase()+"'");
    			ps.execute();
    			rs = ps.getResultSet();
    			if(rs.next()){
    				dbLogniName = rs.getString(1);
    				dbUserCode = rs.getString(2);
    				userType = rs.getString(3);
    			}
    		}catch(SQLException sqle){
    		}catch(DAOException daoe){
    		}finally{
    			if(rs != null){ try{ rs.close(); rs=null; }catch(SQLException sqle){sqle.printStackTrace();} }
    			if(ps != null){ try{ ps.close(); ps=null; }catch(SQLException sqle){sqle.printStackTrace();} }
    			if(dao != null) dao.closeConnection();
    		}
			if(dbLogniName == null)
				dbLogniName = "unknown";
			loginname = dbLogniName.toString();
			
			user = Tools.fillSx(reqUser, '0', 10);
    		if(dbUserCode != null && !dbUserCode.equalsIgnoreCase(user))
				user = Tools.fillSx(dbUserCode, '0', 10);
    	}else{
    		if(reqUser.length() == 11){
        		user = reqUser;
        		loginname = user.toString();
        		userType = ClientSessionContext.USER_TYPE_CLIENTE;
    		}else{
        		user = Tools.fillSx(reqUser, '0', 10);
        		loginname = user.toString();
    		}
    	}
    	if(usc != null && !userType.equals(ClientSessionContext.USER_TYPE_CLIENTE))
    		usc.setUserRoles(loadUserRoles(user));
    	return new String[]{loginname, userType, Tools.fillSx(user, '0', 10).toUpperCase()};
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static String[] loadUserAndRolesForLoginName(UserSessionContext usc,String reqLoginname) throws Exception{
		
    	// Recupero il cUser loginName e il tipo utente solo se user non numerico, ossia se utente non FB
		String loginname = reqLoginname;
		String user = "";
		String userType = ClientSessionContext.USER_TYPE_RETE;

    	boolean loadCuser = false;
    	try{
    		Long.parseLong(loginname);
    	}catch(NumberFormatException nfe){
    		loadCuser = true;
    	}
    	if(loadCuser){
    		userType = ClientSessionContext.USER_TYPE_SEDE;
    		String dbUserCode = null;
    		DAOObject dao = null; PreparedStatement ps = null; ResultSet rs = null;
    		try{
    			dao = new DAOObject(new ClientSessionContext(), "wfem.MainDBConnection.MainDBConnection");
    			dao.openConnection();
    			ps = dao.getConnection().prepareStatement("SELECT 	u.C_USER "+
    													  ",		(select case when count(*) > 0 then '"+ClientSessionContext.USER_TYPE_ASSISTENTEFB+"' else '"+ClientSessionContext.USER_TYPE_SEDE+"' end from PRGM_USERS_ROLES_COMPLETE r where r.C_USER = u.C_USER and r.P_ROLE="+ClientSessionContext.RUOLO_ASSISTENTEFB+") as userType "+
    													  "FROM 	PRGM_USERS u "+
    													  "WHERE 	upper(u.X_LOGIN_USER_ID) = '"+loginname.toUpperCase()+"'");
    			ps.execute();
    			rs = ps.getResultSet();
    			if(rs.next()){
    				dbUserCode = rs.getString(1);
    				userType =  rs.getString(2);
    			}
    		}catch(SQLException sqle){
    		}catch(DAOException daoe){
    		}finally{
    			if(rs != null){ try{ rs.close(); rs=null; }catch(SQLException sqle){sqle.printStackTrace();} }
    			if(ps != null){ try{ ps.close(); ps=null; }catch(SQLException sqle){sqle.printStackTrace();} }
    			if(dao != null) dao.closeConnection();
    		}
			if(dbUserCode == null || dbUserCode.length() == 0){
				throw new Exception("User with X_LOGIN_USER_ID=["+loginname+"] not found or with C_USER null");
			}
			user = Tools.fillSx(dbUserCode, '0', 10);
    	}else{
    		if(loginname.length() == 11){
	    		user = loginname.toString();
        		userType = ClientSessionContext.USER_TYPE_CLIENTE;
    		}else{
	    		loginname = Tools.fillSx(loginname, '0', 10);
	    		user = loginname.toString();
    		}
    	}
    	if(usc != null && !userType.equals(ClientSessionContext.USER_TYPE_CLIENTE))
    		usc.setUserRoles(loadUserRoles(user));
    	return new String[]{loginname, userType, Tools.fillSx(user, '0', 10).toUpperCase()};
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private static String loadUserRoles(String cuser){
		String roles = "";
		DAOObject dao = null; PreparedStatement ps = null; ResultSet rs = null;
		try{
			dao = new DAOObject(new ClientSessionContext(), "wfem.MainDBConnection.MainDBConnection");
			dao.openConnection();
			ps = dao.getConnection().prepareStatement(	"SELECT R.X_ROLE "+ 
														"FROM PRGM_USERS_ROLES UR, PRGM_ROLES R "+ 
														"WHERE R.P_ROLE = UR.P_ROLE "+ 
														"AND (UR.F_PRIMARY = 'S' OR R.F_EXPORT_OID = 'S') "+
														"AND upper(UR.C_USER) = '"+Tools.fillSx(cuser, '0', 10).toUpperCase()+"'");
			ps.execute();
			rs = ps.getResultSet();
			while(rs.next()){
				roles += rs.getString(1).replaceAll("\\\"", "'")+"|";
			}
			if(roles.length() > 0)
				roles = roles.substring(0,roles.length()-1);
		}catch(SQLException sqle){
		}catch(DAOException daoe){
		}finally{
			if(rs != null){ try{ rs.close(); rs=null; }catch(SQLException sqle){sqle.printStackTrace();} }
			if(ps != null){ try{ ps.close(); ps=null; }catch(SQLException sqle){sqle.printStackTrace();} }
			if(dao != null) dao.closeConnection();
		}
		return roles;
	}
}
