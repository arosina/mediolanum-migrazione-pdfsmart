package com.atosorigin.wfem.layout.field;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.atosorigin.wfem.coddesc.CodDescData;
import com.atosorigin.wfem.coddesc.CodDescDataList;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.controller.Configuration;
import com.atosorigin.wfem.dao.DAOAccessInfo;
import com.atosorigin.wfem.layout.FieldsFactory;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.layout.htmlrenderer.ComboTypeRenderer;
import com.atosorigin.wfem.layout.htmlrenderer.GridRenderer;
import com.atosorigin.wfem.layout.htmlrenderer.ListTypeRenderer;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.BooleanType;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.FileType;
import com.atosorigin.wfem.types.IntegerType;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TimestampType;
import com.atosorigin.wfem.types.TypeError;
import com.atosorigin.wfem.types.TypeMessage;
import com.atosorigin.wfem.types.TypeWarning;
import com.atosorigin.wfem.util.Tools;
import com.atosorigin.wfem.util.VersionPrinter;

/**************************************************************************************************/
/**************************************************************************************************/
public class DefaultFieldsFactory extends FieldsFactory{
	
	{VersionPrinter.getInstance().print("wfemLayout",this);}
	
	private static final int STRBUF_SIZE = 2*1024;
	
	static class Patterns{
		private static final String paramPattern="\\s*=\\s*(['\"])(.*?)\\1";
		static final Pattern positionleft    	= Pattern.compile("positionleft"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern positiontop     	= Pattern.compile("positiontop"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern cols            	= Pattern.compile("cols"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern colswidths      	= Pattern.compile("colswidths"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern type            	= Pattern.compile("type"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern booltruevalue   	= Pattern.compile("booltruevalue"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern boolfalsevalue  	= Pattern.compile("boolfalsevalue"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern uppercase       	= Pattern.compile("uppercase"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern rows            	= Pattern.compile("rows"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern ondeactivate    	= Pattern.compile("ondeactivate"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern showcalendar    	= Pattern.compile("showcalendar"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern heavy           	= Pattern.compile("heavy"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern jscombo         	= Pattern.compile("jscombo"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern onchange        	= Pattern.compile("onchange"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern size            	= Pattern.compile("size"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern showcode        	= Pattern.compile("showcode"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern showempty       	= Pattern.compile("showempty"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern put             	= Pattern.compile("put"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern ascombo         	= Pattern.compile("ascombo"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern onclick         	= Pattern.compile("onclick"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern labelcode       	= Pattern.compile("labelcode"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern onkeypress      	= Pattern.compile("onkeypress"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern onpaste         	= Pattern.compile("onpaste"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern onkeydown       	= Pattern.compile("onkeydown"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern onbeforepaste   	= Pattern.compile("onbeforepaste"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern onblur          	= Pattern.compile("onblur"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern onbeforeactivate	= Pattern.compile("onbeforeactivate"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern helper          	= Pattern.compile("helper"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern nodate          	= Pattern.compile("nodate"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern notime          	= Pattern.compile("notime"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern noseconds       	= Pattern.compile("noseconds"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern comboemptylabel	= Pattern.compile("emptylabel"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern showclear			= Pattern.compile("showclear"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern showpreview		= Pattern.compile("showpreview"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern showshow			= Pattern.compile("showshow"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern attachlabel		= Pattern.compile("attachlabel"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern clearlabel			= Pattern.compile("clearlabel"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern previewlabel		= Pattern.compile("previewlabel"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern showlabel			= Pattern.compile("showlabel"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern doublescale		= Pattern.compile("doublescale"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern yearpt				= Pattern.compile("yearpt"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern daymonthpt			= Pattern.compile("daymonthpt"+paramPattern,Pattern.CASE_INSENSITIVE);
		static final Pattern timept				= Pattern.compile("timept"+paramPattern,Pattern.CASE_INSENSITIVE);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String hiddenField(String propName) {
		
		if(!getPrefix().equals(""))
			propName = getPrefix() + "_" + propName;
		
		try{			
			Class propType = Tools.getPropertyType(pageDataModel,propName);
			if(propType == null){
				return "<script>alert('Error on creating hidden field "+propName+"');</script>";
			}

			AbstractType propValue = null;
			propValue = (AbstractType)Tools.getPropertyValue(pageDataModel,propName);
			String htmlValue = "";
			if(propValue != null)
				htmlValue = propValue.toString();
			if(htmlValue.indexOf('\'') >= 0)
				htmlValue = htmlValue.replaceAll("\'","&#39;");
			return "<input type='hidden' name='"+propName+"' id='"+propName+"' value='"+htmlValue+"'>\n";

		}catch(Exception e){
			return "<script>alert('Exception on creating hidden field "+propName+"');</script>";
		}
		
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String field(String propName, int modality, long maxLength, String extraPar, 
						 String labelWidth, String fieldWidth) {

		Matcher mat = null;
		
		int savModality = this.modality;
		this.modality = modality;
    	this.fieldWasReadonly = false;
		
		String positionleft = "";
		try{
			mat = Patterns.positionleft.matcher(extraPar);
			if(mat.find()){
				positionleft = mat.group(2);
				extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
			}
		}catch(Exception e){}
		
		String positiontop = "";
		try{
			mat = Patterns.positiontop.matcher(extraPar);
			if(mat.find()){
				positiontop = mat.group(2);
				extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
			}
		}catch(Exception e){}
		String position = "";
		String width = "width='100%'";
		if(positionleft.length() > 0 || positiontop.length() > 0){
			if(positionleft.length() == 0) positionleft = "0";
			if(positiontop.length() == 0) positiontop = "0";
			positionleft = "left:"+positionleft+";";
			positiontop = "top:"+positiontop+";";
			position = "style='position:absolute;"+positiontop+positionleft+"'";
			width = "";
		}
		
		if(!getPrefix().equals(""))
			propName = getPrefix() + "_" + propName;

		StringBuffer res = new StringBuffer(STRBUF_SIZE);
		
		if(!template.isInGrid()){
			res.append("<table id='"+propName+"Field' name='"+propName+"Field' "+width+" border='"+border+"' " +
							    "cellspacing='0' cellpadding='0' "+position+"><tr>");
		}
		
		try{
			if(extraPar == null)
				extraPar = "";
			extraPar += " ";
				
			Class propType = Tools.getPropertyType(pageDataModel,propName);
			if(propType == null){
				res.append("<td>Field type for property ["+propName+"] is null</td>");
				return res.toString();
			}

			AbstractType propValue = null;
			try{			
				propValue = (AbstractType)Tools.getPropertyValue(pageDataModel,propName);
			}catch(ClassCastException cce){
				res.append("<td>Field ["+propName+"] is not an AbstractType</td>");
				return res.toString();
			}
			
			if(propValue == null){
				res.append("<td>Field ["+propName+"] is null</td>");
				return res.toString();
			}
			
			res.append(getUpLeftLabel(pageName,propName,labelWidth,extraPar));
			
			if((!propValue.hasTypeErrors() && propValue.hasTypeWarnings()) ||
			     propValue.isSkippable()){
			    if(this.modality == READ_MODALITY)
			    	this.fieldWasReadonly = true;
				this.modality = READ_MODALITY;
			}
			
			if(modality != Template.PRINT_MODALITY && !propValue.isEditable())
				this.modality = READ_MODALITY;

			if(isComboType(propName)){
				res.append(manageComboType(propName,propValue,fieldWidth,extraPar));
				res.append(getDownRightLabel(pageName,propName,labelWidth,extraPar));
				return res.toString();
			}
						
			if(propType.equals(StringType.class))
				res.append(manageStringType(propName,(StringType)propValue,maxLength,fieldWidth,extraPar));
			else if(propType.equals(IntegerType.class))
				res.append(manageIntegerType(propName,(IntegerType)propValue,maxLength,fieldWidth,extraPar));
			else if(propType.equals(DoubleType.class))
				res.append(manageDoubleType(propName,(DoubleType)propValue,maxLength,fieldWidth,extraPar));
			else if(propType.equals(DateType.class))
				res.append(manageDateType(propName,(DateType)propValue,fieldWidth,extraPar));
			else if(propType.equals(BooleanType.class))
				res.append(manageBooleanType(propName,(BooleanType)propValue,fieldWidth,extraPar));
			else if (propType.equals(TimestampType.class))
				res.append(manageTimestampType(propName,(TimestampType)propValue,fieldWidth,extraPar));
			else if (propType.equals(FileType.class)){
				res.append(manageFileType(propName,(FileType)propValue,fieldWidth,extraPar));
			}else
				res.append("<td>Field type ["+propType+"] is not managed</td>");

			res.append(getDownRightLabel(pageName,propName,labelWidth,extraPar));
			
						
		}catch(Exception e){
			
			if(!template.isInGrid())
				res.append("<td>"+e.toString()+"</td>");
			else
				res.append(e.toString());
			
		}finally{
			
			if(!template.isInGrid())
				res.append("</tr></table>");
			
			this.modality = savModality;
			return res.toString();
		}
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String tableField(String propName, String[] viewPropertyNames, String[] colWidths, String extraPar) {
		
		if(!getPrefix().equals(""))
			propName = getPrefix() + "_" + propName;
		
		StringBuffer res = new StringBuffer(STRBUF_SIZE);
		try{


			if(extraPar == null)
				extraPar = "";
			extraPar += " ";
			
			Class propType = Tools.getPropertyType(pageDataModel,propName);
			if(propType == null){
				res.append("<span>Field type for property ["+propName+"] is null</span>");
				return res.toString();
			}

			ListType listValue = null;
			try{			
				listValue = (ListType)Tools.getPropertyValue(pageDataModel,propName);
			}catch(ClassCastException cce){
				res.append("<span>Field ["+propName+"] is not an ListType</span>");
				return res.toString();
			}
			
			if(listValue == null){
				res.append("<span>Field ["+propName+"] is null</span>");
				return res.toString();
			}

			if(viewPropertyNames == null || viewPropertyNames.length == 0){

				List propNames = Tools.getAbstractTypeFieldNames((CommandDataModel)listValue.getModelType().newInstance());
				viewPropertyNames = new String[propNames.size()];
				int i = 0;
				for(Iterator it = propNames.iterator(); it.hasNext(); i++){
					viewPropertyNames[i] = (String) it.next();
				}

			}
			
			res.append(manageListType(propName, listValue, viewPropertyNames, colWidths, extraPar));
			
		}catch(Exception e){
			
			res.append("<span style='color: red;'>"+e+"</span>");
			LOG.error(e);
		}finally{
			return res.toString();
		}
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String gridField(String propName, String parameters) {
		
		Matcher mat = null;
		
		if(!getPrefix().equals(""))
			propName = getPrefix() + "_" + propName;
		
		StringBuffer res = new StringBuffer(STRBUF_SIZE);
		try{


			if(parameters == null)
				parameters = "";
			parameters = " "+parameters+" ";
			
			Class propType = Tools.getPropertyType(pageDataModel,propName);
			if(propType == null){
				res.append("<span>Field type for property ["+propName+"] is null</span>");
				return res.toString();
			}

			ListType listValue = null;
			try{			
				listValue = (ListType)Tools.getPropertyValue(pageDataModel,propName);
			}catch(ClassCastException cce){
				res.append("<span>Field ["+propName+"] is not an ListType</span>");
				return res.toString();
			}
			
			if(listValue == null){
				res.append("<span>Field ["+propName+"] is null</span>");
				return res.toString();
			}

			Vector cols = new Vector();
			mat = Patterns.cols.matcher(parameters);
			if(mat.find()){
				String exp = mat.group(2);
				StringTokenizer st = new StringTokenizer(exp,",");
				while(st.hasMoreTokens()){
					cols.add(st.nextToken());
				}
				parameters = parameters.substring(0, mat.start()) + parameters.substring(mat.end());
			}
			
			Vector colsWidths = new Vector();
			mat = Patterns.colswidths.matcher(parameters);
			if(mat.find()){
				String exp = mat.group(2);
				StringTokenizer st = new StringTokenizer(exp,",");
				while(st.hasMoreTokens()){
					colsWidths.add(st.nextToken());
				}
				parameters = parameters.substring(0, mat.start()) + parameters.substring(mat.end());
			}

			GridRenderer gr = new GridRenderer(template, ffTemplate, propName, listValue, cols, colsWidths, parameters);
			res.append(gr.getHtml());

			listValue.setFieldRenderer(gr);
			
		}catch(Exception e){
			
			res.append("<span style='color: red;'>"+e+"</span>");
			LOG.error(e);
		}finally{
			return res.toString();
		}
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private String manageListType(String propName, ListType listValue, String[] viewPropertyNames, 
									String[] colWidths, String extraPar) throws Exception{

		CommandDataModel dataModel = null;
		String modelPropName = propName;
		int sepIdx = propName.lastIndexOf("_");
		if(sepIdx >= 0){
			try{
				dataModel = (CommandDataModel)Tools.getPropertyValue(pageDataModel,propName.substring(0,sepIdx));
				modelPropName = propName.substring(sepIdx+1);
			}catch(Exception e){
				throw e;
			}
		}else{
			dataModel = pageDataModel;
		}
		
		
		StringBuffer res = new StringBuffer(STRBUF_SIZE);

		ListTypeRenderer ltRenderer = new ListTypeRenderer(
											new FieldModel(propName, null, null, extraPar, -1, null, false), 
											listValue,
											viewPropertyNames, colWidths,
											webApp, modelPropName, dataModel, 
											template, ffTemplate, pageName);

		res.append(ltRenderer.getFieldRendering());											
        return res.toString();
	}

	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String manageStringType(String propName, StringType propValue, 
								     long maxLenth, String fieldWidth, String extraPar) throws Exception{

		Matcher mat = null;
		String fieldType = "string";
		
		mat = Patterns.type.matcher(extraPar);
		if(mat.find()){
			fieldType = mat.group(2);
			extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
		}
		
		if(fieldType.equalsIgnoreCase("num"))
			fieldType = "int";
		
		boolean makeHelperAnchor = isHelperEnabled(extraPar);
		
		if(fieldType.equalsIgnoreCase("bool") && modality != Template.PRINT_MODALITY){

		    String booleanTrueValue = DAOAccessInfo.getBooleanTrueValue();
			mat = Patterns.booltruevalue.matcher(extraPar);
			if(mat.find()){
				booleanTrueValue = mat.group(2);
				extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
			}
		    String booleanFalseValue = DAOAccessInfo.getBooleanFalseValue();
			mat = Patterns.boolfalsevalue.matcher(extraPar);
			if(mat.find()){
				booleanFalseValue = mat.group(2);
				extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
			}

			StringBuffer res = new StringBuffer(STRBUF_SIZE);			
			res.append(getFirstTd(fieldWidth));
		    
		    String className = "inputField";
		    res.append("<select class='"+className+"' name='"+propName+"' id='"+propName+"' "+extraPar);
			if(modality == Template.READ_MODALITY)
			    res.append(" disabled tabindex='-1'");
			res.append(">\n");
			String yes = ffTemplate.getProperty("BooleanType.true");
			String no = ffTemplate.getProperty("BooleanType.false");
		    if(propValue.isNull())
		        res.append("<option selected value=\"\">"+pageDataModel.getCodDescEmptyValue()+"</option>");
		    else
		        res.append("<option value=\"\">"+pageDataModel.getCodDescEmptyValue()+"</option>");
		    
		    if(propValue.toString().equalsIgnoreCase(booleanTrueValue))
		        res.append("<option selected value=\""+booleanTrueValue+"\">"+yes+"</option>\n");
		    else
		    	res.append("<option value=\""+booleanTrueValue+"\">"+yes+"</option>\n");
		    
		    if(propValue.toString().equalsIgnoreCase(booleanFalseValue))
			    res.append("<option selected value=\""+booleanFalseValue+"\">"+no+"</option>\n");
		    else
			    res.append("<option value=\""+booleanFalseValue+"\">"+no+"</option>\n");
		    
		    res.append("</select>\n");		   

		    if(makeHelperAnchor)
				res.append(getInlineProblemsHelperAnchor(propName,propValue,ffAnchorPosition));

			res.append(getLastTd());	
		    return res.toString();
		}		

		boolean locUpperCase = ffUpperCase;
		mat = Patterns.uppercase.matcher(extraPar);
		if(mat.find()){
		    String exp = mat.group(2);
			if(exp.equalsIgnoreCase("false"))
			    locUpperCase = false;
			else
			    locUpperCase = true;
			extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
		}

		if(fieldType.equalsIgnoreCase("password"))
			extraPar = getEventManager("string",propName,extraPar,false);
		else
			extraPar = getEventManager(fieldType,propName,extraPar,locUpperCase);

		String className = getClassName(propValue);

		StringBuffer res = new StringBuffer(STRBUF_SIZE);
		res.append(getFirstTd(fieldWidth));

		if(modality == Template.PRINT_MODALITY){
			res.append(Tools.stringToHTMLString(propValue.toString()));
			res.append("</td>");
			return res.toString();
		}
		
		mat = Patterns.rows.matcher(extraPar);
		if(mat.find()){
			res.append("<textarea ");
			if(maxLenth > 0)
				res.append("maxlength='"+maxLenth+"' ");			
			res.append("class='"+className+"' ");
			res.append("name='"+propName+"' id='"+propName+"' ");
			if(modality == Template.READ_MODALITY)
				res.append("readonly tabindex='-1' ");
			if(this.fieldWasReadonly)
				res.append("wasReadonly=true ");
			res.append(extraPar+" ");
			res.append(">"+Tools.stringToHTMLString(propValue.toString())+"</textarea>");
		}else{
			if(fieldType.equalsIgnoreCase("password"))
				res.append("<input type='password' ");
			else
				res.append("<input type='text' ");
			if(maxLenth > 0)
				res.append("maxlength='"+maxLenth+"' ");			
			res.append("value=\""+Tools.stringToHTMLString(propValue.toString())+"\" ");
			res.append("class='"+className+"' ");
			res.append("name='"+propName+"' id='"+propName+"' ");
			if(modality == READ_MODALITY)
				res.append("readonly ");
			if(this.fieldWasReadonly)
				res.append("wasReadonly=true ");
			res.append(extraPar+" ");
			res.append(">");
		}
		
		if(makeHelperAnchor)
			res.append(getInlineProblemsHelperAnchor(propName,propValue,ffAnchorPosition));

		res.append(getLastTd());
		return res.toString();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String manageIntegerType(String propName, IntegerType propValue,
									  long maxLenth, String fieldWidth, String extraPar) throws Exception{

		boolean makeHelperAnchor = isHelperEnabled(extraPar);
		extraPar = getEventManager("int",propName,extraPar,false);
		
		String className = getClassName(propValue);
			
		StringBuffer res = new StringBuffer(STRBUF_SIZE);
		res.append(getFirstTd(fieldWidth));
		
		if(modality == Template.PRINT_MODALITY){
			res.append(propValue);
			res.append(getLastTd());
			return res.toString();
		}
		
		res.append("<input type='text' ");
		if(maxLenth == 0)
			maxLenth = 10;
		res.append(" maxlength='"+maxLenth+"' ");		
		res.append("value=\""+propValue+"\" ");		
		res.append("class='"+className+"' ");
		res.append("name='"+propName+"' id='"+propName+"' ");
		if(modality == READ_MODALITY)
			res.append("readonly ");
		if(this.fieldWasReadonly)
			res.append("wasReadonly=true ");
		res.append(extraPar+" ");
		res.append(">");
		
		if(makeHelperAnchor)
			res.append(getInlineProblemsHelperAnchor(propName,propValue,ffAnchorPosition));
		
		res.append(getLastTd());
		return res.toString();
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private String manageDoubleType(String propName, DoubleType propValue, 
									 long maxLenth, String fieldWidth, String extraPar) throws Exception{

		boolean makeHelperAnchor = isHelperEnabled(extraPar);
		extraPar = getEventManager("double",propName,extraPar,false);

		String className = getClassName(propValue);
			
		StringBuffer res = new StringBuffer(STRBUF_SIZE);
		res.append(getFirstTd(fieldWidth));
		
		String doubleValue = propValue.toString();
		Matcher mat = Patterns.doublescale.matcher(extraPar);
		if(mat.find()){
		    String scale = mat.group(2);
			doubleValue = propValue.toScaledString(Integer.parseInt(scale));
		}

		if(modality == Template.PRINT_MODALITY){
			res.append(doubleValue);
			res.append(getLastTd());
			return res.toString();
		}
			
		res.append("<input type='text' ");
		if(maxLenth == 0)
			maxLenth = 15;
		res.append(" maxlength='"+maxLenth+"' ");
		res.append("value=\""+doubleValue+"\" ");
		res.append("class='"+className+"' ");
		res.append("name='"+propName+"' id='"+propName+"' ");
		if(modality == READ_MODALITY)
			res.append("readonly ");
		if(this.fieldWasReadonly)
			res.append("wasReadonly=true ");
		res.append(extraPar+" ");
		res.append(">");
		
		if(makeHelperAnchor)
			res.append(getInlineProblemsHelperAnchor(propName,propValue,ffAnchorPosition));
		
		res.append(getLastTd());
		return res.toString();
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private String manageDateType(String propName, DateType propValue, 
								   String fieldWidth, String extraPar) throws Exception{

		Matcher mat = null;
		
		boolean makeHelperAnchor = isHelperEnabled(extraPar);

		String exp = "";
		String apice = "'";
		mat = Patterns.onchange.matcher(extraPar);
		if(mat.find()){
			apice = mat.group(1);
			exp = mat.group(2);
			extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
		}
		String ctrlApice = apice.equals("'") ? "\"" : "'";
		String myOnchange = "setDateField("+ctrlApice+propName+ctrlApice+");";
		extraPar += " onchange="+apice+myOnchange+exp+apice+" ";

		extraPar = getEventManager("int",propName,extraPar,false);

		String className = getClassName(propValue);
		
		StringBuffer res = new StringBuffer(STRBUF_SIZE);
		res.append(getFirstTd(fieldWidth));
		
		if(modality == Template.PRINT_MODALITY){
			res.append(propValue);
			res.append(getLastTd());
			return res.toString();
		}

		int yearpt = 25;
		mat = Patterns.yearpt.matcher(extraPar);
		if(mat.find()){
			yearpt = Integer.parseInt(mat.group(2));
			extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
		}
		int daymonthpt = 15;
		mat = Patterns.daymonthpt.matcher(extraPar);
		if(mat.find()){
			daymonthpt = Integer.parseInt(mat.group(2));
			extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
		}
		
		res.append("<table cellspacing='1' cellpadding='1'><tr>");
		
		if(makeHelperAnchor && !template.isInGrid()){
			res.append("<td valign='top'>"+getInlineProblemsHelperAnchor(propName,propValue,ffAnchorPosition)+"</td>");
		}
		
		res.append("<td style='width:"+(daymonthpt+1)+"pt;'>");		
		res.append("<input type='text' maxlength='2' style='width:"+daymonthpt+"pt;' ");
		res.append("value=\""+propValue.getGG()+"\" ");
		res.append("class='"+className+"' ");
		res.append("name='"+propName+"GG' id='"+propName+"GG' ");
		if(modality == READ_MODALITY)
			res.append("readonly ");
		if(this.fieldWasReadonly)
			res.append("wasReadonly=true ");
		res.append(extraPar+" >");		
		res.append("</td>");
		
		res.append("<td style='width:"+(daymonthpt+1)+"pt;'>");
		res.append("<input type='text' maxlength='2' style='width:"+daymonthpt+"pt;' ");
		res.append("value=\""+propValue.getMM()+"\" ");
		res.append("class='"+className+"' ");
		res.append("name='"+propName+"MM' id='"+propName+"MM' ");
		if(modality == READ_MODALITY)
			res.append("readonly ");
		if(this.fieldWasReadonly)
			res.append("wasReadonly=true ");
		res.append(extraPar+" >");
		res.append("</td>");

		res.append("<td style='width:"+(yearpt+1)+"pt;'>");
		res.append("<input type='text' maxlength='4' style='width:"+(yearpt+1)+"pt;' ");
		res.append( "value=\""+propValue.getAA()+"\" ");
		res.append("class='"+className+"' ");
		res.append("name='"+propName+"AA' id='"+propName+"AA' ");
		if(modality == READ_MODALITY)
			res.append("readonly ");
		if(this.fieldWasReadonly)
			res.append("wasReadonly=true ");
		res.append(extraPar+" >");
		res.append("</td>");
		
		boolean createCal = false;
		boolean visibleCal = false;
		if(modality != READ_MODALITY){
			createCal = true;
			visibleCal = true;
		}
		if((!propValue.hasTypeErrors() && propValue.hasTypeWarnings()) && !propValue.isSkippable()){
			createCal = true;
			visibleCal = false;
		}
		exp = "";
		mat = Patterns.showcalendar.matcher(extraPar);
		if(mat.find()){
		    exp = mat.group(2);
			if(exp.equalsIgnoreCase("false"))
				visibleCal = false;
			extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
		}		
		
		if(makeHelperAnchor && template.isInGrid()){
			res.append("<td valign='top' style='width:1px;'>"+getInlineProblemsHelperAnchor(propName,propValue,ffAnchorPosition)+"</td>");
		}
		
		if(createCal){
		    res.append("<td id='"+propName+"CalAnchor' name='"+propName+"CalAnchor' style='width:1px;'></td>");
			res.append("<td valign='center'>");
			if(visibleCal)
				res.append("<table width='100%' border='0' cellspacing='0' cellpadding='0' id='"+propName+"CalendarTable'>");
			else
				res.append("<table width='100%' border='0' cellspacing='0' cellpadding='0' id='"+propName+"CalendarTable' style='visibility: hidden;'>");
			
			res.append("<tr><td valign='center'>");
			res.append("<img tabindex='-1' src='"+Template.CONTROLLER_CALL_CMD+"=getImage&fileName=openCalendar.gif' ");
			res.append("name='"+propName+"_imgSrc' id='"+propName+"_imgSrc' ");
			res.append("onclick='calendar.output=\""+propName+"\";calendar.select(document.all(\""+propName+"\"),\""+propName+"CalAnchor\",\"dd-MM-yyyy\");' style='cursor: hand;'>");
			res.append("</td></tr>");
	
			res.append("<tr><td>");
		    res.append("<img tabindex='-1' src='"+Template.CONTROLLER_CALL_CMD+"=getImage&fileName=clearCalendar.gif' ");
			res.append("onclick='hideHelperAnchor(\""+propName+"\");clearDateField(\""+propName+"\");' style='cursor: hand;'>");
			res.append("</td></tr>");
			
			res.append("</table>");
			res.append("</td>");
		}else{
			res.append("<td>&nbsp;</td>");
		}
		
		res.append("<input type='hidden' class='"+className+"' name='"+propName+"' id='"+propName+"' isDate='true' value=\""+propValue+"\">");
				
		res.append("</tr></table>");
		
		res.append(getLastTd());
		return res.toString();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String manageComboType(String propName, AbstractType propValue, 
									String fieldWidth, String extraPar) throws Exception{

		Matcher mat = null;
		
		boolean makeHelperAnchor = isHelperEnabled(extraPar);

		mat = Patterns.heavy.matcher(extraPar);
		boolean isHeavy = false;
		if(mat.find()){
			isHeavy = new Boolean(mat.group(2)).booleanValue();
			extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
		}

		mat = Patterns.jscombo.matcher(extraPar);
		if(mat.find()){
			isHeavy = new Boolean(mat.group(2)).booleanValue();
			isHeavy = !isHeavy;
			extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
		}

		if(isHeavy || !jsCombo || modality == Template.PRINT_MODALITY) 
			return renderHeavyComboType(propName, propValue, fieldWidth, extraPar, makeHelperAnchor);

		String apice = "'";
		String exp = "";
		mat = Patterns.onchange.matcher(extraPar);
		if(mat.find()){
			apice = mat.group(1);
			exp = mat.group(2);
			extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
		}
		String ctrlApice = apice.equals("'") ? "\"" : "'";
		String myOnchange = "hideHelperAnchor("+ctrlApice+propName+ctrlApice+");";
		extraPar += " onchange="+apice+myOnchange+exp+apice+" ";
		
		CommandDataModel dataModel = null;
		String modelPropName = propName;
		int sepIdx = propName.lastIndexOf("_");
		if(sepIdx >= 0){
			try{
				dataModel = (CommandDataModel)Tools.getPropertyValue(pageDataModel,propName.substring(0,sepIdx));
				modelPropName = propName.substring(sepIdx+1);
			}catch(Exception e){
				throw e;
			}
		}else{
			dataModel = pageDataModel;
		}
		
		String className = "inputField";
		if(modality == READ_MODALITY)
			className = "outputField";
			
		StringBuffer res = new StringBuffer(STRBUF_SIZE);
		res.append(getFirstTd(fieldWidth));

		ComboTypeRenderer ctRenderer = new ComboTypeRenderer(
											new FieldModel(propName, propValue, fieldWidth, extraPar, modality, className, fieldWasReadonly), 
														    webApp, modelPropName, dataModel);

		res.append(ctRenderer.getFieldRendering());											

		if(makeHelperAnchor)
			res.append(getInlineProblemsHelperAnchor(propName,propValue,ffAnchorPosition));
		
		res.append(getLastTd());		
        return res.toString();
	}


	/**************************************************************************************************/
	/**************************************************************************************************/
	private String renderHeavyComboType(String propName, AbstractType propValue, 
									String fieldWidth, String extraPar, boolean makeHelperAnchor) throws Exception{

		Matcher mat = null;
		int size = 1;
		
		mat = Patterns.size.matcher(extraPar);
		if(mat.find()){
			String exp = mat.group(2);
			size = Integer.parseInt(exp);
		}

		String emptyLabel = pageDataModel.getCodDescEmptyValue();
		mat = Patterns.comboemptylabel.matcher(extraPar);
		if(mat.find()){
			emptyLabel = mat.group(2);
			extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
		}
		
		boolean showCode = false;										
		mat = Patterns.showcode.matcher(extraPar);
		if(mat.find()){
			String exp = mat.group(2);
			if(exp.equalsIgnoreCase("true"))
				showCode = true;
			extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
		}

		boolean showEmpty = true;										
		mat = Patterns.showempty.matcher(extraPar);
		if(mat.find()){
			String exp = mat.group(2);
			if(exp.equalsIgnoreCase("false"))
				showEmpty = false;
			extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
		}

		String putProps = "";
		mat = Patterns.put.matcher(extraPar);
		if(mat.find()){
			putProps = mat.group(2);
			extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
		}
		Vector props = new Vector();
		StringTokenizer st = new StringTokenizer(putProps,",");
		while(st.hasMoreTokens())
			props.add(st.nextToken());

		String apice = "'";
		String exp = "";
		mat = Patterns.onchange.matcher(extraPar);
		if(mat.find()){
			apice = mat.group(1);
			exp = mat.group(2);
			extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
		}
		String ctrlApice = apice.equals("'") ? "\"" : "'";
		String myOnchange = "hideHelperAnchor("+ctrlApice+propName+ctrlApice+");";
		extraPar += " onchange="+apice+myOnchange+exp+apice+" ";
		
		CommandDataModel dataModel = null;
		String modelPropName = propName;
		int sepIdx = propName.lastIndexOf("_");
		if(sepIdx >= 0){
			try{
				dataModel = (CommandDataModel)Tools.getPropertyValue(pageDataModel,propName.substring(0,sepIdx));
				modelPropName = propName.substring(sepIdx+1);
			}catch(Exception e){
				throw e;
			}
		}else{
			dataModel = pageDataModel;
		}
		
		StringBuffer res = new StringBuffer(STRBUF_SIZE);
		res.append(getFirstTd(fieldWidth));

		if(modality == Template.PRINT_MODALITY){
		    res.append(dataModel.getDescValue(modelPropName));
			res.append(getLastTd());
			return res.toString();
		}		
			
	    String comboStringIni = "<select class='inputField' name='"+propName+"' id='"+propName+"' "+extraPar;
		if(modality == READ_MODALITY)
		    comboStringIni += " disabled ";
	    comboStringIni += " >";
	    String comboStringEnd = "</select>";

		HashMap codDescFields = dataModel.getCodDescFields();
		
	    String daoCodDescName = (String)codDescFields.get(modelPropName);
	    if(daoCodDescName == null){
	      LOG.warning("makeComboType(): No binding defined for property ["+modelPropName+"]");
	      return res.toString() + comboStringIni + comboStringEnd + getLastTd();
	    }
	
        CodDescDataList dataList = dataModel.getCodDescDataList(modelPropName);
        if(dataList == null){
	      LOG.warning("makeComboType: Table ["+daoCodDescName+"] not loaded for property ["+modelPropName+"]");
	      return res.toString() + comboStringIni + comboStringEnd + getLastTd();
	    }
	    
        String propertyCode = "";
        if(propValue != null)
	        propertyCode = propValue.toString();
        
        StringBuffer options = new StringBuffer(STRBUF_SIZE);

		if(size == 1){
	        if(showEmpty){
		        if(propValue == null || propValue.isNull())
		          options.append("<option selected value=\"\">"+emptyLabel+"</option>");
		        else
		          options.append("<option value=\"\">"+emptyLabel+"</option>");
	        }else{
	        	if(dataList.getCodDescCount() == 0)
		          options.append("<option value=\"\">"+emptyLabel+"</option>");
	        }
		}

        for(int i=0;i<dataList.getCodDescCount();i++){
            
          CodDescData data = dataList.getCodDesc(i);

          if(modality == Template.INSERT_MODALITY && !data.isValid())
             	continue;
             	
          String code = data.getCod();
          String desc = data.getDescr();
          desc = Tools.stringToHTMLString(desc);
          if(showCode)
            	desc = code + " - " + desc;

          String putProp = "";
          for(int j=0;j<props.size();j++){
            	String propN = (String)props.get(j);
            	String propVal = (String)Tools.getPropertyValue(data,propN);
            	putProp += propN+"=\""+propVal+"\" ";
          }
          
          if(code.equals(propertyCode)){
            if(!data.isValid()){
                if(modality != Template.READ_MODALITY)
                    options.append("<option selected "+putProp+" value =\"" + code + "\" style='color: red;'>"+desc+"</option>\n");
                else
                    options.append("<option selected "+putProp+" value =\"" + code + "\">"+desc+"</option>\n");
            }else{
                options.append("<option selected "+putProp+" value =\"" + code + "\">"+desc+"</option>\n");
            }
          }else{
            if(data.isValid())
                options.append("<option "+putProp+" value =\"" + code + "\">"+desc+"</option>\n");
          }
        }        
		
		res.append(comboStringIni);
		res.append(options);
		res.append(comboStringEnd);

		if(makeHelperAnchor)
			res.append(getInlineProblemsHelperAnchor(propName,propValue,ffAnchorPosition));
		
		res.append(getLastTd());		
        return res.toString();
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private String manageBooleanType(String propName, BooleanType propValue, 
									    String fieldWidth, String extraPar) throws Exception{

		Matcher mat = null;
		
		boolean makeHelperAnchor = isHelperEnabled(extraPar);

		String apice = "'";
		String exp = "";
		
		boolean asCombo = false;
		mat = Patterns.ascombo.matcher(extraPar);
		if(mat.find()){
			exp = mat.group(2);
			if(exp.equalsIgnoreCase("true"))
				asCombo = true;
			extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
		}
		
		if(!asCombo){
			exp = "";
			mat = Patterns.onclick.matcher(extraPar);
			if(mat.find()){
				apice = mat.group(1);
				exp = mat.group(2);
				extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
			}
			String ctrlApice = apice.equals("'") ? "\"" : "'";
			String myOnclick = "setBoolField("+ctrlApice+propName+ctrlApice+");hideHelperAnchor("+ctrlApice+propName+ctrlApice+");";
			extraPar += " onclick="+apice+myOnclick+exp+apice+" ";
		}
		
		StringBuffer res = new StringBuffer(STRBUF_SIZE);
		res.append(getFirstTd(fieldWidth));

		if(modality == Template.PRINT_MODALITY){
		    if(propValue.booleanValue())
		        res.append(ffTemplate.getProperty("BooleanType.true"));
		    else
		        res.append(ffTemplate.getProperty("BooleanType.false"));
		    
			res.append(getLastTd());
			return res.toString();
		}		
		
		if(asCombo){
			String yes = ffTemplate.getProperty("BooleanType.true");
			String no = ffTemplate.getProperty("BooleanType.false");
			
			String className="inputField";
			String disabled="";
			if(modality == READ_MODALITY){
				className = "outputField";
				disabled = "disabled";
			}
				
			res.append("<select class='"+className+"' name='"+propName+"' id='"+propName+"' "+extraPar+" "+disabled+">");
			if(propValue.booleanValue()){
		        res.append("<option value=\"false\">"+no+"</option>");
		        res.append("<option selected value=\"true\">"+yes+"</option>");
			}else{
		        res.append("<option selected value=\"false\">"+no+"</option>");				
		        res.append("<option value=\"true\">"+yes+"</option>");
			}
			res.append("</select>");
		}else{
			res.append("<input type='hidden' name='"+propName+"' id='"+propName+"' value='"+propValue+"' isCheck='true'>");		
			res.append("<input type='checkbox' name='"+propName+"Check' id='"+propName+"Check' ");
			if(propValue != null && propValue.booleanValue())
				res.append("checked ");		
			if(modality == READ_MODALITY)
				res.append("disabled ");
			if(this.fieldWasReadonly)
				res.append("wasReadonly=true ");
			res.append(extraPar+" ");
			res.append(">");
		}
		
		if(makeHelperAnchor)
			res.append(getInlineProblemsHelperAnchor(propName,propValue,ffAnchorPosition));	
		
		res.append(getLastTd());		
		return res.toString();
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String action(String actionName, String onExecute, String extraPar, 
						  boolean enabled, String actionText){

		if(onExecute.indexOf("(") < 0)
			onExecute += "()";

		StringBuffer res = new StringBuffer(STRBUF_SIZE);
		
		res.append("<table width='100%' border='"+border+"' cellspacing='0' cellpadding='0'><tr><td align='center'>");
		if(enabled){
			res.append("<input id='"+actionName+"' name='"+actionName+"' type='button' class='action'");
		}else{
			res.append("<input id='"+actionName+"' name='"+actionName+"' type='button' class='disabledAction'");
		}
		res.append(" onmouseover='if(this.className == \"action\"){this.className=\"selectedAction\";}'");
		res.append(" onmouseout='if(this.className == \"selectedAction\"){this.className=\"action\";}'");
		res.append(" onclick='if(this.disabled || this.className == \"disabledAction\")return; if(!isRequestPending()){if(eval(\""+onExecute+"\")){startRequest();}}' ");
		
		if(extraPar != null)
			res.append(extraPar);
			
		res.append(" value=\""+actionText+"\">");
		res.append("</td></tr></table>");
		return res.toString();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getUpLeftLabel(String pageName, String propName, String labelWidth, String extraPar) {
		
		Matcher mat = null;
		StringBuffer res = new StringBuffer(STRBUF_SIZE);

		String locLabelCode = pageName+propName;			
		try{	
			mat = Patterns.labelcode.matcher(extraPar);
			if(mat.find()){
			    locLabelCode = mat.group(2);
				extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
			}
		}catch(Exception e){}
		
		if(labelCodePrefix != null && !labelCodePrefix.equals("")){
			String lastPropName = propName.toString();
			if(propName.lastIndexOf("_") >= 0)
				lastPropName = propName.substring((propName.lastIndexOf("_")+1));
			locLabelCode = labelCodePrefix + lastPropName;
		}
		
		String text = template.getProperty(locLabelCode);
		text = Tools.convertSpecialChars(text);
		
		
		if(labelPosition == LEFT_LABEL){
			String align = labelAlign != null ? labelAlign : RIGHT_ALIGNED_LABEL;
			res.append("<td class='text' align='"+align+"' id='"+propName+"Label' name='"+propName+"Label' ");
			if(labelWidth != null && !labelWidth.equals(""))
				res.append("width='"+labelWidth+"' ");
			if(labelStyle != null && !labelStyle.equals(""))
				res.append("style='"+labelStyle+"' ");
			res.append(">"+text+"&nbsp;</td>");
		}
		
		if(labelPosition == UP_LABEL){
			String align = labelAlign != null ? labelAlign : LEFT_ALIGNED_LABEL;
			if(labelWidth != null && !labelWidth.equals("")){
				res.append("<td width='"+labelWidth+"' class='text' align='"+align+"' "+
				            "id='"+propName+"Label' name='"+propName+"Label' ");
				if(labelStyle != null && !labelStyle.equals(""))
					res.append("style='"+labelStyle+"' ");
				res.append(">"+text+"</td></tr><tr>");
			}else{
				res.append("<td class='text' align='"+align+"' id='"+propName+"Label' "+
				       	    "name='"+propName+"Label' ");
				if(labelStyle != null && !labelStyle.equals(""))
					res.append("style='"+labelStyle+"' ");
				res.append(">"+text+"</td></tr><tr>");
			}
		}			
		return res.toString();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getDownRightLabel(String pageName, String propName, String labelWidth, String extraPar) {

		Matcher mat = null;
		StringBuffer res = new StringBuffer(STRBUF_SIZE);
		
		String align = labelAlign != null ? labelAlign : LEFT_ALIGNED_LABEL;
		String locLabelCode = pageName+propName;
			
		try{	
			mat = Patterns.labelcode.matcher(extraPar);
			if(mat.find()){
			    locLabelCode = mat.group(2);
				extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
			}
		}catch(Exception e){}
		
		if(labelCodePrefix != null && !labelCodePrefix.equals("")){
			String lastPropName = propName.toString();
			if(propName.lastIndexOf("_") >= 0)
				lastPropName = propName.substring((propName.lastIndexOf("_")+1));
			locLabelCode = labelCodePrefix + lastPropName;
		}

		String text = template.getProperty(locLabelCode);
		text = Tools.convertSpecialChars(text);
		
		if(labelPosition == RIGHT_LABEL){
			res.append("<td class='text' align='"+align+"' id='"+propName+"Label' name='"+propName+"Label' ");
			if(labelWidth != null && !labelWidth.equals(""))
				res.append("width='"+labelWidth+"' ");
			if(labelStyle != null && !labelStyle.equals(""))
				res.append("style='"+labelStyle+"' ");
			res.append(">&nbsp;"+text+"</td>");
		}			
			
		if(labelPosition == DOWN_LABEL){
			res.append("</tr><tr><td class='text' align='"+align+"' id='"+propName+"Label' name='"+propName+"Label' ");
			if(labelWidth != null && !labelWidth.equals(""))
				res.append("width='"+labelWidth+"' ");
			if(labelStyle != null && !labelStyle.equals(""))
				res.append("style='"+labelStyle+"' ");
			res.append(">"+text+"</td></tr>");
		}						
		return res.toString();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getFirstTd(String fieldWidth) {
		
		if(template.isInGrid())
			return "";
		
		StringBuffer res = new StringBuffer();
		res.append("<td ");
		if(fieldWidth != null && !fieldWidth.equals(""))
			res.append("width='"+fieldWidth+"' ");
		String align = "center";
		if(labelPosition == RIGHT_LABEL)
			align = "right";
		if(labelPosition == LEFT_LABEL ||
		    labelPosition == UP_LABEL ||
		    labelPosition == DOWN_LABEL)
			align = "left";
		if(fieldAlign != null)
			align = fieldAlign;
		res.append("align='"+align+"' ");
		res.append(">");
		return res.toString();
	}					
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getLastTd() {
		if(template.isInGrid())
			return "";
		
		return "</td>";
	}					

	/***********************************************************************************************/
	/***********************************************************************************************/
	public String getComboElementsString(String propertyName){
		StringBuffer res = new StringBuffer(STRBUF_SIZE);
		CommandDataModel model = getPageDataModel();
		int idx = propertyName.lastIndexOf("_");
		if(idx >= 0){
			String modelPropName = propertyName.substring(0,idx);
			propertyName = propertyName.substring(idx+1);
			try{
				model = (CommandDataModel)Tools.getPropertyValue(getPageDataModel(),modelPropName);
			}catch(Exception e){
				return "XXX,XXXXXX";
			}
		}
		String propStrValue = "";
		try{
			AbstractType propValue = (AbstractType)Tools.getPropertyValue(model,propertyName);
			if(propValue != null)
				propStrValue = propValue.toString();
		}catch(Exception e){
			return "XXX,XXXXXX";
		}
		CodDescDataList dataList = model.getCodDescDataList(propertyName);
		for(int i=0;i<dataList.getCodDescCount();i++){
			CodDescData data = dataList.getCodDesc(i);
	        if(modality == INSERT_MODALITY &&
	           !data.isValid())
	           	continue;
	           	
	        if(data.isValid())
	        	res.append("t,");
	        else
	        	res.append( "f,");
	        if(data.getCod().equals(propStrValue))
	        	res.append("t,");
	        else
	        	res.append("f,");
			res.append(data.getCod() + "," + data.getDescr() + ";");
		}
		return res.toString();
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getClassName(AbstractType propValue){
		String className = "inputField";
		if(modality == READ_MODALITY)
			className = "outputField";
		if(modality != READ_MODALITY && propValue != null && propValue.hasTypeErrors())
			className = "fieldHasError";
		if(propValue != null && !propValue.hasTypeErrors() && propValue.hasTypeWarnings())
			className = "fieldHasWarning";
		return className;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String getEventManager(String fieldType, String propName, String extraPar, boolean upperCase) throws Exception{

		Matcher mat = null;
		String separator = ".";
		String decimalChar = ",";

		String apice = "'";
		String ctrlApice ;
		String exp = "";
	    
		//******* onkeypress **************************************************
		String fUpperCase = "";
		if(fieldType.equals("string") || fieldType.equals("alfa") || fieldType.equals("all")){
			if(upperCase)
		    	fUpperCase = "toUpper(event,this);";
		    else
		    	extraPar += " uppercase='false' ";
		}
		
		mat = Patterns.onkeypress.matcher(extraPar);
		if(mat.find()){
			apice = mat.group(1);
			exp = mat.group(2);
			extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
		}
		ctrlApice = apice.equals("'") ? "\"" : "'";
		String myOnkeypress;		
		if(fieldType.equals("double")){
			myOnkeypress="if(manageField(event,this,"+ctrlApice+fieldType+ctrlApice+","+ctrlApice+separator+ctrlApice+","+ctrlApice+decimalChar+ctrlApice+")){"+fUpperCase+"hideHelperAnchor("+ctrlApice+propName+ctrlApice+");}";		
		}else{
			if(fieldType.equals("all"))
				myOnkeypress="if(manageAllField(event,this)){"+fUpperCase+"hideHelperAnchor("+ctrlApice+propName+ctrlApice+");}";
			else
				myOnkeypress="if(manageField(event,this,"+ctrlApice+fieldType+ctrlApice+")){"+fUpperCase+"hideHelperAnchor("+ctrlApice+propName+ctrlApice+");}";
		}
		extraPar += "onkeypress="+apice+myOnkeypress+exp+apice+" ";
		//*********************************************************************
	
		//******* onpaste *****************************************************
		exp = "";
		mat = Patterns.onpaste.matcher(extraPar);
		if(mat.find()){
			apice = mat.group(1);
			exp = mat.group(2);
			extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
		}
		ctrlApice = apice.equals("'") ? "\"" : "'";
		String myOnpaste;		
		if(fieldType.equals("double")){
			myOnpaste="if(managePasteField(this,"+ctrlApice+fieldType+ctrlApice+","+ctrlApice+separator+ctrlApice+","+ctrlApice+decimalChar+ctrlApice+")){hideHelperAnchor("+ctrlApice+propName+ctrlApice+");}";
		}else{
			if(fieldType.equals("all"))
				myOnpaste="if(manageAllPasteField(this)){hideHelperAnchor("+ctrlApice+propName+ctrlApice+");}";
			else
				myOnpaste="if(managePasteField(this,"+ctrlApice+fieldType+ctrlApice+")){hideHelperAnchor("+ctrlApice+propName+ctrlApice+");}";
		}
		extraPar += "onpaste="+apice+myOnpaste+exp+apice+" ";
		//*********************************************************************

		//******* onkeydown ***************************************************
		exp = "";
		mat = Patterns.onkeydown.matcher(extraPar);
		if(mat.find()){
			apice = mat.group(1);
			exp = mat.group(2);
			extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
		}
		ctrlApice = apice.equals("'") ? "\"" : "'";
		String myOnkeydown;
		if(fieldType.equals("all"))
			myOnkeydown="if(manageAllKeydownField(event,this)){hideHelperAnchor("+ctrlApice+propName+ctrlApice+");}";
		else
			myOnkeydown="if(manageKeydownField(event,this,"+ctrlApice+fieldType+ctrlApice+")){hideHelperAnchor("+ctrlApice+propName+ctrlApice+");}";
		extraPar += "onkeydown="+apice+myOnkeydown+exp+apice+" ";
		//*********************************************************************

		//******* onbeforepaste ***********************************************
		exp = "";
		mat = Patterns.onbeforepaste.matcher(extraPar);
		if(mat.find()){
			apice = mat.group(1);
			exp = mat.group(2);
			extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
		}
		ctrlApice = apice.equals("'") ? "\"" : "'";
		String myOnbeforepaste="";		
		if(!fieldType.equals("all"))
			myOnbeforepaste="manageBeforePasteField(this,"+ctrlApice+fieldType+ctrlApice+");";
		if(!myOnbeforepaste.equals("") || !exp.equals(""))
			extraPar += "onbeforepaste="+apice+myOnbeforepaste+exp+apice+" ";
		//*********************************************************************
	
		if(fieldType.equals("double")){
			//******* onblur ******************************************************
			exp = "";
			mat = Patterns.onblur.matcher(extraPar);
			if(mat.find()){
				apice = mat.group(1);
				exp = mat.group(2);
				extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
			}
			ctrlApice = apice.equals("'") ? "\"" : "'";
			String myOnblur="formatNumber(this,2,"+ctrlApice+separator+ctrlApice+","+ctrlApice+decimalChar+ctrlApice+");";		
			extraPar += "onblur="+apice+myOnblur+exp+apice+" ";
			//*********************************************************************

			//******* onbeforeactivate ********************************************
			exp = "";
			mat = Patterns.onbeforeactivate.matcher(extraPar);
			if(mat.find()){
				apice = mat.group(1);
				exp = mat.group(2);
				extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
			}
			ctrlApice = apice.equals("'") ? "\"" : "'";
			String myOnbeforeactivate="manageBeforeActivateField(this,"+ctrlApice+fieldType+ctrlApice+","+ctrlApice+separator+ctrlApice+","+ctrlApice+decimalChar+ctrlApice+");";
			extraPar += "onbeforeactivate="+apice+myOnbeforeactivate+exp+apice+" ";
			//*********************************************************************
		}else if(fieldType.equals("int")){
			//******* onblur ******************************************************
			exp = "";
			mat = Patterns.onblur.matcher(extraPar);
			if(mat.find()){
				apice = mat.group(1);
				exp = mat.group(2);
				extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
			}
			ctrlApice = apice.equals("'") ? "\"" : "'";
			String myOnblur="formatNumber(this,0,"+ctrlApice+""+ctrlApice+","+ctrlApice+""+ctrlApice+");";		
			extraPar += "onblur="+apice+myOnblur+exp+apice+" ";
			//*********************************************************************
		}
		return extraPar;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private boolean isHelperEnabled(String extraPar) throws Exception{
		
		Matcher mat = null;
		
		boolean makeHelperAnchor = true;
		
		mat = Patterns.helper.matcher(extraPar);
		if(mat.find()){
			String exp = mat.group(2);
			if(exp.equalsIgnoreCase("no"))
				makeHelperAnchor = false;
			extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
		}
		return makeHelperAnchor;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String manageFileType(String propName, FileType propValue, String fieldWidth, String extraPar) throws Exception{
		
		Matcher mat = null;
		String size = "";
		
		try{
			mat = Patterns.size.matcher(extraPar);
			if(mat.find()){
				size = "size='"+mat.group(2)+"'";
				extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
			}
		}catch(Exception e){}
	
		String attachlabel = ffTemplate.getProperty("FileType.attach");
		mat = Patterns.attachlabel.matcher(extraPar);
		if(mat.find()){
			attachlabel = mat.group(2);
			extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
		}
		String clearlabel = ffTemplate.getProperty("FileType.clear");
		mat = Patterns.clearlabel.matcher(extraPar);
		if(mat.find()){
			clearlabel = mat.group(2);
			extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
		}
		String previewlabel = ffTemplate.getProperty("FileType.preview");
		mat = Patterns.previewlabel.matcher(extraPar);
		if(mat.find()){
			previewlabel = mat.group(2);
			extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
		}
		String showlabel = ffTemplate.getProperty("FileType.show");
		mat = Patterns.showlabel.matcher(extraPar);
		if(mat.find()){
			showlabel = mat.group(2);
			extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
		}
		
		boolean showclear = true;
		mat = Patterns.showclear.matcher(extraPar);
		if(mat.find()){
		    String exp = mat.group(2);
			if(exp.equalsIgnoreCase("false"))
				showclear = false;
			extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
		}		
		boolean showpreview = true;
		mat = Patterns.showpreview.matcher(extraPar);
		if(mat.find()){
		    String exp = mat.group(2);
			if(exp.equalsIgnoreCase("false"))
				showpreview = false;
			extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
		}		
		boolean showshow = true;
		mat = Patterns.showshow.matcher(extraPar);
		if(mat.find()){
		    String exp = mat.group(2);
			if(exp.equalsIgnoreCase("false"))
				showshow = false;
			extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
		}		
		
		String className = "outputField";
		
		String showStyle = "style='cursor:hand;width:65;font-family:Arial;font-size: 8pt;color:#1A458F;' ";
		if(propValue.isNull())
			showStyle = "disabled style='width:65;font-family:Arial;font-size:8pt;color:#1A458F;' ";
		
		String clearStyle = "style='cursor:hand;width:65;font-family:Arial;font-size: 8pt;color:#1A458F;";
		if(propValue.isNull())
			clearStyle = "disabled style='width:65;font-family:Arial;font-size:8pt;color:#1A458F;";
		if(showclear)
			clearStyle += "' ";
		else
			clearStyle += "display:none;' ";

		String previewStyle = "style='cursor:hand;width:65;font-family:Arial;font-size: 8pt;color:#1A458F;";
		if(propValue.isNull())
			previewStyle = "disabled style='width:65;font-family:Arial;font-size:8pt;color:#1A458F;";
		if(showpreview)
			previewStyle += "' ";
		else
			previewStyle += "display:none;' ";
		
		boolean makeHelperAnchor = isHelperEnabled(extraPar);
		
		StringBuffer res = new StringBuffer();
		
		res.append(getFirstTd(fieldWidth));
		
		if(modality == READ_MODALITY){
			
			res.append("<table cellpadding='0' cellspacing='0'><tr>\n");
			if(showshow)
				res.append("<td><input type='button' value='"+showlabel+"' "+showStyle+" onclick='showFileType(\""+propName+"\","+propValue.isVirusExamined()+");'></td>\n");
			res.append("<td><input class='"+className+"' type='text' id='"+propName+"' name='"+propName+"' "+size+" "+extraPar+"\n"); 
			res.append("           readonly value='"+propValue.getFileName()+"'></td>\n");
			res.append("</tr></table>\n");

			res.append(getLastTd());
			return res.toString();
		}
		
		res.append("<form name='"+propName+"Form' id='"+propName+"Form' method='post' action='call.wfem' \n"); 
		res.append("      style='margin:0px;' target='"+propName+"IFrame'>\n");
		res.append("<input type='hidden' name='readRequest' value='true'>\n");
		res.append("<input type='hidden' name='wfemCmd' value=''>\n");
		res.append("<input type='hidden' name='BrowserInstance' value='"+template.getBrowserInstance()+"'>\n");
		res.append("<input type='hidden' name='propertyName' value='"+propName+"'>\n");
		res.append("<iframe src='"+Configuration.getInstance().getWfemlayoutWebApp()+"/blankPage.html' name='"+propName+"IFrame' id='"+propName+"IFrame' style='display:none;'></iframe>\n");
		res.append("<table cellpadding='0' cellspacing='0'><tr><td>\n");
		res.append("<table cellpadding='0' cellspacing='0' width='100%'>\n");
		res.append("<tr>\n");
		
		res.append("<td><input class='"+className+"' type='text' id='"+propName+"FileName' name='"+propName+"FileName' "+size+" "+extraPar+"\n"); 
		res.append("           readonly value='"+propValue.getFileName()+"'></td>\n");
		res.append("<td>\n");
		res.append("<div style='position:relative;' id='"+propName+"Div' name='"+propName+"Div'>\n");
		res.append("<input type='file' name='"+propName+"' id='"+propName+"' size='2' filetypes='"+propValue.getFileTypes()+"' value='"+propValue.getFileName()+"'\n");
		res.append(" 	  style='cursor:hand;width:1px;position:relative;filter:alpha(opacity: 0);opacity:0;z-index:2;'\n");
		res.append("		  onchange='loadFileType(this);'>\n");
		res.append(" 		<div id='fakefile' name='fakefile' style='position:absolute;top:0px;left:0px;z-index:1;'>\n");
		res.append("				<input type='button' value='"+attachlabel+"' class='action' style='width:100%;'>\n");
		res.append("		</div>\n");
		res.append("</div>\n");
		res.append("</td>\n");
		res.append("<td><input name='"+propName+"Clear' id='"+propName+"Clear' type='button' value='"+clearlabel+"' "+clearStyle+" onclick='clearFileType(\""+propName+"\");'></td>\n");
		res.append("<td><input name='"+propName+"Preview' id='"+propName+"Preview' type='button' value='"+previewlabel+"' "+previewStyle+" onclick='showFileType(\""+propName+"\","+propValue.isVirusExamined()+");'></td>\n");
		res.append("</tr>\n");
		res.append("</table>\n");	
		res.append("</td></tr></table>\n");
		res.append("</form>\n");
		
		if(makeHelperAnchor)
			res.append(getInlineProblemsHelperAnchor(propName,propValue,ffAnchorPosition));
		
		res.append(getLastTd());
		return res.toString();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	private String manageTimestampType(String propName, TimestampType propValue, 
			   						   String fieldWidth, String extraPar) throws Exception{

		Matcher mat = null;
	    boolean noDateBoolean = false;
	    boolean noTimeBoolean = false;
	    boolean noSecondsBoolean = false;

	    String exp = "";
	    String apice = "'";

	    boolean makeHelperAnchor = isHelperEnabled(extraPar);

		mat = Patterns.nodate.matcher(extraPar);
	    if(mat.find()){
	      apice = mat.group(1);
	      exp = mat.group(2);
	      extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
	      noDateBoolean = true;
	    }
	    
		mat = Patterns.notime.matcher(extraPar);
	    if(mat.find()){
	      apice = mat.group(1);
	      exp = mat.group(2);
	      extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
	      noTimeBoolean = true;
	    }

		mat = Patterns.noseconds.matcher(extraPar);
	    if(mat.find()){
	      apice = mat.group(1);
	      exp = mat.group(2);
	      extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
	      noSecondsBoolean = true;
	    }

	    exp = "";
	    apice = "'";
		mat = Patterns.onchange.matcher(extraPar);
	    if(mat.find()){
	      apice = mat.group(1);
	      exp = mat.group(2);
	      extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
	    }
	    String ctrlApice = apice.equals("'") ? "\"" : "'";
	    String myOnchange = "setTimestampField("+ctrlApice+propName+ctrlApice+");";
	    extraPar += " onchange="+apice+myOnchange+exp+apice+" ";

	    extraPar = getEventManager("int",propName,extraPar,false);

	    String className = getClassName(propValue);

	    StringBuffer res = new StringBuffer(STRBUF_SIZE);
	    res.append(getFirstTd(fieldWidth));

		if(modality == Template.PRINT_MODALITY){
			res.append(propValue);
			res.append(getLastTd());
			return res.toString();
		}
		
		String dateInputType = "", secondsInputType = "";
	    String gg = "", mm = "", aa = "", hh = "", mi = "", ss = "";

	    gg = propValue.getGG();
	    mm = propValue.getMM();
	    aa = propValue.getAA();
	    hh = propValue.getHH();
	    mi = propValue.getMI();
	    ss = propValue.getSS();
	    dateInputType = "text";
	    secondsInputType = "text";

	    if (noDateBoolean){
	      gg = (propValue.isNull() ? "01" : propValue.getGG());
	      mm = (propValue.isNull() ? "01" : propValue.getMM());
	      aa = (propValue.isNull() ? "1970" : propValue.getAA());
	      hh = (propValue.isNull() ? "" : propValue.getHH());
	      mi = (propValue.isNull() ? "" : propValue.getMI());
	      ss = (propValue.isNull() ? "" : propValue.getSS());
	      dateInputType = "hidden";
	    }

	    if (noSecondsBoolean){
	      gg = (propValue.isNull() ? "" : propValue.getGG());
	      mm = (propValue.isNull() ? "" : propValue.getMM());
	      aa = (propValue.isNull() ? "" : propValue.getAA());
	      hh = (propValue.isNull() ? "" : propValue.getHH());
	      mi = (propValue.isNull() ? "" : propValue.getMI());
	      ss = (propValue.isNull() ? "00" : propValue.getSS());
	      secondsInputType = "hidden";
	    }

	    String data = gg + "-" + mm + "-" + aa + " " + hh + ":" + mi + ":" + ss;
	    if (gg.equals("") || mm.equals("") || aa.equals(""))
	    	data = "";
	    else if(hh.equals("") || mi.equals("") || ss.equals(""))
	    	data = gg + "-" + mm + "-" + aa;

		int yearpt = 25;
		mat = Patterns.yearpt.matcher(extraPar);
		if(mat.find()){
			yearpt = Integer.parseInt(mat.group(2));
			extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
		}
		int daymonthpt = 15;
		mat = Patterns.daymonthpt.matcher(extraPar);
		if(mat.find()){
			daymonthpt = Integer.parseInt(mat.group(2));
			extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
		}
		int timept = 15;
		mat = Patterns.timept.matcher(extraPar);
		if(mat.find()){
			timept = Integer.parseInt(mat.group(2));
			extraPar = extraPar.substring(0, mat.start()) + extraPar.substring(mat.end());
		}
	    
	    res.append("<table border='0' cellspacing='1' cellpadding='1'><tr>");

	    if (makeHelperAnchor)  
	        res.append("<td style='width:1px;' valign='top'>"+getInlineProblemsHelperAnchor(propName,propValue,ffAnchorPosition)+"</td>");
	    
	    res.append("<td>");
	    res.append("<input type='" + dateInputType + "' maxlength='2' style='width:"+daymonthpt+"pt;' ");
	    res.append("value=\"" + gg + "\" ");
	    res.append("class='" + className + "' ");
	    res.append("name='" + propName + "GG' id='" + propName + "GG' ");
	    if (modality == Template.READ_MODALITY)
	      res.append("readonly tabindex='-1' ");
	    if (this.fieldWasReadonly)
	      res.append("wasReadonly=true ");
	    res.append(extraPar + " >");
	    res.append("</td>");

	    res.append("<td>");
	    res.append("<input type='" + dateInputType + "' maxlength='2' style='width:"+daymonthpt+"pt' ");
	    res.append("value=\"" + mm + "\" ");
	    res.append("class='" + className + "' ");
	    res.append("name='" + propName + "MM' id='" + propName + "MM' ");
	    if (modality == Template.READ_MODALITY)
	      res.append("readonly tabindex='-1' ");
	    if (this.fieldWasReadonly)
	      res.append("wasReadonly=true ");
	    res.append(extraPar + " >");
	    res.append("</td>");

	    res.append("<td>");
	    res.append("<input type='" + dateInputType + "' maxlength='4'  style='width:"+yearpt+"pt;' ");
	    res.append("value=\"" + aa + "\" ");
	    res.append("class='" + className + "' ");
	    res.append("name='" + propName + "AA' id='" + propName + "AA' ");
	    if (modality == Template.READ_MODALITY)
	      res.append("readonly tabindex='-1' ");
	    if (this.fieldWasReadonly)
	      res.append("wasReadonly=true ");
	    res.append(extraPar + " >");
	    res.append("</td>");
	    
	    if (modality != Template.READ_MODALITY && !noDateBoolean){
		  res.append("<td id='"+propName+"TimestampAnchor' name='"+propName+"TimestampAnchor'>");
		  res.append("</td>");
		  
	      res.append("<td valign='center'>");
	      res.append("<table width='100%' border='0' cellspacing='0' cellpadding='0'>");

	      res.append("<tr><td valign='center'>");
	      res.append("<img tabindex='-1' src='"+Template.CONTROLLER_CALL_CMD+"=getImage&fileName=openCalendar.gif' ");
	      res.append("name='" + propName + "_imgSrc' id='" + propName + "_imgSrc' ");
		  res.append("onclick='calendar.output=\""+propName+"\";calendar.select(document.all(\""+propName+"\"),\""+propName+"TimestampAnchor\",\"dd-MM-yyyy\");' style='cursor: hand;'>");
	      res.append("</td></tr>");

	      res.append("<tr><td>");
	      res.append("<img tabindex='-1' src='" + Template.CONTROLLER_CALL_CMD+"=getImage&fileName=clearCalendar.gif' ");
	      res.append("onclick='hideHelperAnchor(\""+propName+"\");clearDateField(\""+propName+ "\");' style='cursor: hand;'>");
	      res.append("</td></tr>");

	      res.append("</table>");
	      res.append("</td>");
	    }else{
	      res.append("<td>&nbsp;</td>");
	    }

	    if(!noTimeBoolean){
		    res.append("<td>");
		    res.append("<input type='text' maxlength='2' style='width:"+timept+"pt;' ");
		    res.append("value=\"" + hh + "\" ");
		    res.append("class='" + className + "' ");
		    res.append("name='" + propName + "HH' id='" + propName + "HH' ");
		    if (modality == Template.READ_MODALITY)
		      res.append("readonly tabindex='-1' ");
		    if (this.fieldWasReadonly)
		      res.append("wasReadonly=true ");
		    res.append(extraPar + " >");
		    res.append("</td>");
	
		    res.append("<td>");
		    res.append("<input type='text' maxlength='2' style='width:"+timept+"pt;' ");
		    res.append("value=\"" + mi + "\" ");
		    res.append("class='" + className + "' ");
		    res.append("name='" + propName + "MI' id='" + propName + "MI' ");
		    if (modality == Template.READ_MODALITY)
		      res.append("readonly tabindex='-1' ");
		    if (this.fieldWasReadonly)
		      res.append("wasReadonly=true ");
		    res.append(extraPar + " >");
		    res.append("</td>");
	
		    res.append("<td>");
		    res.append("<input type='" + secondsInputType + "' maxlength='2' style='width:"+timept+"pt;' ");
		    res.append("value=\"" + ss + "\" ");
		    res.append("class='" + className + "' ");
		    res.append("name='" + propName + "SS' id='" + propName + "SS' ");
		    if (modality == Template.READ_MODALITY)
		      res.append("readonly tabindex='-1' ");
		    if (this.fieldWasReadonly)
		      res.append("wasReadonly=true ");
		    res.append(extraPar + " >");
		    res.append("</td>");
	    }

	    res.append("<input type='hidden' class='"+className+"' name='"+propName+"' id='"+propName+"' isTime='true' ");
	    if(noDateBoolean)
	   		res.append(" noDate='true' ");
	    if(noTimeBoolean)
	   		res.append(" noTime='true' ");
   		res.append("value=\""+data+"\">");
	    res.append("</tr></table>");

	    res.append(getLastTd());
	    return res.toString();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getProblemsHelperAnchor(String anchorName, String propName, 
											AbstractType propValue, int anchorPosition) {
		StringBuffer res = new StringBuffer(STRBUF_SIZE);
		
		if(propValue == null)
			return res.toString();
		
		if(propValue.hasTypeErrors() ||
		   (propValue.hasTypeWarnings() && !propValue.isSkippable())){
			
		    res.append("<iframe id='"+anchorName+"DivViewPort' src='"+Configuration.getInstance().getWfemlayoutWebApp()+"/blankPage' scrolling='no' frameborder='0' style='position:absolute; top:0px; left:0px; display:none; height: 15px; width: 15px'></iframe>");
			res.append("<div id='"+anchorName+"HelperAnchor' name='"+anchorName+"HelperAnchor' ");
			res.append("anchor=true anchorPosition="+anchorPosition+" anchorName='"+anchorName+"' ");
			res.append("style='display: none; position:absolute; z-index:10;'>\n");
			res.append("<script>\n");
			
			if(propValue.hasTypeErrors()){
				res.append("document.getElementById('"+anchorName+"HelperAnchor').messages=new Array();\n");
				List errors = propValue.getTypeErrors();
				for(int i=0;i<errors.size();i++){
					TypeError error = (TypeError)errors.get(i);
					String msg = resolveTemplateProperty(error);
					msg = Tools.convertSpecialChars(msg);
					res.append("document.getElementById('"+anchorName+"HelperAnchor').messages["+i+"]=\""+msg+"\";\n");
				}
			}else if(propValue.hasTypeWarnings() && !propValue.isSkippable()){
				res.append("document.getElementById('"+anchorName+"HelperAnchor').messages=new Array();\n");
				List warnings = propValue.getTypeWarnings();
				for(int i=0;i<warnings.size();i++){
					TypeWarning warning = (TypeWarning)warnings.get(i);
					String msg = resolveTemplateProperty(warning);
					msg = Tools.convertSpecialChars(msg);
					res.append("document.getElementById('"+anchorName+"HelperAnchor').messages["+i+"]=\""+msg+"\";");
				}
			}
			res.append("</script>\n");
			
			if(propValue.hasTypeErrors()){
				res.append("<table border='0' cellspacing='0' cellpadding='0'><tr><td>");
				res.append("<img src='"+Template.CONTROLLER_CALL_CMD+"=getImage&fileName=typeError.gif' style='cursor:hand;' ");
				res.append("onclick='showHelper(\""+anchorName+"\",\"Err\",\""+propName+"\");'\">");
			}else if(propValue.hasTypeWarnings() && !propValue.isSkippable()){
				res.append("<table border='0' cellspacing='0' cellpadding='0'><tr><td>");
				res.append("<img src='"+Template.CONTROLLER_CALL_CMD+"=getImage&fileName=typeWarning.gif' style='cursor:hand;' ");
				res.append("onclick='showHelper(\""+anchorName+"\",\"War\",\""+propName+"\");'\">");
			}
			
			res.append("</td></tr></table>");
			res.append("</div>");
		}
		
		if(propValue.hasTypeMessages()){
			
			res.append("<div id='"+anchorName+"MsgHelperAnchor' name='"+anchorName+"MsgHelperAnchor' msgAnchor=true anchorPosition="+anchorPosition+" anchorName='"+anchorName+"' ");
			res.append("style='display: none; position:absolute; z-index:0;'>");
			res.append("<script>\n");

			res.append("document.getElementById('"+anchorName+"MsgHelperAnchor').messages=new Array();\n");
			List messages = propValue.getTypeMessages();
			for(int i=0;i<messages.size();i++){
				TypeMessage message = (TypeMessage)messages.get(i);
				String msg = resolveTemplateProperty(message);
				msg = Tools.convertSpecialChars(msg);
				res.append("document.getElementById('"+anchorName+"MsgHelperAnchor').messages["+i+"]=\""+msg+"\";");
			}
			res.append("</script>\n");
			
			res.append("<table border='0' cellspacing='0' cellpadding='0'><tr><td>");
			
			res.append("<img src='"+Template.CONTROLLER_CALL_CMD+"=getImage&fileName=typeMessage.gif' style='cursor:hand;' ");
			res.append("onclick='showHelper(\""+anchorName+"\",\"Msg\",\""+propName+"\");'\">");

			res.append("</td></tr></table>");
			res.append("</div>");
			
		}
		return res.toString();
	}	
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getInlineProblemsHelperAnchor(String propName, AbstractType propValue, int anchorPosition) {

		StringBuffer res = new StringBuffer(STRBUF_SIZE);
		
		if(propValue == null)
			return res.toString();
		
		if(propValue.hasTypeErrors() ||
		   (propValue.hasTypeWarnings() && !propValue.isSkippable())){
			
 			String imageName = null;
 			String jsShowHelper = null;
			if(propValue.hasTypeErrors()){
				imageName = "typeError";
				jsShowHelper = "showInlineErrHelper";
			}else if(propValue.hasTypeWarnings() && !propValue.isSkippable()){
				imageName = "typeWarning";
				jsShowHelper = "showInlineWarHelper";
			}
			
 			res.append("<div id='"+propName+"HelperAnchor' name='"+propName+"HelperAnchor' "+(template.isInGrid() ? "inGrid=true":"")+
							" style='display:inline;position:relative;top:0px;left:0px;z-index:10;'>");
 			res.append("  <div style='position:absolute;left:-5px;top:-8px;height:15px;width:15px;z-index:20;'>"+
			 		   "		<img id='"+propName+"HelperAnchorImg' name='"+propName+"HelperAnchorImg' "+
					   "			 src='"+Template.CONTROLLER_CALL_CMD+"=getImage&fileName="+imageName+".gif' "+
					   "			 style='cursor:hand;' anchorName='"+propName+"' propName='"+propName+"' "+
					   "			 onclick='"+jsShowHelper+"(this);'>"+
 					   "  </div>");
		    res.append("</div>");
		    
			res.append("\n<script>\n");
			if(propValue.hasTypeErrors()){
				res.append("document.getElementById('"+propName+"HelperAnchor').messages=new Array();\n");
				List errors = propValue.getTypeErrors();
				for(int i=0;i<errors.size();i++){
					TypeError error = (TypeError)errors.get(i);
					String msg = resolveTemplateProperty(error);
					msg = Tools.convertSpecialChars(msg);
					res.append("document.getElementById('"+propName+"HelperAnchor').messages.push(\""+msg+"\");\n");
				}
			}else if(propValue.hasTypeWarnings() && !propValue.isSkippable()){
				res.append("document.getElementById('"+propName+"HelperAnchor').messages=new Array();\n");
				List warnings = propValue.getTypeWarnings();
				for(int i=0;i<warnings.size();i++){
					TypeWarning warning = (TypeWarning)warnings.get(i);
					String msg = resolveTemplateProperty(warning);
					msg = Tools.convertSpecialChars(msg);
					res.append("document.getElementById('"+propName+"HelperAnchor').messages.push(\""+msg+"\");\n");
				}
			}
			res.append("</script>\n");
		}
		
		if(propValue.hasTypeMessages() && !template.isInGrid()){

 			res.append("<div id='"+propName+"HelperAnchorMsg' name='"+propName+"HelperAnchorMsg' "+
 							"style='display:inline;position:relative;top:0px;left:0px;z-index:10;'>");
 			res.append("   <div style='position:absolute;left:5px;top:5px;height:15px;width:15px;z-index:20;'>"+
 					   "		<img id='"+propName+"HelperAnchorImgMsg' name='"+propName+"HelperAnchorImgMsg' "+
					   "		     src='"+Template.CONTROLLER_CALL_CMD+"=getImage&fileName=typeMessage.gif' "+
					   "			 style='cursor:hand;' anchorName='"+propName+"' propName='"+propName+"' "+
					   "			 onclick='showInlineMsgHelper(this);'>"+
					   "   </div>");
			res.append("</div>");
			
			res.append("\n<script>\n");
			res.append("document.getElementById('"+propName+"HelperAnchorMsg').messages=new Array();\n");
			List messages = propValue.getTypeMessages();
			for(int i=0;i<messages.size();i++){
				TypeMessage message = (TypeMessage)messages.get(i);
				String msg = resolveTemplateProperty(message);
				msg = Tools.convertSpecialChars(msg);
				res.append("document.getElementById('"+propName+"HelperAnchorMsg').messages.push(\""+msg+"\");\n");
			}
			res.append("</script>\n");
			
		}
		return res.toString();
	}
	
}
