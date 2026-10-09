package com.atosorigin.wfem.charts;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.TickUnitSource;
import org.jfree.chart.axis.TickUnits;
import org.jfree.data.time.Day;
import org.jfree.data.time.Month;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.Year;

import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class TimeDataset extends TimeSeriesCollection {
  
	public  static final String pTimeType_DAY			 	= "day";
	public  static final String pTimeType_MONTH			 	= "month";
	public  static final String pTimeType_YEAR			 	= "year";
	
	private String timeType = pTimeType_DAY;
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	public TimeDataset(String[] series, String[] times, String[][] values, String timeType) {
		this.timeType = timeType;
		for(int i=0;i<series.length;i++){
			
	        TimeSeries timeseries = null;
        	if(timeType.equalsIgnoreCase(pTimeType_DAY))
    	        timeseries = new TimeSeries(series[i],Day.class);
        	else if(timeType.equalsIgnoreCase(pTimeType_MONTH))
    	        timeseries = new TimeSeries(series[i],Month.class);
        	else if(timeType.equalsIgnoreCase(pTimeType_YEAR))
    	        timeseries = new TimeSeries(series[i],Year.class);
	        
	        for(int j=0;j<times.length;j++){
			    try{
			    	String sval = values[i][j];
			    	if(sval.equals("null")){
			        	if(timeType.equalsIgnoreCase(pTimeType_DAY)){
				        	DateType time = new DateType(times[j]);
			        		timeseries.add(new Day(time.dateValue()),null);
			        	}else if(timeType.equalsIgnoreCase(pTimeType_MONTH)){
			        		DateType time = new DateType("01-"+times[j]);
			        		timeseries.add(new Month(time.dateValue()),null);
			        	}else if(timeType.equalsIgnoreCase(pTimeType_YEAR)){
			        		DateType time = new DateType("01-01-"+times[j]);
			        		timeseries.add(new Year(time.dateValue()),null);
			        	}
			    	}else{
			        	double value = new DoubleType(sval).doubleValue();
			        	if(timeType.equalsIgnoreCase(pTimeType_DAY)){
				        	DateType time = new DateType(times[j]);
			        		timeseries.add(new Day(time.dateValue()),value);
			        	}else if(timeType.equalsIgnoreCase(pTimeType_MONTH)){
			        		DateType time = new DateType("01-"+times[j]);
			        		timeseries.add(new Month(time.dateValue()),value);
			        	}else if(timeType.equalsIgnoreCase(pTimeType_YEAR)){
			        		DateType time = new DateType("01-01-"+times[j]);
			        		timeseries.add(new Year(time.dateValue()),value);
			        	}
			    	}
			    }catch(Exception e){}
	        }
			addSeries(timeseries);
		}
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	public void setFormat(DateAxis dateAxis, String format, String language){
		if(format.length() == 0){
			if(timeType.equals(pTimeType_DAY))
				format = "dd-MM-yyyy";
			else if(timeType.equals(pTimeType_MONTH))
				format = "MM-yyyy";
			else if(timeType.equals(pTimeType_YEAR))
				format = "yyyy";
			DateFormat formatter = new SimpleDateFormat(format);
			dateAxis.setDateFormatOverride(formatter);
		}else if(format.equalsIgnoreCase("auto")){
			dateAxis.setStandardTickUnits(TimeDataset.createStandardDateTickUnits(language));
		}else{
			DateFormat formatter = null;
			if(language != null && language.length() > 0)
				formatter = new SimpleDateFormat(format,new Locale(language.toLowerCase()));
			else
				formatter = new SimpleDateFormat(format);
			dateAxis.setDateFormatOverride(formatter);
		}
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
    public static TickUnitSource createStandardDateTickUnits(String language) {

    	Locale locale = new Locale(language.toLowerCase());
        TimeZone zone = TimeZone.getDefault();
        TickUnits units = new TickUnits();

        // date formatters
        DateFormat f1 = new SimpleDateFormat("d-MMM",locale);
        DateFormat f2 = new SimpleDateFormat("MMM-yyyy",locale);
        DateFormat f3 = new SimpleDateFormat("yyyy",locale);
        
        f1.setTimeZone(zone);
        f2.setTimeZone(zone);
        f3.setTimeZone(zone);
        
        // days
        units.add(new DateTickUnit(DateTickUnit.DAY, 1, DateTickUnit.HOUR, 1, f1));
        units.add(new DateTickUnit(DateTickUnit.DAY, 2, DateTickUnit.HOUR, 1, f1));
        units.add(new DateTickUnit(DateTickUnit.DAY, 7, DateTickUnit.DAY, 1, f1));
        units.add(new DateTickUnit(DateTickUnit.DAY, 15, DateTickUnit.DAY, 1, f1));

        // months
        units.add(new DateTickUnit(DateTickUnit.MONTH, 1, DateTickUnit.DAY, 1, f2));
        units.add(new DateTickUnit(DateTickUnit.MONTH, 2, DateTickUnit.DAY, 1, f2));
        units.add(new DateTickUnit(DateTickUnit.MONTH, 3, DateTickUnit.MONTH, 1, f2));
        units.add(new DateTickUnit(DateTickUnit.MONTH, 4,  DateTickUnit.MONTH, 1, f2));
        units.add(new DateTickUnit(DateTickUnit.MONTH, 6,  DateTickUnit.MONTH, 1, f2));

        // years
        units.add(new DateTickUnit(DateTickUnit.YEAR, 1,  DateTickUnit.MONTH, 1, f3));
        units.add(new DateTickUnit(DateTickUnit.YEAR, 2,  DateTickUnit.MONTH, 3, f3));
        units.add(new DateTickUnit(DateTickUnit.YEAR, 5,  DateTickUnit.YEAR, 1, f3));
        units.add(new DateTickUnit(DateTickUnit.YEAR, 10,  DateTickUnit.YEAR, 1, f3));
        units.add(new DateTickUnit(DateTickUnit.YEAR, 25, DateTickUnit.YEAR, 5, f3));
        units.add(new DateTickUnit(DateTickUnit.YEAR, 50, DateTickUnit.YEAR, 10, f3));
        units.add(new DateTickUnit(DateTickUnit.YEAR, 100, DateTickUnit.YEAR, 20, f3));

        return units;

    }
	
}
