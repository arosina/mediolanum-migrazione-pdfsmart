package com.atosorigin.wfem.htmltopdf;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.TreeMap;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.Region;
import org.jfree.chart.ChartUtilities;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.controller.RequestManager;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.IntegerType;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfWriter;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class DocumentController extends AbstractController{
	
	public static final int[] DEFAULT_HTML_FONT_SIZE_REFERENCE = new int[]{8,10,12,14,18,24,36};
	private int[] htmlFontSizeReference = DEFAULT_HTML_FONT_SIZE_REFERENCE;
	
	private ServletContext appContext;
	private RequestManager requestManager;

	private Font addNewLineFont = new Font(Font.HELVETICA,6,Font.NORMAL);

	private java.util.List documentElements = new java.util.ArrayList();

	private ByteArrayOutputStream pdfBaos;

	private PageLayout initialPageLayout = new PageLayout();
	private PageLayout currentPageLayout = null;
	
	private Document 		pdfDocument = new Document(initialPageLayout.getPageSize());
	private HSSFWorkbook	wb = new HSSFWorkbook();
	private HSSFSheet		calcDocument;
	private HSSFPatriarch 	calcPatriarch;
	private TreeMap			calcRows = new TreeMap();
	private int 			calcGlobalRowIdx;
	private int 			calcGlobalColIdx;
	private HSSFCell 		currentCell;
	private CalcCellStylePool calcCellStylePool;

	private CommandDataModel pageModel;
	private Vector fonts = new Vector();
	
	private int chartCounter = 0;
	
	class NewPageIndicator{}
	
	class NewLineIndicator{}
	
	class StartContentIndicator{}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public DocumentController(ByteArrayOutputStream html, ServletContext appContext, 
							  RequestManager requestManager,  CommandDataModel pageModel) throws Exception{
	
		super(null,null);
		setDocumentController(this);
		getCalcRegion().setStartRow(0);
		getCalcRegion().setStartCol(0);
	
		this.appContext = appContext;
		this.requestManager = requestManager;
		this.pageModel = pageModel;
		
		pdfBaos = new ByteArrayOutputStream();
		writer = PdfWriter.getInstance(pdfDocument, pdfBaos);
		
	    ByteArrayInputStream inpHtml = new ByteArrayInputStream(html.toByteArray());
		InputStreamReader isr = new InputStreamReader(inpHtml,requestManager.getResponse().getCharacterEncoding());
	    HtmlParser parser = new HtmlParser(this,writer,true);
	    parser.parse(isr);
	
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public DocumentController(String html) throws Exception {
		
		super(null,null);
		setDocumentController(this);
		getCalcRegion().setStartRow(0);
		getCalcRegion().setStartCol(0);
	
		this.appContext = null;
		
		pdfBaos = new ByteArrayOutputStream();
		writer = PdfWriter.getInstance(pdfDocument, (OutputStream)pdfBaos);

	    ByteArrayInputStream inpHtml = new ByteArrayInputStream(html.getBytes());
		InputStreamReader isr = new InputStreamReader(inpHtml);
	    HtmlParser parser = new HtmlParser(this,writer,false);
	    parser.parse(isr);
	
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public DocumentController(String inputFileName,String outputFileName) throws Exception {
		
		super(null,null);
		setDocumentController(this);
		getCalcRegion().setStartRow(0);
		getCalcRegion().setStartCol(0);
	
		this.appContext = null;
		
		FileOutputStream pdfFile = new FileOutputStream(outputFileName);
		writer = PdfWriter.getInstance(pdfDocument, (OutputStream)pdfFile);

	    Reader reader = new FileReader(inputFileName);
	    HtmlParser parser = new HtmlParser(this,writer,false);
	    parser.parse(reader);
	
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void addFont(FontAttributes fontAttr) {
		fonts.add(fontAttr);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void addHParagraph(HParagraphController hParagraphController){
		documentElements.add(hParagraphController);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void addImage(ImageController image){
		documentElements.add(image);	
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void addNewLine() throws HtmlTagNotSupported{	
		documentElements.add(new NewLineIndicator());
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void addNewPage() throws HtmlTagNotSupported{	
		documentElements.add(new NewPageIndicator());
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void addPageLayout(PageLayout pageLayout) throws CommentTagNotSupported{
		documentElements.add(pageLayout);
		currentPageLayout = pageLayout;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void addParagraph(ParagraphController paragraph){
		documentElements.add(paragraph);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void addTable(TableController table){
		documentElements.add(table);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void addText(String text){
	
		TextController textController = new TextController(this,text,this);
		textController.setWriter(writer);
		documentElements.add(textController);
	
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void addTitle(DocumentTitleController title){
		documentElements.add(title);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public Image loadImageAsIText(String imageName,  int width, int height) {
		try{
			Image image = Image.getInstance(loadImageAsByteArray(imageName,-1,-1));
			if(width > 0 && height > 0)
				image.scaleToFit(width,height);
			return image;
		}catch (Exception e) {
			return null;
		}
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public byte[] loadImageAsByteArray(String imageName, int width, int height) {
		try{
			BufferedImage image = loadImageAsBuffered(imageName,width,height);
			return ChartUtilities.encodeAsPNG(image);
		}catch(Exception e){
			return null;
		}
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public BufferedImage loadImageAsBuffered(String imageName, int width, int height) {
		BufferedImage image = null;
		java.net.URL imageUrl = null;
		try{
			if(appContext != null){
				try{
					int idx = imageName.substring(1).indexOf("/")+1;
					if(imageName.substring(0,1).equals("/") && idx > 1){
						String webApp = imageName.substring(0,idx);
						imageUrl = appContext.getContext(webApp).getResource(imageName.substring(webApp.length()));
						image = ImageIO.read(imageUrl);
					}
				}catch(Exception e){
					image = null;
				}
			}

			if(image == null){
				try{
					if(appContext != null)
						imageUrl = appContext.getResource(imageName);
					else
						imageUrl = new java.net.URL(imageName);
					image = ImageIO.read(imageUrl);
				}catch(java.net.MalformedURLException mu){
					try{
						image = ImageIO.read(new File(imageName));
					}catch(Throwable t){
						image = null;
					}
				}
			}
			
			// Try as resource
			if(image == null){
				InputStream is = imageName.getClass().getResourceAsStream(imageName);
				image = ImageIO.read(is);
			}
			
			if(image == null)
				return null;
			
			if(width > 0 && height > 0){
				java.awt.Image scaledImage = image.getScaledInstance(width,height,BufferedImage.SCALE_DEFAULT);
				image = new BufferedImage(scaledImage.getWidth(null),scaledImage.getHeight(null),BufferedImage.TYPE_INT_RGB);
				Graphics g = image.getGraphics();
				g.drawImage(scaledImage, 0, 0, null);
				g.dispose();
			}
			return image;
		}catch(Exception e){
			return null;
		}
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public Object generatePdfDocument() throws Exception  {
	
		PageEventsManager evManager = new PageEventsManager();
	    writer.setPageEvent(evManager);
	
	    // Find the initial page layout definition
	    // before the <body> tag (If defined, else all at default values)
		for(int i=0;i<documentElements.size();i++){
			
			Object documentElement = documentElements.get(i);
			if(documentElement instanceof StartContentIndicator){
				
				break;
				
			}else if(documentElement instanceof PageLayout){
				
				PageLayout pageLayout = (PageLayout)documentElement;
				Rectangle r = pageLayout.getPageSize();
				pdfDocument.setPageSize(r);
				
				setCurrentPageLayout(pageLayout);
				documentElements.remove(i);
				break;
				
			}
			
		}
		
	    // Find the document title
		for(int i=0;i<documentElements.size();i++){
			
			Object documentElement = documentElements.get(i);
			if(documentElement instanceof DocumentTitleController){
	
				DocumentTitleController title = (DocumentTitleController)documentElement;
				pdfDocument.addTitle(title.getTitle());
				documentElements.remove(i);
				break;
				
			}
			
		}
	
		pdfDocument.setMargins(getCurrentPageLayout().getPointMarginLeft(),getCurrentPageLayout().getPointMarginRigth(),
							   getCurrentPageLayout().getPointMarginTop(),getCurrentPageLayout().getPointMarginBottom());
		
		pdfDocument.open();
	
		if(getCurrentPageLayout().getPageHeader() != null)
			evManager.addHeader(getCurrentPageLayout());
			
		if(getCurrentPageLayout().getPageFooter() != null)
			evManager.addFooter(getCurrentPageLayout());
			
		for(int i=0;i<documentElements.size();i++){
			
			Object documentElement = documentElements.get(i);
			
			if(documentElement instanceof NewPageIndicator){
				
				pdfDocument.newPage();
				
			}else if(documentElement instanceof NewLineIndicator){
				
				pdfDocument.add(new Paragraph(new Phrase(" ",addNewLineFont)));
				
			}else if(documentElement instanceof PdfFileController){
				
				((PdfFileController)documentElement).includePdfDocument();
				
			}else if(documentElement instanceof PageLayout){
	
				PageLayout pageLayout = (PageLayout)documentElement;
				Rectangle r = pageLayout.getPageSize();
				pdfDocument.setPageSize(r);
				
				pdfDocument.setMargins(pageLayout.getPointMarginLeft(),pageLayout.getPointMarginRigth(),
									   pageLayout.getPointMarginTop(),pageLayout.getPointMarginBottom());
	
				pdfDocument.newPage();
				
				if(pageLayout.isNewBlock())
					evManager.addBlock();
	
				if(pageLayout.getPageHeader() != null)
					evManager.addHeader(pageLayout);
					
				if(pageLayout.getPageFooter() != null)
					evManager.addFooter(pageLayout);
					
				setCurrentPageLayout(pageLayout);
				
			}else if(documentElement instanceof AbstractController){
				
				AbstractController documentElementController = (AbstractController)documentElement;
				Object pdfElement = documentElementController.generatePdfDocument();
				if(pdfElement != null)
					pdfDocument.add((Element)pdfElement);
					
			}
		}
	
		pdfDocument.close();
		if(pdfBaos != null)
			return pdfBaos;
		else
			return null;	
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public Object generateCalcDocument() throws Exception  {
	
	    // Find the document title
		for(int i=0;i<documentElements.size();i++){
			
			Object documentElement = documentElements.get(i);
			if(documentElement instanceof DocumentTitleController){
	
				DocumentTitleController title = (DocumentTitleController)documentElement;
				String sheetTitle = "";
				if(title.getTitle() != null)
					sheetTitle = title.getTitle();
				if(sheetTitle.length() > 20)
					sheetTitle = sheetTitle.substring(0,20)+"...";
				if(sheetTitle.equals(""))
					calcDocument = wb.createSheet();
				else
					calcDocument = wb.createSheet(sheetTitle);
				
				documentElements.remove(i);
				break;
				
			}
			
		}
		if(calcDocument == null)
			calcDocument = wb.createSheet();
		
		calcCellStylePool = new CalcCellStylePool(wb);
	
		for(int i=0;i<documentElements.size();i++){
			
			Object documentElement = documentElements.get(i);
			
			if(documentElement instanceof NewPageIndicator){
				
				createCalcNewRow();
				
			}else if(documentElement instanceof NewLineIndicator){
				
				createCalcNewRow();
				
			}else if(documentElement instanceof AbstractController){
				
				AbstractController documentElementController = (AbstractController)documentElement;
				documentElementController.generateCalcDocument();
					
			}
		}
	
		ByteArrayOutputStream calcBaos = new ByteArrayOutputStream();
		wb.write(calcBaos);
		calcBaos.close();
		return calcBaos;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public javax.servlet.ServletContext getAppContext() {
		return appContext;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public Object getCurrentDocumentElement(){
		if(documentElements.size() == 0)
			return this;
		return documentElements.get(documentElements.size()-1);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public FontAttributes getCurrentFontAttributes() {
		if(fonts.size() == 0)
			return getCurrentPageLayout().getPageDefaultFontAttributes();
		return (FontAttributes)fonts.get(fonts.size()-1);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public PageLayout getCurrentPageLayout() {
		if(currentPageLayout == null)
			return initialPageLayout;
		return currentPageLayout;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public java.io.ByteArrayOutputStream getPdfBaos() {
		return pdfBaos;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static void main (String[] args) {
	
		if(args.length != 2){
			System.out.println("DocumentController parameters: fileIn fileOut");
			System.out.println("   fileIn:  input HTML file to convert");
			System.out.println("   fileOut: resulting PDF");
			return;
		}
		
		try{
			
			DocumentController doc = new DocumentController(args[0],args[1]);
			doc.generatePdfDocument();
			System.exit(0);
	
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void removeFont() {
		if(fonts.size() == 0)
			return;
			
		fonts.remove(fonts.size()-1);
		fonts.trimToSize();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setCurrentPageLayout(PageLayout newCurrentPageLayout) {
		currentPageLayout = newCurrentPageLayout;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void startContentNotification() throws ControllerTypeNotSupported{	
		documentElements.add(new StartContentIndicator());
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void addPdfFile(String pdfFileName) throws ControllerTypeNotSupported {
		documentElements.add(new PdfFileController(this,pdfFileName));
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public CommandDataModel getPageModel() {
		return pageModel;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public Document getPdfDocument() {
		return pdfDocument;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private HSSFRow getOrCreateCalcRow(int rowIdx) {
		HSSFRow calcRow = (HSSFRow)calcRows.get(new Integer(rowIdx));
		if(calcRow == null){
			calcRow = calcDocument.createRow(rowIdx);
			calcRows.put(new Integer(rowIdx),calcRow);
		}
		return calcRow;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	protected void createCalcNewRow() {
		getOrCreateCalcRow(calcGlobalRowIdx++);
		setCalcGlobalColIdx(0);
		currentCell = null;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	protected void createCalcOneRowCell(TextController text, boolean canAppend) {
		if(canAppend && currentCell != null){
			String newValue = "";
			HSSFRichTextString value = currentCell.getRichStringCellValue();
			if(value != null)
				newValue = value.toString();
			newValue += text.getText();
			currentCell.setCellValue(new HSSFRichTextString(newValue));
		}else{
			
			HSSFRow	calcRow = getOrCreateCalcRow(calcGlobalRowIdx);
			currentCell = calcRow.createCell((short)0);
			setCalcGlobalColIdx(0);
			currentCell.setCellValue(new HSSFRichTextString(text.getText()));
			
			CalcCellStyle calcCellStyle = calcCellStylePool.getOneRowCellStyle(text);
			currentCell.setCellStyle(calcCellStyle.getCellStyle());
			calcDocument.addMergedRegion(new Region(calcGlobalRowIdx,(short)0,calcGlobalRowIdx,(short)12));
			calcGlobalRowIdx++;
		}
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	protected void createTableCalcCell(CellController cell) {
		int rowIdx = cell.getCalcRegion().getStartRow();
		int colIdx = cell.getCalcRegion().getStartCol();
		if(rowIdx > calcGlobalRowIdx)
			calcGlobalRowIdx = rowIdx;
		
		HSSFRow	calcRow = getOrCreateCalcRow(rowIdx);
		currentCell = calcRow.createCell((short)colIdx);
		setCalcGlobalColIdx(colIdx);
		
		CalcCellStyle calcCellStyle = calcCellStylePool.getTableCellStyle(cell);
		String cellValue = ((TextController)cell.getCellContent()).getText();
		String cellFormat = calcCellStyle.getCellFormat();
		if(cellFormat.equals("date")){
			DateType d = new DateType(cellValue);
			currentCell.setCellValue(d.dateValue());
		}else if(cellFormat.equals("double")){
			DoubleType d = new DoubleType(cellValue);
			currentCell.setCellValue(d.doubleValue());
		}else if(cellFormat.equals("int")){
			IntegerType d = new IntegerType(cellValue);
			currentCell.setCellValue(d.intValue());
		}else{
			currentCell.setCellValue(new HSSFRichTextString(cellValue));
		}
		
		currentCell.setCellStyle(calcCellStyle.getCellStyle());
		mergeCalcRegion(cell.getCalcRegion());
		return;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	protected void mergeCalcRegion(CalcRegion calcRegion){
		if(calcRegion.getStartRow() == calcRegion.getEndRow() &&
		   calcRegion.getStartCol() == calcRegion.getEndCol())
			return;
		calcDocument.addMergedRegion(new Region(calcRegion.getStartRow(),(short)calcRegion.getStartCol(),calcRegion.getEndRow(),(short)calcRegion.getEndCol()));
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	protected void createCalcImage(byte[] image, CalcRegion calcRegion) {
		int imgindex=wb.addPicture(image,HSSFWorkbook.PICTURE_TYPE_PNG);
		if(calcPatriarch == null)
			calcPatriarch=calcDocument.createDrawingPatriarch();
		HSSFClientAnchor anchor = new HSSFClientAnchor(0,0,0,0,(short) calcRegion.getStartCol(),calcRegion.getStartRow(),
															   (short)calcRegion.getEndCol(),calcRegion.getEndRow());
		anchor.setAnchorType(2);
		calcPatriarch.createPicture(anchor,imgindex);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	protected void createCalcChart(String chartTitle, byte[] image, CalcRegion calcRegion) {
		chartCounter++;
		
		if(chartTitle == null || chartTitle.length() == 0)
			chartTitle = "Chart";
		String sheetTitle = "Chart"+chartCounter;
		
		HSSFRow	calcRow = getOrCreateCalcRow(calcGlobalRowIdx);
		currentCell = calcRow.createCell((short)calcGlobalColIdx);
		currentCell.setCellValue(new HSSFRichTextString(sheetTitle+" -> "+chartTitle));
		HSSFCellStyle cellStyle = calcCellStylePool.getChartRefCellStyle();
		currentCell.setCellStyle(cellStyle);
		
		int imgindex=wb.addPicture(image,HSSFWorkbook.PICTURE_TYPE_PNG);
		HSSFSheet chartSheet = wb.createSheet(sheetTitle);
		HSSFPatriarch chartPatriarch=chartSheet.createDrawingPatriarch();
		HSSFClientAnchor anchor = new HSSFClientAnchor(0,0,0,0,(short)0,0,
															   (short)(calcRegion.getEndCol()-calcRegion.getStartCol()),
															   calcRegion.getEndRow()-calcRegion.getStartRow());
		anchor.setAnchorType(2);
		chartPatriarch.createPicture(anchor,imgindex);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	protected CalcRegion getImageRegion(BufferedImage image, int startRow, int startCol) {
		CalcRegion result = new CalcRegion(startRow,startCol,-1,-1);
		int ih = image.getHeight();
		int iw = image.getWidth();
		int ch = 17;
		int cw = 64;
		
		int numRows = (ih/ch)+1;
		int numCols = (iw/cw)+1;
		result.setEndRow(numRows+startRow);
		result.setEndCol(numCols+startCol);
		return result;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public RequestManager getRequestManager() {
		return requestManager;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public int getCalcGlobalColIdx() {
		return calcGlobalColIdx;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setCalcGlobalColIdx(int calcGlobalColIdx) {
		this.calcGlobalColIdx = calcGlobalColIdx;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public int getCalcGlobalRowIdx() {
		return calcGlobalRowIdx;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setCalcGlobalRowIdx(int calcGlobalRowIdx) {
		this.calcGlobalRowIdx = calcGlobalRowIdx;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public int[] getHtmlFontSizeReference() {
		return htmlFontSizeReference;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setHtmlFontSizeReference(int[] htmlFontSizeReference) {
		this.htmlFontSizeReference = htmlFontSizeReference;
	}

}
