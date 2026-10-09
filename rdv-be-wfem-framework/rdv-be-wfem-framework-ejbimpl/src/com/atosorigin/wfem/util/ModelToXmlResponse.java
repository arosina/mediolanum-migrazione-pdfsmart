package com.atosorigin.wfem.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandError;
import com.atosorigin.wfem.command.CommandWarning;
import com.atosorigin.wfem.types.AbstractType;
import com.atosorigin.wfem.types.AbstractTypePropertyDescriptor;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.types.StringType;
import com.atosorigin.wfem.types.TypeError;
import com.atosorigin.wfem.types.TypeWarning;

/********************************************************************************/
/********************************************************************************/
public class ModelToXmlResponse {

	private class XmlPropErrorObject{
		ArrayList<TypeError> errors = new ArrayList<TypeError>();
		ArrayList<TypeWarning> warnings = new ArrayList<TypeWarning>();
	}
	
	private class XmlModelObject{
		Map<String, XmlPropErrorObject> modelPropErrors = new HashMap<String, XmlPropErrorObject>();
		
		void addPropError(String propName, TypeError error){
			XmlPropErrorObject propErrors = modelPropErrors.get(propName);
			if(propErrors == null){
				propErrors = new XmlPropErrorObject();
				modelPropErrors.put(propName, propErrors);
			}
			propErrors.errors.add(error);
		}

		void addPropWarning(String propName, TypeWarning error){
			XmlPropErrorObject propErrors = modelPropErrors.get(propName);
			if(propErrors == null){
				propErrors = new XmlPropErrorObject();
				modelPropErrors.put(propName, propErrors);
			}
			propErrors.warnings.add(error);
		}
	}
	
	/********************************************************************************/
	/********************************************************************************/
	public String xmlFromModel(CommandDataModel model) throws Exception{

		String errors = "";
		if(model.hasCommandErrors())
			errors=" errors=\"true\"";
		
		String xmlString = "";
		xmlString += "<model"+errors+">\n";
		XmlModelObject xmlModelObject = new  XmlModelObject();
		xmlString += transformModelToXmlNode(model," ",xmlModelObject);
		xmlString += transformPropErrorsXml(xmlModelObject," ");
		xmlString += transformModelErrorsXml(model," ");
		xmlString += "</model>\n";
		return xmlString;
	}
	
	/********************************************************************************/
	/********************************************************************************/
	private String transformModelToXmlNode(CommandDataModel model, String ident, XmlModelObject xmlModelObject) throws Exception{
		
		String xmlString = "";
		String newIdent = ident + " ";
		
		try{
			AbstractTypePropertyDescriptor[] props = ObjectsPropertiesCache.getInstance().getAbstractTypesPropertyDescriptors(model);
			if(props == null)
				return xmlString;
				
			for(int i=0;i<props.length;i++){
				String propName = props[i].getName();
				Object propValue = Tools.getPropertyValue(model,propName);
				if(propValue == null)
					continue;
				
				if(propValue instanceof AbstractType){
					String errors = "";
					String warnings = "";
					AbstractType v = (AbstractType)propValue;
					if(v.hasTypeErrors()){
						for(int j=0;j<v.getTypeErrors().size();j++){
							TypeError e = (TypeError)v.getTypeErrors().get(j);
							xmlModelObject.addPropError(propName, e);
						}
						errors=" errors=\"true\"";
					}else if(v.hasTypeWarnings()){
						for(int j=0;j<v.getTypeWarnings().size();j++){
							TypeWarning w = (TypeWarning)v.getTypeWarnings().get(j);
							xmlModelObject.addPropWarning(propName, w);
						}
						warnings=" warnings=\"true\"";
					}else if(v.isNull()){
						continue;
					}
					if(propValue instanceof StringType){
						String value = propValue.toString();
						xmlString += newIdent+"<"+propName+errors+warnings+">"+Tools.stringToXMLString(value)+"</"+propName+">\n";
					}else
						xmlString += newIdent+"<"+propName+errors+warnings+">"+propValue.toString()+"</"+propName+">\n";
					continue;
				}
				
				if(propValue instanceof CommandDataModel){
					String errors = "";
					if(((CommandDataModel) propValue).hasCommandErrors())
						errors=" errors=\"true\"";
					xmlString += newIdent+"<"+propName+errors+">\n";
					XmlModelObject newXmlModelObject = new XmlModelObject();
					xmlString += transformModelToXmlNode((CommandDataModel)propValue,newIdent,newXmlModelObject);
					xmlString += transformPropErrorsXml(newXmlModelObject,newIdent);
					xmlString += transformModelErrorsXml((CommandDataModel)propValue,newIdent);
					xmlString += newIdent+"</"+propName+">\n";
					continue;
				}

				if(propValue instanceof ListType){
					List list = ((ListType)propValue).getElements();
					if(list.size() == 0)
						continue;
						
					xmlString += newIdent+"<"+propName+">\n";
					for(int j=0;j<list.size();j++){
						xmlString += newIdent+" <"+propName+"Element>\n";
						CommandDataModel listElement = (CommandDataModel)list.get(j);
						XmlModelObject newXmlModelObject = new XmlModelObject();
						xmlString += transformModelToXmlNode(listElement,newIdent+" ",newXmlModelObject);
						xmlString += transformPropErrorsXml(newXmlModelObject,newIdent+" ");
						xmlString += transformModelErrorsXml(listElement,newIdent);
						xmlString += newIdent+" </"+propName+"Element>\n";
					}
					xmlString += newIdent+"</"+propName+">\n";
					continue;
				}
			}
			return xmlString;
			
		}catch(Exception e){
			String errMsg = "ModelToXmlResponse.transformModelToXmlNode: Exception in transforming into xml CommandDataModel ["+model+"]: "+e;
			e = new Exception(errMsg);
			throw e;
		}
	}
	
	
	/********************************************************************************/
	/********************************************************************************/
	private String transformPropErrorsXml(XmlModelObject xmlModelObject, String ident) throws Exception{
		
		if(xmlModelObject.modelPropErrors.isEmpty())
			return "";
		
		StringBuffer res = new StringBuffer();
		Iterator<String> it = xmlModelObject.modelPropErrors.keySet().iterator();
		res.append(ident+" <propErrors>\n");
		while (it.hasNext()) {
			
			String propName = (String)it.next();
			res.append(ident+"  <propErrorsElement>\n");
			res.append(ident+"   <propName>"+propName+"</propName>\n");
			
			XmlPropErrorObject xmlPropErrorObject = xmlModelObject.modelPropErrors.get(propName);
			
			if(xmlPropErrorObject.errors.size() > 0){
				res.append(ident+"    <errors>\n");
				for(TypeError e : xmlPropErrorObject.errors){
					res.append(ident+"     <code>"+Tools.stringToXMLString(e.getKey())+"</code>\n");
				}
				res.append(ident+"    </errors>\n");
			}
			
			if(xmlPropErrorObject.warnings.size() > 0){
				res.append(ident+"    <warnings>\n");
				for(TypeWarning w : xmlPropErrorObject.warnings){
					res.append(ident+"     <code>"+Tools.stringToXMLString(w.getKey())+"</code>\n");
				}
				res.append(ident+"    </warnings>\n");
			}
			
			res.append(ident+"  </propErrorsElement>\n");
		}
		res.append(ident+" </propErrors>\n");
		return res.toString();
	}

	/********************************************************************************/
	/********************************************************************************/
	private String transformModelErrorsXml(CommandDataModel model, String ident) throws Exception{
		if(!model.hasCommandErrors() && !model.hasCommandWarnings())
			return "";
		
		StringBuffer res = new StringBuffer();
		res.append(ident+" <modelErrors>\n");
		if(model.getCommandErrors().size() > 0){
			res.append(ident+"  <errors>\n");
			for(int i=0;i<model.getCommandErrors().size();i++){
				CommandError e = (CommandError)model.getCommandErrors().get(i);
				res.append(ident+"  <code>"+Tools.stringToXMLString(e.toString())+"</code>\n");
			}
			res.append(ident+"  </errors>\n");
		}
		if(model.getCommandWarnings().size() > 0){
			res.append(ident+"  <warnings>\n");
			for(int i=0;i<model.getCommandWarnings().size();i++){
				CommandWarning w = (CommandWarning)model.getCommandWarnings().get(i);
				res.append(ident+"  <code>"+Tools.stringToXMLString(w.toString())+"</code>\n");
			}
			res.append(ident+"  </warnings>\n");
		}
		res.append(ident+" </modelErrors>\n");
		return res.toString();
	}
}
