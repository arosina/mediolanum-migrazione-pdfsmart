package prgm.pdfwebforms.drivers;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class PdfPageDriverAction {
	protected String name = "";
	public int page= 0;
	public int x = -1;
	public int y = -1;
	public String helpText = "";
	public String html = "";
	
	public PdfPageDriverAction(){
	}
	
	public PdfPageDriverAction(int page, int x, int y, String helpText, String html){
		this.page = page;
		this.x = x;
		this.y = y;
		this.helpText = helpText;
		this.html = html;
	}
}
