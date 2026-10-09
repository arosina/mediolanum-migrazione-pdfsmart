package com.atosorigin.wfem.xmlservicelogger;

import java.math.BigDecimal;

import javax.ejb.EJBException;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import com.atosorigin.wfem.backend.ManagerObject;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.DAOQueryResultModel;
import com.atosorigin.wfem.dao.DAOTableResultModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.dao.exceptions.NoRowsAffected;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;
import com.atosorigin.wfem.util.MailSender;
import com.atosorigin.wfem.util.Tools;
import com.atosorigin.wfem.util.XmlServiceCallData;

/********************************************************************************/
/********************************************************************************/
@Stateless(name = "XmlServiceLoggerManager", mappedName = "XmlServiceLoggerManager")
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class XmlServiceLoggerManagerBean extends ManagerObject implements XmlServiceLoggerManager{
	
	private static final String XML_NAME = "wfem.XmlServiceLogger.XmlServiceLogger";
	private static final String ICW_USER = "0000000ICW";
	private static final String NOME_HOST_SMTP = "mediolanum";
	
	/********************************************************************************/
	/********************************************************************************/
	public boolean isServiceDisabled(ClientSessionContext csc, String serviceName) throws EJBException{
		try{
			DAOObject dao = new DAOObject(csc,XML_NAME);
			XmlServiceLoggerModel m = new XmlServiceLoggerModel();
			m.setServiceName(new StringType(serviceName));
			DAOQueryResultModel qRes = dao.executeQueryAccess("isServiceDisabled",m);
			BooleanType disabled = (BooleanType)qRes.getSingleResult();
			if(disabled != null && disabled.booleanValue())
				return true;
			return false;
			
		}catch(DAOException daoe){
			daoe.printStackTrace();
			return false;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	/********************************************************************************/
	/********************************************************************************/
	public void log(ClientSessionContext csc, XmlServiceCallData callData) throws EJBException{
		try{
			String operation = callData.getOperationName();
			if(operation.length() > 0)
				operation = "@" + operation;
			
			String filledUserCode = ICW_USER;
			if(csc.getUserCode() != null && csc.getUserCode().length() >= 0){
				if(csc.isCliente())
					filledUserCode = "C"+Tools.fillSx(csc.getUserCode().toUpperCase(),'0',11).substring(2);
				else
					filledUserCode = Tools.fillSx(csc.getUserCode().toUpperCase(),'0',10);
			}
			String filledLinked = ICW_USER;
			if(csc.getCurrentLinkedUserCode() != null && csc.getCurrentLinkedUserCode().length() > 0){
				if(csc.isCliente())
					filledLinked = "C"+Tools.fillSx(csc.getCurrentLinkedUserCode().toUpperCase(),'0',11).substring(2);
				else
					filledLinked = Tools.fillSx(csc.getCurrentLinkedUserCode().toUpperCase(),'0',10);
			}
			
			DAOObject dao = new DAOObject(csc,XML_NAME);
			
			XmlServiceLoggerModel m = new XmlServiceLoggerModel();
			m.setServiceName(new StringType(callData.getServiceName()+operation));
			m.setUserCode(new StringType(filledUserCode));
			m.setStatus(new StringType(Integer.toString(callData.getStatus())));

			// Load extra service info
			dao.executeQueryAccess("loadServiceInfo",m);
			if(m.getServiceDescr().isNull()) // If service not configured set the description = name
				m.setServiceDescr(m.getServiceName());

			boolean doTrace = true;
			if(callData.getStatus() >= XmlServiceCallData.STATUS_OK && !callData.isForcedTrace()){
				// If QARC code is configured to be traced --> do not query trace abilitation
				boolean queryTrace = true;
				if(!m.getApplErrCodeToTrace().isNull()){
					if(m.getApplErrCodeToTrace().toString().indexOf(m.getStatus().toString()) >= 0)
						queryTrace = false;
				}
				if(queryTrace){
					if(m.getNumTraceElements().isNull() || m.getNumTraceElements().intValue() <= 0)
						doTrace = false;
				}
			}
			
			m.setInstanceId(new StringType(new Long(System.currentTimeMillis()).toString())); 
			m.setStartTime(new TimestampType(callData.getStartTime())); 	
			m.setEndTime(new TimestampType(callData.getEndTime())); 	
			m.setXmlSend(new StringType(callData.getXmlSend())); 	
			m.setXmlReceive(new StringType(callData.getXmlReceive())); 	
			m.setHttpStatus(new IntegerType(callData.getHttpStatus()));
			m.setMessage(new StringType(callData.getMessage())); 	
			m.setSeverity(new StringType(callData.getSeverity())); 	
			m.setSource(new StringType(callData.getSource())); 		
			if(callData.getStatus() >= XmlServiceCallData.STATUS_OK)
				m.setOtherStatus(new StringType("OK"));
			else
				m.setOtherStatus(new StringType("ES"));
			m.setInAppInfo(new StringType(callData.getInAppinfo())); 	
			m.setOutAppInfo(new StringType(callData.getOutAppinfo())); 	
			m.setLinkedUser(new StringType(filledLinked));
			
			if(callData.getStartTime() != null && callData.getEndTime() != null){
				long start = callData.getStartTime().getTime();
				long end = callData.getEndTime().getTime();
				m.setDuration(new IntegerType(new BigDecimal(""+(end-start))));
			}
			
			if(doTrace)
				dao.executeTableInsertAccess("log",m);
			
			manageMailToOnError(dao,m);
			
		}catch(DAOException daoe){
			daoe.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/*****************************************************************************************/
	private static final long ONE_MINUTE = 3600000;
	/*****************************************************************************************/
	private void manageMailToOnError(DAOObject dao, XmlServiceLoggerModel m){
		try{
			
			String to = null;
			long mailFreq = -1;
			
			if(!m.getMailToOnError().isNull())
				to = m.getMailToOnError().toString();
			
			if(!m.getMailErrFreqMinutes().isNull())
				mailFreq = ((long)m.getMailErrFreqMinutes().intValue()) * ONE_MINUTE;
			
			String subject = "";
			StringBuffer mailBody = new StringBuffer();
			if(m.getOtherStatus().equals("OK")){
				try{
					dao.executeTableDeleteAccess("resetServiceErrors",m);
					TimestampType now = Tools.now();
					subject = "Ripristino servizio QARC: "+m.getServiceName()+" ("+now+")";
					mailBody.append("<table border='1'>");
					mailBody.append("<tr><td>"+now+"</td></tr>");
					mailBody.append("<tr><td>Il servizio "+m.getServiceDescr()+" ["+m.getServiceName()+"] ha ripreso a rispondere correttamente </td></tr>");
					mailBody.append("</table>");
				}catch(NoRowsAffected nra){
					to = null; // Do not send mail
				}
			}else{
				// Send mail only on service err mail frequecy 
				// Read, if exist, err row
				XmlServiceLoggerModel errModel = new XmlServiceLoggerModel();
				errModel.setServiceName(m.getServiceName());
				errModel.setStatus(m.getStatus());
				DAOTableResultModel tRes = dao.executeTableLoadAccess("serviceError",errModel);
				if(tRes.getResult().intValue() > 0){
					long curTime = m.getStartTime().timestampValue().getTime();
					long lastTime = errModel.getStartTime().timestampValue().getTime();
					long duration = curTime - lastTime;
					if(mailFreq > 0 && duration < mailFreq) // Do not send mail
						return;
					dao.executeTableUpdateAccess("serviceError",m);				
				}else{					
					dao.executeTableInsertAccess("serviceError",m);				
				}
				
				TimestampType now = Tools.now();
				subject = "Errore chiamata al servizio QARC: "+m.getServiceName()+" ("+now+")";
				mailBody.append("<table border='1'>");
				mailBody.append("<tr><td colspan='2'>"+now+"</td></tr>");
				mailBody.append("<tr><td colspan='2'><b>Errore nella chiamata al servizio QARC: "+m.getServiceDescr()+" ["+m.getServiceName()+"]</b></td></tr>");
				mailBody.append("<tr><td>HTTP status code</td><td>"+m.getHttpStatus()+"&nbsp;</td></tr>");
				mailBody.append("<tr><td>CODE</td><td>"+m.getStatus()+"&nbsp;</td></tr>");
				mailBody.append("<tr><td>SEVERITY</td><td>"+m.getSeverity()+"&nbsp;</td></tr>");
				mailBody.append("<tr><td>MESSAGE</td><td>"+m.getMessage()+"&nbsp;</td></tr>");
				mailBody.append("<tr><td>SOURCE</td><td>"+m.getSource()+"&nbsp;</td></tr>");
				mailBody.append("</table>");
			}
			
			if(to != null)
				sendMail(to,subject,mailBody.toString());
			
		}catch(DAOException daoe){
			String errMsg = "Eccezione nell'inviare la mail di notifica errori di chiamata ai servizi QARC: "+daoe.toString();
			Exception e = new Exception(errMsg);
			e.printStackTrace();
		}catch(Exception e){
			String errMsg = "Eccezione nell'inviare la mail di notifica errori di chiamata ai servizi QARC: "+e.toString();
			e = new Exception(errMsg);
			e.printStackTrace();
		}
	}
	
	/*****************************************************************************************/
	/*****************************************************************************************/
	private void sendMail(String to, String subject, String body) throws Exception{
		MailSender ms =new MailSender();
		ms.setHost(NOME_HOST_SMTP);
		ms.setFrom("org@mediolanum.it");
		ms.setPersonalFrom("Monitor servizi Inforete");
		ms.setTo(to);
		ms.setSubject(subject);
		ms.setContentType("text/html");
		ms.setBody(body);
		ms.sendMail();		
	}
		
}