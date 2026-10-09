package com.atosorigin.wfem.layout.htmlrenderer;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.atosorigin.wfem.coddesc.CodDescData;
import com.atosorigin.wfem.coddesc.CodDescDataList;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.layout.FieldsFactory;
import com.atosorigin.wfem.layout.field.FieldModel;

/**************************************************************************************************/
/**************************************************************************************************/
public class ComboTypeRenderer extends AbstractTypeRenderer {

	private int listSize = -1;
	private String width;
	private String onChangeScript;
	private boolean showCode = false;
	private String otherPar;
	
	static class Patterns{
		private static final String paramPattern="\\s*=\\s*(['\"])(.*?)\\1";
		static final Pattern size	 	= Pattern.compile("size"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern width		= Pattern.compile("width"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern onchange	= Pattern.compile("onchange"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern showcode	= Pattern.compile("showcode"+paramPattern,Pattern.CASE_INSENSITIVE);
	}
	/**
	 * Constructor for ComboTypeRenderer.
	 * 
	 * Examples:
	 * 
	 * List rendering (with [size] parameter):
	 * 
	 * <%= ff.field("codProdotto", "size='10' width='150' onchange='myMethod(valueHolder, row.option.value, row.option.text);'") %>
	 * 
	 * 
	 * Select rendering Example:
	 * 
	 * <%= ff.field("codProdotto", "width='150' onchange='myMethod(valueHolder, row.option.value, row.option.text);'") %>
	 * 
	 * 
	 * Relative JScript supported:
	 * 
	 * <script> 	
	 * 
	 * 	var oOption = document.createElement("OPTION");					//
	 * 	oOption.text = "sText";											//
	 * 	oOption.value = "sValue";										//	
	 * 	document.all.codProdotto.options.add(oOption);					// standard ADD option method
	 * 
	 * 	document.all.codProdotto.add("cValue", "cText" [, className]); 	// custom ADD option method
	 * 	!!! document.all.codProdotto.add(oOption); 						//  NOT SUPPORTED
	 * 
	 * 	document.all.codProdotto.remove(3);								// standard REMOVE option method
	 * 	document.all.codProdotto.options.remove(3);						// standard REMOVE option method
	 * 
	 * 	document.all.codProdotto.removeAll();							// custom REMOVE ALL(-1) method
	 * 
	 * 
	 * 	document.all.codProdotto.length()			// replaces: 	 document.all.codProdotto.length
	 * 	document.all.codProdotto.options.length()	// replaces: 	 document.all.codProdotto.options.length
	 * 
	 * 	!!! document.all.codProdotto.length				// NOT SUPPORTED
	 * 	!!! document.all.codProdotto.options.length		// NOT SUPPORTED
	 * 
	 *	document.all.codProdotto.options.item(4)	// standard GET option method
	 * 	!!! document.all.codProdotto.options[5]		// NOT SUPPORTED
	 * 
	 * </script>
	 * 
	 * 
	 * 
	 * @param fieldModel
	 * @param webApp
	 * @param modelPropName
	 * @param dataModel
	 */
	public ComboTypeRenderer(FieldModel fieldModel, String webApp, String modelPropName, CommandDataModel dataModel) {
		super(fieldModel, webApp, modelPropName, dataModel);

		Matcher mat = null;
		try {
			String extraPar = getFieldModel().getExtraPar();

			LOG.debug("makeComboType(): extraPar=[" + extraPar + "]");

			mat = Patterns.size.matcher(extraPar);
			if (mat.find()) {
				setListSize(Integer.parseInt(mat.group(2)));
				getFieldModel().setExtraPar(
					extraPar.substring(0, mat.start()) + extraPar.substring(mat.end()));
				LOG.debug("makeComboType(): [size] parameter found -> rows=" + getListSize());
				extraPar = getFieldModel().getExtraPar();
			}

			mat = Patterns.width.matcher(extraPar);
			if (mat.find()) {
				setWidth(mat.group(2));
				getFieldModel().setExtraPar(
					extraPar.substring(0, mat.start()) + extraPar.substring(mat.end()));
				LOG.debug("makeComboType(): [width] parameter found -> width=" + getListSize());
				extraPar = getFieldModel().getExtraPar();
			}

			mat = Patterns.onchange.matcher(extraPar);
			if (mat.find()) {
				setOnChangeScript(mat.group(2));
				getFieldModel().setExtraPar(
					extraPar.substring(0, mat.start())
						+ extraPar.substring(mat.end()));
				LOG.debug("makeComboType(): [onchange] parameter found -> onChange='" + getOnChangeScript() + "'");
				extraPar = getFieldModel().getExtraPar();
			}

			mat = Patterns.showcode.matcher(extraPar);
			if (mat.find()) {
				setShowCode(Boolean.valueOf(mat.group(2)).booleanValue());
				getFieldModel().setExtraPar(
					extraPar.substring(0, mat.start())
						+ extraPar.substring(mat.end()));
				LOG.debug("makeComboType(): [showcode] parameter found -> showcode='" + isShowCode() + "'");
				extraPar = getFieldModel().getExtraPar();
			}

			if (!extraPar.equals("")) {
				setOtherPar(extraPar);
				LOG.debug("makeComboType(): OTHER parameter found -> " + getOtherPar());
			}
		}
		catch (RuntimeException e) {
			LOG.warning("makeComboType(): " + e);
		}
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getFieldRendering() {

		String rs = "";

		rs += "<DIV id='" + getFieldModel().getPropName() + "Combo'>\n";
		rs += "<INPUT type='hidden' id='"
			+ getFieldModel().getPropName()
			+ "' name='"
			+ getFieldModel().getPropName()
			+ "' value=\""
			+ getFieldModel().getPropValue()
			+ "\"";
		if(getFieldModel().isReadonly())
			rs += "wasReadonly=true ";
		rs += ">\n";
		if (!isAsList()) {
			rs += "<TABLE cellpadding='0' cellspacing='0'> <TR>\n";
			rs += "<TD align='right' ><INPUT type='text' nosubmit=true id='cField' class='"
				+ getFieldModel().getClassName()
				+ "'"
				+ (hasOtherPar() ? getOtherPar() : "")
				+ " readonly></TD>\n";
			rs += "<TD align='left' ><INPUT type='button' nosubmit=true id='cButton' value='&#9660' class='action' style='cursor: hand;' "
				+ (getFieldModel().getModality() == FieldsFactory.READ_MODALITY ? "disabled " : "")
				+ "></TD><TD id='"+getFieldModel().getPropName()+"Anchor' name='"+getFieldModel().getPropName()+"Anchor' style='width: 1px;'></TD>\n";
			rs += "</TR></TABLE>\n";
		}
		rs += "<DIV id='cPanel'>\n";
		rs += "<TABLE id='cTable' width='100%'>\n";
		rs += "</TABLE>\n";
		rs += "</DIV>\n";
		rs += "</DIV>\n";

		rs += "<SCRIPT>\n";

		rs += "var "
			+ getFieldModel().getPropName()
			+ "JSComboBox = new JSComboBox("
			+ getFieldModel().getPropName()
			+ "Combo, "
			+ getFieldModel().getPropName()
			+ "Combo.all."
			+ getFieldModel().getPropName()
			+ (hasOnChangeScript() ? ", '" + getOnChangeScript() + "');\n" : ");\n");

		if (hasWidth()) {
			rs += getFieldModel().getPropName() + "JSComboBox.setWidth('" + getWidth() + "');\n";
		}

		if (isAsList()) {
			rs += getFieldModel().getPropName() + "JSComboBox.setHeightByRows('" + getListSize() + "');\n";
		}

		HashMap codDescFields = getDataModel().getCodDescFields();
		CodDescDataList dataList;
		String daoCodDescName = (String) codDescFields.get(getModelPropName());
		if (daoCodDescName == null) {
			String msg = "makeComboType(): No binding defined for property [" + getModelPropName() + "]";
			LOG.warning(msg);
			rs += "<TR><TD>" + msg + "</TD></TR>\n";

		}
		else {
			dataList = getDataModel().getCodDescDataList(getModelPropName());
			if (dataList == null) {
				String msg =
					"makeComboType: Table ["
						+ daoCodDescName
						+ "] not loaded for property ["
						+ getModelPropName()
						+ "]";
				LOG.warning(msg);
				rs += "<TR><TD>" + msg + "</TD></TR>\n";
			}
			else {

				String options = "";

				if (!isAsList()) {
					options += getFieldModel().getPropName()
						+ "Combo.all."
						+ getFieldModel().getPropName()
						+ ".add(\"\", \""
						+ getDataModel().getCodDescEmptyValue()
						+ "\", true);\n";
				}

				for (int i = 0; i < dataList.getCodDescCount(); i++) {
					CodDescData data = dataList.getCodDesc(i);
					String code = data.getCod();
					String desc = data.getDescr();
					if (getFieldModel().getModality() == FieldsFactory.INSERT_MODALITY && !data.isValid())
						continue;
					String propValue = getFieldModel().getPropValue() == null ? "" : getFieldModel().getPropValue().toString();  
					if (getFieldModel().getModality() == FieldsFactory.UPDATE_MODALITY && !data.isValid() && !code.equals(propValue))
						continue;
					if(isShowCode())
					desc = code +" - " + desc;
					options += getFieldModel().getPropName()
						+ "Combo.all."
						+ getFieldModel().getPropName()
						+ ".add(\""
						+ code
						+ "\", \""
						+ desc
						+ "\""
						+ (data.isValid() ? ", true);\n" : ", false);\n");
				}
				rs += options;
			}
		}

		rs += "</SCRIPT>\n";


		return rs;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public boolean isAsList() {
		return getListSize() > 0;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public boolean hasWidth() {
		return (getWidth() != null) && !getWidth().equals("");
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public boolean hasOnChangeScript() {
		return (getOnChangeScript() != null) && !getOnChangeScript().equals("");
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public boolean hasOtherPar() {
		return (getOtherPar() != null) && !getOtherPar().equals("");
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getOnChangeScript() {
		return onChangeScript;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setOnChangeScript(String onChangeScript) {
		this.onChangeScript = onChangeScript;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public int getListSize() {
		return listSize;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setListSize(int listSize) {
		this.listSize = listSize;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getOtherPar() {
		return otherPar;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setOtherPar(String otherPar) {
		this.otherPar = otherPar;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getWidth() {
		return width;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setWidth(String width) {
		this.width = width;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public boolean isShowCode() {
		return showCode;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setShowCode(boolean showCode) {
		this.showCode = showCode;
	}

}
