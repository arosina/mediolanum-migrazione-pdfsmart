package com.atosorigin.wfem.htmltopdf;

import java.awt.Color;
import java.util.Hashtable;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;

import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class CalcCellStylePool {
	
	private static Pattern datePattern = Pattern.compile("[0-9]{2}-[0-9]{2}-[0-9]{4}");
	private static Pattern intPattern = Pattern.compile("(\\-)*[0-9]+");
	private static Pattern doublePattern = Pattern.compile("(\\-)*[0-9]{1,3}(\\.[0-9]{3})*(\\,[0-9]+)*");
	
	private HSSFWorkbook wb;
	private HSSFCellStyle chartRefCellStyle;
	
	private short dateFormatVal;
	private short intFormatVal;
	private short doubleFormatVal;

	private Map cellStylePool = new Hashtable();
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public CalcCellStylePool(HSSFWorkbook wb){
		this.wb = wb;
		
		HSSFDataFormat dateFormat = wb.createDataFormat();
		HSSFDataFormat intFormat = wb.createDataFormat();
		HSSFDataFormat doubleFormat = wb.createDataFormat();
		
		dateFormatVal = dateFormat.getFormat("dd-mm-yyyy");
		intFormatVal = intFormat.getFormat("0");
		doubleFormatVal = doubleFormat.getFormat("#,##0.000");
		
		chartRefCellStyle = wb.createCellStyle();
		chartRefCellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
		chartRefCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		chartRefCellStyle.setWrapText(true);
		
		HSSFFont font = wb.createFont();
		font.setFontHeightInPoints((short)8);
		font.setColor(HSSFColor.WHITE.index);
		font.setItalic(true);
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		chartRefCellStyle.setFont(font);
		
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public HSSFCellStyle getChartRefCellStyle(){
		return chartRefCellStyle;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public CalcCellStyle getTableCellStyle(CellController cell){
		
		TextController text = (TextController)cell.getCellContent();
		String cellValue = text.getText();
		
		FontAttributes fontAttr = text.getFontAttributes();
		ControllerAttributes controllerAttributes = cell.getAttributes();
		String cellFormat = getCalcCellFormat(cellValue);
		
		String key = fontAttr.getUniqueKey()+controllerAttributes.getUniqueKey()+cellFormat;
		CalcCellStyle calcCellStyle = (CalcCellStyle)cellStylePool.get(key);
		if(calcCellStyle != null)
			return calcCellStyle;
		
		HSSFCellStyle cellStyle = wb.createCellStyle();

		// Cell format
		if(cellFormat.equals("date")){
			cellStyle.setDataFormat(dateFormatVal);
		}else if(cellFormat.equals("double")){
			cellStyle.setDataFormat(doubleFormatVal);
		}else if(cellFormat.equals("int")){
			cellStyle.setDataFormat(intFormatVal);
		}
		
		// Cell border
		cellStyle.setLeftBorderColor(HSSFColor.BLACK.index);
		cellStyle.setTopBorderColor(HSSFColor.BLACK.index);
		cellStyle.setRightBorderColor(HSSFColor.BLACK.index);
		cellStyle.setBottomBorderColor(HSSFColor.BLACK.index);
		cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);		
		
		// Cell alignment
		setCellAlign(controllerAttributes,cellStyle);
		
		// Cell background
		Color bgcolor = controllerAttributes.getBackground();
		if(bgcolor != null && bgcolor.getRGB() != Color.white.getRGB()){
			HSSFColor calcColor = CalcColorMapping.getColor(bgcolor);
			cellStyle.setFillForegroundColor(calcColor.getIndex());
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
		}

		// Cell font
		setCellFont(fontAttr,cellStyle);
		
		calcCellStyle = new CalcCellStyle(key,cellStyle,cellFormat);
		cellStylePool.put(key,calcCellStyle);
		return calcCellStyle;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public CalcCellStyle getOneRowCellStyle(TextController text){
		FontAttributes fontAttr = text.getFontAttributes();
		ControllerAttributes controllerAttributes = text.getAttributes();
		String cellFormat = "string";
		
		String key = fontAttr.getUniqueKey()+controllerAttributes.getUniqueKey()+cellFormat;
		CalcCellStyle calcCellStyle = (CalcCellStyle)cellStylePool.get(key);
		if(calcCellStyle != null)
			return calcCellStyle;
		
		HSSFCellStyle cellStyle = wb.createCellStyle();
		// Cell alignment
		setCellAlign(controllerAttributes,cellStyle);
		// Cell font
		setCellFont(fontAttr,cellStyle);
		
		calcCellStyle = new CalcCellStyle(key,cellStyle,cellFormat);
		cellStylePool.put(key,calcCellStyle);
		return calcCellStyle;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getCalcCellFormat(String cellValue){
		Matcher m = datePattern.matcher(cellValue);
		if(m.matches()){
			try{
				new DateType(cellValue);
				return "date";
			}catch(Exception e){}
		}
		m = intPattern.matcher(cellValue);
		if(m.matches()){
			try{
				if(cellValue.startsWith("0"))
					return "string";				
				new Integer(cellValue);
				return "int";
			}catch(NumberFormatException nfe){
				return "string";				
			}catch(Exception e){}
		}			
		m = doublePattern.matcher(cellValue);
		if(m.matches()){
			try{
				new DoubleType(cellValue);
				return "double";
			}catch(Exception e){}
		}
		return "string";
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private void setCellAlign(ControllerAttributes controllerAttributes, HSSFCellStyle cellStyle){
		if(controllerAttributes.getAlign() == ControllerAttributes.ALIGN_LEFT)
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		else if(controllerAttributes.getAlign() == ControllerAttributes.ALIGN_RIGHT)
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		else if(controllerAttributes.getAlign() == ControllerAttributes.ALIGN_CENTER)
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		cellStyle.setWrapText(true);		
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private void setCellFont(FontAttributes fontAttr, HSSFCellStyle cellStyle){
		HSSFFont font = wb.createFont();
		font.setFontName(fontAttr.getName());
		font.setFontHeightInPoints((short)fontAttr.getPointSize());
		if(fontAttr.getColor().getRGB() != Color.black.getRGB()){
			HSSFColor calcColor = CalcColorMapping.getColor(fontAttr.getColor());
			font.setColor(calcColor.getIndex());
		}
		if(fontAttr.isBold())
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		cellStyle.setFont(font);		
	}
}
