package com.atosorigin.wfem.wlt;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.layout.WLTPage;
import com.atosorigin.wfem.layout.WLTPageIntf;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.AbstractTypeError;
import com.atosorigin.wfem.types.DateType;
import com.atosorigin.wfem.types.TimestampType;
import com.atosorigin.wfem.types.TypeError;
import com.atosorigin.wfem.types.TypeMessage;
import com.atosorigin.wfem.types.TypeWarning;

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public class PageRenderer extends WLTPage implements WLTPageIntf{

	protected static final String paramPattern="\\s*=\\s*(['\"])(.*?)\\1";
	protected static final String stylePattern="\\s*:\\s*(.*?);";

	private String propName = "";
	private String pars = "";
		
	/**************************************************************************************************/
	/**************************************************************************************************/
	protected static Pattern compilePattern(String parName){
		return Pattern.compile("\\s*"+parName+paramPattern,Pattern.CASE_INSENSITIVE);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	protected static Pattern compileStylePattern(String parName){
		return Pattern.compile("\\s*"+parName+stylePattern,Pattern.CASE_INSENSITIVE);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String header() {
		this.pars = "";
		return new HeaderRenderer(this).html();
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String footer() {
		this.pars = "";
		return new FooterRenderer(this).html();
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String messagesAndErrors() {
		this.pars = "";
		return new MsgErrRenderer(this).html();
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String hidden(String propName) {
		if(!template.getPrefix().equals(""))
			propName = template.getPrefix() + "_" + propName;
		this.pars = "";
		return new HiddenRenderer(this).html(propName);
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String action(String actionName, String pars) {
		if(pars == null)
			pars = "";
		this.pars = pars+" ";
		return new ActionRenderer(this).html(actionName);
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String field(String propName, String pars) {
		if(!template.getPrefix().equals(""))
			propName = template.getPrefix() + "_" + propName;
		this.propName = propName;
		if(pars == null)
			pars = "";
		this.pars = pars+" ";
		
		try{
			TypeRenderer typeRenderer = new TypeRenderer(this);
			return typeRenderer.getHtml();
		}catch(Exception e){
			return "<table><tr><td>"+e.toString()+"</td></tr></table>";
		}
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String grid(String propName, String pars) {
		if(!template.getPrefix().equals(""))
			propName = template.getPrefix() + "_" + propName;
		this.propName = propName;
		if(pars == null)
			pars = "";
		this.pars = pars+" ";
		
		try{
			GridRenderer gridRenderer = new GridRenderer(this);
			return gridRenderer.getHtml();
		}catch(Exception e){
			return "<table><tr><td>"+e.toString()+"</td></tr></table>";
		}
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String inlineAnchor(String propName, AbstractType propValue) {
		if(getTemplate().getModality() == Template.PRINT_MODALITY)
			return "";
		
		StringBuffer res = new StringBuffer();
		
		if(propValue == null)
			return res.toString();
		
		if(propValue.hasTypeErrors() ||
		   (propValue.hasTypeWarnings() && !propValue.isSkippable())){
			
 			String imageName = null;
 			String jsShowHelper = null;
			if(propValue.hasTypeErrors()){
				imageName = "typeError.png";
				jsShowHelper = "showErrHelper";
			}else if(propValue.hasTypeWarnings() && !propValue.isSkippable()){
				imageName = "typeWarning.png";
				jsShowHelper = "showWarHelper";
			}
			
			String left = "-10px";
			String top = "-10px";
			if(template.isInGrid()){
				left = "-1px";
				top = "0px";
				if(propValue instanceof DateType || propValue instanceof TimestampType)
					left = "-3px";
			}
 			res.append("<div id='"+propName+"HelperAnchor' style='position:relative;float:right;z-index:2;'>");
 			res.append(  "<div style='position:absolute;left:"+left+";top:"+top+";'>"+
			 		   		"<img src='"+template.getWfemLayoutWebApp()+"/images/"+imageName+"' "+
			 		   			"style='cursor:pointer;' onclick='__fieldIntf."+jsShowHelper+"(\""+propName+"\");'>"+
 					     "</div>");
		    res.append("</div>");
		    
			res.append("\n<script>\n");
			if(propValue.hasTypeErrors()){
				res.append("document.getElementById('"+propName+"HelperAnchor').messages=new Array();\n");
				List errors = propValue.getTypeErrors();
				for(int i=0;i<errors.size();i++){
					TypeError error = (TypeError)errors.get(i);
					String msg = resolveTypeError(error);
					msg = msg.replaceAll("\"", "&quot;").replaceAll("\\n","<br>");
					res.append("document.getElementById('"+propName+"HelperAnchor').messages.push(\""+msg+"\");\n");
				}
			}else if(propValue.hasTypeWarnings() && !propValue.isSkippable()){
				res.append("document.getElementById('"+propName+"HelperAnchor').messages=new Array();\n");
				List warnings = propValue.getTypeWarnings();
				for(int i=0;i<warnings.size();i++){
					TypeWarning warning = (TypeWarning)warnings.get(i);
					String msg = resolveTypeError(warning);
					msg = msg.replaceAll("\"", "&quot;").replaceAll("\\n","<br>");					
					res.append("document.getElementById('"+propName+"HelperAnchor').messages.push(\""+msg+"\");\n");
				}
			}
			res.append("</script>\n");
		}
		return res.toString();
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String inlineMsgAnchor(String propName, AbstractType propValue) {
		if(getTemplate().getModality() == Template.PRINT_MODALITY)
			return "";
		
		StringBuffer res = new StringBuffer();
		
		if(propValue == null)
			return res.toString();
		
		if(propValue.hasTypeMessages() && !template.isInGrid()){

			String left = "10";
			if(propValue instanceof DateType || propValue instanceof TimestampType)
				left = "23";
 			res.append("<div id='"+propName+"HelperAnchorMsg' style='position:relative;float:right;z-index:2;'>");
 			res.append(  "<div style='position:absolute;left:"+left+"px;top:3px;'>"+
					   		"<img src='"+template.getWfemLayoutWebApp()+"/images/typeMessage.png' "+
							   			"style='cursor:pointer;' propName='"+propName+"' "+
							   			"onclick='__fieldIntf.showMsgHelper(\""+propName+"\");'>"+
 					     "</div>");
		    res.append("</div>");
		    
			res.append("\n<script>\n");
			res.append("document.getElementById('"+propName+"HelperAnchorMsg').messages=new Array();\n");
			List messages = propValue.getTypeMessages();
			for(int i=0;i<messages.size();i++){
				TypeMessage message = (TypeMessage)messages.get(i);
				String msg = resolveTypeError(message);
				msg = msg.replaceAll("\"", "&quot;").replaceAll("\\n","<br>");
				res.append("document.getElementById('"+propName+"HelperAnchorMsg').messages.push(\""+msg+"\");\n");
			}
			res.append("</script>\n");
			
		}
		return res.toString();
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String component(String componentType,  String componentName, String pars) {
		if(pars == null)
			pars = "";
		this.pars = pars+" ";
		return "";
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	protected ParamValue getPar(Pattern pattern, String defValue){
		return getPar(pattern, defValue, true);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	protected ParamValue getPar(Pattern pattern, String defValue, boolean removePar){
		ParamValue result = new ParamValue("","");
		if(getPars() == null)
			return result;
		try{
			Matcher mat = pattern.matcher(getPars());
			if(mat.find()){
				result = new ParamValue(mat.group(2),mat.group(1));
				if(removePar)
					this.pars = getPars().substring(0,mat.start())+getPars().substring(mat.end());
			}else{
				return new ParamValue(defValue,"");
			}
		}catch(Exception e){ e.printStackTrace(); }
		return result;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getPropName() {
		return this.propName;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getPars() {
		return this.pars;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	protected void setPars(String pars) {
		this.pars = pars;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	protected String resolveTypeError(AbstractTypeError abstractTypeError){
		
		String msg = "";
		Template t;
		
		String key = abstractTypeError.toString();
		if(abstractTypeError instanceof TypeError)
			msg = ffTemplate.getProperty((TypeError)abstractTypeError);
		else if(abstractTypeError instanceof TypeWarning)
			msg = ffTemplate.getProperty((TypeWarning)abstractTypeError);
		else if(abstractTypeError instanceof TypeMessage)
			msg = ffTemplate.getProperty((TypeMessage)abstractTypeError);
		
		if(msg.equals(key))
			t = template;
		else
			t = ffTemplate;
		
		if(abstractTypeError instanceof TypeError)
			return t.getProperty((TypeError)abstractTypeError);
		else if(abstractTypeError instanceof TypeWarning)
			return t.getProperty((TypeWarning)abstractTypeError);
		else if(abstractTypeError instanceof TypeMessage)
			return t.getProperty((TypeMessage)abstractTypeError);
		else
			return "";
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public static String getNum(String par){
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
	public static String getMeasure(String par){
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

}
