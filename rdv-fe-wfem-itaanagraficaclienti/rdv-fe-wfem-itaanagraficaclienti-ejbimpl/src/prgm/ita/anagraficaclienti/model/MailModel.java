package prgm.ita.anagraficaclienti.model;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;

/**************************************************************************************************/
/**************************************************************************************************/
public class MailModel extends CommandDataModel {
	
	private IntegerType		mailId				= new IntegerType();
	private StringType		mailStatus			= new StringType();
	private TimestampType 	schedTime			= new TimestampType();
	private StringType 		from 				= new StringType(); 
	private StringType 		fromAlias  			= new StringType();
	private StringType 		to 					= new StringType(); 
	private StringType 		subject 			= new StringType(); 
	private StringType 		body 				= new StringType();
	private StringType 		contentType 		= new StringType();
	private StringType 		userIns				= new StringType(); 
	
	public IntegerType getMailId() {
		return mailId;
	}
	public void setMailId(IntegerType mailId) {
		this.mailId = mailId;
	}
	public StringType getMailStatus() {
		return mailStatus;
	}
	public void setMailStatus(StringType mailStatus) {
		this.mailStatus = mailStatus;
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
	public StringType getUserIns() {
		return userIns;
	}
	public void setUserIns(StringType userIns) {
		this.userIns = userIns;
	}
	
}
