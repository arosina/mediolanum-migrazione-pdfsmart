package com.atosorigin.wfem.htmltopdf;

import java.util.Vector;

import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public class PageHeaderFooter {
	
	private String HEADER_FOOTER_LEFT_POSITION = "LEFT";
	private String HEADER_FOOTER_CENTER_POSITION = "CENTER";
	private String HEADER_FOOTER_RIGHT_POSITION = "RIGHT";
	private String HEADER_FOOTER_LEFT_ALIGN = "LEFT";
	private String HEADER_FOOTER_CENTER_ALIGN = "CENTER";
	private String HEADER_FOOTER_RIGHT_ALIGN = "RIGHT";
	
	private DocumentController documentController;
	
	private int DEFAULT_HTML_FONT_SIZE = 1;
	private FontAttributes headerFooterFont = new FontAttributes(DEFAULT_HTML_FONT_SIZE,null);
	
    private boolean header = true;
    private PdfBlock pdfBlock;
    private Rectangle position = null;
    private String decoration = "";
    private Vector leftTextLines = new Vector();
    private Vector centerTextLines = new Vector();
    private Vector rightTextLines = new Vector();
	private Image leftImage;
	private Image centerImage;
	private Image rightImage;
	private String leftAlign = "";
	private String centerAlign = "";
	private String rightAlign = "";
	private String widths = "";
	
    private Vector headerFooterTemplates = new Vector();

    /*****************************************************************************************************/
    /*****************************************************************************************************/
    class HeaderFooterTemplate{
	    private PdfTemplate template;
	    private int pageNum;

	    public HeaderFooterTemplate(PdfTemplate template, int curPageNum){
		    this.template = template;
		    this.pageNum = curPageNum;
	    }
	    public PdfTemplate getTemplate(){
		    return template;
	    }
	    public int getPageNum(){
		    return pageNum;
	    }
    }

    /*****************************************************************************************************/
    /*****************************************************************************************************/
	public PageHeaderFooter(PageHeaderFooter pageHeaderFooter) {
		super();
	
		this.documentController = pageHeaderFooter.getDocumentController();
		
		this.leftTextLines = new Vector(pageHeaderFooter.getLeftTextLines());
		this.centerTextLines = new Vector(pageHeaderFooter.getCenterTextLines());
		this.rightTextLines = new Vector(pageHeaderFooter.getRightTextLines());
		this.leftImage = pageHeaderFooter.getLeftImage();
		this.centerImage = pageHeaderFooter.getCenterImage();
		this.rightImage = pageHeaderFooter.getRightImage();
		this.header = pageHeaderFooter.isHeader();
		this.headerFooterFont = pageHeaderFooter.getHeaderFooterFont();
		this.decoration = pageHeaderFooter.getDecoration();
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public PageHeaderFooter(boolean isHeader, DocumentController documentController) {
		super();
		this.documentController = documentController;
		this.header = isHeader;
		
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public void addTemplate(PdfContentByte cb, int curPageNumber) {
		
		PdfTemplate template = cb.createTemplate(position.width(), position.height());
		cb.addTemplate(template, position.left(), position.bottom());
	
		HeaderFooterTemplate hft = new HeaderFooterTemplate(template,curPageNumber);
		headerFooterTemplates.add(hft);
		
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public String getLeftAlign() {
		return leftAlign;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public String getCenterAlign() {
		return centerAlign;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public String getRightAlign() {
		return rightAlign;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public String getWidths() {
		return widths;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public com.lowagie.text.Image getCenterImage() {
		return centerImage;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public java.util.Vector getCenterTextLines() {
		return centerTextLines;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public java.lang.String getDecoration() {
		return decoration;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public DocumentController getDocumentController() {
		return documentController;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public FontAttributes getHeaderFooterFont() {
		return headerFooterFont;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public com.lowagie.text.Image getLeftImage() {
		return leftImage;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public java.util.Vector getLeftTextLines() {
		return leftTextLines;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public com.lowagie.text.Rectangle getPosition() {
		return position;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public com.lowagie.text.Image getRightImage() {
		return rightImage;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public java.util.Vector getRightTextLines() {
		return rightTextLines;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	private boolean isHeader() {
		return header;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	private boolean isLinedBox() {
		int idx = HtmlParser.indexOfKey(decoration, PageLayoutSyntaxConstants.HEADER_FOOTER_DECOR_LINEBOX);
		if(idx >= 0)
			return true;
		return false;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	private boolean isLinedDown() {
		int idx = HtmlParser.indexOfKey(decoration, PageLayoutSyntaxConstants.HEADER_FOOTER_DECOR_LINEDOWN);
		if(idx >= 0)
			return true;
		return false;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	private boolean isLinedLeft() {
		int idx = HtmlParser.indexOfKey(decoration, PageLayoutSyntaxConstants.HEADER_FOOTER_DECOR_LINELEFT);
		if(idx >= 0)
			return true;
		return false;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	private boolean isLinedRight() {
		int idx = HtmlParser.indexOfKey(decoration, PageLayoutSyntaxConstants.HEADER_FOOTER_DECOR_LINERIGHT);
		if(idx >= 0)
			return true;
		return false;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	private boolean isLinedUp() {
		int idx = HtmlParser.indexOfKey(decoration, PageLayoutSyntaxConstants.HEADER_FOOTER_DECOR_LINEUP);
		if(idx >= 0)
			return true;
		return false;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	private Vector manageMultipleLines(String text) {
	
		Vector textLines = new Vector();
		while(true){
			int idx = HtmlParser.indexOfKey(text,PageLayoutSyntaxConstants.HEADER_FOOTER_TEXT_CR);
			if(idx >= 0){
				textLines.add(text.substring(0,idx));
				text = text.substring(idx+PageLayoutSyntaxConstants.HEADER_FOOTER_TEXT_CR.length());
			}else{
				textLines.add(text);
				break;
			}			
		}		
		return textLines;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public void setCenterText(java.lang.String newText) {
		String upNewText = newText.toUpperCase();
		if(upNewText.startsWith(PageLayoutSyntaxConstants.HEADER_FOOTER_TEXT_IMAGE)){
			String imageName = newText.substring(PageLayoutSyntaxConstants.HEADER_FOOTER_TEXT_IMAGE.length());
			centerImage = createHeadFootImage(imageName);
			centerTextLines.removeAllElements();
			centerTextLines.trimToSize();
		}else{
			centerTextLines = manageMultipleLines(newText);
			centerImage = null;
		}
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public void setDecoration(java.lang.String newDecoration) {
		decoration = newDecoration;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public void setLeftAlign(String leftAlign) {
		this.leftAlign = leftAlign;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public void setCenterAlign(String centerAlign) {
		this.centerAlign = centerAlign;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public void setRightAlign(String rightAlign) {
		this.rightAlign = rightAlign;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public void setWidths(String widths) {
		this.widths = widths;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public void setLeftText(java.lang.String newText) {
		String upNewText = newText.toUpperCase();
		if(upNewText.startsWith(PageLayoutSyntaxConstants.HEADER_FOOTER_TEXT_IMAGE)){
			String imageName = newText.substring(PageLayoutSyntaxConstants.HEADER_FOOTER_TEXT_IMAGE.length());
			leftImage = createHeadFootImage(imageName);
			leftTextLines.removeAllElements();
			leftTextLines.trimToSize();
		}else{
			leftTextLines = manageMultipleLines(newText);
			leftImage = null;
		}
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public void setPosition(com.lowagie.text.Rectangle newPosition) {
		position = newPosition;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public void setRightText(java.lang.String newText) {
		String upNewText = newText.toUpperCase();
		if(upNewText.startsWith(PageLayoutSyntaxConstants.HEADER_FOOTER_TEXT_IMAGE)){
			String imageName = newText.substring(PageLayoutSyntaxConstants.HEADER_FOOTER_TEXT_IMAGE.length());
			rightImage = createHeadFootImage(imageName);
			rightTextLines.removeAllElements();
			rightTextLines.trimToSize();
		}else{
			rightTextLines = manageMultipleLines(newText);
			rightImage = null;
		}
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	private void showTemplate(HeaderFooterTemplate headerFooterTemplate){
		
		PdfTemplate template = headerFooterTemplate.getTemplate();
		int pageNum = headerFooterTemplate.getPageNum();
	
		if(leftImage != null)
			showTemplateImage(template,leftImage,PdfContentByte.ALIGN_LEFT);
		else
			showTemplateText(template,pageNum,leftTextLines,HEADER_FOOTER_LEFT_POSITION);
			
		if(centerImage != null)
			showTemplateImage(template,centerImage,PdfContentByte.ALIGN_CENTER);
		else
			showTemplateText(template,pageNum,centerTextLines,HEADER_FOOTER_CENTER_POSITION);
			
		if(rightImage != null)
			showTemplateImage(template,rightImage,PdfContentByte.ALIGN_RIGHT);
		else
			showTemplateText(template,pageNum,rightTextLines,HEADER_FOOTER_RIGHT_POSITION);
		
		Rectangle rect = template.getBoundingBox();
		rect.setBorderWidth(0.1f);
		rect.setBorderColor(java.awt.Color.black);
		rect.setBorder(Rectangle.NO_BORDER);
		
		if(isLinedBox()){
			rect.setBorder(rect.border() | Rectangle.BOX);
		}else{
			if(isLinedUp())
				rect.setBorder(rect.border() | Rectangle.TOP);
			if(isLinedDown())
				rect.setBorder(rect.border() | Rectangle.BOTTOM);
			if(isLinedLeft())
				rect.setBorder(rect.border() | Rectangle.LEFT);
			if(isLinedRight())
				rect.setBorder(rect.border() | Rectangle.RIGHT);
		}
	
		if(rect.border() != Rectangle.NO_BORDER)	
			template.rectangle(rect);
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	private void showTemplateImage(PdfTemplate template, Image image, int aligned){
		
		if(image == null)
			return;
			
		int INTERSPACE = 3;
			
		Rectangle rect = template.getBoundingBox();
		float imgX = ((rect.width() - image.scaledWidth()) / 2); // Center
		if(aligned == PdfContentByte.ALIGN_LEFT) // Left
			imgX = INTERSPACE;
		else if(aligned == PdfContentByte.ALIGN_RIGHT) // Right
			imgX = rect.right() - image.scaledWidth() - INTERSPACE;
				
		float imgY = ((rect.height() - image.scaledHeight()) / 2);
		image.setAbsolutePosition(imgX,imgY);
		try{
			template.addImage(image);
		}catch(Exception e){
		}
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	private void showTemplateText(PdfTemplate template, int pageNum,
								  Vector textLines, String position){
		
		if(textLines.size() == 0)
			return;
			
		int INTERSPACE = 3;
			
		Rectangle rect = template.getBoundingBox();
		BaseFont headerFooterBaseFont = headerFooterFont.getBaseFont();
		
		int numRows = textLines.size();
		float lineHeight = headerFooterFont.getPointSize() + 2;
		float textHeight = numRows * lineHeight;
		float deltaY = (rect.height() - textHeight) / 2;
		float textY = rect.top() -  deltaY - lineHeight;
	
		for(int i=0;i<numRows;i++){
	
			String text = (String)textLines.get(i);
			
			while(true){
				int idx = HtmlParser.indexOfKey(text, PageLayoutSyntaxConstants.CURRENT_PAGE_VARIABLE);
				if(idx >= 0){
					
					String s1 = text.substring(0,idx);
					String s2 = text.substring(idx+PageLayoutSyntaxConstants.CURRENT_PAGE_VARIABLE.length());
					text = s1  + pageNum + s2;
					
				}else
					break;
			}
	
			while(true){	
				int idx = HtmlParser.indexOfKey(text, PageLayoutSyntaxConstants.TOT_PAGES_VARIABLE);
				if(idx >= 0){
					
					String s1 = text.substring(0,idx);
					String s2 = text.substring(idx+PageLayoutSyntaxConstants.TOT_PAGES_VARIABLE.length());
					text = s1 + getPdfBlock().getTotalPages() + s2;
					
				}
				else
					break;
			}

			float textLen = headerFooterBaseFont.getWidthPoint(text, headerFooterFont.getPointSize());
			float textX = 0;
			
			//Widhts
			float singleRectWidth, leftRectWidth, centerRectWidth, rightRectWidth;
			if (getWidths().equals("")) {
				//Defaults
				singleRectWidth = (rect.width() - INTERSPACE*4)/3;
				leftRectWidth = singleRectWidth;
				centerRectWidth = singleRectWidth;
				rightRectWidth = singleRectWidth;
			} else {
				String[] widths = getWidths().split(",");
				leftRectWidth = ((rect.width() - INTERSPACE*4) * Integer.parseInt(widths[0]))/100;
				centerRectWidth = ((rect.width() - INTERSPACE*4) * Integer.parseInt(widths[1]))/100;;
				rightRectWidth = ((rect.width() - INTERSPACE*4) * Integer.parseInt(widths[2]))/100;;
			}
			
			if (position.equals(HEADER_FOOTER_LEFT_POSITION)){
				if (getLeftAlign().equals("")){
					textX = INTERSPACE; // Left Default
				} else {
					if (getLeftAlign().equals(HEADER_FOOTER_LEFT_ALIGN)) {
						textX = INTERSPACE;
					} else if (getLeftAlign().equals(HEADER_FOOTER_CENTER_ALIGN)) {
						textX = INTERSPACE + ((leftRectWidth - textLen) / 2);
					} else if (getLeftAlign().equals(HEADER_FOOTER_RIGHT_ALIGN)) {
						textX = INTERSPACE + leftRectWidth - textLen;
					}
				}
			} else if (position.equals(HEADER_FOOTER_CENTER_POSITION)){
				if (getCenterAlign().equals("")){
					textX = ((rect.width() - textLen) / 2); // Center Default
				} else {
					if (getCenterAlign().equals(HEADER_FOOTER_LEFT_ALIGN)) {
						textX = 2*INTERSPACE + leftRectWidth;
					} else if (getCenterAlign().equals(HEADER_FOOTER_CENTER_ALIGN)) {
						textX = 2*INTERSPACE + leftRectWidth + ((centerRectWidth - textLen) / 2);
					} else if (getCenterAlign().equals(HEADER_FOOTER_RIGHT_ALIGN)) {
						textX = 2*INTERSPACE + leftRectWidth + centerRectWidth - textLen;
					}
				}
			} else if (position.equals(HEADER_FOOTER_RIGHT_POSITION)){
				if (getRightAlign().equals("")){
					textX = rect.right() - textLen - INTERSPACE; // Right Default
				} else {
					if (getRightAlign().equals(HEADER_FOOTER_LEFT_ALIGN)) {
						textX = 3*INTERSPACE + leftRectWidth + centerRectWidth;
					} else if (getRightAlign().equals(HEADER_FOOTER_CENTER_ALIGN)) {
						textX = 3*INTERSPACE + leftRectWidth + centerRectWidth + ((rightRectWidth - textLen) / 2);
					} else if (getRightAlign().equals(HEADER_FOOTER_RIGHT_ALIGN)) {
						textX = rect.right() - textLen - INTERSPACE;
					}
				}
			}
				
			template.beginText();
			template.setFontAndSize(headerFooterBaseFont, headerFooterFont.getPointSize());
			template.setTextMatrix(textX,textY);
			template.showText(text);
			template.endText();
			
			textY -= lineHeight;
		}
	
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public void showText() {
		
		for(int i=0;i<headerFooterTemplates.size();i++){
			HeaderFooterTemplate hft = (HeaderFooterTemplate)headerFooterTemplates.get(i);
			showTemplate(hft);
		}
		
	}
	
	/*****************************************************************************************************/
    /*****************************************************************************************************/
	private Image createHeadFootImage(String imageName){
		int width=-1;
		int height=-1;
		try{
			int sizedIdx = imageName.indexOf(',');
			if(sizedIdx >= 0){
				String sdim = imageName.substring(sizedIdx+1);
				imageName = imageName.substring(0,sizedIdx);
				int sep = sdim.indexOf(':');
				width = (int)Long.parseLong(sdim.substring(0,sep));
				height = (int)Long.parseLong(sdim.substring(sep+1));
			}
		}catch(Exception e){
			width=-1; height=-1;				
		}
		Image image = documentController.loadImageAsIText(imageName,width,height);
		return image;
	}

	public PdfBlock getPdfBlock() {
		return pdfBlock;
	}

	public void setPdfBlock(PdfBlock pdfBlock) {
		this.pdfBlock = pdfBlock;
	}

}
