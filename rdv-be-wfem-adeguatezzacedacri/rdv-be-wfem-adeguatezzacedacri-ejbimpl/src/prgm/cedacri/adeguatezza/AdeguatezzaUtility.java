package prgm.cedacri.adeguatezza;

import com.atosorigin.wfem.types.*;
import com.atosorigin.wfem.util.Tools;

public class AdeguatezzaUtility {
	public static String getDate()
	{
 		TimestampType tt = Tools.now();
 		return tt.getAA() + tt.getMM() + tt.getGG();
	}
	public static String getTime()
	{
 		TimestampType tt = Tools.now();
 		return tt.getHH() + tt.getMI() + tt.getSS();
	}
	public static String getTimeStamp()
	{
 		TimestampType tt = Tools.now();
 		return tt.getAA() + "-" + tt.getMM() + "-" + tt.getGG() + "-" + tt.getHH() + "." + tt.getMI() + "." + tt.getSS() + ".000000";
	}
	public static IntegerType getDateModel()
	{
 		TimestampType tt = Tools.now();
 		return new IntegerType(tt.getAA() + tt.getMM() + tt.getGG());
	}
	public static IntegerType getTimeModel()
	{
 		TimestampType tt = Tools.now();
 		return new IntegerType(tt.getHH() + tt.getMI() + tt.getSS());
	}
	public static StringType getTimeStampModel()
	{
 		TimestampType tt = Tools.now();
 		return new StringType(tt.getAA() + "-" + tt.getMM() + "-" + tt.getGG() + "-" + tt.getHH() + "." + tt.getMI() + "." + tt.getSS() + ".000000");
	}
	public static TimestampType getTimeStampTypeModel()
	{
		return Tools.now();
	}
	public static StringType convertStringTimestamp(TimestampType time)
	{
		return new StringType(time.getAA() + time.getMM() + time.getGG() + time.getHH() + time.getMI() + time.getSS());
	}
	public static StringType convertStringDate(TimestampType time)
	{
		return new StringType(time.getAA() + time.getMM() + time.getGG());
	}
	public static StringType convertStringTime(TimestampType time)
	{
		return new StringType(time.getHH() + time.getMI() + time.getSS());
	}
	public static TimestampType convertTimestampString(String time)
		throws Exception
	{
		if (time == null)
			throw new Exception("UserKey non valorizzata");
		if (time.length() != 14)
			throw new Exception("UserKey non completa");
		try
		{
			Long.parseLong(time);
		}
		catch (NumberFormatException nfe)
		{
			throw new Exception("UserKey trovati dei caratteri alfabetici nella data");
		}
		try
		{
			new TimestampType(time.substring(6,8)+"-"+time.substring(4,6)+"-"+time.substring(0,4)+" "+time.substring(8,10)+":"+time.substring(10,12)+":"+time.substring(12,14));
		}
		catch (Exception e)
		{
			throw new Exception("UserKey trovati dei valori non validi nella data");
		}
		return new TimestampType(time.substring(6,8)+"-"+time.substring(4,6)+"-"+time.substring(0,4)+" "+time.substring(8,10)+":"+time.substring(10,12)+":"+time.substring(12,14));
	}
	public static String padLeft(String parmString, char fillChar, int parmLen)
	{
		String tempString = parmString;
		if (tempString.length() > parmLen)
		{
			tempString = tempString.substring(tempString.length()-parmLen,tempString.length());
		}
		else
		{
			while (tempString.length() < parmLen)
				tempString = String.valueOf(fillChar) + tempString;
		}
		return tempString;
	}
	public static DateType convertDateString(String time)
		throws Exception
	{
		if (time == null)
			throw new Exception("UserKey non valorizzata");
		if (time.length() != 8)
			throw new Exception("UserKey non completa");
		try
		{
			Long.parseLong(time);
		}
		catch (NumberFormatException nfe)
		{
			throw new Exception("UserKey trovati dei caratteri alfabetici nella data");
		}
		try
		{
			new DateType(time.substring(6,8)+"-"+time.substring(4,6)+"-"+time.substring(0,4));
		}
		catch (Exception e)
		{
			throw new Exception("UserKey trovati dei valori non validi nella data");
		}
		return new DateType(time.substring(6,8)+"-"+time.substring(4,6)+"-"+time.substring(0,4));
	}
}
