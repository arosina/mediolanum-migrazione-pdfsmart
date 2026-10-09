package com.atosorigin.wfem.layout.htmlrenderer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.controller.Constants;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.layout.field.FieldModel;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.util.Tools;

/**************************************************************************************************/
/**************************************************************************************************/
public class ListTypeRenderer extends AbstractTypeRenderer {

	private static final String HEADER_STYLE_CLASS = "tableHeader";
	private static final int SCROLLBAR_WIDTH = 18;
	private static final int PAGE_SELECTOR_HEIGHT = 20;
	private int ROW_NUM_WIDTH = 25;

	private static final int DEFAULT_WIDTH = 800;
	private static final int DEFAULT_HEIGHT = 300;
	private static final String FILLED_LT = "&#9668;";
	private static final String FILLED_GT = "&#9658;";

	static class Patterns{
		private static final String paramPattern="\\s*=\\s*(['\"])(.*?)\\1";
		static final Pattern onclick	 	= Pattern.compile("onclick"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern ondblclick	 	= Pattern.compile("ondblclick"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern onnewrow	 	= Pattern.compile("onnewrow"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern onnewcell	 	= Pattern.compile("onnewcell"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern width	 		= Pattern.compile("width"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern height	 		= Pattern.compile("height"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern noRowsMsg	 	= Pattern.compile("noRowsMsg"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern selection	 	= Pattern.compile("selection"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern sortable	 	= Pattern.compile("sortable"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern pagination	 	= Pattern.compile("pagination"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern showcounter	= Pattern.compile("showcounter"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern tablewidth	 	= Pattern.compile("tablewidth"+paramPattern,Pattern.CASE_INSENSITIVE);
	}

	private int width;
	private int tableWidth = -1;
	private int height;
	private String onClick;
	private String onDblClick;
	private String onNewRow;
	private String onNewCell;
	private String noRowsMsg;
	private String selectionPolicy;
	private String sortable = "true";
	private String pagination = "true";
	private String showCounter = "true";
	private ListType listValue;
	private String[] propertyNames;
	private String[] colWidths;
	private Template ffTemplate;

	public ListTypeRenderer(
		FieldModel fieldModel,
		ListType listValue,
		String[] propertyNames,
		String[] colWidths,
		String webApp,
		String modelPropName,
		CommandDataModel dataModel,
		Template template,
		Template ffTemplate,
		String pageName) {
		super(fieldModel, webApp, modelPropName, dataModel, template, pageName);

		listValue.recalc();
		setListValue(listValue);

		setPropertyNames(propertyNames);

		setWidth(DEFAULT_WIDTH);
		setHeight(DEFAULT_HEIGHT);
		setFfTemplate(ffTemplate);

		if (colWidths == null || colWidths.length == 0) {
			List propNames = getRowEntries(null, true);

			colWidths = new String[propNames.size()];
			for (int i = 0; i < colWidths.length; i++) {
				colWidths[i] = "*";
			}
		}
		setColWidths(colWidths);

		try {

			Matcher mat = null;
			
			String extraPar = getFieldModel().getExtraPar();

			LOG.debug("makeListType(): extraPar=[" + extraPar + "]");

			mat = Patterns.onclick.matcher(extraPar);
			if (mat.find()) {
				setOnClick(mat.group(2));
				getFieldModel().setExtraPar(
					extraPar.substring(0, mat.start()) + extraPar.substring(mat.end()));
				LOG.debug("makeListType(): [onClick] parameter found -> onClick=" + getOnClick());
				extraPar = getFieldModel().getExtraPar();
			}

			mat = Patterns.ondblclick.matcher(extraPar);
			if (mat.find()) {
				setOnDblClick(mat.group(2));
				getFieldModel().setExtraPar(
					extraPar.substring(0, mat.start())
						+ extraPar.substring(mat.end()));
				LOG.debug("makeListType(): [onDblClick] parameter found -> onDblClick=" + getOnDblClick());
				extraPar = getFieldModel().getExtraPar();
			}

			mat = Patterns.onnewcell.matcher(extraPar);
			if (mat.find()) {
				setOnNewCell(mat.group(2));
				getFieldModel().setExtraPar(
					extraPar.substring(0, mat.start())
						+ extraPar.substring(mat.end()));
				LOG.debug("makeListType(): [onNewCell] parameter found -> onNewCell=" + getOnNewCell());
				extraPar = getFieldModel().getExtraPar();
			}

			mat = Patterns.onnewrow.matcher(extraPar);
			if (mat.find()) {
				setOnNewRow(mat.group(2));
				getFieldModel().setExtraPar(
					extraPar.substring(0, mat.start()) + extraPar.substring(mat.end()));
				LOG.debug("makeListType(): [onNewRow] parameter found -> onNewRow=" + getOnNewRow());
				extraPar = getFieldModel().getExtraPar();
			}

			mat = Patterns.tablewidth.matcher(extraPar);
			if (mat.find()) {
				setTableWidth(Integer.parseInt(mat.group(2)));
				getFieldModel().setExtraPar(
					extraPar.substring(0, mat.start()) + extraPar.substring(mat.end()));
				LOG.debug("makeListType(): [tablewidth] parameter found -> tablewidth=" + getTableWidth());
				extraPar = getFieldModel().getExtraPar();
			}

			mat = Patterns.width.matcher(extraPar);
			if (mat.find()) {
				setWidth(Integer.parseInt(mat.group(2)));
				getFieldModel().setExtraPar(
					extraPar.substring(0, mat.start()) + extraPar.substring(mat.end()));
				LOG.debug("makeListType(): [width] parameter found -> width=" + getWidth());
				extraPar = getFieldModel().getExtraPar();
			}

			mat = Patterns.height.matcher(extraPar);
			if (mat.find()) {
				setHeight(Integer.parseInt(mat.group(2)));
				getFieldModel().setExtraPar(
					extraPar.substring(0, mat.start()) + extraPar.substring(mat.end()));
				LOG.debug("makeListType(): [height] parameter found -> height=" + getHeight());
				extraPar = getFieldModel().getExtraPar();
			}

			mat = Patterns.noRowsMsg.matcher(extraPar);
			if (mat.find()) {
				setNoRowsMsg(mat.group(2));
				getFieldModel().setExtraPar(
					extraPar.substring(0, mat.start())
						+ extraPar.substring(mat.end()));
				LOG.debug("makeListType(): [noRowsMsg] parameter found -> noRowsMsg=" + getNoRowsMsg());
				extraPar = getFieldModel().getExtraPar();
			}

			mat = Patterns.selection.matcher(extraPar);
			if (mat.find()) {
				setSelectionPolicy(mat.group(2));
				getFieldModel().setExtraPar(
					extraPar.substring(0, mat.start())
						+ extraPar.substring(mat.end()));
				LOG.debug("makeListType(): [selectionPolicy] parameter found -> selectionPolicy=" + getSelectionPolicy());
				extraPar = getFieldModel().getExtraPar();
			}

			mat = Patterns.sortable.matcher(extraPar);
			if (mat.find()) {
				setSortable(mat.group(2));
				getFieldModel().setExtraPar(
					extraPar.substring(0, mat.start())
						+ extraPar.substring(mat.end()));
				LOG.debug("makeListType(): [sortable] parameter found -> sortable=" + getSortable());
				extraPar = getFieldModel().getExtraPar();
			}

			mat = Patterns.pagination.matcher(extraPar);
			if (mat.find()) {
				setPagination(mat.group(2));
				getFieldModel().setExtraPar(
					extraPar.substring(0, mat.start())
						+ extraPar.substring(mat.end()));
				LOG.debug("makeListType(): [pagination] parameter found -> pagination=" + getPagination());
				extraPar = getFieldModel().getExtraPar();
			}

			mat = Patterns.showcounter.matcher(extraPar);
			if (mat.find()) {
				setShowCounter(mat.group(2));
				getFieldModel().setExtraPar(
					extraPar.substring(0, mat.start())
						+ extraPar.substring(mat.end()));
				LOG.debug("makeListType(): [showCounter] parameter found -> showCounter=" + getShowCounter());
				extraPar = getFieldModel().getExtraPar();
			}
			
			if(!showCounter.equalsIgnoreCase("true"))
				ROW_NUM_WIDTH = 1;

		} catch (RuntimeException e) {
			LOG.warning("makeListType(): " + e);
		}
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getFieldRendering() throws Exception {

		String listName = getFieldModel().getPropName();
		boolean hasPageSelector = getListValue().getTotalPages() > 1;
		if(!getPagination().equalsIgnoreCase("true"))
			hasPageSelector = false;

		String rs = "";

		if(getPagination().equalsIgnoreCase("true")){
			rs += "<DIV id='"
				+ listName
				+ "ListTypePageSelectorView' "
				+ ("style='margin-top: 3px; "
					+ "visibility: hidden;"
					+ ("width: " + getWidth() + "; ")
					+ ("height: " + PAGE_SELECTOR_HEIGHT + "; ")
					+ "' ")
				+ " >\n";
			rs += hasPageSelector ? getPageSelectorRendering() : "";
			rs += "</DIV>\n";
		}

		rs += "<DIV align='left' id='"
			+ listName
			+ "ListTypeView' "
			+ "class='panel' "
			+ ("style='"
				+ "visibility: hidden;"
				+ ("width: " + getWidth() + "; ")
				+ "background-color: whitesmoke; "
				+ "' ")
			+ " >\n";

		if (listValue.size() > 0) {
			if(getTableWidth() > 0){
				rs += getDivTagRendering();
				rs += getWidthTableTagRendering("TableHeader", true);
			}else{
				rs += getTableTagRendering("TableHeader", true);				
			}
			rs += getHeaderRendering();
			rs += "</TABLE>\n";

			if(getTableWidth() > 0){
				rs += getWidthTableTagRendering("Table", true);
			}else{
				rs += getDivTagRendering();
				rs += getTableTagRendering("Table", true);
			}
			rs += getBodyRendering();
			rs += "</TABLE>\n";
			rs += "</DIV>\n";

			rs += getTableTagRendering(null, false);
			rs += getFooterRendering();
			rs += "</TABLE>\n";

			rs += hasPageSelector ? getPageSelectorFormRendering() : "";
			rs += hasPageSelector ? getPageSelectorScriptRendering() : "";
			rs += getRowFormRendering();
			rs += getScriptRendering();
		} else {
			setHeight(getHeight() + 25);
			rs += getDivTagRendering();
			rs += "<DIV style='height: " + (getHeight() + 1) / 3 + ";'></DIV>\n";
			rs += "<DIV align='center'>";
			rs += "<SPAN class='message' style='border-style: none;' >"
				+ (getNoRowsMsg() != null ? getNoRowsMsg() : ffTemplate.getProperty("dao.noRows"))
				+ "</SPAN>\n";
			rs += "</DIV>\n";
			rs += "</DIV>\n";
			rs += "<SCRIPT>" + listName + "ListTypeView.style.visibility = 'visible';</SCRIPT>\n";
		}
		rs += "</DIV>\n";

		return rs;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getRowFormRendering() {

		String listName = getFieldModel().getPropName();

		String rs = "";

		rs += "<SCRIPT>\n";
		rs += "\tvar " + listName + "RowForm = new Object();\n";
		rs += "\t" + listName + "RowForm.all = new Array();\n";

		List modelRowEntries;

		modelRowEntries = getRowEntries(null, false);

		String[] currentEntry;
		int i = 0;
		for (Iterator it = modelRowEntries.iterator(); it.hasNext();) {
			currentEntry = (String[]) it.next();
			rs += ("\t" + listName + "RowForm.all[" + i + "] = new Object();\n")
				+ ("\t" + listName + "RowForm.all[" + i + "].id=\"" + currentEntry[0] + "\";\n")
				+ ("\t" + listName + "RowForm.all[" + i + "].name=\"" + currentEntry[0] + "\";\n")
				+ ("\t" + listName + "RowForm.all[" + i + "].value=\"\";\n");
			i++;
		}

		rs += ("\t" + listName + "RowForm.all[" + i + "] = new Object();\n")
				+ ("\t" + listName + "RowForm.all[" + i + "].id=\"absIndex\";\n")
				+ ("\t" + listName + "RowForm.all[" + i + "].name=\"absIndex\";\n")
				+ ("\t" + listName + "RowForm.all[" + i + "].value=\"\";\n");

		rs += "</SCRIPT>\n";

		return rs;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getDivTagRendering() {

		String rs = "";
		rs += "<DIV align='left' ";
		rs += "style='";
		rs += "overflow: auto; ";
		rs += "width: " + getWidth() + "; ";
		rs += "height: " + getHeight() + "; ";
		rs += "' ";
		rs += ">\n";

		return rs;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getTableTagRendering(String id, boolean hasScrollBar) {

		String listName = getFieldModel().getPropName();

		String rs = "";
		rs += "<TABLE ";
		rs += id != null ? ("id=\"" + listName + id + "\" ") : "";
		rs += "cellpadding='0' cellspacing='0' ";
		rs += "style='";
		rs += "table-layout: fixed; ";
		rs += "border-color: silver; ";
		rs += "background-color: white; ";
		rs += "width: " + (getWidth() - (hasScrollBar ? SCROLLBAR_WIDTH : 0)) + "; ";
		rs += "' ";
		rs += ">\n";

		return rs;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getWidthTableTagRendering(String id, boolean hasScrollBar) {

		String listName = getFieldModel().getPropName();

		String rs = "";
		rs += "<TABLE ";
		rs += id != null ? ("id=\"" + listName + id + "\" ") : "";
		rs += "cellpadding='0' cellspacing='0' ";
		rs += "style='";
		rs += "table-layout: fixed; ";
		rs += "border-color: silver; ";
		rs += "background-color: white; ";
		rs += "width: " + (getTableWidth() - (hasScrollBar ? SCROLLBAR_WIDTH : 0)) + "; ";
		rs += "' ";
		rs += ">\n";

		return rs;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getBodyRendering() {

		String rs = "";

		rs += getColTagRendering(true);

		CommandDataModel rowModel;
		List modelRowEntries;
		List viewRowEntries;
		for (ListIterator listIt = getListValue().getPageRowsIterator(); listIt.hasNext();) {

			int absIndex = getListValue().getAbsIndex(listIt.nextIndex());
			rowModel = (CommandDataModel) listIt.next();
			modelRowEntries = getRowEntries(rowModel, false);
			viewRowEntries = getRowEntries(rowModel, true);

			rs += getTrTagRendering(modelRowEntries, absIndex, rowModel);

			rs += "\t\t<TD title='" + (absIndex + 1) + "' >";
			rs += getActionRendering(absIndex);
			rs += "</TD>\n";

			String[] currentEntry;
			for (Iterator it = viewRowEntries.iterator(); it.hasNext();) {
				currentEntry = (String[]) it.next();
				rs += "\t\t<TD title=\"" + currentEntry[1] + "\">";
				rs += currentEntry[1].equals("") ? "&nbsp;" : currentEntry[1];
				rs += "</TD>\n";
			}
			rs += "\t</TR>\n";
		}
		return rs;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getTrTagRendering(List rowEntries, int absIndex, CommandDataModel rowModel) {
		String rs = "";

		rs += "\t<TR absIndex='" + absIndex + "' ";
		rs += !rowModel.isValid() ? " disabled=true " : "";
		String[] currentEntry;
		for (Iterator it = rowEntries.iterator(); it.hasNext();) {
			currentEntry = (String[]) it.next();
			rs += currentEntry[0] + "=\"";
			rs += currentEntry[1];
			rs += "\" ";
		}
		rs += ">\n";

		return rs;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getActionRendering(int rowIndex) {
		String rs = "";

		rs += "<INPUT type='button' ";
		rs += "title='" + (rowIndex + 1) + "' ";
		rs += "value='" + (rowIndex + 1) + "' ";
		rs += ">";

		return rs;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getHeaderRendering() throws Exception {

		CommandDataModel model = (CommandDataModel) listValue.getModelType().newInstance();

		String listName = getFieldModel().getPropName();

		String rs = "";

		rs += getColTagRendering(true);

		rs += "\t<TR class='" + HEADER_STYLE_CLASS + "' align='center' >\n";

		rs += "\t\t<TD "
			+ "abstractType=\""
			+ com.atosorigin.wfem.types.IntegerType.class.getName()
			+ "\""
			+ ">"
			+ getHeaderActionRendering("&nbsp;&nbsp;")
			+ "</TD>\n";

		String[] currentEntry;
		for (Iterator it = getRowEntries(null, true).iterator(); it.hasNext();) {
			currentEntry = (String[]) it.next();
			rs += "\t\t<TD "
				+ ("title=\"" + (getTemplate().getProperty(getPageName() + listName + "." + currentEntry[0])) + "\" ")
				+ ("abstractType=\"" + (Tools.getPropertyType(model, currentEntry[0]).getName()) + "\" ")
				+ ("propertyName=\"" + currentEntry[0] + "\" ")
				+ ">";
			rs += getHeaderActionRendering(getTemplate().getProperty(getPageName() + listName + "." + currentEntry[0]));
			rs += "</TD>\n";
		}
		//rs += "\t\t<TD/>\n";

		rs += "\t</TR>\n";

		return rs;
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getHeaderActionRendering(String label) {
		String rs = "";

		rs += "<INPUT type='button' ";
		rs += "title='" + label + "' ";
		rs += "value='" + label + "' ";
		rs += ">";

		return rs;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getFooterRendering() {

		String rs = "";

		rs += getColTagRendering(false);

		rs += "\t<TR class='" + HEADER_STYLE_CLASS + "' align='center' >\n";

		rs += "\t\t<TD/>\n";

		int colNum = getRowEntries(null, true).size();

		String msg =
			getListValue().isMaxRowsExceeded()
				? (getFfTemplate().getProperty("dao.maxRowsExceeded")
					+ " "
					+ getFfTemplate().getProperty("dao.maxRowsNumber")
					+ getListValue().size())
				: "&nbsp;";

		rs += "\t\t<TD ";
		rs += "colspan='" + colNum + "' ";
		rs += "title=\"" + msg + "\" ";
		rs += "nowrap='nowrap' ";
		rs += ">";
		rs += msg;
		rs += "</TD>\n";
		rs += "\t\t<TD/>\n";

		rs += "\t</TR>\n";

		return rs;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getColTagRendering(boolean hasScrollBar) {

		String rs = "";
		rs += "\t<COL width='" + ROW_NUM_WIDTH + "'>\n";

		if (getColWidths() != null) {
			String currentWidth;
			for (Iterator it = Arrays.asList(getColWidths()).iterator(); it.hasNext();) {
				currentWidth = (String) it.next();
				rs += "\t<COL width='" + currentWidth + "'>\n";
			}
		}
		rs += hasScrollBar ? "" : "\t<COL width='" + SCROLLBAR_WIDTH + "'>\n";
		return rs;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getScriptRendering() {

		String listName = getFieldModel().getPropName();

		String rs = "";
		rs += "<SCRIPT>\n";
		rs += "\tvar "
			+ (listName + "JSTableList")
			+ " = new JSTableList("
			+ (listName + "ListTypeView")
			+ ", "
			+ (listName + "ListTypeView" + ".all." + listName + "Table")
			+ ", "
			+ (listName + "ListTypeView" + ".all." + listName + "TableHeader")
			+ ", "
			+ (listName + "RowForm")
			+ ", "
			+ listValue.getCurrentPage()
			+ ", "
			+ listValue.getTotalPages()
			+ ", "
			+ listValue.getRowsInPage()
			+ (getOnNewRow() != null ? (", '" + getOnNewRow() + "'") : ", null")
			+ (getOnNewCell() != null ? (", '" + getOnNewCell() + "'") : ", null")
			+ (getSelectionPolicy() != null ? (", '" + getSelectionPolicy() + "'") : ", null")
			+ (getSortable() != null ? (", " + getSortable()) : ", null")
			+ ");\n";

		rs += getOnClick() != null
			? ("\t" + (listName + "JSTableList") + ".setOnClick('" + getOnClick() + "');\n")
			: "";
		rs += getOnDblClick() != null
			? ("\t" + (listName + "JSTableList") + ".setOnDblClick('" + getOnDblClick() + "');\n")
			: "";

		rs += "</SCRIPT>\n";

		return rs;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getPageSelectorRendering() {

		String listName = getFieldModel().getPropName();

		String rs = "";
		rs += "<TABLE  align='center' ";
		rs += "id='" + listName + "PageSelector' ";
		rs += "cellspacing='0' cellpadding='0'>\n";

		rs += "\t<TR >\n";

		/***** firstPage *****/
		rs += "\t\t<TD ";
		rs += "title=\"" + getFfTemplate().getProperty("pageSelector.firstPage") + "\" ";
		rs += ">";
		rs += FILLED_LT + FILLED_LT;
		rs += "</TD>\n";
		rs += "\t\t<TD>&nbsp;&nbsp;</TD>\n";
		/***** firstPage *****/

		/***** previuosPage *****/
		rs += "\t\t<TD ";
		rs += "title=\"" + getFfTemplate().getProperty("pageSelector.previousPage") + "\" ";
		rs += ">";
		rs += FILLED_LT;
		rs += "</TD>\n";
		rs += "\t\t<TD>&nbsp;&nbsp;</TD>\n";
		/***** previuosPage *****/

		for (int i = 1; i <= getListValue().getTotalPages(); i++) {

			/***** gotoPage *****/
			rs += "\t\t<TD ";
			rs += "title=\"" + getFfTemplate().getProperty("pageSelector.gotoPage") + (" " + i + " ") + "\" ";
			rs += ">";
			rs += i;
			rs += "</TD>\n";
			if (i == getListValue().getTotalPages())
				rs += "\t\t<TD>&nbsp;&nbsp;</TD>\n";
			else
				rs += "\t\t<TD>&nbsp;</TD>\n";
			/***** gotoPage *****/
		}

		/***** nextPage *****/
		rs += "\t\t<TD ";
		rs += "title=\"" + getFfTemplate().getProperty("pageSelector.nextPage") + "\" ";
		rs += ">";
		rs += FILLED_GT;
		rs += "</TD>\n";
		rs += "\t\t<TD>&nbsp;&nbsp;</TD>\n";
		/***** nextPage *****/

		/***** lastPage *****/
		rs += "\t\t<TD ";
		rs += "title=\"" + getFfTemplate().getProperty("pageSelector.lastPage") + "\" ";
		rs += ">";
		rs += FILLED_GT + FILLED_GT;
		rs += "</TD>\n";
		/***** lastPage *****/

		rs += "\t</TR>\n";
		rs += "</TABLE>\n";

		return rs;
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getPageSelectorScriptRendering() {

		String listName = getFieldModel().getPropName();

		String rs = "";
		rs += "<SCRIPT>\n";
		rs += "\tvar "
			+ (listName + "JSPageSelector")
			+ " = new JSPageSelector("
			+ ("document" + ".all." + listName + "ListTypePageSelectorView")
			+ ", "
			+ ("document" + ".all." + listName + "PageSelector")
			+ ", "
			+ ("document" + ".all." + listName + "PageSelectorForm")
			+ ", "
			+ listValue.getCurrentPage()
			+ ", "
			+ listValue.getTotalPages()
			+ ");\n";

		rs += "</SCRIPT>\n";

		return rs;
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getPageSelectorFormRendering() {

		String listName = getFieldModel().getPropName();

		String rs = "";
		rs += "<FORM name='" + listName + "PageSelectorForm' ";
		rs += "method='post' action='" + Template.CONTROLLER_CALL + "'>\n";
		rs += "\t<INPUT type='hidden' name='" + Template.CONTROLLER_CMD + "' value=''>\n";
		rs += "\t<INPUT type='hidden' name='index' value=''>\n";
		rs += "\t<INPUT type='hidden' name='listPropertyName' value='" + listName + "'>\n";
		rs += "\t<INPUT type='hidden' name='BrowserInstance' ";
		rs += "value='" + getFfTemplate().getRequest().getAttribute("BrowserInstance") + "' >\n";
		rs += "</FORM>\n";

		return rs;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private List getRowEntries(CommandDataModel rowModel, boolean isView) {
		ArrayList rowEntries = new ArrayList();

		String currentProp;
		for (Iterator it = Arrays.asList(getPropertyNames()).iterator(); it.hasNext();) {
			boolean isHiddenProp = false;

			currentProp = (String) it.next();
			if (currentProp.startsWith("#")) {
				currentProp = currentProp.substring(1, currentProp.length());
				isHiddenProp = true;
			}

			if (isView && isHiddenProp)
				continue;

			String originalCurrentProp = currentProp;
			String propValue = null;
			if (rowModel != null) {

				/*********************  managing nested property's cod/desc ************************/
				CommandDataModel originalRowModel = rowModel;
				
				String separator = Constants.NESTED_INDICATOR;
				int pos;
				if((pos = currentProp.indexOf(separator)) != -1) {
					try {
						rowModel = (CommandDataModel) Tools.getPropertyValue(rowModel, currentProp.substring(0, pos));
						currentProp = currentProp.substring(pos+1);
					} catch (Exception e) {
						String errorMsg = "Exception in " + getClass().getName() + ".getRowEntries: " + e;
						LOG.debug(errorMsg);
					}
				}
				/************************************************************************************/
				
				if (rowModel.getCodDescDataList(currentProp) != null) {
					propValue = rowModel.getDescValue(currentProp);
					if(!isView){
						String codValue = "";
						try {
							AbstractType abstractPropValue = (AbstractType) Tools.getPropertyValue(rowModel, currentProp);
							codValue = abstractPropValue.toString();
							if (abstractPropValue instanceof StringType) {
								codValue = Tools.stringToHTMLString(codValue);
							}
						} catch (Exception e) {
							codValue = "" + e;
						}
						rowEntries.add(new String[] { originalCurrentProp+"_cod", codValue });
					}
				} else {
					try {
						AbstractType abstractPropValue = (AbstractType) Tools.getPropertyValue(rowModel, currentProp);
						propValue = abstractPropValue.toString();
						if (abstractPropValue instanceof StringType) {
							propValue = Tools.stringToHTMLString(propValue);
						}
					} catch (Exception e) {
						propValue = "" + e;
					}
				}
				rowModel = originalRowModel; 
			}
			rowEntries.add(new String[] { originalCurrentProp, propValue });
		}

		return rowEntries;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getOnClick() {
		return onClick;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getOnDblClick() {
		return onDblClick;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setOnClick(String onClick) {
		this.onClick = onClick;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setOnDblClick(String onDblClick) {
		this.onDblClick = onDblClick;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String[] getColWidths() {
		return colWidths;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public ListType getListValue() {
		return listValue;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String[] getPropertyNames() {
		return propertyNames;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/

	public void setColWidths(String[] colWidths) {
		this.colWidths = colWidths;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setListValue(ListType listValue) {
		this.listValue = listValue;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public int getHeight() {
		return height;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public int getWidth() {
		return width;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setHeight(int height) {
		this.height = height;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setPropertyNames(String[] propertyNames) {
		this.propertyNames = propertyNames;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setWidth(int width) {
		this.width = width;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public Template getFfTemplate() {
		return ffTemplate;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setFfTemplate(Template ffTemplate) {
		this.ffTemplate = ffTemplate;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getOnNewCell() {
		return onNewCell;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getOnNewRow() {
		return onNewRow;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setOnNewCell(String onNewCell) {
		this.onNewCell = onNewCell;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setOnNewRow(String onNewRow) {
		this.onNewRow = onNewRow;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getNoRowsMsg() {
		return noRowsMsg;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setNoRowsMsg(String noRowsMsg) {
		this.noRowsMsg = noRowsMsg;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getSelectionPolicy() {
		return selectionPolicy;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setSelectionPolicy(String selectionPolicy) {
		this.selectionPolicy = selectionPolicy;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getSortable() {
		return sortable;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setSortable(String sortable) {
		this.sortable = sortable;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getPagination() {
		return pagination;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getShowCounter() {
		return showCounter;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setPagination(String pagination) {
		this.pagination = pagination;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setShowCounter(String showCounter) {
		this.showCounter = showCounter;
	}

	public int getTableWidth() {
		return tableWidth;
	}

	public void setTableWidth(int tableWidth) {
		this.tableWidth = tableWidth;
	}

}
