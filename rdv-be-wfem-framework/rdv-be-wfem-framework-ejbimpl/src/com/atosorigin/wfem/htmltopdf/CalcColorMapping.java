package com.atosorigin.wfem.htmltopdf;

import java.awt.Color;
import java.util.Iterator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.poi.hssf.util.HSSFColor;

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public class CalcColorMapping {
	
	private static TreeMap calcColors = new TreeMap();
	static{
		Map m = HSSFColor.getTripletHash();
		Iterator i = m.keySet().iterator();
		while(i.hasNext()){
			Object key = i.next();
			HSSFColor calcColor = (HSSFColor)m.get(key);
			short[] triplet = calcColor.getTriplet();
			String r = (triplet[0] < 16 ? "0" : "" ) + Integer.toHexString(triplet[0]);
			String g = (triplet[1] < 16 ? "0" : "" ) + Integer.toHexString(triplet[1]);
			String b = (triplet[2] < 16 ? "0" : "" ) + Integer.toHexString(triplet[2]);
			calcColors.put(r+g+b,calcColor);
		}
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static HSSFColor getColor(Color color){
		String r = (color.getRed() < 16 ? "0" : "" ) + Integer.toHexString(color.getRed());
		String g = (color.getGreen() < 16 ? "0" : "") + Integer.toHexString(color.getGreen());
		String b = (color.getBlue() < 16 ? "0" : "" ) + Integer.toHexString(color.getBlue());
		SortedMap sm = calcColors.tailMap(r+g+b);
		if(sm == null || sm.isEmpty())
			return (HSSFColor)calcColors.get("FFFFFF");
		return (HSSFColor)sm.get(sm.firstKey());
	}

}
