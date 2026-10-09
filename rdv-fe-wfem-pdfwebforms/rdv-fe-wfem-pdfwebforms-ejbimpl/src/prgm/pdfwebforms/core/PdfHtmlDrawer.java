package prgm.pdfwebforms.core;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.util.FacadeLoader;

import prgm.pdfwebforms.backend.PdfInstanceFacade;
import prgm.pdfwebforms.model.PdfModel;
import prgm.pdfwebforms.model.PdfPersonModel;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class PdfHtmlDrawer{

	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void drawPdf(Writer out, String browserInstance, Template t, PdfModel pdf, boolean readonly) throws IOException{
		innerDrawPdf(out, browserInstance, t, pdf, readonly, null, false);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void drawPdfForSign(Writer out, String browserInstance, Template t, PdfModel pdf, PdfPersonModel person) throws IOException{
		innerDrawPdf(out, browserInstance, t, pdf, true, person, false);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public static void drawAllPdf(Writer out, String browserInstance, Template t, PdfModel pdf) throws IOException{
		if(pdf.isMultiPdf()){
			try{
				ClientSessionContext csc = t.getPageDataModel().getUserSessionContext().getClientSessionContext();

				PdfInstanceFacade facade = (PdfInstanceFacade)FacadeLoader.getFacade(csc, PdfInstanceFacade.class);
				pdf = facade.gotoPdf(csc, pdf, 0, false);
				
				float totWidth = pdf.getMaxPageWidth();
				totWidth *= pdf.getScaleFactor().doubleValue();
				totWidth = Math.round(totWidth); 

				float totHeight = 0;
				for(int i=1;i<=pdf.getNumPages();i++)
					totHeight += pdf.getPageHeight(i)*pdf.getScaleFactor().doubleValue();
				totHeight = Math.round(totHeight); 
				
				out.write("<div id='pagesCont' style='position: relative;height:100%;width:100%;margin:0 auto;overflow:auto;'>");
				out.write(	"<div style='position:absolute;left:0;top:0;height:"+(int)totHeight+"px;width:"+(int)totWidth+"px;margin:0;'>");
				out.write(		"<div id='fieldsCont' style='position:absolute;left:0;top:0;margin:0;'>");

				innerDrawPdf(out, browserInstance, t, pdf, true, null, true);
				for(int i=1;i<pdf.getPdfData().getPdfs().size();i++){
					pdf = facade.gotoPdf(csc, pdf, i, false);
					innerDrawPdf(out, browserInstance, t, pdf, true, null, true);
				}
				
				out.write(		"</div>");
				out.write(	"</div>");
				out.write("</div>");
				
			}catch(Exception e){
				throw new IOException(e.toString());
			}
		}else{
			innerDrawPdf(out, browserInstance, t, pdf, true, null, false);
		}
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static void innerDrawPdf(Writer out, String browserInstance, Template t, PdfModel pdf, boolean readonly, 
									 PdfPersonModel person, boolean forAllPdf) throws IOException{
		
		ArrayList<String> visiblePagesAsArray = null; 
		if(!pdf.getPdfData().getVisiblePages().isNull()){
			String[] visiblePages = pdf.getPdfData().getVisiblePages().toString().split("\\,");
			visiblePagesAsArray = new ArrayList<String>(Arrays.asList(visiblePages)); 
		}
		
		float totWidth = pdf.getMaxPageWidth();
		totWidth *= pdf.getScaleFactor().doubleValue();
		totWidth = Math.round(totWidth); 

		float totHeight = 0;
		for(int i=1;i<=pdf.getNumPages();i++){
			if(visiblePagesAsArray != null && !visiblePagesAsArray.contains(""+i))
				totHeight += 0;
			else
				totHeight += pdf.getPageHeight(i)*pdf.getScaleFactor().doubleValue();
		}
		totHeight = Math.round(totHeight); 
		
		int containerId = 1;

		if(!forAllPdf){
			out.write("<div style='position:relative;height:100%;width:100%;margin:0 auto;'>");
			out.write("<div id='pagesCont' style='text-align:left;position:relative;height:100%;width:100%;margin:0 auto;overflow:auto;'>");
			out.write(	"<div id='pdfPagesCont' class='pdf_pages_cont' style='height:"+(int)totHeight+"px;width:"+(int)totWidth+"px;'>");
			out.write(		"<div id='fieldsCont' style='position:absolute;left:0;top:0;margin:0;'>");
		}
		
		String positionHelp = pdf.isTestMode() ? " ondblclick='doPositionHelper(event,this);'" : "";
		int pageOffset = 0;
		for(int i=1;i<=pdf.getNumPages();i++){
					float width = pdf.getPageWidth(i);
					width *= pdf.getScaleFactor().doubleValue(); 
					width = Math.round(width);
					float height = pdf.getPageHeight(i);
					height *= pdf.getScaleFactor().doubleValue(); 
					height = Math.round(height);
					if(visiblePagesAsArray != null && !visiblePagesAsArray.contains(""+i)){
						height = 0;
						out.write("<div style='display:none;position:relative;margin:0;height:"+(int)height+"px;'>");
					}else{
						out.write("<div style='position:relative;margin:0;height:"+(int)height+"px;'>");
					}
					out.write(		"<img aria-hidden='true' width='"+(int)width+"px' height='"+(int)height+"' ");
					out.write(				"src='call.wfem?wfemCmd=prgm.pdfwebforms.business.PageImage.execute&readRequest=false&pdfOnWork="+pdf.isPdfOnWork()+"&pdfId="+pdf.getPdfData().getPdfId()+"&pdfPublicationId="+pdf.getPdfData().getPdfPublicationId()+"&pageImgIdx="+i+"&BrowserInstance="+browserInstance+"'>");  
					out.write(		"<div "+positionHelp+" pageNum="+i+" class='fieldContainer' style='position:absolute;left:0;top:0;margin:0;height:"+(int)height+"px;width:"+(int)width+";'>");
					if(person != null){
						containerId = PdfHtmlFieldsDrawer.drawPersonSignFields(out, t, pdf, i, pageOffset, (int)height, person, containerId);
					}else{
						containerId = PdfHtmlFieldsDrawer.drawFields(out,t, pdf, i, pageOffset, (int)height, containerId, readonly);
						if(!readonly && pdf.getPdfData().getPdfPageDriver() != null){
							out.write(pdf.getPdfData().getPdfPageDriver().drawPageActions(i));
							PdfHtmlFieldsDrawer.drawActions(out,t, pdf, i, pageOffset);
						}
					}
					out.write(		"</div>");
					out.write("</div>");
					pageOffset += height;
					
		}
		
		if(!forAllPdf){				
			out.write(		"</div>");
			out.write(		"<div id='warningsStruct' style='position: absolute; left: 0; top: 0; width:260px; z-index:2001; display:none;'>");
			out.write(			"<table width='100%' cellpadding='0' cellspacing='0'><tr><td>");
			out.write(				"<tr>");
			out.write(					"<td>");
			out.write(						"<table width='100%' style='background-color: white; border: red 1px solid; table-layout: fixed;'>");
			out.write(							"<col width='15%'><col width='*'>");
			out.write(							"<tr>");
			out.write(								"<td align='center' valign='top' style='padding-top:5;'><img src='"+t.getWebApp()+"/images/warning.jpg'></td>");
			out.write(								"<td id='warningText' class='text' style='padding:3; font-size: 12px; color: #666666;'></td>");
			out.write(							"</tr>");
			out.write(						"</table>");
			out.write(					"</td>");
			out.write(				"</tr>");
			out.write(				"<tr><td valign='top'><div id='warningArrow' style='position:relative;top:-1;color:black;'><img src='"+t.getWebApp()+"/images/puntaWarning.png'></div></td></tr>");
			out.write(			"</table>");
			out.write(		"</div>");
			out.write(	"</div>");
			if(person == null) // Non on sign page
				PdfHtmlFieldsDrawer.drawHiddenFields(out,t,pdf);
			out.write("</div>");
			out.write("<div id='errorsMarkers' style='position:absolute;left:0;top:0;height:100%;width:1px;margin:0 auto;z-index:1;'></div>");
			out.write("<div id='errorsMarkersTitle' class='errorsMarkersTitle' style='display:none;position:absolute;left:0;top:-15;margin:0 auto;'></div>");
			out.write("</div>");
		}
		return;
	}	
	
}
