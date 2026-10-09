package prgm.pdfwebforms.core;

import com.atosorigin.wfem.layout.Template;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class PdfMyDialogDrawer {

	private static final String DIV_ID = "<div id='";
	private static final String SLASH_DIV = "</div>";

	/***********************************************************************************************/
	/***********************************************************************************************/
	private PdfMyDialogDrawer(){
		throw new IllegalStateException("PdfMyDialogDrawer class");
	}

    /*******************************************************************/
    /*******************************************************************/
	public static String drawDialogStartHtml(Template t, String id, String title, String height, String width) {
		return drawDialogStartHtml(t, id, title, height, width, null, null);
	}

    /*******************************************************************/
    /*******************************************************************/
	public static String drawDialogStartHtml(Template t, String id, String title, String height, String width, String closeText) {
		return drawDialogStartHtml(t, id, title, height, width, closeText, null);
	}

    /*******************************************************************/
    /*******************************************************************/
	public static String drawDialogStartHtml(Template t, String id, String title, String height, String width,
											String closeText, String actionOnClose) {
		StringBuilder sb = new StringBuilder();
		sb.append(DIV_ID+id+"' class='mydialogsplashscreen' style='display:none;'>");
		sb.append(	DIV_ID+id+"Dialog' role='dialog' tabindex='-1' class='mydialog' style='height:"+height+";width:"+width+";'>");
		sb.append(		"<div class='mydialogcontainer'>");
		sb.append(			"<div class='mydialogtitlebar'>");
		sb.append(				"<div>"+title+SLASH_DIV);
		sb.append(				"<div class='mydialogclosecontainer'>");
		if(closeText != null) {
			if(actionOnClose == null)
				actionOnClose = "closeMyDialog('"+id+"');if(curFocus!=null){curFocus.focus();}";
			sb.append(				DIV_ID+id+"myclosebutton' role='button' aria-label='"+closeText+"' tabindex='0' class='mydialogclosebutton' onclick=\""+actionOnClose+"\">");	
			sb.append(					"<img alt='' src='"+t.getWebApp()+"/images/chiudi_blu.png'>");
			sb.append(				SLASH_DIV);
		}
		sb.append(				SLASH_DIV);
		sb.append(			SLASH_DIV);
		sb.append(			"<div class='mydialogcontent'>");
		return sb.toString();
	}
	
    /*******************************************************************/
    /*******************************************************************/
	public static String drawDialogEndHtml() {
		StringBuilder sb = new StringBuilder();
		sb.append(			SLASH_DIV);
		sb.append(		SLASH_DIV);
		sb.append(	SLASH_DIV);
		sb.append(SLASH_DIV);
		return sb.toString();
	}
}
