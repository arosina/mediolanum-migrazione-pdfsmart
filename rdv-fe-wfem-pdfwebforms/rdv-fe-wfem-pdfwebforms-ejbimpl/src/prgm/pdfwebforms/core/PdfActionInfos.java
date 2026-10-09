package prgm.pdfwebforms.core;

import java.io.Serializable;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class PdfActionInfos implements Serializable{
	
	public String 			pdfFieldName;
	public String 			htmlFieldName;
    public int 				page;
    public String 			helpText = "";
    public String 			html = "";

    public float 			top;
    public float 			left;
    public float 			bottom;
    public float 			right;
    public float 			height;
    public float 			width;
    
    /*******************************************************************/
    /*******************************************************************/
	public boolean equals(Object other) {
		if(!(other instanceof PdfActionInfos))
			return false;
		return pdfFieldName.equals(((PdfActionInfos)other).pdfFieldName);
	}
}
