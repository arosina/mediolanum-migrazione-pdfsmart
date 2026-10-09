package com.atosorigin.wfem.types;

import java.util.Calendar;
import java.util.Date;
import java.util.StringTokenizer;

import com.atosorigin.wfem.controller.FieldFormatException;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public final class DateType	extends AbstractType implements java.io.Serializable {

	/****************************************************************/
	/****************************************************************/
	public DateType() {
	}

	/****************************************************************/
	/****************************************************************/
	public DateType(String str) throws FieldFormatException {
		super(str);
	}

	/****************************************************************/
	/****************************************************************/
	public DateType(Date parameterValue) {
		setValue(clearTime(parameterValue));
	}

	/****************************************************************/
	/****************************************************************/
	public void addDays(int numDays) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime((Date) getValue());

		calendar.add(Calendar.DATE, numDays);

		setValue(calendar.getTime());
	}

	/****************************************************************/
	/****************************************************************/
	public void addMonths(int numMonths) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime((Date) getValue());

		calendar.add(Calendar.MONTH, numMonths);

		setValue(calendar.getTime());
	}

	/****************************************************************/
	/****************************************************************/
	public void addYears(int numYears) {

		Calendar calendar = Calendar.getInstance();
		calendar.setTime((Date) getValue());

		calendar.add(Calendar.YEAR, numYears);

		setValue(calendar.getTime());
	}

	/****************************************************************/
	/****************************************************************/
	public DateType addDaysOnNew(int numDays) {
		if(this.isNull())
			return new DateType();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime((Date) getValue());

		calendar.add(Calendar.DATE, numDays);

		return new DateType(clearTime(calendar.getTime()));
	}

	/****************************************************************/
	/****************************************************************/
	public DateType addMonthsOnNew(int numMonths) {
		if(this.isNull())
			return new DateType();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime((Date) getValue());

		calendar.add(Calendar.MONTH, numMonths);

		return new DateType(clearTime(calendar.getTime()));
	}

	/****************************************************************/
	/****************************************************************/
	public DateType addYearsOnNew(int numYears) {
		if(this.isNull())
			return new DateType();

		Calendar calendar = Calendar.getInstance();
		calendar.setTime((Date) getValue());

		calendar.add(Calendar.YEAR, numYears);

		return new DateType(clearTime(calendar.getTime()));
	}

	/****************************************************************/
	/****************************************************************/
	public DateType lastMonthDay(){
		if(this.isNull())
			return new DateType();
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime((Date)getValue());
		calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		
		return new DateType(clearTime(calendar.getTime()));
	}
	
	/****************************************************************/
	/****************************************************************/
	public int compareTo(AbstractType o) {

		return compareTo(new DateType(o.toString()));
	}

	/****************************************************************/
	/****************************************************************/
	public int compareTo(DateType o) {

		return checkNullCompareToNull(o) != Integer.MAX_VALUE
			? checkNullCompareToNull(o)
			: ((Date) getValue()).compareTo((Date)o.getValue());
	}

	/****************************************************************/
	/****************************************************************/
	public Date dateValue() {
		return (Date)getValue();
	}

	/****************************************************************/
	/****************************************************************/
	public void setDateValue(Date value) {
		setValue(clearTime(value));
	}

	/****************************************************************/
	/****************************************************************/
	public boolean equals(AbstractType o) {

		int check = checkNullCompareToNull(o);
		if (check == 0)
			return true;
		else if (check == 1 || check == -1)
			return false;
		else {
			if (o instanceof DateType) {
				return equals((Date) o.getValue());
			}
			return equals(new DateType(o.toString()));
		}
	}

	/****************************************************************/
	/****************************************************************/
	public boolean equals(DateType date) {

		return toString().equals(date.toString());
	}

	/****************************************************************/
	/****************************************************************/
	public boolean equals(Date date) {

		Calendar thisCalendar = Calendar.getInstance();
		thisCalendar.setTime(dateValue());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return (
			(thisCalendar.get(Calendar.DATE) == calendar.get(Calendar.DATE))
				&& (thisCalendar.get(Calendar.MONTH)
					== calendar.get(Calendar.MONTH))
				&& (thisCalendar.get(Calendar.YEAR)
					== calendar.get(Calendar.YEAR)));
	}

	/****************************************************************/
	/****************************************************************/
	private static String formatDate(String date) throws FieldFormatException {

		try {
			// Replace separators whith '-'
			date = date.replace('/', '-');
			date = date.replace('.', '-');
			date = date.replace('\\', '-');

			StringTokenizer tokenizer = new StringTokenizer(date, "-");

			String gg = tokenizer.nextToken();
			int gi = Integer.parseInt(gg);
			if (gi < 10)
				gg = "0" + gi;

			String mm = tokenizer.nextToken();
			int mi = Integer.parseInt(mm);
			if (mi < 10)
				mm = "0" + mi;

			String aa = tokenizer.nextToken();
			int ai = Integer.parseInt(aa);
			if (ai < 10)
				aa = "200" + ai;
			else if (ai < 100)
				aa = "20" + ai;

			return gg + "-" + mm + "-" + aa;

		} catch (Exception e) {
			throw new FieldFormatException(e.getMessage());
		}
	}

	/****************************************************************/
	/****************************************************************/
	public java.lang.String getAA() {

		if (isNull())
			return "";

		String res = new String("");

		Calendar calendar = Calendar.getInstance();
		calendar.setTime((Date) getValue());

		int iAA = calendar.get(Calendar.YEAR);

		res += iAA;
		return res;
	}

	/****************************************************************/
	/****************************************************************/
	public String getGG() {

		if (isNull())
			return "";

		String res = new String("");

		Calendar calendar = Calendar.getInstance();
		calendar.setTime((Date) getValue());

		int iGG = calendar.get(Calendar.DATE);

		if (iGG < 10)
			res += "0";
		res += iGG;
		return res;
	}

	/****************************************************************/
	/****************************************************************/
	public java.lang.String getMM() {

		if (isNull())
			return "";

		String res = new String("");

		Calendar calendar = Calendar.getInstance();
		calendar.setTime((Date) getValue());

		int iMM = calendar.get(Calendar.MONTH) + 1;

		if (iMM < 10)
			res += "0";
		res += iMM;
		return res;
	}

	/****************************************************************/
	/****************************************************************/
	public static Date isValidDateString(String date) throws FieldFormatException {

		try {
			date = formatDate(date);

			String gg = date.substring(0, 2);
			String mm = date.substring(3, 5);
			String aa = date.substring(6, 10);

			int ggInt = Integer.parseInt(gg);
			int mmInt = Integer.parseInt(mm) - 1;
			int aaInt = Integer.parseInt(aa);
			Calendar calendarTmp = Calendar.getInstance();
			calendarTmp.clear();
			calendarTmp.set(aaInt, mmInt, ggInt);
			if (ggInt == calendarTmp.get(Calendar.DATE)
				&& mmInt == calendarTmp.get(Calendar.MONTH)
				&& aaInt == calendarTmp.get(Calendar.YEAR)) {
				if(aaInt < 1753)
					throw new FieldFormatException("Invalid date");
				return calendarTmp.getTime();
			} else {
				throw new FieldFormatException("Invalid date format");
			}

		} catch (Exception e) {
			throw new FieldFormatException(e.getMessage());
		}
	}

	/****************************************************************/
	/****************************************************************/
	public void setStringValue(java.lang.String newValue) throws FieldFormatException {
		try {

			if (newValue == null || newValue.equals("")) {
				setValue(null);
			} else {
				Date dateTmp = isValidDateString(newValue);
				setValue(clearTime(dateTmp));
			}

		} catch (Exception e) {
			throw new FieldFormatException(e.getMessage());
		}
	}

	/****************************************************************/
	/****************************************************************/
	public java.lang.String toString() {

		if (isNull())
			return "";

		String res = new String("");

		Calendar calendar = Calendar.getInstance();
		calendar.setTime((Date) getValue());

		int iGG = calendar.get(Calendar.DATE);
		int iMM = calendar.get(Calendar.MONTH) + 1;
		int iAA = calendar.get(Calendar.YEAR);

		if (iGG < 10)
			res += "0";
		res += iGG + "-";

		if (iMM < 10)
			res += "0";
		res += iMM + "-";

		res += iAA;

		return res;
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public IntegerType yearsBetween(DateType otherDate){
		
		if(this.isNull() || otherDate.isNull())
			return new IntegerType();
		
	    int res = 0;
	    Calendar otherCal = Calendar.getInstance();
	    otherCal.setTime(otherDate.dateValue());

	    Calendar thisCal = Calendar.getInstance();
	    thisCal.setTime(dateValue());
	    
	    if(otherCal.before(thisCal)){
	    	res = thisCal.get(Calendar.YEAR) - otherCal.get(Calendar.YEAR);
		    otherCal.add(Calendar.YEAR, res);
		    if(thisCal.before(otherCal))
		      res--;
	    }else{
	    	res = otherCal.get(Calendar.YEAR) - thisCal.get(Calendar.YEAR);
		    thisCal.add(Calendar.YEAR, res);
		    if(otherCal.before(thisCal))
		      res--;
	    }	    
	    return new IntegerType(res);
		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public IntegerType monthBetween(DateType otherDate){
		
		if(this.isNull() || otherDate.isNull())
			return new IntegerType();
		
	    int res = 0;
	    
	    Calendar otherCal = Calendar.getInstance();
	    otherCal.setTime(otherDate.dateValue());

	    Calendar thisCal = Calendar.getInstance();
	    thisCal.setTime(dateValue());
	    
	    int yearsBetweenDate = (yearsBetween(otherDate)).intValue();
	    
	    if(otherCal.before(thisCal)){
	    	otherCal.add(Calendar.YEAR, yearsBetweenDate);
	    	if (otherCal.before(thisCal)) {
	    		otherCal.add(Calendar.MONTH, 1);
	    		while ( otherCal.before(thisCal)) {
	    			res++;
	    			otherCal.add(Calendar.MONTH, 1);
	    		}
	    	} else {
	    		while ( otherCal.after(thisCal)) {
	    			res--;
	    			otherCal.add(Calendar.MONTH, -1);
	    		}
	    	}
	    }else{
	    	thisCal.add(Calendar.YEAR, yearsBetweenDate);
	    	if (thisCal.before(otherCal)) {
	    		thisCal.add(Calendar.MONTH, 1);
	    		while ( thisCal.before(otherCal)) {
	    			res++;
	    			thisCal.add(Calendar.MONTH, 1);
	    		}
	    	} else {
	    		while ( thisCal.after(otherCal)) {
	    			res--;
	    			thisCal.add(Calendar.MONTH, -1);
	    		}
	    	}
	    }	    

	    return new IntegerType(yearsBetweenDate*12 + res);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/	
	public IntegerType daysBetween(DateType otherDate){
		
		if(this.isNull() || otherDate.isNull())
			return new IntegerType();
		
	    Calendar otherCal = Calendar.getInstance();
	    otherCal.setTime(otherDate.dateValue());

	    Calendar thisCal = Calendar.getInstance();
	    thisCal.setTime(dateValue());
		
		// different date might have different offset
		long lThisCal = this.dateValue().getTime() + thisCal.get(Calendar.ZONE_OFFSET) + thisCal.get(Calendar.DST_OFFSET);
		long lOtherCal = otherDate.dateValue().getTime() + otherCal.get(Calendar.ZONE_OFFSET) + otherCal.get(Calendar.DST_OFFSET);
		// Use integer calculation, truncate the decimals
		int hrThis   = (int)(lThisCal/3600000); //60*60*1000
		int hrOther   = (int)(lOtherCal/3600000);
		int daysThis = (int)hrThis/24;
		int daysOther = (int)hrOther/24;
		int dateDiff  = daysOther - daysThis;

		return new IntegerType(dateDiff);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private Date clearTime(Date date){
		if(date == null)
			return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int gg = cal.get(Calendar.DATE); int mm = cal.get(Calendar.MONTH); int aa = cal.get(Calendar.YEAR);
		cal.clear();
		cal.set(aa,mm,gg);
		return cal.getTime();
	}
}
