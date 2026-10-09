package com.atosorigin.wfem.htmltopdf;

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public class CalcRegion {
	private int startRow=-1;
	private int startCol=-1;
	private int endRow=-1;
	private int endCol=-1;
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public CalcRegion(){
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public CalcRegion(int startRow, int startCol, int endRow, int endCol){
		this.startRow = startRow;
		this.startCol = startCol;
		this.endRow = endRow;
		this.endCol = endCol;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public int getColumns(){
		if(startRow < 0)
			return 0;
		return (endCol-startCol)+1;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public int getRows(){
		if(startRow < 0)
			return 0;
		return (endRow-startRow)+1;
	}

	public int getEndCol() {
		return endCol;
	}

	public void setEndCol(int endCol) {
		this.endCol = endCol;
	}

	public int getEndRow() {
		return endRow;
	}

	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}

	public int getStartCol() {
		return startCol;
	}

	public void setStartCol(int startCol) {
		this.startCol = startCol;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}
}
