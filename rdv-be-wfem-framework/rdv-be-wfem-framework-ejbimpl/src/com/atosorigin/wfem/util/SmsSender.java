package com.atosorigin.wfem.util;

import java.io.InputStream;
import java.util.Properties;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.controller.Configuration;
import com.atosorigin.wfem.dao.DAOOSBResultModel;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.loggers.MailLogger;
import com.atosorigin.wfem.types.StringType;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class SmsSender {

	private static final String SMS_CONFIGURATION_FILE = Configuration.CONFIGURATION_FILES_ROOT+"/wfemSms.properties";
	private static final String ENABLED_NUMBERS = "enabledNumbers";
	
	private static MailLogger LOG = MailLogger.getInstance();

    private Properties conf = new Properties();
    
	/********************************************************************************/
	/********************************************************************************/
	public SmsSender(){
		
    	try{
	    	InputStream inputStream = this.getClass().getResourceAsStream(SMS_CONFIGURATION_FILE);
	    	if(inputStream == null){
	    		String errorMsg = "Wfem SMS configuration file ["+SMS_CONFIGURATION_FILE+"] not found. SMS utility can't correctly work";
	    		LOG.info(errorMsg);
	    		return;
	    	}
	    	conf.load(inputStream);
	    	inputStream.close();
    	}catch(Exception e){
    		String errorMsg = "Exception initializing mail utility: "+e;
    		e = new Exception(errorMsg);
    		LOG.error(e);		
    	}
				
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public String sendSMS(String number, String message) throws Exception{
		try{
			LOG.debug("Sending SMS to ["+number+"] message ["+message+"]");
			
			if(!isNumberEnabled(number)){
				LOG.debug("Number ["+number+"] not enabled. Do nothing");
				return "";
			}
			
			if(number == null || number.length() == 0){
				LOG.debug("Number ["+number+"] null or  empty. Do nothing");
				return "";
			}

			if(message == null || message.length() == 0){
				LOG.debug("Message ["+message+"] null or  empty. Do nothing");
				return "";
			}
			
			String environment = Configuration.getInstance().getEnvironment();
			if(environment.length()==0) {
				LOG.error(new Exception("ENVIRONMENT gobal configuration is not setted. SMS utility can't correctly work"));
				return "";
			}
			
			return callSmsSrv(number, message, environment);
						
        }catch(Exception e){    
            String errorMsg = getClass().getName()+".sendSms: Exception sending sms to ["+number+"]: "+e;
            e = new Exception(errorMsg);            
            LOG.error(e);
            throw e;
        }
	}

    /****************************************************************/
    /****************************************************************/
	private String callSmsSrv(String number, String message, String environment) {
		try {
			SmsModel smsModel = new SmsModel();
			smsModel.setNumber(new StringType(number.replace("_","")));
			smsModel.setMessage(new StringType(message));
			smsModel.setEnvironment(new StringType(environment.toUpperCase()));
			
			ClientSessionContext csc = new ClientSessionContext();
			csc.setCountryCode("ITA");
			csc.setChannelCode("P");
			DAOOSBResultModel wsRes = new DAOObject(csc, "wfem.SmsSender.SmsSender").executeOSBAccess("sendSms", smsModel);
			if(wsRes.getWsCallData().getStatus() != OSBCallData.STATUS_OK)
				return "";			
			return smsModel.getSmsMtCode().toString();
			
	    }catch(DAOException daoe){
	        String errorMsg = getClass().getName()+".sendSms: DAOException calling sms service for ["+number+"]: "+daoe;
	        Exception e = new Exception(errorMsg);            
	        LOG.error(e);
	        return "";
        }catch(Exception e){    
            String errorMsg = getClass().getName()+".sendSms: Exception calling sms service for ["+number+"]: "+e;
            e = new Exception(errorMsg);            
            LOG.error(e);
	        return "";
	    }		
	}
	
    /****************************************************************/
    /****************************************************************/
    private boolean isNumberEnabled(String number){
    	String enabledNumbers = conf.getProperty(ENABLED_NUMBERS);
    	if(enabledNumbers == null || enabledNumbers.length() == 0 || enabledNumbers.equals("*"))
    		return true;
    	return (enabledNumbers.indexOf(number) >= 0 ? true : false);
    }
    
}
