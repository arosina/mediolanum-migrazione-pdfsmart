package com.atosorigin.wfem.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import com.atosorigin.wfem.controller.ControllerServlet.CommandsStack;
import com.atosorigin.wfem.loggers.SubsessionsInvalidatorLogger;

/********************************************************************/
/********************************************************************/
public class SubsessionsInvalidator implements HttpSessionListener, Runnable {

	private static SubsessionsInvalidatorLogger LOG = SubsessionsInvalidatorLogger.getInstance();
	private static int SLEEP_TIME_IN_KEEP_ALIVE_MULTIPLIER = 3;
	
	private List<HttpSession> sessions = null;
	private Thread refresherThread = null;
	
	/********************************************************************/
	/********************************************************************/
	public void sessionCreated(HttpSessionEvent sessionEvent) {
		if(Configuration.getInstance().getKeepaliveTimerMinutes() <= 0)
			return;

		if(sessions == null)
			sessions = Collections.synchronizedList(new ArrayList<HttpSession>());
		if(sessions.size() == 0){
			refresherThread = new Thread(this);
			refresherThread.setDaemon(true);
			refresherThread.start();
		}
		HttpSession session = sessionEvent.getSession();
		sessions.add(session);
		LOG.debug("HTTPSession ["+session.hashCode()+"] created");
	}

	/********************************************************************/
	/********************************************************************/
	public void sessionDestroyed(HttpSessionEvent sessionEvent) {
		if(sessions == null)
			return;
		HttpSession session = sessionEvent.getSession();
		sessions.remove(session);
		LOG.debug("HTTPSession ["+session.hashCode()+"] destroyed");
		if(sessions.size() == 0){
			refresherThread.interrupt();
		}
	}	
	
	/********************************************************************/
	/********************************************************************/
	public void run() {
		LOG.debug("Subsessions invalidator started");
		while(true){
			try{
				Thread.currentThread().sleep(Configuration.getInstance().getKeepAliveTimerInMilliseconds());
				synchronized(sessions) {
					Iterator<HttpSession> sessionIterator = sessions.iterator();
					while(sessionIterator.hasNext()){
						manageSession(sessionIterator.next());
					}
				}
			}catch(InterruptedException ie){
				break;
			}catch(Throwable t){
				LOG.error(t);
				break;
			}
		}
		LOG.debug("Subsessions invalidator ended");
	}
	
	/********************************************************************/
	/********************************************************************/
	private void manageSession(HttpSession session){
		try{
			Enumeration sessionPars = session.getAttributeNames();
			while(sessionPars.hasMoreElements()){
				String sessionParName = (String)sessionPars.nextElement();
				if(!sessionParName.startsWith("WfemCommandStackContext_"))
					continue;
				if(sessionParName.equals("WfemCommandStackContext_0") || sessionParName.equals("WfemCommandStackContext_1"))
					continue;
				CommandsStack stack = (CommandsStack)session.getAttribute(sessionParName);
				if(stack == null)
					continue;
				
				if(!stack.isKeepAliveIsActive())
					continue;
				
				LOG.debug("Subsession ["+sessionParName+"] check on session ["+session.hashCode()+"] - sleepTime ["+stack.getSleepTimeMinutes()+"]");
				if(stack.getSleepTimeMinutes() > Configuration.getInstance().getKeepaliveTimerMinutes() * SLEEP_TIME_IN_KEEP_ALIVE_MULTIPLIER){
					session.removeAttribute(sessionParName);
					LOG.debug("Subsession ["+sessionParName+"] removed from session ["+session.hashCode()+"]");
				}else{
					stack.addSleepTimeMinutes(Configuration.getInstance().getKeepaliveTimerMinutes());
				}
				
			}
		}catch(Throwable t){
			LOG.error(t);
		}
	}

}
