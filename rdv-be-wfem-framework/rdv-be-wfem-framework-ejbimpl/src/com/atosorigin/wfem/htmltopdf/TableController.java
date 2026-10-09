package com.atosorigin.wfem.htmltopdf;

import java.util.Vector;

import org.apache.poi.hssf.usermodel.HSSFCell;

import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public class TableController extends AbstractController{

	public static final String HEADERROWS_INDICATOR = "HEADERROWS";
	
	private int headerRows = 0;
	private java.util.List tableRows = new java.util.ArrayList();
	private java.util.List pivotColumnsRegion = new java.util.ArrayList();
	private PdfPTable pdfTable;
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public TableController(AbstractController parent, DocumentController documentController){
		super(parent,documentController);
	}
	
	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public void addRow(RowController row) {
		row.setRowIndex(tableRows.size());
		tableRows.add(row);
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public Object generatePdfDocument() throws Exception {
	
		if(getNumRows() == 0)
			return null;
	
		RowController row = (RowController)tableRows.get(0);
		if(row.getNumCells() == 0)
			return null;
	
		int[] columnsWidth = row.getColumnsWidth();
		
		ControllerAttributes controllerAttributes = getAttributes();
		
		pdfTable = new PdfPTable(row.getNumColumns());
		if(getHeaderRows() > 0 &&
		   getNumRows() > getHeaderRows())
			pdfTable.setHeaderRows(getHeaderRows());
		
		if(controllerAttributes.getWidth() > 0)
			pdfTable.setWidthPercentage(controllerAttributes.getWidth());
		else
			pdfTable.setWidthPercentage(100);
		
		for(int i=0;i<tableRows.size();i++){
			row = (RowController)tableRows.get(i);
			Vector cells = (Vector)row.generatePdfDocument();
			for(int j=0;j<cells.size();j++){
				pdfTable.addCell((PdfPCell)cells.get(j));
			}
		}
	
		try {
	
			if(columnsWidth != null)
				pdfTable.setWidths(columnsWidth);
			
		} catch (com.lowagie.text.DocumentException e) {
		}
		
		pdfTable.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
		if(controllerAttributes.getAlign() == ControllerAttributes.ALIGN_LEFT)
			pdfTable.setHorizontalAlignment(PdfPTable.ALIGN_LEFT);
		else if(controllerAttributes.getAlign() == ControllerAttributes.ALIGN_CENTER)
			pdfTable.setHorizontalAlignment(PdfPTable.ALIGN_CENTER);
		else if(controllerAttributes.getAlign() == ControllerAttributes.ALIGN_RIGHT)
			pdfTable.setHorizontalAlignment(PdfPTable.ALIGN_RIGHT);
		return pdfTable;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public Object generateCalcDocument() throws Exception {

		if(getNumRows() == 0)
			return null;
	
		RowController rowZero = (RowController)tableRows.get(0);
		if(rowZero.getNumCells() == 0)
			return null;

		DocumentController doc = getDocumentController();

		// Table start region
		CalcRegion calcRegion = new CalcRegion(doc.getCalcGlobalRowIdx(),
											   getParent().getCalcRegion().getStartCol(),-1,-1); 
		setCalcRegion(calcRegion);
		
		// Initialize pivot colums (retrieving biggest row in table)
		RowController biggestRow = null;
		for(int i=0;i<tableRows.size();i++){
			RowController row = (RowController)tableRows.get(i);
			if(biggestRow == null)
				biggestRow = row;
			else if(row.getNumCells() > biggestRow.getNumCells())
				biggestRow = row;
		}
		for(int i=0;i<biggestRow.getNumCells();i++){
			CalcRegion cr = new CalcRegion();
			cr.setStartCol(calcRegion.getStartCol()+i);
			cr.setEndCol(cr.getStartCol());
			pivotColumnsRegion.add(cr);
		}
		
		// Create recursive info
		CalcRegion rowRegion = new CalcRegion();
		for(int i=0;i<tableRows.size();i++){
			RowController row = (RowController)tableRows.get(i);
			row.generateCalcDocument();
			rowRegion = row.getCalcRegion();
		}
		// Setting table end row region (with last row)
		calcRegion.setEndRow(rowRegion.getEndRow());

		// Put column to initial position
		doc.setCalcGlobalColIdx(calcRegion.getStartCol());

		// Setting table end col region (last pivot column)
		CalcRegion lastPivotColumnRegion = (CalcRegion)pivotColumnsRegion.get(pivotColumnsRegion.size()-1);
		calcRegion.setEndCol(lastPivotColumnRegion.getEndCol());
		
		// Imcrement row
		doc.setCalcGlobalRowIdx(calcRegion.getEndRow()+1);
		
		// Create table cells
		for(int i=0;i<tableRows.size();i++){
			RowController row = (RowController)tableRows.get(i);
			for(int j=0;j<row.getNumCells();j++){
				
				CellController cell = (CellController)row.getCell(j);
				CalcRegion pivotColumnRegion = (CalcRegion)pivotColumnsRegion.get(cell.getAbsCalcColumnIndex());
				
				AbstractController cellContent = cell.getCellContent();
				if(cellContent instanceof TextController){
					cell.getCalcRegion().setStartCol(pivotColumnRegion.getStartCol());
					if(cell.getCalcRegion().getEndCol() < pivotColumnRegion.getEndCol())
						cell.getCalcRegion().setEndCol(pivotColumnRegion.getEndCol());
					
					doc.createTableCalcCell(cell);
				}
			}
		}
		
		return null;
	}
	
	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public int getHeaderRows() {
		return headerRows;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public int getNumRows() {
		return tableRows.size();
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public com.lowagie.text.pdf.PdfPTable getPdfTable() {
		return pdfTable;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public RowController getRow(int i) {
		return (RowController)tableRows.get(i);
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public void setHeaderRows(int newHeaderRows) {
		headerRows = newHeaderRows;
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public java.util.List getPivotColumnsRegion() {
		return pivotColumnsRegion;
	}

}
