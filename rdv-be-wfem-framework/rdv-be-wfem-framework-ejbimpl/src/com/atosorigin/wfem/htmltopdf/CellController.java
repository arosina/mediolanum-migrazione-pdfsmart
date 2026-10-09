package com.atosorigin.wfem.htmltopdf;

import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public class CellController extends AbstractController{

	private int cellIndex = -1;
	private int absCalcColumnIndex = -1;
	private AbstractController cellContent;
	private PdfPCell pdfCell;
	private boolean modifyTablePivotColumn = true;

	/**************************************************************************************************/
	/**************************************************************************************************/
	public CellController(AbstractController parent, DocumentController documentController){
		super(parent,documentController);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setCellIndex(int newCellIndex) {
		cellIndex = newCellIndex;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void addImage(ImageController image){
		if(cellContent == null)
			cellContent = image;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void addTable(TableController table){
		if(cellContent == null)
			cellContent = table;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void addText(String text){
		if(cellContent == null){
			cellContent = new TextController(this,text,getDocumentController());
			cellContent.setWriter(writer);
		}else if(cellContent instanceof TextController){
			((TextController)cellContent).addText(text);
		}
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public Object generatePdfDocument() throws Exception {
	
		pdfCell = new PdfPCell(new Phrase());
		
		if(cellContent == null){
			CellPdfLayoutManager.manageCellLayout(this,pdfCell);			
			return pdfCell;
		}
			
		Object pdfCellContent = cellContent.generatePdfDocument();
		if(pdfCellContent != null){
			if(cellContent instanceof TableController){
				pdfCell = new PdfPCell((PdfPTable)pdfCellContent);
			}
			else if(cellContent instanceof TextController){
				pdfCell = new PdfPCell((Phrase)pdfCellContent);
			}
			else if(cellContent instanceof ImageController){
				pdfCell = new PdfPCell((Image)pdfCellContent);
				float h = ((Image)pdfCellContent).height();
				pdfCell.setFixedHeight(h+10);
			}
		}
	
		CellPdfLayoutManager.manageCellLayout(this,pdfCell);		
		
		return pdfCell;	
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public Object generateCalcDocument() throws Exception {
		if(cellContent == null)
			return null;
		
		DocumentController doc = getDocumentController();
		
		CalcRegion calcRegion = new CalcRegion(getParent().getCalcRegion().getStartRow(),
											   doc.getCalcGlobalColIdx(),-1,-1); 
		setCalcRegion(calcRegion);
		
		cellContent.generateCalcDocument();
		
		CalcRegion contentCalcRegion = cellContent.getCalcRegion();
		if(getAttributes().getColSpan() > contentCalcRegion.getColumns()){
			modifyTablePivotColumn = false;
			contentCalcRegion.setEndCol(contentCalcRegion.getStartCol()+getAttributes().getColSpan()-1);
		}
		
		calcRegion.setEndCol(contentCalcRegion.getEndCol());
		calcRegion.setEndRow(contentCalcRegion.getEndRow());
		
		return null;	
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public AbstractController getCellContent() {
		return cellContent;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public int getCellIndex() {
		return cellIndex;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public com.lowagie.text.pdf.PdfPCell getPdfCell() {
		return pdfCell;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public boolean isModifyTablePivotColumn() {
		return modifyTablePivotColumn;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public int getAbsCalcColumnIndex() {
		return absCalcColumnIndex;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setAbsCalcColumnIndex(int absCalcColumnIndex) {
		this.absCalcColumnIndex = absCalcColumnIndex;
	}

}
