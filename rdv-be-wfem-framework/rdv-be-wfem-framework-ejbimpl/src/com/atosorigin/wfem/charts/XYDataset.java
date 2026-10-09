package com.atosorigin.wfem.charts;

import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.atosorigin.wfem.types.DoubleType;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class XYDataset extends XYSeriesCollection{

	/*************************************************************************************************/
	/*************************************************************************************************/
	public XYDataset(String[] series, String[][] xValues, String[][] yValues) {
		for(int i=0;i<series.length;i++){
	        XYSeries xyseries = new XYSeries(series[i]);
	        for(int j=0;j<xValues[i].length;j++){
	        	try{
		        	double x = new DoubleType(xValues[i][j]).doubleValue();
		        	double y = new DoubleType(yValues[i][j]).doubleValue();
		        	xyseries.add(x,y);
	        	}catch(Exception e){}
	        }
			addSeries(xyseries);
		}
	}
	
}
