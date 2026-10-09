package prgm.pdfwebforms.mom;

import java.io.UnsupportedEncodingException;

import com.atosorigin.wfem.command.BusinessCommand;
import com.atosorigin.wfem.command.ClientSessionContext;
import com.atosorigin.wfem.command.CommandDataModel;
import com.atosorigin.wfem.command.CommandException;
import com.atosorigin.wfem.command.GenericCommandResponseModel;
import com.atosorigin.wfem.command.UserSessionContext;
import com.atosorigin.wfem.dao.exceptions.DAOException;

import prgm.pdfwebforms.dataentryutil.PersonAutocompleteModel;
import prgm.pdfwebforms.dataloader.DataLoader;
import prgm.pdfwebforms.model.PdfPersonModel;

/***********************************************************************************************/
/***********************************************************************************************/
public class RicercaClienteMomCallbackEvent extends BusinessCommand{
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public CommandDataModel execute(UserSessionContext userSessionContext, CommandDataModel dataModel) throws CommandException {
		
        ClientSessionContext csc = userSessionContext.getClientSessionContext();
        PersonAutocompleteModel model = (PersonAutocompleteModel)dataModel;         
        StringBuilder jsonObj = new StringBuilder("{");
        try{
			PdfPersonModel person = DataLoader.loadPersonOnAutocomplete(csc, model.getNdg().toString(), "", true);
			jsonObj.append(person.createJsonData("ndg"));
        }catch(DAOException daoe){}catch(Exception e){}
		jsonObj.append("}");
		
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
	
	/***********************************************************************************************/
	/***********************************************************************************************/
	public Class getInputViewClass() {
		return PersonAutocompleteModel.class;
	}
}
