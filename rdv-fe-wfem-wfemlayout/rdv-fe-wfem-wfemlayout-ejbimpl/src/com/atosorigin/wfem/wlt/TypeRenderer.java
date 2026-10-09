package com.atosorigin.wfem.wlt;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.atosorigin.wfem.coddesc.CodDescData;
import com.atosorigin.wfem.coddesc.CodDescDataList;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.controller.Configuration;
import com.atosorigin.wfem.dao.DAOAccessInfo;
import com.atosorigin.wfem.layout.FieldRenderer;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.FileType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;
import com.atosorigin.wfem.util.Tools;

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public class TypeRenderer extends FieldRenderer {
	
	private static final String FIELD_INDICATOR = "WLTFIELDINDICATORWLT";

	private PageRenderer 	pageRenderer;
	
	private Template 		template;
	private Template 		ffTemplate;
	private String 			propName;

	private AbstractType 	propValue;
	private	String 			label;
	private String 			errMsg;
    private String 			sReadonlyField = "";
    private String 			sReadonlyAttr = "";
    private boolean			readonly;
    private String			cellpadding = "";
    private String 			inputFieldContainerWidth = "";
    private String 			inputFieldStyleWidth = "";
    
	private TypePar			params;

	static class HtmlPatterns{
		static final Pattern style 				= PageRenderer.compilePattern("style");
		static final Pattern size 				= PageRenderer.compilePattern("size");
	}

	static class StylePatterns{
		static final Pattern width 				= PageRenderer.compileStylePattern("width");
	}
	
	static class EventsPatterns{
		static final Pattern onclick 			= PageRenderer.compilePattern("onclick");
		static final Pattern onchange 			= PageRenderer.compilePattern("onchange");		
	}
	
	static class TypePatterns{
		static final Pattern modality         	= PageRenderer.compilePattern("modality");
		static final Pattern labelposition 		= PageRenderer.compilePattern("labelposition");
		static final Pattern labelwidth 		= PageRenderer.compilePattern("labelwidth");
		static final Pattern fieldwidth 		= PageRenderer.compilePattern("fieldwidth");
		static final Pattern labelalign 		= PageRenderer.compilePattern("labelalign");
		static final Pattern fieldalign 		= PageRenderer.compilePattern("fieldalign");
		static final Pattern labelstyle 		= PageRenderer.compilePattern("labelstyle");
		static final Pattern labelcode 			= PageRenderer.compilePattern("labelcode");
		static final Pattern uppercase 			= PageRenderer.compilePattern("uppercase");
		static final Pattern border 			= PageRenderer.compilePattern("border");
	}
	
	static class CodDescPatterns{
		static final Pattern asradio    		= PageRenderer.compilePattern("asradio");
		static final Pattern emptylabel    		= PageRenderer.compilePattern("emptylabel");
		static final Pattern showcode    		= PageRenderer.compilePattern("showcode");
		static final Pattern showempty    		= PageRenderer.compilePattern("showempty");
	}
	
	static class StringPatterns{
		static final Pattern type 					= PageRenderer.compilePattern("type");  // 'all' for any chars - 'num' for numbers
		static final Pattern rows 					= PageRenderer.compilePattern("rows");
		static final Pattern asboolcombo 			= PageRenderer.compilePattern("asboolcombo");
		static final Pattern asboolcomboemptylabel  = PageRenderer.compilePattern("asboolcomboemptylabel");
		static final Pattern asboolradio 			= PageRenderer.compilePattern("asboolradio");
		static final Pattern password 				= PageRenderer.compilePattern("password");
	}

	static class DoublePatterns{
		static final Pattern scale    			= PageRenderer.compilePattern("scale");
		static final Pattern doublescale    	= PageRenderer.compilePattern("doublescale");
	}

	static class DatePatterns{
		static final Pattern showcalendar    	= PageRenderer.compilePattern("showcalendar");
	}

	static class TimestampPatterns{
		static final Pattern showcalendar    	= PageRenderer.compilePattern("showcalendar");
		static final Pattern nodate          	= PageRenderer.compilePattern("nodate");
		static final Pattern notime          	= PageRenderer.compilePattern("notime");
		static final Pattern noseconds    		= PageRenderer.compilePattern("noseconds");
	}
	
	static class BooleanPatterns{
		static final Pattern ascombo    		= PageRenderer.compilePattern("ascombo");
		static final Pattern asradio    		= PageRenderer.compilePattern("asradio");
	}
	
	static class FilePatterns{
		static final Pattern clearlabel    		= PageRenderer.compilePattern("clearlabel");
		static final Pattern previewlabel    	= PageRenderer.compilePattern("previewlabel");
		static final Pattern showlabel    		= PageRenderer.compilePattern("showlabel");
		static final Pattern showclear    		= PageRenderer.compilePattern("showclear");
		static final Pattern showpreview    	= PageRenderer.compilePattern("showpreview");
		static final Pattern showview    		= PageRenderer.compilePattern("showview");
		static final Pattern hidefilename  		= PageRenderer.compilePattern("hidefilename");
		static final Pattern browselabel    	= PageRenderer.compilePattern("browselabel");
		static final Pattern maxdim	    		= PageRenderer.compilePattern("maxdim");
	}
	
    /**************************************************************************************************/
	/**************************************************************************************************/
	class CodDescOption{
		String cod;
		String desc;
		boolean selected;
		boolean valid;
		boolean isempty;
		 
		CodDescOption(String cod, String desc, boolean selected, boolean valid, boolean isempty){
			this.cod = cod;
			this.desc = desc;
			this.selected = selected;
			this.valid = valid;
			this.isempty = isempty;
		}
	}
	
    /**************************************************************************************************/
	/**************************************************************************************************/
	class TypePar{
		
		int	   modality;
		int    labelPosition;
		String labelwidth;
		String fieldwidth;
		String labelalign;
		String fieldalign;
		String labelstyle;
		boolean uppercase;
		String border;
		
	    /**************************************************************************************************/
		/**************************************************************************************************/
		public TypePar(){
			
			modality = template.getModality();
			String par = pageRenderer.getPar(TypePatterns.modality,"").asString();
			if(par.equalsIgnoreCase("insert"))
				modality = Template.INSERT_MODALITY;
			else if(par.equalsIgnoreCase("update"))
				modality = Template.UPDATE_MODALITY;
			else if(par.equalsIgnoreCase("read"))
				modality = Template.READ_MODALITY;
			else if(par.equalsIgnoreCase("print"))
				modality = Template.PRINT_MODALITY;
			
			labelPosition = template.getLabelPosition();
			par = pageRenderer.getPar(TypePatterns.labelposition,"").asString();
			if(par.equalsIgnoreCase("nolabel"))
				labelPosition = Template.NO_LABEL;
			else if(par.equalsIgnoreCase("left"))
				labelPosition = Template.LEFT_LABEL;
			else if(par.equalsIgnoreCase("up"))
				labelPosition = Template.UP_LABEL;
			else if(par.equalsIgnoreCase("right"))
				labelPosition = Template.RIGHT_LABEL;
			else if(par.equalsIgnoreCase("down"))
				labelPosition = Template.DOWN_LABEL;
			
			labelwidth = template.getLabelWidth();
			par = pageRenderer.getPar(TypePatterns.labelwidth,"").asString();
			if(par.length() > 0)
				labelwidth = par.toString();
			try{
				if(Integer.parseInt(PageRenderer.getNum(labelwidth)) == 0)
					labelPosition = Template.NO_LABEL;
			}catch (Exception e) {}
			
			fieldwidth = template.getFieldWidth();
			par = pageRenderer.getPar(TypePatterns.fieldwidth,"").asString();
			if(par.length() > 0)
				fieldwidth = par.toString();

			labelalign = template.getLabelAlign();
			par = pageRenderer.getPar(TypePatterns.labelalign,"").asString();
			if(par.length() > 0)
				labelalign = par.toString();
			
			fieldalign = template.getFieldAlign();
			par = pageRenderer.getPar(TypePatterns.fieldalign,"").asString();
			if(par.length() > 0)
				fieldalign = par.toString();

			labelstyle = template.getLabelStyle();
			par = pageRenderer.getPar(TypePatterns.labelstyle,"").asString();
			if(par.length() > 0)
				labelstyle = par.toString();
			
			uppercase = template.getUpperCase();
			par = pageRenderer.getPar(TypePatterns.uppercase,"").asString();
			if(par.length() > 0)
				uppercase = Boolean.parseBoolean(par.toString());
			
			border = template.getBorder();
			par = pageRenderer.getPar(TypePatterns.border,"").asString();
			if(par.length() > 0)
				border = par.toString();
			
			par = pageRenderer.getPar(HtmlPatterns.style,"",false).asString();
			if(par.length() > 0){
				Matcher mat = StylePatterns.width.matcher(par);
				if(mat.find()){
					inputFieldContainerWidth = " width='"+mat.group(1)+"' ";
					inputFieldStyleWidth = "width:"+mat.group(1)+";";
				}
			}
		}
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public TypeRenderer(PageRenderer pageRenderer) throws Exception{
		
		this.pageRenderer = pageRenderer;
		this.propName = pageRenderer.getPropName();
		this.template = pageRenderer.getTemplate();
		this.ffTemplate = pageRenderer.getFfTemplate();

		Class propType = Tools.getPropertyType(template.getPageDataModel(),propName);
		if(propType == null){
			errMsg = "Field type for property ["+propName+"] is null";
			return;
		}

		try{			
			propValue = (AbstractType)Tools.getPropertyValue(template.getPageDataModel(),propName);
		}catch(ClassCastException cce){
			errMsg = "Field ["+propName+"] is not an AbstractType";
			return;
		}
		
		if(propValue == null){
			errMsg = "Field ["+propName+"] is null";
			return;
		}
		
		String labelcode = template.getPageName() + propName;
		if(template.getLabelCodePrefix() != null && !template.getLabelCodePrefix().equals("")){
			String lastPropName = propName.toString();
			if(propName.lastIndexOf("_") >= 0)
				lastPropName = propName.substring((propName.lastIndexOf("_")+1));
			labelcode = template.getLabelCodePrefix() + lastPropName;
		}else{
			String par = pageRenderer.getPar(TypePatterns.labelcode,"").asString();
			if(par.length() > 0)
				labelcode = par;			
		}
		label = template.getProperty(labelcode);
		label = Tools.convertSpecialChars(label);

		// Load (and remove) all other type parameters
		params = new TypePar();
		
		if((!propValue.hasTypeErrors() && propValue.hasTypeWarnings()) || propValue.isSkippable()){
		    if(params.modality == Template.READ_MODALITY)
		    	sReadonlyField = " tabindex=-1 ";
			params.modality = Template.READ_MODALITY;
		}
		
		if(params.modality == Template.READ_MODALITY){
			sReadonlyField += " isreadonly='true' ";
			sReadonlyAttr = " readonly ";
			readonly = true;
		}
		
		if(template.isNoPadding() || template.isInGrid() || params.labelPosition == Template.UP_LABEL || params.labelPosition == Template.DOWN_LABEL)
			cellpadding = " cellpadding='0' cellspacing='0' ";		
	}		
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getCalc() throws Exception {
		return getPdfCalc();
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getPdf() throws Exception {
		return getPdfCalc();
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getPdfCalc(){
		
		if(errMsg != null)
			return "<table><tr><td>"+errMsg+"</td></tr></table>";
		
		String fieldHtmlSchema = createFieldHtmlShema();
		String fieldValue = propValue.toString();
		if(propValue instanceof BooleanType){
		    if(((BooleanType)propValue).booleanValue())
		        fieldValue = ffTemplate.getProperty("BooleanType.true");
		    else
		    	fieldValue = ffTemplate.getProperty("BooleanType.false");
		}else{
			String codDescValue = getDescValue(propName);
			if(codDescValue != null)
				fieldValue = codDescValue;
		}
		return putFieldInSchema(fieldHtmlSchema,Tools.stringToHTMLString(fieldValue));
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getHtml() throws Exception {
		
		if(params.modality == Template.PRINT_MODALITY){
			return getPdf();
		}

		if(errMsg != null)
			return "<table><tr><td>"+errMsg+"</td></tr></table>";
		
		StringBuffer result = new StringBuffer();
		
		result.append("<div id='"+propName+"Field' name='"+propName+"Field' style='margin:0;width:100%;'><center>");

		String fieldHtmlSchema = createFieldHtmlShema();
		Vector codDescOptions = getCodDescOptions(propName);
		if(codDescOptions != null){
			fieldHtmlSchema = putFieldInSchema(fieldHtmlSchema,manageCodDescType(propValue,codDescOptions));
		}else{
			if(propValue instanceof StringType){
				
				if(template.isInGrid())
					fieldHtmlSchema = manageStringType((StringType)propValue);
				else
					fieldHtmlSchema = putFieldInSchema(fieldHtmlSchema,manageStringType((StringType)propValue));
			
			}else if(propValue instanceof IntegerType){
				
				if(template.isInGrid())
					fieldHtmlSchema = manageIntegerType((IntegerType)propValue);
				else
					fieldHtmlSchema = putFieldInSchema(fieldHtmlSchema,manageIntegerType((IntegerType)propValue));
			
			}else if(propValue instanceof DoubleType){
				
				if(template.isInGrid())
					fieldHtmlSchema = manageDoubleType((DoubleType)propValue);
				else
					fieldHtmlSchema = putFieldInSchema(fieldHtmlSchema,manageDoubleType((DoubleType)propValue));
			
			}else if(propValue instanceof DateType){
				
				if(template.isInGrid())
					fieldHtmlSchema = manageDateType((DateType)propValue);
				else
					fieldHtmlSchema = putFieldInSchema(fieldHtmlSchema,manageDateType((DateType)propValue));
			
			}else if(propValue instanceof TimestampType){
				
				if(template.isInGrid())
					fieldHtmlSchema = manageTimestampType((TimestampType)propValue);
				else
					fieldHtmlSchema = putFieldInSchema(fieldHtmlSchema,manageTimestampType((TimestampType)propValue));
			
			}else if(propValue instanceof BooleanType){
				
					if(template.isInGrid())
						fieldHtmlSchema = manageBooleanType((BooleanType)propValue);
					else
						fieldHtmlSchema = putFieldInSchema(fieldHtmlSchema,manageBooleanType((BooleanType)propValue));
			
			}else if(propValue instanceof FileType){
				
				if(template.isInGrid())
					fieldHtmlSchema = manageFileType((FileType)propValue);
				else
					fieldHtmlSchema = putFieldInSchema(fieldHtmlSchema,manageFileType((FileType)propValue));
			
			}
		}
		result.append(fieldHtmlSchema);
		
		result.append("</center></div>");

		return result.toString();
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private String putFieldInSchema(String htmlSchema, String htmlField){
		String result = htmlSchema.substring(0,htmlSchema.indexOf(FIELD_INDICATOR));
		result += htmlField;
		result += htmlSchema.substring(htmlSchema.indexOf(FIELD_INDICATOR)+FIELD_INDICATOR.length());
		return result;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String createFieldHtmlShema(){

		StringBuffer result = new StringBuffer();

		String lwidth = (params.labelwidth == null || params.labelwidth.length() == 0) ? "" : "width='"+params.labelwidth+"' "; 
		String lalign = (params.labelalign == null || params.labelalign.length() == 0) ? "" : "align='"+params.labelalign+"' "; 
		String fwidth = (params.fieldwidth == null || params.fieldwidth.length() == 0) ? "" : "width='"+params.fieldwidth+"' "; 
		String falign = (params.fieldalign == null || params.fieldalign.length() == 0) ? "" : "align='"+params.fieldalign+"' "; 

		String lstyle = (params.labelstyle == null || params.labelstyle.length() == 0) ? "" : "style='"+params.labelstyle+"' "; 
		String updownStyle = "white-space:nowrap;padding-left:3;";
		if(params.labelPosition == Template.UP_LABEL || params.labelPosition == Template.DOWN_LABEL)
			lstyle = (params.labelstyle == null || params.labelstyle.length() == 0) ? "style='"+updownStyle+"'" : "style='"+updownStyle+params.labelstyle+"' "; 
		
		result.append("<table width='100%' border='"+params.border+"' cellpadding='0' cellspacing='0'>");
		switch(params.labelPosition){
			case Template.NO_LABEL:
				result.append("<tr><td id='"+propName+"Label' style='display:none;'></td><td "+fwidth+falign+">"+FIELD_INDICATOR+"</td></tr>");
				break;
			case Template.LEFT_LABEL:
				if(lalign.length() == 0)
					lalign = "align='right' ";
				result.append("<tr><td class='text' id='"+propName+"Label' "+lwidth+lalign+lstyle+">"+label+"</td><td "+fwidth+falign+">"+FIELD_INDICATOR+"</td></tr>");
				break;
			case Template.RIGHT_LABEL:
				if(falign.length() == 0)
					falign = "align='right' ";
				result.append("<tr><td "+fwidth+falign+">"+FIELD_INDICATOR+"</td><td  id='"+propName+"Label' class='text' "+lwidth+lalign+lstyle+">"+label+"</td></tr>");
				break;
			case Template.UP_LABEL:
				result.append("<tr><td id='"+propName+"Label' class='text' "+lalign+lstyle+">"+label+"</td></tr><tr><td "+falign+">"+FIELD_INDICATOR+"</td></tr>");
				break;
			case Template.DOWN_LABEL:
				result.append("<tr><td "+falign+">"+FIELD_INDICATOR+"</td></tr><tr><td id='"+propName+"Label' class='text' "+lalign+lstyle+">"+label+"</td></tr>");
				break;
			default:
				result.append("<tr><td>labelposition: wrong value</td></tr>");
				break;
		}
		
		result.append("</table>");
		
		return result.toString();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String htmlFieldTableContainer(){
		if(template.isInGrid())
			return "<table "+cellpadding+inputFieldContainerWidth+" style='table-layout:fixed;'><tr>";
		else
			return "<table "+cellpadding+inputFieldContainerWidth+"><tr>";			
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String manageCodDescType(AbstractType propValue, Vector codDescOptions){

		String disabled="";
		if(readonly)
			disabled = "disabled='disabled' ";
		String ftype = propValue.getClass().getName().substring(propValue.getClass().getName().lastIndexOf(".")+1);
		
    	boolean asradio = pageRenderer.getPar(CodDescPatterns.asradio,"false").asBoolean();

    	StringBuffer res = new StringBuffer();
	    res.append(htmlFieldTableContainer());
	    res.append(	 "<td>");
        res.append(pageRenderer.inlineAnchor(propName,propValue));
   		res.append(pageRenderer.inlineMsgAnchor(propName, propValue));
		
    	String apice = "";
    	String userEvent = "";

    	if(asradio){
	    	
	    	ParamValue onclickPar = pageRenderer.getPar(EventsPatterns.onclick,"");
	    	apice = onclickPar.apice();
	    	userEvent = onclickPar.asString();
	    	res.append("<table>");
		    for(int i=0;i<codDescOptions.size();i++){
		    	res.append("<tr><td>");
		    	CodDescOption option = (CodDescOption)codDescOptions.get(i);
			    res.append("<input type='radio' name='"+propName+"' id='"+propName+"' ftype='"+ftype+"'");
		    	if(option.isempty)
					res.append("style='display:none;' value='' ");
		    	else
					res.append("value='"+option.cod+"' ");
				if(option.selected)
					res.append(" checked ");		
				if(readonly)
					res.append(" disabled='disabled' ");
				res.append(			sReadonlyField+pageRenderer.getPars()+">");
		    	if(!option.isempty)
		    		res.append("<span class='text'>"+option.desc+"</span>&nbsp;");
		    	res.append("</td></tr>");
		    }
	    	res.append("</table>");
	    	
	    }else{
	    	
	    	boolean showempty = pageRenderer.getPar(CodDescPatterns.showempty,"true").asBoolean();
	    	int size = pageRenderer.getPar(HtmlPatterns.size,"0").asInt();
	    	
	    	ParamValue onchangePar = pageRenderer.getPar(EventsPatterns.onchange,"");
	    	apice = onchangePar.apice();
	    	userEvent = onchangePar.asString();
	    	
		    res.append(	"<div class='styledCombo'>");
		    res.append(		"<select ftype='"+ftype+"' class='inputField' name='"+propName+"' id='"+propName+"' "+disabled+" ");
		    res.append(			sReadonlyField+pageRenderer.getPars()+" "+(size>1?"size="+size:"")+">");
		    for(int i=0;i<codDescOptions.size();i++){
		    	CodDescOption option = (CodDescOption)codDescOptions.get(i);
		    	if(option.isempty && (!showempty || size > 0))
		    		continue;
			    res.append("<option value=\""+option.cod+"\" "+(option.selected?"selected":"")+" "+(option.valid?"":"style='color:red;'")+">");
			    res.append(         option.desc+"</option>");
		    }
			res.append(		"</select>");
		    res.append(	"</div>");
	    }
		
	    res.append(	 "</td>");
   		res.append("</tr></table>");
   		if(userEvent.length() > 0)
   			res.append("<script>__fieldIntf.initCodDescType('"+propName+"',"+apice+userEvent+apice+");</script>");
   		else
   			res.append("<script>__fieldIntf.initCodDescType('"+propName+"',null);</script>");
	    return res.toString();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String manageStringType(StringType propValue){

		String chars = pageRenderer.getPar(StringPatterns.type,"").asString();
    	int rows = pageRenderer.getPar(StringPatterns.rows,"0").asInt();
    	boolean asboolcombo = pageRenderer.getPar(StringPatterns.asboolcombo,"false").asBoolean();
    	boolean asboolradio = pageRenderer.getPar(StringPatterns.asboolradio,"false").asBoolean();
    	boolean password = pageRenderer.getPar(StringPatterns.password,"false").asBoolean();
    	
		StringBuffer res = new StringBuffer();
	    res.append(htmlFieldTableContainer());
	    res.append(	 "<td>");
        res.append(pageRenderer.inlineAnchor(propName,propValue));
   		res.append(pageRenderer.inlineMsgAnchor(propName, propValue));
	    
    	String apice = "";
    	String userEvent = "";

	    if(asboolcombo){
	    	
	    	String emptylabel = pageRenderer.getPar(StringPatterns.asboolcomboemptylabel,template.getPageDataModel().getCodDescEmptyValue()).asString();
	    	
	    	ParamValue onchangePar = pageRenderer.getPar(EventsPatterns.onchange,"");
	    	apice = onchangePar.apice();
	    	userEvent = onchangePar.asString();
	    	
			String yes = ffTemplate.getProperty("BooleanType.true");
			String no = ffTemplate.getProperty("BooleanType.false");
			
			String disabled="";
			if(readonly)
				disabled = "disabled='disabled' ";
				
			res.append("<select ftype='StringType' class='inputField' name='"+propName+"' id='"+propName+"' "+disabled+">");
		    res.append(			sReadonlyField+pageRenderer.getPars()+">");
			if(propValue.isNull()){
		        res.append("<option selected value=\"\">"+emptylabel+"</option>");
		        res.append("<option value=\""+DAOAccessInfo.getBooleanFalseValue()+"\">"+no+"</option>");
		        res.append("<option value=\""+DAOAccessInfo.getBooleanTrueValue()+"\">"+yes+"</option>");
			}else if(propValue.equalsIgnoreCase("S")){
		        res.append("<option value=\"\">"+emptylabel+"</option>");
		        res.append("<option value=\""+DAOAccessInfo.getBooleanFalseValue()+"\">"+no+"</option>");
		        res.append("<option selected value=\""+DAOAccessInfo.getBooleanTrueValue()+"\">"+yes+"</option>");
			}else{
		        res.append("<option value=\"\">"+emptylabel+"</option>");
		        res.append("<option selected value=\""+DAOAccessInfo.getBooleanFalseValue()+"\">"+no+"</option>");				
		        res.append("<option value=\""+DAOAccessInfo.getBooleanTrueValue()+"\">"+yes+"</option>");
			}
			res.append("</select>");
	    	
	    }else if(asboolradio){
	    	
			String yes = ffTemplate.getProperty("BooleanType.true");
			String no = ffTemplate.getProperty("BooleanType.false");

			ParamValue onclickPar = pageRenderer.getPar(EventsPatterns.onclick,"");
	    	apice = onclickPar.apice();
	    	userEvent = onclickPar.asString();
	    	res.append("<table><tr><td>");
		    res.append("<input type='radio' name='"+propName+"' id='"+propName+"' ftype='StringType' value='' "+(propValue.isNull()?"checked":"")+" "+
		    					" "+(readonly?" disabled='disabled' ":"")+" style='display:none;' "+sReadonlyField+pageRenderer.getPars()+">");
		    res.append("</td><td><input type='radio' name='"+propName+"' id='"+propName+"' ftype='StringType' value='N' "+(!propValue.equalsIgnoreCase("S")?"checked":"")+" "+
		    					" "+(readonly?" disabled='disabled' ":"")+sReadonlyField+pageRenderer.getPars()+">"+
		    					"<span class='text'>"+no+"</span>&nbsp;");
		    res.append("</td><td><input type='radio' name='"+propName+"' id='"+propName+"' ftype='StringType' value='S' "+(propValue.equalsIgnoreCase("S")?"checked":"")+" "+
		    					" "+(readonly?" disabled='disabled' ":"")+sReadonlyField+pageRenderer.getPars()+">"+
		    					"<span class='text'>"+yes+"</span>&nbsp;");
	    	res.append("</td></tr></table>");
	    	
	    }else{
	    	
	    	if(rows > 0){
	    			    		
		    	String myOnchange = "";
	    		if(params.uppercase){
			    	ParamValue onchangePar = pageRenderer.getPar(EventsPatterns.onchange,"");
			    	apice = onchangePar.apice();
			    	userEvent = onchangePar.asString();
			    	myOnchange = " onchange="+apice+"__fieldIntf.toUpper(this);"+userEvent+apice+" ";
	    		}
				res.append("<textarea class='"+getClassName()+"' rows='"+rows+"' "+(chars.length()>0?"okchars='"+chars+"'":"")+" ");
				res.append(			"name='"+propName+"' id='"+propName+"' ftype='StringType' "+(params.uppercase?"":"upper='false'")+" ");
			    res.append(			sReadonlyField+sReadonlyAttr+myOnchange+pageRenderer.getPars()+(Configuration.getInstance().isAllowCopyOnDisabledInputField()?" enabcopy ":"")+">");
			    res.append(			Tools.stringToHTMLString(propValue.toString())+"</textarea>");
			    
	    	}else{
	    		
		    	String myOnchange = "";
	    		if(params.uppercase){
			    	ParamValue onchangePar = pageRenderer.getPar(EventsPatterns.onchange,"");
			    	apice = onchangePar.apice();
			    	userEvent = onchangePar.asString();
			    	myOnchange = " onchange="+apice+"__fieldIntf.toUpper(this);"+userEvent+apice+" ";
	    		}
				res.append("<input type='"+(password?"password":"text")+"' value=\""+Tools.stringToHTMLString(propValue.toString())+"\" class='"+getClassName()+"' "+(chars.length()>0?"okchars='"+chars+"'":"")+" ");
				res.append(			"name='"+propName+"' id='"+propName+"' ftype='StringType' "+(params.uppercase?"":"upper='false'")+" ");
			    res.append(			sReadonlyField+sReadonlyAttr+myOnchange+pageRenderer.getPars()+(Configuration.getInstance().isAllowCopyOnDisabledInputField()?" enabcopy ":"")+">");
			    
	    	}
		    
	    }
	    
	    res.append(	 "</td>");
   		res.append("</tr></table>");
   		if(userEvent.length() > 0)
   			res.append("<script>__fieldIntf.initStringType('"+propName+"',"+apice+userEvent+apice+");</script>");
   		else
   			res.append("<script>__fieldIntf.initStringType('"+propName+"',null);</script>");
	    return res.toString();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String manageIntegerType(IntegerType propValue){

		StringBuffer res = new StringBuffer();
	    res.append(htmlFieldTableContainer());
	    res.append(	 "<td>");
        res.append(pageRenderer.inlineAnchor(propName,propValue));
   		res.append(pageRenderer.inlineMsgAnchor(propName, propValue));
		res.append(		"<input type='text' value='"+propValue+"' class='"+getClassName()+" numberField' ");
		res.append(				"name='"+propName+"' id='"+propName+"' ftype='IntegerType' ");
	    res.append(				sReadonlyField+sReadonlyAttr+pageRenderer.getPars()+(Configuration.getInstance().isAllowCopyOnDisabledInputField()?" enabcopy ":"")+">");
	    res.append(	 "</td>");
   		res.append("</tr></table>");
	    res.append("<script>__fieldIntf.initIntegerType('"+propName+"');</script>");
	    return res.toString();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String manageDoubleType(DoubleType propValue){

		String doubleValue = propValue.toString();
	    int scale = template.getDoubleScale();
	    if(scale < 0)
	    	scale = pageRenderer.getPar(DoublePatterns.scale,"-1").asInt();
		    if(scale < 0)
		    	scale = pageRenderer.getPar(DoublePatterns.doublescale,"-1").asInt();
	    if(scale < 0)
	    	scale = 2;
	    else
	    	doubleValue = propValue.toScaledString(scale);
	    
		StringBuffer res = new StringBuffer();
	    res.append(htmlFieldTableContainer());
	    res.append(	 "<td>");
        res.append(pageRenderer.inlineAnchor(propName,propValue));
   		res.append(pageRenderer.inlineMsgAnchor(propName,propValue));
		res.append(		"<input type='text' value='"+doubleValue+"' class='"+getClassName()+" numberField' ");
		res.append(				"name='"+propName+"' id='"+propName+"' ftype='DoubleType' scale='"+scale+"' ");
	    res.append(				sReadonlyField+sReadonlyAttr+pageRenderer.getPars()+(Configuration.getInstance().isAllowCopyOnDisabledInputField()?" enabcopy ":"")+">");
	    res.append(	 "</td>");
   		res.append("</tr></table>");
	    res.append("<script>__fieldIntf.initDoubleType('"+propName+"');</script>");
	    return res.toString();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String manageDateType(DateType propValue){

		String size = "";
		if(inputFieldContainerWidth.equals("")){
			size = " size='10' ";
			int isize = pageRenderer.getPar(HtmlPatterns.size,"0").asInt();
			if(isize > 0)
				size = " size='"+isize+"' ";
		}
    	boolean showcalendar = pageRenderer.getPar(DatePatterns.showcalendar,"true").asBoolean();
		
		StringBuffer res = new StringBuffer();

	    res.append(htmlFieldTableContainer());
	    res.append(	 "<td>");
	    if(!template.isInGrid())
	    	res.append(pageRenderer.inlineAnchor(propName,propValue));
   		res.append(pageRenderer.inlineMsgAnchor(propName, propValue));
   		
   		res.append(		"<div style='position:relative;'>");
		res.append(			"<input id='"+propName+"Mask' tabindex=-1 type='text' class='"+getClassName()+"' readonly='readonly' "+size+" "+(inputFieldStyleWidth.equals("")?"":"style='"+inputFieldStyleWidth+"'")+"></input>");
		res.append(			"<div style='position:absolute;left:0;top:0;"+inputFieldStyleWidth+"'>");
	    res.append(				"<input type='text' maxlength='10' value='"+propValue+"' class='"+getClassName()+"' style='background:transparent;"+inputFieldStyleWidth+"' ");
	    res.append(						"name='"+propName+"' id='"+propName+"' ftype='DateType'"+size);
	    res.append(						sReadonlyField+sReadonlyAttr+pageRenderer.getPars()+(Configuration.getInstance().isAllowCopyOnDisabledInputField()?" enabcopy ":"")+">");
		res.append(			"</div>");
		res.append(		"</div>");
	    
	    res.append(  "</td>");
	    if(showcalendar){
		    res.append(  "<td width='18px'>");
		    res.append(		"<img id='"+propName+"ImgCal' src='"+template.getWfemLayoutWebApp()+"/private/calendar/images/openCalendar.png' "+
		    					 "style='cursor:pointer;"+(readonly?"visibility:hidden;":"")+"'>");
		    res.append(  "</td>");
	    }
	    if(template.isInGrid())
	    	res.append(pageRenderer.inlineAnchor(propName,propValue));
   		res.append("</tr></table>");
	    res.append("<script>__fieldIntf.initDateType('"+propName+"');</script>");
	    return res.toString();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String manageTimestampType(TimestampType propValue){

    	boolean showcalendar = pageRenderer.getPar(TimestampPatterns.showcalendar,"true").asBoolean();
    	boolean nodate = pageRenderer.getPar(TimestampPatterns.nodate,"false").asBoolean();
    	boolean notime = pageRenderer.getPar(TimestampPatterns.notime,"false").asBoolean();
    	boolean noseconds = pageRenderer.getPar(TimestampPatterns.noseconds,"false").asBoolean();
    	if(nodate)
    		showcalendar = false;
    	
		String size = "";
		if(inputFieldContainerWidth.equals("")){
			int isize = pageRenderer.getPar(HtmlPatterns.size,"0").asInt();
			if(isize == 0){ // Not specified. Manage defaults
				if(notime){
					isize=10;
				}else if(nodate){
					if(noseconds)
						isize=3;
					else
						isize=5;
				}else if(noseconds){
					isize = 15;
				}else{
					isize=17;
				}
			}
			size = " size='"+isize+"' ";
		}
    	
    	String value = "";
    	if(!propValue.isNull()){
    		if(notime){
    		    value = propValue.getGG() + "-" + propValue.getMM() + "-" + propValue.getAA();
    		}else if(nodate){
    			if(noseconds)
        		    value = propValue.getHH()+":"+propValue.getMI();
    			else
        		    value = propValue.getHH()+":"+propValue.getMI()+":"+propValue.getSS();
    		}else if(noseconds){
    		    value = propValue.getGG()+"-"+propValue.getMM()+"-"+propValue.getAA()+" "+propValue.getHH()+":"+propValue.getMI();
    		}else{
    		    value = propValue.getGG()+"-"+propValue.getMM()+"-"+propValue.getAA()+" "+propValue.getHH()+":"+propValue.getMI()+":"+propValue.getSS();
    		}
    	}
		StringBuffer res = new StringBuffer();

	    res.append(htmlFieldTableContainer());
	    res.append(	 "<td>");
	    if(!template.isInGrid())
	    	res.append(pageRenderer.inlineAnchor(propName,propValue));
   		res.append(pageRenderer.inlineMsgAnchor(propName, propValue));
   		res.append(		"<div style='position:relative;'>");
		res.append(			"<input id='"+propName+"Mask' tabindex=-1 type='text' class='"+getClassName()+"' readonly='readonly' "+size+" "+(inputFieldStyleWidth.equals("")?"":"style='"+inputFieldStyleWidth+"'")+"></input>");
		res.append(			"<div style='position:absolute;left:0;top:0;"+inputFieldStyleWidth+"'>");
	    res.append(				"<input type='text' maxlength='19' value='"+value+"' class='"+getClassName()+"' style='background:transparent;"+inputFieldStyleWidth+"' ");
	    res.append(						"name='"+propName+"' id='"+propName+"' ftype='TimestampType' "+size+" ");
	    res.append(						(nodate?"nodate='true'":"")+" "+(notime?"notime='true'":"")+" "+(noseconds?"noseconds='true'":""));
	    res.append(						sReadonlyField+sReadonlyAttr+pageRenderer.getPars()+(Configuration.getInstance().isAllowCopyOnDisabledInputField()?" enabcopy ":"")+">");
		res.append(			"</div>");
		res.append(		"</div>");
	    res.append(  "</td>");
	    if(showcalendar){
		    res.append(  "<td width='18px'>");
		    res.append(		"<img id='"+propName+"ImgCal' src='"+template.getWfemLayoutWebApp()+"/private/calendar/images/openCalendar.png' "+
		    					 "style='cursor:pointer;"+(readonly?"visibility:hidden;":"")+"'>");
		    res.append(  "</td>");
	    }
	    if(template.isInGrid())
	    	res.append(pageRenderer.inlineAnchor(propName,propValue));
   		res.append("</tr></table>");
	    res.append("<script>__fieldIntf.initTimestampType('"+propName+"');</script>");
	    return res.toString();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String manageBooleanType(BooleanType propValue){

    	boolean asradio = false;
    	boolean ascombo = pageRenderer.getPar(BooleanPatterns.ascombo,"false").asBoolean();
    	if(!ascombo)
        	asradio = pageRenderer.getPar(BooleanPatterns.asradio,"false").asBoolean();

		StringBuffer res = new StringBuffer();
	    res.append(htmlFieldTableContainer());
	    res.append(	 "<td>");
        res.append(pageRenderer.inlineAnchor(propName,propValue));
   		res.append(pageRenderer.inlineMsgAnchor(propName, propValue));
	    
    	String apice = "";
    	String userEvent = "";

    	String htmTypeName = propName.toString();
    	
   		if(ascombo){
	    	
	    	ParamValue onchangePar = pageRenderer.getPar(EventsPatterns.onchange,"");
	    	apice = onchangePar.apice();
	    	userEvent = onchangePar.asString();
	    	
			String yes = ffTemplate.getProperty("BooleanType.true");
			String no = ffTemplate.getProperty("BooleanType.false");
			
			String disabled="";
			if(readonly)
				disabled = "disabled='disabled' ";
				
			res.append("<select ftype='BooleanType' class='inputField' name='"+propName+"' id='"+propName+"' "+disabled+">");
		    res.append(			sReadonlyField+pageRenderer.getPars()+">");
			if(propValue.booleanValue()){
		        res.append("<option value=\"false\">"+no+"</option>");
		        res.append("<option selected value=\"true\">"+yes+"</option>");
			}else{
		        res.append("<option selected value=\"false\">"+no+"</option>");				
		        res.append("<option value=\"true\">"+yes+"</option>");
			}
			res.append("</select>");
	    	
	    }else if(asradio){
	    	
	    	ParamValue onclickPar = pageRenderer.getPar(EventsPatterns.onclick,"");
	    	apice = onclickPar.apice();
	    	userEvent = onclickPar.asString();
	    	
			String yes = ffTemplate.getProperty("BooleanType.true");
			String no = ffTemplate.getProperty("BooleanType.false");
	    	
			res.append("<div>");
			res.append(    "<input type='radio' name='"+propName+"' id='"+propName+"' ftype='BooleanType' value='false' ");
			if(propValue != null && !propValue.booleanValue())
				res.append(" checked ");		
			if(readonly)
				res.append(" disabled='disabled' ");
			res.append(			sReadonlyField+pageRenderer.getPars()+"><span class='text'>"+no+"</span>&nbsp;");
			
			res.append(    "<input type='radio' name='"+propName+"' id='"+propName+"' ftype='BooleanType' value='true' ");
			if(propValue != null && propValue.booleanValue())
				res.append(" checked ");		
			if(readonly)
				res.append(" disabled='disabled' ");
			res.append(			sReadonlyField+pageRenderer.getPars()+"><span class='text'>"+yes+"</span>");
			res.append("</div>");
			
	    }else{
	    	
	    	ParamValue onclickPar = pageRenderer.getPar(EventsPatterns.onclick,"");
	    	apice = onclickPar.apice();
	    	userEvent = onclickPar.asString();
	    	
			res.append(    "<input type='checkbox' name='"+propName+"Check' id='"+propName+"Check' ftype='BooleanType' ");
			if(propValue != null && propValue.booleanValue())
				res.append(" checked ");		
			if(readonly)
				res.append(" disabled ");
			res.append(			sReadonlyField+pageRenderer.getPars()+">");
			res.append(    "<input type='hidden' name='"+propName+"' id='"+propName+"' value='"+propValue+"' ftype='BooleanType' "+sReadonlyField+">");		
		
			htmTypeName = htmTypeName+"Check";
	    }
	    
	    res.append(	 "</td>");
   		res.append("</tr></table>");
   		if(userEvent.length() > 0)
   			res.append("<script>__fieldIntf.initBooleanType('"+htmTypeName+"',"+apice+userEvent+apice+");</script>");
   		else
   			res.append("<script>__fieldIntf.initBooleanType('"+htmTypeName+"',null);</script>");
	    return res.toString();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String manageFileType(FileType propValue){
		
		String size = "";
		int isize = pageRenderer.getPar(HtmlPatterns.size,"0").asInt();
		if(isize > 0)
			size = "style='width:"+isize+";'";
		String clearlabel = pageRenderer.getPar(FilePatterns.clearlabel,ffTemplate.getProperty("FileType.clear")).asString();
		String previewlabel = pageRenderer.getPar(FilePatterns.previewlabel,ffTemplate.getProperty("FileType.preview")).asString();
		String showlabel = pageRenderer.getPar(FilePatterns.showlabel,ffTemplate.getProperty("FileType.show")).asString();
		String browseLabel = pageRenderer.getPar(FilePatterns.browselabel,ffTemplate.getProperty("FileType.browse")).asString();
		
		boolean showclear = pageRenderer.getPar(FilePatterns.showclear,"false").asBoolean();
		boolean showpreview = pageRenderer.getPar(FilePatterns.showpreview,"false").asBoolean();
		boolean hideFileName = pageRenderer.getPar(FilePatterns.hidefilename,"false").asBoolean();		
		
		String maxdimattr = "";
		int maxdim = pageRenderer.getPar(FilePatterns.maxdim,"-1").asInt();
		if(maxdim > 0)
			maxdimattr = " maxdim=\""+maxdim+"\"";
		
		String showStyle = "class='uploadButton' onclick='__fileIntf.showFileType(\""+propName+"\","+propValue.isVirusExamined()+");'";
		if(propValue.isNull())
			showStyle += " disabled";
		
		String clearStyle = "class='uploadButton' onclick='__fileIntf.clearFileType(\""+propName+"\");'";
		if(propValue.isNull())
			clearStyle += " disabled";
		
		String previewStyle = "class='uploadButton' onclick='__fileIntf.showFileType(\""+propName+"\","+propValue.isVirusExamined()+");'";
		if(propValue.isNull())
			previewStyle += " disabled";
		
		String browseStyle = "class='uploadButton' ";
				
		StringBuffer res = new StringBuffer();
		
		res.append("<table cellpadding='0' cellspacing='0'><tr>");
		
		if(readonly){
			
			boolean showview = pageRenderer.getPar(FilePatterns.showview,"true").asBoolean();
			if(showview)
				res.append("<td><input type='button' value='"+showlabel+"' "+showStyle+" "+size+"></td>\n");
			res.append("<td><input class='outputField' type='text' id='"+propName+"' name='"+propName+"' "+ 
								   "value='"+propValue.getFileName()+"' "+sReadonlyField+sReadonlyAttr+pageRenderer.getPars()+"></td>");
			
		}else{
			
			String onchangeCallback = pageRenderer.getPar(EventsPatterns.onchange,"").asString();
			res.append("<td>");
			res.append(	"<form enctype='multipart/form-data' name='"+propName+"Form' id='"+propName+"Form' method='post' action='call.wfem' "); 
			res.append(		  		"style='margin:0px;' target='"+propName+"IFrame'>\n");
			res.append(		"<input type='hidden' name='readRequest' value='true'>\n");
			res.append(		"<input type='hidden' name='wfemCmd' value=''>\n");
			res.append(		"<input type='hidden' name='BrowserInstance' value='"+pageRenderer.getTemplate().getBrowserInstance()+"'>\n");
			res.append(		"<input type='hidden' name='propertyName' value='"+propName+"'>\n");
			res.append(		"<iframe src='"+template.getWfemLayoutWebApp()+"/blankPage.html' name='"+propName+"IFrame' id='"+propName+"IFrame' style='display:none;'></iframe>\n");
			res.append(			"<table cellpadding='0' cellspacing='0'>\n");
			res.append(				"<tr>\n");
			res.append(					"<td>");
			res.append(							"<input class='uploadField' type='file'"+maxdimattr+" filetypes='"+propValue.getFileTypes()+"' ftype='FileType' name='"+propName+"' id='"+propName+"' value='"+propValue.getFileName()+"' " + (hideFileName ? "style='position:absolute;left:-100;width:0; height:0;'" : ""));
			res.append(									"onchange='__fileIntf.loadFileType(this);' "+size+" "+(onchangeCallback.length()>0?"onchangeCallback='"+onchangeCallback+"'":"")+">");			
			if(hideFileName)
				res.append(						"<label for='"+propName+"' id='"+propName+"Browse' " + browseStyle + " ><u>" + browseLabel + "</u></label>");			
			res.append(					"</td>"); 
			if(showclear)
				res.append(				"<td><input name='"+propName+"Clear' id='"+propName+"Clear' type='button' value='"+clearlabel+"' "+clearStyle+"></td>\n");
			if(showpreview)
				res.append(				"<td><input name='"+propName+"Preview' id='"+propName+"Preview' type='button' value='"+previewlabel+"' "+previewStyle+"></td>\n");
			res.append(				"</tr>\n");
			res.append(			"</table>\n");	
			res.append(	"</form>\n");
			res.append(	"<form name='"+propName+"xForm' id='"+propName+"xForm' method='post' action='call.wfem' style='display:none;' target='"+propName+"IFrame'>"); 
			res.append(		"<input type='hidden' name='readRequest' value='true'>\n");
			res.append(		"<input type='hidden' name='wfemCmd' value=''>\n");
			res.append(		"<input type='hidden' name='BrowserInstance' value='"+pageRenderer.getTemplate().getBrowserInstance()+"'>\n");
			res.append(		"<input type='hidden' name='propertyName' value='"+propName+"'>\n");
			res.append(	"</form>"); 
			res.append("</td>");
			
		}
		
		res.append("</tr></table>");
	    res.append("<script>__fieldIntf.initFileType('"+propName+"');</script>");
		return res.toString();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getDescValue(String propName){
		CommandDataModel dataModel = null;
		String modelPropName = propName;
		int sepIdx = propName.lastIndexOf("_");
		if(sepIdx >= 0){
			try{
				dataModel = (CommandDataModel)Tools.getPropertyValue(template.getPageDataModel(),propName.substring(0,sepIdx));
				modelPropName = propName.substring(sepIdx+1);
			}catch(Exception e){
				return null;
			}
		}else{
			dataModel = template.getPageDataModel();
		}
		if(dataModel.getCodDescFields().get(modelPropName) != null)
			return dataModel.getDescValue(modelPropName);
		return null;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	protected Vector getCodDescOptions(String propName){
		
    	String emptylabel = pageRenderer.getPar(CodDescPatterns.emptylabel,template.getPageDataModel().getCodDescEmptyValue()).asString();
    	boolean showcode = pageRenderer.getPar(CodDescPatterns.showcode,"false").asBoolean();
    	
		CommandDataModel dataModel = null;
		String modelPropName = propName;
		int sepIdx = propName.lastIndexOf("_");
		if(sepIdx >= 0){
			try{
				dataModel = (CommandDataModel)Tools.getPropertyValue(template.getPageDataModel(),propName.substring(0,sepIdx));
				modelPropName = propName.substring(sepIdx+1);
			}catch(Exception e){
				return null;
			}
		}else{
			dataModel = template.getPageDataModel();
		}
		if(dataModel.getCodDescFields().get(modelPropName) == null)
			return null;
		
		Vector options = new Vector();
		
	    String daoCodDescName = (String)dataModel.getCodDescFields().get(modelPropName);
	    if(daoCodDescName == null){
	    	options.add(new CodDescOption("","No binding defined",false,false,false));
	    	return options;
	    }
	
        CodDescDataList dataList = dataModel.getCodDescDataList(modelPropName);
        if(dataList == null){
	    	options.add(new CodDescOption("","",false,false,false));
	    	return options;
	    }
	    
        String propertyCode = "";
        if(propValue != null)
	        propertyCode = propValue.toString();
        
        if(propValue == null || propValue.isNull()){
	    	options.add(new CodDescOption("",emptylabel,true,true,true));
    	}else{
        	if(dataList.getCodDescCount() == 0)
		    	options.add(new CodDescOption("",emptylabel,false,true,true));
        	else
        		options.add(new CodDescOption("",emptylabel,false,true,true));
        }

        for(int i=0;i<dataList.getCodDescCount();i++){
            
        	CodDescData data = dataList.getCodDesc(i);

        	if(params.modality == Template.INSERT_MODALITY && !data.isValid())
        		continue;
             	
        	String code = data.getCod();
        	String desc = data.getDescr();
        	desc = Tools.stringToHTMLString(desc);
        	if(showcode)
        		desc = code + " - " + desc;

        	if(code.equals(propertyCode)){
	            if(!data.isValid())
			    	options.add(new CodDescOption(code,desc,true,false,false));
	            else
			    	options.add(new CodDescOption(code,desc,true,true,false));
        	}else{
	            if(data.isValid())
			    	options.add(new CodDescOption(code,desc,false,true,false));
        	}
        }
        return options;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getClassName(){
		if(params.modality == Template.READ_MODALITY)
			return "outputField";
		if(propValue != null && propValue.hasTypeErrors())
			return "fieldHasError";
		return "inputField";
	}
	
	
}
