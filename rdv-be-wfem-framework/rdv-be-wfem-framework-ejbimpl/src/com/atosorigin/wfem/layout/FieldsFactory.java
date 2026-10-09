package com.atosorigin.wfem.layout;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.loggers.LayoutLogger;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.AbstractTypeError;
import com.atosorigin.wfem.types.TypeError;
import com.atosorigin.wfem.types.TypeMessage;
import com.atosorigin.wfem.types.TypeWarning;
import com.atosorigin.wfem.util.Tools;

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public abstract class FieldsFactory {

	protected LayoutLogger LOG = LayoutLogger.getInstance();

	public static final int INSERT_MODALITY = 1;
	public static final int UPDATE_MODALITY = 2;
	public static final int READ_MODALITY   = 3;

	public static final double NO_LABEL    = 0;
	public static final double LEFT_LABEL  = 1;
	public static final double UP_LABEL    = 2;
	public static final double RIGHT_LABEL = 3;
	public static final double DOWN_LABEL  = 4;
	
	public static final String LEFT_ALIGNED_LABEL  = "left";
	public static final String RIGHT_ALIGNED_LABEL = "right";
		
	public static final int CENTER_ANCHOR 		= 1;
	public static final int TOP_LEFT_ANCHOR 		= 2;
	public static final int TOP_RIGHT_ANCHOR 	= 3;
	public static final int BOTTOM_LEFT_ANCHOR 	= 4;
	public static final int BOTTOM_RIGHT_ANCHOR 	= 5;
	public static final int TOP_CENTER_ANCHOR 	= 6;
	public static final int RIGHT_CENTER_ANCHOR 	= 7;
	public static final int LEFT_CENTER_ANCHOR 	= 8;
	public static final int BOTTOM_CENTER_ANCHOR	= 9;

	protected String prefix = "";
	protected String savedPrefix = "";
	
	protected String webApp;
	protected String pageName;
	protected String labelStyle;
	protected String labelAlign = null;
	protected String fieldAlign = null;
	protected CommandDataModel pageDataModel;
	protected int modality = INSERT_MODALITY;
	protected double labelPosition = NO_LABEL;
	protected Template template;
	protected Template ffTemplate;
	protected String border = "0";
	protected boolean jsCombo = true;

	protected boolean fieldWasReadonly = false;
	
	protected boolean ffUpperCase = true;
	protected int ffAnchorPosition = TOP_RIGHT_ANCHOR;	
	protected String labelCodePrefix = "";

	/**************************************************************************************************/
	/**************************************************************************************************/
	public FieldsFactory(){
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void init(String webApp, String pageName,
					  CommandDataModel pageDataModel, Template template){
	
		this.webApp = webApp;
		setPageName(pageName);
		this.pageDataModel = pageDataModel;
		this.template = template;
		
		ffTemplate = new Template(template.getLangCode(), null, 
								  template.isTemplateCacheEnabled(), template.getRequest());
		ffTemplate.setApplCode(Template.CONTROLLER_APPL_CODE);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public abstract String hiddenField(String propName);

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String numField(String propName) {
		return numField(propName,this.modality,0,"","","");
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String numField(String propName, String extraPar) {
		return numField(propName,this.modality,0,extraPar,"","");
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String numField(String propName, String extraPar, String labelWidth, String fieldWidth) {
		return numField(propName,this.modality,0,extraPar,labelWidth,fieldWidth);
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String numField(String propName, long maxLength, String labelWidth, String fieldWidth) {
		return numField(propName,this.modality,maxLength,"",labelWidth,fieldWidth);
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String numField(String propName, long maxLength) {
		return numField(propName,this.modality,maxLength,"","","");
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String numField(String propName, long maxLength, String extraPar) {
		return numField(propName,this.modality,maxLength,extraPar,"","");
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String numField(String propName, long maxLength, String extraPar, String labelWidth, String fieldWidth) {
		return numField(propName,this.modality,maxLength,extraPar,labelWidth,fieldWidth);
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String numField(String propName, int modality, long maxLength, String labelWidth, String fieldWidth) {
		return numField(propName,modality,maxLength,"",labelWidth,fieldWidth);
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String numField(String propName, int modality, long maxLength) {
		return numField(propName,modality,maxLength,"","","");
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String numField(String propName, int modality, long maxLength, String extraPar) {
		return numField(propName,modality,maxLength,extraPar,"","");
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String numField(String propName, int modality, String labelWidth, String fieldWidth) {
		return numField(propName,modality,0,"",labelWidth,fieldWidth);
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String numField(String propName,int modality) {
		return numField(propName,modality,0,"","","");
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String numField(String propName, int modality, String extraPar) {
		return numField(propName,modality,0,extraPar,"","");
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String numField(String propName, int modality, String extraPar, String labelWidth, String fieldWidth) {
		return numField(propName,modality,0,extraPar,labelWidth,fieldWidth);
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String numField(String propName, int modality, long maxLength, String extraPar, String labelWidth, String fieldWidth) {
		extraPar = "type='num' "+extraPar;
		String res = field(propName,modality,maxLength,extraPar,labelWidth,fieldWidth);
		return res;
	}


	/**************************************************************************************************/
	/**************************************************************************************************/
	public String alfaField(String propName) {
		return alfaField(propName,this.modality,0,"","","");
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String alfaField(String propName, String extraPar) {
		return alfaField(propName,this.modality,0,extraPar,"","");
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String alfaField(String propName, String extraPar, String labelWidth, String fieldWidth) {
		return alfaField(propName,this.modality,0,extraPar,labelWidth,fieldWidth);
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String alfaField(String propName, long maxLength, String labelWidth, String fieldWidth) {
		return alfaField(propName,this.modality,maxLength,"",labelWidth,fieldWidth);
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String alfaField(String propName, long maxLength) {
		return alfaField(propName,this.modality,maxLength,"","","");
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String alfaField(String propName, long maxLength, String extraPar) {
		return alfaField(propName,this.modality,maxLength,extraPar,"","");
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String alfaField(String propName, long maxLength, String extraPar, String labelWidth, String fieldWidth) {
		return alfaField(propName,this.modality,maxLength,extraPar,labelWidth,fieldWidth);
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String alfaField(String propName, int modality, long maxLength, String labelWidth, String fieldWidth) {
		return alfaField(propName,modality,maxLength,"",labelWidth,fieldWidth);
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String alfaField(String propName, int modality, long maxLength) {
		return alfaField(propName,modality,maxLength,"","","");
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String alfaField(String propName, int modality, long maxLength, String extraPar) {
		return alfaField(propName,modality,maxLength,extraPar,"","");
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String alfaField(String propName, int modality, String labelWidth, String fieldWidth) {
		return alfaField(propName,modality,0,"",labelWidth,fieldWidth);
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String alfaField(String propName,int modality) {
		return alfaField(propName,modality,0,"","","");
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String alfaField(String propName, int modality, String extraPar) {
		return alfaField(propName,modality,0,extraPar,"","");
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String alfaField(String propName, int modality, String extraPar, String labelWidth, String fieldWidth) {
		return alfaField(propName,modality,0,extraPar,labelWidth,fieldWidth);
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String alfaField(String propName, int modality, long maxLength, String extraPar, String labelWidth, String fieldWidth) {
		extraPar = "type='alfa' "+extraPar;
		String res = field(propName,modality,maxLength,extraPar,labelWidth,fieldWidth);
		return res;
	}


	/**************************************************************************************************/
	/**************************************************************************************************/
	public String field(String propName) {
		return field(propName,this.modality,0,"","","");
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String field(String propName, String extraPar) {
		return field(propName,this.modality,0,extraPar,"","");
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String field(String propName, String extraPar, String labelWidth, String fieldWidth) {
		return field(propName,this.modality,0,extraPar,labelWidth,fieldWidth);
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String field(String propName, long maxLength, String labelWidth, String fieldWidth) {
		return field(propName,this.modality,maxLength,"",labelWidth,fieldWidth);
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String field(String propName, long maxLength) {
		return field(propName,this.modality,maxLength,"","","");
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String field(String propName, long maxLength, String extraPar) {
		return field(propName,this.modality,maxLength,extraPar,"","");
	}	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String field(String propName, long maxLength, String extraPar, String labelWidth, String fieldWidth) {
		return field(propName,this.modality,maxLength,extraPar,labelWidth,fieldWidth);
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String field(String propName, int modality, long maxLength, String labelWidth, String fieldWidth) {
		return field(propName,modality,maxLength,"",labelWidth,fieldWidth);
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String field(String propName, int modality, long maxLength) {
		return field(propName,modality,maxLength,"","","");
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String field(String propName, int modality, long maxLength, String extraPar) {
		return field(propName,modality,maxLength,extraPar,"","");
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String field(String propName, int modality, String labelWidth, String fieldWidth) {
		return field(propName,modality,0,"",labelWidth,fieldWidth);
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String field(String propName,int modality) {
		return field(propName,modality,0,"","","");
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String field(String propName, int modality, String extraPar) {
		return field(propName,modality,0,extraPar,"","");
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String field(String propName, int modality, String extraPar, String labelWidth, String fieldWidth) {
		return field(propName,modality,0,extraPar,labelWidth,fieldWidth);
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public abstract String field(String propName, int modality, long maxLength, String extraPar, String labelWidth, String fieldWidth);

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String tableField(String propName) {
		return tableField( propName,  null, null, "");
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String tableField(String propName, String extraPar) {
		return tableField( propName,  null, null, extraPar);
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String tableField(String propName, String[] viewPropertyNames) {
		return tableField( propName,  viewPropertyNames, null, "");
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String tableField(String propName, String[] viewPropertyNames, String extraPar) {
		return tableField( propName, viewPropertyNames, null, extraPar);
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String tableField(String propName, String[] viewPropertyNames, String[] colWidths) {
		return tableField( propName,  viewPropertyNames, colWidths, "");
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public abstract String tableField(String propName, String[] viewPropertyNames, String[] colWidths, String extraPar);

	/**************************************************************************************************/
	/**************************************************************************************************/
	public abstract String gridField(String propName, String parameters);

	/**************************************************************************************************/
	/**************************************************************************************************/
	public double getLabelPosition() {
		return labelPosition;
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setLabelPosition(double labelPosition) {
		this.labelPosition = labelPosition;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getLabelAlign() {
		return labelAlign;
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setLabelAlign(String labelAlign) {
		this.labelAlign = labelAlign;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getFieldAlign() {
		return fieldAlign;
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setFieldAlign(String fieldAlign) {
		this.fieldAlign = fieldAlign;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public int getModality() {
		return modality;
	}
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setModality(int modality) {
		this.modality = modality;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String action(String actionName, String onExecute){
		return action(actionName,onExecute,null,true,template.getProperty(pageName+actionName));
	}
			
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String action(String actionName, String onExecute, boolean enabled){
		return action(actionName,onExecute,null,enabled,template.getProperty(pageName+actionName));
	}
			
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String action(String actionName, String onExecute, String extraPar){
		return action(actionName,onExecute,extraPar,true,template.getProperty(pageName+actionName));
	}
		
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String action(String actionName, String onExecute, String extraPar, boolean enabled){
		return action(actionName,onExecute,extraPar,enabled,template.getProperty(pageName+actionName));
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public abstract String action(String actionName, String onExecute, String extraPar, 
						  		   boolean enabled, String actionText);
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setBorder(String border) {
		this.border = border;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getBorder() {
		return border;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getProblemsHelperAnchor(String anchorName, String propName) {
		try{
			AbstractType propValue = (AbstractType)Tools.getPropertyValue(getPageDataModel(),propName);
			return getProblemsHelperAnchor(anchorName,propName,propValue,TOP_RIGHT_ANCHOR);
		}catch(Exception e){
			LOG.error(e);
			return "";
		}
		
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getProblemsHelperAnchor(String propName, AbstractType propValue, int anchorPosition) {
		try{
			return getProblemsHelperAnchor(propName,propName,propValue,anchorPosition);
		}catch(Exception e){
			LOG.error(e);
			return "";
		}
		
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getProblemsHelperAnchor(String anchorName, String propName, int anchorPosition) {
		try{
			AbstractType propValue = (AbstractType)Tools.getPropertyValue(getPageDataModel(),propName);
			return getProblemsHelperAnchor(anchorName,propName,propValue,anchorPosition);
		}catch(Exception e){
			LOG.error(e);
			return "";
		}
		
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public abstract String getProblemsHelperAnchor(String anchorName, String propName, 
													 AbstractType propValue, int anchorPosition);
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public abstract String getInlineProblemsHelperAnchor(String propName, 
													 	 AbstractType propValue, int anchorPosition);
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getPageName() {
		return pageName;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setPageName(String pageName) {
		this.pageName = pageName.equals("") ? pageName : pageName+".";
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public CommandDataModel getPageDataModel() {
		return pageDataModel;
	}


	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getLabelStyle() {
		return labelStyle;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setLabelStyle(String labelStyle) {
		this.labelStyle = labelStyle;
	}

	/***********************************************************************************************/
	/***********************************************************************************************/
	public abstract String getComboElementsString(String propertyName);

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setPrefix(String prefix) {
		if(prefix == null)
			prefix = "";
		this.prefix = prefix;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void addPrefix(String prefix) {
		if(prefix == null)
			prefix = "";
		if(this.prefix.equals(""))
		    this.prefix = prefix;
		else
		    this.prefix += "_" + prefix;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getPrefix() {
		return prefix;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void savePrefix(){
		savedPrefix = getPrefix().toString();
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void restorePrefix(){
		setPrefix(savedPrefix.toString());
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	protected boolean isComboType(String propName){
		CommandDataModel dataModel = null;
		String modelPropName = propName;
		int sepIdx = propName.lastIndexOf("_");
		if(sepIdx >= 0){
			try{
				dataModel = (CommandDataModel)Tools.getPropertyValue(pageDataModel,propName.substring(0,sepIdx));
				modelPropName = propName.substring(sepIdx+1);
			}catch(Exception e){
				return false;
			}
		}else{
			dataModel = pageDataModel;
		}
		if(dataModel.getCodDescFields().get(modelPropName) != null)
			return true;
		return false;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	protected String resolveTemplateProperty(AbstractTypeError abstractTypeError){
		
		Template t;
		String key = abstractTypeError.toString();
		String msg = "";
		if(abstractTypeError instanceof TypeError)
			msg = ffTemplate.getProperty((TypeError)abstractTypeError);
		else if(abstractTypeError instanceof TypeWarning)
			msg = ffTemplate.getProperty((TypeWarning)abstractTypeError);
		else if(abstractTypeError instanceof TypeMessage)
			msg = ffTemplate.getProperty((TypeMessage)abstractTypeError);
		
		if(msg.equals(key)){
			t = template;
		}else{
			t = ffTemplate;
		}		
		
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
	public void setUpperCase(boolean upperCase) {
		this.ffUpperCase = upperCase;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public boolean getUpperCase() {
		return ffUpperCase;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public int getAnchorPosition() {
		return ffAnchorPosition;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setAnchorPosition(int anchorPosition) {
		this.ffAnchorPosition = anchorPosition;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setJSCombo(boolean jsCombo) {
	    this.jsCombo = jsCombo;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public boolean getJSCombo() {
	    return jsCombo;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getLabelCodePrefix() {
		return labelCodePrefix;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setLabelCodePrefix(String labelCodePrefix) {
		this.labelCodePrefix = labelCodePrefix;
	}

}
