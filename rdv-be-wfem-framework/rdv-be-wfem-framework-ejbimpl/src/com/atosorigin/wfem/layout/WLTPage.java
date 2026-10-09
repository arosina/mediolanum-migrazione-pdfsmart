package com.atosorigin.wfem.layout;

/** ************************************************************************************************
 * @author: Ricotti Corrado
************************************************************************************************ **/
public abstract class WLTPage implements WLTPageIntf{

	protected Template template;
	protected Template ffTemplate;
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public void init(Template template){
	
		this.template = template;
		
		ffTemplate = new Template(template.getLangCode(),null,template.isTemplateCacheEnabled(),template.getRequest());
		ffTemplate.setApplCode(Template.CONTROLLER_APPL_CODE);
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public Template getTemplate() {
		return template;
	}
	
	/**************************************************************************************************/
	/**************************************************************************************************/
	public Template getFfTemplate() {
		return ffTemplate;
	}
	
}
