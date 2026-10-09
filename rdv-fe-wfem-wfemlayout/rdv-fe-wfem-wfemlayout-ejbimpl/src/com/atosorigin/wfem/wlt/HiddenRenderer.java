package com.atosorigin.wfem.wlt;

import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.util.Tools;

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public class HiddenRenderer{
	
	private PageRenderer pageRenderer;
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public HiddenRenderer(PageRenderer pageRenderer){
		this.pageRenderer = pageRenderer;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public String html(String propName) {
		
		if(pageRenderer.getTemplate().getModality() == Template.PRINT_MODALITY)
			return "";
		
		Template t = pageRenderer.getTemplate();
		
		try{			
			Class propType = Tools.getPropertyType(t.getPageDataModel(),propName);
			if(propType == null){
				return "<script>alert('Error on creating hidden field "+propName+"');</script>";
			}

			String htmlValue = "";
			AbstractType propValue = (AbstractType)Tools.getPropertyValue(t.getPageDataModel(),propName);
			if(propValue != null)
				htmlValue = propValue.toString();
			
			if(htmlValue.indexOf('\'') >= 0)
				htmlValue = htmlValue.replaceAll("\'","&#39;");
			
			return "<input type='hidden' name='"+propName+"' id='"+propName+"' value='"+htmlValue+"'>\n";

		}catch(Exception e){
			return "<script>alert('Exception on creating hidden field "+propName+"');</script>";
		}
		
	}

}
