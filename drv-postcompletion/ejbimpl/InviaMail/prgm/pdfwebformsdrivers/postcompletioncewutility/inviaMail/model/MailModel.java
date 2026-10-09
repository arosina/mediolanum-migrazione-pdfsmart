package prgm.pdfwebformsdrivers.postcompletioncewutility.inviaMail.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;

public class MailModel extends CommandDataModel {
	private IntegerType		mailId				= new IntegerType();
	private TimestampType 	schedTime			= new TimestampType();
	private StringType 		from 				= new StringType(); 
	private StringType 		fromAlias  			= new StringType();
	private StringType 		to 					= new StringType(); 
	private StringType 		cc 					= new StringType(); 
	private StringType 		bcc 				= new StringType(); 
	private StringType 		subject 			= new StringType(); 
	private StringType 		body 				= new StringType();
	private StringType 		contentType			= new StringType();
	private StringType		templateName 		= new StringType();
	private BooleanType 	variablesInDb 		= new BooleanType();
	private StringType		mailStatus			= new StringType();
	
	
	public StringType getMailStatus() {
		return mailStatus;
	}
	public void setMailStatus(StringType mailStatus) {
		this.mailStatus = mailStatus;
	}
	public IntegerType getMailId() {
		return mailId;
	}
	public void setMailId(IntegerType mailId) {
		this.mailId = mailId;
	}
	public TimestampType getSchedTime() {
		return schedTime;
	}
	public void setSchedTime(TimestampType schedTime) {
		this.schedTime = schedTime;
	}
	public StringType getFrom() {
		return from;
	}
	public void setFrom(StringType from) {
		this.from = from;
	}
	public StringType getFromAlias() {
		return fromAlias;
	}
	public void setFromAlias(StringType fromAlias) {
		this.fromAlias = fromAlias;
	}
	public StringType getTo() {
		return to;
	}
	public void setTo(StringType to) {
		this.to = to;
	}
	public StringType getCc() {
		return cc;
	}
	public void setCc(StringType cc) {
		this.cc = cc;
	}
	public StringType getBcc() {
		return bcc;
	}
	public void setBcc(StringType bcc) {
		this.bcc = bcc;
	}
	public StringType getSubject() {
		return subject;
	}
	public void setSubject(StringType subject) {
		this.subject = subject;
	}
	public StringType getBody() {
		return body;
	}
	public void setBody(StringType body) {
		this.body = body;
	}
	public StringType getContentType() {
		return contentType;
	}
	public void setContentType(StringType contentType) {
		this.contentType = contentType;
	}
	public StringType getTemplateName() {
		return templateName;
	}
	public void setTemplateName(StringType templateName) {
		this.templateName = templateName;
	}
	public BooleanType getVariablesInDb() {
		return variablesInDb;
	}
	public void setVariablesInDb(BooleanType variablesInDb) {
		this.variablesInDb = variablesInDb;
	}
	

}
