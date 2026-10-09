package com.atosorigin.wfem.charts;

import java.math.BigDecimal;

import org.jfree.data.category.DefaultCategoryDataset;

import com.atosorigin.wfem.types.DoubleType;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class CategoryDataset extends DefaultCategoryDataset{
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	public CategoryDataset(String[] series, String[] xValues, String[][] yValues, boolean isPercentCategory) {
		int rows = yValues.length;
		int cols = 0;
		if(rows > 0)
			cols = yValues[0].length;
		
		if(isPercentCategory){
			
	    	for(int row=0;row<rows;row++){
	    		
    			double sum = 0;
	    		for(int col=0;col<cols;col++){
				    try{
				    	String sval = yValues[row][col];
				    	if(sval.equals("null"))
				    		continue;
			            sum += new DoubleType(sval).doubleValue();
				    }catch(Exception e){}
		    	}
	    		for(int col=0;col<cols;col++){
	    			try{
				    	String sval = yValues[row][col];
				    	if(sval.equals("null"))
					        addValue(null, series[row], xValues[col]);
				    	else{
			    			double percVal = (sum > 0 ? new DoubleType(sval).doubleValue() / sum : 0) * 100;
					        addValue(new BigDecimal(percVal), series[row], xValues[col]);
				    	}
	    			}catch(Exception e){}			    	
	    		}
		    }
	    	
		}else{
			
		    for(int row=0;row<rows;row++){
			    for(int col=0;col<cols;col++){
				    try{
				    	String sval = yValues[row][col];
				    	if(sval.equals("null"))
					        addValue(null, series[row], xValues[col]);
				    	else{
				            double value = new DoubleType(sval).doubleValue();
					        addValue(value, series[row], xValues[col]);
				    	}
				    }catch(Exception e){}
			    }
		    }
		}
		
	}
}
