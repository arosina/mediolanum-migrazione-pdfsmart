package com.atosorigin.wfem.layout.htmlrenderer;

import java.util.ListIterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.controller.Configuration;
import com.atosorigin.wfem.controller.Constants;
import com.atosorigin.wfem.htmltopdf.PageLayoutSyntaxConstants;
import com.atosorigin.wfem.layout.FieldRenderer;
import com.atosorigin.wfem.layout.GridDecorator;
import com.atosorigin.wfem.layout.HtmlColorMapping;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

/**************************************************************************************************/
/**************************************************************************************************/
public class GridRenderer extends FieldRenderer{
	
	private static final int HORZ_SCROLL_HEIGHT = 24;

	private static final int STRBUF_SIZE = 2*1024;
	private static final int BIG_STRBUF_SIZE = 10*1024;
	
	private static final String FILLED_LT = "&#9668;";
	private static final String FILLED_GT = "&#9658;";
	
	private static final String HEADER_COLOR = "#1A458F";
	private static final String HEADER_BACKGROUND = "lightgrey";
	
	private Template template;
	private Template wfemTemplate;
	private String   listPropName;
	private ListType listValue;
	private Vector	 cols;
	private Vector	 visibleCols;
	private Vector	 printableCols;
	private Vector	 hiddenCols;
	private Vector	 colWidths;
	private Vector	 visibleColsEvents;
	
	private Vector colTitles = new Vector();
	
	GridPar gridPar = new GridPar();
	
	GridDecorator gridDecorator;
	
	static class Patterns{
		private static final String paramPattern="\\s*=\\s*(['\"])(.*?)\\1";
		static final Pattern modality         	 = Pattern.compile("\\s*"+"modality"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern fieldsmodality   	 = Pattern.compile("\\s*"+"fieldsmodality"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern pageformname     	 = Pattern.compile("\\s*"+"pageformname"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern title            	 = Pattern.compile("\\s*"+"title"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern onclick          	 = Pattern.compile("\\s*"+"onclick"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern ondblclick       	 = Pattern.compile("\\s*"+"ondblclick"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern onnewrow         	 = Pattern.compile("\\s*"+"onnewrow"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern onnewcell        	 = Pattern.compile("\\s*"+"onnewcell"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern onnewheader     	 = Pattern.compile("\\s*"+"onnewheader"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern counterwidth     	 = Pattern.compile("\\s*"+"counterwidth"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern gridwidth        	 = Pattern.compile("\\s*"+"gridwidth"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern gridbackground   	 = Pattern.compile("\\s*"+"gridbackground"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern cellheight       	 = Pattern.compile("\\s*"+"cellheight"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern headerheight     	 = Pattern.compile("\\s*"+"headerheight"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern topareaheight    	 = Pattern.compile("\\s*"+"topareaheight"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern bottomareaheight 	 = Pattern.compile("\\s*"+"bottomareaheight"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern noRowsMsg        	 = Pattern.compile("\\s*"+"noRowsMsg"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern selection        	 = Pattern.compile("\\s*"+"selection"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern sortable         	 = Pattern.compile("\\s*"+"sortable"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern showcounter      	 = Pattern.compile("\\s*"+"showcounter"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern headerbackground 	 = Pattern.compile("\\s*"+"headerbackground"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern headercolor      	 = Pattern.compile("\\s*"+"headercolor"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern countertitle     	 = Pattern.compile("\\s*"+"countertitle"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern counterbackground	 = Pattern.compile("\\s*"+"counterbackground"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern countercolor     	 = Pattern.compile("\\s*"+"countercolor"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern headernowrap     	 = Pattern.compile("\\s*"+"headernowrap"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern cellsnowrap      	 = Pattern.compile("\\s*"+"cellsnowrap"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern showtoparea      	 = Pattern.compile("\\s*"+"showtoparea"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern showbottomarea   	 = Pattern.compile("\\s*"+"showbottomarea"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern helper           	 = Pattern.compile("\\s*"+"helper"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern decorator        	 = Pattern.compile("\\s*"+"decorator"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern pdf              	 = Pattern.compile("\\s*"+"pdf"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern excel            	 = Pattern.compile("\\s*"+"excel"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern width            	 = Pattern.compile("\\s*"+"width"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern height           	 = Pattern.compile("\\s*"+"height"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern footerhtml          = Pattern.compile("\\s*"+"footerhtml"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern containscdata       = Pattern.compile("\\s*"+"containscdata"+paramPattern,Pattern.CASE_INSENSITIVE);
	}
	
    /**************************************************************************************************/
	/**************************************************************************************************/
	class GridPar{
		
		String strModality;
		String strFieldsModality;
		int	   fieldsModality;
		int	   modality;
		boolean doForm = false;
		
		String paginationformname = "";
		String title = "";
		String onclick = "";
		String ondblclick = "";
		String onnewrow = "";
		String onnewcell = "";
		String onnewheader = "";
		String counterwidth = "";
		String gridwidth = "";
		String width = "";
		String height = "";
		String cellheight = "";
		String headerheight = "";
		String noRowsMsg = "";
		String selection = "";
		String sortable = "";
		String showcounter = "";
		String gridbackground = "";
		String headerbackground = "";
		String headercolor = "";
		String countertitle = "";
		String counterbackground = "";
		String countercolor = "";
		boolean headernowrap;
		boolean cellsnowrap;
		boolean showtoparea;
		boolean showbottomarea;
		String topareaheight = "";
		String bottomareaheight = "";
		String helper = "";
		String decorator = "";
		String pdf = "";
		String excel = "";
		String footerhtml = "";
		boolean containscdata;
		
		String extraPar;
		
	    /**************************************************************************************************/
		/**************************************************************************************************/
		public GridPar(){
		}
		
	    /**************************************************************************************************/
		/**************************************************************************************************/
		public void loadPar(String extraPar){
			
			this.extraPar = extraPar;

			strModality = getExtPar(Patterns.modality,"read");
			if(strModality.equalsIgnoreCase("insert"))
				modality = Template.INSERT_MODALITY;
			else if(strModality.equalsIgnoreCase("update"))
				modality = Template.UPDATE_MODALITY;
			else if(strModality.equalsIgnoreCase("read"))
				modality = Template.READ_MODALITY;
			else if(strModality.equalsIgnoreCase("print"))
				modality = Template.PRINT_MODALITY;

			if(modality == Template.PRINT_MODALITY){
				fieldsModality = Template.PRINT_MODALITY;
			}else{
				strFieldsModality = getExtPar(Patterns.fieldsmodality,strModality);
				if(strFieldsModality.equalsIgnoreCase("insert"))
					fieldsModality = Template.INSERT_MODALITY;
				else if(strFieldsModality.equalsIgnoreCase("update"))
					fieldsModality = Template.UPDATE_MODALITY;
				else if(strFieldsModality.equalsIgnoreCase("read"))
					fieldsModality = Template.READ_MODALITY;
			}

			paginationformname = getExtPar(Patterns.pageformname);
			if((listValue.getTotalPages() > 1 && listValue.getRowsPerPage() != -1) &&
				paginationformname.equals("")){
				doForm = true;
				paginationformname = listPropName+"PaginationForm";
			}
				
			title = getExtPar(Patterns.title);
			footerhtml = getExtPar(Patterns.footerhtml);
			onclick = getExtPar(Patterns.onclick);
			ondblclick = getExtPar(Patterns.ondblclick);
			onnewrow = getExtPar(Patterns.onnewrow);
			onnewcell = getExtPar(Patterns.onnewcell);
			onnewheader = getExtPar(Patterns.onnewheader);
			counterwidth = getExtPar(Patterns.counterwidth,"20px");
			gridwidth = getExtPar(Patterns.gridwidth);
			gridbackground = getExtPar(Patterns.gridbackground,"ivory");
			cellheight = getExtPar(Patterns.cellheight,"18px");
			headerheight = getExtPar(Patterns.headerheight,"18"); 			headerheight = getNum(headerheight)+"px";
			topareaheight = getExtPar(Patterns.topareaheight,"20");  		topareaheight = getNum(topareaheight)+"px";
			bottomareaheight = getExtPar(Patterns.bottomareaheight,"20"); 	bottomareaheight = getNum(bottomareaheight)+"px";
			noRowsMsg = getExtPar(Patterns.noRowsMsg,wfemTemplate.getProperty("gridMsg.noRows"));
			selection = getExtPar(Patterns.selection,"multi");
			sortable = getExtPar(Patterns.sortable,"true");
			showcounter = getExtPar(Patterns.showcounter,"true");
			headercolor = getExtPar(Patterns.headercolor,HEADER_COLOR);
			headerbackground = getExtPar(Patterns.headerbackground,HEADER_BACKGROUND);
			countertitle = getExtPar(Patterns.countertitle,"&nbsp;");
			counterbackground = getExtPar(Patterns.counterbackground,HEADER_BACKGROUND);
			countercolor = getExtPar(Patterns.countercolor,HEADER_COLOR);
			headernowrap = Boolean.valueOf(getExtPar(Patterns.headernowrap,"true")).booleanValue();
			cellsnowrap = Boolean.valueOf(getExtPar(Patterns.cellsnowrap,"true")).booleanValue();
			showtoparea = Boolean.valueOf(getExtPar(Patterns.showtoparea,"true")).booleanValue();
			showbottomarea = Boolean.valueOf(getExtPar(Patterns.showbottomarea,"true")).booleanValue();
			containscdata = Boolean.valueOf(getExtPar(Patterns.containscdata,"false")).booleanValue();
			helper = getExtPar(Patterns.helper,"no");
			decorator = getExtPar(Patterns.decorator);
			pdf = getExtPar(Patterns.pdf,"false");
			excel = getExtPar(Patterns.excel,"false");

			width = getExtPar(Patterns.width,"800px");
			height = getExtPar(Patterns.height,"270px");
		
			if(gridwidth.equals(""))
				gridwidth = "100%";
			else
				gridwidth = getNum(gridwidth)+getMeasure(gridwidth);
		}
		
		/**************************************************************************************************/
		/**************************************************************************************************/
		private String getExtPar(Pattern pattern){
			return getExtPar(pattern,null);
		}

		/**************************************************************************************************/
		/**************************************************************************************************/
		private String getExtPar(Pattern pattern, String defaultValue){
			String result = "";
			try{
				Matcher mat = pattern.matcher(this.extraPar);
				if(mat.find()){
					result = mat.group(2);
					this.extraPar = this.extraPar.substring(0,mat.start())+this.extraPar.substring(mat.end());
				}else{
					if(defaultValue != null)
						result = defaultValue;
				}
			}catch(Exception e){ e.printStackTrace(); }
			return result;
		}
	}
	
    /**************************************************************************************************/
	/**************************************************************************************************/
	public GridRenderer(Template template, Template wfemTemplate, String listPropName, ListType listValue,
						Vector parCols, Vector parColWidths, String extraPar){
		
		listValue.recalc();
		
		boolean columnsFromDb = false;
		// Colums non specified by developer. Use the db columns
		if(parCols == null || parCols.size() == 0){
			parCols = new Vector();
			String[] colProps = listValue.getPropertyNames();
			for(int i=0;i<colProps.length;i++)
				parCols.add(colProps[i]);
			columnsFromDb = true;
		}
		
		this.template = template;
		this.wfemTemplate = wfemTemplate;
		this.listPropName = listPropName;
		this.listValue = listValue;
		this.cols = parCols;
		this.colWidths = parColWidths;
		
		gridPar.loadPar(extraPar);
		
		// Load visible, printable and hidden columns
		visibleCols = new Vector();
		printableCols = new Vector();
		hiddenCols = new Vector();
		visibleColsEvents = new Vector();
		for(int i=0;i<cols.size();i++){
			String col = (String)cols.get(i);
			if(col.startsWith("#")){
				col = col.substring(1);
				hiddenCols.add(col);
				cols.set(i,col);
			}else if(col.startsWith("@")){
				col = col.substring(1);
				visibleCols.add(col);
				printableCols.add(null);
				cols.set(i,col);
			}else{
				visibleCols.add(col);
				printableCols.add(col);
			}
			
		}
		
		// Load columns titles
		if(columnsFromDb){
			String[] coltit = listValue.getColumnLabels();
			for(int i=0;i<coltit.length;i++)
				colTitles.add(coltit[i]);
		}else{
			for(int i=0;i<visibleCols.size();i++){
				if(template.getLabelCodePrefix() != null && !template.getLabelCodePrefix().equals(""))
					colTitles.add(template.getProperty(template.getLabelCodePrefix()+visibleCols.get(i)));
				else
					colTitles.add(template.getProperty(template.getPageName()+listPropName+"."+visibleCols.get(i)));
			}
		}
		
		// Manage columns widths
		if(colWidths == null || colWidths.size() == 0){
			colWidths = new Vector();
			for(int i=0;i<visibleCols.size();i++)
				colWidths.add("*");
		}
		calculateColumnsWidth();
		
		// Manage columns events
		for(int i=0;i<visibleCols.size();i++){
			String parName = (String)visibleCols.get(i);
			String event = "";
			try{
				Matcher mat = Pattern.compile("\\s*"+parName+"\\s*\\=\\s*\\{([^\\}]+)\\}").matcher(extraPar);
				if(mat.find()){
					event = mat.group(1);
					extraPar = extraPar.substring(0,mat.start())+extraPar.substring(mat.end());
				}
			}catch(Exception e){ e.printStackTrace(); }
			visibleColsEvents.add(event);
		}
		
		// Load grid decorator
		if(!gridPar.decorator.equals("")){
			try{
				gridDecorator = (GridDecorator)Class.forName(gridPar.decorator).newInstance();
			}catch(Exception e){gridDecorator=null; e.printStackTrace();}
		}
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private void calculateColumnsWidth(){
		int numNoWidth = 0;
		int totPercent = 0;
		for(int i=0;i<colWidths.size();i++){
			String colWidth = (String)colWidths.get(i);
			if(colWidth.equals("*"))
				numNoWidth++;
			else
				totPercent += Integer.parseInt(getNum(colWidth));
		}
		if(numNoWidth == 0)
			return;
		
		int restPercent = 100-totPercent;
		int noWidthPercent = (int)restPercent/numNoWidth;
		for(int i=0;i<colWidths.size();i++){
			String colWidth = (String)colWidths.get(i);
			if(colWidth.equals("*"))
				colWidths.set(i,noWidthPercent+"%");
		}
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getHtml() throws Exception{		
		if(gridPar.modality == Template.PRINT_MODALITY){
			return getPdfCalc(true,true);
		}
		
		StringBuffer result = new StringBuffer(BIG_STRBUF_SIZE);
		if(listValue.size() == 0){
			String toth = gridPar.height;
			if(!toth.endsWith("%")){
				int height = 0;
				if(gridPar.showtoparea)
					height += Integer.parseInt(getNum(gridPar.topareaheight));
				if(gridPar.showbottomarea)
					height += Integer.parseInt(getNum(gridPar.bottomareaheight));
				height += Integer.parseInt(getNum(gridPar.headerheight));
				height += Integer.parseInt(getNum(gridPar.height));
				toth = ""+height;
			}
			String totw = gridPar.width;
			if(!totw.endsWith("%"))
				totw = ""+getNum(gridPar.width)+getMeasure(gridPar.width);
		
			result.append("<div id='"+listPropName+"Grid' style='width:"+totw+";height:"+toth+";'>");
			result.append("<table class='message' width='"+totw+"' height='"+toth+"' style='border:solid darkgray 1px;vertical-align:top;'><tr><td align='center'>"+gridPar.noRowsMsg+"</td></tr></table>");
			result.append("</div>");
			return result.toString();
		}

		template.setInGrid(true);
		
		boolean isHiddenSubmit=false;
		if(template.isFeatureIncluded(Template.FEATURE_HIDDEN_SUBMIT))
			isHiddenSubmit = true;
		
		if(isHiddenSubmit)
			result.append("<div id='"+listPropName+"GridHiddenSubmitContainer' style='width:"+gridPar.width+";height:"+gridPar.height+";'>\n");
		
		result.append("<div id='"+listPropName+"Grid' style='border: solid 1px darkgray;background-color:"+gridPar.gridbackground+";"+
				           "width:"+gridPar.width+";height:"+gridPar.height+";'>\n");

		/* *************  Pagination Form (start) ************************** */
		if(gridPar.doForm)
			result.append(getPaginationForm());
		
		result.append("<table cellpadding='0' cellspacing='0' width='100%' height='"+gridPar.height+"'>\n");
		
		/* *************  Pagination   ************************** */
		if(gridPar.showtoparea){
			String pagination = getPagination();
			result.append("<tr>\n");
			result.append("<td colspan='2' align='center' class='text' nowrap style='background-color:"+gridPar.gridbackground+";height:"+gridPar.topareaheight+";'>\n");
			if(pagination != null)
				result.append("  <table cellpadding='0' cellspacing='0' width='100%' style='table-layout:fixed;'><tr>\n");
			else
				result.append("  <table cellpadding='0' cellspacing='0' width='100%'><tr>\n");
			result.append("  <td class='text' style='font-weight:bold;padding-left:3;'>"+gridPar.title+"</td>");
			if(pagination != null)
				result.append("  <td align='center'>"+pagination+"</td>");
			result.append("  <td align='right'><table cellpadding='1' cellspacing='1'><tr><td>\n");
			String title = gridPar.title.replaceAll("\\<br\\>"," ");
			if(Boolean.valueOf(gridPar.pdf).booleanValue()){
				result.append("<img src='"+Configuration.getInstance().getWfemlayoutWebApp()+"/images/pdf.gif' style='cursor:pointer;' title='"+wfemTemplate.getProperty("gridMsg.openPdf")+"' "+
			    				"onclick='openGridPdf(\""+title+"\",\""+gridPar.paginationformname+"\",\""+listPropName+"\",\""+template.getBrowserInstance()+"\");'>");
			}
			if(Boolean.valueOf(gridPar.excel).booleanValue()){
				result.append("<img src='"+Configuration.getInstance().getWfemlayoutWebApp()+"/images/calc.gif' style='cursor:pointer;' title='"+wfemTemplate.getProperty("gridMsg.openCalc")+"' "+
								"onclick='openGridCalc(\""+title+"\",\""+gridPar.paginationformname+"\",\""+listPropName+"\",\""+template.getBrowserInstance()+"\");'>");
			}
			result.append("&nbsp;&nbsp;&nbsp;&nbsp;</td></tr></table></td>\n");
			result.append("  </tr></table>\n");
			result.append("</td>\n");
			result.append("</tr>\n");
		}
		/* ************************************************ */

		/* *************  Grid  ************************* */
		result.append("<tr>\n");

		/* *************  Counters  ********************* */
		result.append("<td valign='top'>\n");
		result.append(getGridCounters());
		result.append("</td>\n");
		
		/* *************  Body   ************************** */
		result.append("<td valign='top' width='100%'>\n");
		result.append("  <table height='100%' width='100%' cellpadding='0' cellspacing='0'><tr><td>\n");
		result.append(getGridHeader());
		result.append("  </td></tr><tr><td height='100%'>\n");
		result.append(getGridBody());
		result.append("  </td></tr></table>\n");		
		result.append("</td>\n");

		result.append("</tr>\n");
		/* ************************************************ */
		
		/* *************  Footer   ************************** */
		if(gridPar.showbottomarea || gridPar.footerhtml.length() > 0){
			result.append("<tr>\n");
			result.append("<td colspan='2' valign='top' class='text' style='height:"+gridPar.bottomareaheight+";'>\n");
			if(gridPar.footerhtml.length() == 0)
				result.append(getFooterMessage());
			else
				result.append(gridPar.footerhtml);
			result.append("</td>\n");
			result.append("</tr>\n");
		}
		/* ************************************************ */
		
		result.append("</table>\n");		

		/* *************  Pagination Form (end) ************************** */
		if(gridPar.doForm)
			result.append("</form>\n");			

		result.append("</div>\n");

		result.append("<script>\n");
		result.append("document."+listPropName+"Grid = new GridList('"+listPropName+"Grid'," +
										  				  "'"+gridPar.selection+"'," +
										  				  "'"+gridPar.sortable+"'," +
										  				  "'"+gridPar.onclick+"'," +
										  				  "'"+gridPar.ondblclick+"'," +
										  				  "'"+gridPar.onnewrow+"'," +
										  				  "'"+gridPar.onnewcell+"'," +
										  				  "'"+gridPar.strModality+"'," +
		  												  "'"+gridPar.strFieldsModality+"'," +
										  				  "'"+gridPar.onnewheader+"'," +
										  				      gridPar.containscdata+");\n");
		result.append("</script>\n");
		
		if(isHiddenSubmit)
			result.append("</div>\n");

		template.setInGrid(false);
		
		return result.toString();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getGridCounters(){
		StringBuffer result = new StringBuffer(STRBUF_SIZE);
		String colorStyle = "";
		if(!gridPar.headercolor.equals(HEADER_COLOR))
			colorStyle="color:"+gridPar.headercolor+";";
		String bgcolor = "";
		if(!gridPar.headerbackground.equals(HEADER_BACKGROUND))
			bgcolor="bgcolor='"+gridPar.headerbackground+"'";
		result.append("<div style='width:"+gridPar.counterwidth+";overflow:hidden;'>\n");
		result.append(" <table width='100%' cellpadding='0' cellspacing='0' class='gridHeader' "+bgcolor+">\n");
		result.append("  <tr>\n");
		result.append("   <td align='center' id='upFixColumn' class='gridHeaderCell' " +
				              "style='"+colorStyle+"border-top: solid 1px darkgray;width:"+gridPar.counterwidth+";height:"+gridPar.headerheight+";'>"+gridPar.countertitle+"</td>\n");
		result.append("  </tr>\n");
		result.append(" </table>\n");
		result.append("</div>\n");
		
		colorStyle = "";
		if(!gridPar.countercolor.equals(HEADER_COLOR))
			colorStyle="color:"+gridPar.countercolor+";";
		bgcolor = "";
		if(!gridPar.counterbackground.equals(HEADER_BACKGROUND))
			bgcolor="background-color:"+gridPar.counterbackground+";";
		result.append("<div id='divBodyFixColumn' style='width:"+gridPar.counterwidth+";height:"+gridPar.height+";overflow-x:hidden;overflow-y:hidden;'>\n");
		result.append("<table width='100%' id='tabFixColumn' cellpadding='0' cellspacing='0' class='gridRow' "+bgcolor+">\n");
		result.append("<tbody>\n");
		for(ListIterator listIt=listValue.getPageRowsIterator();listIt.hasNext();){
			int absIndex = listValue.getAbsIndex(listIt.nextIndex());
			listIt.next();
			result.append(" <tr>\n");
			String counter = "&nbsp;";
			if(Boolean.valueOf(gridPar.showcounter).booleanValue())
				counter = ""+(absIndex+1);
			result.append("  <td align='center' id='bodyFixColumn"+absIndex+"' class='gridCounter' "+
		                         "style='"+colorStyle+bgcolor+"height:"+gridPar.cellheight+";'>"+counter+"</td>\n");
			result.append(" </tr>\n");
		}
		
		result.append("</tbody>\n");
		
		// Bottom empty counter row
		result.append("<tfoot>\n");
		
		if(!gridPar.gridwidth.equals("100%"))
			result.append("<tr><td style='background-color:"+gridPar.gridbackground+"; height:"+(HORZ_SCROLL_HEIGHT*2)+"px;'></td></tr>"); // For horizontal scollbar
		else
			result.append("<tr><td style='background-color:"+gridPar.gridbackground+"; height:"+HORZ_SCROLL_HEIGHT+"px;'></td></tr>");
	
		result.append(" <tr>\n");
		result.append("  <td style='background-color:"+gridPar.gridbackground+";height:"+gridPar.cellheight+";'>&nbsp;</td>\n");
		result.append(" </tr>\n");
		result.append("</tfoot>\n");
		
		result.append("</table>\n");
		result.append("</div>\n");

		return result.toString();
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getGridHeader() throws Exception{
		StringBuffer result = new StringBuffer(STRBUF_SIZE);
		
		String colorStyle = "";
		if(!gridPar.headercolor.equals(HEADER_COLOR))
			colorStyle="color:"+gridPar.headercolor+";";
		String bgcolor = "";
		if(!gridPar.headerbackground.equals(HEADER_BACKGROUND))
			bgcolor="bgcolor='"+gridPar.headerbackground+"'";
		
		result.append("<div id='headerDiv' style='width:100%;overflow-y:scroll;overflow-x:hidden;height:"+gridPar.headerheight+";"+
				            "scrollbar-shadow-color:"+gridPar.gridbackground+";scrollbar-highlight-color:"+gridPar.gridbackground+";"+
				            "scrollbar-face-color:"+gridPar.gridbackground+";scrollbar-3dlight-color:"+gridPar.gridbackground+";"+
				            "scrollbar-darkshadow-color:"+gridPar.gridbackground+";scrollbar-track-color:"+gridPar.gridbackground+";"+
				            "scrollbar-arrow-color:"+gridPar.gridbackground+";'>\n");
		result.append(" <div style='position:absolute;top:0px;left:0px;z-index:100;'>\n");
		
		result.append("  <table id='headerTab' cellpadding='0' cellspacing='0' class='gridHeader' "+bgcolor+
				              " style='width:"+gridPar.gridwidth+";table-layout:fixed;'>\n");

		result.append("   <col width='0' style='width:0'>\n");
		for(int i=0;i<colWidths.size();i++){
			String colw = (String)colWidths.get(i);
			result.append("   <col width='"+colw+"'>\n");
		}
		
		result.append("   <tr>\n");
		result.append("    <td style='"+colorStyle+"height:"+gridPar.headerheight+";' abstractType='IntegerType' propertyName='absIndex'></td>\n");
		CommandDataModel rowModel = (CommandDataModel)listValue.getModelType().newInstance();
		for(int i=0;i<visibleCols.size();i++){
			String propName = (String)visibleCols.get(i);
			Class abstractTypeClass = Tools.getPropertyType(rowModel,propName);
			String abstractType = "";
			if(abstractTypeClass != null){
				abstractType = abstractTypeClass.getName();
				abstractType = abstractType.substring(abstractType.lastIndexOf('.')+1);				
			}
			
			String title = (String)colTitles.get(i);
			if(title == null || title.equals(""))
				title = "&nbsp;";
			else
				title = Tools.convertSpecialChars(title);
			if(gridPar.containscdata)
				title = title.replaceAll("\\<", "&lt;").replaceAll("\\>", "&gt;");
			String nowrap = "";
			if(gridPar.headernowrap)
				nowrap = "nowrap";
			result.append("    <td "+nowrap+" align='center' abstractType='"+abstractType+"' propertyName='"+propName+"' title='"+title+"' " +
					               "class='gridHeaderCell' style='"+colorStyle+"border-top: solid 1px darkgray;'>"+title+"</td>\n");
		}
		result.append("   </tr>\n");
	    
		result.append("  </table>\n");
		result.append(" </div>\n");
		result.append("</div>\n");
		
		return result.toString();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getGridBody() throws Exception{
		StringBuffer result = new StringBuffer(BIG_STRBUF_SIZE);
		
		String xOverflow = "auto";
		String yOverflow = "scroll";
		if(!gridPar.gridwidth.equals("100%")){
			xOverflow = "scroll";
			yOverflow = "scroll";
		}

		result.append("<div id='tabDiv' onscroll='gridListManageOnScroll(this,\""+listPropName+"Grid\");' "+
						    "style='width:100%;height:"+gridPar.height+";overflow-y:"+yOverflow+";overflow-x:"+xOverflow+";'>\n"); //???
		result.append(" <div style='position:absolute;top:0px;left:0px;z-index:100;'>\n");
		result.append("  <table id='tab' cellpadding='0' cellspacing='0' style='width:"+gridPar.gridwidth+";table-layout:fixed;'>\n");
		
		result.append("   <col width='0' style='width:0'>\n");
		for(int i=0;i<colWidths.size();i++){
			String colw = (String)colWidths.get(i);
			result.append("<col width='"+colw+"'>\n");
		}

		result.append("<tbody>");
		int rowIndexInPage = 0;
		for(ListIterator listIt=listValue.getPageRowsIterator();listIt.hasNext();){
			int rowIndex = listValue.getAbsIndex(listIt.nextIndex());
			CommandDataModel rowModel = (CommandDataModel) listIt.next();
			result.append(getGridRow(rowIndex,rowIndexInPage,rowModel));
			rowIndexInPage++;
		}
		result.append("</tbody>");
		
		result.append("  </table>\n");
		result.append(" </div>\n");	
		result.append("</div>\n");	
		
		return result.toString();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getGridRow(int rowIndex, int rowIndexInPage, CommandDataModel rowModel) throws Exception{
		StringBuffer result = new StringBuffer(STRBUF_SIZE);
		String style = "";
		
		result.append("<tr rowIndex='"+rowIndexInPage+"' absIndex='"+rowIndex+"' class='gridRow' ");
		if(gridPar.selection.equalsIgnoreCase("multi") || 
		   gridPar.selection.equalsIgnoreCase("single"))
			style += "cursor:pointer;";
		
		if(gridPar.modality != Template.READ_MODALITY &&
			!gridPar.helper.equalsIgnoreCase("yes") && 
			Tools.containsTypeErrors(rowModel))
			style += "background-color:red;";

		if(!style.equals(""))
			result.append("style='"+style+"' ");
		
		for(int i=0;i<cols.size();i++){
			String propName = (String)cols.get(i);
			AbstractType propValue = (AbstractType)Tools.getPropertyValue(rowModel,propName);
			if(propValue == null)
				continue;
			result.append(propName+"=\""+Tools.stringToHTMLString(propValue.toString())+"\" ");
			if(rowModel.getCodDescDataList(propName) != null)
				result.append(propName+"_cod=\""+propValue.toString()+"\" ");
		}
		result.append(">\n");

		result.append("<td style='height:"+gridPar.cellheight+";'>"+rowIndex+"</td>\n");
		for(int i=0;i<visibleCols.size();i++){
			String propName = (String)visibleCols.get(i);
			String event = (String)visibleColsEvents.get(i);
			result.append(getGridCell(rowIndex,i,propName,rowModel,event));
		}
		result.append("</tr>\n");
		return result.toString();
	}	
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getGridCell(int rowIndex, int cellIndex, 
							   String propName, CommandDataModel rowModel, String event) throws Exception{
		
		String originalPropName = propName.toString();
		CommandDataModel originalRowModel = rowModel;
		
		int idx = propName.lastIndexOf(Constants.NESTED_INDICATOR);
		if(idx >= 0){
			rowModel = (CommandDataModel)Tools.getPropertyValue(rowModel,propName.substring(0,idx));
			propName = propName.substring(idx+1);			
		}

		String nowrap = "";
		if(gridPar.cellsnowrap)
			nowrap = "nowrap";
		
		String cellModality = gridPar.strFieldsModality;

		AbstractType propValue = (AbstractType)Tools.getPropertyValue(rowModel,propName);
		if(propValue == null){
			propValue = new StringType();
			propValue.setEditable(false);
		}
		
		if(gridDecorator != null){
			originalRowModel.setTemplate(template);
			gridDecorator.onNewCell(listPropName,originalPropName,originalRowModel,propValue,rowIndex,cellIndex);
			originalRowModel.setTemplate(null);
		}
		
		String editable = "false";
		String cellInnerHTML ="";
		String cellClassName = "outputGridCell";
		if(!propValue.isEditable())
			cellModality = "read";
		if(gridPar.modality != Template.READ_MODALITY && 
		   propValue.isEditable()){
			cellClassName = "inputGridCell";
			editable = "true";
			String savPrefix = template.getPrefix();
			template.setPrefix("");
			boolean savJSombo = template.getJSCombo();
			template.setJSCombo(false);
			int savModality = template.getModality();
			template.setModality(gridPar.fieldsModality);
			String extraParameters = "";
			if(propValue.getStyle() != null && propValue.getStyle().extraParameters != null)
				extraParameters = propValue.getStyle().extraParameters;
			String style = "";
			if(event.indexOf(" size=") < 0 && event.indexOf(" style=") < 0)
				style="style='width:100%;'";
			cellInnerHTML = template.field(listPropName+rowIndex+"_"+originalPropName,
										   extraParameters+" helper='"+gridPar.helper+"' "+style+" labelposition='nolabel' "+event);
			template.setPrefix(savPrefix);
			template.setJSCombo(savJSombo);
			template.setModality(savModality);
		}else{
			if(rowModel.getCodDescDataList(propName) != null){
				cellInnerHTML = rowModel.getDescValue(propName);
			}else{
				if(propValue instanceof BooleanType)
					cellInnerHTML = wfemTemplate.getProperty("BooleanType."+propValue);
				else if(propValue instanceof DoubleType)
					cellInnerHTML = ((DoubleType)propValue).toScaledString(template.getDoubleScale());
				else
					cellInnerHTML = propValue.toString();
			}
			
			cellInnerHTML = Tools.convertSpecialChars(cellInnerHTML);
			if(gridPar.containscdata)
				cellInnerHTML = cellInnerHTML.replaceAll("\\<", "&lt;").replaceAll("\\>", "&gt;");
		}
		if(cellInnerHTML.equals(""))
			cellInnerHTML = "&nbsp;";
		
		StringBuffer result = new StringBuffer();
		result.append("<td modality='"+cellModality+"' "+nowrap+" class='"+cellClassName+"' editable='"+editable+"' ");
		if(propValue.getStyle() != null){
			if(propValue.getStyle().innerHTML != null)
				cellInnerHTML = propValue.getStyle().innerHTML;
			String style = "";
			if(propValue.getStyle().color != null)
				style += "color:"+HtmlColorMapping.getColor(propValue.getStyle().color)+";";
			if(propValue.getStyle().backgroundColor != null)
				style += "background-color:"+HtmlColorMapping.getColor(propValue.getStyle().backgroundColor)+";";
			if(propValue.getStyle().textAlign != null)
				style += "text-align:"+propValue.getStyle().textAlign+";";
			if(style.length() > 0)
				result.append("style='"+style+"'");
		}
		result.append(">"+cellInnerHTML+"</td>\n"); 
		return result.toString();
	}	
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getPagination(){

		if(listValue.getTotalPages() == 1 ||
		   listValue.getRowsPerPage() == -1)
			return null;
		
		String style = " style='background-color:"+gridPar.gridbackground+";' ";
		String options = "";
		StringBuffer result = new StringBuffer(STRBUF_SIZE);
		
		result.append("<table class='text' cellspacing='0' cellpadding='0'>\n");
		result.append("<tr>\n");

		/***** firstPage *****/
		if(listValue.getCurrentPage() == 1)
			options = "class='disabledAction'";
		else
			options = "class='action' onclick='firstPage(\""+gridPar.paginationformname+"\",\""+listPropName+"\");'";
		result.append("<td "+style+" "+options+" title=\""+wfemTemplate.getProperty("pageSelector.firstPage")+"\">\n");
		result.append(FILLED_LT + FILLED_LT);
		result.append("</td>\n");
		result.append("<td>&nbsp;&nbsp;</td>\n");

		/***** previuosPage *****/
		if(listValue.getCurrentPage() == 1)
			options = "class='disabledAction'";
		else
			options = "class='action' onclick='previousPage(\""+gridPar.paginationformname+"\",\""+listPropName+"\");'";
		result.append("<td "+style+" "+options+" title=\""+wfemTemplate.getProperty("pageSelector.previousPage")+"\">\n");
		result.append(FILLED_LT);
		result.append("</td>\n");
		result.append("<td>&nbsp;&nbsp;</td>\n");

		/***** pages *****/
		for(int i=1;i<=listValue.getTotalPages();i++){

			if(i == listValue.getCurrentPage())
				options = "class='disabledAction'";
			else
				options = "class='action' onclick='gotoPage(\""+gridPar.paginationformname+"\",\""+i+"\",\""+listPropName+"\");'";
			result.append("<td "+style+" "+options+" title=\""+wfemTemplate.getProperty("pageSelector.gotoPage")+(" "+i+" ")+"\">\n");
			result.append(""+i);
			result.append("</td>\n");
			if (i == listValue.getTotalPages())
				result.append("<td>&nbsp;&nbsp;</td>\n");
			else
				result.append("<td>&nbsp;</td>\n");
		}

		/***** nextPage *****/
		if(listValue.getCurrentPage() == listValue.getTotalPages())
			options = "class='disabledAction'";
		else
			options = "class='action' onclick='nextPage(\""+gridPar.paginationformname+"\",\""+listPropName+"\");'";
		result.append("<td "+style+" "+options+" title=\""+wfemTemplate.getProperty("pageSelector.nextPage")+"\">\n");
		result.append(FILLED_GT);
		result.append("</td>\n");
		result.append("<td>&nbsp;&nbsp;</td>\n");

		/***** lastPage *****/
		if(listValue.getCurrentPage() == listValue.getTotalPages())
			options = "class='disabledAction'";
		else
			options = "class='action' onclick='lastPage(\""+gridPar.paginationformname+"\",\""+listPropName+"\");'";
		result.append("<td "+style+" "+options+" title=\""+wfemTemplate.getProperty("pageSelector.lastPage")+"\">\n");
		result.append(FILLED_GT + FILLED_GT);
		result.append("</td>\n");
		result.append("<td>&nbsp;&nbsp;</td>\n");

		result.append("</tr>\n");
		result.append("</table>\n");
		
		return result.toString();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getPaginationForm(){
		StringBuffer result = new StringBuffer();
		
		result.append("<form style='margin: 0px;' name='"+gridPar.paginationformname+"' method='post' action='"+Template.CONTROLLER_CALL+"'>\n");
		result.append("<input type='hidden' name='"+Template.CONTROLLER_CMD+"' value=''>\n");
		result.append("<input type='hidden' name='index' value=''>\n");
		result.append("<input type='hidden' name='listPropertyName' value='"+listPropName+"'>\n");
		result.append("<input type='hidden' name='BrowserInstance' value='"+template.getBrowserInstance()+"'>\n");
		
		return result.toString();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getFooterMessage(){
		StringBuffer result = new StringBuffer();
		
		result.append("<table width='100%' height='100%' class='text' cellspacing='0' cellpadding='0'>\n");
		result.append("<tr>\n");
		
		result.append("<td width='15%'>\n");
		if(listValue.getTotalPages() > 1)
			result.append("&nbsp;"+wfemTemplate.getProperty("gridMsg.pageNumber",Integer.toString(listValue.getCurrentPage())));
		else
			result.append("&nbsp;");
		result.append("</td>\n");
		
		result.append("<td align='center'>\n");
		if(listValue.isMaxRowsExceeded())
			result.append(wfemTemplate.getProperty("gridMsg.maxRowsExceeded",Integer.toString(listValue.size())));
		else
			result.append(wfemTemplate.getProperty("gridMsg.numRows",Integer.toString(listValue.size())));
		result.append("</td>\n");
		result.append("<td width='10%'>&nbsp;</td>\n");
		
		result.append("</tr>\n");
		result.append("</table>\n");
		
		return result.toString();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getNum(String par){
		StringBuffer result = new StringBuffer();
		char[] c = par.toCharArray();
		for(int i=0;i<c.length;i++){
			if(c[i] < '0' || c[i] > '9')
				break;
			result.append(String.valueOf(c[i]));
		}
		return result.toString();
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getMeasure(String par){
		char[] c = par.toCharArray();
		int i=0;
		for(;i<c.length;i++){
			if(c[i] < '0' || c[i] > '9')
				break;
		}
		String measure = par.substring(i);
		if(measure.length() != 2)
			return "px";
		return measure;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getCalc() throws Exception{
		return getPdfCalc(false,false);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getPdf() throws Exception{
		return getPdfCalc(true,false);		
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getPdfCalc(boolean isPdf, boolean innerGrid) throws Exception{
		StringBuffer result = new StringBuffer(BIG_STRBUF_SIZE);
		
		String title = gridPar.title.toString();
		title = title.replaceAll("\\<br\\>",PageLayoutSyntaxConstants.HEADER_FOOTER_TEXT_CR);
		title = Tools.htmlToString(title);
		
		int colspan = 0;
		for(int i=0;i<printableCols.size();i++){
			if(printableCols.get(i) == null)
				continue;
			colspan++;
		}
		
		if(!innerGrid){
			result.append("<html>");
			result.append("<!-- PAGEORIENTATION=\"Horizontal\" -->\n");
			result.append("<!-- PAGEFONTSIZE=\"1\" -->\n");
			result.append("<!-- MARGINTOP=\"0.6\" -->\n");
			result.append("<!-- MARGINLEFT=\"0.15\" -->\n");
			result.append("<!-- MARGINRIGHT=\"0.15\" -->\n");
			result.append("<!-- PAGEFONTFACE=\"Times New Roman\" -->\n");
			result.append("<!-- HEADERLEFT=\""+title+"\" -->\n");
			result.append("<!-- HEADERFONTSIZE=\"3\" -->\n");
			result.append("<!-- FOOTERCENTER=\"Pagina #CURPAGE# di #TOTPAGE#\" -->\n");
			if(!isPdf)
				result.append("<title>"+title+"</title>");			
			result.append("<table><!-- headerrows=\"1\" -->");
		}else{
			if(!gridPar.title.toString().equals("")){
				result.append("<table><!-- headerrows=\"4\" -->");
				result.append("<tr>");
				for(int i=0;i<printableCols.size();i++){
					if(printableCols.get(i) == null)
						continue;
					result.append("<td ");
					String colw = (String)colWidths.get(i);
					if(!colw.equals("*"))
						result.append("width=\""+colw+"\">");
					result.append("</td>\n");
				}
				result.append("</tr>");
				result.append("<tr><td colspan=\""+colspan+"\"><font size=\"4\"><b>"+
								gridPar.title.toString()+
								"</b></font></td></tr>");
				result.append("<tr><td colspan=\""+colspan+"\">&nbsp;</td></tr>");
			}else{
				result.append("<table><!-- headerrows=\"1\" -->");				
			}
		}
		
		result.append("<tr>\n");

		// Header
		String hb = gridPar.headerbackground;
		if(!isPdf && hb.equals("#D3D3D3"))
			hb = "#C0C0C0";
		
		for(int i=0;i<printableCols.size();i++){
			if(printableCols.get(i) == null)
				continue;
			title = (String)colTitles.get(i);
			if(title == null || title.equals(""))
				title = "&nbsp;";
			else
				title = Tools.convertSpecialChars(title);
			if(gridPar.containscdata)
				title = title.replaceAll("\\<", "&lt;").replaceAll("\\>", "&gt;");
			result.append("<td border=\"1\" align=\"center\" ");
			String colw = (String)colWidths.get(i);
			if(!colw.equals("*"))
				result.append("width=\""+colw+"\" ");
			result.append("bgcolor=\""+hb+"\"><font color=\""+gridPar.headercolor+"\">");
			result.append("<b>"+title+"</b>");
			result.append("</font></td>\n");
		}
		result.append("</tr>\n");
		
		// Body rows
		for(int i=0;i<listValue.size();i++){
			CommandDataModel rowModel = (CommandDataModel) listValue.get(i);
			result.append(getPdfCalcGridRow(i,rowModel));
		}
		
		if(gridPar.footerhtml.length() > 0){
			result.append("<tr>\n");
			result.append("<td colspan='"+colspan+"'>\n");
			result.append(gridPar.footerhtml);
			result.append("</td>\n");
			result.append("</tr>\n");
		}
		
		result.append("</table>\n");
		
		if(!innerGrid)
			result.append("</html>");
		
		return result.toString();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getPdfCalcGridRow(int rowIndex, CommandDataModel rowModel) throws Exception{
		StringBuffer result = new StringBuffer(STRBUF_SIZE);
		
		result.append("<tr>\n");

		for(int i=0;i<printableCols.size();i++){
			if(printableCols.get(i) == null)
				continue;
			String propName = (String)printableCols.get(i);
			result.append(getPdfCalcGridCell(rowIndex,i,propName,rowModel));
		}
		result.append("</tr>\n");
		return result.toString();
	}	
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getPdfCalcGridCell(int rowIndex, int cellIndex, 
									   String propName, CommandDataModel rowModel) throws Exception{
		
		String originalPropName = propName.toString();
		CommandDataModel originalRowModel = rowModel;
		
		int idx = propName.lastIndexOf(Constants.NESTED_INDICATOR);
		if(idx >= 0){
			rowModel = (CommandDataModel)Tools.getPropertyValue(rowModel,propName.substring(0,idx));
			propName = propName.substring(idx+1);			
		}
		
		AbstractType propValue = (AbstractType)Tools.getPropertyValue(rowModel,propName);
		if(propValue == null)
			propValue = new StringType();
		
		if(gridDecorator != null){
			originalRowModel.setTemplate(template);
			gridDecorator.onNewCell(listPropName,originalPropName,originalRowModel,propValue,rowIndex,cellIndex);
			originalRowModel.setTemplate(null);
		}
		
		String cellInnerHTML = "";
		if(rowModel.getCodDescDataList(propName) != null){
			cellInnerHTML = rowModel.getDescValue(propName);
			cellInnerHTML = Tools.convertSpecialChars(cellInnerHTML);
			if(gridPar.containscdata)
				cellInnerHTML = cellInnerHTML.replaceAll("\\<", "&lt;").replaceAll("\\>", "&gt;");
		}else{
			if(propValue instanceof BooleanType){
				cellInnerHTML = wfemTemplate.getProperty("BooleanType."+propValue);
			}else if(propValue instanceof DoubleType){
				cellInnerHTML = ((DoubleType)propValue).toScaledString(template.getDoubleScale());
			}else{
				cellInnerHTML = Tools.stringToHTMLString(propValue.toString());
				cellInnerHTML = Tools.convertSpecialChars(cellInnerHTML);
			}
		}

		if(cellInnerHTML.equals(""))
			cellInnerHTML = "&nbsp;";

		StringBuffer result = new StringBuffer();
		result.append("<td border=\"1\" ");
		String colw = (String)colWidths.get(cellIndex);
		if(!colw.equals("*"))
			result.append("width=\""+colw+"\" ");
		
		if(propValue.getStyle() != null){
			if(propValue.getStyle().innerHTML != null)
				cellInnerHTML = propValue.getStyle().innerHTML; 
			if(propValue.getStyle().backgroundColor != null)
				result.append("bgcolor=\""+HtmlColorMapping.getColor(propValue.getStyle().backgroundColor)+"\" ");
			if(propValue.getStyle().textAlign != null)
				result.append("align=\""+propValue.getStyle().textAlign+"\" ");
			if(propValue.getStyle().color != null)
				cellInnerHTML = "<font color=\""+HtmlColorMapping.getColor(propValue.getStyle().color)+"\">"+cellInnerHTML+"</font>";
		}
		result.append(">"+cellInnerHTML+"</td>\n");
		return result.toString();
	}
}
