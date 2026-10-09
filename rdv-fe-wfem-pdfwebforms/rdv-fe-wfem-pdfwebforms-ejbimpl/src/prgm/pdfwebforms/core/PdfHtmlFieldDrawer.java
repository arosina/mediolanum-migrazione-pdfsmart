package prgm.pdfwebforms.core;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.atosorigin.wfem.coddesc.CodDescData;
import com.atosorigin.wfem.coddesc.CodDescDataList;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.DoubleType;
import com.atosorigin.wfem.types.TypeError;
import com.atosorigin.wfem.util.Tools;
import com.itextpdf.text.pdf.AcroFields;

import prgm.pdfwebforms.model.PdfModel;

/**************************************************************************************************
 * @author: Ricotti Corrado
**************************************************************************************************/
public class PdfHtmlFieldDrawer{

	private static final String DOCUMENTPUNTO = "document.";
	
	private static final String S_APICE_VALUE_UGUALE_APICE = "' value='";
	private static final String S_APICE_FIELDNAME_UGUALE_APICE = "' fieldName='";
	private static final String S_DATATYPE_UGUALE_APICE = "datatype='";
	private static final String S_PARENTESICHIUSA_PUNTOEVIRGOLA_SLASH_SCRIPT = "');</script>";
	private static final String S_CHECK_APICE_CONTAINERID_UGUALE_APICE = "Check' containerId='";
	private static final String S_CHECK_APICE_NAME_UGUALE_APICE = "Check' name='";
	private static final String S_FONTSIZE_DUEPUNTI = "font-size:";
	private static final String S_READONLY_ISREADONLY_UGUALE_TRUE = "readonly isreadonly='true' ";
	private static final String S_DIV_ID_UGUALE_APICE = "<div id='";
	private static final String S_INPUT_TYPE_HIDDEN_NAME = "<input type='hidden' name='";
	private static final String S_INPUT_TYPE_TEXT = "<input type='text' ";
	
	private ArrayList<String> radios = new ArrayList<String>();
	private ArrayList<String> combos = new ArrayList<String>();
	private ArrayList<String> checks = new ArrayList<String>();
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	protected static String drawLabelField(Template t, PdfModel pdfModel, PdfFieldInfos fi,
									       AbstractType field, int w, int h){
		
		String style = "";
		if(fi.border)
			style="border: solid 1px black;";
		if(style.length() > 0)
			style = "style='"+style+"'";
		
		StringBuffer res = new StringBuffer();
		res.append("<table aria-hidden='true' class='pdfLabelField' height='100%'><tr>");
		res.append("<td valign='middle' align='"+fi.align+"' "+style+">");
       	res.append(Tools.stringToHTMLString(field.toString()));
		res.append("</td>");
		res.append("</tr></table>");
		return res.toString();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	protected static String drawSlaveField(Template t, PdfModel pdfModel, PdfFieldInfos fi, 
									       AbstractType field, int w, int h){
		
		if(fi.pdfFieldName.equalsIgnoreCase(PdfPredefinedFields.BARCODE) ||
		   fi.pdfFieldName.equalsIgnoreCase(PdfPredefinedFields.BARCODE_IMAGE) ||
		   fi.pdfFieldName.equalsIgnoreCase(PdfPredefinedFields.BARCODE_VALUE))
			return "";
		
		String style = "";
		if(fi.border)
			style="border: solid 1px black;";
		if(style.length() > 0)
			style = "style='"+style+"'";
		
		int imgSize = w > h ? h : w;

		String imgName = "checkX.png";
		if(fi.checkType == PdfFieldInfos.CHECK_V)
			imgName = "checkV.png";
		
		StringBuffer res = new StringBuffer();
		res.append("<table width='"+w+"px' height='"+h+"px'><tr>");
		switch(fi.fieldType){
	        case AcroFields.FIELD_TYPE_RADIOBUTTON:
	    		res.append("<td align='center'>");
	    		if(field.toString().equals(fi.expValue))
	    			res.append("<img width='"+imgSize+"px' height='"+imgSize+"px' src='"+t.getWebApp()+"/images/"+imgName+"'>");
	    		res.append("</td>");
	        	break;
	        	
	        case AcroFields.FIELD_TYPE_CHECKBOX:
	    		res.append("<td align='center'>");
	    		if(field.toString().equals("true"))
	    			res.append("<img width='"+imgSize+"px' height='"+imgSize+"px' src='"+t.getWebApp()+"/images/"+imgName+"'>");
	    		res.append("</td>");
	        	break;
	        	
	        case AcroFields.FIELD_TYPE_TEXT:
	    		res.append("<td align='"+fi.align+"' "+style+">");
	        	res.append(Tools.stringToHTMLString(field.toString()));
	    		res.append("</td>");
	        	break;
	        	
	        case AcroFields.FIELD_TYPE_COMBO:
	        case AcroFields.FIELD_TYPE_LIST:
	    		res.append("<td align='"+fi.align+"' "+style+">");
	        	res.append(pdfModel.getPdfData().getDescValue(fi.htmlFieldName));
	    		res.append("</td>");
	        	break;
	        	
	        case AcroFields.FIELD_TYPE_SIGNATURE:
	    		res.append("<td>");
	        	res.append("");
	    		res.append("</td>");
	        	break;
		}    		
		res.append("</tr></table>");
		return res.toString();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	protected String drawRadiobuttonField(Template t, PdfModel pdfModel, PdfFieldInfos fi, String fid, 
		    							  AbstractType field, int w, int h, boolean readonly, int containerId, boolean onSign){
		
		String fname = fi.htmlFieldName;
		
		String tabIndexVal = " tabindex='-1' ";
		if(!readonly)
			tabIndexVal = " tabindex='"+containerId+"' ";

		String imgName = "checkX.png";
		if(fi.checkType == PdfFieldInfos.CHECK_V)
			imgName = "checkV.png";

		int imgSize = w > h ? h : w;
		String img = "<img width='"+imgSize+"px' height='"+imgSize+"px' src='"+t.getWebApp()+"/images/"+imgName+"'";
		if(!fi.expValue.equals(field.toString()) || fi.hidden)
			img += " style='visibility:hidden;'";
		img += ">";

		StringBuffer res = new StringBuffer();
		res.append(S_DIV_ID_UGUALE_APICE+fid+"Radio' name='"+fname+"Radio' containerId='"+containerId+S_APICE_VALUE_UGUALE_APICE+fi.expValue+S_APICE_FIELDNAME_UGUALE_APICE+fi.htmlFieldName+"' "+
						tabIndexVal+
						"class='pdfRadiobuttonField' style='margin:0;width:"+w+"px;text-align:center;height:"+h+"px;'>");
		res.append(img);
		res.append("</div>");
		
		if(onSign)
			return res.toString();
		
		boolean firstField = false;
		if(!radios.contains(fname)){
			firstField = true;
			radios.add(fname);
		}
			
		if(firstField){
			res.append(S_INPUT_TYPE_HIDDEN_NAME+t.getPrefix()+"_"+fi.htmlFieldName+S_APICE_FIELDNAME_UGUALE_APICE+fi.htmlFieldName+"' "+
							  "id='"+fname+"' "+(readonly ? "isreadonly='true' " : "")+"htmltype='radiobutton' "+
							  S_DATATYPE_UGUALE_APICE+fi.dataType+S_APICE_VALUE_UGUALE_APICE+field.toString()+"'>");
		}
		res.append("<script>pdfPageFields.initRadiobutton('"+fname+S_PARENTESICHIUSA_PUNTOEVIRGOLA_SLASH_SCRIPT);
		if(firstField){
			res.append(appendErrors(t, fname, field));
		}
		return res.toString();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	protected String drawCheckboxField(Template t, PdfModel pdfModel, PdfFieldInfos fi, String fid,
		    					  	   AbstractType field, int w, int h, boolean readonly, int containerId, boolean onSign){

		String fname = fi.htmlFieldName;

		String tabIndexVal = " tabindex='-1' ";
		if(!readonly)
			tabIndexVal = " tabindex='"+containerId+"' ";

		String imgName = "checkX.png";
		if(fi.checkType == PdfFieldInfos.CHECK_V)
			imgName = "checkV.png";
		
		int imgSize = w > h ? h : w;
		String img = "<img width='"+imgSize+"px' height='"+imgSize+"px' src='"+t.getWebApp()+"/images/"+imgName+"'";
		if(!"true".equals(field.toString()) || fi.hidden)
			img += " style='visibility:hidden;'";
		img += ">";
		
		StringBuffer res = new StringBuffer();
		res.append(S_DIV_ID_UGUALE_APICE+fid+S_CHECK_APICE_NAME_UGUALE_APICE+fname+S_CHECK_APICE_CONTAINERID_UGUALE_APICE+containerId+S_APICE_FIELDNAME_UGUALE_APICE+fi.htmlFieldName+"' "+
						tabIndexVal+
						"class='pdfCheckboxField' style='margin:0;width:"+w+"px;text-align:center;height:"+h+"px;'>");
		res.append(img);
		res.append("</div>");
		
		if(onSign)
			return res.toString();
		
		boolean firstField = false;
		if(!checks.contains(fname)){
			firstField = true;
			checks.add(fname);
		}
		
		if(firstField){
			res.append(S_INPUT_TYPE_HIDDEN_NAME+t.getPrefix()+"_"+fi.htmlFieldName+S_APICE_FIELDNAME_UGUALE_APICE+fi.htmlFieldName+"' "+
					  "id='"+fname+"' "+(readonly ? "isreadonly='true' " : "")+"htmltype='checkbox' "+
					  S_DATATYPE_UGUALE_APICE+fi.dataType+S_APICE_VALUE_UGUALE_APICE+field.toString()+"'>");
		}
		res.append("<script>pdfPageFields.initCheckbox('"+fname+S_PARENTESICHIUSA_PUNTOEVIRGOLA_SLASH_SCRIPT);
		if(firstField){
			res.append(appendErrors(t, fname, field));
		}
		return res.toString();
	}
	
	/***********************************************************************************************/
	private static Pattern CLIENTE_NDG_PATTERN = Pattern.compile("ndgCliente(\\d+)",Pattern.CASE_INSENSITIVE);
	/***********************************************************************************************/
	protected static String drawTextField(Template t, PdfModel pdfModel, PdfFieldInfos fi, String fid,
		    							  AbstractType field, int w, int h, boolean readonly, int containerId, boolean onSign){
		
		String fname = fi.htmlFieldName;
		Matcher mat = PdfPredefinedFields.CLIENTE_NDG_PATTERN.matcher(fname);
		if(mat.matches())
			fi.onlynum = true;
		
		String hid = "id='"+fid+"'";
		String hname = "name='"+t.getPrefix()+"_"+fname+"'";
		String tabIndexVal = " tabindex='-1' ";
		if(!readonly){
			tabIndexVal = " tabindex='"+containerId+"' ";
		}
		
		String hasClone = "";
		if(fi.mainField != null || fi.hasClone)
			hasClone = "hasClone='true' ";
		
		if(fi.mainField != null || onSign)
			hname = "";
		
		int ifontSize = PdfModel.DEFAULT_FONT_SIZE;
		if(h <= PdfModel.DEFAULT_FONT_SIZE){
			ifontSize = h-2;
		}
		String fontSize = S_FONTSIZE_DUEPUNTI+ifontSize+"px;";
		
		String letterSpacing = letterSpacing(w, h, fi); 
		
		String fieldValue = Tools.stringToHTMLString(field.toString());
		boolean multiline = fi.multiline;
		StringBuffer res = new StringBuffer();
   		res.append("<table cellpadding='0' cellspacing='0' height='100%'><tr>");
	    res.append(  "<td>");
	    if(multiline){
			res.append(	"<textarea "+hid+" "+hname+" "+hasClone+" htmltype='textarea' datatype='"+fi.dataType+S_APICE_FIELDNAME_UGUALE_APICE+fi.htmlFieldName+"' "+
									"class='pdfTextField' containerId='"+containerId+"' "+
									(readonly ? S_READONLY_ISREADONLY_UGUALE_TRUE : "")+
									tabIndexVal+
									(fi.onlynum ? "onlynum='true'" : "")+
									(fi.maxLen > 0 ? " maxlength='"+fi.maxLen+"'" : "")+
									"nowrap style='overflow:hidden;"+fontSize+"height:"+h+";width:"+(w-2)+"px;text-align:"+fi.align+";"+letterSpacing+"'>"+
									fieldValue+
									"</textarea>");
	    }else{
			res.append(		S_INPUT_TYPE_TEXT+hid+" "+hname+" "+hasClone+" htmltype='text' datatype='"+fi.dataType+S_APICE_FIELDNAME_UGUALE_APICE+fi.htmlFieldName+"' "+
									"class='pdfTextField' containerId='"+containerId+"' "+
									(readonly ? S_READONLY_ISREADONLY_UGUALE_TRUE : "")+
									"value=\""+fieldValue+"\" "+
									tabIndexVal+
									(fi.onlynum ? "onlynum='true'" : "")+
									(fi.maxLen > 0 ? " maxlength='"+fi.maxLen+"'" : "")+
									"style='"+fontSize+"width:"+(w-2)+"px;text-align:"+fi.align+";"+letterSpacing+"'></input>");
	    }
	    res.append(  "</td>");
		if(pdfModel.isOperatoreMOM() && !readonly && fi.htmlFieldName.startsWith("ndgCliente")){
			Matcher matNdgMom = PdfPredefinedFields.CLIENTE_NDG_PATTERN.matcher(fi.htmlFieldName);
			if(matNdgMom.matches()){
				res.append("<div align='right' style='width:100%;position:absolute;left:10;top:-10;'><img src='"+t.getWebApp()+"/images/lentina.png' style='cursor:pointer;margin:3;' onclick='callRicercaClienteMomEvent("+mat.group(1)+");'></div>");
			}
		}
   		res.append("</tr></table>");
   		
   		if(onSign)
   			return res.toString();
   		
		res.append("<script>pdfPageFields.initText('"+fid+S_PARENTESICHIUSA_PUNTOEVIRGOLA_SLASH_SCRIPT);
		res.append(appendErrors(t, fid, field));
		return res.toString();
	}
	 
	/***********************************************************************************************/
	/***********************************************************************************************/
	protected static String drawDateField(Template t, PdfModel pdfModel, PdfFieldInfos fi, String fid,
		    							  AbstractType field, int w, int h, boolean readonly, int containerId, boolean onSign){
		
		String fname = fi.htmlFieldName;

		String hid = "id='"+fid+"'";
		String hname = "name='"+t.getPrefix()+"_"+fname+"'";
		String tabIndexVal = " tabindex='-1' ";
		if(!readonly){
			tabIndexVal = " tabindex='"+containerId+"' ";
		}
		
		String hasClone = "";
		if(fi.mainField != null || fi.hasClone)
			hasClone = "hasClone='true' ";
		
		if(fi.mainField != null || onSign)
			hname = "";
		
		int ifontSize = PdfModel.DEFAULT_FONT_SIZE;
		if(h <= PdfModel.DEFAULT_FONT_SIZE){
			ifontSize = h-2;
		}
		String fontSize = S_FONTSIZE_DUEPUNTI+ifontSize+"px;";

		String height = "";
		if(h < 20)
			height = "height:"+h+"px;"; 
		
		StringBuffer res = new StringBuffer();
   		res.append("<table cellpadding='0' cellspacing='0' width='100%' height='100%'><tr>");
	    res.append(  "<td align='center'>");
   		res.append(		"<table cellpadding='0' cellspacing='0'><tr>");
   		res.append(			"<td>");
   		res.append(				"<div style='position:relative;margin:0;'>");
		res.append(					"<input id='"+fid+"Mask' tabindex=-1 type='text' class='pdfDateField' style='"+fontSize+height+"' size='10' readonly='readonly'>");
		res.append(					"<div style='margin:0;position:absolute;left:0;top:0;'>");
	    res.append(						"<input type='text' maxlength='10' value='"+field.toString()+"' size='10' "+
	    										"class='pdfDateField' htmltype='text' datatype='"+fi.dataType+S_APICE_FIELDNAME_UGUALE_APICE+fi.htmlFieldName+"' "+
	    										"style='"+fontSize+height+"background:transparent;' "+tabIndexVal+
	    										""+hname+" "+hid+" "+hasClone+" "+
	    										"containerId='"+containerId+"' "+ 
	    										(readonly ? "readonly isreadonly='true'" : "")+">");
		res.append(					"</div>");
		res.append(				"</div>");
		res.append(			"</td>");
	    res.append(  		"<td>");
	    res.append(				"<img id='"+fid+"ImgCal' fieldName='"+fi.htmlFieldName+"ImgCal' src='"+t.getWfemLayoutWebApp()+"/private/calendar/images/openCalendar.png' "+
	    								"style='"+height+"margin:0;"+(readonly?"visibility:hidden;":"cursor:pointer;")+"'>");
	    res.append(  		"</td>");
		res.append(		"</tr></table>");
	    res.append(  "</td>");
   		res.append("</tr></table>");
   		
   		if(onSign)
   			return res.toString();
   		
		res.append("<script>pdfPageFields.initDate('"+fid+S_PARENTESICHIUSA_PUNTOEVIRGOLA_SLASH_SCRIPT);
		res.append(appendErrors(t, fid, field));
		return res.toString();
	}
	 
	/***********************************************************************************************/
	/***********************************************************************************************/
	protected static String drawIntegerField(Template t, PdfModel pdfModel, PdfFieldInfos fi, String fid,
		    							    AbstractType field, int w, int h, boolean readonly, int containerId, boolean onSign){
		
		String fname = fi.htmlFieldName;
		
		String hid = "id='"+fid+"'";
		String hname = "name='"+t.getPrefix()+"_"+fname+"'";
		String tabIndexVal = " tabindex='-1' ";
		if(!readonly){
			tabIndexVal = " tabindex='"+containerId+"' ";
		}
		
		String hasClone = "";
		if(fi.mainField != null || fi.hasClone)
			hasClone = "hasClone='true' ";

		if(fi.mainField != null || onSign)
			hname = "";
		
		
		int ifontSize = PdfModel.DEFAULT_FONT_SIZE;
		String fontSize = "";
		if(h <= PdfModel.DEFAULT_FONT_SIZE){
			ifontSize = h-2;
			fontSize = S_FONTSIZE_DUEPUNTI+ifontSize+"px;";
		}
		
		String currency = "";
		if(fi.currency.length() > 0){
			w -= ifontSize+4;
			currency = Tools.stringToHTMLString(fi.currency);
		}
		
		String letterSpacing = letterSpacing(w, h, fi); 
		
		StringBuffer res = new StringBuffer();
   		res.append("<table cellpadding='0' cellspacing='0' height='100%'><tr>");
   		if(fi.currency.length() > 0 && fi.currencyPrepend){
   		    res.append(  "<td style='"+fontSize+"padding-right:2px;padding-left:2px;'>"+currency+"</td>");
   		}
	    res.append(  "<td>");
		res.append(		S_INPUT_TYPE_TEXT+hid+" "+hname+" "+hasClone+" "+
								"class='pdfIntegerField' htmltype='text' datatype='"+fi.dataType+S_APICE_FIELDNAME_UGUALE_APICE+fi.htmlFieldName+"' "+
								(readonly ? S_READONLY_ISREADONLY_UGUALE_TRUE : "")+
								"value=\""+field.toString()+"\" "+
    							"scale='"+fi.doubleScale+"' "+
								"containerId='"+containerId+"' "+ 
    							"onlynum='true' "+
								tabIndexVal+
								(fi.maxLen > 0 ? " maxlength='"+fi.maxLen+"'" : "")+
								"style='"+fontSize+"width:"+(w-2)+"px;text-align:"+fi.align+";"+letterSpacing+"'>");
	    res.append(  "</td>");
   		if(fi.currency.length() > 0 && !fi.currencyPrepend){
   		    res.append(  "<td style='"+fontSize+"padding-right:2px;padding-left:2px;'>"+currency+"</td>");
   		}
   		res.append("</tr></table>");
   		
   		if(onSign)
   			return res.toString();
   		
		res.append("<script>pdfPageFields.initText('"+fid+S_PARENTESICHIUSA_PUNTOEVIRGOLA_SLASH_SCRIPT);
		res.append(appendErrors(t, fid, field));
		return res.toString();
	}
	 
	/***********************************************************************************************/
	/***********************************************************************************************/
	protected static String drawDoubleField(Template t, PdfModel pdfModel, PdfFieldInfos fi, String fid,
		    							    AbstractType field, int w, int h, boolean readonly, int containerId, boolean onSign){
		
		String fname = fi.htmlFieldName;
		
		String hid = "id='"+fid+"'";
		String hname = "name='"+t.getPrefix()+"_"+fname+"'";
		String tabIndexVal = " tabindex='-1' ";
		if(!readonly){
			tabIndexVal = " tabindex='"+containerId+"' ";
		}
		
		String hasClone = "";
		if(fi.mainField != null || fi.hasClone)
			hasClone = "hasClone='true' ";
		
		if(fi.mainField != null || onSign)
			hname = "";
		
		int ifontSize = PdfModel.DEFAULT_FONT_SIZE;
		String fontSize = "";
		if(h <= PdfModel.DEFAULT_FONT_SIZE){
			ifontSize = h-2;
			fontSize = S_FONTSIZE_DUEPUNTI+ifontSize+"px;";
		}
		
		String currency = "";
		if(fi.currency.length() > 0){
			w -= ifontSize+4;
			currency = Tools.stringToHTMLString(fi.currency);
		}

		String letterSpacing = letterSpacing(w, h, fi); 
		
		String fieldValueAsString = "";
		DoubleType doubleField = (DoubleType)field;
		if(!doubleField.isNull())
			fieldValueAsString = doubleField.toScaledString(fi.doubleScale);
		
		StringBuffer res = new StringBuffer();
   		res.append("<table cellpadding='0' cellspacing='0' height='100%'><tr>");
   		if(fi.currency.length() > 0 && fi.currencyPrepend){
   		    res.append(  "<td style='"+fontSize+"padding-right:2px;padding-left:2px;'>"+currency+"</td>");
   		}
	    res.append(  "<td>");
		res.append(		S_INPUT_TYPE_TEXT+hid+" "+hname+" "+hasClone+" "+
								"class='pdfDoubleField' htmltype='text' datatype='"+fi.dataType+S_APICE_FIELDNAME_UGUALE_APICE+fi.htmlFieldName+"' "+
								(readonly ? S_READONLY_ISREADONLY_UGUALE_TRUE : "")+
								"value=\""+fieldValueAsString+"\" "+
    							"scale='"+fi.doubleScale+"' "+
								"containerId='"+containerId+"' "+ 
								tabIndexVal+
								(fi.maxLen > 0 ? " maxlength='"+fi.maxLen+"'" : "")+
								"style='"+fontSize+"width:"+(w-2)+"px;text-align:"+fi.align+";"+letterSpacing+"'>");
	    res.append(  "</td>");
   		if(fi.currency.length() > 0 && !fi.currencyPrepend){
   		    res.append(  "<td style='"+fontSize+"padding-right:2px;padding-left:2px;'>"+currency+"</td>");
   		}
   		res.append("</tr></table>");
   		
   		if(onSign)
   			return res.toString();
   		
		res.append("<script>pdfPageFields.initDouble('"+fid+S_PARENTESICHIUSA_PUNTOEVIRGOLA_SLASH_SCRIPT);
		res.append(appendErrors(t, fid, field));
		return res.toString();
	}
	 
	/***********************************************************************************************/
	/***********************************************************************************************/
	protected String drawComboField(Template t, PdfModel pdfModel, PdfFieldInfos fi, String fid,
		    						AbstractType field, int w, int h, boolean readonly, int containerId, boolean onSign){
		
		String fname = fi.htmlFieldName;
		
		String tabIndexVal = " tabindex='-1' ";
		if(!readonly){
			tabIndexVal = " tabindex='"+containerId+"' ";
		}
		
		String hasClone = "";
		if(fi.mainField != null || fi.hasClone)
			hasClone = "hasClone='true' ";
		
		String fontSize = "";
		if(h <= PdfModel.DEFAULT_FONT_SIZE)
			fontSize = S_FONTSIZE_DUEPUNTI+(h-2)+"px;";
		
		StringBuffer res = new StringBuffer();
   		res.append("<table cellpadding='0' cellspacing='0' height='100%'><tr>");
	    res.append(  "<td>");
	    res.append(		"<select id='"+fid+"Combo' "+hasClone+" "+" htmltype='combo' datatype='"+fi.dataType+S_APICE_FIELDNAME_UGUALE_APICE+fi.htmlFieldName+"' "+
	    						"class='pdfComboField' containerId='"+containerId+"' "+
	    						(readonly ? "isReadonly='true' " : "")+
	    						tabIndexVal+
	    						"style='"+fontSize+"width:"+w+"px;'>");
		ArrayList<CodDescOption> codDescOptions = getCodDescOptions(pdfModel, fi, field);
	    for(int i=0;i<codDescOptions.size();i++){
	    	CodDescOption option = (CodDescOption)codDescOptions.get(i);
		    res.append("<option value=\""+option.cod+"\" "+(option.selected?"selected":"")+" "+(option.valid?"":"style='color:red;'")+">");
		    res.append(         option.desc+"</option>");
	    }
		res.append(		"</select>");
	    res.append(  "</td>");
   		res.append("</tr></table>");
   		
   		if(onSign)
   			return res.toString();
   		
		boolean firstField = false;
		if(!combos.contains(fname)){
			firstField = true;
			combos.add(fname);
		}
   		
		if(firstField){
			String apice = "'";
			if(field.toString().indexOf("'") >= 0)
				apice = "\"";
			res.append(S_INPUT_TYPE_HIDDEN_NAME+t.getPrefix()+"_"+fi.htmlFieldName+S_APICE_FIELDNAME_UGUALE_APICE+fi.htmlFieldName+"' "+
							  "id='"+fname+"' "+(readonly ? "isreadonly='true' " : "")+"htmltype='combo' "+
							  S_DATATYPE_UGUALE_APICE+fi.dataType+"' value="+apice+field.toString()+apice+">");
		}
		res.append("<script>pdfPageFields.initCombo('"+fid+S_PARENTESICHIUSA_PUNTOEVIRGOLA_SLASH_SCRIPT);
		if(firstField)
			res.append(appendErrors(t, fname, field));
		return res.toString();
	}
	 
	/***********************************************************************************************/
	private static final String TESTO_FIRMA_PREFIX = "testofirma";
	private static final String TESTO_FIRMA_CLI_SUFFIX = "Cliente";
	/***********************************************************************************************/
	protected static String drawSignField(Template t, PdfModel pdfModel, PdfFieldInfos fi, 
		    					  		  AbstractType field, int w, int h, boolean readonly, int containerId, int containerWidth,
		    					  		  String label){
		
		String fid = fi.htmlFieldName;

		String tabIndexVal = " tabindex='-1' ";
		if(!readonly)
			tabIndexVal = " tabindex='0' ";

		int imgSize = w > h ? h : w;
		String img = "<img width='"+imgSize+"px' height='"+imgSize+"px' src='"+t.getWebApp()+"/images/checkV.png'";
		if(!"true".equals(field.toString()))
			img += " style='visibility:hidden;'";
		img += ">";
		
		String hname = "name='"+t.getPrefix()+"_"+fid+"' ";
		if(readonly)
			hname = "";
		
		boolean isFirmata = field.toString().equals("true");
		StringBuilder res = new StringBuilder();
		res.append("<table width='100%' height='100%'><tr>");
		res.append("<td>");

		String signDescr = "";
		if(!readonly) {
			Matcher mat = PdfPredefinedFields.CLIENTE_FIRMA_N_DI_M_PATTERN.matcher(fi.pdfFieldName);
			if(mat.matches()) {
				PdfFieldInfos fieldTestofirma = pdfModel.getPdfData().getPdfInfos().findFieldInfoByPdfName(TESTO_FIRMA_PREFIX+mat.group(1)+TESTO_FIRMA_CLI_SUFFIX+mat.group(2));
				if(fieldTestofirma != null && !fieldTestofirma.defValue.isEmpty()) {
					signDescr = Tools.stringToHTMLString(fieldTestofirma.defValue.replace("\r"," "))+"\"";
				}else {
					fieldTestofirma = pdfModel.getPdfData().getPdfInfos().findFieldInfoByPdfName(TESTO_FIRMA_PREFIX+mat.group(1)+TESTO_FIRMA_CLI_SUFFIX);
					if(fieldTestofirma != null && !fieldTestofirma.defValue.isEmpty())
						signDescr = Tools.stringToHTMLString(fieldTestofirma.defValue.replace("\r"," "))+"\"";
				}
			}else {
				mat = PdfPredefinedFields.AGENTE_FIRMA_N_PATTERN.matcher(fi.pdfFieldName);
				if(mat.matches()) {
					PdfFieldInfos fieldTestofirma = pdfModel.getPdfData().getPdfInfos().findFieldInfoByPdfName(TESTO_FIRMA_PREFIX+mat.group(1)+"Agente");
					if(fieldTestofirma != null && !fieldTestofirma.defValue.isEmpty())
						signDescr = Tools.stringToHTMLString(fieldTestofirma.defValue.replace("\r"," "))+"\"";
				}
			}
		}
		String ariaLabelNotSigned = "Stai apponendo una firma, "+(signDescr.isEmpty()?"":"una volta ascoltato il contenuto ")+"per firmare premi barra o invio.";
		String ariaLabelSigned = "Firma già apposta da altro soggetto";
		if(!readonly) {
			ariaLabelSigned = "Hai già apposto la tua firma su questo elemento.";
			if(!signDescr.isEmpty())
				ariaLabelSigned += " Premi barra o invio per riascoltare il contenuto.";
		}
		res.append("<div role='checkbox' id='"+fid+S_CHECK_APICE_NAME_UGUALE_APICE+fid+S_CHECK_APICE_CONTAINERID_UGUALE_APICE+containerId+S_APICE_FIELDNAME_UGUALE_APICE+fid+"' "+
						tabIndexVal+
						"class='pdfCheckSignField pdfSignFieldIndicator' style='width:"+w+"px;height:"+h+"px;'"+
						"aria-describedby='"+(isFirmata?"":fid+"SignDescr")+"' "+
						"aria-label='"+(isFirmata?ariaLabelSigned:ariaLabelNotSigned)+"' "+
						"aria-checked='"+field.toString()+"'>");
		res.append("<span id='"+fid+"AriaLabelSigned' class='visually-hidden'>"+ariaLabelSigned+"</span>");
		res.append("<span id='"+fid+"SignDescr' class='visually-hidden'>"+signDescr+"</span>");
		
		res.append(img);
		res.append("</div>");
		res.append("<input type='hidden' class='signFieldValue' "+hname+"id='"+fid+"' "+(readonly ? S_READONLY_ISREADONLY_UGUALE_TRUE : "isreadonly='false' ")+"htmltype='checkbox' datatype='"+fi.dataType+S_APICE_VALUE_UGUALE_APICE+field.toString()+"'>");
		res.append("<script>pdfPageFields.initSignCheckbox('"+fid+S_PARENTESICHIUSA_PUNTOEVIRGOLA_SLASH_SCRIPT);
		res.append("</td>");
		res.append("<td width='100%' align='left' class='pdfSignLabel'>"+label+"</td>");
		res.append("</tr></table>");
		return res.toString();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	protected static String drawClauseField(Template t, PdfModel pdfModel, PdfFieldInfos fi, 
		    					  			AbstractType field, int w, int h, int containerId){

		String fid = fi.htmlFieldName;

		String tabIndexVal = " tabindex='"+containerId+"' ";

		String imgName = "checkV.png";
		
		int imgSize = w > h ? h : w;
		String img = "<img width='"+imgSize+"px' height='"+imgSize+"px' src='"+t.getWebApp()+"/images/"+imgName+"'";
		if(!"true".equals(field.toString()))
			img += " style='visibility:hidden;'";
		img += ">";

		StringBuffer res = new StringBuffer();
		res.append(S_DIV_ID_UGUALE_APICE+fid+S_CHECK_APICE_NAME_UGUALE_APICE+fid+S_CHECK_APICE_CONTAINERID_UGUALE_APICE+containerId+S_APICE_FIELDNAME_UGUALE_APICE+fid+"' "+
						tabIndexVal+
						"class='pdfCheckboxField pdfSignFieldIndicator' style='margin:0;width:"+w+"px;text-align:center;height:"+h+"px;'>");
		res.append(img);
		res.append("</div>");
		res.append(S_INPUT_TYPE_HIDDEN_NAME+t.getPrefix()+"_"+fi.htmlFieldName+"' id='"+fid+"' htmltype='checkbox' datatype='"+fi.dataType+S_APICE_VALUE_UGUALE_APICE+field.toString()+"'>");
		res.append("<script>pdfPageFields.initCheckbox('"+fid+S_PARENTESICHIUSA_PUNTOEVIRGOLA_SLASH_SCRIPT);
		res.append(appendErrors(t, fid, field));
		return res.toString();
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	private static String appendErrors(Template t, String fid, AbstractType field){
		
		StringBuilder res = new StringBuilder();

		if(!field.hasTypeErrors()){
			res.append("<script>");
			res.append(DOCUMENTPUNTO+fid+"messages=new Array();\n");
			res.append("</script>");
			return res.toString();
		}
		
		res.append("<script>");
		res.append(DOCUMENTPUNTO+fid+"messages=new Array();\n");
		List errors = field.getTypeErrors();
		for(int i=0;i<errors.size();i++){
			TypeError error = (TypeError)errors.get(i);
			if(appendCodedError(error, fid, res))
				continue;
			String msg = t.getProperty(error);
			msg = msg.replaceAll("\"", "&quot;").replaceAll("\\n","<br>");
			res.append(DOCUMENTPUNTO+fid+"messages.push(\""+msg+"\");\n");
		}
		res.append("</script>");
		return res.toString();
	}
		
    /**************************************************************************************************/
	/**************************************************************************************************/
	protected static boolean appendCodedError(TypeError error, String fid, StringBuilder res) {
		if(error.getKey().startsWith(PdfCodedMessage.MESSAGE_CODE_PREFIX)) {
			try {
				String msg = PdfCodedMessage.getMessage(error.getKey(), error.getValues(), "TypeError");
				msg = msg.replace("\"", "&quot;").replace("[\\n\\r]","<br>");
				res.append(DOCUMENTPUNTO+fid+"messages.push(\""+msg+"\");\n");
				return true;
			}catch(Exception e) { /* do nothing */ }
		}
		return false;
	}
	
    /**************************************************************************************************/
	/**************************************************************************************************/
	public static class CodDescOption{
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
	private static ArrayList<CodDescOption> getCodDescOptions(PdfModel pdfModel, PdfFieldInfos fi, AbstractType propValue){
		
    	String emptylabel = pdfModel.getCodDescEmptyValue();
    	
    	ArrayList<CodDescOption> options = new ArrayList<PdfHtmlFieldDrawer.CodDescOption>();
		
        CodDescDataList dataList = fi.codDescDataList;
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

        	if(!data.isValid())
        		continue;
             	
        	String code = data.getCod();
        	if(code == null || code.trim().length() == 0)
        		continue;
        	String desc = data.getDescr();
        	desc = Tools.stringToHTMLString(desc);
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
	private static String letterSpacing(int w, int h, PdfFieldInfos fi){

		if(h <= PdfModel.DEFAULT_FONT_SIZE)
			return "";

		int fontWidth = 7;
		if(fi.monospaced && fi.maxLen > 0){
			try{
				float space = (float)w/fi.maxLen - fontWidth;
				return "letter-spacing:"+(Math.round(space*100.0)/100.0)+"px;";
			}catch(Throwable th){
				return "";
			}
		}
		return "";
	}
}
