package com.atosorigin.wfem.htmltopdf;

import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public class HParagraphController extends AbstractController{

	private TextController hParagraphText;
	private Paragraph pdfHParagraphText;

	private static String[] hParagraphTextFontSizeRef = new String[]{"7","6","5","4","2","1"};
		
	/**************************************************************************************************/
	/**************************************************************************************************/
	public HParagraphController(AbstractController parent, DocumentController documentController){
		super(parent,documentController);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void addText(String text){
		if(hParagraphText == null){
			hParagraphText = new TextController(this,text,getDocumentController());
			hParagraphText.setWriter(writer);
		}else{
			hParagraphText.addText(text);
		}
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public Object generatePdfDocument() throws Exception {
	
		pdfHParagraphText = new Paragraph();
	
		if(hParagraphText != null){
			Phrase pdfText = (Phrase)hParagraphText.generatePdfDocument();
			if(pdfText != null)
				pdfHParagraphText = new Paragraph(pdfText);
		}
	
		ControllerAttributes controllerAttributes = getAttributes();
		if(controllerAttributes.getAlign() == ControllerAttributes.ALIGN_LEFT)
			pdfHParagraphText.setAlignment(Paragraph.ALIGN_LEFT);
		else if(controllerAttributes.getAlign() == ControllerAttributes.ALIGN_RIGHT)
			pdfHParagraphText.setAlignment(Paragraph.ALIGN_RIGHT);
		else if(controllerAttributes.getAlign() == ControllerAttributes.ALIGN_CENTER)
			pdfHParagraphText.setAlignment(Paragraph.ALIGN_CENTER);
			
		return pdfHParagraphText;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public Object generateCalcDocument() throws Exception {
		if(hParagraphText == null)
			return null;
		
		DocumentController doc = getDocumentController();
		CalcRegion calcRegion = new CalcRegion(doc.getCalcGlobalRowIdx(),doc.getCalcGlobalColIdx(),
											   doc.getCalcGlobalRowIdx(),12); 
		setCalcRegion(calcRegion);

		hParagraphText.generateCalcDocument();
		return null;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public FontAttributes getHParagraphFont(String h) {
	
		int hs = new Integer(h.substring(1,2)).intValue();
		if(hs < 0)
			hs = 1;
		if(hs > 6)
			hs = 6;
		hs--;
	
		FontAttributes fontAttr = new FontAttributes(getDocumentController().getHtmlFontSizeReference());
		fontAttr.setHtmlSize(hParagraphTextFontSizeRef[hs]);
			
		return fontAttr;
	}
}
