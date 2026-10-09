package com.atosorigin.wfem.charts;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.math.BigDecimal;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.util.Locale;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.DateTickMarkPosition;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.ItemLabelAnchor;
import org.jfree.chart.labels.ItemLabelPosition;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.AbstractRenderer;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.LayeredBarRenderer;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.text.G2TextMeasurer;
import org.jfree.text.TextBlock;
import org.jfree.text.TextUtilities;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.TextAnchor;
import org.jfree.util.SortOrder;

import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.util.Tools;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class BarChart extends AbstractChart {

	private static final String pDataType			 		= "dataType";
	private static final String pDataType_Categories		= "categories";
	private static final String pDataType_Time			 	= "time";
	private static final String pDataType_Time_TimeType		= "timeType";
	public  static final String pDataType_Time_TimeFormat	= "timeFormat";
	private static final String pDataType_XY				= "XY";
	
	private static final String pSeries       			 	= "series";
	private static final String pSeriesColors 			 	= "seriesColors";
	private static final String pBarColors		 			= "barColors";

	private static final String pXValues 					= "xValues";
	private static final String pYValues 					= "yValues";
	private static final String pXMaxValue 					= "xMaxValue";
	private static final String pXMinValue 					= "xMinValue";
	private static final String pYMaxValue 					= "yMaxValue";
	private static final String pYMinValue 					= "yMinValue";
	
	private static final String pVerticalXAxisLabel   		= "verticalXAxisLabels";
	private static final String pUpperMarginPercent		 	= "upperMarginPercent";
	private static final String pLowerMarginPercent		 	= "lowerMarginPercent";
	
	private static final String pCategoryUpperPercent		= "categoryUpperMarginPercent";
	private static final String pCategoryMarginPercent		= "categoryInnerMarginPercent";
	private static final String pCategoryLowerPercent		= "categoryLowerMarginPercent";
	
	private static final String pShow3D 					= "show3D";
	private static final String pShowValues              	= "showValues";
	private static final String pHorizontalBars			    = "horizontalBars";
	private static final String pShowStackedBars            = "showStackedBars";	
	private static final String pShowLayeredBars            = "showLayeredBars";	
	
	// OLD Params *************************************************************************************************
	private static final String pCategoryAsPercentLabels	= "categoryAsPercentLabels";
	private static final String pPositiveTextAnchor			= "positiveTextAnchor";
	// ************************************************************************************************************
	
	private static final String pCategoryLabelWidthRatio  	= "categoryLabelWidthRatio";	
	private static final String pCategoryLabelAlign  		= "categoryLabelAlign";	
	
	private static final String pCategoryAsPercentValues   		= "categoryAsPercentValues";
	private static final String pCategoryAsPercentValuesScale	= "categoryAsPercentValuesScale";
	
	private static final String pCategoryItemLabelAsPercent		= "categoryItemLabelAsPercent";
	private static final String pCategoryItemLabelPadding		= "categoryItemLabelPadding";	

	private static final String pPositiveItemLabelAnchor		= "positiveItemLabelAnchor";
	private static final String pNegativeItemLabelAnchor		= "negativeItemLabelAnchor";
	private static final String pPositiveItemLabelTextAnchor	= "positiveItemLabelTextAnchor";
	private static final String pNegativeItemLabelTextAnchor	= "negativeItemLabelTextAnchor";
	
	private static final String pXAxisLabelFont 			= "xAxisLabelFont";	
	private static final String pXAxisLabelColor 			= "xAxisLabelColor";	
	private static final String pYAxisLabelFont 			= "yAxisLabelFont";	
	private static final String pYAxisLabelColor 			= "yAxisLabelColor";	
	private static final String pItemLabelFont 				= "itemLabelFont";	
	private static final String pItemLabelColor				= "itemLabelColor";
	private static final String pHideZeroItem				= "hideZeroItem";

	/*************************************************************************************************/
	/*************************************************************************************************/
	public JFreeChart createChart(String title, String xAxisLabel, String yAxisLabel) {
		boolean showLegend = false;
		
		String  dataType			= getParam(pDataType,pDataType_Categories);
		
		String[]   series     	   	= getOneDimParam(pSeries);
		String[][] yValues   	   	= getTwoDimParam(pYValues);

		Double xMaxValue				= getDoubleParam(pXMaxValue,null);
		Double xMinValue				= getDoubleParam(pXMinValue,null);
		Double yMaxValue				= getDoubleParam(pYMaxValue,null);
		Double yMinValue				= getDoubleParam(pYMinValue,null);

		double upperMarginPercent	= getDoubleParam(pUpperMarginPercent,new Double(0.1)).doubleValue();
		double lowerMarginPercent	= getDoubleParam(pLowerMarginPercent,new Double(-1)).doubleValue();
		boolean show3D      	   	= getBoolParam(pShow3D,false);		
		boolean horizontalBars     	= getBoolParam(pHorizontalBars,false);
		boolean showStackedBars    	= getBoolParam(pShowStackedBars,false);
		boolean showLayeredBars    	= getBoolParam(pShowLayeredBars,false);
		boolean verticalCatLabel 	= getBoolParam(pVerticalXAxisLabel,false);
		boolean showValues       	= getBoolParam(pShowValues,false);
		boolean hideZeroItem       	= getBoolParam(pHideZeroItem,false);
		
		Font 	xLabelFont = getFont(getParam(pXAxisLabelFont,null));
		Color 	xLabelColor = getColorParam(pXAxisLabelColor,null);
		Font 	yLabelFont = getFont(getParam(pYAxisLabelFont,null));
		Color 	yLabelColor = getColorParam(pYAxisLabelColor,null);
		Font 	itemFont = getFont(getParam(pItemLabelFont,null));
		Color 	itemColor = getColorParam(pItemLabelColor,null);
		
		String[] seriesColors = getOneDimParam(pSeriesColors);
		Color[]  colors = getColors(seriesColors); 	 

	    PlotOrientation po = PlotOrientation.VERTICAL;
	    if(horizontalBars)
	    	po = PlotOrientation.HORIZONTAL;
	    
	    JFreeChart chart = null;
	    
		if(dataType.equals(pDataType_Time)){
			
			String  timeType = getParam(pDataType_Time_TimeType,TimeDataset.pTimeType_DAY);
			String  timeFormat = getParam(pDataType_Time_TimeFormat,"");
			String[] xValues = getOneDimParam(pXValues);

			TimeDataset data = new TimeDataset(series,xValues,yValues,timeType);
		    chart = ChartFactory.createXYBarChart(title,xAxisLabel,true,yAxisLabel,data,
		    									  po,showLegend,false,false);
		    
	        XYPlot plot = chart.getXYPlot();
	        XYBarRenderer renderer = (XYBarRenderer)plot.getRenderer();
	        
	        if(itemFont != null) renderer.setItemLabelFont(itemFont);
	        if(itemColor != null) renderer.setItemLabelPaint(itemColor);
		    setItemLabelPosition(renderer);
		    
	        renderer.setItemLabelGenerator(new StandardXYItemLabelGenerator());
	        renderer.setItemLabelsVisible(showValues);
		    for(int i=0;i<colors.length;i++)
		    	renderer.setSeriesPaint(i,colors[i]);
		    
	        DateAxis xAxis = (DateAxis)plot.getDomainAxis();
	        if(xLabelFont != null) xAxis.setTickLabelFont(xLabelFont);
	        if(xLabelColor != null) xAxis.setTickLabelPaint(xLabelColor);
	        xAxis.setTickMarkPosition(DateTickMarkPosition.MIDDLE);
	    	xAxis.setVerticalTickLabels(verticalCatLabel);

		    ValueAxis yAxis = plot.getRangeAxis();
	        if(yLabelFont != null) yAxis.setTickLabelFont(yLabelFont);
	        if(yLabelColor != null) yAxis.setTickLabelPaint(yLabelColor);
	        yAxis.setUpperMargin(upperMarginPercent);
		    if(lowerMarginPercent >= 0)
		    	yAxis.setLowerMargin(lowerMarginPercent);
		    
	        data.setFormat((DateAxis)plot.getDomainAxis(),timeFormat,getLanguage());
	        
		    if(yMaxValue != null)
	    		yAxis.setUpperBound(yMaxValue.doubleValue());
	    	if(yMinValue != null)
	    		yAxis.setLowerBound(yMinValue.doubleValue());
	        
	        
		}else if(dataType.equals(pDataType_XY)){
			
			String[][] xValues = getTwoDimParam(pXValues);

			XYDataset data = new XYDataset(series,xValues,yValues);
		    chart = ChartFactory.createXYBarChart(title,xAxisLabel,false,yAxisLabel,data,
		    									  po,showLegend,false,false);

	        XYPlot plot = chart.getXYPlot();
	        XYBarRenderer renderer = (XYBarRenderer)plot.getRenderer();
	        
	        if(itemFont != null) renderer.setItemLabelFont(itemFont);
	        if(itemColor != null) renderer.setItemLabelPaint(itemColor);
	        setItemLabelPosition(renderer);
		    
	        renderer.setItemLabelGenerator(new StandardXYItemLabelGenerator());
	        renderer.setItemLabelsVisible(showValues);
		    for(int i=0;i<colors.length;i++)
		    	renderer.setSeriesPaint(i,colors[i]);
		    
	    	ValueAxis xAxis = plot.getDomainAxis();
	        if(xLabelFont != null) xAxis.setTickLabelFont(xLabelFont);
	        if(xLabelColor != null) xAxis.setTickLabelPaint(xLabelColor);
	    	xAxis.setVerticalTickLabels(verticalCatLabel);
	    	if(xMaxValue != null)
	    		xAxis.setUpperBound(xMaxValue.doubleValue());
	    	if(xMinValue != null)
	    		xAxis.setLowerBound(xMinValue.doubleValue());
	    	
		    ValueAxis yAxis = plot.getRangeAxis();
	        if(yLabelFont != null) yAxis.setTickLabelFont(yLabelFont);
	        if(yLabelColor != null) yAxis.setTickLabelPaint(yLabelColor);
		    yAxis.setUpperMargin(upperMarginPercent);
		    if(lowerMarginPercent >= 0)
		    	yAxis.setLowerMargin(lowerMarginPercent);
		    if(yMaxValue != null)
	    		yAxis.setUpperBound(yMaxValue.doubleValue());
	    	if(yMinValue != null)
	    		yAxis.setLowerBound(yMinValue.doubleValue());
			
		}else{
			
			String[] xValues = getOneDimParam(pXValues);
			
			String[] barColors = getOneDimParam(pBarColors);
			if(barColors.length > 0){
				colors = getColors(barColors);
				String[][] multiValues = new String[xValues.length][xValues.length];
				series = new String[xValues.length];
				for(int i=0;i<xValues.length;i++){
					series[i] = ""+xValues[i];
					String v = yValues[0][i];
					for(int j=0;j<xValues.length;j++)
						multiValues[i][j] = i == j ? v : "0";
				}
				yValues = multiValues;
				showStackedBars = true;
				hideZeroItem = true;
			}
			
			boolean categoryAsPercentValues 	= getBoolParam(pCategoryAsPercentValues,false);
			int categoryAsPercentValuesScale 	= getIntParam(pCategoryAsPercentValuesScale,2);
			double categoryLabelWidthRatio 		= getDoubleParam(pCategoryLabelWidthRatio,new Double(0)).doubleValue(); 
			boolean categoryItemLabelAsPercent	= getBoolParam(pCategoryItemLabelAsPercent,categoryAsPercentValues);
			int categoryItemLabelPadding		= getIntParam(pCategoryItemLabelPadding,0);

			// OLD Params *************************************************************************************************
			if(!categoryItemLabelAsPercent)
				categoryItemLabelAsPercent = getBoolParam(pCategoryAsPercentLabels,categoryAsPercentValues);
			// ************************************************************************************************************
			
			double categoryMarginPercent 		= getDoubleParam(pCategoryMarginPercent,new Double(-1)).doubleValue();
			double categoryUpperMarginPercent	= getDoubleParam(pCategoryUpperPercent,new Double(-1)).doubleValue();
			double categoryLowerMarginPercent	= getDoubleParam(pCategoryLowerPercent,new Double(-1)).doubleValue();

			String categoryAlign = getParam(pCategoryLabelAlign,"right");
			
		    CategoryDataset data = new CategoryDataset(series,xValues,yValues,categoryAsPercentValues);
		    if(show3D){
				if(showStackedBars)
			 		chart = ChartFactory.createStackedBarChart3D(title,xAxisLabel,yAxisLabel,data,po,showLegend,false,false);
				else
			 		chart = ChartFactory.createBarChart3D(title,xAxisLabel,yAxisLabel,data,po,showLegend,false,false);
		    }else{
				if(showStackedBars)
			 		chart = ChartFactory.createStackedBarChart(title,xAxisLabel,yAxisLabel,data,po,showLegend,false,false);
				else
			    	chart = ChartFactory.createBarChart(title,xAxisLabel,yAxisLabel,data,po,showLegend,false,false);
		    }
		    
		    CategoryPlot plot = chart.getCategoryPlot();
		    
		    if(showLayeredBars){
		        LayeredBarRenderer layeredbarrenderer = new LayeredBarRenderer();
		        plot.setRenderer(layeredbarrenderer);
		        plot.setRowRenderingOrder(SortOrder.DESCENDING);
		    }
		    
		    BarRenderer renderer = (BarRenderer)plot.getRenderer();
		    
	        if(itemFont != null) renderer.setItemLabelFont(itemFont);
	        if(itemColor != null) renderer.setItemLabelPaint(itemColor);
		    setItemLabelPosition(renderer);
		    
	    	renderer.setItemLabelGenerator(new MyCategoryItemLabelGenerator(categoryItemLabelAsPercent,categoryAsPercentValuesScale,categoryItemLabelPadding,hideZeroItem));
		    renderer.setItemLabelsVisible(showValues);	    
		    for(int i=0;i<colors.length;i++)
		    	renderer.setSeriesPaint(i,colors[i]);
		    
		    CategoryLabelPositions clp = CategoryLabelPositions.STANDARD;
		    if(verticalCatLabel)
		    	clp = CategoryLabelPositions.UP_90;
		    
		    plot.setDomainAxes(new MyCategoryAxis[]{new MyCategoryAxis(categoryAlign)});
		    
	        CategoryAxis xAxis = plot.getDomainAxis();
	        if(xLabelFont != null) xAxis.setTickLabelFont(xLabelFont);
	        if(xLabelColor != null) xAxis.setTickLabelPaint(xLabelColor);
	        
	        if(categoryUpperMarginPercent >= 0)
	        	xAxis.setUpperMargin(categoryUpperMarginPercent);
	        if(categoryMarginPercent >= 0)
	        	xAxis.setCategoryMargin(categoryMarginPercent);
	        if(categoryLowerMarginPercent >= 0)
	        	xAxis.setLowerMargin(categoryLowerMarginPercent);
	        
	    	xAxis.setCategoryLabelPositions(clp);
	    	if(categoryLabelWidthRatio > 0)
	    		xAxis.setMaximumCategoryLabelWidthRatio((float)categoryLabelWidthRatio);
		    
		    ValueAxis yAxis = plot.getRangeAxis();
	        if(yLabelFont != null) yAxis.setTickLabelFont(yLabelFont);
	        if(yLabelColor != null) yAxis.setTickLabelPaint(yLabelColor);
	        
		    if(yAxis instanceof NumberAxis){
		    	if(categoryItemLabelAsPercent)
		    		((NumberAxis)yAxis).setNumberFormatOverride(new PercentBarFormat());
		    	else
		    		((NumberAxis)yAxis).setNumberFormatOverride(NumberFormat.getInstance(Locale.ITALY));
		    }
		    
		    yAxis.setUpperMargin(upperMarginPercent);
		    if(lowerMarginPercent >= 0)
		    	yAxis.setLowerMargin(lowerMarginPercent);
		    
		    if(yMaxValue != null)
	    		yAxis.setUpperBound(yMaxValue.doubleValue());
	    	if(yMinValue != null)
	    		yAxis.setLowerBound(yMinValue.doubleValue());

	    }
		return chart;
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	private void setItemLabelPosition(AbstractRenderer renderer){
	    ItemLabelPosition p = renderer.getPositiveItemLabelPosition();
	    String itemLabelAnchor = getParam(pPositiveItemLabelAnchor,null);
	    String textAnchor = getParam(pPositiveItemLabelTextAnchor,null);
		// OLD Params *************************************************************************************************
	    if(textAnchor == null)
	    	textAnchor = getParam(pPositiveTextAnchor,null);
		// ************************************************************************************************************
	    if(itemLabelAnchor != null && textAnchor != null)
	    	p = new ItemLabelPosition(getItemLabelAnchorInstance(itemLabelAnchor), getTextAnchorInstance(textAnchor));
	    else if(itemLabelAnchor != null)
	    	p = new ItemLabelPosition(getItemLabelAnchorInstance(itemLabelAnchor), p.getTextAnchor());
	    else if(textAnchor != null)
	    	p = new ItemLabelPosition(p.getItemLabelAnchor(), getTextAnchorInstance(textAnchor));
	    renderer.setPositiveItemLabelPosition(p);
	    
	    p = renderer.getNegativeItemLabelPosition();
	    itemLabelAnchor = getParam(pNegativeItemLabelAnchor,null);
	    textAnchor = getParam(pNegativeItemLabelTextAnchor,null);
	    if(itemLabelAnchor != null && textAnchor != null)
	    	p = new ItemLabelPosition(getItemLabelAnchorInstance(itemLabelAnchor), getTextAnchorInstance(textAnchor));
	    else if(itemLabelAnchor != null)
	    	p = new ItemLabelPosition(getItemLabelAnchorInstance(itemLabelAnchor), p.getTextAnchor());
	    else if(textAnchor != null)
	    	p = new ItemLabelPosition(p.getItemLabelAnchor(), getTextAnchorInstance(textAnchor));
	    renderer.setNegativeItemLabelPosition(p);
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	private ItemLabelAnchor getItemLabelAnchorInstance(String itemLabelAnchor){
		try{
			return (ItemLabelAnchor)ItemLabelAnchor.class.getDeclaredField(itemLabelAnchor).get(null);
		}catch(Exception e){
			e.printStackTrace();
			return new ItemLabelPosition().getItemLabelAnchor();
		}
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	private TextAnchor getTextAnchorInstance(String textAnchor){
		try{
			return (TextAnchor)TextAnchor.class.getDeclaredField(textAnchor).get(null);
		}catch(Exception e){
			e.printStackTrace();
			return new ItemLabelPosition().getTextAnchor();
		}
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	class MyCategoryAxis extends CategoryAxis{
		
		private String align;
		
		/*************************************************************************************************/
		/*************************************************************************************************/
		public MyCategoryAxis(String align){
			super();
			this.align = align;
		}
		
		/*************************************************************************************************/
		/*************************************************************************************************/
		protected TextBlock createLabel(Comparable category, float width,
										RectangleEdge edge, Graphics2D g2) {
			String lcat = category.toString();
			int numRow = lcat.split("#CR#").length;
			TextBlock label = TextUtilities.createTextBlock(lcat.replaceAll("#CR#","\n"), 
	                										getTickLabelFont(category), getTickLabelPaint(category), width,
	                										numRow, new G2TextMeasurer(g2));
			if(align.equalsIgnoreCase("LEFT"))
				label.setLineAlignment(HorizontalAlignment.LEFT);
			else
				label.setLineAlignment(HorizontalAlignment.RIGHT);
	        return label; 
		}
		
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	class MyCategoryItemLabelGenerator extends StandardCategoryItemLabelGenerator{

		private boolean asPercent = false;
		private int percentScale = 2;
		private String padding = "";
		boolean hideZeroItem = false;

		/*************************************************************************************************/
		/*************************************************************************************************/
		public MyCategoryItemLabelGenerator(boolean asPercent, int percentScale, int padding, boolean hideZeroItem){
			this.asPercent = asPercent;
			this.percentScale = percentScale;
			this.padding = Tools.fillSx("",' ',padding);
			this.hideZeroItem = hideZeroItem;
		}
		
		/*************************************************************************************************/
		/*************************************************************************************************/
		public String generateLabel(org.jfree.data.category.CategoryDataset dataset, int series, int category) {
			
			BigDecimal bigValue = new BigDecimal(dataset.getValue(series, category).doubleValue()).setScale(percentScale,BigDecimal.ROUND_HALF_UP);
			if(this.hideZeroItem && bigValue.doubleValue() == 0)
				return "";
			
			if(!asPercent){
				DoubleType d = new DoubleType(bigValue);
				return bigValue.doubleValue() > 0 ? padding+d.toString() : d.toString()+padding;
			}
			
			if(bigValue.doubleValue() == 0)
				return padding+"0%";
			String value = bigValue.toString(); 
			int idx = value.toString().indexOf(".");
			if(idx > 0){
				try{
					if(Integer.parseInt(value.toString().substring(idx+1)) == 0)
						return bigValue.doubleValue() > 0 ? padding+value.substring(0,idx)+"%" : value.substring(0,idx)+"%"+padding;
				}catch(Exception e){}
				return bigValue.doubleValue() > 0 ? padding+new DoubleType(bigValue).toString()+"%" : new DoubleType(bigValue).toString()+"%"+padding;
			}else{
				return bigValue.doubleValue() > 0 ? padding+value+"%" : value+"%"+padding;
			}
		}
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	class PercentBarFormat extends NumberFormat{

		/*************************************************************************************************/
		/*************************************************************************************************/
		public StringBuffer format(double number, StringBuffer toAppendTo, FieldPosition pos) {
			try{
				return new StringBuffer(""+new DoubleType(new BigDecimal(number).setScale(0,BigDecimal.ROUND_HALF_UP))+"%");
			}catch(Throwable t){
				return new StringBuffer(""+number);
			}
		}

		/*************************************************************************************************/
		/*************************************************************************************************/
		public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos) {
			try{
				return new StringBuffer(""+new DoubleType(new BigDecimal(number).setScale(0,BigDecimal.ROUND_HALF_UP))+"%");
			}catch(Throwable t){
				return new StringBuffer(""+number);
			}
		}

		/*************************************************************************************************/
		/*************************************************************************************************/
		public Number parse(String source, ParsePosition parsePosition) {
			throw new UnsupportedOperationException();
		}
		
	}
	
}
