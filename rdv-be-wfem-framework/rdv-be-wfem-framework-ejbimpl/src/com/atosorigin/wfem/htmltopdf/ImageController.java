package com.atosorigin.wfem.htmltopdf;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

import com.atosorigin.wfem.charts.AbstractChart;
import com.atosorigin.wfem.charts.ChartParameters;
import com.atosorigin.wfem.controller.ControllerServlet;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.Barcode128;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public class ImageController extends AbstractController{

	private Image pdfImage;
	private String imageName;
	private ChartParameters chartParameters;

	/**************************************************************************************************/
	/**************************************************************************************************/
	public ImageController(AbstractController parent, String imageName, DocumentController documentController){
		super(parent,documentController);
		this.imageName = imageName;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public Object generatePdfDocument() throws Exception {
	
		try{
			
			if(imageName.startsWith("#") ||
			   imageName.startsWith("^")){
				
				String code = imageName.substring(1);
			    Barcode128 barcode = new Barcode128();
			    barcode.setCode(code);
			    barcode.setBarHeight(50);
			    barcode.setX(1.5f);
	
			    if(imageName.startsWith("#"))
		            pdfImage = barcode.createImageWithBarcode(writer.getDirectContent(),null,null);
		        else
	            	pdfImage = barcode.createImageWithBarcode(writer.getDirectContent(),java.awt.Color.black,java.awt.Color.white);

			    if(getAttributes().getWidth() != 0 && getAttributes().getHeight() > 0)
					pdfImage.scaleToFit(getAttributes().getWidth(),getAttributes().getHeight());
				
			}else if(imageName.startsWith("call.wfem?wfemCmd=getChart&")){
				
				Map params = new HashMap();
				String parameters = imageName.substring("call.wfem?wfemCmd=getChart&".length());
				parameters = URLDecoder.decode(parameters,"UTF-8");
				StringTokenizer st = new StringTokenizer(parameters,"&");
				while(st.hasMoreTokens()){
					String tok = st.nextToken();
					String[] pars = tok.split("\\=");
					if(pars.length == 2)
						params.put(pars[0],pars[1]);
				}
				String chartID = (String)params.get(ControllerServlet.CHART_ID_PARAMETER);
				if(chartID != null){
					if(chartParameters == null)
						chartParameters = (ChartParameters)getDocumentController().getRequestManager().getMainSession().getAttribute(chartID);
					Class chartClass = Class.forName("com.atosorigin.wfem.charts."+chartParameters.getType()+"Chart");
					AbstractChart chart = (AbstractChart)chartClass.newInstance();
					chart.setChartParameters(chartParameters);				
					chart.setLanguage(getDocumentController().getRequestManager().getLanguage());
					getDocumentController().getRequestManager().getMainSession().removeAttribute(chartID);
					JFreeChart jfreeChart = chart.createChart();
					PdfContentByte cb = writer.getDirectContent();
					PdfTemplate tp = cb.createTemplate(chart.getWidth(),chart.getHeight());
					Graphics2D g2d = tp.createGraphics(chart.getWidth(),chart.getHeight(),new DefaultFontMapper());
					Rectangle2D r2d = new Rectangle2D.Double(0,0,chart.getWidth(),chart.getHeight());
					jfreeChart.draw(g2d, r2d);
					g2d.dispose();
					pdfImage = Image.getInstance(tp);
					
					getAttributes().setWidth(chart.getWidth());
					getAttributes().setHeight(chart.getHeight());
				}
				
			}else{
	
				pdfImage = getDocumentController().loadImageAsIText(imageName,getAttributes().getWidth(),getAttributes().getHeight());
				
			}
	
		}catch(Exception e){
			pdfImage = null;
		}		
		return pdfImage;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public Object generateCalcDocument() throws Exception {
		
		if(imageName.startsWith("#") || imageName.startsWith("^"))
			return null;
		
		try{
			
			DocumentController doc = getDocumentController();
			BufferedImage imageBi = null;
			boolean isChart = false;
			String chartTitle = "";
			if(imageName.startsWith("call.wfem?wfemCmd=getChart&")){
				
				isChart = true;
				Map params = new HashMap();
				String parameters = imageName.substring("call.wfem?wfemCmd=getChart&".length());
				parameters = URLDecoder.decode(parameters,"UTF-8");
				StringTokenizer st = new StringTokenizer(parameters,"&");
				while(st.hasMoreTokens()){
					String tok = st.nextToken();
					String[] pars = tok.split("\\=");
					if(pars.length == 2)
						params.put(pars[0],pars[1]);
				}
				String chartID = (String)params.get(ControllerServlet.CHART_ID_PARAMETER);
				if(chartID != null){
					if(chartParameters == null)
						chartParameters = (ChartParameters)doc.getRequestManager().getMainSession().getAttribute(chartID);
					Class chartClass = Class.forName("com.atosorigin.wfem.charts."+chartParameters.getType()+"Chart");
					AbstractChart chart = (AbstractChart)chartClass.newInstance();
					chart.setChartParameters(chartParameters);
					chart.setLanguage(doc.getRequestManager().getLanguage());
					doc.getRequestManager().getMainSession().removeAttribute(chartID);
					JFreeChart jfreeChart = chart.createChart();
					chartTitle = jfreeChart.getTitle().getText();
					imageBi = jfreeChart.createBufferedImage(chart.getWidth(),chart.getHeight());
					
					getAttributes().setWidth(chart.getWidth());
					getAttributes().setHeight(chart.getHeight());
				}
				
			}else{

				imageBi = doc.loadImageAsBuffered(imageName,getAttributes().getWidth(),getAttributes().getHeight());
				
			}
			
			if(imageBi == null)
				return null;
	
			CalcRegion calcRegion = doc.getImageRegion(imageBi,doc.getCalcGlobalRowIdx(),doc.getCalcGlobalColIdx());
			if(isChart){
				if(chartParameters != null && chartParameters.getParameters() != null){
					String inSheet = (String)chartParameters.getParameters().get("inSheetOnExportCalc");
					if(inSheet != null && inSheet.equalsIgnoreCase("true")){
						doc.createCalcChart(chartTitle,ChartUtilities.encodeAsPNG(imageBi),calcRegion);
						calcRegion.setEndRow(calcRegion.getStartRow());
						calcRegion.setEndCol(calcRegion.getStartCol());
					}else{
						doc.createCalcImage(ChartUtilities.encodeAsPNG(imageBi),calcRegion);
					}
				}else{
					doc.createCalcImage(ChartUtilities.encodeAsPNG(imageBi),calcRegion);					
				}
			}else{
				doc.createCalcImage(ChartUtilities.encodeAsPNG(imageBi),calcRegion);
			}
			setCalcRegion(calcRegion);
			return null;
			
		}catch(Exception e){
			return null;
		}		
	}
}
