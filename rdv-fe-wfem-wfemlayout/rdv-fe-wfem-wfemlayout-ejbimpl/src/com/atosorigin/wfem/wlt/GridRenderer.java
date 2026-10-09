package com.atosorigin.wfem.wlt;

import java.util.ListIterator;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.atosorigin.wfem.command.CommandDataModel;
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

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public class GridRenderer extends FieldRenderer{
	
	private static final String FILLED_LT = "&#9668;";
	private static final String FILLED_GT = "&#9658;";
	
	private static final String PRINT_HEADER_COLOR = "#1A458F";
	private static final String PRINT_HEADER_BACKGROUND = "lightgrey";
	private static final String PRINT_HEADER_CALC_BACKGROUND = "#C0C0C0";

	private PageRenderer 	pageRenderer;
	
	private String 			listPropName;
	private Template 		template;
	private Template 		ffTemplate;
	private ListType 		listValue;
	private Vector			cols;
	private Vector 			colWidths;
	private Vector	 		visibleCols = new Vector();
	private Vector	 		printableCols = new Vector();
	private Vector	 		hiddenCols = new Vector();
	private Vector	 		visibleColsEvents = new Vector();
	private Vector 			colTitles = new Vector();
	private String			modelPropertyNames = "";
	private GridPar			params;
	private	GridDecorator 	gridDecorator;
	
    /**************************************************************************************************/
	/**************************************************************************************************/
	static class Patterns{
		
		static final Pattern cols            	 = PageRenderer.compilePattern("cols");
		static final Pattern colswidths      	 = PageRenderer.compilePattern("colswidths");
		
		static final Pattern simplelayout      	 = PageRenderer.compilePattern("simplelayout");
		static final Pattern modality         	 = PageRenderer.compilePattern("modality");
		static final Pattern fieldsmodality   	 = PageRenderer.compilePattern("fieldsmodality");
		static final Pattern pageformname     	 = PageRenderer.compilePattern("pageformname");
		static final Pattern title            	 = PageRenderer.compilePattern("title");
		static final Pattern onclick          	 = PageRenderer.compilePattern("onclick");
		static final Pattern ondblclick       	 = PageRenderer.compilePattern("ondblclick");
		static final Pattern onnewrow         	 = PageRenderer.compilePattern("onnewrow");
		static final Pattern onnewcell        	 = PageRenderer.compilePattern("onnewcell");
		static final Pattern onnewheader     	 = PageRenderer.compilePattern("onnewheader");
		static final Pattern counterwidth     	 = PageRenderer.compilePattern("counterwidth");
		static final Pattern gridwidth        	 = PageRenderer.compilePattern("gridwidth");
		static final Pattern cellheight       	 = PageRenderer.compilePattern("cellheight");
		static final Pattern headerheight     	 = PageRenderer.compilePattern("headerheight");
		static final Pattern topareaheight    	 = PageRenderer.compilePattern("topareaheight");
		static final Pattern bottomareaheight 	 = PageRenderer.compilePattern("bottomareaheight");
		static final Pattern norowsmsg        	 = PageRenderer.compilePattern("norowsmsg");
		static final Pattern selection        	 = PageRenderer.compilePattern("selection");
		static final Pattern sortable         	 = PageRenderer.compilePattern("sortable");
		static final Pattern showcounter      	 = PageRenderer.compilePattern("showcounter");
		static final Pattern countertitle     	 = PageRenderer.compilePattern("countertitle");
		static final Pattern headernowrap     	 = PageRenderer.compilePattern("headernowrap");
		static final Pattern cellsnowrap      	 = PageRenderer.compilePattern("cellsnowrap");
		static final Pattern showtoparea      	 = PageRenderer.compilePattern("showtoparea");
		static final Pattern showbottomarea   	 = PageRenderer.compilePattern("showbottomarea");
		static final Pattern helper           	 = PageRenderer.compilePattern("helper");
		static final Pattern decorator        	 = PageRenderer.compilePattern("decorator");
		static final Pattern pdf              	 = PageRenderer.compilePattern("pdf");
		static final Pattern excel            	 = PageRenderer.compilePattern("excel");
		static final Pattern width            	 = PageRenderer.compilePattern("width");
		static final Pattern height           	 = PageRenderer.compilePattern("height");
		static final Pattern footerhtml          = PageRenderer.compilePattern("footerhtml");
		static final Pattern containscdata       = PageRenderer.compilePattern("containscdata");
		static final Pattern textalign       	 = PageRenderer.compilePattern("textalign");
		static final Pattern generatecellid      = PageRenderer.compilePattern("generatecellid");
		static final Pattern showgridborder      = PageRenderer.compilePattern("showgridborder");
	}
	
    /**************************************************************************************************/
	/**************************************************************************************************/
	class GridPar{
		
		boolean doForm = false;
		
		String strModality;
		String strFieldsModality;
		int	   modality;
		int	   fieldsModality;
		
		boolean simplelayout;
		String 	paginationformname;
		String 	title;
		String 	onclick;
		String 	ondblclick;
		String 	onnewrow;
		String 	onnewcell;
		String 	onnewheader;
		String 	counterwidth;
		String 	gridwidth;
		String 	cellheight;
		String 	headerheight;
		String 	topareaheight;
		String 	bottomareaheight;
		String 	norowsmsg;
		String 	selection;
		String	sortable;
		boolean	showcounter;
		String 	countertitle;
		boolean headernowrap;
		boolean cellsnowrap;
		boolean showtoparea;
		boolean showbottomarea;
		String 	helper;
		String 	decorator;
		String 	pdf;
		String 	excel;
		String 	width;
		String 	height;
		String 	footerhtml;
		boolean containscdata;
		String	textalign;
		boolean generatecellid;
		
	    /**************************************************************************************************/
		/**************************************************************************************************/
		public GridPar(){
			
			simplelayout = pageRenderer.getPar(Patterns.simplelayout,"true").asBoolean();
			
			strModality = pageRenderer.getPar(Patterns.modality,"read").asString();
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
				strFieldsModality = pageRenderer.getPar(Patterns.fieldsmodality,strModality).asString();
				if(strFieldsModality.equalsIgnoreCase("insert"))
					fieldsModality = Template.INSERT_MODALITY;
				else if(strFieldsModality.equalsIgnoreCase("update"))
					fieldsModality = Template.UPDATE_MODALITY;
				else if(strFieldsModality.equalsIgnoreCase("read"))
					fieldsModality = Template.READ_MODALITY;
			}

			paginationformname = pageRenderer.getPar(Patterns.pageformname,"").asString();
			if((listValue.getTotalPages() > 1 && listValue.getRowsPerPage() != -1) && paginationformname.equals("")){
				doForm = true;
				paginationformname = listPropName+"PaginationForm";
			}
				
			title = pageRenderer.getPar(Patterns.title,"").asString();
			footerhtml = pageRenderer.getPar(Patterns.footerhtml,"").asString();
			
			onclick = pageRenderer.getPar(Patterns.onclick,"").asString();
			ondblclick = pageRenderer.getPar(Patterns.ondblclick,"").asString();
			onnewrow = pageRenderer.getPar(Patterns.onnewrow,"").asString();
			onnewcell = pageRenderer.getPar(Patterns.onnewcell,"").asString();
			onnewheader = pageRenderer.getPar(Patterns.onnewheader,"").asString();
			counterwidth = pageRenderer.getPar(Patterns.counterwidth,"28").asString();
			gridwidth = pageRenderer.getPar(Patterns.gridwidth,"").asString();
			cellheight = pageRenderer.getPar(Patterns.cellheight,"").asString();
			headerheight = pageRenderer.getPar(Patterns.headerheight,"34px").asString();
			topareaheight = pageRenderer.getPar(Patterns.topareaheight,"25px").asString();
			bottomareaheight = pageRenderer.getPar(Patterns.bottomareaheight,"20px").asString();
			norowsmsg = pageRenderer.getPar(Patterns.norowsmsg,pageRenderer.getFfTemplate().getProperty("gridMsg.noRows")).asString();
			selection = pageRenderer.getPar(Patterns.selection,"single").asString();
			sortable = pageRenderer.getPar(Patterns.sortable,"true").asString();
			showcounter = pageRenderer.getPar(Patterns.showcounter,"true").asBoolean();
			countertitle = pageRenderer.getPar(Patterns.countertitle,"&nbsp;").asString();
			headernowrap = pageRenderer.getPar(Patterns.headernowrap,"true").asBoolean();
			cellsnowrap = pageRenderer.getPar(Patterns.cellsnowrap,"true").asBoolean();
			showtoparea = pageRenderer.getPar(Patterns.showtoparea,"true").asBoolean();
			showbottomarea = pageRenderer.getPar(Patterns.showbottomarea,"true").asBoolean();
			helper = pageRenderer.getPar(Patterns.helper,"no").asString();
			decorator = pageRenderer.getPar(Patterns.decorator,"").asString();
			pdf = pageRenderer.getPar(Patterns.pdf,"false").asString();
			excel = pageRenderer.getPar(Patterns.excel,"false").asString();
			width = pageRenderer.getPar(Patterns.width,"").asString();
			height = pageRenderer.getPar(Patterns.height,"").asString();
			containscdata = pageRenderer.getPar(Patterns.containscdata,"false").asBoolean();
			textalign = pageRenderer.getPar(Patterns.textalign,"left").asString();
			generatecellid = pageRenderer.getPar(Patterns.generatecellid,"false").asBoolean();

			if(width.equals(""))
				width = "100%";
			
			if(height.equals(""))
				height = "100%";
			
			if(gridwidth.equals(""))
				gridwidth = "100%";
			else
				gridwidth = PageRenderer.getNum(gridwidth)+PageRenderer.getMeasure(gridwidth);

			try{
				if(Integer.parseInt(PageRenderer.getNum(counterwidth)) == 0)
					showcounter = false;
			}catch (Exception e) {
				showcounter = false;
			}
			
			if(showcounter && cellheight.length() == 0)
				cellheight = "25px";
			
		}
		
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public GridRenderer(PageRenderer pageRenderer) throws Exception{
		
		this.pageRenderer = pageRenderer;
		this.listPropName = pageRenderer.getPropName();
		this.template = pageRenderer.getTemplate();
		this.ffTemplate = pageRenderer.getFfTemplate();
		
		try{			
			Class propType = Tools.getPropertyType(template.getPageDataModel(),this.listPropName);
			if(propType == null)
				throw new Exception("<span>Field type for property ["+this.listPropName+"] is null</span>");
			this.listValue = (ListType)Tools.getPropertyValue(template.getPageDataModel(),this.listPropName);
			if(this.listValue == null)
				throw new Exception("<span>Field ["+this.listPropName+"] is null</span>");
		}catch(ClassCastException cce){
			throw new Exception("<span>Field ["+this.listPropName+"] is not an ListType</span>");
		}
		this.listValue.recalc();
		
		Vector parCols = new Vector();
		StringTokenizer st = new StringTokenizer(pageRenderer.getPar(Patterns.cols,"").asString(),",");
		while(st.hasMoreTokens())
			parCols.add(st.nextToken());
		
		Vector parColsWidths = new Vector();
		st = new StringTokenizer(pageRenderer.getPar(Patterns.colswidths,"").asString(),",");
		while(st.hasMoreTokens())
			parColsWidths.add(st.nextToken());
		
		boolean columnsFromDb = false;
		// Colums non specified by developer. Use the db columns
		if(parCols == null || parCols.size() == 0){
			parCols = new Vector();
			String[] colProps = listValue.getPropertyNames();
			for(int i=0;i<colProps.length;i++)
				parCols.add(colProps[i]);
			columnsFromDb = true;
		}
		
		this.cols = parCols;
		this.colWidths = parColsWidths;
		
		// Load visible, printable and hidden columns
		for(int i=0;i<cols.size();i++){
			String col = (String)cols.get(i);
			if(col.startsWith("#")){
				col = col.substring(1);
				this.hiddenCols.add(col);
				this.cols.set(i,col);
			}else if(col.startsWith("@")){
				col = col.substring(1);
				this.visibleCols.add(col);
				this.printableCols.add(null);
				this.cols.set(i,col);
			}else{
				this.visibleCols.add(col);
				this.printableCols.add(col);
			}

		}

		// Load columns titles
		if(columnsFromDb){
			String[] coltit = listValue.getColumnLabels();
			for(int i=0;i<coltit.length;i++)
				this.colTitles.add(coltit[i]);
		}else{
			for(int i=0;i<visibleCols.size();i++){
				if(template.getLabelCodePrefix() != null && !template.getLabelCodePrefix().equals(""))
					this.colTitles.add(template.getProperty(template.getLabelCodePrefix()+visibleCols.get(i)));
				else
					this.colTitles.add(template.getProperty(template.getPageName()+listPropName+"."+visibleCols.get(i)));
			}
		}
		
		// Manage columns widths
		if(this.colWidths == null || this.colWidths.size() == 0){
			this.colWidths = new Vector();
			for(int i=0;i<this.visibleCols.size();i++)
				this.colWidths.add("*");
		}

		// Load (and remove) columns events
		for(int i=0;i<visibleCols.size();i++){
			String parName = (String)visibleCols.get(i);
			String event = "";
			try{
				String curPars = pageRenderer.getPars();
				Matcher mat = Pattern.compile("\\s*"+parName+"\\s*\\=\\s*\\{([^\\}]+)\\}").matcher(curPars);
				if(mat.find()){
					event = mat.group(1);
					pageRenderer.setPars(curPars.substring(0,mat.start())+curPars.substring(mat.end()));
				}
			}catch(Exception e){ e.printStackTrace(); }
			visibleColsEvents.add(event);
		}
		
		// Load (and remove) all other grid parameters
		params = new GridPar();
		
		// Load grid decorator
		if(!params.decorator.equals("")){
			try{
				gridDecorator = (GridDecorator)Class.forName(params.decorator).newInstance();
			}catch(Exception e){gridDecorator=null; e.printStackTrace();}
		}
		
		// Load model property names to generate javascript row members
		if(listValue.size() > 0){
			StringBuffer mpn = new StringBuffer();
			for(int i=0;i<cols.size();i++){
				String propName = (String)cols.get(i);
				mpn.append(propName+"|");
			}
			if(mpn.length() > 0)
				modelPropertyNames = mpn.toString().substring(0,mpn.length()-1);
		}
		
		listValue.setFieldRenderer(this);
		
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getHtml() throws Exception {
		
		if(params.modality == Template.PRINT_MODALITY){
			return getPdfCalc(true,true);
		}
		
		StringBuffer result = new StringBuffer();

		if(listValue.size() == 0){
			result.append("<div id='"+listPropName+"Grid' style='width:"+params.width+";height:"+params.height+";'>");
			result.append("<table class='text gridNoRows' width='"+params.width+"' height='"+params.height+"'><tr><td align='center'>"+params.norowsmsg+"</td></tr></table>");
			result.append("</div>");
			return result.toString();
		}
		
		template.setInGrid(true);
		
		String gridContainerName="GridContainerName";
		if(template.isFeatureIncluded(Template.FEATURE_HIDDEN_SUBMIT))
			gridContainerName="GridHiddenSubmitContainer";

		String gridContainerStyle = "";
    	boolean showgridborder = pageRenderer.getPar(Patterns.showgridborder,"true").asBoolean();
		if(showgridborder)
			gridContainerStyle = "border: solid 1px darkgray;-moz-border-radius:5px;-webkit-border-radius:5px;border-radius:5px;padding:2;";
		
		result.append("<div class='text gridPanel' id='"+listPropName+gridContainerName+"' style='"+gridContainerStyle+"position:relative;width:"+params.width+";height:"+params.height+";'>\n");
		
		/* *************  Pagination Form (start) ************************** */
		if(params.doForm)
			result.append(getPaginationForm());
		
		String colspan = "";
		result.append("<table id='"+listPropName+"Grid' cellpadding='0' cellspacing='0' width='100%' height='100%' "+
							 "class='text' style='table-layout:fixed;'>\n");
		if(params.showcounter){
			colspan = "colspan='2'";
			result.append("<col width='"+params.counterwidth+"'>\n");
		}
		result.append("<col width='100%'>\n");
		
		/* *************  Top Area   ************************** */
		if(params.showtoparea){
			result.append("<tr>\n");
			result.append("<td "+colspan+" style='height:"+params.topareaheight+";padding:3;'>\n");
			result.append(getTopArea());
			result.append("</td>\n");
			result.append("</tr>\n");
		}
		/* ************************************************ */
		
		/* *************  Grid  ************************* */
		result.append("<tr>\n");
		if(params.showcounter){
			result.append("<td valign='top'>\n");
			result.append(   "<table height='100%' width='100%' cellpadding='0' cellspacing='0'><tr><td>\n");
			result.append(		getGridCountersHeader());
			result.append(   "</td></tr>\n");
			result.append(   "<tr><td height='100%'>\n");
			result.append(		getGridCounters());
			result.append(   "</td></tr></table>\n");		
			result.append("</td>\n");
		}
		result.append("<td valign='top'>\n");
		result.append(   "<table height='100%' width='100%' cellpadding='0' cellspacing='0'><tr><td>\n");
		result.append(		getGridHeader());
		result.append(   "</td></tr>\n");
		result.append(   "<tr><td height='100%'>\n");
		result.append(		getGridBody());
		result.append(   "</td></tr></table>\n");		
		result.append("</td>\n");
		result.append("</tr>\n");
		/* ************************************************ */
		
		/* *************  Footer   ************************** */
		if(params.showbottomarea || params.footerhtml.length() > 0){
			result.append("<tr>\n");
			result.append("<td "+colspan+" valign='top' style='height:"+params.bottomareaheight+";'>\n");
			if(params.footerhtml.length() == 0)
				result.append(getFooterMessage());
			else
				result.append(params.footerhtml);
			result.append("</td>\n");
			result.append("</tr>\n");
		}
		/* ************************************************ */
		
		result.append("</table>\n");		
		if(params.doForm)
			result.append("</form>\n");			

		result.append("<script>\n");
		result.append("document."+listPropName+"Grid = new GridList('"+listPropName+"'," +
			  														"'"+modelPropertyNames+"'," +
										  				  			"'"+params.selection+"'," +
										  				  			"'"+params.sortable+"'," +
										  				  			"'"+params.onclick+"'," +
										  				  			"'"+params.ondblclick+"'," +
										  				  			"'"+params.onnewrow+"'," +
										  				  			"'"+params.onnewcell+"'," +
										  				  			"'"+params.strModality+"'," +
										  				  			"'"+params.strFieldsModality+"'," +
										  				  			"'"+params.onnewheader+"'," +
										  				  			    params.containscdata+"" +
										  				  			");\n");
		result.append("</script>\n");
		
		result.append("</div>\n");
		
		template.setInGrid(false);
		
		return result.toString();
		
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getTopArea(){
		StringBuffer result = new StringBuffer();
		
		String paginationTable = getPagination();
		String title = params.title.replaceAll("\\<br\\>"," ");
		String pdfExcel = "";
		if(Boolean.valueOf(params.pdf).booleanValue()){
			pdfExcel += "<img src='"+template.getWfemLayoutWebApp()+"/images/pdf.gif' style='cursor:pointer;' title='"+ffTemplate.getProperty("gridMsg.openPdf")+"' "+
		    				"onclick='__gridIntf.openGridPdf(this,\""+title+"\",\""+params.paginationformname+"\",\""+listPropName+"\",\""+template.getBrowserInstance()+"\");'>";
		}
		if(Boolean.valueOf(params.excel).booleanValue()){
			pdfExcel += "<img src='"+template.getWfemLayoutWebApp()+"/images/calc.gif' style='cursor:pointer;' title='"+ffTemplate.getProperty("gridMsg.openCalc")+"' "+
							"onclick='__gridIntf.openGridCalc(this,\""+title+"\",\""+params.paginationformname+"\",\""+listPropName+"\",\""+template.getBrowserInstance()+"\");'>";
		}
		if(pdfExcel.length() > 0)
			pdfExcel = "<span style='white-space:nowrap;'>"+pdfExcel+"</span>";
		
		result.append("<table class='text gridTopArea' cellpadding='0' cellspacing='0' width='100%' style='table-layout:fixed;'>\n");
		result.append("<col width='*'>\n");
		if(paginationTable.length() > 0 && pdfExcel.length() > 0){
			result.append(  "<col width='*'>\n");
			result.append(  "<col width='*'>\n");
		}else if(paginationTable.length() > 0){
			result.append(  "<col width='*'>\n");
			result.append(  "<col width='*'>\n");
		}else if(pdfExcel.length() > 0){
			result.append(  "<col width='50'>\n");
		}
			
		result.append("<tr>\n");
		result.append(  "<td style='font-weight:bold;padding-left:3;'>"+title+"</td>\n");
		if(paginationTable.length() > 0)
			result.append(  "<td align='center'>"+paginationTable+"</td>\n");
		if(pdfExcel.length() > 0)
			result.append(  "<td align='right' style='padding-right:3;'>"+pdfExcel+"</td>\n");
		result.append("</tr>\n");
		result.append("</table>\n");
		
		return result.toString();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getGridCountersHeader(){
		StringBuffer result = new StringBuffer();
		
		result.append("<div style='position:relative;width:100%;overflow:hidden;margin:0;'>\n");
		result.append(" <table width='100%' cellpadding='0' cellspacing='0' class='text gridHeader'>\n");
		result.append("  <tr>\n");
		result.append("   <td align='center' id='"+listPropName+"GridUpFixColumn' " +
				              "style='border-top: solid 1px darkgray;border-left: solid 1px darkgray;width:"+params.counterwidth+";height:"+params.headerheight+";'>"+params.countertitle+"</td>\n");
		result.append("  </tr>\n");
		result.append(" </table>\n");
		result.append("</div>\n");
		
		return result.toString();
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getGridCounters(){

		String xOverflow = "hidden";
		if(!params.gridwidth.equals("100%"))
			xOverflow = "scroll";

		StringBuffer result = new StringBuffer();
		
		result.append("<div id='"+listPropName+"GridDivBodyFixColumn' style='position:relative;width:100%;height:100%;overflow-y:hidden;overflow-x:"+xOverflow+";'>\n");
		result.append("<table width='100%' id='"+listPropName+"GridTabFixColumn' cellpadding='0' cellspacing='0' class='text gridCounter' "+
							 "style='position:absolute;top:0px;left:0px;'>\n");
		
		for(ListIterator listIt=listValue.getPageRowsIterator();listIt.hasNext();){
			int absIndex = listValue.getAbsIndex(listIt.nextIndex());
			listIt.next();
			result.append(" <tr>\n");
			result.append("  <td align='center' absIndex='"+absIndex+"' style='border-left: solid 1px darkgray;height:"+params.cellheight+";'>"+(absIndex+1)+"</td>\n");
			result.append(" </tr>\n");
		}
		
		result.append("</table>\n");
		result.append("</div>\n");

		return result.toString();
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getGridHeader() throws Exception{
		
		StringBuffer result = new StringBuffer();
		
		String nowrapie = "";
		String nowrap = "";
		if(params.headernowrap){
			nowrapie = "nowrap";
			nowrap = "white-space:nowrap;";
		}
		
		result.append("<div id='"+listPropName+"GridHeaderDiv' "+
			    			"style='position:relative;width:100%;height:"+params.headerheight+";overflow-y:scroll;overflow-x:hidden;margin:0;'>\n");
		result.append("<table id='"+listPropName+"GridHeaderTab' class='text gridHeader' cellpadding='0' cellspacing='0' "+
							  "style='position:absolute;top:0px;left:0px;"+nowrap+"height:"+params.headerheight+";width:"+params.gridwidth+";table-layout:fixed;'>\n");

		result.append("<col width='0' style='width:0'>\n");
		for(int i=0;i<colWidths.size();i++){
			String colw = (String)colWidths.get(i);
			result.append("<col width='"+colw+"'>\n");
		}
		
		result.append("<tbody>");
		result.append("<tr style='height:"+params.headerheight+";'>\n");
		
		result.append("<td style='"+params.headerheight+";' abstractType='IntegerType' propertyName='absIndex'></td>\n");
		CommandDataModel rowModel = (CommandDataModel)listValue.get(0);
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
			if(params.containscdata)
				title = title.replaceAll("\\<", "&lt;").replaceAll("\\>", "&gt;");
			
			String style="";
			if(i == 0 && !params.showcounter)
				style = "style='border-left:solid 1px darkgray;'";
			result.append("<td "+nowrapie+" abstractType='"+abstractType+"' propertyName='"+propName+"' "+style+">"+title+"</td>\n");
		}

		result.append(  "</tr>\n");
		result.append("</tbody>");
		result.append("</table>\n");
		result.append("</div>\n");
		
		return result.toString();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getGridBody() throws Exception{
		
		StringBuffer result = new StringBuffer();
		
		String xOverflow = "hidden";
		if(!params.gridwidth.equals("100%"))
			xOverflow = "scroll";

		String nowrapie = "";
		String nowrap = "";
		if(params.cellsnowrap){
			nowrapie = "nowrap";
			nowrap = "white-space:nowrap;";
		}

		result.append("<div id='"+listPropName+"GridTabDiv' onscroll='__gridIntf.gridListManageOnScroll(this);' "+
						    "style='position:relative;width:100%;height:100%;overflow-y:scroll;overflow-x:"+xOverflow+";margin:0;'>\n");
		result.append("<table id='"+listPropName+"GridTab' cellpadding='0' cellspacing='0' class='gridBody' "+
							  "style='position:absolute;top:0px;left:0px;"+nowrap+"width:"+params.gridwidth+";table-layout:fixed;'>\n");
		
		result.append("<col width='0' style='width:0'>\n");
		for(int i=0;i<colWidths.size();i++){
			String colw = (String)colWidths.get(i);
			result.append(	"<col width='"+colw+"'>\n");
		}

		result.append("<tbody>");
		int rowIndexInPage = 0;
		for(ListIterator listIt=listValue.getPageRowsIterator();listIt.hasNext();){
			int rowIndex = listValue.getAbsIndex(listIt.nextIndex());
			CommandDataModel rowModel = (CommandDataModel) listIt.next();
			result.append(getGridRow(rowIndexInPage,rowIndex,rowModel,nowrapie));
			rowIndexInPage++;
		}
		result.append("</tbody>");
		result.append("</table>\n");
		result.append("</div>\n");	
		
		return result.toString();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getGridRow(int rowIndexInPage, int rowIndex, CommandDataModel rowModel, String nowrapie) throws Exception{
		
		StringBuffer result = new StringBuffer();
		String style = "";
		if(params.cellheight.length() > 0)
			style += "height:"+params.cellheight+";";
		if(params.selection.equalsIgnoreCase("multi") || params.selection.equalsIgnoreCase("single"))
			style += "cursor:pointer;";
		if(params.modality != Template.READ_MODALITY && !params.helper.equalsIgnoreCase("yes") && Tools.containsTypeErrors(rowModel))
			style += "background-color:red;";
		if(style.length() > 0)
			style = "style='"+style+"'";
		
		result.append("<tr absIndex='"+rowIndex+"' rowIndexInPage='"+rowIndexInPage+"' class='gridRow' "+style+" ");
		for(int i=0;i<cols.size();i++){
			String propName = (String)cols.get(i);
			AbstractType propValue = (AbstractType)Tools.getPropertyValue(rowModel,propName);
			String pv = "";
			if(propValue != null){
				pv = propValue.toString().replaceAll("\\\r","").replaceAll("\\\n","");
				pv = Tools.stringToHTMLString(pv.toString());
			}
			result.append("pv"+i+"=\""+pv+"\" ");
		}
		result.append(">\n");

		result.append("<td rowIndex='"+rowIndex+"'></td>\n");
		for(int i=0;i<visibleCols.size();i++){
			String propName = (String)visibleCols.get(i);
			String event = (String)visibleColsEvents.get(i);
			result.append(getGridCell(rowIndex,i,propName,rowModel,event,nowrapie));
		}
		result.append("</tr>\n");
		return result.toString();
	}	
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getGridCell(int rowIndex, int cellIndex, 
							   String propName, CommandDataModel rowModel, String event, String nowrapie) throws Exception{
		
		String originalPropName = propName.toString();
		CommandDataModel originalRowModel = rowModel;
		
		int idx = propName.lastIndexOf(Constants.NESTED_INDICATOR);
		if(idx >= 0){
			rowModel = (CommandDataModel)Tools.getPropertyValue(rowModel,propName.substring(0,idx));
			propName = propName.substring(idx+1);			
		}

		String cellModality = params.strFieldsModality;

		AbstractType propValue = (AbstractType)Tools.getPropertyValue(rowModel,propName);
		if(propValue == null){
			propValue = new StringType();
			propValue.setEditable(false);
		}
		
		if(gridDecorator != null){
			originalRowModel.setTemplate(pageRenderer.getTemplate());
			gridDecorator.onNewCell(listPropName,originalPropName,originalRowModel,propValue,rowIndex,cellIndex);
			originalRowModel.setTemplate(null);
		}
		
		String editable = "false";
		String cellInnerHTML ="";
		if(!propValue.isEditable())
			cellModality = "read";
		
		if(params.modality != Template.READ_MODALITY && propValue.isEditable()){
			
			editable = "true";
			String savPrefix = template.getPrefix();
			template.setPrefix("");
			int savModality = template.getModality();
			template.setModality(params.fieldsModality);
			String extraParameters = "";
			if(propValue.getStyle() != null && propValue.getStyle().extraParameters != null)
				extraParameters = propValue.getStyle().extraParameters;
			String style = "";
			if(event.indexOf(" size=") < 0 && event.indexOf(" style=") < 0)
				style="style='width:100%;'";
			cellInnerHTML = template.field(listPropName+rowIndex+"_"+originalPropName,
										   extraParameters+" helper='"+params.helper+"' "+style+" labelposition='nolabel' "+event);
			template.setPrefix(savPrefix);
			template.setModality(savModality);
			
		}else{
			
			if(rowModel.getCodDescDataList(propName) != null){
				
				cellInnerHTML = rowModel.getDescValue(propName);
				
			}else{
				
				if(propValue instanceof BooleanType)
					cellInnerHTML = ffTemplate.getProperty("BooleanType."+propValue);
				else if(propValue instanceof DoubleType)
					cellInnerHTML = ((DoubleType)propValue).toScaledString(template.getDoubleScale());
				else
					cellInnerHTML = propValue.toString();
				
			}
			
			cellInnerHTML = Tools.convertSpecialChars(cellInnerHTML);
			if(params.containscdata)
				cellInnerHTML = cellInnerHTML.replaceAll("\\<", "&lt;").replaceAll("\\>", "&gt;");

		}
		
		cellInnerHTML.trim();
		if(cellInnerHTML.equals(""))
			cellInnerHTML = "&nbsp;";
		
		StringBuffer result = new StringBuffer();

		String textalign = params.textalign;
		String style = "";
		if(propValue.hasTypeErrors() || propValue.hasTypeWarnings())
			style = "padding-right:15px;";
		result.append("<td "+nowrapie+" modality='"+cellModality+"' editable='"+editable+"' class='gridBodyCell' ");
		if(propValue.getStyle() != null){
			if(propValue.getStyle().innerHTML != null)
				cellInnerHTML = propValue.getStyle().innerHTML;
			if(propValue.getStyle().color != null)
				style += "color:"+HtmlColorMapping.getColor(propValue.getStyle().color)+";";
			if(propValue.getStyle().backgroundColor != null)
				style += "background-color:"+HtmlColorMapping.getColor(propValue.getStyle().backgroundColor)+";";
			if(propValue.getStyle().textAlign != null)
				textalign = propValue.getStyle().textAlign;
		}
		
		if(!textalign.equals("left"))
			style+= "text-align:"+textalign+";";
		if(cellIndex == 0 && !params.showcounter)
			style += "border-left:solid 1px darkgray;";
		if(params.cellheight.length() > 0)
			style += "height:"+params.cellheight+";";
			
		if(style.length() > 0)
			result.append("style='"+style+"' ");
		if(params.generatecellid)
			result.append("id='"+listPropName+rowIndex+originalPropName+"' ");
		result.append(">"+cellInnerHTML+"</td>\n"); 
		return result.toString();
	}	
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getPagination(){

		if(listValue.getTotalPages() == 1 || listValue.getRowsPerPage() == -1)
			return "";
		
		String options = "";
		StringBuffer result = new StringBuffer();
		
		result.append("<table class='pageLabel' cellspacing='0' cellpadding='0'>\n");
		result.append("<tr>\n");

		/***** firstPage *****/
		if(listValue.getCurrentPage() == 1)
			options = "class='disabledPageLabel'";
		else
			options = "onclick='__gridIntf.firstPage(\""+params.paginationformname+"\",\""+listPropName+"\");'";
		result.append("<td "+options+" title=\""+ffTemplate.getProperty("pageSelector.firstPage")+"\">\n");
		result.append(FILLED_LT + FILLED_LT);
		result.append("</td>\n");
		result.append("<td>&nbsp;&nbsp;</td>\n");

		/***** previuosPage *****/
		if(listValue.getCurrentPage() == 1)
			options = "class='disabledPageLabel'";
		else
			options = "onclick='__gridIntf.previousPage(\""+params.paginationformname+"\",\""+listPropName+"\");'";
		result.append("<td "+options+" title=\""+ffTemplate.getProperty("pageSelector.previousPage")+"\">\n");
		result.append(FILLED_LT);
		result.append("</td>\n");
		result.append("<td>&nbsp;&nbsp;</td>\n");

		/***** pages *****/
		for(int i=1;i<=listValue.getTotalPages();i++){

			if(i == listValue.getCurrentPage())
				options = "class='disabledPageLabel'";
			else
				options = "onclick='__gridIntf.gotoPage(\""+params.paginationformname+"\",\""+i+"\",\""+listPropName+"\");'";
			result.append("<td "+options+" title=\""+ffTemplate.getProperty("pageSelector.gotoPage")+(" "+i+" ")+"\">\n");
			result.append(""+i);
			result.append("</td>\n");
			if (i == listValue.getTotalPages())
				result.append("<td>&nbsp;&nbsp;</td>\n");
			else
				result.append("<td>&nbsp;</td>\n");
		}

		/***** nextPage *****/
		if(listValue.getCurrentPage() == listValue.getTotalPages())
			options = "class='disabledPageLabel'";
		else
			options = "onclick='__gridIntf.nextPage(\""+params.paginationformname+"\",\""+listPropName+"\");'";
		result.append("<td "+options+" title=\""+ffTemplate.getProperty("pageSelector.nextPage")+"\">\n");
		result.append(FILLED_GT);
		result.append("</td>\n");
		result.append("<td>&nbsp;&nbsp;</td>\n");

		/***** lastPage *****/
		if(listValue.getCurrentPage() == listValue.getTotalPages())
			options = "class='disabledPageLabel'";
		else
			options = "onclick='__gridIntf.lastPage(\""+params.paginationformname+"\",\""+listPropName+"\");'";
		result.append("<td "+options+" title=\""+ffTemplate.getProperty("pageSelector.lastPage")+"\">\n");
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
		
		result.append("<form style='height:100%;margin: 0px;' name='"+params.paginationformname+"' method='post' action='"+Template.CONTROLLER_CALL+"'>\n");
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
		
		result.append("<table width='100%' height='100%' class='text gridBottomArea' cellspacing='0' cellpadding='0'>\n");
		result.append("<tr>\n");
		
		result.append("<td width='15%'>\n");
		if(listValue.getTotalPages() > 1)
			result.append("&nbsp;"+ffTemplate.getProperty("gridMsg.pageNumber",Integer.toString(listValue.getCurrentPage())));
		else
			result.append("&nbsp;");
		result.append("</td>\n");
		
		result.append("<td align='center'>\n");
		if(listValue.isMaxRowsExceeded())
			result.append(ffTemplate.getProperty("gridMsg.maxRowsExceeded",Integer.toString(listValue.size())));
		else
			result.append(ffTemplate.getProperty("gridMsg.numRows",Integer.toString(listValue.size())));
		result.append("</td>\n");
		result.append("<td width='10%'>&nbsp;</td>\n");
		
		result.append("</tr>\n");
		result.append("</table>\n");
		
		return result.toString();
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
	public String getPdfCalc(boolean isPdf, boolean innerGrid) throws Exception{
		
		calculateColumnsWidth(); // Change "*" col width to a numeric value
		
		StringBuffer result = new StringBuffer();
		
		String title = params.title.toString();
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
			if(!params.title.toString().equals("")){
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
								params.title.toString()+
								"</b></font></td></tr>");
				result.append("<tr><td colspan=\""+colspan+"\">&nbsp;</td></tr>");
			}else{
				result.append("<table><!-- headerrows=\"1\" -->");				
			}
		}
		
		result.append("<tr>\n");

		// Header
		String hb = PRINT_HEADER_BACKGROUND;
		if(!isPdf)
			hb = PRINT_HEADER_CALC_BACKGROUND;
		
		for(int i=0;i<printableCols.size();i++){
			if(printableCols.get(i) == null)
				continue;
			title = (String)colTitles.get(i);
			if(title == null || title.equals(""))
				title = "&nbsp;";
			else
				title = Tools.convertSpecialChars(title);
			if(params.containscdata)
				title = title.replaceAll("\\<", "&lt;").replaceAll("\\>", "&gt;");
			result.append("<td border=\"1\" align=\"center\" ");
			String colw = (String)colWidths.get(i);
			result.append("width=\""+colw+"\" ");
			result.append("bgcolor=\""+hb+"\"><font color=\""+PRINT_HEADER_COLOR+"\">");
			result.append("<b>"+title+"</b>");
			result.append("</font></td>\n");
		}
		result.append("</tr>\n");
		
		// Body rows
		for(int i=0;i<listValue.size();i++){
			CommandDataModel rowModel = (CommandDataModel) listValue.get(i);
			result.append(getPdfCalcGridRow(i,rowModel));
		}
		
		if(params.footerhtml.length() > 0){
			result.append("<tr>\n");
			result.append("<td colspan='"+colspan+"'>\n");
			result.append(params.footerhtml);
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
		StringBuffer result = new StringBuffer();
		
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
			if(params.containscdata)
				cellInnerHTML = cellInnerHTML.replaceAll("\\<", "&lt;").replaceAll("\\>", "&gt;");
		}else{
			if(propValue instanceof BooleanType){
				cellInnerHTML = ffTemplate.getProperty("BooleanType."+propValue);
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
				totPercent += Integer.parseInt(PageRenderer.getNum(colWidth));
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

}
