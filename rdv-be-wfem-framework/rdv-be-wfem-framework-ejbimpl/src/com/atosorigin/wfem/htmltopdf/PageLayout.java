package com.atosorigin.wfem.htmltopdf;

import com.lowagie.text.*;

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public class PageLayout {

	private String pageFormat      = "A4";
	private String pageOrientation = "vertical";
	private Rectangle pageSize;
	private boolean newBlock = false;
	
	private PageHeaderFooter pageHeader = null;
	private PageHeaderFooter pageFooter = null;
	
	private double inchMarginLeft   = 0.5;
	private double inchMarginRigth  = 0.5;
	private double inchMarginTop    = 0.5;
	private double inchMarginBottom = 0.5;
	
	private float headerFooterInterline = 3;
	private FontAttributes pageDefaultFontAttributes = new FontAttributes(null);

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public PageLayout(){
		
		super();
	
		initPageSize();
		
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public PageLayout(PageLayout pageLayout){
			                       
		this.inchMarginTop    = pageLayout.getInchMarginTop();
		this.inchMarginBottom = pageLayout.getInchMarginBottom();
		this.inchMarginLeft   = pageLayout.getInchMarginLeft();
		this.inchMarginRigth  = pageLayout.getInchMarginRigth();
		
		this.headerFooterInterline = pageLayout.getHeaderFooterInterline();
		
		this.pageFormat        = pageLayout.getPageFormat();
		this.pageOrientation   = pageLayout.getPageOrientation();
		this.pageSize          = pageLayout.getPageSize();
	
		this.pageDefaultFontAttributes = pageLayout.getPageDefaultFontAttributes();
		
		if(pageLayout.getPageHeader() != null)
			this.pageHeader = new PageHeaderFooter(pageLayout.getPageHeader());
		if(pageLayout.getPageFooter() != null)
			this.pageFooter = new PageHeaderFooter(pageLayout.getPageFooter());
		
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public float getHeaderFooterInterline() {
		return headerFooterInterline;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public double getInchMarginBottom() {
		return inchMarginBottom;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public double getInchMarginLeft() {
		return inchMarginLeft;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public double getInchMarginRigth() {
		return inchMarginRigth;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public double getInchMarginTop() {
		return inchMarginTop;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public FontAttributes getPageDefaultFontAttributes() {
		return pageDefaultFontAttributes;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public PageHeaderFooter getPageFooter() {
		return pageFooter;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public java.lang.String getPageFormat() {
		return pageFormat;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public PageHeaderFooter getPageHeader() {
		return pageHeader;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public java.lang.String getPageOrientation() {
		return pageOrientation;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public Rectangle getPageSize() {
		initPageSize();
		return pageSize;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public float getPointMarginBottom() {
		return new Float(72 * inchMarginBottom).floatValue();
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public float getPointMarginLeft() {
		return new Float(72 * inchMarginLeft).floatValue();
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public float getPointMarginRigth() {
		return new Float(72 * inchMarginRigth).floatValue();
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public float getPointMarginTop() {
		return new Float(72 * inchMarginTop).floatValue();
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	private void initPageSize() {	
		pageSize = PageSize.A4;
		if(pageFormat == null)
			pageFormat = "A4";
	
		if(pageFormat.equalsIgnoreCase("A0"))
			pageSize = PageSize.A0;
		else if(pageFormat.equalsIgnoreCase("A1"))
			pageSize = PageSize.A1;
		else if(pageFormat.equalsIgnoreCase("A2"))
			pageSize = PageSize.A2;
		else if(pageFormat.equalsIgnoreCase("A3"))
			pageSize = PageSize.A3;
		else if(pageFormat.equalsIgnoreCase("A5"))
			pageSize = PageSize.A5;
		else if(pageFormat.equalsIgnoreCase("A6"))
			pageSize = PageSize.A6;
		else if(pageFormat.equalsIgnoreCase("A7"))
			pageSize = PageSize.A7;
		else if(pageFormat.equalsIgnoreCase("A8"))
			pageSize = PageSize.A8;
		else if(pageFormat.equalsIgnoreCase("A9"))
			pageSize = PageSize.A9;
	
		if(pageOrientation != null && pageOrientation.equalsIgnoreCase("horizontal"))
			pageSize = pageSize.rotate();
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public boolean isNewBlock() {
		return newBlock;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public void setHeaderFooterInterline(float newHeaderFooterInterline) {
		headerFooterInterline = newHeaderFooterInterline;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public void setInchMarginBottom(double newInchMarginBottom) {
		inchMarginBottom = newInchMarginBottom;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public void setInchMarginLeft(double newInchMarginLeft) {
		inchMarginLeft = newInchMarginLeft;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public void setInchMarginRigth(double newInchMarginRigth) {
		inchMarginRigth = newInchMarginRigth;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public void setInchMarginTop(double newInchMarginTop) {
		inchMarginTop = newInchMarginTop;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public void setNewBlock(boolean newNewBlock) {
		newBlock = newNewBlock;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public void setPageDefaultFontAttributes(FontAttributes newPageDefaultFontAttributes) {
		pageDefaultFontAttributes = newPageDefaultFontAttributes;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public void setPageFooter(PageHeaderFooter newPageFooter) {
		pageFooter = newPageFooter;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public void setPageFormat(java.lang.String newPageFormat) {
		pageFormat = newPageFormat;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public void setPageHeader(PageHeaderFooter newPageHeader) {
		pageHeader = newPageHeader;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public void setPageOrientation(java.lang.String newPageOrientation) {
		pageOrientation = newPageOrientation;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public void setPageSize(Rectangle newPageSize) {
		pageSize = newPageSize;
	}
}
