package com.atosorigin.wfem.types;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import com.atosorigin.wfem.controller.FieldFormatException;
import com.atosorigin.wfem.util.Tools;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public final class TimestampType extends AbstractType implements java.io.Serializable {
	
	private boolean emptyHour = false;

	/**********************************************************
	 **********************************************************/
	public TimestampType() {
	}

	/**********************************************************
	 **********************************************************/
	public TimestampType(String str) throws FieldFormatException {
		super(str);
	}

	/**********************************************************
	 **********************************************************/
	public TimestampType(Timestamp parameterValue) {
		super(parameterValue);
	}

	/**********************************************************
	 **********************************************************/
	public TimestampType(DateType parameterValue) {
		this(parameterValue.toString());
	}

	/**********************************************************
	 **********************************************************/
	public int compareTo(AbstractType o) {
		return compareTo(new TimestampType(o.toString()));
	}

	/**********************************************************
	 **********************************************************/
	public int compareTo(TimestampType o) {

		return checkNullCompareToNull(o) != Integer.MAX_VALUE
			? checkNullCompareToNull(o)
			: ((Timestamp) getValue()).compareTo((Timestamp)o.getValue());
	}

	/**********************************************************
	 **********************************************************/
	public boolean equals(AbstractType o) {
		int check = checkNullCompareToNull(o);
		if (check == 0)
			return true;
		else if (check == 1 || check == -1)
			return false;
		else {
			if (o instanceof TimestampType) {
				return equals((Timestamp) o.getValue());
			}
			return equals(new TimestampType(o.toString()));
		}
	}

	/**********************************************************
	 **********************************************************/
	public boolean equals(TimestampType tm) {

		return equals(tm.timestampValue());
	}

	/**********************************************************
	 **********************************************************/
	public boolean equals(Timestamp tm) {

		return timestampValue().equals(tm);
	}

	/**********************************************************
	 **********************************************************/
	public void setStringValue(String newValue)	throws FieldFormatException{

		try {

			if (newValue == null || newValue.equals("")) {
				setValue(null);
			} else {

				// We put current hour if the string is only a date

				// The initial format is GG-MM-AAAA HH:MM:SS but
				// the timestamp format is AAAA-MM-GG HH:MM:SS.FFFFFFFFF
				
				String dateStr = "";
				String hourStr = "";
				switch(newValue.length()){
					case 5: 	// HH:MM
						dateStr = "01-01-1970";
						hourStr = newValue.toString()+":00";
						break;
					case 8: 	// HH:MM:SS
						dateStr = "01-01-1970";
						hourStr = newValue.toString();
						break;
					case 10: 	// GG-MM-AAAA
						dateStr = newValue.toString();
						hourStr = "00:00:00";
						break;
					case 13: 	// GG-MM-AAAA HH
						dateStr = newValue.substring(0,10);
						hourStr = newValue.substring(11)+":00:00";
						break;
					case 16: 	// GG-MM-AAAA HH:MM
						dateStr = newValue.substring(0,10);
						hourStr = newValue.substring(11)+":00";
						break;
					case 19: 	// GG-MM-AAAA HH:MM:SS
						dateStr = newValue.substring(0,10);
						hourStr = newValue.substring(11);
						break;
					default:
						dateStr = newValue.substring(0,10);
						if(newValue.length() > 10)
							hourStr = newValue.substring(11, 19);
						else
							hourStr = "00:00:00";
						break;
				}
				
				int hour = Integer.parseInt(hourStr.substring(0,2));
				int min = Integer.parseInt(hourStr.substring(3,5));
				int sec = Integer.parseInt(hourStr.substring(6,8));
				if(hour > 23 || min > 59 || sec > 59)
					throw new FieldFormatException("Invalid hour");
				
				String millisecondsStr = "000000000";
				if(newValue.length() > 20){
					millisecondsStr = newValue.substring(20);
					millisecondsStr = Tools.fillDx(millisecondsStr,'0',9);
				}
				
				DateType date = new DateType(dateStr);
				String timestampStr =
						  date.getAA()
						+ "-"
						+ date.getMM()
						+ "-"
						+ date.getGG()
						+ " "
						+ hourStr
						+ "."
						+ millisecondsStr;
				Timestamp timestampTmp = Timestamp.valueOf(timestampStr);
				setValue(timestampTmp);
				
				if(newValue.length() <= 10)
					emptyHour = true;
			}

		} catch (Exception e) {
			throw new FieldFormatException(e.getMessage());
		}

	}

	/**********************************************************
	 **********************************************************/
	public Timestamp timestampValue() {
		return (Timestamp) getValue();
	}

	/**********************************************************
	 **********************************************************/
	public void setTimestampValue(Timestamp value) {
		setValue(value);
	}

	/**********************************************************
	 **********************************************************/
	public String toString() {

		if (isNull())
			return "";

		String dateStr = ((Timestamp) getValue()).toString();
		String aa = dateStr.substring(0, 4);
		String mm = dateStr.substring(5, 7);
		String gg = dateStr.substring(8, 10);
		String hh = dateStr.substring(11, 19);

		return gg + "-" + mm + "-" + aa + " " + hh;
	}

	/**********************************************************
	 **********************************************************/
	public DateType toDateType() {
		if (isNull())
			return new DateType();
		return new DateType(toDateString());
	}

	/**********************************************************
	 **********************************************************/
	public String toDateString() {
		if (isNull())
			return "";
		String timestampStr = toString();
		return timestampStr.substring(0, 10);
	}

	/**********************************************************
	 **********************************************************/
	public String toHourString() {
		if (isNull())
			return "";
		String timestampStr = toString();
		return timestampStr.substring(11, 19);
	}

	/****************************************************************/
	/****************************************************************/
	public java.lang.String getAA() {
	
		if(isNull())
			return "";
			
		String res = new String("");
	
		Calendar calendar = Calendar.getInstance();
		calendar.setTime((Date)getValue());
	
		int iAA = calendar.get(Calendar.YEAR);
	
		res += iAA;
		return res;
	}

	/****************************************************************/
	/****************************************************************/
	public String getGG() {
	
		if(isNull())
			return "";
			
		String res = new String("");
	
		Calendar calendar = Calendar.getInstance();
		calendar.setTime((Date)getValue());
	
		int iGG = calendar.get(Calendar.DATE);
	
		if(iGG < 10)
			res += "0";
		res += iGG;
		return res;
	}

	/****************************************************************/
	/****************************************************************/
	public java.lang.String getMM() {
	
		if(isNull())
			return "";
			
		String res = new String("");
	
		Calendar calendar = Calendar.getInstance();
		calendar.setTime((Date)getValue());
	
		int iMM = calendar.get(Calendar.MONTH)+1;
	
		if(iMM < 10)
			res += "0";
		res += iMM;
		return res;
	}

	/****************************************************************/
	/****************************************************************/
	public java.lang.String getHH() {
	
		if(isNull())
			return "";
			
		String res = new String("");
	
		Calendar calendar = Calendar.getInstance();
		calendar.setTime((Date)getValue());
	
		int iHH = calendar.get(Calendar.HOUR_OF_DAY);
	
		if(iHH < 10)
			res += "0";
		res += iHH;
		return res;
	}

	/****************************************************************/
	/****************************************************************/
	public java.lang.String getMI() {
	
		if(isNull())
			return "";
			
		String res = new String("");
	
		Calendar calendar = Calendar.getInstance();
		calendar.setTime((Date)getValue());
	
		int iMI = calendar.get(Calendar.MINUTE);
	
		if(iMI < 10)
			res += "0";
		res += iMI;
		return res;
	}

	/****************************************************************/
	/****************************************************************/
	public java.lang.String getSS() {
	
		if(isNull())
			return "";
			
		String res = new String("");
	
		Calendar calendar = Calendar.getInstance();
		calendar.setTime((Date)getValue());
	
		int iSS = calendar.get(Calendar.SECOND);
	
		if(iSS < 10)
			res += "0";
		res += iSS;
		return res;
	}

	/****************************************************************/
	/****************************************************************/
	public java.lang.String getMS() {
		if(isNull())
			return "";
		try{
			int millis = ((Timestamp)getValue()).getNanos(); 
			if(millis == 0)
				return "";
			return Tools.fillDx(""+millis,'0',9);
		}catch(Exception e){
			return "";
		}
	}
	
	/****************************************************************/
	/****************************************************************/
	public boolean isEmptyHour() {
		return emptyHour;
	}
	
}
