package com.atosorigin.wfem.htmltopdf;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public class CalcCellStyle {
	
	String		  key;
	HSSFCellStyle cellStyle;
	String		  cellFormat="string";
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public CalcCellStyle(String key, HSSFCellStyle cellStyle, String cellFormat){
		this.key = key;
		this.cellStyle = cellStyle;
		this.cellFormat = cellFormat;
	}
	
	public String getCellFormat() {
		return cellFormat;
	}
	public HSSFCellStyle getCellStyle() {
		return cellStyle;
	}
	public String getKey() {
		return key;
	}
}
