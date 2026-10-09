package com.atosorigin.wfem.charts;

import java.awt.Color;
import java.awt.Font;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.util.Rotation;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class PieChart extends AbstractChart {

	private static final String pSegments       	= "segments";
	private static final String pSegmentsColors 	= "segmentsColors";
	private static final String pSegmentsValues 	= "segmentsValues";
	
	private static final String pShow3D 				= "show3D";
	private static final String pShowPercent  			= "showPercent";
	private static final String pExtractValue 			= "extractValue";
	private static final String pExtractRadio 			= "extractRadio";	
	private static final String pStartAngle3D 			= "startAngle3D";	
	private static final String pLabelFormat 			= "labelFormat";	
	private static final String pLabelFont 				= "labelFont";	
	private static final String pLegendLabelFormat 		= "legendLabelFormat";	
	private static final String pBorderColor			= "borderColor";	
	private static final String pSectionBorderVisible	= "sectionBorderVisible";
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	protected JFreeChart createChart(String title, String xAxisLabel, String yAxisLabel){
		boolean showLegend = false;
	
		boolean show3D      	= getBoolParam(pShow3D,false);
		boolean percentOn   	= getBoolParam(pShowPercent,false);
		
		String[] segments       = getOneDimParam(pSegments);
		String[] segmentsValues = getOneDimParam(pSegmentsValues);
		
		int extractValue 		 		= getIntParam(pExtractValue,-1);
		double extractRadio 	 		= getDoubleParam(pExtractRadio,new Double(0.3)).doubleValue();
		double startAngle3D 	 		= getDoubleParam(pStartAngle3D,new Double(290)).doubleValue();
		String labelFormat 		 		= getParam(pLabelFormat,null);
		String legendLabelFormat 		= getParam(pLegendLabelFormat,null);
		Font labelFont 		 	 		= getFont(getParam(pLabelFont,null));
		Color borderColor 		 		= getColorParam(pBorderColor,null); 
		boolean sectionBorderVisible	= getBoolParam(pSectionBorderVisible,true);
		
		if(percentOn){
			if(labelFormat == null)
				labelFormat = "{0} ({2})";
			if(legendLabelFormat == null)
				legendLabelFormat = "{0}";			
		}
		
		String[] segmentsColors 	= getOneDimParam(pSegmentsColors);		
		Color[]  colors = getColors(segmentsColors); 	 
		
		PieDataset data = new PieDataset(segments,segmentsValues);
	
	    JFreeChart chart = null;
	    PiePlot plot = null;
	    if(show3D){
	    	chart = ChartFactory.createPieChart3D(title,data,showLegend,false,false);
	    	plot = (PiePlot3D)chart.getPlot();
	    	plot.setStartAngle(startAngle3D);
	    	plot.setDirection(Rotation.CLOCKWISE);
	    	plot.setForegroundAlpha(0.5F);
	    }else{
	    	chart = ChartFactory.createPieChart(title,data,showLegend,false,false);
		    plot = (PiePlot) chart.getPlot();
	    }

	    plot.setSectionOutlinesVisible(sectionBorderVisible);
	    
	    if(borderColor != null)
	    	plot.setOutlinePaint(borderColor);
	    
        for(int i=0;i<colors.length;i++)
	        plot.setSectionPaint(i,colors[i]);
	    
        if(labelFont != null)
        	plot.setLabelFont(labelFont);
        
        if(labelFormat != null){
        	if(labelFormat.equalsIgnoreCase("none"))
        		plot.setLabelGenerator(null);
        	else
        		plot.setLabelGenerator(new StandardPieSectionLabelGenerator(labelFormat));
        }
        if(legendLabelFormat != null)
        	plot.setLegendLabelGenerator(new StandardPieSectionLabelGenerator(legendLabelFormat));
	    
		if(extractValue >= 0)
		    plot.setExplodePercent(extractValue,extractRadio);
	
		return chart;
	}
}
