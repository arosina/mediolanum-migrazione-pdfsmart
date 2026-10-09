package prgm.pdfwebforms.dataentryutil;

import java.io.UnsupportedEncodingException;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.GenericCommandResponseModel;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ListType;

import prgm.pdfwebforms.model.PdfModel;

/********************************************************************************/
/********************************************************************************/
public abstract class AbstractAutocompleteCommand  extends BusinessCommand{

	protected abstract ListType findElements(ClientSessionContext csc,  CommandDataModel dataModel) throws DAOException;
	protected abstract String	getJsonElementProps(CommandDataModel dataModel, String autocompleteFieldName);
	protected abstract String	getNoElementsIndicator(String autocompleteFieldName);
	
	/********************************************************************************/
	/********************************************************************************/
    public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
    	
		try{
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			String autocompleteFieldName = "";
			String noElementsIndicator = "";
			if(dataModel instanceof PdfModel){
				PdfModel model = (PdfModel)dataModel;
				autocompleteFieldName = model.getAutocompleteFieldName().toString();
				noElementsIndicator = model.getNoElementsIndicator().toString();
			}else{
				AbstractAutocompleteModel model = (AbstractAutocompleteModel)dataModel;
				autocompleteFieldName = model.getAutocompleteFieldName().toString();
				noElementsIndicator = model.getNoElementsIndicator().toString();
			}
			ListType lista = findElements(csc, dataModel);
			if(lista.size() == 0){
				if(noElementsIndicator.equalsIgnoreCase("none")){
					jsonNoElementsResponse("");
					return null;
				}
				if(noElementsIndicator.length() == 0)
					noElementsIndicator = getNoElementsIndicator(autocompleteFieldName);
				 if(noElementsIndicator != null && noElementsIndicator.length() > 0){
					jsonNoElementsResponse(noElementsIndicator);
				 }else{
					jsonNoElementsResponse("");
				 }
				 return null;
			}
			jsonResponse(lista, autocompleteFieldName);
			return null;
			
		}catch(DAOException daoe){
			return errorJsonResponse("Errore DB "+daoe.getErrorCode()+" nel recuperare i dati");
		}catch(Exception e){
			return errorJsonResponse("Errore di sistema nel recuperare i dati");
		}
    	
    }

    /********************************************************************************/
    /********************************************************************************/
	private void jsonNoElementsResponse(String noElementsIndicator){
		
		StringBuffer jsonObj = new StringBuffer("[");
		if(noElementsIndicator != null && noElementsIndicator.length() > 0){
			jsonObj.append("{");
			jsonObj.append("\"value\":\"\",");		
			jsonObj.append("\"label\":\""+noElementsIndicator+"\"");			
			jsonObj.append("}");
		}
		jsonObj.append("]");
		
		GenericCommandResponseModel gcrm = new GenericCommandResponseModel();
		gcrm.setContentType("application/json");
		try{
			gcrm.setContent(jsonObj.toString().getBytes("UTF-8"));
		}catch(UnsupportedEncodingException uee){
			gcrm.setContent(jsonObj.toString().getBytes());
		}
		gcrm.setContentLength(jsonObj.length());
		this.setGenericCommandResponse(gcrm);
	}
    
    /********************************************************************************/
    /********************************************************************************/
	private void jsonResponse(ListType elements, String autocompleteFieldName){
		
		StringBuffer jsonObj = new StringBuffer("[");
		for(int i=0;i<elements.size();i++){
			CommandDataModel el = elements.get(i);
			jsonObj.append("{");
			jsonObj.append(getJsonElementProps(el, autocompleteFieldName));
			jsonObj.append("}");
			if(i<elements.size()-1)
				jsonObj.append(",");
		}
		jsonObj.append("]");
		
		GenericCommandResponseModel gcrm = new GenericCommandResponseModel();
		gcrm.setContentType("application/json; charset=UTF-8");
		try{
			gcrm.setContent(jsonObj.toString().getBytes("UTF-8"));
		}catch(UnsupportedEncodingException uee){
			gcrm.setContent(jsonObj.toString().getBytes());
		}
		gcrm.setContentLength(jsonObj.length());
		this.setGenericCommandResponse(gcrm);
	}
    
    /********************************************************************************/
    /********************************************************************************/
    private CommandDataModel errorJsonResponse(String error){
    	String jsonObj =  "[{\"value\":\"\", \"label\":\""+error+"\"}]";
    	
		GenericCommandResponseModel gcrm = new GenericCommandResponseModel();
		gcrm.setContentType("application/json");
		try{
			gcrm.setContent(jsonObj.toString().getBytes("UTF-8"));
		}catch(UnsupportedEncodingException uee){
			gcrm.setContent(jsonObj.toString().getBytes());
		}
		gcrm.setContentLength(jsonObj.length());
		this.setGenericCommandResponse(gcrm);
		return null;
    }
    
}
