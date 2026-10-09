package com.atosorigin.wfem.htmltopdf;

import java.util.List;
import java.util.Vector;

import com.lowagie.text.pdf.PdfPCell;

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public class RowController extends AbstractController{

	private int rowIndex = -1;
	private java.util.List rowCells = new java.util.ArrayList();

	/**************************************************************************************************/
	/**************************************************************************************************/
	public RowController(AbstractController parent, DocumentController documentController){
		super(parent,documentController);
	}
	
	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public void addCell(CellController cell) {
		cell.setCellIndex(rowCells.size());
		rowCells.add(cell);
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public void setRowIndex(int newRowIndex) {
		rowIndex = newRowIndex;
	}
	
	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public Object generatePdfDocument() throws Exception {
	
		Vector cells = new Vector();
		for(int i=0;i<rowCells.size();i++){
			CellController cell = (CellController)rowCells.get(i);
			PdfPCell pdfCell = (PdfPCell)cell.generatePdfDocument();
			cells.add(pdfCell);
		}
		return cells;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public Object generateCalcDocument() throws Exception {

		DocumentController doc = getDocumentController();
		
		CalcRegion calcRegion = new CalcRegion(doc.getCalcGlobalRowIdx(),
											   getParent().getCalcRegion().getStartCol(),-1,-1); 
		setCalcRegion(calcRegion);

		int pivotIdx=0;
		int totRows = 0;
		int totColumns = 0;
		List pivotColumnsRegion = ((TableController)getParent()).getPivotColumnsRegion();
		for(int i=0;i<rowCells.size();i++){
			
			CellController cell = (CellController)rowCells.get(i);
			CalcRegion pivotColumnRegion = (CalcRegion)pivotColumnsRegion.get(pivotIdx);
			cell.setAbsCalcColumnIndex(pivotIdx);
			doc.setCalcGlobalColIdx(pivotColumnRegion.getStartCol());
			
			cell.generateCalcDocument();
			CalcRegion cellRegion = cell.getCalcRegion();
			int cellColumns = cellRegion.getColumns();
			int cellRows = cellRegion.getRows();
			totColumns += cellColumns;
			if(cellRows > totRows)
				totRows = cellRows;

				// Setting pivot column start col
				if(cellRegion.getStartCol() > pivotColumnRegion.getStartCol())
					pivotColumnRegion.setStartCol(cellRegion.getStartCol());
				else
					cellRegion.setStartCol(pivotColumnRegion.getStartCol());
				
				if(cell.isModifyTablePivotColumn()){
					// Setting pivot column end col
					if(cellRegion.getEndCol() > pivotColumnRegion.getEndCol()){
						int delta = cellRegion.getEndCol() - pivotColumnRegion.getEndCol();
						pivotColumnRegion.setEndCol(cellRegion.getEndCol());
						for(int k=i+1;k<pivotColumnsRegion.size();k++){
							pivotColumnRegion = (CalcRegion)pivotColumnsRegion.get(k);
							pivotColumnRegion.setStartCol(pivotColumnRegion.getStartCol()+delta);
							pivotColumnRegion.setEndCol(pivotColumnRegion.getEndCol()+delta);
						}
					}else
						cellRegion.setEndCol(pivotColumnRegion.getEndCol());
				}
				
				if(cell.getAttributes().getColSpan() > 1)
					pivotIdx += cell.getAttributes().getColSpan();
				else
					pivotIdx++;
		}
		
		calcRegion.setEndRow(calcRegion.getStartRow()+(totRows-1));
		calcRegion.setEndCol(calcRegion.getStartCol()+(totColumns-1));
		
		// Align last row for all cell with the maximum row index
		for(int i=0;i<rowCells.size();i++){
			CellController cell = (CellController)rowCells.get(i);
			cell.getCalcRegion().setEndRow(calcRegion.getEndRow());
		}
		
		doc.setCalcGlobalRowIdx(calcRegion.getEndRow()+1);
		doc.setCalcGlobalColIdx(getParent().getCalcRegion().getStartCol());
		return null;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public CellController getCell(int i) {
		return (CellController)rowCells.get(i);
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public int[] getColumnsWidth() {
	
		int[] widths = new int[rowCells.size()];
	
		for(int i=0;i<rowCells.size();i++){
			CellController cell = getCell(i);
			ControllerAttributes attrs = cell.getAttributes();
			if(attrs == null)
				return null;
				
			widths[i] = attrs.getWidth();
			if(widths[i] <= 0)
				return null;
		}
		return widths;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public int getNumCells() {
		return rowCells.size();
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public int getNumColumns() {
		
		int num = rowCells.size();
		int colSpans = 0;
		
		for(int i=0;i<rowCells.size();i++){
			CellController cell = getCell(i);
			ControllerAttributes attrs = cell.getAttributes();
			if(attrs != null){
				if(attrs.getColSpan() > 1)
					colSpans += (attrs.getColSpan()-1);
			}
		}
		return num + colSpans;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public int getRowIndex() {
		return rowIndex;
	}

}
