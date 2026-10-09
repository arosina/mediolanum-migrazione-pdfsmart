package com.atosorigin.wfem.xmlservicelogger;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;

/********************************************************************************/
/********************************************************************************/
public class XmlServiceLoggerModel extends CommandDataModel{
	private StringType 		userCode = new StringType();		// C_USER 

	private IntegerType 	increment 	= new IntegerType();
	private StringType 		progr	= new StringType();

	private StringType 		instanceId = new StringType(); 		// PRGM_SRUN_SVC_INSTANCES.C_SVC_INSTANCE 
	private StringType 		serviceName = new StringType();		// PRGM_SRUN_SVC_INSTANCES.X_SERVICE_ID 
	private TimestampType 	startTime = new TimestampType();	// PRGM_SRUN_SVC_INSTANCES.D_START_TIME // PRGM_SRUN_SERVICES_ERR.ERR_TIME
	private IntegerType		duration = new IntegerType();		// PRGM_SRUN_SVC_INSTANCES.N_DURATION_MS
	private TimestampType 	endTime = new TimestampType();		// PRGM_SRUN_SVC_INSTANCES.D_END_TIME
	private StringType 		xmlSend = new StringType();			// PRGM_SRUN_SVC_INSTANCES.X_SEND_XML_INPUT    
	private StringType 		xmlReceive = new StringType();		// PRGM_SRUN_SVC_INSTANCES.X_SEND_XML_OUTPUT    
	private StringType 		status = new StringType();			// PRGM_SRUN_SVC_INSTANCES.X_STATUS_CODE // PRGM_SRUN_SERVICES_ERR.ERR_STATUS  
	private StringType 		message = new StringType();			// PRGM_SRUN_SVC_INSTANCES.X_STATUS_MESSAGE    
	private StringType 		severity = new StringType();		// PRGM_SRUN_SVC_INSTANCES.X_STATUS_SEVERITY    
	private StringType 		source = new StringType();			// PRGM_SRUN_SVC_INSTANCES.X_STATUS_SOURCE    
	private StringType 		otherStatus = new StringType();		// PRGM_SRUN_SVC_INSTANCES.A_STATUS    
	private StringType 		inAppInfo = new StringType();		// PRGM_SRUN_SVC_INSTANCES.X_INPUT_APP_INFO 
	private StringType 		outAppInfo = new StringType();		// PRGM_SRUN_SVC_INSTANCES.X_OUTPUT_APP_INFO 
	private StringType 		linkedUser = new StringType();		// PRGM_SRUN_SVC_INSTANCES.AGE_C_AGE    
	
	private StringType 		serviceDescr = new StringType();		// PRGM_SRUN_SERVICES.X_SERVICE_DESC  
	private StringType 		mailToOnError = new StringType();		// PRGM_SRUN_SERVICES.MAILTO_ONERR
	private IntegerType		mailErrFreqMinutes = new IntegerType();	// PRGM_SRUN_SERVICES.ERR_MAIL_FREQ_MINUTES
	private StringType 		applErrCodeToTrace = new StringType();	// PRGM_SRUN_SERVICES.APPL_ERR_CODE_TO_TRACE  
	
	private IntegerType		httpStatus = new IntegerType();		// NO DB FIELD
	
	private IntegerType		numTraceElements = new IntegerType();
	
	public TimestampType getEndTime() {
		return endTime;
	}
	public void setEndTime(TimestampType endTime) {
		this.endTime = endTime;
	}
	public StringType getInAppInfo() {
		return inAppInfo;
	}
	public void setInAppInfo(StringType inAppInfo) {
		this.inAppInfo = inAppInfo;
	}
	public StringType getMessage() {
		return message;
	}
	public void setMessage(StringType message) {
		this.message = message;
	}
	public StringType getOtherStatus() {
		return otherStatus;
	}
	public void setOtherStatus(StringType otherStatus) {
		this.otherStatus = otherStatus;
	}
	public StringType getOutAppInfo() {
		return outAppInfo;
	}
	public void setOutAppInfo(StringType outAppInfo) {
		this.outAppInfo = outAppInfo;
	}
	public StringType getServiceName() {
		return serviceName;
	}
	public void setServiceName(StringType serviceName) {
		this.serviceName = serviceName;
	}
	public StringType getSeverity() {
		return severity;
	}
	public void setSeverity(StringType severity) {
		this.severity = severity;
	}
	public StringType getSource() {
		return source;
	}
	public void setSource(StringType source) {
		this.source = source;
	}
	public TimestampType getStartTime() {
		return startTime;
	}
	public void setStartTime(TimestampType startTime) {
		this.startTime = startTime;
	}
	public StringType getStatus() {
		return status;
	}
	public void setStatus(StringType status) {
		this.status = status;
	}
	public StringType getUserCode() {
		return userCode;
	}
	public void setUserCode(StringType userCode) {
		this.userCode = userCode;
	}
	public StringType getXmlReceive() {
		return xmlReceive;
	}
	public void setXmlReceive(StringType xmlReceive) {
		this.xmlReceive = xmlReceive;
	}
	public StringType getXmlSend() {
		return xmlSend;
	}
	public void setXmlSend(StringType xmlSend) {
		this.xmlSend = xmlSend;
	}
	public IntegerType getIncrement() {
		return increment;
	}
	public void setIncrement(IntegerType increment) {
		this.increment = increment;
	}
	public StringType getProgr() {
		return progr;
	}
	public void setProgr(StringType progr) {
		this.progr = progr;
	}
	public StringType getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(StringType instanceId) {
		this.instanceId = instanceId;
	}
	public StringType getLinkedUser() {
		return linkedUser;
	}
	public void setLinkedUser(StringType linkedUser) {
		this.linkedUser = linkedUser;
	}
	public IntegerType getDuration() {
		return duration;
	}
	public void setDuration(IntegerType duration) {
		this.duration = duration;
	}
	public StringType getServiceDescr() {
		return serviceDescr;
	}
	public void setServiceDescr(StringType serviceDescr) {
		this.serviceDescr = serviceDescr;
	}
	public StringType getMailToOnError() {
		return mailToOnError;
	}
	public void setMailToOnError(StringType mailToOnError) {
		this.mailToOnError = mailToOnError;
	}
	public IntegerType getHttpStatus() {
		return httpStatus;
	}
	public void setHttpStatus(IntegerType httpStatus) {
		this.httpStatus = httpStatus;
	}
	public IntegerType getMailErrFreqMinutes() {
		return mailErrFreqMinutes;
	}
	public void setMailErrFreqMinutes(IntegerType mailErrFreqMinutes) {
		this.mailErrFreqMinutes = mailErrFreqMinutes;
	}
	public StringType getApplErrCodeToTrace() {
		return applErrCodeToTrace;
	}
	public void setApplErrCodeToTrace(StringType applErrCodeToTrace) {
		this.applErrCodeToTrace = applErrCodeToTrace;
	}
	public IntegerType getNumTraceElements() {
		return numTraceElements;
	}
	public void setNumTraceElements(IntegerType numTraceElements) {
		this.numTraceElements = numTraceElements;
	}

}
