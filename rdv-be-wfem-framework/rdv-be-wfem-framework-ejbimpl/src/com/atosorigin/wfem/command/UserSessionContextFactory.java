package com.atosorigin.wfem.command;

/**
 * Insert the type's description here.
 * Creation date: (16/07/2002 17.57.18)
 * @author: Administrator
 */
public interface UserSessionContextFactory extends java.io.Serializable {

/**
 * Insert the method's description here.
 * Creation date: (16/07/2002 17.57.37)
 */
UserSessionContext createUserSessionContext(UserSessionContext initialUserSessionContext) throws Exception;
}
