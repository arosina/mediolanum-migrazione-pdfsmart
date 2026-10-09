package com.atosorigin.wfem.htmltopdf;

import java.util.Vector;

import com.lowagie.text.Chunk;
import com.lowagie.text.Font;
import com.lowagie.text.Phrase;

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public class TextController extends AbstractController{

	private Phrase pdfText;
	private Vector textParts = new Vector();
	private Vector textPartsFont = new Vector();
	
	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public TextController(AbstractController parent, String text, DocumentController documentController){
		super(parent,documentController);
		textParts.add(text);
		textPartsFont.add(FontAttributes.createFont(this));
	}
	
	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public String getText(){	
		String text = "";
		for(int i=0;i<textParts.size();i++)
			text += (String)textParts.get(i);
		return text;
	}
	
	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public FontAttributes getFontAttributes(){
		if(textPartsFont.size() > 0)
			return (FontAttributes)textPartsFont.get(0);
		return null;
	}
	
	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public void addText(String text){	
		textParts.add(text);
		textPartsFont.add(FontAttributes.createFont(this));
	}
	
	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public Object generatePdfDocument() throws Exception {
	
		pdfText = new Phrase();
	
		for(int i=0;i<textParts.size();i++){
			
			String text = (String)textParts.get(i);
			Font font = ((FontAttributes)textPartsFont.get(i)).getItextFont();
	
			pdfText.add(new Chunk(text,font));
			
			if(pdfText.leading() < font.size())
				pdfText.setLeading(font.size());
		}
	
		return pdfText;
	}
	
	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public Object generateCalcDocument() throws Exception {		
		DocumentController doc = getDocumentController();
		String text = "";
		for(int i=0;i<textParts.size();i++)
			text += (String)textParts.get(i);
		
		CalcRegion calcRegion = new CalcRegion(getParent().getCalcRegion().getStartRow(),getParent().getCalcRegion().getStartCol(),
											   getParent().getCalcRegion().getStartRow(),getParent().getCalcRegion().getStartCol());
		setCalcRegion(calcRegion);
		
		if(getParent() instanceof DocumentController){	// Free text
			doc.createCalcOneRowCell(this,true);
		}else if(!(getParent() instanceof CellController)){ // HParagraph or Paragraph
			doc.createCalcOneRowCell(this,false);
		}
		
		return null;
	}
	
	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public com.lowagie.text.Phrase getPdfText() {
		return pdfText;
	}
}
