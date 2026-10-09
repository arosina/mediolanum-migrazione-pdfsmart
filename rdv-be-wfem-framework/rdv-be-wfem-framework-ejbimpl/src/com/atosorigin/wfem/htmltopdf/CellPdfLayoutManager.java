package com.atosorigin.wfem.htmltopdf;

import java.awt.Color;

import com.lowagie.text.Cell;
import com.lowagie.text.pdf.PdfPCell;

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public class CellPdfLayoutManager {

	/**************************************************************************************************/
	/**************************************************************************************************/
	public static void manageCellLayout(CellController cell, PdfPCell pdfCell) {
		
		pdfCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		
		manageAttributes(cell,pdfCell);
		manageBorder(cell,pdfCell);
		manageBackgroundColor(cell,pdfCell);		
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private static void manageAttributes(CellController cell, PdfPCell pdfCell) {
		
		ControllerAttributes controllerAttributes = cell.getAttributes();
		
		if(controllerAttributes.getColSpan() > 1)
			pdfCell.setColspan(controllerAttributes.getColSpan());
		
		if(controllerAttributes.isNoWrap())
			pdfCell.setNoWrap(controllerAttributes.isNoWrap());
	
		if(controllerAttributes.getAlign() == ControllerAttributes.ALIGN_LEFT)
			pdfCell.setHorizontalAlignment(PdfPCell.ALIGN_LEFT);
		else if(controllerAttributes.getAlign() == ControllerAttributes.ALIGN_RIGHT)
			pdfCell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
		else if(controllerAttributes.getAlign() == ControllerAttributes.ALIGN_CENTER)
			pdfCell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
		
		if(controllerAttributes.getHeight() >= 1)
			pdfCell.setFixedHeight(controllerAttributes.getHeight());
		
		if(controllerAttributes.getValign() == ControllerAttributes.ALIGN_MIDDLE)
			pdfCell.setVerticalAlignment(PdfPCell.ALIGN_MIDDLE);
		else if(controllerAttributes.getValign() == ControllerAttributes.ALIGN_TOP)
			pdfCell.setVerticalAlignment(PdfPCell.ALIGN_TOP);
		else if(controllerAttributes.getValign() == ControllerAttributes.ALIGN_BOTTOM)
			pdfCell.setVerticalAlignment(PdfPCell.ALIGN_BOTTOM);
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private static void manageBackgroundColor(CellController cell, PdfPCell pdfCell) {
		
		ControllerAttributes controllerAttributes = cell.getAttributes();	
		Color bg = controllerAttributes.getBackground();
		if(bg != null){
			pdfCell.setBackgroundColor(bg);
		}else{
			controllerAttributes = cell.getParent().getParent().getAttributes();
			bg = controllerAttributes.getBackground();
			if(bg != null)
				pdfCell.setBackgroundColor(bg);
		}
			
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private static void manageBorder(CellController cell, PdfPCell pdfCell) {
		
		pdfCell.setBorder(Cell.NO_BORDER);
	
		ControllerAttributes controllerAttributes = cell.getAttributes();
		if(controllerAttributes.getHeight() == 1)
			return;
		
		RowController row = (RowController)cell.getParent();
		TableController table = (TableController)row.getParent();
		
		int border = controllerAttributes.getBorder();
		if(border >= 1){
			pdfCell.setBorder(Cell.BOX);
			float fborder = ((float)border / 10.0f) * (float)border;
			pdfCell.setBorderWidth(fborder);
		}else if(border < 0){
			controllerAttributes = table.getAttributes();
			border = controllerAttributes.getBorder();
			if(border >= 1){
				pdfCell.setBorder(Cell.BOX);
				float fborder = ((float)border / 10.0f) * (float)border;
				pdfCell.setBorderWidth(fborder);
			}
		}else if(border == 0){
			controllerAttributes = table.getAttributes();
			border = controllerAttributes.getBorder();
			if(border >= 1){
				float fborder = ((float)border / 10.0f) * (float)border;
				pdfCell.setBorderWidth(fborder);
				drawTableBorder(cell,pdfCell);
			}	
		}
	
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private static void drawTableBorder(CellController cell, PdfPCell pdfCell) {
		
		RowController row = (RowController)cell.getParent();
		TableController table = (TableController)row.getParent();
	
		int numRows = table.getNumRows();
		int numCols = row.getNumCells();
		int curRow = row.getRowIndex();
		int curCol = cell.getCellIndex();
	
		// There is only one row in the table
		if(curRow == 0 && numRows == 1){
			if(curCol == 0){
				pdfCell.setBorder(Cell.LEFT | Cell.TOP | Cell.BOTTOM);
			}else if(curCol == numCols-1){
				pdfCell.setBorder(Cell.RIGHT | Cell.TOP | Cell.BOTTOM);
			}else{
				pdfCell.setBorder(Cell.TOP | Cell.BOTTOM);
			}
			return;
		}
	
		// There are more than one row in the table
		if(curRow == 0){
			if(curCol == 0){
				pdfCell.setBorder(Cell.LEFT | Cell.TOP);
			}else if(curCol == numCols-1){
				pdfCell.setBorder(Cell.RIGHT | Cell.TOP);
			}else{
				pdfCell.setBorder(Cell.TOP);
			}
		}else if(curRow == numRows-1){
			if(curCol == 0){
				pdfCell.setBorder(Cell.LEFT | Cell.BOTTOM);
			}else if(curCol == numCols-1){
				pdfCell.setBorder(Cell.RIGHT | Cell.BOTTOM);
			}else{
				pdfCell.setBorder(Cell.BOTTOM);
			}
		}else{
			if(curCol == 0){
				pdfCell.setBorder(Cell.LEFT);
			}if(curCol == numCols-1){
				pdfCell.setBorder(pdfCell.border() | Cell.RIGHT);
			}if(curCol > 0 && curCol < numCols-1){
				pdfCell.setBorder(Cell.NO_BORDER);
			}
		}
		
	}

}
