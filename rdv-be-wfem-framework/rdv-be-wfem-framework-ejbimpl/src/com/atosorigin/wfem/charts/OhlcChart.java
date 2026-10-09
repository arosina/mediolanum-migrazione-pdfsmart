package com.atosorigin.wfem.charts;

import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.HighLowRenderer;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.data.time.MovingAverage;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class OhlcChart extends AbstractChart {

	private static final String pSerie       			 	= "serie";
	private static final String pSerieColor 			 	= "serieColor";

	private static final String pDates	 					= "dates";
	private static final String pOhlcvValues 				= "ohlcvValues";
	
	private static final String pVerticalXAxisLabel   		= "verticalXAxisLabels";
	private static final String pUpperMarginPercent		 	= "upperMarginPercent";
	private static final String pLowerMarginPercent		 	= "lowerMarginPercent";
	
	private static final String pOpenColor 			 		= "openColor";
	private static final String pCloseColor 			 	= "closeColor";
	
	private static final String pShowAverage 			 	= "showAverage";
	private static final String pAverageSuffix 			 	= "averageSuffix";
	private static final String pAverageColor 			 	= "averageColor";
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	protected JFreeChart createChart(String title, String xAxisLabel, String yAxisLabel){
		boolean showLegend = false;
	
		String   serie     			= getParam(pSerie,"");
		String[] dates   		   	= getOneDimParam(pDates);
		String[] ohlcvValues  	   	= getOneDimParam(pOhlcvValues);

		double upperMarginPercent	= getDoubleParam(pUpperMarginPercent,new Double(0.1)).doubleValue();
		double lowerMarginPercent	= getDoubleParam(pLowerMarginPercent,new Double(-1)).doubleValue();
		boolean verticalCatLabel 	= getBoolParam(pVerticalXAxisLabel,false);

		boolean showAverage			= getBoolParam(pShowAverage,false);
		String averageSuffix 		= getParam(pAverageSuffix,"");
		Color averageColor			= getColorParam(pAverageColor,"blue");
		
		Color openColor 			= getColorParam(pOpenColor,"green");
		Color closeColor 			= getColorParam(pCloseColor,"red");
		
		Color serieColor	    	= getColorParam(pSerieColor,"blue");
		
		OhlcDataset data = new OhlcDataset(serie,dates,ohlcvValues);
	
        JFreeChart chart = ChartFactory.createHighLowChart(title,xAxisLabel,yAxisLabel,data,showLegend);
        
        XYPlot plot = (XYPlot)chart.getPlot();
        
        HighLowRenderer renderer = (HighLowRenderer)plot.getRenderer();
    	renderer.setSeriesPaint(0,serieColor);
        renderer.setOpenTickPaint(openColor);
        renderer.setCloseTickPaint(closeColor);

        DateAxis xAxis = (DateAxis)plot.getDomainAxis();
        xAxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
    	xAxis.setVerticalTickLabels(verticalCatLabel);
    	
    	if(showAverage){
	        org.jfree.data.xy.XYDataset xydataset = MovingAverage.createMovingAverage(data," "+averageSuffix,1,0);
	        plot.setDataset(1,xydataset);
	        StandardXYItemRenderer ir = new StandardXYItemRenderer();
	        ir.setPaint(averageColor);
	        plot.setRenderer(1,ir);
    	}
    	
	    ValueAxis yAxis = plot.getRangeAxis();
	    yAxis.setUpperMargin(upperMarginPercent);
	    if(lowerMarginPercent >= 0)
	    	yAxis.setLowerMargin(lowerMarginPercent);
	    
        return chart;
	}
}
