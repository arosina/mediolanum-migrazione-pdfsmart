package com.atosorigin.wfem.htmltopdf;

import com.lowagie.text.pdf.PdfWriter;

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public abstract class AbstractController {
	private DocumentController documentController;
	private AbstractController parent;
	private ControllerAttributes attributes = new ControllerAttributes();
	protected PdfWriter writer;
	private CalcRegion calcRegion = new CalcRegion();
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public AbstractController(AbstractController parent, DocumentController documentController){
		setParent(parent);
		setDocumentController(documentController);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setParent(AbstractController newParent) {
		parent = newParent;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setDocumentController(DocumentController newDocumentController) {
		documentController = newDocumentController;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void addPdfFile(String fileName) throws ControllerTypeNotSupported{
		throw new ControllerTypeNotSupported();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void addCell(CellController cell) throws ControllerTypeNotSupported{
		throw new ControllerTypeNotSupported();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void addHParagraph(HParagraphController hParagraphController) throws ControllerTypeNotSupported{
		throw new ControllerTypeNotSupported();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void addImage(ImageController imageName) throws ControllerTypeNotSupported{
		throw new ControllerTypeNotSupported();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void addNewLine() throws HtmlTagNotSupported{
		throw new HtmlTagNotSupported();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void addNewPage() throws HtmlTagNotSupported{
		throw new HtmlTagNotSupported();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void addPageLayout(PageLayout pageLayout) throws CommentTagNotSupported{
		throw new CommentTagNotSupported();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void addParagraph(ParagraphController paragraph) throws ControllerTypeNotSupported{
		throw new ControllerTypeNotSupported();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void addRow(RowController row) throws ControllerTypeNotSupported{
		throw new ControllerTypeNotSupported();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void addTable(TableController table) throws ControllerTypeNotSupported{
		throw new ControllerTypeNotSupported();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void addText(String text) throws ControllerTypeNotSupported{
		throw new ControllerTypeNotSupported();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void addTitle(DocumentTitleController title) throws ControllerTypeNotSupported{
		throw new ControllerTypeNotSupported();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public abstract Object generatePdfDocument() throws Exception;
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public abstract Object generateCalcDocument() throws Exception;
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public ControllerAttributes getAttributes() {
		return attributes;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public DocumentController getDocumentController() {
		return documentController;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public AbstractController getParent() {
		return parent;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public com.lowagie.text.pdf.PdfWriter getWriter() {
		return writer;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setAttributes(ControllerAttributes newAttributes) {
		attributes = newAttributes;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setWriter(com.lowagie.text.pdf.PdfWriter newWriter) {
		writer = newWriter;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public CalcRegion getCalcRegion() {
		return calcRegion;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setCalcRegion(CalcRegion calcRegion) {
		this.calcRegion = calcRegion;
	}
}
