package com.atosorigin.wfem.util;

import java.io.InputStream;
import java.security.Security;
import java.util.Properties;
import java.util.Vector;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import com.atosorigin.wfem.controller.Configuration;
import com.atosorigin.wfem.loggers.MailLogger;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class MailSender {
    
	private static final String MAIL_CONFIGURATION_FILE = Configuration.CONFIGURATION_FILES_ROOT+"/wfemMail.properties";
	private static final String ENABLED_ADDRESSES = "enabledAddresses";

	private static MailLogger LOG = MailLogger.getInstance();

    private String host = "";
    private String sslUser = "";
    private String sslPassword = "";
    
    private String from = "";
    private String personalFrom = "";
    private String to = "";
    private String cc = "";
    private String bcc = "";
    private String subject = "";
    private String body = "";
    private String contentType = "";
    private String[] attachments;
    private int numMailSended = 0;
    
    private Properties conf = new Properties();

    private boolean connected = false;
	private RealHost realHost = null;
    private Session sess = null;
    private Transport transport = null;
    
    class RealHost{
    	public String host ="";
    	public String port = "25";
    	public boolean ssl;
    }
    
    /****************************************************************/
    /****************************************************************/
    public MailSender(){
    	try{
	    	InputStream inputStream = this.getClass().getResourceAsStream(MAIL_CONFIGURATION_FILE);
	    	if(inputStream == null){
	    		String errorMsg = "Wfem mail configuration file ["+MAIL_CONFIGURATION_FILE+"] not found. Mail utility can't correctly work";
	    		LOG.info(errorMsg);
	    		return;
	    	}
	    	conf.load(inputStream);
	    	inputStream.close();
    	}catch(Exception e){
    		String errorMsg = "Exception initializing mail utility: "+e;
    		e = new Exception(errorMsg);
    		e.printStackTrace();    		
    	}
    }
    
    /****************************************************************/
    /****************************************************************/
    public void initMailData(){
        from = "";
        personalFrom = "";
        to = "";
        cc = "";
        bcc = "";
        subject = "";
        body = "";
        contentType = "";
        attachments = null;
        numMailSended = 0;
    }
    
    /****************************************************************/
    /****************************************************************/
    public void connect(String host) throws Exception {
    	setHost(host);
    	connect();
    }
    
    /****************************************************************/
    /****************************************************************/
    public void connect() throws Exception {
        try{
        	
        	if(host == null){
        		String errorMsg = "Host must be setted.";
        		throw new Exception(errorMsg);
        	}
        	
        	realHost = getRealHost();
        	if(realHost == null){
        		String errorMsg = "Host ["+host+"] not found in ["+MAIL_CONFIGURATION_FILE+"] configuration file.";
        		throw new Exception(errorMsg);
        	}
    	
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", realHost.host);
    
            if(realHost.ssl){
                
                props.put("mail.smtp.port", realHost.port);
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.socketFactory.port", realHost.port);
                props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
                props.put("mail.smtp.socketFactory.fallback", "false");
                props.put("mail.smtp.sendpartial","true");
                MailAuthenticator mailAuth = new MailAuthenticator();
                mailAuth.setUser(getSslUser());
                mailAuth.setPassword(getSslPassword());
                sess = Session.getDefaultInstance(props, mailAuth);
            }else{
                props.put("mail.smtp.port", realHost.port);
                props.put("mail.smtp.sendpartial","true");
                sess = Session.getInstance(props);
            }
            
            transport = sess.getTransport();
            transport.connect();
    
            connected = true;
            
        }catch(Exception e){
            
            String errorMsg = getClass().getName()+".connect: Exception connecting to mail server ["+host+"]: "+e;
            e = new Exception(errorMsg);            
            LOG.error(e);
            throw e;
            
        }
    }
    
    /****************************************************************/
    /****************************************************************/
    public void disconnect(){
    	if(isConnected()){
    		try{ 
    			transport.close(); 
    		}catch(Exception e){
                String errorMsg = getClass().getName()+".disconnect: Exception disconnecting to mail server ["+host+"]: "+e;
                e = new Exception(errorMsg);            
                LOG.error(e);
    		}
    	}
    	realHost = null;
    	sess = null;
    	transport = null;
    	connected = false;
    }
    
    /****************************************************************/
    /****************************************************************/
    private RealHost getRealHost(){
    	try{
    		RealHost realHost = new RealHost();
	    	LOG.debug("Retrieving real host from logical one ["+host+"]");
	    	String confRow = conf.getProperty(host);
	    	int idx = confRow.indexOf(",");
	    	if(idx >= 0){
	    		realHost.host = confRow.substring(0,idx);
		    	LOG.debug("   Real host name ["+realHost.host+"]");
	    		confRow = confRow.substring(idx+1);
	    	}
	    	idx = confRow.indexOf(",");
	    	if(idx >= 0){
	    		realHost.port = confRow.substring(0,idx);
		    	LOG.debug("   Port number ["+realHost.port+"]");
	    		confRow = confRow.substring(idx+1);
	    	}
	    	
    		realHost.ssl = Boolean.valueOf(confRow).booleanValue();
	    	LOG.debug("   SSL mail ["+realHost.ssl+"]");
	    	return realHost;
    	}catch(Exception e){
    		return null;
    	}
    	
    }
    
    /****************************************************************/
    /****************************************************************/
    private boolean isAddressEnabled(String address){
    	String enabledAddresses = conf.getProperty(ENABLED_ADDRESSES);
    	if(enabledAddresses == null || enabledAddresses.length() == 0 || enabledAddresses.equals("*"))
    		return true;
    	return (enabledAddresses.indexOf(address) >= 0 ? true : false);
    }
    
    /****************************************************************/
    /****************************************************************/
    private InternetAddress[] getAddresses(String addresses){
    	addresses = addresses.replaceAll("\\;", ",");
    	Vector resultAddress = new Vector();
    	if(addresses == null || addresses.length() == 0)
        	return (InternetAddress[])resultAddress.toArray(new InternetAddress[0]);
    		
    	try{
    		InternetAddress[] iaddr = InternetAddress.parse(addresses,false);
    		for(int i=0;i<iaddr.length;i++){
    			if(isAddressEnabled(iaddr[i].getAddress()))
    				resultAddress.add(iaddr[i]);
    		}
    	}catch(Exception e){
    		String errMsg = "Exception on address ["+addresses+"]: "+e;
    		e = new Exception(errMsg);
    		LOG.error(e);
    	}
    	return (InternetAddress[])resultAddress.toArray(new InternetAddress[0]);
    }
    
    /****************************************************************/
    /****************************************************************/
    public void sendMail() throws Exception {
        
    	boolean mustDisconnect = false;

        try{
        	
        	if(from == null || from.length() == 0)
        		return;

            MimeMessage mess = new MimeMessage(sess);
            mess.setSubject(subject);
            
            InternetAddress[] toAddr = getAddresses(to);
            InternetAddress[] ccAddr = getAddresses(cc);
            InternetAddress[] bccAddr = getAddresses(bcc);
            if(toAddr.length == 0 && ccAddr.length == 0 && bccAddr.length == 0){
            	return;
            }
            
        	if(!connected){
        		mustDisconnect = true;
        		connect();
        	}
        	
            if(toAddr.length > 0)
            	mess.addRecipients(Message.RecipientType.TO,toAddr);            
            if(ccAddr.length > 0)
            	mess.addRecipients(Message.RecipientType.CC,ccAddr);
            if(bccAddr.length > 0)
            	mess.addRecipients(Message.RecipientType.BCC,bccAddr);

            if(from != null){
                InternetAddress[] fromAddrs = new InternetAddress[1];
                if(personalFrom != null && personalFrom.length() > 0)
                    fromAddrs[0] = new InternetAddress(from,personalFrom);
                else
                    fromAddrs[0] = new InternetAddress(from);
                mess.addFrom(fromAddrs);
            }
    
            Multipart multipart = new MimeMultipart();
            if(body != null){
                BodyPart messageBodyPart = new MimeBodyPart();
                String mailContentType = "text/plain";
                if(contentType != null && !contentType.equals(""))
                	mailContentType = contentType;
                messageBodyPart.setDataHandler(new DataHandler(body.toString(),mailContentType));
                multipart.addBodyPart(messageBodyPart);
            }
    
            if(attachments != null){
                for (int i = 0; i < attachments.length; i++){
                	BodyPart messageBodyPart = new MimeBodyPart();
                    DataSource source = new FileDataSource(attachments[i]);
                    messageBodyPart.setDataHandler(new DataHandler(source));
                    messageBodyPart.setFileName(source.getName());
                    multipart.addBodyPart(messageBodyPart);
                }
            }
    
            mess.setContent(multipart);

            if(realHost.ssl)
            	LOG.debug(getClass().getName()+".sendMail: Sending SSL mail from:["+from+"] to:["+to+"] cc:["+cc+"] bcc:["+bcc+"]");
            else
            	LOG.debug(getClass().getName()+".sendMail: Sending mail from:["+from+"] to:["+to+"] cc:["+cc+"] bcc:["+bcc+"]");
            
            // Sending mail
            transport.sendMessage(mess,mess.getAllRecipients());
            
            if(toAddr.length > 0)
            	numMailSended = toAddr.length;
            else if(ccAddr.length > 0)
            	numMailSended = ccAddr.length;
            else if(bccAddr.length > 0)
            	numMailSended = bccAddr.length;
            
            return;
            
        }catch(SendFailedException sfe){	// Maybe partially sended
        	
           	numMailSended = sfe.getValidSentAddresses() == null ? 0 : sfe.getValidSentAddresses().length;
            LOG.error(sfe);
        	
        }catch(Exception e){
            
            String errorMsg = getClass().getName()+".sendMail: Exception sending mail to ["+to+"] from ["+from+"]: "+e;
            e = new Exception(errorMsg);            
            LOG.error(e);
            throw e;
            
        }finally{
        	
            if(mustDisconnect) disconnect();
            
        }
    }

    public String getSslPassword() {
        return sslPassword;
    }

    public void setSslPassword(String sslPassword) {
        this.sslPassword = sslPassword;
    }

    public String getSslUser() {
        return sslUser;
    }

    public void setSslUser(String sslUser) {
        this.sslUser = sslUser;
    }

    public String[] getAttachments() {
        return attachments;
    }

    public void setAttachments(String[] attachments) {
        this.attachments = attachments;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getPersonalFrom() {
        return personalFrom;
    }

    public void setPersonalFrom(String personalFrom) {
        this.personalFrom = personalFrom;
    }

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getBcc() {
		return bcc;
	}

	public void setBcc(String bcc) {
		this.bcc = bcc;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public int getNumMailSended() {
		return numMailSended;
	}

	public boolean isConnected() {
		return connected;
	}

}
