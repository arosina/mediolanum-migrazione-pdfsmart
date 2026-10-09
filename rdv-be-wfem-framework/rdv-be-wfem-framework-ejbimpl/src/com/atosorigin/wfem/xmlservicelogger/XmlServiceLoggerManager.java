package com.atosorigin.wfem.xmlservicelogger;

import javax.ejb.Remote;

import com.atosorigin.wfem.backend.Manager;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.util.XmlServiceCallData;

/********************************************************************************/
/********************************************************************************/
@Remote
public interface XmlServiceLoggerManager extends Manager{
	public boolean 	isServiceDisabled(ClientSessionContext csc, String serviceName);
	public void 	log(ClientSessionContext csc, XmlServiceCallData callData);
}