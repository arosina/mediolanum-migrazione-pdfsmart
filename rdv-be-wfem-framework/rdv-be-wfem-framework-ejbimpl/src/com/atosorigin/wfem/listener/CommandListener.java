package com.atosorigin.wfem.listener;

import com.atosorigin.wfem.command.UserSessionContext;
import javax.servlet.http.*;
import com.atosorigin.wfem.controller.Command;
import com.atosorigin.wfem.command.CommandException;

/**
 * Insert the type's description here.
 * Creation date: (10/07/2002 14.57.39)
 * @author: Administrator
 */
public interface CommandListener extends java.io.Serializable{
/*
 * @param session HttpSession
 * @param command Command
 */
public void reloadCommands() throws Exception;
/*
 * @param session HttpSession
 * @param command Command
 */
public void traceCommandAccess( 
	UserSessionContext userSessionContext, Command command ) throws Exception;
}
