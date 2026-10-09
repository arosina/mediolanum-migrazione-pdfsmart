package com.atosorigin.wfem.htmltopdf;

import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public class ParagraphController extends AbstractController{

	private TextController paragraphText;
	private Paragraph pdfParagraph;

	/**************************************************************************************************/
	/**************************************************************************************************/
	public ParagraphController(AbstractController parent, DocumentController documentController){
		super(parent,documentController);
	}
	
	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public void addText(String text){
		if(paragraphText == null){
			paragraphText = new TextController(this, text,getDocumentController());
			paragraphText.setWriter(writer);
		}else{
			paragraphText.addText(text);
		}
	}

	/*****************************************************************************************************/
    /*****************************************************************************************************/
	public Object generatePdfDocument() throws Exception {
	
		pdfParagraph = new Paragraph();
	
		if(paragraphText != null){
			Phrase pdfText = (Phrase)paragraphText.generatePdfDocument();
			if(pdfText != null)
				pdfParagraph = new Paragraph(pdfText);
		}
	
		ControllerAttributes controllerAttributes = getAttributes();
		if(controllerAttributes.getAlign() == ControllerAttributes.ALIGN_LEFT)
			pdfParagraph.setAlignment(Paragraph.ALIGN_LEFT);
		else if(controllerAttributes.getAlign() == ControllerAttributes.ALIGN_RIGHT)
			pdfParagraph.setAlignment(Paragraph.ALIGN_RIGHT);
		else if(controllerAttributes.getAlign() == ControllerAttributes.ALIGN_CENTER)
			pdfParagraph.setAlignment(Paragraph.ALIGN_CENTER);
			
		return pdfParagraph;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public Object generateCalcDocument() throws Exception {
		if(paragraphText == null)
			return null;
		
		DocumentController doc = getDocumentController();
		CalcRegion calcRegion = new CalcRegion(doc.getCalcGlobalRowIdx(),doc.getCalcGlobalColIdx(),
											   doc.getCalcGlobalRowIdx(),12); 
		setCalcRegion(calcRegion);

		paragraphText.generateCalcDocument();
		return null;
	}
}
