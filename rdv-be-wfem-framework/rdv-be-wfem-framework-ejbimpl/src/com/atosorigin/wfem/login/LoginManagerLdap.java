package com.atosorigin.wfem.login;

import com.atosorigin.wfem.command.UserSessionContext;

public interface LoginManagerLdap extends LoginManager{
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
	public int executeChangePwd(UserSessionContext userSessionContext, String password, String newPassword) throws Exception;
}
