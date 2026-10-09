package com.atosorigin.wfem.charts;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class LineChart extends AbstractChart {
	
	private static final String pDataType			 		= "dataType";
	private static final String pDataType_Categories		= "categories";
	private static final String pDataType_Time			 	= "time";
	private static final String pDataType_Time_TimeType		= "timeType";
	public  static final String pDataType_Time_TimeFormat	= "timeFormat";
	private static final String pDataType_XY				= "XY";
		
	private static final String pSeries       			 	= "series";
	private static final String pSeriesColors 			 	= "seriesColors";
	
	private static final String pXValues 					= "xValues";
	private static final String pYValues 					= "yValues";
	private static final String pXMaxValue 					= "xMaxValue";
	private static final String pXMinValue 					= "xMinValue";
	private static final String pYMaxValue 					= "yMaxValue";
	private static final String pYMinValue 					= "yMinValue";
	
	private static final String pVerticalXAxisLabel   		= "verticalXAxisLabels";
	private static final String pUpperMarginPercent		 	= "upperMarginPercent";
	private static final String pLowerMarginPercent		 	= "lowerMarginPercent";
	private static final String pAutoRangeIncludesZero		= "autoRangeIncludesZero";
	
	private static final String pShowValues              	= "showValues";
	private static final String pShowSeriesShapes 		 	= "showSeriesShapes";
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	public JFreeChart createChart(String title, String xAxisLabel, String yAxisLabel){
		boolean showLegend = false;
	
		String  dataType				= getParam(pDataType,pDataType_Categories);
		
		String[]   series     	   		= getOneDimParam(pSeries);
		String[][] yValues   	   		= getTwoDimParam(pYValues);

		Double xMaxValue				= getDoubleParam(pXMaxValue,null);
		Double xMinValue				= getDoubleParam(pXMinValue,null);
		Double yMaxValue				= getDoubleParam(pYMaxValue,null);
		Double yMinValue				= getDoubleParam(pYMinValue,null);

		double upperMarginPercent		= getDoubleParam(pUpperMarginPercent,new Double(0.1)).doubleValue();	
		double lowerMarginPercent		= getDoubleParam(pLowerMarginPercent,new Double(-1)).doubleValue();
		boolean verticalCatLabel 		= getBoolParam(pVerticalXAxisLabel,false);
		boolean showValues       		= getBoolParam(pShowValues,false);
		boolean showSeriesShapes    	= getBoolParam(pShowSeriesShapes,true);
		boolean autoRangeIncludesZero 	= getBoolParam(pAutoRangeIncludesZero,true);

		String[] seriesColors    	= getOneDimParam(pSeriesColors);
		Color[]  colors = getColors(seriesColors); 	 

	    JFreeChart chart = null;
		if(dataType.equals(pDataType_Time)){
			
			String  timeType = getParam(pDataType_Time_TimeType,TimeDataset.pTimeType_DAY);
			String  timeFormat = getParam(pDataType_Time_TimeFormat,"");
			String[] xValues = getOneDimParam(pXValues);
			
			TimeDataset data = new TimeDataset(series,xValues,yValues,timeType);
	        chart = ChartFactory.createTimeSeriesChart(title,xAxisLabel,yAxisLabel,data,
	        										   showLegend,false,false);
	        
	        XYPlot plot = chart.getXYPlot();
	        
	        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer)plot.getRenderer();
	        renderer.setItemLabelGenerator(new StandardXYItemLabelGenerator());
	        renderer.setItemLabelsVisible(showValues);
            renderer.setBaseShapesVisible(showSeriesShapes);
		    for(int i=0;i<colors.length;i++)
		        renderer.setSeriesPaint(i,colors[i]);
		    
	    	ValueAxis xAxis = plot.getDomainAxis();
	    	xAxis.setVerticalTickLabels(verticalCatLabel);
		    
		    ValueAxis yAxis = plot.getRangeAxis();
		    yAxis.setUpperMargin(upperMarginPercent);
		    if(lowerMarginPercent >= 0)
		    	yAxis.setLowerMargin(lowerMarginPercent);
		    if(yMaxValue != null)
	    		yAxis.setUpperBound(yMaxValue.doubleValue());
	    	if(yMinValue != null)
	    		yAxis.setLowerBound(yMinValue.doubleValue());
	    	
	        data.setFormat((DateAxis)plot.getDomainAxis(),timeFormat,getLanguage());
	        
		}else if(dataType.equals(pDataType_XY)){

			String[][] xValues = getTwoDimParam(pXValues);
			
			XYDataset data = new XYDataset(series,xValues,yValues);			
		    chart = ChartFactory.createXYLineChart(title,xAxisLabel,yAxisLabel,data,
		    									   PlotOrientation.VERTICAL,showLegend,false,false);
		    
	        XYPlot plot = chart.getXYPlot();
	        
	        XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer)plot.getRenderer();
	        renderer.setItemLabelGenerator(new StandardXYItemLabelGenerator());
	        renderer.setItemLabelsVisible(showValues);
	        renderer.setBaseShapesVisible(showSeriesShapes);
		    for(int i=0;i<colors.length;i++)
		        renderer.setSeriesPaint(i,colors[i]);
		    
	    	ValueAxis xAxis = plot.getDomainAxis();
	    	if(xMaxValue != null)
	    		xAxis.setUpperBound(xMaxValue.doubleValue());
	    	if(xMinValue != null)
	    		xAxis.setLowerBound(xMinValue.doubleValue());

	    	ValueAxis yAxis = plot.getRangeAxis();
		    yAxis.setUpperMargin(upperMarginPercent);
		    if(lowerMarginPercent >= 0)
		    	yAxis.setLowerMargin(lowerMarginPercent);
		    if(yMaxValue != null)
	    		yAxis.setUpperBound(yMaxValue.doubleValue());
	    	if(yMinValue != null)
	    		yAxis.setLowerBound(yMinValue.doubleValue());
		    			
		    if(!autoRangeIncludesZero){
		        NumberAxis numberaxis = (NumberAxis)plot.getRangeAxis();
		        numberaxis.setAutoRangeIncludesZero(false);
		    }
		    
		}else{
			
			String[] xValues = getOneDimParam(pXValues);
			
			CategoryDataset data = new CategoryDataset(series,xValues,yValues,false);
		    chart = ChartFactory.createLineChart(title,xAxisLabel,yAxisLabel,data,
		    									 PlotOrientation.VERTICAL,showLegend,false,false);
		    
		    CategoryPlot plot = chart.getCategoryPlot();
		    
	        LineAndShapeRenderer renderer = (LineAndShapeRenderer)plot.getRenderer();
	        renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator());
	        renderer.setItemLabelsVisible(showValues);
	        renderer.setShapesVisible(showSeriesShapes);
		    for(int i=0;i<colors.length;i++)
		    	renderer.setSeriesPaint(i,colors[i]);		    
		    		    
		    CategoryLabelPositions clp = CategoryLabelPositions.STANDARD;
		    if(verticalCatLabel)
		    	clp = CategoryLabelPositions.UP_90;		    
	        CategoryAxis xAxis = plot.getDomainAxis();
	    	xAxis.setCategoryLabelPositions(clp);

		    ValueAxis yAxis = plot.getRangeAxis();
		    yAxis.setUpperMargin(upperMarginPercent);
		    if(lowerMarginPercent >= 0)
		    	yAxis.setLowerMargin(lowerMarginPercent);
		    
		    if(yMaxValue != null)
	    		yAxis.setUpperBound(yMaxValue.doubleValue());
	    	if(yMinValue != null)
	    		yAxis.setLowerBound(yMinValue.doubleValue());
	    	
		    if(!autoRangeIncludesZero){
		        NumberAxis numberaxis = (NumberAxis)plot.getRangeAxis();
		        numberaxis.setAutoRangeIncludesZero(false);
		    }
		}
		return chart;
	}
}
