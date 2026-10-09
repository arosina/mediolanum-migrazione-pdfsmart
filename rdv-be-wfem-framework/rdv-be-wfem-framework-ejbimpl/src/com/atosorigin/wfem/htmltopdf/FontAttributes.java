package com.atosorigin.wfem.htmltopdf;

import java.awt.Color;
import java.util.Hashtable;

import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.BaseFont;

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public class FontAttributes {
	
	private static final String DEFAULT_FONT_NAME = "HELVETICA";
	private static final String DEFAULT_HTML_FONT_SIZE = "3";	
	private int[] htmlFontSizeReference = DocumentController.DEFAULT_HTML_FONT_SIZE_REFERENCE;

	private static Hashtable htmlPdfFontMapping = new Hashtable();
	private static Hashtable htmlPdfITextFontNameMapping = new Hashtable();
	private static Hashtable htmlPdfBaseFontMapping = new Hashtable();
	
	static{
		htmlPdfFontMapping.put("HELVETICA",        new Integer(Font.HELVETICA));
		htmlPdfFontMapping.put("COURIER",          new Integer(Font.COURIER));
		htmlPdfFontMapping.put("TIMES NEW ROMAN",  new Integer(Font.TIMES_ROMAN));
		htmlPdfFontMapping.put("SYMBOL",           new Integer(Font.SYMBOL));
	}
	
	static{
		htmlPdfITextFontNameMapping.put("HELVETICA",        new String(FontFactory.HELVETICA));
		htmlPdfITextFontNameMapping.put("COURIER",          new String(FontFactory.COURIER));
		htmlPdfITextFontNameMapping.put("TIMES NEW ROMAN",  new String(FontFactory.TIMES_ROMAN));
		htmlPdfITextFontNameMapping.put("SYMBOL",           new String(FontFactory.SYMBOL));
	}
	
	static{
		try{
			htmlPdfBaseFontMapping.put("HELVETICA",        BaseFont.createFont(BaseFont.HELVETICA,BaseFont.WINANSI,true));
			htmlPdfBaseFontMapping.put("COURIER",          BaseFont.createFont(BaseFont.COURIER,BaseFont.WINANSI,true));
			htmlPdfBaseFontMapping.put("TIMES NEW ROMAN",  BaseFont.createFont(BaseFont.TIMES_ROMAN,BaseFont.WINANSI,true));
			htmlPdfBaseFontMapping.put("SYMBOL",           BaseFont.createFont(BaseFont.SYMBOL,BaseFont.WINANSI,true));
		}catch(Exception e){e.printStackTrace();}
	}
	
	private String htmlSize;
	private int pointSize;
	private String name;
	private Color color = Color.black;
	private Font itextFont; 
	private boolean bold = false;
	private boolean italic = false;
	private boolean underlined = false;
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getUniqueKey(){
		String result = ""+name+pointSize+color.getRGB()+isBold()+isItalic()+isUnderlined();
		return result;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public FontAttributes(int[] htmlFontSizeReference) {
		cloneHtmlFontSizeReference(htmlFontSizeReference);
		setName(DEFAULT_FONT_NAME);
		setHtmlSize(DEFAULT_HTML_FONT_SIZE);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public FontAttributes(int htmlSize, int[] htmlFontSizeReference) {
		cloneHtmlFontSizeReference(htmlFontSizeReference);
		setName(DEFAULT_FONT_NAME);
		setHtmlSize(""+htmlSize);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public FontAttributes(FontAttributes fontAttr, int[] htmlFontSizeReference) {
		cloneHtmlFontSizeReference(htmlFontSizeReference);
		setName(fontAttr.getName());
		setHtmlSize(fontAttr.getHtmlSize());
		setColor(fontAttr.getColor());
		setBold(fontAttr.isBold());
		setItalic(fontAttr.isItalic());
		setUnderlined(fontAttr.isUnderlined());
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public FontAttributes(String name, int[] htmlFontSizeReference) {
		cloneHtmlFontSizeReference(htmlFontSizeReference);
		setName(name);
		setHtmlSize(DEFAULT_HTML_FONT_SIZE);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public FontAttributes(String name, int htmlSize, int[] htmlFontSizeReference) {
		cloneHtmlFontSizeReference(htmlFontSizeReference);
		setName(name);
		setHtmlSize(""+htmlSize);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private void cloneHtmlFontSizeReference(int[] htmlFontSizeReference){
		if(htmlFontSizeReference == null)
			return;
		this.htmlFontSizeReference = new int[htmlFontSizeReference.length];
		for(int i=0;i<htmlFontSizeReference.length;i++)
			this.htmlFontSizeReference[i] = htmlFontSizeReference[i];
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private void alignPointSize(String newHtmlSize) {
		newHtmlSize = newHtmlSize.toLowerCase();
		try{
			if(newHtmlSize.endsWith("px")){
				htmlSize = newHtmlSize;
				pointSize = Integer.parseInt(htmlSize.substring(0,htmlSize.indexOf("px")));
			}else{
				htmlSize = newHtmlSize;
				int refSize = Integer.parseInt(newHtmlSize);
				if(refSize <=0 || refSize > 7){
					htmlSize = DEFAULT_HTML_FONT_SIZE;
					refSize = Integer.parseInt(htmlSize);
				}
				pointSize = htmlFontSizeReference[refSize-1];
			}
		}catch(Exception e){
			htmlSize = DEFAULT_HTML_FONT_SIZE;
			int refSize = Integer.parseInt(htmlSize);
			pointSize = htmlFontSizeReference[refSize-1];			
		}
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static FontAttributes createFont(AbstractController controller) {
		FontAttributes currentFontAttr = controller.getDocumentController().getCurrentFontAttributes();
		FontAttributes newFont = new FontAttributes(currentFontAttr,controller.getDocumentController().getHtmlFontSizeReference());
		int decor = Font.NORMAL;
		if(newFont.isUnderlined())
			decor = decor | Font.UNDERLINE;
		if(newFont.isBold())
			decor = decor | Font.BOLD;
		if(newFont.isItalic())
			decor = decor | Font.ITALIC;
		Font itextFont = FontFactory.getFont(newFont.getITextFontName(),newFont.getPointSize(),decor);
		itextFont.setColor(newFont.getColor());
		newFont.setItextFont(itextFont);
		return newFont;	
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public BaseFont getBaseFont() {
		BaseFont bf = (BaseFont)htmlPdfBaseFontMapping.get(name.toUpperCase());
		if(bf == null)
			bf = (BaseFont)htmlPdfBaseFontMapping.get(DEFAULT_FONT_NAME);
		return bf;	
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getITextFontName() {
		String iTextfontName = (String)htmlPdfITextFontNameMapping.get(name.toUpperCase());
		if(iTextfontName == null)
			iTextfontName = (String)htmlPdfITextFontNameMapping.get(DEFAULT_FONT_NAME);
		return iTextfontName;	
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setHtmlSize(String newHtmlSize) {
		if(newHtmlSize == null) 
			newHtmlSize = DEFAULT_HTML_FONT_SIZE;
		alignPointSize(newHtmlSize);
	}

	public java.lang.String getName() {
		return name;
	}

	public int getPointSize() {
		return pointSize;
	}

	public void setColor(java.awt.Color newColor) {
		color = newColor;
	}

	public void setName(java.lang.String newName) {
		name = newName;
	}
	
	public String getHtmlSize() {
		return htmlSize;
	}
	
	public java.awt.Color getColor() {
		return color;
	}

	public void setPointSize(int pointSize) {
		this.pointSize = pointSize;
	}

	public Font getItextFont() {
		return itextFont;
	}

	public void setItextFont(Font itextFont) {
		this.itextFont = itextFont;
	}

	public boolean isBold() {
		return bold;
	}

	public void setBold(boolean bold) {
		this.bold = bold;
	}

	public boolean isItalic() {
		return italic;
	}

	public void setItalic(boolean italic) {
		this.italic = italic;
	}

	public boolean isUnderlined() {
		return underlined;
	}

	public void setUnderlined(boolean underlined) {
		this.underlined = underlined;
	}

}
