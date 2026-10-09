package com.atosorigin.wfem.tierlog;

import java.io.Serializable;
import java.util.Calendar;

import com.atosorigin.wfem.util.Tools;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class TierAccessLoggerInfo implements Serializable{
	
	public static final int CLIENT_TIER = 1;
	public static final int MIDDLE_TIER = 2;
	
	private String 	accessId; // If = null --> Trace not configured for command
	private int		tier;
	private String 	clientIp;
	private long 	starttime;
	private long 	endtime;
	private String 	accessName;
	private String 	accessType;
	private String 	accessUser;
	private String 	inputParameters;
	
	/****************************************************************/
	/****************************************************************/
	public String traceString(){
		return 	accessId+";"+
				accessType+";"+
				accessName+";"+
				accessUser+";"+
				getDuration()+";"+
				getStartDate()+";"+
				getEndDate()+";"+
				getClientIp()+";"+
				getInputParameters();
	}
	
	/****************************************************************/
	/****************************************************************/
	public void start(){
		starttime = Calendar.getInstance().getTimeInMillis();
	}
	
	/****************************************************************/
	/****************************************************************/
	public void stop(){
		stop(new StringBuffer(inputParameters));
	}
	
	/****************************************************************/
	/****************************************************************/
	public void stop(StringBuffer tierInputParameters){
		if(tierInputParameters != null)
			setInputParameters(tierInputParameters.toString());
		endtime = Calendar.getInstance().getTimeInMillis();
		AbstractAccessLogger aal = null;
		if(tier == CLIENT_TIER)
			aal = ClientTierAccessLogger.getInstance();
		else if(tier == MIDDLE_TIER)
			aal = MiddleTierAccessLogger.getInstance();
		if(aal != null)
			aal.writeAccessLog(this);
	}

	/****************************************************************/
	/****************************************************************/
	public void exception(){
		setAccessName(getAccessName()+"#exception");
	}
	
	/****************************************************************/
	/****************************************************************/
	private long getDuration(){
		return endtime - starttime;
	}
	
	/****************************************************************/
	/****************************************************************/
	private String getStartDate(){
		return getDate(starttime);
	}

	/****************************************************************/
	/****************************************************************/
	private String getEndDate(){
		return getDate(endtime);
	}

	/****************************************************************/
	/****************************************************************/
	private String getDate(long milliseconds){
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(milliseconds);
		String yy = ""+c.get(Calendar.YEAR);
		String mm = Tools.fillSx(""+(c.get(Calendar.MONTH)+1),'0',2);
		String dd = Tools.fillSx(""+c.get(Calendar.DATE),'0',2);
		String hh = Tools.fillSx(""+c.get(Calendar.HOUR_OF_DAY),'0',2);
		String mi = Tools.fillSx(""+c.get(Calendar.MINUTE),'0',2);
		String ss = Tools.fillSx(""+c.get(Calendar.SECOND),'0',2);
		String ms = Tools.fillSx(""+c.get(Calendar.MILLISECOND),'0',3);
		return yy+mm+dd+" "+hh+":"+mi+":"+ss+":"+ms;
	}

	public String getAccessId() {
		return accessId;
	}

	public void setAccessId(String accessId) {
		this.accessId = accessId;
	}

	public int getTier() {
		return tier;
	}

	public void setTier(int tier) {
		this.tier = tier;
	}

	public long getStarttime() {
		return starttime;
	}

	public void setStarttime(long starttime) {
		this.starttime = starttime;
	}

	public long getEndtime() {
		return endtime;
	}

	public void setEndtime(long endtime) {
		this.endtime = endtime;
	}

	public String getAccessName() {
		return accessName;
	}

	public void setAccessName(String accessName) {
		this.accessName = accessName;
	}

	public String getAccessUser() {
		return accessUser;
	}

	public void setAccessUser(String accessUser) {
		this.accessUser = accessUser;
	}

	public String getAccessType() {
		return accessType;
	}

	public void setAccessType(String accessType) {
		this.accessType = accessType;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getInputParameters() {
		return inputParameters;
	}

	public void setInputParameters(String inputParameters) {
		this.inputParameters = inputParameters;
	}

}
