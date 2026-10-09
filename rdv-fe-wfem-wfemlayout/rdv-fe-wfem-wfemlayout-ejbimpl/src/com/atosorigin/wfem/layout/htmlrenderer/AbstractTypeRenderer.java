package com.atosorigin.wfem.layout.htmlrenderer;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.layout.field.FieldModel;
import com.atosorigin.wfem.loggers.AbstractLogger;
import com.atosorigin.wfem.loggers.LayoutLogger;

/**************************************************************************************************/
/**************************************************************************************************/
public abstract class AbstractTypeRenderer {

	protected static AbstractLogger LOG = LayoutLogger.getInstance();

	private FieldModel fieldModel;
	private String webApp;
	private String modelPropName;
	private CommandDataModel dataModel;
	private Template template;
	private String pageName;

	public AbstractTypeRenderer(
		FieldModel fieldModel,
		String webApp,
		String modelPropName,
		CommandDataModel dataModel) {

		this(fieldModel, webApp, modelPropName, dataModel, null, null);
	}

	public AbstractTypeRenderer(
		FieldModel fieldModel,
		String webApp,
		String modelPropName,
		CommandDataModel dataModel,
		Template template,
		String pageName) {

		setFieldModel(fieldModel);
		setWebApp(webApp);
		setModelPropName(modelPropName);
		setDataModel(dataModel);
		setTemplate(template);
		setPageName(pageName);
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public abstract String getFieldRendering() throws Exception;

	/**************************************************************************************************/
	/**************************************************************************************************/
	public FieldModel getFieldModel() {
		return fieldModel;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setFieldModel(FieldModel fieldModel) {
		this.fieldModel = fieldModel;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public CommandDataModel getDataModel() {
		return dataModel;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getModelPropName() {
		return modelPropName;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getWebApp() {
		return webApp;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setDataModel(CommandDataModel dataModel) {
		this.dataModel = dataModel;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setModelPropName(String modelPropName) {
		this.modelPropName = modelPropName;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setWebApp(String webApp) {
		this.webApp = webApp;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public String getPageName() {
		return pageName;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public Template getTemplate() {
		return template;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	/**************************************************************************************************/
	/**************************************************************************************************/
	public void setTemplate(Template template) {
		this.template = template;
	}

}
