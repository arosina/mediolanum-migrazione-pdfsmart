package prgm.pdfwebforms.catalog.dataentryutil;

import prgm.pdfwebforms.catalog.PdfCatalogModel;

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

/********************************************************************************/
/********************************************************************************/
public class DescrizioneAutocomplete extends BusinessCommand {
	
	/********************************************************************************/
	/********************************************************************************/
    public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
    	
		try{
			
			ClientSessionContext csc = userSessionContext.getClientSessionContext();
			PdfCatalogModel model = (PdfCatalogModel)dataModel;

			ListType els = new DAOObject(csc,"PdfWebForms.PdfCatalog").executeQueryAccess("descrizioneAutocomplete",model.getPdfListParams()).getResult();
			StringBuffer jsonObj = new StringBuffer("[");
			for(int i=0;i<els.size();i++){
				DescrizioneAutocompleteModel el = (DescrizioneAutocompleteModel)els.get(i);
				
				String descr = el.getDescrizione().toString().replaceAll("\\\"","\\\\\"");
				
				jsonObj.append("{");
				jsonObj.append("\"codice\":\""+el.getCodice()+"\",");
				jsonObj.append("\"descrizione\":\""+descr+"\",");
				
				jsonObj.append("\"value\":\"<b>"+el.getCodice()+"</b> - "+Tools.capitalize(descr)+"\"");
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
			CommandException ce = new CommandException(daoe.toString());
			LOG.error(ce);
			throw ce;
		}catch(Exception e){
			CommandException ce = new CommandException(e.toString());
			LOG.error(ce);
			throw ce;
		}
    	
    }

    /********************************************************************************/
    /********************************************************************************/
    public Class getInputViewClass() {
        return PdfCatalogModel.class;
    }
}