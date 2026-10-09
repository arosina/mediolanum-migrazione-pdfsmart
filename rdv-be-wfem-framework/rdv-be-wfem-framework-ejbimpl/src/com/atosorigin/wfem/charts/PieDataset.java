package com.atosorigin.wfem.charts;

import org.jfree.data.general.DefaultPieDataset;

import com.atosorigin.wfem.types.DoubleType;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class PieDataset extends DefaultPieDataset {
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	public PieDataset(String[] series, String[] values) {
	    for(int i=0;i<values.length;i++){
		    try{
	        	double value = new DoubleType(values[i]).doubleValue();
			    setValue(series[i],value);
		    }catch(Exception e){}
	    }
	}
}
