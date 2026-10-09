package prgm.pdfwebforms.dataentryutil;

import java.io.UnsupportedEncodingException;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.GenericCommandResponseModel;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.DAOObject;
import com.atosorigin.wfem.dao.exceptions.DAOException;
import com.atosorigin.wfem.types.ListType;
import com.atosorigin.wfem.util.Tools;

/*******************************************************************/
/*******************************************************************/
public class PdfDescrAutocomplete extends BusinessCommand {
	
	/********************************************************************************/
	/********************************************************************************/
    public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
    	
		try{
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfDescrAutocompleteModel model = (PdfDescrAutocompleteModel)dataModel;

			ListType els = new DAOObject(csc,"PdfWebForms.PdfList").executeQueryAccess("pdfDescrAutocomplete",model).getResult();
			StringBuffer jsonObj = new StringBuffer("[");
			for(int i=0;i<els.size();i++){
				PdfDescrAutocompleteModel el = (PdfDescrAutocompleteModel)els.get(i);
				
				String descr = el.getPdfDescr().toString().replaceAll("\\\"","\\\\\"");
				
				jsonObj.append("{");
				jsonObj.append("\"pdfCode\":\""+el.getPdfCode()+"\",");
				jsonObj.append("\"pdfDescr\":\""+descr+"\",");
				
				jsonObj.append("\"value\":\""+Tools.capitalize(descr)+"\",");
				jsonObj.append("\"label\":\"<b>"+el.getPdfCode()+"</b> - "+Tools.capitalize(descr)+"\"");
				jsonObj.append("}");
				if(i<els.size()-1)
					jsonObj.append(",");
			}
			jsonObj.append("]");
			
			GenericCommandResponseModel gcrm = new GenericCommandResponseModel();
			gcrm.setContentType("application/json");
			gcrm.setContent(jsonObj.toString().getBytes("UTF-8"));
			gcrm.setContentLength(jsonObj.length());
			this.setGenericCommandResponse(gcrm);
			return null;
			
		}catch(DAOException daoe){
			return errorJsonObj("Errore DB "+daoe.getErrorCode()+" nel recuperare i dati");
		}catch(Exception e){
			return errorJsonObj("Errore di sistema nel recuperare i dati");
		}
    	
    }

    /********************************************************************************/
    /********************************************************************************/
    public Class getInputViewClass() {
        return PdfDescrAutocompleteModel.class;
    }

    /********************************************************************************/
    /********************************************************************************/
    private CommandDataModel errorJsonObj(String error){
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