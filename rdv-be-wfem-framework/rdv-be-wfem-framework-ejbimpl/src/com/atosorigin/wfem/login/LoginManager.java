package com.atosorigin.wfem.login;

import com.atosorigin.wfem.command.UserSessionContext;
/**
 * Insert the type's description here.
 * Creation date: (10/07/2002 14.57.39)
 * @author: Administrator
 */
public interface LoginManager extends java.io.Serializable{
	public static final int LOGIN_CORRECT       = 0;
	public static final int LOGIN_FAILED        = 1;
	public static final int LOGIN_PWD_EXPIRED   = 2;
	public static final int LOGIN_NOT_ENABLED   = 3;
	public static final int LOGIN_MINOR_FAILURE_INDEX = 10;

	public static final int CHANGE_PWD_CORRECT  = 0;
	public static final int CHANGE_PWD_FAILED   = 1;
	public static final int CHANGE_PWD_MINOR_FAILURE_INDEX = 10;
/*
 * Result must be:
 *
 *		CHANGE_PWD_CORRECT or
 *		CHANGE_PWD_FAILED  or
 *
 * @param channel String
 * @param user String
 * @param password String
 * @param newPassword String
 * @return int Result
 */
public int executeChangePwd(String channel, String user, String password, String newPassword) throws Exception;
/*
 * Result must be:
 *
 *		LOGIN_CORRECT or
 *		LOGIN_FAILED  or
 *		PWD_EXPIRED
 *
 * @param channel String
 * @param user String
 * @param password String
 * @return int Result
 */
public int executeLogin(UserSessionContext userSessionContext,String password) throws Exception;
/*
 * Result must be:
 *
 *		null if there are no info
 *		a LoginFailureInfo instance 
 * @return LoginFailureInfo
 */
public LoginFailureInfo getLoginFailureInfo();
}