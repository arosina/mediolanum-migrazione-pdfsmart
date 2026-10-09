package prgm.pdfwebforms.core;

import java.io.Serializable;

import com.atosorigin.wfem.coddesc.CodDescDataList;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class PdfFieldInfos implements Serializable, Comparable<PdfFieldInfos>{
	
	public static int CHECK_NONE = 0;
	public static int CHECK_O = 1;
	public static int CHECK_V = 2;
	public static int CHECK_X = 3;
	
	public String 			pdfFieldName;
	public String 			htmlFieldName;
	public int 				fieldType;
    public int 				page;
    public String 			expValue = "";
    public String 			dataType = "StringType";
    public String 			helpText = "";
    public int 				doubleScale = 2;
    public int 				maxLen = 0;
    public boolean 			readonly = false;
    public boolean 			hidden = false;
    public boolean 			monospaced = false;
    public boolean 			mandatory = false;
    public boolean 			onlynum = false;
    public int	 			checkType = CHECK_V;
    public Double			minValue = null;
    public Double			maxValue = null;
    public CodDescDataList 	codDescDataList = null;
    public PdfFieldInfos	mainField = null;
    public boolean 			hasClone = false;
	public String 			align = "left";
    public boolean 			border = false;
    public boolean 			multiline = false;
	public String 			defValue = "";
	public String 			currency = "";
    public boolean 			currencyPrepend = true;

    public float 			top;
    public float 			left;
    public float 			bottom;
    public float 			right;
    public float 			height;
    public float 			width;
    
    /*******************************************************************/
    /*******************************************************************/
	public int compareTo(PdfFieldInfos o) {
		if(this.page > o.page)
			return 1;
		else if(this.page < o.page)
			return -1;
		
		if((top <= o.top && top >= o.bottom) 		|| 
		   (bottom <= o.top && bottom >= o.bottom) 	||
		   (o.top <= top && o.top >= bottom) 		|| 
		   (o.bottom <= top && o.bottom >= bottom))
			return this.left < o.left ? -1 : 1;
		
		return this.top > o.top ? -1 : 1;
	}


    /*******************************************************************/
    /*******************************************************************/
	public boolean equals(Object other) {
		if(!(other instanceof PdfFieldInfos))
			return false;
		return pdfFieldName.equals(((PdfFieldInfos)other).pdfFieldName);
	}
}
