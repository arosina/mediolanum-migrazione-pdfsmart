package com.atosorigin.wfem.charts;

import org.jfree.data.xy.AbstractXYDataset;
import org.jfree.data.xy.OHLCDataItem;

import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class OhlcDataset extends AbstractXYDataset implements org.jfree.data.xy.OHLCDataset{
	
	private String     serie;
	private String[]   dates;
	private OHLCDataItem[] data;
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	public OhlcDataset(String serie, String[] dates, String[] yOHLCVvalues){
		this.serie = serie;
		this.dates = dates;
		data = new OHLCDataItem[dates.length];
		for(int i=0;i<dates.length;i++){
        	DateType date = new DateType(dates[i]);
        	String ohlcv = yOHLCVvalues[i];
        	String[] ohlcvArray = ohlcv.split("\\/",5);
        	if(ohlcvArray.length != 5)
        		continue;
        	double o = new DoubleType(ohlcvArray[0]).doubleValue();
        	double h = new DoubleType(ohlcvArray[1]).doubleValue();
        	double l = new DoubleType(ohlcvArray[2]).doubleValue();
        	double c = new DoubleType(ohlcvArray[3]).doubleValue();
        	double v = new DoubleType(ohlcvArray[4]).doubleValue();
			OHLCDataItem item = new OHLCDataItem(date.dateValue(),o,h,l,c,v);
			data[i] = item;
		}
	}

	public int getItemCount(int serieIdx) {
		return dates.length;
	}

	public Number getX(int serieIdx, int itemIdx) {
		return new Long(data[itemIdx].getDate().getTime());
	}

	public Number getY(int serieIdx, int itemIdx) {
        return getClose(serieIdx, itemIdx);
	}

	public int getSeriesCount() {
		return 1;
	}

	public Comparable getSeriesKey(int serieIdx) {
		return serie;
	}

	public Number getClose(int serieIdx, int itemIdx) {
		return data[itemIdx].getClose();
	}

	public double getCloseValue(int serieIdx, int itemIdx) {
		return data[itemIdx].getClose().doubleValue();
	}

	public Number getHigh(int serieIdx, int itemIdx) {
		return data[itemIdx].getHigh();
	}

	public double getHighValue(int serieIdx, int itemIdx) {
		return data[itemIdx].getHigh().doubleValue();
	}

	public Number getLow(int serieIdx, int itemIdx) {
		return data[itemIdx].getLow();
	}

	public double getLowValue(int serieIdx, int itemIdx) {
		return data[itemIdx].getLow().doubleValue();
	}

	public Number getOpen(int serieIdx, int itemIdx) {
		return data[itemIdx].getOpen();
	}

	public double getOpenValue(int serieIdx, int itemIdx) {
		return data[itemIdx].getOpen().doubleValue();
	}

	public Number getVolume(int serieIdx, int itemIdx) {
		return data[itemIdx].getVolume();
	}

	public double getVolumeValue(int serieIdx, int itemIdx) {
		return data[itemIdx].getVolume().doubleValue();
	}
	
}
