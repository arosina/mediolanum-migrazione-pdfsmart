package com.atosorigin.wfem.charts;

import java.awt.Color;
import java.awt.Font;
import java.io.ByteArrayOutputStream;
import java.util.StringTokenizer;
import java.util.Vector;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.block.BlockContainer;
import org.jfree.chart.block.BorderArrangement;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.Plot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.ui.HorizontalAlignment;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.VerticalAlignment;

import com.atosorigin.wfem.layout.HtmlColorMapping;
import com.atosorigin.wfem.loggers.ControllerLogger;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public abstract class AbstractChart{
	
	transient private ControllerLogger LOG = ControllerLogger.getInstance();
	
	private static final String pTitle 	 			 	= "title";
	private static final String pSubTitles 			 	= "subTitles";
	private static final String pLegendPosition		 	= "legendPosition";
	private static final String pLegendPosition_NONE 	= "none";
	private static final String pLegendPosition_LEFT 	= "left";
	private static final String pLegendPosition_BOTTOM 	= "bottom";
	private static final String pLegendPosition_RIGHT 	= "right";
	private static final String pLegendFont          	= "legendFont";	
	private static final String pChartBackColor  		= "chartBackColor";
	private static final String pChartAreaBackColor 	= "chartAreaBackColor";
	private static final String pLegendBackColor  		= "legendBackColor";
	private static final String pXGridColor				= "xGridColor";
	private static final String pYGridColor	 			= "yGridColor";
	private static final String pXAxisLabel       		= "xAxisLabel";
	private static final String pYAxisLabel             = "yAxisLabel";	
	private static final String pHideDomainValues      	= "hideDomainValues";
	private static final String pHideRangeValues        = "hideRangeValues";	

	private int height = 0;
	private int width = 0;
	private ChartParameters chartParameters;
	private String language;
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	protected abstract JFreeChart createChart(String title, String xAxisLabel, String yAxisLabel);

	/*************************************************************************************************/
	/*************************************************************************************************/
	public JFreeChart createChart(){
		
		try{
			String   title    = getParam(pTitle,"");
			String   legendPosition = getParam(pLegendPosition,pLegendPosition_BOTTOM);
			String[] subTitles = getOneDimParam(pSubTitles);
			Color	 chartBackColor = getColorParam(pChartBackColor,"white");
			Color	 chartAreaBackColor = getColorParam(pChartAreaBackColor,"white");
			Color	 legendBackColor = getColorParam(pLegendBackColor,"white");
			Color 	 xGridColor = getColorParam(pXGridColor,"lightgrey");
			Color    yGridColor	= getColorParam(pYGridColor,"lightgrey");
			String 	 xAxisLabel = getParam(pXAxisLabel,"");
			String   yAxisLabel = getParam(pYAxisLabel,"");
			String   legendFont = getParam(pLegendFont,null);
			boolean  hideDomainValues = getBoolParam(pHideDomainValues,false);
			boolean  hideRangeValues = getBoolParam(pHideRangeValues,false);
			
			this.height = Integer.parseInt(getParam("height","300"));
			this.width = Integer.parseInt(getParam("width","300"));
			
			JFreeChart chart = createChart(title,xAxisLabel,yAxisLabel);
			if(chart == null)
				return null;
			
	        Plot plot = chart.getPlot();
	        if(plot instanceof XYPlot){
        		((XYPlot)plot).setDomainGridlinePaint(xGridColor);
        		((XYPlot)plot).setRangeGridlinePaint(yGridColor);
        		if(hideDomainValues)
        			((XYPlot)plot).getDomainAxis().setVisible(false);
        		if(hideRangeValues)
        			((XYPlot)plot).getRangeAxis().setVisible(false);
	        }else if(plot instanceof CategoryPlot){
        		((CategoryPlot)plot).setRangeGridlinePaint(yGridColor);	        	
        		if(hideDomainValues)
        			((CategoryPlot)plot).getDomainAxis().setVisible(false);
        		if(hideRangeValues)
        			((CategoryPlot)plot).getRangeAxis().setVisible(false);
	        }
    		
			if(!legendPosition.equals(pLegendPosition_NONE)){
		        LegendTitle legendtitle = new LegendTitle(chart.getPlot());
		        Font f = getFont(legendFont);
		        if(f != null)
		        	legendtitle.setItemFont(f);
		        legendtitle.setBackgroundPaint(legendBackColor);
		        BlockContainer blockcontainer = new BlockContainer(new BorderArrangement());
		        blockcontainer.setBorder(new BlockBorder(0.1D, 0.1D, 0.1D, 0.1D));
		        BlockContainer blockcontainer1 = legendtitle.getItemContainer();
		        blockcontainer.add(blockcontainer1);
		        

		        legendtitle.setWrapper(blockcontainer);

				if(legendPosition.equals(pLegendPosition_LEFT)){
			        legendtitle.setPosition(RectangleEdge.LEFT);
			        legendtitle.setVerticalAlignment(VerticalAlignment.CENTER);
			        legendtitle.setHorizontalAlignment(HorizontalAlignment.LEFT);
				}else if(legendPosition.equals(pLegendPosition_BOTTOM)){
			        legendtitle.setPosition(RectangleEdge.BOTTOM);
			        legendtitle.setVerticalAlignment(VerticalAlignment.CENTER);
			        legendtitle.setHorizontalAlignment(HorizontalAlignment.CENTER);
				}else if(legendPosition.equals(pLegendPosition_RIGHT)){
			        legendtitle.setPosition(RectangleEdge.RIGHT);
			        legendtitle.setVerticalAlignment(VerticalAlignment.CENTER);
			        legendtitle.setHorizontalAlignment(HorizontalAlignment.LEFT);
				}
				
				
		        chart.addSubtitle(legendtitle);
			}
			
			chart.setBackgroundPaint(chartBackColor);
			chart.getPlot().setBackgroundPaint(chartAreaBackColor);
			for(int i=0;i<subTitles.length;i++)
				chart.addSubtitle(new TextTitle(subTitles[i]));
			
			return chart;
			
		}catch(Exception e){
			String errMsg = "Exceptions in creating chart ["+getClass().getName()+"]: "+e;
			e = new Exception(errMsg);
			LOG.error(e);
			return null;
		}
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	public ByteArrayOutputStream createChartAsOutputStream(){
		
		try{
			
			JFreeChart chart = createChart();
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			ChartUtilities.writeChartAsPNG(out, chart, getWidth(), getHeight());
		    out.flush();
		    out.close();
		    return out;
		    
		}catch(Exception e){
			String errMsg = "Exceptions in creating chart as output stream ["+getClass().getName()+"]: "+e;
			e = new Exception(errMsg);
			LOG.error(e);
			return null;
		}		
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	protected String getParam(String name, String defaultValue){
		String value = (String)chartParameters.getParameters().get(name);
		if(value == null || value.length() == 0)
			return defaultValue;
		return value;
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	protected boolean getBoolParam(String name, boolean defaultValue){
		String value = (String)getParam(name,null);
		if(value == null)
			return defaultValue;
		return Boolean.valueOf(value).booleanValue();
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	protected int getIntParam(String name, int defaultValue){
		String value = (String)getParam(name,null);
		if(value == null)
			return defaultValue;
		try{
			return new IntegerType(value).intValue();
		}catch(NumberFormatException nfe){
			return defaultValue;
		}
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	protected Double getDoubleParam(String name, Double defaultValue){
		String value = (String)getParam(name,null);
		if(value == null)
			return defaultValue;
		try{
			return new Double(new DoubleType(value).doubleValue());
		}catch(NumberFormatException nfe){
			return defaultValue;
		}
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	protected Color getColorParam(String name, String defaultColor){
		String value = (String)getParam(name,null);
		if(value == null)
			value = defaultColor;
		if(value == null) // Default color is null. Return null
			return null;
		int	rgb = 0;
    	if(value.startsWith("#"))
    		rgb = Integer.decode(value).intValue();
    	else
    		rgb = Integer.decode(HtmlColorMapping.getColor(value)).intValue();
        return new Color(rgb);
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	protected String[] getOneDimParam(String name) {
		
		String value = (String)getParam(name,null);
		if(value == null)
			return new String[0];
			
		StringTokenizer st = new StringTokenizer(value,"|");
		String[] result = new String[st.countTokens()];
		int count=0;
		while(st.hasMoreTokens()){
			String tok = st.nextToken();
			result[count++] = tok.trim();
		}
		return result;
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	protected String[][] getTwoDimParam(String name) {
	
		String[] value = getOneDimParam(name);
		if(value.length > 0){
			String [][] result = new String[1][value.length];
			for(int i=0;i<value.length;i++){
				result[0][i] = value[i];
			}
			return result;
		}
	
		Vector rows = new Vector();
		int cols = 0;
		for(int row=0;;row++){
			String curname = name+row;
			value = getOneDimParam(curname);
			if(value.length == 0)
				break;
				
			cols = value.length;
			rows.add(value);
		}
		
		String [][] result = new String[rows.size()][cols];
		for(int r=0;r<rows.size();r++){
			String[] row = (String[])rows.get(r);
			for(int c=0;c<cols;c++){
				result[r][c] = row[c];
			}
		}
		return result;
	}

	/*************************************************************************************************/
	/*************************************************************************************************/
	public void addParameter(String name, String value) {
		chartParameters.getParameters().put(name,value);
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	protected Color[] getColors(String[] colors){
		Color[] rgbs = new Color[colors.length];
	    for(int i=0;i<colors.length;i++){
		    String sCol = colors[i];
		    if(sCol != null && !sCol.equals("")){
	    		int	rgb = 0;
		    	if(sCol.startsWith("#"))
		    		rgb = Integer.decode(sCol).intValue();
		    	else
		    		rgb = Integer.decode(HtmlColorMapping.getColor(sCol)).intValue();
		        rgbs[i] = new Color(rgb);
		    }
	    }		
	    return rgbs;
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	protected Font getFont(String fontAttr){
		try{
			if(fontAttr == null || fontAttr.length() == 0)
				return null;
			String[] attrs = fontAttr.split("\\,");
			if(attrs.length == 3)
				return new Font(attrs[0],Integer.parseInt(attrs[1]),Integer.parseInt(attrs[2]));
			else if(attrs.length == 2)
				return new Font(attrs[0],Font.PLAIN,Integer.parseInt(attrs[1]));
			return null;
		}catch(Throwable t){
			return null;
			
		}
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	protected Color getColor(String sCol){
		try{
			if(sCol == null || sCol.length() == 0)
				return null;
			
    		int	rgb = 0;
	    	if(sCol.startsWith("#"))
	    		rgb = Integer.decode(sCol).intValue();
	    	else
	    		rgb = Integer.decode(HtmlColorMapping.getColor(sCol)).intValue();
	    	return new Color(rgb);
		}catch(Throwable t){
			return null;
		}
	}
	
	/*************************************************************************************************/
	/*************************************************************************************************/
	public void setChartParameters(ChartParameters chartParameters) {
		this.chartParameters = chartParameters;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

}
