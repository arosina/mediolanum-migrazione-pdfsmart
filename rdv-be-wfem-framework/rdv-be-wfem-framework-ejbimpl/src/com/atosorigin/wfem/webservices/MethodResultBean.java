package com.atosorigin.wfem.webservices;

import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;

import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.layout.Template;
import com.atosorigin.wfem.layout.TemplatePropertiesReader;
import com.atosorigin.wfem.types.AbstractTypeError;
import com.atosorigin.wfem.types.TypeError;
import com.atosorigin.wfem.types.TypeWarning;
import com.atosorigin.wfem.util.Tools;

/***********************************************************************************************/
/***********************************************************************************************/
public abstract class MethodResultBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public static int ERRORS_ON_PROPS	=  1;
	public static int OK 				=  0;
	public static int EXCEPTION 		= -1;
	public static int DAO_EXCEPTION 	= -2;
	
	private int 				resultCode 		= OK;
	private String 				resultMessage 	= "";
	private PropertyErrors[]	resultErrors	= new PropertyErrors[0];

	/***********************************************************************************************/
	/***********************************************************************************************/
	public void exception(Throwable t){
		setResultCode(EXCEPTION);
		setResultMessage(t.toString());		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public void daoException(DAOException daoe){
		setResultCode(DAO_EXCEPTION);
		setResultMessage(daoe.toString());		
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean fillPropertyErrors(ClientSessionContext csc,	CommandDataModel dataModel) throws Throwable{
		return fillPropertyErrors(csc, dataModel, null);
	}
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public boolean fillPropertyErrors(ClientSessionContext csc,	CommandDataModel dataModel, 
									  ArrayList<String> propsToIgnore) throws Throwable{
		
		Properties properties = new Properties();
		InputStream inputStream = this.getClass().getResourceAsStream("/"+csc.getApplCode()+"_"+csc.getLangCode()+".properties");
		if(inputStream != null){
			properties.load(inputStream);
			inputStream.close();
		}
		
		InputStream wfemInputStream = this.getClass().getResourceAsStream("/"+Template.CONTROLLER_APPL_CODE+"_"+csc.getLangCode()+".properties");
		if(wfemInputStream != null){
			Properties wfemProperties = new Properties();
			wfemProperties.load(wfemInputStream);
			wfemInputStream.close();
			properties.putAll(wfemProperties);
		}
		
		TemplatePropertiesReader pr = new TemplatePropertiesReader(properties);
		
		Map<String, ArrayList> modelErrors = Tools.modelErrorMap(dataModel);
		if(modelErrors.isEmpty())
			return false;
		
		ArrayList<PropertyErrors> errors = new ArrayList<PropertyErrors>();
		for(Map.Entry<String, ArrayList> entry : modelErrors.entrySet()){
			
			PropertyErrors propErrors = new PropertyErrors();
			String propName = entry.getKey();
			if(propsToIgnore != null && propsToIgnore.contains(propName))
				continue;
			
			propErrors.setPropName(propName);

			ArrayList<PropertyError> war = new ArrayList<PropertyError>();
			ArrayList<PropertyError> err = new ArrayList<PropertyError>();
			ArrayList<AbstractTypeError> typeErrors = entry.getValue();
			for(AbstractTypeError el : typeErrors){
				PropertyError pe = new PropertyError();
				pe.setCode(el.getKey());
				if(el instanceof TypeError){
					pe.setDescr(Tools.stringToXMLString(pr.getProperty((TypeError)el)));
					err.add(pe);
				}else if(el instanceof TypeWarning){
					pe.setDescr(Tools.stringToXMLString(pr.getProperty((TypeWarning)el)));
					war.add(pe);
					if(pe.getCode().equals(pe.getDescr()))
						pe.setDescr(null);
				}
			}
			
			propErrors.setPropWarnings(war.toArray(new PropertyError[0]));
			propErrors.setPropErrors(err.toArray(new PropertyError[0]));
			errors.add(propErrors);
		}
		setResultErrors(errors.toArray(new PropertyErrors[0]));
		if(errors.size() > 0){
			setResultCode(MethodResultBean.ERRORS_ON_PROPS);
			setResultMessage("Errors on props");
		}
		return true;
	}

	public int getResultCode() {
		return resultCode;
	}

	public void setResultCode(int resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public void setResultMessage(String resultMessage) {
		this.resultMessage = resultMessage;
	}

	public PropertyErrors[] getResultErrors() {
		return resultErrors;
	}

	public void setResultErrors(PropertyErrors[] resultErrors) {
		this.resultErrors = resultErrors;
	}

}
