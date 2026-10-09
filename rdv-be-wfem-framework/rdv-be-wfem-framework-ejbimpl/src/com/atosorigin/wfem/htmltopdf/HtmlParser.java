package com.atosorigin.wfem.htmltopdf;

import java.io.Reader;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.parser.ParserDelegator;

import com.atosorigin.wfem.layout.HtmlColorMapping;
import com.atosorigin.wfem.loggers.HtmlToPdfLogger;
import com.lowagie.text.pdf.PdfWriter;

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public class HtmlParser extends javax.swing.text.html.HTMLEditorKit.ParserCallback {
	
	private ParserDelegator parserDelegator = new ParserDelegator();
	private Vector elementsStack = new Vector();
	private PdfWriter writer;
	private DocumentController documentController;
	private HtmlToPdfLogger LOG;

	/**************************************************************************************************/
	/**************************************************************************************************/
	public HtmlParser(DocumentController documentController, PdfWriter writer, boolean readTraceConfiguration) {
		super();
		elementsStack.add(documentController);
		this.writer = writer;
		this.documentController = documentController;
		LOG = HtmlToPdfLogger.getInstance(readTraceConfiguration);
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getAttributeValueInComment(String comment,String attributeName) {
		
		Pattern attrPattern = Pattern.compile("\\s+"+attributeName+"\\s*=\\s*(['\"])(.*?)\\1",Pattern.CASE_INSENSITIVE);
		Matcher mat = attrPattern.matcher(comment);
		if(!mat.find())
			return null;
		return mat.group(2);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void handleComment(char[] data, int pos){
		
		AbstractController curDocElement = getCurDocElement();
		if(curDocElement == null)
			return;
	
		String text = new String(data);
		text = text.replaceAll("[\r\n]"," ");
		
		LOG.debug("Comment ["+text+"] on document element "+curDocElement.getClass().getName());
	
		try{
	
			if(curDocElement instanceof TableController){
				String attrValue = getAttributeValueInComment(text,TableController.HEADERROWS_INDICATOR);
				if(attrValue != null){
					((TableController)curDocElement).setHeaderRows(Integer.parseInt(attrValue));
				}
				return;
			}
	
			boolean isAttributeManaged = false;
	
			boolean isNewPageLayout = false;
			PageLayout pageLayout = null;
			if(documentController.getCurrentDocumentElement() instanceof PageLayout){
				// Get current page layout
				pageLayout = (PageLayout)documentController.getCurrentDocumentElement();
			}else{
				// New layout equals to current
				isNewPageLayout = true;
				pageLayout = new PageLayout(documentController.getCurrentPageLayout());
			}

			// Page html font size reference
			setHtmlPageFontSizeReference(documentController,text);
			
			// Page margins
			if(setPageMargins(pageLayout,text))
				isAttributeManaged = true;
			
			// Page format
			if(setPageFormat(pageLayout,text))
				isAttributeManaged = true;
	
			// Page font
			if(setPageFont(pageLayout,text))
				isAttributeManaged = true;
			
			// Page Header
			if(setPageHeader(pageLayout,text))
				isAttributeManaged = true;
				
			// Page Footer
			if(setPageFooter(pageLayout,text))
				isAttributeManaged = true;
				
			// NewBlock
			if(setNewBlock(pageLayout,text))
				isAttributeManaged = true;
	
			if(isNewPageLayout && isAttributeManaged)
				curDocElement.addPageLayout(pageLayout);
					
			String includedPdfName = getAttributeValueInComment(text,"INCLUDEPDF");
			if(includedPdfName != null){
				curDocElement.addPdfFile(includedPdfName);
			}
	
	
		}catch(ControllerTypeNotSupported ctns){
			LOG.warning("ControllerTypeNotSupported on document element "+curDocElement.getClass().getName());
		}catch(CommentTagNotSupported ctns){
			LOG.warning("CommentTagNotSupported on document element "+curDocElement.getClass().getName());
		}
		
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void handleEndOfLineString(String eol){
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void handleEndTag(HTML.Tag t, int pos){
	
		AbstractController curDocElement = getCurDocElement();
		if(curDocElement == null)
			return;
		
		LOG.debug("End tag ["+t.toString().toUpperCase()+"] on document element "+curDocElement.getClass().getName());
		
		if(t.equals(HTML.Tag.TITLE) ||
		   t.equals(HTML.Tag.TABLE) ||
		   t.equals(HTML.Tag.TR)    ||
		   t.equals(HTML.Tag.TD)    ||
		   t.equals(HTML.Tag.TH)    ||
		   t.equals(HTML.Tag.P)){
			
			elementsStack.removeElementAt(elementsStack.size()-1);
			elementsStack.trimToSize(); 
			
		}else if(t.equals(HTML.Tag.B)){
			
			FontAttributes currentFontAttr = documentController.getCurrentFontAttributes();
			currentFontAttr.setBold(false);
			
		}else if(t.equals(HTML.Tag.I)){
			
			FontAttributes currentFontAttr = documentController.getCurrentFontAttributes();
			currentFontAttr.setItalic(false);
			
		}else if(t.equals(HTML.Tag.U)){
			
			FontAttributes currentFontAttr = documentController.getCurrentFontAttributes();
			currentFontAttr.setUnderlined(false);
			
		}else if(t.equals(HTML.Tag.H1) ||
		         t.equals(HTML.Tag.H2) ||
		   		 t.equals(HTML.Tag.H3) ||
		   		 t.equals(HTML.Tag.H4) ||
	 	  		 t.equals(HTML.Tag.H5) ||
		   		 t.equals(HTML.Tag.H6)){
	
			documentController.removeFont();
			
			elementsStack.removeElementAt(elementsStack.size()-1);
			elementsStack.trimToSize(); 
			
		}else if(t.equals(HTML.Tag.FONT)){
	
			documentController.removeFont();	
					
		}
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void handleError(String errorMsg, int pos){
		AbstractController curDocElement = getCurDocElement();
		if(curDocElement == null)
			return;
		LOG.warning("Error ["+errorMsg+"] on document element "+curDocElement.getClass().getName());
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void handleSimpleTag(HTML.Tag t, MutableAttributeSet a, int pos){
		
		AbstractController curDocElement = getCurDocElement();
		if(curDocElement == null)
			return;
		
		LOG.debug("Simple tag ["+t.toString()+"] on document element "+curDocElement.getClass().getName());
	
		try{
			
			if(t.equals(HTML.Tag.IMG)){
				
				Enumeration attrs = a.getAttributeNames();
				if(attrs == null)
					return;
				
				while(attrs.hasMoreElements()){
					
					Object oattr = attrs.nextElement();
					if(oattr == null || !(oattr instanceof HTML.Attribute))
					    continue;
					
					HTML.Attribute attr = (HTML.Attribute)oattr;
					if(attr.equals(HTML.Attribute.SRC)){
						
						String imageName = (String)a.getAttribute(attr);
						ImageController image = new ImageController(curDocElement,imageName,documentController);
						manageImageAttributes(a,image.getAttributes());				
						image.setWriter(writer);
						curDocElement.addImage(image);
						
					}			
				}	
				
			}else if(t.equals(HTML.Tag.BR)){
				
				curDocElement.addNewLine();
				
			}else if(t.equals(HTML.Tag.HR)){
				
				curDocElement.addNewPage();
				
			}

		}catch(ControllerTypeNotSupported ctns){
			LOG.warning("ControllerTypeNotSupported on simple tag: ["+t.toString()+"] on document element "+curDocElement.getClass().getName());
		}catch(HtmlTagNotSupported htns){
			LOG.warning("HtmlTagNotSupported on simple tag: ["+t.toString()+"] on document element "+curDocElement.getClass().getName());
		}
		
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void handleStartTag(HTML.Tag t, MutableAttributeSet a, int pos){
	
		AbstractController curDocElement = getCurDocElement();
		if(curDocElement == null)
			return;

		AbstractController newDocElement = null;
	
		LOG.debug("Start tag ["+t.toString()+"] on document element "+curDocElement.getClass().getName());
		
		try{
			
			if(t.equals(HTML.Tag.BODY)){
	
				((DocumentController)curDocElement).startContentNotification();
				
			}
			else if(t.equals(HTML.Tag.TITLE)){
				
				newDocElement = new DocumentTitleController(curDocElement,documentController);
				curDocElement.addTitle((DocumentTitleController)newDocElement);
				
			}
			else if(t.equals(HTML.Tag.TABLE)){
				
				newDocElement = new TableController(curDocElement,documentController);			
				manageTableAttributes(a,newDocElement.getAttributes());
				curDocElement.addTable((TableController)newDocElement);
				
			}
			else if(t.equals(HTML.Tag.TR)){
				
				newDocElement = new RowController(curDocElement,documentController);
				curDocElement.addRow((RowController)newDocElement);
				
			}
			else if(t.equals(HTML.Tag.TD) || t.equals(HTML.Tag.TH)){
				
				newDocElement = new CellController(curDocElement,documentController);			
				manageCellAttributes(a,newDocElement.getAttributes());				
				curDocElement.addCell((CellController)newDocElement);
				
			}
			else if(t.equals(HTML.Tag.P)){
				
				newDocElement = new ParagraphController(curDocElement,documentController);
				manageParagraphAttributes(a,newDocElement.getAttributes());
				curDocElement.addParagraph((ParagraphController)newDocElement);
				
			}
			else if(t.equals(HTML.Tag.H1) ||
				    t.equals(HTML.Tag.H2) ||
				    t.equals(HTML.Tag.H3) ||
				    t.equals(HTML.Tag.H4) ||
				    t.equals(HTML.Tag.H5) ||
				    t.equals(HTML.Tag.H6)){
				
				newDocElement = new HParagraphController(curDocElement,documentController);
				FontAttributes fontAttr = ((HParagraphController)newDocElement).getHParagraphFont(t.toString());
				documentController.addFont(fontAttr);
				
				manageTitleAttributes(a,newDocElement.getAttributes());
				curDocElement.addHParagraph((HParagraphController)newDocElement);
				
			}
			else if(t.equals(HTML.Tag.FONT)){
	
				FontAttributes fontAttr = null;
				if(curDocElement instanceof HParagraphController)
					fontAttr = new FontAttributes(documentController.getCurrentFontAttributes(),documentController.getHtmlFontSizeReference());
				else	
					fontAttr = new FontAttributes(documentController.getCurrentPageLayout().getPageDefaultFontAttributes(),documentController.getHtmlFontSizeReference());
				
				manageFontAttributes(a,fontAttr);			
				documentController.addFont(fontAttr);
					
			}else{
	
				manageAttributesTags(t, curDocElement);
				
			}
	
		}catch(ControllerTypeNotSupported ctns){
			LOG.warning("ControllerTypeNotSupported on tag: ["+t.toString()+"] on document element "+curDocElement.getClass().getName());
		}
		
		if(newDocElement != null){
	
			newDocElement.setWriter(writer);
			
			elementsStack.add(newDocElement);
		}
		
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void handleText(char[] data, int pos){
		
		AbstractController curDocElement = getCurDocElement();
		if(curDocElement == null)
			return;
	
		String text = new String(data);
		
		LOG.debug("Text ["+text+"] on document element "+curDocElement.getClass().getName());
		
		try{
			
			curDocElement.addText(text);
	
		}catch(ControllerTypeNotSupported ctns){
			LOG.warning("ControllerTypeNotSupported on text: ["+text+"] on document element "+curDocElement.getClass().getName());
		}
		
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public static int indexOfKey(String text, String key) {
		
		String upText = text.toUpperCase();
		return upText.indexOf(key);
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private void manageAttributesTags(HTML.Tag t, AbstractController curDocElement) {
		
		if(t.equals(HTML.Tag.B)){
			
			FontAttributes currentFontAttr = documentController.getCurrentFontAttributes();
			currentFontAttr.setBold(true);
			
		}
		else if(t.equals(HTML.Tag.I)){
			
			FontAttributes currentFontAttr = documentController.getCurrentFontAttributes();
			currentFontAttr.setItalic(true);
			
		}
		else if(t.equals(HTML.Tag.U)){
			
			FontAttributes currentFontAttr = documentController.getCurrentFontAttributes();
			currentFontAttr.setUnderlined(true);
			
		}
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void parse(Reader reader) throws Exception{
	    parserDelegator.parse(reader, this, false);
	}

	/**************************************************************************************************/
	private static Pattern newBlockPattern = Pattern.compile("\\s+"+PageLayoutSyntaxConstants.PAGE_LAYOUT_SYNTAX_NEWBLOCK+"\\s+",Pattern.CASE_INSENSITIVE);
	/**************************************************************************************************/
	private boolean setNewBlock(PageLayout pageLayout, String text) {
		
		boolean	isAttributeManaged = false;
		
		Matcher mat = newBlockPattern.matcher(text);
		if(mat.find()){
			pageLayout.setNewBlock(true);
			isAttributeManaged = true;
		}
		
		return isAttributeManaged;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private void setHtmlPageFontSizeReference(DocumentController documentController, String text) {
		
		String attrValue = getAttributeValueInComment(text,PageLayoutSyntaxConstants.PAGE_HTML_FONT_SIZE_REFERENCE);
		if(attrValue == null)
			return;
		
		Vector vals = new Vector();
		StringTokenizer st = new StringTokenizer(attrValue,",");
		while(st.hasMoreTokens())
			vals.add(st.nextToken());
		if(vals.size() != 7)
			return;
		
		int[] ivals = new int[vals.size()];
		for(int i=0;i<vals.size();i++){
			if(((String)vals.get(i)).equals("*"))
				ivals[i] = DocumentController.DEFAULT_HTML_FONT_SIZE_REFERENCE[i];
			else
				ivals[i] = Integer.parseInt((String)vals.get(i));
		}
		documentController.setHtmlFontSizeReference(ivals);
		return;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private boolean setPageFont(PageLayout pageLayout, String text) {
		
		boolean	isAttributeManaged = false;
		
		String attrValue = getAttributeValueInComment(text,PageLayoutSyntaxConstants.PAGE_LAYOUT_SYNTAX_DEFAULT_FONTFACE);
		if(attrValue != null){
			pageLayout.getPageDefaultFontAttributes().setName(attrValue);
			isAttributeManaged = true;
		}
		attrValue = getAttributeValueInComment(text,PageLayoutSyntaxConstants.PAGE_LAYOUT_SYNTAX_DEFAULT_FONTSIZE);
		if(attrValue != null){
			pageLayout.getPageDefaultFontAttributes().setHtmlSize(attrValue);
			isAttributeManaged = true;
		}	
	
		return isAttributeManaged;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private boolean setPageFooter(PageLayout pageLayout, String text) {
		
		boolean	isAttributeManaged = false;
		
		PageHeaderFooter pageFooter = pageLayout.getPageFooter();
		if(pageFooter == null)
			pageFooter = new PageHeaderFooter(false,documentController);

		// Page Footer Widths
		String attrValue = getAttributeValueInComment(text,PageLayoutSyntaxConstants.PAGE_LAYOUT_SYNTAX_FOOTER_WIDTHS);
		if(attrValue != null){
			pageFooter.setWidths(attrValue);
			isAttributeManaged = true;
		}

		// Page Footer Left Text
		attrValue = getAttributeValueInComment(text,PageLayoutSyntaxConstants.PAGE_LAYOUT_SYNTAX_FOOTER_LEFT);
		if(attrValue != null){
			pageFooter.setLeftText(attrValue);
			isAttributeManaged = true;
		}

		// Page Footer Left Align
		attrValue = getAttributeValueInComment(text,PageLayoutSyntaxConstants.PAGE_LAYOUT_SYNTAX_FOOTER_LEFT_ALIGN);
		if(attrValue != null){
			pageFooter.setLeftAlign(attrValue);
			isAttributeManaged = true;
		}

		// Page Footer Center Text
		attrValue = getAttributeValueInComment(text,PageLayoutSyntaxConstants.PAGE_LAYOUT_SYNTAX_FOOTER_CENTER);
		if(attrValue != null){
			pageFooter.setCenterText(attrValue);
			isAttributeManaged = true;
		}

		// Page Footer Center Align
		attrValue = getAttributeValueInComment(text,PageLayoutSyntaxConstants.PAGE_LAYOUT_SYNTAX_FOOTER_CENTER_ALIGN);
		if(attrValue != null){
			pageFooter.setCenterAlign(attrValue);
			isAttributeManaged = true;
		}

		// Page Footer Right Text
		attrValue = getAttributeValueInComment(text,PageLayoutSyntaxConstants.PAGE_LAYOUT_SYNTAX_FOOTER_RIGHT);
		if(attrValue != null){
			pageFooter.setRightText(attrValue);
			isAttributeManaged = true;
		}

		// Page Footer Right Align
		attrValue = getAttributeValueInComment(text,PageLayoutSyntaxConstants.PAGE_LAYOUT_SYNTAX_FOOTER_RIGHT_ALIGN);
		if(attrValue != null){
			pageFooter.setRightAlign(attrValue);
			isAttributeManaged = true;
		}

		// Page Footer Decoration
		attrValue = getAttributeValueInComment(text,PageLayoutSyntaxConstants.PAGE_LAYOUT_SYNTAX_FOOTER_DECOR);
		if(attrValue != null){
			pageFooter.setDecoration(attrValue);
			isAttributeManaged = true;
		}
		
		// Page Footer Font Face
		attrValue = getAttributeValueInComment(text,PageLayoutSyntaxConstants.PAGE_LAYOUT_SYNTAX_FOOTER_FONTFACE);
		if(attrValue != null){
			pageFooter.getHeaderFooterFont().setName(attrValue);
			isAttributeManaged = true;
		}
		
		// Page Footer Font Size
		attrValue = getAttributeValueInComment(text,PageLayoutSyntaxConstants.PAGE_LAYOUT_SYNTAX_FOOTER_FONTSIZE);
		if(attrValue != null){
			pageFooter.getHeaderFooterFont().setHtmlSize(attrValue);
			isAttributeManaged = true;
		}
		
		pageLayout.setPageFooter(pageFooter);
		
		return isAttributeManaged;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private boolean setPageFormat(PageLayout pageLayout, String text) {
		
		boolean	isAttributeManaged = false;
		
		String attrValue = getAttributeValueInComment(text,PageLayoutSyntaxConstants.PAGE_LAYOUT_SYNTAX_PAGEFORMAT);
		if(attrValue != null){
			pageLayout.setPageFormat(attrValue);
			isAttributeManaged = true;
		}
		attrValue = getAttributeValueInComment(text,PageLayoutSyntaxConstants.PAGE_LAYOUT_SYNTAX_PAGEORIENTATION);
		if(attrValue != null){
			pageLayout.setPageOrientation(attrValue);
			isAttributeManaged = true;
		}
	
		return isAttributeManaged;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private boolean setPageHeader(PageLayout pageLayout, String text) {
		
		boolean	isAttributeManaged = false;
		
		PageHeaderFooter pageHeader = pageLayout.getPageHeader();
		if(pageHeader == null)
			pageHeader = new PageHeaderFooter(true,documentController);

		// Page Header Widths
		String attrValue = getAttributeValueInComment(text,PageLayoutSyntaxConstants.PAGE_LAYOUT_SYNTAX_HEADER_WIDTHS);
		if(attrValue != null){
			pageHeader.setWidths(attrValue);
			isAttributeManaged = true;
		}

		// Page Header Left Text
		attrValue = getAttributeValueInComment(text,PageLayoutSyntaxConstants.PAGE_LAYOUT_SYNTAX_HEADER_LEFT);
		if(attrValue != null){
			pageHeader.setLeftText(attrValue);
			isAttributeManaged = true;
		}

		// Page Header Left Align
		attrValue = getAttributeValueInComment(text,PageLayoutSyntaxConstants.PAGE_LAYOUT_SYNTAX_HEADER_LEFT_ALIGN);
		if(attrValue != null){
			pageHeader.setLeftAlign(attrValue);
			isAttributeManaged = true;
		}

		// Page Header Center Text
		attrValue = getAttributeValueInComment(text,PageLayoutSyntaxConstants.PAGE_LAYOUT_SYNTAX_HEADER_CENTER);
		if(attrValue != null){
			pageHeader.setCenterText(attrValue);
			isAttributeManaged = true;
		}

		// Page Header Center Align
		attrValue = getAttributeValueInComment(text,PageLayoutSyntaxConstants.PAGE_LAYOUT_SYNTAX_HEADER_CENTER_ALIGN);
		if(attrValue != null){
			pageHeader.setCenterAlign(attrValue);
			isAttributeManaged = true;
		}

		// Page Header Right Text
		attrValue = getAttributeValueInComment(text,PageLayoutSyntaxConstants.PAGE_LAYOUT_SYNTAX_HEADER_RIGHT);
		if(attrValue != null){
			pageHeader.setRightText(attrValue);
			isAttributeManaged = true;
		}

		// Page Header Right Align
		attrValue = getAttributeValueInComment(text,PageLayoutSyntaxConstants.PAGE_LAYOUT_SYNTAX_HEADER_RIGHT_ALIGN);
		if(attrValue != null){
			pageHeader.setRightAlign(attrValue);
			isAttributeManaged = true;
		}

		// Page Header Decoration
		attrValue = getAttributeValueInComment(text,PageLayoutSyntaxConstants.PAGE_LAYOUT_SYNTAX_HEADER_DECOR);
		if(attrValue != null){
			pageHeader.setDecoration(attrValue);
			isAttributeManaged = true;
		}
		
		// Page Header Font Face
		attrValue = getAttributeValueInComment(text,PageLayoutSyntaxConstants.PAGE_LAYOUT_SYNTAX_HEADER_FONTFACE);
		if(attrValue != null){
			pageHeader.getHeaderFooterFont().setName(attrValue);
			isAttributeManaged = true;
		}
		
		// Page Header Font Size
		attrValue = getAttributeValueInComment(text,PageLayoutSyntaxConstants.PAGE_LAYOUT_SYNTAX_HEADER_FONTSIZE);
		if(attrValue != null){
			pageHeader.getHeaderFooterFont().setHtmlSize(attrValue);
			isAttributeManaged = true;
		}
	
		pageLayout.setPageHeader(pageHeader);
		
		return isAttributeManaged;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private boolean setPageMargins(PageLayout pageLayout, String text) {
		
		boolean	isAttributeManaged = false;
		
		String attrValue = getAttributeValueInComment(text,PageLayoutSyntaxConstants.PAGE_LAYOUT_SYNTAX_MARGIN_BOTTOM);
		if(attrValue != null){
			pageLayout.setInchMarginBottom(Double.parseDouble(attrValue));
			isAttributeManaged = true;
		}
		attrValue = getAttributeValueInComment(text,PageLayoutSyntaxConstants.PAGE_LAYOUT_SYNTAX_MARGIN_TOP);
		if(attrValue != null){
			pageLayout.setInchMarginTop(Double.parseDouble(attrValue));
			isAttributeManaged = true;
		}
		attrValue = getAttributeValueInComment(text,PageLayoutSyntaxConstants.PAGE_LAYOUT_SYNTAX_MARGIN_LEFT);
		if(attrValue != null){
			pageLayout.setInchMarginLeft(Double.parseDouble(attrValue));
			isAttributeManaged = true;
		}
		attrValue = getAttributeValueInComment(text,PageLayoutSyntaxConstants.PAGE_LAYOUT_SYNTAX_MARGIN_RIGHT);
		if(attrValue != null){
			pageLayout.setInchMarginRigth(Double.parseDouble(attrValue));
			isAttributeManaged = true;
		}
	
		return isAttributeManaged;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private void manageCellAttributes(MutableAttributeSet a, 
								      ControllerAttributes controllerAttributes) {
		
		Integer ival = null;
		
		Enumeration attrs = a.getAttributeNames();
		if(attrs == null)
			return;
		
		while(attrs.hasMoreElements()){
			
			Object oattr = attrs.nextElement();
			if(!(oattr instanceof HTML.Attribute))
				continue;
			HTML.Attribute attr = (HTML.Attribute)oattr;
			String attrValue = (String)a.getAttribute(attr);
		
			if(attr.equals(HTML.Attribute.BORDER)){
				
				ival = new Integer(attrValue);
				controllerAttributes.setBorder(ival.intValue());
				
			}else if(attr.equals(HTML.Attribute.COLSPAN)){
				
				ival = new Integer(attrValue);
				controllerAttributes.setColSpan(ival.intValue());
			
			}else if(attr.equals(HTML.Attribute.NOWRAP)){
				
				controllerAttributes.setNoWrap(true);	
				
			}else if(attr.equals(HTML.Attribute.ALIGN)){
	
				if(attrValue.equalsIgnoreCase("right")){
					controllerAttributes.setAlign(ControllerAttributes.ALIGN_RIGHT);
				}else if(attrValue.equalsIgnoreCase("left")){
					controllerAttributes.setAlign(ControllerAttributes.ALIGN_LEFT);
				}else if(attrValue.equalsIgnoreCase("center")){
					controllerAttributes.setAlign(ControllerAttributes.ALIGN_CENTER);
				}
				
			}else if(attr.equals(HTML.Attribute.VALIGN)){
				
				if(attrValue.equalsIgnoreCase("middle")){
					controllerAttributes.setValign(ControllerAttributes.ALIGN_MIDDLE);
				}else if(attrValue.equalsIgnoreCase("bottom")){
					controllerAttributes.setValign(ControllerAttributes.ALIGN_BOTTOM);
				}else if(attrValue.equalsIgnoreCase("top")){
					controllerAttributes.setValign(ControllerAttributes.ALIGN_TOP);
				}
				
			}else if(attr.equals(HTML.Attribute.WIDTH)){
	
				if(attrValue.endsWith("%")){
					attrValue = attrValue.substring(0,attrValue.length()-1);
					ival = new Integer(attrValue);
					controllerAttributes.setWidth(ival.intValue());
				}
				
			}else if(attr.equals(HTML.Attribute.HEIGHT)){
				
				if(attrValue.endsWith("%"))
					attrValue = attrValue.substring(0,attrValue.length()-1);
				ival = new Integer(attrValue);
				controllerAttributes.setHeight(ival.intValue());
				
			}else if(attr.equals(HTML.Attribute.BGCOLOR)){
	
				if(!attrValue.startsWith("#"))
					attrValue = HtmlColorMapping.getColor(attrValue);
				Integer exColor = Integer.decode(attrValue);
				controllerAttributes.setBackground(new java.awt.Color(exColor.intValue()));
				
			}		
		}
		return;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private void manageFontAttributes(MutableAttributeSet a, 
									  FontAttributes fontAttr) {
		
		Integer ival = null;
		
		Enumeration attrs = a.getAttributeNames();
		if(attrs != null){
			
			while(attrs.hasMoreElements()){
				
				HTML.Attribute attr = (HTML.Attribute)attrs.nextElement();
				String attrValue = (String)a.getAttribute(attr);
			
				if(attr.equals(HTML.Attribute.FACE)){
					
					fontAttr.setName(attrValue);
					
				}else if(attr.equals(HTML.Attribute.SIZE)){
	
					fontAttr.setHtmlSize(attrValue);
					
				}else if(attr.equals(HTML.Attribute.COLOR)){
	
					if(!attrValue.startsWith("#"))
						attrValue = HtmlColorMapping.getColor(attrValue);
					ival = Integer.decode(attrValue);
					fontAttr.setColor(new java.awt.Color(ival.intValue()));
					
				}
			}
			
		}
		return;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private void manageImageAttributes(MutableAttributeSet a, 
								      ControllerAttributes controllerAttributes) {
		
		Integer ival = null;
		
		Enumeration attrs = a.getAttributeNames();
		if(attrs == null)
			return;
		
		while(attrs.hasMoreElements()){
			
			Object oattr = attrs.nextElement();
			if(oattr == null || !(oattr instanceof HTML.Attribute))
			    continue;
			
			HTML.Attribute attr = (HTML.Attribute)oattr;
			String attrValue = (String)a.getAttribute(attr);
			if(attr.equals(HTML.Attribute.WIDTH)){
				
				ival = new Integer(attrValue);
				controllerAttributes.setWidth(ival.intValue());
				
			}else if(attr.equals(HTML.Attribute.HEIGHT)){
				
				ival = new Integer(attrValue);
				controllerAttributes.setHeight(ival.intValue());
				
			}
		}
		return;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private void manageParagraphAttributes(MutableAttributeSet a, 
		 								   ControllerAttributes controllerAttributes) {
		
		Enumeration attrs = a.getAttributeNames();
		if(attrs == null)
			return;
		
		while(attrs.hasMoreElements()){
			
			HTML.Attribute attr = (HTML.Attribute)attrs.nextElement();
			String attrValue = (String)a.getAttribute(attr);
		
			if(attr.equals(HTML.Attribute.ALIGN)){
	
				if(attrValue.equalsIgnoreCase("right")){
					controllerAttributes.setAlign(ControllerAttributes.ALIGN_RIGHT);
				}else if(attrValue.equalsIgnoreCase("left")){
					controllerAttributes.setAlign(ControllerAttributes.ALIGN_LEFT);
				}else if(attrValue.equalsIgnoreCase("center")){
					controllerAttributes.setAlign(ControllerAttributes.ALIGN_CENTER);
				}
	
			}		
		}
		return;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private void manageTableAttributes(MutableAttributeSet a,
									   ControllerAttributes controllerAttributes) {
		
		Integer ival = null;
		
		Enumeration attrs = a.getAttributeNames();
		if(attrs == null)
			return;
		
		while(attrs.hasMoreElements()){
			
			HTML.Attribute attr = (HTML.Attribute)attrs.nextElement();
			String attrValue = (String)a.getAttribute(attr);
		
			if(attr.equals(HTML.Attribute.BORDER)){
				
				ival = new Integer(attrValue);
				controllerAttributes.setBorder(ival.intValue());
				
			}else if(attr.equals(HTML.Attribute.WIDTH)){
	
				if(attrValue.endsWith("%")){
					attrValue = attrValue.substring(0,attrValue.length()-1);			
					ival = new Integer(attrValue);
					controllerAttributes.setWidth(ival.intValue());
				}
				
			}else if(attr.equals(HTML.Attribute.ALIGN)){
	
				if(attrValue.equalsIgnoreCase("right")){
					controllerAttributes.setAlign(ControllerAttributes.ALIGN_RIGHT);
				}else if(attrValue.equalsIgnoreCase("left")){
					controllerAttributes.setAlign(ControllerAttributes.ALIGN_LEFT);
				}
				
			}else if(attr.equals(HTML.Attribute.BGCOLOR)){
	
				if(!attrValue.startsWith("#"))
					attrValue = HtmlColorMapping.getColor(attrValue);
				Integer exColor = Integer.decode(attrValue);
				controllerAttributes.setBackground(new java.awt.Color(exColor.intValue()));
				
			}
		}
		return;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private void manageTitleAttributes(MutableAttributeSet a,
									   ControllerAttributes controllerAttributes) {
	
		manageParagraphAttributes(a,controllerAttributes);
		
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private AbstractController getCurDocElement(){
		if(elementsStack.size() == 0)
			return null;
		AbstractController curDocElement = (AbstractController)elementsStack.get(elementsStack.size()-1);
		return curDocElement;
	}

}
