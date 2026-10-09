package com.atosorigin.wfem.htmltopdf;

import java.util.Vector;

import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public class PageEventsManager extends PdfPageEventHelper {

	private Vector headers = new Vector(); 
	private Vector footers = new Vector(); 
	
	private int pageNum = 0;

	private PdfBlock currentBlock = null;
	private Vector blocks = new Vector();

	/**************************************************************************************************/
	/**************************************************************************************************/
	public PageEventsManager() {
		super();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void addBlock() {
		currentBlock = new PdfBlock();
		blocks.addElement(currentBlock);
	
		pageNum = 0;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void addFooter(PageLayout pageLayout) {
	
		PageHeaderFooter pageFooter = pageLayout.getPageFooter();
	    float llx = pageLayout.getPointMarginLeft();
	    float lly = pageLayout.getHeaderFooterInterline();
	    float urx = pageLayout.getPageSize().width() - pageLayout.getPointMarginRigth();
	    float ury = pageLayout.getPointMarginBottom() - pageLayout.getHeaderFooterInterline();
		Rectangle rect = new Rectangle(llx,lly,urx,ury);
		pageFooter.setPosition(rect);
		
		footers.add(pageFooter);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void addHeader(PageLayout pageLayout) {
	
		PageHeaderFooter pageHeader = pageLayout.getPageHeader();
	    float llx = pageLayout.getPointMarginLeft();
	    float lly = pageLayout.getPageSize().height() - pageLayout.getPointMarginTop() + pageLayout.getHeaderFooterInterline();
	    float urx = pageLayout.getPageSize().width() - pageLayout.getPointMarginRigth();
	    float ury = pageLayout.getPageSize().height() - pageLayout.getHeaderFooterInterline();
		Rectangle rect = new Rectangle(llx,lly,urx,ury);
		pageHeader.setPosition(rect);
	
		headers.add(pageHeader);
		
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private PageHeaderFooter getCurrentFooter() {
		if(footers.size() > 0)
			return (PageHeaderFooter)footers.get(footers.size()-1);
		else
			return null;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private PageHeaderFooter getCurrentHeader() {
		if(headers.size() > 0)
			return (PageHeaderFooter)headers.get(headers.size()-1);
		else
			return null;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void onChapter(PdfWriter writer, Document document, float paragraphPosition, Paragraph title) {
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void onCloseDocument(PdfWriter writer, Document document) {
	
		// Headers actualization
		for(int i=0;i<headers.size();i++){
			PageHeaderFooter pageHeader = (PageHeaderFooter)headers.get(i);
			pageHeader.showText();
		}
	
		// Footers actualization
		for(int i=0;i<footers.size();i++){
			PageHeaderFooter pageFooter = (PageHeaderFooter)footers.get(i);
			pageFooter.showText();
		}
		
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void onEndPage(PdfWriter writer, Document document) {
		
		try{
	
			pageNum++;
	
			PdfContentByte cb = writer.getDirectContent();
	
			// Total pages of block
			currentBlock.setTotalPages(currentBlock.getTotalPages() + 1);
				
	        // Add header
	        PageHeaderFooter pageHeader = getCurrentHeader();
	        if(pageHeader != null){
		        pageHeader.addTemplate(cb, pageNum);
		        pageHeader.setPdfBlock(currentBlock);
	        }
	        	
	        // Add footer
	        PageHeaderFooter pageFooter = getCurrentFooter();
	        if(pageFooter != null){
		        pageFooter.addTemplate(cb, pageNum);
		        pageFooter.setPdfBlock(currentBlock);
	        }
	        
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void onOpenDocument(PdfWriter writer, Document document) {		
		addBlock();		
	}
}
